package com.ys.common.core.exception;

/**
 * Business Exception
 * 
 * @author ys
 */
public final class ServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    /**
     * Error code
     */
    private Integer code;

    /**
     * Error prompt
     */
    private String message;

    /**
     * Error details, internal debugging error
     *
     * and the design consistent with {@link CommonResult#getDetailMessage()}
     */
    private String detailMessage;

    /**
     * Empty constructor to avoid deserialization issues.
     */
    public ServiceException()
    {
    }

    public ServiceException(String message)
    {
        this.message = message;
    }

    public ServiceException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }

    public String getDetailMessage()
    {
        return detailMessage;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }

    public ServiceException setMessage(String message)
    {
        this.message = message;
        return this;
    }

    public ServiceException setDetailMessage(String detailMessage)
    {
        this.detailMessage = detailMessage;
        return this;
    }
}
