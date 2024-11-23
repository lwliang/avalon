/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;
import lombok.Data;

@Data
public class LessCondition extends Condition {

    public LessCondition(String name, Object value) {
        setOp(ConditionOperateEnum.Less);
        setName(name);
        setValue(value);
    }

    public LessCondition(Field fieldName, Object value) {
        setOp(ConditionOperateEnum.Less);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.lessCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
