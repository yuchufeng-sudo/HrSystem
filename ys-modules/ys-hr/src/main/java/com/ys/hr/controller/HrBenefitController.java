package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrBenefit;
import com.ys.hr.service.IHrBenefitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *  WELFARE BENEFITS Controller
 *
 * @author ys
 * @date 2025-06-09
 */
@RestController
@RequestMapping("/benefit")
public class HrBenefitController extends BaseController
{
    @Autowired
    private IHrBenefitService hrBenefitService;

    /**
     * Query  WELFARE BENEFITS list
     */
    @RequiresPermissions("hr:benefit:list")
    @GetMapping("/list")
    public TableDataInfo list(HrBenefit hrBenefit)
    {
        hrBenefit.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrBenefit> list = hrBenefitService.selectHrBenefitList(hrBenefit);
        return getDataTable(list);
    }

    /**
     * Export  WELFARE BENEFITS list
     */
    @RequiresPermissions("hr:benefit:export")
    @Log(title = " WELFARE BENEFITS", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrBenefit hrBenefit)
    {
        List<HrBenefit> list = hrBenefitService.selectHrBenefitList(hrBenefit);
        ExcelUtil<HrBenefit> util = new ExcelUtil<HrBenefit>(HrBenefit.class);
        util.exportExcel(response, list, " WELFARE BENEFITS Data");
    }

    /**
     * Get  WELFARE BENEFITS details
     */
    @GetMapping(value = "/{benefitId}")
    public AjaxResult getInfo(@PathVariable("benefitId") String benefitId) {
        return success(hrBenefitService.selectHrBenefitByBenefitId(benefitId));
    }

    /**
     * Add  WELFARE BENEFITS
     */
    @Log(title = " WELFARE BENEFITS", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrBenefit hrBenefit) {
        hrBenefit.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrBenefitService.insertHrBenefit(hrBenefit));
    }

    /**
     * Update  WELFARE BENEFITS
     */
    @Log(title = " WELFARE BENEFITS", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrBenefit hrBenefit) {
        return toAjax(hrBenefitService.updateHrBenefit(hrBenefit));
    }

    /**
     * Delete  WELFARE BENEFITS
     */
    @Log(title = " WELFARE BENEFITS", businessType = BusinessType.DELETE)
    @DeleteMapping("/{benefitIds}")
    public AjaxResult remove(@PathVariable String[] benefitIds) {
        return toAjax(hrBenefitService.removeByIds(Arrays.asList(benefitIds)));
    }
}
