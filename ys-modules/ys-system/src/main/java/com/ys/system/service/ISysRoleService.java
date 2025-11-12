package com.ys.system.service;

import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * ROLE Business Layer
 *
 * @author ruoyi
 */
public interface ISysRoleService
{
    /**
     * Query ROLE data by conditions with pagination.
     *
     * @param role ROLE INFORMATION
     * @return ROLE Data Set INFORMATION
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * According to USER IDQUERYROLE  LIST
     *
     * @param userId USER ID
     * @return ROLE  LIST
     */
    public List<SysRole> selectRolesByUserId(Long userId);

    /**
     * According to USER IDQUERYROLEPermission
     *
     * @param userId USER ID
     * @return Permission  LIST
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * QUERY all ROLE
     *
     * @return ROLE  LIST
     */
    public List<SysRole> selectRoleAll();

    /**
     * According to USER IDOBTAIN ROLESELECT  LIST
     *
     * @param userId USER ID
     * @return Select ROLEID  LIST
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * By ROLE ID QUERY ROLE
     *
     * @param roleId ROLEID
     * @return ROLEObject INFORMATION
     */
    public SysRole selectRoleById(Long roleId);

    /**
     *  Verify whether the ROLE Name is unique.
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public boolean checkRoleNameUnique(SysRole role);

    /**
     *  Verify whether the ROLE Permission is unique.
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public boolean checkRoleKeyUnique(SysRole role);

    /**
     *  Verify whether ROLE is allowed to perform OPERATION.
     *
     * @param role ROLE INFORMATION
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
     * @param roleId ROLEID
     * @return Result
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * ADDsave ROLE INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public int insertRole(SysRole role);

    /**
     * MODIFYsave ROLE INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public int updateRole(SysRole role);

    /**
     * MODIFYROLEStatus
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public int updateRoleStatus(SysRole role);

    /**
     * Modify Data Permission INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public int authDataScope(SysRole role);

    /**
     * By ROLEIDDELETEROLE
     *
     * @param roleId ROLEID
     * @return Result
     */
    public int deleteRoleById(Long roleId);

    /**
     * Batch DELETEROLE INFORMATION
     *
     * @param roleIds ROLE ID to be DELETED
     * @return Result
     */
    public int deleteRoleByIds(Long[] roleIds);

    /**
     * Revoke Authorization for USER ROLE
     *
     * @param userRole Association INFORMATION between USER and ROLE
     * @return Result
     */
    public int deleteAuthUser(SysUserRole userRole);

    /**
     * Batch Revoke Authorization for USER ROLE
     *
     * @param roleId ROLEID
     * @param userIds Data ID of the USER whose authorization needs to be revoked
     * @return Result
     */
    public int deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * Batch SELECT Authorized USER ROLE
     *
     * @param roleId ROLEID
     * @param userIds
     * @return Result
     */
    public int insertAuthUsers(Long roleId, Long[] userIds);
}
