package kawa.standard;

import gnu.bytecode.ClassType;
import gnu.bytecode.Type;
import gnu.expr.Expression;
import gnu.expr.PrimProcedure;
import gnu.expr.QuoteExp;
import gnu.kawa.lispexpr.LispLanguage;
import gnu.lists.LList;
import gnu.lists.Pair;
import kawa.lang.ListPat;
import kawa.lang.Pattern;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class prim_method extends Syntax {
    public static final prim_method interface_method = new prim_method(185);
    public static final prim_method op1 = new prim_method();
    private static Pattern pattern3 = new ListPat(3);
    private static Pattern pattern4 = new ListPat(4);
    public static final prim_method static_method = new prim_method(184);
    public static final prim_method virtual_method = new prim_method(182);
    int op_code;

    static {
        virtual_method.setName("primitive-virtual-method");
        static_method.setName("primitive-static-method");
        interface_method.setName("primitive-interface-method");
        op1.setName("primitive-op1");
    }

    /* access modifiers changed from: package-private */
    public int opcode() {
        return this.op_code;
    }

    public prim_method(int opcode) {
        this.op_code = opcode;
    }

    public prim_method() {
    }

    public Expression rewrite(Object obj, Translator tr) {
        char code;
        PrimProcedure proc;
        Object[] match = new Object[4];
        if (this.op_code != 0 ? !pattern4.match(obj, match, 0) : !pattern3.match(obj, match, 1)) {
            return tr.syntaxError("wrong number of arguments to " + getName() + "(opcode:" + this.op_code + ")");
        } else if (!(match[3] instanceof LList)) {
            return tr.syntaxError("missing/invalid parameter list in " + getName());
        } else {
            LList argp = (LList) match[3];
            int narg = argp.size();
            Type[] args = new Type[narg];
            for (int i = 0; i < narg; i++) {
                Pair p = (Pair) argp;
                args[i] = tr.exp2Type(p);
                argp = (LList) p.getCdr();
            }
            Type rtype = tr.exp2Type(new Pair(match[2], (Object) null));
            if (this.op_code == 0) {
                proc = new PrimProcedure(((Number) match[1]).intValue(), rtype, args);
            } else {
                ClassType cl = null;
                Type ctype = tr.exp2Type((Pair) obj);
                if (ctype != null) {
                    ctype = ctype.getImplementationType();
                }
                try {
                    cl = (ClassType) ctype;
                    cl.getReflectClass();
                } catch (Exception e) {
                    if (cl == null) {
                        code = 'e';
                    } else {
                        code = 'w';
                        cl.setExisting(false);
                    }
                    tr.error(code, "unknown class: " + match[0]);
                }
                if (match[1] instanceof Pair) {
                    Pair p2 = (Pair) match[1];
                    if (p2.getCar() == LispLanguage.quote_sym) {
                        match[1] = ((Pair) p2.getCdr()).getCar();
                    }
                }
                proc = new PrimProcedure(this.op_code, cl, match[1].toString(), rtype, args);
            }
            return new QuoteExp(proc);
        }
    }
}
