package appinventor.ai_anon9222030345824.Weather;

import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentTransaction;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.AppInventorCompatActivity;
import com.google.appinventor.components.runtime.Button;
import com.google.appinventor.components.runtime.Component;
import com.google.appinventor.components.runtime.EventDispatcher;
import com.google.appinventor.components.runtime.Form;
import com.google.appinventor.components.runtime.HandlesEventDispatching;
import com.google.appinventor.components.runtime.HorizontalArrangement;
import com.google.appinventor.components.runtime.Label;
import com.google.appinventor.components.runtime.ListView;
import com.google.appinventor.components.runtime.Switch;
import com.google.appinventor.components.runtime.TextBox;
import com.google.appinventor.components.runtime.TinyDB;
import com.google.appinventor.components.runtime.VerticalArrangement;
import com.google.appinventor.components.runtime.VerticalScrollArrangement;
import com.google.appinventor.components.runtime.Web;
import com.google.appinventor.components.runtime.errors.PermissionException;
import com.google.appinventor.components.runtime.errors.StopBlocksExecution;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.RetValManager;
import com.google.appinventor.components.runtime.util.RuntimeErrorAlert;
import com.google.youngandroid.runtime;
import com.puravidaapps.TaifunTools;
import com.sunny.CornerRadius.CornerRadius;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.Apply;
import gnu.kawa.functions.Format;
import gnu.kawa.functions.GetNamedPart;
import gnu.kawa.functions.IsEqual;
import gnu.kawa.reflect.Invoke;
import gnu.kawa.reflect.SlotGet;
import gnu.kawa.reflect.SlotSet;
import gnu.lists.Consumer;
import gnu.lists.FString;
import gnu.lists.LList;
import gnu.lists.Pair;
import gnu.lists.PairWithPosition;
import gnu.lists.VoidConsumer;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Symbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.math.IntNum;
import kawa.lang.Promise;
import kawa.lib.lists;
import kawa.lib.misc;
import kawa.lib.strings;
import kawa.standard.Scheme;
import kawa.standard.require;

