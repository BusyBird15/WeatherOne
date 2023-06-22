package gnu.bytecode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ListCodeSize {
    public static void usage() {
        System.err.println("Usage: class methodname ...");
        System.exit(-1);
    }

    static void print(Method method) {
        System.out.print(method);
        CodeAttr code = method.getCode();
        if (code == null) {
            System.out.print(": no code");
        } else {
            System.out.print(": ");
            System.out.print(code.getPC());
            System.out.print(" bytes");
        }
        System.out.println();
    }

    public static final void main(String[] args) {
        if (args.length == 0) {
            usage();
        }
        String filename = args[0];
        try {
            InputStream inp = new FileInputStream(filename);
            ClassType ctype = new ClassType();
            new ClassFileInput(ctype, inp);
            if (args.length == 1) {
                for (Method method = ctype.getMethods(); method != null; method = method.getNext()) {
                    print(method);
                }
                return;
            }
            for (int i = 1; i < args.length; i++) {
                for (Method method2 = ctype.getMethods(); method2 != null; method2 = method2.getNext()) {
                    StringBuffer sbuf = new StringBuffer();
                    sbuf.append(method2.getName());
                    method2.listParameters(sbuf);
                    sbuf.append(method2.getReturnType().getName());
                    if (sbuf.toString().startsWith(args[i])) {
                        print(method2);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File " + filename + " not found");
            System.exit(-1);
        } catch (IOException e2) {
            System.err.println(e2);
            e2.printStackTrace();
            System.exit(-1);
        }
    }
}
