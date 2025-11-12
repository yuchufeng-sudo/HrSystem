package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrCertification;
import com.ys.hr.domain.HrTrainingAssignments;
import com.ys.hr.service.IHrCertificationService;
import com.ys.hr.service.IHrTrainingAssignmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Certification information Controller
 *
 * @author ys
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/certification")
public class HrCertificationController extends BaseController
{
    @Autowired
    private IHrCertificationService hrCertificationService;

    /**
     * Query Certification information list
     */
    @RequiresPermissions("hr:certification:list")
    @GetMapping("/list")
    public TableDataInfo list(HrCertification hrCertification)
    {
        startPage();
        hrCertification.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrCertification> list = hrCertificationService.selectHrCertificationList(hrCertification);
        return getDataTable(list);
    }

    /**
     * Export Certification information list
     */
    @RequiresPermissions("hr:certification:export")
    @Log(title = "Certification information", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrCertification hrCertification)
    {
        hrCertification.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        List<HrCertification> list = hrCertificationService.selectHrCertificationList(hrCertification);
        ExcelUtil<HrCertification> util = new ExcelUtil<HrCertification>(HrCertification.class);
        util.exportExcel(response, list, "Certification information Data");
    }

    /**
     * Get Certification information details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrCertificationService.selectHrCertificationById(id));
    }

    /**
     * Add Certification information
     */
    @RequiresPermissions("hr:certification:add")
    @Log(title = "Certification information", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrCertification hrCertification) {
        hrCertification.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrCertificationService.insertHrCertification(hrCertification));
    }

    /**
     * Update Certification information
     */
    @RequiresPermissions("hr:certification:edit")
    @Log(title = "Certification information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrCertification hrCertification) {
        return toAjax(hrCertificationService.updateHrCertification(hrCertification));
    }

    /**
     * Delete Certification information
     */
    @RequiresPermissions("hr:certification:remove")
    @Log(title = "Certification information", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrCertificationService.deleteHrCertificationByIds(ids));
    }

    @Resource
    private IHrTrainingAssignmentsService trainingAssignmentsService;

    /**
     * Employee List
     */
    @GetMapping("/employeeList")
    public AjaxResult employeeList(Long programId) {
        HrTrainingAssignments trainingAssignments = new HrTrainingAssignments();
        trainingAssignments.setProgramId(programId);
        List<HrTrainingAssignments> list = trainingAssignmentsService.selectHrTrainingAssignmentsList(trainingAssignments);
        return AjaxResult.success(list);
    }
}
