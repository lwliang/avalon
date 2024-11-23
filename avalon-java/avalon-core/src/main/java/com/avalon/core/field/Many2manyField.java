/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.ExternalService;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;

/**
 * 两表关联，多对多关系
 */
public class Many2manyField extends RelationField {
    private AbstractService selfService; // 动态创建的表的服务 仅内部使用

    public AbstractService getRealService() {
        return getSelfService();
    }

    private AbstractService getSelfService() {
        if (ObjectUtils.isNotNull(selfService)) {
            return selfService;
        }
        synchronized (this) {
            if (ObjectUtils.isNull(selfService)) {
                ExternalService externalService = getMasterService().getContext().getNewExternalService();
                externalService.setServiceName(Fields.dot2UnderscoreName(getTableSqlName()));
                // 主键
                Field field = IntegerField.Builder.getInstance()
                        .setFieldName(getMasterService().getPrimaryKeyField().getFieldName())
                        .setIsAutoIncrement(true)
                        .setIsPrimaryKey(true)
                        .build();
                field.setService(externalService);
                externalService.addField(field);
                // 外键
                field = IntegerField.Builder.getInstance()
                        .setFieldName(getMasterForeignKeyName())
                        .build();
                field.setService(externalService);
                externalService.addField(field);

                field = Many2oneField.Builder.getInstance()
                        .setFieldName(getRelativeForeignKeyName())
                        .setRelativeServiceName(getRelativeServiceName())
                        .build();
                field.setService(externalService);
                externalService.addField(field);
                selfService = externalService;
                getService().getContext().registerSingleton(externalService.getServiceName(), externalService);
            }
            return selfService;
        }
    }

    private AbstractService relativeService;

    private AbstractService getMasterService() {
        return getService();
    }

    private String relativeServiceName;

