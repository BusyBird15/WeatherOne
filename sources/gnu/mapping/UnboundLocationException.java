package gnu.mapping;

import gnu.text.SourceLocator;

public class UnboundLocationException extends RuntimeException {
    int column;
    String filename;
    int line;
    Location location;
    public Object symbol;

    public UnboundLocationException() {
    }

    public UnboundLocationException(Object symbol2) {
        this.symbol = symbol2;
    }

    public UnboundLocationException(Object symbol2, String filename2, int line2, int column2) {
        this.symbol = symbol2;
        this.filename = filename2;
        this.line = line2;
        this.column = column2;
    }

    public UnboundLocationException(Object symbol2, SourceLocator location2) {
        this.symbol = symbol2;
        if (location2 != null) {
            this.filename = location2.getFileName();
            this.line = location2.getLineNumber();
            this.column = location2.getColumnNumber();
        }
    }

    public UnboundLocationException(Location loc) {
        this.location = loc;
    }

    public UnboundLocationException(Object symbol2, String message) {
        super(message);
        this.symbol = symbol2;
    }

    public void setLine(String filename2, int line2, int column2) {
        this.filename = filename2;
        this.line = line2;
        this.column = column2;
    }

    public String getMessage() {
        Symbol name;
        String msg = super.getMessage();
        if (msg != null) {
            return msg;
        }
        StringBuffer sbuf = new StringBuffer();
        if (this.filename != null || this.line > 0) {
            if (this.filename != null) {
                sbuf.append(this.filename);
            }
            if (this.line >= 0) {
                sbuf.append(':');
                sbuf.append(this.line);
                if (this.column > 0) {
                    sbuf.append(':');
                    sbuf.append(this.column);
                }
            }
            sbuf.append(": ");
        }
        if (this.location == null) {
            name = null;
        } else {
            name = this.location.getKeySymbol();
        }
        if (name != null) {
            sbuf.append("unbound location ");
            sbuf.append(name);
            Object property = this.location.getKeyProperty();
            if (property != null) {
                sbuf.append(" (property ");
                sbuf.append(property);
                sbuf.append(')');
            }
        } else if (this.symbol != null) {
            sbuf.append("unbound location ");
            sbuf.append(this.symbol);
        } else {
            sbuf.append("unbound location");
        }
        return sbuf.toString();
    }

    public String toString() {
        return getMessage();
    }
}
