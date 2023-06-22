package kawa;

import gnu.expr.Language;
import gnu.mapping.Environment;
import gnu.mapping.Future;
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

public class TelnetRepl extends Procedure0 {
    Language language;
    Socket socket;

    public TelnetRepl(Language language2, Socket socket2) {
        this.language = language2;
        this.socket = socket2;
    }

    public Object apply0() {
        try {
            Shell.run(this.language, Environment.getCurrent());
            return Values.empty;
        } finally {
            try {
                this.socket.close();
            } catch (IOException e) {
            }
        }
    }

    public static void serve(Language language2, Socket client) throws IOException {
        Telnet conn = new Telnet(client, true);
        OutputStream sout = conn.getOutputStream();
        InputStream sin = conn.getInputStream();
        OutPort out = new OutPort(sout, (Path) FilePath.valueOf("/dev/stdout"));
        new Future(new TelnetRepl(language2, client), new TtyInPort(sin, (Path) FilePath.valueOf("/dev/stdin"), out), out, out).start();
    }
}
