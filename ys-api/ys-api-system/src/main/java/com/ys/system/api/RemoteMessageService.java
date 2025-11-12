package com.ys.system.api;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.constant.ServiceNameConstants;
import com.ys.common.core.domain.R;
import com.ys.common.core.web.domain.AjaxResult;
import com.ys.system.api.domain.SysMessage;
import com.ys.system.api.factory.RemoteMassageFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Message Send Service
 */
@FeignClient(contextId = "remoteMessageService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteMassageFallbackFactory.class)
public interface RemoteMessageService
{
    /**
     * SendMessage
     *
     * @param sysMessage Message
     * @param source  Request
     * @return Result
     */
    @PostMapping("/message/sendMessage")
    public R<Boolean> sendMessage(@RequestBody SysMessage sysMessage, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping(value = "/message/sendMessageByTemplate")
    public R<Boolean> sendMessageByTemplate(@RequestBody SysMessage sysMessage, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    @PostMapping(value = "/notificationSettings/setting")
    public AjaxResult getInfo(@RequestBody Long userId,@RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
