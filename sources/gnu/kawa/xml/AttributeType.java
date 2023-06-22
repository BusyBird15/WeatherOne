package gnu.kawa.xml;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Method;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.expr.Compilation;
import gnu.expr.TypeValue;
import gnu.lists.AbstractSequence;
import gnu.lists.AttributePredicate;
import gnu.lists.SeqPosition;
import gnu.mapping.Namespace;
import gnu.mapping.Symbol;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;

public class AttributeType extends NodeType implements TypeValue, Externalizable, AttributePredicate {
    static final Method coerceMethod = typeAttributeType.getDeclaredMethod("coerce", 3);
    static final Method coerceOrNullMethod = typeAttributeType.getDeclaredMethod("coerceOrNull", 3);
    public static final ClassType typeAttributeType = ClassType.make("gnu.kawa.xml.AttributeType");
    Symbol qname;

    public static AttributeType make(String namespaceURI, String localName) {
        Symbol qname2;
        if (namespaceURI != null) {
            qname2 = Symbol.make(namespaceURI, localName);
        } else if (localName == "") {
            qname2 = ElementType.MATCH_ANY_QNAME;
        } else {
            qname2 = new Symbol((Namespace) null, localName);
        }
        return new AttributeType(qname2);
    }

    public static AttributeType make(Symbol qname2) {
        return new AttributeType(qname2);
    }

    public AttributeType(Symbol qname2) {
        this((String) null, qname2);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AttributeType(java.lang.String r3, gnu.mapping.Symbol r4) {
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
            java.lang.String r1 = "ATTRIBUTE "
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r4)
            java.lang.String r1 = " (*)"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r3 = r0.toString()
            goto L_0x0008
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.xml.AttributeType.<init>(java.lang.String, gnu.mapping.Symbol):void");
    }

    public Type getImplementationType() {
        return ClassType.make("gnu.kawa.xml.KAttr");
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
        if (kind == 35) {
            return isInstance(seq, ipos, seq.getNextTypeObject(ipos));
        }
        if (kind == 32) {
            return isInstance(seq.getPosNext(ipos));
        }
        return false;
    }

    public boolean isInstance(AbstractSequence seq, int ipos, Object attrType) {
        String curNamespaceURI;
        String curLocalName;
        String namespaceURI = this.qname.getNamespaceURI();
        String localName = this.qname.getLocalName();
        if (attrType instanceof Symbol) {
            Symbol qname2 = (Symbol) attrType;
            curNamespaceURI = qname2.getNamespaceURI();
            curLocalName = qname2.getLocalName();
        } else if (attrType instanceof QName) {
            QName qtype = (QName) attrType;
            curNamespaceURI = qtype.getNamespaceURI();
            curLocalName = qtype.getLocalPart();
        } else {
            curNamespaceURI = "";
            curLocalName = attrType.toString().intern();
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

    public static KAttr coerceOrNull(Object obj, String namespaceURI, String localName) {
        String curNamespaceURI;
        String curLocalName;
        KNode pos = NodeType.coerceOrNull(obj, 4);
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
            return (KAttr) pos;
        }
        return null;
    }

    public static SeqPosition coerce(Object obj, String namespaceURI, String localName) {
        SeqPosition pos = coerceOrNull(obj, namespaceURI, localName);
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
        return "AttributeType " + this.qname;
    }
}
