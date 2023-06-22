package com.google.appinventor.components.runtime.util;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;

public class FroyoWebViewClient<T extends Component> extends WebViewClient {
    /* access modifiers changed from: private */
    public final T component;
    private final boolean followLinks;
    private final Form form;
    private final boolean ignoreErrors;

    public FroyoWebViewClient(boolean followLinks2, boolean ignoreErrors2, Form form2, T component2) {
        this.followLinks = followLinks2;
        this.ignoreErrors = ignoreErrors2;
        this.form = form2;
        this.component = component2;
    }

    public T getComponent() {
        return this.component;
    }

    public Form getForm() {
        return this.form;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return !this.followLinks;
    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        if (this.ignoreErrors) {
            handler.proceed();
            return;
        }
        handler.cancel();
        this.form.dispatchErrorOccurredEvent(this.component, "WebView", ErrorMessages.ERROR_WEBVIEW_SSL_ERROR, new Object[0]);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        EventDispatcher.dispatchEvent(this.component, "BeforePageLoad", url);
    }

    public void onPageFinished(WebView view, String url) {
        EventDispatcher.dispatchEvent(this.component, "PageLoaded", url);
    }

    public void onReceivedError(WebView view, final int errorCode, final String description, final String failingUrl) {
        this.form.runOnUiThread(new Runnable() {
            public void run() {
                EventDispatcher.dispatchEvent(FroyoWebViewClient.this.component, "ErrorOccurred", Integer.valueOf(errorCode), description, failingUrl);
            }
        });
    }
}
