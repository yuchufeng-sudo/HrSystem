package com.ys.system.service.impl;

import com.ys.common.core.utils.StringUtils;
import com.ys.system.api.model.LoginUser;
import com.ys.system.domain.SysUserOnline;
import com.ys.system.service.ISysUserOnlineService;
import org.springframework.stereotype.Service;

/**
 * ONLINE USERS Service Layer Processing
 *
 * @author ys
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{
    /**
     * By Login AddressQUERY Information
     *
     * @param ipaddr Login Address
     * @param user  User Information
     * @return ONLINE User Information
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
     * By User Name Query Information
     *
     * @param userName User Name
     * @param user  User Information
     * @return ONLINE User Information
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
     * By Login Address/User NameQUERY Information
     *
     * @param ipaddr Login Address
     * @param userName User Name
     * @param user  User Information
     * @return ONLINE User Information
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
     * Set ONLINE User Information
     *
     * @param user  User Information
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
