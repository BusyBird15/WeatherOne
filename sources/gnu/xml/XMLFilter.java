package gnu.xml;

import gnu.expr.Keyword;
import gnu.lists.AbstractSequence;
import gnu.lists.CharSeq;
import gnu.lists.Consumer;
import gnu.lists.PositionConsumer;
import gnu.lists.SeqPosition;
import gnu.lists.TreeList;
import gnu.lists.UnescapedData;
import gnu.lists.XConsumer;
import gnu.mapping.Symbol;
import gnu.text.Char;
import gnu.text.LineBufferedReader;
import gnu.text.SourceLocator;
import gnu.text.SourceMessages;
import java.util.List;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DocumentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class XMLFilter implements DocumentHandler, ContentHandler, SourceLocator, XConsumer, PositionConsumer {
    public static final int COPY_NAMESPACES_INHERIT = 2;
    public static final int COPY_NAMESPACES_PRESERVE = 1;
    private static final int SAW_KEYWORD = 1;
    private static final int SAW_WORD = 2;
    int attrCount = -1;
    String attrLocalName;
    String attrPrefix;
    Consumer base;
    public transient int copyNamespacesMode = 1;
    String currentNamespacePrefix;
    protected int ignoringLevel;
    LineBufferedReader in;
    boolean inStartTag;
    SourceLocator locator;
    MappingInfo[] mappingTable = new MappingInfo[128];
    int mappingTableMask = (this.mappingTable.length - 1);
    private SourceMessages messages;
    boolean mismatchReported;
    NamespaceBinding namespaceBindings;
    public boolean namespacePrefixes = false;
    protected int nesting;
    public Consumer out;
    int previous = 0;
    int[] startIndexes = null;
    protected int stringizingElementNesting = -1;
    protected int stringizingLevel;
    TreeList tlist;
    Object[] workStack;

    public void setSourceLocator(LineBufferedReader in2) {
        this.in = in2;
        this.locator = this;
    }

    public void setSourceLocator(SourceLocator locator2) {
        this.locator = locator2;
    }

    public void setMessages(SourceMessages messages2) {
        this.messages = messages2;
    }

    public NamespaceBinding findNamespaceBinding(String prefix, String uri, NamespaceBinding oldBindings) {
        NamespaceBinding namespaces;
        int hash = uri == null ? 0 : uri.hashCode();
        if (prefix != null) {
            hash ^= prefix.hashCode();
        }
        int bucket = hash & this.mappingTableMask;
        for (MappingInfo info = this.mappingTable[bucket]; info != null; info = info.nextInBucket) {
            if (info.tagHash == hash && info.prefix == prefix && (namespaces = info.namespaces) != null && namespaces.getNext() == this.namespaceBindings && namespaces.getPrefix() == prefix && info.uri == uri) {
                return info.namespaces;
            }
        }
        MappingInfo info2 = new MappingInfo();
        info2.nextInBucket = this.mappingTable[bucket];
        this.mappingTable[bucket] = info2;
        info2.tagHash = hash;
        info2.prefix = prefix;
        info2.local = uri;
        info2.uri = uri;
        if (uri == "") {
            uri = null;
        }
        info2.namespaces = new NamespaceBinding(prefix, uri, oldBindings);
        return info2.namespaces;
    }

    public MappingInfo lookupNamespaceBinding(String prefix, char[] uriChars, int uriStart, int uriLength, int uriHash, NamespaceBinding oldBindings) {
        int hash;
        NamespaceBinding namespaces;
        if (prefix == null) {
            hash = uriHash;
        } else {
            hash = prefix.hashCode() ^ uriHash;
        }
        int bucket = hash & this.mappingTableMask;
        for (MappingInfo info = this.mappingTable[bucket]; info != null; info = info.nextInBucket) {
            if (info.tagHash == hash && info.prefix == prefix && (namespaces = info.namespaces) != null && namespaces.getNext() == this.namespaceBindings && namespaces.getPrefix() == prefix && MappingInfo.equals(info.uri, uriChars, uriStart, uriLength)) {
                return info;
            }
        }
        MappingInfo info2 = new MappingInfo();
        info2.nextInBucket = this.mappingTable[bucket];
        this.mappingTable[bucket] = info2;
        String uri = new String(uriChars, uriStart, uriLength).intern();
        info2.tagHash = hash;
        info2.prefix = prefix;
        info2.local = uri;
        info2.uri = uri;
        if (uri == "") {
            uri = null;
        }
        info2.namespaces = new NamespaceBinding(prefix, uri, oldBindings);
        return info2;
    }

    public void endAttribute() {
        if (this.attrLocalName != null) {
            if (this.previous == 1) {
                this.previous = 0;
                return;
            }
            if (this.stringizingElementNesting >= 0) {
                this.ignoringLevel--;
            }
            int i = this.stringizingLevel - 1;
            this.stringizingLevel = i;
            if (i == 0) {
                if (this.attrLocalName == "id" && this.attrPrefix == "xml") {
                    int valStart = this.startIndexes[this.attrCount - 1] + 5;
                    int valEnd = this.tlist.gapStart;
                    char[] data = this.tlist.data;
                    int i2 = valStart;
                    while (true) {
                        if (i2 >= valEnd) {
                            break;
                        }
                        int i3 = i2 + 1;
                        char datum = data[i2];
                        if ((65535 & datum) > 40959 || datum == 9 || datum == 13 || datum == 10 || (datum == ' ' && (i3 == valEnd || data[i3] == ' '))) {
                            StringBuffer sbuf = new StringBuffer();
                            this.tlist.stringValue(valStart, valEnd, sbuf);
                            this.tlist.gapStart = valStart;
                            this.tlist.write(TextUtils.replaceWhitespace(sbuf.toString(), true));
                        } else {
                            i2 = i3;
                        }
                    }
                    StringBuffer sbuf2 = new StringBuffer();
                    this.tlist.stringValue(valStart, valEnd, sbuf2);
                    this.tlist.gapStart = valStart;
                    this.tlist.write(TextUtils.replaceWhitespace(sbuf2.toString(), true));
                }
                this.attrLocalName = null;
                this.attrPrefix = null;
                if (this.currentNamespacePrefix == null || this.namespacePrefixes) {
                    this.tlist.endAttribute();
                }
                if (this.currentNamespacePrefix != null) {
                    int attrStart = this.startIndexes[this.attrCount - 1];
                    int uriStart = attrStart;
                    int uriEnd = this.tlist.gapStart;
                    int uriLength = uriEnd - uriStart;
                    char[] data2 = this.tlist.data;
                    int uriHash = 0;
                    int i4 = uriStart;
                    while (true) {
                        if (i4 >= uriEnd) {
                            break;
                        }
                        char datum2 = data2[i4];
                        if ((65535 & datum2) > 40959) {
                            StringBuffer sbuf3 = new StringBuffer();
                            this.tlist.stringValue(uriStart, uriEnd, sbuf3);
                            uriHash = sbuf3.hashCode();
                            uriStart = 0;
                            uriLength = sbuf3.length();
                            data2 = new char[sbuf3.length()];
                            sbuf3.getChars(0, uriLength, data2, 0);
                            break;
                        }
                        uriHash = (uriHash * 31) + datum2;
                        i4++;
                    }
                    this.tlist.gapStart = attrStart;
                    this.namespaceBindings = lookupNamespaceBinding(this.currentNamespacePrefix == "" ? null : this.currentNamespacePrefix, data2, uriStart, uriLength, uriHash, this.namespaceBindings).namespaces;
                    this.currentNamespacePrefix = null;
                }
            }
        }
    }

    private String resolve(String prefix, boolean isAttribute) {
        if (isAttribute && prefix == null) {
            return "";
        }
        String uri = this.namespaceBindings.resolve(prefix);
        if (uri != null) {
            return uri;
        }
        if (prefix != null) {
            error('e', "unknown namespace prefix '" + prefix + '\'');
        }
        return "";
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: gnu.lists.TreeList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: gnu.lists.TreeList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r27v0, resolved type: gnu.mapping.Symbol} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r27v1, resolved type: gnu.xml.XName} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r27v2, resolved type: gnu.mapping.Symbol} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r27v6, resolved type: gnu.xml.XName} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r25v3, resolved type: gnu.mapping.Symbol} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v25, resolved type: gnu.lists.TreeList} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x02f7, code lost:
        if (r27 != null) goto L_0x020b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x02f9, code lost:
        r0 = new gnu.xml.XName(r9.qname, r3);
        r27 = r0;
        r9.type = r0;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void closeStartTag() {
        /*
            r34 = this;
            r0 = r34
            int r0 = r0.attrCount
            r30 = r0
            if (r30 < 0) goto L_0x0010
            r0 = r34
            int r0 = r0.stringizingLevel
            r30 = r0
            if (r30 <= 0) goto L_0x0011
        L_0x0010:
            return
        L_0x0011:
            r30 = 0
            r0 = r30
            r1 = r34
            r1.inStartTag = r0
            r30 = 0
            r0 = r30
            r1 = r34
            r1.previous = r0
            r0 = r34
            java.lang.String r0 = r0.attrLocalName
            r30 = r0
            if (r30 == 0) goto L_0x002c
            r34.endAttribute()
        L_0x002c:
            r0 = r34
            int r0 = r0.nesting
            r30 = r0
            if (r30 != 0) goto L_0x0088
            gnu.xml.NamespaceBinding r19 = gnu.xml.NamespaceBinding.predefinedXML
        L_0x0036:
            r0 = r34
            gnu.xml.NamespaceBinding r3 = r0.namespaceBindings
            r7 = 0
        L_0x003b:
            r0 = r34
            int r0 = r0.attrCount
            r30 = r0
            r0 = r30
            if (r7 > r0) goto L_0x014c
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + r7
            int r31 = r31 + -1
            r21 = r30[r31]
            r0 = r21
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r30 = r0
            if (r30 == 0) goto L_0x0085
            r23 = r21
            gnu.mapping.Symbol r23 = (gnu.mapping.Symbol) r23
            java.lang.String r20 = r23.getPrefix()
            java.lang.String r30 = ""
            r0 = r20
            r1 = r30
            if (r0 != r1) goto L_0x0071
            r20 = 0
        L_0x0071:
            java.lang.String r28 = r23.getNamespaceURI()
            java.lang.String r30 = ""
            r0 = r28
            r1 = r30
            if (r0 != r1) goto L_0x007f
            r28 = 0
        L_0x007f:
            if (r7 <= 0) goto L_0x009d
            if (r20 != 0) goto L_0x009d
            if (r28 != 0) goto L_0x009d
        L_0x0085:
            int r7 = r7 + 1
            goto L_0x003b
        L_0x0088:
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + -2
            r30 = r30[r31]
            gnu.xml.NamespaceBinding r30 = (gnu.xml.NamespaceBinding) r30
            r19 = r30
            goto L_0x0036
        L_0x009d:
            r11 = 0
            r15 = r3
        L_0x009f:
            r0 = r19
            if (r15 != r0) goto L_0x00a4
            r11 = 1
        L_0x00a4:
            if (r15 != 0) goto L_0x00b5
            if (r20 != 0) goto L_0x00aa
            if (r28 == 0) goto L_0x0085
        L_0x00aa:
            r0 = r34
            r1 = r20
            r2 = r28
            gnu.xml.NamespaceBinding r3 = r0.findNamespaceBinding(r1, r2, r3)
            goto L_0x0085
        L_0x00b5:
            java.lang.String r0 = r15.prefix
            r30 = r0
            r0 = r30
            r1 = r20
            if (r0 != r1) goto L_0x0148
            java.lang.String r0 = r15.uri
            r30 = r0
            r0 = r30
            r1 = r28
            if (r0 == r1) goto L_0x0085
            if (r11 == 0) goto L_0x00d6
            r0 = r34
            r1 = r20
            r2 = r28
            gnu.xml.NamespaceBinding r3 = r0.findNamespaceBinding(r1, r2, r3)
            goto L_0x0085
        L_0x00d6:
            r16 = r3
        L_0x00d8:
            if (r16 != 0) goto L_0x0127
            r12 = 1
        L_0x00db:
            java.lang.StringBuilder r30 = new java.lang.StringBuilder
            r30.<init>()
            java.lang.String r31 = "_ns_"
            java.lang.StringBuilder r30 = r30.append(r31)
            r0 = r30
            java.lang.StringBuilder r30 = r0.append(r12)
            java.lang.String r30 = r30.toString()
            java.lang.String r14 = r30.intern()
            java.lang.String r30 = r3.resolve(r14)
            if (r30 != 0) goto L_0x0124
        L_0x00fa:
            r0 = r34
            r1 = r28
            gnu.xml.NamespaceBinding r3 = r0.findNamespaceBinding(r14, r1, r3)
            java.lang.String r13 = r23.getLocalName()
            if (r28 != 0) goto L_0x010a
            java.lang.String r28 = ""
        L_0x010a:
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + r7
            int r31 = r31 + -1
            r0 = r28
            gnu.mapping.Symbol r32 = gnu.mapping.Symbol.make(r0, r13, r14)
            r30[r31] = r32
            goto L_0x0085
        L_0x0124:
            int r12 = r12 + 1
            goto L_0x00db
        L_0x0127:
            r0 = r16
            java.lang.String r0 = r0.uri
            r30 = r0
            r0 = r30
            r1 = r28
            if (r0 != r1) goto L_0x0141
            r0 = r16
            java.lang.String r14 = r0.prefix
            java.lang.String r30 = r3.resolve(r14)
            r0 = r30
            r1 = r28
            if (r0 == r1) goto L_0x00fa
        L_0x0141:
            r0 = r16
            gnu.xml.NamespaceBinding r0 = r0.next
            r16 = r0
            goto L_0x00d8
        L_0x0148:
            gnu.xml.NamespaceBinding r15 = r15.next
            goto L_0x009f
        L_0x014c:
            r7 = 0
        L_0x014d:
            r0 = r34
            int r0 = r0.attrCount
            r30 = r0
            r0 = r30
            if (r7 > r0) goto L_0x0458
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + r7
            int r31 = r31 + -1
            r21 = r30[r31]
            r10 = 0
            r0 = r21
            boolean r0 = r0 instanceof gnu.xml.MappingInfo
            r30 = r0
            if (r30 != 0) goto L_0x0184
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r31 = r0
            r0 = r30
            r1 = r31
            if (r0 != r1) goto L_0x0335
        L_0x0184:
            r0 = r21
            boolean r0 = r0 instanceof gnu.xml.MappingInfo
            r30 = r0
            if (r30 == 0) goto L_0x0299
            r9 = r21
            gnu.xml.MappingInfo r9 = (gnu.xml.MappingInfo) r9
            java.lang.String r0 = r9.prefix
            r20 = r0
            java.lang.String r13 = r9.local
            if (r7 <= 0) goto L_0x0286
            if (r20 != 0) goto L_0x01a0
            java.lang.String r30 = "xmlns"
            r0 = r30
            if (r13 == r0) goto L_0x01a8
        L_0x01a0:
            java.lang.String r30 = "xmlns"
            r0 = r20
            r1 = r30
            if (r0 != r1) goto L_0x0286
        L_0x01a8:
            r10 = 1
            java.lang.String r28 = "(namespace-node)"
        L_0x01ab:
            int r6 = r9.tagHash
            r0 = r34
            int r0 = r0.mappingTableMask
            r30 = r0
            r4 = r6 & r30
            r0 = r34
            gnu.xml.MappingInfo[] r0 = r0.mappingTable
            r30 = r0
            r9 = r30[r4]
            r26 = 0
        L_0x01bf:
            if (r9 != 0) goto L_0x02b1
            r9 = r26
            gnu.xml.MappingInfo r9 = new gnu.xml.MappingInfo
            r9.<init>()
            r9.tagHash = r6
            r0 = r20
            r9.prefix = r0
            r9.local = r13
            r0 = r34
            gnu.xml.MappingInfo[] r0 = r0.mappingTable
            r30 = r0
            r30 = r30[r4]
            r0 = r30
            r9.nextInBucket = r0
            r0 = r34
            gnu.xml.MappingInfo[] r0 = r0.mappingTable
            r30 = r0
            r30[r4] = r9
            r0 = r28
            r9.uri = r0
            r0 = r28
            r1 = r20
            gnu.mapping.Symbol r30 = gnu.mapping.Symbol.make(r0, r13, r1)
            r0 = r30
            r9.qname = r0
            if (r7 != 0) goto L_0x020b
            gnu.xml.XName r29 = new gnu.xml.XName
            gnu.mapping.Symbol r0 = r9.qname
            r30 = r0
            r0 = r29
            r1 = r30
            r0.<init>(r1, r3)
            r27 = r29
            r0 = r29
            r9.type = r0
            r9.namespaces = r3
        L_0x020b:
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + r7
            int r31 = r31 + -1
            r30[r31] = r9
        L_0x021d:
            r12 = 1
        L_0x021e:
            if (r12 >= r7) goto L_0x0356
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + r12
            int r31 = r31 + -1
            r18 = r30[r31]
            r0 = r18
            boolean r0 = r0 instanceof gnu.mapping.Symbol
            r30 = r0
            if (r30 == 0) goto L_0x0344
            r17 = r18
            gnu.mapping.Symbol r17 = (gnu.mapping.Symbol) r17
        L_0x023e:
            java.lang.String r30 = r17.getLocalPart()
            r0 = r30
            if (r13 != r0) goto L_0x0283
            java.lang.String r30 = r17.getNamespaceURI()
            r0 = r28
            r1 = r30
            if (r0 != r1) goto L_0x0283
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + -1
            r25 = r30[r31]
            r0 = r25
            boolean r0 = r0 instanceof gnu.xml.MappingInfo
            r30 = r0
            if (r30 == 0) goto L_0x0270
            gnu.xml.MappingInfo r25 = (gnu.xml.MappingInfo) r25
            r0 = r25
            gnu.mapping.Symbol r0 = r0.qname
            r25 = r0
        L_0x0270:
            r30 = 101(0x65, float:1.42E-43)
            r0 = r17
            r1 = r25
            java.lang.String r31 = duplicateAttributeMessage(r0, r1)
            r0 = r34
            r1 = r30
            r2 = r31
            r0.error(r1, r2)
        L_0x0283:
            int r12 = r12 + 1
            goto L_0x021e
        L_0x0286:
            if (r7 <= 0) goto L_0x0296
            r30 = 1
        L_0x028a:
            r0 = r34
            r1 = r20
            r2 = r30
            java.lang.String r28 = r0.resolve(r1, r2)
            goto L_0x01ab
        L_0x0296:
            r30 = 0
            goto L_0x028a
        L_0x0299:
            r24 = r21
            gnu.mapping.Symbol r24 = (gnu.mapping.Symbol) r24
            r0 = r34
            r1 = r24
            gnu.xml.MappingInfo r9 = r0.lookupTag(r1)
            java.lang.String r0 = r9.prefix
            r20 = r0
            java.lang.String r13 = r9.local
            java.lang.String r28 = r24.getNamespaceURI()
            goto L_0x01ab
        L_0x02b1:
            int r0 = r9.tagHash
            r30 = r0
            r0 = r30
            if (r0 != r6) goto L_0x0318
            java.lang.String r0 = r9.local
            r30 = r0
            r0 = r30
            if (r0 != r13) goto L_0x0318
            java.lang.String r0 = r9.prefix
            r30 = r0
            r0 = r30
            r1 = r20
            if (r0 != r1) goto L_0x0318
            java.lang.String r0 = r9.uri
            r30 = r0
            if (r30 != 0) goto L_0x030e
            r0 = r28
            r9.uri = r0
            r0 = r28
            r1 = r20
            gnu.mapping.Symbol r30 = gnu.mapping.Symbol.make(r0, r13, r1)
            r0 = r30
            r9.qname = r0
        L_0x02e1:
            if (r7 != 0) goto L_0x032f
            gnu.xml.NamespaceBinding r0 = r9.namespaces
            r30 = r0
            r0 = r30
            if (r0 == r3) goto L_0x02f1
            gnu.xml.NamespaceBinding r0 = r9.namespaces
            r30 = r0
            if (r30 != 0) goto L_0x0318
        L_0x02f1:
            gnu.xml.XName r0 = r9.type
            r27 = r0
            r9.namespaces = r3
            if (r27 != 0) goto L_0x020b
            gnu.xml.XName r29 = new gnu.xml.XName
            gnu.mapping.Symbol r0 = r9.qname
            r30 = r0
            r0 = r29
            r1 = r30
            r0.<init>(r1, r3)
            r27 = r29
            r0 = r29
            r9.type = r0
            goto L_0x020b
        L_0x030e:
            java.lang.String r0 = r9.uri
            r30 = r0
            r0 = r30
            r1 = r28
            if (r0 == r1) goto L_0x031c
        L_0x0318:
            gnu.xml.MappingInfo r9 = r9.nextInBucket
            goto L_0x01bf
        L_0x031c:
            gnu.mapping.Symbol r0 = r9.qname
            r30 = r0
            if (r30 != 0) goto L_0x02e1
            r0 = r28
            r1 = r20
            gnu.mapping.Symbol r30 = gnu.mapping.Symbol.make(r0, r13, r1)
            r0 = r30
            r9.qname = r0
            goto L_0x02e1
        L_0x032f:
            gnu.mapping.Symbol r0 = r9.qname
            r27 = r0
            goto L_0x020b
        L_0x0335:
            r23 = r21
            gnu.mapping.Symbol r23 = (gnu.mapping.Symbol) r23
            java.lang.String r28 = r23.getNamespaceURI()
            java.lang.String r13 = r23.getLocalName()
            r9 = 0
            goto L_0x021d
        L_0x0344:
            r0 = r18
            boolean r0 = r0 instanceof gnu.xml.MappingInfo
            r30 = r0
            if (r30 == 0) goto L_0x0283
            gnu.xml.MappingInfo r18 = (gnu.xml.MappingInfo) r18
            r0 = r18
            gnu.mapping.Symbol r0 = r0.qname
            r17 = r0
            goto L_0x023e
        L_0x0356:
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r31 = r0
            r0 = r30
            r1 = r31
            if (r0 != r1) goto L_0x03dc
            if (r7 != 0) goto L_0x03b5
            gnu.xml.XName r0 = r9.type
            r27 = r0
        L_0x036e:
            int r8 = r9.index
            if (r8 <= 0) goto L_0x0386
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r30 = r0
            r0 = r30
            java.lang.Object[] r0 = r0.objects
            r30 = r0
            r30 = r30[r8]
            r0 = r30
            r1 = r27
            if (r0 == r1) goto L_0x0396
        L_0x0386:
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r30 = r0
            r0 = r30
            r1 = r27
            int r8 = r0.find(r1)
            r9.index = r8
        L_0x0396:
            if (r7 != 0) goto L_0x03ba
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r30 = r0
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r31 = r0
            r0 = r31
            int r0 = r0.gapEnd
            r31 = r0
            r0 = r30
            r1 = r31
            r0.setElementName(r1, r8)
        L_0x03b1:
            int r7 = r7 + 1
            goto L_0x014d
        L_0x03b5:
            gnu.mapping.Symbol r0 = r9.qname
            r27 = r0
            goto L_0x036e
        L_0x03ba:
            if (r10 == 0) goto L_0x03c4
            r0 = r34
            boolean r0 = r0.namespacePrefixes
            r30 = r0
            if (r30 == 0) goto L_0x03b1
        L_0x03c4:
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r30 = r0
            r0 = r34
            int[] r0 = r0.startIndexes
            r31 = r0
            int r32 = r7 + -1
            r31 = r31[r32]
            r0 = r30
            r1 = r31
            r0.setAttributeName(r1, r8)
            goto L_0x03b1
        L_0x03dc:
            if (r9 != 0) goto L_0x03f0
            r27 = r21
        L_0x03e0:
            if (r7 != 0) goto L_0x03fc
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r0 = r30
            r1 = r27
            r0.startElement(r1)
            goto L_0x03b1
        L_0x03f0:
            if (r7 != 0) goto L_0x03f7
            gnu.xml.XName r0 = r9.type
            r27 = r0
            goto L_0x03e0
        L_0x03f7:
            gnu.mapping.Symbol r0 = r9.qname
            r27 = r0
            goto L_0x03e0
        L_0x03fc:
            if (r10 == 0) goto L_0x0406
            r0 = r34
            boolean r0 = r0.namespacePrefixes
            r30 = r0
            if (r30 == 0) goto L_0x03b1
        L_0x0406:
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r0 = r30
            r1 = r27
            r0.startAttribute(r1)
            r0 = r34
            int[] r0 = r0.startIndexes
            r30 = r0
            int r31 = r7 + -1
            r22 = r30[r31]
            r0 = r34
            int r0 = r0.attrCount
            r30 = r0
            r0 = r30
            if (r7 >= r0) goto L_0x044d
            r0 = r34
            int[] r0 = r0.startIndexes
            r30 = r0
            r5 = r30[r7]
        L_0x042f:
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r30 = r0
            int r31 = r22 + 5
            int r32 = r5 + -1
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r33 = r0
            r30.consumeIRange(r31, r32, r33)
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r30.endAttribute()
            goto L_0x03b1
        L_0x044d:
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r30 = r0
            r0 = r30
            int r5 = r0.gapStart
            goto L_0x042f
        L_0x0458:
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r0 = r30
            boolean r0 = r0 instanceof gnu.kawa.sax.ContentConsumer
            r30 = r0
            if (r30 == 0) goto L_0x0471
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            gnu.kawa.sax.ContentConsumer r30 = (gnu.kawa.sax.ContentConsumer) r30
            r30.endStartTag()
        L_0x0471:
            r7 = 1
        L_0x0472:
            r0 = r34
            int r0 = r0.attrCount
            r30 = r0
            r0 = r30
            if (r7 > r0) goto L_0x0493
            r0 = r34
            java.lang.Object[] r0 = r0.workStack
            r30 = r0
            r0 = r34
            int r0 = r0.nesting
            r31 = r0
            int r31 = r31 + r7
            int r31 = r31 + -1
            r32 = 0
            r30[r31] = r32
            int r7 = r7 + 1
            goto L_0x0472
        L_0x0493:
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r31 = r0
            r0 = r30
            r1 = r31
            if (r0 == r1) goto L_0x04ba
            r0 = r34
            gnu.lists.Consumer r0 = r0.out
            r30 = r0
            r0 = r30
            r1 = r34
            r1.base = r0
            r0 = r34
            gnu.lists.TreeList r0 = r0.tlist
            r30 = r0
            r30.clear()
        L_0x04ba:
            r30 = -1
            r0 = r30
            r1 = r34
            r1.attrCount = r0
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.xml.XMLFilter.closeStartTag():void");
    }

    /* access modifiers changed from: protected */
    public boolean checkWriteAtomic() {
        this.previous = 0;
        if (this.ignoringLevel > 0) {
            return false;
        }
        closeStartTag();
        return true;
    }

    public void write(int v) {
        if (checkWriteAtomic()) {
            this.base.write(v);
        }
    }

    public void writeBoolean(boolean v) {
        if (checkWriteAtomic()) {
            this.base.writeBoolean(v);
        }
    }

    public void writeFloat(float v) {
        if (checkWriteAtomic()) {
            this.base.writeFloat(v);
        }
    }

    public void writeDouble(double v) {
        if (checkWriteAtomic()) {
            this.base.writeDouble(v);
        }
    }

    public void writeInt(int v) {
        if (checkWriteAtomic()) {
            this.base.writeInt(v);
        }
    }

    public void writeLong(long v) {
        if (checkWriteAtomic()) {
            this.base.writeLong(v);
        }
    }

    public void writeDocumentUri(Object uri) {
        if (this.nesting == 2 && (this.base instanceof TreeList)) {
            ((TreeList) this.base).writeDocumentUri(uri);
        }
    }

    public void consume(SeqPosition position) {
        writePosition(position.sequence, position.ipos);
    }

    public void writePosition(AbstractSequence seq, int ipos) {
        if (this.ignoringLevel <= 0) {
            if (this.stringizingLevel > 0 && this.previous == 2) {
                if (this.stringizingElementNesting < 0) {
                    write(32);
                }
                this.previous = 0;
            }
            seq.consumeNext(ipos, this);
            if (this.stringizingLevel > 0 && this.stringizingElementNesting < 0) {
                this.previous = 2;
            }
        }
    }

    public void writeObject(Object v) {
        if (this.ignoringLevel <= 0) {
            if (v instanceof SeqPosition) {
                SeqPosition pos = (SeqPosition) v;
                writePosition(pos.sequence, pos.getPos());
            } else if (v instanceof TreeList) {
                ((TreeList) v).consume((Consumer) this);
            } else if ((v instanceof List) && !(v instanceof CharSeq)) {
                int i = 0;
                for (Object writeObject : (List) v) {
                    writeObject(writeObject);
                    i++;
                }
            } else if (v instanceof Keyword) {
                startAttribute(((Keyword) v).asSymbol());
                this.previous = 1;
            } else {
                closeStartTag();
                if (v instanceof UnescapedData) {
                    this.base.writeObject(v);
                    this.previous = 0;
                    return;
                }
                if (this.previous == 2) {
                    write(32);
                }
                TextUtils.textValue(v, this);
                this.previous = 2;
            }
        }
    }

    public XMLFilter(Consumer out2) {
        this.base = out2;
        this.out = out2;
        if (out2 instanceof NodeTree) {
            this.tlist = (NodeTree) out2;
        } else {
            this.tlist = new TreeList();
        }
        this.namespaceBindings = NamespaceBinding.predefinedXML;
    }

    public void write(char[] data, int start, int length) {
        if (length == 0) {
            writeJoiner();
        } else if (checkWriteAtomic()) {
            this.base.write(data, start, length);
        }
    }

    public void write(String str) {
        write((CharSequence) str, 0, str.length());
    }

    public void write(CharSequence str, int start, int length) {
        if (length == 0) {
            writeJoiner();
        } else if (checkWriteAtomic()) {
            this.base.write(str, start, length);
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean inElement() {
        int i = this.nesting;
        while (i > 0 && this.workStack[i - 1] == null) {
            i -= 2;
        }
        return i != 0;
    }

    public void linefeedFromParser() {
        if (inElement() && checkWriteAtomic()) {
            this.base.write(10);
        }
    }

    public void textFromParser(char[] data, int start, int length) {
        if (!inElement()) {
            for (int i = 0; i != length; i++) {
                if (!Character.isWhitespace(data[start + i])) {
                    error('e', "text at document level");
                    return;
                }
            }
        } else if (length > 0 && checkWriteAtomic()) {
            this.base.write(data, start, length);
        }
    }

    /* access modifiers changed from: protected */
    public void writeJoiner() {
        this.previous = 0;
        if (this.ignoringLevel == 0) {
            ((TreeList) this.base).writeJoiner();
        }
    }

    public void writeCDATA(char[] data, int start, int length) {
        if (!checkWriteAtomic()) {
            return;
        }
        if (this.base instanceof XConsumer) {
            ((XConsumer) this.base).writeCDATA(data, start, length);
        } else {
            write(data, start, length);
        }
    }

    /* access modifiers changed from: protected */
    public void startElementCommon() {
        closeStartTag();
        if (this.stringizingLevel == 0) {
            ensureSpaceInWorkStack(this.nesting);
            this.workStack[this.nesting] = this.namespaceBindings;
            this.tlist.startElement(0);
            this.base = this.tlist;
            this.attrCount = 0;
        } else {
            if (this.previous == 2 && this.stringizingElementNesting < 0) {
                write(32);
            }
            this.previous = 0;
            if (this.stringizingElementNesting < 0) {
                this.stringizingElementNesting = this.nesting;
            }
        }
        this.nesting += 2;
    }

    public void emitStartElement(char[] data, int start, int count) {
        closeStartTag();
        MappingInfo info = lookupTag(data, start, count);
        startElementCommon();
        ensureSpaceInWorkStack(this.nesting - 1);
        this.workStack[this.nesting - 1] = info;
    }

    public void startElement(Object type) {
        NamespaceBinding inherited;
        startElementCommon();
        if (this.stringizingLevel == 0) {
            ensureSpaceInWorkStack(this.nesting - 1);
            this.workStack[this.nesting - 1] = type;
            if (this.copyNamespacesMode == 0) {
                this.namespaceBindings = NamespaceBinding.predefinedXML;
            } else if (this.copyNamespacesMode == 1 || this.nesting == 2) {
                this.namespaceBindings = type instanceof XName ? ((XName) type).getNamespaceNodes() : NamespaceBinding.predefinedXML;
            } else {
                int i = 2;
                while (true) {
                    if (i == this.nesting) {
                        inherited = null;
                        break;
                    } else if (this.workStack[i + 1] != null) {
                        inherited = (NamespaceBinding) this.workStack[i];
                        break;
                    } else {
                        i += 2;
                    }
                }
                if (inherited == null) {
                    this.namespaceBindings = type instanceof XName ? ((XName) type).getNamespaceNodes() : NamespaceBinding.predefinedXML;
                } else if (this.copyNamespacesMode == 2) {
                    this.namespaceBindings = inherited;
                } else if (type instanceof XName) {
                    NamespaceBinding preserved = ((XName) type).getNamespaceNodes();
                    if (NamespaceBinding.commonAncestor(inherited, preserved) == inherited) {
                        this.namespaceBindings = preserved;
                    } else {
                        this.namespaceBindings = mergeHelper(inherited, preserved);
                    }
                } else {
                    this.namespaceBindings = inherited;
                }
            }
        }
    }

    private NamespaceBinding mergeHelper(NamespaceBinding list, NamespaceBinding node) {
        if (node == NamespaceBinding.predefinedXML) {
            return list;
        }
        NamespaceBinding list2 = mergeHelper(list, node.next);
        String uri = node.uri;
        if (list2 == null) {
            if (uri == null) {
                return list2;
            }
            list2 = NamespaceBinding.predefinedXML;
        }
        String prefix = node.prefix;
        String found = list2.resolve(prefix);
        if (found != null ? found.equals(uri) : uri == null) {
            return list2;
        }
        return findNamespaceBinding(prefix, uri, list2);
    }

    private boolean startAttributeCommon() {
        if (this.stringizingElementNesting >= 0) {
            this.ignoringLevel++;
        }
        int i = this.stringizingLevel;
        this.stringizingLevel = i + 1;
        if (i > 0) {
            return false;
        }
        if (this.attrCount < 0) {
            this.attrCount = 0;
        }
        ensureSpaceInWorkStack(this.nesting + this.attrCount);
        ensureSpaceInStartIndexes(this.attrCount);
        this.startIndexes[this.attrCount] = this.tlist.gapStart;
        this.attrCount++;
        return true;
    }

    public void startAttribute(Object attrType) {
        this.previous = 0;
        if (attrType instanceof Symbol) {
            Symbol sym = (Symbol) attrType;
            String local = sym.getLocalPart();
            this.attrLocalName = local;
            this.attrPrefix = sym.getPrefix();
            String uri = sym.getNamespaceURI();
            if (uri == "http://www.w3.org/2000/xmlns/" || (uri == "" && local == "xmlns")) {
                error('e', "arttribute name cannot be 'xmlns' or in xmlns namespace");
            }
        }
        if (this.nesting == 2 && this.workStack[1] == null) {
            error('e', "attribute not allowed at document level");
        }
        if (this.attrCount < 0 && this.nesting > 0) {
            error('e', "attribute '" + attrType + "' follows non-attribute content");
        }
        if (startAttributeCommon()) {
            this.workStack[(this.nesting + this.attrCount) - 1] = attrType;
            if (this.nesting == 0) {
                this.base.startAttribute(attrType);
            } else {
                this.tlist.startAttribute(0);
            }
        }
    }

    public void emitStartAttribute(char[] data, int start, int count) {
        if (this.attrLocalName != null) {
            endAttribute();
        }
        if (startAttributeCommon()) {
            MappingInfo info = lookupTag(data, start, count);
            this.workStack[(this.nesting + this.attrCount) - 1] = info;
            String prefix = info.prefix;
            String local = info.local;
            this.attrLocalName = local;
            this.attrPrefix = prefix;
            if (prefix != null) {
                if (prefix == "xmlns") {
                    this.currentNamespacePrefix = local;
                }
            } else if (local == "xmlns" && prefix == null) {
                this.currentNamespacePrefix = "";
            }
            if (this.currentNamespacePrefix == null || this.namespacePrefixes) {
                this.tlist.startAttribute(0);
            }
        }
    }

    public void emitEndAttributes() {
        if (this.attrLocalName != null) {
            endAttribute();
        }
        closeStartTag();
    }

    public void emitEndElement(char[] data, int start, int length) {
        if (this.attrLocalName != null) {
            error('e', "unclosed attribute");
            endAttribute();
        }
        if (!inElement()) {
            error('e', "unmatched end element");
            return;
        }
        if (data != null) {
            MappingInfo info = lookupTag(data, start, length);
            Object old = this.workStack[this.nesting - 1];
            if ((old instanceof MappingInfo) && !this.mismatchReported) {
                MappingInfo mold = (MappingInfo) old;
                if (!(info.local == mold.local && info.prefix == mold.prefix)) {
                    StringBuffer sbuf = new StringBuffer("</");
                    sbuf.append(data, start, length);
                    sbuf.append("> matching <");
                    String oldPrefix = mold.prefix;
                    if (oldPrefix != null) {
                        sbuf.append(oldPrefix);
                        sbuf.append(':');
                    }
                    sbuf.append(mold.local);
                    sbuf.append('>');
                    error('e', sbuf.toString());
                    this.mismatchReported = true;
                }
            }
        }
        closeStartTag();
        if (this.nesting > 0) {
            endElement();
        }
    }

    public void endElement() {
        closeStartTag();
        this.nesting -= 2;
        this.previous = 0;
        if (this.stringizingLevel == 0) {
            this.namespaceBindings = (NamespaceBinding) this.workStack[this.nesting];
            this.workStack[this.nesting] = null;
            this.workStack[this.nesting + 1] = null;
            this.base.endElement();
        } else if (this.stringizingElementNesting == this.nesting) {
            this.stringizingElementNesting = -1;
            this.previous = 2;
        }
    }

    public void emitEntityReference(char[] name, int start, int length) {
        char c0 = name[start];
        char ch = '?';
        if (length == 2 && name[start + 1] == 't') {
            if (c0 == 'l') {
                ch = '<';
            } else if (c0 == 'g') {
                ch = '>';
            }
        } else if (length == 3) {
            if (c0 == 'a' && name[start + 1] == 'm' && name[start + 2] == 'p') {
                ch = '&';
            }
        } else if (length == 4) {
            char c1 = name[start + 1];
            char c2 = name[start + 2];
            char c3 = name[start + 3];
            if (c0 == 'q' && c1 == 'u' && c2 == 'o' && c3 == 't') {
                ch = '\"';
            } else if (c0 == 'a' && c1 == 'p' && c2 == 'o' && c3 == 's') {
                ch = '\'';
            }
        }
        write((int) ch);
    }

    public void emitCharacterReference(int value, char[] name, int start, int length) {
        if (value >= 65536) {
            Char.print(value, this);
        } else {
            write(value);
        }
    }

    /* access modifiers changed from: protected */
    public void checkValidComment(char[] chars, int offset, int length) {
        int i = length;
        boolean sawHyphen = true;
        while (true) {
            i--;
            if (i >= 0) {
                boolean curHyphen = chars[offset + i] == '-';
                if (!sawHyphen || !curHyphen) {
                    sawHyphen = curHyphen;
                } else {
                    error('e', "consecutive or final hyphen in XML comment");
                    return;
                }
            } else {
                return;
            }
        }
    }

    public void writeComment(char[] chars, int start, int length) {
        checkValidComment(chars, start, length);
        commentFromParser(chars, start, length);
    }

    public void commentFromParser(char[] chars, int start, int length) {
        if (this.stringizingLevel == 0) {
            closeStartTag();
            if (this.base instanceof XConsumer) {
                ((XConsumer) this.base).writeComment(chars, start, length);
            }
        } else if (this.stringizingElementNesting < 0) {
            this.base.write(chars, start, length);
        }
    }

    public void writeProcessingInstruction(String target, char[] content, int offset, int length) {
        String target2 = TextUtils.replaceWhitespace(target, true);
        int i = offset + length;
        while (true) {
            i--;
            if (i < offset) {
                break;
            }
            char ch = content[i];
            while (true) {
                if (ch != '>' || i - 1 < offset) {
                    break;
                }
                ch = content[i];
                if (ch == '?') {
                    error('e', "'?>' is not allowed in a processing-instruction");
                    break;
                }
            }
        }
        if ("xml".equalsIgnoreCase(target2)) {
            error('e', "processing-instruction target may not be 'xml' (ignoring case)");
        }
        if (!XName.isNCName(target2)) {
            error('e', "processing-instruction target '" + target2 + "' is not a valid Name");
        }
        processingInstructionCommon(target2, content, offset, length);
    }

    /* access modifiers changed from: package-private */
    public void processingInstructionCommon(String target, char[] content, int offset, int length) {
        if (this.stringizingLevel == 0) {
            closeStartTag();
            if (this.base instanceof XConsumer) {
                ((XConsumer) this.base).writeProcessingInstruction(target, content, offset, length);
            }
        } else if (this.stringizingElementNesting < 0) {
            this.base.write(content, offset, length);
        }
    }

    public void processingInstructionFromParser(char[] buffer, int tstart, int tlength, int dstart, int dlength) {
        if (tlength != 3 || inElement() || buffer[tstart] != 'x' || buffer[tstart + 1] != 'm' || buffer[tstart + 2] != 'l') {
            processingInstructionCommon(new String(buffer, tstart, tlength), buffer, dstart, dlength);
        }
    }

    public void startDocument() {
        closeStartTag();
        if (this.stringizingLevel > 0) {
            writeJoiner();
            return;
        }
        if (this.nesting == 0) {
            this.base.startDocument();
        } else {
            writeJoiner();
        }
        ensureSpaceInWorkStack(this.nesting);
        this.workStack[this.nesting] = this.namespaceBindings;
        this.workStack[this.nesting + 1] = null;
        this.nesting += 2;
    }

    public void endDocument() {
        if (this.stringizingLevel > 0) {
            writeJoiner();
            return;
        }
        this.nesting -= 2;
        this.namespaceBindings = (NamespaceBinding) this.workStack[this.nesting];
        this.workStack[this.nesting] = null;
        this.workStack[this.nesting + 1] = null;
        if (this.nesting == 0) {
            this.base.endDocument();
        } else {
            writeJoiner();
        }
    }

    public void emitDoctypeDecl(char[] buffer, int target, int tlength, int data, int dlength) {
    }

    public void beginEntity(Object baseUri) {
        if (this.base instanceof XConsumer) {
            ((XConsumer) this.base).beginEntity(baseUri);
        }
    }

    public void endEntity() {
        if (this.base instanceof XConsumer) {
            ((XConsumer) this.base).endEntity();
        }
    }

    public XMLFilter append(char c) {
        write((int) c);
        return this;
    }

    public XMLFilter append(CharSequence csq) {
        if (csq == null) {
            csq = "null";
        }
        append(csq, 0, csq.length());
        return this;
    }

    public XMLFilter append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        write(csq, start, end - start);
        return this;
    }

    /* access modifiers changed from: package-private */
    public MappingInfo lookupTag(Symbol qname) {
        String local = qname.getLocalPart();
        String prefix = qname.getPrefix();
        if (prefix == "") {
            prefix = null;
        }
        String uri = qname.getNamespaceURI();
        int hash = MappingInfo.hash(prefix, local);
        int index = hash & this.mappingTableMask;
        MappingInfo first = this.mappingTable[index];
        for (MappingInfo info = first; info != null; info = info.nextInBucket) {
            if (qname == info.qname) {
                return info;
            }
            if (local == info.local && info.qname == null && ((uri == info.uri || info.uri == null) && prefix == info.prefix)) {
                info.uri = uri;
                info.qname = qname;
                return info;
            }
        }
        MappingInfo info2 = new MappingInfo();
        info2.qname = qname;
        info2.prefix = prefix;
        info2.uri = uri;
        info2.local = local;
        info2.tagHash = hash;
        info2.nextInBucket = first;
        this.mappingTable[index] = first;
        return info2;
    }

    /* access modifiers changed from: package-private */
    public MappingInfo lookupTag(char[] data, int start, int length) {
        int hash = 0;
        int prefixHash = 0;
        int colon = -1;
        for (int i = 0; i < length; i++) {
            char ch = data[start + i];
            if (ch != ':' || colon >= 0) {
                hash = (hash * 31) + ch;
            } else {
                colon = i;
                prefixHash = hash;
                hash = 0;
            }
        }
        int hash2 = hash ^ prefixHash;
        int index = hash2 & this.mappingTableMask;
        MappingInfo first = this.mappingTable[index];
        for (MappingInfo info = first; info != null; info = info.nextInBucket) {
            if (hash2 == info.tagHash && info.match(data, start, length)) {
                return info;
            }
        }
        MappingInfo info2 = new MappingInfo();
        info2.tagHash = hash2;
        if (colon >= 0) {
            info2.prefix = new String(data, start, colon).intern();
            int colon2 = colon + 1;
            info2.local = new String(data, start + colon2, length - colon2).intern();
        } else {
            info2.prefix = null;
            info2.local = new String(data, start, length).intern();
        }
        info2.nextInBucket = first;
        this.mappingTable[index] = first;
        return info2;
    }

    private void ensureSpaceInWorkStack(int oldSize) {
        if (this.workStack == null) {
            this.workStack = new Object[20];
        } else if (oldSize >= this.workStack.length) {
            Object[] tmpn = new Object[(this.workStack.length * 2)];
            System.arraycopy(this.workStack, 0, tmpn, 0, oldSize);
            this.workStack = tmpn;
        }
    }

    private void ensureSpaceInStartIndexes(int oldSize) {
        if (this.startIndexes == null) {
            this.startIndexes = new int[20];
        } else if (oldSize >= this.startIndexes.length) {
            int[] tmpn = new int[(this.startIndexes.length * 2)];
            System.arraycopy(this.startIndexes, 0, tmpn, 0, oldSize);
            this.startIndexes = tmpn;
        }
    }

    public static String duplicateAttributeMessage(Symbol attrSymbol, Object elementName) {
        StringBuffer sbuf = new StringBuffer("duplicate attribute: ");
        String uri = attrSymbol.getNamespaceURI();
        if (uri != null && uri.length() > 0) {
            sbuf.append('{');
            sbuf.append('}');
            sbuf.append(uri);
        }
        sbuf.append(attrSymbol.getLocalPart());
        if (elementName != null) {
            sbuf.append(" in <");
            sbuf.append(elementName);
            sbuf.append('>');
        }
        return sbuf.toString();
    }

    public void error(char severity, String message) {
        if (this.messages == null) {
            throw new RuntimeException(message);
        } else if (this.locator != null) {
            this.messages.error(severity, this.locator, message);
        } else {
            this.messages.error(severity, message);
        }
    }

    public boolean ignoring() {
        return this.ignoringLevel > 0;
    }

    public void setDocumentLocator(Locator locator2) {
        if (locator2 instanceof SourceLocator) {
            this.locator = (SourceLocator) locator2;
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        startElement(Symbol.make(namespaceURI, localName));
        int numAttributes = atts.getLength();
        for (int i = 0; i < numAttributes; i++) {
            startAttribute(Symbol.make(atts.getURI(i), atts.getLocalName(i)));
            write(atts.getValue(i));
            endAttribute();
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) {
        endElement();
    }

    public void startElement(String name, AttributeList atts) {
        startElement(name.intern());
        int attrLength = atts.getLength();
        for (int i = 0; i < attrLength; i++) {
            String name2 = atts.getName(i).intern();
            String type = atts.getType(i);
            String value = atts.getValue(i);
            startAttribute(name2);
            write(value);
            endAttribute();
        }
    }

    public void endElement(String name) throws SAXException {
        endElement();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        write(ch, start, length);
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        write(ch, start, length);
    }

    public void processingInstruction(String target, String data) {
        char[] chars = data.toCharArray();
        processingInstructionCommon(target, chars, 0, chars.length);
    }

    public void startPrefixMapping(String prefix, String uri) {
        this.namespaceBindings = findNamespaceBinding(prefix.intern(), uri.intern(), this.namespaceBindings);
    }

    public void endPrefixMapping(String prefix) {
        this.namespaceBindings = this.namespaceBindings.getNext();
    }

    public void skippedEntity(String name) {
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        if (this.in == null) {
            return null;
        }
        return this.in.getName();
    }

    public String getFileName() {
        if (this.in == null) {
            return null;
        }
        return this.in.getName();
    }

    public int getLineNumber() {
        int line;
        if (this.in != null && (line = this.in.getLineNumber()) >= 0) {
            return line + 1;
        }
        return -1;
    }

    public int getColumnNumber() {
        int col;
        if (this.in == null || (col = this.in.getColumnNumber()) <= 0) {
            return -1;
        }
        return col;
    }

    public boolean isStableSourceLocation() {
        return false;
    }
}
