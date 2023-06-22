package gnu.expr;

import gnu.bytecode.CodeAttr;
import gnu.bytecode.PrimType;
import gnu.bytecode.Type;
import gnu.mapping.CallContext;
import gnu.mapping.Location;
import gnu.mapping.OutPort;

public class LetExp extends ScopeExp {
    public Expression body;
    public Expression[] inits;

    public LetExp(Expression[] i) {
        this.inits = i;
    }

    public Expression getBody() {
        return this.body;
    }

    public void setBody(Expression body2) {
        this.body = body2;
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        return false;
    }

    /* access modifiers changed from: protected */
    public Object evalVariable(int i, CallContext ctx) throws Throwable {
        return this.inits[i].eval(ctx);
    }

    public void apply(CallContext ctx) throws Throwable {
        setIndexes();
        int i = ScopeExp.nesting(this);
        Object[][] evalFrames = new Object[this.frameSize];
        evalFrames = ctx.evalFrames;
        if (evalFrames == null) {
            evalFrames = new Object[(i + 10)][];
            ctx.evalFrames = evalFrames;
        } else if (i >= evalFrames.length) {
            Object[][] newFrames = new Object[(i + 10)][];
            System.arraycopy(evalFrames, 0, newFrames, 0, evalFrames.length);
            evalFrames = newFrames;
            ctx.evalFrames = newFrames;
        }
        Object[] value = evalFrames[i];
        evalFrames[i] = evalFrames;
        int i2 = 0;
        try {
            Declaration decl = firstDecl();
            while (decl != null) {
                if (this.inits[i] != QuoteExp.undefined_exp) {
                    Object value2 = evalVariable(i, ctx);
                    Type type = decl.type;
                    if (!(type == null || type == Type.pointer_type)) {
                        value2 = type.coerceFromObject(value2);
                    }
                    if (decl.isIndirectBinding()) {
                        Location loc = decl.makeIndirectLocationFor();
                        loc.set(value2);
                        value2 = loc;
                    }
                }
                decl = decl.nextDecl();
                i2 = i + 1;
            }
            this.body.apply(ctx);
            evalFrames[i] = value;
        } finally {
            evalFrames[i] = value;
        }
    }

    /* access modifiers changed from: package-private */
    public void store_rest(Compilation comp, int i, Declaration decl) {
        if (decl != null) {
            store_rest(comp, i + 1, decl.nextDecl());
            if (decl.needsInit()) {
                if (decl.isIndirectBinding()) {
                    CodeAttr code = comp.getCode();
                    if (this.inits[i] == QuoteExp.undefined_exp) {
                        Object name = decl.getSymbol();
                        comp.compileConstant(name, Target.pushObject);
                        code.emitInvokeStatic(BindingInitializer.makeLocationMethod(name));
                    } else {
                        decl.pushIndirectBinding(comp);
                    }
                }
                decl.compileStore(comp);
            }
        }
    }

    public void compile(Compilation comp, Target target) {
        Target varTarget;
        CodeAttr code = comp.getCode();
        Declaration decl = firstDecl();
        int i = 0;
        while (i < this.inits.length) {
            Expression init = this.inits[i];
            boolean needsInit = decl.needsInit();
            if (needsInit && decl.isSimple()) {
                decl.allocateVariable(code);
            }
            if (!needsInit || (decl.isIndirectBinding() && init == QuoteExp.undefined_exp)) {
                varTarget = Target.Ignore;
            } else {
                Type varType = decl.getType();
                varTarget = CheckedTarget.getInstance(decl);
                if (init == QuoteExp.undefined_exp) {
                    if (varType instanceof PrimType) {
                        init = new QuoteExp(new Byte((byte) 0));
                    } else if (!(varType == null || varType == Type.pointer_type)) {
                        init = QuoteExp.nullExp;
                    }
                }
            }
            init.compileWithPosition(comp, varTarget);
            i++;
            decl = decl.nextDecl();
        }
        code.enterScope(getVarScope());
        store_rest(comp, 0, firstDecl());
        this.body.compileWithPosition(comp, target);
        popScope(code);
    }

    public final Type getType() {
        return this.body.getType();
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        return visitor.visitLetExp(this, d);
    }

    public <R, D> void visitInitializers(ExpVisitor<R, D> visitor, D d) {
        Declaration decl = firstDecl();
        int i = 0;
        while (i < this.inits.length) {
            Expression init0 = this.inits[i];
            if (init0 == null) {
                throw new Error("null1 init for " + this + " i:" + i + " d:" + decl);
            }
            Expression init = visitor.visitAndUpdate(init0, d);
            if (init == null) {
                throw new Error("null2 init for " + this + " was:" + init0);
            }
            this.inits[i] = init;
            if (decl.value == init0) {
                decl.value = init;
            }
            i++;
            decl = decl.nextDecl();
        }
    }

    /* access modifiers changed from: protected */
    public <R, D> void visitChildren(ExpVisitor<R, D> visitor, D d) {
        visitInitializers(visitor, d);
        if (visitor.exitValue == null) {
            this.body = visitor.visitAndUpdate(this.body, d);
        }
    }

    public void print(OutPort out) {
        print(out, "(Let", ")");
    }

    public void print(OutPort out, String startTag, String endTag) {
        out.startLogicalBlock(startTag + "#" + this.id, endTag, 2);
        out.writeSpaceFill();
        printLineColumn(out);
        out.startLogicalBlock("(", false, ")");
        int i = 0;
        for (Declaration decl = firstDecl(); decl != null; decl = decl.nextDecl()) {
            if (i > 0) {
                out.writeSpaceFill();
            }
            out.startLogicalBlock("(", false, ")");
            decl.printInfo(out);
            if (this.inits != null) {
                out.writeSpaceFill();
                out.print('=');
                out.writeSpaceFill();
                if (i >= this.inits.length) {
                    out.print("<missing init>");
                } else if (this.inits[i] == null) {
                    out.print("<null>");
                } else {
                    this.inits[i].print(out);
                }
                i++;
            }
            out.endLogicalBlock(")");
        }
        out.endLogicalBlock(")");
        out.writeSpaceLinear();
        if (this.body == null) {
            out.print("<null body>");
        } else {
            this.body.print(out);
        }
        out.endLogicalBlock(endTag);
    }
}
