package gnu.ecmascript;

import gnu.mapping.InPort;
import gnu.mapping.Procedure1;

class Prompter extends Procedure1 {
    Prompter() {
    }

    /* access modifiers changed from: package-private */
    public String prompt(InPort port) {
        return "(EcmaScript:" + (port.getLineNumber() + 1) + ") ";
    }

    public Object apply1(Object arg) {
        return prompt((InPort) arg);
    }
}
