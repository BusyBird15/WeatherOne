package gnu.kawa.slib;

import androidx.fragment.app.FragmentTransaction;
import com.google.appinventor.components.common.ComponentConstants;
import gnu.expr.GenericProc;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.AddOp;
import gnu.kawa.functions.Apply;
import gnu.kawa.functions.Map;
import gnu.kawa.functions.MultiplyOp;
import gnu.kawa.lispexpr.LangObjType;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.Procedure;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import gnu.math.Numeric;
import kawa.lang.Continuation;
import kawa.lang.Macro;
import kawa.lang.SyntaxPattern;
import kawa.lang.SyntaxRule;
import kawa.lang.SyntaxRules;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.numbers;
import kawa.standard.Scheme;
import kawa.standard.append;
import kawa.standard.call_with_values;

/* compiled from: srfi1.scm */
public class srfi1 extends ModuleBody {
    public static final Macro $Pcevery = Macro.make(Lit84, Lit85, $instance);
    public static final int $Pcprovide$Pclist$Mnlib = 123;
    public static final int $Pcprovide$Pcsrfi$Mn1 = 123;
    public static final srfi1 $instance = new srfi1();
    static final IntNum Lit0 = IntNum.make(0);
    static final IntNum Lit1 = IntNum.make(1);
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("proper-list?").readResolve());
    static final SimpleSymbol Lit100 = ((SimpleSymbol) new SimpleSymbol("tail").readResolve());
    static final SimpleSymbol Lit101 = ((SimpleSymbol) new SimpleSymbol("head").readResolve());
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol("lp").readResolve());
    static final SimpleSymbol Lit103 = ((SimpleSymbol) new SimpleSymbol("car").readResolve());
    static final SimpleSymbol Lit104 = ((SimpleSymbol) new SimpleSymbol("cdr").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("dotted-list?").readResolve());
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("circular-list?").readResolve());
    static final SimpleSymbol Lit13 = ((SimpleSymbol) new SimpleSymbol("not-pair?").readResolve());
    static final SimpleSymbol Lit14;
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("list=").readResolve());
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("length+").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("zip").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("fifth").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("sixth").readResolve());
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("tmp").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("seventh").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("eighth").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("ninth").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("tenth").readResolve());
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("car+cdr").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("take").readResolve());
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("drop").readResolve());
    static final SimpleSymbol Lit27 = ((SimpleSymbol) new SimpleSymbol("take!").readResolve());
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("take-right").readResolve());
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("drop-right").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("xcons").readResolve());
    static final SimpleSymbol Lit30 = ((SimpleSymbol) new SimpleSymbol("drop-right!").readResolve());
    static final SimpleSymbol Lit31 = ((SimpleSymbol) new SimpleSymbol("split-at").readResolve());
    static final SimpleSymbol Lit32 = ((SimpleSymbol) new SimpleSymbol("split-at!").readResolve());
    static final SimpleSymbol Lit33 = ((SimpleSymbol) new SimpleSymbol("last").readResolve());
    static final SimpleSymbol Lit34 = ((SimpleSymbol) new SimpleSymbol("last-pair").readResolve());
    static final SimpleSymbol Lit35 = ((SimpleSymbol) new SimpleSymbol("unzip1").readResolve());
    static final SimpleSymbol Lit36 = ((SimpleSymbol) new SimpleSymbol("unzip2").readResolve());
    static final SimpleSymbol Lit37 = ((SimpleSymbol) new SimpleSymbol("unzip3").readResolve());
    static final SimpleSymbol Lit38 = ((SimpleSymbol) new SimpleSymbol("unzip4").readResolve());
    static final SimpleSymbol Lit39 = ((SimpleSymbol) new SimpleSymbol("unzip5").readResolve());
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol("make-list").readResolve());
    static final SimpleSymbol Lit40 = ((SimpleSymbol) new SimpleSymbol("append!").readResolve());
    static final SimpleSymbol Lit41 = ((SimpleSymbol) new SimpleSymbol("append-reverse").readResolve());
    static final SimpleSymbol Lit42 = ((SimpleSymbol) new SimpleSymbol("append-reverse!").readResolve());
    static final SimpleSymbol Lit43 = ((SimpleSymbol) new SimpleSymbol("concatenate").readResolve());
    static final SimpleSymbol Lit44 = ((SimpleSymbol) new SimpleSymbol("concatenate!").readResolve());
    static final SimpleSymbol Lit45 = ((SimpleSymbol) new SimpleSymbol("count").readResolve());
    static final SimpleSymbol Lit46 = ((SimpleSymbol) new SimpleSymbol("unfold-right").readResolve());
    static final SimpleSymbol Lit47 = ((SimpleSymbol) new SimpleSymbol("unfold").readResolve());
    static final SimpleSymbol Lit48 = ((SimpleSymbol) new SimpleSymbol("fold").readResolve());
    static final SimpleSymbol Lit49 = ((SimpleSymbol) new SimpleSymbol("fold-right").readResolve());
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("list-tabulate").readResolve());
    static final SimpleSymbol Lit50 = ((SimpleSymbol) new SimpleSymbol("pair-fold-right").readResolve());
    static final SimpleSymbol Lit51 = ((SimpleSymbol) new SimpleSymbol("pair-fold").readResolve());
    static final SimpleSymbol Lit52 = ((SimpleSymbol) new SimpleSymbol("reduce").readResolve());
    static final SimpleSymbol Lit53 = ((SimpleSymbol) new SimpleSymbol("reduce-right").readResolve());
    static final SimpleSymbol Lit54 = ((SimpleSymbol) new SimpleSymbol("append-map").readResolve());
    static final SimpleSymbol Lit55 = ((SimpleSymbol) new SimpleSymbol("append-map!").readResolve());
    static final SimpleSymbol Lit56 = ((SimpleSymbol) new SimpleSymbol("pair-for-each").readResolve());
    static final SimpleSymbol Lit57 = ((SimpleSymbol) new SimpleSymbol("map!").readResolve());
    static final SimpleSymbol Lit58 = ((SimpleSymbol) new SimpleSymbol("filter-map").readResolve());
    static final SimpleSymbol Lit59 = ((SimpleSymbol) new SimpleSymbol("filter").readResolve());
    static final SimpleSymbol Lit6 = ((SimpleSymbol) new SimpleSymbol("cons*").readResolve());
    static final SimpleSymbol Lit60 = ((SimpleSymbol) new SimpleSymbol("filter!").readResolve());
    static final SimpleSymbol Lit61 = ((SimpleSymbol) new SimpleSymbol("partition").readResolve());
    static final SimpleSymbol Lit62 = ((SimpleSymbol) new SimpleSymbol("partition!").readResolve());
    static final SimpleSymbol Lit63 = ((SimpleSymbol) new SimpleSymbol("remove").readResolve());
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("remove!").readResolve());
    static final SimpleSymbol Lit65 = ((SimpleSymbol) new SimpleSymbol("delete").readResolve());
    static final SimpleSymbol Lit66 = ((SimpleSymbol) new SimpleSymbol("delete!").readResolve());
    static final SimpleSymbol Lit67 = ((SimpleSymbol) new SimpleSymbol("delete-duplicates").readResolve());
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("delete-duplicates!").readResolve());
    static final SimpleSymbol Lit69 = ((SimpleSymbol) new SimpleSymbol("alist-cons").readResolve());
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("list-copy").readResolve());
    static final SimpleSymbol Lit70 = ((SimpleSymbol) new SimpleSymbol("alist-copy").readResolve());
    static final SimpleSymbol Lit71 = ((SimpleSymbol) new SimpleSymbol("alist-delete").readResolve());
    static final SimpleSymbol Lit72 = ((SimpleSymbol) new SimpleSymbol("alist-delete!").readResolve());
    static final SimpleSymbol Lit73 = ((SimpleSymbol) new SimpleSymbol("find").readResolve());
    static final SimpleSymbol Lit74 = ((SimpleSymbol) new SimpleSymbol("find-tail").readResolve());
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("take-while").readResolve());
    static final SimpleSymbol Lit76 = ((SimpleSymbol) new SimpleSymbol("drop-while").readResolve());
    static final SimpleSymbol Lit77 = ((SimpleSymbol) new SimpleSymbol("take-while!").readResolve());
    static final SimpleSymbol Lit78 = ((SimpleSymbol) new SimpleSymbol("span").readResolve());
    static final SimpleSymbol Lit79 = ((SimpleSymbol) new SimpleSymbol("span!").readResolve());
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("iota").readResolve());
    static final SimpleSymbol Lit80 = ((SimpleSymbol) new SimpleSymbol("break").readResolve());
    static final SimpleSymbol Lit81 = ((SimpleSymbol) new SimpleSymbol("break!").readResolve());
    static final SimpleSymbol Lit82 = ((SimpleSymbol) new SimpleSymbol("any").readResolve());
    static final SimpleSymbol Lit83 = ((SimpleSymbol) new SimpleSymbol("every").readResolve());
    static final SimpleSymbol Lit84;
    static final SyntaxRules Lit85;
    static final SimpleSymbol Lit86 = ((SimpleSymbol) new SimpleSymbol("list-index").readResolve());
    static final SimpleSymbol Lit87 = ((SimpleSymbol) new SimpleSymbol("lset<=").readResolve());
    static final SimpleSymbol Lit88 = ((SimpleSymbol) new SimpleSymbol("lset=").readResolve());
    static final SimpleSymbol Lit89 = ((SimpleSymbol) new SimpleSymbol("lset-adjoin").readResolve());
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("circular-list").readResolve());
    static final SimpleSymbol Lit90 = ((SimpleSymbol) new SimpleSymbol("lset-union").readResolve());
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol("lset-union!").readResolve());
    static final SimpleSymbol Lit92 = ((SimpleSymbol) new SimpleSymbol("lset-intersection").readResolve());
    static final SimpleSymbol Lit93 = ((SimpleSymbol) new SimpleSymbol("lset-intersection!").readResolve());
    static final SimpleSymbol Lit94 = ((SimpleSymbol) new SimpleSymbol("lset-difference").readResolve());
    static final SimpleSymbol Lit95 = ((SimpleSymbol) new SimpleSymbol("lset-difference!").readResolve());
    static final SimpleSymbol Lit96 = ((SimpleSymbol) new SimpleSymbol("lset-xor").readResolve());
    static final SimpleSymbol Lit97 = ((SimpleSymbol) new SimpleSymbol("lset-xor!").readResolve());
    static final SimpleSymbol Lit98 = ((SimpleSymbol) new SimpleSymbol("lset-diff+intersection").readResolve());
    static final SimpleSymbol Lit99 = ((SimpleSymbol) new SimpleSymbol("lset-diff+intersection!").readResolve());
    public static final ModuleMethod alist$Mncons;
    public static final ModuleMethod alist$Mncopy;
    public static final ModuleMethod alist$Mndelete;
    public static final ModuleMethod alist$Mndelete$Ex;
    public static final ModuleMethod any;
    public static final ModuleMethod append$Ex;
    public static final ModuleMethod append$Mnmap;
    public static final ModuleMethod append$Mnmap$Ex;
    public static final ModuleMethod append$Mnreverse;
    public static final ModuleMethod append$Mnreverse$Ex;

    /* renamed from: break  reason: not valid java name */
    public static final ModuleMethod f1break;
    public static final ModuleMethod break$Ex;
    public static final ModuleMethod car$Plcdr;
    public static final ModuleMethod circular$Mnlist;
    public static final ModuleMethod circular$Mnlist$Qu;
    public static final ModuleMethod concatenate;
    public static final ModuleMethod concatenate$Ex;
    public static final ModuleMethod cons$St;
    public static final ModuleMethod count;
    public static final ModuleMethod delete;
    public static final ModuleMethod delete$Ex;
    public static final ModuleMethod delete$Mnduplicates;
    public static final ModuleMethod delete$Mnduplicates$Ex;
    public static final ModuleMethod dotted$Mnlist$Qu;
    public static final ModuleMethod drop;
    public static final ModuleMethod drop$Mnright;
    public static final ModuleMethod drop$Mnright$Ex;
    public static final ModuleMethod drop$Mnwhile;
    public static final ModuleMethod eighth;
    public static final ModuleMethod every;
    public static final ModuleMethod fifth;
    public static final ModuleMethod filter;
    public static final ModuleMethod filter$Ex;
    public static final ModuleMethod filter$Mnmap;
    public static final ModuleMethod find;
    public static final ModuleMethod find$Mntail;
    public static GenericProc first;
    public static final ModuleMethod fold;
    public static final ModuleMethod fold$Mnright;
    public static GenericProc fourth;
    public static final ModuleMethod iota;
    static final ModuleMethod lambda$Fn64;
    static final ModuleMethod lambda$Fn78;
    public static final ModuleMethod last;
    public static final ModuleMethod last$Mnpair;
    public static final ModuleMethod length$Pl;
    public static final ModuleMethod list$Eq;
    public static final ModuleMethod list$Mncopy;
    public static final ModuleMethod list$Mnindex;
    public static final ModuleMethod list$Mntabulate;
    public static final ModuleMethod lset$Eq;
    public static final ModuleMethod lset$Ls$Eq;
    public static final ModuleMethod lset$Mnadjoin;
    public static final ModuleMethod lset$Mndiff$Plintersection;
    public static final ModuleMethod lset$Mndiff$Plintersection$Ex;
    public static final ModuleMethod lset$Mndifference;
    public static final ModuleMethod lset$Mndifference$Ex;
    public static final ModuleMethod lset$Mnintersection;
    public static final ModuleMethod lset$Mnintersection$Ex;
    public static final ModuleMethod lset$Mnunion;
    public static final ModuleMethod lset$Mnunion$Ex;
    public static final ModuleMethod lset$Mnxor;
    public static final ModuleMethod lset$Mnxor$Ex;
    public static final ModuleMethod make$Mnlist;
    public static final ModuleMethod map$Ex;
    public static Map map$Mnin$Mnorder;
    public static final ModuleMethod ninth;
    public static final ModuleMethod not$Mnpair$Qu;
    public static final ModuleMethod null$Mnlist$Qu;
    public static final ModuleMethod pair$Mnfold;
    public static final ModuleMethod pair$Mnfold$Mnright;
    public static final ModuleMethod pair$Mnfor$Mneach;
    public static final ModuleMethod partition;
    public static final ModuleMethod partition$Ex;
    public static final ModuleMethod proper$Mnlist$Qu;
    public static final ModuleMethod reduce;
    public static final ModuleMethod reduce$Mnright;
    public static final ModuleMethod remove;
    public static final ModuleMethod remove$Ex;
    public static GenericProc second;
    public static final ModuleMethod seventh;
    public static final ModuleMethod sixth;
    public static final ModuleMethod span;
    public static final ModuleMethod span$Ex;
    public static final ModuleMethod split$Mnat;
    public static final ModuleMethod split$Mnat$Ex;
    public static final ModuleMethod take;
    public static final ModuleMethod take$Ex;
    public static final ModuleMethod take$Mnright;
    public static final ModuleMethod take$Mnwhile;
    public static final ModuleMethod take$Mnwhile$Ex;
    public static final ModuleMethod tenth;
    public static GenericProc third;
    public static final ModuleMethod unfold;
    public static final ModuleMethod unfold$Mnright;
    public static final ModuleMethod unzip1;
    public static final ModuleMethod unzip2;
    public static final ModuleMethod unzip3;
    public static final ModuleMethod unzip4;
    public static final ModuleMethod unzip5;
    public static final ModuleMethod xcons;
    public static final ModuleMethod zip;

    /* compiled from: srfi1.scm */
    public class frame62 extends ModuleBody {
        Object cars$Mnfinal;
    }

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol("%every").readResolve();
        Lit84 = simpleSymbol;
        SyntaxPattern syntaxPattern = new SyntaxPattern("\f\u0018\f\u0007\f\u000f\b", new Object[0], 2);
        SimpleSymbol simpleSymbol2 = (SimpleSymbol) new SimpleSymbol("null-list?").readResolve();
        Lit14 = simpleSymbol2;
        Lit85 = new SyntaxRules(new Object[]{simpleSymbol}, new SyntaxRule[]{new SyntaxRule(syntaxPattern, "\u0001\u0001", "\u0011\u0018\u0004\u0011\u0018\f¡I\u0011\u0018\u0014\b\u0011\u0018\u001c\b\u000b\b\u0011\u0018$\b\u0011\u0018,\b\u000b\b\u0011\u00184\u0011\u0018<!\t\u0003\u0018D\u0018L", new Object[]{(SimpleSymbol) new SimpleSymbol("let").readResolve(), Lit102, Lit101, Lit103, Lit100, Lit104, (SimpleSymbol) new SimpleSymbol("and").readResolve(), PairWithPosition.make(simpleSymbol2, PairWithPosition.make(Lit100, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722136), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722124), PairWithPosition.make(Lit101, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722148), PairWithPosition.make(PairWithPosition.make(Lit102, PairWithPosition.make(PairWithPosition.make(Lit103, PairWithPosition.make(Lit100, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722163), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722158), PairWithPosition.make(PairWithPosition.make(Lit104, PairWithPosition.make(Lit100, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722174), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722169), LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722169), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722158), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722154), LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm", 5722154)}, 0)}, 2);
        srfi1 srfi1 = $instance;
        xcons = new ModuleMethod(srfi1, 78, Lit3, 8194);
        make$Mnlist = new ModuleMethod(srfi1, 79, Lit4, -4095);
        list$Mntabulate = new ModuleMethod(srfi1, 80, Lit5, 8194);
        cons$St = new ModuleMethod(srfi1, 81, Lit6, -4096);
        list$Mncopy = new ModuleMethod(srfi1, 82, Lit7, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        iota = new ModuleMethod(srfi1, 83, Lit8, 12289);
        circular$Mnlist = new ModuleMethod(srfi1, 86, Lit9, -4095);
        proper$Mnlist$Qu = new ModuleMethod(srfi1, 87, Lit10, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        dotted$Mnlist$Qu = new ModuleMethod(srfi1, 88, Lit11, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        circular$Mnlist$Qu = new ModuleMethod(srfi1, 89, Lit12, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        not$Mnpair$Qu = new ModuleMethod(srfi1, 90, Lit13, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        null$Mnlist$Qu = new ModuleMethod(srfi1, 91, Lit14, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        list$Eq = new ModuleMethod(srfi1, 92, Lit15, -4095);
        length$Pl = new ModuleMethod(srfi1, 93, Lit16, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        zip = new ModuleMethod(srfi1, 94, Lit17, -4095);
        fifth = new ModuleMethod(srfi1, 95, Lit18, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        sixth = new ModuleMethod(srfi1, 96, Lit19, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        seventh = new ModuleMethod(srfi1, 97, Lit20, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        eighth = new ModuleMethod(srfi1, 98, Lit21, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ninth = new ModuleMethod(srfi1, 99, Lit22, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        tenth = new ModuleMethod(srfi1, 100, Lit23, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        car$Plcdr = new ModuleMethod(srfi1, 101, Lit24, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        take = new ModuleMethod(srfi1, 102, Lit25, 8194);
        drop = new ModuleMethod(srfi1, 103, Lit26, 8194);
        take$Ex = new ModuleMethod(srfi1, 104, Lit27, 8194);
        take$Mnright = new ModuleMethod(srfi1, 105, Lit28, 8194);
        drop$Mnright = new ModuleMethod(srfi1, 106, Lit29, 8194);
        drop$Mnright$Ex = new ModuleMethod(srfi1, 107, Lit30, 8194);
        split$Mnat = new ModuleMethod(srfi1, 108, Lit31, 8194);
        split$Mnat$Ex = new ModuleMethod(srfi1, 109, Lit32, 8194);
        last = new ModuleMethod(srfi1, 110, Lit33, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        last$Mnpair = new ModuleMethod(srfi1, 111, Lit34, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        unzip1 = new ModuleMethod(srfi1, 112, Lit35, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        unzip2 = new ModuleMethod(srfi1, 113, Lit36, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        unzip3 = new ModuleMethod(srfi1, 114, Lit37, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        unzip4 = new ModuleMethod(srfi1, 115, Lit38, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        unzip5 = new ModuleMethod(srfi1, 116, Lit39, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        append$Ex = new ModuleMethod(srfi1, 117, Lit40, -4096);
        append$Mnreverse = new ModuleMethod(srfi1, 118, Lit41, 8194);
        append$Mnreverse$Ex = new ModuleMethod(srfi1, 119, Lit42, 8194);
        concatenate = new ModuleMethod(srfi1, 120, Lit43, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        concatenate$Ex = new ModuleMethod(srfi1, 121, Lit44, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        count = new ModuleMethod(srfi1, 122, Lit45, -4094);
        unfold$Mnright = new ModuleMethod(srfi1, 123, Lit46, 20484);
        unfold = new ModuleMethod(srfi1, 125, Lit47, -4092);
        fold = new ModuleMethod(srfi1, 126, Lit48, -4093);
        fold$Mnright = new ModuleMethod(srfi1, 127, Lit49, -4093);
        pair$Mnfold$Mnright = new ModuleMethod(srfi1, 128, Lit50, -4093);
        pair$Mnfold = new ModuleMethod(srfi1, 129, Lit51, -4093);
        reduce = new ModuleMethod(srfi1, 130, Lit52, 12291);
        reduce$Mnright = new ModuleMethod(srfi1, 131, Lit53, 12291);
        append$Mnmap = new ModuleMethod(srfi1, 132, Lit54, -4094);
        append$Mnmap$Ex = new ModuleMethod(srfi1, 133, Lit55, -4094);
        pair$Mnfor$Mneach = new ModuleMethod(srfi1, 134, Lit56, -4094);
        map$Ex = new ModuleMethod(srfi1, 135, Lit57, -4094);
        filter$Mnmap = new ModuleMethod(srfi1, 136, Lit58, -4094);
        filter = new ModuleMethod(srfi1, 137, Lit59, 8194);
        filter$Ex = new ModuleMethod(srfi1, 138, Lit60, 8194);
        partition = new ModuleMethod(srfi1, 139, Lit61, 8194);
        partition$Ex = new ModuleMethod(srfi1, 140, Lit62, 8194);
        remove = new ModuleMethod(srfi1, 141, Lit63, 8194);
        remove$Ex = new ModuleMethod(srfi1, 142, Lit64, 8194);
        delete = new ModuleMethod(srfi1, 143, Lit65, 12290);
        delete$Ex = new ModuleMethod(srfi1, 145, Lit66, 12290);
        delete$Mnduplicates = new ModuleMethod(srfi1, 147, Lit67, 8193);
        delete$Mnduplicates$Ex = new ModuleMethod(srfi1, 149, Lit68, 8193);
        alist$Mncons = new ModuleMethod(srfi1, 151, Lit69, 12291);
        alist$Mncopy = new ModuleMethod(srfi1, 152, Lit70, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        alist$Mndelete = new ModuleMethod(srfi1, 153, Lit71, 12290);
        alist$Mndelete$Ex = new ModuleMethod(srfi1, 155, Lit72, 12290);
        find = new ModuleMethod(srfi1, 157, Lit73, 8194);
        find$Mntail = new ModuleMethod(srfi1, 158, Lit74, 8194);
        take$Mnwhile = new ModuleMethod(srfi1, 159, Lit75, 8194);
        drop$Mnwhile = new ModuleMethod(srfi1, ComponentConstants.TEXTBOX_PREFERRED_WIDTH, Lit76, 8194);
        take$Mnwhile$Ex = new ModuleMethod(srfi1, 161, Lit77, 8194);
        span = new ModuleMethod(srfi1, 162, Lit78, 8194);
        span$Ex = new ModuleMethod(srfi1, 163, Lit79, 8194);
        f1break = new ModuleMethod(srfi1, 164, Lit80, 8194);
        break$Ex = new ModuleMethod(srfi1, 165, Lit81, 8194);
        any = new ModuleMethod(srfi1, 166, Lit82, -4094);
        every = new ModuleMethod(srfi1, 167, Lit83, -4094);
        list$Mnindex = new ModuleMethod(srfi1, 168, Lit86, -4094);
        lset$Ls$Eq = new ModuleMethod(srfi1, 169, Lit87, -4095);
        lset$Eq = new ModuleMethod(srfi1, 170, Lit88, -4095);
        lset$Mnadjoin = new ModuleMethod(srfi1, 171, Lit89, -4094);
        lset$Mnunion = new ModuleMethod(srfi1, 172, Lit90, -4095);
        lset$Mnunion$Ex = new ModuleMethod(srfi1, 173, Lit91, -4095);
        lset$Mnintersection = new ModuleMethod(srfi1, 174, Lit92, -4094);
        lset$Mnintersection$Ex = new ModuleMethod(srfi1, 175, Lit93, -4094);
        lset$Mndifference = new ModuleMethod(srfi1, 176, Lit94, -4094);
        lset$Mndifference$Ex = new ModuleMethod(srfi1, 177, Lit95, -4094);
        lset$Mnxor = new ModuleMethod(srfi1, 178, Lit96, -4095);
        lset$Mnxor$Ex = new ModuleMethod(srfi1, 179, Lit97, -4095);
        lset$Mndiff$Plintersection = new ModuleMethod(srfi1, 180, Lit98, -4094);
        lset$Mndiff$Plintersection$Ex = new ModuleMethod(srfi1, 181, Lit99, -4094);
        lambda$Fn64 = new ModuleMethod(srfi1, 182, (Object) null, 8194);
        lambda$Fn78 = new ModuleMethod(srfi1, 183, (Object) null, 8194);
        $instance.run();
    }

    public srfi1() {
        ModuleInfo.register(this);
    }

    public static Object alistDelete(Object obj, Object obj2) {
        return alistDelete(obj, obj2, Scheme.isEqual);
    }

    public static Object alistDelete$Ex(Object obj, Object obj2) {
        return alistDelete$Ex(obj, obj2, Scheme.isEqual);
    }

    public static Object delete(Object obj, Object obj2) {
        return delete(obj, obj2, Scheme.isEqual);
    }

    public static Object delete$Ex(Object obj, Object obj2) {
        return delete$Ex(obj, obj2, Scheme.isEqual);
    }

    public static Object deleteDuplicates(Object obj) {
        return deleteDuplicates(obj, Scheme.isEqual);
    }

    public static Object deleteDuplicates$Ex(Object obj) {
        return deleteDuplicates$Ex(obj, Scheme.isEqual);
    }

    public static Object iota(IntNum intNum) {
        return iota(intNum, Lit0, Lit1);
    }

    public static Object iota(IntNum intNum, Numeric numeric) {
        return iota(intNum, numeric, Lit1);
    }

    public static Object unfoldRight(Procedure procedure, Procedure procedure2, Procedure procedure3, Object obj) {
        return unfoldRight(procedure, procedure2, procedure3, obj, LList.Empty);
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
        first = lists.car;
        second = lists.cadr;
        third = lists.caddr;
        fourth = lists.cadddr;
        map$Mnin$Mnorder = Scheme.map;
    }

    public static Pair xcons(Object d, Object a) {
        return lists.cons(a, d);
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 78:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 80:
                callContext.value1 = obj;
                if (!(obj2 instanceof Procedure)) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 83:
                if (IntNum.asIntNumOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                if (Numeric.asNumericOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 102:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 103:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 104:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 105:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 106:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 107:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 108:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 109:
                callContext.value1 = obj;
                if (IntNum.asIntNumOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 118:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 119:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 137:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 138:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 139:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 140:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 141:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 142:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 143:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 145:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 147:
                callContext.value1 = obj;
                if (!(obj2 instanceof Procedure)) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 149:
                callContext.value1 = obj;
                if (!(obj2 instanceof Procedure)) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 153:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 155:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 157:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 158:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 159:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case ComponentConstants.TEXTBOX_PREFERRED_WIDTH:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 161:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 162:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 163:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 164:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 165:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 182:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 183:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            default:
                return super.match2(moduleMethod, obj, obj2, callContext);
        }
    }

    public static Object makeList$V(Object len, Object[] argsArray) {
        Object elt;
        LList maybe$Mnelt = LList.makeList(argsArray, 0);
        boolean x = ((numbers.isInteger(len) ? 1 : 0) + true) & true;
        if (!x ? Scheme.numLss.apply2(len, Lit0) != Boolean.FALSE : x) {
            misc.error$V("make-list arg#1 must be a non-negative integer", new Object[0]);
        }
        if (lists.isNull(maybe$Mnelt)) {
            elt = Boolean.FALSE;
        } else if (lists.isNull(lists.cdr.apply1(maybe$Mnelt))) {
            elt = lists.car.apply1(maybe$Mnelt);
        } else {
            elt = misc.error$V("Too many arguments to MAKE-LIST", new Object[]{lists.cons(len, maybe$Mnelt)});
        }
        Object obj = LList.Empty;
        Object i = len;
        while (Scheme.numLEq.apply2(i, Lit0) == Boolean.FALSE) {
            i = AddOp.$Mn.apply2(i, Lit1);
            obj = lists.cons(elt, obj);
        }
        return obj;
    }

    public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 79:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 81:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 86:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 92:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 94:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 117:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 122:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 123:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 125:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 126:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 127:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 128:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 129:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 132:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 133:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 134:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 135:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 136:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 166:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 167:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 168:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 169:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 170:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 171:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 172:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 173:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 174:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 175:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 176:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 177:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 178:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 179:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 180:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 181:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            default:
                return super.matchN(moduleMethod, objArr, callContext);
        }
    }

    public static Object listTabulate(Object len, Procedure proc) {
        boolean x = ((numbers.isInteger(len) ? 1 : 0) + true) & true;
        if (!x ? Scheme.numLss.apply2(len, Lit0) != Boolean.FALSE : x) {
            misc.error$V("list-tabulate arg#1 must be a non-negative integer", new Object[0]);
        }
        Object i = AddOp.$Mn.apply2(len, Lit1);
        Object obj = LList.Empty;
        while (Scheme.numLss.apply2(i, Lit0) == Boolean.FALSE) {
            Object i2 = AddOp.$Mn.apply2(i, Lit1);
            obj = lists.cons(proc.apply1(i), obj);
            i = i2;
        }
        return obj;
    }

    public static Object cons$St(Object... args) {
        return LList.consX(args);
    }

    public static LList listCopy(LList lis) {
        LList result = LList.Empty;
        LList prev = LList.Empty;
        while (lists.isPair(lis)) {
            Pair p = lists.cons(lists.car.apply1(lis), LList.Empty);
            if (prev == LList.Empty) {
                result = p;
            } else {
                try {
                    lists.setCdr$Ex((Pair) prev, p);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "set-cdr!", 1, (Object) prev);
                }
            }
            prev = p;
            lis = (LList) lists.cdr.apply1(lis);
        }
        return result;
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 82:
                if (!(obj instanceof LList)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 83:
                if (IntNum.asIntNumOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 87:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 88:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 89:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 90:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 91:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 93:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 95:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 96:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 97:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 98:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 99:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 100:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 101:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 110:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 111:
                if (!(obj instanceof Pair)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 112:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 113:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 114:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 115:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 116:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 120:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 121:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 147:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 149:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 152:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public static Object iota(IntNum count2, Numeric start, Numeric step) {
        if (IntNum.compare(count2, 0) < 0) {
            misc.error$V("Negative step count", new Object[]{iota, count2});
        }
        Object apply2 = AddOp.$Pl.apply2(start, MultiplyOp.$St.apply2(IntNum.add(count2, -1), step));
        try {
            Object obj = (Numeric) apply2;
            Object obj2 = LList.Empty;
            Object obj3 = count2;
            while (Scheme.numLEq.apply2(obj3, Lit0) == Boolean.FALSE) {
                Object apply22 = AddOp.$Mn.apply2(obj3, Lit1);
                Object val = AddOp.$Mn.apply2(obj, step);
                obj2 = lists.cons(obj, obj2);
                obj = val;
                obj3 = apply22;
            }
            return obj2;
        } catch (ClassCastException e) {
            throw new WrongType(e, "last-val", -2, apply2);
        }
    }

    public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 83:
                if (IntNum.asIntNumOrNull(obj) == null) {
                    return -786431;
                }
                callContext.value1 = obj;
                if (Numeric.asNumericOrNull(obj2) == null) {
                    return -786430;
                }
                callContext.value2 = obj2;
                if (Numeric.asNumericOrNull(obj3) == null) {
                    return -786429;
                }
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 130:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 131:
                if (!(obj instanceof Procedure)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 143:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 145:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 151:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 153:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 155:
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

    public static Pair circularList$V(Object val1, Object[] argsArray) {
        Pair ans = lists.cons(val1, LList.makeList(argsArray, 0));
        Object lastPair = lastPair(ans);
        try {
            lists.setCdr$Ex((Pair) lastPair, ans);
            return ans;
        } catch (ClassCastException e) {
            throw new WrongType(e, "set-cdr!", 1, lastPair);
        }
    }

    public static Object isProperList(Object x) {
        Object lag = x;
        while (lists.isPair(x)) {
            Object x2 = lists.cdr.apply1(x);
            if (!lists.isPair(x2)) {
                return lists.isNull(x2) ? Boolean.TRUE : Boolean.FALSE;
            }
            x = lists.cdr.apply1(x2);
            lag = lists.cdr.apply1(lag);
            boolean x3 = ((x == lag ? 1 : 0) + 1) & true;
            if (!x3) {
                return x3 ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        return lists.isNull(x) ? Boolean.TRUE : Boolean.FALSE;
    }

    public static Object isDottedList(Object x) {
        Object lag = x;
        while (lists.isPair(x)) {
            Object x2 = lists.cdr.apply1(x);
            if (!lists.isPair(x2)) {
                return lists.isNull(x2) ? Boolean.FALSE : Boolean.TRUE;
            }
            x = lists.cdr.apply1(x2);
            lag = lists.cdr.apply1(lag);
            boolean x3 = ((x == lag ? 1 : 0) + 1) & true;
            if (!x3) {
                return x3 ? Boolean.TRUE : Boolean.FALSE;
            }
        }
        return lists.isNull(x) ? Boolean.FALSE : Boolean.TRUE;
    }

    public static Object isCircularList(Object x) {
        boolean x2;
        Object lag = x;
        do {
            boolean x3 = lists.isPair(x);
            if (!x3) {
                return x3 ? Boolean.TRUE : Boolean.FALSE;
            }
            Object x4 = lists.cdr.apply1(x);
            boolean x5 = lists.isPair(x4);
            if (!x5) {
                return x5 ? Boolean.TRUE : Boolean.FALSE;
            }
            x = lists.cdr.apply1(x4);
            lag = lists.cdr.apply1(lag);
            if (x == lag) {
                x2 = true;
                continue;
            } else {
                x2 = false;
                continue;
            }
        } while (!x2);
        if (x2) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static boolean isNotPair(Object x) {
        return ((lists.isPair(x) ? 1 : 0) + true) & true;
    }

    public static Object isNullList(Object l) {
        if (l instanceof Pair) {
            return Boolean.FALSE;
        }
        if (l == LList.Empty) {
            return Boolean.TRUE;
        }
        return misc.error$V("null-list?: argument out of domain", new Object[]{l});
    }

    public static Object list$Eq$V(Object $Eq, Object[] argsArray) {
        int i;
        LList lists = LList.makeList(argsArray, 0);
        boolean x = lists.isNull(lists);
        if (!x) {
            Object list$Mna = lists.car.apply1(lists);
            Object apply1 = lists.cdr.apply1(lists);
            while (true) {
                boolean x2 = lists.isNull(apply1);
                if (x2) {
                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                }
                Object list$Mnb = lists.car.apply1(apply1);
                apply1 = lists.cdr.apply1(apply1);
                if (list$Mna == list$Mnb) {
                    list$Mna = list$Mnb;
                } else {
                    while (true) {
                        Object list$Mnb2 = list$Mnb;
                        if (isNullList(list$Mna) != Boolean.FALSE) {
                            Object x3 = isNullList(list$Mnb2);
                            if (x3 == Boolean.FALSE) {
                                return x3;
                            }
                            list$Mna = list$Mnb2;
                        } else {
                            Object isNullList = isNullList(list$Mnb2);
                            try {
                                if (isNullList != Boolean.FALSE) {
                                    i = 1;
                                } else {
                                    i = 0;
                                }
                                boolean x4 = (i + 1) & true;
                                if (!x4) {
                                    return x4 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                Object x5 = Scheme.applyToArgs.apply3($Eq, lists.car.apply1(list$Mna), lists.car.apply1(list$Mnb2));
                                if (x5 == Boolean.FALSE) {
                                    return x5;
                                }
                                list$Mna = lists.cdr.apply1(list$Mna);
                                list$Mnb = lists.cdr.apply1(list$Mnb2);
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "x", -2, isNullList);
                            }
                        }
                    }
                }
            }
        } else if (x) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public static Object length$Pl(Object x) {
        Object len = Lit0;
        Object lag = x;
        while (lists.isPair(x)) {
            Object x2 = lists.cdr.apply1(x);
            Object len2 = AddOp.$Pl.apply2(len, Lit1);
            if (!lists.isPair(x2)) {
                return len2;
            }
            x = lists.cdr.apply1(x2);
            lag = lists.cdr.apply1(lag);
            len = AddOp.$Pl.apply2(len2, Lit1);
            boolean x3 = ((x == lag ? 1 : 0) + 1) & true;
            if (!x3) {
                if (x3) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        }
        return len;
    }

    public static Object zip$V(Object list1, Object[] argsArray) {
        return Scheme.apply.apply4(Scheme.map, LangObjType.listType, list1, LList.makeList(argsArray, 0));
    }

    public static Object fifth(Object x) {
        return lists.car.apply1(lists.cddddr.apply1(x));
    }

    public static Object sixth(Object x) {
        return lists.cadr.apply1(lists.cddddr.apply1(x));
    }

    public static Object seventh(Object x) {
        return lists.caddr.apply1(lists.cddddr.apply1(x));
    }

    public static Object eighth(Object x) {
        return lists.cadddr.apply1(lists.cddddr.apply1(x));
    }

    public static Object ninth(Object x) {
        return lists.car.apply1(lists.cddddr.apply1(lists.cddddr.apply1(x)));
    }

    public static Object tenth(Object x) {
        return lists.cadr.apply1(lists.cddddr.apply1(lists.cddddr.apply1(x)));
    }

    public static Object car$PlCdr(Object pair) {
        return misc.values(lists.car.apply1(pair), lists.cdr.apply1(pair));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v3, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v4, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object take(java.lang.Object r8, gnu.math.IntNum r9) {
        /*
            r7 = 1
            gnu.lists.LList r4 = gnu.lists.LList.Empty
        L_0x0003:
            r0 = r9
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0031 }
            r3 = r0
            boolean r3 = kawa.lib.numbers.isZero(r3)
            if (r3 == 0) goto L_0x0016
            r0 = r4
            gnu.lists.LList r0 = (gnu.lists.LList) r0     // Catch:{ ClassCastException -> 0x003a }
            r3 = r0
            gnu.lists.LList r3 = kawa.lib.lists.reverse$Ex(r3)
            return r3
        L_0x0016:
            gnu.expr.GenericProc r3 = kawa.lib.lists.cdr
            java.lang.Object r1 = r3.apply1(r8)
            gnu.kawa.functions.AddOp r3 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r5 = Lit1
            java.lang.Object r9 = r3.apply2(r9, r5)
            gnu.expr.GenericProc r3 = kawa.lib.lists.car
            java.lang.Object r3 = r3.apply1(r8)
            gnu.lists.Pair r2 = kawa.lib.lists.cons(r3, r4)
            r4 = r2
            r8 = r1
            goto L_0x0003
        L_0x0031:
            r3 = move-exception
            gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
            java.lang.String r5 = "zero?"
            r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r7, (java.lang.Object) r9)
            throw r4
        L_0x003a:
            r3 = move-exception
            gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
            java.lang.String r6 = "reverse!"
            r5.<init>((java.lang.ClassCastException) r3, (java.lang.String) r6, (int) r7, (java.lang.Object) r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi1.take(java.lang.Object, gnu.math.IntNum):java.lang.Object");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v4, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object drop(java.lang.Object r5, gnu.math.IntNum r6) {
        /*
        L_0x0000:
            r0 = r6
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x001a }
            r1 = r0
            boolean r1 = kawa.lib.numbers.isZero(r1)
            if (r1 == 0) goto L_0x000b
            return r5
        L_0x000b:
            gnu.expr.GenericProc r1 = kawa.lib.lists.cdr
            java.lang.Object r5 = r1.apply1(r5)
            gnu.kawa.functions.AddOp r1 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r2 = Lit1
            java.lang.Object r6 = r1.apply2(r6, r2)
            goto L_0x0000
        L_0x001a:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "zero?"
            r4 = 1
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r4, (java.lang.Object) r6)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi1.drop(java.lang.Object, gnu.math.IntNum):java.lang.Object");
    }

    public static Object take$Ex(Object lis, IntNum k) {
        if (numbers.isZero(k)) {
            return LList.Empty;
        }
        Object drop2 = drop(lis, IntNum.add(k, -1));
        try {
            lists.setCdr$Ex((Pair) drop2, LList.Empty);
            return lis;
        } catch (ClassCastException e) {
            throw new WrongType(e, "set-cdr!", 1, drop2);
        }
    }

    public static Object takeRight(Object lis, IntNum k) {
        Object lag = lis;
        for (Object drop2 = drop(lis, k); lists.isPair(drop2); drop2 = lists.cdr.apply1(drop2)) {
            lag = lists.cdr.apply1(lag);
        }
        return lag;
    }

    public static Object dropRight(Object lis, IntNum k) {
        return lambda1recur(lis, drop(lis, k));
    }

    public static Object lambda1recur(Object lag, Object lead) {
        return lists.isPair(lead) ? lists.cons(lists.car.apply1(lag), lambda1recur(lists.cdr.apply1(lag), lists.cdr.apply1(lead))) : LList.Empty;
    }

    public static Object dropRight$Ex(Object lis, IntNum k) {
        Object lead = drop(lis, k);
        if (!lists.isPair(lead)) {
            return LList.Empty;
        }
        Object lag = lis;
        for (Object lead2 = lists.cdr.apply1(lead); lists.isPair(lead2); lead2 = lists.cdr.apply1(lead2)) {
            lag = lists.cdr.apply1(lag);
        }
        try {
            lists.setCdr$Ex((Pair) lag, LList.Empty);
            return lis;
        } catch (ClassCastException e) {
            throw new WrongType(e, "set-cdr!", 1, lag);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v3, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v4, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object splitAt(java.lang.Object r7, gnu.math.IntNum r8) {
        /*
            r6 = 1
            gnu.lists.LList r1 = gnu.lists.LList.Empty
        L_0x0003:
            r0 = r8
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x003a }
            r3 = r0
            boolean r3 = kawa.lib.numbers.isZero(r3)
            if (r3 == 0) goto L_0x0020
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r4 = 0
            gnu.lists.LList r1 = (gnu.lists.LList) r1     // Catch:{ ClassCastException -> 0x0043 }
            gnu.lists.LList r5 = kawa.lib.lists.reverse$Ex(r1)
            r3[r4] = r5
            r3[r6] = r7
            java.lang.Object r3 = kawa.lib.misc.values(r3)
            return r3
        L_0x0020:
            gnu.expr.GenericProc r3 = kawa.lib.lists.car
            java.lang.Object r3 = r3.apply1(r7)
            gnu.lists.Pair r1 = kawa.lib.lists.cons(r3, r1)
            gnu.expr.GenericProc r3 = kawa.lib.lists.cdr
            java.lang.Object r2 = r3.apply1(r7)
            gnu.kawa.functions.AddOp r3 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r4 = Lit1
            java.lang.Object r8 = r3.apply2(r8, r4)
            r7 = r2
            goto L_0x0003
        L_0x003a:
            r3 = move-exception
            gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
            java.lang.String r5 = "zero?"
            r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r6, (java.lang.Object) r8)
            throw r4
        L_0x0043:
            r3 = move-exception
            gnu.mapping.WrongType r4 = new gnu.mapping.WrongType
            java.lang.String r5 = "reverse!"
            r4.<init>((java.lang.ClassCastException) r3, (java.lang.String) r5, (int) r6, (java.lang.Object) r1)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi1.splitAt(java.lang.Object, gnu.math.IntNum):java.lang.Object");
    }

    public static Object splitAt$Ex(Object x, IntNum k) {
        if (numbers.isZero(k)) {
            return misc.values(LList.Empty, x);
        }
        Object prev = drop(x, IntNum.add(k, -1));
        Object suffix = lists.cdr.apply1(prev);
        try {
            lists.setCdr$Ex((Pair) prev, LList.Empty);
            return misc.values(x, suffix);
        } catch (ClassCastException e) {
            throw new WrongType(e, "set-cdr!", 1, prev);
        }
    }

    public static Object last(Object lis) {
        try {
            return lists.car.apply1(lastPair((Pair) lis));
        } catch (ClassCastException e) {
            throw new WrongType(e, "last-pair", 0, lis);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=gnu.lists.Pair, code=java.lang.Object, for r2v0, types: [gnu.lists.Pair] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object lastPair(java.lang.Object r2) {
        /*
        L_0x0000:
            gnu.expr.GenericProc r1 = kawa.lib.lists.cdr
            java.lang.Object r0 = r1.apply1(r2)
            boolean r1 = kawa.lib.lists.isPair(r0)
            if (r1 == 0) goto L_0x000e
            r2 = r0
            goto L_0x0000
        L_0x000e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi1.lastPair(gnu.lists.Pair):java.lang.Object");
    }

    public static LList unzip1(Object lis) {
        Object obj = LList.Empty;
        Object arg0 = lis;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                obj = Pair.make(lists.car.apply1(arg02.getCar()), obj);
                arg0 = arg03;
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, arg0);
            }
        }
        return LList.reverseInPlace(obj);
    }

    public static Object unzip2(Object lis) {
        new frame();
        return frame.lambda2recur(lis);
    }

    /* compiled from: srfi1.scm */
    public class frame extends ModuleBody {
        public static Object lambda2recur(Object lis) {
            frame0 frame0 = new frame0();
            frame0.lis = lis;
            if (srfi1.isNullList(frame0.lis) != Boolean.FALSE) {
                return misc.values(frame0.lis, frame0.lis);
            }
            frame0.elt = lists.car.apply1(frame0.lis);
            return call_with_values.callWithValues(frame0.lambda$Fn1, frame0.lambda$Fn2);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame0 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn1 = new ModuleMethod(this, 1, (Object) null, 0);
        final ModuleMethod lambda$Fn2;
        Object lis;

        public frame0() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 2, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:627");
            this.lambda$Fn2 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 1 ? lambda3() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 1) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 2 ? lambda4(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda3() {
            return frame.lambda2recur(lists.cdr.apply1(this.lis));
        }

        /* access modifiers changed from: package-private */
        public Object lambda4(Object a, Object b) {
            return misc.values(lists.cons(lists.car.apply1(this.elt), a), lists.cons(lists.cadr.apply1(this.elt), b));
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

    public static Object unzip3(Object lis) {
        new frame1();
        return frame1.lambda5recur(lis);
    }

    /* compiled from: srfi1.scm */
    public class frame1 extends ModuleBody {
        public static Object lambda5recur(Object lis) {
            frame2 frame2 = new frame2();
            frame2.lis = lis;
            if (srfi1.isNullList(frame2.lis) != Boolean.FALSE) {
                return misc.values(frame2.lis, frame2.lis, frame2.lis);
            }
            frame2.elt = lists.car.apply1(frame2.lis);
            return call_with_values.callWithValues(frame2.lambda$Fn3, frame2.lambda$Fn4);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame2 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn3 = new ModuleMethod(this, 3, (Object) null, 0);
        final ModuleMethod lambda$Fn4;
        Object lis;

        public frame2() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 4, (Object) null, 12291);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:635");
            this.lambda$Fn4 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 3 ? lambda6() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 3) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 4 ? lambda7(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        /* access modifiers changed from: package-private */
        public Object lambda6() {
            return frame1.lambda5recur(lists.cdr.apply1(this.lis));
        }

        /* access modifiers changed from: package-private */
        public Object lambda7(Object a, Object b, Object c) {
            return misc.values(lists.cons(lists.car.apply1(this.elt), a), lists.cons(lists.cadr.apply1(this.elt), b), lists.cons(lists.caddr.apply1(this.elt), c));
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 4) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }
    }

    public static Object unzip4(Object lis) {
        new frame3();
        return frame3.lambda8recur(lis);
    }

    /* compiled from: srfi1.scm */
    public class frame3 extends ModuleBody {
        public static Object lambda8recur(Object lis) {
            frame4 frame4 = new frame4();
            frame4.lis = lis;
            if (srfi1.isNullList(frame4.lis) != Boolean.FALSE) {
                return misc.values(frame4.lis, frame4.lis, frame4.lis, frame4.lis);
            }
            frame4.elt = lists.car.apply1(frame4.lis);
            return call_with_values.callWithValues(frame4.lambda$Fn5, frame4.lambda$Fn6);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame4 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn5 = new ModuleMethod(this, 5, (Object) null, 0);
        final ModuleMethod lambda$Fn6;
        Object lis;

        public frame4() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 6, (Object) null, 16388);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:644");
            this.lambda$Fn6 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 5 ? lambda9() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 5) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
            return moduleMethod.selector == 6 ? lambda10(obj, obj2, obj3, obj4) : super.apply4(moduleMethod, obj, obj2, obj3, obj4);
        }

        /* access modifiers changed from: package-private */
        public Object lambda10(Object a, Object b, Object c, Object d) {
            return misc.values(lists.cons(lists.car.apply1(this.elt), a), lists.cons(lists.cadr.apply1(this.elt), b), lists.cons(lists.caddr.apply1(this.elt), c), lists.cons(lists.cadddr.apply1(this.elt), d));
        }

        /* access modifiers changed from: package-private */
        public Object lambda9() {
            return frame3.lambda8recur(lists.cdr.apply1(this.lis));
        }

        public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
            if (moduleMethod.selector != 6) {
                return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.value4 = obj4;
            callContext.proc = moduleMethod;
            callContext.pc = 4;
            return 0;
        }
    }

    public static Object unzip5(Object lis) {
        new frame5();
        return frame5.lambda11recur(lis);
    }

    /* compiled from: srfi1.scm */
    public class frame5 extends ModuleBody {
        public static Object lambda11recur(Object lis) {
            frame6 frame6 = new frame6();
            frame6.lis = lis;
            if (srfi1.isNullList(frame6.lis) != Boolean.FALSE) {
                return misc.values(frame6.lis, frame6.lis, frame6.lis, frame6.lis, frame6.lis);
            }
            frame6.elt = lists.car.apply1(frame6.lis);
            return call_with_values.callWithValues(frame6.lambda$Fn7, frame6.lambda$Fn8);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame6 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn7 = new ModuleMethod(this, 7, (Object) null, 0);
        final ModuleMethod lambda$Fn8;
        Object lis;

        public frame6() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 8, (Object) null, 20485);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:654");
            this.lambda$Fn8 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 7 ? lambda12() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 7) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
            if (moduleMethod.selector != 8) {
                return super.applyN(moduleMethod, objArr);
            }
            return lambda13(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
        }

        /* access modifiers changed from: package-private */
        public Object lambda12() {
            return frame5.lambda11recur(lists.cdr.apply1(this.lis));
        }

        /* access modifiers changed from: package-private */
        public Object lambda13(Object a, Object b, Object c, Object d, Object e) {
            return misc.values(lists.cons(lists.car.apply1(this.elt), a), lists.cons(lists.cadr.apply1(this.elt), b), lists.cons(lists.caddr.apply1(this.elt), c), lists.cons(lists.cadddr.apply1(this.elt), d), lists.cons(lists.car.apply1(lists.cddddr.apply1(this.elt)), e));
        }

        public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
            if (moduleMethod.selector != 8) {
                return super.matchN(moduleMethod, objArr, callContext);
            }
            callContext.values = objArr;
            callContext.proc = moduleMethod;
            callContext.pc = 5;
            return 0;
        }
    }

    public static Object append$Ex$V(Object[] argsArray) {
        Object makeList = LList.makeList(argsArray, 0);
        LList lList = LList.Empty;
        while (lists.isPair(makeList)) {
            Object first2 = lists.car.apply1(makeList);
            Object rest = lists.cdr.apply1(makeList);
            if (!lists.isPair(first2)) {
                lList = first2;
                makeList = rest;
            } else {
                try {
                    Object tail$Mncons = lastPair(first2);
                    while (lists.isPair(rest)) {
                        Object next = lists.car.apply1(rest);
                        rest = lists.cdr.apply1(rest);
                        try {
                            lists.setCdr$Ex((Pair) tail$Mncons, next);
                            if (lists.isPair(next)) {
                                try {
                                    tail$Mncons = lastPair((Pair) next);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "last-pair", 0, next);
                                }
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "set-cdr!", 1, tail$Mncons);
                        }
                    }
                    return first2;
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "last-pair", 0, first2);
                }
            }
        }
        return lList;
    }

    public static Object appendReverse(Object rev$Mnhead, Object tail) {
        while (isNullList(rev$Mnhead) == Boolean.FALSE) {
            Object rev$Mnhead2 = lists.cdr.apply1(rev$Mnhead);
            tail = lists.cons(lists.car.apply1(rev$Mnhead), tail);
            rev$Mnhead = rev$Mnhead2;
        }
        return tail;
    }

    public static Object appendReverse$Ex(Object rev$Mnhead, Object tail) {
        while (isNullList(rev$Mnhead) == Boolean.FALSE) {
            Object next$Mnrev = lists.cdr.apply1(rev$Mnhead);
            try {
                lists.setCdr$Ex((Pair) rev$Mnhead, tail);
                tail = rev$Mnhead;
                rev$Mnhead = next$Mnrev;
            } catch (ClassCastException e) {
                throw new WrongType(e, "set-cdr!", 1, rev$Mnhead);
            }
        }
        return tail;
    }

    public static Object concatenate(Object lists) {
        return reduceRight(append.append, LList.Empty, lists);
    }

    public static Object concatenate$Ex(Object lists) {
        return reduceRight(append$Ex, LList.Empty, lists);
    }

    static Object $PcCdrs(Object lists) {
        Continuation abort = new Continuation(CallContext.getInstance());
        try {
            frame55 frame552 = new frame55();
            frame552.abort = abort;
            Object lambda74recur = frame552.lambda74recur(lists);
            abort.invoked = true;
            return lambda74recur;
        } catch (Throwable th) {
            return Continuation.handleException(th, abort);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame55 extends ModuleBody {
        Continuation abort;

        public Object lambda74recur(Object lists) {
            if (!lists.isPair(lists)) {
                return LList.Empty;
            }
            Object lis = lists.car.apply1(lists);
            if (srfi1.isNullList(lis) != Boolean.FALSE) {
                return this.abort.apply1(LList.Empty);
            }
            return lists.cons(lists.cdr.apply1(lis), lambda74recur(lists.cdr.apply1(lists)));
        }
    }

    static Object $PcCars$Pl(Object lists, Object lastElt) {
        frame56 frame562 = new frame56();
        frame562.last$Mnelt = lastElt;
        return frame562.lambda75recur(lists);
    }

    /* compiled from: srfi1.scm */
    public class frame56 extends ModuleBody {
        Object last$Mnelt;

        public Object lambda75recur(Object lists) {
            return lists.isPair(lists) ? lists.cons(lists.caar.apply1(lists), lambda75recur(lists.cdr.apply1(lists))) : LList.list1(this.last$Mnelt);
        }
    }

    static Object $PcCars$PlCdrs(Object lists) {
        Continuation abort = new Continuation(CallContext.getInstance());
        try {
            frame57 frame572 = new frame57();
            frame572.abort = abort;
            Object lambda76recur = frame572.lambda76recur(lists);
            abort.invoked = true;
            return lambda76recur;
        } catch (Throwable th) {
            return Continuation.handleException(th, abort);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame57 extends ModuleBody {
        Continuation abort;

        public Object lambda76recur(Object lists) {
            frame58 frame58 = new frame58();
            frame58.staticLink = this;
            frame58.lists = lists;
            if (lists.isPair(frame58.lists)) {
                return call_with_values.callWithValues(frame58.lambda$Fn57, frame58.lambda$Fn58);
            }
            return misc.values(LList.Empty, LList.Empty);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame58 extends ModuleBody {
        final ModuleMethod lambda$Fn57 = new ModuleMethod(this, 61, (Object) null, 0);
        final ModuleMethod lambda$Fn58;
        Object lists;
        frame57 staticLink;

        public frame58() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 62, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:762");
            this.lambda$Fn58 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 61 ? lambda77() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 61) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 62 ? lambda78(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda77() {
            return srfi1.car$PlCdr(this.lists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda78(Object list, Object otherLists) {
            frame59 frame59 = new frame59();
            frame59.staticLink = this;
            frame59.list = list;
            frame59.other$Mnlists = otherLists;
            if (srfi1.isNullList(frame59.list) != Boolean.FALSE) {
                return this.staticLink.abort.apply2(LList.Empty, LList.Empty);
            }
            return call_with_values.callWithValues(frame59.lambda$Fn59, frame59.lambda$Fn60);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 62) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame59 extends ModuleBody {
        final ModuleMethod lambda$Fn59 = new ModuleMethod(this, 59, (Object) null, 0);
        final ModuleMethod lambda$Fn60;
        Object list;
        Object other$Mnlists;
        frame58 staticLink;

        public frame59() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 60, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:764");
            this.lambda$Fn60 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 59 ? lambda79() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 59) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 60 ? lambda80(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda79() {
            return srfi1.car$PlCdr(this.list);
        }

        /* access modifiers changed from: package-private */
        public Object lambda80(Object a, Object d) {
            frame60 frame60 = new frame60();
            frame60.staticLink = this;
            frame60.a = a;
            frame60.d = d;
            return call_with_values.callWithValues(frame60.lambda$Fn61, frame60.lambda$Fn62);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 60) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame60 extends ModuleBody {
        Object a;
        Object d;
        final ModuleMethod lambda$Fn61 = new ModuleMethod(this, 57, (Object) null, 0);
        final ModuleMethod lambda$Fn62;
        frame59 staticLink;

        public frame60() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 58, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:765");
            this.lambda$Fn62 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 57 ? lambda81() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 57) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 58 ? lambda82(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda81() {
            return this.staticLink.staticLink.staticLink.lambda76recur(this.staticLink.other$Mnlists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda82(Object cars, Object cdrs) {
            return misc.values(lists.cons(this.a, cars), lists.cons(this.d, cdrs));
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 58) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame61 extends ModuleBody {
        final ModuleMethod lambda$Fn63 = new ModuleMethod(this, 63, (Object) null, 0);
        Object lists;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 63 ? lambda83() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 63) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        static Pair lambda84(Object obj, Object x) {
            return lists.cons(obj, x);
        }

        /* access modifiers changed from: package-private */
        public Object lambda83() {
            return srfi1.$PcCars$PlCdrs(this.lists);
        }
    }

    static Object $PcCars$PlCdrs$SlPair(Object lists) {
        frame61 frame612 = new frame61();
        frame612.lists = lists;
        return call_with_values.callWithValues(frame612.lambda$Fn63, lambda$Fn64);
    }

    static Object $PcCars$PlCdrs$Pl(Object lists, Object carsFinal) {
        frame62 closureEnv = new frame62();
        closureEnv.cars$Mnfinal = carsFinal;
        Continuation abort = new Continuation(CallContext.getInstance());
        try {
            frame63 frame632 = new frame63();
            frame632.staticLink = closureEnv;
            frame632.abort = abort;
            Object lambda85recur = frame632.lambda85recur(lists);
            abort.invoked = true;
            return lambda85recur;
        } catch (Throwable th) {
            return Continuation.handleException(th, abort);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame63 extends ModuleBody {
        Continuation abort;
        frame62 staticLink;

        public Object lambda85recur(Object lists) {
            frame64 frame64 = new frame64();
            frame64.staticLink = this;
            frame64.lists = lists;
            if (lists.isPair(frame64.lists)) {
                return call_with_values.callWithValues(frame64.lambda$Fn65, frame64.lambda$Fn66);
            }
            return misc.values(LList.list1(this.staticLink.cars$Mnfinal), LList.Empty);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame64 extends ModuleBody {
        final ModuleMethod lambda$Fn65 = new ModuleMethod(this, 68, (Object) null, 0);
        final ModuleMethod lambda$Fn66;
        Object lists;
        frame63 staticLink;

        public frame64() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 69, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:783");
            this.lambda$Fn66 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 68 ? lambda86() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 68) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 69 ? lambda87(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda86() {
            return srfi1.car$PlCdr(this.lists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda87(Object list, Object otherLists) {
            frame65 frame65 = new frame65();
            frame65.staticLink = this;
            frame65.list = list;
            frame65.other$Mnlists = otherLists;
            if (srfi1.isNullList(frame65.list) != Boolean.FALSE) {
                return this.staticLink.abort.apply2(LList.Empty, LList.Empty);
            }
            return call_with_values.callWithValues(frame65.lambda$Fn67, frame65.lambda$Fn68);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 69) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame65 extends ModuleBody {
        final ModuleMethod lambda$Fn67 = new ModuleMethod(this, 66, (Object) null, 0);
        final ModuleMethod lambda$Fn68;
        Object list;
        Object other$Mnlists;
        frame64 staticLink;

        public frame65() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 67, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:785");
            this.lambda$Fn68 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 66 ? lambda88() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 66) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 67 ? lambda89(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda88() {
            return srfi1.car$PlCdr(this.list);
        }

        /* access modifiers changed from: package-private */
        public Object lambda89(Object a, Object d) {
            frame66 frame66 = new frame66();
            frame66.staticLink = this;
            frame66.a = a;
            frame66.d = d;
            return call_with_values.callWithValues(frame66.lambda$Fn69, frame66.lambda$Fn70);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 67) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame66 extends ModuleBody {
        Object a;
        Object d;
        final ModuleMethod lambda$Fn69 = new ModuleMethod(this, 64, (Object) null, 0);
        final ModuleMethod lambda$Fn70;
        frame65 staticLink;

        public frame66() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 65, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:786");
            this.lambda$Fn70 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 64 ? lambda90() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 64) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 65 ? lambda91(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda90() {
            return this.staticLink.staticLink.staticLink.lambda85recur(this.staticLink.other$Mnlists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda91(Object cars, Object cdrs) {
            return misc.values(lists.cons(this.a, cars), lists.cons(this.d, cdrs));
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 65) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    static Object $PcCars$PlCdrs$SlNoTest(Object lists) {
        new frame67();
        return frame67.lambda92recur(lists);
    }

    /* compiled from: srfi1.scm */
    public class frame67 extends ModuleBody {
        public static Object lambda92recur(Object lists) {
            frame68 frame68 = new frame68();
            frame68.lists = lists;
            if (lists.isPair(frame68.lists)) {
                return call_with_values.callWithValues(frame68.lambda$Fn71, frame68.lambda$Fn72);
            }
            return misc.values(LList.Empty, LList.Empty);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame68 extends ModuleBody {
        final ModuleMethod lambda$Fn71 = new ModuleMethod(this, 74, (Object) null, 0);
        final ModuleMethod lambda$Fn72;
        Object lists;

        public frame68() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 75, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:794");
            this.lambda$Fn72 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 74 ? lambda93() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 74) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 75 ? lambda94(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda93() {
            return srfi1.car$PlCdr(this.lists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda94(Object list, Object otherLists) {
            frame69 frame69 = new frame69();
            frame69.staticLink = this;
            frame69.list = list;
            frame69.other$Mnlists = otherLists;
            return call_with_values.callWithValues(frame69.lambda$Fn73, frame69.lambda$Fn74);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 75) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame69 extends ModuleBody {
        final ModuleMethod lambda$Fn73 = new ModuleMethod(this, 72, (Object) null, 0);
        final ModuleMethod lambda$Fn74;
        Object list;
        Object other$Mnlists;
        frame68 staticLink;

        public frame69() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 73, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:795");
            this.lambda$Fn74 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 72 ? lambda95() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 72) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 73 ? lambda96(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda95() {
            return srfi1.car$PlCdr(this.list);
        }

        /* access modifiers changed from: package-private */
        public Object lambda96(Object a, Object d) {
            frame70 frame70 = new frame70();
            frame70.staticLink = this;
            frame70.a = a;
            frame70.d = d;
            return call_with_values.callWithValues(frame70.lambda$Fn75, frame70.lambda$Fn76);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 73) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame70 extends ModuleBody {
        Object a;
        Object d;
        final ModuleMethod lambda$Fn75 = new ModuleMethod(this, 70, (Object) null, 0);
        final ModuleMethod lambda$Fn76;
        frame69 staticLink;

        public frame70() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 71, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:796");
            this.lambda$Fn76 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 70 ? lambda97() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 70) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 71 ? lambda98(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda97() {
            return frame67.lambda92recur(this.staticLink.other$Mnlists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda98(Object cars, Object cdrs) {
            return misc.values(lists.cons(this.a, cars), lists.cons(this.d, cdrs));
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 71) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame71 extends ModuleBody {
        final ModuleMethod lambda$Fn77 = new ModuleMethod(this, 76, (Object) null, 0);
        Object lists;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 76 ? lambda99() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 76) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        static Pair lambda100(Object obj, Object x) {
            return lists.cons(obj, x);
        }

        /* access modifiers changed from: package-private */
        public Object lambda99() {
            return srfi1.$PcCars$PlCdrs$SlNoTest(this.lists);
        }
    }

    static Object $PcCars$PlCdrs$SlNoTest$SlPair(Object lists) {
        frame71 frame712 = new frame71();
        frame712.lists = lists;
        return call_with_values.callWithValues(frame712.lambda$Fn77, lambda$Fn78);
    }

    public static Object count$V(Procedure pred, Object list1, Object[] argsArray) {
        Object i;
        Object makeList = LList.makeList(argsArray, 0);
        if (lists.isPair(makeList)) {
            i = Lit0;
            while (isNullList(list1) == Boolean.FALSE) {
                Object split = $PcCars$PlCdrs$SlPair(makeList);
                Object a$Mns = lists.car.apply1(split);
                Object d$Mns = lists.cdr.apply1(split);
                if (lists.isNull(a$Mns)) {
                    break;
                }
                Object list12 = lists.cdr.apply1(list1);
                if (Scheme.apply.apply3(pred, lists.car.apply1(list1), a$Mns) != Boolean.FALSE) {
                    i = AddOp.$Pl.apply2(i, Lit1);
                }
                makeList = d$Mns;
                list1 = list12;
            }
        } else {
            i = Lit0;
            Object lis = list1;
            while (isNullList(lis) == Boolean.FALSE) {
                Object lis2 = lists.cdr.apply1(lis);
                if (pred.apply1(lists.car.apply1(lis)) != Boolean.FALSE) {
                    i = AddOp.$Pl.apply2(i, Lit1);
                }
                lis = lis2;
            }
        }
        return i;
    }

    public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
        if (moduleMethod.selector != 123) {
            return super.apply4(moduleMethod, obj, obj2, obj3, obj4);
        }
        try {
            try {
                try {
                    return unfoldRight((Procedure) obj, (Procedure) obj2, (Procedure) obj3, obj4);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "unfold-right", 3, obj3);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "unfold-right", 2, obj2);
            }
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "unfold-right", 1, obj);
        }
    }

    public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
        if (moduleMethod.selector != 123) {
            return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
        }
        if (!(obj instanceof Procedure)) {
            return -786431;
        }
        callContext.value1 = obj;
        if (!(obj2 instanceof Procedure)) {
            return -786430;
        }
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

    public static Object unfoldRight(Procedure p, Procedure f, Procedure g, Object seed, Object obj) {
        while (p.apply1(seed) == Boolean.FALSE) {
            Object seed2 = g.apply1(seed);
            obj = lists.cons(f.apply1(seed), obj);
            seed = seed2;
        }
        return obj;
    }

    public static Object unfold$V(Procedure p, Procedure f, Procedure g, Object seed, Object[] argsArray) {
        LList maybe$Mntail$Mngen = LList.makeList(argsArray, 0);
        if (lists.isPair(maybe$Mntail$Mngen)) {
            Object tail$Mngen = lists.car.apply1(maybe$Mntail$Mngen);
            if (lists.isPair(lists.cdr.apply1(maybe$Mntail$Mngen))) {
                return Scheme.apply.applyN(new Object[]{misc.error, "Too many arguments", unfold, p, f, g, seed, maybe$Mntail$Mngen});
            }
            Object obj = LList.Empty;
            while (p.apply1(seed) == Boolean.FALSE) {
                Object seed2 = g.apply1(seed);
                obj = lists.cons(f.apply1(seed), obj);
                seed = seed2;
            }
            return appendReverse$Ex(obj, Scheme.applyToArgs.apply2(tail$Mngen, seed));
        }
        LList lList = LList.Empty;
        while (p.apply1(seed) == Boolean.FALSE) {
            Object seed3 = g.apply1(seed);
            lList = lists.cons(f.apply1(seed), lList);
            seed = seed3;
        }
        try {
            return lists.reverse$Ex(lList);
        } catch (ClassCastException e) {
            throw new WrongType(e, "reverse!", 1, (Object) lList);
        }
    }

    public static Object fold$V(Procedure kons, Object knil, Object lis1, Object[] argsArray) {
        frame7 frame73 = new frame7();
        frame73.kons = kons;
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            return frame73.lambda14lp(lists.cons(lis1, lists), knil);
        }
        Object lis = lis1;
        while (isNullList(lis) == Boolean.FALSE) {
            Object lis2 = lists.cdr.apply1(lis);
            knil = frame73.kons.apply2(lists.car.apply1(lis), knil);
            lis = lis2;
        }
        return knil;
    }

    /* compiled from: srfi1.scm */
    public class frame7 extends ModuleBody {
        Procedure kons;

        public Object lambda14lp(Object lists, Object ans) {
            frame8 frame8 = new frame8();
            frame8.staticLink = this;
            frame8.lists = lists;
            frame8.ans = ans;
            return call_with_values.callWithValues(frame8.lambda$Fn9, frame8.lambda$Fn10);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame8 extends ModuleBody {
        Object ans;
        final ModuleMethod lambda$Fn10;
        final ModuleMethod lambda$Fn9 = new ModuleMethod(this, 9, (Object) null, 0);
        Object lists;
        frame7 staticLink;

        public frame8() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 10, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:859");
            this.lambda$Fn10 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 9 ? lambda15() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 9) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 10 ? lambda16(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda15() {
            return srfi1.$PcCars$PlCdrs$Pl(this.lists, this.ans);
        }

        /* access modifiers changed from: package-private */
        public Object lambda16(Object cars$Plans, Object cdrs) {
            if (lists.isNull(cars$Plans)) {
                return this.ans;
            }
            return this.staticLink.lambda14lp(cdrs, Scheme.apply.apply2(this.staticLink.kons, cars$Plans));
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 10) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object foldRight$V(Procedure kons, Object knil, Object lis1, Object[] argsArray) {
        frame9 frame92 = new frame9();
        frame92.kons = kons;
        frame92.knil = knil;
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            return frame92.lambda17recur(lists.cons(lis1, lists));
        }
        return frame92.lambda18recur(lis1);
    }

    /* compiled from: srfi1.scm */
    public class frame9 extends ModuleBody {
        Object knil;
        Procedure kons;

        public Object lambda17recur(Object lists) {
            Object cdrs = srfi1.$PcCdrs(lists);
            if (lists.isNull(cdrs)) {
                return this.knil;
            }
            return Scheme.apply.apply2(this.kons, srfi1.$PcCars$Pl(lists, lambda17recur(cdrs)));
        }

        public Object lambda18recur(Object lis) {
            if (srfi1.isNullList(lis) != Boolean.FALSE) {
                return this.knil;
            }
            return this.kons.apply2(lists.car.apply1(lis), lambda18recur(lists.cdr.apply1(lis)));
        }
    }

    public static Object pairFoldRight$V(Procedure f, Object zero, Object lis1, Object[] argsArray) {
        frame10 frame102 = new frame10();
        frame102.f = f;
        frame102.zero = zero;
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            return frame102.lambda19recur(lists.cons(lis1, lists));
        }
        return frame102.lambda20recur(lis1);
    }

    /* compiled from: srfi1.scm */
    public class frame10 extends ModuleBody {
        Procedure f;
        Object zero;

        public Object lambda19recur(Object lists) {
            Object cdrs = srfi1.$PcCdrs(lists);
            if (lists.isNull(cdrs)) {
                return this.zero;
            }
            return Scheme.apply.apply2(this.f, srfi1.append$Ex$V(new Object[]{lists, LList.list1(lambda19recur(cdrs))}));
        }

        public Object lambda20recur(Object lis) {
            return srfi1.isNullList(lis) != Boolean.FALSE ? this.zero : this.f.apply2(lis, lambda20recur(lists.cdr.apply1(lis)));
        }
    }

    public static Object pairFold$V(Procedure f, Object zero, Object lis1, Object[] argsArray) {
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            Object cons = lists.cons(lis1, lists);
            while (true) {
                Object tails = $PcCdrs(cons);
                if (lists.isNull(tails)) {
                    break;
                }
                Apply apply = Scheme.apply;
                Object[] objArr = {cons, LList.list1(zero)};
                cons = tails;
                zero = apply.apply2(f, append$Ex$V(objArr));
            }
        } else {
            Object lis = lis1;
            while (isNullList(lis) == Boolean.FALSE) {
                Object tail = lists.cdr.apply1(lis);
                Object ans = f.apply2(lis, zero);
                lis = tail;
                zero = ans;
            }
        }
        return zero;
    }

    public static Object reduce(Procedure f, Object ridentity, Object lis) {
        return isNullList(lis) != Boolean.FALSE ? ridentity : fold$V(f, lists.car.apply1(lis), lists.cdr.apply1(lis), new Object[0]);
    }

    public static Object reduceRight(Procedure f, Object ridentity, Object lis) {
        frame11 frame112 = new frame11();
        frame112.f = f;
        return isNullList(lis) != Boolean.FALSE ? ridentity : frame112.lambda21recur(lists.car.apply1(lis), lists.cdr.apply1(lis));
    }

    /* compiled from: srfi1.scm */
    public class frame11 extends ModuleBody {
        Procedure f;

        public Object lambda21recur(Object head, Object lis) {
            if (lists.isPair(lis)) {
                return this.f.apply2(head, lambda21recur(lists.car.apply1(lis), lists.cdr.apply1(lis)));
            }
            return head;
        }
    }

    public static Object appendMap$V(Object f, Object lis1, Object[] argsArray) {
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            return Scheme.apply.apply2(append.append, Scheme.apply.apply4(Scheme.map, f, lis1, lists));
        }
        Apply apply = Scheme.apply;
        append append = append.append;
        Object obj = LList.Empty;
        Object arg0 = lis1;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                obj = Pair.make(Scheme.applyToArgs.apply2(f, arg02.getCar()), obj);
                arg0 = arg03;
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, arg0);
            }
        }
        return apply.apply2(append, LList.reverseInPlace(obj));
    }

    public static Object appendMap$Ex$V(Object f, Object lis1, Object[] argsArray) {
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            return Scheme.apply.apply2(append$Ex, Scheme.apply.apply4(Scheme.map, f, lis1, lists));
        }
        Apply apply = Scheme.apply;
        ModuleMethod moduleMethod = append$Ex;
        Object obj = LList.Empty;
        Object arg0 = lis1;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                obj = Pair.make(Scheme.applyToArgs.apply2(f, arg02.getCar()), obj);
                arg0 = arg03;
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, arg0);
            }
        }
        return apply.apply2(moduleMethod, LList.reverseInPlace(obj));
    }

    public static Object pairForEach$V(Procedure proc, Object lis1, Object[] argsArray) {
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            Object cons = lists.cons(lis1, lists);
            while (true) {
                Object tails = $PcCdrs(cons);
                if (!lists.isPair(tails)) {
                    return Values.empty;
                }
                Scheme.apply.apply2(proc, cons);
                cons = tails;
            }
        } else {
            Object lis = lis1;
            while (isNullList(lis) == Boolean.FALSE) {
                Object tail = lists.cdr.apply1(lis);
                proc.apply1(lis);
                lis = tail;
            }
            return Values.empty;
        }
    }

    public static Object map$Ex$V(Procedure f, Object lis1, Object[] argsArray) {
        frame12 frame122 = new frame12();
        frame122.f = f;
        Object makeList = LList.makeList(argsArray, 0);
        if (lists.isPair(makeList)) {
            Object lis12 = lis1;
            while (isNullList(lis12) == Boolean.FALSE) {
                Object split = $PcCars$PlCdrs$SlNoTest$SlPair(makeList);
                Object heads = lists.car.apply1(split);
                Object tails = lists.cdr.apply1(split);
                try {
                    lists.setCar$Ex((Pair) lis12, Scheme.apply.apply3(frame122.f, lists.car.apply1(lis12), heads));
                    lis12 = lists.cdr.apply1(lis12);
                    makeList = tails;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "set-car!", 1, lis12);
                }
            }
        } else {
            pairForEach$V(frame122.lambda$Fn11, lis1, new Object[0]);
        }
        return lis1;
    }

    /* compiled from: srfi1.scm */
    public class frame12 extends ModuleBody {
        Procedure f;
        final ModuleMethod lambda$Fn11;

        public frame12() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 11, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:961");
            this.lambda$Fn11 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector != 11) {
                return super.apply1(moduleMethod, obj);
            }
            lambda22(obj);
            return Values.empty;
        }

        /* access modifiers changed from: package-private */
        public void lambda22(Object pair) {
            try {
                lists.setCar$Ex((Pair) pair, this.f.apply1(lists.car.apply1(pair)));
            } catch (ClassCastException e) {
                throw new WrongType(e, "set-car!", 1, pair);
            }
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 11) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object filterMap$V(Procedure f, Object lis1, Object[] argsArray) {
        frame13 frame132 = new frame13();
        frame132.f = f;
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            return frame132.lambda23recur(lists.cons(lis1, lists), LList.Empty);
        }
        LList lList = LList.Empty;
        Object lis = lis1;
        while (isNullList(lis) == Boolean.FALSE) {
            Object head = frame132.f.apply1(lists.car.apply1(lis));
            Object tail = lists.cdr.apply1(lis);
            if (head != Boolean.FALSE) {
                lList = lists.cons(head, lList);
                lis = tail;
            } else {
                lis = tail;
            }
        }
        try {
            return lists.reverse$Ex(lList);
        } catch (ClassCastException e) {
            throw new WrongType(e, "reverse!", 1, (Object) lList);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame13 extends ModuleBody {
        Procedure f;

        public Object lambda23recur(Object lists, Object res) {
            frame14 frame14 = new frame14();
            frame14.staticLink = this;
            frame14.lists = lists;
            frame14.res = res;
            return call_with_values.callWithValues(frame14.lambda$Fn12, frame14.lambda$Fn13);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame14 extends ModuleBody {
        final ModuleMethod lambda$Fn12 = new ModuleMethod(this, 12, (Object) null, 0);
        final ModuleMethod lambda$Fn13;
        Object lists;
        Object res;
        frame13 staticLink;

        public frame14() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 13, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:969");
            this.lambda$Fn13 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 12 ? lambda24() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 12) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 13 ? lambda25(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda24() {
            return srfi1.$PcCars$PlCdrs(this.lists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda25(Object cars, Object cdrs) {
            if (srfi1.isNotPair(cars)) {
                Object obj = this.res;
                try {
                    return lists.reverse$Ex((LList) obj);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "reverse!", 1, obj);
                }
            } else {
                Object head = Scheme.apply.apply2(this.staticLink.f, cars);
                if (head != Boolean.FALSE) {
                    return this.staticLink.lambda23recur(cdrs, lists.cons(head, this.res));
                }
                return this.staticLink.lambda23recur(cdrs, this.res);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 13) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object filter(Procedure pred, Object lis) {
        LList lList = LList.Empty;
        while (isNullList(lis) == Boolean.FALSE) {
            Object head = lists.car.apply1(lis);
            Object tail = lists.cdr.apply1(lis);
            if (pred.apply1(head) != Boolean.FALSE) {
                lList = lists.cons(head, lList);
                lis = tail;
            } else {
                lis = tail;
            }
        }
        try {
            return lists.reverse$Ex(lList);
        } catch (ClassCastException e) {
            throw new WrongType(e, "reverse!", 1, (Object) lList);
        }
    }

    public static Object filter$Ex(Procedure pred, Object lis) {
        Object ans = lis;
        while (true) {
            if (isNullList(ans) != Boolean.FALSE) {
                break;
            } else if (pred.apply1(lists.car.apply1(ans)) == Boolean.FALSE) {
                ans = lists.cdr.apply1(ans);
            } else {
                Object prev = lists.cdr.apply1(ans);
                Object prev2 = ans;
                loop1:
                while (true) {
                    if (!lists.isPair(prev)) {
                        break;
                    } else if (pred.apply1(lists.car.apply1(prev)) != Boolean.FALSE) {
                        prev2 = prev;
                        prev = lists.cdr.apply1(prev);
                    } else {
                        Object lis2 = lists.cdr.apply1(prev);
                        while (lists.isPair(lis2)) {
                            if (pred.apply1(lists.car.apply1(lis2)) != Boolean.FALSE) {
                                try {
                                    lists.setCdr$Ex((Pair) prev2, lis2);
                                    prev2 = lis2;
                                    prev = lists.cdr.apply1(lis2);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "set-cdr!", 1, prev2);
                                }
                            } else {
                                lis2 = lists.cdr.apply1(lis2);
                            }
                        }
                        try {
                            lists.setCdr$Ex((Pair) prev2, lis2);
                            break loop1;
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "set-cdr!", 1, prev2);
                        }
                    }
                }
            }
        }
        return ans;
    }

    public static Object partition(Procedure pred, Object lis) {
        LList lList = LList.Empty;
        Object out = LList.Empty;
        while (isNullList(lis) == Boolean.FALSE) {
            Object head = lists.car.apply1(lis);
            Object tail = lists.cdr.apply1(lis);
            if (pred.apply1(head) != Boolean.FALSE) {
                lList = lists.cons(head, lList);
                lis = tail;
            } else {
                out = lists.cons(head, out);
                lis = tail;
            }
        }
        Object[] objArr = new Object[2];
        try {
            objArr[0] = lists.reverse$Ex(lList);
            try {
                objArr[1] = lists.reverse$Ex((LList) out);
                return misc.values(objArr);
            } catch (ClassCastException e) {
                throw new WrongType(e, "reverse!", 1, out);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "reverse!", 1, (Object) lList);
        }
    }

    public static Object partition$Ex(Procedure pred, Object in) {
        Pair in$Mnhead = lists.cons(Lit2, LList.Empty);
        Pair out$Mnhead = lists.cons(Lit2, LList.Empty);
        Pair in2 = in$Mnhead;
        Pair out = out$Mnhead;
        while (!isNotPair(in)) {
            if (pred.apply1(lists.car.apply1(in)) != Boolean.FALSE) {
                try {
                    lists.setCdr$Ex(in2, in);
                    in2 = in;
                    in = lists.cdr.apply1(in);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "set-cdr!", 1, (Object) in2);
                }
            } else {
                try {
                    lists.setCdr$Ex((Pair) out, in);
                    out = in;
                    in = lists.cdr.apply1(in);
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "set-cdr!", 1, out);
                }
            }
        }
        try {
            lists.setCdr$Ex(in2, LList.Empty);
            try {
                lists.setCdr$Ex((Pair) out, LList.Empty);
                return misc.values(lists.cdr.apply1(in$Mnhead), lists.cdr.apply1(out$Mnhead));
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "set-cdr!", 1, out);
            }
        } catch (ClassCastException e4) {
            throw new WrongType(e4, "set-cdr!", 1, (Object) in2);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame15 extends ModuleBody {
        final ModuleMethod lambda$Fn14;
        Object pred;

        public frame15() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 14, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1199");
            this.lambda$Fn14 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 14) {
                return lambda26(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda26(Object x) {
            return ((Scheme.applyToArgs.apply2(this.pred, x) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 14) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame16 extends ModuleBody {
        final ModuleMethod lambda$Fn15;
        Object pred;

        public frame16() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 15, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1200");
            this.lambda$Fn15 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 15) {
                return lambda27(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda27(Object x) {
            return ((Scheme.applyToArgs.apply2(this.pred, x) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 15) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object remove(Object pred, Object l) {
        frame15 frame152 = new frame15();
        frame152.pred = pred;
        return filter(frame152.lambda$Fn14, l);
    }

    public static Object remove$Ex(Object pred, Object l) {
        frame16 frame162 = new frame16();
        frame162.pred = pred;
        return filter$Ex(frame162.lambda$Fn15, l);
    }

    /* compiled from: srfi1.scm */
    public class frame17 extends ModuleBody {
        final ModuleMethod lambda$Fn16;
        Object maybe$Mn$Eq;
        Object x;

        public frame17() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 16, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1222");
            this.lambda$Fn16 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 16) {
                return lambda28(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda28(Object y) {
            return ((Scheme.applyToArgs.apply3(this.maybe$Mn$Eq, this.x, y) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 16) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object delete(Object x, Object lis, Object maybe$Mn$Eq) {
        frame17 frame172 = new frame17();
        frame172.x = x;
        frame172.maybe$Mn$Eq = maybe$Mn$Eq;
        return filter(frame172.lambda$Fn16, lis);
    }

    /* compiled from: srfi1.scm */
    public class frame18 extends ModuleBody {
        final ModuleMethod lambda$Fn17;
        Object maybe$Mn$Eq;
        Object x;

        public frame18() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 17, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1225");
            this.lambda$Fn17 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 17) {
                return lambda29(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda29(Object y) {
            return ((Scheme.applyToArgs.apply3(this.maybe$Mn$Eq, this.x, y) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 17) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object delete$Ex(Object x, Object lis, Object maybe$Mn$Eq) {
        frame18 frame182 = new frame18();
        frame182.x = x;
        frame182.maybe$Mn$Eq = maybe$Mn$Eq;
        return filter$Ex(frame182.lambda$Fn17, lis);
    }

    public static Object deleteDuplicates(Object lis, Procedure maybe$Mn$Eq) {
        frame19 frame192 = new frame19();
        frame192.maybe$Mn$Eq = maybe$Mn$Eq;
        return frame192.lambda30recur(lis);
    }

    /* compiled from: srfi1.scm */
    public class frame19 extends ModuleBody {
        Procedure maybe$Mn$Eq;

        public Object lambda30recur(Object lis) {
            if (srfi1.isNullList(lis) != Boolean.FALSE) {
                return lis;
            }
            Object x = lists.car.apply1(lis);
            Object tail = lists.cdr.apply1(lis);
            Object new$Mntail = lambda30recur(srfi1.delete(x, tail, this.maybe$Mn$Eq));
            return tail != new$Mntail ? lists.cons(x, new$Mntail) : lis;
        }
    }

    public static Object deleteDuplicates$Ex(Object lis, Procedure maybe$Mn$Eq) {
        frame20 frame202 = new frame20();
        frame202.maybe$Mn$Eq = maybe$Mn$Eq;
        return frame202.lambda31recur(lis);
    }

    /* compiled from: srfi1.scm */
    public class frame20 extends ModuleBody {
        Procedure maybe$Mn$Eq;

        public Object lambda31recur(Object lis) {
            if (srfi1.isNullList(lis) != Boolean.FALSE) {
                return lis;
            }
            Object x = lists.car.apply1(lis);
            Object tail = lists.cdr.apply1(lis);
            Object new$Mntail = lambda31recur(srfi1.delete$Ex(x, tail, this.maybe$Mn$Eq));
            return tail != new$Mntail ? lists.cons(x, new$Mntail) : lis;
        }
    }

    public static Pair alistCons(Object key, Object datum, Object alist) {
        return lists.cons(lists.cons(key, datum), alist);
    }

    public static LList alistCopy(Object alist) {
        Object obj = LList.Empty;
        Object arg0 = alist;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                Object elt = arg02.getCar();
                obj = Pair.make(lists.cons(lists.car.apply1(elt), lists.cdr.apply1(elt)), obj);
                arg0 = arg03;
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, arg0);
            }
        }
        return LList.reverseInPlace(obj);
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 82:
                try {
                    return listCopy((LList) obj);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "list-copy", 1, obj);
                }
            case 83:
                try {
                    return iota(LangObjType.coerceIntNum(obj));
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "iota", 1, obj);
                }
            case 87:
                return isProperList(obj);
            case 88:
                return isDottedList(obj);
            case 89:
                return isCircularList(obj);
            case 90:
                return isNotPair(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 91:
                return isNullList(obj);
            case 93:
                return length$Pl(obj);
            case 95:
                return fifth(obj);
            case 96:
                return sixth(obj);
            case 97:
                return seventh(obj);
            case 98:
                return eighth(obj);
            case 99:
                return ninth(obj);
            case 100:
                return tenth(obj);
            case 101:
                return car$PlCdr(obj);
            case 110:
                return last(obj);
            case 111:
                try {
                    return lastPair((Pair) obj);
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "last-pair", 1, obj);
                }
            case 112:
                return unzip1(obj);
            case 113:
                return unzip2(obj);
            case 114:
                return unzip3(obj);
            case 115:
                return unzip4(obj);
            case 116:
                return unzip5(obj);
            case 120:
                return concatenate(obj);
            case 121:
                return concatenate$Ex(obj);
            case 147:
                return deleteDuplicates(obj);
            case 149:
                return deleteDuplicates$Ex(obj);
            case 152:
                return alistCopy(obj);
            default:
                return super.apply1(moduleMethod, obj);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame21 extends ModuleBody {
        Object key;
        final ModuleMethod lambda$Fn18;
        Object maybe$Mn$Eq;

        public frame21() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 18, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1280");
            this.lambda$Fn18 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 18) {
                return lambda32(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda32(Object elt) {
            return ((Scheme.applyToArgs.apply3(this.maybe$Mn$Eq, this.key, lists.car.apply1(elt)) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 18) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object alistDelete(Object key, Object alist, Object maybe$Mn$Eq) {
        frame21 frame212 = new frame21();
        frame212.key = key;
        frame212.maybe$Mn$Eq = maybe$Mn$Eq;
        return filter(frame212.lambda$Fn18, alist);
    }

    /* compiled from: srfi1.scm */
    public class frame22 extends ModuleBody {
        Object key;
        final ModuleMethod lambda$Fn19;
        Object maybe$Mn$Eq;

        public frame22() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 19, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1283");
            this.lambda$Fn19 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 19) {
                return lambda33(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda33(Object elt) {
            return ((Scheme.applyToArgs.apply3(this.maybe$Mn$Eq, this.key, lists.car.apply1(elt)) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 19) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object alistDelete$Ex(Object key, Object alist, Object maybe$Mn$Eq) {
        frame22 frame222 = new frame22();
        frame222.key = key;
        frame222.maybe$Mn$Eq = maybe$Mn$Eq;
        return filter$Ex(frame222.lambda$Fn19, alist);
    }

    public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
        switch (moduleMethod.selector) {
            case 83:
                try {
                    try {
                        try {
                            return iota(LangObjType.coerceIntNum(obj), LangObjType.coerceNumeric(obj2), LangObjType.coerceNumeric(obj3));
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "iota", 3, obj3);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "iota", 2, obj2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "iota", 1, obj);
                }
            case 130:
                try {
                    return reduce((Procedure) obj, obj2, obj3);
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "reduce", 1, obj);
                }
            case 131:
                try {
                    return reduceRight((Procedure) obj, obj2, obj3);
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "reduce-right", 1, obj);
                }
            case 143:
                return delete(obj, obj2, obj3);
            case 145:
                return delete$Ex(obj, obj2, obj3);
            case 151:
                return alistCons(obj, obj2, obj3);
            case 153:
                return alistDelete(obj, obj2, obj3);
            case 155:
                return alistDelete$Ex(obj, obj2, obj3);
            default:
                return super.apply3(moduleMethod, obj, obj2, obj3);
        }
    }

    public static Object find(Object pred, Object list) {
        try {
            Object temp = findTail((Procedure) pred, list);
            if (temp != Boolean.FALSE) {
                return lists.car.apply1(temp);
            }
            return Boolean.FALSE;
        } catch (ClassCastException e) {
            throw new WrongType(e, "find-tail", 0, pred);
        }
    }

    public static Object findTail(Procedure pred, Object list) {
        while (true) {
            Object isNullList = isNullList(list);
            try {
                boolean x = ((isNullList != Boolean.FALSE ? 1 : 0) + 1) & true;
                if (!x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                if (pred.apply1(lists.car.apply1(list)) != Boolean.FALSE) {
                    return list;
                }
                list = lists.cdr.apply1(list);
            } catch (ClassCastException e) {
                throw new WrongType(e, "x", -2, isNullList);
            }
        }
    }

    public static Object takeWhile(Procedure pred, Object lis) {
        frame23 frame232 = new frame23();
        frame232.pred = pred;
        return frame232.lambda34recur(lis);
    }

    /* compiled from: srfi1.scm */
    public class frame23 extends ModuleBody {
        Procedure pred;

        public Object lambda34recur(Object lis) {
            if (srfi1.isNullList(lis) != Boolean.FALSE) {
                return LList.Empty;
            }
            Object x = lists.car.apply1(lis);
            return this.pred.apply1(x) != Boolean.FALSE ? lists.cons(x, lambda34recur(lists.cdr.apply1(lis))) : LList.Empty;
        }
    }

    public static Object dropWhile(Procedure pred, Object lis) {
        while (isNullList(lis) == Boolean.FALSE) {
            if (pred.apply1(lists.car.apply1(lis)) == Boolean.FALSE) {
                return lis;
            }
            lis = lists.cdr.apply1(lis);
        }
        return LList.Empty;
    }

    public static Object takeWhile$Ex(Procedure pred, Object lis) {
        Object x = isNullList(lis);
        if (x == Boolean.FALSE ? pred.apply1(lists.car.apply1(lis)) == Boolean.FALSE : x != Boolean.FALSE) {
            return LList.Empty;
        }
        Object prev = lists.cdr.apply1(lis);
        Object prev2 = lis;
        while (lists.isPair(prev)) {
            if (pred.apply1(lists.car.apply1(prev)) != Boolean.FALSE) {
                prev2 = prev;
                prev = lists.cdr.apply1(prev);
            } else {
                try {
                    lists.setCdr$Ex((Pair) prev2, LList.Empty);
                    return lis;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "set-cdr!", 1, prev2);
                }
            }
        }
        return lis;
    }

    public static Object span(Procedure pred, Object lis) {
        LList lList = LList.Empty;
        while (isNullList(lis) == Boolean.FALSE) {
            Object head = lists.car.apply1(lis);
            if (pred.apply1(head) != Boolean.FALSE) {
                lis = lists.cdr.apply1(lis);
                lList = lists.cons(head, lList);
            } else {
                Object[] objArr = new Object[2];
                try {
                    objArr[0] = lists.reverse$Ex(lList);
                    objArr[1] = lis;
                    return misc.values(objArr);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "reverse!", 1, (Object) lList);
                }
            }
        }
        Object[] objArr2 = new Object[2];
        try {
            objArr2[0] = lists.reverse$Ex(lList);
            objArr2[1] = lis;
            return misc.values(objArr2);
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "reverse!", 1, (Object) lList);
        }
    }

    public static Object span$Ex(Procedure pred, Object lis) {
        Object suffix;
        Object x = isNullList(lis);
        if (x == Boolean.FALSE ? pred.apply1(lists.car.apply1(lis)) == Boolean.FALSE : x != Boolean.FALSE) {
            return misc.values(LList.Empty, lis);
        }
        Object prev = lists.cdr.apply1(lis);
        Object prev2 = lis;
        while (true) {
            if (isNullList(prev) != Boolean.FALSE) {
                suffix = prev;
                break;
            } else if (pred.apply1(lists.car.apply1(prev)) != Boolean.FALSE) {
                prev2 = prev;
                prev = lists.cdr.apply1(prev);
            } else {
                try {
                    lists.setCdr$Ex((Pair) prev2, LList.Empty);
                    suffix = prev;
                    break;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "set-cdr!", 1, prev2);
                }
            }
        }
        return misc.values(lis, suffix);
    }

    /* compiled from: srfi1.scm */
    public class frame24 extends ModuleBody {
        final ModuleMethod lambda$Fn20;
        Object pred;

        public frame24() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 20, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1343");
            this.lambda$Fn20 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 20) {
                return lambda35(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda35(Object x) {
            return ((Scheme.applyToArgs.apply2(this.pred, x) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 20) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame25 extends ModuleBody {
        final ModuleMethod lambda$Fn21;
        Object pred;

        public frame25() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 21, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1344");
            this.lambda$Fn21 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 21) {
                return lambda36(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda36(Object x) {
            return ((Scheme.applyToArgs.apply2(this.pred, x) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 21) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* renamed from: break  reason: not valid java name */
    public static Object m0break(Object pred, Object lis) {
        frame24 frame242 = new frame24();
        frame242.pred = pred;
        return span(frame242.lambda$Fn20, lis);
    }

    public static Object break$Ex(Object pred, Object lis) {
        frame25 frame252 = new frame25();
        frame252.pred = pred;
        return span$Ex(frame252.lambda$Fn21, lis);
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        switch (moduleMethod.selector) {
            case 78:
                return xcons(obj, obj2);
            case 80:
                try {
                    return listTabulate(obj, (Procedure) obj2);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "list-tabulate", 2, obj2);
                }
            case 83:
                try {
                    try {
                        return iota(LangObjType.coerceIntNum(obj), LangObjType.coerceNumeric(obj2));
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "iota", 2, obj2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "iota", 1, obj);
                }
            case 102:
                try {
                    return take(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "take", 2, obj2);
                }
            case 103:
                try {
                    return drop(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "drop", 2, obj2);
                }
            case 104:
                try {
                    return take$Ex(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "take!", 2, obj2);
                }
            case 105:
                try {
                    return takeRight(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e7) {
                    throw new WrongType(e7, "take-right", 2, obj2);
                }
            case 106:
                try {
                    return dropRight(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "drop-right", 2, obj2);
                }
            case 107:
                try {
                    return dropRight$Ex(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e9) {
                    throw new WrongType(e9, "drop-right!", 2, obj2);
                }
            case 108:
                try {
                    return splitAt(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e10) {
                    throw new WrongType(e10, "split-at", 2, obj2);
                }
            case 109:
                try {
                    return splitAt$Ex(obj, LangObjType.coerceIntNum(obj2));
                } catch (ClassCastException e11) {
                    throw new WrongType(e11, "split-at!", 2, obj2);
                }
            case 118:
                return appendReverse(obj, obj2);
            case 119:
                return appendReverse$Ex(obj, obj2);
            case 137:
                try {
                    return filter((Procedure) obj, obj2);
                } catch (ClassCastException e12) {
                    throw new WrongType(e12, "filter", 1, obj);
                }
            case 138:
                try {
                    return filter$Ex((Procedure) obj, obj2);
                } catch (ClassCastException e13) {
                    throw new WrongType(e13, "filter!", 1, obj);
                }
            case 139:
                try {
                    return partition((Procedure) obj, obj2);
                } catch (ClassCastException e14) {
                    throw new WrongType(e14, "partition", 1, obj);
                }
            case 140:
                try {
                    return partition$Ex((Procedure) obj, obj2);
                } catch (ClassCastException e15) {
                    throw new WrongType(e15, "partition!", 1, obj);
                }
            case 141:
                return remove(obj, obj2);
            case 142:
                return remove$Ex(obj, obj2);
            case 143:
                return delete(obj, obj2);
            case 145:
                return delete$Ex(obj, obj2);
            case 147:
                try {
                    return deleteDuplicates(obj, (Procedure) obj2);
                } catch (ClassCastException e16) {
                    throw new WrongType(e16, "delete-duplicates", 2, obj2);
                }
            case 149:
                try {
                    return deleteDuplicates$Ex(obj, (Procedure) obj2);
                } catch (ClassCastException e17) {
                    throw new WrongType(e17, "delete-duplicates!", 2, obj2);
                }
            case 153:
                return alistDelete(obj, obj2);
            case 155:
                return alistDelete$Ex(obj, obj2);
            case 157:
                return find(obj, obj2);
            case 158:
                try {
                    return findTail((Procedure) obj, obj2);
                } catch (ClassCastException e18) {
                    throw new WrongType(e18, "find-tail", 1, obj);
                }
            case 159:
                try {
                    return takeWhile((Procedure) obj, obj2);
                } catch (ClassCastException e19) {
                    throw new WrongType(e19, "take-while", 1, obj);
                }
            case ComponentConstants.TEXTBOX_PREFERRED_WIDTH:
                try {
                    return dropWhile((Procedure) obj, obj2);
                } catch (ClassCastException e20) {
                    throw new WrongType(e20, "drop-while", 1, obj);
                }
            case 161:
                try {
                    return takeWhile$Ex((Procedure) obj, obj2);
                } catch (ClassCastException e21) {
                    throw new WrongType(e21, "take-while!", 1, obj);
                }
            case 162:
                try {
                    return span((Procedure) obj, obj2);
                } catch (ClassCastException e22) {
                    throw new WrongType(e22, "span", 1, obj);
                }
            case 163:
                try {
                    return span$Ex((Procedure) obj, obj2);
                } catch (ClassCastException e23) {
                    throw new WrongType(e23, "span!", 1, obj);
                }
            case 164:
                return m0break(obj, obj2);
            case 165:
                return break$Ex(obj, obj2);
            case 182:
                return frame61.lambda84(obj, obj2);
            case 183:
                return frame71.lambda100(obj, obj2);
            default:
                return super.apply2(moduleMethod, obj, obj2);
        }
    }

    public static Object any$V(Procedure pred, Object lis1, Object[] argsArray) {
        int i = 0;
        frame26 frame262 = new frame26();
        frame262.pred = pred;
        frame262.lis1 = lis1;
        frame262.lists = LList.makeList(argsArray, 0);
        if (lists.isPair(frame262.lists)) {
            return call_with_values.callWithValues(frame262.lambda$Fn22, frame262.lambda$Fn23);
        }
        Object isNullList = isNullList(frame262.lis1);
        try {
            if (isNullList != Boolean.FALSE) {
                i = 1;
            }
            boolean x = (i + 1) & true;
            if (!x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            Object head = lists.car.apply1(frame262.lis1);
            for (Object apply1 = lists.cdr.apply1(frame262.lis1); isNullList(apply1) == Boolean.FALSE; apply1 = lists.cdr.apply1(apply1)) {
                Object x2 = frame262.pred.apply1(head);
                if (x2 != Boolean.FALSE) {
                    return x2;
                }
                head = lists.car.apply1(apply1);
            }
            return frame262.pred.apply1(head);
        } catch (ClassCastException e) {
            throw new WrongType(e, "x", -2, isNullList);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame26 extends ModuleBody {
        final ModuleMethod lambda$Fn22 = new ModuleMethod(this, 22, (Object) null, 0);
        final ModuleMethod lambda$Fn23;
        Object lis1;
        LList lists;
        Procedure pred;

        public frame26() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 23, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1350");
            this.lambda$Fn23 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 22 ? lambda37() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 22) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 23 ? lambda38(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda37() {
            return srfi1.$PcCars$PlCdrs(lists.cons(this.lis1, this.lists));
        }

        /* access modifiers changed from: package-private */
        public Object lambda38(Object heads, Object tails) {
            boolean x = lists.isPair(heads);
            if (!x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            while (true) {
                Object split = srfi1.$PcCars$PlCdrs$SlPair(tails);
                Object next$Mnheads = lists.car.apply1(split);
                Object next$Mntails = lists.cdr.apply1(split);
                if (!lists.isPair(next$Mnheads)) {
                    return Scheme.apply.apply2(this.pred, heads);
                }
                Object x2 = Scheme.apply.apply2(this.pred, heads);
                if (x2 != Boolean.FALSE) {
                    return x2;
                }
                tails = next$Mntails;
                heads = next$Mnheads;
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 23) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object every$V(Procedure pred, Object lis1, Object[] argsArray) {
        frame27 frame272 = new frame27();
        frame272.pred = pred;
        frame272.lis1 = lis1;
        frame272.lists = LList.makeList(argsArray, 0);
        if (lists.isPair(frame272.lists)) {
            return call_with_values.callWithValues(frame272.lambda$Fn24, frame272.lambda$Fn25);
        }
        Object x = isNullList(frame272.lis1);
        if (x != Boolean.FALSE) {
            return x;
        }
        Object head = lists.car.apply1(frame272.lis1);
        for (Object apply1 = lists.cdr.apply1(frame272.lis1); isNullList(apply1) == Boolean.FALSE; apply1 = lists.cdr.apply1(apply1)) {
            Object x2 = frame272.pred.apply1(head);
            if (x2 == Boolean.FALSE) {
                return x2;
            }
            head = lists.car.apply1(apply1);
        }
        return frame272.pred.apply1(head);
    }

    /* compiled from: srfi1.scm */
    public class frame27 extends ModuleBody {
        final ModuleMethod lambda$Fn24 = new ModuleMethod(this, 26, (Object) null, 0);
        final ModuleMethod lambda$Fn25;
        Object lis1;
        LList lists;
        Procedure pred;

        public frame27() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 27, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1378");
            this.lambda$Fn25 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 26 ? lambda39() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 26) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 27 ? lambda40(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda39() {
            return srfi1.$PcCars$PlCdrs(lists.cons(this.lis1, this.lists));
        }

        /* access modifiers changed from: package-private */
        public Object lambda40(Object heads, Object tails) {
            frame28 frame28 = new frame28();
            frame28.staticLink = this;
            boolean x = ((lists.isPair(heads) ? 1 : 0) + true) & true;
            if (x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            return frame28.lambda41lp(heads, tails);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 27) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame28 extends ModuleBody {
        frame27 staticLink;

        public Object lambda41lp(Object heads, Object tails) {
            frame29 frame29 = new frame29();
            frame29.staticLink = this;
            frame29.heads = heads;
            frame29.tails = tails;
            return call_with_values.callWithValues(frame29.lambda$Fn26, frame29.lambda$Fn27);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame29 extends ModuleBody {
        Object heads;
        final ModuleMethod lambda$Fn26 = new ModuleMethod(this, 24, (Object) null, 0);
        final ModuleMethod lambda$Fn27;
        frame28 staticLink;
        Object tails;

        public frame29() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 25, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1381");
            this.lambda$Fn27 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 24 ? lambda42() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 24) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 25 ? lambda43(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda42() {
            return srfi1.$PcCars$PlCdrs(this.tails);
        }

        /* access modifiers changed from: package-private */
        public Object lambda43(Object next$Mnheads, Object next$Mntails) {
            if (!lists.isPair(next$Mnheads)) {
                return Scheme.apply.apply2(this.staticLink.staticLink.pred, this.heads);
            }
            Object x = Scheme.apply.apply2(this.staticLink.staticLink.pred, this.heads);
            if (x != Boolean.FALSE) {
                return this.staticLink.lambda41lp(next$Mnheads, next$Mntails);
            }
            return x;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 25) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object listIndex$V(Procedure pred, Object lis1, Object[] argsArray) {
        int i;
        frame30 frame302 = new frame30();
        frame302.pred = pred;
        LList lists = LList.makeList(argsArray, 0);
        if (lists.isPair(lists)) {
            return frame302.lambda44lp(lists.cons(lis1, lists), Lit0);
        }
        Object obj = Lit0;
        Object lis = lis1;
        while (true) {
            Object isNullList = isNullList(lis);
            try {
                if (isNullList != Boolean.FALSE) {
                    i = 1;
                } else {
                    i = 0;
                }
                boolean x = (i + 1) & true;
                if (!x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                if (frame302.pred.apply1(lists.car.apply1(lis)) != Boolean.FALSE) {
                    return obj;
                }
                lis = lists.cdr.apply1(lis);
                obj = AddOp.$Pl.apply2(obj, Lit1);
            } catch (ClassCastException e) {
                throw new WrongType(e, "x", -2, isNullList);
            }
        }
    }

    /* compiled from: srfi1.scm */
    public class frame30 extends ModuleBody {
        Procedure pred;

        public Object lambda44lp(Object lists, Object n) {
            frame31 frame31 = new frame31();
            frame31.staticLink = this;
            frame31.lists = lists;
            frame31.n = n;
            return call_with_values.callWithValues(frame31.lambda$Fn28, frame31.lambda$Fn29);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame31 extends ModuleBody {
        final ModuleMethod lambda$Fn28 = new ModuleMethod(this, 28, (Object) null, 0);
        final ModuleMethod lambda$Fn29;
        Object lists;
        Object n;
        frame30 staticLink;

        public frame31() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 29, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1404");
            this.lambda$Fn29 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 28 ? lambda45() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 28) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 29 ? lambda46(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda45() {
            return srfi1.$PcCars$PlCdrs(this.lists);
        }

        /* access modifiers changed from: package-private */
        public Object lambda46(Object heads, Object tails) {
            boolean x = lists.isPair(heads);
            if (!x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            if (Scheme.apply.apply2(this.staticLink.pred, heads) != Boolean.FALSE) {
                return this.n;
            }
            return this.staticLink.lambda44lp(tails, AddOp.$Pl.apply2(this.n, srfi1.Lit1));
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 29) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame72 extends ModuleBody {
        Object $Eq;
        final ModuleMethod lambda$Fn79;
        Object lis2;

        public frame72() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 77, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1443");
            this.lambda$Fn79 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 77 ? lambda101(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda101(Object x) {
            Object obj = this.lis2;
            Object obj2 = this.$Eq;
            try {
                return lists.member(x, obj, (Procedure) obj2);
            } catch (ClassCastException e) {
                throw new WrongType(e, "member", 3, obj2);
            }
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 77) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    static Object $PcLset2$Ls$Eq(Object $Eq, Object lis1, Object lis2) {
        frame72 frame722 = new frame72();
        frame722.$Eq = $Eq;
        frame722.lis2 = lis2;
        return every$V(frame722.lambda$Fn79, lis1, new Object[0]);
    }

    public static Object lset$Ls$Eq$V(Procedure $Eq, Object[] argsArray) {
        boolean x;
        Object x2;
        LList lists = LList.makeList(argsArray, 0);
        boolean x3 = ((lists.isPair(lists) ? 1 : 0) + true) & true;
        if (x3) {
            return x3 ? Boolean.TRUE : Boolean.FALSE;
        }
        Object s1 = lists.car.apply1(lists);
        Object apply1 = lists.cdr.apply1(lists);
        while (true) {
            boolean x4 = ((lists.isPair(apply1) ? 1 : 0) + true) & true;
            if (x4) {
                return x4 ? Boolean.TRUE : Boolean.FALSE;
            }
            Object s2 = lists.car.apply1(apply1);
            apply1 = lists.cdr.apply1(apply1);
            if (s2 == s1) {
                x = true;
            } else {
                x = false;
            }
            if (x) {
                x2 = x ? Boolean.TRUE : Boolean.FALSE;
            } else {
                x2 = $PcLset2$Ls$Eq($Eq, s1, s2);
            }
            if (x2 == Boolean.FALSE) {
                return x2;
            }
            s1 = s2;
        }
    }

    public static Object lset$Eq$V(Procedure $Eq, Object[] argsArray) {
        boolean x;
        Object x2;
        LList lists = LList.makeList(argsArray, 0);
        boolean x3 = ((lists.isPair(lists) ? 1 : 0) + true) & true;
        if (x3) {
            return x3 ? Boolean.TRUE : Boolean.FALSE;
        }
        Object s1 = lists.car.apply1(lists);
        Object apply1 = lists.cdr.apply1(lists);
        while (true) {
            boolean x4 = ((lists.isPair(apply1) ? 1 : 0) + true) & true;
            if (x4) {
                return x4 ? Boolean.TRUE : Boolean.FALSE;
            }
            Object s2 = lists.car.apply1(apply1);
            apply1 = lists.cdr.apply1(apply1);
            if (s1 == s2) {
                x = true;
            } else {
                x = false;
            }
            if (x) {
                x2 = x ? Boolean.TRUE : Boolean.FALSE;
            } else {
                x2 = $PcLset2$Ls$Eq($Eq, s1, s2);
                if (x2 != Boolean.FALSE) {
                    x2 = $PcLset2$Ls$Eq($Eq, s2, s1);
                }
            }
            if (x2 == Boolean.FALSE) {
                return x2;
            }
            s1 = s2;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame32 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn30;

        public frame32() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 30, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1466");
            this.lambda$Fn30 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 30 ? lambda47(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda47(Object elt, Object ans) {
            return lists.member(elt, ans, this.$Eq) != Boolean.FALSE ? ans : lists.cons(elt, ans);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 30) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object lsetAdjoin$V(Procedure $Eq, Object lis, Object[] argsArray) {
        frame32 frame322 = new frame32();
        frame322.$Eq = $Eq;
        return fold$V(frame322.lambda$Fn30, lis, LList.makeList(argsArray, 0), new Object[0]);
    }

    /* compiled from: srfi1.scm */
    public class frame33 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn31;
        final ModuleMethod lambda$Fn32;

        public frame33() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 32, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1476");
            this.lambda$Fn32 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 33, (Object) null, 8194);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1471");
            this.lambda$Fn31 = moduleMethod2;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 32:
                    return lambda49(obj, obj2);
                case 33:
                    return lambda48(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda48(Object lis, Object ans) {
            if (lists.isNull(lis)) {
                return ans;
            }
            if (lists.isNull(ans)) {
                return lis;
            }
            return lis != ans ? srfi1.fold$V(this.lambda$Fn32, ans, lis, new Object[0]) : ans;
        }

        /* access modifiers changed from: package-private */
        public Object lambda49(Object elt, Object ans) {
            frame34 frame34 = new frame34();
            frame34.staticLink = this;
            frame34.elt = elt;
            return srfi1.any$V(frame34.lambda$Fn33, ans, new Object[0]) != Boolean.FALSE ? ans : lists.cons(frame34.elt, ans);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 32:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 33:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }
    }

    public static Object lsetUnion$V(Procedure $Eq, Object[] argsArray) {
        frame33 frame332 = new frame33();
        frame332.$Eq = $Eq;
        return reduce(frame332.lambda$Fn31, LList.Empty, LList.makeList(argsArray, 0));
    }

    /* compiled from: srfi1.scm */
    public class frame34 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn33;
        frame33 staticLink;

        public frame34() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 31, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1476");
            this.lambda$Fn33 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 31 ? lambda50(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda50(Object x) {
            return this.staticLink.$Eq.apply2(x, this.elt);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 31) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame35 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn34;
        final ModuleMethod lambda$Fn35;

        public frame35() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 35, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1488");
            this.lambda$Fn35 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 36, (Object) null, 8194);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1483");
            this.lambda$Fn34 = moduleMethod2;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 35:
                    return lambda52(obj, obj2);
                case 36:
                    return lambda51(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda51(Object lis, Object ans) {
            if (lists.isNull(lis)) {
                return ans;
            }
            if (lists.isNull(ans)) {
                return lis;
            }
            return lis != ans ? srfi1.pairFold$V(this.lambda$Fn35, ans, lis, new Object[0]) : ans;
        }

        /* access modifiers changed from: package-private */
        public Object lambda52(Object pair, Object ans) {
            frame36 frame36 = new frame36();
            frame36.staticLink = this;
            frame36.elt = lists.car.apply1(pair);
            if (srfi1.any$V(frame36.lambda$Fn36, ans, new Object[0]) != Boolean.FALSE) {
                return ans;
            }
            try {
                lists.setCdr$Ex((Pair) pair, ans);
                return pair;
            } catch (ClassCastException e) {
                throw new WrongType(e, "set-cdr!", 1, pair);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 35:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 36:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }
    }

    public static Object lsetUnion$Ex$V(Procedure $Eq, Object[] argsArray) {
        frame35 frame352 = new frame35();
        frame352.$Eq = $Eq;
        return reduce(frame352.lambda$Fn34, LList.Empty, LList.makeList(argsArray, 0));
    }

    /* compiled from: srfi1.scm */
    public class frame36 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn36;
        frame35 staticLink;

        public frame36() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 34, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1490");
            this.lambda$Fn36 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 34 ? lambda53(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda53(Object x) {
            return this.staticLink.$Eq.apply2(x, this.elt);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 34) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object lsetIntersection$V(Procedure $Eq, Object lis1, Object[] argsArray) {
        frame37 frame372 = new frame37();
        frame372.$Eq = $Eq;
        frame372.lists = delete(lis1, LList.makeList(argsArray, 0), Scheme.isEq);
        if (any$V(null$Mnlist$Qu, frame372.lists, new Object[0]) != Boolean.FALSE) {
            return LList.Empty;
        }
        return !lists.isNull(frame372.lists) ? filter(frame372.lambda$Fn37, lis1) : lis1;
    }

    /* compiled from: srfi1.scm */
    public class frame37 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn37;
        Object lists;

        public frame37() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 38, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1501");
            this.lambda$Fn37 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 38 ? lambda54(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda54(Object x) {
            frame38 frame38 = new frame38();
            frame38.staticLink = this;
            frame38.x = x;
            return srfi1.every$V(frame38.lambda$Fn38, this.lists, new Object[0]);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 38) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame38 extends ModuleBody {
        final ModuleMethod lambda$Fn38;
        frame37 staticLink;
        Object x;

        public frame38() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 37, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1502");
            this.lambda$Fn38 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 37 ? lambda55(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda55(Object lis) {
            return lists.member(this.x, lis, this.staticLink.$Eq);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 37) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object lsetIntersection$Ex$V(Procedure $Eq, Object lis1, Object[] argsArray) {
        frame39 frame392 = new frame39();
        frame392.$Eq = $Eq;
        frame392.lists = delete(lis1, LList.makeList(argsArray, 0), Scheme.isEq);
        if (any$V(null$Mnlist$Qu, frame392.lists, new Object[0]) != Boolean.FALSE) {
            return LList.Empty;
        }
        return !lists.isNull(frame392.lists) ? filter$Ex(frame392.lambda$Fn39, lis1) : lis1;
    }

    /* compiled from: srfi1.scm */
    public class frame39 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn39;
        Object lists;

        public frame39() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 40, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1509");
            this.lambda$Fn39 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 40 ? lambda56(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda56(Object x) {
            frame40 frame40 = new frame40();
            frame40.staticLink = this;
            frame40.x = x;
            return srfi1.every$V(frame40.lambda$Fn40, this.lists, new Object[0]);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 40) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame40 extends ModuleBody {
        final ModuleMethod lambda$Fn40;
        frame39 staticLink;
        Object x;

        public frame40() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 39, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1510");
            this.lambda$Fn40 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 39 ? lambda57(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda57(Object lis) {
            return lists.member(this.x, lis, this.staticLink.$Eq);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 39) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object lsetDifference$V(Procedure $Eq, Object lis1, Object[] argsArray) {
        frame41 frame412 = new frame41();
        frame412.$Eq = $Eq;
        frame412.lists = filter(lists.pair$Qu, LList.makeList(argsArray, 0));
        if (lists.isNull(frame412.lists)) {
            return lis1;
        }
        if (lists.memq(lis1, frame412.lists) != Boolean.FALSE) {
            return LList.Empty;
        }
        return filter(frame412.lambda$Fn41, lis1);
    }

    /* compiled from: srfi1.scm */
    public class frame41 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn41;
        Object lists;

        public frame41() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 42, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1518");
            this.lambda$Fn41 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 42 ? lambda58(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda58(Object x) {
            frame42 frame42 = new frame42();
            frame42.staticLink = this;
            frame42.x = x;
            return srfi1.every$V(frame42.lambda$Fn42, this.lists, new Object[0]);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 42) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame42 extends ModuleBody {
        final ModuleMethod lambda$Fn42;
        frame41 staticLink;
        Object x;

        public frame42() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 41, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1519");
            this.lambda$Fn42 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 41) {
                return lambda59(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda59(Object lis) {
            return ((lists.member(this.x, lis, this.staticLink.$Eq) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 41) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object lsetDifference$Ex$V(Procedure $Eq, Object lis1, Object[] argsArray) {
        frame43 frame432 = new frame43();
        frame432.$Eq = $Eq;
        frame432.lists = filter(lists.pair$Qu, LList.makeList(argsArray, 0));
        if (lists.isNull(frame432.lists)) {
            return lis1;
        }
        if (lists.memq(lis1, frame432.lists) != Boolean.FALSE) {
            return LList.Empty;
        }
        return filter$Ex(frame432.lambda$Fn43, lis1);
    }

    /* compiled from: srfi1.scm */
    public class frame43 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn43;
        Object lists;

        public frame43() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 44, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1527");
            this.lambda$Fn43 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 44 ? lambda60(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda60(Object x) {
            frame44 frame44 = new frame44();
            frame44.staticLink = this;
            frame44.x = x;
            return srfi1.every$V(frame44.lambda$Fn44, this.lists, new Object[0]);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 44) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame44 extends ModuleBody {
        final ModuleMethod lambda$Fn44;
        frame43 staticLink;
        Object x;

        public frame44() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 43, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1528");
            this.lambda$Fn44 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 43) {
                return lambda61(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda61(Object lis) {
            return ((lists.member(this.x, lis, this.staticLink.$Eq) != Boolean.FALSE ? 1 : 0) + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 43) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame45 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn45;

        public frame45() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 48, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1534");
            this.lambda$Fn45 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 48 ? lambda62(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda62(Object b, Object a) {
            frame46 frame46 = new frame46();
            frame46.staticLink = this;
            frame46.b = b;
            frame46.a = a;
            return call_with_values.callWithValues(frame46.lambda$Fn46, frame46.lambda$Fn47);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 48) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object lsetXor$V(Procedure $Eq, Object[] argsArray) {
        frame45 frame452 = new frame45();
        frame452.$Eq = $Eq;
        return reduce(frame452.lambda$Fn45, LList.Empty, LList.makeList(argsArray, 0));
    }

    /* compiled from: srfi1.scm */
    public class frame46 extends ModuleBody {
        Object a;
        Object b;
        final ModuleMethod lambda$Fn46 = new ModuleMethod(this, 46, (Object) null, 0);
        final ModuleMethod lambda$Fn47;
        frame45 staticLink;

        public frame46() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 47, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1544");
            this.lambda$Fn47 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 46 ? lambda63() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 46) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 47 ? lambda64(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda63() {
            return srfi1.lsetDiff$PlIntersection$V(this.staticLink.$Eq, this.a, new Object[]{this.b});
        }

        /* access modifiers changed from: package-private */
        public Object lambda64(Object a$Mnb, Object aIntB) {
            frame47 frame47 = new frame47();
            frame47.staticLink = this;
            frame47.a$Mnint$Mnb = aIntB;
            if (lists.isNull(a$Mnb)) {
                return srfi1.lsetDifference$V(this.staticLink.$Eq, this.b, new Object[]{this.a});
            } else if (!lists.isNull(frame47.a$Mnint$Mnb)) {
                return srfi1.fold$V(frame47.lambda$Fn48, a$Mnb, this.b, new Object[0]);
            } else {
                return append.append$V(new Object[]{this.b, this.a});
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 47) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame47 extends ModuleBody {
        Object a$Mnint$Mnb;
        final ModuleMethod lambda$Fn48;
        frame46 staticLink;

        public frame47() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 45, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1547");
            this.lambda$Fn48 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 45 ? lambda65(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda65(Object xb, Object ans) {
            return lists.member(xb, this.a$Mnint$Mnb, this.staticLink.staticLink.$Eq) != Boolean.FALSE ? ans : lists.cons(xb, ans);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 45) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame48 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn49;

        public frame48() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 52, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1555");
            this.lambda$Fn49 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 52 ? lambda66(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda66(Object b, Object a) {
            frame49 frame49 = new frame49();
            frame49.staticLink = this;
            frame49.b = b;
            frame49.a = a;
            return call_with_values.callWithValues(frame49.lambda$Fn50, frame49.lambda$Fn51);
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 52) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object lsetXor$Ex$V(Procedure $Eq, Object[] argsArray) {
        frame48 frame482 = new frame48();
        frame482.$Eq = $Eq;
        return reduce(frame482.lambda$Fn49, LList.Empty, LList.makeList(argsArray, 0));
    }

    /* compiled from: srfi1.scm */
    public class frame49 extends ModuleBody {
        Object a;
        Object b;
        final ModuleMethod lambda$Fn50 = new ModuleMethod(this, 50, (Object) null, 0);
        final ModuleMethod lambda$Fn51;
        frame48 staticLink;

        public frame49() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 51, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1565");
            this.lambda$Fn51 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 50 ? lambda67() : super.apply0(moduleMethod);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 50) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 51 ? lambda68(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda67() {
            return srfi1.lsetDiff$PlIntersection$Ex$V(this.staticLink.$Eq, this.a, new Object[]{this.b});
        }

        /* access modifiers changed from: package-private */
        public Object lambda68(Object a$Mnb, Object aIntB) {
            frame50 frame50 = new frame50();
            frame50.staticLink = this;
            frame50.a$Mnint$Mnb = aIntB;
            if (lists.isNull(a$Mnb)) {
                return srfi1.lsetDifference$Ex$V(this.staticLink.$Eq, this.b, new Object[]{this.a});
            } else if (!lists.isNull(frame50.a$Mnint$Mnb)) {
                return srfi1.pairFold$V(frame50.lambda$Fn52, a$Mnb, this.b, new Object[0]);
            } else {
                return srfi1.append$Ex$V(new Object[]{this.b, this.a});
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 51) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame50 extends ModuleBody {
        Object a$Mnint$Mnb;
        final ModuleMethod lambda$Fn52;
        frame49 staticLink;

        public frame50() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 49, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1568");
            this.lambda$Fn52 = moduleMethod;
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 49 ? lambda69(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda69(Object b$Mnpair, Object ans) {
            if (lists.member(lists.car.apply1(b$Mnpair), this.a$Mnint$Mnb, this.staticLink.staticLink.$Eq) != Boolean.FALSE) {
                return ans;
            }
            try {
                lists.setCdr$Ex((Pair) b$Mnpair, ans);
                return b$Mnpair;
            } catch (ClassCastException e) {
                throw new WrongType(e, "set-cdr!", 1, b$Mnpair);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 49) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }
    }

    public static Object lsetDiff$PlIntersection$V(Procedure $Eq, Object lis1, Object[] argsArray) {
        frame51 frame512 = new frame51();
        frame512.$Eq = $Eq;
        frame512.lists = LList.makeList(argsArray, 0);
        if (every$V(null$Mnlist$Qu, frame512.lists, new Object[0]) != Boolean.FALSE) {
            return misc.values(lis1, LList.Empty);
        } else if (lists.memq(lis1, frame512.lists) == Boolean.FALSE) {
            return partition(frame512.lambda$Fn53, lis1);
        } else {
            return misc.values(LList.Empty, lis1);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame51 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn53;
        LList lists;

        public frame51() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 54, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1579");
            this.lambda$Fn53 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 54) {
                return lambda70(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda70(Object elt) {
            int i = 0;
            frame52 frame52 = new frame52();
            frame52.staticLink = this;
            frame52.elt = elt;
            if (srfi1.any$V(frame52.lambda$Fn54, this.lists, new Object[0]) != Boolean.FALSE) {
                i = 1;
            }
            return (i + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 54) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame52 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn54;
        frame51 staticLink;

        public frame52() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 53, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1580");
            this.lambda$Fn54 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 53 ? lambda71(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda71(Object lis) {
            return lists.member(this.elt, lis, this.staticLink.$Eq);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 53) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object lsetDiff$PlIntersection$Ex$V(Procedure $Eq, Object lis1, Object[] argsArray) {
        frame53 frame532 = new frame53();
        frame532.$Eq = $Eq;
        frame532.lists = LList.makeList(argsArray, 0);
        if (every$V(null$Mnlist$Qu, frame532.lists, new Object[0]) != Boolean.FALSE) {
            return misc.values(lis1, LList.Empty);
        } else if (lists.memq(lis1, frame532.lists) == Boolean.FALSE) {
            return partition$Ex(frame532.lambda$Fn55, lis1);
        } else {
            return misc.values(LList.Empty, lis1);
        }
    }

    public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
        switch (moduleMethod.selector) {
            case 79:
                Object obj = objArr[0];
                int length = objArr.length - 1;
                Object[] objArr2 = new Object[length];
                while (true) {
                    length--;
                    if (length < 0) {
                        return makeList$V(obj, objArr2);
                    }
                    objArr2[length] = objArr[length + 1];
                }
            case 81:
                return cons$St(objArr);
            case 86:
                Object obj2 = objArr[0];
                int length2 = objArr.length - 1;
                Object[] objArr3 = new Object[length2];
                while (true) {
                    length2--;
                    if (length2 < 0) {
                        return circularList$V(obj2, objArr3);
                    }
                    objArr3[length2] = objArr[length2 + 1];
                }
            case 92:
                Object obj3 = objArr[0];
                int length3 = objArr.length - 1;
                Object[] objArr4 = new Object[length3];
                while (true) {
                    length3--;
                    if (length3 < 0) {
                        return list$Eq$V(obj3, objArr4);
                    }
                    objArr4[length3] = objArr[length3 + 1];
                }
            case 94:
                Object obj4 = objArr[0];
                int length4 = objArr.length - 1;
                Object[] objArr5 = new Object[length4];
                while (true) {
                    length4--;
                    if (length4 < 0) {
                        return zip$V(obj4, objArr5);
                    }
                    objArr5[length4] = objArr[length4 + 1];
                }
            case 117:
                return append$Ex$V(objArr);
            case 122:
                Procedure procedure = objArr[0];
                try {
                    Procedure procedure2 = procedure;
                    Object obj5 = objArr[1];
                    int length5 = objArr.length - 2;
                    Object[] objArr6 = new Object[length5];
                    while (true) {
                        length5--;
                        if (length5 < 0) {
                            return count$V(procedure2, obj5, objArr6);
                        }
                        objArr6[length5] = objArr[length5 + 2];
                    }
                } catch (ClassCastException e) {
                    throw new WrongType(e, "count", 1, (Object) procedure);
                }
            case 123:
                int length6 = objArr.length - 4;
                Procedure procedure3 = objArr[0];
                try {
                    Procedure procedure4 = procedure3;
                    Procedure procedure5 = objArr[1];
                    try {
                        Procedure procedure6 = procedure5;
                        Procedure procedure7 = objArr[2];
                        try {
                            Procedure procedure8 = procedure7;
                            Object obj6 = objArr[3];
                            if (length6 <= 0) {
                                return unfoldRight(procedure4, procedure6, procedure8, obj6);
                            }
                            int i = length6 - 1;
                            return unfoldRight(procedure4, procedure6, procedure8, obj6, objArr[4]);
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "unfold-right", 3, (Object) procedure7);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "unfold-right", 2, (Object) procedure5);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "unfold-right", 1, (Object) procedure3);
                }
            case 125:
                Procedure procedure9 = objArr[0];
                try {
                    Procedure procedure10 = procedure9;
                    Procedure procedure11 = objArr[1];
                    try {
                        Procedure procedure12 = procedure11;
                        Procedure procedure13 = objArr[2];
                        try {
                            Procedure procedure14 = procedure13;
                            Object obj7 = objArr[3];
                            int length7 = objArr.length - 4;
                            Object[] objArr7 = new Object[length7];
                            while (true) {
                                length7--;
                                if (length7 < 0) {
                                    return unfold$V(procedure10, procedure12, procedure14, obj7, objArr7);
                                }
                                objArr7[length7] = objArr[length7 + 4];
                            }
                        } catch (ClassCastException e5) {
                            throw new WrongType(e5, "unfold", 3, (Object) procedure13);
                        }
                    } catch (ClassCastException e6) {
                        throw new WrongType(e6, "unfold", 2, (Object) procedure11);
                    }
                } catch (ClassCastException e7) {
                    throw new WrongType(e7, "unfold", 1, (Object) procedure9);
                }
            case 126:
                Procedure procedure15 = objArr[0];
                try {
                    Procedure procedure16 = procedure15;
                    Object obj8 = objArr[1];
                    Object obj9 = objArr[2];
                    int length8 = objArr.length - 3;
                    Object[] objArr8 = new Object[length8];
                    while (true) {
                        length8--;
                        if (length8 < 0) {
                            return fold$V(procedure16, obj8, obj9, objArr8);
                        }
                        objArr8[length8] = objArr[length8 + 3];
                    }
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "fold", 1, (Object) procedure15);
                }
            case 127:
                Procedure procedure17 = objArr[0];
                try {
                    Procedure procedure18 = procedure17;
                    Object obj10 = objArr[1];
                    Object obj11 = objArr[2];
                    int length9 = objArr.length - 3;
                    Object[] objArr9 = new Object[length9];
                    while (true) {
                        length9--;
                        if (length9 < 0) {
                            return foldRight$V(procedure18, obj10, obj11, objArr9);
                        }
                        objArr9[length9] = objArr[length9 + 3];
                    }
                } catch (ClassCastException e9) {
                    throw new WrongType(e9, "fold-right", 1, (Object) procedure17);
                }
            case 128:
                Procedure procedure19 = objArr[0];
                try {
                    Procedure procedure20 = procedure19;
                    Object obj12 = objArr[1];
                    Object obj13 = objArr[2];
                    int length10 = objArr.length - 3;
                    Object[] objArr10 = new Object[length10];
                    while (true) {
                        length10--;
                        if (length10 < 0) {
                            return pairFoldRight$V(procedure20, obj12, obj13, objArr10);
                        }
                        objArr10[length10] = objArr[length10 + 3];
                    }
                } catch (ClassCastException e10) {
                    throw new WrongType(e10, "pair-fold-right", 1, (Object) procedure19);
                }
            case 129:
                Procedure procedure21 = objArr[0];
                try {
                    Procedure procedure22 = procedure21;
                    Object obj14 = objArr[1];
                    Object obj15 = objArr[2];
                    int length11 = objArr.length - 3;
                    Object[] objArr11 = new Object[length11];
                    while (true) {
                        length11--;
                        if (length11 < 0) {
                            return pairFold$V(procedure22, obj14, obj15, objArr11);
                        }
                        objArr11[length11] = objArr[length11 + 3];
                    }
                } catch (ClassCastException e11) {
                    throw new WrongType(e11, "pair-fold", 1, (Object) procedure21);
                }
            case 132:
                Object obj16 = objArr[0];
                Object obj17 = objArr[1];
                int length12 = objArr.length - 2;
                Object[] objArr12 = new Object[length12];
                while (true) {
                    length12--;
                    if (length12 < 0) {
                        return appendMap$V(obj16, obj17, objArr12);
                    }
                    objArr12[length12] = objArr[length12 + 2];
                }
            case 133:
                Object obj18 = objArr[0];
                Object obj19 = objArr[1];
                int length13 = objArr.length - 2;
                Object[] objArr13 = new Object[length13];
                while (true) {
                    length13--;
                    if (length13 < 0) {
                        return appendMap$Ex$V(obj18, obj19, objArr13);
                    }
                    objArr13[length13] = objArr[length13 + 2];
                }
            case 134:
                Procedure procedure23 = objArr[0];
                try {
                    Procedure procedure24 = procedure23;
                    Object obj20 = objArr[1];
                    int length14 = objArr.length - 2;
                    Object[] objArr14 = new Object[length14];
                    while (true) {
                        length14--;
                        if (length14 < 0) {
                            return pairForEach$V(procedure24, obj20, objArr14);
                        }
                        objArr14[length14] = objArr[length14 + 2];
                    }
                } catch (ClassCastException e12) {
                    throw new WrongType(e12, "pair-for-each", 1, (Object) procedure23);
                }
            case 135:
                Procedure procedure25 = objArr[0];
                try {
                    Procedure procedure26 = procedure25;
                    Object obj21 = objArr[1];
                    int length15 = objArr.length - 2;
                    Object[] objArr15 = new Object[length15];
                    while (true) {
                        length15--;
                        if (length15 < 0) {
                            return map$Ex$V(procedure26, obj21, objArr15);
                        }
                        objArr15[length15] = objArr[length15 + 2];
                    }
                } catch (ClassCastException e13) {
                    throw new WrongType(e13, "map!", 1, (Object) procedure25);
                }
            case 136:
                Procedure procedure27 = objArr[0];
                try {
                    Procedure procedure28 = procedure27;
                    Object obj22 = objArr[1];
                    int length16 = objArr.length - 2;
                    Object[] objArr16 = new Object[length16];
                    while (true) {
                        length16--;
                        if (length16 < 0) {
                            return filterMap$V(procedure28, obj22, objArr16);
                        }
                        objArr16[length16] = objArr[length16 + 2];
                    }
                } catch (ClassCastException e14) {
                    throw new WrongType(e14, "filter-map", 1, (Object) procedure27);
                }
            case 166:
                Procedure procedure29 = objArr[0];
                try {
                    Procedure procedure30 = procedure29;
                    Object obj23 = objArr[1];
                    int length17 = objArr.length - 2;
                    Object[] objArr17 = new Object[length17];
                    while (true) {
                        length17--;
                        if (length17 < 0) {
                            return any$V(procedure30, obj23, objArr17);
                        }
                        objArr17[length17] = objArr[length17 + 2];
                    }
                } catch (ClassCastException e15) {
                    throw new WrongType(e15, "any", 1, (Object) procedure29);
                }
            case 167:
                Procedure procedure31 = objArr[0];
                try {
                    Procedure procedure32 = procedure31;
                    Object obj24 = objArr[1];
                    int length18 = objArr.length - 2;
                    Object[] objArr18 = new Object[length18];
                    while (true) {
                        length18--;
                        if (length18 < 0) {
                            return every$V(procedure32, obj24, objArr18);
                        }
                        objArr18[length18] = objArr[length18 + 2];
                    }
                } catch (ClassCastException e16) {
                    throw new WrongType(e16, "every", 1, (Object) procedure31);
                }
            case 168:
                Procedure procedure33 = objArr[0];
                try {
                    Procedure procedure34 = procedure33;
                    Object obj25 = objArr[1];
                    int length19 = objArr.length - 2;
                    Object[] objArr19 = new Object[length19];
                    while (true) {
                        length19--;
                        if (length19 < 0) {
                            return listIndex$V(procedure34, obj25, objArr19);
                        }
                        objArr19[length19] = objArr[length19 + 2];
                    }
                } catch (ClassCastException e17) {
                    throw new WrongType(e17, "list-index", 1, (Object) procedure33);
                }
            case 169:
                Procedure procedure35 = objArr[0];
                try {
                    Procedure procedure36 = procedure35;
                    int length20 = objArr.length - 1;
                    Object[] objArr20 = new Object[length20];
                    while (true) {
                        length20--;
                        if (length20 < 0) {
                            return lset$Ls$Eq$V(procedure36, objArr20);
                        }
                        objArr20[length20] = objArr[length20 + 1];
                    }
                } catch (ClassCastException e18) {
                    throw new WrongType(e18, "lset<=", 1, (Object) procedure35);
                }
            case 170:
                Procedure procedure37 = objArr[0];
                try {
                    Procedure procedure38 = procedure37;
                    int length21 = objArr.length - 1;
                    Object[] objArr21 = new Object[length21];
                    while (true) {
                        length21--;
                        if (length21 < 0) {
                            return lset$Eq$V(procedure38, objArr21);
                        }
                        objArr21[length21] = objArr[length21 + 1];
                    }
                } catch (ClassCastException e19) {
                    throw new WrongType(e19, "lset=", 1, (Object) procedure37);
                }
            case 171:
                Procedure procedure39 = objArr[0];
                try {
                    Procedure procedure40 = procedure39;
                    Object obj26 = objArr[1];
                    int length22 = objArr.length - 2;
                    Object[] objArr22 = new Object[length22];
                    while (true) {
                        length22--;
                        if (length22 < 0) {
                            return lsetAdjoin$V(procedure40, obj26, objArr22);
                        }
                        objArr22[length22] = objArr[length22 + 2];
                    }
                } catch (ClassCastException e20) {
                    throw new WrongType(e20, "lset-adjoin", 1, (Object) procedure39);
                }
            case 172:
                Procedure procedure41 = objArr[0];
                try {
                    Procedure procedure42 = procedure41;
                    int length23 = objArr.length - 1;
                    Object[] objArr23 = new Object[length23];
                    while (true) {
                        length23--;
                        if (length23 < 0) {
                            return lsetUnion$V(procedure42, objArr23);
                        }
                        objArr23[length23] = objArr[length23 + 1];
                    }
                } catch (ClassCastException e21) {
                    throw new WrongType(e21, "lset-union", 1, (Object) procedure41);
                }
            case 173:
                Procedure procedure43 = objArr[0];
                try {
                    Procedure procedure44 = procedure43;
                    int length24 = objArr.length - 1;
                    Object[] objArr24 = new Object[length24];
                    while (true) {
                        length24--;
                        if (length24 < 0) {
                            return lsetUnion$Ex$V(procedure44, objArr24);
                        }
                        objArr24[length24] = objArr[length24 + 1];
                    }
                } catch (ClassCastException e22) {
                    throw new WrongType(e22, "lset-union!", 1, (Object) procedure43);
                }
            case 174:
                Procedure procedure45 = objArr[0];
                try {
                    Procedure procedure46 = procedure45;
                    Object obj27 = objArr[1];
                    int length25 = objArr.length - 2;
                    Object[] objArr25 = new Object[length25];
                    while (true) {
                        length25--;
                        if (length25 < 0) {
                            return lsetIntersection$V(procedure46, obj27, objArr25);
                        }
                        objArr25[length25] = objArr[length25 + 2];
                    }
                } catch (ClassCastException e23) {
                    throw new WrongType(e23, "lset-intersection", 1, (Object) procedure45);
                }
            case 175:
                Procedure procedure47 = objArr[0];
                try {
                    Procedure procedure48 = procedure47;
                    Object obj28 = objArr[1];
                    int length26 = objArr.length - 2;
                    Object[] objArr26 = new Object[length26];
                    while (true) {
                        length26--;
                        if (length26 < 0) {
                            return lsetIntersection$Ex$V(procedure48, obj28, objArr26);
                        }
                        objArr26[length26] = objArr[length26 + 2];
                    }
                } catch (ClassCastException e24) {
                    throw new WrongType(e24, "lset-intersection!", 1, (Object) procedure47);
                }
            case 176:
                Procedure procedure49 = objArr[0];
                try {
                    Procedure procedure50 = procedure49;
                    Object obj29 = objArr[1];
                    int length27 = objArr.length - 2;
                    Object[] objArr27 = new Object[length27];
                    while (true) {
                        length27--;
                        if (length27 < 0) {
                            return lsetDifference$V(procedure50, obj29, objArr27);
                        }
                        objArr27[length27] = objArr[length27 + 2];
                    }
                } catch (ClassCastException e25) {
                    throw new WrongType(e25, "lset-difference", 1, (Object) procedure49);
                }
            case 177:
                Procedure procedure51 = objArr[0];
                try {
                    Procedure procedure52 = procedure51;
                    Object obj30 = objArr[1];
                    int length28 = objArr.length - 2;
                    Object[] objArr28 = new Object[length28];
                    while (true) {
                        length28--;
                        if (length28 < 0) {
                            return lsetDifference$Ex$V(procedure52, obj30, objArr28);
                        }
                        objArr28[length28] = objArr[length28 + 2];
                    }
                } catch (ClassCastException e26) {
                    throw new WrongType(e26, "lset-difference!", 1, (Object) procedure51);
                }
            case 178:
                Procedure procedure53 = objArr[0];
                try {
                    Procedure procedure54 = procedure53;
                    int length29 = objArr.length - 1;
                    Object[] objArr29 = new Object[length29];
                    while (true) {
                        length29--;
                        if (length29 < 0) {
                            return lsetXor$V(procedure54, objArr29);
                        }
                        objArr29[length29] = objArr[length29 + 1];
                    }
                } catch (ClassCastException e27) {
                    throw new WrongType(e27, "lset-xor", 1, (Object) procedure53);
                }
            case 179:
                Procedure procedure55 = objArr[0];
                try {
                    Procedure procedure56 = procedure55;
                    int length30 = objArr.length - 1;
                    Object[] objArr30 = new Object[length30];
                    while (true) {
                        length30--;
                        if (length30 < 0) {
                            return lsetXor$Ex$V(procedure56, objArr30);
                        }
                        objArr30[length30] = objArr[length30 + 1];
                    }
                } catch (ClassCastException e28) {
                    throw new WrongType(e28, "lset-xor!", 1, (Object) procedure55);
                }
            case 180:
                Procedure procedure57 = objArr[0];
                try {
                    Procedure procedure58 = procedure57;
                    Object obj31 = objArr[1];
                    int length31 = objArr.length - 2;
                    Object[] objArr31 = new Object[length31];
                    while (true) {
                        length31--;
                        if (length31 < 0) {
                            return lsetDiff$PlIntersection$V(procedure58, obj31, objArr31);
                        }
                        objArr31[length31] = objArr[length31 + 2];
                    }
                } catch (ClassCastException e29) {
                    throw new WrongType(e29, "lset-diff+intersection", 1, (Object) procedure57);
                }
            case 181:
                Procedure procedure59 = objArr[0];
                try {
                    Procedure procedure60 = procedure59;
                    Object obj32 = objArr[1];
                    int length32 = objArr.length - 2;
                    Object[] objArr32 = new Object[length32];
                    while (true) {
                        length32--;
                        if (length32 < 0) {
                            return lsetDiff$PlIntersection$Ex$V(procedure60, obj32, objArr32);
                        }
                        objArr32[length32] = objArr[length32 + 2];
                    }
                } catch (ClassCastException e30) {
                    throw new WrongType(e30, "lset-diff+intersection!", 1, (Object) procedure59);
                }
            default:
                return super.applyN(moduleMethod, objArr);
        }
    }

    /* compiled from: srfi1.scm */
    public class frame53 extends ModuleBody {
        Procedure $Eq;
        final ModuleMethod lambda$Fn55;
        LList lists;

        public frame53() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 56, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1587");
            this.lambda$Fn55 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 56) {
                return lambda72(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda72(Object elt) {
            int i = 0;
            frame54 frame54 = new frame54();
            frame54.staticLink = this;
            frame54.elt = elt;
            if (srfi1.any$V(frame54.lambda$Fn56, this.lists, new Object[0]) != Boolean.FALSE) {
                i = 1;
            }
            return (i + 1) & true;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 56) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    /* compiled from: srfi1.scm */
    public class frame54 extends ModuleBody {
        Object elt;
        final ModuleMethod lambda$Fn56;
        frame53 staticLink;

        public frame54() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 55, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi1.scm:1588");
            this.lambda$Fn56 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 55 ? lambda73(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda73(Object lis) {
            return lists.member(this.elt, lis, this.staticLink.$Eq);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 55) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }
}
