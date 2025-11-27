package com.ys.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ys.common.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JSON Utility Class
 * Handles conversion between objects and JSON strings
 *
 * @author ys
 * @date 2025-11-27
 */
public class JsonUtils {

    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Singleton ObjectMapper to avoid repeated creation
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Convert object to JSON string (pretty print)
     *
     * @param obj Object to convert
     * @return JSON string
     * @throws ServiceException Throws business exception when conversion fails
     */
    public static String toJsonPretty(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON: {}", obj.getClass().getName(), e);
            throw new ServiceException("There is an issue with the format of the uploaded attachment");
        }
    }

    /**
     * Convert object to JSON string (compact)
     *
     * @param obj Object to convert
     * @return JSON string
     * @throws ServiceException Throws business exception when conversion fails
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON: {}", obj.getClass().getName(), e);
            throw new ServiceException("Data formatting failed");
        }
    }

    /**
     * Convert JSON string to object
     *
     * @param json JSON string
     * @param clazz Target class type
     * @param <T> Generic type
     * @return Converted object
     * @throws ServiceException Throws business exception when conversion fails
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }

        try {
            return MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert JSON to object: {}", clazz.getName(), e);
            throw new ServiceException("Data parsing failed");
        }
    }
}
