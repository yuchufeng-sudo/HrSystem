package com.ys.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysSecuritySettings;
import com.ys.system.api.domain.SysUser;
import com.ys.system.mapper.SysSecuritySettingsMapper;
import com.ys.system.mapper.SysUserMapper;
import com.ys.system.service.ISysSecuritySettingsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * System security configurations Service Implementation
 *
 * @author ys
 * @date 2025-07-01
 */
@Service
public class SysSecuritySettingsServiceImpl extends ServiceImpl<SysSecuritySettingsMapper, SysSecuritySettings> implements ISysSecuritySettingsService
{

    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * Query System security configurations list
     *
     * @param sysSecuritySettings System security configurations
     * @return System security configurations
     */
    @Override
    public List<SysSecuritySettings> selectSysSecuritySettingsList(SysSecuritySettings sysSecuritySettings)
    {
        return baseMapper.selectSysSecuritySettingsList(sysSecuritySettings);
    }

    @Override
    public SysSecuritySettings getInfo() {
        return getInfo(SecurityUtils.getUserEnterpriseId());
    }

    @Override
    public SysSecuritySettings getInfo(String enterpriseId) {
        SysSecuritySettings sysSecuritySettings = new SysSecuritySettings();
        sysSecuritySettings.setEnterpriseId(enterpriseId);
        List<SysSecuritySettings> sysSecuritySettings1 = selectSysSecuritySettingsList(sysSecuritySettings);
        if (sysSecuritySettings1.isEmpty()){
            sysSecuritySettings.setMinPasswordLength(6);
            sysSecuritySettings.setEnable2fa(false);
            sysSecuritySettings.setRequireNumbers(false);
            sysSecuritySettings.setRequireUppercase(false);
            sysSecuritySettings.setSessionTimeoutMinutes(30);
            sysSecuritySettings.setMaxFailedAttempts(5);
            save(sysSecuritySettings);
            return sysSecuritySettings;
        }else {
            return sysSecuritySettings1.get(0);
        }
    }

    /**
     * Validates a password against specified complexity rules
     * @return true if password meets all requirements, false otherwise
     */
    @Override
    public void validatePassword(String password) {
        if ("00".equals(SecurityUtils.getUserType())){
            return;
        }
        SysSecuritySettings info = getInfo();
        validatePassword(password, info);
    }
    @Override
    public void validatePassword(String password,Long userId) {
        SysUser sysUser = sysUserMapper.selectUserById(userId);
        String enterpriseId = sysUser.getEnterpriseId();
        String userType = sysUser.getUserType();
        if ("00".equals(userType)){
            return;
        }
        SysSecuritySettings info = getInfo(enterpriseId);
        validatePassword(password, info);
    }

    private static void validatePassword(String password, SysSecuritySettings info) {
        int minLength = info.getMinPasswordLength();
        boolean requireDigit = info.getRequireNumbers();
        boolean requireMixedCase = info.getRequireUppercase();
//        boolean requireSpecialChar = info.getRequireSpecialChars();
        // Check for null or empty password
        if (password.isEmpty()) {
            throw new ServiceException("Password cannot be empty");
        }

        // Check for null or insufficient length
        if (password.length() < minLength) {
            throw new ServiceException(String.format("Password must be at least %d characters long", minLength));
        }

        // Check for digit requirement
        if (requireDigit && !Pattern.compile("[0-9]").matcher(password).find()) {
            throw new ServiceException("Password must contain at least one digit");
        }

        // Check for mixed case requirement
        if (requireMixedCase &&
                (!Pattern.compile("[a-z]").matcher(password).find() ||
                        !Pattern.compile("[A-Z]").matcher(password).find())) {
            throw new ServiceException("Password must contain at least uppercase and lowercase letters");
        }

        // Check for special character requirement
//        if (requireSpecialChar && !Pattern.compile("[^a-zA-Z0-9]").matcher(password).find()) {
//            throw new ServiceException("Password must contain at least one special character");
//        }
    }
}
