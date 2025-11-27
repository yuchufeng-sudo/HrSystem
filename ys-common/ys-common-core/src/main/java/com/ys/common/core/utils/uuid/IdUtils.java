package com.ys.common.core.utils.uuid;

/**
 * 
 * 
 * @author ys
 */
public class IdUtils
{
    /**
     * 
     * 
     * @return
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 
     * 
     * @return 
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 
     * 
     * @return 
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 
     * 
     * @return 
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }
}
