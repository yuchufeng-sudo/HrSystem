package com.ys.common.core.utils.html;

import com.ys.common.core.utils.StringUtils;

/**
 * HTML escape and unescape utility class
 *
 * Provides methods for escaping HTML special characters, unescaping escaped characters,
 * and removing HTML tags while preserving content.
 *
 * @author ruoyi
 */
public class EscapeUtil
{
    /** Regular expression pattern to match HTML tags (opening, closing, self-closing) */
    public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";

    /**
     * Character mapping array for HTML special character escaping
     * Index represents the original character (ASCII), value is the escaped char array
     */
    private static final char[][] TEXT = new char[64][];

    static
    {
        // Initialize default mapping (original character as is)
        for (int i = 0; i < 64; i++)
        {
            TEXT[i] = new char[] { (char) i };
        }

        // Map HTML special characters to their numeric character references
        TEXT['\''] = "&#039;".toCharArray(); // Apostrophe
        TEXT['"'] = "&#34;".toCharArray();  // Double quote
        TEXT['&'] = "&#38;".toCharArray();  // Ampersand
        TEXT['<'] = "&#60;".toCharArray();  // Less than sign
        TEXT['>'] = "&#62;".toCharArray();  // Greater than sign
    }

    /**
     * Escape HTML-special characters in text to safe numeric character references
     *
     * @param text The text to be escaped
     * @return Escaped text with HTML special characters replaced
     */
    public static String escape(String text)
    {
        return encode(text);
    }

    /**
     * Restore escaped HTML special characters to their original form
     *
     * @param content The content containing escaped HTML characters
     * @return Restored string with original HTML special characters
     */
    public static String unescape(String content)
    {
        return decode(content);
    }

    /**
     * Remove all HTML tags from content while preserving the text inside the tags
     *
     * @param content The text containing HTML tags
     * @return Clean text without any HTML tags
     */
    public static String clean(String content)
    {
        return new HTMLFilter().filter(content);
    }

    /**
     * Perform escape encoding on the input text
     *
     * @param text The text to be encoded
     * @return Encoded string with special characters converted to percent-encoded format
     */
    private static String encode(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return StringUtils.EMPTY;
        }

        final StringBuilder tmp = new StringBuilder(text.length() * 6);
        char c;
        for (int i = 0; i < text.length(); i++)
        {
            c = text.charAt(i);
            if (c < 256)
            {
                tmp.append("%");
                if (c < 16)
                {
                    tmp.append("0");
                }
                // Encode ASCII characters to hexadecimal format
                tmp.append(Integer.toString(c, 16));
            }
            else
            {
                tmp.append("%u");
                if (c <= 0xfff)
                {
                    // Fix for issue#I49JU8@Gitee: add leading zero for characters <= 0xFFF
                    tmp.append("0");
                }
                // Encode non-ASCII characters to Unicode hexadecimal format
                tmp.append(Integer.toString(c, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * Perform escape decoding on the input content
     *
     * @param content The content to be decoded (contains percent-encoded characters)
     * @return Decoded string with percent-encoded characters restored to original
     */
    public static String decode(String content)
    {
        if (StringUtils.isEmpty(content))
        {
            return content;
        }

        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < content.length())
        {
            // Find the next percent sign
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos)
            {
                // Handle Unicode encoding (%uXXXX)
                if (content.charAt(pos + 1) == 'u')
                {
                    ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                }
                // Handle ASCII encoding (%XX)
                else
                {
                    ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            }
            else
            {
                // Append content between last position and current percent sign
                if (pos == -1)
                {
                    tmp.append(content.substring(lastPos));
                    lastPos = content.length();
                }
                else
                {
                    tmp.append(content.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    /**
     * Test method for escape, unescape and clean functionalities
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args)
    {
        String html = "<script>alert(1);</script>";
        String escape = EscapeUtil.escape(html);

        // Test cases can be uncommented for additional verification:
        // String html = "<scr<script>ipt>alert(\"XSS\")</scr<script>ipt>";
        // String html = "<123";
        // String html = "123>";

        System.out.println("clean: " + EscapeUtil.clean(html));
        System.out.println("escape: " + escape);
        System.out.println("unescape: " + EscapeUtil.unescape(escape));
    }
}
