// Generated from /home/zineb/semester2/Projet_GL/src/main/antlr4/fr/ensimag/deca/syntax/DecaLexer.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DecaLexer extends AbstractDecaLexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EOL=1, ESPACE=2, TAB=3, RETOUR=4, ASM=5, CLASS=6, EXTENDS=7, ELSE=8, FALSE=9, 
		IF=10, INSTANCEOF=11, NEW=12, NULL=13, READINT=14, READFLOAT=15, PRINT=16, 
		PRINTLN=17, PRINTLNX=18, PRINTX=19, PROTECTED=20, RETURN=21, THIS=22, 
		TRUE=23, WHILE=24, IDENT=25, INT=26, EQUALS=27, PLUS=28, MINUS=29, TIMES=30, 
		SLASH=31, EXCLAM=32, OPARENT=33, CPARENT=34, OBRACE=35, CBRACE=36, SEMI=37, 
		DOT=38, COMMA=39, AND=40, OR=41, EQEQ=42, NEQ=43, LEQ=44, GEQ=45, LT=46, 
		GT=47, PERCENT=48, FLOAT=49, STRING=50, MULTI_LINE_STRING=51, COMMENT=52, 
		INCLUDE=53;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"EOL", "ESPACE", "TAB", "RETOUR", "ASM", "CLASS", "EXTENDS", "ELSE", 
			"FALSE", "IF", "INSTANCEOF", "NEW", "NULL", "READINT", "READFLOAT", "PRINT", 
			"PRINTLN", "PRINTLNX", "PRINTX", "PROTECTED", "RETURN", "THIS", "TRUE", 
			"WHILE", "LETTER", "DIGIT", "IDENT", "POSITIVE_DIGIT", "INT", "EQUALS", 
			"PLUS", "MINUS", "TIMES", "SLASH", "EXCLAM", "OPARENT", "CPARENT", "OBRACE", 
			"CBRACE", "SEMI", "DOT", "COMMA", "AND", "OR", "EQEQ", "NEQ", "LEQ", 
			"GEQ", "LT", "GT", "PERCENT", "NUM", "SIGN", "EXP", "DEC", "FLOATDEC", 
			"DIGITHEX", "NUMHEX", "FLOATHEX", "FLOAT", "STRING_CAR", "STRING", "MULTI_LINE_STRING", 
			"COMM", "COMMENT", "FILENAME", "INCLUDE"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'\n'", "' '", "'\t'", "'\r'", "'asm'", "'class'", "'extends'", 
			"'else'", "'false'", "'if'", "'instanceof'", "'new'", "'null'", "'readInt'", 
			"'readFloat'", "'print'", "'println'", "'printlnx'", "'printx'", "'protected'", 
			"'return'", "'this'", "'true'", "'while'", null, null, "'='", "'+'", 
			"'-'", "'*'", "'/'", "'!'", "'('", "')'", "'{'", "'}'", "';'", "'.'", 
			"','", "'&&'", "'||'", "'=='", "'!='", "'<='", "'>='", "'<'", "'>'", 
			"'%'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "EOL", "ESPACE", "TAB", "RETOUR", "ASM", "CLASS", "EXTENDS", "ELSE", 
			"FALSE", "IF", "INSTANCEOF", "NEW", "NULL", "READINT", "READFLOAT", "PRINT", 
			"PRINTLN", "PRINTLNX", "PRINTX", "PROTECTED", "RETURN", "THIS", "TRUE", 
			"WHILE", "IDENT", "INT", "EQUALS", "PLUS", "MINUS", "TIMES", "SLASH", 
			"EXCLAM", "OPARENT", "CPARENT", "OBRACE", "CBRACE", "SEMI", "DOT", "COMMA", 
			"AND", "OR", "EQEQ", "NEQ", "LEQ", "GEQ", "LT", "GT", "PERCENT", "FLOAT", 
			"STRING", "MULTI_LINE_STRING", "COMMENT", "INCLUDE"
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




	public DecaLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DecaLexer.g4"; }

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

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 0:
			EOL_action((RuleContext)_localctx, actionIndex);
			break;
		case 1:
			ESPACE_action((RuleContext)_localctx, actionIndex);
			break;
		case 2:
			TAB_action((RuleContext)_localctx, actionIndex);
			break;
		case 3:
			RETOUR_action((RuleContext)_localctx, actionIndex);
			break;
		case 64:
			COMMENT_action((RuleContext)_localctx, actionIndex);
			break;
		case 66:
			INCLUDE_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void EOL_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 skip(); 
			break;
		}
	}
	private void ESPACE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 1:
			 skip(); 
			break;
		}
	}
	private void TAB_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 2:
			skip();
			break;
		}
	}
	private void RETOUR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 3:
			skip();
			break;
		}
	}
	private void COMMENT_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 4:
			 skip(); 
			break;
		}
	}
	private void INCLUDE_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 5:
			doInclude(getText());
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\67\u01ed\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t"+
		"=\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\3\2\3\2\3\2\3\3\3\3\3\3\3"+
		"\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3"+
		"\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\32\3\32\3\33\3\33\3\34\3\34\5\34\u0121\n\34\3\34\3\34"+
		"\3\34\7\34\u0126\n\34\f\34\16\34\u0129\13\34\3\35\3\35\3\36\3\36\3\36"+
		"\7\36\u0130\n\36\f\36\16\36\u0133\13\36\5\36\u0135\n\36\3\37\3\37\3 \3"+
		" \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3"+
		"+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3/\3\60\3\60\3\60\3\61\3\61\3\61\3"+
		"\62\3\62\3\63\3\63\3\64\3\64\3\65\6\65\u016a\n\65\r\65\16\65\u016b\3\66"+
		"\3\66\3\67\3\67\3\67\3\67\38\38\38\38\39\39\39\39\59\u017c\n9\39\39\5"+
		"9\u0180\n9\3:\3:\3;\6;\u0185\n;\r;\16;\u0186\3<\3<\3<\3<\5<\u018d\n<\3"+
		"<\3<\3<\3<\3<\3<\3<\3<\5<\u0197\n<\3=\3=\5=\u019b\n=\3>\3>\3?\3?\3?\3"+
		"?\3?\3?\7?\u01a5\n?\f?\16?\u01a8\13?\3?\3?\3@\3@\3@\3@\3@\3@\3@\7@\u01b3"+
		"\n@\f@\16@\u01b6\13@\3@\3@\3A\3A\3A\3A\7A\u01be\nA\fA\16A\u01c1\13A\3"+
		"B\3B\3B\3B\3B\7B\u01c8\nB\fB\16B\u01cb\13B\3B\3B\5B\u01cf\nB\3B\3B\3C"+
		"\3C\3C\6C\u01d6\nC\rC\16C\u01d7\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\7D\u01e4"+
		"\nD\fD\16D\u01e7\13D\3D\3D\3D\3D\3D\3\u01c9\2E\3\3\5\4\7\5\t\6\13\7\r"+
		"\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25"+
		")\26+\27-\30/\31\61\32\63\2\65\2\67\339\2;\34=\35?\36A\37C E!G\"I#K$M"+
		"%O&Q\'S(U)W*Y+[,]-_.a/c\60e\61g\62i\2k\2m\2o\2q\2s\2u\2w\2y\63{\2}\64"+
		"\177\65\u0081\2\u0083\66\u0085\2\u0087\67\3\2\f\4\2C\\c|\4\2&&aa\4\2-"+
		"-//\4\2GGgg\4\2HHhh\5\2\62;CHch\4\2RRrr\5\2\f\f$$^^\3\2\f\f\4\2/\60aa"+
		"\2\u01f9\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2"+
		"\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27"+
		"\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2"+
		"\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2"+
		"\2/\3\2\2\2\2\61\3\2\2\2\2\67\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2"+
		"\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M"+
		"\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2"+
		"\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2"+
		"\2g\3\2\2\2\2y\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0083\3\2\2\2\2\u0087"+
		"\3\2\2\2\3\u0089\3\2\2\2\5\u008c\3\2\2\2\7\u008f\3\2\2\2\t\u0092\3\2\2"+
		"\2\13\u0095\3\2\2\2\r\u0099\3\2\2\2\17\u009f\3\2\2\2\21\u00a7\3\2\2\2"+
		"\23\u00ac\3\2\2\2\25\u00b2\3\2\2\2\27\u00b5\3\2\2\2\31\u00c0\3\2\2\2\33"+
		"\u00c4\3\2\2\2\35\u00c9\3\2\2\2\37\u00d1\3\2\2\2!\u00db\3\2\2\2#\u00e1"+
		"\3\2\2\2%\u00e9\3\2\2\2\'\u00f2\3\2\2\2)\u00f9\3\2\2\2+\u0103\3\2\2\2"+
		"-\u010a\3\2\2\2/\u010f\3\2\2\2\61\u0114\3\2\2\2\63\u011a\3\2\2\2\65\u011c"+
		"\3\2\2\2\67\u0120\3\2\2\29\u012a\3\2\2\2;\u0134\3\2\2\2=\u0136\3\2\2\2"+
		"?\u0138\3\2\2\2A\u013a\3\2\2\2C\u013c\3\2\2\2E\u013e\3\2\2\2G\u0140\3"+
		"\2\2\2I\u0142\3\2\2\2K\u0144\3\2\2\2M\u0146\3\2\2\2O\u0148\3\2\2\2Q\u014a"+
		"\3\2\2\2S\u014c\3\2\2\2U\u014e\3\2\2\2W\u0150\3\2\2\2Y\u0153\3\2\2\2["+
		"\u0156\3\2\2\2]\u0159\3\2\2\2_\u015c\3\2\2\2a\u015f\3\2\2\2c\u0162\3\2"+
		"\2\2e\u0164\3\2\2\2g\u0166\3\2\2\2i\u0169\3\2\2\2k\u016d\3\2\2\2m\u016f"+
		"\3\2\2\2o\u0173\3\2\2\2q\u017b\3\2\2\2s\u0181\3\2\2\2u\u0184\3\2\2\2w"+
		"\u018c\3\2\2\2y\u019a\3\2\2\2{\u019c\3\2\2\2}\u019e\3\2\2\2\177\u01ab"+
		"\3\2\2\2\u0081\u01b9\3\2\2\2\u0083\u01ce\3\2\2\2\u0085\u01d5\3\2\2\2\u0087"+
		"\u01d9\3\2\2\2\u0089\u008a\7\f\2\2\u008a\u008b\b\2\2\2\u008b\4\3\2\2\2"+
		"\u008c\u008d\7\"\2\2\u008d\u008e\b\3\3\2\u008e\6\3\2\2\2\u008f\u0090\7"+
		"\13\2\2\u0090\u0091\b\4\4\2\u0091\b\3\2\2\2\u0092\u0093\7\17\2\2\u0093"+
		"\u0094\b\5\5\2\u0094\n\3\2\2\2\u0095\u0096\7c\2\2\u0096\u0097\7u\2\2\u0097"+
		"\u0098\7o\2\2\u0098\f\3\2\2\2\u0099\u009a\7e\2\2\u009a\u009b\7n\2\2\u009b"+
		"\u009c\7c\2\2\u009c\u009d\7u\2\2\u009d\u009e\7u\2\2\u009e\16\3\2\2\2\u009f"+
		"\u00a0\7g\2\2\u00a0\u00a1\7z\2\2\u00a1\u00a2\7v\2\2\u00a2\u00a3\7g\2\2"+
		"\u00a3\u00a4\7p\2\2\u00a4\u00a5\7f\2\2\u00a5\u00a6\7u\2\2\u00a6\20\3\2"+
		"\2\2\u00a7\u00a8\7g\2\2\u00a8\u00a9\7n\2\2\u00a9\u00aa\7u\2\2\u00aa\u00ab"+
		"\7g\2\2\u00ab\22\3\2\2\2\u00ac\u00ad\7h\2\2\u00ad\u00ae\7c\2\2\u00ae\u00af"+
		"\7n\2\2\u00af\u00b0\7u\2\2\u00b0\u00b1\7g\2\2\u00b1\24\3\2\2\2\u00b2\u00b3"+
		"\7k\2\2\u00b3\u00b4\7h\2\2\u00b4\26\3\2\2\2\u00b5\u00b6\7k\2\2\u00b6\u00b7"+
		"\7p\2\2\u00b7\u00b8\7u\2\2\u00b8\u00b9\7v\2\2\u00b9\u00ba\7c\2\2\u00ba"+
		"\u00bb\7p\2\2\u00bb\u00bc\7e\2\2\u00bc\u00bd\7g\2\2\u00bd\u00be\7q\2\2"+
		"\u00be\u00bf\7h\2\2\u00bf\30\3\2\2\2\u00c0\u00c1\7p\2\2\u00c1\u00c2\7"+
		"g\2\2\u00c2\u00c3\7y\2\2\u00c3\32\3\2\2\2\u00c4\u00c5\7p\2\2\u00c5\u00c6"+
		"\7w\2\2\u00c6\u00c7\7n\2\2\u00c7\u00c8\7n\2\2\u00c8\34\3\2\2\2\u00c9\u00ca"+
		"\7t\2\2\u00ca\u00cb\7g\2\2\u00cb\u00cc\7c\2\2\u00cc\u00cd\7f\2\2\u00cd"+
		"\u00ce\7K\2\2\u00ce\u00cf\7p\2\2\u00cf\u00d0\7v\2\2\u00d0\36\3\2\2\2\u00d1"+
		"\u00d2\7t\2\2\u00d2\u00d3\7g\2\2\u00d3\u00d4\7c\2\2\u00d4\u00d5\7f\2\2"+
		"\u00d5\u00d6\7H\2\2\u00d6\u00d7\7n\2\2\u00d7\u00d8\7q\2\2\u00d8\u00d9"+
		"\7c\2\2\u00d9\u00da\7v\2\2\u00da \3\2\2\2\u00db\u00dc\7r\2\2\u00dc\u00dd"+
		"\7t\2\2\u00dd\u00de\7k\2\2\u00de\u00df\7p\2\2\u00df\u00e0\7v\2\2\u00e0"+
		"\"\3\2\2\2\u00e1\u00e2\7r\2\2\u00e2\u00e3\7t\2\2\u00e3\u00e4\7k\2\2\u00e4"+
		"\u00e5\7p\2\2\u00e5\u00e6\7v\2\2\u00e6\u00e7\7n\2\2\u00e7\u00e8\7p\2\2"+
		"\u00e8$\3\2\2\2\u00e9\u00ea\7r\2\2\u00ea\u00eb\7t\2\2\u00eb\u00ec\7k\2"+
		"\2\u00ec\u00ed\7p\2\2\u00ed\u00ee\7v\2\2\u00ee\u00ef\7n\2\2\u00ef\u00f0"+
		"\7p\2\2\u00f0\u00f1\7z\2\2\u00f1&\3\2\2\2\u00f2\u00f3\7r\2\2\u00f3\u00f4"+
		"\7t\2\2\u00f4\u00f5\7k\2\2\u00f5\u00f6\7p\2\2\u00f6\u00f7\7v\2\2\u00f7"+
		"\u00f8\7z\2\2\u00f8(\3\2\2\2\u00f9\u00fa\7r\2\2\u00fa\u00fb\7t\2\2\u00fb"+
		"\u00fc\7q\2\2\u00fc\u00fd\7v\2\2\u00fd\u00fe\7g\2\2\u00fe\u00ff\7e\2\2"+
		"\u00ff\u0100\7v\2\2\u0100\u0101\7g\2\2\u0101\u0102\7f\2\2\u0102*\3\2\2"+
		"\2\u0103\u0104\7t\2\2\u0104\u0105\7g\2\2\u0105\u0106\7v\2\2\u0106\u0107"+
		"\7w\2\2\u0107\u0108\7t\2\2\u0108\u0109\7p\2\2\u0109,\3\2\2\2\u010a\u010b"+
		"\7v\2\2\u010b\u010c\7j\2\2\u010c\u010d\7k\2\2\u010d\u010e\7u\2\2\u010e"+
		".\3\2\2\2\u010f\u0110\7v\2\2\u0110\u0111\7t\2\2\u0111\u0112\7w\2\2\u0112"+
		"\u0113\7g\2\2\u0113\60\3\2\2\2\u0114\u0115\7y\2\2\u0115\u0116\7j\2\2\u0116"+
		"\u0117\7k\2\2\u0117\u0118\7n\2\2\u0118\u0119\7g\2\2\u0119\62\3\2\2\2\u011a"+
		"\u011b\t\2\2\2\u011b\64\3\2\2\2\u011c\u011d\4\62;\2\u011d\66\3\2\2\2\u011e"+
		"\u0121\5\63\32\2\u011f\u0121\t\3\2\2\u0120\u011e\3\2\2\2\u0120\u011f\3"+
		"\2\2\2\u0121\u0127\3\2\2\2\u0122\u0126\5\63\32\2\u0123\u0126\5\65\33\2"+
		"\u0124\u0126\t\3\2\2\u0125\u0122\3\2\2\2\u0125\u0123\3\2\2\2\u0125\u0124"+
		"\3\2\2\2\u0126\u0129\3\2\2\2\u0127\u0125\3\2\2\2\u0127\u0128\3\2\2\2\u0128"+
		"8\3\2\2\2\u0129\u0127\3\2\2\2\u012a\u012b\4\63;\2\u012b:\3\2\2\2\u012c"+
		"\u0135\7\62\2\2\u012d\u0131\59\35\2\u012e\u0130\5\65\33\2\u012f\u012e"+
		"\3\2\2\2\u0130\u0133\3\2\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132"+
		"\u0135\3\2\2\2\u0133\u0131\3\2\2\2\u0134\u012c\3\2\2\2\u0134\u012d\3\2"+
		"\2\2\u0135<\3\2\2\2\u0136\u0137\7?\2\2\u0137>\3\2\2\2\u0138\u0139\7-\2"+
		"\2\u0139@\3\2\2\2\u013a\u013b\7/\2\2\u013bB\3\2\2\2\u013c\u013d\7,\2\2"+
		"\u013dD\3\2\2\2\u013e\u013f\7\61\2\2\u013fF\3\2\2\2\u0140\u0141\7#\2\2"+
		"\u0141H\3\2\2\2\u0142\u0143\7*\2\2\u0143J\3\2\2\2\u0144\u0145\7+\2\2\u0145"+
		"L\3\2\2\2\u0146\u0147\7}\2\2\u0147N\3\2\2\2\u0148\u0149\7\177\2\2\u0149"+
		"P\3\2\2\2\u014a\u014b\7=\2\2\u014bR\3\2\2\2\u014c\u014d\7\60\2\2\u014d"+
		"T\3\2\2\2\u014e\u014f\7.\2\2\u014fV\3\2\2\2\u0150\u0151\7(\2\2\u0151\u0152"+
		"\7(\2\2\u0152X\3\2\2\2\u0153\u0154\7~\2\2\u0154\u0155\7~\2\2\u0155Z\3"+
		"\2\2\2\u0156\u0157\7?\2\2\u0157\u0158\7?\2\2\u0158\\\3\2\2\2\u0159\u015a"+
		"\7#\2\2\u015a\u015b\7?\2\2\u015b^\3\2\2\2\u015c\u015d\7>\2\2\u015d\u015e"+
		"\7?\2\2\u015e`\3\2\2\2\u015f\u0160\7@\2\2\u0160\u0161\7?\2\2\u0161b\3"+
		"\2\2\2\u0162\u0163\7>\2\2\u0163d\3\2\2\2\u0164\u0165\7@\2\2\u0165f\3\2"+
		"\2\2\u0166\u0167\7\'\2\2\u0167h\3\2\2\2\u0168\u016a\5\65\33\2\u0169\u0168"+
		"\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u0169\3\2\2\2\u016b\u016c\3\2\2\2\u016c"+
		"j\3\2\2\2\u016d\u016e\t\4\2\2\u016el\3\2\2\2\u016f\u0170\t\5\2\2\u0170"+
		"\u0171\5k\66\2\u0171\u0172\5i\65\2\u0172n\3\2\2\2\u0173\u0174\5i\65\2"+
		"\u0174\u0175\7\60\2\2\u0175\u0176\5i\65\2\u0176p\3\2\2\2\u0177\u017c\5"+
		"o8\2\u0178\u0179\5o8\2\u0179\u017a\5m\67\2\u017a\u017c\3\2\2\2\u017b\u0177"+
		"\3\2\2\2\u017b\u0178\3\2\2\2\u017c\u017f\3\2\2\2\u017d\u0180\t\6\2\2\u017e"+
		"\u0180\3\2\2\2\u017f\u017d\3\2\2\2\u017f\u017e\3\2\2\2\u0180r\3\2\2\2"+
		"\u0181\u0182\t\7\2\2\u0182t\3\2\2\2\u0183\u0185\5s:\2\u0184\u0183\3\2"+
		"\2\2\u0185\u0186\3\2\2\2\u0186\u0184\3\2\2\2\u0186\u0187\3\2\2\2\u0187"+
		"v\3\2\2\2\u0188\u0189\7\62\2\2\u0189\u018d\7z\2\2\u018a\u018b\7\62\2\2"+
		"\u018b\u018d\7Z\2\2\u018c\u0188\3\2\2\2\u018c\u018a\3\2\2\2\u018d\u018e"+
		"\3\2\2\2\u018e\u018f\5u;\2\u018f\u0190\7\60\2\2\u0190\u0191\5u;\2\u0191"+
		"\u0192\t\b\2\2\u0192\u0193\5k\66\2\u0193\u0196\5i\65\2\u0194\u0197\t\6"+
		"\2\2\u0195\u0197\3\2\2\2\u0196\u0194\3\2\2\2\u0196\u0195\3\2\2\2\u0197"+
		"x\3\2\2\2\u0198\u019b\5q9\2\u0199\u019b\5w<\2\u019a\u0198\3\2\2\2\u019a"+
		"\u0199\3\2\2\2\u019bz\3\2\2\2\u019c\u019d\n\t\2\2\u019d|\3\2\2\2\u019e"+
		"\u01a6\7$\2\2\u019f\u01a5\5{>\2\u01a0\u01a1\7^\2\2\u01a1\u01a5\7$\2\2"+
		"\u01a2\u01a3\7^\2\2\u01a3\u01a5\7^\2\2\u01a4\u019f\3\2\2\2\u01a4\u01a0"+
		"\3\2\2\2\u01a4\u01a2\3\2\2\2\u01a5\u01a8\3\2\2\2\u01a6\u01a4\3\2\2\2\u01a6"+
		"\u01a7\3\2\2\2\u01a7\u01a9\3\2\2\2\u01a8\u01a6\3\2\2\2\u01a9\u01aa\7$"+
		"\2\2\u01aa~\3\2\2\2\u01ab\u01b4\7$\2\2\u01ac\u01b3\5{>\2\u01ad\u01b3\5"+
		"\3\2\2\u01ae\u01af\7^\2\2\u01af\u01b3\7$\2\2\u01b0\u01b1\7^\2\2\u01b1"+
		"\u01b3\7^\2\2\u01b2\u01ac\3\2\2\2\u01b2\u01ad\3\2\2\2\u01b2\u01ae\3\2"+
		"\2\2\u01b2\u01b0\3\2\2\2\u01b3\u01b6\3\2\2\2\u01b4\u01b2\3\2\2\2\u01b4"+
		"\u01b5\3\2\2\2\u01b5\u01b7\3\2\2\2\u01b6\u01b4\3\2\2\2\u01b7\u01b8\7$"+
		"\2\2\u01b8\u0080\3\2\2\2\u01b9\u01ba\7\61\2\2\u01ba\u01bb\7\61\2\2\u01bb"+
		"\u01bf\3\2\2\2\u01bc\u01be\n\n\2\2\u01bd\u01bc\3\2\2\2\u01be\u01c1\3\2"+
		"\2\2\u01bf\u01bd\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u0082\3\2\2\2\u01c1"+
		"\u01bf\3\2\2\2\u01c2\u01cf\5\u0081A\2\u01c3\u01c4\7\61\2\2\u01c4\u01c5"+
		"\7,\2\2\u01c5\u01c9\3\2\2\2\u01c6\u01c8\13\2\2\2\u01c7\u01c6\3\2\2\2\u01c8"+
		"\u01cb\3\2\2\2\u01c9\u01ca\3\2\2\2\u01c9\u01c7\3\2\2\2\u01ca\u01cc\3\2"+
		"\2\2\u01cb\u01c9\3\2\2\2\u01cc\u01cd\7,\2\2\u01cd\u01cf\7\61\2\2\u01ce"+
		"\u01c2\3\2\2\2\u01ce\u01c3\3\2\2\2\u01cf\u01d0\3\2\2\2\u01d0\u01d1\bB"+
		"\6\2\u01d1\u0084\3\2\2\2\u01d2\u01d6\5\63\32\2\u01d3\u01d6\5\65\33\2\u01d4"+
		"\u01d6\t\13\2\2\u01d5\u01d2\3\2\2\2\u01d5\u01d3\3\2\2\2\u01d5\u01d4\3"+
		"\2\2\2\u01d6\u01d7\3\2\2\2\u01d7\u01d5\3\2\2\2\u01d7\u01d8\3\2\2\2\u01d8"+
		"\u0086\3\2\2\2\u01d9\u01da\7%\2\2\u01da\u01db\7k\2\2\u01db\u01dc\7p\2"+
		"\2\u01dc\u01dd\7e\2\2\u01dd\u01de\7n\2\2\u01de\u01df\7w\2\2\u01df\u01e0"+
		"\7f\2\2\u01e0\u01e1\7g\2\2\u01e1\u01e5\3\2\2\2\u01e2\u01e4\7\"\2\2\u01e3"+
		"\u01e2\3\2\2\2\u01e4\u01e7\3\2\2\2\u01e5\u01e3\3\2\2\2\u01e5\u01e6\3\2"+
		"\2\2\u01e6\u01e8\3\2\2\2\u01e7\u01e5\3\2\2\2\u01e8\u01e9\7$\2\2\u01e9"+
		"\u01ea\5\u0085C\2\u01ea\u01eb\7$\2\2\u01eb\u01ec\bD\7\2\u01ec\u0088\3"+
		"\2\2\2\31\2\u0120\u0125\u0127\u0131\u0134\u016b\u017b\u017f\u0186\u018c"+
		"\u0196\u019a\u01a4\u01a6\u01b2\u01b4\u01bf\u01c9\u01ce\u01d5\u01d7\u01e5"+
		"\b\3\2\2\3\3\3\3\4\4\3\5\5\3B\6\3D\7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}