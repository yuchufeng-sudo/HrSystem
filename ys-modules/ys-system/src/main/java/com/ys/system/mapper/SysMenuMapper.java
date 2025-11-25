package com.ys.system.mapper;

import com.ys.system.domain.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Menu table data layer
 *
 * @author ys
 */
public interface SysMenuMapper
{
    /**
     * Query system menu list
     *
     * @param menu Menu information
     * @return Menu list
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * Query all permissions according to user
     *
     * @return Permission list
     */
    public List<String> selectMenuPerms();

    /**
     * Query system menu list according to user
     *
     * @param menu Menu information
     * @return Menu list
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * Query permissions according to role ID
     *
     * @param roleId Role ID
     * @return Permission list
     */
    public List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * Query permissions according to user ID
     *
     * @param userId User ID
     * @return Permission list
     */
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * Query menu tree according to user ID
     *
     * @return Menu list
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * Query menu tree according to user ID
     *
     * @param userId User ID
     * @return Menu list
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * Query menu tree information according to role ID
     *
     * @param roleId Role ID
     * @param menuCheckStrictly Whether menu tree selection items are associated for display
     * @return Selected menu list
     */
    public List<Long> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

    /**
     * Query information according to menu ID
     *
     * @param menuId Menu ID
     * @return Menu information
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * Check if the menu has child nodes
     *
     * @param menuId Menu ID
     * @return Result
     */
    public int hasChildByMenuId(Long menuId);

    /**
     * Add menu information
     *
     * @param menu Menu information
     * @return Result
     */
    public int insertMenu(SysMenu menu);

    /**
     * Update menu information
     *
     * @param menu Menu information
     * @return Result
     */
    public int updateMenu(SysMenu menu);

    /**
     * Delete menu management information
     *
     * @param menuId Menu ID
     * @return Result
     */
    public int deleteMenuById(Long menuId);

    /**
     * Verify whether the menu name is unique
     *
     * @param menuName Menu name
     * @param parentId Parent menu ID
     * @return Result
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}
