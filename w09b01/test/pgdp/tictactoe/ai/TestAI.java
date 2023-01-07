package pgdp.tictactoe.ai;

import pgdp.tictactoe.Field;
import pgdp.tictactoe.Move;
import pgdp.tictactoe.PenguAI;

public class TestAI extends PenguAI {

    private final Move[] moves;
    private int index = 0;

    public TestAI(Move[] moves) {
        this.moves = moves;
    }

    @Override
    public Move makeMove(Field[][] board, boolean firstPlayer, boolean[] firstPlayedPieces, boolean[] secondPlayedPieces) {
        if(index >= moves.length) throw new ArrayIndexOutOfBoundsException("Called PenguAI.makeMove after game was already finished");
        return moves[index++];
    }

    public int getIndex() {
        return this.index;
    }
}
