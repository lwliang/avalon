/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.erp.sys.addon.external.service;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.face.field.FieldDefaultValue;
import com.avalon.core.field.*;
import com.avalon.core.model.RecordRow;
import com.avalon.core.service.AbstractService;
import com.avalon.erp.sys.addon.base.service.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExternalFieldService extends FieldService {
    @Override
    public String getServiceName() {
        return "external.field";
    }

    protected final Field isExternal = Fields.createBoolean("扩展");

    public Field register(RecordRow row, AbstractService service) {
        String fieldType = row.getString(this.type);

        if (BigDecimalField.class.getName().equals(fieldType)) {
            return createBigDecimal(row);
        }

        if (IntegerField.class.getName().equals(fieldType)) {
            return createInteger(row);
        }

        if (DateField.class.getName().equals(fieldType)) {
            return createDate(row);
        }

        if (DateTimeField.class.getName().equals(fieldType)) {
            return createDateTime(row);
        }

        if (TimeField.class.getName().equals(fieldType)) {
            return createTime(row);
        }

        if (HtmlField.class.getName().equals(fieldType)) {
            return createHtml(row);
        }

        if (ImageField.class.getName().equals(fieldType)) {
            return createImage(row);
        }

        if (StringField.class.getName().equals(fieldType)) {
            return createString(row);
        }

        if (TextField.class.getName().equals(fieldType)) {
            return createText(row);
        }

        throw new AvalonException("不支持的字段类型: " + fieldType);
    }

    public Field createTime(RecordRow row) {
        return new TimeField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .build();
    }

    public Field createText(RecordRow row) {
        return new TextField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .setMaxLength(row.getInteger(maxValue))
                .setMinLength(row.getInteger(minValue))
                .setSize(row.getInteger(maxValue))
                .build();
    }

    public Field createDateTime(RecordRow row) {
        return new DateTimeField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .build();
    }

    public Field createImage(RecordRow row) {
        return new ImageField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .build();
    }

    public Field createHtml(RecordRow row) {
        return new HtmlField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .setMaxLength(row.getInteger(maxValue))
                .setMinLength(row.getInteger(minValue))
                .setSize(row.getInteger(maxValue))
                .build();
    }

    public Field createString(RecordRow row) {
        return new StringField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .setMaxLength(row.getInteger(maxValue))
                .setMinLength(row.getInteger(minValue))
                .build();
    }

    public Field createDate(RecordRow row) {
        return new DateField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .build();
    }

    public Field createInteger(RecordRow row) {
        return new IntegerField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .setMaxValue(row.getInteger(maxValue))
                .setMinValue(row.getInteger(minValue))
                .build();
    }

    public Field createBigDecimal(RecordRow row) {
        return new BigDecimalField.Builder()
                .setFieldName(row.getString("name"))
                .setAllowNull(row.getBoolean(allowNull))
                .setDefaultValue((createDefaultValue(row.getString(defaultValue))))
                .setIsAutoIncrement(row.getBoolean(isAutoIncrement))
                .setIsPrimaryKey(row.getBoolean(isPrimaryKey))
                .setIsReadonly(row.getBoolean(isReadonly))
                .setIsRequired(row.getBoolean(isRequired))
                .setIsUnique(row.getBoolean(isUnique))
                .setLabel(row.getString(label))
                .setMaxValue(row.getBigDecimal(maxValue))
                .setMinValue(row.getBigDecimal(minValue))
                .build();
    }

    private FieldDefaultValue createDefaultValue(String scriptValue) {
        return new FieldDefaultValue(scriptValue);
    }
}