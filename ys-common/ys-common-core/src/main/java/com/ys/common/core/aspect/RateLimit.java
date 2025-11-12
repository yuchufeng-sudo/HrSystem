package com.ys.common.core.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;

/**
 * @description: Rate limiting annotation
 * @author: xz_Frank
 * @date: 2025/8/14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * Number of requests allowed per second
     */
    int limit() default 10;

    /**
     * Prefix for rate limiting key (by default, class name + method name is used)
     */
    String prefix() default "";
}
