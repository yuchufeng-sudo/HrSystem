package com.ys.common.core.utils;

import com.github.pagehelper.PageHelper;
import com.ys.common.core.utils.sql.SqlUtil;
import com.ys.common.core.web.page.PageDomain;
import com.ys.common.core.web.page.TableSupport;

/**
 *
 *
 * @author ys
 */
public class PageUtils extends PageHelper
{
    /**
     *
     */
    public static void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     *
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
