package com.ys.system.controller;

import com.ys.common.core.constant.CacheConstants;
import com.ys.common.core.domain.R;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.file.FileTypeUtils;
import com.ys.common.core.utils.file.MimeTypeUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.redis.service.RedisService;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.RemoteFileService;
import com.ys.system.api.domain.SysFile;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.domain.SysEnterprise;
import com.ys.system.service.ISysEnterpriseService;
import com.ys.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 *  Enterprise management Controller
 *
 * @author ys
 * @date 2025-05-15
 */
@RestController
@RequestMapping("/enterprise")
public class SysEnterpriseController extends BaseController
{
    @Autowired
    private ISysEnterpriseService sysEnterpriseService;

    @Autowired
    private RemoteFileService remoteFileService;

    @Resource
    private ISysUserService userService;

    /**
     * Query Enterprise management   list
     */
//    @RequiresPermissions("system:enterprise:list")
    @GetMapping("/list")
    public TableDataInfo list(SysEnterprise sysEnterprise)
    {
        startPage();
        if ("00".equals(SecurityUtils.getUserType())) {
            sysEnterprise.setOrderById(SecurityUtils.getUserEnterpriseId());
            sysEnterprise.setId(null);
        }else {
            sysEnterprise.setId(SecurityUtils.getUserEnterpriseId());
        }
        List<SysEnterprise> list = sysEnterpriseService.selectSysEnterpriseList(sysEnterprise);
        return getDataTable(list);
    }

    /**
     * Export Enterprise management   list
     */
    @RequiresPermissions("system:enterprise:export")
    @Log(title = "Enterprise management", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysEnterprise sysEnterprise)
    {
        if (!SecurityUtils.getUserId().equals(1L)) {
            sysEnterprise.setId(SecurityUtils.getUserEnterpriseId());
        }
        List<SysEnterprise> list = sysEnterpriseService.selectSysEnterpriseList(sysEnterprise);
        ExcelUtil<SysEnterprise> util = new ExcelUtil<SysEnterprise>(SysEnterprise.class);
        util.exportExcel(response, list, "Enterprise Management Data");
    }

    /**
     * Get Enterprise management Details
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return success(sysEnterpriseService.getById(id));
    }

    @GetMapping(value = "/join/{code}")
    public AjaxResult getJoinInfo(@PathVariable("code") String code) {
        return success(sysEnterpriseService.selectSysEnterpriseByInviteCode(code));
    }

    /**
     * Add Enterprise management
     */
    @RequiresPermissions("system:enterprise:add")
    @Log(title = "Enterprise management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysEnterprise sysEnterprise) {
        return toAjax(sysEnterpriseService.insertSysEnterprise(sysEnterprise));
    }

    /**
     * Update Enterprise management
     */
    @RequiresPermissions("system:enterprise:edit")
    @Log(title = "Enterprise management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysEnterprise sysEnterprise) {
        return toAjax(sysEnterpriseService.updateSysEnterprise(sysEnterprise));
    }

    @PutMapping("/setting")
    public AjaxResult editSetting(@RequestBody SysEnterprise sysEnterprise) {
        return toAjax(sysEnterpriseService.updateSysEnterpriseByFirst(sysEnterprise));
    }

    /**
     * Delete Enterprise management
     */
    @RequiresPermissions("system:enterprise:remove")
    @Log(title = "Enterprise management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable String id) {
        return toAjax(sysEnterpriseService.disablesEnterprise(id));
    }

    @PostMapping("resume/{id}")
    public AjaxResult resume(@PathVariable String id) {
        return toAjax(sysEnterpriseService.resumeEnterprise(id));
    }

    @Resource
    private RedisService redisService;

