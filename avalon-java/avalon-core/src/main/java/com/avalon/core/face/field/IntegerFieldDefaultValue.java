/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;
import com.avalon.core.util.ObjectUtils;

public class IntegerFieldDefaultValue extends FieldDefaultValue {

    private Integer defaultValue;

    public IntegerFieldDefaultValue() {

    }

    @Override
    public Object getDefault(Context context) {
        if (ObjectUtils.isNotNull(defaultValue)) {
            return defaultValue;
        }
        return null;
    }

    @Override
    public String getDefaultString() {
        if (ObjectUtils.isNull(defaultValue)) {
            return "0";
        }
        return defaultValue.toString();
    }

    public IntegerFieldDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    private static final IntegerFieldDefaultValue instance = new IntegerFieldDefaultValue();

    public static IntegerFieldDefaultValue getInstance() {
        return instance;
    }
}
