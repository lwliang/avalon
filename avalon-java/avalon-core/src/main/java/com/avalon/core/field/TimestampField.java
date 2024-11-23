/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.face.field.IFieldDefaultValue;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.sql.Types;

public class TimestampField extends Field implements IFieldFormat {
    public TimestampField(Builder builder) {
        super(builder);
    }

    public TimestampField(String label) {
        super(label);
    }

    public TimestampField(String label, Boolean isRequired) {
        super(label, isRequired);
    }

    public TimestampField(String label, Boolean isRequired, Boolean isReadonly) {
        super(label, isRequired, isReadonly);
    }

    public TimestampField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue) {
        super(label, isRequired, isReadonly, defaultValue);
    }

    public TimestampField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
    }

    @Override
    public Integer getSqlType() {
        return Types.TIMESTAMP;
    }

    @Override
    public Type getFieldType() {
        return Timestamp.class;
    }

    @Override
    public String getFormat() {
        return "";
    }

    @Override
    public Object getClientValue(Object value) {
        if (value instanceof Long) {
            return value;
        }
        return ((Timestamp) value).getTime();
    }

    @Override
    public Object parse(Object value) throws AvalonException {
        if (value instanceof Long) {
            return new Timestamp((Long) value);
        }
        return value;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public Object getReportValue(Object value) {
        return IFieldFormat.super.getReportValue(value);
    }

    @Override
    public String getCreateTableSql() {
        return "timestamp";
    }

    @Override
    public String getSampleCreateTableSql() {
        return "timestamp";
    }

    public static class Builder extends Field.Builder<TimestampField.Builder> {
        @Override
        public TimestampField.Builder setIsUnique(Boolean isUnique) {
            this.isUnique = isUnique;
            return this;
        }

        @Override
        public TimestampField.Builder setAllowNull(Boolean allowNull) {
            this.allowNull = allowNull;
            return this;
        }

        @Override
        public TimestampField.Builder setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        @Override
        public TimestampField.Builder setDefaultValue(IFieldDefaultValue defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        @Override
        public TimestampField.Builder setIsAutoIncrement(Boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
            return this;
        }

        @Override
        public TimestampField.Builder setIsPrimaryKey(Boolean isPrimaryKey) {
            this.isPrimaryKey = isPrimaryKey;
            return this;
        }

        @Override
        public TimestampField.Builder setIsReadonly(Boolean isReadonly) {
            this.isReadonly = isReadonly;
            return this;
        }

        @Override
        public TimestampField.Builder setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        @Override
        public TimestampField.Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public Field build() {
            return new TimestampField(this);
        }

        public static TimestampField.Builder getInstance() {
            return new Builder();
        }
    }
}
