package com.ys.hr.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.hr.domain.HrSettings;
import com.ys.hr.service.IHrSettingsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Hr settings Controller
 *
 * @author ys
 * @date 2025-05-28
 */
@RestController
@RequestMapping("/settings")
public class HrSettingsController extends BaseController
{
    @Autowired
    private IHrSettingsService hrSettingsService;

    /**
     * Query Hr settings list
     */
    @RequiresPermissions("hr:settings:list")
    @GetMapping("/list")
    public TableDataInfo list(HrSettings hrSettings)
    {
        startPage();
        List<HrSettings> list = hrSettingsService.selectHrSettingsList(hrSettings);
        return getDataTable(list);
    }

    /**
     * Export Hr settings list
     */
    @RequiresPermissions("hr:settings:export")
    @Log(title = "Hr settings", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, HrSettings hrSettings)
    {
        List<HrSettings> list = hrSettingsService.selectHrSettingsList(hrSettings);
        ExcelUtil<HrSettings> util = new ExcelUtil<HrSettings>(HrSettings.class);
        util.exportExcel(response, list, "Hr settings Data");
    }

    /**
     * Get Hr settings details
     */
    @GetMapping("/company")
    public AjaxResult getInfo() {
        String Eid = SecurityUtils.getUserEnterpriseId();
        return success(hrSettingsService.selectHrSettingsByEid(Eid));
    }

    /**
     * Add Hr settings
     */
    @Log(title = "Hr settings", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody HrSettings hrSettings) {
        hrSettings.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
        HrSettings settings = hrSettingsService.selectHrSettingsByEid(SecurityUtils.getUserEnterpriseId());
        if(ObjectUtils.isNotEmpty(settings)){
            hrSettings.setId(settings.getId());
            return toAjax(hrSettingsService.updateHrSettings(hrSettings));
        }
        return toAjax(hrSettingsService.insertHrSettings(hrSettings));
    }

    /**
     * Update Hr settings
     */
    @Log(title = "Hr settings", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody HrSettings hrSettings) {
        return toAjax(hrSettingsService.updateHrSettings(hrSettings));
    }

    /**
     * Delete Hr settings
     */
    @RequiresPermissions("hr:settings:remove")
    @Log(title = "Hr settings", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(hrSettingsService.removeByIds(Arrays.asList(ids)));
    }
}
