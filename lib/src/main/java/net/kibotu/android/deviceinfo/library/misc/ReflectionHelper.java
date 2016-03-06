package net.kibotu.android.deviceinfo.library.misc;

import java.lang.reflect.Field;
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
            e.printStackTrace();
            return null;
        }
    }

    public static Method tryGetMethod(final Class<?> cls, final String name, final Class<?>... parameterTypes) {
        try {
            return cls.getDeclaredMethod(name, parameterTypes);
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T tryInvoke(final Method m, final Object object, final Object... params) {
        try {
            return (T) m.invoke(object, params);
        } catch (final InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T get(final Class<?> cls, final String method, final Object target, final Object... params) {
        Method m = tryGetMethod(cls, method);
        return tryInvoke(m, target, params);
    }

    public static <T> T getPublicStaticField(final Class<?> cls, final String method) {

        T result = null;

        try {
            final Field field = cls.getField(method);
            result = (T) field.get(null);
        } catch (final NoSuchFieldException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static <T> T tryInvokePublicStatic(String cls, String method, final ReflectionHelperParamater params) {
        T t = null;
        try {
            final Method m = tryClassForName(cls).getMethod(method, params.types);
            t = (T) m.invoke(null, params.values);
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static Object contruct(final String cls) {
        try {
            return ReflectionHelper.tryClassForName(cls).getConstructor(String.class).newInstance("Throwable");
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void tryInvoke(final Class cls, final Object object, final String method, final ReflectionHelperParamater params) {
        Method m = ReflectionHelper.tryGetMethod(cls, method, params.types);
        try {
            m.invoke(object, params.values);
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Object contruct(final Class cls, final ReflectionHelperParamater params) {
        try {
            return cls.getConstructor(params.types).newInstance(params.values);
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void tryInvoke(final Class cls, final Object obj, final String method) {
        tryInvoke(tryGetMethod(cls, method, null), obj);
    }

    final static public class ReflectionHelperParamater {
        public final Class<?>[] types;
        public final Object[] values;

        public ReflectionHelperParamater(final Class<?>[] types, final Object[] values) {
            this.types = types;
            this.values = values;
        }
    }
}