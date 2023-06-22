package com.google.appinventor.components.runtime.util;

import android.util.Log;
import com.google.appinventor.components.runtime.repackaged.org.json.HTTP;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class NanoHTTPD {
    public static final String HTTP_BADREQUEST = "400 Bad Request";
    public static final String HTTP_FORBIDDEN = "403 Forbidden";
    public static final String HTTP_INTERNALERROR = "500 Internal Server Error";
    public static final String HTTP_NOTFOUND = "404 Not Found";
    public static final String HTTP_NOTIMPLEMENTED = "501 Not Implemented";
    public static final String HTTP_NOTMODIFIED = "304 Not Modified";
    public static final String HTTP_OK = "200 OK";
    public static final String HTTP_PARTIALCONTENT = "206 Partial Content";
    public static final String HTTP_RANGE_NOT_SATISFIABLE = "416 Requested Range Not Satisfiable";
    public static final String HTTP_REDIRECT = "301 Moved Permanently";
    private static final String LICENCE = "Copyright (C) 2001,2005-2011 by Jarno Elonen <elonen@iki.fi>\nand Copyright (C) 2010 by Konstantinos Togias <info@ktogias.gr>\n\nRedistribution and use in source and binary forms, with or without\nmodification, are permitted provided that the following conditions\nare met:\n\nRedistributions of source code must retain the above copyright notice,\nthis list of conditions and the following disclaimer. Redistributions in\nbinary form must reproduce the above copyright notice, this list of\nconditions and the following disclaimer in the documentation and/or other\nmaterials provided with the distribution. The name of the author may not\nbe used to endorse or promote products derived from this software without\nspecific prior written permission. \n \nTHIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\nIMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\nOF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\nIN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\nINCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\nNOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\nDATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\nTHEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\nOF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
    private static final String LOG_TAG = "AppInvHTTPD";
    public static final String MIME_DEFAULT_BINARY = "application/octet-stream";
    public static final String MIME_HTML = "text/html";
    public static final String MIME_PLAINTEXT = "text/plain";
    public static final String MIME_XML = "text/xml";
    private static final int REPL_STACK_SIZE = 262144;
    /* access modifiers changed from: private */
    public static SimpleDateFormat gmtFrmt = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    protected static PrintStream myErr = System.err;
    protected static PrintStream myOut = System.out;
    /* access modifiers changed from: private */
    public static int theBufferSize = 16384;
    private static Hashtable theMimeTypes = new Hashtable();
    /* access modifiers changed from: private */
    public ThreadPoolExecutor myExecutor = new ThreadPoolExecutor(2, 10, 5, TimeUnit.SECONDS, new SynchronousQueue(), new myThreadFactory());
    private File myRootDir;
    /* access modifiers changed from: private */
    public final ServerSocket myServerSocket;
    private int myTcpPort;
    private Thread myThread;

    public Response serve(String uri, String method, Properties header, Properties parms, Properties files, Socket mySocket) {
        myOut.println(method + " '" + uri + "' ");
        Enumeration e = header.propertyNames();
        while (e.hasMoreElements()) {
            String value = (String) e.nextElement();
            myOut.println("  HDR: '" + value + "' = '" + header.getProperty(value) + "'");
        }
        Enumeration e2 = parms.propertyNames();
        while (e2.hasMoreElements()) {
            String value2 = (String) e2.nextElement();
            myOut.println("  PRM: '" + value2 + "' = '" + parms.getProperty(value2) + "'");
        }
        Enumeration e3 = files.propertyNames();
        while (e3.hasMoreElements()) {
            String value3 = (String) e3.nextElement();
            myOut.println("  UPLOADED: '" + value3 + "' = '" + files.getProperty(value3) + "'");
        }
        return serveFile(uri, header, this.myRootDir, true);
    }

    public class Response {
        public InputStream data;
        public Properties header;
        public String mimeType;
        public String status;

        public Response() {
            this.header = new Properties();
            this.status = NanoHTTPD.HTTP_OK;
        }

        public Response(String status2, String mimeType2, InputStream data2) {
            this.header = new Properties();
            this.status = status2;
            this.mimeType = mimeType2;
            this.data = data2;
        }

        public Response(String status2, String mimeType2, String txt) {
            this.header = new Properties();
            this.status = status2;
            this.mimeType = mimeType2;
            try {
                this.data = new ByteArrayInputStream(txt.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException uee) {
                uee.printStackTrace();
            }
        }

        public void addHeader(String name, String value) {
            this.header.put(name, value);
        }
    }

    public NanoHTTPD(int port, File wwwroot) throws IOException {
        this.myTcpPort = port;
        this.myRootDir = wwwroot;
        this.myServerSocket = new ServerSocket(this.myTcpPort);
        this.myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        new HTTPSession(NanoHTTPD.this.myServerSocket.accept());
                    } catch (IOException e) {
                        return;
                    }
                }
            }
        });
        this.myThread.setDaemon(true);
        this.myThread.start();
    }

    public void stop() {
        try {
            this.myServerSocket.close();
            this.myThread.join();
        } catch (IOException | InterruptedException e) {
        }
    }

    public static void main(String[] args) {
        myOut.println("NanoHTTPD 1.25 (C) 2001,2005-2011 Jarno Elonen and (C) 2010 Konstantinos Togias\n(Command line options: [-p port] [-d root-dir] [--licence])\n");
        int port = 80;
        File wwwroot = new File(".").getAbsoluteFile();
        int i = 0;
        while (true) {
            if (i < args.length) {
                if (args[i].equalsIgnoreCase("-p")) {
                    port = Integer.parseInt(args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-d")) {
                    wwwroot = new File(args[i + 1]).getAbsoluteFile();
                } else if (args[i].toLowerCase().endsWith("licence")) {
                    myOut.println("Copyright (C) 2001,2005-2011 by Jarno Elonen <elonen@iki.fi>\nand Copyright (C) 2010 by Konstantinos Togias <info@ktogias.gr>\n\nRedistribution and use in source and binary forms, with or without\nmodification, are permitted provided that the following conditions\nare met:\n\nRedistributions of source code must retain the above copyright notice,\nthis list of conditions and the following disclaimer. Redistributions in\nbinary form must reproduce the above copyright notice, this list of\nconditions and the following disclaimer in the documentation and/or other\nmaterials provided with the distribution. The name of the author may not\nbe used to endorse or promote products derived from this software without\nspecific prior written permission. \n \nTHIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR\nIMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES\nOF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.\nIN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,\nINCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT\nNOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,\nDATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY\nTHEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE\nOF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n");
                    break;
                }
                i++;
            }
        }
        try {
            new NanoHTTPD(port, wwwroot);
        } catch (IOException ioe) {
            myErr.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }
        myOut.println("Now serving files in port " + port + " from \"" + wwwroot + "\"");
        myOut.println("Hit Enter to stop.\n");
        try {
            System.in.read();
        } catch (Throwable th) {
        }
    }

    private class myThreadFactory implements ThreadFactory {
        private myThreadFactory() {
        }

        public Thread newThread(Runnable r) {
            Thread retval = new Thread(new ThreadGroup("biggerstack"), r, "HTTPD Session", 262144);
            retval.setDaemon(true);
            return retval;
        }
    }

    private class HTTPSession implements Runnable {
        private Socket mySocket;

        public HTTPSession(Socket s) {
            this.mySocket = s;
            Log.d(NanoHTTPD.LOG_TAG, "NanoHTTPD: getPoolSize() = " + NanoHTTPD.this.myExecutor.getPoolSize());
            NanoHTTPD.this.myExecutor.execute(this);
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r24v0, resolved type: java.io.ByteArrayOutputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r24v1, resolved type: java.io.FileOutputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r24v3, resolved type: java.io.FileOutputStream} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v44, resolved type: java.io.FileOutputStream} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r41 = this;
                r0 = r41
                java.net.Socket r4 = r0.mySocket     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.io.InputStream r28 = r4.getInputStream()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r28 != 0) goto L_0x000b
            L_0x000a:
                return
            L_0x000b:
                r20 = 8192(0x2000, float:1.14794E-41)
                r0 = r20
                byte[] r0 = new byte[r0]     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r19 = r0
                r4 = 0
                r0 = r28
                r1 = r19
                r2 = r20
                int r34 = r0.read(r1, r4, r2)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r34 <= 0) goto L_0x000a
                java.io.ByteArrayInputStream r25 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r4 = 0
                r0 = r25
                r1 = r19
                r2 = r34
                r0.<init>(r1, r4, r2)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.io.BufferedReader r26 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r25
                r4.<init>(r0)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r26
                r0.<init>(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.util.Properties r31 = new java.util.Properties     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r31.<init>()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.util.Properties r8 = new java.util.Properties     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r8.<init>()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.util.Properties r13 = new java.util.Properties     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r13.<init>()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.util.Properties r9 = new java.util.Properties     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r9.<init>()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r41
                r1 = r26
                r2 = r31
                r0.decodeHeader(r1, r2, r8, r13)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r4 = "method"
                r0 = r31
                java.lang.String r12 = r0.getProperty(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r4 = "uri"
                r0 = r31
                java.lang.String r11 = r0.getProperty(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r36 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                java.lang.String r4 = "content-length"
                java.lang.String r21 = r13.getProperty(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r21 == 0) goto L_0x007b
                int r4 = java.lang.Integer.parseInt(r21)     // Catch:{ NumberFormatException -> 0x0264 }
                long r0 = (long) r4
                r36 = r0
            L_0x007b:
                r38 = 0
                r35 = 0
            L_0x007f:
                r0 = r38
                r1 = r34
                if (r0 >= r1) goto L_0x00a5
                byte r4 = r19[r38]     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r10 = 13
                if (r4 != r10) goto L_0x0139
                int r38 = r38 + 1
                byte r4 = r19[r38]     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r10 = 10
                if (r4 != r10) goto L_0x0139
                int r38 = r38 + 1
                byte r4 = r19[r38]     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r10 = 13
                if (r4 != r10) goto L_0x0139
                int r38 = r38 + 1
                byte r4 = r19[r38]     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r10 = 10
                if (r4 != r10) goto L_0x0139
                r35 = 1
            L_0x00a5:
                int r38 = r38 + 1
                java.lang.String r4 = "PUT"
                boolean r4 = r12.equalsIgnoreCase(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r4 == 0) goto L_0x013d
                java.lang.String r4 = "upload"
                java.lang.String r10 = "bin"
                java.io.File r40 = java.io.File.createTempFile(r4, r10)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r40.deleteOnExit()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.io.FileOutputStream r24 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r24
                r1 = r40
                r0.<init>(r1)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r4 = "content"
                java.lang.String r10 = r40.getAbsolutePath()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r9.put(r4, r10)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x00cc:
                r0 = r38
                r1 = r34
                if (r0 >= r1) goto L_0x00dd
                int r4 = r34 - r38
                r0 = r24
                r1 = r19
                r2 = r38
                r0.write(r1, r2, r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x00dd:
                r0 = r38
                r1 = r34
                if (r0 >= r1) goto L_0x0143
                int r4 = r34 - r38
                int r4 = r4 + 1
                long r14 = (long) r4     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                long r36 = r36 - r14
            L_0x00ea:
                r4 = 512(0x200, float:7.175E-43)
                byte[] r0 = new byte[r4]     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r19 = r0
            L_0x00f0:
                if (r34 < 0) goto L_0x0151
                r14 = 0
                int r4 = (r36 > r14 ? 1 : (r36 == r14 ? 0 : -1))
                if (r4 <= 0) goto L_0x0151
                r4 = 0
                r10 = 512(0x200, float:7.175E-43)
                r0 = r28
                r1 = r19
                int r34 = r0.read(r1, r4, r10)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r34
                long r14 = (long) r0     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                long r36 = r36 - r14
                if (r34 <= 0) goto L_0x00f0
                r4 = 0
                r0 = r24
                r1 = r19
                r2 = r34
                r0.write(r1, r4, r2)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                goto L_0x00f0
            L_0x0115:
                r27 = move-exception
                java.lang.String r4 = "500 Internal Server Error"
                java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0136 }
                r10.<init>()     // Catch:{ Throwable -> 0x0136 }
                java.lang.String r14 = "SERVER INTERNAL ERROR: IOException: "
                java.lang.StringBuilder r10 = r10.append(r14)     // Catch:{ Throwable -> 0x0136 }
                java.lang.String r14 = r27.getMessage()     // Catch:{ Throwable -> 0x0136 }
                java.lang.StringBuilder r10 = r10.append(r14)     // Catch:{ Throwable -> 0x0136 }
                java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x0136 }
                r0 = r41
                r0.sendError(r4, r10)     // Catch:{ Throwable -> 0x0136 }
                goto L_0x000a
            L_0x0136:
                r4 = move-exception
                goto L_0x000a
            L_0x0139:
                int r38 = r38 + 1
                goto L_0x007f
            L_0x013d:
                java.io.ByteArrayOutputStream r24 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r24.<init>()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                goto L_0x00cc
            L_0x0143:
                if (r35 == 0) goto L_0x014e
                r14 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r4 = (r36 > r14 ? 1 : (r36 == r14 ? 0 : -1))
                if (r4 != 0) goto L_0x00ea
            L_0x014e:
                r36 = 0
                goto L_0x00ea
            L_0x0151:
                java.lang.String r4 = "POST"
                boolean r4 = r12.equalsIgnoreCase(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r4 == 0) goto L_0x0242
                java.io.ByteArrayOutputStream r24 = (java.io.ByteArrayOutputStream) r24     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                byte[] r6 = r24.toByteArray()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.io.ByteArrayInputStream r17 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r17
                r0.<init>(r6)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r17
                r4.<init>(r0)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r7.<init>(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r22 = ""
                java.lang.String r4 = "content-type"
                java.lang.String r23 = r13.getProperty(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.util.StringTokenizer r39 = new java.util.StringTokenizer     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r4 = "; "
                r0 = r39
                r1 = r23
                r0.<init>(r1, r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                boolean r4 = r39.hasMoreTokens()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r4 == 0) goto L_0x018f
                java.lang.String r22 = r39.nextToken()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x018f:
                java.lang.String r4 = "multipart/form-data"
                r0 = r22
                boolean r4 = r0.equalsIgnoreCase(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r4 == 0) goto L_0x01f9
                boolean r4 = r39.hasMoreTokens()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r4 != 0) goto L_0x01a8
                java.lang.String r4 = "400 Bad Request"
                java.lang.String r10 = "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html"
                r0 = r41
                r0.sendError(r4, r10)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x01a8:
                java.lang.String r18 = r39.nextToken()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.util.StringTokenizer r39 = new java.util.StringTokenizer     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r4 = "="
                r0 = r39
                r1 = r18
                r0.<init>(r1, r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                int r4 = r39.countTokens()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r10 = 2
                if (r4 == r10) goto L_0x01c7
                java.lang.String r4 = "400 Bad Request"
                java.lang.String r10 = "BAD REQUEST: Content type is multipart/form-data but boundary syntax error. Usage: GET /example/file.html"
                r0 = r41
                r0.sendError(r4, r10)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x01c7:
                r39.nextToken()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r5 = r39.nextToken()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r4 = r41
                r4.decodeMultipartData(r5, r6, r7, r8, r9)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x01d3:
                r7.close()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x01d6:
                r0 = r41
                com.google.appinventor.components.runtime.util.NanoHTTPD r10 = com.google.appinventor.components.runtime.util.NanoHTTPD.this     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r41
                java.net.Socket r0 = r0.mySocket     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r16 = r0
                r14 = r8
                r15 = r9
                com.google.appinventor.components.runtime.util.NanoHTTPD$Response r32 = r10.serve(r11, r12, r13, r14, r15, r16)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r32 != 0) goto L_0x024e
                java.lang.String r4 = "500 Internal Server Error"
                java.lang.String r10 = "SERVER INTERNAL ERROR: Serve() returned a null response."
                r0 = r41
                r0.sendError(r4, r10)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x01f1:
                r28.close()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                goto L_0x000a
            L_0x01f6:
                r4 = move-exception
                goto L_0x000a
            L_0x01f9:
                java.lang.String r30 = ""
                r4 = 512(0x200, float:7.175E-43)
                char[] r0 = new char[r4]     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r29 = r0
                r0 = r29
                int r33 = r7.read(r0)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
            L_0x0207:
                if (r33 < 0) goto L_0x0236
                java.lang.String r4 = "\r\n"
                r0 = r30
                boolean r4 = r0.endsWith(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r4 != 0) goto L_0x0236
                java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r4.<init>()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r30
                java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r10 = 0
                r0 = r29
                r1 = r33
                java.lang.String r10 = java.lang.String.valueOf(r0, r10, r1)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.StringBuilder r4 = r4.append(r10)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                java.lang.String r30 = r4.toString()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r29
                int r33 = r7.read(r0)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                goto L_0x0207
            L_0x0236:
                java.lang.String r30 = r30.trim()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r41
                r1 = r30
                r0.decodeParms(r1, r8)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                goto L_0x01d3
            L_0x0242:
                java.lang.String r4 = "PUT "
                boolean r4 = r12.equalsIgnoreCase(r4)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                if (r4 == 0) goto L_0x01d6
                r24.close()     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                goto L_0x01d6
            L_0x024e:
                r0 = r32
                java.lang.String r4 = r0.status     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r32
                java.lang.String r10 = r0.mimeType     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r32
                java.util.Properties r14 = r0.header     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r32
                java.io.InputStream r15 = r0.data     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                r0 = r41
                r0.sendResponse(r4, r10, r14, r15)     // Catch:{ IOException -> 0x0115, InterruptedException -> 0x01f6 }
                goto L_0x01f1
            L_0x0264:
                r4 = move-exception
                goto L_0x007b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.NanoHTTPD.HTTPSession.run():void");
        }

        private void decodeHeader(BufferedReader in, Properties pre, Properties parms, Properties header) throws InterruptedException {
            String uri;
            try {
                String inLine = in.readLine();
                if (inLine != null) {
                    StringTokenizer st = new StringTokenizer(inLine);
                    if (!st.hasMoreTokens()) {
                        sendError(NanoHTTPD.HTTP_BADREQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");
                    }
                    pre.put("method", st.nextToken());
                    if (!st.hasMoreTokens()) {
                        sendError(NanoHTTPD.HTTP_BADREQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");
                    }
                    String uri2 = st.nextToken();
                    int qmi = uri2.indexOf(63);
                    if (qmi >= 0) {
                        decodeParms(uri2.substring(qmi + 1), parms);
                        uri = decodePercent(uri2.substring(0, qmi));
                    } else {
                        uri = decodePercent(uri2);
                    }
                    if (st.hasMoreTokens()) {
                        String line = in.readLine();
                        while (line != null && line.trim().length() > 0) {
                            int p = line.indexOf(58);
                            if (p >= 0) {
                                header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
                            }
                            line = in.readLine();
                        }
                    }
                    pre.put("uri", uri);
                }
            } catch (IOException ioe) {
                sendError(NanoHTTPD.HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            }
        }

        private void decodeMultipartData(String boundary, byte[] fbuf, BufferedReader in, Properties parms, Properties files) throws InterruptedException {
            try {
                int[] bpositions = getBoundaryPositions(fbuf, boundary.getBytes());
                int boundarycount = 1;
                String mpline = in.readLine();
                while (mpline != null) {
                    if (mpline.indexOf(boundary) == -1) {
                        sendError(NanoHTTPD.HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but next chunk does not start with boundary. Usage: GET /example/file.html");
                    }
                    boundarycount++;
                    Properties item = new Properties();
                    mpline = in.readLine();
                    while (mpline != null && mpline.trim().length() > 0) {
                        int p = mpline.indexOf(58);
                        if (p != -1) {
                            item.put(mpline.substring(0, p).trim().toLowerCase(), mpline.substring(p + 1).trim());
                        }
                        mpline = in.readLine();
                    }
                    if (mpline != null) {
                        String contentDisposition = item.getProperty("content-disposition");
                        if (contentDisposition == null) {
                            sendError(NanoHTTPD.HTTP_BADREQUEST, "BAD REQUEST: Content type is multipart/form-data but no content-disposition info found. Usage: GET /example/file.html");
                        }
                        StringTokenizer st = new StringTokenizer(contentDisposition, "; ");
                        Properties disposition = new Properties();
                        while (st.hasMoreTokens()) {
                            String token = st.nextToken();
                            int p2 = token.indexOf(61);
                            if (p2 != -1) {
                                disposition.put(token.substring(0, p2).trim().toLowerCase(), token.substring(p2 + 1).trim());
                            }
                        }
                        String pname = disposition.getProperty("name");
                        String pname2 = pname.substring(1, pname.length() - 1);
                        String value = "";
                        if (item.getProperty("content-type") == null) {
                            while (mpline != null && mpline.indexOf(boundary) == -1) {
                                mpline = in.readLine();
                                if (mpline != null) {
                                    int d = mpline.indexOf(boundary);
                                    if (d == -1) {
                                        value = value + mpline;
                                    } else {
                                        value = value + mpline.substring(0, d - 2);
                                    }
                                }
                            }
                        } else {
                            if (boundarycount > bpositions.length) {
                                sendError(NanoHTTPD.HTTP_INTERNALERROR, "Error processing request");
                            }
                            int offset = stripMultipartHeaders(fbuf, bpositions[boundarycount - 2]);
                            files.put(pname2, saveTmpFile(fbuf, offset, (bpositions[boundarycount - 1] - offset) - 4));
                            String value2 = disposition.getProperty("filename");
                            value = value2.substring(1, value2.length() - 1);
                            do {
                                mpline = in.readLine();
                                if (mpline == null) {
                                    break;
                                }
                            } while (mpline.indexOf(boundary) != -1);
                        }
                        parms.put(pname2, value);
                    }
                }
            } catch (IOException ioe) {
                sendError(NanoHTTPD.HTTP_INTERNALERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            }
        }

        public int[] getBoundaryPositions(byte[] b, byte[] boundary) {
            int matchcount = 0;
            int matchbyte = -1;
            Vector matchbytes = new Vector();
            int i = 0;
            while (i < b.length) {
                if (b[i] == boundary[matchcount]) {
                    if (matchcount == 0) {
                        matchbyte = i;
                    }
                    matchcount++;
                    if (matchcount == boundary.length) {
                        matchbytes.addElement(new Integer(matchbyte));
                        matchcount = 0;
                        matchbyte = -1;
                    }
                } else {
                    i -= matchcount;
                    matchcount = 0;
                    matchbyte = -1;
                }
                i++;
            }
            int[] ret = new int[matchbytes.size()];
            for (int i2 = 0; i2 < ret.length; i2++) {
                ret[i2] = ((Integer) matchbytes.elementAt(i2)).intValue();
            }
            return ret;
        }

        private String saveTmpFile(byte[] b, int offset, int len) {
            if (len <= 0) {
                return "";
            }
            try {
                File temp = File.createTempFile("NanoHTTPD", "", new File(System.getProperty("java.io.tmpdir")));
                OutputStream fstream = new FileOutputStream(temp);
                fstream.write(b, offset, len);
                fstream.close();
                return temp.getAbsolutePath();
            } catch (Exception e) {
                NanoHTTPD.myErr.println("Error: " + e.getMessage());
                return "";
            }
        }

        private int stripMultipartHeaders(byte[] b, int offset) {
            int i = offset;
            while (i < b.length) {
                if (b[i] == 13) {
                    i++;
                    if (b[i] == 10) {
                        i++;
                        if (b[i] == 13) {
                            i++;
                            if (b[i] == 10) {
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                i++;
            }
            return i + 1;
        }

        private String decodePercent(String str) throws InterruptedException {
            try {
                StringBuffer sb = new StringBuffer();
                int i = 0;
                while (i < str.length()) {
                    char c = str.charAt(i);
                    switch (c) {
                        case '%':
                            sb.append((char) Integer.parseInt(str.substring(i + 1, i + 3), 16));
                            i += 2;
                            break;
                        case '+':
                            sb.append(' ');
                            break;
                        default:
                            sb.append(c);
                            break;
                    }
                    i++;
                }
                return sb.toString();
            } catch (Exception e) {
                sendError(NanoHTTPD.HTTP_BADREQUEST, "BAD REQUEST: Bad percent-encoding.");
                return null;
            }
        }

        private void decodeParms(String parms, Properties p) throws InterruptedException {
            if (parms != null) {
                StringTokenizer st = new StringTokenizer(parms, "&");
                while (st.hasMoreTokens()) {
                    String e = st.nextToken();
                    int sep = e.indexOf(61);
                    if (sep >= 0) {
                        p.put(decodePercent(e.substring(0, sep)).trim(), decodePercent(e.substring(sep + 1)));
                    }
                }
            }
        }

        private void sendError(String status, String msg) throws InterruptedException {
            sendResponse(status, NanoHTTPD.MIME_PLAINTEXT, (Properties) null, new ByteArrayInputStream(msg.getBytes()));
            throw new InterruptedException();
        }

        private void sendResponse(String status, String mime, Properties header, InputStream data) {
            int i;
            if (status == null) {
                try {
                    throw new Error("sendResponse(): Status can't be null.");
                } catch (IOException e) {
                    this.mySocket.close();
                } catch (Throwable th) {
                }
            } else {
                OutputStream out = this.mySocket.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.print("HTTP/1.0 " + status + " \r\n");
                if (mime != null) {
                    pw.print("Content-Type: " + mime + HTTP.CRLF);
                }
                if (header == null || header.getProperty("Date") == null) {
                    pw.print("Date: " + NanoHTTPD.gmtFrmt.format(new Date()) + HTTP.CRLF);
                }
                if (header != null) {
                    Enumeration e2 = header.keys();
                    while (e2.hasMoreElements()) {
                        String key = (String) e2.nextElement();
                        pw.print(key + ": " + header.getProperty(key) + HTTP.CRLF);
                    }
                }
                pw.print(HTTP.CRLF);
                pw.flush();
                if (data != null) {
                    int pending = data.available();
                    byte[] buff = new byte[NanoHTTPD.theBufferSize];
                    while (pending > 0) {
                        if (pending > NanoHTTPD.theBufferSize) {
                            i = NanoHTTPD.theBufferSize;
                        } else {
                            i = pending;
                        }
                        int read = data.read(buff, 0, i);
                        if (read <= 0) {
                            break;
                        }
                        out.write(buff, 0, read);
                        pending -= read;
                    }
                }
                out.flush();
                out.close();
                if (data != null) {
                    data.close();
                }
            }
        }
    }

    private String encodeUri(String uri) {
        String newUri = "";
        StringTokenizer st = new StringTokenizer(uri, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/")) {
                newUri = newUri + "/";
            } else if (tok.equals(" ")) {
                newUri = newUri + "%20";
            } else {
                newUri = newUri + URLEncoder.encode(tok);
            }
        }
        return newUri;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r35v41, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v31, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0634  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0157  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.appinventor.components.runtime.util.NanoHTTPD.Response serveFile(java.lang.String r41, java.util.Properties r42, java.io.File r43, boolean r44) {
        /*
            r40 = this;
            r29 = 0
            boolean r35 = r43.isDirectory()
            if (r35 != 0) goto L_0x001d
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response
            java.lang.String r35 = "500 Internal Server Error"
            java.lang.String r36 = "text/plain"
            java.lang.String r37 = "INTERNAL ERRROR: serveFile(): given homeDir is not a directory."
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r37
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
        L_0x001d:
            if (r29 != 0) goto L_0x0086
            java.lang.String r35 = r41.trim()
            char r36 = java.io.File.separatorChar
            r37 = 47
            java.lang.String r41 = r35.replace(r36, r37)
            r35 = 63
            r0 = r41
            r1 = r35
            int r35 = r0.indexOf(r1)
            if (r35 < 0) goto L_0x004d
            r35 = 0
            r36 = 63
            r0 = r41
            r1 = r36
            int r36 = r0.indexOf(r1)
            r0 = r41
            r1 = r35
            r2 = r36
            java.lang.String r41 = r0.substring(r1, r2)
        L_0x004d:
            java.lang.String r35 = ".."
            r0 = r41
            r1 = r35
            boolean r35 = r0.startsWith(r1)
            if (r35 != 0) goto L_0x0071
            java.lang.String r35 = ".."
            r0 = r41
            r1 = r35
            boolean r35 = r0.endsWith(r1)
            if (r35 != 0) goto L_0x0071
            java.lang.String r35 = "../"
            r0 = r41
            r1 = r35
            int r35 = r0.indexOf(r1)
            if (r35 < 0) goto L_0x0086
        L_0x0071:
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response
            java.lang.String r35 = "403 Forbidden"
            java.lang.String r36 = "text/plain"
            java.lang.String r37 = "FORBIDDEN: Won't serve ../ for security reasons."
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r37
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
        L_0x0086:
            java.io.File r14 = new java.io.File
            r0 = r43
            r1 = r41
            r14.<init>(r0, r1)
            if (r29 != 0) goto L_0x00ac
            boolean r35 = r14.exists()
            if (r35 != 0) goto L_0x00ac
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response
            java.lang.String r35 = "404 Not Found"
            java.lang.String r36 = "text/plain"
            java.lang.String r37 = "Error 404, file not found."
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r37
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
        L_0x00ac:
            if (r29 != 0) goto L_0x0638
            boolean r35 = r14.isDirectory()
            if (r35 == 0) goto L_0x0638
            java.lang.String r35 = "/"
            r0 = r41
            r1 = r35
            boolean r35 = r0.endsWith(r1)
            if (r35 != 0) goto L_0x0120
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r41
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "/"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r41 = r35.toString()
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response
            java.lang.String r35 = "301 Moved Permanently"
            java.lang.String r36 = "text/html"
            java.lang.StringBuilder r37 = new java.lang.StringBuilder
            r37.<init>()
            java.lang.String r38 = "<html><body>Redirected: <a href=\""
            java.lang.StringBuilder r37 = r37.append(r38)
            r0 = r37
            r1 = r41
            java.lang.StringBuilder r37 = r0.append(r1)
            java.lang.String r38 = "\">"
            java.lang.StringBuilder r37 = r37.append(r38)
            r0 = r37
            r1 = r41
            java.lang.StringBuilder r37 = r0.append(r1)
            java.lang.String r38 = "</a></body></html>"
            java.lang.StringBuilder r37 = r37.append(r38)
            java.lang.String r37 = r37.toString()
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r37
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
            java.lang.String r35 = "Location"
            r0 = r29
            r1 = r35
            r2 = r41
            r0.addHeader(r1, r2)
        L_0x0120:
            if (r29 != 0) goto L_0x0638
            java.io.File r35 = new java.io.File
            java.lang.String r36 = "index.html"
            r0 = r35
            r1 = r36
            r0.<init>(r14, r1)
            boolean r35 = r35.exists()
            if (r35 == 0) goto L_0x0264
            java.io.File r14 = new java.io.File
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r41
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "/index.html"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r35 = r35.toString()
            r0 = r43
            r1 = r35
            r14.<init>(r0, r1)
            r30 = r29
        L_0x0155:
            if (r30 != 0) goto L_0x0634
            r21 = 0
            java.lang.String r35 = r14.getCanonicalPath()     // Catch:{ IOException -> 0x062c }
            r36 = 46
            int r10 = r35.lastIndexOf(r36)     // Catch:{ IOException -> 0x062c }
            if (r10 < 0) goto L_0x017f
            java.util.Hashtable r35 = theMimeTypes     // Catch:{ IOException -> 0x062c }
            java.lang.String r36 = r14.getCanonicalPath()     // Catch:{ IOException -> 0x062c }
            int r37 = r10 + 1
            java.lang.String r36 = r36.substring(r37)     // Catch:{ IOException -> 0x062c }
            java.lang.String r36 = r36.toLowerCase()     // Catch:{ IOException -> 0x062c }
            java.lang.Object r35 = r35.get(r36)     // Catch:{ IOException -> 0x062c }
            r0 = r35
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ IOException -> 0x062c }
            r21 = r0
        L_0x017f:
            if (r21 != 0) goto L_0x0183
            java.lang.String r21 = "application/octet-stream"
        L_0x0183:
            java.lang.StringBuilder r35 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x062c }
            r35.<init>()     // Catch:{ IOException -> 0x062c }
            java.lang.String r36 = r14.getAbsolutePath()     // Catch:{ IOException -> 0x062c }
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ IOException -> 0x062c }
            long r36 = r14.lastModified()     // Catch:{ IOException -> 0x062c }
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ IOException -> 0x062c }
            java.lang.String r36 = ""
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ IOException -> 0x062c }
            long r36 = r14.length()     // Catch:{ IOException -> 0x062c }
            java.lang.StringBuilder r35 = r35.append(r36)     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = r35.toString()     // Catch:{ IOException -> 0x062c }
            int r35 = r35.hashCode()     // Catch:{ IOException -> 0x062c }
            java.lang.String r11 = java.lang.Integer.toHexString(r35)     // Catch:{ IOException -> 0x062c }
            r32 = 0
            r12 = -1
            java.lang.String r35 = "range"
            r0 = r42
            r1 = r35
            java.lang.String r28 = r0.getProperty(r1)     // Catch:{ IOException -> 0x062c }
            if (r28 == 0) goto L_0x0206
            java.lang.String r35 = "bytes="
            r0 = r28
            r1 = r35
            boolean r35 = r0.startsWith(r1)     // Catch:{ IOException -> 0x062c }
            if (r35 == 0) goto L_0x0206
            java.lang.String r35 = "bytes="
            int r35 = r35.length()     // Catch:{ IOException -> 0x062c }
            r0 = r28
            r1 = r35
            java.lang.String r28 = r0.substring(r1)     // Catch:{ IOException -> 0x062c }
            r35 = 45
            r0 = r28
            r1 = r35
            int r24 = r0.indexOf(r1)     // Catch:{ IOException -> 0x062c }
            if (r24 <= 0) goto L_0x0206
            r35 = 0
            r0 = r28
            r1 = r35
            r2 = r24
            java.lang.String r35 = r0.substring(r1, r2)     // Catch:{ NumberFormatException -> 0x0631 }
            long r32 = java.lang.Long.parseLong(r35)     // Catch:{ NumberFormatException -> 0x0631 }
            int r35 = r24 + 1
            r0 = r28
            r1 = r35
            java.lang.String r35 = r0.substring(r1)     // Catch:{ NumberFormatException -> 0x0631 }
            long r12 = java.lang.Long.parseLong(r35)     // Catch:{ NumberFormatException -> 0x0631 }
        L_0x0206:
            long r16 = r14.length()     // Catch:{ IOException -> 0x062c }
            if (r28 == 0) goto L_0x05c0
            r36 = 0
            int r35 = (r32 > r36 ? 1 : (r32 == r36 ? 0 : -1))
            if (r35 < 0) goto L_0x05c0
            int r35 = (r32 > r16 ? 1 : (r32 == r16 ? 0 : -1))
            if (r35 < 0) goto L_0x0506
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = "416 Requested Range Not Satisfiable"
            java.lang.String r36 = "text/plain"
            java.lang.String r37 = ""
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r37
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = "Content-Range"
            java.lang.StringBuilder r36 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x05a8 }
            r36.<init>()     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r37 = "bytes 0-0/"
            java.lang.StringBuilder r36 = r36.append(r37)     // Catch:{ IOException -> 0x05a8 }
            r0 = r36
            r1 = r16
            java.lang.StringBuilder r36 = r0.append(r1)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r36 = r36.toString()     // Catch:{ IOException -> 0x05a8 }
            r0 = r29
            r1 = r35
            r2 = r36
            r0.addHeader(r1, r2)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r35 = "ETag"
            r0 = r29
            r1 = r35
            r0.addHeader(r1, r11)     // Catch:{ IOException -> 0x05a8 }
        L_0x0256:
            java.lang.String r35 = "Accept-Ranges"
            java.lang.String r36 = "bytes"
            r0 = r29
            r1 = r35
            r2 = r36
            r0.addHeader(r1, r2)
            return r29
        L_0x0264:
            java.io.File r35 = new java.io.File
            java.lang.String r36 = "index.htm"
            r0 = r35
            r1 = r36
            r0.<init>(r14, r1)
            boolean r35 = r35.exists()
            if (r35 == 0) goto L_0x0299
            java.io.File r14 = new java.io.File
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r41
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "/index.htm"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r35 = r35.toString()
            r0 = r43
            r1 = r35
            r14.<init>(r0, r1)
            r30 = r29
            goto L_0x0155
        L_0x0299:
            if (r44 == 0) goto L_0x04ed
            boolean r35 = r14.canRead()
            if (r35 == 0) goto L_0x04ed
            java.lang.String[] r15 = r14.list()
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            java.lang.String r36 = "<html><body><h1>Directory "
            java.lang.StringBuilder r35 = r35.append(r36)
            r0 = r35
            r1 = r41
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "</h1><br/>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            int r35 = r41.length()
            r36 = 1
            r0 = r35
            r1 = r36
            if (r0 <= r1) goto L_0x0321
            r35 = 0
            int r36 = r41.length()
            int r36 = r36 + -1
            r0 = r41
            r1 = r35
            r2 = r36
            java.lang.String r34 = r0.substring(r1, r2)
            r35 = 47
            int r31 = r34.lastIndexOf(r35)
            if (r31 < 0) goto L_0x0321
            int r35 = r34.length()
            r0 = r31
            r1 = r35
            if (r0 >= r1) goto L_0x0321
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "<b><a href=\""
            java.lang.StringBuilder r35 = r35.append(r36)
            r36 = 0
            int r37 = r31 + 1
            r0 = r41
            r1 = r36
            r2 = r37
            java.lang.String r36 = r0.substring(r1, r2)
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = "\">..</a></b><br/>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
        L_0x0321:
            if (r15 == 0) goto L_0x04bf
            r19 = 0
        L_0x0325:
            int r0 = r15.length
            r35 = r0
            r0 = r19
            r1 = r35
            if (r0 >= r1) goto L_0x04bf
            java.io.File r6 = new java.io.File
            r35 = r15[r19]
            r0 = r35
            r6.<init>(r14, r0)
            boolean r7 = r6.isDirectory()
            if (r7 == 0) goto L_0x036b
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "<b>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r36 = r15[r19]
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = "/"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r35 = r35.toString()
            r15[r19] = r35
        L_0x036b:
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "<a href=\""
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.StringBuilder r36 = new java.lang.StringBuilder
            r36.<init>()
            r0 = r36
            r1 = r41
            java.lang.StringBuilder r36 = r0.append(r1)
            r37 = r15[r19]
            java.lang.StringBuilder r36 = r36.append(r37)
            java.lang.String r36 = r36.toString()
            r0 = r40
            r1 = r36
            java.lang.String r36 = r0.encodeUri(r1)
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = "\">"
            java.lang.StringBuilder r35 = r35.append(r36)
            r36 = r15[r19]
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = "</a>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            boolean r35 = r6.isFile()
            if (r35 == 0) goto L_0x0414
            long r22 = r6.length()
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = " &nbsp;<font size=2>("
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            r36 = 1024(0x400, double:5.06E-321)
            int r35 = (r22 > r36 ? 1 : (r22 == r36 ? 0 : -1))
            if (r35 >= 0) goto L_0x0448
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            r0 = r35
            r1 = r22
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = " bytes"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
        L_0x03fd:
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = ")</font>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
        L_0x0414:
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "<br/>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            if (r7 == 0) goto L_0x0444
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "</b>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
        L_0x0444:
            int r19 = r19 + 1
            goto L_0x0325
        L_0x0448:
            r36 = 1048576(0x100000, double:5.180654E-318)
            int r35 = (r22 > r36 ? 1 : (r22 == r36 ? 0 : -1))
            if (r35 >= 0) goto L_0x0486
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            r36 = 1024(0x400, double:5.06E-321)
            long r36 = r22 / r36
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = "."
            java.lang.StringBuilder r35 = r35.append(r36)
            r36 = 1024(0x400, double:5.06E-321)
            long r36 = r22 % r36
            r38 = 10
            long r36 = r36 / r38
            r38 = 100
            long r36 = r36 % r38
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = " KB"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            goto L_0x03fd
        L_0x0486:
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            r36 = 1048576(0x100000, double:5.180654E-318)
            long r36 = r22 / r36
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = "."
            java.lang.StringBuilder r35 = r35.append(r36)
            r36 = 1048576(0x100000, double:5.180654E-318)
            long r36 = r22 % r36
            r38 = 10
            long r36 = r36 / r38
            r38 = 100
            long r36 = r36 % r38
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r36 = " MB"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            goto L_0x03fd
        L_0x04bf:
            java.lang.StringBuilder r35 = new java.lang.StringBuilder
            r35.<init>()
            r0 = r35
            r1 = r25
            java.lang.StringBuilder r35 = r0.append(r1)
            java.lang.String r36 = "</body></html>"
            java.lang.StringBuilder r35 = r35.append(r36)
            java.lang.String r25 = r35.toString()
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response
            java.lang.String r35 = "200 OK"
            java.lang.String r36 = "text/html"
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r25
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
            r30 = r29
            goto L_0x0155
        L_0x04ed:
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response
            java.lang.String r35 = "403 Forbidden"
            java.lang.String r36 = "text/plain"
            java.lang.String r37 = "FORBIDDEN: No directory listing."
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r37
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
            r30 = r29
            goto L_0x0155
        L_0x0506:
            r36 = 0
            int r35 = (r12 > r36 ? 1 : (r12 == r36 ? 0 : -1))
            if (r35 >= 0) goto L_0x0510
            r36 = 1
            long r12 = r16 - r36
        L_0x0510:
            long r36 = r12 - r32
            r38 = 1
            long r26 = r36 + r38
            r36 = 0
            int r35 = (r26 > r36 ? 1 : (r26 == r36 ? 0 : -1))
            if (r35 >= 0) goto L_0x051e
            r26 = 0
        L_0x051e:
            r8 = r26
            com.google.appinventor.components.runtime.util.NanoHTTPD$2 r18 = new com.google.appinventor.components.runtime.util.NanoHTTPD$2     // Catch:{ IOException -> 0x062c }
            r0 = r18
            r1 = r40
            r0.<init>(r14, r8)     // Catch:{ IOException -> 0x062c }
            r0 = r18
            r1 = r32
            r0.skip(r1)     // Catch:{ IOException -> 0x062c }
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = "206 Partial Content"
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r21
            r4 = r18
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.io.InputStream) r4)     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = "Content-Length"
            java.lang.StringBuilder r36 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x05a8 }
            r36.<init>()     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r37 = ""
            java.lang.StringBuilder r36 = r36.append(r37)     // Catch:{ IOException -> 0x05a8 }
            r0 = r36
            java.lang.StringBuilder r36 = r0.append(r8)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r36 = r36.toString()     // Catch:{ IOException -> 0x05a8 }
            r0 = r29
            r1 = r35
            r2 = r36
            r0.addHeader(r1, r2)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r35 = "Content-Range"
            java.lang.StringBuilder r36 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x05a8 }
            r36.<init>()     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r37 = "bytes "
            java.lang.StringBuilder r36 = r36.append(r37)     // Catch:{ IOException -> 0x05a8 }
            r0 = r36
            r1 = r32
            java.lang.StringBuilder r36 = r0.append(r1)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r37 = "-"
            java.lang.StringBuilder r36 = r36.append(r37)     // Catch:{ IOException -> 0x05a8 }
            r0 = r36
            java.lang.StringBuilder r36 = r0.append(r12)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r37 = "/"
            java.lang.StringBuilder r36 = r36.append(r37)     // Catch:{ IOException -> 0x05a8 }
            r0 = r36
            r1 = r16
            java.lang.StringBuilder r36 = r0.append(r1)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r36 = r36.toString()     // Catch:{ IOException -> 0x05a8 }
            r0 = r29
            r1 = r35
            r2 = r36
            r0.addHeader(r1, r2)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r35 = "ETag"
            r0 = r29
            r1 = r35
            r0.addHeader(r1, r11)     // Catch:{ IOException -> 0x05a8 }
            goto L_0x0256
        L_0x05a8:
            r20 = move-exception
        L_0x05a9:
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response
            java.lang.String r35 = "403 Forbidden"
            java.lang.String r36 = "text/plain"
            java.lang.String r37 = "FORBIDDEN: Reading file failed."
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r36
            r4 = r37
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
            goto L_0x0256
        L_0x05c0:
            java.lang.String r35 = "if-none-match"
            r0 = r42
            r1 = r35
            java.lang.String r35 = r0.getProperty(r1)     // Catch:{ IOException -> 0x062c }
            r0 = r35
            boolean r35 = r11.equals(r0)     // Catch:{ IOException -> 0x062c }
            if (r35 == 0) goto L_0x05e7
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = "304 Not Modified"
            java.lang.String r36 = ""
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r21
            r4 = r36
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)     // Catch:{ IOException -> 0x062c }
            goto L_0x0256
        L_0x05e7:
            com.google.appinventor.components.runtime.util.NanoHTTPD$Response r29 = new com.google.appinventor.components.runtime.util.NanoHTTPD$Response     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = "200 OK"
            java.io.FileInputStream r36 = new java.io.FileInputStream     // Catch:{ IOException -> 0x062c }
            r0 = r36
            r0.<init>(r14)     // Catch:{ IOException -> 0x062c }
            r0 = r29
            r1 = r40
            r2 = r35
            r3 = r21
            r4 = r36
            r0.<init>((java.lang.String) r2, (java.lang.String) r3, (java.io.InputStream) r4)     // Catch:{ IOException -> 0x062c }
            java.lang.String r35 = "Content-Length"
            java.lang.StringBuilder r36 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x05a8 }
            r36.<init>()     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r37 = ""
            java.lang.StringBuilder r36 = r36.append(r37)     // Catch:{ IOException -> 0x05a8 }
            r0 = r36
            r1 = r16
            java.lang.StringBuilder r36 = r0.append(r1)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r36 = r36.toString()     // Catch:{ IOException -> 0x05a8 }
            r0 = r29
            r1 = r35
            r2 = r36
            r0.addHeader(r1, r2)     // Catch:{ IOException -> 0x05a8 }
            java.lang.String r35 = "ETag"
            r0 = r29
            r1 = r35
            r0.addHeader(r1, r11)     // Catch:{ IOException -> 0x05a8 }
            goto L_0x0256
        L_0x062c:
            r20 = move-exception
            r29 = r30
            goto L_0x05a9
        L_0x0631:
            r35 = move-exception
            goto L_0x0206
        L_0x0634:
            r29 = r30
            goto L_0x0256
        L_0x0638:
            r30 = r29
            goto L_0x0155
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.util.NanoHTTPD.serveFile(java.lang.String, java.util.Properties, java.io.File, boolean):com.google.appinventor.components.runtime.util.NanoHTTPD$Response");
    }

    static {
        StringTokenizer st = new StringTokenizer("css            text/css htm            text/html html           text/html xml            text/xml txt            text/plain asc            text/plain gif            image/gif jpg            image/jpeg jpeg           image/jpeg png            image/png mp3            audio/mpeg m3u            audio/mpeg-url mp4            video/mp4 ogv            video/ogg flv            video/x-flv mov            video/quicktime swf            application/x-shockwave-flash js                     application/javascript pdf            application/pdf doc            application/msword ogg            application/x-ogg zip            application/octet-stream exe            application/octet-stream class          application/octet-stream ");
        while (st.hasMoreTokens()) {
            theMimeTypes.put(st.nextToken(), st.nextToken());
        }
        gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
}
