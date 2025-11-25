package com.ys.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author ys
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUserOnline
{

    private String tokenId;

    /** User Name */
    private String userName;

    /** Login IP Address */
    private String ipaddr;

    /** Login Address */
    private String loginLocation;

    /**  Browser Type */
    private String browser;

    /** operating System */
    private String os;

    /** Login Time  */
    private Long loginTime;
}
