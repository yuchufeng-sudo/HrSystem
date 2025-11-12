package com.ys.common.core.web.page;

import com.ys.common.core.text.Convert;
import com.ys.common.core.utils.ServletUtils;

/**
 * 
 * 
 * @author ruoyi
 */
public class TableSupport
{
    /**
     * Current Record start index
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * Number of Records displayed per page
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * Sorting column
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * The direction of Sorting is "desc" or "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 
     */
    public static final String REASONABLE = "reasonable";

    /**
     * 
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        pageDomain.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        pageDomain.setReasonable(ServletUtils.getParameterToBool(REASONABLE));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
