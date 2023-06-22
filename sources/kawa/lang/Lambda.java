package kawa.lang;

import gnu.expr.Declaration;
import gnu.expr.ErrorExp;
import gnu.expr.Expression;
import gnu.expr.Keyword;
import gnu.expr.LambdaExp;
import gnu.expr.LangExp;
import gnu.expr.QuoteExp;
import gnu.expr.ScopeExp;
import gnu.kawa.lispexpr.LangObjType;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.mapping.Symbol;

public class Lambda extends Syntax {
    public static final Keyword nameKeyword = Keyword.make("name");
    public Expression defaultDefault = QuoteExp.falseExp;
    public Object keyKeyword;
    public Object optionalKeyword;
    public Object restKeyword;

    public void setKeywords(Object optional, Object rest, Object key) {
        this.optionalKeyword = optional;
        this.restKeyword = rest;
        this.keyKeyword = key;
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        Expression exp = rewrite(form.getCdr(), tr);
        Translator.setLine(exp, (Object) form);
        return exp;
    }

    public Expression rewrite(Object obj, Translator tr) {
        if (!(obj instanceof Pair)) {
            return tr.syntaxError("missing formals in lambda");
        }
        int old_errors = tr.getMessages().getErrorCount();
        LambdaExp lexp = new LambdaExp();
        Pair pair = (Pair) obj;
        Translator.setLine((Expression) lexp, (Object) pair);
        rewrite(lexp, pair.getCar(), pair.getCdr(), tr, (TemplateScope) null);
        if (tr.getMessages().getErrorCount() > old_errors) {
            return new ErrorExp("bad lambda expression");
        }
        return lexp;
    }

    public void rewrite(LambdaExp lexp, Object formals, Object body, Translator tr, TemplateScope templateScopeRest) {
        rewriteFormals(lexp, formals, tr, templateScopeRest);
        if (body instanceof PairWithPosition) {
            lexp.setFile(((PairWithPosition) body).getFileName());
        }
        rewriteBody(lexp, rewriteAttrs(lexp, body, tr), tr);
    }