    @GetMapping("/getSelectEnterpriseId")
    public AjaxResult getSelectEnterpriseId() {
        String userEnterpriseId = SecurityUtils.getUserEnterpriseId();
        if (userEnterpriseId==null|| userEnterpriseId.isEmpty()){
            List<SysEnterprise> tbEnterprises = sysEnterpriseService.selectSysEnterpriseList(new SysEnterprise());
            if (!tbEnterprises.isEmpty()) {
                String enterpriseId = tbEnterprises.get(0).getId();
                redisService.setCacheObject(CacheConstants.LOGIN_SELECT_Enterprise_ID+SecurityUtils.getUserId(), enterpriseId);
                userEnterpriseId = enterpriseId;
            }
        }
        return AjaxResult.success("Operation SUCCESS", userEnterpriseId);
    }

    @PostMapping("setSelectEnterpriseId")
    public AjaxResult setSelectEnterpriseId(@RequestBody String enterpriseId){
        redisService.setCacheObject(CacheConstants.LOGIN_SELECT_Enterprise_ID+SecurityUtils.getUserId(),enterpriseId);
        return AjaxResult.success();
    }

    /**
     * Query enterprise information.
     * @return
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo(){
        SysEnterprise byId = sysEnterpriseService.getById(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(byId);
    }

    @GetMapping("/getCompany")
    public AjaxResult getCompany(){
        SysEnterprise byId = sysEnterpriseService.getById(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(byId);
    }

    @PostMapping("/update")
    public AjaxResult update(@RequestBody SysEnterprise sysEnterprise){
        sysEnterprise.setId(SecurityUtils.getUserEnterpriseId());
        return AjaxResult.success(sysEnterpriseService.updateSysEnterprise(sysEnterprise));
    }


    /**
     * Update Enterprise Logo
     */
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file)
    {
        if (!file.isEmpty())
        {
            String extension = FileTypeUtils.getExtension(file);
            if (!StringUtils.equalsAnyIgnoreCase(extension, MimeTypeUtils.IMAGE_EXTENSION))
            {
                return error("The file format is incorrect. Please upload it." + Arrays.toString(MimeTypeUtils.IMAGE_EXTENSION) + "Format");
            }
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData()))
            {
                return error("File Service exception. Please contact the management staff.");
            }
            String url = fileResult.getData().getUrl();
            SysEnterprise sysEnterprise = new SysEnterprise();
            sysEnterprise.setId(SecurityUtils.getUserEnterpriseId());
            sysEnterprise.setLogoUrl(url);
            if (sysEnterpriseService.updateSysEnterprise(sysEnterprise)!=0)
            {
                SysUser user = new SysUser();
                user.setEnterpriseId(SecurityUtils.getUserEnterpriseId());
                user.setUserType("01");
                List<SysUser> sysUsers = userService.selectUserList(user);
                if (!sysUsers.isEmpty()) {
                    SysUser sysUser = sysUsers.get(0);
                    SysUser sysUser1 = new SysUser();
                    sysUser1.setUserId(sysUser.getUserId());
                    sysUser1.setAvatar(url);
                    userService.updateUser(sysUser1);
                }
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", url);
                return ajax;
            }
        }
        return error("Upload Picture exception. Please contact the management staff.");
    }


    // Enterprise information
//    @RequiresPermissions("system:enterprise:list")
    @GetMapping("/listEnterprise")
    public TableDataInfo listEnterprise(SysEnterprise sysEnterprise)
    {
        startPage();
        if (!SecurityUtils.getUserId().equals(1L)) {
            sysEnterprise.setId(SecurityUtils.getUserEnterpriseId());
        }
        List<SysEnterprise> list = sysEnterpriseService.selectEnterpriseList(sysEnterprise);
        return getDataTable(list);
    }

    @GetMapping("adminUser")
    public AjaxResult adminUser(String enterpriseId){
        SysUser user = new SysUser();
        user.setEnterpriseId(enterpriseId);
        user.setUserType("01");
        List<SysUser> sysUsers = userService.selectUserList(user);
        if (!sysUsers.isEmpty()) {
            return AjaxResult.success(sysUsers.get(0));
        }else {
            return AjaxResult.success();
        }
    }
}
