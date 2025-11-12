package com.ys.system.service;

import com.ys.system.domain.SysMenu;
import com.ys.system.domain.vo.RouterVo;
import com.ys.system.domain.vo.TreeSelect;

import java.util.List;
import java.util.Set;

/**
 *  MENU Business Layer
 *
 * @author ruoyi
 */
public interface ISysMenuService
{
    /**
     * According to USER QUERY System MENU   LIST
     *
     * @param userId USER ID
     * @return  MENU   LIST
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * According to USER QUERY System MENU   LIST
     *
     * @param menu  MENU  INFORMATION
     * @param userId USER ID
     * @return  MENU   LIST
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * According to USER IDQUERYPermission
     *
     * @param userId USER ID
     * @return Permission  LIST
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * According to ROLEIDQUERYPermission
     *
     * @param roleId ROLEID
     * @return Permission  LIST
     */
    public Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * According to the user ID, query the menu tree information.
     *
     * @param userId USER ID
     * @return  MENU   LIST
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * Query the MENU tree INFORMATION according to ROLEID.
     *
     * @param roleId ROLEID
     * @return Select the MENU LIST.
     */
    public List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * The MENU required for building front-end routes.
     *
     * @param menus  MENU   LIST
     * @return Route LIST
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * Construct the tree structure required for the front end.
     *
     * @param menus  MENU   LIST
     * @return Tree Structure LIST
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * Construct the DROP-DOWN TREE STRUCTURE required for the front end.
     *
     * @param menus  MENU   LIST
     * @return  DROP-DOWN TREE STRUCTURE   LIST
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * According to  MENU IDQUERY INFORMATION
     *
     * @param menuId  MENU ID
     * @return  MENU  INFORMATION
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * Whether there are child nodes of the MENU.
     *
     * @param menuId  MENU ID
     * @return Result: true for Existence, false for Non-Existence
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     *Query whether the role exists in the menu.
     *
     * @param menuId  MENU ID
     * @return Result: true for Existence, false for Non-Existence
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * ADDsave  MENU  INFORMATION
     *
     * @param menu  MENU  INFORMATION
     * @return Result
     */
    public int insertMenu(SysMenu menu);

    /**
     * MODIFYsave  MENU  INFORMATION
     *
     * @param menu  MENU  INFORMATION
     * @return Result
     */
    public int updateMenu(SysMenu menu);

    /**
     * DELETE MENU  MANAGEMENT   INFORMATION
     *
     * @param menuId  MENU ID
     * @return Result
     */
    public int deleteMenuById(Long menuId);

    /**
     *  Verify whether the MENU Name is unique.
     *
     * @param menu  MENU  INFORMATION
     * @return Result
     */
    public boolean checkMenuNameUnique(SysMenu menu);
}
