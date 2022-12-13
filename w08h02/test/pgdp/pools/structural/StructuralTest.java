package pgdp.pools.structural;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.StringJoiner;

public abstract class StructuralTest<T> {

    private final Class<T> testSubject;

    public StructuralTest(Class<T> testSubject) {
        this.testSubject = testSubject;
    }

    /**
     * Checks if the given attribute exists in {@code testSubject}.<p>
     * The position of the attribute is not important when invoking this method.
     *
     * @param name the name of the attribute to look for
     * @param type the data type of the attribute
     * @param modifiers any given modifiers applied to the attribute
     */
    public final void expectAttribute(String name, Class<?> type, int modifiers) {
        expectAttribute(name, -1, type, modifiers);
    }

    /**
     * Checks if the given attribute exists in {@code testSubject}
     *
     * @param name the name of the attribute to look for
     * @param declarationIndex index at which the attribute has to be declared
     * @param type the data type of the attribute
     * @param modifiers any given modifiers applied to the attribute
     */
    public final void expectAttribute(String name, int declarationIndex, Class<?> type, int modifiers) {
        if (declarationIndex >= 0) {
            expectAttributeAt(name, declarationIndex);
        }
        try {
            Field f = testSubject.getDeclaredField(name);
            if (f.getType() != type) {
                var expectedTypeName = getTypeName(type);
                var actualTypeName = getTypeName(f.getType());
                fail("The attribute \"" + name + "\" in class " + testSubject.getSimpleName() +
                     " is not of correct type.\nShould be of type \"" + expectedTypeName + "\"" +
                     " but actually is of type \"" + actualTypeName + "\".\n");
            }
            if (f.getModifiers() != modifiers) {
                fail("The attribute \"" + name + "\" in class " + testSubject.getSimpleName() +
                     " does not have the correct modifiers.\nShould have the modifier(s) \"" + Modifier.toString(modifiers) + "\"" +
                     " but actually uses the modifier(s) \"" + Modifier.toString(f.getModifiers()) + "\".\n");
            }
        } catch (NoSuchFieldException e) {
            var typeName = getTypeName(type);

            String textSuffix = modifiers != -1
                ? " that has the following modifier(s):\n" + Modifier.toString(modifiers)
                : ".";
            fail("The class " + testSubject.getSimpleName() + " must implement an attribute " +
                 "called \"" + name + "\" of type " + typeName + textSuffix + "\n", e);
        }
    }

    private void expectAttributeAt(String name, int index) {
        if (index < 0) {
            return;
        }
        Field[] fields = testSubject.getDeclaredFields();
        if (index >= fields.length) {
            fail("Expected \"" + name + "\" to be the attribute at index " + index +
                 " (zero-based) in class " + testSubject.getSimpleName() +
                 " but only " + fields.length + " attributes were specified!\n");
        }
        var field = fields[index];
        if (!field.getName().equals(name)) {
            fail("Expected \"" + name + "\" to be the attribute at index " + index +
                 " (zero-based) in class " + testSubject.getSimpleName() +
                 " but actually found the attribute \"" + field.getName() + "\" there!\n");
        }
    }

    /**
     * Checks if ONLY the given constructor exists in {@code testSubject}
     *
     * @param modifiers any given modifiers applied to the attribute
     * @param types all the expected constructor parameter data types (order is important!)
     */
    public final void onlyExpectConstructor(int modifiers, Class<?>... types) {
        StringJoiner parameters = new StringJoiner(", ");
        for (Class<?> type : types) {
            parameters.add(getTypeName(type));
        }
        String mods = Modifier.toString(modifiers);

        if (testSubject.getDeclaredConstructors().length != 1) {
            fail("Only expected exactly one constructor in class " + testSubject.getSimpleName() +
                 " with parameters (" + parameters + ") and modifier(s) \"" + mods + "\".\n");
        }
        try {
            Constructor<T> f = testSubject.getDeclaredConstructor(types);
            if (f.getModifiers() != modifiers) {
                fail("The constructor in class " + testSubject.getSimpleName() + " with parameters (" + parameters + ") in that order" +
                     " does not have the correct modifier(s).\nShould have the modifier(s) \"" + mods + "\"" +
                     " but actually uses the modifier(s) \"" + Modifier.toString(f.getModifiers()) + "\".\n");
            }
        } catch (NoSuchMethodException e) {
            fail("The class " + testSubject.getSimpleName() + " must implement the constructor " +
                 (!mods.isEmpty() ? mods + " " : "") + testSubject.getSimpleName() + "(" + parameters + ")\n", e);
        }
    }

