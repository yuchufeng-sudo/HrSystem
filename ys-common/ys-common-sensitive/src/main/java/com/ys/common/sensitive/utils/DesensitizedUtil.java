package com.ys.common.sensitive.utils;

import com.ys.common.core.utils.StringUtils;

/**
 * 
 *
 * @author ruoyi
 */
public class DesensitizedUtil
{
    /**
     * 
     *
     * @param password Password
     * @return
     */
    public static String password(String password)
    {
        if (StringUtils.isBlank(password))
        {
            return StringUtils.EMPTY;
        }
        return StringUtils.repeat('*', password.length());
    }

    /**
     * 
     *
     * @param carLicense 
     * @return
     */
    public static String carLicense(String carLicense)
    {
        if (StringUtils.isBlank(carLicense))
        {
            return StringUtils.EMPTY;
        }
        
        if (carLicense.length() == 7)
        {
            carLicense = StringUtils.hide(carLicense, 3, 6);
        }
        else if (carLicense.length() == 8)
        {
            
            carLicense = StringUtils.hide(carLicense, 3, 7);
        }
        return carLicense;
    }
}
