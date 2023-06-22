package xyz.kumaraswamy.itoo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.util.Log;
import java.util.HashMap;
import xyz.kumaraswamy.itoox.ItooCreator;

public class ItooJobService extends JobService {
    private static final String TAG = "ItooService";
    /* access modifiers changed from: private */
    public final HashMap<String, String> actions = new HashMap<>();
    /* access modifiers changed from: private */
    public ItooCreator creator;
    JobParameters parms;
    private String procedure;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(ItooJobService.TAG, "Event()");
            try {
                String action = intent.getAction();
                ItooJobService.this.creator.startProcedureInvoke((String) ItooJobService.this.actions.get(action), new Object[0]);
            } catch (Throwable th) {
                throw new RuntimeException(th);
            }
        }
    };
    private boolean registered = false;
    private boolean restart;
    private String screen;

    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job Started[]");
        this.parms = jobParameters;
        PowerManager.WakeLock newWakeLock = ((PowerManager) getSystemService("power")).newWakeLock(1, "Doraemon");
        if (newWakeLock.isHeld()) {
            newWakeLock.release();
        }
        newWakeLock.setReferenceCounted(false);
        newWakeLock.acquire(86400000);
        PersistableBundle extras = jobParameters.getExtras();
        this.screen = extras.getString("screen");
        this.procedure = extras.getString("procedure");
        this.restart = extras.getBoolean("restart");
        try {
            this.creator = new ItooCreator(jobParameters.getJobId(), this, this.procedure, this.screen, true);
            return true;
        } catch (Throwable th) {
            Log.e(TAG, "Error While Executing Procedure");
            throw new RuntimeException(th);
        }
    }

    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job Ended []");
        try {
            this.creator.flagEnd();
            if (this.registered) {
                unregisterReceiver(this.receiver);
            }
            if (!this.restart) {
                return false;
            }
            Intent intent = new Intent(this, ItooReceiver.class);
            intent.putExtra("jobId", jobParameters.getJobId());
            intent.putExtra("screen", this.screen);
            intent.putExtra("restart", this.restart);
            intent.putExtra("procedure", this.procedure);
            intent.putExtra("actions", this.actions);
            sendBroadcast(intent);
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