    /**
     * Checks if the given constructor exists in {@code testSubject}
     *
     * @param modifiers any given modifiers applied to the attribute
     * @param types all the expected constructor parameter data types (order is important!)
     */
    public final void expectConstructor(int modifiers, Class<?>... types) {
        String mods = Modifier.toString(modifiers);
        try {
            Constructor<T> f = testSubject.getDeclaredConstructor(types);
            if (f.getModifiers() != modifiers) {
                StringJoiner parameters = new StringJoiner(", ");
                for (Class<?> type : types) {
                    parameters.add(getTypeName(type));
                }
                fail("The constructor in class " + testSubject.getSimpleName() + " with parameters (" + parameters + ") in that order" +
                     " does not have the correct modifier(s).\nShould have the modifier(s) \"" + mods + "\"" +
                     " but actually uses the modifier(s) \"" + Modifier.toString(f.getModifiers()) + "\".\n");
            }
        } catch (NoSuchMethodException e) {
            StringJoiner parameters = new StringJoiner(", ");
            for (Class<?> type : types) {
                parameters.add(getTypeName(type));
            }
            fail("The class " + testSubject.getSimpleName() + " must implement the constructor " +
                 (!mods.isEmpty() ? mods + " " : "") + testSubject.getSimpleName() + "(" + parameters + ")\n", e);
        }
    }

    /**
     * Checks if the given method exists in {@code testSubject}
     *
     * @param modifiers any given modifiers applied to the attribute
     * @param name the name of the method to look for
     * @param types all the expected constructor parameter data types (order is important!)
     */
    public final void expectMethod(int modifiers, String name, Class<?> returnType, Class<?>... types) {
        String mods = Modifier.toString(modifiers);
        try {
            Method f = testSubject.getDeclaredMethod(name, types);
            StringJoiner parameters = new StringJoiner(", ");
            for (Class<?> type : types) {
                parameters.add(getTypeName(type));
            }
            if (f.getModifiers() != modifiers) {
                fail("The method " + name + "(" + parameters + ") in class " + testSubject.getSimpleName() +
                     " does not have the correct modifier(s).\nShould have the modifier(s) \"" + mods + "\"" +
                     " but actually uses the modifier(s) \"" + Modifier.toString(f.getModifiers()) + "\".\n");
            }
            if (f.getReturnType() != returnType) {
                fail("The method " + name + "(" + parameters + ") in class " + testSubject.getSimpleName() +
                     " does not have the correct return type.\nShould have the return type \"" + returnType.getSimpleName() + "\"" +
                     " but actually uses the return type \"" + f.getReturnType().getSimpleName() + "\".\n");
            }
        } catch (NoSuchMethodException e) {
            StringJoiner parameters = new StringJoiner(", ");
            for (Class<?> type : types) {
                parameters.add(getTypeName(type));
            }
            fail("The class " + testSubject.getSimpleName() + " must implement the method " +
                 (!mods.isEmpty() ? mods + " " : "") + name + "(" + parameters + ")\n", e);
        }
    }

    /**
     * Checks if the given method does not exist in {@code testSubject}
     *
     * @param modifiers any given modifiers applied to the attribute
     * @param name the name of the method to look for
     * @param types all the expected constructor parameter data types (order is important!)
     */
    public final void dontExpectMethod(int modifiers, String name, Class<?>... types) {
        try {
            Method f = testSubject.getDeclaredMethod(name, types);
            if (f.getModifiers() == modifiers) {
                StringJoiner parameters = new StringJoiner(", ");
                for (Class<?> type : types) {
                    parameters.add(getTypeName(type));
                }
                fail("The method " + name + "(" + parameters + ") in class " + testSubject.getSimpleName() +
                     " should not exist according to the exercise but it does!\n");
            }
        } catch (NoSuchMethodException ignored) {
            // dontExpectMethod should check for non-existing method
        }
    }

    //<editor-fold desc="Helper Methods">
    private String getTypeName(Class<?> type) {
        int arrayDepth = 0;
        while (type.isArray()) {
            type = type.getComponentType();
            arrayDepth++;
        }
        return type.getSimpleName() + "[]".repeat(arrayDepth);
    }
    //</editor-fold>

}
