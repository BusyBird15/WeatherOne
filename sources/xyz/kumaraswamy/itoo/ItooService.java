package xyz.kumaraswamy.itoo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import gnu.expr.Declaration;
import java.io.IOException;
import java.util.HashMap;
import xyz.kumaraswamy.itoox.ItooCreator;

public class ItooService extends Service {
    private static final String TAG = "ItooService";
    /* access modifiers changed from: private */
    public final HashMap<String, String> actions = new HashMap<>();
    /* access modifiers changed from: private */
    public ItooCreator creator;
    private Data data;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(ItooService.TAG, "Event()");
            try {
                String action = intent.getAction();
                ItooService.this.creator.startProcedureInvoke((String) ItooService.this.actions.get(action), new Object[0]);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
    };

    public void onCreate() {
        super.onCreate();
        this.data = new Data(this);
        try {
            this.data.put("running", "yes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            this.data.put("running", "no");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        Log.d(TAG, "Service Started[]");
        if (!this.data.exists("screen")) {
            return 2;
        }
        try {
            foregroundInit();
            PowerManager.WakeLock newWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, "Doraemon");
            if (newWakeLock.isHeld()) {
                newWakeLock.release();
            }
            newWakeLock.setReferenceCounted(false);
            newWakeLock.acquire(86400000);
            try {
                String str = this.data.get("screen");
                String str2 = this.data.get("procedure");
                try {
                    this.creator = new ItooCreator(this, str2, str, true);
                    return 1;
                } catch (Throwable th) {
                    th.printStackTrace();
                    Log.e(TAG, "Error While Executing Procedure");
                    throw new RuntimeException(th.getMessage() + " call [" + str2 + "]");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void foregroundInit() throws IOException {
        notificationChannel();
        startForeground(123321, new NotificationCompat.Builder(this, "ItooApple").setOngoing(true).setSmallIcon(Integer.parseInt(this.data.get("icon"))).setContentIntent(PendingIntent.getService(this, 127, new Intent(), Declaration.PUBLIC_ACCESS)).setPriority(2).setContentTitle(this.data.get("notification_title")).setContentText(this.data.get("notification_subtitle")).build());
    }

    private void notificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("ItooApple", "ItooApple", 4);
            notificationChannel.setSound((Uri) null, (AudioAttributes) null);
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(notificationChannel);
        }
    }
}
