package com.ys.system.service.impl;

import com.ys.common.core.utils.StringUtils;
import com.ys.system.api.model.LoginUser;
import com.ys.system.domain.SysUserOnline;
import com.ys.system.service.ISysUserOnlineService;
import org.springframework.stereotype.Service;

/**
 * ONLINE USERS Service Layer Processing
 *
 * @author ruoyi
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{
    /**
     * By Login AddressQUERY INFORMATION
     *
     * @param ipaddr Login Address
     * @param user  USER INFORMATION
     * @return ONLINE USER INFORMATION
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * By USER Name QUERY INFORMATION
     *
     * @param userName USER Name
     * @param user  USER INFORMATION
     * @return ONLINE USER INFORMATION
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user)
    {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * By Login Address/USER NameQUERY INFORMATION
     *
     * @param ipaddr Login Address
     * @param userName USER Name
     * @param user  USER INFORMATION
     * @return ONLINE USER INFORMATION
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * Set ONLINE USER INFORMATION
     *
     * @param user  USER INFORMATION
     * @return ONLINE USERS
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user)
    {
        if (StringUtils.isNull(user))
        {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginTime(user.getLoginTime());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        return sysUserOnline;
    }
}
