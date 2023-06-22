package com.KIO4_TransportNet;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.telephony.TelephonyManager;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class KIO4_TransportNet extends AndroidNonvisibleComponent implements Component {
    public static final int VERSION = 1;
    private final Activity a;

    /* renamed from: a  reason: collision with other field name */
    private Context f10a;

    /* renamed from: a  reason: collision with other field name */
    private ConnectivityManager.NetworkCallback f11a;

    /* renamed from: a  reason: collision with other field name */
    private ConnectivityManager f12a;

    /* renamed from: a  reason: collision with other field name */
    private boolean f13a = false;
    public boolean chequear = false;
    public String salida = "";
    public String ssid = "-";

    public KIO4_TransportNet(ComponentContainer componentContainer) {
        super(componentContainer.$form());
        this.f10a = componentContainer.$context();
        this.a = componentContainer.$context();
    }

    public boolean CellularEnabled() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f10a.getSystemService("connectivity");
        try {
            Method declaredMethod = Class.forName(connectivityManager.getClass().getName()).getDeclaredMethod("getMobileDataEnabled", new Class[0]);
            declaredMethod.setAccessible(true);
            return ((Boolean) declaredMethod.invoke(connectivityManager, new Object[0])).booleanValue();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean CellularTransport() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f10a.getSystemService("connectivity");
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (networkCapabilities != null) {
            return networkCapabilities.hasTransport(0);
        }
        return false;
    }

    public void CheckTransport(String str) {
        EventDispatcher.dispatchEvent(this, "CheckTransport", str);
    }

    public int GetApi() {
        int i = -1;
        for (Field field : Build.VERSION_CODES.class.getFields()) {
            field.getName();
            try {
                i = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                i = -1;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                i = -1;
            } catch (NullPointerException e3) {
                e3.printStackTrace();
                i = -1;
            }
            int i2 = Build.VERSION.SDK_INT;
        }
        return i;
    }

    public String NombreDelOperador() {
        return ((TelephonyManager) this.f10a.getSystemService("phone")).getNetworkOperatorName();
    }

    public boolean StartCheckTransport() {
        this.f12a = (ConnectivityManager) this.f10a.getSystemService("connectivity");
        Network activeNetwork = this.f12a.getActiveNetwork();
        this.f12a.getNetworkCapabilities(activeNetwork);
        this.f12a.getLinkProperties(activeNetwork);
        this.f11a = new a(this);
        this.f12a.registerDefaultNetworkCallback(this.f11a);
        this.f13a = true;
        return this.f13a;
    }

    public boolean StopCheckTransport() {
        try {
            this.f12a.unregisterNetworkCallback(this.f11a);
        } catch (Exception e) {
        }
        this.f13a = false;
        return this.f13a;
    }

    public boolean WifiTransport() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.f10a.getSystemService("connectivity");
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        if (networkCapabilities != null) {
            return networkCapabilities.hasTransport(1);
        }
        return false;
    }
}
