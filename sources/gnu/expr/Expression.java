package gnu.expr;

import gnu.bytecode.Type;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.reflect.OccurrenceType;
import gnu.kawa.util.IdentityHashTable;
import gnu.lists.Consumer;
import gnu.mapping.CallContext;
import gnu.mapping.CharArrayOutPort;
import gnu.mapping.Environment;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure0;
import gnu.text.Printable;
import gnu.text.SourceLocator;
import java.io.PrintWriter;
import java.io.Writer;

public abstract class Expression extends Procedure0 implements Printable, SourceLocator {
    protected static final int NEXT_AVAIL_FLAG = 2;
    public static final int VALIDATED = 1;
    public static final Expression[] noExpressions = new Expression[0];
    String filename;
    protected int flags;
    int position;

    public abstract void compile(Compilation compilation, Target target);

    /* access modifiers changed from: protected */
    public abstract boolean mustCompile();

    public abstract void print(OutPort outPort);

    public final Object eval(CallContext ctx) throws Throwable {
        int start = ctx.startFromContext();
        try {
            match0(ctx);
            return ctx.getFromContext(start);
        } catch (Throwable ex) {
            ctx.cleanupFromContext(start);
            throw ex;
        }
    }

    public final Object eval(Environment env) throws Throwable {
        CallContext ctx = CallContext.getInstance();
        Environment saveEnv = Environment.setSaveCurrent(env);
        try {
            return eval(ctx);
        } finally {
            Environment.restoreCurrent(saveEnv);
        }
    }

    public final int match0(CallContext ctx) {
        ctx.proc = this;
        ctx.pc = 0;
        return 0;
    }

    public final Object apply0() throws Throwable {
        CallContext ctx = CallContext.getInstance();
        check0(ctx);
        return ctx.runUntilValue();
    }

    public void apply(CallContext ctx) throws Throwable {
        throw new RuntimeException("internal error - " + getClass() + ".eval called");
    }

    public final void print(Consumer out) {
        if (out instanceof OutPort) {
            print((OutPort) out);
        } else if (out instanceof PrintWriter) {
            OutPort port = new OutPort((Writer) (PrintWriter) out);
            print(port);
            port.close();
        } else {
            CharArrayOutPort port2 = new CharArrayOutPort();
            print((OutPort) port2);
            port2.close();
            port2.writeTo(out);
        }
    }

    public void printLineColumn(OutPort out) {
        int line = getLineNumber();
        if (line > 0) {
            out.print("line:");
            out.print(line);
            int column = getColumnNumber();
            if (column > 0) {
                out.print(':');
                out.print(column);
            }
            out.writeSpaceFill();
        }
    }

    public final void compileWithPosition(Compilation comp, Target target) {
        int line = getLineNumber();
        if (line > 0) {
            comp.getCode().putLineNumber(getFileName(), line);
            compileNotePosition(comp, target, this);
            return;
        }
        compile(comp, target);
    }

    public final void compileWithPosition(Compilation comp, Target target, Expression position2) {
        int line = position2.getLineNumber();
        if (line > 0) {
            comp.getCode().putLineNumber(position2.getFileName(), line);
            compileNotePosition(comp, target, position2);
            return;
        }
        compile(comp, target);
    }

    public final void compileNotePosition(Compilation comp, Target target, Expression position2) {
        String saveFilename = comp.getFileName();
        int saveLine = comp.getLineNumber();
        int saveColumn = comp.getColumnNumber();
        comp.setLine(position2);
        compile(comp, target);
        comp.setLine(saveFilename, saveLine, saveColumn);
    }

    public final void compile(Compilation comp, Type type) {
        compile(comp, StackTarget.getInstance(type));
    }

