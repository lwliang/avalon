/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.context.Context;
import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.sql.Types;

/**
 * 关联主表字段,或者选择资源字段
 */
public class Many2oneField extends RelationField {

    private AbstractService abstractService;

    private String relativeServiceName;

    public String getRelativeServiceName() {
        return relativeServiceName;
    }

    @Override
    public String getRelativeFieldName() {
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AbstractService getRelativeService() {
        return getService().getServiceBean(getRelativeServiceName());
    }

    protected void setRelativeServiceName(String relativeServiceName) {
        this.relativeServiceName = relativeServiceName;
    }

    public Many2oneField(Builder builder) {
        super(builder);
        setRelativeServiceName(builder.relativeServiceName);
    }

    public Many2oneField(String label, String relativeServiceName) {
        this(label, false, relativeServiceName);
    }

    public Many2oneField(String label, Boolean isRequired, String relativeServiceName) {
        this(label, isRequired, false, relativeServiceName);
    }

    public Many2oneField(String label, Boolean isRequired, Boolean isReadonly,
                         String relativeServiceName) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getFieldDefaultValue(),
                relativeServiceName);
    }

    public Many2oneField(String label, Boolean isRequired, Boolean isReadonly,
                         IFieldDefaultValue defaultValue, String relativeServiceName) {
        this(label, isRequired, isReadonly, defaultValue, relativeServiceName,
                false, false);
    }

    public Many2oneField(String label, Boolean isRequired, Boolean isReadonly,
                         IFieldDefaultValue defaultValue, String relativeServiceName,
                         Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, true,
                defaultValue, isPrimaryKey, isAutoIncrement);
        this.setRelativeServiceName(relativeServiceName);
    }

    private AbstractService getAbstractService() {
        if (ObjectUtils.isNotNull(abstractService)) return abstractService;
        synchronized (this) {
            if (ObjectUtils.isNotNull(abstractService)) return abstractService;
            abstractService = Context.getServiceBeanInstance(getRelativeServiceName());
            return abstractService;
        }
    }

    @Override
    public Type getFieldType() {
        AbstractService abstractService = getAbstractService();
        return abstractService.getPrimaryKeyType();
    }

    @Override
    public Object getSqlValue(Object value) {
        return getAbstractService().getPrimaryKeyField().getSqlValue(value);
    }

    @Override
    public String getCreateTableSql() {
        return getSampleCreateTableSql();
    }

    @Override
    public String getSampleCreateTableSql() {
        return getAbstractService().getPrimaryKeyField().getSampleCreateTableSql();
    }

    @Override
    public Integer getSqlType() {
        return Types.INTEGER;
    }

    public static class Builder extends Field.Builder<Builder> {
        private String relativeServiceName;//子表的服务名

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
            return new Many2oneField(this);
        }

        public Builder setRelativeServiceName(String relativeServiceName) {
            this.relativeServiceName = relativeServiceName;
            return this;
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
