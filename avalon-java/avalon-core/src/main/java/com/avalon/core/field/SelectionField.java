/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.annotation.EnumFieldOrder;
import com.avalon.core.face.field.FieldDefaultValue;
import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.util.EnumUtils;
import com.avalon.core.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.sql.Types;

@Getter
@Setter
public class SelectionField extends Field implements IFieldFormat {

    private Boolean isMulti = false;//是否可以多选

    private SelectionHashMap section;

    /**
     * 获取用于排序的字段顺序
     *
     * @return 字段排序
     */
    public String[] getFieldOrder() {
        Class<? extends Enum> type = section.getType();

        EnumFieldOrder annotation = type.getAnnotation(EnumFieldOrder.class);
        if (ObjectUtils.isNotNull(annotation)) {
            return annotation.value();
        }
        return new String[]{};
    }

    protected void setSection(SelectionHashMap section) {
        this.section = section;
    }

    public SelectionField(Builder builder) {
        super(builder);
        setSection(builder.section);
        setIsMulti(builder.isMulti);
    }

    public SelectionField(String label, Class<? extends Enum> enumClass) {
        this(label, EnumUtils.getSelectionMapFromEnum(enumClass));
    }

    public SelectionField(String label, Class<? extends Enum> enumClass, Object defaultValue) {
        this(label, true, EnumUtils.getSelectionMapFromEnum(enumClass), new FieldDefaultValue(defaultValue));
    }

    public SelectionField(String label, SelectionHashMap section) {
        this(label, false, section);
    }

    public SelectionField(String label, SelectionHashMap section, Boolean isMulti) {
        this(label, false, section);
        setIsMulti(isMulti);
    }

    public SelectionField(String label, Boolean isRequired, SelectionHashMap section) {
        this(label, isRequired, false, section);
    }

    public SelectionField(String label, Boolean isRequired, Boolean isReadonly,
                          SelectionHashMap section) {
        this(label, isRequired, isReadonly,
                IFieldDefaultValue.getFieldDefaultValue(), section);
    }

    public SelectionField(String label, Boolean isRequired, SelectionHashMap section,
                          IFieldDefaultValue defaultValue) {
        this(label, isRequired, false, defaultValue, section);
    }

    public SelectionField(String label, Boolean isRequired, Boolean isReadonly,
                          IFieldDefaultValue defaultValue, SelectionHashMap section) {
        this(label, isRequired, isReadonly, defaultValue, section
                , false, false);
    }

    public SelectionField(String label, Boolean isRequired, Boolean isReadonly,
                          IFieldDefaultValue defaultValue, SelectionHashMap section,
                          Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, isPrimaryKey, isAutoIncrement);
        this.setSection(section);
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
    public String getFormat() {
        return null;
    }

    @Override
    public Object getReportValue(Object value) {
        return IFieldFormat.super.getReportValue(value);
    }

    @Override
    public String getCreateTableSql() {
        return "VARCHAR(20)";
    }

    @Override
    public String getSampleCreateTableSql() {
        return getCreateTableSql();
    }

    @Override
    protected void setName(String propertyName) {
        super.setName(propertyName);
    }

    @Override
    public Integer getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public Object getClientValue(Object value) {
        if (ObjectUtils.isNull(value)) return value;
        if (value.getClass().isEnum()) {
            return value.toString();
        }
        return super.getClientValue(value);
    }

    public static class Builder extends Field.Builder {
        private SelectionHashMap section;
        private Boolean isMulti = false;//是否可以多选

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
            return new SelectionField(Builder.this);
        }

        public Builder setSection(SelectionHashMap section) {
            this.section = section;
            return this;
        }

        public Builder setMulti(Boolean multi) {
            isMulti = multi;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
