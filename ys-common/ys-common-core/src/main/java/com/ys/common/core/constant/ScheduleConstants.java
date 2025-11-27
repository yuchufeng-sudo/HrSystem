package com.ys.common.core.constant;

/**
 * General Constants for Task Scheduling
 *
 * @author ys
 */
public class ScheduleConstants
{
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** Execute target key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** Default  */
    public static final String MISFIRE_DEFAULT = "0";

    /** Trigger Execute immediately  */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /** Trigger an Execute  */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /** Do not trigger an immediate Execute  */
    public static final String MISFIRE_DO_NOTHING = "3";

    public enum Status
    {
        /**
         * Normal
         */
        NORMAL("0"),
        /**
         * Pause
         */
        PAUSE("1");

        private String value;

        private Status(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
