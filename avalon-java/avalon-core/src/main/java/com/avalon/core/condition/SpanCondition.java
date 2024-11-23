/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.JoinRelationMap;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.FieldUtils;
import lombok.Data;

/**
 * 区间条件
 * field - value < span
 */
@Data
public class SpanCondition extends Condition {
    private Object span;
    private SpanOpEnum spanOp;
    private SpanCompareEnum spanCompare;

    public SpanCondition(String name, SpanOpEnum spanOp, SpanCompareEnum spanCompare, Object value, Object span) {
        super();
        setOp(ConditionOperateEnum.Span);
        setName(name);
        setValue(value);
        setSpan(span);
        setSpanOp(spanOp);
        setSpanCompare(spanCompare);
    }

    public SpanCondition(Field fieldName, SpanOpEnum spanOp, SpanCompareEnum spanCompare, Object value, Object span) {
        super();
        setOp(ConditionOperateEnum.Span);
        setFieldName(fieldName);
        setValue(value);
        setSpan(span);
        setSpanOp(spanOp);
        setSpanCompare(spanCompare);
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String conditionAlias = builder.getQueryRoot().getFieldWithAliasTable(getRealName(),
                builder.getAliasSupport());
        Field field = builder.getService().getField(getRealName());
        builder.getFieldValueStatement().put(field, getValue());
        return String.format(getOp().getValue(),
                conditionAlias,
                getSpanOp().getValue(),
                getSpan(),
                getSpanCompare().getValue(),
                "?");
    }

    @Override
    public String getReversePolishNotation() {
        // 默认相加，是大于等于匹配
        return String.format("(%s,%s,%s,%s)", getOp().getName(), getRealName(), getValue(), getSpan());
    }

    @Override
    protected Condition doParseReversePolishNotation(String[] values) {
        return Condition.subGreaterThanSpanCondition(values[1], values[2], values[3]);
    }

    @Override
    public String toString() {
        return super.toString();
    }


    public static enum SpanCompareEnum {
        Greater(" > "),
        Equal(" = "),
        GreaterEqual(" >= "),
        Less(" < "),
        LessEqual(" <= ");

        private final String value;

        SpanCompareEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum SpanOpEnum {
        Add(" + "),
        Sub(" - "),
        Mul(" * "),
        Div(" / ");

        private final String value;

        SpanOpEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
