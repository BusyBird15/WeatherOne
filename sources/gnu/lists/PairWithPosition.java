package gnu.lists;

import gnu.text.SourceLocator;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class PairWithPosition extends ImmutablePair implements SourceLocator {
    String filename;
    int position;

    public final void setFile(String filename2) {
        this.filename = filename2;
    }

    public final void setLine(int lineno, int colno) {
        if (lineno < 0) {
            lineno = 0;
        }
        if (colno < 0) {
            colno = 0;
        }
        this.position = (lineno << 12) + colno;
    }

    public final void setLine(int lineno) {
        setLine(lineno, 0);
    }

    public final String getFileName() {
        return this.filename;
    }

    public String getPublicId() {
        return null;
    }

    public String getSystemId() {
        return this.filename;
    }

    public final int getLineNumber() {
        int line = this.position >> 12;
        if (line == 0) {
            return -1;
        }
        return line;
    }

    public final int getColumnNumber() {
        int column = this.position & 4095;
        if (column == 0) {
            return -1;
        }
        return column;
    }

    public boolean isStableSourceLocation() {
        return true;
    }

    public PairWithPosition() {
    }

    public PairWithPosition(SourceLocator where, Object car, Object cdr) {
        super(car, cdr);
        this.filename = where.getFileName();
        setLine(where.getLineNumber(), where.getColumnNumber());
    }

    public PairWithPosition(Object car, Object cdr) {
        super(car, cdr);
    }

    public static PairWithPosition make(Object car, Object cdr, String filename2, int line, int column) {
        PairWithPosition pair = new PairWithPosition(car, cdr);
        pair.filename = filename2;
        pair.setLine(line, column);
        return pair;
    }

    public static PairWithPosition make(Object car, Object cdr, String filename2, int position2) {
        PairWithPosition pair = new PairWithPosition(car, cdr);
        pair.filename = filename2;
        pair.position = position2;
        return pair;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.car);
        out.writeObject(this.cdr);
        out.writeObject(this.filename);
        out.writeInt(this.position);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.car = in.readObject();
        this.cdr = in.readObject();
        this.filename = (String) in.readObject();
        this.position = in.readInt();
    }
}
