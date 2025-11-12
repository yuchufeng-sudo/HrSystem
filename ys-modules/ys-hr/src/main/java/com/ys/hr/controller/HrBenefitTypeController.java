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
import com.ys.hr.domain.HrBenefitType;
import com.ys.hr.service.IHrBenefitTypeService;
import com.ys.hr.service.impl.HrBenefitServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *  WELFARE TYPE  LIST Controller
 *
 * @author ys
 * @date 2025-06-09
 */
@RestController
@RequestMapping("/type")
public class HrBenefitTypeController extends BaseController
{
    @Autowired
    private IHrBenefitTypeService hrBenefitTypeService;

    @Autowired
    private HrBenefitServiceImpl hrEmpBenefitService;

    /**
     * Query  WELFARE TYPE   LIST list
     */
    @GetMapping("/list")
    public TableDataInfo list(HrBenefitType hrBenefitType)
    {
        hrBenefitType.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrBenefitType> list = hrBenefitTypeService.selectHrBenefitTypeList(hrBenefitType);
        return getDataTable(list);
    }

    /**
     * Export  WELFARE TYPE   LIST list
     */
    @RequiresPermissions("hr:type:export")
    @Log(title = " WELFARE TYPE   LIST", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrBenefitType hrBenefitType)
    {
        List<HrBenefitType> list = hrBenefitTypeService.selectHrBenefitTypeList(hrBenefitType);
        ExcelUtil<HrBenefitType> util = new ExcelUtil<HrBenefitType>(HrBenefitType.class);
        util.exportExcel(response, list, " WELFARE TYPE   LIST Data");
    }

    /**
     * Get  WELFARE TYPE   LIST details
     */
    @GetMapping(value = "/{benefitTypeId}")
    public AjaxResult getInfo(@PathVariable("benefitTypeId") String benefitTypeId) {
        return success(hrBenefitTypeService.selectHrBenefitTypeByBenefitTypeId(benefitTypeId));
    }

    /**
     * Add  WELFARE TYPE   LIST
     */
    @Log(title = " WELFARE TYPE   LIST", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrBenefitType hrBenefitType) {
        hrBenefitType.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrBenefitTypeService.insertHrBenefitType(hrBenefitType));
    }

    /**
     * Update  WELFARE TYPE   LIST
     */
    @Log(title = " WELFARE TYPE   LIST", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrBenefitType hrBenefitType) {
        return toAjax(hrBenefitTypeService.updateHrBenefitType(hrBenefitType));
    }

    /**
     * Delete  WELFARE TYPE   LIST
     */
    @Log(title = " WELFARE TYPE   LIST", businessType = BusinessType.DELETE)
    @DeleteMapping("/{benefitTypeIds}")
    public AjaxResult remove(@PathVariable String[] benefitTypeIds) {
        HrBenefit hrBenefit = new HrBenefit();
        hrBenefit.setBenefitTypeId(benefitTypeIds[0]);
        List<HrBenefit> hrBenefits = hrEmpBenefitService.selectHrBenefitList(hrBenefit);
        if(ObjectUtils.isNotEmpty(hrBenefits)){
            return AjaxResult.warn("Welfare Type is used by employee Welfare Application Form, please delete the employee Welfare Application Form first");
        }
        return toAjax(hrBenefitTypeService.removeByIds(Arrays.asList(benefitTypeIds)));
    }
}
