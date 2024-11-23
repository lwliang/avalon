/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.field.builder.INumberFieldBuilder;

import java.lang.reflect.Type;
import java.sql.Types;

public class FloatField extends Field implements INumberField<Float> {

    private Float max = Float.MAX_VALUE;
    private Float min = Float.MIN_VALUE;

    public Float getMax() {
        return max;
    }

    protected void setMax(Float max) {
        this.max = max;
    }

    public Float getMin() {
        return min;
    }

    protected void setMin(Float min) {
        this.min = min;
    }

    public FloatField(Builder builder) {
        super(builder);
        setMax(builder.max);
        setMin(builder.min);
    }

    public FloatField(String label, Float min, Float max) {
        this(label, true, false, min, max);
    }

    public FloatField(String label, Boolean isRequired, Boolean isReadonly) {
        this(label, isRequired, isReadonly,
                Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatField(String label, Boolean isRequired, Boolean isReadonly, Float min, Float max) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getFloatFieldDefaultValue(), min, max);
    }

    public FloatField(String label, Boolean isRequired, Boolean isReadonly,
                      IFieldDefaultValue defaultValue) {
        this(label, isRequired, isReadonly, defaultValue, Float.MIN_VALUE, Float.MAX_VALUE);
    }

    public FloatField(String label, Boolean isRequired, Boolean isReadonly,
                      IFieldDefaultValue defaultValue, Float min, Float max) {
        super(label, isRequired, isReadonly, defaultValue);
        this.setMin(min);
        this.setMax(max);
    }

    public FloatField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue,
                      Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
    }

    @Override
    public Type getFieldType() {
        return Float.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public String getCreateTableSql() {
        return "FLOAT";
    }

    @Override
    public String getSampleCreateTableSql() {
        return getCreateTableSql();
    }

    public FloatField(String label) {
        this(label, false);
    }

    public FloatField(String label, Boolean isRequired) {
        this(label, isRequired, false);
    }

    @Override
    public Integer getSqlType() {
        return Types.FLOAT;
    }

    @Override
    public Float getMaxValue() {
        return max;
    }

    @Override
    public Float getMinValue() {
        return min;
    }

    public static class Builder extends Field.Builder<Builder> implements INumberFieldBuilder<Float, Builder> {
        private Float max = Float.MAX_VALUE;
        private Float min = Float.MIN_VALUE;

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

        public FloatField build() {
            return new FloatField(this);
        }

        @Override
        public Builder setMaxValue(Float maxValue) {
            this.max = maxValue;
            return this;
        }

        @Override
        public Builder setMinValue(Float minValue) {
            this.min = minValue;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
