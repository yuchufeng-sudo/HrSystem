package com.ys.common.core.utils;

import com.ys.common.core.constant.Constants;
import com.ys.common.core.text.StrFormatter;
import org.springframework.util.AntPathMatcher;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 *
 * @author ys
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils
{

    private static final String NULLSTR = "";


    private static final char SEPARATOR = '_';


    private static final char ASTERISK = '*';

    /**
     *
     *
     * @param value
     * @return value Return
     */
    public static <T> T nvl(T value, T defaultValue)
    {
        return value != null ? value : defaultValue;
    }

    /**
     * *
     *
     * @param coll
     * @return
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * *
     *
     * @param coll
     * @return
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * *
     *
     * @param objects
     ** @return
     */
    public static boolean isEmpty(Object[] objects)
    {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * *
     *
     * @param objects
     * @return
     */
    public static boolean isNotEmpty(Object[] objects)
    {
        return !isEmpty(objects);
    }

    /**
     * *
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * *
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * *
     *
     * @param str String
     * @return
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || NULLSTR.equals(str.trim());
    }

    /**
     * *
     *
     * @param str String
     * @return
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * *
     *
     * @param object Object
     * @return
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * *
     *
     * @param object Object
     * @return
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    /**
     * *
     *
     * @param object Object
     * @return
     */
    public static boolean isArray(Object object)
    {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     *
     */
    public static String trim(String str)
    {
        return (str == null ? "" : str.trim());
    }

    /**
     *
     *
     * @param str  Character string
     * @param startInclude
     * @param endExclude
     * @return
     */
    public static String hide(CharSequence str, int startInclude, int endExclude)
    {
        if (isEmpty(str))
        {
            return NULLSTR;
        }
        final int strLength = str.length();
        if (startInclude > strLength)
        {
            return NULLSTR;
        }
        if (endExclude > strLength)
        {
            endExclude = strLength;
        }
        if (startInclude > endExclude)
        {

            return NULLSTR;
        }
        final char[] chars = new char[strLength];
        for (int i = 0; i < strLength; i++)
        {
            if (i >= startInclude && i < endExclude)
            {
                chars[i] = ASTERISK;
            }
            else
            {
                chars[i] = str.charAt(i);
            }
        }
        return new String(chars);
    }

    /**
     *  Character string
     *
     * @param str  Character string
     * @param start
     * @return Result
     */
    public static String substring(final String str, int start)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = str.length() + start;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (start > str.length())
        {
            return NULLSTR;
        }

        return str.substring(start);
    }

    /**
     *  Character string
     *
     * @param str  Character string
     * @param start
     * @param end
     * @return Result
     */
    public static String substring(final String str, int start, int end)
    {
        if (str == null)
        {
            return NULLSTR;
        }

        if (end < 0)
        {
            end = str.length() + end;
        }
        if (start < 0)
        {
            start = str.length() + start;
        }

        if (end > str.length())
        {
            end = str.length();
        }

        if (start > end)
        {
            return NULLSTR;
        }

        if (start < 0)
        {
            start = 0;
        }
        if (end < 0)
        {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     *  Character
     *
     * @param str value
     * @return Result
     */
    public static boolean hasText(String str)
    {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str)
    {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     *
     *
     *
     *
     *
     * @param template
     * @param params
     * @return
     */
    public static String format(String template, Object... params)
    {
        if (isEmpty(params) || isEmpty(template))
        {
            return template;
        }
        return StrFormatter.format(template, params);
    }

    /**
     *
     *
     * @param link
     * @return Result
     */
    public static boolean ishttp(String link)
    {
        return StringUtils.startsWithAny(link, Constants.HTTP, Constants.HTTPS);
    }

    /**
     *
     *
     * @param collection
     * @param array
     * @return boolean Result
     */
    public static boolean containsAny(Collection<String> collection, String... array)
    {
        if (isEmpty(collection) || isEmpty(array))
        {
            return false;
        }
        else
        {
            for (String str : array)
            {
                if (collection.contains(str))
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     *
     */
    public static String toUnderScoreCase(String str)
    {
        if (str == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();

        boolean preCharIsUpperCase = true;

        boolean curreCharIsUpperCase = true;

        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (i > 0)
            {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            }
            else
            {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1))
            {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase)
            {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     *  Character string
     *
     * @param str  Character string
     * @param strs  Character string
     * @return Return true
     */
    public static boolean inStringIgnoreCase(String str, String... strs)
    {
        if (str != null && strs != null)
        {
            for (String s : strs)
            {
                if (str.equalsIgnoreCase(trim(s)))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     *
     * @param name
     * @return
     */
    public static String convertToCamelCase(String name)
    {
        StringBuilder result = new StringBuilder();
        //
        if (name == null || name.isEmpty())
        {
            //
            return "";
        }
        else if (!name.contains("_"))
        {
            //
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        //
        String[] camels = name.split("_");
        for (String camel : camels)
        {
            //
            if (camel.isEmpty())
            {
                continue;
            }
            //
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     *
     *
     */
    public static String toCamelCase(String s)
    {
        if (s == null)
        {
            return null;
        }
        if (s.indexOf(SEPARATOR) == -1)
        {
            return s;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (c == SEPARATOR)
            {
                upperCase = true;
            }
            else if (upperCase)
            {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     *
     *
     * @param str
     * @param strs Character string Array
     * @return
     */
    public static boolean matches(String str, List<String> strs)
    {
        if (isEmpty(str) || isEmpty(strs))
        {
            return false;
        }
        for (String pattern : strs)
        {
            if (isMatch(pattern, str))
            {
                return true;
            }
        }
        return false;
    }

    /**

     *
     * @param pattern
     * @param url
     * @return
     */
    public static boolean isMatch(String pattern, String url)
    {
        AntPathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, url);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    /**
     *
     *
     * @param num Digital Object
     * @param size
     * @return
     */
    public static final String padl(final Number num, final int size)
    {
        return padl(num.toString(), size, '0');
    }

    /**
     *
     *
     * @param s
     * @param size
     * @param c
     * @return
     */
    public static final String padl(final String s, final int size, final char c)
    {
        final StringBuilder sb = new StringBuilder(size);
        if (s != null)
        {
            final int len = s.length();
            if (s.length() <= size)
            {
                for (int i = size - len; i > 0; i--)
                {
                    sb.append(c);
                }
                sb.append(s);
            }
            else
            {
                return s.substring(len - size, len);
            }
        }
        else
        {
            for (int i = size; i > 0; i--)
            {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static final List<String> convertNumbersToWeek(String week){
        String[] dayNames = {
                "Monday",  // index 0
                "Tues",    // index 1
                "Wed",     // index 2
                "Thur",    // index 3
                "Friday",  // index 4
                "Sat",     // index 5
                "Sun"      // index 6
        };
        List<String> weekListOnePass = Arrays.stream(
                        week.substring(1, week.length() - 1).split(",")
                )
                .map(String::trim)
                .map(Integer::parseInt)
                .map(i -> {
                    if (i >= 1 && i <= 7) {
                        return dayNames[i - 1];
                    } else {
                        throw new IllegalArgumentException("Invalid day number: " + i);
                    }
                })
                .collect(Collectors.toList());
        return weekListOnePass;
    }

    /**
     * time  Conversion
     * @param time
     * @return
     */
    public static String formatTime(LocalTime time) {
        LocalTime noon = LocalTime.NOON;

        if (time.isBefore(noon)) {

            DateTimeFormatter amFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
            return time.format(amFormatter);
        } else {

            DateTimeFormatter pmFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            return time.format(pmFormatter) + " PM";
        }
    }

    /**
     *
     * @param dayOfWeek
     * @return
     */
    public static String getChineseWeek(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "1";
            case TUESDAY: return "2";
            case WEDNESDAY: return "3";
            case THURSDAY: return "4";
            case FRIDAY: return "5";
            case SATURDAY: return "6";
            case SUNDAY: return "7";
            default: return "";
        }
    }
}
