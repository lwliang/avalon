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
    public String toString() {
        return super.toString();
    }
}
