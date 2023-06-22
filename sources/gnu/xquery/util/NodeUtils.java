package gnu.xquery.util;

import gnu.kawa.xml.Document;
import gnu.kawa.xml.KDocument;
import gnu.kawa.xml.KElement;
import gnu.kawa.xml.KNode;
import gnu.kawa.xml.Nodes;
import gnu.kawa.xml.SortedNodes;
import gnu.lists.Consumer;
import gnu.lists.PositionConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.text.Path;
import gnu.xml.NamespaceBinding;
import gnu.xml.NodeTree;
import gnu.xml.TextUtils;
import gnu.xml.XName;
import gnu.xquery.lang.XQuery;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Stack;

public class NodeUtils {
    static String collectionNamespace = "http://gnu.org/kawa/cached-collections";
    public static final Symbol collectionResolverSymbol = Symbol.make(XQuery.LOCAL_NAMESPACE, "collection-resolver", "qexo");

    public static Object nodeName(Object node) {
        if (node == Values.empty || node == null) {
            return node;
        }
        if (!(node instanceof KNode)) {
            throw new WrongType("node-name", 1, node, "node()?");
        }
        Symbol sym = ((KNode) node).getNodeSymbol();
        if (sym == null) {
            return Values.empty;
        }
        return sym;
    }

    public static String name(Object node) {
        Object name;
        if (node == Values.empty || node == null || (name = ((KNode) node).getNodeNameObject()) == null || name == Values.empty) {
            return "";
        }
        return name.toString();
    }

    public static String localName(Object node) {
        if (node == Values.empty || node == null) {
            return "";
        }
        if (!(node instanceof KNode)) {
            throw new WrongType("local-name", 1, node, "node()?");
        }
        Object name = ((KNode) node).getNodeNameObject();
        if (name == null || name == Values.empty) {
            return "";
        }
        if (name instanceof Symbol) {
            return ((Symbol) name).getName();
        }
        return name.toString();
    }

    public static Object namespaceURI(Object node) {
        if (!(node == Values.empty || node == null)) {
            if (!(node instanceof KNode)) {
                throw new WrongType("namespace-uri", 1, node, "node()?");
            }
            Object name = ((KNode) node).getNodeNameObject();
            if (name instanceof Symbol) {
                return QNameUtils.namespaceURIFromQName(name);
            }
        }
        return "";
    }

    public static void prefixesFromNodetype(XName name, Consumer out) {
        NamespaceBinding bindings = name.getNamespaceNodes();
        for (NamespaceBinding ns = bindings; ns != null; ns = ns.getNext()) {
            if (ns.getUri() != null) {
                String prefix = ns.getPrefix();
                NamespaceBinding ns2 = bindings;
                while (true) {
                    if (ns2 != ns) {
                        if (ns2.getPrefix() == prefix) {
                            break;
                        }
                        ns2 = ns2.getNext();
                    } else {
                        if (prefix == null) {
                            prefix = "";
                        }
                        out.writeObject(prefix);
                    }
                }
            }
        }
    }

    public static void inScopePrefixes$X(Object node, CallContext ctx) {
        Object type = ((KElement) node).getNodeNameObject();
        if (type instanceof XName) {
            prefixesFromNodetype((XName) type, ctx.consumer);
        } else {
            ctx.consumer.writeObject("xml");
        }
    }

    public static void data$X(Object arg, CallContext ctx) {
        Consumer out = ctx.consumer;
        if (arg instanceof Values) {
            Values vals = (Values) arg;
            int ipos = vals.startPos();
            while (true) {
                ipos = vals.nextPos(ipos);
                if (ipos != 0) {
                    out.writeObject(KNode.atomicValue(vals.getPosPrevious(ipos)));
                } else {
                    return;
                }
            }
        } else {
            out.writeObject(KNode.atomicValue(arg));
        }
    }

    public static Object root(Object arg) {
        if (arg == null || arg == Values.empty) {
            return arg;
        }
        if (!(arg instanceof KNode)) {
            throw new WrongType("root", 1, arg, "node()?");
        }
        KNode node = (KNode) arg;
        return Nodes.root((NodeTree) node.sequence, node.getPos());
    }

    public static KDocument rootDocument(Object arg) {
        if (!(arg instanceof KNode)) {
            throw new WrongType("root-document", 1, arg, "node()?");
        }
        KNode node = (KNode) arg;
        KNode node2 = Nodes.root((NodeTree) node.sequence, node.getPos());
        if (node2 instanceof KDocument) {
            return (KDocument) node2;
        }
        throw new WrongType("root-document", 1, arg, "document()");
    }

    public static String getLang(KNode node) {
        NodeTree seq = (NodeTree) node.sequence;
        int attr = seq.ancestorAttribute(node.ipos, NamespaceBinding.XML_NAMESPACE, "lang");
        if (attr == 0) {
            return null;
        }
        return KNode.getNodeValue(seq, attr);
    }

