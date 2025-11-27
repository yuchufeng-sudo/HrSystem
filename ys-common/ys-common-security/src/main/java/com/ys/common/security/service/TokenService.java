package com.ys.common.security.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ys.common.core.domain.R;
import com.ys.system.api.RemoteUserService;
import com.ys.system.api.domain.SysSecuritySettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ys.common.core.constant.CacheConstants;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.utils.JwtUtils;
import com.ys.common.core.utils.ServletUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.ip.IpUtils;
import com.ys.common.core.utils.uuid.IdUtils;
import com.ys.common.redis.service.RedisService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.utils.ip.AddressUtils;
import com.ys.system.api.model.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * Token Validation Service
 * Handles token creation, validation, and user session management
 *
 * @author ruoyi
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private RedisService redisService;

    @Resource
    private RemoteUserService remoteUserService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * Create token for user login
     *
     * @param loginUser Login user information
     * @return Map containing access token and expiration time
     */
    public Map<String, Object> createToken(LoginUser loginUser)
    {
        String token = IdUtils.fastUUID();
        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();
        String enterpriseId = loginUser.getSysUser().getEnterpriseId();
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr());
        setUserAgent(loginUser);
        refreshToken(loginUser);

        // JWT stored information
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, token);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);
        claimsMap.put(SecurityConstants.DETAILS_ENTERPRISE_ID, enterpriseId);
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName);

        // API response information
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * Set user agent information
     *
     * @param loginUser Login user information
     */
    public void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(Objects.requireNonNull(ServletUtils.getRequest()).getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * Get user identity information from current request
     *
     * @return User information
     */
    public LoginUser getLoginUser()
    {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * Get user identity information from HTTP request
     *
     * @param request HTTP request
     * @return User information
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // Get token from request
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * Get user identity information by token
     *
     * @param token Authentication token
     * @return User information
     */
    public LoginUser getLoginUser(String token)
    {
        LoginUser user = null;
        try
        {
            if (StringUtils.isNotEmpty(token))
            {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        }
        catch (Exception e)
        {
            log.error("Failed to get user information '{}'", e.getMessage());
        }
        return user;
    }

    /**
     * Set user identity information
     *
     * @param loginUser Login user information
     */
    public void setLoginUser(LoginUser loginUser)
    {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            refreshToken(loginUser);
        }
    }

    /**
     * Delete user cache information
     *
     * @param token Authentication token
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * Verify token validity period, automatically refreshes cache if less than 120 minutes remaining
     *
     * @param loginUser Login user information
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }

    /**
     * Refresh token validity period
     *
     * @param loginUser Login user information
     */
    public void refreshToken(LoginUser loginUser)
    {
        SysSecuritySettings setting = getSetting(loginUser);
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + setting.getSessionTimeoutMinutes() * 24 * MILLIS_MINUTE);

        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, (long) (setting.getSessionTimeoutMinutes() * 60 * 24), TimeUnit.MINUTES);
    }

    /**
     * Get token cache key
     *
     * @param token User token
     * @return Cache key
     */
    private String getTokenKey(String token)
    {
        return ACCESS_TOKEN + token;
    }

    private SysSecuritySettings getSetting(LoginUser loginUser){
        if ("00".equals(loginUser.getSysUser().getUserType())){
            SysSecuritySettings setting = new SysSecuritySettings();
            setting.setSessionTimeoutMinutes(30);
            return setting;
        }
        R<SysSecuritySettings> settingInfo =
                remoteUserService.getSettingInfo(loginUser.getSysUser().getEnterpriseId(), SecurityConstants.INNER);
        return settingInfo.getData();
    }
}
