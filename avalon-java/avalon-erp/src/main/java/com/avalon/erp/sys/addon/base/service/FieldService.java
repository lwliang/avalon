/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.base.service;

import com.avalon.core.condition.Condition;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.face.field.DateFieldDefaultValue;
import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.face.field.IntegerFieldDefaultValue;
import com.avalon.core.face.field.StringFieldDefaultValue;
import com.avalon.core.field.*;
import com.avalon.core.model.PrimaryKey;
import com.avalon.core.model.Record;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.core.service.IExtendFieldSupportService;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import com.avalon.erp.sys.addon.base.model.enums.FieldSourceTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class FieldService extends AbstractService implements IExtendFieldSupportService {
    @Override
    public String getServiceName() {
        return "base.field";
    }

    protected final Field label = Fields.createString("字段名");
    protected final Field isPrimaryKey = Fields.createBoolean("主键", false, false);
    protected final Field isAutoIncrement = Fields.createBoolean("自增", false, false);
    protected final Field isRequired = Fields.createBoolean("必填", false, false);
    protected final Field isReadonly = Fields.createBoolean("只读", false, false);
    protected final Field defaultValue = Fields.createText("默认值", false);
    protected final Field type = Fields.createFieldSelectionField();
    protected final Field serviceId = Fields.createMany2one("服务", "base.service");
    protected final Field isUnique = Fields.createBoolean("唯一", false, false);
    protected final Field allowNull = Fields.createBoolean("允许空", false, false);
    protected final Field isMulti = Fields.createBoolean("多选", false, false);
    protected final Field minValue = Fields.createBigDecimal("最小值", 50, 6);
    protected final Field maxValue = Fields.createBigDecimal("最大值", 50, 6);

    public final Field masterForeignKeyName = Fields.createString("主外键");
    public final Field relativeForeignKeyName = Fields.createString("关联表外键");
    public final Field relativeServiceName = Fields.createString("关联服务");
    public final Field manyServiceTable = Fields.createString("多对多关联服务");
    public final Field relativeFieldName = Fields.createString("关联字段");

    public final Field sourceType = Fields.createSelection("来源类型", FieldSourceTypeEnum.class,
            FieldSourceTypeEnum.custom);


    @Override
    public PrimaryKey insert(RecordRow recordRow) throws AvalonException {
        PrimaryKey key = super.insert(recordRow);

        Record select = select(Condition.equalCondition("id", key.getInteger()), "serviceId.name");
        String targetService = select.get(0).getRecordRow("serviceId").getString("name");
        AbstractService serviceBean = getContext().getServiceBean(targetService);
        if(ObjectUtils.isNotNull(serviceBean)) {
            serviceBean.updateExtendField();
            serviceBean.upgradeExtendFieldTable();
        }

        return key;
    }

    @Override
    public Integer update(RecordRow recordRow) throws AvalonException {
        Integer update = super.update(recordRow);

        Record select = select(Condition.equalCondition("id", recordRow.getInteger("id")), "serviceId.name");
        String targetService = select.get(0).getRecordRow("serviceId").getString("name");
        AbstractService serviceBean = getContext().getServiceBean(targetService);
        serviceBean.updateExtendField();
        serviceBean.upgradeTable();

        return update;
    }

    @Override
    public Integer delete(RecordRow row) throws AvalonException {
        String name = getFieldValue("name",
                Condition.equalCondition("id", row.getInteger("id"))).getString();
        Record select = select(Condition.equalCondition("id", row.getInteger("id")), "serviceId.name");
        String targetService = select.get(0).getRecordRow("serviceId").getString("name");
        AbstractService serviceBean = getContext().getServiceBean(targetService);
        serviceBean.dropField(name);
        return super.delete(row);
    }

    private Record selectExtendField(Condition condition) {
        return select(condition, "id", "name", "label", "isPrimaryKey", "isRequired", "isReadonly",
                "defaultValue", "type", "serviceId", "isUnique", "allowNull", "isMulti", "minValue", "maxValue",
                "masterForeignKeyName", "relativeForeignKeyName", "relativeServiceName", "manyServiceTable",
                "relativeFieldName", "sourceType", "isAutoIncrement");
    }

    @Override
    public FieldList getExtendField(String service) {
        Condition condition = Condition.equalCondition("serviceId.name", service)
                .andEqualCondition(sourceType, FieldSourceTypeEnum.custom);
        Record fieldRecord = select(condition, "id", "name", "label", "isPrimaryKey", "isRequired", "isReadonly",
                "defaultValue", "type", "serviceId", "isUnique", "allowNull", "isMulti", "minValue", "maxValue",
                "masterForeignKeyName", "relativeForeignKeyName", "relativeServiceName", "manyServiceTable",
                "relativeFieldName", "sourceType", "isAutoIncrement");
        FieldList fields = new FieldList();
        for (RecordRow recordRow : fieldRecord) {
            Field field = buildField(recordRow, service);

            if (ObjectUtils.isNotNull(field)) {
                fields.add(field);
            }
        }
        return fields;
    }

    protected Field buildField(RecordRow recordRow, String service) {
        return buildField(
                recordRow.getString("type"),
                recordRow.getString("name"),
                recordRow.getString("label"),
                recordRow.getBoolean("isPrimaryKey"),
                recordRow.getBoolean("isRequired"),
                recordRow.getBoolean("isReadonly"),
                recordRow.isNotNull(defaultValue) ? recordRow.getString("defaultValue") : "",
                service,
                recordRow.getBoolean("isUnique"),
                recordRow.getBoolean("allowNull"),
                recordRow.getBoolean("isMulti"),
                recordRow.getBoolean("isAutoIncrement"),
                recordRow.getBigDecimal("minValue"),
                recordRow.getBigDecimal("maxValue"),
                recordRow.isNotNull(masterForeignKeyName) ? recordRow.getString("masterForeignKeyName") : "",
                recordRow.isNotNull(relativeForeignKeyName) ? recordRow.getString("relativeForeignKeyName") : "",
                recordRow.isNotNull(relativeServiceName) ? recordRow.getString("relativeServiceName") : "",
                recordRow.isNotNull(manyServiceTable) ? recordRow.getString("manyServiceTable") : "",
                recordRow.isNotNull(relativeFieldName) ? recordRow.getString("relativeFieldName") : "");
    }

    protected Field buildField(String type, String name, String label, boolean isPrimaryKey, boolean isRequired,
                               boolean isReadonly, String defaultValue, String serviceId, boolean isUnique,
                               boolean allowNull, boolean isMulti, boolean isAutoIncrement, BigDecimal minValue, BigDecimal maxValue,
                               String masterForeignKeyName, String relativeForeignKeyName, String relativeServiceName,
                               String manyServiceTable, String relativeFieldName) {
        if (IntegerField.class.getSimpleName().equals(type)) {
            return buildIntegerField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, minValue, maxValue);
        } else if (StringField.class.getSimpleName().equals(type)) {
            return buildStringField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, minValue, maxValue);
        } else if (TextField.class.getSimpleName().equals(type)) {
            return buildTextField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, minValue, maxValue);
        } else if (HtmlField.class.getSimpleName().equals(type)) {
            return buildHtmlField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, minValue, maxValue);
        } else if (BooleanField.class.getSimpleName().equals(type)) {
            return buildBooleanField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement);
        } else if (BigDecimalField.class.getSimpleName().equals(type)) {
            return buildBigDecimalField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, minValue, maxValue);
        } else if (BigIntegerField.class.getSimpleName().equals(type)) {
            return buildBigIntegerField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, minValue, maxValue);
        } else if (DateField.class.getSimpleName().equals(type)) {
            return buildDateField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull);
        } else if (DateTimeField.class.getSimpleName().equals(type)) {
            return buildDateTimeField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull);
        } else if (DoubleField.class.getSimpleName().equals(type)) {
            return buildDoubleField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, minValue, maxValue);
        } else if (ImageField.class.getSimpleName().equals(type)) {
            return buildImageField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement);
        } else if (Many2manyField.class.getSimpleName().equals(type)) {
            return buildMany2manyField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, masterForeignKeyName, relativeForeignKeyName, relativeServiceName,
                    manyServiceTable);
        } else if (Many2oneField.class.getSimpleName().equals(type)) {
            return buildMany2oneField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, relativeServiceName);
        } else if (One2manyField.class.getSimpleName().equals(type)) {
            return buildOne2manyField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, relativeServiceName, relativeFieldName);
        } else if (One2oneField.class.getSimpleName().equals(type)) {
            return buildOne2oneField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull, isAutoIncrement, relativeServiceName, relativeFieldName);
        } else if (PasswordField.class.getSimpleName().equals(type)) {
            return buildPasswordField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull);
        } else if (TimeField.class.getSimpleName().equals(type)) {
            return buildTimeField(name, label, isPrimaryKey, isRequired, isReadonly, defaultValue, serviceId, isUnique,
                    allowNull);
        } else {
            return null;
        }
    }

    private Field buildIntegerField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                    String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                    BigDecimal minValue, BigDecimal maxValue) {
        IntegerField.Builder builder = new IntegerField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new IntegerFieldDefaultValue(Integer.parseInt(defaultValue)));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setMinValue(minValue.intValue());
        builder.setMaxValue(maxValue.intValue());
        builder.setService(getContext().getServiceBean(service));

        return builder.build();
    }

    private Field buildStringField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                   String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                   BigDecimal minValue, BigDecimal maxValue) {
        StringField.Builder builder = new StringField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setMinLength(minValue.intValue());
        builder.setMaxLength(maxValue.intValue());
        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildTextField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                 String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                 BigDecimal minValue, BigDecimal maxValue) {
        TextField.Builder builder = new TextField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setMinLength(minValue.intValue());
        builder.setMaxLength(maxValue.intValue());
        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildHtmlField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                 String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                 BigDecimal minValue, BigDecimal maxValue) {
        HtmlField.Builder builder = new HtmlField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setMinLength(minValue.intValue());
        builder.setMaxLength(maxValue.intValue());
        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildBooleanField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                    String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement) {
        BooleanField.Builder builder = new BooleanField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildBigDecimalField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                       String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                       BigDecimal minValue, BigDecimal maxValue) {
        BigDecimalField.Builder builder = new BigDecimalField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new IntegerFieldDefaultValue(Integer.parseInt(defaultValue)));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setMinValue(minValue);
        builder.setMaxValue(maxValue);
        builder.setService(getContext().getServiceBean(service));

        return builder.build();
    }

    private Field buildBigIntegerField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                       String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                       BigDecimal minValue, BigDecimal maxValue) {
        BigIntegerField.Builder builder = new BigIntegerField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new IntegerFieldDefaultValue(Integer.parseInt(defaultValue)));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setMinValue(minValue.toBigInteger().longValue());
        builder.setMaxValue(maxValue.toBigInteger().longValue());
        builder.setService(getContext().getServiceBean(service));

        return builder.build();
    }

    private Field buildDateField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                 String defaultValue, String service, boolean isUnique, boolean allowNull) {
        DateField.Builder builder = new DateField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new DateFieldDefaultValue());
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setService(getContext().getServiceBean(service));

        return builder.build();
    }

    private Field buildDateTimeField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                     String defaultValue, String service, boolean isUnique, boolean allowNull) {
        DateTimeField.Builder builder = new DateTimeField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new DateFieldDefaultValue());
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setService(getContext().getServiceBean(service));

        return builder.build();
    }

    private Field buildDoubleField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                   String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                   BigDecimal minValue, BigDecimal maxValue) {
        DoubleField.Builder builder = new DoubleField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new IntegerFieldDefaultValue(Integer.parseInt(defaultValue)));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setMinValue(minValue.doubleValue());
        builder.setMaxValue(maxValue.doubleValue());
        builder.setService(getContext().getServiceBean(service));

        return builder.build();
    }

    private Field buildImageField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                  String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement) {
        ImageField.Builder builder = new ImageField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);
        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildMany2manyField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                      String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                      String masterForeignKeyName, String relativeForeignKeyName, String relativeServiceName,
                                      String manyServiceTable) {
        Many2manyField.Builder builder = new Many2manyField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);

        builder.setMasterKeyName(masterForeignKeyName);
        builder.setRelativeKeyName(relativeForeignKeyName);
        builder.setRelativeServiceName(relativeServiceName);
        builder.setTableName(manyServiceTable);

        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildMany2oneField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                     String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                     String relativeServiceName) {
        Many2oneField.Builder builder = new Many2oneField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);

        builder.setRelativeServiceName(relativeServiceName);

        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildOne2manyField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                     String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                     String relativeServiceName, String relativeFieldName) {
        One2manyField.Builder builder = new One2manyField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);

        builder.setRelativeServiceName(relativeServiceName);
        builder.setRelativeFieldName(relativeFieldName);

        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }


    private Field buildOne2oneField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                    String defaultValue, String service, boolean isUnique, boolean allowNull, boolean isAutoIncrement,
                                    String relativeServiceName, String relativeFieldName) {
        One2oneField.Builder builder = new One2oneField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);
        builder.setIsAutoIncrement(isAutoIncrement);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);

        builder.setRelativeServiceName(relativeServiceName);
        builder.setRelativeFieldName(relativeFieldName);

        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildPasswordField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                     String defaultValue, String service, boolean isUnique, boolean allowNull) {
        PasswordField.Builder builder = new PasswordField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);

        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }

    private Field buildTimeField(String name, String label, boolean isPrimaryKey, boolean isRequired, boolean isReadonly,
                                 String defaultValue, String service, boolean isUnique, boolean allowNull) {
        TimeField.Builder builder = new TimeField.Builder();
        builder.setFieldName(name);
        builder.setLabel(label);
        builder.setIsPrimaryKey(isPrimaryKey);
        builder.setIsRequired(isRequired);
        builder.setIsReadonly(isReadonly);

        if (StringUtils.isNotEmpty(defaultValue)) {
            builder.setDefaultValue(new StringFieldDefaultValue(defaultValue));
        }

        builder.setIsUnique(isUnique);
        builder.setAllowNull(allowNull);

        builder.setService(getContext().getServiceBean(service));
        return builder.build();
    }
}

