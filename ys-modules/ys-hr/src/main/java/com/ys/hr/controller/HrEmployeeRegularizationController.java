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
import com.ys.hr.domain.HrEmployeeRegularization;
import com.ys.hr.service.IHrEmployeeRegularizationService;
import org.springframework.validation.annotation.Validated;

/**
 * Employee Regularization Management Controller
 *
 * @author ys
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/employeeRegularization")
public class HrEmployeeRegularizationController extends BaseController
{
    @Autowired
    private IHrEmployeeRegularizationService hrEmployeeRegularizationService;

    /**
     * Query Employee Regularization Management list
     */
    @RequiresPermissions("hr:employeeRegularization:list")
    @GetMapping("/list")
    public TableDataInfo list(HrEmployeeRegularization hrEmployeeRegularization)
    {
        startPage();
        List<HrEmployeeRegularization> list = hrEmployeeRegularizationService.selectHrEmployeeRegularizationList(hrEmployeeRegularization);
        return getDataTable(list);
    }

    /**
     * Export Employee Regularization Management list
     */
    @RequiresPermissions("hr:employeeRegularization:export")
    @Log(title = "Employee Regularization Management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmployeeRegularization hrEmployeeRegularization)
    {
        List<HrEmployeeRegularization> list = hrEmployeeRegularizationService.selectHrEmployeeRegularizationList(hrEmployeeRegularization);
        ExcelUtil<HrEmployeeRegularization> util = new ExcelUtil<HrEmployeeRegularization>(HrEmployeeRegularization.class);
        util.exportExcel(response, list, "Employee Regularization Management Data");
    }

    /**
     * Get Employee Regularization Management details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrEmployeeRegularizationService.selectHrEmployeeRegularizationById(id));
    }

    /**
     * Add Employee Regularization Management
     */
    @RequiresPermissions("hr:employeeRegularization:add")
    @Log(title = "Employee Regularization Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmployeeRegularization hrEmployeeRegularization) {
        return toAjax(hrEmployeeRegularizationService.insertHrEmployeeRegularization(hrEmployeeRegularization));
    }

    /**
     * Update Employee Regularization Management
     */
    @RequiresPermissions("hr:employeeRegularization:edit")
    @Log(title = "Employee Regularization Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmployeeRegularization hrEmployeeRegularization) {
        return toAjax(hrEmployeeRegularizationService.updateHrEmployeeRegularization(hrEmployeeRegularization));
    }

    /**
     * Delete Employee Regularization Management
     */
    @RequiresPermissions("hr:employeeRegularization:remove")
    @Log(title = "Employee Regularization Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrEmployeeRegularizationService.removeByIds(Arrays.asList(ids)));
    }
}
