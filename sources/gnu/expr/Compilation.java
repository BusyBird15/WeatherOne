package gnu.expr;

import gnu.bytecode.ArrayClassLoader;
import gnu.bytecode.ArrayType;
import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Field;
import gnu.bytecode.Label;
import gnu.bytecode.Method;
import gnu.bytecode.ObjectType;
import gnu.bytecode.PrimType;
import gnu.bytecode.SwitchState;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.kawa.functions.Convert;
import gnu.kawa.lispexpr.LispReader;
import gnu.mapping.Environment;
import gnu.mapping.EnvironmentKey;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrappedException;
import gnu.text.Lexer;
import gnu.text.Options;
import gnu.text.Path;
import gnu.text.SourceLocator;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import kawa.Shell;

public class Compilation implements SourceLocator {
    public static final int BODY_PARSED = 4;
    public static final int CALL_WITH_CONSUMER = 2;
    public static final int CALL_WITH_CONTINUATIONS = 4;
    public static final int CALL_WITH_RETURN = 1;
    public static final int CALL_WITH_TAILCALLS = 3;
    public static final int CALL_WITH_UNSPECIFIED = 0;
    public static final int CLASS_WRITTEN = 14;
    public static final int COMPILED = 12;
    public static final int COMPILE_SETUP = 10;
    public static final int ERROR_SEEN = 100;
    public static final int MODULE_NONSTATIC = -1;
    public static final int MODULE_STATIC = 1;
    public static final int MODULE_STATIC_DEFAULT = 0;
    public static final int MODULE_STATIC_RUN = 2;
    public static final int PROLOG_PARSED = 2;
    public static final int PROLOG_PARSING = 1;
    public static final int RESOLVED = 6;
    public static final int WALKED = 8;
    public static Type[] apply0args = Type.typeArray0;
    public static Method apply0method = typeProcedure.addMethod("apply0", apply0args, (Type) typeObject, 17);
    public static Type[] apply1args = {typeObject};
    public static Method apply1method = typeProcedure.addMethod("apply1", apply1args, (Type) typeObject, 1);
    public static Type[] apply2args = {typeObject, typeObject};
    public static Method apply2method = typeProcedure.addMethod("apply2", apply2args, (Type) typeObject, 1);
    public static Method apply3method = typeProcedure.addMethod("apply3", new Type[]{typeObject, typeObject, typeObject}, (Type) typeObject, 1);
    public static Method apply4method = typeProcedure.addMethod("apply4", new Type[]{typeObject, typeObject, typeObject, typeObject}, (Type) typeObject, 1);
    private static Type[] applyCpsArgs = {typeCallContext};
    public static Method applyCpsMethod = typeProcedure.addMethod("apply", applyCpsArgs, (Type) Type.voidType, 1);
    public static Type[] applyNargs = {objArrayType};
    public static Method applyNmethod = typeProcedure.addMethod("applyN", applyNargs, (Type) typeObject, 1);
    public static Method[] applymethods = {apply0method, apply1method, apply2method, apply3method, apply4method, applyNmethod};
    public static Field argsCallContextField = typeCallContext.getDeclaredField("values");
    private static Compilation chainUninitialized;
    static Method checkArgCountMethod = typeProcedure.addMethod("checkArgCount", new Type[]{typeProcedure, Type.intType}, (Type) Type.voidType, 9);
    public static String classPrefixDefault = "";
    private static final ThreadLocal<Compilation> current = new InheritableThreadLocal();
    public static boolean debugPrintExpr = false;
    public static boolean debugPrintFinalExpr;
    public static int defaultCallConvention;
    public static int defaultClassFileVersion = ClassType.JDK_1_5_VERSION;
    public static boolean emitSourceDebugExtAttr = true;
    public static final Field falseConstant = scmBooleanType.getDeclaredField("FALSE");
    public static boolean generateMainDefault = false;
    public static Method getCallContextInstanceMethod = typeCallContext.getDeclaredMethod("getInstance", 0);
    public static Method getCurrentEnvironmentMethod = typeEnvironment.addMethod("getCurrent", Type.typeArray0, (Type) typeEnvironment, 9);
    public static final Method getLocation1EnvironmentMethod = typeEnvironment.getDeclaredMethod("getLocation", 1);
    public static final Method getLocation2EnvironmentMethod = typeEnvironment.addMethod("getLocation", new Type[]{typeSymbol, Type.objectType}, (Type) typeLocation, 17);
    public static final Method getLocationMethod = typeLocation.addMethod("get", Type.typeArray0, (Type) Type.objectType, 1);
    public static final Method getProcedureBindingMethod = typeSymbol.addMethod("getProcedure", Type.typeArray0, (Type) typeProcedure, 1);
    public static final Method getSymbolProcedureMethod = typeLanguage.getDeclaredMethod("getSymbolProcedure", 1);
    public static final Method getSymbolValueMethod = typeLanguage.getDeclaredMethod("getSymbolValue", 1);
    public static boolean inlineOk = true;
    public static final Type[] int1Args = {Type.intType};
    public static ClassType javaStringType = typeString;
    static Method makeListMethod = scmListType.addMethod("makeList", new Type[]{objArrayType, Type.intType}, (Type) scmListType, 9);
    public static int moduleStatic = 0;
    public static Field noArgsField = typeValues.getDeclaredField("noArgs");
    public static final ArrayType objArrayType = ArrayType.make((Type) typeObject);
    public static Options options = new Options();
    public static Field pcCallContextField = typeCallContext.getDeclaredField("pc");
    public static Field procCallContextField = typeCallContext.getDeclaredField("proc");
    public static ClassType scmBooleanType = ClassType.make("java.lang.Boolean");
    public static ClassType scmKeywordType = ClassType.make("gnu.expr.Keyword");
    public static ClassType scmListType = ClassType.make("gnu.lists.LList");
    public static ClassType scmSequenceType = ClassType.make("gnu.lists.Sequence");
    static final Method setNameMethod = typeProcedure.getDeclaredMethod("setName", 1);
    public static final Type[] string1Arg = {javaStringType};
    public static final Type[] sym1Arg = string1Arg;
    public static final Field trueConstant = scmBooleanType.getDeclaredField("TRUE");
    public static ClassType typeApplet = ClassType.make("java.applet.Applet");
    public static ClassType typeCallContext = ClassType.make("gnu.mapping.CallContext");
    public static ClassType typeClass = Type.javalangClassType;
    public static ClassType typeClassType = ClassType.make("gnu.bytecode.ClassType");
    public static final ClassType typeConsumer = ClassType.make("gnu.lists.Consumer");
    public static ClassType typeEnvironment = ClassType.make("gnu.mapping.Environment");
    public static ClassType typeLanguage = ClassType.make("gnu.expr.Language");
    public static ClassType typeLocation = ClassType.make("gnu.mapping.Location");
    public static ClassType typeMethodProc = ClassType.make("gnu.mapping.MethodProc");
    public static ClassType typeModuleBody = ClassType.make("gnu.expr.ModuleBody");
    public static ClassType typeModuleMethod = ClassType.make("gnu.expr.ModuleMethod");
    public static ClassType typeModuleWithContext = ClassType.make("gnu.expr.ModuleWithContext");
    public static ClassType typeObject = Type.objectType;
    public static ClassType typeObjectType = ClassType.make("gnu.bytecode.ObjectType");
    public static ClassType typePair = ClassType.make("gnu.lists.Pair");
    public static ClassType typeProcedure = ClassType.make("gnu.mapping.Procedure");
    public static ClassType typeProcedure0 = ClassType.make("gnu.mapping.Procedure0");
    public static ClassType typeProcedure1 = ClassType.make("gnu.mapping.Procedure1");
    public static ClassType typeProcedure2 = ClassType.make("gnu.mapping.Procedure2");
    public static ClassType typeProcedure3 = ClassType.make("gnu.mapping.Procedure3");
    public static ClassType typeProcedure4 = ClassType.make("gnu.mapping.Procedure4");
    public static ClassType[] typeProcedureArray = {typeProcedure0, typeProcedure1, typeProcedure2, typeProcedure3, typeProcedure4};
    public static ClassType typeProcedureN = ClassType.make("gnu.mapping.ProcedureN");
    public static ClassType typeRunnable = ClassType.make("java.lang.Runnable");
    public static ClassType typeServlet = ClassType.make("gnu.kawa.servlet.KawaServlet");
    public static ClassType typeString = ClassType.make("java.lang.String");
    public static ClassType typeSymbol = ClassType.make("gnu.mapping.Symbol");
    public static ClassType typeType = ClassType.make("gnu.bytecode.Type");
    public static ClassType typeValues = ClassType.make("gnu.mapping.Values");
    public static Options.OptionInfo warnAsError = options.add("warn-as-error", 1, Boolean.FALSE, "Make all warnings into errors");
    public static Options.OptionInfo warnInvokeUnknownMethod = options.add("warn-invoke-unknown-method", 1, warnUnknownMember, "warn if invoke calls an unknown method (subsumed by warn-unknown-member)");
    public static Options.OptionInfo warnUndefinedVariable = options.add("warn-undefined-variable", 1, Boolean.TRUE, "warn if no compiler-visible binding for a variable");
    public static Options.OptionInfo warnUnknownMember = options.add("warn-unknown-member", 1, Boolean.TRUE, "warn if referencing an unknown method or field");
    Variable callContextVar;
    Variable callContextVarForInit;
    public String classPrefix = classPrefixDefault;
    ClassType[] classes;
    Initializer clinitChain;
    Method clinitMethod;
    public ClassType curClass;
    public LambdaExp curLambda;
    public Options currentOptions = new Options(options);
    protected ScopeExp current_scope;
    public boolean explicit;
    public Stack<Expression> exprStack;
    Method forNameHelper;
    SwitchState fswitch;
    Field fswitchIndex;
    public boolean generateMain = generateMainDefault;
    public boolean immediate;
    private int keyUninitialized;
    int langOptions;
    protected Language language;
    public Lexer lexer;
    public NameLookup lexical;
    LitTable litTable;
    ArrayClassLoader loader;
    int localFieldIndex;
    public ClassType mainClass;
    public ModuleExp mainLambda;
    int maxSelectorValue;
    protected SourceMessages messages;
    public Method method;
    int method_counter;
    public ModuleInfo minfo;
    public ClassType moduleClass;
    Field moduleInstanceMainField;
    Variable moduleInstanceVar;
    public boolean mustCompile = ModuleExp.alwaysCompile;
    private Compilation nextUninitialized;
    int numClasses;
    boolean pedantic;
    public Stack<Object> pendingImports;
    private int state;
    public Variable thisDecl;

    public int getState() {
        return this.state;
    }

