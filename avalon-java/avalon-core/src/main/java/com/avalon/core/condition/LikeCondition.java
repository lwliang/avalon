/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;
import com.avalon.core.util.StringUtils;
import lombok.Data;

@Data
public class LikeCondition extends Condition {
    public LikeCondition(String name, Object value) {
        setOp(ConditionOperateEnum.Like);
        setName(name);
        setValue(value);
    }

    public LikeCondition(Field fieldName, Object value) {
        setOp(ConditionOperateEnum.Like);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        if (values.length == 2) { // 如果只有两个元素，说明没有输入值，直接返回一个空的like条件
            return Condition.likeCondition(values[1], StringUtils.EMPTY);
        }
        return Condition.likeCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
