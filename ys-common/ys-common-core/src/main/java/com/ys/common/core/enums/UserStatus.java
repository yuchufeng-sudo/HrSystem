package com.ys.common.core.enums;

/**
 * USER Status
 * 
 * @author ruoyi
 */
public enum UserStatus
{
    OK("0", "Normal "), DISABLE("1", "Disable"), DELETED("2", "DELETE");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
