package com.ys.common.core.exception.auth;

import org.apache.commons.lang3.StringUtils;

/**
 *role authentication exception that fails to pass
 *
 * @author ys
 */
public class NotRoleException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NotRoleException(String role)
    {
        super(role);
    }

    public NotRoleException(String[] roles)
    {
        super(StringUtils.join(roles, ","));
    }
}
