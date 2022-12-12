package pgdp;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NoUsageOfValuesMethodTest {
    // Note: This test has a lot of false positives (e. g. using the not allowed
    // code in helper methods for allowed methods). The test should just warn
    // you if you have code in your solution that is not allowed.
    @DisplayName("Source code should not contain .values() method because they are not allowed in this exercise")
    @Test
    public void testUsageOfValuesMethod() throws IOException {
        String[] notAllowedRegExp = new String[] {
                "\\.values\\(",
        };

        try (Stream<Path> sourceFiles = Files.find(Path.of("src"), 100,
                (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.toString().endsWith(".java"))) {
            sourceFiles.forEach(f -> {
                String path = f.toString();

                // Methods where everything is allowed.
                String[] allowedMethods = new String[] {
                        "public static void main(String[] args) {",
                        "public static void main(String... args) {",
                        "public static String toString(Collection<?> collection) {",
                        "public static int[] toIntArray(Collection<Integer> collection) {",
                        "public static <T> T[] generateGenericArray(Class<T> clazz, int length) {",
                        "public static <T> T[] specialSort(Class<T> clazz, Collection<T> collection, Comparator<T> comparator) {",
                        "public static <T> Collection<T> intersection(Collection<T>[] collections) {",
                };

                String file = readFile(path);
                String fileWithoutComments = removeComments(file);
                String filteredFile = removeMultipleMethods(fileWithoutComments, allowedMethods);

                // Check if there is not allowed usage of code.
                for (String regExp : notAllowedRegExp) {
                    String[] lines = filteredFile.split("\n");
                    for (int i = 0; i < lines.length; i++) {
                        Matcher matcher = Pattern.compile(regExp).matcher(lines[i]);
                        assertFalse(matcher.find(),
                                "You are not allowed to use .values() methods in your solution! Found a .values() method in "
                                        + path);
                    }
                }
            });
        }
    }

    private String removeMultipleMethods(String file, String[] methods) {
        for (String method : methods) {
            file = removeMethod(file, method);
        }
        return file;
    }

    /**
     * Removes all comments from a file.
     * 
     * @param file The file as string.
     * @return The file without comments.
     */
    private String removeComments(String file) {
        file = removeSingleLineComments(file);
        file = removeBlockComments(file);
        return file;
    }

    /**
     * Removes all single line comments from a file.
     *
     * Block comments are not removed.
     *
     * @param file
     * @return
     */
    private String removeSingleLineComments(String file) {
        String[] lines = file.split("\n");
        String newFile = "";
        for (String line : lines) {
            if (!line.trim().startsWith("//")) {
                newFile += line + "\n";
            }
        }
        return newFile;
    }

    /**
     * Removes all block comments from a file.
     *
     * Single line comments are not removed.
     *
     * @param file
     * @return
     */
    private String removeBlockComments(String file) {
        String[] lines = file.split("\n");
        String newFile = "";
        boolean inBlockComment = false;
        for (String line : lines) {
            if (line.contains("/*")) {
                inBlockComment = true;
            }
            if (!inBlockComment) {
                newFile += line + "\n";
            }
            if (line.contains("*/")) {
                inBlockComment = false;
            }
        }
        return newFile;
    }

    /**
     * Removes a method from a file
     * <p>
     * The method goes through the file line by line and searches the name of
     * the method. When the method is found, the method is searching for the
     * closing bracket of the method. When the closing bracket is found, the
     * method is removed.
     *
     * @param file   The file as string
     * @param method The name of the method to remove
     * @return
     */
    private String removeMethod(String file, String method) {
        String[] lines = file.split("\n");
        int start = -1;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(method)) {
                start = i;
                break;
            }
        }

        if (start == -1) {
            // Could not find me
            return file;
        }

        // When the method is found, we search for the closing bracket. However,
        // we need to keep in mind that we can not search for the next closing
        // bracket because this could be the closing bracket of a for loop or
        // what ever.For this we use a stack. When we find a opening bracket, we
        // push it to the stack. When we find a closing bracket, we pop the
        // stack. When the stack is empty, we found the closing bracket of the
        // method.
        Stack<String> brackets = new Stack<String>();

        int end = -1;
        for (int i = start + 1; i < lines.length && end == -1; i++) {
            String[] characters = lines[i].split("");
            for (String c : characters) {
                if (c.equals("{")) {
                    brackets.push(c);
                } else if (c.equals("}")) {
                    if (brackets.isEmpty()) {
                        // When the stack is empty, we know that we found the
                        // matching closing bracket
                        end = i;
                        break;
                    }
                    brackets.pop();
                }
            }
        }

        if (end == -1) {
            // Could not find end
            return file;
        }

        // Build new file without the lines of the removed method.
        String newFile = "";
        for (int i = 0; i < lines.length; i++) {
            if (i < start || i > end) {
                newFile += lines[i] + "\n";
            }
        }

        return newFile;
    }

    private String readFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                return sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
