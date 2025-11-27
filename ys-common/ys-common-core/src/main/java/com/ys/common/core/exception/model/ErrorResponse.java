package com.ys.common.core.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Unified error response model
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Error code
     */
    private String errorCode;

    /**
     * Error message
     */
    private String message;

    /**
     * Detailed error information (visible in dev environment)
     */
    private String details;

    /**
     * Field validation error list
     */
    private List<FieldError> fieldErrors;

    /**
     * Timestamp
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Request path
     */
    private String path;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
    }
}
