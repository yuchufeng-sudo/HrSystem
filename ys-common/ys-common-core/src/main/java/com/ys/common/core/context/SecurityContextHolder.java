package com.ys.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.text.Convert;
import com.ys.common.core.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *Obtain the User id, User Name, Token and other Information from the current thread variables
 * Note: It must be passed in through the method of the Request header in the gateway, and the value must be set in the HeaderInterceptor interceptor. Otherwise, it cannot be OBTAINED here
 *
 * @author ys
 */
public class SecurityContextHolder
{
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void set(String key, Object value)
    {
        Map<String, Object> map = getLocalMap();
        map.put(key, value == null ? StringUtils.EMPTY : value);
    }

    public static String get(String key)
    {
        Map<String, Object> map = getLocalMap();
        return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
    }

    public static <T> T get(String key, Class<T> clazz)
    {
        Map<String, Object> map = getLocalMap();
        return StringUtils.cast(map.getOrDefault(key, null));
    }

    public static Map<String, Object> getLocalMap()
    {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null)
        {
            map = new ConcurrentHashMap<String, Object>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, Object> threadLocalMap)
    {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static String getEnterpriseId()
    {
        return Convert.toStr(get(SecurityConstants.DETAILS_ENTERPRISE_ID));
    }

    public static void setEnterpriseId(String account)
    {
        set(SecurityConstants.DETAILS_ENTERPRISE_ID, account);
    }

    public static Long getUserId()
    {
        return Convert.toLong(get(SecurityConstants.DETAILS_USER_ID), 0L);
    }

    public static void setUserId(String account)
    {
        set(SecurityConstants.DETAILS_USER_ID, account);
    }

    public static String getUserName()
    {
        return get(SecurityConstants.DETAILS_USERNAME);
    }

    public static void setUserName(String username)
    {
        set(SecurityConstants.DETAILS_USERNAME, username);
    }

    public static String getUserKey()
    {
        return get(SecurityConstants.USER_KEY);
    }

    public static void setUserKey(String userKey)
    {
        set(SecurityConstants.USER_KEY, userKey);
    }

    public static String getPermission()
    {
        return get(SecurityConstants.ROLE_PERMISSION);
    }

    public static void setPermission(String permissions)
    {
        set(SecurityConstants.ROLE_PERMISSION, permissions);
    }

    public static void remove()
    {
        THREAD_LOCAL.remove();
    }
}
