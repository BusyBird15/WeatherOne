package gnu.kawa.slib;

import androidx.core.view.InputDeviceCompat;
import androidx.core.view.PointerIconCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.appinventor.components.common.ComponentConstants;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.runtime.util.FullScreenVideoUtil;
import com.google.appinventor.components.runtime.util.ScreenDensityUtil;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.AddOp;
import gnu.kawa.functions.ApplyToArgs;
import gnu.kawa.functions.BitwiseOp;
import gnu.kawa.functions.DivideOp;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.functions.MultiplyOp;
import gnu.kawa.functions.NumberCompare;
import gnu.kawa.lispexpr.LangObjType;
import gnu.kawa.lispexpr.LangPrimType;
import gnu.kawa.reflect.Invoke;
import gnu.lists.CharSeq;
import gnu.lists.Consumer;
import gnu.lists.FVector;
import gnu.lists.LList;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.Location;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.ThreadLocation;
import gnu.mapping.UnboundLocationException;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import gnu.text.Char;
import kawa.Telnet;
import kawa.lang.Macro;
import kawa.lang.SyntaxPattern;
import kawa.lang.SyntaxRule;
import kawa.lang.SyntaxRules;
import kawa.lib.characters;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.numbers;
import kawa.lib.rnrs.unicode;
import kawa.lib.strings;
import kawa.lib.vectors;
import kawa.standard.Scheme;
import kawa.standard.call_with_values;

/* compiled from: srfi13.scm */
public class srfi13 extends ModuleBody {
    public static final ModuleMethod $Pccheck$Mnbounds;
    public static final ModuleMethod $Pcfinish$Mnstring$Mnconcatenate$Mnreverse;
    public static final ModuleMethod $Pckmp$Mnsearch;
    public static final ModuleMethod $Pcmultispan$Mnrepcopy$Ex;
    public static final ModuleMethod $Pcstring$Mncompare;
    public static final ModuleMethod $Pcstring$Mncompare$Mnci;
    public static final ModuleMethod $Pcstring$Mncopy$Ex;
    public static final ModuleMethod $Pcstring$Mnhash;
    public static final ModuleMethod $Pcstring$Mnmap;
    public static final ModuleMethod $Pcstring$Mnmap$Ex;
    public static final ModuleMethod $Pcstring$Mnprefix$Mnci$Qu;
    public static final ModuleMethod $Pcstring$Mnprefix$Mnlength;
    public static final ModuleMethod $Pcstring$Mnprefix$Mnlength$Mnci;
    public static final ModuleMethod $Pcstring$Mnprefix$Qu;
    public static final ModuleMethod $Pcstring$Mnsuffix$Mnci$Qu;
    public static final ModuleMethod $Pcstring$Mnsuffix$Mnlength;
    public static final ModuleMethod $Pcstring$Mnsuffix$Mnlength$Mnci;
    public static final ModuleMethod $Pcstring$Mnsuffix$Qu;
    public static final ModuleMethod $Pcstring$Mntitlecase$Ex;
    public static final ModuleMethod $Pcsubstring$Slshared;
    public static final srfi13 $instance = new srfi13();
    static final IntNum Lit0 = IntNum.make(0);
    static final IntNum Lit1 = IntNum.make(1);
    static final IntNum Lit10 = IntNum.make(4194304);
    static final SimpleSymbol Lit100 = ((SimpleSymbol) new SimpleSymbol("string-hash-ci").readResolve());
    static final SimpleSymbol Lit101 = ((SimpleSymbol) new SimpleSymbol("string-upcase").readResolve());
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol("string-upcase!").readResolve());
    static final SimpleSymbol Lit103 = ((SimpleSymbol) new SimpleSymbol("string-downcase").readResolve());
    static final SimpleSymbol Lit104 = ((SimpleSymbol) new SimpleSymbol("string-downcase!").readResolve());
    static final SimpleSymbol Lit105 = ((SimpleSymbol) new SimpleSymbol("%string-titlecase!").readResolve());
    static final SimpleSymbol Lit106 = ((SimpleSymbol) new SimpleSymbol("string-titlecase!").readResolve());
    static final SimpleSymbol Lit107 = ((SimpleSymbol) new SimpleSymbol("string-titlecase").readResolve());
    static final SimpleSymbol Lit108 = ((SimpleSymbol) new SimpleSymbol("string-take").readResolve());
    static final SimpleSymbol Lit109 = ((SimpleSymbol) new SimpleSymbol("string-take-right").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("whitespace").readResolve());
    static final SimpleSymbol Lit110 = ((SimpleSymbol) new SimpleSymbol("string-drop").readResolve());
    static final SimpleSymbol Lit111 = ((SimpleSymbol) new SimpleSymbol("string-drop-right").readResolve());
    static final SimpleSymbol Lit112 = ((SimpleSymbol) new SimpleSymbol("string-trim").readResolve());
    static final SimpleSymbol Lit113 = ((SimpleSymbol) new SimpleSymbol("string-trim-right").readResolve());
    static final SimpleSymbol Lit114 = ((SimpleSymbol) new SimpleSymbol("string-trim-both").readResolve());
    static final SimpleSymbol Lit115 = ((SimpleSymbol) new SimpleSymbol("string-pad-right").readResolve());
    static final SimpleSymbol Lit116 = ((SimpleSymbol) new SimpleSymbol("string-pad").readResolve());
    static final SimpleSymbol Lit117 = ((SimpleSymbol) new SimpleSymbol("string-delete").readResolve());
    static final SimpleSymbol Lit118 = ((SimpleSymbol) new SimpleSymbol("string-filter").readResolve());
    static final SimpleSymbol Lit119 = ((SimpleSymbol) new SimpleSymbol("string-index").readResolve());
    static final Char Lit12 = Char.make(32);
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("string-index-right").readResolve());
    static final SimpleSymbol Lit121 = ((SimpleSymbol) new SimpleSymbol("string-skip").readResolve());
    static final SimpleSymbol Lit122 = ((SimpleSymbol) new SimpleSymbol("string-skip-right").readResolve());
    static final SimpleSymbol Lit123 = ((SimpleSymbol) new SimpleSymbol("string-count").readResolve());
    static final SimpleSymbol Lit124 = ((SimpleSymbol) new SimpleSymbol("string-fill!").readResolve());
    static final SimpleSymbol Lit125 = ((SimpleSymbol) new SimpleSymbol("string-copy!").readResolve());
    static final SimpleSymbol Lit126 = ((SimpleSymbol) new SimpleSymbol("%string-copy!").readResolve());
    static final SimpleSymbol Lit127 = ((SimpleSymbol) new SimpleSymbol("string-contains").readResolve());
    static final SimpleSymbol Lit128 = ((SimpleSymbol) new SimpleSymbol("string-contains-ci").readResolve());
    static final SimpleSymbol Lit129 = ((SimpleSymbol) new SimpleSymbol("%kmp-search").readResolve());
    static final IntNum Lit13 = IntNum.make(-1);
    static final SimpleSymbol Lit130 = ((SimpleSymbol) new SimpleSymbol("make-kmp-restart-vector").readResolve());
    static final SimpleSymbol Lit131 = ((SimpleSymbol) new SimpleSymbol("kmp-step").readResolve());
    static final SimpleSymbol Lit132 = ((SimpleSymbol) new SimpleSymbol("string-kmp-partial-search").readResolve());
    static final SimpleSymbol Lit133 = ((SimpleSymbol) new SimpleSymbol("string-null?").readResolve());
    static final SimpleSymbol Lit134 = ((SimpleSymbol) new SimpleSymbol("string-reverse").readResolve());
    static final SimpleSymbol Lit135 = ((SimpleSymbol) new SimpleSymbol("string-reverse!").readResolve());
    static final SimpleSymbol Lit136 = ((SimpleSymbol) new SimpleSymbol("reverse-list->string").readResolve());
    static final SimpleSymbol Lit137 = ((SimpleSymbol) new SimpleSymbol("string->list").readResolve());
    static final SimpleSymbol Lit138 = ((SimpleSymbol) new SimpleSymbol("string-append/shared").readResolve());
    static final SimpleSymbol Lit139 = ((SimpleSymbol) new SimpleSymbol("string-concatenate/shared").readResolve());
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("graphic").readResolve());
    static final SimpleSymbol Lit140 = ((SimpleSymbol) new SimpleSymbol("string-concatenate").readResolve());
    static final SimpleSymbol Lit141 = ((SimpleSymbol) new SimpleSymbol("string-concatenate-reverse").readResolve());
    static final SimpleSymbol Lit142 = ((SimpleSymbol) new SimpleSymbol("string-concatenate-reverse/shared").readResolve());
    static final SimpleSymbol Lit143 = ((SimpleSymbol) new SimpleSymbol("%finish-string-concatenate-reverse").readResolve());
    static final SimpleSymbol Lit144 = ((SimpleSymbol) new SimpleSymbol("string-replace").readResolve());
    static final SimpleSymbol Lit145 = ((SimpleSymbol) new SimpleSymbol("string-tokenize").readResolve());
    static final SimpleSymbol Lit146 = ((SimpleSymbol) new SimpleSymbol("xsubstring").readResolve());
    static final SimpleSymbol Lit147 = ((SimpleSymbol) new SimpleSymbol("string-xcopy!").readResolve());
    static final SimpleSymbol Lit148 = ((SimpleSymbol) new SimpleSymbol("%multispan-repcopy!").readResolve());
    static final SimpleSymbol Lit149 = ((SimpleSymbol) new SimpleSymbol("string-join").readResolve());
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("infix").readResolve());
    static final SimpleSymbol Lit150 = ((SimpleSymbol) new SimpleSymbol("receive").readResolve());
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("strict-infix").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("prefix").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("suffix").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("check-arg").readResolve());
    static final IntNum Lit2 = IntNum.make(4096);
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol(":optional").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("let-optionals*").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("base").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("make-final").readResolve());
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("char-set?").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("char-set-contains?").readResolve());
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("bound").readResolve());
    static final SimpleSymbol Lit27;
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("char-cased?").readResolve());
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("criterion").readResolve());
    static final IntNum Lit3 = IntNum.make(40);
    static final SimpleSymbol Lit30 = ((SimpleSymbol) new SimpleSymbol("char-set").readResolve());
    static final SimpleSymbol Lit31 = ((SimpleSymbol) new SimpleSymbol("c=").readResolve());
    static final SimpleSymbol Lit32 = ((SimpleSymbol) new SimpleSymbol("start").readResolve());
    static final SimpleSymbol Lit33 = ((SimpleSymbol) new SimpleSymbol("end").readResolve());
    static final SimpleSymbol Lit34 = ((SimpleSymbol) new SimpleSymbol("p-start").readResolve());
    static final SimpleSymbol Lit35 = ((SimpleSymbol) new SimpleSymbol("s-start").readResolve());
    static final SimpleSymbol Lit36 = ((SimpleSymbol) new SimpleSymbol("s-end").readResolve());
    static final SimpleSymbol Lit37 = ((SimpleSymbol) new SimpleSymbol("final").readResolve());
    static final SimpleSymbol Lit38 = ((SimpleSymbol) new SimpleSymbol("token-chars").readResolve());
    static final SimpleSymbol Lit39 = ((SimpleSymbol) new SimpleSymbol("delim").readResolve());
    static final IntNum Lit4 = IntNum.make(4096);
    static final SimpleSymbol Lit40 = ((SimpleSymbol) new SimpleSymbol("grammar").readResolve());
    static final SimpleSymbol Lit41;
    static final SyntaxRules Lit42 = new SyntaxRules(new Object[]{Lit41}, new SyntaxRule[]{new SyntaxRule(new SyntaxPattern("\f\u0018,\f\u0007\f\u000f\b\f\u0017\f\u001f\f'\r/(\b\b", new Object[0], 6), "\u0001\u0001\u0001\u0001\u0001\u0003", "\u0011\u0018\u0004!\t\u0003\b\u000bI\u0011\u0018\f\t\u0013\t\u001b\b#\b-+", new Object[]{Lit150, Lit47}, 1), new SyntaxRule(new SyntaxPattern("\f\u0018<\f\u0007\f\u000f\f\u0017\b\f\u001f\f'\f/\r70\b\b", new Object[0], 7), "\u0001\u0001\u0001\u0001\u0001\u0001\u0003", "\u0011\u0018\u00041\t\u0013\t\u0003\b\u000bI\u0011\u0018\f\t\u001b\t#\b+\b53", new Object[]{Lit150, Lit45}, 1)}, 7);
    static final SimpleSymbol Lit43 = ((SimpleSymbol) new SimpleSymbol("let-string-start+end2").readResolve());
    static final SyntaxRules Lit44;
    static final SimpleSymbol Lit45 = ((SimpleSymbol) new SimpleSymbol("string-parse-start+end").readResolve());
    static final SimpleSymbol Lit46 = ((SimpleSymbol) new SimpleSymbol("%check-bounds").readResolve());
    static final SimpleSymbol Lit47 = ((SimpleSymbol) new SimpleSymbol("string-parse-final-start+end").readResolve());
    static final SimpleSymbol Lit48 = ((SimpleSymbol) new SimpleSymbol("substring-spec-ok?").readResolve());
    static final SimpleSymbol Lit49 = ((SimpleSymbol) new SimpleSymbol("check-substring-spec").readResolve());
    static final IntNum Lit5 = IntNum.make(65536);
    static final SimpleSymbol Lit50 = ((SimpleSymbol) new SimpleSymbol("substring/shared").readResolve());
    static final SimpleSymbol Lit51 = ((SimpleSymbol) new SimpleSymbol("%substring/shared").readResolve());
    static final SimpleSymbol Lit52 = ((SimpleSymbol) new SimpleSymbol("string-copy").readResolve());
    static final SimpleSymbol Lit53 = ((SimpleSymbol) new SimpleSymbol("string-map").readResolve());
    static final SimpleSymbol Lit54 = ((SimpleSymbol) new SimpleSymbol("%string-map").readResolve());
    static final SimpleSymbol Lit55 = ((SimpleSymbol) new SimpleSymbol("string-map!").readResolve());
    static final SimpleSymbol Lit56 = ((SimpleSymbol) new SimpleSymbol("%string-map!").readResolve());
    static final SimpleSymbol Lit57 = ((SimpleSymbol) new SimpleSymbol("string-fold").readResolve());
    static final SimpleSymbol Lit58 = ((SimpleSymbol) new SimpleSymbol("string-fold-right").readResolve());
    static final SimpleSymbol Lit59 = ((SimpleSymbol) new SimpleSymbol("string-unfold").readResolve());
    static final IntNum Lit6 = IntNum.make(37);
    static final SimpleSymbol Lit60 = ((SimpleSymbol) new SimpleSymbol("string-unfold-right").readResolve());
    static final SimpleSymbol Lit61 = ((SimpleSymbol) new SimpleSymbol("string-for-each").readResolve());
    static final SimpleSymbol Lit62 = ((SimpleSymbol) new SimpleSymbol("string-for-each-index").readResolve());
    static final SimpleSymbol Lit63 = ((SimpleSymbol) new SimpleSymbol("string-every").readResolve());
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("string-any").readResolve());
    static final SimpleSymbol Lit65 = ((SimpleSymbol) new SimpleSymbol("string-tabulate").readResolve());
    static final SimpleSymbol Lit66 = ((SimpleSymbol) new SimpleSymbol("%string-prefix-length").readResolve());
    static final SimpleSymbol Lit67 = ((SimpleSymbol) new SimpleSymbol("%string-suffix-length").readResolve());
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("%string-prefix-length-ci").readResolve());
    static final SimpleSymbol Lit69 = ((SimpleSymbol) new SimpleSymbol("%string-suffix-length-ci").readResolve());
    static final IntNum Lit7 = IntNum.make(4194304);
    static final SimpleSymbol Lit70 = ((SimpleSymbol) new SimpleSymbol("string-prefix-length").readResolve());
    static final SimpleSymbol Lit71 = ((SimpleSymbol) new SimpleSymbol("string-suffix-length").readResolve());
    static final SimpleSymbol Lit72 = ((SimpleSymbol) new SimpleSymbol("string-prefix-length-ci").readResolve());
    static final SimpleSymbol Lit73 = ((SimpleSymbol) new SimpleSymbol("string-suffix-length-ci").readResolve());
    static final SimpleSymbol Lit74 = ((SimpleSymbol) new SimpleSymbol("string-prefix?").readResolve());
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("string-suffix?").readResolve());
    static final SimpleSymbol Lit76 = ((SimpleSymbol) new SimpleSymbol("string-prefix-ci?").readResolve());
    static final SimpleSymbol Lit77 = ((SimpleSymbol) new SimpleSymbol("string-suffix-ci?").readResolve());
    static final SimpleSymbol Lit78 = ((SimpleSymbol) new SimpleSymbol("%string-prefix?").readResolve());
    static final SimpleSymbol Lit79 = ((SimpleSymbol) new SimpleSymbol("%string-suffix?").readResolve());
    static final IntNum Lit8 = IntNum.make(4194304);
    static final SimpleSymbol Lit80 = ((SimpleSymbol) new SimpleSymbol("%string-prefix-ci?").readResolve());
    static final SimpleSymbol Lit81 = ((SimpleSymbol) new SimpleSymbol("%string-suffix-ci?").readResolve());
    static final SimpleSymbol Lit82 = ((SimpleSymbol) new SimpleSymbol("%string-compare").readResolve());
    static final SimpleSymbol Lit83 = ((SimpleSymbol) new SimpleSymbol("%string-compare-ci").readResolve());
    static final SimpleSymbol Lit84 = ((SimpleSymbol) new SimpleSymbol("string-compare").readResolve());
    static final SimpleSymbol Lit85 = ((SimpleSymbol) new SimpleSymbol("string-compare-ci").readResolve());
    static final SimpleSymbol Lit86 = ((SimpleSymbol) new SimpleSymbol("string=").readResolve());
    static final SimpleSymbol Lit87 = ((SimpleSymbol) new SimpleSymbol("string<>").readResolve());
    static final SimpleSymbol Lit88 = ((SimpleSymbol) new SimpleSymbol("string<").readResolve());
    static final SimpleSymbol Lit89 = ((SimpleSymbol) new SimpleSymbol("string>").readResolve());
    static final IntNum Lit9 = IntNum.make(4194304);
    static final SimpleSymbol Lit90 = ((SimpleSymbol) new SimpleSymbol("string<=").readResolve());
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol("string>=").readResolve());
    static final SimpleSymbol Lit92 = ((SimpleSymbol) new SimpleSymbol("string-ci=").readResolve());
    static final SimpleSymbol Lit93 = ((SimpleSymbol) new SimpleSymbol("string-ci<>").readResolve());
    static final SimpleSymbol Lit94 = ((SimpleSymbol) new SimpleSymbol("string-ci<").readResolve());
    static final SimpleSymbol Lit95 = ((SimpleSymbol) new SimpleSymbol("string-ci>").readResolve());
    static final SimpleSymbol Lit96 = ((SimpleSymbol) new SimpleSymbol("string-ci<=").readResolve());
    static final SimpleSymbol Lit97 = ((SimpleSymbol) new SimpleSymbol("string-ci>=").readResolve());
    static final SimpleSymbol Lit98 = ((SimpleSymbol) new SimpleSymbol("%string-hash").readResolve());
    static final SimpleSymbol Lit99 = ((SimpleSymbol) new SimpleSymbol("string-hash").readResolve());
    public static final ModuleMethod check$Mnsubstring$Mnspec;
    public static final ModuleMethod kmp$Mnstep;
    static final ModuleMethod lambda$Fn100;
    static final ModuleMethod lambda$Fn105;
    static final ModuleMethod lambda$Fn106;
    static final ModuleMethod lambda$Fn111;
    static final ModuleMethod lambda$Fn116;
    static final ModuleMethod lambda$Fn117;
    static final ModuleMethod lambda$Fn122;
    static final ModuleMethod lambda$Fn123;
    static final ModuleMethod lambda$Fn128;
    static final ModuleMethod lambda$Fn133;
    static final ModuleMethod lambda$Fn138;
    static final ModuleMethod lambda$Fn163;
    static final ModuleMethod lambda$Fn166;
    static final ModuleMethod lambda$Fn17;
    static final ModuleMethod lambda$Fn18;
    static final ModuleMethod lambda$Fn210;
    static final ModuleMethod lambda$Fn216;
    static final ModuleMethod lambda$Fn220;
    static final ModuleMethod lambda$Fn27;
    static final ModuleMethod lambda$Fn5;
    static final ModuleMethod lambda$Fn72;
    static final ModuleMethod lambda$Fn73;
    static final ModuleMethod lambda$Fn78;
    static final ModuleMethod lambda$Fn83;
    static final ModuleMethod lambda$Fn84;
    static final ModuleMethod lambda$Fn89;
    static final ModuleMethod lambda$Fn90;
    static final ModuleMethod lambda$Fn95;
    public static final Macro let$Mnstring$Mnstart$Plend = Macro.make(Lit41, Lit42, $instance);
    public static final Macro let$Mnstring$Mnstart$Plend2 = Macro.make(Lit43, Lit44, $instance);
    static final Location loc$$Cloptional = ThreadLocation.getInstance(Lit20, (Object) null);
    static final Location loc$base = ThreadLocation.getInstance(Lit22, (Object) null);
    static final Location loc$bound = ThreadLocation.getInstance(Lit26, (Object) null);
    static final Location loc$c$Eq = ThreadLocation.getInstance(Lit31, (Object) null);
    static final Location loc$char$Mncased$Qu = ThreadLocation.getInstance(Lit28, (Object) null);
    static final Location loc$char$Mnset = ThreadLocation.getInstance(Lit30, (Object) null);
    static final Location loc$char$Mnset$Mncontains$Qu = ThreadLocation.getInstance(Lit25, (Object) null);
    static final Location loc$char$Mnset$Qu = ThreadLocation.getInstance(Lit24, (Object) null);
    static final Location loc$check$Mnarg = ThreadLocation.getInstance(Lit19, (Object) null);
    static final Location loc$criterion = ThreadLocation.getInstance(Lit29, (Object) null);
    static final Location loc$delim = ThreadLocation.getInstance(Lit39, (Object) null);
    static final Location loc$end = ThreadLocation.getInstance(Lit33, (Object) null);
    static final Location loc$final = ThreadLocation.getInstance(Lit37, (Object) null);
    static final Location loc$grammar = ThreadLocation.getInstance(Lit40, (Object) null);
    static final Location loc$let$Mnoptionals$St = ThreadLocation.getInstance(Lit21, (Object) null);
    static final Location loc$make$Mnfinal = ThreadLocation.getInstance(Lit23, (Object) null);
    static final Location loc$p$Mnstart = ThreadLocation.getInstance(Lit34, (Object) null);
    static final Location loc$rest = ThreadLocation.getInstance(Lit27, (Object) null);
    static final Location loc$s$Mnend = ThreadLocation.getInstance(Lit36, (Object) null);
    static final Location loc$s$Mnstart = ThreadLocation.getInstance(Lit35, (Object) null);
    static final Location loc$start = ThreadLocation.getInstance(Lit32, (Object) null);
    static final Location loc$token$Mnchars = ThreadLocation.getInstance(Lit38, (Object) null);
    public static final ModuleMethod make$Mnkmp$Mnrestart$Mnvector;
    public static final ModuleMethod reverse$Mnlist$Mn$Grstring;
    public static final ModuleMethod string$Eq;
    public static final ModuleMethod string$Gr;
    public static final ModuleMethod string$Gr$Eq;
    public static final ModuleMethod string$Ls;
    public static final ModuleMethod string$Ls$Eq;
    public static final ModuleMethod string$Ls$Gr;
    public static final ModuleMethod string$Mn$Grlist;
    public static final ModuleMethod string$Mnany;
    public static final ModuleMethod string$Mnappend$Slshared;
    public static final ModuleMethod string$Mnci$Eq;
    public static final ModuleMethod string$Mnci$Gr;
    public static final ModuleMethod string$Mnci$Gr$Eq;
    public static final ModuleMethod string$Mnci$Ls;
    public static final ModuleMethod string$Mnci$Ls$Eq;
    public static final ModuleMethod string$Mnci$Ls$Gr;
    public static final ModuleMethod string$Mncompare;
    public static final ModuleMethod string$Mncompare$Mnci;
    public static final ModuleMethod string$Mnconcatenate;
    public static final ModuleMethod string$Mnconcatenate$Mnreverse;
    public static final ModuleMethod string$Mnconcatenate$Mnreverse$Slshared;
    public static final ModuleMethod string$Mnconcatenate$Slshared;
    public static final ModuleMethod string$Mncontains;
    public static final ModuleMethod string$Mncontains$Mnci;
    public static final ModuleMethod string$Mncopy;
    public static final ModuleMethod string$Mncopy$Ex;
    public static final ModuleMethod string$Mncount;
    public static final ModuleMethod string$Mndelete;
    public static final ModuleMethod string$Mndowncase;
    public static final ModuleMethod string$Mndowncase$Ex;
    public static final ModuleMethod string$Mndrop;
    public static final ModuleMethod string$Mndrop$Mnright;
    public static final ModuleMethod string$Mnevery;
    public static final ModuleMethod string$Mnfill$Ex;
    public static final ModuleMethod string$Mnfilter;
    public static final ModuleMethod string$Mnfold;
    public static final ModuleMethod string$Mnfold$Mnright;
    public static final ModuleMethod string$Mnfor$Mneach;
    public static final ModuleMethod string$Mnfor$Mneach$Mnindex;
    public static final ModuleMethod string$Mnhash;
    public static final ModuleMethod string$Mnhash$Mnci;
    public static final ModuleMethod string$Mnindex;
    public static final ModuleMethod string$Mnindex$Mnright;
    public static final ModuleMethod string$Mnjoin;
    public static final ModuleMethod string$Mnkmp$Mnpartial$Mnsearch;
    public static final ModuleMethod string$Mnmap;
    public static final ModuleMethod string$Mnmap$Ex;
    public static final ModuleMethod string$Mnnull$Qu;
    public static final ModuleMethod string$Mnpad;
    public static final ModuleMethod string$Mnpad$Mnright;
    public static final ModuleMethod string$Mnparse$Mnfinal$Mnstart$Plend;
    public static final ModuleMethod string$Mnparse$Mnstart$Plend;
    public static final ModuleMethod string$Mnprefix$Mnci$Qu;
    public static final ModuleMethod string$Mnprefix$Mnlength;
    public static final ModuleMethod string$Mnprefix$Mnlength$Mnci;
    public static final ModuleMethod string$Mnprefix$Qu;
    public static final ModuleMethod string$Mnreplace;
    public static final ModuleMethod string$Mnreverse;
    public static final ModuleMethod string$Mnreverse$Ex;
    public static final ModuleMethod string$Mnskip;
    public static final ModuleMethod string$Mnskip$Mnright;
    public static final ModuleMethod string$Mnsuffix$Mnci$Qu;
    public static final ModuleMethod string$Mnsuffix$Mnlength;
    public static final ModuleMethod string$Mnsuffix$Mnlength$Mnci;
    public static final ModuleMethod string$Mnsuffix$Qu;
    public static final ModuleMethod string$Mntabulate;
    public static final ModuleMethod string$Mntake;
    public static final ModuleMethod string$Mntake$Mnright;
    public static final ModuleMethod string$Mntitlecase;
    public static final ModuleMethod string$Mntitlecase$Ex;
    public static final ModuleMethod string$Mntokenize;
    public static final ModuleMethod string$Mntrim;
    public static final ModuleMethod string$Mntrim$Mnboth;
    public static final ModuleMethod string$Mntrim$Mnright;
    public static final ModuleMethod string$Mnunfold;
    public static final ModuleMethod string$Mnunfold$Mnright;
    public static final ModuleMethod string$Mnupcase;
    public static final ModuleMethod string$Mnupcase$Ex;
    public static final ModuleMethod string$Mnxcopy$Ex;
    public static final ModuleMethod substring$Mnspec$Mnok$Qu;
    public static final ModuleMethod substring$Slshared;
    public static final ModuleMethod xsubstring;

    /* compiled from: srfi13.scm */
    public class frame55 extends ModuleBody {
        Object char$Mn$Grint;
    }

    static {
        Object[] objArr = {(SimpleSymbol) new SimpleSymbol("l-s-s+e2").readResolve()};
        SyntaxPattern syntaxPattern = new SyntaxPattern("\f\u0018L\f\u0007\f\u000f\f\u0017\f\u001f\b\f'\f/\f7\f?\rG@\b\b", new Object[0], 9);
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol("let-string-start+end").readResolve();
        Lit41 = simpleSymbol;
        SimpleSymbol simpleSymbol2 = (SimpleSymbol) new SimpleSymbol("rest").readResolve();
        Lit27 = simpleSymbol2;
        Lit44 = new SyntaxRules(objArr, new SyntaxRule[]{new SyntaxRule(syntaxPattern, "\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003", "\u0011\u0018\u00041\b\u0011\u0018\f\b#\b\u0011\u0018\u00141\t\u0003\t\u000b\u0018\u001c\u0011\u0018\f\t+\t;\b\u0011\u0018\u0014!\t\u0013\b\u001b\u0011\u0018\f\t3\u0011\u0018$\bEC", new Object[]{(SimpleSymbol) new SimpleSymbol("let").readResolve(), (SimpleSymbol) new SimpleSymbol("procv").readResolve(), simpleSymbol, PairWithPosition.make(simpleSymbol2, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 553003), Lit27}, 1)}, 9);
        srfi13 srfi13 = $instance;
        string$Mnparse$Mnstart$Plend = new ModuleMethod(srfi13, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SOURCE, Lit45, 12291);
        $Pccheck$Mnbounds = new ModuleMethod(srfi13, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_FULLSCREEN, Lit46, 16388);
        string$Mnparse$Mnfinal$Mnstart$Plend = new ModuleMethod(srfi13, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_DURATION, Lit47, 12291);
        substring$Mnspec$Mnok$Qu = new ModuleMethod(srfi13, 197, Lit48, 12291);
        check$Mnsubstring$Mnspec = new ModuleMethod(srfi13, 198, Lit49, 16388);
        ModuleMethod moduleMethod = new ModuleMethod(srfi13, 199, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:223");
        lambda$Fn5 = moduleMethod;
        substring$Slshared = new ModuleMethod(srfi13, 200, Lit50, -4094);
        $Pcsubstring$Slshared = new ModuleMethod(srfi13, ErrorMessages.ERROR_CAMERA_NO_IMAGE_RETURNED, Lit51, 12291);
        string$Mncopy = new ModuleMethod(srfi13, ErrorMessages.ERROR_NO_CAMERA_PERMISSION, Lit52, -4095);
        string$Mnmap = new ModuleMethod(srfi13, 203, Lit53, -4094);
        $Pcstring$Mnmap = new ModuleMethod(srfi13, 204, Lit54, 16388);
        string$Mnmap$Ex = new ModuleMethod(srfi13, 205, Lit55, -4094);
        $Pcstring$Mnmap$Ex = new ModuleMethod(srfi13, 206, Lit56, 16388);
        string$Mnfold = new ModuleMethod(srfi13, 207, Lit57, -4093);
        string$Mnfold$Mnright = new ModuleMethod(srfi13, 208, Lit58, -4093);
        ModuleMethod moduleMethod2 = new ModuleMethod(srfi13, 209, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:377");
        lambda$Fn17 = moduleMethod2;
        string$Mnunfold = new ModuleMethod(srfi13, 210, Lit59, -4092);
        ModuleMethod moduleMethod3 = new ModuleMethod(srfi13, 211, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod3.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:422");
        lambda$Fn18 = moduleMethod3;
        string$Mnunfold$Mnright = new ModuleMethod(srfi13, 212, Lit60, -4092);
        string$Mnfor$Mneach = new ModuleMethod(srfi13, 213, Lit61, -4094);
        string$Mnfor$Mneach$Mnindex = new ModuleMethod(srfi13, 214, Lit62, -4094);
        string$Mnevery = new ModuleMethod(srfi13, 215, Lit63, -4094);
        string$Mnany = new ModuleMethod(srfi13, 216, Lit64, -4094);
        ModuleMethod moduleMethod4 = new ModuleMethod(srfi13, 217, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod4.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:535");
        lambda$Fn27 = moduleMethod4;
        string$Mntabulate = new ModuleMethod(srfi13, 218, Lit65, 8194);
        $Pcstring$Mnprefix$Mnlength = new ModuleMethod(srfi13, 219, Lit66, 24582);
        $Pcstring$Mnsuffix$Mnlength = new ModuleMethod(srfi13, 220, Lit67, 24582);
        $Pcstring$Mnprefix$Mnlength$Mnci = new ModuleMethod(srfi13, YaVersion.YOUNG_ANDROID_VERSION, Lit68, 24582);
        $Pcstring$Mnsuffix$Mnlength$Mnci = new ModuleMethod(srfi13, 222, Lit69, 24582);
        string$Mnprefix$Mnlength = new ModuleMethod(srfi13, 223, Lit70, -4094);
        string$Mnsuffix$Mnlength = new ModuleMethod(srfi13, 224, Lit71, -4094);
        string$Mnprefix$Mnlength$Mnci = new ModuleMethod(srfi13, 225, Lit72, -4094);
        string$Mnsuffix$Mnlength$Mnci = new ModuleMethod(srfi13, 226, Lit73, -4094);
        string$Mnprefix$Qu = new ModuleMethod(srfi13, 227, Lit74, -4094);
        string$Mnsuffix$Qu = new ModuleMethod(srfi13, 228, Lit75, -4094);
        string$Mnprefix$Mnci$Qu = new ModuleMethod(srfi13, 229, Lit76, -4094);
        string$Mnsuffix$Mnci$Qu = new ModuleMethod(srfi13, 230, Lit77, -4094);
        $Pcstring$Mnprefix$Qu = new ModuleMethod(srfi13, 231, Lit78, 24582);
        $Pcstring$Mnsuffix$Qu = new ModuleMethod(srfi13, 232, Lit79, 24582);
        $Pcstring$Mnprefix$Mnci$Qu = new ModuleMethod(srfi13, 233, Lit80, 24582);
        $Pcstring$Mnsuffix$Mnci$Qu = new ModuleMethod(srfi13, 234, Lit81, 24582);
        $Pcstring$Mncompare = new ModuleMethod(srfi13, 235, Lit82, 36873);
        $Pcstring$Mncompare$Mnci = new ModuleMethod(srfi13, 236, Lit83, 36873);
        string$Mncompare = new ModuleMethod(srfi13, 237, Lit84, -4091);
        string$Mncompare$Mnci = new ModuleMethod(srfi13, 238, Lit85, -4091);
        ModuleMethod moduleMethod5 = new ModuleMethod(srfi13, 239, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod5.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:756");
        lambda$Fn72 = moduleMethod5;
        ModuleMethod moduleMethod6 = new ModuleMethod(srfi13, 240, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod6.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:758");
        lambda$Fn73 = moduleMethod6;
        string$Eq = new ModuleMethod(srfi13, LispEscapeFormat.ESCAPE_NORMAL, Lit86, -4094);
        ModuleMethod moduleMethod7 = new ModuleMethod(srfi13, LispEscapeFormat.ESCAPE_ALL, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod7.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:767");
        lambda$Fn78 = moduleMethod7;
        string$Ls$Gr = new ModuleMethod(srfi13, 243, Lit87, -4094);
        ModuleMethod moduleMethod8 = new ModuleMethod(srfi13, 244, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod8.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:778");
        lambda$Fn83 = moduleMethod8;
        ModuleMethod moduleMethod9 = new ModuleMethod(srfi13, 245, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod9.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:779");
        lambda$Fn84 = moduleMethod9;
        string$Ls = new ModuleMethod(srfi13, 246, Lit88, -4094);
        ModuleMethod moduleMethod10 = new ModuleMethod(srfi13, 247, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod10.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:788");
        lambda$Fn89 = moduleMethod10;
        ModuleMethod moduleMethod11 = new ModuleMethod(srfi13, 248, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod11.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:789");
        lambda$Fn90 = moduleMethod11;
        string$Gr = new ModuleMethod(srfi13, 249, Lit89, -4094);
        ModuleMethod moduleMethod12 = new ModuleMethod(srfi13, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod12.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:801");
        lambda$Fn95 = moduleMethod12;
        string$Ls$Eq = new ModuleMethod(srfi13, Telnet.WILL, Lit90, -4094);
        ModuleMethod moduleMethod13 = new ModuleMethod(srfi13, Telnet.WONT, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod13.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:810");
        lambda$Fn100 = moduleMethod13;
        string$Gr$Eq = new ModuleMethod(srfi13, Telnet.DO, Lit91, -4094);
        ModuleMethod moduleMethod14 = new ModuleMethod(srfi13, Telnet.DONT, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod14.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:820");
        lambda$Fn105 = moduleMethod14;
        ModuleMethod moduleMethod15 = new ModuleMethod(srfi13, 255, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod15.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:822");
        lambda$Fn106 = moduleMethod15;
        string$Mnci$Eq = new ModuleMethod(srfi13, 256, Lit92, -4094);
        ModuleMethod moduleMethod16 = new ModuleMethod(srfi13, InputDeviceCompat.SOURCE_KEYBOARD, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod16.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:831");
        lambda$Fn111 = moduleMethod16;
        string$Mnci$Ls$Gr = new ModuleMethod(srfi13, 258, Lit93, -4094);
        ModuleMethod moduleMethod17 = new ModuleMethod(srfi13, 259, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod17.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:842");
        lambda$Fn116 = moduleMethod17;
        ModuleMethod moduleMethod18 = new ModuleMethod(srfi13, 260, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod18.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:843");
        lambda$Fn117 = moduleMethod18;
        string$Mnci$Ls = new ModuleMethod(srfi13, 261, Lit94, -4094);
        ModuleMethod moduleMethod19 = new ModuleMethod(srfi13, 262, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod19.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:852");
        lambda$Fn122 = moduleMethod19;
        ModuleMethod moduleMethod20 = new ModuleMethod(srfi13, 263, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod20.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:853");
        lambda$Fn123 = moduleMethod20;
        string$Mnci$Gr = new ModuleMethod(srfi13, 264, Lit95, -4094);
        ModuleMethod moduleMethod21 = new ModuleMethod(srfi13, 265, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod21.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:865");
        lambda$Fn128 = moduleMethod21;
        string$Mnci$Ls$Eq = new ModuleMethod(srfi13, 266, Lit96, -4094);
        ModuleMethod moduleMethod22 = new ModuleMethod(srfi13, 267, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod22.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:874");
        lambda$Fn133 = moduleMethod22;
        string$Mnci$Gr$Eq = new ModuleMethod(srfi13, 268, Lit97, -4094);
        $Pcstring$Mnhash = new ModuleMethod(srfi13, 269, Lit98, 20485);
        string$Mnhash = new ModuleMethod(srfi13, 270, Lit99, -4095);
        ModuleMethod moduleMethod23 = new ModuleMethod(srfi13, 271, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod23.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:922");
        lambda$Fn138 = moduleMethod23;
        string$Mnhash$Mnci = new ModuleMethod(srfi13, 272, Lit100, -4095);
        string$Mnupcase = new ModuleMethod(srfi13, 273, Lit101, -4095);
        string$Mnupcase$Ex = new ModuleMethod(srfi13, 274, Lit102, -4095);
        string$Mndowncase = new ModuleMethod(srfi13, 275, Lit103, -4095);
        string$Mndowncase$Ex = new ModuleMethod(srfi13, 276, Lit104, -4095);
        $Pcstring$Mntitlecase$Ex = new ModuleMethod(srfi13, 277, Lit105, 12291);
        string$Mntitlecase$Ex = new ModuleMethod(srfi13, 278, Lit106, -4095);
        string$Mntitlecase = new ModuleMethod(srfi13, 279, Lit107, -4095);
        string$Mntake = new ModuleMethod(srfi13, 280, Lit108, 8194);
        string$Mntake$Mnright = new ModuleMethod(srfi13, 281, Lit109, 8194);
        string$Mndrop = new ModuleMethod(srfi13, 282, Lit110, 8194);
        string$Mndrop$Mnright = new ModuleMethod(srfi13, 283, Lit111, 8194);
        string$Mntrim = new ModuleMethod(srfi13, 284, Lit112, -4095);
        string$Mntrim$Mnright = new ModuleMethod(srfi13, 285, Lit113, -4095);
        string$Mntrim$Mnboth = new ModuleMethod(srfi13, 286, Lit114, -4095);
        ModuleMethod moduleMethod24 = new ModuleMethod(srfi13, 287, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod24.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1047");
        lambda$Fn163 = moduleMethod24;
        string$Mnpad$Mnright = new ModuleMethod(srfi13, 288, Lit115, -4094);
        ModuleMethod moduleMethod25 = new ModuleMethod(srfi13, 289, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod25.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1059");
        lambda$Fn166 = moduleMethod25;
        string$Mnpad = new ModuleMethod(srfi13, 290, Lit116, -4094);
        string$Mndelete = new ModuleMethod(srfi13, 291, Lit117, -4094);
        string$Mnfilter = new ModuleMethod(srfi13, 292, Lit118, -4094);
        string$Mnindex = new ModuleMethod(srfi13, 293, Lit119, -4094);
        string$Mnindex$Mnright = new ModuleMethod(srfi13, 294, Lit120, -4094);
        string$Mnskip = new ModuleMethod(srfi13, 295, Lit121, -4094);
        string$Mnskip$Mnright = new ModuleMethod(srfi13, 296, Lit122, -4094);
        string$Mncount = new ModuleMethod(srfi13, 297, Lit123, -4094);
        string$Mnfill$Ex = new ModuleMethod(srfi13, 298, Lit124, -4094);
        string$Mncopy$Ex = new ModuleMethod(srfi13, 299, Lit125, 20483);
        $Pcstring$Mncopy$Ex = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_BLANK_CONSUMER_KEY_OR_SECRET, Lit126, 20485);
        string$Mncontains = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_EXCEPTION, Lit127, -4094);
        string$Mncontains$Mnci = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_UNABLE_TO_GET_ACCESS_TOKEN, Lit128, -4094);
        $Pckmp$Mnsearch = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_AUTHORIZATION_FAILED, Lit129, 28679);
        make$Mnkmp$Mnrestart$Mnvector = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_SET_STATUS_FAILED, Lit130, -4095);
        kmp$Mnstep = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_REQUEST_MENTIONS_FAILED, Lit131, 24582);
        string$Mnkmp$Mnpartial$Mnsearch = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_REQUEST_FOLLOWERS_FAILED, Lit132, -4092);
        string$Mnnull$Qu = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_REQUEST_DIRECT_MESSAGES_FAILED, Lit133, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        string$Mnreverse = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_DIRECT_MESSAGE_FAILED, Lit134, -4095);
        string$Mnreverse$Ex = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_FOLLOW_FAILED, Lit135, -4095);
        reverse$Mnlist$Mn$Grstring = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_STOP_FOLLOWING_FAILED, Lit136, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        string$Mn$Grlist = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_REQUEST_FRIEND_TIMELINE_FAILED, Lit137, -4095);
        string$Mnappend$Slshared = new ModuleMethod(srfi13, ErrorMessages.ERROR_TWITTER_SEARCH_FAILED, Lit138, -4096);
        string$Mnconcatenate$Slshared = new ModuleMethod(srfi13, 315, Lit139, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        string$Mnconcatenate = new ModuleMethod(srfi13, 316, Lit140, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        string$Mnconcatenate$Mnreverse = new ModuleMethod(srfi13, 317, Lit141, -4095);
        string$Mnconcatenate$Mnreverse$Slshared = new ModuleMethod(srfi13, 318, Lit142, -4095);
        $Pcfinish$Mnstring$Mnconcatenate$Mnreverse = new ModuleMethod(srfi13, 319, Lit143, 16388);
        string$Mnreplace = new ModuleMethod(srfi13, ScreenDensityUtil.DEFAULT_NORMAL_SHORT_DIMENSION, Lit144, -4092);
        string$Mntokenize = new ModuleMethod(srfi13, 321, Lit145, -4095);
        ModuleMethod moduleMethod26 = new ModuleMethod(srfi13, 322, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod26.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1738");
        lambda$Fn210 = moduleMethod26;
        xsubstring = new ModuleMethod(srfi13, 323, Lit146, -4094);
        ModuleMethod moduleMethod27 = new ModuleMethod(srfi13, 324, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod27.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1779");
        lambda$Fn216 = moduleMethod27;
        ModuleMethod moduleMethod28 = new ModuleMethod(srfi13, 325, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod28.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1785");
        lambda$Fn220 = moduleMethod28;
        string$Mnxcopy$Ex = new ModuleMethod(srfi13, 326, Lit147, -4092);
        $Pcmultispan$Mnrepcopy$Ex = new ModuleMethod(srfi13, 327, Lit148, 28679);
        string$Mnjoin = new ModuleMethod(srfi13, 328, Lit149, -4095);
        $instance.run();
    }

    public srfi13() {
        ModuleInfo.register(this);
    }

    public static Object stringCopy$Ex(Object obj, int i, CharSequence charSequence) {
        return stringCopy$Ex(obj, i, charSequence, 0);
    }

    public static Object stringCopy$Ex(Object obj, int i, CharSequence charSequence, int i2) {
        return stringCopy$Ex(obj, i, charSequence, i2, charSequence.length());
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
    }

    public static Object stringParseStart$PlEnd(Object proc, Object s, Object args) {
        boolean x;
        frame frame97 = new frame();
        frame97.proc = proc;
        frame97.s = s;
        if (!strings.isString(frame97.s)) {
            misc.error$V("Non-string value", new Object[]{frame97.proc, frame97.s});
        }
        Object obj = frame97.s;
        try {
            frame97.slen = strings.stringLength((CharSequence) obj);
            if (lists.isPair(args)) {
                Object apply1 = lists.car.apply1(args);
                frame97.args = lists.cdr.apply1(args);
                frame97.start = apply1;
                boolean x2 = numbers.isInteger(frame97.start);
                if (!x2 ? x2 : !(!(x = numbers.isExact(frame97.start)) ? !x : Scheme.numGEq.apply2(frame97.start, Lit0) == Boolean.FALSE)) {
                    return call_with_values.callWithValues(frame97.lambda$Fn1, frame97.lambda$Fn2);
                }
                return misc.error$V("Illegal substring START spec", new Object[]{frame97.proc, frame97.start, frame97.s});
            }
            return misc.values(LList.Empty, Lit0, Integer.valueOf(frame97.slen));
        } catch (ClassCastException e) {
            throw new WrongType(e, "string-length", 1, obj);
        }
    }

    public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
        switch (moduleMethod.selector) {
            case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SOURCE:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_DURATION:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 197:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case ErrorMessages.ERROR_CAMERA_NO_IMAGE_RETURNED:
                if (!(obj instanceof CharSequence)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 277:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 299:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                if (!(obj3 instanceof CharSequence)) {
                    return -786429;
                }
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            default:
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame extends ModuleBody {
        Object args;
        final ModuleMethod lambda$Fn1 = new ModuleMethod(this, 1, (Object) null, 0);
        final ModuleMethod lambda$Fn2;
        Object proc;
        Object s;
        int slen;
        Object start;

        public frame() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 2, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:150");
            this.lambda$Fn2 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 1 ? lambda1() : super.apply0(moduleMethod);
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
            return moduleMethod.selector == 2 ? lambda2(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda2(Object end, Object args2) {
            if (Scheme.numLEq.apply2(this.start, end) != Boolean.FALSE) {
                return misc.values(args2, this.start, end);
            }
            return misc.error$V("Illegal substring START/END spec", new Object[]{this.proc, this.start, end, this.s});
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

        /* access modifiers changed from: package-private */
        public Object lambda1() {
            boolean x;
            if (lists.isPair(this.args)) {
                Object end = lists.car.apply1(this.args);
                Object args2 = lists.cdr.apply1(this.args);
                boolean x2 = numbers.isInteger(end);
                if (!x2 ? !x2 : !(x = numbers.isExact(end)) ? !x : Scheme.numLEq.apply2(end, Integer.valueOf(this.slen)) == Boolean.FALSE) {
                    return misc.error$V("Illegal substring END spec", new Object[]{this.proc, end, this.s});
                }
                return misc.values(end, args2);
            }
            return misc.values(Integer.valueOf(this.slen), this.args);
        }
    }

    public static Object $PcCheckBounds(Object proc, CharSequence s, int start, int end) {
        if (start < 0) {
            return misc.error$V("Illegal substring START spec", new Object[]{proc, Integer.valueOf(start), s});
        } else if (start > end) {
            return misc.error$V("Illegal substring START/END spec", new Object[0]);
        } else {
            if (end <= strings.stringLength(s)) {
                return Values.empty;
            }
            return misc.error$V("Illegal substring END spec", new Object[]{proc, Integer.valueOf(end), s});
        }
    }

    public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
        switch (moduleMethod.selector) {
            case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_FULLSCREEN:
                callContext.value1 = obj;
                if (!(obj2 instanceof CharSequence)) {
                    return -786430;
                }
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.value4 = obj4;
                callContext.proc = moduleMethod;
                callContext.pc = 4;
                return 0;
            case 198:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.value4 = obj4;
                callContext.proc = moduleMethod;
                callContext.pc = 4;
                return 0;
            case 204:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.value4 = obj4;
                callContext.proc = moduleMethod;
                callContext.pc = 4;
                return 0;
            case 206:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.value4 = obj4;
                callContext.proc = moduleMethod;
                callContext.pc = 4;
                return 0;
            case 299:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                if (!(obj3 instanceof CharSequence)) {
                    return -786429;
                }
                callContext.value3 = obj3;
                callContext.value4 = obj4;
                callContext.proc = moduleMethod;
                callContext.pc = 4;
                return 0;
            case 319:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.value4 = obj4;
                callContext.proc = moduleMethod;
                callContext.pc = 4;
                return 0;
            default:
                return super.match4(moduleMethod, obj, obj2, obj3, obj4, callContext);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame0 extends ModuleBody {
        Object args;
        final ModuleMethod lambda$Fn3 = new ModuleMethod(this, 3, (Object) null, 0);
        final ModuleMethod lambda$Fn4;
        Object proc;
        Object s;

        public frame0() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 4, (Object) null, 12291);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:174");
            this.lambda$Fn4 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 3 ? lambda3() : super.apply0(moduleMethod);
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
            return moduleMethod.selector == 4 ? lambda4(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        /* access modifiers changed from: package-private */
        public Object lambda3() {
            return srfi13.stringParseStart$PlEnd(this.proc, this.s, this.args);
        }

        /* access modifiers changed from: package-private */
        public Object lambda4(Object rest, Object start, Object end) {
            if (lists.isPair(rest)) {
                return misc.error$V("Extra arguments to procedure", new Object[]{this.proc, rest});
            }
            return misc.values(start, end);
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

    public static Object stringParseFinalStart$PlEnd(Object proc, Object s, Object args) {
        frame0 frame02 = new frame0();
        frame02.proc = proc;
        frame02.s = s;
        frame02.args = args;
        return call_with_values.callWithValues(frame02.lambda$Fn3, frame02.lambda$Fn4);
    }

    public static boolean isSubstringSpecOk(Object s, Object start, Object end) {
        boolean x = strings.isString(s);
        if (!x) {
            return x;
        }
        boolean x2 = numbers.isInteger(start);
        if (!x2) {
            return x2;
        }
        boolean x3 = numbers.isExact(start);
        if (!x3) {
            return x3;
        }
        boolean x4 = numbers.isInteger(end);
        if (!x4) {
            return x4;
        }
        boolean x5 = numbers.isExact(end);
        if (!x5) {
            return x5;
        }
        Object apply2 = Scheme.numLEq.apply2(Lit0, start);
        try {
            boolean x6 = ((Boolean) apply2).booleanValue();
            if (!x6) {
                return x6;
            }
            Object apply22 = Scheme.numLEq.apply2(start, end);
            try {
                boolean x7 = ((Boolean) apply22).booleanValue();
                if (!x7) {
                    return x7;
                }
                try {
                    return ((Boolean) Scheme.numLEq.apply2(end, Integer.valueOf(strings.stringLength((CharSequence) s)))).booleanValue();
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-length", 1, s);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "x", -2, apply22);
            }
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "x", -2, apply2);
        }
    }

    public static Object checkSubstringSpec(Object proc, Object s, Object start, Object end) {
        if (isSubstringSpecOk(s, start, end)) {
            return Values.empty;
        }
        return misc.error$V("Illegal substring spec.", new Object[]{proc, s, start, end});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0007, code lost:
        if (r0 != false) goto L_0x0009;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Object $PcCheckSubstringSpec(java.lang.Object r5, java.lang.CharSequence r6, int r7, int r8) {
        /*
            r1 = 1
            r2 = 0
            if (r7 >= 0) goto L_0x0025
            r0 = r1
        L_0x0005:
            if (r0 == 0) goto L_0x0027
            if (r0 == 0) goto L_0x002e
        L_0x0009:
            java.lang.String r3 = "Illegal substring spec."
            r4 = 4
            java.lang.Object[] r4 = new java.lang.Object[r4]
            r4[r2] = r5
            r4[r1] = r6
            r1 = 2
            java.lang.Integer r2 = java.lang.Integer.valueOf(r7)
            r4[r1] = r2
            r1 = 3
            java.lang.Integer r2 = java.lang.Integer.valueOf(r8)
            r4[r1] = r2
            java.lang.Object r1 = kawa.lib.misc.error$V(r3, r4)
        L_0x0024:
            return r1
        L_0x0025:
            r0 = r2
            goto L_0x0005
        L_0x0027:
            if (r7 <= r8) goto L_0x0031
            r0 = r1
        L_0x002a:
            if (r0 == 0) goto L_0x0033
            if (r0 != 0) goto L_0x0009
        L_0x002e:
            gnu.mapping.Values r1 = gnu.mapping.Values.empty
            goto L_0x0024
        L_0x0031:
            r0 = r2
            goto L_0x002a
        L_0x0033:
            int r3 = kawa.lib.strings.stringLength(r6)
            if (r8 <= r3) goto L_0x002e
            goto L_0x0009
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi13.$PcCheckSubstringSpec(java.lang.Object, java.lang.CharSequence, int, int):java.lang.Object");
    }

    public static Object substring$SlShared$V(Object s, Object start, Object[] argsArray) {
        frame1 frame110 = new frame1();
        frame110.start = start;
        LList maybe$Mnend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), strings.string$Qu, s, substring$Slshared);
            try {
                frame110.slen = strings.stringLength((CharSequence) s);
                try {
                    Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), lambda$Fn5, frame110.start, substring$Slshared);
                    try {
                        CharSequence charSequence = (CharSequence) s;
                        Object obj = frame110.start;
                        try {
                            int intValue = ((Number) obj).intValue();
                            try {
                                Object apply4 = Scheme.applyToArgs.apply4(loc$$Cloptional.get(), maybe$Mnend, Integer.valueOf(frame110.slen), frame110.lambda$Fn6);
                                try {
                                    return $PcSubstring$SlShared(charSequence, intValue, ((Number) apply4).intValue());
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "%substring/shared", 2, apply4);
                                }
                            } catch (UnboundLocationException e2) {
                                e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 226, 10);
                                throw e2;
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "%substring/shared", 1, obj);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "%substring/shared", 0, s);
                    }
                } catch (UnboundLocationException e5) {
                    e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 223, 5);
                    throw e5;
                }
            } catch (ClassCastException e6) {
                throw new WrongType(e6, "string-length", 1, s);
            }
        } catch (UnboundLocationException e7) {
            e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", YaVersion.YOUNG_ANDROID_VERSION, 3);
            throw e7;
        }
    }

    public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 200:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_NO_CAMERA_PERMISSION:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 203:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 205:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 207:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 208:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 210:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 212:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 213:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 214:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 215:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 216:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 219:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 220:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case YaVersion.YOUNG_ANDROID_VERSION:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 222:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 223:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 224:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 225:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 226:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 227:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 228:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 229:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 230:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 231:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 232:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 233:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 234:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 235:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 236:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 237:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 238:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case LispEscapeFormat.ESCAPE_NORMAL:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 243:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 246:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 249:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case Telnet.WILL:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case Telnet.DO:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 256:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 258:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 261:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 264:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 266:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 268:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 269:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 270:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 272:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 273:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 274:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 275:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 276:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 278:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 279:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 284:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 285:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 286:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 288:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 290:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 291:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 292:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 293:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 294:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 295:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 296:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 297:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 298:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 299:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_BLANK_CONSUMER_KEY_OR_SECRET:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_EXCEPTION:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_UNABLE_TO_GET_ACCESS_TOKEN:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_AUTHORIZATION_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_SET_STATUS_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_REQUEST_MENTIONS_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_REQUEST_FOLLOWERS_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_DIRECT_MESSAGE_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_FOLLOW_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_REQUEST_FRIEND_TIMELINE_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ErrorMessages.ERROR_TWITTER_SEARCH_FAILED:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 317:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 318:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case ScreenDensityUtil.DEFAULT_NORMAL_SHORT_DIMENSION:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 321:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 323:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 326:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 327:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 328:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            default:
                return super.matchN(moduleMethod, objArr, callContext);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame1 extends ModuleBody {
        final ModuleMethod lambda$Fn6;
        int slen;
        Object start;

        public frame1() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 5, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:227");
            this.lambda$Fn6 = moduleMethod;
        }

        static boolean lambda5(Object start2) {
            boolean x = numbers.isInteger(start2);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(start2);
            return x2 ? ((Boolean) Scheme.numLEq.apply2(srfi13.Lit0, start2)).booleanValue() : x2;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 5) {
                return lambda6(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda6(Object end) {
            boolean x = numbers.isInteger(end);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(end);
            if (!x2) {
                return x2;
            }
            Object apply2 = Scheme.numLEq.apply2(this.start, end);
            try {
                boolean x3 = ((Boolean) apply2).booleanValue();
                if (x3) {
                    return ((Boolean) Scheme.numLEq.apply2(end, Integer.valueOf(this.slen))).booleanValue();
                }
                return x3;
            } catch (ClassCastException e) {
                throw new WrongType(e, "x", -2, apply2);
            }
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 5) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 199:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 209:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 211:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 217:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 239:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 240:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case LispEscapeFormat.ESCAPE_ALL:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 244:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 245:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 247:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 248:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case Telnet.WONT:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case Telnet.DONT:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 255:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case InputDeviceCompat.SOURCE_KEYBOARD:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 259:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 260:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 262:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 263:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 265:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 267:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 271:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 287:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 289:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case ErrorMessages.ERROR_TWITTER_REQUEST_DIRECT_MESSAGES_FAILED:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case ErrorMessages.ERROR_TWITTER_STOP_FOLLOWING_FAILED:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 315:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 316:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 322:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 324:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 325:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public static Object $PcSubstring$SlShared(CharSequence s, int start, int end) {
        boolean x = numbers.isZero(Integer.valueOf(start));
        if (x) {
            if (end == strings.stringLength(s)) {
                return s;
            }
        } else if (x) {
            return s;
        }
        return strings.substring(s, start, end);
    }

    /* compiled from: srfi13.scm */
    public class frame2 extends ModuleBody {
        final ModuleMethod lambda$Fn7 = new ModuleMethod(this, 6, (Object) null, 0);
        final ModuleMethod lambda$Fn8 = new ModuleMethod(this, 7, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 6 ? lambda7() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 7 ? lambda8(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 6) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 7) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda7() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mncopy, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda8(Object start, Object end) {
            Object obj = this.s;
            try {
                try {
                    try {
                        return strings.substring((CharSequence) obj, ((Number) start).intValue(), ((Number) end).intValue());
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "substring", 3, end);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "substring", 2, start);
                }
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "substring", 1, obj);
            }
        }
    }

    public static Object stringCopy$V(Object s, Object[] argsArray) {
        frame2 frame210 = new frame2();
        frame210.s = s;
        frame210.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame210.lambda$Fn7, frame210.lambda$Fn8);
    }

    public static Object stringMap$V(Object proc, Object s, Object[] argsArray) {
        frame3 frame310 = new frame3();
        frame310.proc = proc;
        frame310.s = s;
        frame310.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame310.proc, string$Mnmap);
            return call_with_values.callWithValues(frame310.lambda$Fn9, frame310.lambda$Fn10);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 271, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame3 extends ModuleBody {
        final ModuleMethod lambda$Fn10 = new ModuleMethod(this, 9, (Object) null, 8194);
        final ModuleMethod lambda$Fn9 = new ModuleMethod(this, 8, (Object) null, 0);
        LList maybe$Mnstart$Plend;
        Object proc;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 8 ? lambda9() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 9 ? lambda10(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 8) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 9) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda9() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnmap, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda10(Object start, Object end) {
            return srfi13.$PcStringMap(this.proc, this.s, start, end);
        }
    }

    public static Object $PcStringMap(Object proc, Object s, Object start, Object end) {
        Object len = AddOp.$Mn.apply2(end, start);
        try {
            CharSequence ans = strings.makeString(((Number) len).intValue());
            Object i = AddOp.$Mn.apply2(end, Lit1);
            Object apply2 = AddOp.$Mn.apply2(len, Lit1);
            while (Scheme.numLss.apply2(apply2, Lit0) == Boolean.FALSE) {
                try {
                    CharSeq charSeq = (CharSeq) ans;
                    try {
                        int intValue = ((Number) apply2).intValue();
                        try {
                            try {
                                Object apply22 = Scheme.applyToArgs.apply2(proc, Char.make(strings.stringRef((CharSequence) s, ((Number) i).intValue())));
                                try {
                                    strings.stringSet$Ex(charSeq, intValue, ((Char) apply22).charValue());
                                    i = AddOp.$Mn.apply2(i, Lit1);
                                    apply2 = AddOp.$Mn.apply2(apply2, Lit1);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-set!", 3, apply22);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 2, i);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "string-ref", 1, s);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "string-set!", 2, apply2);
                    }
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "string-set!", 1, (Object) ans);
                }
            }
            return ans;
        } catch (ClassCastException e6) {
            throw new WrongType(e6, "make-string", 1, len);
        }
    }

    public static Object stringMap$Ex$V(Object proc, Object s, Object[] argsArray) {
        frame4 frame410 = new frame4();
        frame410.proc = proc;
        frame410.s = s;
        frame410.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame410.proc, string$Mnmap$Ex);
            return call_with_values.callWithValues(frame410.lambda$Fn11, frame410.lambda$Fn12);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 285, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame4 extends ModuleBody {
        final ModuleMethod lambda$Fn11 = new ModuleMethod(this, 10, (Object) null, 0);
        final ModuleMethod lambda$Fn12 = new ModuleMethod(this, 11, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object proc;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 10 ? lambda11() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 11 ? lambda12(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 10) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 11) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda11() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnmap$Ex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda12(Object start, Object end) {
            return srfi13.$PcStringMap$Ex(this.proc, this.s, start, end);
        }
    }

    public static Object $PcStringMap$Ex(Object proc, Object s, Object start, Object end) {
        Object i = AddOp.$Mn.apply2(end, Lit1);
        while (Scheme.numLss.apply2(i, start) == Boolean.FALSE) {
            try {
                CharSeq charSeq = (CharSeq) s;
                try {
                    int intValue = ((Number) i).intValue();
                    try {
                        try {
                            Object apply2 = Scheme.applyToArgs.apply2(proc, Char.make(strings.stringRef((CharSequence) s, ((Number) i).intValue())));
                            try {
                                strings.stringSet$Ex(charSeq, intValue, ((Char) apply2).charValue());
                                i = AddOp.$Mn.apply2(i, Lit1);
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "string-set!", 3, apply2);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "string-ref", 2, i);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "string-ref", 1, s);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "string-set!", 2, i);
                }
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "string-set!", 1, s);
            }
        }
        return Values.empty;
    }

    public static Object stringFold$V(Object kons, Object knil, Object s, Object[] argsArray) {
        frame5 frame510 = new frame5();
        frame510.kons = kons;
        frame510.knil = knil;
        frame510.s = s;
        frame510.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame510.kons, string$Mnfold);
            return call_with_values.callWithValues(frame510.lambda$Fn13, frame510.lambda$Fn14);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 295, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame5 extends ModuleBody {
        Object knil;
        Object kons;
        final ModuleMethod lambda$Fn13 = new ModuleMethod(this, 12, (Object) null, 0);
        final ModuleMethod lambda$Fn14 = new ModuleMethod(this, 13, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 12 ? lambda13() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 13 ? lambda14(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 12) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda13() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnfold, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda14(Object start, Object end) {
            Object v = this.knil;
            Object obj = start;
            while (Scheme.numLss.apply2(obj, end) != Boolean.FALSE) {
                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                Object obj2 = this.kons;
                Object obj3 = this.s;
                try {
                    try {
                        v = applyToArgs.apply3(obj2, Char.make(strings.stringRef((CharSequence) obj3, ((Number) obj).intValue())), v);
                        obj = AddOp.$Pl.apply2(obj, srfi13.Lit1);
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string-ref", 2, obj);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "string-ref", 1, obj3);
                }
            }
            return v;
        }
    }

    public static Object stringFoldRight$V(Object kons, Object knil, Object s, Object[] argsArray) {
        frame6 frame610 = new frame6();
        frame610.kons = kons;
        frame610.knil = knil;
        frame610.s = s;
        frame610.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame610.kons, string$Mnfold$Mnright);
            return call_with_values.callWithValues(frame610.lambda$Fn15, frame610.lambda$Fn16);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_TWITTER_BLANK_CONSUMER_KEY_OR_SECRET, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame6 extends ModuleBody {
        Object knil;
        Object kons;
        final ModuleMethod lambda$Fn15 = new ModuleMethod(this, 14, (Object) null, 0);
        final ModuleMethod lambda$Fn16 = new ModuleMethod(this, 15, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 14 ? lambda15() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 15 ? lambda16(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 14) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 15) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda15() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnfold$Mnright, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda16(Object start, Object end) {
            Object v = this.knil;
            Object apply2 = AddOp.$Mn.apply2(end, srfi13.Lit1);
            while (Scheme.numGEq.apply2(apply2, start) != Boolean.FALSE) {
                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                Object obj = this.kons;
                Object obj2 = this.s;
                try {
                    try {
                        v = applyToArgs.apply3(obj, Char.make(strings.stringRef((CharSequence) obj2, ((Number) apply2).intValue())), v);
                        apply2 = AddOp.$Mn.apply2(apply2, srfi13.Lit1);
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string-ref", 2, apply2);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "string-ref", 1, obj2);
                }
            }
            return v;
        }
    }

    public static Object stringUnfold$V(Object p, Object f, Object g, Object seed, Object[] argsArray) {
        Object valueOf;
        Object obj;
        Object obj2;
        LList base$Plmake$Mnfinal = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, p, string$Mnunfold);
            try {
                Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, f, string$Mnunfold);
                try {
                    Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, g, string$Mnunfold);
                    ApplyToArgs applyToArgs = Scheme.applyToArgs;
                    try {
                        Object obj3 = loc$let$Mnoptionals$St.get();
                        try {
                            try {
                                try {
                                    try {
                                        Object apply2 = Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply3(loc$base.get(), "", strings.isString(loc$base.get()) ? Boolean.TRUE : Boolean.FALSE), Scheme.applyToArgs.apply3(loc$make$Mnfinal.get(), lambda$Fn17, misc.isProcedure(loc$make$Mnfinal.get()) ? Boolean.TRUE : Boolean.FALSE));
                                        Object obj4 = LList.Empty;
                                        int i = 0;
                                        CharSequence makeString = strings.makeString(40);
                                        int i2 = 40;
                                        int i3 = 0;
                                        loop0:
                                        while (true) {
                                            valueOf = Integer.valueOf(i3);
                                            while (Scheme.applyToArgs.apply2(p, seed) == Boolean.FALSE) {
                                                Object c = Scheme.applyToArgs.apply2(f, seed);
                                                seed = Scheme.applyToArgs.apply2(g, seed);
                                                if (Scheme.numLss.apply2(valueOf, Integer.valueOf(i2)) != Boolean.FALSE) {
                                                    try {
                                                        try {
                                                            try {
                                                                strings.stringSet$Ex((CharSeq) makeString, ((Number) valueOf).intValue(), ((Char) c).charValue());
                                                                valueOf = AddOp.$Pl.apply2(valueOf, Lit1);
                                                            } catch (ClassCastException e) {
                                                                throw new WrongType(e, "string-set!", 3, c);
                                                            }
                                                        } catch (ClassCastException e2) {
                                                            throw new WrongType(e2, "string-set!", 2, valueOf);
                                                        }
                                                    } catch (ClassCastException e3) {
                                                        throw new WrongType(e3, "string-set!", 1, (Object) makeString);
                                                    }
                                                } else {
                                                    Object min = numbers.min(Lit2, Integer.valueOf(i2 + i));
                                                    try {
                                                        int chunk$Mnlen2 = ((Number) min).intValue();
                                                        CharSequence new$Mnchunk = strings.makeString(chunk$Mnlen2);
                                                        try {
                                                            try {
                                                                strings.stringSet$Ex((CharSeq) new$Mnchunk, 0, ((Char) c).charValue());
                                                                obj4 = lists.cons(makeString, obj4);
                                                                i += i2;
                                                                i3 = 1;
                                                                i2 = chunk$Mnlen2;
                                                                makeString = new$Mnchunk;
                                                            } catch (ClassCastException e4) {
                                                                throw new WrongType(e4, "string-set!", 3, c);
                                                            }
                                                        } catch (ClassCastException e5) {
                                                            throw new WrongType(e5, "string-set!", 1, (Object) new$Mnchunk);
                                                        }
                                                    } catch (ClassCastException e6) {
                                                        throw new WrongType(e6, "chunk-len2", -2, min);
                                                    }
                                                }
                                            }
                                            break loop0;
                                        }
                                        try {
                                            Object apply22 = Scheme.applyToArgs.apply2(loc$make$Mnfinal.get(), seed);
                                            try {
                                                int flen = strings.stringLength((CharSequence) apply22);
                                                try {
                                                    try {
                                                        int base$Mnlen = strings.stringLength((CharSequence) loc$base.get());
                                                        Object apply23 = AddOp.$Pl.apply2(Integer.valueOf(base$Mnlen + i), valueOf);
                                                        try {
                                                            int j = ((Number) apply23).intValue();
                                                            CharSequence ans = strings.makeString(j + flen);
                                                            try {
                                                                $PcStringCopy$Ex(ans, j, (CharSequence) apply22, 0, flen);
                                                                Object apply24 = AddOp.$Mn.apply2(Integer.valueOf(j), valueOf);
                                                                try {
                                                                    int j2 = ((Number) apply24).intValue();
                                                                    try {
                                                                        try {
                                                                            $PcStringCopy$Ex(ans, j2, makeString, 0, ((Number) valueOf).intValue());
                                                                            Object j3 = Integer.valueOf(j2);
                                                                            while (lists.isPair(obj4)) {
                                                                                Object chunk = lists.car.apply1(obj4);
                                                                                obj4 = lists.cdr.apply1(obj4);
                                                                                try {
                                                                                    int chunk$Mnlen = strings.stringLength((CharSequence) chunk);
                                                                                    j3 = AddOp.$Mn.apply2(j3, Integer.valueOf(chunk$Mnlen));
                                                                                    try {
                                                                                        try {
                                                                                            $PcStringCopy$Ex(ans, ((Number) j3).intValue(), (CharSequence) chunk, 0, chunk$Mnlen);
                                                                                        } catch (ClassCastException e7) {
                                                                                            throw new WrongType(e7, "%string-copy!", 2, chunk);
                                                                                        }
                                                                                    } catch (ClassCastException e8) {
                                                                                        throw new WrongType(e8, "%string-copy!", 1, j3);
                                                                                    }
                                                                                } catch (ClassCastException e9) {
                                                                                    throw new WrongType(e9, "string-length", 1, chunk);
                                                                                }
                                                                            }
                                                                            try {
                                                                                try {
                                                                                    $PcStringCopy$Ex(ans, 0, (CharSequence) loc$base.get(), 0, base$Mnlen);
                                                                                    return applyToArgs.apply4(obj3, base$Plmake$Mnfinal, apply2, ans);
                                                                                } catch (ClassCastException e10) {
                                                                                    throw new WrongType(e10, "%string-copy!", 2, obj2);
                                                                                }
                                                                            } catch (UnboundLocationException e11) {
                                                                                e11.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_NXT_UNABLE_TO_DOWNLOAD_FILE, 29);
                                                                                throw e11;
                                                                            }
                                                                        } catch (ClassCastException e12) {
                                                                            throw new WrongType(e12, "%string-copy!", 4, valueOf);
                                                                        }
                                                                    } catch (ClassCastException e13) {
                                                                        throw new WrongType(e13, "%string-copy!", 2, (Object) makeString);
                                                                    }
                                                                } catch (ClassCastException e14) {
                                                                    throw new WrongType(e14, "j", -2, apply24);
                                                                }
                                                            } catch (ClassCastException e15) {
                                                                throw new WrongType(e15, "%string-copy!", 2, apply22);
                                                            }
                                                        } catch (ClassCastException e16) {
                                                            throw new WrongType(e16, "j", -2, apply23);
                                                        }
                                                    } catch (ClassCastException e17) {
                                                        throw new WrongType(e17, "string-length", 1, obj);
                                                    }
                                                } catch (UnboundLocationException e18) {
                                                    e18.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_NXT_NOT_CONNECTED_TO_ROBOT, 38);
                                                    throw e18;
                                                }
                                            } catch (ClassCastException e19) {
                                                throw new WrongType(e19, "string-length", 1, apply22);
                                            }
                                        } catch (UnboundLocationException e20) {
                                            e20.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 400, 20);
                                            throw e20;
                                        }
                                    } catch (UnboundLocationException e21) {
                                        e21.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 377, 46);
                                        throw e21;
                                    }
                                } catch (UnboundLocationException e22) {
                                    e22.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 377, 6);
                                    throw e22;
                                }
                            } catch (UnboundLocationException e23) {
                                e23.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 376, 57);
                                throw e23;
                            }
                        } catch (UnboundLocationException e24) {
                            e24.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 376, 20);
                            throw e24;
                        }
                    } catch (UnboundLocationException e25) {
                        e25.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 375, 3);
                        throw e25;
                    }
                } catch (UnboundLocationException e26) {
                    e26.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 374, 3);
                    throw e26;
                }
            } catch (UnboundLocationException e27) {
                e27.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 373, 3);
                throw e27;
            }
        } catch (UnboundLocationException e28) {
            e28.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 372, 3);
            throw e28;
        }
    }

    static String lambda17(Object x) {
        return "";
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v0, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r16v1, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v33, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v53, resolved type: java.lang.Number} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v42, resolved type: java.lang.Number} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object stringUnfoldRight$V(java.lang.Object r29, java.lang.Object r30, java.lang.Object r31, java.lang.Object r32, java.lang.Object[] r33) {
        /*
            r21 = 0
            r0 = r33
            r1 = r21
            gnu.lists.LList r7 = gnu.lists.LList.makeList(r0, r1)
            gnu.kawa.functions.ApplyToArgs r25 = kawa.standard.Scheme.applyToArgs
            gnu.mapping.Location r21 = loc$let$Mnoptionals$St
            java.lang.Object r26 = r21.get()     // Catch:{ UnboundLocationException -> 0x0279 }
            gnu.kawa.functions.ApplyToArgs r22 = kawa.standard.Scheme.applyToArgs
            gnu.kawa.functions.ApplyToArgs r23 = kawa.standard.Scheme.applyToArgs
            gnu.mapping.Location r21 = loc$base
            java.lang.Object r24 = r21.get()     // Catch:{ UnboundLocationException -> 0x0284 }
            java.lang.String r27 = ""
            gnu.mapping.Location r21 = loc$base
            java.lang.Object r21 = r21.get()     // Catch:{ UnboundLocationException -> 0x028f }
            boolean r21 = kawa.lib.strings.isString(r21)
            if (r21 == 0) goto L_0x00e7
            java.lang.Boolean r21 = java.lang.Boolean.TRUE
        L_0x002c:
            r0 = r23
            r1 = r24
            r2 = r27
            r3 = r21
            java.lang.Object r23 = r0.apply3(r1, r2, r3)
            gnu.kawa.functions.ApplyToArgs r24 = kawa.standard.Scheme.applyToArgs
            gnu.mapping.Location r21 = loc$make$Mnfinal
            java.lang.Object r27 = r21.get()     // Catch:{ UnboundLocationException -> 0x029a }
            gnu.expr.ModuleMethod r28 = lambda$Fn18
            gnu.mapping.Location r21 = loc$make$Mnfinal
            java.lang.Object r21 = r21.get()     // Catch:{ UnboundLocationException -> 0x02a5 }
            boolean r21 = kawa.lib.misc.isProcedure(r21)
            if (r21 == 0) goto L_0x00eb
            java.lang.Boolean r21 = java.lang.Boolean.TRUE
        L_0x0050:
            r0 = r24
            r1 = r27
            r2 = r28
            r3 = r21
            java.lang.Object r21 = r0.apply3(r1, r2, r3)
            r0 = r22
            r1 = r23
            r2 = r21
            java.lang.Object r27 = r0.apply2(r1, r2)
            gnu.lists.LList r13 = gnu.lists.LList.Empty
            gnu.math.IntNum r18 = Lit0
            r21 = 40
            java.lang.CharSequence r22 = kawa.lib.strings.makeString(r21)
            gnu.math.IntNum r24 = Lit3
            gnu.math.IntNum r16 = Lit3
        L_0x0074:
            gnu.kawa.functions.ApplyToArgs r21 = kawa.standard.Scheme.applyToArgs
            r0 = r21
            r1 = r29
            r2 = r32
            java.lang.Object r21 = r0.apply2(r1, r2)
            java.lang.Boolean r23 = java.lang.Boolean.FALSE
            r0 = r21
            r1 = r23
            if (r0 != r1) goto L_0x0167
            gnu.kawa.functions.ApplyToArgs r21 = kawa.standard.Scheme.applyToArgs
            r0 = r21
            r1 = r30
            r2 = r32
            java.lang.Object r8 = r0.apply2(r1, r2)
            gnu.kawa.functions.ApplyToArgs r21 = kawa.standard.Scheme.applyToArgs
            r0 = r21
            r1 = r31
            r2 = r32
            java.lang.Object r32 = r0.apply2(r1, r2)
            gnu.kawa.functions.NumberCompare r21 = kawa.standard.Scheme.numGrt
            gnu.math.IntNum r23 = Lit0
            r0 = r21
            r1 = r16
            r2 = r23
            java.lang.Object r21 = r0.apply2(r1, r2)
            java.lang.Boolean r23 = java.lang.Boolean.FALSE
            r0 = r21
            r1 = r23
            if (r0 == r1) goto L_0x00ef
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r23 = Lit1
            r0 = r21
            r1 = r16
            r2 = r23
            java.lang.Object r16 = r0.apply2(r1, r2)
            r0 = r22
            gnu.lists.CharSeq r0 = (gnu.lists.CharSeq) r0     // Catch:{ ClassCastException -> 0x02b0 }
            r21 = r0
            r0 = r16
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x02c5 }
            r23 = r0
            int r28 = r23.intValue()     // Catch:{ ClassCastException -> 0x02c5 }
            r0 = r8
            gnu.text.Char r0 = (gnu.text.Char) r0     // Catch:{ ClassCastException -> 0x02da }
            r23 = r0
            char r23 = r23.charValue()     // Catch:{ ClassCastException -> 0x02da }
            r0 = r21
            r1 = r28
            r2 = r23
            kawa.lib.strings.stringSet$Ex(r0, r1, r2)
            goto L_0x0074
        L_0x00e7:
            java.lang.Boolean r21 = java.lang.Boolean.FALSE
            goto L_0x002c
        L_0x00eb:
            java.lang.Boolean r21 = java.lang.Boolean.FALSE
            goto L_0x0050
        L_0x00ef:
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Pl
            r0 = r21
            r1 = r24
            r2 = r18
            java.lang.Object r19 = r0.apply2(r1, r2)
            r21 = 2
            r0 = r21
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r21 = r0
            r23 = 0
            gnu.math.IntNum r28 = Lit4
            r21[r23] = r28
            r23 = 1
            r21[r23] = r19
            java.lang.Object r11 = kawa.lib.numbers.min(r21)
            r0 = r11
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x02ed }
            r21 = r0
            int r21 = r21.intValue()     // Catch:{ ClassCastException -> 0x02ed }
            java.lang.CharSequence r20 = kawa.lib.strings.makeString(r21)
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r23 = Lit1
            r0 = r21
            r1 = r23
            java.lang.Object r16 = r0.apply2(r11, r1)
            r0 = r20
            gnu.lists.CharSeq r0 = (gnu.lists.CharSeq) r0     // Catch:{ ClassCastException -> 0x0300 }
            r21 = r0
            r0 = r16
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0315 }
            r23 = r0
            int r28 = r23.intValue()     // Catch:{ ClassCastException -> 0x0315 }
            r0 = r8
            gnu.text.Char r0 = (gnu.text.Char) r0     // Catch:{ ClassCastException -> 0x032a }
            r23 = r0
            char r23 = r23.charValue()     // Catch:{ ClassCastException -> 0x032a }
            r0 = r21
            r1 = r28
            r2 = r23
            kawa.lib.strings.stringSet$Ex(r0, r1, r2)
            r0 = r22
            gnu.lists.Pair r13 = kawa.lib.lists.cons(r0, r13)
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Pl
            r0 = r21
            r1 = r18
            r2 = r24
            java.lang.Object r18 = r0.apply2(r1, r2)
            r10 = r11
            r9 = r20
            r24 = r10
            r22 = r9
            goto L_0x0074
        L_0x0167:
            gnu.kawa.functions.ApplyToArgs r21 = kawa.standard.Scheme.applyToArgs
            gnu.mapping.Location r23 = loc$make$Mnfinal
            java.lang.Object r23 = r23.get()     // Catch:{ UnboundLocationException -> 0x033d }
            r0 = r21
            r1 = r23
            r2 = r32
            java.lang.Object r14 = r0.apply2(r1, r2)
            r0 = r14
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0348 }
            r21 = r0
            int r15 = kawa.lib.strings.stringLength(r21)
            gnu.mapping.Location r21 = loc$base
            java.lang.Object r21 = r21.get()     // Catch:{ UnboundLocationException -> 0x035b }
            java.lang.CharSequence r21 = (java.lang.CharSequence) r21     // Catch:{ ClassCastException -> 0x0366 }
            int r6 = kawa.lib.strings.stringLength(r21)
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Mn
            r0 = r21
            r1 = r24
            r2 = r16
            java.lang.Object r12 = r0.apply2(r1, r2)
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Pl
            gnu.kawa.functions.AddOp r23 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r28 = java.lang.Integer.valueOf(r6)
            r0 = r23
            r1 = r28
            r2 = r18
            java.lang.Object r23 = r0.apply2(r1, r2)
            r0 = r21
            r1 = r23
            java.lang.Object r17 = r0.apply2(r1, r12)
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r23 = java.lang.Integer.valueOf(r15)
            r0 = r21
            r1 = r17
            r2 = r23
            java.lang.Object r23 = r0.apply2(r1, r2)
            r0 = r23
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x037b }
            r21 = r0
            int r21 = r21.intValue()     // Catch:{ ClassCastException -> 0x037b }
            java.lang.CharSequence r5 = kawa.lib.strings.makeString(r21)
            r21 = 0
            java.lang.CharSequence r14 = (java.lang.CharSequence) r14     // Catch:{ ClassCastException -> 0x0390 }
            r23 = 0
            r0 = r21
            r1 = r23
            $PcStringCopy$Ex(r5, r0, r14, r1, r15)
            java.lang.CharSequence r22 = (java.lang.CharSequence) r22     // Catch:{ ClassCastException -> 0x03a3 }
            r0 = r16
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x03b8 }
            r21 = r0
            int r23 = r21.intValue()     // Catch:{ ClassCastException -> 0x03b8 }
            r0 = r24
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x03cd }
            r21 = r0
            int r21 = r21.intValue()     // Catch:{ ClassCastException -> 0x03cd }
            r0 = r22
            r1 = r23
            r2 = r21
            $PcStringCopy$Ex(r5, r15, r0, r1, r2)
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r22 = java.lang.Integer.valueOf(r15)
            r0 = r21
            r1 = r22
            java.lang.Object r17 = r0.apply2(r1, r12)
        L_0x020c:
            boolean r21 = kawa.lib.lists.isPair(r13)
            if (r21 == 0) goto L_0x0251
            gnu.expr.GenericProc r21 = kawa.lib.lists.car
            r0 = r21
            java.lang.Object r9 = r0.apply1(r13)
            gnu.expr.GenericProc r21 = kawa.lib.lists.cdr
            r0 = r21
            java.lang.Object r13 = r0.apply1(r13)
            r0 = r9
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x03e2 }
            r21 = r0
            int r10 = kawa.lib.strings.stringLength(r21)
            r0 = r17
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x03f5 }
            r21 = r0
            int r21 = r21.intValue()     // Catch:{ ClassCastException -> 0x03f5 }
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x040a }
            r22 = 0
            r0 = r21
            r1 = r22
            $PcStringCopy$Ex(r5, r0, r9, r1, r10)
            gnu.kawa.functions.AddOp r21 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r22 = java.lang.Integer.valueOf(r10)
            r0 = r21
            r1 = r17
            r2 = r22
            java.lang.Object r17 = r0.apply2(r1, r2)
            goto L_0x020c
        L_0x0251:
            r0 = r17
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x041d }
            r21 = r0
            int r22 = r21.intValue()     // Catch:{ ClassCastException -> 0x041d }
            gnu.mapping.Location r21 = loc$base
            java.lang.Object r21 = r21.get()     // Catch:{ UnboundLocationException -> 0x0432 }
            java.lang.CharSequence r21 = (java.lang.CharSequence) r21     // Catch:{ ClassCastException -> 0x043d }
            r23 = 0
            r0 = r22
            r1 = r21
            r2 = r23
            $PcStringCopy$Ex(r5, r0, r1, r2, r6)
            r0 = r25
            r1 = r26
            r2 = r27
            java.lang.Object r21 = r0.apply4(r1, r7, r2, r5)
            return r21
        L_0x0279:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 420(0x1a4, float:5.89E-43)
            r24 = 3
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x0284:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 421(0x1a5, float:5.9E-43)
            r24 = 20
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x028f:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 421(0x1a5, float:5.9E-43)
            r24 = 57
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x029a:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 422(0x1a6, float:5.91E-43)
            r24 = 6
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x02a5:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 422(0x1a6, float:5.91E-43)
            r24 = 46
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x02b0:
            r21 = move-exception
            gnu.mapping.WrongType r23 = new gnu.mapping.WrongType
            java.lang.String r24 = "string-set!"
            r25 = 1
            r0 = r23
            r1 = r21
            r2 = r24
            r3 = r25
            r4 = r22
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r23
        L_0x02c5:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "string-set!"
            r24 = 2
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r4 = r16
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x02da:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "string-set!"
            r24 = 3
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r8)
            throw r22
        L_0x02ed:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "make-string"
            r24 = 1
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r11)
            throw r22
        L_0x0300:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "string-set!"
            r24 = 1
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r4 = r20
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x0315:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "string-set!"
            r24 = 2
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r4 = r16
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x032a:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "string-set!"
            r24 = 3
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r8)
            throw r22
        L_0x033d:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 447(0x1bf, float:6.26E-43)
            r24 = 20
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x0348:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "string-length"
            r24 = 1
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r14)
            throw r22
        L_0x035b:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 449(0x1c1, float:6.29E-43)
            r24 = 31
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x0366:
            r22 = move-exception
            gnu.mapping.WrongType r23 = new gnu.mapping.WrongType
            java.lang.String r24 = "string-length"
            r25 = 1
            r0 = r23
            r1 = r22
            r2 = r24
            r3 = r25
            r4 = r21
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r23
        L_0x037b:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r24 = "make-string"
            r25 = 1
            r0 = r22
            r1 = r21
            r2 = r24
            r3 = r25
            r4 = r23
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x0390:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "%string-copy!"
            r24 = 2
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r14)
            throw r22
        L_0x03a3:
            r21 = move-exception
            gnu.mapping.WrongType r23 = new gnu.mapping.WrongType
            java.lang.String r24 = "%string-copy!"
            r25 = 2
            r0 = r23
            r1 = r21
            r2 = r24
            r3 = r25
            r4 = r22
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r23
        L_0x03b8:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "%string-copy!"
            r24 = 3
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r4 = r16
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x03cd:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "%string-copy!"
            r25 = 4
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r25
            r4 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x03e2:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "string-length"
            r24 = 1
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r9)
            throw r22
        L_0x03f5:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "%string-copy!"
            r24 = 1
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r4 = r17
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x040a:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "%string-copy!"
            r24 = 2
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r9)
            throw r22
        L_0x041d:
            r21 = move-exception
            gnu.mapping.WrongType r22 = new gnu.mapping.WrongType
            java.lang.String r23 = "%string-copy!"
            r24 = 1
            r0 = r22
            r1 = r21
            r2 = r23
            r3 = r24
            r4 = r17
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r22
        L_0x0432:
            r21 = move-exception
            java.lang.String r22 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r23 = 463(0x1cf, float:6.49E-43)
            r24 = 30
            r21.setLine(r22, r23, r24)
            throw r21
        L_0x043d:
            r22 = move-exception
            gnu.mapping.WrongType r23 = new gnu.mapping.WrongType
            java.lang.String r24 = "%string-copy!"
            r25 = 2
            r0 = r23
            r1 = r22
            r2 = r24
            r3 = r25
            r4 = r21
            r0.<init>((java.lang.ClassCastException) r1, (java.lang.String) r2, (int) r3, (java.lang.Object) r4)
            throw r23
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi13.stringUnfoldRight$V(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object[]):java.lang.Object");
    }

    static String lambda18(Object x) {
        return "";
    }

    public static Object stringForEach$V(Object proc, Object s, Object[] argsArray) {
        frame7 frame710 = new frame7();
        frame710.proc = proc;
        frame710.s = s;
        frame710.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame710.proc, string$Mnfor$Mneach);
            return call_with_values.callWithValues(frame710.lambda$Fn19, frame710.lambda$Fn20);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 468, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame7 extends ModuleBody {
        final ModuleMethod lambda$Fn19 = new ModuleMethod(this, 16, (Object) null, 0);
        final ModuleMethod lambda$Fn20 = new ModuleMethod(this, 17, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object proc;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 16 ? lambda19() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 17 ? lambda20(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 16) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 17) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda19() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnfor$Mneach, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda20(Object start, Object end) {
            Object i = start;
            while (Scheme.numLss.apply2(i, end) != Boolean.FALSE) {
                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                Object obj = this.proc;
                Object obj2 = this.s;
                try {
                    try {
                        applyToArgs.apply2(obj, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())));
                        i = AddOp.$Pl.apply2(i, srfi13.Lit1);
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string-ref", 2, i);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "string-ref", 1, obj2);
                }
            }
            return Values.empty;
        }
    }

    public static Object stringForEachIndex$V(Object proc, Object s, Object[] argsArray) {
        frame8 frame810 = new frame8();
        frame810.proc = proc;
        frame810.s = s;
        frame810.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame810.proc, string$Mnfor$Mneach$Mnindex);
            return call_with_values.callWithValues(frame810.lambda$Fn21, frame810.lambda$Fn22);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 476, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame8 extends ModuleBody {
        final ModuleMethod lambda$Fn21 = new ModuleMethod(this, 18, (Object) null, 0);
        final ModuleMethod lambda$Fn22 = new ModuleMethod(this, 19, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object proc;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 18 ? lambda21() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 19 ? lambda22(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 18) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 19) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda21() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnfor$Mneach$Mnindex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda22(Object start, Object end) {
            for (Object i = start; Scheme.numLss.apply2(i, end) != Boolean.FALSE; i = AddOp.$Pl.apply2(i, srfi13.Lit1)) {
                Scheme.applyToArgs.apply2(this.proc, i);
            }
            return Values.empty;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame9 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn23 = new ModuleMethod(this, 20, (Object) null, 0);
        final ModuleMethod lambda$Fn24 = new ModuleMethod(this, 21, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 20 ? lambda23() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 21 ? lambda24(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 20) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 21) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda23() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnevery, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda24(Object start, Object end) {
            if (characters.isChar(this.criterion)) {
                Object i = start;
                while (true) {
                    Object apply2 = Scheme.numGEq.apply2(i, end);
                    try {
                        boolean x = ((Boolean) apply2).booleanValue();
                        if (!x) {
                            Object obj = this.criterion;
                            try {
                                Char charR = (Char) obj;
                                Object obj2 = this.s;
                                try {
                                    try {
                                        boolean x2 = characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())));
                                        if (!x2) {
                                            return x2 ? Boolean.TRUE : Boolean.FALSE;
                                        }
                                        i = AddOp.$Pl.apply2(i, srfi13.Lit1);
                                    } catch (ClassCastException e) {
                                        throw new WrongType(e, "string-ref", 2, i);
                                    }
                                } catch (ClassCastException e2) {
                                    throw new WrongType(e2, "string-ref", 1, obj2);
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "char=?", 1, obj);
                            }
                        } else if (x) {
                            return Boolean.TRUE;
                        } else {
                            return Boolean.FALSE;
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "x", -2, apply2);
                    }
                }
            } else {
                try {
                    if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                        Object i2 = start;
                        while (true) {
                            Object apply22 = Scheme.numGEq.apply2(i2, end);
                            try {
                                boolean x3 = ((Boolean) apply22).booleanValue();
                                if (x3) {
                                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                                try {
                                    Object obj3 = srfi13.loc$char$Mnset$Mncontains$Qu.get();
                                    Object obj4 = this.criterion;
                                    Object obj5 = this.s;
                                    try {
                                        try {
                                            Object x4 = applyToArgs.apply3(obj3, obj4, Char.make(strings.stringRef((CharSequence) obj5, ((Number) i2).intValue())));
                                            if (x4 == Boolean.FALSE) {
                                                return x4;
                                            }
                                            i2 = AddOp.$Pl.apply2(i2, srfi13.Lit1);
                                        } catch (ClassCastException e5) {
                                            throw new WrongType(e5, "string-ref", 2, i2);
                                        }
                                    } catch (ClassCastException e6) {
                                        throw new WrongType(e6, "string-ref", 1, obj5);
                                    }
                                } catch (UnboundLocationException e7) {
                                    e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 492, 9);
                                    throw e7;
                                }
                            } catch (ClassCastException e8) {
                                throw new WrongType(e8, "x", -2, apply22);
                            }
                        }
                    } else if (misc.isProcedure(this.criterion)) {
                        Object apply23 = Scheme.numEqu.apply2(start, end);
                        try {
                            boolean x5 = ((Boolean) apply23).booleanValue();
                            if (x5) {
                                return x5 ? Boolean.TRUE : Boolean.FALSE;
                            }
                            Object i3 = start;
                            while (true) {
                                Object obj6 = this.s;
                                try {
                                    try {
                                        char c = strings.stringRef((CharSequence) obj6, ((Number) i3).intValue());
                                        Object i1 = AddOp.$Pl.apply2(i3, srfi13.Lit1);
                                        if (Scheme.numEqu.apply2(i1, end) != Boolean.FALSE) {
                                            return Scheme.applyToArgs.apply2(this.criterion, Char.make(c));
                                        }
                                        Object x6 = Scheme.applyToArgs.apply2(this.criterion, Char.make(c));
                                        if (x6 == Boolean.FALSE) {
                                            return x6;
                                        }
                                        i3 = i1;
                                    } catch (ClassCastException e9) {
                                        throw new WrongType(e9, "string-ref", 2, i3);
                                    }
                                } catch (ClassCastException e10) {
                                    throw new WrongType(e10, "string-ref", 1, obj6);
                                }
                            }
                        } catch (ClassCastException e11) {
                            throw new WrongType(e11, "x", -2, apply23);
                        }
                    } else {
                        return misc.error$V("Second param is neither char-set, char, or predicate procedure.", new Object[]{srfi13.string$Mnevery, this.criterion});
                    }
                } catch (UnboundLocationException e12) {
                    e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 489, 5);
                    throw e12;
                }
            }
        }
    }

    public static Object stringEvery$V(Object criterion, Object s, Object[] argsArray) {
        frame9 frame97 = new frame9();
        frame97.criterion = criterion;
        frame97.s = s;
        frame97.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame97.lambda$Fn23, frame97.lambda$Fn24);
    }

    /* compiled from: srfi13.scm */
    public class frame10 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn25 = new ModuleMethod(this, 22, (Object) null, 0);
        final ModuleMethod lambda$Fn26 = new ModuleMethod(this, 23, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 22 ? lambda25() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 23 ? lambda26(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 22) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda25() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnany, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda26(Object start, Object end) {
            Boolean bool;
            if (characters.isChar(this.criterion)) {
                Object i = start;
                while (true) {
                    Object apply2 = Scheme.numLss.apply2(i, end);
                    try {
                        boolean x = ((Boolean) apply2).booleanValue();
                        if (x) {
                            Object obj = this.criterion;
                            try {
                                Char charR = (Char) obj;
                                Object obj2 = this.s;
                                try {
                                    try {
                                        boolean x2 = characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())));
                                        if (x2) {
                                            bool = x2 ? Boolean.TRUE : Boolean.FALSE;
                                        } else {
                                            i = AddOp.$Pl.apply2(i, srfi13.Lit1);
                                        }
                                    } catch (ClassCastException e) {
                                        throw new WrongType(e, "string-ref", 2, i);
                                    }
                                } catch (ClassCastException e2) {
                                    throw new WrongType(e2, "string-ref", 1, obj2);
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "char=?", 1, obj);
                            }
                        } else {
                            bool = x ? Boolean.TRUE : Boolean.FALSE;
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "x", -2, apply2);
                    }
                }
                return bool;
            }
            try {
                if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                    Object i2 = start;
                    while (true) {
                        Object apply22 = Scheme.numLss.apply2(i2, end);
                        try {
                            boolean x3 = ((Boolean) apply22).booleanValue();
                            if (!x3) {
                                return x3 ? Boolean.TRUE : Boolean.FALSE;
                            }
                            ApplyToArgs applyToArgs = Scheme.applyToArgs;
                            try {
                                Object obj3 = srfi13.loc$char$Mnset$Mncontains$Qu.get();
                                Object obj4 = this.criterion;
                                Object obj5 = this.s;
                                try {
                                    try {
                                        Object x4 = applyToArgs.apply3(obj3, obj4, Char.make(strings.stringRef((CharSequence) obj5, ((Number) i2).intValue())));
                                        if (x4 != Boolean.FALSE) {
                                            return x4;
                                        }
                                        i2 = AddOp.$Pl.apply2(i2, srfi13.Lit1);
                                    } catch (ClassCastException e5) {
                                        throw new WrongType(e5, "string-ref", 2, i2);
                                    }
                                } catch (ClassCastException e6) {
                                    throw new WrongType(e6, "string-ref", 1, obj5);
                                }
                            } catch (UnboundLocationException e7) {
                                e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_BLUETOOTH_END_OF_STREAM, 9);
                                throw e7;
                            }
                        } catch (ClassCastException e8) {
                            throw new WrongType(e8, "x", -2, apply22);
                        }
                    }
                } else if (misc.isProcedure(this.criterion)) {
                    Object apply23 = Scheme.numLss.apply2(start, end);
                    try {
                        boolean x5 = ((Boolean) apply23).booleanValue();
                        if (!x5) {
                            return x5 ? Boolean.TRUE : Boolean.FALSE;
                        }
                        Object i3 = start;
                        while (true) {
                            Object obj6 = this.s;
                            try {
                                try {
                                    char c = strings.stringRef((CharSequence) obj6, ((Number) i3).intValue());
                                    Object i1 = AddOp.$Pl.apply2(i3, srfi13.Lit1);
                                    if (Scheme.numEqu.apply2(i1, end) != Boolean.FALSE) {
                                        return Scheme.applyToArgs.apply2(this.criterion, Char.make(c));
                                    }
                                    Object x6 = Scheme.applyToArgs.apply2(this.criterion, Char.make(c));
                                    if (x6 != Boolean.FALSE) {
                                        return x6;
                                    }
                                    i3 = i1;
                                } catch (ClassCastException e9) {
                                    throw new WrongType(e9, "string-ref", 2, i3);
                                }
                            } catch (ClassCastException e10) {
                                throw new WrongType(e10, "string-ref", 1, obj6);
                            }
                        }
                    } catch (ClassCastException e11) {
                        throw new WrongType(e11, "x", -2, apply23);
                    }
                } else {
                    return misc.error$V("Second param is neither char-set, char, or predicate procedure.", new Object[]{srfi13.string$Mnany, this.criterion});
                }
            } catch (UnboundLocationException e12) {
                e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_BLUETOOTH_NOT_CONNECTED_TO_DEVICE, 5);
                throw e12;
            }
        }
    }

    public static Object stringAny$V(Object criterion, Object s, Object[] argsArray) {
        frame10 frame102 = new frame10();
        frame102.criterion = criterion;
        frame102.s = s;
        frame102.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame102.lambda$Fn25, frame102.lambda$Fn26);
    }

    public static CharSequence stringTabulate(Object proc, int len) {
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, proc, string$Mntabulate);
            try {
                Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), lambda$Fn27, Integer.valueOf(len), string$Mntabulate);
                CharSequence s = strings.makeString(len);
                int i = len - 1;
                while (i >= 0) {
                    try {
                        CharSeq charSeq = (CharSeq) s;
                        Object apply2 = Scheme.applyToArgs.apply2(proc, Integer.valueOf(i));
                        try {
                            strings.stringSet$Ex(charSeq, i, ((Char) apply2).charValue());
                            i--;
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-set!", 3, apply2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-set!", 1, (Object) s);
                    }
                }
                return s;
            } catch (UnboundLocationException e3) {
                e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 535, 3);
                throw e3;
            }
        } catch (UnboundLocationException e4) {
            e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 534, 3);
            throw e4;
        }
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 218:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 280:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 281:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 282:
                if (!(obj instanceof CharSequence)) {
                    return -786431;
                }
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 283:
                if (!(obj instanceof CharSequence)) {
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

    static boolean lambda27(Object val) {
        boolean x = numbers.isInteger(val);
        if (!x) {
            return x;
        }
        boolean x2 = numbers.isExact(val);
        return x2 ? ((Boolean) Scheme.numLEq.apply2(Lit0, val)).booleanValue() : x2;
    }

    public static Object $PcStringPrefixLength(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2) {
        boolean x = false;
        Object delta = numbers.min(AddOp.$Mn.apply2(end1, start1), AddOp.$Mn.apply2(end2, start2));
        Object end12 = AddOp.$Pl.apply2(start1, delta);
        if (s1 == s2) {
            x = true;
        }
        if (x) {
            if (Scheme.numEqu.apply2(start1, start2) != Boolean.FALSE) {
                return delta;
            }
        } else if (x) {
            return delta;
        }
        Object i = start1;
        Object obj = start2;
        while (true) {
            Object apply2 = Scheme.numGEq.apply2(i, end12);
            try {
                boolean x2 = ((Boolean) apply2).booleanValue();
                if (!x2) {
                    try {
                        try {
                            try {
                                try {
                                    if (!characters.isChar$Eq(Char.make(strings.stringRef((CharSequence) s1, ((Number) i).intValue())), Char.make(strings.stringRef((CharSequence) s2, ((Number) obj).intValue())))) {
                                        break;
                                    }
                                    i = AddOp.$Pl.apply2(i, Lit1);
                                    obj = AddOp.$Pl.apply2(obj, Lit1);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, obj);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, s2);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "string-ref", 2, i);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "string-ref", 1, s1);
                    }
                } else if (x2) {
                    break;
                } else {
                    i = AddOp.$Pl.apply2(i, Lit1);
                    obj = AddOp.$Pl.apply2(obj, Lit1);
                }
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "x", -2, apply2);
            }
        }
        return AddOp.$Mn.apply2(i, start1);
    }

    public static Object $PcStringSuffixLength(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2) {
        boolean x = false;
        Object delta = numbers.min(AddOp.$Mn.apply2(end1, start1), AddOp.$Mn.apply2(end2, start2));
        Object start12 = AddOp.$Mn.apply2(end1, delta);
        if (s1 == s2) {
            x = true;
        }
        if (x) {
            if (Scheme.numEqu.apply2(end1, end2) != Boolean.FALSE) {
                return delta;
            }
        } else if (x) {
            return delta;
        }
        Object i = AddOp.$Mn.apply2(end1, Lit1);
        Object apply2 = AddOp.$Mn.apply2(end2, Lit1);
        while (true) {
            Object apply22 = Scheme.numLss.apply2(i, start12);
            try {
                boolean x2 = ((Boolean) apply22).booleanValue();
                if (!x2) {
                    try {
                        try {
                            try {
                                try {
                                    if (!characters.isChar$Eq(Char.make(strings.stringRef((CharSequence) s1, ((Number) i).intValue())), Char.make(strings.stringRef((CharSequence) s2, ((Number) apply2).intValue())))) {
                                        break;
                                    }
                                    i = AddOp.$Mn.apply2(i, Lit1);
                                    apply2 = AddOp.$Mn.apply2(apply2, Lit1);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, apply2);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, s2);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "string-ref", 2, i);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "string-ref", 1, s1);
                    }
                } else if (x2) {
                    break;
                } else {
                    i = AddOp.$Mn.apply2(i, Lit1);
                    apply2 = AddOp.$Mn.apply2(apply2, Lit1);
                }
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "x", -2, apply22);
            }
        }
        return AddOp.$Mn.apply2(AddOp.$Mn.apply2(end1, i), Lit1);
    }

    public static int $PcStringPrefixLengthCi(Object s1, int start1, int end1, Object s2, int start2, int end2) {
        boolean x;
        boolean x2;
        Object min = numbers.min(Integer.valueOf(end1 - start1), Integer.valueOf(end2 - start2));
        try {
            int delta = ((Number) min).intValue();
            int end12 = start1 + delta;
            if (s1 == s2) {
                x = true;
            } else {
                x = false;
            }
            if (x) {
                if (start1 == start2) {
                    return delta;
                }
            } else if (x) {
                return delta;
            }
            int i = start1;
            while (true) {
                if (i >= end12) {
                    x2 = true;
                } else {
                    x2 = false;
                }
                if (!x2) {
                    try {
                        try {
                            if (!unicode.isCharCi$Eq(Char.make(strings.stringRef((CharSequence) s1, i)), Char.make(strings.stringRef((CharSequence) s2, start2)))) {
                                break;
                            }
                            i++;
                            start2++;
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-ref", 1, s2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-ref", 1, s1);
                    }
                } else if (x2) {
                    break;
                } else {
                    i++;
                    start2++;
                }
            }
            return i - start1;
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "delta", -2, min);
        }
    }

    public static int $PcStringSuffixLengthCi(Object s1, int start1, int end1, Object s2, int start2, int end2) {
        boolean x;
        boolean x2;
        Object min = numbers.min(Integer.valueOf(end1 - start1), Integer.valueOf(end2 - start2));
        try {
            int delta = ((Number) min).intValue();
            int start12 = end1 - delta;
            if (s1 == s2) {
                x = true;
            } else {
                x = false;
            }
            if (x) {
                if (end1 == end2) {
                    return delta;
                }
            } else if (x) {
                return delta;
            }
            int i = end1 - 1;
            int i2 = end2 - 1;
            while (true) {
                if (i < start12) {
                    x2 = true;
                } else {
                    x2 = false;
                }
                if (!x2) {
                    try {
                        try {
                            if (!unicode.isCharCi$Eq(Char.make(strings.stringRef((CharSequence) s1, i)), Char.make(strings.stringRef((CharSequence) s2, i2)))) {
                                break;
                            }
                            i2--;
                            i--;
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-ref", 1, s2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-ref", 1, s1);
                    }
                } else if (x2) {
                    break;
                } else {
                    i2--;
                    i--;
                }
            }
            return (end1 - i) - 1;
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "delta", -2, min);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame11 extends ModuleBody {
        final ModuleMethod lambda$Fn28 = new ModuleMethod(this, 26, (Object) null, 0);
        final ModuleMethod lambda$Fn29 = new ModuleMethod(this, 27, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 26 ? lambda28() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 27 ? lambda29(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 26) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 27) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda28() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnprefix$Mnlength, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda29(Object rest, Object start1, Object end1) {
            frame12 frame12 = new frame12();
            frame12.staticLink = this;
            frame12.rest = rest;
            frame12.start1 = start1;
            frame12.end1 = end1;
            return call_with_values.callWithValues(frame12.lambda$Fn30, frame12.lambda$Fn31);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame12 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn30 = new ModuleMethod(this, 24, (Object) null, 0);
        final ModuleMethod lambda$Fn31 = new ModuleMethod(this, 25, (Object) null, 8194);
        Object rest;
        Object start1;
        frame11 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 24 ? lambda30() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 25 ? lambda31(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 24) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda30() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnprefix$Mnlength, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda31(Object start2, Object end2) {
            return srfi13.$PcStringPrefixLength(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2);
        }
    }

    public static Object stringPrefixLength$V(Object s1, Object s2, Object[] argsArray) {
        frame11 frame112 = new frame11();
        frame112.s1 = s1;
        frame112.s2 = s2;
        frame112.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame112.lambda$Fn28, frame112.lambda$Fn29);
    }

    /* compiled from: srfi13.scm */
    public class frame13 extends ModuleBody {
        final ModuleMethod lambda$Fn32 = new ModuleMethod(this, 30, (Object) null, 0);
        final ModuleMethod lambda$Fn33 = new ModuleMethod(this, 31, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 30 ? lambda32() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 31 ? lambda33(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 30) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 31) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda32() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnsuffix$Mnlength, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda33(Object rest, Object start1, Object end1) {
            frame14 frame14 = new frame14();
            frame14.staticLink = this;
            frame14.rest = rest;
            frame14.start1 = start1;
            frame14.end1 = end1;
            return call_with_values.callWithValues(frame14.lambda$Fn34, frame14.lambda$Fn35);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame14 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn34 = new ModuleMethod(this, 28, (Object) null, 0);
        final ModuleMethod lambda$Fn35 = new ModuleMethod(this, 29, (Object) null, 8194);
        Object rest;
        Object start1;
        frame13 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 28 ? lambda34() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 29 ? lambda35(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 28) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda34() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnsuffix$Mnlength, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda35(Object start2, Object end2) {
            return srfi13.$PcStringSuffixLength(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2);
        }
    }

    public static Object stringSuffixLength$V(Object s1, Object s2, Object[] argsArray) {
        frame13 frame132 = new frame13();
        frame132.s1 = s1;
        frame132.s2 = s2;
        frame132.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame132.lambda$Fn32, frame132.lambda$Fn33);
    }

    /* compiled from: srfi13.scm */
    public class frame15 extends ModuleBody {
        final ModuleMethod lambda$Fn36 = new ModuleMethod(this, 34, (Object) null, 0);
        final ModuleMethod lambda$Fn37 = new ModuleMethod(this, 35, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 34 ? lambda36() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 35 ? lambda37(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 34) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 35) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda36() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnprefix$Mnlength$Mnci, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda37(Object rest, Object start1, Object end1) {
            frame16 frame16 = new frame16();
            frame16.staticLink = this;
            frame16.rest = rest;
            frame16.start1 = start1;
            frame16.end1 = end1;
            return call_with_values.callWithValues(frame16.lambda$Fn38, frame16.lambda$Fn39);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame16 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn38 = new ModuleMethod(this, 32, (Object) null, 0);
        final ModuleMethod lambda$Fn39 = new ModuleMethod(this, 33, (Object) null, 8194);
        Object rest;
        Object start1;
        frame15 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 32 ? lambda38() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 33 ? Integer.valueOf(lambda39(obj, obj2)) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 32) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 33) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda38() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnprefix$Mnlength$Mnci, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public int lambda39(Object start2, Object end2) {
            Object obj = this.staticLink.s1;
            Object obj2 = this.start1;
            try {
                int intValue = ((Number) obj2).intValue();
                Object obj3 = this.end1;
                try {
                    try {
                        try {
                            return srfi13.$PcStringPrefixLengthCi(obj, intValue, ((Number) obj3).intValue(), this.staticLink.s2, ((Number) start2).intValue(), ((Number) end2).intValue());
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%string-prefix-length-ci", 5, end2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%string-prefix-length-ci", 4, start2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "%string-prefix-length-ci", 2, obj3);
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "%string-prefix-length-ci", 1, obj2);
            }
        }
    }

    public static Object stringPrefixLengthCi$V(Object s1, Object s2, Object[] argsArray) {
        frame15 frame152 = new frame15();
        frame152.s1 = s1;
        frame152.s2 = s2;
        frame152.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame152.lambda$Fn36, frame152.lambda$Fn37);
    }

    /* compiled from: srfi13.scm */
    public class frame17 extends ModuleBody {
        final ModuleMethod lambda$Fn40 = new ModuleMethod(this, 38, (Object) null, 0);
        final ModuleMethod lambda$Fn41 = new ModuleMethod(this, 39, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 38 ? lambda40() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 39 ? lambda41(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 38) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 39) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda40() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnsuffix$Mnlength$Mnci, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda41(Object rest, Object start1, Object end1) {
            frame18 frame18 = new frame18();
            frame18.staticLink = this;
            frame18.rest = rest;
            frame18.start1 = start1;
            frame18.end1 = end1;
            return call_with_values.callWithValues(frame18.lambda$Fn42, frame18.lambda$Fn43);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame18 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn42 = new ModuleMethod(this, 36, (Object) null, 0);
        final ModuleMethod lambda$Fn43 = new ModuleMethod(this, 37, (Object) null, 8194);
        Object rest;
        Object start1;
        frame17 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 36 ? lambda42() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 37 ? Integer.valueOf(lambda43(obj, obj2)) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 36) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 37) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda42() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnsuffix$Mnlength$Mnci, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public int lambda43(Object start2, Object end2) {
            Object obj = this.staticLink.s1;
            Object obj2 = this.start1;
            try {
                int intValue = ((Number) obj2).intValue();
                Object obj3 = this.end1;
                try {
                    try {
                        try {
                            return srfi13.$PcStringSuffixLengthCi(obj, intValue, ((Number) obj3).intValue(), this.staticLink.s2, ((Number) start2).intValue(), ((Number) end2).intValue());
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%string-suffix-length-ci", 5, end2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%string-suffix-length-ci", 4, start2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "%string-suffix-length-ci", 2, obj3);
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "%string-suffix-length-ci", 1, obj2);
            }
        }
    }

    public static Object stringSuffixLengthCi$V(Object s1, Object s2, Object[] argsArray) {
        frame17 frame172 = new frame17();
        frame172.s1 = s1;
        frame172.s2 = s2;
        frame172.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame172.lambda$Fn40, frame172.lambda$Fn41);
    }

    /* compiled from: srfi13.scm */
    public class frame19 extends ModuleBody {
        final ModuleMethod lambda$Fn44 = new ModuleMethod(this, 42, (Object) null, 0);
        final ModuleMethod lambda$Fn45 = new ModuleMethod(this, 43, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 42 ? lambda44() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 43 ? lambda45(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 42) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 43) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda44() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnprefix$Qu, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda45(Object rest, Object start1, Object end1) {
            frame20 frame20 = new frame20();
            frame20.staticLink = this;
            frame20.rest = rest;
            frame20.start1 = start1;
            frame20.end1 = end1;
            return call_with_values.callWithValues(frame20.lambda$Fn46, frame20.lambda$Fn47);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame20 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn46 = new ModuleMethod(this, 40, (Object) null, 0);
        final ModuleMethod lambda$Fn47 = new ModuleMethod(this, 41, (Object) null, 8194);
        Object rest;
        Object start1;
        frame19 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 40 ? lambda46() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 41 ? lambda47(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 40) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 41) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda46() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnprefix$Qu, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda47(Object start2, Object end2) {
            return srfi13.$PcStringPrefix$Qu(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2);
        }
    }

    public static Object isStringPrefix$V(Object s1, Object s2, Object[] argsArray) {
        frame19 frame192 = new frame19();
        frame192.s1 = s1;
        frame192.s2 = s2;
        frame192.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame192.lambda$Fn44, frame192.lambda$Fn45);
    }

    /* compiled from: srfi13.scm */
    public class frame21 extends ModuleBody {
        final ModuleMethod lambda$Fn48 = new ModuleMethod(this, 46, (Object) null, 0);
        final ModuleMethod lambda$Fn49 = new ModuleMethod(this, 47, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 46 ? lambda48() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 47 ? lambda49(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 46) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 47) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda48() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnsuffix$Qu, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda49(Object rest, Object start1, Object end1) {
            frame22 frame22 = new frame22();
            frame22.staticLink = this;
            frame22.rest = rest;
            frame22.start1 = start1;
            frame22.end1 = end1;
            return call_with_values.callWithValues(frame22.lambda$Fn50, frame22.lambda$Fn51);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame22 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn50 = new ModuleMethod(this, 44, (Object) null, 0);
        final ModuleMethod lambda$Fn51 = new ModuleMethod(this, 45, (Object) null, 8194);
        Object rest;
        Object start1;
        frame21 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 44 ? lambda50() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 45 ? lambda51(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 44) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda50() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnsuffix$Qu, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda51(Object start2, Object end2) {
            return srfi13.$PcStringSuffix$Qu(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2);
        }
    }

    public static Object isStringSuffix$V(Object s1, Object s2, Object[] argsArray) {
        frame21 frame212 = new frame21();
        frame212.s1 = s1;
        frame212.s2 = s2;
        frame212.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame212.lambda$Fn48, frame212.lambda$Fn49);
    }

    /* compiled from: srfi13.scm */
    public class frame23 extends ModuleBody {
        final ModuleMethod lambda$Fn52 = new ModuleMethod(this, 50, (Object) null, 0);
        final ModuleMethod lambda$Fn53 = new ModuleMethod(this, 51, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 50 ? lambda52() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 51 ? lambda53(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 50) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 51) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda52() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnprefix$Mnci$Qu, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda53(Object rest, Object start1, Object end1) {
            frame24 frame24 = new frame24();
            frame24.staticLink = this;
            frame24.rest = rest;
            frame24.start1 = start1;
            frame24.end1 = end1;
            return call_with_values.callWithValues(frame24.lambda$Fn54, frame24.lambda$Fn55);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame24 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn54 = new ModuleMethod(this, 48, (Object) null, 0);
        final ModuleMethod lambda$Fn55 = new ModuleMethod(this, 49, (Object) null, 8194);
        Object rest;
        Object start1;
        frame23 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 48 ? lambda54() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 49 ? lambda55(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 48) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda54() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnprefix$Mnci$Qu, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda55(Object start2, Object end2) {
            return srfi13.$PcStringPrefixCi$Qu(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2);
        }
    }

    public static Object isStringPrefixCi$V(Object s1, Object s2, Object[] argsArray) {
        frame23 frame232 = new frame23();
        frame232.s1 = s1;
        frame232.s2 = s2;
        frame232.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame232.lambda$Fn52, frame232.lambda$Fn53);
    }

    /* compiled from: srfi13.scm */
    public class frame25 extends ModuleBody {
        final ModuleMethod lambda$Fn56 = new ModuleMethod(this, 54, (Object) null, 0);
        final ModuleMethod lambda$Fn57 = new ModuleMethod(this, 55, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 54 ? lambda56() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 55 ? lambda57(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 54) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 55) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda56() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnsuffix$Mnci$Qu, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda57(Object rest, Object start1, Object end1) {
            frame26 frame26 = new frame26();
            frame26.staticLink = this;
            frame26.rest = rest;
            frame26.start1 = start1;
            frame26.end1 = end1;
            return call_with_values.callWithValues(frame26.lambda$Fn58, frame26.lambda$Fn59);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame26 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn58 = new ModuleMethod(this, 52, (Object) null, 0);
        final ModuleMethod lambda$Fn59 = new ModuleMethod(this, 53, (Object) null, 8194);
        Object rest;
        Object start1;
        frame25 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 52 ? lambda58() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 53 ? lambda59(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 52) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 53) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda58() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnsuffix$Mnci$Qu, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda59(Object start2, Object end2) {
            return srfi13.$PcStringSuffixCi$Qu(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2);
        }
    }

    public static Object isStringSuffixCi$V(Object s1, Object s2, Object[] argsArray) {
        frame25 frame252 = new frame25();
        frame252.s1 = s1;
        frame252.s2 = s2;
        frame252.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame252.lambda$Fn56, frame252.lambda$Fn57);
    }

    public static Object $PcStringPrefix$Qu(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2) {
        Object len1 = AddOp.$Mn.apply2(end1, start1);
        Object apply2 = Scheme.numLEq.apply2(len1, AddOp.$Mn.apply2(end2, start2));
        try {
            boolean x = ((Boolean) apply2).booleanValue();
            if (x) {
                return Scheme.numEqu.apply2($PcStringPrefixLength(s1, start1, end1, s2, start2, end2), len1);
            }
            return x ? Boolean.TRUE : Boolean.FALSE;
        } catch (ClassCastException e) {
            throw new WrongType(e, "x", -2, apply2);
        }
    }

    public static Object $PcStringSuffix$Qu(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2) {
        Object len1 = AddOp.$Mn.apply2(end1, start1);
        Object apply2 = Scheme.numLEq.apply2(len1, AddOp.$Mn.apply2(end2, start2));
        try {
            boolean x = ((Boolean) apply2).booleanValue();
            if (x) {
                return Scheme.numEqu.apply2(len1, $PcStringSuffixLength(s1, start1, end1, s2, start2, end2));
            }
            return x ? Boolean.TRUE : Boolean.FALSE;
        } catch (ClassCastException e) {
            throw new WrongType(e, "x", -2, apply2);
        }
    }

    public static Object $PcStringPrefixCi$Qu(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2) {
        Object len1 = AddOp.$Mn.apply2(end1, start1);
        Object apply2 = Scheme.numLEq.apply2(len1, AddOp.$Mn.apply2(end2, start2));
        try {
            boolean x = ((Boolean) apply2).booleanValue();
            if (!x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            try {
                try {
                    try {
                        try {
                            return Scheme.numEqu.apply2(len1, Integer.valueOf($PcStringPrefixLengthCi(s1, ((Number) start1).intValue(), ((Number) end1).intValue(), s2, ((Number) start2).intValue(), ((Number) end2).intValue())));
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%string-prefix-length-ci", 5, end2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%string-prefix-length-ci", 4, start2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "%string-prefix-length-ci", 2, end1);
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "%string-prefix-length-ci", 1, start1);
            }
        } catch (ClassCastException e5) {
            throw new WrongType(e5, "x", -2, apply2);
        }
    }

    public static Object $PcStringSuffixCi$Qu(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2) {
        Object len1 = AddOp.$Mn.apply2(end1, start1);
        Object apply2 = Scheme.numLEq.apply2(len1, AddOp.$Mn.apply2(end2, start2));
        try {
            boolean x = ((Boolean) apply2).booleanValue();
            if (!x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            try {
                try {
                    try {
                        try {
                            return Scheme.numEqu.apply2(len1, Integer.valueOf($PcStringSuffixLengthCi(s1, ((Number) start1).intValue(), ((Number) end1).intValue(), s2, ((Number) start2).intValue(), ((Number) end2).intValue())));
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%string-suffix-length-ci", 5, end2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%string-suffix-length-ci", 4, start2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "%string-suffix-length-ci", 2, end1);
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "%string-suffix-length-ci", 1, start1);
            }
        } catch (ClassCastException e5) {
            throw new WrongType(e5, "x", -2, apply2);
        }
    }

    public static Object $PcStringCompare(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2, Object proc$Ls, Object proc$Eq, Object proc$Gr) {
        Object size1 = AddOp.$Mn.apply2(end1, start1);
        Object size2 = AddOp.$Mn.apply2(end2, start2);
        Object match = $PcStringPrefixLength(s1, start1, end1, s2, start2, end2);
        if (Scheme.numEqu.apply2(match, size1) != Boolean.FALSE) {
            ApplyToArgs applyToArgs = Scheme.applyToArgs;
            if (Scheme.numEqu.apply2(match, size2) == Boolean.FALSE) {
                proc$Eq = proc$Ls;
            }
            return applyToArgs.apply2(proc$Eq, end1);
        }
        ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
        if (Scheme.numEqu.apply2(match, size2) == Boolean.FALSE) {
            try {
                CharSequence charSequence = (CharSequence) s1;
                Object apply2 = AddOp.$Pl.apply2(start1, match);
                try {
                    Char make = Char.make(strings.stringRef(charSequence, ((Number) apply2).intValue()));
                    try {
                        CharSequence charSequence2 = (CharSequence) s2;
                        Object apply22 = AddOp.$Pl.apply2(start2, match);
                        try {
                            if (characters.isChar$Ls(make, Char.make(strings.stringRef(charSequence2, ((Number) apply22).intValue())))) {
                                proc$Gr = proc$Ls;
                            }
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-ref", 2, apply22);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-ref", 1, s2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "string-ref", 2, apply2);
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "string-ref", 1, s1);
            }
        }
        return applyToArgs2.apply2(proc$Gr, AddOp.$Pl.apply2(match, start1));
    }

    public static Object $PcStringCompareCi(Object s1, Object start1, Object end1, Object s2, Object start2, Object end2, Object proc$Ls, Object proc$Eq, Object proc$Gr) {
        Object size1 = AddOp.$Mn.apply2(end1, start1);
        Object size2 = AddOp.$Mn.apply2(end2, start2);
        try {
            try {
                try {
                    try {
                        int match = $PcStringPrefixLengthCi(s1, ((Number) start1).intValue(), ((Number) end1).intValue(), s2, ((Number) start2).intValue(), ((Number) end2).intValue());
                        if (Scheme.numEqu.apply2(Integer.valueOf(match), size1) != Boolean.FALSE) {
                            ApplyToArgs applyToArgs = Scheme.applyToArgs;
                            if (Scheme.numEqu.apply2(Integer.valueOf(match), size2) == Boolean.FALSE) {
                                proc$Eq = proc$Ls;
                            }
                            return applyToArgs.apply2(proc$Eq, end1);
                        }
                        ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                        if (Scheme.numEqu.apply2(Integer.valueOf(match), size2) == Boolean.FALSE) {
                            try {
                                CharSequence charSequence = (CharSequence) s1;
                                Object apply2 = AddOp.$Pl.apply2(start1, Integer.valueOf(match));
                                try {
                                    Char make = Char.make(strings.stringRef(charSequence, ((Number) apply2).intValue()));
                                    try {
                                        CharSequence charSequence2 = (CharSequence) s2;
                                        Object apply22 = AddOp.$Pl.apply2(start2, Integer.valueOf(match));
                                        try {
                                            if (unicode.isCharCi$Ls(make, Char.make(strings.stringRef(charSequence2, ((Number) apply22).intValue())))) {
                                                proc$Gr = proc$Ls;
                                            }
                                        } catch (ClassCastException e) {
                                            throw new WrongType(e, "string-ref", 2, apply22);
                                        }
                                    } catch (ClassCastException e2) {
                                        throw new WrongType(e2, "string-ref", 1, s2);
                                    }
                                } catch (ClassCastException e3) {
                                    throw new WrongType(e3, "string-ref", 2, apply2);
                                }
                            } catch (ClassCastException e4) {
                                throw new WrongType(e4, "string-ref", 1, s1);
                            }
                        }
                        return applyToArgs2.apply2(proc$Gr, AddOp.$Pl.apply2(start1, Integer.valueOf(match)));
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "%string-prefix-length-ci", 5, end2);
                    }
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "%string-prefix-length-ci", 4, start2);
                }
            } catch (ClassCastException e7) {
                throw new WrongType(e7, "%string-prefix-length-ci", 2, end1);
            }
        } catch (ClassCastException e8) {
            throw new WrongType(e8, "%string-prefix-length-ci", 1, start1);
        }
    }

    public static Object stringCompare$V(Object s1, Object s2, Object proc$Ls, Object proc$Eq, Object proc$Gr, Object[] argsArray) {
        frame27 frame272 = new frame27();
        frame272.s1 = s1;
        frame272.s2 = s2;
        frame272.proc$Ls = proc$Ls;
        frame272.proc$Eq = proc$Eq;
        frame272.proc$Gr = proc$Gr;
        frame272.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame272.proc$Ls, string$Mncompare);
            try {
                Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame272.proc$Eq, string$Mncompare);
                try {
                    Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame272.proc$Gr, string$Mncompare);
                    return call_with_values.callWithValues(frame272.lambda$Fn60, frame272.lambda$Fn61);
                } catch (UnboundLocationException e) {
                    e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 728, 3);
                    throw e;
                }
            } catch (UnboundLocationException e2) {
                e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 727, 3);
                throw e2;
            }
        } catch (UnboundLocationException e3) {
            e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 726, 3);
            throw e3;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame27 extends ModuleBody {
        final ModuleMethod lambda$Fn60 = new ModuleMethod(this, 58, (Object) null, 0);
        final ModuleMethod lambda$Fn61 = new ModuleMethod(this, 59, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object proc$Eq;
        Object proc$Gr;
        Object proc$Ls;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 58 ? lambda60() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 59 ? lambda61(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 58) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 59) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda60() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mncompare, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda61(Object rest, Object start1, Object end1) {
            frame28 frame28 = new frame28();
            frame28.staticLink = this;
            frame28.rest = rest;
            frame28.start1 = start1;
            frame28.end1 = end1;
            return call_with_values.callWithValues(frame28.lambda$Fn62, frame28.lambda$Fn63);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame28 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn62 = new ModuleMethod(this, 56, (Object) null, 0);
        final ModuleMethod lambda$Fn63 = new ModuleMethod(this, 57, (Object) null, 8194);
        Object rest;
        Object start1;
        frame27 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 56 ? lambda62() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 57 ? lambda63(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 56) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 57) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda62() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mncompare, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda63(Object start2, Object end2) {
            return srfi13.$PcStringCompare(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, this.staticLink.proc$Ls, this.staticLink.proc$Eq, this.staticLink.proc$Gr);
        }
    }

    public static Object stringCompareCi$V(Object s1, Object s2, Object proc$Ls, Object proc$Eq, Object proc$Gr, Object[] argsArray) {
        frame29 frame292 = new frame29();
        frame292.s1 = s1;
        frame292.s2 = s2;
        frame292.proc$Ls = proc$Ls;
        frame292.proc$Eq = proc$Eq;
        frame292.proc$Gr = proc$Gr;
        frame292.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame292.proc$Ls, string$Mncompare$Mnci);
            try {
                Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame292.proc$Eq, string$Mncompare$Mnci);
                try {
                    Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), misc.procedure$Qu, frame292.proc$Gr, string$Mncompare$Mnci);
                    return call_with_values.callWithValues(frame292.lambda$Fn64, frame292.lambda$Fn65);
                } catch (UnboundLocationException e) {
                    e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 736, 3);
                    throw e;
                }
            } catch (UnboundLocationException e2) {
                e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 735, 3);
                throw e2;
            }
        } catch (UnboundLocationException e3) {
            e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 734, 3);
            throw e3;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame29 extends ModuleBody {
        final ModuleMethod lambda$Fn64 = new ModuleMethod(this, 62, (Object) null, 0);
        final ModuleMethod lambda$Fn65 = new ModuleMethod(this, 63, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object proc$Eq;
        Object proc$Gr;
        Object proc$Ls;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 62 ? lambda64() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 63 ? lambda65(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 62) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 63) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda64() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mncompare$Mnci, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda65(Object rest, Object start1, Object end1) {
            frame30 frame30 = new frame30();
            frame30.staticLink = this;
            frame30.rest = rest;
            frame30.start1 = start1;
            frame30.end1 = end1;
            return call_with_values.callWithValues(frame30.lambda$Fn66, frame30.lambda$Fn67);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame30 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn66 = new ModuleMethod(this, 60, (Object) null, 0);
        final ModuleMethod lambda$Fn67 = new ModuleMethod(this, 61, (Object) null, 8194);
        Object rest;
        Object start1;
        frame29 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 60 ? lambda66() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 61 ? lambda67(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 60) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 61) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda66() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mncompare$Mnci, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda67(Object start2, Object end2) {
            return srfi13.$PcStringCompareCi(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, this.staticLink.proc$Ls, this.staticLink.proc$Eq, this.staticLink.proc$Gr);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame31 extends ModuleBody {
        final ModuleMethod lambda$Fn68 = new ModuleMethod(this, 66, (Object) null, 0);
        final ModuleMethod lambda$Fn69 = new ModuleMethod(this, 67, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 66 ? lambda68() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 67 ? lambda69(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 66) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 67) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda68() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Eq, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda69(Object rest, Object start1, Object end1) {
            frame32 frame32 = new frame32();
            frame32.staticLink = this;
            frame32.rest = rest;
            frame32.start1 = start1;
            frame32.end1 = end1;
            return call_with_values.callWithValues(frame32.lambda$Fn70, frame32.lambda$Fn71);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame32 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn70 = new ModuleMethod(this, 64, (Object) null, 0);
        final ModuleMethod lambda$Fn71 = new ModuleMethod(this, 65, (Object) null, 8194);
        Object rest;
        Object start1;
        frame31 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 64 ? lambda70() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 65 ? lambda71(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 64) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda70() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Eq, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda71(Object start2, Object end2) {
            Object apply2 = Scheme.numEqu.apply2(AddOp.$Mn.apply2(this.end1, this.start1), AddOp.$Mn.apply2(end2, start2));
            try {
                boolean x = ((Boolean) apply2).booleanValue();
                if (!x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                boolean x2 = this.staticLink.s1 == this.staticLink.s2;
                if (x2) {
                    Object apply22 = Scheme.numEqu.apply2(this.start1, start2);
                    try {
                        x2 = ((Boolean) apply22).booleanValue();
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "x", -2, apply22);
                    }
                }
                if (x2) {
                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                }
                return srfi13.$PcStringCompare(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, srfi13.lambda$Fn72, misc.values, srfi13.lambda$Fn73);
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "x", -2, apply2);
            }
        }

        static Boolean lambda72(Object i) {
            return Boolean.FALSE;
        }

        static Boolean lambda73(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object string$Eq$V(Object s1, Object s2, Object[] argsArray) {
        frame31 frame312 = new frame31();
        frame312.s1 = s1;
        frame312.s2 = s2;
        frame312.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame312.lambda$Fn68, frame312.lambda$Fn69);
    }

    /* compiled from: srfi13.scm */
    public class frame33 extends ModuleBody {
        final ModuleMethod lambda$Fn74 = new ModuleMethod(this, 70, (Object) null, 0);
        final ModuleMethod lambda$Fn75 = new ModuleMethod(this, 71, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 70 ? lambda74() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 71 ? lambda75(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 70) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 71) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda74() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Ls$Gr, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda75(Object rest, Object start1, Object end1) {
            frame34 frame34 = new frame34();
            frame34.staticLink = this;
            frame34.rest = rest;
            frame34.start1 = start1;
            frame34.end1 = end1;
            return call_with_values.callWithValues(frame34.lambda$Fn76, frame34.lambda$Fn77);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame34 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn76 = new ModuleMethod(this, 68, (Object) null, 0);
        final ModuleMethod lambda$Fn77 = new ModuleMethod(this, 69, (Object) null, 8194);
        Object rest;
        Object start1;
        frame33 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 68 ? lambda76() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 69 ? lambda77(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 68) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda76() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Ls$Gr, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda77(Object start2, Object end2) {
            int i;
            boolean z = true;
            Object apply2 = Scheme.numEqu.apply2(AddOp.$Mn.apply2(this.end1, this.start1), AddOp.$Mn.apply2(end2, start2));
            try {
                if (apply2 != Boolean.FALSE) {
                    i = 1;
                } else {
                    i = 0;
                }
                boolean x = (i + 1) & true;
                if (x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                boolean x2 = this.staticLink.s1 == this.staticLink.s2;
                if (x2) {
                    Object apply22 = Scheme.numEqu.apply2(this.start1, start2);
                    try {
                        if (apply22 == Boolean.FALSE) {
                            z = false;
                        }
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "x", -2, apply22);
                    }
                } else {
                    z = x2;
                }
                boolean x3 = (z + true) & true;
                if (!x3) {
                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                }
                return srfi13.$PcStringCompare(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, misc.values, srfi13.lambda$Fn78, misc.values);
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "x", -2, apply2);
            }
        }

        static Boolean lambda78(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object string$Ls$Gr$V(Object s1, Object s2, Object[] argsArray) {
        frame33 frame332 = new frame33();
        frame332.s1 = s1;
        frame332.s2 = s2;
        frame332.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame332.lambda$Fn74, frame332.lambda$Fn75);
    }

    /* compiled from: srfi13.scm */
    public class frame35 extends ModuleBody {
        final ModuleMethod lambda$Fn79 = new ModuleMethod(this, 74, (Object) null, 0);
        final ModuleMethod lambda$Fn80 = new ModuleMethod(this, 75, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 74 ? lambda79() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 75 ? lambda80(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 74) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 75) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda79() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Ls, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda80(Object rest, Object start1, Object end1) {
            frame36 frame36 = new frame36();
            frame36.staticLink = this;
            frame36.rest = rest;
            frame36.start1 = start1;
            frame36.end1 = end1;
            return call_with_values.callWithValues(frame36.lambda$Fn81, frame36.lambda$Fn82);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame36 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn81 = new ModuleMethod(this, 72, (Object) null, 0);
        final ModuleMethod lambda$Fn82 = new ModuleMethod(this, 73, (Object) null, 8194);
        Object rest;
        Object start1;
        frame35 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 72 ? lambda81() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 73 ? lambda82(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 72) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
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

        /* access modifiers changed from: package-private */
        public Object lambda81() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Ls, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda82(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numLss.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompare(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, misc.values, srfi13.lambda$Fn83, srfi13.lambda$Fn84);
        }

        static Boolean lambda83(Object i) {
            return Boolean.FALSE;
        }

        static Boolean lambda84(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object string$Ls$V(Object s1, Object s2, Object[] argsArray) {
        frame35 frame352 = new frame35();
        frame352.s1 = s1;
        frame352.s2 = s2;
        frame352.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame352.lambda$Fn79, frame352.lambda$Fn80);
    }

    /* compiled from: srfi13.scm */
    public class frame37 extends ModuleBody {
        final ModuleMethod lambda$Fn85 = new ModuleMethod(this, 78, (Object) null, 0);
        final ModuleMethod lambda$Fn86 = new ModuleMethod(this, 79, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 78 ? lambda85() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 79 ? lambda86(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 78) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 79) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda85() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Gr, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda86(Object rest, Object start1, Object end1) {
            frame38 frame38 = new frame38();
            frame38.staticLink = this;
            frame38.rest = rest;
            frame38.start1 = start1;
            frame38.end1 = end1;
            return call_with_values.callWithValues(frame38.lambda$Fn87, frame38.lambda$Fn88);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame38 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn87 = new ModuleMethod(this, 76, (Object) null, 0);
        final ModuleMethod lambda$Fn88 = new ModuleMethod(this, 77, (Object) null, 8194);
        Object rest;
        Object start1;
        frame37 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 76 ? lambda87() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 77 ? lambda88(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 76) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 77) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda87() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Gr, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda88(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numGrt.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompare(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, srfi13.lambda$Fn89, srfi13.lambda$Fn90, misc.values);
        }

        static Boolean lambda89(Object i) {
            return Boolean.FALSE;
        }

        static Boolean lambda90(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object string$Gr$V(Object s1, Object s2, Object[] argsArray) {
        frame37 frame372 = new frame37();
        frame372.s1 = s1;
        frame372.s2 = s2;
        frame372.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame372.lambda$Fn85, frame372.lambda$Fn86);
    }

    /* compiled from: srfi13.scm */
    public class frame39 extends ModuleBody {
        final ModuleMethod lambda$Fn91 = new ModuleMethod(this, 82, (Object) null, 0);
        final ModuleMethod lambda$Fn92 = new ModuleMethod(this, 83, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 82 ? lambda91() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 83 ? lambda92(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 82) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 83) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda91() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Ls$Eq, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda92(Object rest, Object start1, Object end1) {
            frame40 frame40 = new frame40();
            frame40.staticLink = this;
            frame40.rest = rest;
            frame40.start1 = start1;
            frame40.end1 = end1;
            return call_with_values.callWithValues(frame40.lambda$Fn93, frame40.lambda$Fn94);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame40 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn93 = new ModuleMethod(this, 80, (Object) null, 0);
        final ModuleMethod lambda$Fn94 = new ModuleMethod(this, 81, (Object) null, 8194);
        Object rest;
        Object start1;
        frame39 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 80 ? lambda93() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 81 ? lambda94(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 80) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 81) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda93() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Ls$Eq, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda94(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numLEq.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompare(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, misc.values, misc.values, srfi13.lambda$Fn95);
        }

        static Boolean lambda95(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object string$Ls$Eq$V(Object s1, Object s2, Object[] argsArray) {
        frame39 frame392 = new frame39();
        frame392.s1 = s1;
        frame392.s2 = s2;
        frame392.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame392.lambda$Fn91, frame392.lambda$Fn92);
    }

    /* compiled from: srfi13.scm */
    public class frame41 extends ModuleBody {
        final ModuleMethod lambda$Fn96 = new ModuleMethod(this, 86, (Object) null, 0);
        final ModuleMethod lambda$Fn97 = new ModuleMethod(this, 87, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 86 ? lambda96() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 87 ? lambda97(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 86) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 87) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda96() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Gr$Eq, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda97(Object rest, Object start1, Object end1) {
            frame42 frame42 = new frame42();
            frame42.staticLink = this;
            frame42.rest = rest;
            frame42.start1 = start1;
            frame42.end1 = end1;
            return call_with_values.callWithValues(frame42.lambda$Fn98, frame42.lambda$Fn99);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame42 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn98 = new ModuleMethod(this, 84, (Object) null, 0);
        final ModuleMethod lambda$Fn99 = new ModuleMethod(this, 85, (Object) null, 8194);
        Object rest;
        Object start1;
        frame41 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 84 ? lambda98() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 85 ? lambda99(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 84) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 85) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda98() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Gr$Eq, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda99(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numGEq.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompare(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, srfi13.lambda$Fn100, misc.values, misc.values);
        }

        static Boolean lambda100(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object string$Gr$Eq$V(Object s1, Object s2, Object[] argsArray) {
        frame41 frame412 = new frame41();
        frame412.s1 = s1;
        frame412.s2 = s2;
        frame412.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame412.lambda$Fn96, frame412.lambda$Fn97);
    }

    /* compiled from: srfi13.scm */
    public class frame43 extends ModuleBody {
        final ModuleMethod lambda$Fn101 = new ModuleMethod(this, 90, (Object) null, 0);
        final ModuleMethod lambda$Fn102 = new ModuleMethod(this, 91, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 90 ? lambda101() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 91 ? lambda102(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 90) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 91) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda101() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnci$Eq, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda102(Object rest, Object start1, Object end1) {
            frame44 frame44 = new frame44();
            frame44.staticLink = this;
            frame44.rest = rest;
            frame44.start1 = start1;
            frame44.end1 = end1;
            return call_with_values.callWithValues(frame44.lambda$Fn103, frame44.lambda$Fn104);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame44 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn103 = new ModuleMethod(this, 88, (Object) null, 0);
        final ModuleMethod lambda$Fn104 = new ModuleMethod(this, 89, (Object) null, 8194);
        Object rest;
        Object start1;
        frame43 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 88 ? lambda103() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 89 ? lambda104(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 88) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 89) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda103() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnci$Eq, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda104(Object start2, Object end2) {
            Object apply2 = Scheme.numEqu.apply2(AddOp.$Mn.apply2(this.end1, this.start1), AddOp.$Mn.apply2(end2, start2));
            try {
                boolean x = ((Boolean) apply2).booleanValue();
                if (!x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                boolean x2 = this.staticLink.s1 == this.staticLink.s2;
                if (x2) {
                    Object apply22 = Scheme.numEqu.apply2(this.start1, start2);
                    try {
                        x2 = ((Boolean) apply22).booleanValue();
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "x", -2, apply22);
                    }
                }
                if (x2) {
                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                }
                return srfi13.$PcStringCompareCi(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, srfi13.lambda$Fn105, misc.values, srfi13.lambda$Fn106);
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "x", -2, apply2);
            }
        }

        static Boolean lambda105(Object i) {
            return Boolean.FALSE;
        }

        static Boolean lambda106(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object stringCi$Eq$V(Object s1, Object s2, Object[] argsArray) {
        frame43 frame432 = new frame43();
        frame432.s1 = s1;
        frame432.s2 = s2;
        frame432.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame432.lambda$Fn101, frame432.lambda$Fn102);
    }

    /* compiled from: srfi13.scm */
    public class frame45 extends ModuleBody {
        final ModuleMethod lambda$Fn107 = new ModuleMethod(this, 94, (Object) null, 0);
        final ModuleMethod lambda$Fn108 = new ModuleMethod(this, 95, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 94 ? lambda107() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 95 ? lambda108(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 94) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 95) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda107() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnci$Ls$Gr, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda108(Object rest, Object start1, Object end1) {
            frame46 frame46 = new frame46();
            frame46.staticLink = this;
            frame46.rest = rest;
            frame46.start1 = start1;
            frame46.end1 = end1;
            return call_with_values.callWithValues(frame46.lambda$Fn109, frame46.lambda$Fn110);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame46 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn109 = new ModuleMethod(this, 92, (Object) null, 0);
        final ModuleMethod lambda$Fn110 = new ModuleMethod(this, 93, (Object) null, 8194);
        Object rest;
        Object start1;
        frame45 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 92 ? lambda109() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 93 ? lambda110(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 92) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 93) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda109() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnci$Ls$Gr, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda110(Object start2, Object end2) {
            int i;
            boolean z = true;
            Object apply2 = Scheme.numEqu.apply2(AddOp.$Mn.apply2(this.end1, this.start1), AddOp.$Mn.apply2(end2, start2));
            try {
                if (apply2 != Boolean.FALSE) {
                    i = 1;
                } else {
                    i = 0;
                }
                boolean x = (i + 1) & true;
                if (x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                boolean x2 = this.staticLink.s1 == this.staticLink.s2;
                if (x2) {
                    Object apply22 = Scheme.numEqu.apply2(this.start1, start2);
                    try {
                        if (apply22 == Boolean.FALSE) {
                            z = false;
                        }
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "x", -2, apply22);
                    }
                } else {
                    z = x2;
                }
                boolean x3 = (z + true) & true;
                if (!x3) {
                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                }
                return srfi13.$PcStringCompareCi(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, misc.values, srfi13.lambda$Fn111, misc.values);
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "x", -2, apply2);
            }
        }

        static Boolean lambda111(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object stringCi$Ls$Gr$V(Object s1, Object s2, Object[] argsArray) {
        frame45 frame452 = new frame45();
        frame452.s1 = s1;
        frame452.s2 = s2;
        frame452.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame452.lambda$Fn107, frame452.lambda$Fn108);
    }

    /* compiled from: srfi13.scm */
    public class frame47 extends ModuleBody {
        final ModuleMethod lambda$Fn112 = new ModuleMethod(this, 98, (Object) null, 0);
        final ModuleMethod lambda$Fn113 = new ModuleMethod(this, 99, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 98 ? lambda112() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 99 ? lambda113(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 98) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 99) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda112() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnci$Ls, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda113(Object rest, Object start1, Object end1) {
            frame48 frame48 = new frame48();
            frame48.staticLink = this;
            frame48.rest = rest;
            frame48.start1 = start1;
            frame48.end1 = end1;
            return call_with_values.callWithValues(frame48.lambda$Fn114, frame48.lambda$Fn115);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame48 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn114 = new ModuleMethod(this, 96, (Object) null, 0);
        final ModuleMethod lambda$Fn115 = new ModuleMethod(this, 97, (Object) null, 8194);
        Object rest;
        Object start1;
        frame47 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 96 ? lambda114() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 97 ? lambda115(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 96) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 97) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda114() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnci$Ls, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda115(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numLss.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompareCi(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, misc.values, srfi13.lambda$Fn116, srfi13.lambda$Fn117);
        }

        static Boolean lambda116(Object i) {
            return Boolean.FALSE;
        }

        static Boolean lambda117(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object stringCi$Ls$V(Object s1, Object s2, Object[] argsArray) {
        frame47 frame472 = new frame47();
        frame472.s1 = s1;
        frame472.s2 = s2;
        frame472.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame472.lambda$Fn112, frame472.lambda$Fn113);
    }

    /* compiled from: srfi13.scm */
    public class frame49 extends ModuleBody {
        final ModuleMethod lambda$Fn118 = new ModuleMethod(this, 102, (Object) null, 0);
        final ModuleMethod lambda$Fn119 = new ModuleMethod(this, 103, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 102 ? lambda118() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 103 ? lambda119(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 102) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 103) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda118() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnci$Gr, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda119(Object rest, Object start1, Object end1) {
            frame50 frame50 = new frame50();
            frame50.staticLink = this;
            frame50.rest = rest;
            frame50.start1 = start1;
            frame50.end1 = end1;
            return call_with_values.callWithValues(frame50.lambda$Fn120, frame50.lambda$Fn121);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame50 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn120 = new ModuleMethod(this, 100, (Object) null, 0);
        final ModuleMethod lambda$Fn121 = new ModuleMethod(this, 101, (Object) null, 8194);
        Object rest;
        Object start1;
        frame49 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 100 ? lambda120() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 101 ? lambda121(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 100) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 101) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda120() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnci$Gr, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda121(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numGrt.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompareCi(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, srfi13.lambda$Fn122, srfi13.lambda$Fn123, misc.values);
        }

        static Boolean lambda122(Object i) {
            return Boolean.FALSE;
        }

        static Boolean lambda123(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object stringCi$Gr$V(Object s1, Object s2, Object[] argsArray) {
        frame49 frame492 = new frame49();
        frame492.s1 = s1;
        frame492.s2 = s2;
        frame492.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame492.lambda$Fn118, frame492.lambda$Fn119);
    }

    /* compiled from: srfi13.scm */
    public class frame51 extends ModuleBody {
        final ModuleMethod lambda$Fn124 = new ModuleMethod(this, 106, (Object) null, 0);
        final ModuleMethod lambda$Fn125 = new ModuleMethod(this, 107, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 106 ? lambda124() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 107 ? lambda125(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 106) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 107) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda124() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnci$Ls$Eq, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda125(Object rest, Object start1, Object end1) {
            frame52 frame52 = new frame52();
            frame52.staticLink = this;
            frame52.rest = rest;
            frame52.start1 = start1;
            frame52.end1 = end1;
            return call_with_values.callWithValues(frame52.lambda$Fn126, frame52.lambda$Fn127);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame52 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn126 = new ModuleMethod(this, 104, (Object) null, 0);
        final ModuleMethod lambda$Fn127 = new ModuleMethod(this, 105, (Object) null, 8194);
        Object rest;
        Object start1;
        frame51 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 104 ? lambda126() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 105 ? lambda127(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 104) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 105) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda126() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnci$Ls$Eq, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda127(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numLEq.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompareCi(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, misc.values, misc.values, srfi13.lambda$Fn128);
        }

        static Boolean lambda128(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object stringCi$Ls$Eq$V(Object s1, Object s2, Object[] argsArray) {
        frame51 frame512 = new frame51();
        frame512.s1 = s1;
        frame512.s2 = s2;
        frame512.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame512.lambda$Fn124, frame512.lambda$Fn125);
    }

    /* compiled from: srfi13.scm */
    public class frame53 extends ModuleBody {
        final ModuleMethod lambda$Fn129 = new ModuleMethod(this, 110, (Object) null, 0);
        final ModuleMethod lambda$Fn130 = new ModuleMethod(this, 111, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object s1;
        Object s2;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 110 ? lambda129() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 111 ? lambda130(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 110) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 111) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda129() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnci$Gr$Eq, this.s1, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda130(Object rest, Object start1, Object end1) {
            frame54 frame54 = new frame54();
            frame54.staticLink = this;
            frame54.rest = rest;
            frame54.start1 = start1;
            frame54.end1 = end1;
            return call_with_values.callWithValues(frame54.lambda$Fn131, frame54.lambda$Fn132);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame54 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn131 = new ModuleMethod(this, 108, (Object) null, 0);
        final ModuleMethod lambda$Fn132 = new ModuleMethod(this, 109, (Object) null, 8194);
        Object rest;
        Object start1;
        frame53 staticLink;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 108 ? lambda131() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 109 ? lambda132(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 108) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 109) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda131() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnci$Gr$Eq, this.staticLink.s2, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda132(Object start2, Object end2) {
            boolean x = this.staticLink.s1 == this.staticLink.s2;
            if (!x ? x : Scheme.numEqu.apply2(this.start1, start2) != Boolean.FALSE) {
                return Scheme.numGEq.apply2(this.end1, end2);
            }
            return srfi13.$PcStringCompareCi(this.staticLink.s1, this.start1, this.end1, this.staticLink.s2, start2, end2, srfi13.lambda$Fn133, misc.values, misc.values);
        }

        static Boolean lambda133(Object i) {
            return Boolean.FALSE;
        }
    }

    public static Object stringCi$Gr$Eq$V(Object s1, Object s2, Object[] argsArray) {
        frame53 frame532 = new frame53();
        frame532.s1 = s1;
        frame532.s2 = s2;
        frame532.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame532.lambda$Fn129, frame532.lambda$Fn130);
    }

    public static Object $PcStringHash(Object s, Object char$To$Int, Object bound, Object start, Object end) {
        frame55 closureEnv = new frame55();
        closureEnv.char$Mn$Grint = char$To$Int;
        Object obj = Lit5;
        while (Scheme.numGEq.apply2(obj, bound) == Boolean.FALSE) {
            obj = AddOp.$Pl.apply2(obj, obj);
        }
        Object mask = AddOp.$Mn.apply2(obj, Lit1);
        Object obj2 = Lit0;
        Object i = start;
        while (Scheme.numGEq.apply2(i, end) == Boolean.FALSE) {
            Object i2 = AddOp.$Pl.apply2(i, Lit1);
            try {
                try {
                    obj2 = BitwiseOp.and.apply2(mask, AddOp.$Pl.apply2(MultiplyOp.$St.apply2(Lit6, obj2), Scheme.applyToArgs.apply2(closureEnv.char$Mn$Grint, Char.make(strings.stringRef((CharSequence) s, ((Number) i).intValue())))));
                    i = i2;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-ref", 2, i);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-ref", 1, s);
            }
        }
        return DivideOp.modulo.apply2(obj2, bound);
    }

    public static Object stringHash$V(Object s, Object[] argsArray) {
        Object obj;
        Object obj2;
        Object obj3;
        frame56 frame562 = new frame56();
        frame562.s = s;
        LList maybe$Mnbound$Plstart$Plend = LList.makeList(argsArray, 0);
        ApplyToArgs applyToArgs = Scheme.applyToArgs;
        try {
            Object obj4 = loc$let$Mnoptionals$St.get();
            ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
            ApplyToArgs applyToArgs3 = Scheme.applyToArgs;
            try {
                Object obj5 = loc$bound.get();
                IntNum intNum = Lit7;
                try {
                    boolean x = numbers.isInteger(loc$bound.get());
                    if (x) {
                        try {
                            boolean x2 = numbers.isExact(loc$bound.get());
                            if (x2) {
                                try {
                                    obj = Scheme.numLEq.apply2(Lit0, loc$bound.get());
                                } catch (UnboundLocationException e) {
                                    e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 909, 19);
                                    throw e;
                                }
                            } else {
                                obj = x2 ? Boolean.TRUE : Boolean.FALSE;
                            }
                        } catch (UnboundLocationException e2) {
                            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_PERMISSION_DENIED, 21);
                            throw e2;
                        }
                    } else {
                        obj = x ? Boolean.TRUE : Boolean.FALSE;
                    }
                    try {
                        Object apply2 = applyToArgs2.apply2(applyToArgs3.apply3(obj5, intNum, obj), loc$rest.get());
                        try {
                            try {
                                if (numbers.isZero((Number) loc$bound.get())) {
                                    obj3 = Lit8;
                                } else {
                                    try {
                                        obj3 = loc$bound.get();
                                    } catch (UnboundLocationException e3) {
                                        e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 911, 18);
                                        throw e3;
                                    }
                                }
                                frame562.bound = obj3;
                                return applyToArgs.apply4(obj4, maybe$Mnbound$Plstart$Plend, apply2, call_with_values.callWithValues(frame562.lambda$Fn134, frame562.lambda$Fn135));
                            } catch (ClassCastException e4) {
                                throw new WrongType(e4, "zero?", 1, obj2);
                            }
                        } catch (UnboundLocationException e5) {
                            e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 911, 29);
                            throw e5;
                        }
                    } catch (UnboundLocationException e6) {
                        e6.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 910, 7);
                        throw e6;
                    }
                } catch (UnboundLocationException e7) {
                    e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_ACTIONBAR_NOT_SUPPORTED, 72);
                    throw e7;
                }
            } catch (UnboundLocationException e8) {
                e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_ACTIONBAR_NOT_SUPPORTED, 42);
                throw e8;
            }
        } catch (UnboundLocationException e9) {
            e9.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_ACTIONBAR_NOT_SUPPORTED, 3);
            throw e9;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame56 extends ModuleBody {
        Object bound;
        final ModuleMethod lambda$Fn134 = new ModuleMethod(this, 112, (Object) null, 0);
        final ModuleMethod lambda$Fn135 = new ModuleMethod(this, 113, (Object) null, 8194);
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 112 ? lambda134() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 113 ? lambda135(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 112) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 113) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda134() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnhash, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 912, 55);
                throw e;
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda135(Object start, Object end) {
            return srfi13.$PcStringHash(this.s, characters.char$Mn$Grinteger, this.bound, start, end);
        }
    }

    public static Object stringHashCi$V(Object s, Object[] argsArray) {
        Object obj;
        Object obj2;
        Object obj3;
        frame57 frame572 = new frame57();
        frame572.s = s;
        LList maybe$Mnbound$Plstart$Plend = LList.makeList(argsArray, 0);
        ApplyToArgs applyToArgs = Scheme.applyToArgs;
        try {
            Object obj4 = loc$let$Mnoptionals$St.get();
            ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
            ApplyToArgs applyToArgs3 = Scheme.applyToArgs;
            try {
                Object obj5 = loc$bound.get();
                IntNum intNum = Lit9;
                try {
                    boolean x = numbers.isInteger(loc$bound.get());
                    if (x) {
                        try {
                            boolean x2 = numbers.isExact(loc$bound.get());
                            if (x2) {
                                try {
                                    obj = Scheme.numLEq.apply2(Lit0, loc$bound.get());
                                } catch (UnboundLocationException e) {
                                    e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 918, 19);
                                    throw e;
                                }
                            } else {
                                obj = x2 ? Boolean.TRUE : Boolean.FALSE;
                            }
                        } catch (UnboundLocationException e2) {
                            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 917, 21);
                            throw e2;
                        }
                    } else {
                        obj = x ? Boolean.TRUE : Boolean.FALSE;
                    }
                    try {
                        Object apply2 = applyToArgs2.apply2(applyToArgs3.apply3(obj5, intNum, obj), loc$rest.get());
                        try {
                            try {
                                if (numbers.isZero((Number) loc$bound.get())) {
                                    obj3 = Lit10;
                                } else {
                                    try {
                                        obj3 = loc$bound.get();
                                    } catch (UnboundLocationException e3) {
                                        e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 920, 18);
                                        throw e3;
                                    }
                                }
                                frame572.bound = obj3;
                                return applyToArgs.apply4(obj4, maybe$Mnbound$Plstart$Plend, apply2, call_with_values.callWithValues(frame572.lambda$Fn136, frame572.lambda$Fn137));
                            } catch (ClassCastException e4) {
                                throw new WrongType(e4, "zero?", 1, obj2);
                            }
                        } catch (UnboundLocationException e5) {
                            e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 920, 29);
                            throw e5;
                        }
                    } catch (UnboundLocationException e6) {
                        e6.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 919, 7);
                        throw e6;
                    }
                } catch (UnboundLocationException e7) {
                    e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 916, 72);
                    throw e7;
                }
            } catch (UnboundLocationException e8) {
                e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 916, 42);
                throw e8;
            }
        } catch (UnboundLocationException e9) {
            e9.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 916, 3);
            throw e9;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame57 extends ModuleBody {
        Object bound;
        final ModuleMethod lambda$Fn136 = new ModuleMethod(this, 114, (Object) null, 0);
        final ModuleMethod lambda$Fn137 = new ModuleMethod(this, 115, (Object) null, 8194);
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 114 ? lambda136() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 115 ? lambda137(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 114) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 115) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda136() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnhash$Mnci, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 921, 58);
                throw e;
            }
        }

        static int lambda138(Object c) {
            try {
                return characters.char$To$Integer(unicode.charDowncase((Char) c));
            } catch (ClassCastException e) {
                throw new WrongType(e, "char-downcase", 1, c);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda137(Object start, Object end) {
            return srfi13.$PcStringHash(this.s, srfi13.lambda$Fn138, this.bound, start, end);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame58 extends ModuleBody {
        final ModuleMethod lambda$Fn139 = new ModuleMethod(this, 116, (Object) null, 0);
        final ModuleMethod lambda$Fn140 = new ModuleMethod(this, 117, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 116 ? lambda139() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 117 ? lambda140(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 116) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 117) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda139() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnupcase, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda140(Object start, Object end) {
            return srfi13.$PcStringMap(unicode.char$Mnupcase, this.s, start, end);
        }
    }

    public static Object stringUpcase$V(Object s, Object[] argsArray) {
        frame58 frame582 = new frame58();
        frame582.s = s;
        frame582.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame582.lambda$Fn139, frame582.lambda$Fn140);
    }

    /* compiled from: srfi13.scm */
    public class frame59 extends ModuleBody {
        final ModuleMethod lambda$Fn141 = new ModuleMethod(this, 118, (Object) null, 0);
        final ModuleMethod lambda$Fn142 = new ModuleMethod(this, 119, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 118 ? lambda141() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 119 ? lambda142(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 118) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 119) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda141() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnupcase$Ex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda142(Object start, Object end) {
            return srfi13.$PcStringMap$Ex(unicode.char$Mnupcase, this.s, start, end);
        }
    }

    public static Object stringUpcase$Ex$V(Object s, Object[] argsArray) {
        frame59 frame592 = new frame59();
        frame592.s = s;
        frame592.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame592.lambda$Fn141, frame592.lambda$Fn142);
    }

    /* compiled from: srfi13.scm */
    public class frame60 extends ModuleBody {
        final ModuleMethod lambda$Fn143 = new ModuleMethod(this, 120, (Object) null, 0);
        final ModuleMethod lambda$Fn144 = new ModuleMethod(this, 121, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 120 ? lambda143() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 121 ? lambda144(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 120) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 121) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda143() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mndowncase, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda144(Object start, Object end) {
            return srfi13.$PcStringMap(unicode.char$Mndowncase, this.s, start, end);
        }
    }

    public static Object stringDowncase$V(Object s, Object[] argsArray) {
        frame60 frame602 = new frame60();
        frame602.s = s;
        frame602.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame602.lambda$Fn143, frame602.lambda$Fn144);
    }

    /* compiled from: srfi13.scm */
    public class frame61 extends ModuleBody {
        final ModuleMethod lambda$Fn145 = new ModuleMethod(this, 122, (Object) null, 0);
        final ModuleMethod lambda$Fn146 = new ModuleMethod(this, 123, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 122 ? lambda145() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 123 ? lambda146(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 122) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 123) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda145() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mndowncase$Ex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda146(Object start, Object end) {
            return srfi13.$PcStringMap$Ex(unicode.char$Mndowncase, this.s, start, end);
        }
    }

    public static Object stringDowncase$Ex$V(Object s, Object[] argsArray) {
        frame61 frame612 = new frame61();
        frame612.s = s;
        frame612.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame612.lambda$Fn145, frame612.lambda$Fn146);
    }

    public static Object $PcStringTitlecase$Ex(Object s, Object start, Object end) {
        Object i = start;
        while (true) {
            try {
                Object temp = stringIndex$V(s, loc$char$Mncased$Qu.get(), new Object[]{i, end});
                if (temp == Boolean.FALSE) {
                    return Values.empty;
                }
                try {
                    CharSeq charSeq = (CharSeq) s;
                    try {
                        int intValue = ((Number) temp).intValue();
                        try {
                            try {
                                Char charTitlecase = unicode.charTitlecase(Char.make(strings.stringRef((CharSequence) s, ((Number) temp).intValue())));
                                try {
                                    strings.stringSet$Ex(charSeq, intValue, charTitlecase.charValue());
                                    Object i1 = AddOp.$Pl.apply2(temp, Lit1);
                                    try {
                                        Object temp2 = stringSkip$V(s, loc$char$Mncased$Qu.get(), new Object[]{i1, end});
                                        if (temp2 != Boolean.FALSE) {
                                            stringDowncase$Ex$V(s, new Object[]{i1, temp2});
                                            i = AddOp.$Pl.apply2(temp2, Lit1);
                                        } else {
                                            return stringDowncase$Ex$V(s, new Object[]{i1, end});
                                        }
                                    } catch (UnboundLocationException e) {
                                        e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 959, 31);
                                        throw e;
                                    }
                                } catch (ClassCastException e2) {
                                    throw new WrongType(e2, "string-set!", 3, (Object) charTitlecase);
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "string-ref", 2, temp);
                            }
                        } catch (ClassCastException e4) {
                            throw new WrongType(e4, "string-ref", 1, s);
                        }
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "string-set!", 2, temp);
                    }
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "string-set!", 1, s);
                }
            } catch (UnboundLocationException e7) {
                e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 955, 28);
                throw e7;
            }
        }
    }

    /* compiled from: srfi13.scm */
    public class frame62 extends ModuleBody {
        final ModuleMethod lambda$Fn147 = new ModuleMethod(this, 124, (Object) null, 0);
        final ModuleMethod lambda$Fn148 = new ModuleMethod(this, 125, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 124 ? lambda147() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 125 ? lambda148(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 124) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 125) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda147() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mntitlecase$Ex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda148(Object start, Object end) {
            return srfi13.$PcStringTitlecase$Ex(this.s, start, end);
        }
    }

    public static Object stringTitlecase$Ex$V(Object s, Object[] argsArray) {
        frame62 frame622 = new frame62();
        frame622.s = s;
        frame622.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame622.lambda$Fn147, frame622.lambda$Fn148);
    }

    /* compiled from: srfi13.scm */
    public class frame63 extends ModuleBody {
        final ModuleMethod lambda$Fn149 = new ModuleMethod(this, 126, (Object) null, 0);
        final ModuleMethod lambda$Fn150 = new ModuleMethod(this, 127, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 126 ? lambda149() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 127 ? lambda150(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 126) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 127) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda149() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mntitlecase$Ex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda150(Object start, Object end) {
            Object obj = this.s;
            try {
                try {
                    try {
                        CharSequence ans = strings.substring((CharSequence) obj, ((Number) start).intValue(), ((Number) end).intValue());
                        srfi13.$PcStringTitlecase$Ex(ans, srfi13.Lit0, AddOp.$Mn.apply2(end, start));
                        return ans;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "substring", 3, end);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "substring", 2, start);
                }
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "substring", 1, obj);
            }
        }
    }

    public static Object stringTitlecase$V(Object s, Object[] argsArray) {
        frame63 frame632 = new frame63();
        frame632.s = s;
        frame632.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame632.lambda$Fn149, frame632.lambda$Fn150);
    }

    public static Object stringTake(Object s, Object n) {
        frame64 frame642 = new frame64();
        frame642.s = s;
        frame642.n = n;
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), strings.string$Qu, frame642.s, string$Mntake);
            try {
                Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), frame642.lambda$Fn151, frame642.n, string$Mntake);
                Object obj = frame642.s;
                try {
                    CharSequence charSequence = (CharSequence) obj;
                    Object obj2 = frame642.n;
                    try {
                        return $PcSubstring$SlShared(charSequence, 0, ((Number) obj2).intValue());
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "%substring/shared", 2, obj2);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "%substring/shared", 0, obj);
                }
            } catch (UnboundLocationException e3) {
                e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 996, 3);
                throw e3;
            }
        } catch (UnboundLocationException e4) {
            e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 995, 3);
            throw e4;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame64 extends ModuleBody {
        final ModuleMethod lambda$Fn151;
        Object n;
        Object s;

        public frame64() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 128, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:996");
            this.lambda$Fn151 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 128) {
                return lambda151(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda151(Object val) {
            boolean x = numbers.isInteger(this.n);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(this.n);
            if (!x2) {
                return x2;
            }
            NumberCompare numberCompare = Scheme.numLEq;
            IntNum intNum = srfi13.Lit0;
            Object obj = this.n;
            Object obj2 = this.s;
            try {
                return ((Boolean) numberCompare.apply3(intNum, obj, Integer.valueOf(strings.stringLength((CharSequence) obj2)))).booleanValue();
            } catch (ClassCastException e) {
                throw new WrongType(e, "string-length", 1, obj2);
            }
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 128) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object stringTakeRight(Object s, Object n) {
        frame65 frame652 = new frame65();
        frame652.n = n;
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), strings.string$Qu, s, string$Mntake$Mnright);
            try {
                frame652.len = strings.stringLength((CharSequence) s);
                try {
                    Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), frame652.lambda$Fn152, frame652.n, string$Mntake$Mnright);
                    try {
                        CharSequence charSequence = (CharSequence) s;
                        Object apply2 = AddOp.$Mn.apply2(Integer.valueOf(frame652.len), frame652.n);
                        try {
                            return $PcSubstring$SlShared(charSequence, ((Number) apply2).intValue(), frame652.len);
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%substring/shared", 1, apply2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%substring/shared", 0, s);
                    }
                } catch (UnboundLocationException e3) {
                    e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1004, 5);
                    throw e3;
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "string-length", 1, s);
            }
        } catch (UnboundLocationException e5) {
            e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1002, 3);
            throw e5;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame65 extends ModuleBody {
        final ModuleMethod lambda$Fn152;
        int len;
        Object n;

        public frame65() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 129, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1004");
            this.lambda$Fn152 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 129) {
                return lambda152(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda152(Object val) {
            boolean x = numbers.isInteger(this.n);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(this.n);
            return x2 ? ((Boolean) Scheme.numLEq.apply3(srfi13.Lit0, this.n, Integer.valueOf(this.len))).booleanValue() : x2;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 129) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object stringDrop(CharSequence s, Object n) {
        frame66 frame662 = new frame66();
        frame662.n = n;
        frame662.len = strings.stringLength(s);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), frame662.lambda$Fn153, frame662.n, string$Mndrop);
            Object obj = frame662.n;
            try {
                return $PcSubstring$SlShared(s, ((Number) obj).intValue(), frame662.len);
            } catch (ClassCastException e) {
                throw new WrongType(e, "%substring/shared", 1, obj);
            }
        } catch (UnboundLocationException e2) {
            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", PointerIconCompat.TYPE_ALIAS, 5);
            throw e2;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame66 extends ModuleBody {
        final ModuleMethod lambda$Fn153;
        int len;
        Object n;

        public frame66() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 130, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1010");
            this.lambda$Fn153 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 130) {
                return lambda153(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda153(Object val) {
            boolean x = numbers.isInteger(this.n);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(this.n);
            return x2 ? ((Boolean) Scheme.numLEq.apply3(srfi13.Lit0, this.n, Integer.valueOf(this.len))).booleanValue() : x2;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 130) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object stringDropRight(CharSequence s, Object n) {
        frame67 frame672 = new frame67();
        frame672.n = n;
        frame672.len = strings.stringLength(s);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), frame672.lambda$Fn154, frame672.n, string$Mndrop$Mnright);
            Object apply2 = AddOp.$Mn.apply2(Integer.valueOf(frame672.len), frame672.n);
            try {
                return $PcSubstring$SlShared(s, 0, ((Number) apply2).intValue());
            } catch (ClassCastException e) {
                throw new WrongType(e, "%substring/shared", 2, apply2);
            }
        } catch (UnboundLocationException e2) {
            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", PointerIconCompat.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW, 5);
            throw e2;
        }
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        switch (moduleMethod.selector) {
            case 218:
                try {
                    return stringTabulate(obj, ((Number) obj2).intValue());
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-tabulate", 2, obj2);
                }
            case 280:
                return stringTake(obj, obj2);
            case 281:
                return stringTakeRight(obj, obj2);
            case 282:
                try {
                    return stringDrop((CharSequence) obj, obj2);
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "string-drop", 1, obj);
                }
            case 283:
                try {
                    return stringDropRight((CharSequence) obj, obj2);
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "string-drop-right", 1, obj);
                }
            default:
                return super.apply2(moduleMethod, obj, obj2);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame67 extends ModuleBody {
        final ModuleMethod lambda$Fn154;
        int len;
        Object n;

        public frame67() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 131, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1016");
            this.lambda$Fn154 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 131) {
                return lambda154(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda154(Object val) {
            boolean x = numbers.isInteger(this.n);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(this.n);
            return x2 ? ((Boolean) Scheme.numLEq.apply3(srfi13.Lit0, this.n, Integer.valueOf(this.len))).booleanValue() : x2;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 131) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object stringTrim$V(Object s, Object[] argsArray) {
        frame68 frame682 = new frame68();
        frame682.s = s;
        LList criterion$Plstart$Plend = LList.makeList(argsArray, 0);
        try {
            try {
                try {
                    try {
                        return Scheme.applyToArgs.apply4(loc$let$Mnoptionals$St.get(), criterion$Plstart$Plend, Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply2(loc$criterion.get(), GetNamedPart.getNamedPart.apply2(loc$char$Mnset.get(), Lit11)), loc$rest.get()), call_with_values.callWithValues(frame682.lambda$Fn155, frame682.lambda$Fn156));
                    } catch (UnboundLocationException e) {
                        e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1022, 72);
                        throw e;
                    }
                } catch (UnboundLocationException e2) {
                    e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1022, 51);
                    throw e2;
                }
            } catch (UnboundLocationException e3) {
                e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1022, 40);
                throw e3;
            }
        } catch (UnboundLocationException e4) {
            e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1022, 3);
            throw e4;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame68 extends ModuleBody {
        final ModuleMethod lambda$Fn155 = new ModuleMethod(this, 132, (Object) null, 0);
        final ModuleMethod lambda$Fn156 = new ModuleMethod(this, 133, (Object) null, 8194);
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 132 ? lambda155() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 133 ? lambda156(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 132) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 133) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda155() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mntrim, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1023, 53);
                throw e;
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda156(Object start, Object end) {
            try {
                Object temp = srfi13.stringSkip$V(this.s, srfi13.loc$criterion.get(), new Object[]{start, end});
                if (temp == Boolean.FALSE) {
                    return "";
                }
                Object obj = this.s;
                try {
                    try {
                        try {
                            return srfi13.$PcSubstring$SlShared((CharSequence) obj, ((Number) temp).intValue(), ((Number) end).intValue());
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%substring/shared", 2, end);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%substring/shared", 1, temp);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "%substring/shared", 0, obj);
                }
            } catch (UnboundLocationException e4) {
                e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1024, 29);
                throw e4;
            }
        }
    }

    public static Object stringTrimRight$V(Object s, Object[] argsArray) {
        frame69 frame692 = new frame69();
        frame692.s = s;
        LList criterion$Plstart$Plend = LList.makeList(argsArray, 0);
        try {
            try {
                try {
                    try {
                        return Scheme.applyToArgs.apply4(loc$let$Mnoptionals$St.get(), criterion$Plstart$Plend, Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply2(loc$criterion.get(), GetNamedPart.getNamedPart.apply2(loc$char$Mnset.get(), Lit11)), loc$rest.get()), call_with_values.callWithValues(frame692.lambda$Fn157, frame692.lambda$Fn158));
                    } catch (UnboundLocationException e) {
                        e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1029, 72);
                        throw e;
                    }
                } catch (UnboundLocationException e2) {
                    e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1029, 51);
                    throw e2;
                }
            } catch (UnboundLocationException e3) {
                e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1029, 40);
                throw e3;
            }
        } catch (UnboundLocationException e4) {
            e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1029, 3);
            throw e4;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame69 extends ModuleBody {
        final ModuleMethod lambda$Fn157 = new ModuleMethod(this, 134, (Object) null, 0);
        final ModuleMethod lambda$Fn158 = new ModuleMethod(this, 135, (Object) null, 8194);
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 134 ? lambda157() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 135 ? lambda158(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 134) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 135) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda157() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mntrim$Mnright, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1030, 59);
                throw e;
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda158(Object start, Object end) {
            try {
                Object temp = srfi13.stringSkipRight$V(this.s, srfi13.loc$criterion.get(), new Object[]{start, end});
                if (temp == Boolean.FALSE) {
                    return "";
                }
                Object obj = this.s;
                try {
                    CharSequence charSequence = (CharSequence) obj;
                    Object apply2 = AddOp.$Pl.apply2(srfi13.Lit1, temp);
                    try {
                        return srfi13.$PcSubstring$SlShared(charSequence, 0, ((Number) apply2).intValue());
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "%substring/shared", 2, apply2);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "%substring/shared", 0, obj);
                }
            } catch (UnboundLocationException e3) {
                e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1031, 35);
                throw e3;
            }
        }
    }

    public static Object stringTrimBoth$V(Object s, Object[] argsArray) {
        frame70 frame702 = new frame70();
        frame702.s = s;
        LList criterion$Plstart$Plend = LList.makeList(argsArray, 0);
        try {
            try {
                try {
                    try {
                        return Scheme.applyToArgs.apply4(loc$let$Mnoptionals$St.get(), criterion$Plstart$Plend, Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply2(loc$criterion.get(), GetNamedPart.getNamedPart.apply2(loc$char$Mnset.get(), Lit11)), loc$rest.get()), call_with_values.callWithValues(frame702.lambda$Fn159, frame702.lambda$Fn160));
                    } catch (UnboundLocationException e) {
                        e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1036, 72);
                        throw e;
                    }
                } catch (UnboundLocationException e2) {
                    e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1036, 51);
                    throw e2;
                }
            } catch (UnboundLocationException e3) {
                e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1036, 40);
                throw e3;
            }
        } catch (UnboundLocationException e4) {
            e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1036, 3);
            throw e4;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame70 extends ModuleBody {
        final ModuleMethod lambda$Fn159 = new ModuleMethod(this, 136, (Object) null, 0);
        final ModuleMethod lambda$Fn160 = new ModuleMethod(this, 137, (Object) null, 8194);
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 136 ? lambda159() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 137 ? lambda160(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 136) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 137) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda159() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mntrim$Mnboth, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1037, 58);
                throw e;
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda160(Object start, Object end) {
            try {
                Object temp = srfi13.stringSkip$V(this.s, srfi13.loc$criterion.get(), new Object[]{start, end});
                if (temp == Boolean.FALSE) {
                    return "";
                }
                Object obj = this.s;
                try {
                    CharSequence charSequence = (CharSequence) obj;
                    try {
                        int intValue = ((Number) temp).intValue();
                        try {
                            Object apply2 = AddOp.$Pl.apply2(srfi13.Lit1, srfi13.stringSkipRight$V(this.s, srfi13.loc$criterion.get(), new Object[]{temp, end}));
                            try {
                                return srfi13.$PcSubstring$SlShared(charSequence, intValue, ((Number) apply2).intValue());
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "%substring/shared", 2, apply2);
                            }
                        } catch (UnboundLocationException e2) {
                            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1040, 58);
                            throw e2;
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "%substring/shared", 1, temp);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "%substring/shared", 0, obj);
                }
            } catch (UnboundLocationException e5) {
                e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1038, 29);
                throw e5;
            }
        }
    }

    public static Object stringPadRight$V(Object s, Object n, Object[] argsArray) {
        frame71 frame712 = new frame71();
        frame712.s = s;
        frame712.n = n;
        try {
            try {
                return Scheme.applyToArgs.apply4(loc$let$Mnoptionals$St.get(), LList.makeList(argsArray, 0), Scheme.applyToArgs.apply2(Invoke.make.apply3(LangPrimType.charType, Lit12, characters.isChar(LangPrimType.charType) ? Boolean.TRUE : Boolean.FALSE), loc$rest.get()), call_with_values.callWithValues(frame712.lambda$Fn161, frame712.lambda$Fn162));
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1045, 63);
                throw e;
            }
        } catch (UnboundLocationException e2) {
            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1045, 3);
            throw e2;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame71 extends ModuleBody {
        final ModuleMethod lambda$Fn161 = new ModuleMethod(this, 138, (Object) null, 0);
        final ModuleMethod lambda$Fn162 = new ModuleMethod(this, 139, (Object) null, 8194);
        Object n;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 138 ? lambda161() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 139 ? lambda162(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 138) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 139) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda161() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnpad$Mnright, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1046, 58);
                throw e;
            }
        }

        static boolean lambda163(Object n2) {
            boolean x = numbers.isInteger(n2);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(n2);
            return x2 ? ((Boolean) Scheme.numLEq.apply2(srfi13.Lit0, n2)).booleanValue() : x2;
        }

        /* access modifiers changed from: package-private */
        public Object lambda162(Object start, Object end) {
            try {
                Scheme.applyToArgs.apply4(srfi13.loc$check$Mnarg.get(), srfi13.lambda$Fn163, this.n, srfi13.string$Mnpad$Mnright);
                if (Scheme.numLEq.apply2(this.n, AddOp.$Mn.apply2(end, start)) != Boolean.FALSE) {
                    Object obj = this.s;
                    try {
                        CharSequence charSequence = (CharSequence) obj;
                        try {
                            int intValue = ((Number) start).intValue();
                            Object apply2 = AddOp.$Pl.apply2(start, this.n);
                            try {
                                return srfi13.$PcSubstring$SlShared(charSequence, intValue, ((Number) apply2).intValue());
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "%substring/shared", 2, apply2);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "%substring/shared", 1, start);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "%substring/shared", 0, obj);
                    }
                } else {
                    Object obj2 = this.n;
                    try {
                        CharSequence ans = strings.makeString(((Number) obj2).intValue(), LangPrimType.charType);
                        Object obj3 = this.s;
                        try {
                            try {
                                try {
                                    srfi13.$PcStringCopy$Ex(ans, 0, (CharSequence) obj3, ((Number) start).intValue(), ((Number) end).intValue());
                                    return ans;
                                } catch (ClassCastException e4) {
                                    throw new WrongType(e4, "%string-copy!", 4, end);
                                }
                            } catch (ClassCastException e5) {
                                throw new WrongType(e5, "%string-copy!", 3, start);
                            }
                        } catch (ClassCastException e6) {
                            throw new WrongType(e6, "%string-copy!", 2, obj3);
                        }
                    } catch (ClassCastException e7) {
                        throw new WrongType(e7, "make-string", 1, obj2);
                    }
                }
            } catch (UnboundLocationException e8) {
                e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1047, 7);
                throw e8;
            }
        }
    }

    public static Object stringPad$V(Object s, Object n, Object[] argsArray) {
        frame72 frame722 = new frame72();
        frame722.s = s;
        frame722.n = n;
        try {
            try {
                return Scheme.applyToArgs.apply4(loc$let$Mnoptionals$St.get(), LList.makeList(argsArray, 0), Scheme.applyToArgs.apply2(Invoke.make.apply3(LangPrimType.charType, Lit12, characters.isChar(LangPrimType.charType) ? Boolean.TRUE : Boolean.FALSE), loc$rest.get()), call_with_values.callWithValues(frame722.lambda$Fn164, frame722.lambda$Fn165));
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1057, 63);
                throw e;
            }
        } catch (UnboundLocationException e2) {
            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1057, 3);
            throw e2;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame72 extends ModuleBody {
        final ModuleMethod lambda$Fn164 = new ModuleMethod(this, 140, (Object) null, 0);
        final ModuleMethod lambda$Fn165 = new ModuleMethod(this, 141, (Object) null, 8194);
        Object n;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 140 ? lambda164() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 141 ? lambda165(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 140) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 141) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda164() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnpad, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1058, 52);
                throw e;
            }
        }

        static boolean lambda166(Object n2) {
            boolean x = numbers.isInteger(n2);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(n2);
            return x2 ? ((Boolean) Scheme.numLEq.apply2(srfi13.Lit0, n2)).booleanValue() : x2;
        }

        /* access modifiers changed from: package-private */
        public Object lambda165(Object start, Object end) {
            try {
                Scheme.applyToArgs.apply4(srfi13.loc$check$Mnarg.get(), srfi13.lambda$Fn166, this.n, srfi13.string$Mnpad);
                Object len = AddOp.$Mn.apply2(end, start);
                if (Scheme.numLEq.apply2(this.n, len) != Boolean.FALSE) {
                    Object obj = this.s;
                    try {
                        CharSequence charSequence = (CharSequence) obj;
                        Object apply2 = AddOp.$Mn.apply2(end, this.n);
                        try {
                            try {
                                return srfi13.$PcSubstring$SlShared(charSequence, ((Number) apply2).intValue(), ((Number) end).intValue());
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "%substring/shared", 2, end);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "%substring/shared", 1, apply2);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "%substring/shared", 0, obj);
                    }
                } else {
                    Object obj2 = this.n;
                    try {
                        CharSequence ans = strings.makeString(((Number) obj2).intValue(), LangPrimType.charType);
                        Object apply22 = AddOp.$Mn.apply2(this.n, len);
                        try {
                            int intValue = ((Number) apply22).intValue();
                            Object obj3 = this.s;
                            try {
                                try {
                                    try {
                                        srfi13.$PcStringCopy$Ex(ans, intValue, (CharSequence) obj3, ((Number) start).intValue(), ((Number) end).intValue());
                                        return ans;
                                    } catch (ClassCastException e4) {
                                        throw new WrongType(e4, "%string-copy!", 4, end);
                                    }
                                } catch (ClassCastException e5) {
                                    throw new WrongType(e5, "%string-copy!", 3, start);
                                }
                            } catch (ClassCastException e6) {
                                throw new WrongType(e6, "%string-copy!", 2, obj3);
                            }
                        } catch (ClassCastException e7) {
                            throw new WrongType(e7, "%string-copy!", 1, apply22);
                        }
                    } catch (ClassCastException e8) {
                        throw new WrongType(e8, "make-string", 1, obj2);
                    }
                }
            } catch (UnboundLocationException e9) {
                e9.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1059, 7);
                throw e9;
            }
        }
    }

    /* compiled from: srfi13.scm */
    public class frame73 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn167 = new ModuleMethod(this, 145, (Object) null, 0);
        final ModuleMethod lambda$Fn168 = new ModuleMethod(this, 146, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 145 ? lambda167() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 146 ? lambda168(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 145) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 146) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda167() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mndelete, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda168(Object start, Object end) {
            Object error$V;
            frame74 frame74 = new frame74();
            frame74.staticLink = this;
            if (misc.isProcedure(this.criterion)) {
                Object slen = AddOp.$Mn.apply2(end, start);
                try {
                    frame74.temp = strings.makeString(((Number) slen).intValue());
                    Object ans$Mnlen = srfi13.stringFold$V(frame74.lambda$Fn169, srfi13.Lit0, this.s, new Object[]{start, end});
                    if (Scheme.numEqu.apply2(ans$Mnlen, slen) != Boolean.FALSE) {
                        return frame74.temp;
                    }
                    try {
                        return strings.substring(frame74.temp, 0, ((Number) ans$Mnlen).intValue());
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "substring", 3, ans$Mnlen);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "make-string", 1, slen);
                }
            } else {
                try {
                    if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                        error$V = this.criterion;
                    } else if (characters.isChar(this.criterion)) {
                        try {
                            error$V = Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset.get(), this.criterion);
                        } catch (UnboundLocationException e3) {
                            e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1097, 26);
                            throw e3;
                        }
                    } else {
                        error$V = misc.error$V("string-delete criterion not predicate, char or char-set", new Object[]{this.criterion});
                    }
                    frame74.cset = error$V;
                    Object len = srfi13.stringFold$V(frame74.lambda$Fn170, srfi13.Lit0, this.s, new Object[]{start, end});
                    try {
                        frame74.ans = strings.makeString(((Number) len).intValue());
                        srfi13.stringFold$V(frame74.lambda$Fn171, srfi13.Lit0, this.s, new Object[]{start, end});
                        return frame74.ans;
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "make-string", 1, len);
                    }
                } catch (UnboundLocationException e5) {
                    e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1096, 22);
                    throw e5;
                }
            }
        }
    }

    public static Object stringDelete$V(Object criterion, Object s, Object[] argsArray) {
        frame73 frame732 = new frame73();
        frame732.criterion = criterion;
        frame732.s = s;
        frame732.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame732.lambda$Fn167, frame732.lambda$Fn168);
    }

    /* compiled from: srfi13.scm */
    public class frame74 extends ModuleBody {
        CharSequence ans;
        Object cset;
        final ModuleMethod lambda$Fn169;
        final ModuleMethod lambda$Fn170;
        final ModuleMethod lambda$Fn171;
        frame73 staticLink;
        CharSequence temp;

        public frame74() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 142, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1089");
            this.lambda$Fn169 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 143, (Object) null, 8194);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1099");
            this.lambda$Fn170 = moduleMethod2;
            ModuleMethod moduleMethod3 = new ModuleMethod(this, 144, (Object) null, 8194);
            moduleMethod3.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1104");
            this.lambda$Fn171 = moduleMethod3;
        }

        /* access modifiers changed from: package-private */
        public Object lambda169(Object c, Object i) {
            if (Scheme.applyToArgs.apply2(this.staticLink.criterion, c) != Boolean.FALSE) {
                return i;
            }
            CharSequence charSequence = this.temp;
            try {
                try {
                    try {
                        strings.stringSet$Ex((CharSeq) charSequence, ((Number) i).intValue(), ((Char) c).charValue());
                        return AddOp.$Pl.apply2(i, srfi13.Lit1);
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string-set!", 3, c);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "string-set!", 2, i);
                }
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "string-set!", 1, (Object) charSequence);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
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
                case 144:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda170(Object c, Object i) {
            try {
                return Scheme.applyToArgs.apply3(srfi13.loc$char$Mnset$Mncontains$Qu.get(), this.cset, c) != Boolean.FALSE ? i : AddOp.$Pl.apply2(i, srfi13.Lit1);
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1099, 45);
                throw e;
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 142:
                    return lambda169(obj, obj2);
                case 143:
                    return lambda170(obj, obj2);
                case 144:
                    return lambda171(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda171(Object c, Object i) {
            try {
                if (Scheme.applyToArgs.apply3(srfi13.loc$char$Mnset$Mncontains$Qu.get(), this.cset, c) != Boolean.FALSE) {
                    return i;
                }
                CharSequence charSequence = this.ans;
                try {
                    try {
                        try {
                            strings.stringSet$Ex((CharSeq) charSequence, ((Number) i).intValue(), ((Char) c).charValue());
                            return AddOp.$Pl.apply2(i, srfi13.Lit1);
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-set!", 3, c);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-set!", 2, i);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "string-set!", 1, (Object) charSequence);
                }
            } catch (UnboundLocationException e4) {
                e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1104, 35);
                throw e4;
            }
        }
    }

    /* compiled from: srfi13.scm */
    public class frame75 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn172 = new ModuleMethod(this, 150, (Object) null, 0);
        final ModuleMethod lambda$Fn173 = new ModuleMethod(this, 151, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 150 ? lambda172() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 151 ? lambda173(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 150) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 151) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda172() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnfilter, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda173(Object start, Object end) {
            Object error$V;
            frame76 frame76 = new frame76();
            frame76.staticLink = this;
            if (misc.isProcedure(this.criterion)) {
                Object slen = AddOp.$Mn.apply2(end, start);
                try {
                    frame76.temp = strings.makeString(((Number) slen).intValue());
                    Object ans$Mnlen = srfi13.stringFold$V(frame76.lambda$Fn174, srfi13.Lit0, this.s, new Object[]{start, end});
                    if (Scheme.numEqu.apply2(ans$Mnlen, slen) != Boolean.FALSE) {
                        return frame76.temp;
                    }
                    try {
                        return strings.substring(frame76.temp, 0, ((Number) ans$Mnlen).intValue());
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "substring", 3, ans$Mnlen);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "make-string", 1, slen);
                }
            } else {
                try {
                    if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                        error$V = this.criterion;
                    } else if (characters.isChar(this.criterion)) {
                        try {
                            error$V = Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset.get(), this.criterion);
                        } catch (UnboundLocationException e3) {
                            e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1125, 26);
                            throw e3;
                        }
                    } else {
                        error$V = misc.error$V("string-delete criterion not predicate, char or char-set", new Object[]{this.criterion});
                    }
                    frame76.cset = error$V;
                    Object len = srfi13.stringFold$V(frame76.lambda$Fn175, srfi13.Lit0, this.s, new Object[]{start, end});
                    try {
                        frame76.ans = strings.makeString(((Number) len).intValue());
                        srfi13.stringFold$V(frame76.lambda$Fn176, srfi13.Lit0, this.s, new Object[]{start, end});
                        return frame76.ans;
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "make-string", 1, len);
                    }
                } catch (UnboundLocationException e5) {
                    e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1124, 22);
                    throw e5;
                }
            }
        }
    }

    public static Object stringFilter$V(Object criterion, Object s, Object[] argsArray) {
        frame75 frame752 = new frame75();
        frame752.criterion = criterion;
        frame752.s = s;
        frame752.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame752.lambda$Fn172, frame752.lambda$Fn173);
    }

    /* compiled from: srfi13.scm */
    public class frame76 extends ModuleBody {
        CharSequence ans;
        Object cset;
        final ModuleMethod lambda$Fn174;
        final ModuleMethod lambda$Fn175;
        final ModuleMethod lambda$Fn176;
        frame75 staticLink;
        CharSequence temp;

        public frame76() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 147, (Object) null, 8194);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1116");
            this.lambda$Fn174 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 148, (Object) null, 8194);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1128");
            this.lambda$Fn175 = moduleMethod2;
            ModuleMethod moduleMethod3 = new ModuleMethod(this, 149, (Object) null, 8194);
            moduleMethod3.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1133");
            this.lambda$Fn176 = moduleMethod3;
        }

        /* access modifiers changed from: package-private */
        public Object lambda174(Object c, Object i) {
            if (Scheme.applyToArgs.apply2(this.staticLink.criterion, c) == Boolean.FALSE) {
                return i;
            }
            CharSequence charSequence = this.temp;
            try {
                try {
                    try {
                        strings.stringSet$Ex((CharSeq) charSequence, ((Number) i).intValue(), ((Char) c).charValue());
                        return AddOp.$Pl.apply2(i, srfi13.Lit1);
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string-set!", 3, c);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "string-set!", 2, i);
                }
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "string-set!", 1, (Object) charSequence);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 147:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 148:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 149:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda175(Object c, Object i) {
            try {
                if (Scheme.applyToArgs.apply3(srfi13.loc$char$Mnset$Mncontains$Qu.get(), this.cset, c) != Boolean.FALSE) {
                    return AddOp.$Pl.apply2(i, srfi13.Lit1);
                }
                return i;
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1128, 45);
                throw e;
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 147:
                    return lambda174(obj, obj2);
                case 148:
                    return lambda175(obj, obj2);
                case 149:
                    return lambda176(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda176(Object c, Object i) {
            try {
                if (Scheme.applyToArgs.apply3(srfi13.loc$char$Mnset$Mncontains$Qu.get(), this.cset, c) == Boolean.FALSE) {
                    return i;
                }
                CharSequence charSequence = this.ans;
                try {
                    try {
                        try {
                            strings.stringSet$Ex((CharSeq) charSequence, ((Number) i).intValue(), ((Char) c).charValue());
                            return AddOp.$Pl.apply2(i, srfi13.Lit1);
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-set!", 3, c);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-set!", 2, i);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "string-set!", 1, (Object) charSequence);
                }
            } catch (UnboundLocationException e4) {
                e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1133, 35);
                throw e4;
            }
        }
    }

    /* compiled from: srfi13.scm */
    public class frame77 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn177 = new ModuleMethod(this, 152, (Object) null, 0);
        final ModuleMethod lambda$Fn178 = new ModuleMethod(this, 153, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object str;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 152 ? lambda177() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 153 ? lambda178(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 152) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 153) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda177() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnindex, this.str, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda178(Object start, Object end) {
            if (characters.isChar(this.criterion)) {
                Object i = start;
                while (true) {
                    Object apply2 = Scheme.numLss.apply2(i, end);
                    try {
                        boolean x = ((Boolean) apply2).booleanValue();
                        if (!x) {
                            return x ? Boolean.TRUE : Boolean.FALSE;
                        }
                        Object obj = this.criterion;
                        try {
                            Char charR = (Char) obj;
                            Object obj2 = this.str;
                            try {
                                try {
                                    if (characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())))) {
                                        return i;
                                    }
                                    i = AddOp.$Pl.apply2(i, srfi13.Lit1);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, i);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, obj2);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "char=?", 1, obj);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "x", -2, apply2);
                    }
                }
            } else {
                try {
                    if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                        Object i2 = start;
                        while (true) {
                            Object apply22 = Scheme.numLss.apply2(i2, end);
                            try {
                                boolean x2 = ((Boolean) apply22).booleanValue();
                                if (!x2) {
                                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                                try {
                                    Object obj3 = srfi13.loc$char$Mnset$Mncontains$Qu.get();
                                    Object obj4 = this.criterion;
                                    Object obj5 = this.str;
                                    try {
                                        try {
                                            if (applyToArgs.apply3(obj3, obj4, Char.make(strings.stringRef((CharSequence) obj5, ((Number) i2).intValue()))) != Boolean.FALSE) {
                                                return i2;
                                            }
                                            i2 = AddOp.$Pl.apply2(i2, srfi13.Lit1);
                                        } catch (ClassCastException e5) {
                                            throw new WrongType(e5, "string-ref", 2, i2);
                                        }
                                    } catch (ClassCastException e6) {
                                        throw new WrongType(e6, "string-ref", 1, obj5);
                                    }
                                } catch (UnboundLocationException e7) {
                                    e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1162, 9);
                                    throw e7;
                                }
                            } catch (ClassCastException e8) {
                                throw new WrongType(e8, "x", -2, apply22);
                            }
                        }
                    } else if (misc.isProcedure(this.criterion)) {
                        Object i3 = start;
                        while (true) {
                            Object apply23 = Scheme.numLss.apply2(i3, end);
                            try {
                                boolean x3 = ((Boolean) apply23).booleanValue();
                                if (!x3) {
                                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                                Object obj6 = this.criterion;
                                Object obj7 = this.str;
                                try {
                                    try {
                                        if (applyToArgs2.apply2(obj6, Char.make(strings.stringRef((CharSequence) obj7, ((Number) i3).intValue()))) != Boolean.FALSE) {
                                            return i3;
                                        }
                                        i3 = AddOp.$Pl.apply2(i3, srfi13.Lit1);
                                    } catch (ClassCastException e9) {
                                        throw new WrongType(e9, "string-ref", 2, i3);
                                    }
                                } catch (ClassCastException e10) {
                                    throw new WrongType(e10, "string-ref", 1, obj7);
                                }
                            } catch (ClassCastException e11) {
                                throw new WrongType(e11, "x", -2, apply23);
                            }
                        }
                    } else {
                        return misc.error$V("Second param is neither char-set, char, or predicate procedure.", new Object[]{srfi13.string$Mnindex, this.criterion});
                    }
                } catch (UnboundLocationException e12) {
                    e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1159, 5);
                    throw e12;
                }
            }
        }
    }

    public static Object stringIndex$V(Object str, Object criterion, Object[] argsArray) {
        frame77 frame772 = new frame77();
        frame772.str = str;
        frame772.criterion = criterion;
        frame772.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame772.lambda$Fn177, frame772.lambda$Fn178);
    }

    /* compiled from: srfi13.scm */
    public class frame78 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn179 = new ModuleMethod(this, 154, (Object) null, 0);
        final ModuleMethod lambda$Fn180 = new ModuleMethod(this, 155, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object str;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 154 ? lambda179() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 155 ? lambda180(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 154) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 155) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda179() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnindex$Mnright, this.str, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda180(Object start, Object end) {
            if (characters.isChar(this.criterion)) {
                Object i = AddOp.$Mn.apply2(end, srfi13.Lit1);
                while (true) {
                    Object apply2 = Scheme.numGEq.apply2(i, start);
                    try {
                        boolean x = ((Boolean) apply2).booleanValue();
                        if (!x) {
                            return x ? Boolean.TRUE : Boolean.FALSE;
                        }
                        Object obj = this.criterion;
                        try {
                            Char charR = (Char) obj;
                            Object obj2 = this.str;
                            try {
                                try {
                                    if (characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())))) {
                                        return i;
                                    }
                                    i = AddOp.$Mn.apply2(i, srfi13.Lit1);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, i);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, obj2);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "char=?", 1, obj);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "x", -2, apply2);
                    }
                }
            } else {
                try {
                    if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                        Object i2 = AddOp.$Mn.apply2(end, srfi13.Lit1);
                        while (true) {
                            Object apply22 = Scheme.numGEq.apply2(i2, start);
                            try {
                                boolean x2 = ((Boolean) apply22).booleanValue();
                                if (!x2) {
                                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                                try {
                                    Object obj3 = srfi13.loc$char$Mnset$Mncontains$Qu.get();
                                    Object obj4 = this.criterion;
                                    Object obj5 = this.str;
                                    try {
                                        try {
                                            if (applyToArgs.apply3(obj3, obj4, Char.make(strings.stringRef((CharSequence) obj5, ((Number) i2).intValue()))) != Boolean.FALSE) {
                                                return i2;
                                            }
                                            i2 = AddOp.$Mn.apply2(i2, srfi13.Lit1);
                                        } catch (ClassCastException e5) {
                                            throw new WrongType(e5, "string-ref", 2, i2);
                                        }
                                    } catch (ClassCastException e6) {
                                        throw new WrongType(e6, "string-ref", 1, obj5);
                                    }
                                } catch (UnboundLocationException e7) {
                                    e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1182, 9);
                                    throw e7;
                                }
                            } catch (ClassCastException e8) {
                                throw new WrongType(e8, "x", -2, apply22);
                            }
                        }
                    } else if (misc.isProcedure(this.criterion)) {
                        Object i3 = AddOp.$Mn.apply2(end, srfi13.Lit1);
                        while (true) {
                            Object apply23 = Scheme.numGEq.apply2(i3, start);
                            try {
                                boolean x3 = ((Boolean) apply23).booleanValue();
                                if (!x3) {
                                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                                Object obj6 = this.criterion;
                                Object obj7 = this.str;
                                try {
                                    try {
                                        if (applyToArgs2.apply2(obj6, Char.make(strings.stringRef((CharSequence) obj7, ((Number) i3).intValue()))) != Boolean.FALSE) {
                                            return i3;
                                        }
                                        i3 = AddOp.$Mn.apply2(i3, srfi13.Lit1);
                                    } catch (ClassCastException e9) {
                                        throw new WrongType(e9, "string-ref", 2, i3);
                                    }
                                } catch (ClassCastException e10) {
                                    throw new WrongType(e10, "string-ref", 1, obj7);
                                }
                            } catch (ClassCastException e11) {
                                throw new WrongType(e11, "x", -2, apply23);
                            }
                        }
                    } else {
                        return misc.error$V("Second param is neither char-set, char, or predicate procedure.", new Object[]{srfi13.string$Mnindex$Mnright, this.criterion});
                    }
                } catch (UnboundLocationException e12) {
                    e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1179, 5);
                    throw e12;
                }
            }
        }
    }

    public static Object stringIndexRight$V(Object str, Object criterion, Object[] argsArray) {
        frame78 frame782 = new frame78();
        frame782.str = str;
        frame782.criterion = criterion;
        frame782.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame782.lambda$Fn179, frame782.lambda$Fn180);
    }

    /* compiled from: srfi13.scm */
    public class frame79 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn181 = new ModuleMethod(this, 156, (Object) null, 0);
        final ModuleMethod lambda$Fn182 = new ModuleMethod(this, 157, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object str;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 156 ? lambda181() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 157 ? lambda182(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 156) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 157) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda181() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnskip, this.str, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda182(Object start, Object end) {
            if (characters.isChar(this.criterion)) {
                Object i = start;
                while (true) {
                    Object apply2 = Scheme.numLss.apply2(i, end);
                    try {
                        boolean x = ((Boolean) apply2).booleanValue();
                        if (x) {
                            Object obj = this.criterion;
                            try {
                                Char charR = (Char) obj;
                                Object obj2 = this.str;
                                try {
                                    try {
                                        if (!characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())))) {
                                            return i;
                                        }
                                        i = AddOp.$Pl.apply2(i, srfi13.Lit1);
                                    } catch (ClassCastException e) {
                                        throw new WrongType(e, "string-ref", 2, i);
                                    }
                                } catch (ClassCastException e2) {
                                    throw new WrongType(e2, "string-ref", 1, obj2);
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "char=?", 1, obj);
                            }
                        } else if (x) {
                            return Boolean.TRUE;
                        } else {
                            return Boolean.FALSE;
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "x", -2, apply2);
                    }
                }
            } else {
                try {
                    if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                        Object i2 = start;
                        while (true) {
                            Object apply22 = Scheme.numLss.apply2(i2, end);
                            try {
                                boolean x2 = ((Boolean) apply22).booleanValue();
                                if (!x2) {
                                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                                try {
                                    Object obj3 = srfi13.loc$char$Mnset$Mncontains$Qu.get();
                                    Object obj4 = this.criterion;
                                    Object obj5 = this.str;
                                    try {
                                        try {
                                            if (applyToArgs.apply3(obj3, obj4, Char.make(strings.stringRef((CharSequence) obj5, ((Number) i2).intValue()))) == Boolean.FALSE) {
                                                return i2;
                                            }
                                            i2 = AddOp.$Pl.apply2(i2, srfi13.Lit1);
                                        } catch (ClassCastException e5) {
                                            throw new WrongType(e5, "string-ref", 2, i2);
                                        }
                                    } catch (ClassCastException e6) {
                                        throw new WrongType(e6, "string-ref", 1, obj5);
                                    }
                                } catch (UnboundLocationException e7) {
                                    e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1203, 9);
                                    throw e7;
                                }
                            } catch (ClassCastException e8) {
                                throw new WrongType(e8, "x", -2, apply22);
                            }
                        }
                    } else if (misc.isProcedure(this.criterion)) {
                        Object i3 = start;
                        while (true) {
                            Object apply23 = Scheme.numLss.apply2(i3, end);
                            try {
                                boolean x3 = ((Boolean) apply23).booleanValue();
                                if (!x3) {
                                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                                Object obj6 = this.criterion;
                                Object obj7 = this.str;
                                try {
                                    try {
                                        if (applyToArgs2.apply2(obj6, Char.make(strings.stringRef((CharSequence) obj7, ((Number) i3).intValue()))) == Boolean.FALSE) {
                                            return i3;
                                        }
                                        i3 = AddOp.$Pl.apply2(i3, srfi13.Lit1);
                                    } catch (ClassCastException e9) {
                                        throw new WrongType(e9, "string-ref", 2, i3);
                                    }
                                } catch (ClassCastException e10) {
                                    throw new WrongType(e10, "string-ref", 1, obj7);
                                }
                            } catch (ClassCastException e11) {
                                throw new WrongType(e11, "x", -2, apply23);
                            }
                        }
                    } else {
                        return misc.error$V("Second param is neither char-set, char, or predicate procedure.", new Object[]{srfi13.string$Mnskip, this.criterion});
                    }
                } catch (UnboundLocationException e12) {
                    e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1200, 5);
                    throw e12;
                }
            }
        }
    }

    public static Object stringSkip$V(Object str, Object criterion, Object[] argsArray) {
        frame79 frame792 = new frame79();
        frame792.str = str;
        frame792.criterion = criterion;
        frame792.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame792.lambda$Fn181, frame792.lambda$Fn182);
    }

    /* compiled from: srfi13.scm */
    public class frame80 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn183 = new ModuleMethod(this, 158, (Object) null, 0);
        final ModuleMethod lambda$Fn184 = new ModuleMethod(this, 159, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object str;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 158 ? lambda183() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 159 ? lambda184(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 158) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 159) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda183() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnskip$Mnright, this.str, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda184(Object start, Object end) {
            if (characters.isChar(this.criterion)) {
                Object i = AddOp.$Mn.apply2(end, srfi13.Lit1);
                while (true) {
                    Object apply2 = Scheme.numGEq.apply2(i, start);
                    try {
                        boolean x = ((Boolean) apply2).booleanValue();
                        if (x) {
                            Object obj = this.criterion;
                            try {
                                Char charR = (Char) obj;
                                Object obj2 = this.str;
                                try {
                                    try {
                                        if (!characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())))) {
                                            return i;
                                        }
                                        i = AddOp.$Mn.apply2(i, srfi13.Lit1);
                                    } catch (ClassCastException e) {
                                        throw new WrongType(e, "string-ref", 2, i);
                                    }
                                } catch (ClassCastException e2) {
                                    throw new WrongType(e2, "string-ref", 1, obj2);
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "char=?", 1, obj);
                            }
                        } else if (x) {
                            return Boolean.TRUE;
                        } else {
                            return Boolean.FALSE;
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "x", -2, apply2);
                    }
                }
            } else {
                try {
                    if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                        Object i2 = AddOp.$Mn.apply2(end, srfi13.Lit1);
                        while (true) {
                            Object apply22 = Scheme.numGEq.apply2(i2, start);
                            try {
                                boolean x2 = ((Boolean) apply22).booleanValue();
                                if (!x2) {
                                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                                try {
                                    Object obj3 = srfi13.loc$char$Mnset$Mncontains$Qu.get();
                                    Object obj4 = this.criterion;
                                    Object obj5 = this.str;
                                    try {
                                        try {
                                            if (applyToArgs.apply3(obj3, obj4, Char.make(strings.stringRef((CharSequence) obj5, ((Number) i2).intValue()))) == Boolean.FALSE) {
                                                return i2;
                                            }
                                            i2 = AddOp.$Mn.apply2(i2, srfi13.Lit1);
                                        } catch (ClassCastException e5) {
                                            throw new WrongType(e5, "string-ref", 2, i2);
                                        }
                                    } catch (ClassCastException e6) {
                                        throw new WrongType(e6, "string-ref", 1, obj5);
                                    }
                                } catch (UnboundLocationException e7) {
                                    e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1225, 9);
                                    throw e7;
                                }
                            } catch (ClassCastException e8) {
                                throw new WrongType(e8, "x", -2, apply22);
                            }
                        }
                    } else if (misc.isProcedure(this.criterion)) {
                        Object i3 = AddOp.$Mn.apply2(end, srfi13.Lit1);
                        while (true) {
                            Object apply23 = Scheme.numGEq.apply2(i3, start);
                            try {
                                boolean x3 = ((Boolean) apply23).booleanValue();
                                if (!x3) {
                                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                                Object obj6 = this.criterion;
                                Object obj7 = this.str;
                                try {
                                    try {
                                        if (applyToArgs2.apply2(obj6, Char.make(strings.stringRef((CharSequence) obj7, ((Number) i3).intValue()))) == Boolean.FALSE) {
                                            return i3;
                                        }
                                        i3 = AddOp.$Mn.apply2(i3, srfi13.Lit1);
                                    } catch (ClassCastException e9) {
                                        throw new WrongType(e9, "string-ref", 2, i3);
                                    }
                                } catch (ClassCastException e10) {
                                    throw new WrongType(e10, "string-ref", 1, obj7);
                                }
                            } catch (ClassCastException e11) {
                                throw new WrongType(e11, "x", -2, apply23);
                            }
                        }
                    } else {
                        return misc.error$V("CRITERION param is neither char-set or char.", new Object[]{srfi13.string$Mnskip$Mnright, this.criterion});
                    }
                } catch (UnboundLocationException e12) {
                    e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1222, 5);
                    throw e12;
                }
            }
        }
    }

    public static Object stringSkipRight$V(Object str, Object criterion, Object[] argsArray) {
        frame80 frame802 = new frame80();
        frame802.str = str;
        frame802.criterion = criterion;
        frame802.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame802.lambda$Fn183, frame802.lambda$Fn184);
    }

    /* compiled from: srfi13.scm */
    public class frame81 extends ModuleBody {
        Object criterion;
        final ModuleMethod lambda$Fn185 = new ModuleMethod(this, ComponentConstants.TEXTBOX_PREFERRED_WIDTH, (Object) null, 0);
        final ModuleMethod lambda$Fn186 = new ModuleMethod(this, 161, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 160 ? lambda185() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 161 ? lambda186(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 160) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 161) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda185() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mncount, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda186(Object start, Object end) {
            if (characters.isChar(this.criterion)) {
                Object count = srfi13.Lit0;
                Object i = start;
                while (Scheme.numGEq.apply2(i, end) == Boolean.FALSE) {
                    Object i2 = AddOp.$Pl.apply2(i, srfi13.Lit1);
                    Object obj = this.criterion;
                    try {
                        Char charR = (Char) obj;
                        Object obj2 = this.s;
                        try {
                            try {
                                if (characters.isChar$Eq(charR, Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())))) {
                                    count = AddOp.$Pl.apply2(count, srfi13.Lit1);
                                }
                                i = i2;
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "string-ref", 2, i);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "string-ref", 1, obj2);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "char=?", 1, obj);
                    }
                }
                return count;
            }
            try {
                if (Scheme.applyToArgs.apply2(srfi13.loc$char$Mnset$Qu.get(), this.criterion) != Boolean.FALSE) {
                    Object count2 = srfi13.Lit0;
                    Object i3 = start;
                    while (Scheme.numGEq.apply2(i3, end) == Boolean.FALSE) {
                        Object i4 = AddOp.$Pl.apply2(i3, srfi13.Lit1);
                        ApplyToArgs applyToArgs = Scheme.applyToArgs;
                        try {
                            Object obj3 = srfi13.loc$char$Mnset$Mncontains$Qu.get();
                            Object obj4 = this.criterion;
                            Object obj5 = this.s;
                            try {
                                try {
                                    if (applyToArgs.apply3(obj3, obj4, Char.make(strings.stringRef((CharSequence) obj5, ((Number) i3).intValue()))) != Boolean.FALSE) {
                                        count2 = AddOp.$Pl.apply2(count2, srfi13.Lit1);
                                    }
                                    i3 = i4;
                                } catch (ClassCastException e4) {
                                    throw new WrongType(e4, "string-ref", 2, i3);
                                }
                            } catch (ClassCastException e5) {
                                throw new WrongType(e5, "string-ref", 1, obj5);
                            }
                        } catch (UnboundLocationException e6) {
                            e6.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1248, 16);
                            throw e6;
                        }
                    }
                    return count2;
                } else if (misc.isProcedure(this.criterion)) {
                    Object count3 = srfi13.Lit0;
                    Object i5 = start;
                    while (Scheme.numGEq.apply2(i5, end) == Boolean.FALSE) {
                        Object i6 = AddOp.$Pl.apply2(i5, srfi13.Lit1);
                        ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                        Object obj6 = this.criterion;
                        Object obj7 = this.s;
                        try {
                            try {
                                if (applyToArgs2.apply2(obj6, Char.make(strings.stringRef((CharSequence) obj7, ((Number) i5).intValue()))) != Boolean.FALSE) {
                                    count3 = AddOp.$Pl.apply2(count3, srfi13.Lit1);
                                }
                                i5 = i6;
                            } catch (ClassCastException e7) {
                                throw new WrongType(e7, "string-ref", 2, i5);
                            }
                        } catch (ClassCastException e8) {
                            throw new WrongType(e8, "string-ref", 1, obj7);
                        }
                    }
                    return count3;
                } else {
                    return misc.error$V("CRITERION param is neither char-set or char.", new Object[]{srfi13.string$Mncount, this.criterion});
                }
            } catch (UnboundLocationException e9) {
                e9.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1246, 5);
                throw e9;
            }
        }
    }

    public static Object stringCount$V(Object s, Object criterion, Object[] argsArray) {
        frame81 frame812 = new frame81();
        frame812.s = s;
        frame812.criterion = criterion;
        frame812.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame812.lambda$Fn185, frame812.lambda$Fn186);
    }

    public static Object stringFill$Ex$V(Object s, Object obj, Object[] argsArray) {
        frame82 frame822 = new frame82();
        frame822.s = s;
        frame822.f2char = obj;
        frame822.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), characters.char$Qu, frame822.f2char, string$Mnfill$Ex);
            return call_with_values.callWithValues(frame822.lambda$Fn187, frame822.lambda$Fn188);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1270, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame82 extends ModuleBody {

        /* renamed from: char  reason: not valid java name */
        Object f2char;
        final ModuleMethod lambda$Fn187 = new ModuleMethod(this, 162, (Object) null, 0);
        final ModuleMethod lambda$Fn188 = new ModuleMethod(this, 163, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 162 ? lambda187() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 163 ? lambda188(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 162) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 163) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda187() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnfill$Ex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda188(Object start, Object end) {
            Object i = AddOp.$Mn.apply2(end, srfi13.Lit1);
            while (Scheme.numLss.apply2(i, start) == Boolean.FALSE) {
                Object obj = this.s;
                try {
                    CharSeq charSeq = (CharSeq) obj;
                    try {
                        int intValue = ((Number) i).intValue();
                        Object obj2 = this.f2char;
                        try {
                            strings.stringSet$Ex(charSeq, intValue, ((Char) obj2).charValue());
                            i = AddOp.$Mn.apply2(i, srfi13.Lit1);
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-set!", 3, obj2);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-set!", 2, i);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "string-set!", 1, obj);
                }
            }
            return Values.empty;
        }
    }

    public static Object stringCopy$Ex(Object to, int tstart, CharSequence from, int fstart, int fend) {
        $PcCheckBounds(string$Mncopy$Ex, from, fstart, fend);
        try {
            $PcCheckSubstringSpec(string$Mncopy$Ex, (CharSequence) to, tstart, (fend - fstart) + tstart);
            try {
                return $PcStringCopy$Ex((CharSequence) to, tstart, from, fstart, fend);
            } catch (ClassCastException e) {
                throw new WrongType(e, "%string-copy!", 0, to);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "%check-substring-spec", 1, to);
        }
    }

    public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
        switch (moduleMethod.selector) {
            case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SOURCE:
                return stringParseStart$PlEnd(obj, obj2, obj3);
            case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_DURATION:
                return stringParseFinalStart$PlEnd(obj, obj2, obj3);
            case 197:
                return isSubstringSpecOk(obj, obj2, obj3) ? Boolean.TRUE : Boolean.FALSE;
            case ErrorMessages.ERROR_CAMERA_NO_IMAGE_RETURNED:
                try {
                    try {
                        try {
                            return $PcSubstring$SlShared((CharSequence) obj, ((Number) obj2).intValue(), ((Number) obj3).intValue());
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%substring/shared", 3, obj3);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%substring/shared", 2, obj2);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "%substring/shared", 1, obj);
                }
            case 277:
                return $PcStringTitlecase$Ex(obj, obj2, obj3);
            case 299:
                try {
                    try {
                        return stringCopy$Ex(obj, ((Number) obj2).intValue(), (CharSequence) obj3);
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "string-copy!", 3, obj3);
                    }
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "string-copy!", 2, obj2);
                }
            default:
                return super.apply3(moduleMethod, obj, obj2, obj3);
        }
    }

    public static Object $PcStringCopy$Ex(CharSequence to, int tstart, CharSequence from, int fstart, int fend) {
        if (fstart > tstart) {
            int j = tstart;
            int i = fstart;
            while (i < fend) {
                try {
                    strings.stringSet$Ex((CharSeq) to, j, strings.stringRef(from, i));
                    j++;
                    i++;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-set!", 1, (Object) to);
                }
            }
            return Values.empty;
        }
        int j2 = (tstart - 1) + (fend - fstart);
        int i2 = fend - 1;
        while (i2 >= fstart) {
            try {
                strings.stringSet$Ex((CharSeq) to, j2, strings.stringRef(from, i2));
                j2--;
                i2--;
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-set!", 1, (Object) to);
            }
        }
        return Values.empty;
    }

    /* compiled from: srfi13.scm */
    public class frame83 extends ModuleBody {
        final ModuleMethod lambda$Fn189 = new ModuleMethod(this, 166, (Object) null, 0);
        final ModuleMethod lambda$Fn190 = new ModuleMethod(this, 167, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object pattern;
        Object text;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 166 ? lambda189() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 167 ? lambda190(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 166) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 167) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda189() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mncontains, this.text, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda190(Object rest, Object tStart, Object tEnd) {
            frame84 frame84 = new frame84();
            frame84.staticLink = this;
            frame84.rest = rest;
            frame84.t$Mnstart = tStart;
            frame84.t$Mnend = tEnd;
            return call_with_values.callWithValues(frame84.lambda$Fn191, frame84.lambda$Fn192);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame84 extends ModuleBody {
        final ModuleMethod lambda$Fn191 = new ModuleMethod(this, 164, (Object) null, 0);
        final ModuleMethod lambda$Fn192 = new ModuleMethod(this, 165, (Object) null, 8194);
        Object rest;
        frame83 staticLink;
        Object t$Mnend;
        Object t$Mnstart;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 164 ? lambda191() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 165 ? lambda192(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 164) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 165) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda191() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mncontains, this.staticLink.pattern, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda192(Object p$Mnstart, Object p$Mnend) {
            return srfi13.$PcKmpSearch(this.staticLink.pattern, this.staticLink.text, characters.char$Eq$Qu, p$Mnstart, p$Mnend, this.t$Mnstart, this.t$Mnend);
        }
    }

    public static Object stringContains$V(Object text, Object pattern, Object[] argsArray) {
        frame83 frame832 = new frame83();
        frame832.text = text;
        frame832.pattern = pattern;
        frame832.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame832.lambda$Fn189, frame832.lambda$Fn190);
    }

    /* compiled from: srfi13.scm */
    public class frame85 extends ModuleBody {
        final ModuleMethod lambda$Fn193 = new ModuleMethod(this, 170, (Object) null, 0);
        final ModuleMethod lambda$Fn194 = new ModuleMethod(this, 171, (Object) null, 12291);
        LList maybe$Mnstarts$Plends;
        Object pattern;
        Object text;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 170 ? lambda193() : super.apply0(moduleMethod);
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 171 ? lambda194(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 170) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 171) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda193() {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mncontains$Mnci, this.text, this.maybe$Mnstarts$Plends);
        }

        /* access modifiers changed from: package-private */
        public Object lambda194(Object rest, Object tStart, Object tEnd) {
            frame86 frame86 = new frame86();
            frame86.staticLink = this;
            frame86.rest = rest;
            frame86.t$Mnstart = tStart;
            frame86.t$Mnend = tEnd;
            return call_with_values.callWithValues(frame86.lambda$Fn195, frame86.lambda$Fn196);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame86 extends ModuleBody {
        final ModuleMethod lambda$Fn195 = new ModuleMethod(this, 168, (Object) null, 0);
        final ModuleMethod lambda$Fn196 = new ModuleMethod(this, 169, (Object) null, 8194);
        Object rest;
        frame85 staticLink;
        Object t$Mnend;
        Object t$Mnstart;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 168 ? lambda195() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 169 ? lambda196(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 168) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 169) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda195() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mncontains$Mnci, this.staticLink.pattern, this.rest);
        }

        /* access modifiers changed from: package-private */
        public Object lambda196(Object p$Mnstart, Object p$Mnend) {
            return srfi13.$PcKmpSearch(this.staticLink.pattern, this.staticLink.text, unicode.char$Mnci$Eq$Qu, p$Mnstart, p$Mnend, this.t$Mnstart, this.t$Mnend);
        }
    }

    public static Object stringContainsCi$V(Object text, Object pattern, Object[] argsArray) {
        frame85 frame852 = new frame85();
        frame852.text = text;
        frame852.pattern = pattern;
        frame852.maybe$Mnstarts$Plends = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame852.lambda$Fn193, frame852.lambda$Fn194);
    }

    public static Object $PcKmpSearch(Object pattern, Object text, Object c$Eq, Object p$Mnstart, Object p$Mnend, Object t$Mnstart, Object t$Mnend) {
        Object plen = AddOp.$Mn.apply2(p$Mnend, p$Mnstart);
        Object rv = makeKmpRestartVector$V(pattern, new Object[]{c$Eq, p$Mnstart, p$Mnend});
        Number pi = Lit0;
        Object apply2 = AddOp.$Mn.apply2(t$Mnend, t$Mnstart);
        Object ti = t$Mnstart;
        Object obj = plen;
        while (true) {
            Number number = pi;
            if (Scheme.numEqu.apply2(number, plen) != Boolean.FALSE) {
                return AddOp.$Mn.apply2(ti, plen);
            }
            Object apply22 = Scheme.numLEq.apply2(obj, apply2);
            try {
                boolean x = ((Boolean) apply22).booleanValue();
                if (!x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                ApplyToArgs applyToArgs = Scheme.applyToArgs;
                try {
                    try {
                        Char make = Char.make(strings.stringRef((CharSequence) text, ((Number) ti).intValue()));
                        try {
                            CharSequence charSequence = (CharSequence) pattern;
                            Object apply23 = AddOp.$Pl.apply2(p$Mnstart, number);
                            try {
                                if (applyToArgs.apply3(c$Eq, make, Char.make(strings.stringRef(charSequence, ((Number) apply23).intValue()))) != Boolean.FALSE) {
                                    ti = AddOp.$Pl.apply2(Lit1, ti);
                                    pi = AddOp.$Pl.apply2(Lit1, number);
                                    apply2 = AddOp.$Mn.apply2(apply2, Lit1);
                                    obj = AddOp.$Mn.apply2(obj, Lit1);
                                } else {
                                    try {
                                        try {
                                            pi = vectors.vectorRef((FVector) rv, number.intValue());
                                            if (Scheme.numEqu.apply2(pi, Lit13) != Boolean.FALSE) {
                                                ti = AddOp.$Pl.apply2(ti, Lit1);
                                                pi = Lit0;
                                                apply2 = AddOp.$Mn.apply2(apply2, Lit1);
                                                obj = plen;
                                            } else {
                                                obj = AddOp.$Mn.apply2(plen, pi);
                                            }
                                        } catch (ClassCastException e) {
                                            throw new WrongType(e, "vector-ref", 2, (Object) number);
                                        }
                                    } catch (ClassCastException e2) {
                                        throw new WrongType(e2, "vector-ref", 1, rv);
                                    }
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "string-ref", 2, apply23);
                            }
                        } catch (ClassCastException e4) {
                            throw new WrongType(e4, "string-ref", 1, pattern);
                        }
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "string-ref", 2, ti);
                    }
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "string-ref", 1, text);
                }
            } catch (ClassCastException e7) {
                throw new WrongType(e7, "x", -2, apply22);
            }
        }
    }

    public static Object makeKmpRestartVector$V(Object pattern, Object[] argsArray) {
        frame87 frame872 = new frame87();
        frame872.pattern = pattern;
        LList maybe$Mnc$Eq$Plstart$Plend = LList.makeList(argsArray, 0);
        ApplyToArgs applyToArgs = Scheme.applyToArgs;
        try {
            Object obj = loc$let$Mnoptionals$St.get();
            try {
                try {
                    try {
                        try {
                            Object apply2 = Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply3(loc$c$Eq.get(), characters.char$Eq$Qu, misc.isProcedure(loc$c$Eq.get()) ? Boolean.TRUE : Boolean.FALSE), Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply2(loc$start.get(), loc$end.get()), frame872.lambda$Fn197));
                            try {
                                try {
                                    Object rvlen = AddOp.$Mn.apply2(loc$end.get(), loc$start.get());
                                    try {
                                        FVector rv = vectors.makeVector(((Number) rvlen).intValue(), Lit13);
                                        if (Scheme.numGrt.apply2(rvlen, Lit0) != Boolean.FALSE) {
                                            Object rvlen$Mn1 = AddOp.$Mn.apply2(rvlen, Lit1);
                                            Object obj2 = frame872.pattern;
                                            try {
                                                CharSequence charSequence = (CharSequence) obj2;
                                                try {
                                                    Object obj3 = loc$start.get();
                                                    try {
                                                        char c0 = strings.stringRef(charSequence, ((Number) obj3).intValue());
                                                        Object obj4 = Lit0;
                                                        Object j = Lit13;
                                                        try {
                                                            Object obj5 = loc$start.get();
                                                            while (true) {
                                                                Object obj6 = obj5;
                                                                if (Scheme.numLss.apply2(obj4, rvlen$Mn1) == Boolean.FALSE) {
                                                                    break;
                                                                }
                                                                while (true) {
                                                                    if (Scheme.numEqu.apply2(j, Lit13) != Boolean.FALSE) {
                                                                        Object i1 = AddOp.$Pl.apply2(Lit1, obj4);
                                                                        ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                                                                        try {
                                                                            Object obj7 = loc$c$Eq.get();
                                                                            Object obj8 = frame872.pattern;
                                                                            try {
                                                                                CharSequence charSequence2 = (CharSequence) obj8;
                                                                                Object apply22 = AddOp.$Pl.apply2(obj6, Lit1);
                                                                                try {
                                                                                    if (applyToArgs2.apply3(obj7, Char.make(strings.stringRef(charSequence2, ((Number) apply22).intValue())), Char.make(c0)) == Boolean.FALSE) {
                                                                                        try {
                                                                                            vectors.vectorSet$Ex(rv, ((Number) i1).intValue(), Lit0);
                                                                                        } catch (ClassCastException e) {
                                                                                            throw new WrongType(e, "vector-set!", 2, i1);
                                                                                        }
                                                                                    }
                                                                                    j = Lit0;
                                                                                    obj5 = AddOp.$Pl.apply2(obj6, Lit1);
                                                                                    obj4 = i1;
                                                                                } catch (ClassCastException e2) {
                                                                                    throw new WrongType(e2, "string-ref", 2, apply22);
                                                                                }
                                                                            } catch (ClassCastException e3) {
                                                                                throw new WrongType(e3, "string-ref", 1, obj8);
                                                                            }
                                                                        } catch (UnboundLocationException e4) {
                                                                            e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1418, 18);
                                                                            throw e4;
                                                                        }
                                                                    } else {
                                                                        ApplyToArgs applyToArgs3 = Scheme.applyToArgs;
                                                                        try {
                                                                            Object obj9 = loc$c$Eq.get();
                                                                            Object obj10 = frame872.pattern;
                                                                            try {
                                                                                try {
                                                                                    Char make = Char.make(strings.stringRef((CharSequence) obj10, ((Number) obj6).intValue()));
                                                                                    Object obj11 = frame872.pattern;
                                                                                    try {
                                                                                        CharSequence charSequence3 = (CharSequence) obj11;
                                                                                        try {
                                                                                            Object apply23 = AddOp.$Pl.apply2(j, loc$start.get());
                                                                                            try {
                                                                                                if (applyToArgs3.apply3(obj9, make, Char.make(strings.stringRef(charSequence3, ((Number) apply23).intValue()))) != Boolean.FALSE) {
                                                                                                    Object i12 = AddOp.$Pl.apply2(Lit1, obj4);
                                                                                                    Object j1 = AddOp.$Pl.apply2(Lit1, j);
                                                                                                    try {
                                                                                                        vectors.vectorSet$Ex(rv, ((Number) i12).intValue(), j1);
                                                                                                        obj5 = AddOp.$Pl.apply2(obj6, Lit1);
                                                                                                        j = j1;
                                                                                                        obj4 = i12;
                                                                                                        break;
                                                                                                    } catch (ClassCastException e5) {
                                                                                                        throw new WrongType(e5, "vector-set!", 2, i12);
                                                                                                    }
                                                                                                } else {
                                                                                                    try {
                                                                                                        j = vectors.vectorRef(rv, ((Number) j).intValue());
                                                                                                    } catch (ClassCastException e6) {
                                                                                                        throw new WrongType(e6, "vector-ref", 2, j);
                                                                                                    }
                                                                                                }
                                                                                            } catch (ClassCastException e7) {
                                                                                                throw new WrongType(e7, "string-ref", 2, apply23);
                                                                                            }
                                                                                        } catch (UnboundLocationException e8) {
                                                                                            e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1422, 59);
                                                                                            throw e8;
                                                                                        }
                                                                                    } catch (ClassCastException e9) {
                                                                                        throw new WrongType(e9, "string-ref", 1, obj11);
                                                                                    }
                                                                                } catch (ClassCastException e10) {
                                                                                    throw new WrongType(e10, "string-ref", 2, obj6);
                                                                                }
                                                                            } catch (ClassCastException e11) {
                                                                                throw new WrongType(e11, "string-ref", 1, obj10);
                                                                            }
                                                                        } catch (UnboundLocationException e12) {
                                                                            e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1422, 7);
                                                                            throw e12;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } catch (UnboundLocationException e13) {
                                                            e13.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1410, 6);
                                                            throw e13;
                                                        }
                                                    } catch (ClassCastException e14) {
                                                        throw new WrongType(e14, "string-ref", 2, obj3);
                                                    }
                                                } catch (UnboundLocationException e15) {
                                                    e15.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1406, 27);
                                                    throw e15;
                                                }
                                            } catch (ClassCastException e16) {
                                                throw new WrongType(e16, "string-ref", 1, obj2);
                                            }
                                        }
                                        return applyToArgs.apply4(obj, maybe$Mnc$Eq$Plstart$Plend, apply2, rv);
                                    } catch (ClassCastException e17) {
                                        throw new WrongType(e17, "make-vector", 1, rvlen);
                                    }
                                } catch (UnboundLocationException e18) {
                                    e18.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_BAD_VALUE_FOR_VERTICAL_ALIGNMENT, 26);
                                    throw e18;
                                }
                            } catch (UnboundLocationException e19) {
                                e19.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_BAD_VALUE_FOR_VERTICAL_ALIGNMENT, 22);
                                throw e19;
                            }
                        } catch (UnboundLocationException e20) {
                            e20.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1399, 14);
                            throw e20;
                        }
                    } catch (UnboundLocationException e21) {
                        e21.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1399, 7);
                        throw e21;
                    }
                } catch (UnboundLocationException e22) {
                    e22.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1398, 43);
                    throw e22;
                }
            } catch (UnboundLocationException e23) {
                e23.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1398, 20);
                throw e23;
            }
        } catch (UnboundLocationException e24) {
            e24.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1397, 3);
            throw e24;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame87 extends ModuleBody {
        final ModuleMethod lambda$Fn197;
        Object pattern;

        public frame87() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 172, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1399");
            this.lambda$Fn197 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 172 ? lambda197(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda197(Object args) {
            return srfi13.stringParseStart$PlEnd(srfi13.make$Mnkmp$Mnrestart$Mnvector, this.pattern, args);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 172) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object kmpStep(Object pat, Object rv, Object c, Object i, Object c$Eq, Object p$Mnstart) {
        do {
            ApplyToArgs applyToArgs = Scheme.applyToArgs;
            try {
                CharSequence charSequence = (CharSequence) pat;
                Object apply2 = AddOp.$Pl.apply2(i, p$Mnstart);
                try {
                    if (applyToArgs.apply3(c$Eq, c, Char.make(strings.stringRef(charSequence, ((Number) apply2).intValue()))) != Boolean.FALSE) {
                        return AddOp.$Pl.apply2(i, Lit1);
                    }
                    try {
                        try {
                            i = vectors.vectorRef((FVector) rv, ((Number) i).intValue());
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "vector-ref", 2, i);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "vector-ref", 1, rv);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "string-ref", 2, apply2);
                }
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "string-ref", 1, pat);
            }
        } while (Scheme.numEqu.apply2(i, Lit13) == Boolean.FALSE);
        return Lit0;
    }

    public static Object stringKmpPartialSearch$V(Object pat, Object rv, Object s, Object i, Object[] argsArray) {
        Object obj;
        frame88 frame882 = new frame88();
        frame882.s = s;
        LList c$Eq$Plp$Mnstart$Pls$Mnstart$Pls$Mnend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), vectors.vector$Qu, rv, string$Mnkmp$Mnpartial$Mnsearch);
            ApplyToArgs applyToArgs = Scheme.applyToArgs;
            try {
                Object obj2 = loc$let$Mnoptionals$St.get();
                ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
                try {
                    try {
                        Object apply3 = Scheme.applyToArgs.apply3(loc$c$Eq.get(), characters.char$Eq$Qu, misc.isProcedure(loc$c$Eq.get()) ? Boolean.TRUE : Boolean.FALSE);
                        ApplyToArgs applyToArgs3 = Scheme.applyToArgs;
                        try {
                            Object obj3 = loc$p$Mnstart.get();
                            IntNum intNum = Lit0;
                            try {
                                boolean x = numbers.isInteger(loc$p$Mnstart.get());
                                if (x) {
                                    try {
                                        boolean x2 = numbers.isExact(loc$p$Mnstart.get());
                                        if (x2) {
                                            try {
                                                obj = Scheme.numLEq.apply2(Lit0, loc$p$Mnstart.get());
                                            } catch (UnboundLocationException e) {
                                                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1467, 64);
                                                throw e;
                                            }
                                        } else {
                                            obj = x2 ? Boolean.TRUE : Boolean.FALSE;
                                        }
                                    } catch (UnboundLocationException e2) {
                                        e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1467, 49);
                                        throw e2;
                                    }
                                } else {
                                    obj = x ? Boolean.TRUE : Boolean.FALSE;
                                }
                                try {
                                    try {
                                        Object apply32 = applyToArgs2.apply3(apply3, applyToArgs3.apply3(obj3, intNum, obj), Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply2(loc$s$Mnstart.get(), loc$s$Mnend.get()), frame882.lambda$Fn198));
                                        try {
                                            frame882.patlen = vectors.vectorLength((FVector) rv);
                                            try {
                                                Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), frame882.lambda$Fn199, i, string$Mnkmp$Mnpartial$Mnsearch);
                                                try {
                                                    Object si = loc$s$Mnstart.get();
                                                    Object vi = i;
                                                    while (true) {
                                                        if (Scheme.numEqu.apply2(vi, Integer.valueOf(frame882.patlen)) != Boolean.FALSE) {
                                                            vi = AddOp.$Mn.apply1(si);
                                                            break;
                                                        }
                                                        try {
                                                            if (Scheme.numEqu.apply2(si, loc$s$Mnend.get()) != Boolean.FALSE) {
                                                                break;
                                                            }
                                                            Object obj4 = frame882.s;
                                                            try {
                                                                try {
                                                                    char c = strings.stringRef((CharSequence) obj4, ((Number) si).intValue());
                                                                    si = AddOp.$Pl.apply2(si, Lit1);
                                                                    while (true) {
                                                                        ApplyToArgs applyToArgs4 = Scheme.applyToArgs;
                                                                        try {
                                                                            Object obj5 = loc$c$Eq.get();
                                                                            Char make = Char.make(c);
                                                                            try {
                                                                                CharSequence charSequence = (CharSequence) pat;
                                                                                try {
                                                                                    Object apply2 = AddOp.$Pl.apply2(vi, loc$p$Mnstart.get());
                                                                                    try {
                                                                                        if (applyToArgs4.apply3(obj5, make, Char.make(strings.stringRef(charSequence, ((Number) apply2).intValue()))) != Boolean.FALSE) {
                                                                                            vi = AddOp.$Pl.apply2(vi, Lit1);
                                                                                            break;
                                                                                        }
                                                                                        try {
                                                                                            try {
                                                                                                vi = vectors.vectorRef((FVector) rv, ((Number) vi).intValue());
                                                                                                if (Scheme.numEqu.apply2(vi, Lit13) != Boolean.FALSE) {
                                                                                                    vi = Lit0;
                                                                                                    break;
                                                                                                }
                                                                                            } catch (ClassCastException e3) {
                                                                                                throw new WrongType(e3, "vector-ref", 2, vi);
                                                                                            }
                                                                                        } catch (ClassCastException e4) {
                                                                                            throw new WrongType(e4, "vector-ref", 1, rv);
                                                                                        }
                                                                                    } catch (ClassCastException e5) {
                                                                                        throw new WrongType(e5, "string-ref", 2, apply2);
                                                                                    }
                                                                                } catch (UnboundLocationException e6) {
                                                                                    e6.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1484, 42);
                                                                                    throw e6;
                                                                                }
                                                                            } catch (ClassCastException e7) {
                                                                                throw new WrongType(e7, "string-ref", 1, pat);
                                                                            }
                                                                        } catch (UnboundLocationException e8) {
                                                                            e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1484, 14);
                                                                            throw e8;
                                                                        }
                                                                    }
                                                                } catch (ClassCastException e9) {
                                                                    throw new WrongType(e9, "string-ref", 2, si);
                                                                }
                                                            } catch (ClassCastException e10) {
                                                                throw new WrongType(e10, "string-ref", 1, obj4);
                                                            }
                                                        } catch (UnboundLocationException e11) {
                                                            e11.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1479, 15);
                                                            throw e11;
                                                        }
                                                    }
                                                    return applyToArgs.apply4(obj2, c$Eq$Plp$Mnstart$Pls$Mnstart$Pls$Mnend, apply32, vi);
                                                } catch (UnboundLocationException e12) {
                                                    e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1476, 7);
                                                    throw e12;
                                                }
                                            } catch (UnboundLocationException e13) {
                                                e13.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1472, 7);
                                                throw e13;
                                            }
                                        } catch (ClassCastException e14) {
                                            throw new WrongType(e14, "vector-length", 1, rv);
                                        }
                                    } catch (UnboundLocationException e15) {
                                        e15.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1468, 16);
                                        throw e15;
                                    }
                                } catch (UnboundLocationException e16) {
                                    e16.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1468, 7);
                                    throw e16;
                                }
                            } catch (UnboundLocationException e17) {
                                e17.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1467, 32);
                                throw e17;
                            }
                        } catch (UnboundLocationException e18) {
                            e18.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1467, 6);
                            throw e18;
                        }
                    } catch (UnboundLocationException e19) {
                        e19.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1466, 34);
                        throw e19;
                    }
                } catch (UnboundLocationException e20) {
                    e20.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1466, 6);
                    throw e20;
                }
            } catch (UnboundLocationException e21) {
                e21.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1465, 3);
                throw e21;
            }
        } catch (UnboundLocationException e22) {
            e22.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1464, 3);
            throw e22;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame88 extends ModuleBody {
        final ModuleMethod lambda$Fn198;
        final ModuleMethod lambda$Fn199;
        int patlen;
        Object s;

        public frame88() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 173, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1468");
            this.lambda$Fn198 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 174, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1472");
            this.lambda$Fn199 = moduleMethod2;
        }

        /* access modifiers changed from: package-private */
        public Object lambda198(Object args) {
            return srfi13.stringParseStart$PlEnd(srfi13.string$Mnkmp$Mnpartial$Mnsearch, this.s, args);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 173:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 174:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 173:
                    return lambda198(obj);
                case 174:
                    return lambda199(obj) ? Boolean.TRUE : Boolean.FALSE;
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean lambda199(Object i) {
            boolean x = numbers.isInteger(i);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(i);
            if (!x2) {
                return x2;
            }
            Object apply2 = Scheme.numLEq.apply2(srfi13.Lit0, i);
            try {
                boolean x3 = ((Boolean) apply2).booleanValue();
                return x3 ? ((Boolean) Scheme.numLss.apply2(i, Integer.valueOf(this.patlen))).booleanValue() : x3;
            } catch (ClassCastException e) {
                throw new WrongType(e, "x", -2, apply2);
            }
        }
    }

    public static boolean isStringNull(Object s) {
        try {
            return numbers.isZero(Integer.valueOf(strings.stringLength((CharSequence) s)));
        } catch (ClassCastException e) {
            throw new WrongType(e, "string-length", 1, s);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame89 extends ModuleBody {
        final ModuleMethod lambda$Fn200 = new ModuleMethod(this, 175, (Object) null, 0);
        final ModuleMethod lambda$Fn201 = new ModuleMethod(this, 176, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 175 ? lambda200() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 176 ? lambda201(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 175) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 176) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda200() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnreverse, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda201(Object start, Object end) {
            Object len = AddOp.$Mn.apply2(end, start);
            try {
                CharSequence ans = strings.makeString(((Number) len).intValue());
                Object apply2 = AddOp.$Mn.apply2(len, srfi13.Lit1);
                Object i = start;
                while (Scheme.numLss.apply2(apply2, srfi13.Lit0) == Boolean.FALSE) {
                    try {
                        CharSeq charSeq = (CharSeq) ans;
                        try {
                            int intValue = ((Number) apply2).intValue();
                            Object obj = this.s;
                            try {
                                try {
                                    strings.stringSet$Ex(charSeq, intValue, strings.stringRef((CharSequence) obj, ((Number) i).intValue()));
                                    i = AddOp.$Pl.apply2(i, srfi13.Lit1);
                                    apply2 = AddOp.$Mn.apply2(apply2, srfi13.Lit1);
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, i);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, obj);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "string-set!", 2, apply2);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "string-set!", 1, (Object) ans);
                    }
                }
                return ans;
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "make-string", 1, len);
            }
        }
    }

    public static Object stringReverse$V(Object s, Object[] argsArray) {
        frame89 frame892 = new frame89();
        frame892.s = s;
        frame892.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame892.lambda$Fn200, frame892.lambda$Fn201);
    }

    /* compiled from: srfi13.scm */
    public class frame90 extends ModuleBody {
        final ModuleMethod lambda$Fn202 = new ModuleMethod(this, 177, (Object) null, 0);
        final ModuleMethod lambda$Fn203 = new ModuleMethod(this, 178, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 177 ? lambda202() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 178 ? lambda203(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 177) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 178) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda202() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnreverse$Ex, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda203(Object start, Object end) {
            Object i = AddOp.$Mn.apply2(end, srfi13.Lit1);
            Object obj = start;
            while (Scheme.numLEq.apply2(i, obj) == Boolean.FALSE) {
                Object obj2 = this.s;
                try {
                    try {
                        char ci = strings.stringRef((CharSequence) obj2, ((Number) i).intValue());
                        Object obj3 = this.s;
                        try {
                            CharSeq charSeq = (CharSeq) obj3;
                            try {
                                int intValue = ((Number) i).intValue();
                                Object obj4 = this.s;
                                try {
                                    try {
                                        strings.stringSet$Ex(charSeq, intValue, strings.stringRef((CharSequence) obj4, ((Number) obj).intValue()));
                                        Object obj5 = this.s;
                                        try {
                                            try {
                                                strings.stringSet$Ex((CharSeq) obj5, ((Number) obj).intValue(), ci);
                                                i = AddOp.$Mn.apply2(i, srfi13.Lit1);
                                                obj = AddOp.$Pl.apply2(obj, srfi13.Lit1);
                                            } catch (ClassCastException e) {
                                                throw new WrongType(e, "string-set!", 2, obj);
                                            }
                                        } catch (ClassCastException e2) {
                                            throw new WrongType(e2, "string-set!", 1, obj5);
                                        }
                                    } catch (ClassCastException e3) {
                                        throw new WrongType(e3, "string-ref", 2, obj);
                                    }
                                } catch (ClassCastException e4) {
                                    throw new WrongType(e4, "string-ref", 1, obj4);
                                }
                            } catch (ClassCastException e5) {
                                throw new WrongType(e5, "string-set!", 2, i);
                            }
                        } catch (ClassCastException e6) {
                            throw new WrongType(e6, "string-set!", 1, obj3);
                        }
                    } catch (ClassCastException e7) {
                        throw new WrongType(e7, "string-ref", 2, i);
                    }
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "string-ref", 1, obj2);
                }
            }
            return Values.empty;
        }
    }

    public static Object stringReverse$Ex$V(Object s, Object[] argsArray) {
        frame90 frame902 = new frame90();
        frame902.s = s;
        frame902.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame902.lambda$Fn202, frame902.lambda$Fn203);
    }

    public static CharSequence reverseList$To$String(Object clist) {
        try {
            int len = lists.length((LList) clist);
            CharSequence s = strings.makeString(len);
            Object valueOf = Integer.valueOf(len - 1);
            while (lists.isPair(clist)) {
                try {
                    CharSeq charSeq = (CharSeq) s;
                    try {
                        int intValue = ((Number) valueOf).intValue();
                        Object apply1 = lists.car.apply1(clist);
                        try {
                            strings.stringSet$Ex(charSeq, intValue, ((Char) apply1).charValue());
                            valueOf = AddOp.$Mn.apply2(valueOf, Lit1);
                            clist = lists.cdr.apply1(clist);
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "string-set!", 3, apply1);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "string-set!", 2, valueOf);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "string-set!", 1, (Object) s);
                }
            }
            return s;
        } catch (ClassCastException e4) {
            throw new WrongType(e4, PropertyTypeConstants.PROPERTY_TYPE_LENGTH, 1, clist);
        }
    }

    /* compiled from: srfi13.scm */
    public class frame91 extends ModuleBody {
        final ModuleMethod lambda$Fn204 = new ModuleMethod(this, 179, (Object) null, 0);
        final ModuleMethod lambda$Fn205 = new ModuleMethod(this, 180, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 179 ? lambda204() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 180 ? lambda205(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 179) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 180) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda204() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mn$Grlist, this.s, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public Object lambda205(Object start, Object end) {
            Object i = AddOp.$Mn.apply2(end, srfi13.Lit1);
            Object obj = LList.Empty;
            while (Scheme.numLss.apply2(i, start) == Boolean.FALSE) {
                Object i2 = AddOp.$Mn.apply2(i, srfi13.Lit1);
                Object obj2 = this.s;
                try {
                    try {
                        obj = lists.cons(Char.make(strings.stringRef((CharSequence) obj2, ((Number) i).intValue())), obj);
                        i = i2;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "string-ref", 2, i);
                    }
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "string-ref", 1, obj2);
                }
            }
            return obj;
        }
    }

    public static Object string$To$List$V(Object s, Object[] argsArray) {
        frame91 frame912 = new frame91();
        frame912.s = s;
        frame912.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        return call_with_values.callWithValues(frame912.lambda$Fn204, frame912.lambda$Fn205);
    }

    public static Object stringAppend$SlShared$V(Object[] argsArray) {
        return stringConcatenate$SlShared(LList.makeList(argsArray, 0));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v5, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v6, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v9, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: java.lang.Boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.lang.Boolean} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object stringConcatenate$SlShared(java.lang.Object r14) {
        /*
            r13 = 1
            gnu.math.IntNum r4 = Lit0
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
        L_0x0005:
            boolean r9 = kawa.lib.lists.isPair(r14)
            if (r9 == 0) goto L_0x003b
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r7 = r9.apply1(r14)
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r8 = r9.apply1(r14)
            java.lang.CharSequence r7 = (java.lang.CharSequence) r7     // Catch:{ ClassCastException -> 0x00ac }
            int r6 = kawa.lib.strings.stringLength(r7)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r6)
            boolean r9 = kawa.lib.numbers.isZero(r9)
            if (r9 == 0) goto L_0x0029
            r14 = r8
            goto L_0x0005
        L_0x0029:
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r10 = java.lang.Integer.valueOf(r6)
            java.lang.Object r4 = r9.apply2(r4, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r2 == r9) goto L_0x0039
        L_0x0037:
            r14 = r8
            goto L_0x0005
        L_0x0039:
            r2 = r14
            goto L_0x0037
        L_0x003b:
            r0 = r4
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00b5 }
            r9 = r0
            boolean r9 = kawa.lib.numbers.isZero(r9)
            if (r9 == 0) goto L_0x0048
            java.lang.String r1 = ""
        L_0x0047:
            return r1
        L_0x0048:
            gnu.kawa.functions.NumberCompare r10 = kawa.standard.Scheme.numEqu
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r9 = r9.apply1(r2)
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x00be }
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r9 = r10.apply2(r4, r9)
            java.lang.Boolean r10 = java.lang.Boolean.FALSE
            if (r9 == r10) goto L_0x0069
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r1 = r9.apply1(r2)
            goto L_0x0047
        L_0x0069:
            r0 = r4
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00c7 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x00c7 }
            java.lang.CharSequence r1 = kawa.lib.strings.makeString(r9)
            gnu.math.IntNum r10 = Lit0
            r14 = r2
        L_0x0078:
            boolean r9 = kawa.lib.lists.isPair(r14)
            if (r9 == 0) goto L_0x0047
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r5 = r9.apply1(r14)
            r0 = r5
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x00d0 }
            r9 = r0
            int r6 = kawa.lib.strings.stringLength(r9)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00d9 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x00d9 }
            java.lang.CharSequence r5 = (java.lang.CharSequence) r5     // Catch:{ ClassCastException -> 0x00e2 }
            r11 = 0
            $PcStringCopy$Ex(r1, r9, r5, r11, r6)
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r14 = r9.apply1(r14)
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r11 = java.lang.Integer.valueOf(r6)
            java.lang.Object r3 = r9.apply2(r10, r11)
            r10 = r3
            goto L_0x0078
        L_0x00ac:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r13, (java.lang.Object) r7)
            throw r10
        L_0x00b5:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "zero?"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r13, (java.lang.Object) r4)
            throw r10
        L_0x00be:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x00c7:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "make-string"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r13, (java.lang.Object) r4)
            throw r10
        L_0x00d0:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r13, (java.lang.Object) r5)
            throw r10
        L_0x00d9:
            r9 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "%string-copy!"
            r11.<init>((java.lang.ClassCastException) r9, (java.lang.String) r12, (int) r13, (java.lang.Object) r10)
            throw r11
        L_0x00e2:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "%string-copy!"
            r12 = 2
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r5)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi13.stringConcatenate$SlShared(java.lang.Object):java.lang.Object");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.CharSequence stringConcatenate(java.lang.Object r13) {
        /*
            r12 = 1
            gnu.math.IntNum r7 = Lit0
            r5 = r13
        L_0x0004:
            boolean r8 = kawa.lib.lists.isPair(r5)
            if (r8 == 0) goto L_0x0029
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r6 = r8.apply1(r5)
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.expr.GenericProc r8 = kawa.lib.lists.car
            java.lang.Object r8 = r8.apply1(r5)
            java.lang.CharSequence r8 = (java.lang.CharSequence) r8     // Catch:{ ClassCastException -> 0x006a }
            int r8 = kawa.lib.strings.stringLength(r8)
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            java.lang.Object r2 = r9.apply2(r7, r8)
            r7 = r2
            r5 = r6
            goto L_0x0004
        L_0x0029:
            r0 = r7
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0073 }
            r8 = r0
            int r8 = r8.intValue()     // Catch:{ ClassCastException -> 0x0073 }
            java.lang.CharSequence r1 = kawa.lib.strings.makeString(r8)
            gnu.math.IntNum r2 = Lit0
        L_0x0037:
            boolean r8 = kawa.lib.lists.isPair(r13)
            if (r8 == 0) goto L_0x0098
            gnu.expr.GenericProc r8 = kawa.lib.lists.car
            java.lang.Object r3 = r8.apply1(r13)
            r0 = r3
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x007c }
            r8 = r0
            int r4 = kawa.lib.strings.stringLength(r8)
            r0 = r2
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0085 }
            r8 = r0
            int r8 = r8.intValue()     // Catch:{ ClassCastException -> 0x0085 }
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3     // Catch:{ ClassCastException -> 0x008e }
            r9 = 0
            $PcStringCopy$Ex(r1, r8, r3, r9, r4)
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r9 = java.lang.Integer.valueOf(r4)
            java.lang.Object r2 = r8.apply2(r2, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r13 = r8.apply1(r13)
            goto L_0x0037
        L_0x006a:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-length"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0073:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "make-string"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r7)
            throw r9
        L_0x007c:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-length"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r3)
            throw r9
        L_0x0085:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "%string-copy!"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r2)
            throw r9
        L_0x008e:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "%string-copy!"
            r11 = 2
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r3)
            throw r9
        L_0x0098:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi13.stringConcatenate(java.lang.Object):java.lang.CharSequence");
    }

    public static Object stringConcatenateReverse$V(Object string$Mnlist, Object[] argsArray) {
        Object obj;
        Object obj2;
        Object obj3;
        LList maybe$Mnfinal$Plend = LList.makeList(argsArray, 0);
        ApplyToArgs applyToArgs = Scheme.applyToArgs;
        try {
            Object obj4 = loc$let$Mnoptionals$St.get();
            ApplyToArgs applyToArgs2 = Scheme.applyToArgs;
            try {
                try {
                    Object apply3 = Scheme.applyToArgs.apply3(loc$final.get(), "", strings.isString(loc$final.get()) ? Boolean.TRUE : Boolean.FALSE);
                    ApplyToArgs applyToArgs3 = Scheme.applyToArgs;
                    try {
                        Object obj5 = loc$end.get();
                        try {
                            try {
                                Integer valueOf = Integer.valueOf(strings.stringLength((CharSequence) loc$final.get()));
                                try {
                                    boolean x = numbers.isInteger(loc$end.get());
                                    if (x) {
                                        try {
                                            boolean x2 = numbers.isExact(loc$end.get());
                                            if (x2) {
                                                try {
                                                    try {
                                                        try {
                                                            obj2 = Scheme.numLEq.apply3(Lit0, loc$end.get(), Integer.valueOf(strings.stringLength((CharSequence) loc$final.get())));
                                                        } catch (ClassCastException e) {
                                                            throw new WrongType(e, "string-length", 1, obj3);
                                                        }
                                                    } catch (UnboundLocationException e2) {
                                                        e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1621, 36);
                                                        throw e2;
                                                    }
                                                } catch (UnboundLocationException e3) {
                                                    e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1621, 17);
                                                    throw e3;
                                                }
                                            } else {
                                                obj2 = x2 ? Boolean.TRUE : Boolean.FALSE;
                                            }
                                        } catch (UnboundLocationException e4) {
                                            e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1620, 19);
                                            throw e4;
                                        }
                                    } else {
                                        obj2 = x ? Boolean.TRUE : Boolean.FALSE;
                                    }
                                    Object apply2 = applyToArgs2.apply2(apply3, applyToArgs3.apply3(obj5, valueOf, obj2));
                                    Object obj6 = Lit0;
                                    Object obj7 = string$Mnlist;
                                    while (true) {
                                        Object sum = obj6;
                                        if (lists.isPair(obj7)) {
                                            AddOp addOp = AddOp.$Pl;
                                            Object apply1 = lists.car.apply1(obj7);
                                            try {
                                                obj6 = addOp.apply2(sum, Integer.valueOf(strings.stringLength((CharSequence) apply1)));
                                                obj7 = lists.cdr.apply1(obj7);
                                            } catch (ClassCastException e5) {
                                                throw new WrongType(e5, "string-length", 1, apply1);
                                            }
                                        } else {
                                            try {
                                                try {
                                                    return applyToArgs.apply4(obj4, maybe$Mnfinal$Plend, apply2, $PcFinishStringConcatenateReverse(sum, string$Mnlist, loc$final.get(), loc$end.get()));
                                                } catch (UnboundLocationException e6) {
                                                    e6.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1627, 65);
                                                    throw e6;
                                                }
                                            } catch (UnboundLocationException e7) {
                                                e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1627, 59);
                                                throw e7;
                                            }
                                        }
                                    }
                                } catch (UnboundLocationException e8) {
                                    e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1619, 21);
                                    throw e8;
                                }
                            } catch (ClassCastException e9) {
                                throw new WrongType(e9, "string-length", 1, obj);
                            }
                        } catch (UnboundLocationException e10) {
                            e10.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1618, 28);
                            throw e10;
                        }
                    } catch (UnboundLocationException e11) {
                        e11.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1618, 8);
                        throw e11;
                    }
                } catch (UnboundLocationException e12) {
                    e12.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1617, 55);
                    throw e12;
                }
            } catch (UnboundLocationException e13) {
                e13.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1617, 36);
                throw e13;
            }
        } catch (UnboundLocationException e14) {
            e14.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1617, 3);
            throw e14;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:147:0x0205, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:148:0x0206, code lost:
        r9.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1652, 56);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:0x020f, code lost:
        throw r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x0210, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x0211, code lost:
        r9.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1652, 62);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:152:0x021a, code lost:
        throw r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x012a, code lost:
        if (r13.apply2(r3, java.lang.Integer.valueOf(kawa.lib.strings.stringLength((java.lang.CharSequence) r9))) != java.lang.Boolean.FALSE) goto L_0x012c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0133, code lost:
        if (r8 == false) goto L_0x0135;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0140, code lost:
        r9 = $PcFinishStringConcatenateReverse(r3, r6, loc$final.get(), loc$end.get());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object stringConcatenateReverse$SlShared$V(java.lang.Object r20, java.lang.Object[] r21) {
        /*
            r9 = 0
            r0 = r21
            gnu.lists.LList r5 = gnu.lists.LList.makeList(r0, r9)
            gnu.kawa.functions.ApplyToArgs r10 = kawa.standard.Scheme.applyToArgs
            gnu.mapping.Location r9 = loc$let$Mnoptionals$St
            java.lang.Object r11 = r9.get()     // Catch:{ UnboundLocationException -> 0x0146 }
            gnu.kawa.functions.ApplyToArgs r12 = kawa.standard.Scheme.applyToArgs
            gnu.kawa.functions.ApplyToArgs r13 = kawa.standard.Scheme.applyToArgs
            gnu.mapping.Location r9 = loc$final
            java.lang.Object r14 = r9.get()     // Catch:{ UnboundLocationException -> 0x0150 }
            java.lang.String r15 = ""
            gnu.mapping.Location r9 = loc$final
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x015b }
            boolean r9 = kawa.lib.strings.isString(r9)
            if (r9 == 0) goto L_0x00c6
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
        L_0x0029:
            java.lang.Object r13 = r13.apply3(r14, r15, r9)
            gnu.kawa.functions.ApplyToArgs r14 = kawa.standard.Scheme.applyToArgs
            gnu.mapping.Location r9 = loc$end
            java.lang.Object r15 = r9.get()     // Catch:{ UnboundLocationException -> 0x0166 }
            gnu.mapping.Location r9 = loc$final
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x0171 }
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x017c }
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r16 = java.lang.Integer.valueOf(r9)
            gnu.mapping.Location r9 = loc$end
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x0186 }
            boolean r8 = kawa.lib.numbers.isInteger(r9)
            if (r8 == 0) goto L_0x00d2
            gnu.mapping.Location r9 = loc$end
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x0191 }
            boolean r8 = kawa.lib.numbers.isExact(r9)
            if (r8 == 0) goto L_0x00ca
            gnu.kawa.functions.NumberCompare r17 = kawa.standard.Scheme.numLEq
            gnu.math.IntNum r18 = Lit0
            gnu.mapping.Location r9 = loc$end
            java.lang.Object r19 = r9.get()     // Catch:{ UnboundLocationException -> 0x019c }
            gnu.mapping.Location r9 = loc$final
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x01a7 }
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x01b2 }
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            r0 = r17
            r1 = r18
            r2 = r19
            java.lang.Object r9 = r0.apply3(r1, r2, r9)
        L_0x0081:
            r0 = r16
            java.lang.Object r9 = r14.apply3(r15, r0, r9)
            java.lang.Object r12 = r12.apply2(r13, r9)
            gnu.math.IntNum r3 = Lit0
            java.lang.Boolean r6 = java.lang.Boolean.FALSE
        L_0x008f:
            boolean r9 = kawa.lib.lists.isPair(r20)
            if (r9 == 0) goto L_0x00dd
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            r0 = r20
            java.lang.Object r9 = r9.apply1(r0)
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x01bc }
            int r7 = kawa.lib.strings.stringLength(r9)
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            java.lang.Integer r13 = java.lang.Integer.valueOf(r7)
            java.lang.Object r3 = r9.apply2(r3, r13)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r6 != r9) goto L_0x00bb
            java.lang.Integer r9 = java.lang.Integer.valueOf(r7)
            boolean r9 = kawa.lib.numbers.isZero(r9)
            if (r9 == 0) goto L_0x00da
        L_0x00bb:
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            r0 = r20
            java.lang.Object r4 = r9.apply1(r0)
            r20 = r4
            goto L_0x008f
        L_0x00c6:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            goto L_0x0029
        L_0x00ca:
            if (r8 == 0) goto L_0x00cf
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            goto L_0x0081
        L_0x00cf:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            goto L_0x0081
        L_0x00d2:
            if (r8 == 0) goto L_0x00d7
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            goto L_0x0081
        L_0x00d7:
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            goto L_0x0081
        L_0x00da:
            r6 = r20
            goto L_0x00bb
        L_0x00dd:
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01c6 }
            r9 = r0
            boolean r9 = kawa.lib.numbers.isZero(r9)
            if (r9 == 0) goto L_0x0104
            gnu.mapping.Location r9 = loc$final
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x01d0 }
            gnu.math.IntNum r13 = Lit0
            r14 = 1
            java.lang.Object[] r14 = new java.lang.Object[r14]
            r15 = 0
            gnu.mapping.Location r16 = loc$end
            java.lang.Object r16 = r16.get()     // Catch:{ UnboundLocationException -> 0x01db }
            r14[r15] = r16
            java.lang.Object r9 = substring$SlShared$V(r9, r13, r14)
        L_0x00ff:
            java.lang.Object r9 = r10.apply4(r11, r5, r12, r9)
            return r9
        L_0x0104:
            gnu.mapping.Location r9 = loc$end
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x01e6 }
            java.lang.Number r9 = (java.lang.Number) r9     // Catch:{ ClassCastException -> 0x01f1 }
            boolean r8 = kawa.lib.numbers.isZero(r9)
            if (r8 == 0) goto L_0x0133
            gnu.kawa.functions.NumberCompare r13 = kawa.standard.Scheme.numEqu
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r9 = r9.apply1(r6)
            java.lang.CharSequence r9 = (java.lang.CharSequence) r9     // Catch:{ ClassCastException -> 0x01fb }
            int r9 = kawa.lib.strings.stringLength(r9)
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)
            java.lang.Object r9 = r13.apply2(r3, r9)
            java.lang.Boolean r13 = java.lang.Boolean.FALSE
            if (r9 == r13) goto L_0x0135
        L_0x012c:
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r9 = r9.apply1(r6)
            goto L_0x00ff
        L_0x0133:
            if (r8 != 0) goto L_0x012c
        L_0x0135:
            gnu.mapping.Location r9 = loc$final
            java.lang.Object r9 = r9.get()     // Catch:{ UnboundLocationException -> 0x0205 }
            gnu.mapping.Location r13 = loc$end
            java.lang.Object r13 = r13.get()     // Catch:{ UnboundLocationException -> 0x0210 }
            java.lang.Object r9 = $PcFinishStringConcatenateReverse(r3, r6, r9, r13)
            goto L_0x00ff
        L_0x0146:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1630(0x65e, float:2.284E-42)
            r12 = 3
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x0150:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1630(0x65e, float:2.284E-42)
            r12 = 36
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x015b:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1630(0x65e, float:2.284E-42)
            r12 = 55
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x0166:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1631(0x65f, float:2.286E-42)
            r12 = 8
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x0171:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1631(0x65f, float:2.286E-42)
            r12 = 28
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x017c:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0186:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1632(0x660, float:2.287E-42)
            r12 = 21
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x0191:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1633(0x661, float:2.288E-42)
            r12 = 19
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x019c:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1634(0x662, float:2.29E-42)
            r12 = 17
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x01a7:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1634(0x662, float:2.29E-42)
            r12 = 36
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x01b2:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x01bc:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x01c6:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "zero?"
            r12 = 1
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r3)
            throw r10
        L_0x01d0:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1645(0x66d, float:2.305E-42)
            r12 = 41
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x01db:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1645(0x66d, float:2.305E-42)
            r12 = 49
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x01e6:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1649(0x671, float:2.311E-42)
            r12 = 16
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x01f1:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "zero?"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x01fb:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-length"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r9)
            throw r11
        L_0x0205:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1652(0x674, float:2.315E-42)
            r12 = 56
            r9.setLine(r10, r11, r12)
            throw r9
        L_0x0210:
            r9 = move-exception
            java.lang.String r10 = "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm"
            r11 = 1652(0x674, float:2.315E-42)
            r12 = 62
            r9.setLine(r10, r11, r12)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi13.stringConcatenateReverse$SlShared$V(java.lang.Object, java.lang.Object[]):java.lang.Object");
    }

    public static Object $PcFinishStringConcatenateReverse(Object len, Object string$Mnlist, Object obj, Object end) {
        Object apply2 = AddOp.$Pl.apply2(end, len);
        try {
            CharSequence ans = strings.makeString(((Number) apply2).intValue());
            try {
                try {
                    try {
                        $PcStringCopy$Ex(ans, ((Number) len).intValue(), (CharSequence) obj, 0, ((Number) end).intValue());
                        Object i = len;
                        while (lists.isPair(string$Mnlist)) {
                            Object s = lists.car.apply1(string$Mnlist);
                            Object lis = lists.cdr.apply1(string$Mnlist);
                            try {
                                int slen = strings.stringLength((CharSequence) s);
                                i = AddOp.$Mn.apply2(i, Integer.valueOf(slen));
                                try {
                                    try {
                                        $PcStringCopy$Ex(ans, ((Number) i).intValue(), (CharSequence) s, 0, slen);
                                        string$Mnlist = lis;
                                    } catch (ClassCastException e) {
                                        throw new WrongType(e, "%string-copy!", 2, s);
                                    }
                                } catch (ClassCastException e2) {
                                    throw new WrongType(e2, "%string-copy!", 1, i);
                                }
                            } catch (ClassCastException e3) {
                                throw new WrongType(e3, "string-length", 1, s);
                            }
                        }
                        return ans;
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "%string-copy!", 4, end);
                    }
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "%string-copy!", 2, obj);
                }
            } catch (ClassCastException e6) {
                throw new WrongType(e6, "%string-copy!", 1, len);
            }
        } catch (ClassCastException e7) {
            throw new WrongType(e7, "make-string", 1, apply2);
        }
    }

    public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
        switch (moduleMethod.selector) {
            case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_FULLSCREEN:
                try {
                    try {
                        try {
                            return $PcCheckBounds(obj, (CharSequence) obj2, ((Number) obj3).intValue(), ((Number) obj4).intValue());
                        } catch (ClassCastException e) {
                            throw new WrongType(e, "%check-bounds", 4, obj4);
                        }
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "%check-bounds", 3, obj3);
                    }
                } catch (ClassCastException e3) {
                    throw new WrongType(e3, "%check-bounds", 2, obj2);
                }
            case 198:
                return checkSubstringSpec(obj, obj2, obj3, obj4);
            case 204:
                return $PcStringMap(obj, obj2, obj3, obj4);
            case 206:
                return $PcStringMap$Ex(obj, obj2, obj3, obj4);
            case 299:
                try {
                    try {
                        try {
                            return stringCopy$Ex(obj, ((Number) obj2).intValue(), (CharSequence) obj3, ((Number) obj4).intValue());
                        } catch (ClassCastException e4) {
                            throw new WrongType(e4, "string-copy!", 4, obj4);
                        }
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "string-copy!", 3, obj3);
                    }
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "string-copy!", 2, obj2);
                }
            case 319:
                return $PcFinishStringConcatenateReverse(obj, obj2, obj3, obj4);
            default:
                return super.apply4(moduleMethod, obj, obj2, obj3, obj4);
        }
    }

    public static Object stringReplace$V(Object s1, Object s2, Object start1, Object end1, Object[] argsArray) {
        frame92 frame922 = new frame92();
        frame922.s1 = s1;
        frame922.s2 = s2;
        frame922.start1 = start1;
        frame922.end1 = end1;
        frame922.maybe$Mnstart$Plend = LList.makeList(argsArray, 0);
        checkSubstringSpec(string$Mnreplace, frame922.s1, frame922.start1, frame922.end1);
        return call_with_values.callWithValues(frame922.lambda$Fn206, frame922.lambda$Fn207);
    }

    /* compiled from: srfi13.scm */
    public class frame92 extends ModuleBody {
        Object end1;
        final ModuleMethod lambda$Fn206 = new ModuleMethod(this, 181, (Object) null, 0);
        final ModuleMethod lambda$Fn207 = new ModuleMethod(this, 182, (Object) null, 8194);
        LList maybe$Mnstart$Plend;
        Object s1;
        Object s2;
        Object start1;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 181 ? lambda206() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 182 ? lambda207(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 181) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 182) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda206() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnreplace, this.s2, this.maybe$Mnstart$Plend);
        }

        /* access modifiers changed from: package-private */
        public CharSequence lambda207(Object start2, Object end2) {
            Object obj = this.s1;
            try {
                int slen1 = strings.stringLength((CharSequence) obj);
                Object sublen2 = AddOp.$Mn.apply2(end2, start2);
                Object alen = AddOp.$Pl.apply2(AddOp.$Mn.apply2(Integer.valueOf(slen1), AddOp.$Mn.apply2(this.end1, this.start1)), sublen2);
                try {
                    CharSequence ans = strings.makeString(((Number) alen).intValue());
                    Object obj2 = this.s1;
                    try {
                        CharSequence charSequence = (CharSequence) obj2;
                        Object obj3 = this.start1;
                        try {
                            srfi13.$PcStringCopy$Ex(ans, 0, charSequence, 0, ((Number) obj3).intValue());
                            Object obj4 = this.start1;
                            try {
                                int intValue = ((Number) obj4).intValue();
                                Object obj5 = this.s2;
                                try {
                                    try {
                                        try {
                                            srfi13.$PcStringCopy$Ex(ans, intValue, (CharSequence) obj5, ((Number) start2).intValue(), ((Number) end2).intValue());
                                            Object apply2 = AddOp.$Pl.apply2(this.start1, sublen2);
                                            try {
                                                int intValue2 = ((Number) apply2).intValue();
                                                Object obj6 = this.s1;
                                                try {
                                                    CharSequence charSequence2 = (CharSequence) obj6;
                                                    Object obj7 = this.end1;
                                                    try {
                                                        srfi13.$PcStringCopy$Ex(ans, intValue2, charSequence2, ((Number) obj7).intValue(), slen1);
                                                        return ans;
                                                    } catch (ClassCastException e) {
                                                        throw new WrongType(e, "%string-copy!", 3, obj7);
                                                    }
                                                } catch (ClassCastException e2) {
                                                    throw new WrongType(e2, "%string-copy!", 2, obj6);
                                                }
                                            } catch (ClassCastException e3) {
                                                throw new WrongType(e3, "%string-copy!", 1, apply2);
                                            }
                                        } catch (ClassCastException e4) {
                                            throw new WrongType(e4, "%string-copy!", 4, end2);
                                        }
                                    } catch (ClassCastException e5) {
                                        throw new WrongType(e5, "%string-copy!", 3, start2);
                                    }
                                } catch (ClassCastException e6) {
                                    throw new WrongType(e6, "%string-copy!", 2, obj5);
                                }
                            } catch (ClassCastException e7) {
                                throw new WrongType(e7, "%string-copy!", 1, obj4);
                            }
                        } catch (ClassCastException e8) {
                            throw new WrongType(e8, "%string-copy!", 4, obj3);
                        }
                    } catch (ClassCastException e9) {
                        throw new WrongType(e9, "%string-copy!", 2, obj2);
                    }
                } catch (ClassCastException e10) {
                    throw new WrongType(e10, "make-string", 1, alen);
                }
            } catch (ClassCastException e11) {
                throw new WrongType(e11, "string-length", 1, obj);
            }
        }
    }

    public static Object stringTokenize$V(Object s, Object[] argsArray) {
        frame93 frame932 = new frame93();
        frame932.s = s;
        LList token$Mnchars$Plstart$Plend = LList.makeList(argsArray, 0);
        try {
            try {
                try {
                    try {
                        try {
                            try {
                                return Scheme.applyToArgs.apply4(loc$let$Mnoptionals$St.get(), token$Mnchars$Plstart$Plend, Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply3(loc$token$Mnchars.get(), GetNamedPart.getNamedPart.apply2(loc$char$Mnset.get(), Lit14), Scheme.applyToArgs.apply2(loc$char$Mnset$Qu.get(), loc$token$Mnchars.get())), loc$rest.get()), call_with_values.callWithValues(frame932.lambda$Fn208, frame932.lambda$Fn209));
                            } catch (UnboundLocationException e) {
                                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1695, 75);
                                throw e;
                            }
                        } catch (UnboundLocationException e2) {
                            e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1695, 61);
                            throw e2;
                        }
                    } catch (UnboundLocationException e3) {
                        e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1695, 50);
                        throw e3;
                    }
                } catch (UnboundLocationException e4) {
                    e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1695, 33);
                    throw e4;
                }
            } catch (UnboundLocationException e5) {
                e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1695, 20);
                throw e5;
            }
        } catch (UnboundLocationException e6) {
            e6.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1694, 3);
            throw e6;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame93 extends ModuleBody {
        final ModuleMethod lambda$Fn208 = new ModuleMethod(this, 183, (Object) null, 0);
        final ModuleMethod lambda$Fn209 = new ModuleMethod(this, 184, (Object) null, 8194);
        Object s;

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 183 ? lambda208() : super.apply0(moduleMethod);
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 184 ? lambda209(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 183) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 184) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda208() {
            try {
                return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mntokenize, this.s, srfi13.loc$rest.get());
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1696, 57);
                throw e;
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda209(Object start, Object end) {
            Object temp;
            Object obj = LList.Empty;
            Object i = end;
            while (true) {
                Object apply2 = Scheme.numLss.apply2(start, i);
                try {
                    boolean x = ((Boolean) apply2).booleanValue();
                    if (x) {
                        try {
                            temp = srfi13.stringIndexRight$V(this.s, srfi13.loc$token$Mnchars.get(), new Object[]{start, i});
                        } catch (UnboundLocationException e) {
                            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1698, 48);
                            throw e;
                        }
                    } else {
                        temp = x ? Boolean.TRUE : Boolean.FALSE;
                    }
                    if (temp == Boolean.FALSE) {
                        return obj;
                    }
                    Object tend = AddOp.$Pl.apply2(srfi13.Lit1, temp);
                    try {
                        Object temp2 = srfi13.stringSkipRight$V(this.s, srfi13.loc$token$Mnchars.get(), new Object[]{start, temp});
                        if (temp2 != Boolean.FALSE) {
                            Object obj2 = this.s;
                            try {
                                CharSequence charSequence = (CharSequence) obj2;
                                Object apply22 = AddOp.$Pl.apply2(srfi13.Lit1, temp2);
                                try {
                                    try {
                                        obj = lists.cons(strings.substring(charSequence, ((Number) apply22).intValue(), ((Number) tend).intValue()), obj);
                                        i = temp2;
                                    } catch (ClassCastException e2) {
                                        throw new WrongType(e2, "substring", 3, tend);
                                    }
                                } catch (ClassCastException e3) {
                                    throw new WrongType(e3, "substring", 2, apply22);
                                }
                            } catch (ClassCastException e4) {
                                throw new WrongType(e4, "substring", 1, obj2);
                            }
                        } else {
                            Object obj3 = this.s;
                            try {
                                try {
                                    try {
                                        return lists.cons(strings.substring((CharSequence) obj3, ((Number) start).intValue(), ((Number) tend).intValue()), obj);
                                    } catch (ClassCastException e5) {
                                        throw new WrongType(e5, "substring", 3, tend);
                                    }
                                } catch (ClassCastException e6) {
                                    throw new WrongType(e6, "substring", 2, start);
                                }
                            } catch (ClassCastException e7) {
                                throw new WrongType(e7, "substring", 1, obj3);
                            }
                        }
                    } catch (UnboundLocationException e8) {
                        e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", ErrorMessages.ERROR_BAD_VALUE_FOR_TEXT_RECEIVING, 34);
                        throw e8;
                    }
                } catch (ClassCastException e9) {
                    throw new WrongType(e9, "x", -2, apply2);
                }
            }
        }
    }

    /* compiled from: srfi13.scm */
    public class frame94 extends ModuleBody {
        Object from;
        final ModuleMethod lambda$Fn211;
        final ModuleMethod lambda$Fn212 = new ModuleMethod(this, 185, (Object) null, 0);
        final ModuleMethod lambda$Fn213;
        final ModuleMethod lambda$Fn214;
        final ModuleMethod lambda$Fn215;
        LList maybe$Mnto$Plstart$Plend;
        Object s;

        public frame94() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 186, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1744");
            this.lambda$Fn214 = moduleMethod;
            this.lambda$Fn213 = new ModuleMethod(this, 187, (Object) null, 8194);
            this.lambda$Fn211 = new ModuleMethod(this, 188, (Object) null, 0);
            ModuleMethod moduleMethod2 = new ModuleMethod(this, FullScreenVideoUtil.FULLSCREEN_VIDEO_DIALOG_FLAG, (Object) null, 12291);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1740");
            this.lambda$Fn215 = moduleMethod2;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 185:
                    return lambda212();
                case 188:
                    return lambda211();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 187 ? lambda213(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 185:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 188:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 187) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        static boolean lambda210(Object val) {
            boolean x = numbers.isInteger(val);
            return x ? numbers.isExact(val) : x;
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 189 ? lambda215(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        /* access modifiers changed from: package-private */
        public Object lambda215(Object to, Object start, Object end) {
            Object slen = AddOp.$Mn.apply2(end, start);
            Object anslen = AddOp.$Mn.apply2(to, this.from);
            try {
                if (numbers.isZero((Number) anslen)) {
                    return "";
                }
                try {
                    if (numbers.isZero((Number) slen)) {
                        return misc.error$V("Cannot replicate empty (sub)string", new Object[]{srfi13.xsubstring, this.s, this.from, to, start, end});
                    } else if (Scheme.numEqu.apply2(srfi13.Lit1, slen) != Boolean.FALSE) {
                        try {
                            int intValue = ((Number) anslen).intValue();
                            Object obj = this.s;
                            try {
                                try {
                                    return strings.makeString(intValue, Char.make(strings.stringRef((CharSequence) obj, ((Number) start).intValue())));
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, start);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, obj);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "make-string", 1, anslen);
                        }
                    } else {
                        Object apply2 = DivideOp.$Sl.apply2(this.from, slen);
                        try {
                            double doubleValue = numbers.floor(LangObjType.coerceRealNum(apply2)).doubleValue();
                            Object apply22 = DivideOp.$Sl.apply2(to, slen);
                            try {
                                if (doubleValue == numbers.floor(LangObjType.coerceRealNum(apply22)).doubleValue()) {
                                    Object obj2 = this.s;
                                    try {
                                        CharSequence charSequence = (CharSequence) obj2;
                                        Object apply23 = AddOp.$Pl.apply2(start, DivideOp.modulo.apply2(this.from, slen));
                                        try {
                                            int intValue2 = ((Number) apply23).intValue();
                                            Object apply24 = AddOp.$Pl.apply2(start, DivideOp.modulo.apply2(to, slen));
                                            try {
                                                return strings.substring(charSequence, intValue2, ((Number) apply24).intValue());
                                            } catch (ClassCastException e4) {
                                                throw new WrongType(e4, "substring", 3, apply24);
                                            }
                                        } catch (ClassCastException e5) {
                                            throw new WrongType(e5, "substring", 2, apply23);
                                        }
                                    } catch (ClassCastException e6) {
                                        throw new WrongType(e6, "substring", 1, obj2);
                                    }
                                } else {
                                    try {
                                        CharSequence ans = strings.makeString(((Number) anslen).intValue());
                                        srfi13.$PcMultispanRepcopy$Ex(ans, srfi13.Lit0, this.s, this.from, to, start, end);
                                        return ans;
                                    } catch (ClassCastException e7) {
                                        throw new WrongType(e7, "make-string", 1, anslen);
                                    }
                                }
                            } catch (ClassCastException e8) {
                                throw new WrongType(e8, "floor", 1, apply22);
                            }
                        } catch (ClassCastException e9) {
                            throw new WrongType(e9, "floor", 1, apply2);
                        }
                    }
                } catch (ClassCastException e10) {
                    throw new WrongType(e10, "zero?", 1, slen);
                }
            } catch (ClassCastException e11) {
                throw new WrongType(e11, "zero?", 1, anslen);
            }
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 189) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda211() {
            if (lists.isPair(this.maybe$Mnto$Plstart$Plend)) {
                return call_with_values.callWithValues(this.lambda$Fn212, this.lambda$Fn213);
            }
            try {
                Object apply4 = Scheme.applyToArgs.apply4(srfi13.loc$check$Mnarg.get(), strings.string$Qu, this.s, srfi13.xsubstring);
                try {
                    int slen = strings.stringLength((CharSequence) apply4);
                    return misc.values(AddOp.$Pl.apply2(this.from, Integer.valueOf(slen)), srfi13.Lit0, Integer.valueOf(slen));
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-length", 1, apply4);
                }
            } catch (UnboundLocationException e2) {
                e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1749, 36);
                throw e2;
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda212() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.xsubstring, this.s, lists.cdr.apply1(this.maybe$Mnto$Plstart$Plend));
        }

        /* access modifiers changed from: package-private */
        public Object lambda213(Object start, Object end) {
            Object to = lists.car.apply1(this.maybe$Mnto$Plstart$Plend);
            try {
                Scheme.applyToArgs.apply4(srfi13.loc$check$Mnarg.get(), this.lambda$Fn214, to, srfi13.xsubstring);
                return misc.values(to, start, end);
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1744, 6);
                throw e;
            }
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            if (moduleMethod.selector == 186) {
                return lambda214(obj) ? Boolean.TRUE : Boolean.FALSE;
            }
            return super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public boolean lambda214(Object val) {
            boolean x = numbers.isInteger(val);
            if (!x) {
                return x;
            }
            boolean x2 = numbers.isExact(val);
            if (x2) {
                return ((Boolean) Scheme.numLEq.apply2(this.from, val)).booleanValue();
            }
            return x2;
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 186) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }
    }

    public static Object xsubstring$V(Object s, Object from, Object[] argsArray) {
        frame94 frame942 = new frame94();
        frame942.s = s;
        frame942.from = from;
        frame942.maybe$Mnto$Plstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), lambda$Fn210, frame942.from, xsubstring);
            return call_with_values.callWithValues(frame942.lambda$Fn211, frame942.lambda$Fn215);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1738, 3);
            throw e;
        }
    }

    /* compiled from: srfi13.scm */
    public class frame95 extends ModuleBody {
        final ModuleMethod lambda$Fn217 = new ModuleMethod(this, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE, (Object) null, 0);
        final ModuleMethod lambda$Fn218 = new ModuleMethod(this, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SEEK, (Object) null, 0);
        final ModuleMethod lambda$Fn219 = new ModuleMethod(this, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PLAY, (Object) null, 8194);
        final ModuleMethod lambda$Fn221;
        LList maybe$Mnsto$Plstart$Plend;
        Object s;
        Object sfrom;
        Object target;
        Object tstart;

        public frame95() {
            ModuleMethod moduleMethod = new ModuleMethod(this, FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_STOP, (Object) null, 12291);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm:1781");
            this.lambda$Fn221 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SEEK:
                    return lambda218();
                case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE:
                    return lambda217();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            return moduleMethod.selector == 191 ? lambda219(obj, obj2) : super.apply2(moduleMethod, obj, obj2);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_SEEK:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case FullScreenVideoUtil.FULLSCREEN_VIDEO_ACTION_PAUSE:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            if (moduleMethod.selector != 191) {
                return super.match2(moduleMethod, obj, obj2, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.proc = moduleMethod;
            callContext.pc = 2;
            return 0;
        }

        static boolean lambda216(Object val) {
            boolean x = numbers.isInteger(val);
            return x ? numbers.isExact(val) : x;
        }

        public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
            return moduleMethod.selector == 193 ? lambda221(obj, obj2, obj3) : super.apply3(moduleMethod, obj, obj2, obj3);
        }

        /* access modifiers changed from: package-private */
        public Object lambda221(Object sto, Object start, Object end) {
            Object tocopy = AddOp.$Mn.apply2(sto, this.sfrom);
            Object tend = AddOp.$Pl.apply2(this.tstart, tocopy);
            Object slen = AddOp.$Mn.apply2(end, start);
            srfi13.checkSubstringSpec(srfi13.string$Mnxcopy$Ex, this.target, this.tstart, tend);
            try {
                boolean x = numbers.isZero((Number) tocopy);
                if (!x) {
                    try {
                        if (numbers.isZero((Number) slen)) {
                            return misc.error$V("Cannot replicate empty (sub)string", new Object[]{srfi13.string$Mnxcopy$Ex, this.target, this.tstart, this.s, this.sfrom, sto, start, end});
                        } else if (Scheme.numEqu.apply2(srfi13.Lit1, slen) != Boolean.FALSE) {
                            Object obj = this.target;
                            Object obj2 = this.s;
                            try {
                                try {
                                    return srfi13.stringFill$Ex$V(obj, Char.make(strings.stringRef((CharSequence) obj2, ((Number) start).intValue())), new Object[]{this.tstart, tend});
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, start);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, obj2);
                            }
                        } else {
                            Object apply2 = DivideOp.$Sl.apply2(this.sfrom, slen);
                            try {
                                double doubleValue = numbers.floor(LangObjType.coerceRealNum(apply2)).doubleValue();
                                Object apply22 = DivideOp.$Sl.apply2(sto, slen);
                                try {
                                    if (doubleValue != numbers.floor(LangObjType.coerceRealNum(apply22)).doubleValue()) {
                                        return srfi13.$PcMultispanRepcopy$Ex(this.target, this.tstart, this.s, this.sfrom, sto, start, end);
                                    }
                                    Object obj3 = this.target;
                                    try {
                                        CharSequence charSequence = (CharSequence) obj3;
                                        Object obj4 = this.tstart;
                                        try {
                                            int intValue = ((Number) obj4).intValue();
                                            Object obj5 = this.s;
                                            try {
                                                CharSequence charSequence2 = (CharSequence) obj5;
                                                Object apply23 = AddOp.$Pl.apply2(start, DivideOp.modulo.apply2(this.sfrom, slen));
                                                try {
                                                    int intValue2 = ((Number) apply23).intValue();
                                                    Object apply24 = AddOp.$Pl.apply2(start, DivideOp.modulo.apply2(sto, slen));
                                                    try {
                                                        return srfi13.$PcStringCopy$Ex(charSequence, intValue, charSequence2, intValue2, ((Number) apply24).intValue());
                                                    } catch (ClassCastException e3) {
                                                        throw new WrongType(e3, "%string-copy!", 4, apply24);
                                                    }
                                                } catch (ClassCastException e4) {
                                                    throw new WrongType(e4, "%string-copy!", 3, apply23);
                                                }
                                            } catch (ClassCastException e5) {
                                                throw new WrongType(e5, "%string-copy!", 2, obj5);
                                            }
                                        } catch (ClassCastException e6) {
                                            throw new WrongType(e6, "%string-copy!", 1, obj4);
                                        }
                                    } catch (ClassCastException e7) {
                                        throw new WrongType(e7, "%string-copy!", 0, obj3);
                                    }
                                } catch (ClassCastException e8) {
                                    throw new WrongType(e8, "floor", 1, apply22);
                                }
                            } catch (ClassCastException e9) {
                                throw new WrongType(e9, "floor", 1, apply2);
                            }
                        }
                    } catch (ClassCastException e10) {
                        throw new WrongType(e10, "zero?", 1, slen);
                    }
                } else if (x) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            } catch (ClassCastException e11) {
                throw new WrongType(e11, "zero?", 1, tocopy);
            }
        }

        public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
            if (moduleMethod.selector != 193) {
                return super.match3(moduleMethod, obj, obj2, obj3, callContext);
            }
            callContext.value1 = obj;
            callContext.value2 = obj2;
            callContext.value3 = obj3;
            callContext.proc = moduleMethod;
            callContext.pc = 3;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda217() {
            if (lists.isPair(this.maybe$Mnsto$Plstart$Plend)) {
                return call_with_values.callWithValues(this.lambda$Fn218, this.lambda$Fn219);
            }
            Object obj = this.s;
            try {
                int slen = strings.stringLength((CharSequence) obj);
                return misc.values(AddOp.$Pl.apply2(this.sfrom, Integer.valueOf(slen)), srfi13.Lit0, Integer.valueOf(slen));
            } catch (ClassCastException e) {
                throw new WrongType(e, "string-length", 1, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda218() {
            return srfi13.stringParseFinalStart$PlEnd(srfi13.string$Mnxcopy$Ex, this.s, lists.cdr.apply1(this.maybe$Mnsto$Plstart$Plend));
        }

        /* access modifiers changed from: package-private */
        public Object lambda219(Object start, Object end) {
            Object sto = lists.car.apply1(this.maybe$Mnsto$Plstart$Plend);
            try {
                Scheme.applyToArgs.apply4(srfi13.loc$check$Mnarg.get(), srfi13.lambda$Fn220, sto, srfi13.string$Mnxcopy$Ex);
                return misc.values(sto, start, end);
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1785, 6);
                throw e;
            }
        }

        static boolean lambda220(Object val) {
            boolean x = numbers.isInteger(val);
            return x ? numbers.isExact(val) : x;
        }
    }

    public static Object stringXcopy$Ex$V(Object target, Object tstart, Object s, Object sfrom, Object[] argsArray) {
        frame95 frame952 = new frame95();
        frame952.target = target;
        frame952.tstart = tstart;
        frame952.s = s;
        frame952.sfrom = sfrom;
        frame952.maybe$Mnsto$Plstart$Plend = LList.makeList(argsArray, 0);
        try {
            Scheme.applyToArgs.apply4(loc$check$Mnarg.get(), lambda$Fn216, frame952.sfrom, string$Mnxcopy$Ex);
            return call_with_values.callWithValues(frame952.lambda$Fn217, frame952.lambda$Fn221);
        } catch (UnboundLocationException e) {
            e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1779, 3);
            throw e;
        }
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 199:
                return frame1.lambda5(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 209:
                return lambda17(obj);
            case 211:
                return lambda18(obj);
            case 217:
                return lambda27(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 239:
                return frame32.lambda72(obj);
            case 240:
                return frame32.lambda73(obj);
            case LispEscapeFormat.ESCAPE_ALL:
                return frame34.lambda78(obj);
            case 244:
                return frame36.lambda83(obj);
            case 245:
                return frame36.lambda84(obj);
            case 247:
                return frame38.lambda89(obj);
            case 248:
                return frame38.lambda90(obj);
            case ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION:
                return frame40.lambda95(obj);
            case Telnet.WONT:
                return frame42.lambda100(obj);
            case Telnet.DONT:
                return frame44.lambda105(obj);
            case 255:
                return frame44.lambda106(obj);
            case InputDeviceCompat.SOURCE_KEYBOARD:
                return frame46.lambda111(obj);
            case 259:
                return frame48.lambda116(obj);
            case 260:
                return frame48.lambda117(obj);
            case 262:
                return frame50.lambda122(obj);
            case 263:
                return frame50.lambda123(obj);
            case 265:
                return frame52.lambda128(obj);
            case 267:
                return frame54.lambda133(obj);
            case 271:
                return Integer.valueOf(frame57.lambda138(obj));
            case 287:
                return frame71.lambda163(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 289:
                return frame72.lambda166(obj) ? Boolean.TRUE : Boolean.FALSE;
            case ErrorMessages.ERROR_TWITTER_REQUEST_DIRECT_MESSAGES_FAILED:
                return isStringNull(obj) ? Boolean.TRUE : Boolean.FALSE;
            case ErrorMessages.ERROR_TWITTER_STOP_FOLLOWING_FAILED:
                return reverseList$To$String(obj);
            case 315:
                return stringConcatenate$SlShared(obj);
            case 316:
                return stringConcatenate(obj);
            case 322:
                return frame94.lambda210(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 324:
                return frame95.lambda216(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 325:
                return frame95.lambda220(obj) ? Boolean.TRUE : Boolean.FALSE;
            default:
                return super.apply1(moduleMethod, obj);
        }
    }

    public static Object $PcMultispanRepcopy$Ex(Object target, Object tstart, Object s, Object sfrom, Object sto, Object start, Object end) {
        Object slen = AddOp.$Mn.apply2(end, start);
        Object i0 = AddOp.$Pl.apply2(start, DivideOp.modulo.apply2(sfrom, slen));
        Object total$Mnchars = AddOp.$Mn.apply2(sto, sfrom);
        try {
            try {
                try {
                    try {
                        try {
                            $PcStringCopy$Ex((CharSequence) target, ((Number) tstart).intValue(), (CharSequence) s, ((Number) i0).intValue(), ((Number) end).intValue());
                            Object ncopied = AddOp.$Mn.apply2(end, i0);
                            Object nspans = DivideOp.quotient.apply2(AddOp.$Mn.apply2(total$Mnchars, ncopied), slen);
                            Object i = AddOp.$Pl.apply2(tstart, ncopied);
                            while (!numbers.isZero((Number) nspans)) {
                                try {
                                    try {
                                        try {
                                            try {
                                                try {
                                                    try {
                                                        $PcStringCopy$Ex((CharSequence) target, ((Number) i).intValue(), (CharSequence) s, ((Number) start).intValue(), ((Number) end).intValue());
                                                        i = AddOp.$Pl.apply2(i, slen);
                                                        nspans = AddOp.$Mn.apply2(nspans, Lit1);
                                                    } catch (ClassCastException e) {
                                                        throw new WrongType(e, "%string-copy!", 4, end);
                                                    }
                                                } catch (ClassCastException e2) {
                                                    throw new WrongType(e2, "%string-copy!", 3, start);
                                                }
                                            } catch (ClassCastException e3) {
                                                throw new WrongType(e3, "%string-copy!", 2, s);
                                            }
                                        } catch (ClassCastException e4) {
                                            throw new WrongType(e4, "%string-copy!", 1, i);
                                        }
                                    } catch (ClassCastException e5) {
                                        throw new WrongType(e5, "%string-copy!", 0, target);
                                    }
                                } catch (ClassCastException e6) {
                                    throw new WrongType(e6, "zero?", 1, nspans);
                                }
                            }
                            try {
                                CharSequence charSequence = (CharSequence) target;
                                try {
                                    int intValue = ((Number) i).intValue();
                                    try {
                                        CharSequence charSequence2 = (CharSequence) s;
                                        try {
                                            int intValue2 = ((Number) start).intValue();
                                            Object apply2 = AddOp.$Pl.apply2(start, AddOp.$Mn.apply2(total$Mnchars, AddOp.$Mn.apply2(i, tstart)));
                                            try {
                                                return $PcStringCopy$Ex(charSequence, intValue, charSequence2, intValue2, ((Number) apply2).intValue());
                                            } catch (ClassCastException e7) {
                                                throw new WrongType(e7, "%string-copy!", 4, apply2);
                                            }
                                        } catch (ClassCastException e8) {
                                            throw new WrongType(e8, "%string-copy!", 3, start);
                                        }
                                    } catch (ClassCastException e9) {
                                        throw new WrongType(e9, "%string-copy!", 2, s);
                                    }
                                } catch (ClassCastException e10) {
                                    throw new WrongType(e10, "%string-copy!", 1, i);
                                }
                            } catch (ClassCastException e11) {
                                throw new WrongType(e11, "%string-copy!", 0, target);
                            }
                        } catch (ClassCastException e12) {
                            throw new WrongType(e12, "%string-copy!", 4, end);
                        }
                    } catch (ClassCastException e13) {
                        throw new WrongType(e13, "%string-copy!", 3, i0);
                    }
                } catch (ClassCastException e14) {
                    throw new WrongType(e14, "%string-copy!", 2, s);
                }
            } catch (ClassCastException e15) {
                throw new WrongType(e15, "%string-copy!", 1, tstart);
            }
        } catch (ClassCastException e16) {
            throw new WrongType(e16, "%string-copy!", 0, target);
        }
    }

    public static Object stringJoin$V(Object strings, Object[] argsArray) {
        Object obj;
        Object error$V;
        LList delim$Plgrammar = LList.makeList(argsArray, 0);
        ApplyToArgs applyToArgs = Scheme.applyToArgs;
        try {
            Object obj2 = loc$let$Mnoptionals$St.get();
            try {
                try {
                    try {
                        Object apply2 = Scheme.applyToArgs.apply2(Scheme.applyToArgs.apply3(loc$delim.get(), " ", strings.isString(loc$delim.get()) ? Boolean.TRUE : Boolean.FALSE), Scheme.applyToArgs.apply2(loc$grammar.get(), Lit15));
                        if (lists.isPair(strings)) {
                            try {
                                Object tmp = loc$grammar.get();
                                Object x = Scheme.isEqv.apply2(tmp, Lit15);
                                if (x == Boolean.FALSE ? Scheme.isEqv.apply2(tmp, Lit16) != Boolean.FALSE : x != Boolean.FALSE) {
                                    error$V = lists.cons(lists.car.apply1(strings), lambda222buildit(lists.cdr.apply1(strings), LList.Empty));
                                } else if (Scheme.isEqv.apply2(tmp, Lit17) != Boolean.FALSE) {
                                    error$V = lambda222buildit(strings, LList.Empty);
                                } else if (Scheme.isEqv.apply2(tmp, Lit18) != Boolean.FALSE) {
                                    try {
                                        error$V = lists.cons(lists.car.apply1(strings), lambda222buildit(lists.cdr.apply1(strings), LList.list1(loc$delim.get())));
                                    } catch (UnboundLocationException e) {
                                        e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1870, 53);
                                        throw e;
                                    }
                                } else {
                                    Object[] objArr = new Object[2];
                                    try {
                                        objArr[0] = loc$grammar.get();
                                        objArr[1] = string$Mnjoin;
                                        error$V = misc.error$V("Illegal join grammar", objArr);
                                    } catch (UnboundLocationException e2) {
                                        e2.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1873, 9);
                                        throw e2;
                                    }
                                }
                                obj = stringConcatenate(error$V);
                            } catch (UnboundLocationException e3) {
                                e3.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1862, 14);
                                throw e3;
                            }
                        } else if (!lists.isNull(strings)) {
                            obj = misc.error$V("STRINGS parameter not list.", new Object[]{strings, string$Mnjoin});
                        } else {
                            try {
                                if (loc$grammar.get() == Lit16) {
                                    obj = misc.error$V("Empty list cannot be joined with STRICT-INFIX grammar.", new Object[]{string$Mnjoin});
                                } else {
                                    obj = "";
                                }
                            } catch (UnboundLocationException e4) {
                                e4.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1880, 13);
                                throw e4;
                            }
                        }
                        return applyToArgs.apply4(obj2, delim$Plgrammar, apply2, obj);
                    } catch (UnboundLocationException e5) {
                        e5.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1853, 6);
                        throw e5;
                    }
                } catch (UnboundLocationException e6) {
                    e6.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1852, 54);
                    throw e6;
                }
            } catch (UnboundLocationException e7) {
                e7.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1852, 34);
                throw e7;
            }
        } catch (UnboundLocationException e8) {
            e8.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1852, 3);
            throw e8;
        }
    }

    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object applyN(gnu.expr.ModuleMethod r11, java.lang.Object[] r12) {
        /*
            r10 = this;
            r8 = 4
            r7 = 3
            r6 = 2
            r5 = 1
            r2 = 0
            int r1 = r11.selector
            switch(r1) {
                case 200: goto L_0x000f;
                case 201: goto L_0x000a;
                case 202: goto L_0x0028;
                case 203: goto L_0x003f;
                case 204: goto L_0x000a;
                case 205: goto L_0x0058;
                case 206: goto L_0x000a;
                case 207: goto L_0x0071;
                case 208: goto L_0x008c;
                case 209: goto L_0x000a;
                case 210: goto L_0x00a8;
                case 211: goto L_0x000a;
                case 212: goto L_0x00c6;
                case 213: goto L_0x00e4;
                case 214: goto L_0x00fe;
                case 215: goto L_0x0118;
                case 216: goto L_0x0132;
                case 217: goto L_0x000a;
                case 218: goto L_0x000a;
                case 219: goto L_0x014c;
                case 220: goto L_0x015f;
                case 221: goto L_0x0172;
                case 222: goto L_0x01a9;
                case 223: goto L_0x01e0;
                case 224: goto L_0x01fa;
                case 225: goto L_0x0214;
                case 226: goto L_0x022e;
                case 227: goto L_0x0248;
                case 228: goto L_0x0262;
                case 229: goto L_0x027c;
                case 230: goto L_0x0296;
                case 231: goto L_0x02b0;
                case 232: goto L_0x02c3;
                case 233: goto L_0x02d6;
                case 234: goto L_0x02e9;
                case 235: goto L_0x02fc;
                case 236: goto L_0x0319;
                case 237: goto L_0x0336;
                case 238: goto L_0x0356;
                case 239: goto L_0x000a;
                case 240: goto L_0x000a;
                case 241: goto L_0x0376;
                case 242: goto L_0x000a;
                case 243: goto L_0x0390;
                case 244: goto L_0x000a;
                case 245: goto L_0x000a;
                case 246: goto L_0x03aa;
                case 247: goto L_0x000a;
                case 248: goto L_0x000a;
                case 249: goto L_0x03c4;
                case 250: goto L_0x000a;
                case 251: goto L_0x03de;
                case 252: goto L_0x000a;
                case 253: goto L_0x03f8;
                case 254: goto L_0x000a;
                case 255: goto L_0x000a;
                case 256: goto L_0x0412;
                case 257: goto L_0x000a;
                case 258: goto L_0x042c;
                case 259: goto L_0x000a;
                case 260: goto L_0x000a;
                case 261: goto L_0x0446;
                case 262: goto L_0x000a;
                case 263: goto L_0x000a;
                case 264: goto L_0x0460;
                case 265: goto L_0x000a;
                case 266: goto L_0x047a;
                case 267: goto L_0x000a;
                case 268: goto L_0x0494;
                case 269: goto L_0x04ae;
                case 270: goto L_0x04be;
                case 271: goto L_0x000a;
                case 272: goto L_0x04d6;
                case 273: goto L_0x04ee;
                case 274: goto L_0x0506;
                case 275: goto L_0x051e;
                case 276: goto L_0x0536;
                case 277: goto L_0x000a;
                case 278: goto L_0x054e;
                case 279: goto L_0x0566;
                case 280: goto L_0x000a;
                case 281: goto L_0x000a;
                case 282: goto L_0x000a;
                case 283: goto L_0x000a;
                case 284: goto L_0x057e;
                case 285: goto L_0x0596;
                case 286: goto L_0x05ae;
                case 287: goto L_0x000a;
                case 288: goto L_0x05c6;
                case 289: goto L_0x000a;
                case 290: goto L_0x05e0;
                case 291: goto L_0x05fa;
                case 292: goto L_0x0614;
                case 293: goto L_0x062e;
                case 294: goto L_0x0648;
                case 295: goto L_0x0662;
                case 296: goto L_0x067c;
                case 297: goto L_0x0696;
                case 298: goto L_0x06b0;
                case 299: goto L_0x06ca;
                case 300: goto L_0x000a;
                case 301: goto L_0x000a;
                case 302: goto L_0x070b;
                case 303: goto L_0x0737;
                case 304: goto L_0x0751;
                case 305: goto L_0x076b;
                case 306: goto L_0x0781;
                case 307: goto L_0x0799;
                case 308: goto L_0x07ac;
                case 309: goto L_0x000a;
                case 310: goto L_0x07ca;
                case 311: goto L_0x07e2;
                case 312: goto L_0x000a;
                case 313: goto L_0x07fa;
                case 314: goto L_0x0812;
                case 315: goto L_0x000a;
                case 316: goto L_0x000a;
                case 317: goto L_0x0818;
                case 318: goto L_0x0830;
                case 319: goto L_0x000a;
                case 320: goto L_0x0848;
                case 321: goto L_0x0866;
                case 322: goto L_0x000a;
                case 323: goto L_0x087e;
                case 324: goto L_0x000a;
                case 325: goto L_0x000a;
                case 326: goto L_0x0898;
                case 327: goto L_0x08b6;
                case 328: goto L_0x08cc;
                default: goto L_0x000a;
            }
        L_0x000a:
            java.lang.Object r1 = super.applyN(r11, r12)
        L_0x000e:
            return r1
        L_0x000f:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0018:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0021
            java.lang.Object r1 = substring$SlShared$V(r3, r4, r1)
            goto L_0x000e
        L_0x0021:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0018
        L_0x0028:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x002f:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0038
            java.lang.Object r1 = stringCopy$V(r3, r1)
            goto L_0x000e
        L_0x0038:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x002f
        L_0x003f:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0048:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0051
            java.lang.Object r1 = stringMap$V(r3, r4, r1)
            goto L_0x000e
        L_0x0051:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0048
        L_0x0058:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0061:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x006a
            java.lang.Object r1 = stringMap$Ex$V(r3, r4, r1)
            goto L_0x000e
        L_0x006a:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0061
        L_0x0071:
            r3 = r12[r2]
            r4 = r12[r5]
            r5 = r12[r6]
            int r1 = r12.length
            int r2 = r1 + -3
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x007c:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0085
            java.lang.Object r1 = stringFold$V(r3, r4, r5, r1)
            goto L_0x000e
        L_0x0085:
            int r6 = r2 + 3
            r6 = r12[r6]
            r1[r2] = r6
            goto L_0x007c
        L_0x008c:
            r3 = r12[r2]
            r4 = r12[r5]
            r5 = r12[r6]
            int r1 = r12.length
            int r2 = r1 + -3
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0097:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x00a1
            java.lang.Object r1 = stringFoldRight$V(r3, r4, r5, r1)
            goto L_0x000e
        L_0x00a1:
            int r6 = r2 + 3
            r6 = r12[r6]
            r1[r2] = r6
            goto L_0x0097
        L_0x00a8:
            r3 = r12[r2]
            r4 = r12[r5]
            r5 = r12[r6]
            r6 = r12[r7]
            int r1 = r12.length
            int r2 = r1 + -4
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x00b5:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x00bf
            java.lang.Object r1 = stringUnfold$V(r3, r4, r5, r6, r1)
            goto L_0x000e
        L_0x00bf:
            int r7 = r2 + 4
            r7 = r12[r7]
            r1[r2] = r7
            goto L_0x00b5
        L_0x00c6:
            r3 = r12[r2]
            r4 = r12[r5]
            r5 = r12[r6]
            r6 = r12[r7]
            int r1 = r12.length
            int r2 = r1 + -4
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x00d3:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x00dd
            java.lang.Object r1 = stringUnfoldRight$V(r3, r4, r5, r6, r1)
            goto L_0x000e
        L_0x00dd:
            int r7 = r2 + 4
            r7 = r12[r7]
            r1[r2] = r7
            goto L_0x00d3
        L_0x00e4:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x00ed:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x00f7
            java.lang.Object r1 = stringForEach$V(r3, r4, r1)
            goto L_0x000e
        L_0x00f7:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x00ed
        L_0x00fe:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0107:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0111
            java.lang.Object r1 = stringForEachIndex$V(r3, r4, r1)
            goto L_0x000e
        L_0x0111:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0107
        L_0x0118:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0121:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x012b
            java.lang.Object r1 = stringEvery$V(r3, r4, r1)
            goto L_0x000e
        L_0x012b:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0121
        L_0x0132:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x013b:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0145
            java.lang.Object r1 = stringAny$V(r3, r4, r1)
            goto L_0x000e
        L_0x0145:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x013b
        L_0x014c:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            java.lang.Object r1 = $PcStringPrefixLength(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x015f:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            java.lang.Object r1 = $PcStringSuffixLength(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x0172:
            r1 = r12[r2]
            r3 = r12[r5]
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x08e4 }
            r2 = r0
            int r2 = r2.intValue()     // Catch:{ ClassCastException -> 0x08e4 }
            r4 = r12[r6]
            r0 = r4
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x08ed }
            r3 = r0
            int r3 = r3.intValue()     // Catch:{ ClassCastException -> 0x08ed }
            r4 = r12[r7]
            r6 = r12[r8]
            r0 = r6
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x08f6 }
            r5 = r0
            int r5 = r5.intValue()     // Catch:{ ClassCastException -> 0x08f6 }
            r6 = 5
            r7 = r12[r6]
            r0 = r7
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0900 }
            r6 = r0
            int r6 = r6.intValue()     // Catch:{ ClassCastException -> 0x0900 }
            int r1 = $PcStringPrefixLengthCi(r1, r2, r3, r4, r5, r6)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            goto L_0x000e
        L_0x01a9:
            r1 = r12[r2]
            r3 = r12[r5]
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x090a }
            r2 = r0
            int r2 = r2.intValue()     // Catch:{ ClassCastException -> 0x090a }
            r4 = r12[r6]
            r0 = r4
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0913 }
            r3 = r0
            int r3 = r3.intValue()     // Catch:{ ClassCastException -> 0x0913 }
            r4 = r12[r7]
            r6 = r12[r8]
            r0 = r6
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x091c }
            r5 = r0
            int r5 = r5.intValue()     // Catch:{ ClassCastException -> 0x091c }
            r6 = 5
            r7 = r12[r6]
            r0 = r7
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0926 }
            r6 = r0
            int r6 = r6.intValue()     // Catch:{ ClassCastException -> 0x0926 }
            int r1 = $PcStringSuffixLengthCi(r1, r2, r3, r4, r5, r6)
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            goto L_0x000e
        L_0x01e0:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x01e9:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x01f3
            java.lang.Object r1 = stringPrefixLength$V(r3, r4, r1)
            goto L_0x000e
        L_0x01f3:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x01e9
        L_0x01fa:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0203:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x020d
            java.lang.Object r1 = stringSuffixLength$V(r3, r4, r1)
            goto L_0x000e
        L_0x020d:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0203
        L_0x0214:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x021d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0227
            java.lang.Object r1 = stringPrefixLengthCi$V(r3, r4, r1)
            goto L_0x000e
        L_0x0227:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x021d
        L_0x022e:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0237:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0241
            java.lang.Object r1 = stringSuffixLengthCi$V(r3, r4, r1)
            goto L_0x000e
        L_0x0241:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0237
        L_0x0248:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0251:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x025b
            java.lang.Object r1 = isStringPrefix$V(r3, r4, r1)
            goto L_0x000e
        L_0x025b:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0251
        L_0x0262:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x026b:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0275
            java.lang.Object r1 = isStringSuffix$V(r3, r4, r1)
            goto L_0x000e
        L_0x0275:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x026b
        L_0x027c:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0285:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x028f
            java.lang.Object r1 = isStringPrefixCi$V(r3, r4, r1)
            goto L_0x000e
        L_0x028f:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0285
        L_0x0296:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x029f:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x02a9
            java.lang.Object r1 = isStringSuffixCi$V(r3, r4, r1)
            goto L_0x000e
        L_0x02a9:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x029f
        L_0x02b0:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            java.lang.Object r1 = $PcStringPrefix$Qu(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x02c3:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            java.lang.Object r1 = $PcStringSuffix$Qu(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x02d6:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            java.lang.Object r1 = $PcStringPrefixCi$Qu(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x02e9:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            java.lang.Object r1 = $PcStringSuffixCi$Qu(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x02fc:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            r7 = 6
            r7 = r12[r7]
            r8 = 7
            r8 = r12[r8]
            r9 = 8
            r9 = r12[r9]
            java.lang.Object r1 = $PcStringCompare(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x000e
        L_0x0319:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            r7 = 6
            r7 = r12[r7]
            r8 = 7
            r8 = r12[r8]
            r9 = 8
            r9 = r12[r9]
            java.lang.Object r1 = $PcStringCompareCi(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            goto L_0x000e
        L_0x0336:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            int r6 = r12.length
            int r7 = r6 + -5
            java.lang.Object[] r6 = new java.lang.Object[r7]
        L_0x0345:
            int r7 = r7 + -1
            if (r7 >= 0) goto L_0x034f
            java.lang.Object r1 = stringCompare$V(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x034f:
            int r8 = r7 + 5
            r8 = r12[r8]
            r6[r7] = r8
            goto L_0x0345
        L_0x0356:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            int r6 = r12.length
            int r7 = r6 + -5
            java.lang.Object[] r6 = new java.lang.Object[r7]
        L_0x0365:
            int r7 = r7 + -1
            if (r7 >= 0) goto L_0x036f
            java.lang.Object r1 = stringCompareCi$V(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x036f:
            int r8 = r7 + 5
            r8 = r12[r8]
            r6[r7] = r8
            goto L_0x0365
        L_0x0376:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x037f:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0389
            java.lang.Object r1 = string$Eq$V(r3, r4, r1)
            goto L_0x000e
        L_0x0389:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x037f
        L_0x0390:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0399:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x03a3
            java.lang.Object r1 = string$Ls$Gr$V(r3, r4, r1)
            goto L_0x000e
        L_0x03a3:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0399
        L_0x03aa:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x03b3:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x03bd
            java.lang.Object r1 = string$Ls$V(r3, r4, r1)
            goto L_0x000e
        L_0x03bd:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x03b3
        L_0x03c4:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x03cd:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x03d7
            java.lang.Object r1 = string$Gr$V(r3, r4, r1)
            goto L_0x000e
        L_0x03d7:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x03cd
        L_0x03de:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x03e7:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x03f1
            java.lang.Object r1 = string$Ls$Eq$V(r3, r4, r1)
            goto L_0x000e
        L_0x03f1:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x03e7
        L_0x03f8:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0401:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x040b
            java.lang.Object r1 = string$Gr$Eq$V(r3, r4, r1)
            goto L_0x000e
        L_0x040b:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0401
        L_0x0412:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x041b:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0425
            java.lang.Object r1 = stringCi$Eq$V(r3, r4, r1)
            goto L_0x000e
        L_0x0425:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x041b
        L_0x042c:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0435:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x043f
            java.lang.Object r1 = stringCi$Ls$Gr$V(r3, r4, r1)
            goto L_0x000e
        L_0x043f:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0435
        L_0x0446:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x044f:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0459
            java.lang.Object r1 = stringCi$Ls$V(r3, r4, r1)
            goto L_0x000e
        L_0x0459:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x044f
        L_0x0460:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0469:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0473
            java.lang.Object r1 = stringCi$Gr$V(r3, r4, r1)
            goto L_0x000e
        L_0x0473:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0469
        L_0x047a:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0483:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x048d
            java.lang.Object r1 = stringCi$Ls$Eq$V(r3, r4, r1)
            goto L_0x000e
        L_0x048d:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0483
        L_0x0494:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x049d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x04a7
            java.lang.Object r1 = stringCi$Gr$Eq$V(r3, r4, r1)
            goto L_0x000e
        L_0x04a7:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x049d
        L_0x04ae:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            java.lang.Object r1 = $PcStringHash(r1, r2, r3, r4, r5)
            goto L_0x000e
        L_0x04be:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x04c5:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x04cf
            java.lang.Object r1 = stringHash$V(r3, r1)
            goto L_0x000e
        L_0x04cf:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x04c5
        L_0x04d6:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x04dd:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x04e7
            java.lang.Object r1 = stringHashCi$V(r3, r1)
            goto L_0x000e
        L_0x04e7:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x04dd
        L_0x04ee:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x04f5:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x04ff
            java.lang.Object r1 = stringUpcase$V(r3, r1)
            goto L_0x000e
        L_0x04ff:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x04f5
        L_0x0506:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x050d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0517
            java.lang.Object r1 = stringUpcase$Ex$V(r3, r1)
            goto L_0x000e
        L_0x0517:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x050d
        L_0x051e:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0525:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x052f
            java.lang.Object r1 = stringDowncase$V(r3, r1)
            goto L_0x000e
        L_0x052f:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x0525
        L_0x0536:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x053d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0547
            java.lang.Object r1 = stringDowncase$Ex$V(r3, r1)
            goto L_0x000e
        L_0x0547:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x053d
        L_0x054e:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0555:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x055f
            java.lang.Object r1 = stringTitlecase$Ex$V(r3, r1)
            goto L_0x000e
        L_0x055f:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x0555
        L_0x0566:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x056d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0577
            java.lang.Object r1 = stringTitlecase$V(r3, r1)
            goto L_0x000e
        L_0x0577:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x056d
        L_0x057e:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0585:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x058f
            java.lang.Object r1 = stringTrim$V(r3, r1)
            goto L_0x000e
        L_0x058f:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x0585
        L_0x0596:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x059d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x05a7
            java.lang.Object r1 = stringTrimRight$V(r3, r1)
            goto L_0x000e
        L_0x05a7:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x059d
        L_0x05ae:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x05b5:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x05bf
            java.lang.Object r1 = stringTrimBoth$V(r3, r1)
            goto L_0x000e
        L_0x05bf:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x05b5
        L_0x05c6:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x05cf:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x05d9
            java.lang.Object r1 = stringPadRight$V(r3, r4, r1)
            goto L_0x000e
        L_0x05d9:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x05cf
        L_0x05e0:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x05e9:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x05f3
            java.lang.Object r1 = stringPad$V(r3, r4, r1)
            goto L_0x000e
        L_0x05f3:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x05e9
        L_0x05fa:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0603:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x060d
            java.lang.Object r1 = stringDelete$V(r3, r4, r1)
            goto L_0x000e
        L_0x060d:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0603
        L_0x0614:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x061d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0627
            java.lang.Object r1 = stringFilter$V(r3, r4, r1)
            goto L_0x000e
        L_0x0627:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x061d
        L_0x062e:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0637:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0641
            java.lang.Object r1 = stringIndex$V(r3, r4, r1)
            goto L_0x000e
        L_0x0641:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0637
        L_0x0648:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0651:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x065b
            java.lang.Object r1 = stringIndexRight$V(r3, r4, r1)
            goto L_0x000e
        L_0x065b:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0651
        L_0x0662:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x066b:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0675
            java.lang.Object r1 = stringSkip$V(r3, r4, r1)
            goto L_0x000e
        L_0x0675:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x066b
        L_0x067c:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0685:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x068f
            java.lang.Object r1 = stringSkipRight$V(r3, r4, r1)
            goto L_0x000e
        L_0x068f:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0685
        L_0x0696:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x069f:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x06a9
            java.lang.Object r1 = stringCount$V(r3, r4, r1)
            goto L_0x000e
        L_0x06a9:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x069f
        L_0x06b0:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x06b9:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x06c3
            java.lang.Object r1 = stringFill$Ex$V(r3, r4, r1)
            goto L_0x000e
        L_0x06c3:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x06b9
        L_0x06ca:
            int r1 = r12.length
            int r3 = r1 + -3
            r4 = r12[r2]
            r2 = r12[r5]
            r0 = r2
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0930 }
            r1 = r0
            int r5 = r1.intValue()     // Catch:{ ClassCastException -> 0x0930 }
            r1 = r12[r6]
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1     // Catch:{ ClassCastException -> 0x0939 }
            if (r3 > 0) goto L_0x06e5
            java.lang.Object r1 = stringCopy$Ex(r4, r5, r1)
            goto L_0x000e
        L_0x06e5:
            int r6 = r3 + -1
            r3 = r12[r7]
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0942 }
            r2 = r0
            int r7 = r2.intValue()     // Catch:{ ClassCastException -> 0x0942 }
            if (r6 > 0) goto L_0x06f9
            java.lang.Object r1 = stringCopy$Ex(r4, r5, r1, r7)
            goto L_0x000e
        L_0x06f9:
            int r2 = r6 + -1
            r3 = r12[r8]
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x094b }
            r2 = r0
            int r2 = r2.intValue()     // Catch:{ ClassCastException -> 0x094b }
            java.lang.Object r1 = stringCopy$Ex(r4, r5, r1, r7, r2)
            goto L_0x000e
        L_0x070b:
            r1 = r12[r2]
            java.lang.CharSequence r1 = (java.lang.CharSequence) r1     // Catch:{ ClassCastException -> 0x0955 }
            r3 = r12[r5]
            r0 = r3
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x095e }
            r2 = r0
            int r5 = r2.intValue()     // Catch:{ ClassCastException -> 0x095e }
            r2 = r12[r6]
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ ClassCastException -> 0x0967 }
            r4 = r12[r7]
            r0 = r4
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0970 }
            r3 = r0
            int r6 = r3.intValue()     // Catch:{ ClassCastException -> 0x0970 }
            r4 = r12[r8]
            r0 = r4
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0979 }
            r3 = r0
            int r3 = r3.intValue()     // Catch:{ ClassCastException -> 0x0979 }
            java.lang.Object r1 = $PcStringCopy$Ex(r1, r5, r2, r6, r3)
            goto L_0x000e
        L_0x0737:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0740:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x074a
            java.lang.Object r1 = stringContains$V(r3, r4, r1)
            goto L_0x000e
        L_0x074a:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0740
        L_0x0751:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x075a:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0764
            java.lang.Object r1 = stringContainsCi$V(r3, r4, r1)
            goto L_0x000e
        L_0x0764:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x075a
        L_0x076b:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            r7 = 6
            r7 = r12[r7]
            java.lang.Object r1 = $PcKmpSearch(r1, r2, r3, r4, r5, r6, r7)
            goto L_0x000e
        L_0x0781:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0788:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0792
            java.lang.Object r1 = makeKmpRestartVector$V(r3, r1)
            goto L_0x000e
        L_0x0792:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x0788
        L_0x0799:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            java.lang.Object r1 = kmpStep(r1, r2, r3, r4, r5, r6)
            goto L_0x000e
        L_0x07ac:
            r3 = r12[r2]
            r4 = r12[r5]
            r5 = r12[r6]
            r6 = r12[r7]
            int r1 = r12.length
            int r2 = r1 + -4
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x07b9:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x07c3
            java.lang.Object r1 = stringKmpPartialSearch$V(r3, r4, r5, r6, r1)
            goto L_0x000e
        L_0x07c3:
            int r7 = r2 + 4
            r7 = r12[r7]
            r1[r2] = r7
            goto L_0x07b9
        L_0x07ca:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x07d1:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x07db
            java.lang.Object r1 = stringReverse$V(r3, r1)
            goto L_0x000e
        L_0x07db:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x07d1
        L_0x07e2:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x07e9:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x07f3
            java.lang.Object r1 = stringReverse$Ex$V(r3, r1)
            goto L_0x000e
        L_0x07f3:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x07e9
        L_0x07fa:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0801:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x080b
            java.lang.Object r1 = string$To$List$V(r3, r1)
            goto L_0x000e
        L_0x080b:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x0801
        L_0x0812:
            java.lang.Object r1 = stringAppend$SlShared$V(r12)
            goto L_0x000e
        L_0x0818:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x081f:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0829
            java.lang.Object r1 = stringConcatenateReverse$V(r3, r1)
            goto L_0x000e
        L_0x0829:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x081f
        L_0x0830:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0837:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0841
            java.lang.Object r1 = stringConcatenateReverse$SlShared$V(r3, r1)
            goto L_0x000e
        L_0x0841:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x0837
        L_0x0848:
            r3 = r12[r2]
            r4 = r12[r5]
            r5 = r12[r6]
            r6 = r12[r7]
            int r1 = r12.length
            int r2 = r1 + -4
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0855:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x085f
            java.lang.Object r1 = stringReplace$V(r3, r4, r5, r6, r1)
            goto L_0x000e
        L_0x085f:
            int r7 = r2 + 4
            r7 = r12[r7]
            r1[r2] = r7
            goto L_0x0855
        L_0x0866:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x086d:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0877
            java.lang.Object r1 = stringTokenize$V(r3, r1)
            goto L_0x000e
        L_0x0877:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x086d
        L_0x087e:
            r3 = r12[r2]
            r4 = r12[r5]
            int r1 = r12.length
            int r2 = r1 + -2
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x0887:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x0891
            java.lang.Object r1 = xsubstring$V(r3, r4, r1)
            goto L_0x000e
        L_0x0891:
            int r5 = r2 + 2
            r5 = r12[r5]
            r1[r2] = r5
            goto L_0x0887
        L_0x0898:
            r3 = r12[r2]
            r4 = r12[r5]
            r5 = r12[r6]
            r6 = r12[r7]
            int r1 = r12.length
            int r2 = r1 + -4
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x08a5:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x08af
            java.lang.Object r1 = stringXcopy$Ex$V(r3, r4, r5, r6, r1)
            goto L_0x000e
        L_0x08af:
            int r7 = r2 + 4
            r7 = r12[r7]
            r1[r2] = r7
            goto L_0x08a5
        L_0x08b6:
            r1 = r12[r2]
            r2 = r12[r5]
            r3 = r12[r6]
            r4 = r12[r7]
            r5 = r12[r8]
            r6 = 5
            r6 = r12[r6]
            r7 = 6
            r7 = r12[r7]
            java.lang.Object r1 = $PcMultispanRepcopy$Ex(r1, r2, r3, r4, r5, r6, r7)
            goto L_0x000e
        L_0x08cc:
            r3 = r12[r2]
            int r1 = r12.length
            int r2 = r1 + -1
            java.lang.Object[] r1 = new java.lang.Object[r2]
        L_0x08d3:
            int r2 = r2 + -1
            if (r2 >= 0) goto L_0x08dd
            java.lang.Object r1 = stringJoin$V(r3, r1)
            goto L_0x000e
        L_0x08dd:
            int r4 = r2 + 1
            r4 = r12[r4]
            r1[r2] = r4
            goto L_0x08d3
        L_0x08e4:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r4 = "%string-prefix-length-ci"
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r6, (java.lang.Object) r3)
            throw r2
        L_0x08ed:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-prefix-length-ci"
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r7, (java.lang.Object) r4)
            throw r2
        L_0x08f6:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-prefix-length-ci"
            r4 = 5
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r4, (java.lang.Object) r6)
            throw r2
        L_0x0900:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-prefix-length-ci"
            r4 = 6
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r4, (java.lang.Object) r7)
            throw r2
        L_0x090a:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r4 = "%string-suffix-length-ci"
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r6, (java.lang.Object) r3)
            throw r2
        L_0x0913:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-suffix-length-ci"
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r7, (java.lang.Object) r4)
            throw r2
        L_0x091c:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-suffix-length-ci"
            r4 = 5
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r4, (java.lang.Object) r6)
            throw r2
        L_0x0926:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-suffix-length-ci"
            r4 = 6
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r4, (java.lang.Object) r7)
            throw r2
        L_0x0930:
            r1 = move-exception
            gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
            java.lang.String r4 = "string-copy!"
            r3.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r6, (java.lang.Object) r2)
            throw r3
        L_0x0939:
            r2 = move-exception
            gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
            java.lang.String r4 = "string-copy!"
            r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r7, (java.lang.Object) r1)
            throw r3
        L_0x0942:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r4 = "string-copy!"
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r8, (java.lang.Object) r3)
            throw r2
        L_0x094b:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r4 = "string-copy!"
            r5 = 5
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r5, (java.lang.Object) r3)
            throw r2
        L_0x0955:
            r2 = move-exception
            gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
            java.lang.String r4 = "%string-copy!"
            r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r1)
            throw r3
        L_0x095e:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r4 = "%string-copy!"
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r6, (java.lang.Object) r3)
            throw r2
        L_0x0967:
            r1 = move-exception
            gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
            java.lang.String r4 = "%string-copy!"
            r3.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r7, (java.lang.Object) r2)
            throw r3
        L_0x0970:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-copy!"
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r8, (java.lang.Object) r4)
            throw r2
        L_0x0979:
            r1 = move-exception
            gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
            java.lang.String r3 = "%string-copy!"
            r5 = 5
            r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r3, (int) r5, (java.lang.Object) r4)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.srfi13.applyN(gnu.expr.ModuleMethod, java.lang.Object[]):java.lang.Object");
    }

    public static Object lambda222buildit(Object lis, Object obj) {
        frame96 frame962 = new frame96();
        frame962.f3final = obj;
        return frame962.lambda223recur(lis);
    }

    /* compiled from: srfi13.scm */
    public class frame96 extends ModuleBody {

        /* renamed from: final  reason: not valid java name */
        Object f3final;

        public Object lambda223recur(Object lis) {
            if (!lists.isPair(lis)) {
                return this.f3final;
            }
            try {
                return lists.cons(srfi13.loc$delim.get(), lists.cons(lists.car.apply1(lis), lambda223recur(lists.cdr.apply1(lis))));
            } catch (UnboundLocationException e) {
                e.setLine("/u2/home/jis/ai2-kawa/gnu/kawa/slib/srfi13.scm", 1857, 13);
                throw e;
            }
        }
    }
}