/* compiled from: Alerts.yail */
public class Alerts extends Form implements Runnable {
    public static Alerts Alerts;
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol("Alerts").readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("getMessage").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("g$revisedAlerts").readResolve());
    static final PairWithPosition Lit100 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 360675), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 360669);
    static final PairWithPosition Lit101 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 360690), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 360684);
    static final SimpleSymbol Lit102 = ((SimpleSymbol) new SimpleSymbol("Button4$Click").readResolve());
    static final FString Lit103 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit104 = ((SimpleSymbol) new SimpleSymbol("Label3").readResolve());
    static final SimpleSymbol Lit105 = ((SimpleSymbol) new SimpleSymbol("FontBold").readResolve());
    static final SimpleSymbol Lit106 = ((SimpleSymbol) new SimpleSymbol("FontSize").readResolve());
    static final IntNum Lit107 = IntNum.make(19);
    static final SimpleSymbol Lit108 = ((SimpleSymbol) new SimpleSymbol("FontTypeface").readResolve());
    static final SimpleSymbol Lit109 = ((SimpleSymbol) new SimpleSymbol("TextAlignment").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("AccentColor").readResolve());
    static final IntNum Lit110 = IntNum.make(1);
    static final FString Lit111 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit112 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit113 = ((SimpleSymbol) new SimpleSymbol("Button2").readResolve());
    static final FString Lit114 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit115 = ((SimpleSymbol) new SimpleSymbol("Button2$Click").readResolve());
    static final FString Lit116 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit117 = ((SimpleSymbol) new SimpleSymbol("Label44").readResolve());
    static final FString Lit118 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit119 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit12;
    static final SimpleSymbol Lit120 = ((SimpleSymbol) new SimpleSymbol("Label5").readResolve());
    static final FString Lit121 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit122 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit123 = ((SimpleSymbol) new SimpleSymbol("Label1").readResolve());
    static final FString Lit124 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit125 = new FString("com.google.appinventor.components.runtime.ListView");
    static final IntNum Lit126;
    static final SimpleSymbol Lit127 = ((SimpleSymbol) new SimpleSymbol("FontSizeDetail").readResolve());
    static final IntNum Lit128 = IntNum.make(15);
    static final SimpleSymbol Lit129 = ((SimpleSymbol) new SimpleSymbol("FontTypefaceDetail").readResolve());
    static final SimpleSymbol Lit13;
    static final SimpleSymbol Lit130 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_LISTVIEW_LAYOUT).readResolve());
    static final SimpleSymbol Lit131 = ((SimpleSymbol) new SimpleSymbol("SelectionColor").readResolve());
    static final IntNum Lit132 = IntNum.make(16777215);
    static final SimpleSymbol Lit133 = ((SimpleSymbol) new SimpleSymbol("TextColor").readResolve());
    static final IntNum Lit134;
    static final SimpleSymbol Lit135 = ((SimpleSymbol) new SimpleSymbol("TextColorDetail").readResolve());
    static final IntNum Lit136;
    static final SimpleSymbol Lit137 = ((SimpleSymbol) new SimpleSymbol("TextSize").readResolve());
    static final IntNum Lit138 = IntNum.make(17);
    static final IntNum Lit139 = IntNum.make(-1090);
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("ActionBar").readResolve());
    static final FString Lit140 = new FString("com.google.appinventor.components.runtime.ListView");
    static final SimpleSymbol Lit141 = ((SimpleSymbol) new SimpleSymbol("Selection").readResolve());
    static final PairWithPosition Lit142 = PairWithPosition.make(Lit31, PairWithPosition.make(Lit13, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 663917), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 663911);
    static final PairWithPosition Lit143 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664045), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664041), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664036);
    static final SimpleSymbol Lit144 = ((SimpleSymbol) new SimpleSymbol("name").readResolve());
    static final SimpleSymbol Lit145 = ((SimpleSymbol) new SimpleSymbol("$alertInfo").readResolve());
    static final PairWithPosition Lit146 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664301), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664297), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664292);
    static final PairWithPosition Lit147 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664330);
    static final PairWithPosition Lit148 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664497), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664493), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664488);
    static final SimpleSymbol Lit149 = ((SimpleSymbol) new SimpleSymbol("count").readResolve());
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN).readResolve());
    static final PairWithPosition Lit150 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664913), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664909), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664904);
    static final PairWithPosition Lit151 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 664942);
    static final PairWithPosition Lit152 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665115), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665111), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665106);
    static final PairWithPosition Lit153 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665283), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665278);
    static final PairWithPosition Lit154 = PairWithPosition.make(Lit430, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665307);
    static final PairWithPosition Lit155 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit429, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665344), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665338);
    static final PairWithPosition Lit156 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665390), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665384);
    static final SimpleSymbol Lit157 = ((SimpleSymbol) new SimpleSymbol("exp2").readResolve());
    static final PairWithPosition Lit158 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665905), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665901), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665896);
    static final PairWithPosition Lit159 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 665934);
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("AlignHorizontal").readResolve());
    static final PairWithPosition Lit160 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666107), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666103), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666098);
    static final PairWithPosition Lit161 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666278), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666273);
    static final PairWithPosition Lit162 = PairWithPosition.make(Lit430, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666302);
    static final PairWithPosition Lit163 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit429, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666339), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666333);
    static final PairWithPosition Lit164 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666389), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666383);
    static final PairWithPosition Lit165;
    static final PairWithPosition Lit166 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666451), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666445);
    static final SimpleSymbol Lit167 = ((SimpleSymbol) new SimpleSymbol("svr").readResolve());
    static final PairWithPosition Lit168 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666772), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666768), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666763);
    static final PairWithPosition Lit169 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666801);
    static final IntNum Lit17 = IntNum.make(3);
    static final PairWithPosition Lit170 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666974), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666970), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666965);
    static final PairWithPosition Lit171 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667011), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667005);
    static final SimpleSymbol Lit172 = ((SimpleSymbol) new SimpleSymbol("desc").readResolve());
    static final PairWithPosition Lit173 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667348), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667344), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667339);
    static final PairWithPosition Lit174 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667377);
    static final PairWithPosition Lit175 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667572), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667568), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667563);
    static final PairWithPosition Lit176 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667741), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667736);
    static final PairWithPosition Lit177 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667846), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667841);
    static final PairWithPosition Lit178 = PairWithPosition.make(Lit430, PairWithPosition.make(Lit430, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667876), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667870);
    static final PairWithPosition Lit179 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit429, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667912), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 667906);
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("AppName").readResolve());
    static final SimpleSymbol Lit180 = ((SimpleSymbol) new SimpleSymbol("instr").readResolve());
    static final PairWithPosition Lit181 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668272), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668268), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668263);
    static final PairWithPosition Lit182 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668301);
    static final PairWithPosition Lit183 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668496), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668492), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668487);
    static final PairWithPosition Lit184 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668665), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668660);
    static final PairWithPosition Lit185 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668770), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668765);
    static final PairWithPosition Lit186 = PairWithPosition.make(Lit430, PairWithPosition.make(Lit430, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668800), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668794);
    static final PairWithPosition Lit187 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit429, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668836), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 668830);
    static final SimpleSymbol Lit188 = ((SimpleSymbol) new SimpleSymbol("ListView1$AfterPicking").readResolve());
    static final SimpleSymbol Lit189 = ((SimpleSymbol) new SimpleSymbol("AfterPicking").readResolve());
    static final SimpleSymbol Lit19;
    static final FString Lit190 = new FString("com.google.appinventor.components.runtime.VerticalScrollArrangement");
    static final IntNum Lit191;
    static final IntNum Lit192 = IntNum.make(-1090);
    static final FString Lit193 = new FString("com.google.appinventor.components.runtime.VerticalScrollArrangement");
    static final FString Lit194 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit195 = ((SimpleSymbol) new SimpleSymbol("Label15").readResolve());
    static final FString Lit196 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit197 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit198;
    static final FString Lit199 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("*the-null-value*").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("BackgroundColor").readResolve());
    static final FString Lit200 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit201 = ((SimpleSymbol) new SimpleSymbol("Label17").readResolve());
    static final FString Lit202 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit203 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit204 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement2").readResolve());
    static final IntNum Lit205 = IntNum.make(16777215);
    static final FString Lit206 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit207 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit208 = ((SimpleSymbol) new SimpleSymbol("Label7").readResolve());
    static final FString Lit209 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit21;
    static final FString Lit210 = new FString("com.google.appinventor.components.runtime.Label");
    static final IntNum Lit211 = IntNum.make(16);
    static final SimpleSymbol Lit212 = ((SimpleSymbol) new SimpleSymbol("HTMLFormat").readResolve());
    static final FString Lit213 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit214 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit215 = ((SimpleSymbol) new SimpleSymbol("Label21").readResolve());
    static final FString Lit216 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit217 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit218 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement6").readResolve());
    static final IntNum Lit219 = IntNum.make(16777215);
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("BackgroundImage").readResolve());
    static final FString Lit220 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit221 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit222 = ((SimpleSymbol) new SimpleSymbol("Label8").readResolve());
    static final FString Lit223 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit224 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit225 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit226 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit227 = ((SimpleSymbol) new SimpleSymbol("Label11").readResolve());
    static final FString Lit228 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit229 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("ScreenOrientation").readResolve());
    static final SimpleSymbol Lit230 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement3").readResolve());
    static final IntNum Lit231 = IntNum.make(16777215);
    static final FString Lit232 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit233 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit234 = ((SimpleSymbol) new SimpleSymbol("Label9").readResolve());
    static final FString Lit235 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit236 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit237 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit238 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit239 = ((SimpleSymbol) new SimpleSymbol("Label12").readResolve());
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("ShowListsAsJson").readResolve());
    static final FString Lit240 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit241 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit242 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement4").readResolve());
    static final IntNum Lit243 = IntNum.make(16777215);
    static final FString Lit244 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit245 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit246 = ((SimpleSymbol) new SimpleSymbol("Label13").readResolve());
    static final FString Lit247 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit248 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit249 = ((SimpleSymbol) new SimpleSymbol("Label18").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("Sizing").readResolve());
    static final FString Lit250 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit251 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit252 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit253 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit254 = ((SimpleSymbol) new SimpleSymbol("Label22").readResolve());
    static final FString Lit255 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit256 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit257 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement5").readResolve());
    static final IntNum Lit258 = IntNum.make(16777215);
    static final FString Lit259 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("Theme").readResolve());
    static final FString Lit260 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit261 = ((SimpleSymbol) new SimpleSymbol("Label19").readResolve());
    static final FString Lit262 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit263 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit264 = ((SimpleSymbol) new SimpleSymbol("Label20").readResolve());
    static final FString Lit265 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit266 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit267 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit268 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit269 = ((SimpleSymbol) new SimpleSymbol("Label23").readResolve());
    static final SimpleSymbol Lit27 = ((SimpleSymbol) new SimpleSymbol("Title").readResolve());
    static final FString Lit270 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit271 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final IntNum Lit272 = IntNum.make(-1090);
    static final FString Lit273 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final FString Lit274 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit275 = ((SimpleSymbol) new SimpleSymbol("Label25").readResolve());
    static final FString Lit276 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit277 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit278 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement7").readResolve());
    static final FString Lit279 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("TitleVisible").readResolve());
    static final FString Lit280 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit281 = ((SimpleSymbol) new SimpleSymbol("Label26").readResolve());
    static final FString Lit282 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit283 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final SimpleSymbol Lit284 = ((SimpleSymbol) new SimpleSymbol("VerticalArrangement2").readResolve());
    static final IntNum Lit285 = IntNum.make(-1060);
    static final FString Lit286 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final FString Lit287 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit288 = ((SimpleSymbol) new SimpleSymbol("Label28").readResolve());
    static final FString Lit289 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("Web1").readResolve());
    static final FString Lit290 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit291 = ((SimpleSymbol) new SimpleSymbol("Label29").readResolve());
    static final FString Lit292 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit293 = new FString("com.google.appinventor.components.runtime.TextBox");
    static final SimpleSymbol Lit294 = ((SimpleSymbol) new SimpleSymbol("Hint").readResolve());
    static final FString Lit295 = new FString("com.google.appinventor.components.runtime.TextBox");
    static final FString Lit296 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit297 = ((SimpleSymbol) new SimpleSymbol("Label27").readResolve());
    static final FString Lit298 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit299 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("g$request_headers").readResolve());
    static final SimpleSymbol Lit30 = ((SimpleSymbol) new SimpleSymbol("RequestHeaders").readResolve());
    static final SimpleSymbol Lit300 = ((SimpleSymbol) new SimpleSymbol("Label30").readResolve());
    static final FString Lit301 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit302 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit303 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement8").readResolve());
    static final FString Lit304 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit305 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit306 = ((SimpleSymbol) new SimpleSymbol("Label31").readResolve());
    static final FString Lit307 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit308 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final SimpleSymbol Lit309 = ((SimpleSymbol) new SimpleSymbol("VerticalArrangement3").readResolve());
    static final SimpleSymbol Lit31;
    static final IntNum Lit310 = IntNum.make(-1060);
    static final FString Lit311 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final FString Lit312 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit313 = ((SimpleSymbol) new SimpleSymbol("Label32").readResolve());
    static final FString Lit314 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit315 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit316 = ((SimpleSymbol) new SimpleSymbol("Label33").readResolve());
    static final FString Lit317 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit318 = new FString("com.google.appinventor.components.runtime.TextBox");
    static final FString Lit319 = new FString("com.google.appinventor.components.runtime.TextBox");
    static final SimpleSymbol Lit32 = ((SimpleSymbol) new SimpleSymbol(TinyDB.DEFAULT_NAMESPACE).readResolve());
    static final FString Lit320 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit321 = ((SimpleSymbol) new SimpleSymbol("Label34").readResolve());
    static final FString Lit322 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit323 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit324 = ((SimpleSymbol) new SimpleSymbol("Label35").readResolve());
    static final FString Lit325 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit326 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit327 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement9").readResolve());
    static final FString Lit328 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit329 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit33 = ((SimpleSymbol) new SimpleSymbol("StoreValue").readResolve());
    static final SimpleSymbol Lit330 = ((SimpleSymbol) new SimpleSymbol("Label37").readResolve());
    static final FString Lit331 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit332 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final SimpleSymbol Lit333 = ((SimpleSymbol) new SimpleSymbol("VerticalArrangement4").readResolve());
    static final IntNum Lit334 = IntNum.make(-1060);
    static final FString Lit335 = new FString("com.google.appinventor.components.runtime.VerticalArrangement");
    static final FString Lit336 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit337 = ((SimpleSymbol) new SimpleSymbol("Label38").readResolve());
    static final FString Lit338 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit339 = new FString("com.google.appinventor.components.runtime.Label");
    static final PairWithPosition Lit34 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 110842), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 110836);
    static final SimpleSymbol Lit340 = ((SimpleSymbol) new SimpleSymbol("Label39").readResolve());
    static final FString Lit341 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit342 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit343 = ((SimpleSymbol) new SimpleSymbol("Label40").readResolve());
    static final IntNum Lit344 = IntNum.make(-1005);
    static final FString Lit345 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit346 = new FString("com.google.appinventor.components.runtime.Switch");
    static final FString Lit347 = new FString("com.google.appinventor.components.runtime.Switch");
    static final FString Lit348 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit349 = ((SimpleSymbol) new SimpleSymbol("Label41").readResolve());
    static final SimpleSymbol Lit35 = ((SimpleSymbol) new SimpleSymbol("Switch1").readResolve());
    static final FString Lit350 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit351 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit352 = ((SimpleSymbol) new SimpleSymbol("Label36").readResolve());
    static final FString Lit353 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit354 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit355 = ((SimpleSymbol) new SimpleSymbol("Button3").readResolve());
    static final IntNum Lit356;
    static final FString Lit357 = new FString("com.google.appinventor.components.runtime.Button");
    static final PairWithPosition Lit358 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2670710), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2670704);
    static final PairWithPosition Lit359 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2670827), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2670821);
    static final SimpleSymbol Lit36 = ((SimpleSymbol) new SimpleSymbol("On").readResolve());
    static final PairWithPosition Lit360 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2670942), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2670936);
    static final SimpleSymbol Lit361 = ((SimpleSymbol) new SimpleSymbol("Button3$Click").readResolve());
    static final FString Lit362 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit363 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit364 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit365 = ((SimpleSymbol) new SimpleSymbol("Label2").readResolve());
    static final FString Lit366 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit367 = new FString("com.puravidaapps.TaifunTools");
    static final SimpleSymbol Lit368 = ((SimpleSymbol) new SimpleSymbol("TaifunTools1").readResolve());
    static final SimpleSymbol Lit369 = ((SimpleSymbol) new SimpleSymbol("NavigationBarColor").readResolve());
    static final SimpleSymbol Lit37 = ((SimpleSymbol) new SimpleSymbol("GetValue").readResolve());
    static final IntNum Lit370;
    static final SimpleSymbol Lit371 = ((SimpleSymbol) new SimpleSymbol("StatusBarColor").readResolve());
    static final IntNum Lit372;
    static final SimpleSymbol Lit373 = ((SimpleSymbol) new SimpleSymbol("SuppressWarnings").readResolve());
    static final FString Lit374 = new FString("com.puravidaapps.TaifunTools");
    static final FString Lit375 = new FString("com.google.appinventor.components.runtime.Web");
    static final SimpleSymbol Lit376 = ((SimpleSymbol) new SimpleSymbol("Timeout").readResolve());
    static final IntNum Lit377 = IntNum.make(6000);
    static final FString Lit378 = new FString("com.google.appinventor.components.runtime.Web");
    static final SimpleSymbol Lit379 = ((SimpleSymbol) new SimpleSymbol("JsonTextDecodeWithDictionaries").readResolve());
    static final PairWithPosition Lit38 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 110969), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 110963);
    static final SimpleSymbol Lit380 = ((SimpleSymbol) new SimpleSymbol("$responseContent").readResolve());
    static final PairWithPosition Lit381 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810008);
    static final SimpleSymbol Lit382 = ((SimpleSymbol) new SimpleSymbol("$county").readResolve());
    static final PairWithPosition Lit383 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810319);
    static final PairWithPosition Lit384 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810483), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810477);
    static final PairWithPosition Lit385 = PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810491);
    static final PairWithPosition Lit386 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810517), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810512);
    static final SimpleSymbol Lit387 = ((SimpleSymbol) new SimpleSymbol("$elements").readResolve());
    static final SimpleSymbol Lit388 = ((SimpleSymbol) new SimpleSymbol("CreateElement").readResolve());
    static final PairWithPosition Lit389 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810854), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810849);
    static final SimpleSymbol Lit39 = ((SimpleSymbol) new SimpleSymbol("TextBox2").readResolve());
    static final SimpleSymbol Lit390 = ((SimpleSymbol) new SimpleSymbol("$entireAlert").readResolve());
    static final PairWithPosition Lit391 = PairWithPosition.make(Lit31, PairWithPosition.make(Lit429, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810939), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810928), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2810922);
    static final PairWithPosition Lit392 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811139), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811134);
    static final PairWithPosition Lit393 = PairWithPosition.make(Lit31, PairWithPosition.make(Lit429, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811232), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811221), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811215);
    static final PairWithPosition Lit394 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811286), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811281), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811275);
    static final PairWithPosition Lit395 = PairWithPosition.make(Lit31, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811302), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811296);
    static final PairWithPosition Lit396 = PairWithPosition.make(Lit31, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811456), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811450);
    static final PairWithPosition Lit397 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811709), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811704);
    static final PairWithPosition Lit398 = PairWithPosition.make(Lit31, PairWithPosition.make(Lit429, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811790), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811779), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811773);
    static final PairWithPosition Lit399 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811900), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811895);
    static final PairWithPosition Lit4 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 32949), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 32944);
    static final SimpleSymbol Lit40 = ((SimpleSymbol) new SimpleSymbol("Text").readResolve());
    static final PairWithPosition Lit400 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit31, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811929), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2811923);
    static final SimpleSymbol Lit401 = ((SimpleSymbol) new SimpleSymbol("proc").readResolve());
    static final PairWithPosition Lit402 = PairWithPosition.make(Lit431, PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812149), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812145), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812140);
    static final PairWithPosition Lit403;
    static final PairWithPosition Lit404 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812502), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812496);
    static final PairWithPosition Lit405 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812610), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812604);
    static final PairWithPosition Lit406;
    static final SimpleSymbol Lit407 = ((SimpleSymbol) new SimpleSymbol("Elements").readResolve());
    static final SimpleSymbol Lit408 = ((SimpleSymbol) new SimpleSymbol("Web1$GotText").readResolve());
    static final SimpleSymbol Lit409 = ((SimpleSymbol) new SimpleSymbol("GotText").readResolve());
    static final PairWithPosition Lit41 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111108), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111102);
    static final FString Lit410 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit411 = new FString("com.google.appinventor.components.runtime.TinyDB");
    static final FString Lit412 = new FString("com.sunny.CornerRadius.CornerRadius");
    static final FString Lit413 = new FString("com.sunny.CornerRadius.CornerRadius");
    static final SimpleSymbol Lit414 = ((SimpleSymbol) new SimpleSymbol("get-simple-name").readResolve());
    static final SimpleSymbol Lit415 = ((SimpleSymbol) new SimpleSymbol("android-log-form").readResolve());
    static final SimpleSymbol Lit416 = ((SimpleSymbol) new SimpleSymbol("add-to-form-environment").readResolve());
    static final SimpleSymbol Lit417 = ((SimpleSymbol) new SimpleSymbol("lookup-in-form-environment").readResolve());
    static final SimpleSymbol Lit418 = ((SimpleSymbol) new SimpleSymbol("is-bound-in-form-environment").readResolve());
    static final SimpleSymbol Lit419 = ((SimpleSymbol) new SimpleSymbol("add-to-global-var-environment").readResolve());
    static final SimpleSymbol Lit42 = ((SimpleSymbol) new SimpleSymbol("TextBox1").readResolve());
    static final SimpleSymbol Lit420 = ((SimpleSymbol) new SimpleSymbol("add-to-events").readResolve());
    static final SimpleSymbol Lit421 = ((SimpleSymbol) new SimpleSymbol("add-to-components").readResolve());
    static final SimpleSymbol Lit422 = ((SimpleSymbol) new SimpleSymbol("add-to-global-vars").readResolve());
    static final SimpleSymbol Lit423 = ((SimpleSymbol) new SimpleSymbol("add-to-form-do-after-creation").readResolve());
    static final SimpleSymbol Lit424 = ((SimpleSymbol) new SimpleSymbol("send-error").readResolve());
    static final SimpleSymbol Lit425 = ((SimpleSymbol) new SimpleSymbol("dispatchEvent").readResolve());
    static final SimpleSymbol Lit426 = ((SimpleSymbol) new SimpleSymbol("dispatchGenericEvent").readResolve());
    static final SimpleSymbol Lit427 = ((SimpleSymbol) new SimpleSymbol("lookup-handler").readResolve());
    static final SimpleSymbol Lit428 = ((SimpleSymbol) new SimpleSymbol("any").readResolve());
    static final SimpleSymbol Lit429 = ((SimpleSymbol) new SimpleSymbol("dictionary").readResolve());
    static final PairWithPosition Lit43 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111245), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111239);
    static final SimpleSymbol Lit430 = ((SimpleSymbol) new SimpleSymbol("pair").readResolve());
    static final SimpleSymbol Lit431 = ((SimpleSymbol) new SimpleSymbol("key").readResolve());
    static final SimpleSymbol Lit432 = ((SimpleSymbol) new SimpleSymbol("component").readResolve());
    static final SimpleSymbol Lit44 = ((SimpleSymbol) new SimpleSymbol("CornerRadius1").readResolve());
    static final SimpleSymbol Lit45 = ((SimpleSymbol) new SimpleSymbol("SetCornerRadius").readResolve());
    static final SimpleSymbol Lit46 = ((SimpleSymbol) new SimpleSymbol("VerticalScrollArrangement1").readResolve());
    static final IntNum Lit47 = IntNum.make(-1);
    static final IntNum Lit48 = IntNum.make(10);
    static final PairWithPosition Lit49 = PairWithPosition.make(Lit432, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111436), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111429), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111422), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111415), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111408), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111397);
    static final PairWithPosition Lit5 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33066), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33061);
    static final SimpleSymbol Lit50 = ((SimpleSymbol) new SimpleSymbol("VerticalArrangement1").readResolve());
    static final PairWithPosition Lit51 = PairWithPosition.make(Lit432, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, PairWithPosition.make(Lit13, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111617), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111610), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111603), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111596), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111589), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111578);
    static final SimpleSymbol Lit52 = ((SimpleSymbol) new SimpleSymbol("Url").readResolve());
    static final PairWithPosition Lit53 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111847), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111841);
    static final PairWithPosition Lit54 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111862), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 111856);
    static final SimpleSymbol Lit55 = ((SimpleSymbol) new SimpleSymbol("Label6").readResolve());
    static final PairWithPosition Lit56 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112084), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112078);
    static final PairWithPosition Lit57 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112196), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112190);
    static final PairWithPosition Lit58 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112230), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112225), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112220), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112215), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 112209);
    static final SimpleSymbol Lit59 = ((SimpleSymbol) new SimpleSymbol("Get").readResolve());
    static final PairWithPosition Lit6 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33095), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33090);
    static final SimpleSymbol Lit60 = ((SimpleSymbol) new SimpleSymbol("Alerts$Initialize").readResolve());
    static final SimpleSymbol Lit61 = ((SimpleSymbol) new SimpleSymbol("Initialize").readResolve());
    static final SimpleSymbol Lit62 = ((SimpleSymbol) new SimpleSymbol("Alerts$BackPressed").readResolve());
    static final SimpleSymbol Lit63 = ((SimpleSymbol) new SimpleSymbol("BackPressed").readResolve());
    static final FString Lit64 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit65 = ((SimpleSymbol) new SimpleSymbol("Label24").readResolve());
    static final FString Lit66 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit67 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final SimpleSymbol Lit68 = ((SimpleSymbol) new SimpleSymbol("HorizontalArrangement1").readResolve());
    static final SimpleSymbol Lit69 = ((SimpleSymbol) new SimpleSymbol("AlignVertical").readResolve());
    static final PairWithPosition Lit7 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 32949), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 32944);
    static final IntNum Lit70 = IntNum.make(2);
    static final IntNum Lit71 = IntNum.make(16777215);
    static final SimpleSymbol Lit72 = ((SimpleSymbol) new SimpleSymbol("Width").readResolve());
    static final IntNum Lit73 = IntNum.make(-2);
    static final FString Lit74 = new FString("com.google.appinventor.components.runtime.HorizontalArrangement");
    static final FString Lit75 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit76 = ((SimpleSymbol) new SimpleSymbol("Label4").readResolve());
    static final FString Lit77 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit78 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit79 = ((SimpleSymbol) new SimpleSymbol("Label43").readResolve());
    static final PairWithPosition Lit8 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33066), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33061);
    static final FString Lit80 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit81 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit82 = ((SimpleSymbol) new SimpleSymbol("Button1").readResolve());
    static final SimpleSymbol Lit83 = ((SimpleSymbol) new SimpleSymbol("Height").readResolve());
    static final IntNum Lit84 = IntNum.make(40);
    static final SimpleSymbol Lit85 = ((SimpleSymbol) new SimpleSymbol(Component.LISTVIEW_KEY_IMAGE).readResolve());
    static final FString Lit86 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit87 = ((SimpleSymbol) new SimpleSymbol("Visible").readResolve());
    static final SimpleSymbol Lit88 = ((SimpleSymbol) new SimpleSymbol("ListView1").readResolve());
    static final PairWithPosition Lit89 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287162), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287156);
    static final PairWithPosition Lit9 = PairWithPosition.make(Lit428, PairWithPosition.make(Lit428, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33095), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 33090);
    static final PairWithPosition Lit90 = PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287292), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287287), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287282), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287277), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287271);
    static final PairWithPosition Lit91 = PairWithPosition.make(Lit31, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 287548);
    static final SimpleSymbol Lit92 = ((SimpleSymbol) new SimpleSymbol("Button1$Click").readResolve());
    static final SimpleSymbol Lit93 = ((SimpleSymbol) new SimpleSymbol("Click").readResolve());
    static final FString Lit94 = new FString("com.google.appinventor.components.runtime.Label");
    static final SimpleSymbol Lit95 = ((SimpleSymbol) new SimpleSymbol("Label42").readResolve());
    static final FString Lit96 = new FString("com.google.appinventor.components.runtime.Label");
    static final FString Lit97 = new FString("com.google.appinventor.components.runtime.Button");
    static final SimpleSymbol Lit98 = ((SimpleSymbol) new SimpleSymbol("Button4").readResolve());
    static final FString Lit99 = new FString("com.google.appinventor.components.runtime.Button");
    static final ModuleMethod lambda$Fn1 = null;
    static final ModuleMethod lambda$Fn10 = null;
    static final ModuleMethod lambda$Fn11 = null;
    static final ModuleMethod lambda$Fn12 = null;
    static final ModuleMethod lambda$Fn13 = null;
    static final ModuleMethod lambda$Fn14 = null;
    static final ModuleMethod lambda$Fn15 = null;
    static final ModuleMethod lambda$Fn16 = null;
    static final ModuleMethod lambda$Fn17 = null;
    static final ModuleMethod lambda$Fn18 = null;
    static final ModuleMethod lambda$Fn19 = null;
    static final ModuleMethod lambda$Fn2 = null;
    static final ModuleMethod lambda$Fn20 = null;
    static final ModuleMethod lambda$Fn21 = null;
    static final ModuleMethod lambda$Fn22 = null;
    static final ModuleMethod lambda$Fn23 = null;
    static final ModuleMethod lambda$Fn24 = null;
    static final ModuleMethod lambda$Fn25 = null;
    static final ModuleMethod lambda$Fn26 = null;
    static final ModuleMethod lambda$Fn27 = null;
    static final ModuleMethod lambda$Fn28 = null;
    static final ModuleMethod lambda$Fn29 = null;
    static final ModuleMethod lambda$Fn3 = null;
    static final ModuleMethod lambda$Fn30 = null;
    static final ModuleMethod lambda$Fn31 = null;
    static final ModuleMethod lambda$Fn32 = null;
    static final ModuleMethod lambda$Fn33 = null;
    static final ModuleMethod lambda$Fn34 = null;
    static final ModuleMethod lambda$Fn35 = null;
    static final ModuleMethod lambda$Fn36 = null;
    static final ModuleMethod lambda$Fn37 = null;
    static final ModuleMethod lambda$Fn38 = null;
    static final ModuleMethod lambda$Fn39 = null;
    static final ModuleMethod lambda$Fn4 = null;
    static final ModuleMethod lambda$Fn40 = null;
    static final ModuleMethod lambda$Fn41 = null;
    static final ModuleMethod lambda$Fn42 = null;
    static final ModuleMethod lambda$Fn43 = null;
    static final ModuleMethod lambda$Fn44 = null;
    static final ModuleMethod lambda$Fn45 = null;
    static final ModuleMethod lambda$Fn46 = null;
    static final ModuleMethod lambda$Fn47 = null;
    static final ModuleMethod lambda$Fn48 = null;
    static final ModuleMethod lambda$Fn49 = null;
    static final ModuleMethod lambda$Fn5 = null;
    static final ModuleMethod lambda$Fn50 = null;
    static final ModuleMethod lambda$Fn51 = null;
    static final ModuleMethod lambda$Fn52 = null;
    static final ModuleMethod lambda$Fn53 = null;
    static final ModuleMethod lambda$Fn54 = null;
    static final ModuleMethod lambda$Fn55 = null;
    static final ModuleMethod lambda$Fn56 = null;
    static final ModuleMethod lambda$Fn57 = null;
    static final ModuleMethod lambda$Fn58 = null;
    static final ModuleMethod lambda$Fn59 = null;
    static final ModuleMethod lambda$Fn6 = null;
    static final ModuleMethod lambda$Fn60 = null;
    static final ModuleMethod lambda$Fn61 = null;
    static final ModuleMethod lambda$Fn62 = null;
    static final ModuleMethod lambda$Fn63 = null;
    static final ModuleMethod lambda$Fn64 = null;
    static final ModuleMethod lambda$Fn65 = null;
    static final ModuleMethod lambda$Fn66 = null;
    static final ModuleMethod lambda$Fn67 = null;
    static final ModuleMethod lambda$Fn68 = null;
    static final ModuleMethod lambda$Fn69 = null;
    static final ModuleMethod lambda$Fn7 = null;
    static final ModuleMethod lambda$Fn70 = null;
    static final ModuleMethod lambda$Fn71 = null;
    static final ModuleMethod lambda$Fn72 = null;
    static final ModuleMethod lambda$Fn73 = null;
    static final ModuleMethod lambda$Fn74 = null;
    static final ModuleMethod lambda$Fn75 = null;
    static final ModuleMethod lambda$Fn76 = null;
    static final ModuleMethod lambda$Fn77 = null;
    static final ModuleMethod lambda$Fn78 = null;
    static final ModuleMethod lambda$Fn79 = null;
    static final ModuleMethod lambda$Fn8 = null;
    static final ModuleMethod lambda$Fn80 = null;
    static final ModuleMethod lambda$Fn81 = null;
    static final ModuleMethod lambda$Fn82 = null;
    static final ModuleMethod lambda$Fn83 = null;
    static final ModuleMethod lambda$Fn84 = null;
    static final ModuleMethod lambda$Fn85 = null;
    static final ModuleMethod lambda$Fn86 = null;
    static final ModuleMethod lambda$Fn87 = null;
    static final ModuleMethod lambda$Fn88 = null;
    static final ModuleMethod lambda$Fn9 = null;
    public Boolean $Stdebug$Mnform$St;
    public final ModuleMethod $define;
    public final ModuleMethod Alerts$BackPressed;
    public final ModuleMethod Alerts$Initialize;
    public Button Button1;
    public final ModuleMethod Button1$Click;
    public Button Button2;
    public final ModuleMethod Button2$Click;
    public Button Button3;
    public final ModuleMethod Button3$Click;
    public Button Button4;
    public final ModuleMethod Button4$Click;
    public CornerRadius CornerRadius1;
    public HorizontalArrangement HorizontalArrangement1;
    public HorizontalArrangement HorizontalArrangement2;
    public HorizontalArrangement HorizontalArrangement3;
    public HorizontalArrangement HorizontalArrangement4;
    public HorizontalArrangement HorizontalArrangement5;
    public HorizontalArrangement HorizontalArrangement6;
    public HorizontalArrangement HorizontalArrangement7;
    public HorizontalArrangement HorizontalArrangement8;
    public HorizontalArrangement HorizontalArrangement9;
    public Label Label1;
    public Label Label11;
    public Label Label12;
    public Label Label13;
    public Label Label15;
    public Label Label17;
    public Label Label18;
    public Label Label19;
    public Label Label2;
    public Label Label20;
    public Label Label21;
    public Label Label22;
    public Label Label23;
    public Label Label24;
    public Label Label25;
    public Label Label26;
    public Label Label27;
    public Label Label28;
    public Label Label29;
    public Label Label3;
    public Label Label30;
    public Label Label31;
    public Label Label32;
    public Label Label33;
    public Label Label34;
    public Label Label35;
    public Label Label36;
    public Label Label37;
    public Label Label38;
    public Label Label39;
    public Label Label4;
    public Label Label40;
    public Label Label41;
    public Label Label42;
    public Label Label43;
    public Label Label44;
    public Label Label5;
    public Label Label6;
    public Label Label7;
    public Label Label8;
    public Label Label9;
    public ListView ListView1;
    public final ModuleMethod ListView1$AfterPicking;
    public Switch Switch1;
    public TaifunTools TaifunTools1;
    public TextBox TextBox1;
    public TextBox TextBox2;
    public TinyDB TinyDB1;
    public VerticalArrangement VerticalArrangement1;
    public VerticalArrangement VerticalArrangement2;
    public VerticalArrangement VerticalArrangement3;
    public VerticalArrangement VerticalArrangement4;
    public VerticalScrollArrangement VerticalScrollArrangement1;
    public Web Web1;
    public final ModuleMethod Web1$GotText;
    public final ModuleMethod add$Mnto$Mncomponents;
    public final ModuleMethod add$Mnto$Mnevents;
    public final ModuleMethod add$Mnto$Mnform$Mndo$Mnafter$Mncreation;
    public final ModuleMethod add$Mnto$Mnform$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvar$Mnenvironment;
    public final ModuleMethod add$Mnto$Mnglobal$Mnvars;
    public final ModuleMethod android$Mnlog$Mnform;
    public LList components$Mnto$Mncreate;
    public Label count;
    public Label desc;
    public final ModuleMethod dispatchEvent;
    public final ModuleMethod dispatchGenericEvent;
    public LList events$Mnto$Mnregister;
    public Label exp2;
    public LList form$Mndo$Mnafter$Mncreation;
    public Environment form$Mnenvironment;
    public Symbol form$Mnname$Mnsymbol;
    public final ModuleMethod get$Mnsimple$Mnname;
    public Environment global$Mnvar$Mnenvironment;
    public LList global$Mnvars$Mnto$Mncreate;
    public Label instr;
    public final ModuleMethod is$Mnbound$Mnin$Mnform$Mnenvironment;
    public final ModuleMethod lookup$Mnhandler;
    public final ModuleMethod lookup$Mnin$Mnform$Mnenvironment;
    public Label name;
    public final ModuleMethod onCreate;
    public final ModuleMethod process$Mnexception;
    public final ModuleMethod send$Mnerror;
    public Label svr;

    static {
        SimpleSymbol simpleSymbol = (SimpleSymbol) new SimpleSymbol(PropertyTypeConstants.PROPERTY_TYPE_TEXT).readResolve();
        Lit19 = simpleSymbol;
        Lit406 = PairWithPosition.make(simpleSymbol, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, PairWithPosition.make(Lit19, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812644), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812639), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812634), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812629), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812623);
        SimpleSymbol simpleSymbol2 = (SimpleSymbol) new SimpleSymbol("list").readResolve();
        Lit31 = simpleSymbol2;
        Lit403 = PairWithPosition.make(simpleSymbol2, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 2812267);
        int[] iArr = new int[2];
        iArr[0] = -16777216;
        Lit372 = IntNum.make(iArr);
        int[] iArr2 = new int[2];
        iArr2[0] = -16777216;
        Lit370 = IntNum.make(iArr2);
        int[] iArr3 = new int[2];
        iArr3[0] = -3355444;
        Lit356 = IntNum.make(iArr3);
        int[] iArr4 = new int[2];
        iArr4[0] = -65536;
        Lit198 = IntNum.make(iArr4);
        int[] iArr5 = new int[2];
        iArr5[0] = -1;
        Lit191 = IntNum.make(iArr5);
        SimpleSymbol simpleSymbol3 = Lit31;
        SimpleSymbol simpleSymbol4 = (SimpleSymbol) new SimpleSymbol("number").readResolve();
        Lit13 = simpleSymbol4;
        Lit165 = PairWithPosition.make(simpleSymbol3, PairWithPosition.make(simpleSymbol4, LList.Empty, "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666414), "/tmp/1686523513501_0.949150536456459-0/youngandroidproject/../src/appinventor/ai_anon9222030345824/Weather/Alerts.yail", 666408);
        int[] iArr6 = new int[2];
        iArr6[0] = -7829368;
        Lit136 = IntNum.make(iArr6);
        int[] iArr7 = new int[2];
        iArr7[0] = -65536;
        Lit134 = IntNum.make(iArr7);
        int[] iArr8 = new int[2];
        iArr8[0] = -1;
        Lit126 = IntNum.make(iArr8);
        int[] iArr9 = new int[2];
        iArr9[0] = -16776961;
        Lit21 = IntNum.make(iArr9);
        int[] iArr10 = new int[2];
        iArr10[0] = -16776961;
        Lit12 = IntNum.make(iArr10);
    }

    public Alerts() {
        ModuleInfo.register(this);
        frame frame2 = new frame();
        frame2.$main = this;
        this.get$Mnsimple$Mnname = new ModuleMethod(frame2, 3, Lit414, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.onCreate = new ModuleMethod(frame2, 4, "onCreate", FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.android$Mnlog$Mnform = new ModuleMethod(frame2, 5, Lit415, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.add$Mnto$Mnform$Mnenvironment = new ModuleMethod(frame2, 6, Lit416, 8194);
        this.lookup$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame2, 7, Lit417, 8193);
        this.is$Mnbound$Mnin$Mnform$Mnenvironment = new ModuleMethod(frame2, 9, Lit418, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.add$Mnto$Mnglobal$Mnvar$Mnenvironment = new ModuleMethod(frame2, 10, Lit419, 8194);
        this.add$Mnto$Mnevents = new ModuleMethod(frame2, 11, Lit420, 8194);
        this.add$Mnto$Mncomponents = new ModuleMethod(frame2, 12, Lit421, 16388);
        this.add$Mnto$Mnglobal$Mnvars = new ModuleMethod(frame2, 13, Lit422, 8194);
        this.add$Mnto$Mnform$Mndo$Mnafter$Mncreation = new ModuleMethod(frame2, 14, Lit423, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.send$Mnerror = new ModuleMethod(frame2, 15, Lit424, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.process$Mnexception = new ModuleMethod(frame2, 16, "process-exception", FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        this.dispatchEvent = new ModuleMethod(frame2, 17, Lit425, 16388);
        this.dispatchGenericEvent = new ModuleMethod(frame2, 18, Lit426, 16388);
        this.lookup$Mnhandler = new ModuleMethod(frame2, 19, Lit427, 8194);
        ModuleMethod moduleMethod = new ModuleMethod(frame2, 20, (Object) null, 0);
        moduleMethod.setProperty("source-location", "/tmp/runtime8324196797772320115.scm:634");
        lambda$Fn1 = moduleMethod;
        this.$define = new ModuleMethod(frame2, 21, "$define", 0);
        lambda$Fn2 = new ModuleMethod(frame2, 22, (Object) null, 0);
        lambda$Fn3 = new ModuleMethod(frame2, 23, (Object) null, 0);
        lambda$Fn4 = new ModuleMethod(frame2, 24, (Object) null, 0);
        this.Alerts$Initialize = new ModuleMethod(frame2, 25, Lit60, 0);
        this.Alerts$BackPressed = new ModuleMethod(frame2, 26, Lit62, 0);
        lambda$Fn5 = new ModuleMethod(frame2, 27, (Object) null, 0);
        lambda$Fn6 = new ModuleMethod(frame2, 28, (Object) null, 0);
        lambda$Fn7 = new ModuleMethod(frame2, 29, (Object) null, 0);
        lambda$Fn8 = new ModuleMethod(frame2, 30, (Object) null, 0);
        lambda$Fn9 = new ModuleMethod(frame2, 31, (Object) null, 0);
        lambda$Fn10 = new ModuleMethod(frame2, 32, (Object) null, 0);
        this.Button1$Click = new ModuleMethod(frame2, 33, Lit92, 0);
        lambda$Fn11 = new ModuleMethod(frame2, 34, (Object) null, 0);
        lambda$Fn12 = new ModuleMethod(frame2, 35, (Object) null, 0);
        this.Button4$Click = new ModuleMethod(frame2, 36, Lit102, 0);
        lambda$Fn13 = new ModuleMethod(frame2, 37, (Object) null, 0);
        lambda$Fn14 = new ModuleMethod(frame2, 38, (Object) null, 0);
        lambda$Fn15 = new ModuleMethod(frame2, 39, (Object) null, 0);
        lambda$Fn16 = new ModuleMethod(frame2, 40, (Object) null, 0);
        this.Button2$Click = new ModuleMethod(frame2, 41, Lit115, 0);
        lambda$Fn17 = new ModuleMethod(frame2, 42, (Object) null, 0);
        lambda$Fn18 = new ModuleMethod(frame2, 43, (Object) null, 0);
        this.ListView1$AfterPicking = new ModuleMethod(frame2, 44, Lit188, 0);
        lambda$Fn19 = new ModuleMethod(frame2, 45, (Object) null, 0);
        lambda$Fn20 = new ModuleMethod(frame2, 46, (Object) null, 0);
        lambda$Fn21 = new ModuleMethod(frame2, 47, (Object) null, 0);
        lambda$Fn22 = new ModuleMethod(frame2, 48, (Object) null, 0);
        lambda$Fn23 = new ModuleMethod(frame2, 49, (Object) null, 0);
        lambda$Fn24 = new ModuleMethod(frame2, 50, (Object) null, 0);
        lambda$Fn25 = new ModuleMethod(frame2, 51, (Object) null, 0);
        lambda$Fn26 = new ModuleMethod(frame2, 52, (Object) null, 0);
        lambda$Fn27 = new ModuleMethod(frame2, 53, (Object) null, 0);
        lambda$Fn28 = new ModuleMethod(frame2, 54, (Object) null, 0);
        lambda$Fn29 = new ModuleMethod(frame2, 55, (Object) null, 0);
        lambda$Fn30 = new ModuleMethod(frame2, 56, (Object) null, 0);
        lambda$Fn31 = new ModuleMethod(frame2, 57, (Object) null, 0);
        lambda$Fn32 = new ModuleMethod(frame2, 58, (Object) null, 0);
        lambda$Fn33 = new ModuleMethod(frame2, 59, (Object) null, 0);
        lambda$Fn34 = new ModuleMethod(frame2, 60, (Object) null, 0);
        lambda$Fn35 = new ModuleMethod(frame2, 61, (Object) null, 0);
        lambda$Fn36 = new ModuleMethod(frame2, 62, (Object) null, 0);
        lambda$Fn37 = new ModuleMethod(frame2, 63, (Object) null, 0);
        lambda$Fn38 = new ModuleMethod(frame2, 64, (Object) null, 0);
        lambda$Fn39 = new ModuleMethod(frame2, 65, (Object) null, 0);
        lambda$Fn40 = new ModuleMethod(frame2, 66, (Object) null, 0);
        lambda$Fn41 = new ModuleMethod(frame2, 67, (Object) null, 0);
        lambda$Fn42 = new ModuleMethod(frame2, 68, (Object) null, 0);
        lambda$Fn43 = new ModuleMethod(frame2, 69, (Object) null, 0);
        lambda$Fn44 = new ModuleMethod(frame2, 70, (Object) null, 0);
        lambda$Fn45 = new ModuleMethod(frame2, 71, (Object) null, 0);
        lambda$Fn46 = new ModuleMethod(frame2, 72, (Object) null, 0);
        lambda$Fn47 = new ModuleMethod(frame2, 73, (Object) null, 0);
        lambda$Fn48 = new ModuleMethod(frame2, 74, (Object) null, 0);
        lambda$Fn49 = new ModuleMethod(frame2, 75, (Object) null, 0);
        lambda$Fn50 = new ModuleMethod(frame2, 76, (Object) null, 0);
        lambda$Fn51 = new ModuleMethod(frame2, 77, (Object) null, 0);
        lambda$Fn52 = new ModuleMethod(frame2, 78, (Object) null, 0);
        lambda$Fn53 = new ModuleMethod(frame2, 79, (Object) null, 0);
        lambda$Fn54 = new ModuleMethod(frame2, 80, (Object) null, 0);
        lambda$Fn55 = new ModuleMethod(frame2, 81, (Object) null, 0);
        lambda$Fn56 = new ModuleMethod(frame2, 82, (Object) null, 0);
        lambda$Fn57 = new ModuleMethod(frame2, 83, (Object) null, 0);
        lambda$Fn58 = new ModuleMethod(frame2, 84, (Object) null, 0);
        lambda$Fn59 = new ModuleMethod(frame2, 85, (Object) null, 0);
        lambda$Fn60 = new ModuleMethod(frame2, 86, (Object) null, 0);
        lambda$Fn61 = new ModuleMethod(frame2, 87, (Object) null, 0);
        lambda$Fn62 = new ModuleMethod(frame2, 88, (Object) null, 0);
        lambda$Fn63 = new ModuleMethod(frame2, 89, (Object) null, 0);
        lambda$Fn64 = new ModuleMethod(frame2, 90, (Object) null, 0);
        lambda$Fn65 = new ModuleMethod(frame2, 91, (Object) null, 0);
        lambda$Fn66 = new ModuleMethod(frame2, 92, (Object) null, 0);
        lambda$Fn67 = new ModuleMethod(frame2, 93, (Object) null, 0);
        lambda$Fn68 = new ModuleMethod(frame2, 94, (Object) null, 0);
        lambda$Fn69 = new ModuleMethod(frame2, 95, (Object) null, 0);
        lambda$Fn70 = new ModuleMethod(frame2, 96, (Object) null, 0);
        lambda$Fn71 = new ModuleMethod(frame2, 97, (Object) null, 0);
        lambda$Fn72 = new ModuleMethod(frame2, 98, (Object) null, 0);
        lambda$Fn73 = new ModuleMethod(frame2, 99, (Object) null, 0);
        lambda$Fn74 = new ModuleMethod(frame2, 100, (Object) null, 0);
        lambda$Fn75 = new ModuleMethod(frame2, 101, (Object) null, 0);
        lambda$Fn76 = new ModuleMethod(frame2, 102, (Object) null, 0);
        lambda$Fn77 = new ModuleMethod(frame2, 103, (Object) null, 0);
        lambda$Fn78 = new ModuleMethod(frame2, 104, (Object) null, 0);
        lambda$Fn79 = new ModuleMethod(frame2, 105, (Object) null, 0);
        lambda$Fn80 = new ModuleMethod(frame2, 106, (Object) null, 0);
        lambda$Fn81 = new ModuleMethod(frame2, 107, (Object) null, 0);
        lambda$Fn82 = new ModuleMethod(frame2, 108, (Object) null, 0);
        this.Button3$Click = new ModuleMethod(frame2, 109, Lit361, 0);
        lambda$Fn83 = new ModuleMethod(frame2, 110, (Object) null, 0);
        lambda$Fn84 = new ModuleMethod(frame2, 111, (Object) null, 0);
        lambda$Fn85 = new ModuleMethod(frame2, 112, (Object) null, 0);
        lambda$Fn86 = new ModuleMethod(frame2, 113, (Object) null, 0);
        lambda$Fn87 = new ModuleMethod(frame2, 114, (Object) null, 0);
        lambda$Fn88 = new ModuleMethod(frame2, 115, (Object) null, 0);
        this.Web1$GotText = new ModuleMethod(frame2, 116, Lit408, 16388);
    }

    public Object lookupInFormEnvironment(Symbol symbol) {
        return lookupInFormEnvironment(symbol, Boolean.FALSE);
    }

    public void run() {
        CallContext instance = CallContext.getInstance();
        Consumer consumer = instance.consumer;
        instance.consumer = VoidConsumer.instance;
        try {
            run(instance);
            th = null;
        } catch (Throwable th) {
            th = th;
        }
        ModuleBody.runCleanup(instance, th, consumer);
    }

    public final void run(CallContext $ctx) {
        String obj;
        Consumer $result = $ctx.consumer;
        Object find = require.find("com.google.youngandroid.runtime");
        try {
            ((Runnable) find).run();
            this.$Stdebug$Mnform$St = Boolean.FALSE;
            this.form$Mnenvironment = Environment.make(misc.symbol$To$String(Lit0));
            FString stringAppend = strings.stringAppend(misc.symbol$To$String(Lit0), "-global-vars");
            if (stringAppend == null) {
                obj = null;
            } else {
                obj = stringAppend.toString();
            }
            this.global$Mnvar$Mnenvironment = Environment.make(obj);
            Alerts = null;
            this.form$Mnname$Mnsymbol = Lit0;
            this.events$Mnto$Mnregister = LList.Empty;
            this.components$Mnto$Mncreate = LList.Empty;
            this.global$Mnvars$Mnto$Mncreate = LList.Empty;
            this.form$Mndo$Mnafter$Mncreation = LList.Empty;
            Object find2 = require.find("com.google.youngandroid.runtime");
            try {
                ((Runnable) find2).run();
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit3, runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2(runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("User-Agent", "(busybird15@mail.com)"), Lit4, "make a list"), runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("Accept", "application/geo+json"), Lit5, "make a list")), Lit6, "make a list")), $result);
                } else {
                    addToGlobalVars(Lit3, lambda$Fn2);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addGlobalVarToCurrentFormEnvironment(Lit10, runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.Empty, LList.Empty, "make a list")), $result);
                } else {
                    addToGlobalVars(Lit10, lambda$Fn3);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit11, Lit12, Lit13);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit14, Boolean.TRUE, Lit15);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit16, Lit17, Lit13);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit18, "Weather", Lit19);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit20, Lit21, Lit13);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit22, "bitmap.png", Lit19);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit23, "portrait", Lit19);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit24, Boolean.TRUE, Lit15);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit25, "Responsive", Lit19);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit26, "AppTheme.Light.DarkActionBar", Lit19);
                    runtime.setAndCoerceProperty$Ex(Lit0, Lit27, "Alerts", Lit19);
                    Values.writeValues(runtime.setAndCoerceProperty$Ex(Lit0, Lit28, Boolean.FALSE, Lit15), $result);
                } else {
                    addToFormDoAfterCreation(new Promise(lambda$Fn4));
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit60, this.Alerts$Initialize);
                } else {
                    addToFormEnvironment(Lit60, this.Alerts$Initialize);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Alerts", "Initialize");
                } else {
                    addToEvents(Lit0, Lit61);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit62, this.Alerts$BackPressed);
                } else {
                    addToFormEnvironment(Lit62, this.Alerts$BackPressed);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Alerts", "BackPressed");
                } else {
                    addToEvents(Lit0, Lit63);
                }
                this.Label24 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit64, Lit65, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit66, Lit65, Boolean.FALSE);
                }
                this.HorizontalArrangement1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit67, Lit68, lambda$Fn5), $result);
                } else {
                    addToComponents(Lit0, Lit74, Lit68, lambda$Fn6);
                }
                this.Label4 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit75, Lit76, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit68, Lit77, Lit76, Boolean.FALSE);
                }
                this.Label43 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit78, Lit79, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit68, Lit80, Lit79, Boolean.FALSE);
                }
                this.Button1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit81, Lit82, lambda$Fn7), $result);
                } else {
                    addToComponents(Lit68, Lit86, Lit82, lambda$Fn8);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit92, this.Button1$Click);
                } else {
                    addToFormEnvironment(Lit92, this.Button1$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Button1", "Click");
                } else {
                    addToEvents(Lit82, Lit93);
                }
                this.Label42 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit94, Lit95, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit68, Lit96, Lit95, Boolean.FALSE);
                }
                this.Button4 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit97, Lit98, lambda$Fn11), $result);
                } else {
                    addToComponents(Lit68, Lit99, Lit98, lambda$Fn12);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit102, this.Button4$Click);
                } else {
                    addToFormEnvironment(Lit102, this.Button4$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Button4", "Click");
                } else {
                    addToEvents(Lit98, Lit93);
                }
                this.Label3 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit103, Lit104, lambda$Fn13), $result);
                } else {
                    addToComponents(Lit68, Lit111, Lit104, lambda$Fn14);
                }
                this.Button2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit112, Lit113, lambda$Fn15), $result);
                } else {
                    addToComponents(Lit68, Lit114, Lit113, lambda$Fn16);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit115, this.Button2$Click);
                } else {
                    addToFormEnvironment(Lit115, this.Button2$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Button2", "Click");
                } else {
                    addToEvents(Lit113, Lit93);
                }
                this.Label44 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit116, Lit117, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit68, Lit118, Lit117, Boolean.FALSE);
                }
                this.Label5 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit68, Lit119, Lit120, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit68, Lit121, Lit120, Boolean.FALSE);
                }
                this.Label1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit122, Lit123, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit124, Lit123, Boolean.FALSE);
                }
                this.ListView1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit125, Lit88, lambda$Fn17), $result);
                } else {
                    addToComponents(Lit0, Lit140, Lit88, lambda$Fn18);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit188, this.ListView1$AfterPicking);
                } else {
                    addToFormEnvironment(Lit188, this.ListView1$AfterPicking);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "ListView1", "AfterPicking");
                } else {
                    addToEvents(Lit88, Lit189);
                }
                this.VerticalScrollArrangement1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit190, Lit46, lambda$Fn19), $result);
                } else {
                    addToComponents(Lit0, Lit193, Lit46, lambda$Fn20);
                }
                this.Label15 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit194, Lit195, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit46, Lit196, Lit195, Boolean.FALSE);
                }
                this.name = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit197, Lit144, lambda$Fn21), $result);
                } else {
                    addToComponents(Lit46, Lit199, Lit144, lambda$Fn22);
                }
                this.Label17 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit200, Lit201, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit46, Lit202, Lit201, Boolean.FALSE);
                }
                this.HorizontalArrangement2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit203, Lit204, lambda$Fn23), $result);
                } else {
                    addToComponents(Lit46, Lit206, Lit204, lambda$Fn24);
                }
                this.Label7 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit204, Lit207, Lit208, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit204, Lit209, Lit208, Boolean.FALSE);
                }
                this.count = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit204, Lit210, Lit149, lambda$Fn25), $result);
                } else {
                    addToComponents(Lit204, Lit213, Lit149, lambda$Fn26);
                }
                this.Label21 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit214, Lit215, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit46, Lit216, Lit215, Boolean.FALSE);
                }
                this.HorizontalArrangement6 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit217, Lit218, lambda$Fn27), $result);
                } else {
                    addToComponents(Lit46, Lit220, Lit218, lambda$Fn28);
                }
                this.Label8 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit218, Lit221, Lit222, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit218, Lit223, Lit222, Boolean.FALSE);
                }
                this.exp2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit218, Lit224, Lit157, lambda$Fn29), $result);
                } else {
                    addToComponents(Lit218, Lit225, Lit157, lambda$Fn30);
                }
                this.Label11 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit226, Lit227, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit46, Lit228, Lit227, Boolean.FALSE);
                }
                this.HorizontalArrangement3 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit229, Lit230, lambda$Fn31), $result);
                } else {
                    addToComponents(Lit46, Lit232, Lit230, lambda$Fn32);
                }
                this.Label9 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit230, Lit233, Lit234, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit230, Lit235, Lit234, Boolean.FALSE);
                }
                this.svr = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit230, Lit236, Lit167, lambda$Fn33), $result);
                } else {
                    addToComponents(Lit230, Lit237, Lit167, lambda$Fn34);
                }
                this.Label12 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit238, Lit239, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit46, Lit240, Lit239, Boolean.FALSE);
                }
                this.HorizontalArrangement4 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit241, Lit242, lambda$Fn35), $result);
                } else {
                    addToComponents(Lit46, Lit244, Lit242, lambda$Fn36);
                }
                this.Label13 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit242, Lit245, Lit246, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit242, Lit247, Lit246, Boolean.FALSE);
                }
                this.Label18 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit242, Lit248, Lit249, lambda$Fn37), $result);
                } else {
                    addToComponents(Lit242, Lit250, Lit249, lambda$Fn38);
                }
                this.desc = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit242, Lit251, Lit172, lambda$Fn39), $result);
                } else {
                    addToComponents(Lit242, Lit252, Lit172, lambda$Fn40);
                }
                this.Label22 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit253, Lit254, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit46, Lit255, Lit254, Boolean.FALSE);
                }
                this.HorizontalArrangement5 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit256, Lit257, lambda$Fn41), $result);
                } else {
                    addToComponents(Lit46, Lit259, Lit257, lambda$Fn42);
                }
                this.Label19 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit257, Lit260, Lit261, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit257, Lit262, Lit261, Boolean.FALSE);
                }
                this.Label20 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit257, Lit263, Lit264, lambda$Fn43), $result);
                } else {
                    addToComponents(Lit257, Lit265, Lit264, lambda$Fn44);
                }
                this.instr = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit257, Lit266, Lit180, lambda$Fn45), $result);
                } else {
                    addToComponents(Lit257, Lit267, Lit180, lambda$Fn46);
                }
                this.Label23 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit46, Lit268, Lit269, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit46, Lit270, Lit269, Boolean.FALSE);
                }
                this.VerticalArrangement1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit271, Lit50, lambda$Fn47), $result);
                } else {
                    addToComponents(Lit0, Lit273, Lit50, lambda$Fn48);
                }
                this.Label25 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit274, Lit275, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit50, Lit276, Lit275, Boolean.FALSE);
                }
                this.HorizontalArrangement7 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit277, Lit278, lambda$Fn49), $result);
                } else {
                    addToComponents(Lit50, Lit279, Lit278, lambda$Fn50);
                }
                this.Label26 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit278, Lit280, Lit281, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit278, Lit282, Lit281, Boolean.FALSE);
                }
                this.VerticalArrangement2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit278, Lit283, Lit284, lambda$Fn51), $result);
                } else {
                    addToComponents(Lit278, Lit286, Lit284, lambda$Fn52);
                }
                this.Label28 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit284, Lit287, Lit288, lambda$Fn53), $result);
                } else {
                    addToComponents(Lit284, Lit289, Lit288, lambda$Fn54);
                }
                this.Label29 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit284, Lit290, Lit291, lambda$Fn55), $result);
                } else {
                    addToComponents(Lit284, Lit292, Lit291, lambda$Fn56);
                }
                this.TextBox1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit278, Lit293, Lit42, lambda$Fn57), $result);
                } else {
                    addToComponents(Lit278, Lit295, Lit42, lambda$Fn58);
                }
                this.Label27 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit278, Lit296, Lit297, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit278, Lit298, Lit297, Boolean.FALSE);
                }
                this.Label30 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit299, Lit300, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit50, Lit301, Lit300, Boolean.FALSE);
                }
                this.HorizontalArrangement8 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit302, Lit303, lambda$Fn59), $result);
                } else {
                    addToComponents(Lit50, Lit304, Lit303, lambda$Fn60);
                }
                this.Label31 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit303, Lit305, Lit306, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit303, Lit307, Lit306, Boolean.FALSE);
                }
                this.VerticalArrangement3 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit303, Lit308, Lit309, lambda$Fn61), $result);
                } else {
                    addToComponents(Lit303, Lit311, Lit309, lambda$Fn62);
                }
                this.Label32 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit309, Lit312, Lit313, lambda$Fn63), $result);
                } else {
                    addToComponents(Lit309, Lit314, Lit313, lambda$Fn64);
                }
                this.Label33 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit309, Lit315, Lit316, lambda$Fn65), $result);
                } else {
                    addToComponents(Lit309, Lit317, Lit316, lambda$Fn66);
                }
                this.TextBox2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit303, Lit318, Lit39, lambda$Fn67), $result);
                } else {
                    addToComponents(Lit303, Lit319, Lit39, lambda$Fn68);
                }
                this.Label34 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit303, Lit320, Lit321, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit303, Lit322, Lit321, Boolean.FALSE);
                }
                this.Label35 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit323, Lit324, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit50, Lit325, Lit324, Boolean.FALSE);
                }
                this.HorizontalArrangement9 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit326, Lit327, lambda$Fn69), $result);
                } else {
                    addToComponents(Lit50, Lit328, Lit327, lambda$Fn70);
                }
                this.Label37 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit327, Lit329, Lit330, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit327, Lit331, Lit330, Boolean.FALSE);
                }
                this.VerticalArrangement4 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit327, Lit332, Lit333, lambda$Fn71), $result);
                } else {
                    addToComponents(Lit327, Lit335, Lit333, lambda$Fn72);
                }
                this.Label38 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit333, Lit336, Lit337, lambda$Fn73), $result);
                } else {
                    addToComponents(Lit333, Lit338, Lit337, lambda$Fn74);
                }
                this.Label39 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit333, Lit339, Lit340, lambda$Fn75), $result);
                } else {
                    addToComponents(Lit333, Lit341, Lit340, lambda$Fn76);
                }
                this.Label40 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit327, Lit342, Lit343, lambda$Fn77), $result);
                } else {
                    addToComponents(Lit327, Lit345, Lit343, lambda$Fn78);
                }
                this.Switch1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit327, Lit346, Lit35, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit327, Lit347, Lit35, Boolean.FALSE);
                }
                this.Label41 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit327, Lit348, Lit349, lambda$Fn79), $result);
                } else {
                    addToComponents(Lit327, Lit350, Lit349, lambda$Fn80);
                }
                this.Label36 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit351, Lit352, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit50, Lit353, Lit352, Boolean.FALSE);
                }
                this.Button3 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit50, Lit354, Lit355, lambda$Fn81), $result);
                } else {
                    addToComponents(Lit50, Lit357, Lit355, lambda$Fn82);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit361, this.Button3$Click);
                } else {
                    addToFormEnvironment(Lit361, this.Button3$Click);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Button3", "Click");
                } else {
                    addToEvents(Lit355, Lit93);
                }
                this.Label6 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit362, Lit55, lambda$Fn83), $result);
                } else {
                    addToComponents(Lit0, Lit363, Lit55, lambda$Fn84);
                }
                this.Label2 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit364, Lit365, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit366, Lit365, Boolean.FALSE);
                }
                this.TaifunTools1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit367, Lit368, lambda$Fn85), $result);
                } else {
                    addToComponents(Lit0, Lit374, Lit368, lambda$Fn86);
                }
                this.Web1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit375, Lit29, lambda$Fn87), $result);
                } else {
                    addToComponents(Lit0, Lit378, Lit29, lambda$Fn88);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    runtime.addToCurrentFormEnvironment(Lit408, this.Web1$GotText);
                } else {
                    addToFormEnvironment(Lit408, this.Web1$GotText);
                }
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    EventDispatcher.registerEventForDelegation((HandlesEventDispatching) runtime.$Stthis$Mnform$St, "Web1", "GotText");
                } else {
                    addToEvents(Lit29, Lit409);
                }
                this.TinyDB1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit410, Lit32, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit411, Lit32, Boolean.FALSE);
                }
                this.CornerRadius1 = null;
                if (runtime.$Stthis$Mnis$Mnthe$Mnrepl$St != Boolean.FALSE) {
                    Values.writeValues(runtime.addComponentWithinRepl(Lit0, Lit412, Lit44, Boolean.FALSE), $result);
                } else {
                    addToComponents(Lit0, Lit413, Lit44, Boolean.FALSE);
                }
                runtime.initRuntime();
            } catch (ClassCastException e) {
                throw new WrongType(e, "java.lang.Runnable.run()", 1, find2);
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "java.lang.Runnable.run()", 1, find);
        }
    }

    static Object lambda3() {
        return runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2(runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("User-Agent", "(busybird15@mail.com)"), Lit7, "make a list"), runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("Accept", "application/geo+json"), Lit8, "make a list")), Lit9, "make a list");
    }

    static Object lambda4() {
        return runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.Empty, LList.Empty, "make a list");
    }

    static Object lambda5() {
        runtime.setAndCoerceProperty$Ex(Lit0, Lit11, Lit12, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit14, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit18, "Weather", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit20, Lit21, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit22, "bitmap.png", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit23, "portrait", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit24, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit25, "Responsive", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit26, "AppTheme.Light.DarkActionBar", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit0, Lit27, "Alerts", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit0, Lit28, Boolean.FALSE, Lit15);
    }

    public Object Alerts$Initialize() {
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit29, Lit30, runtime.lookupGlobalVarInCurrentFormEnvironment(Lit3, runtime.$Stthe$Mnnull$Mnvalue$St), Lit31);
        runtime.callComponentMethod(Lit32, Lit33, LList.list2("usedAlerts", runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.Empty, LList.Empty, "make a list")), Lit34);
        runtime.setAndCoerceProperty$Ex(Lit35, Lit36, runtime.callComponentMethod(Lit32, Lit37, LList.list2("alerts", Boolean.FALSE), Lit38), Lit15);
        runtime.setAndCoerceProperty$Ex(Lit39, Lit40, runtime.callComponentMethod(Lit32, Lit37, LList.list2("state", ""), Lit41), Lit19);
        runtime.setAndCoerceProperty$Ex(Lit42, Lit40, runtime.callComponentMethod(Lit32, Lit37, LList.list2("filter", ""), Lit43), Lit19);
        SimpleSymbol simpleSymbol = Lit44;
        SimpleSymbol simpleSymbol2 = Lit45;
        Pair list1 = LList.list1(runtime.lookupInCurrentFormEnvironment(Lit46));
        LList.chain1(LList.chain4(list1, Lit47, Lit48, Lit48, Lit48), Lit48);
        runtime.callComponentMethod(simpleSymbol, simpleSymbol2, list1, Lit49);
        SimpleSymbol simpleSymbol3 = Lit44;
        SimpleSymbol simpleSymbol4 = Lit45;
        Pair list12 = LList.list1(runtime.lookupInCurrentFormEnvironment(Lit50));
        LList.chain1(LList.chain4(list12, Lit47, Lit48, Lit48, Lit48), Lit48);
        runtime.callComponentMethod(simpleSymbol3, simpleSymbol4, list12, Lit51);
        runtime.setAndCoerceProperty$Ex(Lit29, Lit52, runtime.callYailPrimitive(strings.string$Mnappend, LList.list2("https://api.weather.gov/alerts/active/area/", runtime.callComponentMethod(Lit32, Lit37, LList.list2("state", "NY"), Lit53)), Lit54, "join"), Lit19);
        SimpleSymbol simpleSymbol5 = Lit55;
        SimpleSymbol simpleSymbol6 = Lit40;
        ModuleMethod moduleMethod = strings.string$Mnappend;
        Pair list13 = LList.list1("No alerts for ");
        LList.chain4(list13, runtime.callComponentMethod(Lit32, Lit37, LList.list2("filter", "[none]"), Lit56), " county in ", runtime.callComponentMethod(Lit32, Lit37, LList.list2("state", "[no state]"), Lit57), ".");
        runtime.setAndCoerceProperty$Ex(simpleSymbol5, simpleSymbol6, runtime.callYailPrimitive(moduleMethod, list13, Lit58, "join"), Lit19);
        return runtime.callComponentMethod(Lit29, Lit59, LList.Empty, LList.Empty);
    }

    public Object Alerts$BackPressed() {
        runtime.setThisForm();
        return runtime.callYailPrimitive(runtime.close$Mnscreen, LList.Empty, LList.Empty, "close screen");
    }

    static Object lambda6() {
        runtime.setAndCoerceProperty$Ex(Lit68, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit68, Lit69, Lit70, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit68, Lit20, Lit71, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit68, Lit72, Lit73, Lit13);
    }

    static Object lambda7() {
        runtime.setAndCoerceProperty$Ex(Lit68, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit68, Lit69, Lit70, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit68, Lit20, Lit71, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit68, Lit72, Lit73, Lit13);
    }

    static Object lambda8() {
        runtime.setAndCoerceProperty$Ex(Lit82, Lit83, Lit84, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit82, Lit85, "BackArow.png", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit82, Lit72, Lit84, Lit13);
    }

    static Object lambda9() {
        runtime.setAndCoerceProperty$Ex(Lit82, Lit83, Lit84, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit82, Lit85, "BackArow.png", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit82, Lit72, Lit84, Lit13);
    }

    public Object Button1$Click() {
        runtime.setThisForm();
        if (runtime.processOrDelayed$V(new Object[]{lambda$Fn9, lambda$Fn10}) != Boolean.FALSE) {
            return runtime.callYailPrimitive(runtime.close$Mnscreen, LList.Empty, LList.Empty, "close screen");
        }
        if (runtime.getProperty$1(Lit50, Lit87) == Boolean.FALSE) {
            return runtime.setAndCoerceProperty$Ex(Lit88, Lit87, Boolean.TRUE, Lit15);
        }
        SimpleSymbol simpleSymbol = Lit55;
        SimpleSymbol simpleSymbol2 = Lit40;
        ModuleMethod moduleMethod = strings.string$Mnappend;
        Pair list1 = LList.list1("No alerts for ");
        LList.chain4(list1, runtime.callComponentMethod(Lit32, Lit37, LList.list2("filter", "[none]"), Lit89), " county in ", runtime.callYailPrimitive(runtime.get$Mnstart$Mnvalue, LList.Empty, LList.Empty, "get start value"), ".");
        runtime.setAndCoerceProperty$Ex(simpleSymbol, simpleSymbol2, runtime.callYailPrimitive(moduleMethod, list1, Lit90, "join"), Lit19);
        runtime.setAndCoerceProperty$Ex(Lit50, Lit87, Boolean.FALSE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit87, Boolean.FALSE, Lit15);
        if (runtime.callYailPrimitive(runtime.yail$Mnlist$Mnempty$Qu, LList.list1(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit10, runtime.$Stthe$Mnnull$Mnvalue$St)), Lit91, "is list empty?") != Boolean.FALSE) {
            runtime.setAndCoerceProperty$Ex(Lit88, Lit87, Boolean.FALSE, Lit15);
            return runtime.setAndCoerceProperty$Ex(Lit55, Lit87, Boolean.TRUE, Lit15);
        }
        runtime.setAndCoerceProperty$Ex(Lit88, Lit87, Boolean.TRUE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit55, Lit87, Boolean.FALSE, Lit15);
    }

    static Object lambda10() {
        return runtime.getProperty$1(Lit55, Lit87);
    }

    static Object lambda11() {
        return runtime.getProperty$1(Lit88, Lit87);
    }

    static Object lambda12() {
        runtime.setAndCoerceProperty$Ex(Lit98, Lit83, Lit84, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit98, Lit85, "Reload.png", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit98, Lit72, Lit84, Lit13);
    }

    static Object lambda13() {
        runtime.setAndCoerceProperty$Ex(Lit98, Lit83, Lit84, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit98, Lit85, "Reload.png", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit98, Lit72, Lit84, Lit13);
    }

    public Object Button4$Click() {
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit29, Lit52, runtime.callYailPrimitive(strings.string$Mnappend, LList.list2("https://api.weather.gov/alerts/active/area/", runtime.callComponentMethod(Lit32, Lit37, LList.list2("state", "NY"), Lit100)), Lit101, "join"), Lit19);
        return runtime.callComponentMethod(Lit29, Lit59, LList.Empty, LList.Empty);
    }

    static Object lambda14() {
        runtime.setAndCoerceProperty$Ex(Lit104, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit106, Lit107, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit40, "Alerts", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit109, Lit110, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit104, Lit72, Lit73, Lit13);
    }

    static Object lambda15() {
        runtime.setAndCoerceProperty$Ex(Lit104, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit106, Lit107, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit40, "Alerts", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit104, Lit109, Lit110, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit104, Lit72, Lit73, Lit13);
    }

    static Object lambda16() {
        runtime.setAndCoerceProperty$Ex(Lit113, Lit83, Lit84, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit113, Lit85, "SettingsIcon.png", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit113, Lit72, Lit84, Lit13);
    }

    static Object lambda17() {
        runtime.setAndCoerceProperty$Ex(Lit113, Lit83, Lit84, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit113, Lit85, "SettingsIcon.png", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit113, Lit72, Lit84, Lit13);
    }

    public Object Button2$Click() {
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit50, Lit87, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit87, Boolean.FALSE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit87, Boolean.FALSE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit55, Lit87, Boolean.FALSE, Lit15);
    }

    static Object lambda18() {
        runtime.setAndCoerceProperty$Ex(Lit88, Lit20, Lit126, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit127, Lit128, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit129, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit83, Lit73, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit130, Lit110, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit131, Lit132, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit133, Lit134, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit135, Lit136, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit137, Lit138, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit88, Lit72, Lit139, Lit13);
    }

    static Object lambda19() {
        runtime.setAndCoerceProperty$Ex(Lit88, Lit20, Lit126, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit127, Lit128, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit129, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit83, Lit73, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit130, Lit110, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit131, Lit132, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit133, Lit134, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit135, Lit136, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit137, Lit138, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit88, Lit72, Lit139, Lit13);
    }

    public Object ListView1$AfterPicking() {
        Object obj;
        Object obj2;
        Object callYailPrimitive;
        Object obj3;
        Object obj4;
        Object callYailPrimitive2;
        Object obj5;
        Object obj6;
        Object callYailPrimitive3;
        Object obj7;
        Object obj8;
        Object callYailPrimitive4;
        Object obj9;
        Object obj10;
        Object callYailPrimitive5;
        Object obj11;
        Object callYailPrimitive6;
        runtime.setThisForm();
        runtime.setAndCoerceProperty$Ex(Lit88, Lit87, Boolean.FALSE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit87, Boolean.TRUE, Lit15);
        Object $alertInfo = runtime.callYailPrimitive(runtime.yail$Mndictionary$Mnlookup, LList.list3("properties", runtime.callYailPrimitive(runtime.yail$Mnlist$Mnget$Mnitem, LList.list2(runtime.lookupGlobalVarInCurrentFormEnvironment(Lit10, runtime.$Stthe$Mnnull$Mnvalue$St), runtime.getProperty$1(Lit88, Lit141)), Lit142, "select list item"), runtime.callYailPrimitive(runtime.make$Mnyail$Mndictionary, LList.Empty, LList.Empty, "make a dictionary")), Lit143, "dictionary lookup");
        SimpleSymbol simpleSymbol = Lit144;
        SimpleSymbol simpleSymbol2 = Lit40;
        ModuleMethod moduleMethod = runtime.string$Mnempty$Qu;
        ModuleMethod moduleMethod2 = runtime.yail$Mndictionary$Mnlookup;
        if ($alertInfo instanceof Package) {
            obj = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
        } else {
            obj = $alertInfo;
        }
        if (runtime.callYailPrimitive(moduleMethod, LList.list1(runtime.callYailPrimitive(moduleMethod2, LList.list3(NotificationCompat.CATEGORY_EVENT, obj, "Unknown Alert"), Lit146, "dictionary lookup")), Lit147, "is text empty?") != Boolean.FALSE) {
            callYailPrimitive = "Unknown Alert";
        } else {
            ModuleMethod moduleMethod3 = runtime.yail$Mndictionary$Mnlookup;
            if ($alertInfo instanceof Package) {
                obj2 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj2 = $alertInfo;
            }
            callYailPrimitive = runtime.callYailPrimitive(moduleMethod3, LList.list3(NotificationCompat.CATEGORY_EVENT, obj2, "Unknown Alert"), Lit148, "dictionary lookup");
        }
        runtime.setAndCoerceProperty$Ex(simpleSymbol, simpleSymbol2, callYailPrimitive, Lit19);
        SimpleSymbol simpleSymbol3 = Lit149;
        SimpleSymbol simpleSymbol4 = Lit40;
        ModuleMethod moduleMethod4 = strings.string$Mnappend;
        ModuleMethod moduleMethod5 = runtime.string$Mnreplace$Mnmappings$Mnlongest$Mnstring;
        ModuleMethod moduleMethod6 = runtime.string$Mnempty$Qu;
        ModuleMethod moduleMethod7 = runtime.yail$Mndictionary$Mnlookup;
        if ($alertInfo instanceof Package) {
            obj3 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
        } else {
            obj3 = $alertInfo;
        }
        if (runtime.callYailPrimitive(moduleMethod6, LList.list1(runtime.callYailPrimitive(moduleMethod7, LList.list3("areaDesc", obj3, "Unknown Alert"), Lit150, "dictionary lookup")), Lit151, "is text empty?") != Boolean.FALSE) {
            callYailPrimitive2 = "Unknown Counties";
        } else {
            ModuleMethod moduleMethod8 = runtime.yail$Mndictionary$Mnlookup;
            if ($alertInfo instanceof Package) {
                obj4 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj4 = $alertInfo;
            }
            callYailPrimitive2 = runtime.callYailPrimitive(moduleMethod8, LList.list3("areaDesc", obj4, "Unknown Alert"), Lit152, "dictionary lookup");
        }
        runtime.setAndCoerceProperty$Ex(simpleSymbol3, simpleSymbol4, runtime.callYailPrimitive(moduleMethod4, LList.list2("<b>Counties: </b>", runtime.callYailPrimitive(moduleMethod5, LList.list2(callYailPrimitive2, runtime.callYailPrimitive(runtime.make$Mnyail$Mndictionary, LList.list1(runtime.callYailPrimitive(runtime.make$Mndictionary$Mnpair, LList.list2(";", ","), Lit153, "make a pair")), Lit154, "make a dictionary")), Lit155, "replace with mappings")), Lit156, "join"), Lit19);
        SimpleSymbol simpleSymbol5 = Lit157;
        SimpleSymbol simpleSymbol6 = Lit40;
        ModuleMethod moduleMethod9 = strings.string$Mnappend;
        ModuleMethod moduleMethod10 = runtime.yail$Mnlist$Mnget$Mnitem;
        ModuleMethod moduleMethod11 = runtime.string$Mnsplit;
        ModuleMethod moduleMethod12 = runtime.string$Mnreplace$Mnmappings$Mnlongest$Mnstring;
        ModuleMethod moduleMethod13 = runtime.string$Mnempty$Qu;
        ModuleMethod moduleMethod14 = runtime.yail$Mndictionary$Mnlookup;
        if ($alertInfo instanceof Package) {
            obj5 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
        } else {
            obj5 = $alertInfo;
        }
        if (runtime.callYailPrimitive(moduleMethod13, LList.list1(runtime.callYailPrimitive(moduleMethod14, LList.list3("areaDesc", obj5, "Unknown Alert"), Lit158, "dictionary lookup")), Lit159, "is text empty?") != Boolean.FALSE) {
            callYailPrimitive3 = "Unknown Counties";
        } else {
            ModuleMethod moduleMethod15 = runtime.yail$Mndictionary$Mnlookup;
            if ($alertInfo instanceof Package) {
                obj6 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj6 = $alertInfo;
            }
            callYailPrimitive3 = runtime.callYailPrimitive(moduleMethod15, LList.list3("areaDesc", obj6, "Unknown Alert"), Lit160, "dictionary lookup");
        }
        runtime.setAndCoerceProperty$Ex(simpleSymbol5, simpleSymbol6, runtime.callYailPrimitive(moduleMethod9, LList.list2("<b>Expires: </b>", runtime.callYailPrimitive(moduleMethod10, LList.list2(runtime.callYailPrimitive(moduleMethod11, LList.list2(runtime.callYailPrimitive(moduleMethod12, LList.list2(callYailPrimitive3, runtime.callYailPrimitive(runtime.make$Mnyail$Mndictionary, LList.list1(runtime.callYailPrimitive(runtime.make$Mndictionary$Mnpair, LList.list2("T", " at "), Lit161, "make a pair")), Lit162, "make a dictionary")), Lit163, "replace with mappings"), "-0"), Lit164, "split"), Lit110), Lit165, "select list item")), Lit166, "join"), Lit19);
        SimpleSymbol simpleSymbol7 = Lit167;
        SimpleSymbol simpleSymbol8 = Lit40;
        ModuleMethod moduleMethod16 = strings.string$Mnappend;
        ModuleMethod moduleMethod17 = runtime.string$Mnempty$Qu;
        ModuleMethod moduleMethod18 = runtime.yail$Mndictionary$Mnlookup;
        if ($alertInfo instanceof Package) {
            obj7 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
        } else {
            obj7 = $alertInfo;
        }
        if (runtime.callYailPrimitive(moduleMethod17, LList.list1(runtime.callYailPrimitive(moduleMethod18, LList.list3("severity", obj7, "Unknown Alert"), Lit168, "dictionary lookup")), Lit169, "is text empty?") != Boolean.FALSE) {
            callYailPrimitive4 = "Unknown Severity";
        } else {
            ModuleMethod moduleMethod19 = runtime.yail$Mndictionary$Mnlookup;
            if ($alertInfo instanceof Package) {
                obj8 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj8 = $alertInfo;
            }
            callYailPrimitive4 = runtime.callYailPrimitive(moduleMethod19, LList.list3("severity", obj8, "Unknown Alert"), Lit170, "dictionary lookup");
        }
        runtime.setAndCoerceProperty$Ex(simpleSymbol7, simpleSymbol8, runtime.callYailPrimitive(moduleMethod16, LList.list2("<b>Severity: </b>", callYailPrimitive4), Lit171, "join"), Lit19);
        SimpleSymbol simpleSymbol9 = Lit172;
        SimpleSymbol simpleSymbol10 = Lit40;
        ModuleMethod moduleMethod20 = runtime.string$Mnreplace$Mnmappings$Mndictionary;
        ModuleMethod moduleMethod21 = runtime.string$Mnempty$Qu;
        ModuleMethod moduleMethod22 = runtime.yail$Mndictionary$Mnlookup;
        if ($alertInfo instanceof Package) {
            obj9 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
        } else {
            obj9 = $alertInfo;
        }
        if (runtime.callYailPrimitive(moduleMethod21, LList.list1(runtime.callYailPrimitive(moduleMethod22, LList.list3("description", obj9, "No description provided."), Lit173, "dictionary lookup")), Lit174, "is text empty?") != Boolean.FALSE) {
            callYailPrimitive5 = "No description provided.";
        } else {
            ModuleMethod moduleMethod23 = runtime.yail$Mndictionary$Mnlookup;
            if ($alertInfo instanceof Package) {
                obj10 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj10 = $alertInfo;
            }
            callYailPrimitive5 = runtime.callYailPrimitive(moduleMethod23, LList.list3("description", obj10, "No description provided."), Lit175, "dictionary lookup");
        }
        runtime.setAndCoerceProperty$Ex(simpleSymbol9, simpleSymbol10, runtime.callYailPrimitive(moduleMethod20, LList.list2(callYailPrimitive5, runtime.callYailPrimitive(runtime.make$Mnyail$Mndictionary, LList.list2(runtime.callYailPrimitive(runtime.make$Mndictionary$Mnpair, LList.list2("\n", " "), Lit176, "make a pair"), runtime.callYailPrimitive(runtime.make$Mndictionary$Mnpair, LList.list2("\n\n", "<br>"), Lit177, "make a pair")), Lit178, "make a dictionary")), Lit179, "replace with mappings"), Lit19);
        SimpleSymbol simpleSymbol11 = Lit180;
        SimpleSymbol simpleSymbol12 = Lit40;
        ModuleMethod moduleMethod24 = runtime.string$Mnreplace$Mnmappings$Mndictionary;
        ModuleMethod moduleMethod25 = runtime.string$Mnempty$Qu;
        ModuleMethod moduleMethod26 = runtime.yail$Mndictionary$Mnlookup;
        if ($alertInfo instanceof Package) {
            obj11 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
        } else {
            obj11 = $alertInfo;
        }
        if (runtime.callYailPrimitive(moduleMethod25, LList.list1(runtime.callYailPrimitive(moduleMethod26, LList.list3("instruction", obj11, "No description provided."), Lit181, "dictionary lookup")), Lit182, "is text empty?") != Boolean.FALSE) {
            callYailPrimitive6 = "No description provided.";
        } else {
            ModuleMethod moduleMethod27 = runtime.yail$Mndictionary$Mnlookup;
            if ($alertInfo instanceof Package) {
                $alertInfo = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit145), " is not bound in the current context"), "Unbound Variable");
            }
            callYailPrimitive6 = runtime.callYailPrimitive(moduleMethod27, LList.list3("instruction", $alertInfo, "No description provided."), Lit183, "dictionary lookup");
        }
        return runtime.setAndCoerceProperty$Ex(simpleSymbol11, simpleSymbol12, runtime.callYailPrimitive(moduleMethod24, LList.list2(callYailPrimitive6, runtime.callYailPrimitive(runtime.make$Mnyail$Mndictionary, LList.list2(runtime.callYailPrimitive(runtime.make$Mndictionary$Mnpair, LList.list2("\n", " "), Lit184, "make a pair"), runtime.callYailPrimitive(runtime.make$Mndictionary$Mnpair, LList.list2("\n\n", "<br>"), Lit185, "make a pair")), Lit186, "make a dictionary")), Lit187, "replace with mappings"), Lit19);
    }

    static Object lambda20() {
        runtime.setAndCoerceProperty$Ex(Lit46, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit20, Lit191, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit83, Lit73, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit87, Boolean.FALSE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit46, Lit72, Lit192, Lit13);
    }

    static Object lambda21() {
        runtime.setAndCoerceProperty$Ex(Lit46, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit20, Lit191, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit83, Lit73, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit46, Lit87, Boolean.FALSE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit46, Lit72, Lit192, Lit13);
    }

    static Object lambda22() {
        runtime.setAndCoerceProperty$Ex(Lit144, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit40, "Unknown Alert", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit109, Lit110, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit133, Lit198, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit144, Lit72, Lit73, Lit13);
    }

    static Object lambda23() {
        runtime.setAndCoerceProperty$Ex(Lit144, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit40, "Unknown Alert", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit109, Lit110, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit144, Lit133, Lit198, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit144, Lit72, Lit73, Lit13);
    }

    static Object lambda24() {
        runtime.setAndCoerceProperty$Ex(Lit204, Lit20, Lit205, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit204, Lit72, Lit73, Lit13);
    }

    static Object lambda25() {
        runtime.setAndCoerceProperty$Ex(Lit204, Lit20, Lit205, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit204, Lit72, Lit73, Lit13);
    }

    static Object lambda26() {
        runtime.setAndCoerceProperty$Ex(Lit149, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit149, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit149, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit149, Lit40, "<b>Counties:</b> Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit149, Lit72, Lit73, Lit13);
    }

    static Object lambda27() {
        runtime.setAndCoerceProperty$Ex(Lit149, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit149, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit149, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit149, Lit40, "<b>Counties:</b> Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit149, Lit72, Lit73, Lit13);
    }

    static Object lambda28() {
        runtime.setAndCoerceProperty$Ex(Lit218, Lit20, Lit219, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit218, Lit72, Lit73, Lit13);
    }

    static Object lambda29() {
        runtime.setAndCoerceProperty$Ex(Lit218, Lit20, Lit219, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit218, Lit72, Lit73, Lit13);
    }

    static Object lambda30() {
        runtime.setAndCoerceProperty$Ex(Lit157, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit157, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit157, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit157, Lit40, "<b>Expires:</b> Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit157, Lit72, Lit73, Lit13);
    }

    static Object lambda31() {
        runtime.setAndCoerceProperty$Ex(Lit157, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit157, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit157, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit157, Lit40, "<b>Expires:</b> Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit157, Lit72, Lit73, Lit13);
    }

    static Object lambda32() {
        runtime.setAndCoerceProperty$Ex(Lit230, Lit20, Lit231, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit230, Lit72, Lit73, Lit13);
    }

    static Object lambda33() {
        runtime.setAndCoerceProperty$Ex(Lit230, Lit20, Lit231, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit230, Lit72, Lit73, Lit13);
    }

    static Object lambda34() {
        runtime.setAndCoerceProperty$Ex(Lit167, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit40, "<b>Severity:</b> Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit167, Lit72, Lit73, Lit13);
    }

    static Object lambda35() {
        runtime.setAndCoerceProperty$Ex(Lit167, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit167, Lit40, "<b>Severity:</b> Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit167, Lit72, Lit73, Lit13);
    }

    static Object lambda36() {
        runtime.setAndCoerceProperty$Ex(Lit242, Lit20, Lit243, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit242, Lit72, Lit73, Lit13);
    }

    static Object lambda37() {
        runtime.setAndCoerceProperty$Ex(Lit242, Lit20, Lit243, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit242, Lit72, Lit73, Lit13);
    }

    /* compiled from: Alerts.yail */
    public class frame extends ModuleBody {
        Alerts $main;

        public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 3:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 4:
                    if (!(obj instanceof Alerts)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 5:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 7:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 9:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 14:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 15:
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                case 16:
                    if (!(obj instanceof Alerts)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.proc = moduleMethod;
                    callContext.pc = 1;
                    return 0;
                default:
                    return super.match1(moduleMethod, obj, callContext);
            }
        }

        public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 6:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 7:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 10:
                    if (!(obj instanceof Symbol)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 11:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 13:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                case 19:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.proc = moduleMethod;
                    callContext.pc = 2;
                    return 0;
                default:
                    return super.match2(moduleMethod, obj, obj2, callContext);
            }
        }

        public int match4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 12:
                    callContext.value1 = obj;
                    callContext.value2 = obj2;
                    callContext.value3 = obj3;
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                case 17:
                    if (!(obj instanceof Alerts)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    if (!(obj2 instanceof Component)) {
                        return -786430;
                    }
                    callContext.value2 = obj2;
                    if (!(obj3 instanceof String)) {
                        return -786429;
                    }
                    callContext.value3 = obj3;
                    if (!(obj4 instanceof String)) {
                        return -786428;
                    }
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                case 18:
                    if (!(obj instanceof Alerts)) {
                        return -786431;
                    }
                    callContext.value1 = obj;
                    if (!(obj2 instanceof Component)) {
                        return -786430;
                    }
                    callContext.value2 = obj2;
                    if (!(obj3 instanceof String)) {
                        return -786429;
                    }
                    callContext.value3 = obj3;
                    callContext.value4 = obj4;
                    callContext.proc = moduleMethod;
                    callContext.pc = 4;
                    return 0;
                case 116:
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

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            switch (moduleMethod.selector) {
                case 3:
                    return this.$main.getSimpleName(obj);
                case 4:
                    try {
                        this.$main.onCreate((Bundle) obj);
                        return Values.empty;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "onCreate", 1, obj);
                    }
                case 5:
                    this.$main.androidLogForm(obj);
                    return Values.empty;
                case 7:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "lookup-in-form-environment", 1, obj);
                    }
                case 9:
                    try {
                        return this.$main.isBoundInFormEnvironment((Symbol) obj) ? Boolean.TRUE : Boolean.FALSE;
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "is-bound-in-form-environment", 1, obj);
                    }
                case 14:
                    this.$main.addToFormDoAfterCreation(obj);
                    return Values.empty;
                case 15:
                    this.$main.sendError(obj);
                    return Values.empty;
                case 16:
                    this.$main.processException(obj);
                    return Values.empty;
                default:
                    return super.apply1(moduleMethod, obj);
            }
        }

        public Object apply4(ModuleMethod moduleMethod, Object obj, Object obj2, Object obj3, Object obj4) {
            boolean z = true;
            switch (moduleMethod.selector) {
                case 12:
                    this.$main.addToComponents(obj, obj2, obj3, obj4);
                    return Values.empty;
                case 17:
                    try {
                        try {
                            try {
                                try {
                                    return this.$main.dispatchEvent((Component) obj, (String) obj2, (String) obj3, (Object[]) obj4) ? Boolean.TRUE : Boolean.FALSE;
                                } catch (ClassCastException e) {
                                    throw new WrongType(e, "dispatchEvent", 4, obj4);
                                }
                            } catch (ClassCastException e2) {
                                throw new WrongType(e2, "dispatchEvent", 3, obj3);
                            }
                        } catch (ClassCastException e3) {
                            throw new WrongType(e3, "dispatchEvent", 2, obj2);
                        }
                    } catch (ClassCastException e4) {
                        throw new WrongType(e4, "dispatchEvent", 1, obj);
                    }
                case 18:
                    Alerts alerts = this.$main;
                    try {
                        Component component = (Component) obj;
                        try {
                            String str = (String) obj2;
                            try {
                                if (obj3 == Boolean.FALSE) {
                                    z = false;
                                }
                                try {
                                    alerts.dispatchGenericEvent(component, str, z, (Object[]) obj4);
                                    return Values.empty;
                                } catch (ClassCastException e5) {
                                    throw new WrongType(e5, "dispatchGenericEvent", 4, obj4);
                                }
                            } catch (ClassCastException e6) {
                                throw new WrongType(e6, "dispatchGenericEvent", 3, obj3);
                            }
                        } catch (ClassCastException e7) {
                            throw new WrongType(e7, "dispatchGenericEvent", 2, obj2);
                        }
                    } catch (ClassCastException e8) {
                        throw new WrongType(e8, "dispatchGenericEvent", 1, obj);
                    }
                case 116:
                    return this.$main.Web1$GotText(obj, obj2, obj3, obj4);
                default:
                    return super.apply4(moduleMethod, obj, obj2, obj3, obj4);
            }
        }

        public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
            switch (moduleMethod.selector) {
                case 6:
                    try {
                        this.$main.addToFormEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e) {
                        throw new WrongType(e, "add-to-form-environment", 1, obj);
                    }
                case 7:
                    try {
                        return this.$main.lookupInFormEnvironment((Symbol) obj, obj2);
                    } catch (ClassCastException e2) {
                        throw new WrongType(e2, "lookup-in-form-environment", 1, obj);
                    }
                case 10:
                    try {
                        this.$main.addToGlobalVarEnvironment((Symbol) obj, obj2);
                        return Values.empty;
                    } catch (ClassCastException e3) {
                        throw new WrongType(e3, "add-to-global-var-environment", 1, obj);
                    }
                case 11:
                    this.$main.addToEvents(obj, obj2);
                    return Values.empty;
                case 13:
                    this.$main.addToGlobalVars(obj, obj2);
                    return Values.empty;
                case 19:
                    return this.$main.lookupHandler(obj, obj2);
                default:
                    return super.apply2(moduleMethod, obj, obj2);
            }
        }

        public Object apply0(ModuleMethod moduleMethod) {
            switch (moduleMethod.selector) {
                case 20:
                    return Alerts.lambda2();
                case 21:
                    this.$main.$define();
                    return Values.empty;
                case 22:
                    return Alerts.lambda3();
                case 23:
                    return Alerts.lambda4();
                case 24:
                    return Alerts.lambda5();
                case 25:
                    return this.$main.Alerts$Initialize();
                case 26:
                    return this.$main.Alerts$BackPressed();
                case 27:
                    return Alerts.lambda6();
                case 28:
                    return Alerts.lambda7();
                case 29:
                    return Alerts.lambda8();
                case 30:
                    return Alerts.lambda9();
                case 31:
                    return Alerts.lambda10();
                case 32:
                    return Alerts.lambda11();
                case 33:
                    return this.$main.Button1$Click();
                case 34:
                    return Alerts.lambda12();
                case 35:
                    return Alerts.lambda13();
                case 36:
                    return this.$main.Button4$Click();
                case 37:
                    return Alerts.lambda14();
                case 38:
                    return Alerts.lambda15();
                case 39:
                    return Alerts.lambda16();
                case 40:
                    return Alerts.lambda17();
                case 41:
                    return this.$main.Button2$Click();
                case 42:
                    return Alerts.lambda18();
                case 43:
                    return Alerts.lambda19();
                case 44:
                    return this.$main.ListView1$AfterPicking();
                case 45:
                    return Alerts.lambda20();
                case 46:
                    return Alerts.lambda21();
                case 47:
                    return Alerts.lambda22();
                case 48:
                    return Alerts.lambda23();
                case 49:
                    return Alerts.lambda24();
                case 50:
                    return Alerts.lambda25();
                case 51:
                    return Alerts.lambda26();
                case 52:
                    return Alerts.lambda27();
                case 53:
                    return Alerts.lambda28();
                case 54:
                    return Alerts.lambda29();
                case 55:
                    return Alerts.lambda30();
                case 56:
                    return Alerts.lambda31();
                case 57:
                    return Alerts.lambda32();
                case 58:
                    return Alerts.lambda33();
                case 59:
                    return Alerts.lambda34();
                case 60:
                    return Alerts.lambda35();
                case 61:
                    return Alerts.lambda36();
                case 62:
                    return Alerts.lambda37();
                case 63:
                    return Alerts.lambda38();
                case 64:
                    return Alerts.lambda39();
                case 65:
                    return Alerts.lambda40();
                case 66:
                    return Alerts.lambda41();
                case 67:
                    return Alerts.lambda42();
                case 68:
                    return Alerts.lambda43();
                case 69:
                    return Alerts.lambda44();
                case 70:
                    return Alerts.lambda45();
                case 71:
                    return Alerts.lambda46();
                case 72:
                    return Alerts.lambda47();
                case 73:
                    return Alerts.lambda48();
                case 74:
                    return Alerts.lambda49();
                case 75:
                    return Alerts.lambda50();
                case 76:
                    return Alerts.lambda51();
                case 77:
                    return Alerts.lambda52();
                case 78:
                    return Alerts.lambda53();
                case 79:
                    return Alerts.lambda54();
                case 80:
                    return Alerts.lambda55();
                case 81:
                    return Alerts.lambda56();
                case 82:
                    return Alerts.lambda57();
                case 83:
                    return Alerts.lambda58();
                case 84:
                    return Alerts.lambda59();
                case 85:
                    return Alerts.lambda60();
                case 86:
                    return Alerts.lambda61();
                case 87:
                    return Alerts.lambda62();
                case 88:
                    return Alerts.lambda63();
                case 89:
                    return Alerts.lambda64();
                case 90:
                    return Alerts.lambda65();
                case 91:
                    return Alerts.lambda66();
                case 92:
                    return Alerts.lambda67();
                case 93:
                    return Alerts.lambda68();
                case 94:
                    return Alerts.lambda69();
                case 95:
                    return Alerts.lambda70();
                case 96:
                    return Alerts.lambda71();
                case 97:
                    return Alerts.lambda72();
                case 98:
                    return Alerts.lambda73();
                case 99:
                    return Alerts.lambda74();
                case 100:
                    return Alerts.lambda75();
                case 101:
                    return Alerts.lambda76();
                case 102:
                    return Alerts.lambda77();
                case 103:
                    return Alerts.lambda78();
                case 104:
                    return Alerts.lambda79();
                case 105:
                    return Alerts.lambda80();
                case 106:
                    return Alerts.lambda81();
                case 107:
                    return Alerts.lambda82();
                case 108:
                    return Alerts.lambda83();
                case 109:
                    return this.$main.Button3$Click();
                case 110:
                    return Alerts.lambda84();
                case 111:
                    return Alerts.lambda85();
                case 112:
                    return Alerts.lambda86();
                case 113:
                    return Alerts.lambda87();
                case 114:
                    return Alerts.lambda88();
                case 115:
                    return Alerts.lambda89();
                default:
                    return super.apply0(moduleMethod);
            }
        }

        public int match0(ModuleMethod moduleMethod, CallContext callContext) {
            switch (moduleMethod.selector) {
                case 20:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 21:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 22:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 23:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 24:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 25:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 26:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 27:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 28:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 29:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 30:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 31:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 32:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 33:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 34:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 35:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
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
                case 42:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 43:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 44:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 45:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 46:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 47:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 48:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 49:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 50:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 51:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 52:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 53:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 54:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 55:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 56:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 57:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 58:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 59:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 60:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 61:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 62:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 63:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 64:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 65:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 66:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 67:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 68:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 69:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 70:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 71:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 72:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 73:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 74:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 75:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 76:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 77:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 78:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 79:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 80:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 81:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 82:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 83:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 84:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 85:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 86:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 87:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 88:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 89:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 90:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 91:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 92:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 93:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 94:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 95:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 96:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 97:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 98:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 99:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 100:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 101:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 102:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 103:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 104:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 105:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 106:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 107:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 108:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 109:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 110:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 111:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 112:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 113:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 114:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                case 115:
                    callContext.proc = moduleMethod;
                    callContext.pc = 0;
                    return 0;
                default:
                    return super.match0(moduleMethod, callContext);
            }
        }
    }

    static Object lambda38() {
        runtime.setAndCoerceProperty$Ex(Lit249, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit249, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit249, Lit212, Boolean.TRUE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit249, Lit40, "<b>Info:</b> ", Lit19);
    }

    static Object lambda39() {
        runtime.setAndCoerceProperty$Ex(Lit249, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit249, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit249, Lit212, Boolean.TRUE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit249, Lit40, "<b>Info:</b> ", Lit19);
    }

    static Object lambda40() {
        runtime.setAndCoerceProperty$Ex(Lit172, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit172, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit172, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit172, Lit40, "Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit172, Lit72, Lit73, Lit13);
    }

    static Object lambda41() {
        runtime.setAndCoerceProperty$Ex(Lit172, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit172, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit172, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit172, Lit40, "Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit172, Lit72, Lit73, Lit13);
    }

    static Object lambda42() {
        runtime.setAndCoerceProperty$Ex(Lit257, Lit20, Lit258, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit257, Lit72, Lit73, Lit13);
    }

    static Object lambda43() {
        runtime.setAndCoerceProperty$Ex(Lit257, Lit20, Lit258, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit257, Lit72, Lit73, Lit13);
    }

    static Object lambda44() {
        runtime.setAndCoerceProperty$Ex(Lit264, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit264, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit264, Lit212, Boolean.TRUE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit264, Lit40, "<b>Instructions:</b> ", Lit19);
    }

    static Object lambda45() {
        runtime.setAndCoerceProperty$Ex(Lit264, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit264, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit264, Lit212, Boolean.TRUE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit264, Lit40, "<b>Instructions:</b> ", Lit19);
    }

    static Object lambda46() {
        runtime.setAndCoerceProperty$Ex(Lit180, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit180, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit180, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit180, Lit40, "Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit180, Lit72, Lit73, Lit13);
    }

    static Object lambda47() {
        runtime.setAndCoerceProperty$Ex(Lit180, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit180, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit180, Lit212, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit180, Lit40, "Unknown", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit180, Lit72, Lit73, Lit13);
    }

    static Object lambda48() {
        runtime.setAndCoerceProperty$Ex(Lit50, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit50, Lit83, Lit73, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit50, Lit87, Boolean.FALSE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit50, Lit72, Lit272, Lit13);
    }

    static Object lambda49() {
        runtime.setAndCoerceProperty$Ex(Lit50, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit50, Lit83, Lit73, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit50, Lit87, Boolean.FALSE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit50, Lit72, Lit272, Lit13);
    }

    static Object lambda50() {
        runtime.setAndCoerceProperty$Ex(Lit278, Lit16, Lit17, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit278, Lit72, Lit73, Lit13);
    }

    static Object lambda51() {
        runtime.setAndCoerceProperty$Ex(Lit278, Lit16, Lit17, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit278, Lit72, Lit73, Lit13);
    }

    static Object lambda52() {
        runtime.setAndCoerceProperty$Ex(Lit284, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit284, Lit69, Lit70, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit284, Lit72, Lit285, Lit13);
    }

    static Object lambda53() {
        runtime.setAndCoerceProperty$Ex(Lit284, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit284, Lit69, Lit70, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit284, Lit72, Lit285, Lit13);
    }

    static Object lambda54() {
        runtime.setAndCoerceProperty$Ex(Lit288, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit288, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit288, Lit40, "Your County", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit288, Lit72, Lit73, Lit13);
    }

    static Object lambda55() {
        runtime.setAndCoerceProperty$Ex(Lit288, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit288, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit288, Lit40, "Your County", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit288, Lit72, Lit73, Lit13);
    }

    static Object lambda56() {
        runtime.setAndCoerceProperty$Ex(Lit291, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit291, Lit40, "Receive alerts for this county only.", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit291, Lit72, Lit73, Lit13);
    }

    static Object lambda57() {
        runtime.setAndCoerceProperty$Ex(Lit291, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit291, Lit40, "Receive alerts for this county only.", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit291, Lit72, Lit73, Lit13);
    }

    static Object lambda58() {
        runtime.setAndCoerceProperty$Ex(Lit42, Lit106, Lit128, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit42, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit42, Lit294, "County", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit42, Lit72, Lit73, Lit13);
    }

    static Object lambda59() {
        runtime.setAndCoerceProperty$Ex(Lit42, Lit106, Lit128, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit42, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit42, Lit294, "County", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit42, Lit72, Lit73, Lit13);
    }

    static Object lambda60() {
        runtime.setAndCoerceProperty$Ex(Lit303, Lit16, Lit17, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit303, Lit72, Lit73, Lit13);
    }

    static Object lambda61() {
        runtime.setAndCoerceProperty$Ex(Lit303, Lit16, Lit17, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit303, Lit72, Lit73, Lit13);
    }

    static Object lambda62() {
        runtime.setAndCoerceProperty$Ex(Lit309, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit309, Lit69, Lit70, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit309, Lit72, Lit310, Lit13);
    }

    static Object lambda63() {
        runtime.setAndCoerceProperty$Ex(Lit309, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit309, Lit69, Lit70, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit309, Lit72, Lit310, Lit13);
    }

    static Object lambda64() {
        runtime.setAndCoerceProperty$Ex(Lit313, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit313, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit313, Lit40, "Two-letter State Code", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit313, Lit72, Lit73, Lit13);
    }

    static Object lambda65() {
        runtime.setAndCoerceProperty$Ex(Lit313, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit313, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit313, Lit40, "Two-letter State Code", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit313, Lit72, Lit73, Lit13);
    }

    static Object lambda66() {
        runtime.setAndCoerceProperty$Ex(Lit316, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit316, Lit40, "Receive notification alerts for your county in this state. (e.g. 'NY')", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit316, Lit72, Lit73, Lit13);
    }

    static Object lambda67() {
        runtime.setAndCoerceProperty$Ex(Lit316, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit316, Lit40, "Receive notification alerts for your county in this state. (e.g. 'NY')", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit316, Lit72, Lit73, Lit13);
    }

    static Object lambda68() {
        runtime.setAndCoerceProperty$Ex(Lit39, Lit106, Lit128, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit39, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit39, Lit294, "State", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit39, Lit72, Lit73, Lit13);
    }

    static Object lambda69() {
        runtime.setAndCoerceProperty$Ex(Lit39, Lit106, Lit128, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit39, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit39, Lit294, "State", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit39, Lit72, Lit73, Lit13);
    }

    static Object lambda70() {
        runtime.setAndCoerceProperty$Ex(Lit327, Lit16, Lit17, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit327, Lit72, Lit73, Lit13);
    }

    static Object lambda71() {
        runtime.setAndCoerceProperty$Ex(Lit327, Lit16, Lit17, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit327, Lit72, Lit73, Lit13);
    }

    static Object lambda72() {
        runtime.setAndCoerceProperty$Ex(Lit333, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit333, Lit69, Lit70, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit333, Lit72, Lit334, Lit13);
    }

    static Object lambda73() {
        runtime.setAndCoerceProperty$Ex(Lit333, Lit16, Lit17, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit333, Lit69, Lit70, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit333, Lit72, Lit334, Lit13);
    }

    static Object lambda74() {
        runtime.setAndCoerceProperty$Ex(Lit337, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit337, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit337, Lit40, "Notification Alerts", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit337, Lit72, Lit73, Lit13);
    }

    static Object lambda75() {
        runtime.setAndCoerceProperty$Ex(Lit337, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit337, Lit106, Lit138, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit337, Lit40, "Notification Alerts", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit337, Lit72, Lit73, Lit13);
    }

    static Object lambda76() {
        runtime.setAndCoerceProperty$Ex(Lit340, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit340, Lit40, "Receive notifications for unread alerts.", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit340, Lit72, Lit73, Lit13);
    }

    static Object lambda77() {
        runtime.setAndCoerceProperty$Ex(Lit340, Lit108, "VarelaRound-Regular.ttf", Lit19);
        runtime.setAndCoerceProperty$Ex(Lit340, Lit40, "Receive notifications for unread alerts.", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit340, Lit72, Lit73, Lit13);
    }

    static Object lambda78() {
        return runtime.setAndCoerceProperty$Ex(Lit343, Lit72, Lit344, Lit13);
    }

    static Object lambda79() {
        return runtime.setAndCoerceProperty$Ex(Lit343, Lit72, Lit344, Lit13);
    }

    static Object lambda80() {
        return runtime.setAndCoerceProperty$Ex(Lit349, Lit72, Lit73, Lit13);
    }

    static Object lambda81() {
        return runtime.setAndCoerceProperty$Ex(Lit349, Lit72, Lit73, Lit13);
    }

    static Object lambda82() {
        runtime.setAndCoerceProperty$Ex(Lit355, Lit20, Lit356, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit355, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit355, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit355, Lit108, "VarelaRound-Regular.ttf", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit355, Lit40, "Save", Lit19);
    }

    static Object lambda83() {
        runtime.setAndCoerceProperty$Ex(Lit355, Lit20, Lit356, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit355, Lit105, Boolean.TRUE, Lit15);
        runtime.setAndCoerceProperty$Ex(Lit355, Lit106, Lit211, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit355, Lit108, "VarelaRound-Regular.ttf", Lit19);
        return runtime.setAndCoerceProperty$Ex(Lit355, Lit40, "Save", Lit19);
    }

    public Object Button3$Click() {
        runtime.setThisForm();
        runtime.callComponentMethod(Lit32, Lit33, LList.list2("filter", runtime.getProperty$1(Lit42, Lit40)), Lit358);
        runtime.callComponentMethod(Lit32, Lit33, LList.list2("state", runtime.getProperty$1(Lit39, Lit40)), Lit359);
        return runtime.callComponentMethod(Lit32, Lit33, LList.list2("alerts", runtime.getProperty$1(Lit35, Lit36)), Lit360);
    }

    static Object lambda84() {
        runtime.setAndCoerceProperty$Ex(Lit55, Lit106, Lit128, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit55, Lit108, "VarelaRound-Regular.ttf", Lit19);
    }

    static Object lambda85() {
        runtime.setAndCoerceProperty$Ex(Lit55, Lit106, Lit128, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit55, Lit108, "VarelaRound-Regular.ttf", Lit19);
    }

    static Object lambda86() {
        runtime.setAndCoerceProperty$Ex(Lit368, Lit369, Lit370, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit368, Lit371, Lit372, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit368, Lit373, Boolean.TRUE, Lit15);
    }

    static Object lambda87() {
        runtime.setAndCoerceProperty$Ex(Lit368, Lit369, Lit370, Lit13);
        runtime.setAndCoerceProperty$Ex(Lit368, Lit371, Lit372, Lit13);
        return runtime.setAndCoerceProperty$Ex(Lit368, Lit373, Boolean.TRUE, Lit15);
    }

    static Object lambda88() {
        return runtime.setAndCoerceProperty$Ex(Lit29, Lit376, Lit377, Lit13);
    }

    static Object lambda89() {
        return runtime.setAndCoerceProperty$Ex(Lit29, Lit376, Lit377, Lit13);
    }

    /* compiled from: Alerts.yail */
    public class frame0 extends ModuleBody {
        Object $elements;
        final ModuleMethod proc$Fn89 = new ModuleMethod(this, 2, Alerts.Lit401, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 2 ? lambda90proc(obj) : super.apply1(moduleMethod, obj);
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

        public Object lambda90proc(Object $entireAlert) {
            Object obj;
            frame1 closureEnv = new frame1();
            closureEnv.staticLink = this;
            closureEnv.$entireAlert = $entireAlert;
            ModuleMethod moduleMethod = closureEnv.proc$Fn90;
            ModuleMethod moduleMethod2 = closureEnv.proc$Fn90;
            ModuleMethod moduleMethod3 = runtime.string$Mnsplit$Mnat$Mnany;
            ModuleMethod moduleMethod4 = runtime.yail$Mndictionary$Mnrecursive$Mnlookup;
            Object callYailPrimitive = runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("properties", "areaDesc"), Alerts.Lit397, "make a list");
            if (closureEnv.$entireAlert instanceof Package) {
                obj = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Alerts.Lit390), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj = closureEnv.$entireAlert;
            }
            return runtime.yailForEach(moduleMethod2, runtime.callYailPrimitive(moduleMethod3, LList.list2(runtime.callYailPrimitive(moduleMethod4, LList.list3(callYailPrimitive, obj, "not found"), Alerts.Lit398, "dictionary recursive lookup"), runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("; ", " "), Alerts.Lit399, "make a list")), Alerts.Lit400, "split at any"));
        }
    }

    /* compiled from: Alerts.yail */
    public class frame1 extends ModuleBody {
        Object $entireAlert;
        final ModuleMethod proc$Fn90 = new ModuleMethod(this, 1, Alerts.Lit401, FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        frame0 staticLink;

        public Object apply1(ModuleMethod moduleMethod, Object obj) {
            return moduleMethod.selector == 1 ? lambda91proc(obj) : super.apply1(moduleMethod, obj);
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

        public Object lambda91proc(Object $county) {
            Object obj;
            Object obj2;
            Object obj3;
            Object obj4;
            ModuleMethod moduleMethod = runtime.yail$Mnequal$Qu;
            ModuleMethod moduleMethod2 = runtime.string$Mnto$Mnlower$Mncase;
            if ($county instanceof Package) {
                $county = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Alerts.Lit382), " is not bound in the current context"), "Unbound Variable");
            }
            if (runtime.callYailPrimitive(moduleMethod, LList.list2(runtime.callYailPrimitive(moduleMethod2, LList.list1($county), Alerts.Lit383, "downcase"), runtime.callYailPrimitive(runtime.string$Mnto$Mnlower$Mncase, LList.list1(runtime.callComponentMethod(Alerts.Lit32, Alerts.Lit37, LList.list2("filter", ""), Alerts.Lit384)), Alerts.Lit385, "downcase")), Alerts.Lit386, "=") == Boolean.FALSE) {
                return Values.empty;
            }
            ModuleMethod moduleMethod3 = runtime.yail$Mnlist$Mnadd$Mnto$Mnlist$Ex;
            if (this.staticLink.$elements instanceof Package) {
                obj = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Alerts.Lit387), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj = this.staticLink.$elements;
            }
            SimpleSymbol simpleSymbol = Alerts.Lit88;
            SimpleSymbol simpleSymbol2 = Alerts.Lit388;
            ModuleMethod moduleMethod4 = runtime.yail$Mndictionary$Mnrecursive$Mnlookup;
            Object callYailPrimitive = runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("properties", NotificationCompat.CATEGORY_EVENT), Alerts.Lit389, "make a list");
            if (this.$entireAlert instanceof Package) {
                obj2 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Alerts.Lit390), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj2 = this.$entireAlert;
            }
            Object callYailPrimitive2 = runtime.callYailPrimitive(moduleMethod4, LList.list3(callYailPrimitive, obj2, "Unknown Alert"), Alerts.Lit391, "dictionary recursive lookup");
            ModuleMethod moduleMethod5 = runtime.yail$Mndictionary$Mnrecursive$Mnlookup;
            Object callYailPrimitive3 = runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.list2("properties", "headline"), Alerts.Lit392, "make a list");
            if (this.$entireAlert instanceof Package) {
                obj3 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Alerts.Lit390), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj3 = this.$entireAlert;
            }
            runtime.callYailPrimitive(moduleMethod3, LList.list2(obj, runtime.callComponentMethod(simpleSymbol, simpleSymbol2, LList.list3(callYailPrimitive2, runtime.callYailPrimitive(moduleMethod5, LList.list3(callYailPrimitive3, obj3, "No headline provided."), Alerts.Lit393, "dictionary recursive lookup"), ""), Alerts.Lit394)), Alerts.Lit395, "add items to list");
            ModuleMethod moduleMethod6 = runtime.yail$Mnlist$Mnadd$Mnto$Mnlist$Ex;
            Object lookupGlobalVarInCurrentFormEnvironment = runtime.lookupGlobalVarInCurrentFormEnvironment(Alerts.Lit10, runtime.$Stthe$Mnnull$Mnvalue$St);
            if (this.$entireAlert instanceof Package) {
                obj4 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Alerts.Lit390), " is not bound in the current context"), "Unbound Variable");
            } else {
                obj4 = this.$entireAlert;
            }
            return runtime.callYailPrimitive(moduleMethod6, LList.list2(lookupGlobalVarInCurrentFormEnvironment, obj4), Alerts.Lit396, "add items to list");
        }
    }

    public Object Web1$GotText(Object $url, Object $responseCode, Object $responseType, Object $responseContent) {
        Object obj;
        frame0 closureEnv = new frame0();
        runtime.sanitizeComponentData($url);
        runtime.sanitizeComponentData($responseCode);
        runtime.sanitizeComponentData($responseType);
        Object $responseContent2 = runtime.sanitizeComponentData($responseContent);
        runtime.setThisForm();
        SimpleSymbol simpleSymbol = Lit29;
        SimpleSymbol simpleSymbol2 = Lit379;
        if ($responseContent2 instanceof Package) {
            $responseContent2 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit380), " is not bound in the current context"), "Unbound Variable");
        }
        Object $responseContent3 = runtime.callComponentMethod(simpleSymbol, simpleSymbol2, LList.list1($responseContent2), Lit381);
        closureEnv.$elements = runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.Empty, LList.Empty, "make a list");
        ModuleMethod moduleMethod = closureEnv.proc$Fn89;
        ModuleMethod moduleMethod2 = closureEnv.proc$Fn89;
        ModuleMethod moduleMethod3 = runtime.yail$Mndictionary$Mnlookup;
        if ($responseContent3 instanceof Package) {
            $responseContent3 = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit380), " is not bound in the current context"), "Unbound Variable");
        }
        runtime.yailForEach(moduleMethod2, runtime.callYailPrimitive(moduleMethod3, LList.list3("features", $responseContent3, runtime.callYailPrimitive(runtime.make$Mnyail$Mnlist, LList.Empty, LList.Empty, "make a list")), Lit402, "dictionary lookup"));
        ModuleMethod moduleMethod4 = runtime.yail$Mnlist$Mnempty$Qu;
        if (closureEnv.$elements instanceof Package) {
            obj = runtime.signalRuntimeError(strings.stringAppend("The variable ", runtime.getDisplayRepresentation(Lit387), " is not bound in the current context"), "Unbound Variable");
        } else {
            obj = closureEnv.$elements;
        }
        if (runtime.callYailPrimitive(moduleMethod4, LList.list1(obj), Lit403, "is list empty?") != Boolean.FALSE) {
            SimpleSymbol simpleSymbol3 = Lit55;
            SimpleSymbol simpleSymbol4 = Lit40;
            ModuleMethod moduleMethod5 = strings.string$Mnappend;
            Pair list1 = LList.list1("No alerts for ");
            LList.chain4(list1, runtime.callComponentMethod(Lit32, Lit37, LList.list2("filter", "[none]"), Lit404), " county in ", runtime.callComponentMethod(Lit32, Lit37, LList.list2("state", "[none]"), Lit405), ".");
            runtime.setAndCoerceProperty$Ex(simpleSymbol3, simpleSymbol4, runtime.callYailPrimitive(moduleMethod5, list1, Lit406, "join"), Lit19);
            runtime.setAndCoerceProperty$Ex(Lit88, Lit87, Boolean.FALSE, Lit15);
            return runtime.setAndCoerceProperty$Ex(Lit55, Lit87, Boolean.TRUE, Lit15);
        }
        runtime.setAndCoerceProperty$Ex(Lit88, Lit407, runtime.lookupGlobalVarInCurrentFormEnvironment(Lit10, runtime.$Stthe$Mnnull$Mnvalue$St), Lit31);
        runtime.setAndCoerceProperty$Ex(Lit88, Lit87, Boolean.TRUE, Lit15);
        return runtime.setAndCoerceProperty$Ex(Lit55, Lit87, Boolean.FALSE, Lit15);
    }

    public String getSimpleName(Object object) {
        return object.getClass().getSimpleName();
    }

    public void onCreate(Bundle icicle) {
        AppInventorCompatActivity.setClassicModeFromYail(false);
        super.onCreate(icicle);
    }

    public void androidLogForm(Object message) {
    }

    public void addToFormEnvironment(Symbol name2, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name2, this.form$Mnenvironment, object));
        this.form$Mnenvironment.put(name2, object);
    }

    public Object lookupInFormEnvironment(Symbol name2, Object default$Mnvalue) {
        boolean x = ((this.form$Mnenvironment == null ? 1 : 0) + 1) & true;
        if (x) {
            if (!this.form$Mnenvironment.isBound(name2)) {
                return default$Mnvalue;
            }
        } else if (!x) {
            return default$Mnvalue;
        }
        return this.form$Mnenvironment.get(name2);
    }

    public boolean isBoundInFormEnvironment(Symbol name2) {
        return this.form$Mnenvironment.isBound(name2);
    }

    public void addToGlobalVarEnvironment(Symbol name2, Object object) {
        androidLogForm(Format.formatToString(0, "Adding ~A to env ~A with value ~A", name2, this.global$Mnvar$Mnenvironment, object));
        this.global$Mnvar$Mnenvironment.put(name2, object);
    }

    public void addToEvents(Object component$Mnname, Object event$Mnname) {
        this.events$Mnto$Mnregister = lists.cons(lists.cons(component$Mnname, event$Mnname), this.events$Mnto$Mnregister);
    }

    public void addToComponents(Object container$Mnname, Object component$Mntype, Object component$Mnname, Object init$Mnthunk) {
        this.components$Mnto$Mncreate = lists.cons(LList.list4(container$Mnname, component$Mntype, component$Mnname, init$Mnthunk), this.components$Mnto$Mncreate);
    }

    public void addToGlobalVars(Object var, Object val$Mnthunk) {
        this.global$Mnvars$Mnto$Mncreate = lists.cons(LList.list2(var, val$Mnthunk), this.global$Mnvars$Mnto$Mncreate);
    }

    public void addToFormDoAfterCreation(Object thunk) {
        this.form$Mndo$Mnafter$Mncreation = lists.cons(thunk, this.form$Mndo$Mnafter$Mncreation);
    }

    public void sendError(Object error) {
        RetValManager.sendError(error == null ? null : error.toString());
    }

    public void processException(Object ex) {
        Object apply1 = Scheme.applyToArgs.apply1(GetNamedPart.getNamedPart.apply2(ex, Lit1));
        RuntimeErrorAlert.alert(this, apply1 == null ? null : apply1.toString(), ex instanceof YailRuntimeError ? ((YailRuntimeError) ex).getErrorType() : "Runtime Error", "End Application");
    }

    public boolean dispatchEvent(Component componentObject, String registeredComponentName, String eventName, Object[] args) {
        boolean x;
        SimpleSymbol registeredObject = misc.string$To$Symbol(registeredComponentName);
        if (!isBoundInFormEnvironment(registeredObject)) {
            EventDispatcher.unregisterEventForDelegation(this, registeredComponentName, eventName);
            return false;
        } else if (lookupInFormEnvironment(registeredObject) != componentObject) {
            return false;
        } else {
            try {
                Scheme.apply.apply2(lookupHandler(registeredComponentName, eventName), LList.makeList(args, 0));
                return true;
            } catch (StopBlocksExecution e) {
                return false;
            } catch (PermissionException exception) {
                exception.printStackTrace();
                if (this == componentObject) {
                    x = true;
                } else {
                    x = false;
                }
                if (!x ? x : IsEqual.apply(eventName, "PermissionNeeded")) {
                    processException(exception);
                } else {
                    PermissionDenied(componentObject, eventName, exception.getPermissionNeeded());
                }
                return false;
            } catch (Throwable exception2) {
                androidLogForm(exception2.getMessage());
                exception2.printStackTrace();
                processException(exception2);
                return false;
            }
        }
    }

    public void dispatchGenericEvent(Component componentObject, String eventName, boolean notAlreadyHandled, Object[] args) {
        Boolean bool;
        boolean x = true;
        Object handler = lookupInFormEnvironment(misc.string$To$Symbol(strings.stringAppend("any$", getSimpleName(componentObject), "$", eventName)));
        if (handler != Boolean.FALSE) {
            try {
                Apply apply = Scheme.apply;
                if (notAlreadyHandled) {
                    bool = Boolean.TRUE;
                } else {
                    bool = Boolean.FALSE;
                }
                apply.apply2(handler, lists.cons(componentObject, lists.cons(bool, LList.makeList(args, 0))));
            } catch (StopBlocksExecution e) {
            } catch (PermissionException exception) {
                exception.printStackTrace();
                if (this != componentObject) {
                    x = false;
                }
                if (!x ? x : IsEqual.apply(eventName, "PermissionNeeded")) {
                    processException(exception);
                } else {
                    PermissionDenied(componentObject, eventName, exception.getPermissionNeeded());
                }
            } catch (Throwable exception2) {
                androidLogForm(exception2.getMessage());
                exception2.printStackTrace();
                processException(exception2);
            }
        }
    }

    public Object lookupHandler(Object componentName, Object eventName) {
        String str = null;
        String obj = componentName == null ? null : componentName.toString();
        if (eventName != null) {
            str = eventName.toString();
        }
        return lookupInFormEnvironment(misc.string$To$Symbol(EventDispatcher.makeFullEventName(obj, str)));
    }

    public void $define() {
        Object reverse;
        Object obj;
        Object reverse2;
        Object obj2;
        Object obj3;
        Object var;
        Object component$Mnname;
        Object obj4;
        Language.setDefaults(Scheme.getInstance());
        try {
            run();
        } catch (Exception exception) {
            androidLogForm(exception.getMessage());
            processException(exception);
        }
        Alerts = this;
        addToFormEnvironment(Lit0, this);
        Object obj5 = this.events$Mnto$Mnregister;
        while (obj5 != LList.Empty) {
            try {
                Pair arg0 = (Pair) obj5;
                Object event$Mninfo = arg0.getCar();
                Object apply1 = lists.car.apply1(event$Mninfo);
                String obj6 = apply1 == null ? null : apply1.toString();
                Object apply12 = lists.cdr.apply1(event$Mninfo);
                EventDispatcher.registerEventForDelegation(this, obj6, apply12 == null ? null : apply12.toString());
                obj5 = arg0.getCdr();
            } catch (ClassCastException e) {
                throw new WrongType(e, "arg0", -2, obj5);
            }
        }
        try {
            LList components = lists.reverse(this.components$Mnto$Mncreate);
            addToGlobalVars(Lit2, lambda$Fn1);
            reverse = lists.reverse(this.form$Mndo$Mnafter$Mncreation);
            while (reverse != LList.Empty) {
                Pair arg02 = (Pair) reverse;
                misc.force(arg02.getCar());
                reverse = arg02.getCdr();
            }
            obj = components;
            while (obj != LList.Empty) {
                Pair arg03 = (Pair) obj;
                Object component$Mninfo = arg03.getCar();
                component$Mnname = lists.caddr.apply1(component$Mninfo);
                lists.cadddr.apply1(component$Mninfo);
                Object component$Mnobject = Invoke.make.apply2(lists.cadr.apply1(component$Mninfo), lookupInFormEnvironment((Symbol) lists.car.apply1(component$Mninfo)));
                SlotSet.set$Mnfield$Ex.apply3(this, component$Mnname, component$Mnobject);
                addToFormEnvironment((Symbol) component$Mnname, component$Mnobject);
                obj = arg03.getCdr();
            }
            reverse2 = lists.reverse(this.global$Mnvars$Mnto$Mncreate);
            while (reverse2 != LList.Empty) {
                Pair arg04 = (Pair) reverse2;
                Object var$Mnval = arg04.getCar();
                var = lists.car.apply1(var$Mnval);
                addToGlobalVarEnvironment((Symbol) var, Scheme.applyToArgs.apply1(lists.cadr.apply1(var$Mnval)));
                reverse2 = arg04.getCdr();
            }
            LList component$Mndescriptors = components;
            obj2 = component$Mndescriptors;
            while (obj2 != LList.Empty) {
                Pair arg05 = (Pair) obj2;
                Object component$Mninfo2 = arg05.getCar();
                lists.caddr.apply1(component$Mninfo2);
                Object init$Mnthunk = lists.cadddr.apply1(component$Mninfo2);
                if (init$Mnthunk != Boolean.FALSE) {
                    Scheme.applyToArgs.apply1(init$Mnthunk);
                }
                obj2 = arg05.getCdr();
            }
            obj3 = component$Mndescriptors;
            while (obj3 != LList.Empty) {
                Pair arg06 = (Pair) obj3;
                Object component$Mninfo3 = arg06.getCar();
                Object component$Mnname2 = lists.caddr.apply1(component$Mninfo3);
                lists.cadddr.apply1(component$Mninfo3);
                callInitialize(SlotGet.field.apply2(this, component$Mnname2));
                obj3 = arg06.getCdr();
            }
        } catch (ClassCastException e2) {
            throw new WrongType(e2, "arg0", -2, obj3);
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "arg0", -2, obj2);
        } catch (ClassCastException e4) {
            throw new WrongType(e4, "add-to-global-var-environment", 0, var);
        } catch (ClassCastException e5) {
            throw new WrongType(e5, "arg0", -2, reverse2);
        } catch (ClassCastException e6) {
            throw new WrongType(e6, "add-to-form-environment", 0, component$Mnname);
        } catch (ClassCastException e7) {
            throw new WrongType(e7, "lookup-in-form-environment", 0, obj4);
        } catch (ClassCastException e8) {
            throw new WrongType(e8, "arg0", -2, obj);
        } catch (ClassCastException e9) {
            throw new WrongType(e9, "arg0", -2, reverse);
        } catch (YailRuntimeError exception2) {
            processException(exception2);
        }
    }

    public static SimpleSymbol lambda1symbolAppend$V(Object[] argsArray) {
        LList symbols = LList.makeList(argsArray, 0);
        Apply apply = Scheme.apply;
        ModuleMethod moduleMethod = strings.string$Mnappend;
        Object obj = LList.Empty;
        LList lList = symbols;
        while (lList != LList.Empty) {
            try {
                Pair arg0 = (Pair) lList;
                Object arg02 = arg0.getCdr();
                Object car = arg0.getCar();
                try {
                    obj = Pair.make(misc.symbol$To$String((Symbol) car), obj);
                    lList = arg02;
                } catch (ClassCastException e) {
                    throw new WrongType(e, "symbol->string", 1, car);
                }
            } catch (ClassCastException e2) {
                throw new WrongType(e2, "arg0", -2, lList);
            }
        }
        Object apply2 = apply.apply2(moduleMethod, LList.reverseInPlace(obj));
        try {
            return misc.string$To$Symbol((CharSequence) apply2);
        } catch (ClassCastException e3) {
            throw new WrongType(e3, "string->symbol", 1, apply2);
        }
    }

    static Object lambda2() {
        return null;
    }
}
