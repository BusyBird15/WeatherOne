package gnu.kawa.slib;

import androidx.fragment.app.FragmentTransaction;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.AddOp;
import gnu.lists.Consumer;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.mapping.CallContext;
import gnu.mapping.Procedure;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import gnu.text.Char;
import kawa.lib.characters;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.numbers;
import kawa.lib.ports;
import kawa.lib.rnrs.unicode;
import kawa.lib.strings;
import kawa.standard.Scheme;
import kawa.standard.append;

/* compiled from: pregexp.scm */
public class pregexp extends ModuleBody {
    public static Char $Stpregexp$Mncomment$Mnchar$St;
    public static Object $Stpregexp$Mnnul$Mnchar$Mnint$St;
    public static Object $Stpregexp$Mnreturn$Mnchar$St;
    public static Object $Stpregexp$Mnspace$Mnsensitive$Qu$St;
    public static Object $Stpregexp$Mntab$Mnchar$St;
    public static IntNum $Stpregexp$Mnversion$St;
    public static final pregexp $instance = new pregexp();
    static final IntNum Lit0 = IntNum.make(20050502);
    static final Char Lit1 = Char.make(59);
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol(":bos").readResolve());
    static final SimpleSymbol Lit100 = ((SimpleSymbol) new SimpleSymbol(":sub").readResolve());
    static final SimpleSymbol Lit101 = ((SimpleSymbol) new SimpleSymbol("pregexp-match-positions-aux").readResolve());
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol("non-existent-backref").readResolve());
    static final SimpleSymbol Lit103 = ((SimpleSymbol) new SimpleSymbol(":lookahead").readResolve());
    static final SimpleSymbol Lit104 = ((SimpleSymbol) new SimpleSymbol(":neg-lookahead").readResolve());
    static final SimpleSymbol Lit105 = ((SimpleSymbol) new SimpleSymbol(":lookbehind").readResolve());
    static final PairWithPosition Lit106 = PairWithPosition.make(Lit68, PairWithPosition.make(Boolean.FALSE, PairWithPosition.make(Lit73, PairWithPosition.make(Boolean.FALSE, PairWithPosition.make(Lit14, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2302017), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2302014), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2302012), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2302009), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2301999);
    static final SimpleSymbol Lit107 = ((SimpleSymbol) new SimpleSymbol(":neg-lookbehind").readResolve());
    static final PairWithPosition Lit108;
    static final SimpleSymbol Lit109 = ((SimpleSymbol) new SimpleSymbol(":no-backtrack").readResolve());
    static final Char Lit11;
    static final SimpleSymbol Lit110 = ((SimpleSymbol) new SimpleSymbol("greedy-quantifier-operand-could-be-empty").readResolve());
    static final SimpleSymbol Lit111 = ((SimpleSymbol) new SimpleSymbol("fk").readResolve());
    static final SimpleSymbol Lit112 = ((SimpleSymbol) new SimpleSymbol("identity").readResolve());
    static final Char Lit113 = Char.make(38);
    static final SimpleSymbol Lit114 = ((SimpleSymbol) new SimpleSymbol("pregexp-match-positions").readResolve());
    static final SimpleSymbol Lit115 = ((SimpleSymbol) new SimpleSymbol("pattern-must-be-compiled-or-string-regexp").readResolve());
    static final PairWithPosition Lit116;
    static final SimpleSymbol Lit117 = ((SimpleSymbol) new SimpleSymbol("pregexp-reverse!").readResolve());
    static final SimpleSymbol Lit118 = ((SimpleSymbol) new SimpleSymbol("pregexp-error").readResolve());
    static final SimpleSymbol Lit119 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-pattern").readResolve());
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol(":eos").readResolve());
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-branch").readResolve());
    static final SimpleSymbol Lit121 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-escaped-number").readResolve());
    static final SimpleSymbol Lit122 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-escaped-char").readResolve());
    static final SimpleSymbol Lit123 = ((SimpleSymbol) new SimpleSymbol("pregexp-invert-char-list").readResolve());
    static final SimpleSymbol Lit124 = ((SimpleSymbol) new SimpleSymbol("pregexp-string-match").readResolve());
    static final SimpleSymbol Lit125 = ((SimpleSymbol) new SimpleSymbol("pregexp-char-word?").readResolve());
    static final SimpleSymbol Lit126 = ((SimpleSymbol) new SimpleSymbol("pregexp-at-word-boundary?").readResolve());
    static final SimpleSymbol Lit127 = ((SimpleSymbol) new SimpleSymbol("pregexp-list-ref").readResolve());
    static final SimpleSymbol Lit128 = ((SimpleSymbol) new SimpleSymbol("pregexp-make-backref-list").readResolve());
    static final SimpleSymbol Lit129 = ((SimpleSymbol) new SimpleSymbol("pregexp-replace-aux").readResolve());
    static final Char Lit13;
    static final SimpleSymbol Lit130 = ((SimpleSymbol) new SimpleSymbol("pregexp").readResolve());
    static final SimpleSymbol Lit131 = ((SimpleSymbol) new SimpleSymbol("pregexp-match").readResolve());
    static final SimpleSymbol Lit132 = ((SimpleSymbol) new SimpleSymbol("pregexp-split").readResolve());
    static final SimpleSymbol Lit133 = ((SimpleSymbol) new SimpleSymbol("pregexp-replace").readResolve());
    static final SimpleSymbol Lit134 = ((SimpleSymbol) new SimpleSymbol("pregexp-replace*").readResolve());
    static final SimpleSymbol Lit135 = ((SimpleSymbol) new SimpleSymbol("pregexp-quote").readResolve());
    static final SimpleSymbol Lit14;
    static final Char Lit15;
    static final IntNum Lit16 = IntNum.make(2);
    static final SimpleSymbol Lit17;
    static final Char Lit18;
    static final Char Lit19;
    static final Char Lit2 = Char.make(97);
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol(":backref").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-piece").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("backslash").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol(":empty").readResolve());
    static final Char Lit24 = Char.make(10);
    static final Char Lit25 = Char.make(98);
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol(":wbdry").readResolve());
    static final Char Lit27 = Char.make(66);
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol(":not-wbdry").readResolve());
    static final Char Lit29 = Char.make(100);
    static final Char Lit3 = Char.make(32);
    static final SimpleSymbol Lit30;
    static final Char Lit31 = Char.make(68);
    static final PairWithPosition Lit32;
    static final Char Lit33 = Char.make(110);
    static final Char Lit34 = Char.make(114);
    static final Char Lit35 = Char.make(115);
    static final SimpleSymbol Lit36;
    static final Char Lit37 = Char.make(83);
    static final PairWithPosition Lit38;
    static final Char Lit39 = Char.make(116);
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol(":or").readResolve());
    static final Char Lit40 = Char.make(119);
    static final SimpleSymbol Lit41;
    static final Char Lit42 = Char.make(87);
    static final PairWithPosition Lit43;
    static final Char Lit44 = Char.make(58);
    static final SimpleSymbol Lit45 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-posix-char-class").readResolve());
    static final Char Lit46;
    static final Char Lit47;
    static final Char Lit48 = Char.make(61);
    static final PairWithPosition Lit49 = PairWithPosition.make(Lit103, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 851996);
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol(":seq").readResolve());
    static final Char Lit50 = Char.make(33);
    static final PairWithPosition Lit51 = PairWithPosition.make(Lit104, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 856092);
    static final Char Lit52 = Char.make(62);
    static final PairWithPosition Lit53 = PairWithPosition.make(Lit109, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 860188);
    static final Char Lit54 = Char.make(60);
    static final PairWithPosition Lit55 = PairWithPosition.make(Lit105, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 872479);
    static final PairWithPosition Lit56 = PairWithPosition.make(Lit107, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 876575);
    static final SimpleSymbol Lit57 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-cluster-type").readResolve());
    static final Char Lit58 = Char.make(45);
    static final Char Lit59 = Char.make(105);
    static final Char Lit6;
    static final SimpleSymbol Lit60 = ((SimpleSymbol) new SimpleSymbol(":case-sensitive").readResolve());
    static final SimpleSymbol Lit61 = ((SimpleSymbol) new SimpleSymbol(":case-insensitive").readResolve());
    static final Char Lit62 = Char.make(120);
    static final PairWithPosition Lit63 = PairWithPosition.make(Lit100, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 942102);
    static final SimpleSymbol Lit64 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-subpattern").readResolve());
    static final Char Lit65;
    static final Char Lit66;
    static final Char Lit67;
    static final SimpleSymbol Lit68;
    static final SimpleSymbol Lit69 = ((SimpleSymbol) new SimpleSymbol("minimal?").readResolve());
    static final Char Lit7;
    static final SimpleSymbol Lit70 = ((SimpleSymbol) new SimpleSymbol("at-least").readResolve());
    static final SimpleSymbol Lit71 = ((SimpleSymbol) new SimpleSymbol("at-most").readResolve());
    static final SimpleSymbol Lit72 = ((SimpleSymbol) new SimpleSymbol("next-i").readResolve());
    static final IntNum Lit73;
    static final SimpleSymbol Lit74 = ((SimpleSymbol) new SimpleSymbol("pregexp-wrap-quantifier-if-any").readResolve());
    static final SimpleSymbol Lit75 = ((SimpleSymbol) new SimpleSymbol("left-brace-must-be-followed-by-number").readResolve());
    static final SimpleSymbol Lit76 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-nums").readResolve());
    static final Char Lit77 = Char.make(44);
    static final Char Lit78;
    static final SimpleSymbol Lit79 = ((SimpleSymbol) new SimpleSymbol(":none-of-chars").readResolve());
    static final IntNum Lit8 = IntNum.make(1);
    static final SimpleSymbol Lit80 = ((SimpleSymbol) new SimpleSymbol("pregexp-read-char-list").readResolve());
    static final SimpleSymbol Lit81 = ((SimpleSymbol) new SimpleSymbol("character-class-ended-too-soon").readResolve());
    static final SimpleSymbol Lit82 = ((SimpleSymbol) new SimpleSymbol(":one-of-chars").readResolve());
    static final SimpleSymbol Lit83 = ((SimpleSymbol) new SimpleSymbol(":char-range").readResolve());
    static final Char Lit84 = Char.make(95);
    static final SimpleSymbol Lit85 = ((SimpleSymbol) new SimpleSymbol(":alnum").readResolve());
    static final SimpleSymbol Lit86 = ((SimpleSymbol) new SimpleSymbol(":alpha").readResolve());
    static final SimpleSymbol Lit87 = ((SimpleSymbol) new SimpleSymbol(":ascii").readResolve());
    static final SimpleSymbol Lit88 = ((SimpleSymbol) new SimpleSymbol(":blank").readResolve());
    static final SimpleSymbol Lit89 = ((SimpleSymbol) new SimpleSymbol(":cntrl").readResolve());
    static final Char Lit9;
    static final SimpleSymbol Lit90 = ((SimpleSymbol) new SimpleSymbol(":graph").readResolve());
    static final SimpleSymbol Lit91 = ((SimpleSymbol) new SimpleSymbol(":lower").readResolve());
    static final SimpleSymbol Lit92 = ((SimpleSymbol) new SimpleSymbol(":print").readResolve());
    static final SimpleSymbol Lit93 = ((SimpleSymbol) new SimpleSymbol(":punct").readResolve());
    static final SimpleSymbol Lit94 = ((SimpleSymbol) new SimpleSymbol(":upper").readResolve());
    static final SimpleSymbol Lit95 = ((SimpleSymbol) new SimpleSymbol(":xdigit").readResolve());
    static final Char Lit96 = Char.make(99);
    static final Char Lit97 = Char.make(101);
    static final Char Lit98 = Char.make(102);
    static final SimpleSymbol Lit99 = ((SimpleSymbol) new SimpleSymbol("pregexp-check-if-in-char-class?").readResolve());
    static final ModuleMethod lambda$Fn1;
    static final ModuleMethod lambda$Fn10;
    static final ModuleMethod lambda$Fn6;
    static final ModuleMethod lambda$Fn7;
    static final ModuleMethod lambda$Fn8;
    static final ModuleMethod lambda$Fn9;
    public static final ModuleMethod pregexp;
    public static final ModuleMethod pregexp$Mnat$Mnword$Mnboundary$Qu;
    public static final ModuleMethod pregexp$Mnchar$Mnword$Qu;
    public static final ModuleMethod pregexp$Mncheck$Mnif$Mnin$Mnchar$Mnclass$Qu;
    public static final ModuleMethod pregexp$Mnerror;
    public static final ModuleMethod pregexp$Mninvert$Mnchar$Mnlist;
    public static final ModuleMethod pregexp$Mnlist$Mnref;
    public static final ModuleMethod pregexp$Mnmake$Mnbackref$Mnlist;
    public static final ModuleMethod pregexp$Mnmatch;
    public static final ModuleMethod pregexp$Mnmatch$Mnpositions;
    public static final ModuleMethod pregexp$Mnmatch$Mnpositions$Mnaux;
    public static final ModuleMethod pregexp$Mnquote;
    public static final ModuleMethod pregexp$Mnread$Mnbranch;
    public static final ModuleMethod pregexp$Mnread$Mnchar$Mnlist;
    public static final ModuleMethod pregexp$Mnread$Mncluster$Mntype;
    public static final ModuleMethod pregexp$Mnread$Mnescaped$Mnchar;
    public static final ModuleMethod pregexp$Mnread$Mnescaped$Mnnumber;
    public static final ModuleMethod pregexp$Mnread$Mnnums;
    public static final ModuleMethod pregexp$Mnread$Mnpattern;
    public static final ModuleMethod pregexp$Mnread$Mnpiece;
    public static final ModuleMethod pregexp$Mnread$Mnposix$Mnchar$Mnclass;
    public static final ModuleMethod pregexp$Mnread$Mnsubpattern;
    public static final ModuleMethod pregexp$Mnreplace;
    public static final ModuleMethod pregexp$Mnreplace$Mnaux;
    public static final ModuleMethod pregexp$Mnreplace$St;
    public static final ModuleMethod pregexp$Mnreverse$Ex;
    public static final ModuleMethod pregexp$Mnsplit;
    public static final ModuleMethod pregexp$Mnstring$Mnmatch;
    public static final ModuleMethod pregexp$Mnwrap$Mnquantifier$Mnif$Mnany;

    static {
        Char make = Char.make(92);
        Lit19 = make;
        Char make2 = Char.make(46);
        Lit13 = make2;
        Char make3 = Char.make(63);
        Lit47 = make3;
        Char make4 = Char.make(42);
        Lit65 = make4;
        Char make5 = Char.make(43);
        Lit66 = make5;
        Char make6 = Char.make(124);
        Lit7 = make6;
        Char make7 = Char.make(94);
        Lit9 = make7;
        Char make8 = Char.make(36);
        Lit11 = make8;
        Char make9 = Char.make(91);
        Lit15 = make9;
        Char make10 = Char.make(93);
        Lit46 = make10;
        Char make11 = Char.make(123);
        Lit67 = make11;
        Char make12 = Char.make(125);
        Lit78 = make12;
        Char make13 = Char.make(40);
        Lit18 = make13;
        Char make14 = Char.make(41);
        Lit6 = make14;
        Lit116 = PairWithPosition.make(make, PairWithPosition.make(make2, PairWithPosition.make(make3, PairWithPosition.make(make4, PairWithPosition.make(make5, PairWithPosition.make(make6, PairWithPosition.make(make7, PairWithPosition.make(make8, PairWithPosition.make(make9, PairWithPosition.make(make10, PairWithPosition.make(make11, PairWithPosition.make(make12, PairWithPosition.make(make13, PairWithPosition.make(make14, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3153977), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3153973), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3153969), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3153965), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3153961), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3153957), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149885), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149881), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149877), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149873), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149869), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149865), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149861), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 3149856);
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(":between").readResolve();
        Lit68 = simpleSymbol;
        Boolean bool = Boolean.FALSE;
        IntNum make15 = IntNum.make(0);
        Lit73 = make15;
        Boolean bool2 = Boolean.FALSE;
        SimpleSymbol simpleSymbol2 = (SimpleSymbol) new SimpleSymbol(":any").readResolve();
        Lit14 = simpleSymbol2;
        Lit108 = PairWithPosition.make(simpleSymbol, PairWithPosition.make(bool, PairWithPosition.make(make15, PairWithPosition.make(bool2, PairWithPosition.make(simpleSymbol2, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2338881), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2338878), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2338876), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2338873), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 2338863);
        SimpleSymbol simpleSymbol3 = (SimpleSymbol) new SimpleSymbol(":neg-char").readResolve();
        Lit17 = simpleSymbol3;
        SimpleSymbol simpleSymbol4 = (SimpleSymbol) new SimpleSymbol(":word").readResolve();
        Lit41 = simpleSymbol4;
        Lit43 = PairWithPosition.make(simpleSymbol3, PairWithPosition.make(simpleSymbol4, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 696359), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 696348);
        SimpleSymbol simpleSymbol5 = Lit17;
        SimpleSymbol simpleSymbol6 = (SimpleSymbol) new SimpleSymbol(":space").readResolve();
        Lit36 = simpleSymbol6;
        Lit38 = PairWithPosition.make(simpleSymbol5, PairWithPosition.make(simpleSymbol6, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 684071), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 684060);
        SimpleSymbol simpleSymbol7 = Lit17;
        SimpleSymbol simpleSymbol8 = (SimpleSymbol) new SimpleSymbol(":digit").readResolve();
        Lit30 = simpleSymbol8;
        Lit32 = PairWithPosition.make(simpleSymbol7, PairWithPosition.make(simpleSymbol8, LList.Empty, "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 667687), "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm", 667676);
        pregexp pregexp2 = $instance;
        ModuleMethod moduleMethod = new ModuleMethod(pregexp2, 16, Lit117, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:47");
        pregexp$Mnreverse$Ex = moduleMethod;
        ModuleMethod moduleMethod2 = new ModuleMethod(pregexp2, 17, Lit118, -4096);
        moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:57");
        pregexp$Mnerror = moduleMethod2;
        ModuleMethod moduleMethod3 = new ModuleMethod(pregexp2, 18, Lit119, 12291);
        moduleMethod3.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:65");
        pregexp$Mnread$Mnpattern = moduleMethod3;
        ModuleMethod moduleMethod4 = new ModuleMethod(pregexp2, 19, Lit120, 12291);
        moduleMethod4.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:79");
        pregexp$Mnread$Mnbranch = moduleMethod4;
        ModuleMethod moduleMethod5 = new ModuleMethod(pregexp2, 20, Lit21, 12291);
        moduleMethod5.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:91");
        pregexp$Mnread$Mnpiece = moduleMethod5;
        ModuleMethod moduleMethod6 = new ModuleMethod(pregexp2, 21, Lit121, 12291);
        moduleMethod6.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:138");
        pregexp$Mnread$Mnescaped$Mnnumber = moduleMethod6;
        ModuleMethod moduleMethod7 = new ModuleMethod(pregexp2, 22, Lit122, 12291);
        moduleMethod7.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:155");
        pregexp$Mnread$Mnescaped$Mnchar = moduleMethod7;
        ModuleMethod moduleMethod8 = new ModuleMethod(pregexp2, 23, Lit45, 12291);
        moduleMethod8.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:174");
        pregexp$Mnread$Mnposix$Mnchar$Mnclass = moduleMethod8;
        ModuleMethod moduleMethod9 = new ModuleMethod(pregexp2, 24, Lit57, 12291);
        moduleMethod9.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:200");
        pregexp$Mnread$Mncluster$Mntype = moduleMethod9;
        ModuleMethod moduleMethod10 = new ModuleMethod(pregexp2, 25, Lit64, 12291);
        moduleMethod10.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:233");
        pregexp$Mnread$Mnsubpattern = moduleMethod10;
        ModuleMethod moduleMethod11 = new ModuleMethod(pregexp2, 26, Lit74, 12291);
        moduleMethod11.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:254");
        pregexp$Mnwrap$Mnquantifier$Mnif$Mnany = moduleMethod11;
        ModuleMethod moduleMethod12 = new ModuleMethod(pregexp2, 27, Lit76, 12291);
        moduleMethod12.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:300");
        pregexp$Mnread$Mnnums = moduleMethod12;
        ModuleMethod moduleMethod13 = new ModuleMethod(pregexp2, 28, Lit123, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod13.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:323");
        pregexp$Mninvert$Mnchar$Mnlist = moduleMethod13;
        ModuleMethod moduleMethod14 = new ModuleMethod(pregexp2, 29, Lit80, 12291);
        moduleMethod14.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:330");
        pregexp$Mnread$Mnchar$Mnlist = moduleMethod14;
        ModuleMethod moduleMethod15 = new ModuleMethod(pregexp2, 30, Lit124, 24582);
        moduleMethod15.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:368");
        pregexp$Mnstring$Mnmatch = moduleMethod15;
        ModuleMethod moduleMethod16 = new ModuleMethod(pregexp2, 31, Lit125, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod16.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:379");
        pregexp$Mnchar$Mnword$Qu = moduleMethod16;
        ModuleMethod moduleMethod17 = new ModuleMethod(pregexp2, 32, Lit126, 12291);
        moduleMethod17.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:387");
        pregexp$Mnat$Mnword$Mnboundary$Qu = moduleMethod17;
        ModuleMethod moduleMethod18 = new ModuleMethod(pregexp2, 33, Lit99, 8194);
        moduleMethod18.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:399");
        pregexp$Mncheck$Mnif$Mnin$Mnchar$Mnclass$Qu = moduleMethod18;
        ModuleMethod moduleMethod19 = new ModuleMethod(pregexp2, 34, Lit127, 8194);
        moduleMethod19.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:429");
        pregexp$Mnlist$Mnref = moduleMethod19;
        ModuleMethod moduleMethod20 = new ModuleMethod(pregexp2, 35, Lit128, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod20.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:448");
        pregexp$Mnmake$Mnbackref$Mnlist = moduleMethod20;
        ModuleMethod moduleMethod21 = new ModuleMethod(pregexp2, 36, (Object) null, 0);
        moduleMethod21.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:463");
        lambda$Fn1 = moduleMethod21;
        ModuleMethod moduleMethod22 = new ModuleMethod(pregexp2, 37, (Object) null, 0);
        moduleMethod22.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:551");
        lambda$Fn6 = moduleMethod22;
        ModuleMethod moduleMethod23 = new ModuleMethod(pregexp2, 38, (Object) null, 0);
        moduleMethod23.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:556");
        lambda$Fn7 = moduleMethod23;
        ModuleMethod moduleMethod24 = new ModuleMethod(pregexp2, 39, (Object) null, 0);
        moduleMethod24.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:564");
        lambda$Fn8 = moduleMethod24;
        ModuleMethod moduleMethod25 = new ModuleMethod(pregexp2, 40, (Object) null, 0);
        moduleMethod25.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:573");
        lambda$Fn9 = moduleMethod25;
        ModuleMethod moduleMethod26 = new ModuleMethod(pregexp2, 41, (Object) null, 0);
        moduleMethod26.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:578");
        lambda$Fn10 = moduleMethod26;
        ModuleMethod moduleMethod27 = new ModuleMethod(pregexp2, 42, Lit101, 24582);
        moduleMethod27.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:459");
        pregexp$Mnmatch$Mnpositions$Mnaux = moduleMethod27;
        ModuleMethod moduleMethod28 = new ModuleMethod(pregexp2, 43, Lit129, 16388);
        moduleMethod28.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:639");
        pregexp$Mnreplace$Mnaux = moduleMethod28;
        ModuleMethod moduleMethod29 = new ModuleMethod(pregexp2, 44, Lit130, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod29.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:665");
        pregexp = moduleMethod29;
        ModuleMethod moduleMethod30 = new ModuleMethod(pregexp2, 45, Lit114, -4094);
        moduleMethod30.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:670");
        pregexp$Mnmatch$Mnpositions = moduleMethod30;
        ModuleMethod moduleMethod31 = new ModuleMethod(pregexp2, 46, Lit131, -4094);
        moduleMethod31.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:690");
        pregexp$Mnmatch = moduleMethod31;
        ModuleMethod moduleMethod32 = new ModuleMethod(pregexp2, 47, Lit132, 8194);
        moduleMethod32.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:700");
        pregexp$Mnsplit = moduleMethod32;
        ModuleMethod moduleMethod33 = new ModuleMethod(pregexp2, 48, Lit133, 12291);
        moduleMethod33.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:723");
        pregexp$Mnreplace = moduleMethod33;
        ModuleMethod moduleMethod34 = new ModuleMethod(pregexp2, 49, Lit134, 12291);
        moduleMethod34.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:736");
        pregexp$Mnreplace$St = moduleMethod34;
        ModuleMethod moduleMethod35 = new ModuleMethod(pregexp2, 50, Lit135, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        moduleMethod35.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:764");
        pregexp$Mnquote = moduleMethod35;
        $instance.run();
    }

    public pregexp() {
        ModuleInfo.register(this);
    }

    public final void run(CallContext $ctx) {
        Consumer consumer = $ctx.consumer;
        $Stpregexp$Mnversion$St = Lit0;
        $Stpregexp$Mncomment$Mnchar$St = Lit1;
        $Stpregexp$Mnnul$Mnchar$Mnint$St = Integer.valueOf(characters.char$To$Integer(Lit2) - 97);
        $Stpregexp$Mnreturn$Mnchar$St = characters.integer$To$Char(((Number) $Stpregexp$Mnnul$Mnchar$Mnint$St).intValue() + 13);
        $Stpregexp$Mntab$Mnchar$St = characters.integer$To$Char(((Number) $Stpregexp$Mnnul$Mnchar$Mnint$St).intValue() + 9);
        $Stpregexp$Mnspace$Mnsensitive$Qu$St = Boolean.TRUE;
    }

    public static Object pregexpReverse$Ex(Object s) {
        Object obj = LList.Empty;
        while (!lists.isNull(s)) {
            Object d = lists.cdr.apply1(s);
            try {
                lists.setCdr$Ex((Pair) s, obj);
                obj = s;
                s = d;
            } catch (ClassCastException e) {
                throw new WrongType(e, "set-cdr!", 1, s);
            }
        }
        return obj;
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 16:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 28:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 31:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 35:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 44:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 50:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public static Object pregexpError$V(Object[] argsArray) {
        LList whatever = LList.makeList(argsArray, 0);
        ports.display("Error:");
        Object obj = whatever;
        while (obj != LList.Empty) {
            try {
                Pair arg0 = (Pair) obj;
                Object x = arg0.getCar();
                ports.display(Lit3);
                ports.write(x);
                obj = arg0.getCdr();
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, obj);
            }
        }
        ports.newline();
        return misc.error$V("pregexp-error", new Object[0]);
    }

    public int matchN(ModuleMethod moduleMethod, Object[] objArr, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 17:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 30:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 42:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 45:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            case 46:
                callContext.values = objArr;
                callContext.proc = moduleMethod;
                callContext.pc = 5;
                return 0;
            default:
                return super.matchN(moduleMethod, objArr, callContext);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0078  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0080 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpReadPattern(java.lang.Object r9, java.lang.Object r10, java.lang.Object r11) {
        /*
            r8 = 2
            r7 = 1
            gnu.kawa.functions.NumberCompare r4 = kawa.standard.Scheme.numGEq
            java.lang.Object r4 = r4.apply2(r10, r11)
            java.lang.Boolean r5 = java.lang.Boolean.FALSE
            if (r4 == r5) goto L_0x001d
            gnu.mapping.SimpleSymbol r4 = Lit4
            gnu.mapping.SimpleSymbol r5 = Lit5
            gnu.lists.Pair r5 = gnu.lists.LList.list1(r5)
            gnu.lists.Pair r4 = gnu.lists.LList.list2(r4, r5)
            gnu.lists.Pair r4 = gnu.lists.LList.list2(r4, r10)
        L_0x001c:
            return r4
        L_0x001d:
            gnu.lists.LList r1 = gnu.lists.LList.Empty
        L_0x001f:
            gnu.kawa.functions.NumberCompare r4 = kawa.standard.Scheme.numGEq
            java.lang.Object r5 = r4.apply2(r10, r11)
            r0 = r5
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x0095 }
            r4 = r0
            boolean r3 = r4.booleanValue()     // Catch:{ ClassCastException -> 0x0095 }
            if (r3 == 0) goto L_0x0040
            if (r3 == 0) goto L_0x005c
        L_0x0031:
            gnu.mapping.SimpleSymbol r4 = Lit4
            java.lang.Object r5 = pregexpReverse$Ex(r1)
            gnu.lists.Pair r4 = kawa.lib.lists.cons(r4, r5)
            gnu.lists.Pair r4 = gnu.lists.LList.list2(r4, r10)
            goto L_0x001c
        L_0x0040:
            r0 = r9
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x009f }
            r4 = r0
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00a8 }
            r5 = r0
            int r5 = r5.intValue()     // Catch:{ ClassCastException -> 0x00a8 }
            char r4 = kawa.lib.strings.stringRef(r4, r5)
            gnu.text.Char r4 = gnu.text.Char.make(r4)
            gnu.text.Char r5 = Lit6
            boolean r4 = kawa.lib.characters.isChar$Eq(r4, r5)
            if (r4 != 0) goto L_0x0031
        L_0x005c:
            r0 = r9
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x00b1 }
            r4 = r0
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00ba }
            r5 = r0
            int r5 = r5.intValue()     // Catch:{ ClassCastException -> 0x00ba }
            char r4 = kawa.lib.strings.stringRef(r4, r5)
            gnu.text.Char r4 = gnu.text.Char.make(r4)
            gnu.text.Char r5 = Lit7
            boolean r4 = kawa.lib.characters.isChar$Eq(r4, r5)
            if (r4 == 0) goto L_0x0080
            gnu.kawa.functions.AddOp r4 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r5 = Lit8
            java.lang.Object r10 = r4.apply2(r10, r5)
        L_0x0080:
            java.lang.Object r2 = pregexpReadBranch(r9, r10, r11)
            gnu.expr.GenericProc r4 = kawa.lib.lists.car
            java.lang.Object r4 = r4.apply1(r2)
            gnu.lists.Pair r1 = kawa.lib.lists.cons(r4, r1)
            gnu.expr.GenericProc r4 = kawa.lib.lists.cadr
            java.lang.Object r10 = r4.apply1(r2)
            goto L_0x001f
        L_0x0095:
            r4 = move-exception
            gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
            java.lang.String r7 = "x"
            r8 = -2
            r6.<init>((java.lang.ClassCastException) r4, (java.lang.String) r7, (int) r8, (java.lang.Object) r5)
            throw r6
        L_0x009f:
            r4 = move-exception
            gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
            java.lang.String r6 = "string-ref"
            r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r7, (java.lang.Object) r9)
            throw r5
        L_0x00a8:
            r4 = move-exception
            gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
            java.lang.String r6 = "string-ref"
            r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r8, (java.lang.Object) r10)
            throw r5
        L_0x00b1:
            r4 = move-exception
            gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
            java.lang.String r6 = "string-ref"
            r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r7, (java.lang.Object) r9)
            throw r5
        L_0x00ba:
            r4 = move-exception
            gnu.mapping.WrongType r5 = new gnu.mapping.WrongType
            java.lang.String r6 = "string-ref"
            r5.<init>((java.lang.ClassCastException) r4, (java.lang.String) r6, (int) r8, (java.lang.Object) r10)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpReadPattern(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public int match3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 18:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 19:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 20:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 21:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 22:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 23:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 24:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 25:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 26:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 27:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 29:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 32:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 48:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.value3 = obj3;
                callContext.proc = moduleMethod;
                callContext.pc = 3;
                return 0;
            case 49:
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

    public static Object pregexpReadBranch(Object s, Object i, Object n) {
        Object obj = LList.Empty;
        while (Scheme.numGEq.apply2(i, n) == Boolean.FALSE) {
            try {
                try {
                    char c = strings.stringRef((CharSequence) s, ((Number) i).intValue());
                    boolean x = characters.isChar$Eq(Char.make(c), Lit7);
                    if (x) {
                        if (!x) {
                            Object vv = pregexpReadPiece(s, i, n);
                            obj = lists.cons(lists.car.apply1(vv), obj);
                            i = lists.cadr.apply1(vv);
                        }
                    } else if (!characters.isChar$Eq(Char.make(c), Lit6)) {
                        Object vv2 = pregexpReadPiece(s, i, n);
                        obj = lists.cons(lists.car.apply1(vv2), obj);
                        i = lists.cadr.apply1(vv2);
                    }
                    return LList.list2(lists.cons(Lit5, pregexpReverse$Ex(obj)), i);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-ref", 2, i);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-ref", 1, s);
            }
        }
        return LList.list2(lists.cons(Lit5, pregexpReverse$Ex(obj)), i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:63:0x01c0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpReadPiece(java.lang.Object r13, java.lang.Object r14, java.lang.Object r15) {
        /*
            r12 = 2
            r11 = 1
            r0 = r13
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0222 }
            r8 = r0
            r0 = r14
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x022b }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x022b }
            char r1 = kawa.lib.strings.stringRef(r8, r9)
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit9
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0031
            gnu.mapping.SimpleSymbol r8 = Lit10
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r9 = r9.apply2(r14, r10)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
        L_0x0030:
            return r8
        L_0x0031:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit11
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0050
            gnu.mapping.SimpleSymbol r8 = Lit12
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r9 = r9.apply2(r14, r10)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
            goto L_0x0030
        L_0x0050:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit13
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0073
            gnu.mapping.SimpleSymbol r8 = Lit14
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r9 = r9.apply2(r14, r10)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
            java.lang.Object r8 = pregexpWrapQuantifierIfAny(r8, r13, r15)
            goto L_0x0030
        L_0x0073:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit15
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x00f0
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r2 = r8.apply2(r14, r9)
            gnu.kawa.functions.NumberCompare r8 = kawa.standard.Scheme.numLss
            java.lang.Object r9 = r8.apply2(r2, r15)
            r0 = r9
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x0234 }
            r8 = r0
            boolean r7 = r8.booleanValue()     // Catch:{ ClassCastException -> 0x0234 }
            if (r7 == 0) goto L_0x00e3
            r0 = r13
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x023e }
            r8 = r0
            r0 = r2
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0247 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x0247 }
            char r8 = kawa.lib.strings.stringRef(r8, r9)
            gnu.text.Char r5 = gnu.text.Char.make(r8)
        L_0x00af:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = Lit9
            java.lang.Object r8 = r8.apply2(r5, r9)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x00eb
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit16
            java.lang.Object r8 = r8.apply2(r14, r9)
            java.lang.Object r6 = pregexpReadCharList(r13, r8, r15)
            gnu.mapping.SimpleSymbol r8 = Lit17
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r9 = r9.apply1(r6)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
            gnu.expr.GenericProc r9 = kawa.lib.lists.cadr
            java.lang.Object r9 = r9.apply1(r6)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
        L_0x00dd:
            java.lang.Object r8 = pregexpWrapQuantifierIfAny(r8, r13, r15)
            goto L_0x0030
        L_0x00e3:
            if (r7 == 0) goto L_0x00e8
            java.lang.Boolean r5 = java.lang.Boolean.TRUE
            goto L_0x00af
        L_0x00e8:
            java.lang.Boolean r5 = java.lang.Boolean.FALSE
            goto L_0x00af
        L_0x00eb:
            java.lang.Object r8 = pregexpReadCharList(r13, r2, r15)
            goto L_0x00dd
        L_0x00f0:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit18
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0112
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r8 = r8.apply2(r14, r9)
            java.lang.Object r8 = pregexpReadSubpattern(r13, r8, r15)
            java.lang.Object r8 = pregexpWrapQuantifierIfAny(r8, r13, r15)
            goto L_0x0030
        L_0x0112:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit19
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x016f
            java.lang.Object r4 = pregexpReadEscapedNumber(r13, r14, r15)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r4 == r8) goto L_0x0146
            gnu.mapping.SimpleSymbol r8 = Lit20
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r9 = r9.apply1(r4)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
            gnu.expr.GenericProc r9 = kawa.lib.lists.cadr
            java.lang.Object r9 = r9.apply1(r4)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
        L_0x0140:
            java.lang.Object r8 = pregexpWrapQuantifierIfAny(r8, r13, r15)
            goto L_0x0030
        L_0x0146:
            java.lang.Object r4 = pregexpReadEscapedChar(r13, r14, r15)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r4 == r8) goto L_0x015f
            gnu.expr.GenericProc r8 = kawa.lib.lists.car
            java.lang.Object r8 = r8.apply1(r4)
            gnu.expr.GenericProc r9 = kawa.lib.lists.cadr
            java.lang.Object r9 = r9.apply1(r4)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
            goto L_0x0140
        L_0x015f:
            java.lang.Object[] r8 = new java.lang.Object[r12]
            r9 = 0
            gnu.mapping.SimpleSymbol r10 = Lit21
            r8[r9] = r10
            gnu.mapping.SimpleSymbol r9 = Lit22
            r8[r11] = r9
            java.lang.Object r8 = pregexpError$V(r8)
            goto L_0x0140
        L_0x016f:
            java.lang.Object r7 = $Stpregexp$Mnspace$Mnsensitive$Qu$St
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x018f
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x01a9
        L_0x0179:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r9 = r9.apply2(r14, r10)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
            java.lang.Object r8 = pregexpWrapQuantifierIfAny(r8, r13, r15)
            goto L_0x0030
        L_0x018f:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            boolean r8 = kawa.lib.rnrs.unicode.isCharWhitespace(r8)
            int r8 = r8 + 1
            r7 = r8 & 1
            if (r7 == 0) goto L_0x01bd
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.text.Char r9 = Lit1
            boolean r8 = kawa.lib.characters.isChar$Eq(r8, r9)
            if (r8 == 0) goto L_0x0179
        L_0x01a9:
            java.lang.Boolean r3 = java.lang.Boolean.FALSE
        L_0x01ab:
            gnu.kawa.functions.NumberCompare r8 = kawa.standard.Scheme.numGEq
            java.lang.Object r8 = r8.apply2(r14, r15)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x01c0
            gnu.mapping.SimpleSymbol r8 = Lit23
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r14)
            goto L_0x0030
        L_0x01bd:
            if (r7 == 0) goto L_0x01a9
            goto L_0x0179
        L_0x01c0:
            r0 = r13
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0250 }
            r8 = r0
            r0 = r14
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0259 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x0259 }
            char r1 = kawa.lib.strings.stringRef(r8, r9)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r3 == r8) goto L_0x01ee
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.text.Char r9 = Lit24
            boolean r8 = kawa.lib.characters.isChar$Eq(r8, r9)
            if (r8 == 0) goto L_0x01eb
            java.lang.Boolean r3 = java.lang.Boolean.FALSE
        L_0x01ea:
            goto L_0x01ab
        L_0x01eb:
            java.lang.Boolean r3 = java.lang.Boolean.TRUE
            goto L_0x01ea
        L_0x01ee:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            boolean r8 = kawa.lib.rnrs.unicode.isCharWhitespace(r8)
            if (r8 == 0) goto L_0x0203
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            java.lang.Boolean r3 = java.lang.Boolean.FALSE
            goto L_0x01ab
        L_0x0203:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.text.Char r9 = Lit1
            boolean r8 = kawa.lib.characters.isChar$Eq(r8, r9)
            if (r8 == 0) goto L_0x021a
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            java.lang.Boolean r3 = java.lang.Boolean.TRUE
            goto L_0x01ab
        L_0x021a:
            gnu.mapping.SimpleSymbol r8 = Lit23
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r14)
            goto L_0x0030
        L_0x0222:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r13)
            throw r9
        L_0x022b:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r14)
            throw r9
        L_0x0234:
            r8 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "x"
            r12 = -2
            r10.<init>((java.lang.ClassCastException) r8, (java.lang.String) r11, (int) r12, (java.lang.Object) r9)
            throw r10
        L_0x023e:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r13)
            throw r9
        L_0x0247:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r2)
            throw r9
        L_0x0250:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r13)
            throw r9
        L_0x0259:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r14)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpReadPiece(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public static Object pregexpReadEscapedNumber(Object s, Object i, Object n) {
        Object apply2 = Scheme.numLss.apply2(AddOp.$Pl.apply2(i, Lit8), n);
        try {
            boolean x = ((Boolean) apply2).booleanValue();
            if (!x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            try {
                CharSequence charSequence = (CharSequence) s;
                Object apply22 = AddOp.$Pl.apply2(i, Lit8);
                try {
                    char c = strings.stringRef(charSequence, ((Number) apply22).intValue());
                    boolean x2 = unicode.isCharNumeric(Char.make(c));
                    if (!x2) {
                        return x2 ? Boolean.TRUE : Boolean.FALSE;
                    }
                    Object i2 = AddOp.$Pl.apply2(i, Lit16);
                    Pair r = LList.list1(Char.make(c));
                    while (Scheme.numGEq.apply2(i2, n) == Boolean.FALSE) {
                        try {
                            try {
                                char c2 = strings.stringRef((CharSequence) s, ((Number) i2).intValue());
                                if (unicode.isCharNumeric(Char.make(c2))) {
                                    i2 = AddOp.$Pl.apply2(i2, Lit8);
                                    r = lists.cons(Char.make(c2), r);
                                } else {
                                    Object pregexpReverse$Ex = pregexpReverse$Ex(r);
                                    try {
                                        return LList.list2(numbers.string$To$Number(strings.list$To$String((LList) pregexpReverse$Ex)), i2);
                                    } catch (ClassCastException e) {
                                        throw new WrongType(e, "list->string", 1, pregexpReverse$Ex);
                                    }
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 2, i2);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "string-ref", 1, s);
                        }
                    }
                    Object pregexpReverse$Ex2 = pregexpReverse$Ex(r);
                    try {
                        return LList.list2(numbers.string$To$Number(strings.list$To$String((LList) pregexpReverse$Ex2)), i2);
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "list->string", 1, pregexpReverse$Ex2);
                    }
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "string-ref", 2, apply22);
                }
            } catch (ClassCastException e6) {
                throw new WrongType(e6, "string-ref", 1, s);
            }
        } catch (ClassCastException e7) {
            throw new WrongType(e7, "x", -2, apply2);
        }
    }

    public static Object pregexpReadEscapedChar(Object s, Object i, Object n) {
        Object apply2 = Scheme.numLss.apply2(AddOp.$Pl.apply2(i, Lit8), n);
        try {
            boolean x = ((Boolean) apply2).booleanValue();
            if (!x) {
                return x ? Boolean.TRUE : Boolean.FALSE;
            }
            try {
                CharSequence charSequence = (CharSequence) s;
                Object apply22 = AddOp.$Pl.apply2(i, Lit8);
                try {
                    char c = strings.stringRef(charSequence, ((Number) apply22).intValue());
                    if (Scheme.isEqv.apply2(Char.make(c), Lit25) != Boolean.FALSE) {
                        return LList.list2(Lit26, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit27) != Boolean.FALSE) {
                        return LList.list2(Lit28, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit29) != Boolean.FALSE) {
                        return LList.list2(Lit30, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit31) != Boolean.FALSE) {
                        return LList.list2(Lit32, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit33) != Boolean.FALSE) {
                        return LList.list2(Lit24, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit34) != Boolean.FALSE) {
                        return LList.list2($Stpregexp$Mnreturn$Mnchar$St, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit35) != Boolean.FALSE) {
                        return LList.list2(Lit36, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit37) != Boolean.FALSE) {
                        return LList.list2(Lit38, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit39) != Boolean.FALSE) {
                        return LList.list2($Stpregexp$Mntab$Mnchar$St, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit40) != Boolean.FALSE) {
                        return LList.list2(Lit41, AddOp.$Pl.apply2(i, Lit16));
                    }
                    if (Scheme.isEqv.apply2(Char.make(c), Lit42) != Boolean.FALSE) {
                        return LList.list2(Lit43, AddOp.$Pl.apply2(i, Lit16));
                    }
                    return LList.list2(Char.make(c), AddOp.$Pl.apply2(i, Lit16));
                } catch (ClassCastException e) {
                    throw new WrongType(e, "string-ref", 2, apply22);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "string-ref", 1, s);
            }
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "x", -2, apply2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0086, code lost:
        if (r5 != false) goto L_0x0088;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b3, code lost:
        if (kawa.lib.characters.isChar$Eq(gnu.text.Char.make(kawa.lib.strings.stringRef(r12, ((java.lang.Number) r7).intValue())), Lit46) != false) goto L_0x00b5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b5, code lost:
        r6 = pregexpReverse$Ex(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00bb, code lost:
        r3 = kawa.lib.misc.string$To$Symbol(kawa.lib.strings.list$To$String((gnu.lists.LList) r6));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00c5, code lost:
        if (r2 == java.lang.Boolean.FALSE) goto L_0x00cd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c7, code lost:
        r3 = gnu.lists.LList.list2(Lit17, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0115, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x011d, code lost:
        throw new gnu.mapping.WrongType(r7, "list->string", 1, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
        return gnu.lists.LList.list2(r3, gnu.kawa.functions.AddOp.$Pl.apply2(r13, Lit16));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpReadPosixCharClass(java.lang.Object r12, java.lang.Object r13, java.lang.Object r14) {
        /*
            r11 = 2
            r9 = 0
            r10 = 1
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
            gnu.text.Char r6 = Lit44
            gnu.lists.Pair r4 = gnu.lists.LList.list1(r6)
        L_0x000b:
            gnu.kawa.functions.NumberCompare r6 = kawa.standard.Scheme.numGEq
            java.lang.Object r6 = r6.apply2(r13, r14)
            java.lang.Boolean r7 = java.lang.Boolean.FALSE
            if (r6 == r7) goto L_0x0020
            java.lang.Object[] r6 = new java.lang.Object[r10]
            gnu.mapping.SimpleSymbol r7 = Lit45
            r6[r9] = r7
            java.lang.Object r6 = pregexpError$V(r6)
        L_0x001f:
            return r6
        L_0x0020:
            r0 = r12
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x00e7 }
            r6 = r0
            r0 = r13
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00f0 }
            r7 = r0
            int r7 = r7.intValue()     // Catch:{ ClassCastException -> 0x00f0 }
            char r1 = kawa.lib.strings.stringRef(r6, r7)
            gnu.text.Char r6 = gnu.text.Char.make(r1)
            gnu.text.Char r7 = Lit9
            boolean r6 = kawa.lib.characters.isChar$Eq(r6, r7)
            if (r6 == 0) goto L_0x0047
            java.lang.Boolean r2 = java.lang.Boolean.TRUE
            gnu.kawa.functions.AddOp r6 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r7 = Lit8
            java.lang.Object r13 = r6.apply2(r13, r7)
            goto L_0x000b
        L_0x0047:
            gnu.text.Char r6 = gnu.text.Char.make(r1)
            boolean r6 = kawa.lib.rnrs.unicode.isCharAlphabetic(r6)
            if (r6 == 0) goto L_0x0062
            gnu.kawa.functions.AddOp r6 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r7 = Lit8
            java.lang.Object r13 = r6.apply2(r13, r7)
            gnu.text.Char r6 = gnu.text.Char.make(r1)
            gnu.lists.Pair r4 = kawa.lib.lists.cons(r6, r4)
            goto L_0x000b
        L_0x0062:
            gnu.text.Char r6 = gnu.text.Char.make(r1)
            gnu.text.Char r7 = Lit44
            boolean r6 = kawa.lib.characters.isChar$Eq(r6, r7)
            if (r6 == 0) goto L_0x00db
            gnu.kawa.functions.NumberCompare r6 = kawa.standard.Scheme.numGEq
            gnu.kawa.functions.AddOp r7 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r7 = r7.apply2(r13, r8)
            java.lang.Object r7 = r6.apply2(r7, r14)
            r0 = r7
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x00f9 }
            r6 = r0
            boolean r5 = r6.booleanValue()     // Catch:{ ClassCastException -> 0x00f9 }
            if (r5 == 0) goto L_0x0093
            if (r5 == 0) goto L_0x00b5
        L_0x0088:
            java.lang.Object[] r6 = new java.lang.Object[r10]
            gnu.mapping.SimpleSymbol r7 = Lit45
            r6[r9] = r7
            java.lang.Object r6 = pregexpError$V(r6)
            goto L_0x001f
        L_0x0093:
            java.lang.CharSequence r12 = (java.lang.CharSequence) r12     // Catch:{ ClassCastException -> 0x0103 }
            gnu.kawa.functions.AddOp r6 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r7 = Lit8
            java.lang.Object r7 = r6.apply2(r13, r7)
            r0 = r7
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x010c }
            r6 = r0
            int r6 = r6.intValue()     // Catch:{ ClassCastException -> 0x010c }
            char r6 = kawa.lib.strings.stringRef(r12, r6)
            gnu.text.Char r6 = gnu.text.Char.make(r6)
            gnu.text.Char r7 = Lit46
            boolean r6 = kawa.lib.characters.isChar$Eq(r6, r7)
            if (r6 == 0) goto L_0x0088
        L_0x00b5:
            java.lang.Object r6 = pregexpReverse$Ex(r4)
            gnu.lists.LList r6 = (gnu.lists.LList) r6     // Catch:{ ClassCastException -> 0x0115 }
            java.lang.CharSequence r6 = kawa.lib.strings.list$To$String(r6)
            gnu.mapping.SimpleSymbol r3 = kawa.lib.misc.string$To$Symbol(r6)
            java.lang.Boolean r6 = java.lang.Boolean.FALSE
            if (r2 == r6) goto L_0x00cd
            gnu.mapping.SimpleSymbol r6 = Lit17
            gnu.lists.Pair r3 = gnu.lists.LList.list2(r6, r3)
        L_0x00cd:
            gnu.kawa.functions.AddOp r6 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r7 = Lit16
            java.lang.Object r6 = r6.apply2(r13, r7)
            gnu.lists.Pair r6 = gnu.lists.LList.list2(r3, r6)
            goto L_0x001f
        L_0x00db:
            java.lang.Object[] r6 = new java.lang.Object[r10]
            gnu.mapping.SimpleSymbol r7 = Lit45
            r6[r9] = r7
            java.lang.Object r6 = pregexpError$V(r6)
            goto L_0x001f
        L_0x00e7:
            r6 = move-exception
            gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
            java.lang.String r8 = "string-ref"
            r7.<init>((java.lang.ClassCastException) r6, (java.lang.String) r8, (int) r10, (java.lang.Object) r12)
            throw r7
        L_0x00f0:
            r6 = move-exception
            gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
            java.lang.String r8 = "string-ref"
            r7.<init>((java.lang.ClassCastException) r6, (java.lang.String) r8, (int) r11, (java.lang.Object) r13)
            throw r7
        L_0x00f9:
            r6 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "x"
            r10 = -2
            r8.<init>((java.lang.ClassCastException) r6, (java.lang.String) r9, (int) r10, (java.lang.Object) r7)
            throw r8
        L_0x0103:
            r6 = move-exception
            gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
            java.lang.String r8 = "string-ref"
            r7.<init>((java.lang.ClassCastException) r6, (java.lang.String) r8, (int) r10, (java.lang.Object) r12)
            throw r7
        L_0x010c:
            r6 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "string-ref"
            r8.<init>((java.lang.ClassCastException) r6, (java.lang.String) r9, (int) r11, (java.lang.Object) r7)
            throw r8
        L_0x0115:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "list->string"
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r6)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpReadPosixCharClass(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public static Object pregexpReadClusterType(Object s, Object i, Object n) {
        char c;
        try {
            try {
                if (Scheme.isEqv.apply2(Char.make(strings.stringRef((CharSequence) s, ((Number) i).intValue())), Lit47) == Boolean.FALSE) {
                    return LList.list2(Lit63, i);
                }
                Object i2 = AddOp.$Pl.apply2(i, Lit8);
                try {
                    try {
                        char tmp = strings.stringRef((CharSequence) s, ((Number) i2).intValue());
                        if (Scheme.isEqv.apply2(Char.make(tmp), Lit44) != Boolean.FALSE) {
                            return LList.list2(LList.Empty, AddOp.$Pl.apply2(i2, Lit8));
                        }
                        if (Scheme.isEqv.apply2(Char.make(tmp), Lit48) != Boolean.FALSE) {
                            return LList.list2(Lit49, AddOp.$Pl.apply2(i2, Lit8));
                        }
                        if (Scheme.isEqv.apply2(Char.make(tmp), Lit50) != Boolean.FALSE) {
                            return LList.list2(Lit51, AddOp.$Pl.apply2(i2, Lit8));
                        }
                        if (Scheme.isEqv.apply2(Char.make(tmp), Lit52) != Boolean.FALSE) {
                            return LList.list2(Lit53, AddOp.$Pl.apply2(i2, Lit8));
                        }
                        if (Scheme.isEqv.apply2(Char.make(tmp), Lit54) != Boolean.FALSE) {
                            try {
                                CharSequence charSequence = (CharSequence) s;
                                Object apply2 = AddOp.$Pl.apply2(i2, Lit8);
                                try {
                                    char tmp2 = strings.stringRef(charSequence, ((Number) apply2).intValue());
                                    return LList.list2(Scheme.isEqv.apply2(Char.make(tmp2), Lit48) != Boolean.FALSE ? Lit55 : Scheme.isEqv.apply2(Char.make(tmp2), Lit50) != Boolean.FALSE ? Lit56 : pregexpError$V(new Object[]{Lit57}), AddOp.$Pl.apply2(i2, Lit16));
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, apply2);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, s);
                            }
                        } else {
                            LList lList = LList.Empty;
                            Boolean bool = Boolean.FALSE;
                            while (true) {
                                try {
                                    try {
                                        c = strings.stringRef((CharSequence) s, ((Number) i2).intValue());
                                        if (Scheme.isEqv.apply2(Char.make(c), Lit58) == Boolean.FALSE) {
                                            if (Scheme.isEqv.apply2(Char.make(c), Lit59) == Boolean.FALSE) {
                                                if (Scheme.isEqv.apply2(Char.make(c), Lit62) == Boolean.FALSE) {
                                                    break;
                                                }
                                                $Stpregexp$Mnspace$Mnsensitive$Qu$St = bool;
                                                i2 = AddOp.$Pl.apply2(i2, Lit8);
                                                bool = Boolean.FALSE;
                                            } else {
                                                i2 = AddOp.$Pl.apply2(i2, Lit8);
                                                lList = lists.cons(bool != Boolean.FALSE ? Lit60 : Lit61, lList);
                                                bool = Boolean.FALSE;
                                            }
                                        } else {
                                            i2 = AddOp.$Pl.apply2(i2, Lit8);
                                            bool = Boolean.TRUE;
                                        }
                                    } catch (ClassCastException e3) {
                                        throw new WrongType(e3, "string-ref", 2, i2);
                                    }
                                } catch (ClassCastException e4) {
                                    throw new WrongType(e4, "string-ref", 1, s);
                                }
                            }
                            if (Scheme.isEqv.apply2(Char.make(c), Lit44) != Boolean.FALSE) {
                                return LList.list2(lList, AddOp.$Pl.apply2(i2, Lit8));
                            }
                            return pregexpError$V(new Object[]{Lit57});
                        }
                    } catch (ClassCastException e5) {
                        throw new WrongType(e5, "string-ref", 2, i2);
                    }
                } catch (ClassCastException e6) {
                    throw new WrongType(e6, "string-ref", 1, s);
                }
            } catch (ClassCastException e7) {
                throw new WrongType(e7, "string-ref", 2, i);
            }
        } catch (ClassCastException e8) {
            throw new WrongType(e8, "string-ref", 1, s);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x004e, code lost:
        if (kawa.lib.characters.isChar$Eq(gnu.text.Char.make(kawa.lib.strings.stringRef((java.lang.CharSequence) r15, ((java.lang.Number) r7).intValue())), Lit6) != false) goto L_0x0050;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0054, code lost:
        if (kawa.lib.lists.isNull(r1) == false) goto L_0x0072;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0063, code lost:
        if (r9 == false) goto L_0x0065;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0072, code lost:
        r2 = kawa.lib.lists.cdr.apply1(r1);
        r8 = gnu.lists.LList.list2(kawa.lib.lists.car.apply1(r1), r8);
        r1 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return gnu.lists.LList.list2(r8, gnu.kawa.functions.AddOp.$Pl.apply2(r7, Lit8));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpReadSubpattern(java.lang.Object r15, java.lang.Object r16, java.lang.Object r17) {
        /*
            java.lang.Object r5 = $Stpregexp$Mnspace$Mnsensitive$Qu$St
            java.lang.Object r3 = pregexpReadClusterType(r15, r16, r17)
            gnu.expr.GenericProc r10 = kawa.lib.lists.car
            java.lang.Object r1 = r10.apply1(r3)
            gnu.expr.GenericProc r10 = kawa.lib.lists.cadr
            java.lang.Object r16 = r10.apply1(r3)
            java.lang.Object r6 = pregexpReadPattern(r15, r16, r17)
            $Stpregexp$Mnspace$Mnsensitive$Qu$St = r5
            gnu.expr.GenericProc r10 = kawa.lib.lists.car
            java.lang.Object r8 = r10.apply1(r6)
            gnu.expr.GenericProc r10 = kawa.lib.lists.cadr
            java.lang.Object r7 = r10.apply1(r6)
            gnu.kawa.functions.NumberCompare r10 = kawa.standard.Scheme.numLss
            r0 = r17
            java.lang.Object r11 = r10.apply2(r7, r0)
            r0 = r11
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x0085 }
            r10 = r0
            boolean r9 = r10.booleanValue()     // Catch:{ ClassCastException -> 0x0085 }
            if (r9 == 0) goto L_0x0063
            java.lang.CharSequence r15 = (java.lang.CharSequence) r15     // Catch:{ ClassCastException -> 0x008f }
            r0 = r7
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0099 }
            r10 = r0
            int r10 = r10.intValue()     // Catch:{ ClassCastException -> 0x0099 }
            char r10 = kawa.lib.strings.stringRef(r15, r10)
            gnu.text.Char r10 = gnu.text.Char.make(r10)
            gnu.text.Char r11 = Lit6
            boolean r10 = kawa.lib.characters.isChar$Eq(r10, r11)
            if (r10 == 0) goto L_0x0065
        L_0x0050:
            boolean r10 = kawa.lib.lists.isNull(r1)
            if (r10 == 0) goto L_0x0072
            gnu.kawa.functions.AddOp r10 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r11 = Lit8
            java.lang.Object r10 = r10.apply2(r7, r11)
            gnu.lists.Pair r10 = gnu.lists.LList.list2(r8, r10)
        L_0x0062:
            return r10
        L_0x0063:
            if (r9 != 0) goto L_0x0050
        L_0x0065:
            r10 = 1
            java.lang.Object[] r10 = new java.lang.Object[r10]
            r11 = 0
            gnu.mapping.SimpleSymbol r12 = Lit64
            r10[r11] = r12
            java.lang.Object r10 = pregexpError$V(r10)
            goto L_0x0062
        L_0x0072:
            gnu.expr.GenericProc r10 = kawa.lib.lists.cdr
            java.lang.Object r2 = r10.apply1(r1)
            gnu.expr.GenericProc r10 = kawa.lib.lists.car
            java.lang.Object r10 = r10.apply1(r1)
            gnu.lists.Pair r4 = gnu.lists.LList.list2(r10, r8)
            r8 = r4
            r1 = r2
            goto L_0x0050
        L_0x0085:
            r10 = move-exception
            gnu.mapping.WrongType r12 = new gnu.mapping.WrongType
            java.lang.String r13 = "x"
            r14 = -2
            r12.<init>((java.lang.ClassCastException) r10, (java.lang.String) r13, (int) r14, (java.lang.Object) r11)
            throw r12
        L_0x008f:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-ref"
            r13 = 1
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r15)
            throw r11
        L_0x0099:
            r10 = move-exception
            gnu.mapping.WrongType r11 = new gnu.mapping.WrongType
            java.lang.String r12 = "string-ref"
            r13 = 2
            r11.<init>((java.lang.ClassCastException) r10, (java.lang.String) r12, (int) r13, (java.lang.Object) r7)
            throw r11
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpReadSubpattern(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x020b  */
    /* JADX WARNING: Removed duplicated region for block: B:166:0x00a9 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0054  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00c4  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x0101  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01b1  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01e9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpWrapQuantifierIfAny(java.lang.Object r13, java.lang.Object r14, java.lang.Object r15) {
        /*
            r11 = 2
            r12 = 1
            gnu.expr.GenericProc r8 = kawa.lib.lists.car
            java.lang.Object r6 = r8.apply1(r13)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cadr
            java.lang.Object r2 = r8.apply1(r13)
        L_0x000e:
            gnu.kawa.functions.NumberCompare r8 = kawa.standard.Scheme.numGEq
            java.lang.Object r8 = r8.apply2(r2, r15)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0019
        L_0x0018:
            return r13
        L_0x0019:
            r0 = r14
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0225 }
            r8 = r0
            r0 = r2
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x022e }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x022e }
            char r1 = kawa.lib.strings.stringRef(r8, r9)
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            boolean r7 = kawa.lib.rnrs.unicode.isCharWhitespace(r8)
            if (r7 == 0) goto L_0x0042
            java.lang.Object r8 = $Stpregexp$Mnspace$Mnsensitive$Qu$St
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 != r9) goto L_0x0044
        L_0x0039:
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r2 = r8.apply2(r2, r9)
            goto L_0x000e
        L_0x0042:
            if (r7 != 0) goto L_0x0039
        L_0x0044:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit65
            java.lang.Object r7 = r8.apply2(r9, r10)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x00c4
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x0018
        L_0x0058:
            gnu.mapping.SimpleSymbol r8 = Lit68
            gnu.lists.Pair r3 = gnu.lists.LList.list1(r8)
            gnu.mapping.SimpleSymbol r8 = Lit69
            gnu.mapping.SimpleSymbol r9 = Lit70
            gnu.mapping.SimpleSymbol r10 = Lit71
            gnu.lists.LList.chain4(r3, r8, r9, r10, r6)
            gnu.mapping.SimpleSymbol r8 = Lit72
            gnu.lists.Pair r4 = gnu.lists.LList.list2(r3, r8)
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit65
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0101
            gnu.expr.GenericProc r8 = kawa.lib.lists.cddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x0237 }
            gnu.math.IntNum r9 = Lit73
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x0240 }
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            kawa.lib.lists.setCar$Ex(r8, r9)
        L_0x0097:
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r2 = r8.apply2(r2, r9)
        L_0x009f:
            gnu.kawa.functions.NumberCompare r8 = kawa.standard.Scheme.numGEq
            java.lang.Object r8 = r8.apply2(r2, r15)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x01b1
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x027f }
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r8 = r8.apply1(r4)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x0288 }
            kawa.lib.lists.setCar$Ex(r8, r2)
        L_0x00c1:
            r13 = r4
            goto L_0x0018
        L_0x00c4:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit66
            java.lang.Object r7 = r8.apply2(r9, r10)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x00d9
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x0018
            goto L_0x0058
        L_0x00d9:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit47
            java.lang.Object r7 = r8.apply2(r9, r10)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x00ef
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x0018
            goto L_0x0058
        L_0x00ef:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit67
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0018
            goto L_0x0058
        L_0x0101:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit66
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x012d
            gnu.expr.GenericProc r8 = kawa.lib.lists.cddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x0249 }
            gnu.math.IntNum r9 = Lit8
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x0252 }
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            kawa.lib.lists.setCar$Ex(r8, r9)
            goto L_0x0097
        L_0x012d:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit47
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0159
            gnu.expr.GenericProc r8 = kawa.lib.lists.cddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x025b }
            gnu.math.IntNum r9 = Lit73
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x0264 }
            gnu.math.IntNum r9 = Lit8
            kawa.lib.lists.setCar$Ex(r8, r9)
            goto L_0x0097
        L_0x0159:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit67
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x0097
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r8 = r8.apply2(r2, r9)
            java.lang.Object r5 = pregexpReadNums(r14, r8, r15)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r5 != r8) goto L_0x0187
            java.lang.Object[] r8 = new java.lang.Object[r11]
            r9 = 0
            gnu.mapping.SimpleSymbol r10 = Lit74
            r8[r9] = r10
            gnu.mapping.SimpleSymbol r9 = Lit75
            r8[r12] = r9
            pregexpError$V(r8)
        L_0x0187:
            gnu.expr.GenericProc r8 = kawa.lib.lists.cddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x026d }
            gnu.expr.GenericProc r9 = kawa.lib.lists.car
            java.lang.Object r9 = r9.apply1(r5)
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdddr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x0276 }
            gnu.expr.GenericProc r9 = kawa.lib.lists.cadr
            java.lang.Object r9 = r9.apply1(r5)
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.caddr
            java.lang.Object r2 = r8.apply1(r5)
            goto L_0x0097
        L_0x01b1:
            r0 = r14
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0291 }
            r8 = r0
            r0 = r2
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x029a }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x029a }
            char r1 = kawa.lib.strings.stringRef(r8, r9)
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            boolean r7 = kawa.lib.rnrs.unicode.isCharWhitespace(r8)
            if (r7 == 0) goto L_0x01db
            java.lang.Object r8 = $Stpregexp$Mnspace$Mnsensitive$Qu$St
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 != r9) goto L_0x01dd
        L_0x01d1:
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r2 = r8.apply2(r2, r9)
            goto L_0x009f
        L_0x01db:
            if (r7 != 0) goto L_0x01d1
        L_0x01dd:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.text.Char r9 = Lit47
            boolean r8 = kawa.lib.characters.isChar$Eq(r8, r9)
            if (r8 == 0) goto L_0x020b
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x02a3 }
            java.lang.Boolean r9 = java.lang.Boolean.TRUE
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r8 = r8.apply1(r4)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x02ac }
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r9 = r9.apply2(r2, r10)
            kawa.lib.lists.setCar$Ex(r8, r9)
            goto L_0x00c1
        L_0x020b:
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x02b5 }
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            kawa.lib.lists.setCar$Ex(r8, r9)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cdr
            java.lang.Object r8 = r8.apply1(r4)
            gnu.lists.Pair r8 = (gnu.lists.Pair) r8     // Catch:{ ClassCastException -> 0x02be }
            kawa.lib.lists.setCar$Ex(r8, r2)
            goto L_0x00c1
        L_0x0225:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r14)
            throw r9
        L_0x022e:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r2)
            throw r9
        L_0x0237:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0240:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0249:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0252:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x025b:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0264:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x026d:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0276:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x027f:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0288:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x0291:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r12, (java.lang.Object) r14)
            throw r9
        L_0x029a:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r2)
            throw r9
        L_0x02a3:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x02ac:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x02b5:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        L_0x02be:
            r9 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "set-car!"
            r10.<init>((java.lang.ClassCastException) r9, (java.lang.String) r11, (int) r12, (java.lang.Object) r8)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpWrapQuantifierIfAny(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0086, code lost:
        r6 = kawa.lib.characters.isChar$Eq(gnu.text.Char.make(r1), Lit77);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0090, code lost:
        if (r6 == false) goto L_0x00ab;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x009c, code lost:
        if (kawa.standard.Scheme.numEqu.apply2(r5, Lit8) == java.lang.Boolean.FALSE) goto L_0x00ad;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x009e, code lost:
        r2 = gnu.kawa.functions.AddOp.$Pl.apply2(r9, Lit8);
        r5 = Lit16;
        r9 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ab, code lost:
        if (r6 != false) goto L_0x009e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpReadNums(java.lang.Object r12, java.lang.Object r13, java.lang.Object r14) {
        /*
            r11 = 0
            r10 = 1
            gnu.lists.LList r3 = gnu.lists.LList.Empty
            gnu.lists.LList r4 = gnu.lists.LList.Empty
            gnu.math.IntNum r5 = Lit8
            r9 = r13
        L_0x0009:
            gnu.kawa.functions.NumberCompare r7 = kawa.standard.Scheme.numGEq
            java.lang.Object r7 = r7.apply2(r9, r14)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x001c
            java.lang.Object[] r7 = new java.lang.Object[r10]
            gnu.mapping.SimpleSymbol r8 = Lit76
            r7[r11] = r8
            pregexpError$V(r7)
        L_0x001c:
            r0 = r12
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0112 }
            r7 = r0
            r0 = r9
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x011b }
            r8 = r0
            int r8 = r8.intValue()     // Catch:{ ClassCastException -> 0x011b }
            char r1 = kawa.lib.strings.stringRef(r7, r8)
            gnu.text.Char r7 = gnu.text.Char.make(r1)
            boolean r7 = kawa.lib.rnrs.unicode.isCharNumeric(r7)
            if (r7 == 0) goto L_0x006a
            gnu.kawa.functions.NumberCompare r7 = kawa.standard.Scheme.numEqu
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r7 = r7.apply2(r5, r8)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x0056
            gnu.text.Char r7 = gnu.text.Char.make(r1)
            gnu.lists.Pair r3 = kawa.lib.lists.cons(r7, r3)
            gnu.kawa.functions.AddOp r7 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r2 = r7.apply2(r9, r8)
            gnu.math.IntNum r5 = Lit8
            r9 = r2
            goto L_0x0009
        L_0x0056:
            gnu.text.Char r7 = gnu.text.Char.make(r1)
            gnu.lists.Pair r4 = kawa.lib.lists.cons(r7, r4)
            gnu.kawa.functions.AddOp r7 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r2 = r7.apply2(r9, r8)
            gnu.math.IntNum r5 = Lit16
            r9 = r2
            goto L_0x0009
        L_0x006a:
            gnu.text.Char r7 = gnu.text.Char.make(r1)
            boolean r6 = kawa.lib.rnrs.unicode.isCharWhitespace(r7)
            if (r6 == 0) goto L_0x0084
            java.lang.Object r7 = $Stpregexp$Mnspace$Mnsensitive$Qu$St
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 != r8) goto L_0x0086
        L_0x007a:
            gnu.kawa.functions.AddOp r7 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r2 = r7.apply2(r9, r8)
            r9 = r2
            goto L_0x0009
        L_0x0084:
            if (r6 != 0) goto L_0x007a
        L_0x0086:
            gnu.text.Char r7 = gnu.text.Char.make(r1)
            gnu.text.Char r8 = Lit77
            boolean r6 = kawa.lib.characters.isChar$Eq(r7, r8)
            if (r6 == 0) goto L_0x00ab
            gnu.kawa.functions.NumberCompare r7 = kawa.standard.Scheme.numEqu
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r7 = r7.apply2(r5, r8)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x00ad
        L_0x009e:
            gnu.kawa.functions.AddOp r7 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r2 = r7.apply2(r9, r8)
            gnu.math.IntNum r5 = Lit16
            r9 = r2
            goto L_0x0009
        L_0x00ab:
            if (r6 != 0) goto L_0x009e
        L_0x00ad:
            gnu.text.Char r7 = gnu.text.Char.make(r1)
            gnu.text.Char r8 = Lit78
            boolean r7 = kawa.lib.characters.isChar$Eq(r7, r8)
            if (r7 == 0) goto L_0x010f
            java.lang.Object r7 = pregexpReverse$Ex(r3)
            gnu.lists.LList r7 = (gnu.lists.LList) r7     // Catch:{ ClassCastException -> 0x0125 }
            java.lang.CharSequence r7 = kawa.lib.strings.list$To$String(r7)
            java.lang.Object r3 = kawa.lib.numbers.string$To$Number(r7)
            java.lang.Object r7 = pregexpReverse$Ex(r4)
            gnu.lists.LList r7 = (gnu.lists.LList) r7     // Catch:{ ClassCastException -> 0x012e }
            java.lang.CharSequence r7 = kawa.lib.strings.list$To$String(r7)
            java.lang.Object r4 = kawa.lib.numbers.string$To$Number(r7)
            java.lang.Boolean r7 = java.lang.Boolean.FALSE     // Catch:{ ClassCastException -> 0x0137 }
            if (r3 == r7) goto L_0x00f5
            r7 = r10
        L_0x00da:
            int r7 = r7 + 1
            r6 = r7 & 1
            if (r6 == 0) goto L_0x00f7
            gnu.kawa.functions.NumberCompare r7 = kawa.standard.Scheme.numEqu
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r7 = r7.apply2(r5, r8)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x00f9
        L_0x00ec:
            gnu.math.IntNum r7 = Lit73
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            gnu.lists.Pair r7 = gnu.lists.LList.list3(r7, r8, r9)
        L_0x00f4:
            return r7
        L_0x00f5:
            r7 = r11
            goto L_0x00da
        L_0x00f7:
            if (r6 != 0) goto L_0x00ec
        L_0x00f9:
            gnu.kawa.functions.NumberCompare r7 = kawa.standard.Scheme.numEqu
            gnu.math.IntNum r8 = Lit8
            java.lang.Object r7 = r7.apply2(r5, r8)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x010a
            gnu.lists.Pair r7 = gnu.lists.LList.list3(r3, r3, r9)
            goto L_0x00f4
        L_0x010a:
            gnu.lists.Pair r7 = gnu.lists.LList.list3(r3, r4, r9)
            goto L_0x00f4
        L_0x010f:
            java.lang.Boolean r7 = java.lang.Boolean.FALSE
            goto L_0x00f4
        L_0x0112:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "string-ref"
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r12)
            throw r8
        L_0x011b:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r11 = 2
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r10, (int) r11, (java.lang.Object) r9)
            throw r8
        L_0x0125:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r11 = "list->string"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r11, (int) r10, (java.lang.Object) r7)
            throw r9
        L_0x012e:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r11 = "list->string"
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r11, (int) r10, (java.lang.Object) r7)
            throw r9
        L_0x0137:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "x"
            r10 = -2
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r3)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpReadNums(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public static Object pregexpInvertCharList(Object vv) {
        Object apply1 = lists.car.apply1(vv);
        try {
            lists.setCar$Ex((Pair) apply1, Lit79);
            return vv;
        } catch (ClassCastException e) {
            throw new WrongType(e, "set-car!", 1, apply1);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00bf, code lost:
        if (r7 != false) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0105, code lost:
        if (kawa.lib.characters.isChar$Eq(gnu.text.Char.make(kawa.lib.strings.stringRef((java.lang.CharSequence) r13, ((java.lang.Number) r4).intValue())), Lit46) == false) goto L_0x0107;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0107, code lost:
        r2 = kawa.lib.lists.car.apply1(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0111, code lost:
        if (kawa.lib.characters.isChar(r2) == false) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0113, code lost:
        r11 = Lit83;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        r8 = (java.lang.CharSequence) r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0119, code lost:
        r10 = gnu.kawa.functions.AddOp.$Pl.apply2(r14, Lit8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0128, code lost:
        r6 = kawa.lib.lists.cons(gnu.lists.LList.list3(r11, r2, gnu.text.Char.make(kawa.lib.strings.stringRef(r8, ((java.lang.Number) r10).intValue()))), kawa.lib.lists.cdr.apply1(r6));
        r14 = gnu.kawa.functions.AddOp.$Pl.apply2(r14, Lit16);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0149, code lost:
        if (r7 != false) goto L_0x00c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x014d, code lost:
        r6 = kawa.lib.lists.cons(gnu.text.Char.make(r1), r6);
        r14 = gnu.kawa.functions.AddOp.$Pl.apply2(r14, Lit8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0207, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0210, code lost:
        throw new gnu.mapping.WrongType(r8, "string-ref", 1, r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x0211, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x021a, code lost:
        throw new gnu.mapping.WrongType(r8, "string-ref", 2, r10);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpReadCharList(java.lang.Object r13, java.lang.Object r14, java.lang.Object r15) {
        /*
            gnu.lists.LList r6 = gnu.lists.LList.Empty
        L_0x0002:
            gnu.kawa.functions.NumberCompare r8 = kawa.standard.Scheme.numGEq
            java.lang.Object r8 = r8.apply2(r14, r15)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x001e
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r9 = 0
            gnu.mapping.SimpleSymbol r10 = Lit80
            r8[r9] = r10
            r9 = 1
            gnu.mapping.SimpleSymbol r10 = Lit81
            r8[r9] = r10
            java.lang.Object r8 = pregexpError$V(r8)
        L_0x001d:
            return r8
        L_0x001e:
            r0 = r13
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x01d5 }
            r8 = r0
            r0 = r14
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01df }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x01df }
            char r1 = kawa.lib.strings.stringRef(r8, r9)
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit46
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x006c
            boolean r8 = kawa.lib.lists.isNull(r6)
            if (r8 == 0) goto L_0x0055
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r6)
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            goto L_0x0002
        L_0x0055:
            gnu.mapping.SimpleSymbol r8 = Lit82
            java.lang.Object r9 = pregexpReverse$Ex(r6)
            gnu.lists.Pair r8 = kawa.lib.lists.cons(r8, r9)
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r9 = r9.apply2(r14, r10)
            gnu.lists.Pair r8 = gnu.lists.LList.list2(r8, r9)
            goto L_0x001d
        L_0x006c:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit19
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x00a9
            java.lang.Object r3 = pregexpReadEscapedChar(r13, r14, r15)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r3 == r8) goto L_0x0096
            gnu.expr.GenericProc r8 = kawa.lib.lists.car
            java.lang.Object r8 = r8.apply1(r3)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r6)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cadr
            java.lang.Object r14 = r8.apply1(r3)
            goto L_0x0002
        L_0x0096:
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r9 = 0
            gnu.mapping.SimpleSymbol r10 = Lit80
            r8[r9] = r10
            r9 = 1
            gnu.mapping.SimpleSymbol r10 = Lit22
            r8[r9] = r10
            java.lang.Object r8 = pregexpError$V(r8)
            goto L_0x001d
        L_0x00a9:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit58
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x015f
            boolean r7 = kawa.lib.lists.isNull(r6)
            if (r7 == 0) goto L_0x00d3
            if (r7 == 0) goto L_0x0107
        L_0x00c1:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r6)
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            goto L_0x0002
        L_0x00d3:
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r4 = r8.apply2(r14, r9)
            gnu.kawa.functions.NumberCompare r8 = kawa.standard.Scheme.numLss
            java.lang.Object r9 = r8.apply2(r4, r15)
            r0 = r9
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ ClassCastException -> 0x01e9 }
            r8 = r0
            boolean r7 = r8.booleanValue()     // Catch:{ ClassCastException -> 0x01e9 }
            if (r7 == 0) goto L_0x0149
            r0 = r13
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x01f3 }
            r8 = r0
            r0 = r4
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x01fd }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x01fd }
            char r8 = kawa.lib.strings.stringRef(r8, r9)
            gnu.text.Char r8 = gnu.text.Char.make(r8)
            gnu.text.Char r9 = Lit46
            boolean r8 = kawa.lib.characters.isChar$Eq(r8, r9)
            if (r8 != 0) goto L_0x00c1
        L_0x0107:
            gnu.expr.GenericProc r8 = kawa.lib.lists.car
            java.lang.Object r2 = r8.apply1(r6)
            boolean r8 = kawa.lib.characters.isChar(r2)
            if (r8 == 0) goto L_0x014d
            gnu.mapping.SimpleSymbol r11 = Lit83
            r0 = r13
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0207 }
            r8 = r0
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r10 = r9.apply2(r14, r10)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0211 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x0211 }
            char r8 = kawa.lib.strings.stringRef(r8, r9)
            gnu.text.Char r8 = gnu.text.Char.make(r8)
            gnu.lists.Pair r8 = gnu.lists.LList.list3(r11, r2, r8)
            gnu.expr.GenericProc r9 = kawa.lib.lists.cdr
            java.lang.Object r9 = r9.apply1(r6)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r9)
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit16
            java.lang.Object r14 = r8.apply2(r14, r9)
            goto L_0x0002
        L_0x0149:
            if (r7 == 0) goto L_0x0107
            goto L_0x00c1
        L_0x014d:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r6)
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            goto L_0x0002
        L_0x015f:
            gnu.kawa.functions.IsEqv r8 = kawa.standard.Scheme.isEqv
            gnu.text.Char r9 = gnu.text.Char.make(r1)
            gnu.text.Char r10 = Lit15
            java.lang.Object r8 = r8.apply2(r9, r10)
            java.lang.Boolean r9 = java.lang.Boolean.FALSE
            if (r8 == r9) goto L_0x01c3
            r0 = r13
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x021b }
            r8 = r0
            gnu.kawa.functions.AddOp r9 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r10 = Lit8
            java.lang.Object r10 = r9.apply2(r14, r10)
            r0 = r10
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0225 }
            r9 = r0
            int r9 = r9.intValue()     // Catch:{ ClassCastException -> 0x0225 }
            char r8 = kawa.lib.strings.stringRef(r8, r9)
            gnu.text.Char r8 = gnu.text.Char.make(r8)
            gnu.text.Char r9 = Lit44
            boolean r8 = kawa.lib.characters.isChar$Eq(r8, r9)
            if (r8 == 0) goto L_0x01b1
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit16
            java.lang.Object r8 = r8.apply2(r14, r9)
            java.lang.Object r5 = pregexpReadPosixCharClass(r13, r8, r15)
            gnu.expr.GenericProc r8 = kawa.lib.lists.car
            java.lang.Object r8 = r8.apply1(r5)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r6)
            gnu.expr.GenericProc r8 = kawa.lib.lists.cadr
            java.lang.Object r14 = r8.apply1(r5)
            goto L_0x0002
        L_0x01b1:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r6)
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            goto L_0x0002
        L_0x01c3:
            gnu.text.Char r8 = gnu.text.Char.make(r1)
            gnu.lists.Pair r6 = kawa.lib.lists.cons(r8, r6)
            gnu.kawa.functions.AddOp r8 = gnu.kawa.functions.AddOp.$Pl
            gnu.math.IntNum r9 = Lit8
            java.lang.Object r14 = r8.apply2(r14, r9)
            goto L_0x0002
        L_0x01d5:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r11 = 1
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r13)
            throw r9
        L_0x01df:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r11 = 2
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r14)
            throw r9
        L_0x01e9:
            r8 = move-exception
            gnu.mapping.WrongType r10 = new gnu.mapping.WrongType
            java.lang.String r11 = "x"
            r12 = -2
            r10.<init>((java.lang.ClassCastException) r8, (java.lang.String) r11, (int) r12, (java.lang.Object) r9)
            throw r10
        L_0x01f3:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r11 = 1
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r13)
            throw r9
        L_0x01fd:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r11 = 2
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r4)
            throw r9
        L_0x0207:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r11 = 1
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r13)
            throw r9
        L_0x0211:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-ref"
            r12 = 2
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r11, (int) r12, (java.lang.Object) r10)
            throw r9
        L_0x021b:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r10 = "string-ref"
            r11 = 1
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r10, (int) r11, (java.lang.Object) r13)
            throw r9
        L_0x0225:
            r8 = move-exception
            gnu.mapping.WrongType r9 = new gnu.mapping.WrongType
            java.lang.String r11 = "string-ref"
            r12 = 2
            r9.<init>((java.lang.ClassCastException) r8, (java.lang.String) r11, (int) r12, (java.lang.Object) r10)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpReadCharList(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public static Object pregexpStringMatch(Object s1, Object s, Object i, Object n, Object sk, Object fk) {
        try {
            int n1 = strings.stringLength((CharSequence) s1);
            if (Scheme.numGrt.apply2(Integer.valueOf(n1), n) != Boolean.FALSE) {
                return Scheme.applyToArgs.apply1(fk);
            }
            Object obj = Lit73;
            Object obj2 = i;
            while (Scheme.numGEq.apply2(obj, Integer.valueOf(n1)) == Boolean.FALSE) {
                if (Scheme.numGEq.apply2(obj2, n) != Boolean.FALSE) {
                    return Scheme.applyToArgs.apply1(fk);
                }
                try {
                    try {
                        try {
                            try {
                                if (!characters.isChar$Eq(Char.make(strings.stringRef((CharSequence) s1, ((Number) obj).intValue())), Char.make(strings.stringRef((CharSequence) s, ((Number) obj2).intValue())))) {
                                    return Scheme.applyToArgs.apply1(fk);
                                }
                                obj = AddOp.$Pl.apply2(obj, Lit8);
                                obj2 = AddOp.$Pl.apply2(obj2, Lit8);
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "string-ref", 2, obj2);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "string-ref", 1, s);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "string-ref", 2, obj);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "string-ref", 1, s1);
                }
            }
            return Scheme.applyToArgs.apply2(sk, obj2);
        } catch (ClassCastException e5) {
            throw new WrongType(e5, "string-length", 1, s1);
        }
    }

    public static boolean isPregexpCharWord(Object c) {
        try {
            boolean x = unicode.isCharAlphabetic((Char) c);
            if (x) {
                return x;
            }
            try {
                boolean x2 = unicode.isCharNumeric((Char) c);
                if (x2) {
                    return x2;
                }
                try {
                    return characters.isChar$Eq((Char) c, Lit84);
                } catch (ClassCastException e) {
                    throw new WrongType(e, "char=?", 1, c);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "char-numeric?", 1, c);
            }
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "char-alphabetic?", 1, c);
        }
    }

    public static Object isPregexpAtWordBoundary(Object s, Object i, Object n) {
        Boolean x;
        Object apply2 = Scheme.numEqu.apply2(i, Lit73);
        try {
            boolean x2 = ((Boolean) apply2).booleanValue();
            if (x2) {
                return x2 ? Boolean.TRUE : Boolean.FALSE;
            }
            Object apply22 = Scheme.numGEq.apply2(i, n);
            try {
                boolean x3 = ((Boolean) apply22).booleanValue();
                if (x3) {
                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                }
                try {
                    try {
                        char c$Sli = strings.stringRef((CharSequence) s, ((Number) i).intValue());
                        try {
                            CharSequence charSequence = (CharSequence) s;
                            Object apply23 = AddOp.$Mn.apply2(i, Lit8);
                            try {
                                char c$Sli$Mn1 = strings.stringRef(charSequence, ((Number) apply23).intValue());
                                Object c$Sli$Slw$Qu = isPregexpCheckIfInCharClass(Char.make(c$Sli), Lit41);
                                Object c$Sli$Mn1$Slw$Qu = isPregexpCheckIfInCharClass(Char.make(c$Sli$Mn1), Lit41);
                                if (c$Sli$Slw$Qu != Boolean.FALSE) {
                                    x = c$Sli$Mn1$Slw$Qu != Boolean.FALSE ? Boolean.FALSE : Boolean.TRUE;
                                } else {
                                    x = c$Sli$Slw$Qu;
                                }
                                if (x != Boolean.FALSE) {
                                    return x;
                                }
                                try {
                                    boolean x4 = ((c$Sli$Slw$Qu != Boolean.FALSE ? 1 : 0) + 1) & true;
                                    if (x4) {
                                        return c$Sli$Mn1$Slw$Qu;
                                    }
                                    return x4 ? Boolean.TRUE : Boolean.FALSE;
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "x", -2, c$Sli$Slw$Qu);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 2, apply23);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "string-ref", 1, s);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "string-ref", 2, i);
                    }
                } catch (ClassCastException e5) {
                    throw new WrongType(e5, "string-ref", 1, s);
                }
            } catch (ClassCastException e6) {
                throw new WrongType(e6, "x", -2, apply22);
            }
        } catch (ClassCastException e7) {
            throw new WrongType(e7, "x", -2, apply2);
        }
    }

    public static Object isPregexpCheckIfInCharClass(Object c, Object char$Mnclass) {
        boolean x = false;
        if (Scheme.isEqv.apply2(char$Mnclass, Lit14) != Boolean.FALSE) {
            try {
                if (characters.isChar$Eq((Char) c, Lit24)) {
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            } catch (ClassCastException e) {
                throw new WrongType(e, "char=?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit85) != Boolean.FALSE) {
            try {
                boolean x2 = unicode.isCharAlphabetic((Char) c);
                if (x2) {
                    return x2 ? Boolean.TRUE : Boolean.FALSE;
                }
                try {
                    return unicode.isCharNumeric((Char) c) ? Boolean.TRUE : Boolean.FALSE;
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "char-numeric?", 1, c);
                }
            } catch (ClassCastException e3) {
                throw new WrongType(e3, "char-alphabetic?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit86) != Boolean.FALSE) {
            try {
                return unicode.isCharAlphabetic((Char) c) ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "char-alphabetic?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit87) != Boolean.FALSE) {
            try {
                return characters.char$To$Integer((Char) c) < 128 ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "char->integer", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit88) != Boolean.FALSE) {
            try {
                boolean x3 = characters.isChar$Eq((Char) c, Lit3);
                if (x3) {
                    return x3 ? Boolean.TRUE : Boolean.FALSE;
                }
                try {
                    Char charR = (Char) c;
                    Object obj = $Stpregexp$Mntab$Mnchar$St;
                    try {
                        return characters.isChar$Eq(charR, (Char) obj) ? Boolean.TRUE : Boolean.FALSE;
                    } catch (ClassCastException e6) {
                        throw new WrongType(e6, "char=?", 2, obj);
                    }
                } catch (ClassCastException e7) {
                    throw new WrongType(e7, "char=?", 1, c);
                }
            } catch (ClassCastException e8) {
                throw new WrongType(e8, "char=?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit89) != Boolean.FALSE) {
            try {
                return characters.char$To$Integer((Char) c) < 32 ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e9) {
                throw new WrongType(e9, "char->integer", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit30) != Boolean.FALSE) {
            try {
                return unicode.isCharNumeric((Char) c) ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e10) {
                throw new WrongType(e10, "char-numeric?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit90) != Boolean.FALSE) {
            try {
                if (characters.char$To$Integer((Char) c) >= 32) {
                    x = true;
                }
                if (!x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                try {
                    return unicode.isCharWhitespace((Char) c) ? Boolean.FALSE : Boolean.TRUE;
                } catch (ClassCastException e11) {
                    throw new WrongType(e11, "char-whitespace?", 1, c);
                }
            } catch (ClassCastException e12) {
                throw new WrongType(e12, "char->integer", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit91) != Boolean.FALSE) {
            try {
                return unicode.isCharLowerCase((Char) c) ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e13) {
                throw new WrongType(e13, "char-lower-case?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit92) != Boolean.FALSE) {
            try {
                return characters.char$To$Integer((Char) c) >= 32 ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e14) {
                throw new WrongType(e14, "char->integer", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit93) != Boolean.FALSE) {
            try {
                if (characters.char$To$Integer((Char) c) >= 32) {
                    x = true;
                }
                if (!x) {
                    return x ? Boolean.TRUE : Boolean.FALSE;
                }
                try {
                    boolean x4 = ((unicode.isCharWhitespace((Char) c) ? 1 : 0) + true) & true;
                    if (!x4) {
                        return x4 ? Boolean.TRUE : Boolean.FALSE;
                    }
                    try {
                        boolean x5 = ((unicode.isCharAlphabetic((Char) c) ? 1 : 0) + true) & true;
                        if (!x5) {
                            return x5 ? Boolean.TRUE : Boolean.FALSE;
                        }
                        try {
                            return unicode.isCharNumeric((Char) c) ? Boolean.FALSE : Boolean.TRUE;
                        } catch (ClassCastException e15) {
                            throw new WrongType(e15, "char-numeric?", 1, c);
                        }
                    } catch (ClassCastException e16) {
                        throw new WrongType(e16, "char-alphabetic?", 1, c);
                    }
                } catch (ClassCastException e17) {
                    throw new WrongType(e17, "char-whitespace?", 1, c);
                }
            } catch (ClassCastException e18) {
                throw new WrongType(e18, "char->integer", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit36) != Boolean.FALSE) {
            try {
                return unicode.isCharWhitespace((Char) c) ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e19) {
                throw new WrongType(e19, "char-whitespace?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit94) != Boolean.FALSE) {
            try {
                return unicode.isCharUpperCase((Char) c) ? Boolean.TRUE : Boolean.FALSE;
            } catch (ClassCastException e20) {
                throw new WrongType(e20, "char-upper-case?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit41) != Boolean.FALSE) {
            try {
                boolean x6 = unicode.isCharAlphabetic((Char) c);
                if (x6) {
                    return x6 ? Boolean.TRUE : Boolean.FALSE;
                }
                try {
                    boolean x7 = unicode.isCharNumeric((Char) c);
                    if (x7) {
                        return x7 ? Boolean.TRUE : Boolean.FALSE;
                    }
                    try {
                        return characters.isChar$Eq((Char) c, Lit84) ? Boolean.TRUE : Boolean.FALSE;
                    } catch (ClassCastException e21) {
                        throw new WrongType(e21, "char=?", 1, c);
                    }
                } catch (ClassCastException e22) {
                    throw new WrongType(e22, "char-numeric?", 1, c);
                }
            } catch (ClassCastException e23) {
                throw new WrongType(e23, "char-alphabetic?", 1, c);
            }
        } else if (Scheme.isEqv.apply2(char$Mnclass, Lit95) != Boolean.FALSE) {
            try {
                boolean x8 = unicode.isCharNumeric((Char) c);
                if (x8) {
                    return x8 ? Boolean.TRUE : Boolean.FALSE;
                }
                try {
                    boolean x9 = unicode.isCharCi$Eq((Char) c, Lit2);
                    if (x9) {
                        return x9 ? Boolean.TRUE : Boolean.FALSE;
                    }
                    try {
                        boolean x10 = unicode.isCharCi$Eq((Char) c, Lit25);
                        if (x10) {
                            return x10 ? Boolean.TRUE : Boolean.FALSE;
                        }
                        try {
                            boolean x11 = unicode.isCharCi$Eq((Char) c, Lit96);
                            if (x11) {
                                return x11 ? Boolean.TRUE : Boolean.FALSE;
                            }
                            try {
                                boolean x12 = unicode.isCharCi$Eq((Char) c, Lit29);
                                if (x12) {
                                    return x12 ? Boolean.TRUE : Boolean.FALSE;
                                }
                                try {
                                    boolean x13 = unicode.isCharCi$Eq((Char) c, Lit97);
                                    if (x13) {
                                        return x13 ? Boolean.TRUE : Boolean.FALSE;
                                    }
                                    try {
                                        return unicode.isCharCi$Eq((Char) c, Lit98) ? Boolean.TRUE : Boolean.FALSE;
                                    } catch (ClassCastException e24) {
                                        throw new WrongType(e24, "char-ci=?", 1, c);
                                    }
                                } catch (ClassCastException e25) {
                                    throw new WrongType(e25, "char-ci=?", 1, c);
                                }
                            } catch (ClassCastException e26) {
                                throw new WrongType(e26, "char-ci=?", 1, c);
                            }
                        } catch (ClassCastException e27) {
                            throw new WrongType(e27, "char-ci=?", 1, c);
                        }
                    } catch (ClassCastException e28) {
                        throw new WrongType(e28, "char-ci=?", 1, c);
                    }
                } catch (ClassCastException e29) {
                    throw new WrongType(e29, "char-ci=?", 1, c);
                }
            } catch (ClassCastException e30) {
                throw new WrongType(e30, "char-numeric?", 1, c);
            }
        } else {
            return pregexpError$V(new Object[]{Lit99});
        }
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 33:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 34:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 47:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            default:
                return super.match2(moduleMethod, obj, obj2, callContext);
        }
    }

    public static Object pregexpListRef(Object s, Object i) {
        Object obj = Lit73;
        while (!lists.isNull(s)) {
            if (Scheme.numEqu.apply2(obj, i) != Boolean.FALSE) {
                return lists.car.apply1(s);
            }
            s = lists.cdr.apply1(s);
            obj = AddOp.$Pl.apply2(obj, Lit8);
        }
        return Boolean.FALSE;
    }

    public static Object pregexpMakeBackrefList(Object re) {
        return lambda1sub(re);
    }

    public static Object lambda1sub(Object re) {
        if (!lists.isPair(re)) {
            return LList.Empty;
        }
        Object car$Mnre = lists.car.apply1(re);
        Object sub$Mncdr$Mnre = lambda1sub(lists.cdr.apply1(re));
        if (Scheme.isEqv.apply2(car$Mnre, Lit100) != Boolean.FALSE) {
            return lists.cons(lists.cons(re, Boolean.FALSE), sub$Mncdr$Mnre);
        }
        return append.append$V(new Object[]{lambda1sub(car$Mnre), sub$Mncdr$Mnre});
    }

    /* compiled from: pregexp.scm */
    public class frame extends ModuleBody {
        Object backrefs;
        Object case$Mnsensitive$Qu;
        Procedure identity;
        Object n;
        Object s;
        Object sn;
        Object start;

        public frame() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 15, pregexp.Lit112, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:460");
            this.identity = moduleMethod;
        }

        public static Object lambda2identity(Object x) {
            return x;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 15 ? lambda2identity(obj) : super.apply1(moduleMethod, obj);
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

        static Boolean lambda4() {
            return Boolean.FALSE;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:103:0x02c9, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit17) == java.lang.Boolean.FALSE) goto L_0x02f7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:105:0x02d7, code lost:
            if (kawa.standard.Scheme.numGEq.apply2(r6.i, r15.n) == java.lang.Boolean.FALSE) goto L_0x02e3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:109:0x0301, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit5) == java.lang.Boolean.FALSE) goto L_0x0313;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:112:0x031d, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit4) == java.lang.Boolean.FALSE) goto L_0x032d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:115:0x0337, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit20) == java.lang.Boolean.FALSE) goto L_0x03ad;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:116:0x0339, code lost:
            r8 = gnu.kawa.slib.pregexp.pregexpListRef(r15.backrefs, kawa.lib.lists.cadr.apply1(r6.re$1));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:117:0x0349, code lost:
            if (r8 == java.lang.Boolean.FALSE) goto L_0x0389;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:118:0x034b, code lost:
            r7 = kawa.lib.lists.cdr.apply1(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:120:0x0353, code lost:
            if (r7 == java.lang.Boolean.FALSE) goto L_0x03a1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:121:0x0355, code lost:
            r1 = r15.s;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:123:?, code lost:
            r1 = (java.lang.CharSequence) r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:124:0x0359, code lost:
            r3 = kawa.lib.lists.car.apply1(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:126:?, code lost:
            r4 = ((java.lang.Number) r3).intValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:127:0x0366, code lost:
            r3 = kawa.lib.lists.cdr.apply1(r7);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:131:0x0389, code lost:
            gnu.kawa.slib.pregexp.pregexpError$V(new java.lang.Object[]{gnu.kawa.slib.pregexp.Lit101, gnu.kawa.slib.pregexp.Lit102, r6.re$1});
            r7 = java.lang.Boolean.FALSE;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:134:0x03b7, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit100) == java.lang.Boolean.FALSE) goto L_0x03cd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:137:0x03d7, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit103) == java.lang.Boolean.FALSE) goto L_0x0405;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:139:0x03ed, code lost:
            if (lambda3sub(kawa.lib.lists.cadr.apply1(r6.re$1), r6.i, r15.identity, gnu.kawa.slib.pregexp.lambda$Fn6) == java.lang.Boolean.FALSE) goto L_0x03fb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:143:0x040f, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit104) == java.lang.Boolean.FALSE) goto L_0x043d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:145:0x0425, code lost:
            if (lambda3sub(kawa.lib.lists.cadr.apply1(r6.re$1), r6.i, r15.identity, gnu.kawa.slib.pregexp.lambda$Fn7) == java.lang.Boolean.FALSE) goto L_0x0431;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:149:0x0447, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit105) == java.lang.Boolean.FALSE) goto L_0x048f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:150:0x0449, code lost:
            r11 = r15.n;
            r12 = r15.sn;
            r15.n = r6.i;
            r15.sn = r6.i;
            r10 = lambda3sub(gnu.lists.LList.list4(gnu.kawa.slib.pregexp.Lit5, gnu.kawa.slib.pregexp.Lit106, kawa.lib.lists.cadr.apply1(r6.re$1), gnu.kawa.slib.pregexp.Lit12), gnu.kawa.slib.pregexp.Lit73, r15.identity, gnu.kawa.slib.pregexp.lambda$Fn8);
            r15.n = r11;
            r15.sn = r12;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:151:0x0477, code lost:
            if (r10 == java.lang.Boolean.FALSE) goto L_0x0485;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:155:0x0499, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit107) == java.lang.Boolean.FALSE) goto L_0x04e1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:156:0x049b, code lost:
            r11 = r15.n;
            r12 = r15.sn;
            r15.n = r6.i;
            r15.sn = r6.i;
            r10 = lambda3sub(gnu.lists.LList.list4(gnu.kawa.slib.pregexp.Lit5, gnu.kawa.slib.pregexp.Lit108, kawa.lib.lists.cadr.apply1(r6.re$1), gnu.kawa.slib.pregexp.Lit12), gnu.kawa.slib.pregexp.Lit73, r15.identity, gnu.kawa.slib.pregexp.lambda$Fn9);
            r15.n = r11;
            r15.sn = r12;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:157:0x04c9, code lost:
            if (r10 == java.lang.Boolean.FALSE) goto L_0x04d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:161:0x04eb, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit109) == java.lang.Boolean.FALSE) goto L_0x0517;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:162:0x04ed, code lost:
            r10 = lambda3sub(kawa.lib.lists.cadr.apply1(r6.re$1), r6.i, r15.identity, gnu.kawa.slib.pregexp.lambda$Fn10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:163:0x0501, code lost:
            if (r10 == java.lang.Boolean.FALSE) goto L_0x050d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:166:0x0517, code lost:
            r14 = kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit60);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:167:0x0521, code lost:
            if (r14 == java.lang.Boolean.FALSE) goto L_0x0551;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:169:0x0525, code lost:
            if (r14 == java.lang.Boolean.FALSE) goto L_0x055d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:170:0x0527, code lost:
            r6.old = r15.case$Mnsensitive$Qu;
            r15.case$Mnsensitive$Qu = kawa.standard.Scheme.isEqv.apply2(kawa.lib.lists.car.apply1(r6.re$1), gnu.kawa.slib.pregexp.Lit60);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:172:0x055b, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit61) != java.lang.Boolean.FALSE) goto L_0x0527;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:174:0x0567, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit68) == java.lang.Boolean.FALSE) goto L_0x05c2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:175:0x0569, code lost:
            r1 = kawa.lib.lists.cadr.apply1(r6.re$1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:178:0x0573, code lost:
            if (r1 == java.lang.Boolean.FALSE) goto L_0x05bb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:179:0x0575, code lost:
            r1 = 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:180:0x0576, code lost:
            r6.maximal$Qu = (r1 + 1) & true;
            r6.p = kawa.lib.lists.caddr.apply1(r6.re$1);
            r6.q = kawa.lib.lists.cadddr.apply1(r6.re$1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:181:0x0592, code lost:
            if (r6.maximal$Qu == false) goto L_0x05bf;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:182:0x0594, code lost:
            r1 = r6.q;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:185:0x0598, code lost:
            if (r1 == java.lang.Boolean.FALSE) goto L_0x05bd;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:186:0x059a, code lost:
            r1 = 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:187:0x059b, code lost:
            r1 = (r1 + 1) & true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:188:0x059f, code lost:
            r6.could$Mnloop$Mninfinitely$Qu = r1;
            r6.re = kawa.lib.lists.car.apply1(kawa.lib.lists.cddddr.apply1(r6.re$1));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:189:0x05bb, code lost:
            r1 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:190:0x05bd, code lost:
            r1 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:191:0x05bf, code lost:
            r1 = r6.maximal$Qu;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:194:0x05dc, code lost:
            if (kawa.standard.Scheme.numGEq.apply2(r6.i, r15.n) == java.lang.Boolean.FALSE) goto L_0x05e8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:203:0x060a, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:205:0x0613, code lost:
            throw new gnu.mapping.WrongType(r2, "string-ref", 1, r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:206:0x0614, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:208:0x061d, code lost:
            throw new gnu.mapping.WrongType(r1, "string-ref", 2, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:209:0x061e, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:211:0x0627, code lost:
            throw new gnu.mapping.WrongType(r2, "string-ref", 1, r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:212:0x0628, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:214:0x0631, code lost:
            throw new gnu.mapping.WrongType(r1, "string-ref", 2, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:215:0x0632, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:217:0x063b, code lost:
            throw new gnu.mapping.WrongType(r2, "substring", 1, r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:218:0x063c, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:220:0x0645, code lost:
            throw new gnu.mapping.WrongType(r1, "substring", 2, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:221:0x0646, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:223:0x064f, code lost:
            throw new gnu.mapping.WrongType(r1, "substring", 3, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:224:0x0650, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:226:0x0659, code lost:
            throw new gnu.mapping.WrongType(r2, "maximal?", -2, r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:227:0x065a, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:229:0x0663, code lost:
            throw new gnu.mapping.WrongType(r2, "could-loop-infinitely?", -2, r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:240:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, gnu.kawa.functions.AddOp.$Pl.apply2(r6.i, gnu.kawa.slib.pregexp.Lit8));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:242:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, gnu.kawa.functions.AddOp.$Pl.apply2(r6.i, gnu.kawa.slib.pregexp.Lit8));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:243:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:244:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:245:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:246:?, code lost:
            return gnu.kawa.slib.pregexp.pregexpError$V(new java.lang.Object[]{gnu.kawa.slib.pregexp.Lit101});
         */
        /* JADX WARNING: Code restructure failed: missing block: B:247:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:248:?, code lost:
            return r6.lambda5loupOneOfChars(kawa.lib.lists.cdr.apply1(r6.re$1));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:249:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:250:?, code lost:
            return lambda3sub(kawa.lib.lists.cadr.apply1(r6.re$1), r6.i, r6.lambda$Fn2, r6.lambda$Fn3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:251:?, code lost:
            return r6.lambda6loupSeq(kawa.lib.lists.cdr.apply1(r6.re$1), r6.i);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:252:?, code lost:
            return r6.lambda7loupOr(kawa.lib.lists.cdr.apply1(r6.re$1));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:253:?, code lost:
            return gnu.kawa.slib.pregexp.pregexpStringMatch(kawa.lib.strings.substring(r1, r4, ((java.lang.Number) r3).intValue()), r15.s, r6.i, r15.n, r6.lambda$Fn4, r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:254:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, r6.i);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:255:?, code lost:
            return lambda3sub(kawa.lib.lists.cadr.apply1(r6.re$1), r6.i, r6.lambda$Fn5, r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:256:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, r6.i);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:257:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:258:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:259:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, r6.i);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:260:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, r6.i);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:261:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:262:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:263:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, r6.i);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:264:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply2(r6.sk, r10);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:265:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:266:?, code lost:
            return lambda3sub(kawa.lib.lists.cadr.apply1(r6.re$1), r6.i, r6.lambda$Fn11, r6.lambda$Fn12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:267:?, code lost:
            return r6.lambda8loupP(gnu.kawa.slib.pregexp.Lit73, r6.i);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:268:?, code lost:
            return gnu.kawa.slib.pregexp.pregexpError$V(new java.lang.Object[]{gnu.kawa.slib.pregexp.Lit101});
         */
        /* JADX WARNING: Code restructure failed: missing block: B:269:?, code lost:
            return kawa.standard.Scheme.applyToArgs.apply1(r6.fk);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:270:?, code lost:
            return gnu.kawa.slib.pregexp.pregexpError$V(new java.lang.Object[]{gnu.kawa.slib.pregexp.Lit101});
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0148, code lost:
            r14 = ((kawa.lib.lists.isPair(r6.re$1) ? 1 : 0) + true) & true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0152, code lost:
            if (r14 == false) goto L_0x01a5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0160, code lost:
            if (kawa.standard.Scheme.numLss.apply2(r6.i, r15.n) == java.lang.Boolean.FALSE) goto L_0x01a7;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0162, code lost:
            r1 = r15.s;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
            r1 = (java.lang.CharSequence) r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:51:0x0166, code lost:
            r3 = r6.i;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x0180, code lost:
            if (gnu.kawa.slib.pregexp.isPregexpCheckIfInCharClass(gnu.text.Char.make(kawa.lib.strings.stringRef(r1, ((java.lang.Number) r3).intValue())), r6.re$1) == java.lang.Boolean.FALSE) goto L_0x0227;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:59:0x01a5, code lost:
            if (r14 != false) goto L_0x0162;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:0x01a7, code lost:
            r14 = kawa.lib.lists.isPair(r6.re$1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:0x01ad, code lost:
            if (r14 == false) goto L_0x0269;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:0x01af, code lost:
            r14 = kawa.standard.Scheme.isEqv.apply2(kawa.lib.lists.car.apply1(r6.re$1), gnu.kawa.slib.pregexp.Lit83);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:0x01c1, code lost:
            if (r14 == java.lang.Boolean.FALSE) goto L_0x0231;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:0x01cf, code lost:
            if (kawa.standard.Scheme.numLss.apply2(r6.i, r15.n) == java.lang.Boolean.FALSE) goto L_0x0235;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:0x01d1, code lost:
            r1 = r15.s;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            r1 = (java.lang.CharSequence) r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x01d5, code lost:
            r3 = r6.i;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x01de, code lost:
            r8 = kawa.lib.strings.stringRef(r1, ((java.lang.Number) r3).intValue());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:0x01e7, code lost:
            if (r15.case$Mnsensitive$Qu == java.lang.Boolean.FALSE) goto L_0x026d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x01e9, code lost:
            r9 = kawa.lib.characters.char$Ls$Eq$Qu;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x01eb, code lost:
            r14 = r9.apply2(kawa.lib.lists.cadr.apply1(r6.re$1), gnu.text.Char.make(r8));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x01fd, code lost:
            if (r14 == java.lang.Boolean.FALSE) goto L_0x0271;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x0211, code lost:
            if (r9.apply2(gnu.text.Char.make(r8), kawa.lib.lists.caddr.apply1(r6.re$1)) == java.lang.Boolean.FALSE) goto L_0x0275;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:82:0x0233, code lost:
            if (r14 != java.lang.Boolean.FALSE) goto L_0x01d1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:84:0x023b, code lost:
            if (kawa.lib.lists.isPair(r6.re$1) == false) goto L_0x05d0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:85:0x023d, code lost:
            r13 = kawa.lib.lists.car.apply1(r6.re$1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:86:0x024f, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit83) == java.lang.Boolean.FALSE) goto L_0x028d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:88:0x025d, code lost:
            if (kawa.standard.Scheme.numGEq.apply2(r6.i, r15.n) == java.lang.Boolean.FALSE) goto L_0x027f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:90:0x0269, code lost:
            if (r14 == false) goto L_0x0235;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:0x026d, code lost:
            r9 = kawa.lib.rnrs.unicode.char$Mnci$Ls$Eq$Qu;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:93:0x0273, code lost:
            if (r14 != java.lang.Boolean.FALSE) goto L_0x0213;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:97:0x0297, code lost:
            if (kawa.standard.Scheme.isEqv.apply2(r13, gnu.kawa.slib.pregexp.Lit82) == java.lang.Boolean.FALSE) goto L_0x02bf;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:99:0x02a5, code lost:
            if (kawa.standard.Scheme.numGEq.apply2(r6.i, r15.n) == java.lang.Boolean.FALSE) goto L_0x02b1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Object lambda3sub(java.lang.Object r16, java.lang.Object r17, java.lang.Object r18, java.lang.Object r19) {
            /*
                r15 = this;
                gnu.kawa.slib.pregexp$frame0 r6 = new gnu.kawa.slib.pregexp$frame0
                r6.<init>()
                r6.staticLink = r15
                r0 = r16
                r6.re$1 = r0
                r0 = r17
                r6.i = r0
                r0 = r18
                r6.sk = r0
                r0 = r19
                r6.fk = r0
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                java.lang.Object r2 = r6.re$1
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit10
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0047
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numEqu
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.start
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x003e
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
            L_0x003d:
                return r1
            L_0x003e:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x0047:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                java.lang.Object r2 = r6.re$1
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit12
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0077
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numGEq
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x006e
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x006e:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x0077:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                java.lang.Object r2 = r6.re$1
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit23
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0090
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x0090:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                java.lang.Object r2 = r6.re$1
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit26
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x00c1
                java.lang.Object r1 = r15.s
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = gnu.kawa.slib.pregexp.isPregexpAtWordBoundary(r1, r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x00b7
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x00b7:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x00c1:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                java.lang.Object r2 = r6.re$1
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit28
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x00f3
                java.lang.Object r1 = r15.s
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = gnu.kawa.slib.pregexp.isPregexpAtWordBoundary(r1, r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x00e7
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x00e7:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x00f3:
                java.lang.Object r1 = r6.re$1
                boolean r14 = kawa.lib.characters.isChar(r1)
                if (r14 == 0) goto L_0x0146
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numLss
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0148
            L_0x0109:
                java.lang.Object r1 = r15.case$Mnsensitive$Qu
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0196
                gnu.expr.ModuleMethod r1 = kawa.lib.characters.char$Eq$Qu
                r4 = r1
            L_0x0112:
                java.lang.Object r1 = r15.s
                java.lang.CharSequence r1 = (java.lang.CharSequence) r1     // Catch:{ ClassCastException -> 0x05f6 }
                java.lang.Object r3 = r6.i
                r0 = r3
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0600 }
                r2 = r0
                int r2 = r2.intValue()     // Catch:{ ClassCastException -> 0x0600 }
                char r1 = kawa.lib.strings.stringRef(r1, r2)
                gnu.text.Char r1 = gnu.text.Char.make(r1)
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r4.apply2(r1, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x019b
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                gnu.kawa.functions.AddOp r3 = gnu.kawa.functions.AddOp.$Pl
                java.lang.Object r4 = r6.i
                gnu.math.IntNum r5 = gnu.kawa.slib.pregexp.Lit8
                java.lang.Object r3 = r3.apply2(r4, r5)
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x0146:
                if (r14 != 0) goto L_0x0109
            L_0x0148:
                java.lang.Object r1 = r6.re$1
                boolean r1 = kawa.lib.lists.isPair(r1)
                int r1 = r1 + 1
                r14 = r1 & 1
                if (r14 == 0) goto L_0x01a5
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numLss
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x01a7
            L_0x0162:
                java.lang.Object r1 = r15.s
                java.lang.CharSequence r1 = (java.lang.CharSequence) r1     // Catch:{ ClassCastException -> 0x060a }
                java.lang.Object r3 = r6.i
                r0 = r3
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0614 }
                r2 = r0
                int r2 = r2.intValue()     // Catch:{ ClassCastException -> 0x0614 }
                char r1 = kawa.lib.strings.stringRef(r1, r2)
                gnu.text.Char r1 = gnu.text.Char.make(r1)
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = gnu.kawa.slib.pregexp.isPregexpCheckIfInCharClass(r1, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0227
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                gnu.kawa.functions.AddOp r3 = gnu.kawa.functions.AddOp.$Pl
                java.lang.Object r4 = r6.i
                gnu.math.IntNum r5 = gnu.kawa.slib.pregexp.Lit8
                java.lang.Object r3 = r3.apply2(r4, r5)
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x0196:
                gnu.expr.ModuleMethod r1 = kawa.lib.rnrs.unicode.char$Mnci$Eq$Qu
                r4 = r1
                goto L_0x0112
            L_0x019b:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x01a5:
                if (r14 != 0) goto L_0x0162
            L_0x01a7:
                java.lang.Object r1 = r6.re$1
                boolean r14 = kawa.lib.lists.isPair(r1)
                if (r14 == 0) goto L_0x0269
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.expr.GenericProc r2 = kawa.lib.lists.car
                java.lang.Object r3 = r6.re$1
                java.lang.Object r2 = r2.apply1(r3)
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit83
                java.lang.Object r14 = r1.apply2(r2, r3)
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r14 == r1) goto L_0x0231
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numLss
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0235
            L_0x01d1:
                java.lang.Object r1 = r15.s
                java.lang.CharSequence r1 = (java.lang.CharSequence) r1     // Catch:{ ClassCastException -> 0x061e }
                java.lang.Object r3 = r6.i
                r0 = r3
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0628 }
                r2 = r0
                int r2 = r2.intValue()     // Catch:{ ClassCastException -> 0x0628 }
                char r8 = kawa.lib.strings.stringRef(r1, r2)
                java.lang.Object r1 = r15.case$Mnsensitive$Qu
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x026d
                gnu.expr.ModuleMethod r9 = kawa.lib.characters.char$Ls$Eq$Qu
            L_0x01eb:
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                gnu.text.Char r2 = gnu.text.Char.make(r8)
                java.lang.Object r14 = r9.apply2(r1, r2)
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r14 == r1) goto L_0x0271
                gnu.text.Char r1 = gnu.text.Char.make(r8)
                gnu.expr.GenericProc r2 = kawa.lib.lists.caddr
                java.lang.Object r3 = r6.re$1
                java.lang.Object r2 = r2.apply1(r3)
                java.lang.Object r1 = r9.apply2(r1, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0275
            L_0x0213:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                gnu.kawa.functions.AddOp r3 = gnu.kawa.functions.AddOp.$Pl
                java.lang.Object r4 = r6.i
                gnu.math.IntNum r5 = gnu.kawa.slib.pregexp.Lit8
                java.lang.Object r3 = r3.apply2(r4, r5)
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x0227:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x0231:
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r14 != r1) goto L_0x01d1
            L_0x0235:
                java.lang.Object r1 = r6.re$1
                boolean r1 = kawa.lib.lists.isPair(r1)
                if (r1 == 0) goto L_0x05d0
                gnu.expr.GenericProc r1 = kawa.lib.lists.car
                java.lang.Object r2 = r6.re$1
                java.lang.Object r13 = r1.apply1(r2)
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit83
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x028d
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numGEq
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x027f
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x0269:
                if (r14 == 0) goto L_0x0235
                goto L_0x01d1
            L_0x026d:
                gnu.expr.ModuleMethod r9 = kawa.lib.rnrs.unicode.char$Mnci$Ls$Eq$Qu
                goto L_0x01eb
            L_0x0271:
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r14 != r1) goto L_0x0213
            L_0x0275:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x027f:
                r1 = 1
                java.lang.Object[] r1 = new java.lang.Object[r1]
                r2 = 0
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit101
                r1[r2] = r3
                java.lang.Object r1 = gnu.kawa.slib.pregexp.pregexpError$V(r1)
                goto L_0x003d
            L_0x028d:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit82
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x02bf
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numGEq
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x02b1
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x02b1:
                gnu.expr.GenericProc r1 = kawa.lib.lists.cdr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r1 = r6.lambda5loupOneOfChars(r1)
                goto L_0x003d
            L_0x02bf:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit17
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x02f7
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numGEq
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x02e3
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x02e3:
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r2 = r6.i
                gnu.expr.ModuleMethod r3 = r6.lambda$Fn2
                gnu.expr.ModuleMethod r4 = r6.lambda$Fn3
                java.lang.Object r1 = r15.lambda3sub(r1, r2, r3, r4)
                goto L_0x003d
            L_0x02f7:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit5
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0313
                gnu.expr.GenericProc r1 = kawa.lib.lists.cdr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r2 = r6.i
                java.lang.Object r1 = r6.lambda6loupSeq(r1, r2)
                goto L_0x003d
            L_0x0313:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit4
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x032d
                gnu.expr.GenericProc r1 = kawa.lib.lists.cdr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r1 = r6.lambda7loupOr(r1)
                goto L_0x003d
            L_0x032d:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit20
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x03ad
                java.lang.Object r1 = r15.backrefs
                gnu.expr.GenericProc r2 = kawa.lib.lists.cadr
                java.lang.Object r3 = r6.re$1
                java.lang.Object r2 = r2.apply1(r3)
                java.lang.Object r8 = gnu.kawa.slib.pregexp.pregexpListRef(r1, r2)
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r8 == r1) goto L_0x0389
                gnu.expr.GenericProc r1 = kawa.lib.lists.cdr
                java.lang.Object r7 = r1.apply1(r8)
            L_0x0351:
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r7 == r1) goto L_0x03a1
                java.lang.Object r1 = r15.s
                java.lang.CharSequence r1 = (java.lang.CharSequence) r1     // Catch:{ ClassCastException -> 0x0632 }
                gnu.expr.GenericProc r2 = kawa.lib.lists.car
                java.lang.Object r3 = r2.apply1(r7)
                r0 = r3
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x063c }
                r2 = r0
                int r4 = r2.intValue()     // Catch:{ ClassCastException -> 0x063c }
                gnu.expr.GenericProc r2 = kawa.lib.lists.cdr
                java.lang.Object r3 = r2.apply1(r7)
                r0 = r3
                java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x0646 }
                r2 = r0
                int r2 = r2.intValue()     // Catch:{ ClassCastException -> 0x0646 }
                java.lang.CharSequence r1 = kawa.lib.strings.substring(r1, r4, r2)
                java.lang.Object r2 = r15.s
                java.lang.Object r3 = r6.i
                java.lang.Object r4 = r15.n
                gnu.expr.ModuleMethod r5 = r6.lambda$Fn4
                java.lang.Object r6 = r6.fk
                java.lang.Object r1 = gnu.kawa.slib.pregexp.pregexpStringMatch(r1, r2, r3, r4, r5, r6)
                goto L_0x003d
            L_0x0389:
                r1 = 3
                java.lang.Object[] r1 = new java.lang.Object[r1]
                r2 = 0
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit101
                r1[r2] = r3
                r2 = 1
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit102
                r1[r2] = r3
                r2 = 2
                java.lang.Object r3 = r6.re$1
                r1[r2] = r3
                gnu.kawa.slib.pregexp.pregexpError$V(r1)
                java.lang.Boolean r7 = java.lang.Boolean.FALSE
                goto L_0x0351
            L_0x03a1:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x03ad:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit100
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x03cd
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r2 = r6.i
                gnu.expr.ModuleMethod r3 = r6.lambda$Fn5
                java.lang.Object r4 = r6.fk
                java.lang.Object r1 = r15.lambda3sub(r1, r2, r3, r4)
                goto L_0x003d
            L_0x03cd:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit103
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0405
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r2 = r6.i
                gnu.mapping.Procedure r3 = r15.identity
                gnu.expr.ModuleMethod r4 = gnu.kawa.slib.pregexp.lambda$Fn6
                java.lang.Object r10 = r15.lambda3sub(r1, r2, r3, r4)
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r10 == r1) goto L_0x03fb
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x03fb:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x0405:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit104
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x043d
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r2 = r6.i
                gnu.mapping.Procedure r3 = r15.identity
                gnu.expr.ModuleMethod r4 = gnu.kawa.slib.pregexp.lambda$Fn7
                java.lang.Object r10 = r15.lambda3sub(r1, r2, r3, r4)
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r10 == r1) goto L_0x0431
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x0431:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x043d:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit105
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x048f
                java.lang.Object r11 = r15.n
                java.lang.Object r12 = r15.sn
                java.lang.Object r1 = r6.i
                r15.n = r1
                java.lang.Object r1 = r6.i
                r15.sn = r1
                gnu.mapping.SimpleSymbol r1 = gnu.kawa.slib.pregexp.Lit5
                gnu.lists.PairWithPosition r2 = gnu.kawa.slib.pregexp.Lit106
                gnu.expr.GenericProc r3 = kawa.lib.lists.cadr
                java.lang.Object r4 = r6.re$1
                java.lang.Object r3 = r3.apply1(r4)
                gnu.mapping.SimpleSymbol r4 = gnu.kawa.slib.pregexp.Lit12
                gnu.lists.Pair r1 = gnu.lists.LList.list4(r1, r2, r3, r4)
                gnu.math.IntNum r2 = gnu.kawa.slib.pregexp.Lit73
                gnu.mapping.Procedure r3 = r15.identity
                gnu.expr.ModuleMethod r4 = gnu.kawa.slib.pregexp.lambda$Fn8
                java.lang.Object r10 = r15.lambda3sub(r1, r2, r3, r4)
                r15.n = r11
                r15.sn = r12
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r10 == r1) goto L_0x0485
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x0485:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x048f:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit107
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x04e1
                java.lang.Object r11 = r15.n
                java.lang.Object r12 = r15.sn
                java.lang.Object r1 = r6.i
                r15.n = r1
                java.lang.Object r1 = r6.i
                r15.sn = r1
                gnu.mapping.SimpleSymbol r1 = gnu.kawa.slib.pregexp.Lit5
                gnu.lists.PairWithPosition r2 = gnu.kawa.slib.pregexp.Lit108
                gnu.expr.GenericProc r3 = kawa.lib.lists.cadr
                java.lang.Object r4 = r6.re$1
                java.lang.Object r3 = r3.apply1(r4)
                gnu.mapping.SimpleSymbol r4 = gnu.kawa.slib.pregexp.Lit12
                gnu.lists.Pair r1 = gnu.lists.LList.list4(r1, r2, r3, r4)
                gnu.math.IntNum r2 = gnu.kawa.slib.pregexp.Lit73
                gnu.mapping.Procedure r3 = r15.identity
                gnu.expr.ModuleMethod r4 = gnu.kawa.slib.pregexp.lambda$Fn9
                java.lang.Object r10 = r15.lambda3sub(r1, r2, r3, r4)
                r15.n = r11
                r15.sn = r12
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r10 == r1) goto L_0x04d5
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x04d5:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r3 = r6.i
                java.lang.Object r1 = r1.apply2(r2, r3)
                goto L_0x003d
            L_0x04e1:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit109
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x0517
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r2 = r6.i
                gnu.mapping.Procedure r3 = r15.identity
                gnu.expr.ModuleMethod r4 = gnu.kawa.slib.pregexp.lambda$Fn10
                java.lang.Object r10 = r15.lambda3sub(r1, r2, r3, r4)
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r10 == r1) goto L_0x050d
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.sk
                java.lang.Object r1 = r1.apply2(r2, r10)
                goto L_0x003d
            L_0x050d:
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x0517:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit60
                java.lang.Object r14 = r1.apply2(r13, r2)
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r14 == r1) goto L_0x0551
                java.lang.Boolean r1 = java.lang.Boolean.FALSE
                if (r14 == r1) goto L_0x055d
            L_0x0527:
                java.lang.Object r1 = r15.case$Mnsensitive$Qu
                r6.old = r1
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.expr.GenericProc r2 = kawa.lib.lists.car
                java.lang.Object r3 = r6.re$1
                java.lang.Object r2 = r2.apply1(r3)
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit60
                java.lang.Object r1 = r1.apply2(r2, r3)
                r15.case$Mnsensitive$Qu = r1
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Object r2 = r6.i
                gnu.expr.ModuleMethod r3 = r6.lambda$Fn11
                gnu.expr.ModuleMethod r4 = r6.lambda$Fn12
                java.lang.Object r1 = r15.lambda3sub(r1, r2, r3, r4)
                goto L_0x003d
            L_0x0551:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit61
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 != r2) goto L_0x0527
            L_0x055d:
                gnu.kawa.functions.IsEqv r1 = kawa.standard.Scheme.isEqv
                gnu.mapping.SimpleSymbol r2 = gnu.kawa.slib.pregexp.Lit68
                java.lang.Object r1 = r1.apply2(r13, r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x05c2
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE     // Catch:{ ClassCastException -> 0x0650 }
                if (r1 == r2) goto L_0x05bb
                r1 = 1
            L_0x0576:
                int r1 = r1 + 1
                r1 = r1 & 1
                r6.maximal$Qu = r1
                gnu.expr.GenericProc r1 = kawa.lib.lists.caddr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                r6.p = r1
                gnu.expr.GenericProc r1 = kawa.lib.lists.cadddr
                java.lang.Object r2 = r6.re$1
                java.lang.Object r1 = r1.apply1(r2)
                r6.q = r1
                boolean r1 = r6.maximal$Qu
                if (r1 == 0) goto L_0x05bf
                java.lang.Object r1 = r6.q
                java.lang.Boolean r2 = java.lang.Boolean.FALSE     // Catch:{ ClassCastException -> 0x065a }
                if (r1 == r2) goto L_0x05bd
                r1 = 1
            L_0x059b:
                int r1 = r1 + 1
                r1 = r1 & 1
            L_0x059f:
                r6.could$Mnloop$Mninfinitely$Qu = r1
                gnu.expr.GenericProc r1 = kawa.lib.lists.car
                gnu.expr.GenericProc r2 = kawa.lib.lists.cddddr
                java.lang.Object r3 = r6.re$1
                java.lang.Object r2 = r2.apply1(r3)
                java.lang.Object r1 = r1.apply1(r2)
                r6.re = r1
                gnu.math.IntNum r1 = gnu.kawa.slib.pregexp.Lit73
                java.lang.Object r2 = r6.i
                java.lang.Object r1 = r6.lambda8loupP(r1, r2)
                goto L_0x003d
            L_0x05bb:
                r1 = 0
                goto L_0x0576
            L_0x05bd:
                r1 = 0
                goto L_0x059b
            L_0x05bf:
                boolean r1 = r6.maximal$Qu
                goto L_0x059f
            L_0x05c2:
                r1 = 1
                java.lang.Object[] r1 = new java.lang.Object[r1]
                r2 = 0
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit101
                r1[r2] = r3
                java.lang.Object r1 = gnu.kawa.slib.pregexp.pregexpError$V(r1)
                goto L_0x003d
            L_0x05d0:
                gnu.kawa.functions.NumberCompare r1 = kawa.standard.Scheme.numGEq
                java.lang.Object r2 = r6.i
                java.lang.Object r3 = r15.n
                java.lang.Object r1 = r1.apply2(r2, r3)
                java.lang.Boolean r2 = java.lang.Boolean.FALSE
                if (r1 == r2) goto L_0x05e8
                gnu.kawa.functions.ApplyToArgs r1 = kawa.standard.Scheme.applyToArgs
                java.lang.Object r2 = r6.fk
                java.lang.Object r1 = r1.apply1(r2)
                goto L_0x003d
            L_0x05e8:
                r1 = 1
                java.lang.Object[] r1 = new java.lang.Object[r1]
                r2 = 0
                gnu.mapping.SimpleSymbol r3 = gnu.kawa.slib.pregexp.Lit101
                r1[r2] = r3
                java.lang.Object r1 = gnu.kawa.slib.pregexp.pregexpError$V(r1)
                goto L_0x003d
            L_0x05f6:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r5 = 1
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r1)
                throw r3
            L_0x0600:
                r1 = move-exception
                gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r5 = 2
                r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r5, (java.lang.Object) r3)
                throw r2
            L_0x060a:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r5 = 1
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r1)
                throw r3
            L_0x0614:
                r1 = move-exception
                gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r5 = 2
                r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r5, (java.lang.Object) r3)
                throw r2
            L_0x061e:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r5 = 1
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r1)
                throw r3
            L_0x0628:
                r1 = move-exception
                gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
                java.lang.String r4 = "string-ref"
                r5 = 2
                r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r5, (java.lang.Object) r3)
                throw r2
            L_0x0632:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "substring"
                r5 = 1
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r1)
                throw r3
            L_0x063c:
                r1 = move-exception
                gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
                java.lang.String r4 = "substring"
                r5 = 2
                r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r5, (java.lang.Object) r3)
                throw r2
            L_0x0646:
                r1 = move-exception
                gnu.mapping.WrongType r2 = new gnu.mapping.WrongType
                java.lang.String r4 = "substring"
                r5 = 3
                r2.<init>((java.lang.ClassCastException) r1, (java.lang.String) r4, (int) r5, (java.lang.Object) r3)
                throw r2
            L_0x0650:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "maximal?"
                r5 = -2
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r1)
                throw r3
            L_0x065a:
                r2 = move-exception
                gnu.mapping.WrongType r3 = new gnu.mapping.WrongType
                java.lang.String r4 = "could-loop-infinitely?"
                r5 = -2
                r3.<init>((java.lang.ClassCastException) r2, (java.lang.String) r4, (int) r5, (java.lang.Object) r1)
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.frame.lambda3sub(java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
        }
    }

    public static Object pregexpMatchPositionsAux(Object re, Object s, Object sn, Object start, Object n, Object i) {
        frame frame6 = new frame();
        frame6.s = s;
        frame6.sn = sn;
        frame6.start = start;
        frame6.n = n;
        Procedure procedure = frame6.identity;
        Object pregexpMakeBackrefList = pregexpMakeBackrefList(re);
        frame6.case$Mnsensitive$Qu = Boolean.TRUE;
        frame6.backrefs = pregexpMakeBackrefList;
        frame6.identity = procedure;
        frame6.lambda3sub(re, i, frame6.identity, lambda$Fn1);
        Object arg0 = frame6.backrefs;
        Object obj = LList.Empty;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                obj = Pair.make(lists.cdr.apply1(arg02.getCar()), obj);
                arg0 = arg03;
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, arg0);
            }
        }
        LList backrefs = LList.reverseInPlace(obj);
        Object x = lists.car.apply1(backrefs);
        if (x != Boolean.FALSE) {
            return backrefs;
        }
        return x;
    }

    public int match0(ModuleMethod moduleMethod, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 36:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 37:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 38:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 39:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 40:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 41:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            default:
                return super.match0(moduleMethod, callContext);
        }
    }

    /* compiled from: pregexp.scm */
    public class frame0 extends ModuleBody {
        boolean could$Mnloop$Mninfinitely$Qu;
        Object fk;
        Object i;
        final ModuleMethod lambda$Fn11;
        final ModuleMethod lambda$Fn12;
        final ModuleMethod lambda$Fn2;
        final ModuleMethod lambda$Fn3;
        final ModuleMethod lambda$Fn4;
        final ModuleMethod lambda$Fn5;
        boolean maximal$Qu;
        Object old;
        Object p;
        Object q;
        Object re;
        Object re$1;
        Object sk;
        frame staticLink;

        public frame0() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 9, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:513");
            this.lambda$Fn2 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 10, (Object) null, 0);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:514");
            this.lambda$Fn3 = moduleMethod2;
            ModuleMethod moduleMethod3 = new ModuleMethod(this, 11, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod3.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:541");
            this.lambda$Fn4 = moduleMethod3;
            ModuleMethod moduleMethod4 = new ModuleMethod(this, 12, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod4.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:545");
            this.lambda$Fn5 = moduleMethod4;
            ModuleMethod moduleMethod5 = new ModuleMethod(this, 13, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod5.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:587");
            this.lambda$Fn11 = moduleMethod5;
            ModuleMethod moduleMethod6 = new ModuleMethod(this, 14, (Object) null, 0);
            moduleMethod6.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:590");
            this.lambda$Fn12 = moduleMethod6;
        }

        public Object lambda5loupOneOfChars(Object chars) {
            frame1 frame1 = new frame1();
            frame1.staticLink = this;
            frame1.chars = chars;
            if (lists.isNull(frame1.chars)) {
                return Scheme.applyToArgs.apply1(this.fk);
            }
            return this.staticLink.lambda3sub(lists.car.apply1(frame1.chars), this.i, this.sk, frame1.lambda$Fn13);
        }

        /* access modifiers changed from: package-private */
        public Object lambda9(Object i1) {
            return Scheme.applyToArgs.apply1(this.fk);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 9:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 11:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 12:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 13:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda10() {
            return Scheme.applyToArgs.apply2(this.sk, AddOp.$Pl.apply2(this.i, pregexp.Lit8));
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 10:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 14:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }

        public Object lambda6loupSeq(Object res, Object i2) {
            frame2 frame2 = new frame2();
            frame2.staticLink = this;
            frame2.res = res;
            if (lists.isNull(frame2.res)) {
                return Scheme.applyToArgs.apply2(this.sk, i2);
            }
            return this.staticLink.lambda3sub(lists.car.apply1(frame2.res), i2, frame2.lambda$Fn14, this.fk);
        }

        public Object lambda7loupOr(Object res) {
            frame3 frame3 = new frame3();
            frame3.staticLink = this;
            frame3.res = res;
            if (lists.isNull(frame3.res)) {
                return Scheme.applyToArgs.apply1(this.fk);
            }
            return this.staticLink.lambda3sub(lists.car.apply1(frame3.res), this.i, frame3.lambda$Fn15, frame3.lambda$Fn16);
        }

        /* access modifiers changed from: package-private */
        public Object lambda11(Object i2) {
            return Scheme.applyToArgs.apply2(this.sk, i2);
        }

        /* access modifiers changed from: package-private */
        public Object lambda12(Object i1) {
            Object assv = lists.assv(this.re$1, this.staticLink.backrefs);
            try {
                lists.setCdr$Ex((Pair) assv, lists.cons(this.i, i1));
                return Scheme.applyToArgs.apply2(this.sk, i1);
            } catch (ClassCastException e) {
                throw new WrongType(e, "set-cdr!", 1, assv);
            }
        }

        static Boolean lambda13() {
            return Boolean.FALSE;
        }

        static Boolean lambda14() {
            return Boolean.FALSE;
        }

        static Boolean lambda15() {
            return Boolean.FALSE;
        }

        static Boolean lambda16() {
            return Boolean.FALSE;
        }

        static Boolean lambda17() {
            return Boolean.FALSE;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 9:
                    return lambda9(obj);
                case 11:
                    return lambda11(obj);
                case 12:
                    return lambda12(obj);
                case 13:
                    return lambda18(obj);
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda18(Object i1) {
            this.staticLink.case$Mnsensitive$Qu = this.old;
            return Scheme.applyToArgs.apply2(this.sk, i1);
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 10:
                    return lambda10();
                case 14:
                    return lambda19();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda19() {
            this.staticLink.case$Mnsensitive$Qu = this.old;
            return Scheme.applyToArgs.apply1(this.fk);
        }

        public Object lambda8loupP(Object k, Object i2) {
            frame4 frame4 = new frame4();
            frame4.staticLink = this;
            frame4.k = k;
            frame4.i = i2;
            if (Scheme.numLss.apply2(frame4.k, this.p) != Boolean.FALSE) {
                return this.staticLink.lambda3sub(this.re, frame4.i, frame4.lambda$Fn17, this.fk);
            }
            frame4.q = this.q != Boolean.FALSE ? AddOp.$Mn.apply2(this.q, this.p) : this.q;
            return frame4.lambda24loupQ(pregexp.Lit73, frame4.i);
        }
    }

    /* compiled from: pregexp.scm */
    public class frame1 extends ModuleBody {
        Object chars;
        final ModuleMethod lambda$Fn13;
        frame0 staticLink;

        public frame1() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 1, (Object) null, 0);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:508");
            this.lambda$Fn13 = moduleMethod;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 1 ? lambda20() : super.apply0(moduleMethod);
        }

        /* access modifiers changed from: package-private */
        public Object lambda20() {
            return this.staticLink.lambda5loupOneOfChars(lists.cdr.apply1(this.chars));
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 1) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }
    }

    /* compiled from: pregexp.scm */
    public class frame2 extends ModuleBody {
        final ModuleMethod lambda$Fn14;
        Object res;
        frame0 staticLink;

        public frame2() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 2, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:519");
            this.lambda$Fn14 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 2 ? lambda21(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda21(Object i1) {
            return this.staticLink.lambda6loupSeq(lists.cdr.apply1(this.res), i1);
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
    }

    /* compiled from: pregexp.scm */
    public class frame3 extends ModuleBody {
        final ModuleMethod lambda$Fn15;
        final ModuleMethod lambda$Fn16;
        Object res;
        frame0 staticLink;

        public frame3() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 3, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:526");
            this.lambda$Fn15 = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 4, (Object) null, 0);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:529");
            this.lambda$Fn16 = moduleMethod2;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 3 ? lambda22(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda22(Object i1) {
            Object x = Scheme.applyToArgs.apply2(this.staticLink.sk, i1);
            return x != Boolean.FALSE ? x : this.staticLink.lambda7loupOr(lists.cdr.apply1(this.res));
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 3) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 4 ? lambda23() : super.apply0(moduleMethod);
        }

        /* access modifiers changed from: package-private */
        public Object lambda23() {
            return this.staticLink.lambda7loupOr(lists.cdr.apply1(this.res));
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 4) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }
    }

    public Object apply0(ModuleMethod moduleMethod) {
        switch (moduleMethod.selector) {
            case 36:
                return frame.lambda4();
            case 37:
                return frame0.lambda13();
            case 38:
                return frame0.lambda14();
            case 39:
                return frame0.lambda15();
            case 40:
                return frame0.lambda16();
            case 41:
                return frame0.lambda17();
            default:
                return super.apply0(moduleMethod);
        }
    }

    /* compiled from: pregexp.scm */
    public class frame4 extends ModuleBody {
        Object i;
        Object k;
        final ModuleMethod lambda$Fn17;
        Object q;
        frame0 staticLink;

        public frame4() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 8, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:602");
            this.lambda$Fn17 = moduleMethod;
        }

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 8 ? lambda25(obj) : super.apply1(moduleMethod, obj);
        }

        /* access modifiers changed from: package-private */
        public Object lambda25(Object i1) {
            if (!this.staticLink.could$Mnloop$Mninfinitely$Qu ? this.staticLink.could$Mnloop$Mninfinitely$Qu : Scheme.numEqu.apply2(i1, this.i) != Boolean.FALSE) {
                pregexp.pregexpError$V(new Object[]{pregexp.Lit101, pregexp.Lit110});
            }
            return this.staticLink.lambda8loupP(AddOp.$Pl.apply2(this.k, pregexp.Lit8), i1);
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            if (moduleMethod.selector != 8) {
                return super.match1(moduleMethod, obj, callContext);
            }
            callContext.value1 = obj;
            callContext.proc = moduleMethod;
            callContext.pc = 1;
            return 0;
        }

        public Object lambda24loupQ(Object k2, Object i2) {
            frame5 frame5 = new frame5();
            frame5.staticLink = this;
            frame5.k = k2;
            frame5.i = i2;
            frame5.fk = frame5.fk;
            if (this.q == Boolean.FALSE ? this.q != Boolean.FALSE : Scheme.numGEq.apply2(frame5.k, this.q) != Boolean.FALSE) {
                return frame5.lambda26fk();
            }
            if (this.staticLink.maximal$Qu) {
                return this.staticLink.staticLink.lambda3sub(this.staticLink.re, frame5.i, frame5.lambda$Fn18, frame5.fk);
            }
            Object x = frame5.lambda26fk();
            return x == Boolean.FALSE ? this.staticLink.staticLink.lambda3sub(this.staticLink.re, frame5.i, frame5.lambda$Fn19, frame5.fk) : x;
        }
    }

    /* compiled from: pregexp.scm */
    public class frame5 extends ModuleBody {
        Procedure fk;
        Object i;
        Object k;
        final ModuleMethod lambda$Fn18;
        final ModuleMethod lambda$Fn19;
        frame4 staticLink;

        public frame5() {
            ModuleMethod moduleMethod = new ModuleMethod(this, 5, pregexp.Lit111, 0);
            moduleMethod.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:612");
            this.fk = moduleMethod;
            ModuleMethod moduleMethod2 = new ModuleMethod(this, 6, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod2.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:617");
            this.lambda$Fn18 = moduleMethod2;
            ModuleMethod moduleMethod3 = new ModuleMethod(this, 7, (Object) null, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            moduleMethod3.setProperty("source-location", "/u2/home/jis/ai2-kawa/gnu/kawa/slib/pregexp.scm:628");
            this.lambda$Fn19 = moduleMethod3;
        }

        public Object apply0(ModuleMethod moduleMethod) {
            return moduleMethod.selector == 5 ? lambda26fk() : super.apply0(moduleMethod);
        }

        public Object lambda26fk() {
            return Scheme.applyToArgs.apply2(this.staticLink.staticLink.sk, this.i);
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            if (moduleMethod.selector != 5) {
                return super.match0(moduleMethod, callContext);
            }
            callContext.proc = moduleMethod;
            callContext.pc = 0;
            return 0;
        }

        /* access modifiers changed from: package-private */
        public Object lambda27(Object i1) {
            if (!this.staticLink.staticLink.could$Mnloop$Mninfinitely$Qu ? this.staticLink.staticLink.could$Mnloop$Mninfinitely$Qu : Scheme.numEqu.apply2(i1, this.i) != Boolean.FALSE) {
                pregexp.pregexpError$V(new Object[]{pregexp.Lit101, pregexp.Lit110});
            }
            Object x = this.staticLink.lambda24loupQ(AddOp.$Pl.apply2(this.k, pregexp.Lit8), i1);
            return x != Boolean.FALSE ? x : lambda26fk();
        }

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 6:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 7:
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
                case 6:
                    return lambda27(obj);
                case 7:
                    return lambda28(obj);
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        /* access modifiers changed from: package-private */
        public Object lambda28(Object i1) {
            return this.staticLink.lambda24loupQ(AddOp.$Pl.apply2(this.k, pregexp.Lit8), i1);
        }
    }

    public static Object pregexpReplaceAux(Object str, Object ins, Object n, Object backrefs) {
        Object br;
        Number i = Lit73;
        Object obj = "";
        while (Scheme.numGEq.apply2(i, n) == Boolean.FALSE) {
            try {
                try {
                    char c = strings.stringRef((CharSequence) ins, i.intValue());
                    if (characters.isChar$Eq(Char.make(c), Lit19)) {
                        Object br$Mni = pregexpReadEscapedNumber(ins, i, n);
                        if (br$Mni != Boolean.FALSE) {
                            br = lists.car.apply1(br$Mni);
                        } else {
                            try {
                                CharSequence charSequence = (CharSequence) ins;
                                Object apply2 = AddOp.$Pl.apply2(i, Lit8);
                                try {
                                    br = characters.isChar$Eq(Char.make(strings.stringRef(charSequence, ((Number) apply2).intValue())), Lit113) ? Lit73 : Boolean.FALSE;
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "string-ref", 2, apply2);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "string-ref", 1, ins);
                            }
                        }
                        if (br$Mni != Boolean.FALSE) {
                            i = lists.cadr.apply1(br$Mni);
                        } else if (br != Boolean.FALSE) {
                            i = AddOp.$Pl.apply2(i, Lit16);
                        } else {
                            i = AddOp.$Pl.apply2(i, Lit8);
                        }
                        if (br == Boolean.FALSE) {
                            try {
                                try {
                                    char c2 = strings.stringRef((CharSequence) ins, ((Number) i).intValue());
                                    i = AddOp.$Pl.apply2(i, Lit8);
                                    if (!characters.isChar$Eq(Char.make(c2), Lit11)) {
                                        obj = strings.stringAppend(obj, strings.$make$string$(Char.make(c2)));
                                    }
                                } catch (ClassCastException e3) {
                                    throw new WrongType(e3, "string-ref", 2, i);
                                }
                            } catch (ClassCastException e4) {
                                throw new WrongType(e4, "string-ref", 1, ins);
                            }
                        } else {
                            Object backref = pregexpListRef(backrefs, br);
                            if (backref != Boolean.FALSE) {
                                Object[] objArr = new Object[2];
                                objArr[0] = obj;
                                try {
                                    CharSequence charSequence2 = (CharSequence) str;
                                    Object apply1 = lists.car.apply1(backref);
                                    try {
                                        int intValue = ((Number) apply1).intValue();
                                        Object apply12 = lists.cdr.apply1(backref);
                                        try {
                                            objArr[1] = strings.substring(charSequence2, intValue, ((Number) apply12).intValue());
                                            obj = strings.stringAppend(objArr);
                                        } catch (ClassCastException e5) {
                                            throw new WrongType(e5, "substring", 3, apply12);
                                        }
                                    } catch (ClassCastException e6) {
                                        throw new WrongType(e6, "substring", 2, apply1);
                                    }
                                } catch (ClassCastException e7) {
                                    throw new WrongType(e7, "substring", 1, str);
                                }
                            }
                        }
                    } else {
                        i = AddOp.$Pl.apply2(i, Lit8);
                        obj = strings.stringAppend(obj, strings.$make$string$(Char.make(c)));
                    }
                } catch (ClassCastException e8) {
                    throw new WrongType(e8, "string-ref", 2, (Object) i);
                }
            } catch (ClassCastException e9) {
                throw new WrongType(e9, "string-ref", 1, ins);
            }
        }
        return obj;
    }

    public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
        return moduleMethod.selector == 43 ? pregexpReplaceAux(obj, obj2, obj3, obj4) : super.apply4(moduleMethod, obj, obj2, obj3, obj4);
    }

    public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
        if (moduleMethod.selector != 43) {
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

    public static Pair pregexp(Object s) {
        $Stpregexp$Mnspace$Mnsensitive$Qu$St = Boolean.TRUE;
        try {
            return LList.list2(Lit100, lists.car.apply1(pregexpReadPattern(s, Lit73, Integer.valueOf(strings.stringLength((CharSequence) s)))));
        } catch (ClassCastException e) {
            throw new WrongType(e, "string-length", 1, s);
        }
    }

    public static Object pregexpMatchPositions$V(Object pat, Object str, Object[] argsArray) {
        Object start;
        Object end;
        LList opt$Mnargs = LList.makeList(argsArray, 0);
        if (strings.isString(pat)) {
            pat = pregexp(pat);
        } else if (!lists.isPair(pat)) {
            pregexpError$V(new Object[]{Lit114, Lit115, pat});
        }
        try {
            int str$Mnlen = strings.stringLength((CharSequence) str);
            if (lists.isNull(opt$Mnargs)) {
                start = Lit73;
            } else {
                start = lists.car.apply1(opt$Mnargs);
                Object apply1 = lists.cdr.apply1(opt$Mnargs);
                try {
                    opt$Mnargs = (LList) apply1;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "opt-args", -2, apply1);
                }
            }
            if (lists.isNull(opt$Mnargs)) {
                end = Integer.valueOf(str$Mnlen);
            } else {
                end = lists.car.apply1(opt$Mnargs);
            }
            Object i = start;
            while (true) {
                Object apply2 = Scheme.numLEq.apply2(i, end);
                try {
                    boolean x = ((Boolean) apply2).booleanValue();
                    if (!x) {
                        return x ? Boolean.TRUE : Boolean.FALSE;
                    }
                    Object x2 = pregexpMatchPositionsAux(pat, str, Integer.valueOf(str$Mnlen), start, end, i);
                    if (x2 != Boolean.FALSE) {
                        return x2;
                    }
                    i = AddOp.$Pl.apply2(i, Lit8);
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "x", -2, apply2);
                }
            }
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "string-length", 1, str);
        }
    }

    public static Object pregexpMatch$V(Object pat, Object str, Object[] argsArray) {
        Object ix$Mnprs = Scheme.apply.apply4(pregexp$Mnmatch$Mnpositions, pat, str, LList.makeList(argsArray, 0));
        if (ix$Mnprs == Boolean.FALSE) {
            return ix$Mnprs;
        }
        Object obj = LList.Empty;
        Object arg0 = ix$Mnprs;
        while (arg0 != LList.Empty) {
            try {
                Pair arg02 = (Pair) arg0;
                Object arg03 = arg02.getCdr();
                Object ix$Mnpr = arg02.getCar();
                if (ix$Mnpr != Boolean.FALSE) {
                    try {
                        CharSequence charSequence = (CharSequence) str;
                        Object apply1 = lists.car.apply1(ix$Mnpr);
                        try {
                            int intValue = ((Number) apply1).intValue();
                            Object apply12 = lists.cdr.apply1(ix$Mnpr);
                            try {
                                ix$Mnpr = strings.substring(charSequence, intValue, ((Number) apply12).intValue());
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "substring", 3, apply12);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "substring", 2, apply1);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "substring", 1, str);
                    }
                }
                obj = Pair.make(ix$Mnpr, obj);
                arg0 = arg03;
            } catch (ClassCastException e4) {
                throw new WrongType(e4, "arg0", -2, arg0);
            }
        }
        return LList.reverseInPlace(obj);
    }

    public Object applyN(ModuleMethod moduleMethod, Object[] objArr) {
        switch (moduleMethod.selector) {
            case 17:
                return pregexpError$V(objArr);
            case 30:
                return pregexpStringMatch(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5]);
            case 42:
                return pregexpMatchPositionsAux(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5]);
            case 45:
                Object obj = objArr[0];
                Object obj2 = objArr[1];
                int length = objArr.length - 2;
                Object[] objArr2 = new Object[length];
                while (true) {
                    length--;
                    if (length < 0) {
                        return pregexpMatchPositions$V(obj, obj2, objArr2);
                    }
                    objArr2[length] = objArr[length + 2];
                }
            case 46:
                Object obj3 = objArr[0];
                Object obj4 = objArr[1];
                int length2 = objArr.length - 2;
                Object[] objArr3 = new Object[length2];
                while (true) {
                    length2--;
                    if (length2 < 0) {
                        return pregexpMatch$V(obj3, obj4, objArr3);
                    }
                    objArr3[length2] = objArr[length2 + 2];
                }
            default:
                return super.applyN(moduleMethod, objArr);
        }
    }

    public static Object pregexpSplit(Object pat, Object str) {
        try {
            int n = strings.stringLength((CharSequence) str);
            Object obj = Lit73;
            LList lList = LList.Empty;
            Boolean bool = Boolean.FALSE;
            while (Scheme.numGEq.apply2(obj, Integer.valueOf(n)) == Boolean.FALSE) {
                Object temp = pregexpMatchPositions$V(pat, str, new Object[]{obj, Integer.valueOf(n)});
                if (temp != Boolean.FALSE) {
                    Object jk = lists.car.apply1(temp);
                    Object j = lists.car.apply1(jk);
                    Object k = lists.cdr.apply1(jk);
                    if (Scheme.numEqu.apply2(j, k) != Boolean.FALSE) {
                        Object i = AddOp.$Pl.apply2(k, Lit8);
                        try {
                            CharSequence charSequence = (CharSequence) str;
                            try {
                                int intValue = ((Number) obj).intValue();
                                Object apply2 = AddOp.$Pl.apply2(j, Lit8);
                                try {
                                    lList = lists.cons(strings.substring(charSequence, intValue, ((Number) apply2).intValue()), lList);
                                    bool = Boolean.TRUE;
                                    obj = i;
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "substring", 3, apply2);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "substring", 2, obj);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "substring", 1, str);
                        }
                    } else {
                        Object apply22 = Scheme.numEqu.apply2(j, obj);
                        try {
                            boolean x = ((Boolean) apply22).booleanValue();
                            if (!x ? x : bool != Boolean.FALSE) {
                                bool = Boolean.FALSE;
                                obj = k;
                            } else {
                                try {
                                    try {
                                        try {
                                            lList = lists.cons(strings.substring((CharSequence) str, ((Number) obj).intValue(), ((Number) j).intValue()), lList);
                                            bool = Boolean.FALSE;
                                            obj = k;
                                        } catch (ClassCastException e4) {
                                            throw new WrongType(e4, "substring", 3, j);
                                        }
                                    } catch (ClassCastException e5) {
                                        throw new WrongType(e5, "substring", 2, obj);
                                    }
                                } catch (ClassCastException e6) {
                                    throw new WrongType(e6, "substring", 1, str);
                                }
                            }
                        } catch (ClassCastException e7) {
                            throw new WrongType(e7, "x", -2, apply22);
                        }
                    }
                } else {
                    Integer i2 = Integer.valueOf(n);
                    try {
                        try {
                            lList = lists.cons(strings.substring((CharSequence) str, ((Number) obj).intValue(), n), lList);
                            bool = Boolean.FALSE;
                            obj = i2;
                        } catch (ClassCastException e8) {
                            throw new WrongType(e8, "substring", 2, obj);
                        }
                    } catch (ClassCastException e9) {
                        throw new WrongType(e9, "substring", 1, str);
                    }
                }
            }
            return pregexpReverse$Ex(lList);
        } catch (ClassCastException e10) {
            throw new WrongType(e10, "string-length", 1, str);
        }
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        switch (moduleMethod.selector) {
            case 33:
                return isPregexpCheckIfInCharClass(obj, obj2);
            case 34:
                return pregexpListRef(obj, obj2);
            case 47:
                return pregexpSplit(obj, obj2);
            default:
                return super.apply2(moduleMethod, obj, obj2);
        }
    }

    public static Object pregexpReplace(Object pat, Object str, Object ins) {
        try {
            int n = strings.stringLength((CharSequence) str);
            Object pp = pregexpMatchPositions$V(pat, str, new Object[]{Lit73, Integer.valueOf(n)});
            if (pp == Boolean.FALSE) {
                return str;
            }
            try {
                int ins$Mnlen = strings.stringLength((CharSequence) ins);
                Object m$Mni = lists.caar.apply1(pp);
                Object m$Mnn = lists.cdar.apply1(pp);
                Object[] objArr = new Object[3];
                try {
                    try {
                        objArr[0] = strings.substring((CharSequence) str, 0, ((Number) m$Mni).intValue());
                        objArr[1] = pregexpReplaceAux(str, ins, Integer.valueOf(ins$Mnlen), pp);
                        try {
                            try {
                                objArr[2] = strings.substring((CharSequence) str, ((Number) m$Mnn).intValue(), n);
                                return strings.stringAppend(objArr);
                            } catch (ClassCastException e) {
                                throw new WrongType(e, "substring", 2, m$Mnn);
                            }
                        } catch (ClassCastException e2) {
                            throw new WrongType(e2, "substring", 1, str);
                        }
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "substring", 3, m$Mni);
                    }
                } catch (ClassCastException e4) {
                    throw new WrongType(e4, "substring", 1, str);
                }
            } catch (ClassCastException e5) {
                throw new WrongType(e5, "string-length", 1, ins);
            }
        } catch (ClassCastException e6) {
            throw new WrongType(e6, "string-length", 1, str);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v0, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: gnu.math.IntNum} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: gnu.math.IntNum} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpReplace$St(java.lang.Object r13, java.lang.Object r14, java.lang.Object r15) {
        /*
            boolean r7 = kawa.lib.strings.isString(r13)
            if (r7 == 0) goto L_0x000a
            gnu.lists.Pair r13 = pregexp(r13)
        L_0x000a:
            r0 = r14
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x00ab }
            r7 = r0
            int r4 = kawa.lib.strings.stringLength(r7)
            r0 = r15
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x00b5 }
            r7 = r0
            int r3 = kawa.lib.strings.stringLength(r7)
            gnu.math.IntNum r1 = Lit73
            java.lang.String r6 = ""
        L_0x001e:
            gnu.kawa.functions.NumberCompare r7 = kawa.standard.Scheme.numGEq
            java.lang.Integer r8 = java.lang.Integer.valueOf(r4)
            java.lang.Object r7 = r7.apply2(r1, r8)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x002d
        L_0x002c:
            return r6
        L_0x002d:
            r7 = 2
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r8 = 0
            r7[r8] = r1
            r8 = 1
            java.lang.Integer r9 = java.lang.Integer.valueOf(r4)
            r7[r8] = r9
            java.lang.Object r5 = pregexpMatchPositions$V(r13, r14, r7)
            java.lang.Boolean r7 = java.lang.Boolean.FALSE
            if (r5 != r7) goto L_0x006c
            gnu.kawa.functions.NumberCompare r7 = kawa.standard.Scheme.numEqu
            gnu.math.IntNum r8 = Lit73
            java.lang.Object r7 = r7.apply2(r1, r8)
            java.lang.Boolean r8 = java.lang.Boolean.FALSE
            if (r7 == r8) goto L_0x0050
        L_0x004e:
            r6 = r14
            goto L_0x002c
        L_0x0050:
            r7 = 2
            java.lang.Object[] r8 = new java.lang.Object[r7]
            r7 = 0
            r8[r7] = r6
            r9 = 1
            java.lang.CharSequence r14 = (java.lang.CharSequence) r14     // Catch:{ ClassCastException -> 0x00bf }
            r0 = r1
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00c9 }
            r7 = r0
            int r7 = r7.intValue()     // Catch:{ ClassCastException -> 0x00c9 }
            java.lang.CharSequence r7 = kawa.lib.strings.substring(r14, r7, r4)
            r8[r9] = r7
            gnu.lists.FString r14 = kawa.lib.strings.stringAppend(r8)
            goto L_0x004e
        L_0x006c:
            gnu.expr.GenericProc r7 = kawa.lib.lists.cdar
            java.lang.Object r2 = r7.apply1(r5)
            r7 = 3
            java.lang.Object[] r10 = new java.lang.Object[r7]
            r7 = 0
            r10[r7] = r6
            r11 = 1
            r0 = r14
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x00d3 }
            r7 = r0
            r0 = r1
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00dd }
            r8 = r0
            int r12 = r8.intValue()     // Catch:{ ClassCastException -> 0x00dd }
            gnu.expr.GenericProc r8 = kawa.lib.lists.caar
            java.lang.Object r9 = r8.apply1(r5)
            r0 = r9
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x00e7 }
            r8 = r0
            int r8 = r8.intValue()     // Catch:{ ClassCastException -> 0x00e7 }
            java.lang.CharSequence r7 = kawa.lib.strings.substring(r7, r12, r8)
            r10[r11] = r7
            r7 = 2
            java.lang.Integer r8 = java.lang.Integer.valueOf(r3)
            java.lang.Object r8 = pregexpReplaceAux(r14, r15, r8, r5)
            r10[r7] = r8
            gnu.lists.FString r6 = kawa.lib.strings.stringAppend(r10)
            r1 = r2
            goto L_0x001e
        L_0x00ab:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "string-length"
            r10 = 1
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r14)
            throw r8
        L_0x00b5:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "string-length"
            r10 = 1
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r15)
            throw r8
        L_0x00bf:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "substring"
            r10 = 1
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r14)
            throw r8
        L_0x00c9:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "substring"
            r10 = 2
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r1)
            throw r8
        L_0x00d3:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "substring"
            r10 = 1
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r14)
            throw r8
        L_0x00dd:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r9 = "substring"
            r10 = 2
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r9, (int) r10, (java.lang.Object) r1)
            throw r8
        L_0x00e7:
            r7 = move-exception
            gnu.mapping.WrongType r8 = new gnu.mapping.WrongType
            java.lang.String r10 = "substring"
            r11 = 3
            r8.<init>((java.lang.ClassCastException) r7, (java.lang.String) r10, (int) r11, (java.lang.Object) r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpReplace$St(java.lang.Object, java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public Object apply3(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3) {
        switch (moduleMethod.selector) {
            case 18:
                return pregexpReadPattern(obj, obj2, obj3);
            case 19:
                return pregexpReadBranch(obj, obj2, obj3);
            case 20:
                return pregexpReadPiece(obj, obj2, obj3);
            case 21:
                return pregexpReadEscapedNumber(obj, obj2, obj3);
            case 22:
                return pregexpReadEscapedChar(obj, obj2, obj3);
            case 23:
                return pregexpReadPosixCharClass(obj, obj2, obj3);
            case 24:
                return pregexpReadClusterType(obj, obj2, obj3);
            case 25:
                return pregexpReadSubpattern(obj, obj2, obj3);
            case 26:
                return pregexpWrapQuantifierIfAny(obj, obj2, obj3);
            case 27:
                return pregexpReadNums(obj, obj2, obj3);
            case 29:
                return pregexpReadCharList(obj, obj2, obj3);
            case 32:
                return isPregexpAtWordBoundary(obj, obj2, obj3);
            case 48:
                return pregexpReplace(obj, obj2, obj3);
            case 49:
                return pregexpReplace$St(obj, obj2, obj3);
            default:
                return super.apply3(moduleMethod, obj, obj2, obj3);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: java.lang.Integer} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Integer} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: java.lang.Integer} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Object pregexpQuote(java.lang.Object r10) {
        /*
            r9 = 1
            r0 = r10
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0064 }
            r5 = r0
            int r5 = kawa.lib.strings.stringLength(r5)
            int r5 = r5 + -1
            java.lang.Integer r2 = java.lang.Integer.valueOf(r5)
            gnu.lists.LList r5 = gnu.lists.LList.Empty
        L_0x0011:
            gnu.kawa.functions.NumberCompare r6 = kawa.standard.Scheme.numLss
            gnu.math.IntNum r7 = Lit73
            java.lang.Object r6 = r6.apply2(r2, r7)
            java.lang.Boolean r7 = java.lang.Boolean.FALSE
            if (r6 == r7) goto L_0x0024
            gnu.lists.LList r5 = (gnu.lists.LList) r5     // Catch:{ ClassCastException -> 0x006d }
            java.lang.CharSequence r5 = kawa.lib.strings.list$To$String(r5)
            return r5
        L_0x0024:
            gnu.kawa.functions.AddOp r6 = gnu.kawa.functions.AddOp.$Mn
            gnu.math.IntNum r7 = Lit8
            java.lang.Object r3 = r6.apply2(r2, r7)
            r0 = r10
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0     // Catch:{ ClassCastException -> 0x0076 }
            r6 = r0
            r0 = r2
            java.lang.Number r0 = (java.lang.Number) r0     // Catch:{ ClassCastException -> 0x007f }
            r7 = r0
            int r7 = r7.intValue()     // Catch:{ ClassCastException -> 0x007f }
            char r1 = kawa.lib.strings.stringRef(r6, r7)
            gnu.text.Char r6 = gnu.text.Char.make(r1)
            gnu.lists.PairWithPosition r7 = Lit116
            java.lang.Object r6 = kawa.lib.lists.memv(r6, r7)
            java.lang.Boolean r7 = java.lang.Boolean.FALSE
            if (r6 == r7) goto L_0x005b
            gnu.text.Char r6 = Lit19
            gnu.text.Char r7 = gnu.text.Char.make(r1)
            gnu.lists.Pair r5 = kawa.lib.lists.cons(r7, r5)
            gnu.lists.Pair r4 = kawa.lib.lists.cons(r6, r5)
        L_0x0058:
            r5 = r4
            r2 = r3
            goto L_0x0011
        L_0x005b:
            gnu.text.Char r6 = gnu.text.Char.make(r1)
            gnu.lists.Pair r4 = kawa.lib.lists.cons(r6, r5)
            goto L_0x0058
        L_0x0064:
            r5 = move-exception
            gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
            java.lang.String r7 = "string-length"
            r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r9, (java.lang.Object) r10)
            throw r6
        L_0x006d:
            r6 = move-exception
            gnu.mapping.WrongType r7 = new gnu.mapping.WrongType
            java.lang.String r8 = "list->string"
            r7.<init>((java.lang.ClassCastException) r6, (java.lang.String) r8, (int) r9, (java.lang.Object) r5)
            throw r7
        L_0x0076:
            r5 = move-exception
            gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
            java.lang.String r7 = "string-ref"
            r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r9, (java.lang.Object) r10)
            throw r6
        L_0x007f:
            r5 = move-exception
            gnu.mapping.WrongType r6 = new gnu.mapping.WrongType
            java.lang.String r7 = "string-ref"
            r8 = 2
            r6.<init>((java.lang.ClassCastException) r5, (java.lang.String) r7, (int) r8, (java.lang.Object) r2)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.slib.pregexp.pregexpQuote(java.lang.Object):java.lang.Object");
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        switch (moduleMethod.selector) {
            case 16:
                return pregexpReverse$Ex(obj);
            case 28:
                return pregexpInvertCharList(obj);
            case 31:
                return isPregexpCharWord(obj) ? Boolean.TRUE : Boolean.FALSE;
            case 35:
                return pregexpMakeBackrefList(obj);
            case 44:
                return pregexp(obj);
            case 50:
                return pregexpQuote(obj);
            default:
                return super.apply1(moduleMethod, obj);
        }
    }
}
