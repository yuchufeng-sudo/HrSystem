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
import com.ys.hr.domain.HrOnboardingResult;
import com.ys.hr.service.IHrOnboardingResultService;
import org.springframework.validation.annotation.Validated;

/**
 * Onboarding Result Controller
 *
 * @author ys
 * @date 2025-10-14
 */
@RestController
@RequestMapping("/onboardingResult")
public class HrOnboardingResultController extends BaseController
{
    @Autowired
    private IHrOnboardingResultService hrOnboardingResultService;
    /**
     * Add Onboarding Result
     */
    @Log(title = "Onboarding Result", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrOnboardingResult hrOnboardingResult) {
        return toAjax(hrOnboardingResultService.insertHrOnboardingResult(hrOnboardingResult));
    }

    /**
     * Update Onboarding Result
     */
    @Log(title = "Onboarding Result", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrOnboardingResult hrOnboardingResult) {
        return toAjax(hrOnboardingResultService.updateHrOnboardingResult(hrOnboardingResult));
    }
}
