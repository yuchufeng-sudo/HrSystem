package com.ys.common.core.exception;

/**
 * Internal Authentication Exception
 * 
 * @author ruoyi
 */
public class InnerAuthException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public InnerAuthException(String message)
    {
        super(message);
    }
}
