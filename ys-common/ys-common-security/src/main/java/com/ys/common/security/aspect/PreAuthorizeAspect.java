package com.ys.common.security.aspect;

import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.ys.common.security.annotation.RequiresLogin;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.annotation.RequiresRoles;
import com.ys.common.security.auth.AuthUtil;

/**
 * Annotation-based Authorization using Spring AOP
 * Intercepts and validates authentication annotations on methods
 *
 * @author kong
 */
@Aspect
@Component
public class PreAuthorizeAspect
{
    /**
     * Constructor
     */
    public PreAuthorizeAspect()
    {
    }

    /**
     * Define AOP pointcut signature (intercepts all methods using authorization annotations)
     */
    public static final String POINTCUT_SIGN = " @annotation(com.ys.common.security.annotation.RequiresLogin) || "
            + "@annotation(com.ys.common.security.annotation.RequiresPermissions) || "
            + "@annotation(com.ys.common.security.annotation.RequiresRoles)";

    /**
     * Declare AOP pointcut
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut()
    {
    }

    /**
     * Around advice for method interception
     *
     * @param joinPoint Aspect join point object
     * @return Return value from the underlying method execution
     * @throws Throwable Exception thrown by the underlying method
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        // Annotation-based authentication
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        checkMethodAnnotation(signature.getMethod());
        try
        {
            // Execute the original logic
            Object obj = joinPoint.proceed();
            return obj;
        }
        catch (Throwable e)
        {
            throw e;
        }
    }

    /**
     * Perform annotation validation on a Method object
     *
     * @param method Method to validate
     */
    public void checkMethodAnnotation(Method method)
    {
        // Validate @RequiresLogin annotation
        RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);
        if (requiresLogin != null)
        {
            AuthUtil.checkLogin();
        }

        // Validate @RequiresRoles annotation
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        if (requiresRoles != null)
        {
            AuthUtil.checkRole(requiresRoles);
        }

        // Validate @RequiresPermissions annotation
        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions != null)
        {
            AuthUtil.checkPermi(requiresPermissions);
        }
    }
}
