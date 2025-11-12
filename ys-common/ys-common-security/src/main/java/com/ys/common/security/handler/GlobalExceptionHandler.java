package com.ys.common.security.handler;

import com.ys.common.core.constant.HttpStatus;
import com.ys.common.core.exception.DemoModeException;
import com.ys.common.core.exception.InnerAuthException;
import com.ys.common.core.exception.ServiceException;
import com.ys.common.core.exception.auth.NotPermissionException;
import com.ys.common.core.exception.auth.NotRoleException;
import com.ys.common.core.text.Convert;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.web.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * Global Exception
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     *
     */
    @ExceptionHandler(NotPermissionException.class)
    public AjaxResult handleNotPermissionException(NotPermissionException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(" RequestAddress'{}', VerifyFailure'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "No access permission. Please contact the management staff for authorization");
    }

    /**
     * ROLEPermission
     */
    @ExceptionHandler(NotRoleException.class)
    public AjaxResult handleNotRoleException(NotRoleException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(" RequestAddress'{}',ROLEPermission VerifyFailure'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "No access permission. Please contact the management staff for authorization");
    }

    /**
     * Request Method Not supported
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(" RequestAddress'{}',Not supported'{}' Request", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * Business Exception
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     *  The required path variable is missing in the request path
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(" The required path variable is missing in the request path'{}',A system exception has occurred.", requestURI, e);
        return AjaxResult.error(String.format(" The required path variable is missing in the request path[%s]", e.getVariableName()));
    }

    /**
     * Request Parameters Type
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
        log.error("The request parameter types do not match '{}', and a system exception has occurred.", requestURI, e);
        return AjaxResult.error(String.format("The request parameter types do not match. Parameter [%s] requires the type: '%s', but the input value is: '%s'.", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     *
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(" RequestAddress'{}',An unknown exception has occurred.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error(" RequestAddress'{}',An unknown exception has occurred.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }


    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * Internal Authentication Exception
     */
    @ExceptionHandler(InnerAuthException.class)
    public AjaxResult handleInnerAuthException(InnerAuthException e)
    {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * Demo Mode Exception
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("Demo mode. Operations are not allowed");
    }
}
