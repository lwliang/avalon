/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;
import com.avalon.core.util.DateTimeUtils;

public class DateFieldDefaultValue extends FieldDefaultValue {
    @Override
    public Object getDefault(Context context) {
        return DateTimeUtils.getCurrentDate();
    }

    @Override
    public String getDefaultString() {
        return "DateTimeUtils.getCurrentDate()";
    }

    private static final DateFieldDefaultValue instance = new DateFieldDefaultValue();

    public static DateFieldDefaultValue getInstance() {
        return instance;
    }
}
