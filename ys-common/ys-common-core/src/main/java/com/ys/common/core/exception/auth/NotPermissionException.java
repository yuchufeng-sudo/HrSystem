package com.ys.common.core.exception.auth;

import org.apache.commons.lang3.StringUtils;

/**
 * Permission authentication exception that fails to pass
 *
 * @author ys
 */
public class NotPermissionException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NotPermissionException(String permission)
    {
        super(permission);
    }

    public NotPermissionException(String[] permissions)
    {
        super(StringUtils.join(permissions, ","));
    }
}
