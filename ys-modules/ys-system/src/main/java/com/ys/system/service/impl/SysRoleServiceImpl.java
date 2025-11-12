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
 * ROLE Business Layer Processing
 *
 * @author ruoyi
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
     * Query ROLE data by conditions with pagination.
     *
     * @param role ROLE INFORMATION
     * @return ROLE Data Set INFORMATION
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * According to USER IDQUERYROLE
     *
     * @param userId USER ID
     * @return ROLE  LIST
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
     * According to USER IDQUERYPermission
     *
     * @param userId USER ID
     * @return Permission  LIST
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
     * QUERY all ROLE
     *
     * @return ROLE  LIST
     */
    @Override
    public List<SysRole> selectRoleAll()
    {
        SysRole role = new SysRole();
        role.setType("1");
        return SpringUtils.getAopProxy(this).selectRoleList(role);
    }

    /**
     * According to USER IDOBTAIN ROLE SELECT box LIST
     *
     * @param userId USER ID
     * @return Select ROLEID  LIST
     */
    @Override
    public List<Long> selectRoleListByUserId(Long userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * By ROLE ID QUERY ROLE
     *
     * @param roleId ROLEID
     * @return ROLEObject INFORMATION
     */
    @Override
    public SysRole selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     *  Verify whether the ROLE Name is unique.
     *
     * @param role ROLE INFORMATION
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
     *  Verify whether the ROLE Permission is unique.
     *
     * @param role ROLE INFORMATION
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
     *  Verify whether ROLE is allowed to perform OPERATION.
     *
     * @param role ROLE INFORMATION
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
     * @param roleId ROLEID
     * @return Result
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * ADDsave ROLE INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(SysRole role)
    {
        // ADDROLE INFORMATION
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * MODIFYsave ROLE INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRole(SysRole role)
    {
        // MODIFYROLE INFORMATION
        roleMapper.updateRole(role);
        // Delete the association between ROLE and MENU.
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * MODIFYROLEStatus
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateRole(role);
    }

    /**
     * Modify Data Permission INFORMATION
     *
     * @param role ROLE INFORMATION
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int authDataScope(SysRole role)
    {
        // MODIFYROLE INFORMATION
        roleMapper.updateRole(role);
        return roleMapper.updateRole(role);
    }

    /**
     * ADDROLE MENU  INFORMATION
     *
     * @param role ROLEObject
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // Add the Association between USER and ROLE MANAGEMENT
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
     * @param roleId ROLEID
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteRoleById(Long roleId)
    {
        // Delete the association between ROLE and MENU.
        roleMenuMapper.deleteRoleMenuByRoleId(roleId);
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * Batch DELETEROLE INFORMATION
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
        // Delete the association between ROLE and MENU.
        roleMenuMapper.deleteRoleMenu(roleIds);
        return roleMapper.deleteRoleByIds(roleIds);
    }

    /**
     * Revoke Authorization for USER ROLE
     *
     * @param userRole Association INFORMATION between USER and ROLE
     * @return Result
     */
    @Override
    public int deleteAuthUser(SysUserRole userRole)
    {
        return userRoleMapper.deleteUserRoleInfo(userRole);
    }

    /**
     * Batch Revoke Authorization for USER ROLE
     *
     * @param roleId ROLEID
     * @param userIds Data ID of the USER whose authorization needs to be revoked
     * @return Result
     */
    @Override
    public int deleteAuthUsers(Long roleId, Long[] userIds)
    {
        return userRoleMapper.deleteUserRoleInfos(roleId, userIds);
    }

    /**
     * Batch SELECT Authorized USER ROLE
     *
     * @param roleId ROLEID
     * @param userIds Data ID of the USER to be authorized
     * @return Result
     */
    @Override
    public int insertAuthUsers(Long roleId, Long[] userIds)
    {
        // Add the Association between USER and ROLE MANAGEMENT
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
