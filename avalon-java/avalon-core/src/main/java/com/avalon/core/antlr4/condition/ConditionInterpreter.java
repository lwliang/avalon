package com.avalon.core.antlr4.condition;

import com.avalon.core.condition.Condition;
import com.avalon.core.context.Context;
import com.avalon.core.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/02/12 20:17
 */
public class ConditionInterpreter extends ExpressionBaseVisitor<Condition> {
    @Getter
    @Setter
    public Context context;

    @Override
    public Condition visitExpr(ExpressionParser.ExprContext ctx) {
        return super.visitExpr(ctx);
    }

    @Override
    public Condition visitGroupedExpression(ExpressionParser.GroupedExpressionContext ctx) {
        return visit(ctx.logicalExpr());
    }

    @Override
    public Condition visitNotExpression(ExpressionParser.NotExpressionContext ctx) {
        Condition condition = visit(ctx.logicalExpr());
        return Condition.notCondition(condition);
    }

    @Override
    public Condition visitAndExpression(ExpressionParser.AndExpressionContext ctx) {
        Condition leftCondition = visit(ctx.left);
        Condition rightCondition = visit(ctx.right);
        return Condition.andCondition(leftCondition, rightCondition);
    }

    @Override
    public Condition visitOrExpression(ExpressionParser.OrExpressionContext ctx) {
        Condition leftCondition = visit(ctx.left);
        Condition rightCondition = visit(ctx.right);
        return Condition.orCondition(leftCondition, rightCondition);
    }

    @Override
    public Condition visitBetweenComparison(ExpressionParser.BetweenComparisonContext ctx) {
        ExpressionParser.IdentifierContext identifier = ctx.identifier();
        TerminalNode nameStr = ctx.STRING();
        ExpressionParser.ValueContext low = ctx.value(0);
        ExpressionParser.ValueContext high = ctx.value(1);

        if (ObjectUtils.isNull(identifier)) {
            return Condition.betweenCondition(nameStr.getText().replaceAll("'", ""),
                    visitValue(low), visitValue(high));
        }
        return Condition.betweenCondition(identifier.getText(), visitValue(low), visitValue(high));
    }

    @Override
    public Condition visitInComparison(ExpressionParser.InComparisonContext ctx) {
        ExpressionParser.IdentifierContext identifier = ctx.identifier();
        TerminalNode nameStr = ctx.STRING();
        List<Object> values = visitValues((ExpressionParser.ListOfValuesContext) ctx.valueList());
        if (ObjectUtils.isNull(identifier)) {
            return Condition.inCondition(nameStr.getText().replaceAll("'", ""), values);
        }
        return Condition.inCondition(identifier.getText(), values);
    }

    @Override
    public Condition visitNotInComparison(ExpressionParser.NotInComparisonContext ctx) {
        ExpressionParser.IdentifierContext identifier = ctx.identifier();
        TerminalNode nameStr = ctx.STRING();
        List<Object> values = visitValues((ExpressionParser.ListOfValuesContext) ctx.valueList());
        if (ObjectUtils.isNull(identifier)) {
            return Condition.notInCondition(nameStr.getText().replaceAll("'", ""), values);
        }
        return Condition.notInCondition(identifier.getText(), values);
    }

    @Override
    public Condition visitComparison(ExpressionParser.ComparisonContext ctx) {
        ExpressionParser.IdentifierContext identifier = ctx.identifier();
        TerminalNode nameStr = ctx.STRING();
        ExpressionParser.OperatorContext operator = ctx.operator();

        if (ObjectUtils.isNull(identifier)) {
            return visitOperator(operator, nameStr.getText().replaceAll("'", ""), visitValue(ctx.value()));
        }

        return visitOperator(operator, identifier.getText(), visitValue(ctx.value()));
    }

    @Override
    public Condition visitLikeComparison(ExpressionParser.LikeComparisonContext ctx) {
        ExpressionParser.IdentifierContext identifier = ctx.identifier();
        TerminalNode nameStr = ctx.STRING();

        ExpressionParser.ValueContext like = ctx.value();

        if (ObjectUtils.isNull(identifier)) {
            return Condition.likeCondition(nameStr.getText().replaceAll("'", ""), "'" + visitValue(like) + "'");
        }
        return Condition.likeCondition(identifier.getText(), "'" + visitValue(like) + "'");
    }

    @Override
    public Condition visitNotLikeComparison(ExpressionParser.NotLikeComparisonContext ctx) {
        ExpressionParser.IdentifierContext identifier = ctx.identifier();
        TerminalNode nameStr = ctx.STRING();

        ExpressionParser.ValueContext like = ctx.value();

        if (ObjectUtils.isNull(identifier)) {
            return Condition.notLikeCondition(nameStr.getText().replaceAll("'", ""), "'" + visitValue(like) + "'");
        }
        return Condition.notLikeCondition(identifier.getText(), "'" + visitValue(like) + "'");
    }

    @Override
    public Condition visitIdentifierValue(ExpressionParser.IdentifierValueContext ctx) {
        return super.visitIdentifierValue(ctx);
    }

    public Condition visitOperator(ExpressionParser.OperatorContext ctx, String name, Object value) {
        String op = ctx.getText();

        return switch (op) {
            case "=" -> Condition.equalCondition(name, value);
            case "!=" -> Condition.notEqualCondition(name, value);
            case ">=" -> Condition.greaterEqualCondition(name, value);
            case ">" -> Condition.greaterCondition(name, value);
            case "<" -> Condition.lessCondition(name, value);
            case "<=" -> Condition.lessEqualCondition(name, value);
            default -> null;
        };
    }

    public List<Object> visitValues(ExpressionParser.ListOfValuesContext ctx) {
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < ctx.value().size(); i++) {
            values.add(visitValue(ctx.value().get(i)));
        }
        return values;
    }

    public Object doVisitIdentifierValue(ExpressionParser.IdentifierValueContext ctx) {
        if (ObjectUtils.isNull(context)) {
            return ctx.getText().replaceAll("'", "").replaceAll("\"", "");
        }
        String token = ctx.getText().trim().toLowerCase();
        if (token.equals("userid")) {
            return context.getUserId();
        }
        return null;
    }

    public Object visitValue(ExpressionParser.ValueContext ctx) {
        if (ctx instanceof ExpressionParser.FloatValueContext) {
            return Float.valueOf(ctx.getText());
        }
        if (ctx instanceof ExpressionParser.IntegerValueContext) {
            return Integer.valueOf(ctx.getText());
        }
        if (ctx instanceof ExpressionParser.StringValueContext) {
            return ctx.getText().replaceAll("'", "").replaceAll("\"", "");
        }
        if (ctx instanceof ExpressionParser.BooleanValueContext) {
            return Boolean.valueOf(ctx.getText());
        }

        if (ctx instanceof ExpressionParser.NullValueContext) {
            return null;
        }
        if (ctx instanceof ExpressionParser.IdentifierValueContext) {
            return doVisitIdentifierValue((ExpressionParser.IdentifierValueContext) ctx);
        }

        return ctx.getText();
    }
}
