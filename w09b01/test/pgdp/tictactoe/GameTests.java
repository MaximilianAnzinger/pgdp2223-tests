package pgdp.tictactoe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.tictactoe.ai.TestAI;

public class GameTests {

    /*
    HINWEISE:

    - DIESER TEST BENÖTIGT DIE KLASSE TestAI AUS DEN ANDEREN GITHUB TESTS
    - Dieser Test erwartet ein attribut board vom typ Field[][] in Game


    TEST CASES:
    - einfacher sieg in zeile
    - einfacher sieg in spalte
    - einfacher sieg in diagonale (beide diagonalen)
    - direkt verloren illegaler zug (x koord, y koord, value out of bounds, value schon genutzt,
        eigener stein dort, höherer gegner stein dort)
    - sieg nach überdecken fremder stein (test nur für zeile)
    - unentschieden
    - kann nicht mehr legen
    - sieg mit letztem stein

     */

    private Game game;
    private PenguAI first, second;
    private Move[] movesFirst, movesSecond;
    private PenguAI winner;

    Field[][] getBoard(Game game) {
        try {
            java.lang.reflect.Field boardField = Game.class.getDeclaredField("board");
            boardField.setAccessible(true);
            return (Field[][]) boardField.get(game);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("This test expects a field \"board: Field[][]\" as a member of \"Game\"", e);
        }
    }

    @Test
    @DisplayName("DG Test 01 einfacher sieg für ersten spieler in einer zeile")
    public void test01(){
        this.movesFirst = parseString("""
                1,0,0,
                2,1,0,
                3,2,0""");
        this.movesSecond = parseString("""
                0,0,1,
                1,1,1""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][0].value());
        Assertions.assertTrue(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[1][0].value());
        Assertions.assertTrue(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[2][0].value());

        Assertions.assertFalse(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 02 einfacher sieg für zweiten spieler in einer zeile")
    public void test02(){
        this.movesFirst = parseString("""
                1,0,0,
                2,1,0,
                3,0,2""");
        this.movesSecond = parseString("""
                0,0,1,
                1,1,1,
                8,2,1""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][0].value());
        Assertions.assertTrue(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertFalse(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][1].value());
        Assertions.assertFalse(getBoard(this.game)[2][1].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[2][1].value());

