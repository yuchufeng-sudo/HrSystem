package com.ys.system.service;

import com.ys.system.api.domain.SysUser;

import java.util.Set;

/**
 * PERMISSION INFORMATION Service Layer
 *
 * @author ruoyi
 */
public interface ISysPermissionService
{
    /**
     * OBTAIN ROLE Data Permission
     *
     * @param userId USER Id
     * @return ROLEPermission INFORMATION
     */
    public Set<String> getRolePermission(SysUser user);

    /**
     * OBTAIN MENU Data Permission
     *
     * @param userId USER Id
     * @return  MENU Permission INFORMATION
     */
    public Set<String> getMenuPermission(SysUser user);
}