    @Override
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
        if (ObjectUtils.isNotNull(relativeService)) return relativeService;
        relativeService = getService().getServiceBean(getRelativeServiceName());
        return relativeService;
    }

    protected void setRelativeServiceName(String relativeServiceName) {
        this.relativeServiceName = relativeServiceName;
    }

    @Getter
    @Setter
    private String tableName; // 服务名，格式a.b.c

    public String getTableSqlName() {
        if (StringUtils.isNotEmpty(tableName)) return Fields.dot2UnderscoreName(tableName);
        return getMasterService().getServiceTableName() +
                "_" + getRelativeService().getServiceTableName() + "_" + getFieldName() + "_ref";
    }

    /**
     * 得到主表的外键名称
     *
     * @return
     */
    public String getMasterForeignKeyName() {
        if (StringUtils.isNotEmpty(masterKeyName)) return masterKeyName;
        return Fields.camelName(getMasterForeignSqlKeyName());
    }

    @Getter
    @Setter
    private String masterKeyName; // 主表关联字段

    @Getter
    @Setter
    private String relativeKeyName; // 关联表关联字段

    public String getMasterForeignSqlKeyName() {
        Field primaryKeyField = getMasterService().getPrimaryKeyField();
        return getMasterService().getServiceTableName() + "_" + primaryKeyField.getFieldName();
    }

    /**
     * 得到关联表的外键名称
     *
     * @return
     */
    public String getRelativeForeignKeyName() {
        if (StringUtils.isNotEmpty(relativeKeyName)) return relativeKeyName;
        return Fields.camelName(getRelativeForeignSqlKeyName());
    }

    public String getRelativeForeignSqlKeyName() {
        Field primaryKeyField = getRelativeService().getPrimaryKeyField();
        return getRelativeService().getServiceTableName() + "_" + primaryKeyField.getFieldName();
    }

    public void createTable() {
        if (isPostgres()) {
            getService().getContext().getJdbcTemplate().execute(new StringBuilder(createPostgresSequenceSql()));
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
        stringBuilder.append(getTableSqlName());
        stringBuilder.append(" ( ");
        Field primaryKeyField = getMasterService().getPrimaryKeyField();
        stringBuilder.append(primaryKeyField.getFieldName());
        stringBuilder.append(" ");
        stringBuilder.append(getPrimaryKeySql(primaryKeyField));
        stringBuilder.append(",");
        stringBuilder.append(Fields.underscoreName(getMasterForeignKeyName())).append(" ");
        stringBuilder.append(primaryKeyField.getSampleCreateTableSql());
        stringBuilder.append(",");
        primaryKeyField = getRelativeService().getPrimaryKeyField();
        stringBuilder.append(Fields.underscoreName(getRelativeForeignKeyName())).append(" ");
        stringBuilder.append(primaryKeyField.getSampleCreateTableSql());
        stringBuilder.append(" ) ");
        getService().getContext().getJdbcTemplate().execute(stringBuilder);
    }

    /**
     * 创建自增主键sql语法
     *
     * @param primaryKeyField
     * @return
     */
    private String getPrimaryKeySql(Field primaryKeyField) {
        String sql = primaryKeyField.getSampleCreateTableSql();

        sql += " PRIMARY KEY ";

        if (isMySql()) {
            sql += " AUTO_INCREMENT ";
        } else {
            sql += String.format(" DEFAULT NEXTVAL('%s')", getPostgresSequenceTable());
        }

        return sql;
    }

    public void dropTable() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE if exists ");
        stringBuilder.append(getTableSqlName());
        getService().getContext().getJdbcTemplate().execute(stringBuilder);

        if (isPostgres()) {
            String postgresSequenceTable = dropPostgresSequenceSql();
            getService().getContext().getJdbcTemplate().execute(new StringBuilder(postgresSequenceTable));
        }
    }

    public Many2manyField(String relativeServiceName) {
        this("", relativeServiceName);
    }

    @Override
    public Type getFieldType() {
        return Many2manyField.class;
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

    public Many2manyField(Builder builder) {
        super(builder);
        if (StringUtils.isNotEmpty(builder.getRelativeServiceName())) {
            setRelativeServiceName(builder.getRelativeServiceName());
        }

        if (StringUtils.isNotEmpty(builder.getTableName())) {
            setTableName(builder.getTableName());
        }
        if (StringUtils.isNotEmpty(builder.getMasterKeyName())) {
            setMasterKeyName(builder.getMasterKeyName());
        }
        if (StringUtils.isNotEmpty(builder.getRelativeKeyName())) {
            setRelativeKeyName(builder.getRelativeKeyName());
        }
    }

    public Many2manyField(String label, String relativeServiceName) {
        this(label, false, relativeServiceName);
    }

    public Many2manyField(String label, Boolean isRequired, String relativeServiceName) {
        this(label, isRequired, false, relativeServiceName);
    }

    public Many2manyField(String label, Boolean isRequired, Boolean isReadonly, String relativeServiceName) {
        this(label, isRequired, isReadonly, IFieldDefaultValue.getFieldDefaultValue(), relativeServiceName);
    }

    public Many2manyField(String label, Boolean isRequired, Boolean isReadonly,
                          IFieldDefaultValue defaultValue, String relativeServiceName) {
        this(label, isRequired, isReadonly, defaultValue, relativeServiceName, false, false);
    }

    public Many2manyField(String label, Boolean isRequired, Boolean isReadonly,
                          IFieldDefaultValue defaultValue,
                          String relativeServiceName, Boolean isPrimaryKey,
                          Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, true,
                defaultValue, isPrimaryKey, isAutoIncrement);
        this.setRelativeServiceName(relativeServiceName);
    }

    @Override
    public Integer getSqlType() {
        return null;
    }

    public static class Builder extends Field.Builder<Many2manyField.Builder> {
        @Getter
        private String tableName; // 服务名，格式a.b.c
        @Getter
        private String masterKeyName; // 主表关联字段
        @Getter
        private String relativeKeyName; // 关联表关联字段
        @Getter
        private String relativeServiceName;

        public Builder setTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder setMasterKeyName(String masterKeyName) {
            this.masterKeyName = masterKeyName;
            return this;
        }

        public Builder setRelativeKeyName(String relativeKeyName) {
            this.relativeKeyName = relativeKeyName;
            return this;
        }

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

        public Builder setRelativeServiceName(String relativeServiceName) {
            this.relativeServiceName = relativeServiceName;
            return this;
        }

        @Override
        public Field build() {
            return new Many2manyField(this);
        }

        public static Many2manyField.Builder getInstance() {
            return new Many2manyField.Builder();
        }
    }
}
