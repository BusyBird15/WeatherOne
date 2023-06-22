package kawa;

import gnu.bytecode.ClassType;
import gnu.expr.ApplicationMainSupport;
import gnu.expr.Compilation;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleExp;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleManager;
import gnu.kawa.servlet.HttpRequestContext;
import gnu.lists.FString;
import gnu.mapping.CharArrayInPort;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure0or1;
import gnu.mapping.Values;
import gnu.text.Options;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import gnu.text.WriterManager;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class repl extends Procedure0or1 {
    public static String compilationTopname = null;
    static int defaultParseOptions = 72;
    public static String homeDirectory;
    public static boolean noConsole;
    static Language previousLanguage;
    static boolean shutdownRegistered = WriterManager.instance.registerShutdownHook();
    Language language;

    public repl(Language language2) {
        this.language = language2;
    }

    public Object apply0() {
        Shell.run(this.language, Environment.getCurrent());
        return Values.empty;
    }

    public Object apply1(Object env) {
        Shell.run(this.language, (Environment) env);
        return Values.empty;
    }

    static void bad_option(String str) {
        System.err.println("kawa: bad option '" + str + "'");
        printOptions(System.err);
        System.exit(-1);
    }

    public static void printOption(PrintStream out, String option, String doc) {
        out.print(" ");
        out.print(option);
        int len = option.length() + 1;
        for (int i = 0; i < 30 - len; i++) {
            out.print(" ");
        }
        out.print(" ");
        out.println(doc);
    }

    public static void printOptions(PrintStream out) {
        out.println("Usage: [java kawa.repl | kawa] [options ...]");
        out.println();
        out.println(" Generic options:");
        printOption(out, "--help", "Show help about options");
        printOption(out, "--author", "Show author information");
        printOption(out, "--version", "Show version information");
        out.println();
        out.println(" Options");
        printOption(out, "-e <expr>", "Evaluate expression <expr>");
        printOption(out, "-c <expr>", "Same as -e, but make sure ~/.kawarc.scm is run first");
        printOption(out, "-f <filename>", "File to interpret");
        printOption(out, "-s| --", "Start reading commands interactively from console");
        printOption(out, "-w", "Launch the interpreter in a GUI window");
        printOption(out, "--server <port>", "Start a server accepting telnet connections on <port>");
        printOption(out, "--debug-dump-zip", "Compiled interactive expressions to a zip archive");
        printOption(out, "--debug-print-expr", "Print generated internal expressions");
        printOption(out, "--debug-print-final-expr", "Print expression after any optimizations");
        printOption(out, "--debug-error-prints-stack-trace", "Print stack trace with errors");
        printOption(out, "--debug-warning-prints-stack-trace", "Print stack trace with warnings");
        printOption(out, "--[no-]full-tailcalls", "(Don't) use full tail-calls");
        printOption(out, "-C <filename> ...", "Compile named files to Java class files");
        printOption(out, "--output-format <format>", "Use <format> when printing top-level output");
        printOption(out, "--<language>", "Select source language, one of:");
        String[][] languages = Language.getLanguages();
        for (int i = 0; i < languages.length; i++) {
            out.print("   ");
            String[] lang = languages[i];
            int nwords = lang.length - 1;
            for (int j = 0; j < nwords; j++) {
                out.print(lang[j] + " ");
            }
            if (i == 0) {
                out.print("[default]");
            }
            out.println();
        }
        out.println(" Compilation options, must be specified before -C");
        printOption(out, "-d <dirname>", "Directory to place .class files in");
        printOption(out, "-P <prefix>", "Prefix to prepand to class names");
        printOption(out, "-T <topname>", "name to give to top-level class");
        printOption(out, "--main", "Generate an application, with a main method");
        printOption(out, "--applet", "Generate an applet");
        printOption(out, "--servlet", "Generate a servlet");
        printOption(out, "--module-static", "Top-level definitions are by default static");
        ArrayList<String> keys = Compilation.options.keys();
        for (int i2 = 0; i2 < keys.size(); i2++) {
            String name = keys.get(i2);
            printOption(out, "--" + name, Compilation.options.getDoc(name));
        }
        out.println();
        out.println("For more information go to:  http://www.gnu.org/software/kawa/");
    }

    static void checkInitFile() {
        Object obj;
        if (homeDirectory == null) {
            File initFile = null;
            homeDirectory = System.getProperty("user.home");
            if (homeDirectory != null) {
                obj = new FString(homeDirectory);
                initFile = new File(homeDirectory, "/".equals(System.getProperty("file.separator")) ? ".kawarc.scm" : "kawarc.scm");
            } else {
                obj = Boolean.FALSE;
            }
            Environment.getCurrent().put("home-directory", obj);
            if (initFile != null && initFile.exists() && !Shell.runFileOrClass(initFile.getPath(), true, 0)) {
                System.exit(-1);
            }
        }
    }

    public static void setArgs(String[] args, int arg_start) {
        ApplicationMainSupport.setArgs(args, arg_start);
    }

    public static void getLanguageFromFilenameExtension(String name) {
        if (previousLanguage == null) {
            previousLanguage = Language.getInstanceFromFilenameExtension(name);
            if (previousLanguage != null) {
                Language.setDefaults(previousLanguage);
                return;
            }
        }
        getLanguage();
    }

    public static void getLanguage() {
        if (previousLanguage == null) {
            previousLanguage = Language.getInstance((String) null);
            Language.setDefaults(previousLanguage);
        }
    }

    public static int processArgs(String[] args, int iArg, int maxArg) {
        int port;
        String opt_value;
        int port2;
        boolean something_done = false;
        while (iArg < maxArg) {
            String arg = args[iArg];
            if (arg.equals("-c") || arg.equals("-e")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                getLanguage();
                setArgs(args, iArg + 1);
                if (arg.equals("-c")) {
                    checkInitFile();
                }
                Language language2 = Language.getDefaultLanguage();
                SourceMessages messages = new SourceMessages();
                Throwable ex = Shell.run(language2, Environment.getCurrent(), (InPort) new CharArrayInPort(args[iArg]), OutPort.outDefault(), (OutPort) null, messages);
                if (ex != null) {
                    Shell.printError(ex, messages, OutPort.errDefault());
                    System.exit(-1);
                }
                something_done = true;
            } else if (arg.equals("-f")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                String filename = args[iArg];
                getLanguageFromFilenameExtension(filename);
                setArgs(args, iArg + 1);
                checkInitFile();
                if (!Shell.runFileOrClass(filename, true, 0)) {
                    System.exit(-1);
                }
                something_done = true;
            } else if (arg.startsWith("--script")) {
                String count = arg.substring(8);
                int iArg2 = iArg + 1;
                int skipLines = 0;
                if (count.length() > 0) {
                    try {
                        skipLines = Integer.parseInt(count);
                    } catch (Throwable th) {
                        iArg2 = maxArg;
                    }
                }
                if (iArg2 == maxArg) {
                    bad_option(arg);
                }
                String filename2 = args[iArg2];
                getLanguageFromFilenameExtension(filename2);
                setArgs(args, iArg2 + 1);
                checkInitFile();
                if (!Shell.runFileOrClass(filename2, true, skipLines)) {
                    System.exit(-1);
                }
                return -1;
            } else if (arg.equals("\\")) {
                int iArg3 = iArg + 1;
                if (iArg3 == maxArg) {
                    bad_option(arg);
                }
                String filename3 = args[iArg3];
                SourceMessages messages2 = new SourceMessages();
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filename3));
                    int ch = bufferedInputStream.read();
                    if (ch == 35) {
                        StringBuffer stringBuffer = new StringBuffer(100);
                        Vector vector = new Vector(10);
                        int state = 0;
                        while (ch != 10 && ch != 13 && ch >= 0) {
                            ch = bufferedInputStream.read();
                        }
                        while (true) {
                            int ch2 = bufferedInputStream.read();
                            if (ch2 < 0) {
                                System.err.println("unexpected end-of-file processing argument line for: '" + filename3 + '\'');
                                System.exit(-1);
                            }
                            if (state == 0) {
                                if (ch2 == 92 || ch2 == 39 || ch2 == 34) {
                                    state = ch2;
                                } else if (ch2 != 10 && ch2 != 13) {
                                    if (ch2 == 32 || ch2 == 9) {
                                        if (stringBuffer.length() > 0) {
                                            vector.addElement(stringBuffer.toString());
                                            stringBuffer.setLength(0);
                                        }
                                    }
                                }
                            } else if (state == 92) {
                                state = 0;
                            } else if (ch2 == state) {
                                state = 0;
                            }
                            stringBuffer.append((char) ch2);
                        }
                        if (stringBuffer.length() > 0) {
                            vector.addElement(stringBuffer.toString());
                        }
                        int nxargs = vector.size();
                        if (nxargs > 0) {
                            String[] sargs = new String[nxargs];
                            vector.copyInto(sargs);
                            int ixarg = processArgs(sargs, 0, nxargs);
                            if (ixarg >= 0 && ixarg < nxargs) {
                                System.err.println("" + (nxargs - ixarg) + " unused meta args");
                            }
                        }
                    }
                    getLanguageFromFilenameExtension(filename3);
                    InPort freader = InPort.openFile(bufferedInputStream, filename3);
                    setArgs(args, iArg3 + 1);
                    checkInitFile();
                    OutPort err = OutPort.errDefault();
                    Throwable ex2 = Shell.run(Language.getDefaultLanguage(), Environment.getCurrent(), freader, OutPort.outDefault(), (OutPort) null, messages2);
                    messages2.printAll((PrintWriter) err, 20);
                    if (ex2 != null) {
                        if ((ex2 instanceof SyntaxException) && ((SyntaxException) ex2).getMessages() == messages2) {
                            System.exit(1);
                        }
                        throw ex2;
                    }
                } catch (Throwable ex3) {
                    Shell.printError(ex3, messages2, OutPort.errDefault());
                    System.exit(1);
                }
                return -1;
            } else if (arg.equals("-s") || arg.equals("--")) {
                getLanguage();
                setArgs(args, iArg + 1);
                checkInitFile();
                Shell.run(Language.getDefaultLanguage(), Environment.getCurrent());
                return -1;
            } else if (arg.equals("-w")) {
                iArg++;
                getLanguage();
                setArgs(args, iArg);
                checkInitFile();
                startGuiConsole();
                something_done = true;
            } else if (arg.equals("-d")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                ModuleManager.getInstance().setCompilationDirectory(args[iArg]);
            } else if (arg.equals("--target") || arg.equals("target")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                String arg2 = args[iArg];
                if (arg2.equals("7")) {
                    Compilation.defaultClassFileVersion = ClassType.JDK_1_7_VERSION;
                }
                if (arg2.equals("6") || arg2.equals("1.6")) {
                    Compilation.defaultClassFileVersion = ClassType.JDK_1_6_VERSION;
                } else if (arg2.equals("5") || arg2.equals("1.5")) {
                    Compilation.defaultClassFileVersion = ClassType.JDK_1_5_VERSION;
                } else if (arg2.equals("1.4")) {
                    Compilation.defaultClassFileVersion = ClassType.JDK_1_4_VERSION;
                } else if (arg2.equals("1.3")) {
                    Compilation.defaultClassFileVersion = ClassType.JDK_1_3_VERSION;
                } else if (arg2.equals("1.2")) {
                    Compilation.defaultClassFileVersion = ClassType.JDK_1_2_VERSION;
                } else if (arg2.equals("1.1")) {
                    Compilation.defaultClassFileVersion = ClassType.JDK_1_1_VERSION;
                } else {
                    bad_option(arg2);
                }
            } else if (arg.equals("-P")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                Compilation.classPrefixDefault = args[iArg];
            } else if (arg.equals("-T")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                compilationTopname = args[iArg];
            } else if (arg.equals("-C")) {
                int iArg4 = iArg + 1;
                if (iArg4 == maxArg) {
                    bad_option(arg);
                }
                compileFiles(args, iArg4, maxArg);
                return -1;
            } else if (arg.equals("--output-format") || arg.equals("--format")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                Shell.setDefaultFormat(args[iArg]);
            } else if (arg.equals("--connect")) {
                iArg++;
                if (iArg == maxArg) {
                    bad_option(arg);
                }
                if (args[iArg].equals("-")) {
                    port2 = 0;
                } else {
                    try {
                        port2 = Integer.parseInt(args[iArg]);
                    } catch (NumberFormatException e) {
                        bad_option("--connect port#");
                        port2 = -1;
                    }
                }
                try {
                    Telnet telnet = new Telnet(new Socket(InetAddress.getByName((String) null), port2), true);
                    InputStream sin = telnet.getInputStream();
                    PrintStream printStream = new PrintStream(telnet.getOutputStream(), true);
                    System.setIn(sin);
                    System.setOut(printStream);
                    System.setErr(printStream);
                } catch (IOException ex4) {
                    ex4.printStackTrace(System.err);
                    throw new Error(ex4.toString());
                }
            } else if (arg.equals("--server")) {
                getLanguage();
                int iArg5 = iArg + 1;
                if (iArg5 == maxArg) {
                    bad_option(arg);
                }
                if (args[iArg5].equals("-")) {
                    port = 0;
                } else {
                    try {
                        port = Integer.parseInt(args[iArg5]);
                    } catch (NumberFormatException e2) {
                        bad_option("--server port#");
                        port = -1;
                    }
                }
                try {
                    ServerSocket serverSocket = new ServerSocket(port);
                    System.err.println("Listening on port " + serverSocket.getLocalPort());
                    while (true) {
                        System.err.print("waiting ... ");
                        System.err.flush();
                        Socket client = serverSocket.accept();
                        System.err.println("got connection from " + client.getInetAddress() + " port:" + client.getPort());
                        TelnetRepl.serve(Language.getDefaultLanguage(), client);
                    }
                } catch (IOException ex5) {
                    throw new Error(ex5.toString());
                }
            } else if (arg.equals("--http-auto-handler")) {
                iArg += 2;
                if (iArg >= maxArg) {
                    bad_option(arg);
                }
                System.err.println("kawa: HttpServer classes not found");
                System.exit(-1);
            } else if (arg.equals("--http-start")) {
                iArg++;
                if (iArg >= maxArg) {
                    bad_option("missing httpd port argument");
                }
                System.err.println("kawa: HttpServer classes not found");
                System.exit(-1);
            } else if (arg.equals("--main")) {
                Compilation.generateMainDefault = true;
            } else if (arg.equals("--applet")) {
                defaultParseOptions |= 16;
            } else if (arg.equals("--servlet")) {
                defaultParseOptions |= 32;
                HttpRequestContext.importServletDefinitions = 2;
            } else if (arg.equals("--debug-dump-zip")) {
                ModuleExp.dumpZipPrefix = "kawa-zip-dump-";
            } else if (arg.equals("--debug-print-expr")) {
                Compilation.debugPrintExpr = true;
            } else if (arg.equals("--debug-print-final-expr")) {
                Compilation.debugPrintFinalExpr = true;
            } else if (arg.equals("--debug-error-prints-stack-trace")) {
                SourceMessages.debugStackTraceOnError = true;
            } else if (arg.equals("--debug-warning-prints-stack-trace")) {
                SourceMessages.debugStackTraceOnWarning = true;
            } else if (arg.equals("--module-nonstatic") || arg.equals("--no-module-static")) {
                Compilation.moduleStatic = -1;
            } else if (arg.equals("--module-static")) {
                Compilation.moduleStatic = 1;
            } else if (arg.equals("--module-static-run")) {
                Compilation.moduleStatic = 2;
            } else if (arg.equals("--no-inline") || arg.equals("--inline=none")) {
                Compilation.inlineOk = false;
            } else if (arg.equals("--no-console")) {
                noConsole = true;
            } else if (arg.equals("--inline")) {
                Compilation.inlineOk = true;
            } else if (arg.equals("--cps")) {
                Compilation.defaultCallConvention = 4;
            } else if (arg.equals("--full-tailcalls")) {
                Compilation.defaultCallConvention = 3;
            } else if (arg.equals("--no-full-tailcalls")) {
                Compilation.defaultCallConvention = 1;
            } else if (arg.equals("--pedantic")) {
                Language.requirePedantic = true;
            } else if (arg.equals("--help")) {
                printOptions(System.out);
                System.exit(0);
            } else if (arg.equals("--author")) {
                System.out.println("Per Bothner <per@bothner.com>");
                System.exit(0);
            } else if (arg.equals("--version")) {
                System.out.print("Kawa ");
                System.out.print(Version.getVersion());
                System.out.println();
                System.out.println("Copyright (C) 2009 Per Bothner");
                something_done = true;
            } else if (arg.length() > 0 && arg.charAt(0) == '-') {
                String name = arg;
                if (name.length() > 2 && name.charAt(0) == '-') {
                    name = name.substring(name.charAt(1) == '-' ? 2 : 1);
                }
                Language lang = Language.getInstance(name);
                if (lang != null) {
                    if (previousLanguage == null) {
                        Language.setDefaults(lang);
                    } else {
                        Language.setCurrentLanguage(lang);
                    }
                    previousLanguage = lang;
                } else {
                    int eq = name.indexOf("=");
                    if (eq < 0) {
                        opt_value = null;
                    } else {
                        opt_value = name.substring(eq + 1);
                        name = name.substring(0, eq);
                    }
                    boolean startsWithNo = name.startsWith("no-") && name.length() > 3;
                    if (opt_value == null && startsWithNo) {
                        opt_value = "no";
                        name = name.substring(3);
                    }
                    String msg = Compilation.options.set(name, opt_value);
                    if (msg != null) {
                        if (startsWithNo && msg == Options.UNKNOWN) {
                            msg = "both '--no-' prefix and '=" + opt_value + "' specified";
                        }
                        if (msg == Options.UNKNOWN) {
                            bad_option(arg);
                        } else {
                            System.err.println("kawa: bad option '" + arg + "': " + msg);
                            System.exit(-1);
                        }
                    }
                }
            } else if (!ApplicationMainSupport.processSetProperty(arg)) {
                break;
            }
            iArg++;
        }
        if (something_done) {
            return -1;
        }
        return iArg;
    }

    public static void compileFiles(String[] args, int iArg, int maxArg) {
        ModuleManager manager = ModuleManager.getInstance();
        Compilation[] comps = new Compilation[(maxArg - iArg)];
        ModuleInfo[] infos = new ModuleInfo[(maxArg - iArg)];
        SourceMessages messages = new SourceMessages();
        for (int i = iArg; i < maxArg; i++) {
            String arg = args[i];
            getLanguageFromFilenameExtension(arg);
            try {
                Compilation comp = Language.getDefaultLanguage().parse(InPort.openFile(arg), messages, defaultParseOptions);
                if (compilationTopname != null) {
                    ClassType ctype = new ClassType(Compilation.mangleNameIfNeeded(compilationTopname));
                    ModuleExp mexp = comp.getModule();
                    mexp.setType(ctype);
                    mexp.setName(compilationTopname);
                    comp.mainClass = ctype;
                }
                infos[i - iArg] = manager.find(comp);
                comps[i - iArg] = comp;
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
                System.exit(-1);
                break;
            } catch (Throwable ex2) {
                if (!(ex2 instanceof SyntaxException) || ((SyntaxException) ex2).getMessages() != messages) {
                    internalError(ex2, (Compilation) null, arg);
                }
            }
            if (messages.seenErrorsOrWarnings()) {
                System.err.println("(compiling " + arg + ')');
                if (messages.checkErrors(System.err, 20)) {
                    System.exit(1);
                }
            }
        }
        for (int i2 = iArg; i2 < maxArg; i2++) {
            String arg2 = args[i2];
            Compilation comp2 = comps[i2 - iArg];
            try {
                System.err.println("(compiling " + arg2 + " to " + comp2.mainClass.getName() + ')');
                infos[i2 - iArg].loadByStages(14);
                boolean sawErrors = messages.seenErrors();
                messages.checkErrors(System.err, 50);
                if (sawErrors) {
                    System.exit(-1);
                }
                comps[i2 - iArg] = comp2;
                boolean sawErrors2 = messages.seenErrors();
                messages.checkErrors(System.err, 50);
                if (sawErrors2) {
                    System.exit(-1);
                }
            } catch (Throwable ex3) {
                internalError(ex3, comp2, arg2);
            }
        }
    }

    static void internalError(Throwable ex, Compilation comp, Object arg) {
        StringBuffer sbuf = new StringBuffer();
        if (comp != null) {
            String file = comp.getFileName();
            int line = comp.getLineNumber();
            if (file != null && line > 0) {
                sbuf.append(file);
                sbuf.append(':');
                sbuf.append(line);
                sbuf.append(": ");
            }
        }
        sbuf.append("internal error while compiling ");
        sbuf.append(arg);
        System.err.println(sbuf.toString());
        ex.printStackTrace(System.err);
        System.exit(-1);
    }

    public static void main(String[] args) {
        try {
            int iArg = processArgs(args, 0, args.length);
            if (iArg >= 0) {
                if (iArg < args.length) {
                    String filename = args[iArg];
                    getLanguageFromFilenameExtension(filename);
                    setArgs(args, iArg + 1);
                    checkInitFile();
                    boolean runFileOrClass = Shell.runFileOrClass(filename, false, 0);
                } else {
                    getLanguage();
                    setArgs(args, iArg);
                    checkInitFile();
                    if (shouldUseGuiConsole()) {
                        startGuiConsole();
                    } else if (!Shell.run(Language.getDefaultLanguage(), Environment.getCurrent())) {
                        System.exit(-1);
                    }
                }
                if (!shutdownRegistered) {
                    OutPort.runCleanups();
                }
                ModuleBody.exitDecrement();
            }
        } finally {
            if (!shutdownRegistered) {
                OutPort.runCleanups();
            }
            ModuleBody.exitDecrement();
        }
    }

    public static boolean shouldUseGuiConsole() {
        if (noConsole) {
            return true;
        }
        try {
            if (Class.forName("java.lang.System").getMethod("console", new Class[0]).invoke(new Object[0], new Object[0]) == null) {
                return true;
            }
        } catch (Throwable th) {
        }
        return false;
    }

    private static void startGuiConsole() {
        try {
            Class.forName("kawa.GuiConsole").newInstance();
        } catch (Exception ex) {
            System.err.println("failed to create Kawa window: " + ex);
            System.exit(-1);
        }
    }
}
