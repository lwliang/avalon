/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.alias.DefaultAliasSupport;
import com.avalon.core.alias.IAliasRequire;
import com.avalon.core.condition.Condition;
import com.avalon.core.condition.field.IFieldCondition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.field.builder.IFieldBuilder;
import com.avalon.core.field.external.ExternalField;
import com.avalon.core.model.FieldSerializer;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.reflect.Type;

@JsonSerialize(using = FieldSerializer.class)
public abstract class Field implements IField, ExternalField, IAliasRequire, IFieldCondition {
    private String propertyName = ""; // 字段名 小驼峰 比如 firstName
    private String fieldName = ""; // 数据库名 带下划线_ first_name
    private Boolean isPrimaryKey = false;
    private Boolean isRequired = false;
    private Boolean isReadonly = false;
    private IFieldDefaultValue defaultValue;
    private Boolean isAutoIncrement = false;
    private String label = "";
    private AbstractService service;

    private Boolean isExternalField = false;

    /**
     * 是否是扩展字段
     *
     * @return 是否是扩展字段
     */
    @Override
    public Boolean getIsExternalField() {
        return isExternalField;
    }

    /**
     * 设置是否是扩展字段
     *
     * @param isExternalField 是否是扩展字段
     */
    @Override
    public void setIsExternalField(Boolean isExternalField) {
        this.isExternalField = isExternalField;
    }

    public Boolean needLog() {
        return true;
    }

    public String getLabel() {
        return label;
    }

    protected void setLabel(String label) {
        this.label = label;
    }

    public Field(Builder builder) {
        if (ObjectUtils.isNotEmpty(builder.fieldName)) {
            setName(builder.fieldName);
        }
        if (ObjectUtils.isNotNull(builder.isPrimaryKey)) {
            this.isPrimaryKey = builder.isPrimaryKey;
        }
        if (ObjectUtils.isNotNull(builder.isRequired)) {
            this.isRequired = builder.isRequired;
        }
        if (ObjectUtils.isNotNull(builder.isReadonly)) {
            this.isReadonly = builder.isReadonly;
        }
        if (ObjectUtils.isNotNull(builder.defaultValue)) {
            this.defaultValue = builder.defaultValue;
        }
        if (ObjectUtils.isNotNull(builder.isAutoIncrement)) {
            this.isAutoIncrement = builder.isAutoIncrement;
        }
        if (ObjectUtils.isNotEmpty(builder.label)) {
            this.label = builder.label;
        }
        if (ObjectUtils.isNotNull(builder.allowNull)) {
            this.allowNull = builder.allowNull;
        }
        if (ObjectUtils.isNotNull(builder.isUnique)) {
            this.isUnique = builder.isUnique;
        }
    }

    public Field(String label) {
        this(label, false);
    }

    public Field(String label, Boolean isRequired) {
        this(label, isRequired, false);
    }

    public Field(String label,
                 Boolean isRequired,
                 Boolean isReadonly) {
        this(label, isRequired, isReadonly, null);
    }

    public Field(String label,
                 Boolean isRequired,
                 Boolean isReadonly,
                 IFieldDefaultValue defaultValue) {
        this(label, isRequired, isReadonly, defaultValue,
                false, false);
    }

    @Override
    public AbstractService getService() {
        if (ObjectUtils.isNotNull(service)) {
            return service.getService();
        }
        return service;
    }

    protected void setService(AbstractService service) {
        this.service = service;
    }

