package defpackage;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import com.KIO4_TransportNet.KIO4_TransportNet;
import com.google.appinventor.components.runtime.Component;

/* renamed from: a  reason: default package */
public final class a extends ConnectivityManager.NetworkCallback {
    private /* synthetic */ KIO4_TransportNet a;

    public a(KIO4_TransportNet kIO4_TransportNet) {
        this.a = kIO4_TransportNet;
    }

    public final void onAvailable(Network network) {
        this.a.salida = network.toString();
        this.a.a.runOnUiThread(new b(this.a));
    }

    public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        String str = Component.TYPEFACE_DEFAULT;
        this.a.salida = networkCapabilities.toString();
        if (this.a.salida.indexOf("SignalStrength") != -1) {
            str = this.a.salida.substring(this.a.salida.indexOf("SignalStrength") + 15, this.a.salida.indexOf("SignalStrength") + 19);
        }
        if (this.a.salida.indexOf("SSID") != -1) {
            this.a.ssid = this.a.salida.substring(this.a.salida.indexOf("SSID") + 6);
            this.a.ssid = this.a.ssid.substring(0, this.a.ssid.length() - 1);
        }
        if (this.a.salida.indexOf("WIFI") != -1) {
            this.a.salida = "WiFi," + str + "," + this.a.ssid;
        }
        if (this.a.salida.indexOf("CELLULAR") != -1) {
            String NombreDelOperador = this.a.NombreDelOperador();
            this.a.salida = "Cellular,0,".concat(String.valueOf(NombreDelOperador));
        }
        this.a.a.runOnUiThread(new b(this.a));
    }

    public final void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
    }

    public final void onLost(Network network) {
        this.a.salida = network.toString();
        this.a.salida = "-,0,-";
        this.a.a.runOnUiThread(new b(this.a));
    }
}
