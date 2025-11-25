package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmpTime;
import com.ys.hr.service.IHrEmpTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *  Employee time  Controller
 *
 * @author ys
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/time")
public class HrEmpTimeController extends BaseController
{
    @Autowired
    private IHrEmpTimeService hrEmpTimeService;

    /**
     * Query Employee time  list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrEmpTime hrEmpTime)
    {
        hrEmpTime.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrEmpTime> list = hrEmpTimeService.selectHrEmpTimeList(hrEmpTime);
        return getDataTable(list);
    }

    /**
     * Export Employee time  list
     */
    @RequiresPermissions("hr:time:export")
    @Log(title = "Employee time", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmpTime hrEmpTime)
    {
        List<HrEmpTime> list = hrEmpTimeService.selectHrEmpTimeList(hrEmpTime);
        ExcelUtil<HrEmpTime> util = new ExcelUtil<HrEmpTime>(HrEmpTime.class);
        util.exportExcel(response, list, " Employee time  Data");
    }

    /**
     * Get Employee time  details
     */
    @GetMapping(value = "/{empTimeId}")
    public AjaxResult getInfo(@PathVariable("empTimeId") String empTimeId) {
        return success(hrEmpTimeService.selectHrEmpTimeByEmpTimeId(empTimeId));
    }

    /**
     * Add Employee time
     */
    @RequiresPermissions("hr:time:add")
    @Log(title = "Employee time", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmpTime hrEmpTime) {
        hrEmpTime.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrEmpTimeService.insertHrEmpTime(hrEmpTime));
    }

    /**
     * Update Employee time
     */
    @RequiresPermissions("hr:time:edit")
    @Log(title = "Employee time", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmpTime hrEmpTime) {
        return toAjax(hrEmpTimeService.updateHrEmpTime(hrEmpTime));
    }

    /**
     * Delete Employee time
     */
    @RequiresPermissions("hr:time:remove")
    @Log(title = "Employee time", businessType = BusinessType.DELETE)
    @DeleteMapping("/{empTimeIds}")
    public AjaxResult remove(@PathVariable String[] empTimeIds) {
        return toAjax(hrEmpTimeService.removeByIds(Arrays.asList(empTimeIds)));
    }


    @RequiresPermissions("hr:payroll:list")
    @GetMapping("/getTimeByUserId")
    public AjaxResult getTimeByUserId(HrEmpTime hrEmpTime)
    {
        HrEmpTime list = hrEmpTimeService.getTimeByUserId(hrEmpTime);
        return AjaxResult.success(list);
    }

    @RequiresPermissions("hr:time:add")
    @PostMapping("/addList")
    public AjaxResult add(@Validated @RequestBody List<HrEmpTime> hrEmpTimes) {
        return toAjax(hrEmpTimeService.insertHrEmpTimes(hrEmpTimes));
    }



}
