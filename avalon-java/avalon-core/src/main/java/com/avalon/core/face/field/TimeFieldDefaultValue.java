/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;
import com.avalon.core.util.DateTimeUtils;

public class TimeFieldDefaultValue extends FieldDefaultValue {
    @Override
    public Object getDefault(Context context) {
        return DateTimeUtils.getCurrentTime();
    }

    @Override
    public String getDefaultString() {
        return "DateTimeUtils.getCurrentTime()";
    }

    private static final TimeFieldDefaultValue instance = new TimeFieldDefaultValue();

    public static TimeFieldDefaultValue getInstance() {
        return instance;
    }
}
