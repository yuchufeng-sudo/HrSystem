package com.ys.system.api;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.ServiceNameConstants;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.system.api.domain.SysEnterprise;
import com.ys.system.api.factory.RemoteCareersFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * USER  Service
 * @author Administrator
 */
@FeignClient(contextId = "remoteCareersService", value = ServiceNameConstants.HR_SERVICE, fallbackFactory = RemoteCareersFallbackFactory.class)
public interface RemoteCareersService
{
    @PostMapping(value = "/careers/add")
    public AjaxResult add(@RequestBody SysEnterprise enterprise, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
