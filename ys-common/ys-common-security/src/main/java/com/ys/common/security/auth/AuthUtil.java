package com.ys.common.security.auth;

import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.annotation.RequiresRoles;
import com.ys.system.api.model.LoginUser;

/**
 * Token Permission
 * 
 * @author ruoyi
 */
public class AuthUtil
{
    /**
     * AuthLogic Object
     */
    public static AuthLogic authLogic = new AuthLogic();

    /**
     * Logout
     */
    public static void logout()
    {
        authLogic.logout();
    }

    /**
     * 
     * 
     * @param token 
     */
    public static void logoutByToken(String token)
    {
        authLogic.logoutByToken(token);
    }

    /**
     * 
     */
    public static void checkLogin()
    {
        authLogic.checkLogin();
    }

    /**
     * 
     * 
     * @param token 
     * @return  USER INFORMATION
     */
    public static LoginUser getLoginUser(String token)
    {
        return authLogic.getLoginUser(token);
    }

    /**
     * 
     * 
     * @param loginUser  USER INFORMATION
     */
    public static void verifyLoginUserExpire(LoginUser loginUser)
    {
        authLogic.verifyLoginUserExpire(loginUser);
    }

    /**
     * 
     * 
     * @param role 
     * @return 
     */
    public static boolean hasRole(String role)
    {
        return authLogic.hasRole(role);
    }

    /**
     * 
     * 
     * @param role 
     */
    public static void checkRole(String role)
    {
        authLogic.checkRole(role);
    }

    /**
     * 
     * 
     * @param requiresRoles 
     */
    public static void checkRole(RequiresRoles requiresRoles)
    {
        authLogic.checkRole(requiresRoles);
    }

    /**
     * 
     * 
     * @param roles  Array
     */
    public static void checkRoleAnd(String... roles)
    {
        authLogic.checkRoleAnd(roles);
    }

    /**
     * 
     * 
     * @param roles  Array
     */
    public static void checkRoleOr(String... roles)
    {
        authLogic.checkRoleOr(roles);
    }

    /**
     * 
     * 
     * @param permission 
     * @return Permission
     */
    public static boolean hasPermi(String permission)
    {
        return authLogic.hasPermi(permission);
    }

    /**
     * 
     * 
     * @param permission 
     */
    public static void checkPermi(String permission)
    {
        authLogic.checkPermi(permission);
    }

    /**
     * 
     * 
     * @param requiresPermissions 
     */
    public static void checkPermi(RequiresPermissions requiresPermissions)
    {
        authLogic.checkPermi(requiresPermissions);
    }

    /**
     * 
     * 
     * @param permissions  Array
     */
    public static void checkPermiAnd(String... permissions)
    {
        authLogic.checkPermiAnd(permissions);
    }

    /**
     *
     * 
     * @param permissions  Array
     */
    public static void checkPermiOr(String... permissions)
    {
        authLogic.checkPermiOr(permissions);
    }
}
