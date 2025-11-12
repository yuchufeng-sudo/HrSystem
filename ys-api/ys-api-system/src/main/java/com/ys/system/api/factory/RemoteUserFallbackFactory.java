package com.ys.system.api.factory;

import com.ys.common.core.domain.R;
import com.ys.system.api.RemoteUserService;
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
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable)
    {
        log.error(" User Service InvocationFailure:{}", throwable.getMessage());
        return new RemoteUserService()
        {
            @Override
            public R<LoginUser> getUserInfo(String username, String source)
            {
                return R.fail("Get User Failure:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(SysUser sysUser, String source)
            {
                return R.fail("Register User Failure:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> recordUserLogin(SysUser sysUser, String source)
            {
                return R.fail("Record User Login Information Failure:" + throwable.getMessage());
            }

            @Override
            public R<SysSecuritySettings> getSettingInfo(String enterpriseId, String source) {
                return R.fail("Failed to obtain security setting information:" + throwable.getMessage());
            }
        };
    }
}
