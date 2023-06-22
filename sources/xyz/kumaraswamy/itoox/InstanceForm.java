package xyz.kumaraswamy.itoox;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.Form;
import gnu.mapping.SimpleEnvironment;
import gnu.mapping.Symbol;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import xyz.kumaraswamy.itoox.ItooCreator;

public class InstanceForm {
    private static final String TAG = "ItooCreator";
    private final ActivityX activityX;
    public FormX formX = new FormX();

    public interface Listener {
        void event(Component component, String str, String str2, Object... objArr) throws Throwable;
    }

    public InstanceForm(ItooCreator creator) throws Exception {
        String screenName = creator.refScreen;
        Context baseContext = creator.context;
        ItooCreator.EnvironmentX envX = creator.envX;
        this.formX.attach(baseContext);
        this.activityX = new ActivityX();
        this.activityX.attach(baseContext, screenName);
        fieldModification(baseContext);
        this.formX.form$Mnenvironment = envX;
        this.formX.baseLinearLayout = new LinearLayout(this.formX, 0);
    }

    private void fieldModification(Context context) throws Exception {
        String packageName = context.getPackageName();
        String[] fieldNames = {"mWindow", "mComponent", "mWindowManager"};
        Object[] fieldNewValues = {new Dialog(context).getWindow(), new ComponentName(packageName, packageName + ".Screen1"), context.getSystemService("window")};
        for (int i = 0; i < fieldNames.length; i++) {
            modify(fieldNames[i], fieldNewValues[i]);
        }
    }

    private void modify(String name, Object value) throws Exception {
        Field field = this.activityX.getClass().getSuperclass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(this.activityX, value);
        field.set(this.formX, value);
    }

    public static class FormX extends Form {
        public LinearLayout baseLinearLayout;
        public ItooCreator creator;
        public ItooCreator.EnvironmentX form$Mnenvironment;
        public SimpleEnvironment global$Mnvar$Mnenvironment = new SimpleEnvironment() {
            public void addNewInt(Symbol key, Object newValue) {
                Log.d(InstanceForm.TAG, "put: " + key + " = " + newValue);
                System.out.println(FormX.this.symbols);
                FormX.this.symbols.put(key.getName(), newValue);
            }

            public boolean isBound(Symbol key, Object property) {
                String name = key.getName();
                boolean contains = FormX.this.symbols.containsKey(key.getName());
                Log.d(InstanceForm.TAG, "get: attempt " + name + " = " + contains);
                return contains;
            }

            public Object get(Symbol sym) {
                Log.d(InstanceForm.TAG, "get: returning " + sym);
                return FormX.this.symbols.get(sym.getName());
            }
        };
        public final HashMap<String, Object> symbols = new HashMap<>();

        public void attach(Context baseContext) {
            attachBaseContext(baseContext);
        }

        public void $add(AndroidViewComponent component) {
            this.baseLinearLayout.add(component);
        }

        public boolean canDispatchEvent(Component component, String eventName) {
            return true;
        }

        public boolean dispatchEvent(Component component, String componentName, String eventName, Object[] args) {
            dispatchGenericEvent(component, eventName, false, args);
            return true;
        }

        public void dispatchGenericEvent(Component component, String eventName, boolean notAlreadyHandled, Object[] args) {
            System.out.println(this.creator.listener);
            String componentName = this.form$Mnenvironment.toSimpleName(component);
            Log.d(InstanceForm.TAG, "AEvent(" + eventName + "=" + componentName + ") args " + Arrays.toString(args) + " listener = " + this.creator.listener);
            try {
                this.creator.listener.event(component, componentName, eventName, args);
            } catch (Throwable e) {
                e.printStackTrace();
                Log.e(InstanceForm.TAG, "Unable To Invoke Event '" + eventName + "'");
            }
        }

        public void onDestroy() {
        }

        public void onPause() {
        }

        public void onStop() {
        }

        public void onResume() {
        }
    }

    static class ActivityX extends Activity {
        private String refScreen;

        ActivityX() {
        }

        public void attach(Context baseContext, String refScreen2) {
            this.refScreen = refScreen2;
            attachBaseContext(baseContext);
        }

        public String getLocalClassName() {
            return this.refScreen;
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
        }
    }
}
