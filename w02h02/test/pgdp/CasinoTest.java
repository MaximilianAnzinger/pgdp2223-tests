package pgdp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.casino.CardDeck;
import pgdp.casino.Casino;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CasinoTest {
    private void setCardDeck(int seed) {
        try {
            Constructor<CardDeck> constructor = CardDeck.class.getDeclaredConstructor(int.class);
            constructor.setAccessible(true);
            CardDeck deck = constructor.newInstance(seed);

            Field deckField = CardDeck.class.getDeclaredField("deck");
            deckField.setAccessible(true);
            deckField.set(null, deck);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    void testBlackjackOutput(int seed, String expected) {
        InputStream stdin = System.in;
        PrintStream stdout = System.out;

        String[] lines = expected.split("\\r?\\n");
        String[] inputs = Arrays.stream(lines)
                .filter((l) -> l.contains("IN:"))
                .map((l) -> l.replace("IN:", "").trim())
                .toArray(String[]::new);


        String output = Arrays.stream(lines)
                .filter((l) -> !l.contains("IN:"))
                .reduce((a, l) -> a + System.lineSeparator() + l).get();
        output += System.lineSeparator();

        InputStream in = new LineInputStream(inputs);
        OutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        setCardDeck(seed);
        Casino.penguBlackJack();
        assertEquals(output, out.toString());

        System.setIn(stdin);
        System.setOut(stdout);
    }

    @Test
    @DisplayName("Example from Artemis")
    void artemisExample() {
        testBlackjackOutput(
                420,
                """
                        Welcome to Pengu-BlackJack!
                        (1) Start a game or (2) exit
                        IN: 1
                        Your current balance: 1000
                        How much do you want to bet?
                        IN: 69
                        Player cards:
                        1 : 1
                        2 : 8
                        Current standing: 9
                        (1) draw another card or (2) stay
                        IN: 1
                        3 : 3
                        Current standing: 12
                        (1) draw another card or (2) stay
                        IN: 1
                        4 : 8
                        Current standing: 20
                        (1) draw another card or (2) stay
                        IN: 2
                        Dealer cards:
                        1 : 4
                        2 : 2
                        3 : 8
                        4 : 8
                        Dealer: 22
                        You won 69 tokens.
                        (1) Start a game or (2) exit
                        IN: 1
                        Your current balance: 1069
                        How much do you want to bet?
                        IN: 420
                        Player cards:
                        1 : 9
                        2 : 5
                        Current standing: 14
                        (1) draw another card or (2) stay
                        IN: 1
                        3 : 7
                        Current standing: 21
                        Blackjack! You won 840 tokens.
                        (1) Start a game or (2) exit
                        IN: 1
                        Your current balance: 1909
                        How much do you want to bet?
                        IN: 1337
                        Player cards:
                        1 : 3
                        2 : 3
                        Current standing: 6
                        (1) draw another card or (2) stay
                        IN: 1
                        3 : 7
                        Current standing: 13
                        (1) draw another card or (2) stay
                        IN: 1
                        4 : 10
                        Current standing: 23
                        You lost 1337 tokens.
                        (1) Start a game or (2) exit
                        IN: 2
                        Your final balance: 572
                        That's very very sad :(
                        Thank you for playing. See you next time.
                        """
        );
    }

    @Test
    @DisplayName("Player should lose when net profit is 0")
    void check0Profi() {
        testBlackjackOutput(
                10,
                """
                        Welcome to Pengu-BlackJack!
                        (1) Start a game or (2) exit
                        IN: 2
                        Your final balance: 1000
                        That's very very sad :(
                        Thank you for playing. See you next time.
                        """);
    }

    @Test
    @DisplayName("No tokens left")
    void broke() {
        testBlackjackOutput(
                420,
                """
                        Welcome to Pengu-BlackJack!
                        (1) Start a game or (2) exit
                        IN: 1
                        Your current balance: 1000
                        How much do you want to bet?
                        IN: 1000
                        Player cards:
                        1 : 1
                        2 : 8
                        Current standing: 9
                        (1) draw another card or (2) stay
                        IN: 1
                        3 : 3
                        Current standing: 12
                        (1) draw another card or (2) stay
                        IN: 1
                        4 : 8
                        Current standing: 20
                        (1) draw another card or (2) stay
                        IN: 1
                        5 : 4
                        Current standing: 24
                        You lost 1000 tokens.
                        Sorry, you are broke. Better Luck next time.
                        Your final balance: 0
                        That's very very sad :(
                        Thank you for playing. See you next time.
                        """
        );
    }

    @Test
    @DisplayName("Profit message")
    void profit() {
        testBlackjackOutput(
                420,
                """
                        Welcome to Pengu-BlackJack!
                        (1) Start a game or (2) exit
                        IN: 1
                        Your current balance: 1000
                        How much do you want to bet?
                        IN: 69
                        Player cards:
                        1 : 1
                        2 : 8
                        Current standing: 9
                        (1) draw another card or (2) stay
                        IN: 1
                        3 : 3
                        Current standing: 12
                        (1) draw another card or (2) stay
                        IN: 1
                        4 : 8
                        Current standing: 20
                        (1) draw another card or (2) stay
                        IN: 2
                        Dealer cards:
                        1 : 4
                        2 : 2
                        3 : 8
                        4 : 8
                        Dealer: 22
                        You won 69 tokens.
                        (1) Start a game or (2) exit
                        IN: 2
                        Your final balance: 1069
                        Wohooo! Ez profit! :D
                        Thank you for playing. See you next time.
                        """
        );
    }

    @Test
    @DisplayName("Invalid Inputs")
    void invalidInputs() {
        testBlackjackOutput(
                420,
                """
                        Welcome to Pengu-BlackJack!
                        (1) Start a game or (2) exit
                        IN: 0
                        What?!
                        (1) Start a game or (2) exit
                        IN: -1
                        What?!
                        (1) Start a game or (2) exit
                        IN: 3
                        What?!
                        (1) Start a game or (2) exit
                        IN: test
                        This was not a valid number, please try again.
                        IN: 1
                        Your current balance: 1000
                        How much do you want to bet?
                        IN: 0
                        How much do you want to bet?
                        IN: -1
                        How much do you want to bet?
                        IN: 1001
                        How much do you want to bet?
                        IN: 2147483648
                        This was not a valid number, please try again.
                        IN: 1
                        Player cards:
                        1 : 1
                        2 : 8
                        Current standing: 9
                        (1) draw another card or (2) stay
                        IN: 0
                        What?!
                        (1) draw another card or (2) stay
                        What?!
                        IN: -1
                        (1) draw another card or (2) stay
                        What?!
                        IN: 3
                        (1) draw another card or (2) stay
                        IN: 2
                        Dealer cards:
                        1 : 3
                        2 : 8
                        Dealer: 11
                        Dealer wins. You lost 1 tokens.
                        (1) Start a game or (2) exit
                        IN: 2
                        Your final balance: 999
                        That's very very sad :(
                        Thank you for playing. See you next time.
                        """
        );
    }
}
