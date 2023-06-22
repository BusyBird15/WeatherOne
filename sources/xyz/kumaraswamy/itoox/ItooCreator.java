package xyz.kumaraswamy.itoox;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobService;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.util.YailDictionary;
import com.google.youngandroid.runtime;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleMethod;
import gnu.mapping.CallContext;
import gnu.mapping.SimpleEnvironment;
import gnu.mapping.Symbol;
import gnu.math.IntNum;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import kawa.standard.Scheme;
import xyz.kumaraswamy.itoox.InstanceForm;

public class ItooCreator {
    private static final String CHANNEL_ID = "Itoo Service";
    private static final String TAG = "ItooCreator";
    private final Form activeForm;
    public final boolean appOpen;
    /* access modifiers changed from: private */
    public final HashMap<String, Component> components;
    public final Context context;
    public EnvironmentX envX;
    /* access modifiers changed from: private */
    public InstanceForm formInst;
    /* access modifiers changed from: private */
    public IntInvoke intIvk;
    /* access modifiers changed from: private */
    public ItooInt ints;
    public InstanceForm.Listener listener;
    private String notification_subtitle;
    private String notification_title;
    public final String refScreen;
    private Timer timer;

    public ItooCreator(Context context2, String procedure, String refScreen2, boolean runIfActive) throws Throwable {
        this(-1, context2, procedure, refScreen2, runIfActive);
    }

    public ItooCreator(final int jobId, Context context2, final String procedure, String refScreen2, boolean runIfActive) throws Throwable {
        this.formInst = null;
        this.components = new HashMap<>();
        this.notification_title = "Itoo";
        this.notification_subtitle = "Itoo Creator";
        this.listener = new InstanceForm.Listener() {
            public void event(Component component, String componentName, String eventName, Object... args) {
                Log.d(ItooCreator.TAG, "Event Default Triggered");
            }
        };
        this.context = context2;
        this.refScreen = refScreen2;
        Log.d(TAG, "Itoo Creator, name = " + procedure + ", ref screen = " + refScreen2 + " runIfActive = " + runIfActive);
        this.activeForm = Form.getActiveForm();
        if (this.activeForm instanceof InstanceForm.FormX) {
            this.appOpen = false;
        } else {
            this.appOpen = this.activeForm != null;
        }
        Log.d(TAG, "ItooCreator: is the app active " + this.appOpen);
        if (!this.appOpen) {
            this.envX = new EnvironmentX();
            Log.d(TAG, "ItooCreator: Pass 1");
            languageInitialization();
            Log.d(TAG, "ItooCreator: Pass 2");
            activeFieldModification(true);
            Log.d(TAG, "ItooCreator: Pass 3");
            runtimeInitialization();
            Log.d(TAG, "ItooCreator: Pass 4");
            addIntsToEnvironment();
            Log.d(TAG, "ItooCreator: Pass 5");
            Log.d(TAG, "ItooCreator: theme set");
            context2.setTheme(2131427488);
        }
        if (!this.appOpen || runIfActive) {
            if (this.ints == null) {
                initializeIntVars();
            }
            Log.d(TAG, "ItooCreator: app ref instance " + Class.forName(this.ints.getScreenPkgName(refScreen2)).getConstructor(new Class[0]).newInstance(new Object[0]));
            boolean typeNormal = true;
            YailDictionary config = (YailDictionary) startProcedureInvoke("itoo_config", new Object[0]);
            if (config != null) {
                Log.d(TAG, "ItooCreator: Config = " + config);
                typeNormal = ((Boolean) config.get("type")).booleanValue();
                if (config.containsKey("notification")) {
                    YailDictionary notif_config = (YailDictionary) config.get("notification");
                    this.notification_title = String.valueOf(notif_config.get("title"));
                    this.notification_subtitle = String.valueOf(notif_config.get("subtitle"));
                }
            }
            if (!typeNormal) {
                Log.d(TAG, "ItooCreator: multiple invocations");
                foregroundInitialization();
                this.timer = new Timer();
                this.timer.schedule(new TimerTask() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            public void run() {
                                try {
                                    ItooCreator.this.startProcedureInvoke(procedure, Integer.valueOf(jobId));
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, 0, ((IntNum) config.get("ftimer")).longValue());
                return;
            }
            Log.d(TAG, "ItooCreator: normal invocations");
            startProcedureInvoke(procedure, Integer.valueOf(jobId));
            return;
        }
        Log.i(TAG, "Reject Initialization");
    }

    private void foregroundInitialization() {
        notificationChannel();
        ((JobService) this.context).startForeground(177723, new NotificationCompat.Builder(this.context, CHANNEL_ID).setOngoing(true).setSmallIcon(17301569).setPriority(-1).setContentTitle(this.notification_title).setContentText(this.notification_subtitle).build());
    }

    private void notificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Lite Service", 4);
            serviceChannel.setSound((Uri) null, (AudioAttributes) null);
            ((NotificationManager) this.context.getSystemService(NotificationManager.class)).createNotificationChannel(serviceChannel);
        }
    }

    private void addIntsToEnvironment() throws Exception {
        initializeIntVars();
        for (Map.Entry<String, ?> key : this.ints.getAll().entrySet()) {
            Integer value = (Integer) key.getValue();
            Log.d(TAG, "addIntsToEnvironment: add int (" + key.getKey() + ", " + value + ")");
            this.formInst.formX.symbols.put(ItooInt.PROCEDURE_PREFIX + key.getKey(), new IntBody(value.intValue(), 0));
        }
    }

    private void initializeIntVars() throws Exception {
        this.ints = new ItooInt(this.appOpen ? this.activeForm : this.formInst.formX, this.refScreen);
        this.intIvk = new IntInvoke();
    }

