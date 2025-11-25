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
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.domain.HrSchedulingEmp;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrSchedulingEmpService;
import com.ys.hr.service.impl.HrLeaveServiceImpl;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Employee scheduling Controller
 *
 * @author ys
 * @date 2025-06-08
 */
@RestController
@RequestMapping("/emp")
public class HrSchedulingEmpController extends BaseController {
    @Resource
    private IHrSchedulingEmpService hrSchedulingEmpService;
    @Resource
    private HrEmployeesMapper hrEmployeesMapper;
    @Resource
    private RemoteMessageService remoteMessageService;


    /**
     * Query  Employee scheduling list
     */
//    @RequiresPermissions("hr:emp:list")
    @GetMapping("/list")
    public TableDataInfo list(HrSchedulingEmp hrSchedulingEmp) {
        startPage();
        List<HrSchedulingEmp> list = hrSchedulingEmpService.selectHrSchedulingEmpList(hrSchedulingEmp);
        return getDataTable(list);
    }

    /**
     * Export Employee scheduling list
     */
    @RequiresPermissions("hr:emp:export")
    @Log(title = "Employee scheduling", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrSchedulingEmp hrSchedulingEmp) {
        List<HrSchedulingEmp> list = hrSchedulingEmpService.selectHrSchedulingEmpList(hrSchedulingEmp);
        ExcelUtil<HrSchedulingEmp> util = new ExcelUtil<>(HrSchedulingEmp.class);
        util.exportExcel(response, list, "   Employee scheduling Data");
    }

    /**
     * Get Employee scheduling details
     */
    @GetMapping(value = "/{schedulingEmpId}")
    public AjaxResult getInfo(@PathVariable("schedulingEmpId") String schedulingEmpId) {
        return success(hrSchedulingEmpService.selectHrSchedulingEmpBySchedulingEmpId(schedulingEmpId));
    }

    //Get Employee scheduling
    @GetMapping(value = "/getUserInfo")
    public AjaxResult getUserInfo() {
        return success(hrSchedulingEmpService.getUserInfo(SecurityUtils.getUserId()));
    }

    /**
     * Add Employee scheduling
     */
    @Log(title = "Employee scheduling", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrSchedulingEmp hrSchedulingEmp) {
        int i = hrSchedulingEmpService.insertHrSchedulingEmp(hrSchedulingEmp);
        if (i > 0) {
            AjaxResult info = remoteMessageService.getInfo(hrSchedulingEmp.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String shiftScheduleUpdate = setting.get("shiftScheduleUpdate");
            if("1".equals(shiftScheduleUpdate)){
                HrEmployees Leader = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrSchedulingEmp.getUserId());
                Date schedulingData = hrSchedulingEmp.getSchedulingData();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                LocalDate schedulingData2 = schedulingData.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String schedulingDataStr = schedulingData2.format(formatter);
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrSchedulingEmp.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(7);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                map1.put("arranger", Leader.getFullName());
                map1.put("employee", hrEmployees.getFullName());
                map1.put("month", schedulingDataStr);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return toAjax(i);
    }

    /**
     * Update Employee scheduling
     */
    @Log(title = "Employee scheduling", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrSchedulingEmp hrSchedulingEmp) {
        int i = hrSchedulingEmpService.updateHrSchedulingEmp(hrSchedulingEmp);
        if (i > 0) {
            AjaxResult info = remoteMessageService.getInfo(hrSchedulingEmp.getUserId(),SecurityConstants.INNER);
            Map<String,String> setting = (Map<String, String>) info.get("data");
            String shiftScheduleUpdate = setting.get("shiftScheduleUpdate");
            if("1".equals(shiftScheduleUpdate)){
                HrEmployees Leader = hrEmployeesMapper.selectHrEmployeesByUserId(SecurityUtils.getUserId());
                HrEmployees hrEmployees = hrEmployeesMapper.selectHrEmployeesByUserId(hrSchedulingEmp.getUserId());
                Date schedulingData = hrSchedulingEmp.getSchedulingData();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                LocalDate schedulingData2 = schedulingData.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String schedulingDataStr = schedulingData2.format(formatter);
                SysMessage sysMessage = new SysMessage();
                sysMessage.setMessageRecipient(hrSchedulingEmp.getUserId());
                sysMessage.setMessageStatus("0");
                sysMessage.setMessageType(7);
                sysMessage.setCreateTime(DateUtils.getNowDate());
                Map<String, Object> map1 = new HashMap<>();
                Map<String, Object> map2 = new HashMap<>();
                if(ObjectUtils.isNotEmpty( Leader)){
                    map1.put("arranger", Leader.getFullName());
                }else{
                    map1.put("arranger", "Unknown");
                }
                map1.put("employee", hrEmployees.getFullName());
                map1.put("month", schedulingDataStr);
                sysMessage.setMap1(map1);
                sysMessage.setMap2(map2);
                remoteMessageService.sendMessageByTemplate(sysMessage, SecurityConstants.INNER);
            }
        }
        return toAjax(i);
    }

    /**
     * Delete Employee scheduling
     */
    @RequiresPermissions("hr:emp:remove")
    @Log(title = "Employee scheduling", businessType = BusinessType.DELETE)
    @DeleteMapping("/{schedulingEmpIds}")
    public AjaxResult remove(@PathVariable String[] schedulingEmpIds) {
        return toAjax(hrSchedulingEmpService.removeByIds(Arrays.asList(schedulingEmpIds)));
    }
}
