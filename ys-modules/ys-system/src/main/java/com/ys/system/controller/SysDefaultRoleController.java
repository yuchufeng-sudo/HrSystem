package com.ys.system.controller;

import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.domain.SysUserRole;
import com.ys.system.service.ISysRoleService;
import com.ys.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * ROLE INFORMATION
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/defaultRole")
public class SysDefaultRoleController extends BaseController
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysUserService userService;

    @RequiresPermissions("system:defaultRole:list")
    @GetMapping("/list")
    public TableDataInfo list(SysRole role)
    {
        startPage();
        role.setType("2");
        List<SysRole> list = roleService.selectRoleList(role);
        return getDataTable(list);
    }

    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:defaultRole:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRole role)
    {
        role.setType("2");
        List<SysRole> list = roleService.selectRoleList(role);
        ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
        util.exportExcel(response, list, "ROLE data");
    }

    /**
     * According to ROLE IDOBTAIN DETAILEDLY INFORMATION
     */
    @RequiresPermissions("system:defaultRole:query")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId)
    {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }

    /**
     * ADDROLE
     */
    @RequiresPermissions("system:defaultRole:add")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role)
    {
        role.setType("2");
        role.setCreateBy(SecurityUtils.getUsername());
        return toAjax(roleService.insertRole(role));
    }

    /**
     * MODIFYsave ROLE
     */
    @RequiresPermissions("system:defaultRole:edit")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRole(role));
    }

    /**
     * MODIFY save data Permission
     */
    @RequiresPermissions("system:defaultRole:edit")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }

    /**
     * Status modification
     */
    @RequiresPermissions("system:defaultRole:edit")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role)
    {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * DELETEROLE
     */
    @RequiresPermissions("system:defaultRole:remove")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds)
    {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * OBTAIN ROLE SELECT box LIST
     */
    @RequiresPermissions("system:defaultRole:query")
    @GetMapping("/optionselect")
    public AjaxResult optionselect()
    {
        return success(roleService.selectRoleAll());
    }
    /**
     * QUERY assigned USER ROLE LIST
     */
    @RequiresPermissions("system:defaultRole:list")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }

    /**
     * QUERY unassigned USER ROLE LIST
     */
    @RequiresPermissions("system:defaultRole:list")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user)
    {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }

    /**
     * Cancel authorized USER
     */
    @RequiresPermissions("system:defaultRole:edit")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole)
    {
        return toAjax(roleService.deleteAuthUser(userRole));
    }

    /**
     * Batch Cancel authorized USER
     */
    @RequiresPermissions("system:defaultRole:edit")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds)
    {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }

    /**
     * Batch SELECT USER authorization
     */
    @RequiresPermissions("system:defaultRole:edit")
    @Log(title = "ROLE MANAGEMENT  ", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds)
    {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }
}
