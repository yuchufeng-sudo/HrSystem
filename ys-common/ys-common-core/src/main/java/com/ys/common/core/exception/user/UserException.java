package com.ys.common.core.exception.user;

import com.ys.common.core.exception.base.BaseException;

/**
 *  User Information Exception Class
 *
 * @author ys
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
