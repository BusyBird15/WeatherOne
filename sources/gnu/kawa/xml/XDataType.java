package gnu.kawa.xml;

import com.google.appinventor.components.common.PropertyTypeConstants;
import gnu.bytecode.ClassType;
import gnu.bytecode.CodeAttr;
import gnu.bytecode.Method;
import gnu.bytecode.Type;
import gnu.bytecode.Variable;
import gnu.expr.Compilation;
import gnu.expr.Declaration;
import gnu.expr.Expression;
import gnu.expr.Target;
import gnu.expr.TypeValue;
import gnu.kawa.reflect.InstanceOf;
import gnu.lists.Consumer;
import gnu.lists.SeqPosition;
import gnu.mapping.Procedure;
import gnu.mapping.Values;
import gnu.math.DateTime;
import gnu.math.Duration;
import gnu.math.IntNum;
import gnu.math.RealNum;
import gnu.math.Unit;
import gnu.text.Path;
import gnu.text.Printable;
import gnu.text.URIPath;
import gnu.xml.TextUtils;
import java.math.BigDecimal;

public class XDataType extends Type implements TypeValue {
    public static final int ANY_ATOMIC_TYPE_CODE = 3;
    public static final int ANY_SIMPLE_TYPE_CODE = 2;
    public static final int ANY_URI_TYPE_CODE = 33;
    public static final int BASE64_BINARY_TYPE_CODE = 34;
    public static final int BOOLEAN_TYPE_CODE = 31;
    public static final int BYTE_TYPE_CODE = 11;
    public static final int DATE_TIME_TYPE_CODE = 20;
    public static final int DATE_TYPE_CODE = 21;
    public static final int DAY_TIME_DURATION_TYPE_CODE = 30;
    public static final BigDecimal DECIMAL_ONE = BigDecimal.valueOf(1);
    public static final int DECIMAL_TYPE_CODE = 4;
    public static final Double DOUBLE_ONE = makeDouble(1.0d);
    public static final int DOUBLE_TYPE_CODE = 19;
    public static final Double DOUBLE_ZERO = makeDouble(0.0d);
    public static final int DURATION_TYPE_CODE = 28;
    public static final int ENTITY_TYPE_CODE = 47;
    public static final Float FLOAT_ONE = makeFloat(1.0f);
    public static final int FLOAT_TYPE_CODE = 18;
    public static final Float FLOAT_ZERO = makeFloat(0.0f);
    public static final int G_DAY_TYPE_CODE = 26;
    public static final int G_MONTH_DAY_TYPE_CODE = 25;
    public static final int G_MONTH_TYPE_CODE = 27;
    public static final int G_YEAR_MONTH_TYPE_CODE = 23;
    public static final int G_YEAR_TYPE_CODE = 24;
    public static final int HEX_BINARY_TYPE_CODE = 35;
    public static final int IDREF_TYPE_CODE = 46;
    public static final int ID_TYPE_CODE = 45;
    public static final int INTEGER_TYPE_CODE = 5;
    public static final int INT_TYPE_CODE = 9;
    public static final int LANGUAGE_TYPE_CODE = 41;
    public static final int LONG_TYPE_CODE = 8;
    public static final int NAME_TYPE_CODE = 43;
    public static final int NCNAME_TYPE_CODE = 44;
    public static final int NEGATIVE_INTEGER_TYPE_CODE = 7;
    public static final int NMTOKEN_TYPE_CODE = 42;
    public static final int NONNEGATIVE_INTEGER_TYPE_CODE = 12;
    public static final int NON_POSITIVE_INTEGER_TYPE_CODE = 6;
    public static final int NORMALIZED_STRING_TYPE_CODE = 39;
    public static final int NOTATION_TYPE_CODE = 36;
    public static final XDataType NotationType = new XDataType("NOTATION", ClassType.make("gnu.kawa.xml.Notation"), 36);
    public static final int POSITIVE_INTEGER_TYPE_CODE = 17;
    public static final int QNAME_TYPE_CODE = 32;
    public static final int SHORT_TYPE_CODE = 10;
    public static final int STRING_TYPE_CODE = 38;
    public static final int TIME_TYPE_CODE = 22;
    public static final int TOKEN_TYPE_CODE = 40;
    public static final int UNSIGNED_BYTE_TYPE_CODE = 16;
    public static final int UNSIGNED_INT_TYPE_CODE = 14;
    public static final int UNSIGNED_LONG_TYPE_CODE = 13;
    public static final int UNSIGNED_SHORT_TYPE_CODE = 15;
    public static final int UNTYPED_ATOMIC_TYPE_CODE = 37;
    public static final int UNTYPED_TYPE_CODE = 48;
    public static final int YEAR_MONTH_DURATION_TYPE_CODE = 29;
    public static final XDataType anyAtomicType = new XDataType("anyAtomicType", Type.objectType, 3);
    public static final XDataType anySimpleType = new XDataType("anySimpleType", Type.objectType, 2);
    public static final XDataType anyURIType = new XDataType("anyURI", ClassType.make("gnu.text.Path"), 33);
    public static final XDataType base64BinaryType = new XDataType("base64Binary", ClassType.make("gnu.kawa.xml.Base64Binary"), 34);
    public static final XDataType booleanType = new XDataType(PropertyTypeConstants.PROPERTY_TYPE_BOOLEAN, Type.booleanType, 31);
    public static final XDataType dayTimeDurationType = new XDataType("dayTimeDuration", ClassType.make("gnu.math.Duration"), 30);
    public static final XDataType decimalType = new XDataType("decimal", ClassType.make("java.lang.Number"), 4);
    public static final XDataType doubleType = new XDataType("double", ClassType.make("java.lang.Double"), 19);
    public static final XDataType durationType = new XDataType("duration", ClassType.make("gnu.math.Duration"), 28);
    public static final XDataType floatType = new XDataType(PropertyTypeConstants.PROPERTY_TYPE_FLOAT, ClassType.make("java.lang.Float"), 18);
    public static final XDataType hexBinaryType = new XDataType("hexBinary", ClassType.make("gnu.kawa.xml.HexBinary"), 35);
    public static final XDataType stringStringType = new XDataType("String", ClassType.make("java.lang.String"), 38);
    public static final XDataType stringType = new XDataType(PropertyTypeConstants.PROPERTY_TYPE_STRING, ClassType.make("java.lang.CharSequence"), 38);
    public static final XDataType untypedAtomicType = new XDataType(PropertyTypeConstants.PROPERTY_TYPE_STRING, ClassType.make("gnu.kawa.xml.UntypedAtomic"), 37);
    public static final XDataType untypedType = new XDataType("untyped", Type.objectType, 48);
    public static final XDataType yearMonthDurationType = new XDataType("yearMonthDuration", ClassType.make("gnu.math.Duration"), 29);
    XDataType baseType;
    Type implementationType;
    Object name;
    int typeCode;

