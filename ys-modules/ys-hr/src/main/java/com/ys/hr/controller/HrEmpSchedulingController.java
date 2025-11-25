package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmpScheduling;
import com.ys.hr.domain.HrSchedulingEmp;
import com.ys.hr.service.IHrEmpSchedulingService;
import com.ys.hr.service.IHrSchedulingEmpService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  Employee Scheduling Controller
 *
 * @author ys
 * @date 2025-06-04
 */
@RestController
@RequestMapping("/scheduling")
public class HrEmpSchedulingController extends BaseController
{
    @Autowired
    private IHrEmpSchedulingService hrEmpSchedulingService;

    @Autowired
    private IHrSchedulingEmpService hrSchedulingEmpService;

    /**
     * Query Employee Scheduling list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrEmpScheduling hrEmpScheduling)
    {
        hrEmpScheduling.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrEmpScheduling> list = hrEmpSchedulingService.selectHrEmpSchedulingList(hrEmpScheduling);
        return getDataTable(list);
    }

    /**
     * Export Employee Scheduling list
     */
    @RequiresPermissions("hr:scheduling:export")
    @Log(title = "Employee Scheduling", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmpScheduling hrEmpScheduling)
    {
        List<HrEmpScheduling> list = hrEmpSchedulingService.selectHrEmpSchedulingList(hrEmpScheduling);
        ExcelUtil<HrEmpScheduling> util = new ExcelUtil<HrEmpScheduling>(HrEmpScheduling.class);
        util.exportExcel(response, list, " Employee Scheduling Data");
    }

    /**
     * Get Employee Scheduling details
     */
    @GetMapping(value = "/{schedulingId}")
    public AjaxResult getInfo(@PathVariable("schedulingId") String schedulingId) {
        return success(hrEmpSchedulingService.selectHrEmpSchedulingBySchedulingId(schedulingId));
    }

    /**
     * Add Employee Scheduling
     */
    @Log(title = "Employee Scheduling", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmpScheduling hrEmpScheduling) {
        hrEmpScheduling.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrEmpSchedulingService.insertHrEmpScheduling(hrEmpScheduling);
        return AjaxResult.success(hrEmpScheduling);
    }

    /**
     * Update Employee Scheduling
     */
    @Log(title = "Employee Scheduling", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmpScheduling hrEmpScheduling) {
        return toAjax(hrEmpSchedulingService.updateHrEmpScheduling(hrEmpScheduling));
    }

    /**
     * Delete Employee Scheduling
     */
    @Log(title = "Employee Scheduling", businessType = BusinessType.DELETE)
    @DeleteMapping("/{schedulingIds}")
    public AjaxResult remove(@PathVariable String[] schedulingIds) {
        ArrayList<String> list = new ArrayList<>();
        for (String schedulingId : schedulingIds) {
            HrSchedulingEmp emp = new HrSchedulingEmp();
            emp.setSchedulingId(schedulingId);
            List<HrSchedulingEmp> hrSchedulingEmps = hrSchedulingEmpService.selectHrSchedulingEmpList(emp);
            if(ObjectUtils.isNotEmpty(hrSchedulingEmps)){
                return AjaxResult.warn("There are already employees under the shift.");
            }
        }
        return toAjax(hrEmpSchedulingService.removeByIds(Arrays.asList(schedulingIds)));
    }
}
