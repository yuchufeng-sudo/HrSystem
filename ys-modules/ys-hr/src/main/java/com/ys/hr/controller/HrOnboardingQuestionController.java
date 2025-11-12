package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
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
import com.ys.hr.domain.HrOnboardingQuestion;
import com.ys.hr.service.IHrOnboardingQuestionService;
import org.springframework.validation.annotation.Validated;

/**
 * Job listings questions table Controller
 *
 * @author ys
 * @date 2025-10-13
 */
@RestController
@RequestMapping("/onboardingQuestion")
public class HrOnboardingQuestionController extends BaseController
{
    @Autowired
    private IHrOnboardingQuestionService hrOnboardingQuestionService;

    /**
     * Query Job listings questions table list
     */
    @GetMapping("/listByEmp")
    public TableDataInfo listByEmp(HrOnboardingQuestion hrOnboardingQuestion)
    {
        hrOnboardingQuestion.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrOnboardingQuestion> list = hrOnboardingQuestionService.selectHrOnboardingQuestionListByEmp(hrOnboardingQuestion);
        return getDataTable(list);
    }

    @RequiresPermissions("hr:onboarding:list")
    @GetMapping("/list")
    public TableDataInfo list(HrOnboardingQuestion hrOnboardingQuestion)
    {
        hrOnboardingQuestion.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrOnboardingQuestion> list = hrOnboardingQuestionService.selectHrOnboardingQuestionList(hrOnboardingQuestion);
        return getDataTable(list);
    }

    /**
     * Get Job listings questions table details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(hrOnboardingQuestionService.selectHrOnboardingQuestionById(id));
    }

    /**
     * Add Job listings questions table
     */
    @RequiresPermissions("hr:onboarding:add")
    @Log(title = "Job listings questions table", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrOnboardingQuestion hrOnboardingQuestion) {
        hrOnboardingQuestion.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrOnboardingQuestionService.insertHrOnboardingQuestion(hrOnboardingQuestion));
    }

    /**
     * Update Job listings questions table
     */
    @RequiresPermissions("hr:onboarding:edit")
    @Log(title = "Job listings questions table", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrOnboardingQuestion hrOnboardingQuestion) {
        return toAjax(hrOnboardingQuestionService.updateHrOnboardingQuestion(hrOnboardingQuestion));
    }

    /**
     * Delete Job listings questions table
     */
    @RequiresPermissions("hr:onboarding:remove")
    @Log(title = "Job listings questions table", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(hrOnboardingQuestionService.removeByIds(Arrays.asList(ids)));
    }
}
