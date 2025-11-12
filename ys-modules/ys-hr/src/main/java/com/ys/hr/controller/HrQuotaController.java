package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrCandidateInfo;
import com.ys.hr.domain.HrQuota;
import com.ys.hr.service.IHrQuotaService;
import com.ys.hr.service.ITbCandidateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Personnel Quota Management Controller
 *
 * @author ys
 * @date 2025-06-23
 */
@RestController
@RequestMapping("/quota")
public class HrQuotaController extends BaseController
{
    @Autowired
    private IHrQuotaService hrQuotaService;

    @Autowired
    private ITbCandidateInfoService tbCandidateInfoService;

    /**
     * Query Personnel Quota Management list
     */
    @RequiresPermissions("hr:quota:list")
    @GetMapping("/list")
    public TableDataInfo list(HrQuota hrQuota)
    {
        hrQuota.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        startPage();
        List<HrQuota> list = hrQuotaService.selectHrQuotaList(hrQuota);
        return getDataTable(list);
    }

    /**
     * Export Personnel Quota Management list
     */
    @RequiresPermissions("hr:quota:export")
    @Log(title = "Personnel Quota Management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrQuota hrQuota)
    {
        List<HrQuota> list = hrQuotaService.selectHrQuotaList(hrQuota);
        ExcelUtil<HrQuota> util = new ExcelUtil<HrQuota>(HrQuota.class);
        util.exportExcel(response, list, "Personnel Quota Management Data");
    }

    /**
     * Get Personnel Quota Management details
     */
    @GetMapping(value = "/{quotaId}")
    public AjaxResult getInfo(@PathVariable("quotaId") Long quotaId) {
        return success(hrQuotaService.selectHrQuotaByQuotaId(quotaId));
    }

    /**
     * Add Personnel Quota Management
     */
    @RequiresPermissions("hr:quota:add")
    @Log(title = "Personnel Quota Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrQuota hrQuota) {
        hrQuota.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        return toAjax(hrQuotaService.insertHrQuota(hrQuota));
    }

    /**
     * Update Personnel Quota Management
     */
    @RequiresPermissions("hr:quota:edit")
    @Log(title = "Personnel Quota Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrQuota hrQuota) {
        return toAjax(hrQuotaService.updateHrQuota(hrQuota));
    }

    /**
     * Delete Personnel Quota Management
     */
    @RequiresPermissions("hr:quota:remove")
    @Log(title = "Personnel Quota Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{quotaIds}")
    public AjaxResult remove(@PathVariable Long[] quotaIds) {
            HrCandidateInfo tbCandidateInfo = new HrCandidateInfo();
            tbCandidateInfo.setJobInformation(quotaIds[0]);
            List<HrCandidateInfo> hrCandidateInfos = tbCandidateInfoService.selectTbCandidateInfoList(tbCandidateInfo);
            if(!hrCandidateInfos.isEmpty()){
                AjaxResult.warn("Employees exist in this position");
            }
            return toAjax(hrQuotaService.removeByIds(Arrays.asList(quotaIds)));
    }
}
