package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrBenefitGroup;
import com.ys.hr.service.IHrBenefitGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Table of benefit types Controller
 *
 * @author ys
 * @date 2025-07-10
 */
@RestController
@RequestMapping("/group")
public class HrBenefitGroupController extends BaseController
{
    @Autowired
    private IHrBenefitGroupService hrBenefitGroupService;

    /**
     * Query Table of benefit types list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrBenefitGroup hrBenefitGroup)
    {
        hrBenefitGroup.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrBenefitGroup> list = hrBenefitGroupService.selectHrBenefitGroupList(hrBenefitGroup);
        return getDataTable(list);
    }

    /**
     * Export Table of benefit types list
     */
    @Log(title = "Table of benefit types", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrBenefitGroup hrBenefitGroup)
    {
        List<HrBenefitGroup> list = hrBenefitGroupService.selectHrBenefitGroupList(hrBenefitGroup);
        ExcelUtil<HrBenefitGroup> util = new ExcelUtil<HrBenefitGroup>(HrBenefitGroup.class);
        util.exportExcel(response, list, "Table of benefit types Data");
    }

    /**
     * Get Table of benefit types details
     */
    @GetMapping(value = "/{benefitGroupId}")
    public AjaxResult getInfo(@PathVariable("benefitGroupId") String benefitGroupId) {
        return success(hrBenefitGroupService.selectHrBenefitGroupByBenefitGroupId(benefitGroupId));
    }

    /**
     * Add Table of benefit types
     */
    @Log(title = "Table of benefit types", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrBenefitGroup hrBenefitGroup) {
        hrBenefitGroup.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrBenefitGroupService.insertHrBenefitGroup(hrBenefitGroup));
    }

    /**
     * Update Table of benefit types
     */
    @Log(title = "Table of benefit types", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrBenefitGroup hrBenefitGroup) {
        return toAjax(hrBenefitGroupService.updateHrBenefitGroup(hrBenefitGroup));
    }

    /**
     * Delete Table of benefit types
     */
    @Log(title = "Table of benefit types", businessType = BusinessType.DELETE)
    @DeleteMapping("/{benefitGroupIds}")
    public AjaxResult remove(@PathVariable String[] benefitGroupIds) {
        return toAjax(hrBenefitGroupService.removeByIds(Arrays.asList(benefitGroupIds)));
    }
}
