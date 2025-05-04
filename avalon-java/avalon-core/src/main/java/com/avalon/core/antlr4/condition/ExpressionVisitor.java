package com.avalon.core.antlr4.condition;// Generated from Expression.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExpressionParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExpressionVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(ExpressionParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpression(ExpressionParser.AndExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SingleComparison}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleComparison(ExpressionParser.SingleComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(ExpressionParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GroupedExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGroupedExpression(ExpressionParser.GroupedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpression(ExpressionParser.OrExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Comparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitComparison(ExpressionParser.ComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BetweenComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBetweenComparison(ExpressionParser.BetweenComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInComparison(ExpressionParser.InComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LikeComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLikeComparison(ExpressionParser.LikeComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotLikeComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotLikeComparison(ExpressionParser.NotLikeComparisonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ListOfValues}
	 * labeled alternative in {@link ExpressionParser#valueList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitListOfValues(ExpressionParser.ListOfValuesContext ctx);
	/**
	 * Visit a parse tree produced by {@link ExpressionParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(ExpressionParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NullValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNullValue(ExpressionParser.NullValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IdentifierValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierValue(ExpressionParser.IdentifierValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IntegerValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegerValue(ExpressionParser.IntegerValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FloatValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatValue(ExpressionParser.FloatValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code StringValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringValue(ExpressionParser.StringValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BooleanValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBooleanValue(ExpressionParser.BooleanValueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotSeparatedIdentifier}
	 * labeled alternative in {@link ExpressionParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotSeparatedIdentifier(ExpressionParser.DotSeparatedIdentifierContext ctx);
}