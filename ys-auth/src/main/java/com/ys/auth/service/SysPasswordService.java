package com.ys.auth.service;

import com.ys.common.core.constant.CacheConstants;
import com.ys.common.core.constant.Constants;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.domain.R;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.redis.service.RedisService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.RemoteUserService;
import com.ys.system.api.domain.SysSecuritySettings;
import com.ys.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Login Password
 *
 * @author ruoyi
 */
@Component
public class SysPasswordService
{
    @Autowired
    private RedisService redisService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Resource
    private RemoteUserService remoteUserService;

    /**
     *
     *
     * @param username USER Name
     * @return
     */
    private String getCacheKey(String username)
    {
        return CacheConstants.CIPHER_ERR_CNT_KEY + username;
    }

    public void validate(SysUser user, String password)
    {
        String username = user.getUserName();
        SysSecuritySettings settings = null;
        int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;
        if ("00".equals(user.getUserType())){
            settings = new SysSecuritySettings();
            settings.setMaxFailedAttempts(maxRetryCount);
        }else {
            R<SysSecuritySettings> settingInfo = remoteUserService.getSettingInfo(user.getEnterpriseId(), SecurityConstants.INNER);
            settings = settingInfo.getData();
        }
        Integer retryCount = redisService.getCacheObject(getCacheKey(username));

        if (retryCount == null)
        {
            retryCount = 0;
        }

        Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;
        if (retryCount >= settings.getMaxFailedAttempts())
        {
            String errMsg = String.format("The password was entered incorrectly %s times, and the account is locked for %s minutes.", maxRetryCount, lockTime);
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL,errMsg);
            throw new ServiceException(errMsg);
        }

        if (!matches(user, password))
        {
            retryCount = retryCount + 1;
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, String.format("The password was entered incorrectly %s times.", retryCount));
            redisService.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException("User does not exist/Password is incorrect.");
        }
        else
        {
            clearLoginRecordCache(username);
        }
    }

    public boolean matches(SysUser user, String rawPassword)
    {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    public void clearLoginRecordCache(String loginName)
    {
        if (redisService.hasKey(getCacheKey(loginName)))
        {
            redisService.deleteObject(getCacheKey(loginName));
        }
    }
}
