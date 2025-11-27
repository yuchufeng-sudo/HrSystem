package com.ys.utils.constant;

/**
 * Redis Constants for Verification Codes
 * Contains key prefixes and expiration times for SMS and email verification codes
 *
 * @author ys
 */
public class RedisConstants
{
    /**
     * Phone SMS verification code key prefix
     */
    public static final String PHONE_CAPTCHA = "phone:captcha:";

    /**
     * Email verification code key prefix
     */
    public static final String EMAIL_CAPTCHA = "email:captcha:";

    /**
     * Email 2FA verification code key prefix
     */
    public static final String EMAIL_CAPTCHA_2FA = "email:captcha2fa:";

    /**
     * Phone SMS verification code validity period (in seconds)
     * Default: 5 minutes (300 seconds)
     */
    public static final Integer PHONE_CAPTCHA_VALIDITY = 60 * 5;

    /**
     * Email verification code validity period (in seconds)
     * Default: 5 minutes (300 seconds)
     */
    public static final Integer EMAIL_CAPTCHA_VALIDITY = 60 * 5;

}
