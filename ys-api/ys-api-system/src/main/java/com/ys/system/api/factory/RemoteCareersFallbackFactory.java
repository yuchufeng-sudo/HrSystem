package com.ys.system.api.factory;

import com.ys.common.core.web.domain.AjaxResult;
import com.ys.system.api.RemoteCareersService;
import com.ys.system.api.RemoteEmployeesService;
import com.ys.system.api.domain.HrEmployees;
import com.ys.system.api.domain.SysEnterprise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 *  User Service Degradation Handling
 */
@Component
public class RemoteCareersFallbackFactory implements FallbackFactory<RemoteCareersService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteCareersFallbackFactory.class);

    @Override
    public RemoteCareersService create(Throwable throwable)
    {
        log.error(" User Service InvocationFailure:{}", throwable.getMessage());
        return new RemoteCareersService()
        {
            @Override
            public AjaxResult add(SysEnterprise enterprise, String source) {
                return AjaxResult.warn("Add Failure:" + throwable.getMessage());
            }
        };
    }
}
