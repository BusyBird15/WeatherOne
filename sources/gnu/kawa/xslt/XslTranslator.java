package gnu.kawa.xslt;

import com.google.appinventor.components.common.PropertyTypeConstants;
import gnu.bytecode.ClassType;
import gnu.bytecode.Method;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.IfExp;
import gnu.expr.LambdaExp;
import gnu.expr.ModuleExp;
import gnu.expr.PrimProcedure;
import gnu.expr.QuoteExp;
import gnu.kawa.functions.AppendValues;
import gnu.kawa.xml.MakeAttribute;
import gnu.kawa.xml.MakeElement;
import gnu.lists.Consumer;
import gnu.mapping.CharArrayInPort;
import gnu.mapping.InPort;
import gnu.mapping.Procedure;
import gnu.mapping.Symbol;
import gnu.math.DFloNum;
import gnu.math.IntNum;
import gnu.text.Lexer;
import gnu.text.LineBufferedReader;
import gnu.text.SourceMessages;
import gnu.xml.XMLParser;
import gnu.xml.XName;
import gnu.xquery.lang.XQParser;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

public class XslTranslator extends Lexer implements Consumer {
    static final String XSL_TRANSFORM_URI = "http://www.w3.org/1999/XSL/Transform";
    static final Method applyTemplatesMethod = typeXSLT.getDeclaredMethod("applyTemplates", 2);
    static final PrimProcedure applyTemplatesProc = new PrimProcedure(applyTemplatesMethod);
    static final Method defineTemplateMethod = typeXSLT.getDeclaredMethod("defineTemplate", 5);
    static final PrimProcedure defineTemplateProc = new PrimProcedure(defineTemplateMethod);
    static final Method runStylesheetMethod = typeXSLT.getDeclaredMethod("runStylesheet", 0);
    static final PrimProcedure runStylesheetProc = new PrimProcedure(runStylesheetMethod);
    static final ClassType typeTemplateTable = ClassType.make("gnu.kawa.xslt.TemplateTable");
    static final ClassType typeXSLT = ClassType.make("gnu.kawa.xslt.XSLT");
    Object attributeType;
    StringBuffer attributeValue = new StringBuffer(100);
    Compilation comp;
    Declaration consumerDecl;
    InPort in;
    boolean inAttribute;
    boolean inTemplate;
    XSLT interpreter;
    ModuleExp mexp;
    StringBuffer nesting = new StringBuffer(100);
    boolean preserveSpace;
    LambdaExp templateLambda;

    XslTranslator(InPort inp, SourceMessages messages, XSLT interpreter2) {
        super(inp, messages);
        this.interpreter = interpreter2;
        this.in = inp;
    }

    /* access modifiers changed from: package-private */
    public void maybeSkipWhitespace() {
        if (!this.preserveSpace) {
            int size = this.comp.exprStack.size();
            while (true) {
                size--;
                if (size < 0) {
                    break;
                }
                Expression expr = (Expression) this.comp.exprStack.elementAt(size);
                if (!(expr instanceof QuoteExp)) {
                    break;
                }
                Object value = ((QuoteExp) expr).getValue();
                String str = value == null ? "" : value.toString();
                int j = str.length();
                while (true) {
                    j--;
                    if (j >= 0) {
                        char ch = str.charAt(j);
                        if (ch != ' ' && ch != 9 && ch != 13 && ch != 10) {
                            return;
                        }
                    }
                }
            }
            this.comp.exprStack.setSize(size + 1);
        }
    }

