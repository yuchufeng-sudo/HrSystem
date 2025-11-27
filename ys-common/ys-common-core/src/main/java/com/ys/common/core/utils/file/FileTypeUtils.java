package com.ys.common.core.utils.file;

import java.io.File;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * File Type Utility Class
 * Provides methods for determining file types and extensions
 *
 * @author ruoyi
 */
public class FileTypeUtils
{
    /**
     * Get file type from File object
     * <p>
     * Example: ruoyi.txt returns: txt
     *
     * @param file File object
     * @return File extension (without ".")
     */
    public static String getFileType(File file)
    {
        if (null == file)
        {
            return StringUtils.EMPTY;
        }
        return getFileType(file.getName());
    }

    /**
     * Get file type from filename
     * <p>
     * Example: ruoyi.txt returns: txt
     *
     * @param fileName Filename
     * @return File extension (without ".")
     */
    public static String getFileType(String fileName)
    {
        int separatorIndex = fileName.lastIndexOf(".");
        if (separatorIndex < 0)
        {
            return "";
        }
        return fileName.substring(separatorIndex + 1).toLowerCase();
    }

    /**
     * Get file extension from MultipartFile
     *
     * @param file Form file (MultipartFile)
     * @return File extension
     */
    public static final String getExtension(MultipartFile file)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }

    /**
     * Get file type from byte array by analyzing file header
     *
     * @param photoByte File byte array
     * @return File extension (without ".")
     */
    public static String getFileExtendName(byte[] photoByte)
    {
        String strFileExtendName = "JPG";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97))
        {
            strFileExtendName = "GIF";
        }
        else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70))
        {
            strFileExtendName = "JPG";
        }
        else if ((photoByte[0] == 66) && (photoByte[1] == 77))
        {
            strFileExtendName = "BMP";
        }
        else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71))
        {
            strFileExtendName = "PNG";
        }
        return strFileExtendName;
    }
}
