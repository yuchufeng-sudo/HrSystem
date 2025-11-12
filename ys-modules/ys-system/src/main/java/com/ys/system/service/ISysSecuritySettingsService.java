package com.ys.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ys.system.api.domain.SysSecuritySettings;

import java.util.List;

/**
 * System security configurations Service Interface
 *
 * @author ys
 * @date 2025-07-01
 */
public interface ISysSecuritySettingsService extends IService<SysSecuritySettings>
{
    /**
     * Query System security configurations list
     *
     * @param sysSecuritySettings System security configurations
     * @return System security configurations collection
     */
    public List<SysSecuritySettings> selectSysSecuritySettingsList(SysSecuritySettings sysSecuritySettings);

    SysSecuritySettings getInfo();

    SysSecuritySettings getInfo(String enterpriseId);

    void validatePassword(String password);
    void validatePassword(String password, Long userId);
}
