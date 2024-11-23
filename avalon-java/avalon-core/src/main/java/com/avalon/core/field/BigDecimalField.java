/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.field.builder.INumberFieldBuilder;
import com.avalon.core.util.ObjectUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Types;

public class BigDecimalField extends Field implements INumberField<BigDecimal> {

    private Integer size = SIZE_DEFAULT;
    private Integer scale = SCALE_DEFAULT;
    private BigDecimal max = new BigDecimal(Integer.MAX_VALUE);
    private BigDecimal min = new BigDecimal(Integer.MIN_VALUE);

    public final static Integer SIZE_DEFAULT = 24;
    public final static Integer SCALE_DEFAULT = 6;

    public Integer getSize() {
        return size;
    }

    protected void setSize(Integer size) {
        this.size = size;
    }

    public Integer getScale() {
        return scale;
    }

    protected void setScale(Integer scale) {
        this.scale = scale;
    }

    public BigDecimal getMax() {
        return max;
    }

    protected void setMax(BigDecimal max) {
        if (ObjectUtils.isNull(max)) return;
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    protected void setMin(BigDecimal min) {
        if (ObjectUtils.isNull(min)) return;
        this.min = min;
    }

    public BigDecimalField(Builder builder) {
        super(builder);
        setSize(builder.size);
        setScale(builder.scale);
        setMax(builder.max);
        setMin(builder.min);
    }

    public BigDecimalField(String label) {
        this(label, false, SIZE_DEFAULT, SCALE_DEFAULT);
    }

    @Override
    public Type getFieldType() {
        return BigDecimal.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public String getCreateTableSql() {
        return "DECIMAL(" + getSize() + "," + getScale() + ")";
    }

    @Override
    public String getSampleCreateTableSql() {
        return getCreateTableSql();
    }

    public BigDecimalField(String label, Integer size, Integer scale) {
        this(label, false, size, scale);
    }

    public BigDecimalField(String label, Boolean isRequired, Integer size, Integer scale) {
        this(label, isRequired, false, size, scale, new BigDecimal(Integer.MAX_VALUE),
                new BigDecimal(Integer.MIN_VALUE));
    }

    public BigDecimalField(String label, Boolean isRequired, Boolean isReadonly,
                           Integer size, Integer scale, BigDecimal max, BigDecimal min) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getBigDecimalFieldDefaultValue(),
                size, scale, max, min);
    }

    public BigDecimalField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue,
                           Integer size, Integer scale, BigDecimal max, BigDecimal min) {
        this(label, isRequired, isReadonly, defaultValue, size, scale, max, min, false, false);
    }

    public BigDecimalField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue,
                           Integer size, Integer scale, BigDecimal max, BigDecimal min,
                           Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
        this.setSize(size);
        this.setScale(scale);
        this.setMax(max);
        this.setMin(min);
    }

    @Override
    public Integer getSqlType() {
        return Types.DECIMAL;
    }

    @Override
    public BigDecimal getMaxValue() {
        return max;
    }

    @Override
    public BigDecimal getMinValue() {
        return min;
    }

    public static class Builder extends Field.Builder<Builder> implements INumberFieldBuilder<BigDecimal, Builder> {
        private BigDecimal max = BigDecimal.valueOf(Long.MAX_VALUE);
        private BigDecimal min = BigDecimal.valueOf(Long.MIN_VALUE);
        private Integer size = SIZE_DEFAULT;
        private Integer scale = SCALE_DEFAULT;

        @Override
        public Builder setIsUnique(Boolean isUnique) {
            this.isUnique = isUnique;
            return this;
        }

        @Override
        public Builder setAllowNull(Boolean allowNull) {
            this.allowNull = allowNull;
            return Builder.this;
        }

        @Override
        public Builder setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return Builder.this;
        }

        @Override
        public Builder setDefaultValue(IFieldDefaultValue defaultValue) {
            this.defaultValue = defaultValue;
            return Builder.this;
        }

        @Override
        public Builder setIsAutoIncrement(Boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
            return Builder.this;
        }

        @Override
        public Builder setIsPrimaryKey(Boolean isPrimaryKey) {
            this.isPrimaryKey = isPrimaryKey;
            return Builder.this;
        }

        @Override
        public Builder setIsReadonly(Boolean isReadonly) {
            this.isReadonly = isReadonly;
            return Builder.this;
        }

        @Override
        public Builder setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return Builder.this;
        }

        @Override
        public Builder setLabel(String label) {
            this.label = label;
            return Builder.this;
        }

        @Override
        public Field build() {
            return new BigDecimalField(this);
        }

        @Override
        public Builder setMaxValue(BigDecimal maxValue) {
            max = maxValue;
            return Builder.this;
        }

        @Override
        public Builder setMinValue(BigDecimal minValue) {
            min = minValue;
            return this;
        }

        public Builder setSize(Integer size) {
            this.size = size;
            return this;
        }

        public Builder setScale(Integer scale) {
            this.scale = scale;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
