package com.ys.system.api.factory;

import com.ys.common.core.domain.R;
import com.ys.system.api.RemoteLogService;
import com.ys.system.api.domain.SysLogininfor;
import com.ys.system.api.domain.SysOperLog;
import com.ys.system.api.domain.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 *  LOG Service Degradation Handling
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable)
    {
        log.error(" LOG Service InvocationFailure:{}", throwable.getMessage());
        return new RemoteLogService()
        {
            @Override
            public R<Boolean> saveLog(SysOperLog sysOperLog, String source)
            {
                return R.fail("save OPERATION LOG Failure:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source)
            {
                return R.fail("save LOGIN LOG Failure:" + throwable.getMessage());
            }

            @Override
            public R<Integer> countLogininfor(SysLogininfor sysLogininfor, String source)
            {
                return R.fail("save LOGIN LOG Failure:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> checkEmail(SysUser sysUser, String source) {
                return R.fail("save LOGIN LOG Failure:" + throwable.getMessage());
            }
        };

    }
}
