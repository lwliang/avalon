/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.db.AutoKeyPreparedStatement;
import com.avalon.core.db.AvalonPreparedStatement;
import com.avalon.core.db.DataBaseTools;
import com.avalon.core.db.DynamicJdbcTemplate;
import com.avalon.core.enums.ServiceOperateEnum;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.exception.FieldCheckException;
import com.avalon.core.field.*;
import com.avalon.core.log.IAvalonServiceLog;
import com.avalon.core.log.ServiceLog;
import com.avalon.core.model.Record;
import com.avalon.core.model.*;
import com.avalon.core.alias.DefaultAliasSupport;
import com.avalon.core.alias.IAliasRequire;
import com.avalon.core.module.AbstractModule;
import com.avalon.core.select.builder.QueryStatement;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.select.builder.SelectPageBuilder;
import com.avalon.core.select.builder.SelectTreeBuilder;
import com.avalon.core.tree.QueryNode;
import com.avalon.core.tree.SelectOneModel;
import com.avalon.core.tree.SelectOneTree;
import com.avalon.core.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
@Lazy
public abstract class AbstractService implements IAvalonService, IAliasRequire {

    public final static String CREATE_TIME = "createTime";
    public final static String CREATOR = "creator";
    public final static String UPDATE_TIME = "updateTime";
    public final static String UPDATER = "updater";
    public final static String NAME = "name";
    public final static String ID = "id";
    public final static String OPERATE = "op";//系统使用的字段，子类不能使用

    /**
     * 获取当前事物id
     *
     * @return
     */
    public String getTransactionId() {
        return TransactionSynchronizationManager.getCurrentTransactionName() + "." + context.getUUid();
    }

    @Getter
    @Autowired
    private DynamicJdbcTemplate jdbcTemplate;

    private String getUserServiceName() {
        try {
            if (this instanceof IUserService) {
                return getServiceName();
            }
            IUserService userService = Context.getAvalonApplicationContextInstance().getBean(IUserService.class);
            return userService.getServiceName();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return "base.user";
        }
    }

    @Autowired
    private Context context;

    @Override
    public Boolean getNeedLog() {
        return true;
    }

    private Boolean needLog = null;//缓存提高效率

    protected Boolean canLog() {
        if (ObjectUtils.isNotNull(needLog)) return needLog;

        Boolean result = getNeedLog();
        try {
            IAvalonServiceLog bean = getContext().getAvalonApplicationContext()
                    .getBean(IAvalonServiceLog.class);
        } catch (Exception e) {
            log.info("未实现日志接口,不能记录操作日志");
            result = false;
        }
        needLog = result;
        return result;
    }

    protected IAvalonServiceLog getServiceLogBean() {
        return getContext().getAvalonApplicationContext()
                .getBean(IAvalonServiceLog.class);
    }

    /**
     * 插入日志
     *
     * @param row
     */
    protected void insertLog(RecordRow row) {
        if (!canLog()) return;
        IAvalonServiceLog serviceLogBean = getServiceLogBean();
        ServiceLog serviceLog = new ServiceLog();
        RecordRow masterRecordRow = getMasterRecordRow(row);
        serviceLog.setServiceId(getPrimaryKeyValue(row));
        serviceLog.setServiceName(getServiceName());
        serviceLog.setHandleId(getContext().getUserId());
        serviceLog.setHandleName(getContext().getUserName());
        serviceLog.setContent(masterRecordRow.convert2Json());
        serviceLog.setOp(ServiceOperateEnum.insert);
        serviceLogBean.insert(serviceLog);
    }

    /**
     * 更新日志
     *
     * @param row
     */
    protected void updateLog(RecordRow row) {
        if (!canLog()) return;
        IAvalonServiceLog serviceLogBean = getServiceLogBean();
        ServiceLog serviceLog = new ServiceLog();
        RecordRow masterRecordRow = getMasterRecordRow(row);
        serviceLog.setServiceId(getPrimaryKeyValue(row));
        serviceLog.setServiceName(getServiceName());
        serviceLog.setHandleId(getContext().getUserId());
        serviceLog.setHandleName(getContext().getUserName());
        serviceLog.setContent(masterRecordRow.convert2Json());
        serviceLog.setOp(ServiceOperateEnum.update);
        serviceLogBean.insert(serviceLog);
    }

    /**
     * 删除日志
     *
     * @param row
     */
    protected void deleteLog(RecordRow row) {
        if (!canLog()) return;
        IAvalonServiceLog serviceLogBean = getServiceLogBean();
        ServiceLog serviceLog = new ServiceLog();
        RecordRow masterRecordRow = getMasterRecordRow(row);
        serviceLog.setServiceId(getPrimaryKeyValue(row));
        serviceLog.setServiceName(getServiceName());
        serviceLog.setHandleId(getContext().getUserId());
        serviceLog.setHandleName(getContext().getUserName());
        serviceLog.setContent(masterRecordRow.convert2Json());
        serviceLog.setOp(ServiceOperateEnum.delete);
        serviceLogBean.insert(serviceLog);
    }


    /**
     * 只获取主表自身的字段值
     *
     * @param row
     * @return
     */
    protected RecordRow getMasterRecordRow(RecordRow row) {
        FieldHashMap fieldMap1 = getMasterFieldMap();
        RecordRow newRow = new RecordRow();
        for (Map.Entry<String, Field> field : fieldMap1.entrySet()) {

            if (row.containsKey(field.getKey())) {
                FieldValue x = new FieldValue(row.getRawValue(field.getKey()));
                if (field.getValue() instanceof IFieldFormat) {
                    x.setField((IFieldFormat) field.getValue());
                }
                newRow.put(field.getKey(), x);
            }
        }
        return newRow;
    }

    @Override
    public String getLabel() {
        return "";
    }

    /**
     * 返回主表字段，排除one2Many one2One Many2many字段
     *
     * @return
     */
    public FieldHashMap getMasterFieldMap() {
        Map<String, Field> collect = getFieldMap().entrySet().stream().filter(
                        field -> !(field.getValue() instanceof One2manyField
                                || field.getValue() instanceof One2oneField
                                || field.getValue() instanceof Many2manyField) &&
                                field.getValue().needLog()
                ).
                collect(Collectors.toMap(k -> k.getKey(), k -> k.getValue()));
        return new FieldHashMap(collect);
    }


