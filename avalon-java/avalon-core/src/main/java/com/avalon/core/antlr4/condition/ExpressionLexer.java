package com.avalon.core.antlr4.condition;// Generated from Expression.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ExpressionLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		ID=18, INT=19, FLOAT=20, STRING=21, BOOLEAN=22, WS=23;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"ID", "INT", "FLOAT", "STRING", "BOOLEAN", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'!'", "'&'", "'|'", "'('", "')'", "','", "'between'", "'in'", 
			"'like'", "'notlike'", "'='", "'!='", "'>'", "'<'", "'>='", "'<='", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, "ID", "INT", "FLOAT", "STRING", "BOOLEAN", 
			"WS"
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


	public ExpressionLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Expression.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0017\u00a7\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r"+
		"\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0005\u0011g\b"+
		"\u0011\n\u0011\f\u0011j\t\u0011\u0001\u0012\u0003\u0012m\b\u0012\u0001"+
		"\u0012\u0004\u0012p\b\u0012\u000b\u0012\f\u0012q\u0001\u0013\u0004\u0013"+
		"u\b\u0013\u000b\u0013\f\u0013v\u0001\u0013\u0001\u0013\u0005\u0013{\b"+
		"\u0013\n\u0013\f\u0013~\t\u0013\u0001\u0013\u0001\u0013\u0003\u0013\u0082"+
		"\b\u0013\u0001\u0013\u0004\u0013\u0085\b\u0013\u000b\u0013\f\u0013\u0086"+
		"\u0003\u0013\u0089\b\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0005\u0014\u008f\b\u0014\n\u0014\f\u0014\u0092\t\u0014\u0001\u0014\u0001"+
		"\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0003\u0015\u009f\b\u0015\u0001"+
		"\u0016\u0004\u0016\u00a2\b\u0016\u000b\u0016\f\u0016\u00a3\u0001\u0016"+
		"\u0001\u0016\u0000\u0000\u0017\u0001\u0001\u0003\u0002\u0005\u0003\u0007"+
		"\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b"+
		"\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013"+
		"\'\u0014)\u0015+\u0016-\u0017\u0001\u0000\u0007\u0003\u0000AZ__az\u0004"+
		"\u000009AZ__az\u0001\u000009\u0002\u0000EEee\u0002\u0000++--\u0002\u0000"+
		"\'\'\\\\\u0003\u0000\t\n\r\r  \u00b2\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"+
		"\u0000\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000"+
		"-\u0001\u0000\u0000\u0000\u0001/\u0001\u0000\u0000\u0000\u00031\u0001"+
		"\u0000\u0000\u0000\u00053\u0001\u0000\u0000\u0000\u00075\u0001\u0000\u0000"+
		"\u0000\t7\u0001\u0000\u0000\u0000\u000b9\u0001\u0000\u0000\u0000\r;\u0001"+
		"\u0000\u0000\u0000\u000fC\u0001\u0000\u0000\u0000\u0011F\u0001\u0000\u0000"+
		"\u0000\u0013K\u0001\u0000\u0000\u0000\u0015S\u0001\u0000\u0000\u0000\u0017"+
		"U\u0001\u0000\u0000\u0000\u0019X\u0001\u0000\u0000\u0000\u001bZ\u0001"+
		"\u0000\u0000\u0000\u001d\\\u0001\u0000\u0000\u0000\u001f_\u0001\u0000"+
		"\u0000\u0000!b\u0001\u0000\u0000\u0000#d\u0001\u0000\u0000\u0000%l\u0001"+
		"\u0000\u0000\u0000\'t\u0001\u0000\u0000\u0000)\u008a\u0001\u0000\u0000"+
		"\u0000+\u009e\u0001\u0000\u0000\u0000-\u00a1\u0001\u0000\u0000\u0000/"+
		"0\u0005!\u0000\u00000\u0002\u0001\u0000\u0000\u000012\u0005&\u0000\u0000"+
		"2\u0004\u0001\u0000\u0000\u000034\u0005|\u0000\u00004\u0006\u0001\u0000"+
		"\u0000\u000056\u0005(\u0000\u00006\b\u0001\u0000\u0000\u000078\u0005)"+
		"\u0000\u00008\n\u0001\u0000\u0000\u00009:\u0005,\u0000\u0000:\f\u0001"+
		"\u0000\u0000\u0000;<\u0005b\u0000\u0000<=\u0005e\u0000\u0000=>\u0005t"+
		"\u0000\u0000>?\u0005w\u0000\u0000?@\u0005e\u0000\u0000@A\u0005e\u0000"+
		"\u0000AB\u0005n\u0000\u0000B\u000e\u0001\u0000\u0000\u0000CD\u0005i\u0000"+
		"\u0000DE\u0005n\u0000\u0000E\u0010\u0001\u0000\u0000\u0000FG\u0005l\u0000"+
		"\u0000GH\u0005i\u0000\u0000HI\u0005k\u0000\u0000IJ\u0005e\u0000\u0000"+
		"J\u0012\u0001\u0000\u0000\u0000KL\u0005n\u0000\u0000LM\u0005o\u0000\u0000"+
		"MN\u0005t\u0000\u0000NO\u0005l\u0000\u0000OP\u0005i\u0000\u0000PQ\u0005"+
		"k\u0000\u0000QR\u0005e\u0000\u0000R\u0014\u0001\u0000\u0000\u0000ST\u0005"+
		"=\u0000\u0000T\u0016\u0001\u0000\u0000\u0000UV\u0005!\u0000\u0000VW\u0005"+
		"=\u0000\u0000W\u0018\u0001\u0000\u0000\u0000XY\u0005>\u0000\u0000Y\u001a"+
		"\u0001\u0000\u0000\u0000Z[\u0005<\u0000\u0000[\u001c\u0001\u0000\u0000"+
		"\u0000\\]\u0005>\u0000\u0000]^\u0005=\u0000\u0000^\u001e\u0001\u0000\u0000"+
		"\u0000_`\u0005<\u0000\u0000`a\u0005=\u0000\u0000a \u0001\u0000\u0000\u0000"+
		"bc\u0005.\u0000\u0000c\"\u0001\u0000\u0000\u0000dh\u0007\u0000\u0000\u0000"+
		"eg\u0007\u0001\u0000\u0000fe\u0001\u0000\u0000\u0000gj\u0001\u0000\u0000"+
		"\u0000hf\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000i$\u0001\u0000"+
		"\u0000\u0000jh\u0001\u0000\u0000\u0000km\u0005-\u0000\u0000lk\u0001\u0000"+
		"\u0000\u0000lm\u0001\u0000\u0000\u0000mo\u0001\u0000\u0000\u0000np\u0007"+
		"\u0002\u0000\u0000on\u0001\u0000\u0000\u0000pq\u0001\u0000\u0000\u0000"+
		"qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000r&\u0001\u0000\u0000"+
		"\u0000su\u0007\u0002\u0000\u0000ts\u0001\u0000\u0000\u0000uv\u0001\u0000"+
		"\u0000\u0000vt\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000wx\u0001"+
		"\u0000\u0000\u0000x|\u0005.\u0000\u0000y{\u0007\u0002\u0000\u0000zy\u0001"+
		"\u0000\u0000\u0000{~\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000"+
		"|}\u0001\u0000\u0000\u0000}\u0088\u0001\u0000\u0000\u0000~|\u0001\u0000"+
		"\u0000\u0000\u007f\u0081\u0007\u0003\u0000\u0000\u0080\u0082\u0007\u0004"+
		"\u0000\u0000\u0081\u0080\u0001\u0000\u0000\u0000\u0081\u0082\u0001\u0000"+
		"\u0000\u0000\u0082\u0084\u0001\u0000\u0000\u0000\u0083\u0085\u0007\u0002"+
		"\u0000\u0000\u0084\u0083\u0001\u0000\u0000\u0000\u0085\u0086\u0001\u0000"+
		"\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000"+
		"\u0000\u0000\u0087\u0089\u0001\u0000\u0000\u0000\u0088\u007f\u0001\u0000"+
		"\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089(\u0001\u0000\u0000"+
		"\u0000\u008a\u0090\u0005\'\u0000\u0000\u008b\u008f\b\u0005\u0000\u0000"+
		"\u008c\u008d\u0005\\\u0000\u0000\u008d\u008f\t\u0000\u0000\u0000\u008e"+
		"\u008b\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008f"+
		"\u0092\u0001\u0000\u0000\u0000\u0090\u008e\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0001\u0000\u0000\u0000\u0091\u0093\u0001\u0000\u0000\u0000\u0092"+
		"\u0090\u0001\u0000\u0000\u0000\u0093\u0094\u0005\'\u0000\u0000\u0094*"+
		"\u0001\u0000\u0000\u0000\u0095\u0096\u0005t\u0000\u0000\u0096\u0097\u0005"+
		"r\u0000\u0000\u0097\u0098\u0005u\u0000\u0000\u0098\u009f\u0005e\u0000"+
		"\u0000\u0099\u009a\u0005f\u0000\u0000\u009a\u009b\u0005a\u0000\u0000\u009b"+
		"\u009c\u0005l\u0000\u0000\u009c\u009d\u0005s\u0000\u0000\u009d\u009f\u0005"+
		"e\u0000\u0000\u009e\u0095\u0001\u0000\u0000\u0000\u009e\u0099\u0001\u0000"+
		"\u0000\u0000\u009f,\u0001\u0000\u0000\u0000\u00a0\u00a2\u0007\u0006\u0000"+
		"\u0000\u00a1\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000"+
		"\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a3\u00a4\u0001\u0000\u0000"+
		"\u0000\u00a4\u00a5\u0001\u0000\u0000\u0000\u00a5\u00a6\u0006\u0016\u0000"+
		"\u0000\u00a6.\u0001\u0000\u0000\u0000\r\u0000hlqv|\u0081\u0086\u0088\u008e"+
		"\u0090\u009e\u00a3\u0001\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}