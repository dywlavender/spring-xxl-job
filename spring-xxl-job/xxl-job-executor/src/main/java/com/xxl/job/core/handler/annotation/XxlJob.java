package com.xxl.job.core.handler.annotation;

import java.lang.annotation.*;

/**
 * @author dyw
 * @date 2022-07-03  22:48
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface XxlJob {
    String value();
    String init() default "";
    String destory() default "";
}
