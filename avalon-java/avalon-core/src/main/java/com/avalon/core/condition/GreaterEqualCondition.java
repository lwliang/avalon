/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;
import lombok.Data;

@Data
public class GreaterEqualCondition extends Condition {
    public GreaterEqualCondition(String name, Object value) {
        super();
        setOp(ConditionOperateEnum.GreaterEqual);
        setName(name);
        setValue(value);
    }

    public GreaterEqualCondition(Field fieldName, Object value) {
        super();
        setOp(ConditionOperateEnum.GreaterEqual);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.greaterEqualCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
