package com.ys.common.security.auth;

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
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Token Permission
 *
 * @author ruoyi
 */
public class AuthLogic
{
    /**  all Permission */
    private static final String ALL_PERMISSION = "*:*:*";


    private static final String SUPER_ADMIN = "admin";

    public TokenService tokenService = SpringUtils.getBean(TokenService.class);

    /**
     * Logout
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
     * Logout
     */
    public void logoutByToken(String token)
    {
        tokenService.delLoginUser(token);
    }

    /**
     *
     */
    public void checkLogin()
    {
        getLoginUser();
    }

    /**
     *
     *
     * @return
     */
    public LoginUser getLoginUser()
    {
        String token = SecurityUtils.getToken();
        if (token == null)
        {
            throw new NotLoginException("No token provided");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null)
        {
            throw new NotLoginException("Invalid token");
        }
        return loginUser;
    }

    /**
     *
     *
     * @param token
     * @return
     */
    public LoginUser getLoginUser(String token)
    {
        return tokenService.getLoginUser(token);
    }

    /**
     *
     *
     * @param loginUser
     */
    public void verifyLoginUserExpire(LoginUser loginUser)
    {
        tokenService.verifyToken(loginUser);
    }

    /**
     *
     *
     * @param permission Permission Character string
     * @return
     */
    public boolean hasPermi(String permission)
    {
        return hasPermi(getPermiList(), permission);
    }

    /**
     *
     *
     * @param permission Permission Character string
     * @return
     */
    public void checkPermi(String permission)
    {
        if (!hasPermi(getPermiList(), permission))
        {
            throw new NotPermissionException(permission);
        }
    }

    /**
     *
     *
     * @param requiresPermissions
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
     *
     *
     * @param permissions Permission  LIST
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
     *
     *
     * @param permissions
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
     *
     *
     * @param role
     * @return
     */
    public boolean hasRole(String role)
    {
        return hasRole(getRoleList(), role);
    }

    /**
     *
     *
     * @param role
     */
    public void checkRole(String role)
    {
        if (!hasRole(role))
        {
            throw new NotRoleException(role);
        }
    }

    /**
     *
     *
     * @param requiresRoles
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
     *
     *
     * @param roles
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
     *
     *
     * @param roles
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
     *
     *
     * @param at
     */
    public void checkByAnnotation(RequiresLogin at)
    {
        this.checkLogin();
    }

    /**
     *
     *
     * @param at
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
     *
     *
     * @param at
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
     *
     *
     * @return
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
     *
     *
     * @return
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
     *
     *
     * @param authorities Permission  LIST
     * @param permission Permission Character string
     * @return
     */
    public boolean hasPermi(Collection<String> authorities, String permission)
    {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     *
     *
     * @param roles ROLE  LIST
     * @param role ROLE
     * @return
     */
    public boolean hasRole(Collection<String> roles, String role)
    {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}
