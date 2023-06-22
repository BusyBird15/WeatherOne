package gnu.expr;

import gnu.mapping.OutPort;
import gnu.text.SourceMessages;

public class ErrorExp extends Expression {
    String message;

    public ErrorExp(String message2) {
        this.message = message2;
    }

    public ErrorExp(String message2, SourceMessages messages) {
        messages.error('e', message2);
        this.message = message2;
    }

    public ErrorExp(String message2, Compilation comp) {
        comp.getMessages().error('e', message2);
        this.message = message2;
    }

    /* access modifiers changed from: protected */
    public boolean mustCompile() {
        return false;
    }

    public void print(OutPort out) {
        out.startLogicalBlock("(Error", false, ")");
        out.writeSpaceLinear();
        out.print(this.message);
        out.endLogicalBlock(")");
    }

    public void compile(Compilation comp, Target target) {
        throw new Error(comp.getFileName() + ":" + comp.getLineNumber() + ": internal error: compiling error expression: " + this.message);
    }
}
