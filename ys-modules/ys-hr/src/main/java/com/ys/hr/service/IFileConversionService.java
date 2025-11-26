package com.ys.hr.service;

import java.io.File;
import java.util.List;

/**
 * File Conversion Service Interface
 *
 * Provides file conversion and download operations for handling
 * various file sources including local paths and remote URLs.
 *
 * @author ys
 * @date 2025-11-26
 */
public interface IFileConversionService {

    /**
     * Convert a list of file paths to File objects
     *
     * This method handles both local file paths and remote URLs.
     * For local paths, it creates File objects directly.
     * For URLs (http/https), it downloads the files to temporary locations.
     *
     * @param filePaths List of file paths or URLs to convert
     * @return List of File objects (null entries are filtered out)
     */
    List<File> convertToFileList(List<String> filePaths);

    /**
     * Convert a single file path to a File object
     *
     * @param filePath File path or URL to convert
     * @return File object, or null if conversion fails
     */
    File convertToFile(String filePath);
}
