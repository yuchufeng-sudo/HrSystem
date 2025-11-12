package com.ys.hr.controller;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrBenefit;
import com.ys.hr.domain.HrEmpBenefit;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.mapper.HrLeaveMapper;
import com.ys.hr.service.IHrEmpBenefitService;
import com.ys.hr.service.impl.HrBenefitServiceImpl;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *   EMPLOYEE WELFARE APPLICATION FORM Controller
 *
 * @author ys
 * @date 2025-06-09
 */
@RestController
@RequestMapping("/empBenefit")
public class HrEmpBenefitController extends BaseController
{
    @Autowired
    private IHrEmpBenefitService hrEmpBenefitService;

    @Resource
    private RemoteMessageService remoteMessageService;

    @Autowired
    private HrEmployeesMapper hrEmployeesMapper;

    @Autowired
    private HrBenefitServiceImpl hrBenefitService;

    @Resource
    private HrLeaveMapper hrLeaveMapper;
    /**
     * Query   EMPLOYEE WELFARE APPLICATION FORM list
     */
    @RequiresPermissions("hr:empBenefit:list")
    @GetMapping("/list")
    public TableDataInfo list(HrEmpBenefit hrEmpBenefit)
    {
        if(ObjectUtils.isNotEmpty(hrEmpBenefit.getFlag())){
            hrEmpBenefit.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        }else{
            hrEmpBenefit.setUserId(SecurityUtils.getUserId());
        }
        startPage();
        List<HrEmpBenefit> list = hrEmpBenefitService.selectHrEmpBenefitList(hrEmpBenefit);
        return getDataTable(list);
    }

    /**
     * Export   EMPLOYEE WELFARE APPLICATION FORM list
     */
    @RequiresPermissions("hr:empBenefit:export")
    @Log(title = "  EMPLOYEE WELFARE APPLICATION FORM", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmpBenefit hrEmpBenefit)
    {
        List<HrEmpBenefit> list = hrEmpBenefitService.selectHrEmpBenefitList(hrEmpBenefit);
        ExcelUtil<HrEmpBenefit> util = new ExcelUtil<HrEmpBenefit>(HrEmpBenefit.class);
        util.exportExcel(response, list, "  EMPLOYEE WELFARE APPLICATION FORM Data");
    }

    /**
     * Get   EMPLOYEE WELFARE APPLICATION FORM details
     */
    @GetMapping(value = "/{benefitEmpId}")
    public AjaxResult getInfo(@PathVariable("benefitEmpId") String benefitEmpId) {
        return success(hrEmpBenefitService.selectHrEmpBenefitByBenefitEmpId(benefitEmpId));
    }

    /**
     * Add   EMPLOYEE WELFARE APPLICATION FORM
     */
    @RequiresPermissions("hr:empBenefit:add")
    @Log(title = "  EMPLOYEE WELFARE APPLICATION FORM", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmpBenefit hrEmpBenefit) {
        HrEmpBenefit hrEmpBenefit1 = new HrEmpBenefit();
        hrEmpBenefit1.setBenefitId(hrEmpBenefit.getBenefitId());
        hrEmpBenefit1.setUserId(SecurityUtils.getUserId());
        List<HrEmpBenefit> hrEmpBenefits = hrEmpBenefitService.selectHrEmpBenefitList(hrEmpBenefit1);
        if(ObjectUtils.isNotEmpty(hrEmpBenefits)){
            return AjaxResult.error("Employees cannot apply for the same benefit twice!!");
        }
        hrEmpBenefit.setUserId(SecurityUtils.getUserId());
        hrEmpBenefit.setBenefitStatus("1");
        int i = hrEmpBenefitService.insertHrEmpBenefit(hrEmpBenefit);
        if(i>0){
            Long leader = hrLeaveMapper.selectLeader(hrEmpBenefit.getUserId());
            HrEmployees leaderEmp = hrEmployeesMapper.selectHrEmployeesById(leader);
            HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
            HrBenefit hrBenefit = hrBenefitService.selectHrBenefitByBenefitId(hrEmpBenefit.getBenefitId());
            AjaxResult info = remoteMessageService.getInfo(SecurityUtils.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String salaryAndBenefitsNotifications = setting.get("salaryAndBenefitsNotifications");
            if(ObjectUtils.isNotEmpty(leaderEmp) && "1".equals(salaryAndBenefitsNotifications)){
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
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return toAjax(i);
    }

    /**
     * Update   EMPLOYEE WELFARE APPLICATION FORM
     */
    @RequiresPermissions("hr:empBenefit:edit")
    @Log(title = "  EMPLOYEE WELFARE APPLICATION FORM", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmpBenefit hrEmpBenefit) {
        int i = hrEmpBenefitService.updateHrEmpBenefit(hrEmpBenefit);
        if(i>0){
            AjaxResult info = remoteMessageService.getInfo(hrEmpBenefit.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String salaryAndBenefitsNotifications = setting.get("salaryAndBenefitsNotifications");
            if("1".equals(salaryAndBenefitsNotifications)){
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrEmpBenefit.getUserId());
                HrBenefit hrBenefit = hrBenefitService.selectHrBenefitByBenefitId(hrEmpBenefit.getBenefitId());
                if("3".equals(hrEmpBenefit.getBenefitStatus())){
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(hrEmpBenefit.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(10);
                    sysMessage.setCreateTime(DateUtils.getNowDate());
                    Map<String, Object> map1 = new HashMap<>();
                    Map<String, Object> map2 = new HashMap<>();
                    map1.put("benefitStatus", "Refuse");
                    map1.put("name", hrEmployees.getFullName());
                    map1.put("benefitName", hrBenefit.getBenefitName());
                    sysMessage.setMap1(map1);
                    sysMessage.setMap2(map2);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
                }else if("2".equals(hrEmpBenefit.getBenefitStatus())){
                    SysMessage sysMessage = new SysMessage();
                    sysMessage.setMessageRecipient(hrEmpBenefit.getUserId());
                    sysMessage.setMessageStatus("0");
                    sysMessage.setMessageType(10);
                    sysMessage.setCreateTime(DateUtils.getNowDate());
                    Map<String, Object> map1 = new HashMap<>();
                    Map<String, Object> map2 = new HashMap<>();
                    map1.put("benefitStatus", "Pass");
                    map1.put("name", hrEmployees.getFullName());
                    map1.put("benefitName", hrBenefit.getBenefitName());
                    sysMessage.setMap1(map1);
                    sysMessage.setMap2(map2);
                    remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
                }
            }
        }
        return toAjax(i);
    }

    /**
     * Delete   EMPLOYEE WELFARE APPLICATION FORM
     */
    @Log(title = "  EMPLOYEE WELFARE APPLICATION FORM", businessType = BusinessType.DELETE)
    @DeleteMapping("/{benefitEmpIds}")
    public AjaxResult remove(@PathVariable String[] benefitEmpIds) {
        return toAjax(hrEmpBenefitService.removeByIds(Arrays.asList(benefitEmpIds)));
    }
}
