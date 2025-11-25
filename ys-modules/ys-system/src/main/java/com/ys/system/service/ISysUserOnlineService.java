package com.ys.system.service;

import com.ys.system.api.model.LoginUser;
import com.ys.system.domain.SysUserOnline;

/**
 * ONLINE USERS  Service
 *
 * @author ys
 */
public interface ISysUserOnlineService
{
    /**
     * By Login AddressQUERY Information
     *
     * @param ipaddr Login Address
     * @param user  User Information
     * @return ONLINE User Information
     */
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user);

    /**
     * By User NameQUERY Information
     *
     * @param userName User Name
     * @param user  User Information
     * @return ONLINE User Information
     */
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user);

    /**
     * By Login Address/User NameQUERY Information
     *
     * @param ipaddr Login Address
     * @param userName User Name
     * @param user  User Information
     * @return ONLINE User Information
     */
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user);

    /**
     * Set ONLINE User Information
     *
     * @param user  User Information
     * @return ONLINE USERS
     */
    public SysUserOnline loginUserToUserOnline(LoginUser user);
}
