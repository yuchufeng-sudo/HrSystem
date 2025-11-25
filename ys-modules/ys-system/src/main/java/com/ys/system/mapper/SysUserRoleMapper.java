package com.ys.system.mapper;

import com.ys.system.api.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Data layer for the association table between User and role
 *
 * @author ys
 */
public interface SysUserRoleMapper
{
    /**
     * Delete the association between User and role by User ID
     *
     * @param userId User ID
     * @return Result
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * Batch delete the association between User and role
     *
     * @param The data IDs that need to be deleted for ids.
     * @return Result
     */
    public int deleteUserRole(Long[] ids);

    /**
     * Query the usage quantity of role by role ID.
     *
     * @param roleId role ID
     * @return Result
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * Batch ADDUSER role Information
     *
     * @param userRoleList User role  list
     * @return Result
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * DELETEAssociation Information between User and role
     *
     * @param userRole Association Information between User and role
     * @return Result
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * Batch Revoke Authorization for User role
     *
     * @param roleId role ID
     * @paramUser data IDs that need to be deleted for userIds.
     * @return Result
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
