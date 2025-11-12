package com.ys.auth.service;

import com.alibaba.fastjson2.JSONObject;
import com.ys.auth.form.LoginBody;
import com.ys.common.core.constant.CacheConstants;
import com.ys.common.core.constant.Constants;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.UserConstants;
import com.ys.common.core.domain.R;
import com.ys.common.core.enums.UserStatus;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.text.Convert;
import com.ys.common.core.utils.AESUtils;
import com.ys.common.core.utils.DateUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.ip.IpUtils;
import com.ys.common.redis.service.RedisService;
import com.ys.common.security.service.TokenService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.RemoteUserService;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.model.LoginUser;
import org.bouncycastle.openssl.EncryptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Login Validation Method
 *
 * @author ruoyi
 */
@Component
public class SysLoginService
{
    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TokenService tokenService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public LoginUser login(String username, String password)
    {
        if (StringUtils.isAnyBlank(username, password))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "User/Password/Required");
            throw new ServiceException("User/Password/Required");
        }

//        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
//                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
//        {
//            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "The user password is not in the specified range");
//            throw new ServiceException("The user password is not in the specified range");
//        }

        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "The username is not in the specified range");
            throw new ServiceException("The username is not in the specified range");
        }

        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        String ipAddr = IpUtils.getIpAddr();
        if (IpUtils.isMatchedIp(blackStr, ipAddr))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Unfortunately, the access IP has been blacklisted by the system");
            throw new ServiceException("Unfortunately, the access IP has been blacklisted by the system");
        }
        // QUERY USER INFORMATION
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);

        if (R.FAIL == userResult.getCode())
        {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Sorry, your account has been deleted");
            throw new ServiceException("Sorry, your account:" + username + " has been deleted");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus()))
        {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "The user has been deactivated, please contact your administrator");
            throw new ServiceException("Sorry, your account:" + username + " Disabled");
        }

        String enterpriseStatus = userResult.getData().getEnterpriseStatus();
        if ("0".equals(enterpriseStatus)) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Enterprise disabled");
            throw new ServiceException("Enterprise disabled: " + username);
        }

        passwordService.validate(user, password);
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "Login successful");
        recordLoginInfo(user.getUserId());
        return userInfo;
    }

    /**
     * emailLogin
     */
    public LoginUser loginEmail(String username, String password) {
        // Check if username or password is empty
        if (StringUtils.isAnyBlank(username, password)) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Email/Password must be filled in");
            throw new ServiceException("Email/Password must be filled in");
        }

        // Validate password length
//        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
//                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
//            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Password length is not within the specified range");
//            throw new ServiceException("Password length is not within the specified range");
//        }

        // Validate email format
        if (!isEmail(username)) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Invalid email format");
            throw new ServiceException("Invalid email format");
        }

        // Check IP blacklist
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Your IP address has been blacklisted");
            throw new ServiceException("Your IP address has been blacklisted");
        }

        // Get user info
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();

        // Check account status
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Account has been deleted: " + username);
            throw new ServiceException("Account has been deleted: " + username);
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Account disabled. Contact administrator");
            throw new ServiceException("Account disabled. Contact administrator");
        }

        String enterpriseStatus = userResult.getData().getEnterpriseStatus();
        if ("0".equals(enterpriseStatus)) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Enterprise disabled");
            throw new ServiceException("Enterprise disabled: " + username);
        }

        // Validate password
        passwordService.validate(user, password);

        // Record success
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "Login successful");
        recordLoginInfo(user.getUserId());

        return userInfo;
    }

    /**
     * google Login
     */
    public LoginUser loginByGoole(String username) {
        // IP Blacklist check
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Your IP address has been blacklisted");
            throw new ServiceException("Your IP address has been blacklisted");
        }

        // Get user info
        R<LoginUser> userResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }

        LoginUser userInfo = userResult.getData();
        SysUser user = userResult.getData().getSysUser();

        // Check account status
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Account has been deleted");
            throw new ServiceException("Account has been deleted: " + username);
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Account disabled. Contact administrator");
            throw new ServiceException("Account disabled: " + username);
        }

        String enterpriseStatus = userResult.getData().getEnterpriseStatus();
        if ("0".equals(enterpriseStatus)) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "Enterprise disabled");
            throw new ServiceException("Enterprise disabled: " + username);
        }

        // Record success
        recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "Login successful");
        recordLoginInfo(user.getUserId());

        return userInfo;
    }

    /**
     * RecordLogin  INFORMATION
     *
     * @param userId USER ID
     */
    public void recordLoginInfo(Long userId)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        // Update USER Login IP
        sysUser.setLoginIp(IpUtils.getIpAddr());
        // Update USER Login  TIME
        sysUser.setLoginDate(DateUtils.getNowDate());
        remoteUserService.recordUserLogin(sysUser, SecurityConstants.INNER);
    }

    public void logout(String loginName)
    {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "Logout SUCCESS");
    }

    /**
     * Register
     */
    public void register(String username, String password)
    {
        // The user name or password is empty. Error.
        if (StringUtils.isAnyBlank(username, password))
        {
            throw new ServiceException("USER /PasswordMust be filled in");
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            throw new ServiceException("The account LENGTH must be between 2 and 20 Characters.");
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            throw new ServiceException("Password LENGTH must be between 5 and 20 Characters.");
        }

        // Register USER INFORMATION
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(SecurityUtils.encryptPassword(password));
        R<?> registerResult = remoteUserService.registerUserInfo(sysUser, SecurityConstants.INNER);

        if (R.FAIL == registerResult.getCode())
        {
            throw new ServiceException(registerResult.getMsg());
        }
        recordLogService.recordLogininfor(username, Constants.REGISTER, "RegisterSUCCESS");
    }

    //email
    public static boolean isEmail(String input) {
        if (input == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    /**
     * Authenticates user based on username format (email or username)
     * @param form LoginBody containing credentials
     * @return LoginUser authenticated user information
     * @throws AuthenticationException if authentication fails
     */
    public LoginUser authenticateUser(LoginBody form) throws AuthenticationException {
        if (form == null || StringUtils.isEmpty(form.getUsername()) || StringUtils.isEmpty(form.getPassword())) {
            throw new AuthenticationException("Username and password are required");
        }

        boolean isEmailFormat = isEmail(form.getUsername());
        if (isEmailFormat) {
            return loginEmail(form.getUsername(), form.getPassword());
        } else {
            return login(form.getUsername(), form.getPassword());
        }
    }

    /**
     * Creates encrypted token with user information
     * @param userInfo LoginUser object containing user details
     * @return Map<String, Object> containing token and encrypted user info
     * @throws EncryptionException if encryption fails
     */
    public Map<String, Object> createEncryptedToken(LoginUser userInfo) throws EncryptionException {
        if (userInfo == null) {
            throw new IllegalArgumentException("User information cannot be null");
        }

        try {
            // Create base token
            Map<String, Object> tokenResponse = tokenService.createToken(userInfo);

            // Prepare user info for encryption
            Map<String, Object> userInfoMap = new HashMap<>();
            userInfoMap.put("userInfo", userInfo);

            // Encrypt user information
            String userInfoJson = JSONObject.toJSONString(userInfoMap);
            String encryptedUserInfo = AESUtils.encrypt(userInfoJson);
            tokenResponse.put("userInfo", encryptedUserInfo);

            return tokenResponse;

        } catch (Exception e) {
            throw new EncryptionException("Failed to encrypt user information", e);
        }
    }
}
