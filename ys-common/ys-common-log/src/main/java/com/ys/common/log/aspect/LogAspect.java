package com.ys.common.log.aspect;

import java.util.Collection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson2.JSON;
import com.ys.common.core.utils.ServletUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.core.utils.ip.IpUtils;
import com.ys.common.log.annotation.Log;
import com.ys.common.log.enums.BusinessStatus;
import com.ys.common.log.filter.PropertyPreExcludeFilter;
import com.ys.common.log.service.AsyncLogService;
import com.ys.common.security.utils.SecurityUtils;
import com.ys.system.api.domain.SysOperLog;

/**
 * Operation Log Recording Handler
 * AOP aspect for logging user operations and system activities
 *
 * @author ruoyi
 */
@Aspect
@Component
public class LogAspect
{
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /** Exclude sensitive property fields */
    public static final String[] EXCLUDE_PROPERTIES = { "password", "oldPassword", "newPassword", "confirmPassword" };

    /** Calculate operation execution time */
    private static final ThreadLocal<Long> TIME_THREADLOCAL = new NamedThreadLocal<Long>("Cost Time");

    @Autowired
    private AsyncLogService asyncLogService;

    /**
     * Execute before processing request
     *
     * @param joinPoint Join point
     * @param controllerLog Log annotation
     */
    @Before(value = "@annotation(controllerLog)")
    public void boBefore(JoinPoint joinPoint, Log controllerLog)
    {
        TIME_THREADLOCAL.set(System.currentTimeMillis());
    }

    /**
     * Execute after processing request
     *
     * @param joinPoint Join point
     * @param controllerLog Log annotation
     * @param jsonResult Response result
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult)
    {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * Intercept exception operations
     *
     * @param joinPoint Join point
     * @param controllerLog Log annotation
     * @param e Exception
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e)
    {
        handleLog(joinPoint, controllerLog, e, null);
    }

    /**
     * Handle log recording
     *
     * @param joinPoint Join point
     * @param controllerLog Log annotation
     * @param e Exception (if any)
     * @param jsonResult Response result (if any)
     */
    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult)
    {
        try
        {
            // *========Database Log=========*//
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // Request IP address
            String ip = IpUtils.getIpAddr();
            operLog.setOperIp(ip);
            operLog.setOperUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            String username = SecurityUtils.getUsername();
            if (StringUtils.isNotBlank(username))
            {
                operLog.setOperName(username);
            }

            if (e != null)
            {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // Set method name
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // Set request method
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // Process annotation parameters
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult);
            // Set execution time
            operLog.setCostTime(System.currentTimeMillis() - TIME_THREADLOCAL.get());
            // Save to database
            asyncLogService.saveSysLog(operLog);
        }
        catch (Exception exp)
        {
            // Log local exception
            log.error("Exception information: {}", exp.getMessage());
            exp.printStackTrace();
        }
        finally
        {
            TIME_THREADLOCAL.remove();
        }
    }

    /**
     * Get method description from annotation for Controller layer
     *
     * @param joinPoint Join point
     * @param log Log annotation
     * @param operLog Operation log
     * @param jsonResult Response result
     * @throws Exception if error occurs
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog, Object jsonResult) throws Exception
    {
        // Set action type
        operLog.setBusinessType(log.businessType().ordinal());
        // Set title
        operLog.setTitle(log.title());
        // Set operator type
        operLog.setOperatorType(log.operatorType().ordinal());
        // Check if request data should be saved
        if (log.isSaveRequestData())
        {
            // Get parameter information and save to database
            setRequestValue(joinPoint, operLog, log.excludeParamNames());
        }
        // Check if response data should be saved
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult))
        {
            operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
        }
    }

    /**
     * Get request parameters and add to log
     *
     * @param joinPoint Join point
     * @param operLog Operation log
     * @param excludeParamNames Parameters to exclude
     * @throws Exception if error occurs
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog, String[] excludeParamNames) throws Exception
    {
        Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
        String requestMethod = operLog.getRequestMethod();
        if (StringUtils.isEmpty(paramsMap) && StringUtils.equalsAny(requestMethod, HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name()))
        {
            String params = argsArrayToString(joinPoint.getArgs(), excludeParamNames);
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        }
        else
        {
            operLog.setOperParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter(excludeParamNames)), 0, 2000));
        }
    }

    /**
     * Concatenate parameters into string
     *
     * @param paramsArray Parameters array
     * @param excludeParamNames Parameters to exclude
     * @return Concatenated parameter string
     */
    private String argsArrayToString(Object[] paramsArray, String[] excludeParamNames)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0)
        {
            for (Object o : paramsArray)
            {
                if (StringUtils.isNotNull(o) && !isFilterObject(o))
                {
                    try
                    {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter(excludeParamNames));
                        params += jsonObj.toString() + " ";
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * Create filter to exclude sensitive properties
     *
     * @param excludeParamNames Additional parameter names to exclude
     * @return Property filter
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter(String[] excludeParamNames)
    {
        return new PropertyPreExcludeFilter().addExcludes(ArrayUtils.addAll(EXCLUDE_PROPERTIES, excludeParamNames));
    }

    /**
     * Check if object should be filtered
     *
     * @param o Object to check
     * @return true if object should be filtered, false otherwise
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o)
    {
        if (o == null) {
            return false;
        }

        Class<?> clazz = o.getClass();

        // Handle array types
        if (clazz.isArray()) {
            return MultipartFile.class.isAssignableFrom(clazz.getComponentType());
        }

        // Handle Collection types
        if (Collection.class.isAssignableFrom(clazz)) {
            Collection<?> collection = (Collection<?>) o;
            return collection.stream().anyMatch(MultipartFile.class::isInstance);
        }

        // Handle Map types
        if (Map.class.isAssignableFrom(clazz)) {
            Map<?, ?> map = (Map<?, ?>) o;
            return map.values().stream().anyMatch(MultipartFile.class::isInstance);
        }

        // Handle other types
        return o instanceof MultipartFile ||
                o instanceof HttpServletRequest ||
                o instanceof HttpServletResponse ||
                o instanceof BindingResult;
    }
}
