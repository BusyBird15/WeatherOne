package gnu.kawa.xml;

import gnu.lists.Consumer;
import gnu.lists.XConsumer;
import gnu.mapping.ThreadLocation;
import gnu.text.Path;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import gnu.xml.NodeTree;
import gnu.xml.XMLParser;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class Document {
    private static HashMap cache = new HashMap();
    private static ThreadLocation docMapLocation = new ThreadLocation("document-map");
    public static final Document document = new Document();

    public static void parse(Object name, Consumer out) throws Throwable {
        SourceMessages messages = new SourceMessages();
        if (out instanceof XConsumer) {
            ((XConsumer) out).beginEntity(name);
        }
        XMLParser.parse(name, messages, out);
        if (messages.seenErrors()) {
            throw new SyntaxException("document function read invalid XML", messages);
        } else if (out instanceof XConsumer) {
            ((XConsumer) out).endEntity();
        }
    }

    public static KDocument parse(Object uri) throws Throwable {
        NodeTree tree = new NodeTree();
        parse(uri, tree);
        return new KDocument(tree, 10);
    }

    private static class DocReference extends SoftReference {
        static ReferenceQueue queue = new ReferenceQueue();
        Path key;

        public DocReference(Path key2, KDocument doc) {
            super(doc, queue);
            this.key = key2;
        }
    }

    public static void clearLocalCache() {
        docMapLocation.getLocation().set((Object) null);
    }

    public static void clearSoftCache() {
        cache = new HashMap();
    }

    public static KDocument parseCached(Object uri) throws Throwable {
        return parseCached(Path.valueOf(uri));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
        r5 = (gnu.kawa.xml.Document.DocReference) cache.get(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0042, code lost:
        if (r5 == null) goto L_0x0051;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0044, code lost:
        r0 = (gnu.kawa.xml.KDocument) r5.get();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004a, code lost:
        if (r0 != null) goto L_0x0064;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004c, code lost:
        cache.remove(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0051, code lost:
        r0 = parse(r9);
        r3.put(r9, r0);
        cache.put(r9, new gnu.kawa.xml.Document.DocReference(r9, r0));
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0064, code lost:
        r3.put(r9, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0067, code lost:
        r1 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:5:0x000d, code lost:
        r2 = docMapLocation.getLocation();
        r3 = (java.util.Hashtable) r2.get((java.lang.Object) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        if (r3 != null) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001c, code lost:
        r3 = new java.util.Hashtable();
        r2.set(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0024, code lost:
        r0 = (gnu.kawa.xml.KDocument) r3.get(r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002a, code lost:
        if (r0 == null) goto L_0x003a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized gnu.kawa.xml.KDocument parseCached(gnu.text.Path r9) throws java.lang.Throwable {
        /*
            java.lang.Class<gnu.kawa.xml.Document> r7 = gnu.kawa.xml.Document.class
            monitor-enter(r7)
        L_0x0003:
            java.lang.ref.ReferenceQueue r6 = gnu.kawa.xml.Document.DocReference.queue     // Catch:{ all -> 0x0037 }
            java.lang.ref.Reference r4 = r6.poll()     // Catch:{ all -> 0x0037 }
            gnu.kawa.xml.Document$DocReference r4 = (gnu.kawa.xml.Document.DocReference) r4     // Catch:{ all -> 0x0037 }
            if (r4 != 0) goto L_0x002f
            gnu.mapping.ThreadLocation r6 = docMapLocation     // Catch:{ all -> 0x0037 }
            gnu.mapping.NamedLocation r2 = r6.getLocation()     // Catch:{ all -> 0x0037 }
            r6 = 0
            java.lang.Object r3 = r2.get(r6)     // Catch:{ all -> 0x0037 }
            java.util.Hashtable r3 = (java.util.Hashtable) r3     // Catch:{ all -> 0x0037 }
            if (r3 != 0) goto L_0x0024
            java.util.Hashtable r3 = new java.util.Hashtable     // Catch:{ all -> 0x0037 }
            r3.<init>()     // Catch:{ all -> 0x0037 }
            r2.set(r3)     // Catch:{ all -> 0x0037 }
        L_0x0024:
            java.lang.Object r0 = r3.get(r9)     // Catch:{ all -> 0x0037 }
            gnu.kawa.xml.KDocument r0 = (gnu.kawa.xml.KDocument) r0     // Catch:{ all -> 0x0037 }
            if (r0 == 0) goto L_0x003a
            r1 = r0
        L_0x002d:
            monitor-exit(r7)
            return r1
        L_0x002f:
            java.util.HashMap r6 = cache     // Catch:{ all -> 0x0037 }
            gnu.text.Path r8 = r4.key     // Catch:{ all -> 0x0037 }
            r6.remove(r8)     // Catch:{ all -> 0x0037 }
            goto L_0x0003
        L_0x0037:
            r6 = move-exception
            monitor-exit(r7)
            throw r6
        L_0x003a:
            java.util.HashMap r6 = cache     // Catch:{ all -> 0x0037 }
            java.lang.Object r5 = r6.get(r9)     // Catch:{ all -> 0x0037 }
            gnu.kawa.xml.Document$DocReference r5 = (gnu.kawa.xml.Document.DocReference) r5     // Catch:{ all -> 0x0037 }
            if (r5 == 0) goto L_0x0051
            java.lang.Object r0 = r5.get()     // Catch:{ all -> 0x0037 }
            gnu.kawa.xml.KDocument r0 = (gnu.kawa.xml.KDocument) r0     // Catch:{ all -> 0x0037 }
            if (r0 != 0) goto L_0x0064
            java.util.HashMap r6 = cache     // Catch:{ all -> 0x0037 }
            r6.remove(r9)     // Catch:{ all -> 0x0037 }
        L_0x0051:
            gnu.kawa.xml.KDocument r0 = parse(r9)     // Catch:{ all -> 0x0037 }
            r3.put(r9, r0)     // Catch:{ all -> 0x0037 }
            java.util.HashMap r6 = cache     // Catch:{ all -> 0x0037 }
            gnu.kawa.xml.Document$DocReference r8 = new gnu.kawa.xml.Document$DocReference     // Catch:{ all -> 0x0037 }
            r8.<init>(r9, r0)     // Catch:{ all -> 0x0037 }
            r6.put(r9, r8)     // Catch:{ all -> 0x0037 }
            r1 = r0
            goto L_0x002d
        L_0x0064:
            r3.put(r9, r0)     // Catch:{ all -> 0x0037 }
            r1 = r0
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.xml.Document.parseCached(gnu.text.Path):gnu.kawa.xml.KDocument");
    }
}
