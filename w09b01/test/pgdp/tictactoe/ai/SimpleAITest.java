package pgdp.tictactoe.ai;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pgdp.tictactoe.Field;
import pgdp.tictactoe.Move;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleAITest {

    void moveCoordinatesInRange(Move move){
        assertTrue(move.x() >= 0 && move.x() <= 2);
        assertTrue(move.y() >= 0 && move.y() <= 2);
    }

    @Test
    @DisplayName("Very first move")
    void nullArray(){
        SimpleAI ai = new SimpleAI();
        Move move = ai.makeMove(new Field[3][3], true, new boolean[9], new boolean[9]);
        assertNotNull(move);
        moveCoordinatesInRange(move);
        assertTrue(move.value() >= 0 && move.value() <= 8);
    }

    @Nested
    @DisplayName("AI could win immediately")
    class AIcouldWin{
        @Test
        @DisplayName("Example #1")
        void basicExampleOne(){
            Field[][] board = {
                    {new Field(6, true), null, new Field(5, true)},
                    {new Field(3, false), new Field(5, false), null},
                    {null, null, null}
            };
            SimpleAI ai = new SimpleAI();
            boolean[] firstPlayedPieces = {false, false, false, false, false, true, true, false, false};
            boolean[] secondPlayedPieces = {false, false, false, true, false, true, false, false, false};
            Move move = ai.makeMove(board, true, firstPlayedPieces, secondPlayedPieces);
            assertEquals(0, move.x());
            assertEquals(1, move.y());
            assertFalse(firstPlayedPieces[move.value()]);
        }

        @Test
        @DisplayName("Example #2")
        void basicExampleTwo(){
            Field[][] board = {
                    {new Field(6, true), null, new Field(5, true)},
                    {new Field(3, false), new Field(5, false), null},
                    {new Field(8, false), null, new Field(2, true)}
            };
            SimpleAI ai = new SimpleAI();
            boolean[] firstPlayedPieces = {false, false, true, false, false, true, true, false, false};
            boolean[] secondPlayedPieces = {false, false, false, true, false, true, false, false, true};
            Move move = ai.makeMove(board, true, firstPlayedPieces, secondPlayedPieces);
            assertTrue((move.x() == 0 && move.y() == 1) || (move.x() == 1 && move.y() == 2) || (move.x() == 1 && move.y() == 1 && move.value() > 5));
            assertFalse(firstPlayedPieces[move.value()]);
        }
    }

    @Nested
    @DisplayName("oppponent could win immediately")
    class OpponentCouldWin{
        @Test
        @DisplayName("Example #1")
        void basicExampleOne () {
            Field[][] board = {
                    {new Field(6, true), null, new Field(5, true)},
                    {new Field(3, false), null, null},
                    {null, null, null}
            };
            SimpleAI ai = new SimpleAI();
            boolean[] firstPlayedPieces = {false, false, false, false, false, true, true, false, false};
            boolean[] secondPlayedPieces = {false, false, false, true, false, false, false, false, false};
            Move move = ai.makeMove(board, false, firstPlayedPieces, secondPlayedPieces);
            assertEquals(0, move.x());
            assertTrue(move.value() >= (new int[]{6, 0, 5})[move.y()]);
            assertFalse(secondPlayedPieces[move.value()]);
        }

        @Test
        @DisplayName("Example #2")
        void basicExampleTwo(){
            Field[][] board = {
                    {new Field(6, true), null, new Field(5, true)},
                    {new Field(3, false), new Field(5, false), null},
                    {new Field(8, false), null, new Field(2, true)}
            };
            SimpleAI ai = new SimpleAI();
            boolean[] firstPlayedPieces = {false, false, true, false, false, true, true, false, false};
            boolean[] secondPlayedPieces = {false, false, false, true, false, true, false, false, true};
            Move move = ai.makeMove(board, false, firstPlayedPieces, secondPlayedPieces);
            moveCoordinatesInRange(move);
            assertFalse(secondPlayedPieces[move.value()]);
        }
    }
}
