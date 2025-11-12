package com.ys.system.mapper;

import com.ys.system.domain.SysRoleMenu;

import java.util.List;

/**
 * Role and menu association table data layer
 *
 * @author ruoyi
 */
public interface SysRoleMenuMapper
{
    /**
     * QUERY the usage quantity of MENU.
     *
     * @param menuId  MENU ID
     * @return Result
     */
    public int checkMenuExistRole(Long menuId);

    /**
     *
     *
     * @param roleId ROLEID
     * @return Result
     */
    public int deleteRoleMenuByRoleId(Long roleId);

    /**
     * Batch DELETEROLE MENU Associated INFORMATION
     *
     * @param The data IDs that need to be deleted for ids.
     * @return Result
     */
    public int deleteRoleMenu(Long[] ids);

    /**
     * Batch ADDROLE MENU  INFORMATION
     *
     * @param roleMenuList ROLE MENU   LIST
     * @return Result
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);

    Long[] selectMenuByRoleId(Long roleId);
}
