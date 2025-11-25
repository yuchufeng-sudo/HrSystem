package com.ys.system.controller;

import com.ys.common.core.domain.R;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.poi.ExcelUtil;
import com.ys.common.core.web.controller.BaseController;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.web.page.TableDataInfo;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.security.annotation.InnerAuth;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.service.TokenService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysGoogle;
import com.ys.system.api.domain.SysRole;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.model.LoginUser;
import com.ys.system.api.domain.SysEnterprise;
import com.ys.system.mapper.SysUserMapper;
import com.ys.system.service.*;
import com.ys.utils.constant.RedisConstants;
import com.ys.utils.core.redis.RedisCache;
import com.ys.utils.email.EmailUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * User Information
 *
 * @author ys
 */
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysEnterpriseService enterpriseService;

    @Autowired
    private ISysPermissionService permissionService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailUtils emailUtils;

    @Resource
    private RedisCache redisCache;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    /**
     * OBTAIN User   list
     */
    @RequiresPermissions("system:user:list")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        user.setUserType("00");
        List<SysUser> list = userService.selectUserList(user);
        return getDataTable(list);
    }

    @Log(title = "User  management", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:user:export")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUser user) {
        user.setUserType("00");
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.exportExcel(response, list, "User data");
    }

    @Log(title = "User  management", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = SecurityUtils.getUsername();
        String message = userService.importUser(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "User data");
    }

    /**
     * OBTAIN current User Information
     */
    @InnerAuth
    @GetMapping("/info/{username}")
    public R<LoginUser> info(@PathVariable("username") String username) {
        SysUser sysUser = userService.selectUserByUserName(username);
        if (StringUtils.isNull(sysUser)) {
            return R.fail("The account number or password is incorrect");
        }
        // role Set
        Set<String> roles = permissionService.getRolePermission(sysUser);
        // PermissionSet
        Set<String> permissions = permissionService.getMenuPermission(sysUser);
        LoginUser sysUserVo = new LoginUser();
        if (!"00".equals(sysUser.getUserType())) {
            String enterpriseId = sysUser.getEnterpriseId();
            SysEnterprise sysEnterprise = enterpriseService.selectSysEnterpriseById(enterpriseId);
            sysUserVo.setEnterpriseStatus(sysEnterprise.getStatus());
            sysUserVo.setEnterpriseName(sysEnterprise.getEnterpriseName());
            sysUserVo.setEnterpriseLogo(sysEnterprise.getLogoUrl());
        }
        sysUserVo.setSysUser(sysUser);
        sysUserVo.setRoles(roles);
        sysUserVo.setPermissions(permissions);
        return R.ok(sysUserVo);
    }

    /**
     * Register User Information
     */
    @InnerAuth
    @PostMapping("/register")
    public R<Boolean> register(@RequestBody SysUser sysUser) {
        String username = sysUser.getUserName();
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return R.fail("The registration function is not enabled in the current system!");
        }
        if (!userService.checkUserNameUnique(sysUser)) {
            return R.fail("save User '" + username + "'Failure，RegisterAccount Already Exists");
        }
        sysUser.setUserType("00");
        return R.ok(userService.registerUser(sysUser));
    }

    /**
     * RecordUSER Login IPAddressandLogin time
     */
    @InnerAuth
    @PutMapping("/recordlogin")
    public R<Boolean> recordlogin(@RequestBody SysUser sysUser) {
        return R.ok(userService.updateUserProfile(sysUser));
    }

    /**
     * Get User Information
     *
     * @return User Information
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getSysUser();
        // role Set
        Set<String> roles = permissionService.getRolePermission(user);
        // PermissionSet
        Set<String> permissions = permissionService.getMenuPermission(user);
        if (!loginUser.getPermissions().equals(permissions)) {
            loginUser.setPermissions(permissions);
            tokenService.refreshToken(loginUser);
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * According to User  IDOBTAIN Details
     */
    @RequiresPermissions("system:user:query")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        userService.checkUserDataScope(userId);
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        return ajax;
    }

    @GetMapping("/setting")
    public AjaxResult getInfoSetting() {
        AjaxResult ajax = AjaxResult.success();
        SysUser sysUser = userService.selectUserById(SecurityUtils.getUserId());
        ajax.put(AjaxResult.DATA_TAG, sysUser);
        ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * ADDUSER
     */
    @RequiresPermissions("system:user:add")
    @Log(title = "User  management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysUser user) {
        roleService.checkRoleDataScope(user.getRoleIds());
        if (!userService.checkUserNameUnique(user)) {
            return error("Add User '" + user.getUserName() + "'Failure，Login Account Already Exists");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("Add User '" + user.getUserName() + "'Failure，Mobile Phone Number Already Exists");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("Add User '" + user.getUserName() + "'Failure，emailAccount Already Exists");
        }
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

    /**
     * MODIFYUSER
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "User  management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        roleService.checkRoleDataScope(user.getRoleIds());
        if (!userService.checkUserNameUnique(user)) {
            return error("Update User '" + user.getUserName() + "'Failure，Login Account Already Exists");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return error("Update User '" + user.getUserName() + "'Failure，Mobile Phone Number Already Exists");
        } else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user)) {
            return error("Update User '" + user.getUserName() + "'Failure，emailAccount Already Exists");
        }
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUser(user));
    }

    @PutMapping("/setting")
    public AjaxResult editSetting(@Validated @RequestBody SysGoogle user) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(SecurityUtils.getUserId());
        sysUser.setGoogle(user.getGoogle());
        if (!userService.checkGoogleUnique(sysUser)) {
            return error("Update User '" + user.getGoogle() + "'Failure，Google Account Already Exists");
        }
        sysUser.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userMapper.updateUser(sysUser));
    }


    @PutMapping("/GoolgeAuth")
    public AjaxResult GoolgeAuth(@Validated @RequestBody SysGoogle user) {
        SysUser sysUser = new SysUser();
        SysUser user1 = userMapper.selectUserByUserName(user.getUsername());
        if(ObjectUtils.isNotEmpty(user1)){
            boolean matchesPassword = SecurityUtils.matchesPassword(user.getPassword(),user1.getPassword());
            if (!matchesPassword) {
                return error("Sorry , Your Password is Error");
            }
            else{
                sysUser.setUserId(user1.getUserId());
                sysUser.setGoogle(user.getEmail());
                if(!userService.checkGoogleUnique(sysUser)){
                    return error("MODIFYUSER '" + user.getGoogle() + "'Failure，Google Account Already Exists");
                }
            }
        }else{
            return error("Sorry , Your User Name is Error");
        }
        sysUser.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userMapper.updateUser(sysUser));
    }

    @PutMapping("/delGoolgeAuth")
    public AjaxResult delGoolgeAuth() {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(SecurityUtils.getUserId());
        sysUser.setGoogle("");
        return toAjax(userMapper.updateUser(sysUser));
    }



    /**
     * DELETEUSER
     */
    @RequiresPermissions("system:user:remove")
    @Log(title = "User  management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {
        if (ArrayUtils.contains(userIds, SecurityUtils.getUserId())) {
            return error("The current user cannot delete.");
        }
        return toAjax(userService.deleteUserByIds(userIds));
    }

    /**
     * RESETPassword
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "User  management", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * Status modification
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "User  management", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.updateUserStatus(user));
    }

    /**
     * According to the user ID to obtain the authorized role.
     */
    @RequiresPermissions("system:user:query")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(@PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        SysUser user = userService.selectUserById(userId);
        List<SysRole> roles = roleService.selectRolesByUserId(userId);
        ajax.put("user", user);
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        return ajax;
    }

    /**
     * User Authorizes role
     */
    @RequiresPermissions("system:user:edit")
    @Log(title = "User  management", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(Long userId, Long[] roleIds) {
        userService.checkUserDataScope(userId);
        roleService.checkRoleDataScope(roleIds);
        userService.insertUserAuth(userId, roleIds);
        return success();
    }

    /**
     * Send email verification code
     */
    @GetMapping("/sendCode")
    public AjaxResult sendCode(String email,String type,String username) {
        // Send verification code to mobile phone
        Boolean send = false;
        if (StringUtils.isEmpty(email)) {
            return AjaxResult.error(502, "The mailbox cannot be empty");
        }
        boolean b = isEmail(email);
        if (!b) {
            return AjaxResult.error(502, "The mailbox is incorrectly formatted");
        }
        //判断账号是否存在
        SysUser sysUser = userService.selectUserByEmail(email);
        if (ObjectUtils.isNotEmpty(sysUser)) {
            if(username!=null&&!sysUser.getUserName().equals(username)){
                return AjaxResult.error(502, "Email and account do not match");
            }
            send = emailUtils.sendEmail(email,type);
        }else {
            return AjaxResult.error(502, "There is no user in the mailbox");
        }
        if (Boolean.FALSE.equals(send)) {
            return AjaxResult.error(502, "Failed to send verification code");
        }
        return AjaxResult.success(send);
    }

    @PutMapping("/checkEmail")
    public AjaxResult checkEmail(@RequestBody SysUser user) {
        String email = user.getEmail();
        String userName = user.getUserName();
        String codes = redisCache.getCacheObject(RedisConstants.EMAIL_CAPTCHA + email);
        SysUser sysUser = userService.selectUserByEmail(email);
        if (!sysUser.getUserName().equals(userName)) {
            return AjaxResult.warn("Email and account do not match");
        }
        if (codes == null) {
            return AjaxResult.warn("Verification code has expired");
        }
        if (!codes.equals(user.getCode())) {
            return AjaxResult.warn("Verification code is incorrect");
        }
        return AjaxResult.success();
    }

    @Autowired
    private ISysSecuritySettingsService securitySettingsService;

    @PutMapping("/resetPwdByCode")
    public AjaxResult resetPwdByCode(@RequestBody SysUser user) {
        String email = user.getEmail();
        String codes = redisCache.getCacheObject(RedisConstants.EMAIL_CAPTCHA + email);
        if (codes == null) {
            return AjaxResult.warn("Verification code has expired");
        }
        if (!codes.equals(user.getCode())) {
            return AjaxResult.warn("Verification code is incorrect");
        }
        String password = user.getPassword();
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setUpdateBy(email);
        SysUser sysUser = userService.selectUserByEmail(email);
        if (sysUser == null) {
            return error("The mailbox does not exist!");
        }
        securitySettingsService.validatePassword(password, sysUser.getUserId());
        return toAjax(userService.resetPwdByCode(user));
    }

    @PutMapping("/resetPwdByEmail")
    public AjaxResult resetPwdByEmail(@RequestBody SysUser user) {
        String password = user.getPassword();
        String email = user.getEmail();
        user.setPassword(SecurityUtils.encryptPassword(password));
        user.setUpdateBy(email);
        SysUser sysUser = userService.selectUserByEmail(email);
        if (sysUser == null) {
            return null;
        }
        Long userId = sysUser.getUserId();
        securitySettingsService.validatePassword(password, userId);
        userService.resetPwdByCode(user);
        return AjaxResult.success(userId);
    }


    @GetMapping("/getUserInfoByGoogleId/{googleId}")
    public SysUser getUserInfoByGoogleId(@PathVariable("googleId") String googleId) {
        return userService.selectUserByGoogleId(googleId);
    }


    public static boolean isEmail(String input) {
        if (input == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * According to Enterprise IDQUERY
     *
     * @param eid
     * @return
     */
    @GetMapping("/getByEid/{eid}")
    public R<SysUser> getByEid(@PathVariable("eid") String eid) {
        return R.ok(userService.selectUserByEid(eid));
    }
}
