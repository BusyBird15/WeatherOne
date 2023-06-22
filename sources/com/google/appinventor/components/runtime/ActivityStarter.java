package com.google.appinventor.components.runtime;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.AnimationUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.NougatUtil;
import com.google.appinventor.components.runtime.util.YailList;
import java.io.File;

@SimpleObject
@DesignerComponent(category = ComponentCategory.CONNECTIVITY, description = "A component that can launch an activity using the <code>StartActivity</code> method. \n<p>Activities that can be launched include:<ul> <li> Starting another App Inventor for Android app. \n To do so, first      find out the <em>class</em> of the other application by      downloading the source code and using a file explorer or unzip      utility to find a file named      \"youngandroidproject/project.properties\".  \n The first line of      the file will start with \"main=\" and be followed by the class      name; for example,      <code>main=com.gmail.Bitdiddle.Ben.HelloPurr.Screen1</code>.       (The first components indicate that it was created by      Ben.Bitdiddle@gmail.com.)  \n To make your      <code>ActivityStarter</code> launch this application, set the      following properties: <ul>\n      <li> <code>ActivityPackage</code> to the class name, dropping the           last component (for example,           <code>com.gmail.Bitdiddle.Ben.HelloPurr</code>)</li>\n      <li> <code>ActivityClass</code> to the entire class name (for           example,           <code>com.gmail.Bitdiddle.Ben.HelloPurr.Screen1</code>)</li>      </ul></li> \n<li> Starting the camera application by setting the following      properties:<ul> \n     <li> <code>Action: android.intent.action.MAIN</code> </li> \n     <li> <code>ActivityPackage: com.android.camera</code> </li> \n     <li> <code>ActivityClass: com.android.camera.Camera</code></li>\n      </ul></li>\n<li> Performing web search.  Assuming the term you want to search      for is \"vampire\" (feel free to substitute your own choice), \n     set the properties to:\n<ul><code>     <li>Action: android.intent.action.WEB_SEARCH</li>      <li>ExtraKey: query</li>      <li>ExtraValue: vampire</li>      <li>ActivityPackage: com.google.android.providers.enhancedgooglesearch</li>     <li>ActivityClass: com.google.android.providers.enhancedgooglesearch.Launcher</li>      </code></ul></li> \n<li> Opening a browser to a specified web page.  Assuming the page you      want to go to is \"www.facebook.com\" (feel free to substitute      your own choice), set the properties to:\n<ul><code>      <li>Action: android.intent.action.VIEW</li>      <li>DataUri: http://www.facebook.com</li> </code> </ul> </li> </ul></p>", designerHelpDescription = "A component that can launch an activity using the <code>StartActivity</code> method.<p>Activities that can be launched include: <ul> \n<li> starting other App Inventor for Android apps </li> \n<li> starting the camera application </li> \n<li> performing web search </li> \n<li> opening a browser to a specified web page</li> \n<li> opening the map application to a specified location</li></ul> \nYou can also launch activities that return text data.  See the documentation on using the Activity Starter for examples.</p>", iconName = "images/activityStarter.png", nonVisible = true, version = 6)
public class ActivityStarter extends AndroidNonvisibleComponent implements ActivityResultListener, Component, Deleteable {
    private static final String LOG_TAG = "ActivityStarter";
    private String action;
    private String activityClass;
    private String activityPackage;
    private final ComponentContainer container;
    private String dataType;
    private String dataUri;
    private String extraKey;
    private String extraValue;
    private YailList extras;
    private int requestCode;
    private String result = "";
    private Intent resultIntent;
    private String resultName;

