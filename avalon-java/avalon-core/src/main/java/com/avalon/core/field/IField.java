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

    // 获取字段所在模型
    AbstractService getService();

    // 唯一值
    Boolean isUnique();//是否是唯一的值

    /**
     * 唯一
     *
     * @param isUnique 唯一
     */
    void setIsUnique(Boolean isUnique);

    // 允许为null
    Boolean allowNull();//是否允许为空

    /**
     * 设置可以为空值
     *
     * @param allowNull 空值
     */
    void setAllowNull(Boolean allowNull);

    // 字段名称，即属性名，也是数据库字段名 使用驼峰
    String getName();

    //数据库名称
    String getFieldName();

    // 主键
    Boolean isPrimaryKey();

    // 自增
    Boolean isAutoIncrement();

    //默认值
    IFieldDefaultValue getDefaultValue();

    void setDefaultValue(IFieldDefaultValue defaultValue);

    // 必填
    Boolean isRequired();

    void setIsRequired(Boolean isRequired);

    // 只读
    Boolean isReadonly();

    Type getFieldType();

    Object getSqlValue(Object value);

    Object getClientValue(Object value); // 得到前端显示的值

    String getClassType();

    /**
     * 字段是否可以查询，用于web端
     * @return 查询
     */
    Boolean getCanSearch();
}
