package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.domain.SysNotificationSettings;

import java.util.List;

/**
 * Notification Settings Service Interface
 *
 * @author ys
 * @date 2025-05-27
 */
public interface ISysNotificationSettingsService extends IService<SysNotificationSettings>
{

    /**
     * QUERYNotification Settings
     *
     * @param id Notification Settings Primary Key
     * @return Notification Settings
     */
    public SysNotificationSettings selectSysNotificationSettingsById(String id);

    /**
     * QUERYNotification Settings  LIST
     *
     * @param sysNotificationSettings Notification Settings
     * @return Notification SettingsSet
     */
    public List<SysNotificationSettings> selectSysNotificationSettingsList(SysNotificationSettings sysNotificationSettings);

    /**
     * ADDNotification Settings
     *
     * @param sysNotificationSettings Notification Settings
     * @return Result
     */
    public int insertSysNotificationSettings(SysNotificationSettings sysNotificationSettings);

    /**
     * MODIFYNotification Settings
     *
     * @param sysNotificationSettings Notification Settings
     * @return Result
     */
    public int updateSysNotificationSettings(SysNotificationSettings sysNotificationSettings);

    /**
     * Batch DELETENotification Settings
     *
     * @param ids The Notification Settings Primary Key to be DELETEDSet
     * @return Result
     */
    public int deleteSysNotificationSettingsByIds(String[] ids);

    /**
     * DELETENotification Settings INFORMATION
     *
     * @param id Notification Settings Primary Key
     * @return Result
     */
    public int deleteSysNotificationSettingsById(String id);

}
