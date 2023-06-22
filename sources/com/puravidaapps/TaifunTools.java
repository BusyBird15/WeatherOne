package com.puravidaapps;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.OnDestroyListener;
import com.google.appinventor.components.runtime.OnPauseListener;
import com.google.appinventor.components.runtime.OnResumeListener;
import com.google.appinventor.components.runtime.OnStopListener;
import com.google.appinventor.components.runtime.ReplForm;
import com.google.appinventor.components.runtime.util.Ev3Constants;
import gnu.expr.Declaration;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@SimpleObject(external = true)
@DesignerComponent(category = ComponentCategory.EXTENSION, description = "A collection of several tools, which do not need additional permissions. Version 21a as of 2021-01-06.", helpUrl = "https://puravidaapps.com/tools.php", iconName = "https://puravidaapps.com/images/taifun16.png", nonVisible = true, version = 21)
public class TaifunTools extends AndroidNonvisibleComponent implements Component, OnPauseListener, OnResumeListener, OnStopListener, OnDestroyListener {
    private static final String LOG_TAG = "TaifunTools";
    public static final int VERSION = 21;
    private static Context context;
    private final Activity activity;
    private ComponentContainer container;
    private File file;
    private final InputMethodManager imm;
    private boolean isRepl = false;
    private MediaScannerConnection msc;
    private boolean suppressWarnings;

    public TaifunTools(ComponentContainer container2) {
        super(container2.$form());
        if (this.form instanceof ReplForm) {
            this.isRepl = true;
        }
        this.form.registerForOnPause(this);
        this.form.registerForOnStop(this);
        this.form.registerForOnResume(this);
        this.form.registerForOnDestroy(this);
        this.container = container2;
        context = container2.$context();
        this.activity = container2.$context();
        this.imm = (InputMethodManager) context.getSystemService("input_method");
        Log.d(LOG_TAG, "TaifunTools Created");
    }

