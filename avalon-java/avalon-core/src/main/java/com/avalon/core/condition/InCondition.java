/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.JoinRelationMap;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class InCondition extends Condition {

    private List<Object> values;

    public InCondition(String name, List<Object> values) {
        super();
        setOp(ConditionOperateEnum.In);
        setName(name);
        setValue(values);
        this.values = values;
    }

    public InCondition(Field fieldName, List<Object> values) {
        super();
        setOp(ConditionOperateEnum.In);
        setFieldName(fieldName);
        setValue(values);
        this.values = values;
    }

    public InCondition(String name, Object... values) {
        super();
        setOp(ConditionOperateEnum.In);
        setName(name);
        this.values = ObjectUtils.toList(values);
        setValue(this.values);
    }

    public InCondition(Field fieldName, Object... values) {
        super();
        setOp(ConditionOperateEnum.In);
        setFieldName(fieldName);
        this.values = ObjectUtils.toList(values);
        setValue(this.values);
    }

    @Override
    public String getSql(AbstractService service, FieldValueStatement valueStatement) {

        Field field = getField(service);

        if (getValue() instanceof List) {
            List<Object> values = (List<Object>) getValue();

            StringBuilder sb = new StringBuilder();
            for (Object value : values) {
                if (sb.length() == 0) {
                    sb.append("?");
                } else {
                    sb.append(",?");
                }
                valueStatement.put(field, value);
            }
            return String.format(getOp().getValue(), Fields.underscoreName(getRealName()), sb);
        }
        valueStatement.put(field, getValue());
        return String.format(getOp().getValue(), Fields.underscoreName(getRealName()), getValue().toString());
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String conditionAlias = builder.getQueryRoot().getFieldWithAliasTable(getRealName(),
                builder.getAliasSupport());
        Field field = builder.getService().getField(getRealName());
        StringBuilder sb = new StringBuilder();
        for (Object value : values) {
            if (sb.length() == 0) {
                sb.append("?");
            } else {
                sb.append(",?");
            }
            builder.getFieldValueStatement().put(field, value);
        }

        return String.format(getOp().getValue(), conditionAlias, sb);
    }

    @Override
    public Object parse(AbstractService service) throws AvalonException {

        Field field = getField(service);

        List<Object> values = (List<Object>) getValue();

        for (int i = 0; i < values.size(); i++) {
            if (ObjectUtils.isNotNull(values.get(i))) {
                values.set(i, field.parse(values.get(i)));
            }
        }
        return values;
    }

    @Override
    public String getReversePolishNotation() {
        return String.format("(%s,%s,%s)", getOp().getName(), getRealName(),
                String.join(",", getValues().stream().map(Object::toString).toArray(String[]::new)));
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        List<Object> inValues = new ArrayList<>(Arrays.asList(values).subList(2, values.length));
        return Condition.inCondition(values[1], inValues);
    }

    @Override
    public String toString() {
        return String.format(getOp().getValue(), getRealName(), ObjectUtils.toString(getValues()));
    }
}
