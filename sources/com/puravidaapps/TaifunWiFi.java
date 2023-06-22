package com.puravidaapps;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.RouteInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
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
import com.google.appinventor.components.runtime.OnDestroyListener;
import com.google.appinventor.components.runtime.ReplForm;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@DesignerComponent(category = ComponentCategory.EXTENSION, description = "WiFi Manager Extension. Version 15 as of 2021-11-12.", helpUrl = "https://puravidaapps.com/wifi.php", iconName = "https://puravidaapps.com/images/taifun16.png", nonVisible = true, version = 14)
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.ACCESS_WIFI_STATE, android.permission.CHANGE_WIFI_STATE, android.permission.ACCESS_NETWORK_STATE")
public class TaifunWiFi extends AndroidNonvisibleComponent implements Component, OnDestroyListener {
    private static final String LOG_TAG = "TaifunWiFi";
    public static final int VERSION = 14;
    /* access modifiers changed from: private */
    public static WifiManager wm;
    private final Activity activity;
    private ComponentContainer container;
    /* access modifiers changed from: private */
    public Context context;
    private boolean isRepl = false;
    private boolean suppressSuccessMessage;
    private boolean suppressWarnings;
    private WiFiReceiverScan wiFiReceiverScan;

    public TaifunWiFi(ComponentContainer container2) {
        super(container2.$form());
        if (this.form instanceof ReplForm) {
            this.isRepl = true;
        }
        this.container = container2;
        this.context = container2.$context();
        this.activity = container2.$context();
        wm = (WifiManager) this.context.getSystemService("wifi");
        Log.d(LOG_TAG, "TaifunWiFi Created");
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "whether Success Message should be suppressed. Used in Enable and Disable method.")
    public boolean SuppressSuccessMessage() {
        return this.suppressSuccessMessage;
    }

