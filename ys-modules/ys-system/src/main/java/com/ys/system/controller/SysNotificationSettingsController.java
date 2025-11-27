package com.ys.system.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.domain.SysNotificationSettings;
import com.ys.system.service.ISysNotificationSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Notification SettingsController
 *
 * @author ys
 * @date 2025-05-27
 */
@RestController
@RequestMapping("/notificationSettings")
public class SysNotificationSettingsController extends BaseController
{
    @Autowired
    private ISysNotificationSettingsService sysNotificationSettingsService;

    /**
     * Query Notification Settings list
     */
    @RequiresPermissions("system:notificationSettings:list")
    @GetMapping("/list")
    public TableDataInfo list(SysNotificationSettings sysNotificationSettings)
    {
        startPage();
        List<SysNotificationSettings> list = sysNotificationSettingsService.selectSysNotificationSettingsList(sysNotificationSettings);
        return getDataTable(list);
    }

    /**
     * Export Notification Settings list
     */
    @RequiresPermissions("system:notificationSettings:export")
    @Log(title = "Notification Settings", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysNotificationSettings sysNotificationSettings)
    {
        List<SysNotificationSettings> list = sysNotificationSettingsService.selectSysNotificationSettingsList(sysNotificationSettings);
        ExcelUtil<SysNotificationSettings> util = new ExcelUtil<SysNotificationSettings>(SysNotificationSettings.class);
        util.exportExcel(response, list, "Notification Settings Data");
    }


    /**
     * Obtain Notification SettingsDETAILEDLY Information
     */
    @GetMapping()
    public AjaxResult getInfo() {
        SysNotificationSettings sysNotificationSettings = new SysNotificationSettings();
        sysNotificationSettings.setUserId(SecurityUtils.getUserId());
        List<SysNotificationSettings> sysNotificationSettings1 = sysNotificationSettingsService.selectSysNotificationSettingsList(sysNotificationSettings);
        if (sysNotificationSettings1.isEmpty()){
            sysNotificationSettings.setAccountEmailLevel("1");
            sysNotificationSettings.setLeaveRequestNotification("1");
            sysNotificationSettings.setContractPeriodAlerts("1");
            sysNotificationSettings.setPerformanceEvaluationAlerts("1");
            sysNotificationSettings.setSalaryAndBenefitsNotifications("1");
            sysNotificationSettings.setSmsAccountInfo("1");
            sysNotificationSettings.setShiftScheduleUpdate("1");
            sysNotificationSettings.setSmsOffersPromotions("1");
            sysNotificationSettings.setNewFeaturesUpdates("1");
            sysNotificationSettings.setOffersPromotions("1");
            sysNotificationSettings.setFeedbackSurveys("1");
            sysNotificationSettingsService.save(sysNotificationSettings);
            return AjaxResult.success(sysNotificationSettings);
        }else {
            return AjaxResult.success(sysNotificationSettings1.get(0));
        }
    }

    @PostMapping("/setting")
    public AjaxResult getInfoSetting(@RequestBody Long userId) {
        SysNotificationSettings sysNotificationSettings = new SysNotificationSettings();
        sysNotificationSettings.setUserId(userId);
        List<SysNotificationSettings> sysNotificationSettings1 = sysNotificationSettingsService.selectSysNotificationSettingsList(sysNotificationSettings);
        if (sysNotificationSettings1.isEmpty()){
            sysNotificationSettings.setAccountEmailLevel("1");
            sysNotificationSettings.setLeaveRequestNotification("1");
            sysNotificationSettings.setContractPeriodAlerts("1");
            sysNotificationSettings.setPerformanceEvaluationAlerts("1");
            sysNotificationSettings.setSalaryAndBenefitsNotifications("1");
            sysNotificationSettings.setSmsAccountInfo("1");
            sysNotificationSettings.setShiftScheduleUpdate("1");
            sysNotificationSettings.setSmsOffersPromotions("1");
            sysNotificationSettings.setNewFeaturesUpdates("1");
            sysNotificationSettings.setOffersPromotions("1");
            sysNotificationSettings.setFeedbackSurveys("1");
            sysNotificationSettingsService.save(sysNotificationSettings);
            return AjaxResult.success(sysNotificationSettings);
        }else {
            return AjaxResult.success(sysNotificationSettings1.get(0));
        }
    }

    /**
     * ADDNotification Settings
     */
    @Log(title = "Notification Settings", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@Validated @RequestBody SysNotificationSettings sysNotificationSettings) {
        return toAjax(sysNotificationSettingsService.updateSysNotificationSettings(sysNotificationSettings));
    }
}
