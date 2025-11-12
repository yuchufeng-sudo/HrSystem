package com.ys.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ys.common.core.domain.R;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysSecuritySettings;
import com.ys.system.service.ISysEnterpriseService;
import com.ys.system.service.ISysSecuritySettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * System security configurations Controller
 *
 * @author ys
 * @date 2025-07-01
 */
@RestController
@RequestMapping("/securitySettings")
public class SysSecuritySettingsController extends BaseController
{
    @Autowired
    private ISysSecuritySettingsService sysSecuritySettingsService;


    /**
     * OBTAIN Workspace SettingsDETAILEDLY INFORMATION
     */
    @GetMapping()
    public AjaxResult getInfo() {
        return AjaxResult.success(sysSecuritySettingsService.getInfo());
    }

    /**
     * ADDWorkspace Settings
     */
    @Log(title = "Workspace Settings", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysSecuritySettings sysSecuritySettings) {
        QueryWrapper<SysSecuritySettings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enterprise_id",SecurityUtils.getUserEnterpriseId());
        return toAjax(sysSecuritySettingsService.update(sysSecuritySettings,queryWrapper));
    }

    /**
     * Get enterprise security settings
     * @param enterpriseId
     * @return
     */
    @GetMapping("/getSettingInfo/{enterpriseId}")
    public R<?> getSettingInfo(@PathVariable("enterpriseId") String enterpriseId) {
        return R.ok(sysSecuritySettingsService.getInfo(enterpriseId));
    }
}
