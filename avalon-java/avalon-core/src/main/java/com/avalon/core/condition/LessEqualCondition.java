/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessEqualCondition extends Condition {
    public LessEqualCondition(String name, Object value) {
        setOp(ConditionOperateEnum.LessEqual);
        setName(name);
        setValue(value);
    }

    public LessEqualCondition(Field fieldName, Object value) {
        setOp(ConditionOperateEnum.LessEqual);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.lessEqualCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
