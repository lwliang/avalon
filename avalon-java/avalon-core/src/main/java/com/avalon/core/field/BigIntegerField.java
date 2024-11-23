/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.face.field.LongFieldDefaultValue;
import com.avalon.core.field.builder.INumberFieldBuilder;

import java.lang.reflect.Type;
import java.sql.Types;

/**
 * description: BigIntegerField
 * date: 2022/6/21 9:52
 * author: AN
 * version: 1.0
 */
public class BigIntegerField extends Field implements INumberField<Long> {
    private Long max = Long.MAX_VALUE;
    private Long min = Long.MIN_VALUE;

    public BigIntegerField(Builder builder) {
        super(builder);
        setMax(builder.max);
        setMin(builder.min);
    }

    public BigIntegerField(String label) {
        this(label, false);
    }

    public BigIntegerField(String label, Boolean isPrimaryKey) {
        this(label, false, true, null, isPrimaryKey, false);
    }

    public BigIntegerField(String label, Boolean isRequired, Boolean isReadonly) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getIntegerFieldDefaultValue());
    }

    public BigIntegerField(String label, Boolean isRequired, Boolean isReadonly,
                           IFieldDefaultValue defaultValue) {
        this(label, isRequired, isReadonly, defaultValue, false, false);
    }

    public BigIntegerField(String label,
                           Boolean isRequired,
                           Boolean isReadonly,
                           IFieldDefaultValue defaultValue,
                           Boolean isPrimaryKey,
                           Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
    }

    protected void setMax(Long max) {
        this.max = max;
    }

    protected void setMin(Long min) {
        this.min = min;
    }

    public Long getMax() {
        return max;
    }

    public Long getMin() {
        return min;
    }

    @Override
    public Type getFieldType() {
        return Long.class;
    }

    @Override
    public Object getSqlValue(Object value) {
        return value;
    }

    @Override
    public String getCreateTableSql() {
        String sql = "BIGINT";

        if (isPrimaryKey()) {
            sql += " PRIMARY KEY ";
            if (isAutoIncrement()) {
                if (isMySql()) {
                    sql += " AUTO_INCREMENT ";
                } else {
                    sql += String.format("DEFAULT NEXTVAL('%s')", getPostgresSequenceTable());
                }
            }
        }
        return sql;
    }

    @Override
    public String getSampleCreateTableSql() {
        return "BIGINT";
    }

    @Override
    public Integer getSqlType() {
        return Types.BIGINT;
    }

    @Override
    public Long getMaxValue() {
        return max;
    }

    @Override
    public Long getMinValue() {
        return min;
    }

    public static class Builder extends Field.Builder<Builder> implements INumberFieldBuilder<Long, Builder> {
        private Long max = Long.MAX_VALUE;
        private Long min = Long.MIN_VALUE;

        @Override
        public BigIntegerField.Builder setIsUnique(Boolean isUnique) {
            this.isUnique = isUnique;
            return this;
        }

        @Override
        public BigIntegerField.Builder setAllowNull(Boolean allowNull) {
            this.allowNull = allowNull;
            return this;
        }

        @Override
        public BigIntegerField.Builder setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        @Override
        public BigIntegerField.Builder setDefaultValue(IFieldDefaultValue defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        @Override
        public BigIntegerField.Builder setIsAutoIncrement(Boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
            return this;
        }

        @Override
        public BigIntegerField.Builder setIsPrimaryKey(Boolean isPrimaryKey) {
            this.isPrimaryKey = isPrimaryKey;
            return this;
        }

        @Override
        public BigIntegerField.Builder setIsReadonly(Boolean isReadonly) {
            this.isReadonly = isReadonly;
            return this;
        }

        @Override
        public BigIntegerField.Builder setFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        @Override
        public BigIntegerField.Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public Field build() {
            return new BigIntegerField(this);
        }

        @Override
        public Builder setMaxValue(Long maxValue) {
            max = maxValue;
            return this;
        }

        @Override
        public Builder setMinValue(Long minValue) {
            min = minValue;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
