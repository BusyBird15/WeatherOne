package kawa.standard;

import com.google.appinventor.components.runtime.repackaged.org.json.zip.JSONzip;
import gnu.bytecode.ClassType;
import gnu.bytecode.Field;
import gnu.bytecode.Method;
import gnu.bytecode.Type;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.Language;
import gnu.expr.ModuleExp;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleManager;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.ScopeExp;
import gnu.expr.SetExp;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.reflect.SlotGet;
import gnu.lists.Pair;
import gnu.mapping.InPort;
import gnu.mapping.Procedure;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.text.Path;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class require extends Syntax {
    private static final String SLIB_PREFIX = "gnu.kawa.slib.";
    static Hashtable featureMap = new Hashtable();
    public static final require require = new require();

    static {
        require.setName("require");
        map("generic-write", "gnu.kawa.slib.genwrite");
        map("pretty-print", "gnu.kawa.slib.pp");
        map("pprint-file", "gnu.kawa.slib.ppfile");
        map("printf", "gnu.kawa.slib.printf");
        map("xml", "gnu.kawa.slib.XML");
        map("readtable", "gnu.kawa.slib.readtable");
        map("srfi-10", "gnu.kawa.slib.readtable");
        map("http", "gnu.kawa.servlet.HTTP");
        map("servlets", "gnu.kawa.servlet.servlets");
        map("srfi-1", "gnu.kawa.slib.srfi1");
        map("list-lib", "gnu.kawa.slib.srfi1");
        map("srfi-2", "gnu.kawa.slib.srfi2");
        map("and-let*", "gnu.kawa.slib.srfi2");
        map("srfi-13", "gnu.kawa.slib.srfi13");
        map("string-lib", "gnu.kawa.slib.srfi13");
        map("srfi-34", "gnu.kawa.slib.srfi34");
        map("srfi-35", "gnu.kawa.slib.conditions");
        map("condition", "gnu.kawa.slib.conditions");
        map("conditions", "gnu.kawa.slib.conditions");
        map("srfi-37", "gnu.kawa.slib.srfi37");
        map("args-fold", "gnu.kawa.slib.srfi37");
        map("srfi-64", "gnu.kawa.slib.testing");
        map("testing", "gnu.kawa.slib.testing");
        map("srfi-69", "gnu.kawa.slib.srfi69");
        map("hash-table", "gnu.kawa.slib.srfi69");
        map("basic-hash-tables", "gnu.kawa.slib.srfi69");
        map("srfi-95", "kawa.lib.srfi95");
        map("sorting-and-merging", "kawa.lib.srfi95");
        map("regex", "kawa.lib.kawa.regex");
        map("pregexp", "gnu.kawa.slib.pregexp");
        map("gui", "gnu.kawa.slib.gui");
        map("swing-gui", "gnu.kawa.slib.swing");
        map("android-defs", "gnu.kawa.android.defs");
        map("syntax-utils", "gnu.kawa.slib.syntaxutils");
    }

    static void map(String featureName, String className) {
        featureMap.put(featureName, className);
    }

    public static String mapFeature(String featureName) {
        return (String) featureMap.get(featureName);
    }

    public static Object find(String typeName) {
        return ModuleManager.getInstance().findWithClassName(typeName).getInstance();
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0099  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x014f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean scanForDefinitions(gnu.lists.Pair r17, java.util.Vector r18, gnu.expr.ScopeExp r19, kawa.lang.Translator r20) {
        /*
            r16 = this;
            int r2 = r20.getState()
            r4 = 1
            if (r2 != r4) goto L_0x0015
            r2 = 2
            r0 = r20
            r0.setState(r2)
            r0 = r17
            r1 = r20
            r1.pendingForm = r0
            r2 = 1
        L_0x0014:
            return r2
        L_0x0015:
            java.lang.Object r10 = r17.getCdr()
            gnu.lists.Pair r10 = (gnu.lists.Pair) r10
            java.lang.Object r12 = r10.getCar()
            r15 = 0
            boolean r2 = r12 instanceof gnu.lists.Pair
            if (r2 == 0) goto L_0x00a5
            r13 = r12
            gnu.lists.Pair r13 = (gnu.lists.Pair) r13
            java.lang.Object r2 = r13.getCar()
            java.lang.String r4 = "quote"
            r0 = r20
            boolean r2 = r0.matches(r2, r4)
            if (r2 == 0) goto L_0x00a5
            java.lang.Object r12 = r13.getCdr()
            boolean r2 = r12 instanceof gnu.lists.Pair
            if (r2 == 0) goto L_0x0050
            r13 = r12
            gnu.lists.Pair r13 = (gnu.lists.Pair) r13
            java.lang.Object r2 = r13.getCdr()
            gnu.lists.LList r4 = gnu.lists.LList.Empty
            if (r2 != r4) goto L_0x0050
            java.lang.Object r2 = r13.getCar()
            boolean r2 = r2 instanceof gnu.mapping.Symbol
            if (r2 != 0) goto L_0x005b
        L_0x0050:
            r2 = 101(0x65, float:1.42E-43)
            java.lang.String r4 = "invalid quoted symbol for 'require'"
            r0 = r20
            r0.error(r2, r4)
            r2 = 0
            goto L_0x0014
        L_0x005b:
            java.lang.Object r2 = r13.getCar()
            java.lang.String r2 = r2.toString()
            java.lang.String r12 = mapFeature(r2)
            if (r12 != 0) goto L_0x008f
            r2 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "unknown feature name '"
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.Object r6 = r13.getCar()
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r6 = "' for 'require'"
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r4 = r4.toString()
            r0 = r20
            r0.error(r2, r4)
            r2 = 0
            goto L_0x0014
        L_0x008f:
            java.lang.String r12 = (java.lang.String) r12
            gnu.bytecode.ClassType r15 = gnu.bytecode.ClassType.make(r12)
        L_0x0095:
            boolean r2 = r15 instanceof gnu.bytecode.ClassType
            if (r2 != 0) goto L_0x014f
            r2 = 101(0x65, float:1.42E-43)
            java.lang.String r4 = "invalid specifier for 'require'"
            r0 = r20
            r0.error(r2, r4)
            r2 = 0
            goto L_0x0014
        L_0x00a5:
            boolean r2 = r12 instanceof java.lang.CharSequence
            if (r2 == 0) goto L_0x00e0
            java.lang.String r14 = r12.toString()
            r0 = r19
            gnu.expr.ModuleInfo r3 = lookupModuleFromSourcePath(r14, r0)
            if (r3 != 0) goto L_0x00d2
            r2 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "malformed URL: "
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.StringBuilder r4 = r4.append(r14)
            java.lang.String r4 = r4.toString()
            r0 = r20
            r0.error(r2, r4)
            r2 = 0
            goto L_0x0014
        L_0x00d2:
            r2 = 0
            r4 = 0
            r5 = r18
            r6 = r19
            r7 = r20
            boolean r2 = importDefinitions(r2, r3, r4, r5, r6, r7)
            goto L_0x0014
        L_0x00e0:
            boolean r2 = r12 instanceof gnu.mapping.Symbol
            if (r2 == 0) goto L_0x0095
            r0 = r20
            boolean r2 = r0.selfEvaluatingSymbol(r12)
            if (r2 != 0) goto L_0x0095
            gnu.expr.Language r2 = r20.getLanguage()
            r4 = 0
            r0 = r20
            gnu.expr.Expression r4 = r0.rewrite(r12, r4)
            gnu.bytecode.Type r15 = r2.getTypeFor((gnu.expr.Expression) r4)
            boolean r2 = r15 instanceof gnu.bytecode.ClassType
            if (r2 == 0) goto L_0x0095
            java.lang.Object r2 = r10.getCdr()
            boolean r2 = r2 instanceof gnu.lists.Pair
            if (r2 == 0) goto L_0x0095
            java.lang.Object r2 = r10.getCdr()
            gnu.lists.Pair r2 = (gnu.lists.Pair) r2
            java.lang.Object r12 = r2.getCar()
            boolean r2 = r12 instanceof java.lang.CharSequence
            if (r2 == 0) goto L_0x0095
            java.lang.String r14 = r12.toString()
            r0 = r19
            gnu.expr.ModuleInfo r3 = lookupModuleFromSourcePath(r14, r0)
            if (r3 != 0) goto L_0x013e
            r2 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "malformed URL: "
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.StringBuilder r4 = r4.append(r14)
            java.lang.String r4 = r4.toString()
            r0 = r20
            r0.error(r2, r4)
            r2 = 0
            goto L_0x0014
        L_0x013e:
            java.lang.String r2 = r15.getName()
            r4 = 0
            r5 = r18
            r6 = r19
            r7 = r20
            boolean r2 = importDefinitions(r2, r3, r4, r5, r6, r7)
            goto L_0x0014
        L_0x014f:
            r0 = r15
            gnu.bytecode.ClassType r0 = (gnu.bytecode.ClassType) r0     // Catch:{ Exception -> 0x0165 }
            r2 = r0
            gnu.expr.ModuleInfo r5 = gnu.expr.ModuleInfo.find(r2)     // Catch:{ Exception -> 0x0165 }
            r4 = 0
            r6 = 0
            r7 = r18
            r8 = r19
            r9 = r20
            importDefinitions(r4, r5, r6, r7, r8, r9)
            r2 = 1
            goto L_0x0014
        L_0x0165:
            r11 = move-exception
            r2 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r6 = "unknown class "
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r6 = r15.getName()
            java.lang.StringBuilder r4 = r4.append(r6)
            java.lang.String r4 = r4.toString()
            r0 = r20
            r0.error(r2, r4)
            r2 = 0
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: kawa.standard.require.scanForDefinitions(gnu.lists.Pair, java.util.Vector, gnu.expr.ScopeExp, kawa.lang.Translator):boolean");
    }

    public static ModuleInfo lookupModuleFromSourcePath(String sourceName, ScopeExp defs) {
        ModuleManager manager = ModuleManager.getInstance();
        String baseName = defs.getFileName();
        if (baseName != null) {
            sourceName = Path.valueOf(baseName).resolve(sourceName).toString();
        }
        return manager.findWithSourcePath(sourceName);
    }

    public static boolean importDefinitions(String className, ModuleInfo info, Procedure renamer, Vector forms, ScopeExp defs, Compilation tr) {
        boolean isRunnable;
        Declaration adecl;
        Object obj;
        ModuleManager manager = ModuleManager.getInstance();
        if ((info.getState() & 1) == 0 && info.getCompilation() == null && !info.checkCurrent(manager, System.currentTimeMillis())) {
            SourceMessages messages = tr.getMessages();
            Language language = Language.getDefaultLanguage();
            try {
                InPort fstream = InPort.openFile(info.getSourceAbsPath());
                info.clearClass();
                info.setClassName(className);
                int options = 8;
                if (tr.immediate) {
                    options = 8 | 1;
                }
                Compilation comp = language.parse(fstream, messages, options, info);
                info.setClassName(comp.getModule().classFor(comp).getName());
            } catch (FileNotFoundException ex) {
                tr.error('e', "not found: " + ex.getMessage());
                return false;
            } catch (IOException ex2) {
                tr.error('e', "caught " + ex2);
                return false;
            } catch (SyntaxException ex3) {
                if (ex3.getMessages() == messages) {
                    return false;
                }
                throw new RuntimeException("confussing syntax error: " + ex3);
            }
        }
        if (tr.minfo != null && tr.getState() < 4) {
            tr.minfo.addDependency(info);
            if (!info.loadEager(12) && info.getState() < 6) {
                tr.pushPendingImport(info, defs, forms.size());
                return true;
            }
        }
        ClassType type = info.getClassType();
        String tname = type.getName();
        boolean sharedModule = tr.sharedModuleDefs();
        if (info.getState() < 6) {
            isRunnable = info.getCompilation().makeRunnable();
        } else {
            isRunnable = type.isSubtype(Compilation.typeRunnable);
        }
        Declaration decl = null;
        Expression dofind = Invoke.makeInvokeStatic(ClassType.make("kawa.standard.require"), "find", new Expression[]{new QuoteExp(tname)});
        Field instanceField = null;
        Language language2 = tr.getLanguage();
        dofind.setLine(tr);
        int formsStart = forms.size();
        ModuleExp mod = info.setupModuleExp();
        Vector declPairs = new Vector();
        for (Declaration fdecl = mod.firstDecl(); fdecl != null; fdecl = fdecl.nextDecl()) {
            if (!fdecl.isPrivate()) {
                Symbol aname = (Symbol) fdecl.getSymbol();
                if (renamer != null) {
                    try {
                        obj = renamer.apply1(aname);
                    } catch (Throwable ex4) {
                        obj = ex4;
                    }
                    if (obj != null) {
                        if (!(obj instanceof Symbol)) {
                            tr.error('e', "internal error - import name mapper returned non-symbol: " + obj.getClass().getName());
                        } else {
                            aname = (Symbol) obj;
                        }
                    }
                }
                boolean isStatic = fdecl.getFlag(2048);
                if (!isStatic && decl == null) {
                    decl = new Declaration((Object) SimpleSymbol.valueOf(tname.replace('.', '$') + "$instance"), (Type) type);
                    decl.setPrivate(true);
                    decl.setFlag(1073758208);
                    defs.addDeclaration(decl);
                    decl.noteValue(dofind);
                    SetExp setExp = new SetExp(decl, dofind);
                    setExp.setLine(tr);
                    setExp.setDefining(true);
                    forms.addElement(setExp);
                    formsStart = forms.size();
                    decl.setFlag(536870912);
                    if (isRunnable) {
                        decl.setSimple(false);
                    }
                    decl.setFlag(8192);
                }
                if (fdecl.field == null || !fdecl.field.getName().equals("$instance")) {
                    boolean isImportedInstance = fdecl.field != null && fdecl.field.getName().endsWith("$instance");
                    Declaration old = defs.lookup(aname, language2, language2.getNamespaceOf(fdecl));
                    if (isImportedInstance) {
                        if (old == null) {
                            adecl = defs.addDeclaration((Object) aname);
                            adecl.setFlag(1073758208);
                            adecl.setType(fdecl.getType());
                            adecl.setFlag(8192);
                        }
                    } else if (old == null || old.getFlag(512) || Declaration.followAliases(old) != Declaration.followAliases(fdecl)) {
                        if (old == null || !old.getFlag(66048)) {
                            adecl = defs.addDeclaration((Object) aname);
                            if (old != null) {
                                ScopeExp.duplicateDeclarationError(old, adecl, tr);
                            }
                        } else {
                            old.setFlag(false, 66048);
                            adecl = old;
                        }
                        adecl.setAlias(true);
                        adecl.setIndirectBinding(true);
                    }
                    adecl.setLocation(tr);
                    ReferenceExp referenceExp = new ReferenceExp(fdecl);
                    referenceExp.setContextDecl(decl);
                    if (!isImportedInstance) {
                        referenceExp.setDontDereference(true);
                        if (!sharedModule) {
                            adecl.setPrivate(true);
                        }
                    }
                    adecl.setFlag(JSONzip.int14);
                    if (fdecl.getFlag(32768)) {
                        adecl.setFlag(32768);
                    }
                    if (fdecl.isProcedureDecl()) {
                        adecl.setProcedureDecl(true);
                    }
                    if (isStatic) {
                        adecl.setFlag(2048);
                    }
                    SetExp setExp2 = new SetExp(adecl, (Expression) referenceExp);
                    adecl.setFlag(536870912);
                    setExp2.setDefining(true);
                    if (isImportedInstance) {
                        forms.insertElementAt(setExp2, formsStart);
                        formsStart++;
                    } else {
                        forms.addElement(setExp2);
                    }
                    declPairs.add(adecl);
                    declPairs.add(fdecl);
                    adecl.noteValue(referenceExp);
                    adecl.setFlag(131072);
                    tr.push(adecl);
                } else {
                    instanceField = fdecl.field;
                }
            }
        }
        int ndecls = declPairs.size();
        for (int i = 0; i < ndecls; i += 2) {
            Declaration adecl2 = (Declaration) declPairs.elementAt(i);
            Declaration fdecl2 = (Declaration) declPairs.elementAt(i + 1);
            Expression fval = fdecl2.getValue();
            if (fdecl2.isIndirectBinding() && (fval instanceof ReferenceExp)) {
                ReferenceExp aref = (ReferenceExp) adecl2.getValue();
                Declaration xdecl = ((ReferenceExp) fval).getBinding();
                aref.setBinding(xdecl);
                if (xdecl.needsContext()) {
                    Declaration cdecl = defs.lookup(SimpleSymbol.valueOf(xdecl.field.getDeclaringClass().getName().replace('.', '$') + "$instance"));
                    cdecl.setFlag(1024);
                    aref.setContextDecl(cdecl);
                }
            }
        }
        if (isRunnable) {
            Method run = Compilation.typeRunnable.getDeclaredMethod("run", 0);
            if (decl != null) {
                dofind = new ReferenceExp(decl);
            } else if (instanceField != null) {
                dofind = new ApplyExp((Procedure) SlotGet.staticField, new QuoteExp(type), new QuoteExp("$instance"));
            }
            ApplyExp applyExp = new ApplyExp(run, dofind);
            applyExp.setLine(tr);
            forms.addElement(applyExp);
            ApplyExp applyExp2 = applyExp;
        }
        return true;
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        return null;
    }
}
