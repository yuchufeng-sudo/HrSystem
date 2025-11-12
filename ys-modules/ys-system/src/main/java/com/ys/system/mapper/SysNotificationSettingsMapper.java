package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.domain.SysNotificationSettings;

import java.util.List;

/**
 * Notification SettingsMapper Interface
 *
 * @author ys
 * @date 2025-05-27
 */
public interface SysNotificationSettingsMapper extends BaseMapper<SysNotificationSettings>
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

}
