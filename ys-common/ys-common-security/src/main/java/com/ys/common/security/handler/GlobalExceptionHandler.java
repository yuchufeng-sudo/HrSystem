package com.ys.common.security.handler;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.ys.common.core.constant.HttpStatus;
import com.ys.common.core.exception.DemoModeException;
import com.ys.common.core.exception.InnerAuthException;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.exception.auth.NotPermissionException;
import com.ys.common.core.exception.auth.NotRoleException;
import com.ys.common.core.text.Convert;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.web.domain.AjaxResult;

/**
 * Global Exception Handler
 * Centralized exception handling for the application
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Permission code exception handler
     */
    @ExceptionHandler(NotPermissionException.class)
    public AjaxResult handleNotPermissionException(NotPermissionException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("Request URI '{}', permission code validation failed '{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "No access permission, please contact administrator for authorization");
    }

    /**
     * Role permission exception handler
     */
    @ExceptionHandler(NotRoleException.class)
    public AjaxResult handleNotRoleException(NotRoleException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("Request URI '{}', role permission validation failed '{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "No access permission, please contact administrator for authorization");
    }

    /**
     * HTTP request method not supported exception handler
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("Request URI '{}', does not support '{}' request method", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * Business exception handler
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * Missing required path variable in request path exception handler
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("Missing required path variable in request path '{}', system exception occurred.", requestURI, e);
        return AjaxResult.error(String.format("Missing required path variable [%s] in request path", e.getVariableName()));
    }

    /**
     * Request parameter type mismatch exception handler
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
//        if (StringUtils.isNotEmpty(value))
//        {
//            value = EscapeUtil.clean(value);
//        }
        log.error("Request parameter type mismatch '{}', system exception occurred.", requestURI, e);
        return AjaxResult.error(String.format("Request parameter type mismatch, parameter [%s] requires type: '%s', but input value is: '%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     * Intercept unknown runtime exceptions
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("Request URI '{}', unknown exception occurred.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * System exception handler
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("Request URI '{}', system exception occurred.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * Custom validation exception handler (BindException)
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * Custom validation exception handler (MethodArgumentNotValidException)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * Internal authentication exception handler
     */
    @ExceptionHandler(InnerAuthException.class)
    public AjaxResult handleInnerAuthException(InnerAuthException e)
    {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * Demo mode exception handler
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("Demo mode, operation not allowed");
    }
}
