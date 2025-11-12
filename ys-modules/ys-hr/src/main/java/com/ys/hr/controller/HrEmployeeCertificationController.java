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
import com.ys.hr.domain.HrEmployeeCertification;
import com.ys.hr.service.IHrEmployeeCertificationService;
import org.springframework.validation.annotation.Validated;

/**
 * Employee certification issuance Controller
 *
 * @author ys
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/employeeCertification")
public class HrEmployeeCertificationController extends BaseController
{
    @Autowired
    private IHrEmployeeCertificationService hrEmployeeCertificationService;

    /**
     * Query Employee certification issuance list
     */
    @RequiresPermissions("hr:employeeCertification:list")
    @GetMapping("/list")
    public TableDataInfo list(HrEmployeeCertification hrEmployeeCertification)
    {
        startPage();
        List<HrEmployeeCertification> list = hrEmployeeCertificationService.selectHrEmployeeCertificationList(hrEmployeeCertification);
        return getDataTable(list);
    }

    /**
     * Export Employee certification issuance list
     */
    @RequiresPermissions("hr:employeeCertification:export")
    @Log(title = "Employee certification issuance", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmployeeCertification hrEmployeeCertification)
    {
        List<HrEmployeeCertification> list = hrEmployeeCertificationService.selectHrEmployeeCertificationList(hrEmployeeCertification);
        ExcelUtil<HrEmployeeCertification> util = new ExcelUtil<HrEmployeeCertification>(HrEmployeeCertification.class);
        util.exportExcel(response, list, "Employee certification issuance Data");
    }

    /**
     * Get Employee certification issuance details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(hrEmployeeCertificationService.selectHrEmployeeCertificationById(id));
    }

    /**
     * Add Employee certification issuance
     */
    @RequiresPermissions("hr:employeeCertification:add")
    @Log(title = "Employee certification issuance", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmployeeCertification hrEmployeeCertification) {
        return toAjax(hrEmployeeCertificationService.insertHrEmployeeCertification(hrEmployeeCertification));
    }

    /**
     * Update Employee certification issuance
     */
    @RequiresPermissions("hr:employeeCertification:edit")
    @Log(title = "Employee certification issuance", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmployeeCertification hrEmployeeCertification) {
        return toAjax(hrEmployeeCertificationService.updateHrEmployeeCertification(hrEmployeeCertification));
    }

    /**
     * Delete Employee certification issuance
     */
    @RequiresPermissions("hr:employeeCertification:remove")
    @Log(title = "Employee certification issuance", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(hrEmployeeCertificationService.removeByIds(Arrays.asList(ids)));
    }
}
