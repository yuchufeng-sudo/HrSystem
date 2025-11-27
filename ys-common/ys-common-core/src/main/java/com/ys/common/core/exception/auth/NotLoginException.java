package com.ys.common.core.exception.auth;

/**
 * Login authentication exception that fails to pass
 * 
 * @author ys
 */
public class NotLoginException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public NotLoginException(String message)
    {
        super(message);
    }
}
