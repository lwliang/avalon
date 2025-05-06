/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.condition;

import com.avalon.core.model.FieldValueStatement;
import com.avalon.core.model.JoinRelationMap;
import com.avalon.core.select.builder.SelectBuilder;
import com.avalon.core.service.AbstractService;
import com.avalon.core.util.ObjectUtils;
import lombok.Getter;

import java.util.List;

public class AndCondition extends Condition {
    @Getter
    private final Condition leftCondition;
    @Getter
    private final Condition rightCondition;


    public AndCondition(Condition leftCondition, Condition rightCondition) {
        super();
        this.leftCondition = leftCondition;
        this.rightCondition = rightCondition;
        setOp(ConditionOperateEnum.And);
    }

    @Override
    public String getSql(AbstractService service, FieldValueStatement valueStatement) {
        String leftSql = leftCondition.getSql(service, valueStatement);
        String rightSql = rightCondition.getSql(service, valueStatement);
        return String.format(getOp().getValue(),
                leftSql,
                rightSql);
    }

    @Override
    public Boolean hasJoinSelect() {
        return leftCondition.hasJoinSelect() || rightCondition.hasJoinSelect();
    }

    @Override
    public String getSql(SelectBuilder builder) {
        String leftSql = leftCondition.getSql(builder);
        String rightSql = rightCondition.getSql(builder);
        return String.format(getOp().getValue(),
                leftSql,
                rightSql);
    }

    @Override
    public String toString() {
        return String.format(getOp().getValue(),
                leftCondition.toString(),
                rightCondition.toString());
    }

    @Override
    public String getConditionString() {
        return String.format(getOp().getCondition(),
                leftCondition.getConditionString(),
                rightCondition.getConditionString());
    }

    @Override
    protected void addFlatCondition(List<Condition> conditions) {
        leftCondition.addFlatCondition(conditions);
        rightCondition.addFlatCondition(conditions);
    }
}
