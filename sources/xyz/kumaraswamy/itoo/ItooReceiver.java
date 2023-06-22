package xyz.kumaraswamy.itoo;

import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ItooReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "ItooReceiver";

    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "On Receive");
        Bundle extras = intent.getExtras();
        Log.d(LOG_TAG, "Itoo Receiver, status = " + (((JobScheduler) context.getSystemService("jobscheduler")).schedule(Itoo.build(context, extras.getInt("jobId"), 1000, extras.getBoolean("restart"), extras.getString("screen"), extras.getString("procedure"))) == 1));
    }
}
