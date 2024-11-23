/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.face.field;

import com.avalon.core.context.Context;

public interface IFieldDefaultValue {
    Object getDefault(Context context);

    /**
     * 获取默认值的脚本
     *
     * @return 脚本
     */
    String getDefaultString();

    Object getDefaultValue();


    void setDefaultString(String scriptValue);

    static IFieldDefaultValue getStringFieldFaultValue() {
        return StringFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getIntegerFieldDefaultValue() {
        return IntegerFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getLongFieldDefaultValue() {
        return LongFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getTimeFieldDefaultValue() {
        return TimeFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getFloatFieldDefaultValue() {
        return FloatFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getDoubleFieldDefaultValue() {
        return DoubleFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getDateTimeFieldDefaultValue() {
        return DateTimeFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getDateFieldDefaultValue() {
        return DateFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getBigDecimalFieldDefaultValue() {
        return BigDecimalFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getBooleanFieldDefaultValue() {
        return BooleanFieldDefaultValue.getInstance();
    }

    static IFieldDefaultValue getFieldDefaultValue() {
        return FieldDefaultValue.getInstance();
    }
}
