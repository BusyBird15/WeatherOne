package gnu.xml;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public final class NamespaceBinding implements Externalizable {
    public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    public static final NamespaceBinding predefinedXML = new NamespaceBinding("xml", XML_NAMESPACE, (NamespaceBinding) null);
    int depth;
    NamespaceBinding next;
    String prefix;
    String uri;

    public final String getPrefix() {
        return this.prefix;
    }

    public final void setPrefix(String prefix2) {
        this.prefix = prefix2;
    }

    public final String getUri() {
        return this.uri;
    }

    public final void setUri(String uri2) {
        this.uri = uri2;
    }

    public final NamespaceBinding getNext() {
        return this.next;
    }

    public final void setNext(NamespaceBinding next2) {
        this.next = next2;
        this.depth = next2 == null ? 0 : next2.depth + 1;
    }

    public static final NamespaceBinding nconc(NamespaceBinding list1, NamespaceBinding list2) {
        if (list1 == null) {
            return list2;
        }
        list1.setNext(nconc(list1.next, list2));
        return list1;
    }

    public NamespaceBinding(String prefix2, String uri2, NamespaceBinding next2) {
        this.prefix = prefix2;
        this.uri = uri2;
        setNext(next2);
    }

    public String resolve(String prefix2) {
        for (NamespaceBinding ns = this; ns != null; ns = ns.next) {
            if (ns.prefix == prefix2) {
                return ns.uri;
            }
        }
        return null;
    }

    public String resolve(String prefix2, NamespaceBinding fencePost) {
        for (NamespaceBinding ns = this; ns != fencePost; ns = ns.next) {
            if (ns.prefix == prefix2) {
                return ns.uri;
            }
        }
        return null;
    }

    public static NamespaceBinding commonAncestor(NamespaceBinding ns1, NamespaceBinding ns2) {
        if (ns1.depth > ns2.depth) {
            NamespaceBinding tmp = ns1;
            ns1 = ns2;
            ns2 = tmp;
        }
        while (ns2.depth > ns1.depth) {
            ns2 = ns2.next;
        }
        while (ns1 != ns2) {
            ns1 = ns1.next;
            ns2 = ns2.next;
        }
        return ns1;
    }

    public NamespaceBinding reversePrefix(NamespaceBinding fencePost) {
        NamespaceBinding prev = fencePost;
        NamespaceBinding t = this;
        int depth2 = fencePost == null ? -1 : fencePost.depth;
        while (t != fencePost) {
            NamespaceBinding next2 = t.next;
            t.next = prev;
            prev = t;
            depth2++;
            t.depth = depth2;
            t = next2;
        }
        return prev;
    }

    public int count(NamespaceBinding fencePost) {
        int count = 0;
        for (NamespaceBinding ns = this; ns != fencePost; ns = ns.next) {
            count++;
        }
        return count;
    }

    public static NamespaceBinding maybeAdd(String prefix2, String uri2, NamespaceBinding bindings) {
        if (bindings == null) {
            if (uri2 == null) {
                return bindings;
            }
            bindings = predefinedXML;
        }
        String found = bindings.resolve(prefix2);
        if (found != null ? found.equals(uri2) : uri2 == null) {
            return bindings;
        }
        return new NamespaceBinding(prefix2, uri2, bindings);
    }

    public String toString() {
        return "Namespace{" + this.prefix + "=" + this.uri + ", depth:" + this.depth + "}";
    }

    public String toStringAll() {
        String str;
        StringBuffer sbuf = new StringBuffer("Namespaces{");
        for (NamespaceBinding ns = this; ns != null; ns = ns.next) {
            sbuf.append(ns.prefix);
            sbuf.append("=\"");
            sbuf.append(ns.uri);
            if (ns == null) {
                str = "\"";
            } else {
                str = "\", ";
            }
            sbuf.append(str);
        }
        sbuf.append('}');
        return sbuf.toString();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.prefix);
        out.writeUTF(this.uri);
        out.writeObject(this.next);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.prefix = in.readUTF();
        this.uri = in.readUTF();
        this.next = (NamespaceBinding) in.readObject();
    }
}
