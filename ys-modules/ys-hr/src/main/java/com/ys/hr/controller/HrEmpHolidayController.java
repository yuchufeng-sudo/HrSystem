package com.ys.hr.controller;

import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmpHoliday;
import com.ys.hr.service.IHrEmpHolidayService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *  Employee Holiday  Controller
 *
 * @author ys
 * @date 2025-05-23
 */
@RestController
@RequestMapping("/holiday")
public class HrEmpHolidayController extends BaseController
{
    @Autowired
    private IHrEmpHolidayService hrEmpHolidayService;

    /**
     * Query Employee Holiday list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrEmpHoliday hrEmpHoliday)
    {
        hrEmpHoliday.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrEmpHoliday> list = hrEmpHolidayService.selectHrEmpHolidayList(hrEmpHoliday);
        return getDataTable(list);
    }

    /**
     * Export Employee Holiday list
     */
    @RequiresPermissions("hr:holiday:export")
    @Log(title = "Employee Holiday", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmpHoliday hrEmpHoliday)
    {
        List<HrEmpHoliday> list = hrEmpHolidayService.selectHrEmpHolidayList(hrEmpHoliday);
        ExcelUtil<HrEmpHoliday> util = new ExcelUtil<HrEmpHoliday>(HrEmpHoliday.class);
        util.exportExcel(response, list, " employee holiday data");
    }

    /**
     * Get Employee Holiday Details
     */
    @GetMapping(value = "/{empleHolidayId}")
    public AjaxResult getInfo(@PathVariable("empleHolidayId") Long empleHolidayId) {
        return success(hrEmpHolidayService.selectHrEmpHolidayById(empleHolidayId));
    }

    /**
     * Add Employee Holiday
     */
    @Log(title = "Employee Holiday", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody HrEmpHoliday hrEmpHoliday) {
        return toAjax(hrEmpHolidayService.insertHrEmpHoliday(hrEmpHoliday));
    }

    @Log(title = "Employee Holiday", businessType = BusinessType.INSERT)
    @PostMapping("/addHolidays")
    public AjaxResult addHolidays(@RequestBody HrEmpHoliday hrEmpHoliday) {
        hrEmpHolidayService.addHolidays(hrEmpHoliday);
        return success();
    }

    /**
     * Update Employee Holiday
     */
    @Log(title = "Employee Holiday", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmpHoliday hrEmpHoliday) {
        hrEmpHoliday.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        hrEmpHoliday.setUpdateTime(DateUtils.getNowDate());
        if(ObjectUtils.isNotEmpty(hrEmpHoliday.getStateTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(hrEmpHoliday.getStateTime());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            hrEmpHoliday.setSearchTime(localDate);
        }
        return toAjax(hrEmpHolidayService.updateByHrEmpHolidayById(hrEmpHoliday));
    }

    /**
     * Delete Employee Holiday
     */
    @RequiresPermissions("hr:holiday:remove")
    @Log(title = "Employee Holiday", businessType = BusinessType.DELETE)
    @DeleteMapping("/{empleHolidayIds}")
    public AjaxResult remove(@PathVariable Long[] empleHolidayIds) {
        return toAjax(hrEmpHolidayService.removeByIds(Arrays.asList(empleHolidayIds)));
    }
}
