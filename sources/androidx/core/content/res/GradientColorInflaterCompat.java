package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.core.R;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
final class GradientColorInflaterCompat {
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;

    private GradientColorInflaterCompat() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:6:0x0010  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0018  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static android.graphics.Shader createFromXml(@androidx.annotation.NonNull android.content.res.Resources r4, @androidx.annotation.NonNull org.xmlpull.v1.XmlPullParser r5, @androidx.annotation.Nullable android.content.res.Resources.Theme r6) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r3 = 2
            android.util.AttributeSet r0 = android.util.Xml.asAttributeSet(r5)
        L_0x0005:
            int r1 = r5.next()
            if (r1 == r3) goto L_0x000e
            r2 = 1
            if (r1 != r2) goto L_0x0005
        L_0x000e:
            if (r1 == r3) goto L_0x0018
            org.xmlpull.v1.XmlPullParserException r2 = new org.xmlpull.v1.XmlPullParserException
            java.lang.String r3 = "No start tag found"
            r2.<init>(r3)
            throw r2
        L_0x0018:
            android.graphics.Shader r2 = createFromXmlInner(r4, r5, r0, r6)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.GradientColorInflaterCompat.createFromXml(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.content.res.Resources$Theme):android.graphics.Shader");
    }

    static Shader createFromXmlInner(@NonNull Resources resources, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) throws IOException, XmlPullParserException {
        String name = parser.getName();
        if (!name.equals("gradient")) {
            throw new XmlPullParserException(parser.getPositionDescription() + ": invalid gradient color tag " + name);
        }
        TypedArray a = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R.styleable.GradientColor);
        float startX = TypedArrayUtils.getNamedFloat(a, parser, "startX", R.styleable.GradientColor_android_startX, 0.0f);
        float startY = TypedArrayUtils.getNamedFloat(a, parser, "startY", R.styleable.GradientColor_android_startY, 0.0f);
        float endX = TypedArrayUtils.getNamedFloat(a, parser, "endX", R.styleable.GradientColor_android_endX, 0.0f);
        float endY = TypedArrayUtils.getNamedFloat(a, parser, "endY", R.styleable.GradientColor_android_endY, 0.0f);
        float centerX = TypedArrayUtils.getNamedFloat(a, parser, "centerX", R.styleable.GradientColor_android_centerX, 0.0f);
        float centerY = TypedArrayUtils.getNamedFloat(a, parser, "centerY", R.styleable.GradientColor_android_centerY, 0.0f);
        int type = TypedArrayUtils.getNamedInt(a, parser, "type", R.styleable.GradientColor_android_type, 0);
        int startColor = TypedArrayUtils.getNamedColor(a, parser, "startColor", R.styleable.GradientColor_android_startColor, 0);
        boolean hasCenterColor = TypedArrayUtils.hasAttribute(parser, "centerColor");
        int centerColor = TypedArrayUtils.getNamedColor(a, parser, "centerColor", R.styleable.GradientColor_android_centerColor, 0);
        int endColor = TypedArrayUtils.getNamedColor(a, parser, "endColor", R.styleable.GradientColor_android_endColor, 0);
        int tileMode = TypedArrayUtils.getNamedInt(a, parser, "tileMode", R.styleable.GradientColor_android_tileMode, 0);
        float gradientRadius = TypedArrayUtils.getNamedFloat(a, parser, "gradientRadius", R.styleable.GradientColor_android_gradientRadius, 0.0f);
        a.recycle();
        ColorStops colorStops = checkColors(inflateChildElements(resources, parser, attrs, theme), startColor, endColor, hasCenterColor, centerColor);
        switch (type) {
            case 1:
                if (gradientRadius > 0.0f) {
                    return new RadialGradient(centerX, centerY, gradientRadius, colorStops.mColors, colorStops.mOffsets, parseTileMode(tileMode));
                }
                throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
            case 2:
                return new SweepGradient(centerX, centerY, colorStops.mColors, colorStops.mOffsets);
            default:
                return new LinearGradient(startX, startY, endX, endY, colorStops.mColors, colorStops.mOffsets, parseTileMode(tileMode));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0071, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(r16.getPositionDescription() + ": <item> tag requires a 'color' attribute and a 'offset' " + "attribute!");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static androidx.core.content.res.GradientColorInflaterCompat.ColorStops inflateChildElements(@androidx.annotation.NonNull android.content.res.Resources r15, @androidx.annotation.NonNull org.xmlpull.v1.XmlPullParser r16, @androidx.annotation.NonNull android.util.AttributeSet r17, @androidx.annotation.Nullable android.content.res.Resources.Theme r18) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            int r12 = r16.getDepth()
            int r8 = r12 + 1
            java.util.ArrayList r10 = new java.util.ArrayList
            r12 = 20
            r10.<init>(r12)
            java.util.ArrayList r4 = new java.util.ArrayList
            r12 = 20
            r4.<init>(r12)
        L_0x0014:
            int r11 = r16.next()
            r12 = 1
            if (r11 == r12) goto L_0x0092
            int r5 = r16.getDepth()
            if (r5 >= r8) goto L_0x0024
            r12 = 3
            if (r11 == r12) goto L_0x0092
        L_0x0024:
            r12 = 2
            if (r11 != r12) goto L_0x0014
            if (r5 > r8) goto L_0x0014
            java.lang.String r12 = r16.getName()
            java.lang.String r13 = "item"
            boolean r12 = r12.equals(r13)
            if (r12 == 0) goto L_0x0014
            int[] r12 = androidx.core.R.styleable.GradientColorItem
            r0 = r18
            r1 = r17
            android.content.res.TypedArray r2 = androidx.core.content.res.TypedArrayUtils.obtainAttributes(r15, r0, r1, r12)
            int r12 = androidx.core.R.styleable.GradientColorItem_android_color
            boolean r6 = r2.hasValue(r12)
            int r12 = androidx.core.R.styleable.GradientColorItem_android_offset
            boolean r7 = r2.hasValue(r12)
            if (r6 == 0) goto L_0x004f
            if (r7 != 0) goto L_0x0072
        L_0x004f:
            org.xmlpull.v1.XmlPullParserException r12 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = r16.getPositionDescription()
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r14 = ": <item> tag requires a 'color' attribute and a 'offset' "
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r14 = "attribute!"
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x0072:
            int r12 = androidx.core.R.styleable.GradientColorItem_android_color
            r13 = 0
            int r3 = r2.getColor(r12, r13)
            int r12 = androidx.core.R.styleable.GradientColorItem_android_offset
            r13 = 0
            float r9 = r2.getFloat(r12, r13)
            r2.recycle()
            java.lang.Integer r12 = java.lang.Integer.valueOf(r3)
            r4.add(r12)
            java.lang.Float r12 = java.lang.Float.valueOf(r9)
            r10.add(r12)
            goto L_0x0014
        L_0x0092:
            int r12 = r4.size()
            if (r12 <= 0) goto L_0x009e
            androidx.core.content.res.GradientColorInflaterCompat$ColorStops r12 = new androidx.core.content.res.GradientColorInflaterCompat$ColorStops
            r12.<init>((java.util.List<java.lang.Integer>) r4, (java.util.List<java.lang.Float>) r10)
        L_0x009d:
            return r12
        L_0x009e:
            r12 = 0
            goto L_0x009d
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.content.res.GradientColorInflaterCompat.inflateChildElements(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):androidx.core.content.res.GradientColorInflaterCompat$ColorStops");
    }

    private static ColorStops checkColors(@Nullable ColorStops colorItems, @ColorInt int startColor, @ColorInt int endColor, boolean hasCenterColor, @ColorInt int centerColor) {
        if (colorItems != null) {
            return colorItems;
        }
        if (hasCenterColor) {
            return new ColorStops(startColor, centerColor, endColor);
        }
        return new ColorStops(startColor, endColor);
    }

    private static Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case 1:
                return Shader.TileMode.REPEAT;
            case 2:
                return Shader.TileMode.MIRROR;
            default:
                return Shader.TileMode.CLAMP;
        }
    }

    static final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(@NonNull List<Integer> colorsList, @NonNull List<Float> offsetsList) {
            int size = colorsList.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; i++) {
                this.mColors[i] = colorsList.get(i).intValue();
                this.mOffsets[i] = offsetsList.get(i).floatValue();
            }
        }

        ColorStops(@ColorInt int startColor, @ColorInt int endColor) {
            this.mColors = new int[]{startColor, endColor};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(@ColorInt int startColor, @ColorInt int centerColor, @ColorInt int endColor) {
            this.mColors = new int[]{startColor, centerColor, endColor};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }
    }
}