    public ActivityStarter(ComponentContainer container2) {
        super(container2.$form());
        this.container = container2;
        Action("android.intent.action.MAIN");
        ActivityPackage("");
        ActivityClass("");
        DataUri("");
        DataType("");
        ExtraKey("");
        ExtraValue("");
        Extras(new YailList());
        ResultName("");
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Action() {
        return this.action;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void Action(String action2) {
        this.action = action2.trim();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the extra key that will be passed to the activity.\nDEPRECATED: New code should use Extras property instead.")
    public String ExtraKey() {
        return this.extraKey;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ExtraKey(String extraKey2) {
        this.extraKey = extraKey2.trim();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Returns the extra value that will be passed to the activity.\nDEPRECATED: New code should use Extras property instead.")
    public String ExtraValue() {
        return this.extraValue;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ExtraValue(String extraValue2) {
        this.extraValue = extraValue2.trim();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String ResultName() {
        return this.resultName;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ResultName(String resultName2) {
        this.resultName = resultName2.trim();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String Result() {
        return this.result;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String DataUri() {
        return this.dataUri;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void DataUri(String dataUri2) {
        this.dataUri = dataUri2.trim();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String DataType() {
        return this.dataType;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void DataType(String dataType2) {
        this.dataType = dataType2.trim();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String ActivityPackage() {
        return this.activityPackage;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ActivityPackage(String activityPackage2) {
        this.activityPackage = activityPackage2.trim();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public String ActivityClass() {
        return this.activityClass;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty
    public void ActivityClass(String activityClass2) {
        this.activityClass = activityClass2.trim();
    }

    @SimpleEvent(description = "Event raised after this ActivityStarter returns.")
    public void AfterActivity(String result2) {
        EventDispatcher.dispatchEvent(this, "AfterActivity", result2);
    }

    @SimpleEvent(description = "Event raised if this ActivityStarter returns because the activity was canceled.")
    public void ActivityCanceled() {
        EventDispatcher.dispatchEvent(this, "ActivityCanceled", new Object[0]);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.resultIntent.getType();
     */
    @com.google.appinventor.components.annotations.SimpleProperty(category = com.google.appinventor.components.annotations.PropertyCategory.BEHAVIOR)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String ResultType() {
        /*
            r2 = this;
            android.content.Intent r1 = r2.resultIntent
            if (r1 == 0) goto L_0x000d
            android.content.Intent r1 = r2.resultIntent
            java.lang.String r0 = r1.getType()
            if (r0 == 0) goto L_0x000d
        L_0x000c:
            return r0
        L_0x000d:
            java.lang.String r0 = ""
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.ActivityStarter.ResultType():java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.resultIntent.getDataString();
     */
    @com.google.appinventor.components.annotations.SimpleProperty(category = com.google.appinventor.components.annotations.PropertyCategory.BEHAVIOR)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String ResultUri() {
        /*
            r2 = this;
            android.content.Intent r1 = r2.resultIntent
            if (r1 == 0) goto L_0x000d
            android.content.Intent r1 = r2.resultIntent
            java.lang.String r0 = r1.getDataString()
            if (r0 == 0) goto L_0x000d
        L_0x000c:
            return r0
        L_0x000d:
            java.lang.String r0 = ""
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.appinventor.components.runtime.ActivityStarter.ResultUri():java.lang.String");
    }

    @SimpleProperty
    public void Extras(YailList pairs) {
        for (Object pair : pairs.toArray()) {
            boolean isYailList = pair instanceof YailList;
            boolean isPair = isYailList ? ((YailList) pair).size() == 2 : false;
            if (!isYailList || !isPair) {
                throw new YailRuntimeError("Argument to Extras should be a list of pairs", "ActivityStarter Error");
            }
        }
        this.extras = pairs;
    }

    @SimpleProperty
    public YailList Extras() {
        return this.extras;
    }

    @SimpleFunction(description = "Returns the name of the activity that corresponds to this ActivityStarter, or an empty string if no corresponding activity can be found.")
    public String ResolveActivity() {
        ResolveInfo resolveInfo = this.container.$context().getPackageManager().resolveActivity(buildActivityIntent(), 0);
        if (resolveInfo == null || resolveInfo.activityInfo == null) {
            return "";
        }
        return resolveInfo.activityInfo.name;
    }

    @SimpleFunction(description = "Start the activity corresponding to this ActivityStarter.")
    public void StartActivity() {
        this.resultIntent = null;
        this.result = "";
        Intent intent = buildActivityIntent();
        if (this.requestCode == 0) {
            this.requestCode = this.form.registerForActivityResult(this);
        }
        if (intent == null) {
            this.form.dispatchErrorOccurredEvent(this, "StartActivity", ErrorMessages.ERROR_ACTIVITY_STARTER_NO_ACTION_INFO, new Object[0]);
            return;
        }
        try {
            this.container.$context().startActivityForResult(intent, this.requestCode);
            AnimationUtil.ApplyOpenScreenAnimation(this.container.$context(), this.container.$form().OpenScreenAnimation());
        } catch (ActivityNotFoundException e) {
            this.form.dispatchErrorOccurredEvent(this, "StartActivity", ErrorMessages.ERROR_ACTIVITY_STARTER_NO_CORRESPONDING_ACTIVITY, new Object[0]);
        }
    }

    private Intent buildActivityIntent() {
        Uri uri = this.dataUri.length() != 0 ? Uri.parse(this.dataUri) : null;
        Intent intent = new Intent(this.action);
        if (uri != null && this.dataUri.toLowerCase().startsWith("file://")) {
            Log.d(LOG_TAG, "Using file://");
            File file = new File(uri.getPath());
            if (file.isFile()) {
                Log.d(LOG_TAG, "It's a file");
                uri = NougatUtil.getPackageUri(this.form, file);
                intent = new Intent(this.action);
                intent.setFlags(1);
                Log.d(LOG_TAG, "added permissions");
            }
        }
        if (TextUtils.isEmpty(Action())) {
            return null;
        }
        if (this.dataType.length() == 0) {
            intent.setData(uri);
        } else if (uri != null) {
            intent.setDataAndType(uri, this.dataType);
        } else {
            intent.setType(this.dataType);
        }
        if (this.activityPackage.length() != 0 || this.activityClass.length() != 0) {
            intent.setComponent(new ComponentName(this.activityPackage, this.activityClass));
        } else if (Action().equals("android.intent.action.MAIN")) {
            return null;
        }
        if (!(this.extraKey.length() == 0 || this.extraValue.length() == 0)) {
            Log.i(LOG_TAG, "Adding extra, key = " + this.extraKey + " value = " + this.extraValue);
            intent.putExtra(this.extraKey, this.extraValue);
        }
        for (Object extra : this.extras.toArray()) {
            YailList castExtra = (YailList) extra;
            String key = castExtra.getString(0);
            Object value = castExtra.getObject(1);
            Log.i(LOG_TAG, "Adding extra, key = " + key + " value = " + value);
            if (key.length() != 0) {
                if (value instanceof YailList) {
                    Log.i(LOG_TAG, "Adding extra list, key = " + key + " value = " + value);
                    intent.putExtra(key, ((YailList) value).toStringArray());
                } else {
                    String stringValue = castExtra.getString(1);
                    Log.i(LOG_TAG, "Adding extra string, key = " + key + " value = " + stringValue);
                    intent.putExtra(key, stringValue);
                }
            }
        }
        return intent;
    }

    public void resultReturned(int requestCode2, int resultCode, Intent data) {
        if (requestCode2 == this.requestCode) {
            Log.i(LOG_TAG, "resultReturned - resultCode = " + resultCode);
            if (resultCode == -1) {
                this.resultIntent = data;
                if (this.resultName.length() == 0 || this.resultIntent == null || !this.resultIntent.hasExtra(this.resultName)) {
                    this.result = "";
                } else {
                    this.result = this.resultIntent.getStringExtra(this.resultName);
                }
                AfterActivity(this.result);
            } else if (resultCode == 0) {
                ActivityCanceled();
            }
        }
    }

    @SimpleEvent(description = "The ActivityError event is no longer used. Please use the Screen.ErrorOccurred event instead.", userVisible = false)
    public void ActivityError(String message) {
    }

    public void onDelete() {
        this.form.unregisterForActivityResult(this);
    }
}
