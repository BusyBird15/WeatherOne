package gnu.kawa.xml;

import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Method;
import gnu.bytecode.Variable;
import gnu.expr.ApplyExp;
import gnu.expr.Compilation;
import gnu.expr.ConsumerTarget;
import gnu.expr.Expression;
import gnu.expr.Target;
import gnu.lists.Consumer;
import gnu.lists.TreeList;
import gnu.lists.XConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Values;
import gnu.xml.XMLFilter;

public class MakeWithBaseUri extends NodeConstructor {
    static final Method beginEntityMethod = typeXConsumer.getDeclaredMethod("beginEntity", 1);
    static final Method endEntityMethod = typeXConsumer.getDeclaredMethod("endEntity", 0);
    public static final MakeWithBaseUri makeWithBaseUri = new MakeWithBaseUri();
    static final ClassType typeXConsumer = ClassType.make("gnu.lists.XConsumer");

    public int numArgs() {
        return 8194;
    }

    public void apply(CallContext ctx) {
        Consumer saved = ctx.consumer;
        XMLFilter pushNodeContext = NodeConstructor.pushNodeContext(ctx);
        Object baseUri = ctx.getNextArg();
        Object node = ctx.getNextArg();
        if (pushNodeContext instanceof XConsumer) {
            pushNodeContext.beginEntity(baseUri);
        }
        try {
            Values.writeValues(node, pushNodeContext);
        } finally {
            if (pushNodeContext instanceof XConsumer) {
                pushNodeContext.endEntity();
            }
            if (pushNodeContext instanceof TreeList) {
                ((TreeList) pushNodeContext).dump();
            }
            NodeConstructor.popNodeContext(saved, ctx);
        }
    }

    public void compileToNode(ApplyExp exp, Compilation comp, ConsumerTarget target) {
        Variable consumer = target.getConsumerVariable();
        Expression[] args = exp.getArgs();
        int length = args.length;
        CodeAttr code = comp.getCode();
        code.emitLoad(consumer);
        args[0].compile(comp, Target.pushObject);
        code.emitInvokeInterface(beginEntityMethod);
        compileChild(args[1], comp, target);
        code.emitLoad(consumer);
        code.emitInvokeInterface(endEntityMethod);
    }
}
