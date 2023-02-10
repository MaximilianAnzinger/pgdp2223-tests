package pgdp.situationTests;

import pgdp.game.PinguGame;

import java.util.*;

public class SpecificPinguGame extends PinguGame {
	private Deque<Integer> diceRollQueue;

	public SpecificPinguGame(int... diceRolls) {
		super(true, 420, 0);

		diceRollQueue = new ArrayDeque<>(diceRolls.length);

		for (int roll : diceRolls) {
			diceRollQueue.addLast(roll);
		}
	}

	//Take List as Input
	public SpecificPinguGame(List<Integer> diceRolls)
	{
		super(true, 420, 0);

		diceRollQueue = new ArrayDeque<>(diceRolls);

		/*for (int roll : diceRolls) {
			diceRollQueue.addLast(roll);
		}*/
	}

	// Only as many dices as entrys in the diceRollQueue will be "rolled", as an NoSuchElementException will be thrown with the next roll, which is caught here.
	// The output can now be compared to the expected output. I know, not very clean, but whatever.
	@Override
	public void play() {
		try {
			super.play();
		} catch (NoSuchElementException wanted) {
		}
	}

	@Override
	protected int rollDice() {
		return diceRollQueue.pop();
	}

}
