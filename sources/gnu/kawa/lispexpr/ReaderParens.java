package gnu.kawa.lispexpr;

import gnu.mapping.Values;
import gnu.text.Lexer;
import gnu.text.LineBufferedReader;
import gnu.text.SyntaxException;
import java.io.IOException;

public class ReaderParens extends ReadTableEntry {
    private static ReaderParens instance;
    char close;
    Object command;
    int kind;
    char open;

    public int getKind() {
        return this.kind;
    }

    public static ReaderParens getInstance(char open2, char close2) {
        return getInstance(open2, close2, 5);
    }

    public static ReaderParens getInstance(char open2, char close2, int kind2) {
        if (open2 != '(' || close2 != ')' || kind2 != 5) {
            return new ReaderParens(open2, close2, kind2, (Object) null);
        }
        if (instance == null) {
            instance = new ReaderParens(open2, close2, kind2, (Object) null);
        }
        return instance;
    }

    public static ReaderParens getInstance(char open2, char close2, int kind2, Object command2) {
        if (command2 == null) {
            return getInstance(open2, close2, kind2);
        }
        return new ReaderParens(open2, close2, kind2, command2);
    }

    public ReaderParens(char open2, char close2, int kind2, Object command2) {
        this.open = open2;
        this.close = close2;
        this.kind = kind2;
        this.command = command2;
    }

    public Object read(Lexer in, int ch, int count) throws IOException, SyntaxException {
        Object r = readList((LispReader) in, ch, count, this.close);
        if (this.command == null) {
            return r;
        }
        LineBufferedReader port = in.getPort();
        Object p = ((LispReader) in).makePair(this.command, port.getLineNumber(), port.getColumnNumber());
        ((LispReader) in).setCdr(p, r);
        return p;
    }

    public static Object readList(LispReader lexer, int ch, int count, int close2) throws IOException, SyntaxException {
        ReadTableEntry entry;
        Object list;
        Object value;
        LineBufferedReader port = lexer.getPort();
        char saveReadState = lexer.pushNesting(close2 == 93 ? '[' : '(');
        int startLine = port.getLineNumber();
        int startColumn = port.getColumnNumber();
        Object last = null;
        try {
            Object list2 = lexer.makeNil();
            boolean sawDot = false;
            boolean sawDotCdr = false;
            ReadTable readTable = ReadTable.getCurrent();
            while (true) {
                int line = port.getLineNumber();
                int column = port.getColumnNumber();
                int ch2 = port.read();
                if (ch2 == close2) {
                    break;
                }
                if (ch2 < 0) {
                    lexer.eofError("unexpected EOF in list starting here", startLine + 1, startColumn);
                }
                if (ch2 == 46) {
                    ch2 = port.peek();
                    entry = readTable.lookup(ch2);
                    int kind2 = entry.getKind();
                    if (kind2 == 1 || kind2 == 5 || kind2 == 0) {
                        port.skip();
                        column++;
                        if (ch2 == close2) {
                            lexer.error("unexpected '" + ((char) close2) + "' after '.'");
                            break;
                        }
                        if (ch2 < 0) {
                            lexer.eofError("unexpected EOF in list starting here", startLine + 1, startColumn);
                        }
                        if (sawDot) {
                            lexer.error("multiple '.' in list");
                            sawDotCdr = false;
                            list2 = lexer.makeNil();
                            last = null;
                        }
                        sawDot = true;
                    } else {
                        ch2 = 46;
                        entry = ReadTableEntry.getConstituentInstance();
                    }
                    list = list2;
                } else {
                    entry = readTable.lookup(ch2);
                    list = list2;
                }
                Object value2 = lexer.readValues(ch2, entry, readTable);
                if (value2 == Values.empty) {
                    list2 = list;
                } else {
                    Object value3 = lexer.handlePostfix(value2, readTable, line, column);
                    if (sawDotCdr) {
                        lexer.error("multiple values after '.'");
                        last = null;
                        list2 = lexer.makeNil();
                        sawDotCdr = false;
                    } else {
                        if (sawDot) {
                            sawDotCdr = true;
                            value = value3;
                        } else {
                            if (last == null) {
                                line = startLine;
                                column = startColumn - 1;
                            }
                            value = lexer.makePair(value3, line, column);
                        }
                        if (last == null) {
                            list = value;
                        } else {
                            lexer.setCdr(last, value);
                        }
                        last = value;
                        list2 = list;
                    }
                }
            }
            return list2;
        } finally {
            lexer.popNesting(saveReadState);
        }
    }
}
