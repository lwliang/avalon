/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.core.antlr4.grammar;// Generated from AvalonExpr.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class AvalonExprParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		ID=10, NEWLINE=11, SEMI=12, NUMBER=13, INT=14, STRING=15, WS=16, LINE_COMMENT=17, 
		COMMENT=18, MUL=19, DIV=20, ADD=21, SUB=22, TRUE=23, FALSE=24, NOT=25, 
		GT=26, GE=27, LT=28, LE=29, EQ=30, NEQ=31, LPAREN=32, RPAREN=33, LBrace=34, 
		RBrace=35;
	public static final int
		RULE_prog = 0, RULE_stat = 1, RULE_ifStatExpr = 2, RULE_elseIfStatExpr = 3, 
		RULE_elseStatExpr = 4, RULE_ifBodyStatExpr = 5, RULE_forStatExpr = 6, 
		RULE_forInitExpr = 7, RULE_forBodyExpr = 8, RULE_whileStatExpr = 9, RULE_whileBody = 10, 
		RULE_boolExpr = 11, RULE_expr = 12, RULE_exprList = 13, RULE_methodExpr = 14;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "stat", "ifStatExpr", "elseIfStatExpr", "elseStatExpr", "ifBodyStatExpr", 
			"forStatExpr", "forInitExpr", "forBodyExpr", "whileStatExpr", "whileBody", 
			"boolExpr", "expr", "exprList", "methodExpr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "'if'", "'else'", "'for'", "'while'", "'?'", "':'", "'.'", 
			"','", null, null, "';'", null, null, null, null, null, null, "'*'", 
			"'/'", "'+'", "'-'", "'true'", "'false'", "'!'", "'>'", "'>='", "'<'", 
			"'<='", "'=='", "'!='", "'('", "')'", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, "ID", "NEWLINE", 
			"SEMI", "NUMBER", "INT", "STRING", "WS", "LINE_COMMENT", "COMMENT", "MUL", 
			"DIV", "ADD", "SUB", "TRUE", "FALSE", "NOT", "GT", "GE", "LT", "LE", 
			"EQ", "NEQ", "LPAREN", "RPAREN", "LBrace", "RBrace"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "AvalonExpr.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public AvalonExprParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitProg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(30);
				stat();
				}
				}
				setState(33); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4360039476L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatContext extends ParserRuleContext {
		public StatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stat; }
	 
		public StatContext() { }
		public void copyFrom(StatContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ForStatContext extends StatContext {
		public ForStatExprContext forStatExpr() {
			return getRuleContext(ForStatExprContext.class,0);
		}
		public ForStatContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterForStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitForStat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitForStat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AssignStatContext extends StatContext {
		public TerminalNode ID() { return getToken(AvalonExprParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(AvalonExprParser.SEMI, 0); }
		public AssignStatContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterAssignStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitAssignStat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitAssignStat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class PrintStatContext extends StatContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(AvalonExprParser.SEMI, 0); }
		public PrintStatContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterPrintStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitPrintStat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitPrintStat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IfStatContext extends StatContext {
		public IfStatExprContext ifStatExpr() {
			return getRuleContext(IfStatExprContext.class,0);
		}
		public IfStatContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterIfStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitIfStat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitIfStat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NewlineStatContext extends StatContext {
		public TerminalNode NEWLINE() { return getToken(AvalonExprParser.NEWLINE, 0); }
		public NewlineStatContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterNewlineStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitNewlineStat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitNewlineStat(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatContext extends StatContext {
		public WhileStatExprContext whileStatExpr() {
			return getRuleContext(WhileStatExprContext.class,0);
		}
		public WhileStatContext(StatContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterWhileStat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitWhileStat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitWhileStat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatContext stat() throws RecognitionException {
		StatContext _localctx = new StatContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stat);
		try {
			setState(47);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new PrintStatContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				expr(0);
				setState(36);
				match(SEMI);
				}
				break;
			case 2:
				_localctx = new AssignStatContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(38);
				match(ID);
				setState(39);
				match(T__0);
				setState(40);
				expr(0);
				setState(41);
				match(SEMI);
				}
				break;
			case 3:
				_localctx = new IfStatContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(43);
				ifStatExpr();
				}
				break;
			case 4:
				_localctx = new ForStatContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(44);
				forStatExpr();
				}
				break;
			case 5:
				_localctx = new WhileStatContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(45);
				whileStatExpr();
				}
				break;
			case 6:
				_localctx = new NewlineStatContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(46);
				match(NEWLINE);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStatExprContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(AvalonExprParser.LPAREN, 0); }
		public BoolExprContext boolExpr() {
			return getRuleContext(BoolExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AvalonExprParser.RPAREN, 0); }
		public TerminalNode LBrace() { return getToken(AvalonExprParser.LBrace, 0); }
		public IfBodyStatExprContext ifBodyStatExpr() {
			return getRuleContext(IfBodyStatExprContext.class,0);
		}
		public TerminalNode RBrace() { return getToken(AvalonExprParser.RBrace, 0); }
		public List<ElseIfStatExprContext> elseIfStatExpr() {
			return getRuleContexts(ElseIfStatExprContext.class);
		}
		public ElseIfStatExprContext elseIfStatExpr(int i) {
			return getRuleContext(ElseIfStatExprContext.class,i);
		}
		public ElseStatExprContext elseStatExpr() {
			return getRuleContext(ElseStatExprContext.class,0);
		}
		public IfStatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterIfStatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitIfStatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitIfStatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatExprContext ifStatExpr() throws RecognitionException {
		IfStatExprContext _localctx = new IfStatExprContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_ifStatExpr);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(T__1);
			setState(50);
			match(LPAREN);
			setState(51);
			boolExpr();
			setState(52);
			match(RPAREN);
			setState(53);
			match(LBrace);
			setState(54);
			ifBodyStatExpr();
			setState(55);
			match(RBrace);
			setState(59);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(56);
					elseIfStatExpr();
					}
					} 
				}
				setState(61);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(62);
				elseStatExpr();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseIfStatExprContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(AvalonExprParser.LPAREN, 0); }
		public BoolExprContext boolExpr() {
			return getRuleContext(BoolExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AvalonExprParser.RPAREN, 0); }
		public TerminalNode LBrace() { return getToken(AvalonExprParser.LBrace, 0); }
		public IfBodyStatExprContext ifBodyStatExpr() {
			return getRuleContext(IfBodyStatExprContext.class,0);
		}
		public TerminalNode RBrace() { return getToken(AvalonExprParser.RBrace, 0); }
		public ElseIfStatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseIfStatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterElseIfStatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitElseIfStatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitElseIfStatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseIfStatExprContext elseIfStatExpr() throws RecognitionException {
		ElseIfStatExprContext _localctx = new ElseIfStatExprContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_elseIfStatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(T__2);
			setState(66);
			match(T__1);
			setState(67);
			match(LPAREN);
			setState(68);
			boolExpr();
			setState(69);
			match(RPAREN);
			setState(70);
			match(LBrace);
			setState(71);
			ifBodyStatExpr();
			setState(72);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseStatExprContext extends ParserRuleContext {
		public TerminalNode LBrace() { return getToken(AvalonExprParser.LBrace, 0); }
		public IfBodyStatExprContext ifBodyStatExpr() {
			return getRuleContext(IfBodyStatExprContext.class,0);
		}
		public TerminalNode RBrace() { return getToken(AvalonExprParser.RBrace, 0); }
		public ElseStatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterElseStatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitElseStatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitElseStatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStatExprContext elseStatExpr() throws RecognitionException {
		ElseStatExprContext _localctx = new ElseStatExprContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_elseStatExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(74);
			match(T__2);
			setState(75);
			match(LBrace);
			setState(76);
			ifBodyStatExpr();
			setState(77);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfBodyStatExprContext extends ParserRuleContext {
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public IfBodyStatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifBodyStatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterIfBodyStatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitIfBodyStatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitIfBodyStatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfBodyStatExprContext ifBodyStatExpr() throws RecognitionException {
		IfBodyStatExprContext _localctx = new IfBodyStatExprContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ifBodyStatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(79);
				stat();
				}
				}
				setState(82); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4360039476L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatExprContext extends ParserRuleContext {
		public ExprContext judge;
		public ExprContext last;
		public TerminalNode LPAREN() { return getToken(AvalonExprParser.LPAREN, 0); }
		public List<TerminalNode> SEMI() { return getTokens(AvalonExprParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(AvalonExprParser.SEMI, i);
		}
		public TerminalNode RPAREN() { return getToken(AvalonExprParser.RPAREN, 0); }
		public TerminalNode LBrace() { return getToken(AvalonExprParser.LBrace, 0); }
		public ForBodyExprContext forBodyExpr() {
			return getRuleContext(ForBodyExprContext.class,0);
		}
		public TerminalNode RBrace() { return getToken(AvalonExprParser.RBrace, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ForInitExprContext forInitExpr() {
			return getRuleContext(ForInitExprContext.class,0);
		}
		public List<TerminalNode> NEWLINE() { return getTokens(AvalonExprParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(AvalonExprParser.NEWLINE, i);
		}
		public ForStatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterForStatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitForStatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitForStatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStatExprContext forStatExpr() throws RecognitionException {
		ForStatExprContext _localctx = new ForStatExprContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_forStatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(T__3);
			setState(85);
			match(LPAREN);
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(86);
				forInitExpr();
				}
			}

			setState(89);
			match(SEMI);
			setState(90);
			((ForStatExprContext)_localctx).judge = expr(0);
			setState(91);
			match(SEMI);
			setState(92);
			((ForStatExprContext)_localctx).last = expr(0);
			setState(93);
			match(RPAREN);
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(94);
				match(NEWLINE);
				}
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(100);
			match(LBrace);
			setState(101);
			forBodyExpr();
			setState(102);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForInitExprContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(AvalonExprParser.ID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ForInitExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInitExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterForInitExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitForInitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitForInitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitExprContext forInitExpr() throws RecognitionException {
		ForInitExprContext _localctx = new ForInitExprContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_forInitExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(ID);
			setState(105);
			match(T__0);
			setState(106);
			expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForBodyExprContext extends ParserRuleContext {
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public ForBodyExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forBodyExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterForBodyExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitForBodyExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitForBodyExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForBodyExprContext forBodyExpr() throws RecognitionException {
		ForBodyExprContext _localctx = new ForBodyExprContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_forBodyExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(108);
				stat();
				}
				}
				setState(111); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4360039476L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatExprContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(AvalonExprParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AvalonExprParser.RPAREN, 0); }
		public TerminalNode LBrace() { return getToken(AvalonExprParser.LBrace, 0); }
		public WhileBodyContext whileBody() {
			return getRuleContext(WhileBodyContext.class,0);
		}
		public TerminalNode RBrace() { return getToken(AvalonExprParser.RBrace, 0); }
		public List<TerminalNode> NEWLINE() { return getTokens(AvalonExprParser.NEWLINE); }
		public TerminalNode NEWLINE(int i) {
			return getToken(AvalonExprParser.NEWLINE, i);
		}
		public WhileStatExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterWhileStatExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitWhileStatExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitWhileStatExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatExprContext whileStatExpr() throws RecognitionException {
		WhileStatExprContext _localctx = new WhileStatExprContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_whileStatExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(T__4);
			setState(114);
			match(LPAREN);
			setState(115);
			expr(0);
			setState(116);
			match(RPAREN);
			setState(120);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==NEWLINE) {
				{
				{
				setState(117);
				match(NEWLINE);
				}
				}
				setState(122);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(123);
			match(LBrace);
			setState(124);
			whileBody();
			setState(125);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileBodyContext extends ParserRuleContext {
		public List<StatContext> stat() {
			return getRuleContexts(StatContext.class);
		}
		public StatContext stat(int i) {
			return getRuleContext(StatContext.class,i);
		}
		public WhileBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterWhileBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitWhileBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitWhileBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileBodyContext whileBody() throws RecognitionException {
		WhileBodyContext _localctx = new WhileBodyContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_whileBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(127);
				stat();
				}
				}
				setState(130); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 4360039476L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BoolExprContext extends ParserRuleContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode GT() { return getToken(AvalonExprParser.GT, 0); }
		public TerminalNode GE() { return getToken(AvalonExprParser.GE, 0); }
		public TerminalNode LT() { return getToken(AvalonExprParser.LT, 0); }
		public TerminalNode LE() { return getToken(AvalonExprParser.LE, 0); }
		public TerminalNode EQ() { return getToken(AvalonExprParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(AvalonExprParser.NEQ, 0); }
		public BoolExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterBoolExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitBoolExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitBoolExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BoolExprContext boolExpr() throws RecognitionException {
		BoolExprContext _localctx = new BoolExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_boolExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			((BoolExprContext)_localctx).left = expr(0);
			setState(133);
			((BoolExprContext)_localctx).op = _input.LT(1);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4227858432L) != 0)) ) {
				((BoolExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(134);
			((BoolExprContext)_localctx).right = expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IntExpressionContext extends ExprContext {
		public TerminalNode INT() { return getToken(AvalonExprParser.INT, 0); }
		public IntExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterIntExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitIntExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitIntExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NotExpressionContext extends ExprContext {
		public TerminalNode NOT() { return getToken(AvalonExprParser.NOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NotExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitNotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitNotExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SubSubRightExpressionContext extends ExprContext {
		public List<TerminalNode> SUB() { return getTokens(AvalonExprParser.SUB); }
		public TerminalNode SUB(int i) {
			return getToken(AvalonExprParser.SUB, i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SubSubRightExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterSubSubRightExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitSubSubRightExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitSubSubRightExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class NumberExpressionContext extends ExprContext {
		public TerminalNode NUMBER() { return getToken(AvalonExprParser.NUMBER, 0); }
		public NumberExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterNumberExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitNumberExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitNumberExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ObjVisitMethodExpressionContext extends ExprContext {
		public ExprContext obj;
		public MethodExprContext methodExpr() {
			return getRuleContext(MethodExprContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ObjVisitMethodExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterObjVisitMethodExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitObjVisitMethodExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitObjVisitMethodExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddAddLeftExpressionContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<TerminalNode> ADD() { return getTokens(AvalonExprParser.ADD); }
		public TerminalNode ADD(int i) {
			return getToken(AvalonExprParser.ADD, i);
		}
		public AddAddLeftExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterAddAddLeftExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitAddAddLeftExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitAddAddLeftExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddSubExpressionContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ADD() { return getToken(AvalonExprParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(AvalonExprParser.SUB, 0); }
		public AddSubExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterAddSubExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitAddSubExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitAddSubExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class TrueExpressionContext extends ExprContext {
		public TerminalNode TRUE() { return getToken(AvalonExprParser.TRUE, 0); }
		public TrueExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterTrueExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitTrueExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitTrueExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ParentExpressionContext extends ExprContext {
		public TerminalNode LPAREN() { return getToken(AvalonExprParser.LPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(AvalonExprParser.RPAREN, 0); }
		public ParentExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterParentExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitParentExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitParentExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SubSubLeftExpressionContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<TerminalNode> SUB() { return getTokens(AvalonExprParser.SUB); }
		public TerminalNode SUB(int i) {
			return getToken(AvalonExprParser.SUB, i);
		}
		public SubSubLeftExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterSubSubLeftExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitSubSubLeftExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitSubSubLeftExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ComparatorExpressionContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode GT() { return getToken(AvalonExprParser.GT, 0); }
		public TerminalNode GE() { return getToken(AvalonExprParser.GE, 0); }
		public TerminalNode LT() { return getToken(AvalonExprParser.LT, 0); }
		public TerminalNode LE() { return getToken(AvalonExprParser.LE, 0); }
		public TerminalNode EQ() { return getToken(AvalonExprParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(AvalonExprParser.NEQ, 0); }
		public ComparatorExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterComparatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitComparatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitComparatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConditionExpressionContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ConditionExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterConditionExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitConditionExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitConditionExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class StringExpressionContext extends ExprContext {
		public TerminalNode STRING() { return getToken(AvalonExprParser.STRING, 0); }
		public StringExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterStringExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitStringExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitStringExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class IdExpressionContext extends ExprContext {
		public TerminalNode ID() { return getToken(AvalonExprParser.ID, 0); }
		public IdExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterIdExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitIdExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitIdExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AddAddRightExpressionContext extends ExprContext {
		public List<TerminalNode> ADD() { return getTokens(AvalonExprParser.ADD); }
		public TerminalNode ADD(int i) {
			return getToken(AvalonExprParser.ADD, i);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public AddAddRightExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterAddAddRightExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitAddAddRightExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitAddAddRightExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CallFuncExpressionContext extends ExprContext {
		public ExprContext funName;
		public TerminalNode LPAREN() { return getToken(AvalonExprParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(AvalonExprParser.RPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public CallFuncExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterCallFuncExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitCallFuncExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitCallFuncExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class UnaryOpExpressionContext extends ExprContext {
		public Token op;
		public ExprContext right;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ADD() { return getToken(AvalonExprParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(AvalonExprParser.SUB, 0); }
		public UnaryOpExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterUnaryOpExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitUnaryOpExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitUnaryOpExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ObjVisitFieldExpressionContext extends ExprContext {
		public ExprContext obj;
		public Token field;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode ID() { return getToken(AvalonExprParser.ID, 0); }
		public ObjVisitFieldExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterObjVisitFieldExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitObjVisitFieldExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitObjVisitFieldExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class FalseExpressionContext extends ExprContext {
		public TerminalNode FALSE() { return getToken(AvalonExprParser.FALSE, 0); }
		public FalseExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterFalseExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitFalseExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitFalseExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class MulDivExpressionContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MUL() { return getToken(AvalonExprParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(AvalonExprParser.DIV, 0); }
		public MulDivExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterMulDivExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitMulDivExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitMulDivExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				_localctx = new UnaryOpExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(137);
				((UnaryOpExpressionContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
					((UnaryOpExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(138);
				((UnaryOpExpressionContext)_localctx).right = expr(18);
				}
				break;
			case 2:
				{
				_localctx = new NotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(139);
				match(NOT);
				setState(140);
				expr(17);
				}
				break;
			case 3:
				{
				_localctx = new AddAddRightExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(141);
				match(ADD);
				setState(142);
				match(ADD);
				setState(143);
				expr(15);
				}
				break;
			case 4:
				{
				_localctx = new SubSubRightExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(144);
				match(SUB);
				setState(145);
				match(SUB);
				setState(146);
				expr(13);
				}
				break;
			case 5:
				{
				_localctx = new IntExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(147);
				match(INT);
				}
				break;
			case 6:
				{
				_localctx = new NumberExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(148);
				match(NUMBER);
				}
				break;
			case 7:
				{
				_localctx = new IdExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(149);
				match(ID);
				}
				break;
			case 8:
				{
				_localctx = new TrueExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(150);
				match(TRUE);
				}
				break;
			case 9:
				{
				_localctx = new FalseExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(151);
				match(FALSE);
				}
				break;
			case 10:
				{
				_localctx = new StringExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(152);
				match(STRING);
				}
				break;
			case 11:
				{
				_localctx = new ParentExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(153);
				match(LPAREN);
				setState(154);
				expr(0);
				setState(155);
				match(RPAREN);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(194);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(192);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
					case 1:
						{
						_localctx = new MulDivExpressionContext(new ExprContext(_parentctx, _parentState));
						((MulDivExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(159);
						if (!(precpred(_ctx, 20))) throw new FailedPredicateException(this, "precpred(_ctx, 20)");
						setState(160);
						((MulDivExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MUL || _la==DIV) ) {
							((MulDivExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(161);
						((MulDivExpressionContext)_localctx).right = expr(21);
						}
						break;
					case 2:
						{
						_localctx = new AddSubExpressionContext(new ExprContext(_parentctx, _parentState));
						((AddSubExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(162);
						if (!(precpred(_ctx, 19))) throw new FailedPredicateException(this, "precpred(_ctx, 19)");
						setState(163);
						((AddSubExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((AddSubExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(164);
						((AddSubExpressionContext)_localctx).right = expr(20);
						}
						break;
					case 3:
						{
						_localctx = new ComparatorExpressionContext(new ExprContext(_parentctx, _parentState));
						((ComparatorExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(165);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(166);
						((ComparatorExpressionContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 4227858432L) != 0)) ) {
							((ComparatorExpressionContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(167);
						((ComparatorExpressionContext)_localctx).right = expr(17);
						}
						break;
					case 4:
						{
						_localctx = new ConditionExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(168);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(169);
						match(T__5);
						setState(170);
						expr(0);
						setState(171);
						match(T__6);
						setState(172);
						expr(12);
						}
						break;
					case 5:
						{
						_localctx = new AddAddLeftExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(174);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(175);
						match(ADD);
						setState(176);
						match(ADD);
						}
						break;
					case 6:
						{
						_localctx = new SubSubLeftExpressionContext(new ExprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(177);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(178);
						match(SUB);
						setState(179);
						match(SUB);
						}
						break;
					case 7:
						{
						_localctx = new ObjVisitMethodExpressionContext(new ExprContext(_parentctx, _parentState));
						((ObjVisitMethodExpressionContext)_localctx).obj = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(180);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(181);
						match(T__7);
						setState(182);
						methodExpr();
						}
						break;
					case 8:
						{
						_localctx = new ObjVisitFieldExpressionContext(new ExprContext(_parentctx, _parentState));
						((ObjVisitFieldExpressionContext)_localctx).obj = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(183);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(184);
						match(T__7);
						setState(185);
						((ObjVisitFieldExpressionContext)_localctx).field = match(ID);
						}
						break;
					case 9:
						{
						_localctx = new CallFuncExpressionContext(new ExprContext(_parentctx, _parentState));
						((CallFuncExpressionContext)_localctx).funName = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(186);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(187);
						match(LPAREN);
						setState(189);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4360037376L) != 0)) {
							{
							setState(188);
							exprList();
							}
						}

						setState(191);
						match(RPAREN);
						}
						break;
					}
					} 
				}
				setState(196);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprListContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public ExprListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterExprList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitExprList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitExprList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprListContext exprList() throws RecognitionException {
		ExprListContext _localctx = new ExprListContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_exprList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(197);
			expr(0);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				setState(198);
				match(T__8);
				setState(199);
				expr(0);
				}
				}
				setState(204);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodExprContext extends ParserRuleContext {
		public ExprContext funName;
		public TerminalNode LPAREN() { return getToken(AvalonExprParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(AvalonExprParser.RPAREN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ExprListContext exprList() {
			return getRuleContext(ExprListContext.class,0);
		}
		public MethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).enterMethodExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AvalonExprListener ) ((AvalonExprListener)listener).exitMethodExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AvalonExprVisitor ) return ((AvalonExprVisitor<? extends T>)visitor).visitMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodExprContext methodExpr() throws RecognitionException {
		MethodExprContext _localctx = new MethodExprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_methodExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			((MethodExprContext)_localctx).funName = expr(0);
			setState(206);
			match(LPAREN);
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 4360037376L) != 0)) {
				{
				setState(207);
				exprList();
				}
			}

			setState(210);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 12:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 20);
		case 1:
			return precpred(_ctx, 19);
		case 2:
			return precpred(_ctx, 16);
		case 3:
			return precpred(_ctx, 11);
		case 4:
			return precpred(_ctx, 14);
		case 5:
			return precpred(_ctx, 12);
		case 6:
			return precpred(_ctx, 10);
		case 7:
			return precpred(_ctx, 9);
		case 8:
			return precpred(_ctx, 8);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001#\u00d5\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0001\u0000\u0004\u0000"+
		" \b\u0000\u000b\u0000\f\u0000!\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0003\u00010\b\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0005\u0002:\b\u0002\n\u0002\f\u0002=\t\u0002\u0001\u0002\u0003"+
		"\u0002@\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0004\u0005Q\b"+
		"\u0005\u000b\u0005\f\u0005R\u0001\u0006\u0001\u0006\u0001\u0006\u0003"+
		"\u0006X\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0005\u0006`\b\u0006\n\u0006\f\u0006c\t\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0004\bn\b\b\u000b\b\f\bo\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0005\tw\b\t\n\t\f\tz\t\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\n\u0004\n\u0081\b\n\u000b\n\f\n\u0082\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u009e\b\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001"+
		"\f\u0001\f\u0001\f\u0003\f\u00be\b\f\u0001\f\u0005\f\u00c1\b\f\n\f\f\f"+
		"\u00c4\t\f\u0001\r\u0001\r\u0001\r\u0005\r\u00c9\b\r\n\r\f\r\u00cc\t\r"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00d1\b\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0000\u0001\u0018\u000f\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u0000\u0003\u0001"+
		"\u0000\u001a\u001f\u0001\u0000\u0015\u0016\u0001\u0000\u0013\u0014\u00e9"+
		"\u0000\u001f\u0001\u0000\u0000\u0000\u0002/\u0001\u0000\u0000\u0000\u0004"+
		"1\u0001\u0000\u0000\u0000\u0006A\u0001\u0000\u0000\u0000\bJ\u0001\u0000"+
		"\u0000\u0000\nP\u0001\u0000\u0000\u0000\fT\u0001\u0000\u0000\u0000\u000e"+
		"h\u0001\u0000\u0000\u0000\u0010m\u0001\u0000\u0000\u0000\u0012q\u0001"+
		"\u0000\u0000\u0000\u0014\u0080\u0001\u0000\u0000\u0000\u0016\u0084\u0001"+
		"\u0000\u0000\u0000\u0018\u009d\u0001\u0000\u0000\u0000\u001a\u00c5\u0001"+
		"\u0000\u0000\u0000\u001c\u00cd\u0001\u0000\u0000\u0000\u001e \u0003\u0002"+
		"\u0001\u0000\u001f\u001e\u0001\u0000\u0000\u0000 !\u0001\u0000\u0000\u0000"+
		"!\u001f\u0001\u0000\u0000\u0000!\"\u0001\u0000\u0000\u0000\"\u0001\u0001"+
		"\u0000\u0000\u0000#$\u0003\u0018\f\u0000$%\u0005\f\u0000\u0000%0\u0001"+
		"\u0000\u0000\u0000&\'\u0005\n\u0000\u0000\'(\u0005\u0001\u0000\u0000("+
		")\u0003\u0018\f\u0000)*\u0005\f\u0000\u0000*0\u0001\u0000\u0000\u0000"+
		"+0\u0003\u0004\u0002\u0000,0\u0003\f\u0006\u0000-0\u0003\u0012\t\u0000"+
		".0\u0005\u000b\u0000\u0000/#\u0001\u0000\u0000\u0000/&\u0001\u0000\u0000"+
		"\u0000/+\u0001\u0000\u0000\u0000/,\u0001\u0000\u0000\u0000/-\u0001\u0000"+
		"\u0000\u0000/.\u0001\u0000\u0000\u00000\u0003\u0001\u0000\u0000\u0000"+
		"12\u0005\u0002\u0000\u000023\u0005 \u0000\u000034\u0003\u0016\u000b\u0000"+
		"45\u0005!\u0000\u000056\u0005\"\u0000\u000067\u0003\n\u0005\u00007;\u0005"+
		"#\u0000\u00008:\u0003\u0006\u0003\u000098\u0001\u0000\u0000\u0000:=\u0001"+
		"\u0000\u0000\u0000;9\u0001\u0000\u0000\u0000;<\u0001\u0000\u0000\u0000"+
		"<?\u0001\u0000\u0000\u0000=;\u0001\u0000\u0000\u0000>@\u0003\b\u0004\u0000"+
		"?>\u0001\u0000\u0000\u0000?@\u0001\u0000\u0000\u0000@\u0005\u0001\u0000"+
		"\u0000\u0000AB\u0005\u0003\u0000\u0000BC\u0005\u0002\u0000\u0000CD\u0005"+
		" \u0000\u0000DE\u0003\u0016\u000b\u0000EF\u0005!\u0000\u0000FG\u0005\""+
		"\u0000\u0000GH\u0003\n\u0005\u0000HI\u0005#\u0000\u0000I\u0007\u0001\u0000"+
		"\u0000\u0000JK\u0005\u0003\u0000\u0000KL\u0005\"\u0000\u0000LM\u0003\n"+
		"\u0005\u0000MN\u0005#\u0000\u0000N\t\u0001\u0000\u0000\u0000OQ\u0003\u0002"+
		"\u0001\u0000PO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000RP\u0001"+
		"\u0000\u0000\u0000RS\u0001\u0000\u0000\u0000S\u000b\u0001\u0000\u0000"+
		"\u0000TU\u0005\u0004\u0000\u0000UW\u0005 \u0000\u0000VX\u0003\u000e\u0007"+
		"\u0000WV\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000\u0000XY\u0001\u0000"+
		"\u0000\u0000YZ\u0005\f\u0000\u0000Z[\u0003\u0018\f\u0000[\\\u0005\f\u0000"+
		"\u0000\\]\u0003\u0018\f\u0000]a\u0005!\u0000\u0000^`\u0005\u000b\u0000"+
		"\u0000_^\u0001\u0000\u0000\u0000`c\u0001\u0000\u0000\u0000a_\u0001\u0000"+
		"\u0000\u0000ab\u0001\u0000\u0000\u0000bd\u0001\u0000\u0000\u0000ca\u0001"+
		"\u0000\u0000\u0000de\u0005\"\u0000\u0000ef\u0003\u0010\b\u0000fg\u0005"+
		"#\u0000\u0000g\r\u0001\u0000\u0000\u0000hi\u0005\n\u0000\u0000ij\u0005"+
		"\u0001\u0000\u0000jk\u0003\u0018\f\u0000k\u000f\u0001\u0000\u0000\u0000"+
		"ln\u0003\u0002\u0001\u0000ml\u0001\u0000\u0000\u0000no\u0001\u0000\u0000"+
		"\u0000om\u0001\u0000\u0000\u0000op\u0001\u0000\u0000\u0000p\u0011\u0001"+
		"\u0000\u0000\u0000qr\u0005\u0005\u0000\u0000rs\u0005 \u0000\u0000st\u0003"+
		"\u0018\f\u0000tx\u0005!\u0000\u0000uw\u0005\u000b\u0000\u0000vu\u0001"+
		"\u0000\u0000\u0000wz\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000\u0000"+
		"xy\u0001\u0000\u0000\u0000y{\u0001\u0000\u0000\u0000zx\u0001\u0000\u0000"+
		"\u0000{|\u0005\"\u0000\u0000|}\u0003\u0014\n\u0000}~\u0005#\u0000\u0000"+
		"~\u0013\u0001\u0000\u0000\u0000\u007f\u0081\u0003\u0002\u0001\u0000\u0080"+
		"\u007f\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082"+
		"\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000\u0083"+
		"\u0015\u0001\u0000\u0000\u0000\u0084\u0085\u0003\u0018\f\u0000\u0085\u0086"+
		"\u0007\u0000\u0000\u0000\u0086\u0087\u0003\u0018\f\u0000\u0087\u0017\u0001"+
		"\u0000\u0000\u0000\u0088\u0089\u0006\f\uffff\uffff\u0000\u0089\u008a\u0007"+
		"\u0001\u0000\u0000\u008a\u009e\u0003\u0018\f\u0012\u008b\u008c\u0005\u0019"+
		"\u0000\u0000\u008c\u009e\u0003\u0018\f\u0011\u008d\u008e\u0005\u0015\u0000"+
		"\u0000\u008e\u008f\u0005\u0015\u0000\u0000\u008f\u009e\u0003\u0018\f\u000f"+
		"\u0090\u0091\u0005\u0016\u0000\u0000\u0091\u0092\u0005\u0016\u0000\u0000"+
		"\u0092\u009e\u0003\u0018\f\r\u0093\u009e\u0005\u000e\u0000\u0000\u0094"+
		"\u009e\u0005\r\u0000\u0000\u0095\u009e\u0005\n\u0000\u0000\u0096\u009e"+
		"\u0005\u0017\u0000\u0000\u0097\u009e\u0005\u0018\u0000\u0000\u0098\u009e"+
		"\u0005\u000f\u0000\u0000\u0099\u009a\u0005 \u0000\u0000\u009a\u009b\u0003"+
		"\u0018\f\u0000\u009b\u009c\u0005!\u0000\u0000\u009c\u009e\u0001\u0000"+
		"\u0000\u0000\u009d\u0088\u0001\u0000\u0000\u0000\u009d\u008b\u0001\u0000"+
		"\u0000\u0000\u009d\u008d\u0001\u0000\u0000\u0000\u009d\u0090\u0001\u0000"+
		"\u0000\u0000\u009d\u0093\u0001\u0000\u0000\u0000\u009d\u0094\u0001\u0000"+
		"\u0000\u0000\u009d\u0095\u0001\u0000\u0000\u0000\u009d\u0096\u0001\u0000"+
		"\u0000\u0000\u009d\u0097\u0001\u0000\u0000\u0000\u009d\u0098\u0001\u0000"+
		"\u0000\u0000\u009d\u0099\u0001\u0000\u0000\u0000\u009e\u00c2\u0001\u0000"+
		"\u0000\u0000\u009f\u00a0\n\u0014\u0000\u0000\u00a0\u00a1\u0007\u0002\u0000"+
		"\u0000\u00a1\u00c1\u0003\u0018\f\u0015\u00a2\u00a3\n\u0013\u0000\u0000"+
		"\u00a3\u00a4\u0007\u0001\u0000\u0000\u00a4\u00c1\u0003\u0018\f\u0014\u00a5"+
		"\u00a6\n\u0010\u0000\u0000\u00a6\u00a7\u0007\u0000\u0000\u0000\u00a7\u00c1"+
		"\u0003\u0018\f\u0011\u00a8\u00a9\n\u000b\u0000\u0000\u00a9\u00aa\u0005"+
		"\u0006\u0000\u0000\u00aa\u00ab\u0003\u0018\f\u0000\u00ab\u00ac\u0005\u0007"+
		"\u0000\u0000\u00ac\u00ad\u0003\u0018\f\f\u00ad\u00c1\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\n\u000e\u0000\u0000\u00af\u00b0\u0005\u0015\u0000\u0000"+
		"\u00b0\u00c1\u0005\u0015\u0000\u0000\u00b1\u00b2\n\f\u0000\u0000\u00b2"+
		"\u00b3\u0005\u0016\u0000\u0000\u00b3\u00c1\u0005\u0016\u0000\u0000\u00b4"+
		"\u00b5\n\n\u0000\u0000\u00b5\u00b6\u0005\b\u0000\u0000\u00b6\u00c1\u0003"+
		"\u001c\u000e\u0000\u00b7\u00b8\n\t\u0000\u0000\u00b8\u00b9\u0005\b\u0000"+
		"\u0000\u00b9\u00c1\u0005\n\u0000\u0000\u00ba\u00bb\n\b\u0000\u0000\u00bb"+
		"\u00bd\u0005 \u0000\u0000\u00bc\u00be\u0003\u001a\r\u0000\u00bd\u00bc"+
		"\u0001\u0000\u0000\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u00bf"+
		"\u0001\u0000\u0000\u0000\u00bf\u00c1\u0005!\u0000\u0000\u00c0\u009f\u0001"+
		"\u0000\u0000\u0000\u00c0\u00a2\u0001\u0000\u0000\u0000\u00c0\u00a5\u0001"+
		"\u0000\u0000\u0000\u00c0\u00a8\u0001\u0000\u0000\u0000\u00c0\u00ae\u0001"+
		"\u0000\u0000\u0000\u00c0\u00b1\u0001\u0000\u0000\u0000\u00c0\u00b4\u0001"+
		"\u0000\u0000\u0000\u00c0\u00b7\u0001\u0000\u0000\u0000\u00c0\u00ba\u0001"+
		"\u0000\u0000\u0000\u00c1\u00c4\u0001\u0000\u0000\u0000\u00c2\u00c0\u0001"+
		"\u0000\u0000\u0000\u00c2\u00c3\u0001\u0000\u0000\u0000\u00c3\u0019\u0001"+
		"\u0000\u0000\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000\u00c5\u00ca\u0003"+
		"\u0018\f\u0000\u00c6\u00c7\u0005\t\u0000\u0000\u00c7\u00c9\u0003\u0018"+
		"\f\u0000\u00c8\u00c6\u0001\u0000\u0000\u0000\u00c9\u00cc\u0001\u0000\u0000"+
		"\u0000\u00ca\u00c8\u0001\u0000\u0000\u0000\u00ca\u00cb\u0001\u0000\u0000"+
		"\u0000\u00cb\u001b\u0001\u0000\u0000\u0000\u00cc\u00ca\u0001\u0000\u0000"+
		"\u0000\u00cd\u00ce\u0003\u0018\f\u0000\u00ce\u00d0\u0005 \u0000\u0000"+
		"\u00cf\u00d1\u0003\u001a\r\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d0"+
		"\u00d1\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2"+
		"\u00d3\u0005!\u0000\u0000\u00d3\u001d\u0001\u0000\u0000\u0000\u0010!/"+
		";?RWaox\u0082\u009d\u00bd\u00c0\u00c2\u00ca\u00d0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}