package net.kibotu.android.deviceinfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final public class ReflectionHelper {

    private ReflectionHelper() throws IllegalAccessException {
        throw new IllegalAccessException("static class");
    }

    public static String getSystemProperty(final String propName) {
        Class<?> clsSystemProperties = tryClassForName("android.os.SystemProperties");
        Method mtdGet = tryGetMethod(clsSystemProperties, "get", String.class);
        return tryInvoke(mtdGet, null, propName);
    }

    public static Class<?> tryClassForName(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            Logger.e(e);
            return null;
        }
    }

    public static Method tryGetMethod(final Class<?> cls, final String name, final Class<?>... parameterTypes) {
        try {
            return cls.getDeclaredMethod(name, parameterTypes);
        } catch (final Exception e) {
            Logger.e(e);
            return null;
        }
    }

    public static <T> T tryInvoke(final Method m, final Object object, final Object... params) {
        try {
            return (T) m.invoke(object, params);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (final Exception e) {
            Logger.e(e);
            return null;
        }
    }

    public static <T> T get(final Class<?> cls, final String method, final Object target, final Object... params) {
        Method m = tryGetMethod(cls, method);
        return tryInvoke(m, target, params);
    }
}
