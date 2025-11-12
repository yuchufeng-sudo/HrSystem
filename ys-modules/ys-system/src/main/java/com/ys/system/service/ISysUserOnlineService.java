package com.ys.system.service;

import com.ys.system.api.model.LoginUser;
import com.ys.system.domain.SysUserOnline;

/**
 * ONLINE USERS  Service
 * 
 * @author ruoyi
 */
public interface ISysUserOnlineService
{
    /**
     * By Login AddressQUERY INFORMATION
     * 
     * @param ipaddr Login Address
     * @param user  USER INFORMATION
     * @return ONLINE USER INFORMATION
     */
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * By USER NameQUERY INFORMATION
     * 
     * @param userName USER Name
     * @param user  USER INFORMATION
     * @return ONLINE USER INFORMATION
     */
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * By Login Address/USER NameQUERY INFORMATION
     * 
     * @param ipaddr Login Address
     * @param userName USER Name
     * @param user  USER INFORMATION
     * @return ONLINE USER INFORMATION
     */
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * Set ONLINE USER INFORMATION
     * 
     * @param user  USER INFORMATION
     * @return ONLINE USERS
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user);
}
