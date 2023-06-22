package kawa.lang;

import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.ModuleExp;
import gnu.expr.ScopeExp;
import gnu.lists.Consumer;
import gnu.lists.FVector;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.OutPort;
import gnu.mapping.Symbol;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.util.Vector;

public class SyntaxPattern extends Pattern implements Externalizable {
    static final int MATCH_ANY = 3;
    static final int MATCH_ANY_CAR = 7;
    static final int MATCH_EQUALS = 2;
    static final int MATCH_IGNORE = 24;
    static final int MATCH_LENGTH = 6;
    static final int MATCH_LREPEAT = 5;
    static final int MATCH_MISC = 0;
    static final int MATCH_NIL = 8;
    static final int MATCH_PAIR = 4;
    static final int MATCH_VECTOR = 16;
    static final int MATCH_WIDE = 1;
    Object[] literals;
    String program;
    int varCount;

    public int varCount() {
        return this.varCount;
    }

    public boolean match(Object obj, Object[] vars, int start_vars) {
        return match(obj, vars, start_vars, 0, (SyntaxForm) null);
    }

    public SyntaxPattern(String program2, Object[] literals2, int varCount2) {
        this.program = program2;
        this.literals = literals2;
        this.varCount = varCount2;
    }

    public SyntaxPattern(Object pattern, Object[] literal_identifiers, Translator tr) {
        this(new StringBuffer(), pattern, (SyntaxForm) null, literal_identifiers, tr);
    }

    SyntaxPattern(StringBuffer programbuf, Object pattern, SyntaxForm syntax, Object[] literal_identifiers, Translator tr) {
        Vector literalsbuf = new Vector();
        translate(pattern, programbuf, literal_identifiers, 0, literalsbuf, (SyntaxForm) null, 0, tr);
        this.program = programbuf.toString();
        this.literals = new Object[literalsbuf.size()];
        literalsbuf.copyInto(this.literals);
        this.varCount = tr.patternScope.pattern_names.size();
    }

    public void disassemble() {
        disassemble(OutPort.errDefault(), (Translator) Compilation.getCurrent(), 0, this.program.length());
    }

