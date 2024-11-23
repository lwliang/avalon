/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;

/**
 * description: NotLikeCondition
 * date: 2022/8/26 11:22
 * author: AN
 * version: 1.0
 */
public class NotLikeCondition extends Condition{
    public NotLikeCondition(String name, Object value) {
        setOp(ConditionOperateEnum.NotLike);
        setName(name);
        setValue(value);
    }

    public NotLikeCondition(Field fieldName, Object value) {
        setOp(ConditionOperateEnum.NotLike);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.notLikeCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
