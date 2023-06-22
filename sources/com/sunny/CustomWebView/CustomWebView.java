package com.sunny.CustomWebView;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.HVArrangement;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.NanoHTTPD;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.sunny.CustomWebView.WView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@DesignerComponent(androidMinSdk = 21, category = ComponentCategory.EXTENSION, description = "An extended form of Web Viewer <br> Developed by Sunny Gupta", helpUrl = "https://github.com/vknow360/CustomWebView", iconName = "https://res.cloudinary.com/andromedaviewflyvipul/image/upload/c_scale,h_20,w_20/v1571472765/ktvu4bapylsvnykoyhdm.png", nonVisible = true, version = 12)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.WRITE_EXTERNAL_STORAGE,android.permission.ACCESS_DOWNLOAD_MANAGER,android.permission.ACCESS_FINE_LOCATION,android.permission.RECORD_AUDIO, android.permission.MODIFY_AUDIO_SETTINGS, android.permission.CAMERA,android.permission.VIBRATE,android.webkit.resource.VIDEO_CAPTURE,android.webkit.resource.AUDIO_CAPTURE,android.launcher.permission.INSTALL_SHORTCUT")
public final class CustomWebView extends AndroidNonvisibleComponent implements WView.SwipeCallback {
    /* access modifiers changed from: private */
    public String AD_HOSTS = "";
    private String MOBILE_USER_AGENT = "";
    private String UserAgent = "";
    /* access modifiers changed from: private */
    public final Activity activity;
    /* access modifiers changed from: private */
    public boolean blockAds = false;
    private final Context context;
    private final CookieManager cookieManager;
    private final List<String> customDeepLink = new ArrayList();
    /* access modifiers changed from: private */
    public boolean deepLinks = false;
    private boolean desktopMode = false;
    private final float deviceDensity;
    private boolean displayZoom = true;
    /* access modifiers changed from: private */
    public Message dontSend;
    /* access modifiers changed from: private */
    public boolean followLinks = true;
    /* access modifiers changed from: private */
    public HttpAuthHandler httpAuthHandler;
    private int iD = 0;
    /* access modifiers changed from: private */
    public boolean isLoading = false;
    /* access modifiers changed from: private */
    public boolean isScrollEnabled = true;
    /* access modifiers changed from: private */
    public String jobName = "";
    /* access modifiers changed from: private */
    public JsResult jsAlert;
    /* access modifiers changed from: private */
    public JsPromptResult jsPromptResult;
    /* access modifiers changed from: private */
    public JsResult jsResult;
    /* access modifiers changed from: private */
    public ValueCallback<Uri[]> mFilePathCallback;
    /* access modifiers changed from: private */
    public PermissionRequest permissionRequest;
    /* access modifiers changed from: private */
    public PrintJob printJob;
    /* access modifiers changed from: private */
    public boolean prompt = true;
    /* access modifiers changed from: private */
    public Message reSend;
    /* access modifiers changed from: private */
    public Message resultObj;
    /* access modifiers changed from: private */
    public SslErrorHandler sslHandler;
    /* access modifiers changed from: private */
    public GeolocationPermissions.Callback theCallback;
    /* access modifiers changed from: private */
    public String theOrigin;
    /* access modifiers changed from: private */
    public WView webView;
    /* access modifiers changed from: private */
    public final HashMap<Integer, WView> wv = new HashMap<>();
    private final WebViewInterface wvInterface;
    private boolean zoomEnabled = true;
    private int zoomPercent = 100;

    private class AdBlocker {
        private AdBlocker() {
        }

        /* access modifiers changed from: private */
        public WebResourceResponse createEmptyResource() {
            return new WebResourceResponse(NanoHTTPD.MIME_PLAINTEXT, "utf-8", new ByteArrayInputStream("".getBytes()));
        }

        private String getHost(String str) {
            try {
                return new URL(str).getHost() != null ? new URL(str).getHost() : "";
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        /* access modifiers changed from: private */
        public boolean isAd(String str) {
            try {
                return isAdHost(getHost(str));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        /* access modifiers changed from: private */
        public boolean isAdHost(String str) {
            if (CustomWebView.this.webView.getUrl().contains(str)) {
                return false;
            }
            return CustomWebView.this.AD_HOSTS.contains(str);
        }
    }

    private class ChromeClient extends WebChromeClient {
        private final int FULL_SCREEN_SETTING;
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        private ChromeClient() {
            this.FULL_SCREEN_SETTING = 3846;
        }

        public void onCloseWindow(WebView webView) {
            CustomWebView.this.OnCloseWindowRequest(CustomWebView.this.getIndex(webView));
        }

        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            CustomWebView.this.OnConsoleMessage(consoleMessage.message(), consoleMessage.lineNumber(), consoleMessage.lineNumber(), consoleMessage.messageLevel().toString());
            return true;
        }

        public boolean onCreateWindow(WebView webView, boolean z, boolean z2, Message message) {
            if (CustomWebView.this.SupportMultipleWindows()) {
                Message unused = CustomWebView.this.resultObj = message;
                CustomWebView.this.OnNewWindowRequest(CustomWebView.this.getIndex(webView), z, z2);
            }
            return CustomWebView.this.SupportMultipleWindows();
        }

        public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
            if (!CustomWebView.this.prompt) {
                callback.invoke(str, true, true);
                return;
            }
            GeolocationPermissions.Callback unused = CustomWebView.this.theCallback = callback;
            String unused2 = CustomWebView.this.theOrigin = str;
            CustomWebView.this.OnGeolocationRequested(str);
        }

        public void onHideCustomView() {
            CustomWebView.this.OnHideCustomView();
            ((FrameLayout) CustomWebView.this.activity.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            CustomWebView.this.activity.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            CustomWebView.this.activity.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
            CustomWebView.this.activity.setRequestedOrientation(2);
        }

        public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            CustomWebView.this.OnJsAlert(CustomWebView.this.getIndex(webView), str, str2);
            JsResult unused = CustomWebView.this.jsAlert = jsResult;
            return CustomWebView.this.EnableJS();
        }

        public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
            JsResult unused = CustomWebView.this.jsResult = jsResult;
            CustomWebView.this.OnJsConfirm(CustomWebView.this.getIndex(webView), str, str2);
            return CustomWebView.this.EnableJS();
        }

        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            JsPromptResult unused = CustomWebView.this.jsPromptResult = jsPromptResult;
            CustomWebView.this.OnJsPrompt(CustomWebView.this.getIndex(webView), str, str2, str3);
            return CustomWebView.this.EnableJS();
        }

        public void onPermissionRequest(PermissionRequest permissionRequest) {
            if (!CustomWebView.this.prompt) {
                permissionRequest.grant(permissionRequest.getResources());
                return;
            }
            PermissionRequest unused = CustomWebView.this.permissionRequest = permissionRequest;
            CustomWebView.this.OnPermissionRequest(Arrays.asList(permissionRequest.getResources()));
        }