    public void rewriteFormals(LambdaExp lexp, Object formals, Translator tr, TemplateScope templateScopeRest) {
        if (lexp.getSymbol() == null) {
            String filename = lexp.getFileName();
            int line = lexp.getLineNumber();
            if (filename != null && line > 0) {
                lexp.setSourceLocation(filename, line);
            }
        }
        Object bindings = formals;
        int opt_args = -1;
        int rest_args = -1;
        int key_args = -1;
        while (true) {
            if (bindings instanceof SyntaxForm) {
                bindings = ((SyntaxForm) bindings).getDatum();
            }
            if (!(bindings instanceof Pair)) {
                if (bindings instanceof Symbol) {
                    if (opt_args >= 0 || key_args >= 0 || rest_args >= 0) {
                        tr.syntaxError("dotted rest-arg after " + this.optionalKeyword + ", " + this.restKeyword + ", or " + this.keyKeyword);
                        return;
                    }
                    rest_args = 1;
                } else if (bindings != LList.Empty) {
                    tr.syntaxError("misformed formals in lambda");
                    return;
                }
                if (rest_args > 1) {
                    tr.syntaxError("multiple " + this.restKeyword + " parameters");
                    return;
                }
                if (opt_args < 0) {
                    opt_args = 0;
                }
                if (rest_args < 0) {
                    rest_args = 0;
                }
                if (key_args < 0) {
                    key_args = 0;
                }
                if (rest_args > 0) {
                    lexp.max_args = -1;
                } else {
                    lexp.max_args = lexp.min_args + opt_args + (key_args * 2);
                }
                if (opt_args + key_args > 0) {
                    lexp.defaultArgs = new Expression[(opt_args + key_args)];
                }
                if (key_args > 0) {
                    lexp.keywords = new Keyword[key_args];
                }
                Object bindings2 = formals;
                int opt_args2 = 0;
                int key_args2 = 0;
                Object obj = null;
                while (true) {
                    if (bindings2 instanceof SyntaxForm) {
                        SyntaxForm sf = (SyntaxForm) bindings2;
                        bindings2 = sf.getDatum();
                        templateScopeRest = sf.getScope();
                    }
                    TemplateScope templateScope = templateScopeRest;
                    if (!(bindings2 instanceof Pair)) {
                        if (bindings2 instanceof SyntaxForm) {
                            SyntaxForm sf2 = (SyntaxForm) bindings2;
                            bindings2 = sf2.getDatum();
                            templateScopeRest = sf2.getScope();
                        }
                        if (bindings2 instanceof Symbol) {
                            Declaration decl = new Declaration(bindings2);
                            decl.setType(LangObjType.listType);
                            decl.setFlag(262144);
                            decl.noteValue((Expression) null);
                            addParam(decl, templateScopeRest, lexp, tr);
                            return;
                        }
                        return;
                    }
                    Pair pair = (Pair) bindings2;
                    Object pair_car = pair.getCar();
                    if (pair_car instanceof SyntaxForm) {
                        SyntaxForm sf3 = (SyntaxForm) pair_car;
                        pair_car = sf3.getDatum();
                        templateScope = sf3.getScope();
                    }
                    if (pair_car == this.optionalKeyword || pair_car == this.restKeyword || pair_car == this.keyKeyword) {
                        obj = pair_car;
                    } else {
                        Object savePos = tr.pushPositionOf(pair);
                        Object name = null;
                        Object defaultValue = this.defaultDefault;
                        Pair typeSpecPair = null;
                        if (tr.matches(pair_car, "::")) {
                            tr.syntaxError("'::' must follow parameter name");
                            return;
                        }
                        Object pair_car2 = tr.namespaceResolve(pair_car);
                        if (pair_car2 instanceof Symbol) {
                            name = pair_car2;
                            if (pair.getCdr() instanceof Pair) {
                                Pair p = (Pair) pair.getCdr();
                                if (tr.matches(p.getCar(), "::")) {
                                    if (!(pair.getCdr() instanceof Pair)) {
                                        tr.syntaxError("'::' not followed by a type specifier (for parameter '" + name + "')");
                                        return;
                                    }
                                    Pair p2 = (Pair) p.getCdr();
                                    typeSpecPair = p2;
                                    pair = p2;
                                }
                            }
                        } else if (pair_car2 instanceof Pair) {
                            Pair p3 = (Pair) pair_car2;
                            Object pair_car3 = p3.getCar();
                            if (pair_car3 instanceof SyntaxForm) {
                                SyntaxForm sf4 = (SyntaxForm) pair_car3;
                                pair_car3 = sf4.getDatum();
                                templateScope = sf4.getScope();
                            }
                            Object pair_car4 = tr.namespaceResolve(pair_car3);
                            if ((pair_car4 instanceof Symbol) && (p3.getCdr() instanceof Pair)) {
                                name = pair_car4;
                                Pair p4 = (Pair) p3.getCdr();
                                if (tr.matches(p4.getCar(), "::")) {
                                    if (!(p4.getCdr() instanceof Pair)) {
                                        tr.syntaxError("'::' not followed by a type specifier (for parameter '" + name + "')");
                                        return;
                                    }
                                    Pair p5 = (Pair) p4.getCdr();
                                    typeSpecPair = p5;
                                    if (p5.getCdr() instanceof Pair) {
                                        p4 = (Pair) p5.getCdr();
                                    } else {
                                        if (p5.getCdr() == LList.Empty) {
                                            p4 = null;
                                        } else {
                                            tr.syntaxError("improper list in specifier for parameter '" + name + "')");
                                            return;
                                        }
                                    }
                                }
                                if (!(p4 == null || obj == null)) {
                                    defaultValue = p4.getCar();
                                    if (p4.getCdr() instanceof Pair) {
                                        p4 = (Pair) p4.getCdr();
                                    } else {
                                        if (p4.getCdr() == LList.Empty) {
                                            p4 = null;
                                        } else {
                                            tr.syntaxError("improper list in specifier for parameter '" + name + "')");
                                            return;
                                        }
                                    }
                                }
                                if (p4 != null) {
                                    if (typeSpecPair != null) {
                                        tr.syntaxError("duplicate type specifier for parameter '" + name + '\'');
                                        return;
                                    }
                                    typeSpecPair = p4;
                                    if (p4.getCdr() != LList.Empty) {
                                        tr.syntaxError("junk at end of specifier for parameter '" + name + '\'' + " after type " + p4.getCar());
                                        return;
                                    }
                                }
                            }
                        }
                        if (name == null) {
                            tr.syntaxError("parameter is neither name nor (name :: type) nor (name default): " + pair);
                            return;
                        }
                        if (obj == this.optionalKeyword || obj == this.keyKeyword) {
                            lexp.defaultArgs[opt_args2] = new LangExp(defaultValue);
                            opt_args2++;
                        }
                        if (obj == this.keyKeyword) {
                            int key_args3 = key_args2 + 1;
                            lexp.keywords[key_args2] = Keyword.make(name instanceof Symbol ? ((Symbol) name).getName() : name.toString());
                            key_args2 = key_args3;
                        }
                        Declaration decl2 = new Declaration(name);
                        Translator.setLine(decl2, bindings2);
                        if (typeSpecPair != null) {
                            decl2.setTypeExp(new LangExp(typeSpecPair));
                            decl2.setFlag(8192);
                        } else if (obj == this.restKeyword) {
                            decl2.setType(LangObjType.listType);
                        }
                        decl2.setFlag(262144);
                        decl2.noteValue((Expression) null);
                        addParam(decl2, templateScope, lexp, tr);
                        tr.popPositionOf(savePos);
                    }
                    bindings2 = pair.getCdr();
                }
            } else {
                Pair pair2 = (Pair) bindings;
                Object pair_car5 = pair2.getCar();
                if (pair_car5 instanceof SyntaxForm) {
                    pair_car5 = ((SyntaxForm) pair_car5).getDatum();
                }
                if (pair_car5 != this.optionalKeyword) {
                    if (pair_car5 != this.restKeyword) {
                        if (pair_car5 == this.keyKeyword) {
                            if (key_args >= 0) {
                                tr.syntaxError("multiple " + this.keyKeyword + " in parameter list");
                                return;
                            }
                            key_args = 0;
                        } else if (tr.matches(pair2.getCar(), "::") && (pair2.getCdr() instanceof Pair)) {
                            pair2 = (Pair) pair2.getCdr();
                        } else if (key_args >= 0) {
                            key_args++;
                        } else if (rest_args >= 0) {
                            rest_args++;
                        } else if (opt_args >= 0) {
                            opt_args++;
                        } else {
                            lexp.min_args++;
                        }
                    } else if (rest_args >= 0) {
                        tr.syntaxError("multiple " + this.restKeyword + " in parameter list");
                        return;
                    } else if (key_args >= 0) {
                        tr.syntaxError(this.restKeyword.toString() + " after " + this.keyKeyword);
                        return;
                    } else {
                        rest_args = 0;
                    }
                } else if (opt_args >= 0) {
                    tr.syntaxError("multiple " + this.optionalKeyword + " in parameter list");
                    return;
                } else if (rest_args >= 0 || key_args >= 0) {
                    tr.syntaxError(this.optionalKeyword.toString() + " after " + this.restKeyword + " or " + this.keyKeyword);
                } else {
                    opt_args = 0;
                }
                Object bindings3 = pair2.getCdr();
                bindings = pair2.getCdr();
            }
        }
        tr.syntaxError(this.optionalKeyword.toString() + " after " + this.restKeyword + " or " + this.keyKeyword);
    }

