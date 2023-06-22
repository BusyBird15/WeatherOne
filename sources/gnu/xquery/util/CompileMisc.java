package gnu.xquery.util;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Method;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.ConsumerTarget;
import gnu.expr.Declaration;
import gnu.expr.ErrorExp;
import gnu.expr.Expression;
import gnu.expr.IfExp;
import gnu.expr.InlineCalls;
import gnu.expr.LambdaExp;
import gnu.expr.LetExp;
import gnu.expr.PrimProcedure;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.Target;
import gnu.kawa.functions.AddOp;
import gnu.kawa.functions.ValuesMap;
import gnu.kawa.reflect.CompileReflect;
import gnu.kawa.xml.CoerceNodes;
import gnu.kawa.xml.SortNodes;
import gnu.kawa.xml.XDataType;
import gnu.mapping.Procedure;
import gnu.math.IntNum;
import gnu.xquery.lang.XQuery;

public class CompileMisc {
    static final Method castMethod = typeXDataType.getDeclaredMethod("cast", 1);
    static final Method castableMethod = typeXDataType.getDeclaredMethod("castable", 1);
    static final ClassType typeTuples = ClassType.make("gnu.xquery.util.OrderedTuples");
    static final ClassType typeXDataType = ClassType.make("gnu.kawa.xml.XDataType");

    public static Expression validateCompare(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc) {
        exp.visitArgs(visitor);
        Expression folded = exp.inlineIfConstant(proc, visitor);
        if (folded != exp) {
            return folded;
        }
        Compare cproc = (Compare) proc;
        if ((cproc.flags & 32) == 0) {
            exp = new ApplyExp(ClassType.make("gnu.xquery.util.Compare").getDeclaredMethod("apply", 4), new QuoteExp(IntNum.make(cproc.flags)), exp.getArg(0), exp.getArg(1), QuoteExp.nullExp);
        }
        if (exp.getTypeRaw() == null) {
            exp.setType(XDataType.booleanType);
        }
        return exp;
    }

    public static Expression validateBooleanValue(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc) {
        exp.visitArgs(visitor);
        Expression[] args = exp.getArgs();
        if (args.length == 1) {
            Expression arg = args[0];
            Type type = arg.getType();
            if (type == XDataType.booleanType) {
                return arg;
            }
            if (type == null) {
                exp.setType(XDataType.booleanType);
            }
            if (arg instanceof QuoteExp) {
                try {
                    return BooleanValue.booleanValue(((QuoteExp) arg).getValue()) ? XQuery.trueExp : XQuery.falseExp;
                } catch (Throwable th) {
                    visitor.getMessages().error('e', "cannot convert to a boolean");
                    return new ErrorExp("cannot convert to a boolean");
                }
            }
        }
        return exp;
    }

    public static Expression validateArithOp(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc) {
        exp.visitArgs(visitor);
        return exp;
    }

