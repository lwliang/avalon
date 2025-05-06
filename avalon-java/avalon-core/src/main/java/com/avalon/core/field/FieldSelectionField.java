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
        selectionHashMap.put(BigDecimalField.class.getSimpleName(), "BigDecimal");
        selectionHashMap.put(BigIntegerField.class.getSimpleName(), "Long");
        selectionHashMap.put(IntegerField.class.getSimpleName(), "整数");
        selectionHashMap.put(BooleanField.class.getSimpleName(), "Bool");
        selectionHashMap.put(DateField.class.getSimpleName(), "日期");
        selectionHashMap.put(DateTimeField.class.getSimpleName(), "日期时间");
        selectionHashMap.put(DoubleField.class.getSimpleName(), "Double");
        selectionHashMap.put(FileField.class.getSimpleName(), "文件");
        selectionHashMap.put(FloatField.class.getSimpleName(), "Float");
        selectionHashMap.put(TimeField.class.getSimpleName(), "时间");
        selectionHashMap.put(HtmlField.class.getSimpleName(), "HTML");
        selectionHashMap.put(DrawField.class.getSimpleName(), "Draw");
        selectionHashMap.put(ImageField.class.getSimpleName(), "图片");
        selectionHashMap.put(VideoField.class.getSimpleName(), "视频");
        selectionHashMap.put(StringField.class.getSimpleName(), "单行文本");
        selectionHashMap.put(PasswordField.class.getSimpleName(), "密码");
        selectionHashMap.put(TextField.class.getSimpleName(), "多行文本");
        selectionHashMap.put(SelectionField.class.getSimpleName(), "字典");
        selectionHashMap.put(One2oneField.class.getSimpleName(), "1对1");
        selectionHashMap.put(One2manyField.class.getSimpleName(), "1对多");
        selectionHashMap.put(Many2oneField.class.getSimpleName(), "多对1");
        selectionHashMap.put(Many2manyField.class.getSimpleName(), "多对多");
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
