package com.ys.system.controller;

import com.ys.common.core.constant.UserConstants;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.domain.SysMenu;
import com.ys.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  MENU  INFORMATION
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    /**
     * OBTAIN  MENU   LIST
     */
    @RequiresPermissions("system:menu:list")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return success(menus);
    }

    /**
     * According to  MENU  IDOBTAIN DETAILEDLY INFORMATION
     */
    @RequiresPermissions("system:menu:query")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return success(menuService.selectMenuById(menuId));
    }

    /**
     *
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     *
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }
    @GetMapping(value = "/allRoleMenuTreeselect/{roleId}")
    public AjaxResult allRoleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuList(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * ADD MENU
     */
    @RequiresPermissions("system:menu:add")
    @Log(title = " MENU  MANAGEMENT  ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
//        if (!menuService.checkMenuNameUnique(menu))
//        {
//            return error("ADD MENU '" + menu.getMenuName() + "'Failure， MENU Name Already Exists");
//        }
        if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error("ADD MENU '" + menu.getMenuName() + "' failed, Address must start with http(s)://");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * MODIFY MENU
     */
    @RequiresPermissions("system:menu:edit")
    @Log(title = " MENU  MANAGEMENT  ", businessType = BusinessType.UPDATE)
    @PutMapping
 public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
//        if (!menuService.checkMenuNameUnique(menu))
//        {
//            return error("MODIFY MENU '" + menu.getMenuName() + "'Failure， MENU Name Already Exists");
//        }
        if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath()))
        {
            return error("MODIFY MENU '" + menu.getMenuName() + "' failed, Address must start with http(s)://");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return error("MODIFY MENU '" + menu.getMenuName() + "' failed, Parent MENU cannot select itself");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * DELETE MENU
     */
    @RequiresPermissions("system:menu:remove")
    @Log(title = " MENU  MANAGEMENT  ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return warn("There are sub-MENUs, so DELETE is not allowed");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return warn("MENU has been assigned, so DELETE is not allowed");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }

    /**
     *
     *
     * @return
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return success(menuService.buildMenus(menus));
    }
}
