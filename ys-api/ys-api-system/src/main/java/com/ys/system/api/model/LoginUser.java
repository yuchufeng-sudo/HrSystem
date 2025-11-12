package com.ys.system.api.model;

import com.ys.system.api.domain.SysUser;

import java.io.Serializable;
import java.util.Set;

/**
 *  USER INFORMATION
 *
 * @author ruoyi
 */
public class LoginUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * USER Unique Identifier
     */
    private String token;

    /**
     * USER Name ID
     */
    private Long userid;

    /**
     * USER
     */
    private String username;

    /**
     * Login  TIME
     */
    private Long loginTime;

    /**
     * Expired TIME
     */
    private Long expireTime;

    /**
     * Login IPAddress
     */
    private String ipaddr;

    /**
     *  Browser Type
     */
    private String browser;

    /**
     * OPERATION System
     */
    private String os;

    /**
     * OPERATION System
     */
    private String loginLocation;

    /**
     * Permission  LIST
     */
    private Set<String> permissions;

    /**
     * ROLE  LIST
     */
    private Set<String> roles;

    /**
     *  USER INFORMATION
     */
    private SysUser sysUser;

    private String enterpriseStatus;

    private String enterpriseName;

    private String enterpriseLogo;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public Long getUserid()
    {
        return userid;
    }

    public void setUserid(Long userid)
    {
        this.userid = userid;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }

    public Set<String> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<String> roles)
    {
        this.roles = roles;
    }

    public SysUser getSysUser()
    {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser)
    {
        this.sysUser = sysUser;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getLoginLocation() {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public String getEnterpriseStatus() {
        return enterpriseStatus;
    }

    public void setEnterpriseStatus(String enterpriseStatus) {
        this.enterpriseStatus = enterpriseStatus;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseLogo() {
        return enterpriseLogo;
    }

    public void setEnterpriseLogo(String enterpriseLogo) {
        this.enterpriseLogo = enterpriseLogo;
    }
}
