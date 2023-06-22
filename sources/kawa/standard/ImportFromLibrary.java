package kawa.standard;

import com.google.appinventor.components.runtime.Component;
import gnu.expr.Compilation;
import gnu.expr.Expression;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleManager;
import gnu.expr.ScopeExp;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Procedure;
import java.util.Vector;
import kawa.lang.Syntax;
import kawa.lang.Translator;

public class ImportFromLibrary extends Syntax {
    private static final String BUILTIN = "";
    private static final String MISSING = null;
    static final String[][] SRFI97Map = {new String[]{Component.TYPEFACE_SANSSERIF, "lists", "gnu.kawa.slib.srfi1"}, new String[]{Component.TYPEFACE_SERIF, "and-let*", "gnu.kawa.slib.srfi2"}, new String[]{"5", "let", MISSING}, new String[]{"6", "basic-string-ports", ""}, new String[]{"8", "receive", ""}, new String[]{"9", "records", ""}, new String[]{"11", "let-values", ""}, new String[]{"13", "strings", "gnu.kawa.slib.srfi13"}, new String[]{"14", "char-sets", MISSING}, new String[]{"16", "case-lambda", ""}, new String[]{"17", "generalized-set!", ""}, new String[]{"18", "multithreading", MISSING}, new String[]{"19", "time", MISSING}, new String[]{"21", "real-time-multithreading", MISSING}, new String[]{"23", "error", ""}, new String[]{"25", "multi-dimensional-arrays", ""}, new String[]{"26", "cut", ""}, new String[]{"27", "random-bits", MISSING}, new String[]{"28", "basic-format-strings", ""}, new String[]{"29", "localization", MISSING}, new String[]{"31", "rec", MISSING}, new String[]{"38", "with-shared-structure", MISSING}, new String[]{"39", "parameters", ""}, new String[]{"41", "streams", MISSING}, new String[]{"42", "eager-comprehensions", MISSING}, new String[]{"43", "vectors", MISSING}, new String[]{"44", "collections", MISSING}, new String[]{"45", "lazy", MISSING}, new String[]{"46", "syntax-rules", MISSING}, new String[]{"47", "arrays", MISSING}, new String[]{"48", "intermediate-format-strings", MISSING}, new String[]{"51", "rest-values", MISSING}, new String[]{"54", "cat", MISSING}, new String[]{"57", "records", MISSING}, new String[]{"59", "vicinities", MISSING}, new String[]{"60", "integer-bits", MISSING}, new String[]{"61", "cond", MISSING}, new String[]{"63", "arrays", MISSING}, new String[]{"64", "testing", "gnu.kawa.slib.testing"}, new String[]{"66", "octet-vectors", MISSING}, new String[]{"67", "compare-procedures", MISSING}, new String[]{"69", "basic-hash-tables", "gnu.kawa.slib.srfi69"}, new String[]{"71", "let", MISSING}, new String[]{"74", "blobs", MISSING}, new String[]{"78", "lightweight-testing", MISSING}, new String[]{"86", "mu-and-nu", MISSING}, new String[]{"87", "case", MISSING}, new String[]{"95", "sorting-and-merging", "kawa.lib.srfi95"}};
    public static final ImportFromLibrary instance = new ImportFromLibrary();
    public String[] classPrefixPath = {"", "kawa.lib."};

    public boolean scanForDefinitions(Pair st, Vector forms, ScopeExp defs, Translator tr) {
        String srfiName;
        Procedure mapper = null;
        Object args = st.getCdr();
        if (!(args instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) args;
        Object libref = pair.getCar();
        if (LList.listLength(libref, false) <= 0) {
            tr.error('e', "expected <library reference> which must be a list");
            return false;
        }
        Object rest = pair.getCdr();
        if ((rest instanceof Pair) && (((Pair) rest).getCar() instanceof Procedure)) {
            mapper = (Procedure) ((Pair) rest).getCar();
        }
        Object versionSpec = null;
        String sourcePath = null;
        StringBuffer sbuf = new StringBuffer();
        while (libref instanceof Pair) {
            Pair pair2 = (Pair) libref;
            Object car = pair2.getCar();
            Object cdr = pair2.getCdr();
            if (car instanceof Pair) {
                if (versionSpec != null) {
                    tr.error('e', "duplicate version reference - was " + versionSpec);
                }
                versionSpec = car;
                System.err.println("import version " + car);
            } else if (car instanceof String) {
                if (cdr instanceof Pair) {
                    tr.error('e', "source specifier must be last elemnt in library reference");
                }
                sourcePath = car;
            } else {
                if (sbuf.length() > 0) {
                    sbuf.append('.');
                }
                sbuf.append(Compilation.mangleNameIfNeeded(car.toString()));
            }
            libref = cdr;
        }
        ModuleInfo minfo = null;
        if (sourcePath == null || (minfo = require.lookupModuleFromSourcePath(sourcePath, defs)) != null) {
            String lname = sbuf.toString();
            if (lname.startsWith("srfi.")) {
                String demangled = Compilation.demangleName(lname.substring(5));
                int dot = demangled.indexOf(46);
                if (dot < 0) {
                    srfiName = null;
                    dot = demangled.length();
                } else {
                    srfiName = demangled.substring(dot + 1);
                }
                String srfiNumber = null;
                if (dot >= 2 || demangled.charAt(0) == ':') {
                    int i = 1;
                    while (true) {
                        if (i != dot) {
                            if (Character.digit(demangled.charAt(i), 10) < 0) {
                                break;
                            }
                            i++;
                        } else {
                            srfiNumber = demangled.substring(1, dot);
                            break;
                        }
                    }
                }
                if (srfiNumber == null) {
                    tr.error('e', "SRFI library reference must have the form: (srfi :NNN [name])");
                    return false;
                }
                int srfiIndex = SRFI97Map.length;
                do {
                    srfiIndex--;
                    if (srfiIndex < 0) {
                        tr.error('e', "unknown SRFI number '" + srfiNumber + "' in SRFI library reference");
                        return false;
                    }
                } while (!SRFI97Map[srfiIndex][0].equals(srfiNumber));
                String srfiNameExpected = SRFI97Map[srfiIndex][1];
                String srfiClass = SRFI97Map[srfiIndex][2];
                if (srfiName != null && !srfiName.equals(srfiNameExpected)) {
                    tr.error('w', "the name of SRFI " + srfiNumber + " should be '" + srfiNameExpected + '\'');
                }
                if (srfiClass == "") {
                    return true;
                }
                if (srfiClass == MISSING) {
                    tr.error('e', "sorry - Kawa does not support SRFI " + srfiNumber + " (" + srfiNameExpected + ')');
                    return false;
                }
                lname = srfiClass;
            }
            int classPrefixPathLength = this.classPrefixPath.length;
            for (int i2 = 0; i2 < classPrefixPathLength; i2++) {
                try {
                    minfo = ModuleManager.getInstance().findWithClassName(this.classPrefixPath[i2] + lname);
                } catch (Exception e) {
                }
            }
            if (minfo == null) {
                tr.error('e', "unknown class " + lname);
                return false;
            }
            require.importDefinitions((String) null, minfo, mapper, forms, defs, tr);
            return true;
        }
        tr.error('e', "malformed URL: " + sourcePath);
        return false;
    }

    public Expression rewriteForm(Pair form, Translator tr) {
        return null;
    }
}
