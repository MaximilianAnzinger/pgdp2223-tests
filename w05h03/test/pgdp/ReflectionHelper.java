package pgdp;

import java.lang.reflect.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReflectionHelper {

    /**
     * InvokeMethod but for void methods.
     * @param obj source Object
     * @param name name
     * @param paramClasses parameter classes
     * @param params actual parameters for invoking
     * @return
     */
    public static void invokeVoidMethod(Object obj, String name, Class[] paramClasses, Object... params) {
        invokeMethod(Void.TYPE, obj, name, paramClasses, params);
    }

    /**
     * Gets method from object, asserts existence, invokes method,
     * asserts return type and automatically casts returned Object to the type specified.
     * @param returnType expected return type
     * @param obj source Object
     * @param name name
     * @param paramClasses parameter classes
     * @param params actual parameters for invoking
     * @param <T>
     * @return
     */
    public static <T> T invokeMethod(Class<T> returnType, Object obj, String name, Class[] paramClasses, Object... params) {
        Method m = getMethod(obj.getClass(), name, paramClasses);
        if(m.getReturnType() != returnType) {
            fail("Method " + obj.getClass().getName() + "." + name + " should return " + returnType.getName() + "!");
        } else {
            try {
                return (T) m.invoke(obj, params);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Gets method and asserts existence.
     * @param clazz source class
     * @param name name
     * @param params parameters
     * @return
     */
    public static Method getMethod(Class<?> clazz, String name, Class... params) {
        try {
            return clazz.getMethod(name, params);
        } catch (NoSuchMethodException e) {
            return fail("Method " + clazz.getName() + "." + name + " with parameters " + Arrays.toString(params) + " not implemented!");
        }
    }

    /**
     * Gets field from object, asserts existence, checks type and automatically casts it to the specified type.
     * @param obj source Object
     * @param type expected type
     * @param name name
     * @param <T>
     * @return
     */
    public static <T> T getField(Object obj, Class<T> type, String name) {
        try {
            Field f = obj.getClass().getDeclaredField(name);
            f.setAccessible(true);
            if(f.getType() != type) {
                fail("Field " + obj.getClass().getName() + "." + name + " should be of type " + type.getName() + " but is of type " + f.getType().getName() + "!");
            } else
                return (T) f.get(obj);

        } catch (NoSuchFieldException e) {
            return fail("Field " + obj.getClass().getName() + "." + name + " not declared!");

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets Constructor Object and asserts existence.
     * @param clazz source class
     * @param params parameters
     * @return
     */
    public static Constructor getConstructor(Class<?> clazz, Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException e) {
            fail("Constructor for " + clazz.getName() + " with parameters " + Arrays.toString(params) + " not implemented!");
        }

        return null;
    }

    /**
     * Asserts methods existence, parameters and modifier.
     * @param modifier expected modifiers
     * @param clazz source class
     * @param name name
     * @param params parameters
     */
    public static void assertMethodModifiers(int modifier, Class<?> clazz, String name, Class<?>... params) {
        Method m = getMethod(clazz, name, params);
        assertEquals(m.getModifiers() & (~ Modifier.FINAL), modifier, "Method " + clazz.getName() + "." + name + " has wrong modifiers!");
    }

    /**
     * asserts Fields existence, type and modifiers.
     * @param modifier expected modifiers
     * @param clazz source class
     * @param name name
     * @param type expected type
     */
    public static void assertFieldModifiers(int modifier, Class<?> clazz, String name, Class<?> type) {
        try {
            Field f = clazz.getDeclaredField(name);
            assertEquals(f.getModifiers() & (~ Modifier.FINAL), modifier, "Field " + clazz.getName() + "." + name + " has wrong modifiers!");
            if(f.getType() != type) {
                fail("Field " + clazz.getName() + "." + name + " should be of type " + type.getName() + " but is of type " + f.getType().getName() + "!");
            }
        } catch (NoSuchFieldException e) {
            fail("Field " + clazz.getName() + "." + name + " not declared!");
        }
    }

    /**
     * Asserts Constructors existence and modifiers.
     * @param modifier expected modifiers
     * @param clazz source class
     * @param params parameters
     */
    public static void assertConstructorModifiers(int modifier, Class<?> clazz, Class<?>... params) {
        Constructor c = getConstructor(clazz, params);
        assertEquals(c.getModifiers(), modifier, "Constructor of " + clazz.getName() + " with parameters " + Arrays.toString(params) + " has wrong modifiers!");
    }
}
