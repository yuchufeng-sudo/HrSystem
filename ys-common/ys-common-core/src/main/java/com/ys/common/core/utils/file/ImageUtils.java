package com.ys.common.core.utils.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Image processing utility class
 *
 * @author ruoyi
 */
public class ImageUtils
{
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * Get image byte array by image path
     *
     * @param imagePath Path of the image (supports network URL)
     * @return Byte array of the image; null if loading fails or exception occurs
     */
    public static byte[] getImage(String imagePath)
    {
        InputStream is = getFile(imagePath);
        try
        {
            return IOUtils.toByteArray(is);
        }
        catch (Exception e)
        {
            log.error("Image loading exception: {}", e);
            return null;
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Get input stream of the image by image path
     *
     * @param imagePath Path of the image (supports network URL)
     * @return ByteArrayInputStream of the image; null if acquisition fails or exception occurs
     * @note Creates a copy of the byte array to prevent external modification of the original data
     */
    public static InputStream getFile(String imagePath)
    {
        try
        {
            byte[] result = readFile(imagePath);
            if (result != null)
            {
                // Copy byte array to avoid external modification of the original data
                result = Arrays.copyOf(result, result.length);
            }
            return new ByteArrayInputStream(result);
        }
        catch (Exception e)
        {
            log.error("Image acquisition exception: {}", e);
        }
        return null;
    }

    /**
     * Read file (supports network resources) into byte array
     *
     * @param url String representation of the resource URL (network address)
     * @return Byte array of the file; null if access fails or exception occurs
     * @note Sets connection timeout to 30 seconds and read timeout to 60 seconds
     */
    public static byte[] readFile(String url)
    {
        InputStream in = null;
        try
        {
            URL urlObj = new URL(url);
            URLConnection urlConnection = urlObj.openConnection();
            // Set connection timeout: 30 seconds
            urlConnection.setConnectTimeout(30 * 1000);
            // Set read timeout: 60 seconds
            urlConnection.setReadTimeout(60 * 1000);
            // Enable input stream for reading
            urlConnection.setDoInput(true);
            in = urlConnection.getInputStream();
            return IOUtils.toByteArray(in);
        }
        catch (Exception e)
        {
            log.error("File access exception: {}", e);
            return null;
        }
        finally
        {
            // Quietly close the input stream (no exception thrown if stream is null)
            IOUtils.closeQuietly(in);
        }
    }
}
