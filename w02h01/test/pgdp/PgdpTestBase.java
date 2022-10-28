package pgdp;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class PgdpTestBase {

    public static abstract class MethodCallCapture<R> {

        final List<String> consoleLines;
        String methodName = null;
        Object[] parameters = new Object[0];

        public MethodCallCapture(List<String> consoleLines) {
            this.consoleLines = consoleLines;
        }

        public abstract <M extends MethodCallCapture<R>> ConsoleAssertions<M, R> theConsole();

        /**
         * Causes a test failure. The method parameter is used to add a description to the test failure.
         *
         * @param messageDecorator The decorator used to add a failure message to the test failure
         */
        public void thenFailTest(Consumer<StringJoiner> messageDecorator) {
            var message = new StringJoiner("\n");
            message.add("");
            messageDecorator.accept(message);
            if (message.toString().contains("|")) {
                message.add("").add("""
                    +-----------------------------------------------------------+
                    | IMPORTANT:                                                |
                    | The prefix '| ' is just used for making lines more clear. |
                    | It is not part of the expected lines nor your output.     |
                    | The number in front of '|' is the console's line number.  |
                    +-----------------------------------------------------------+""");
            }
            System.err.println(message);
            fail();
        }

        /**
         * Adds additional data to the error message that will be printed if a test failure occurs.
         *
         * @param methodName The method that is currently being tested
         * @param parameters The parameters the tested method is being called with
         */
        public abstract MethodCallCapture<R> withTheFollowingDescribingData(String methodName, Object... parameters);
    }

    public static class VoidMethodCallCapture extends MethodCallCapture<Void> {

        public VoidMethodCallCapture(List<String> consoleLines) {
            super(consoleLines);
        }

        @Override
        public VoidMethodCallCapture withTheFollowingDescribingData(String methodName, Object... parameters) {
            if (methodName != null) {
                this.methodName = methodName;
            }
            if (parameters != null) {
                this.parameters = parameters;
            }
            return this;
        }

        /**
         * Access to console assertion methods
         */
        @SuppressWarnings("unchecked")
        @Override
        public ConsoleAssertions<VoidMethodCallCapture, Void> theConsole() {
            return new ConsoleAssertions<>(this);
        }
    }

    public static class ReturningMethodCallCapture<R> extends MethodCallCapture<R> {

        private final R returnValue;

        public ReturningMethodCallCapture(R returnValue, List<String> consoleLines) {
            super(consoleLines);
            this.returnValue = returnValue;
        }

        /**
         * Access to console assertion methods
         */
        @SuppressWarnings("unchecked")
        @Override
        public ConsoleAssertions<ReturningMethodCallCapture<R>, R> theConsole() {
            return new ConsoleAssertions<>(this);
        }

        /**
         * Assert the return value of the tested method. If it does not match {@code expectedValue} exactly the test will fail
         *
         * @param expectedValue The value the return value has to match exactly
         */
        public ReturningMethodCallCapture<R> itShouldReturn(R expectedValue) {
            if (!Objects.deepEquals(returnValue, expectedValue)) {
                thenFailTest(errorMessage -> {
                    if (methodName != null && parameters != null) {
                        var parametersJoined = Arrays.stream(parameters).map(Object::toString).collect(Collectors.joining(", "));
                        errorMessage
                            .add("=== WRONG VALUE RETURNED FROM METHOD -> " + methodName + "(" + parametersJoined + ") ===");
                    } else {
                        errorMessage
                            .add("=== WRONG VALUE RETURNED FROM METHOD ===");
                    }
                    errorMessage
                        .add("Your implementation has to return the following value:")
                        .add("" + expectedValue)
                        .add("Currently it returns the following value:")
                        .add("" + returnValue);
                });
            }
            return this;
        }

        @Override
        public ReturningMethodCallCapture<R> withTheFollowingDescribingData(String methodName, Object... parameters) {
            if (methodName != null) {
                this.methodName = methodName;
            }
            if (parameters != null) {
                this.parameters = parameters;
            }
            return this;
        }
    }

    public static class ConsoleAssertions<M extends MethodCallCapture<R>, R> {

        private final M root;
        private final List<String> lines;

        private ConsoleAssertions(M root) {
            this.root = root;
            this.lines = this.root.consoleLines;
        }

        /**
         * Asserts the tested method to print {@code message} exactly in line {@code lineNumber}
         * <p>The provided string have to match exactly! Differences in casing, spacing, etc. will cause the test to fail</p>
         *
         * @param lineNumber The line number the {@code message} has to be in. Is zero-based (-> Console line 1 is lineNumber 0)
         * @param message The text to check against
         */
        public ConsoleAssertions<M, R> hasMessageInLineThatMatches(int lineNumber, String message) {
            if (lineNumber >= lines.size()) {
                this.root.thenFailTest(errorMessage -> {
                    if (root.methodName != null && root.parameters != null) {
                        var parameters = Arrays.stream(root.parameters).map(Object::toString).collect(Collectors.joining(", "));
                        errorMessage
                            .add("=== WRONG CONSOLE OUTPUT -> " + root.methodName + "(" + parameters + ") ===");
                    } else {
                        errorMessage
                            .add("=== WRONG CONSOLE OUTPUT ===");
                    }
                    errorMessage
                        .add("Your implementation has to print at least " + (lineNumber + 1) + " line.")
                        .add("Additionally your implementation must print the following text in line " + lineNumber + ":")
                        .add("| " + message)
                        .add("Currently it prints the following messages to the console:");
                    for (var line : lines) {
                        errorMessage.add("| " + line);
                    }
                    errorMessage
                        .add("")
                        .add("They look the same? Check if there are whitespaces after your")
                        .add("message that should not be there.");
                });
            }
            var textInLine = lines.get(lineNumber);
            if (!Objects.equals(textInLine, message)) {
                this.root.thenFailTest(errorMessage -> {
                    if (root.methodName != null && root.parameters != null) {
                        var parameters = Arrays.stream(root.parameters).map(Object::toString).collect(Collectors.joining(", "));
                        errorMessage
                            .add("=== WRONG CONSOLE OUTPUT -> " + root.methodName + "(" + parameters + ") ===");
                    } else {
                        errorMessage
                            .add("=== WRONG CONSOLE OUTPUT ===");
                    }
                    errorMessage
                        .add("Your implementation has to print the following text in line " + lineNumber + ":")
                        .add("| " + message)
                        .add("Currently it prints the following messages to the console:");
                    for (var i = 0; i < lines.size(); i++) {
                        errorMessage.add((i + 1) + " | " + lines.get(i));
                    }
                    errorMessage
                        .add("")
                        .add("They look the same? Check if there are whitespaces after your")
                        .add("message that should not be there.");
                });
            }
            return this;
        }

        /**
         * Asserts the tested method to print at least <b>one</b> of the given {@code texts}
         * <p>The provided strings have to match exactly! Differences in casing, spacing, etc. will cause the test to fail</p>
         * <p>The order in which the tested method prints these {@code texts} does not play a role.</p>
         *
         * @param texts The texts that should be used for matching
         */
        public ConsoleAssertions<M, R> mustContainAnyOf(String... texts) {
            boolean foundAny = false;
            for (var line : texts) {
                if (this.lines.contains(line)) {
                    foundAny = true;
                    break;
                }
            }
            if (!foundAny) {
                this.root.thenFailTest(errorMessage -> {
                    if (root.methodName != null && root.parameters != null) {
                        var parameters = Arrays.stream(root.parameters).map(Object::toString).collect(Collectors.joining(", "));
                        errorMessage
                            .add("=== WRONG CONSOLE OUTPUT -> " + root.methodName + "(" + parameters + ") ===");
                    } else {
                        errorMessage
                            .add("=== WRONG CONSOLE OUTPUT ===");
                    }
                    errorMessage
                        .add("Your implementation has to print at least one of the following messages to the console:");
                    for (var line : texts) {
                        errorMessage.add("| " + line);
                    }
                    errorMessage.add("Currently it prints the following messages to the console:");
                    for (var i = 0; i < lines.size(); i++) {
                        errorMessage.add((i + 1) + " | " + lines.get(i));
                    }
                    errorMessage
                        .add("")
                        .add("They look the same? Check if there are whitespaces after your")
                        .add("message that should not be there.");
                });
            }
            return this;
        }

        /**
         * Asserts the tested method to print at least <b>all</b> of the given {@code texts} but it can also print more.
         * <p>The provided strings have to match exactly! Differences in casing, spacing, etc. will cause the test to fail</p>
         * <p>The order in which the tested method prints these {@code texts} does not play a role.</p>
         *
         * @param texts The texts that should be used for matching
         */
        public ConsoleAssertions<M, R> mustContainAtLeast(String... texts) {
            if (texts.length > 0) {
                List<String> remaining = Arrays.asList(texts);
                for (var line : lines) {
                    remaining.remove(line);
                }

                if (!remaining.isEmpty()) {
                    this.root.thenFailTest(errorMessage -> {
                        if (root.methodName != null && root.parameters != null) {
                            var parameters = Arrays.stream(root.parameters).map(Object::toString).collect(Collectors.joining(", "));
                            errorMessage
                                .add("=== WRONG CONSOLE OUTPUT -> " + root.methodName + "(" + parameters + ") ===");
                        } else {
                            errorMessage
                                .add("=== WRONG CONSOLE OUTPUT ===");
                        }
                        errorMessage
                            .add("Your implementation has to print at least all of the following messages to the console:");
                        for (var line : texts) {
                            errorMessage.add("| " + line);
                        }
                        errorMessage.add("Currently it prints the following messages to the console:");
                        for (var i = 0; i < lines.size(); i++) {
                            errorMessage.add((i + 1) + " | " + lines.get(i));
                        }
                        errorMessage
                            .add("")
                            .add("They look the same? Check if there are whitespaces after your")
                            .add("message that should not be there.");
                    });
                }
            }
            return this;
        }

        /**
         * Asserts the tested method to print just exactly <b>all</b> of the given {@code texts}.
         * <p>The provided strings have to match exactly! Differences in casing, spacing, etc. will cause the test to fail</p>
         * <p>The order in which the tested method prints these {@code texts} does not play a role.</p>
         *
         * @param texts The texts that should be used for matching
         */
        public ConsoleAssertions<M, R> mustContainExactly(String... texts) {
            if (texts.length > 0) {
                var identical = false;

                if (lines.size() != texts.length) {
                    this.root.thenFailTest(errorMessage -> {
                        if (root.methodName != null && root.parameters != null) {
                            var parameters = Arrays.stream(root.parameters).map(Object::toString).collect(Collectors.joining(", "));
                            errorMessage
                                .add("=== WRONG CONSOLE OUTPUT -> " + root.methodName + "(" + parameters + ") ===");
                        } else {
                            errorMessage
                                .add("=== WRONG CONSOLE OUTPUT ===");
                        }
                        errorMessage
                            .add("Your implementation has to print exactly the following messages to the console:");
                        for (var line : texts) {
                            errorMessage.add("| " + line);
                        }
                        errorMessage.add("Currently it prints the following messages to the console:");
                        for (var i = 0; i < lines.size(); i++) {
                            errorMessage.add((i + 1) + " | " + lines.get(i));
                        }
                        errorMessage
                            .add("")
                            .add("They look the same? Check if there are whitespaces after your")
                            .add("message that should not be there.");
                    });
                }

                for (int i = 0; i < texts.length; i++) {
                    identical = Objects.equals(texts[i], lines.get(i));
                }

                if (!identical) {
                    this.root.thenFailTest(errorMessage -> {
                        if (root.methodName != null && root.parameters != null) {
                            var parameters = Arrays.stream(root.parameters).map(Object::toString).collect(Collectors.joining(", "));
                            errorMessage
                                .add("=== WRONG CONSOLE OUTPUT -> " + root.methodName + "(" + parameters + ") ===");
                        } else {
                            errorMessage
                                .add("=== WRONG CONSOLE OUTPUT ===");
                        }
                        errorMessage
                            .add("Your implementation has to print exactly the following messages to the console:");
                        for (var line : texts) {
                            errorMessage.add("| " + line);
                        }
                        errorMessage.add("Currently it prints the following messages to the console:");
                        for (var i = 0; i < lines.size(); i++) {
                            errorMessage.add((i + 1) + " | " + lines.get(i));
                        }
                        errorMessage
                            .add("")
                            .add("They look the same? Check if there are whitespaces after your")
                            .add("message that should not be there.");
                    });
                }
            }
            return this;
        }

        public M and() {
            return root;
        }

    }

    /**
     * Tests a void method for certain conditions
     *
     * @param voidMethodCall A call to the tested method
     * @return A new instance of a class containing testing utilities
     */
    public VoidMethodCallCapture whenCallingMethod(Runnable voidMethodCall) {
        List<String> consoleLines = runInCapturingConsoleEnvironment(voidMethodCall);
        return new VoidMethodCallCapture(consoleLines);
    }

    /**
     * Tests a method returning some value for certain conditions
     *
     * @param returningMethodCall A call to the tested method
     * @return A new instance of a class containing testing utilities
     */
    public <R> ReturningMethodCallCapture<R> whenCallingMethod(Supplier<R> returningMethodCall) {
        var ref = new Object() {
            R returnValue = null;
        };
        List<String> consoleLines = runInCapturingConsoleEnvironment(() -> ref.returnValue = returningMethodCall.get());
        return new ReturningMethodCallCapture<>(ref.returnValue, consoleLines);
    }

    /**
     * Runs a method inside a context that overwrites {@code System.out} {@link PrintStream}.
     * <p>This context modification is used to access messages written to {@code System.out}</p>
     * <p>After the code that is provided via {@code code} got executed the {@code System.out} gets reset to the default one.</p>
     *
     * @param code Code to execute in the modified context
     * @return All messages written to {@code System.out}
     */
    private List<String> runInCapturingConsoleEnvironment(Runnable code) {
        var stdOut = System.out;
        List<String> lines;
        try (var outputStream = new ByteArrayOutputStream(); var consoleStream = new PrintStream(outputStream)) {
            System.setOut(consoleStream);

            code.run();
            lines = Arrays.asList(outputStream.toString().split(System.lineSeparator()));

            System.setOut(stdOut);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

}
