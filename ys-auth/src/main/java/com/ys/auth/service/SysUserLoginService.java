package com.ys.auth.service;


import com.ys.system.api.domain.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ys-system")
public interface SysUserLoginService {

    @GetMapping("/user/getUserInfoByGoogleId/{googleId}")
    public SysUser getUserInfoByGoogleId(@PathVariable("googleId") String googleId);

}
