package com.ys.common.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import com.ys.common.security.config.ApplicationConfig;
import com.ys.common.security.feign.FeignAutoConfiguration;

/**
 * Custom Configuration Enabler Annotation
 * Enables common configurations including AOP proxy, MyBatis mapper scanning,
 * asynchronous execution, and auto-configuration imports
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// Expose the proxy object through AOP framework, allowing AopContext to access it
@EnableAspectJAutoProxy(exposeProxy = true)
// Specify the package path to scan for Mapper classes
@MapperScan("com.ys.**.mapper")
// Enable asynchronous thread execution
@EnableAsync
// Auto-load configuration classes
@Import({ ApplicationConfig.class, FeignAutoConfiguration.class })
public @interface EnableCustomConfig
{

}
