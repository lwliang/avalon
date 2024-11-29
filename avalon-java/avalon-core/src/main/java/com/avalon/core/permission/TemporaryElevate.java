package com.avalon.core.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/29 8:41
 */

/**
 * 用于临时提示权限
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TemporaryElevate {
    ElevatePermissionEnum[] value();
}
