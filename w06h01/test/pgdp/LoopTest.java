package pgdp;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

public class LoopTest {
    // Note: This test has a lot of false positives (e.g. using the word "for"
    // in a method name or a comment). The test should just warn you if you use
    // loops in your solution.
    @DisplayName("Source code should not contain loops because they are not allowed in this exercise")
    @Test
    public void testUsageOfLoops() throws IOException {
        String[] notAllowedRegExp = new String[]{
                "for\s*[(]{1}",
                "while\s*[(]{1}",
                "Stream.of\\(",
                "Stream<",
                "Arrays.stream\\(",
        };

        try (Stream<Path> sourceFiles = Files.find(Path.of("src"), 100, (filePath, fileAttr) -> fileAttr.isRegularFile() && filePath.toString().endsWith(".java"))) {
            sourceFiles.forEach(f -> {
                String path = f.toString();


                // Methods that are allowed to use loops
                String[] allowedMethods = new String[]{
                        "public static void main(String[] args) {",
                        "public String toString() {",
                        "public String toConnectionString() {"
                };

                String file = readFile(path);
                String filteredFile = removeMultipleMethods(file, allowedMethods);

                // Check if there are any loops
                for (String regExp : notAllowedRegExp) {
                    int lineNum = getOffendingLineNum(filteredFile, regExp);
                    boolean noLoop = lineNum == -1;

                    assertTrue(noLoop,
                            "You are not allowed to use loops in your solution! Found a loop at " + path + ":" + lineNum);
                }
            });
        }
    }

    private int getOffendingLineNum(String s, String regExp) {
        Pattern pattern = Pattern.compile(regExp);

        String[] lines = s.split("\n");
        for (int i = 0; i < lines.length; i++) {
            Matcher matcher = pattern.matcher(lines[i]);
            if (matcher.find()) {
                return i + 1;
            }
        }
        // No loop found, returning -1
        return -1;
    }

    private String removeMultipleMethods(String file, String[] methods) {
        for (String method : methods) {
            file = removeMethod(file, method);
        }
        return file;
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
