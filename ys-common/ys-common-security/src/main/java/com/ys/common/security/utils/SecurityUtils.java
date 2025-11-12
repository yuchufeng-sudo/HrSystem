package com.ys.common.security.utils;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.TokenConstants;
import com.ys.common.core.context.SecurityContextHolder;
import com.ys.common.core.utils.ServletUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.model.LoginUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;

/**
 * PermissionOBTAIN
 *
 * @author ruoyi
 */
public class SecurityUtils
{
    /**
     * OBTAIN USER ID
     */
    public static Long getUserId()
    {
        return SecurityContextHolder.getUserId();
    }

    /**
     * OBTAIN USER Name
     */
    public static String getUsername()
    {
        return SecurityContextHolder.getUserName();
    }

    /**
     * OBTAIN USER key
     */
    public static String getUserKey()
    {
        return SecurityContextHolder.getUserKey();
    }

    /**
     * OBTAIN Login  USER INFORMATION
     */
    public static LoginUser getLoginUser()
    {
        return SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
    }

    /**
     * OBTAIN  Requesttoken
     */
    public static String getToken()
    {
        return getToken(ServletUtils.getRequest());
    }

    /**
     * According to requestOBTAIN  Requesttoken
     */
    public static String getToken(HttpServletRequest request)
    {

        String token = request.getHeader(TokenConstants.AUTHENTICATION);
        return replaceTokenPrefix(token);
    }

    /**
     *
     */
    public static String replaceTokenPrefix(String token)
    {

        if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX))
        {
            token = token.replaceFirst(TokenConstants.PREFIX, "");
        }
        return token;
    }

    /**
     *
     *
     * @param userId USER ID
     * @return Result
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    /**
     * GenerateBCryptPasswordEncoderPassword
     *
     * @param password Password
     * @return  Character string
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     *
     *
     * @param rawPassword
     * @param encodedPassword
     * @return Result
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     *
     * */
    public static void setUserEnterpriseId(String enterpriseId)
    {
        SecurityContextHolder.setEnterpriseId(enterpriseId);
    }

    /**
     *
     * */
    public static String getUserEnterpriseId()
    {
        SysUser user = getLoginUser().getSysUser();
        if (user.getUserType().equals("00")){
            return SecurityContextHolder.getEnterpriseId();
        }else {
            return user.getEnterpriseId();
        }
    }

    public static String getUserType() {
        return getLoginUser().getSysUser().getUserType();
    }
}
