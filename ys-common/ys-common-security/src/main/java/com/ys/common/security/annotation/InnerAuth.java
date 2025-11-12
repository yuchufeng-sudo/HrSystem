package com.ys.common.security.annotation;

import java.lang.annotation.*;

/**
 * 
 * 
 * @author ruoyi
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAuth
{
    /**
     */
    boolean isUser() default false;
}