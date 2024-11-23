/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;
import lombok.Data;

@Data
public class GreaterCondition extends Condition {

    public GreaterCondition(String name, Object value) {
        super();
        setOp(ConditionOperateEnum.Greater);
        setName(name);
        setValue(value);
    }

    public GreaterCondition(Field fieldName, Object value) {
        super();
        setOp(ConditionOperateEnum.Greater);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.greaterCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
