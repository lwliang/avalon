/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.face.field.IntegerFieldDefaultValue;
import com.avalon.core.field.builder.INumberFieldBuilder;
import com.avalon.core.util.ObjectUtils;

import java.lang.reflect.Type;
import java.sql.Types;


public class IntegerField extends Field implements INumberField<Integer> {

    protected void setMax(Integer max) {
        this.max = max;
    }

    protected void setMin(Integer min) {
        this.min = min;
    }

    private Integer max = Integer.MAX_VALUE;//最大值

    public Integer getMax() {
        return max;
    }

    public Integer getMin() {
        return min;
    }

    private Integer min = Integer.MIN_VALUE;//最小值

    public IntegerField(Builder builder) {
        super(builder);
        setMax(builder.max);
        setMin(builder.min);
    }

    public IntegerField(String label) {
        this(label, false);
    }

    public IntegerField(String label, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        this(label, true, true, IFieldDefaultValue.getIntegerFieldDefaultValue(),
                Integer.MIN_VALUE, Integer.MAX_VALUE, isPrimaryKey, isAutoIncrement);
    }

    public IntegerField(String label, Integer defaultValue) {
        this(label, false, false, new IntegerFieldDefaultValue(defaultValue), Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerField(String label, Boolean isRequired) {
        this(label, isRequired, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public IntegerField(String label, Boolean isRequired, Boolean isReadonly, Integer min, Integer max) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getIntegerFieldDefaultValue(),
                min, max);
    }

    public IntegerField(String label, Boolean isRequired, Boolean isReadonly,
                        IFieldDefaultValue defaultValue, Integer min, Integer max) {
        this(label, isRequired, isReadonly, defaultValue, min, max, false, false);
    }

    public IntegerField(String label, Boolean isRequired, Boolean isReadonly,
                        IFieldDefaultValue defaultValue, Integer min, Integer max,
                        Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
        setMax(max);
        setMin(min);
    }

    @Override
    public Type getFieldType() {
        return Integer.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public String getCreateTableSql() {
        String sql = "INT";
        if (isPrimaryKey()) {
            sql += " PRIMARY KEY ";
        }
        if (isAutoIncrement()) {
            if (isMySql()) {
                sql += " AUTO_INCREMENT ";
            } else {
                sql += String.format("DEFAULT NEXTVAL('%s')", getPostgresSequenceTable());
            }
        }
        return sql;
    }

    @Override
    public String getSampleCreateTableSql() {
        return "INT";
    }

    @Override
    public Integer getSqlType() {
        return Types.INTEGER;
    }

    @Override
    public Integer getMaxValue() {
        return getMax();
    }

    @Override
    public Integer getMinValue() {
        return getMin();
    }


    public static class Builder extends Field.Builder<Builder> implements INumberFieldBuilder<Integer, Builder> {
        private Integer max = Integer.MAX_VALUE;//最大值
        private Integer min = Integer.MIN_VALUE;//最小值

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
            return new IntegerField(Builder.this);
        }


        @Override
        public Builder setMaxValue(Integer maxValue) {
            max = maxValue;
            return this;
        }

        @Override
        public Builder setMinValue(Integer minValue) {
            min = minValue;
            return this;
        }

        public static Builder getInstance() {

            return  new Builder();
        }
    }
}
