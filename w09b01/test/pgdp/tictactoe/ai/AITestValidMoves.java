package pgdp.tictactoe.ai;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.tictactoe.ai.SimpleAI;


public class AITestValidMoves {

            public boolean isvalidf(Move m1,Field [] [] board, boolean []stones1 ){
                if(m1.value()<9&&m1.value()>=0 && m1.x()<3&&m1.x()>=0&& m1.y()<3&&m1.y()>=0 &&(board[m1.x()][m1.y()]==null||(board[m1.x()][m1.y()].value()<m1.value()&& !board[m1.x()][m1.y()].firstPlayer()))&& !stones1[m1.value()] ) {

                    return true;}
                else{
                    return false;
                }
            }
            public boolean isvalids(Move m2, Field [][] board,boolean []stones2){
                if(m2.value()<9&&m2.value()>=0 && m2.x()<3&&m2.x()>=0&& m2.y()<3&&m2.y()>=0 && (board[m2.x()][m2.y()]==null||(board[m2.x()][m2.y()].value()<m2.value()&& board[m2.x()][m2.y()].firstPlayer()))&& !stones2[m2.value()] ){
                    return true;
                }else{return false;}}

    private Field[][] board;    // {{spalte 1},{spalte 2},{spalte 3}}
    private boolean[] firstPlayedMoves, secondPlayedMoves;
    private PenguAI ai;

    @BeforeEach
    public void initialize() {
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

        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));


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
        Assertions.assertTrue(isvalids(move,board,secondPlayedMoves));

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
        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));

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
        Assertions.assertTrue(isvalids(move,board,secondPlayedMoves));
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
        printBoard(board);
        System.out.println(move);
        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));

    }

    public static void printBoard(Field[][] board) {
        System.out.println("┏━━━┳━━━┳━━━┓");
        for (int y = 0; y < board.length; y++) {
            System.out.print("┃");
            for (int x = 0; x < board.length; x++) {
                if (board[x][y] != null) {
                    System.out.print(board[x][y] + "┃");
                } else {
                    System.out.print("   ┃");
                }
            }
            System.out.println();
            if (y != board.length - 1) {
                System.out.println("┣━━━╋━━━╋━━━┫");
            }
        }
        System.out.println("┗━━━┻━━━┻━━━┛");
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
        printBoard(board);
        System.out.println(move);
        Assertions.assertTrue(isvalids(move,board,secondPlayedMoves));

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
        printBoard(board);
        System.out.println(move);
        System.out.println(this.firstPlayedMoves);
        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));

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

        Assertions.assertTrue(isvalids(move,board,secondPlayedMoves));

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

        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));

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

        Assertions.assertTrue(isvalids(move,board,secondPlayedMoves));

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

        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));

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

        Assertions.assertTrue(isvalids(move,board,secondPlayedMoves));

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

        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));

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

        Assertions.assertTrue(isvalidf(move,board,firstPlayedMoves));

    }




}
