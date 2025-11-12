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
import com.ys.hr.domain.HrProbationEvaluation;
import com.ys.hr.service.IHrProbationEvaluationService;
import org.springframework.validation.annotation.Validated;

/**
 * Employee Probationary Evaluation Controller
 *
 * @author ys
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/probationEvaluation")
public class HrProbationEvaluationController extends BaseController
{
    @Autowired
    private IHrProbationEvaluationService hrProbationEvaluationService;

    /**
     * Query Employee Probationary Evaluation list
     */
    @RequiresPermissions("hr:probationEvaluation:list")
    @GetMapping("/list")
    public TableDataInfo list(HrProbationEvaluation hrProbationEvaluation)
    {
        startPage();
        List<HrProbationEvaluation> list = hrProbationEvaluationService.selectHrProbationEvaluationList(hrProbationEvaluation);
        return getDataTable(list);
    }

    /**
     * Export Employee Probationary Evaluation list
     */
    @RequiresPermissions("hr:probationEvaluation:export")
    @Log(title = "Employee Probationary Evaluation", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrProbationEvaluation hrProbationEvaluation)
    {
        List<HrProbationEvaluation> list = hrProbationEvaluationService.selectHrProbationEvaluationList(hrProbationEvaluation);
        ExcelUtil<HrProbationEvaluation> util = new ExcelUtil<HrProbationEvaluation>(HrProbationEvaluation.class);
        util.exportExcel(response, list, "Employee Probationary Evaluation Data");
    }

    /**
     * Get Employee Probationary Evaluation details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrProbationEvaluationService.selectHrProbationEvaluationById(id));
    }

    /**
     * Add Employee Probationary Evaluation
     */
    @RequiresPermissions("hr:probationEvaluation:add")
    @Log(title = "Employee Probationary Evaluation", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrProbationEvaluation hrProbationEvaluation) {
        return toAjax(hrProbationEvaluationService.insertHrProbationEvaluation(hrProbationEvaluation));
    }

    /**
     * Update Employee Probationary Evaluation
     */
    @RequiresPermissions("hr:probationEvaluation:edit")
    @Log(title = "Employee Probationary Evaluation", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrProbationEvaluation hrProbationEvaluation) {
        return toAjax(hrProbationEvaluationService.updateHrProbationEvaluation(hrProbationEvaluation));
    }

    /**
     * Delete Employee Probationary Evaluation
     */
    @RequiresPermissions("hr:probationEvaluation:remove")
    @Log(title = "Employee Probationary Evaluation", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrProbationEvaluationService.removeByIds(Arrays.asList(ids)));
    }
}
