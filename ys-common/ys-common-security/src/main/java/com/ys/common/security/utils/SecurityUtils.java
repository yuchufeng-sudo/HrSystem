package com.ys.common.security.utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.ys.common.core.constant.CacheConstants;
import com.ys.common.redis.service.RedisService;
import com.ys.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.TokenConstants;
import com.ys.common.core.context.SecurityContextHolder;
import com.ys.common.core.utils.ServletUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.system.api.model.LoginUser;

/**
 * Security Utility Class
 * Provides utility methods for retrieving user information and handling authentication
 *
 * @author ruoyi
 */
public class SecurityUtils
{
    /**
     * Get user ID
     *
     * @return User ID
     */
    public static Long getUserId()
    {
        return SecurityContextHolder.getUserId();
    }

    /**
     * Get username
     *
     * @return Username
     */
    public static String getUsername()
    {
        return SecurityContextHolder.getUserName();
    }

    /**
     * Get user key
     *
     * @return User key
     */
    public static String getUserKey()
    {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * Get logged-in user information
     *
     * @return Login user object
     */
    public static LoginUser getLoginUser()
    {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * Get request token
     *
     * @return Token string
     */
    public static String getToken()
    {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * Get request token from HTTP request
     *
     * @param request HTTP request
     * @return Token string
     */
    public static String getToken(HttpServletRequest request)
    {
        // Get token identifier from header
        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     * Remove token prefix
     *
     * @param token Token with possible prefix
     * @return Token without prefix
     */
    public static String replaceTokenPrefix(String token)
    {
        // If frontend sets token prefix, remove the prefix
        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     * Check if user is administrator
     *
     * @param userId User ID
     * @return true if administrator, false otherwise
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    /**
     * Generate BCrypt encrypted password
     *
     * @param password Plain text password
     * @return Encrypted password string
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Check if password matches
     *
     * @param rawPassword Plain text password
     * @param encodedPassword Encrypted password
     * @return true if passwords match, false otherwise
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Set user's associated enterprise ID
     *
     * @param enterpriseId Enterprise ID to set
     */
    public static void setUserEnterpriseId(String enterpriseId)
    {
        SecurityContextHolder.setEnterpriseId(enterpriseId);
    }

    /**
     * Get user's associated enterprise ID
     *
     * @return Enterprise ID
     */
    public static String getUserEnterpriseId()
    {
        SysUser user = getLoginUser().getSysUser();
        if (user.getUserType().equals("00")){
            return SecurityContextHolder.getEnterpriseId();
        }else {
            return user.getEnterpriseId();
        }
    }

    /**
     * Get user type
     *
     * @return User type
     */
    public static String getUserType() {
        return getLoginUser().getSysUser().getUserType();
    }
}
