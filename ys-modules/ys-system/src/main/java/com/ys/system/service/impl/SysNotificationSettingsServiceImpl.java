package com.ys.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.utils.DateUtils;
import com.ys.system.domain.SysNotificationSettings;
import com.ys.system.mapper.SysNotificationSettingsMapper;
import com.ys.system.service.ISysNotificationSettingsService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Notification Settings Service Business Layer Processing
 *
 * @author ys
 * @date 2025-05-27
 */
@Service
public class SysNotificationSettingsServiceImpl extends ServiceImpl<SysNotificationSettingsMapper, SysNotificationSettings> implements ISysNotificationSettingsService
{

    /**
     * QUERYNotification Settings
     *
     * @param id Notification Settings Primary Key
     * @return Notification Settings
     */
    @Override
    public SysNotificationSettings selectSysNotificationSettingsById(String id)
    {
        return baseMapper.selectSysNotificationSettingsById(id);
    }

    /**
     * QUERYNotification Settings  LIST
     *
     * @param sysNotificationSettings Notification Settings
     * @return Notification Settings
     */
    @Override
    public List<SysNotificationSettings> selectSysNotificationSettingsList(SysNotificationSettings sysNotificationSettings)
    {
        return baseMapper.selectSysNotificationSettingsList(sysNotificationSettings);
    }

    /**
     * ADDNotification Settings
     *
     * @param sysNotificationSettings Notification Settings
     * @return Result
     */
    @Override
    public int insertSysNotificationSettings(SysNotificationSettings sysNotificationSettings)
    {
        sysNotificationSettings.setCreateTime(DateUtils.getNowDate());
        return baseMapper.insert(sysNotificationSettings);
    }

    /**
     * MODIFYNotification Settings
     *
     * @param sysNotificationSettings Notification Settings
     * @return Result
     */
    @Override
    public int updateSysNotificationSettings(SysNotificationSettings sysNotificationSettings)
    {
        sysNotificationSettings.setUpdateTime(DateUtils.getNowDate());
        return baseMapper.updateById(sysNotificationSettings);
    }

    /**
     * Batch DELETENotification Settings
     *
     * @param ids The Notification Settings Primary Key to be DELETED
     * @return Result
     */
    @Override
    public int deleteSysNotificationSettingsByIds(String[] ids)
    {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * DELETENotification Settings INFORMATION
     *
     * @param id Notification Settings Primary Key
     * @return Result
     */
    @Override
    public int deleteSysNotificationSettingsById(String id)
    {
        return baseMapper.deleteById(id);
    }
}
