/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;

@Data
public class EqualCondition extends Condition {
    public EqualCondition(String name, Object value) {
        super();
        setOp(ConditionOperateEnum.Equal);
        setName(name);
        setValue(value);
    }

    public EqualCondition(Field fieldName, Object value) {
        super();
        setOp(ConditionOperateEnum.Equal);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    public String getSql(AbstractService service, FieldValueStatement valueStatement) {
        Field field = service.getField(getRealName());
        if (ObjectUtils.isNull(getValue())) {
            return String.format("%s is null", Fields.underscoreName(getRealName()));
        }
        valueStatement.put(field, getValue());
        return String.format(getOp().getValue(), Fields.underscoreName(getRealName()), "?");
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String conditionAlias = builder.getQueryRoot().getFieldWithAliasTable(getRealName(),
                builder.getAliasSupport());
        Field field = builder.getService().getField(getRealName());
        if (ObjectUtils.isNull(field)) {
            throw new AvalonException("field not found: " + getRealName());
        }
        if (ObjectUtils.isNotNull(getValue())) {
            builder.getFieldValueStatement().put(field, getValue());
            return String.format(getOp().getValue(), conditionAlias, "?");
        } else {
            return String.format("%s is null", conditionAlias);
        }
    }

    @Override
    public String getReversePolishNotation() {
        return super.getReversePolishNotation();
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.equalCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
