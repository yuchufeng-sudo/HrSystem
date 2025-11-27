package com.ys.common.security.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.util.PatternMatchUtils;
import com.ys.common.core.context.SecurityContextHolder;
import com.ys.common.core.exception.auth.NotLoginException;
import com.ys.common.core.exception.auth.NotPermissionException;
import com.ys.common.core.exception.auth.NotRoleException;
import com.ys.common.core.utils.SpringUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.security.annotation.Logical;
import com.ys.common.security.annotation.RequiresLogin;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.annotation.RequiresRoles;
import com.ys.common.security.service.TokenService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.model.LoginUser;

/**
 * Token Permission Validation Logic Implementation
 * Core logic for authentication and authorization operations
 *
 * @author ruoyi
 */
public class AuthLogic
{
    /** All permissions identifier */
    private static final String ALL_PERMISSION = "*:*:*";

    /** Administrator role permission identifier */
    private static final String SUPER_ADMIN = "admin";

    public TokenService tokenService = SpringUtils.getBean(TokenService.class);

    /**
     * Session logout
     */
    public void logout()
    {
        String token = SecurityUtils.getToken();
        if (token == null)
        {
            return;
        }
        logoutByToken(token);
    }

    /**
     * Session logout by specified token
     *
     * @param token Token to logout
     */
    public void logoutByToken(String token)
    {
        tokenService.delLoginUser(token);
    }

    /**
     * Verify if user is logged in, throws exception if not logged in
     */
    public void checkLogin()
    {
        getLoginUser();
    }

    /**
     * Get current user cache information, throws exception if not logged in
     *
     * @return User cache information
     */
    public LoginUser getLoginUser()
    {
        String token = SecurityUtils.getToken();
        if (token == null)
        {
            throw new NotLoginException("Token not provided");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null)
        {
            throw new NotLoginException("Invalid token");
        }
        return loginUser;
    }

    /**
     * Get current user cache information by token, throws exception if not logged in
     *
     * @param token Authentication token from frontend
     * @return User cache information
     */
    public LoginUser getLoginUser(String token)
    {
        return tokenService.getLoginUser(token);
    }

    /**
     * Validate current user's validity period, automatically refreshes cache if less than 120 minutes remaining
     *
     * @param loginUser Current user information
     */
    public void verifyLoginUserExpire(LoginUser loginUser)
    {
        tokenService.verifyToken(loginUser);
    }

    /**
     * Verify if user has a specific permission
     *
     * @param permission Permission string
     * @return Whether user has the permission
     */
    public boolean hasPermi(String permission)
    {
        return hasPermi(getPermiList(), permission);
    }

    /**
     * Verify if user has a specific permission, throws NotPermissionException if validation fails
     *
     * @param permission Permission string
     */
    public void checkPermi(String permission)
    {
        if (!hasPermi(getPermiList(), permission))
        {
            throw new NotPermissionException(permission);
        }
    }

    /**
     * Authorize based on annotation (@RequiresPermissions), throws NotPermissionException if validation fails
     *
     * @param requiresPermissions Annotation object
     */
    public void checkPermi(RequiresPermissions requiresPermissions)
    {
        SecurityContextHolder.setPermission(StringUtils.join(requiresPermissions.value(), ","));
        if (requiresPermissions.logical() == Logical.AND)
        {
            checkPermiAnd(requiresPermissions.value());
        }
        else
        {
            checkPermiOr(requiresPermissions.value());
        }
    }

    /**
     * Verify if user has all specified permissions (AND logic)
     *
     * @param permissions Permission list
     */
    public void checkPermiAnd(String... permissions)
    {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions)
        {
            if (!hasPermi(permissionList, permission))
            {
                throw new NotPermissionException(permission);
            }
        }
    }

    /**
     * Verify if user has any of the specified permissions (OR logic)
     *
     * @param permissions Permission code array
     */
    public void checkPermiOr(String... permissions)
    {
        Set<String> permissionList = getPermiList();
        for (String permission : permissions)
        {
            if (hasPermi(permissionList, permission))
            {
                return;
            }
        }
        if (permissions.length > 0)
        {
            throw new NotPermissionException(permissions);
        }
    }

    /**
     * Check if user has a specific role
     *
     * @param role Role identifier
     * @return Whether user has the role
     */
    public boolean hasRole(String role)
    {
        return hasRole(getRoleList(), role);
    }

    /**
     * Check if user has a specific role, throws NotRoleException if validation fails
     *
     * @param role Role identifier
     */
    public void checkRole(String role)
    {
        if (!hasRole(role))
        {
            throw new NotRoleException(role);
        }
    }

    /**
     * Authorize based on annotation (@RequiresRoles)
     *
     * @param requiresRoles Annotation object
     */
    public void checkRole(RequiresRoles requiresRoles)
    {
        if (requiresRoles.logical() == Logical.AND)
        {
            checkRoleAnd(requiresRoles.value());
        }
        else
        {
            checkRoleOr(requiresRoles.value());
        }
    }

    /**
     * Verify if user has all specified roles (AND logic)
     *
     * @param roles Role identifier array
     */
    public void checkRoleAnd(String... roles)
    {
        Set<String> roleList = getRoleList();
        for (String role : roles)
        {
            if (!hasRole(roleList, role))
            {
                throw new NotRoleException(role);
            }
        }
    }

    /**
     * Verify if user has any of the specified roles (OR logic)
     *
     * @param roles Role identifier array
     */
    public void checkRoleOr(String... roles)
    {
        Set<String> roleList = getRoleList();
        for (String role : roles)
        {
            if (hasRole(roleList, role))
            {
                return;
            }
        }
        if (roles.length > 0)
        {
            throw new NotRoleException(roles);
        }
    }

    /**
     * Authorize based on annotation (@RequiresLogin)
     *
     * @param at Annotation object
     */
    public void checkByAnnotation(RequiresLogin at)
    {
        this.checkLogin();
    }

    /**
     * Authorize based on annotation (@RequiresRoles)
     *
     * @param at Annotation object
     */
    public void checkByAnnotation(RequiresRoles at)
    {
        String[] roleArray = at.value();
        if (at.logical() == Logical.AND)
        {
            this.checkRoleAnd(roleArray);
        }
        else
        {
            this.checkRoleOr(roleArray);
        }
    }

    /**
     * Authorize based on annotation (@RequiresPermissions)
     *
     * @param at Annotation object
     */
    public void checkByAnnotation(RequiresPermissions at)
    {
        String[] permissionArray = at.value();
        if (at.logical() == Logical.AND)
        {
            this.checkPermiAnd(permissionArray);
        }
        else
        {
            this.checkPermiOr(permissionArray);
        }
    }

    /**
     * Get current account's role list
     *
     * @return Role list
     */
    public Set<String> getRoleList()
    {
        try
        {
            LoginUser loginUser = getLoginUser();
            return loginUser.getRoles();
        }
        catch (Exception e)
        {
            return new HashSet<>();
        }
    }

    /**
     * Get current account's permission list
     *
     * @return Permission list
     */
    public Set<String> getPermiList()
    {
        try
        {
            LoginUser loginUser = getLoginUser();
            return loginUser.getPermissions();
        }
        catch (Exception e)
        {
            return new HashSet<>();
        }
    }

    /**
     * Check if permission is contained
     *
     * @param authorities Permission list
     * @param permission Permission string
     * @return Whether user has the permission
     */
    public boolean hasPermi(Collection<String> authorities, String permission)
    {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * Check if role is contained
     *
     * @param roles Role list
     * @param role Role identifier
     * @return Whether user has the role permission
     */
    public boolean hasRole(Collection<String> roles, String role)
    {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}
