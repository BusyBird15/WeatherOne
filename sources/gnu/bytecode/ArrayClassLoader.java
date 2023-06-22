package gnu.bytecode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

public class ArrayClassLoader extends ClassLoader {
    Hashtable cmap = new Hashtable(100);
    URL context;
    Hashtable map = new Hashtable(100);

    public ArrayClassLoader() {
    }

    public ArrayClassLoader(ClassLoader parent) {
        super(parent);
    }

    public URL getResourceContext() {
        return this.context;
    }

    public void setResourceContext(URL context2) {
        this.context = context2;
    }

    public ArrayClassLoader(byte[][] classBytes) {
        int i = classBytes.length;
        while (true) {
            i--;
            if (i >= 0) {
                addClass("lambda" + i, classBytes[i]);
            } else {
                return;
            }
        }
    }

    public ArrayClassLoader(String[] classNames, byte[][] classBytes) {
        int i = classBytes.length;
        while (true) {
            i--;
            if (i >= 0) {
                addClass(classNames[i], classBytes[i]);
            } else {
                return;
            }
        }
    }

    public void addClass(Class clas) {
        this.cmap.put(clas.getName(), clas);
    }

    public void addClass(String name, byte[] bytes) {
        this.map.put(name, bytes);
    }

    public void addClass(ClassType ctype) {
        this.map.put(ctype.getName(), ctype);
    }

    public InputStream getResourceAsStream(String name) {
        InputStream in = super.getResourceAsStream(name);
        if (in != null || !name.endsWith(".class")) {
            return in;
        }
        Object r = this.map.get(name.substring(0, name.length() - 6).replace('/', '.'));
        if (r instanceof byte[]) {
            return new ByteArrayInputStream((byte[]) r);
        }
        return in;
    }

    /* access modifiers changed from: protected */
    public URL findResource(String name) {
        if (this.context != null) {
            try {
                URL url = new URL(this.context, name);
                url.openConnection().connect();
                return url;
            } catch (Throwable th) {
            }
        }
        return super.findResource(name);
    }

    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class clas = loadClass(name);
        if (resolve) {
            resolveClass(clas);
        }
        return clas;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.Class<?>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v11, resolved type: java.lang.Class} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Class loadClass(java.lang.String r8) throws java.lang.ClassNotFoundException {
        /*
            r7 = this;
            java.util.Hashtable r5 = r7.cmap
            java.lang.Object r4 = r5.get(r8)
            if (r4 == 0) goto L_0x000b
            java.lang.Class r4 = (java.lang.Class) r4
        L_0x000a:
            return r4
        L_0x000b:
            java.util.Hashtable r5 = r7.map
            java.lang.Object r4 = r5.get(r8)
            boolean r5 = r4 instanceof gnu.bytecode.ClassType
            if (r5 == 0) goto L_0x0020
            r3 = r4
            gnu.bytecode.ClassType r3 = (gnu.bytecode.ClassType) r3
            boolean r5 = r3.isExisting()
            if (r5 == 0) goto L_0x0043
            java.lang.Class r4 = r3.reflectClass
        L_0x0020:
            boolean r5 = r4 instanceof byte[]
            if (r5 == 0) goto L_0x0050
            monitor-enter(r7)
            java.util.Hashtable r5 = r7.map     // Catch:{ all -> 0x004d }
            java.lang.Object r4 = r5.get(r8)     // Catch:{ all -> 0x004d }
            boolean r5 = r4 instanceof byte[]     // Catch:{ all -> 0x004d }
            if (r5 == 0) goto L_0x0048
            byte[] r4 = (byte[]) r4     // Catch:{ all -> 0x004d }
            r0 = r4
            byte[] r0 = (byte[]) r0     // Catch:{ all -> 0x004d }
            r1 = r0
            r5 = 0
            int r6 = r1.length     // Catch:{ all -> 0x004d }
            java.lang.Class r2 = r7.defineClass(r8, r1, r5, r6)     // Catch:{ all -> 0x004d }
            java.util.Hashtable r5 = r7.cmap     // Catch:{ all -> 0x004d }
            r5.put(r8, r2)     // Catch:{ all -> 0x004d }
        L_0x0040:
            monitor-exit(r7)     // Catch:{ all -> 0x004d }
        L_0x0041:
            r4 = r2
            goto L_0x000a
        L_0x0043:
            byte[] r4 = r3.writeToArray()
            goto L_0x0020
        L_0x0048:
            r0 = r4
            java.lang.Class r0 = (java.lang.Class) r0     // Catch:{ all -> 0x004d }
            r2 = r0
            goto L_0x0040
        L_0x004d:
            r5 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x004d }
            throw r5
        L_0x0050:
            if (r4 != 0) goto L_0x005b
            java.lang.ClassLoader r5 = r7.getParent()
            java.lang.Class r2 = r5.loadClass(r8)
            goto L_0x0041
        L_0x005b:
            r2 = r4
            java.lang.Class r2 = (java.lang.Class) r2
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.bytecode.ArrayClassLoader.loadClass(java.lang.String):java.lang.Class");
    }

    public static Package getContextPackage(String cname) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader instanceof ArrayClassLoader) {
                return ((ArrayClassLoader) loader).getPackage(cname);
            }
        } catch (SecurityException e) {
        }
        return Package.getPackage(cname);
    }
}
