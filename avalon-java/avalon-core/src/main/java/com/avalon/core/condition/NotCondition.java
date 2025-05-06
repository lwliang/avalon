package com.avalon.core.condition;

import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;

import java.util.List;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/02/13 9:38
 */
public class NotCondition extends Condition {
    private Condition condition;

    public NotCondition(Condition condition) {
        super();
        this.condition = condition;
        setOp(ConditionOperateEnum.Not);
    }

    @Override
    public String getSql(AbstractService service, FieldValueStatement valueStatement) {
        String sql = condition.getSql(service, valueStatement);
        return String.format(getOp().getValue(), sql);
    }

    @Override
    public Boolean hasJoinSelect() {
        return condition.hasJoinSelect();
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String sql = condition.getSql(builder);
        return String.format(getOp().getValue(), sql);
    }

    @Override
    public String toString() {
        return String.format(getOp().getValue(), condition.toString());
    }

    @Override
    public String getConditionString() {
        return String.format(getOp().getCondition(), condition.getConditionString());
    }

    @Override
    protected void addFlatCondition(List<Condition> conditions) {
        condition.addFlatCondition(conditions);
    }
}
