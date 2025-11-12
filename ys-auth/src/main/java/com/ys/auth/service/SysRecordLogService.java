package com.ys.auth.service;

import com.ys.common.core.constant.Constants;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.domain.R;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.ip.IpUtils;
import com.ys.system.api.RemoteLogService;
import com.ys.system.api.domain.SysLogininfor;
import com.ys.system.api.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Record LOG
 *
 * @author ruoyi
 */
@Component
public class SysRecordLogService
{
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * RecordLogin  INFORMATION
     *
     * @param username USER Name
     * @param status Status
     * @param message Message Content
     * @return
     */
    public void recordLogininfor(String username, String status, String message)
    {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr());
        logininfor.setMsg(message);
        //  LOG Status
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER))
        {
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        }
        else if (Constants.LOGIN_FAIL.equals(status))
        {
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
    }

    public Integer countRecordLogininfor(String username, String ipaddr)
    {
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(ipaddr);
        logininfor.setStatus("0");
        return remoteLogService.countLogininfor(logininfor, SecurityConstants.INNER).getData();
    }

    public void checkEmail(String username, String email, String code)
    {
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setEmail(email);
        sysUser.setCode(code);
        R<Boolean> booleanR = remoteLogService.checkEmail(sysUser, SecurityConstants.INNER);
        if (booleanR.getCode()==R.FAIL) {
            throw new ServiceException(booleanR.getMsg());
        }
    }
}
