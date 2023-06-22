package gnu.kawa.reflect;

import gnu.bytecode.ClassType;
import gnu.bytecode.ObjectType;
import gnu.bytecode.Type;
import gnu.expr.ClassExp;
import gnu.expr.Expression;
import gnu.expr.InlineCalls;
import gnu.expr.Keyword;
import gnu.expr.PrimProcedure;
import gnu.mapping.MethodProc;

public class CompileInvoke {
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r66v1, resolved type: gnu.bytecode.ObjectType} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v0, resolved type: gnu.bytecode.ObjectType} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v1, resolved type: gnu.bytecode.ObjectType} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v2, resolved type: gnu.bytecode.ObjectType} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v9, resolved type: gnu.bytecode.ObjectType} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r29v1, resolved type: gnu.expr.ApplyExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r29v2, resolved type: gnu.expr.ApplyExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r29v3, resolved type: gnu.expr.ApplyExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v18, resolved type: gnu.expr.ApplyExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r29v4, resolved type: gnu.expr.LetExp} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v12, resolved type: gnu.bytecode.ObjectType} */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x029f, code lost:
        if ((gnu.kawa.reflect.ClassMethods.selectApplicable(r4, new gnu.bytecode.Type[]{gnu.expr.Compilation.typeClassType}) >> 32) == 1) goto L_0x02a1;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static gnu.expr.Expression validateApplyInvoke(gnu.expr.ApplyExp r70, gnu.expr.InlineCalls r71, gnu.bytecode.Type r72, gnu.mapping.Procedure r73) {
        /*
            r36 = r73
            gnu.kawa.reflect.Invoke r36 = (gnu.kawa.reflect.Invoke) r36
            r0 = r36
            char r0 = r0.kind
            r40 = r0
            gnu.expr.Compilation r25 = r71.getCompilation()
            gnu.expr.Expression[] r6 = r70.getArgs()
            int r0 = r6.length
            r49 = r0
            r0 = r25
            boolean r10 = r0.mustCompile
            if (r10 == 0) goto L_0x002e
            if (r49 == 0) goto L_0x002e
            r10 = 86
            r0 = r40
            if (r0 == r10) goto L_0x0029
            r10 = 42
            r0 = r40
            if (r0 != r10) goto L_0x0032
        L_0x0029:
            r10 = 1
            r0 = r49
            if (r0 != r10) goto L_0x0032
        L_0x002e:
            r70.visitArgs(r71)
        L_0x0031:
            return r70
        L_0x0032:
            r10 = 0
            r10 = r6[r10]
            r11 = 0
            r0 = r71
            gnu.expr.Expression r20 = r0.visit((gnu.expr.Expression) r10, (gnu.bytecode.Type) r11)
            r10 = 0
            r6[r10] = r20
            r10 = 86
            r0 = r40
            if (r0 == r10) goto L_0x004b
            r10 = 42
            r0 = r40
            if (r0 != r10) goto L_0x0107
        L_0x004b:
            gnu.bytecode.Type r67 = r20.getType()
        L_0x004f:
            r0 = r67
            boolean r10 = r0 instanceof gnu.expr.PairClassType
            if (r10 == 0) goto L_0x0113
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x0113
            gnu.expr.PairClassType r67 = (gnu.expr.PairClassType) r67
            r0 = r67
            gnu.bytecode.ClassType r0 = r0.instanceType
            r66 = r0
        L_0x0063:
            r0 = r40
            java.lang.String r48 = getMethodName(r6, r0)
            r10 = 86
            r0 = r40
            if (r0 == r10) goto L_0x0075
            r10 = 42
            r0 = r40
            if (r0 != r10) goto L_0x0123
        L_0x0075:
            int r7 = r49 + -1
            r8 = 2
            r9 = 0
        L_0x0079:
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x0228
            r0 = r66
            boolean r10 = r0 instanceof gnu.bytecode.ArrayType
            if (r10 == 0) goto L_0x0228
            r22 = r66
            gnu.bytecode.ArrayType r22 = (gnu.bytecode.ArrayType) r22
            gnu.bytecode.Type r30 = r22.getComponentType()
            r61 = 0
            r43 = 0
            int r10 = r6.length
            r11 = 3
            if (r10 < r11) goto L_0x00ca
            r10 = 1
            r10 = r6[r10]
            boolean r10 = r10 instanceof gnu.expr.QuoteExp
            if (r10 == 0) goto L_0x00ca
            r10 = 1
            r10 = r6[r10]
            gnu.expr.QuoteExp r10 = (gnu.expr.QuoteExp) r10
            java.lang.Object r21 = r10.getValue()
            r0 = r21
            boolean r10 = r0 instanceof gnu.expr.Keyword
            if (r10 == 0) goto L_0x00ca
            java.lang.String r10 = "length"
            gnu.expr.Keyword r21 = (gnu.expr.Keyword) r21
            java.lang.String r48 = r21.getName()
            r0 = r48
            boolean r10 = r10.equals(r0)
            if (r10 != 0) goto L_0x00c5
            java.lang.String r10 = "size"
            r0 = r48
            boolean r10 = r10.equals(r0)
            if (r10 == 0) goto L_0x00ca
        L_0x00c5:
            r10 = 2
            r61 = r6[r10]
            r43 = 1
        L_0x00ca:
            if (r61 != 0) goto L_0x00d8
            java.lang.Integer r10 = new java.lang.Integer
            int r11 = r6.length
            int r11 = r11 + -1
            r10.<init>(r11)
            gnu.expr.QuoteExp r61 = gnu.expr.QuoteExp.getInstance(r10)
        L_0x00d8:
            gnu.bytecode.PrimType r10 = gnu.bytecode.Type.intType
            r0 = r71
            r1 = r61
            gnu.expr.Expression r61 = r0.visit((gnu.expr.Expression) r1, (gnu.bytecode.Type) r10)
            gnu.expr.ApplyExp r18 = new gnu.expr.ApplyExp
            gnu.kawa.reflect.ArrayNew r10 = new gnu.kawa.reflect.ArrayNew
            r0 = r30
            r10.<init>(r0)
            r11 = 1
            gnu.expr.Expression[] r11 = new gnu.expr.Expression[r11]
            r12 = 0
            r11[r12] = r61
            r0 = r18
            r0.<init>((gnu.mapping.Procedure) r10, (gnu.expr.Expression[]) r11)
            r0 = r18
            r1 = r22
            r0.setType(r1)
            if (r43 == 0) goto L_0x0152
            int r10 = r6.length
            r11 = 3
            if (r10 != r11) goto L_0x0152
            r70 = r18
            goto L_0x0031
        L_0x0107:
            r0 = r36
            gnu.expr.Language r10 = r0.language
            r0 = r20
            gnu.bytecode.Type r67 = r10.getTypeFor((gnu.expr.Expression) r0)
            goto L_0x004f
        L_0x0113:
            r0 = r67
            boolean r10 = r0 instanceof gnu.bytecode.ObjectType
            if (r10 == 0) goto L_0x011f
            r66 = r67
            gnu.bytecode.ObjectType r66 = (gnu.bytecode.ObjectType) r66
            goto L_0x0063
        L_0x011f:
            r66 = 0
            goto L_0x0063
        L_0x0123:
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x012f
            r7 = r49
            r8 = 0
            r9 = -1
            goto L_0x0079
        L_0x012f:
            r10 = 83
            r0 = r40
            if (r0 == r10) goto L_0x013b
            r10 = 115(0x73, float:1.61E-43)
            r0 = r40
            if (r0 != r10) goto L_0x0141
        L_0x013b:
            int r7 = r49 + -2
            r8 = 2
            r9 = -1
            goto L_0x0079
        L_0x0141:
            r10 = 80
            r0 = r40
            if (r0 != r10) goto L_0x014d
            int r7 = r49 + -2
            r8 = 3
            r9 = 1
            goto L_0x0079
        L_0x014d:
            r70.visitArgs(r71)
            goto L_0x0031
        L_0x0152:
            gnu.expr.LetExp r44 = new gnu.expr.LetExp
            r10 = 1
            gnu.expr.Expression[] r10 = new gnu.expr.Expression[r10]
            r11 = 0
            r10[r11] = r18
            r0 = r44
            r0.<init>(r10)
            r10 = 0
            java.lang.String r10 = (java.lang.String) r10
            r0 = r44
            r1 = r22
            gnu.expr.Declaration r16 = r0.addDeclaration(r10, r1)
            r0 = r16
            r1 = r18
            r0.noteValue(r1)
            gnu.expr.BeginExp r23 = new gnu.expr.BeginExp
            r23.<init>()
            r35 = 0
            if (r43 == 0) goto L_0x01ea
            r33 = 3
        L_0x017c:
            int r10 = r6.length
            r0 = r33
            if (r0 >= r10) goto L_0x0212
            r19 = r6[r33]
            if (r43 == 0) goto L_0x01ac
            int r10 = r33 + 1
            int r11 = r6.length
            if (r10 >= r11) goto L_0x01ac
            r0 = r19
            boolean r10 = r0 instanceof gnu.expr.QuoteExp
            if (r10 == 0) goto L_0x01ac
            r10 = r19
            gnu.expr.QuoteExp r10 = (gnu.expr.QuoteExp) r10
            java.lang.Object r38 = r10.getValue()
            r0 = r38
            boolean r10 = r0 instanceof gnu.expr.Keyword
            if (r10 == 0) goto L_0x01ac
            gnu.expr.Keyword r38 = (gnu.expr.Keyword) r38
            java.lang.String r41 = r38.getName()
            int r35 = java.lang.Integer.parseInt(r41)     // Catch:{ Throwable -> 0x01ed }
            int r33 = r33 + 1
            r19 = r6[r33]     // Catch:{ Throwable -> 0x01ed }
        L_0x01ac:
            r0 = r71
            r1 = r19
            r2 = r30
            gnu.expr.Expression r19 = r0.visit((gnu.expr.Expression) r1, (gnu.bytecode.Type) r2)
            gnu.expr.ApplyExp r10 = new gnu.expr.ApplyExp
            gnu.kawa.reflect.ArraySet r11 = new gnu.kawa.reflect.ArraySet
            r0 = r30
            r11.<init>(r0)
            r12 = 3
            gnu.expr.Expression[] r12 = new gnu.expr.Expression[r12]
            r13 = 0
            gnu.expr.ReferenceExp r14 = new gnu.expr.ReferenceExp
            r0 = r16
            r14.<init>((gnu.expr.Declaration) r0)
            r12[r13] = r14
            r13 = 1
            java.lang.Integer r14 = new java.lang.Integer
            r0 = r35
            r14.<init>(r0)
            gnu.expr.QuoteExp r14 = gnu.expr.QuoteExp.getInstance(r14)
            r12[r13] = r14
            r13 = 2
            r12[r13] = r19
            r10.<init>((gnu.mapping.Procedure) r11, (gnu.expr.Expression[]) r12)
            r0 = r23
            r0.add(r10)
            int r35 = r35 + 1
            int r33 = r33 + 1
            goto L_0x017c
        L_0x01ea:
            r33 = 1
            goto L_0x017c
        L_0x01ed:
            r32 = move-exception
            r10 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "non-integer keyword '"
            java.lang.StringBuilder r11 = r11.append(r12)
            r0 = r41
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r12 = "' in array constructor"
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r0 = r25
            r0.error(r10, r11)
            goto L_0x0031
        L_0x0212:
            gnu.expr.ReferenceExp r10 = new gnu.expr.ReferenceExp
            r0 = r16
            r10.<init>((gnu.expr.Declaration) r0)
            r0 = r23
            r0.add(r10)
            r0 = r23
            r1 = r44
            r1.body = r0
            r70 = r44
            goto L_0x0031
        L_0x0228:
            if (r66 == 0) goto L_0x0748
            if (r48 == 0) goto L_0x0748
            r0 = r66
            boolean r10 = r0 instanceof gnu.expr.TypeValue
            if (r10 == 0) goto L_0x0264
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x0264
            r10 = r66
            gnu.expr.TypeValue r10 = (gnu.expr.TypeValue) r10
            gnu.mapping.Procedure r26 = r10.getConstructor()
            if (r26 == 0) goto L_0x0264
            int r10 = r49 + -1
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r10]
            r69 = r0
            r10 = 1
            r11 = 0
            int r12 = r49 + -1
            r0 = r69
            java.lang.System.arraycopy(r6, r10, r0, r11, r12)
            gnu.expr.ApplyExp r10 = new gnu.expr.ApplyExp
            r0 = r26
            r1 = r69
            r10.<init>((gnu.mapping.Procedure) r0, (gnu.expr.Expression[]) r1)
            r0 = r71
            r1 = r72
            gnu.expr.Expression r70 = r0.visit((gnu.expr.Expression) r10, (gnu.bytecode.Type) r1)
            goto L_0x0031
        L_0x0264:
            if (r25 != 0) goto L_0x02fe
            r24 = 0
        L_0x0268:
            r5 = r66
            r0 = r48
            r1 = r24
            r2 = r36
            gnu.expr.PrimProcedure[] r4 = getMethods(r5, r0, r1, r2)     // Catch:{ Exception -> 0x0314 }
            int r51 = gnu.kawa.reflect.ClassMethods.selectApplicable((gnu.expr.PrimProcedure[]) r4, (int) r7)     // Catch:{ Exception -> 0x0314 }
            r35 = -1
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x04aa
            r10 = 1
            int r39 = hasKeywordArgument(r10, r6)
            int r10 = r6.length
            r0 = r39
            if (r0 < r10) goto L_0x02a1
            if (r51 > 0) goto L_0x04aa
            r10 = 1
            gnu.bytecode.Type[] r10 = new gnu.bytecode.Type[r10]
            r11 = 0
            gnu.bytecode.ClassType r12 = gnu.expr.Compilation.typeClassType
            r10[r11] = r12
            long r10 = gnu.kawa.reflect.ClassMethods.selectApplicable((gnu.expr.PrimProcedure[]) r4, (gnu.bytecode.Type[]) r10)
            r12 = 32
            long r10 = r10 >> r12
            r12 = 1
            int r10 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r10 != 0) goto L_0x04aa
        L_0x02a1:
            r0 = r39
            r1 = r24
            java.lang.Object[] r63 = checkKeywords(r5, r6, r0, r1)
            r0 = r63
            int r10 = r0.length
            int r10 = r10 * 2
            int r11 = r6.length
            int r11 = r11 - r39
            if (r10 == r11) goto L_0x02c7
            java.lang.String r10 = "add"
            r11 = 86
            r12 = 0
            r0 = r36
            gnu.expr.Language r13 = r0.language
            gnu.expr.PrimProcedure[] r10 = gnu.kawa.reflect.ClassMethods.getMethods(r5, r10, r11, r12, r13)
            r11 = 2
            int r10 = gnu.kawa.reflect.ClassMethods.selectApplicable((gnu.expr.PrimProcedure[]) r10, (int) r11)
            if (r10 <= 0) goto L_0x04aa
        L_0x02c7:
            r31 = 0
            r33 = 0
        L_0x02cb:
            r0 = r63
            int r10 = r0.length
            r0 = r33
            if (r0 >= r10) goto L_0x033d
            r10 = r63[r33]
            boolean r10 = r10 instanceof java.lang.String
            if (r10 == 0) goto L_0x02fb
            if (r31 != 0) goto L_0x0335
            java.lang.StringBuffer r31 = new java.lang.StringBuffer
            r31.<init>()
            java.lang.String r10 = "no field or setter "
            r0 = r31
            r0.append(r10)
        L_0x02e6:
            r10 = 96
            r0 = r31
            r0.append(r10)
            r10 = r63[r33]
            r0 = r31
            r0.append(r10)
            r10 = 39
            r0 = r31
            r0.append(r10)
        L_0x02fb:
            int r33 = r33 + 1
            goto L_0x02cb
        L_0x02fe:
            r0 = r25
            gnu.bytecode.ClassType r10 = r0.curClass
            if (r10 == 0) goto L_0x030c
            r0 = r25
            gnu.bytecode.ClassType r0 = r0.curClass
            r24 = r0
            goto L_0x0268
        L_0x030c:
            r0 = r25
            gnu.bytecode.ClassType r0 = r0.mainClass
            r24 = r0
            goto L_0x0268
        L_0x0314:
            r32 = move-exception
            r10 = 119(0x77, float:1.67E-43)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "unknown class: "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = r66.getName()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r0 = r25
            r0.error(r10, r11)
            goto L_0x0031
        L_0x0335:
            java.lang.String r10 = ", "
            r0 = r31
            r0.append(r10)
            goto L_0x02e6
        L_0x033d:
            if (r31 == 0) goto L_0x035c
            java.lang.String r10 = " in class "
            r0 = r31
            r0.append(r10)
            java.lang.String r10 = r66.getName()
            r0 = r31
            r0.append(r10)
            r10 = 119(0x77, float:1.67E-43)
            java.lang.String r11 = r31.toString()
            r0 = r25
            r0.error(r10, r11)
            goto L_0x0031
        L_0x035c:
            int r10 = r6.length
            r0 = r39
            if (r0 >= r10) goto L_0x03ed
            r0 = r39
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r0]
            r69 = r0
            r10 = 0
            r11 = 0
            r0 = r69
            r1 = r39
            java.lang.System.arraycopy(r6, r10, r0, r11, r1)
            gnu.expr.ApplyExp r10 = new gnu.expr.ApplyExp
            gnu.expr.Expression r11 = r70.getFunction()
            r0 = r69
            r10.<init>((gnu.expr.Expression) r11, (gnu.expr.Expression[]) r0)
            r0 = r71
            gnu.expr.Expression r17 = r0.visit((gnu.expr.Expression) r10, (gnu.bytecode.Type) r5)
            gnu.expr.ApplyExp r17 = (gnu.expr.ApplyExp) r17
        L_0x0383:
            r0 = r17
            r0.setType(r5)
            r29 = r17
            int r10 = r6.length
            if (r10 <= 0) goto L_0x0498
            r33 = 0
        L_0x038f:
            r0 = r63
            int r10 = r0.length
            r0 = r33
            if (r0 >= r10) goto L_0x0410
            r62 = r63[r33]
            r0 = r62
            boolean r10 = r0 instanceof gnu.bytecode.Method
            if (r10 == 0) goto L_0x03fe
            r10 = r62
            gnu.bytecode.Method r10 = (gnu.bytecode.Method) r10
            gnu.bytecode.Type[] r10 = r10.getParameterTypes()
            r11 = 0
            r65 = r10[r11]
        L_0x03a9:
            if (r65 == 0) goto L_0x03b5
            r0 = r36
            gnu.expr.Language r10 = r0.language
            r0 = r65
            gnu.bytecode.Type r65 = r10.getLangTypeFor(r0)
        L_0x03b5:
            int r10 = r33 * 2
            int r10 = r10 + r39
            int r10 = r10 + 1
            r10 = r6[r10]
            r0 = r71
            r1 = r65
            gnu.expr.Expression r19 = r0.visit((gnu.expr.Expression) r10, (gnu.bytecode.Type) r1)
            r10 = 3
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r10]
            r59 = r0
            r10 = 0
            r59[r10] = r17
            r10 = 1
            gnu.expr.QuoteExp r11 = new gnu.expr.QuoteExp
            r0 = r62
            r11.<init>(r0)
            r59[r10] = r11
            r10 = 2
            r59[r10] = r19
            gnu.expr.ApplyExp r17 = new gnu.expr.ApplyExp
            gnu.kawa.reflect.SlotSet r10 = gnu.kawa.reflect.SlotSet.setFieldReturnObject
            r0 = r17
            r1 = r59
            r0.<init>((gnu.mapping.Procedure) r10, (gnu.expr.Expression[]) r1)
            r0 = r17
            r0.setType(r5)
            int r33 = r33 + 1
            goto L_0x038f
        L_0x03ed:
            gnu.expr.ApplyExp r17 = new gnu.expr.ApplyExp
            r10 = 0
            r10 = r4[r10]
            r11 = 1
            gnu.expr.Expression[] r11 = new gnu.expr.Expression[r11]
            r12 = 0
            r11[r12] = r20
            r0 = r17
            r0.<init>((gnu.mapping.Procedure) r10, (gnu.expr.Expression[]) r11)
            goto L_0x0383
        L_0x03fe:
            r0 = r62
            boolean r10 = r0 instanceof gnu.bytecode.Field
            if (r10 == 0) goto L_0x040d
            r10 = r62
            gnu.bytecode.Field r10 = (gnu.bytecode.Field) r10
            gnu.bytecode.Type r65 = r10.getType()
            goto L_0x03a9
        L_0x040d:
            r65 = 0
            goto L_0x03a9
        L_0x0410:
            int r10 = r6.length
            r0 = r39
            if (r0 != r10) goto L_0x047c
            r59 = 1
        L_0x0417:
            r29 = r17
            int r10 = r6.length
            r0 = r59
            if (r0 >= r10) goto L_0x0498
            gnu.expr.LetExp r44 = new gnu.expr.LetExp
            r10 = 1
            gnu.expr.Expression[] r10 = new gnu.expr.Expression[r10]
            r11 = 0
            r10[r11] = r29
            r0 = r44
            r0.<init>(r10)
            r10 = 0
            java.lang.String r10 = (java.lang.String) r10
            r0 = r44
            gnu.expr.Declaration r16 = r0.addDeclaration(r10, r5)
            r0 = r16
            r1 = r29
            r0.noteValue(r1)
            gnu.expr.BeginExp r23 = new gnu.expr.BeginExp
            r23.<init>()
            r33 = r59
        L_0x0442:
            int r10 = r6.length
            r0 = r33
            if (r0 >= r10) goto L_0x0484
            r10 = 3
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r10]
            r34 = r0
            r10 = 0
            gnu.expr.ReferenceExp r11 = new gnu.expr.ReferenceExp
            r0 = r16
            r11.<init>((gnu.expr.Declaration) r0)
            r34[r10] = r11
            r10 = 1
            java.lang.String r11 = "add"
            gnu.expr.QuoteExp r11 = gnu.expr.QuoteExp.getInstance(r11)
            r34[r10] = r11
            r10 = 2
            r11 = r6[r33]
            r34[r10] = r11
            gnu.expr.ApplyExp r10 = new gnu.expr.ApplyExp
            gnu.kawa.reflect.Invoke r11 = gnu.kawa.reflect.Invoke.invoke
            r0 = r34
            r10.<init>((gnu.mapping.Procedure) r11, (gnu.expr.Expression[]) r0)
            r11 = 0
            r0 = r71
            gnu.expr.Expression r10 = r0.visit((gnu.expr.Expression) r10, (gnu.bytecode.Type) r11)
            r0 = r23
            r0.add(r10)
            int r33 = r33 + 1
            goto L_0x0442
        L_0x047c:
            r0 = r63
            int r10 = r0.length
            int r10 = r10 * 2
            int r59 = r10 + r39
            goto L_0x0417
        L_0x0484:
            gnu.expr.ReferenceExp r10 = new gnu.expr.ReferenceExp
            r0 = r16
            r10.<init>((gnu.expr.Declaration) r0)
            r0 = r23
            r0.add(r10)
            r0 = r23
            r1 = r44
            r1.body = r0
            r29 = r44
        L_0x0498:
            r0 = r29
            r1 = r70
            gnu.expr.Expression r10 = r0.setLine((gnu.expr.Expression) r1)
            r0 = r71
            r1 = r72
            gnu.expr.Expression r70 = r0.checkType(r10, r1)
            goto L_0x0031
        L_0x04aa:
            if (r51 < 0) goto L_0x062b
            r33 = 1
        L_0x04ae:
            r0 = r33
            r1 = r49
            if (r0 >= r1) goto L_0x0558
            r22 = 0
            int r10 = r49 + -1
            r0 = r33
            if (r0 != r10) goto L_0x04e5
            r42 = 1
        L_0x04be:
            r10 = 80
            r0 = r40
            if (r0 != r10) goto L_0x04c9
            r10 = 2
            r0 = r33
            if (r0 == r10) goto L_0x04d4
        L_0x04c9:
            r10 = 78
            r0 = r40
            if (r0 == r10) goto L_0x04e8
            r10 = 1
            r0 = r33
            if (r0 != r10) goto L_0x04e8
        L_0x04d4:
            r22 = 0
        L_0x04d6:
            r10 = r6[r33]
            r0 = r71
            r1 = r22
            gnu.expr.Expression r10 = r0.visit((gnu.expr.Expression) r10, (gnu.bytecode.Type) r1)
            r6[r33] = r10
            int r33 = r33 + 1
            goto L_0x04ae
        L_0x04e5:
            r42 = 0
            goto L_0x04be
        L_0x04e8:
            r10 = 80
            r0 = r40
            if (r0 != r10) goto L_0x04f6
            r10 = 1
            r0 = r33
            if (r0 != r10) goto L_0x04f6
            r22 = r5
            goto L_0x04d6
        L_0x04f6:
            if (r51 <= 0) goto L_0x04d6
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x0531
            r10 = 1
        L_0x04ff:
            int r55 = r33 - r10
            r37 = 0
        L_0x0503:
            r0 = r37
            r1 = r51
            if (r0 >= r1) goto L_0x04d6
            r57 = r4[r37]
            r10 = 83
            r0 = r40
            if (r0 == r10) goto L_0x0533
            boolean r10 = r57.takesTarget()
            if (r10 == 0) goto L_0x0533
            r10 = 1
        L_0x0518:
            int r56 = r55 + r10
            if (r42 == 0) goto L_0x0535
            boolean r10 = r57.takesVarArgs()
            if (r10 == 0) goto L_0x0535
            int r10 = r57.minArgs()
            r0 = r56
            if (r0 != r10) goto L_0x0535
            r22 = 0
        L_0x052c:
            if (r22 == 0) goto L_0x04d6
            int r37 = r37 + 1
            goto L_0x0503
        L_0x0531:
            r10 = r8
            goto L_0x04ff
        L_0x0533:
            r10 = 0
            goto L_0x0518
        L_0x0535:
            r0 = r57
            r1 = r56
            gnu.bytecode.Type r58 = r0.getParameterType(r1)
            if (r37 != 0) goto L_0x0542
            r22 = r58
            goto L_0x052c
        L_0x0542:
            r0 = r58
            boolean r10 = r0 instanceof gnu.bytecode.PrimType
            r0 = r22
            boolean r11 = r0 instanceof gnu.bytecode.PrimType
            if (r10 == r11) goto L_0x054f
            r22 = 0
            goto L_0x052c
        L_0x054f:
            r0 = r22
            r1 = r58
            gnu.bytecode.Type r22 = gnu.bytecode.Type.lowestCommonSuperType(r0, r1)
            goto L_0x052c
        L_0x0558:
            long r52 = selectApplicable(r4, r5, r6, r7, r8, r9)
            r10 = 32
            long r10 = r52 >> r10
            int r0 = (int) r10
            r54 = r0
            r0 = r52
            int r0 = (int) r0
            r46 = r0
        L_0x0568:
            int r0 = r4.length
            r50 = r0
            int r10 = r54 + r46
            if (r10 != 0) goto L_0x0598
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x0598
            java.lang.String r10 = "valueOf"
            gnu.kawa.reflect.Invoke r11 = gnu.kawa.reflect.Invoke.invokeStatic
            r0 = r24
            gnu.expr.PrimProcedure[] r4 = getMethods(r5, r10, r0, r11)
            r8 = 1
            int r7 = r49 + -1
            r15 = -1
            r10 = r4
            r11 = r5
            r12 = r6
            r13 = r7
            r14 = r8
            long r52 = selectApplicable(r10, r11, r12, r13, r14, r15)
            r10 = 32
            long r10 = r52 >> r10
            int r0 = (int) r10
            r54 = r0
            r0 = r52
            int r0 = (int) r0
            r46 = r0
        L_0x0598:
            int r10 = r54 + r46
            if (r10 != 0) goto L_0x0658
            r10 = 80
            r0 = r40
            if (r0 == r10) goto L_0x05a8
            boolean r10 = r25.warnInvokeUnknownMethod()
            if (r10 == 0) goto L_0x05fc
        L_0x05a8:
            r10 = 78
            r0 = r40
            if (r0 != r10) goto L_0x05c3
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            r0 = r48
            java.lang.StringBuilder r10 = r10.append(r0)
            java.lang.String r11 = "/valueOf"
            java.lang.StringBuilder r10 = r10.append(r11)
            java.lang.String r48 = r10.toString()
        L_0x05c3:
            java.lang.StringBuilder r60 = new java.lang.StringBuilder
            r60.<init>()
            int r10 = r4.length
            int r10 = r10 + r50
            if (r10 != 0) goto L_0x0631
            java.lang.String r10 = "no accessible method '"
            r0 = r60
            r0.append(r10)
        L_0x05d4:
            r0 = r60
            r1 = r48
            r0.append(r1)
            java.lang.String r10 = "' in "
            r0 = r60
            r0.append(r10)
            java.lang.String r10 = r66.getName()
            r0 = r60
            r0.append(r10)
            r10 = 80
            r0 = r40
            if (r0 != r10) goto L_0x0655
            r10 = 101(0x65, float:1.42E-43)
        L_0x05f3:
            java.lang.String r11 = r60.toString()
            r0 = r25
            r0.error(r10, r11)
        L_0x05fc:
            if (r35 < 0) goto L_0x0748
            gnu.expr.Expression[] r0 = new gnu.expr.Expression[r7]
            r45 = r0
            r47 = r4[r35]
            boolean r68 = r47.takesVarArgs()
            r27 = 0
            if (r9 < 0) goto L_0x0614
            int r28 = r27 + 1
            r10 = r6[r9]
            r45[r27] = r10
            r27 = r28
        L_0x0614:
            r64 = r8
        L_0x0616:
            int r10 = r6.length
            r0 = r64
            if (r0 >= r10) goto L_0x072a
            r0 = r45
            int r10 = r0.length
            r0 = r27
            if (r0 >= r10) goto L_0x072a
            r10 = r6[r64]
            r45[r27] = r10
            int r64 = r64 + 1
            int r27 = r27 + 1
            goto L_0x0616
        L_0x062b:
            r54 = 0
            r46 = 0
            goto L_0x0568
        L_0x0631:
            r10 = -983040(0xfffffffffff10000, float:NaN)
            r0 = r51
            if (r0 != r10) goto L_0x063f
            java.lang.String r10 = "too few arguments for method '"
            r0 = r60
            r0.append(r10)
            goto L_0x05d4
        L_0x063f:
            r10 = -917504(0xfffffffffff20000, float:NaN)
            r0 = r51
            if (r0 != r10) goto L_0x064d
            java.lang.String r10 = "too many arguments for method '"
            r0 = r60
            r0.append(r10)
            goto L_0x05d4
        L_0x064d:
            java.lang.String r10 = "no possibly applicable method '"
            r0 = r60
            r0.append(r10)
            goto L_0x05d4
        L_0x0655:
            r10 = 119(0x77, float:1.67E-43)
            goto L_0x05f3
        L_0x0658:
            r10 = 1
            r0 = r54
            if (r0 == r10) goto L_0x0664
            if (r54 != 0) goto L_0x0667
            r10 = 1
            r0 = r46
            if (r0 != r10) goto L_0x0667
        L_0x0664:
            r35 = 0
            goto L_0x05fc
        L_0x0667:
            if (r54 <= 0) goto L_0x06de
            r0 = r54
            int r35 = gnu.mapping.MethodProc.mostSpecific((gnu.mapping.MethodProc[]) r4, (int) r0)
            if (r35 >= 0) goto L_0x068b
            r10 = 83
            r0 = r40
            if (r0 != r10) goto L_0x068b
            r33 = 0
        L_0x0679:
            r0 = r33
            r1 = r54
            if (r0 >= r1) goto L_0x068b
            r10 = r4[r33]
            boolean r10 = r10.getStaticFlag()
            if (r10 == 0) goto L_0x06d8
            if (r35 < 0) goto L_0x06d6
            r35 = -1
        L_0x068b:
            if (r35 >= 0) goto L_0x05fc
            r10 = 80
            r0 = r40
            if (r0 == r10) goto L_0x0699
            boolean r10 = r25.warnInvokeUnknownMethod()
            if (r10 == 0) goto L_0x05fc
        L_0x0699:
            java.lang.StringBuffer r60 = new java.lang.StringBuffer
            r60.<init>()
            java.lang.String r10 = "more than one definitely applicable method `"
            r0 = r60
            r0.append(r10)
            r0 = r60
            r1 = r48
            r0.append(r1)
            java.lang.String r10 = "' in "
            r0 = r60
            r0.append(r10)
            java.lang.String r10 = r66.getName()
            r0 = r60
            r0.append(r10)
            r0 = r54
            r1 = r60
            append(r4, r0, r1)
            r10 = 80
            r0 = r40
            if (r0 != r10) goto L_0x06db
            r10 = 101(0x65, float:1.42E-43)
        L_0x06cb:
            java.lang.String r11 = r60.toString()
            r0 = r25
            r0.error(r10, r11)
            goto L_0x05fc
        L_0x06d6:
            r35 = r33
        L_0x06d8:
            int r33 = r33 + 1
            goto L_0x0679
        L_0x06db:
            r10 = 119(0x77, float:1.67E-43)
            goto L_0x06cb
        L_0x06de:
            r10 = 80
            r0 = r40
            if (r0 == r10) goto L_0x06ea
            boolean r10 = r25.warnInvokeUnknownMethod()
            if (r10 == 0) goto L_0x05fc
        L_0x06ea:
            java.lang.StringBuffer r60 = new java.lang.StringBuffer
            r60.<init>()
            java.lang.String r10 = "more than one possibly applicable method '"
            r0 = r60
            r0.append(r10)
            r0 = r60
            r1 = r48
            r0.append(r1)
            java.lang.String r10 = "' in "
            r0 = r60
            r0.append(r10)
            java.lang.String r10 = r66.getName()
            r0 = r60
            r0.append(r10)
            r0 = r46
            r1 = r60
            append(r4, r0, r1)
            r10 = 80
            r0 = r40
            if (r0 != r10) goto L_0x0727
            r10 = 101(0x65, float:1.42E-43)
        L_0x071c:
            java.lang.String r11 = r60.toString()
            r0 = r25
            r0.error(r10, r11)
            goto L_0x05fc
        L_0x0727:
            r10 = 119(0x77, float:1.67E-43)
            goto L_0x071c
        L_0x072a:
            gnu.expr.ApplyExp r29 = new gnu.expr.ApplyExp
            r0 = r29
            r1 = r47
            r2 = r45
            r0.<init>((gnu.mapping.Procedure) r1, (gnu.expr.Expression[]) r2)
            r0 = r29
            r1 = r70
            r0.setLine((gnu.expr.Expression) r1)
            r0 = r71
            r1 = r29
            r2 = r72
            gnu.expr.Expression r70 = r0.visitApplyOnly(r1, r2)
            goto L_0x0031
        L_0x0748:
            r70.visitArgs(r71)
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.reflect.CompileInvoke.validateApplyInvoke(gnu.expr.ApplyExp, gnu.expr.InlineCalls, gnu.bytecode.Type, gnu.mapping.Procedure):gnu.expr.Expression");
    }

    static Object[] checkKeywords(ObjectType type, Expression[] args, int start, ClassType caller) {
        int len = args.length;
        int npairs = 0;
        while ((npairs * 2) + start + 1 < len && (args[(npairs * 2) + start].valueIfConstant() instanceof Keyword)) {
            npairs++;
        }
        Object[] fields = new Object[npairs];
        for (int i = 0; i < npairs; i++) {
            String name = ((Keyword) args[(i * 2) + start].valueIfConstant()).getName();
            String lookupMember = SlotSet.lookupMember(type, name, caller);
            if (lookupMember == null) {
                lookupMember = type.getMethod(ClassExp.slotToMethodName("add", name), SlotSet.type1Array);
            }
            if (lookupMember == null) {
                lookupMember = name;
            }
            fields[i] = lookupMember;
        }
        return fields;
    }

    private static String getMethodName(Expression[] args, char kind) {
        if (kind == 'N') {
            return "<init>";
        }
        int nameIndex = kind == 'P' ? 2 : 1;
        if (args.length >= nameIndex + 1) {
            return ClassMethods.checkName(args[nameIndex], false);
        }
        return null;
    }

    private static void append(PrimProcedure[] methods, int mcount, StringBuffer sbuf) {
        for (int i = 0; i < mcount; i++) {
            sbuf.append("\n  candidate: ");
            sbuf.append(methods[i]);
        }
    }

    protected static PrimProcedure[] getMethods(ObjectType ctype, String mname, ClassType caller, Invoke iproc) {
        char c = 'P';
        int kind = iproc.kind;
        if (kind != 80) {
            c = (kind == 42 || kind == 86) ? 'V' : 0;
        }
        return ClassMethods.getMethods(ctype, mname, c, caller, iproc.language);
    }

    static int hasKeywordArgument(int argsStartIndex, Expression[] args) {
        for (int i = argsStartIndex; i < args.length; i++) {
            if (args[i].valueIfConstant() instanceof Keyword) {
                return i;
            }
        }
        return args.length;
    }

    private static long selectApplicable(PrimProcedure[] methods, ObjectType ctype, Expression[] args, int margsLength, int argsStartIndex, int objIndex) {
        Type[] atypes = new Type[margsLength];
        int dst = 0;
        if (objIndex >= 0) {
            atypes[0] = ctype;
            dst = 0 + 1;
        }
        int src = argsStartIndex;
        while (src < args.length && dst < atypes.length) {
            Expression arg = args[src];
            Type atype = null;
            if (InlineCalls.checkIntValue(arg) != null) {
                atype = Type.intType;
            } else if (InlineCalls.checkLongValue(arg) != null) {
                atype = Type.longType;
            } else if (0 == 0) {
                atype = arg.getType();
            }
            atypes[dst] = atype;
            src++;
            dst++;
        }
        return ClassMethods.selectApplicable(methods, atypes);
    }

    public static synchronized PrimProcedure getStaticMethod(ClassType type, String name, Expression[] args) {
        int index;
        PrimProcedure primProcedure;
        synchronized (CompileInvoke.class) {
            PrimProcedure[] methods = getMethods(type, name, (ClassType) null, Invoke.invokeStatic);
            long num = selectApplicable(methods, type, args, args.length, 0, -1);
            int okCount = (int) (num >> 32);
            int maybeCount = (int) num;
            if (methods == null) {
                index = -1;
            } else if (okCount > 0) {
                index = MethodProc.mostSpecific((MethodProc[]) methods, okCount);
            } else if (maybeCount == 1) {
                index = 0;
            } else {
                index = -1;
            }
            if (index < 0) {
                primProcedure = null;
            } else {
                primProcedure = methods[index];
            }
        }
        return primProcedure;
    }
}
