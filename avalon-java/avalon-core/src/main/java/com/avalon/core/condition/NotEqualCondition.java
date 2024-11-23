/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;

@Data
public class NotEqualCondition extends Condition {

    public NotEqualCondition(String name, Object value) {
        setOp(ConditionOperateEnum.NotEqual);
        setName(name);
        setValue(value);
    }

    public NotEqualCondition(Field fieldName, Object value) {
        setOp(ConditionOperateEnum.NotEqual);
        setFieldName(fieldName);
        setValue(value);
    }

    @Override
    public String getSql(AbstractService service, FieldValueStatement valueStatement) {
        Field field = getField(service);
        if (ObjectUtils.isNull(getValue())) {
            return String.format("%s is not null", Fields.underscoreName(getRealName()));
        }
        valueStatement.put(field, getValue());
        return String.format(getOp().getValue(), Fields.underscoreName(getRealName()), "?");
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String conditionAlias = builder.getQueryRoot().getFieldWithAliasTable(getRealName(),
                builder.getAliasSupport());
        Field field = builder.getService().getField(getRealName());
        if (ObjectUtils.isNotNull(getValue())) {
            Object value = getValue();
            if (value instanceof String) {
                value = builder.getService().getContext().executeScript((String) value);
            }
            builder.getFieldValueStatement().put(field, value);
            return String.format(getOp().getValue(), conditionAlias, "?");
        } else {
            return String.format("%s is not null", conditionAlias);
        }
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.notEqualCondition(values[1], values[2]);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
