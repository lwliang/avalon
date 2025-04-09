package com.avalon.core.antlr4.condition;// Generated from Expression.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ExpressionParser}.
 */
public interface ExpressionListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(ExpressionParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(ExpressionParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpression(ExpressionParser.AndExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AndExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpression(ExpressionParser.AndExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingleComparison}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void enterSingleComparison(ExpressionParser.SingleComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingleComparison}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void exitSingleComparison(ExpressionParser.SingleComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(ExpressionParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(ExpressionParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GroupedExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void enterGroupedExpression(ExpressionParser.GroupedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GroupedExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void exitGroupedExpression(ExpressionParser.GroupedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpression(ExpressionParser.OrExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code OrExpression}
	 * labeled alternative in {@link ExpressionParser#logicalExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpression(ExpressionParser.OrExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Comparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterComparison(ExpressionParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Comparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitComparison(ExpressionParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BetweenComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterBetweenComparison(ExpressionParser.BetweenComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BetweenComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitBetweenComparison(ExpressionParser.BetweenComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterInComparison(ExpressionParser.InComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitInComparison(ExpressionParser.InComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LikeComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterLikeComparison(ExpressionParser.LikeComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LikeComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitLikeComparison(ExpressionParser.LikeComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotLikeComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotLikeComparison(ExpressionParser.NotLikeComparisonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotLikeComparison}
	 * labeled alternative in {@link ExpressionParser#comparisonExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotLikeComparison(ExpressionParser.NotLikeComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ListOfValues}
	 * labeled alternative in {@link ExpressionParser#valueList}.
	 * @param ctx the parse tree
	 */
	void enterListOfValues(ExpressionParser.ListOfValuesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ListOfValues}
	 * labeled alternative in {@link ExpressionParser#valueList}.
	 * @param ctx the parse tree
	 */
	void exitListOfValues(ExpressionParser.ListOfValuesContext ctx);
	/**
	 * Enter a parse tree produced by {@link ExpressionParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(ExpressionParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ExpressionParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(ExpressionParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdentifierValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierValue(ExpressionParser.IdentifierValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdentifierValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierValue(ExpressionParser.IdentifierValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntegerValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void enterIntegerValue(ExpressionParser.IntegerValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntegerValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void exitIntegerValue(ExpressionParser.IntegerValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FloatValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void enterFloatValue(ExpressionParser.FloatValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FloatValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void exitFloatValue(ExpressionParser.FloatValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void enterStringValue(ExpressionParser.StringValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void exitStringValue(ExpressionParser.StringValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code BooleanValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void enterBooleanValue(ExpressionParser.BooleanValueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code BooleanValue}
	 * labeled alternative in {@link ExpressionParser#value}.
	 * @param ctx the parse tree
	 */
	void exitBooleanValue(ExpressionParser.BooleanValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DotSeparatedIdentifier}
	 * labeled alternative in {@link ExpressionParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterDotSeparatedIdentifier(ExpressionParser.DotSeparatedIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DotSeparatedIdentifier}
	 * labeled alternative in {@link ExpressionParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitDotSeparatedIdentifier(ExpressionParser.DotSeparatedIdentifierContext ctx);
}