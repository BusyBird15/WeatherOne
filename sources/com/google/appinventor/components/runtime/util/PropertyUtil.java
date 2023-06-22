package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.SimplePropertyCopier;
import com.google.appinventor.components.runtime.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PropertyUtil {
    public static Component copyComponentProperties(Component source, Component target) throws Throwable {
        if (!source.getClass().equals(target.getClass())) {
            throw new IllegalArgumentException("Source and target classes must be identical");
        }
        Class componentClass = source.getClass();
        for (Method componentMethod : componentClass.getMethods()) {
            if (componentMethod.isAnnotationPresent(SimpleProperty.class) && componentMethod.getParameterTypes().length == 1) {
                Method propertySetterMethod = componentMethod;
                try {
                    String propertyName = propertySetterMethod.getName();
                    Method propertyCopierMethod = getPropertyCopierMethod("Copy" + propertyName, componentClass);
                    if (propertyCopierMethod != null) {
                        propertyCopierMethod.invoke(target, new Object[]{source});
                    } else {
                        Method propertyGetterMethod = componentClass.getMethod(propertyName, new Class[0]);
                        Class propertySetterParameterType = propertySetterMethod.getParameterTypes()[0];
                        if (propertyGetterMethod.isAnnotationPresent(SimpleProperty.class) && propertySetterParameterType.isAssignableFrom(propertyGetterMethod.getReturnType())) {
                            propertySetterMethod.invoke(target, new Object[]{propertyGetterMethod.invoke(source, new Object[0])});
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e2) {
                    throw e2.getCause();
                }
            }
        }
        return target;
    }

    private static Method getPropertyCopierMethod(String copierMethodName, Class componentClass) {
        do {
            try {
                Method propertyCopierMethod = componentClass.getMethod(copierMethodName, new Class[]{componentClass});
                if (propertyCopierMethod.isAnnotationPresent(SimplePropertyCopier.class)) {
                    return propertyCopierMethod;
                }
            } catch (NoSuchMethodException e) {
            }
            componentClass = componentClass.getSuperclass();
        } while (componentClass != null);
        return null;
    }
}
