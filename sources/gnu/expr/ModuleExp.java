package gnu.expr;

import com.google.appinventor.components.runtime.repackaged.org.json.zip.JSONzip;
import gnu.bytecode.ArrayClassLoader;
import gnu.bytecode.ClassType;
import gnu.bytecode.Field;
import gnu.kawa.reflect.ClassMemberLocation;
import gnu.kawa.reflect.StaticFieldLocation;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.Location;
import gnu.mapping.OutPort;
import gnu.mapping.Symbol;
import gnu.mapping.WrappedException;
import gnu.text.Path;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import java.io.Externalizable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ModuleExp extends LambdaExp implements Externalizable {
    public static final int EXPORT_SPECIFIED = 16384;
    public static final int IMMEDIATE = 1048576;
    public static final int LAZY_DECLARATIONS = 524288;
    public static final int NONSTATIC_SPECIFIED = 65536;
    public static final int STATIC_RUN_SPECIFIED = 262144;
    public static final int STATIC_SPECIFIED = 32768;
    public static final int SUPERTYPE_SPECIFIED = 131072;
    public static boolean alwaysCompile = compilerAvailable;
    public static boolean compilerAvailable = true;
    public static String dumpZipPrefix;
    public static int interactiveCounter;
    static int lastZipCounter;
    public static boolean neverCompile = false;
    ModuleInfo info;
    ClassType[] interfaces;
    ClassType superType;

    public static Class evalToClass(Compilation comp, URL url) throws SyntaxException {
        ModuleExp module = comp.getModule();
        SourceMessages messages = comp.getMessages();
        try {
            comp.minfo.loadByStages(12);
            if (messages.seenErrors()) {
                return null;
            }
            ArrayClassLoader loader = comp.loader;
            if (url == null) {
                url = Path.currentPath().toURL();
            }
            loader.setResourceContext(url);
            ZipOutputStream zout = null;
            if (dumpZipPrefix != null) {
                StringBuffer stringBuffer = new StringBuffer(dumpZipPrefix);
                lastZipCounter++;
                if (interactiveCounter > lastZipCounter) {
                    lastZipCounter = interactiveCounter;
                }
                stringBuffer.append(lastZipCounter);
                stringBuffer.append(".zip");
                zout = new ZipOutputStream(new FileOutputStream(stringBuffer.toString()));
            }
            for (int iClass = 0; iClass < comp.numClasses; iClass++) {
                ClassType clas = comp.classes[iClass];
                String className = clas.getName();
                byte[] classBytes = clas.writeToArray();
                loader.addClass(className, classBytes);
                if (zout != null) {
                    ZipEntry zipEntry = new ZipEntry(className.replace('.', '/') + ".class");
                    zipEntry.setSize((long) classBytes.length);
                    CRC32 crc = new CRC32();
                    crc.update(classBytes);
                    zipEntry.setCrc(crc.getValue());
                    zipEntry.setMethod(0);
                    zout.putNextEntry(zipEntry);
                    zout.write(classBytes);
                }
            }
            if (zout != null) {
                zout.close();
            }
            Class clas2 = null;
            ArrayClassLoader context = loader;
            while (context.getParent() instanceof ArrayClassLoader) {
                context = (ArrayClassLoader) context.getParent();
            }
            for (int iClass2 = 0; iClass2 < comp.numClasses; iClass2++) {
                ClassType ctype = comp.classes[iClass2];
                Class cclass = loader.loadClass(ctype.getName());
                ctype.setReflectClass(cclass);
                ctype.setExisting(true);
                if (iClass2 == 0) {
                    clas2 = cclass;
                } else if (context != loader) {
                    context.addClass(cclass);
                }
            }
            ModuleInfo minfo = comp.minfo;
            minfo.setModuleClass(clas2);
            comp.cleanupAfterCompilation();
            int ndeps = minfo.numDependencies;
            for (int idep = 0; idep < ndeps; idep++) {
                ModuleInfo dep = minfo.dependencies[idep];
                Class dclass = dep.getModuleClassRaw();
                if (dclass == null) {
                    dclass = evalToClass(dep.comp, (URL) null);
                }
                comp.loader.addClass(dclass);
            }
            return clas2;
        } catch (IOException ex) {
            throw new WrappedException("I/O error in lambda eval", ex);
        } catch (ClassNotFoundException ex2) {
            throw new WrappedException("class not found in lambda eval", ex2);
        } catch (Throwable ex3) {
            comp.getMessages().error('f', "internal compile error - caught " + ex3, ex3);
            throw new SyntaxException(messages);
        }
    }

    public static void mustNeverCompile() {
        alwaysCompile = false;
        neverCompile = true;
        compilerAvailable = false;
    }

    public static void mustAlwaysCompile() {
        alwaysCompile = true;
        neverCompile = false;
    }

    public static final boolean evalModule(Environment env, CallContext ctx, Compilation comp, URL url, OutPort msg) throws Throwable {
        ModuleExp mexp = comp.getModule();
        Language language = comp.getLanguage();
        Object inst = evalModule1(env, comp, url, msg);
        if (inst == null) {
            return false;
        }
        evalModule2(env, ctx, language, mexp, inst);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0041, code lost:
        if (r2.checkErrors((java.io.PrintWriter) r14, 20) != false) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0054, code lost:
        if (r2.seenErrors() == false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0058, code lost:
        if (r12.mustCompile != false) goto L_0x0099;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005c, code lost:
        if (gnu.expr.Compilation.debugPrintFinalExpr == false) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005e, code lost:
        if (r14 == null) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0060, code lost:
        r14.println("[Evaluating final module \"" + r3.getName() + "\":");
        r3.print(r14);
        r14.println(']');
        r14.flush();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x008b, code lost:
        r0 = java.lang.Boolean.TRUE;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x008d, code lost:
        gnu.mapping.Environment.restoreCurrent(r5);
        gnu.expr.Compilation.restoreCurrent(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0093, code lost:
        if (r7 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0095, code lost:
        r7.setContextClassLoader(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r0 = evalToClass(r12, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009d, code lost:
        if (r0 != null) goto L_0x00ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x009f, code lost:
        gnu.mapping.Environment.restoreCurrent(r5);
        gnu.expr.Compilation.restoreCurrent(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a5, code lost:
        if (r7 == null) goto L_0x00aa;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a7, code lost:
        r7.setContextClassLoader(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r7 = java.lang.Thread.currentThread();
        r6 = r7.getContextClassLoader();
        r7.setContextClassLoader(r0.getClassLoader());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00c9, code lost:
        if (r2.checkErrors((java.io.PrintWriter) r14, 20) != false) goto L_0x00cb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00de, code lost:
        r7 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00e4, code lost:
        if (r2.seenErrors() == false) goto L_0x00e6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00e6, code lost:
        gnu.mapping.Environment.restoreCurrent(r5);
        gnu.expr.Compilation.restoreCurrent(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00ec, code lost:
        if (r7 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00ee, code lost:
        r7.setContextClassLoader(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final java.lang.Object evalModule1(gnu.mapping.Environment r11, gnu.expr.Compilation r12, java.net.URL r13, gnu.mapping.OutPort r14) throws gnu.text.SyntaxException {
        /*
            r10 = 0
            r8 = 0
            gnu.expr.ModuleExp r3 = r12.getModule()
            gnu.expr.ModuleInfo r9 = r12.minfo
            r3.info = r9
            gnu.mapping.Environment r5 = gnu.mapping.Environment.setSaveCurrent(r11)
            gnu.expr.Compilation r4 = gnu.expr.Compilation.setSaveCurrent(r12)
            gnu.text.SourceMessages r2 = r12.getMessages()
            r6 = 0
            r7 = 0
            boolean r9 = alwaysCompile
            if (r9 == 0) goto L_0x0028
            boolean r9 = neverCompile
            if (r9 == 0) goto L_0x0028
            java.lang.RuntimeException r8 = new java.lang.RuntimeException
            java.lang.String r9 = "alwaysCompile and neverCompile are both true!"
            r8.<init>(r9)
            throw r8
        L_0x0028:
            boolean r9 = neverCompile
            if (r9 == 0) goto L_0x002e
            r12.mustCompile = r10
        L_0x002e:
            r9 = 6
            r12.process(r9)     // Catch:{ all -> 0x00f3 }
            gnu.expr.ModuleInfo r9 = r12.minfo     // Catch:{ all -> 0x00f3 }
            r10 = 8
            r9.loadByStages(r10)     // Catch:{ all -> 0x00f3 }
            if (r14 == 0) goto L_0x0050
            r9 = 20
            boolean r9 = r2.checkErrors((java.io.PrintWriter) r14, (int) r9)     // Catch:{ all -> 0x00f3 }
            if (r9 == 0) goto L_0x0056
        L_0x0043:
            gnu.mapping.Environment.restoreCurrent(r5)
            gnu.expr.Compilation.restoreCurrent(r4)
            if (r7 == 0) goto L_0x004e
            r7.setContextClassLoader(r6)
        L_0x004e:
            r0 = r8
        L_0x004f:
            return r0
        L_0x0050:
            boolean r9 = r2.seenErrors()     // Catch:{ all -> 0x00f3 }
            if (r9 != 0) goto L_0x0043
        L_0x0056:
            boolean r9 = r12.mustCompile     // Catch:{ all -> 0x00f3 }
            if (r9 != 0) goto L_0x0099
            boolean r8 = gnu.expr.Compilation.debugPrintFinalExpr     // Catch:{ all -> 0x00f3 }
            if (r8 == 0) goto L_0x008b
            if (r14 == 0) goto L_0x008b
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f3 }
            r8.<init>()     // Catch:{ all -> 0x00f3 }
            java.lang.String r9 = "[Evaluating final module \""
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x00f3 }
            java.lang.String r9 = r3.getName()     // Catch:{ all -> 0x00f3 }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x00f3 }
            java.lang.String r9 = "\":"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x00f3 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x00f3 }
            r14.println(r8)     // Catch:{ all -> 0x00f3 }
            r3.print(r14)     // Catch:{ all -> 0x00f3 }
            r8 = 93
            r14.println(r8)     // Catch:{ all -> 0x00f3 }
            r14.flush()     // Catch:{ all -> 0x00f3 }
        L_0x008b:
            java.lang.Boolean r0 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x00f3 }
            gnu.mapping.Environment.restoreCurrent(r5)
            gnu.expr.Compilation.restoreCurrent(r4)
            if (r7 == 0) goto L_0x004f
            r7.setContextClassLoader(r6)
            goto L_0x004f
        L_0x0099:
            java.lang.Class r0 = evalToClass(r12, r13)     // Catch:{ all -> 0x00f3 }
            if (r0 != 0) goto L_0x00ac
            gnu.mapping.Environment.restoreCurrent(r5)
            gnu.expr.Compilation.restoreCurrent(r4)
            if (r7 == 0) goto L_0x00aa
            r7.setContextClassLoader(r6)
        L_0x00aa:
            r0 = r8
            goto L_0x004f
        L_0x00ac:
            java.lang.Thread r7 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x00dd }
            java.lang.ClassLoader r6 = r7.getContextClassLoader()     // Catch:{ Throwable -> 0x00dd }
            java.lang.ClassLoader r8 = r0.getClassLoader()     // Catch:{ Throwable -> 0x00dd }
            r7.setContextClassLoader(r8)     // Catch:{ Throwable -> 0x00dd }
        L_0x00bb:
            r8 = 0
            r3.body = r8     // Catch:{ all -> 0x00f3 }
            r8 = 0
            r3.thisVariable = r8     // Catch:{ all -> 0x00f3 }
            if (r14 == 0) goto L_0x00e0
            r8 = 20
            boolean r8 = r2.checkErrors((java.io.PrintWriter) r14, (int) r8)     // Catch:{ all -> 0x00f3 }
            if (r8 == 0) goto L_0x00e6
        L_0x00cb:
            r8 = 0
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r8)     // Catch:{ all -> 0x00f3 }
            gnu.mapping.Environment.restoreCurrent(r5)
            gnu.expr.Compilation.restoreCurrent(r4)
            if (r7 == 0) goto L_0x004f
            r7.setContextClassLoader(r6)
            goto L_0x004f
        L_0x00dd:
            r1 = move-exception
            r7 = 0
            goto L_0x00bb
        L_0x00e0:
            boolean r8 = r2.seenErrors()     // Catch:{ all -> 0x00f3 }
            if (r8 != 0) goto L_0x00cb
        L_0x00e6:
            gnu.mapping.Environment.restoreCurrent(r5)
            gnu.expr.Compilation.restoreCurrent(r4)
            if (r7 == 0) goto L_0x004f
            r7.setContextClassLoader(r6)
            goto L_0x004f
        L_0x00f3:
            r8 = move-exception
            gnu.mapping.Environment.restoreCurrent(r5)
            gnu.expr.Compilation.restoreCurrent(r4)
            if (r7 == 0) goto L_0x00ff
            r7.setContextClassLoader(r6)
        L_0x00ff:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.ModuleExp.evalModule1(gnu.mapping.Environment, gnu.expr.Compilation, java.net.URL, gnu.mapping.OutPort):java.lang.Object");
    }

    public static final void evalModule2(Environment env, CallContext ctx, Language language, ModuleExp mexp, Object inst) throws Throwable {
        Symbol sym;
        Object value;
        Environment orig_env = Environment.setSaveCurrent(env);
        Thread thread = null;
        try {
            if (inst == Boolean.TRUE) {
                mexp.body.apply(ctx);
            } else {
                if (inst instanceof Class) {
                    inst = ModuleContext.getContext().findInstance((Class) inst);
                }
                if (inst instanceof Runnable) {
                    if (inst instanceof ModuleBody) {
                        ModuleBody mb = (ModuleBody) inst;
                        if (!mb.runDone) {
                            mb.runDone = true;
                            mb.run(ctx);
                        }
                    } else {
                        ((Runnable) inst).run();
                    }
                }
                if (mexp == null) {
                    ClassMemberLocation.defineAll(inst, language, env);
                } else {
                    for (Declaration decl = mexp.firstDecl(); decl != null; decl = decl.nextDecl()) {
                        Object dname = decl.getSymbol();
                        if (!decl.isPrivate() && dname != null) {
                            Field fld = decl.field;
                            if (dname instanceof Symbol) {
                                sym = (Symbol) dname;
                            } else {
                                sym = Symbol.make("", dname.toString().intern());
                            }
                            Object property = language.getEnvPropertyFor(decl);
                            Expression dvalue = decl.getValue();
                            if ((decl.field.getModifiers() & 16) != 0) {
                                if (!(dvalue instanceof QuoteExp) || dvalue == QuoteExp.undefined_exp) {
                                    value = decl.field.getReflectField().get((Object) null);
                                    if (!decl.isIndirectBinding()) {
                                        decl.setValue(QuoteExp.getInstance(value));
                                    } else if (!decl.isAlias() || !(dvalue instanceof ReferenceExp)) {
                                        decl.setValue((Expression) null);
                                    }
                                } else {
                                    value = ((QuoteExp) dvalue).getValue();
                                }
                                if (decl.isIndirectBinding()) {
                                    env.addLocation(sym, property, (Location) value);
                                } else {
                                    env.define(sym, property, value);
                                }
                            } else {
                                StaticFieldLocation loc = new StaticFieldLocation(fld.getDeclaringClass(), fld.getName());
                                loc.setDeclaration(decl);
                                env.addLocation(sym, property, loc);
                                decl.setValue((Expression) null);
                            }
                        }
                    }
                }
            }
            ctx.runUntilDone();
        } finally {
            Environment.restoreCurrent(orig_env);
            if (thread != null) {
                thread.setContextClassLoader((ClassLoader) null);
            }
        }
    }

    public String getNamespaceUri() {
        return this.info.uri;
    }

    public final ClassType getSuperType() {
        return this.superType;
    }

    public final void setSuperType(ClassType s) {
        this.superType = s;
    }

    public final ClassType[] getInterfaces() {
        return this.interfaces;
    }

    public final void setInterfaces(ClassType[] s) {
        this.interfaces = s;
    }

    public final boolean isStatic() {
        return getFlag(32768) || ((Compilation.moduleStatic >= 0 || getFlag(1048576)) && !getFlag(131072) && !getFlag(65536));
    }

    public boolean staticInitRun() {
        return isStatic() && (getFlag(262144) || Compilation.moduleStatic == 2);
    }

    public void allocChildClasses(Compilation comp) {
        declareClosureEnv();
        if (comp.usingCPStyle()) {
            allocFrame(comp);
        }
    }

    /* access modifiers changed from: package-private */
    public void allocFields(Compilation comp) {
        for (Declaration decl = firstDecl(); decl != null; decl = decl.nextDecl()) {
            if ((!decl.isSimple() || decl.isPublic()) && decl.field == null && decl.getFlag(65536) && decl.getFlag(6)) {
                decl.makeField(comp, (Expression) null);
            }
        }
        for (Declaration decl2 = firstDecl(); decl2 != null; decl2 = decl2.nextDecl()) {
            if (decl2.field == null) {
                Expression value = decl2.getValue();
                if ((!decl2.isSimple() || decl2.isPublic() || decl2.isNamespaceDecl() || (decl2.getFlag(JSONzip.int14) && decl2.getFlag(6))) && !decl2.getFlag(65536)) {
                    if (!(value instanceof LambdaExp) || (value instanceof ModuleExp) || (value instanceof ClassExp)) {
                        if (!decl2.shouldEarlyInit() && !decl2.isAlias()) {
                            value = null;
                        }
                        decl2.makeField(comp, value);
                    } else {
                        ((LambdaExp) value).allocFieldFor(comp);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public <R, D> R visit(ExpVisitor<R, D> visitor, D d) {
        return visitor.visitModuleExp(this, d);
    }

    public void print(OutPort out) {
        out.startLogicalBlock("(Module/", ")", 2);
        Object sym = getSymbol();
        if (sym != null) {
            out.print(sym);
            out.print('/');
        }
        out.print(this.id);
        out.print('/');
        out.writeSpaceFill();
        out.startLogicalBlock("(", false, ")");
        Declaration decl = firstDecl();
        if (decl != null) {
            out.print("Declarations:");
            while (decl != null) {
                out.writeSpaceFill();
                decl.printInfo(out);
                decl = decl.nextDecl();
            }
        }
        out.endLogicalBlock(")");
        out.writeSpaceLinear();
        if (this.body == null) {
            out.print("<null body>");
        } else {
            this.body.print(out);
        }
        out.endLogicalBlock(")");
    }

    public Declaration firstDecl() {
        synchronized (this) {
            if (getFlag(524288)) {
                this.info.setupModuleExp();
            }
        }
        return this.decls;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.bytecode.ClassType classFor(gnu.expr.Compilation r12) {
        /*
            r11 = this;
            gnu.bytecode.ClassType r8 = r11.type
            if (r8 == 0) goto L_0x000d
            gnu.bytecode.ClassType r8 = r11.type
            gnu.bytecode.ClassType r9 = gnu.expr.Compilation.typeProcedure
            if (r8 == r9) goto L_0x000d
            gnu.bytecode.ClassType r0 = r11.type
        L_0x000c:
            return r0
        L_0x000d:
            java.lang.String r3 = r11.getFileName()
            java.lang.String r4 = r11.getName()
            r1 = 0
            r7 = 0
            if (r4 == 0) goto L_0x0084
            r3 = r4
        L_0x001a:
            java.lang.String r8 = r11.getName()
            if (r8 != 0) goto L_0x0023
            r11.setName(r3)
        L_0x0023:
            java.lang.String r3 = gnu.expr.Compilation.mangleNameIfNeeded(r3)
            java.lang.String r8 = r12.classPrefix
            int r8 = r8.length()
            if (r8 != 0) goto L_0x00e0
            if (r7 == 0) goto L_0x00e0
            boolean r8 = r7.isAbsolute()
            if (r8 != 0) goto L_0x00e0
            gnu.text.Path r6 = r7.getParent()
            if (r6 == 0) goto L_0x00e0
            java.lang.String r5 = r6.toString()
            int r8 = r5.length()
            if (r8 <= 0) goto L_0x00e0
            java.lang.String r8 = ".."
            int r8 = r5.indexOf(r8)
            if (r8 >= 0) goto L_0x00e0
            java.lang.String r8 = "file.separator"
            java.lang.String r8 = java.lang.System.getProperty(r8)
            java.lang.String r9 = "/"
            java.lang.String r5 = r5.replaceAll(r8, r9)
            java.lang.String r8 = "./"
            boolean r8 = r5.startsWith(r8)
            if (r8 == 0) goto L_0x0068
            r8 = 2
            java.lang.String r5 = r5.substring(r8)
        L_0x0068:
            java.lang.String r8 = "."
            boolean r8 = r5.equals(r8)
            if (r8 == 0) goto L_0x00c4
            r1 = r3
        L_0x0071:
            gnu.bytecode.ClassType r0 = new gnu.bytecode.ClassType
            r0.<init>(r1)
            r11.setType(r0)
            gnu.expr.ModuleExp r8 = r12.mainLambda
            if (r8 != r11) goto L_0x000c
            gnu.bytecode.ClassType r8 = r12.mainClass
            if (r8 != 0) goto L_0x00f5
            r12.mainClass = r0
            goto L_0x000c
        L_0x0084:
            if (r3 != 0) goto L_0x008f
            java.lang.String r3 = r11.getName()
            if (r3 != 0) goto L_0x001a
            java.lang.String r3 = "$unnamed_input_file$"
            goto L_0x001a
        L_0x008f:
            java.lang.String r8 = r11.filename
            java.lang.String r9 = "-"
            boolean r8 = r8.equals(r9)
            if (r8 != 0) goto L_0x00a3
            java.lang.String r8 = r11.filename
            java.lang.String r9 = "/dev/stdin"
            boolean r8 = r8.equals(r9)
            if (r8 == 0) goto L_0x00ad
        L_0x00a3:
            java.lang.String r3 = r11.getName()
            if (r3 != 0) goto L_0x001a
            java.lang.String r3 = "$stdin$"
            goto L_0x001a
        L_0x00ad:
            gnu.text.Path r7 = gnu.text.Path.valueOf(r3)
            java.lang.String r3 = r7.getLast()
            r8 = 46
            int r2 = r3.lastIndexOf(r8)
            if (r2 <= 0) goto L_0x001a
            r8 = 0
            java.lang.String r3 = r3.substring(r8, r2)
            goto L_0x001a
        L_0x00c4:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = gnu.expr.Compilation.mangleURI(r5)
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.String r9 = "."
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r3)
            java.lang.String r1 = r8.toString()
            goto L_0x0071
        L_0x00e0:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = r12.classPrefix
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r8 = r8.append(r3)
            java.lang.String r1 = r8.toString()
            goto L_0x0071
        L_0x00f5:
            gnu.bytecode.ClassType r8 = r12.mainClass
            java.lang.String r8 = r8.getName()
            boolean r8 = r1.equals(r8)
            if (r8 != 0) goto L_0x000c
            r8 = 101(0x65, float:1.42E-43)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "inconsistent main class name: "
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.StringBuilder r9 = r9.append(r1)
            java.lang.String r10 = " - old name: "
            java.lang.StringBuilder r9 = r9.append(r10)
            gnu.bytecode.ClassType r10 = r12.mainClass
            java.lang.String r10 = r10.getName()
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            r12.error(r8, r9)
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.expr.ModuleExp.classFor(gnu.expr.Compilation):gnu.bytecode.ClassType");
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        String name = null;
        if (this.type == null || this.type == Compilation.typeProcedure || this.type.isExisting()) {
            if (0 == 0) {
                name = getName();
            }
            if (name == null) {
                name = getFileName();
            }
            out.writeObject(name);
            return;
        }
        out.writeObject(this.type);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Object name = in.readObject();
        if (name instanceof ClassType) {
            this.type = (ClassType) name;
            setName(this.type.getName());
        } else {
            setName((String) name);
        }
        this.flags |= 524288;
    }
}
