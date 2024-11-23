/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;

import java.math.BigDecimal;

public class BigDecimalFieldDefaultValue extends FieldDefaultValue {
    @Override
    public Object getDefault(Context context) {
        return BigDecimal.ZERO;
    }

    private static final BigDecimalFieldDefaultValue instance = new BigDecimalFieldDefaultValue();

    public static BigDecimalFieldDefaultValue getInstance() {
        return instance;
    }

    @Override
    public String getDefaultString() {
        return "0";
    }
}
