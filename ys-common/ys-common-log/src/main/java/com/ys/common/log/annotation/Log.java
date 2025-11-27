package com.ys.common.log.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.ys.common.log.enums.BusinessType;
import com.ys.common.log.enums.OperatorType;

/**
 * Custom Operation Log Recording Annotation
 * Used to mark methods that require operation logging
 *
 * @author ruoyi
 *
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * Module name
     */
    public String title() default "";

    /**
     * Business operation type
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * Operator type category
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * Whether to save request parameters
     */
    public boolean isSaveRequestData() default true;

    /**
     * Whether to save response parameters
     */
    public boolean isSaveResponseData() default true;

    /**
     * Exclude specified request parameter names
     */
    public String[] excludeParamNames() default {};
}
