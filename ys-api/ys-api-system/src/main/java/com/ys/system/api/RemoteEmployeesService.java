package com.ys.system.api;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.ServiceNameConstants;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.system.api.domain.HrEmployees;
import com.ys.system.api.factory.RemoteEmployeesFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * USER  Service
 * @author Administrator
 */
@FeignClient(contextId = "remoteEmployeesService", value = ServiceNameConstants.HR_SERVICE, fallbackFactory = RemoteEmployeesFallbackFactory.class)
public interface RemoteEmployeesService
{
    @PostMapping(value = "/employees/add")
    public AjaxResult add(@RequestBody HrEmployees hrEmployees, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