    public void setState(int state2) {
        this.state = state2;
    }

    public boolean isPedantic() {
        return this.pedantic;
    }

    public void pushPendingImport(ModuleInfo info, ScopeExp defs, int formSize) {
        if (this.pendingImports == null) {
            this.pendingImports = new Stack<>();
        }
        this.pendingImports.push(info);
        this.pendingImports.push(defs);
        Expression posExp = new ReferenceExp((Object) null);
        posExp.setLine(this);
        this.pendingImports.push(posExp);
        this.pendingImports.push(Integer.valueOf(formSize));
    }

    public boolean warnUndefinedVariable() {
        return this.currentOptions.getBoolean(warnUndefinedVariable);
    }

    public boolean warnUnknownMember() {
        return this.currentOptions.getBoolean(warnUnknownMember);
    }

    public boolean warnInvokeUnknownMethod() {
        return this.currentOptions.getBoolean(warnInvokeUnknownMethod);
    }

    public boolean warnAsError() {
        return this.currentOptions.getBoolean(warnAsError);
    }

    public final boolean getBooleanOption(String key, boolean defaultValue) {
        return this.currentOptions.getBoolean(key, defaultValue);
    }

    public final boolean getBooleanOption(String key) {
        return this.currentOptions.getBoolean(key);
    }

    public boolean usingCPStyle() {
        return defaultCallConvention == 4;
    }

    public boolean usingTailCalls() {
        return defaultCallConvention >= 3;
    }

    public final CodeAttr getCode() {
        return this.method.getCode();
    }

    public boolean generatingApplet() {
        return (this.langOptions & 16) != 0;
    }

    public boolean generatingServlet() {
        return (this.langOptions & 32) != 0;
    }

    public boolean sharedModuleDefs() {
        return (this.langOptions & 2) != 0;
    }

    public void setSharedModuleDefs(boolean shared) {
        if (shared) {
            this.langOptions |= 2;
        } else {
            this.langOptions &= -3;
        }
    }

    public final ClassType getModuleType() {
        return defaultCallConvention >= 2 ? typeModuleWithContext : typeModuleBody;
    }

    public void compileConstant(Object value) {
        CodeAttr code = getCode();
        if (value == null) {
            code.emitPushNull();
        } else if (!(value instanceof String) || this.immediate) {
            code.emitGetStatic(compileConstantToField(value));
        } else {
            code.emitPushString((String) value);
        }
    }

    public Field compileConstantToField(Object value) {
        Literal literal = this.litTable.findLiteral(value);
        if (literal.field == null) {
            literal.assign(this.litTable);
        }
        return literal.field;
    }

    public boolean inlineOk(Expression proc) {
        if (proc instanceof LambdaExp) {
            LambdaExp lproc = (LambdaExp) proc;
            Declaration nameDecl = lproc.nameDecl;
            if (nameDecl == null || nameDecl.getSymbol() == null || !(nameDecl.context instanceof ModuleExp)) {
                return true;
            }
            if (this.immediate && nameDecl.isPublic() && !lproc.getFlag(2048) && (this.curLambda == null || lproc.topLevel() != this.curLambda.topLevel())) {
                return false;
            }
        }
        return inlineOk;
    }

    public boolean inlineOk(Procedure proc) {
        if (!this.immediate || !(proc instanceof ModuleMethod) || !(((ModuleMethod) proc).module.getClass().getClassLoader() instanceof ArrayClassLoader)) {
            return inlineOk;
        }
        return false;
    }

    public void compileConstant(Object value, Target target) {
        Type make;
        if (!(target instanceof IgnoreTarget)) {
            if (value instanceof Values) {
                if (target instanceof ConsumerTarget) {
                    for (Object compileConstant : ((Values) value).getValues()) {
                        compileConstant(compileConstant, target);
                    }
                    return;
                }
            }
            if (target instanceof ConditionalTarget) {
                ConditionalTarget ctarg = (ConditionalTarget) target;
                getCode().emitGoto(getLanguage().isTrue(value) ? ctarg.ifTrue : ctarg.ifFalse);
                return;
            }
            if (target instanceof StackTarget) {
                Type type = ((StackTarget) target).getType();
                if (type instanceof PrimType) {
                    try {
                        String signature = type.getSignature();
                        CodeAttr code = getCode();
                        char sig1 = (signature == null || signature.length() != 1) ? ' ' : signature.charAt(0);
                        if (value instanceof Number) {
                            Number num = (Number) value;
                            switch (sig1) {
                                case 'B':
                                    code.emitPushInt(num.byteValue());
                                    return;
                                case 'D':
                                    code.emitPushDouble(num.doubleValue());
                                    return;
                                case 'F':
                                    code.emitPushFloat(num.floatValue());
                                    return;
                                case 'I':
                                    code.emitPushInt(num.intValue());
                                    return;
                                case 'J':
                                    code.emitPushLong(num.longValue());
                                    return;
                                case 'S':
                                    code.emitPushInt(num.shortValue());
                                    return;
                            }
                        }
                        if (sig1 == 'C') {
                            code.emitPushInt(((PrimType) type).charValue(value));
                            return;
                        } else if (sig1 == 'Z') {
                            code.emitPushInt(PrimType.booleanValue(value) ? 1 : 0);
                            return;
                        }
                    } catch (ClassCastException e) {
                    }
                }
                if (type != typeClass || !(value instanceof ClassType)) {
                    try {
                        value = type.coerceFromObject(value);
                    } catch (Exception e2) {
                        StringBuffer sbuf = new StringBuffer();
                        if (value == Values.empty) {
                            sbuf.append("cannot convert void to ");
                        } else {
                            sbuf.append("cannot convert literal (of type ");
                            if (value == null) {
                                sbuf.append("<null>");
                            } else {
                                sbuf.append(value.getClass().getName());
                            }
                            sbuf.append(") to ");
                        }
                        sbuf.append(type.getName());
                        error('w', sbuf.toString());
                    }
                } else {
                    loadClassRef((ClassType) value);
                    return;
                }
            }
            compileConstant(value);
            if (value == null) {
                make = target.getType();
            } else {
                make = Type.make(value.getClass());
            }
            target.compileFromStack(this, make);
        }
    }

    private void dumpInitializers(Initializer inits) {
        for (Initializer init = Initializer.reverse(inits); init != null; init = init.next) {
            init.emit(this);
        }
    }

    public ClassType findNamedClass(String name) {
        for (int i = 0; i < this.numClasses; i++) {
            if (name.equals(this.classes[i].getName())) {
                return this.classes[i];
            }
        }
        return null;
    }

    private static void putURLWords(String name, StringBuffer sbuf) {
        int dot = name.indexOf(46);
        if (dot > 0) {
            putURLWords(name.substring(dot + 1), sbuf);
            sbuf.append('.');
            name = name.substring(0, dot);
        }
        sbuf.append(name);
    }

    public static String mangleURI(String name) {
        int end;
        int dot;
        int extLen;
        boolean hasSlash = name.indexOf(47) >= 0;
        int len = name.length();
        if (len > 6 && name.startsWith("class:")) {
            return name.substring(6);
        }
        if (len > 5 && name.charAt(4) == ':' && name.substring(0, 4).equalsIgnoreCase("http")) {
            name = name.substring(5);
            len -= 5;
            hasSlash = true;
        } else if (len > 4 && name.charAt(3) == ':' && name.substring(0, 3).equalsIgnoreCase("uri")) {
            name = name.substring(4);
            len -= 4;
        }
        int start = 0;
        StringBuffer sbuf = new StringBuffer();
        while (true) {
            int slash = name.indexOf(47, start);
            if (slash < 0) {
                end = len;
            } else {
                end = slash;
            }
            boolean first = sbuf.length() == 0;
            if (first && hasSlash) {
                String host = name.substring(start, end);
                if (end - start > 4 && host.startsWith("www.")) {
                    host = host.substring(4);
                }
                putURLWords(host, sbuf);
            } else if (start != end) {
                if (!first) {
                    sbuf.append('.');
                }
                if (end == len && (dot = name.lastIndexOf(46, len)) > start + 1 && !first && ((extLen = len - dot) <= 4 || (extLen == 5 && name.endsWith("html")))) {
                    len -= extLen;
                    end = len;
                    name = name.substring(0, len);
                }
                sbuf.append(name.substring(start, end));
            }
            if (slash < 0) {
                return sbuf.toString();
            }
            start = slash + 1;
        }
    }

    public static String mangleName(String name) {
        return mangleName(name, -1);
    }

    public static String mangleNameIfNeeded(String name) {
        return (name == null || isValidJavaName(name)) ? name : mangleName(name, 0);
    }

