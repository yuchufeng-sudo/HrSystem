package com.ys.system.controller;

import com.ys.common.core.constant.CacheConstants;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.redis.service.RedisService;
import com.ys.common.security.annotation.InnerAuth;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.system.api.domain.SysLogininfor;
import com.ys.system.api.domain.SysSecuritySettings;
import com.ys.system.api.domain.SysUser;
import com.ys.system.service.ISysLogininforService;
import com.ys.system.service.ISysSecuritySettingsService;
import com.ys.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *  SystemAccessRecord
 *
 * @author ys
 */
@RestController
@RequestMapping("/logininfor")
public class SysLogininforController extends BaseController
{
    @Autowired
    private ISysLogininforService logininforService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysSecuritySettingsService securitySettingsService;

    @RequiresPermissions("system:logininfor:list")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor)
    {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @PostMapping("/count")
    public AjaxResult count(@RequestBody SysLogininfor logininfor)
    {
        SysUser sysUser = sysUserService.selectUserByUserName(logininfor.getUserName());
        String enterpriseId = sysUser.getEnterpriseId();
        SysSecuritySettings sysSecuritySettings = new SysSecuritySettings();
        sysSecuritySettings.setEnterpriseId(enterpriseId);
        SysSecuritySettings info = securitySettingsService.getInfo(enterpriseId);
        Boolean enable2fa = info.getEnable2fa();
        if (enable2fa){
            Integer count = logininforService.selectLogininforCount(logininfor);
            return AjaxResult.success(count);
        }else {
            return AjaxResult.success(1);
        }
    }

    @Log(title = "Login Log", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:logininfor:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysLogininfor logininfor)
    {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        util.exportExcel(response, list, " Login Log ");
    }

    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "Login Log", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds)
    {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @RequiresPermissions("system:logininfor:remove")
    @Log(title = "Login Log", businessType = BusinessType.DELETE)
    @DeleteMapping("/clean")
    public AjaxResult clean()
    {
        logininforService.cleanLogininfor();
        return success();
    }

    @RequiresPermissions("system:logininfor:unlock")
    @Log(title = "Account unlock", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public AjaxResult unlock(@PathVariable("userName") String userName)
    {
        redisService.deleteObject(CacheConstants.CIPHER_ERR_CNT_KEY + userName);
        return success();
    }

    @InnerAuth
    @PostMapping
    public AjaxResult add(@RequestBody SysLogininfor logininfor)
    {
        return toAjax(logininforService.insertLogininfor(logininfor));
    }
}
