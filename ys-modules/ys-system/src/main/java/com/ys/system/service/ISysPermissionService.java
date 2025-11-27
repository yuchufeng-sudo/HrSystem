package com.ys.system.service;

import com.ys.system.api.domain.SysUser;

import java.util.Set;

/**
 * PERMISSION Information Service Layer
 *
 * @author ys
 */
public interface ISysPermissionService
{
    /**
     * Obtain role Data Permission
     *
     * @param userId User Id
     * @return role Permission Information
     */
    public Set<String> getRolePermission(SysUser user);

    /**
     * Obtain menu Data Permission
     *
     * @param userId User Id
     * @return menu Permission Information
     */
    public Set<String> getMenuPermission(SysUser user);
}
