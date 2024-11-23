/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.antlr4.grammar;// Generated from AvalonExpr.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AvalonExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AvalonExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(AvalonExprParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PrintStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrintStat(AvalonExprParser.PrintStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AssignStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignStat(AvalonExprParser.AssignStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStat(AvalonExprParser.IfStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ForStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStat(AvalonExprParser.ForStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStat(AvalonExprParser.WhileStatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NewlineStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNewlineStat(AvalonExprParser.NewlineStatContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#ifStatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatExpr(AvalonExprParser.IfStatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#elseIfStatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseIfStatExpr(AvalonExprParser.ElseIfStatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#elseStatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElseStatExpr(AvalonExprParser.ElseStatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#ifBodyStatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfBodyStatExpr(AvalonExprParser.IfBodyStatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#forStatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForStatExpr(AvalonExprParser.ForStatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#forInitExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInitExpr(AvalonExprParser.ForInitExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#forBodyExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForBodyExpr(AvalonExprParser.ForBodyExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#whileStatExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatExpr(AvalonExprParser.WhileStatExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#whileBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileBody(AvalonExprParser.WhileBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#boolExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpr(AvalonExprParser.BoolExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntExpression(AvalonExprParser.IntExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(AvalonExprParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubSubRightExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubSubRightExpression(AvalonExprParser.SubSubRightExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NumberExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumberExpression(AvalonExprParser.NumberExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjVisitMethodExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjVisitMethodExpression(AvalonExprParser.ObjVisitMethodExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddAddLeftExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddAddLeftExpression(AvalonExprParser.AddAddLeftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSubExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSubExpression(AvalonExprParser.AddSubExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueExpression(AvalonExprParser.TrueExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParentExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentExpression(AvalonExprParser.ParentExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SubSubLeftExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubSubLeftExpression(AvalonExprParser.SubSubLeftExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ComparatorExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparatorExpression(AvalonExprParser.ComparatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConditionExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConditionExpression(AvalonExprParser.ConditionExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpression(AvalonExprParser.StringExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpression(AvalonExprParser.IdExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddAddRightExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddAddRightExpression(AvalonExprParser.AddAddRightExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CallFuncExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCallFuncExpression(AvalonExprParser.CallFuncExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryOpExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryOpExpression(AvalonExprParser.UnaryOpExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjVisitFieldExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjVisitFieldExpression(AvalonExprParser.ObjVisitFieldExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseExpression(AvalonExprParser.FalseExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDivExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDivExpression(AvalonExprParser.MulDivExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#exprList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprList(AvalonExprParser.ExprListContext ctx);
	/**
	 * Visit a parse tree produced by {@link AvalonExprParser#methodExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodExpr(AvalonExprParser.MethodExprContext ctx);
}