    public static boolean isValidJavaName(String name) {
        int len = name.length();
        if (len == 0 || !Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        int i = len;
        do {
            i--;
            if (i <= 0) {
                return true;
            }
        } while (Character.isJavaIdentifierPart(name.charAt(i)));
        return false;
    }

    public static String mangleName(String name, boolean reversible) {
        return mangleName(name, reversible ? 1 : -1);
    }

    public static String mangleName(String name, int kind) {
        boolean reversible;
        char first;
        char next;
        if (kind >= 0) {
            reversible = true;
        } else {
            reversible = false;
        }
        int len = name.length();
        if (len == 6 && name.equals("*init*")) {
            return "<init>";
        }
        StringBuffer mangled = new StringBuffer(len);
        boolean upcaseNext = false;
        int i = 0;
        while (i < len) {
            char ch = name.charAt(i);
            if (upcaseNext) {
                ch = Character.toTitleCase(ch);
                upcaseNext = false;
            }
            if (Character.isDigit(ch)) {
                if (i == 0) {
                    mangled.append("$N");
                }
                mangled.append(ch);
            } else if (Character.isLetter(ch) || ch == '_') {
                mangled.append(ch);
            } else if (ch == '$') {
                mangled.append(kind > 1 ? "$$" : "$");
            } else {
                switch (ch) {
                    case '!':
                        mangled.append("$Ex");
                        break;
                    case '\"':
                        mangled.append("$Dq");
                        break;
                    case '#':
                        mangled.append("$Nm");
                        break;
                    case '%':
                        mangled.append("$Pc");
                        break;
                    case '&':
                        mangled.append("$Am");
                        break;
                    case '\'':
                        mangled.append("$Sq");
                        break;
                    case '(':
                        mangled.append("$LP");
                        break;
                    case ')':
                        mangled.append("$RP");
                        break;
                    case '*':
                        mangled.append("$St");
                        break;
                    case '+':
                        mangled.append("$Pl");
                        break;
                    case ',':
                        mangled.append("$Cm");
                        break;
                    case '-':
                        if (!reversible) {
                            if (i + 1 < len) {
                                next = name.charAt(i + 1);
                            } else {
                                next = 0;
                            }
                            if (next != '>') {
                                if (!Character.isLowerCase(next)) {
                                    mangled.append("$Mn");
                                    break;
                                }
                            } else {
                                mangled.append("$To$");
                                i++;
                                break;
                            }
                        } else {
                            mangled.append("$Mn");
                            break;
                        }
                        break;
                    case '.':
                        mangled.append("$Dt");
                        break;
                    case '/':
                        mangled.append("$Sl");
                        break;
                    case ':':
                        mangled.append("$Cl");
                        break;
                    case ';':
                        mangled.append("$SC");
                        break;
                    case '<':
                        mangled.append("$Ls");
                        break;
                    case '=':
                        mangled.append("$Eq");
                        break;
                    case '>':
                        mangled.append("$Gr");
                        break;
                    case '?':
                        if (mangled.length() > 0) {
                            first = mangled.charAt(0);
                        } else {
                            first = 0;
                        }
                        if (!reversible && i + 1 == len && Character.isLowerCase(first)) {
                            mangled.setCharAt(0, Character.toTitleCase(first));
                            mangled.insert(0, "is");
                            break;
                        } else {
                            mangled.append("$Qu");
                            break;
                        }
                    case '@':
                        mangled.append("$At");
                        break;
                    case '[':
                        mangled.append("$LB");
                        break;
                    case ']':
                        mangled.append("$RB");
                        break;
                    case '^':
                        mangled.append("$Up");
                        break;
                    case '{':
                        mangled.append("$LC");
                        break;
                    case '|':
                        mangled.append("$VB");
                        break;
                    case '}':
                        mangled.append("$RC");
                        break;
                    case '~':
                        mangled.append("$Tl");
                        break;
                    default:
                        mangled.append('$');
                        mangled.append(Character.forDigit((ch >> 12) & 15, 16));
                        mangled.append(Character.forDigit((ch >> 8) & 15, 16));
                        mangled.append(Character.forDigit((ch >> 4) & 15, 16));
                        mangled.append(Character.forDigit(ch & 15, 16));
                        break;
                }
                if (!reversible) {
                    upcaseNext = true;
                }
            }
            i++;
        }
        String mname = mangled.toString();
        if (!mname.equals(name)) {
            return mname;
        }
        return name;
    }

    public static char demangle2(char char1, char char2) {
        switch ((char1 << 16) | char2) {
            case 'm':
                return '&';
            case 't':
                return '@';
            case 'l':
                return ':';
            case 'm':
                return ',';
            case 'q':
                return '\"';
            case 't':
                return '.';
            case 'q':
                return '=';
            case 'x':
                return '!';
            case 'r':
                return '>';
            case 'B':
                return '[';
            case 'C':
                return '{';
            case 'P':
                return '(';
            case 's':
                return '<';
            case 'c':
            case 'c':
                return '%';
            case 'n':
                return '-';
            case 'm':
                return '#';
            case 'l':
                return '+';
            case 'u':
                return '?';
            case 'B':
                return ']';
            case 'C':
                return '}';
            case 'P':
                return ')';
            case 'C':
                return ';';
            case 'l':
                return '/';
            case 'q':
                return '\\';
            case 't':
                return '*';
            case 'l':
                return '~';
            case 'p':
                return '^';
            case 'B':
                return '|';
            default:
                return LispReader.TOKEN_ESCAPE_CHAR;
        }
    }

    public static String demangleName(String name) {
        return demangleName(name, false);
    }

    public static String demangleName(String name, boolean reversible) {
        StringBuffer sbuf = new StringBuffer();
        int len = name.length();
        boolean mangled = false;
        boolean predicate = false;
        boolean downCaseNext = false;
        int i = 0;
        while (i < len) {
            char ch = name.charAt(i);
            if (downCaseNext && !reversible) {
                ch = Character.toLowerCase(ch);
                downCaseNext = false;
            }
            if (!reversible && ch == 'i' && i == 0 && len > 2 && name.charAt(i + 1) == 's') {
                char d = name.charAt(i + 2);
                if (!Character.isLowerCase(d)) {
                    mangled = true;
                    predicate = true;
                    i++;
                    if (Character.isUpperCase(d) || Character.isTitleCase(d)) {
                        sbuf.append(Character.toLowerCase(d));
                        i++;
                        i++;
                    } else {
                        i++;
                    }
                }
            }
            if (ch == '$' && i + 2 < len) {
                char c1 = name.charAt(i + 1);
                char c2 = name.charAt(i + 2);
                char d2 = demangle2(c1, c2);
                if (d2 != 65535) {
                    sbuf.append(d2);
                    i += 2;
                    mangled = true;
                    downCaseNext = true;
                } else if (c1 == 'T' && c2 == 'o' && i + 3 < len && name.charAt(i + 3) == '$') {
                    sbuf.append("->");
                    i += 3;
                    mangled = true;
                    downCaseNext = true;
                }
                i++;
            } else if (!reversible && i > 1 && ((Character.isUpperCase(ch) || Character.isTitleCase(ch)) && Character.isLowerCase(name.charAt(i - 1)))) {
                sbuf.append('-');
                mangled = true;
                ch = Character.toLowerCase(ch);
            }
            sbuf.append(ch);
            i++;
        }
        if (predicate) {
            sbuf.append('?');
        }
        return mangled ? sbuf.toString() : name;
    }

    public String generateClassName(String hint) {
        String hint2 = mangleName(hint, true);
        if (this.mainClass != null) {
            hint2 = this.mainClass.getName() + '$' + hint2;
        } else if (this.classPrefix != null) {
            hint2 = this.classPrefix + hint2;
        }
        if (findNamedClass(hint2) == null) {
            return hint2;
        }
        int i = 0;
        while (true) {
            String new_hint = hint2 + i;
            if (findNamedClass(new_hint) == null) {
                return new_hint;
            }
            i++;
        }
    }

    public Compilation(Language language2, SourceMessages messages2, NameLookup lexical2) {
        this.language = language2;
        this.messages = messages2;
        this.lexical = lexical2;
    }

    public void walkModule(ModuleExp mexp) {
        if (debugPrintExpr) {
            OutPort dout = OutPort.errDefault();
            dout.println("[Module:" + mexp.getName());
            mexp.print(dout);
            dout.println(']');
            dout.flush();
        }
        InlineCalls.inlineCalls(mexp, this);
        PushApply.pushApply(mexp);
        ChainLambdas.chainLambdas(mexp, this);
        FindTailCalls.findTailCalls(mexp, this);
    }

    public void outputClass(String directory) throws IOException {
        char dirSep = File.separatorChar;
        for (int iClass = 0; iClass < this.numClasses; iClass++) {
            ClassType clas = this.classes[iClass];
            String out_name = directory + clas.getName().replace('.', dirSep) + ".class";
            String parent = new File(out_name).getParent();
            if (parent != null) {
                new File(parent).mkdirs();
            }
            clas.writeToFile(out_name);
        }
        this.minfo.cleanupAfterCompilation();
    }

    public void cleanupAfterCompilation() {
        for (int iClass = 0; iClass < this.numClasses; iClass++) {
            this.classes[iClass].cleanupAfterCompilation();
        }
        this.classes = null;
        this.minfo.comp = null;
        if (this.minfo.exp != null) {
            this.minfo.exp.body = null;
        }
        this.mainLambda.body = null;
        this.mainLambda = null;
        if (!this.immediate) {
            this.litTable = null;
        }
    }

    public void compileToArchive(ModuleExp mexp, String fname) throws IOException {
        boolean makeJar;
        ZipOutputStream zout;
        if (fname.endsWith(".zip")) {
            makeJar = false;
        } else if (fname.endsWith(".jar")) {
            makeJar = true;
        } else {
            fname = fname + ".zip";
            makeJar = false;
        }
        process(12);
        File zar_file = new File(fname);
        if (zar_file.exists()) {
            zar_file.delete();
        }
        if (makeJar) {
            zout = new JarOutputStream(new FileOutputStream(zar_file));
        } else {
            zout = new ZipOutputStream(new FileOutputStream(zar_file));
        }
        byte[][] classBytes = new byte[this.numClasses][];
        CRC32 zcrc = new CRC32();
        for (int iClass = 0; iClass < this.numClasses; iClass++) {
            ClassType clas = this.classes[iClass];
            classBytes[iClass] = clas.writeToArray();
            ZipEntry zent = new ZipEntry(clas.getName().replace('.', '/') + ".class");
            zent.setSize((long) classBytes[iClass].length);
            zcrc.reset();
            zcrc.update(classBytes[iClass], 0, classBytes[iClass].length);
            zent.setCrc(zcrc.getValue());
            zout.putNextEntry(zent);
            zout.write(classBytes[iClass]);
        }
        zout.close();
    }

    private void registerClass(ClassType new_class) {
        if (this.classes == null) {
            this.classes = new ClassType[20];
        } else if (this.numClasses >= this.classes.length) {
            ClassType[] new_classes = new ClassType[(this.classes.length * 2)];
            System.arraycopy(this.classes, 0, new_classes, 0, this.numClasses);
            this.classes = new_classes;
        }
        new_class.addModifiers(new_class.isInterface() ? 1 : 33);
        if (new_class == this.mainClass && this.numClasses > 0) {
            new_class = this.classes[0];
            this.classes[0] = this.mainClass;
        }
        ClassType[] classTypeArr = this.classes;
        int i = this.numClasses;
        this.numClasses = i + 1;
        classTypeArr[i] = new_class;
    }

    public void addClass(ClassType new_class) {
        if (this.mainLambda.filename != null) {
            if (emitSourceDebugExtAttr) {
                new_class.setStratum(getLanguage().getName());
            }
            new_class.setSourceFile(this.mainLambda.filename);
        }
        registerClass(new_class);
        new_class.setClassfileVersion(defaultClassFileVersion);
    }

    public boolean makeRunnable() {
        return !generatingServlet() && !generatingApplet() && !getModule().staticInitRun();
    }

    public void addMainClass(ModuleExp module) {
        this.mainClass = module.classFor(this);
        ClassType type = this.mainClass;
        ClassType[] interfaces = module.getInterfaces();
        if (interfaces != null) {
            type.setInterfaces(interfaces);
        }
        ClassType sup = module.getSuperType();
        if (sup == null) {
            if (generatingApplet()) {
                sup = typeApplet;
            } else if (generatingServlet()) {
                sup = typeServlet;
            } else {
                sup = getModuleType();
            }
        }
        if (makeRunnable()) {
            type.addInterface(typeRunnable);
        }
        type.setSuper(sup);
        module.type = type;
        addClass(type);
        getConstructor(this.mainClass, module);
    }

    public final Method getConstructor(LambdaExp lexp) {
        return getConstructor(lexp.getHeapFrameType(), lexp);
    }

    public static final Method getConstructor(ClassType clas, LambdaExp lexp) {
        Method meth = clas.getDeclaredMethod("<init>", 0);
        if (meth != null) {
            return meth;
        }
        return clas.addMethod("<init>", 1, (!(lexp instanceof ClassExp) || lexp.staticLinkField == null) ? apply0args : new Type[]{lexp.staticLinkField.getType()}, (Type) Type.voidType);
    }

    public final void generateConstructor(LambdaExp lexp) {
        generateConstructor(lexp.getHeapFrameType(), lexp);
    }

    public final void generateConstructor(ClassType clas, LambdaExp lexp) {
        Method save_method = this.method;
        Variable callContextSave = this.callContextVar;
        this.callContextVar = null;
        ClassType save_class = this.curClass;
        this.curClass = clas;
        Method constructor_method = getConstructor(clas, lexp);
        clas.constructor = constructor_method;
        this.method = constructor_method;
        CodeAttr code = constructor_method.startCode();
        if ((lexp instanceof ClassExp) && lexp.staticLinkField != null) {
            code.emitPushThis();
            code.emitLoad(code.getCurrentScope().getVariable(1));
            code.emitPutField(lexp.staticLinkField);
        }
        ClassExp.invokeDefaultSuperConstructor(clas.getSuperclass(), this, lexp);
        if (!(this.curClass != this.mainClass || this.minfo == null || this.minfo.sourcePath == null)) {
            code.emitPushThis();
            code.emitInvokeStatic(ClassType.make("gnu.expr.ModuleInfo").getDeclaredMethod("register", 1));
        }
        if (!(lexp == null || lexp.initChain == null)) {
            LambdaExp save = this.curLambda;
            this.curLambda = new LambdaExp();
            this.curLambda.closureEnv = code.getArg(0);
            this.curLambda.outer = save;
            while (true) {
                Initializer init = lexp.initChain;
                if (init == null) {
                    break;
                }
                lexp.initChain = null;
                dumpInitializers(init);
            }
            this.curLambda = save;
        }
        if (lexp instanceof ClassExp) {
            callInitMethods(((ClassExp) lexp).getCompiledClassType(this), new Vector(10));
        }
        code.emitReturn();
        this.method = save_method;
        this.curClass = save_class;
        this.callContextVar = callContextSave;
    }

    /* JADX WARNING: type inference failed for: r9v10, types: [gnu.bytecode.Type] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void callInitMethods(gnu.bytecode.ClassType r12, java.util.Vector<gnu.bytecode.ClassType> r13) {
        /*
            r11 = this;
            if (r12 != 0) goto L_0x0003
        L_0x0002:
            return
        L_0x0003:
            java.lang.String r8 = r12.getName()
            java.lang.String r9 = "java.lang.Object"
            boolean r9 = r9.equals(r8)
            if (r9 != 0) goto L_0x0002
            int r4 = r13.size()
        L_0x0013:
            int r4 = r4 + -1
            if (r4 < 0) goto L_0x0024
            java.lang.Object r9 = r13.elementAt(r4)
            gnu.bytecode.ClassType r9 = (gnu.bytecode.ClassType) r9
            java.lang.String r9 = r9.getName()
            if (r9 != r8) goto L_0x0013
            goto L_0x0002
        L_0x0024:
            r13.addElement(r12)
            gnu.bytecode.ClassType[] r5 = r12.getInterfaces()
            if (r5 == 0) goto L_0x0039
            int r7 = r5.length
            r4 = 0
        L_0x002f:
            if (r4 >= r7) goto L_0x0039
            r9 = r5[r4]
            r11.callInitMethods(r9, r13)
            int r4 = r4 + 1
            goto L_0x002f
        L_0x0039:
            r1 = 1
            boolean r9 = r12.isInterface()
            if (r9 == 0) goto L_0x0081
            boolean r9 = r12 instanceof gnu.expr.PairClassType
            if (r9 == 0) goto L_0x005b
            gnu.expr.PairClassType r12 = (gnu.expr.PairClassType) r12
            gnu.bytecode.ClassType r12 = r12.instanceType
        L_0x0048:
            java.lang.String r9 = "$finit$"
            gnu.bytecode.Method r6 = r12.getDeclaredMethod((java.lang.String) r9, (int) r1)
            if (r6 == 0) goto L_0x0002
            gnu.bytecode.CodeAttr r2 = r11.getCode()
            r2.emitPushThis()
            r2.emitInvoke(r6)
            goto L_0x0002
        L_0x005b:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x007f }
            r9.<init>()     // Catch:{ Throwable -> 0x007f }
            java.lang.String r10 = r12.getName()     // Catch:{ Throwable -> 0x007f }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r10 = "$class"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x007f }
            java.lang.Class r9 = java.lang.Class.forName(r9)     // Catch:{ Throwable -> 0x007f }
            gnu.bytecode.Type r9 = gnu.bytecode.Type.make(r9)     // Catch:{ Throwable -> 0x007f }
            r0 = r9
            gnu.bytecode.ClassType r0 = (gnu.bytecode.ClassType) r0     // Catch:{ Throwable -> 0x007f }
            r12 = r0
            goto L_0x0048
        L_0x007f:
            r3 = move-exception
            goto L_0x0002
        L_0x0081:
            r1 = 0
            goto L_0x0048
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.Compilation.callInitMethods(gnu.bytecode.ClassType, java.util.Vector):void");
    }

    public void generateMatchMethods(LambdaExp lexp) {
        int methodIndex;
        int numApplyMethods = lexp.applyMethods == null ? 0 : lexp.applyMethods.size();
        if (numApplyMethods != 0) {
            Method save_method = this.method;
            ClassType save_class = this.curClass;
            ClassType procType = typeModuleMethod;
            this.curClass = lexp.getHeapFrameType();
            if (!this.curClass.getSuperclass().isSubtype(typeModuleBody)) {
                this.curClass = this.moduleClass;
            }
            CodeAttr code = null;
            int i = 0;
            while (i <= 5) {
                boolean needThisMatch = false;
                SwitchState aswitch = null;
                String mname = null;
                Type[] matchArgs = null;
                int j = numApplyMethods;
                while (true) {
                    j--;
                    if (j < 0) {
                        break;
                    }
                    LambdaExp source = (LambdaExp) lexp.applyMethods.elementAt(j);
                    int numMethods = source.primMethods.length;
                    boolean varArgs = source.max_args < 0 || source.max_args >= source.min_args + numMethods;
                    if (i < 5) {
                        methodIndex = i - source.min_args;
                        if (methodIndex >= 0 && methodIndex < numMethods) {
                            if (methodIndex != numMethods - 1 || !varArgs) {
                            }
                        }
                    } else {
                        int methodIndex2 = 5 - source.min_args;
                        if (methodIndex2 <= 0 || numMethods > methodIndex2 || varArgs) {
                            methodIndex = numMethods - 1;
                        }
                    }
                    if (!needThisMatch) {
                        if (i < 5) {
                            mname = "match" + i;
                            matchArgs = new Type[(i + 2)];
                            for (int k = i; k >= 0; k--) {
                                matchArgs[k + 1] = typeObject;
                            }
                            matchArgs[i + 1] = typeCallContext;
                        } else {
                            mname = "matchN";
                            matchArgs = new Type[3];
                            matchArgs[1] = objArrayType;
                            matchArgs[2] = typeCallContext;
                        }
                        matchArgs[0] = procType;
                        this.method = this.curClass.addMethod(mname, matchArgs, (Type) Type.intType, 1);
                        code = this.method.startCode();
                        code.emitLoad(code.getArg(1));
                        code.emitGetField(procType.getField("selector"));
                        aswitch = code.startSwitch();
                        needThisMatch = true;
                    }
                    aswitch.addCase(source.getSelectorValue(this), code);
                    int line = source.getLineNumber();
                    if (line > 0) {
                        code.putLineNumber(source.getFileName(), line);
                    }
                    Variable ctxVar = code.getArg(i == 5 ? 3 : i + 2);
                    if (i < 5) {
                        Declaration var = source.firstDecl();
                        for (int k2 = 1; k2 <= i; k2++) {
                            code.emitLoad(ctxVar);
                            code.emitLoad(code.getArg(k2 + 1));
                            Type ptype = var.getType();
                            if (ptype != Type.objectType) {
                                if (ptype instanceof TypeValue) {
                                    Label label = new Label(code);
                                    Label falseLabel = new Label(code);
                                    ConditionalTarget ctarget = new ConditionalTarget(label, falseLabel, getLanguage());
                                    code.emitDup();
                                    ((TypeValue) ptype).emitIsInstance((Variable) null, this, ctarget);
                                    falseLabel.define(code);
                                    code.emitPushInt(-786432 | k2);
                                    code.emitReturn();
                                    label.define(code);
                                } else if (!(!(ptype instanceof ClassType) || ptype == Type.objectType || ptype == Type.toStringType)) {
                                    code.emitDup();
                                    ptype.emitIsInstance(code);
                                    code.emitIfIntEqZero();
                                    code.emitPushInt(-786432 | k2);
                                    code.emitReturn();
                                    code.emitFi();
                                }
                            }
                            code.emitPutField(typeCallContext.getField("value" + k2));
                            var = var.nextDecl();
                        }
                    } else {
                        code.emitLoad(ctxVar);
                        code.emitLoad(code.getArg(2));
                        code.emitPutField(typeCallContext.getField("values"));
                    }
                    code.emitLoad(ctxVar);
                    if (defaultCallConvention < 2) {
                        code.emitLoad(code.getArg(1));
                    } else {
                        code.emitLoad(code.getArg(0));
                    }
                    code.emitPutField(procCallContextField);
                    code.emitLoad(ctxVar);
                    if (defaultCallConvention >= 2) {
                        code.emitPushInt(source.getSelectorValue(this) + methodIndex);
                    } else {
                        code.emitPushInt(i);
                    }
                    code.emitPutField(pcCallContextField);
                    code.emitPushInt(0);
                    code.emitReturn();
                }
                if (needThisMatch) {
                    aswitch.addDefault(code);
                    int nargs = (i > 4 ? 2 : i + 1) + 1;
                    for (int k3 = 0; k3 <= nargs; k3++) {
                        code.emitLoad(code.getArg(k3));
                    }
                    code.emitInvokeSpecial(typeModuleBody.getDeclaredMethod(mname, matchArgs.length));
                    code.emitReturn();
                    aswitch.finish(code);
                }
                i++;
            }
            this.method = save_method;
            this.curClass = save_class;
        }
    }

    public void generateApplyMethodsWithContext(LambdaExp lexp) {
        int explicitFrameArg;
        int numApplyMethods = lexp.applyMethods == null ? 0 : lexp.applyMethods.size();
        if (numApplyMethods != 0) {
            ClassType save_class = this.curClass;
            this.curClass = lexp.getHeapFrameType();
            if (!this.curClass.getSuperclass().isSubtype(typeModuleWithContext)) {
                this.curClass = this.moduleClass;
            }
            ClassType classType = typeModuleMethod;
            Method save_method = this.method;
            this.method = this.curClass.addMethod("apply", new Type[]{typeCallContext}, (Type) Type.voidType, 1);
            CodeAttr code = this.method.startCode();
            Variable ctxVar = code.getArg(1);
            code.emitLoad(ctxVar);
            code.emitGetField(pcCallContextField);
            SwitchState aswitch = code.startSwitch();
            for (int j = 0; j < numApplyMethods; j++) {
                LambdaExp source = (LambdaExp) lexp.applyMethods.elementAt(j);
                Method[] primMethods = source.primMethods;
                int numMethods = primMethods.length;
                int i = 0;
                while (i < numMethods) {
                    boolean varArgs = i == numMethods + -1 && (source.max_args < 0 || source.max_args >= source.min_args + numMethods);
                    int methodIndex = i;
                    aswitch.addCase(source.getSelectorValue(this) + i, code);
                    SourceLocator saveLoc1 = this.messages.swapSourceLocator(source);
                    int line = source.getLineNumber();
                    if (line > 0) {
                        code.putLineNumber(source.getFileName(), line);
                    }
                    Method primMethod = primMethods[methodIndex];
                    Type[] primArgTypes = primMethod.getParameterTypes();
                    int singleArgs = source.min_args + methodIndex;
                    Variable counter = null;
                    int pendingIfEnds = 0;
                    if (i > 4 && numMethods > 1) {
                        counter = code.addLocal(Type.intType);
                        code.emitLoad(ctxVar);
                        code.emitInvoke(typeCallContext.getDeclaredMethod("getArgCount", 0));
                        if (source.min_args != 0) {
                            code.emitPushInt(source.min_args);
                            code.emitSub(Type.intType);
                        }
                        code.emitStore(counter);
                    }
                    int needsThis = primMethod.getStaticFlag() ? 0 : 1;
                    if ((varArgs ? 2 : 1) + singleArgs < primArgTypes.length) {
                        explicitFrameArg = 1;
                    } else {
                        explicitFrameArg = 0;
                    }
                    if (needsThis + explicitFrameArg > 0) {
                        code.emitPushThis();
                        if (this.curClass == this.moduleClass && this.mainClass != this.moduleClass) {
                            code.emitGetField(this.moduleInstanceMainField);
                        }
                    }
                    Declaration var = source.firstDecl();
                    if (var != null && var.isThisParameter()) {
                        var = var.nextDecl();
                    }
                    for (int k = 0; k < singleArgs; k++) {
                        if (counter != null && k >= source.min_args) {
                            code.emitLoad(counter);
                            code.emitIfIntLEqZero();
                            code.emitLoad(ctxVar);
                            code.emitInvoke(primMethods[k - source.min_args]);
                            code.emitElse();
                            pendingIfEnds++;
                            code.emitInc(counter, -1);
                        }
                        code.emitLoad(ctxVar);
                        if (k > 4 || varArgs || source.max_args > 4) {
                            code.emitGetField(typeCallContext.getDeclaredField("values"));
                            code.emitPushInt(k);
                            code.emitArrayLoad(Type.objectType);
                        } else {
                            code.emitGetField(typeCallContext.getDeclaredField("value" + (k + 1)));
                        }
                        Type ptype = var.getType();
                        if (ptype != Type.objectType) {
                            SourceLocator saveLoc2 = this.messages.swapSourceLocator(var);
                            CheckedTarget.emitCheckedCoerce(this, source, k + 1, ptype);
                            this.messages.swapSourceLocator(saveLoc2);
                        }
                        var = var.nextDecl();
                    }
                    if (varArgs) {
                        Type lastArgType = primArgTypes[explicitFrameArg + singleArgs];
                        if (lastArgType instanceof ArrayType) {
                            varArgsToArray(source, singleArgs, counter, lastArgType, ctxVar);
                        } else if ("gnu.lists.LList".equals(lastArgType.getName())) {
                            code.emitLoad(ctxVar);
                            code.emitPushInt(singleArgs);
                            code.emitInvokeVirtual(typeCallContext.getDeclaredMethod("getRestArgsList", 1));
                        } else if (lastArgType == typeCallContext) {
                            code.emitLoad(ctxVar);
                        } else {
                            throw new RuntimeException("unsupported #!rest type:" + lastArgType);
                        }
                    }
                    code.emitLoad(ctxVar);
                    code.emitInvoke(primMethod);
                    while (true) {
                        pendingIfEnds--;
                        if (pendingIfEnds < 0) {
                            break;
                        }
                        code.emitFi();
                    }
                    if (defaultCallConvention < 2) {
                        Target.pushObject.compileFromStack(this, source.getReturnType());
                    }
                    this.messages.swapSourceLocator(saveLoc1);
                    code.emitReturn();
                    i++;
                }
            }
            aswitch.addDefault(code);
            code.emitInvokeStatic(typeModuleMethod.getDeclaredMethod("applyError", 0));
            code.emitReturn();
            aswitch.finish(code);
            this.method = save_method;
            this.curClass = save_class;
        }
    }

    public void generateApplyMethodsWithoutContext(LambdaExp lexp) {
        int methodIndex;
        int explicitFrameArg;
        int numApplyMethods = lexp.applyMethods == null ? 0 : lexp.applyMethods.size();
        if (numApplyMethods != 0) {
            ClassType save_class = this.curClass;
            this.curClass = lexp.getHeapFrameType();
            ClassType procType = typeModuleMethod;
            if (!this.curClass.getSuperclass().isSubtype(typeModuleBody)) {
                this.curClass = this.moduleClass;
            }
            Method save_method = this.method;
            CodeAttr code = null;
            int i = defaultCallConvention >= 2 ? 5 : 0;
            while (i < 6) {
                boolean needThisApply = false;
                SwitchState aswitch = null;
                String mname = null;
                Type[] applyArgs = null;
                for (int j = 0; j < numApplyMethods; j++) {
                    LambdaExp source = (LambdaExp) lexp.applyMethods.elementAt(j);
                    Method[] primMethods = source.primMethods;
                    int numMethods = primMethods.length;
                    boolean varArgs = source.max_args < 0 || source.max_args >= source.min_args + numMethods;
                    boolean skipThisProc = false;
                    if (i < 5) {
                        methodIndex = i - source.min_args;
                        if (methodIndex < 0 || methodIndex >= numMethods || (methodIndex == numMethods - 1 && varArgs)) {
                            skipThisProc = true;
                        }
                        numMethods = 1;
                        varArgs = false;
                    } else {
                        int methodIndex2 = 5 - source.min_args;
                        if (methodIndex2 > 0 && numMethods <= methodIndex2 && !varArgs) {
                            skipThisProc = true;
                        }
                        methodIndex = numMethods - 1;
                    }
                    if (!skipThisProc) {
                        if (!needThisApply) {
                            if (i < 5) {
                                mname = "apply" + i;
                                applyArgs = new Type[(i + 1)];
                                for (int k = i; k > 0; k--) {
                                    applyArgs[k] = typeObject;
                                }
                            } else {
                                mname = "applyN";
                                applyArgs = new Type[2];
                                applyArgs[1] = objArrayType;
                            }
                            applyArgs[0] = procType;
                            this.method = this.curClass.addMethod(mname, applyArgs, defaultCallConvention >= 2 ? Type.voidType : Type.objectType, 1);
                            code = this.method.startCode();
                            code.emitLoad(code.getArg(1));
                            code.emitGetField(procType.getField("selector"));
                            aswitch = code.startSwitch();
                            needThisApply = true;
                        }
                        aswitch.addCase(source.getSelectorValue(this), code);
                        SourceLocator saveLoc1 = this.messages.swapSourceLocator(source);
                        int line = source.getLineNumber();
                        if (line > 0) {
                            code.putLineNumber(source.getFileName(), line);
                        }
                        Method primMethod = primMethods[methodIndex];
                        Type[] primArgTypes = primMethod.getParameterTypes();
                        int singleArgs = source.min_args + methodIndex;
                        Variable counter = null;
                        int pendingIfEnds = 0;
                        if (i > 4 && numMethods > 1) {
                            counter = code.addLocal(Type.intType);
                            code.emitLoad(code.getArg(2));
                            code.emitArrayLength();
                            if (source.min_args != 0) {
                                code.emitPushInt(source.min_args);
                                code.emitSub(Type.intType);
                            }
                            code.emitStore(counter);
                        }
                        int needsThis = primMethod.getStaticFlag() ? 0 : 1;
                        if ((varArgs ? 1 : 0) + singleArgs < primArgTypes.length) {
                            explicitFrameArg = 1;
                        } else {
                            explicitFrameArg = 0;
                        }
                        if (needsThis + explicitFrameArg > 0) {
                            code.emitPushThis();
                            if (this.curClass == this.moduleClass && this.mainClass != this.moduleClass) {
                                code.emitGetField(this.moduleInstanceMainField);
                            }
                        }
                        Declaration var = source.firstDecl();
                        if (var != null && var.isThisParameter()) {
                            var = var.nextDecl();
                        }
                        for (int k2 = 0; k2 < singleArgs; k2++) {
                            if (counter != null && k2 >= source.min_args) {
                                code.emitLoad(counter);
                                code.emitIfIntLEqZero();
                                code.emitInvoke(primMethods[k2 - source.min_args]);
                                code.emitElse();
                                pendingIfEnds++;
                                code.emitInc(counter, -1);
                            }
                            Variable pvar = null;
                            if (i <= 4) {
                                pvar = code.getArg(k2 + 2);
                                code.emitLoad(pvar);
                            } else {
                                code.emitLoad(code.getArg(2));
                                code.emitPushInt(k2);
                                code.emitArrayLoad(Type.objectType);
                            }
                            Type ptype = var.getType();
                            if (ptype != Type.objectType) {
                                SourceLocator saveLoc2 = this.messages.swapSourceLocator(var);
                                CheckedTarget.emitCheckedCoerce(this, source, k2 + 1, ptype, pvar);
                                this.messages.swapSourceLocator(saveLoc2);
                            }
                            var = var.nextDecl();
                        }
                        if (varArgs) {
                            Type lastArgType = primArgTypes[explicitFrameArg + singleArgs];
                            if (lastArgType instanceof ArrayType) {
                                varArgsToArray(source, singleArgs, counter, lastArgType, (Variable) null);
                            } else if ("gnu.lists.LList".equals(lastArgType.getName())) {
                                code.emitLoad(code.getArg(2));
                                code.emitPushInt(singleArgs);
                                code.emitInvokeStatic(makeListMethod);
                            } else if (lastArgType == typeCallContext) {
                                code.emitLoad(code.getArg(2));
                            } else {
                                throw new RuntimeException("unsupported #!rest type:" + lastArgType);
                            }
                        }
                        code.emitInvoke(primMethod);
                        while (true) {
                            pendingIfEnds--;
                            if (pendingIfEnds < 0) {
                                break;
                            }
                            code.emitFi();
                        }
                        if (defaultCallConvention < 2) {
                            Target.pushObject.compileFromStack(this, source.getReturnType());
                        }
                        this.messages.swapSourceLocator(saveLoc1);
                        code.emitReturn();
                    }
                }
                if (needThisApply) {
                    aswitch.addDefault(code);
                    if (defaultCallConvention >= 2) {
                        code.emitInvokeStatic(typeModuleMethod.getDeclaredMethod("applyError", 0));
                    } else {
                        int nargs = (i > 4 ? 2 : i + 1) + 1;
                        for (int k3 = 0; k3 < nargs; k3++) {
                            code.emitLoad(code.getArg(k3));
                        }
                        code.emitInvokeSpecial(typeModuleBody.getDeclaredMethod(mname, applyArgs));
                    }
                    code.emitReturn();
                    aswitch.finish(code);
                }
                i++;
            }
            this.method = save_method;
            this.curClass = save_class;
        }
    }

    private void varArgsToArray(LambdaExp source, int singleArgs, Variable counter, Type lastArgType, Variable ctxVar) {
        boolean mustConvert;
        CodeAttr code = getCode();
        Type elType = ((ArrayType) lastArgType).getComponentType();
        if (!"java.lang.Object".equals(elType.getName())) {
            mustConvert = true;
        } else {
            mustConvert = false;
        }
        if (ctxVar != null && !mustConvert) {
            code.emitLoad(ctxVar);
            code.emitPushInt(singleArgs);
            code.emitInvokeVirtual(typeCallContext.getDeclaredMethod("getRestArgsArray", 1));
        } else if (singleArgs != 0 || mustConvert) {
            code.pushScope();
            if (counter == null) {
                counter = code.addLocal(Type.intType);
                if (ctxVar != null) {
                    code.emitLoad(ctxVar);
                    code.emitInvoke(typeCallContext.getDeclaredMethod("getArgCount", 0));
                } else {
                    code.emitLoad(code.getArg(2));
                    code.emitArrayLength();
                }
                if (singleArgs != 0) {
                    code.emitPushInt(singleArgs);
                    code.emitSub(Type.intType);
                }
                code.emitStore(counter);
            }
            code.emitLoad(counter);
            code.emitNewArray(elType.getImplementationType());
            Label testLabel = new Label(code);
            Label loopTopLabel = new Label(code);
            loopTopLabel.setTypes(code);
            code.emitGoto(testLabel);
            loopTopLabel.define(code);
            code.emitDup(1);
            code.emitLoad(counter);
            if (ctxVar != null) {
                code.emitLoad(ctxVar);
            } else {
                code.emitLoad(code.getArg(2));
            }
            code.emitLoad(counter);
            if (singleArgs != 0) {
                code.emitPushInt(singleArgs);
                code.emitAdd(Type.intType);
            }
            if (ctxVar != null) {
                code.emitInvokeVirtual(typeCallContext.getDeclaredMethod("getArgAsObject", 1));
            } else {
                code.emitArrayLoad(Type.objectType);
            }
            if (mustConvert) {
                CheckedTarget.emitCheckedCoerce(this, source, source.getName(), 0, elType, (Variable) null);
            }
            code.emitArrayStore(elType);
            testLabel.define(code);
            code.emitInc(counter, -1);
            code.emitLoad(counter);
            code.emitGotoIfIntGeZero(loopTopLabel);
            code.popScope();
        } else {
            code.emitLoad(code.getArg(2));
        }
    }

    private Method startClassInit() {
        Method registerMethod;
        this.method = this.curClass.addMethod("<clinit>", apply0args, (Type) Type.voidType, 9);
        CodeAttr code = this.method.startCode();
        if ((this.generateMain || generatingApplet() || generatingServlet()) && (registerMethod = ((ClassType) Type.make(getLanguage().getClass())).getDeclaredMethod("registerEnvironment", 0)) != null) {
            code.emitInvokeStatic(registerMethod);
        }
        return this.method;
    }

    public void process(int wantedState) {
        int i = 10;
        int i2 = 8;
        int i3 = 6;
        int i4 = 100;
        Compilation saveCompilation = setSaveCurrent(this);
        try {
            ModuleExp mexp = getModule();
            if (wantedState >= 4 && getState() < 3) {
                setState(3);
                this.language.parse(this, 0);
                this.lexer.close();
                this.lexer = null;
                setState(this.messages.seenErrors() ? 100 : 4);
                if (this.pendingImports != null) {
                    return;
                }
            }
            if (wantedState >= 6) {
                if (getState() < 6) {
                    addMainClass(mexp);
                    this.language.resolve(this);
                    if (this.messages.seenErrors()) {
                        i3 = 100;
                    }
                    setState(i3);
                }
            }
            if (!this.explicit && !this.immediate && this.minfo.checkCurrent(ModuleManager.getInstance(), System.currentTimeMillis())) {
                this.minfo.cleanupAfterCompilation();
                setState(14);
            }
            if (wantedState >= 8 && getState() < 8) {
                walkModule(mexp);
                if (this.messages.seenErrors()) {
                    i2 = 100;
                }
                setState(i2);
            }
            if (wantedState >= 10 && getState() < 10) {
                this.litTable = new LitTable(this);
                mexp.setCanRead(true);
                FindCapturedVars.findCapturedVars(mexp, this);
                mexp.allocFields(this);
                mexp.allocChildMethods(this);
                if (this.messages.seenErrors()) {
                    i = 100;
                }
                setState(i);
            }
            if (wantedState >= 12 && getState() < 12) {
                if (this.immediate) {
                    this.loader = new ArrayClassLoader(ObjectType.getContextClassLoader());
                }
                generateBytecode();
                if (!this.messages.seenErrors()) {
                    i4 = 12;
                }
                setState(i4);
            }
            if (wantedState >= 14 && getState() < 14) {
                outputClass(ModuleManager.getInstance().getCompilationDirectory());
                setState(14);
            }
            restoreCurrent(saveCompilation);
        } catch (SyntaxException ex) {
            setState(100);
            if (ex.getMessages() != getMessages()) {
                throw new RuntimeException("confussing syntax error: " + ex);
            }
        } catch (IOException ex2) {
            ex2.printStackTrace();
            error('f', "caught " + ex2);
            setState(100);
        } finally {
            restoreCurrent(saveCompilation);
        }
    }

    /* access modifiers changed from: package-private */
    public void generateBytecode() {
        Type[] arg_types;
        Variable declareThis;
        Variable variable;
        String mainPrefix;
        ModuleExp module = getModule();
        if (debugPrintFinalExpr) {
            OutPort dout = OutPort.errDefault();
            dout.println("[Compiling final " + module.getName() + " to " + this.mainClass.getName() + ":");
            module.print(dout);
            dout.println(']');
            dout.flush();
        }
        ClassType neededSuper = getModuleType();
        if (this.mainClass.getSuperclass().isSubtype(neededSuper)) {
            this.moduleClass = this.mainClass;
        } else {
            this.moduleClass = new ClassType(generateClassName("frame"));
            this.moduleClass.setSuper(neededSuper);
            addClass(this.moduleClass);
            generateConstructor(this.moduleClass, (LambdaExp) null);
        }
        this.curClass = module.type;
        LambdaExp saveLambda = this.curLambda;
        this.curLambda = module;
        if (!module.isHandlingTailCalls()) {
            if (module.min_args == module.max_args && module.min_args <= 4) {
                int arg_count = module.min_args;
                arg_types = new Type[arg_count];
                int i = arg_count;
                while (true) {
                    i--;
                    if (i < 0) {
                        break;
                    }
                    arg_types[i] = typeObject;
                }
            } else {
                arg_types = new Type[]{new ArrayType(typeObject)};
            }
        } else {
            arg_types = new Type[]{typeCallContext};
        }
        Variable heapFrame = module.heapFrame;
        boolean staticModule = module.isStatic();
        this.method = this.curClass.addMethod("run", arg_types, (Type) Type.voidType, 17);
        this.method.initCode();
        CodeAttr code = getCode();
        if (this.method.getStaticFlag()) {
            declareThis = null;
        } else {
            declareThis = module.declareThis(module.type);
        }
        this.thisDecl = declareThis;
        module.closureEnv = module.thisVariable;
        if (module.isStatic()) {
            variable = null;
        } else {
            variable = module.thisVariable;
        }
        module.heapFrame = variable;
        module.allocChildClasses(this);
        if (module.isHandlingTailCalls() || usingCPStyle()) {
            this.callContextVar = new Variable("$ctx", typeCallContext);
            module.getVarScope().addVariableAfter(this.thisDecl, this.callContextVar);
            this.callContextVar.setParameter(true);
        }
        int line = module.getLineNumber();
        if (line > 0) {
            code.putLineNumber(module.getFileName(), line);
        }
        module.allocParameters(this);
        module.enterFunction(this);
        if (usingCPStyle()) {
            loadCallContext();
            code.emitGetField(pcCallContextField);
            this.fswitch = code.startSwitch();
            this.fswitch.addCase(0, code);
        }
        module.compileBody(this);
        module.compileEnd(this);
        Label startLiterals = null;
        Label afterLiterals = null;
        Method initMethod = null;
        if (this.curClass == this.mainClass) {
            Method save_method = this.method;
            Variable callContextSave = this.callContextVar;
            this.callContextVar = null;
            initMethod = startClassInit();
            this.clinitMethod = initMethod;
            CodeAttr code2 = getCode();
            startLiterals = new Label(code2);
            afterLiterals = new Label(code2);
            code2.fixupChain(afterLiterals, startLiterals);
            if (staticModule) {
                generateConstructor(module);
                code2.emitNew(this.moduleClass);
                code2.emitDup((Type) this.moduleClass);
                code2.emitInvokeSpecial(this.moduleClass.constructor);
                this.moduleInstanceMainField = this.moduleClass.addField("$instance", this.moduleClass, 25);
                code2.emitPutStatic(this.moduleInstanceMainField);
            }
            while (true) {
                Initializer init = this.clinitChain;
                if (init == null) {
                    break;
                }
                this.clinitChain = null;
                dumpInitializers(init);
            }
            if (module.staticInitRun()) {
                code2.emitGetStatic(this.moduleInstanceMainField);
                code2.emitInvoke(typeModuleBody.getDeclaredMethod("run", 0));
            }
            code2.emitReturn();
            if (this.moduleClass != this.mainClass && !staticModule && !this.generateMain && !this.immediate) {
                this.method = this.curClass.addMethod("run", 1, Type.typeArray0, (Type) Type.voidType);
                CodeAttr code3 = this.method.startCode();
                Variable ctxVar = code3.addLocal(typeCallContext);
                Variable saveVar = code3.addLocal(typeConsumer);
                Variable exceptionVar = code3.addLocal(Type.javalangThrowableType);
                code3.emitInvokeStatic(getCallContextInstanceMethod);
                code3.emitStore(ctxVar);
                Field consumerFld = typeCallContext.getDeclaredField("consumer");
                code3.emitLoad(ctxVar);
                code3.emitGetField(consumerFld);
                code3.emitStore(saveVar);
                code3.emitLoad(ctxVar);
                code3.emitGetStatic(ClassType.make("gnu.lists.VoidConsumer").getDeclaredField("instance"));
                code3.emitPutField(consumerFld);
                code3.emitTryStart(false, Type.voidType);
                code3.emitPushThis();
                code3.emitLoad(ctxVar);
                code3.emitInvokeVirtual(save_method);
                code3.emitPushNull();
                code3.emitStore(exceptionVar);
                code3.emitTryEnd();
                code3.emitCatchStart(exceptionVar);
                code3.emitCatchEnd();
                code3.emitTryCatchEnd();
                code3.emitLoad(ctxVar);
                code3.emitLoad(exceptionVar);
                code3.emitLoad(saveVar);
                code3.emitInvokeStatic(typeModuleBody.getDeclaredMethod("runCleanup", 3));
                code3.emitReturn();
            }
            this.method = save_method;
            this.callContextVar = callContextSave;
        }
        module.generateApplyMethods(this);
        this.curLambda = saveLambda;
        module.heapFrame = heapFrame;
        if (usingCPStyle()) {
            this.fswitch.finish(getCode());
        }
        if (!(startLiterals == null && this.callContextVar == null)) {
            this.method = initMethod;
            CodeAttr code4 = getCode();
            Label label = new Label(code4);
            code4.fixupChain(startLiterals, label);
            if (this.callContextVarForInit != null) {
                code4.emitInvokeStatic(getCallContextInstanceMethod);
                code4.emitStore(this.callContextVarForInit);
            }
            try {
                if (this.immediate) {
                    code4.emitPushInt(registerForImmediateLiterals(this));
                    code4.emitInvokeStatic(ClassType.make("gnu.expr.Compilation").getDeclaredMethod("setupLiterals", 1));
                } else {
                    this.litTable.emit();
                }
            } catch (Throwable ex) {
                error('e', "Literals: Internal error:" + ex);
            }
            code4.fixupChain(label, afterLiterals);
        }
        if (this.generateMain && this.curClass == this.mainClass) {
            this.method = this.curClass.addMethod("main", 9, new Type[]{new ArrayType(javaStringType)}, (Type) Type.voidType);
            CodeAttr code5 = this.method.startCode();
            if (Shell.defaultFormatName != null) {
                code5.emitPushString(Shell.defaultFormatName);
                code5.emitInvokeStatic(ClassType.make("kawa.Shell").getDeclaredMethod("setDefaultFormat", 1));
            }
            code5.emitLoad(code5.getArg(0));
            code5.emitInvokeStatic(ClassType.make("gnu.expr.ApplicationMainSupport").getDeclaredMethod("processArgs", 1));
            if (this.moduleInstanceMainField != null) {
                code5.emitGetStatic(this.moduleInstanceMainField);
            } else {
                code5.emitNew(this.curClass);
                code5.emitDup((Type) this.curClass);
                code5.emitInvokeSpecial(this.curClass.constructor);
            }
            code5.emitInvokeVirtual(typeModuleBody.getDeclaredMethod("runAsMain", 0));
            code5.emitReturn();
        }
        if (this.minfo != null && this.minfo.getNamespaceUri() != null) {
            ModuleManager manager = ModuleManager.getInstance();
            String mainPrefix2 = this.mainClass.getName();
            int dot = mainPrefix2.lastIndexOf(46);
            if (dot < 0) {
                mainPrefix = "";
            } else {
                String mainPackage = mainPrefix2.substring(0, dot);
                try {
                    manager.loadPackageInfo(mainPackage);
                } catch (ClassNotFoundException e) {
                } catch (Throwable ex2) {
                    error('e', "error loading map for " + mainPackage + " - " + ex2);
                }
                mainPrefix = mainPrefix2.substring(0, dot + 1);
            }
            ClassType classType = new ClassType(mainPrefix + ModuleSet.MODULES_MAP);
            ClassType typeModuleSet = ClassType.make("gnu.expr.ModuleSet");
            classType.setSuper(typeModuleSet);
            registerClass(classType);
            this.method = classType.addMethod("<init>", 1, apply0args, (Type) Type.voidType);
            Method superConstructor = typeModuleSet.addMethod("<init>", 1, apply0args, (Type) Type.voidType);
            CodeAttr code6 = this.method.startCode();
            code6.emitPushThis();
            code6.emitInvokeSpecial(superConstructor);
            code6.emitReturn();
            ClassType typeModuleManager = ClassType.make("gnu.expr.ModuleManager");
            this.method = classType.addMethod("register", new Type[]{typeModuleManager}, (Type) Type.voidType, 1);
            CodeAttr code7 = this.method.startCode();
            Method reg = typeModuleManager.getDeclaredMethod("register", 3);
            int i2 = manager.numModules;
            while (true) {
                i2--;
                if (i2 >= 0) {
                    ModuleInfo mi = manager.modules[i2];
                    String miClassName = mi.getClassName();
                    if (miClassName != null && miClassName.startsWith(mainPrefix)) {
                        String moduleSource = mi.sourcePath;
                        String moduleUri = mi.getNamespaceUri();
                        code7.emitLoad(code7.getArg(1));
                        compileConstant(miClassName);
                        if (!Path.valueOf(moduleSource).isAbsolute()) {
                            try {
                                char sep = File.separatorChar;
                                String path = Path.toURL(manager.getCompilationDirectory() + mainPrefix.replace('.', sep)).toString();
                                int plen = path.length();
                                if (plen > 0 && path.charAt(plen - 1) != sep) {
                                    path = path + sep;
                                }
                                moduleSource = Path.relativize(mi.getSourceAbsPathname(), path);
                            } catch (Throwable ex3) {
                                throw new WrappedException("exception while fixing up '" + moduleSource + '\'', ex3);
                            }
                        }
                        compileConstant(moduleSource);
                        compileConstant(moduleUri);
                        code7.emitInvokeVirtual(reg);
                    }
                } else {
                    code7.emitReturn();
                    return;
                }
            }
        }
    }

    public Field allocLocalField(Type type, String name) {
        if (name == null) {
            StringBuilder append = new StringBuilder().append("tmp_");
            int i = this.localFieldIndex + 1;
            this.localFieldIndex = i;
            name = append.append(i).toString();
        }
        return this.curClass.addField(name, type, 0);
    }

    public final void loadCallContext() {
        CodeAttr code = getCode();
        if (this.callContextVar != null && !this.callContextVar.dead()) {
            code.emitLoad(this.callContextVar);
        } else if (this.method == this.clinitMethod) {
            this.callContextVar = new Variable("$ctx", typeCallContext);
            this.callContextVar.reserveLocal(code.getMaxLocals(), code);
            code.emitLoad(this.callContextVar);
            this.callContextVarForInit = this.callContextVar;
        } else {
            code.emitInvokeStatic(getCallContextInstanceMethod);
            code.emitDup();
            this.callContextVar = new Variable("$ctx", typeCallContext);
            code.getCurrentScope().addVariable(code, this.callContextVar);
            code.emitStore(this.callContextVar);
        }
    }

    public void freeLocalField(Field field) {
    }

    public Expression parse(Object input) {
        throw new Error("unimeplemented parse");
    }

    public Language getLanguage() {
        return this.language;
    }

    public LambdaExp currentLambda() {
        return this.current_scope.currentLambda();
    }

    public final ModuleExp getModule() {
        return this.mainLambda;
    }

    public void setModule(ModuleExp mexp) {
        this.mainLambda = mexp;
    }

    public boolean isStatic() {
        return this.mainLambda.isStatic();
    }

    public ModuleExp currentModule() {
        return this.current_scope.currentModule();
    }

    public void mustCompileHere() {
        if (this.mustCompile || ModuleExp.compilerAvailable) {
            this.mustCompile = true;
        } else {
            error('w', "this expression claimed that it must be compiled, but compiler is unavailable");
        }
    }

    public ScopeExp currentScope() {
        return this.current_scope;
    }

    public void setCurrentScope(ScopeExp scope) {
        int scope_nesting = ScopeExp.nesting(scope);
        int current_nesting = ScopeExp.nesting(this.current_scope);
        while (current_nesting > scope_nesting) {
            pop(this.current_scope);
            current_nesting--;
        }
        ScopeExp sc = scope;
        while (scope_nesting > current_nesting) {
            sc = sc.outer;
            scope_nesting--;
        }
        while (sc != this.current_scope) {
            pop(this.current_scope);
            sc = sc.outer;
        }
        pushChain(scope, sc);
    }

    /* access modifiers changed from: package-private */
    public void pushChain(ScopeExp scope, ScopeExp limit) {
        if (scope != limit) {
            pushChain(scope.outer, limit);
            pushScope(scope);
            this.lexical.push(scope);
        }
    }

    public ModuleExp pushNewModule(Lexer lexer2) {
        this.lexer = lexer2;
        return pushNewModule(lexer2.getName());
    }

    public ModuleExp pushNewModule(String filename) {
        ModuleExp module = new ModuleExp();
        if (filename != null) {
            module.setFile(filename);
        }
        if (generatingApplet() || generatingServlet()) {
            module.setFlag(131072);
        }
        if (this.immediate) {
            module.setFlag(1048576);
            new ModuleInfo().setCompilation(this);
        }
        this.mainLambda = module;
        push((ScopeExp) module);
        return module;
    }

    public void push(ScopeExp scope) {
        pushScope(scope);
        this.lexical.push(scope);
    }

    public final void pushScope(ScopeExp scope) {
        if (!this.mustCompile && (scope.mustCompile() || (ModuleExp.compilerAvailable && (scope instanceof LambdaExp) && !(scope instanceof ModuleExp)))) {
            mustCompileHere();
        }
        scope.outer = this.current_scope;
        this.current_scope = scope;
    }

    public void pop(ScopeExp scope) {
        this.lexical.pop(scope);
        this.current_scope = scope.outer;
    }

    public final void pop() {
        pop(this.current_scope);
    }

    public void push(Declaration decl) {
        this.lexical.push(decl);
    }

    public Declaration lookup(Object name, int namespace) {
        return this.lexical.lookup(name, namespace);
    }

    public void usedClass(Type type) {
        while (type instanceof ArrayType) {
            type = ((ArrayType) type).getComponentType();
        }
        if (this.immediate && (type instanceof ClassType)) {
            this.loader.addClass((ClassType) type);
        }
    }

    public SourceMessages getMessages() {
        return this.messages;
    }

    public void setMessages(SourceMessages messages2) {
        this.messages = messages2;
    }

    public void error(char severity, String message, SourceLocator location) {
        String file = location.getFileName();
        int line = location.getLineNumber();
        int column = location.getColumnNumber();
        if (file == null || line <= 0) {
            file = getFileName();
            line = getLineNumber();
            column = getColumnNumber();
        }
        if (severity == 'w' && warnAsError()) {
            severity = 'e';
        }
        this.messages.error(severity, file, line, column, message);
    }

    public void error(char severity, String message) {
        if (severity == 'w' && warnAsError()) {
            severity = 'e';
        }
        this.messages.error(severity, (SourceLocator) this, message);
    }

    public void error(char severity, Declaration decl, String msg1, String msg2) {
        error(severity, msg1 + decl.getName() + msg2, (String) null, decl);
    }

    public void error(char severity, String message, String code, Declaration decl) {
        if (severity == 'w' && warnAsError()) {
            severity = 'e';
        }
        String filename = getFileName();
        int line = getLineNumber();
        int column = getColumnNumber();
        int decl_line = decl.getLineNumber();
        if (decl_line > 0) {
            filename = decl.getFileName();
            line = decl_line;
            column = decl.getColumnNumber();
        }
        this.messages.error(severity, filename, line, column, message, code);
    }

    public Expression syntaxError(String message) {
        error('e', message);
        return new ErrorExp(message);
    }

    public final int getLineNumber() {
        return this.messages.getLineNumber();
    }

    public final int getColumnNumber() {
        return this.messages.getColumnNumber();
    }

    public final String getFileName() {
        return this.messages.getFileName();
    }

    public String getPublicId() {
        return this.messages.getPublicId();
    }

    public String getSystemId() {
        return this.messages.getSystemId();
    }

    public boolean isStableSourceLocation() {
        return false;
    }

    public void setFile(String filename) {
        this.messages.setFile(filename);
    }

    public void setLine(int line) {
        this.messages.setLine(line);
    }

    public void setColumn(int column) {
        this.messages.setColumn(column);
    }

    public final void setLine(Expression position) {
        this.messages.setLocation(position);
    }

    public void setLine(Object location) {
        if (location instanceof SourceLocator) {
            this.messages.setLocation((SourceLocator) location);
        }
    }

    public final void setLocation(SourceLocator position) {
        this.messages.setLocation(position);
    }

    public void setLine(String filename, int line, int column) {
        this.messages.setLine(filename, line, column);
    }

    public void letStart() {
        pushScope(new LetExp((Expression[]) null));
    }

    public Declaration letVariable(Object name, Type type, Expression init) {
        Declaration decl = ((LetExp) this.current_scope).addDeclaration(name, type);
        decl.noteValue(init);
        return decl;
    }

    public void letEnter() {
        LetExp let = (LetExp) this.current_scope;
        Expression[] inits = new Expression[let.countDecls()];
        Declaration decl = let.firstDecl();
        int i = 0;
        while (decl != null) {
            inits[i] = decl.getValue();
            decl = decl.nextDecl();
            i++;
        }
        let.inits = inits;
        this.lexical.push((ScopeExp) let);
    }

    public LetExp letDone(Expression body) {
        LetExp let = (LetExp) this.current_scope;
        let.body = body;
        pop(let);
        return let;
    }

    private void checkLoop() {
        if (((LambdaExp) this.current_scope).getName() != "%do%loop") {
            throw new Error("internal error - bad loop state");
        }
    }

    public void loopStart() {
        LambdaExp loopLambda = new LambdaExp();
        LetExp let = new LetExp(new Expression[]{loopLambda});
        let.addDeclaration((Object) "%do%loop").noteValue(loopLambda);
        loopLambda.setName("%do%loop");
        let.outer = this.current_scope;
        loopLambda.outer = let;
        this.current_scope = loopLambda;
    }

    public Declaration loopVariable(Object name, Type type, Expression init) {
        checkLoop();
        LambdaExp loopLambda = (LambdaExp) this.current_scope;
        Declaration decl = loopLambda.addDeclaration(name, type);
        if (this.exprStack == null) {
            this.exprStack = new Stack<>();
        }
        this.exprStack.push(init);
        loopLambda.min_args++;
        return decl;
    }

    public void loopEnter() {
        checkLoop();
        LambdaExp loopLambda = (LambdaExp) this.current_scope;
        int ninits = loopLambda.min_args;
        loopLambda.max_args = ninits;
        Expression[] inits = new Expression[ninits];
        int i = ninits;
        while (true) {
            i--;
            if (i >= 0) {
                inits[i] = this.exprStack.pop();
            } else {
                LetExp let = (LetExp) loopLambda.outer;
                let.setBody(new ApplyExp((Expression) new ReferenceExp(let.firstDecl()), inits));
                this.lexical.push((ScopeExp) loopLambda);
                return;
            }
        }
    }

    public void loopCond(Expression cond) {
        checkLoop();
        this.exprStack.push(cond);
    }

    public void loopBody(Expression body) {
        ((LambdaExp) this.current_scope).body = body;
    }

    public Expression loopRepeat(Expression[] exps) {
        LambdaExp loopLambda = (LambdaExp) this.current_scope;
        ScopeExp let = loopLambda.outer;
        loopLambda.body = new IfExp(this.exprStack.pop(), new BeginExp(loopLambda.body, new ApplyExp((Expression) new ReferenceExp(let.firstDecl()), exps)), QuoteExp.voidExp);
        this.lexical.pop((ScopeExp) loopLambda);
        this.current_scope = let.outer;
        return let;
    }

    public Expression loopRepeat() {
        return loopRepeat(Expression.noExpressions);
    }

    public Expression loopRepeat(Expression exp) {
        return loopRepeat(new Expression[]{exp});
    }

    public static ApplyExp makeCoercion(Expression value, Expression type) {
        return new ApplyExp((Expression) new QuoteExp(Convert.getInstance()), type, value);
    }

    public static Expression makeCoercion(Expression value, Type type) {
        return makeCoercion(value, (Expression) new QuoteExp(type));
    }

    public void loadClassRef(ObjectType clas) {
        CodeAttr code = getCode();
        if (this.curClass.getClassfileVersion() >= 3211264) {
            code.emitPushClass(clas);
        } else if (clas != this.mainClass || !this.mainLambda.isStatic() || this.moduleInstanceMainField == null) {
            code.emitPushString(clas instanceof ClassType ? clas.getName() : clas.getInternalName().replace('/', '.'));
            code.emitInvokeStatic(getForNameHelper());
        } else {
            code.emitGetStatic(this.moduleInstanceMainField);
            code.emitInvokeVirtual(Type.objectType.getDeclaredMethod("getClass", 0));
        }
    }

    public Method getForNameHelper() {
        if (this.forNameHelper == null) {
            Method save_method = this.method;
            this.method = this.curClass.addMethod("class$", 9, string1Arg, (Type) typeClass);
            this.forNameHelper = this.method;
            CodeAttr code = this.method.startCode();
            code.emitLoad(code.getArg(0));
            code.emitPushInt(0);
            code.emitPushString(this.mainClass.getName());
            code.emitInvokeStatic(typeClass.getDeclaredMethod("forName", 1));
            code.emitInvokeVirtual(typeClass.getDeclaredMethod("getClassLoader", 0));
            code.emitInvokeStatic(typeClass.getDeclaredMethod("forName", 3));
            code.emitReturn();
            this.method = save_method;
        }
        return this.forNameHelper;
    }

    public Object resolve(Object name, boolean function) {
        Symbol symbol;
        Environment env = Environment.getCurrent();
        if (name instanceof String) {
            symbol = env.defaultNamespace().lookup((String) name);
        } else {
            symbol = (Symbol) name;
        }
        if (symbol == null) {
            return null;
        }
        if (!function || !getLanguage().hasSeparateFunctionNamespace()) {
            return env.get((EnvironmentKey) symbol, (Object) null);
        }
        return env.getFunction(symbol, (Object) null);
    }

    public static void setupLiterals(int key) {
        Compilation comp = findForImmediateLiterals(key);
        try {
            Class clas = comp.loader.loadClass(comp.mainClass.getName());
            for (Literal init = comp.litTable.literalsChain; init != null; init = init.next) {
                clas.getDeclaredField(init.field.getName()).set((Object) null, init.value);
            }
            comp.litTable = null;
        } catch (Throwable ex) {
            throw new WrappedException("internal error", ex);
        }
    }

    public static synchronized int registerForImmediateLiterals(Compilation comp) {
        int i;
        synchronized (Compilation.class) {
            i = 0;
            for (Compilation c = chainUninitialized; c != null; c = c.nextUninitialized) {
                if (i <= c.keyUninitialized) {
                    i = c.keyUninitialized + 1;
                }
            }
            comp.keyUninitialized = i;
            comp.nextUninitialized = chainUninitialized;
            chainUninitialized = comp;
        }
        return i;
    }

    public static synchronized Compilation findForImmediateLiterals(int key) {
        Compilation comp;
        Compilation next;
        synchronized (Compilation.class) {
            Compilation prev = null;
            comp = chainUninitialized;
            while (true) {
                next = comp.nextUninitialized;
                if (comp.keyUninitialized == key) {
                    break;
                }
                prev = comp;
                comp = next;
            }
            if (prev == null) {
                chainUninitialized = next;
            } else {
                prev.nextUninitialized = next;
            }
            comp.nextUninitialized = null;
        }
        return comp;
    }

    public static Compilation getCurrent() {
        return current.get();
    }

    public static void setCurrent(Compilation comp) {
        current.set(comp);
    }

    public static Compilation setSaveCurrent(Compilation comp) {
        Compilation save = current.get();
        current.set(comp);
        return save;
    }

    public static void restoreCurrent(Compilation saved) {
        current.set(saved);
    }

    public String toString() {
        return "<compilation " + this.mainLambda + ">";
    }
}