    private static void addParam(Declaration decl, ScopeExp templateScope, LambdaExp lexp, Translator tr) {
        if (templateScope != null) {
            decl = tr.makeRenamedAlias(decl, templateScope);
        }
        lexp.addDeclaration(decl);
        if (templateScope != null) {
            decl.context = templateScope;
        }
    }

    /* JADX WARNING: Failed to insert additional move for type inference */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object rewriteAttrs(gnu.expr.LambdaExp r27, java.lang.Object r28, kawa.lang.Translator r29) {
        /*
            r26 = this;
            r5 = 0
            r7 = 0
            r4 = 0
            r6 = 0
            r18 = 0
        L_0x0006:
            r0 = r28
            boolean r0 = r0 instanceof kawa.lang.SyntaxForm
            r22 = r0
            if (r22 == 0) goto L_0x0017
            r18 = r28
            kawa.lang.SyntaxForm r18 = (kawa.lang.SyntaxForm) r18
            java.lang.Object r28 = r18.getDatum()
            goto L_0x0006
        L_0x0017:
            r0 = r28
            boolean r0 = r0 instanceof gnu.lists.Pair
            r22 = r0
            if (r22 != 0) goto L_0x003d
        L_0x001f:
            r4 = r4 | r6
            if (r4 == 0) goto L_0x0032
            r0 = r27
            gnu.expr.Declaration r0 = r0.nameDecl
            r22 = r0
            long r0 = (long) r4
            r24 = r0
            r0 = r22
            r1 = r24
            r0.setFlag(r1)
        L_0x0032:
            if (r18 == 0) goto L_0x003c
            r0 = r28
            r1 = r18
            java.lang.Object r28 = kawa.lang.SyntaxForms.fromDatumIfNeeded(r0, r1)
        L_0x003c:
            return r28
        L_0x003d:
            r14 = r28
            gnu.lists.Pair r14 = (gnu.lists.Pair) r14
            java.lang.Object r22 = r14.getCar()
            java.lang.Object r9 = kawa.lang.Translator.stripSyntax(r22)
            java.lang.String r22 = "::"
            r0 = r29
            r1 = r22
            boolean r22 = r0.matches(r9, r1)
            if (r22 == 0) goto L_0x006b
            r9 = 0
        L_0x0056:
            r19 = r18
            java.lang.Object r15 = r14.getCdr()
        L_0x005c:
            boolean r0 = r15 instanceof kawa.lang.SyntaxForm
            r22 = r0
            if (r22 == 0) goto L_0x0072
            r19 = r15
            kawa.lang.SyntaxForm r19 = (kawa.lang.SyntaxForm) r19
            java.lang.Object r15 = r19.getDatum()
            goto L_0x005c
        L_0x006b:
            boolean r0 = r9 instanceof gnu.expr.Keyword
            r22 = r0
            if (r22 != 0) goto L_0x0056
            goto L_0x001f
        L_0x0072:
            boolean r0 = r15 instanceof gnu.lists.Pair
            r22 = r0
            if (r22 == 0) goto L_0x001f
            r16 = r15
            gnu.lists.Pair r16 = (gnu.lists.Pair) r16
            if (r9 != 0) goto L_0x00bf
            boolean r22 = r27.isClassMethod()
            if (r22 == 0) goto L_0x00a3
            java.lang.String r22 = "*init*"
            java.lang.String r23 = r27.getName()
            boolean r22 = r22.equals(r23)
            if (r22 == 0) goto L_0x00a3
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "explicit return type for '*init*' method"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
        L_0x009d:
            java.lang.Object r28 = r16.getCdr()
            goto L_0x0006
        L_0x00a3:
            gnu.expr.LangExp r22 = new gnu.expr.LangExp
            r23 = 2
            r0 = r23
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r23 = r0
            r24 = 0
            r23[r24] = r16
            r24 = 1
            r23[r24] = r19
            r22.<init>(r23)
            r0 = r22
            r1 = r27
            r1.body = r0
            goto L_0x009d
        L_0x00bf:
            gnu.expr.Keyword r22 = kawa.standard.object.accessKeyword
            r0 = r22
            if (r9 != r0) goto L_0x018e
            r0 = r29
            r1 = r16
            r2 = r19
            gnu.expr.Expression r8 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            boolean r0 = r8 instanceof gnu.expr.QuoteExp
            r22 = r0
            if (r22 == 0) goto L_0x00e7
            gnu.expr.QuoteExp r8 = (gnu.expr.QuoteExp) r8
            java.lang.Object r10 = r8.getValue()
            boolean r0 = r10 instanceof gnu.mapping.SimpleSymbol
            r22 = r0
            if (r22 != 0) goto L_0x00f5
            boolean r0 = r10 instanceof java.lang.CharSequence
            r22 = r0
            if (r22 != 0) goto L_0x00f5
        L_0x00e7:
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "access: value not a constant symbol or string"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x009d
        L_0x00f5:
            r0 = r27
            gnu.expr.Declaration r0 = r0.nameDecl
            r22 = r0
            if (r22 != 0) goto L_0x010b
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "access: not allowed for anonymous function"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x009d
        L_0x010b:
            java.lang.String r21 = r10.toString()
            java.lang.String r22 = "private"
            r0 = r22
            r1 = r21
            boolean r22 = r0.equals(r1)
            if (r22 == 0) goto L_0x0153
            r4 = 16777216(0x1000000, float:2.3509887E-38)
        L_0x011d:
            if (r5 == 0) goto L_0x014f
            if (r21 == 0) goto L_0x014f
            r22 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r23 = new java.lang.StringBuilder
            r23.<init>()
            java.lang.String r24 = "duplicate access specifiers - "
            java.lang.StringBuilder r23 = r23.append(r24)
            r0 = r23
            java.lang.StringBuilder r23 = r0.append(r5)
            java.lang.String r24 = " and "
            java.lang.StringBuilder r23 = r23.append(r24)
            r0 = r23
            r1 = r21
            java.lang.StringBuilder r23 = r0.append(r1)
            java.lang.String r23 = r23.toString()
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
        L_0x014f:
            r5 = r21
            goto L_0x009d
        L_0x0153:
            java.lang.String r22 = "protected"
            r0 = r22
            r1 = r21
            boolean r22 = r0.equals(r1)
            if (r22 == 0) goto L_0x0162
            r4 = 33554432(0x2000000, float:9.403955E-38)
            goto L_0x011d
        L_0x0162:
            java.lang.String r22 = "public"
            r0 = r22
            r1 = r21
            boolean r22 = r0.equals(r1)
            if (r22 == 0) goto L_0x0171
            r4 = 67108864(0x4000000, float:1.5046328E-36)
            goto L_0x011d
        L_0x0171:
            java.lang.String r22 = "package"
            r0 = r22
            r1 = r21
            boolean r22 = r0.equals(r1)
            if (r22 == 0) goto L_0x0180
            r4 = 134217728(0x8000000, float:3.85186E-34)
            goto L_0x011d
        L_0x0180:
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "unknown access specifier"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x011d
        L_0x018e:
            gnu.expr.Keyword r22 = kawa.standard.object.allocationKeyword
            r0 = r22
            if (r9 != r0) goto L_0x024d
            r0 = r29
            r1 = r16
            r2 = r19
            gnu.expr.Expression r8 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            boolean r0 = r8 instanceof gnu.expr.QuoteExp
            r22 = r0
            if (r22 == 0) goto L_0x01b6
            gnu.expr.QuoteExp r8 = (gnu.expr.QuoteExp) r8
            java.lang.Object r10 = r8.getValue()
            boolean r0 = r10 instanceof gnu.mapping.SimpleSymbol
            r22 = r0
            if (r22 != 0) goto L_0x01c5
            boolean r0 = r10 instanceof java.lang.CharSequence
            r22 = r0
            if (r22 != 0) goto L_0x01c5
        L_0x01b6:
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "allocation: value not a constant symbol or string"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x009d
        L_0x01c5:
            r0 = r27
            gnu.expr.Declaration r0 = r0.nameDecl
            r22 = r0
            if (r22 != 0) goto L_0x01dc
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "allocation: not allowed for anonymous function"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x009d
        L_0x01dc:
            java.lang.String r21 = r10.toString()
            java.lang.String r22 = "class"
            r0 = r22
            r1 = r21
            boolean r22 = r0.equals(r1)
            if (r22 != 0) goto L_0x01f8
            java.lang.String r22 = "static"
            r0 = r22
            r1 = r21
            boolean r22 = r0.equals(r1)
            if (r22 == 0) goto L_0x0230
        L_0x01f8:
            r6 = 2048(0x800, float:2.87E-42)
        L_0x01fa:
            if (r7 == 0) goto L_0x022c
            if (r21 == 0) goto L_0x022c
            r22 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r23 = new java.lang.StringBuilder
            r23.<init>()
            java.lang.String r24 = "duplicate allocation specifiers - "
            java.lang.StringBuilder r23 = r23.append(r24)
            r0 = r23
            java.lang.StringBuilder r23 = r0.append(r7)
            java.lang.String r24 = " and "
            java.lang.StringBuilder r23 = r23.append(r24)
            r0 = r23
            r1 = r21
            java.lang.StringBuilder r23 = r0.append(r1)
            java.lang.String r23 = r23.toString()
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
        L_0x022c:
            r7 = r21
            goto L_0x009d
        L_0x0230:
            java.lang.String r22 = "instance"
            r0 = r22
            r1 = r21
            boolean r22 = r0.equals(r1)
            if (r22 == 0) goto L_0x023f
            r6 = 4096(0x1000, float:5.74E-42)
            goto L_0x01fa
        L_0x023f:
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "unknown allocation specifier"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x01fa
        L_0x024d:
            gnu.expr.Keyword r22 = kawa.standard.object.throwsKeyword
            r0 = r22
            if (r9 != r0) goto L_0x02a9
            java.lang.Object r10 = r16.getCar()
            int r11 = kawa.lang.Translator.listLength(r10)
            if (r11 >= 0) goto L_0x026c
            r22 = 101(0x65, float:1.42E-43)
            java.lang.String r23 = "throws: not followed by a list"
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x009d
        L_0x026c:
            gnu.expr.Expression[] r12 = new gnu.expr.Expression[r11]
            r20 = r19
            r13 = 0
        L_0x0271:
            if (r13 >= r11) goto L_0x02a2
        L_0x0273:
            boolean r0 = r10 instanceof kawa.lang.SyntaxForm
            r22 = r0
            if (r22 == 0) goto L_0x0282
            r20 = r10
            kawa.lang.SyntaxForm r20 = (kawa.lang.SyntaxForm) r20
            java.lang.Object r10 = r20.getDatum()
            goto L_0x0273
        L_0x0282:
            r17 = r10
            gnu.lists.Pair r17 = (gnu.lists.Pair) r17
            r0 = r29
            r1 = r17
            r2 = r20
            gnu.expr.Expression r22 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            r12[r13] = r22
            r22 = r12[r13]
            r0 = r22
            r1 = r17
            kawa.lang.Translator.setLine((gnu.expr.Expression) r0, (java.lang.Object) r1)
            java.lang.Object r10 = r17.getCdr()
            int r13 = r13 + 1
            goto L_0x0271
        L_0x02a2:
            r0 = r27
            r0.setExceptions(r12)
            goto L_0x009d
        L_0x02a9:
            gnu.expr.Keyword r22 = nameKeyword
            r0 = r22
            if (r9 != r0) goto L_0x02d2
            r0 = r29
            r1 = r16
            r2 = r19
            gnu.expr.Expression r8 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            boolean r0 = r8 instanceof gnu.expr.QuoteExp
            r22 = r0
            if (r22 == 0) goto L_0x009d
            gnu.expr.QuoteExp r8 = (gnu.expr.QuoteExp) r8
            java.lang.Object r22 = r8.getValue()
            java.lang.String r22 = r22.toString()
            r0 = r27
            r1 = r22
            r0.setName(r1)
            goto L_0x009d
        L_0x02d2:
            r22 = 119(0x77, float:1.67E-43)
            java.lang.StringBuilder r23 = new java.lang.StringBuilder
            r23.<init>()
            java.lang.String r24 = "unknown procedure property "
            java.lang.StringBuilder r23 = r23.append(r24)
            r0 = r23
            java.lang.StringBuilder r23 = r0.append(r9)
            java.lang.String r23 = r23.toString()
            r0 = r29
            r1 = r22
            r2 = r23
            r0.error(r1, r2)
            goto L_0x009d
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Lambda.rewriteAttrs(gnu.expr.LambdaExp, java.lang.Object, kawa.lang.Translator):java.lang.Object");
    }

    public Object skipAttrs(LambdaExp lexp, Object body, Translator tr) {
        while (body instanceof Pair) {
            Pair pair = (Pair) body;
            if (!(pair.getCdr() instanceof Pair)) {
                break;
            }
            Object attrName = pair.getCar();
            if (!tr.matches(attrName, "::")) {
                if (!(attrName instanceof Keyword)) {
                    break;
                }
            }
            body = ((Pair) pair.getCdr()).getCdr();
        }
        return body;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x01cc, code lost:
        if ((r22 instanceof java.lang.Class) == false) goto L_0x0230;
     */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x020f  */
    /* JADX WARNING: Removed duplicated region for block: B:65:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void rewriteBody(gnu.expr.LambdaExp r27, java.lang.Object r28, kawa.lang.Translator r29) {
        /*
            r26 = this;
            r11 = 0
            r0 = r29
            gnu.expr.LambdaExp r0 = r0.curMethodLambda
            r23 = r0
            if (r23 != 0) goto L_0x0023
            r0 = r27
            gnu.expr.Declaration r0 = r0.nameDecl
            r23 = r0
            if (r23 == 0) goto L_0x0023
            gnu.expr.ModuleExp r23 = r29.getModule()
            r24 = 131072(0x20000, float:1.83671E-40)
            boolean r23 = r23.getFlag(r24)
            if (r23 == 0) goto L_0x0023
            r0 = r27
            r1 = r29
            r1.curMethodLambda = r0
        L_0x0023:
            gnu.expr.ScopeExp r6 = r29.currentScope()
            r0 = r29
            r1 = r27
            r0.pushScope(r1)
            r15 = 0
            r0 = r27
            gnu.expr.Keyword[] r0 = r0.keywords
            r23 = r0
            if (r23 != 0) goto L_0x00e1
            r8 = 0
        L_0x0038:
            r0 = r27
            gnu.expr.Expression[] r0 = r0.defaultArgs
            r23 = r0
            if (r23 != 0) goto L_0x00ec
            r12 = 0
        L_0x0041:
            r4 = 0
            r13 = 0
            gnu.expr.Declaration r5 = r27.firstDecl()
        L_0x0047:
            if (r5 == 0) goto L_0x00fb
            boolean r23 = r5.isAlias()
            if (r23 == 0) goto L_0x0068
            gnu.expr.ReferenceExp r23 = kawa.lang.Translator.getOriginalRef(r5)
            gnu.expr.Declaration r14 = r23.getBinding()
            r0 = r27
            r0.replaceFollowing(r15, r14)
            r0 = r27
            r14.context = r0
            r0 = r29
            r0.pushRenamedAlias(r5)
            int r11 = r11 + 1
            r5 = r14
        L_0x0068:
            gnu.expr.Expression r19 = r5.getTypeExp()
            r0 = r19
            boolean r0 = r0 instanceof gnu.expr.LangExp
            r23 = r0
            if (r23 == 0) goto L_0x0089
            gnu.expr.LangExp r19 = (gnu.expr.LangExp) r19
            java.lang.Object r21 = r19.getLangValue()
            gnu.lists.Pair r21 = (gnu.lists.Pair) r21
            r0 = r29
            r1 = r21
            gnu.bytecode.Type r23 = r0.exp2Type(r1)
            r0 = r23
            r5.setType(r0)
        L_0x0089:
            r15 = r5
            r0 = r27
            int r0 = r0.min_args
            r23 = r0
            r0 = r23
            if (r4 < r0) goto L_0x00ce
            r0 = r27
            int r0 = r0.min_args
            r23 = r0
            int r23 = r23 + r12
            r0 = r23
            if (r4 < r0) goto L_0x00b4
            r0 = r27
            int r0 = r0.max_args
            r23 = r0
            if (r23 >= 0) goto L_0x00b4
            r0 = r27
            int r0 = r0.min_args
            r23 = r0
            int r23 = r23 + r12
            r0 = r23
            if (r4 == r0) goto L_0x00ce
        L_0x00b4:
            r0 = r27
            gnu.expr.Expression[] r0 = r0.defaultArgs
            r23 = r0
            r0 = r27
            gnu.expr.Expression[] r0 = r0.defaultArgs
            r24 = r0
            r24 = r24[r13]
            r0 = r29
            r1 = r24
            gnu.expr.Expression r24 = r0.rewrite(r1)
            r23[r13] = r24
            int r13 = r13 + 1
        L_0x00ce:
            int r4 = r4 + 1
            r0 = r29
            gnu.expr.NameLookup r0 = r0.lexical
            r23 = r0
            r0 = r23
            r0.push((gnu.expr.Declaration) r5)
            gnu.expr.Declaration r5 = r5.nextDecl()
            goto L_0x0047
        L_0x00e1:
            r0 = r27
            gnu.expr.Keyword[] r0 = r0.keywords
            r23 = r0
            r0 = r23
            int r8 = r0.length
            goto L_0x0038
        L_0x00ec:
            r0 = r27
            gnu.expr.Expression[] r0 = r0.defaultArgs
            r23 = r0
            r0 = r23
            int r0 = r0.length
            r23 = r0
            int r12 = r23 - r8
            goto L_0x0041
        L_0x00fb:
            boolean r23 = r27.isClassMethod()
            if (r23 == 0) goto L_0x0121
            r0 = r27
            gnu.expr.Declaration r0 = r0.nameDecl
            r23 = r0
            r24 = 2048(0x800, double:1.0118E-320)
            boolean r23 = r23.getFlag(r24)
            if (r23 != 0) goto L_0x0121
            r23 = 0
            gnu.expr.Declaration r24 = new gnu.expr.Declaration
            java.lang.String r25 = gnu.expr.ThisExp.THIS_NAME
            r24.<init>((java.lang.Object) r25)
            r0 = r27
            r1 = r23
            r2 = r24
            r0.add(r1, r2)
        L_0x0121:
            r0 = r29
            gnu.expr.LambdaExp r0 = r0.curLambda
            r18 = r0
            r0 = r27
            r1 = r29
            r1.curLambda = r0
            r0 = r27
            gnu.bytecode.Type r0 = r0.returnType
            r17 = r0
            r0 = r27
            gnu.expr.Expression r0 = r0.body
            r23 = r0
            r0 = r23
            boolean r0 = r0 instanceof gnu.expr.LangExp
            r23 = r0
            if (r23 == 0) goto L_0x0175
            r0 = r27
            gnu.expr.Expression r0 = r0.body
            r23 = r0
            gnu.expr.LangExp r23 = (gnu.expr.LangExp) r23
            java.lang.Object r23 = r23.getLangValue()
            java.lang.Object[] r23 = (java.lang.Object[]) r23
            r20 = r23
            java.lang.Object[] r20 = (java.lang.Object[]) r20
            r23 = 0
            r23 = r20[r23]
            gnu.lists.Pair r23 = (gnu.lists.Pair) r23
            r24 = 1
            r24 = r20[r24]
            kawa.lang.SyntaxForm r24 = (kawa.lang.SyntaxForm) r24
            r0 = r29
            r1 = r23
            r2 = r24
            gnu.expr.Expression r19 = r0.rewrite_car((gnu.lists.Pair) r1, (kawa.lang.SyntaxForm) r2)
            gnu.expr.Language r23 = r29.getLanguage()
            r0 = r23
            r1 = r19
            gnu.bytecode.Type r17 = r0.getTypeFor((gnu.expr.Expression) r1)
        L_0x0175:
            r0 = r29
            r1 = r28
            gnu.expr.Expression r23 = r0.rewrite_body(r1)
            r0 = r23
            r1 = r27
            r1.body = r0
            r0 = r18
            r1 = r29
            r1.curLambda = r0
            r0 = r27
            gnu.expr.Expression r0 = r0.body
            r23 = r0
            r0 = r23
            boolean r0 = r0 instanceof gnu.expr.BeginExp
            r23 = r0
            if (r23 == 0) goto L_0x0230
            r0 = r27
            gnu.expr.Expression r0 = r0.body
            r23 = r0
            gnu.expr.BeginExp r23 = (gnu.expr.BeginExp) r23
            gnu.expr.Expression[] r7 = r23.getExpressions()
            int r9 = r7.length
            r23 = 1
            r0 = r23
            if (r9 <= r0) goto L_0x0230
            r23 = 0
            r23 = r7[r23]
            r0 = r23
            boolean r0 = r0 instanceof gnu.expr.ReferenceExp
            r23 = r0
            if (r23 != 0) goto L_0x01ce
            r23 = 0
            r23 = r7[r23]
            java.lang.Object r22 = r23.valueIfConstant()
            r0 = r22
            boolean r0 = r0 instanceof gnu.bytecode.Type
            r23 = r0
            if (r23 != 0) goto L_0x01ce
            r0 = r22
            boolean r0 = r0 instanceof java.lang.Class
            r23 = r0
            if (r23 == 0) goto L_0x0230
        L_0x01ce:
            r23 = 0
            r16 = r7[r23]
            int r9 = r9 + -1
            r23 = 1
            r0 = r23
            if (r9 != r0) goto L_0x0218
            r23 = 1
            r23 = r7[r23]
            r0 = r23
            r1 = r27
            r1.body = r0
        L_0x01e4:
            gnu.expr.Language r23 = r29.getLanguage()
            r0 = r27
            r1 = r16
            r2 = r23
            r0.setCoercedReturnValue(r1, r2)
        L_0x01f1:
            r0 = r29
            r1 = r27
            r0.pop(r1)
            r27.countDecls()
            r0 = r29
            r0.popRenamedAlias(r11)
            r27.countDecls()
            r0 = r29
            gnu.expr.LambdaExp r0 = r0.curMethodLambda
            r23 = r0
            r0 = r23
            r1 = r27
            if (r0 != r1) goto L_0x0217
            r23 = 0
            r0 = r23
            r1 = r29
            r1.curMethodLambda = r0
        L_0x0217:
            return
        L_0x0218:
            gnu.expr.Expression[] r10 = new gnu.expr.Expression[r9]
            r23 = 1
            r24 = 0
            r0 = r23
            r1 = r24
            java.lang.System.arraycopy(r7, r0, r10, r1, r9)
            gnu.expr.Expression r23 = gnu.expr.BeginExp.canonicalize((gnu.expr.Expression[]) r10)
            r0 = r23
            r1 = r27
            r1.body = r0
            goto L_0x01e4
        L_0x0230:
            r0 = r27
            r1 = r17
            r0.setCoercedReturnType(r1)
            goto L_0x01f1
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.lang.Lambda.rewriteBody(gnu.expr.LambdaExp, java.lang.Object, kawa.lang.Translator):void");
    }

    public void print(Consumer out) {
        out.write("#<builtin lambda>");
    }
}
