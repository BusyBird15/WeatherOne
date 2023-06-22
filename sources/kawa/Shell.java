package kawa;

import gnu.bytecode.ArrayClassLoader;
import gnu.bytecode.ZipLoader;
import gnu.expr.Compilation;
import gnu.expr.CompiledModule;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleExp;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleManager;
import gnu.lists.AbstractFormat;
import gnu.lists.Consumer;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure;
import gnu.mapping.TtyInPort;
import gnu.mapping.Values;
import gnu.mapping.WrappedException;
import gnu.mapping.WrongArguments;
import gnu.text.FilePath;
import gnu.text.Lexer;
import gnu.text.Path;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;

public class Shell {
    private static Class[] boolClasses = {Boolean.TYPE};
    public static ThreadLocal currentLoadPath = new ThreadLocal();
    public static Object[] defaultFormatInfo;
    public static Method defaultFormatMethod;
    public static String defaultFormatName;
    static Object[][] formats = {new Object[]{"scheme", "gnu.kawa.functions.DisplayFormat", "getSchemeFormat", boolClasses, Boolean.FALSE}, new Object[]{"readable-scheme", "gnu.kawa.functions.DisplayFormat", "getSchemeFormat", boolClasses, Boolean.TRUE}, new Object[]{"elisp", "gnu.kawa.functions.DisplayFormat", "getEmacsLispFormat", boolClasses, Boolean.FALSE}, new Object[]{"readable-elisp", "gnu.kawa.functions.DisplayFormat", "getEmacsLispFormat", boolClasses, Boolean.TRUE}, new Object[]{"clisp", "gnu.kawa.functions.DisplayFormat", "getCommonLispFormat", boolClasses, Boolean.FALSE}, new Object[]{"readable-clisp", "gnu.kawa.functions.DisplayFormat", "getCommonLispFormat", boolClasses, Boolean.TRUE}, new Object[]{"commonlisp", "gnu.kawa.functions.DisplayFormat", "getCommonLispFormat", boolClasses, Boolean.FALSE}, new Object[]{"readable-commonlisp", "gnu.kawa.functions.DisplayFormat", "getCommonLispFormat", boolClasses, Boolean.TRUE}, new Object[]{"xml", "gnu.xml.XMLPrinter", "make", xmlPrinterClasses, portArg, null}, new Object[]{"html", "gnu.xml.XMLPrinter", "make", xmlPrinterClasses, portArg, "html"}, new Object[]{"xhtml", "gnu.xml.XMLPrinter", "make", xmlPrinterClasses, portArg, "xhtml"}, new Object[]{"cgi", "gnu.kawa.xml.HttpPrinter", "make", httpPrinterClasses, portArg}, new Object[]{"ignore", "gnu.lists.VoidConsumer", "getInstance", noClasses}, new Object[]{null}};
    private static Class[] httpPrinterClasses = {OutPort.class};
    private static Class[] noClasses = new Class[0];
    private static Object portArg = "(port)";
    private static Class[] xmlPrinterClasses = {OutPort.class, Object.class};

    public static void setDefaultFormat(String name) {
        Object[] info;
        String name2 = name.intern();
        defaultFormatName = name2;
        int i = 0;
        while (true) {
            info = formats[i];
            Object iname = info[0];
            if (iname == null) {
                System.err.println("kawa: unknown output format '" + name2 + "'");
                System.exit(-1);
            } else if (iname == name2) {
                break;
            }
            i++;
        }
        defaultFormatInfo = info;
        try {
            defaultFormatMethod = Class.forName((String) info[1]).getMethod((String) info[2], (Class[]) info[3]);
        } catch (Throwable ex) {
            System.err.println("kawa:  caught " + ex + " while looking for format '" + name2 + "'");
            System.exit(-1);
        }
        if (!defaultFormatInfo[1].equals("gnu.lists.VoidConsumer")) {
            ModuleBody.setMainPrintValues(true);
        }
    }

    public static Consumer getOutputConsumer(OutPort out) {
        Object[] info = defaultFormatInfo;
        if (out == null) {
            return VoidConsumer.getInstance();
        }
        if (info == null) {
            return Language.getDefaultLanguage().getOutputConsumer(out);
        }
        try {
            Object[] args = new Object[(info.length - 4)];
            System.arraycopy(info, 4, args, 0, args.length);
            int i = args.length;
            while (true) {
                i--;
                if (i < 0) {
                    break;
                } else if (args[i] == portArg) {
                    args[i] = out;
                }
            }
            Object format = defaultFormatMethod.invoke((Object) null, args);
            if (!(format instanceof AbstractFormat)) {
                return (Consumer) format;
            }
            out.objectFormat = (AbstractFormat) format;
            return out;
        } catch (Throwable ex) {
            throw new RuntimeException("cannot get output-format '" + defaultFormatName + "' - caught " + ex);
        }
    }

