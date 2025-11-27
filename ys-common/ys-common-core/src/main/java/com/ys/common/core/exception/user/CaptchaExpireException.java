package com.ys.common.core.exception.user;

/**
 * Verification Code Expired Exception Class
 * 
 * @author ys
 */
public class CaptchaExpireException extends UserException
{
    private static final long serialVersionUID = 1L;

    public CaptchaExpireException()
    {
        super("user.jcaptcha.expire", null);
    }
}
