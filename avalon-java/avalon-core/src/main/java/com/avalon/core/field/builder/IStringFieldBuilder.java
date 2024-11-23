/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field.builder;

public interface IStringFieldBuilder<R> extends IFieldBuilder<R> {
    R setMaxLength(Integer maxLength);

    R setMinLength(Integer minLength);

    R setSize(Integer size);
}