    public static boolean run(Language language, Environment env) {
        OutPort perr;
        InPort inp = InPort.inDefault();
        SourceMessages messages = new SourceMessages();
        if (inp instanceof TtyInPort) {
            Procedure prompter = language.getPrompter();
            if (prompter != null) {
                ((TtyInPort) inp).setPrompter(prompter);
            }
            perr = OutPort.errDefault();
        } else {
            perr = null;
        }
        Throwable ex = run(language, env, inp, OutPort.outDefault(), perr, messages);
        if (ex == null) {
            return true;
        }
        printError(ex, messages, OutPort.errDefault());
        return false;
    }

    public static Throwable run(Language language, Environment env, InPort inp, OutPort pout, OutPort perr, SourceMessages messages) {
        AbstractFormat saveFormat = null;
        if (pout != null) {
            saveFormat = pout.objectFormat;
        }
        try {
            return run(language, env, inp, getOutputConsumer(pout), perr, (URL) null, messages);
        } finally {
            if (pout != null) {
                pout.objectFormat = saveFormat;
            }
        }
    }

    public static boolean run(Language language, Environment env, InPort inp, Consumer out, OutPort perr, URL url) {
        SourceMessages messages = new SourceMessages();
        Throwable ex = run(language, env, inp, out, perr, url, messages);
        if (ex != null) {
            printError(ex, messages, perr);
        }
        return ex == null;
    }

