package com.ys.system.mapper;

import com.ys.system.api.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Data layer for the association table between USER and ROLE
 *
 * @author ruoyi
 */
public interface SysUserRoleMapper
{
    /**
     * Delete the association between USER and ROLE by USER ID
     *
     * @param userId USER ID
     * @return Result
     */
    public int deleteUserRoleByUserId(Long userId);

    /**
     * Batch delete the association between USER and ROLE
     *
     * @param The data IDs that need to be deleted for ids.
     * @return Result
     */
    public int deleteUserRole(Long[] ids);

    /**
     * Query the usage quantity of ROLE by ROLE ID.
     *
     * @param roleId ROLEID
     * @return Result
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * Batch ADDUSER ROLE INFORMATION
     *
     * @param userRoleList USER ROLE  LIST
     * @return Result
     */
    public int batchUserRole(List<SysUserRole> userRoleList);

    /**
     * DELETEAssociation INFORMATION between USER and ROLE
     *
     * @param userRole Association INFORMATION between USER and ROLE
     * @return Result
     */
    public int deleteUserRoleInfo(SysUserRole userRole);

    /**
     * Batch Revoke Authorization for USER ROLE
     *
     * @param roleId ROLEID
     * @paramUser data IDs that need to be deleted for userIds.
     * @return Result
     */
    public int deleteUserRoleInfos(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);
}
