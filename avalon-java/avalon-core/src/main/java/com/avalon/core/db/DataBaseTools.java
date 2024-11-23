/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.db;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.*;
import com.avalon.core.model.*;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.select.builder.SelectPageBuilder;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class DataBaseTools {
    public static SelectBuilder selectSql(AbstractService service,
                                          String[] fields,
                                          Condition condition) {
        return selectAllSql(service, fields,
                condition, null, true);
    }

    public static SelectBuilder selectSql(AbstractService service,
                                          String[] fields,
                                          Condition condition,
                                          String orderBy) {
        return selectAllSql(service,
                fields,
                condition,
                orderBy,
                true);
    }

    /**
     * 获取所有记录
     *
     * @param service
     * @param fields
     * @param condition
     * @return
     */
    public static SelectBuilder selectAllSql(AbstractService service,
                                             String[] fields,
                                             Condition condition,
                                             String order,
                                             Boolean isDistinct) {
        SelectBuilder selectBuilder = new SelectBuilder(service);
        selectBuilder.setDistinct(isDistinct);
        for (String s : fields) {
            selectBuilder.addField(s);
        }

        selectBuilder.setOrderBy("");
        if (ObjectUtils.isNotNull(order)) {
            selectBuilder.setOrderBy(order);
        }
        if (ObjectUtils.isNotNull(condition)) {
            selectBuilder.setCondition(condition);
            selectBuilder.completeTable(condition);
        }

        return selectBuilder;
    }

    public static SelectBuilder selectCountSql(AbstractService service,
                                               String[] fields,
                                               Condition condition,
                                               String order,
                                               Boolean isDistinct) {

        return selectAllSql(service, fields, condition, order, isDistinct);
    }

    public static StringBuilder deleteSql(AbstractService service, Object id) {
        return deleteSql(service, ObjectUtils.object2List(id));
    }

    public static StringBuilder deleteSql(AbstractService service, List<Object> ids) {

        StringBuilder sb = new StringBuilder();

        sb.append("DELETE ");
        sb.append("FROM ");
        sb.append(service.getServiceTableName());
        sb.append(" WHERE ");
        sb.append(service.getPrimaryKeyField().getFieldName());
        Field keyField = service.getPrimaryKeyField();
        if (ids.size() == 1) {
            sb.append(" = ");
            sb.append(keyField.getSqlValue(ids.get(0)));
        } else {
            sb.append(" IN (");
            final StringBuilder ins = new StringBuilder();
            ids.forEach(id -> {
                if (ins.length() != 0) {
                    ins.append(",");
                }
                ins.append(keyField.getSqlValue(id));
            });
            sb.append(ins);
            sb.append(")");
        }
        return sb;
    }

    /**
     * 参数化
     *
     * @param service
     * @param valueMap
     * @param fieldValueStatement
     * @return
     */
    public static StringBuilder insertSql(AbstractService service, RecordRow valueMap,
                                          FieldValueStatement fieldValueStatement) {

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(service.getServiceTableName());
        sb.append("(");
        StringBuilder fields = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (RecordRow.Entry<String, RecordColumn> item : valueMap.entrySet()) {

            if (ObjectUtils.isNull(item.getValue())) continue;

            Object value = item.getValue().getValue();
            if (ObjectUtils.isNull(value)) continue;

            Field field = service.getField(item.getKey());
            if (ObjectUtils.isNull(field)) continue;
            if (field.isPrimaryKey() && field.isAutoIncrement()) continue;
            if (field instanceof RelationField) {
                if (field instanceof One2manyField ||
                        field instanceof One2oneField ||
                        field instanceof Many2manyField) {
                    continue;
                }
            }
            if (ObjectUtils.isNull(value)) continue;
            if (fields.length() != 0) {
                fields.append(",");
                values.append(",");
            }
            fields.append(field.getFieldName());

            addSqlParam(fieldValueStatement, values, item.getValue(), value, field);
        }
        sb.append(fields);
        sb.append(")");
        sb.append(" VALUES(");
        sb.append(values);
        sb.append(")");

        return sb;
    }

    private static void addSqlParam(FieldValueStatement fieldValueStatement,
                                    StringBuilder values,
                                    RecordColumn column,
                                    Object value,
                                    Field field) {
        if (ObjectUtils.isNotNull(fieldValueStatement)) {//参数化
            values.append("?");
            if (field instanceof IFieldFormat) {
                if (ObjectUtils.isNotNull(column)) {
                    column.setField((IFieldFormat) field);
                }
            }
            fieldValueStatement.put(field, column);
        } else {
            values.append(value);
        }
    }

    /**
     * 更新值 使用+的方式
     *
     * @param service
     * @param valueMap
     * @param condition
     * @param fieldValueStatement
     * @return
     */
    public static StringBuilder updateNumberSql(AbstractService service, RecordRow valueMap, Condition condition,
                                                FieldValueStatement fieldValueStatement) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE  ");
        sb.append(service.getServiceTableName());
        sb.append(" SET ");
        StringBuilder updateValues = new StringBuilder();

        for (RecordRow.Entry<String, RecordColumn> item : valueMap.entrySet()) {
            Object value = item.getValue().getValue();

            Field field = service.getField(item.getKey());
            if (ObjectUtils.isNull(field)) continue;

            if (field.isPrimaryKey() && field.isAutoIncrement()) continue;
            value = field.getSqlValue(value);
            if (ObjectUtils.isNull(value) && !field.allowNull()) continue;//值为空，并且不能设置为空

            if (field instanceof One2oneField || field instanceof One2manyField) continue;// 如果是子表关联字段则跳过
            if (updateValues.length() != 0) {
                updateValues.append(",");
            }
            updateValues.append(field.getFieldName());
            updateValues.append("=");
            updateValues.append(field.getFieldName());
            updateValues.append("+");
            addSqlParam(fieldValueStatement, updateValues,
                    item.getValue(), value, field);
        }

        sb.append(updateValues);

        Field keyField = service.getPrimaryKeyField();//设置主键条件
        RecordColumn recordColumn = valueMap.get(keyField.getName());
        if (ObjectUtils.isNull(recordColumn) && ObjectUtils.isNull(condition)) {
            throw new AvalonException("没有主键或更新条件");
        }
        if (ObjectUtils.isNotNull(condition)) {
            if (ObjectUtils.isNotNull(recordColumn)) {
                condition = condition.andCondition(Condition.equalCondition(service.getPrimaryKeyName(), recordColumn.getValue()));
            }
        } else {
            condition = Condition.equalCondition(service.getPrimaryKeyName(), recordColumn.getValue());
        }

        sb.append(" WHERE ");

        String sql = condition.getSql(service, fieldValueStatement);

        sb.append(sql);

        return sb;
    }

    /**
     * 参数化传惨
     *
     * @param service             服务
     * @param valueMap            值
     * @param condition           更新条件
     * @param fieldValueStatement 参数化
     * @return 影响的行数
     * @throws AvalonException 异常
     */
    public static StringBuilder updateSql(AbstractService service,
                                          RecordRow valueMap,
                                          Condition condition,
                                          FieldValueStatement fieldValueStatement) throws AvalonException {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE  ");
        sb.append(service.getServiceTableName());
        sb.append(" SET ");
        StringBuilder updateValues = new StringBuilder();

        for (RecordRow.Entry<String, RecordColumn> item : valueMap.entrySet()) {
            Object value = null;
            if (ObjectUtils.isNotNull(item) && ObjectUtils.isNotNull(item.getValue())) {
                value = item.getValue().getValue();
            }

            Field field = service.getField(item.getKey());
            if (ObjectUtils.isNull(field)) continue;

            if (field.isPrimaryKey() && field.isAutoIncrement()) continue;
            value = field.getSqlValue(value);
            if (ObjectUtils.isNull(value) && !field.allowNull()) continue;//值为空，并且不能设置为空

            if (field instanceof One2oneField || field instanceof One2manyField || field instanceof Many2manyField)
                continue;// 如果是子表关联字段则跳过
            if (updateValues.length() != 0) {
                updateValues.append(",");
            }
            updateValues.append(field.getFieldName());
            updateValues.append("=");
            addSqlParam(fieldValueStatement, updateValues,
                    item.getValue(), value, field);
        }

        sb.append(updateValues);

        Field keyField = service.getPrimaryKeyField();//设置主键条件
        RecordColumn recordColumn = valueMap.get(keyField.getName());
        if (ObjectUtils.isNull(recordColumn) && ObjectUtils.isNull(condition)) {
            throw new AvalonException("没有主键或更新条件");
        }
        if (ObjectUtils.isNotNull(condition)) {
            if (ObjectUtils.isNotNull(recordColumn)) {
                condition = condition.andCondition(Condition.equalCondition(service.getPrimaryKeyName(), recordColumn.getValue()));
            }
        } else {
            condition = Condition.equalCondition(service.getPrimaryKeyName(), recordColumn.getValue());
        }

        sb.append(" WHERE ");

        String sql = condition.getSql(service, fieldValueStatement);

        sb.append(sql);

        return sb;
    }

    /**
     * 参数化传惨
     *
     * @param service             服务
     * @param valueMap            值
     * @param fieldValueStatement 参数化
     * @return 影响的行数
     * @throws AvalonException
     */
    public static StringBuilder updateSql(AbstractService service, RecordRow valueMap,
                                          FieldValueStatement fieldValueStatement) throws AvalonException {
        return updateSql(service, valueMap, null, fieldValueStatement);
    }

    /**
     * 创建表
     *
     * @param service
     * @return
     */
    public static StringBuilder createTableSql(AbstractService service) {
        FieldHashMap fieldMap = service.getFieldMap();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
        stringBuilder.append(service.getServiceTableName());
        stringBuilder.append(" ( ");
        Integer count = 1;
        Field primaryKeyField = service.getPrimaryKeyField();
        stringBuilder.append(primaryKeyField.getFieldName()).append(" ").append(primaryKeyField.getCreateTableSql());
        for (FieldHashMap.Entry<String, Field> fieldEntry : fieldMap.entrySet()) {
            if (fieldEntry.getValue() instanceof One2manyField ||
                    fieldEntry.getValue() instanceof One2oneField ||
                    fieldEntry.getValue() instanceof Many2manyField ||
                    fieldEntry.getValue().isPrimaryKey()) {
                continue;
            }//关系字段暂停使用
            String sqlDataType = null;
            if (fieldEntry.getValue() instanceof Many2oneField) {
                sqlDataType = fieldEntry.getValue().getFieldName() + " " +
                        fieldEntry.getValue().getSampleCreateTableSql();
            } else {
                sqlDataType = fieldEntry.getValue().getFieldName() + " " +
                        fieldEntry.getValue().getCreateTableSql();
            }


            if (!count.equals(0)) {
                stringBuilder.append(",");
            }
            stringBuilder.append(sqlDataType);
            count++;
        }
        stringBuilder.append(" );");

        return stringBuilder;
    }

    public static StringBuilder dropTableSql(AbstractService service) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE IF EXISTS ");
        stringBuilder.append(service.getServiceTableName());
        stringBuilder.append(";");
        return stringBuilder;
    }

    public static StringBuilder existTable(AbstractService service) {
        StringBuilder stringBuilder = new StringBuilder();
        if (service.getContext().isMysql()) {
            stringBuilder.append("SELECT COUNT(TABLE_NAME) ");
            stringBuilder.append("FROM INFORMATION_SCHEMA.TABLES");
            stringBuilder.append(" where TABLE_SCHEMA = '" + service.getBaseName() + "'");
            stringBuilder.append(" AND TABLE_NAME = '" + service.getServiceTableName() + "'");
        } else {
            stringBuilder.append("select COUNT(tablename) ");
            stringBuilder.append("from pg_tables where schemaname = 'public'");
            stringBuilder.append(" and tablename  = '" + service.getServiceTableName() + "'");
        }
        return stringBuilder;
    }

    public static StringBuilder existField(AbstractService service, Field field) {
        StringBuilder stringBuilder = new StringBuilder();
        if (service.getContext().isMysql()) {
            stringBuilder.append("SELECT COUNT(COLUMN_NAME) ");
            stringBuilder.append("FROM INFORMATION_SCHEMA.columns");
            stringBuilder.append(" where TABLE_SCHEMA = '" + service.getBaseName() + "'");
            stringBuilder.append(" AND TABLE_NAME = '" + service.getServiceTableName() + "'");
            stringBuilder.append(" AND column_name = '" + field.getFieldName() + "'");
        } else {
            stringBuilder.append("select COUNT(column_name) ");
            stringBuilder.append("from information_schema.columns ");
            stringBuilder.append("where table_name = '" + service.getServiceTableName() + "'");
            stringBuilder.append(" and column_name = '" + field.getFieldName() + "'");
        }
        return stringBuilder;
    }

    public static StringBuilder addColumn(AbstractService service, Field field) {
        StringBuilder sql = new StringBuilder();

        sql.append("ALTER TABLE ");
        sql.append(service.getServiceTableName());
        sql.append(" ADD COLUMN ");
        sql.append(field.getFieldName());
        sql.append(" ");
        sql.append(field.getCreateTableSql());

        return sql;
    }

    public static StringBuilder modifyColumn(AbstractService service, Field field) {
        StringBuilder sql = new StringBuilder();
        if (service.getContext().isMysql()) {
            sql.append("ALTER TABLE ");
            sql.append(service.getServiceTableName());
            sql.append(" MODIFY COLUMN ");
            sql.append(field.getFieldName());
            sql.append(" ");
            sql.append(field.getSampleCreateTableSql());
        } else {
            sql.append("ALTER TABLE ");
            sql.append(service.getServiceTableName());
            sql.append(" ALTER COLUMN ");
            sql.append(field.getFieldName());
            sql.append(" type ");
            sql.append(field.getSampleCreateTableSql());
        }
        return sql;
    }
}
