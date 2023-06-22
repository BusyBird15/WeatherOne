package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.VisibleForTesting;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.HtmlEntities;
import com.google.appinventor.components.runtime.collect.Lists;
import com.google.appinventor.components.runtime.collect.Maps;
import com.google.appinventor.components.runtime.errors.DispatchableError;
import com.google.appinventor.components.runtime.errors.IllegalArgumentError;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.errors.RequestTimeoutException;
import com.google.appinventor.components.runtime.repackaged.org.json.XML;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.BulkPermissionRequest;
import com.google.appinventor.components.runtime.util.ChartDataSourceUtil;
import com.google.appinventor.components.runtime.util.CsvUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FileUtil;
import com.google.appinventor.components.runtime.util.GingerbreadUtil;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.MediaUtil;
import com.google.appinventor.components.runtime.util.NanoHTTPD;
import com.google.appinventor.components.runtime.util.SdkLevel;
import com.google.appinventor.components.runtime.util.XmlParser;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.appinventor.components.runtime.util.YailList;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.json.JSONException;
import org.xml.sax.InputSource;

@DesignerComponent(category = ComponentCategory.CONNECTIVITY, description = "Non-visible component that provides functions for HTTP GET, POST, PUT, and DELETE requests.", iconName = "images/web.png", nonVisible = true, version = 8)
@UsesLibraries(libraries = "json.jar")
@SimpleObject
@UsesPermissions({"android.permission.INTERNET"})
public class Web extends AndroidNonvisibleComponent implements Component, ObservableDataSource<YailList, Future<YailList>> {
    private static final String LOG_TAG = "Web";
    private static final Map<String, String> mimeTypeToExtension = Maps.newHashMap();
    private final Activity activity;
    /* access modifiers changed from: private */
    public boolean allowCookies;
    private YailList columns;
    /* access modifiers changed from: private */
    public final CookieHandler cookieHandler;
    private HashSet<DataSourceChangeListener> dataSourceObservers;
    /* access modifiers changed from: private */
    public boolean haveReadPermission;
    /* access modifiers changed from: private */
    public boolean haveWritePermission;
    private FutureTask<Void> lastTask;
    /* access modifiers changed from: private */
    public YailList requestHeaders;
    /* access modifiers changed from: private */
    public String responseFileName;
    /* access modifiers changed from: private */
    public boolean saveResponse;
    /* access modifiers changed from: private */
    public int timeout;
    /* access modifiers changed from: private */
    public String urlString;

    private static class InvalidRequestHeadersException extends Exception {
        final int errorNumber;
        final int index;

        InvalidRequestHeadersException(int errorNumber2, int index2) {
            this.errorNumber = errorNumber2;
            this.index = index2;
        }
    }

    static class BuildRequestDataException extends Exception {
        final int errorNumber;
        final int index;

        BuildRequestDataException(int errorNumber2, int index2) {
            this.errorNumber = errorNumber2;
            this.index = index2;
        }
    }

    private static class CapturedProperties {
        final boolean allowCookies;
        final Map<String, List<String>> cookies;
        final Map<String, List<String>> requestHeaders;
        final String responseFileName;
        final boolean saveResponse;
        final int timeout;
        final URL url = new URL(this.urlString);
        final String urlString;

        CapturedProperties(Web web) throws MalformedURLException, InvalidRequestHeadersException {
            this.urlString = web.urlString;
            this.allowCookies = web.allowCookies;
            this.saveResponse = web.saveResponse;
            this.responseFileName = web.responseFileName;
            this.timeout = web.timeout;
            this.requestHeaders = Web.processRequestHeaders(web.requestHeaders);
            Map<String, List<String>> cookiesTemp = null;
            if (this.allowCookies && web.cookieHandler != null) {
                try {
                    cookiesTemp = web.cookieHandler.get(this.url.toURI(), this.requestHeaders);
                } catch (IOException | URISyntaxException e) {
                }
            }
            this.cookies = cookiesTemp;
        }
    }

