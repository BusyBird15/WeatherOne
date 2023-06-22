package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.common.OptionList;
import com.google.appinventor.components.runtime.errors.DispatchableError;
import gnu.mapping.Symbol;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class TypeUtil {
    private TypeUtil() {
    }

    public static <T> T cast(Object o, Class<T> tClass, String expected) {
        if (o == null) {
            return null;
        }
        if (tClass.isInstance(o)) {
            return tClass.cast(o);
        }
        throw new DispatchableError(ErrorMessages.ERROR_INVALID_TYPE, o.getClass().getSimpleName(), expected);
    }

    public static <T> T castNotNull(Object o, Class<T> tClass, String expected) {
        if (o != null) {
            return cast(o, tClass, expected);
        }
        throw new DispatchableError(ErrorMessages.ERROR_INVALID_TYPE, "null", expected);
    }

    public static <T> OptionList<T> castToEnum(T value, Symbol className) {
        int i = 0;
        String classNameStr = stripEnumSuffix(className.getName());
        try {
            Class<?> clazz = Class.forName(classNameStr);
            if (!OptionList.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(classNameStr + " does not identify an OptionList type.");
            }
            Method[] methods = clazz.getMethods();
            int length = methods.length;
            while (i < length) {
                Method m = methods[i];
                if (!"fromUnderlyingValue".equals(m.getName()) || !m.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                    i++;
                } else {
                    return (OptionList) m.invoke(clazz, new Object[]{value});
                }
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (InvocationTargetException e2) {
            return null;
        } catch (IllegalAccessException e3) {
            return null;
        }
    }

    private static String stripEnumSuffix(String className) {
        if (className.endsWith("Enum")) {
            return className.substring(0, className.length() - 4);
        }
        return className;
    }
}
