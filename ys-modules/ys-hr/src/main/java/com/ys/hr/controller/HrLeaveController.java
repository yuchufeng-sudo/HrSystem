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
import com.ys.hr.domain.HrHoliday;
import com.ys.hr.domain.HrLeave;
import com.ys.hr.mapper.HrEmployeesMapper;
import com.ys.hr.service.IHrLeaveService;
import com.ys.hr.service.impl.HrHolidayServiceImpl;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *  Leave Application Controller
 *
 * @author ys
 * @date 2025-05-21
 */
@RestController
@RequestMapping("/leave")
public class HrLeaveController extends BaseController
{
    @Resource
    private IHrLeaveService hrLeaveService;

    /**
     * Query Leave Application list
     */
    @RequiresPermissions("hr:leave:list")
    @GetMapping("/list")
    public TableDataInfo list(HrLeave hrLeave)
    {
        // Employee
        if(ObjectUtils.isNotEmpty(hrLeave.getFlag())){
            if(ObjectUtils.isEmpty(hrLeave.getUserId())){
                hrLeave.setUserId(SecurityUtils.getUserId());
            }
        }else{
        //hr User
            hrLeave.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        }
        startPage();
        List<HrLeave> list = hrLeaveService.selectHrLeaveList(hrLeave);
        return getDataTable(list);
    }

    /**
     * Export Leave Application list
     */
    @RequiresPermissions("hr:leave:export")
    @Log(title = "Leave Application", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrLeave hrLeave)
    {
        List<HrLeave> list = hrLeaveService.selectHrLeaveList(hrLeave);
        ExcelUtil<HrLeave> util = new ExcelUtil<HrLeave>(HrLeave.class);
        util.exportExcel(response, list, "Leave Application Data");
    }

    /**
     * Get Leave Application Details
     */
    @GetMapping(value = "/{leaveId}")
    public AjaxResult getInfo(@PathVariable("leaveId") Long leaveId) {
        HrLeave hrLeave1 = hrLeaveService.getById(leaveId);
        HrLeave hrLeave = hrLeaveService.selectHrLeaveLastTime(hrLeave1.getUserId(),hrLeave1.getStateTime(),hrLeave1.getLeaveType());
        if(ObjectUtils.isNotEmpty(hrLeave)){
            hrLeave1.setLastStateTime(hrLeave.getStateTime());
            hrLeave1.setLastEndTime(hrLeave.getEndTime());
            hrLeave1.setRemainingDays(hrLeave.getRemainingDays());
        }else{
            hrLeave1.setRemainingDays(hrLeave.getRemainingDays());
        }
        return success(hrLeave1);
    }

    /**
     * Add Leave Application
     */
    @RequiresPermissions("hr:leave:add")
    @Log(title = "Leave Application", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrLeave hrLeave) {
        return toAjax(hrLeaveService.save(hrLeave));
    }

    /**
     * Update Leave Application
     */
    @RequiresPermissions("hr:leave:edit")
    @Log(title = "Leave Application", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrLeave hrLeave) {
        return toAjax(hrLeaveService.updateByLeaveId(hrLeave));
    }

    /**
     * Delete Leave Application
     */
    @RequiresPermissions("hr:leave:remove")
    @Log(title = "Leave Application", businessType = BusinessType.DELETE)
    @DeleteMapping("/{leaveIds}")
    public AjaxResult remove(@PathVariable Long[] leaveIds) {
        return toAjax(hrLeaveService.removeByIds(Arrays.asList(leaveIds)));
    }

    // Leave Application data statistics
    @GetMapping("/leaveCount")
    public AjaxResult candidateCount()
    {
        HrLeave hrLeave = new HrLeave();
        hrLeave.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(hrLeaveService.leaveCount(hrLeave));
    }

    //User  Leave Application data statistics
    @GetMapping("/leaveCountByUser")
    public AjaxResult leaveCountByUser()
    {
        HrLeave hrLeave = new HrLeave();
        hrLeave.setUserId(SecurityUtils.getUserId());
        return AjaxResult.success(hrLeaveService.leaveCountByUser(hrLeave));
    }
}
