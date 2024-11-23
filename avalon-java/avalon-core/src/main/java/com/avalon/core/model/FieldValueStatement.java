/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.model;

import com.avalon.core.field.Field;
import com.avalon.core.util.FieldValue;

import java.util.LinkedList;

public class FieldValueStatement extends LinkedList<FieldValueSql> {

    public void put(Field field, Object value) {
        FieldValueSql fieldValueSql = new FieldValueSql();
        fieldValueSql.setField(field);
        fieldValueSql.setValue(value);
        add(fieldValueSql);
    }

    public void put(Field field, FieldValue fieldValue) {
        FieldValueSql fieldValueSql = new FieldValueSql();
        fieldValueSql.setField(field);
        fieldValueSql.setValue(fieldValue);
        add(fieldValueSql);
    }

    /**
     * 返回值
     *
     * @return
     */
    public Object[] flatValue() {

        Object[] values = new Object[this.size()];
        for (int i = 0; i < this.size(); i++) {
            FieldValueSql fieldValueSql = this.get(i);
            values[i] = fieldValueSql.getField().getSqlValue(fieldValueSql.getValue().getValue());
        }

        return values;
    }

    /**
     * 返回数据库对应的数据库字段
     *
     * @return
     */
    public int[] flatType() {
        int[] values = new int[this.size()];
        for (int i = 0; i < this.size(); i++) {
            FieldValueSql fieldValueSql = this.get(i);
            values[i] = fieldValueSql.getField().getSqlType();
        }
        return values;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("sql param->");
        this.forEach((fieldValueSql) -> {
            builder.append(fieldValueSql.getValue()).append(",");
        });
        return builder.toString();
    }
}
