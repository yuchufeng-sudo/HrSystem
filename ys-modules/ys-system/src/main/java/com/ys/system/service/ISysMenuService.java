package com.ys.system.service;

import com.ys.system.domain.SysMenu;
import com.ys.system.domain.vo.RouterVo;
import com.ys.system.domain.vo.TreeSelect;

import java.util.List;
import java.util.Set;

/**
 *  menu Business Layer
 *
 * @author ys
 */
public interface ISysMenuService
{
    /**
     * According to User Query System menu   list
     *
     * @param userId User ID
     * @return menu   list
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * According to User Query System menu   list
     *
     * @param menu  menu  Information
     * @param userId User ID
     * @return menu   list
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * According to User IDQUERYPermission
     *
     * @param userId User ID
     * @return Permission  list
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * According to role ID Query Permission
     *
     * @param roleId role ID
     * @return Permission  list
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * According to the user ID, query the menu tree information.
     *
     * @param userId User ID
     * @return menu   list
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * Query the menu tree Information according to role ID.
     *
     * @param roleId role ID
     * @return Select the menu list.
     */
    public List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * The menu required for building front-end routes.
     *
     * @param menus  menu   list
     * @return Route list
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * Construct the tree structure required for the front end.
     *
     * @param menus  menu   list
     * @return Tree Structure list
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * Construct the DROP-DOWN TREE STRUCTURE required for the front end.
     *
     * @param menus  menu   list
     * @return DROP-DOWN TREE STRUCTURE   list
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * According to  menu IDQUERY Information
     *
     * @param menuId  menu ID
     * @return menu  Information
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * Whether there are child nodes of the menu.
     *
     * @param menuId  menu ID
     * @return Result: true for Existence, false for Non-Existence
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     *Query whether the role exists in the menu.
     *
     * @param menuId  menu ID
     * @return Result: true for Existence, false for Non-Existence
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * Save menu Information
     *
     * @param menu menu Information
     * @return Result
     */
    public int insertMenu(SysMenu menu);

    /**
     * MODIFYsave  menu  Information
     *
     * @param menu  menu  Information
     * @return Result
     */
    public int updateMenu(SysMenu menu);

    /**
     * Delete menu  management   Information
     *
     * @param menuId  menu ID
     * @return Result
     */
    public int deleteMenuById(Long menuId);

    /**
     *  Verify whether the menu Name is unique.
     *
     * @param menu  menu  Information
     * @return Result
     */
    public boolean checkMenuNameUnique(SysMenu menu);
}
