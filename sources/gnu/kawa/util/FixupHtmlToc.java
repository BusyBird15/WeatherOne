package gnu.kawa.util;

import java.io.File;

public class FixupHtmlToc {
    static FileInfo[] argFiles;

    public static void main(String[] args) {
        try {
            argFiles = new FileInfo[args.length];
            for (int i = 0; i < args.length; i++) {
                FileInfo info = FileInfo.find(new File(args[i]));
                info.writeNeeded = true;
                argFiles[i] = info;
            }
            for (int i2 = 0; i2 < args.length; i2++) {
                argFiles[i2].scan();
                argFiles[i2].write();
            }
        } catch (Throwable ex) {
            System.err.println("caught " + ex);
            ex.printStackTrace();
        }
    }
}