    public void disassemble(PrintWriter ps, Translator tr) {
        disassemble(ps, tr, 0, this.program.length());
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void disassemble(java.io.PrintWriter r10, kawa.lang.Translator r11, int r12, int r13) {
        /*
            r9 = this;
            r8 = 8
            r4 = 0
            if (r11 == 0) goto L_0x000d
            kawa.lang.PatternScope r6 = r11.patternScope
            if (r6 == 0) goto L_0x000d
            kawa.lang.PatternScope r6 = r11.patternScope
            java.util.Vector r4 = r6.pattern_names
        L_0x000d:
            r5 = 0
            r1 = r12
        L_0x000f:
            if (r1 >= r13) goto L_0x01ee
            java.lang.String r6 = r9.program
            char r0 = r6.charAt(r1)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r1)
            java.lang.String r7 = ": "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r6 = r6.toString()
            r10.print(r6)
            int r1 = r1 + 1
            r3 = r0 & 7
            int r6 = r5 << 13
            int r7 = r0 >> 3
            r5 = r6 | r7
            switch(r3) {
                case 0: goto L_0x01a9;
                case 1: goto L_0x0066;
                case 2: goto L_0x007d;
                case 3: goto L_0x00af;
                case 4: goto L_0x00e6;
                case 5: goto L_0x0104;
                case 6: goto L_0x017c;
                case 7: goto L_0x00af;
                default: goto L_0x0044;
            }
        L_0x0044:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " - "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r3)
            r7 = 47
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r5)
            java.lang.String r6 = r6.toString()
            r10.println(r6)
        L_0x0064:
            r5 = 0
            goto L_0x000f
        L_0x0066:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " - WIDE "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r5)
            java.lang.String r6 = r6.toString()
            r10.println(r6)
            goto L_0x000f
        L_0x007d:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " - EQUALS["
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r5)
            java.lang.String r7 = "]"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r10.print(r6)
            java.lang.Object[] r6 = r9.literals
            if (r6 == 0) goto L_0x00ab
            if (r5 < 0) goto L_0x00ab
            java.lang.Object[] r6 = r9.literals
            int r6 = r6.length
            if (r5 >= r6) goto L_0x00ab
            java.lang.Object[] r6 = r9.literals
            r6 = r6[r5]
            r10.print(r6)
        L_0x00ab:
            r10.println()
            goto L_0x0064
        L_0x00af:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r6 = 3
            if (r3 != r6) goto L_0x00e3
            java.lang.String r6 = " - ANY["
        L_0x00b9:
            java.lang.StringBuilder r6 = r7.append(r6)
            java.lang.StringBuilder r6 = r6.append(r5)
            java.lang.String r7 = "]"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r10.print(r6)
            if (r4 == 0) goto L_0x00df
            if (r5 < 0) goto L_0x00df
            int r6 = r4.size()
            if (r5 >= r6) goto L_0x00df
            java.lang.Object r6 = r4.elementAt(r5)
            r10.print(r6)
        L_0x00df:
            r10.println()
            goto L_0x0064
        L_0x00e3:
            java.lang.String r6 = " - ANY_CAR["
            goto L_0x00b9
        L_0x00e6:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " - PAIR["
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r5)
            java.lang.String r7 = "]"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r10.println(r6)
            goto L_0x0064
        L_0x0104:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " - LREPEAT["
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r5)
            java.lang.String r7 = "]"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r10.println(r6)
            int r6 = r1 + r5
            r9.disassemble(r10, r11, r1, r6)
            int r1 = r1 + r5
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r1)
            java.lang.String r7 = ": - repeat first var:"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = r9.program
            int r2 = r1 + 1
            char r7 = r7.charAt(r1)
            int r7 = r7 >> 3
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r10.println(r6)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r2)
            java.lang.String r7 = ": - repeast nested vars:"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = r9.program
            int r1 = r2 + 1
            char r7 = r7.charAt(r2)
            int r7 = r7 >> 3
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r10.println(r6)
            goto L_0x0064
        L_0x017c:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = " - LENGTH "
            java.lang.StringBuilder r6 = r6.append(r7)
            int r7 = r5 >> 1
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r7 = " pairs. "
            java.lang.StringBuilder r7 = r6.append(r7)
            r6 = r5 & 1
            if (r6 != 0) goto L_0x01a6
            java.lang.String r6 = "pure list"
        L_0x0199:
            java.lang.StringBuilder r6 = r7.append(r6)
            java.lang.String r6 = r6.toString()
            r10.println(r6)
            goto L_0x0064
        L_0x01a6:
            java.lang.String r6 = "impure list"
            goto L_0x0199
        L_0x01a9:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "[misc ch:"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r0)
            java.lang.String r7 = " n:"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r8)
            java.lang.String r7 = "]"
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.String r6 = r6.toString()
            r10.print(r6)
            if (r0 != r8) goto L_0x01d8
            java.lang.String r6 = " - NIL"
            r10.println(r6)
            goto L_0x0064
        L_0x01d8:
            r6 = 16
            if (r0 != r6) goto L_0x01e3
            java.lang.String r6 = " - VECTOR"
            r10.println(r6)
            goto L_0x0064
        L_0x01e3:
            r6 = 24
            if (r0 != r6) goto L_0x0044
            java.lang.String r6 = " - IGNORE"
            r10.println(r6)
            goto L_0x0064
        L_0x01ee:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.SyntaxPattern.disassemble(java.io.PrintWriter, kawa.lang.Translator, int, int):void");
    }

    /* access modifiers changed from: package-private */
    public void translate(Object pattern, StringBuffer program2, Object[] literal_identifiers, int nesting, Vector literals2, SyntaxForm syntax, char context, Translator tr) {
        int i;
        ScopeExp scope1;
        Object literal;
        ScopeExp scope2;
        Object next;
        int i2;
        int restLength;
        PatternScope patternScope = tr.patternScope;
        Vector patternNames = patternScope.pattern_names;
        while (true) {
            if (pattern instanceof SyntaxForm) {
                syntax = (SyntaxForm) pattern;
                pattern = syntax.getDatum();
            } else if (pattern instanceof Pair) {
                Object savePos = tr.pushPositionOf(pattern);
                try {
                    int start_pc = program2.length();
                    program2.append(4);
                    Pair pair = (Pair) pattern;
                    SyntaxForm car_syntax = syntax;
                    Object next2 = pair.getCdr();
                    while (next instanceof SyntaxForm) {
                        syntax = (SyntaxForm) next;
                        next2 = syntax.getDatum();
                    }
                    boolean repeat = false;
                    if (next instanceof Pair) {
                        if (tr.matches(((Pair) next).getCar(), "...")) {
                            repeat = true;
                            next = ((Pair) next).getCdr();
                            while (next instanceof SyntaxForm) {
                                syntax = (SyntaxForm) next;
                                next = syntax.getDatum();
                            }
                        }
                    }
                    int subvar0 = patternNames.size();
                    if (context == 'P') {
                        context = 0;
                    }
                    Object car = pair.getCar();
                    if (repeat) {
                        i2 = nesting + 1;
                    } else {
                        i2 = nesting;
                    }
                    translate(car, program2, literal_identifiers, i2, literals2, car_syntax, context == 'V' ? 0 : 'P', tr);
                    int subvarN = patternNames.size() - subvar0;
                    int width = (((program2.length() - start_pc) - 1) << 3) | (repeat ? 5 : 4);
                    if (width > 65535) {
                        start_pc += insertInt(start_pc, program2, (width >> 13) + 1);
                    }
                    program2.setCharAt(start_pc, (char) width);
                    int restLength2 = Translator.listLength(next);
                    if (restLength2 == Integer.MIN_VALUE) {
                        tr.syntaxError("cyclic pattern list");
                        tr.popPositionOf(savePos);
                        return;
                    }
                    if (repeat) {
                        addInt(program2, subvar0 << 3);
                        addInt(program2, subvarN << 3);
                        if (next == LList.Empty) {
                            program2.append(8);
                            tr.popPositionOf(savePos);
                            return;
                        }
                        if (restLength2 >= 0) {
                            restLength = restLength2 << 1;
                        } else {
                            restLength = ((-restLength2) << 1) - 1;
                        }
                        addInt(program2, (restLength << 3) | 6);
                    }
                    pattern = next;
                } finally {
                    tr.popPositionOf(savePos);
                }
            } else if (pattern instanceof Symbol) {
                int j = literal_identifiers.length;
                do {
                    j--;
                    if (j >= 0) {
                        ScopeExp current = tr.currentScope();
                        scope1 = syntax == null ? current : syntax.getScope();
                        literal = literal_identifiers[j];
                        if (literal instanceof SyntaxForm) {
                            SyntaxForm syntax2 = (SyntaxForm) literal;
                            literal = syntax2.getDatum();
                            scope2 = syntax2.getScope();
                        } else if (tr.currentMacroDefinition != null) {
                            scope2 = tr.currentMacroDefinition.getCapturedScope();
                        } else {
                            scope2 = current;
                        }
                    } else {
                        if (patternNames.contains(pattern)) {
                            tr.syntaxError("duplicated pattern variable " + pattern);
                        }
                        int i3 = patternNames.size();
                        patternNames.addElement(pattern);
                        boolean matchCar = context == 'P';
                        patternScope.patternNesting.append((char) ((nesting << 1) + (matchCar ? 1 : 0)));
                        Declaration decl = patternScope.addDeclaration(pattern);
                        decl.setLocation(tr);
                        tr.push(decl);
                        int i4 = i3 << 3;
                        if (matchCar) {
                            i = 7;
                        } else {
                            i = 3;
                        }
                        addInt(program2, i | i4);
                        return;
                    }
                } while (!literalIdentifierEq(pattern, scope1, literal, scope2));
                int i5 = SyntaxTemplate.indexOf(literals2, pattern);
                if (i5 < 0) {
                    i5 = literals2.size();
                    literals2.addElement(pattern);
                }
                addInt(program2, (i5 << 3) | 2);
                return;
            } else if (pattern == LList.Empty) {
                program2.append(8);
                return;
            } else if (pattern instanceof FVector) {
                program2.append(16);
                pattern = LList.makeList((FVector) pattern);
                context = 'V';
            } else {
                int i6 = SyntaxTemplate.indexOf(literals2, pattern);
                if (i6 < 0) {
                    i6 = literals2.size();
                    literals2.addElement(pattern);
                }
                addInt(program2, (i6 << 3) | 2);
                return;
            }
        }
    }

    private static void addInt(StringBuffer sbuf, int val) {
        if (val > 65535) {
            addInt(sbuf, (val << 13) + 1);
        }
        sbuf.append((char) val);
    }

    private static int insertInt(int offset, StringBuffer sbuf, int val) {
        if (val > 65535) {
            offset += insertInt(offset, sbuf, (val << 13) + 1);
        }
        sbuf.insert(offset, (char) val);
        return offset + 1;
    }

    /* access modifiers changed from: package-private */
    public boolean match_car(Pair p, Object[] vars, int start_vars, int pc, SyntaxForm syntax) {
        int pc_start = pc;
        int pc2 = pc + 1;
        char ch = this.program.charAt(pc);
        int value = ch >> 3;
        while ((ch & 7) == 1) {
            ch = this.program.charAt(pc2);
            value = (value << 13) | (ch >> 3);
            pc2++;
        }
        if ((ch & 7) == 7) {
            if (syntax != null && !(p.getCar() instanceof SyntaxForm)) {
                p = Translator.makePair(p, SyntaxForms.fromDatum(p.getCar(), syntax), p.getCdr());
            }
            vars[start_vars + value] = p;
            return true;
        }
        return match(p.getCar(), vars, start_vars, pc_start, syntax);
    }

    public boolean match(Object obj, Object[] vars, int start_vars, int pc, SyntaxForm syntax) {
        int pc2;
        Object id1;
        ScopeExp sc1;
        Object id2;
        ScopeExp sc2;
        boolean z;
        int pairsRequired;
        boolean listValue;
        int value = 0;
        while (true) {
            if (obj instanceof SyntaxForm) {
                syntax = (SyntaxForm) obj;
                obj = syntax.getDatum();
            } else {
                pc2 = pc + 1;
                int ch = this.program.charAt(pc);
                value = (value << 13) | (ch >> 3);
                switch (ch & 7) {
                    case 0:
                        if (ch == 8) {
                            if (obj == LList.Empty) {
                                z = true;
                            } else {
                                z = false;
                            }
                            int i = pc2;
                            return z;
                        } else if (ch == 16) {
                            if (!(obj instanceof FVector)) {
                                int i2 = pc2;
                                return false;
                            }
                            int i3 = pc2;
                            return match(LList.makeList((FVector) obj), vars, start_vars, pc2, syntax);
                        } else if (ch == 24) {
                            int i4 = pc2;
                            return true;
                        } else {
                            throw new Error("unknwon pattern opcode");
                        }
                    case 1:
                        pc = pc2;
                        break;
                    case 2:
                        Object lit = this.literals[value];
                        Translator tr = (Translator) Compilation.getCurrent();
                        if (lit instanceof SyntaxForm) {
                            SyntaxForm sf = (SyntaxForm) lit;
                            id1 = sf.getDatum();
                            sc1 = sf.getScope();
                        } else {
                            id1 = lit;
                            Syntax curSyntax = tr.getCurrentSyntax();
                            sc1 = curSyntax instanceof Macro ? ((Macro) curSyntax).getCapturedScope() : null;
                        }
                        if (obj instanceof SyntaxForm) {
                            SyntaxForm sf2 = (SyntaxForm) obj;
                            id2 = sf2.getDatum();
                            sc2 = sf2.getScope();
                        } else {
                            id2 = obj;
                            sc2 = syntax == null ? tr.currentScope() : syntax.getScope();
                        }
                        int i5 = pc2;
                        return literalIdentifierEq(id1, sc1, id2, sc2);
                    case 3:
                        if (syntax != null) {
                            obj = SyntaxForms.fromDatum(obj, syntax);
                        }
                        vars[start_vars + value] = obj;
                        int i6 = pc2;
                        return true;
                    case 4:
                        if (obj instanceof Pair) {
                            Pair p = (Pair) obj;
                            if (match_car(p, vars, start_vars, pc2, syntax)) {
                                pc = pc2 + value;
                                value = 0;
                                obj = p.getCdr();
                                break;
                            } else {
                                int i7 = pc2;
                                return false;
                            }
                        } else {
                            int i8 = pc2;
                            return false;
                        }
                    case 5:
                        int repeat_pc = pc2;
                        int pc3 = pc2 + value;
                        int pc4 = pc3 + 1;
                        char ch2 = this.program.charAt(pc3);
                        int subvar0 = ch2 >> 3;
                        while ((ch2 & 7) == 1) {
                            ch2 = this.program.charAt(pc4);
                            subvar0 = (subvar0 << 13) | (ch2 >> 3);
                            pc4++;
                        }
                        int subvar02 = subvar0 + start_vars;
                        int pc5 = pc4 + 1;
                        int subvarN = this.program.charAt(pc4) >> 3;
                        while (true) {
                            int pc6 = pc5;
                            if ((ch2 & 7) == 1) {
                                pc5 = pc6 + 1;
                                ch2 = this.program.charAt(pc6);
                                subvarN = (subvarN << 13) | (ch2 >> 3);
                            } else {
                                pc = pc6 + 1;
                                char ch3 = this.program.charAt(pc6);
                                boolean listRequired = true;
                                if (ch3 == 8) {
                                    pairsRequired = 0;
                                } else {
                                    int value2 = ch3 >> 3;
                                    while (true) {
                                        int pc7 = pc;
                                        if ((ch3 & 7) == 1) {
                                            pc = pc7 + 1;
                                            ch3 = this.program.charAt(pc7);
                                            value2 = (value2 << 13) | (ch3 >> 3);
                                        } else {
                                            if ((value2 & 1) != 0) {
                                                listRequired = false;
                                            }
                                            pairsRequired = value2 >> 1;
                                            pc = pc7;
                                        }
                                    }
                                }
                                int pairsValue = Translator.listLength(obj);
                                if (pairsValue >= 0) {
                                    listValue = true;
                                } else {
                                    listValue = false;
                                    pairsValue = -1 - pairsValue;
                                }
                                if (pairsValue < pairsRequired || (listRequired && !listValue)) {
                                    return false;
                                }
                                int repeat_count = pairsValue - pairsRequired;
                                Object[][] arrays = new Object[subvarN][];
                                for (int j = 0; j < subvarN; j++) {
                                    arrays[j] = new Object[repeat_count];
                                }
                                for (int i9 = 0; i9 < repeat_count; i9++) {
                                    while (obj instanceof SyntaxForm) {
                                        syntax = (SyntaxForm) obj;
                                        obj = syntax.getDatum();
                                    }
                                    Pair p2 = (Pair) obj;
                                    if (!match_car(p2, vars, start_vars, repeat_pc, syntax)) {
                                        return false;
                                    }
                                    obj = p2.getCdr();
                                    for (int j2 = 0; j2 < subvarN; j2++) {
                                        arrays[j2][i9] = vars[subvar02 + j2];
                                    }
                                }
                                for (int j3 = 0; j3 < subvarN; j3++) {
                                    vars[subvar02 + j3] = arrays[j3];
                                }
                                value = 0;
                                if (pairsRequired == 0 && listRequired) {
                                    return true;
                                }
                            }
                        }
                        break;
                    case 6:
                        int npairs = value >> 1;
                        Object o = obj;
                        int i10 = 0;
                        while (true) {
                            if (o instanceof SyntaxForm) {
                                o = ((SyntaxForm) o).getDatum();
                            } else if (i10 == npairs) {
                                if ((value & 1) == 0) {
                                    if (o != LList.Empty) {
                                        break;
                                    }
                                    value = 0;
                                    pc = pc2;
                                    break;
                                } else {
                                    if (o instanceof Pair) {
                                        break;
                                    }
                                    value = 0;
                                    pc = pc2;
                                }
                            } else if (o instanceof Pair) {
                                o = ((Pair) o).getCdr();
                                i10++;
                            } else {
                                int i11 = pc2;
                                return false;
                            }
                        }
                    case 8:
                        int i12 = pc2;
                        return obj == LList.Empty;
                    default:
                        disassemble();
                        throw new Error("unrecognized pattern opcode @pc:" + pc2);
                }
            }
        }
        int i13 = pc2;
        return false;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.program);
        out.writeObject(this.literals);
        out.writeInt(this.varCount);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.literals = (Object[]) in.readObject();
        this.program = (String) in.readObject();
        this.varCount = in.readInt();
    }

    public static Object[] allocVars(int varCount2, Object[] outer) {
        Object[] vars = new Object[varCount2];
        if (outer != null) {
            System.arraycopy(outer, 0, vars, 0, outer.length);
        }
        return vars;
    }

    public static boolean literalIdentifierEq(Object id1, ScopeExp sc1, Object id2, ScopeExp sc2) {
        if (id1 != id2 && (id1 == null || id2 == null || !id1.equals(id2))) {
            return false;
        }
        if (sc1 == sc2) {
            return true;
        }
        Declaration d1 = null;
        Declaration d2 = null;
        while (sc1 != null && !(sc1 instanceof ModuleExp)) {
            d1 = sc1.lookup(id1);
            if (d1 != null) {
                break;
            }
            sc1 = sc1.outer;
        }
        while (sc2 != null && !(sc2 instanceof ModuleExp)) {
            d2 = sc2.lookup(id2);
            if (d2 != null) {
                break;
            }
            sc2 = sc2.outer;
        }
        if (d1 != d2) {
            return false;
        }
        return true;
    }

    public static Object[] getLiteralsList(Object list, SyntaxForm syntax, Translator tr) {
        Object wrapped;
        Object savePos = tr.pushPositionOf(list);
        int count = Translator.listLength(list);
        if (count < 0) {
            tr.error('e', "missing or malformed literals list");
            count = 0;
        }
        Object[] literals2 = new Object[(count + 1)];
        for (int i = 1; i <= count; i++) {
            while (list instanceof SyntaxForm) {
                list = ((SyntaxForm) list).getDatum();
            }
            Pair pair = (Pair) list;
            tr.pushPositionOf(pair);
            Object literal = pair.getCar();
            if (literal instanceof SyntaxForm) {
                wrapped = literal;
                literal = ((SyntaxForm) literal).getDatum();
            } else {
                wrapped = literal;
            }
            if (!(literal instanceof Symbol)) {
                tr.error('e', "non-symbol '" + literal + "' in literals list");
            }
            literals2[i] = wrapped;
            list = pair.getCdr();
        }
        tr.popPositionOf(savePos);
        return literals2;
    }

    public void print(Consumer out) {
        out.write("#<syntax-pattern>");
    }
}
