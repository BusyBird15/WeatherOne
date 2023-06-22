package gnu.bytecode;

import java.util.ArrayList;
import java.util.Iterator;

public class Label {
    int first_fixup;
    Type[] localTypes;
    boolean needsStackMapEntry;
    int position;
    Type[] stackTypes;
    private Object[] typeChangeListeners;

    public final boolean defined() {
        return this.position >= 0;
    }

    public Label() {
        this(-1);
    }

    public Label(CodeAttr code) {
        this(-1);
    }

    public Label(int position2) {
        this.position = position2;
    }

    /* access modifiers changed from: package-private */
    public Type mergeTypes(Type t1, Type t2) {
        if ((t1 instanceof PrimType) != (t2 instanceof PrimType)) {
            return null;
        }
        return Type.lowestCommonSuperType(t1, t2);
    }

    /* access modifiers changed from: package-private */
    public void setTypes(Type[] locals, int usedLocals, Type[] stack, int usedStack) {
        while (usedLocals > 0 && locals[usedLocals - 1] == null) {
            usedLocals--;
        }
        if (this.stackTypes == null) {
            if (usedStack == 0) {
                this.stackTypes = Type.typeArray0;
            } else {
                this.stackTypes = new Type[usedStack];
                System.arraycopy(stack, 0, this.stackTypes, 0, usedStack);
            }
            if (usedLocals == 0) {
                this.localTypes = Type.typeArray0;
                return;
            }
            this.localTypes = new Type[usedLocals];
            System.arraycopy(locals, 0, this.localTypes, 0, usedLocals);
            return;
        }
        int SP = usedStack;
        if (SP != this.stackTypes.length) {
            throw new InternalError("inconsistent stack length");
        }
        for (int i = 0; i < SP; i++) {
            this.stackTypes[i] = mergeTypes(this.stackTypes[i], stack[i]);
        }
        int min = usedLocals < this.localTypes.length ? usedLocals : this.localTypes.length;
        for (int i2 = 0; i2 < min; i2++) {
            mergeLocalType(i2, locals[i2]);
        }
        for (int i3 = usedLocals; i3 < this.localTypes.length; i3++) {
            this.localTypes[i3] = null;
        }
    }

    public void setTypes(CodeAttr code) {
        addTypeChangeListeners(code);
        if (this.stackTypes == null || code.SP == this.stackTypes.length) {
            setTypes(code.local_types, code.local_types == null ? 0 : code.local_types.length, code.stack_types, code.SP);
            return;
        }
        throw new InternalError();
    }

    public void setTypes(Label other) {
        setTypes(other.localTypes, other.localTypes.length, other.stackTypes, other.stackTypes.length);
    }

    private void mergeLocalType(int varnum, Type newType) {
        Type oldLocal = this.localTypes[varnum];
        Type newLocal = mergeTypes(oldLocal, newType);
        this.localTypes[varnum] = newLocal;
        if (newLocal != oldLocal) {
            notifyTypeChangeListeners(varnum, newLocal);
        }
    }

    private void notifyTypeChangeListeners(int varnum, Type newType) {
        Object listeners;
        Object[] arr = this.typeChangeListeners;
        if (arr != null && arr.length > varnum && (listeners = arr[varnum]) != null) {
            if (listeners instanceof Label) {
                ((Label) listeners).mergeLocalType(varnum, newType);
            } else {
                Iterator i$ = ((ArrayList) listeners).iterator();
                while (i$.hasNext()) {
                    ((Label) i$.next()).mergeLocalType(varnum, newType);
                }
            }
            if (newType == null) {
                arr[varnum] = null;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addTypeChangeListener(int varnum, Label listener) {
        Object[] arr;
        ArrayList<Label> list;
        Object[] arr2 = this.typeChangeListeners;
        if (arr2 == null) {
            Object[] arr3 = new Object[(varnum + 10)];
            this.typeChangeListeners = arr3;
            arr = arr3;
        } else {
            int length = arr2.length;
            arr = arr2;
            if (length <= varnum) {
                Object[] arr4 = new Object[(varnum + 10)];
                System.arraycopy(this.typeChangeListeners, 0, arr4, 0, this.typeChangeListeners.length);
                this.typeChangeListeners = arr4;
                arr = arr4;
            }
        }
        Object set = arr[varnum];
        if (set == null) {
            arr[varnum] = listener;
            return;
        }
        if (set instanceof Label) {
            list = new ArrayList<>();
            list.add((Label) set);
            arr[varnum] = list;
        } else {
            list = set;
        }
        list.add(listener);
    }

    /* access modifiers changed from: package-private */
    public void addTypeChangeListeners(CodeAttr code) {
        if (code.local_types != null && code.previousLabel != null) {
            int len = code.local_types.length;
            for (int varnum = 0; varnum < len; varnum++) {
                if (code.local_types[varnum] != null && (code.varsSetInCurrentBlock == null || code.varsSetInCurrentBlock.length <= varnum || !code.varsSetInCurrentBlock[varnum])) {
                    code.previousLabel.addTypeChangeListener(varnum, this);
                }
            }
        }
    }

    public void defineRaw(CodeAttr code) {
        if (this.position >= 0) {
            throw new Error("label definition more than once");
        }
        this.position = code.PC;
        this.first_fixup = code.fixup_count;
        if (this.first_fixup >= 0) {
            code.fixupAdd(1, this);
        }
    }

    public void define(CodeAttr code) {
        if (code.reachableHere()) {
            setTypes(code);
        } else if (this.localTypes != null) {
            int i = this.localTypes.length;
            while (true) {
                i--;
                if (i < 0) {
                    break;
                } else if (this.localTypes[i] != null && (code.locals.used == null || code.locals.used[i] == null)) {
                    this.localTypes[i] = null;
                }
            }
        }
        code.previousLabel = this;
        code.varsSetInCurrentBlock = null;
        defineRaw(code);
        if (this.localTypes != null) {
            code.setTypes(this);
        }
        code.setReachable(true);
    }
}
