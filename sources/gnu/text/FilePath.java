package gnu.text;

import gnu.lists.FString;
import gnu.mapping.WrappedException;
import gnu.mapping.WrongType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

public class FilePath extends Path implements Comparable<FilePath> {
    final File file;
    final String path;

    private FilePath(File file2) {
        this.file = file2;
        this.path = file2.toString();
    }

    private FilePath(File file2, String path2) {
        this.file = file2;
        this.path = path2;
    }

    public static FilePath valueOf(String str) {
        return new FilePath(new File(str), str);
    }

    public static FilePath valueOf(File file2) {
        return new FilePath(file2);
    }

    public static FilePath coerceToFilePathOrNull(Object path2) {
        String str;
        if (path2 instanceof FilePath) {
            return (FilePath) path2;
        }
        if (path2 instanceof URIPath) {
            return valueOf(new File(((URIPath) path2).uri));
        }
        if (path2 instanceof URI) {
            return valueOf(new File((URI) path2));
        }
        if (path2 instanceof File) {
            return valueOf((File) path2);
        }
        if (path2 instanceof FString) {
            str = path2.toString();
        } else if (!(path2 instanceof String)) {
            return null;
        } else {
            str = (String) path2;
        }
        return valueOf(str);
    }

    public static FilePath makeFilePath(Object arg) {
        FilePath path2 = coerceToFilePathOrNull(arg);
        if (path2 != null) {
            return path2;
        }
        throw new WrongType((String) null, -4, arg, "filepath");
    }

    public boolean isAbsolute() {
        return this == Path.userDirPath || this.file.isAbsolute();
    }

    public boolean isDirectory() {
        int len;
        char last;
        if (this.file.isDirectory()) {
            return true;
        }
        if (this.file.exists() || (len = this.path.length()) <= 0 || ((last = this.path.charAt(len - 1)) != '/' && last != File.separatorChar)) {
            return false;
        }
        return true;
    }

    public boolean delete() {
        return toFile().delete();
    }

    public long getLastModified() {
        return this.file.lastModified();
    }

    public boolean exists() {
        return this.file.exists();
    }

    public long getContentLength() {
        long length = this.file.length();
        if (length != 0 || this.file.exists()) {
            return length;
        }
        return -1;
    }

    public String getPath() {
        return this.file.getPath();
    }

    public String getLast() {
        return this.file.getName();
    }

    public FilePath getParent() {
        File parent = this.file.getParentFile();
        if (parent == null) {
            return null;
        }
        return valueOf(parent);
    }

    public int compareTo(FilePath path2) {
        return this.file.compareTo(path2.file);
    }

    public boolean equals(Object obj) {
        return (obj instanceof FilePath) && this.file.equals(((FilePath) obj).file);
    }

    public int hashCode() {
        return this.file.hashCode();
    }

    public String toString() {
        return this.path;
    }

    public File toFile() {
        return this.file;
    }

    public URL toURL() {
        if (this == Path.userDirPath) {
            return resolve("").toURL();
        }
        if (!isAbsolute()) {
            return getAbsolute().toURL();
        }
        try {
            return this.file.toURI().toURL();
        } catch (Throwable ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    private static URI toUri(File file2) {
        try {
            if (file2.isAbsolute()) {
                return file2.toURI();
            }
            String fname = file2.toString();
            char fileSep = File.separatorChar;
            if (fileSep != '/') {
                fname = fname.replace(fileSep, '/');
            }
            return new URI((String) null, (String) null, fname, (String) null);
        } catch (Throwable ex) {
            throw WrappedException.wrapIfNeeded(ex);
        }
    }

    public URI toUri() {
        if (this == Path.userDirPath) {
            return resolve("").toURI();
        }
        return toUri(this.file);
    }

    public InputStream openInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    public OutputStream openOutputStream() throws IOException {
        return new FileOutputStream(this.file);
    }

    public String getScheme() {
        if (isAbsolute()) {
            return "file";
        }
        return null;
    }

    public Path resolve(String relative) {
        File nfile;
        if (Path.uriSchemeSpecified(relative)) {
            return URLPath.valueOf(relative);
        }
        File rfile = new File(relative);
        if (rfile.isAbsolute()) {
            return valueOf(rfile);
        }
        char sep = File.separatorChar;
        if (sep != '/') {
            relative = relative.replace('/', sep);
        }
        if (this == Path.userDirPath) {
            nfile = new File(System.getProperty("user.dir"), relative);
        } else {
            nfile = new File(isDirectory() ? this.file : this.file.getParentFile(), relative);
        }
        return valueOf(nfile);
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    public Path getCanonical() {
        try {
            File canon = this.file.getCanonicalFile();
            if (!canon.equals(this.file)) {
                return valueOf(canon);
            }
            return this;
        } catch (Throwable th) {
            return this;
        }
    }
}
