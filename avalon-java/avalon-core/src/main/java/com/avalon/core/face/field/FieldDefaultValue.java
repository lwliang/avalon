/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;

public class FieldDefaultValue implements IFieldDefaultValue {
    public FieldDefaultValue() {
    }

    public FieldDefaultValue(String scriptValue) {
        this.scriptValue = scriptValue;
    }

    public FieldDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Object getDefault(Context context) {
        return context.executeScript(getDefaultString());
    }

    private Object defaultValue;

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    private String scriptValue;

    @Override
    public String getDefaultString() {
        return this.scriptValue;
    }

    @Override
    public void setDefaultString(String scriptValue) {
        this.scriptValue = scriptValue;
    }

    private static final FieldDefaultValue instance = new FieldDefaultValue();

    public static FieldDefaultValue getInstance() {
        return instance;
    }
}
