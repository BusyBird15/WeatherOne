package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.repackaged.org.json.HTTP;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public final class CsvUtil {
    private CsvUtil() {
    }

    public static YailList fromCsvTable(String csvString) throws Exception {
        CsvParser csvParser = new CsvParser(new StringReader(csvString));
        ArrayList<YailList> csvList = new ArrayList<>();
        while (csvParser.hasNext()) {
            csvList.add(YailList.makeList((List) csvParser.next()));
        }
        csvParser.throwAnyProblem();
        return YailList.makeList((List) csvList);
    }

    public static YailList fromCsvRow(String csvString) throws Exception {
        CsvParser csvParser = new CsvParser(new StringReader(csvString));
        if (csvParser.hasNext()) {
            YailList row = YailList.makeList((List) csvParser.next());
            if (csvParser.hasNext()) {
                throw new IllegalArgumentException("CSV text has multiple rows. Expected just one row.");
            }
            csvParser.throwAnyProblem();
            return row;
        }
        throw new IllegalArgumentException("CSV text cannot be parsed as a row.");
    }

    public static String toCsvRow(YailList csvRow) {
        StringBuilder csvStringBuilder = new StringBuilder();
        makeCsvRow(csvRow, csvStringBuilder);
        return csvStringBuilder.toString();
    }

    public static String toCsvTable(YailList csvList) {
        StringBuilder csvStringBuilder = new StringBuilder();
        for (Object rowObj : csvList.toArray()) {
            makeCsvRow((YailList) rowObj, csvStringBuilder);
            csvStringBuilder.append(HTTP.CRLF);
        }
        return csvStringBuilder.toString();
    }

    private static void makeCsvRow(YailList row, StringBuilder csvStringBuilder) {
        String fieldDelim = "";
        for (Object fieldObj : row.toArray()) {
            csvStringBuilder.append(fieldDelim).append("\"").append(fieldObj.toString().replaceAll("\"", "\"\"")).append("\"");
            fieldDelim = ",";
        }
    }

    private static class CsvParser implements Iterator<List<String>> {
        private final Pattern ESCAPED_QUOTE_PATTERN = Pattern.compile("\"\"");
        private final char[] buf = new char[10240];
        private int cellLength = -1;
        private int delimitedCellLength = -1;
        private final Reader in;
        private Exception lastException;
        private int limit;
        private boolean opened = true;
        private int pos;
        private long previouslyRead;

        public CsvParser(Reader in2) {
            this.in = in2;
        }

        public void skip(long charPosition) throws IOException {
            int n;
            while (charPosition > 0 && (n = this.in.read(this.buf, 0, Math.min((int) charPosition, this.buf.length))) >= 0) {
                this.previouslyRead += (long) n;
                charPosition -= (long) n;
            }
        }

        public boolean hasNext() {
            if (this.limit == 0) {
                fill();
            }
            return (this.pos < this.limit || indexAfterCompactionAndFilling(this.pos) < this.limit) && lookingAtCell();
        }

        /* JADX WARNING: Removed duplicated region for block: B:13:0x0054  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x006f  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0092  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x0094  */
        /* JADX WARNING: Removed duplicated region for block: B:3:0x0012  */
        /* JADX WARNING: Removed duplicated region for block: B:8:0x0035  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.ArrayList<java.lang.String> next() {
            /*
                r12 = this;
                r11 = 44
                r5 = 1
                r6 = 0
                java.util.ArrayList r3 = com.google.appinventor.components.runtime.collect.Lists.newArrayList()
            L_0x0008:
                char[] r7 = r12.buf
                int r8 = r12.pos
                char r7 = r7[r8]
                r8 = 34
                if (r7 == r8) goto L_0x006f
                java.lang.String r7 = new java.lang.String
                char[] r8 = r12.buf
                int r9 = r12.pos
                int r10 = r12.cellLength
                r7.<init>(r8, r9, r10)
                java.lang.String r7 = r7.trim()
                r3.add(r7)
            L_0x0024:
                int r7 = r12.delimitedCellLength
                if (r7 <= 0) goto L_0x0092
                char[] r7 = r12.buf
                int r8 = r12.pos
                int r9 = r12.delimitedCellLength
                int r8 = r8 + r9
                int r8 = r8 + -1
                char r7 = r7[r8]
                if (r7 != r11) goto L_0x0092
                r4 = r5
            L_0x0036:
                int r7 = r12.pos
                int r8 = r12.delimitedCellLength
                int r7 = r7 + r8
                r12.pos = r7
                r7 = -1
                r12.cellLength = r7
                r12.delimitedCellLength = r7
                int r2 = r12.limit
                int r7 = r12.pos
                int r8 = r12.limit
                if (r7 < r8) goto L_0x0054
                int r7 = r12.pos
                int r7 = r12.indexAfterCompactionAndFilling(r7)
                int r8 = r12.limit
                if (r7 >= r8) goto L_0x0094
            L_0x0054:
                r1 = r5
            L_0x0055:
                if (r1 != 0) goto L_0x0064
                char[] r7 = r12.buf
                int r8 = r2 + -1
                char r7 = r7[r8]
                if (r7 != r11) goto L_0x0064
                java.lang.String r7 = ""
                r3.add(r7)
            L_0x0064:
                if (r4 == 0) goto L_0x006e
                if (r1 == 0) goto L_0x006e
                boolean r7 = r12.lookingAtCell()
                if (r7 != 0) goto L_0x0008
            L_0x006e:
                return r3
            L_0x006f:
                java.lang.String r0 = new java.lang.String
                char[] r7 = r12.buf
                int r8 = r12.pos
                int r8 = r8 + 1
                int r9 = r12.cellLength
                int r9 = r9 + -2
                r0.<init>(r7, r8, r9)
                java.util.regex.Pattern r7 = r12.ESCAPED_QUOTE_PATTERN
                java.util.regex.Matcher r7 = r7.matcher(r0)
                java.lang.String r8 = "\""
                java.lang.String r7 = r7.replaceAll(r8)
                java.lang.String r7 = r7.trim()
                r3.add(r7)
                goto L_0x0024
            L_0x0092:
                r4 = r6
                goto L_0x0036
            L_0x0094:
                r1 = r6
                goto L_0x0055
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.CsvUtil.CsvParser.next():java.util.ArrayList");
        }

        public long getCharPosition() {
            return this.previouslyRead + ((long) this.pos);
        }

        private int indexAfterCompactionAndFilling(int i) {
            if (this.pos > 0) {
                i = compact(i);
            }
            fill();
            return i;
        }

        private int compact(int i) {
            int oldPos = this.pos;
            this.pos = 0;
            int toMove = this.limit - oldPos;
            if (toMove > 0) {
                System.arraycopy(this.buf, oldPos, this.buf, 0, toMove);
            }
            this.limit -= oldPos;
            this.previouslyRead += (long) oldPos;
            return i - oldPos;
        }

        private void fill() {
            int toFill = this.buf.length - this.limit;
            while (this.opened && toFill > 0) {
                try {
                    int n = this.in.read(this.buf, this.limit, toFill);
                    if (n == -1) {
                        this.opened = false;
                    } else {
                        this.limit += n;
                        toFill -= n;
                    }
                } catch (IOException e) {
                    this.lastException = e;
                    this.opened = false;
                }
            }
        }

        private boolean lookingAtCell() {
            return this.buf[this.pos] == '\"' ? findUnescapedEndQuote(this.pos + 1) : findUnquotedCellEnd(this.pos);
        }

        private boolean findUnescapedEndQuote(int i) {
            while (true) {
                if (i >= this.limit && (i = indexAfterCompactionAndFilling(i)) >= this.limit) {
                    this.lastException = new IllegalArgumentException("Syntax Error. unclosed quoted cell");
                    return false;
                } else if (this.buf[i] != '\"' || ((i = checkedIndex(i + 1)) != this.limit && this.buf[i] == '\"')) {
                    i++;
                }
            }
            this.cellLength = i - this.pos;
            return findDelimOrEnd(i);
        }

        private boolean findDelimOrEnd(int i) {
            while (true) {
                if (i < this.limit || (i = indexAfterCompactionAndFilling(i)) < this.limit) {
                    switch (this.buf[i]) {
                        case 9:
                        case ' ':
                            i++;
                        case 10:
                        case ',':
                            this.delimitedCellLength = checkedIndex(i + 1) - this.pos;
                            return true;
                        case 13:
                            int j = checkedIndex(i + 1);
                            if (this.buf[j] == 10) {
                                j = checkedIndex(j + 1);
                            }
                            this.delimitedCellLength = j - this.pos;
                            return true;
                        default:
                            this.lastException = new IOException("Syntax Error: non-whitespace between closing quote and delimiter or end");
                            return false;
                    }
                } else {
                    this.delimitedCellLength = this.limit - this.pos;
                    return true;
                }
            }
        }

        private int checkedIndex(int i) {
            return i < this.limit ? i : indexAfterCompactionAndFilling(i);
        }

        private boolean findUnquotedCellEnd(int i) {
            while (true) {
                if (i < this.limit || (i = indexAfterCompactionAndFilling(i)) < this.limit) {
                    switch (this.buf[i]) {
                        case 10:
                        case ',':
                            this.cellLength = i - this.pos;
                            this.delimitedCellLength = this.cellLength + 1;
                            return true;
                        case 13:
                            this.cellLength = i - this.pos;
                            int j = checkedIndex(i + 1);
                            if (this.buf[j] == 10) {
                                j = checkedIndex(j + 1);
                            }
                            this.delimitedCellLength = j - this.pos;
                            return true;
                        case '\"':
                            this.lastException = new IllegalArgumentException("Syntax Error: quote in unquoted cell");
                            return false;
                        default:
                            i++;
                    }
                } else {
                    int i2 = this.limit - this.pos;
                    this.cellLength = i2;
                    this.delimitedCellLength = i2;
                    return true;
                }
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public void throwAnyProblem() throws Exception {
            if (this.lastException != null) {
                throw this.lastException;
            }
        }
    }
}
