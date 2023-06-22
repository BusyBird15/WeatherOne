package gnu.bytecode;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

public class AnnotationEntry implements Annotation {
    ClassType annotationType;
    LinkedHashMap<String, Object> elementsValue = new LinkedHashMap<>(10);

    public ClassType getAnnotationType() {
        return this.annotationType;
    }

    public void addMember(String name, Object value) {
        this.elementsValue.put(name, value);
    }

    public Class<? extends Annotation> annotationType() {
        return this.annotationType.getReflectClass();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AnnotationEntry)) {
            return false;
        }
        AnnotationEntry other = (AnnotationEntry) obj;
        if (!getAnnotationType().getName().equals(other.getAnnotationType().getName())) {
            return false;
        }
        for (Map.Entry<String, Object> it : this.elementsValue.entrySet()) {
            Object value1 = it.getValue();
            Object value2 = other.elementsValue.get(it.getKey());
            if (value1 != value2 && (value1 == null || value2 == null || !value1.equals(value2))) {
                return false;
            }
        }
        for (Map.Entry<String, Object> it2 : other.elementsValue.entrySet()) {
            Object value22 = it2.getValue();
            Object value12 = this.elementsValue.get(it2.getKey());
            if (value12 != value22 && (value12 == null || value22 == null || !value12.equals(value22))) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int hash = 0;
        for (Map.Entry<String, Object> it : this.elementsValue.entrySet()) {
            int khash = it.getKey().hashCode();
            hash += (khash * 127) ^ it.getValue().hashCode();
        }
        return hash;
    }

    public String toString() {
        StringBuilder sbuf = new StringBuilder();
        sbuf.append('@');
        sbuf.append(getAnnotationType().getName());
        sbuf.append('(');
        int count = 0;
        for (Map.Entry<String, Object> it : this.elementsValue.entrySet()) {
            if (count > 0) {
                sbuf.append(", ");
            }
            sbuf.append(it.getKey());
            sbuf.append('=');
            sbuf.append(it.getValue());
            count++;
        }
        sbuf.append(')');
        return sbuf.toString();
    }
}
