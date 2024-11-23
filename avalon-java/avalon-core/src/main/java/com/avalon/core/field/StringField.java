/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.face.field.StringFieldDefaultValue;
import com.avalon.core.field.builder.IStringFieldBuilder;
import com.avalon.core.util.ObjectUtils;

import java.lang.reflect.Type;
import java.sql.Types;

public class StringField extends Field {

    private Integer size = SIZE_DEFAULT;//数据库记录的长度
    private Integer maxLength = SIZE_DEFAULT;
    private Integer minLength = 0;

    public final static Integer SIZE_DEFAULT = 255;

    public Integer getSize() {
        return size;
    }

    protected void setSize(Integer size) {
        this.size = size;
        if (ObjectUtils.isNull(this.getMaxLength())) {
            this.setMaxLength(this.size);
        } else if (this.getMaxLength().compareTo(this.size) > 0) {
            this.setMaxLength(this.size);
        }
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    protected void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    protected void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public StringField(Builder builder) {
        super(builder);
        setSize(builder.size);
        setMaxLength(builder.maxLength);
        setMinLength(builder.minLength);
    }

    public StringField(String label) {
        this(label, false, SIZE_DEFAULT);
    }

    public StringField(String label, Boolean isUnique) {
        this(label, isUnique, SIZE_DEFAULT);
    }

    public StringField(String label, Integer length) {
        this(label, false, false, length);
    }

    public StringField(String label, Boolean isRequired, Integer length) {
        this(label, isRequired, false, length);
    }

    public StringField(String label, Integer length, String defaultValue, Boolean isUnique) {
        this(label, false, false, isUnique, new StringFieldDefaultValue(defaultValue), length, length);
    }

    public StringField(String label, Integer length, String defaultValue) {
        this(label, false, false, false, new StringFieldDefaultValue(defaultValue), length, length);
    }

    public StringField(String label, Boolean isRequired, Boolean isUnique, Integer length) {
        this(label, isRequired, false, isUnique, length);
    }


    public StringField(String label, Boolean isRequired, Boolean isReadonly, Boolean isUnique, Integer length) {
        this(label, isRequired, isReadonly, isUnique, length, length);
    }

    public StringField(String label, Boolean isRequired, Boolean isReadonly, Boolean isUnique,
                       Integer size, Integer maxLength) {
        this(label, isRequired, isReadonly, isUnique, IFieldDefaultValue.getStringFieldFaultValue(), size, maxLength);
    }

    public StringField(String label, Boolean isRequired, Boolean isReadonly, Boolean isUnique,
                       IFieldDefaultValue defaultValue) {
        this(label, isRequired, isReadonly, isUnique, defaultValue,
                SIZE_DEFAULT, SIZE_DEFAULT);
    }

    @Override
    public Type getFieldType() {
        return String.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public String getCreateTableSql() {
        String sql = "VARCHAR(" + getSize() + ")";
        if (isPrimaryKey()) {
            sql += " PRIMARY KEY ";
        }
        return sql;
    }

    @Override
    public String getSampleCreateTableSql() {
        return "VARCHAR(" + getSize() + ")";
    }

    public StringField(String label, Boolean isRequired, Boolean isReadonly, Boolean isUnique,
                       IFieldDefaultValue defaultValue, Integer size, Integer maxLength) {
        this(label, isRequired, isReadonly, isUnique, defaultValue, size, maxLength,
                0, false, false);
    }

    public StringField(String label, Boolean isRequired, Boolean isReadonly, Boolean isUnique, IFieldDefaultValue defaultValue,
                       Integer size, Integer maxLength, Integer minLength,
                       Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
        this.setSize(size);
        this.setMinLength(minLength);
        this.setMaxLength(maxLength);
        this.setIsUnique(isUnique);
    }

    @Override
    public Integer getSqlType() {
        return Types.VARCHAR;
    }

    public static class Builder extends Field.Builder<Builder> implements IStringFieldBuilder<Builder> {
        private Integer size = SIZE_DEFAULT;
        private Integer maxLength = SIZE_DEFAULT;
        private Integer minLength = 0;

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
            return new StringField(Builder.this);
        }

        @Override
        public Builder setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        @Override
        public Builder setMinLength(Integer minLength) {
            this.minLength = minLength;
            return this;
        }

        @Override
        public Builder setSize(Integer size) {
            this.size = size;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
