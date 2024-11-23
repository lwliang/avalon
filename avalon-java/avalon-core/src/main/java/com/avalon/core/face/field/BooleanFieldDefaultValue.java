/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;
import com.avalon.core.util.ObjectUtils;

public class BooleanFieldDefaultValue extends FieldDefaultValue {

    private Boolean defaultValue;

    @Override
    public Object getDefault(Context context) {

        return ObjectUtils.isNotNull(defaultValue) ? defaultValue : false;
    }

    @Override
    public String getDefaultString() {
        return "false";
    }

    public BooleanFieldDefaultValue() {
    }

    public BooleanFieldDefaultValue(Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    private static final BooleanFieldDefaultValue instance = new BooleanFieldDefaultValue();

    public static BooleanFieldDefaultValue getInstance() {
        return instance;
    }
}
