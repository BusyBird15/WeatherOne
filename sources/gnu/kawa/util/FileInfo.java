package gnu.kawa.util;

import gnu.mapping.InPort;
import gnu.mapping.OutPort;
import gnu.text.Path;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* compiled from: FixupHtmlToc */
class FileInfo {
    static final Pattern childPat = Pattern.compile("<a .*</a>");
    static Hashtable fileMap = new Hashtable();
    static final Pattern linkPat = Pattern.compile(" href=['\"]([^'\"]*)['\"]");
    static final Pattern parentPat = Pattern.compile("<ul[^>]* parent=['\"]([^'\"]*)['\"]");
    StringBuffer beforeNavbarText;
    ByteArrayOutputStream bout;
    String[] childLinkText;
    OutPort cout;
    File file;
    FileInputStream fin;
    InPort in;
    int nchildren;
    StringBuffer newNavbarText;
    StringBuffer oldNavbarText;
    FileInfo parent;
    String parentName;
    String path;
    boolean scanned;
    boolean writeNeeded;

    FileInfo() {
    }

    public static FileInfo find(File file2) throws Throwable {
        String abs = file2.getCanonicalPath();
        FileInfo info = (FileInfo) fileMap.get(abs);
        if (info != null) {
            return info;
        }
        FileInfo info2 = new FileInfo();
        info2.file = file2;
        fileMap.put(abs, info2);
        return info2;
    }

    public void scan() throws Throwable {
        if (!this.scanned) {
            this.scanned = true;
            this.fin = new FileInputStream(this.file);
            this.in = new InPort((InputStream) new BufferedInputStream(this.fin));
            this.oldNavbarText = new StringBuffer();
            this.newNavbarText = new StringBuffer();
            if (this.writeNeeded) {
                this.bout = new ByteArrayOutputStream();
                this.cout = new OutPort((OutputStream) this.bout);
            }
            boolean inNavbar = false;
            boolean inChildList = false;
            Vector chvec = new Vector();
            while (true) {
                String line = this.in.readLine();
                if (line == null) {
                    break;
                }
                if (inNavbar) {
                    if (line.indexOf("<!--end-generated-navbar-->") >= 0) {
                        break;
                    }
                    this.oldNavbarText.append(line);
                    this.oldNavbarText.append(10);
                    if (inChildList) {
                        if (line.indexOf("<!--end-children-toc-->") >= 0) {
                            inChildList = false;
                        } else {
                            Matcher childMatcher = childPat.matcher(line);
                            if (childMatcher.find()) {
                                chvec.add(childMatcher.group());
                            }
                            Matcher parentMatcher = parentPat.matcher(line);
                            if (parentMatcher.find() && this.parentName == null) {
                                this.parentName = parentMatcher.group(1);
                            }
                        }
                    }
                    if (line.indexOf("<!--start-children-toc-->") >= 0) {
                        inChildList = true;
                    }
                } else if (line.indexOf("<!--start-generated-navbar-->") >= 0) {
                    inNavbar = true;
                }
                if (this.writeNeeded && !inNavbar) {
                    this.cout.println(line);
                }
            }
            String[] charr = new String[chvec.size()];
            this.nchildren = charr.length;
            chvec.copyInto(charr);
            this.childLinkText = charr;
            if (!this.writeNeeded) {
                this.in.close();
            }
            if (this.parentName != null) {
                FileInfo parentInfo = find(new File(this.file.toURI().resolve(this.parentName)));
                parentInfo.scan();
                this.parent = parentInfo;
            }
        }
    }

    public void writeLinks(int uplevel, StringBuffer out) throws Throwable {
        FileInfo current = this;
        FileInfo thischild = null;
        int i = uplevel;
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            thischild = current;
            current = current.parent;
        }
        if (uplevel == 0) {
            out.append("<!--start-children-toc-->\n");
        }
        if (uplevel != 0 || this.parentName == null) {
            out.append("<ul>\n");
        } else {
            out.append("<ul parent=\"");
            out.append(this.parentName);
            out.append("\">\n");
        }
        URI thisURI = this.file.toURI();
        URI currentURI = current.file.toURI();
        for (int i2 = 0; i2 < current.nchildren; i2++) {
            String linkText = current.childLinkText[i2];
            boolean ancestor = false;
            if (uplevel > 0) {
                Matcher linkMatcher = linkPat.matcher(linkText);
                linkMatcher.find();
                String hrefText = linkMatcher.group(1);
                linkText = linkMatcher.replaceFirst(" href=\"" + Path.relativize(currentURI.resolve(hrefText).toString(), thisURI.toString()) + "\"");
                int hash = hrefText.indexOf(35);
                if (hash >= 0) {
                    hrefText = hrefText.substring(0, hash);
                }
                ancestor = find(new File(currentURI.resolve(hrefText))) == thischild;
                if (!ancestor && uplevel > 1) {
                }
            }
            out.append("<li>");
            out.append(linkText);
            if (ancestor) {
                out.append(10);
                writeLinks(uplevel - 1, out);
            }
            out.append("</li>\n");
        }
        out.append("</ul>\n");
        if (uplevel == 0) {
            out.append("<!--end-children-toc-->\n");
        }
    }

    public void write() throws Throwable {
        int level = 0;
        FileInfo current = this;
        while (current.parent != null) {
            current = current.parent;
            level++;
        }
        this.cout.println("<!--start-generated-navbar-->");
        writeLinks(level, this.newNavbarText);
        this.cout.print((Object) this.newNavbarText);
        this.cout.println("<!--end-generated-navbar-->");
        while (true) {
            String line = this.in.readLine();
            if (line == null) {
                break;
            }
            this.cout.println(line);
        }
        new StringBuffer();
        this.in.close();
        if (this.oldNavbarText.toString().equals(this.newNavbarText.toString())) {
            System.err.println("fixup " + this.file + " - no change");
            return;
        }
        FileOutputStream outs = new FileOutputStream(this.file);
        outs.write(this.bout.toByteArray());
        outs.close();
        System.err.println("fixup " + this.file + " - updated");
    }
}
