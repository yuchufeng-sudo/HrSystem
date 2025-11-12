package com.ys.hr.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrEmployees;
import com.ys.hr.service.IHrEmployeesService;
import com.ys.hr.service.ITbCandidateInfoService;
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
import com.ys.hr.domain.HrBackgroundCheckInfo;
import com.ys.hr.service.IHrBackgroundCheckInfoService;
import org.springframework.validation.annotation.Validated;

/**
 * Background check personnel information Controller
 *
 * @author ys
 * @date 2025-06-25
 */
@RestController
@RequestMapping("/backgroundCheckInfo")
public class HrBackgroundCheckInfoController extends BaseController
{
    @Autowired
    private IHrBackgroundCheckInfoService hrBackgroundCheckInfoService;

    @Autowired
    private IHrEmployeesService hrEmployeesService;

    @Autowired
    private ITbCandidateInfoService tbCandidateInfoService;

    /**
     * Query Background check personnel information list
     */
    @RequiresPermissions("hr:backgroundCheckInfo:list")
    @GetMapping("/list")
    public TableDataInfo list(HrBackgroundCheckInfo hrBackgroundCheckInfo)
    {
        hrBackgroundCheckInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrBackgroundCheckInfo> list = hrBackgroundCheckInfoService.selectHrBackgroundCheckInfoList(hrBackgroundCheckInfo);
        return getDataTable(list);
    }

    /**
     * Export Background check personnel information list
     */
    @RequiresPermissions("hr:backgroundCheckInfo:export")
    @Log(title = "Background check personnel information", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrBackgroundCheckInfo hrBackgroundCheckInfo)
    {
        List<HrBackgroundCheckInfo> list = hrBackgroundCheckInfoService.selectHrBackgroundCheckInfoList(hrBackgroundCheckInfo);
        ExcelUtil<HrBackgroundCheckInfo> util = new ExcelUtil<HrBackgroundCheckInfo>(HrBackgroundCheckInfo.class);
        util.exportExcel(response, list, "Background check personnel information Data");
    }

    /**
     * Get Background check personnel information details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(hrBackgroundCheckInfoService.selectHrBackgroundCheckInfoById(id));
    }

    /**
     * Add Background check personnel information
     */
    @RequiresPermissions("hr:backgroundCheckInfo:add")
    @Log(title = "Background check personnel information", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrBackgroundCheckInfo hrBackgroundCheckInfo) {
        hrBackgroundCheckInfo.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrBackgroundCheckInfoService.insertHrBackgroundCheckInfo(hrBackgroundCheckInfo));
    }

    /**
     * Update Background check personnel information
     */
    @RequiresPermissions("hr:backgroundCheckInfo:edit")
    @Log(title = "Background check personnel information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrBackgroundCheckInfo hrBackgroundCheckInfo) {
        return toAjax(hrBackgroundCheckInfoService.updateHrBackgroundCheckInfo(hrBackgroundCheckInfo));
    }

    /**
     * Delete Background check personnel information
     */
    @RequiresPermissions("hr:backgroundCheckInfo:remove")
    @Log(title = "Background check personnel information", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrBackgroundCheckInfoService.removeByIds(Arrays.asList(ids)));
    }

    /**
     * Get a list of employees who are not included in the candidate list
     */
    @GetMapping("/getEmployeeList")
    public AjaxResult getEmployeeList() {
        List<HrEmployees> list = hrEmployeesService.getEmployeeList(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(list);
    }

    /**
     * Get a list of candidates who are not included in the candidate list
     */
    @GetMapping("/getHrCandidateInfoList")
    public AjaxResult getHrCandidateInfoList() {
        List<HrCandidateInfo> list = tbCandidateInfoService.getCandidateInfoList(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(list);
    }

}