    public final void compile(Compilation comp, Declaration lhs) {
        compile(comp, CheckedTarget.getInstance(lhs));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = (gnu.expr.BeginExp) r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void compileButFirst(gnu.expr.Expression r6, gnu.expr.Compilation r7) {
        /*
            boolean r4 = r6 instanceof gnu.expr.BeginExp
            if (r4 == 0) goto L_0x000b
            r0 = r6
            gnu.expr.BeginExp r0 = (gnu.expr.BeginExp) r0
            int r3 = r0.length
            if (r3 != 0) goto L_0x000c
        L_0x000b:
            return
        L_0x000c:
            gnu.expr.Expression[] r1 = r0.exps
            r4 = 0
            r4 = r1[r4]
            compileButFirst(r4, r7)
            r2 = 1
        L_0x0015:
            if (r2 >= r3) goto L_0x000b
            r4 = r1[r2]
            gnu.expr.Target r5 = gnu.expr.Target.Ignore
            r4.compileWithPosition(r7, r5)
            int r2 = r2 + 1
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.Expression.compileButFirst(gnu.expr.Expression, gnu.expr.Compilation):void");
    }

    public static Expression deepCopy(Expression exp, IdentityHashTable mapper) {
        if (exp == null) {
            return null;
        }
        Object tr = mapper.get(exp);
        if (tr != null) {
            return (Expression) tr;
        }
        Expression copy = exp.deepCopy(mapper);
        mapper.put(exp, copy);
        return copy;
    }

    public static Expression[] deepCopy(Expression[] exps, IdentityHashTable mapper) {
        if (exps == null) {
            return null;
        }
        int nargs = exps.length;
        Expression[] a = new Expression[nargs];
        for (int i = 0; i < nargs; i++) {
            Expression ei = exps[i];
            Expression ai = deepCopy(ei, mapper);
            if (ai == null && ei != null) {
                return null;
            }
            a[i] = ai;
        }
        return a;
    }

    protected static Expression deepCopy(Expression exp) {
        return deepCopy(exp, new IdentityHashTable());
    }

    /* access modifiers changed from: protected */
    public Expression deepCopy(IdentityHashTable mapper) {
        return null;
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        return visitor.visitExpression(this, d);
    }

    /* access modifiers changed from: protected */
    public <R, D> void visitChildren(ExpVisitor<R, D> expVisitor, D d) {
    }

    public Expression validateApply(ApplyExp exp, InlineCalls visitor, Type required, Declaration decl) {
        exp.args = visitor.visitExps(exp.args, null);
        return exp;
    }

    public static Expression makeWhile(Object cond, Object body, Compilation parser) {
        LetExp let = new LetExp(inits);
        Declaration fdecl = let.addDeclaration((Object) "%do%loop");
        Expression recurse = new ApplyExp((Expression) new ReferenceExp(fdecl), noExpressions);
        LambdaExp lexp = new LambdaExp();
        parser.push((ScopeExp) lexp);
        lexp.body = new IfExp(parser.parse(cond), new BeginExp(parser.parse(body), recurse), QuoteExp.voidExp);
        lexp.setName("%do%loop");
        parser.pop(lexp);
        Expression[] inits = {lexp};
        fdecl.noteValue(lexp);
        let.setBody(new ApplyExp((Expression) new ReferenceExp(fdecl), noExpressions));
        return let;
    }

    public final void setLocation(SourceLocator location) {
        this.filename = location.getFileName();
        setLine(location.getLineNumber(), location.getColumnNumber());
    }

    public final Expression setLine(Expression old) {
        setLocation(old);
        return this;
    }

    public final void setFile(String filename2) {
        this.filename = filename2;
    }

    public final void setLine(int lineno, int colno) {
        if (lineno < 0) {
            lineno = 0;
        }
        if (colno < 0) {
            colno = 0;
        }
        this.position = (lineno << 12) + colno;
    }

    public final void setLine(int lineno) {
        setLine(lineno, 0);
    }

    public final String getFileName() {
        return this.filename;
    }

    public void setLine(Compilation comp) {
        int line = comp.getLineNumber();
        if (line > 0) {
            setFile(comp.getFileName());
            setLine(line, comp.getColumnNumber());
        }
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return this.filename;
    }

    public final int getLineNumber() {
        int line = this.position >> 12;
        if (line == 0) {
            return -1;
        }
        return line;
    }

    public final int getColumnNumber() {
        int column = this.position & 4095;
        if (column == 0) {
            return -1;
        }
        return column;
    }

    public boolean isStableSourceLocation() {
        return true;
    }

    public Type getType() {
        return Type.pointer_type;
    }

    public boolean isSingleValue() {
        return OccurrenceType.itemCountIsOne(getType());
    }

    public Object valueIfConstant() {
        return null;
    }

    public void setFlag(boolean setting, int flag) {
        if (setting) {
            this.flags |= flag;
        } else {
            this.flags &= flag ^ -1;
        }
    }

    public void setFlag(int flag) {
        this.flags |= flag;
    }

    public int getFlags() {
        return this.flags;
    }

    public boolean getFlag(int flag) {
        return (this.flags & flag) != 0;
    }

    public boolean side_effects() {
        return true;
    }

    public String toString() {
        String tname = getClass().getName();
        if (tname.startsWith("gnu.expr.")) {
            tname = tname.substring(9);
        }
        return tname + GetNamedPart.CAST_METHOD_NAME + Integer.toHexString(hashCode());
    }
}
