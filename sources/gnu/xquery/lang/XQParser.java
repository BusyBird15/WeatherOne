package gnu.xquery.lang;

import com.google.appinventor.components.common.PropertyTypeConstants;
import gnu.bytecode.Access;
import gnu.bytecode.ClassType;
import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.BeginExp;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.ErrorExp;
import gnu.expr.Expression;
import gnu.expr.IfExp;
import gnu.expr.LambdaExp;
import gnu.expr.PrimProcedure;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.ScopeExp;
import gnu.kawa.functions.Convert;
import gnu.kawa.lispexpr.LangPrimType;
import gnu.kawa.reflect.InstanceOf;
import gnu.kawa.reflect.OccurrenceType;
import gnu.kawa.reflect.SingletonType;
import gnu.kawa.xml.DescendantOrSelfAxis;
import gnu.kawa.xml.ElementType;
import gnu.kawa.xml.MakeAttribute;
import gnu.kawa.xml.MakeElement;
import gnu.kawa.xml.MakeWithBaseUri;
import gnu.kawa.xml.NodeType;
import gnu.kawa.xml.ParentAxis;
import gnu.kawa.xml.ProcessingInstructionType;
import gnu.kawa.xml.XDataType;
import gnu.mapping.CharArrayInPort;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.mapping.Namespace;
import gnu.mapping.Procedure;
import gnu.mapping.Symbol;
import gnu.mapping.TtyInPort;
import gnu.mapping.WrappedException;
import gnu.math.IntNum;
import gnu.text.FilePath;
import gnu.text.Lexer;
import gnu.text.LineBufferedReader;
import gnu.text.Path;
import gnu.text.SourceError;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import gnu.text.URIPath;
import gnu.xml.NamespaceBinding;
import gnu.xml.TextUtils;
import gnu.xml.XName;
import gnu.xquery.util.CastableAs;
import gnu.xquery.util.NamedCollator;
import gnu.xquery.util.QNameUtils;
import gnu.xquery.util.RelativeStep;
import gnu.xquery.util.ValuesFilter;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

public class XQParser extends Lexer {
    static final int ARROW_TOKEN = 82;
    static final int ATTRIBUTE_TOKEN = 252;
    static final int AXIS_ANCESTOR = 0;
    static final int AXIS_ANCESTOR_OR_SELF = 1;
    static final int AXIS_ATTRIBUTE = 2;
    static final int AXIS_CHILD = 3;
    static final int AXIS_DESCENDANT = 4;
    static final int AXIS_DESCENDANT_OR_SELF = 5;
    static final int AXIS_FOLLOWING = 6;
    static final int AXIS_FOLLOWING_SIBLING = 7;
    static final int AXIS_NAMESPACE = 8;
    static final int AXIS_PARENT = 9;
    static final int AXIS_PRECEDING = 10;
    static final int AXIS_PRECEDING_SIBLING = 11;
    static final int AXIS_SELF = 12;
    static final int CASE_DOLLAR_TOKEN = 247;
    static final int COLON_COLON_TOKEN = 88;
    static final int COLON_EQUAL_TOKEN = 76;
    static final int COMMENT_TOKEN = 254;
    static final int COUNT_OP_AXIS = 13;
    static final char DECIMAL_TOKEN = '1';
    static final int DECLARE_BASE_URI_TOKEN = 66;
    static final int DECLARE_BOUNDARY_SPACE_TOKEN = 83;
    static final int DECLARE_CONSTRUCTION_TOKEN = 75;
    static final int DECLARE_COPY_NAMESPACES_TOKEN = 76;
    static final int DECLARE_FUNCTION_TOKEN = 80;
    static final int DECLARE_NAMESPACE_TOKEN = 78;
    static final int DECLARE_OPTION_TOKEN = 111;
    static final int DECLARE_ORDERING_TOKEN = 85;
    static final int DECLARE_VARIABLE_TOKEN = 86;
    static final int DEFAULT_COLLATION_TOKEN = 71;
    static final int DEFAULT_ELEMENT_TOKEN = 69;
    static final int DEFAULT_FUNCTION_TOKEN = 79;
    static final int DEFAULT_ORDER_TOKEN = 72;
    static final int DEFINE_QNAME_TOKEN = 87;
    static final int DOCUMENT_TOKEN = 256;
    static final int DOTDOT_TOKEN = 51;
    static final Symbol DOT_VARNAME = Symbol.makeUninterned("$dot$");
    static final char DOUBLE_TOKEN = '2';
    static final int ELEMENT_TOKEN = 251;
    static final int EOF_TOKEN = -1;
    static final int EOL_TOKEN = 10;
    static final int EVERY_DOLLAR_TOKEN = 246;
    static final int FNAME_TOKEN = 70;
    static final int FOR_DOLLAR_TOKEN = 243;
    static final int IF_LPAREN_TOKEN = 241;
    static final int IMPORT_MODULE_TOKEN = 73;
    static final int IMPORT_SCHEMA_TOKEN = 84;
    static final char INTEGER_TOKEN = '0';
    static final Symbol LAST_VARNAME = Symbol.makeUninterned("$last$");
    static final int LET_DOLLAR_TOKEN = 244;
    static final int MODULE_NAMESPACE_TOKEN = 77;
    static final int NCNAME_COLON_TOKEN = 67;
    static final int NCNAME_TOKEN = 65;
    static final int OP_ADD = 413;
    static final int OP_AND = 401;
    static final int OP_ATTRIBUTE = 236;
    static final int OP_AXIS_FIRST = 100;
    static final int OP_BASE = 400;
    static final int OP_CASTABLE_AS = 424;
    static final int OP_CAST_AS = 425;
    static final int OP_COMMENT = 232;
    static final int OP_DIV = 416;
    static final int OP_DOCUMENT = 234;
    static final int OP_ELEMENT = 235;
    static final int OP_EMPTY_SEQUENCE = 238;
    static final int OP_EQ = 426;
    static final int OP_EQU = 402;
    static final int OP_EXCEPT = 421;
    static final int OP_GE = 431;
    static final int OP_GEQ = 407;
    static final int OP_GRT = 405;
    static final int OP_GRTGRT = 410;
    static final int OP_GT = 430;
    static final int OP_IDIV = 417;
    static final int OP_INSTANCEOF = 422;
    static final int OP_INTERSECT = 420;
    static final int OP_IS = 408;
    static final int OP_ISNOT = 409;
    static final int OP_ITEM = 237;
    static final int OP_LE = 429;
    static final int OP_LEQ = 406;
    static final int OP_LSS = 404;
    static final int OP_LSSLSS = 411;
    static final int OP_LT = 428;
    static final int OP_MOD = 418;
    static final int OP_MUL = 415;
    static final int OP_NE = 427;
    static final int OP_NEQ = 403;
    static final int OP_NODE = 230;
    static final int OP_OR = 400;
    static final int OP_PI = 233;
    static final int OP_RANGE_TO = 412;
    static final int OP_SCHEMA_ATTRIBUTE = 239;
    static final int OP_SCHEMA_ELEMENT = 240;
    static final int OP_SUB = 414;
    static final int OP_TEXT = 231;
    static final int OP_TREAT_AS = 423;
    static final int OP_UNION = 419;
    static final int OP_WHERE = 196;
    static final int ORDERED_LBRACE_TOKEN = 249;
    static final int PI_TOKEN = 255;
    static final Symbol POSITION_VARNAME = Symbol.makeUninterned("$position$");
    static final int PRAGMA_START_TOKEN = 197;
    static final int QNAME_TOKEN = 81;
    static final int SLASHSLASH_TOKEN = 68;
    static final int SOME_DOLLAR_TOKEN = 245;
    static final int STRING_TOKEN = 34;
    static final int TEXT_TOKEN = 253;
    static final int TYPESWITCH_LPAREN_TOKEN = 242;
    static final int UNORDERED_LBRACE_TOKEN = 250;
    static final int VALIDATE_LBRACE_TOKEN = 248;
    static final int XQUERY_VERSION_TOKEN = 89;
    public static final String[] axisNames = new String[13];
    static NamespaceBinding builtinNamespaces = new NamespaceBinding("local", XQuery.LOCAL_NAMESPACE, new NamespaceBinding("qexo", XQuery.QEXO_FUNCTION_NAMESPACE, new NamespaceBinding("kawa", XQuery.KAWA_FUNCTION_NAMESPACE, new NamespaceBinding("html", "http://www.w3.org/1999/xhtml", new NamespaceBinding("fn", XQuery.XQUERY_FUNCTION_NAMESPACE, new NamespaceBinding("xsi", XQuery.SCHEMA_INSTANCE_NAMESPACE, new NamespaceBinding("xs", XQuery.SCHEMA_NAMESPACE, new NamespaceBinding("xml", NamespaceBinding.XML_NAMESPACE, NamespaceBinding.predefinedXML))))))));
    public static final CastableAs castableAs = CastableAs.castableAs;
    public static final QuoteExp getExternalFunction = QuoteExp.getInstance(new PrimProcedure("gnu.xquery.lang.XQuery", "getExternal", 2));
    public static final InstanceOf instanceOf = new InstanceOf(XQuery.getInstance(), "instance");
    static final Expression makeCDATA = makeFunctionExp("gnu.kawa.xml.MakeCDATA", "makeCDATA");
    public static QuoteExp makeChildAxisStep = QuoteExp.getInstance(new PrimProcedure("gnu.kawa.xml.ChildAxis", "make", 1));
    public static QuoteExp makeDescendantAxisStep = QuoteExp.getInstance(new PrimProcedure("gnu.kawa.xml.DescendantAxis", "make", 1));
    public static Expression makeText = makeFunctionExp("gnu.kawa.xml.MakeText", "makeText");
    static PrimProcedure proc_OccurrenceType_getInstance = new PrimProcedure(ClassType.make("gnu.kawa.reflect.OccurrenceType").getDeclaredMethod("getInstance", 3));
    public static final Convert treatAs = Convert.as;
    public static boolean warnHidePreviousDeclaration = false;
    public static boolean warnOldVersion = true;
    Path baseURI = null;
    boolean baseURIDeclarationSeen;
    boolean boundarySpaceDeclarationSeen;
    boolean boundarySpacePreserve;
    int commentCount;
    Compilation comp;
    boolean constructionModeDeclarationSeen;
    boolean constructionModeStrip;
    NamespaceBinding constructorNamespaces = NamespaceBinding.predefinedXML;
    boolean copyNamespacesDeclarationSeen;
    int copyNamespacesMode = 3;
    int curColumn;
    int curLine;
    int curToken;
    Object curValue;
    NamedCollator defaultCollator = null;
    String defaultElementNamespace = "";
    char defaultEmptyOrder = 'L';
    boolean emptyOrderDeclarationSeen;
    int enclosedExpressionsSeen;
    String errorIfComment;
    Declaration[] flworDecls;
    int flworDeclsCount;
    int flworDeclsFirst;
    public Namespace[] functionNamespacePath = XQuery.defaultFunctionNamespacePath;
    XQuery interpreter;
    String libraryModuleNamespace;
    boolean orderingModeSeen;
    boolean orderingModeUnordered;
    int parseContext;
    int parseCount;
    NamespaceBinding prologNamespaces;
    private int saveToken;
    private Object saveValue;
    boolean seenDeclaration;
    int seenLast;
    int seenPosition;
    private boolean warnedOldStyleKindTest;

    static {
        axisNames[0] = "ancestor";
        axisNames[1] = "ancestor-or-self";
        axisNames[2] = "attribute";
        axisNames[3] = "child";
        axisNames[4] = "descendant";
        axisNames[5] = "descendant-or-self";
        axisNames[6] = "following";
        axisNames[7] = "following-sibling";
        axisNames[8] = "namespace";
        axisNames[9] = "parent";
        axisNames[10] = "preceding";
        axisNames[11] = "preceding-sibling";
        axisNames[12] = "self";
    }

    public void setStaticBaseUri(String uri) {
        try {
            this.baseURI = fixupStaticBaseUri(URIPath.valueOf(uri));
        } catch (Throwable th) {
            ex = th;
            if (ex instanceof WrappedException) {
                ex = ((WrappedException) ex).getCause();
            }
            error('e', "invalid URI: " + ex.getMessage());
        }
    }

    static Path fixupStaticBaseUri(Path path) {
        Path path2 = path.getAbsolute();
        if (path2 instanceof FilePath) {
            return URIPath.valueOf(path2.toURI());
        }
        return path2;
    }

    public String getStaticBaseUri() {
        LineBufferedReader port;
        Path path = this.baseURI;
        if (path == null) {
            Object value = Environment.getCurrent().get(Symbol.make("", "base-uri"), (Object) null, (Object) null);
            if (value != null && !(value instanceof Path)) {
                path = URIPath.valueOf(value.toString());
            }
            if (path == null && (port = getPort()) != null) {
                path = port.getPath();
                if ((path instanceof FilePath) && (!path.exists() || (port instanceof TtyInPort) || (port instanceof CharArrayInPort))) {
                    path = null;
                }
            }
            if (path == null) {
                path = Path.currentPath();
            }
            path = fixupStaticBaseUri(path);
            this.baseURI = path;
        }
        return path.toString();
    }

    public String resolveAgainstBaseUri(String uri) {
        return Path.uriSchemeSpecified(uri) ? uri : Path.valueOf(getStaticBaseUri()).resolve(uri).toString();
    }

    /* access modifiers changed from: package-private */
    public final int skipSpace() throws IOException, SyntaxException {
        return skipSpace(true);
    }

    /* access modifiers changed from: package-private */
    public final int skipSpace(boolean verticalToo) throws IOException, SyntaxException {
        int ch;
        while (true) {
            ch = read();
            if (ch != 40) {
                if (ch != 123) {
                    if (!verticalToo) {
                        if (!(ch == 32 || ch == 9)) {
                            break;
                        }
                    } else if (ch < 0 || !Character.isWhitespace((char) ch)) {
                        break;
                    }
                } else {
                    int ch2 = read();
                    if (ch2 != 45) {
                        unread(ch2);
                        return 123;
                    }
                    int ch3 = read();
                    if (ch3 != 45) {
                        unread(ch3);
                        unread(45);
                        return 123;
                    }
                    skipOldComment();
                }
            } else if (!checkNext(':')) {
                return 40;
            } else {
                skipComment();
            }
        }
        return ch;
    }

