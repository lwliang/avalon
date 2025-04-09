/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotInCondition extends InCondition {
    public NotInCondition(String name, List<?> values) {
        super(name, values);
        setOp(ConditionOperateEnum.NotIn);
    }

    public NotInCondition(Field fieldName, List<?> values) {
        super(fieldName, values);
        setOp(ConditionOperateEnum.NotIn);
    }

    public NotInCondition(String name, Object... values) {
        super(name, values);
        setOp(ConditionOperateEnum.NotIn);
    }

    public NotInCondition(Field fieldName, Object... values) {
        super(fieldName, values);
        setOp(ConditionOperateEnum.NotIn);
    }
}
