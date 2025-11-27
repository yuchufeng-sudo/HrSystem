package com.ys.common.log.service;

import com.ys.common.core.constant.SecurityConstants;
import com.ys.system.api.RemoteLogService;
import com.ys.system.api.domain.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Asynchronous call to log service
 *
 * @author ys
 */
@Service
public class AsyncLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * Save system log records
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception
    {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