    public XDataType(Object name2, Type implementationType2, int typeCode2) {
        super(implementationType2);
        this.name = name2;
        if (name2 != null) {
            setName(name2.toString());
        }
        this.implementationType = implementationType2;
        this.typeCode = typeCode2;
    }

    public Class getReflectClass() {
        return this.implementationType.getReflectClass();
    }

    public Type getImplementationType() {
        return this.implementationType;
    }

    public void emitCoerceFromObject(CodeAttr code) {
        Compilation.getCurrent().compileConstant(this, Target.pushObject);
        Method meth = ClassType.make("gnu.kawa.xml.XDataType").getDeclaredMethod("coerceFromObject", 1);
        code.emitSwap();
        code.emitInvokeVirtual(meth);
        this.implementationType.emitCoerceFromObject(code);
    }

    public void emitCoerceToObject(CodeAttr code) {
        if (this.typeCode == 31) {
            this.implementationType.emitCoerceToObject(code);
        } else {
            super.emitCoerceToObject(code);
        }
    }

    public void emitTestIf(Variable incoming, Declaration decl, Compilation comp) {
        CodeAttr code = comp.getCode();
        if (this.typeCode == 31) {
            if (incoming != null) {
                code.emitLoad(incoming);
            }
            Type.javalangBooleanType.emitIsInstance(code);
            code.emitIfIntNotZero();
            if (decl != null) {
                code.emitLoad(incoming);
                Type.booleanType.emitCoerceFromObject(code);
                decl.compileStore(comp);
                return;
            }
            return;
        }
        comp.compileConstant(this, Target.pushObject);
        if (incoming == null) {
            code.emitSwap();
        } else {
            code.emitLoad(incoming);
        }
        code.emitInvokeVirtual(Compilation.typeType.getDeclaredMethod("isInstance", 1));
        code.emitIfIntNotZero();
        if (decl != null) {
            code.emitLoad(incoming);
            emitCoerceFromObject(code);
            decl.compileStore(comp);
        }
    }