    public static boolean lang(Object testlang, Object node) {
        String teststr;
        if (testlang == null || testlang == Values.empty) {
            teststr = "";
        } else {
            teststr = TextUtils.stringValue(testlang);
        }
        String lang = getLang((KNode) node);
        if (lang == null) {
            return false;
        }
        int langlen = lang.length();
        int testlen = teststr.length();
        if (langlen > testlen && lang.charAt(testlen) == '-') {
            lang = lang.substring(0, testlen);
        }
        return lang.equalsIgnoreCase(teststr);
    }

    public static Object documentUri(Object arg) {
        if (arg == null || arg == Values.empty) {
            return arg;
        }
        if (!(arg instanceof KNode)) {
            throw new WrongType("xs:document-uri", 1, arg, "node()?");
        }
        KNode node = (KNode) arg;
        Object uri = ((NodeTree) node.sequence).documentUriOfPos(node.ipos);
        return uri == null ? Values.empty : uri;
    }

    public static Object nilled(Object arg) {
        if (arg == null || arg == Values.empty) {
            return arg;
        }
        if (!(arg instanceof KNode)) {
            throw new WrongType("nilled", 1, arg, "node()?");
        } else if (!(arg instanceof KElement)) {
            return Values.empty;
        } else {
            return Boolean.FALSE;
        }
    }

    public static Object baseUri(Object arg) {
        if (arg == null || arg == Values.empty) {
            return arg;
        }
        if (!(arg instanceof KNode)) {
            throw new WrongType("base-uri", 1, arg, "node()?");
        }
        Path uri = ((KNode) arg).baseURI();
        if (uri == null) {
            return Values.empty;
        }
        return uri;
    }

    static Object getIDs(Object arg, Object collector) {
        int start;
        Stack st;
        if (arg instanceof KNode) {
            arg = KNode.atomicValue(arg);
        }
        if (arg instanceof Values) {
            Object[] ar = ((Values) arg).getValues();
            int i = ar.length;
            while (true) {
                i--;
                if (i < 0) {
                    return collector;
                }
                collector = getIDs(ar[i], collector);
            }
        } else {
            String str = StringUtils.coerceToString(arg, "fn:id", 1, "");
            int len = str.length();
            int i2 = 0;
            Stack stack = collector;
            while (i2 < len) {
                int i3 = i2 + 1;
                char ch = str.charAt(i2);
                if (Character.isWhitespace(ch)) {
                    i2 = i3;
                } else {
                    if (XName.isNameStart(ch)) {
                        start = i3 - 1;
                    } else {
                        start = len;
                    }
                    while (i3 < len) {
                        char ch2 = str.charAt(i3);
                        if (Character.isWhitespace(ch2)) {
                            break;
                        }
                        i3++;
                        if (start < len && !XName.isNamePart(ch2)) {
                            start = len;
                        }
                    }
                    if (start < len) {
                        String ref = str.substring(start, i3);
                        if (stack == null) {
                            stack = ref;
                        } else {
                            if (stack instanceof Stack) {
                                st = (Stack) stack;
                            } else {
                                st = new Stack();
                                st.push(stack);
                                stack = st;
                            }
                            st.push(ref);
                        }
                    }
                    i2 = i3 + 1;
                }
            }
            return stack;
        }
    }

    public static void id$X(Object arg1, Object arg2, CallContext ctx) {
        KNode node = (KNode) arg2;
        NodeTree ntree = (NodeTree) node.sequence;
        KDocument kDocument = (KDocument) Nodes.root(ntree, node.ipos);
        Consumer out = ctx.consumer;
        Object idrefs = getIDs(arg1, (Object) null);
        if (idrefs != null) {
            ntree.makeIDtableIfNeeded();
            if ((out instanceof PositionConsumer) && ((idrefs instanceof String) || (out instanceof SortedNodes))) {
                idScan(idrefs, ntree, (PositionConsumer) out);
            } else if (idrefs instanceof String) {
                int pos = ntree.lookupID((String) idrefs);
                if (pos != -1) {
                    out.writeObject(KNode.make(ntree, pos));
                }
            } else {
                SortedNodes nodes = new SortedNodes();
                idScan(idrefs, ntree, nodes);
                Values.writeValues(nodes, out);
            }
        }
    }

    private static void idScan(Object ids, NodeTree seq, PositionConsumer out) {
        if (ids instanceof String) {
            int pos = seq.lookupID((String) ids);
            if (pos != -1) {
                out.writePosition(seq, pos);
            }
        } else if (ids instanceof Stack) {
            Stack st = (Stack) ids;
            int n = st.size();
            for (int i = 0; i < n; i++) {
                idScan(st.elementAt(i), seq, out);
            }
        }
    }

    public static Object idref(Object arg1, Object arg2) {
        KNode node = (KNode) arg2;
        KDocument kDocument = (KDocument) Nodes.root((NodeTree) node.sequence, node.getPos());
        return Values.empty;
    }

