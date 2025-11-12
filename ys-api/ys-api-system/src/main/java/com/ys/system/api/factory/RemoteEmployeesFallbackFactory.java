package com.ys.system.api.factory;

import com.ys.common.core.domain.R;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.system.api.RemoteEmployeesService;
import com.ys.system.api.RemoteUserService;
import com.ys.system.api.domain.HrEmployees;
import com.ys.system.api.domain.SysSecuritySettings;
import com.ys.system.api.domain.SysUser;
import com.ys.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 *  User Service Degradation Handling
 */
@Component
public class RemoteEmployeesFallbackFactory implements FallbackFactory<RemoteEmployeesService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteEmployeesFallbackFactory.class);

    @Override
    public RemoteEmployeesService create(Throwable throwable)
    {
        log.error(" User Service InvocationFailure:{}", throwable.getMessage());
        return new RemoteEmployeesService()
        {
            @Override
            public AjaxResult add(HrEmployees hrEmployees, String source) {
                return AjaxResult.warn("Add Failure:" + throwable.getMessage());
            }
        };
    }
}