    @Override
    public Boolean isLazyLoadField() {
        return false;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public Boolean getNeedDefaultField() {
        return true;
    }

    public Boolean getNeedDefaultKeyField() {
        return true;
    }

    public String getBaseName() {
        return context.getBaseName();
    }

    public Field getNameField() {
        return name;
    }

    private final Field name = createNameField();

    protected Field createNameField() {
        return Fields.createString("名称");
    }

    //  主键
    private final Field id = createPrimaryKeyField();

    protected Field createPrimaryKeyField() {
        return Fields.createInteger("主键", true, true);
    }

    @Override
    public Boolean needDefaultNameField() {
        return true;
    }

    @Override
    public Boolean containOp(RecordRow recordRow) {
        return recordRow.containsKey(OPERATE);
    }

    @Override
    public ServiceOperateEnum getOp(RecordRow recordRow) {
        if (!recordRow.containsKey(OPERATE)) return ServiceOperateEnum.none;
        return ServiceOperateEnum.getFromRecordColumn(recordRow.get(OPERATE));
    }

    /**
     * 动态加载字段 提高系统启动速度 并且实现按需使用模块
     */
    protected void loadField() {
        List<java.lang.reflect.Field> allFieldsList = FieldUtils.getAllFieldsList(this.getClass(), AbstractService.class, Field.class);
        if (!getNeedDefaultKeyField()) {
            allFieldsList.removeIf(field ->
                    field.getName().equals("id"));
        }
        FieldHashMap fieldHashMap = new FieldHashMap();
        allFieldsList.forEach(field -> {
            if (Field.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                if (!needDefaultNameField() && field.getName().equals(NAME)) {
                    return;
                }
                try {
                    if (!fieldHashMap.containsKey(field.getName())) {
                        fieldHashMap.put(field.getName(), (Field) field.get(this));
                    }
                    Method setService = null;
                    Class<?> cls = field.getType();
                    while (cls != Object.class) {//将变量名设置为字段名
                        try {
                            setService = cls.getDeclaredMethod("setService", AbstractService.class);
                            break;
                        } catch (NoSuchMethodException e) {
                            cls = cls.getSuperclass();
                        }
                    }

                    if (setService != null) {
                        setService.setAccessible(true);
                        setService.invoke(field.get(this), this);
                    }

                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.debug(this.getClass() + " postConstruct方法 error->" + e.getMessage());
                }
            }
        });

        if (getNeedDefaultField()) {//需要创建人 创建时间，修改人，修改时间字段
            Field field = Fields.createMany2one("创建人", getUserServiceName(), false);
            setFieldService(field);
            fieldHashMap.put(CREATOR, field);
            field = Fields.createDateTime("创建时间");
            setFieldService(field);
            fieldHashMap.put(CREATE_TIME, field);
            field = Fields.createMany2one("修改人", getUserServiceName(), false);
            setFieldService(field);
            fieldHashMap.put(UPDATER, field);
            field = Fields.createDateTime("修改时间");
            setFieldService(field);
            fieldHashMap.put(UPDATE_TIME, field);
        }
        if (getNeedDefaultKeyField()) {
            setFieldService(id);
            fieldHashMap.put(ID, id);
        }

        setFieldMap(fieldHashMap);
    }

    private void setFieldService(Field field) {
        try {
            Method setService = null;
            Class<?> cls = field.getClass();
            while (cls != Object.class) {//将变量名设置为字段名
                try {
                    setService = cls.getDeclaredMethod("setService", AbstractService.class);
                    break;
                } catch (NoSuchMethodException e) {
                    cls = cls.getSuperclass();
                }
            }

            if (setService != null) {
                setService.setAccessible(true);
                setService.invoke(field, this);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug(this.getClass() + " postConstruct方法 error->" + e.getMessage());
        }
    }

    @PostConstruct
    public void postConstruct() { // post中不能执行getService方法
        if (!isLazyLoadField()) {
            loadField();
        }
        if (log.isDebugEnabled()) {
            log.debug("service init->" + getServiceName() + ",class->" + this.getClass().getName());
        }
        try {
            if (context.getAvalonApplicationContext().isActive()) {
                String firstBeanName = context.getFirstBeanName(this.getClass());
                if (StringUtils.isNotEmpty(firstBeanName) && StringUtils.isNotEmpty(getServiceTableName())) {
                    context.registerAlias(firstBeanName, getServiceName());
                    log.debug("registerAlias service {} -> {}", firstBeanName, getServiceName());
                }
            }
        } catch (AvalonException e) {
            log.error("bean 注册别名失败,错误信息->" + e.getMessage());
            log.error(e.getMessage(), e);
        }

        String moduleName = getModuleName();

        if (StringUtils.isNotEmpty(moduleName)) {
            context.putModule(moduleName, this);
        } else {
            context.putModule("tool", this);
        }
    }

    /**
     * 获取模块
     *
     * @param moduleName
     * @return AbstractModule
     */
    protected AbstractModule getModule(String moduleName) {
        if (context.containsModule(moduleName)) {
            return context.getModuleByPackageName(moduleName);
        }

        Set<Class> scan = ClassUtils.scan(moduleName);
        if (scan.isEmpty()) {
            throw new AvalonException("模块" + moduleName + "未注册");
        }
        for (Class cl : scan) {
            if (!AbstractModule.class.isAssignableFrom(cl)) continue;// AbstractModule 是cl的superclass
            context.getAvalonApplicationContext().getBean(cl);
            return context.getModuleByPackageName(moduleName);
        }
        throw new AvalonException("模块" + moduleName + "未注册");
    }

    protected String getModuleName() {
        Package modulePackage = getClass().getPackage(); //xxx.service
        String moduleName;

        moduleName = modulePackage.getName();
        int i = moduleName.lastIndexOf("service");
        moduleName = moduleName.substring(0, i - 1); //xxx
        this.module = getModule(moduleName);
        return this.module.getModuleName();
    }

    private FieldHashMap fieldMap;

    protected void setFieldMap(FieldHashMap fieldMap) {
        this.fieldMap = fieldMap;
    }

    /**
     * 判断字段是否存在
     *
     * @param fieldName
     * @return
     */
    public Boolean containsField(String fieldName) {
        return getFieldMap().containsKey(fieldName);
    }

    public FieldHashMap getFieldMap() {
        if (!(ObjectUtils.isNull(fieldMap) || fieldMap.isEmpty())) {
            return fieldMap;
        }

        synchronized (this) {
            if (!(ObjectUtils.isNull(fieldMap) || fieldMap.isEmpty())) { // 双重判断
                return fieldMap;
            }
            if (this instanceof ExternalService) {
                fieldMap = new FieldHashMap();
            } else {
                loadField();
            }
        }
        return fieldMap;
    }

    /**
     * 获取全部关系字段
     *
     * @return
     */
    public FieldHashMap getRelationFieldMap() {
        Map<String, Field> collect = getFieldMap().entrySet().stream().filter(field -> field.getValue() instanceof RelationField).
                collect(Collectors.toMap(k -> k.getKey(), k -> k.getValue()));
        return new FieldHashMap(collect);
    }

    // 返回非关系、非主键字段
    public FieldHashMap getNotRelationAndPrimaryKeyFieldMap() {
        Map<String, Field> collect = getFieldMap().entrySet().stream().filter(field ->
                !(field.getValue() instanceof RelationField) && !(field.getValue().isPrimaryKey())
        ).collect(Collectors.toMap(k -> k.getKey(), k -> k.getValue()));
        return new FieldHashMap(collect);
    }

    public FieldHashMap getRelationFieldMap(List<String> fields) {
        Map<String, Field> collect = getFieldMap().entrySet().stream().filter(
                        field -> field.getValue() instanceof RelationField && fields.contains(field.getKey())).
                collect(Collectors.toMap(k -> k.getKey(), k -> k.getValue()));
        return new FieldHashMap(collect);
    }

    /**
     * 返回字段 支持及联字段
     *
     * @param name
     * @return
     */
    public Field getField(String name) {
        if (FieldUtils.hasJoinSelect(name)) {
            String tableField = FieldUtils.getJoinFirstTableString(name);
            RelationField field = (RelationField) getFieldMap().get(tableField);
            if (ObjectUtils.isNull(field)) {
                throw new AvalonException("字段" + name + "不存在");
            }
            AbstractService serviceBean = field.getRealService();
            return serviceBean.getField(FieldUtils.getJoinFirstFieldString(name));
        } else {
            return getFieldMap().get(name);
        }
    }

    /**
     * 包含字段
     *
     * @param name
     * @return
     */
    public Boolean containField(String name) {
        if (FieldUtils.hasJoinSelect(name)) {
            String tableField = FieldUtils.getJoinFirstTableString(name);
            FieldHashMap fieldMap1 = getFieldMap();
            if (!fieldMap1.containsKey(tableField)) return false;
            RelationField field = (RelationField) fieldMap1.get(tableField);
            AbstractService serviceBean = getServiceBean(field.getRelativeServiceName());
            return serviceBean.containField(FieldUtils.getJoinFirstFieldString(name));
        } else {
            return getFieldMap().containsKey(name);
        }
    }

    private void doQueryNode(QueryNode node, List<Object> ids, Record parentRecord) {
        if (ids.isEmpty()) {
            return;
        }
        if (node.getField() instanceof One2oneField) {
            selectOne2OneFieldRecord(node, ids, parentRecord);
        } else if (node.getField() instanceof One2manyField) {
            selectOne2ManyFieldRecord(node, ids, parentRecord);
        } else if (node.getField() instanceof Many2oneField) {
            selectMany2OneFieldRecord(node, ids, parentRecord);
        } else if (node.getField() instanceof Many2manyField) {
            selectMany2ManyFieldRecord(node, ids, parentRecord);
        }
    }

    /**
     * 获取多对1的数据集合
     *
     * @param node         查询节点
     * @param ids          主键id
     * @param resultRecord 父结果集
     */
    private void selectMany2OneFieldRecord(QueryNode node,
                                           List<Object> ids,
                                           Record resultRecord) {
        if (ids.isEmpty() || node.getOriginalFields().isEmpty()) {
            return;
        }
        Many2oneField relationField = (Many2oneField) node.getField();
        // 第一步先获取子表ids
        String parentKey = node.getParentNode().getService().getPrimaryKeyName();
        String serviceKey = node.getService().getPrimaryKeyName();
        Record select = node.getParentNode().getService().select(Condition.inCondition(parentKey, ids),
                relationField.getName(), parentKey);
        List<String> originalFields1 = node.getOriginalFields();

        List<Object> values = select.getValues(relationField.getName());
        if (ObjectUtils.isEmpty(values)) return;

        boolean containServiceKey = true;
        List<String> needFields = originalFields1.subList(0, originalFields1.size());
        if (!originalFields1.contains(serviceKey)) {
            containServiceKey = false;
            needFields.add(serviceKey);
        }

        Record many2oneRecord = node.getService().select(Condition.inCondition(serviceKey, values),
                needFields.toArray(new String[0]));
        for (RecordRow recordRow : resultRecord) {
            RecordRow recordRow2 = select.getRecordRow(parentKey, recordRow.getRawValue(parentKey));
            RecordRow recordRow1 = many2oneRecord.getRecordRow(serviceKey, recordRow2.getRawValue(relationField));
            recordRow.put(relationField, recordRow1);
        }
        for (QueryNode queryNode : node.getQueryNodeList()) {
            doQueryNode(queryNode, many2oneRecord.getValues(serviceKey), many2oneRecord);
        }
        for (RecordRow recordRow : many2oneRecord) {
            if (!containServiceKey) {
                recordRow.remove(serviceKey);
            }
        }
    }

    private void selectMany2ManyFieldRecord(QueryNode node,
                                            List<Object> ids,
                                            Record parentRecord) {
        if (ids.isEmpty()) {
            return;
        }
        Many2manyField relationField = (Many2manyField) node.getField();
        List<String> originalFields = node.getOriginalFields();
        String masterForeignKeyName = relationField.getMasterForeignKeyName();
        String serviceKey = node.getService().getPrimaryKeyName();
        String parentKey = node.getParentNode().getService().getPrimaryKeyName();

        List<String> wantFields = originalFields.subList(0, originalFields.size());
        boolean containServiceKey = true;
        if (!originalFields.contains(serviceKey)) {
            containServiceKey = false;
            wantFields.add(serviceKey);
        }
        boolean containMasterKey = true;
        if (!originalFields.contains(masterForeignKeyName)) {
            containMasterKey = false;
            wantFields.add(masterForeignKeyName);
        }

        Record many2Record = node.getService().select(Condition.inCondition(masterForeignKeyName, ids),
                wantFields.toArray(new String[0]));

        for (QueryNode childNode : node.getQueryNodeList()) {
            doQueryNode(childNode, many2Record.getValues(serviceKey), many2Record);
        }

        if (!many2Record.isEmpty()) {
            for (RecordRow recordRow : many2Record) {
                RecordRow recordRow2 = parentRecord.getRecordRow(parentKey, recordRow.getRawValue(masterForeignKeyName));
                if (!containMasterKey) {
                    recordRow.remove(masterForeignKeyName);
                }
                if (!containServiceKey) {
                    recordRow.remove(serviceKey);
                }
                Record record;
                if (!recordRow2.containsKey(relationField)) {
                    record = Record.build();
                    recordRow2.put(relationField, record);
                } else {
                    record = recordRow2.getRecord(relationField);
                }
                if (!recordRow.isEmpty()) {
                    record.add(recordRow);
                }
            }
        } else {
            for (RecordRow recordRow : parentRecord) {
                recordRow.put(relationField, Record.build());
            }
        }
    }

    /**
     * 获取1对多的数据集合
     *
     * @param node         查询节点
     * @param ids          主键id
     * @param resultRecord 父结果集
     */
    private void selectOne2ManyFieldRecord(QueryNode node,
                                           List<Object> ids,
                                           Record resultRecord) {
        if (ids.isEmpty()) {
            return;
        }
        AvalonPreparedStatement avalonPrepareStatement;
        QueryStatement sql;
        SelectBuilder selectBuilder;
        One2manyField relationField = (One2manyField) node.getField();
        List<String> needFields = new ArrayList<>(node.getOriginalFields());

        boolean isContainReltiveField = true;
        String relativeFieldName = relationField.getRelativeFieldName();
        if (!node.getOriginalFields().contains(relativeFieldName)) {
            isContainReltiveField = false;
            needFields.add(relativeFieldName);
        }
        boolean containServiceKey = true;
        String serviceKey = node.getService().getPrimaryKeyName();
        if (!node.getOriginalFields().contains(serviceKey)) {
            containServiceKey = false;
            needFields.add(serviceKey);
        }

        selectBuilder = DataBaseTools.selectSql(node.getService(),
                needFields.toArray(new String[0]),
                Condition.inCondition(relativeFieldName, ids));
        sql = selectBuilder.getSql();
        avalonPrepareStatement = createAvalonPrepareStatement(sql.getSql(),
                sql.getValueStatement());
        Record one2Record = jdbcTemplate.select(avalonPrepareStatement, selectBuilder);

        for (QueryNode childNode : node.getQueryNodeList()) {
            doQueryNode(childNode, one2Record.getValues(serviceKey), one2Record);
        }
        if (one2Record.isEmpty()) {
            resultRecord.putAll(node.getField().getName(), Record.build());
            return;
        }
        for (RecordRow recordRow : one2Record) {
            RecordRow recordRow1 = null;
            if (recordRow.isRecordRow(relativeFieldName)) {
                recordRow1 = resultRecord.getRecordRow(node.getParentNode().getService().getPrimaryKeyName(),
                        recordRow.getRecordRow(relativeFieldName).getRawValue(relationField.getRealService().getPrimaryKeyName()));
            } else {
                recordRow1 = resultRecord.getRecordRow(node.getParentNode().getService().getPrimaryKeyName(),
                        recordRow.getRawValue(relativeFieldName));
            }

            if (!isContainReltiveField) {
                recordRow.remove(relativeFieldName);
            }
            if (!containServiceKey) {
                recordRow.remove(serviceKey);
            }
            if (ObjectUtils.isNull(recordRow1)) {
                continue;
            }
            Record record;
            if (recordRow1.containsKey(node.getField().getName())) {
                record = recordRow1.getRecord(node.getField().getName());
            } else {
                record = Record.build();
                recordRow1.put(node.getField().getName(), record);
            }
            if (!recordRow.isEmpty()) {
                record.add(recordRow);
            }
        }
    }


    /**
     * 获取1对1的单表
     *
     * @param node         查询节点
     * @param ids          主键id
     * @param resultRecord 父结果集
     */
    private void selectOne2OneFieldRecord(QueryNode node,
                                          List<Object> ids,
                                          Record resultRecord) {
        if (ids.isEmpty()) {
            return;
        }
        AvalonPreparedStatement avalonPrepareStatement;
        QueryStatement sql;
        SelectBuilder selectBuilder;
        One2oneField relationField = (One2oneField) node.getField();
        List<String> needFields = new ArrayList<>(node.getOriginalFields());

        boolean isContainReltiveField = true;
        String relativeFieldName = relationField.getRelativeFieldName();
        if (!needFields.contains(relativeFieldName)) {
            isContainReltiveField = false;
            needFields.add(relativeFieldName);
        }

        boolean containServiceKey = true;
        String serviceKey = node.getService().getPrimaryKeyName();
        if (!needFields.contains(serviceKey)) {
            containServiceKey = false;
            needFields.add(serviceKey);
        }
        selectBuilder = DataBaseTools.selectSql(node.getService(),
                needFields.toArray(new String[0]),
                Condition.inCondition(relativeFieldName, ids));
        sql = selectBuilder.getSql();
        avalonPrepareStatement = createAvalonPrepareStatement(sql.getSql(),
                sql.getValueStatement());
        Record one2Record = jdbcTemplate.select(avalonPrepareStatement, selectBuilder);

        for (QueryNode childNode : node.getQueryNodeList()) {
            doQueryNode(childNode, one2Record.getValues(serviceKey), one2Record);
        }

        for (RecordRow recordRow : one2Record) {
            RecordRow recordRow1 = resultRecord.getRecordRow(node.getParentNode().getService().getPrimaryKeyName(),
                    recordRow.getRawValue(relativeFieldName));
            if (!isContainReltiveField) {
                recordRow.remove(relativeFieldName);
            }
            if (!containServiceKey) {
                recordRow.remove(serviceKey);
            }
            if (ObjectUtils.isNotNull(recordRow1)) {
                recordRow1.put(node.getField().getName(), recordRow);
            }
        }
    }


    public AvalonPreparedStatement createAvalonPrepareStatement(StringBuilder sql, FieldValueStatement valueStatement) throws AvalonException {
        return new AvalonPreparedStatement(getService(), sql, valueStatement);
    }

    /**
     * 扩张主键字段
     *
     * @param fields
     * @return
     */
    private String appendPrimaryKey(String fields) {
        if (StringUtils.isEmpty(fields)) return fields;

        if (!FieldUtils.getFieldList(fields).contains(getPrimaryKeyName()))
            return fields + "," + getPrimaryKeyName();

        return fields;
    }

    /**
     * 将字段转换为树字段，进行及联查询
     *
     * @param fields
     * @return
     */
    private SelectOneTree getSelectTreeFromField(String fields) {
        List<String> fieldList = new ArrayList<>(FieldUtils.getFieldList(fields));
        SelectOneTree selectOneTree = new SelectOneTree();
        SelectOneModel selectOneModel = new SelectOneModel();
        selectOneModel.setService(getService());
        selectOneTree.setData(selectOneModel);//设置主表作为根节点

        for (String s : fieldList) {//遍历字段,并且分类字段
            selectOneTree.addChildren(s, getService(), selectOneTree);
        }

        return selectOneTree;
    }


    /**
     * 删除记录 不会检查
     *
     * @param id
     * @return
     * @throws AvalonException
     */
    public Integer delete(Object id) throws AvalonException {
        StringBuilder sql = DataBaseTools.deleteSql(getServiceBean(getServiceName()), id);
        RecordRow row = new RecordRow();
        row.put("id", id);
        deleteLog(row);
        return jdbcTemplate.delete(sql);
    }

    /**
     * 条件删除 会检查记录是否存在
     *
     * @param condition
     * @return
     * @throws AvalonException
     */
    public Integer delete(Condition condition, String serviceName) throws AvalonException {
        AbstractService serviceBean = getServiceBean(serviceName);

        Record record = serviceBean.select("", condition,
                serviceBean.getAllFieldName().toArray(new String[0]));

        if (ObjectUtils.isNotEmpty(record)) {
            for (RecordRow row : record) {
                serviceBean.delete(row);
            }
            return record.size();
        }
        return 0;
    }

    public Integer delete(Condition condition) throws AvalonException {
        Record record = select("", condition, getAllFieldName().toArray(new String[0]));
        if (ObjectUtils.isNotEmpty(record)) {
            for (RecordRow row : record) {
                this.delete(row);
            }
            return record.size();
        }
        return 0;
    }

    /**
     * 删除记录 会检查当前记录
     *
     * @param row
     * @return
     * @throws AvalonException
     */
    public Integer delete(RecordRow row) throws AvalonException {
        Integer id = (Integer) getPrimaryKeyValue(row);
        checkDelete(row);

        FieldHashMap relationFieldMap = getRelationFieldMap();
        for (Map.Entry<String, Field> fieldEntry : relationFieldMap.entrySet()) {//处理子表
            String key = fieldEntry.getKey();
            RelationField field = (RelationField) fieldEntry.getValue();

            RecordColumn recordColumn = row.get(getPrimaryKeyName());
            if (ObjectUtils.isNull(recordColumn)) continue;

            if (field instanceof One2manyField) {
                AbstractService serviceBean = field.getRealService();
                Record record = serviceBean.select(Condition.equalCondition(field.getRelativeFieldName(), id),
                        serviceBean.getAllFieldName().toArray(new String[0]));
                if (ObjectUtils.isNotEmpty(record)) {
                    serviceBean.deleteMulti(record);
                }
            } else if (field instanceof One2oneField) {
                AbstractService serviceBean = field.getRealService();
                Record record = serviceBean.select(Condition.equalCondition(field.getRelativeFieldName(), id),
                        serviceBean.getAllFieldName().toArray(new String[0]));
                if (ObjectUtils.isNotEmpty(record)) {
                    serviceBean.deleteMulti(record);
                }
            } else if (field instanceof Many2manyField) {
                AbstractService serviceBean = field.getRealService();
                Record record = serviceBean.select(Condition.equalCondition(((Many2manyField) field).getMasterKeyName(), id),
                        serviceBean.getAllFieldName().toArray(new String[0]));
                if (ObjectUtils.isNotEmpty(record)) {
                    serviceBean.deleteMulti(record);
                }
            }
        }

        return delete(id);
    }

    protected void checkDelete(RecordRow recordRow) throws AvalonException {

    }

    public Integer deleteMulti(List<Integer> ids) throws AvalonException {
        Integer count = 0;
        for (Integer id : ids) {
            count += delete(id);
        }
        return count;
    }

    public Integer deleteMulti(Record record) throws AvalonException {
        Integer count = 0;
        for (RecordRow row : record) {
            count += delete(row);
        }
        return count;
    }

    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        if (getNeedDefaultField()) {
            if (!recordRow.containsKey(CREATE_TIME)) {
                recordRow.put(CREATE_TIME, new RecordColumn(DateTimeUtils.getCurrentDate()));
            }
            if (!recordRow.containsKey(CREATOR)) {
                Integer userId = context.getUserId();
                if (ObjectUtils.isNotNull(userId)) {
                    recordRow.put(CREATOR, new RecordColumn(context.getUserId()));
                }
            }
        }

        //增加默认值
        RecordRow defaultValueAll = this.getDefaultValueAll();

        defaultValueAll.forEach(((name, value) -> {
            if (ObjectUtils.isNotNull(value.getValue())) {
                if (!recordRow.containsKey(name)) {
                    recordRow.put(name, value);
                } else {

                    if (ObjectUtils.isNull(recordRow.get(name))) {
                        recordRow.put(name, value);
                        return;
                    }

                    if (ObjectUtils.isEmpty(recordRow.get(name).getValue())) {
                        recordRow.put(name, value);
                    }
                }
            }
        }));

        checkBeforeInsert(recordRow);
        // 判断是否可以保存
        checkInsert(recordRow);

        FieldValueStatement fieldValueStatement = new FieldValueStatement();
        StringBuilder sql = DataBaseTools.insertSql(getService(), recordRow,
                fieldValueStatement);//返回主键

        AvalonPreparedStatement autoKeyPreparedStatement = null;

        Object masterId;
        if (getPrimaryKeyField().isAutoIncrement()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            autoKeyPreparedStatement = new AutoKeyPreparedStatement(getService(),
                    sql, fieldValueStatement);
            masterId = jdbcTemplate.insert(autoKeyPreparedStatement, keyHolder);
        } else {
            autoKeyPreparedStatement = new AvalonPreparedStatement(getService(),
                    sql, fieldValueStatement);
            jdbcTemplate.insert(autoKeyPreparedStatement);
            masterId = getPrimaryKeyValue(recordRow);
        }

        recordRow.put(getPrimaryKeyName(), masterId);
        insertLog(recordRow);
        checkAfterInsert(recordRow);
        FieldHashMap relationFieldMap = getRelationFieldMap();

        for (Map.Entry<String, Field> fieldEntry : relationFieldMap.entrySet()) {//处理子表
            String key = fieldEntry.getKey();
            RelationField field = (RelationField) fieldEntry.getValue();

            RecordColumn recordColumn = recordRow.get(key);
            if (ObjectUtils.isNull(recordColumn)) continue;

            if (field instanceof One2manyField) {
                AbstractService serviceBean =
                        context.getServiceBean(field.getRelativeServiceName());
                String relativeFieldName = field.getRelativeFieldName();
                Record record = recordColumn.getRecord();

                record.putAll(relativeFieldName, masterId);//子表外键赋值
                serviceBean.insertMulti(record);
            } else if (field instanceof One2oneField) {
                AbstractService serviceBean =
                        context.getServiceBean(field.getRelativeServiceName());
                String relativeFieldName = field.getRelativeFieldName();
                RecordRow recordRow1 = recordColumn.getRecordRow();

                recordRow1.put(relativeFieldName, masterId);//子表外键赋值
                serviceBean.insert(recordRow1);
            } else if (field instanceof Many2manyField) {
                Many2manyField many2manyField = (Many2manyField) field;
                AbstractService serviceBean = many2manyField.getRealService();
                String masterForeignKeyName = many2manyField.getMasterForeignKeyName();
                Record record = recordColumn.getRecord();
                record.putAll(masterForeignKeyName, masterId);
                serviceBean.insertMulti(record);
            }
        }

        return PrimaryKey.build(masterId);
    }

    /**
     * 更新之前
     *
     * @param recordRow
     * @throws AvalonException
     */

    protected void checkBeforeInsert(RecordRow recordRow) throws AvalonException {
        checkUniqueField(recordRow);
    }

    @Override
    public FieldHashMap getUniqueFiled() {
        Map<String, Field> collect = getFieldMap().entrySet().stream().filter(field -> field.getValue().isUnique()).
                collect(Collectors.toMap(k -> k.getKey(), k -> k.getValue()));
        return new FieldHashMap(collect);
    }

    @Override
    public Boolean hasRelationTable() {
        return getRelationFieldMap().size() != 0;
    }

    /**
     * 检查更新之后
     *
     * @param recordRow
     * @throws AvalonException
     */
    protected void checkAfterInsert(RecordRow recordRow) throws AvalonException {

    }

    private void checkUniqueField(RecordRow recordRow) throws AvalonException {
        FieldHashMap uniqueFiled = getUniqueFiled();
        if (uniqueFiled.size() > 0) {
            for (Map.Entry<String, Field> fieldEntry : uniqueFiled.entrySet()) {
                String key = fieldEntry.getKey();
                Field field = fieldEntry.getValue();
                if (recordRow.containsKey(key)) {
                    RecordColumn recordColumn = recordRow.get(key);

                    Condition condition = Condition.equalCondition(key, recordColumn.getValue());
                    if (recordRow.containsKey(getPrimaryKeyName())) {
                        condition = condition.andCondition(Condition.notEqualCondition(getPrimaryKeyName(),
                                recordRow.get(getPrimaryKeyName()).getValue()));
                    }
                    Integer count = selectCount(condition);

                    if (count >= 1) {
                        throw new FieldCheckException(field, field.getLabel() + "：" + recordColumn.getValue() + "，已经存在");
                    }
                }
            }
        }
    }

    /**
     * 新增之前检查，报错 则无法创建
     *
     * @param recordRow
     * @throws AvalonException
     */
    protected void checkInsert(RecordRow recordRow) throws AvalonException {
        checkRequiredField(recordRow);
    }

    protected void checkRequiredField(RecordRow recordRow) {
        FieldHashMap requiredFields = getIsRequiredAll();

        for (Map.Entry<String, Field> fieldEntry : requiredFields.entrySet()) {
            if (fieldEntry.getValue().isPrimaryKey()) continue;
            if (RecordColumnUtils.isEmpty(recordRow.get(fieldEntry.getKey()))) {
                throw new FieldCheckException(getServiceName(), fieldEntry.getValue(), fieldEntry.getValue().getLabel() + "为必填项");
            }
        }
    }

    /**
     * 更新之前检查，报错 则无法更新
     *
     * @param recordRow
     * @throws AvalonException
     */
    protected void checkBeforeUpdate(RecordRow recordRow) throws AvalonException {

    }

    /**
     * 更新之后检查，报错 则无法更新
     *
     * @param recordRow
     * @throws AvalonException
     */
    protected void checkAfterUpdate(RecordRow recordRow) throws AvalonException {

    }

    /**
     * 更新之前
     *
     * @param recordRow
     * @throws AvalonException
     */

    protected void checkUpdate(RecordRow recordRow) throws AvalonException {
        checkUniqueField(recordRow);
    }

    public List<Object> insertMulti(Record record) throws AvalonException {
        ArrayList<Object> ids = new ArrayList<>();
        for (RecordRow recordRow : record) {
            ids.add(insert(recordRow));
        }
        return ids;
    }

    /**
     * 根据条件更新表，只更新单表
     *
     * @param recordRow
     * @param condition
     * @return
     * @throws AvalonException
     */
    public Integer update(RecordRow recordRow, Condition condition) throws AvalonException {
        if (getNeedDefaultField()) {
            recordRow.put(UPDATE_TIME, new RecordColumn(DateTimeUtils.getCurrentDate()));
            recordRow.put(UPDATER, new RecordColumn(context.getUserId()));
        }
        FieldValueStatement fieldValueStatement = new FieldValueStatement();
        StringBuilder sql = DataBaseTools.updateSql(getService(), recordRow, condition, fieldValueStatement);

        AvalonPreparedStatement avalonPreparedStatement =
                new AutoKeyPreparedStatement(getService(), sql, fieldValueStatement);

        return jdbcTemplate.update(avalonPreparedStatement);
    }

    public Integer update(RecordRow recordRow) throws AvalonException {
        if (getNeedDefaultField()) {
            recordRow.put(UPDATE_TIME, new RecordColumn(DateTimeUtils.getCurrentDate()));
            recordRow.put(UPDATER, new RecordColumn(context.getUserId()));
        }
        checkBeforeUpdate(recordRow);
        // 判断是否可以更新
        checkUpdate(recordRow);
        FieldValueStatement fieldValueStatement = new FieldValueStatement();
        StringBuilder sql = DataBaseTools.updateSql(getService(), recordRow, fieldValueStatement);

        AvalonPreparedStatement avalonPreparedStatement =
                new AutoKeyPreparedStatement(getService(), sql, fieldValueStatement);
        Integer count = jdbcTemplate.update(avalonPreparedStatement);
        updateLog(recordRow);//记录日志
        FieldHashMap relationFieldMap = getRelationFieldMap();
        for (Map.Entry<String, Field> fieldEntry : relationFieldMap.entrySet()) {//处理子表
            String key = fieldEntry.getKey();
            RelationField field = (RelationField) fieldEntry.getValue();

            RecordColumn recordColumn = recordRow.get(key);
            if (ObjectUtils.isNull(recordColumn)) continue;

            if (field instanceof One2manyField) {
                AbstractService serviceBean =
                        context.getServiceBean(field.getRelativeServiceName());
                String relativeFieldName = field.getRelativeFieldName();
                Record record = recordColumn.getRecord();

                record.putAll(relativeFieldName, getPrimaryKeyValue(recordRow));//子表外键赋值

                for (RecordRow row : record) {
                    ServiceOperateEnum op = serviceBean.getOp(row);
                    switch (op) {
                        case insert -> serviceBean.insert(row);
                        case update -> serviceBean.update(row);
                        case del, delete -> serviceBean.delete(row);
                    }
                }
            } else if (field instanceof Many2manyField) {
                Many2manyField many2manyField = (Many2manyField) field;
                AbstractService serviceBean = many2manyField.getRealService();
                String masterForeignKeyName = many2manyField.getMasterForeignKeyName();
                Record record = recordColumn.getRecord();
                record.putAll(masterForeignKeyName, getPrimaryKeyValue(recordRow));
                for (RecordRow row : record) {
                    ServiceOperateEnum op = serviceBean.getOp(row);
                    switch (op) {
                        case insert -> serviceBean.insert(row);
                        case update -> serviceBean.update(row);
                        case del, delete -> serviceBean.delete(row);
                    }
                }
            } else if (field instanceof One2oneField) {
                AbstractService serviceBean =
                        context.getServiceBean(field.getRelativeServiceName());
                String relativeFieldName = field.getRelativeFieldName();
                RecordRow recordRow1 = recordColumn.getRecordRow();
                recordRow1.put(relativeFieldName, getPrimaryKeyValue(recordRow));
                ServiceOperateEnum op = serviceBean.getOp(recordRow1);
                switch (op) {
                    case insert -> serviceBean.insert(recordRow1);
                    case update -> serviceBean.update(recordRow1);
                    case del, delete -> serviceBean.delete(recordRow1);
                }
            }
        }

        checkAfterUpdate(recordRow);
        return count;
    }


    /**
     * 获取主键的值
     *
     * @param row
     * @return
     */
    public Object getPrimaryKeyValue(RecordRow row) {
        if (getPrimaryKeyField() instanceof BigIntegerField) {
            return row.get(getPrimaryKeyName()).getLong();
        }

        return row.get(getPrimaryKeyName()).getInteger();
    }

    public Object getPrimaryKeyValue(PrimaryKey primaryKey) {
        if (getPrimaryKeyField() instanceof BigIntegerField) {
            return primaryKey.getLong();
        }

        return primaryKey.getInteger();
    }

    private List<Field> fieldList;

    public List<Field> getFields() {
        if (!ObjectUtils.isNull(fieldList)) {
            return fieldList;
        }
        synchronized (this) {
            if (!ObjectUtils.isNull(fieldList)) {
                return fieldList;
            }
            fieldList = new ArrayList<>();
            for (Map.Entry<String, Field> item : getFieldMap().entrySet()) {
                fieldList.add(item.getValue());
            }
            return fieldList;
        }
    }

    public Integer updateMulti(Record record) throws AvalonException {
        Integer count = 0;
        for (RecordRow recordRow : record) {
            count += update(recordRow);
        }
        return count;
    }

    public abstract String getServiceName();

    public String getServiceTableName() {
        if (!isInherit()) {
            return Fields.dot2UnderscoreName(getServiceName());
        }
        return Fields.dot2UnderscoreName(getInheritTable());
    }

    private String primaryKeyName;//


    public String getPrimaryKeyName() {
        if (!StringUtils.isEmpty(primaryKeyName)) {
            return primaryKeyName;
        }

        synchronized (this) {
            if (!StringUtils.isEmpty(primaryKeyName)) {
                return primaryKeyName;
            }
            for (Map.Entry<String, Field> item : getFieldMap().entrySet()) {
                if (item.getValue().isPrimaryKey()) {
                    primaryKeyName = item.getKey();
                    break;
                }
            }
            return primaryKeyName;
        }
    }

    public Type getPrimaryKeyType() {
        return getFieldMap().get(getPrimaryKeyName()).getFieldType();
    }

    public Field getPrimaryKeyField() {
        return getFieldMap().get(getPrimaryKeyName());
    }

    public List<String> clearRelationField(List<String> fields) {
        List<String> list = new ArrayList<>();
        for (String s : fields) {
            Field field = getField(s);
            if (field instanceof One2manyField) {
                continue;
            } else if (field instanceof One2oneField) {
                continue;
            }
            list.add(s);
        }
        return list;
    }

    public List<String> clearRelationField(String fields) {
        if (StringUtils.isEmpty(fields)) return null;
        return clearRelationField(List.of(fields.split(",")));
    }

    /**
     * 默认不包括One2manyField和One2oneField
     *
     * @return
     */
    public List<String> getAllFieldName() {
        return getAllFieldName(false);
    }

    /**
     * 带上关联字段
     *
     * @param withRelation
     * @return
     */
    public List<String> getAllFieldName(Boolean withRelation) {
        List<String> fieldNames = new ArrayList<>();

        for (Map.Entry<String, Field> item : getFieldMap().entrySet()) {
            if (item.getValue() instanceof One2manyField) {
                if (!withRelation) continue;
            } else if (item.getValue() instanceof Many2manyField) {
                if (!withRelation) continue;
            } else if (item.getValue() instanceof One2oneField) {
                if (!withRelation) continue;
            }
            fieldNames.add(item.getKey());
        }

        return fieldNames;
    }

    /**
     * 创建数据库表
     */
    public void createTable() {
        if (!isInherit()) { // 表继承，共用一张表，业务模型不同
            doCreateTable();
            return;
        }
        if (existTable()) {
            upgradeTable();
            return;
        }
        doCreateTable();
    }

    private void doCreateTable() {
        if (getContext().isPostgres()) { // postgresql
            Field primaryKeyField = getPrimaryKeyField();
            if (ObjectUtils.isNotNull(primaryKeyField)) {
                String postgresSequenceTable = primaryKeyField.createPostgresSequenceSql();
                jdbcTemplate.execute(new StringBuilder(postgresSequenceTable));
            }
        }
        StringBuilder tableSql = DataBaseTools.createTableSql(getService());
        jdbcTemplate.execute(tableSql);

        // 创建关联表
        List<Field> fields = getFields();
        for (Field field : fields) {
            if (field instanceof Many2manyField) {
                Many2manyField manyField = (Many2manyField) field;
                manyField.createTable();
            }
        }
    }

    /**
     * 创建字段信息
     *
     * @param serviceId 表id
     */
    public void insertFieldInfo(PrimaryKey serviceId) {
        List<Field> fields = getFields();
        for (Field field : fields) {
            field.insertFieldInfo(serviceId);
        }
    }

    /**
     * 创建表记录信息
     */
    public PrimaryKey insertTableInfo(PrimaryKey moduleId) {
        RecordRow row = RecordRow.build();
        row.put("moduleId", getPrimaryKeyValue(moduleId));
        row.put("name", getServiceName());
        row.put("tableName", getServiceTableName());
        row.put("label", getLabel());
        row.put("nameField", getNameField().getName());
        row.put("keyField", getPrimaryKeyName());
        return context.getServiceBean("base.service").insert(row);
    }

    public PrimaryKey upgradeTableInfo(PrimaryKey moduleId) {
        Record serviceInfo = context.getServiceBean("base.service").select(
                Condition.equalCondition("name", getServiceName()), "id");
        if (serviceInfo.isEmpty()) {
            return insertTableInfo(moduleId);
        } else {
            RecordRow row = serviceInfo.get(0);
            row.put("moduleId", getPrimaryKeyValue(moduleId));
            row.put("name", getServiceName());
            row.put("tableName", getServiceTableName());
            row.put("label", getLabel());
            row.put("nameField", getNameField().getName());
            row.put("keyField", getPrimaryKeyName());
            context.getServiceBean("base.service").update(row);
            return new PrimaryKey(row.getRawValue("id"));
        }
    }

    public void dropTable() {
        StringBuilder tableSql = DataBaseTools.dropTableSql(getService());
        jdbcTemplate.execute(tableSql);
        if (getContext().isPostgres()) { // postgresql
            Field primaryKeyField = getPrimaryKeyField();
            if (ObjectUtils.isNotNull(primaryKeyField)) {
                String postgresSequenceTable = primaryKeyField.dropPostgresSequenceSql();
                jdbcTemplate.execute(new StringBuilder(postgresSequenceTable));
            }
        }
        List<Field> fields = getFields();
        for (Field field : fields) {
            if (field instanceof Many2manyField) {
                Many2manyField manyField = (Many2manyField) field;
                manyField.dropTable();
            }
        }
    }

    protected Boolean existTable() {
        StringBuilder sql = DataBaseTools.existTable(getService());
        return jdbcTemplate.executeScalar(sql, Integer.class) != 0;
    }

    protected Boolean existField(Field field) {
        StringBuilder sql = DataBaseTools.existField(getService(), field);
        log.debug("existField sql:{}", sql);
        return jdbcTemplate.executeScalar(sql, Integer.class) != 0;
    }

    protected Boolean existField(String fieldName) {
        Field field = getField(fieldName);
        return existField(field);
    }

    /**
     * 升级表结构
     */
    public void upgradeTable() {
        if (existTable()) {
            List<Field> fields = getFields();

            for (Field field : fields) {
                if (field.isPrimaryKey()) continue;
                if (field instanceof Many2manyField) {
                    ((Many2manyField) field).createTable();
                    continue;
                }
                if (field instanceof One2manyField || field instanceof One2oneField) continue;
                StringBuilder sql = new StringBuilder();
                if (existField(field)) {
                    sql.append(DataBaseTools.modifyColumn(getService(), field));
                } else {
                    sql.append(DataBaseTools.addColumn(getService(), field));
                }
                log.debug("db {} upgradeTable sql:{}", context.getBaseName(), sql);
                jdbcTemplate.execute(sql);
            }

        } else {
            createTable();
        }
    }

    public RecordRow getDefaultValueAll() {

        RecordRow recordRow = new RecordRow();
        getFieldMap().forEach((s, field) -> {
            if (field.getName().equals(CREATE_TIME) ||
                    field.getName().equals(CREATOR) ||
                    field.getName().equals(UPDATE_TIME) ||
                    field.getName().equals(UPDATER) ||
                    field.getName().equals(NAME) ||
                    field.getName().equals(ID) ||
                    field instanceof RelationField
            ) {
                return;
            }
            if (ObjectUtils.isNotNull(field.getDefaultValue())) {
                if (ObjectUtils.isNotNull(field.getDefaultValue().getDefaultValue())) {
                    recordRow.put(field.getName(), field.getDefaultValue().getDefaultValue());
                } else if (StringUtils.isNotEmpty(field.getDefaultValue().getDefaultString())) {
                    recordRow.put(field.getName(), field.getDefaultValue().getDefault(getContext()));
                }
            }
        });
        return recordRow;
    }

    public FieldHashMap getIsRequiredAll() {
        FieldHashMap requiredFields = new FieldHashMap();
        getFieldMap().forEach((s, field) -> {
            if (field.getFieldName().equals(UPDATE_TIME) || field.getFieldName().equals(UPDATER)) return;
            if (field.isRequired()) {
                requiredFields.put(s, field);
            }
        });
        return requiredFields;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public AbstractService getServiceBean(String serviceName) {
        return context.getServiceBean(serviceName);
    }

    public AbstractService getService() {
        return context.getServiceBean(getServiceName());
    }

    @Override
    public String getAlias(DefaultAliasSupport defaultAliasSupport) {
        return defaultAliasSupport.getAlias(getService());
    }

    /**
     * 获取字段的总和 只能是数值字段
     *
     * @param field
     * @param condition
     * @return
     */
    public FieldValue getSumFieldValue(Field field, Condition condition) {
        return getSumFieldValue(field.getName(), condition);
    }

    /**
     * 获取字段的总和 只能是数值字段
     *
     * @param fieldName
     * @param condition
     * @return
     */
    public FieldValue getSumFieldValue(String fieldName, Condition condition) {
        SelectBuilder selectBuilder = DataBaseTools.selectSql(this.getService(),
                new String[]{fieldName}, condition);
        QueryStatement sumSql = selectBuilder.getSumSql();
        AvalonPreparedStatement avalonPrepareStatement = createAvalonPrepareStatement(sumSql.getSql(),
                selectBuilder.getFieldValueStatement());
        Record record = jdbcTemplate.select(avalonPrepareStatement, selectBuilder);

        if (record.isEmpty()) {
            return FieldValue.build(0);
        }
        RecordColumn recordColumn = record.get(0).get(FieldUtils.getJoinDisplayString(fieldName));
        return ObjectUtils.isNull(recordColumn) || recordColumn.isNull() ?
                FieldValue.build(0) : recordColumn;
    }


    @Override
    public void updateNumberValue(RecordRow recordRow) {
        FieldValueStatement fieldValueStatement = new FieldValueStatement();
        StringBuilder sql = DataBaseTools.updateNumberSql(getService(), recordRow, null, fieldValueStatement);

        AvalonPreparedStatement avalonPreparedStatement =
                new AutoKeyPreparedStatement(getService(), sql, fieldValueStatement);
        jdbcTemplate.update(avalonPreparedStatement);
    }

    @Override
    public void updateNumberValue(RecordRow recordRow, Condition condition) {
        FieldValueStatement fieldValueStatement = new FieldValueStatement();
        StringBuilder sql = DataBaseTools.updateNumberSql(getService(), recordRow, condition, fieldValueStatement);

        AvalonPreparedStatement avalonPreparedStatement =
                new AutoKeyPreparedStatement(getService(), sql, fieldValueStatement);
        jdbcTemplate.update(avalonPreparedStatement);
    }

    @Override
    public Object getLock(Map mapTable, Object key) {
        if (mapTable.containsKey(key)) {
            return mapTable.get(key);
        }
        synchronized (mapTable) {
            if (mapTable.containsKey(key)) {
                return mapTable.get(key);
            }
            Object lock = new Object();
            mapTable.put(key, lock);
            return lock;
        }
    }

    private FieldHashMap fieldFormatMap;

    @Override
    public FieldHashMap getFieldFormatMap() {
        if (ObjectUtils.isNull(fieldFormatMap) || fieldFormatMap.isEmpty()) {
            synchronized (this) {
                if (ObjectUtils.isNull(fieldFormatMap) || fieldFormatMap.isEmpty()) {//双重判断
                    if (ObjectUtils.isNull(fieldFormatMap)) {
                        fieldFormatMap = new FieldHashMap();
                    }
                    FieldHashMap fieldMap1 = getFieldMap();
                    fieldMap1.forEach((fieldName, field) -> {
                        if (field instanceof IFieldFormat) {
                            fieldFormatMap.put(fieldName, field);
                        }
                    });
                }
            }
        }
        return fieldFormatMap;
    }

    /**
     * @return
     */
    @Override
    public Boolean isInherit() {
        return false;
    }

    /**
     * 每个子类需要实现该方法，返回实际的表名
     *
     * @return 表名
     */
    @Override
    public String getInheritTable() {
        Class<?> superclass = this.getClass().getSuperclass();// 获取父类
        if (superclass.equals(AbstractService.class)) { // 如果父类是AbstractService, 则直接返回当前表名
            return getServiceName();
        }
        AbstractService serviceBean = (AbstractService) getContext().getServiceBean(superclass); // 父类实例
        return serviceBean.getInheritTable();
    }

    private AbstractModule module;

    @Override
    public AbstractModule getModule() {
        return module;
    }

    @Override
    public void setModule(AbstractModule module) {
        this.module = module;
        context.putModule(module.getModuleName(), this);
    }


    /**  以下是查询接口 **/

    /**
     * 查询主键集合
     *
     * @param condition
     * @return
     * @throws AvalonException
     */
    @Override
    public Record search(Condition condition) throws AvalonException {
        return select(condition, getPrimaryKeyName());
    }

    /**
     * 统计个数
     *
     * @param condition
     * @return
     * @throws AvalonException
     */
    @Override
    public Integer selectCount(Condition condition) throws AvalonException {
        SelectBuilder selectBuilder = DataBaseTools.selectCountSql(getService(),
                new String[]{getPrimaryKeyName()},
                condition,
                "",
                true);
        QueryStatement countSql = selectBuilder.getCountSql();
        return jdbcTemplate.selectCount(countSql.getSql(), countSql.getValueStatement());
    }

    @Override
    public Record select(Condition condition, String... fields) throws AvalonException {
        return select("", condition, fields);
    }

    @Override
    public Record select(String order, Condition condition, String... fields) throws AvalonException {
        SelectBuilder selectBuilder = DataBaseTools.selectSql(getService(),
                fields,
                condition,
                order);
        QueryStatement masterSql = selectBuilder.getMasterSql();
        AvalonPreparedStatement avalonPrepareStatement = createAvalonPrepareStatement(masterSql.getSql(),
                masterSql.getValueStatement());
        Record record = jdbcTemplate.select(avalonPrepareStatement, selectBuilder);
        List<Object> ids = record.getValues(getPrimaryKeyName());
        for (QueryNode node : selectBuilder.getQueryRoot().getQueryNodeList()) {
            if (ObjectUtils.isNotEmpty(node.getFields()) || !node.getQueryNodeList().isEmpty()) {
                doQueryNode(node, ids, record);
            }
        }
        return record;
    }

    @Override
    public Record select(Integer limit, String order, Condition condition, String... fields) throws AvalonException {
        SelectBuilder selectBuilder = DataBaseTools.selectSql(getService(),
                fields,
                condition,
                order);
        QueryStatement masterSql = selectBuilder.getMasterSql(limit);
        AvalonPreparedStatement avalonPrepareStatement = createAvalonPrepareStatement(masterSql.getSql(),
                masterSql.getValueStatement());
        Record record = jdbcTemplate.select(avalonPrepareStatement, selectBuilder);
        List<Object> ids = record.getValues(getPrimaryKeyName());
        for (QueryNode node : selectBuilder.getQueryRoot().getQueryNodeList()) {
            if (ObjectUtils.isNotEmpty(node.getFields()) || !node.getQueryNodeList().isEmpty()) {
                doQueryNode(node, ids, record);
            }
        }
        return record;
    }

    @Override
    public PageInfo selectPage(PageParam pageParam,
                               String order,
                               Condition condition,
                               String... fields) throws AvalonException {

        Integer size = context.getApplicationConfig().getPageSize();
        if (ObjectUtils.isNotNull(pageParam.getPageSize())) {
            size = pageParam.getPageSize();
        }
        SelectBuilder selectBuilder = DataBaseTools.selectCountSql(getService(),
                fields,
                condition,
                order,
                true);
        QueryStatement countSql = selectBuilder.getCountSql();
        Integer total = jdbcTemplate.selectCount(countSql.getSql(), countSql.getValueStatement());

        SelectPageBuilder selectPageBuilder = new SelectPageBuilder(selectBuilder, pageParam.getPageNum(), size);

        QueryStatement masterSql = selectPageBuilder.getMasterSql();

        AvalonPreparedStatement avalonPrepareStatement = createAvalonPrepareStatement(masterSql.getSql(),
                masterSql.getValueStatement());
        Record record = jdbcTemplate.select(avalonPrepareStatement, selectPageBuilder);
        List<Object> ids = record.getValues(getPrimaryKeyName());
        for (QueryNode node : selectPageBuilder.getQueryRoot().getQueryNodeList()) {
            if (ObjectUtils.isNotEmpty(node.getFields()) || !node.getQueryNodeList().isEmpty()) {
                doQueryNode(node, ids, record);
            }
        }
        return new PageInfo(record, total, pageParam.getPageNum(), size);
    }

    @Override
    public FieldValue getFieldValue(String fieldName, Condition condition) {
        Record select = select(condition, fieldName);
        if (select.isEmpty()) {
            return FieldValue.build(null);
        }
        return select.get(0).get(fieldName);
    }

    @Override
    public Object invokeMethod(String methodName, List<Object> ids, RecordRow row) {
        try {
            Method method = this.getClass().getMethod(methodName, List.class, RecordRow.class);
            return method.invoke(this, ids, row);
        } catch (NoSuchMethodException e) {
            throw new AvalonException(methodName + "不存在方法");
        } catch (InvocationTargetException | IllegalAccessException e) {
            String message = e.getMessage();
            if (e instanceof InvocationTargetException) {
                message = ((InvocationTargetException) e).getTargetException().getMessage();
            }
            throw new AvalonException(message, e);
        }
    }
}
