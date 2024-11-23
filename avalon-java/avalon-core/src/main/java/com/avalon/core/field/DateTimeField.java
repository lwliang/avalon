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
import java.sql.Types;
import java.util.Date;

public class DateTimeField extends Field implements IFieldFormat {

    private String format;

    public String getFormat() {
        String f = format;
        if (StringUtils.isEmpty(f)) {
            f = Context.getDateTimeFormat();
        }
        return f;
    }

    protected void setFormat(String format) {
        this.format = format;
    }

    public DateTimeField(String label) {
        this(label, "");
    }

    public DateTimeField(Builder builder) {
        super(builder);
        setFormat(builder.format);
    }

    @Override
    public Type getFieldType() {
        return Date.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        String f = getFormat();
        if (allowNull() && (ObjectUtils.isNull(value) || StringUtils.isEmpty(value))) {
            return null;
        }
        if (isMySql()) {
            if (value instanceof String) {
                return value;
            }
            return DateTimeUtils.getDateTimeFormat(f, (Date) value);
        } else {
            if (value instanceof String) {
                if (StringUtils.isEmpty(value)) {
                    return null;
                }
                return String.format("TO_TIMESTAMP('%s','YYYY-MM-DD HH24:MI:SS')", value);
            }
            return String.format("TO_TIMESTAMP('%s','YYYY-MM-DD HH24:MI:SS')", DateTimeUtils.getDateTimeFormat(f, (Date) value));
        }
    }

    public Object getClientValue(Object value) {
        if (value instanceof String) {
            return value;
        }
        String f = getFormat();
        if (allowNull() && ObjectUtils.isNull(value)) {
            return null;
        }
        return DateTimeUtils.getDateTimeFormat(getFormat(), (Date) value);
    }

    public DateTimeField(String label, String format) {
        this(label, false, format);
    }

    public DateTimeField(String label, Boolean isRequired) {
        this(label, isRequired, "");
    }

    public DateTimeField(String label, Boolean isRequired, IFieldDefaultValue defaultValue) {
        this(label, isRequired, false, defaultValue, "", false, false);
    }

    public DateTimeField(String label, Boolean isRequired, String format) {
        this(label, isRequired, false, format);
    }

    public DateTimeField(String label, Boolean isRequired, Boolean isReadonly, String format) {
        this(label, isRequired, isReadonly,
                null, format);
    }

    public DateTimeField(String label, Boolean isRequired, Boolean isReadonly,
                         IFieldDefaultValue defaultValue, String format) {
        this(label, isRequired, isReadonly, defaultValue, format, false, false);
    }

    public DateTimeField(String label, Boolean isRequired, Boolean isReadonly,
                         IFieldDefaultValue defaultValue, String format, Boolean isPrimaryKey,
                         Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
        this.setFormat(format);
    }

    @Override
    public Object parse(Object value) throws AvalonException {

        if (ObjectUtils.isNull(value)) return null;

        return DateTimeUtils.parseDate(getFormat(), value.toString());
    }

    @Override
    public String getCreateTableSql() {
        return isMySql() ? "DATETIME" : "TIMESTAMP";
    }

    @Override
    public String getSampleCreateTableSql() {
        return isMySql() ? "DATETIME" : "TIMESTAMP";
    }

    @Override
    public Integer getSqlType() {
        return Types.TIMESTAMP;
    }

    @Override
    public Object getReportValue(Object value) {
        return DateTimeUtils.getDateTimeFormat("yyyy-MM-dd HH:mm", (Date) value);
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
            return new DateTimeField(this);
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
