package com.ys.common.security.auth;

import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.annotation.RequiresRoles;
import com.ys.system.api.model.LoginUser;

/**
 * Token Permission Validation Utility Class
 * Static utility methods for authentication and authorization operations
 *
 * @author ruoyi
 */
public class AuthUtil
{
    /**
     * Underlying AuthLogic object
     */
    public static AuthLogic authLogic = new AuthLogic();

    /**
     * Session logout
     */
    public static void logout()
    {
        authLogic.logout();
    }

    /**
     * Session logout by specified token
     *
     * @param token Specified token
     */
    public static void logoutByToken(String token)
    {
        authLogic.logoutByToken(token);
    }

    /**
     * Verify if current session is logged in, throws exception if not logged in
     */
    public static void checkLogin()
    {
        authLogic.checkLogin();
    }

    /**
     * Get current logged-in user information
     *
     * @param token Specified token
     * @return User information
     */
    public static LoginUser getLoginUser(String token)
    {
        return authLogic.getLoginUser(token);
    }

    /**
     * Validate current user's validity period
     *
     * @param loginUser User information
     */
    public static void verifyLoginUserExpire(LoginUser loginUser)
    {
        authLogic.verifyLoginUserExpire(loginUser);
    }

    /**
     * Check if current account has specified role identifier, returns true or false
     *
     * @param role Role identifier
     * @return Whether the specified role identifier is present
     */
    public static boolean hasRole(String role)
    {
        return authLogic.hasRole(role);
    }

    /**
     * Check if current account has specified role identifier, throws NotRoleException if validation fails
     *
     * @param role Role identifier
     */
    public static void checkRole(String role)
    {
        authLogic.checkRole(role);
    }

    /**
     * Authorize based on annotation parameters, throws NotRoleException if validation fails
     *
     * @param requiresRoles Role permission annotation
     */
    public static void checkRole(RequiresRoles requiresRoles)
    {
        authLogic.checkRole(requiresRoles);
    }

    /**
     * Check if current account has specified role identifiers [multiple roles, all must pass validation]
     *
     * @param roles Role identifier array
     */
    public static void checkRoleAnd(String... roles)
    {
        authLogic.checkRoleAnd(roles);
    }

    /**
     * Check if current account has specified role identifiers [multiple roles, any one passing validation is sufficient]
     *
     * @param roles Role identifier array
     */
    public static void checkRoleOr(String... roles)
    {
        authLogic.checkRoleOr(roles);
    }

    /**
     * Check if current account has specified permission, returns true or false
     *
     * @param permission Permission code
     * @return Whether the specified permission is present
     */
    public static boolean hasPermi(String permission)
    {
        return authLogic.hasPermi(permission);
    }

    /**
     * Check if current account has specified permission, throws NotPermissionException if validation fails
     *
     * @param permission Permission code
     */
    public static void checkPermi(String permission)
    {
        authLogic.checkPermi(permission);
    }

    /**
     * Authorize based on annotation parameters, throws NotPermissionException if validation fails
     *
     * @param requiresPermissions Permission annotation
     */
    public static void checkPermi(RequiresPermissions requiresPermissions)
    {
        authLogic.checkPermi(requiresPermissions);
    }

    /**
     * Check if current account has specified permissions [multiple permissions, all must pass validation]
     *
     * @param permissions Permission code array
     */
    public static void checkPermiAnd(String... permissions)
    {
        authLogic.checkPermiAnd(permissions);
    }

    /**
     * Check if current account has specified permissions [multiple permissions, any one passing validation is sufficient]
     *
     * @param permissions Permission code array
     */
    public static void checkPermiOr(String... permissions)
    {
        authLogic.checkPermiOr(permissions);
    }
}
