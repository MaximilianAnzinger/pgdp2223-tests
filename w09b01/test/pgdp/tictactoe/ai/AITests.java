package pgdp.tictactoe.ai;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.tictactoe.Field;
import pgdp.tictactoe.Move;
import pgdp.tictactoe.PenguAI;

public class AITests {

    /*
    HINWEISE:
    - DIE ÜBERGEBENEN BOARDS SIND TEILS NICHT DIREKT DURCH EIN SPIEL ENTSTANDEN SONDERN KREIERT,
        ABER DIE AI SOLLTE TROTZDEM VALIDE ZÜGE GEBEN KÖNNEN

    TEST CASES:
    - erster zug legal
    - nur ein freier value
    - nur ein freies feld
    - nur ein feld möglich zum überschreiben
    - gewinnzug zeile
    - gewinnzug spalte
    - gewinnzug diagonale
    - gewinnzug verhindern zeile
    - gewinnzug verhindern spalte
    - gewinnzug verhindern diagonale
    - gewinnzug durch überschreiben
    - gewinnzug verhindern durch überschreiben
    - gewinnzug verhindern (mehrere gewinne verhindern mit einem zug)

     */

    private Field[][] board;    // {{spalte 1},{spalte 2},{spalte 3}}
    private boolean[] firstPlayedMoves, secondPlayedMoves;
    private PenguAI ai;

    @BeforeEach
    public void initialize(){
        this.board = null;
        this.firstPlayedMoves = null;
        this.secondPlayedMoves = null;
        this.ai = new SimpleAI();
    }

    // VALIDEN ZUG FINDEN

