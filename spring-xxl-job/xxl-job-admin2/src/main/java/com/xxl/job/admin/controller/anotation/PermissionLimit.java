package com.xxl.job.admin.controller.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName PermissionLimit
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/17 22:18
 * @Version 1.0
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionLimit {
    boolean limit() default true;
    boolean adminuser() default false;
}
