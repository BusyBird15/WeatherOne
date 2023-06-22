package kawa.standard;

import gnu.expr.Keyword;
import gnu.expr.ScopeExp;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.text.Options;
import java.util.Stack;
import kawa.lang.Syntax;
import kawa.lang.SyntaxForm;
import kawa.lang.Translator;

public class with_compile_options extends Syntax {
    public static final with_compile_options with_compile_options = new with_compile_options();

    static {
        with_compile_options.setName("with-compile-options");
    }

    public void scanForm(Pair form, ScopeExp defs, Translator tr) {
        Stack stack = new Stack();
        Object rest = getOptions(form.getCdr(), stack, this, tr);
        if (rest != LList.Empty) {
            if (rest == form.getCdr()) {
                tr.scanBody(rest, defs, false);
                return;
            }
            Pair rest2 = new Pair(stack, tr.scanBody(rest, defs, true));
            tr.currentOptions.popOptionValues(stack);
            tr.formStack.add(Translator.makePair(form, form.getCar(), rest2));
        }
    }

    public static Object getOptions(Object form, Stack stack, Syntax command, Translator tr) {
        boolean seenKey = false;
        Options options = tr.currentOptions;
        SyntaxForm syntax = null;
        while (true) {
            if (!(form instanceof SyntaxForm)) {
                if (form instanceof Pair) {
                    Pair pair = (Pair) form;
                    Object pair_car = Translator.stripSyntax(pair.getCar());
                    if (!(pair_car instanceof Keyword)) {
                        break;
                    }
                    String key = ((Keyword) pair_car).getName();
                    seenKey = true;
                    Object savePos = tr.pushPositionOf(pair);
                    try {
                        Object form2 = pair.getCdr();
                        while (form2 instanceof SyntaxForm) {
                            syntax = (SyntaxForm) form2;
                            form2 = syntax.getDatum();
                        }
                        if (!(form2 instanceof Pair)) {
                            tr.error('e', "keyword " + key + " not followed by value");
                            LList lList = LList.Empty;
                            tr.popPositionOf(savePos);
                            return lList;
                        }
                        Pair pair2 = (Pair) form2;
                        Object value = Translator.stripSyntax(pair2.getCar());
                        form = pair2.getCdr();
                        Object oldValue = options.getLocal(key);
                        if (options.getInfo(key) == null) {
                            tr.error('w', "unknown compile option: " + key);
                        } else {
                            if (value instanceof FString) {
                                value = value.toString();
                            } else if (!(value instanceof Boolean) && !(value instanceof Number)) {
                                value = null;
                                tr.error('e', "invalid literal value for key " + key);
                            }
                            options.set(key, value, tr.getMessages());
                            if (stack != null) {
                                stack.push(key);
                                stack.push(oldValue);
                                stack.push(value);
                            }
                            tr.popPositionOf(savePos);
                        }
                    } finally {
                        tr.popPositionOf(savePos);
                    }
                } else {
                    break;
                }
            } else {
                syntax = (SyntaxForm) form;
                form = syntax.getDatum();
            }
        }
        if (!seenKey) {
            tr.error('e', "no option keyword in " + command.getName());
        }
        return Translator.wrapSyntax(form, syntax);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0041 A[SYNTHETIC, Splitter:B:13:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x002a A[Catch:{ all -> 0x004d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression rewriteForm(gnu.lists.Pair r10, kawa.lang.Translator r11) {
        /*
            r9 = this;
            java.lang.Object r2 = r10.getCdr()
            boolean r7 = r2 instanceof gnu.lists.Pair
            if (r7 == 0) goto L_0x0037
            r3 = r2
            gnu.lists.Pair r3 = (gnu.lists.Pair) r3
            java.lang.Object r7 = r3.getCar()
            boolean r7 = r7 instanceof java.util.Stack
            if (r7 == 0) goto L_0x0037
            java.lang.Object r6 = r3.getCar()
            java.util.Stack r6 = (java.util.Stack) r6
            java.lang.Object r4 = r3.getCdr()
            gnu.text.Options r7 = r11.currentOptions
            r7.pushOptionValues(r6)
        L_0x0022:
            gnu.expr.Expression r5 = r11.rewrite_body(r4)     // Catch:{ all -> 0x004d }
            boolean r7 = r5 instanceof gnu.expr.BeginExp     // Catch:{ all -> 0x004d }
            if (r7 == 0) goto L_0x0041
            r0 = r5
            gnu.expr.BeginExp r0 = (gnu.expr.BeginExp) r0     // Catch:{ all -> 0x004d }
            r1 = r0
        L_0x002e:
            r1.setCompileOptions(r6)     // Catch:{ all -> 0x004d }
            gnu.text.Options r7 = r11.currentOptions
            r7.popOptionValues(r6)
            return r1
        L_0x0037:
            java.util.Stack r6 = new java.util.Stack
            r6.<init>()
            java.lang.Object r4 = getOptions(r2, r6, r9, r11)
            goto L_0x0022
        L_0x0041:
            gnu.expr.BeginExp r1 = new gnu.expr.BeginExp     // Catch:{ all -> 0x004d }
            r7 = 1
            gnu.expr.Expression[] r7 = new gnu.expr.Expression[r7]     // Catch:{ all -> 0x004d }
            r8 = 0
            r7[r8] = r5     // Catch:{ all -> 0x004d }
            r1.<init>(r7)     // Catch:{ all -> 0x004d }
            goto L_0x002e
        L_0x004d:
            r7 = move-exception
            gnu.text.Options r8 = r11.currentOptions
            r8.popOptionValues(r6)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.with_compile_options.rewriteForm(gnu.lists.Pair, kawa.lang.Translator):gnu.expr.Expression");
    }
}
