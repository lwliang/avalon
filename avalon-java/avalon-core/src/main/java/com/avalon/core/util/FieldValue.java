/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.util;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.IFieldFormat;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class FieldValue implements Serializable, IFieldFormat {
    /**
     * 创建fieldValue
     *
     * @param value 值
     * @return FieldValue
     */
    public static FieldValue build(Object value) {
        return factory(value);
    }

    private Object value;

    private IFieldFormat field;

    public void setField(IFieldFormat field) {
        this.field = field;
    }

    public IFieldFormat getField() {
        return this.field;
    }

    public FieldValue(Object value) {
        this.value = value;
    }

    public static FieldValue factory(Object value) {
        return new FieldValue(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getInteger() {
        return Integer.parseInt(getString());
    }

    public Record getRecord() {
        return (Record) value;
    }

    public RecordRow getRecordRow() {
        return (RecordRow) value;
    }

    public String getString() {
        return value.toString();
    }

    public BigDecimal getBigDecimal() {
        return new BigDecimal(getString().trim());
    }

    public Date getDate() {
        if (value instanceof Date) {
            return (Date) value;
        }
        if(value instanceof Long) {
            return new Date((Long) value);
        }
        if (value instanceof String) {
            if(ObjectUtils.isNotNull(field) ) {
                return (Date) field.parse(value);
            }
        }
        return (Date) value;
    }

    public Timestamp getTimestamp() {
        if(value instanceof Long) {
            return new Timestamp((Long) value);
        }
        return (Timestamp) value;
    }

    public Float getFloat() {
        return Float.parseFloat(getString().trim());
    }

    public Double getDouble() {
        return Double.parseDouble(getString().trim());
    }

    public Long getLong() {
        return Long.parseLong(getString());
    }

    public Boolean getBoolean() {
        return Boolean.parseBoolean(getString());
    }

    public <T extends Enum<T>> T getValueOfEnum(Class<T> t) {
        return T.valueOf(t, getString());
    }

    public FieldValue incrInteger(Integer incr) {
        return FieldValue.factory(getInteger() + incr);
    }

    public FieldValue incrString(String incr) {
        return factory(getString() + incr);
    }

    public FieldValue incrBigDecimal(BigDecimal incr) {
        return factory(getBigDecimal().add(incr));
    }

    public FieldValue incrDouble(Double incr) {
        return factory(getDouble() + incr);
    }

    public FieldValue incrFloat(Float incr) {
        return factory(getFloat() + incr);
    }

    public FieldValue incrLong(Long incr) {
        return factory(getLong() + incr);
    }

    public FieldValue decrInteger(Integer decr) {
        return factory(getInteger() - decr);
    }

    public FieldValue decrBigDecimal(BigDecimal decr) {
        return factory(getBigDecimal().subtract(decr));
    }

    public FieldValue decrDouble(Double decr) {
        return factory(getDouble() - decr);
    }

    public FieldValue decrFloat(Float decr) {
        return factory(getFloat() - decr);
    }

    public FieldValue decrLong(Long decr) {
        return factory(getLong() - decr);
    }

    public FieldValue multiplyInteger(Integer multi) {
        return factory(getInteger() * multi);
    }

    public FieldValue multiplyBigDecimal(BigDecimal multi) {
        return factory(getBigDecimal().multiply(multi));
    }

    public FieldValue multiplyDouble(Double multi) {
        return factory(getDouble() * multi);
    }

    public FieldValue multiplyFloat(Float multi) {
        return factory(getFloat() * multi);
    }

    public FieldValue multiplyLong(Long multi) {
        return factory(getLong() * multi);
    }

    public FieldValue divideInteger(Integer divide) {
        return factory(getInteger() / divide);
    }

    public FieldValue divideBigDecimal(BigDecimal divide) {
        return factory(getBigDecimal().divide(divide));
    }

    public FieldValue divideDouble(Double divide) {
        return factory(getDouble() / divide);
    }

    public FieldValue divideFloat(Float divide) {
        return factory(getFloat() / divide);
    }

    public FieldValue divideLong(Long divide) {
        return factory(getLong() / divide);
    }

    @Override
    public String toString() {
        if (ObjectUtils.isNotNull(value)) {
            Object sqlValue = getSqlValue(value);
            if(ObjectUtils.isNull(sqlValue)) return null;
            return sqlValue.toString();
        }
        return null;
    }


    /**
     * 判断值是否为空
     */
    public Boolean isNull() {
        return ObjectUtils.isNull(value);
    }


    /**
     * 判断值不为空
     */
    public Boolean isNotNull() {
        return !isNull();
    }

    /**
     * 判断值是否为空
     *
     * @return
     */
    public Boolean isEmpty() {
        if (ObjectUtils.isNull(value)) {
            return true;
        }

        return value instanceof String && StringUtils.isEmpty(getString());
    }

    public Boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public String getFormat() {
        if (ObjectUtils.isNotNull(field)) {
            return field.getFormat();
        }
        return null;
    }

    @Override
    public Object parse(Object value) throws AvalonException {
        if (ObjectUtils.isNotNull(field)) {
            return field.parse(value);
        }
        return null;
    }

    @Override
    public Object getSqlValue(Object value) {
        if (ObjectUtils.isNotNull(field)) {
            return field.getSqlValue(value);
        }
        return IFieldFormat.super.getSqlValue(value);
    }

    public Object getSqlValue() {
        return getSqlValue(value);
    }

    public Object getClientValue(Object value) {
        if (ObjectUtils.isNotNull(field)) {
            return field.getClientValue(value);
        }
        return IFieldFormat.super.getClientValue(value);
    }

    public Object getClientValue() {
        return getClientValue(value);
    }
}
