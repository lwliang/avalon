/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.context.Context;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.util.DateTimeUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;

import java.lang.reflect.Type;
import java.sql.Time;
import java.sql.Types;
import java.util.Date;

public class TimeField extends Field implements IFieldFormat {
    private String format;

    @Override
    public String getFormat() {
        String f = format;
        if (StringUtils.isEmpty(f)) {
            f = Context.getTimeFormat();
        }
        return f;
    }

    protected void setFormat(String format) {
        this.format = format;
    }

    public TimeField(String label) {
        this(label, "");
    }

    @Override
    public Type getFieldType() {
        return Date.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        String f = getFormat();
        if (value instanceof String) {
            return value;
        }
        return DateTimeUtils.getDateTimeFormat(f, (Time) value);
    }

    public TimeField(Builder builder) {
        super(builder);
        setFormat(builder.format);
    }

    public TimeField(String label, String format) {
        this(label, false, format);
    }

    public TimeField(String label, Boolean isRequired, String format) {
        this(label, isRequired, false, format);
    }

    public TimeField(String label, Boolean isRequired, Boolean isReadonly, String format) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getTimeFieldDefaultValue(), format);
    }

    public TimeField(String label, Boolean isRequired, Boolean isReadonly,
                     IFieldDefaultValue defaultValue, String format) {
        this(label, isRequired, isReadonly, defaultValue, format, false, false);
    }

    public TimeField(String label, Boolean isRequired, Boolean isReadonly,
                     IFieldDefaultValue defaultValue, String format,
                     Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
        this.setFormat(format);
    }

    @Override
    public Object parse(Object value) throws AvalonException {
        String f = getFormat();
        if (ObjectUtils.isEmpty(value)) return null;
        return DateTimeUtils.parseTime(f, value.toString());
    }

    @Override
    public String getCreateTableSql() {
        return isMySql() ? "VARCHAR(8)" : "TIME";
    }

    @Override
    public String getSampleCreateTableSql() {
        return getCreateTableSql();
    }

    @Override
    public Integer getSqlType() {
        return Types.VARCHAR;
    }

    public static class Builder extends Field.Builder<Builder> {
        private String format;

        @Override
        public Builder setIsUnique(Boolean isUnique) {
            this.isUnique = isUnique;
            return this;
        }

        @Override
        public Builder setAllowNull(Boolean allowNull) {
            this.allowNull = allowNull;
            return this;
        }

        @Override
        public Builder setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        @Override
        public Builder setDefaultValue(IFieldDefaultValue defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        @Override
        public Builder setIsAutoIncrement(Boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
            return this;
        }

        @Override
        public Builder setIsPrimaryKey(Boolean isPrimaryKey) {
            this.isPrimaryKey = isPrimaryKey;
            return this;
        }

        @Override
        public Builder setIsReadonly(Boolean isReadonly) {
            this.isReadonly = isReadonly;
            return this;
        }

        @Override
        public Builder setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        @Override
        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public Field build() {
            return new TimeField(this);
        }

        public Builder setFormat(String format) {
            this.format = format;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
