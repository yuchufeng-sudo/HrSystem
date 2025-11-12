package com.ys.auth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ys.auth.form.LoginBody;
import com.ys.auth.form.RegisterBody;
import com.ys.auth.service.SysLoginService;
import com.ys.auth.service.SysRecordLogService;
import com.ys.auth.service.SysUserLoginService;
import com.ys.common.core.domain.R;
import com.ys.common.core.utils.JwtUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.ip.IpUtils;
import com.ys.common.security.auth.AuthUtil;
import com.ys.common.security.service.TokenService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.model.LoginUser;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.openssl.EncryptionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * token
 */
@RestController
public class TokenController {
    private final String GOOGLE_CLIENT_ID = "712461820455-cbt810gttmndp0hb0q6pjt91j4ehiecs.apps.googleusercontent.com";

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private SysUserLoginService userService;

    @Autowired
    private SysRecordLogService recordLogService;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form) {
        boolean email = isEmail(form.getUsername());
        LoginUser userInfo = null;
        String ipAddr = IpUtils.getIpAddr();
        Integer i = recordLogService.countRecordLogininfor(form.getUsername(), ipAddr);
        if (i!=null&&i==0&&(form.getEmail()==null||form.getCode()==null)){
            return R.fail("Discovered remote login, please perform Two Factor Authentication");
        }
        if (i!=null&&i==0){
            recordLogService.checkEmail(form.getUsername(), form.getEmail(),form.getCode());
        }
        if (email) {
            // emailLogin
            userInfo = sysLoginService.loginEmail(form.getUsername(), form.getPassword());
        } else {
            // userLogin
            userInfo = sysLoginService.login(form.getUsername(), form.getPassword());
        }

        // OBTAIN Login token
        Map<String, Object> token = tokenService.createToken(userInfo);
        return R.ok(token);
    }

    @PostMapping("checkIP")
    public R<?> checkIP(@RequestBody LoginBody form){
        String ipAddr = IpUtils.getIpAddr();
        Integer i = recordLogService.countRecordLogininfor(form.getUsername(), ipAddr);
        Map<String, Object> rspMap = new HashMap<>();
        if (i!=null&&i==0){
            rspMap.put("type",2);
        }else {
            rspMap.put("type",1);
        }
        return R.ok(rspMap);
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // DELETEUSER Record
            AuthUtil.logoutByToken(token);
            // RecordUSER  LOG
            sysLoginService.logout(username);
        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            //
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        // USER Register
        sysLoginService.register(registerBody.getUsername(), registerBody.getPassword());
        return R.ok();
    }

    @PutMapping("/google")
    public R<?> GoogleLicensed(@RequestBody Map<String, String> body) {
        String idTokenString = body.get("idToken");
        if (idTokenString == null || idTokenString.trim().isEmpty()) {
            return R.fail("The googleID token is missing");
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpTransport httpTransport = new NetHttpTransport.Builder()
                .setProxy(proxy)
                .build();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(httpTransport, jsonFactory)
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload tokenPayload = idToken.getPayload();
                String email = tokenPayload.getEmail();
                String name = (String) tokenPayload.get("name");
                String pictureUrl = (String) tokenPayload.get("picture");
                String googleId = (String) tokenPayload.get("sub");
                Map<String, Object> result = new HashMap<>();
                result.put("email", email);
                result.put("name", name);
                result.put("avatar", pictureUrl);
                result.put("googleId", googleId);
                SysUser sysUser = userService.getUserInfoByGoogleId(email);
                if (ObjectUtils.isEmpty(sysUser)) {
                    return R.ok(result);
                }
                //
                sysUser.setPassword(SecurityUtils.encryptPassword(sysUser.getPassword()));
                LoginUser userInfo = sysLoginService.loginByGoole(sysUser.getUserName());
                // OBTAIN Login token
                return R.ok(tokenService.createToken(userInfo));
            } else {
                return R.fail("Google sign-in failed");
            }
        } catch (Exception e) {
            return R.fail("Google sign-in failed：" + e.getMessage());
        }
    }

    /**
     * Handles user login to CRM system
     * @param form LoginBody containing username and password
     * @return R<Map<String, Object>> response with token information or error message
     */
    @PostMapping("/loginToCRM")
    public R<Map<String, Object>> loginToCRM(@RequestBody LoginBody form) {
        // Validate input
        if (form == null || StringUtils.isEmpty(form.getUsername()) || StringUtils.isEmpty(form.getPassword())) {
            return R.fail("Username and password are required");
        }
        try {
            LoginUser userInfo = sysLoginService.authenticateUser(form);
            // Create and encrypt token
            Map<String, Object> tokenResponse = sysLoginService.createEncryptedToken(userInfo);
            return R.ok(tokenResponse);
        } catch (AuthenticationException e) {
            return R.fail("Invalid credentials");
        } catch (EncryptionException e) {
            return R.fail("Authorization failed, please try again");
        } catch (Exception e) {
            return R.fail("Login failed, please try again");
        }
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
}
