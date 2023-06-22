package kawa.lib;

import androidx.fragment.app.FragmentTransaction;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.FileUtils;
import gnu.kawa.functions.Format;
import gnu.kawa.functions.IsEqual;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.mapping.CallContext;
import gnu.mapping.InPort;
import gnu.mapping.OutPort;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.text.FilePath;
import gnu.text.Path;
import gnu.text.URIPath;
import java.io.File;
import java.io.IOException;
import kawa.standard.readchar;

/* compiled from: files.scm */
public class files extends ModuleBody {
    public static final ModuleMethod $Mn$Grpathname;
    public static final ModuleMethod $Pcfile$Mnseparator;
    public static final files $instance = new files();
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("path?").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("filepath?").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("path-parent").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("path-last").readResolve());
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("path-extension").readResolve());
    static final SimpleSymbol Lit13 = ((SimpleSymbol) new SimpleSymbol("path-port").readResolve());
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("path-query").readResolve());
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("path-fragment").readResolve());
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("file-exists?").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("file-directory?").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("file-readable?").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("file-writable?").readResolve());
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("URI?").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("delete-file").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("rename-file").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("copy-file").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("create-directory").readResolve());
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("directory-files").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("->pathname").readResolve());
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("%file-separator").readResolve());
    static final SimpleSymbol Lit27 = ((SimpleSymbol) new SimpleSymbol("system-tmpdir").readResolve());
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("resolve-uri").readResolve());
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("make-temporary-file").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("absolute-path?").readResolve());
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol("path-scheme").readResolve());
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("path-authority").readResolve());
    static final SimpleSymbol Lit6 = ((SimpleSymbol) new SimpleSymbol("path-user-info").readResolve());
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("path-host").readResolve());
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("path-file").readResolve());
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("path-directory").readResolve());
    public static final ModuleMethod URI$Qu;
    public static final ModuleMethod absolute$Mnpath$Qu;
    public static final ModuleMethod copy$Mnfile;
    public static final ModuleMethod create$Mndirectory;
    public static final ModuleMethod delete$Mnfile;
    public static final ModuleMethod directory$Mnfiles;
    public static final ModuleMethod file$Mndirectory$Qu;
    public static final ModuleMethod file$Mnexists$Qu;
    public static final ModuleMethod file$Mnreadable$Qu;
    public static final ModuleMethod file$Mnwritable$Qu;
    public static final ModuleMethod filepath$Qu;
    public static final ModuleMethod make$Mntemporary$Mnfile;
    public static final ModuleMethod path$Mnauthority;
    public static final ModuleMethod path$Mndirectory;
    public static final ModuleMethod path$Mnextension;
    public static final ModuleMethod path$Mnfile;
    public static final ModuleMethod path$Mnfragment;
    public static final ModuleMethod path$Mnhost;
    public static final ModuleMethod path$Mnlast;
    public static final ModuleMethod path$Mnparent;
    public static final ModuleMethod path$Mnport;
    public static final ModuleMethod path$Mnquery;
    public static final ModuleMethod path$Mnscheme;
    public static final ModuleMethod path$Mnuser$Mninfo;
    public static final ModuleMethod path$Qu;
    public static final ModuleMethod rename$Mnfile;
    public static final ModuleMethod resolve$Mnuri;
    public static final ModuleMethod system$Mntmpdir;

    static {
        files files = $instance;
        path$Qu = new ModuleMethod(files, 1, Lit0, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        filepath$Qu = new ModuleMethod(files, 2, Lit1, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        URI$Qu = new ModuleMethod(files, 3, Lit2, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        absolute$Mnpath$Qu = new ModuleMethod(files, 4, Lit3, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnscheme = new ModuleMethod(files, 5, Lit4, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnauthority = new ModuleMethod(files, 6, Lit5, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnuser$Mninfo = new ModuleMethod(files, 7, Lit6, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnhost = new ModuleMethod(files, 8, Lit7, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnfile = new ModuleMethod(files, 9, Lit8, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mndirectory = new ModuleMethod(files, 10, Lit9, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnparent = new ModuleMethod(files, 11, Lit10, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnlast = new ModuleMethod(files, 12, Lit11, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnextension = new ModuleMethod(files, 13, Lit12, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnport = new ModuleMethod(files, 14, Lit13, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnquery = new ModuleMethod(files, 15, Lit14, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        path$Mnfragment = new ModuleMethod(files, 16, Lit15, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        file$Mnexists$Qu = new ModuleMethod(files, 17, Lit16, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        file$Mndirectory$Qu = new ModuleMethod(files, 18, Lit17, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        file$Mnreadable$Qu = new ModuleMethod(files, 19, Lit18, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        file$Mnwritable$Qu = new ModuleMethod(files, 20, Lit19, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        delete$Mnfile = new ModuleMethod(files, 21, Lit20, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        rename$Mnfile = new ModuleMethod(files, 22, Lit21, 8194);
        copy$Mnfile = new ModuleMethod(files, 23, Lit22, 8194);
        create$Mndirectory = new ModuleMethod(files, 24, Lit23, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        directory$Mnfiles = new ModuleMethod(files, 25, Lit24, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        $Mn$Grpathname = new ModuleMethod(files, 26, Lit25, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        $Pcfile$Mnseparator = new ModuleMethod(files, 27, Lit26, 0);
        system$Mntmpdir = new ModuleMethod(files, 28, Lit27, 0);
        resolve$Mnuri = new ModuleMethod(files, 29, Lit28, 8194);
        make$Mntemporary$Mnfile = new ModuleMethod(files, 30, Lit29, 4096);
        $instance.run();
    }

    public files() {
        ModuleInfo.register(this);
    }

    public static FilePath makeTemporaryFile() {
        return makeTemporaryFile("kawa~d.tmp");
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
    }

    public static boolean isPath(Object path) {
        return path instanceof Path;
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 1:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 2:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 3:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 4:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 5:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 6:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 7:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 8:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 9:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 10:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 11:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 12:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 13:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 14:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 15:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 16:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 17:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 18:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 19:
                if (FilePath.coerceToFilePathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 20:
                if (FilePath.coerceToFilePathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 21:
                if (FilePath.coerceToFilePathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 24:
                if (FilePath.coerceToFilePathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 25:
                if (FilePath.coerceToFilePathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 26:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 30:
                if (!(obj instanceof CharSequence)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public static boolean isFilepath(Object path) {
        return path instanceof FilePath;
    }

    public static boolean URI$Qu(Object path) {
        return path instanceof URIPath;
    }

    public static boolean isAbsolutePath(Path path) {
        return path.isAbsolute();
    }

    public static Object pathScheme(Path p) {
        String s = p.getScheme();
        return s == null ? Boolean.FALSE : s;
    }

    public static Object pathAuthority(Path p) {
        String s = p.getAuthority();
        return s == null ? Boolean.FALSE : s;
    }

    public static Object pathUserInfo(Path p) {
        String s = p.getUserInfo();
        return s == null ? Boolean.FALSE : s;
    }

    public static String pathHost(Path p) {
        return p.getHost();
    }

    public static Object pathFile(Path p) {
        String s = p.getPath();
        return s == null ? Boolean.FALSE : s;
    }

    public static Object pathDirectory(Path p) {
        Path s = p.getDirectory();
        return s == null ? Boolean.FALSE : s.toString();
    }

    public static Object pathParent(Path p) {
        Path s = p.getParent();
        return s == null ? Boolean.FALSE : s.toString();
    }

    public static Object pathLast(Path p) {
        String s = p.getLast();
        return s == null ? Boolean.FALSE : s;
    }

    public static Object pathExtension(Path p) {
        String s = p.getExtension();
        return s == null ? Boolean.FALSE : s;
    }

    public static int pathPort(Path p) {
        return p.getPort();
    }

    public static Object pathQuery(Path p) {
        String s = p.getQuery();
        return s == null ? Boolean.FALSE : s;
    }

    public static Object pathFragment(Path p) {
        String s = p.getFragment();
        return s == null ? Boolean.FALSE : s;
    }

    public static boolean isFileExists(Path file) {
        return file.exists();
    }

    public static boolean isFileDirectory(Path file) {
        return file.isDirectory();
    }

    public static boolean isFileReadable(FilePath file) {
        return file.toFile().canRead();
    }

    public static boolean isFileWritable(FilePath file) {
        return file.toFile().canWrite();
    }

    public static void deleteFile(FilePath file) {
        if (!file.delete()) {
            throw new IOException(Format.formatToString(0, "cannot delete ~a", file).toString());
        }
    }

    public static boolean renameFile(FilePath oldname, FilePath newname) {
        return oldname.toFile().renameTo(newname.toFile());
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 22:
                if (FilePath.coerceToFilePathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                if (FilePath.coerceToFilePathOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 23:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                if (Path.coerceToPathOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 29:
                if (Path.coerceToPathOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                if (Path.coerceToPathOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            default:
                return super.match2(moduleMethod, obj, obj2, callContext);
        }
    }

    public static void copyFile(Path from, Path to) {
        InPort in = ports.openInputFile(from);
        OutPort out = ports.openOutputFile(to);
        for (Object ch = readchar.readChar.apply1(in); !ports.isEofObject(ch); ch = readchar.readChar.apply1(in)) {
            ports.writeChar(ch, out);
        }
        ports.closeOutputPort(out);
        ports.closeInputPort(in);
    }

    public static boolean createDirectory(FilePath dirname) {
        return dirname.toFile().mkdir();
    }

    public static Object directoryFiles(FilePath dir) {
        File file = dir.toFile();
        String[] files = new File(file == null ? null : file.toString()).list();
        if (files == null) {
            return Boolean.FALSE;
        }
        return LList.makeList(files, 0);
    }

    public static Path $To$Pathname(Object filename) {
        return Path.valueOf(filename);
    }

    public static String $PcFileSeparator() {
        return System.getProperty("file.separator");
    }

    public int match0(ModuleMethod moduleMethod, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 27:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 28:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 30:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            default:
                return super.match0(moduleMethod, callContext);
        }
    }

    public static String systemTmpdir() {
        String name = System.getProperty("java.io.tmpdir");
        if (name != null) {
            return name;
        }
        return IsEqual.apply($PcFileSeparator(), "\\") ? "C:\\temp" : "/tmp";
    }

    public static Path resolveUri(Path uri, Path base) {
        return base.resolve(uri);
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        switch (moduleMethod.selector) {
            case 22:
                try {
                    try {
                        return renameFile(FilePath.makeFilePath(obj), FilePath.makeFilePath(obj2)) ? Boolean.TRUE : Boolean.FALSE;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "rename-file", 2, obj2);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "rename-file", 1, obj);
                }
            case 23:
                try {
                    try {
                        copyFile(Path.valueOf(obj), Path.valueOf(obj2));
                        return Values.empty;
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "copy-file", 2, obj2);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "copy-file", 1, obj);
                }
            case 29:
                try {
                    try {
                        return resolveUri(Path.valueOf(obj), Path.valueOf(obj2));
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "resolve-uri", 2, obj2);
                    }
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "resolve-uri", 1, obj);
                }
            default:
                return super.apply2(moduleMethod, obj, obj2);
        }
    }

    public static FilePath makeTemporaryFile(CharSequence fmt) {
        return FilePath.makeFilePath(FileUtils.createTempFile(fmt.toString()));
    }

    public Object apply0(ModuleMethod moduleMethod) {
        switch (moduleMethod.selector) {
            case 27:
                return $PcFileSeparator();
            case 28:
                return systemTmpdir();
            case 30:
                return makeTemporaryFile();
            default:
                return super.apply0(moduleMethod);
        }
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 1:
                return isPath(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 2:
                return isFilepath(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 3:
                return URI$Qu(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 4:
                try {
                    return isAbsolutePath(Path.valueOf(obj)) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "absolute-path?", 1, obj);
                }
            case 5:
                try {
                    return pathScheme(Path.valueOf(obj));
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "path-scheme", 1, obj);
                }
            case 6:
                try {
                    return pathAuthority(Path.valueOf(obj));
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "path-authority", 1, obj);
                }
            case 7:
                try {
                    return pathUserInfo(Path.valueOf(obj));
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "path-user-info", 1, obj);
                }
            case 8:
                try {
                    return pathHost(Path.valueOf(obj));
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "path-host", 1, obj);
                }
            case 9:
                try {
                    return pathFile(Path.valueOf(obj));
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "path-file", 1, obj);
                }
            case 10:
                try {
                    return pathDirectory(Path.valueOf(obj));
                } catch (ClassCastException e7) {
                    throw new WrongType(e7, "path-directory", 1, obj);
                }
            case 11:
                try {
                    return pathParent(Path.valueOf(obj));
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "path-parent", 1, obj);
                }
            case 12:
                try {
                    return pathLast(Path.valueOf(obj));
                } catch (ClassCastException e9) {
                    throw new WrongType(e9, "path-last", 1, obj);
                }
            case 13:
                try {
                    return pathExtension(Path.valueOf(obj));
                } catch (ClassCastException e10) {
                    throw new WrongType(e10, "path-extension", 1, obj);
                }
            case 14:
                try {
                    return Integer.valueOf(pathPort(Path.valueOf(obj)));
                } catch (ClassCastException e11) {
                    throw new WrongType(e11, "path-port", 1, obj);
                }
            case 15:
                try {
                    return pathQuery(Path.valueOf(obj));
                } catch (ClassCastException e12) {
                    throw new WrongType(e12, "path-query", 1, obj);
                }
            case 16:
                try {
                    return pathFragment(Path.valueOf(obj));
                } catch (ClassCastException e13) {
                    throw new WrongType(e13, "path-fragment", 1, obj);
                }
            case 17:
                try {
                    return isFileExists(Path.valueOf(obj)) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e14) {
                    throw new WrongType(e14, "file-exists?", 1, obj);
                }
            case 18:
                try {
                    return isFileDirectory(Path.valueOf(obj)) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e15) {
                    throw new WrongType(e15, "file-directory?", 1, obj);
                }
            case 19:
                try {
                    return isFileReadable(FilePath.makeFilePath(obj)) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e16) {
                    throw new WrongType(e16, "file-readable?", 1, obj);
                }
            case 20:
                try {
                    return isFileWritable(FilePath.makeFilePath(obj)) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e17) {
                    throw new WrongType(e17, "file-writable?", 1, obj);
                }
            case 21:
                try {
                    deleteFile(FilePath.makeFilePath(obj));
                    return Values.empty;
                } catch (ClassCastException e18) {
                    throw new WrongType(e18, "delete-file", 1, obj);
                }
            case 24:
                try {
                    return createDirectory(FilePath.makeFilePath(obj)) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e19) {
                    throw new WrongType(e19, "create-directory", 1, obj);
                }
            case 25:
                try {
                    return directoryFiles(FilePath.makeFilePath(obj));
                } catch (ClassCastException e20) {
                    throw new WrongType(e20, "directory-files", 1, obj);
                }
            case 26:
                return $To$Pathname(obj);
            case 30:
                try {
                    return makeTemporaryFile((CharSequence) obj);
                } catch (ClassCastException e21) {
                    throw new WrongType(e21, "make-temporary-file", 1, obj);
                }
            default:
                return super.apply1(moduleMethod, obj);
        }
    }
}
