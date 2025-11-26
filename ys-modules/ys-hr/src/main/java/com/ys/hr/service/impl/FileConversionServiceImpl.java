package com.ys.hr.service.impl;

import com.ys.hr.service.IFileConversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * File Conversion Service Implementation
 *
 * Handles conversion of file paths and URLs to File objects.
 * Supports both local file system paths and remote HTTP/HTTPS URLs.
 * Downloads remote files to temporary locations for processing.
 *
 * @author ys
 * @date 2025-11-26
 */
@Service
public class FileConversionServiceImpl implements IFileConversionService {

    private static final Logger log = LoggerFactory.getLogger(FileConversionServiceImpl.class);

    // HTTP/HTTPS URL prefixes
    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    // Buffer size for file download (8KB)
    private static final int BUFFER_SIZE = 8192;

    // HTTP success response code
    private static final int HTTP_OK = 200;

    /**
     * Convert a list of file paths to File objects
     *
     * Processes each path independently and filters out any null results
     * (failed conversions). Logs warnings for failed conversions but continues
     * processing remaining paths.
     *
     * @param filePaths List of file paths or URLs to convert
     * @return List of successfully converted File objects
     */
    @Override
    public List<File> convertToFileList(List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            log.debug("No file paths provided for conversion");
            return new ArrayList<>();
        }

        log.info("Starting conversion of {} file path(s)", filePaths.size());

        List<File> files = filePaths.stream()
                .map(this::convertToFile)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("Successfully converted {}/{} file path(s)", files.size(), filePaths.size());

        return files;
    }

    /**
     * Convert a single file path to a File object
     *
     * Determines if the path is a URL or local path and handles accordingly.
     * For URLs: downloads the file to a temporary location
     * For local paths: creates a File object and verifies existence
     *
     * @param filePath File path or URL to convert
     * @return File object, or null if conversion fails
     */
    @Override
    public File convertToFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            log.warn("Empty or null file path provided");
            return null;
        }

        try {
            File file;

            if (isUrl(filePath)) {
                log.debug("Detected URL path: {}", filePath);
                file = downloadFileFromUrl(filePath);
            } else {
                log.debug("Detected local path: {}", filePath);
                file = new File(filePath);
            }

            // Verify file exists and is accessible
            if (file != null && file.exists()) {
                log.debug("File successfully converted: {}", file.getAbsolutePath());
                return file;
            } else {
                log.warn("File does not exist: {}", filePath);
                return null;
            }

        } catch (Exception e) {
            log.error("Failed to convert file path: {}. Error: {}", filePath, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Check if a path is a URL (http or https)
     *
     * @param path The path to check
     * @return true if the path starts with http:// or https://
     */
    private boolean isUrl(String path) {
        return path.startsWith(HTTP_PREFIX) || path.startsWith(HTTPS_PREFIX);
    }

    /**
     * Download a file from a URL to a temporary location
     *
     * Creates a temporary file and streams the remote content into it.
     * The temporary file will be automatically deleted when the JVM exits.
     *
     * @param urlPath The URL path to download from
     * @return File object pointing to the downloaded temporary file
     * @throws IOException if download fails or connection cannot be established
     */
    private File downloadFileFromUrl(String urlPath) throws IOException {
        log.info("Starting file download from URL: {}", urlPath);

        // Encode the URL to handle special characters
        String encodedUrl = encodeUrl(urlPath);
        URL url = new URL(encodedUrl);

        // Open connection and configure
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(30000); // 30 seconds timeout
        conn.setReadTimeout(60000);    // 60 seconds read timeout
        conn.connect();

        // Check response code
        int responseCode = conn.getResponseCode();
        if (responseCode != HTTP_OK) {
            String errorMessage = String.format(
                    "Failed to download file from URL: %s. HTTP response code: %d",
                    urlPath, responseCode
            );
            log.error(errorMessage);
            throw new IOException(errorMessage);
        }

        // Extract filename from URL path
        String fileName = extractFileName(url.getPath());

        // Create temporary file with unique name
        File tempFile = File.createTempFile("download_", "_" + fileName);
        tempFile.deleteOnExit(); // Auto-delete when JVM exits

        log.debug("Created temporary file: {}", tempFile.getAbsolutePath());

        // Download file content
        try (InputStream in = conn.getInputStream();
             FileOutputStream out = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            long totalBytesRead = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }

            log.info("Successfully downloaded {} bytes from URL: {}", totalBytesRead, urlPath);
        }

        return tempFile;
    }

    /**
     * Encode URL to handle special characters (e.g., Chinese characters, spaces)
     *
     * Only encodes the filename portion of the URL to avoid breaking the
     * protocol and domain parts.
     *
     * @param urlPath The URL path to encode
     * @return Encoded URL path
     * @throws UnsupportedEncodingException if UTF-8 encoding is not supported
     */
    private String encodeUrl(String urlPath) throws UnsupportedEncodingException {
        // Find the last slash to separate prefix from filename
        int lastSlash = urlPath.lastIndexOf("/");

        if (lastSlash == -1) {
            // No slash found, encode the entire path
            return URLEncoder.encode(urlPath, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");
        }

        // Split into prefix (protocol + domain + path) and filename
        String prefix = urlPath.substring(0, lastSlash + 1);
        String fileName = urlPath.substring(lastSlash + 1);

        // Only encode the filename to handle special characters
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                // Replace + with %20 for spaces
                .replace("+", "%20");

        return prefix + encodedFileName;
    }

    /**
     * Extract filename from URL path
     *
     * @param urlPath The URL path
     * @return The filename, or "unknown" if extraction fails
     */
    private String extractFileName(String urlPath) {
        if (urlPath == null || urlPath.isEmpty()) {
            return "unknown";
        }

        int lastSlash = urlPath.lastIndexOf("/");
        if (lastSlash != -1 && lastSlash < urlPath.length() - 1) {
            return urlPath.substring(lastSlash + 1);
        }

        return "unknown";
    }
}
