/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.service.AbstractService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;

public class One2manyField extends RelationField {

    private String relativeServiceName;//子表的服务名
    private String relativeFieldName;//子表的字段明

    public String getRelativeServiceName() {
        return relativeServiceName;
    }

    protected void setRelativeServiceName(String relativeServiceName) {
        this.relativeServiceName = relativeServiceName;
    }

    public String getRelativeFieldName() {
        return relativeFieldName;
    }

    protected void setRelativeFieldName(String relativeFieldName) {
        this.relativeFieldName = relativeFieldName;
    }

    public One2manyField(Builder builder) {
        super(builder);
        setRelativeServiceName(builder.relativeServiceName);
        setRelativeFieldName(builder.relativeFieldName);
    }

    public One2manyField(String relativeServiceName, String relativeFieldName) {
        this("", relativeServiceName, relativeFieldName);
    }

    public One2manyField(String label, String relativeServiceName, String relativeFieldName) {
        this(label, false, relativeServiceName, relativeFieldName);
    }

    public One2manyField(String label, Boolean isRequired, String relativeServiceName, String relativeFieldName) {
        this(label, isRequired, false, relativeServiceName, relativeFieldName);
    }

    public One2manyField(String label, Boolean isRequired, Boolean isReadonly, String relativeServiceName, String relativeFieldName) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getFieldDefaultValue(), relativeServiceName, relativeFieldName);
    }

    public One2manyField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, String relativeServiceName, String relativeFieldName) {
        this(label, isRequired, isReadonly, defaultValue, relativeServiceName, relativeFieldName, false, false);
    }

    public One2manyField(String label, Boolean isRequired, Boolean isReadonly,
                         IFieldDefaultValue defaultValue, String relativeServiceName,
                         String relativeFieldName, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, true,
                defaultValue, isPrimaryKey, isAutoIncrement);
        this.setRelativeServiceName(relativeServiceName);
        this.setRelativeFieldName(relativeFieldName);
    }
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public AbstractService getRelativeService() {
        return getService().getServiceBean(getRelativeServiceName());
    }

    @Override
    public Type getFieldType() {
        return null;
    }

    @Override
    public Object getSqlValue(Object value) {
        return null;
    }

    @Override
    public String getCreateTableSql() {
        return null;
    }

    @Override
    public String getSampleCreateTableSql() {
        return null;
    }

    @Override
    public Integer getSqlType() {
        return null;
    }

    public static class Builder extends Field.Builder<Builder> {
        private String relativeServiceName;
        private String relativeFieldName;

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
            return new One2manyField(this);
        }

        public Builder setRelativeServiceName(String relativeServiceName) {
            this.relativeServiceName = relativeServiceName;
            return this;
        }

        public Builder setRelativeFieldName(String relativeFieldName) {
            this.relativeFieldName = relativeFieldName;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
