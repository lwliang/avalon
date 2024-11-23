/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;

public class LongFieldDefaultValue extends FieldDefaultValue {


    @Override
    public Long getDefault(Context context) {
        return 0L;
    }

    @Override
    public String getDefaultString() {
        return "0";
    }

    private static final LongFieldDefaultValue instance = new LongFieldDefaultValue();

    public static LongFieldDefaultValue getInstance() {
        return instance;
    }
}