    public static Expression validateApplyValuesFilter(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc) {
        Type seqType;
        Method sizeMethod;
        ValuesFilter vproc = (ValuesFilter) proc;
        exp.visitArgs(visitor);
        Expression[] args = exp.getArgs();
        Expression exp2 = args[1];
        if (!(exp2 instanceof LambdaExp)) {
            return exp;
        }
        LambdaExp lexp2 = (LambdaExp) exp2;
        if (lexp2.min_args != 3 || lexp2.max_args != 3) {
            return exp;
        }
        exp.setType(args[0].getType());
        Compilation parser = visitor.getCompilation();
        Declaration dotArg = lexp2.firstDecl();
        Declaration posArg = dotArg.nextDecl();
        Declaration lastArg = posArg.nextDecl();
        lexp2.setInlineOnly(true);
        lexp2.returnContinuation = exp;
        lexp2.inlineHome = visitor.getCurrentLambda();
        lexp2.remove(posArg, lastArg);
        lexp2.min_args = 2;
        lexp2.max_args = 2;
        if (!lastArg.getCanRead() && vproc.kind != 'R') {
            return exp;
        }
        parser.letStart();
        Expression seq = args[0];
        if (vproc.kind == 'P') {
            seqType = seq.getType();
            sizeMethod = Compilation.typeValues.getDeclaredMethod("countValues", 1);
        } else {
            seqType = SortNodes.typeSortedNodes;
            ApplyExp applyExp = new ApplyExp((Procedure) SortNodes.sortNodes, seq);
            sizeMethod = CoerceNodes.typeNodes.getDeclaredMethod("size", 0);
            seq = applyExp;
        }
        Declaration sequence = parser.letVariable("sequence", seqType, seq);
        parser.letEnter();
        Expression pred = lexp2.body;
        if (lexp2.body.getType() != XDataType.booleanType) {
            pred = new ApplyExp(ValuesFilter.matchesMethod, pred, new ReferenceExp(posArg));
        }
        if (vproc.kind == 'R') {
            Declaration declaration = new Declaration((Object) null, (Type) Type.intType);
            Expression init = new ApplyExp((Procedure) AddOp.$Mn, new ReferenceExp(lastArg), new ReferenceExp(declaration));
            LetExp let = new LetExp(new Expression[]{new ApplyExp((Procedure) AddOp.$Pl, init, new QuoteExp(IntNum.one()))});
            lexp2.replaceFollowing(dotArg, declaration);
            let.add(posArg);
            let.body = pred;
            pred = let;
        }
        lexp2.body = new IfExp(pred, new ReferenceExp(dotArg), QuoteExp.voidExp);
        ApplyExp doMap = new ApplyExp((Procedure) ValuesMap.valuesMapWithPos, lexp2, new ReferenceExp(sequence));
        doMap.setType(dotArg.getType());
        lexp2.returnContinuation = doMap;
        LetExp let2 = new LetExp(new Expression[]{new ApplyExp(sizeMethod, new ReferenceExp(sequence))});
        let2.add(lastArg);
        let2.body = gnu.kawa.functions.CompileMisc.validateApplyValuesMap(doMap, visitor, required, ValuesMap.valuesMapWithPos);
        return parser.letDone(let2);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: gnu.xquery.util.RelativeStep} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v10, resolved type: gnu.kawa.xml.DescendantOrSelfAxis} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v22, resolved type: gnu.bytecode.ClassType} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static gnu.expr.Expression validateApplyRelativeStep(gnu.expr.ApplyExp r40, gnu.expr.InlineCalls r41, gnu.bytecode.Type r42, gnu.mapping.Procedure r43) {
        /*
            r40.visitArgs(r41)
            gnu.expr.Expression[] r5 = r40.getArgs()
            r35 = 0
            r9 = r5[r35]
            r35 = 1
            r11 = r5[r35]
            gnu.expr.Compilation r6 = r41.getCompilation()
            boolean r0 = r11 instanceof gnu.expr.LambdaExp
            r35 = r0
            if (r35 == 0) goto L_0x003f
            boolean r0 = r6.mustCompile
            r35 = r0
            if (r35 == 0) goto L_0x003f
            r17 = r11
            gnu.expr.LambdaExp r17 = (gnu.expr.LambdaExp) r17
            r0 = r17
            int r0 = r0.min_args
            r35 = r0
            r36 = 3
            r0 = r35
            r1 = r36
            if (r0 != r1) goto L_0x003f
            r0 = r17
            int r0 = r0.max_args
            r35 = r0
            r36 = 3
            r0 = r35
            r1 = r36
            if (r0 == r1) goto L_0x0042
        L_0x003f:
            r26 = r40
        L_0x0041:
            return r26
        L_0x0042:
            r35 = 1
            r0 = r17
            r1 = r35
            r0.setInlineOnly(r1)
            r0 = r40
            r1 = r17
            r1.returnContinuation = r0
            gnu.expr.LambdaExp r35 = r41.getCurrentLambda()
            r0 = r35
            r1 = r17
            r1.inlineHome = r0
            r0 = r17
            gnu.expr.Expression r11 = r0.body
            gnu.expr.Declaration r8 = r17.firstDecl()
            gnu.expr.Declaration r24 = r8.nextDecl()
            gnu.expr.Declaration r13 = r24.nextDecl()
            gnu.expr.Declaration r35 = r13.nextDecl()
            r0 = r24
            r1 = r35
            r0.setNext(r1)
            r35 = 0
            r0 = r35
            r13.setNext(r0)
            r35 = 2
            r0 = r35
            r1 = r17
            r1.min_args = r0
            r35 = 2
            r0 = r35
            r1 = r17
            r1.max_args = r0
            gnu.bytecode.Type r31 = r9.getType()
            if (r31 == 0) goto L_0x00e6
            gnu.kawa.xml.NodeType r35 = gnu.kawa.xml.NodeType.anyNodeTest
            r0 = r35
            r1 = r31
            int r35 = r0.compare(r1)
            r36 = -3
            r0 = r35
            r1 = r36
            if (r0 != r1) goto L_0x00e6
            gnu.expr.Compilation r35 = r41.getCompilation()
            gnu.expr.Language r12 = r35.getLanguage()
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "step input is "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r31
            java.lang.String r36 = r12.formatType(r0)
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = " - not a node sequence"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r19 = r35.toString()
            gnu.text.SourceMessages r35 = r41.getMessages()
            r36 = 101(0x65, float:1.42E-43)
            r0 = r35
            r1 = r36
            r2 = r19
            r0.error(r1, r2)
            gnu.expr.ErrorExp r26 = new gnu.expr.ErrorExp
            r0 = r26
            r1 = r19
            r0.<init>(r1)
            goto L_0x0041
        L_0x00e6:
            gnu.bytecode.Type r27 = r40.getTypeRaw()
            if (r27 == 0) goto L_0x00f4
            gnu.bytecode.ClassType r35 = gnu.bytecode.Type.pointer_type
            r0 = r27
            r1 = r35
            if (r0 != r1) goto L_0x0113
        L_0x00f4:
            gnu.bytecode.Type r32 = r11.getType()
            gnu.bytecode.Type r28 = gnu.kawa.reflect.OccurrenceType.itemPrimeType(r32)
            gnu.kawa.xml.NodeType r35 = gnu.kawa.xml.NodeType.anyNodeTest
            r0 = r35
            r1 = r28
            int r20 = r0.compare(r1)
            if (r20 < 0) goto L_0x01ae
            gnu.bytecode.Type r27 = gnu.kawa.xml.NodeSetType.getInstance(r28)
        L_0x010c:
            r0 = r40
            r1 = r27
            r0.setType(r1)
        L_0x0113:
            boolean r35 = r13.getCanRead()
            if (r35 == 0) goto L_0x01be
            gnu.bytecode.ClassType r33 = gnu.kawa.xml.CoerceNodes.typeNodes
            r6.letStart()
            r35 = 0
            gnu.expr.ApplyExp r36 = new gnu.expr.ApplyExp
            gnu.kawa.xml.CoerceNodes r37 = gnu.kawa.xml.CoerceNodes.coerceNodes
            r38 = 1
            r0 = r38
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r0]
            r38 = r0
            r39 = 0
            r38[r39] = r9
            r36.<init>((gnu.mapping.Procedure) r37, (gnu.expr.Expression[]) r38)
            r0 = r35
            r1 = r33
            r2 = r36
            gnu.expr.Declaration r29 = r6.letVariable(r0, r1, r2)
            r6.letEnter()
            java.lang.String r35 = "size"
            r36 = 0
            r0 = r33
            r1 = r35
            r2 = r36
            gnu.bytecode.Method r30 = r0.getDeclaredMethod((java.lang.String) r1, (int) r2)
            gnu.expr.ApplyExp r14 = new gnu.expr.ApplyExp
            r35 = 1
            r0 = r35
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r0]
            r35 = r0
            r36 = 0
            gnu.expr.ReferenceExp r37 = new gnu.expr.ReferenceExp
            r0 = r37
            r1 = r29
            r0.<init>((gnu.expr.Declaration) r1)
            r35[r36] = r37
            r0 = r30
            r1 = r35
            r14.<init>((gnu.bytecode.Method) r0, (gnu.expr.Expression[]) r1)
            gnu.expr.LetExp r15 = new gnu.expr.LetExp
            r35 = 1
            r0 = r35
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r0]
            r35 = r0
            r36 = 0
            r35[r36] = r14
            r0 = r35
            r15.<init>(r0)
            r15.addDeclaration((gnu.expr.Declaration) r13)
            gnu.expr.ApplyExp r35 = new gnu.expr.ApplyExp
            gnu.expr.Expression r36 = r40.getFunction()
            r37 = 2
            r0 = r37
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r0]
            r37 = r0
            r38 = 0
            gnu.expr.ReferenceExp r39 = new gnu.expr.ReferenceExp
            r0 = r39
            r1 = r29
            r0.<init>((gnu.expr.Declaration) r1)
            r37[r38] = r39
            r38 = 1
            r37[r38] = r17
            r35.<init>((gnu.expr.Expression) r36, (gnu.expr.Expression[]) r37)
            r0 = r35
            r15.body = r0
            gnu.expr.LetExp r26 = r6.letDone(r15)
            goto L_0x0041
        L_0x01ae:
            r35 = 0
            r36 = -1
            r0 = r28
            r1 = r35
            r2 = r36
            gnu.bytecode.Type r27 = gnu.kawa.reflect.OccurrenceType.getInstance(r0, r1, r2)
            goto L_0x010c
        L_0x01be:
            r26 = r40
            boolean r0 = r11 instanceof gnu.expr.ApplyExp
            r35 = r0
            if (r35 == 0) goto L_0x0238
            r4 = r11
            gnu.expr.ApplyExp r4 = (gnu.expr.ApplyExp) r4
            gnu.expr.Expression r35 = r4.getFunction()
            java.lang.Object r25 = r35.valueIfConstant()
            r0 = r25
            boolean r0 = r0 instanceof gnu.xquery.util.ValuesFilter
            r35 = r0
            if (r35 == 0) goto L_0x0238
            gnu.expr.Expression[] r35 = r4.getArgs()
            r36 = 1
            r34 = r35[r36]
            r0 = r34
            boolean r0 = r0 instanceof gnu.expr.LambdaExp
            r35 = r0
            if (r35 == 0) goto L_0x0238
            r18 = r34
            gnu.expr.LambdaExp r18 = (gnu.expr.LambdaExp) r18
            gnu.expr.Declaration r7 = r18.firstDecl()
            if (r7 == 0) goto L_0x0238
            gnu.expr.Declaration r23 = r7.nextDecl()
            if (r23 == 0) goto L_0x0238
            gnu.expr.Declaration r35 = r23.nextDecl()
            if (r35 != 0) goto L_0x0238
            boolean r35 = r23.getCanRead()
            if (r35 != 0) goto L_0x0238
            java.lang.String r35 = "java.lang.Number"
            gnu.bytecode.ClassType r35 = gnu.bytecode.ClassType.make(r35)
            r0 = r18
            gnu.expr.Expression r0 = r0.body
            r36 = r0
            gnu.bytecode.Type r36 = r36.getType()
            int r35 = r35.compare(r36)
            r36 = -3
            r0 = r35
            r1 = r36
            if (r0 != r1) goto L_0x0238
            r35 = 0
            r0 = r35
            gnu.expr.Expression r11 = r4.getArg(r0)
            r0 = r17
            r0.body = r11
            r35 = 0
            r0 = r35
            r1 = r40
            r4.setArg(r0, r1)
            r26 = r4
        L_0x0238:
            boolean r0 = r9 instanceof gnu.expr.ApplyExp
            r35 = r0
            if (r35 == 0) goto L_0x0041
            boolean r0 = r11 instanceof gnu.expr.ApplyExp
            r35 = r0
            if (r35 == 0) goto L_0x0041
            r3 = r9
            gnu.expr.ApplyExp r3 = (gnu.expr.ApplyExp) r3
            r4 = r11
            gnu.expr.ApplyExp r4 = (gnu.expr.ApplyExp) r4
            gnu.expr.Expression r35 = r3.getFunction()
            java.lang.Object r21 = r35.valueIfConstant()
            gnu.expr.Expression r35 = r4.getFunction()
            java.lang.Object r22 = r35.valueIfConstant()
            gnu.xquery.util.RelativeStep r35 = gnu.xquery.util.RelativeStep.relativeStep
            r0 = r21
            r1 = r35
            if (r0 != r1) goto L_0x0041
            r0 = r22
            boolean r0 = r0 instanceof gnu.kawa.xml.ChildAxis
            r35 = r0
            if (r35 == 0) goto L_0x0041
            int r35 = r3.getArgCount()
            r36 = 2
            r0 = r35
            r1 = r36
            if (r0 != r1) goto L_0x0041
            r35 = 1
            r0 = r35
            gnu.expr.Expression r10 = r3.getArg(r0)
            boolean r0 = r10 instanceof gnu.expr.LambdaExp
            r35 = r0
            if (r35 == 0) goto L_0x0041
            r16 = r10
            gnu.expr.LambdaExp r16 = (gnu.expr.LambdaExp) r16
            r0 = r16
            gnu.expr.Expression r0 = r0.body
            r35 = r0
            r0 = r35
            boolean r0 = r0 instanceof gnu.expr.ApplyExp
            r35 = r0
            if (r35 == 0) goto L_0x0041
            r0 = r16
            gnu.expr.Expression r0 = r0.body
            r35 = r0
            gnu.expr.ApplyExp r35 = (gnu.expr.ApplyExp) r35
            gnu.expr.Expression r35 = r35.getFunction()
            java.lang.Object r35 = r35.valueIfConstant()
            gnu.kawa.xml.DescendantOrSelfAxis r36 = gnu.kawa.xml.DescendantOrSelfAxis.anyNode
            r0 = r35
            r1 = r36
            if (r0 != r1) goto L_0x0041
            r35 = 0
            r36 = 0
            r0 = r36
            gnu.expr.Expression r36 = r3.getArg(r0)
            r0 = r40
            r1 = r35
            r2 = r36
            r0.setArg(r1, r2)
            gnu.expr.QuoteExp r35 = new gnu.expr.QuoteExp
            gnu.kawa.xml.ChildAxis r22 = (gnu.kawa.xml.ChildAxis) r22
            gnu.lists.NodePredicate r36 = r22.getNodePredicate()
            gnu.kawa.xml.DescendantAxis r36 = gnu.kawa.xml.DescendantAxis.make(r36)
            r35.<init>(r36)
            r0 = r35
            r4.setFunction(r0)
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.util.CompileMisc.validateApplyRelativeStep(gnu.expr.ApplyExp, gnu.expr.InlineCalls, gnu.bytecode.Type, gnu.mapping.Procedure):gnu.expr.Expression");
    }

    public static Expression validateApplyOrderedMap(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc) {
        exp.visitArgs(visitor);
        Expression[] args = exp.getArgs();
        if (args.length <= 2) {
            return exp;
        }
        Expression[] rargs = new Expression[(args.length - 1)];
        System.arraycopy(args, 1, rargs, 0, rargs.length);
        return new ApplyExp(proc, args[0], new ApplyExp(typeTuples.getDeclaredMethod("make$V", 2), rargs));
    }

    public static void compileOrderedMap(ApplyExp exp, Compilation comp, Target target, Procedure proc) {
        Expression[] args = exp.getArgs();
        if (args.length != 2) {
            ApplyExp.compile(exp, comp, target);
            return;
        }
        CodeAttr code = comp.getCode();
        Variable consumer = code.pushScope().addVariable(code, typeTuples, (String) null);
        args[1].compile(comp, Target.pushValue(typeTuples));
        code.emitStore(consumer);
        args[0].compile(comp, (Target) new ConsumerTarget(consumer));
        Method mm = typeTuples.getDeclaredMethod("run$X", 1);
        code.emitLoad(consumer);
        PrimProcedure.compileInvoke(comp, mm, target, exp.isTailCall(), 182, Type.pointer_type);
        code.popScope();
    }

    public static Expression validateApplyCastAs(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc) {
        exp.visitArgs(visitor);
        ApplyExp exp2 = CompileReflect.inlineClassName(exp, 0, visitor);
        Expression[] args = exp2.getArgs();
        if (args.length != 2 || !(args[0] instanceof QuoteExp) || !(((QuoteExp) args[0]).getValue() instanceof XDataType)) {
            return exp2;
        }
        return new ApplyExp(castMethod, args);
    }

    public static Expression validateApplyCastableAs(ApplyExp exp, InlineCalls visitor, Type required, Procedure proc) {
        exp.visitArgs(visitor);
        ApplyExp exp2 = CompileReflect.inlineClassName(exp, 1, visitor);
        Expression[] args = exp2.getArgs();
        if (args.length != 2 || !(args[1] instanceof QuoteExp) || !(((QuoteExp) args[1]).getValue() instanceof XDataType)) {
            return exp2;
        }
        return new ApplyExp(castableMethod, args[1], args[0]);
    }
}
