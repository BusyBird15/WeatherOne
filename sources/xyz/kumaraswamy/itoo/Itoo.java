package xyz.kumaraswamy.itoo;

import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.util.Log;
import androidx.appcompat.widget.ActivityChooserView;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import xyz.kumaraswamy.itoox.InstanceForm;
import xyz.kumaraswamy.itoox.ItooInt;

public class Itoo extends AndroidNonvisibleComponent {
    private final List<String> actions = new ArrayList();
    private final Data data;
    /* access modifiers changed from: private */
    public final HashMap<String, String> events = new HashMap<>();
    private int icon = 17301543;
    private final JobScheduler scheduler = ((JobScheduler) this.form.getSystemService("jobscheduler"));
    private String screenName = this.form.getClass().getSimpleName();
    private final Data userData;

    public Itoo(ComponentContainer componentContainer) throws Throwable {
        super(componentContainer.$form());
        ItooInt.saveIntStuff(this.form, this.screenName);
        if (this.form instanceof InstanceForm.FormX) {
            this.screenName = ((InstanceForm.FormX) this.form).creator.refScreen;
            final InstanceForm.FormX formX = (InstanceForm.FormX) this.form;
            formX.creator.listener = new InstanceForm.Listener() {
                public void event(Component component, String str, String str2, Object... objArr) throws Throwable {
                    String str3 = (String) Itoo.this.events.get(str + "." + str2);
                    Log.i("Itoo", "Event " + str + " invoke " + str3);
                    if (str3 != null) {
                        formX.creator.startProcedureInvoke(str3, objArr);
                    }
                }
            };
        }
        this.data = new Data(this.form);
        this.userData = new Data(this.form, "stored_files");
    }

    @SimpleFunction
    public void RegisterEvent(String str, String str2) {
        this.events.put(str, str2);
    }

    @SimpleProperty
    public void NotificationIcon(int i) {
        this.icon = i;
    }

    @SimpleProperty
    public int NotificationIcon() {
        return this.icon;
    }

    @SimpleFunction
    public void SaveProcessForBoot(String str, String str2, String str3) throws IOException, JSONException {
        dumpDetails(str, str2, str3);
        this.data.put("boot", "process");
    }

    @SimpleFunction(description = "Starts a background service with procedure call")
    public boolean CreateProcess(String str, String str2, String str3) throws IOException, JSONException {
        StopProcess();
        dumpDetails(str, str2, str3);
        Intent intent = new Intent(this.form, ItooService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            this.form.startForegroundService(intent);
            return true;
        }
        this.form.startService(intent);
        return true;
    }

    private void dumpDetails(String str, String str2, String str3) throws IOException, JSONException {
        this.data.put("screen", this.screenName).put("procedure", str).put("notification_title", str2).put("notification_subtitle", str3).put("icon", String.valueOf(this.icon));
    }

    private boolean isMyServiceRunning(Class<?> cls) {
        ActivityManager activityManager = (ActivityManager) this.form.getSystemService("activity");
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED)) {
                if (cls.getName().equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @SimpleFunction
    public boolean ProcessRunning() {
        return isMyServiceRunning(ItooService.class);
    }

    @SimpleFunction(description = "Ends the service from inside")
    public void StopProcess() {
        this.data.delete("screen");
        this.data.delete("procedure");
        this.data.delete("notification_title");
        this.data.delete("notification_subtitle");
        this.data.delete("actions");
        this.form.stopService(new Intent(this.form, ItooService.class));
    }

    @SimpleFunction(description = "Starts a background service with procedure call")
    public boolean CreateTask(long j, int i, String str, boolean z) throws JSONException {
        return this.scheduler.schedule(build(this.form, i, j, z, this.screenName, str)) == 1;
    }

    public static JobInfo build(Context context, int i, long j, boolean z, String str, String str2) {
        JobInfo.Builder overrideDeadline = new JobInfo.Builder(i, new ComponentName(context, ItooJobService.class)).setRequiredNetworkType(0).setMinimumLatency(j).setOverrideDeadline(100 + j);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putString("screen", str);
        persistableBundle.putString("procedure", str2);
        persistableBundle.putBoolean("restart", z);
        overrideDeadline.setExtras(persistableBundle);
        return overrideDeadline.build();
    }

    @SimpleFunction(description = "Ends the service from inside the service only.")
    public void StopTask() throws Exception {
        if (this.form instanceof InstanceForm.FormX) {
            InstanceForm.FormX formX = (InstanceForm.FormX) this.form;
            formX.creator.flagEnd();
            ItooJobService itooJobService = (ItooJobService) formX.creator.context;
            itooJobService.jobFinished(itooJobService.parms, false);
            return;
        }
        throw new YailRuntimeError("Use CancelTask block instead when calling outside the service", "Itoo");
    }

    @SimpleFunction(description = "Check if a task corresponding to the Id is running")
    public boolean IsRunning(int i) {
        return this.scheduler.getPendingJob(i) != null;
    }

    @SimpleFunction(description = "Cancels the service by Id")
    public void CancelTask(int i) {
        this.scheduler.cancel(i);
    }

    @SimpleFunction
    public void StoreProperty(String str, String str2) throws IOException {
        this.userData.put(str, str2);
    }

    @SimpleFunction
    public String FetchProperty(String str) throws IOException {
        if (!this.userData.exists(str)) {
            return "";
        }
        return this.userData.get(str);
    }
}
