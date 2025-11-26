package com.ys.hr.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrBenefit;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.mapper.HrLeaveMapper;
import com.ys.hr.service.IHrBenefitService;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrEmpBenefitMapper;
import com.ys.hr.domain.HrEmpBenefit;
import com.ys.hr.service.IHrEmpBenefitService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *   Employee Welfare Application Form Service Implementation
 *
 * @author ys
 * @date 2025-06-09
 */
@Service
public class HrEmpBenefitServiceImpl extends ServiceImpl<HrEmpBenefitMapper, HrEmpBenefit> implements IHrEmpBenefitService
{

    @Resource
    private RemoteMessageService remoteMessageService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private HrLeaveMapper hrLeaveMapper;

    @Resource
    private IHrBenefitService hrBenefitService;

    /**
     * Query Employee Welfare Application Form
     *
     * @param benefitEmpId   Employee Welfare Application Form primary key
     * @return Employee Welfare Application Form
     */
    @Override
    public HrEmpBenefit selectHrEmpBenefitByBenefitEmpId(String benefitEmpId)
    {
        return baseMapper.selectHrEmpBenefitByBenefitEmpId(benefitEmpId);
    }

    /**
     * Query Employee Welfare Application Form list
     *
     * @param hrEmpBenefit Employee Welfare Application Form
     * @return Employee Welfare Application Form
     */
    @Override
    public List<HrEmpBenefit> selectHrEmpBenefitList(HrEmpBenefit hrEmpBenefit)
    {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<HrEmpBenefit> hrEmpBenefits = new ArrayList<>();
        if ("1".equals(hrEmpBenefit.getSearchValue())) {
            LocalDate sevenDaysAgo = today.minusDays(7);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrEmpBenefit.setParams(timeMap);
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        } else if ("2".equals(hrEmpBenefit.getSearchValue())) {
            // Data from 14 days ago
            LocalDate sevenDaysAgo = today.minusDays(14);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrEmpBenefit.setParams(timeMap);
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        } else if ("3".equals(hrEmpBenefit.getSearchValue())) {
            // Data from 7 days ago
            LocalDate sevenDaysAgo = today.minusDays(30);
            HashMap<String, Object> timeMap = new HashMap<>();
            timeMap.put("beginCreateTime", sevenDaysAgo.format(formatter));
            timeMap.put("endCreateTime", today.format(formatter));
            hrEmpBenefit.setParams(timeMap);
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        } else {
            hrEmpBenefits = baseMapper.selectHrEmpBenefitList(hrEmpBenefit);
        }
        return hrEmpBenefits;
    }

    /**
     * Add Employee Welfare Application Form
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return Result
     */
    @Override
    @Transactional
    public int insertHrEmpBenefit(HrEmpBenefit hrEmpBenefit)
    {
        HrEmpBenefit hrEmpBenefit1 = new HrEmpBenefit();
        hrEmpBenefit1.setBenefitId(hrEmpBenefit.getBenefitId());
        hrEmpBenefit1.setUserId(SecurityUtils.getUserId());
        List<HrEmpBenefit> hrEmpBenefits = selectHrEmpBenefitList(hrEmpBenefit1);
        if(ObjectUtils.isNotEmpty(hrEmpBenefits)){
            throw new ServiceException("Employees cannot apply for the same benefit twice!!");
        }
        hrEmpBenefit.setUserId(SecurityUtils.getUserId());
        hrEmpBenefit.setBenefitStatus("1");
        hrEmpBenefit.setCreateTime(DateUtils.getNowDate());
        int i = baseMapper.insert(hrEmpBenefit);
        if(i>0){
            Long leader = hrLeaveMapper.selectLeader(hrEmpBenefit.getUserId());
            HrEmployees leaderEmp = hrEmployeesMapper.selectHrEmployeesById(leader);
            HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            HrBenefit hrBenefit = hrBenefitService.selectHrBenefitByBenefitId(hrEmpBenefit.getBenefitId());
            AjaxResult info = remoteMessageService.getInfo(SecurityUtils.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String salaryAndBenefitsNotifications = setting.get("salaryAndBenefitsNotifications");
            if(ObjectUtils.isNotEmpty(leaderEmp) && "1".equals(salaryAndBenefitsNotifications)){
                SysMessage sysMessage = getSysMessage(leaderEmp, hrEmployees, hrBenefit);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return i;
    }

    @NotNull
    private static SysMessage getSysMessage(HrEmployees leaderEmp, HrEmployees hrEmployees, HrBenefit hrBenefit) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageRecipient(leaderEmp.getUserId());
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageType(9);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("name", hrEmployees.getFullName());
        map1.put("benefitName", hrBenefit.getBenefitName());
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        return sysMessage;
    }

    /**
     * Update Employee Welfare Application Form
     *
     * @param hrEmpBenefit   Employee Welfare Application Form
     * @return Result
     */
    @Override
    @Transactional
    public int updateHrEmpBenefit(HrEmpBenefit hrEmpBenefit)
    {
        int i = baseMapper.updateById(hrEmpBenefit);
        if(i>0){
            AjaxResult info = remoteMessageService.getInfo(hrEmpBenefit.getUserId(), SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String salaryAndBenefitsNotifications = setting.get("salaryAndBenefitsNotifications");
            if("1".equals(salaryAndBenefitsNotifications)){
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrEmpBenefit.getUserId());
                HrBenefit hrBenefit = hrBenefitService.selectHrBenefitByBenefitId(hrEmpBenefit.getBenefitId());
                if("3".equals(hrEmpBenefit.getBenefitStatus())){
                    SysMessage sysMessage = buildMessage(hrEmpBenefit, "Refuse", hrEmployees, hrBenefit);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
                }else if("2".equals(hrEmpBenefit.getBenefitStatus())){
                    SysMessage sysMessage = buildMessage(hrEmpBenefit, "Pass", hrEmployees, hrBenefit);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
                }
            }
        }
        hrEmpBenefit.setUpdateTime(DateUtils.getNowDate());
        return i;
    }

    @NotNull
    private static SysMessage buildMessage(HrEmpBenefit hrEmpBenefit, String Refuse, HrEmployees hrEmployees, HrBenefit hrBenefit) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setMessageRecipient(hrEmpBenefit.getUserId());
        sysMessage.setMessageStatus("0");
        sysMessage.setMessageType(10);
        sysMessage.setCreateTime(DateUtils.getNowDate());
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("benefitStatus", Refuse);
        map1.put("name", hrEmployees.getFullName());
        map1.put("benefitName", hrBenefit.getBenefitName());
        sysMessage.setMap1(map1);
        sysMessage.setMap2(map2);
        return sysMessage;
    }

}