    public static Throwable run(Language language, Environment env, InPort inp, Consumer out, OutPort perr, URL url, SourceMessages messages) {
        boolean sawError;
        int ch;
        Language saveLanguage = Language.setSaveCurrent(language);
        Lexer lexer = language.getLexer(inp, messages);
        boolean interactive = perr != null;
        lexer.setInteractive(interactive);
        CallContext ctx = CallContext.getInstance();
        Consumer saveConsumer = null;
        if (out != null) {
            saveConsumer = ctx.consumer;
            ctx.consumer = out;
        }
        try {
            Thread thread = Thread.currentThread();
            ClassLoader parentLoader = thread.getContextClassLoader();
            if (!(parentLoader instanceof ArrayClassLoader)) {
                thread.setContextClassLoader(new ArrayClassLoader(parentLoader));
            }
        } catch (SecurityException e) {
        }
        while (true) {
            try {
                Compilation comp = language.parse(lexer, 7, (ModuleInfo) null);
                if (interactive) {
                    sawError = messages.checkErrors((PrintWriter) perr, 20);
                } else if (messages.seenErrors()) {
                    throw new SyntaxException(messages);
                } else {
                    sawError = false;
                }
                if (comp == null) {
                    break;
                } else if (!sawError) {
                    ModuleExp module = comp.getModule();
                    StringBuilder append = new StringBuilder().append("atInteractiveLevel$");
                    int i = ModuleExp.interactiveCounter + 1;
                    ModuleExp.interactiveCounter = i;
                    module.setName(append.append(i).toString());
                    while (true) {
                        ch = inp.read();
                        if (ch >= 0 && ch != 13 && ch != 10) {
                            if (ch != 32 && ch != 9) {
                                inp.unread();
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (!ModuleExp.evalModule(env, ctx, comp, url, perr)) {
                        continue;
                    } else {
                        if (out instanceof Writer) {
                            ((Writer) out).flush();
                        }
                        if (ch < 0) {
                            break;
                        }
                    }
                } else {
                    continue;
                }
            } catch (Throwable th) {
                if (out != null) {
                    ctx.consumer = saveConsumer;
                }
                Language.restoreCurrent(saveLanguage);
                throw th;
            }
        }
        if (out != null) {
            ctx.consumer = saveConsumer;
        }
        Language.restoreCurrent(saveLanguage);
        return null;
    }

    public static void printError(Throwable ex, SourceMessages messages, OutPort perr) {
        if (ex instanceof WrongArguments) {
            WrongArguments e = (WrongArguments) ex;
            messages.printAll((PrintWriter) perr, 20);
            if (e.usage != null) {
                perr.println("usage: " + e.usage);
            }
            e.printStackTrace(perr);
        } else if (ex instanceof ClassCastException) {
            messages.printAll((PrintWriter) perr, 20);
            perr.println("Invalid parameter, was: " + ex.getMessage());
            ex.printStackTrace(perr);
        } else {
            if (ex instanceof SyntaxException) {
                SyntaxException se = (SyntaxException) ex;
                if (se.getMessages() == messages) {
                    se.printAll(perr, 20);
                    se.clear();
                    return;
                }
            }
            messages.printAll((PrintWriter) perr, 20);
            ex.printStackTrace(perr);
        }
    }

    public static final CompiledModule checkCompiledZip(InputStream fs, Path path, Environment env, Language language) throws IOException {
        CompiledModule compiledModule = null;
        try {
            fs.mark(5);
            boolean isZip = fs.read() == 80 && fs.read() == 75 && fs.read() == 3 && fs.read() == 4;
            fs.reset();
            if (isZip) {
                fs.close();
                Environment orig_env = Environment.getCurrent();
                String name = path.toString();
                if (env != orig_env) {
                    try {
                        Environment.setCurrent(env);
                    } catch (IOException ex) {
                        throw new WrappedException("load: " + name + " - " + ex.toString(), ex);
                    } catch (Throwable th) {
                        if (env != orig_env) {
                            Environment.setCurrent(orig_env);
                        }
                        throw th;
                    }
                }
                if (!(path instanceof FilePath)) {
                    throw new RuntimeException("load: " + name + " - not a file path");
                }
                File zfile = ((FilePath) path).toFile();
                if (!zfile.exists()) {
                    throw new RuntimeException("load: " + name + " - not found");
                } else if (!zfile.canRead()) {
                    throw new RuntimeException("load: " + name + " - not readable");
                } else {
                    compiledModule = CompiledModule.make(new ZipLoader(name).loadAllClasses(), language);
                    if (env != orig_env) {
                        Environment.setCurrent(orig_env);
                    }
                }
            }
        } catch (IOException e) {
        }
        return compiledModule;
    }

    public static boolean runFileOrClass(String fname, boolean lineByLine, int skipLines) {
        Path path;
        InputStream fs;
        Language language = Language.getDefaultLanguage();
        try {
            if (fname.equals("-")) {
                path = Path.valueOf("/dev/stdin");
                fs = System.in;
            } else {
                path = Path.valueOf(fname);
                fs = path.openInputStream();
            }
            return runFile(fs, path, Environment.getCurrent(), lineByLine, skipLines);
        } catch (Throwable e) {
            try {
                CompiledModule.make(Class.forName(fname), language).evalModule(Environment.getCurrent(), OutPort.outDefault());
                return true;
            } catch (Throwable ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }

    public static final boolean runFile(InputStream fs, Path path, Environment env, boolean lineByLine, int skipLines) throws Throwable {
        InPort src;
        if (!(fs instanceof BufferedInputStream)) {
            fs = new BufferedInputStream(fs);
        }
        Language language = Language.getDefaultLanguage();
        Path savePath = (Path) currentLoadPath.get();
        try {
            currentLoadPath.set(path);
            CompiledModule cmodule = checkCompiledZip(fs, path, env, language);
            if (cmodule == null) {
                src = InPort.openFile(fs, path);
                while (true) {
                    skipLines--;
                    if (skipLines < 0) {
                        break;
                    }
                    src.skipRestOfLine();
                }
                SourceMessages messages = new SourceMessages();
                URL url = path.toURL();
                if (lineByLine) {
                    Throwable ex = run(language, env, src, ModuleBody.getMainPrintValues() ? getOutputConsumer(OutPort.outDefault()) : new VoidConsumer(), (OutPort) null, url, messages);
                    if (ex != null) {
                        throw ex;
                    }
                } else {
                    cmodule = compileSource(src, env, url, language, messages);
                    messages.printAll((PrintWriter) OutPort.errDefault(), 20);
                    if (cmodule == null) {
                        src.close();
                        currentLoadPath.set(savePath);
                        return false;
                    }
                }
                src.close();
            }
            if (cmodule != null) {
                cmodule.evalModule(env, OutPort.outDefault());
            }
            currentLoadPath.set(savePath);
            return true;
        } catch (Throwable th) {
            currentLoadPath.set(savePath);
            throw th;
        }
    }

    static CompiledModule compileSource(InPort port, Environment env, URL url, Language language, SourceMessages messages) throws SyntaxException, IOException {
        Compilation comp = language.parse(port, messages, 1, ModuleManager.getInstance().findWithSourcePath(port.getName()));
        CallContext.getInstance().values = Values.noArgs;
        Object inst = ModuleExp.evalModule1(env, comp, url, (OutPort) null);
        if (inst == null || messages.seenErrors()) {
            return null;
        }
        return new CompiledModule(comp.getModule(), inst, language);
    }
}
