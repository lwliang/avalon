/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;

import java.lang.reflect.Type;
import java.sql.Types;

public class BooleanField extends Field {
    public BooleanField(Builder builder) {
        super(builder);
    }

    public BooleanField(String label) {
        super(label);
    }

    public BooleanField(String label, Boolean isRequired) {
        super(label, isRequired);
    }

    public BooleanField(String label, Boolean isRequired, Boolean isReadonly) {
        super(label, isRequired, isReadonly);
    }

    public BooleanField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue) {
        super(label, isRequired, isReadonly, defaultValue);
    }

    public BooleanField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
    }

    @Override
    public Type getFieldType() {
        return Boolean.TYPE;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public String getCreateTableSql() {
        return isMySql() ? "BIT" : "boolean";
    }

    @Override
    public String getSampleCreateTableSql() {
        return getCreateTableSql();
    }

    @Override
    public Integer getSqlType() {
        return Types.BIT;
    }

    public static class Builder extends Field.Builder<Builder> {
        @Override
        public BooleanField.Builder setIsUnique(Boolean isUnique) {
            this.isUnique = isUnique;
            return this;
        }

        @Override
        public BooleanField.Builder setAllowNull(Boolean allowNull) {
            this.allowNull = allowNull;
            return this;
        }

        @Override
        public BooleanField.Builder setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        @Override
        public BooleanField.Builder setDefaultValue(IFieldDefaultValue defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        @Override
        public BooleanField.Builder setIsAutoIncrement(Boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
            return this;
        }

        @Override
        public BooleanField.Builder setIsPrimaryKey(Boolean isPrimaryKey) {
            this.isPrimaryKey = isPrimaryKey;
            return this;
        }

        @Override
        public BooleanField.Builder setIsReadonly(Boolean isReadonly) {
            this.isReadonly = isReadonly;
            return this;
        }

        @Override
        public BooleanField.Builder setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        @Override
        public BooleanField.Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public Field build() {
            return new BooleanField(this);
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
