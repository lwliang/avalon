/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.alias.DefaultAliasSupport;
import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.db.AvalonPreparedStatement;
import com.avalon.core.enums.ServiceOperateEnum;
import com.avalon.core.field.Field;
import com.avalon.core.lock.ILockSupport;
import com.avalon.core.model.*;
import com.avalon.core.util.FieldValue;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;

public interface IAvalonService extends ILockSupport,
        ISelectService,
        IInsertService,
        IDeleteService,
        IUpdateService,
        IModuleSupport,
        IInvokeMethod,
        IInheritTable {
    String getLabel(); // 服务标签 比如用户

    Boolean getNeedLog();

    Boolean getNeedDefaultField();//是否需要默认字段 createTime,updateTime,creator,updater 默认需要

    Boolean getNeedDefaultKeyField();//是否需要默认主键字段 id 默认需要

    String getBaseName();//获取数据库名称

    Field getNameField();//获取名称字段

    FieldHashMap getFieldMap();//获取所有字段字典

    FieldHashMap getRelationFieldMap();//获取所有关联字段字典

    FieldHashMap getNotRelationAndPrimaryKeyFieldMap();//获取所有非关联主键字段字典


    AvalonPreparedStatement createAvalonPrepareStatement(StringBuilder sql, FieldValueStatement valueStatement);//创建avalon预编译语句

    Object getPrimaryKeyValue(RecordRow row);//获取主键值

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    String getServiceName();//获取服务名

    String getServiceTableName();//获取服务表明

    String getPrimaryKeyName();//获取主键名

    Type getPrimaryKeyType();//获取主键数据类型

    List<String> getAllFieldName();//默认不包括One2manyField和One2oneField

    void createTable();//创建表

    void dropTable();//删除表

    RecordRow getDefaultValueAll();//获取默认值

    FieldHashMap getIsRequiredAll();//获取所有字段是否必填

    String getAlias(DefaultAliasSupport defaultAliasSupport);//获取别名

    FieldValue getSumFieldValue(Field field, Condition condition);//获取指定字段的求和值

    FieldValue getSumFieldValue(String fieldName, Condition condition);//获取指定字段的求和值

    Field getPrimaryKeyField();//获取主键字段

    List<Field> getFields();//获取所有字段

    Field getField(String name);//获取字段

    Boolean containField(String name);//包含字段

    Boolean containOp(RecordRow recordRow);

    ServiceOperateEnum getOp(RecordRow recordRow);

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    AbstractService getServiceBean(String serviceName);

    Boolean needDefaultNameField();//是否需要默认名称字段 name 默认需要

    Context getContext();

    Boolean isLazyLoadField();//是否延迟加载字段

    FieldHashMap getUniqueFiled();//获取唯一字段

    Boolean hasRelationTable();//是否具有子表

    AbstractService getService();//获取当前服务 为了解决spring代理之后 对象不一致的问题

    FieldHashMap getFieldFormatMap();//获取所有需要格式化的字段字典

}