    @SimpleFunction(description = "Gallery Refresh for a specific filename.")
    public String GalleryRefresh(String fileName) {
        Log.i(LOG_TAG, "Refresh, filename: " + fileName);
        this.file = new File(fileName);
        MediaScannerConnection.scanFile(context, new String[]{this.file.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                Log.i(TaifunTools.LOG_TAG, "onScanCompleted, path: " + path + ", uri: " + uri);
            }
        });
        return fileName;
    }

    @SimpleFunction(description = "Returns a base64 encoded HMAC SHA1 hash. Precondition to use this block: Min Android Version of the app must be 8!")
    public String HmacSha1(String message, String key) {
        Log.d(LOG_TAG, "HmacSha1");
        return HmacDigest(message, key, "HmacSHA1");
    }

    @SimpleFunction(description = "Returns a base64 encoded HMAC SHA256 hash. Precondition to use this block: Min Android Version of the app must be 8!")
    public String HmacSha256(String message, String key) {
        Log.d(LOG_TAG, "HmacSha256");
        return HmacDigest(message, key, "HmacSha256");
    }

    private static String HmacDigest(String msg, String keyString, String algo) {
        if (Build.VERSION.SDK_INT < 8) {
            Log.w(LOG_TAG, "Sorry, Base64 encode is not available for API < 8");
            return null;
        }
        try {
            SecretKeySpec key = new SecretKeySpec(keyString.getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);
            return Base64.encodeToString(mac.doFinal(msg.getBytes("UTF-8")), 0);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @SimpleFunction(description = "Returns a list of available sensors")
    public Object SensorList() {
        List<Sensor> sensorList = ((SensorManager) this.activity.getSystemService("sensor")).getSensorList(-1);
        List<Map<String, String>> sensorData = new ArrayList<>();
        for (Sensor sensor : sensorList) {
            Map<String, String> data = new HashMap<>();
            data.put("name", sensor.getName());
            data.put("vendor", sensor.getVendor());
            sensorData.add(data);
        }
        return sensorData;
    }

    @SimpleFunction(description = "Hide the system user interface")
    public void HideSystemUI() {
        this.activity.getWindow().getDecorView().setSystemUiVisibility(5894);
    }

    @SimpleFunction(description = "Show the system user interface")
    public void ShowSystemUI() {
        this.activity.getWindow().getDecorView().setSystemUiVisibility(1792);
    }

    @SimpleFunction(description = "Returns the version code of the app")
    public int VersionCode() {
        Log.d(LOG_TAG, "VersionCode");
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    @SimpleEvent(description = "Event indicating that the state of the activity changed.Possible values are pause, stop, resume. See also the activity lifecycle here http://developer.android.com/reference/android/app/Activity.html#ActivityLifecycle")
    public void ActivityStateChanged(String state) {
        Log.i(LOG_TAG, "ActivityStateChanged: " + state);
        EventDispatcher.dispatchEvent(this, "ActivityStateChanged", state);
    }

    public void onResume() {
        ActivityStateChanged("resume");
    }

    public void onPause() {
        ActivityStateChanged("pause");
    }

    public void onStop() {
        ActivityStateChanged("stop");
    }

    public void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        ActivityStateChanged("destroy");
    }

    @SimpleFunction(description = "Show the keyboard")
    public void ShowKeyboard() {
        Log.d(LOG_TAG, "ShowKeyboard");
        this.imm.toggleSoftInput(2, 1);
    }

    @SimpleFunction(description = "Clear the cache of the current app. Returns true if clearing was successful, else false.")
    public boolean ClearCache() {
        Log.d(LOG_TAG, "ClearCache");
        try {
            File dir = context.getCacheDir();
            Log.d(LOG_TAG, "dir: " + dir);
            return deleteDir(dir);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                Log.d(LOG_TAG, "i: " + i + ", children: " + children[i]);
                if (!deleteDir(new File(dir, children[i]))) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir == null || !dir.isFile()) {
            return false;
        } else {
            return dir.delete();
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "whether Warnings should be suppressed")
    public boolean SuppressWarnings() {
        return this.suppressWarnings;
    }

    @DesignerProperty(defaultValue = "false", editorType = "boolean")
    @SimpleProperty
    public void SuppressWarnings(boolean suppressWarnings2) {
        this.suppressWarnings = suppressWarnings2;
    }

    @SimpleFunction(description = "Exclusive or for boolean: returns true only when inputs differ (one is true, the other is false).")
    public boolean Xor(boolean a, boolean b) {
        return a ^ b;
    }

    @SimpleFunction(description = "Exclusive or for hex strings, returns hex result. Strings must have the same length.")
    public String XorHex(String a, String b) {
        if (a.length() != b.length()) {
            if (!this.suppressWarnings) {
                Toast.makeText(context, "Strings must have the same length.", 0).show();
            }
            return "";
        } else if (!isHex(a) || !isHex(b)) {
            if (!this.suppressWarnings) {
                Toast.makeText(context, "Strings must be hex values.", 0).show();
            }
            return "";
        } else {
            char[] chars = new char[a.length()];
            for (int i = 0; i < chars.length; i++) {
                chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
            }
            return new String(chars);
        }
    }

    private static int fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return (c - 'A') + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return (c - 'a') + 10;
        }
        throw new IllegalArgumentException();
    }

    private char toHex(int nybble) {
        if (nybble >= 0 && nybble <= 15) {
            return "0123456789ABCDEF".charAt(nybble);
        }
        throw new IllegalArgumentException();
    }

    private boolean isHex(String s) {
        return s.matches("-?[0-9a-fA-F]+");
    }

    @DesignerProperty(defaultValue = "&H00FFFFFF", editorType = "color")
    @SimpleProperty(description = "Set status bar color. This will work starting from API Level 21 (Android Lollipop")
    public void StatusBarColor(int argb) {
        if (Build.VERSION.SDK_INT < 21) {
            Log.w(LOG_TAG, "Sorry, setStatusBarColor is not available for API Level < 21");
            return;
        }
        Window window = this.activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(Declaration.PUBLIC_ACCESS);
        window.setStatusBarColor(argb);
    }

    @DesignerProperty(defaultValue = "&H00FFFFFF", editorType = "color")
    @SimpleProperty(description = "Set navigation bar color. This will work starting from API Level 21 (Android Lollipop")
    public void NavigationBarColor(int argb) {
        if (Build.VERSION.SDK_INT < 21) {
            Log.w(LOG_TAG, "Sorry, NavigationBarColor is not available for API Level < 21");
            return;
        }
        Window window = this.activity.getWindow();
        window.addFlags(Integer.MIN_VALUE);
        window.clearFlags(Declaration.PACKAGE_ACCESS);
        window.setNavigationBarColor(argb);
    }

    @SimpleFunction(description = "Hide content, i.e. enable content to move behind status and navigation bar.")
    public void HideContent() {
        this.activity.getWindow().getDecorView().setSystemUiVisibility(1792);
    }

    @DesignerProperty(defaultValue = "&H00FFFFFF", editorType = "color")
    @SimpleProperty(description = "Set title color.")
    public void TitleColor(int argb) {
        this.activity.getWindow().setTitleColor(argb);
    }

    @SimpleProperty(description = "Returns the language code of the current Locale. A Locale object represents a specific geographical, political, or cultural region.")
    public String Language() {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    @SimpleProperty(description = "Returns the country/region code of the current Locale, which should either be the empty string, an uppercase ISO 3166 2-letter code, or a UN M.49 3-digit code. A Locale object represents a specific geographical, political, or cultural region.")
    public String Country() {
        return context.getResources().getConfiguration().locale.getCountry();
    }

    @SimpleFunction(description = "Base64 encode a string. Precondition to use this block: Min Android Version of the app must be 8!")
    public String Base64Encode(String string) {
        Log.d(LOG_TAG, "Base64Encode");
        try {
            return Base64.encodeToString(string.getBytes("UTF-8"), 2);
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @SimpleFunction(description = "returns true, if string matches a regular expression, else false")
    public boolean Matches(String string, String regex) {
        Log.d(LOG_TAG, "Matches");
        if (string.matches(regex)) {
            return true;
        }
        return false;
    }

    @SimpleFunction(description = "As long as this window is visible to the user, keep the device's screen turned on and bright.")
    public void KeepScreenOn() {
        Log.d(LOG_TAG, "KeepScreenOn");
        this.activity.getWindow().addFlags(128);
    }

    @SimpleFunction(description = "Returns the version name of the app")
    public String VersionName() {
        Log.d(LOG_TAG, "VersionName");
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    @SimpleFunction(description = "Returns true, if it is a valid email address, else false.")
    public boolean EmailAddressIsValid(String emailAddress) {
        Log.d(LOG_TAG, "EmailAddressIsValid");
        return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    @SimpleFunction(description = "Returns the path to the assets. This method is useful, if you want to access files from the webviewer component stored in the assets of the app. The method will return 'file:///android_asset/' in case the app is running after building the app as apk file. For the companion app there can be returned 3 different values: file:///storage/emulated/0/Android/data/<packageName>/files/assets/ for devices >= Android 10, file:///storage/emulated/0/AppInventor/assets/' for the App Inventor companion app and < Android 10, file:///storage/emulated/0/Makeroid/assets/' for the Kodular (formerly Makeroid) companion app and < Android 10.")
    public String PathToAssets() {
        Log.d(LOG_TAG, "PathToAssets");
        if (!this.isRepl) {
            return Form.ASSETS_PREFIX;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            return "file://" + ApplicationSpecificDirectory() + "/assets/";
        }
        if (context.getPackageName().contains("makeroid")) {
            return "file:///storage/emulated/0/Makeroid/assets/";
        }
        return "file:///storage/emulated/0/AppInventor/assets/";
    }

    @SimpleFunction(description = "Returns the density of the device.")
    public double Density() {
        Log.d(LOG_TAG, "Density");
        return (double) this.activity.getResources().getDisplayMetrics().density;
    }

    @SimpleFunction(description = "Returns the package name of the app.")
    public String PackageName() {
        Log.d(LOG_TAG, "PackageName: " + context.getPackageName());
        return context.getPackageName();
    }

    @SimpleFunction(description = "Returns true, if app is running in development mode (using the companion app), else returns false.")
    public boolean IsDevelopment() {
        Log.d(LOG_TAG, "IsDevelopment: " + this.isRepl);
        return this.isRepl;
    }

    @SimpleFunction(description = "Returns a Sha256 hash of a given string.")
    public String Sha256(String string) {
        Log.d(LOG_TAG, "Sha256");
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(string.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (byte b : hash) {
                String hex = Integer.toHexString(b & Ev3Constants.Opcode.TST);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    @SimpleFunction(description = "Remove the KeepScreenOn flag.")
    public void DontKeepScreenOn() {
        Log.d(LOG_TAG, "KeepScreenOn");
        this.activity.getWindow().clearFlags(128);
    }

    @SimpleFunction(description = "API Level.")
    public int ApiLevel() {
        Log.d(LOG_TAG, "ApiLEvel");
        return Build.VERSION.SDK_INT;
    }

    @SimpleFunction(description = "Returns the application specific directory you can use to write files without having WRITE_EXTERNAL_STORAGE permission.")
    public String ApplicationSpecificDirectory() {
        Log.d(LOG_TAG, "ApplicationSpecificDirectory");
        return context.getExternalFilesDir((String) null).toString();
    }

    @SimpleFunction(description = "Clear the app data of the current app. Returns true if clearing was successful, else false.")
    public boolean ClearAppData() {
        Log.d(LOG_TAG, "ClearAppData");
        try {
            if (19 <= Build.VERSION.SDK_INT) {
                Context context2 = context;
                Context context3 = context;
                ((ActivityManager) context2.getSystemService("activity")).clearApplicationUserData();
            } else {
                Runtime.getRuntime().exec("pm clear " + PackageName());
            }
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
