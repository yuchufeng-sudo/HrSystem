package com.ys.system.mapper;

import com.ys.system.api.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Role table data layer
 *
 * @author ruoyi
 */
public interface SysRoleMapper
{
    /**
     * Query ROLE data by conditions with pagination.
     *
     * @param role ROLE INFORMATION
     * @return ROLE Data Set INFORMATION
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * According to USER ID QUERY ROLE
     *
     * @param userId USER ID
     * @return ROLE  LIST
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * QUERY all ROLE
     *
     * @return ROLE  LIST
     */
    public List<SysRole> selectRoleAll();

    /**
     * According to USER ID OBTAIN ROLE SELECT box LIST
     *
     * @param userId USER ID
     * @return Select ROLEID  LIST
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * By ROLE ID QUERY ROLE
     *
     * @param roleId ROLE ID
     * @return ROLEObject INFORMATION
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * According to USER ID QUERY ROLE
     *
     * @param userName USER Name
     * @return ROLE  LIST
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     *  Verify whether the ROLE Name is unique.
     *
     * @param roleName         ROLEName
     * @param enterpriseId     Enterprise Number
     * @return ROLE INFORMATION
     */
    public SysRole checkRoleNameUnique(@Param("roleName") String roleName, @Param("enterpriseId") String enterpriseId);

    /**
     *  Verify whether the ROLE Permission is unique.
     *
     * @param roleKey          ROLEPermission
     * @param enterpriseId     Enterprise Number
     * @return ROLE INFORMATION
     */
    public SysRole checkRoleKeyUnique(@Param("roleKey") String roleKey, @Param("enterpriseId") String enterpriseId);

    /**
     * MODIFYROLE INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public int updateRole(SysRole role);

    /**
     * ADDROLE INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    public int insertRole(SysRole role);

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
}
