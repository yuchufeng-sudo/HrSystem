package com.ys.system.mapper;

import com.ys.system.api.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Role table data layer
 *
 * @author ys
 */
public interface SysRoleMapper
{
    /**
     * Query role data by conditions with pagination.
     *
     * @param role role Information
     * @return role Data Set Information
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * According to User ID Query role
     *
     * @param userId User ID
     * @return role  list
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * Query all role
     *
     * @return role  list
     */
    public List<SysRole> selectRoleAll();

    /**
     * According to User ID OBTAIN role SELECT box list
     *
     * @param userId User ID
     * @return Select roleID  list
     */
    public List<Long> selectRoleListByUserId(Long userId);

    /**
     * By role ID Query role
     *
     * @param roleId role ID
     * @return ROL  Object Information
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * According to User ID Query role
     *
     * @param userName User Name
     * @return role  list
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     *  Verify whether the role Name is unique.
     *
     * @param roleName         role Name
     * @param enterpriseId     Enterprise Number
     * @return role Information
     */
    public SysRole checkRoleNameUnique(@Param("roleName") String roleName, @Param("enterpriseId") String enterpriseId);

    /**
     *  Verify whether the role Permission is unique.
     *
     * @param roleKey          role Permission
     * @param enterpriseId     Enterprise Number
     * @return role Information
     */
    public SysRole checkRoleKeyUnique(@Param("roleKey") String roleKey, @Param("enterpriseId") String enterpriseId);

    /**
     * MODIFYrole Information
     *
     * @param role role Information
     * @return Result
     */
    public int updateRole(SysRole role);

    /**
     * Add role Information
     *
     * @param role ROLE Information
     * @return Result
     */
    public int insertRole(SysRole role);

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
}
