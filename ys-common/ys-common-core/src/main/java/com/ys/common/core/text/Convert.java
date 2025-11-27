package com.ys.common.core.text;

import com.ys.common.core.utils.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Set;

/**
 *
 *
 * @author ys
 */
public class Convert
{
    /**
     *  Conversion is a character string.</p>
     * If the given value is null or a conversion failure occurs, return the default value<br>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static String toStr(Object value, String defaultValue)
    {
        if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof String)
        {
            return (String) value;
        }
        return value.toString();
    }

    /**
     *  Conversion is a character string.</p>
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static String toStr(Object value)
    {
        return toStr(value, null);
    }

    /**
     *
     * If the given value is null or a conversion failure occurs, return the default value<br>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Character toChar(Object value, Character defaultValue)
    {
        if (null == value)
        {
            return defaultValue;
        }
        if (value instanceof Character)
        {
            return (Character) value;
        }

        final String valueStr = toStr(value, null);
        return StringUtils.isEmpty(valueStr) ? defaultValue : valueStr.charAt(0);
    }

    /**
     *
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Character toChar(Object value)
    {
        return toChar(value, null);
    }

    /**
     *  Conversion is a byte.</p>
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Byte toByte(Object value, Byte defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Byte)
        {
            return (Byte) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).byteValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Byte.parseByte(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *  Conversion is a byte.</p>
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Byte toByte(Object value)
    {
        return toByte(value, null);
    }

    /**
     *  Conversion results in a Short value.</p>
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Short toShort(Object value, Short defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Short)
        {
            return (Short) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).shortValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Short.parseShort(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *  Conversion results in a Short value.</p>
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Short toShort(Object value)
    {
        return toShort(value, null);
    }

    /**
     *  Conversion is a Number.</p>
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Number toNumber(Object value, Number defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Number)
        {
            return (Number) value;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return NumberFormat.getInstance().parse(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *  Conversion is a Number.</p>
     * If the given value is empty or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Number toNumber(Object value)
    {
        return toNumber(value, null);
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Integer toInt(Object value, Integer defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Integer)
        {
            return (Integer) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).intValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Integer.parseInt(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Integer toInt(Object value)
    {
        return toInt(value, null);
    }

    /**
     *
     *
     * @param str The value being converted
     * @return Result
     */
    public static Integer[] toIntArray(String str)
    {
        return toIntArray(",", str);
    }

    /**
     *
     *
     * @param str The value being converted
     * @return Result
     */
    public static Long[] toLongArray(String str)
    {
        return toLongArray(",", str);
    }