        Assertions.assertTrue(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[0][2].value());
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 03 einfacher sieg für ersten spieler in einer spalte")
    public void test03(){
        this.movesFirst = parseString("""
                1,0,0,
                2,0,1,
                3,0,2""");
        this.movesSecond = parseString("""
                0,1,0,
                1,1,1""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertTrue(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertTrue(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[0][2].value());
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 04 einfacher sieg für zweiten spieler in einer spalte")
    public void test04(){
        this.movesFirst = parseString("""
                1,0,0,
                2,0,1,
                3,2,2""");
        this.movesSecond = parseString("""
                0,1,0,
                1,1,1,
                8,1,2""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertTrue(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertFalse(getBoard(this.game)[1][2].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[1][2].value());
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[2][2].value());


    }

    @Test
    @DisplayName("DG Test 05 einfacher sieg für ersten spieler in diagonale links oben rechts unten")
    public void test05(){
        this.movesFirst = parseString("""
                1,0,0,
                2,1,1,
                3,2,2""");
        this.movesSecond = parseString("""
                0,1,0,
                1,2,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[1][0].value());
        Assertions.assertFalse(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[2][0].value());

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertTrue(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 06 einfacher sieg für zweiten spieler in diagonale links oben rechts unten")
    public void test06(){
        this.movesFirst = parseString("""
                0,1,0,
                1,2,0,
                6,0,2""");
        this.movesSecond = parseString("""
                1,0,0,
                2,1,1,
                3,2,2""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertFalse(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][0].value());
        Assertions.assertTrue(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[1][0].value());
        Assertions.assertTrue(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[2][0].value());

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertTrue(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[0][2].value());
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertFalse(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 07 einfacher sieg für ersten spieler in diagonale links unten rechts oben")
    public void test07(){
        this.movesFirst = parseString("""
                1,0,2,
                2,1,1,
                3,2,0""");
        this.movesSecond = parseString("""
                0,1,0,
                1,0,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertFalse(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[1][0].value());
        Assertions.assertTrue(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[2][0].value());

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertTrue(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertTrue(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][2].value());
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 08 einfacher sieg für zweiten spieler in diagonale links unten rechts oben")
    public void test08(){
        this.movesFirst = parseString("""
                0,1,0,
                1,2,0,
                6,2,2""");
        this.movesSecond = parseString("""
                1,0,2,
                2,1,1,
                3,2,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertNull(getBoard(this.game)[0][0]);
        Assertions.assertTrue(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(0, getBoard(this.game)[1][0].value());
        Assertions.assertFalse(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[2][0].value());

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertFalse(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[0][2].value());
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 09 illegaler zug erster spieler x koordinate zu groß")
    public void test09(){
        this.movesFirst = parseString("""
                5,3,0""");
        this.movesSecond = parseString("""
                """);
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertNull(getBoard(this.game)[0][0]);
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 10 illegaler zug erster spieler x koordinate zu klein")
    public void test10(){
        this.movesFirst = parseString("""
                5,-1,0""");
        this.movesSecond = parseString("""
                """);
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertNull(getBoard(this.game)[0][0]);
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 11 illegaler zug erster spieler y koordinate zu groß")
    public void test11(){
        this.movesFirst = parseString("""
                5,0,3""");
        this.movesSecond = parseString("""
                """);
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertNull(getBoard(this.game)[0][0]);
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 12 illegaler zug erster spieler y koordinate zu klein")
    public void test12(){
        this.movesFirst = parseString("""
                5,0,-1""");
        this.movesSecond = parseString("""
                """);
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertNull(getBoard(this.game)[0][0]);
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 13 illegaler zug zweiter spieler x koordinate zu groß")
    public void test13(){
        this.movesFirst = parseString("""
                5,0,0""");
        this.movesSecond = parseString("""
                5,3,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 14 illegaler zug zweiter spieler x koordinate zu klein")
    public void test14(){
        this.movesFirst = parseString("""
                5,0,0""");
        this.movesSecond = parseString("""
                5,-1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 15 illegaler zug zweiter spieler y koordinate zu groß")
    public void test15(){
        this.movesFirst = parseString("""
                5,0,0""");
        this.movesSecond = parseString("""
                5,0,3""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 16 illegaler zug zweiter spieler y koordinate zu klein")
    public void test16(){
        this.movesFirst = parseString("""
                5,0,0""");
        this.movesSecond = parseString("""
                5,0,-1""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 17 illegaler zug erster spieler value zu groß")
    public void test17(){
        this.movesFirst = parseString("""
                9,0,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertNull(getBoard(this.game)[0][0]);
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 18 illegaler zug erster spieler value zu klein")
    public void test18(){
        this.movesFirst = parseString("""
                -1,0,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertNull(getBoard(this.game)[0][0]);
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 19 illegaler zug zweiter spieler value zu groß")
    public void test19(){
        this.movesFirst = parseString("""
                5,0,0""");
        this.movesSecond = parseString("""
                9,1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 20 illegaler zug zweiter spieler value zu klein")
    public void test20(){
        this.movesFirst = parseString("""
                5,0,0""");
        this.movesSecond = parseString("""
                -1,1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertNull(getBoard(this.game)[1][0]);
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 21 illegaler zug erster spieler value schon gelegt")
    public void test21(){
        this.movesFirst = parseString("""
                5,0,0,
                5,2,2""");
        this.movesSecond = parseString("""
                1,1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 22 illegaler zug zweiter spieler value schon gelegt")
    public void test22(){
        this.movesFirst = parseString("""
                5,0,0,
                6,2,2""");
        this.movesSecond = parseString("""
                1,1,0,
                1,2,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 23 illegaler zug erster spieler auf eigenen stein gelegt")
    public void test23(){
        this.movesFirst = parseString("""
                5,0,0,
                6,0,0""");
        this.movesSecond = parseString("""
                1,1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 24 illegaler zug zweiter spieler auf eigenen stein gelegt")
    public void test24(){
        this.movesFirst = parseString("""
                5,0,0,
                6,2,2""");
        this.movesSecond = parseString("""
                1,1,0,
                2,1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 25 illegaler zug erster spieler auf fremden stein mit kleinerem value gelegt")
    public void test25(){
        this.movesFirst = parseString("""
                5,0,0,
                6,0,0""");
        this.movesSecond = parseString("""
                7,1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 26 illegaler zug zweiter spieler auf fremden stein mit kleinerem value gelegt")
    public void test26(){
        this.movesFirst = parseString("""
                5,0,0,
                6,2,2""");
        this.movesSecond = parseString("""
                1,1,0,
                2,2,2""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(1, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 27 sieg erster spieler nach überdecken gegnerstein")
    public void test27(){
        this.movesFirst = parseString("""
                5,0,0,
                3,1,0,
                4,2,0""");
        this.movesSecond = parseString("""
                1,1,0,
                2,2,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertTrue(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[1][0].value());
        Assertions.assertTrue(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(4, getBoard(this.game)[2][0].value());

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 28 sieg zweiter spieler nach überdecken gegnerstein")
    public void test28(){
        this.movesFirst = parseString("""
                1,0,0,
                2,1,0,
                3,2,0""");
        this.movesSecond = parseString("""
                2,0,0,
                3,1,0,
                8,2,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertFalse(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(2, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(3, getBoard(this.game)[1][0].value());
        Assertions.assertFalse(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[2][0].value());

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 29 unentschieden")
    public void test29(){
        this.movesFirst = parseString("""
                0,0,0,
                1,1,0,
                2,0,0,
                3,1,0,
                4,0,0,
                5,1,0,
                6,0,0,
                7,1,0,
                8,0,0
                """);
        this.movesSecond = parseString("""
                0,1,0,
                1,0,0,
                2,1,0,
                3,0,0,
                4,1,0,
                5,0,0,
                6,1,0,
                7,0,0,
                8,1,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertNull(this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertNull(getBoard(this.game)[0][1]);
        Assertions.assertNull(getBoard(this.game)[1][1]);
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertNull(getBoard(this.game)[0][2]);
        Assertions.assertNull(getBoard(this.game)[1][2]);
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }

    @Test
    @DisplayName("DG Test 30 erster spieler kann nicht mehr legen")
    public void test30(){
        this.movesFirst = parseString("""
                8,0,0,
                7,2,0,
                6,0,1,
                5,1,2,
                1,2,2""");
        this.movesSecond = parseString("""
                8,1,0,
                7,1,1,
                6,0,2,
                5,2,1,
                4,2,2""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[1][0].value());
        Assertions.assertTrue(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[2][0].value());

        Assertions.assertTrue(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[1][1].value());
        Assertions.assertFalse(getBoard(this.game)[2][1].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[2][1].value());

        Assertions.assertFalse(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[0][2].value());
        Assertions.assertTrue(getBoard(this.game)[1][2].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[1][2].value());
        Assertions.assertFalse(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(4, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 31 zweiter spieler kann nicht mehr legen")
    public void test31(){
        this.movesFirst = parseString("""
                8,0,0,
                7,2,0,
                6,0,1,
                5,1,2,
                4,2,2""");
        this.movesSecond = parseString("""
                8,1,0,
                7,1,1,
                6,0,2,
                5,2,1""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertTrue(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[0][0].value());
        Assertions.assertFalse(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[1][0].value());
        Assertions.assertTrue(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[2][0].value());

        Assertions.assertTrue(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[1][1].value());
        Assertions.assertFalse(getBoard(this.game)[2][1].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[2][1].value());

        Assertions.assertFalse(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[0][2].value());
        Assertions.assertTrue(getBoard(this.game)[1][2].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[1][2].value());
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(4, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 32 sieg mit letztem zug erster spieler")
    public void test32(){
        this.movesFirst = parseString("""
                8,0,2,
                7,1,2,
                0,0,0,
                1,1,0,
                2,0,0,
                3,1,0,
                4,0,0,
                5,1,0,
                6,2,2""");
        this.movesSecond = parseString("""
                8,0,1,
                7,1,1,
                0,1,0,
                1,0,0,
                2,1,0,
                3,0,0,
                4,1,0,
                5,0,0""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.first, this.winner);

        // check board
        Assertions.assertFalse(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertTrue(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[1][0].value());
        Assertions.assertNull(getBoard(this.game)[2][0]);

        Assertions.assertFalse(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[1][1].value());
        Assertions.assertNull(getBoard(this.game)[2][1]);

        Assertions.assertTrue(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[0][2].value());
        Assertions.assertTrue(getBoard(this.game)[1][2].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[1][2].value());
        Assertions.assertTrue(getBoard(this.game)[2][2].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[2][2].value());

    }

    @Test
    @DisplayName("DG Test 33 sieg mit letztem zug zweiter spieler")
    public void test33(){
        this.movesFirst = parseString("""
                8,0,2,
                7,1,2,
                0,0,0,
                1,1,0,
                2,0,0,
                3,1,0,
                4,0,0,
                5,1,0,
                6,2,0""");
        this.movesSecond = parseString("""
                8,0,1,
                7,1,1,
                0,1,0,
                1,0,0,
                2,1,0,
                3,0,0,
                4,1,0,
                5,0,0,
                6,2,1""");
        this.first = new TestAI(this.movesFirst);
        this.second = new TestAI(this.movesSecond);
        this.game = new Game(first, second);

        this.game.playGame();

        this.winner = this.game.getWinner();

        // check winner
        Assertions.assertEquals(this.second, this.winner);

        // check board
        Assertions.assertFalse(getBoard(this.game)[0][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[0][0].value());
        Assertions.assertTrue(getBoard(this.game)[1][0].firstPlayer());
        Assertions.assertEquals(5, getBoard(this.game)[1][0].value());
        Assertions.assertTrue(getBoard(this.game)[2][0].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[2][0].value());

        Assertions.assertFalse(getBoard(this.game)[0][1].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[0][1].value());
        Assertions.assertFalse(getBoard(this.game)[1][1].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[1][1].value());
        Assertions.assertFalse(getBoard(this.game)[2][1].firstPlayer());
        Assertions.assertEquals(6, getBoard(this.game)[2][1].value());


        Assertions.assertTrue(getBoard(this.game)[0][2].firstPlayer());
        Assertions.assertEquals(8, getBoard(this.game)[0][2].value());
        Assertions.assertTrue(getBoard(this.game)[1][2].firstPlayer());
        Assertions.assertEquals(7, getBoard(this.game)[1][2].value());
        Assertions.assertNull(getBoard(this.game)[2][2]);

    }


    /**
     * Creates a Move[] array from string. A move consists of three integers separates by commas.
     * The first is the value, the second is the x coordinate, the third is the y coordinate.
     */
    public static Move[] parseString(String s) {
        if(s.isEmpty()) return  new Move[] {};
        s = s.replaceAll("\\s+","");
        String[] split = s.split(",");

        Move[] moves = new Move[split.length / 3];

        for(int i = 0; i < split.length; i += 3) {
            moves[i / 3] = new Move(Integer.parseInt(split[i + 1]), Integer.parseInt(split[i + 2]), Integer.parseInt(split[i]));
        }

        return moves;
    }

}
