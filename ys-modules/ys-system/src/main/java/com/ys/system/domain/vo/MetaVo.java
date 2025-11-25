package com.ys.system.domain.vo;

import com.ys.common.core.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Route Display Information
 *
 * @author ys
 */
@Data
public class MetaVo
{
    /**
     * Set the name displayed for this route in the sidebar and breadcrumbs
     */
    private String title;

    /**
     * Set the icon for this route, corresponding to the path src/assets/icons/svg
     */
    private String icon;

    /**
     * Set to true to prevent caching by <keep-alive>
     */
    private boolean noCache;

    /**
     * Internal link address (starting with http(s)://)
     */
    private String link;

    /**
     *  cling path
     */
    private String clingPath;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public MetaVo(String title, String icon, boolean noCache)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    public MetaVo(String title, String icon, String link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public MetaVo(String title, String icon, boolean noCache, String link, String clingPath)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.clingPath = clingPath;
        if (StringUtils.ishttp(link))
        {
            this.link = link;
        }
    }
}
