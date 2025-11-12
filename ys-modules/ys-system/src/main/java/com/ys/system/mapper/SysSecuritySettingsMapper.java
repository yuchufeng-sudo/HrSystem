package com.ys.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ys.system.api.domain.SysSecuritySettings;

import java.util.List;

/**
 * System security configurations Mapper Interface
 *
 * @author ys
 * @date 2025-07-01
 */
public interface SysSecuritySettingsMapper extends BaseMapper<SysSecuritySettings>
{
    /**
     * Query System security configurations
     *
     * @param id System security configurations primary key
     * @return System security configurations
     */
    public SysSecuritySettings selectSysSecuritySettingsById(String id);

    /**
     * Query System security configurations list
     *
     * @param sysSecuritySettings System security configurations
     * @return System security configurations collection
     */
    public List<SysSecuritySettings> selectSysSecuritySettingsList(SysSecuritySettings sysSecuritySettings);

}
