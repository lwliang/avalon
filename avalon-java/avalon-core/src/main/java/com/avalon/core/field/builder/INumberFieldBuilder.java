/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field.builder;

import com.avalon.core.field.Field;

/**
 * 数字类型字段构建器
 *
 * @param <T> 数字类型 比如 int float double long
 */
public interface INumberFieldBuilder<T, R> extends IFieldBuilder<R> {
    R setMaxValue(T maxValue);

    R setMinValue(T minValue);
}