    public Expression convertValue(Expression value) {
        return null;
    }

    public boolean isInstance(Object obj) {
        boolean z = false;
        switch (this.typeCode) {
            case 2:
                if ((obj instanceof SeqPosition) || (obj instanceof Nodes)) {
                    return false;
                }
                return true;
            case 3:
                if ((obj instanceof Values) || (obj instanceof SeqPosition)) {
                    return false;
                }
                return true;
            case 4:
                if ((obj instanceof BigDecimal) || (obj instanceof IntNum)) {
                    z = true;
                }
                return z;
            case 18:
                return obj instanceof Float;
            case 19:
                return obj instanceof Double;
            case 28:
                return obj instanceof Duration;
            case 29:
                if (!(obj instanceof Duration) || ((Duration) obj).unit() != Unit.month) {
                    return false;
                }
                return true;
            case 30:
                return (obj instanceof Duration) && ((Duration) obj).unit() == Unit.second;
            case 31:
                return obj instanceof Boolean;
            case 33:
                return obj instanceof Path;
            case 37:
                return obj instanceof UntypedAtomic;
            case 38:
                return obj instanceof CharSequence;
            case 48:
                return true;
            default:
                return super.isInstance(obj);
        }
    }

    public void emitIsInstance(Variable incoming, Compilation comp, Target target) {
        InstanceOf.emitIsInstance(this, incoming, comp, target);
    }

    public String toString(Object value) {
        return value.toString();
    }

    public void print(Object value, Consumer out) {
        if (value instanceof Printable) {
            ((Printable) value).print(out);
        } else {
            out.write(toString(value));
        }
    }

