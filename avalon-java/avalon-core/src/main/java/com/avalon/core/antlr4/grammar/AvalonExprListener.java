/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.antlr4.grammar;// Generated from AvalonExpr.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AvalonExprParser}.
 */
public interface AvalonExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(AvalonExprParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(AvalonExprParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PrintStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintStat(AvalonExprParser.PrintStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PrintStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintStat(AvalonExprParser.PrintStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AssignStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssignStat(AvalonExprParser.AssignStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AssignStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssignStat(AvalonExprParser.AssignStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterIfStat(AvalonExprParser.IfStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitIfStat(AvalonExprParser.IfStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ForStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterForStat(AvalonExprParser.ForStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ForStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitForStat(AvalonExprParser.ForStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterWhileStat(AvalonExprParser.WhileStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitWhileStat(AvalonExprParser.WhileStatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NewlineStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterNewlineStat(AvalonExprParser.NewlineStatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NewlineStat}
	 * labeled alternative in {@link AvalonExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitNewlineStat(AvalonExprParser.NewlineStatContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#ifStatExpr}.
	 * @param ctx the parse tree
	 */
	void enterIfStatExpr(AvalonExprParser.IfStatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#ifStatExpr}.
	 * @param ctx the parse tree
	 */
	void exitIfStatExpr(AvalonExprParser.IfStatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#elseIfStatExpr}.
	 * @param ctx the parse tree
	 */
	void enterElseIfStatExpr(AvalonExprParser.ElseIfStatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#elseIfStatExpr}.
	 * @param ctx the parse tree
	 */
	void exitElseIfStatExpr(AvalonExprParser.ElseIfStatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#elseStatExpr}.
	 * @param ctx the parse tree
	 */
	void enterElseStatExpr(AvalonExprParser.ElseStatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#elseStatExpr}.
	 * @param ctx the parse tree
	 */
	void exitElseStatExpr(AvalonExprParser.ElseStatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#ifBodyStatExpr}.
	 * @param ctx the parse tree
	 */
	void enterIfBodyStatExpr(AvalonExprParser.IfBodyStatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#ifBodyStatExpr}.
	 * @param ctx the parse tree
	 */
	void exitIfBodyStatExpr(AvalonExprParser.IfBodyStatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#forStatExpr}.
	 * @param ctx the parse tree
	 */
	void enterForStatExpr(AvalonExprParser.ForStatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#forStatExpr}.
	 * @param ctx the parse tree
	 */
	void exitForStatExpr(AvalonExprParser.ForStatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#forInitExpr}.
	 * @param ctx the parse tree
	 */
	void enterForInitExpr(AvalonExprParser.ForInitExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#forInitExpr}.
	 * @param ctx the parse tree
	 */
	void exitForInitExpr(AvalonExprParser.ForInitExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#forBodyExpr}.
	 * @param ctx the parse tree
	 */
	void enterForBodyExpr(AvalonExprParser.ForBodyExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#forBodyExpr}.
	 * @param ctx the parse tree
	 */
	void exitForBodyExpr(AvalonExprParser.ForBodyExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#whileStatExpr}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatExpr(AvalonExprParser.WhileStatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#whileStatExpr}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatExpr(AvalonExprParser.WhileStatExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#whileBody}.
	 * @param ctx the parse tree
	 */
	void enterWhileBody(AvalonExprParser.WhileBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#whileBody}.
	 * @param ctx the parse tree
	 */
	void exitWhileBody(AvalonExprParser.WhileBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#boolExpr}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpr(AvalonExprParser.BoolExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#boolExpr}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpr(AvalonExprParser.BoolExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IntExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntExpression(AvalonExprParser.IntExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IntExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntExpression(AvalonExprParser.IntExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(AvalonExprParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(AvalonExprParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubSubRightExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubSubRightExpression(AvalonExprParser.SubSubRightExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubSubRightExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubSubRightExpression(AvalonExprParser.SubSubRightExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NumberExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNumberExpression(AvalonExprParser.NumberExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumberExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNumberExpression(AvalonExprParser.NumberExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ObjVisitMethodExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterObjVisitMethodExpression(AvalonExprParser.ObjVisitMethodExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ObjVisitMethodExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitObjVisitMethodExpression(AvalonExprParser.ObjVisitMethodExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddAddLeftExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddAddLeftExpression(AvalonExprParser.AddAddLeftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddAddLeftExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddAddLeftExpression(AvalonExprParser.AddAddLeftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSubExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSubExpression(AvalonExprParser.AddSubExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSubExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSubExpression(AvalonExprParser.AddSubExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TrueExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTrueExpression(AvalonExprParser.TrueExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TrueExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTrueExpression(AvalonExprParser.TrueExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParentExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParentExpression(AvalonExprParser.ParentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParentExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParentExpression(AvalonExprParser.ParentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SubSubLeftExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubSubLeftExpression(AvalonExprParser.SubSubLeftExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SubSubLeftExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubSubLeftExpression(AvalonExprParser.SubSubLeftExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ComparatorExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterComparatorExpression(AvalonExprParser.ComparatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ComparatorExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitComparatorExpression(AvalonExprParser.ComparatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConditionExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConditionExpression(AvalonExprParser.ConditionExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConditionExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConditionExpression(AvalonExprParser.ConditionExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code StringExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStringExpression(AvalonExprParser.StringExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code StringExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStringExpression(AvalonExprParser.StringExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IdExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIdExpression(AvalonExprParser.IdExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IdExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIdExpression(AvalonExprParser.IdExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddAddRightExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddAddRightExpression(AvalonExprParser.AddAddRightExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddAddRightExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddAddRightExpression(AvalonExprParser.AddAddRightExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code CallFuncExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCallFuncExpression(AvalonExprParser.CallFuncExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code CallFuncExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCallFuncExpression(AvalonExprParser.CallFuncExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryOpExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryOpExpression(AvalonExprParser.UnaryOpExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryOpExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryOpExpression(AvalonExprParser.UnaryOpExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ObjVisitFieldExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterObjVisitFieldExpression(AvalonExprParser.ObjVisitFieldExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ObjVisitFieldExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitObjVisitFieldExpression(AvalonExprParser.ObjVisitFieldExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code FalseExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFalseExpression(AvalonExprParser.FalseExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code FalseExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFalseExpression(AvalonExprParser.FalseExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDivExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDivExpression(AvalonExprParser.MulDivExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDivExpression}
	 * labeled alternative in {@link AvalonExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDivExpression(AvalonExprParser.MulDivExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#exprList}.
	 * @param ctx the parse tree
	 */
	void enterExprList(AvalonExprParser.ExprListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#exprList}.
	 * @param ctx the parse tree
	 */
	void exitExprList(AvalonExprParser.ExprListContext ctx);
	/**
	 * Enter a parse tree produced by {@link AvalonExprParser#methodExpr}.
	 * @param ctx the parse tree
	 */
	void enterMethodExpr(AvalonExprParser.MethodExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AvalonExprParser#methodExpr}.
	 * @param ctx the parse tree
	 */
	void exitMethodExpr(AvalonExprParser.MethodExprContext ctx);
}