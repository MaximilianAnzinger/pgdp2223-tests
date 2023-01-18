package pgdp.networking;

import java.lang.reflect.*;
import java.util.Arrays;

public class ReflectUtils {
    public static Method getMethod(Object object, String name, Class<?>... params) {
        Method method;
        try {
            method = object.getClass().getDeclaredMethod(name, params);
        } catch (NoSuchMethodException e) {
            String paramsString = String.join(", ", Arrays.stream(params).map(Class::getName).toArray(String[]::new));
            throw new RuntimeException("Method `" + name + "(" + paramsString + ")` was not found in " + object.getClass().getName() + ".");
        }
        method.setAccessible(true);
        return method;
    }

    public static void setField(Object object, String field, Object value) {
        Field f = getField(object.getClass(), field);
        try {
            f.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static<T> T getField(Object object, String name) {
        Field field = getField(object.getClass(), name);
        try {
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getFieldInt(Object object, String name) {
        Field field = getField(object.getClass(), name);
        try {
            return field.getInt(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        Field field;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("The `" + name + "` attribute could not be found in your implementation of " + clazz.getName() + ".");
        }
        field.setAccessible(true);
        return field;
    }
}
