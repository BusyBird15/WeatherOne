package gnu.kawa.lispexpr;

import gnu.expr.QuoteExp;
import gnu.lists.FVector;
import gnu.mapping.InPort;
import gnu.mapping.Values;
import gnu.text.Lexer;
import gnu.text.LineBufferedReader;
import gnu.text.SyntaxException;
import java.io.IOException;
import java.util.Vector;

public class ReaderVector extends ReadTableEntry {
    char close;

    public ReaderVector(char close2) {
        this.close = close2;
    }

    public Object read(Lexer in, int ch, int count) throws IOException, SyntaxException {
        return readVector((LispReader) in, in.getPort(), count, this.close);
    }

    public static FVector readVector(LispReader lexer, LineBufferedReader port, int count, char close2) throws IOException, SyntaxException {
        char saveReadState = ' ';
        if (port instanceof InPort) {
            saveReadState = ((InPort) port).readState;
            ((InPort) port).readState = close2 == ']' ? '[' : '(';
        }
        try {
            Vector vec = new Vector();
            ReadTable rtable = ReadTable.getCurrent();
            while (true) {
                int ch = lexer.read();
                if (ch < 0) {
                    lexer.eofError("unexpected EOF in vector");
                }
                if (ch == close2) {
                    break;
                }
                Object value = lexer.readValues(ch, rtable);
                if (value instanceof Values) {
                    for (Object addElement : ((Values) value).getValues()) {
                        vec.addElement(addElement);
                    }
                } else {
                    if (value == QuoteExp.voidExp) {
                        value = Values.empty;
                    }
                    vec.addElement(value);
                }
            }
            Object[] objs = new Object[vec.size()];
            vec.copyInto(objs);
            return new FVector(objs);
        } finally {
            if (port instanceof InPort) {
                ((InPort) port).readState = saveReadState;
            }
        }
    }
}