    public String popMatchingAttribute(String ns, String name, int start) {
        int size = this.comp.exprStack.size();
        for (int i = start; i < size; i++) {
            Object el = this.comp.exprStack.elementAt(start);
            if (!(el instanceof ApplyExp)) {
                return null;
            }
            ApplyExp aexp = (ApplyExp) el;
            Expression function = aexp.getFunction();
            if (aexp.getFunction() != MakeAttribute.makeAttributeExp) {
                return null;
            }
            Expression[] args = aexp.getArgs();
            if (args.length != 2) {
                return null;
            }
            Expression arg0 = args[0];
            if (!(arg0 instanceof QuoteExp)) {
                return null;
            }
            Object tag = ((QuoteExp) arg0).getValue();
            if (!(tag instanceof Symbol)) {
                return null;
            }
            Symbol stag = (Symbol) tag;
            if (stag.getLocalPart() == name && stag.getNamespaceURI() == ns) {
                this.comp.exprStack.removeElementAt(i);
                return (String) ((QuoteExp) args[1]).getValue();
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Expression popTemplateBody(int start) {
        int i = this.comp.exprStack.size() - start;
        Expression[] args = new Expression[i];
        while (true) {
            i--;
            if (i < 0) {
                return new ApplyExp((Procedure) AppendValues.appendValues, args);
            }
            args[i] = this.comp.exprStack.pop();
        }
    }

    public static String isXslTag(Object type) {
        if (type instanceof QuoteExp) {
            type = ((QuoteExp) type).getValue();
        }
        if (!(type instanceof Symbol)) {
            return null;
        }
        Symbol qname = (Symbol) type;
        if (qname.getNamespaceURI() == XSL_TRANSFORM_URI) {
            return qname.getLocalName();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void append(Expression expr) {
    }

    public void startElement(Object type) {
        maybeSkipWhitespace();
        String xslTag = isXslTag(type);
        if (xslTag == "template") {
            if (this.templateLambda != null) {
                error("nested xsl:template");
            }
            this.templateLambda = new LambdaExp();
        } else if (xslTag == PropertyTypeConstants.PROPERTY_TYPE_TEXT) {
            this.preserveSpace = false;
        }
        if (type instanceof XName) {
            XName xn = (XName) type;
            type = Symbol.make(xn.getNamespaceURI(), xn.getLocalPart(), xn.getPrefix());
        }
        this.nesting.append((char) this.comp.exprStack.size());
        push(type);
    }

    public void startAttribute(Object attrType) {
        if (this.inAttribute) {
            error('f', "internal error - attribute inside attribute");
        }
        this.attributeType = attrType;
        this.attributeValue.setLength(0);
        this.nesting.append((char) this.comp.exprStack.size());
        this.inAttribute = true;
    }

    public void endAttribute() {
        push((Expression) new ApplyExp((Expression) MakeAttribute.makeAttributeExp, new QuoteExp(this.attributeType), new QuoteExp(this.attributeValue.toString())));
        this.nesting.setLength(this.nesting.length() - 1);
        this.inAttribute = false;
    }

    public void endElement() {
        maybeSkipWhitespace();
        int nlen = this.nesting.length() - 1;
        char charAt = this.nesting.charAt(nlen);
        this.nesting.setLength(nlen);
        String xslTag = isXslTag((Expression) this.comp.exprStack.elementAt(charAt));
        if (xslTag == "value-of") {
            String select = popMatchingAttribute("", "select", charAt + 1);
            if (select != null) {
                Expression exp = new ApplyExp(XQParser.makeText, parseXPath(select));
                this.comp.exprStack.pop();
                push(exp);
            }
        } else if (xslTag == "copy-of") {
            String select2 = popMatchingAttribute("", "select", charAt + 1);
            if (select2 != null) {
                Expression exp2 = parseXPath(select2);
                this.comp.exprStack.pop();
                push(exp2);
            }
        } else if (xslTag == "apply-templates") {
            Expression[] args = {new QuoteExp(popMatchingAttribute("", "select", charAt + 1)), resolveQNameExpression(popMatchingAttribute("", "mode", charAt + 1))};
            this.comp.exprStack.pop();
            push((Expression) new ApplyExp((Expression) new QuoteExp(applyTemplatesProc), args));
        } else if (xslTag == "if") {
            Expression test = XQParser.booleanValue(parseXPath(popMatchingAttribute("", "test", charAt + 1)));
            Expression clause = popTemplateBody(charAt + 1);
            this.comp.exprStack.pop();
            push((Expression) new IfExp(test, clause, QuoteExp.voidExp));
        } else if (xslTag == "stylesheet" || xslTag == "transform") {
            String popMatchingAttribute = popMatchingAttribute("", "version", charAt + 1);
            push((Expression) new ApplyExp((Expression) new QuoteExp(runStylesheetProc), Expression.noExpressions));
            Expression body = popTemplateBody(charAt + 1);
            push(body);
            this.mexp.body = body;
        } else if (xslTag == "template") {
            String match = popMatchingAttribute("", "match", charAt + 1);
            String name = popMatchingAttribute("", "name", charAt + 1);
            String popMatchingAttribute2 = popMatchingAttribute("", "priority", charAt + 1);
            String mode = popMatchingAttribute("", "mode", charAt + 1);
            this.templateLambda.body = popTemplateBody(charAt + 1);
            this.comp.exprStack.pop();
            push((Expression) new ApplyExp((Expression) new QuoteExp(defineTemplateProc), resolveQNameExpression(name), new QuoteExp(match), new QuoteExp(DFloNum.make(0.0d)), resolveQNameExpression(mode), this.templateLambda));
            this.templateLambda = null;
        } else if (xslTag == PropertyTypeConstants.PROPERTY_TYPE_TEXT) {
            this.preserveSpace = false;
            Expression[] args2 = new Expression[((this.comp.exprStack.size() - charAt) - 1)];
            int i = args2.length;
            while (true) {
                i--;
                if (i >= 0) {
                    args2[i] = this.comp.exprStack.pop();
                } else {
                    this.comp.exprStack.pop();
                    Expression exp3 = new ApplyExp(XQParser.makeText, args2);
                    push(exp3);
                    this.mexp.body = exp3;
                    return;
                }
            }
        } else {
            Expression[] args3 = new Expression[(this.comp.exprStack.size() - charAt)];
            int i2 = args3.length;
            while (true) {
                i2--;
                if (i2 >= 0) {
                    args3[i2] = this.comp.exprStack.pop();
                } else {
                    Expression exp4 = new ApplyExp((Expression) new QuoteExp(new MakeElement()), args3);
                    push(exp4);
                    this.mexp.body = exp4;
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Expression parseXPath(String string) {
        try {
            XQParser parser = new XQParser(new CharArrayInPort(string), this.comp.getMessages(), this.interpreter);
            Vector exps = new Vector(20);
            while (true) {
                Expression sexp = parser.parse(this.comp);
                if (sexp == null) {
                    break;
                }
                exps.addElement(sexp);
            }
            int nexps = exps.size();
            if (nexps == 0) {
                return QuoteExp.voidExp;
            }
            if (nexps == 1) {
                return (Expression) exps.elementAt(0);
            }
            throw new InternalError("too many xpath expressions");
        } catch (Throwable ex) {
            ex.printStackTrace();
            throw new InternalError("caught " + ex);
        }
    }

    public void write(int v) {
        String str;
        if (this.inAttribute) {
            this.attributeValue.appendCodePoint(v);
            return;
        }
        if (v < 65536) {
            str = String.valueOf(v);
        } else {
            str = new String(new char[]{(char) (((v - 65536) >> 10) + 55296), (char) ((v & 1023) + 56320)});
        }
        push((Object) str);
    }

    public Consumer append(char v) {
        if (this.inAttribute) {
            this.attributeValue.append(v);
        } else {
            push((Object) String.valueOf(v));
        }
        return this;
    }

    public Consumer append(CharSequence csq) {
        if (this.inAttribute) {
            this.attributeValue.append(csq);
        } else {
            push((Object) csq.toString());
        }
        return this;
    }

    public Consumer append(CharSequence csq, int start, int end) {
        return append(csq.subSequence(start, end));
    }

    /* access modifiers changed from: package-private */
    public void push(Expression exp) {
        this.comp.exprStack.push(exp);
    }

    /* access modifiers changed from: package-private */
    public void push(Object value) {
        push((Expression) new QuoteExp(value));
    }

    public void writeBoolean(boolean v) {
        if (this.inAttribute) {
            this.attributeValue.append(v);
        } else {
            push((Expression) v ? QuoteExp.trueExp : QuoteExp.falseExp);
        }
    }

    public void writeFloat(float v) {
        if (this.inAttribute) {
            this.attributeValue.append(v);
        } else {
            push((Object) DFloNum.make((double) v));
        }
    }

    public void writeDouble(double v) {
        if (this.inAttribute) {
            this.attributeValue.append(v);
        } else {
            push((Object) DFloNum.make(v));
        }
    }

    public void writeInt(int v) {
        if (this.inAttribute) {
            this.attributeValue.append(v);
        } else {
            push((Object) IntNum.make(v));
        }
    }

    public void writeLong(long v) {
        if (this.inAttribute) {
            this.attributeValue.append(v);
        } else {
            push((Object) IntNum.make(v));
        }
    }

    public void startDocument() {
    }

    public void startDocument(ModuleExp mexp2) {
        this.mexp = mexp2;
        startDocument();
    }

    public void endDocument() {
    }

    public void writeObject(Object v) {
        if (this.inAttribute) {
            this.attributeValue.append(v);
        } else {
            push(v);
        }
    }

    public void write(char[] buf, int off, int len) {
        if (this.inAttribute) {
            this.attributeValue.append(buf, off, len);
        } else {
            push((Object) new String(buf, off, len));
        }
    }

    public void write(String str) {
        if (this.inAttribute) {
            this.attributeValue.append(str);
        } else {
            push((Object) str);
        }
    }

    public void write(CharSequence str, int start, int length) {
        write(str.subSequence(start, length).toString());
    }

    public boolean ignoring() {
        return false;
    }

    public Expression getExpression() {
        return this.comp.exprStack.pop();
    }

    public void error(char kind, String message) {
        getMessages().error(kind, message);
    }

    /* access modifiers changed from: package-private */
    public Expression resolveQNameExpression(String name) {
        if (name == null) {
            return QuoteExp.nullExp;
        }
        return new QuoteExp(Symbol.make((Object) null, name));
    }

    public void parse(Compilation comp2) throws IOException {
        this.comp = comp2;
        if (comp2.exprStack == null) {
            comp2.exprStack = new Stack<>();
        }
        ModuleExp mexp2 = comp2.pushNewModule((Lexer) this);
        comp2.mustCompileHere();
        startDocument(mexp2);
        XMLParser.parse((LineBufferedReader) this.in, getMessages(), (Consumer) this);
        endDocument();
        comp2.pop(mexp2);
    }
}
