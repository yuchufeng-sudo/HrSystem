package com.ys.common.core.exception.handler;

import com.ys.common.core.web.domain.AjaxResult;
import com.ys.common.core.exception.*;
import com.ys.common.core.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler
 * Handles all exceptions uniformly and returns user-friendly error messages
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle resource not found exception
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public AjaxResult handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        log.warn("Resource not found: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return AjaxResult.error(HttpStatus.NOT_FOUND.value(), errorResponse.getMessage());
    }

    /**
     * Handle validation exception
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {

        log.warn("Validation error: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return AjaxResult.error(HttpStatus.BAD_REQUEST.value(), errorResponse.getMessage());
    }

    /**
     * Handle external service exception
     */
    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public AjaxResult handleExternalServiceException(
            ExternalServiceException ex,
            HttpServletRequest request) {

        log.error("External service error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message("External service temporarily unavailable. Please try again later.")
                .details(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return AjaxResult.error(HttpStatus.SERVICE_UNAVAILABLE.value(),
                errorResponse.getMessage());
    }

    /**
     * Handle data access exception
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleDataAccessException(
            DataAccessException ex,
            HttpServletRequest request) {

        log.error("Data access error: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode(ex.getErrorCode())
                .message("Database operation failed. Please contact administrator.")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return AjaxResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                errorResponse.getMessage());
    }

    /**
     * Handle Bean Validation exceptions (triggered by @Valid annotation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        log.warn("Validation failed: {} errors", fieldErrors.size());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("VALIDATION_ERROR")
                .message("Input validation failed")
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return AjaxResult.error(HttpStatus.BAD_REQUEST.value(),errorResponse.getMessage());
    }

    /**
     * Handle parameter binding exception
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handleBindException(
            BindException ex) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getFieldErrors()
                .stream()
                .map(error -> new ErrorResponse.FieldError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .collect(Collectors.toList());

        log.warn("Parameter binding failed: {} errors", fieldErrors.size());

        return AjaxResult.error(HttpStatus.BAD_REQUEST.value(),
                "Parameter binding failed");
    }

    /**
     * Handle constraint violation exception (triggered by @Validated annotation)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AjaxResult handleConstraintViolationException(
            ConstraintViolationException ex) {

        List<ErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new ErrorResponse.FieldError(
                        violation.getPropertyPath().toString(),
                        violation.getMessage(),
                        violation.getInvalidValue()
                ))
                .collect(Collectors.toList());

        log.warn("Constraint violation: {} errors", fieldErrors.size());

        return AjaxResult.error(HttpStatus.BAD_REQUEST.value(),
                "Constraint violation");
    }

    /**
     * Handle all uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult handleException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred. Please contact administrator.")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return AjaxResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                errorResponse.getMessage());
    }
}