    @DesignerProperty(defaultValue = "false", editorType = "boolean")
    @SimpleProperty
    public void SuppressSuccessMessage(boolean suppressSuccessMessage2) {
        this.suppressSuccessMessage = suppressSuccessMessage2;
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

    @SimpleFunction(description = "Return the local IP Address. Returns wifi ip if its enabled else the cellular one.")
    public String LocalIP() {
        if (wm.isWifiEnabled()) {
            int ipAddress = wm.getConnectionInfo().getIpAddress();
            return String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(ipAddress & 255), Integer.valueOf((ipAddress >> 8) & 255), Integer.valueOf((ipAddress >> 16) & 255), Integer.valueOf((ipAddress >> 24) & 255)});
        }
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
                while (true) {
                    if (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        Log.i(LOG_TAG, "inetAddress.getHostAddress(): " + inetAddress.getHostAddress());
                        if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                            Log.i(LOG_TAG, "return inetAddress.getHostAddress(): " + inetAddress.getHostAddress());
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    @SimpleFunction(description = "Get current WiFi state: true or false.")
    public boolean IsEnabled() {
        WifiInfo wi = wm.getConnectionInfo();
        try {
            if (!wm.isWifiEnabled() || wi.getSSID() == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SimpleFunction(description = "Enable WiFi. You can hide the success message after setting the suppressSuccessMessage property to false.")
    public void Enable() {
        if (this.isRepl) {
            Log.w(LOG_TAG, "You have to build the app to be able to use this method!");
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, "You have to build the app to be able to use this method!", 0).show();
                return;
            }
            return;
        }
        wm.setWifiEnabled(true);
        if (!this.suppressSuccessMessage) {
            Toast.makeText(this.context, "WiFi enabled.", 0).show();
        }
    }

    @SimpleFunction(description = "Disable WiFi. You can hide the success message after setting the suppressSuccessMessage property to false.")
    public void Disable() {
        if (this.isRepl) {
            Log.w(LOG_TAG, "You have to build the app to be able to use this method!");
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, "You have to build the app to be able to use this method!", 0).show();
                return;
            }
            return;
        }
        wm.setWifiEnabled(false);
        if (!this.suppressSuccessMessage) {
            Toast.makeText(this.context, "WiFi disabled.", 1).show();
        }
    }

    @SimpleFunction(description = "Get current WiFi SSID (Service Set Identifier).")
    public String SSID() {
        WifiInfo wi;
        String ssid = "";
        if (((ConnectivityManager) this.context.getSystemService("connectivity")).getNetworkInfo(1).isConnected() && (wi = wm.getConnectionInfo()) != null && !TextUtils.isEmpty(wi.getSSID())) {
            ssid = wi.getSSID().replace("\"", "");
        }
        if (!wm.isWifiEnabled() && !this.suppressWarnings) {
            Toast.makeText(this.context, "WiFi is disabled, can't get current SSID.", 0).show();
        }
        Log.d(LOG_TAG, "SSID: " + ssid);
        return ssid;
    }

    @SimpleFunction(description = "Connect to a SSID (Service Set Identifier).")
    public void ConnectSSID(final String ssid, final String password) {
        Log.d(LOG_TAG, "ConnectSSID");
        if (TextUtils.isEmpty(ssid)) {
            Log.d(LOG_TAG, "ssid is empty");
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, "Can't connect to an empty SSID", 0).show();
            }
        } else if (!wm.isWifiEnabled()) {
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, "WiFi is disabled, can't connect to " + ssid, 0).show();
            }
        } else if (!this.isRepl) {
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    TaifunWiFi.this.AsyncConnectSSID(ssid, password);
                }
            });
        } else if (!this.suppressWarnings) {
            Toast.makeText(this.context, "You have to build the app to be able to use this method!", 0).show();
        }
    }

    /* access modifiers changed from: private */
    public void AsyncConnectSSID(String ssid, String password) {
        Log.d(LOG_TAG, "AsyncConnectSSID");
        WifiConfiguration wc = new WifiConfiguration();
        wc.SSID = String.format("\"%s\"", new Object[]{ssid});
        if (password.isEmpty()) {
            Log.v(LOG_TAG, "open network");
            wc.allowedKeyManagement.set(0);
            wc.allowedProtocols.set(1);
            wc.allowedProtocols.set(0);
            wc.allowedAuthAlgorithms.clear();
            wc.allowedPairwiseCiphers.set(2);
            wc.allowedPairwiseCiphers.set(1);
            wc.allowedGroupCiphers.set(0);
            wc.allowedGroupCiphers.set(1);
            wc.allowedGroupCiphers.set(3);
            wc.allowedGroupCiphers.set(2);
        } else {
            Log.v(LOG_TAG, "WPA");
            wc.allowedProtocols.set(1);
            wc.allowedProtocols.set(0);
            wc.allowedKeyManagement.set(1);
            wc.allowedPairwiseCiphers.set(2);
            wc.allowedPairwiseCiphers.set(1);
            wc.allowedGroupCiphers.set(0);
            wc.allowedGroupCiphers.set(1);
            wc.allowedGroupCiphers.set(3);
            wc.allowedGroupCiphers.set(2);
            wc.preSharedKey = String.format("\"%s\"", new Object[]{password});
        }
        boolean found = false;
        int netId = 0;
        Iterator<WifiConfiguration> it = wm.getConfiguredNetworks().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            WifiConfiguration i = it.next();
            if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                found = true;
                wm.disconnect();
                netId = i.networkId;
                Log.d(LOG_TAG, "ssid: " + ssid + " found in configured SSIDs, netId: " + netId);
                wm.enableNetwork(netId, true);
                wm.reconnect();
                break;
            }
        }
        if (!found) {
            netId = wm.addNetwork(wc);
            Log.d(LOG_TAG, "remember ssid: " + ssid + " in configured SSIDs, netId: " + netId);
            wm.disconnect();
            wm.enableNetwork(netId, true);
            wm.reconnect();
        }
        if (netId == -1) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    TaifunWiFi.this.AfterWifiNegotiation(false);
                }
            });
            return;
        }
        final boolean successful = checkWifiNegotiation(netId);
        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                TaifunWiFi.this.AfterWifiNegotiation(successful);
            }
        });
    }

    @SimpleFunction(description = "Get a list of configured SSIDs (Service Set Identifiers). WiFi must be enabled for this.")
    public Object ConfiguredSSIDs() {
        Log.d(LOG_TAG, "ConfiguredSSIDs");
        try {
            List<WifiConfiguration> networks = wm.getConfiguredNetworks();
            List<String> ssids = new ArrayList<>();
            if (networks == null) {
                return ssids;
            }
            for (WifiConfiguration result : networks) {
                ssids.add(result.SSID.replace("\"", ""));
            }
            return ssids;
        } catch (Exception e) {
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, e.getMessage(), 0).show();
            }
            e.printStackTrace();
            Log.e(LOG_TAG, e.getMessage(), e);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0037 A[Catch:{ Exception -> 0x0087 }] */
    @com.google.appinventor.components.annotations.SimpleFunction(description = "Get MAC address")
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String MacAddress() {
        /*
            r14 = this;
            r13 = 0
            java.lang.String r9 = "TaifunWiFi"
            java.lang.String r10 = "MacAddress"
            android.util.Log.d(r9, r10)
            java.lang.String r7 = "02:00:00:00:00:00"
            android.net.wifi.WifiManager r9 = wm
            android.net.wifi.WifiInfo r8 = r9.getConnectionInfo()
            java.lang.String r7 = r8.getMacAddress()
            java.lang.String r9 = "02:00:00:00:00:00"
            boolean r9 = r7.equals(r9)
            if (r9 == 0) goto L_0x0051
            java.lang.String r9 = "TaifunWiFi"
            java.lang.String r10 = "MacAddress workaround"
            android.util.Log.d(r9, r10)
            java.lang.String r3 = "wlan0"
            java.util.Enumeration r9 = java.net.NetworkInterface.getNetworkInterfaces()     // Catch:{ Exception -> 0x0087 }
            java.util.ArrayList r4 = java.util.Collections.list(r9)     // Catch:{ Exception -> 0x0087 }
            java.util.Iterator r9 = r4.iterator()     // Catch:{ Exception -> 0x0087 }
        L_0x0031:
            boolean r10 = r9.hasNext()     // Catch:{ Exception -> 0x0087 }
            if (r10 == 0) goto L_0x00a2
            java.lang.Object r5 = r9.next()     // Catch:{ Exception -> 0x0087 }
            java.net.NetworkInterface r5 = (java.net.NetworkInterface) r5     // Catch:{ Exception -> 0x0087 }
            if (r3 == 0) goto L_0x0049
            java.lang.String r10 = r5.getName()     // Catch:{ Exception -> 0x0087 }
            boolean r10 = r10.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x0087 }
            if (r10 == 0) goto L_0x0031
        L_0x0049:
            byte[] r6 = r5.getHardwareAddress()     // Catch:{ Exception -> 0x0087 }
            if (r6 != 0) goto L_0x0052
            java.lang.String r7 = "02:00:00:00:00:00"
        L_0x0051:
            return r7
        L_0x0052:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0087 }
            r0.<init>()     // Catch:{ Exception -> 0x0087 }
            r2 = 0
        L_0x0058:
            int r9 = r6.length     // Catch:{ Exception -> 0x0087 }
            if (r2 >= r9) goto L_0x0073
            java.lang.String r9 = "%02X:"
            r10 = 1
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ Exception -> 0x0087 }
            r11 = 0
            byte r12 = r6[r2]     // Catch:{ Exception -> 0x0087 }
            java.lang.Byte r12 = java.lang.Byte.valueOf(r12)     // Catch:{ Exception -> 0x0087 }
            r10[r11] = r12     // Catch:{ Exception -> 0x0087 }
            java.lang.String r9 = java.lang.String.format(r9, r10)     // Catch:{ Exception -> 0x0087 }
            r0.append(r9)     // Catch:{ Exception -> 0x0087 }
            int r2 = r2 + 1
            goto L_0x0058
        L_0x0073:
            int r9 = r0.length()     // Catch:{ Exception -> 0x0087 }
            if (r9 <= 0) goto L_0x0082
            int r9 = r0.length()     // Catch:{ Exception -> 0x0087 }
            int r9 = r9 + -1
            r0.deleteCharAt(r9)     // Catch:{ Exception -> 0x0087 }
        L_0x0082:
            java.lang.String r7 = r0.toString()     // Catch:{ Exception -> 0x0087 }
            goto L_0x0051
        L_0x0087:
            r1 = move-exception
            java.lang.String r9 = "TaifunWiFi"
            java.lang.String r10 = r1.getMessage()
            android.util.Log.e(r9, r10, r1)
            boolean r9 = r14.suppressWarnings
            if (r9 != 0) goto L_0x00a2
            android.content.Context r9 = r14.context
            java.lang.String r10 = r1.getMessage()
            android.widget.Toast r9 = android.widget.Toast.makeText(r9, r10, r13)
            r9.show()
        L_0x00a2:
            java.lang.String r7 = "02:00:00:00:00:00"
            goto L_0x0051
        */
        throw new UnsupportedOperationException("Method not decompiled: com.puravidaapps.TaifunWiFi.MacAddress():java.lang.String");
    }

    @SimpleFunction(description = "Get current WiFi BSSID (the MAC address of the access point).")
    public String BSSID() {
        WifiInfo wi;
        String bssid = "";
        if (((ConnectivityManager) this.context.getSystemService("connectivity")).getNetworkInfo(1).isConnected() && (wi = wm.getConnectionInfo()) != null && !TextUtils.isEmpty(wi.getBSSID())) {
            bssid = wi.getBSSID();
        }
        if (!wm.isWifiEnabled() && !this.suppressWarnings) {
            Toast.makeText(this.context, "WiFi is disabled, can't get current BSSID.", 0).show();
        }
        Log.d(LOG_TAG, "SSID: " + bssid);
        return bssid;
    }

    @SimpleFunction(description = "Get signal strength (RSSI) in a range between 0 and 100.")
    public int SignalStrength() {
        Log.d(LOG_TAG, "SignalStrength");
        WifiInfo info = ((WifiManager) this.context.getSystemService("wifi")).getConnectionInfo();
        int rssi = info.getRssi();
        if (Build.VERSION.SDK_INT >= 14) {
            return WifiManager.calculateSignalLevel(info.getRssi(), 101);
        }
        if (rssi <= -100) {
            return 0;
        }
        if (rssi >= -55) {
            return 100;
        }
        return (int) ((((float) (rssi - -100)) * ((float) 100)) / ((float) 45));
    }

    @SimpleFunction(description = "Get current connection info.")
    public String ConnectionInfo() {
        WifiInfo wi;
        Log.d(LOG_TAG, "ConnectionInfo");
        NetworkInfo networkInfo = ((ConnectivityManager) this.context.getSystemService("connectivity")).getNetworkInfo(1);
        if (!wm.isWifiEnabled() && !this.suppressWarnings) {
            Toast.makeText(this.context, "WiFi is disabled, can't get current connection info.", 0).show();
        }
        if (!networkInfo.isConnected() || (wi = wm.getConnectionInfo()) == null) {
            return "";
        }
        return wi.toString();
    }

    @SimpleFunction(description = "Check, if 5 GHz Band is supported.")
    public boolean Is5GHzBandSupported() {
        Log.d(LOG_TAG, "Is5GHzBandSupported");
        if (wm.isWifiEnabled()) {
            return wm.is5GHzBandSupported();
        }
        if (this.suppressWarnings) {
            return false;
        }
        Toast.makeText(this.context, "WiFi is disabled, can't get check if 5 Ghz band is supported.", 0).show();
        return false;
    }

    @SimpleFunction(description = "Return the IP Address of access point.")
    public String AccessPointIP() {
        try {
            return InetAddress.getByAddress(convert2Bytes(wm.getDhcpInfo().serverAddress)).getHostAddress();
        } catch (UnknownHostException e) {
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, e.getMessage(), 0).show();
            }
            return "";
        }
    }

    private static byte[] convert2Bytes(int hostAddress) {
        return new byte[]{(byte) (hostAddress & 255), (byte) ((hostAddress >> 8) & 255), (byte) ((hostAddress >> 16) & 255), (byte) ((hostAddress >> 24) & 255)};
    }

    @SimpleFunction(description = "Disconnect.")
    public void Disconnect() {
        wm.disconnect();
    }

    @SimpleFunction(description = "Return a list of DNS servers (primary and secondary) of the current network.")
    public Object DnsServers() {
        Log.d(LOG_TAG, "DnsServers");
        String[] dnsArray = new DnsServersDetector().getServers();
        List<String> dnsList = new ArrayList<>();
        for (String dns : dnsArray) {
            dnsList.add(dns.toString());
        }
        return dnsList;
    }

    public class DnsServersDetector {
        private static final String METHOD_EXEC_PROP_DELIM = "]: [";
        private static final String TAG = "DnsServersDetector";
        private final String[] FACTORY_DNS_SERVERS = {"0.0.0.0", "0.0.0.0"};

        public DnsServersDetector() {
        }

        public String[] getServers() {
            String[] result = getServersMethodSystemProperties();
            Log.d(TaifunWiFi.LOG_TAG, "DNS, method1");
            if (result == null || result.length <= 0) {
                String[] result2 = getServersMethodConnectivityManager();
                Log.d(TaifunWiFi.LOG_TAG, "DNS, method2");
                if (result2 == null || result2.length <= 0) {
                    String[] result3 = getServersMethodExec();
                    Log.d(TaifunWiFi.LOG_TAG, "DNS, method3");
                    if (result3 == null || result3.length <= 0) {
                        Log.d(TaifunWiFi.LOG_TAG, "DNS, return default, " + this.FACTORY_DNS_SERVERS.toString());
                        return this.FACTORY_DNS_SERVERS;
                    }
                    Log.d(TaifunWiFi.LOG_TAG, "result: " + result3.toString());
                    return result3;
                }
                Log.d(TaifunWiFi.LOG_TAG, "result: " + result2.toString());
                return result2;
            }
            Log.d(TaifunWiFi.LOG_TAG, "result: " + result.toString());
            return result;
        }

        private String[] getServersMethodConnectivityManager() {
            if (Build.VERSION.SDK_INT >= 21) {
                try {
                    ArrayList<String> priorityServersArrayList = new ArrayList<>();
                    ArrayList<String> serversArrayList = new ArrayList<>();
                    ConnectivityManager connectivityManager = (ConnectivityManager) TaifunWiFi.this.context.getSystemService("connectivity");
                    if (connectivityManager != null) {
                        for (Network network : connectivityManager.getAllNetworks()) {
                            if (connectivityManager.getNetworkInfo(network).isConnected()) {
                                LinkProperties linkProperties = connectivityManager.getLinkProperties(network);
                                List<InetAddress> dnsServersList = linkProperties.getDnsServers();
                                if (linkPropertiesHasDefaultRoute(linkProperties)) {
                                    for (InetAddress element : dnsServersList) {
                                        priorityServersArrayList.add(element.getHostAddress());
                                    }
                                } else {
                                    for (InetAddress element2 : dnsServersList) {
                                        serversArrayList.add(element2.getHostAddress());
                                    }
                                }
                            }
                        }
                    }
                    if (priorityServersArrayList.isEmpty()) {
                        priorityServersArrayList.addAll(serversArrayList);
                    }
                    if (priorityServersArrayList.size() > 0) {
                        return (String[]) priorityServersArrayList.toArray(new String[0]);
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "Exception detecting DNS servers using ConnectivityManager method", ex);
                }
            }
            return null;
        }

        private String[] getServersMethodSystemProperties() {
            if (Build.VERSION.SDK_INT < 26) {
                ArrayList<String> serversArrayList = new ArrayList<>();
                try {
                    Method method = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
                    String[] netdns = {"net.dns1", "net.dns2", "net.dns3", "net.dns4"};
                    for (int i = 0; i < netdns.length; i++) {
                        String v = (String) method.invoke((Object) null, new Object[]{netdns[i]});
                        if (v != null && ((v.matches("^\\d+(\\.\\d+){3}$") || v.matches("^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$")) && !serversArrayList.contains(v))) {
                            serversArrayList.add(v);
                        }
                    }
                    if (serversArrayList.size() > 0) {
                        return (String[]) serversArrayList.toArray(new String[0]);
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "Exception detecting DNS servers using SystemProperties method", ex);
                }
            }
            return null;
        }

        private String[] getServersMethodExec() {
            if (Build.VERSION.SDK_INT >= 16) {
                try {
                    Set<String> serversSet = methodExecParseProps(new LineNumberReader(new InputStreamReader(Runtime.getRuntime().exec("getprop").getInputStream())));
                    if (serversSet != null && serversSet.size() > 0) {
                        return (String[]) serversSet.toArray(new String[0]);
                    }
                } catch (Exception ex) {
                    Log.d(TAG, "Exception in getServersMethodExec", ex);
                }
            }
            return null;
        }

        private Set<String> methodExecParseProps(BufferedReader lineNumberReader) throws Exception {
            InetAddress ip;
            String value;
            Set<String> serversSet = new HashSet<>(10);
            while (true) {
                String line = lineNumberReader.readLine();
                if (line == null) {
                    return serversSet;
                }
                int split = line.indexOf(METHOD_EXEC_PROP_DELIM);
                if (split != -1) {
                    String property = line.substring(1, split);
                    int valueStart = split + METHOD_EXEC_PROP_DELIM.length();
                    int valueEnd = line.length() - 1;
                    if (valueEnd < valueStart) {
                        Log.d(TAG, "Malformed property detected: \"" + line + '\"');
                    } else {
                        String value2 = line.substring(valueStart, valueEnd);
                        if (!value2.isEmpty() && !((!property.endsWith(".dns") && !property.endsWith(".dns1") && !property.endsWith(".dns2") && !property.endsWith(".dns3") && !property.endsWith(".dns4")) || (ip = InetAddress.getByName(value2)) == null || (value = ip.getHostAddress()) == null || value.length() == 0)) {
                            serversSet.add(value);
                        }
                    }
                }
            }
        }

        @TargetApi(21)
        private boolean linkPropertiesHasDefaultRoute(LinkProperties linkProperties) {
            for (RouteInfo route : linkProperties.getRoutes()) {
                if (route.isDefaultRoute()) {
                    return true;
                }
            }
            return false;
        }
    }

    @SimpleFunction(description = "Get a list of available SSIDs (Service Set Identifiers). WiFi must be enabled for this.")
    public void AvailableSSIDs() {
        Log.d(LOG_TAG, "AvailableSSIDs called");
        if (!wm.isWifiEnabled()) {
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, "WiFi is disabled, can't get list of available SSIDs.", 0).show();
            }
            Log.d(LOG_TAG, "WiFi is disabled, exit.");
        } else if (this.isRepl) {
            Log.w(LOG_TAG, "You have to build the app to be able to use this method!");
            if (!this.suppressWarnings) {
                Toast.makeText(this.context, "You have to build the app to be able to use this method!", 0).show();
            }
        } else {
            this.wiFiReceiverScan = new WiFiReceiverScan();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
            this.context.registerReceiver(this.wiFiReceiverScan, intentFilter);
            wm.startScan();
        }
    }

    @SimpleEvent(description = "indicating that Available SSIDs (Service Set Identifiers) have been scanned.")
    public void GotAvailableSSIDs(Object availableSSIDs, String bestSSID, Object correspondingRSSIs, Object correspondingBSSIs) {
        Log.d(LOG_TAG, "GotAvailableSSIDs");
        EventDispatcher.dispatchEvent(this, "GotAvailableSSIDs", availableSSIDs, bestSSID, correspondingRSSIs, correspondingBSSIs);
        unregisterReceiverScan();
    }

    private class WiFiReceiverScan extends BroadcastReceiver {
        private WiFiReceiverScan() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.d(TaifunWiFi.LOG_TAG, "onReceive, API version: " + Build.VERSION.SDK_INT);
            List<ScanResult> scanresult = TaifunWiFi.wm.getScanResults();
            List<String> ssids = new ArrayList<>();
            List<String> rssis = new ArrayList<>();
            List<String> bssis = new ArrayList<>();
            ScanResult bestResult = null;
            String bestResultSSID = "";
            Log.d(TaifunWiFi.LOG_TAG, "intent: " + intent + ", size: " + scanresult.size() + ", enabled: " + TaifunWiFi.wm.isWifiEnabled());
            for (ScanResult result : scanresult) {
                String ssid = result.SSID.replace("\"", "");
                Log.d(TaifunWiFi.LOG_TAG, ssid);
                ssids.add(ssid);
                rssis.add(String.valueOf(result.level));
                bssis.add(String.valueOf(result.BSSID));
                if (bestResult == null || WifiManager.compareSignalLevel(bestResult.level, result.level) < 0) {
                    bestResult = result;
                    bestResultSSID = bestResult.SSID.replace("\"", "");
                }
            }
            TaifunWiFi.this.GotAvailableSSIDs(ssids, bestResultSSID, rssis, bssis);
        }
    }

    private void unregisterReceiverScan() {
        if (this.wiFiReceiverScan != null) {
            Log.d(LOG_TAG, "unregisterReceiver");
            this.context.unregisterReceiver(this.wiFiReceiverScan);
            this.wiFiReceiverScan = null;
        }
    }

    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        unregisterReceiverScan();
    }

    private static boolean checkWifiNegotiation(int netId) {
        Log.d(LOG_TAG, "checkWifiNegotiation, netId: " + netId);
        boolean startedHandshake = false;
        boolean successful = false;
        int i = 0;
        while (true) {
            if (i >= 30) {
                break;
            }
            Log.d(LOG_TAG, "checkWifiNegotiation, i: " + i);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, e.getMessage());
            }
            SupplicantState currentState = wm.getConnectionInfo().getSupplicantState();
            if (startedHandshake || !currentState.equals(SupplicantState.FOUR_WAY_HANDSHAKE)) {
                if (startedHandshake) {
                    if (currentState.equals(SupplicantState.DISCONNECTED)) {
                        break;
                    } else if (currentState.equals(SupplicantState.COMPLETED)) {
                        successful = true;
                        break;
                    }
                } else {
                    continue;
                }
            } else {
                startedHandshake = true;
            }
            wm.enableNetwork(netId, true);
            i++;
        }
        if (successful || !wm.getConnectionInfo().getSupplicantState().equals(SupplicantState.COMPLETED)) {
            return successful;
        }
        return true;
    }

    @SimpleEvent(description = "Check if the negotiation with the WifiConfiguration was successful, Returns true or false.")
    public void AfterWifiNegotiation(boolean successful) {
        Log.d(LOG_TAG, "AfterWifiNegotiation");
        EventDispatcher.dispatchEvent(this, "AfterWifiNegotiation", Boolean.valueOf(successful));
    }

    @SimpleFunction(description = "Remove a SSID from the network list. Note: starting from Android M, apps are not allowed to remove networks that they did not create.")
    public boolean RemoveSSID(String ssid) {
        Log.d(LOG_TAG, "RemoveSSID");
        if (!this.isRepl) {
            for (WifiConfiguration i : wm.getConfiguredNetworks()) {
                if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
                    wm.disconnect();
                    int netId = i.networkId;
                    Log.d(LOG_TAG, "ssid: " + ssid + " found in configured SSIDs, netId: " + netId);
                    wm.removeNetwork(netId);
                    wm.saveConfiguration();
                    return true;
                }
            }
            return false;
        } else if (this.suppressWarnings) {
            return false;
        } else {
            Toast.makeText(this.context, "You have to build the app to be able to use this method!", 0).show();
            return false;
        }
    }
}
