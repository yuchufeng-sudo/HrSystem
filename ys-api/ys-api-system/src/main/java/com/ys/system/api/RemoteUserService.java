package com.ys.system.api;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.ServiceNameConstants;
import com.ys.common.core.domain.R;
import com.ys.system.api.domain.SysSecuritySettings;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.factory.RemoteUserFallbackFactory;
import com.ys.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * USER  Service
 * @author Administrator
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    /**
     *
     *
     * @param username USER
     * @param source  Request
     * @return Result
     */
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * Register USER INFORMATION
     *
     * @param sysUser  USER INFORMATION
     * @param source  Request
     * @return Result
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * RecordUSER Login IPAddress Login  TIME
     *
     * @param sysUser  USER INFORMATION
     * @param source  Request
     * @return Result
     */
    @PutMapping("/user/recordlogin")
    public R<Boolean> recordUserLogin(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * Get enterprise security settings
     * @param enterpriseId
     * @param source  Request
     * @return Result
     */
    @GetMapping("/securitySettings/getSettingInfo/{enterpriseId}")
    public R<SysSecuritySettings> getSettingInfo(@PathVariable("enterpriseId") String enterpriseId,
                                                 @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
