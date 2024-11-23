/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;

public class FloatFieldDefaultValue extends FieldDefaultValue {
    @Override
    public Object getDefault(Context context) {
        return 0f;
    }

    @Override
    public String getDefaultString() {
        return "0";
    }

    private static final FloatFieldDefaultValue instance = new FloatFieldDefaultValue();

    public static FloatFieldDefaultValue getInstance() {
        return instance;
    }
}
