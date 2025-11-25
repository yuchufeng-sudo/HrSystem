package com.ys.system.mapper;

import com.ys.system.domain.SysRoleMenu;

import java.util.List;

/**
 * Role and menu association table data layer
 *
 * @author ys
 */
public interface SysRoleMenuMapper
{
    /**
     * Query the usage quantity of menu.
     *
     * @param menuId  menu ID
     * @return Result
     */
    public int checkMenuExistRole(Long menuId);

    /**
     *
     *
     * @param roleId role ID
     * @return Result
     */
    public int deleteRoleMenuByRoleId(Long roleId);

    /**
     * Batch Delete role menu Associated Information
     *
     * @param The data IDs that need to be deleted for ids.
     * @return Result
     */
    public int deleteRoleMenu(Long[] ids);

    /**
     * Batch Add role menu  Information
     *
     * @param roleMenuList role menu   list
     * @return Result
     */
    public int batchRoleMenu(List<SysRoleMenu> roleMenuList);

    Long[] selectMenuByRoleId(Long roleId);
}
