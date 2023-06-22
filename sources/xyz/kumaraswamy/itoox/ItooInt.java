package xyz.kumaraswamy.itoox;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.util.Pair;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.Form;
import gnu.expr.ModuleMethod;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.kumaraswamy.itoox.InstanceForm;

public class ItooInt {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final String COMPONENT_NAMES_FIELD = "components$Mnto$Mncreate";
    public static final String PROCEDURE_PREFIX = "p$";
    private static final String VARS_FIELD_NAME = "global$Mnvars$Mnto$Mncreate";
    private static HashMap<String, ArrayList<Pair<String, AndroidViewComponent>>> components = new HashMap<>();
    private final SharedPreferences prefComponents;
    private final SharedPreferences prefInts;
    private final JSONObject screenPkgNames;

    static {
        boolean z;
        if (!ItooInt.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    public ItooInt(Form form, String refScreen) throws JSONException {
        this.prefInts = getSharedPreference(form, refScreen, 0);
        this.prefComponents = getSharedPreference(form, refScreen, 1);
        this.screenPkgNames = new JSONObject(getSharedPreference(form, "", 2).getString("names", "{}"));
    }

    public int getInt(String name) {
        return this.prefInts.getInt(name, -1);
    }

    public Map<String, ?> getAll() {
        return this.prefInts.getAll();
    }

    public String getPackageNameOf(String name) {
        return this.prefComponents.getString(name, "");
    }

    public String getScreenPkgName(String name) throws JSONException {
        Log.d("ItooCreator", "Get Screen Pkg Name = " + name + " in " + this.screenPkgNames);
        return this.screenPkgNames.getString(name);
    }

    public static void listComponents(Form form) throws IllegalAccessException {
        String clazz = form.getClass().getName();
        ArrayList<Pair<String, AndroidViewComponent>> list = new ArrayList<>();
        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object object = field.get(form);
            if (object instanceof AndroidViewComponent) {
                list.add(new Pair(field.getName(), (AndroidViewComponent) object));
            }
        }
        components.put(clazz, list);
    }

    public static ArrayList<Pair<String, AndroidViewComponent>> getComponents(Form form) {
        return components.get(form.getClass().getName());
    }

    public static void saveIntStuff(Form form, String refScreen) throws Throwable {
        boolean isChanged;
        if (!(form instanceof InstanceForm.FormX)) {
            PackageInfo pkgInfo = null;
            try {
                pkgInfo = form.getPackageManager().getPackageInfo(form.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if ($assertionsDisabled || pkgInfo != null) {
                long lastUpdateTime = pkgInfo.lastUpdateTime;
                SharedPreferences prefs = getSharedPreference(form, refScreen, 3);
                if (!prefs.contains("lastUpdateTime") || prefs.getLong("lastUpdateTime", -1) != lastUpdateTime) {
                    isChanged = true;
                } else {
                    isChanged = false;
                }
                if (isChanged) {
                    saveIntsNames(form, getSharedPreference(form, refScreen, 0));
                    saveComponentNames(form, getSharedPreference(form, refScreen, 1));
                    saveScreenPkgNames(form, getSharedPreference(form, "", 2));
                    prefs.edit().putBoolean("saved", true).commit();
                    prefs.edit().putLong("lastUpdateTime", lastUpdateTime).commit();
                    return;
                }
                Log.d("ItooCreator", "Skipping save, already saved ints");
                return;
            }
            throw new AssertionError();
        }
    }

    private static void saveScreenPkgNames(Form form, SharedPreferences prefs) throws JSONException {
        JSONObject object = new JSONObject(prefs.getString("names", "{}"));
        object.put(form.getClass().getSimpleName(), form.getClass().getName());
        Log.d("ItooCreator", "Screen Pkg Names = " + object);
        prefs.edit().putString("names", object.toString()).commit();
        Log.d("ItooCreator", "Screen Pkg Names Saved");
    }

    private static void saveComponentNames(Form form, SharedPreferences prefs) throws Throwable {
        SharedPreferences.Editor editor = prefs.edit();
        Iterator it = ((LList) form.getClass().getDeclaredField(COMPONENT_NAMES_FIELD).get(form)).iterator();
        while (it.hasNext()) {
            gnu.lists.Pair right = (gnu.lists.Pair) ((gnu.lists.Pair) it.next()).getCdr();
            editor.putString(((SimpleSymbol) ((gnu.lists.Pair) right.getCdr()).getCar()).getName(), new String(((FString) right.getCar()).data));
        }
        editor.commit();
    }

    private static SharedPreferences getSharedPreference(Form form, String refScreen, int type) {
        return form.getSharedPreferences("ItooInt_" + type + "_" + refScreen, 0);
    }

    private static void saveIntsNames(Form form, SharedPreferences prefs) throws Throwable {
        SharedPreferences.Editor editor = prefs.edit();
        Iterator it = ((LList) form.getClass().getField(VARS_FIELD_NAME).get(form)).iterator();
        while (it.hasNext()) {
            Object variable = it.next();
            if (!LList.Empty.equals(variable)) {
                LList asPair = (LList) variable;
                String name = ((Symbol) asPair.get(0)).getName();
                if (name.startsWith(PROCEDURE_PREFIX)) {
                    int selector = ((ModuleMethod) ((ModuleMethod) asPair.get(1)).apply0()).selector;
                    Log.d("ItooCreator", "Put(" + name + ", " + selector + ")");
                    editor.putInt(name.substring(PROCEDURE_PREFIX.length()), selector);
                }
            }
        }
        editor.commit();
    }
}
