package com.google.appinventor.components.runtime.util;

import android.util.Log;
import gnu.expr.Language;
import gnu.mapping.Environment;
import gnu.mapping.OutPort;
import gnu.mapping.Procedure0;
import gnu.mapping.TtyInPort;
import gnu.mapping.Values;
import gnu.text.FilePath;
import gnu.text.Path;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import kawa.Shell;
import kawa.Telnet;

public class TelnetRepl extends Procedure0 {
    private static final int REPL_STACK_SIZE = 262144;
    Language language;
    Socket socket;

    public TelnetRepl(Language language2, Socket socket2) {
        this.language = language2;
        this.socket = socket2;
    }

    public Object apply0() {
        Thread thread = Thread.currentThread();
        if (thread.getContextClassLoader() == null) {
            thread.setContextClassLoader(Telnet.class.getClassLoader());
        }
        try {
            Shell.run(this.language, Environment.getCurrent());
            Values values = Values.empty;
            try {
                this.socket.close();
            } catch (IOException e) {
            }
            return values;
        } catch (RuntimeException e2) {
            Log.d("TelnetRepl", "Repl is exiting with error " + e2.getMessage());
            e2.printStackTrace();
            throw e2;
        } catch (Throwable th) {
            try {
                this.socket.close();
            } catch (IOException e3) {
            }
            throw th;
        }
    }

    public static Thread serve(Language language2, Socket client) throws IOException {
        Telnet conn = new Telnet(client, true);
        OutputStream sout = conn.getOutputStream();
        InputStream sin = conn.getInputStream();
        OutPort out = new OutPort(sout, (Path) FilePath.valueOf("/dev/stdout"));
        Thread thread = new BiggerFuture(new TelnetRepl(language2, client), new TtyInPort(sin, (Path) FilePath.valueOf("/dev/stdin"), out), out, out, "Telnet Repl Thread", 262144);
        thread.start();
        return thread;
    }
}