    public boolean castable(Object value) {
        try {
            cast(value);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    /* Debug info: failed to restart local var, previous not found, register: 8 */
    public Object cast(Object value) {
        Object value2 = KNode.atomicValue(value);
        if (value2 instanceof UntypedAtomic) {
            if (this.typeCode == 37) {
                return value2;
            }
            return valueOf(value2.toString());
        } else if (value2 instanceof String) {
            return valueOf(value2.toString());
        } else {
            switch (this.typeCode) {
                case 4:
                    if (value2 instanceof BigDecimal) {
                        return value2;
                    }
                    if (value2 instanceof RealNum) {
                        return ((RealNum) value2).asBigDecimal();
                    }
                    if ((value2 instanceof Float) || (value2 instanceof Double)) {
                        return BigDecimal.valueOf(((Number) value2).doubleValue());
                    }
                    if (value2 instanceof Boolean) {
                        return cast(((Boolean) value2).booleanValue() ? IntNum.one() : IntNum.zero());
                    }
                    break;
                case 18:
                    if (value2 instanceof Float) {
                        return value2;
                    }
                    if (value2 instanceof Number) {
                        return makeFloat(((Number) value2).floatValue());
                    }
                    if (value2 instanceof Boolean) {
                        return ((Boolean) value2).booleanValue() ? FLOAT_ONE : FLOAT_ZERO;
                    }
                    break;
                case 19:
                    if (value2 instanceof Double) {
                        return value2;
                    }
                    if (value2 instanceof Number) {
                        return makeDouble(((Number) value2).doubleValue());
                    }
                    if (value2 instanceof Boolean) {
                        return ((Boolean) value2).booleanValue() ? DOUBLE_ONE : DOUBLE_ZERO;
                    }
                    break;
                case 20:
                case 21:
                case 22:
                    if (value2 instanceof DateTime) {
                        return ((DateTime) value2).cast(XTimeType.components(((XTimeType) this).typeCode));
                    }
                    break;
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                    if (value2 instanceof DateTime) {
                        int dstMask = XTimeType.components(((XTimeType) this).typeCode);
                        DateTime dt = (DateTime) value2;
                        int srcMask = dt.components();
                        if (dstMask == srcMask || (srcMask & 14) == 14) {
                            return dt.cast(dstMask);
                        }
                        throw new ClassCastException();
                    }
                    break;
                case 28:
                    return castToDuration(value2, Unit.duration);
                case 29:
                    return castToDuration(value2, Unit.month);
                case 30:
                    return castToDuration(value2, Unit.second);
                case 31:
                    if (value2 instanceof Boolean) {
                        return ((Boolean) value2).booleanValue() ? Boolean.TRUE : Boolean.FALSE;
                    } else if (value2 instanceof Number) {
                        double d = ((Number) value2).doubleValue();
                        return (d == 0.0d || Double.isNaN(d)) ? Boolean.FALSE : Boolean.TRUE;
                    }
                    break;
                case 33:
                    return URIPath.makeURI(value2);
                case 34:
                    if (value2 instanceof BinaryObject) {
                        return new Base64Binary(((BinaryObject) value2).getBytes());
                    }
                    break;
                case 35:
                    break;
                case 37:
                    return new UntypedAtomic(TextUtils.stringValue(value2));
                case 38:
                    return TextUtils.asString(value2);
            }
            if (value2 instanceof BinaryObject) {
                return new HexBinary(((BinaryObject) value2).getBytes());
            }
            return coerceFromObject(value2);
        }
    }

    /* access modifiers changed from: package-private */
    public Duration castToDuration(Object value, Unit unit) {
        if (!(value instanceof Duration)) {
            return (Duration) coerceFromObject(value);
        }
        Duration dur = (Duration) value;
        if (dur.unit() == unit) {
            return dur;
        }
        int months = dur.getTotalMonths();
        long seconds = dur.getTotalSeconds();
        int nanos = dur.getNanoSecondsOnly();
        if (unit == Unit.second) {
            months = 0;
        }
        if (unit == Unit.month) {
            seconds = 0;
            nanos = 0;
        }
        return Duration.make(months, seconds, nanos, unit);
    }

    public Object coerceFromObject(Object obj) {
        if (isInstance(obj)) {
            return obj;
        }
        throw new ClassCastException("cannot cast " + obj + " to " + this.name);
    }

    public int compare(Type other) {
        if (this == other || ((this == stringStringType && other == stringType) || (this == stringType && other == stringStringType))) {
            return 0;
        }
        return this.implementationType.compare(other);
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00dc A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object valueOf(java.lang.String r6) {
        /*
            r5 = this;
            int r2 = r5.typeCode
            switch(r2) {
                case 4: goto L_0x00a5;
                case 18: goto L_0x007b;
                case 19: goto L_0x007b;
                case 28: goto L_0x00e3;
                case 29: goto L_0x00e9;
                case 30: goto L_0x00ef;
                case 31: goto L_0x0032;
                case 33: goto L_0x0028;
                case 34: goto L_0x00f5;
                case 35: goto L_0x00fb;
                case 37: goto L_0x0022;
                case 38: goto L_0x0020;
                default: goto L_0x0005;
            }
        L_0x0005:
            java.lang.RuntimeException r2 = new java.lang.RuntimeException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "valueOf not implemented for "
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.Object r4 = r5.name
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        L_0x0020:
            r2 = r6
        L_0x0021:
            return r2
        L_0x0022:
            gnu.kawa.xml.UntypedAtomic r2 = new gnu.kawa.xml.UntypedAtomic
            r2.<init>(r6)
            goto L_0x0021
        L_0x0028:
            r2 = 1
            java.lang.String r2 = gnu.xml.TextUtils.replaceWhitespace(r6, r2)
            gnu.text.URIPath r2 = gnu.text.URIPath.makeURI(r2)
            goto L_0x0021
        L_0x0032:
            java.lang.String r6 = r6.trim()
            java.lang.String r2 = "true"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0046
            java.lang.String r2 = "1"
            boolean r2 = r6.equals(r2)
            if (r2 == 0) goto L_0x0049
        L_0x0046:
            java.lang.Boolean r2 = java.lang.Boolean.TRUE
            goto L_0x0021
        L_0x0049:
            java.lang.String r2 = "false"
            boolean r2 = r6.equals(r2)
            if (r2 != 0) goto L_0x0059
            java.lang.String r2 = "0"
            boolean r2 = r6.equals(r2)
            if (r2 == 0) goto L_0x005c
        L_0x0059:
            java.lang.Boolean r2 = java.lang.Boolean.FALSE
            goto L_0x0021
        L_0x005c:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "not a valid boolean: '"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r4 = "'"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        L_0x007b:
            java.lang.String r6 = r6.trim()
            java.lang.String r2 = "INF"
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L_0x0094
            java.lang.String r6 = "Infinity"
        L_0x0089:
            int r2 = r5.typeCode
            r3 = 18
            if (r2 != r3) goto L_0x009f
            java.lang.Float r2 = java.lang.Float.valueOf(r6)
            goto L_0x0021
        L_0x0094:
            java.lang.String r2 = "-INF"
            boolean r2 = r2.equals(r6)
            if (r2 == 0) goto L_0x0089
            java.lang.String r6 = "-Infinity"
            goto L_0x0089
        L_0x009f:
            java.lang.Double r2 = java.lang.Double.valueOf(r6)
            goto L_0x0021
        L_0x00a5:
            java.lang.String r6 = r6.trim()
            int r1 = r6.length()
        L_0x00ad:
            int r1 = r1 + -1
            if (r1 < 0) goto L_0x00dc
            char r0 = r6.charAt(r1)
            r2 = 101(0x65, float:1.42E-43)
            if (r0 == r2) goto L_0x00bd
            r2 = 69
            if (r0 != r2) goto L_0x00ad
        L_0x00bd:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "not a valid decimal: '"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r4 = "'"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        L_0x00dc:
            java.math.BigDecimal r2 = new java.math.BigDecimal
            r2.<init>(r6)
            goto L_0x0021
        L_0x00e3:
            gnu.math.Duration r2 = gnu.math.Duration.parseDuration(r6)
            goto L_0x0021
        L_0x00e9:
            gnu.math.Duration r2 = gnu.math.Duration.parseYearMonthDuration(r6)
            goto L_0x0021
        L_0x00ef:
            gnu.math.Duration r2 = gnu.math.Duration.parseDayTimeDuration(r6)
            goto L_0x0021
        L_0x00f5:
            gnu.kawa.xml.Base64Binary r2 = gnu.kawa.xml.Base64Binary.valueOf(r6)
            goto L_0x0021
        L_0x00fb:
            gnu.kawa.xml.HexBinary r2 = gnu.kawa.xml.HexBinary.valueOf(r6)
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.kawa.xml.XDataType.valueOf(java.lang.String):java.lang.Object");
    }

    public static Float makeFloat(float value) {
        return Float.valueOf(value);
    }

    public static Double makeDouble(double value) {
        return Double.valueOf(value);
    }

    public Procedure getConstructor() {
        return null;
    }
}
