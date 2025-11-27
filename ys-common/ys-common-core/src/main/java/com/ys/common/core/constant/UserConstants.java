package com.ys.common.core.constant;

/**
 * User constant Information
 *
 * @author ys
 */
public class UserConstants
{
    /**
     *The unique identifier of SystemUSER within the platform
     */
    public static final String SYS_USER = "SYS_USER";

    /** Normal Status */
    public static final String NORMAL = "0";

    /** Abnormal Status */
    public static final String EXCEPTION = "1";

    /** User Blocked Status */
    public static final String USER_DISABLE = "1";

    /** role Normal Status */
    public static final String ROLE_NORMAL = "0";

    /**role ban status */
    public static final String ROLE_DISABLE = "1";

    /**  DEPARTMENT
Normal Status */
    public static final String DEPT_NORMAL = "0";

    /**  DEPARTMENT
Inactive Status */
    public static final String DEPT_DISABLE = "1";

    /** Dictionary Normal Status */
    public static final String DICT_NORMAL = "0";

    /** Whether it is SystemDefault (Yes) */
    public static final String YES = "Y";

    /** Whether it is a menu external link (Yes) */
    public static final String YES_FRAME = "0";

    /** Whether it is a menu external link (No) */
    public static final String NO_FRAME = "1";

    /**  menu Type (Directory) */
    public static final String TYPE_DIR = "M";

    /**  menu  Type（ menu ） */
    public static final String TYPE_menu = "C";

    /**  menu  Type（Button） */
    public static final String TYPE_BUTTON = "F";

    /** Layout Component identifier*/
    public final static String LAYOUT = "Layout";

    /** Identifier of ParentView Component */
    public final static String PARENT_VIEW = "ParentView";

    /**Identifier of InnerLink Component */
    public final static String INNER_LINK = "InnerLink";

    /**  Return identifier for verifying uniqueness */
    public final static boolean UNIQUE = true;
    public final static boolean NOT_UNIQUE = false;

    /**
     * username length restriction
     */
    public static final int USERNAME_MIN_LENGTH = 2;

    public static final int USERNAME_MAX_LENGTH = 20;

    /**
     * Password length restriction
     */
    public static final int PASSWORD_MIN_LENGTH = 5;

    public static final int PASSWORD_MAX_LENGTH = 20;
}