    /*  JADX ERROR: StackOverflow in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: 
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:47)
        	at jadx.core.utils.ErrorsCounter.methodError(ErrorsCounter.java:81)
        */
    final void skipToSemicolon() throws java.io.IOException {
        /*
            r2 = this;
        L_0x0000:
            int r0 = r2.read()
            if (r0 < 0) goto L_0x000a
            r1 = 59
            if (r0 != r1) goto L_0x0000
        L_0x000a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.lang.XQParser.skipToSemicolon():void");
    }

    /* access modifiers changed from: package-private */
    public final void skipOldComment() throws IOException, SyntaxException {
        int seenDashes = 0;
        int startLine = getLineNumber() + 1;
        int startColumn = getColumnNumber() - 2;
        warnOldVersion("use (: :) instead of old-style comment {-- --}");
        while (true) {
            int ch = read();
            if (ch == 45) {
                seenDashes++;
            } else if (ch == 125 && seenDashes >= 2) {
                return;
            } else {
                if (ch < 0) {
                    this.curLine = startLine;
                    this.curColumn = startColumn;
                    eofError("non-terminated comment starting here");
                } else {
                    seenDashes = 0;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void skipComment() throws IOException, SyntaxException {
        this.commentCount++;
        int startLine = getLineNumber() + 1;
        int startColumn = getColumnNumber() - 1;
        if (this.errorIfComment != null) {
            this.curLine = startLine;
            this.curColumn = startColumn;
            error('e', this.errorIfComment);
        }
        int prev = 0;
        int commentNesting = 0;
        char saveReadState = pushNesting(':');
        while (true) {
            int ch = read();
            if (ch == 58) {
                if (prev == 40) {
                    commentNesting++;
                    ch = 0;
                }
            } else if (ch == 41 && prev == 58) {
                if (commentNesting == 0) {
                    popNesting(saveReadState);
                    return;
                }
                commentNesting--;
            } else if (ch < 0) {
                this.curLine = startLine;
                this.curColumn = startColumn;
                eofError("non-terminated comment starting here");
            }
            prev = ch;
        }
    }

    /* access modifiers changed from: package-private */
    public final int peekNonSpace(String message) throws IOException, SyntaxException {
        int ch = skipSpace();
        if (ch < 0) {
            eofError(message);
        }
        unread(ch);
        return ch;
    }

    public void mark() throws IOException {
        super.mark();
        this.saveToken = this.curToken;
        this.saveValue = this.curValue;
    }

    public void reset() throws IOException {
        this.curToken = this.saveToken;
        this.curValue = this.saveValue;
        super.reset();
    }

    private int setToken(int token, int width) {
        this.curToken = token;
        this.curLine = this.port.getLineNumber() + 1;
        this.curColumn = (this.port.getColumnNumber() + 1) - width;
        return token;
    }

    /* access modifiers changed from: package-private */
    public void checkSeparator(char ch) {
        if (XName.isNameStart(ch)) {
            error('e', "missing separator", "XPST0003");
        }
    }

    /* access modifiers changed from: package-private */
    public int getRawToken() throws IOException, SyntaxException {
        int next;
        int next2;
        boolean seenDot = true;
        while (true) {
            int next3 = readUnicodeChar();
            if (next3 < 0) {
                return setToken(-1, 0);
            }
            if (next3 == 10 || next3 == 13) {
                if (this.nesting <= 0) {
                    return setToken(10, 0);
                }
            } else if (next3 == 40) {
                if (checkNext(':')) {
                    skipComment();
                } else if (checkNext('#')) {
                    return setToken(PRAGMA_START_TOKEN, 2);
                } else {
                    return setToken(40, 1);
                }
            } else if (next3 == 123) {
                if (!checkNext('-')) {
                    return setToken(123, 1);
                }
                if (read() != 45) {
                    unread();
                    unread();
                    return setToken(123, 1);
                }
                skipOldComment();
            } else if (!(next3 == 32 || next3 == 9)) {
                this.tokenBufferLength = 0;
                this.curLine = this.port.getLineNumber() + 1;
                this.curColumn = this.port.getColumnNumber();
                char ch = (char) next3;
                switch (ch) {
                    case '!':
                        if (checkNext('=')) {
                            ch = 403;
                            break;
                        }
                        break;
                    case '\"':
                    case '\'':
                        char saveReadState = pushNesting((char) next3);
                        while (true) {
                            int next4 = readUnicodeChar();
                            if (next4 < 0) {
                                eofError("unexpected end-of-file in string starting here");
                            }
                            if (next4 == 38) {
                                parseEntityOrCharRef();
                            } else {
                                if (ch == next4) {
                                    if (ch != peek()) {
                                        popNesting(saveReadState);
                                        ch = '\"';
                                        break;
                                    } else {
                                        next4 = read();
                                    }
                                }
                                tokenBufferAppend(next4);
                            }
                        }
                    case '$':
                    case ')':
                    case ',':
                    case ';':
                    case '?':
                    case '@':
                    case '[':
                    case ']':
                    case '}':
                        break;
                    case '*':
                        ch = 415;
                        break;
                    case '+':
                        ch = 413;
                        break;
                    case '-':
                        ch = 414;
                        break;
                    case '/':
                        if (checkNext('/')) {
                            ch = 'D';
                            break;
                        }
                        break;
                    case ':':
                        if (!checkNext('=')) {
                            if (checkNext(':')) {
                                ch = 'X';
                                break;
                            }
                        } else {
                            ch = 'L';
                            break;
                        }
                        break;
                    case '<':
                        if (!checkNext('=')) {
                            if (!checkNext('<')) {
                                ch = 404;
                                break;
                            } else {
                                ch = 411;
                                break;
                            }
                        } else {
                            ch = 406;
                            break;
                        }
                    case '=':
                        if (checkNext('>')) {
                        }
                        ch = 402;
                        break;
                    case '>':
                        if (!checkNext('=')) {
                            if (!checkNext('>')) {
                                ch = 405;
                                break;
                            } else {
                                ch = 410;
                                break;
                            }
                        } else {
                            ch = 407;
                            break;
                        }
                    case '|':
                        ch = 419;
                        break;
                    default:
                        if (!Character.isDigit(ch) && (ch != '.' || !Character.isDigit((char) peek()))) {
                            if (ch != '.') {
                                if (!XName.isNameStart(ch)) {
                                    if (ch >= ' ' && ch < 127) {
                                        syntaxError("invalid character '" + ch + '\'');
                                        break;
                                    } else {
                                        syntaxError("invalid character '\\u" + Integer.toHexString(ch) + '\'');
                                        break;
                                    }
                                } else {
                                    do {
                                        tokenBufferAppend(ch);
                                        next2 = read();
                                        ch = (char) next2;
                                    } while (XName.isNamePart(ch));
                                    if (next2 >= 0) {
                                        if (next2 != 58) {
                                            ch = 'A';
                                        } else {
                                            next2 = read();
                                            if (next2 < 0) {
                                                eofError("unexpected end-of-file after NAME ':'");
                                            }
                                            char ch2 = (char) next2;
                                            if (XName.isNameStart(ch2)) {
                                                tokenBufferAppend(58);
                                                do {
                                                    tokenBufferAppend(ch2);
                                                    next2 = read();
                                                    ch2 = (char) next2;
                                                } while (XName.isNamePart(ch2));
                                                ch = 'Q';
                                            } else if (ch2 == '=') {
                                                unread(ch2);
                                                ch = 'A';
                                            } else {
                                                ch = Access.CLASS_CONTEXT;
                                            }
                                        }
                                        unread(next2);
                                        break;
                                    } else {
                                        ch = 'A';
                                        break;
                                    }
                                }
                            } else if (checkNext('.')) {
                                ch = '3';
                                break;
                            }
                        } else {
                            if (ch != '.') {
                                seenDot = false;
                            }
                            while (true) {
                                tokenBufferAppend(ch);
                                next = read();
                                if (next >= 0) {
                                    ch = (char) next;
                                    if (ch == '.') {
                                        if (!seenDot) {
                                            seenDot = true;
                                        }
                                    } else if (!Character.isDigit(ch)) {
                                    }
                                }
                            }
                            if (next != 101 && next != 69) {
                                ch = seenDot ? DECIMAL_TOKEN : INTEGER_TOKEN;
                                if (next >= 0) {
                                    checkSeparator((char) next);
                                    unread(next);
                                    break;
                                }
                            } else {
                                tokenBufferAppend((char) next);
                                int next5 = read();
                                if (next5 == 43 || next5 == 45) {
                                    tokenBufferAppend(next5);
                                    next5 = read();
                                }
                                int expDigits = 0;
                                while (true) {
                                    if (next5 >= 0) {
                                        char ch3 = (char) next5;
                                        if (!Character.isDigit(ch3)) {
                                            checkSeparator(ch3);
                                            unread();
                                        } else {
                                            tokenBufferAppend(ch3);
                                            next5 = read();
                                            expDigits++;
                                        }
                                    }
                                }
                                if (expDigits == 0) {
                                    error('e', "no digits following exponent", "XPST0003");
                                }
                                ch = DOUBLE_TOKEN;
                                break;
                            }
                        }
                        break;
                }
                this.curToken = ch;
                return ch;
            }
        }
    }

    public void getDelimited(String delimiter) throws IOException, SyntaxException {
        if (!readDelimited(delimiter)) {
            eofError("unexpected end-of-file looking for '" + delimiter + '\'');
        }
    }

    public void appendNamedEntity(String name) {
        String name2 = name.intern();
        char ch = '?';
        if (name2 == "lt") {
            ch = '<';
        } else if (name2 == "gt") {
            ch = '>';
        } else if (name2 == "amp") {
            ch = '&';
        } else if (name2 == "quot") {
            ch = '\"';
        } else if (name2 == "apos") {
            ch = '\'';
        } else {
            error("unknown enity reference: '" + name2 + "'");
        }
        tokenBufferAppend(ch);
    }

    /* access modifiers changed from: package-private */
    public boolean match(String word1, String word2, boolean force) throws IOException, SyntaxException {
        if (match(word1)) {
            mark();
            getRawToken();
            if (match(word2)) {
                reset();
                getRawToken();
                return true;
            }
            reset();
            if (force) {
                error('e', "'" + word1 + "' must be followed by '" + word2 + "'", "XPST0003");
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public int peekOperator() throws IOException, SyntaxException {
        while (this.curToken == 10) {
            if (this.nesting == 0) {
                return 10;
            }
            getRawToken();
        }
        if (this.curToken == 65) {
            switch (this.tokenBufferLength) {
                case 2:
                    char c1 = this.tokenBuffer[0];
                    char c2 = this.tokenBuffer[1];
                    if (c1 != 'o' || c2 != 'r') {
                        if (c1 != 't' || c2 != 'o') {
                            if (c1 != 'i' || c2 != 's') {
                                if (c1 != 'e' || c2 != 'q') {
                                    if (c1 != 'n' || c2 != 'e') {
                                        if (c1 != 'g') {
                                            if (c1 == 'l') {
                                                if (c2 != 'e') {
                                                    if (c2 == 't') {
                                                        this.curToken = OP_LT;
                                                        break;
                                                    }
                                                } else {
                                                    this.curToken = OP_LE;
                                                    break;
                                                }
                                            }
                                        } else if (c2 != 'e') {
                                            if (c2 == 't') {
                                                this.curToken = OP_GT;
                                                break;
                                            }
                                        } else {
                                            this.curToken = OP_GE;
                                            break;
                                        }
                                    } else {
                                        this.curToken = OP_NE;
                                        break;
                                    }
                                } else {
                                    this.curToken = OP_EQ;
                                    break;
                                }
                            } else {
                                this.curToken = 408;
                                break;
                            }
                        } else {
                            this.curToken = 412;
                            break;
                        }
                    } else {
                        this.curToken = 400;
                        break;
                    }
                    break;
                case 3:
                    char c12 = this.tokenBuffer[0];
                    char c22 = this.tokenBuffer[1];
                    char c3 = this.tokenBuffer[2];
                    if (c12 != 'a') {
                        if (c12 != 'm') {
                            if (c12 == 'd' && c22 == 'i' && c3 == 'v') {
                                this.curToken = 416;
                                break;
                            }
                        } else {
                            if (c22 == 'u' && c3 == 'l') {
                                this.curToken = 415;
                            }
                            if (c22 == 'o' && c3 == 'd') {
                                this.curToken = 418;
                                break;
                            }
                        }
                    } else if (c22 == 'n' && c3 == 'd') {
                        this.curToken = 401;
                        break;
                    }
                case 4:
                    if (!match("idiv")) {
                        if (match("cast", "as", true)) {
                            this.curToken = OP_CAST_AS;
                            break;
                        }
                    } else {
                        this.curToken = 417;
                        break;
                    }
                    break;
                case 5:
                    if (!match("where")) {
                        if (!match("isnot")) {
                            if (!match("union")) {
                                if (match("treat", "as", true)) {
                                    this.curToken = 423;
                                    break;
                                }
                            } else {
                                this.curToken = 419;
                                break;
                            }
                        } else {
                            this.curToken = 409;
                            break;
                        }
                    } else {
                        this.curToken = 196;
                        break;
                    }
                    break;
                case 6:
                    if (match("except")) {
                        this.curToken = 421;
                        break;
                    }
                    break;
                case 8:
                    if (!match("instance", "of", true)) {
                        if (match("castable", "as", true)) {
                            this.curToken = OP_CASTABLE_AS;
                            break;
                        }
                    } else {
                        this.curToken = 422;
                        break;
                    }
                    break;
                case 9:
                    if (match("intersect")) {
                        this.curToken = 420;
                        break;
                    }
                    break;
                case 10:
                    if (match("instanceof")) {
                        warnOldVersion("use 'instanceof of' (two words) instead of 'instanceof'");
                        this.curToken = 422;
                        break;
                    }
                    break;
            }
        }
        return this.curToken;
    }

    private boolean lookingAt(String word0, String word1) throws IOException, SyntaxException {
        if (!word0.equals(this.curValue)) {
            return false;
        }
        int i = 0;
        int len = word1.length();
        while (true) {
            int ch = read();
            if (i != len) {
                if (ch < 0) {
                    break;
                }
                int i2 = i + 1;
                if (ch != word1.charAt(i)) {
                    i = i2;
                    break;
                }
                i = i2;
            } else if (ch < 0) {
                return true;
            } else {
                if (!XName.isNamePart((char) ch)) {
                    unread();
                    return true;
                }
                i++;
            }
        }
        this.port.skip(-i);
        return false;
    }

    /* access modifiers changed from: package-private */
    public int getAxis() {
        String name = new String(this.tokenBuffer, 0, this.tokenBufferLength).intern();
        int i = 13;
        do {
            i--;
            if (i < 0 || axisNames[i] == name) {
                if (i < 0 || i == 8) {
                    error('e', "unknown axis name '" + name + '\'', "XPST0003");
                    i = 3;
                }
            }
            i--;
            break;
        } while (axisNames[i] == name);
        error('e', "unknown axis name '" + name + '\'', "XPST0003");
        i = 3;
        return (char) (i + 100);
    }

    /* access modifiers changed from: package-private */
    public int peekOperand() throws IOException, SyntaxException {
        while (this.curToken == 10) {
            getRawToken();
        }
        if (this.curToken == 65 || this.curToken == 81) {
            int next = skipSpace(this.nesting != 0);
            switch (this.tokenBuffer[0]) {
                case 'a':
                    if (match("attribute")) {
                        if (next == 40) {
                            this.curToken = OP_ATTRIBUTE;
                            return OP_ATTRIBUTE;
                        } else if (next == 123 || XName.isNameStart((char) next)) {
                            unread();
                            this.curToken = 252;
                            return 252;
                        }
                    }
                    break;
                case 'c':
                    if (match("comment")) {
                        if (next == 40) {
                            this.curToken = OP_COMMENT;
                            return OP_COMMENT;
                        } else if (next == 123) {
                            unread();
                            this.curToken = 254;
                            return 254;
                        }
                    }
                    break;
                case 'd':
                    if (next == 123 && match("document")) {
                        unread();
                        this.curToken = 256;
                        return 256;
                    } else if (next == 40 && match("document-node")) {
                        this.curToken = OP_DOCUMENT;
                        return OP_DOCUMENT;
                    }
                    break;
                case 'e':
                    if (match("element")) {
                        if (next == 40) {
                            this.curToken = OP_ELEMENT;
                            return OP_ELEMENT;
                        } else if (next == 123 || XName.isNameStart((char) next)) {
                            unread();
                            this.curToken = 251;
                            return 251;
                        }
                    } else if (next == 40 && match("empty-sequence")) {
                        this.curToken = OP_EMPTY_SEQUENCE;
                        return OP_EMPTY_SEQUENCE;
                    } else if (next == 36 && match("every")) {
                        this.curToken = EVERY_DOLLAR_TOKEN;
                        return EVERY_DOLLAR_TOKEN;
                    }
                    break;
                case 'f':
                    if (next == 36 && match("for")) {
                        this.curToken = FOR_DOLLAR_TOKEN;
                        return FOR_DOLLAR_TOKEN;
                    }
                case 'i':
                    if (next == 40 && match("if")) {
                        this.curToken = 241;
                        return 241;
                    } else if (next == 40 && match("item")) {
                        this.curToken = OP_ITEM;
                        return OP_ITEM;
                    }
                    break;
                case 'l':
                    if (next == 36 && match("let")) {
                        this.curToken = LET_DOLLAR_TOKEN;
                        return LET_DOLLAR_TOKEN;
                    }
                case 'n':
                    if (next == 40 && match("node")) {
                        this.curToken = OP_NODE;
                        return OP_NODE;
                    }
                case 'o':
                    if (next == 123 && match("ordered")) {
                        this.curToken = ORDERED_LBRACE_TOKEN;
                        return ORDERED_LBRACE_TOKEN;
                    }
                case 'p':
                    if (match("processing-instruction")) {
                        if (next == 40) {
                            this.curToken = OP_PI;
                            return OP_PI;
                        } else if (next == 123 || XName.isNameStart((char) next)) {
                            unread();
                            this.curToken = 255;
                            return 255;
                        }
                    }
                    break;
                case 's':
                    if (next == 36 && match("some")) {
                        this.curToken = SOME_DOLLAR_TOKEN;
                        return SOME_DOLLAR_TOKEN;
                    } else if (next == 40 && match("schema-attribute")) {
                        this.curToken = OP_SCHEMA_ATTRIBUTE;
                        return OP_SCHEMA_ATTRIBUTE;
                    } else if (next == 40 && match("schema-element")) {
                        this.curToken = OP_SCHEMA_ELEMENT;
                        return OP_SCHEMA_ELEMENT;
                    }
                    break;
                case 't':
                    if (match(PropertyTypeConstants.PROPERTY_TYPE_TEXT)) {
                        if (next == 40) {
                            this.curToken = OP_TEXT;
                            return OP_TEXT;
                        } else if (next == 123) {
                            unread();
                            this.curToken = 253;
                            return 253;
                        }
                    }
                    if (next == 40 && match("typeswitch")) {
                        this.curToken = 242;
                        return 242;
                    }
                case 'u':
                    if (next == 123 && match("unordered")) {
                        this.curToken = 250;
                        return 250;
                    }
                case 'v':
                    if (next == 123 && match("validate")) {
                        this.curToken = VALIDATE_LBRACE_TOKEN;
                        return VALIDATE_LBRACE_TOKEN;
                    }
            }
            if (next == 40 && peek() != 58) {
                this.curToken = 70;
                return 70;
            } else if (next == 58 && peek() == 58) {
                int axis = getAxis();
                this.curToken = axis;
                return axis;
            } else {
                this.curValue = new String(this.tokenBuffer, 0, this.tokenBufferLength);
                switch (next) {
                    case 98:
                        if (lookingAt("declare", "ase-uri")) {
                            this.curToken = 66;
                            return 66;
                        } else if (lookingAt("declare", "oundary-space")) {
                            this.curToken = 83;
                            return 83;
                        }
                        break;
                    case 99:
                        if (lookingAt("declare", "onstruction")) {
                            this.curToken = 75;
                            return 75;
                        } else if (lookingAt("declare", "opy-namespaces")) {
                            this.curToken = 76;
                            return 76;
                        }
                        break;
                    case 100:
                        if (lookingAt("declare", "efault")) {
                            getRawToken();
                            if (match("function")) {
                                this.curToken = 79;
                                return 79;
                            } else if (match("element")) {
                                this.curToken = 69;
                                return 69;
                            } else if (match("collation")) {
                                this.curToken = 71;
                                return 71;
                            } else if (match("order")) {
                                this.curToken = 72;
                                return 72;
                            } else {
                                error("unrecognized/unimplemented 'declare default'");
                                skipToSemicolon();
                                return peekOperand();
                            }
                        }
                        break;
                    case 101:
                        break;
                    case 102:
                        if (lookingAt("declare", "unction")) {
                            this.curToken = 80;
                            return 80;
                        } else if (lookingAt("define", "unction")) {
                            warnOldVersion("replace 'define function' by 'declare function'");
                            this.curToken = 80;
                            return 80;
                        } else if (lookingAt("default", "unction")) {
                            warnOldVersion("replace 'default function' by 'declare default function namespace'");
                            this.curToken = 79;
                            return 79;
                        }
                        break;
                    case 109:
                        if (lookingAt("import", "odule")) {
                            this.curToken = 73;
                            return 73;
                        }
                        break;
                    case 110:
                        if (lookingAt("declare", "amespace")) {
                            this.curToken = 78;
                            return 78;
                        } else if (lookingAt("default", "amespace")) {
                            warnOldVersion("replace 'default namespace' by 'declare default element namespace'");
                            this.curToken = 69;
                            return 69;
                        } else if (lookingAt("module", "amespace")) {
                            this.curToken = 77;
                            return 77;
                        }
                        break;
                    case 111:
                        if (lookingAt("declare", "rdering")) {
                            this.curToken = 85;
                            return 85;
                        } else if (lookingAt("declare", "ption")) {
                            this.curToken = 111;
                            return 111;
                        }
                        break;
                    case 115:
                        if (lookingAt("import", "chema")) {
                            this.curToken = 84;
                            return 84;
                        }
                        break;
                    case 118:
                        if (lookingAt("declare", "ariable")) {
                            this.curToken = 86;
                            return 86;
                        } else if (lookingAt("define", "ariable")) {
                            warnOldVersion("replace 'define variable' by 'declare variable'");
                            this.curToken = 86;
                            return 86;
                        } else if (lookingAt("xquery", "ersion")) {
                            this.curToken = 89;
                            return 89;
                        }
                        break;
                    case 120:
                        if (lookingAt("declare", "mlspace")) {
                            warnOldVersion("replace 'define xmlspace' by 'declare boundary-space'");
                            this.curToken = 83;
                            return 83;
                        }
                        break;
                }
                if (lookingAt("default", "lement")) {
                    warnOldVersion("replace 'default element' by 'declare default element namespace'");
                    this.curToken = 69;
                    return 69;
                }
                if (next >= 0) {
                    unread();
                    if (XName.isNameStart((char) next) && this.curValue.equals("define")) {
                        getRawToken();
                        this.curToken = 87;
                    }
                }
                return this.curToken;
            }
        } else {
            if (this.curToken == 67) {
                int next2 = read();
                if (next2 == 58) {
                    this.curToken = getAxis();
                } else {
                    unread(next2);
                }
            }
            return this.curToken;
        }
    }

    /* access modifiers changed from: package-private */
    public void checkAllowedNamespaceDeclaration(String prefix, String uri, boolean inConstructor) {
        boolean xmlPrefix = "xml".equals(prefix);
        if (NamespaceBinding.XML_NAMESPACE.equals(uri)) {
            if (!xmlPrefix || !inConstructor) {
                error('e', "namespace uri cannot be the same as the prefined xml namespace", "XQST0070");
            }
        } else if (xmlPrefix || "xmlns".equals(prefix)) {
            error('e', "namespace prefix cannot be 'xml' or 'xmlns'", "XQST0070");
        }
    }

    /* access modifiers changed from: package-private */
    public void pushNamespace(String prefix, String uri) {
        if (uri.length() == 0) {
            uri = null;
        }
        this.prologNamespaces = new NamespaceBinding(prefix, uri, this.prologNamespaces);
    }

    public XQParser(InPort port, SourceMessages messages, XQuery interp) {
        super(port, messages);
        this.interpreter = interp;
        this.nesting = 1;
        this.prologNamespaces = builtinNamespaces;
    }

    public void setInteractive(boolean v) {
        if (this.interactive != v) {
            if (v) {
                this.nesting--;
            } else {
                this.nesting++;
            }
        }
        this.interactive = v;
    }

    private static final int priority(int opcode) {
        switch (opcode) {
            case 400:
                return 1;
            case 401:
                return 2;
            case 402:
            case 403:
            case 404:
            case 405:
            case 406:
            case 407:
            case 408:
            case 409:
            case 410:
            case 411:
            case OP_EQ /*426*/:
            case OP_NE /*427*/:
            case OP_LT /*428*/:
            case OP_LE /*429*/:
            case OP_GT /*430*/:
            case OP_GE /*431*/:
                return 3;
            case 412:
                return 4;
            case 413:
            case 414:
                return 5;
            case 415:
            case 416:
            case 417:
            case 418:
                return 6;
            case 419:
                return 7;
            case 420:
            case 421:
                return 8;
            case 422:
                return 9;
            case 423:
                return 10;
            case OP_CASTABLE_AS /*424*/:
                return 11;
            case OP_CAST_AS /*425*/:
                return 12;
            default:
                return 0;
        }
    }

    static Expression makeBinary(Expression func, Expression exp1, Expression exp2) {
        return new ApplyExp(func, exp1, exp2);
    }

    static Expression makeExprSequence(Expression exp1, Expression exp2) {
        return makeBinary(makeFunctionExp("gnu.kawa.functions.AppendValues", "appendValues"), exp1, exp2);
    }

    /* access modifiers changed from: package-private */
    public Expression makeBinary(int op, Expression exp1, Expression exp2) throws IOException, SyntaxException {
        Expression func;
        switch (op) {
            case 402:
                func = makeFunctionExp("gnu.xquery.util.Compare", "=");
                break;
            case 403:
                func = makeFunctionExp("gnu.xquery.util.Compare", "!=");
                break;
            case 404:
                func = makeFunctionExp("gnu.xquery.util.Compare", "<");
                break;
            case 405:
                func = makeFunctionExp("gnu.xquery.util.Compare", ">");
                break;
            case 406:
                func = makeFunctionExp("gnu.xquery.util.Compare", "<=");
                break;
            case 407:
                func = makeFunctionExp("gnu.xquery.util.Compare", ">=");
                break;
            case 408:
                func = makeFunctionExp("gnu.kawa.xml.NodeCompare", "$Eq", "is");
                break;
            case 409:
                func = makeFunctionExp("gnu.kawa.xml.NodeCompare", "$Ne", "isnot");
                break;
            case 410:
                func = makeFunctionExp("gnu.kawa.xml.NodeCompare", "$Gr", ">>");
                break;
            case 411:
                func = makeFunctionExp("gnu.kawa.xml.NodeCompare", "$Ls", "<<");
                break;
            case 412:
                func = makeFunctionExp("gnu.xquery.util.IntegerRange", "integerRange");
                break;
            case 413:
                func = makeFunctionExp("gnu.xquery.util.ArithOp", "add", "+");
                break;
            case 414:
                func = makeFunctionExp("gnu.xquery.util.ArithOp", "sub", "-");
                break;
            case 415:
                func = makeFunctionExp("gnu.xquery.util.ArithOp", "mul", "*");
                break;
            case 416:
                func = makeFunctionExp("gnu.xquery.util.ArithOp", "div", "div");
                break;
            case 417:
                func = makeFunctionExp("gnu.xquery.util.ArithOp", "idiv", "idiv");
                break;
            case 418:
                func = makeFunctionExp("gnu.xquery.util.ArithOp", "mod", "mod");
                break;
            case 419:
                func = makeFunctionExp("gnu.kawa.xml.UnionNodes", "unionNodes");
                break;
            case 420:
                func = makeFunctionExp("gnu.kawa.xml.IntersectNodes", "intersectNodes");
                break;
            case 421:
                func = makeFunctionExp("gnu.kawa.xml.IntersectNodes", "exceptNodes");
                break;
            case OP_EQ /*426*/:
                func = makeFunctionExp("gnu.xquery.util.Compare", "valEq", "eq");
                break;
            case OP_NE /*427*/:
                func = makeFunctionExp("gnu.xquery.util.Compare", "valNe", "ne");
                break;
            case OP_LT /*428*/:
                func = makeFunctionExp("gnu.xquery.util.Compare", "valLt", "lt");
                break;
            case OP_LE /*429*/:
                func = makeFunctionExp("gnu.xquery.util.Compare", "valLe", "le");
                break;
            case OP_GT /*430*/:
                func = makeFunctionExp("gnu.xquery.util.Compare", "valGt", "gt");
                break;
            case OP_GE /*431*/:
                func = makeFunctionExp("gnu.xquery.util.Compare", "valGe", "ge");
                break;
            default:
                return syntaxError("unimplemented binary op: " + op);
        }
        return makeBinary(func, exp1, exp2);
    }

    private void parseSimpleKindType() throws IOException, SyntaxException {
        getRawToken();
        if (this.curToken == 41) {
            getRawToken();
        } else {
            error("expected ')'");
        }
    }

    public Expression parseNamedNodeType(boolean attribute) throws IOException, SyntaxException {
        Expression qname;
        Expression tname = null;
        getRawToken();
        if (this.curToken == 41) {
            qname = QuoteExp.getInstance(ElementType.MATCH_ANY_QNAME);
            getRawToken();
        } else {
            if (this.curToken == 81 || this.curToken == 65) {
                qname = parseNameTest(attribute);
            } else {
                if (this.curToken != 415) {
                    syntaxError("expected QName or *");
                }
                qname = QuoteExp.getInstance(ElementType.MATCH_ANY_QNAME);
            }
            getRawToken();
            if (this.curToken == 44) {
                getRawToken();
                if (this.curToken == 81 || this.curToken == 65) {
                    tname = parseDataType();
                } else {
                    syntaxError("expected QName");
                }
            }
            if (this.curToken == 41) {
                getRawToken();
            } else {
                error("expected ')' after element");
            }
        }
        return makeNamedNodeType(attribute, qname, tname);
    }

    static Expression makeNamedNodeType(boolean attribute, Expression qname, Expression tname) {
        Expression[] expressionArr = new Expression[2];
        ApplyExp elt = new ApplyExp(ClassType.make(attribute ? "gnu.kawa.xml.AttributeType" : "gnu.kawa.xml.ElementType").getDeclaredMethod("make", 1), qname);
        elt.setFlag(4);
        return tname == null ? elt : new BeginExp(tname, elt);
    }

    private void warnOldStyleKindTest() {
        if (!this.warnedOldStyleKindTest) {
            error('w', "old-style KindTest - first one here");
            this.warnedOldStyleKindTest = true;
        }
    }

    public Expression parseOptionalTypeDeclaration() throws IOException, SyntaxException {
        if (!match("as")) {
            return null;
        }
        getRawToken();
        return parseDataType();
    }

    public Expression parseDataType() throws IOException, SyntaxException {
        int min;
        int max;
        Expression etype = parseItemType();
        if (etype == null) {
            if (this.curToken != OP_EMPTY_SEQUENCE) {
                return syntaxError("bad syntax - expected DataType");
            }
            parseSimpleKindType();
            if (this.curToken == 63 || this.curToken == 413 || this.curToken == 415) {
                getRawToken();
                return syntaxError("occurrence-indicator meaningless after empty-sequence()");
            }
            etype = QuoteExp.getInstance(OccurrenceType.emptySequenceType);
            min = 0;
            max = 0;
        } else if (this.curToken == 63) {
            min = 0;
            max = 1;
        } else if (this.curToken == 413) {
            min = 1;
            max = -1;
        } else if (this.curToken == 415) {
            min = 0;
            max = -1;
        } else {
            min = 1;
            max = 1;
        }
        if (this.parseContext == 67 && max != 1) {
            return syntaxError("type to 'cast as' or 'castable as' must be a 'SingleType'");
        }
        if (min == max) {
            return etype;
        }
        getRawToken();
        ApplyExp otype = new ApplyExp((Procedure) proc_OccurrenceType_getInstance, etype, QuoteExp.getInstance(IntNum.make(min)), QuoteExp.getInstance(IntNum.make(max)));
        otype.setFlag(4);
        return otype;
    }

    public Expression parseMaybeKindTest() throws IOException, SyntaxException {
        Type type;
        boolean z = false;
        switch (this.curToken) {
            case OP_NODE /*230*/:
                parseSimpleKindType();
                type = NodeType.anyNodeTest;
                break;
            case OP_TEXT /*231*/:
                parseSimpleKindType();
                type = NodeType.textNodeTest;
                break;
            case OP_COMMENT /*232*/:
                parseSimpleKindType();
                type = NodeType.commentNodeTest;
                break;
            case OP_PI /*233*/:
                getRawToken();
                String piTarget = null;
                if (this.curToken == 65 || this.curToken == 34) {
                    piTarget = new String(this.tokenBuffer, 0, this.tokenBufferLength);
                    getRawToken();
                }
                if (this.curToken == 41) {
                    getRawToken();
                } else {
                    error("expected ')'");
                }
                type = ProcessingInstructionType.getInstance(piTarget);
                break;
            case OP_DOCUMENT /*234*/:
                parseSimpleKindType();
                type = NodeType.documentNodeTest;
                break;
            case OP_ELEMENT /*235*/:
            case OP_ATTRIBUTE /*236*/:
                if (this.curToken == OP_ATTRIBUTE) {
                    z = true;
                }
                return parseNamedNodeType(z);
            default:
                return null;
        }
        return QuoteExp.getInstance(type);
    }

    public Expression parseItemType() throws IOException, SyntaxException {
        Type type;
        peekOperand();
        Expression etype = parseMaybeKindTest();
        if (etype != null) {
            if (this.parseContext != 67) {
                return etype;
            }
            type = XDataType.anyAtomicType;
        } else if (this.curToken == OP_ITEM) {
            parseSimpleKindType();
            type = SingletonType.getInstance();
        } else if (this.curToken != 65 && this.curToken != 81) {
            return null;
        } else {
            ReferenceExp rexp = new ReferenceExp((Object) new String(this.tokenBuffer, 0, this.tokenBufferLength));
            rexp.setFlag(16);
            maybeSetLine((Expression) rexp, this.curLine, this.curColumn);
            getRawToken();
            return rexp;
        }
        return QuoteExp.getInstance(type);
    }

    /* access modifiers changed from: package-private */
    public Object parseURILiteral() throws IOException, SyntaxException {
        getRawToken();
        if (this.curToken != 34) {
            return declError("expected a URILiteral");
        }
        return TextUtils.replaceWhitespace(new String(this.tokenBuffer, 0, this.tokenBufferLength), true);
    }

    /* access modifiers changed from: package-private */
    public Expression parseExpr() throws IOException, SyntaxException {
        return parseExprSingle();
    }

    /* access modifiers changed from: package-private */
    public final Expression parseExprSingle() throws IOException, SyntaxException {
        int i = this.curLine;
        int i2 = this.curColumn;
        peekOperand();
        switch (this.curToken) {
            case 241:
                return parseIfExpr();
            case 242:
                return parseTypeSwitch();
            case FOR_DOLLAR_TOKEN /*243*/:
                return parseFLWRExpression(true);
            case LET_DOLLAR_TOKEN /*244*/:
                return parseFLWRExpression(false);
            case SOME_DOLLAR_TOKEN /*245*/:
                return parseQuantifiedExpr(false);
            case EVERY_DOLLAR_TOKEN /*246*/:
                return parseQuantifiedExpr(true);
            default:
                return parseBinaryExpr(priority(400));
        }
    }

    /* access modifiers changed from: package-private */
    public Expression parseBinaryExpr(int prio) throws IOException, SyntaxException {
        int tokPriority;
        Expression func;
        Expression exp = parseUnaryExpr();
        while (true) {
            int token = peekOperator();
            if (token == 10 || ((token == 404 && peek() == 47) || (tokPriority = priority(token)) < prio)) {
                return exp;
            }
            char saveReadState = pushNesting('%');
            getRawToken();
            popNesting(saveReadState);
            if (token >= 422 && token <= OP_CAST_AS) {
                if (token == OP_CAST_AS || token == OP_CASTABLE_AS) {
                    this.parseContext = 67;
                }
                Expression type = parseDataType();
                this.parseContext = 0;
                Expression[] args = new Expression[2];
                switch (token) {
                    case 422:
                        args[0] = exp;
                        args[1] = type;
                        func = makeFunctionExp("gnu.xquery.lang.XQParser", "instanceOf");
                        break;
                    case 423:
                        args[0] = type;
                        args[1] = exp;
                        func = makeFunctionExp("gnu.xquery.lang.XQParser", "treatAs");
                        break;
                    case OP_CASTABLE_AS /*424*/:
                        args[0] = exp;
                        args[1] = type;
                        func = new ReferenceExp(XQResolveNames.castableAsDecl);
                        break;
                    default:
                        args[0] = type;
                        args[1] = exp;
                        func = new ReferenceExp(XQResolveNames.castAsDecl);
                        break;
                }
                exp = new ApplyExp(func, args);
            } else if (token == 422) {
                exp = new ApplyExp(makeFunctionExp("gnu.xquery.lang.XQParser", "instanceOf"), exp, parseDataType());
            } else {
                Expression exp2 = parseBinaryExpr(tokPriority + 1);
                if (token == 401) {
                    exp = new IfExp(booleanValue(exp), booleanValue(exp2), XQuery.falseExp);
                } else if (token == 400) {
                    exp = new IfExp(booleanValue(exp), XQuery.trueExp, booleanValue(exp2));
                } else {
                    exp = makeBinary(token, exp, exp2);
                }
            }
        }
        return exp;
    }

    /* access modifiers changed from: package-private */
    public Expression parseUnaryExpr() throws IOException, SyntaxException {
        if (this.curToken != 414 && this.curToken != 413) {
            return parseUnionExpr();
        }
        int op = this.curToken;
        getRawToken();
        return new ApplyExp(makeFunctionExp("gnu.xquery.util.ArithOp", op == 413 ? "plus" : "minus", op == 413 ? "+" : "-"), parseUnaryExpr());
    }

    /* access modifiers changed from: package-private */
    public Expression parseUnionExpr() throws IOException, SyntaxException {
        Expression exp = parseIntersectExceptExpr();
        while (true) {
            int op = peekOperator();
            if (op != 419) {
                return exp;
            }
            getRawToken();
            exp = makeBinary(op, exp, parseIntersectExceptExpr());
        }
    }

    /* access modifiers changed from: package-private */
    public Expression parseIntersectExceptExpr() throws IOException, SyntaxException {
        Expression exp = parsePathExpr();
        while (true) {
            int op = peekOperator();
            if (op != 420 && op != 421) {
                return exp;
            }
            getRawToken();
            exp = makeBinary(op, exp, parsePathExpr());
        }
    }

    /* access modifiers changed from: package-private */
    public Expression parsePathExpr() throws IOException, SyntaxException {
        Expression step1;
        Expression dot;
        boolean z = true;
        if (this.curToken == 47 || this.curToken == 68) {
            Declaration dotDecl = this.comp.lexical.lookup((Object) DOT_VARNAME, false);
            if (dotDecl == null) {
                dot = syntaxError("context item is undefined", "XPDY0002");
            } else {
                dot = new ReferenceExp(DOT_VARNAME, dotDecl);
            }
            step1 = new ApplyExp(ClassType.make("gnu.xquery.util.NodeUtils").getDeclaredMethod("rootDocument", 1), dot);
            if (this.nesting == 0) {
                z = false;
            }
            int next = skipSpace(z);
            unread(next);
            if (next < 0 || next == 41 || next == 125) {
                getRawToken();
                return step1;
            }
        } else {
            step1 = parseStepExpr();
        }
        return parseRelativePathExpr(step1);
    }

    /* access modifiers changed from: package-private */
    public Expression parseNameTest(boolean attribute) throws IOException, SyntaxException {
        String str;
        String local = null;
        String prefix = null;
        if (this.curToken == 81) {
            int colon = this.tokenBufferLength;
            do {
                colon--;
            } while (this.tokenBuffer[colon] != ':');
            prefix = new String(this.tokenBuffer, 0, colon);
            int colon2 = colon + 1;
            local = new String(this.tokenBuffer, colon2, this.tokenBufferLength - colon2);
        } else if (this.curToken == 415) {
            int next = read();
            String local2 = "";
            if (next != 58) {
                unread(next);
            } else {
                int next2 = read();
                if (next2 < 0) {
                    eofError("unexpected end-of-file after '*:'");
                }
                if (XName.isNameStart((char) next2)) {
                    unread();
                    getRawToken();
                    if (this.curToken != 65) {
                        syntaxError("invalid name test");
                    } else {
                        local2 = new String(this.tokenBuffer, 0, this.tokenBufferLength).intern();
                    }
                } else if (next2 != 42) {
                    syntaxError("missing local-name after '*:'");
                }
            }
            return QuoteExp.getInstance(new Symbol((Namespace) null, local2));
        } else if (this.curToken == 65) {
            local = new String(this.tokenBuffer, 0, this.tokenBufferLength);
            if (attribute) {
                return new QuoteExp(Namespace.EmptyNamespace.getSymbol(local.intern()));
            }
            prefix = null;
        } else if (this.curToken == 67) {
            prefix = new String(this.tokenBuffer, 0, this.tokenBufferLength);
            if (read() != 42) {
                syntaxError("invalid characters after 'NCName:'");
            }
            local = "";
        }
        if (prefix != null) {
            prefix = prefix.intern();
        }
        Expression[] args = new Expression[3];
        args[0] = new ApplyExp((Expression) new ReferenceExp(XQResolveNames.resolvePrefixDecl), QuoteExp.getInstance(prefix));
        if (local == null) {
            str = "";
        } else {
            str = local;
        }
        args[1] = new QuoteExp(str);
        args[2] = new QuoteExp(prefix);
        ApplyExp make = new ApplyExp(Compilation.typeSymbol.getDeclaredMethod("make", 3), args);
        make.setFlag(4);
        return make;
    }

    /* access modifiers changed from: package-private */
    public Expression parseNodeTest(int axis) throws IOException, SyntaxException {
        Expression dot;
        Expression makeAxisStep;
        String axisName;
        int peekOperand = peekOperand();
        Expression[] args = new Expression[1];
        Expression etype = parseMaybeKindTest();
        if (etype != null) {
            args[0] = etype;
        } else if (this.curToken == 65 || this.curToken == 81 || this.curToken == 67 || this.curToken == 415) {
            args[0] = makeNamedNodeType(axis == 2, parseNameTest(axis == 2), (Expression) null);
        } else if (axis >= 0) {
            return syntaxError("unsupported axis '" + axisNames[axis] + "::'");
        } else {
            return null;
        }
        Declaration dotDecl = this.comp.lexical.lookup((Object) DOT_VARNAME, false);
        if (dotDecl == null) {
            dot = syntaxError("node test when context item is undefined", "XPDY0002");
        } else {
            dot = new ReferenceExp(DOT_VARNAME, dotDecl);
        }
        if (etype == null) {
            getRawToken();
        }
        if (axis == 3 || axis == -1) {
            makeAxisStep = makeChildAxisStep;
        } else if (axis == 4) {
            makeAxisStep = makeDescendantAxisStep;
        } else {
            switch (axis) {
                case 0:
                    axisName = "Ancestor";
                    break;
                case 1:
                    axisName = "AncestorOrSelf";
                    break;
                case 2:
                    axisName = "Attribute";
                    break;
                case 5:
                    axisName = "DescendantOrSelf";
                    break;
                case 6:
                    axisName = "Following";
                    break;
                case 7:
                    axisName = "FollowingSibling";
                    break;
                case 9:
                    axisName = "Parent";
                    break;
                case 10:
                    axisName = "Preceding";
                    break;
                case 11:
                    axisName = "PrecedingSibling";
                    break;
                case 12:
                    axisName = "Self";
                    break;
                default:
                    throw new Error();
            }
            makeAxisStep = QuoteExp.getInstance(new PrimProcedure("gnu.kawa.xml." + axisName + "Axis", "make", 1));
        }
        ApplyExp mkAxis = new ApplyExp(makeAxisStep, args);
        mkAxis.setFlag(4);
        return new ApplyExp((Expression) mkAxis, dot);
    }

    /* access modifiers changed from: package-private */
    public Expression parseRelativePathExpr(Expression exp) throws IOException, SyntaxException {
        Expression beforeSlashSlash = null;
        while (true) {
            if (this.curToken != 47 && this.curToken != 68) {
                return exp;
            }
            boolean descendants = this.curToken == 68;
            LambdaExp lexp = new LambdaExp(3);
            Declaration dotDecl = lexp.addDeclaration((Object) DOT_VARNAME);
            dotDecl.setFlag(262144);
            dotDecl.setType(NodeType.anyNodeTest);
            dotDecl.noteValue((Expression) null);
            lexp.addDeclaration(POSITION_VARNAME, LangPrimType.intType);
            lexp.addDeclaration(LAST_VARNAME, LangPrimType.intType);
            this.comp.push((ScopeExp) lexp);
            if (descendants) {
                this.curToken = 47;
                lexp.body = new ApplyExp((Procedure) DescendantOrSelfAxis.anyNode, new ReferenceExp(DOT_VARNAME, dotDecl));
                beforeSlashSlash = exp;
            } else {
                getRawToken();
                Expression exp2 = parseStepExpr();
                if (beforeSlashSlash != null && (exp2 instanceof ApplyExp)) {
                    Expression func = ((ApplyExp) exp2).getFunction();
                    if (func instanceof ApplyExp) {
                        ApplyExp aexp = (ApplyExp) func;
                        if (aexp.getFunction() == makeChildAxisStep) {
                            aexp.setFunction(makeDescendantAxisStep);
                            exp = beforeSlashSlash;
                        }
                    }
                }
                lexp.body = exp2;
                beforeSlashSlash = null;
            }
            this.comp.pop(lexp);
            exp = new ApplyExp((Procedure) RelativeStep.relativeStep, exp, lexp);
        }
    }

    /* access modifiers changed from: package-private */
    public Expression parseStepExpr() throws IOException, SyntaxException {
        int axis;
        Expression exp;
        int i;
        Expression unqualifiedStep;
        if (this.curToken == 46 || this.curToken == 51) {
            if (this.curToken == 46) {
                axis = 12;
            } else {
                axis = 9;
            }
            getRawToken();
            Declaration dotDecl = this.comp.lexical.lookup((Object) DOT_VARNAME, false);
            if (dotDecl == null) {
                exp = syntaxError("context item is undefined", "XPDY0002");
            } else {
                exp = new ReferenceExp(DOT_VARNAME, dotDecl);
            }
            if (axis == 9) {
                exp = new ApplyExp((Procedure) ParentAxis.make(NodeType.anyNodeTest), exp);
            }
            if (axis == 12) {
                i = -1;
            } else {
                i = axis;
            }
            return parseStepQualifiers(exp, i);
        }
        int axis2 = peekOperand() - 100;
        if (axis2 >= 0 && axis2 < 13) {
            getRawToken();
            unqualifiedStep = parseNodeTest(axis2);
        } else if (this.curToken == 64) {
            getRawToken();
            axis2 = 2;
            unqualifiedStep = parseNodeTest(2);
        } else if (this.curToken == OP_ATTRIBUTE) {
            axis2 = 2;
            unqualifiedStep = parseNodeTest(2);
        } else {
            unqualifiedStep = parseNodeTest(-1);
            if (unqualifiedStep != null) {
                axis2 = 3;
            } else {
                axis2 = -1;
                unqualifiedStep = parsePrimaryExpr();
            }
        }
        return parseStepQualifiers(unqualifiedStep, axis2);
    }

    /* access modifiers changed from: package-private */
    public Expression parseStepQualifiers(Expression exp, int axis) throws IOException, SyntaxException {
        Procedure valuesFilter;
        while (this.curToken == 91) {
            int startLine = getLineNumber() + 1;
            int startColumn = getColumnNumber() + 1;
            int i = this.seenPosition;
            int i2 = this.seenLast;
            getRawToken();
            LambdaExp lexp = new LambdaExp(3);
            maybeSetLine((Expression) lexp, startLine, startColumn);
            Declaration dot = lexp.addDeclaration((Object) DOT_VARNAME);
            if (axis >= 0) {
                dot.setType(NodeType.anyNodeTest);
            } else {
                dot.setType(SingletonType.getInstance());
            }
            lexp.addDeclaration(POSITION_VARNAME, Type.intType);
            lexp.addDeclaration(LAST_VARNAME, Type.intType);
            this.comp.push((ScopeExp) lexp);
            dot.noteValue((Expression) null);
            Expression cond = parseExprSequence(93, false);
            if (this.curToken == -1) {
                eofError("missing ']' - unexpected end-of-file");
            }
            if (axis < 0) {
                valuesFilter = ValuesFilter.exprFilter;
            } else if (axis == 0 || axis == 1 || axis == 9 || axis == 10 || axis == 11) {
                valuesFilter = ValuesFilter.reverseFilter;
            } else {
                valuesFilter = ValuesFilter.forwardFilter;
            }
            maybeSetLine(cond, startLine, startColumn);
            this.comp.pop(lexp);
            lexp.body = cond;
            getRawToken();
            exp = new ApplyExp(valuesFilter, exp, lexp);
        }
        return exp;
    }

    /* access modifiers changed from: package-private */
    public Expression parsePrimaryExpr() throws IOException, SyntaxException {
        Expression exp = parseMaybePrimaryExpr();
        if (exp != null) {
            return exp;
        }
        Expression exp2 = syntaxError("missing expression");
        if (this.curToken != -1) {
            getRawToken();
        }
        return exp2;
    }

    /* access modifiers changed from: package-private */
    public void parseEntityOrCharRef() throws IOException, SyntaxException {
        int base;
        int next = read();
        if (next == 35) {
            int next2 = read();
            if (next2 == 120) {
                base = 16;
                next2 = read();
            } else {
                base = 10;
            }
            int value = 0;
            while (next2 >= 0) {
                int digit = Character.digit((char) next2, base);
                if (digit < 0 || value >= 134217728) {
                    break;
                }
                value = (value * base) + digit;
                next2 = read();
            }
            if (next2 != 59) {
                unread();
                error("invalid character reference");
            } else if ((value <= 0 || value > 55295) && ((value < 57344 || value > 65533) && (value < 65536 || value > 1114111))) {
                error('e', "invalid character value " + value, "XQST0090");
            } else {
                tokenBufferAppend(value);
            }
        } else {
            int saveLength = this.tokenBufferLength;
            while (next >= 0) {
                char ch = (char) next;
                if (!XName.isNamePart(ch)) {
                    break;
                }
                tokenBufferAppend(ch);
                next = read();
            }
            if (next != 59) {
                unread();
                error("invalid entity reference");
                return;
            }
            String ref = new String(this.tokenBuffer, saveLength, this.tokenBufferLength - saveLength);
            this.tokenBufferLength = saveLength;
            appendNamedEntity(ref);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0170  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x008c A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parseContent(char r21, java.util.Vector r22) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r20 = this;
            r17 = 0
            r0 = r17
            r1 = r20
            r1.tokenBufferLength = r0
            int r14 = r22.size()
            int r11 = r14 + -1
            r0 = r20
            boolean r0 = r0.boundarySpacePreserve
            r17 = r0
            if (r17 != 0) goto L_0x008d
            r17 = 60
            r0 = r21
            r1 = r17
            if (r0 != r1) goto L_0x008d
            r12 = 1
        L_0x001f:
            r13 = r12
        L_0x0020:
            int r9 = r20.read()
            r0 = r21
            if (r9 != r0) goto L_0x00f8
            r17 = 60
            r0 = r21
            r1 = r17
            if (r0 != r1) goto L_0x00ed
            int r9 = r20.read()
            r16 = 0
            r0 = r20
            int r0 = r0.tokenBufferLength
            r17 = r0
            if (r17 <= 0) goto L_0x0073
            java.lang.String r15 = new java.lang.String
            r0 = r20
            char[] r0 = r0.tokenBuffer
            r17 = r0
            r18 = 0
            r0 = r20
            int r0 = r0.tokenBufferLength
            r19 = r0
            r0 = r17
            r1 = r18
            r2 = r19
            r15.<init>(r0, r1, r2)
            r17 = 1
            r0 = r17
            gnu.expr.Expression[] r4 = new gnu.expr.Expression[r0]
            r17 = 0
            gnu.expr.QuoteExp r18 = new gnu.expr.QuoteExp
            r0 = r18
            r0.<init>(r15)
            r4[r17] = r18
            gnu.expr.ApplyExp r16 = new gnu.expr.ApplyExp
            gnu.expr.Expression r17 = makeText
            r0 = r16
            r1 = r17
            r0.<init>((gnu.expr.Expression) r1, (gnu.expr.Expression[]) r4)
        L_0x0073:
            r17 = 0
            r0 = r17
            r1 = r20
            r1.tokenBufferLength = r0
            r17 = 47
            r0 = r17
            if (r9 != r0) goto L_0x008f
            if (r16 == 0) goto L_0x008c
            if (r13 != 0) goto L_0x008c
            r0 = r22
            r1 = r16
            r0.addElement(r1)
        L_0x008c:
            return
        L_0x008d:
            r12 = 0
            goto L_0x001f
        L_0x008f:
            r17 = 1
            r0 = r20
            r1 = r17
            gnu.expr.Expression r5 = r0.parseXMLConstructor(r9, r1)
            r8 = 0
            r6 = 0
            boolean r0 = r5 instanceof gnu.expr.ApplyExp
            r17 = r0
            if (r17 == 0) goto L_0x00c8
            r3 = r5
            gnu.expr.ApplyExp r3 = (gnu.expr.ApplyExp) r3
            gnu.expr.Expression r17 = r3.getFunction()
            gnu.expr.Expression r18 = makeCDATA
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x00c8
            r8 = 1
            r17 = 0
            r0 = r17
            gnu.expr.Expression r17 = r3.getArg(r0)
            java.lang.Object r15 = r17.valueIfConstant()
            java.lang.String r15 = (java.lang.String) r15
            if (r15 == 0) goto L_0x00e9
            int r17 = r15.length()
            if (r17 != 0) goto L_0x00e9
            r6 = 1
        L_0x00c8:
            if (r16 == 0) goto L_0x00d5
            if (r13 == 0) goto L_0x00ce
            if (r8 == 0) goto L_0x00d5
        L_0x00ce:
            r0 = r22
            r1 = r16
            r0.addElement(r1)
        L_0x00d5:
            if (r8 == 0) goto L_0x00eb
            r13 = 0
        L_0x00d8:
            if (r6 != 0) goto L_0x00df
            r0 = r22
            r0.addElement(r5)
        L_0x00df:
            r17 = 0
            r0 = r17
            r1 = r20
            r1.tokenBufferLength = r0
            goto L_0x0020
        L_0x00e9:
            r6 = 0
            goto L_0x00c8
        L_0x00eb:
            r13 = r12
            goto L_0x00d8
        L_0x00ed:
            boolean r17 = r20.checkNext(r21)
            if (r17 == 0) goto L_0x00f8
            r20.tokenBufferAppend(r21)
            goto L_0x0020
        L_0x00f8:
            r0 = r21
            if (r9 == r0) goto L_0x0104
            if (r9 < 0) goto L_0x0104
            r17 = 123(0x7b, float:1.72E-43)
            r0 = r17
            if (r9 != r0) goto L_0x01b9
        L_0x0104:
            r17 = 123(0x7b, float:1.72E-43)
            r0 = r17
            if (r9 != r0) goto L_0x0120
            int r10 = r20.read()
        L_0x010e:
            r17 = 123(0x7b, float:1.72E-43)
            r0 = r17
            if (r10 != r0) goto L_0x0122
            r17 = 123(0x7b, float:1.72E-43)
            r0 = r20
            r1 = r17
            r0.tokenBufferAppend(r1)
            r13 = 0
            goto L_0x0020
        L_0x0120:
            r10 = -1
            goto L_0x010e
        L_0x0122:
            r0 = r20
            int r0 = r0.tokenBufferLength
            r17 = r0
            if (r17 <= 0) goto L_0x017d
            if (r13 != 0) goto L_0x017d
            java.lang.String r16 = new java.lang.String
            r0 = r20
            char[] r0 = r0.tokenBuffer
            r17 = r0
            r18 = 0
            r0 = r20
            int r0 = r0.tokenBufferLength
            r19 = r0
            r16.<init>(r17, r18, r19)
        L_0x013f:
            r17 = 1
            r0 = r17
            gnu.expr.Expression[] r4 = new gnu.expr.Expression[r0]
            r17 = 0
            gnu.expr.QuoteExp r18 = new gnu.expr.QuoteExp
            r0 = r18
            r1 = r16
            r0.<init>(r1)
            r4[r17] = r18
            gnu.expr.ApplyExp r17 = new gnu.expr.ApplyExp
            gnu.expr.Expression r18 = makeText
            r0 = r17
            r1 = r18
            r0.<init>((gnu.expr.Expression) r1, (gnu.expr.Expression[]) r4)
            r0 = r22
            r1 = r17
            r0.addElement(r1)
        L_0x0164:
            r17 = 0
            r0 = r17
            r1 = r20
            r1.tokenBufferLength = r0
            r0 = r21
            if (r9 == r0) goto L_0x008c
            if (r9 >= 0) goto L_0x018e
            java.lang.String r17 = "unexpected end-of-file"
            r0 = r20
            r1 = r17
            r0.eofError(r1)
            goto L_0x0020
        L_0x017d:
            r17 = 123(0x7b, float:1.72E-43)
            r0 = r17
            if (r9 != r0) goto L_0x0164
            int r17 = r22.size()
            r0 = r17
            if (r11 != r0) goto L_0x0164
            java.lang.String r16 = ""
            goto L_0x013f
        L_0x018e:
            r0 = r20
            r0.unread(r10)
            r0 = r20
            int r0 = r0.enclosedExpressionsSeen
            r17 = r0
            int r17 = r17 + 1
            r0 = r17
            r1 = r20
            r1.enclosedExpressionsSeen = r0
            gnu.expr.Expression r7 = r20.parseEnclosedExpr()
            r0 = r22
            r0.addElement(r7)
            r17 = 0
            r0 = r17
            r1 = r20
            r1.tokenBufferLength = r0
            int r11 = r22.size()
            r13 = r12
            goto L_0x0020
        L_0x01b9:
            r17 = 125(0x7d, float:1.75E-43)
            r0 = r17
            if (r9 != r0) goto L_0x01e5
            int r9 = r20.read()
            r17 = 125(0x7d, float:1.75E-43)
            r0 = r17
            if (r9 != r0) goto L_0x01d5
            r17 = 125(0x7d, float:1.75E-43)
            r0 = r20
            r1 = r17
            r0.tokenBufferAppend(r1)
            r13 = 0
            goto L_0x0020
        L_0x01d5:
            java.lang.String r17 = "unexpected '}' in element content"
            r0 = r20
            r1 = r17
            r0.error(r1)
            r0 = r20
            r0.unread(r9)
            goto L_0x0020
        L_0x01e5:
            r17 = 38
            r0 = r17
            if (r9 != r0) goto L_0x01f1
            r20.parseEntityOrCharRef()
            r13 = 0
            goto L_0x0020
        L_0x01f1:
            r17 = 60
            r0 = r21
            r1 = r17
            if (r0 == r1) goto L_0x020d
            r17 = 9
            r0 = r17
            if (r9 == r0) goto L_0x020b
            r17 = 10
            r0 = r17
            if (r9 == r0) goto L_0x020b
            r17 = 13
            r0 = r17
            if (r9 != r0) goto L_0x020d
        L_0x020b:
            r9 = 32
        L_0x020d:
            r17 = 60
            r0 = r17
            if (r9 != r0) goto L_0x0220
            r17 = 101(0x65, float:1.42E-43)
            java.lang.String r18 = "'<' must be quoted in a direct attribute value"
            r0 = r20
            r1 = r17
            r2 = r18
            r0.error(r1, r2)
        L_0x0220:
            if (r13 == 0) goto L_0x0229
            char r0 = (char) r9
            r17 = r0
            boolean r13 = java.lang.Character.isWhitespace(r17)
        L_0x0229:
            char r0 = (char) r9
            r17 = r0
            r0 = r20
            r1 = r17
            r0.tokenBufferAppend(r1)
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.lang.XQParser.parseContent(char, java.util.Vector):void");
    }

    /* access modifiers changed from: package-private */
    public Expression parseEnclosedExpr() throws IOException, SyntaxException {
        String saveErrorIfComment = this.errorIfComment;
        this.errorIfComment = null;
        char saveReadState = pushNesting('{');
        peekNonSpace("unexpected end-of-file after '{'");
        int startLine = getLineNumber() + 1;
        int startColumn = getColumnNumber() + 1;
        getRawToken();
        Expression exp = parseExpr();
        while (true) {
            if (this.curToken == 125) {
                break;
            } else if (this.curToken == -1 || this.curToken == 41 || this.curToken == 93) {
                exp = syntaxError("missing '}'");
            } else {
                if (this.curToken != 44) {
                    exp = syntaxError("missing '}' or ','");
                } else {
                    getRawToken();
                }
                exp = makeExprSequence(exp, parseExpr());
            }
        }
        exp = syntaxError("missing '}'");
        maybeSetLine(exp, startLine, startColumn);
        popNesting(saveReadState);
        this.errorIfComment = saveErrorIfComment;
        return exp;
    }

    public static Expression booleanValue(Expression exp) {
        return new ApplyExp(makeFunctionExp("gnu.xquery.util.BooleanValue", "booleanValue"), exp);
    }

    /* access modifiers changed from: package-private */
    public Expression parseXMLConstructor(int next, boolean inElementContent) throws IOException, SyntaxException {
        if (next == 33) {
            int next2 = read();
            if (next2 == 45 && peek() == 45) {
                skip();
                getDelimited("-->");
                boolean bad = false;
                int i = this.tokenBufferLength;
                boolean sawHyphen = true;
                while (true) {
                    i--;
                    if (i >= 0) {
                        boolean curHyphen = this.tokenBuffer[i] == '-';
                        if (sawHyphen && curHyphen) {
                            bad = true;
                            break;
                        }
                        sawHyphen = curHyphen;
                    } else {
                        break;
                    }
                }
                if (bad) {
                    return syntaxError("consecutive or final hyphen in XML comment");
                }
                return new ApplyExp(makeFunctionExp("gnu.kawa.xml.CommentConstructor", "commentConstructor"), new QuoteExp(new String(this.tokenBuffer, 0, this.tokenBufferLength)));
            } else if (next2 != 91 || read() != 67 || read() != 68 || read() != 65 || read() != 84 || read() != 65 || read() != 91) {
                return syntaxError("'<!' must be followed by '--' or '[CDATA['");
            } else {
                if (!inElementContent) {
                    error('e', "CDATA section must be in element content");
                }
                getDelimited("]]>");
                return new ApplyExp(makeCDATA, new QuoteExp(new String(this.tokenBuffer, 0, this.tokenBufferLength)));
            }
        } else if (next == 63) {
            int next3 = peek();
            if (next3 < 0 || !XName.isNameStart((char) next3) || getRawToken() != 65) {
                syntaxError("missing target after '<?'");
            }
            String target = new String(this.tokenBuffer, 0, this.tokenBufferLength);
            int nspaces = 0;
            while (true) {
                int ch = read();
                if (ch < 0) {
                    break;
                } else if (!Character.isWhitespace((char) ch)) {
                    unread();
                    break;
                } else {
                    nspaces++;
                }
            }
            getDelimited("?>");
            if (nspaces == 0 && this.tokenBufferLength > 0) {
                syntaxError("target must be followed by space or '?>'");
            }
            return new ApplyExp(makeFunctionExp("gnu.kawa.xml.MakeProcInst", "makeProcInst"), new QuoteExp(target), new QuoteExp(new String(this.tokenBuffer, 0, this.tokenBufferLength)));
        } else if (next < 0 || !XName.isNameStart((char) next)) {
            return syntaxError("expected QName after '<'");
        } else {
            unread(next);
            getRawToken();
            char saveReadState = pushNesting('<');
            Expression exp = parseElementConstructor();
            if (!inElementContent) {
                exp = wrapWithBaseUri(exp);
            }
            popNesting(saveReadState);
            return exp;
        }
    }

    static ApplyExp castQName(Expression value, boolean element) {
        return new ApplyExp((Expression) new ReferenceExp(element ? XQResolveNames.xsQNameDecl : XQResolveNames.xsQNameIgnoreDefaultDecl), value);
    }

    /* access modifiers changed from: package-private */
    public Expression parseElementConstructor() throws IOException, SyntaxException {
        int ch;
        ApplyExp ax;
        String str = new String(this.tokenBuffer, 0, this.tokenBufferLength);
        Vector vec = new Vector();
        vec.addElement(castQName(new QuoteExp(str), true));
        this.errorIfComment = "comment not allowed in element start tag";
        NamespaceBinding nsBindings = null;
        while (true) {
            boolean sawSpace = false;
            ch = read();
            while (ch >= 0 && Character.isWhitespace((char) ch)) {
                ch = read();
                sawSpace = true;
            }
            if (ch >= 0 && ch != 62 && ch != 47) {
                if (!sawSpace) {
                    syntaxError("missing space before attribute");
                }
                unread(ch);
                getRawToken();
                int vecSize = vec.size();
                if (this.curToken != 65 && this.curToken != 81) {
                    break;
                }
                String attrName = new String(this.tokenBuffer, 0, this.tokenBufferLength);
                int startLine = getLineNumber() + 1;
                int startColumn = (getColumnNumber() + 1) - this.tokenBufferLength;
                String definingNamespace = null;
                if (this.curToken == 65) {
                    if (attrName.equals("xmlns")) {
                        definingNamespace = "";
                    }
                } else if (attrName.startsWith("xmlns:")) {
                    definingNamespace = attrName.substring(6).intern();
                }
                Expression makeAttr = definingNamespace != null ? null : MakeAttribute.makeAttributeExp;
                vec.addElement(castQName(new QuoteExp(attrName), false));
                if (skipSpace() != 61) {
                    this.errorIfComment = null;
                    return syntaxError("missing '=' after attribute");
                }
                int ch2 = skipSpace();
                int enclosedExpressionsStart = this.enclosedExpressionsSeen;
                if (ch2 == 123) {
                    warnOldVersion("enclosed attribute value expression should be quoted");
                    vec.addElement(parseEnclosedExpr());
                } else {
                    parseContent((char) ch2, vec);
                }
                int n = vec.size() - vecSize;
                if (definingNamespace != null) {
                    String ns = "";
                    if (n == 1) {
                        ns = "";
                    } else if (this.enclosedExpressionsSeen > enclosedExpressionsStart) {
                        syntaxError("enclosed expression not allowed in namespace declaration");
                    } else {
                        Object x = vec.elementAt(vecSize + 1);
                        if ((x instanceof ApplyExp) && (ax = (ApplyExp) x).getFunction() == makeText) {
                            x = ax.getArg(0);
                        }
                        ns = ((QuoteExp) x).getValue().toString().intern();
                    }
                    vec.setSize(vecSize);
                    checkAllowedNamespaceDeclaration(definingNamespace, ns, true);
                    if (definingNamespace == "") {
                        definingNamespace = null;
                    }
                    NamespaceBinding nsb = nsBindings;
                    while (true) {
                        if (nsb == null) {
                            break;
                        } else if (nsb.getPrefix() == definingNamespace) {
                            error('e', definingNamespace == null ? "duplicate default namespace declaration" : "duplicate namespace prefix '" + definingNamespace + '\'', "XQST0071");
                        } else {
                            nsb = nsb.getNext();
                        }
                    }
                    if (ns == "") {
                        ns = null;
                    }
                    nsBindings = new NamespaceBinding(definingNamespace, ns, nsBindings);
                } else {
                    Expression[] args = new Expression[n];
                    int i = n;
                    while (true) {
                        i--;
                        if (i < 0) {
                            break;
                        }
                        args[i] = (Expression) vec.elementAt(vecSize + i);
                    }
                    vec.setSize(vecSize);
                    ApplyExp aexp = new ApplyExp(makeAttr, args);
                    maybeSetLine((Expression) aexp, startLine, startColumn);
                    vec.addElement(aexp);
                }
            } else {
                break;
            }
        }
        this.errorIfComment = null;
        boolean empty = false;
        if (ch == 47) {
            ch = read();
            if (ch == 62) {
                empty = true;
            } else {
                unread(ch);
            }
        }
        if (!empty) {
            if (ch != 62) {
                return syntaxError("missing '>' after start element");
            }
            parseContent('<', vec);
            int ch3 = peek();
            if (ch3 >= 0) {
                if (!XName.isNameStart((char) ch3)) {
                    return syntaxError("invalid tag syntax after '</'");
                }
                getRawToken();
                String str2 = new String(this.tokenBuffer, 0, this.tokenBufferLength);
                if (!str2.equals(str)) {
                    return syntaxError("'<" + str + ">' closed by '</" + str2 + ">'");
                }
                this.errorIfComment = "comment not allowed in element end tag";
                ch3 = skipSpace();
                this.errorIfComment = null;
            }
            if (ch3 != 62) {
                return syntaxError("missing '>' after end element");
            }
        }
        Expression[] args2 = new Expression[vec.size()];
        vec.copyInto(args2);
        MakeElement mkElement = new MakeElement();
        mkElement.copyNamespacesMode = this.copyNamespacesMode;
        mkElement.setNamespaceNodes(nsBindings);
        return new ApplyExp((Expression) new QuoteExp(mkElement), args2);
    }

    /* access modifiers changed from: package-private */
    public Expression wrapWithBaseUri(Expression exp) {
        if (getStaticBaseUri() == null) {
            return exp;
        }
        return new ApplyExp((Procedure) MakeWithBaseUri.makeWithBaseUri, new ApplyExp((Expression) new ReferenceExp(XQResolveNames.staticBaseUriDecl), Expression.noExpressions), exp).setLine(exp);
    }

    /* access modifiers changed from: package-private */
    public Expression parseParenExpr() throws IOException, SyntaxException {
        getRawToken();
        char saveReadState = pushNesting('(');
        Expression exp = parseExprSequence(41, true);
        popNesting(saveReadState);
        if (this.curToken == -1) {
            eofError("missing ')' - unexpected end-of-file");
        }
        return exp;
    }

    /* access modifiers changed from: package-private */
    public Expression parseExprSequence(int rightToken, boolean optional) throws IOException, SyntaxException {
        String str;
        if (this.curToken == rightToken || this.curToken == -1) {
            if (!optional) {
                syntaxError("missing expression");
            }
            return QuoteExp.voidExp;
        }
        Expression exp = null;
        while (true) {
            Expression exp1 = parseExprSingle();
            exp = exp == null ? exp1 : makeExprSequence(exp, exp1);
            if (this.curToken == rightToken || this.curToken == -1) {
                return exp;
            }
            if (this.nesting == 0 && this.curToken == 10) {
                return exp;
            }
            if (this.curToken != 44) {
                if (rightToken == 41) {
                    str = "expected ')'";
                } else {
                    str = "confused by syntax error";
                }
                return syntaxError(str);
            }
            getRawToken();
        }
    }

    /* access modifiers changed from: package-private */
    public Expression parseTypeSwitch() throws IOException, SyntaxException {
        Declaration decl;
        Declaration decl2;
        char c = 'e';
        char save = pushNesting('t');
        Expression selector = parseParenExpr();
        getRawToken();
        Vector vec = new Vector();
        vec.addElement(selector);
        while (match("case")) {
            pushNesting('c');
            getRawToken();
            if (this.curToken == 36) {
                decl2 = parseVariableDeclaration();
                if (decl2 == null) {
                    return syntaxError("missing Variable after '$'");
                }
                getRawToken();
                if (match("as")) {
                    getRawToken();
                } else {
                    error('e', "missing 'as'");
                }
            } else {
                decl2 = new Declaration((Object) "(arg)");
            }
            decl2.setTypeExp(parseDataType());
            popNesting('t');
            LambdaExp lexp = new LambdaExp(1);
            lexp.addDeclaration(decl2);
            if (match("return")) {
                getRawToken();
            } else {
                error("missing 'return' after 'case'");
            }
            this.comp.push((ScopeExp) lexp);
            pushNesting('r');
            lexp.body = parseExpr();
            popNesting('t');
            this.comp.pop(lexp);
            vec.addElement(lexp);
        }
        if (match("default")) {
            LambdaExp lexp2 = new LambdaExp(1);
            getRawToken();
            if (this.curToken == 36) {
                decl = parseVariableDeclaration();
                if (decl == null) {
                    return syntaxError("missing Variable after '$'");
                }
                getRawToken();
            } else {
                decl = new Declaration((Object) "(arg)");
            }
            lexp2.addDeclaration(decl);
            if (match("return")) {
                getRawToken();
            } else {
                error("missing 'return' after 'default'");
            }
            this.comp.push((ScopeExp) lexp2);
            lexp2.body = parseExpr();
            this.comp.pop(lexp2);
            vec.addElement(lexp2);
        } else {
            if (!this.comp.isPedantic()) {
                c = 'w';
            }
            error(c, "no 'default' clause in 'typeswitch'", "XPST0003");
        }
        popNesting(save);
        Expression[] args = new Expression[vec.size()];
        vec.copyInto(args);
        return new ApplyExp(makeFunctionExp("gnu.kawa.reflect.TypeSwitch", "typeSwitch"), args);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v0, resolved type: gnu.expr.Expression} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v1, resolved type: gnu.expr.Expression} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v3, resolved type: gnu.expr.QuoteExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v4, resolved type: gnu.expr.QuoteExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v192, resolved type: gnu.expr.QuoteExp} */
    /* JADX WARNING: type inference failed for: r25v2 */
    /* JADX WARNING: type inference failed for: r27v1 */
    /* JADX WARNING: type inference failed for: r0v193, types: [java.lang.Double] */
    /* JADX WARNING: type inference failed for: r0v194, types: [java.math.BigDecimal] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x00b9 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression parseMaybePrimaryExpr() throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r34 = this;
            r0 = r34
            int r0 = r0.curLine
            r23 = r0
            r0 = r34
            int r0 = r0.curColumn
            r22 = r0
            int r26 = r34.peekOperand()
            switch(r26) {
                case 34: goto L_0x0225;
                case 36: goto L_0x02cb;
                case 40: goto L_0x0015;
                case 48: goto L_0x0245;
                case 49: goto L_0x0264;
                case 50: goto L_0x0264;
                case 70: goto L_0x02f9;
                case 123: goto L_0x015b;
                case 197: goto L_0x001d;
                case 249: goto L_0x05ce;
                case 250: goto L_0x05ce;
                case 251: goto L_0x03a3;
                case 252: goto L_0x03a3;
                case 253: goto L_0x03a3;
                case 254: goto L_0x03a3;
                case 255: goto L_0x03a3;
                case 256: goto L_0x03a3;
                case 404: goto L_0x016a;
                default: goto L_0x0013;
            }
        L_0x0013:
            r9 = 0
        L_0x0014:
            return r9
        L_0x0015:
            gnu.expr.Expression r9 = r34.parseParenExpr()
        L_0x0019:
            r34.getRawToken()
            goto L_0x0014
        L_0x001d:
            java.util.Stack r10 = new java.util.Stack
            r10.<init>()
        L_0x0022:
            r34.getRawToken()
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 81
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x00a1
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 65
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x00a1
            java.lang.String r29 = "missing pragma name"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r16 = r0.syntaxError(r1)
        L_0x004b:
            r0 = r16
            r10.push(r0)
            java.lang.StringBuffer r20 = new java.lang.StringBuffer
            r20.<init>()
            r21 = -1
        L_0x0057:
            int r6 = r34.read()
            int r21 = r21 + 1
            if (r6 < 0) goto L_0x0068
            char r0 = (char) r6
            r29 = r0
            boolean r29 = java.lang.Character.isWhitespace(r29)
            if (r29 != 0) goto L_0x0057
        L_0x0068:
            r29 = 35
            r0 = r29
            if (r6 != r0) goto L_0x007a
            int r29 = r34.peek()
            r30 = 41
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x00b9
        L_0x007a:
            if (r6 >= 0) goto L_0x0085
            java.lang.String r29 = "pragma ended by end-of-file"
            r0 = r34
            r1 = r29
            r0.eofError(r1)
        L_0x0085:
            if (r21 != 0) goto L_0x0090
            java.lang.String r29 = "missing space between pragma and extension content"
            r0 = r34
            r1 = r29
            r0.error(r1)
        L_0x0090:
            r21 = 1
            char r0 = (char) r6
            r29 = r0
            r0 = r20
            r1 = r29
            r0.append(r1)
            int r6 = r34.read()
            goto L_0x0068
        L_0x00a1:
            java.lang.String r29 = new java.lang.String
            r0 = r34
            char[] r0 = r0.tokenBuffer
            r30 = r0
            r31 = 0
            r0 = r34
            int r0 = r0.tokenBufferLength
            r32 = r0
            r29.<init>(r30, r31, r32)
            gnu.expr.QuoteExp r16 = gnu.expr.QuoteExp.getInstance(r29)
            goto L_0x004b
        L_0x00b9:
            r34.read()
            java.lang.String r29 = r20.toString()
            gnu.expr.QuoteExp r29 = gnu.expr.QuoteExp.getInstance(r29)
            r0 = r29
            r10.push(r0)
            r34.getRawToken()
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 197(0xc5, float:2.76E-43)
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x0022
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 123(0x7b, float:1.72E-43)
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x014f
            r34.getRawToken()
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 125(0x7d, float:1.75E-43)
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x0134
            r29 = 123(0x7b, float:1.72E-43)
            r0 = r34
            r1 = r29
            char r19 = r0.pushNesting(r1)
            r29 = 125(0x7d, float:1.75E-43)
            r30 = 0
            r0 = r34
            r1 = r29
            r2 = r30
            gnu.expr.Expression r29 = r0.parseExprSequence(r1, r2)
            r0 = r29
            r10.push(r0)
            r0 = r34
            r1 = r19
            r0.popNesting(r1)
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = -1
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x0134
            java.lang.String r29 = "missing '}' - unexpected end-of-file"
            r0 = r34
            r1 = r29
            r0.eofError(r1)
        L_0x0134:
            int r29 = r10.size()
            r0 = r29
            gnu.expr.Expression[] r5 = new gnu.expr.Expression[r0]
            r10.copyInto(r5)
            gnu.expr.ApplyExp r9 = new gnu.expr.ApplyExp
            gnu.expr.ReferenceExp r29 = new gnu.expr.ReferenceExp
            gnu.expr.Declaration r30 = gnu.xquery.lang.XQResolveNames.handleExtensionDecl
            r29.<init>((gnu.expr.Declaration) r30)
            r0 = r29
            r9.<init>((gnu.expr.Expression) r0, (gnu.expr.Expression[]) r5)
            goto L_0x0019
        L_0x014f:
            java.lang.String r29 = "missing '{' after pragma"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            goto L_0x0019
        L_0x015b:
            java.lang.String r29 = "saw unexpected '{' - assume you meant '('"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            r34.parseEnclosedExpr()
            goto L_0x0019
        L_0x016a:
            int r15 = r34.read()
            r29 = 47
            r0 = r29
            if (r15 != r0) goto L_0x0210
            r34.getRawToken()
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 65
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x01a1
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 81
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x01a1
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 67
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x020d
        L_0x01a1:
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            java.lang.String r30 = "saw end tag '</"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r30 = new java.lang.String
            r0 = r34
            char[] r0 = r0.tokenBuffer
            r31 = r0
            r32 = 0
            r0 = r34
            int r0 = r0.tokenBufferLength
            r33 = r0
            r30.<init>(r31, r32, r33)
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r30 = ">' not in an element constructor"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r13 = r29.toString()
        L_0x01cd:
            r0 = r23
            r1 = r34
            r1.curLine = r0
            r0 = r22
            r1 = r34
            r1.curColumn = r0
            r0 = r34
            gnu.expr.Expression r9 = r0.syntaxError(r13)
        L_0x01df:
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 405(0x195, float:5.68E-43)
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x0014
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = -1
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x0014
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 10
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x0014
            r34.getRawToken()
            goto L_0x01df
        L_0x020d:
            java.lang.String r13 = "saw end tag '</' not in an element constructor"
            goto L_0x01cd
        L_0x0210:
            r29 = 0
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.parseXMLConstructor(r15, r1)
            r0 = r34
            r1 = r23
            r2 = r22
            r0.maybeSetLine((gnu.expr.Expression) r9, (int) r1, (int) r2)
            goto L_0x0019
        L_0x0225:
            gnu.expr.QuoteExp r9 = new gnu.expr.QuoteExp
            java.lang.String r29 = new java.lang.String
            r0 = r34
            char[] r0 = r0.tokenBuffer
            r30 = r0
            r31 = 0
            r0 = r34
            int r0 = r0.tokenBufferLength
            r32 = r0
            r29.<init>(r30, r31, r32)
            java.lang.String r29 = r29.intern()
            r0 = r29
            r9.<init>(r0)
            goto L_0x0019
        L_0x0245:
            gnu.expr.QuoteExp r9 = new gnu.expr.QuoteExp
            r0 = r34
            char[] r0 = r0.tokenBuffer
            r29 = r0
            r30 = 0
            r0 = r34
            int r0 = r0.tokenBufferLength
            r31 = r0
            r32 = 10
            r33 = 0
            gnu.math.IntNum r29 = gnu.math.IntNum.valueOf(r29, r30, r31, r32, r33)
            r0 = r29
            r9.<init>(r0)
            goto L_0x0019
        L_0x0264:
            java.lang.String r24 = new java.lang.String
            r0 = r34
            char[] r0 = r0.tokenBuffer
            r29 = r0
            r30 = 0
            r0 = r34
            int r0 = r0.tokenBufferLength
            r31 = r0
            r0 = r24
            r1 = r29
            r2 = r30
            r3 = r31
            r0.<init>(r1, r2, r3)
            r29 = 49
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x0299
            java.math.BigDecimal r27 = new java.math.BigDecimal     // Catch:{ Throwable -> 0x02a3 }
            r0 = r27
            r1 = r24
            r0.<init>(r1)     // Catch:{ Throwable -> 0x02a3 }
        L_0x0290:
            gnu.expr.QuoteExp r9 = new gnu.expr.QuoteExp     // Catch:{ Throwable -> 0x02a3 }
            r0 = r27
            r9.<init>(r0)     // Catch:{ Throwable -> 0x02a3 }
            goto L_0x0019
        L_0x0299:
            java.lang.Double r27 = new java.lang.Double     // Catch:{ Throwable -> 0x02a3 }
            r0 = r27
            r1 = r24
            r0.<init>(r1)     // Catch:{ Throwable -> 0x02a3 }
            goto L_0x0290
        L_0x02a3:
            r8 = move-exception
            java.lang.StringBuilder r29 = new java.lang.StringBuilder
            r29.<init>()
            java.lang.String r30 = "invalid decimal literal: '"
            java.lang.StringBuilder r29 = r29.append(r30)
            r0 = r29
            r1 = r24
            java.lang.StringBuilder r29 = r0.append(r1)
            java.lang.String r30 = "'"
            java.lang.StringBuilder r29 = r29.append(r30)
            java.lang.String r29 = r29.toString()
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            goto L_0x0019
        L_0x02cb:
            java.lang.Object r14 = r34.parseVariable()
            if (r14 != 0) goto L_0x02dd
            java.lang.String r29 = "missing Variable"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            goto L_0x0014
        L_0x02dd:
            gnu.expr.ReferenceExp r9 = new gnu.expr.ReferenceExp
            r9.<init>((java.lang.Object) r14)
            r0 = r34
            int r0 = r0.curLine
            r29 = r0
            r0 = r34
            int r0 = r0.curColumn
            r30 = r0
            r0 = r34
            r1 = r29
            r2 = r30
            r0.maybeSetLine((gnu.expr.Expression) r9, (int) r1, (int) r2)
            goto L_0x0019
        L_0x02f9:
            java.lang.String r14 = new java.lang.String
            r0 = r34
            char[] r0 = r0.tokenBuffer
            r29 = r0
            r30 = 0
            r0 = r34
            int r0 = r0.tokenBufferLength
            r31 = r0
            r0 = r29
            r1 = r30
            r2 = r31
            r14.<init>(r0, r1, r2)
            r29 = 40
            r0 = r34
            r1 = r29
            char r18 = r0.pushNesting(r1)
            r34.getRawToken()
            java.util.Vector r28 = new java.util.Vector
            r29 = 10
            r28.<init>(r29)
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 41
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x034b
        L_0x0334:
            gnu.expr.Expression r4 = r34.parseExpr()
            r0 = r28
            r0.addElement(r4)
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 41
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x0385
        L_0x034b:
            int r29 = r28.size()
            r0 = r29
            gnu.expr.Expression[] r5 = new gnu.expr.Expression[r0]
            r0 = r28
            r0.copyInto(r5)
            gnu.expr.ReferenceExp r17 = new gnu.expr.ReferenceExp
            r29 = 0
            r0 = r17
            r1 = r29
            r0.<init>(r14, r1)
            r29 = 1
            r0 = r17
            r1 = r29
            r0.setProcedureName(r1)
            gnu.expr.ApplyExp r9 = new gnu.expr.ApplyExp
            r0 = r17
            r9.<init>((gnu.expr.Expression) r0, (gnu.expr.Expression[]) r5)
            r0 = r34
            r1 = r23
            r2 = r22
            r0.maybeSetLine((gnu.expr.Expression) r9, (int) r1, (int) r2)
            r0 = r34
            r1 = r18
            r0.popNesting(r1)
            goto L_0x0019
        L_0x0385:
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 44
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x039f
            java.lang.String r29 = "missing ')' after function call"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            goto L_0x0014
        L_0x039f:
            r34.getRawToken()
            goto L_0x0334
        L_0x03a3:
            r34.getRawToken()
            java.util.Vector r28 = new java.util.Vector
            r28.<init>()
            r29 = 251(0xfb, float:3.52E-43)
            r0 = r26
            r1 = r29
            if (r0 == r1) goto L_0x03bb
            r29 = 252(0xfc, float:3.53E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x0470
        L_0x03bb:
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 65
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x03d7
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 81
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x044b
        L_0x03d7:
            r29 = 251(0xfb, float:3.52E-43)
            r0 = r26
            r1 = r29
            if (r0 == r1) goto L_0x0448
            r29 = 1
        L_0x03e1:
            r0 = r34
            r1 = r29
            gnu.expr.Expression r7 = r0.parseNameTest(r1)
        L_0x03e9:
            r29 = 251(0xfb, float:3.52E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x046a
            r29 = 1
        L_0x03f3:
            r0 = r29
            gnu.expr.ApplyExp r29 = castQName(r7, r0)
            r28.addElement(r29)
            r29 = 251(0xfb, float:3.52E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x046d
            gnu.kawa.xml.MakeElement r12 = new gnu.kawa.xml.MakeElement
            r12.<init>()
            r0 = r34
            int r0 = r0.copyNamespacesMode
            r29 = r0
            r0 = r29
            r12.copyNamespacesMode = r0
            gnu.expr.QuoteExp r11 = new gnu.expr.QuoteExp
            r11.<init>(r12)
        L_0x0418:
            r34.getRawToken()
        L_0x041b:
            r29 = 123(0x7b, float:1.72E-43)
            r0 = r34
            r1 = r29
            char r19 = r0.pushNesting(r1)
            java.lang.String r29 = "unexpected end-of-file after '{'"
            r0 = r34
            r1 = r29
            r0.peekNonSpace(r1)
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 123(0x7b, float:1.72E-43)
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x0515
            java.lang.String r29 = "missing '{'"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            goto L_0x0014
        L_0x0448:
            r29 = 0
            goto L_0x03e1
        L_0x044b:
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 123(0x7b, float:1.72E-43)
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x045e
            gnu.expr.Expression r7 = r34.parseEnclosedExpr()
            goto L_0x03e9
        L_0x045e:
            java.lang.String r29 = "missing element/attribute name"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            goto L_0x0014
        L_0x046a:
            r29 = 0
            goto L_0x03f3
        L_0x046d:
            gnu.expr.QuoteExp r11 = gnu.kawa.xml.MakeAttribute.makeAttributeExp
            goto L_0x0418
        L_0x0470:
            r29 = 256(0x100, float:3.59E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x0481
            java.lang.String r29 = "gnu.kawa.xml.DocumentConstructor"
            java.lang.String r30 = "documentConstructor"
            gnu.expr.Expression r11 = makeFunctionExp(r29, r30)
            goto L_0x041b
        L_0x0481:
            r29 = 254(0xfe, float:3.56E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x0492
            java.lang.String r29 = "gnu.kawa.xml.CommentConstructor"
            java.lang.String r30 = "commentConstructor"
            gnu.expr.Expression r11 = makeFunctionExp(r29, r30)
            goto L_0x041b
        L_0x0492:
            r29 = 255(0xff, float:3.57E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x050b
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 65
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x04dc
            gnu.expr.QuoteExp r25 = new gnu.expr.QuoteExp
            java.lang.String r29 = new java.lang.String
            r0 = r34
            char[] r0 = r0.tokenBuffer
            r30 = r0
            r31 = 0
            r0 = r34
            int r0 = r0.tokenBufferLength
            r32 = r0
            r29.<init>(r30, r31, r32)
            java.lang.String r29 = r29.intern()
            r0 = r25
            r1 = r29
            r0.<init>(r1)
        L_0x04c8:
            r0 = r28
            r1 = r25
            r0.addElement(r1)
            java.lang.String r29 = "gnu.kawa.xml.MakeProcInst"
            java.lang.String r30 = "makeProcInst"
            gnu.expr.Expression r11 = makeFunctionExp(r29, r30)
            r34.getRawToken()
            goto L_0x041b
        L_0x04dc:
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 123(0x7b, float:1.72E-43)
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x04ef
            gnu.expr.Expression r25 = r34.parseEnclosedExpr()
            goto L_0x04c8
        L_0x04ef:
            java.lang.String r29 = "expected NCName or '{' after 'processing-instruction'"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r25 = r0.syntaxError(r1)
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 81
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x04c8
            r9 = r25
            goto L_0x0014
        L_0x050b:
            java.lang.String r29 = "gnu.kawa.xml.MakeText"
            java.lang.String r30 = "makeText"
            gnu.expr.Expression r11 = makeFunctionExp(r29, r30)
            goto L_0x041b
        L_0x0515:
            r34.getRawToken()
            r29 = 253(0xfd, float:3.55E-43)
            r0 = r26
            r1 = r29
            if (r0 == r1) goto L_0x0530
            r29 = 254(0xfe, float:3.56E-43)
            r0 = r26
            r1 = r29
            if (r0 == r1) goto L_0x0530
            r29 = 255(0xff, float:3.57E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x056d
        L_0x0530:
            r30 = 125(0x7d, float:1.75E-43)
            r29 = 255(0xff, float:3.57E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x056a
            r29 = 1
        L_0x053c:
            r0 = r34
            r1 = r30
            r2 = r29
            gnu.expr.Expression r29 = r0.parseExprSequence(r1, r2)
            r28.addElement(r29)
        L_0x0549:
            r0 = r34
            r1 = r19
            r0.popNesting(r1)
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 125(0x7d, float:1.75E-43)
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x059b
            java.lang.String r29 = "missing '}'"
            r0 = r34
            r1 = r29
            gnu.expr.Expression r9 = r0.syntaxError(r1)
            goto L_0x0014
        L_0x056a:
            r29 = 0
            goto L_0x053c
        L_0x056d:
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 125(0x7d, float:1.75E-43)
            r0 = r29
            r1 = r30
            if (r0 == r1) goto L_0x0549
            gnu.expr.Expression r29 = r34.parseExpr()
            r28.addElement(r29)
        L_0x0582:
            r0 = r34
            int r0 = r0.curToken
            r29 = r0
            r30 = 44
            r0 = r29
            r1 = r30
            if (r0 != r1) goto L_0x0549
            r34.getRawToken()
            gnu.expr.Expression r29 = r34.parseExpr()
            r28.addElement(r29)
            goto L_0x0582
        L_0x059b:
            int r29 = r28.size()
            r0 = r29
            gnu.expr.Expression[] r5 = new gnu.expr.Expression[r0]
            r0 = r28
            r0.copyInto(r5)
            gnu.expr.ApplyExp r9 = new gnu.expr.ApplyExp
            r9.<init>((gnu.expr.Expression) r11, (gnu.expr.Expression[]) r5)
            r0 = r34
            r1 = r23
            r2 = r22
            r0.maybeSetLine((gnu.expr.Expression) r9, (int) r1, (int) r2)
            r29 = 256(0x100, float:3.59E-43)
            r0 = r26
            r1 = r29
            if (r0 == r1) goto L_0x05c6
            r29 = 251(0xfb, float:3.52E-43)
            r0 = r26
            r1 = r29
            if (r0 != r1) goto L_0x0019
        L_0x05c6:
            r0 = r34
            gnu.expr.Expression r9 = r0.wrapWithBaseUri(r9)
            goto L_0x0019
        L_0x05ce:
            r34.getRawToken()
            r29 = 125(0x7d, float:1.75E-43)
            r30 = 0
            r0 = r34
            r1 = r29
            r2 = r30
            gnu.expr.Expression r9 = r0.parseExprSequence(r1, r2)
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.lang.XQParser.parseMaybePrimaryExpr():gnu.expr.Expression");
    }

    public Expression parseIfExpr() throws IOException, SyntaxException {
        char saveReadState1 = pushNesting('i');
        getRawToken();
        char saveReadState2 = pushNesting('(');
        Expression cond = parseExprSequence(41, false);
        popNesting(saveReadState2);
        if (this.curToken == -1) {
            eofError("missing ')' - unexpected end-of-file");
        }
        getRawToken();
        if (!match("then")) {
            syntaxError("missing 'then'");
        } else {
            getRawToken();
        }
        Expression thenPart = parseExpr();
        if (!match("else")) {
            syntaxError("missing 'else'");
        } else {
            getRawToken();
        }
        popNesting(saveReadState1);
        return new IfExp(booleanValue(cond), thenPart, parseExpr());
    }

    public boolean match(String word) {
        int len;
        if (this.curToken != 65 || this.tokenBufferLength != (len = word.length())) {
            return false;
        }
        int i = len;
        do {
            i--;
            if (i < 0) {
                return true;
            }
        } while (word.charAt(i) == this.tokenBuffer[i]);
        return false;
    }

    public Object parseVariable() throws IOException, SyntaxException {
        if (this.curToken == 36) {
            getRawToken();
        } else {
            syntaxError("missing '$' before variable name");
        }
        String str = new String(this.tokenBuffer, 0, this.tokenBufferLength);
        if (this.curToken == 81) {
            return str;
        }
        if (this.curToken == 65) {
            return Namespace.EmptyNamespace.getSymbol(str.intern());
        }
        return null;
    }

    public Declaration parseVariableDeclaration() throws IOException, SyntaxException {
        Object name = parseVariable();
        if (name == null) {
            return null;
        }
        Declaration decl = new Declaration(name);
        maybeSetLine(decl, getLineNumber() + 1, (getColumnNumber() + 1) - this.tokenBufferLength);
        return decl;
    }

    public Expression parseFLWRExpression(boolean isFor) throws IOException, SyntaxException {
        int flworDeclsSave = this.flworDeclsFirst;
        this.flworDeclsFirst = this.flworDeclsCount;
        Expression exp = parseFLWRInner(isFor);
        if (match("order")) {
            char saveNesting = pushNesting(isFor ? 'f' : 'l');
            getRawToken();
            if (match("by")) {
                getRawToken();
            } else {
                error("missing 'by' following 'order'");
            }
            Stack specs = new Stack();
            while (true) {
                boolean descending = false;
                char emptyOrder = this.defaultEmptyOrder;
                LambdaExp lexp = new LambdaExp(this.flworDeclsCount - this.flworDeclsFirst);
                for (int i = this.flworDeclsFirst; i < this.flworDeclsCount; i++) {
                    lexp.addDeclaration(this.flworDecls[i].getSymbol());
                }
                this.comp.push((ScopeExp) lexp);
                lexp.body = parseExprSingle();
                this.comp.pop(lexp);
                specs.push(lexp);
                if (match("ascending")) {
                    getRawToken();
                } else if (match("descending")) {
                    getRawToken();
                    descending = true;
                }
                if (match("empty")) {
                    getRawToken();
                    if (match("greatest")) {
                        getRawToken();
                        emptyOrder = 'G';
                    } else if (match("least")) {
                        getRawToken();
                        emptyOrder = 'L';
                    } else {
                        error("'empty' sequence order must be 'greatest' or 'least'");
                    }
                }
                specs.push(new QuoteExp((descending ? "D" : "A") + emptyOrder));
                NamedCollator collation = this.defaultCollator;
                if (match("collation")) {
                    Object uri = parseURILiteral();
                    if (uri instanceof String) {
                        try {
                            collation = NamedCollator.make(resolveAgainstBaseUri((String) uri));
                        } catch (Exception e) {
                            error('e', "unknown collation '" + uri + "'", "XQST0076");
                        }
                    }
                    getRawToken();
                }
                specs.push(new QuoteExp(collation));
                if (this.curToken != 44) {
                    break;
                }
                getRawToken();
            }
            if (!match("return")) {
                return syntaxError("expected 'return' clause");
            }
            getRawToken();
            LambdaExp lexp2 = new LambdaExp(this.flworDeclsCount - this.flworDeclsFirst);
            for (int i2 = this.flworDeclsFirst; i2 < this.flworDeclsCount; i2++) {
                lexp2.addDeclaration(this.flworDecls[i2].getSymbol());
            }
            popNesting(saveNesting);
            this.comp.push((ScopeExp) lexp2);
            lexp2.body = parseExprSingle();
            this.comp.pop(lexp2);
            int nspecs = specs.size();
            Expression[] args = new Expression[(nspecs + 2)];
            args[0] = exp;
            args[1] = lexp2;
            for (int i3 = 0; i3 < nspecs; i3++) {
                args[i3 + 2] = (Expression) specs.elementAt(i3);
            }
            return new ApplyExp(makeFunctionExp("gnu.xquery.util.OrderedMap", "orderedMap"), args);
        }
        this.flworDeclsCount = this.flworDeclsFirst;
        this.flworDeclsFirst = flworDeclsSave;
        return exp;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v0, resolved type: gnu.expr.LetExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v1, resolved type: gnu.expr.LetExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v2, resolved type: gnu.expr.ApplyExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v102, resolved type: gnu.expr.LambdaExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v7, resolved type: gnu.expr.LetExp} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression parseFLWRInner(boolean r33) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r32 = this;
            if (r33 == 0) goto L_0x003a
            r28 = 102(0x66, float:1.43E-43)
        L_0x0004:
            r0 = r32
            r1 = r28
            char r20 = r0.pushNesting(r1)
            r28 = 36
            r0 = r28
            r1 = r32
            r1.curToken = r0
            gnu.expr.Declaration r12 = r32.parseVariableDeclaration()
            if (r12 != 0) goto L_0x003d
            java.lang.StringBuilder r28 = new java.lang.StringBuilder
            r28.<init>()
            java.lang.String r29 = "missing Variable - saw "
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r29 = r32.tokenString()
            java.lang.StringBuilder r28 = r28.append(r29)
            java.lang.String r28 = r28.toString()
            r0 = r32
            r1 = r28
            gnu.expr.Expression r25 = r0.syntaxError(r1)
        L_0x0039:
            return r25
        L_0x003a:
            r28 = 108(0x6c, float:1.51E-43)
            goto L_0x0004
        L_0x003d:
            r0 = r32
            gnu.expr.Declaration[] r0 = r0.flworDecls
            r28 = r0
            if (r28 != 0) goto L_0x0164
            r28 = 8
            r0 = r28
            gnu.expr.Declaration[] r0 = new gnu.expr.Declaration[r0]
            r28 = r0
            r0 = r28
            r1 = r32
            r1.flworDecls = r0
        L_0x0053:
            r0 = r32
            gnu.expr.Declaration[] r0 = r0.flworDecls
            r28 = r0
            r0 = r32
            int r0 = r0.flworDeclsCount
            r29 = r0
            int r30 = r29 + 1
            r0 = r30
            r1 = r32
            r1.flworDeclsCount = r0
            r28[r29] = r12
            r32.getRawToken()
            gnu.expr.Expression r27 = r32.parseOptionalTypeDeclaration()
            r28 = 1
            r0 = r28
            gnu.expr.Expression[] r14 = new gnu.expr.Expression[r0]
            r18 = 0
            if (r33 == 0) goto L_0x01ce
            java.lang.String r28 = "at"
            r0 = r32
            r1 = r28
            boolean r21 = r0.match(r1)
            gnu.expr.LambdaExp r16 = new gnu.expr.LambdaExp
            if (r21 == 0) goto L_0x01ae
            r28 = 2
        L_0x008a:
            r0 = r16
            r1 = r28
            r0.<init>((int) r1)
            if (r21 == 0) goto L_0x00b6
            r32.getRawToken()
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 36
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x00ab
            gnu.expr.Declaration r18 = r32.parseVariableDeclaration()
            r32.getRawToken()
        L_0x00ab:
            if (r18 != 0) goto L_0x00b6
            java.lang.String r28 = "missing Variable after 'at'"
            r0 = r32
            r1 = r28
            r0.syntaxError(r1)
        L_0x00b6:
            r25 = r16
            java.lang.String r28 = "in"
            r0 = r32
            r1 = r28
            boolean r28 = r0.match(r1)
            if (r28 == 0) goto L_0x01b2
            r32.getRawToken()
        L_0x00c7:
            r28 = 0
            gnu.expr.Expression r29 = r32.parseExprSingle()
            r14[r28] = r29
            if (r27 == 0) goto L_0x00e3
            if (r33 != 0) goto L_0x00e3
            r28 = 0
            r29 = 0
            r29 = r14[r29]
            r0 = r29
            r1 = r27
            gnu.expr.ApplyExp r29 = gnu.expr.Compilation.makeCoercion((gnu.expr.Expression) r0, (gnu.expr.Expression) r1)
            r14[r28] = r29
        L_0x00e3:
            r0 = r32
            r1 = r20
            r0.popNesting(r1)
            r0 = r32
            gnu.expr.Compilation r0 = r0.comp
            r28 = r0
            r0 = r28
            r1 = r25
            r0.push((gnu.expr.ScopeExp) r1)
            r0 = r25
            r0.addDeclaration((gnu.expr.Declaration) r12)
            if (r27 == 0) goto L_0x0103
            r0 = r27
            r12.setTypeExp(r0)
        L_0x0103:
            if (r33 == 0) goto L_0x0114
            r28 = 0
            r0 = r28
            r12.noteValue(r0)
            r28 = 262144(0x40000, double:1.295163E-318)
            r0 = r28
            r12.setFlag(r0)
        L_0x0114:
            if (r18 == 0) goto L_0x0139
            r0 = r25
            r1 = r18
            r0.addDeclaration((gnu.expr.Declaration) r1)
            gnu.bytecode.PrimType r28 = gnu.kawa.lispexpr.LangPrimType.intType
            r0 = r18
            r1 = r28
            r0.setType(r1)
            r28 = 0
            r0 = r18
            r1 = r28
            r0.noteValue(r1)
            r28 = 262144(0x40000, double:1.295163E-318)
            r0 = r18
            r1 = r28
            r0.setFlag(r1)
        L_0x0139:
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 44
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x0253
            r32.getRawToken()
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 36
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x0201
            java.lang.String r28 = "missing $NAME after ','"
            r0 = r32
            r1 = r28
            gnu.expr.Expression r25 = r0.syntaxError(r1)
            goto L_0x0039
        L_0x0164:
            r0 = r32
            int r0 = r0.flworDeclsCount
            r28 = r0
            r0 = r32
            gnu.expr.Declaration[] r0 = r0.flworDecls
            r29 = r0
            r0 = r29
            int r0 = r0.length
            r29 = r0
            r0 = r28
            r1 = r29
            if (r0 < r1) goto L_0x0053
            r0 = r32
            int r0 = r0.flworDeclsCount
            r28 = r0
            int r28 = r28 * 2
            r0 = r28
            gnu.expr.Declaration[] r0 = new gnu.expr.Declaration[r0]
            r26 = r0
            r0 = r32
            gnu.expr.Declaration[] r0 = r0.flworDecls
            r28 = r0
            r29 = 0
            r30 = 0
            r0 = r32
            int r0 = r0.flworDeclsCount
            r31 = r0
            r0 = r28
            r1 = r29
            r2 = r26
            r3 = r30
            r4 = r31
            java.lang.System.arraycopy(r0, r1, r2, r3, r4)
            r0 = r26
            r1 = r32
            r1.flworDecls = r0
            goto L_0x0053
        L_0x01ae:
            r28 = 1
            goto L_0x008a
        L_0x01b2:
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 76
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x01c3
            r32.getRawToken()
        L_0x01c3:
            java.lang.String r28 = "missing 'in' in 'for' clause"
            r0 = r32
            r1 = r28
            r0.syntaxError(r1)
            goto L_0x00c7
        L_0x01ce:
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 76
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x01e8
            r32.getRawToken()
        L_0x01df:
            gnu.expr.LetExp r15 = new gnu.expr.LetExp
            r15.<init>(r14)
            r25 = r15
            goto L_0x00c7
        L_0x01e8:
            java.lang.String r28 = "in"
            r0 = r32
            r1 = r28
            boolean r28 = r0.match(r1)
            if (r28 == 0) goto L_0x01f7
            r32.getRawToken()
        L_0x01f7:
            java.lang.String r28 = "missing ':=' in 'let' clause"
            r0 = r32
            r1 = r28
            r0.syntaxError(r1)
            goto L_0x01df
        L_0x0201:
            gnu.expr.Expression r7 = r32.parseFLWRInner(r33)
        L_0x0205:
            r0 = r32
            gnu.expr.Compilation r0 = r0.comp
            r28 = r0
            r0 = r28
            r1 = r25
            r0.pop(r1)
            if (r33 == 0) goto L_0x03c5
            r16 = r25
            gnu.expr.LambdaExp r16 = (gnu.expr.LambdaExp) r16
            r0 = r16
            r0.body = r7
            r28 = 2
            r0 = r28
            gnu.expr.Expression[] r6 = new gnu.expr.Expression[r0]
            r28 = 0
            r6[r28] = r25
            r28 = 1
            r29 = 0
            r29 = r14[r29]
            r6[r28] = r29
            gnu.expr.ApplyExp r25 = new gnu.expr.ApplyExp
            java.lang.String r29 = "gnu.kawa.functions.ValuesMap"
            r0 = r16
            int r0 = r0.min_args
            r28 = r0
            r30 = 1
            r0 = r28
            r1 = r30
            if (r0 != r1) goto L_0x03c1
            java.lang.String r28 = "valuesMap"
        L_0x0242:
            r0 = r29
            r1 = r28
            gnu.expr.Expression r28 = makeFunctionExp(r0, r1)
            r0 = r25
            r1 = r28
            r0.<init>((gnu.expr.Expression) r1, (gnu.expr.Expression[]) r6)
            goto L_0x0039
        L_0x0253:
            java.lang.String r28 = "for"
            r0 = r32
            r1 = r28
            boolean r28 = r0.match(r1)
            if (r28 == 0) goto L_0x0288
            r32.getRawToken()
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 36
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x027c
            java.lang.String r28 = "missing $NAME after 'for'"
            r0 = r32
            r1 = r28
            gnu.expr.Expression r25 = r0.syntaxError(r1)
            goto L_0x0039
        L_0x027c:
            r28 = 1
            r0 = r32
            r1 = r28
            gnu.expr.Expression r7 = r0.parseFLWRInner(r1)
            goto L_0x0205
        L_0x0288:
            java.lang.String r28 = "let"
            r0 = r32
            r1 = r28
            boolean r28 = r0.match(r1)
            if (r28 == 0) goto L_0x02bd
            r32.getRawToken()
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 36
            r0 = r28
            r1 = r29
            if (r0 == r1) goto L_0x02b1
            java.lang.String r28 = "missing $NAME after 'let'"
            r0 = r32
            r1 = r28
            gnu.expr.Expression r25 = r0.syntaxError(r1)
            goto L_0x0039
        L_0x02b1:
            r28 = 0
            r0 = r32
            r1 = r28
            gnu.expr.Expression r7 = r0.parseFLWRInner(r1)
            goto L_0x0205
        L_0x02bd:
            r28 = 119(0x77, float:1.67E-43)
            r0 = r32
            r1 = r28
            char r19 = r0.pushNesting(r1)
            r0 = r32
            int r0 = r0.curToken
            r28 = r0
            r29 = 196(0xc4, float:2.75E-43)
            r0 = r28
            r1 = r29
            if (r0 != r1) goto L_0x032e
            r32.getRawToken()
            gnu.expr.Expression r11 = r32.parseExprSingle()
        L_0x02dc:
            r0 = r32
            r1 = r19
            r0.popNesting(r1)
            java.lang.String r28 = "stable"
            r0 = r32
            r1 = r28
            boolean r24 = r0.match(r1)
            if (r24 == 0) goto L_0x02f2
            r32.getRawToken()
        L_0x02f2:
            java.lang.String r28 = "return"
            r0 = r32
            r1 = r28
            boolean r23 = r0.match(r1)
            java.lang.String r28 = "order"
            r0 = r32
            r1 = r28
            boolean r22 = r0.match(r1)
            if (r23 != 0) goto L_0x0341
            if (r22 != 0) goto L_0x0341
            java.lang.String r28 = "let"
            r0 = r32
            r1 = r28
            boolean r28 = r0.match(r1)
            if (r28 != 0) goto L_0x0341
            java.lang.String r28 = "for"
            r0 = r32
            r1 = r28
            boolean r28 = r0.match(r1)
            if (r28 != 0) goto L_0x0341
            java.lang.String r28 = "missing 'return' clause"
            r0 = r32
            r1 = r28
            gnu.expr.Expression r25 = r0.syntaxError(r1)
            goto L_0x0039
        L_0x032e:
            java.lang.String r28 = "where"
            r0 = r32
            r1 = r28
            boolean r28 = r0.match(r1)
            if (r28 == 0) goto L_0x033f
            gnu.expr.Expression r11 = r32.parseExprSingle()
            goto L_0x02dc
        L_0x033f:
            r11 = 0
            goto L_0x02dc
        L_0x0341:
            if (r22 != 0) goto L_0x034c
            java.lang.String r28 = "unexpected eof-of-file after 'return'"
            r0 = r32
            r1 = r28
            r0.peekNonSpace(r1)
        L_0x034c:
            int r28 = r32.getLineNumber()
            int r10 = r28 + 1
            int r28 = r32.getColumnNumber()
            int r9 = r28 + 1
            if (r23 == 0) goto L_0x035d
            r32.getRawToken()
        L_0x035d:
            if (r22 == 0) goto L_0x03bb
            r0 = r32
            int r0 = r0.flworDeclsCount
            r28 = r0
            r0 = r32
            int r0 = r0.flworDeclsFirst
            r29 = r0
            int r17 = r28 - r29
            r0 = r17
            gnu.expr.Expression[] r6 = new gnu.expr.Expression[r0]
            r13 = 0
        L_0x0372:
            r0 = r17
            if (r13 >= r0) goto L_0x0390
            gnu.expr.ReferenceExp r28 = new gnu.expr.ReferenceExp
            r0 = r32
            gnu.expr.Declaration[] r0 = r0.flworDecls
            r29 = r0
            r0 = r32
            int r0 = r0.flworDeclsFirst
            r30 = r0
            int r30 = r30 + r13
            r29 = r29[r30]
            r28.<init>((gnu.expr.Declaration) r29)
            r6[r13] = r28
            int r13 = r13 + 1
            goto L_0x0372
        L_0x0390:
            gnu.expr.ApplyExp r7 = new gnu.expr.ApplyExp
            gnu.expr.PrimProcedure r28 = new gnu.expr.PrimProcedure
            java.lang.String r29 = "gnu.xquery.util.OrderedMap"
            java.lang.String r30 = "makeTuple$V"
            r31 = 1
            r28.<init>((java.lang.String) r29, (java.lang.String) r30, (int) r31)
            r0 = r28
            r7.<init>((gnu.mapping.Procedure) r0, (gnu.expr.Expression[]) r6)
            r8 = r7
        L_0x03a3:
            if (r11 == 0) goto L_0x03d0
            gnu.expr.IfExp r7 = new gnu.expr.IfExp
            gnu.expr.Expression r28 = booleanValue(r11)
            gnu.expr.QuoteExp r29 = gnu.expr.QuoteExp.voidExp
            r0 = r28
            r1 = r29
            r7.<init>(r0, r8, r1)
        L_0x03b4:
            r0 = r32
            r0.maybeSetLine((gnu.expr.Expression) r7, (int) r10, (int) r9)
            goto L_0x0205
        L_0x03bb:
            gnu.expr.Expression r7 = r32.parseExprSingle()
            r8 = r7
            goto L_0x03a3
        L_0x03c1:
            java.lang.String r28 = "valuesMapWithPos"
            goto L_0x0242
        L_0x03c5:
            r28 = r25
            gnu.expr.LetExp r28 = (gnu.expr.LetExp) r28
            r0 = r28
            r0.setBody(r7)
            goto L_0x0039
        L_0x03d0:
            r7 = r8
            goto L_0x03b4
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.lang.XQParser.parseFLWRInner(boolean):gnu.expr.Expression");
    }

    public Expression parseQuantifiedExpr(boolean isEvery) throws IOException, SyntaxException {
        Expression body;
        String str;
        char saveNesting = pushNesting(isEvery ? 'e' : 's');
        this.curToken = 36;
        Declaration decl = parseVariableDeclaration();
        if (decl == null) {
            return syntaxError("missing Variable token:" + this.curToken);
        }
        getRawToken();
        LambdaExp lexp = new LambdaExp(1);
        lexp.addDeclaration(decl);
        decl.noteValue((Expression) null);
        decl.setFlag(262144);
        decl.setTypeExp(parseOptionalTypeDeclaration());
        if (match("in")) {
            getRawToken();
        } else {
            if (this.curToken == 76) {
                getRawToken();
            }
            syntaxError("missing 'in' in QuantifiedExpr");
        }
        Expression[] inits = {parseExprSingle()};
        popNesting(saveNesting);
        this.comp.push((ScopeExp) lexp);
        if (this.curToken == 44) {
            getRawToken();
            if (this.curToken != 36) {
                return syntaxError("missing $NAME after ','");
            }
            body = parseQuantifiedExpr(isEvery);
        } else {
            boolean sawSatisfies = match("satisfies");
            if (!sawSatisfies && !match("every") && !match("some")) {
                return syntaxError("missing 'satisfies' clause");
            }
            peekNonSpace("unexpected eof-of-file after 'satisfies'");
            int bodyLine = getLineNumber() + 1;
            int bodyColumn = getColumnNumber() + 1;
            if (sawSatisfies) {
                getRawToken();
            }
            body = parseExprSingle();
            maybeSetLine(body, bodyLine, bodyColumn);
        }
        this.comp.pop(lexp);
        lexp.body = body;
        Expression[] args = {lexp, inits[0]};
        if (isEvery) {
            str = "every";
        } else {
            str = "some";
        }
        return new ApplyExp(makeFunctionExp("gnu.xquery.util.ValuesEvery", str), args);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:74:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression parseFunctionDefinition(int r14, int r15) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r13 = this;
            int r9 = r13.curToken
            r10 = 81
            if (r9 == r10) goto L_0x0013
            int r9 = r13.curToken
            r10 = 65
            if (r9 == r10) goto L_0x0013
            java.lang.String r9 = "missing function name"
            gnu.expr.Expression r6 = r13.syntaxError(r9)
        L_0x0012:
            return r6
        L_0x0013:
            java.lang.String r3 = new java.lang.String
            char[] r9 = r13.tokenBuffer
            r10 = 0
            int r11 = r13.tokenBufferLength
            r3.<init>(r9, r10, r11)
            gnu.text.SourceMessages r9 = r13.getMessages()
            gnu.text.LineBufferedReader r10 = r13.port
            java.lang.String r10 = r10.getName()
            int r11 = r13.curLine
            int r12 = r13.curColumn
            r9.setLine(r10, r11, r12)
            r9 = 1
            gnu.mapping.Symbol r7 = r13.namespaceResolve(r3, r9)
            java.lang.String r8 = r7.getNamespaceURI()
            java.lang.String r9 = "http://www.w3.org/XML/1998/namespace"
            if (r8 == r9) goto L_0x0047
            java.lang.String r9 = "http://www.w3.org/2001/XMLSchema"
            if (r8 == r9) goto L_0x0047
            java.lang.String r9 = "http://www.w3.org/2001/XMLSchema-instance"
            if (r8 == r9) goto L_0x0047
            java.lang.String r9 = "http://www.w3.org/2005/xpath-functions"
            if (r8 != r9) goto L_0x008a
        L_0x0047:
            r9 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "cannot declare function in standard namespace '"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.StringBuilder r10 = r10.append(r8)
            r11 = 39
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r10 = r10.toString()
            java.lang.String r11 = "XQST0045"
            r13.error(r9, r10, r11)
        L_0x0067:
            r13.getRawToken()
            int r9 = r13.curToken
            r10 = 40
            if (r9 == r10) goto L_0x00c5
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "missing parameter list:"
            java.lang.StringBuilder r9 = r9.append(r10)
            int r10 = r13.curToken
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            gnu.expr.Expression r6 = r13.syntaxError(r9)
            goto L_0x0012
        L_0x008a:
            java.lang.String r9 = ""
            if (r8 != r9) goto L_0x00a3
            gnu.expr.Compilation r9 = r13.comp
            boolean r9 = r9.isPedantic()
            if (r9 == 0) goto L_0x00a0
            r9 = 101(0x65, float:1.42E-43)
        L_0x0098:
            java.lang.String r10 = "cannot declare function in empty namespace"
            java.lang.String r11 = "XQST0060"
            r13.error(r9, r10, r11)
            goto L_0x0067
        L_0x00a0:
            r9 = 119(0x77, float:1.67E-43)
            goto L_0x0098
        L_0x00a3:
            java.lang.String r9 = r13.libraryModuleNamespace
            if (r9 == 0) goto L_0x0067
            java.lang.String r9 = r13.libraryModuleNamespace
            if (r8 == r9) goto L_0x0067
            java.lang.String r9 = "http://www.w3.org/2005/xquery-local-functions"
            boolean r9 = r9.equals(r8)
            if (r9 == 0) goto L_0x00bb
            gnu.expr.Compilation r9 = r13.comp
            boolean r9 = r9.isPedantic()
            if (r9 == 0) goto L_0x0067
        L_0x00bb:
            r9 = 101(0x65, float:1.42E-43)
            java.lang.String r10 = "function not in namespace of library module"
            java.lang.String r11 = "XQST0048"
            r13.error(r9, r10, r11)
            goto L_0x0067
        L_0x00c5:
            r13.getRawToken()
            gnu.expr.LambdaExp r2 = new gnu.expr.LambdaExp
            r2.<init>()
            r13.maybeSetLine((gnu.expr.Expression) r2, (int) r14, (int) r15)
            r2.setName(r3)
            gnu.expr.Compilation r9 = r13.comp
            gnu.expr.ScopeExp r9 = r9.currentScope()
            gnu.expr.Declaration r0 = r9.addDeclaration((java.lang.Object) r7)
            gnu.expr.Compilation r9 = r13.comp
            boolean r9 = r9.isStatic()
            if (r9 == 0) goto L_0x00ea
            r10 = 2048(0x800, double:1.0118E-320)
            r0.setFlag(r10)
        L_0x00ea:
            r9 = 2048(0x800, float:2.87E-42)
            r2.setFlag(r9)
            r9 = 1
            r0.setCanRead(r9)
            r9 = 1
            r0.setProcedureDecl(r9)
            r13.maybeSetLine((gnu.expr.Declaration) r0, (int) r14, (int) r15)
            gnu.expr.Compilation r9 = r13.comp
            r9.push((gnu.expr.ScopeExp) r2)
            int r9 = r13.curToken
            r10 = 41
            if (r9 == r10) goto L_0x0116
        L_0x0105:
            gnu.expr.Declaration r4 = r13.parseVariableDeclaration()
            if (r4 != 0) goto L_0x013d
            java.lang.String r9 = "missing parameter name"
            r13.error(r9)
        L_0x0110:
            int r9 = r13.curToken
            r10 = 41
            if (r9 != r10) goto L_0x0157
        L_0x0116:
            r13.getRawToken()
            gnu.expr.Expression r5 = r13.parseOptionalTypeDeclaration()
            gnu.expr.Expression r9 = r13.parseEnclosedExpr()
            r2.body = r9
            gnu.expr.Compilation r9 = r13.comp
            r9.pop(r2)
            if (r5 == 0) goto L_0x012f
            gnu.xquery.lang.XQuery r9 = r13.interpreter
            r2.setCoercedReturnValue(r5, r9)
        L_0x012f:
            gnu.expr.SetExp r6 = new gnu.expr.SetExp
            r6.<init>((gnu.expr.Declaration) r0, (gnu.expr.Expression) r2)
            r9 = 1
            r6.setDefining(r9)
            r0.noteValue(r2)
            goto L_0x0012
        L_0x013d:
            r2.addDeclaration((gnu.expr.Declaration) r4)
            r13.getRawToken()
            int r9 = r2.min_args
            int r9 = r9 + 1
            r2.min_args = r9
            int r9 = r2.max_args
            int r9 = r9 + 1
            r2.max_args = r9
            gnu.expr.Expression r9 = r13.parseOptionalTypeDeclaration()
            r4.setTypeExp(r9)
            goto L_0x0110
        L_0x0157:
            int r9 = r13.curToken
            r10 = 44
            if (r9 == r10) goto L_0x0186
            java.lang.String r9 = "missing ',' in parameter list"
            gnu.expr.Expression r1 = r13.syntaxError(r9)
        L_0x0163:
            r13.getRawToken()
            int r9 = r13.curToken
            if (r9 < 0) goto L_0x0176
            int r9 = r13.curToken
            r10 = 59
            if (r9 == r10) goto L_0x0176
            int r9 = r13.curToken
            r10 = 59
            if (r9 != r10) goto L_0x0179
        L_0x0176:
            r6 = r1
            goto L_0x0012
        L_0x0179:
            int r9 = r13.curToken
            r10 = 41
            if (r9 == r10) goto L_0x0116
            int r9 = r13.curToken
            r10 = 44
            if (r9 != r10) goto L_0x0163
            goto L_0x0105
        L_0x0186:
            r13.getRawToken()
            goto L_0x0105
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.lang.XQParser.parseFunctionDefinition(int, int):gnu.expr.Expression");
    }

    public Object readObject() throws IOException, SyntaxException {
        return parse((Compilation) null);
    }

    /* access modifiers changed from: protected */
    public Symbol namespaceResolve(String name, boolean function) {
        int colon = name.indexOf(58);
        String prefix = colon >= 0 ? name.substring(0, colon).intern() : function ? XQuery.DEFAULT_FUNCTION_PREFIX : XQuery.DEFAULT_ELEMENT_PREFIX;
        String uri = QNameUtils.lookupPrefix(prefix, this.constructorNamespaces, this.prologNamespaces);
        if (uri == null) {
            if (colon < 0) {
                uri = "";
            } else if (!this.comp.isPedantic()) {
                try {
                    Class<?> cls = Class.forName(prefix);
                    uri = "class:" + prefix;
                } catch (Exception e) {
                    uri = null;
                }
            }
            if (uri == null) {
                getMessages().error('e', "unknown namespace prefix '" + prefix + "'", "XPST0081");
                uri = "(unknown namespace)";
            }
        }
        return Symbol.make(uri, colon < 0 ? name : name.substring(colon + 1), prefix);
    }

    /* access modifiers changed from: package-private */
    public void parseSeparator() throws IOException, SyntaxException {
        int startLine = this.port.getLineNumber() + 1;
        int startColumn = this.port.getColumnNumber() + 1;
        int next = skipSpace(this.nesting != 0);
        if (next != 59) {
            if (warnOldVersion && next != 10) {
                this.curLine = startLine;
                this.curColumn = startColumn;
                error('w', "missing ';' after declaration");
            }
            if (next >= 0) {
                unread(next);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r30v8, resolved type: gnu.expr.ApplyExp} */
    /* JADX WARNING: type inference failed for: r30v1, types: [gnu.expr.Expression] */
    /* JADX WARNING: type inference failed for: r30v2 */
    /* JADX WARNING: type inference failed for: r30v4 */
    /* JADX WARNING: type inference failed for: r30v5 */
    /* JADX WARNING: type inference failed for: r0v303, types: [gnu.expr.ApplyExp] */
    /* JADX WARNING: type inference failed for: r30v9 */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0051, code lost:
        if (r18 != 47) goto L_0x0053;
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:373:0x0b46  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression parse(gnu.expr.Compilation r52) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r51 = this;
            r0 = r52
            r1 = r51
            r1.comp = r0
            int r18 = r51.skipSpace()
            if (r18 >= 0) goto L_0x000f
            r27 = 0
        L_0x000e:
            return r27
        L_0x000f:
            r0 = r51
            int r6 = r0.parseCount
            int r6 = r6 + 1
            r0 = r51
            r0.parseCount = r6
            r0 = r51
            r1 = r18
            r0.unread(r1)
            int r6 = r51.getLineNumber()
            int r44 = r6 + 1
            int r6 = r51.getColumnNumber()
            int r43 = r6 + 1
            r6 = 35
            r0 = r18
            if (r0 != r6) goto L_0x006d
            r6 = 1
            r0 = r44
            if (r0 != r6) goto L_0x006d
            r6 = 1
            r0 = r43
            if (r0 != r6) goto L_0x006d
            r51.read()
            int r18 = r51.read()
            r6 = 33
            r0 = r18
            if (r0 != r6) goto L_0x0053
            int r18 = r51.read()
            r6 = 47
            r0 = r18
            if (r0 == r6) goto L_0x005a
        L_0x0053:
            java.lang.String r6 = "'#' is only allowed in initial '#!/PROGRAM'"
            r0 = r51
            r0.error(r6)
        L_0x005a:
            r6 = 13
            r0 = r18
            if (r0 == r6) goto L_0x006d
            r6 = 10
            r0 = r18
            if (r0 == r6) goto L_0x006d
            if (r18 < 0) goto L_0x006d
            int r18 = r51.read()
            goto L_0x005a
        L_0x006d:
            int r6 = r51.getRawToken()
            r9 = -1
            if (r6 != r9) goto L_0x0077
            r27 = 0
            goto L_0x000e
        L_0x0077:
            r51.peekOperand()
            r0 = r51
            int r6 = r0.curToken
            r9 = 65
            if (r6 != r9) goto L_0x00a3
            java.lang.String r9 = "namespace"
            r0 = r51
            java.lang.Object r6 = r0.curValue
            java.lang.String r6 = (java.lang.String) r6
            boolean r6 = r9.equals(r6)
            if (r6 == 0) goto L_0x00a3
            boolean r6 = warnOldVersion
            if (r6 == 0) goto L_0x009d
            r6 = 119(0x77, float:1.67E-43)
            java.lang.String r9 = "use 'declare namespace' instead of 'namespace'"
            r0 = r51
            r0.error(r6, r9)
        L_0x009d:
            r6 = 78
            r0 = r51
            r0.curToken = r6
        L_0x00a3:
            r0 = r51
            int r6 = r0.curToken
            switch(r6) {
                case 66: goto L_0x0b67;
                case 69: goto L_0x06c1;
                case 71: goto L_0x0653;
                case 72: goto L_0x0910;
                case 73: goto L_0x043d;
                case 75: goto L_0x0821;
                case 76: goto L_0x086e;
                case 77: goto L_0x02b9;
                case 78: goto L_0x02b9;
                case 79: goto L_0x06c1;
                case 80: goto L_0x011c;
                case 83: goto L_0x07ab;
                case 84: goto L_0x0434;
                case 85: goto L_0x0a04;
                case 86: goto L_0x0160;
                case 87: goto L_0x00df;
                case 89: goto L_0x0a51;
                case 111: goto L_0x0974;
                default: goto L_0x00aa;
            }
        L_0x00aa:
            r6 = -1
            r9 = 1
            r0 = r51
            gnu.expr.Expression r27 = r0.parseExprSequence(r6, r9)
            r0 = r51
            int r6 = r0.curToken
            r9 = 10
            if (r6 != r9) goto L_0x00c1
            r6 = 10
            r0 = r51
            r0.unread(r6)
        L_0x00c1:
            r0 = r51
            r1 = r27
            r2 = r44
            r3 = r43
            r0.maybeSetLine((gnu.expr.Expression) r1, (int) r2, (int) r3)
            r0 = r51
            java.lang.String r6 = r0.libraryModuleNamespace
            if (r6 == 0) goto L_0x000e
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "query body in a library module"
            java.lang.String r10 = "XPST0003"
            r0 = r51
            r0.error(r6, r9, r10)
            goto L_0x000e
        L_0x00df:
            int r6 = r51.getLineNumber()
            int r23 = r6 + 1
            int r6 = r51.getColumnNumber()
            int r22 = r6 + 1
            java.lang.String r6 = "unexpected end-of-file after 'define QName'"
            r0 = r51
            int r36 = r0.peekNonSpace(r6)
            r6 = 40
            r0 = r36
            if (r0 != r6) goto L_0x0112
            java.lang.String r6 = "'missing 'function' after 'define'"
            r0 = r51
            r0.syntaxError(r6)
            r6 = 65
            r0 = r51
            r0.curToken = r6
            r0 = r51
            r1 = r23
            r2 = r22
            gnu.expr.Expression r27 = r0.parseFunctionDefinition(r1, r2)
            goto L_0x000e
        L_0x0112:
            java.lang.String r6 = "missing keyword after 'define'"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x011c:
            int r6 = r51.getLineNumber()
            int r23 = r6 + 1
            int r6 = r51.getColumnNumber()
            int r22 = r6 + 1
            r51.getRawToken()
            java.lang.String r6 = "unexpected end-of-file after 'define function'"
            r0 = r51
            r0.peekNonSpace(r6)
            r6 = 100
            r0 = r51
            char r40 = r0.pushNesting(r6)
            r0 = r51
            r1 = r23
            r2 = r22
            gnu.expr.Expression r27 = r0.parseFunctionDefinition(r1, r2)
            r0 = r51
            r1 = r40
            r0.popNesting(r1)
            r51.parseSeparator()
            r0 = r51
            r1 = r27
            r2 = r44
            r3 = r43
            r0.maybeSetLine((gnu.expr.Expression) r1, (int) r2, (int) r3)
            r6 = 1
            r0 = r51
            r0.seenDeclaration = r6
            goto L_0x000e
        L_0x0160:
            r51.getRawToken()
            gnu.expr.Declaration r21 = r51.parseVariableDeclaration()
            if (r21 != 0) goto L_0x0173
            java.lang.String r6 = "missing Variable"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0173:
            java.lang.Object r35 = r21.getSymbol()
            r0 = r35
            boolean r6 = r0 instanceof java.lang.String
            if (r6 == 0) goto L_0x01a4
            gnu.text.SourceMessages r6 = r51.getMessages()
            r0 = r51
            gnu.text.LineBufferedReader r9 = r0.port
            java.lang.String r9 = r9.getName()
            r0 = r51
            int r10 = r0.curLine
            r0 = r51
            int r11 = r0.curColumn
            r6.setLine(r9, r10, r11)
            java.lang.String r35 = (java.lang.String) r35
            r6 = 0
            r0 = r51
            r1 = r35
            gnu.mapping.Symbol r6 = r0.namespaceResolve(r1, r6)
            r0 = r21
            r0.setSymbol(r6)
        L_0x01a4:
            r0 = r51
            java.lang.String r6 = r0.libraryModuleNamespace
            if (r6 == 0) goto L_0x01d7
            java.lang.Object r6 = r21.getSymbol()
            gnu.mapping.Symbol r6 = (gnu.mapping.Symbol) r6
            java.lang.String r48 = r6.getNamespaceURI()
            r0 = r51
            java.lang.String r6 = r0.libraryModuleNamespace
            r0 = r48
            if (r0 == r6) goto L_0x01d7
            java.lang.String r6 = "http://www.w3.org/2005/xquery-local-functions"
            r0 = r48
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x01cc
            boolean r6 = r52.isPedantic()
            if (r6 == 0) goto L_0x01d7
        L_0x01cc:
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "variable not in namespace of library module"
            java.lang.String r10 = "XQST0048"
            r0 = r51
            r0.error(r6, r9, r10)
        L_0x01d7:
            gnu.expr.ScopeExp r6 = r52.currentScope()
            r0 = r21
            r6.addDeclaration((gnu.expr.Declaration) r0)
            r51.getRawToken()
            gnu.expr.Expression r47 = r51.parseOptionalTypeDeclaration()
            r6 = 1
            r0 = r21
            r0.setCanRead(r6)
            r10 = 16384(0x4000, double:8.0948E-320)
            r0 = r21
            r0.setFlag(r10)
            r30 = 0
            r42 = 0
            r0 = r51
            int r6 = r0.curToken
            r9 = 402(0x192, float:5.63E-43)
            if (r6 == r9) goto L_0x0208
            r0 = r51
            int r6 = r0.curToken
            r9 = 76
            if (r6 != r9) goto L_0x021c
        L_0x0208:
            r0 = r51
            int r6 = r0.curToken
            r9 = 402(0x192, float:5.63E-43)
            if (r6 != r9) goto L_0x0217
            java.lang.String r6 = "declare variable contains '=' instead of ':='"
            r0 = r51
            r0.error(r6)
        L_0x0217:
            r51.getRawToken()
            r42 = 1
        L_0x021c:
            r0 = r51
            int r6 = r0.curToken
            r9 = 123(0x7b, float:1.72E-43)
            if (r6 != r9) goto L_0x025d
            java.lang.String r6 = "obsolete '{' in variable declaration"
            r0 = r51
            r0.warnOldVersion(r6)
            gnu.expr.Expression r30 = r51.parseEnclosedExpr()
            r51.parseSeparator()
        L_0x0232:
            if (r47 == 0) goto L_0x023c
            r0 = r30
            r1 = r47
            gnu.expr.ApplyExp r30 = gnu.expr.Compilation.makeCoercion((gnu.expr.Expression) r0, (gnu.expr.Expression) r1)
        L_0x023c:
            r0 = r21
            r1 = r30
            r0.noteValue(r1)
            r0 = r21
            r1 = r30
            gnu.expr.SetExp r27 = gnu.expr.SetExp.makeDefinition((gnu.expr.Declaration) r0, (gnu.expr.Expression) r1)
            r0 = r51
            r1 = r27
            r2 = r44
            r3 = r43
            r0.maybeSetLine((gnu.expr.Expression) r1, (int) r2, (int) r3)
            r6 = 1
            r0 = r51
            r0.seenDeclaration = r6
            goto L_0x000e
        L_0x025d:
            java.lang.String r6 = "external"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x02a1
            r6 = 2
            gnu.expr.Expression[] r15 = new gnu.expr.Expression[r6]
            r6 = 0
            gnu.expr.QuoteExp r9 = new gnu.expr.QuoteExp
            java.lang.Object r10 = r21.getSymbol()
            r9.<init>(r10)
            r10 = 0
            gnu.expr.ApplyExp r9 = castQName(r9, r10)
            r15[r6] = r9
            r9 = 1
            if (r47 != 0) goto L_0x029e
            gnu.expr.QuoteExp r6 = gnu.expr.QuoteExp.nullExp
        L_0x0280:
            r15[r9] = r6
            gnu.expr.ApplyExp r30 = new gnu.expr.ApplyExp
            gnu.expr.QuoteExp r6 = getExternalFunction
            r0 = r30
            r0.<init>((gnu.expr.Expression) r6, (gnu.expr.Expression[]) r15)
            r0 = r51
            int r6 = r0.curLine
            r0 = r51
            int r9 = r0.curColumn
            r0 = r51
            r1 = r30
            r0.maybeSetLine((gnu.expr.Expression) r1, (int) r6, (int) r9)
            r51.getRawToken()
            goto L_0x0232
        L_0x029e:
            r6 = r47
            goto L_0x0280
        L_0x02a1:
            gnu.expr.Expression r30 = r51.parseExpr()
            r25 = 0
            if (r42 == 0) goto L_0x02ab
            if (r30 != 0) goto L_0x02b3
        L_0x02ab:
            java.lang.String r6 = "expected ':= init' or 'external'"
            r0 = r51
            gnu.expr.Expression r25 = r0.syntaxError(r6)
        L_0x02b3:
            if (r30 != 0) goto L_0x0232
            r30 = r25
            goto L_0x0232
        L_0x02b9:
            r0 = r51
            int r0 = r0.curToken
            r20 = r0
            r6 = 77
            r0 = r20
            if (r0 != r6) goto L_0x0304
            r0 = r51
            java.lang.String r6 = r0.libraryModuleNamespace
            if (r6 == 0) goto L_0x0304
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "duplicate module declaration"
            r0 = r51
            r0.error(r6, r9)
        L_0x02d4:
            r0 = r51
            int r6 = r0.nesting
            if (r6 == 0) goto L_0x031a
            r6 = 1
        L_0x02db:
            r0 = r51
            int r36 = r0.skipSpace(r6)
            if (r36 < 0) goto L_0x0434
            r51.unread()
            r0 = r36
            char r6 = (char) r0
            boolean r6 = gnu.xml.XName.isNameStart(r6)
            if (r6 == 0) goto L_0x0434
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 65
            if (r6 == r9) goto L_0x031c
            java.lang.String r6 = "missing namespace prefix"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0304:
            r0 = r51
            boolean r6 = r0.seenDeclaration
            if (r6 == 0) goto L_0x02d4
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x02d4
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "namespace declared after function/variable/option"
            r0 = r51
            r0.error(r6, r9)
            goto L_0x02d4
        L_0x031a:
            r6 = 0
            goto L_0x02db
        L_0x031c:
            java.lang.String r39 = new java.lang.String
            r0 = r51
            char[] r6 = r0.tokenBuffer
            r9 = 0
            r0 = r51
            int r10 = r0.tokenBufferLength
            r0 = r39
            r0.<init>(r6, r9, r10)
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 402(0x192, float:5.63E-43)
            if (r6 == r9) goto L_0x0341
            java.lang.String r6 = "missing '=' in namespace declaration"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0341:
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 == r9) goto L_0x0356
            java.lang.String r6 = "missing uri in namespace declaration"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0356:
            java.lang.String r6 = new java.lang.String
            r0 = r51
            char[] r9 = r0.tokenBuffer
            r10 = 0
            r0 = r51
            int r11 = r0.tokenBufferLength
            r6.<init>(r9, r10, r11)
            java.lang.String r48 = r6.intern()
            java.lang.String r39 = r39.intern()
            r0 = r51
            gnu.xml.NamespaceBinding r0 = r0.prologNamespaces
            r37 = r0
        L_0x0372:
            gnu.xml.NamespaceBinding r6 = builtinNamespaces
            r0 = r37
            if (r0 == r6) goto L_0x03a4
            java.lang.String r6 = r37.getPrefix()
            r0 = r39
            if (r6 != r0) goto L_0x0424
            r6 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "duplicate declarations for the same namespace prefix '"
            java.lang.StringBuilder r9 = r9.append(r10)
            r0 = r39
            java.lang.StringBuilder r9 = r9.append(r0)
            java.lang.String r10 = "'"
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "XQST0033"
            r0 = r51
            r0.error(r6, r9, r10)
        L_0x03a4:
            r0 = r51
            r1 = r39
            r2 = r48
            r0.pushNamespace(r1, r2)
            r6 = 0
            r0 = r51
            r1 = r39
            r2 = r48
            r0.checkAllowedNamespaceDeclaration(r1, r2, r6)
            r51.parseSeparator()
            r6 = 77
            r0 = r20
            if (r0 != r6) goto L_0x0430
            gnu.expr.ModuleExp r8 = r52.getModule()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r9 = gnu.expr.Compilation.mangleURI(r48)
            java.lang.StringBuilder r6 = r6.append(r9)
            r9 = 46
            java.lang.StringBuilder r6 = r6.append(r9)
            java.lang.String r9 = r8.getFileName()
            java.lang.String r9 = gnu.xquery.lang.XQuery.makeClassName(r9)
            java.lang.StringBuilder r6 = r6.append(r9)
            java.lang.String r4 = r6.toString()
            r8.setName(r4)
            gnu.bytecode.ClassType r6 = new gnu.bytecode.ClassType
            r6.<init>(r4)
            r0 = r52
            r0.mainClass = r6
            r0 = r52
            gnu.bytecode.ClassType r6 = r0.mainClass
            r8.setType(r6)
            gnu.expr.ModuleManager r32 = gnu.expr.ModuleManager.getInstance()
            r0 = r32
            r1 = r52
            gnu.expr.ModuleInfo r5 = r0.find(r1)
            r0 = r48
            r5.setNamespaceUri(r0)
            r0 = r52
            gnu.bytecode.ClassType r6 = r0.mainClass
            r8.setType(r6)
            int r6 = r48.length()
            if (r6 != 0) goto L_0x042a
            java.lang.String r6 = "zero-length module namespace"
            java.lang.String r9 = "XQST0088"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6, r9)
            goto L_0x000e
        L_0x0424:
            gnu.xml.NamespaceBinding r37 = r37.getNext()
            goto L_0x0372
        L_0x042a:
            r0 = r48
            r1 = r51
            r1.libraryModuleNamespace = r0
        L_0x0430:
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0434:
            java.lang.String r6 = "'import schema' not implemented"
            java.lang.String r9 = "XQST0009"
            r0 = r51
            r0.fatal(r6, r9)
        L_0x043d:
            r51.getRawToken()
            r39 = 0
            java.lang.String r6 = "namespace"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0489
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 65
            if (r6 == r9) goto L_0x0461
            java.lang.String r6 = "missing namespace prefix"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0461:
            java.lang.String r39 = new java.lang.String
            r0 = r51
            char[] r6 = r0.tokenBuffer
            r9 = 0
            r0 = r51
            int r10 = r0.tokenBufferLength
            r0 = r39
            r0.<init>(r6, r9, r10)
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 402(0x192, float:5.63E-43)
            if (r6 == r9) goto L_0x0486
            java.lang.String r6 = "missing '=' in namespace declaration"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0486:
            r51.getRawToken()
        L_0x0489:
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 == r9) goto L_0x049b
            java.lang.String r6 = "missing uri in namespace declaration"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x049b:
            r0 = r51
            int r6 = r0.tokenBufferLength
            if (r6 != 0) goto L_0x04ad
            java.lang.String r6 = "zero-length target namespace"
            java.lang.String r9 = "XQST0088"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6, r9)
            goto L_0x000e
        L_0x04ad:
            java.lang.String r6 = new java.lang.String
            r0 = r51
            char[] r9 = r0.tokenBuffer
            r10 = 0
            r0 = r51
            int r11 = r0.tokenBufferLength
            r6.<init>(r9, r10, r11)
            java.lang.String r48 = r6.intern()
            if (r39 == 0) goto L_0x04d6
            r6 = 0
            r0 = r51
            r1 = r39
            r2 = r48
            r0.checkAllowedNamespaceDeclaration(r1, r2, r6)
            java.lang.String r6 = r39.intern()
            r0 = r51
            r1 = r48
            r0.pushNamespace(r6, r1)
        L_0x04d6:
            r51.getRawToken()
            gnu.expr.ModuleManager r6 = gnu.expr.ModuleManager.getInstance()
            r0 = r52
            r6.find(r0)
            gnu.expr.ModuleExp r8 = r52.getModule()
            java.util.Vector r7 = new java.util.Vector
            r7.<init>()
            java.lang.String r38 = gnu.expr.Compilation.mangleURI(r48)
            r0 = r51
            gnu.text.LineBufferedReader r6 = r0.port
            java.lang.String r6 = r6.getName()
            r0 = r52
            r1 = r44
            r2 = r43
            r0.setLine(r6, r1, r2)
            java.lang.String r6 = "at"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x05c5
        L_0x050a:
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 == r9) goto L_0x051f
            java.lang.String r6 = "missing module location"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x051f:
            java.lang.String r16 = new java.lang.String
            r0 = r51
            char[] r6 = r0.tokenBuffer
            r9 = 0
            r0 = r51
            int r10 = r0.tokenBufferLength
            r0 = r16
            r0.<init>(r6, r9, r10)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r9 = gnu.expr.Compilation.mangleURI(r48)
            java.lang.StringBuilder r6 = r6.append(r9)
            r9 = 46
            java.lang.StringBuilder r6 = r6.append(r9)
            java.lang.String r9 = gnu.xquery.lang.XQuery.makeClassName(r16)
            java.lang.StringBuilder r6 = r6.append(r9)
            java.lang.String r4 = r6.toString()
            r0 = r16
            gnu.expr.ModuleInfo r5 = kawa.standard.require.lookupModuleFromSourcePath(r0, r8)
            if (r5 != 0) goto L_0x0572
            r6 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "malformed URL: "
            java.lang.StringBuilder r9 = r9.append(r10)
            r0 = r16
            java.lang.StringBuilder r9 = r9.append(r0)
            java.lang.String r9 = r9.toString()
            r0 = r52
            r0.error(r6, r9)
        L_0x0572:
            r6 = 0
            r9 = r52
            kawa.standard.require.importDefinitions(r4, r5, r6, r7, r8, r9)
            r0 = r51
            int r6 = r0.nesting
            if (r6 == 0) goto L_0x05c3
            r6 = 1
        L_0x057f:
            r0 = r51
            int r36 = r0.skipSpace(r6)
            r6 = 44
            r0 = r36
            if (r0 == r6) goto L_0x050a
            r0 = r51
            r1 = r36
            r0.unread(r1)
            r51.parseSeparator()
        L_0x0595:
            r0 = r52
            java.util.Stack<java.lang.Object> r6 = r0.pendingImports
            if (r6 == 0) goto L_0x05b0
            r0 = r52
            java.util.Stack<java.lang.Object> r6 = r0.pendingImports
            int r6 = r6.size()
            if (r6 <= 0) goto L_0x05b0
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "module import forms a cycle"
            java.lang.String r10 = "XQST0073"
            r0 = r51
            r0.error(r6, r9, r10)
        L_0x05b0:
            int r6 = r7.size()
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r6]
            r31 = r0
            r0 = r31
            r7.toArray(r0)
            gnu.expr.Expression r27 = gnu.expr.BeginExp.canonicalize((gnu.expr.Expression[]) r31)
            goto L_0x000e
        L_0x05c3:
            r6 = 0
            goto L_0x057f
        L_0x05c5:
            gnu.expr.ModuleManager r32 = gnu.expr.ModuleManager.getInstance()
            r34 = 0
            r0 = r32
            r1 = r38
            r0.loadPackageInfo(r1)     // Catch:{ ClassNotFoundException -> 0x0ba1, Throwable -> 0x060a }
        L_0x05d2:
            r29 = 0
        L_0x05d4:
            r0 = r32
            r1 = r29
            gnu.expr.ModuleInfo r5 = r0.getModule(r1)
            if (r5 != 0) goto L_0x0634
            if (r34 != 0) goto L_0x05fc
            r6 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "no module found for "
            java.lang.StringBuilder r9 = r9.append(r10)
            r0 = r48
            java.lang.StringBuilder r9 = r9.append(r0)
            java.lang.String r9 = r9.toString()
            r0 = r51
            r0.error(r6, r9)
        L_0x05fc:
            r16 = 0
            r0 = r51
            int r6 = r0.curToken
            r9 = 59
            if (r6 == r9) goto L_0x0595
            r51.parseSeparator()
            goto L_0x0595
        L_0x060a:
            r26 = move-exception
            r6 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "error loading map for "
            java.lang.StringBuilder r9 = r9.append(r10)
            r0 = r48
            java.lang.StringBuilder r9 = r9.append(r0)
            java.lang.String r10 = " - "
            java.lang.StringBuilder r9 = r9.append(r10)
            r0 = r26
            java.lang.StringBuilder r9 = r9.append(r0)
            java.lang.String r9 = r9.toString()
            r0 = r51
            r0.error(r6, r9)
            goto L_0x05d2
        L_0x0634:
            java.lang.String r6 = r5.getNamespaceUri()
            r0 = r48
            boolean r6 = r0.equals(r6)
            if (r6 != 0) goto L_0x0643
        L_0x0640:
            int r29 = r29 + 1
            goto L_0x05d4
        L_0x0643:
            int r34 = r34 + 1
            java.lang.String r9 = r5.getClassName()
            r11 = 0
            r10 = r5
            r12 = r7
            r13 = r8
            r14 = r52
            kawa.standard.require.importDefinitions(r9, r10, r11, r12, r13, r14)
            goto L_0x0640
        L_0x0653:
            r0 = r51
            gnu.xquery.util.NamedCollator r6 = r0.defaultCollator
            if (r6 == 0) goto L_0x066a
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x066a
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "duplicate default collation declaration"
            java.lang.String r10 = "XQST0038"
            r0 = r51
            r0.error(r6, r9, r10)
        L_0x066a:
            java.lang.Object r49 = r51.parseURILiteral()
            r0 = r49
            boolean r6 = r0 instanceof gnu.expr.Expression
            if (r6 == 0) goto L_0x067a
            gnu.expr.Expression r49 = (gnu.expr.Expression) r49
            r27 = r49
            goto L_0x000e
        L_0x067a:
            r19 = r49
            java.lang.String r19 = (java.lang.String) r19
            r0 = r51
            r1 = r19
            java.lang.String r19 = r0.resolveAgainstBaseUri(r1)     // Catch:{ Exception -> 0x0695 }
            gnu.xquery.util.NamedCollator r6 = gnu.xquery.util.NamedCollator.make(r19)     // Catch:{ Exception -> 0x0695 }
            r0 = r51
            r0.defaultCollator = r6     // Catch:{ Exception -> 0x0695 }
        L_0x068e:
            r51.parseSeparator()
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0695:
            r26 = move-exception
            gnu.xquery.util.NamedCollator r6 = gnu.xquery.util.NamedCollator.codepointCollation
            r0 = r51
            r0.defaultCollator = r6
            r6 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "unknown collation '"
            java.lang.StringBuilder r9 = r9.append(r10)
            r0 = r19
            java.lang.StringBuilder r9 = r9.append(r0)
            java.lang.String r10 = "'"
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "XQST0038"
            r0 = r51
            r0.error(r6, r9, r10)
            goto L_0x068e
        L_0x06c1:
            r0 = r51
            int r6 = r0.curToken
            r9 = 79
            if (r6 != r9) goto L_0x0724
            r28 = 1
        L_0x06cb:
            if (r28 == 0) goto L_0x0727
            java.lang.String r39 = "(functions)"
        L_0x06cf:
            r0 = r51
            gnu.xml.NamespaceBinding r6 = r0.prologNamespaces
            gnu.xml.NamespaceBinding r9 = builtinNamespaces
            r0 = r39
            java.lang.String r6 = r6.resolve(r0, r9)
            if (r6 == 0) goto L_0x072a
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "duplicate default namespace declaration"
            java.lang.String r10 = "XQST0066"
            r0 = r51
            r0.error(r6, r9, r10)
        L_0x06e8:
            r51.getRawToken()
            java.lang.String r6 = "namespace"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0740
            r51.getRawToken()
        L_0x06f8:
            r0 = r51
            int r6 = r0.curToken
            r9 = 402(0x192, float:5.63E-43)
            if (r6 == r9) goto L_0x0708
            r0 = r51
            int r6 = r0.curToken
            r9 = 76
            if (r6 != r9) goto L_0x0712
        L_0x0708:
            java.lang.String r6 = "extra '=' in default namespace declaration"
            r0 = r51
            r0.warnOldVersion(r6)
            r51.getRawToken()
        L_0x0712:
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 == r9) goto L_0x0764
            java.lang.String r6 = "missing namespace uri"
            r0 = r51
            gnu.expr.Expression r27 = r0.declError(r6)
            goto L_0x000e
        L_0x0724:
            r28 = 0
            goto L_0x06cb
        L_0x0727:
            java.lang.String r39 = gnu.xquery.lang.XQuery.DEFAULT_ELEMENT_PREFIX
            goto L_0x06cf
        L_0x072a:
            r0 = r51
            boolean r6 = r0.seenDeclaration
            if (r6 == 0) goto L_0x06e8
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x06e8
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "default namespace declared after function/variable/option"
            r0 = r51
            r0.error(r6, r9)
            goto L_0x06e8
        L_0x0740:
            java.lang.String r33 = "expected 'namespace' keyword"
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 == r9) goto L_0x075c
            r0 = r51
            int r6 = r0.curToken
            r9 = 402(0x192, float:5.63E-43)
            if (r6 == r9) goto L_0x075c
            r0 = r51
            r1 = r33
            gnu.expr.Expression r27 = r0.declError(r1)
            goto L_0x000e
        L_0x075c:
            r0 = r51
            r1 = r33
            r0.warnOldVersion(r1)
            goto L_0x06f8
        L_0x0764:
            java.lang.String r6 = new java.lang.String
            r0 = r51
            char[] r9 = r0.tokenBuffer
            r10 = 0
            r0 = r51
            int r11 = r0.tokenBufferLength
            r6.<init>(r9, r10, r11)
            java.lang.String r48 = r6.intern()
            if (r28 == 0) goto L_0x07a4
            r6 = 1
            gnu.mapping.Namespace[] r6 = new gnu.mapping.Namespace[r6]
            r0 = r51
            r0.functionNamespacePath = r6
            r0 = r51
            gnu.mapping.Namespace[] r6 = r0.functionNamespacePath
            r9 = 0
            gnu.mapping.Namespace r10 = gnu.mapping.Namespace.valueOf(r48)
            r6[r9] = r10
        L_0x078a:
            r0 = r51
            r1 = r39
            r2 = r48
            r0.pushNamespace(r1, r2)
            r6 = 0
            r0 = r51
            r1 = r39
            r2 = r48
            r0.checkAllowedNamespaceDeclaration(r1, r2, r6)
            r51.parseSeparator()
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x07a4:
            r0 = r48
            r1 = r51
            r1.defaultElementNamespace = r0
            goto L_0x078a
        L_0x07ab:
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 402(0x192, float:5.63E-43)
            if (r6 != r9) goto L_0x07c0
            java.lang.String r6 = "obsolate '=' in boundary-space declaration"
            r0 = r51
            r0.warnOldVersion(r6)
            r51.getRawToken()
        L_0x07c0:
            r0 = r51
            boolean r6 = r0.boundarySpaceDeclarationSeen
            if (r6 == 0) goto L_0x07d5
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x07d5
            java.lang.String r6 = "duplicate 'declare boundary-space' seen"
            java.lang.String r9 = "XQST0068"
            r0 = r51
            r0.syntaxError(r6, r9)
        L_0x07d5:
            r6 = 1
            r0 = r51
            r0.boundarySpaceDeclarationSeen = r6
            java.lang.String r6 = "preserve"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x07f0
            r6 = 1
            r0 = r51
            r0.boundarySpacePreserve = r6
        L_0x07e9:
            r51.parseSeparator()
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x07f0:
            java.lang.String r6 = "strip"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0800
            r6 = 0
            r0 = r51
            r0.boundarySpacePreserve = r6
            goto L_0x07e9
        L_0x0800:
            java.lang.String r6 = "skip"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0817
            java.lang.String r6 = "update: declare boundary-space skip -> strip"
            r0 = r51
            r0.warnOldVersion(r6)
            r6 = 0
            r0 = r51
            r0.boundarySpacePreserve = r6
            goto L_0x07e9
        L_0x0817:
            java.lang.String r6 = "boundary-space declaration must be preserve or strip"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0821:
            r51.getRawToken()
            r0 = r51
            boolean r6 = r0.constructionModeDeclarationSeen
            if (r6 == 0) goto L_0x0839
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x0839
            java.lang.String r6 = "duplicate 'declare construction' seen"
            java.lang.String r9 = "XQST0067"
            r0 = r51
            r0.syntaxError(r6, r9)
        L_0x0839:
            r6 = 1
            r0 = r51
            r0.constructionModeDeclarationSeen = r6
            java.lang.String r6 = "strip"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0854
            r6 = 1
            r0 = r51
            r0.constructionModeStrip = r6
        L_0x084d:
            r51.parseSeparator()
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0854:
            java.lang.String r6 = "preserve"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0864
            r6 = 0
            r0 = r51
            r0.constructionModeStrip = r6
            goto L_0x084d
        L_0x0864:
            java.lang.String r6 = "construction declaration must be strip or preserve"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x086e:
            r51.getRawToken()
            r0 = r51
            boolean r6 = r0.copyNamespacesDeclarationSeen
            if (r6 == 0) goto L_0x0886
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x0886
            java.lang.String r6 = "duplicate 'declare copy-namespaces' seen"
            java.lang.String r9 = "XQST0055"
            r0 = r51
            r0.syntaxError(r6, r9)
        L_0x0886:
            r6 = 1
            r0 = r51
            r0.copyNamespacesDeclarationSeen = r6
            java.lang.String r6 = "preserve"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x08b4
            r0 = r51
            int r6 = r0.copyNamespacesMode
            r6 = r6 | 1
            r0 = r51
            r0.copyNamespacesMode = r6
        L_0x089f:
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 44
            if (r6 == r9) goto L_0x08d3
            java.lang.String r6 = "missing ',' in copy-namespaces declaration"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x08b4:
            java.lang.String r6 = "no-preserve"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x08c9
            r0 = r51
            int r6 = r0.copyNamespacesMode
            r6 = r6 & -2
            r0 = r51
            r0.copyNamespacesMode = r6
            goto L_0x089f
        L_0x08c9:
            java.lang.String r6 = "expected 'preserve' or 'no-preserve' after 'declare copy-namespaces'"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x08d3:
            r51.getRawToken()
            java.lang.String r6 = "inherit"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x08f1
            r0 = r51
            int r6 = r0.copyNamespacesMode
            r6 = r6 | 2
            r0 = r51
            r0.copyNamespacesMode = r6
        L_0x08ea:
            r51.parseSeparator()
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x08f1:
            java.lang.String r6 = "no-inherit"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0906
            r0 = r51
            int r6 = r0.copyNamespacesMode
            r6 = r6 & -3
            r0 = r51
            r0.copyNamespacesMode = r6
            goto L_0x08ea
        L_0x0906:
            java.lang.String r6 = "expected 'inherit' or 'no-inherit' in copy-namespaces declaration"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0910:
            r51.getRawToken()
            java.lang.String r6 = "empty"
            r0 = r51
            boolean r41 = r0.match(r6)
            r0 = r51
            boolean r6 = r0.emptyOrderDeclarationSeen
            if (r6 == 0) goto L_0x0930
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x0930
            java.lang.String r6 = "duplicate 'declare default empty order' seen"
            java.lang.String r9 = "XQST0069"
            r0 = r51
            r0.syntaxError(r6, r9)
        L_0x0930:
            r6 = 1
            r0 = r51
            r0.emptyOrderDeclarationSeen = r6
            if (r41 == 0) goto L_0x0951
            r51.getRawToken()
        L_0x093a:
            java.lang.String r6 = "greatest"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0959
            r6 = 71
            r0 = r51
            r0.defaultEmptyOrder = r6
        L_0x094a:
            r51.parseSeparator()
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0951:
            java.lang.String r6 = "expected 'empty greatest' or 'empty least'"
            r0 = r51
            r0.syntaxError(r6)
            goto L_0x093a
        L_0x0959:
            java.lang.String r6 = "least"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x096a
            r6 = 76
            r0 = r51
            r0.defaultEmptyOrder = r6
            goto L_0x094a
        L_0x096a:
            java.lang.String r6 = "expected 'empty greatest' or 'empty least'"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0974:
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 81
            if (r6 == r9) goto L_0x0992
            java.lang.String r6 = "expected QName after 'declare option'"
            r0 = r51
            r0.syntaxError(r6)
        L_0x0986:
            r51.parseSeparator()
            r6 = 1
            r0 = r51
            r0.seenDeclaration = r6
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0992:
            java.lang.String r45 = new java.lang.String
            r0 = r51
            char[] r6 = r0.tokenBuffer
            r9 = 0
            r0 = r51
            int r10 = r0.tokenBufferLength
            r0 = r45
            r0.<init>(r6, r9, r10)
            gnu.text.SourceMessages r6 = r51.getMessages()
            r0 = r51
            gnu.text.LineBufferedReader r9 = r0.port
            java.lang.String r9 = r9.getName()
            r0 = r51
            int r10 = r0.curLine
            r0 = r51
            int r11 = r0.curColumn
            r6.setLine(r9, r10, r11)
            r6 = 0
            r0 = r51
            r1 = r45
            gnu.mapping.Symbol r46 = r0.namespaceResolve(r1, r6)
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 == r9) goto L_0x09ee
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r9 = "expected string literal after 'declare option "
            java.lang.StringBuilder r6 = r6.append(r9)
            r0 = r45
            java.lang.StringBuilder r6 = r6.append(r0)
            r9 = 39
            java.lang.StringBuilder r6 = r6.append(r9)
            java.lang.String r6 = r6.toString()
            r0 = r51
            r0.syntaxError(r6)
            goto L_0x0986
        L_0x09ee:
            java.lang.String r6 = new java.lang.String
            r0 = r51
            char[] r9 = r0.tokenBuffer
            r10 = 0
            r0 = r51
            int r11 = r0.tokenBufferLength
            r6.<init>(r9, r10, r11)
            r0 = r51
            r1 = r46
            r0.handleOption(r1, r6)
            goto L_0x0986
        L_0x0a04:
            r0 = r51
            boolean r6 = r0.orderingModeSeen
            if (r6 == 0) goto L_0x0a19
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x0a19
            java.lang.String r6 = "duplicate 'declare ordering' seen"
            java.lang.String r9 = "XQST0065"
            r0 = r51
            r0.syntaxError(r6, r9)
        L_0x0a19:
            r6 = 1
            r0 = r51
            r0.orderingModeSeen = r6
            r51.getRawToken()
            java.lang.String r6 = "ordered"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0a37
            r6 = 0
            r0 = r51
            r0.orderingModeUnordered = r6
        L_0x0a30:
            r51.parseSeparator()
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0a37:
            java.lang.String r6 = "unordered"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0a47
            r6 = 1
            r0 = r51
            r0.orderingModeUnordered = r6
            goto L_0x0a30
        L_0x0a47:
            java.lang.String r6 = "ordering declaration must be ordered or unordered"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0a51:
            r0 = r51
            int r6 = r0.parseCount
            r9 = 1
            if (r6 == r9) goto L_0x0ac6
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "'xquery version' does not start module"
            r0 = r51
            r0.error(r6, r9)
        L_0x0a61:
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 != r9) goto L_0x0ad6
            java.lang.String r50 = new java.lang.String
            r0 = r51
            char[] r6 = r0.tokenBuffer
            r9 = 0
            r0 = r51
            int r10 = r0.tokenBufferLength
            r0 = r50
            r0.<init>(r6, r9, r10)
            java.lang.String r6 = "1.0"
            r0 = r50
            boolean r6 = r0.equals(r6)
            if (r6 != 0) goto L_0x0aa4
            r6 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "unrecognized xquery version "
            java.lang.StringBuilder r9 = r9.append(r10)
            r0 = r50
            java.lang.StringBuilder r9 = r9.append(r0)
            java.lang.String r9 = r9.toString()
            java.lang.String r10 = "XQST0031"
            r0 = r51
            r0.error(r6, r9, r10)
        L_0x0aa4:
            r51.getRawToken()
            java.lang.String r6 = "encoding"
            r0 = r51
            boolean r6 = r0.match(r6)
            if (r6 == 0) goto L_0x0b54
            r51.getRawToken()
            r0 = r51
            int r6 = r0.curToken
            r9 = 34
            if (r6 == r9) goto L_0x0ae0
            java.lang.String r6 = "invalid encoding specification"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0ac6:
            r0 = r51
            int r6 = r0.commentCount
            if (r6 <= 0) goto L_0x0a61
            r6 = 119(0x77, float:1.67E-43)
            java.lang.String r9 = "comments should not precede 'xquery version'"
            r0 = r51
            r0.error(r6, r9)
            goto L_0x0a61
        L_0x0ad6:
            java.lang.String r6 = "missing version string after 'xquery version'"
            r0 = r51
            gnu.expr.Expression r27 = r0.syntaxError(r6)
            goto L_0x000e
        L_0x0ae0:
            java.lang.String r24 = new java.lang.String
            r0 = r51
            char[] r6 = r0.tokenBuffer
            r9 = 0
            r0 = r51
            int r10 = r0.tokenBufferLength
            r0 = r24
            r0.<init>(r6, r9, r10)
            r0 = r51
            int r0 = r0.tokenBufferLength
            r29 = r0
            if (r29 != 0) goto L_0x0b41
            r17 = 1
        L_0x0afa:
            int r29 = r29 + -1
            if (r29 < 0) goto L_0x0b44
            if (r17 != 0) goto L_0x0b44
            r0 = r51
            char[] r6 = r0.tokenBuffer
            char r18 = r6[r29]
            r6 = 65
            r0 = r18
            if (r0 < r6) goto L_0x0b12
            r6 = 90
            r0 = r18
            if (r0 <= r6) goto L_0x0afa
        L_0x0b12:
            r6 = 97
            r0 = r18
            if (r0 < r6) goto L_0x0b1e
            r6 = 122(0x7a, float:1.71E-43)
            r0 = r18
            if (r0 <= r6) goto L_0x0afa
        L_0x0b1e:
            if (r29 == 0) goto L_0x0b3e
            r6 = 48
            r0 = r18
            if (r0 < r6) goto L_0x0b2c
            r6 = 57
            r0 = r18
            if (r0 <= r6) goto L_0x0afa
        L_0x0b2c:
            r6 = 46
            r0 = r18
            if (r0 == r6) goto L_0x0afa
            r6 = 95
            r0 = r18
            if (r0 == r6) goto L_0x0afa
            r6 = 45
            r0 = r18
            if (r0 == r6) goto L_0x0afa
        L_0x0b3e:
            r17 = 1
            goto L_0x0afa
        L_0x0b41:
            r17 = 0
            goto L_0x0afa
        L_0x0b44:
            if (r17 == 0) goto L_0x0b51
            r6 = 101(0x65, float:1.42E-43)
            java.lang.String r9 = "invalid encoding name syntax"
            java.lang.String r10 = "XQST0087"
            r0 = r51
            r0.error(r6, r9, r10)
        L_0x0b51:
            r51.getRawToken()
        L_0x0b54:
            r0 = r51
            int r6 = r0.curToken
            r9 = 59
            if (r6 == r9) goto L_0x0b63
            java.lang.String r6 = "missing ';'"
            r0 = r51
            r0.syntaxError(r6)
        L_0x0b63:
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0b67:
            r0 = r51
            boolean r6 = r0.baseURIDeclarationSeen
            if (r6 == 0) goto L_0x0b7c
            r0 = r51
            boolean r6 = r0.interactive
            if (r6 != 0) goto L_0x0b7c
            java.lang.String r6 = "duplicate 'declare base-uri' seen"
            java.lang.String r9 = "XQST0032"
            r0 = r51
            r0.syntaxError(r6, r9)
        L_0x0b7c:
            r6 = 1
            r0 = r51
            r0.baseURIDeclarationSeen = r6
            java.lang.Object r49 = r51.parseURILiteral()
            r0 = r49
            boolean r6 = r0 instanceof gnu.expr.Expression
            if (r6 == 0) goto L_0x0b91
            gnu.expr.Expression r49 = (gnu.expr.Expression) r49
            r27 = r49
            goto L_0x000e
        L_0x0b91:
            r51.parseSeparator()
            java.lang.String r49 = (java.lang.String) r49
            r0 = r51
            r1 = r49
            r0.setStaticBaseUri(r1)
            gnu.expr.QuoteExp r27 = gnu.expr.QuoteExp.voidExp
            goto L_0x000e
        L_0x0ba1:
            r6 = move-exception
            goto L_0x05d2
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.lang.XQParser.parse(gnu.expr.Compilation):gnu.expr.Expression");
    }

    public void handleOption(Symbol name, String value) {
    }

    public static Expression makeFunctionExp(String className, String name) {
        return makeFunctionExp(className, Compilation.mangleNameIfNeeded(name), name);
    }

    public static Expression makeFunctionExp(String className, String fieldName, String name) {
        return new ReferenceExp(name, Declaration.getDeclarationValueFromStatic(className, fieldName, name));
    }

    /* access modifiers changed from: package-private */
    public String tokenString() {
        switch (this.curToken) {
            case -1:
                return "<EOF>";
            case 34:
                StringBuffer sbuf = new StringBuffer();
                sbuf.append('\"');
                for (int i = 0; i < this.tokenBufferLength; i++) {
                    char ch = this.tokenBuffer[i];
                    if (ch == '\"') {
                        sbuf.append('\"');
                    }
                    sbuf.append(ch);
                }
                sbuf.append('\"');
                return sbuf.toString();
            case 65:
            case 81:
                return new String(this.tokenBuffer, 0, this.tokenBufferLength);
            case 70:
                return new String(this.tokenBuffer, 0, this.tokenBufferLength) + " + '('";
            default:
                if (this.curToken >= 100 && this.curToken - 100 < 13) {
                    return axisNames[this.curToken - 100] + "::-axis(" + this.curToken + ")";
                }
                if (this.curToken < 32 || this.curToken >= 127) {
                    return Integer.toString(this.curToken);
                }
                return Integer.toString(this.curToken) + "='" + ((char) this.curToken) + "'";
        }
    }

    public void error(char severity, String message, String code) {
        SourceMessages messages = getMessages();
        SourceError err = new SourceError(severity, this.port.getName(), this.curLine, this.curColumn, message);
        err.code = code;
        messages.error(err);
    }

    public void error(char severity, String message) {
        error(severity, message, (String) null);
    }

    public Expression declError(String message) throws IOException, SyntaxException {
        if (this.interactive) {
            return syntaxError(message);
        }
        error(message);
        while (this.curToken != 59 && this.curToken != -1) {
            getRawToken();
        }
        return new ErrorExp(message);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0031, code lost:
        unread(r0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression syntaxError(java.lang.String r5, java.lang.String r6) throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r4 = this;
            r3 = 10
            r2 = 0
            r1 = 101(0x65, float:1.42E-43)
            r4.error(r1, r5, r6)
            boolean r1 = r4.interactive
            if (r1 == 0) goto L_0x0035
            r4.curToken = r2
            r1 = 0
            r4.curValue = r1
            r4.nesting = r2
            gnu.text.LineBufferedReader r1 = r4.getPort()
            gnu.mapping.InPort r1 = (gnu.mapping.InPort) r1
            r1.readState = r3
        L_0x001b:
            int r0 = r4.read()
            if (r0 >= 0) goto L_0x002b
        L_0x0021:
            gnu.text.SyntaxException r1 = new gnu.text.SyntaxException
            gnu.text.SourceMessages r2 = r4.getMessages()
            r1.<init>(r2)
            throw r1
        L_0x002b:
            r1 = 13
            if (r0 == r1) goto L_0x0031
            if (r0 != r3) goto L_0x001b
        L_0x0031:
            r4.unread(r0)
            goto L_0x0021
        L_0x0035:
            gnu.expr.ErrorExp r1 = new gnu.expr.ErrorExp
            r1.<init>(r5)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.lang.XQParser.syntaxError(java.lang.String, java.lang.String):gnu.expr.Expression");
    }

    public Expression syntaxError(String message) throws IOException, SyntaxException {
        return syntaxError(message, "XPST0003");
    }

    public void eofError(String msg) throws SyntaxException {
        fatal(msg, "XPST0003");
    }

    public void fatal(String msg, String code) throws SyntaxException {
        SourceMessages messages = getMessages();
        SourceError err = new SourceError('f', this.port.getName(), this.curLine, this.curColumn, msg);
        err.code = code;
        messages.error(err);
        throw new SyntaxException(messages);
    }

    /* access modifiers changed from: package-private */
    public void warnOldVersion(String message) {
        if (warnOldVersion || this.comp.isPedantic()) {
            error(this.comp.isPedantic() ? 'e' : 'w', message);
        }
    }

    public void maybeSetLine(Expression exp, int line, int column) {
        String file = getName();
        if (file != null && exp.getFileName() == null && !(exp instanceof QuoteExp)) {
            exp.setFile(file);
            exp.setLine(line, column);
        }
    }

    public void maybeSetLine(Declaration decl, int line, int column) {
        String file = getName();
        if (file != null) {
            decl.setFile(file);
            decl.setLine(line, column);
        }
    }
}