    public Component getInstance(String pkgName) throws Exception {
        return this.envX.getComponent(pkgName, this.ints.getPackageNameOf(pkgName));
    }

    public Object startProcedureInvoke(String procName, Object... args) throws Throwable {
        int _int = this.ints.getInt(procName);
        Log.d(TAG, "startProcedureInvoke: " + _int);
        if (_int != -1) {
            return this.intIvk.intInvoke(_int, args);
        }
        Log.d(TAG, "startProcedureInvoke: failed to find name(" + procName + ")");
        return null;
    }

    public void invokeInt(int _int, Object... args) throws Throwable {
        Object unused = this.intIvk.intInvoke(_int, args);
    }

    public void flagEnd() throws Exception {
        if (this.timer != null) {
            this.timer.cancel();
        }
        for (Component component : this.components.values()) {
            callSilently(component, "onPause");
            callSilently(component, "onDestroy");
        }
        Language.setCurrentLanguage((Language) null);
        activeFieldModification(false);
    }

    private void callSilently(Component component, String name) {
        try {
            component.getClass().getMethod(name, new Class[0]).invoke(component, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        }
    }

    class IntInvoke {
        private final ModuleBody frameX;

        public IntInvoke() throws Exception {
            String className = ItooCreator.this.ints.getScreenPkgName(ItooCreator.this.refScreen) + "$frame";
            Log.d(ItooCreator.TAG, "IntInvoke: the attempt class name: " + className);
            this.frameX = (ModuleBody) Class.forName(className).getConstructor(new Class[0]).newInstance(new Object[0]);
        }

        public Object intInvoke(int _int) throws Throwable {
            return intInvoke(_int, new Object[0]);
        }

        /* access modifiers changed from: private */
        public Object intInvoke(int _int, Object... args) throws Throwable {
            return applySlex(new IntBody(_int, args.length), args);
        }

        public Object applySlex(ModuleMethod method, Object... args) throws Throwable {
            switch (args.length) {
                case 0:
                    return this.frameX.apply0(method);
                case 1:
                    return this.frameX.apply1(method, args[0]);
                case 2:
                    return this.frameX.apply2(method, args[0], args[1]);
                case 3:
                    return this.frameX.apply3(method, args[0], args[1], args[2]);
                case 4:
                    return this.frameX.apply4(method, args[0], args[1], args[2], args[3]);
                default:
                    return this.frameX.applyN(method, args);
            }
        }
    }

    class IntBody extends ModuleMethod {
        public IntBody(int selector, int args) {
            super((ModuleBody) null, selector, (Object) null, args);
        }

        public Object applyN(Object[] args) throws Throwable {
            Log.d(ItooCreator.TAG, "applyN: with args(" + Arrays.toString(args) + ")");
            return ItooCreator.this.intIvk.applySlex(this, args);
        }
    }

    private void runtimeInitialization() {
        new runtime().run(new CallContext());
        runtime.setThisForm();
    }

    private void activeFieldModification(boolean init) throws Exception {
        Field field = Form.class.getDeclaredField("activeForm");
        field.setAccessible(true);
        if (init) {
            field.set((Object) null, formInstance());
        } else {
            field.set((Object) null, (Object) null);
        }
    }

    /* access modifiers changed from: private */
    public Form formInstance() throws Exception {
        this.formInst = new InstanceForm(this);
        this.formInst.formX.creator = this;
        float deviceDensity = this.context.getResources().getDisplayMetrics().density;
        set("deviceDensity", Float.valueOf(deviceDensity));
        set("formWidth", Integer.valueOf((int) (((float) this.context.getResources().getDisplayMetrics().widthPixels) / deviceDensity)));
        set("formHeight", Integer.valueOf((int) (((float) this.context.getResources().getDisplayMetrics().heightPixels) / deviceDensity)));
        return this.formInst.formX;
    }

    private void set(String name, Object value) throws Exception {
        Field field = Form.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(this.formInst.formX, value);
    }

    private void languageInitialization() throws Exception {
        Language language = Scheme.getInstance();
        if (language == null) {
            Log.i(TAG, "Language = null");
        }
        Language.setCurrentLanguage(language);
        activeFieldModification(false);
    }

    public class EnvironmentX extends SimpleEnvironment {
        public final HashMap<Component, String> names = new HashMap<>();

        public EnvironmentX() {
        }

        public String toSimpleName(Component component) {
            return this.names.get(component);
        }

        public boolean isBound(Symbol key, Object property) {
            if (ItooCreator.this.components.containsKey(key.getName())) {
                return super.isBound(key, property);
            }
            try {
                componentInstance(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        private void componentInstance(Symbol symbol) throws Exception {
            String name = symbol.getName();
            Component component = name.equals(ItooCreator.this.refScreen) ? ItooCreator.this.formInst.formX : getComponent(name, ItooCreator.this.ints.getPackageNameOf(name));
            put(symbol, (Object) component);
            this.names.put(component, name);
            ItooCreator.this.components.put(name, component);
        }

        /* access modifiers changed from: private */
        public Component getComponent(String name, String packageNameOf) throws Exception {
            Constructor<?> constructor;
            Log.d(ItooCreator.TAG, "Create component = " + name + " = " + packageNameOf);
            try {
                Class<?> clazz = Class.forName(packageNameOf);
                try {
                    constructor = clazz.getConstructor(new Class[]{ComponentContainer.class});
                } catch (NoSuchMethodException e) {
                    constructor = clazz.getConstructor(new Class[]{Form.class});
                }
                return (Component) constructor.newInstance(new Object[]{ItooCreator.this.formInstance()});
            } catch (ClassNotFoundException e2) {
                Log.d(ItooCreator.TAG, "Component Not found Name = " + packageNameOf + " realName = " + name);
                throw e2;
            }
        }
    }
}
