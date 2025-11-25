package com.ys.system.service.impl;

import com.ys.common.core.constant.UserConstants;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.SpringUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.datascope.annotation.DataScope;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.domain.SysUserRole;
import com.ys.system.domain.SysRoleMenu;
import com.ys.system.mapper.SysRoleMapper;
import com.ys.system.mapper.SysRoleMenuMapper;
import com.ys.system.mapper.SysUserRoleMapper;
import com.ys.system.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * role Business Layer Processing
 *
 * @author ys
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    /**
     * Query role data by conditions with pagination.
     *
     * @param role role Information
     * @return role Data Set Information
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * According to User ID Query role
     *
     * @param userId User ID
     * @return role  list
     */
    @Override
    public List<SysRole> selectRolesByUserId(Long userId)
    {
        List<SysRole> userRoles = roleMapper.selectRolePermissionByUserId(userId);
        List<SysRole> roles = selectRoleAll();
        for (SysRole role : roles)
        {
            for (SysRole userRole : userRoles)
            {
                if (role.getRoleId().longValue() == userRole.getRoleId().longValue())
                {
                    role.setFlag(true);
                    break;
                }
            }
        }
        return roles;
    }

    /**
     * According to User IDQUERYPermission
     *
     * @param userId User ID
     * @return Permission  list
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * Query all role
     *
     * @return role  list
     */
    @Override
    public List<SysRole> selectRoleAll()
    {
        SysRole role = new SysRole();
        role.setType("1");
        return SpringUtils.getAopProxy(this).selectRoleList(role);
    }

    /**
     * According to User IDOBTAIN role SELECT box list
     *
     * @param userId User ID
     * @return Select role ID  list
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * By role ID Query role
     *
     * @param roleId role ID
     * @return role Object Information
     */
    @Override
    public SysRole selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     *  Verify whether the role Name is unique.
     *
     * @param role role Information
     * @return Result
     */
    @Override
    public boolean checkRoleNameUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName(),SecurityUtils.getUserEnterpriseId());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     *  Verify whether the role Permission is unique.
     *
     * @param role role Information
     * @return Result
     */
    @Override
    public boolean checkRoleKeyUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId()) ? -1L : role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey(),SecurityUtils.getUserEnterpriseId());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     *  Verify whether role is allowed to perform OPERATION.
     *
     * @param role ROLE Information
     */
    @Override
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {

        }
    }

    /**
     *  Verify whether the ROLE has data Permissions.
     *
     * @param roleIds ROLEid
     */
    @Override
    public void checkRoleDataScope(Long... roleIds)
    {
        if (!SysUser.isAdmin(SecurityUtils.getUserId()))
        {
            for (Long roleId : roleIds)
            {
                SysRole role = new SysRole();
                role.setRoleId(roleId);
                List<SysRole> roles = SpringUtils.getAopProxy(this).selectRoleList(role);
                if (StringUtils.isEmpty(roles))
                {
                    throw new ServiceException("No permission to access role data!");
                }
            }
        }
    }

    /**
     * Query the usage quantity of ROLE by ROLE ID.
     *
     * @param roleId Role Id
     * @return Result
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * Save ROLE Information
     *
     * @param role ROLE Information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role)
    {
        // ADDROLE Information
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * MODIFYsave ROLE Information
     *
     * @param role ROLE Information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role)
    {
        // MODIFYROLE Information
        roleMapper.updateRole(role);
        // Delete the association between ROLE and menu.
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * MODIFYROLEStatus
     *
     * @param role ROLE Information
     * @return Result
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * Update Data Permission Information
     *
     * @param role ROLE Information
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(SysRole role)
    {
        // MODIFYROLE Information
        roleMapper.updateRole(role);
        return roleMapper.updateRole(role);
    }

    /**
     * ADDROLE menu  Information
     *
     * @param role ROLEObject
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // Add the Association between User and ROLE management
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId : role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * By ROLEIDDELETEROLE
     *
     * @param roleId Role Id
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Long roleId)
    {
        // Delete the association between ROLE and menu.
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * Batch DELETEROLE Information
     *
     * @param roleIds ROLE ID to be DELETED
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleByIds(Long[] roleIds)
    {
        for (Long roleId : roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            checkRoleDataScope(roleId);
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0)
            {
                throw new ServiceException(String.format("%1$s has been assigned and cannot be deleted", role.getRoleName()));
            }
        }
        // Delete the association between ROLE and menu.
        roleMenuMapper.deleteRoleMenu(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * Revoke Authorization for User ROLE
     *
     * @param userRole Association Information between User and ROLE
     * @return Result
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * Batch Revoke Authorization for User ROLE
     *
     * @param roleId Role Id
     * @param userIds Data ID of the User whose authorization needs to be revoked
     * @return Result
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds)
    {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * Batch SELECT Authorized User ROLE
     *
     * @param roleId Role Id
     * @param userIds Data ID of the User to be authorized
     * @return Result
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds)
    {
        // Add the Association between User and ROLE management
        List<SysUserRole> list = new ArrayList<SysUserRole>();
        for (Long userId : userIds)
        {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }
        return userRoleMapper.batchUserRole(list);
    }
}
