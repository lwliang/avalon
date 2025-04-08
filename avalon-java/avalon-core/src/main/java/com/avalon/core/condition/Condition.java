/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.exception.AvalonException;
import com.avalon.core.field.DateField;
import com.avalon.core.field.Field;
import com.avalon.core.field.Fields;
import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.JoinRelationMap;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;
import com.avalon.core.tree.QueryNode;
import com.avalon.core.util.FieldUtils;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Data
public abstract class Condition implements ICondition {

    private String name;
    private Field fieldName;

    private ConditionOperateEnum op;

    private Object value;

    public Boolean isEmpty() {
        return ObjectUtils.isEmpty(value);
    }

    @Override
    public Boolean hasJoinSelect() {
        return FieldUtils.hasJoinSelect(name);
    }

    /**
     * 获取真实的字段名
     *
     * @return
     */
    public String getRealName() {
        if (ObjectUtils.isNotNull(name)) {
            return name;
        }
        return fieldName.getName();
    }

    public Field getField(AbstractService service) {
        if (ObjectUtils.isNotNull(name)) {
            return service.getField(name);
        }
        return fieldName;
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String conditionAlias = builder.getQueryRoot().getFieldWithAliasTable(getRealName(),
                builder.getAliasSupport());
        Field field = builder.getService().getField(getRealName());
        Object value = getValue();
        if (getValue() instanceof String) {
            if (FieldUtils.isDateOrTime(field)) {
                if (!value.toString().contains("'")) {
                    value = "'" + value + "'";
                }
            }
            value = builder.getService().getContext().executeScript((String) value);
        }
        builder.getFieldValueStatement().put(field, value);
        return String.format(op.getValue(), conditionAlias, "?");
    }

    @Override
    public Object parse(AbstractService service) throws AvalonException {
        Field field = getField(service);

        if (ObjectUtils.isNotNull(value)) {
            value = field.parse(value);
        }
        return value;
    }


    @Override
    public String getSql(AbstractService service, FieldValueStatement valueStatement) {
        Field field = getField(service);
        valueStatement.put(field, getValue());
        return String.format(op.getValue(), Fields.underscoreName(getRealName()), "?");
    }

    @Override
    public String toString() {
        return String.format(op.getValue(), getRealName(), value);
    }

    @Override
    public String getConditionString() {
        return String.format(op.getCondition(), getRealName(), value);
    }

    public Condition andCondition(Condition condition) {
        return new AndCondition(this, condition);
    }

    public Condition andCondition(String name, Object value) {
        return new AndCondition(this, Condition.equalCondition(name, value));
    }

    public Condition andCondition(Field field, Object value) {
        return new AndCondition(this, Condition.equalCondition(field, value));
    }

    public Condition orCondition(String name, Object value) {
        return new OrCondition(this, Condition.equalCondition(name, value));
    }

    public Condition orCondition(Field field, Object value) {
        return new OrCondition(this, Condition.equalCondition(field, value));
    }

    public Condition andNotEqualCondition(String name, Object value) {
        return new AndCondition(this, Condition.notEqualCondition(name, value));
    }

    public Condition andNotEqualCondition(Field field, Object value) {
        return new AndCondition(this, Condition.notEqualCondition(field, value));
    }

    public Condition orNotEqualCondition(String name, Object value) {
        return new OrCondition(this, Condition.notEqualCondition(name, value));
    }

    public Condition orNotEqualCondition(Field field, Object value) {
        return new OrCondition(this, Condition.notEqualCondition(field, value));
    }

    public Condition andInCondition(String name, List<?> value) {
        return new AndCondition(this, Condition.inCondition(name, (List<?>) value));
    }

    public Condition andInCondition(Field field, List<?> value) {
        return new AndCondition(this, Condition.inCondition(field, (List<?>) value));
    }

    public Condition andInCondition(String name, Object... value) {
        return new AndCondition(this, Condition.inCondition(name, value));
    }

    public Condition andInCondition(Field field, Object... value) {
        return new AndCondition(this, Condition.inCondition(field, value));
    }

    public Condition andNotInCondition(String name, List<?> value) {
        return new AndCondition(this, Condition.notInCondition(name, value));
    }

    public Condition andNotInCondition(Field field, List<?> value) {
        return new AndCondition(this, Condition.notInCondition(field, value));
    }

    public Condition andNotInCondition(String name, Object... value) {
        return new AndCondition(this, Condition.notInCondition(name, value));
    }

    public Condition andNotInCondition(Field field, Object... value) {
        return new AndCondition(this, Condition.notInCondition(field, value));
    }

