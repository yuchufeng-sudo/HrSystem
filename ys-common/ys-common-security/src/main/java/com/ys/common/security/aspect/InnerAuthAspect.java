package com.ys.common.security.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import com.ys.common.core.constant.SecurityConstants;
import com.ys.common.core.exception.InnerAuthException;
import com.ys.common.core.utils.ServletUtils;
import com.ys.common.core.utils.StringUtils;
import com.ys.common.security.annotation.InnerAuth;

/**
 * Internal Service Call Validation Handler
 * AOP aspect for validating internal service-to-service authentication
 *
 * @author ruoyi
 */
@Aspect
@Component
public class InnerAuthAspect implements Ordered
{
    @Around("@annotation(innerAuth)")
    public Object innerAround(ProceedingJoinPoint point, InnerAuth innerAuth) throws Throwable
    {
        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        // Internal request verification
        if (!StringUtils.equals(SecurityConstants.INNER, source))
        {
            throw new InnerAuthException("No internal access permission, access denied");
        }

        String userid = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USER_ID);
        String username = ServletUtils.getRequest().getHeader(SecurityConstants.DETAILS_USERNAME);
        // User information verification
        if (innerAuth.isUser() && (StringUtils.isEmpty(userid) || StringUtils.isEmpty(username)))
        {
            throw new InnerAuthException("User information is not set, access denied");
        }
        return point.proceed();
    }

    /**
     * Ensure this aspect executes before permission authentication AOP
     */
    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
