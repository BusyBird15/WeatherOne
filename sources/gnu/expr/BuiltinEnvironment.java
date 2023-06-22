package gnu.expr;

import gnu.mapping.Environment;
import gnu.mapping.Location;
import gnu.mapping.LocationEnumeration;
import gnu.mapping.NamedLocation;
import gnu.mapping.Symbol;
import gnu.mapping.ThreadLocation;

public class BuiltinEnvironment extends Environment {
    static final BuiltinEnvironment instance = new BuiltinEnvironment();

    static {
        instance.setName("language-builtins");
    }

    private BuiltinEnvironment() {
    }

    public static BuiltinEnvironment getInstance() {
        return instance;
    }

    public Environment getLangEnvironment() {
        Language lang = Language.getDefaultLanguage();
        if (lang == null) {
            return null;
        }
        return lang.getLangEnvironment();
    }

    public NamedLocation lookup(Symbol name, Object property, int hash) {
        Language lang;
        if (property == ThreadLocation.ANONYMOUS || (lang = Language.getDefaultLanguage()) == null) {
            return null;
        }
        return lang.lookupBuiltin(name, property, hash);
    }

    public NamedLocation getLocation(Symbol key, Object property, int hash, boolean create) {
        throw new RuntimeException();
    }

    public void define(Symbol key, Object property, Object newValue) {
        throw new RuntimeException();
    }

    public LocationEnumeration enumerateLocations() {
        return getLangEnvironment().enumerateLocations();
    }

    public LocationEnumeration enumerateAllLocations() {
        return getLangEnvironment().enumerateAllLocations();
    }

    /* access modifiers changed from: protected */
    public boolean hasMoreElements(LocationEnumeration it) {
        throw new RuntimeException();
    }

    public NamedLocation addLocation(Symbol name, Object prop, Location loc) {
        throw new RuntimeException();
    }
}
