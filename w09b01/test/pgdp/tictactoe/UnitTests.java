package pgdp.tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pgdp.tictactoe.ai.TestAI;

public class UnitTests {

    @Test
    @DisplayName("Should play Artemis example")
    public void artemisExample() {
        Move[] movesX = parseString("""
            7, 0, 1,
            6, 0, 2,
            8, 1, 2,
            1, 2, 1,
            5, 2, 0,
            4, 0, 0,
            3, 1, 1""");

        Move[] movesO = parseString("""
            8, 2, 2,
            7, 0, 2,
            0, 2, 1,
            6, 2, 1,
            3, 0, 0,
            5, 0, 0,
            4, 1, 1""");


        runTest(movesX, movesO, false);
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("Should play Artemis example")
    public void outOfBoundsTest(Move[] movesX, Move[] movesO, boolean firstWon) {
        runTest(movesX, movesO, firstWon);
    }

    public static Stream<Arguments> outOfBoundsTest() {
        Move[] emptyMove = parseString("");
        Move[] move = parseString("0, 0, 0");

        Move[] outOfBounds1 = parseString("0, 0, -1");
        Move[] outOfBounds2 = parseString("0, 0, 99");
        Move[] outOfBounds3 = parseString("0, -1, 0");
        Move[] outOfBounds4 = parseString("0, 99, 0");
        Move[] outOfBounds5 = parseString("-1, 0, 0");
        Move[] outOfBounds6 = parseString("99, 0, 0");

        return Stream.of(
            arguments(outOfBounds1, emptyMove, false),
            arguments(outOfBounds2, emptyMove, false),
            arguments(outOfBounds3, emptyMove, false),
            arguments(outOfBounds4, emptyMove, false),
            arguments(outOfBounds5, emptyMove, false),
            arguments(outOfBounds6, emptyMove, false),
            arguments(move, outOfBounds1, true),
            arguments(move, outOfBounds2, true),
            arguments(move, outOfBounds3, true),
            arguments(move, outOfBounds4, true),
            arguments(move, outOfBounds5, true),
            arguments(move, outOfBounds6, true)
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("Should play Artemis example")
    public void invalidMoveTest(Move[] movesX, Move[] movesO, boolean firstWon) {
        runTest(movesX, movesO, firstWon);
    }

    public static Stream<Arguments> invalidMoveTest() {
        Move[] emptyMove = parseString("");

        Move[] move1 = parseString("5, 0, 0");
        Move[] invalid1 = parseString("0, 0, 0");
        Move[] invalid2 = parseString("5, 0, 0");
        Move[] invalid3 = parseString("0, 1, 0, 0, 1, 0");
        Move[] invalid4 = parseString("0, 1, 0, 8, 1, 0");

        return Stream.of(
                arguments(move1, invalid1, true),
                arguments(move1, invalid2, true),
                arguments(invalid3, move1, false),
                arguments(invalid4, move1, false)
        );
    }

    public void runTest(Move[] movesX, Move[] movesO, boolean firstWon) {
        TestAI X = new TestAI(movesX);
        TestAI O = new TestAI(movesO);
        Game g = new Game(X, O);
        g.playGame();

        assertEquals(firstWon ? X : O, g.getWinner());
        assertEquals(movesX.length, X.getIndex());
        assertEquals(movesO.length, O.getIndex());
    }

    /**
     * Creates a Move[] array from string. A move consists of three integers separates by commas.
     * The first is the value, the second is the x coordinate, the third is the y coordinate.
     * @param s
     * @return
     */
    public static Move[] parseString(String s) {
        if(s.isEmpty()) return  new Move[] {};
        s = s.replaceAll("\\s+","");
        String[] split = s.split(",");

        Move[] moves = new Move[split.length / 3];

        for(int i = 0; i < split.length; i += 3) {
            moves[i / 3] = new Move(Integer.valueOf(split[i + 1]), Integer.valueOf(split[i + 2]), Integer.valueOf(split[i]));
        }

        return moves;
    }
}