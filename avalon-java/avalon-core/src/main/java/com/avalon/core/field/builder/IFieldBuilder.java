/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field.builder;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.field.Field;

public interface IFieldBuilder<R> {
    R setIsUnique(Boolean isUnique);

    R setAllowNull(Boolean allowNull);

    R setIsRequired(Boolean isRequired);

    R setDefaultValue(IFieldDefaultValue defaultValue);

    R setIsAutoIncrement(Boolean isAutoIncrement);

    R setIsPrimaryKey(Boolean isPrimaryKey);

    R setIsReadonly(Boolean isReadonly);

    R setFieldName(String fieldName);

    R setLabel(String label);

    /**
     * 创建字段
     *
     * @return 字段
     */
    Field build();
}
