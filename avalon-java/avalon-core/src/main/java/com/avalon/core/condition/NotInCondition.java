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
    public NotInCondition(String name, List<Object> values) {
        super(name, values);
        setOp(ConditionOperateEnum.NotIn);
    }

    public NotInCondition(Field fieldName, List<Object> values) {
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

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        List<Object> inValues = new ArrayList<>(Arrays.asList(values).subList(2, values.length));
        return Condition.notInCondition(values[1], inValues);
    }
}
