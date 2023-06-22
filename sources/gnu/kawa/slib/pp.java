package gnu.kawa.slib;

import androidx.fragment.app.FragmentTransaction;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.lists.Consumer;
import gnu.mapping.CallContext;
import gnu.mapping.SimpleSymbol;
import gnu.math.IntNum;
import kawa.lib.ports;

/* compiled from: pp.scm */
public class pp extends ModuleBody {
    public static final pp $instance = new pp();
    static final IntNum Lit0 = IntNum.make(79);
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("pretty-print").readResolve());
    public static final ModuleMethod pretty$Mnprint = new ModuleMethod($instance, 2, Lit1, 8193);

    static {
        $instance.run();
    }

    public pp() {
        ModuleInfo.register(this);
    }

    public static Object prettyPrint(Object obj) {
        return prettyPrint(obj, ports.current$Mnoutput$Mnport.apply0());
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
    }

    /* compiled from: pp.scm */
    public class frame extends ModuleBody {
        final ModuleMethod lambda$Fn1;
        Object port;

        public frame() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 1, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pp.scm:9");
            this.lambda$Fn1 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 1 ? lambda1(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Boolean lambda1(Object s) {
            ports.display(s, this.port);
            return Boolean.TRUE;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 1) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object prettyPrint(Object obj, Object port) {
        frame frame2 = new frame();
        frame2.port = port;
        return genwrite.genericWrite(obj, Boolean.FALSE, Lit0, frame2.lambda$Fn1);
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        return moduleMethod.selector == 2 ? prettyPrint(obj) : super.apply1(moduleMethod, obj);
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        return moduleMethod.selector == 2 ? prettyPrint(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        if (moduleMethod.selector != 2) {
            return super.match1(moduleMethod, obj, callContext);
        }
        callContext.value1 = obj;
        callContext.proc = moduleMethod;
        callContext.pc = 1;
        return 0;
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        if (moduleMethod.selector != 2) {
            return super.match2(moduleMethod, obj, obj2, callContext);
        }
        callContext.value1 = obj;
        callContext.value2 = obj2;
        callContext.proc = moduleMethod;
        callContext.pc = 2;
        return 0;
    }
}
