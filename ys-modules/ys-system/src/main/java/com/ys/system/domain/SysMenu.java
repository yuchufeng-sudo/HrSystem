package com.ys.system.domain;

import com.ys.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 *  menu Permission sys_menu
 *
 * @author ys
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** menu ID */
    private Long menuId;

    /** menu Name */
    @NotBlank(message = "menu name cannot be empty.")
    @Size(min = 0, max = 50, message = " The length of the menu name cannot exceed 50 characters.")
    private String menuName;

    /** menu Name */
    private String parentName;

    /** menu ID */
    private Long parentId;

    /** cling path */
    private String clingPath;

    /** Display Order */
    @NotNull(message = "Display Order cannot be empty.")
    private Integer orderNum;

    /** Route Address */
    @Size(min = 0, max = 200, message = "The route address cannot exceed 200 characters.")
    private String path;

    /** Component */
    @Size(min = 0, max = 200, message = "The component path cannot exceed 255 characters.")
    private String component;

    /** Parameters */
    private String query;

    private String routeName;

    private String isFrame;

    private String isCache;

    @NotBlank(message = "menu Type cannot be empty.")
    private String menuType;

    private String visible;

    private String status;

    @Size(min = 0, max = 100, message = "The length of the permission identifier cannot exceed 100 characters.")
    private String perms;

    private String icon;

    private List<SysMenu> children = new ArrayList<SysMenu>();
}
