package gnu.kawa.xml;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Method;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.expr.Compilation;
import gnu.expr.TypeValue;
import gnu.lists.AbstractSequence;
import gnu.lists.ElementPredicate;
import gnu.mapping.Namespace;
import gnu.mapping.Procedure;
import gnu.mapping.Symbol;
import gnu.xml.NamespaceBinding;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;

public class ElementType extends NodeType implements TypeValue, Externalizable, ElementPredicate {
    public static final String MATCH_ANY_LOCALNAME = "";
    public static final Symbol MATCH_ANY_QNAME = new Symbol((Namespace) null, "");
    public static final ElementType anyElement = make((String) null, (String) null);
    static final Method coerceMethod = typeElementType.getDeclaredMethod("coerce", 3);
    static final Method coerceOrNullMethod = typeElementType.getDeclaredMethod("coerceOrNull", 3);
    public static final ClassType typeElementType = ClassType.make("gnu.kawa.xml.ElementType");
    NamespaceBinding namespaceNodes;
    Symbol qname;

    public static ElementType make(String namespaceURI, String localName) {
        Symbol qname2;
        if (namespaceURI != null) {
            qname2 = Symbol.make(namespaceURI, localName);
        } else if (localName == "") {
            qname2 = MATCH_ANY_QNAME;
        } else {
            qname2 = new Symbol((Namespace) null, localName);
        }
        return new ElementType(qname2);
    }

    public static ElementType make(Symbol qname2) {
        return new ElementType(qname2);
    }

    public ElementType(Symbol qname2) {
        this((String) null, qname2);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ElementType(java.lang.String r3, gnu.mapping.Symbol r4) {
        /*
            r2 = this;
            if (r3 == 0) goto L_0x000e
            int r0 = r3.length()
            if (r0 <= 0) goto L_0x000e
        L_0x0008:
            r2.<init>(r3)
            r2.qname = r4
            return
        L_0x000e:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "ELEMENT "
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r4)
            java.lang.String r1 = " (*)"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r3 = r0.toString()
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.xml.ElementType.<init>(java.lang.String, gnu.mapping.Symbol):void");
    }

    public Type getImplementationType() {
        return ClassType.make("gnu.kawa.xml.KElement");
    }

    public final String getNamespaceURI() {
        return this.qname.getNamespaceURI();
    }

    public final String getLocalName() {
        return this.qname.getLocalName();
    }

    public void emitCoerceFromObject(CodeAttr code) {
        code.emitPushString(this.qname.getNamespaceURI());
        code.emitPushString(this.qname.getLocalName());
        code.emitInvokeStatic(coerceMethod);
    }

    public Object coerceFromObject(Object obj) {
        return coerce(obj, this.qname.getNamespaceURI(), this.qname.getLocalName());
    }

    public boolean isInstancePos(AbstractSequence seq, int ipos) {
        int kind = seq.getNextKind(ipos);
        if (kind == 33) {
            return isInstance(seq, ipos, seq.getNextTypeObject(ipos));
        }
        if (kind == 32) {
            return isInstance(seq.getPosNext(ipos));
        }
        return false;
    }

    public boolean isInstance(AbstractSequence seq, int ipos, Object elementType) {
        String curNamespaceURI;
        String curLocalName;
        String namespaceURI = this.qname.getNamespaceURI();
        String localName = this.qname.getLocalName();
        if (elementType instanceof Symbol) {
            Symbol qname2 = (Symbol) elementType;
            curNamespaceURI = qname2.getNamespaceURI();
            curLocalName = qname2.getLocalName();
        } else if (elementType instanceof QName) {
            QName qtype = (QName) elementType;
            curNamespaceURI = qtype.getNamespaceURI();
            curLocalName = qtype.getLocalPart();
        } else {
            curNamespaceURI = "";
            curLocalName = elementType.toString().intern();
        }
        if (localName != null && localName.length() == 0) {
            localName = null;
        }
        if ((localName == curLocalName || localName == null) && (namespaceURI == curNamespaceURI || namespaceURI == null)) {
            return true;
        }
        return false;
    }

    public boolean isInstance(Object obj) {
        return coerceOrNull(obj, this.qname.getNamespaceURI(), this.qname.getLocalName()) != null;
    }

    public static KElement coerceOrNull(Object obj, String namespaceURI, String localName) {
        String curNamespaceURI;
        String curLocalName;
        KElement pos = (KElement) NodeType.coerceOrNull(obj, 2);
        if (pos == null) {
            return null;
        }
        if (localName != null && localName.length() == 0) {
            localName = null;
        }
        Object curName = pos.getNextTypeObject();
        if (curName instanceof Symbol) {
            Symbol qname2 = (Symbol) curName;
            curNamespaceURI = qname2.getNamespaceURI();
            curLocalName = qname2.getLocalName();
        } else if (curName instanceof QName) {
            QName qtype = (QName) curName;
            curNamespaceURI = qtype.getNamespaceURI();
            curLocalName = qtype.getLocalPart();
        } else {
            curNamespaceURI = "";
            curLocalName = curName.toString().intern();
        }
        if ((localName == curLocalName || localName == null) && (namespaceURI == curNamespaceURI || namespaceURI == null)) {
            return pos;
        }
        return null;
    }

    public static KElement coerce(Object obj, String namespaceURI, String localName) {
        KElement pos = coerceOrNull(obj, namespaceURI, localName);
        if (pos != null) {
            return pos;
        }
        throw new ClassCastException();
    }

    /* access modifiers changed from: protected */
    public void emitCoerceOrNullMethod(Variable incoming, Compilation comp) {
        CodeAttr code = comp.getCode();
        if (incoming != null) {
            code.emitLoad(incoming);
        }
        code.emitPushString(this.qname.getNamespaceURI());
        code.emitPushString(this.qname.getLocalName());
        code.emitInvokeStatic(coerceOrNullMethod);
    }

    public NamespaceBinding getNamespaceNodes() {
        return this.namespaceNodes;
    }

    public void setNamespaceNodes(NamespaceBinding bindings) {
        this.namespaceNodes = bindings;
    }

    public Procedure getConstructor() {
        MakeElement element = new MakeElement();
        element.tag = this.qname;
        element.setHandlingKeywordParameters(true);
        if (this.namespaceNodes != null) {
            element.setNamespaceNodes(this.namespaceNodes);
        }
        return element;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        String name = getName();
        if (name == null) {
            name = "";
        }
        out.writeUTF(name);
        out.writeObject(this.qname);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String name = in.readUTF();
        if (name.length() > 0) {
            setName(name);
        }
        this.qname = (Symbol) in.readObject();
    }

    public String toString() {
        return "ElementType " + this.qname;
    }
}
