package xyz.kumaraswamy.itoox;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Form;
import gnu.expr.Declaration;
import xyz.kumaraswamy.itoox.InstanceForm;

public class ItooA extends AndroidNonvisibleComponent {
    private static final String TAG = "ItooCreator";

    public ItooA(ComponentContainer container) throws Throwable {
        super(container.$form());
        ItooInt.saveIntStuff(this.form, this.form.getClass().getSimpleName());
    }

    public static class ItooAlarm extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.d(ItooA.TAG, "onReceive()");
            try {
                new ItooCreator(context, intent.getStringExtra("name"), intent.getStringExtra("screen_name"), intent.getBooleanExtra("runIfActive", true));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void alarmEveryDay(Form form, int alarmId, long triggerAtMillis, int intervalMillis, String procedure, boolean runIfActive) {
        Intent intent = new Intent(form, ItooAlarm.class);
        intent.putExtra("name", procedure);
        intent.putExtra("screen_name", screenNameOf(form));
        intent.putExtra("runIfActive", runIfActive);
        ((AlarmManager) form.getSystemService(NotificationCompat.CATEGORY_ALARM)).set(0, triggerAtMillis, PendingIntent.getBroadcast(form, alarmId, intent, Declaration.PACKAGE_ACCESS));
        Log.d(TAG, "alarmEveryDay: alarm set " + triggerAtMillis);
    }

    public static void startItoOJob(Form form, long latency, int jobId, String procedure, boolean runIfActive) {
        throw new IllegalStateException("Nah");
    }

    public static void cancelJob(Form form, int jobId) {
        ((JobScheduler) form.getSystemService("jobscheduler")).cancel(jobId);
    }

    public static void cancelAlarm(Form form, int alarmId) {
        ((AlarmManager) form.getSystemService(NotificationCompat.CATEGORY_ALARM)).cancel(PendingIntent.getBroadcast(form, alarmId, new Intent(form, ItooAlarm.class), Declaration.PACKAGE_ACCESS));
    }

    public static String screenNameOf(Form form) {
        if (form instanceof InstanceForm.FormX) {
            return ((InstanceForm.FormX) form).creator.refScreen;
        }
        return form.getClass().getSimpleName();
    }
}
