/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.service.AbstractService;

import java.lang.reflect.Type;

public interface IField {
    //返回数据类型对应的数据库类型字段
    Integer getSqlType();

    AbstractService getService();

    Boolean isUnique();//是否是唯一的值

    /**
     * 唯一
     *
     * @param isUnique 唯一
     */
    void setIsUnique(Boolean isUnique);

    Boolean allowNull();//是否允许为空

    /**
     * 设置可以为空值
     *
     * @param allowNull 空值
     */
    void setAllowNull(Boolean allowNull);

    String getName();


    String getFieldName();

    Boolean isPrimaryKey();

    Boolean isAutoIncrement();

    IFieldDefaultValue getDefaultValue();

    void setDefaultValue(IFieldDefaultValue defaultValue);

    Boolean isRequired();

    void setIsRequired(Boolean isRequired);

    Boolean isReadonly();

    Type getFieldType();

    Object getSqlValue(Object value);

    Object getClientValue(Object value); // 得到前端显示的值

    String getClassType();
}
