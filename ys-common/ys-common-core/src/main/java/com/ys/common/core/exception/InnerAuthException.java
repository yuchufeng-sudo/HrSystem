package com.ys.common.core.exception;

/**
 * Internal Authentication Exception
 *
 * @author ys
 */
public class InnerAuthException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message)
    {
        super(message);
    }
}
