package com.ys.system.domain.vo;

import com.ys.common.core.utils.StringUtils;

/**
 * Route Display INFORMATION
 *
 * @author ruoyi
 */
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

    public boolean isNoCache()
    {
        return noCache;
    }

    public void setNoCache(boolean noCache)
    {
        this.noCache = noCache;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }

    public String getClingPath() {
        return clingPath;
    }

    public void setClingPath(String clingPath) {
        this.clingPath = clingPath;
    }
}
