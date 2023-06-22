package gnu.kawa.lispexpr;

import gnu.lists.PairWithPosition;
import gnu.text.Lexer;
import gnu.text.SyntaxException;
import java.io.IOException;

public class ReaderQuote extends ReadTableEntry {
    Object magicSymbol;
    Object magicSymbol2;
    char next;

    public ReaderQuote(Object magicSymbol3) {
        this.magicSymbol = magicSymbol3;
    }

    public ReaderQuote(Object magicSymbol3, char next2, Object magicSymbol22) {
        this.next = next2;
        this.magicSymbol = magicSymbol3;
        this.magicSymbol2 = magicSymbol22;
    }

    public Object read(Lexer in, int ch, int count) throws IOException, SyntaxException {
        LispReader reader = (LispReader) in;
        String file = reader.getName();
        int line1 = reader.getLineNumber() + 1;
        int column1 = reader.getColumnNumber() + 1;
        Object magic = this.magicSymbol;
        if (this.next != 0) {
            int ch2 = reader.read();
            if (ch2 == this.next) {
                magic = this.magicSymbol2;
            } else if (ch2 >= 0) {
                reader.unread(ch2);
            }
        }
        return PairWithPosition.make(magic, PairWithPosition.make(reader.readObject(), reader.makeNil(), file, reader.getLineNumber() + 1, reader.getColumnNumber() + 1), file, line1, column1);
    }
}
