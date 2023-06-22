package gnu.kawa.functions;

import gnu.expr.QuoteExp;
import gnu.mapping.Procedure;
import gnu.mapping.Procedure0;

public class ConstantFunction0 extends Procedure0 {
    final QuoteExp constant;
    final Object value;

    public ConstantFunction0(String name, Object value2) {
        this(name, QuoteExp.getInstance(value2));
    }

    public ConstantFunction0(String name, QuoteExp constant2) {
        super(name);
        this.value = constant2.getValue();
        this.constant = constant2;
        setProperty(Procedure.validateApplyKey, "gnu.kawa.functions.CompileMisc:validateApplyConstantFunction0");
    }

    public Object apply0() {
        return this.value;
    }
}
