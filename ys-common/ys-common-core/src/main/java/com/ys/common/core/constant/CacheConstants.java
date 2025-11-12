package com.ys.common.core.constant;

/**
 * Cache constant INFORMATION
 *
 * @author ruoyi
 */
public class CacheConstants
{
    /**
     *Cache validity period, Default 720 (minutes)
     */
    public final static long EXPIRATION = 720;

    /**
     * Cache refresh TIME, Default 120 (minutes)
     */
    public final static long REFRESH_TIME = 120;

    /**
     * Maximum number of password errors
     */
    public final static int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * Password lockout TIME, Default 10 (minutes)
     */
    public final static long PASSWORD_LOCK_TIME = 10;

    /**
     * 
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * Login MANAGEMENT staff USER's selected Enterprise Number.
     */
    public static final String LOGIN_SELECT_ENTERPRISE_ID = "login_select_enterprise_id:";

    /**
     * Verification code redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * Parameters MANAGEMENT   cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * Dictionary  MANAGEMENT   cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 
     */
    public static final String CIPHER_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * Login IP blacklist cache key
     */
    public static final String SYS_LOGIN_BLACKIPLIST = SYS_CONFIG_KEY + "sys.login.blackIPList";
}
