package com.ys.hr.controller;

import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrTargetFeedback;
import com.ys.hr.service.IHrTargetFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Target Feedback Controller
 *
 * @author ys
 * @date 2025-06-30
 */
@RestController
@RequestMapping("/targetFeedback")
public class HrTargetFeedbackController extends BaseController
{
    @Autowired
    private IHrTargetFeedbackService hrTargetFeedbackService;

    /**
     * Query Target Feedback list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrTargetFeedback hrTargetFeedback)
    {
        startPage();
        List<HrTargetFeedback> list = hrTargetFeedbackService.selectHrTargetFeedbackList(hrTargetFeedback);
        return getDataTable(list);
    }

    /**
     * Export Target Feedback list
     */
    @Log(title = "Target Feedback", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrTargetFeedback hrTargetFeedback)
    {
        List<HrTargetFeedback> list = hrTargetFeedbackService.selectHrTargetFeedbackList(hrTargetFeedback);
        ExcelUtil<HrTargetFeedback> util = new ExcelUtil<HrTargetFeedback>(HrTargetFeedback.class);
        util.exportExcel(response, list, "Target Feedback Data");
    }

    /**
     * Get Target Feedback details
     */
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrTargetFeedbackService.selectHrTargetFeedbackById(id));
    }

    /**
     * Add Target Feedback
     */
    @Log(title = "Target Feedback", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrTargetFeedback hrTargetFeedback) {
        hrTargetFeedback.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        hrTargetFeedback.setFeedbackBy(SecurityUtils.getUserId());
        hrTargetFeedback.setFeedbackTime(DateUtils.getNowDate());
        return toAjax(hrTargetFeedbackService.insertHrTargetFeedback(hrTargetFeedback));
    }

    /**
     * Update Target Feedback
     */
    @Log(title = "Target Feedback", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrTargetFeedback hrTargetFeedback) {
        return toAjax(hrTargetFeedbackService.updateHrTargetFeedback(hrTargetFeedback));
    }
}
