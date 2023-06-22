package gnu.kawa.lispexpr;

import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.mapping.Namespace;
import gnu.mapping.Symbol;

public class LispPackage extends Namespace {
    Namespace exported;
    NamespaceUse imported;
    NamespaceUse importing;
    LList shadowingSymbols = LList.Empty;

    public Symbol lookup(String name, int hash, boolean create) {
        Symbol sym = this.exported.lookup(name, hash, false);
        if (sym != null) {
            return sym;
        }
        Symbol sym2 = lookupInternal(name, hash);
        if (sym2 != null) {
            return sym2;
        }
        for (NamespaceUse used = this.imported; used != null; used = used.nextImported) {
            Symbol sym3 = lookup(name, hash, false);
            if (sym3 != null) {
                return sym3;
            }
        }
        if (create) {
            return add(new Symbol(this, name), hash);
        }
        return null;
    }

    public Symbol lookupPresent(String name, int hash, boolean intern) {
        Symbol sym = this.exported.lookup(name, hash, false);
        if (sym == null) {
            return super.lookup(name, hash, intern);
        }
        return sym;
    }

    public boolean isPresent(String name) {
        return lookupPresent(name, name.hashCode(), false) != null;
    }

    public boolean unintern(Symbol symbol) {
        String name = symbol.getName();
        int hash = name.hashCode();
        if (this.exported.lookup(name, hash, false) == symbol) {
            this.exported.remove(symbol);
        } else if (super.lookup(name, hash, false) != symbol) {
            return false;
        } else {
            super.remove(symbol);
        }
        symbol.setNamespace((Namespace) null);
        if (removeFromShadowingSymbols(symbol)) {
        }
        return true;
    }

    private void addToShadowingSymbols(Symbol sym) {
        Object obj = this.shadowingSymbols;
        while (obj != LList.Empty) {
            Pair p = (Pair) obj;
            if (p.getCar() != sym) {
                obj = p.getCdr();
            } else {
                return;
            }
        }
        this.shadowingSymbols = new Pair(sym, this.shadowingSymbols);
    }

    private boolean removeFromShadowingSymbols(Symbol sym) {
        Pair prev = null;
        Object s = this.shadowingSymbols;
        while (s != LList.Empty) {
            Pair p = (Pair) s;
            s = p.getCdr();
            if (p.getCar() == sym) {
                if (prev == null) {
                    this.shadowingSymbols = (LList) s;
                } else {
                    prev.setCdr(s);
                }
                return true;
            }
            prev = p;
        }
        return false;
    }

    public void shadow(String name) {
        addToShadowingSymbols(lookupPresent(name, name.hashCode(), true));
    }

    public void shadowingImport(Symbol symbol) {
        String name = symbol.getName();
        int hashCode = name.hashCode();
        Symbol old = lookupPresent(name, name.hashCode(), false);
        if (!(old == null || old == symbol)) {
            unintern(old);
        }
        addToShadowingSymbols(symbol);
    }
}
