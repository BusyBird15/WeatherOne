package com.google.appinventor.components.runtime.multidex;

import androidx.core.internal.view.SupportMenu;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

class ZipEntryReader {
    private static final long CENSIG = 33639248;
    private static final int GPBF_ENCRYPTED_FLAG = 1;
    private static final int GPBF_UNSUPPORTED_MASK = 1;
    static final Charset UTF_8 = Charset.forName("UTF-8");

    ZipEntryReader() {
    }

    static ZipEntry readEntry(ByteBuffer in) throws IOException {
        if (((long) in.getInt()) != CENSIG) {
            throw new ZipException("Central Directory Entry not found");
        }
        in.position(8);
        int gpbf = in.getShort() & SupportMenu.USER_MASK;
        if ((gpbf & 1) != 0) {
            throw new ZipException("Invalid General Purpose Bit Flag: " + gpbf);
        }
        int compressionMethod = in.getShort() & SupportMenu.USER_MASK;
        int time = in.getShort() & SupportMenu.USER_MASK;
        int modDate = in.getShort() & SupportMenu.USER_MASK;
        long crc = ((long) in.getInt()) & 4294967295L;
        long compressedSize = ((long) in.getInt()) & 4294967295L;
        long size = ((long) in.getInt()) & 4294967295L;
        int nameLength = in.getShort() & SupportMenu.USER_MASK;
        int extraLength = in.getShort() & 65535;
        int commentByteCount = in.getShort() & 65535;
        in.position(42);
        long j = ((long) in.getInt()) & 4294967295L;
        byte[] nameBytes = new byte[nameLength];
        in.get(nameBytes, 0, nameBytes.length);
        ZipEntry entry = new ZipEntry(new String(nameBytes, 0, nameBytes.length, UTF_8));
        entry.setMethod(compressionMethod);
        entry.setTime(getTime(time, modDate));
        entry.setCrc(crc);
        entry.setCompressedSize(compressedSize);
        entry.setSize(size);
        if (commentByteCount > 0) {
            byte[] commentBytes = new byte[commentByteCount];
            in.get(commentBytes, 0, commentByteCount);
            entry.setComment(new String(commentBytes, 0, commentBytes.length, UTF_8));
        }
        if (extraLength > 0) {
            byte[] extra = new byte[extraLength];
            in.get(extra, 0, extraLength);
            entry.setExtra(extra);
        }
        return entry;
    }

    private static long getTime(int time, int modDate) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(14, 0);
        cal.set(((modDate >> 9) & 127) + 1980, ((modDate >> 5) & 15) - 1, modDate & 31, (time >> 11) & 31, (time >> 5) & 63, (time & 31) << 1);
        return cal.getTime().getTime();
    }
}
