package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.hr.domain.HrAgreement;
import com.ys.hr.service.IHrAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * agreement Controller
 *
 * @author ys
 * @date 2025-07-01
 */
@RestController
@RequestMapping("/agreement")
public class HrAgreementController extends BaseController
{
    @Autowired
    private IHrAgreementService hrAgreementService;

    /**
     * Query agreement list
     */
    @RequiresPermissions("hr:agreement:list")
    @GetMapping("/list")
    public TableDataInfo list(HrAgreement hrAgreement)
    {
        startPage();
        List<HrAgreement> list = hrAgreementService.selectHrAgreementList(hrAgreement);
        return getDataTable(list);
    }

    /**
     * Export agreement list
     */
    @RequiresPermissions("hr:agreement:export")
    @Log(title = "agreement", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrAgreement hrAgreement)
    {
        List<HrAgreement> list = hrAgreementService.selectHrAgreementList(hrAgreement);
        ExcelUtil<HrAgreement> util = new ExcelUtil<HrAgreement>(HrAgreement.class);
        util.exportExcel(response, list, "agreement Data");
    }

    /**
     * Get agreement details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrAgreementService.selectHrAgreementById(id));
    }

    /**
     * Add agreement
     */
    @RequiresPermissions("hr:agreement:add")
    @Log(title = "agreement", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrAgreement hrAgreement) {
        return toAjax(hrAgreementService.insertHrAgreement(hrAgreement));
    }

    /**
     * Update agreement
     */
    @RequiresPermissions("hr:agreement:edit")
    @Log(title = "agreement", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrAgreement hrAgreement) {
        return toAjax(hrAgreementService.updateHrAgreement(hrAgreement));
    }

    /**
     * Delete agreement
     */
    @RequiresPermissions("hr:agreement:remove")
    @Log(title = "agreement", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrAgreementService.removeByIds(Arrays.asList(ids)));
    }
}
