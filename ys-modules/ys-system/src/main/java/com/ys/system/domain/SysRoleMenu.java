package com.ys.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 *
 *
 * @author ys
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleMenu
{
    /** role ID */
    private Long roleId;

    /**  menu ID */
    private Long menuId;
}