    public static void setSavedCollection(Object uri, Object value, Environment env) {
        if (uri == null) {
            uri = "#default";
        }
        env.put(Symbol.make(collectionNamespace, uri.toString()), (Object) null, value);
    }

    public static void setSavedCollection(Object uri, Object value) {
        setSavedCollection(uri, value, Environment.getCurrent());
    }

    public static Object getSavedCollection(Object uri, Environment env) {
        if (uri == null) {
            uri = "#default";
        }
        Object coll = env.get(Symbol.make(collectionNamespace, uri.toString()), (Object) null, (Object) null);
        if (coll != null) {
            return coll;
        }
        throw new RuntimeException("collection '" + uri + "' not found");
    }

    public static Object getSavedCollection(Object uri) {
        return getSavedCollection(uri, Environment.getCurrent());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0033, code lost:
        r9 = r8.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object collection(java.lang.Object r14, java.lang.Object r15) throws java.lang.Throwable {
        /*
            r13 = 0
            r12 = 0
            java.lang.String r10 = "collection"
            java.lang.Object r14 = resolve(r14, r15, r10)
            gnu.mapping.Environment r2 = gnu.mapping.Environment.getCurrent()
            gnu.mapping.Symbol r7 = collectionResolverSymbol
            java.lang.Object r8 = r2.get(r7, r12, r12)
            if (r8 != 0) goto L_0x0024
            java.lang.String r10 = r7.getLocalName()
            java.lang.String r11 = r7.getPrefix()
            gnu.mapping.Symbol r10 = gnu.mapping.Symbol.makeWithUnknownNamespace(r10, r11)
            java.lang.Object r8 = r2.get(r10, r12, r12)
        L_0x0024:
            if (r8 != 0) goto L_0x002b
            java.lang.Object r10 = getSavedCollection(r14)
        L_0x002a:
            return r10
        L_0x002b:
            boolean r10 = r8 instanceof java.lang.String
            if (r10 != 0) goto L_0x0033
            boolean r10 = r8 instanceof gnu.kawa.xml.UntypedAtomic
            if (r10 == 0) goto L_0x00b8
        L_0x0033:
            java.lang.String r9 = r8.toString()
            r10 = 58
            int r1 = r9.indexOf(r10)
            if (r1 <= 0) goto L_0x00b8
            java.lang.String r0 = r9.substring(r13, r1)
            int r10 = r1 + 1
            java.lang.String r4 = r9.substring(r10)
            java.lang.Class r5 = java.lang.Class.forName(r0)     // Catch:{ ClassNotFoundException -> 0x007e, Throwable -> 0x009e }
            gnu.bytecode.Type r6 = gnu.bytecode.ClassType.make(r5)
            gnu.bytecode.ClassType r6 = (gnu.bytecode.ClassType) r6
            gnu.xquery.lang.XQuery r10 = gnu.xquery.lang.XQuery.instance
            gnu.mapping.MethodProc r8 = gnu.kawa.reflect.ClassMethods.apply(r6, r4, r13, r10)
            if (r8 != 0) goto L_0x00b8
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "invalid collection-resolver: no method "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r4)
            java.lang.String r12 = " in "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        L_0x007e:
            r3 = move-exception
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "invalid collection-resolver: class "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r12 = " not found"
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        L_0x009e:
            r3 = move-exception
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "invalid collection-resolver: "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r3)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        L_0x00b8:
            boolean r10 = r8 instanceof gnu.mapping.Procedure
            if (r10 != 0) goto L_0x00d5
            java.lang.RuntimeException r10 = new java.lang.RuntimeException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "invalid collection-resolver: "
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.StringBuilder r11 = r11.append(r8)
            java.lang.String r11 = r11.toString()
            r10.<init>(r11)
            throw r10
        L_0x00d5:
            gnu.mapping.Procedure r8 = (gnu.mapping.Procedure) r8
            java.lang.Object r10 = r8.apply1(r14)
            goto L_0x002a
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xquery.util.NodeUtils.collection(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    static Object resolve(Object uri, Object base, String fname) throws Throwable {
        if (!(uri instanceof File) && !(uri instanceof Path) && !(uri instanceof URI) && !(uri instanceof URL)) {
            uri = StringUtils.coerceToString(uri, fname, 1, (String) null);
        }
        if (uri == Values.empty || uri == null) {
            return null;
        }
        return Path.currentPath().resolve(Path.valueOf(uri));
    }

    public static Object docCached(Object uri, Object base) throws Throwable {
        Object uri2 = resolve(uri, base, "doc");
        if (uri2 == null) {
            return Values.empty;
        }
        return Document.parseCached(uri2);
    }

    public static boolean availableCached(Object uri, Object base) throws Throwable {
        Object uri2 = resolve(uri, base, "doc-available");
        if (uri2 == null) {
            return false;
        }
        try {
            Document.parseCached(uri2);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }
}
