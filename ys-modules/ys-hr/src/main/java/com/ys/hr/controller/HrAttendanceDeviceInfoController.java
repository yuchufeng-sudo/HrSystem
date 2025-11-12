package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrAttendanceDeviceInfo;
import com.ys.hr.service.IHrAttendanceDeviceInfoService;
import org.springframework.validation.annotation.Validated;

/**
 * Attendance Device Information  Controller
 *
 * @author ys
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/deviceInfo")
public class HrAttendanceDeviceInfoController extends BaseController
{
    @Autowired
    private IHrAttendanceDeviceInfoService hrAttendanceDeviceInfoService;

    /**
     * Query Attendance Device Information  list
     */
    @RequiresPermissions("hr:deviceInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(HrAttendanceDeviceInfo hrAttendanceDeviceInfo)
    {
        startPage();
        List<HrAttendanceDeviceInfo> list = hrAttendanceDeviceInfoService.selectHrAttendanceDeviceInfoList(hrAttendanceDeviceInfo);
        return getDataTable(list);
    }

    /**
     * Export Attendance Device Information  list
     */
    @RequiresPermissions("hr:deviceInfo:export")
    @Log(title = "Attendance Device Information ", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrAttendanceDeviceInfo hrAttendanceDeviceInfo)
    {
        List<HrAttendanceDeviceInfo> list = hrAttendanceDeviceInfoService.selectHrAttendanceDeviceInfoList(hrAttendanceDeviceInfo);
        ExcelUtil<HrAttendanceDeviceInfo> util = new ExcelUtil<HrAttendanceDeviceInfo>(HrAttendanceDeviceInfo.class);
        util.exportExcel(response, list, "Attendance Device Information  Data");
    }

    /**
     * Get Attendance Device Information  details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrAttendanceDeviceInfoService.selectHrAttendanceDeviceInfoById(id));
    }

    /**
     * Add Attendance Device Information 
     */
    @RequiresPermissions("hr:deviceInfo:add")
    @Log(title = "Attendance Device Information ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrAttendanceDeviceInfo hrAttendanceDeviceInfo) {
        return toAjax(hrAttendanceDeviceInfoService.insertHrAttendanceDeviceInfo(hrAttendanceDeviceInfo));
    }

    /**
     * Update Attendance Device Information 
     */
    @RequiresPermissions("hr:deviceInfo:edit")
    @Log(title = "Attendance Device Information ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrAttendanceDeviceInfo hrAttendanceDeviceInfo) {
        return toAjax(hrAttendanceDeviceInfoService.updateHrAttendanceDeviceInfo(hrAttendanceDeviceInfo));
    }

    /**
     * Delete Attendance Device Information 
     */
    @RequiresPermissions("hr:deviceInfo:remove")
    @Log(title = "Attendance Device Information ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(hrAttendanceDeviceInfoService.removeByIds(Arrays.asList(ids)));
    }
}
