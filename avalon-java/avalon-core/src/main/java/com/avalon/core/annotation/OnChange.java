package com.avalon.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/05/07 10:19
 */

/**
 * 记录需要监听字段变更
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnChange {
    String[] value();
}
