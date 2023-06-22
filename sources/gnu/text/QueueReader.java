package gnu.text;

import gnu.lists.CharSeq;
import java.io.Reader;

public class QueueReader extends Reader implements Appendable {
    boolean EOFseen;
    char[] buffer;
    int limit;
    int mark;
    int pos;
    int readAheadLimit;

    public boolean markSupported() {
        return true;
    }

    public synchronized void mark(int readAheadLimit2) {
        this.readAheadLimit = readAheadLimit2;
        this.mark = this.pos;
    }

    public synchronized void reset() {
        if (this.readAheadLimit > 0) {
            this.pos = this.mark;
        }
    }

    /* access modifiers changed from: package-private */
    public void resize(int len) {
        int cur_size = this.limit - this.pos;
        if (this.readAheadLimit <= 0 || this.pos - this.mark > this.readAheadLimit) {
            this.mark = this.pos;
        } else {
            cur_size = this.limit - this.mark;
        }
        char[] new_buffer = this.buffer.length < cur_size + len ? new char[((cur_size * 2) + len)] : this.buffer;
        System.arraycopy(this.buffer, this.mark, new_buffer, 0, cur_size);
        this.buffer = new_buffer;
        this.pos -= this.mark;
        this.mark = 0;
        this.limit = cur_size;
    }

    public QueueReader append(CharSequence csq) {
        if (csq == null) {
            csq = "null";
        }
        return append(csq, 0, csq.length());
    }

    public synchronized QueueReader append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = "null";
        }
        int len = end - start;
        reserveSpace(len);
        int sz = this.limit;
        char[] d = this.buffer;
        if (csq instanceof String) {
            ((String) csq).getChars(start, end, d, sz);
        } else if (csq instanceof CharSeq) {
            ((CharSeq) csq).getChars(start, end, d, sz);
        } else {
            int i = start;
            int j = sz;
            while (i < end) {
                int j2 = j + 1;
                d[j] = csq.charAt(i);
                i++;
                j = j2;
            }
        }
        this.limit = sz + len;
        notifyAll();
        return this;
    }

    public void append(char[] chars) {
        append(chars, 0, chars.length);
    }

    public synchronized void append(char[] chars, int off, int len) {
        reserveSpace(len);
        System.arraycopy(chars, off, this.buffer, this.limit, len);
        this.limit += len;
        notifyAll();
    }

    public synchronized QueueReader append(char ch) {
        reserveSpace(1);
        char[] cArr = this.buffer;
        int i = this.limit;
        this.limit = i + 1;
        cArr[i] = ch;
        notifyAll();
        return this;
    }

    public synchronized void appendEOF() {
        this.EOFseen = true;
    }

    /* access modifiers changed from: protected */
    public void reserveSpace(int len) {
        if (this.buffer == null) {
            this.buffer = new char[(len + 100)];
        } else if (this.buffer.length < this.limit + len) {
            resize(len);
        }
    }

    public synchronized boolean ready() {
        return this.pos < this.limit || this.EOFseen;
    }

    public void checkAvailable() {
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r1 = r4.buffer;
        r2 = r4.pos;
        r4.pos = r2 + 1;
        r0 = r1[r2];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read() {
        /*
            r4 = this;
            monitor-enter(r4)
        L_0x0001:
            int r1 = r4.pos     // Catch:{ all -> 0x0022 }
            int r2 = r4.limit     // Catch:{ all -> 0x0022 }
            if (r1 < r2) goto L_0x0017
            boolean r1 = r4.EOFseen     // Catch:{ all -> 0x0022 }
            if (r1 == 0) goto L_0x000e
            r0 = -1
        L_0x000c:
            monitor-exit(r4)
            return r0
        L_0x000e:
            r4.checkAvailable()     // Catch:{ all -> 0x0022 }
            r4.wait()     // Catch:{ InterruptedException -> 0x0015 }
            goto L_0x0001
        L_0x0015:
            r1 = move-exception
            goto L_0x0001
        L_0x0017:
            char[] r1 = r4.buffer     // Catch:{ all -> 0x0022 }
            int r2 = r4.pos     // Catch:{ all -> 0x0022 }
            int r3 = r2 + 1
            r4.pos = r3     // Catch:{ all -> 0x0022 }
            char r0 = r1[r2]     // Catch:{ all -> 0x0022 }
            goto L_0x000c
        L_0x0022:
            r1 = move-exception
            monitor-exit(r4)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.QueueReader.read():int");
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 127 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int read(char[] r4, int r5, int r6) {
        /*
            r3 = this;
            monitor-enter(r3)
            if (r6 != 0) goto L_0x000c
            r1 = 0
        L_0x0004:
            monitor-exit(r3)
            return r1
        L_0x0006:
            r3.checkAvailable()     // Catch:{ all -> 0x0031 }
            r3.wait()     // Catch:{ InterruptedException -> 0x002f }
        L_0x000c:
            int r1 = r3.pos     // Catch:{ all -> 0x0031 }
            int r2 = r3.limit     // Catch:{ all -> 0x0031 }
            if (r1 < r2) goto L_0x0018
            boolean r1 = r3.EOFseen     // Catch:{ all -> 0x0031 }
            if (r1 == 0) goto L_0x0006
            r1 = -1
            goto L_0x0004
        L_0x0018:
            int r1 = r3.limit     // Catch:{ all -> 0x0031 }
            int r2 = r3.pos     // Catch:{ all -> 0x0031 }
            int r0 = r1 - r2
            if (r6 <= r0) goto L_0x0021
            r6 = r0
        L_0x0021:
            char[] r1 = r3.buffer     // Catch:{ all -> 0x0031 }
            int r2 = r3.pos     // Catch:{ all -> 0x0031 }
            java.lang.System.arraycopy(r1, r2, r4, r5, r6)     // Catch:{ all -> 0x0031 }
            int r1 = r3.pos     // Catch:{ all -> 0x0031 }
            int r1 = r1 + r6
            r3.pos = r1     // Catch:{ all -> 0x0031 }
            r1 = r6
            goto L_0x0004
        L_0x002f:
            r1 = move-exception
            goto L_0x000c
        L_0x0031:
            r1 = move-exception
            monitor-exit(r3)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.text.QueueReader.read(char[], int, int):int");
    }

    public synchronized void close() {
        this.pos = 0;
        this.limit = 0;
        this.mark = 0;
        this.EOFseen = true;
        this.buffer = null;
    }
}
