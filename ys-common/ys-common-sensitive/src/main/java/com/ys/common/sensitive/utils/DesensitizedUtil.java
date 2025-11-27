package com.ys.common.sensitive.utils;

import com.ys.common.core.utils.StringUtils;

/**
 * Desensitization Utility Class
 * Provides utility methods for masking sensitive data
 *
 * @author ruoyi
 */
public class DesensitizedUtil
{
    /**
     * Replace all characters in password with asterisks
     * Example: password123 -> ***********
     *
     * @param password Original password string
     * @return Desensitized password with all characters replaced by asterisks
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
     * Replace middle characters of car license plate with asterisks
     * If the license plate format is invalid, returns it unchanged
     *
     * @param carLicense Complete car license plate number
     * @return Desensitized car license plate
     */
    public static String carLicense(String carLicense)
    {
        if (StringUtils.isBlank(carLicense))
        {
            return StringUtils.EMPTY;
        }
        // Regular car license plate (7 characters)
        if (carLicense.length() == 7)
        {
            carLicense = StringUtils.hide(carLicense, 3, 6);
        }
        else if (carLicense.length() == 8)
        {
            // New energy vehicle license plate (8 characters)
            carLicense = StringUtils.hide(carLicense, 3, 7);
        }
        return carLicense;
    }
}
