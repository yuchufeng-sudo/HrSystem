package com.ys.system.service;

import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * role Business Layer
 *
 * @author ys
 */
public interface ISysRoleService
{
    /**
     * Query role data by conditions with pagination.
     *
     * @param role role Information
     * @return role Data Set Information
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * According to User IDQUERY role  list
     *
     * @param userId User ID
     * @return role  list
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * According to User ID Query role Permission
     *
     * @param userId User ID
     * @return Permission  list
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * Query all role
     *
     * @return role  list
     */
    public List<SysRole> selectRoleAll();

    /**
     * According to User ID Obtain role SELECT list
     *
     * @param userId User ID
     * @return Select role ID  list
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * By role ID Query role
     *
     * @param roleId role ID
     * @return role Object Information
     */
    public SysRole selectRoleById(Long roleId);

    /**
     *  Verify whether the role Name is unique.
     *
     * @param role ROLE Information
     * @return Result
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     *  Verify whether the ROLE Permission is unique.
     *
     * @param role ROLE Information
     * @return Result
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     *  Verify whether ROLE is allowed to perform Operation.
     *
     * @param role ROLE Information
     */
    public void checkRoleAllowed(SysRole role);

    /**
     *  Verify whether the ROLE has data Permissions.
     *
     * @param roleIds ROLEid
     */
    public void checkRoleDataScope(Long... roleIds);

    /**
     * Query the usage quantity of ROLE by ROLE ID.
     *
     * @param roleId Role Id
     * @return Result
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * Save ROLE Information
     *
     * @param role ROLE Information
     * @return Result
     */
    public int insertRole(SysRole role);

    /**
     * MODIFYsave ROLE Information
     *
     * @param role ROLE Information
     * @return Result
     */
    public int updateRole(SysRole role);

    /**
     * MODIFYROLEStatus
     *
     * @param role ROLE Information
     * @return Result
     */
    public int updateRoleStatus(SysRole role);

    /**
     * Update Data Permission Information
     *
     * @param role ROLE Information
     * @return Result
     */
    public int authDataScope(SysRole role);

    /**
     * By ROLEIDDELETEROLE
     *
     * @param roleId Role Id
     * @return Result
     */
    public int deleteRoleById(Long roleId);

    /**
     * Batch DELETEROLE Information
     *
     * @param roleIds ROLE ID to be DELETED
     * @return Result
     */
    public int deleteRoleByIds(Long[] roleIds);

    /**
     * Revoke Authorization for User ROLE
     *
     * @param userRole Association Information between User and ROLE
     * @return Result
     */
    public int deleteAuthUser(SysUserRole userRole);

    /**
     * Batch Revoke Authorization for User ROLE
     *
     * @param roleId Role Id
     * @param userIds Data ID of the User whose authorization needs to be revoked
     * @return Result
     */
    public int deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * Batch SELECT Authorized User ROLE
     *
     * @param roleId Role Id
     * @param userIds
     * @return Result
     */
    public int insertAuthUsers(Long roleId, Long[] userIds);
}