        public void onProgressChanged(WebView webView, int i) {
            CustomWebView.this.OnProgressChanged(CustomWebView.this.getIndex(webView), i);
        }

        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            CustomWebView.this.OnShowCustomView();
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = view;
            this.mOriginalSystemUiVisibility = CustomWebView.this.activity.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = CustomWebView.this.activity.getRequestedOrientation();
            this.mCustomViewCallback = customViewCallback;
            ((FrameLayout) CustomWebView.this.activity.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            CustomWebView.this.activity.getWindow().getDecorView().setSystemUiVisibility(3846);
            CustomWebView.this.activity.setRequestedOrientation(2);
            this.mCustomView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                public void onSystemUiVisibilityChange(int i) {
                    ChromeClient.this.updateControls();
                }
            });
        }

        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            ValueCallback unused = CustomWebView.this.mFilePathCallback = valueCallback;
            CustomWebView.this.FileUploadNeeded(CustomWebView.this.getIndex(webView), fileChooserParams.getAcceptTypes()[0], fileChooserParams.isCaptureEnabled());
            return CustomWebView.this.FileAccess();
        }

        /* access modifiers changed from: package-private */
        public void updateControls() {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mCustomView.getLayoutParams();
            layoutParams.bottomMargin = 0;
            layoutParams.topMargin = 0;
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.height = -1;
            layoutParams.width = -1;
            this.mCustomView.setLayoutParams(layoutParams);
            CustomWebView.this.activity.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

    public class PrintDocumentAdapterWrapper extends PrintDocumentAdapter {
        private final PrintDocumentAdapter delegate;

        public PrintDocumentAdapterWrapper(PrintDocumentAdapter printDocumentAdapter) {
            this.delegate = printDocumentAdapter;
        }

        public void onFinish() {
            this.delegate.onFinish();
            CustomWebView.this.GotPrintResult(CustomWebView.this.jobName, CustomWebView.this.printJob.isCompleted(), CustomWebView.this.printJob.isFailed(), CustomWebView.this.printJob.isBlocked());
        }

        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
            this.delegate.onLayout(printAttributes, printAttributes2, cancellationSignal, layoutResultCallback, bundle);
        }

        public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
            this.delegate.onWrite(pageRangeArr, parcelFileDescriptor, cancellationSignal, writeResultCallback);
        }
    }

    private class WebClient extends WebViewClient {
        private static final String ASSET_PREFIX = "file:///appinventor_asset/";

        private WebClient() {
        }

        private WebResourceResponse handleAppRequest(String str) {
            String substring = str.startsWith(ASSET_PREFIX) ? str.substring(ASSET_PREFIX.length()) : str.substring(str.indexOf("//localhost/") + 12);
            try {
                InputStream openAsset = CustomWebView.this.form.openAsset(substring);
                HashMap hashMap = new HashMap();
                hashMap.put("Access-Control-Allow-Origin", "localhost");
                String contentTypeFor = URLConnection.getFileNameMap().getContentTypeFor(substring);
                String str2 = "utf-8";
                if (contentTypeFor == null || (!contentTypeFor.startsWith("text/") && !contentTypeFor.equals("application/javascript"))) {
                    str2 = null;
                }
                return Build.VERSION.SDK_INT >= 21 ? new WebResourceResponse(contentTypeFor, str2, 200, "OK", hashMap, openAsset) : new WebResourceResponse(contentTypeFor, str2, openAsset);
            } catch (Exception e) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(NanoHTTPD.HTTP_NOTFOUND.getBytes());
                return Build.VERSION.SDK_INT >= 21 ? new WebResourceResponse(NanoHTTPD.MIME_PLAINTEXT, "utf-8", 404, "Not Found", (Map) null, byteArrayInputStream) : new WebResourceResponse(NanoHTTPD.MIME_PLAINTEXT, "utf-8", byteArrayInputStream);
            }
        }

        public void onFormResubmission(WebView webView, Message message, Message message2) {
            Message unused = CustomWebView.this.dontSend = message;
            Message unused2 = CustomWebView.this.reSend = message2;
            CustomWebView.this.OnFormResubmission(CustomWebView.this.getIndex(webView));
        }

        public void onPageFinished(WebView webView, String str) {
            if (CustomWebView.this.wv.get(Integer.valueOf(CustomWebView.this.CurrentId())) == webView && CustomWebView.this.isLoading) {
                boolean unused = CustomWebView.this.isLoading = false;
                CustomWebView.this.PageLoaded(CustomWebView.this.getIndex(webView));
            }
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            if (!CustomWebView.this.isLoading) {
                CustomWebView.this.PageStarted(CustomWebView.this.getIndex(webView), str);
                boolean unused = CustomWebView.this.isLoading = true;
            }
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            CustomWebView.this.OnErrorReceived(CustomWebView.this.getIndex(webView), str, i, str2);
        }

        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            CustomWebView.this.OnErrorReceived(CustomWebView.this.getIndex(webView), webResourceError.getDescription().toString(), webResourceError.getErrorCode(), webResourceRequest.getUrl().toString());
        }

        public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
            HttpAuthHandler unused = CustomWebView.this.httpAuthHandler = httpAuthHandler;
            CustomWebView.this.OnReceivedHttpAuthRequest(CustomWebView.this.getIndex(webView), str, str2);
        }

        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
            CustomWebView.this.OnErrorReceived(CustomWebView.this.getIndex(webView), webResourceResponse.getReasonPhrase(), webResourceResponse.getStatusCode(), webResourceRequest.getUrl().toString());
        }

        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            SslErrorHandler unused = CustomWebView.this.sslHandler = sslErrorHandler;
            CustomWebView.this.OnReceivedSslError(sslError.getPrimaryError());
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            if (!"localhost".equals(webResourceRequest.getUrl().getAuthority()) && !webResourceRequest.getUrl().toString().startsWith(ASSET_PREFIX)) {
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }
            if (!CustomWebView.this.blockAds) {
                return handleAppRequest(webResourceRequest.getUrl().toString());
            }
            AdBlocker adBlocker = new AdBlocker();
            return adBlocker.isAdHost(webResourceRequest.getUrl().getHost()) ? adBlocker.createEmptyResource() : super.shouldInterceptRequest(webView, webResourceRequest);
        }

        public WebResourceResponse shouldInterceptRequest(WebView webView, String str) {
            if (!str.startsWith("http://localhost/") && !str.startsWith(ASSET_PREFIX)) {
                return super.shouldInterceptRequest(webView, str);
            }
            if (!CustomWebView.this.blockAds) {
                return handleAppRequest(str);
            }
            AdBlocker adBlocker = new AdBlocker();
            return adBlocker.isAd(str) ? adBlocker.createEmptyResource() : super.shouldInterceptRequest(webView, str);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            String uri = webResourceRequest.getUrl().toString();
            if (uri.startsWith("http")) {
                return !CustomWebView.this.followLinks;
            }
            if (CustomWebView.this.deepLinks) {
                return CustomWebView.this.DeepLinkParser(uri);
            }
            return false;
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str.startsWith("http")) {
                return !CustomWebView.this.followLinks;
            }
            if (CustomWebView.this.deepLinks) {
                return CustomWebView.this.DeepLinkParser(str);
            }
            return false;
        }
    }

    public class WebViewInterface {
        String webViewString = "";

        WebViewInterface() {
        }

        @JavascriptInterface
        public String getWebViewString() {
            return this.webViewString;
        }

        @JavascriptInterface
        public void setWebViewString(final String str) {
            this.webViewString = str;
            CustomWebView.this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    CustomWebView.this.WebViewStringChanged(str);
                }
            });
        }

        public void setWebViewStringFromBlocks(String str) {
            this.webViewString = str;
        }
    }

    public CustomWebView(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.activity = componentContainer.$context();
        this.context = this.activity;
        this.wvInterface = new WebViewInterface();
        this.cookieManager = CookieManager.getInstance();
        this.deviceDensity = componentContainer.$form().deviceDensity();
        this.webView = new WView(-1, this.context, this);
        resetWebView(this.webView);
    }

    private void CancelJsRequests() {
        if (this.jsAlert != null) {
            this.jsAlert.cancel();
            this.jsAlert = null;
        } else if (this.jsResult != null) {
            this.jsResult.cancel();
            this.jsResult = null;
        } else if (this.jsPromptResult != null) {
            this.jsPromptResult.cancel();
            this.jsPromptResult = null;
        } else if (this.mFilePathCallback != null) {
            this.mFilePathCallback.onReceiveValue((Object) null);
            this.mFilePathCallback = null;
        }
    }

    /* access modifiers changed from: private */
    public boolean DeepLinkParser(String str) {
        PackageManager packageManager = this.context.getPackageManager();
        if (str.startsWith("tel:")) {
            this.activity.startActivity(new Intent("android.intent.action.DIAL", Uri.parse(str)));
            return true;
        } else if (str.startsWith("mailto:") || str.startsWith("sms:")) {
            this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            return true;
        } else if (str.startsWith("whatsapp:")) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.setPackage("com.whatsapp");
            this.activity.startActivity(intent);
            return true;
        } else if (str.startsWith("geo:")) {
            Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent2.setPackage("com.google.android.apps.maps");
            if (intent2.resolveActivity(packageManager) == null) {
                return false;
            }
            this.activity.startActivity(intent2);
            return true;
        } else if (str.startsWith("intent:")) {
            try {
                Intent parseUri = Intent.parseUri(str, 1);
                if (parseUri.resolveActivity(packageManager) != null) {
                    this.activity.startActivity(parseUri);
                    return true;
                }
                String stringExtra = parseUri.getStringExtra("browser_fallback_url");
                if (stringExtra != null) {
                    this.webView.loadUrl(stringExtra);
                }
                Intent data = new Intent("android.intent.action.VIEW").setData(Uri.parse("market://details?id=" + parseUri.getPackage()));
                if (data.resolveActivity(packageManager) == null) {
                    return false;
                }
                this.activity.startActivity(data);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else if (this.customDeepLink.isEmpty() || !this.customDeepLink.contains(str.split(":")[0])) {
            return false;
        } else {
            this.activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
            return true;
        }
    }

    private int d2p(int i) {
        return Math.round(((float) i) / this.deviceDensity);
    }

    /* access modifiers changed from: private */
    public int getIndex(WebView webView2) {
        return ((Integer) new ArrayList(this.wv.keySet()).get(new ArrayList(this.wv.values()).indexOf(webView2))).intValue();
    }

    /* access modifiers changed from: private */
    public int p2d(int i) {
        return Math.round(((float) i) * this.deviceDensity);
    }

    private void resetWebView(final WebView webView2) {
        webView2.addJavascriptInterface(this.wvInterface, "AppInventor");
        this.MOBILE_USER_AGENT = webView2.getSettings().getUserAgentString();
        webView2.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webView2.getSettings().setCacheMode(-1);
        webView2.setFocusable(true);
        webView2.setWebViewClient(new WebClient());
        webView2.setWebChromeClient(new ChromeClient());
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.getSettings().setDisplayZoomControls(this.displayZoom);
        webView2.getSettings().setAllowFileAccess(false);
        webView2.getSettings().setAllowFileAccessFromFileURLs(false);
        webView2.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webView2.getSettings().setAllowContentAccess(false);
        webView2.getSettings().setSupportZoom(this.zoomEnabled);
        webView2.getSettings().setBuiltInZoomControls(this.zoomEnabled);
        webView2.setLongClickable(false);
        webView2.getSettings().setTextZoom(this.zoomPercent);
        this.cookieManager.setAcceptThirdPartyCookies(webView2, true);
        webView2.getSettings().setDomStorageEnabled(true);
        webView2.setVerticalScrollBarEnabled(true);
        webView2.setHorizontalScrollBarEnabled(true);
        webView2.getSettings().setDefaultFontSize(16);
        webView2.getSettings().setBlockNetworkImage(false);
        webView2.getSettings().setLoadsImagesAutomatically(true);
        webView2.getSettings().setLoadWithOverviewMode(true);
        webView2.getSettings().setUseWideViewPort(true);
        webView2.getSettings().setBlockNetworkLoads(false);
        webView2.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView2.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView2.getSettings().setSupportMultipleWindows(true);
        webView2.getSettings().setGeolocationDatabasePath((String) null);
        webView2.getSettings().setDatabaseEnabled(true);
        webView2.getSettings().setGeolocationEnabled(false);
        if (this.UserAgent.isEmpty()) {
            this.UserAgent = this.MOBILE_USER_AGENT;
        }
        webView2.getSettings().setUserAgentString(this.UserAgent);
        webView2.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
                CustomWebView.this.OnDownloadNeeded(CustomWebView.this.getIndex(webView2), str, str3, str4, j);
            }
        });
        webView2.setFindListener(new WebView.FindListener() {
            public void onFindResultReceived(int i, int i2, boolean z) {
                CustomWebView.this.FindResultReceived(CustomWebView.this.getIndex(webView2), i, i2, z);
            }
        });
        webView2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!CustomWebView.this.isScrollEnabled) {
                    return motionEvent.getAction() == 2;
                }
                switch (motionEvent.getAction()) {
                    case 0:
                    case 1:
                        if (view.hasFocus()) {
                            return false;
                        }
                        view.requestFocus();
                        return false;
                    default:
                        return false;
                }
            }
        });
        webView2.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                if (!CustomWebView.this.webView.isLongClickable()) {
                    return true;
                }
                WebView.HitTestResult hitTestResult = CustomWebView.this.webView.getHitTestResult();
                String extra = hitTestResult.getExtra();
                int type = hitTestResult.getType();
                if (type == 0) {
                    return false;
                }
                String str = extra == null ? "" : extra;
                String str2 = "";
                if (type == 8) {
                    Message obtainMessage = new Handler().obtainMessage();
                    webView2.requestFocusNodeHref(obtainMessage);
                    str2 = (String) obtainMessage.getData().get("url");
                }
                CustomWebView.this.LongClicked(CustomWebView.this.getIndex(webView2), str, str2, type);
                return !CustomWebView.this.webView.isLongClickable();
            }
        });
        webView2.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                CustomWebView.this.OnScrollChanged(CustomWebView.this.getIndex(webView2), i, i2, i3, i4, webView2.canScrollHorizontally(-1), webView2.canScrollHorizontally(1));
            }
        });
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty(description = "Sets the ad hosts which will be blocked")
    public void AdHosts(String str) {
        this.AD_HOSTS = str;
    }

    @SimpleEvent(description = "Event raised after 'SaveArchive' method.If 'success' is true then returns file path else empty string.")
    public void AfterArchiveSaved(boolean z, String str) {
        EventDispatcher.dispatchEvent(this, "AfterArchiveSaved", Boolean.valueOf(z), str);
    }

    @SimpleEvent(description = "Event raised after evaluating Js and returns result.")
    public void AfterJavaScriptEvaluated(String str) {
        EventDispatcher.dispatchEvent(this, "AfterJavaScriptEvaluated", str);
    }

    @SimpleFunction
    public void AllowGeolocationAccess(boolean z, boolean z2) {
        if (this.theCallback != null) {
            this.theCallback.invoke(this.theOrigin, z, z2);
            this.theCallback = null;
            this.theOrigin = "";
        }
    }

    @SimpleProperty(description = "Sets whether the WebView should load image resources")
    public void AutoLoadImages(boolean z) {
        this.webView.getSettings().setBlockNetworkImage(!z);
        this.webView.getSettings().setLoadsImagesAutomatically(z);
    }

    @SimpleProperty(description = "Returnss whether the WebView should load image resources")
    public boolean AutoLoadImages() {
        return this.webView.getSettings().getLoadsImagesAutomatically();
    }

    @SimpleProperty(description = "Sets whether the WebView requires a user gesture to play media")
    public void AutoplayMedia(boolean z) {
        this.webView.getSettings().setMediaPlaybackRequiresUserGesture(z);
    }

    @SimpleProperty(description = "Returns whether the WebView requires a user gesture to play media")
    public boolean AutoplayMedia() {
        return this.webView.getSettings().getMediaPlaybackRequiresUserGesture();
    }

    @SimpleProperty(description = "Sets background color of webview")
    public void BackgroundColor(int i) {
        this.webView.setBackgroundColor(i);
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(description = "Sets whether to block ads or not")
    public void BlockAds(boolean z) {
        this.blockAds = z;
    }

    @SimpleProperty(description = "Sets whether the WebView should not load resources from the network.Use this to save data.")
    public void BlockNetworkLoads(boolean z) {
        this.webView.getSettings().setBlockNetworkLoads(z);
    }

    @SimpleProperty(description = "Returns whether the WebView should not load resources from the network")
    public boolean BlockNetworkLoads() {
        return this.webView.getSettings().getBlockNetworkLoads();
    }

    @SimpleProperty(description = "Gets cache mode of active webview")
    public int CacheMode() {
        return this.webView.getSettings().getCacheMode();
    }

    @SimpleProperty(description = "Sets cache mode for active webview")
    public void CacheMode(int i) {
        this.webView.getSettings().setCacheMode(i);
    }

    @SimpleFunction(description = "Gets whether this WebView has a back history item")
    public boolean CanGoBack() {
        return this.webView.canGoBack();
    }

    @SimpleFunction(description = "Gets whether the page can go back or forward the given number of steps.")
    public boolean CanGoBackOrForward(int i) {
        return this.webView.canGoBackOrForward(i);
    }

    @SimpleFunction(description = "Gets whether this WebView has a forward history item.")
    public boolean CanGoForward() {
        return this.webView.canGoForward();
    }

    @SimpleFunction(description = "Cancels current print job. You can request cancellation of a queued, started, blocked, or failed print job.")
    public void CancelPrinting() throws Exception {
        this.printJob.cancel();
    }

    @SimpleFunction(description = "Clears the resource cache.")
    public void ClearCache() {
        this.webView.clearCache(true);
    }

    @SimpleFunction(description = "Removes all cookies and raises 'CookiesRemoved' event")
    public void ClearCookies() {
        this.cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
            public void onReceiveValue(Boolean bool) {
                CustomWebView.this.CookiesRemoved(bool.booleanValue());
            }
        });
        this.cookieManager.flush();
    }

    @SimpleFunction(description = "Clears the form data of the webview <br> Added by Xoma")
    public void ClearFormData(int i) {
        WebView webView2 = this.wv.get(Integer.valueOf(i));
        if (webView2 != null) {
            webView2.clearFormData();
        }
    }

    @SimpleFunction(description = "Tells this WebView to clear its internal back/forward list.")
    public void ClearInternalHistory() {
        this.webView.clearHistory();
    }

    @SimpleFunction(description = "Clear all location preferences.")
    public void ClearLocation() {
        GeolocationPermissions.getInstance().clearAll();
    }

    @SimpleFunction(description = "Clears the highlighting surrounding text matches.")
    public void ClearMatches() {
        this.webView.clearMatches();
    }

    @SimpleFunction(description = "Whether to proceed JavaScript originated request")
    public void ConfirmJs(boolean z) {
        if (this.jsResult != null) {
            if (z) {
                this.jsResult.confirm();
            } else {
                this.jsResult.cancel();
            }
            this.jsResult = null;
        }
    }

    @SimpleFunction(description = "Gets height of HTML content")
    public int ContentHeight() {
        return d2p(this.webView.getContentHeight());
    }

    @SimpleFunction(description = "Inputs a confirmation response to Js")
    public void ContinueJs(String str) {
        if (this.jsPromptResult != null) {
            this.jsPromptResult.confirm(str);
            this.jsPromptResult = null;
        }
    }

    @SimpleEvent(description = "Event raised after 'ClearCokies' method with result")
    public void CookiesRemoved(boolean z) {
        EventDispatcher.dispatchEvent(this, "CookiesRemoved", Boolean.valueOf(z));
    }

    @SimpleFunction(description = "Creates a shortcut of given website on home screen")
    public void CreateShortcut(String str, String str2, String str3) {
        try {
            Bitmap decodeFile = BitmapFactory.decodeFile(str2);
            if (decodeFile != null) {
                String string = this.context.getSharedPreferences(TinyDB.DEFAULT_NAMESPACE, 0).getString("ssn", "");
                String packageName = this.context.getPackageName();
                Intent intent = new Intent();
                intent.setClassName(this.context, ((ResolveInfo) Objects.requireNonNull(this.context.getPackageManager().resolveActivity(this.context.getPackageManager().getLaunchIntentForPackage(packageName), 0))).activityInfo.name.replaceAll("Screen1", string.length() == 0 ? "Screen1" : JsonUtil.getObjectFromJson(string, true).toString()));
                ArrayList arrayList = new ArrayList();
                arrayList.add(str);
                arrayList.add(Component.TYPEFACE_SERIF);
                intent.putExtra("APP_INVENTOR_START", JsonUtil.getJsonRepresentation(arrayList));
                if (Build.VERSION.SDK_INT < 26) {
                    Intent intent2 = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
                    intent2.putExtra("android.intent.extra.shortcut.INTENT", intent);
                    intent2.putExtra("android.intent.extra.shortcut.NAME", str3);
                    intent2.putExtra("android.intent.extra.shortcut.ICON", decodeFile);
                    intent2.putExtra("duplicate", false);
                    this.context.sendBroadcast(intent2);
                    return;
                }
                ShortcutManager shortcutManager = (ShortcutManager) this.context.getSystemService("shortcut");
                if (shortcutManager.isRequestPinShortcutSupported()) {
                    ShortcutInfo build = new ShortcutInfo.Builder(this.context, str3).setShortLabel(str3).setIcon(Icon.createWithBitmap(decodeFile)).setIntent(intent).build();
                    shortcutManager.requestPinShortcut(build, PendingIntent.getBroadcast(this.context, 0, shortcutManager.createShortcutResultIntent(build), 0).getIntentSender());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SimpleFunction(description = "Creates the webview in given arrangement with id")
    public void CreateWebView(HVArrangement hVArrangement, int i) {
        if (!this.wv.containsKey(Integer.valueOf(i)) || hVArrangement != null) {
            View view = hVArrangement.getView();
            if (!this.wv.containsKey(Integer.valueOf(i))) {
                WView wView = new WView(i, this.context, this);
                resetWebView(wView);
                ((FrameLayout) view).addView(wView, new FrameLayout.LayoutParams(-1, -1));
                this.wv.put(Integer.valueOf(i), wView);
            }
        }
    }

    @SimpleFunction(description = "Returns current id")
    public int CurrentId() {
        return this.iD;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Title of the page currently viewed")
    public String CurrentPageTitle() {
        return this.webView.getTitle() == null ? "" : this.webView.getTitle();
    }

    @SimpleProperty(description = "URL of the page currently viewed")
    public String CurrentUrl() {
        return this.webView.getUrl() == null ? "" : this.webView.getUrl();
    }

    @DesignerProperty(defaultValue = "False", editorType = "boolean")
    @SimpleProperty(description = "Sets whether to enable deep links or not i.e. tel: , whatsapp: , sms: , etc.")
    public void DeepLinks(boolean z) {
        this.deepLinks = z;
    }

    @SimpleProperty(description = "Returns whether deep links are enabled or not")
    public boolean DeepLinks() {
        return this.deepLinks;
    }

    @SimpleProperty(description = "Sets whether to load content in desktop mode")
    public void DesktopMode(boolean z) {
        if (z) {
            this.UserAgent = this.UserAgent.replace("Android", "diordnA").replace("Mobile", "eliboM");
        } else {
            this.UserAgent = this.UserAgent.replace("diordnA", "Android").replace("eliboM", "Mobile");
        }
        this.webView.getSettings().setUserAgentString(this.UserAgent);
        this.desktopMode = z;
    }

    @SimpleProperty(description = "Returns whether to load content in desktop mode")
    public boolean DesktopMode() {
        return this.desktopMode;
    }

    @SimpleFunction(description = "Dismiss previously requested Js alert")
    public void DismissJsAlert() {
        if (this.jsAlert != null) {
            this.jsAlert.cancel();
            this.jsAlert = null;
        }
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(description = "Sets whether the WebView should display on-screen zoom controls")
    public void DisplayZoom(boolean z) {
        this.displayZoom = z;
    }

    @SimpleProperty(description = "Gets whether the WebView should display on-screen zoom controls")
    public boolean DisplayZoom() {
        return this.displayZoom;
    }

    @SimpleProperty(description = "Tells the WebView to enable JavaScript execution.")
    public void EnableJS(boolean z) {
        this.webView.getSettings().setJavaScriptEnabled(z);
    }

    @SimpleProperty(description = "Returns whether webview supports JavaScript execution")
    public boolean EnableJS() {
        return this.webView.getSettings().getJavaScriptEnabled();
    }

    @SimpleFunction(description = "Asynchronously evaluates JavaScript in the context of the currently displayed page.")
    public void EvaluateJavaScript(String str) {
        this.webView.evaluateJavascript(str, new ValueCallback<String>() {
            public void onReceiveValue(String str) {
                CustomWebView.this.AfterJavaScriptEvaluated(str);
            }
        });
    }

    @SimpleProperty(description = "Sets whether webview can access local files.Use this to enable file uploading and loading files using HTML")
    public void FileAccess(boolean z) {
        this.webView.getSettings().setAllowFileAccess(z);
        this.webView.getSettings().setAllowFileAccessFromFileURLs(z);
        this.webView.getSettings().setAllowUniversalAccessFromFileURLs(z);
        this.webView.getSettings().setAllowContentAccess(z);
    }

    @SimpleProperty(description = "Returns whether webview can access local files")
    public boolean FileAccess() {
        return this.webView.getSettings().getAllowFileAccess();
    }

    @SimpleEvent(description = "Event raised when file uploading is needed")
    public void FileUploadNeeded(int i, String str, boolean z) {
        EventDispatcher.dispatchEvent(this, "FileUploadNeeded", Integer.valueOf(i), str, Boolean.valueOf(z));
    }

    @SimpleFunction(description = "Finds all instances of find on the page and highlights them, asynchronously. Successive calls to this will cancel any pending searches.")
    public void Find(String str) {
        this.webView.findAllAsync(str);
    }

    @SimpleFunction(description = "Highlights and scrolls to the next match if 'forward' is true else scrolls to previous match.")
    public void FindNext(boolean z) {
        this.webView.findNext(z);
    }

    @SimpleEvent(description = "Event raised after 'Find' method with int 'activeMatchOrdinal','numberOfMatches' and 'isDoneCounting'")
    public void FindResultReceived(int i, int i2, int i3, boolean z) {
        EventDispatcher.dispatchEvent(this, "FindResultReceived", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Boolean.valueOf(z));
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(description = "Sets whether to follow links or not")
    public void FollowLinks(boolean z) {
        this.followLinks = z;
    }

    @SimpleProperty(description = "Determines whether to follow links when they are tapped in the WebViewer.If you follow links, you can use GoBack and GoForward to navigate the browser history")
    public boolean FollowLinks() {
        return this.followLinks;
    }

    @SimpleProperty(description = "Returns the font size of text")
    public int FontSize() {
        return this.webView.getSettings().getDefaultFontSize();
    }

    @SimpleProperty(description = "Sets the default font size of text. The default is 16.")
    public void FontSize(int i) {
        this.webView.getSettings().setDefaultFontSize(i);
    }

    @SimpleFunction(description = "Get cookies for specific url")
    public String GetCookies(String str) {
        String cookie = CookieManager.getInstance().getCookie(str);
        return cookie != null ? cookie : "";
    }

    @SimpleFunction(description = "Returns a list of used ids")
    public List<Integer> GetIds() {
        return new ArrayList(this.wv.keySet());
    }

    @SimpleFunction(description = "Get internal history of given webview.")
    public List<String> GetInternalHistory(int i) {
        ArrayList arrayList = new ArrayList();
        if (this.wv.containsKey(Integer.valueOf(i))) {
            WebBackForwardList copyBackForwardList = this.wv.get(Integer.valueOf(i)).copyBackForwardList();
            for (int i2 = 0; i2 < copyBackForwardList.getSize(); i2++) {
                arrayList.add(copyBackForwardList.getItemAtIndex(i2).getUrl());
            }
        }
        return arrayList;
    }

    @SimpleFunction(description = "Gets the progress for the given webview")
    public int GetProgress(int i) {
        return this.wv.get(Integer.valueOf(i)).getProgress();
    }

    @SimpleFunction(description = "Return the scrolled left position of the webview")
    public int GetScrollX() {
        return d2p(this.webView.getScrollX());
    }

    @SimpleFunction(description = "Return the scrolled top position of the webview")
    public int GetScrollY() {
        return d2p(this.webView.getScrollY());
    }

    @SimpleFunction(description = "Gets the SSL certificate for the main top-level page and raises 'GotCertificate' event")
    public void GetSslCertificate() {
        SslCertificate certificate = this.webView.getCertificate();
        if (certificate != null) {
            GotCertificate(true, certificate.getIssuedBy().getDName(), certificate.getIssuedTo().getDName(), certificate.getValidNotAfterDate().toString());
        } else {
            GotCertificate(false, "", "", "");
        }
    }

    @SimpleFunction(description = "Returns webview object from id")
    public Object GetWebView(int i) {
        if (this.wv.containsKey(Integer.valueOf(i))) {
            return this.wv.get(Integer.valueOf(i));
        }
        return null;
    }

    @SimpleFunction(description = "Goes back in the history of this WebView.")
    public void GoBack() {
        if (CanGoBack()) {
            this.webView.goBack();
        }
    }

    @SimpleFunction(description = "Goes to the history item that is the number of steps away from the current item. Steps is negative if backward and positive if forward.")
    public void GoBackOrForward(int i) {
        if (CanGoBackOrForward(i)) {
            this.webView.goBackOrForward(i);
        }
    }

    @SimpleFunction(description = "Goes forward in the history of this WebView.")
    public void GoForward() {
        if (CanGoForward()) {
            this.webView.goForward();
        }
    }

    @SimpleFunction(description = "Loads the given URL.")
    public void GoToUrl(String str) {
        CancelJsRequests();
        this.webView.loadUrl(str);
    }

    @SimpleEvent(description = "Event raised after getting SSL certificate of current displayed url/website with boolean 'isSecure' and Strings 'issuedBy','issuedTo' and 'validTill'.If 'isSecure' is false and other values are empty then assume that website is not secure")
    public void GotCertificate(boolean z, String str, String str2, String str3) {
        EventDispatcher.dispatchEvent(this, "GotCertificate", Boolean.valueOf(z), str, str2, str3);
    }

    @SimpleEvent(description = "Event raised after getting previus print's result.")
    public void GotPrintResult(String str, boolean z, boolean z2, boolean z3) {
        EventDispatcher.dispatchEvent(this, "GotPrintResult", str, Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3));
    }

    @SimpleFunction(description = "Grants given permissions to webview.Use empty list to deny the request.")
    public void GrantPermission(final String str) {
        if (this.permissionRequest != null) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (str.isEmpty()) {
                        CustomWebView.this.permissionRequest.deny();
                    } else {
                        CustomWebView.this.permissionRequest.grant(CustomWebView.this.permissionRequest.getResources());
                    }
                    PermissionRequest unused = CustomWebView.this.permissionRequest = null;
                }
            });
        }
    }

    @SimpleFunction(description = "Hides previously shown custom view")
    public void HideCustomView() {
        this.webView.getWebChromeClient().onHideCustomView();
    }

    @SimpleProperty(description = "Sets the initial scale for active WebView. 0 means default. If initial scale is greater than 0, WebView starts with this value as initial scale.")
    public void InitialScale(int i) {
        this.webView.setInitialScale(i);
    }

    @SimpleFunction(description = "Invokes the graphical zoom picker widget for this WebView. This will result in the zoom widget appearing on the screen to control the zoom level of this WebView.Note that it does not checks whether zoom is enabled or not.")
    public void InvokeZoomPicker() {
        this.webView.invokeZoomPicker();
    }

    @SimpleProperty(description = "Gets layer type")
    public int LayerType() {
        return this.webView.getLayerType();
    }

    @SimpleProperty(description = "Sets layer type")
    public void LayerType(int i) {
        this.webView.setLayerType(i, (Paint) null);
    }

    @SimpleFunction(description = "Loads the given data into this WebView using a 'data' scheme URL.")
    public void LoadHtml(String str) {
        CancelJsRequests();
        this.webView.loadDataWithBaseURL((String) null, str, NanoHTTPD.MIME_HTML, "UTF-8", (String) null);
    }

    @SimpleFunction(description = "Loads requested url in given webview")
    public void LoadInNewWindow(int i) {
        if (this.wv.containsKey(Integer.valueOf(i)) && this.resultObj != null) {
            ((WebView.WebViewTransport) this.resultObj.obj).setWebView(this.wv.get(Integer.valueOf(i)));
            this.resultObj.sendToTarget();
            this.resultObj = null;
        }
    }

    @SimpleFunction(description = "Loads the given URL with the specified additional HTTP headers defined is list of lists.")
    public void LoadWithHeaders(String str, YailDictionary yailDictionary) {
        if (!yailDictionary.isEmpty()) {
            HashMap hashMap = new HashMap();
            for (Object next : yailDictionary.keySet()) {
                hashMap.put(next, yailDictionary.get(next));
            }
            this.webView.loadUrl(str, hashMap);
            return;
        }
        GoToUrl(str);
    }

    @SimpleProperty(description = "Sets whether the WebView loads pages in overview mode, that is, zooms out the content to fit on screen by width. This setting is taken into account when the content width is greater than the width of the WebView control.")
    public void LoadWithOverviewMode(boolean z) {
        this.webView.getSettings().setLoadWithOverviewMode(z);
    }

    @SimpleProperty(description = "Returns whether the WebView loads pages in overview mode")
    public boolean LoadWithOverviewMode() {
        return this.webView.getSettings().getLoadWithOverviewMode();
    }

    @SimpleProperty(description = "Sets whether to enable text selection and context menu")
    public void LongClickable(boolean z) {
        this.webView.setLongClickable(z);
    }

    @SimpleProperty(description = "Returns whether text selection and context menu are enabled or not")
    public boolean LongClickable() {
        return this.webView.isLongClickable();
    }

    @SimpleEvent(description = "Event raised when something is long clicked in webview with item(image,string,empty,etc) and type(item type like 0,1,8,etc)")
    public void LongClicked(int i, String str, String str2, int i2) {
        EventDispatcher.dispatchEvent(this, "LongClicked", Integer.valueOf(i), str, str2, Integer.valueOf(i2));
    }

    @SimpleEvent(description = "Event triggered when a window needs to be closed")
    public void OnCloseWindowRequest(int i) {
        EventDispatcher.dispatchEvent(this, "OnCloseWindowRequest", Integer.valueOf(i));
    }

    @SimpleEvent(description = "Event raised after getting console message.")
    public void OnConsoleMessage(String str, int i, int i2, String str2) {
        EventDispatcher.dispatchEvent(this, "OnConsoleMessage", str, Integer.valueOf(i), Integer.valueOf(i2), str2);
    }

    @SimpleEvent(description = "Event raised when downloading is needed.")
    public void OnDownloadNeeded(int i, String str, String str2, String str3, long j) {
        EventDispatcher.dispatchEvent(this, "OnDownloadNeeded", Integer.valueOf(i), str, str2, str3, Long.valueOf(j));
    }

    @SimpleEvent(description = "Event raised when any error is received during loading url and returns message,error code and failing url")
    public void OnErrorReceived(int i, String str, int i2, String str2) {
        EventDispatcher.dispatchEvent(this, "OnErrorReceived", Integer.valueOf(i), str, Integer.valueOf(i2), str2);
    }

    @SimpleEvent(description = "Event raised when resubmission of form is needed")
    public void OnFormResubmission(int i) {
        EventDispatcher.dispatchEvent(this, "OnFormResubmission", Integer.valueOf(i));
    }

    @SimpleEvent(description = "Event raised when page asks for location access. Developer must handle/show dialog from him/herself.")
    public void OnGeolocationRequested(String str) {
        EventDispatcher.dispatchEvent(this, "OnGeolocationRequested", str);
    }

    @SimpleEvent(description = "Event raised when current page exits from full screen mode")
    public void OnHideCustomView() {
        EventDispatcher.dispatchEvent(this, "OnHideCustomView", new Object[0]);
    }

    @SimpleEvent(description = "Event raised when Js have to show an alert to user")
    public void OnJsAlert(int i, String str, String str2) {
        EventDispatcher.dispatchEvent(this, "OnJsAlert", Integer.valueOf(i), str, str2);
    }

    @SimpleEvent(description = "Tells to display a confirm dialog to the user.")
    public void OnJsConfirm(int i, String str, String str2) {
        EventDispatcher.dispatchEvent(this, "OnJsConfirm", Integer.valueOf(i), str, str2);
    }

    @SimpleEvent(description = "Event raised when JavaScript needs input from user")
    public void OnJsPrompt(int i, String str, String str2, String str3) {
        EventDispatcher.dispatchEvent(this, "OnJsPrompt", Integer.valueOf(i), str, str2, str3);
    }

    @SimpleEvent(description = "Event raised when new window is requested by webview with boolean 'isDialog' and 'isPopup'")
    public void OnNewWindowRequest(int i, boolean z, boolean z2) {
        EventDispatcher.dispatchEvent(this, "OnNewWindowRequest", Integer.valueOf(i), Boolean.valueOf(z), Boolean.valueOf(z2));
    }

    @SimpleEvent(description = "Event raised when a website asks for specific permission(s) in list format.")
    public void OnPermissionRequest(List<String> list) {
        EventDispatcher.dispatchEvent(this, "OnPermissionRequest", list);
    }

    @SimpleEvent(description = "Event raised when page loading progress has changed.")
    public void OnProgressChanged(int i, int i2) {
        EventDispatcher.dispatchEvent(this, "OnProgressChanged", Integer.valueOf(i), Integer.valueOf(i2));
    }

    @SimpleEvent(description = "Notifies that the WebView received an HTTP authentication request.")
    public void OnReceivedHttpAuthRequest(int i, String str, String str2) {
        EventDispatcher.dispatchEvent(this, "OnReceivedHttpAuthRequest", Integer.valueOf(i), str, str2);
    }

    @SimpleEvent
    public void OnReceivedSslError(int i) {
        EventDispatcher.dispatchEvent(this, "OnReceivedSslError", Integer.valueOf(i));
    }

    @SimpleEvent(description = "Event raised when webview gets scrolled")
    public void OnScrollChanged(int i, int i2, int i3, int i4, int i5, boolean z, boolean z2) {
        EventDispatcher.dispatchEvent(this, "OnScrollChanged", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Boolean.valueOf(z), Boolean.valueOf(z2));
    }

    @SimpleEvent(description = "Event raised when current page enters in full screen mode")
    public void OnShowCustomView() {
        EventDispatcher.dispatchEvent(this, "OnShowCustomView", new Object[0]);
    }

    @SimpleProperty(description = "Gets over scroll mode")
    public int OverScrollMode() {
        return this.webView.getOverScrollMode();
    }

    @SimpleProperty(description = "Sets over scroll mode")
    public void OverScrollMode(int i) {
        this.webView.setOverScrollMode(i);
    }

    @SimpleFunction(description = "Scrolls the contents of the WebView down by half the page size")
    public void PageDown(boolean z) {
        this.webView.pageDown(z);
    }

    @SimpleEvent(description = "Event raised when page loading has finished.")
    public void PageLoaded(int i) {
        EventDispatcher.dispatchEvent(this, "PageLoaded", Integer.valueOf(i));
    }

    @SimpleEvent(description = "Event indicating that page loading has started in web view.")
    public void PageStarted(int i, String str) {
        EventDispatcher.dispatchEvent(this, "PageStarted", Integer.valueOf(i), str);
    }

    @SimpleFunction(description = "Scrolls the contents of the WebView up by half the page size")
    public void PageUp(boolean z) {
        this.webView.pageUp(z);
    }

    @SimpleFunction(description = "Does a best-effort attempt to pause any processing that can be paused safely, such as animations and geolocation. Note that this call does not pause JavaScript.")
    public void PauseWebView(int i) {
        this.wv.get(Integer.valueOf(i)).onPause();
    }

    @SimpleFunction(description = "Loads the URL with postData using 'POST' method into active WebView.")
    public void PostData(String str, String str2) throws UnsupportedEncodingException {
        this.webView.postUrl(str, str2.getBytes("UTF-8"));
    }

    @SimpleFunction(description = "Prints the content of webview with given document name")
    public void PrintWebContent(String str) throws Exception {
        PrintManager printManager = (PrintManager) this.context.getSystemService("print");
        if (str.isEmpty()) {
            this.jobName = this.webView.getTitle() + "_Document";
        } else {
            this.jobName = str;
        }
        PrintDocumentAdapterWrapper printDocumentAdapterWrapper = new PrintDocumentAdapterWrapper(this.webView.createPrintDocumentAdapter(this.jobName));
        if (printManager != null) {
            this.printJob = printManager.print(this.jobName, printDocumentAdapterWrapper, new PrintAttributes.Builder().build());
        }
    }

    @SimpleFunction(description = "Instructs the WebView to proceed with the authentication with the given credentials.If both parameters are empty then it will cancel the request.")
    public void ProceedHttpAuthRequest(String str, String str2) {
        if (this.httpAuthHandler != null) {
            if (!str.isEmpty() || !str2.isEmpty()) {
                this.httpAuthHandler.proceed(str, str2);
            } else {
                this.httpAuthHandler.cancel();
            }
            this.httpAuthHandler = null;
        }
    }

    @SimpleFunction
    public void ProceedSslError(boolean z) {
        if (this.sslHandler != null) {
            if (z) {
                this.sslHandler.proceed();
            } else {
                this.sslHandler.cancel();
            }
            this.sslHandler = null;
        }
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(description = "Sets whether webview will prompt for permission and raise 'OnPermissionRequest' event or not else assume permission is granted.")
    public void PromptForPermission(boolean z) {
        this.prompt = z;
    }

    @SimpleProperty(description = "Returns whether webview will prompt for permission and raise 'OnPermissionRequest' event or not")
    public boolean PromptForPermission() {
        return this.prompt;
    }

    @SimpleFunction(description = "Registers to open specified link in associated external app(s)")
    public void RegisterDeepLink(String str) {
        this.customDeepLink.add(str);
    }

    @SimpleFunction(description = "Reloads the current URL.")
    public void Reload() {
        CancelJsRequests();
        this.webView.reload();
    }

    @SimpleFunction(description = "Destroys the webview and removes it completely from view system")
    public void RemoveWebView(int i) {
        if (this.wv.containsKey(Integer.valueOf(i))) {
            WebView webView2 = this.wv.get(Integer.valueOf(i));
            ((FrameLayout) webView2.getParent()).removeView(webView2);
            webView2.destroy();
            this.wv.remove(Integer.valueOf(i));
            this.iD = 0;
        }
    }

    @SimpleFunction(description = "Restarts current/previous print job. You can request restart of a failed print job.")
    public void RestartPrinting() throws Exception {
        this.printJob.restart();
    }

    @SimpleFunction(description = "Whether to resubmit form or not.")
    public void ResubmitForm(boolean z) {
        if (this.reSend != null && this.dontSend != null) {
            if (z) {
                this.reSend.sendToTarget();
            } else {
                this.dontSend.sendToTarget();
            }
            this.reSend = null;
            this.dontSend = null;
        }
    }

    @SimpleFunction(description = "Resumes the previously paused WebView.")
    public void ResumeWebView(int i) {
        this.wv.get(Integer.valueOf(i)).onResume();
    }

    @SimpleProperty(description = "Gets rotation angle")
    public float RotationAngle() {
        return this.webView.getRotation();
    }

    @SimpleProperty(description = "Sets rotation angle")
    public void RotationAngle(float f) {
        this.webView.setRotation(f);
    }

    @SimpleFunction(description = "Saves the current site as a web archive")
    public void SaveArchive(String str) {
        this.webView.saveWebArchive(str, true, new ValueCallback<String>() {
            public void onReceiveValue(String str) {
                if (str == null) {
                    CustomWebView.this.AfterArchiveSaved(false, "");
                } else {
                    CustomWebView.this.AfterArchiveSaved(true, str);
                }
            }
        });
    }

    @SimpleProperty(description = "Whether to display horizonatal and vertical scrollbars or not")
    public void ScrollBar(boolean z) {
        this.webView.setVerticalScrollBarEnabled(z);
        this.webView.setHorizontalScrollBarEnabled(z);
    }

    @SimpleProperty(description = "Gets scroll bar style")
    public int ScrollBarStyle() {
        return this.webView.getScrollBarStyle();
    }

    @SimpleProperty(description = "Sets scroll bar style")
    public void ScrollBarStyle(int i) {
        this.webView.setScrollBarStyle(i);
    }

    @SimpleFunction(description = "Scrolls the webview to given position")
    public void ScrollTo(final int i, final int i2) {
        this.webView.postDelayed(new Runnable() {
            public void run() {
                CustomWebView.this.webView.scrollTo(CustomWebView.this.p2d(i), CustomWebView.this.p2d(i2));
            }
        }, 300);
    }

    @SimpleProperty
    public void Scrollable(boolean z) {
        this.isScrollEnabled = z;
    }

    @SimpleFunction(description = "Sets cookies for given url")
    public void SetCookies(String str, String str2) {
        try {
            CookieManager.getInstance().setCookie(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SimpleFunction(description = "Sets the visibility of webview by id")
    public void SetVisibility(int i, boolean z) {
        if (!this.wv.containsKey(Integer.valueOf(i))) {
            return;
        }
        if (z) {
            this.wv.get(Integer.valueOf(i)).setVisibility(0);
        } else {
            this.wv.get(Integer.valueOf(i)).setVisibility(8);
        }
    }

    @SimpleFunction(description = "Set specific webview to current webview by id")
    public void SetWebView(int i) {
        if (this.wv.containsKey(Integer.valueOf(i))) {
            this.webView = this.wv.get(Integer.valueOf(i));
            this.webView.setVisibility(0);
            this.iD = i;
        }
    }

    @SimpleFunction(description = "Stops the current load.")
    public void StopLoading() {
        this.webView.stopLoading();
    }

    @SimpleProperty(description = "Sets whether the WebView supports multiple windows")
    public void SupportMultipleWindows(boolean z) {
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(z);
        this.webView.getSettings().setSupportMultipleWindows(z);
    }

    @SimpleProperty(description = "Returns whether the WebView supports multiple windows")
    public boolean SupportMultipleWindows() {
        return this.webView.getSettings().getJavaScriptCanOpenWindowsAutomatically();
    }

    @SimpleEvent(description = "Event raised when webview is swiped")
    public void Swiped(int i, int i2) {
        EventDispatcher.dispatchEvent(this, "Swiped", Integer.valueOf(i), Integer.valueOf(i2));
    }

    @SimpleFunction(description = "Uploads the given file from content uri.Use empty string to cancel the upload request.")
    public void UploadFile(String str) {
        if (this.mFilePathCallback == null) {
            return;
        }
        if (str.isEmpty()) {
            this.mFilePathCallback.onReceiveValue((Object) null);
            this.mFilePathCallback = null;
            return;
        }
        this.mFilePathCallback.onReceiveValue(new Uri[]{Uri.parse(str)});
        this.mFilePathCallback = null;
    }

    @SimpleProperty(description = "Sets whether the WebView should enable support for the 'viewport' HTML meta tag or should use a wide viewport.")
    public void UseWideViewPort(boolean z) {
        this.webView.getSettings().setUseWideViewPort(z);
    }

    @SimpleProperty(description = "Returns whether the WebView should enable support for the 'viewport' HTML meta tag or should use a wide viewport.")
    public boolean UseWideViewPort() {
        return this.webView.getSettings().getUseWideViewPort();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Get webview user agent")
    public String UserAgent() {
        return this.UserAgent;
    }

    @SimpleProperty(description = "Sets the WebView's user-agent string. If the string is null or empty, the system default value will be used. ")
    public void UserAgent(String str) {
        if (!str.isEmpty()) {
            this.UserAgent = str;
        } else {
            this.UserAgent = this.MOBILE_USER_AGENT;
        }
        this.webView.getSettings().setUserAgentString(this.UserAgent);
    }

    @SimpleProperty(description = "Whether or not to give the application permission to use the Javascript geolocation API")
    public void UsesLocation(boolean z) {
        this.webView.getSettings().setGeolocationEnabled(z);
    }

    @SimpleProperty(description = "Sets whether vibration feedback enabled on long click ")
    public void VibrationEnabled(boolean z) {
        this.webView.setHapticFeedbackEnabled(z);
    }

    @SimpleProperty(description = "Returns whether vibration feedback enabled on long click ")
    public boolean VibrationEnabled() {
        return this.webView.isHapticFeedbackEnabled();
    }

    @SimpleProperty(description = "Returns the visibility of current webview")
    public boolean Visible() {
        return this.webView.getVisibility() == 0;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Get webview string")
    public String WebViewString() {
        return this.wvInterface.webViewString;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Set webview string")
    public void WebViewString(String str) {
        this.wvInterface.setWebViewStringFromBlocks(str);
    }

    @SimpleEvent(description = "When the JavaScript calls AppInventor.setWebViewString this event is run.")
    public void WebViewStringChanged(String str) {
        EventDispatcher.dispatchEvent(this, "WebViewStringChanged", str);
    }

    @SimpleFunction(description = "Performs a zoom operation in the WebView by given zoom percent")
    public void ZoomBy(int i) {
        this.webView.zoomBy((float) i);
    }

    @DesignerProperty(defaultValue = "True", editorType = "boolean")
    @SimpleProperty(description = "Sets whether the WebView should support zooming using its on-screen zoom controls and gestures")
    public void ZoomEnabled(boolean z) {
        this.zoomEnabled = z;
    }

    @SimpleProperty(description = "Gets whether the WebView should support zooming using its on-screen zoom controls and gestures")
    public boolean ZoomEnabled() {
        return this.zoomEnabled;
    }

    @SimpleFunction(description = "Performs zoom in in the WebView")
    public void ZoomIn() {
        this.webView.zoomIn();
    }

    @SimpleFunction(description = "Performs zoom out in the WebView")
    public void ZoomOut() {
        this.webView.zoomOut();
    }

    @SimpleProperty(description = "Gets the zoom of the page in percent")
    public int ZoomPercent() {
        return this.zoomPercent;
    }

    @DesignerProperty(defaultValue = "100", editorType = "integer")
    @SimpleProperty(description = "Sets the zoom of the page in percent. The default is 100")
    public void ZoomPercent(int i) {
        this.zoomPercent = i;
    }

    public void onSwipe(int i, int i2) {
        Swiped(i, i2);
    }
}
