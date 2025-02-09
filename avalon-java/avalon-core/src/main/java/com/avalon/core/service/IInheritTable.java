/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.field.Field;
import com.avalon.core.field.FieldList;
import com.avalon.core.model.DelegateInheritMap;

import java.util.List;

public interface IInheritTable {
    /**
     * 继承模式 getServiceName == getInherit 则是扩展，否则是继承
     *
     * @return 继承模型
     */
    String getInherit();

    /**
     * 继承字段
     *
     * @return 继承字段
     */
    List<Field> getInheritFields();

    /**
     * 委托继承
     * 格式 serviceName: field
     *
     * @return 继承模型
     */
    DelegateInheritMap getDelegateInherit();

    /**
     * 获取委托继承字段
     *
     * @return 字段列表
     */
    FieldList getDelegateInheritFields();

    FieldList getDelegateInheritFields(String delegateServiceName);

    /**
     * 判断是否是委托字段
     *
     * @param fieldName 字段名
     * @return 是 否
     */
    boolean isDelegateInheritField(String fieldName);
}
