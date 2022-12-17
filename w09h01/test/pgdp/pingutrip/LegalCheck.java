// ************* PLEASE READ THIS DISCLAIMER ************* //
/*                                                          *
 * Even when there is a test provided by Artemis to test    *
 * the usage of not allowed things, you could still lose    *
 * points after the deadline, if you are using not allowed  *
 * things. See the link below                               *
 *                                                          *
 * https://zulip.in.tum.de/#narrow/stream/1503-PGdP-W09H01  *
 * /topic/Nach.20Deadline.20Abzug.20von.20Punkten/near      *
 * /861199                                                  *
 *                                                          *
 * THIS TEST CHECKS FOR INSTANCES OF KEYWORDS WITHIN YOUR   *
 * CODE. IN THE CASE OF FALSE POSITIVE UNCOMMENT LINE 37    *
 * AT YOUR OWN RISK                                         *
 *                                                          */                                                       
// ******************************************************* //

package pgdp.pingutrip;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//@Disabled
public class LegalCheck {

    String[] BANNED_EXPRESSIONS = new String[] {
            "if", "else", "for", "do", "while", "forEach"
    };

    private abstract class Token {
        public int line;
        public int pos;

        public Token(int l, int p) {
            line = l;
            pos = p;
        }
    }

    private class StringWord extends Token {
        public StringWord(int l, int p) {
            super(l, p);
        }
    }

    private class Word extends Token {
        public String value;

        public Word(String s, int l, int p) {
            super(l, p);
            value = s;
        }
    }

    private class Symbol extends Word {
        public Symbol(String s, int l, int p) {
            super(s, l, p);
        }
    }

    private class Numeric extends Word {
        public Numeric(String s, int l, int p) {
            super(s, l, p);
        }
    }

    //
    // Lex Java File
    //

    private List<Token> lexer(String document) {
        var tokens = new ArrayList<Token>();
        char lastChar = ' ';

        int line = 0;
        int line_position = 0;

        while (document.length() > 0) {
            char character = document.charAt(0);

            ///////////////////////
            // IGNORE WHITESPACE //
            ///////////////////////

            if (Character.isWhitespace(character)) {
                if (character == '\n') {
                    // System.out.print("NL;");
                    line++;
                    line_position = 0;
                }
                document = document.substring(1, document.length());
                continue;
            }

            /////////////////////
            // IGNORE COMMENTS //
            /////////////////////

            if (lastChar == '/') {
                if (character == '/') {
                    // INLINE COMMENT
                    document = document.substring(document.indexOf("\n") + 1, document.length());
                    lastChar = ' ';
                    // Inline Comments end with newline
                    line++;
                    line_position = 0;
                    continue;
                } else if (character == '*') {
                    // MULTILINE COMMENT
                    String comment = document.substring(0, document.indexOf("*/"));
                    document = document.substring(document.indexOf("*/") + 2, document.length());
                    lastChar = ' ';

                    // calculate lines
                    // https://stackoverflow.com/a/28210079
                    line += comment.length() - comment.replaceAll("\n", "").length();

                    int lio = comment.lastIndexOf("\n"); // last index of
                    line_position = lio == -1 ? (line_position += comment.length()) : (comment.length() - lio);

                    continue;
                }
            }

            ////////////////////
            // COMBINE STRING //
            ////////////////////

            if (character == '"' || character == '\'') {
                char endCharacter = character;
                boolean disableEnd = false;
                // PARSE STRING
                while (document.length() > 0) {
                    char string_character = document.charAt(0);

                    if (string_character == '/')
                        disableEnd = !disableEnd;
                    if (string_character == endCharacter && disableEnd) {
                        document = document.substring(1, document.length());
                        continue;
                    }

                    line_position++;
                    document = document.substring(1, document.length());
                }
                tokens.add(new StringWord(line, line_position));
                continue;
            }

            //////////
            // WORD //
            //////////

            if (Character.isAlphabetic(character) || character == '_') {
                String buffer = "" + character;
                line_position++;
                document = document.substring(1, document.length());

                while (Character.isAlphabetic(character) || Character.isDigit(character) || character == '_') {
                    buffer += document.charAt(0);
                    document = document.substring(1, document.length());
                    character = document.charAt(0);
                    line_position++;
                }

                tokens.add(new Word(buffer, line, line_position));
                continue;
            }

            /////////////
            // NUMERIC //
            /////////////

            if (Character.isDigit(character)) {
                String buffer = "" + character;
                line_position++;
                document = document.substring(1, document.length());

                while (document.matches("^[0-9]")) {
                    buffer += document.charAt(0);
                    document = document.substring(1, document.length());
                    line_position++;
                }

                tokens.add(new Numeric(buffer, line, line_position));
                continue;
            }

            ////////////
            // SYMBOL //
            ////////////

            line_position++;
            tokens.add(new Symbol("" + character, line, line_position));

            lastChar = character;
            document = document.substring(1, document.length());
        }

        return tokens;
    }

    //
    // GET ALL FILES
    //

    @ParameterizedTest
    @MethodSource
    @DisplayName("should not contain illegal operations")
    public void srcDirectory(File file) throws IOException {
        String document_content = Files.readString(Path.of(file.toURI()));

        // *** LEX JAVA FILE ***
        // Assume all Java Files are lexable since otherwise
        // the test file would not compile.
        var tokens = lexer(document_content);

        for (Token t : tokens) {
            int l = t.line, p = t.pos;
            String at = " at (" + l + ", " + p + ")";

            if (t instanceof Symbol) {
                Symbol s = (Symbol) t;
                assertFalse(s.value.equals("?"),
                        "Illegal Ternary Operator" + at);
            } else if (t instanceof Word) {
                Word w = (Word) t;
                for (int i = 0; i < BANNED_EXPRESSIONS.length; i++) {
                    assertFalse(w.value.equals(BANNED_EXPRESSIONS[i]),
                            "Illegal Keyword [" + BANNED_EXPRESSIONS[i] + "]" + at);
                }
            }
        }
    }

    static List<Arguments> srcDirectory() {
        var files = new File("src/pgdp/pingutrip").listFiles();
        var file_contents = new ArrayList<Arguments>();

        for (int i = 0; i < files.length; i++) {
            file_contents.add(arguments(files[i]));
        }

        return file_contents;
    }
}
