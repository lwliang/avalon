/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;

public class TextField extends StringField {
    public TextField(Builder builder) {
        super(builder);
    }

    public TextField(String label) {
        super(label);
    }

    public TextField(String label, Integer length) {
        super(label, length);
    }

    public TextField(String label, Boolean isRequired, Integer length) {
        super(label, isRequired, length);
    }

    public TextField(String label, Boolean isRequired, Boolean isReadonly, Integer length) {
        super(label, isRequired, isReadonly, length);
    }

    public TextField(String label, Boolean isRequired, Boolean isReadonly, Integer size, Integer maxLength) {
        super(label, isRequired, isReadonly, false, size, maxLength);
    }

    public TextField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue) {
        super(label, isRequired, isReadonly, false, defaultValue);
    }

    public TextField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue,
                     Integer size, Integer maxLength) {
        super(label, isRequired, isReadonly, false, defaultValue, size, maxLength);
    }

    public TextField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue,
                     Integer size, Integer maxLength, Integer minLength, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, false, defaultValue, size, maxLength, minLength, isPrimaryKey, isAutoIncrement);
    }

    @Override
    public String getSampleCreateTableSql() {
        return getCreateTableSql();
    }

    @Override
    public String getCreateTableSql() {
        return "TEXT";
    }

    @Override
    public Boolean needLog() {
        return false;
    }


    public static class Builder extends StringField.Builder {
        @Override
        public Field build() {
            return new TextField(Builder.this);
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
