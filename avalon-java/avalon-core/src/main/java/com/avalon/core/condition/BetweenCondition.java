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

import java.util.List;

@Data
public class BetweenCondition extends Condition {

    private Object begin;
    private Object end;

    public BetweenCondition(String name, List<Object> value) {
        setName(name);
        setOp(ConditionOperateEnum.Between);
        setValue(value);
        begin = value.get(0);
        end = value.get(1);
    }

    public BetweenCondition(Field fieldName, List<Object> value) {
        setFieldName(fieldName);
        setOp(ConditionOperateEnum.Between);
        setValue(value);
        begin = value.get(0);
        end = value.get(1);
    }

    /**
     * value参数
     *
     * @param name
     * @param value
     */
    public BetweenCondition(String name, Object... value) {
        setName(name);
        setOp(ConditionOperateEnum.Between);
        setValue(List.of(value));
        begin = value[0];
        end = value[1];
    }

    public BetweenCondition(Field fieldName, Object... value) {
        setFieldName(fieldName);
        setOp(ConditionOperateEnum.Between);
        setValue(List.of(value));
        begin = value[0];
        end = value[1];
    }

    public BetweenCondition(String name, Object begin, Object end) {
        setName(name);
        setOp(ConditionOperateEnum.Between);
        setBegin(begin);
        setEnd(end);
    }

    public BetweenCondition(Field fieldName, Object begin, Object end) {
        setFieldName(fieldName);
        setOp(ConditionOperateEnum.Between);
        setBegin(begin);
        setEnd(end);
    }

    @Override
    public String getSql(AbstractService service, FieldValueStatement valueStatement) {
        Field field = service.getField(getRealName());

        if (getValue() instanceof List) {
            List<Object> values = (List<Object>) getValue();
            for (Object value : values) {
                valueStatement.put(field, value);
            }
            return String.format(getOp().getValue(), Fields.underscoreName(getRealName()), "?", "?");
        }
        valueStatement.put(field, begin);
        valueStatement.put(field, end);
        return String.format(getOp().getValue(), Fields.underscoreName(getRealName()), getValue().toString());
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String conditionAlias = builder.getQueryRoot().getFieldWithAliasTable(getRealName(),
                builder.getAliasSupport());
        Field field = builder.getService().getField(getRealName());
        Object beginValue = begin;
        if (beginValue instanceof String) {
            beginValue = builder.getService().getContext().executeScript((String) beginValue);
        }
        Object endValue = end;
        if (endValue instanceof String) {
            endValue = builder.getService().getContext().executeScript((String) endValue);
        }
        builder.getFieldValueStatement().put(field, beginValue);
        builder.getFieldValueStatement().put(field, endValue);

        return String.format(getOp().getValue(), conditionAlias, "?", "?");
    }

    @Override
    public Object parse(AbstractService service) throws AvalonException {

        Field field = service.getField(getRealName());

        if (ObjectUtils.isNotNull(begin)) {
            begin = field.parse(begin);
        }

        if (ObjectUtils.isNotNull(end)) {
            end = field.parse(end);
        }

        List<Object> values = (List) getValue();

        for (int i = 0; i < values.size(); i++) {
            if (ObjectUtils.isNotNull(values.get(i))) {
                values.set(i, field.parse(values.get(i)));
            }
        }


        return getValue();
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.betweenCondition(values[1], values[2], values[3]);
    }

    @Override
    public String getReversePolishNotation() {
        return String.format("(%s,%s,%s,%s)", getOp().getName(), getRealName(), begin, end);
    }

    @Override
    public String toString() {
        return String.format(getOp().getValue(), getRealName(), begin, end);
    }
}