    /**
     *
     *
     * @param split Delimiter
     * @param str The value being converted
     * @return Result
     */
    public static Integer[] toIntArray(String split, String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new Integer[] {};
        }
        String[] arr = str.split(split);
        final Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++)
        {
            final Integer v = toInt(arr[i], 0);
            ints[i] = v;
        }
        return ints;
    }

    /**
     *
     *
     * @param split Delimiter
     * @param str The value being converted
     * @return Result
     */
    public static Long[] toLongArray(String split, String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new Long[] {};
        }
        String[] arr = str.split(split);
        final Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; i++)
        {
            final Long v = toLong(arr[i], null);
            longs[i] = v;
        }
        return longs;
    }

    /**
     *
     *
     * @param str The value being converted
     * @return Result
     */
    public static String[] toStrArray(String str)
    {
        if (StringUtils.isEmpty(str))
        {
            return new String[] {};
        }
        return toStrArray(",", str);
    }

    /**
     *
     *
     * @param split Delimiter
     * @param str The value being converted
     * @return Result
     */
    public static String[] toStrArray(String split, String str)
    {
        return str.split(split);
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Long toLong(Object value, Long defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Long)
        {
            return (Long) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).longValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {

            return new BigDecimal(valueStr.trim()).longValue();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *
     * If the given value is <code>null</code> or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Long toLong(Object value)
    {
        return toLong(value, null);
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Double toDouble(Object value, Double defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Double)
        {
            return (Double) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).doubleValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {

            return new BigDecimal(valueStr.trim()).doubleValue();
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *
     * If the given value is empty or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Double toDouble(Object value)
    {
        return toDouble(value, null);
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Float toFloat(Object value, Float defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Float)
        {
            return (Float) value;
        }
        if (value instanceof Number)
        {
            return ((Number) value).floatValue();
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Float.parseFloat(valueStr.trim());
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *
     * If the given value is empty or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Float toFloat(Object value)
    {
        return toFloat(value, null);
    }

    /**
     *
     *
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static Boolean toBool(Object value, Boolean defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof Boolean)
        {
            return (Boolean) value;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        valueStr = valueStr.trim().toLowerCase();
        switch (valueStr)
        {
            case "true":
            case "yes":
            case "ok":
            case "1":
                return true;
            case "false":
            case "no":
            case "0":
                return false;
            default:
                return defaultValue;
        }
    }

    /**
     *
     * If the given value is empty or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static Boolean toBool(Object value)
    {
        return toBool(value, null);
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *
     * @param clazz
     * @param value
     * @param defaultValue
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (clazz.isAssignableFrom(value.getClass()))
        {
            @SuppressWarnings("unchecked")
            E myE = (E) value;
            return myE;
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return Enum.valueOf(clazz, valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *
     * If the given value is empty or a conversion failure occurs, return the default value <code>null</code></p>
     *
     * @param clazz
     * @param value
     * @return Enum
     */
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value)
    {
        return toEnum(clazz, value, null);
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static BigInteger toBigInteger(Object value, BigInteger defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof BigInteger)
        {
            return (BigInteger) value;
        }
        if (value instanceof Long)
        {
            return BigInteger.valueOf((Long) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigInteger(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *
     * If the given value is empty or a conversion failure occurs, return the default value <code>null</code></p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static BigInteger toBigInteger(Object value)
    {
        return toBigInteger(value, null);
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @param defaultValue  Default value when conversion fails
     * @return Result
     */
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue)
    {
        if (value == null)
        {
            return defaultValue;
        }
        if (value instanceof BigDecimal)
        {
            return (BigDecimal) value;
        }
        if (value instanceof Long)
        {
            return new BigDecimal((Long) value);
        }
        if (value instanceof Double)
        {
            return BigDecimal.valueOf((Double) value);
        }
        if (value instanceof Integer)
        {
            return new BigDecimal((Integer) value);
        }
        final String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr))
        {
            return defaultValue;
        }
        try
        {
            return new BigDecimal(valueStr);
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    /**
     *
     * If the given value is empty or conversion fails, return the default value.</p>
     *  Conversion failure will not result in an error.
     *
     * @param value The value being converted
     * @return Result
     */
    public static BigDecimal toBigDecimal(Object value)
    {
        return toBigDecimal(value, null);
    }

    /**
     *
     *
     *
     * @param obj Object
     * @return Character string
     */
    public static String utf8Str(Object obj)
    {
        return str(obj, CharsetKit.CHARSET_UTF_8);
    }

    /**
     *
     *
     *
     * @param obj Object
     * @param charsetName  Character set
     * @return Character string
     */
    public static String str(Object obj, String charsetName)
    {
        return str(obj, Charset.forName(charsetName));
    }

    /**
     *
     *
     *
     * @param obj Object
     * @param charset  Character set
     * @return Character string
     */
    public static String str(Object obj, Charset charset)
    {
        if (null == obj)
        {
            return null;
        }

        if (obj instanceof String)
        {
            return (String) obj;
        }
        else if (obj instanceof byte[] || obj instanceof Byte[])
        {
            if (obj instanceof byte[])
            {
                return str((byte[]) obj, charset);
            }
            else
            {
                Byte[] bytes = (Byte[]) obj;
                int length = bytes.length;
                byte[] dest = new byte[length];
                for (int i = 0; i < length; i++)
                {
                    dest[i] = bytes[i];
                }
                return str(dest, charset);
            }
        }
        else if (obj instanceof ByteBuffer)
        {
            return str((ByteBuffer) obj, charset);
        }
        return obj.toString();
    }

    /**
     * Convert the byte array into a character string.
     *
     * @param bytes byte Array
     * @param charset  Character set
     * @return Character string
     */
    public static String str(byte[] bytes, String charset)
    {
        return str(bytes, StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }

    /**
     * Decode bytecode.
     *
     * @param data  Character string
     * @param charset  Character set. If this field is empty, the decoding result depends on the platform.
     * @return The decoded character string.
     */
    public static String str(byte[] data, Charset charset)
    {
        if (data == null)
        {
            return null;
        }

        if (null == charset)
        {
            return new String(data);
        }
        return new String(data, charset);
    }

    /**
     *
     *
     * @param data
     * @param charset
     * @return Character string
     */
    public static String str(ByteBuffer data, String charset)
    {
        if (data == null)
        {
            return null;
        }

        return str(data, Charset.forName(charset));
    }

    /**
     *
     *
     * @param data
     * @param charset
     * @return Character string
     */
    public static String str(ByteBuffer data, Charset charset)
    {
        if (null == charset)
        {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }

    // ----------------------------------------------------------------------- full-width and half-width Conversion
    /**
     *Convert half-width to full-width
     *
     * @param input String.
     * @return Full-width character string.
     */
    public static String toSBC(String input)
    {
        return toSBC(input, null);
    }

    /**
     *Convert half-width to full-width
     *
     * @param input String
     * @param notConvertSet Character Set that is not replaced
     * @return Full-width character string.
     */
    public static String toSBC(String input, Set<Character> notConvertSet)
    {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (null != notConvertSet && notConvertSet.contains(c[i]))
            {
                // Skip characters that are not to be replaced.
                continue;
            }

            if (c[i] == ' ')
            {
                c[i] = '\u3000';
            }
            else if (c[i] < '\177')
            {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * Convert full-width to half-width
     *
     * @param input String.
     * @return Half-width character string
     */
    public static String toDBC(String input)
    {
        return toDBC(input, null);
    }

    /**
     * Replace full-width characters with half-width ones.
     *
     * @param text
     * @param notConvertSet Character Set that is not replaced
     * @return
     */
    public static String toDBC(String text, Set<Character> notConvertSet)
    {
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; i++)
        {
            if (null != notConvertSet && notConvertSet.contains(c[i]))
            {
                // Skip characters that are not to be replaced.
                continue;
            }

            if (c[i] == '\u3000')
            {
                c[i] = ' ';
            }
            else if (c[i] > '\uFF00' && c[i] < '\uFF5F')
            {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     *
     *
     * @param n Digital
     * @return
     */
   public static String digitUppercase(double n)
    {
        String[] fraction = { "Jiao", "Fen" };
        String[] digit = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine" };
        String[][] unit = { { "Yuan", "Ten Thousand", "Hundred Million" }, { "", "Ten", "Hundred", "Thousand" } };

        String head = n < 0 ? "Negative" : "";
        n = Math.abs(n);

        String s = "";
        for (int i = 0; i < fraction.length; i++)
        {
            //
            BigDecimal nNum = BigDecimal.valueOf(n);
            BigDecimal decimal = new BigDecimal(10);
            BigDecimal scale = nNum.multiply(decimal).setScale(2, RoundingMode.HALF_EVEN);
            double d = scale.doubleValue();
            s += (digit[(int) (Math.floor(d * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(Zero.)+", "");
        }
        if (s.length() < 1)
        {
            s = "Whole";
        }
        int integerPart = (int) Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++)
        {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++)
            {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(Zero.)*Zero$", "").replaceAll("^$", "Zero") + unit[0][i] + s;
        }
        return head + s.replaceAll("(Zero.)*Zero Yuan", "Yuan").replaceFirst("(Zero.)+", "").replaceAll("(Zero.)+", "Zero").replaceAll("^Whole$", "Zero Yuan Whole");
    }
}
