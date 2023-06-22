package xyz.kumaraswamy.itoo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import java.io.IOException;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d("BootItoo", "Yep");
        Data data = new Data(context);
        if (data.exists("boot")) {
            try {
                if (data.get("boot").equals("process")) {
                    Intent intent2 = new Intent(context, ItooService.class);
                    if (Build.VERSION.SDK_INT >= 26) {
                        context.startForegroundService(intent2);
                    } else {
                        context.startService(intent2);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
