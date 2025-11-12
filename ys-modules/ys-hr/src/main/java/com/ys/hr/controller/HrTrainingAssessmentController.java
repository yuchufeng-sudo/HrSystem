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
import com.ys.hr.domain.HrTrainingAssessment;
import com.ys.hr.service.IHrTrainingAssessmentService;
import org.springframework.validation.annotation.Validated;

/**
 * Training Assessment Controller
 *
 * @author ys
 * @date 2025-06-25
 */
@RestController
@RequestMapping("/trainingAssessment")
public class HrTrainingAssessmentController extends BaseController
{
    @Autowired
    private IHrTrainingAssessmentService hrTrainingAssessmentService;

    /**
     * Query Training Assessment list
     */
    @RequiresPermissions("hr:trainingAssessment:list")
    @GetMapping("/list")
    public TableDataInfo list(HrTrainingAssessment hrTrainingAssessment)
    {
        startPage();
        List<HrTrainingAssessment> list = hrTrainingAssessmentService.selectHrTrainingAssessmentList(hrTrainingAssessment);
        return getDataTable(list);
    }

    /**
     * Export Training Assessment list
     */
    @RequiresPermissions("hr:trainingAssessment:export")
    @Log(title = "Training Assessment", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrTrainingAssessment hrTrainingAssessment)
    {
        List<HrTrainingAssessment> list = hrTrainingAssessmentService.selectHrTrainingAssessmentList(hrTrainingAssessment);
        ExcelUtil<HrTrainingAssessment> util = new ExcelUtil<HrTrainingAssessment>(HrTrainingAssessment.class);
        util.exportExcel(response, list, "Training Assessment Data");
    }

    /**
     * Get Training Assessment details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrTrainingAssessmentService.selectHrTrainingAssessmentById(id));
    }

    /**
     * Add Training Assessment
     */
    @RequiresPermissions("hr:trainingAssessment:add")
    @Log(title = "Training Assessment", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTrainingAssessment hrTrainingAssessment) {
        return toAjax(hrTrainingAssessmentService.insertHrTrainingAssessment(hrTrainingAssessment));
    }

    /**
     * Update Training Assessment
     */
    @RequiresPermissions("hr:trainingAssessment:edit")
    @Log(title = "Training Assessment", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTrainingAssessment hrTrainingAssessment) {
        return toAjax(hrTrainingAssessmentService.updateHrTrainingAssessment(hrTrainingAssessment));
    }

    /**
     * Delete Training Assessment
     */
    @RequiresPermissions("hr:trainingAssessment:remove")
    @Log(title = "Training Assessment", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrTrainingAssessmentService.removeByIds(Arrays.asList(ids)));
    }
}
