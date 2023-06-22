package gnu.text;

import gnu.mapping.WrappedException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class URLPath extends URIPath {
    final URL url;

    URLPath(URL url2) {
        super(toUri(url2));
        this.url = url2;
    }

    public static URLPath valueOf(URL url2) {
        return new URLPath(url2);
    }

    public boolean isAbsolute() {
        return true;
    }

    public long getLastModified() {
        return getLastModified(this.url);
    }

    public static long getLastModified(URL url2) {
        try {
            return url2.openConnection().getLastModified();
        } catch (Throwable th) {
            return 0;
        }
    }

    public long getContentLength() {
        return (long) getContentLength(this.url);
    }

    public static int getContentLength(URL url2) {
        try {
            return url2.openConnection().getContentLength();
        } catch (Throwable th) {
            return -1;
        }
    }

    public URL toURL() {
        return this.url;
    }

    public static URI toUri(URL url2) {
        try {
            return url2.toURI();
        } catch (Throwable ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    public URI toUri() {
        return toUri(this.url);
    }

    public String toURIString() {
        return this.url.toString();
    }

    public Path resolve(String relative) {
        try {
            return valueOf(new URL(this.url, relative));
        } catch (Throwable ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    public static InputStream openInputStream(URL url2) throws IOException {
        return url2.openConnection().getInputStream();
    }

    public InputStream openInputStream() throws IOException {
        return openInputStream(this.url);
    }

    public static OutputStream openOutputStream(URL url2) throws IOException {
        String str = url2.toString();
        if (str.startsWith("file:")) {
            try {
                return new FileOutputStream(new File(new URI(str)));
            } catch (Throwable th) {
            }
        }
        URLConnection conn = url2.openConnection();
        conn.setDoInput(false);
        conn.setDoOutput(true);
        return conn.getOutputStream();
    }

    public OutputStream openOutputStream() throws IOException {
        return openOutputStream(this.url);
    }

    public static URLPath classResourcePath(Class clas) {
        URL url2;
        try {
            url2 = ResourceStreamHandler.makeURL(clas);
        } catch (SecurityException e) {
            url2 = clas.getClassLoader().getResource(clas.getName().replace('.', '/') + ".class");
        } catch (Throwable ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
        return valueOf(url2);
    }
}
