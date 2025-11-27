package com.ys.common.datasource.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

/**
 * Master Data Source
 *
 * @author ys
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("master")
public @interface Master
{

}