    public Condition orInCondition(String name, List<Object> value) {
        return new OrCondition(this, Condition.inCondition(name, value));
    }

    public Condition orInCondition(Field field, List<Object> value) {
        return new OrCondition(this, Condition.inCondition(field, value));
    }

    public Condition orInCondition(String name, Object... value) {
        return new OrCondition(this, Condition.inCondition(name, value));
    }

    public Condition orInCondition(Field field, Object... value) {
        return new OrCondition(this, Condition.inCondition(field, value));
    }

    public Condition orNotInCondition(String name, List<Object> value) {
        return new OrCondition(this, Condition.notInCondition(name, value));
    }

    public Condition orNotInCondition(Field field, List<Object> value) {
        return new OrCondition(this, Condition.notInCondition(field, value));
    }

    public Condition orNotInCondition(String name, Object... value) {
        return new OrCondition(this, Condition.notInCondition(name, value));
    }

    public Condition orNotInCondition(Field field, Object... value) {
        return new OrCondition(this, Condition.notInCondition(field, value));
    }

    public Condition andLikeCondition(String name, Object value) {
        return new AndCondition(this, Condition.likeCondition(name, value));
    }

    public Condition andLikeCondition(Field field, Object value) {
        return new AndCondition(this, Condition.likeCondition(field, value));
    }

    public Condition andNotLikeCondition(String name, Object value) {
        return new AndCondition(this, Condition.notLikeCondition(name, value));
    }

    public Condition andNotLikeCondition(Field field, Object value) {
        return new AndCondition(this, Condition.notLikeCondition(field, value));
    }


    public Condition orLikeCondition(String name, Object value) {
        return new OrCondition(this, Condition.likeCondition(name, value));
    }

    public Condition orLikeCondition(Field field, Object value) {
        return new OrCondition(this, Condition.likeCondition(field, value));
    }

    public Condition andGreaterCondition(String name, Object value) {
        return new AndCondition(this, Condition.greaterCondition(name, value));
    }

    public Condition andGreaterCondition(Field field, Object value) {
        return new AndCondition(this, Condition.greaterCondition(field, value));
    }

    public Condition orGreaterCondition(String name, Object value) {
        return new OrCondition(this, Condition.greaterCondition(name, value));
    }

    public Condition orGreaterCondition(Field field, Object value) {
        return new OrCondition(this, Condition.greaterCondition(field, value));
    }

    public Condition andGreaterEqualCondition(String name, Object value) {
        return new AndCondition(this, Condition.greaterEqualCondition(name, value));
    }

    public Condition andGreaterEqualCondition(Field field, Object value) {
        return new AndCondition(this, Condition.greaterEqualCondition(field, value));
    }

    public Condition orGreaterEqualCondition(String name, Object value) {
        return new OrCondition(this, Condition.greaterEqualCondition(name, value));
    }

    public Condition orGreaterEqualCondition(Field field, Object value) {
        return new OrCondition(this, Condition.greaterEqualCondition(field, value));
    }

    public Condition andLessCondition(String name, Object value) {
        return new AndCondition(this, Condition.lessCondition(name, value));
    }

    public Condition andLessCondition(Field field, Object value) {
        return new AndCondition(this, Condition.lessCondition(field, value));
    }

    public Condition orLessCondition(String name, Object value) {
        return new OrCondition(this, Condition.lessCondition(name, value));
    }

    public Condition orLessCondition(Field field, Object value) {
        return new OrCondition(this, Condition.lessCondition(field, value));
    }

    public Condition andLessEqualCondition(String name, Object value) {
        return new AndCondition(this, Condition.lessEqualCondition(name, value));
    }

    public Condition andLessEqualCondition(Field field, Object value) {
        return new AndCondition(this, Condition.lessEqualCondition(field, value));
    }

    public Condition orLessEqualCondition(String name, Object value) {
        return new OrCondition(this, Condition.lessEqualCondition(name, value));
    }

    public Condition orLessEqualCondition(Field field, Object value) {
        return new OrCondition(this, Condition.lessEqualCondition(field, value));
    }

    public Condition andEqualCondition(String name, Object value) {
        return new AndCondition(this, Condition.equalCondition(name, value));
    }

    public Condition andEqualCondition(Field field, Object value) {
        return new AndCondition(this, Condition.equalCondition(field, value));
    }

    public Condition orEqualCondition(String name, Object value) {
        return new OrCondition(this, Condition.equalCondition(name, value));
    }