    public Field(String label,
                 Boolean isRequired,
                 Boolean isReadonly,
                 IFieldDefaultValue defaultValue,
                 Boolean isPrimaryKey,
                 Boolean isAutoIncrement) {
        this.label = label;
        this.isPrimaryKey = isPrimaryKey;
        this.isReadonly = isReadonly;
        this.isRequired = isRequired;
        this.defaultValue = defaultValue;
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getName() {
        return propertyName;
    }

    protected void setName(String propertyName) {
        if (ObjectUtils.isNotEmpty(this.propertyName)) {
            return;
        }
        this.propertyName = propertyName;
        this.fieldName = Fields.underscoreName(propertyName);
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public String createPostgresSequenceSql() {
        return String.format(" DO $$\n" +
                "BEGIN\n" +
                "    IF NOT EXISTS (SELECT sequencename FROM pg_sequences WHERE sequencename = '%s') THEN\n" +
                "        CREATE SEQUENCE %s START 1;\n" +
                "    END IF;\n" +
                "END $$;", getPostgresSequenceTable(), getPostgresSequenceTable());
    }

    public String dropPostgresSequenceSql() {
        return String.format("DROP SEQUENCE IF EXISTS %s;", getPostgresSequenceTable());
    }

    public String getPostgresSequenceTable() {
        return getService().getServiceTableName() + "_" + fieldName + "_seq";
    }

    @Override
    public Boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    @Override
    public IFieldDefaultValue getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(IFieldDefaultValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Boolean isRequired() {
        return isRequired;
    }

    @Override
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    @Override
    public Boolean isReadonly() {
        return isReadonly;
    }

    /**
     * 返回对应的java 内置数据类型
     *
     * @return
     */
    public abstract Type getFieldType();

    public abstract Object getSqlValue(Object value);

    public Object getClientValue(Object value) {
        return getSqlValue(value);
    }

    public Object parse(Object value) throws AvalonException {
        return value;
    }

    // 创建表时，显示字段的sql语句，包括数据类型 主键，非空，自增等 比如 INT PRIMARY KEY AUTOINCREMENT NOT NULL
    public abstract String getCreateTableSql();

    // 字段的数据类型 比如 VARCHAR(255) OR TEXT
    public abstract String getSampleCreateTableSql();

    private Boolean isUnique = false;

    @Override
    public Boolean isUnique() {
        return isUnique;
    }

    @Override
    public void setIsUnique(Boolean isUnique) {
        this.isUnique = isUnique;
    }

    private Boolean allowNull = true;

    @Override
    public Boolean allowNull() {
        return allowNull;
    }

    public void setAllowNull(Boolean allowNull) {
        this.allowNull = allowNull;
    }

    @Override
    public String toString() {
        return String.format("label=%s,name=%s,type=%s", getLabel(), getName(), getClass().getSimpleName());
    }

    @Override
    public String getAlias(DefaultAliasSupport defaultAliasSupport) {
        return defaultAliasSupport.getAlias(this);
    }

    public void insertFieldInfo(PrimaryKey serviceId) {
        AbstractService service = getService().getServiceBean("base.field");
        Condition condition = Condition.equalCondition("serviceId", serviceId.getValue())
                .andEqualCondition("name", getName());
        Record select = service.select(condition, "id");
        RecordRow row = RecordRow.build();

        if (!select.isEmpty()) {
            row = select.get(0);
        }

        row.put("serviceId", serviceId.getValue());
        row.put("name", getName());
        row.put("label", getLabel());
        row.put("isPrimaryKey", isPrimaryKey());
        row.put("isRequired", isRequired());
        row.put("isReadonly", isReadonly());
        row.put("isAutoIncrement", isAutoIncrement());
        row.put("isUnique", isUnique());
        row.put("allowNull", allowNull());
        row.put("type", getClassType());
        if (ObjectUtils.isNotEmpty(getDefaultValue())) {
            row.put("defaultValue", getDefaultValue().getDefaultString());
        }
        if (this instanceof StringField) {
            StringField field = (StringField) this;
            row.put("maxLength", field.getMaxLength());
            row.put("minLength", field.getMinLength());
        }
        if (FieldUtils.isNumber(this)) {
            INumberField field = (INumberField) this;
            row.put("maxValue", field.getMaxValue());
            row.put("minValue", field.getMinValue());
        }
        if (this instanceof SelectionField) {
            row.put("isMulti", ((SelectionField) this).getIsMulti());
        }

        if (this instanceof RelationField) {
            RelationField field = ((RelationField) this);
            row.put("relativeServiceName", field.getRelativeServiceName());
            row.put("relativeFieldName", field.getRelativeFieldName());

            if (this instanceof Many2manyField) {
                Many2manyField many2manyField = (Many2manyField) this;
                row.put("masterForeignKeyName", many2manyField.getMasterForeignKeyName());
                row.put("relativeForeignKeyName", many2manyField.getRelativeForeignKeyName());
                row.put("manyServiceTable", many2manyField.getTableSqlName());
            }
        }
        if (select.isEmpty()) {
            service.insert(row);
        } else {
            service.update(row);
        }
    }

    public Boolean isMySql() {
        return getService().getContext().isMysql();
    }

    public Boolean isPostgres() {
        return getService().getContext().isPostgres();
    }

    @Override
    public String getClassType() {
        return getClass().getSimpleName();
    }

    @Override
    public Condition eq(Object value) {
        return Condition.equalCondition(this, value);
    }

    @Override
    public Condition ne(Object value) {
        return Condition.notEqualCondition(this, value);
    }

    /**
     * 用于创建字段的builder
     */
    protected abstract static class Builder<R> implements IFieldBuilder<R> {
        protected Boolean isUnique = false;
        protected Boolean allowNull = false;
        protected Boolean isRequired = false;
        protected IFieldDefaultValue defaultValue;
        protected Boolean isAutoIncrement = false;
        protected Boolean isPrimaryKey = false;
        protected Boolean isReadonly = false;
        protected String fieldName = "";
        protected String label = "";

        protected Boolean getIsUnique() {
            return isUnique;
        }

        protected Boolean getAllowNull() {
            return allowNull;
        }

        protected Boolean getIsRequired() {
            return isRequired;
        }

        protected IFieldDefaultValue getDefaultValue() {
            return defaultValue;
        }

        protected Boolean getIsAutoIncrement() {
            return isAutoIncrement;
        }

        protected Boolean getIsPrimaryKey() {
            return isPrimaryKey;
        }

        protected Boolean getIsReadonly() {
            return isReadonly;
        }

        protected String getFieldName() {
            return fieldName;
        }

        protected String getLabel() {
            return label;
        }

    }
}
