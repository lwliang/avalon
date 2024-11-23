/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;

public class DoubleFieldDefaultValue extends FieldDefaultValue {
    @Override
    public Object getDefault(Context context) {
        return (double) 0;
    }

    @Override
    public String getDefaultString() {
        return "0";
    }

    private static final DoubleFieldDefaultValue instance = new DoubleFieldDefaultValue();

    public static DoubleFieldDefaultValue getInstance() {
        return instance;
    }
}
