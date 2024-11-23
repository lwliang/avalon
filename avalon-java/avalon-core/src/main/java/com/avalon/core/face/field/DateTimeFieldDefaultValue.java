/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;
import com.avalon.core.util.DateTimeUtils;

public class DateTimeFieldDefaultValue extends FieldDefaultValue {
    @Override
    public Object getDefault(Context context) {
        return DateTimeUtils.getCurrentDate();
    }

    @Override
    public String getDefaultString() {
        return "DateTimeUtils.getCurrentDate()";
    }

    private static final DateTimeFieldDefaultValue instance = new DateTimeFieldDefaultValue();

    public static DateTimeFieldDefaultValue getInstance() {
        return instance;
    }
}
