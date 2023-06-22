package com.sunny.CustomWebView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesActivities;
import com.google.appinventor.components.annotations.androidmanifest.ActionElement;
import com.google.appinventor.components.annotations.androidmanifest.ActivityElement;
import com.google.appinventor.components.annotations.androidmanifest.CategoryElement;
import com.google.appinventor.components.annotations.androidmanifest.DataElement;
import com.google.appinventor.components.annotations.androidmanifest.IntentFilterElement;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.OnNewIntentListener;

@SimpleObject(external = true)
@UsesActivities(activities = {@ActivityElement(intentFilters = {@IntentFilterElement(actionElements = {@ActionElement(name = "android.intent.action.VIEW")}, categoryElements = {@CategoryElement(name = "android.intent.category.DEFAULT"), @CategoryElement(name = "android.intent.category.BROWSABLE")}, dataElements = {@DataElement(scheme = "http"), @DataElement(scheme = "https")}), @IntentFilterElement(actionElements = {@ActionElement(name = "android.intent.action.VIEW")}, categoryElements = {@CategoryElement(name = "android.intent.category.DEFAULT"), @CategoryElement(name = "android.intent.category.BROWSABLE")}, dataElements = {@DataElement(scheme = "http"), @DataElement(scheme = "https"), @DataElement(mimeType = "text/html"), @DataElement(mimeType = "text/plain"), @DataElement(mimeType = "application/xhtml+xml")})}, launchMode = "singleTask", name = ".Screen1")})
@DesignerComponent(androidMinSdk = 21, category = ComponentCategory.EXTENSION, description = "Helper class of CustomWebView extension to add app to browsers list<br> Developed by Sunny Gupta", helpUrl = "https://github.com/vknow360/CustomWebView", iconName = "https://res.cloudinary.com/andromedaviewflyvipul/image/upload/c_scale,h_20,w_20/v1571472765/ktvu4bapylsvnykoyhdm.png", nonVisible = true, version = 1, versionName = "1.1")
public class BrowserPromptHelper extends AndroidNonvisibleComponent implements OnNewIntentListener {
    public Activity activity;

    public BrowserPromptHelper(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.activity = componentContainer.$context();
        this.form.registerForOnNewIntent(this);
    }

    @SimpleFunction(description = "Returns the url which started the current activity")
    public String GetStartUrl() {
        return getUrl(this.activity.getIntent());
    }

    @SimpleEvent(description = "Event raised when app gets resumed and gives the url which started this activity/screen if there is any else empty string")
    public void OnResume(String str) {
        EventDispatcher.dispatchEvent(this, "OnResume", str);
    }

    public String getUrl(Intent intent) {
        Uri data = intent.getData();
        return (data == null || data.toString() == null) ? "" : data.toString();
    }

    public void onNewIntent(Intent intent) {
        OnResume(getUrl(intent));
    }
}