    @Test
    @DisplayName("DG Test 01 einfacher zug leeres feld erster spieler")
    public void test01() {
        this.board = new Field[][]{
                {null, null, null},
                {null, null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue(move.x() >= 0 && move.x() < 3);
        Assertions.assertTrue(move.y() >= 0 && move.y() < 3);
        Assertions.assertTrue(move.value() >= 0 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 02 einfacher zug fast leeres feld zweiter spieler")
    public void test02() {
        this.board = new Field[][]{
                {new Field(8, true), null, null},
                {null, null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertFalse(move.x() == 2 && move.y() == 2);
        Assertions.assertTrue(move.x() >= 0 && move.x() < 3);
        Assertions.assertTrue(move.y() >= 0 && move.y() < 3);
        Assertions.assertTrue(move.value() >= 0 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 03 erster spieler nur ein value frei")
    public void test03() {
        this.board = new Field[][]{
                {null, null, null},
                {null, null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                true, true, true,
                true, false, true};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue(move.x() >= 0 && move.x() < 3);
        Assertions.assertTrue(move.y() >= 0 && move.y() < 3);
        Assertions.assertEquals(7, move.value());

    }

    @Test
    @DisplayName("DG Test 04 zweiter spieler nur ein value frei")
    public void test04() {
        this.board = new Field[][]{
                {new Field(8, true), null, null},
                {null, null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, true,
                true, true, true,
                true, true, true};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertFalse(move.x() == 2 && move.y() == 2);
        Assertions.assertTrue(move.x() >= 0 && move.x() < 3);
        Assertions.assertTrue(move.y() >= 0 && move.y() < 3);
        Assertions.assertEquals(1, move.value());

    }

    @Test
    @DisplayName("DG Test 05 erster spieler nur ein feld null")
    public void test05() {
        this.board = new Field[][]{
                {new Field(8, true), new Field(8, false), new Field(7, true)},
                {new Field(6, true), new Field(7, false), new Field(5, false)},
                {new Field(5, false), null, new Field(5, true)}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, true,
                true, true, true};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, true,
                true, true, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 0 && move.value() < 5);

    }

    @Test
    @DisplayName("DG Test 06 zweiter spieler nur ein feld null")
    public void test06() {
        this.board = new Field[][]{
                {new Field(8, true), new Field(8, false), new Field(7, true)},
                {new Field(6, true), new Field(7, false), null},
                {new Field(5, false), new Field(5, true), new Field(4, true)}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, true, true,
                true, true, true};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, true, true,
                false, true, true};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 0 && (move.value() < 4 || move.value() == 6));

    }

    @Test
    @DisplayName("DG Test 07 erster spieler nur ein feld frei mit überschreiben")
    public void test07() {
        this.board = new Field[][]{
                {new Field(8, true), new Field(8, false), new Field(7, true)},
                {new Field(6, true), new Field(7, false), new Field(5, false)},
                {new Field(5, false), new Field(1, false), new Field(5, true)}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, true,
                true, true, true};
        this.secondPlayedMoves = new boolean[]{
                false, true, false,
                false, false, true,
                true, true, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 5);

    }

    @Test
    @DisplayName("DG Test 08 zweiter spieler nur ein feld frei mit überschreiben")
    public void test08() {
        this.board = new Field[][]{
                {new Field(8, true), new Field(8, false), new Field(7, true)},
                {new Field(6, true), new Field(7, false), new Field(1, true)},
                {new Field(5, false), new Field(5, true), new Field(4, true)}};
        this.firstPlayedMoves = new boolean[]{
                false, true, false,
                false, true, true,
                true, true, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, true, true,
                false, true, true};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 2 && (move.value() < 4 || move.value() == 6));

    }

    // GEWINNZUG FINDEN

    @Test
    @DisplayName("DG Test 09 einfacher gewinnzug erster spieler spalte")
    public void test09() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), null},
                {new Field(0, false), new Field(1, false), null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(2, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 10 einfacher gewinnzug zweiter spieler spalte")
    public void test10() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), null},
                {new Field(0, false), new Field(1, false), null},
                {new Field(2, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(2, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 11 einfacher gewinnzug erster spieler zeile")
    public void test11() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), null},
                {new Field(1, true), new Field(1, false), null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 12 einfacher gewinnzug zweiter spieler zeile")
    public void test12() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), null},
                {new Field(1, true), new Field(1, false), null},
                {null, null, new Field(2, true)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 13 einfacher gewinnzug erster spieler diagonale links oben rechts unten test 1")
    public void test13() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), new Field(1, false)},
                {null, new Field(1, true), null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(2, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 14 einfacher gewinnzug erster spieler diagonale links oben rechts unten test 2")
    public void test14() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), new Field(1, false)},
                {null, null, null},
                {null, null, new Field(1, true)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 15 einfacher gewinnzug zweiter spieler diagonale links oben rechts unten test 1")
    public void test15() {
        this.board = new Field[][]{
                {null, new Field(0, true), new Field(1, true)},
                {null, new Field(1, false), new Field(2, true)},
                {null, null, new Field(0, false)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 16 einfacher gewinnzug zweiter spieler diagonale links oben rechts unten test 2")
    public void test16() {
        this.board = new Field[][]{
                {new Field(1, false), new Field(0, true), new Field(1, true)},
                {null, null, new Field(2, true)},
                {null, null, new Field(0, false)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 17 einfacher gewinnzug erster spieler diagonale links unten rechts oben test 1")
    public void test17() {
        this.board = new Field[][]{
                {new Field(0, false), new Field(1, false), new Field(0, true)},
                {null, new Field(1, true), null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 18 einfacher gewinnzug erster spieler diagonale links unten rechts oben test 2")
    public void test18() {
        this.board = new Field[][]{
                {new Field(0, false), new Field(1, false), new Field(0, true)},
                {null, null, null},
                {new Field(1, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 19 einfacher gewinnzug zweiter spieler diagonale links unten rechts oben test 1")
    public void test19() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), new Field(0, false)},
                {null, new Field(1, false), new Field(2, true)},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 20 einfacher gewinnzug zweiter spieler diagonale links unten rechts oben test 2")
    public void test20() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), new Field(0, false)},
                {null, null, new Field(2, true)},
                {new Field(1, false), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 2 && move.value() < 9);

    }

    // GEWINNZUG VERHINDERN

    @Test
    @DisplayName("DG Test 21 einfacher gewinnzug verhindern erster spieler spalte")
    public void test21() {
        this.board = new Field[][]{
                {null, new Field(0, true), null},
                {new Field(0, false), new Field(1, false), null},
                {new Field(1, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        //Assertions.assertEquals(2, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 22 einfacher gewinnzug verhindern zweiter spieler spalte")
    public void test22() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), null},
                {new Field(0, false), null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        //Assertions.assertEquals(2, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 23 einfacher gewinnzug verhindern erster spieler zeile")
    public void test23() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), null},
                {null, new Field(1, false), new Field(1, true)},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        //Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 24 einfacher gewinnzug verhindern zweiter spieler zeile")
    public void test24() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), null},
                {new Field(1, true), null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        //Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 25 einfacher gewinnzug verhindern erster spieler diagonale links oben rechts unten test 1")
    public void test25() {
        this.board = new Field[][]{
                {new Field(0, false), null, new Field(0, true)},
                {null, new Field(1, false), null},
                {null, new Field(1, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 0) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 2));
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 26 einfacher gewinnzug verhindern erster spieler diagonale links oben rechts unten test 2")
    public void test26() {
        this.board = new Field[][]{
                {new Field(0, false), null, new Field(0, true)},
                {null, null, null},
                {null, new Field(1, true), new Field(1, false)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 0) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 2));
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 27 einfacher gewinnzug verhindern zweiter spieler diagonale links oben rechts unten test 1")
    public void test27() {
        this.board = new Field[][]{
                {new Field(0, true), null, null},
                {null, new Field(1, true), null},
                {null, new Field(0, false), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 0) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 2));
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 28 einfacher gewinnzug verhindern zweiter spieler diagonale links oben rechts unten test 2")
    public void test28() {
        this.board = new Field[][]{
                {new Field(0, true), null, new Field(0, false)},
                {null, null, null},
                {null, null, new Field(1, true)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 0) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 2));
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 29 einfacher gewinnzug verhindern erster spieler diagonale links unten rechts oben test 1")
    public void test29() {
        this.board = new Field[][]{
                {new Field(0, true), null, null},
                {null, new Field(1, false), null},
                {new Field(0, false), new Field(1, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 2) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 0));
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 30 einfacher gewinnzug verhindern erster spieler diagonale links unten rechts oben test 2")
    public void test30() {
        this.board = new Field[][]{
                {new Field(0, true), null, new Field(1, false)},
                {null, null, null},
                {new Field(0, false), new Field(1, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 2) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 0));
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 31 einfacher gewinnzug verhindern zweiter spieler diagonale links unten rechts oben test 1")
    public void test31() {
        this.board = new Field[][]{
                {new Field(0, false), null, null},
                {null, new Field(1, true), null},
                {new Field(0, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 2) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 0));
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 32 einfacher gewinnzug verhindern zweiter spieler diagonale links unten rechts oben test 2")
    public void test32() {
        this.board = new Field[][]{
                {new Field(0, false), null, new Field(1, true)},
                {null, null, null},
                {new Field(0, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 2) ||
                (move.x() == 1 && move.y() == 1) ||
                (move.x() == 2 || move.y() == 0));
        Assertions.assertEquals(8, move.value());

    }

    // GEWINNZUG DURCH ÜBERSCHREIBEN

    @Test
    @DisplayName("DG Test 33 einfacher gewinnzug durch überschreiben erster spieler spalte")
    public void test33() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), new Field(2, false)},
                {new Field(0, false), new Field(1, false), null},
                {null, new Field(2, false), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(2, move.y());
        Assertions.assertTrue(move.value() >= 3 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 34 einfacher gewinnzug durch überschreiben zweiter spieler spalte")
    public void test34() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), null},
                {new Field(0, false), new Field(1, false), new Field(2, true)},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(2, move.y());
        Assertions.assertTrue(move.value() >= 3 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 35 einfacher gewinnzug durch überschreiben erster spieler zeile")
    public void test35() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), null},
                {new Field(1, true), new Field(1, false), null},
                {new Field(2, false), new Field(2, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 3 && move.value() < 9);

    }

    @Test
    @DisplayName("DG Test 36 einfacher gewinnzug durch überschreiben zweiter spieler zeile")
    public void test36() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(0, false), null},
                {new Field(1, true), new Field(1, false), null},
                {null, new Field(2, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 3 && move.value() < 9);

    }

    // GEWINNZUG VERHINDERN DURCH ÜBERSCHREIBEN

    @Test
    @DisplayName("DG Test 37 einfacher gewinnzug verhindern durch überschreiben erster spieler spalte test 1")
    public void test37() {
        this.board = new Field[][]{
                {null, null, null},
                {new Field(0, false), new Field(8, false), new Field(0, true)},
                {new Field(1, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);
        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 38 einfacher gewinnzug verhindern durch überschreiben erster spieler spalte test 2")
    public void test38() {
        this.board = new Field[][]{
                {null, null, null},
                {new Field(8, false), new Field(0, false), new Field(0, true)},
                {new Field(1, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 39 einfacher gewinnzug verhindern durch überschreiben zweiter spieler spalte test 1")
    public void test39() {
        this.board = new Field[][]{
                {null, null, null},
                {new Field(0, true), new Field(8, true), new Field(0, false)},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 40 einfacher gewinnzug verhindern durch überschreiben zweiter spieler spalte test 2")
    public void test40() {
        this.board = new Field[][]{
                {null, null, null},
                {new Field(8, true), new Field(0, true), new Field(0, false)},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 41 einfacher gewinnzug verhindern durch überschreiben erster spieler zeile test 1")
    public void test41() {
        this.board = new Field[][]{
                {null, new Field(0, false), null},
                {null, new Field(8, false), new Field(1, true)},
                {null, new Field(0, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 42 einfacher gewinnzug verhindern durch überschreiben erster spieler zeile test 2")
    public void test42() {
        this.board = new Field[][]{
                {null, new Field(8, false), null},
                {null, new Field(0, false), new Field(1, true)},
                {null, new Field(0, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 43 einfacher gewinnzug verhindern durch überschreiben zweiter spieler zeile test 1")
    public void test43() {
        this.board = new Field[][]{
                {null, new Field(0, true), null},
                {null, new Field(8, true), null},
                {null, new Field(0, false), null}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 44 einfacher gewinnzug verhindern durch überschreiben zweiter spieler zeile test 2")
    public void test44() {
        this.board = new Field[][]{
                {null, new Field(8, true), null},
                {null, new Field(0, true), null},
                {null, new Field(0, false), null}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 45 einfacher gewinnzug verhindern durch überschreiben erster spieler diagonale links oben rechts unten test 1")
    public void test45() {
        this.board = new Field[][]{
                {new Field(0, false), new Field(1, true), null},
                {null, new Field(8, false), null},
                {null, null, new Field(0, true)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 0 && move.y() == 0));
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 46 einfacher gewinnzug verhindern durch überschreiben erster spieler diagonale links oben rechts unten test 2")
    public void test46() {
        this.board = new Field[][]{
                {new Field(8, false), null, null},
                {null, new Field(0, true), null},
                {null, null, new Field(0, false)}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 2 || move.y() == 2));
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 47 einfacher gewinnzug verhindern durch überschreiben zweiter spieler diagonale links oben rechts unten test 1")
    public void test47() {
        this.board = new Field[][]{
                {new Field(8, true), null, null},
                {null, new Field(0, true), null},
                {null, null, new Field(0, false)}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 1 && move.y() == 1));
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 48 einfacher gewinnzug verhindern durch überschreiben zweiter spieler diagonale links oben rechts unten test 2")
    public void test48() {
        this.board = new Field[][]{
                {new Field(8, true), null, null},
                {null, new Field(0, false), null},
                {null, null, new Field(0, true)}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertTrue((move.x() == 2 || move.y() == 2));
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 49 einfacher gewinnzug verhindern erster spieler diagonale links unten rechts oben test 1")
    public void test49() {
        this.board = new Field[][]{
                {null, null, new Field(0, true)},
                {null, new Field(8, false), null},
                {new Field(0, false), new Field(1, true), null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 50 einfacher gewinnzug verhindern erster spieler diagonale links unten rechts oben test 2")
    public void test50() {
        this.board = new Field[][]{
                {null, null, new Field(0, false)},
                {null, new Field(0, true), null},
                {new Field(8, false), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(2, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 51 einfacher gewinnzug verhindern zweiter spieler diagonale links unten rechts oben test 1")
    public void test51() {
        this.board = new Field[][]{
                {null, null, new Field(0, false)},
                {null, new Field(0, true), null},
                {new Field(8, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    @Test
    @DisplayName("DG Test 52 einfacher gewinnzug verhindern zweiter spieler diagonale links unten rechts oben test 2")
    public void test52() {
        this.board = new Field[][]{
                {null, null, new Field(8, true)},
                {null, new Field(0, false), null},
                {new Field(0, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(2, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertTrue(move.value() >= 7);

    }

    // MEHRERE GEWINNE VERHINDERN MIT EINEM ZUG

    @Test
    @DisplayName("DG Test 53 mehrere gewinne verhindern erster spieler spalte und zeile test 1")
    public void test53() {
        this.board = new Field[][]{
                {null, new Field(0, false), new Field(1, false)},
                {new Field(2, false), null, null},
                {new Field(3, false), null, null}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, true,
                true, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 54 mehrere gewinne verhindern erster spieler spalte und zeile test 2")
    public void test54() {
        this.board = new Field[][]{
                {new Field(0, false), new Field(1, false), null},
                {new Field(2, false), null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 55 mehrere gewinne verhindern erster spieler spalte und zeile test 3")
    public void test55() {
        this.board = new Field[][]{
                {new Field(8, false), new Field(1, false), null},
                {null, new Field(0, false), null},
                {null, null, new Field(8, true)}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertEquals(7, move.value());

    }

    @Test
    @DisplayName("DG Test 57 mehrere gewinne verhindern zweiter spieler spalte und zeile test 1")
    public void test57() {
        this.board = new Field[][]{
                {null, new Field(0, true), new Field(1, true)},
                {new Field(2, true), null, null},
                {new Field(3, true), null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                true, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 58 mehrere gewinne verhindern zweiter spieler spalte und zeile test 2")
    public void test58() {
        this.board = new Field[][]{
                {new Field(0, true), new Field(1, true), null},
                {new Field(2, true), null, null},
                {null, null, null}};
        this.firstPlayedMoves = new boolean[]{
                true, true, true,
                false, false, false,
                false, false, false};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, false};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(8, move.value());

    }

    @Test
    @DisplayName("DG Test 59 mehrere gewinne verhindern zweiter spieler spalte und zeile test 3")
    public void test59() {
        this.board = new Field[][]{
                {new Field(8, true), new Field(1, true), null},
                {null, new Field(0, true), null},
                {null, null, new Field(8, false)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(1, move.y());
        Assertions.assertEquals(7, move.value());

    }

    @Test
    @DisplayName("DG Test 61 mehrere gewinne verhindern erster spieler mit diagonale test 1")
    public void test61() {
        this.board = new Field[][]{
                {null, new Field(0, false), new Field(1, false)},
                {null, new Field(7, false), new Field(8, true)},
                {null, new Field(7, true), new Field(8, false)}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, true, true};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, true, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(6, move.value());

    }

    @Test
    @DisplayName("DG Test 62 mehrere gewinne verhindern erster spieler mit diagonale test 2")
    public void test62() {
        this.board = new Field[][]{
                {new Field(0, false), null , new Field(1, false)},
                {null, null, new Field(8, true)},
                {null, null, new Field(8, false)}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(7, move.value());

    }

    @Test
    @DisplayName("DG Test 63 mehrere gewinne verhindern zweiter spieler mit diagonale test 1")
    public void test63() {
        this.board = new Field[][]{
                {null, new Field(0, true), new Field(1, true)},
                {null, new Field(7, true), new Field(8, false)},
                {null, new Field(7, false), new Field(8, true)}};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, true, true};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, true, true};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(6, move.value());

    }

    @Test
    @DisplayName("DG Test 64 mehrere gewinne verhindern zweiter spieler mit diagonale test 2")
    public void test64() {
        this.board = new Field[][]{
                {new Field(0, true), null , new Field(1, true)},
                {null, null, new Field(8, false)},
                {null, null, new Field(8, true)}};
        this.firstPlayedMoves = new boolean[]{
                true, true, false,
                false, false, false,
                false, false, true};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, false,
                false, false, true};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(0, move.x());
        Assertions.assertEquals(0, move.y());
        Assertions.assertEquals(7, move.value());
    }

    @Test
    @DisplayName("DG Test 65 mehrere gewinne verhindern erster spieler beide diagonalen")
    public void test65() {
        this.board = new Field[][]{
                {new Field(8,false), new Field(5, true), new Field(7, false)},
                {new Field(4, true), null, new Field(6, true)},
                {new Field(5, false), new Field(7, true), new Field(6, false)}};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, true, true,
                true, true, false};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, false, true,
                true, true, true};

        Move move = this.ai.makeMove(this.board, true, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
    }

    @Test
    @DisplayName("DG Test 66 mehrere gewinne verhindern zweiter spieler beide diagonalen")
    public void test66() {
        this.board = new Field[][]{
                {new Field(8,true), new Field(5, false), new Field(7, true)},
                {new Field(4, false), null, new Field(6, false)},
                {new Field(5, true), new Field(7, false), new Field(6, true)}};
        this.secondPlayedMoves = new boolean[]{
                false, false, false,
                false, true, true,
                true, true, false};
        this.firstPlayedMoves = new boolean[]{
                false, false, false,
                false, false, true,
                true, true, true};

        Move move = this.ai.makeMove(this.board, false, this.firstPlayedMoves, this.secondPlayedMoves);

        Assertions.assertEquals(1, move.x());
        Assertions.assertEquals(1, move.y());
    }

}

