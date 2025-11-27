package com.ys.common.core.utils.file;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import com.ys.common.core.utils.StringUtils;

/**
 * File processing utility class
 *
 * @author ruoyi
 */
public class FileUtils
{
    /** Character constant: slash {@code '/'} */
    public static final char SLASH = '/';

    /** Character constant: backslash {@code '\\'} */
    public static final char BACKSLASH = '\\';

    /** Filename validation regex pattern: allows letters, numbers, underscores, hyphens, pipes, dots and Chinese characters */
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * Write the byte array of the specified file to the output stream
     *
     * @param filePath Path of the file to read
     * @param os Output stream to write the file bytes
     * @throws IOException If an I/O error occurs (e.g., file not found, stream operation failed)
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Delete a file
     *
     * @param filePath Path of the file to delete
     * @return true if the file is successfully deleted; false otherwise
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // Delete if the path points to an existing file
        if (file.isFile() && file.exists())
        {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * Validate if the filename is legal
     *
     * @param filename Filename to validate
     * @return true if the filename matches the allowed pattern; false otherwise
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * Check if the file is allowed to be downloaded
     *
     * @param resource Path of the file to download
     * @return true if the file is allowed to download; false otherwise
     * @note Prohibits directory traversal (contains "..") and checks if the file extension is in the allowed list
     */
    public static boolean checkAllowDownload(String resource)
    {
        // Prohibit directory traversal
        if (StringUtils.contains(resource, ".."))
        {
            return false;
        }
        // Check if the file extension is in the allowed list
        return ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource));
    }

    /**
     * Re-encode the download filename for different browsers
     *
     * @param request HttpServletRequest object
     * @param fileName Original filename
     * @return Encoded filename suitable for the requesting browser
     * @throws UnsupportedEncodingException If the required encoding (utf-8, ISO8859-1) is not supported
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE browser encoding
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // Firefox browser encoding
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // Chrome browser encoding
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // Other browsers encoding
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * Extract the filename from the file path
     *
     * @param filePath Full path of the file
     * @return Extracted filename; null if the input path is null
     * @note Handles paths ending with a file separator by removing the trailing separator first,
     * then finds the last file separator ('/' or '\') to extract the filename
     */
    public static String getName(String filePath)
    {
        if (null == filePath)
        {
            return null;
        }
        int len = filePath.length();
        if (0 == len)
        {
            return filePath;
        }
        if (isFileSeparator(filePath.charAt(len - 1)))
        {
            // Remove trailing separator if the path ends with it
            len--;
        }

        int begin = 0;
        char c;
        for (int i = len - 1; i > -1; i--)
        {
            c = filePath.charAt(i);
            if (isFileSeparator(c))
            {
                // Find the last file separator ('/' or '\')
                begin = i + 1;
                break;
            }
        }

        return filePath.substring(begin, len);
    }

    /**
     * Check if the character is a Windows or Linux (Unix) file separator
     *
     * @param c Character to check
     * @return true if the character is '/' (Linux/Unix) or '\' (Windows); false otherwise
     */
    public static boolean isFileSeparator(char c)
    {
        return SLASH == c || BACKSLASH == c;
    }

    /**
     * Set HTTP response headers for file attachment download
     *
     * @param response HttpServletResponse object
     * @param realFileName Real filename of the attachment
     * @throws UnsupportedEncodingException If UTF-8 encoding is not supported
     * @note Sets "Content-disposition" and "download-filename" headers with properly encoded filename
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException
    {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * Percent-encode a string for HTTP headers (complies with RFC 3986)
     *
     * @param s String to be percent-encoded
     * @return Percent-encoded string
     * @throws UnsupportedEncodingException If UTF-8 encoding is not supported
     * @note Uses UTF-8 encoding and replaces '+' with '%20' to meet HTTP header standards
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException
    {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }
}
