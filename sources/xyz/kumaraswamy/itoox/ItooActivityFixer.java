package xyz.kumaraswamy.itoox;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.appinventor.components.runtime.Form;
import java.lang.reflect.Field;

public class ItooActivityFixer implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ItooActivityFixer";
    private static ItooActivityFixer fixer;
    private final Application application;
    private final ItooDestroyListener listener;
    private final boolean resetForm;

    public interface ItooDestroyListener {
        void onDestroy();
    }

    public static void registerItooDestroyListener(ItooDestroyListener listener2, Form form) {
        Application application2 = form.getApplication();
        ItooActivityFixer itooActivityFixer = new ItooActivityFixer(application2, listener2, false);
        fixer = itooActivityFixer;
        application2.registerActivityLifecycleCallbacks(itooActivityFixer);
    }

    public static void fixItooListener(Form form) {
        Application application2 = form.getApplication();
        ItooActivityFixer itooActivityFixer = new ItooActivityFixer(application2, (ItooDestroyListener) null, true);
        fixer = itooActivityFixer;
        application2.registerActivityLifecycleCallbacks(itooActivityFixer);
    }

    private ItooActivityFixer(Application application2, ItooDestroyListener listener2, boolean resetForm2) {
        this.application = application2;
        this.listener = listener2;
        this.resetForm = resetForm2;
    }

    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d(TAG, "onActivityDestroyed()");
        if (this.resetForm) {
            try {
                activeFieldModification();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "Unable to modify form");
            }
        }
        if (this.listener != null) {
            this.listener.onDestroy();
        }
        this.application.unregisterActivityLifecycleCallbacks(this);
    }

    private void activeFieldModification() throws Exception {
        Field field = Form.class.getDeclaredField("activeForm");
        field.setAccessible(true);
        field.set((Object) null, (Object) null);
    }

    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
    }

    public void onActivityStarted(@NonNull Activity activity) {
    }

    public void onActivityResumed(@NonNull Activity activity) {
    }

    public void onActivityPaused(@NonNull Activity activity) {
    }

    public void onActivityStopped(@NonNull Activity activity) {
    }

    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }
}
