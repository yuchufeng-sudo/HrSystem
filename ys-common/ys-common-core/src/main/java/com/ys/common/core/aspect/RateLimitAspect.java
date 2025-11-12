package com.ys.common.core.aspect;
import com.ys.common.core.exception.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @description: Rate limiting aspect component
 * @author: xz_Frank
 * @date: 2025/8/14
 */

@Aspect
@Component
public class RateLimitAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * Redis rate limit key prefix
     */
    private static final String REDIS_LIMIT_PREFIX = "rate_limit:";

    @Around("@annotation(com.ys.common.core.aspect.RateLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get current request and annotation information
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        // Generate rate limit key (IP + interface identifier to prevent interference between different IPs)
        String ip = getClientIp(request);
        String key = REDIS_LIMIT_PREFIX + (rateLimit.prefix().isEmpty()
                ? method.getDeclaringClass().getName() + ":" + method.getName()
                : rateLimit.prefix()) + ":" + ip;

        // Try to acquire token (distributed rate limiting implemented with Redis)
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, "1", 1, TimeUnit.SECONDS);
        if (acquired != null && acquired) {
            // First request, set initial count to 1
            redisTemplate.opsForValue().increment(key, 1);
        } else {
            // Not the first request, increment count by 1
            Long count = redisTemplate.opsForValue().increment(key, 1);
            if (count != null && count > rateLimit.limit()) {
                // Exceed rate limit threshold, return error
                throw new ServiceException("Requests are too frequent, please try again later");
            }
        }

        // Execute original method
        return joinPoint.proceed();
    }

    /**
     * Get client's real IP address
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // Take the first IP when there are multiple proxies
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
