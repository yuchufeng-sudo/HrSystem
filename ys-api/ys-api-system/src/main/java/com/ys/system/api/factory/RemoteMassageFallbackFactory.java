package com.ys.system.api.factory;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.domain.R;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.system.api.RemoteMessageService;
import com.ys.system.api.domain.SysMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * FileService Degradation Handling
 */
@Component
public class RemoteMassageFallbackFactory implements FallbackFactory<RemoteMessageService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteMassageFallbackFactory.class);

    @Override
    public RemoteMessageService create(Throwable throwable)
    {
        log.error("Message  Notice Service InvocationFailure:{}", throwable.getMessage());
        return new RemoteMessageService()
        {
            @Override
            public R<Boolean> sendMessage(SysMessage sysMessage, String source) {
                return R.fail("SendMessage Failure:" + throwable.getMessage());
            }
            @Override
            public R<Boolean> sendMessageByTemplate(@RequestBody SysMessage sysMessage, String source) {
                return R.fail("SendMessage Failure:" + throwable.getMessage());
            }
            @Override
            public AjaxResult getInfo(@RequestBody Long userId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source){
                return AjaxResult.warn("GetInfo Failure:" + throwable.getMessage());
            }
        };
    }
}
