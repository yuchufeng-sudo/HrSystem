package com.ys.common.core.constant;

/**
 * General constant INFORMATION
 *
 * @author ruoyi
 */
public class Constants
{
    /**
     * UTF-8 Character set
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK  Character set
     */
    public static final String GBK = "GBK";

    /**
     * WWW primary domain
     */
    public static final String WWW = "www.";

    /**
     * RMI Remote Method Invocation
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     *LDAP Remote Method Invocation
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * 
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * http Request
     */
    public static final String HTTP = "http://";

    /**
     * https Request
     */
    public static final String HTTPS = "https://";

    /**
     * SUCCESS marker
     */
    public static final Integer SUCCESS = 200;

    /**
     * Failure marker
     */
    public static final Integer FAIL = 500;

    /**
     * Login SUCCESSStatus
     */
    public static final String LOGIN_SUCCESS_STATUS = "0";

    /**
     * Login FailureStatus
     */
    public static final String LOGIN_FAIL_STATUS = "1";

    /**
     * Login SUCCESS
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * Logout
     */
    public static final String LOGOUT = "Logout";

    /**
     * Register
     */
    public static final String REGISTER = "Register";

    /**
     * Login Failure
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * Current Record start index
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * Number of Records displayed per page
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * Sorting column
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * The direction of Sorting is "desc" or "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * Verification code validity period (minutes)
     */
    public static final long CAPTCHA_EXPIRATION = 2;

    /**
     * Resource mapping path prefix
     */
    public static final String RESOURCE_PREFIX = "/profile";
}
