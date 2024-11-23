/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.antlr4.interpreter;

import com.avalon.core.antlr4.grammar.AvalonExprBaseVisitor;
import com.avalon.core.antlr4.grammar.AvalonExprParser;
import com.avalon.core.exception.AvalonException;
import com.avalon.core.util.FieldValue;
import com.avalon.core.util.ObjectUtils;
import com.avalon.core.util.StringUtils;
import org.antlr.v4.runtime.Token;
import org.apache.pulsar.shade.org.apache.commons.lang.NotImplementedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AvalonExprInterpreter extends AvalonExprBaseVisitor<Object> {

    private final Map<String, Object> symbolTable = new LinkedHashMap<>(); //服务号表 最后一个是最终的返回值

    private final static Hashtable<String, Object> ObjValueMap = new Hashtable<>();

    /**
     * 添加object值，可以对外访问
     *
     * @param name  访问的名字
     * @param value 对象
     */
    public void addObjectValueMap(String name, Object value) {
        ObjValueMap.put(name, value);
    }

    /**
     * 返回值
     *
     * @param name key
     * @return
     */
    public Object getObjectValue(String name) {
        return ObjValueMap.get(name);
    }

    /**
     * 增加/更新 符号的值
     *
     * @param symbol 符号
     * @param value  值
     */
    public void addSymbol(String symbol, Object value) {
        symbolTable.put(symbol, value);
        result = value;
    }


    /**
     * 得到符号表的值
     *
     * @param symbol 符号
     * @return 值
     */
    public Optional<Object> getSymbolValue(String symbol) {
        if (symbolTable.containsKey(symbol)) {
            return Optional.ofNullable(symbolTable.get(symbol));
        }
        return Optional.empty();
    }

    private FieldValue getSymbolValueEx(String symbol) {
        return new FieldValue(symbolTable.get(symbol));
    }

    private Object result;

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * 结果值
     *
     * @return 结果值
     */
    public Optional<Object> getResult() {
        return Optional.ofNullable(result);
    }

    @Override
    public Object visitNumberExpression(AvalonExprParser.NumberExpressionContext ctx) {
        if (StringUtils.isInt(ctx.getText())) {
            return Integer.parseInt(ctx.getText());
        }
        return new BigDecimal(ctx.getText());
    }

    @Override
    public Object visitStringExpression(AvalonExprParser.StringExpressionContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitTrueExpression(AvalonExprParser.TrueExpressionContext ctx) {
        return true;
    }

    @Override
    public Object visitIntExpression(AvalonExprParser.IntExpressionContext ctx) {
        return Integer.parseInt(ctx.getText());
    }

    @Override
    public Object visitFalseExpression(AvalonExprParser.FalseExpressionContext ctx) {
        return false;
    }

    @Override
    public Object visitIdExpression(AvalonExprParser.IdExpressionContext ctx) {
        Optional<Object> value = getSymbolValue(ctx.getText());
        setResult(value.orElse(0));
        return value.orElse(0);
    }

    @Override
    public Object visitAssignStat(AvalonExprParser.AssignStatContext ctx) {
        String id = ctx.ID().getText();
        Object value = visit(ctx.expr());
        addSymbol(id, value);
        return value;
    }

    @Override
    public Object visitUnaryOpExpression(AvalonExprParser.UnaryOpExpressionContext ctx) {
        Object obj = visit(ctx.expr());
        if (ctx.op.getType() == AvalonExprParser.SUB) {
            return new FieldValue(obj).multiplyBigDecimal(BigDecimal.valueOf(-1)).getBigDecimal();
        }

        return new FieldValue(obj).getBigDecimal();
    }

    @Override
    public Object visitMulDivExpression(AvalonExprParser.MulDivExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);
        FieldValue leftValue = new FieldValue(left);
        FieldValue rightValue = new FieldValue(right);
        if (ctx.op.getType() == AvalonExprParser.MUL) {
            return leftValue.multiplyBigDecimal(rightValue.getBigDecimal()).getBigDecimal();
        }

        return leftValue.divideBigDecimal(rightValue.getBigDecimal()).getBigDecimal();
    }

    @Override
    public Object visitAddSubExpression(AvalonExprParser.AddSubExpressionContext ctx) {
        Object left = visit(ctx.left);
        Object right = visit(ctx.right);
        FieldValue leftValue = new FieldValue(left);
        FieldValue rightValue = new FieldValue(right);
        if (ctx.op.getType() == AvalonExprParser.ADD) {
            return leftValue.incrBigDecimal(rightValue.getBigDecimal()).getBigDecimal();
        }

        return leftValue.decrBigDecimal(rightValue.getBigDecimal()).getBigDecimal();
    }

    private Object visitAddAdd(String id) {
        Optional<Object> value = getSymbolValue(id);
        if (value.isPresent()) {
            FieldValue fieldValue = new FieldValue(value.get());
            fieldValue = fieldValue.incrBigDecimal(new BigDecimal(1));
            addSymbol(id, fieldValue.getValue());
            return fieldValue.getValue();
        }

        addSymbol(id, 1);

        return 1;
    }

    @Override
    public Object visitAddAddLeftExpression(AvalonExprParser.AddAddLeftExpressionContext ctx) {
        if (ctx.expr() instanceof AvalonExprParser.IdExpressionContext) {
            return visitAddAdd(ctx.expr().getText());
        }
        String id = visit(ctx.expr()).toString();
        return visitAddAdd(id);
    }

    @Override
    public Object visitAddAddRightExpression(AvalonExprParser.AddAddRightExpressionContext ctx) {
        if (ctx.expr() instanceof AvalonExprParser.IdExpressionContext) {
            return visitAddAdd(ctx.expr().getText());
        }
        String id = visit(ctx.expr()).toString();

        return visitAddAdd(id);
    }

    private Object visitSubSub(String id) {
        Optional<Object> value = getSymbolValue(id);
        if (value.isPresent()) {
            FieldValue fieldValue = new FieldValue(value.get());
            fieldValue = fieldValue.decrBigDecimal(new BigDecimal(1));
            addSymbol(id, fieldValue.getValue());
            return fieldValue.getValue();
        }

        addSymbol(id, 1);
        return 1;
    }

    @Override
    public Object visitSubSubLeftExpression(AvalonExprParser.SubSubLeftExpressionContext ctx) {
        if (ctx.expr() instanceof AvalonExprParser.IdExpressionContext) {
            return visitAddAdd(ctx.expr().getText());
        }
        String id = visit(ctx.expr()).toString();

        return visitSubSub(id);
    }

    @Override
    public Object visitSubSubRightExpression(AvalonExprParser.SubSubRightExpressionContext ctx) {
        if (ctx.expr() instanceof AvalonExprParser.IdExpressionContext) {
            return visitAddAdd(ctx.expr().getText());
        }
        String id = visit(ctx.expr()).toString();

        return visitSubSub(id);
    }


    @Override
    public Object visitComparatorExpression(AvalonExprParser.ComparatorExpressionContext ctx) {
        boolean result = boolExpr(ctx.left, ctx.op, ctx.right);
        setResult(result);
        return result;
    }

    private boolean boolExpr(AvalonExprParser.ExprContext leftCtx, Token opCtx, AvalonExprParser.ExprContext rightCtx) {
        Object left = visit(leftCtx);
        Object right = visit(rightCtx);
        boolean result = false;
        if (left.getClass() == String.class) {
            int compare = left.toString().compareTo(right.toString());
            result = switch (opCtx.getType()) {
                case AvalonExprParser.EQ -> left.toString().equals(right);
                case AvalonExprParser.NEQ -> !left.toString().equals(right);
                case AvalonExprParser.GT -> compare > 0;
                case AvalonExprParser.GE -> compare >= 0;
                case AvalonExprParser.LT -> compare < 0;
                case AvalonExprParser.LE -> compare <= 0;
                default -> false;
            };
        } else {
            BigDecimal leftValue = new BigDecimal(left.toString());
            BigDecimal rightValue = new BigDecimal(right.toString());
            int compare = leftValue.compareTo(rightValue);
            result = switch (opCtx.getType()) {
                case AvalonExprParser.EQ -> compare == 0;
                case AvalonExprParser.NEQ -> !(compare == 0);
                case AvalonExprParser.GT -> compare > 0;
                case AvalonExprParser.GE -> compare >= 0;
                case AvalonExprParser.LT -> compare < 0;
                case AvalonExprParser.LE -> compare <= 0;
                default -> false;
            };
        }
        return result;
    }

    @Override
    public Object visitIfStatExpr(AvalonExprParser.IfStatExprContext ctx) {
        boolean boolExpr = (boolean) visit(ctx.boolExpr());
        if (boolExpr) {
            return visit(ctx.ifBodyStatExpr());
        }
        List<AvalonExprParser.ElseIfStatExprContext> elseIfStatExprContexts = ctx.elseIfStatExpr();
        // 没有else if 直接执行else语句
        if (!ObjectUtils.isNotEmpty(elseIfStatExprContexts)) {
            return visit(ctx.elseStatExpr().ifBodyStatExpr());
        }
        for (AvalonExprParser.ElseIfStatExprContext elseIfStatExprContext : elseIfStatExprContexts) {
            boolExpr = (boolean) visit(elseIfStatExprContext.boolExpr());
            if (boolExpr) {
                return visit(elseIfStatExprContext.ifBodyStatExpr());
            }
        }
        return visit(ctx.elseStatExpr().ifBodyStatExpr());
    }

    @Override
    public Object visitIfBodyStatExpr(AvalonExprParser.IfBodyStatExprContext ctx) {
        Object o = super.visitIfBodyStatExpr(ctx);
        setResult(o);
        return o;
    }

    @Override
    public Object visitBoolExpr(AvalonExprParser.BoolExprContext ctx) {
        return boolExpr(ctx.left, ctx.op, ctx.right);
    }

    @Override
    public Object visitForStatExpr(AvalonExprParser.ForStatExprContext ctx) {
        if (ObjectUtils.isNotEmpty(ctx.forInitExpr())) {
            visit(ctx.forInitExpr());
        }

        do {
            boolean breakFor = cycleCondition(ctx.judge);
            if (!breakFor) {
                break;
            }

            visit(ctx.forBodyExpr());

            visit(ctx.last);
        } while (true);
        return true;
    }

    @Override
    public Object visitForInitExpr(AvalonExprParser.ForInitExprContext ctx) {
        Object o = super.visitForInitExpr(ctx);
        addSymbol(ctx.ID().getText(), o);
        return o;
    }

    /**
     * 解析循环条件值
     *
     * @return 循环条件
     */
    private boolean cycleCondition(AvalonExprParser.ExprContext expr) {
        Object cycleCondition = visit(expr);
        boolean breakFor = false;
        if (cycleCondition.getClass() == Boolean.class) {
            breakFor = (Boolean) cycleCondition;
        } else if (cycleCondition.getClass() == Integer.class) {
            if ((Integer) cycleCondition != 0) {
                breakFor = true;
            }
        } else if (cycleCondition.getClass() == BigDecimal.class) {
            if (((BigDecimal) cycleCondition).compareTo(BigDecimal.ZERO) != 0) {
                breakFor = true;
            }
        } else if (cycleCondition.getClass() == String.class) {
            if (StringUtils.isNotEmpty(cycleCondition)) {
                breakFor = true;
            }
        }
        return breakFor;
    }

    @Override
    public Object visitWhileStatExpr(AvalonExprParser.WhileStatExprContext ctx) {
        do {
            boolean breakFor = cycleCondition(ctx.expr());
            if (!breakFor) {
                break;
            }
            visit(ctx.whileBody());
        } while (true);

        return null;
    }

    @Override
    public Object visitWhileBody(AvalonExprParser.WhileBodyContext ctx) {
        AtomicReference<Object> lastValue = new AtomicReference<>();
        ctx.stat().forEach(statContext -> {
            Object value = visit(statContext);
            setResult(value);
            lastValue.set(value);
        });
        return lastValue.get();
    }

    @Override
    public Object visitWhileStat(AvalonExprParser.WhileStatContext ctx) {
        return super.visitWhileStat(ctx);
    }

    @Override
    public Object visitPrintStat(AvalonExprParser.PrintStatContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Object visitCallFuncExpression(AvalonExprParser.CallFuncExpressionContext ctx) {
        throw new NotImplementedException();
    }

    @Override
    public Object visitObjVisitMethodExpression(AvalonExprParser.ObjVisitMethodExpressionContext ctx) {
        Object objValue = getObjectValue(ctx.obj);

        if (ObjectUtils.isNull(objValue)) {
            throw new AvalonException(ctx.obj.getText() + "不存在对象");
        }
        Object value;
        AvalonExprParser.MethodExprContext methodExprContext = ctx.methodExpr();
        AvalonExprParser.ExprListContext exprArgs = methodExprContext.exprList();
        if (ObjectUtils.isNull(exprArgs)) {
            value = getObjectMethod(objValue, methodExprContext.funName.getText(), null, null);
            setResult(value);
            return value;
        }
        Object[] argsValue = new Object[exprArgs.expr().size()];
        Class<?>[] argsType = new Class[exprArgs.expr().size()];
        int i = 0;
        for (AvalonExprParser.ExprContext exprContext : exprArgs.expr()) {
            Object arg = visit(exprContext);
            argsType[i] = arg.getClass();
            argsValue[i] = arg;
            i++;
        }
        value = getObjectMethod(objValue, methodExprContext.funName.getText(), argsType, argsValue);
        setResult(value);
        return value;
    }


    @Override
    public Object visitObjVisitFieldExpression(AvalonExprParser.ObjVisitFieldExpressionContext ctx) {
        Object objValue = getObjectValue(ctx.obj);
        if (ObjectUtils.isNull(objValue)) {
            throw new AvalonException(ctx.obj.getText() + "不存在对象");
        }
        Object value = getObjectField(objValue, ctx.field.getText());
        setResult(value);
        return value;
    }


    private Object getObjectValue(AvalonExprParser.ExprContext obj) {
        if (obj instanceof AvalonExprParser.IdExpressionContext) {
            return getObjectValue(obj.getText());
        }
        return visit(obj);
    }

    private Object getObjectField(Object value, String fieldName) {
        try {
            Field field = value.getClass().getField(fieldName);
            field.setAccessible(true);
            return field.get(value);
        } catch (NoSuchFieldException e) {
            throw new AvalonException(fieldName + "不存在属性");
        } catch (IllegalAccessException e) {
            throw new AvalonException(fieldName + "无权访问");
        }
    }

    private Object getObjectMethod(Object value, String methodName, Class<?>[] types, Object[] args) {
        try {
            Method method;
            if (ObjectUtils.isNotEmpty(types)) {
                method = value.getClass().getDeclaredMethod(methodName, types);
            } else {
                method = value.getClass().getDeclaredMethod(methodName);
            }

            method.setAccessible(true);
            if (ObjectUtils.isEmpty(args)) {
                return method.invoke(value);
            }
            return method.invoke(value, args);
        } catch (NoSuchMethodException e) {
            throw new AvalonException(methodName + "不存在方法");
        } catch (InvocationTargetException e) {
            throw new AvalonException(methodName + "参数错误");
        } catch (IllegalAccessException e) {
            throw new AvalonException(methodName + "无权访问");
        }
    }
}
