/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.model.SelectionHashMap;
import com.avalon.core.util.ObjectUtils;

public class FieldSelectionField extends SelectionField {

    public FieldSelectionField(String label) {
        this(label, null);
        if (ObjectUtils.isNull(label)) {
            label = "类型选择";
        }
        SelectionHashMap selectionHashMap = new SelectionHashMap();
        selectionHashMap.put(BigDecimalField.class.getName(), "实数");
        selectionHashMap.put(IntegerField.class.getName(), "整数");
        selectionHashMap.put(DateField.class.getName(), "日期");
        selectionHashMap.put(DateTimeField.class.getName(), "日期时间");
        selectionHashMap.put(TimeField.class.getName(), "时间");
        selectionHashMap.put(HtmlField.class.getName(), "HTML");
        selectionHashMap.put(ImageField.class.getName(), "图片");
        selectionHashMap.put(StringField.class.getName(), "单行文本");
        selectionHashMap.put(TextField.class.getName(), "多行文本");
        setSection(selectionHashMap);
        setLabel(label);
    }

    public FieldSelectionField(Builder builder) {
        super(builder);
    }

    public FieldSelectionField(String label, SelectionHashMap section) {
        super(label, section);
    }

    public FieldSelectionField(String label, SelectionHashMap section, Boolean isMulti) {
        super(label, section, isMulti);
    }

    public FieldSelectionField(String label, Boolean isRequired, SelectionHashMap section) {
        super(label, isRequired, section);
    }

    public FieldSelectionField(String label, Boolean isRequired, Boolean isReadonly, SelectionHashMap section) {
        super(label, isRequired, isReadonly, section);
    }

    public FieldSelectionField(String label, Boolean isRequired, SelectionHashMap section, IFieldDefaultValue defaultValue) {
        super(label, isRequired, section, defaultValue);
    }

    public FieldSelectionField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, SelectionHashMap section) {
        super(label, isRequired, isReadonly, defaultValue, section);
    }

    public FieldSelectionField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, SelectionHashMap section, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, defaultValue, section, isPrimaryKey, isAutoIncrement);
    }


    public static class Builder extends SelectionField.Builder {
        @Override
        public Field build() {
            return new FieldSelectionField(this);
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
