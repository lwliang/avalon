/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

/**
 * 获取数字型的大小值
 */
public interface INumberField<T> {
    T getMaxValue();

    T getMinValue();
}
