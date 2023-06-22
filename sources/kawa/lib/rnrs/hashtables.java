package kawa.lib.rnrs;

import androidx.fragment.app.FragmentTransaction;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.util.AbstractHashTable;
import gnu.kawa.util.HashNode;
import gnu.lists.Consumer;
import gnu.lists.FVector;
import gnu.lists.Pair;
import gnu.mapping.CallContext;
import gnu.mapping.Procedure;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import kawa.lib.kawa.hashtable;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.standard.Scheme;

/* compiled from: hashtables.scm */
public class hashtables extends ModuleBody {
    public static final hashtables $instance = new hashtables();
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("hash-by-identity").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("hash-for-eqv").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("hashtable-contains?").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("hashtable-update!").readResolve());
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("hashtable-copy").readResolve());
    static final SimpleSymbol Lit13 = ((SimpleSymbol) new SimpleSymbol("hashtable-clear!").readResolve());
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("hashtable-keys").readResolve());
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("hashtable-entries").readResolve());
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("hashtable-equivalence-function").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("hashtable-hash-function").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("hashtable-mutable?").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("equal-hash").readResolve());
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("make-eq-hashtable").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("string-hash").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("string-ci-hash").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("symbol-hash").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("make-eqv-hashtable").readResolve());
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol("make-hashtable").readResolve());
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("hashtable?").readResolve());
    static final SimpleSymbol Lit6 = ((SimpleSymbol) new SimpleSymbol("hashtable-size").readResolve());
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("hashtable-ref").readResolve());
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("hashtable-set!").readResolve());
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("hashtable-delete!").readResolve());
    public static final ModuleMethod equal$Mnhash;
    static final ModuleMethod hash$Mnby$Mnidentity;
    static final ModuleMethod hash$Mnfor$Mneqv;
    public static final ModuleMethod hashtable$Mnclear$Ex;
    public static final ModuleMethod hashtable$Mncontains$Qu;
    public static final ModuleMethod hashtable$Mncopy;
    public static final ModuleMethod hashtable$Mndelete$Ex;
    public static final ModuleMethod hashtable$Mnentries;
    public static final ModuleMethod hashtable$Mnequivalence$Mnfunction;
    public static final ModuleMethod hashtable$Mnhash$Mnfunction;
    public static final ModuleMethod hashtable$Mnkeys;
    public static final ModuleMethod hashtable$Mnmutable$Qu;
    public static final ModuleMethod hashtable$Mnref;
    public static final ModuleMethod hashtable$Mnset$Ex;
    public static final ModuleMethod hashtable$Mnsize;
    public static final ModuleMethod hashtable$Mnupdate$Ex;
    public static final ModuleMethod hashtable$Qu;
    public static final ModuleMethod make$Mneq$Mnhashtable;
    public static final ModuleMethod make$Mneqv$Mnhashtable;
    public static final ModuleMethod make$Mnhashtable;
    public static final ModuleMethod string$Mnci$Mnhash;
    public static final ModuleMethod string$Mnhash;
    public static final ModuleMethod symbol$Mnhash;

    static {
        hashtables hashtables = $instance;
        hash$Mnby$Mnidentity = new ModuleMethod(hashtables, 1, Lit0, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hash$Mnfor$Mneqv = new ModuleMethod(hashtables, 2, Lit1, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        make$Mneq$Mnhashtable = new ModuleMethod(hashtables, 3, Lit2, 4096);
        make$Mneqv$Mnhashtable = new ModuleMethod(hashtables, 5, Lit3, 4096);
        make$Mnhashtable = new ModuleMethod(hashtables, 7, Lit4, 12290);
        hashtable$Qu = new ModuleMethod(hashtables, 9, Lit5, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hashtable$Mnsize = new ModuleMethod(hashtables, 10, Lit6, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hashtable$Mnref = new ModuleMethod(hashtables, 11, Lit7, 12291);
        hashtable$Mnset$Ex = new ModuleMethod(hashtables, 12, Lit8, 12291);
        hashtable$Mndelete$Ex = new ModuleMethod(hashtables, 13, Lit9, 8194);
        hashtable$Mncontains$Qu = new ModuleMethod(hashtables, 14, Lit10, 8194);
        hashtable$Mnupdate$Ex = new ModuleMethod(hashtables, 15, Lit11, 16388);
        hashtable$Mncopy = new ModuleMethod(hashtables, 16, Lit12, 8193);
        hashtable$Mnclear$Ex = new ModuleMethod(hashtables, 18, Lit13, 8193);
        hashtable$Mnkeys = new ModuleMethod(hashtables, 20, Lit14, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hashtable$Mnentries = new ModuleMethod(hashtables, 21, Lit15, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hashtable$Mnequivalence$Mnfunction = new ModuleMethod(hashtables, 22, Lit16, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hashtable$Mnhash$Mnfunction = new ModuleMethod(hashtables, 23, Lit17, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        hashtable$Mnmutable$Qu = new ModuleMethod(hashtables, 24, Lit18, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        equal$Mnhash = new ModuleMethod(hashtables, 25, Lit19, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        string$Mnhash = new ModuleMethod(hashtables, 26, Lit20, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        string$Mnci$Mnhash = new ModuleMethod(hashtables, 27, Lit21, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        symbol$Mnhash = new ModuleMethod(hashtables, 28, Lit22, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        $instance.run();
    }

    public hashtables() {
        ModuleInfo.register(this);
    }

    public static void hashtableClear$Ex(hashtable.HashTable hashTable) {
        hashtableClear$Ex(hashTable, 64);
    }

    public static hashtable.HashTable hashtableCopy(hashtable.HashTable hashTable) {
        return hashtableCopy(hashTable, false);
    }

    public static hashtable.HashTable makeEqHashtable() {
        return makeEqHashtable(AbstractHashTable.DEFAULT_INITIAL_SIZE);
    }

    public static hashtable.HashTable makeEqvHashtable() {
        return makeEqvHashtable(AbstractHashTable.DEFAULT_INITIAL_SIZE);
    }

    public static hashtable.HashTable makeHashtable(Procedure procedure, Procedure procedure2) {
        return makeHashtable(procedure, procedure2, AbstractHashTable.DEFAULT_INITIAL_SIZE);
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
    }

    static int hashByIdentity(Object obj) {
        return System.identityHashCode(obj);
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 1:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 2:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 3:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 5:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 9:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 10:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 16:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 18:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 20:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 21:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 22:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 23:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 24:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 25:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 26:
                if (!(obj instanceof CharSequence)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 27:
                if (!(obj instanceof CharSequence)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 28:
                if (!(obj instanceof Symbol)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    static int hashForEqv(Object obj) {
        return obj.hashCode();
    }

    public static hashtable.HashTable makeEqHashtable(int k) {
        return new hashtable.HashTable(Scheme.isEq, hash$Mnby$Mnidentity, AbstractHashTable.DEFAULT_INITIAL_SIZE);
    }

    public int match0(ModuleMethod moduleMethod, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 3:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 5:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            default:
                return super.match0(moduleMethod, callContext);
        }
    }

    public static hashtable.HashTable makeEqvHashtable(int k) {
        return new hashtable.HashTable(Scheme.isEqv, hash$Mnfor$Mneqv, AbstractHashTable.DEFAULT_INITIAL_SIZE);
    }

    public Object apply0(ModuleMethod moduleMethod) {
        switch (moduleMethod.selector) {
            case 3:
                return makeEqHashtable();
            case 5:
                return makeEqvHashtable();
            default:
                return super.apply0(moduleMethod);
        }
    }

    public static hashtable.HashTable makeHashtable(Procedure comparison, Procedure hash, int size) {
        return new hashtable.HashTable(comparison, hash, size);
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 7:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                if (!(obj2 instanceof Procedure)) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 13:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 14:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 16:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 18:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            default:
                return super.match2(moduleMethod, obj, obj2, callContext);
        }
    }

    public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 7:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                if (!(obj2 instanceof Procedure)) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 11:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 12:
                if (!(obj instanceof hashtable.HashTable)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            default:
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
        }
    }

    public static boolean isHashtable(Object obj) {
        return obj instanceof hashtable.HashTable;
    }

    public static int hashtableSize(hashtable.HashTable ht) {
        return ht.size();
    }

    public static Object hashtableRef(hashtable.HashTable ht, Object key, Object obj) {
        HashNode node = ht.getNode(key);
        return node == null ? obj : node.getValue();
    }

    public static void hashtableSet$Ex(hashtable.HashTable ht, Object key, Object value) {
        hashtable.hashtableCheckMutable(ht);
        ht.put(key, value);
    }

    public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
        switch (moduleMethod.selector) {
            case 7:
                try {
                    try {
                        try {
                            return makeHashtable((Procedure) obj, (Procedure) obj2, ((Number) obj3).intValue());
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "make-hashtable", 3, obj3);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "make-hashtable", 2, obj2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "make-hashtable", 1, obj);
                }
            case 11:
                try {
                    return hashtableRef((hashtable.HashTable) obj, obj2, obj3);
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "hashtable-ref", 1, obj);
                }
            case 12:
                try {
                    hashtableSet$Ex((hashtable.HashTable) obj, obj2, obj3);
                    return Values.empty;
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "hashtable-set!", 1, obj);
                }
            default:
                return super.apply3(moduleMethod, obj, obj2, obj3);
        }
    }

    public static void hashtableDelete$Ex(hashtable.HashTable ht, Object key) {
        hashtable.hashtableCheckMutable(ht);
        ht.remove(key);
    }

    public static boolean isHashtableContains(hashtable.HashTable ht, Object key) {
        return ((ht.getNode(key) == null ? 1 : 0) + 1) & true;
    }

    public static Object hashtableUpdate$Ex(hashtable.HashTable ht, Object key, Procedure proc, Object obj) {
        hashtable.hashtableCheckMutable(ht);
        HashNode node = ht.getNode(key);
        if (node != null) {
            return node.setValue(proc.apply1(node.getValue()));
        }
        hashtableSet$Ex(ht, key, proc.apply1(obj));
        return Values.empty;
    }

    public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
        if (moduleMethod.selector != 15) {
            return super.apply4(moduleMethod, obj, obj2, obj3, obj4);
        }
        try {
            try {
                return hashtableUpdate$Ex((hashtable.HashTable) obj, obj2, (Procedure) obj3, obj4);
            } catch (ClassCastException e) {
                throw new WrongType(e, "hashtable-update!", 3, obj3);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "hashtable-update!", 1, obj);
        }
    }

    public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
        if (moduleMethod.selector != 15) {
            return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
        }
        if (!(obj instanceof hashtable.HashTable)) {
            return -786431;
        }
        callContext.value1 = obj;
        callContext.value2 = obj2;
        if (!(obj3 instanceof Procedure)) {
            return -786429;
        }
        callContext.value3 = obj3;
        callContext.value4 = obj4;
        callContext.proc = moduleMethod;
        callContext.pc = 4;
        return 0;
    }

    public static hashtable.HashTable hashtableCopy(hashtable.HashTable ht, boolean mutable) {
        return new hashtable.HashTable(ht, mutable);
    }

    public static void hashtableClear$Ex(hashtable.HashTable ht, int k) {
        hashtable.hashtableCheckMutable(ht);
        ht.clear();
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        boolean z = true;
        switch (moduleMethod.selector) {
            case 7:
                try {
                    try {
                        return makeHashtable((Procedure) obj, (Procedure) obj2);
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "make-hashtable", 2, obj2);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "make-hashtable", 1, obj);
                }
            case 13:
                try {
                    hashtableDelete$Ex((hashtable.HashTable) obj, obj2);
                    return Values.empty;
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "hashtable-delete!", 1, obj);
                }
            case 14:
                try {
                    return isHashtableContains((hashtable.HashTable) obj, obj2) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "hashtable-contains?", 1, obj);
                }
            case 16:
                try {
                    hashtable.HashTable hashTable = (hashtable.HashTable) obj;
                    try {
                        if (obj2 == Boolean.FALSE) {
                            z = false;
                        }
                        return hashtableCopy(hashTable, z);
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "hashtable-copy", 2, obj2);
                    }
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "hashtable-copy", 1, obj);
                }
            case 18:
                try {
                    try {
                        hashtableClear$Ex((hashtable.HashTable) obj, ((Number) obj2).intValue());
                        return Values.empty;
                    } catch (ClassCastException e7) {
                        throw new WrongType(e7, "hashtable-clear!", 2, obj2);
                    }
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "hashtable-clear!", 1, obj);
                }
            default:
                return super.apply2(moduleMethod, obj, obj2);
        }
    }

    public static FVector hashtableKeys(hashtable.HashTable ht) {
        return ht.keysVector();
    }

    public static Object hashtableEntries(hashtable.HashTable ht) {
        Pair pair = ht.entriesVectorPair();
        return misc.values(lists.car.apply1(pair), lists.cdr.apply1(pair));
    }

    public static Procedure hashtableEquivalenceFunction(hashtable.HashTable ht) {
        return (Procedure) ht.equivalenceFunction.apply1(ht);
    }

    public static Object hashtableHashFunction(hashtable.HashTable ht) {
        Object hasher = ht.hashFunction.apply1(ht);
        Object x = Scheme.isEqv.apply2(hasher, hash$Mnby$Mnidentity);
        if (x != Boolean.FALSE) {
            if (x == Boolean.FALSE) {
                return hasher;
            }
        } else if (Scheme.isEqv.apply2(hasher, hash$Mnfor$Mneqv) == Boolean.FALSE) {
            return hasher;
        }
        return Boolean.FALSE;
    }

    public static Object isHashtableMutable(hashtable.HashTable ht) {
        return Scheme.applyToArgs.apply1(ht.mutable ? Boolean.TRUE : Boolean.FALSE);
    }

    public static int equalHash(Object key) {
        return key.hashCode();
    }

    public static int stringHash(CharSequence s) {
        return s.hashCode();
    }

    public static int stringCiHash(CharSequence s) {
        return s.toString().toLowerCase().hashCode();
    }

    public static int symbolHash(Symbol s) {
        return s.hashCode();
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 1:
                return Integer.valueOf(hashByIdentity(obj));
            case 2:
                return Integer.valueOf(hashForEqv(obj));
            case 3:
                try {
                    return makeEqHashtable(((Number) obj).intValue());
                } catch (ClassCastException e) {
                    throw new WrongType(e, "make-eq-hashtable", 1, obj);
                }
            case 5:
                try {
                    return makeEqvHashtable(((Number) obj).intValue());
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "make-eqv-hashtable", 1, obj);
                }
            case 9:
                return isHashtable(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 10:
                try {
                    return Integer.valueOf(hashtableSize((hashtable.HashTable) obj));
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "hashtable-size", 1, obj);
                }
            case 16:
                try {
                    return hashtableCopy((hashtable.HashTable) obj);
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "hashtable-copy", 1, obj);
                }
            case 18:
                try {
                    hashtableClear$Ex((hashtable.HashTable) obj);
                    return Values.empty;
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "hashtable-clear!", 1, obj);
                }
            case 20:
                try {
                    return hashtableKeys((hashtable.HashTable) obj);
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "hashtable-keys", 1, obj);
                }
            case 21:
                try {
                    return hashtableEntries((hashtable.HashTable) obj);
                } catch (ClassCastException e7) {
                    throw new WrongType(e7, "hashtable-entries", 1, obj);
                }
            case 22:
                try {
                    return hashtableEquivalenceFunction((hashtable.HashTable) obj);
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "hashtable-equivalence-function", 1, obj);
                }
            case 23:
                try {
                    return hashtableHashFunction((hashtable.HashTable) obj);
                } catch (ClassCastException e9) {
                    throw new WrongType(e9, "hashtable-hash-function", 1, obj);
                }
            case 24:
                try {
                    return isHashtableMutable((hashtable.HashTable) obj);
                } catch (ClassCastException e10) {
                    throw new WrongType(e10, "hashtable-mutable?", 1, obj);
                }
            case 25:
                return Integer.valueOf(equalHash(obj));
            case 26:
                try {
                    return Integer.valueOf(stringHash((CharSequence) obj));
                } catch (ClassCastException e11) {
                    throw new WrongType(e11, "string-hash", 1, obj);
                }
            case 27:
                try {
                    return Integer.valueOf(stringCiHash((CharSequence) obj));
                } catch (ClassCastException e12) {
                    throw new WrongType(e12, "string-ci-hash", 1, obj);
                }
            case 28:
                try {
                    return Integer.valueOf(symbolHash((Symbol) obj));
                } catch (ClassCastException e13) {
                    throw new WrongType(e13, "symbol-hash", 1, obj);
                }
            default:
                return super.apply1(moduleMethod, obj);
        }
    }
}
