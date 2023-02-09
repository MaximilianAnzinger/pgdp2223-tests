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

	@Override
	public void play() {
		try {
			super.play();
		} catch (NoSuchElementException wanted) {}
	}

	@Override
	protected int rollDice() {
		return diceRollQueue.pop();
	}

}
