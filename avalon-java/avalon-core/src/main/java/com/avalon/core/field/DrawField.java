/**
 * @author lwlianghehe@gmail.com
 * @date 2025/04/20
 */

package com.avalon.core.field;

import com.avalon.core.face.field.IFieldDefaultValue;
import com.avalon.core.util.HtmlUtils;
import com.avalon.core.util.ObjectUtils;

// 绘画字段
public class DrawField extends StringField {
    public DrawField(Builder builder) {
        super(builder);
    }

    public DrawField(String label) {
        super(label);
    }

    public DrawField(String label, Integer length) {
        super(label, length);
    }

    public DrawField(String label, Boolean isRequired, Integer length) {
        super(label, isRequired, length);
    }

    public DrawField(String label, Boolean isRequired, Boolean isReadonly, Integer length) {
        super(label, isRequired, isReadonly, length);
    }

    public DrawField(String label, Boolean isRequired, Boolean isReadonly, Integer size, Integer maxLength) {
        super(label, isRequired, isReadonly, false, size, maxLength);
    }

    public DrawField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue) {
        super(label, isRequired, isReadonly, false, defaultValue);
    }

    public DrawField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, Integer size, Integer maxLength) {
        super(label, isRequired, isReadonly, false, defaultValue, size, maxLength);
    }

    public DrawField(String label, Boolean isRequired, Boolean isReadonly, IFieldDefaultValue defaultValue, Integer size, Integer maxLength, Integer minLength, Boolean isPrimaryKey, Boolean isAutoIncrement) {
        super(label, isRequired, isReadonly, false, defaultValue, size, maxLength, minLength, isPrimaryKey, isAutoIncrement);
    }

    /**
     * 将html字符串转text字符串
     *
     * @param html
     * @return
     */
    public String toText(String html) {
        if (ObjectUtils.isEmpty(html)) {
            return html;
        }
        return HtmlUtils.splitAndFilterString(html, html.length());
    }

    @Override
    public String getCreateTableSql() {
        return "TEXT";
    }

    @Override
    public String getSampleCreateTableSql() {
        return getCreateTableSql();
    }

    @Override
    public Boolean needLog() {
        return false;
    }

    public static class Builder extends StringField.Builder {
        @Override
        public Field build() {
            return new DrawField(this);
        }

        public static Builder getInstance() {
            return new Builder();
        }
    }
}
