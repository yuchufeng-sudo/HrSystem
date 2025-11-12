package com.ys.common.security.aspect;

import com.ys.common.security.annotation.RequiresLogin;
import com.ys.common.security.annotation.RequiresPermissions;
import com.ys.common.security.annotation.RequiresRoles;
import com.ys.common.security.auth.AuthUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 *
 * @author kong
 */
@Aspect
@Component
public class PreAuthorizeAspect
{
    /**
     *
     */
    public PreAuthorizeAspect()
    {
    }

    /**
     *
     */
    public static final String POINTCUT_SIGN = " @annotation(com.ys.common.security.annotation.RequiresLogin) || "
            + "@annotation(com.ys.common.security.annotation.RequiresPermissions) || "
            + "@annotation(com.ys.common.security.annotation.RequiresRoles)";

    /**
     *
     */
    @Pointcut(POINTCUT_SIGN)
    public void pointcut()
    {
    }

    /**
     *
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable
    {
        //
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        checkMethodAnnotation(signature.getMethod());
        try
        {
            //
            Object obj = joinPoint.proceed();
            return obj;
        }
        catch (Throwable e)
        {
            throw e;
        }
    }

    /**
     *
     */
    public void checkMethodAnnotation(Method method)
    {
        //
        RequiresLogin requiresLogin = method.getAnnotation(RequiresLogin.class);
        if (requiresLogin != null)
        {
            AuthUtil.checkLogin();
        }

        //
        RequiresRoles requiresRoles = method.getAnnotation(RequiresRoles.class);
        if (requiresRoles != null)
        {
            AuthUtil.checkRole(requiresRoles);
        }


        RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);
        if (requiresPermissions != null)
        {
            AuthUtil.checkPermi(requiresPermissions);
        }
    }
}
