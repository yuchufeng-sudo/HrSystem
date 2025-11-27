package com.ys.common.sensitive.enums;

import java.util.function.Function;
import com.ys.common.sensitive.utils.DesensitizedUtil;

/**
 * Desensitization Type Enumeration
 * Defines various types of data desensitization strategies for sensitive information
 *
 * @author ys
 */
public enum DesensitizedType
{
    /**
     * Username: Replace the 2nd character with asterisk
     * Example: 张三 -> 张*三
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

    /**
     * Password: Replace all characters with asterisks
     * Example: password123 -> ***********
     */
    PASSWORD(DesensitizedUtil::password),

    /**
     * ID Card: Replace middle 10 digits with asterisks
     * Example: 123456789012345678 -> 1234** **** ****5678
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\d{3}[Xx]|\\d{4})", "$1** **** ****$2")),

    /**
     * Phone Number: Replace middle 4 digits with asterisks
     * Example: 13812345678 -> 138****5678
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    /**
     * Email: Show only the first character and the domain, replace the rest with asterisks
     * Example: example@email.com -> e****@email.com
     */
    EMAIL(s -> s.replaceAll("(^.)[^@]*(@.*$)", "$1****$2")),

    /**
     * Bank Card Number: Keep the last 4 digits, replace the rest with asterisks
     * Example: 1234567890123456 -> **** **** **** **** 3456
     */
    BANK_CARD(s -> s.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1")),

    /**
     * Car License Plate: Supports both regular and new energy vehicle plates
     * Example: 京A12345 -> 京A1***5
     */
    CAR_LICENSE(DesensitizedUtil::carLicense);

    private final Function<String, String> desensitizer;

    DesensitizedType(Function<String, String> desensitizer)
    {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer()
    {
        return desensitizer;
    }
}
