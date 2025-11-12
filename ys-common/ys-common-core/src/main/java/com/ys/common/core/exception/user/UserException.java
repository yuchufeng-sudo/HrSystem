package com.ys.common.core.exception.user;

import com.ys.common.core.exception.base.BaseException;

/**
 *  USER INFORMATION Exception Class
 * 
 * @author ruoyi
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
