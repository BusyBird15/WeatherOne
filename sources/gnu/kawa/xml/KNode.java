package gnu.kawa.xml;

import gnu.lists.AbstractSequence;
import gnu.lists.Consumable;
import gnu.lists.Consumer;
import gnu.lists.PositionConsumer;
import gnu.lists.SeqPosition;
import gnu.lists.TreePosition;
import gnu.mapping.CharArrayOutPort;
import gnu.mapping.Namespace;
import gnu.mapping.Symbol;
import gnu.text.Path;
import gnu.xml.NodeTree;
import gnu.xml.XMLPrinter;
import java.io.Writer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public abstract class KNode extends SeqPosition implements Node, Consumable {
    public abstract short getNodeType();

    public KNode(NodeTree seq, int ipos) {
        super(seq, ipos);
    }

    public static Object atomicValue(Object value) {
        if (!(value instanceof KNode)) {
            return value;
        }
        KNode node = (KNode) value;
        return ((NodeTree) node.sequence).typedValue(node.ipos);
    }

    public static KNode coerce(Object value) {
        if (value instanceof KNode) {
            return (KNode) value;
        }
        if (value instanceof NodeTree) {
            NodeTree ntree = (NodeTree) value;
            return make(ntree, ntree.startPos());
        }
        if ((value instanceof SeqPosition) && !(value instanceof TreePosition)) {
            SeqPosition seqp = (SeqPosition) value;
            if (seqp.sequence instanceof NodeTree) {
                return make((NodeTree) seqp.sequence, seqp.ipos);
            }
        }
        return null;
    }

    public static KNode make(NodeTree seq, int ipos) {
        int index = seq.posToDataIndex(ipos);
        while (index < seq.data.length && seq.data[index] == 61714) {
            index += 5;
            if (index == seq.gapStart) {
                index = seq.gapEnd;
            }
            ipos = index << 1;
        }
        switch (seq.getNextKindI(seq.posToDataIndex(ipos))) {
            case 0:
                if (!seq.isEmpty()) {
                    return null;
                }
                break;
            case 31:
                return new KCDATASection(seq, ipos);
            case 33:
                return new KElement(seq, ipos);
            case 34:
                return new KDocument(seq, ipos);
            case 35:
                return new KAttr(seq, ipos);
            case 36:
                return new KComment(seq, ipos);
            case 37:
                return new KProcessingInstruction(seq, ipos);
        }
        return new KText(seq, ipos);
    }

    public KNode copy() {
        return make((NodeTree) this.sequence, this.sequence.copyPos(getPos()));
    }

    public static KNode make(NodeTree seq) {
        return make(seq, 0);
    }

    public boolean isSupported(String feature, String version) {
        return false;
    }

    public String getNodeName() {
        return this.sequence.getNextTypeName(this.ipos);
    }

    public Symbol getNodeSymbol() {
        Object type = ((NodeTree) this.sequence).getNextTypeObject(this.ipos);
        if (type == null) {
            return null;
        }
        if (type instanceof Symbol) {
            return (Symbol) type;
        }
        return Namespace.EmptyNamespace.getSymbol(type.toString().intern());
    }

    public Object getNodeNameObject() {
        return ((NodeTree) this.sequence).getNextTypeObject(this.ipos);
    }

    public String getNamespaceURI() {
        return ((NodeTree) this.sequence).posNamespaceURI(this.ipos);
    }

    public String getPrefix() {
        return ((NodeTree) this.sequence).posPrefix(this.ipos);
    }

    public String getLocalName() {
        return ((NodeTree) this.sequence).posLocalName(this.ipos);
    }

    public static String getNodeValue(NodeTree seq, int ipos) {
        StringBuffer sbuf = new StringBuffer();
        getNodeValue(seq, ipos, sbuf);
        return sbuf.toString();
    }

    public static void getNodeValue(NodeTree seq, int ipos, StringBuffer sbuf) {
        seq.stringValue(seq.posToDataIndex(ipos), sbuf);
    }

    public String getNodeValue() {
        StringBuffer sbuf = new StringBuffer();
        getNodeValue(sbuf);
        return sbuf.toString();
    }

    public void getNodeValue(StringBuffer sbuf) {
        getNodeValue((NodeTree) this.sequence, this.ipos, sbuf);
    }

    public boolean hasChildNodes() {
        return ((NodeTree) this.sequence).posFirstChild(this.ipos) >= 0;
    }

    public String getTextContent() {
        StringBuffer sbuf = new StringBuffer();
        getTextContent(sbuf);
        return sbuf.toString();
    }

    /* access modifiers changed from: protected */
    public void getTextContent(StringBuffer sbuf) {
        getNodeValue(sbuf);
    }

    public Node getParentNode() {
        int parent = this.sequence.parentPos(this.ipos);
        if (parent == -1) {
            return null;
        }
        return make((NodeTree) this.sequence, parent);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0028 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.w3c.dom.Node getPreviousSibling() {
        /*
            r6 = this;
            gnu.lists.AbstractSequence r4 = r6.sequence
            int r5 = r6.ipos
            int r2 = r4.parentPos(r5)
            r4 = -1
            if (r2 != r4) goto L_0x000c
            r2 = 0
        L_0x000c:
            gnu.lists.AbstractSequence r4 = r6.sequence
            gnu.xml.NodeTree r4 = (gnu.xml.NodeTree) r4
            int r5 = r6.ipos
            int r1 = r4.posToDataIndex(r5)
            gnu.lists.AbstractSequence r4 = r6.sequence
            int r0 = r4.firstChildPos(r2)
            r3 = 0
        L_0x001d:
            r3 = r0
            gnu.lists.AbstractSequence r4 = r6.sequence
            int r0 = r4.nextPos(r0)
            if (r0 != 0) goto L_0x002a
        L_0x0026:
            if (r3 != 0) goto L_0x0035
            r4 = 0
        L_0x0029:
            return r4
        L_0x002a:
            gnu.lists.AbstractSequence r4 = r6.sequence
            gnu.xml.NodeTree r4 = (gnu.xml.NodeTree) r4
            int r4 = r4.posToDataIndex(r0)
            if (r4 != r1) goto L_0x001d
            goto L_0x0026
        L_0x0035:
            gnu.lists.AbstractSequence r4 = r6.sequence
            gnu.xml.NodeTree r4 = (gnu.xml.NodeTree) r4
            gnu.kawa.xml.KNode r4 = make(r4, r3)
            goto L_0x0029
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.xml.KNode.getPreviousSibling():org.w3c.dom.Node");
    }

    public Node getNextSibling() {
        int next = ((NodeTree) this.sequence).nextPos(this.ipos);
        if (next == 0) {
            return null;
        }
        return make((NodeTree) this.sequence, next);
    }

    public Node getFirstChild() {
        return make((NodeTree) this.sequence, ((NodeTree) this.sequence).posFirstChild(this.ipos));
    }

    public Node getLastChild() {
        int last = 0;
        int child = this.sequence.firstChildPos(this.ipos);
        while (child != 0) {
            last = child;
            child = this.sequence.nextPos(child);
        }
        if (last == 0) {
            return null;
        }
        return make((NodeTree) this.sequence, last);
    }

    public NodeList getChildNodes() {
        Nodes nodes = new SortedNodes();
        int child = this.sequence.firstChildPos(this.ipos);
        while (child != 0) {
            nodes.writePosition(this.sequence, child);
            child = this.sequence.nextPos(child);
        }
        return nodes;
    }

    public NodeList getElementsByTagName(String tagname) {
        throw new UnsupportedOperationException("getElementsByTagName not implemented yet");
    }

    public void setNodeValue(String nodeValue) throws DOMException {
        throw new DOMException(7, "setNodeValue not supported");
    }

    public void setPrefix(String prefix) throws DOMException {
        throw new DOMException(7, "setPrefix not supported");
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        throw new DOMException(7, "insertBefore not supported");
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw new DOMException(7, "replaceChild not supported");
    }

    public Node removeChild(Node oldChild) throws DOMException {
        throw new DOMException(7, "removeChild not supported");
    }

    public Node appendChild(Node newChild) throws DOMException {
        throw new DOMException(7, "appendChild not supported");
    }

    public void setTextContent(String textContent) throws DOMException {
        throw new DOMException(7, "setTextContent not supported");
    }

    public Node cloneNode(boolean deep) {
        if (!deep) {
            throw new UnsupportedOperationException("shallow cloneNode not implemented");
        }
        NodeTree tree = new NodeTree();
        ((NodeTree) this.sequence).consumeNext(this.ipos, tree);
        return make(tree);
    }

    public Document getOwnerDocument() {
        if (this.sequence.getNextKind(this.ipos) == 34) {
            return new KDocument((NodeTree) this.sequence, 0);
        }
        return null;
    }

    public NamedNodeMap getAttributes() {
        throw new UnsupportedOperationException("getAttributes not implemented yet");
    }

    public void normalize() {
    }

    public boolean hasAttributes() {
        return false;
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        return ((NodeTree) this.sequence).posIsDefaultNamespace(this.ipos, namespaceURI);
    }

    public String lookupNamespaceURI(String prefix) {
        return ((NodeTree) this.sequence).posLookupNamespaceURI(this.ipos, prefix);
    }

    public String lookupPrefix(String namespaceURI) {
        return ((NodeTree) this.sequence).posLookupPrefix(this.ipos, namespaceURI);
    }

    public String getBaseURI() {
        Path uri = ((NodeTree) this.sequence).baseUriOfPos(this.ipos, true);
        if (uri == null) {
            return null;
        }
        return uri.toString();
    }

    public Path baseURI() {
        return ((NodeTree) this.sequence).baseUriOfPos(this.ipos, true);
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        if (!(other instanceof KNode)) {
            throw new DOMException(9, "other Node is a " + other.getClass().getName());
        }
        KNode n = (KNode) other;
        AbstractSequence nseq = n.sequence;
        return (short) (this.sequence == nseq ? nseq.compare(this.ipos, n.ipos) : this.sequence.stableCompare(nseq));
    }

    public boolean isSameNode(Node node) {
        if (!(node instanceof KNode)) {
            return false;
        }
        KNode n = (KNode) node;
        if (this.sequence == n.sequence) {
            return this.sequence.equals(this.ipos, n.ipos);
        }
        return false;
    }

    public boolean isEqualNode(Node node) {
        throw new UnsupportedOperationException("getAttributesisEqualNode not implemented yet");
    }

    public String toString() {
        CharArrayOutPort wr = new CharArrayOutPort();
        XMLPrinter xp = new XMLPrinter((Writer) wr);
        ((NodeTree) this.sequence).consumeNext(this.ipos, xp);
        xp.close();
        wr.close();
        return wr.toString();
    }

    public Object getFeature(String feature, String version) {
        return null;
    }

    public void consume(Consumer out) {
        if (out instanceof PositionConsumer) {
            ((PositionConsumer) out).consume(this);
        } else {
            ((NodeTree) this.sequence).consumeNext(this.ipos, out);
        }
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        throw new UnsupportedOperationException("setUserData not implemented yet");
    }

    public Object getUserData(String key) {
        return null;
    }
}
