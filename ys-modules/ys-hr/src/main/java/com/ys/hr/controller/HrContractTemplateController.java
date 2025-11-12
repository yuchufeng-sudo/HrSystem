package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrEmployeeContract;
import com.ys.hr.service.IHrEmployeeContractService;
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
import com.ys.hr.domain.HrContractTemplate;
import com.ys.hr.service.IHrContractTemplateService;
import org.springframework.validation.annotation.Validated;

/**
 * CONTRACT TEMPLATE INFORMATION Controller
 *
 * @author ys
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/contractTemplate")
public class HrContractTemplateController extends BaseController
{
    @Autowired
    private IHrContractTemplateService hrContractTemplateService;

    @Autowired
    private IHrEmployeeContractService hrEmployeeContractService;

    /**
     * Query CONTRACT TEMPLATE INFORMATION list
     */
//    @RequiresPermissions("hr:contractTemplate:list")
    @GetMapping("/list")
    public TableDataInfo list(HrContractTemplate hrContractTemplate)
    {
        hrContractTemplate.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrContractTemplate> list = hrContractTemplateService.selectHrContractTemplateList(hrContractTemplate);
        return getDataTable(list);
    }

    /**
     * Export CONTRACT TEMPLATE INFORMATION list
     */
//    @RequiresPermissions("hr:contractTemplate:export")
    @Log(title = "CONTRACT TEMPLATE INFORMATION", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrContractTemplate hrContractTemplate)
    {
        List<HrContractTemplate> list = hrContractTemplateService.selectHrContractTemplateList(hrContractTemplate);
        ExcelUtil<HrContractTemplate> util = new ExcelUtil<HrContractTemplate>(HrContractTemplate.class);
        util.exportExcel(response, list, "CONTRACT TEMPLATE INFORMATION Data");
    }

    /**
     * Get CONTRACT TEMPLATE INFORMATION details
     */
//    @RequiresPermissions("hr:contractTemplate:query")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId) {
        return success(hrContractTemplateService.selectHrContractTemplateByTemplateId(templateId));
    }

    /**
     * Add CONTRACT TEMPLATE INFORMATION
     */
//    @RequiresPermissions("hr:contractTemplate:add")
    @Log(title = "CONTRACT TEMPLATE INFORMATION", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrContractTemplate hrContractTemplate) {
        return toAjax(hrContractTemplateService.insertHrContractTemplate(hrContractTemplate));
    }

    /**
     * Update CONTRACT TEMPLATE INFORMATION
     */
//    @RequiresPermissions("hr:contractTemplate:edit")
    @Log(title = "CONTRACT TEMPLATE INFORMATION", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrContractTemplate hrContractTemplate) {
        return toAjax(hrContractTemplateService.updateHrContractTemplate(hrContractTemplate));
    }

    /**
     * Delete CONTRACT TEMPLATE INFORMATION
     */
//    @RequiresPermissions("hr:contractTemplate:remove")
    @Log(title = "CONTRACT TEMPLATE INFORMATION", businessType = BusinessType.DELETE)
    @DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds) {
        return toAjax(hrContractTemplateService.removeByIds(Arrays.asList(templateIds)));
    }

    /**
     * Query CONTRACT TEMPLATE INFORMATION list
     */
//    @RequiresPermissions("hr:contractTemplate:list")
    @GetMapping("/templateList")
    public AjaxResult templateList(HrContractTemplate hrContractTemplate)
    {
        hrContractTemplate.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
       return AjaxResult.success(hrContractTemplateService.selectHrContractTemplateList(hrContractTemplate));
    }

    /**
     * QUERY INFORMATION
     * @return
     */
    @PostMapping(value = "/getInfo")
    public AjaxResult getInfo(@RequestBody HrEmployeeContract employeeContract) {
        HrContractTemplate contractTemplate =
                hrContractTemplateService.selectHrContractTemplateByTemplateId(employeeContract.getTemplateId());
        String templateContent = contractTemplate.getTemplateContent();
        contractTemplate.setTemplateContent(hrEmployeeContractService.replaceContent(employeeContract,templateContent));
        return AjaxResult.success(contractTemplate);
    }
}
