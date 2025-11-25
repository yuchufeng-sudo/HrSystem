package com.ys.hr.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrTargetEmployee;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrTargetEmployeeService;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import com.ys.hr.mapper.HrTargetsMapper;
import com.ys.hr.domain.HrTargets;
import com.ys.hr.service.IHrTargetsService;
import com.ys.common.core.utils.DateUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Main table storing all target information Service Implementation
 *
 * @author ys
 * @date 2025-06-18
 */
@Service
@EnableCaching
public class HrTargetsServiceImpl extends ServiceImpl<HrTargetsMapper, HrTargets> implements IHrTargetsService {

    @Resource
    private IHrTargetEmployeeService hrTargetEmployeeService;

    @Resource
    private HrEmployeesMapper hrEmployeesMapper;

    @Resource
    private RemoteMessageService remoteMessageService;

    /**
     * Query Main table storing all target information
     *
     * @param id Main table storing all target information primary key
     * @return Main table storing all target information
     */
    @Override
    public HrTargets selectHrTargetsById(Long id) {
        return baseMapper.selectHrTargetsById(id);
    }

    /**
     * Query Main table storing all target information list
     *
     * @param hrTargets Main table storing all target information
     * @return Main table storing all target information
     */
    @Override
    public List<HrTargets> selectHrTargetsList(HrTargets hrTargets) {
        return baseMapper.selectHrTargetsList(hrTargets);
    }

    /**
     * Add Main table storing all target information
     *
     * @param hrTargets Main table storing all target information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HrTargets insertHrTargets(HrTargets hrTargets) {
        hrTargets.setCreateTime(DateUtils.getNowDate());
        int insert = baseMapper.insert(hrTargets);
        batchSaveTargetEmployee(hrTargets);
        if (insert > 0) {
            Long[] employeeIds = hrTargets.getEmployeeIds();
            if (ObjectUtils.isNotEmpty(employeeIds)) {
                for (Long employeeId : employeeIds) {
                    HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesById(employeeId);
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(hrEmployees.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(15);
                    sysMessage.setCreateTime(DateUtils.getNowDate());
                    Map<String, Object> map1 = new HashMap<>();
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("id", hrTargets.getId());
                    map1.put("title", hrTargets.getName());
                    sysMessage.setMap1(map1);
                    sysMessage.setMap2(map2);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
                }
            }
        }
        return hrTargets;
    }

    private void batchSaveTargetEmployee(HrTargets hrTargets) {
        Long[] employeeIds = hrTargets.getEmployeeIds();
        if (employeeIds != null && employeeIds.length != 0) {
            List<HrTargetEmployee> list = new ArrayList<>();
            for (Long employeeId : employeeIds) {
                HrTargetEmployee hrTargetEmployee = new HrTargetEmployee();
                hrTargetEmployee.setEmployeeId(employeeId);
                hrTargetEmployee.setTargetId(hrTargets.getId());
                hrTargetEmployee.setCreateTime(DateUtils.getNowDate());
                hrTargetEmployee.setType(1L);
                list.add(hrTargetEmployee);
            }
            hrTargetEmployeeService.saveBatchEmployees(list);
        }
    }

    /**
     * Update Main table storing all target information
     *
     * @param hrTargets Main table storing all target information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HrTargets updateHrTargets(HrTargets hrTargets) {
        hrTargets.setUpdateTime(DateUtils.getNowDate());
        int rows = baseMapper.updateById(hrTargets);
        if (rows == 0) {
            throw new OptimisticLockingFailureException("Update failed. The target information has been modified by another user. Please refresh and try again");
        }
        Long[] employeeIds = hrTargets.getEmployeeIds();
        if (employeeIds != null && employeeIds.length != 0) {
            QueryWrapper<HrTargetEmployee> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("target_id",hrTargets.getId());
            queryWrapper.notIn("employee_id",hrTargets.getEmployeeIds());
            hrTargetEmployeeService.remove(queryWrapper);
        }
        batchSaveTargetEmployee(hrTargets);
        return hrTargets;
    }

    /**
     * Delete Main table storing all target information
     *
     * @param id Main table storing all target information primary key
     * @return Result
     */
    @Override
    public int deleteHrTargetsById(Long id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public List<HrTargetEmployee> selectHrTargets(HrTargets hrTargets) {
        List<HrTargetEmployee> hrTargetEmployees = null;
        // Select User Role
        String role = baseMapper.selectUserRole(SecurityUtils.getUserId());
        if ("admin".equals(role)) {
            QueryWrapper<HrEmployees> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", SecurityUtils.getUserId());
            queryWrapper.select("employee_id");
            HrEmployees hrEmployees = hrEmployeesMapper.selectOne(queryWrapper);
            hrTargets.setUserId(hrEmployees.getEmployeeId());
            hrTargets.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
            hrTargetEmployees  = hrTargetEmployeeService.selectEmployeeTargetsByAdmin(hrTargets);
        }else {
            QueryWrapper<HrEmployees> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", SecurityUtils.getUserId());
            queryWrapper.select("employee_id");
            HrEmployees hrEmployees = hrEmployeesMapper.selectOne(queryWrapper);
            hrTargets.setUserId(hrEmployees.getEmployeeId());
            hrTargetEmployees  = hrTargetEmployeeService.selectEmployeeTargets(hrTargets);
        }
        for (HrTargetEmployee hrTargetEmployee : hrTargetEmployees) {
            if ("0".equals(hrTargetEmployee.getIsStart()) || "3".equals(hrTargetEmployee.getIsStart()) || hrTargetEmployee.getIsAssigned() == 0) {
                hrTargetEmployee.setStartStatus(false);
            } else {
                hrTargetEmployee.setStartStatus(true);
            }
            if (hrTargetEmployee.getIsAssigned() == 0){
                hrTargetEmployee.setTargetExecutionTime("00:00:00");
            }else {
                long hh = hrTargetEmployee.getExecutionSecond() / 3600;
                long mm = (hrTargetEmployee.getExecutionSecond() % 3600) / 60;
                long ss = hrTargetEmployee.getExecutionSecond() % 60;
                hrTargetEmployee.setTargetExecutionTime(String.format("%02d:%02d:%02d", hh, mm, ss));
            }
        }
        return hrTargetEmployees;
    }

}
