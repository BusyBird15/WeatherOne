package gnu.xquery.util;

import androidx.core.app.NotificationCompat;
import gnu.mapping.Symbol;
import gnu.mapping.Values;

public class XQException extends RuntimeException {
    public static Symbol FOER0000_QNAME = Symbol.make("http://www.w3.org/2005/xqt-errors", "FOER0000", NotificationCompat.CATEGORY_ERROR);
    public Symbol code;
    public String description;
    public Object errorValue;

    public XQException(Symbol code2, String description2, Object errorValue2) {
        super(description2);
        this.code = code2;
        this.description = description2;
        this.errorValue = errorValue2;
    }

    public static void error() {
        throw new XQException(FOER0000_QNAME, (String) null, (Object) null);
    }

    public static void error(Symbol error) {
        throw new XQException(error, (String) null, (Object) null);
    }

    public static void error(Object error, String description2) {
        if (error == null || error == Values.empty) {
            error = FOER0000_QNAME;
        }
        throw new XQException((Symbol) error, description2, (Object) null);
    }

    public static void error(Object error, String description2, Object errorValue2) {
        if (error == null || error == Values.empty) {
            error = FOER0000_QNAME;
        }
        throw new XQException((Symbol) error, description2, errorValue2);
    }

    public String getMessage() {
        StringBuffer sbuf = new StringBuffer(100);
        if (this.description == null) {
            sbuf.append("XQuery-error");
        } else {
            sbuf.append(this.description);
        }
        if (this.code != null) {
            sbuf.append(" [");
            String prefix = this.code.getPrefix();
            if (prefix != null && prefix.length() > 0) {
                sbuf.append(prefix);
                sbuf.append(':');
            }
            sbuf.append(this.code.getLocalName());
            sbuf.append(']');
        }
        if (!(this.errorValue == null || this.errorValue == Values.empty)) {
            sbuf.append(" value: ");
            sbuf.append(this.errorValue);
        }
        return sbuf.toString();
    }
}
