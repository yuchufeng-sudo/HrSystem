package com.ys.common.core.utils.sql;

import com.ys.common.core.exception.UtilException;
import com.ys.common.core.utils.StringUtils;

/**
 *
 *
 * @author ys
 */
public class SqlUtil
{
    /**
     *
     */
    public static String SQL_REGEX = "and |extractvalue|updatexml|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |+|user()";

    /**
     *
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     *
     */
    private static final int ORDER_BY_MAX_LENGTH = 500;

    /**
     *
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new UtilException("Parameters do not conform to the specification, and Query cannot be performed.");
        }
        if (StringUtils.length(value) > ORDER_BY_MAX_LENGTH)
        {
            throw new UtilException("Parameters have exceeded the maximum limit, and Query cannot be performed.");
        }
        return value;
    }

    /**
     *
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }

    /**
     *
     */
    public static void filterKeyword(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return;
        }
        String[] sqlKeywords = StringUtils.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords)
        {
            if (StringUtils.indexOfIgnoreCase(value, sqlKeyword) > -1)
            {
                throw new UtilException("Parameters have a SQL injection risk.");
            }
        }
    }
}