    static {
        mimeTypeToExtension.put("application/pdf", "pdf");
        mimeTypeToExtension.put("application/zip", "zip");
        mimeTypeToExtension.put("audio/mpeg", "mpeg");
        mimeTypeToExtension.put("audio/mp3", "mp3");
        mimeTypeToExtension.put("audio/mp4", "mp4");
        mimeTypeToExtension.put("image/gif", "gif");
        mimeTypeToExtension.put("image/jpeg", "jpg");
        mimeTypeToExtension.put("image/png", "png");
        mimeTypeToExtension.put("image/tiff", "tiff");
        mimeTypeToExtension.put(NanoHTTPD.MIME_PLAINTEXT, "txt");
        mimeTypeToExtension.put(NanoHTTPD.MIME_HTML, "html");
        mimeTypeToExtension.put(NanoHTTPD.MIME_XML, "xml");
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public Web(ComponentContainer container) {
        super(container.$form());
        CookieHandler cookieHandler2 = null;
        this.urlString = "";
        this.requestHeaders = new YailList();
        this.responseFileName = "";
        this.timeout = 0;
        this.haveReadPermission = false;
        this.haveWritePermission = false;
        this.lastTask = null;
        this.columns = new YailList();
        this.dataSourceObservers = new HashSet<>();
        this.activity = container.$context();
        this.cookieHandler = SdkLevel.getLevel() >= 9 ? GingerbreadUtil.newCookieManager() : cookieHandler2;
    }

    protected Web() {
        super((Form) null);
        this.urlString = "";
        this.requestHeaders = new YailList();
        this.responseFileName = "";
        this.timeout = 0;
        this.haveReadPermission = false;
        this.haveWritePermission = false;
        this.lastTask = null;
        this.columns = new YailList();
        this.dataSourceObservers = new HashSet<>();
        this.activity = null;
        this.cookieHandler = null;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The URL for the web request.")
    public String Url() {
        return this.urlString;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void Url(String url) {
        this.urlString = url;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The request headers, as a list of two-element sublists. The first element of each sublist represents the request header field name. The second element of each sublist represents the request header field values, either a single value or a list containing multiple values.")
    public YailList RequestHeaders() {
        return this.requestHeaders;
    }

    @SimpleProperty
    public void RequestHeaders(YailList list) {
        try {
            processRequestHeaders(list);
            this.requestHeaders = list;
        } catch (InvalidRequestHeadersException e) {
            this.form.dispatchErrorOccurredEvent(this, "RequestHeaders", e.errorNumber, Integer.valueOf(e.index));
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the cookies from a response should be saved and used in subsequent requests. Cookies are only supported on Android version 2.3 or greater.")
    public boolean AllowCookies() {
        return this.allowCookies;
    }

    @DesignerProperty(defaultValue = "false", editorType = "boolean")
    @SimpleProperty
    public void AllowCookies(boolean allowCookies2) {
        this.allowCookies = allowCookies2;
        if (allowCookies2 && this.cookieHandler == null) {
            this.form.dispatchErrorOccurredEvent(this, "AllowCookies", 4, new Object[0]);
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Whether the response should be saved in a file.")
    public boolean SaveResponse() {
        return this.saveResponse;
    }

    @DesignerProperty(defaultValue = "false", editorType = "boolean")
    @SimpleProperty
    public void SaveResponse(boolean saveResponse2) {
        this.saveResponse = saveResponse2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The name of the file where the response should be saved. If SaveResponse is true and ResponseFileName is empty, then a new file name will be generated.")
    public String ResponseFileName() {
        return this.responseFileName;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ResponseFileName(String responseFileName2) {
        this.responseFileName = responseFileName2;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "The number of milliseconds that a web request will wait for a response before giving up. If set to 0, then there is no time limit on how long the request will wait.")
    public int Timeout() {
        return this.timeout;
    }

    @DesignerProperty(defaultValue = "0", editorType = "non_negative_integer")
    @SimpleProperty
    public void Timeout(int timeout2) {
        if (timeout2 < 0) {
            throw new IllegalArgumentError("Web Timeout must be a non-negative integer.");
        }
        this.timeout = timeout2;
    }

    @SimpleFunction(description = "Clears all cookies for this Web component.")
    public void ClearCookies() {
        if (this.cookieHandler != null) {
            GingerbreadUtil.clearCookies(this.cookieHandler);
        } else {
            this.form.dispatchErrorOccurredEvent(this, "ClearCookies", 4, new Object[0]);
        }
    }

    @SimpleFunction
    public void Get() {
        final CapturedProperties webProps = capturePropertyValues("Get");
        if (webProps != null) {
            this.lastTask = new FutureTask<>(new Runnable() {
                public void run() {
                    Web.this.performRequest(webProps, (byte[]) null, (String) null, "GET", "Get");
                }
            }, (Object) null);
            AsynchUtil.runAsynchronously(this.lastTask);
        }
    }

    @SimpleFunction(description = "Performs an HTTP POST request using the Url property and the specified text.\nThe characters of the text are encoded using UTF-8 encoding.\nIf the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The responseFileName property can be used to specify the name of the file.\nIf the SaveResponse property is false, the GotText event will be triggered.")
    public void PostText(String text) {
        requestTextImpl(text, "UTF-8", "PostText", "POST");
    }

    @SimpleFunction(description = "Performs an HTTP POST request using the Url property and the specified text.\nThe characters of the text are encoded using the given encoding.\nIf the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The ResponseFileName property can be used to specify the name of the file.\nIf the SaveResponse property is false, the GotText event will be triggered.")
    public void PostTextWithEncoding(String text, String encoding) {
        requestTextImpl(text, encoding, "PostTextWithEncoding", "POST");
    }

    @SimpleFunction(description = "Performs an HTTP POST request using the Url property and data from the specified file.\nIf the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The ResponseFileName property can be used to specify the name of the file.\nIf the SaveResponse property is false, the GotText event will be triggered.")
    public void PostFile(final String path) {
        final CapturedProperties webProps = capturePropertyValues("PostFile");
        if (webProps != null) {
            this.lastTask = new FutureTask<>(new Runnable() {
                public void run() {
                    Web.this.performRequest(webProps, (byte[]) null, path, "POST", "PostFile");
                }
            }, (Object) null);
            AsynchUtil.runAsynchronously(this.lastTask);
        }
    }

    @SimpleFunction(description = "Performs an HTTP PATCH request using the Url property and the specified text.<br>The characters of the text are encoded using UTF-8 encoding.<br>If the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The responseFileName property can be used to specify the name of the file.<br>If the SaveResponse property is false, the GotText event will be triggered.")
    public void PatchText(String text) {
        requestTextImpl(text, "UTF-8", "PatchText", "PATCH");
    }

    @SimpleFunction(description = "Performs an HTTP PATCH request using the Url property and the specified text.<br>The characters of the text are encoded using the given encoding.<br>If the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The ResponseFileName property can be used to specify the name of the file.<br>If the SaveResponse property is false, the GotText event will be triggered.")
    public void PatchTextWithEncoding(String text, String encoding) {
        requestTextImpl(text, encoding, "PatchTextWithEncoding", "PATCH");
    }

    @SimpleFunction(description = "Performs an HTTP PATCH request using the Url property and data from the specified file.<br>If the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The ResponseFileName property can be used to specify the name of the file.<br>If the SaveResponse property is false, the GotText event will be triggered.")
    public void PatchFile(final String path) {
        final CapturedProperties webProps = capturePropertyValues("PatchFile");
        if (webProps != null) {
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    Web.this.performRequest(webProps, (byte[]) null, path, "PATCH", "PatchFile");
                }
            });
        }
    }

    @SimpleFunction(description = "Performs an HTTP PUT request using the Url property and the specified text.<br>The characters of the text are encoded using UTF-8 encoding.<br>If the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The responseFileName property can be used to specify the name of the file.<br>If the SaveResponse property is false, the GotText event will be triggered.")
    public void PutText(String text) {
        requestTextImpl(text, "UTF-8", "PutText", "PUT");
    }

    @SimpleFunction(description = "Performs an HTTP PUT request using the Url property and the specified text.<br>The characters of the text are encoded using the given encoding.<br>If the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The ResponseFileName property can be used to specify the name of the file.<br>If the SaveResponse property is false, the GotText event will be triggered.")
    public void PutTextWithEncoding(String text, String encoding) {
        requestTextImpl(text, encoding, "PutTextWithEncoding", "PUT");
    }

    @SimpleFunction(description = "Performs an HTTP PUT request using the Url property and data from the specified file.<br>If the SaveResponse property is true, the response will be saved in a file and the GotFile event will be triggered. The ResponseFileName property can be used to specify the name of the file.<br>If the SaveResponse property is false, the GotText event will be triggered.")
    public void PutFile(final String path) {
        final CapturedProperties webProps = capturePropertyValues("PutFile");
        if (webProps != null) {
            this.lastTask = new FutureTask<>(new Runnable() {
                public void run() {
                    Web.this.performRequest(webProps, (byte[]) null, path, "PUT", "PutFile");
                }
            }, (Object) null);
            AsynchUtil.runAsynchronously(this.lastTask);
        }
    }

    @SimpleFunction
    public void Delete() {
        final CapturedProperties webProps = capturePropertyValues("Delete");
        if (webProps != null) {
            this.lastTask = new FutureTask<>(new Runnable() {
                public void run() {
                    Web.this.performRequest(webProps, (byte[]) null, (String) null, "DELETE", "Delete");
                }
            }, (Object) null);
            AsynchUtil.runAsynchronously(this.lastTask);
        }
    }

    private void requestTextImpl(String text, String encoding, String functionName, String httpVerb) {
        final CapturedProperties webProps = capturePropertyValues(functionName);
        if (webProps != null) {
            final String str = encoding;
            final String str2 = text;
            final String str3 = functionName;
            final String str4 = httpVerb;
            this.lastTask = new FutureTask<>(new Runnable() {
                public void run() {
                    byte[] requestData;
                    try {
                        if (str == null || str.length() == 0) {
                            requestData = str2.getBytes("UTF-8");
                        } else {
                            requestData = str2.getBytes(str);
                        }
                        Web.this.performRequest(webProps, requestData, (String) null, str4, str3);
                    } catch (UnsupportedEncodingException e) {
                        Web.this.form.dispatchErrorOccurredEvent(Web.this, str3, ErrorMessages.ERROR_WEB_UNSUPPORTED_ENCODING, str);
                    }
                }
            }, (Object) null);
            AsynchUtil.runAsynchronously(this.lastTask);
        }
    }

    @SimpleEvent
    public void GotText(String url, int responseCode, String responseType, String responseContent) {
        EventDispatcher.dispatchEvent(this, "GotText", url, Integer.valueOf(responseCode), responseType, responseContent);
    }

    @SimpleEvent
    public void GotFile(String url, int responseCode, String responseType, String fileName) {
        EventDispatcher.dispatchEvent(this, "GotFile", url, Integer.valueOf(responseCode), responseType, fileName);
    }

    @SimpleEvent
    public void TimedOut(String url) {
        EventDispatcher.dispatchEvent(this, "TimedOut", url);
    }

    @SimpleFunction
    public String BuildRequestData(YailList list) {
        try {
            return buildRequestData(list);
        } catch (BuildRequestDataException e) {
            this.form.dispatchErrorOccurredEvent(this, "BuildRequestData", e.errorNumber, Integer.valueOf(e.index));
            return "";
        }
    }

    /* access modifiers changed from: package-private */
    public String buildRequestData(YailList list) throws BuildRequestDataException {
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        int i = 0;
        while (i < list.size()) {
            Object item = list.getObject(i);
            if (item instanceof YailList) {
                YailList sublist = (YailList) item;
                if (sublist.size() == 2) {
                    sb.append(delimiter).append(UriEncode(sublist.getObject(0).toString())).append('=').append(UriEncode(sublist.getObject(1).toString()));
                    delimiter = "&";
                    i++;
                } else {
                    throw new BuildRequestDataException(ErrorMessages.ERROR_WEB_BUILD_REQUEST_DATA_NOT_TWO_ELEMENTS, i + 1);
                }
            } else {
                throw new BuildRequestDataException(ErrorMessages.ERROR_WEB_BUILD_REQUEST_DATA_NOT_LIST, i + 1);
            }
        }
        return sb.toString();
    }

    @SimpleFunction
    public String UriEncode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "UTF-8 is unsupported?", e);
            return "";
        }
    }

    @SimpleFunction
    public String UriDecode(String text) {
        try {
            return URLDecoder.decode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "UTF-8 is unsupported?", e);
            return "";
        }
    }

    @SimpleFunction
    public Object JsonTextDecode(String jsonText) {
        try {
            return decodeJsonText(jsonText, false);
        } catch (IllegalArgumentException e) {
            this.form.dispatchErrorOccurredEvent(this, "JsonTextDecode", ErrorMessages.ERROR_WEB_JSON_TEXT_DECODE_FAILED, jsonText);
            return "";
        }
    }

    @SimpleFunction
    public Object JsonTextDecodeWithDictionaries(String jsonText) {
        try {
            return decodeJsonText(jsonText, true);
        } catch (IllegalArgumentException e) {
            this.form.dispatchErrorOccurredEvent(this, "JsonTextDecodeWithDictionaries", ErrorMessages.ERROR_WEB_JSON_TEXT_DECODE_FAILED, jsonText);
            return "";
        }
    }

    @VisibleForTesting
    static Object decodeJsonText(String jsonText, boolean useDicts) throws IllegalArgumentException {
        try {
            return JsonUtil.getObjectFromJson(jsonText, useDicts);
        } catch (JSONException e) {
            throw new IllegalArgumentException("jsonText is not a legal JSON value");
        }
    }

    @VisibleForTesting
    @Deprecated
    static Object decodeJsonText(String jsonText) throws IllegalArgumentException {
        return decodeJsonText(jsonText, false);
    }

    @SimpleFunction
    public String JsonObjectEncode(Object jsonObject) {
        try {
            return JsonUtil.encodeJsonObject(jsonObject);
        } catch (IllegalArgumentException e) {
            this.form.dispatchErrorOccurredEvent(this, "JsonObjectEncode", ErrorMessages.ERROR_WEB_JSON_TEXT_ENCODE_FAILED, jsonObject);
            return "";
        }
    }

    @SimpleFunction(description = "Decodes the given XML into a set of nested dictionaries that capture the structure and data contained in the XML. See the help for more details.")
    public Object XMLTextDecodeAsDictionary(String XmlText) {
        try {
            XmlParser p = new XmlParser();
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            InputSource is = new InputSource(new StringReader(XmlText));
            is.setEncoding("UTF-8");
            parser.parse(is, p);
            return p.getRoot();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            this.form.dispatchErrorOccurredEvent(this, "XMLTextDecodeAsDictionary", ErrorMessages.ERROR_WEB_JSON_TEXT_DECODE_FAILED, e.getMessage());
            return new YailDictionary();
        }
    }

    @SimpleFunction(description = "Decodes the given XML string to produce a dictionary structure. See the App Inventor documentation on \"Other topics, notes, and details\" for information.")
    public Object XMLTextDecode(String XmlText) {
        try {
            return JsonTextDecode(XML.toJSONObject(XmlText).toString());
        } catch (com.google.appinventor.components.runtime.repackaged.org.json.JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
            this.form.dispatchErrorOccurredEvent(this, "XMLTextDecode", ErrorMessages.ERROR_WEB_JSON_TEXT_DECODE_FAILED, e.getMessage());
            return YailList.makeEmptyList();
        }
    }

    @SimpleFunction(description = "Decodes the given HTML text value. HTML character entities such as `&`, `<`, `>`, `'`, and `\"` are changed to &, <, >, ', and \". Entities such as &#xhhhh, and &#nnnn are changed to the appropriate characters.")
    public String HtmlTextDecode(String htmlText) {
        try {
            return HtmlEntities.decodeHtmlText(htmlText);
        } catch (IllegalArgumentException e) {
            this.form.dispatchErrorOccurredEvent(this, "HtmlTextDecode", ErrorMessages.ERROR_WEB_HTML_TEXT_DECODE_FAILED, htmlText);
            return "";
        }
    }

    /* access modifiers changed from: private */
    public void performRequest(CapturedProperties webProps, byte[] postData, String postFile, String httpVerb, String method) {
        int message;
        String[] args;
        final List<String> neededPermissions = new ArrayList<>();
        if (postFile != null && FileUtil.needsReadPermission(this.form, postFile) && !this.haveReadPermission) {
            neededPermissions.add("android.permission.READ_EXTERNAL_STORAGE");
        }
        if (this.saveResponse) {
            if (FileUtil.needsWritePermission(this.form, FileUtil.resolveFileName(this.form, webProps.responseFileName, this.form.DefaultFileScope())) && !this.haveWritePermission) {
                neededPermissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
            }
        }
        if (neededPermissions.size() <= 0 || this.haveReadPermission) {
            try {
                HttpURLConnection connection = openConnection(webProps, httpVerb);
                if (connection != null) {
                    if (postData != null) {
                        try {
                            writeRequestData(connection, postData);
                        } catch (SocketTimeoutException e) {
                            final CapturedProperties capturedProperties = webProps;
                            this.activity.runOnUiThread(new Runnable() {
                                public void run() {
                                    Web.this.TimedOut(capturedProperties.urlString);
                                }
                            });
                            throw new RequestTimeoutException();
                        } catch (Throwable th) {
                            connection.disconnect();
                            throw th;
                        }
                    } else if (postFile != null) {
                        writeRequestFile(connection, postFile);
                    }
                    final int responseCode = connection.getResponseCode();
                    final String responseType = getResponseType(connection);
                    processResponseCookies(connection);
                    if (this.saveResponse) {
                        final String path = saveResponseContent(connection, webProps.responseFileName, responseType);
                        final CapturedProperties capturedProperties2 = webProps;
                        this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Web.this.GotFile(capturedProperties2.urlString, responseCode, responseType, path);
                            }
                        });
                    } else {
                        final String responseContent = getResponseContent(connection);
                        final CapturedProperties capturedProperties3 = webProps;
                        final int i = responseCode;
                        final String str = responseType;
                        this.activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Web.this.GotText(capturedProperties3.urlString, i, str, responseContent);
                            }
                        });
                        updateColumns(responseContent, responseType);
                        notifyDataObservers((YailList) null, (Object) null);
                    }
                    connection.disconnect();
                }
            } catch (PermissionException e2) {
                this.form.dispatchPermissionDeniedEvent((Component) this, method, e2);
            } catch (FileUtil.FileException e3) {
                this.form.dispatchErrorOccurredEvent(this, method, e3.getErrorMessageNumber(), new Object[0]);
            } catch (DispatchableError e4) {
                this.form.dispatchErrorOccurredEvent(this, method, e4.getErrorCode(), e4.getArguments());
            } catch (RequestTimeoutException e5) {
                this.form.dispatchErrorOccurredEvent(this, method, ErrorMessages.ERROR_WEB_REQUEST_TIMED_OUT, webProps.urlString);
            } catch (Exception e6) {
                if (method.equals("Get")) {
                    message = ErrorMessages.ERROR_WEB_UNABLE_TO_GET;
                    args = new String[]{webProps.urlString};
                } else if (method.equals("Delete")) {
                    message = ErrorMessages.ERROR_WEB_UNABLE_TO_DELETE;
                    args = new String[]{webProps.urlString};
                } else if (method.equals("PostFile") || method.equals("PutFile") || method.equals("PatchFile")) {
                    message = 1104;
                    args = new String[]{postFile, webProps.urlString};
                } else {
                    message = 1103;
                    String content = "";
                    if (postData != null) {
                        try {
                            content = new String(postData, "UTF-8");
                        } catch (UnsupportedEncodingException e7) {
                            Log.e(LOG_TAG, "UTF-8 is the default charset for Android but not available???");
                        }
                    }
                    args = new String[]{content, webProps.urlString};
                }
                this.form.dispatchErrorOccurredEvent(this, method, message, (Object[]) args);
            }
        } else {
            final CapturedProperties capturedProperties4 = webProps;
            final byte[] bArr = postData;
            final String str2 = postFile;
            final String str3 = httpVerb;
            final String str4 = method;
            Form form = this.form;
            form.askPermission(new BulkPermissionRequest(this, method, (String[]) neededPermissions.toArray(new String[0])) {
                public void onGranted() {
                    if (neededPermissions.contains("android.permission.READ_EXTERNAL_STORAGE")) {
                        boolean unused = this.haveReadPermission = true;
                    }
                    if (neededPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
                        boolean unused2 = this.haveWritePermission = true;
                    }
                    AsynchUtil.runAsynchronously(new Runnable() {
                        public void run() {
                            this.performRequest(capturedProperties4, bArr, str2, str3, str4);
                        }
                    });
                }
            });
        }
    }

    private static HttpURLConnection openConnection(CapturedProperties webProps, String httpVerb) throws IOException, ClassCastException, ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) webProps.url.openConnection();
        connection.setConnectTimeout(webProps.timeout);
        connection.setReadTimeout(webProps.timeout);
        if (httpVerb.equals("PUT") || httpVerb.equals("PATCH") || httpVerb.equals("DELETE")) {
            connection.setRequestMethod(httpVerb);
        }
        for (Map.Entry<String, List<String>> header : webProps.requestHeaders.entrySet()) {
            String name = header.getKey();
            for (String value : header.getValue()) {
                connection.addRequestProperty(name, value);
            }
        }
        if (webProps.cookies != null) {
            for (Map.Entry<String, List<String>> cookie : webProps.cookies.entrySet()) {
                String name2 = cookie.getKey();
                for (String value2 : cookie.getValue()) {
                    connection.addRequestProperty(name2, value2);
                }
            }
        }
        return connection;
    }

    private static void writeRequestData(HttpURLConnection connection, byte[] postData) throws IOException {
        connection.setDoOutput(true);
        connection.setFixedLengthStreamingMode(postData.length);
        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        try {
            out.write(postData, 0, postData.length);
            out.flush();
        } finally {
            out.close();
        }
    }

    private void writeRequestFile(HttpURLConnection connection, String path) throws IOException {
        BufferedOutputStream out;
        BufferedInputStream in = new BufferedInputStream(MediaUtil.openMedia(this.form, path));
        try {
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);
            out = new BufferedOutputStream(connection.getOutputStream());
            while (true) {
                int b = in.read();
                if (b == -1) {
                    out.flush();
                    out.close();
                    in.close();
                    return;
                }
                out.write(b);
            }
        } catch (Throwable th) {
            in.close();
            throw th;
        }
    }

    private static String getResponseType(HttpURLConnection connection) {
        String responseType = connection.getContentType();
        return responseType != null ? responseType : "";
    }

    private void processResponseCookies(HttpURLConnection connection) {
        if (this.allowCookies && this.cookieHandler != null) {
            try {
                this.cookieHandler.put(connection.getURL().toURI(), connection.getHeaderFields());
            } catch (IOException | URISyntaxException e) {
            }
        }
    }

    private static String getResponseContent(HttpURLConnection connection) throws IOException {
        String encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        InputStreamReader reader = new InputStreamReader(getConnectionStream(connection), encoding);
        try {
            int contentLength = connection.getContentLength();
            StringBuilder sb = contentLength != -1 ? new StringBuilder(contentLength) : new StringBuilder();
            char[] buf = new char[1024];
            while (true) {
                int read = reader.read(buf);
                if (read == -1) {
                    return sb.toString();
                }
                sb.append(buf, 0, read);
            }
        } finally {
            reader.close();
        }
    }

    private String saveResponseContent(HttpURLConnection connection, String responseFileName2, String responseType) throws IOException {
        BufferedOutputStream out;
        File file = createFile(responseFileName2, responseType);
        File parent = file.getParentFile();
        if (parent.exists() || parent.mkdirs()) {
            BufferedInputStream in = new BufferedInputStream(getConnectionStream(connection), 4096);
            try {
                out = new BufferedOutputStream(new FileOutputStream(file), 4096);
                while (true) {
                    int b = in.read();
                    if (b == -1) {
                        out.flush();
                        out.close();
                        in.close();
                        return file.getAbsolutePath();
                    }
                    out.write(b);
                }
            } catch (Throwable th) {
                in.close();
                throw th;
            }
        } else {
            throw new DispatchableError(ErrorMessages.ERROR_CANNOT_MAKE_DIRECTORY, parent.getAbsolutePath());
        }
    }

    private static InputStream getConnectionStream(HttpURLConnection connection) throws SocketTimeoutException {
        try {
            return connection.getInputStream();
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e2) {
            return connection.getErrorStream();
        }
    }

    private File createFile(String fileName, String responseType) throws IOException, FileUtil.FileException {
        if (!TextUtils.isEmpty(fileName)) {
            return FileUtil.getExternalFile(this.form, fileName);
        }
        int indexOfSemicolon = responseType.indexOf(59);
        if (indexOfSemicolon != -1) {
            responseType = responseType.substring(0, indexOfSemicolon);
        }
        String extension = mimeTypeToExtension.get(responseType);
        if (extension == null) {
            extension = "tmp";
        }
        return FileUtil.getDownloadFile(this.form, extension);
    }

    /* access modifiers changed from: private */
    public static Map<String, List<String>> processRequestHeaders(YailList list) throws InvalidRequestHeadersException {
        Map<String, List<String>> requestHeadersMap = Maps.newHashMap();
        int i = 0;
        while (i < list.size()) {
            Object item = list.getObject(i);
            if (item instanceof YailList) {
                YailList sublist = (YailList) item;
                if (sublist.size() == 2) {
                    String fieldName = sublist.getObject(0).toString();
                    Object fieldValues = sublist.getObject(1);
                    String key = fieldName;
                    List<String> values = Lists.newArrayList();
                    if (fieldValues instanceof YailList) {
                        YailList multipleFieldsValues = (YailList) fieldValues;
                        for (int j = 0; j < multipleFieldsValues.size(); j++) {
                            values.add(multipleFieldsValues.getObject(j).toString());
                        }
                    } else {
                        values.add(fieldValues.toString());
                    }
                    requestHeadersMap.put(key, values);
                    i++;
                } else {
                    throw new InvalidRequestHeadersException(ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_TWO_ELEMENTS, i + 1);
                }
            } else {
                throw new InvalidRequestHeadersException(ErrorMessages.ERROR_WEB_REQUEST_HEADER_NOT_LIST, i + 1);
            }
        }
        return requestHeadersMap;
    }

    private CapturedProperties capturePropertyValues(String functionName) {
        try {
            return new CapturedProperties(this);
        } catch (MalformedURLException e) {
            this.form.dispatchErrorOccurredEvent(this, functionName, ErrorMessages.ERROR_WEB_MALFORMED_URL, this.urlString);
        } catch (InvalidRequestHeadersException e2) {
            this.form.dispatchErrorOccurredEvent(this, functionName, e2.errorNumber, Integer.valueOf(e2.index));
        }
        return null;
    }

    public Future<YailList> getDataValue(final YailList key) {
        final FutureTask<Void> currentTask = this.lastTask;
        FutureTask<YailList> getDataValueTask = new FutureTask<>(new Callable<YailList>() {
            public YailList call() throws Exception {
                if (currentTask != null && !currentTask.isDone() && !currentTask.isCancelled()) {
                    try {
                        currentTask.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e2) {
                        e2.printStackTrace();
                    }
                }
                return Web.this.getColumns(key);
            }
        });
        AsynchUtil.runAsynchronously(getDataValueTask);
        return getDataValueTask;
    }

    private void updateColumns(String responseContent, String responseType) {
        if (responseType.contains("json")) {
            try {
                this.columns = JsonUtil.getColumnsFromJson(responseContent);
            } catch (JSONException e) {
            }
        } else if (responseType.contains("csv") || responseType.startsWith("text/")) {
            try {
                this.columns = CsvUtil.fromCsvTable(responseContent);
                this.columns = ChartDataSourceUtil.getTranspose(this.columns);
            } catch (Exception e2) {
                this.columns = new YailList();
            }
        }
    }

    public YailList getColumn(String column) {
        for (int i = 0; i < this.columns.size(); i++) {
            YailList list = (YailList) this.columns.getObject(i);
            if (!list.isEmpty() && list.getString(0).equals(column)) {
                return list;
            }
        }
        return new YailList();
    }

    public YailList getColumns(YailList keyColumns) {
        ArrayList<YailList> resultingColumns = new ArrayList<>();
        for (int i = 0; i < keyColumns.size(); i++) {
            resultingColumns.add(getColumn(keyColumns.getString(i)));
        }
        return YailList.makeList((List) resultingColumns);
    }

    public void addDataObserver(DataSourceChangeListener dataComponent) {
        this.dataSourceObservers.add(dataComponent);
    }

    public void removeDataObserver(DataSourceChangeListener dataComponent) {
        this.dataSourceObservers.remove(dataComponent);
    }

    public void notifyDataObservers(YailList key, Object newValue) {
        Iterator<DataSourceChangeListener> it = this.dataSourceObservers.iterator();
        while (it.hasNext()) {
            it.next().onDataSourceValueChange(this, (String) null, this.columns);
        }
    }
}
