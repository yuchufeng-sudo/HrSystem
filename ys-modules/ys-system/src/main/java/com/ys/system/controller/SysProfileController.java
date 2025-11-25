package com.ys.system.controller;

import com.ys.common.core.domain.R;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.file.FileTypeUtils;
import com.ys.common.core.utils.file.MimeTypeUtils;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.service.TokenService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.RemoteFileService;
import com.ys.system.api.domain.SysFile;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.model.LoginUser;
import com.ys.system.api.domain.SysEnterprise;
import com.ys.system.service.ISysEnterpriseService;
import com.ys.system.service.ISysSecuritySettingsService;
import com.ys.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 *
 *
 * @author ys
 */
@RestController
@RequestMapping("/user/profile")
public class SysProfileController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysSecuritySettingsService securitySettingsService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteFileService remoteFileService;

    @Resource
    private ISysEnterpriseService sysEnterpriseService;

    /**
     *
     */
    @GetMapping
    public AjaxResult profile()
    {
        String username = SecurityUtils.getUsername();
        SysUser user = userService.selectUserByUserName(username);
        return AjaxResult.success(user);
    }

    @GetMapping("userType")
    public AjaxResult getUserType()
    {
        return AjaxResult.success().put("userType",SecurityUtils.getUserType());
    }

    @GetMapping("enterprise")
    public AjaxResult info(){
        return AjaxResult.success(sysEnterpriseService.selectSysEnterpriseById(SecurityUtils.getUserEnterpriseId()));
    }

    @PutMapping("enterprise")
    public AjaxResult update(SysEnterprise sysEnterprise){
        sysEnterprise.setId(SecurityUtils.getUserEnterpriseId());
        return toAjax(sysEnterpriseService.updateSysEnterprise(sysEnterprise));
    }

    /**
     * MODIFYUSER
     */
    @Log(title = "Personal Information", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult updateProfile(@RequestBody SysUser user)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser currentUser = loginUser.getSysUser();
        currentUser.setNickName(user.getNickName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhonenumber(user.getPhonenumber());
        currentUser.setSex(user.getSex());
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(currentUser))
        {
            return error("MODIFYUSER '" + loginUser.getUsername() + "'Failure，Mobile Phone Number Already Exists");
        }
        if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(currentUser))
        {
            return error("MODIFYUSER '" + loginUser.getUsername() + "'Failure，emailAccount Already Exists");
        }
        if (userService.updateUserProfile(currentUser))
        {
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("There is an exception in modifying personal Information. Please contact the management staff.");
    }

    /**
     * RESETPassword
     */
    @Log(title = "Personal Information", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public AjaxResult updatePwd(String oldPassword, String newPassword)
    {

        String username = SecurityUtils.getUsername();
        SysUser user = userService.selectUserByUserName(username);
        String password = user.getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password))
        {
            return error("Update Password Failure, the old password is incorrect.");
        }
        if (SecurityUtils.matchesPassword(newPassword, password))
        {
            return error("The new password cannot be the same as the old one");
        }
        securitySettingsService.validatePassword(newPassword);
        newPassword = SecurityUtils.encryptPassword(newPassword);
        if (userService.resetUserPwd(username, newPassword) > 0)
        {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            loginUser.getSysUser().setPassword(newPassword);
            tokenService.setLoginUser(loginUser);
            return success();
        }
        return error("There is an exception in modifying the password. Please contact the management staff.");
    }

    /**
     *
     */
    @Log(title = "User avatar", businessType = BusinessType.UPDATE)
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file)
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = SecurityUtils.getLoginUser();
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
            if (userService.updateUserAvatar(loginUser.getUsername(), url))
            {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", url);

                loginUser.getSysUser().setAvatar(url);
                tokenService.setLoginUser(loginUser);
                return ajax;
            }
        }
        return error("Upload Picture exception. Please contact the management staff.");
    }
}
