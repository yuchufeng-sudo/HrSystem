package com.ys.system.api;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.ServiceNameConstants;
import com.ys.common.core.domain.R;
import com.ys.system.api.domain.SysLogininfor;
import com.ys.system.api.domain.SysOperLog;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *  LOG  Service
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService
{
    /**
     * save  System LOG
     *
     * @param sysOperLog  LOG
     * @param source  Request
     * @return Result
     */
    @PostMapping("/operlog")
    public R<Boolean> saveLog(@RequestBody SysOperLog sysOperLog, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) throws Exception;

    /**
     * save AccessRecord
     *
     * @param sysLogininfor Access
     * @param source  Request
     * @return Result
     */
    @PostMapping("/logininfor")
    public R<Boolean> saveLogininfor(@RequestBody SysLogininfor sysLogininfor, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/logininfor/count")
    public R<Integer> countLogininfor(SysLogininfor sysLogininfor, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PutMapping("/user/checkEmail")
    public R<Boolean> checkEmail(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
