package gnu.kawa.functions;

import gnu.expr.Declaration;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Procedure;
import gnu.mapping.ProcedureN;
import gnu.mapping.Values;

public class Map extends ProcedureN {
    final Declaration applyFieldDecl;
    final ApplyToArgs applyToArgs;
    boolean collect;
    final IsEq isEq;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Map(boolean collect2, ApplyToArgs applyToArgs2, Declaration applyFieldDecl2, IsEq isEq2) {
        super(collect2 ? "map" : "for-each");
        this.collect = collect2;
        this.applyToArgs = applyToArgs2;
        this.applyFieldDecl = applyFieldDecl2;
        this.isEq = isEq2;
        setProperty(Procedure.validateApplyKey, "gnu.kawa.functions.CompileMisc:validateApplyMap");
    }

    public static Object map1(Procedure proc, Object list) throws Throwable {
        LList result = LList.Empty;
        Pair last = null;
        while (list != LList.Empty) {
            Pair pair = (Pair) list;
            Pair new_pair = new Pair(proc.apply1(pair.getCar()), LList.Empty);
            if (last == null) {
                result = new_pair;
            } else {
                last.setCdr(new_pair);
            }
            last = new_pair;
            list = pair.getCdr();
        }
        return result;
    }

    public static void forEach1(Procedure proc, Object list) throws Throwable {
        while (list != LList.Empty) {
            Pair pair = (Pair) list;
            proc.apply1(pair.getCar());
            list = pair.getCdr();
        }
    }

    public Object apply2(Object arg1, Object arg2) throws Throwable {
        if (arg1 instanceof Procedure) {
            Procedure proc = (Procedure) arg1;
            if (this.collect) {
                return map1(proc, arg2);
            }
            forEach1(proc, arg2);
            return Values.empty;
        }
        return applyN(new Object[]{arg1, arg2});
    }

    public Object applyN(Object[] args) throws Throwable {
        Pair pair;
        Procedure proc;
        int need_apply;
        Object[] each_args;
        int arity = args.length - 1;
        if (arity != 1 || !(args[0] instanceof Procedure)) {
            Pair last = null;
            if (this.collect) {
                pair = LList.Empty;
            } else {
                pair = Values.empty;
            }
            Object[] rest = new Object[arity];
            System.arraycopy(args, 1, rest, 0, arity);
            if (args[0] instanceof Procedure) {
                need_apply = 0;
                each_args = new Object[arity];
                proc = args[0];
            } else {
                need_apply = 1;
                each_args = new Object[(arity + 1)];
                each_args[0] = args[0];
                proc = this.applyToArgs;
            }
            while (true) {
                for (int i = 0; i < arity; i++) {
                    Object list = rest[i];
                    if (list == LList.Empty) {
                        return pair;
                    }
                    Pair pair2 = (Pair) list;
                    each_args[need_apply + i] = pair2.getCar();
                    rest[i] = pair2.getCdr();
                }
                Object value = proc.applyN(each_args);
                if (this.collect) {
                    Pair new_pair = new Pair(value, LList.Empty);
                    if (last == null) {
                        pair = new_pair;
                    } else {
                        last.setCdr(new_pair);
                    }
                    last = new_pair;
                }
            }
        } else {
            Procedure proc2 = args[0];
            if (this.collect) {
                return map1(proc2, args[1]);
            }
            forEach1(proc2, args[1]);
            return Values.empty;
        }
    }
}
