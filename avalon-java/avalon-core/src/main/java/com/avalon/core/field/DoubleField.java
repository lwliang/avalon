/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.field.builder.INumberFieldBuilder;

import java.lang.reflect.Type;
import java.sql.Types;

public class DoubleField extends Field implements INumberField<Double> {
    private Double max = (double) Integer.MAX_VALUE;
    private Double min = (double) Integer.MIN_VALUE;

    public DoubleField(Builder builder) {
        super(builder);
        setMax(builder.max);
        setMin(builder.min);
    }

    public DoubleField(String label) {
        this(label, false);
    }

    public DoubleField(String label, Boolean isRequired) {
        this(label, isRequired, false);
    }

    public DoubleField(String label, Boolean isRequired, Boolean isReadonly) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getDoubleFieldDefaultValue());
    }

    public DoubleField(String label, Boolean isRequired, Boolean isReadonly,
                       IFieldDefaultValue defaultValue) {
        this(label, isRequired, isReadonly, defaultValue, (double) Integer.MAX_VALUE, (double) Integer.MIN_VALUE);
    }

    public DoubleField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue,
                       Double min, Double max) {
        this(label, isRequired, isReadonly, defaultValue, min, max,
                false, false);
    }

    public DoubleField(String label, Boolean isRequired, Boolean isReadonly,
                       IFieldDefaultValue defaultValue, Double min, Double max,
                       Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);

        this.setMax(max);
        this.setMin(min);
    }

    public Double getMax() {
        return max;
    }

    protected void setMax(Double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    protected void setMin(Double min) {
        this.min = min;
    }

    @Override
    public Type getFieldType() {
        return Double.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public String getCreateTableSql() {
        return "DOUBLE";
    }

    @Override
    public String getSampleCreateTableSql() {
        return "DOUBLE";
    }

    @Override
    public Integer getSqlType() {
        return Types.DOUBLE;
    }

    @Override
    public Double getMaxValue() {
        return max;
    }

    @Override
    public Double getMinValue() {
        return min;
    }


    public static class Builder extends Field.Builder<Builder> implements INumberFieldBuilder<Double, Builder> {
        private Double max = (double) Integer.MAX_VALUE;
        private Double min = (double) Integer.MIN_VALUE;

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
            return new DoubleField(this);
        }

        @Override
        public Builder setMaxValue(Double maxValue) {
            max = maxValue;
            return this;
        }

        @Override
        public Builder setMinValue(Double minValue) {
            min = minValue;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
