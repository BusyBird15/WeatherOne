package kawa.standard;

import com.google.appinventor.components.runtime.repackaged.org.json.zip.JSONzip;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.Language;
import gnu.expr.QuoteExp;
import gnu.expr.ScopeExp;
import gnu.kawa.lispexpr.LispReader;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.Sequence;
import gnu.mapping.InPort;
import gnu.mapping.Symbol;
import gnu.text.SyntaxException;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import kawa.lang.AutoloadProcedure;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class define_autoload extends Syntax {
    public static final define_autoload define_autoload = new define_autoload("define-autoload", false);
    public static final define_autoload define_autoloads_from_file = new define_autoload("define-autoloads-from-file", true);
    boolean fromFile;

    public define_autoload(String name, boolean fromFile2) {
        super(name);
        this.fromFile = fromFile2;
    }

    public boolean scanForDefinitions(Pair st, Vector forms, ScopeExp defs, Translator tr) {
        if (!(st.getCdr() instanceof Pair)) {
            return super.scanForDefinitions(st, forms, defs, tr);
        }
        Pair p = (Pair) st.getCdr();
        if (this.fromFile) {
            while (p.getCar() instanceof CharSequence) {
                if (scanFile(p.getCar().toString(), defs, tr)) {
                    Object rest = p.getCdr();
                    if (rest != LList.Empty) {
                        if (!(rest instanceof Pair)) {
                            break;
                        }
                        p = (Pair) p.getCdr();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            tr.syntaxError("invalid syntax for define-autoloads-from-file");
            return false;
        }
        Object names = p.getCar();
        if (p.getCdr() instanceof Pair) {
            return process(names, ((Pair) p.getCdr()).getCar(), forms, defs, tr);
        }
        tr.syntaxError("invalid syntax for define-autoload");
        return false;
    }

    public boolean scanFile(String filespec, ScopeExp defs, Translator tr) {
        if (filespec.endsWith(".el")) {
        }
        File file = new File(filespec);
        if (!file.isAbsolute()) {
            file = new File(new File(tr.getFileName()).getParent(), filespec);
        }
        String filename = file.getPath();
        int dot = filename.lastIndexOf(46);
        if (dot >= 0) {
            String extension = filename.substring(dot);
            Language language = Language.getInstance(extension);
            if (language == null) {
                tr.syntaxError("unknown extension for " + filename);
                return true;
            }
            String prefix = tr.classPrefix;
            String cname = filespec.substring(0, filespec.length() - extension.length());
            while (cname.startsWith("../")) {
                int i = prefix.lastIndexOf(46, prefix.length() - 2);
                if (i < 0) {
                    tr.syntaxError("cannot use relative filename \"" + filespec + "\" with simple prefix \"" + prefix + "\"");
                    return false;
                }
                prefix = prefix.substring(0, i + 1);
                cname = cname.substring(3);
            }
            try {
                findAutoloadComments((LispReader) language.getLexer(InPort.openFile(filename), tr.getMessages()), (prefix + cname).replace('/', '.'), defs, tr);
            } catch (Exception ex) {
                tr.syntaxError("error reading " + filename + ": " + ex);
                return true;
            }
        }
        return true;
    }

    public static void findAutoloadComments(LispReader in, String filename, ScopeExp defs, Translator tr) throws IOException, SyntaxException {
        boolean lineStart = true;
        int magicLength = ";;;###autoload".length();
        while (true) {
            int ch = in.peek();
            if (ch >= 0) {
                if (ch == 10 || ch == 13) {
                    in.read();
                    lineStart = true;
                } else {
                    if (lineStart && ch == 59) {
                        int i = 0;
                        while (true) {
                            if (i != magicLength) {
                                ch = in.read();
                                if (ch < 0) {
                                    return;
                                }
                                if (ch == 10 || ch == 13) {
                                    lineStart = true;
                                } else if (i >= 0) {
                                    int i2 = i + 1;
                                    if (ch == ";;;###autoload".charAt(i)) {
                                        i = i2;
                                    } else {
                                        i = -1;
                                    }
                                }
                            } else if (i > 0) {
                                Object form = in.readObject();
                                if (form instanceof Pair) {
                                    Pair pair = (Pair) form;
                                    AutoloadProcedure autoloadProcedure = null;
                                    String name = null;
                                    Object car = pair.getCar();
                                    if ((car instanceof String ? car.toString() : car instanceof Symbol ? ((Symbol) car).getName() : null) == "defun") {
                                        name = ((Pair) pair.getCdr()).getCar().toString();
                                        autoloadProcedure = new AutoloadProcedure(name, filename, tr.getLanguage());
                                    } else {
                                        tr.error('w', "unsupported ;;;###autoload followed by: " + pair.getCar());
                                    }
                                    if (autoloadProcedure != null) {
                                        Declaration decl = defs.getDefine(name, 'w', tr);
                                        Expression ex = new QuoteExp(autoloadProcedure);
                                        decl.setFlag(JSONzip.int14);
                                        decl.noteValue(ex);
                                        decl.setProcedureDecl(true);
                                        decl.setType(Compilation.typeProcedure);
                                    }
                                }
                                lineStart = false;
                            }
                        }
                        lineStart = true;
                    }
                    lineStart = false;
                    in.skip();
                    if (ch == 35 && in.peek() == 124) {
                        in.skip();
                        in.readNestedComment('#', '|');
                    } else if (!Character.isWhitespace((char) ch) && in.readObject(ch) == Sequence.eofValue) {
                        return;
                    }
                }
            } else {
                return;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        r2 = (java.lang.String) r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean process(java.lang.Object r10, java.lang.Object r11, java.util.Vector r12, gnu.expr.ScopeExp r13, kawa.lang.Translator r14) {
        /*
            r8 = 0
            r7 = 1
            boolean r9 = r10 instanceof gnu.lists.Pair
            if (r9 == 0) goto L_0x0020
            r5 = r10
            gnu.lists.Pair r5 = (gnu.lists.Pair) r5
            java.lang.Object r9 = r5.getCar()
            boolean r9 = process(r9, r11, r12, r13, r14)
            if (r9 == 0) goto L_0x001e
            java.lang.Object r9 = r5.getCdr()
            boolean r9 = process(r9, r11, r12, r13, r14)
            if (r9 == 0) goto L_0x001e
        L_0x001d:
            return r7
        L_0x001e:
            r7 = r8
            goto L_0x001d
        L_0x0020:
            gnu.lists.LList r9 = gnu.lists.LList.Empty
            if (r10 == r9) goto L_0x001d
            boolean r9 = r10 instanceof java.lang.String
            if (r9 != 0) goto L_0x002c
            boolean r9 = r10 instanceof gnu.mapping.Symbol
            if (r9 == 0) goto L_0x0077
        L_0x002c:
            java.lang.String r4 = r10.toString()
            r9 = 119(0x77, float:1.67E-43)
            gnu.expr.Declaration r0 = r13.getDefine(r4, r9, r14)
            boolean r9 = r11 instanceof java.lang.String
            if (r9 == 0) goto L_0x005c
            r2 = r11
            java.lang.String r2 = (java.lang.String) r2
            int r3 = r2.length()
            r9 = 2
            if (r3 <= r9) goto L_0x005c
            char r8 = r2.charAt(r8)
            r9 = 60
            if (r8 != r9) goto L_0x005c
            int r8 = r3 + -1
            char r8 = r2.charAt(r8)
            r9 = 62
            if (r8 != r9) goto L_0x005c
            int r8 = r3 + -1
            java.lang.String r11 = r2.substring(r7, r8)
        L_0x005c:
            kawa.lang.AutoloadProcedure r6 = new kawa.lang.AutoloadProcedure
            java.lang.String r8 = r11.toString()
            gnu.expr.Language r9 = r14.getLanguage()
            r6.<init>(r4, r8, r9)
            gnu.expr.QuoteExp r1 = new gnu.expr.QuoteExp
            r1.<init>(r6)
            r8 = 16384(0x4000, double:8.0948E-320)
            r0.setFlag(r8)
            r0.noteValue(r1)
            goto L_0x001d
        L_0x0077:
            r7 = r8
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.define_autoload.process(java.lang.Object, java.lang.Object, java.util.Vector, gnu.expr.ScopeExp, kawa.lang.Translator):boolean");
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        return null;
    }
}
