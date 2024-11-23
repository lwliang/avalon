/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;
import com.avalon.core.util.ObjectUtils;

public class StringFieldDefaultValue extends FieldDefaultValue {

    private String defaultValue;

    public StringFieldDefaultValue() {
    }

    public StringFieldDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Object getDefault(Context context) {
        if (ObjectUtils.isNotNull(defaultValue)) {
            return defaultValue;
        }
        return "";
    }

    @Override
    public String getDefaultString() {
        return "";
    }

    private static final StringFieldDefaultValue instance = new StringFieldDefaultValue();

    public static StringFieldDefaultValue getInstance() {
        return instance;
    }

}
