package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmpBenefit;
import com.ys.hr.service.IHrEmpBenefitService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *   Employee Welfare Application Form Controller
 *
 * @author ys
 * @date 2025-06-09
 */
@RestController
@RequestMapping("/empBenefit")
public class HrEmpBenefitController extends BaseController
{
    @Resource
    private IHrEmpBenefitService hrEmpBenefitService;

    /**
     * Query Employee Welfare Application Form list
     */
    @RequiresPermissions("hr:empBenefit:list")
    @GetMapping("/list")
    public TableDataInfo list(HrEmpBenefit hrEmpBenefit)
    {
        if(ObjectUtils.isNotEmpty(hrEmpBenefit.getFlag())){
            hrEmpBenefit.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        }else{
            hrEmpBenefit.setUserId(SecurityUtils.getUserId());
        }
        startPage();
        List<HrEmpBenefit> list = hrEmpBenefitService.selectHrEmpBenefitList(hrEmpBenefit);
        return getDataTable(list);
    }

    /**
     * Export Employee Welfare Application Form list
     */
    @RequiresPermissions("hr:empBenefit:export")
    @Log(title = "Employee Welfare Application Form", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrEmpBenefit hrEmpBenefit)
    {
        List<HrEmpBenefit> list = hrEmpBenefitService.selectHrEmpBenefitList(hrEmpBenefit);
        ExcelUtil<HrEmpBenefit> util = new ExcelUtil<HrEmpBenefit>(HrEmpBenefit.class);
        util.exportExcel(response, list, "  Employee Welfare Application Form Data");
    }

    /**
     * Get Employee Welfare Application Form details
     */
    @GetMapping(value = "/{benefitEmpId}")
    public AjaxResult getInfo(@PathVariable("benefitEmpId") String benefitEmpId) {
        return success(hrEmpBenefitService.selectHrEmpBenefitByBenefitEmpId(benefitEmpId));
    }

    /**
     * Add Employee Welfare Application Form
     */
    @RequiresPermissions("hr:empBenefit:add")
    @Log(title = "Employee Welfare Application Form", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrEmpBenefit hrEmpBenefit) {
        return toAjax(hrEmpBenefitService.insertHrEmpBenefit(hrEmpBenefit));
    }

    /**
     * Update Employee Welfare Application Form
     */
    @RequiresPermissions("hr:empBenefit:edit")
    @Log(title = "Employee Welfare Application Form", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrEmpBenefit hrEmpBenefit) {
        return toAjax(hrEmpBenefitService.updateHrEmpBenefit(hrEmpBenefit));
    }

    /**
     * Delete Employee Welfare Application Form
     */
    @Log(title = "Employee Welfare Application Form", businessType = BusinessType.DELETE)
    @DeleteMapping("/{benefitEmpIds}")
    public AjaxResult remove(@PathVariable String[] benefitEmpIds) {
        return toAjax(hrEmpBenefitService.removeByIds(Arrays.asList(benefitEmpIds)));
    }
}