    public Condition orEqualCondition(Field field, Object value) {
        return new OrCondition(this, Condition.equalCondition(field, value));
    }

    public Condition orCondition(Condition condition) {
        return new OrCondition(this, condition);
    }

    public static Condition equalCondition(String name, Object value) {
        return new EqualCondition(name, value);
    }

    public static Condition equalCondition(Field field, Object value) {
        return new EqualCondition(field, value);
    }

    public static Condition notEqualCondition(String name, Object value) {
        return new NotEqualCondition(name, value);
    }

    public static Condition notEqualCondition(Field field, Object value) {
        return new NotEqualCondition(field, value);
    }

    public static Condition lessCondition(String name, Object value) {
        return new LessCondition(name, value);
    }

    public static Condition lessCondition(Field field, Object value) {
        return new LessCondition(field, value);
    }


    public static Condition lessEqualCondition(String name, Object value) {
        return new LessEqualCondition(name, value);
    }

    public static Condition lessEqualCondition(Field field, Object value) {
        return new LessEqualCondition(field, value);
    }

    public static Condition likeCondition(String name, Object value) {
        return new LikeCondition(name, value);
    }

    public static Condition likeCondition(Field field, Object value) {
        return new LikeCondition(field, value);
    }

    public static Condition notLikeCondition(String name, Object value) {
        return new NotLikeCondition(name, value);
    }

    public static Condition notLikeCondition(Field field, Object value) {
        return new NotLikeCondition(field, value);
    }

    public static Condition greaterCondition(String name, Object value) {
        return new GreaterCondition(name, value);
    }

    public static Condition greaterCondition(Field field, Object value) {
        return new GreaterCondition(field, value);
    }

    public static Condition greaterEqualCondition(String name, Object value) {
        return new GreaterEqualCondition(name, value);
    }

    public static Condition greaterEqualCondition(Field field, Object value) {
        return new GreaterEqualCondition(field, value);
    }

    public static Condition betweenCondition(String name, Object begin, Object end) {
        return new BetweenCondition(name, begin, end);
    }

    public static Condition inCondition(String name, List<?> values) {
        return new InCondition(name, values);
    }

    public static Condition inCondition(Field field, List<?> values) {
        return new InCondition(field, values);
    }

    public static Condition notInCondition(String name, List<?> values) {
        return new NotInCondition(name, values);
    }

    public static Condition notInCondition(Field field, List<?> values) {
        return new NotInCondition(field, values);
    }


    public static Condition inCondition(String name, Object... values) {
        return new InCondition(name, values);
    }

    public static Condition inCondition(Field field, Object... values) {
        return new InCondition(field, values);
    }

    public static Condition notInCondition(String name, Object... values) {
        return new NotInCondition(name, values);
    }

    public static Condition notInCondition(Field field, Object... values) {
        return new NotInCondition(field, values);
    }

    public static Condition subGreaterThanSpanCondition(String name, Object value, Object span) {
        return new SpanCondition(name, SpanCondition.SpanOpEnum.Add,
                SpanCondition.SpanCompareEnum.GreaterEqual, value, span);
    }

    public static Condition subGreaterThanSpanCondition(Field field, Object value, Object span) {
        return new SpanCondition(field, SpanCondition.SpanOpEnum.Add,
                SpanCondition.SpanCompareEnum.GreaterEqual, value, span);
    }

    /**
     * 需要重载
     *
     * @param conditions
     */
    protected void addFlatCondition(List<Condition> conditions) {
        conditions.add(this);
    }

    /**
     * 将条件转换为一维列表
     */
    public List<Condition> getFlatConditions() {
        List<Condition> conditions = new ArrayList<>();
        addFlatCondition(conditions);
        return conditions;
    }

    /**
     * 左右条件按照and的顺序合并 如果left为null 则返回right
     *
     * @param left
     * @param right
     * @return
     */
    public static Condition andCondition(Condition left, Condition right) {
        if (ObjectUtils.isNotNull(left)) {
            return left.andCondition(right);
        }
        return right;
    }


    /**
     * 左右条件按照or的顺序合并 如果left为null 则返回right
     *
     * @param left
     * @param right
     * @return
     */
    public static Condition orCondition(Condition left, Condition right) {
        if (ObjectUtils.isNotNull(left)) {
            return left.orCondition(right);
        }
        return right;
    }

    public static Condition notCondition(Condition condition) {
        if (ObjectUtils.isNotNull(condition)) {
            return new NotCondition(condition);
        }
        return condition;
    }

}
