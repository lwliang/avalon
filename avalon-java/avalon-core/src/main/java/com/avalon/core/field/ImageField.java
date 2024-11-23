/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;

import java.lang.reflect.Type;
import java.sql.Types;

public class ImageField extends Field {
    public ImageField(Builder builder) {
        super(builder);
    }

    public ImageField(String label) {
        super(label);
    }

    public ImageField(String label, Boolean isRequired) {
        super(label, isRequired);
    }

    public ImageField(String label, Boolean isRequired, Boolean isReadonly) {
        super(label, isRequired, isReadonly);
    }

    public ImageField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue) {
        super(label, isRequired, isReadonly, defaultValue);
    }

    public ImageField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
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
        return "VARCHAR(100)";
    }

    @Override
    public String getSampleCreateTableSql() {
        return "VARCHAR(100)";
    }

    @Override
    public Integer getSqlType() {
        return Types.VARCHAR;
    }

    public static class Builder extends Field.Builder<Builder> {
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
            return new ImageField(Builder.this);
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
