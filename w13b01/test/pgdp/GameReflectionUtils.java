package pgdp;

import org.junit.jupiter.api.Assertions;
import pgdp.game.Board;
import pgdp.game.PinguGame;

import java.lang.reflect.Field;

public class GameReflectionUtils {

	private GameReflectionUtils() {
	}

	public static int[] getBoardFields(PinguGame game) {
		Board board = null;
		try {
			Field boardField = PinguGame.class.getDeclaredField("board");
			boardField.setAccessible(true);

			board = (Board) boardField.get(game);
		} catch (NoSuchFieldException e) {
			Assertions.fail("Field 'board' of class 'PinguGame' mustn't be renamed to maintain functionality of tests!");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}


		int[] boardFields = null;
		try {
			Field boardFieldsField = Board.class.getDeclaredField("boardFields");
			boardFieldsField.setAccessible(true);

			boardFields = (int[]) boardFieldsField.get(board);
		} catch (NoSuchFieldException e) {
			Assertions.fail("Field 'boardFields' of class 'Board' mustn't be renamed to maintain functionality of tests!");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		return boardFields;
	}

	/**
	 * sets figures attributes
	 *
	 * @param playerID 1-based id of player
	 * @param figureID 1-based id of figure
	 */
	public static void setFigureAttributes(PinguGame game, int playerID, int figureID, boolean isHome, boolean reachedGoal, int position) {
		Board board = null;
		try {
			Field boardField = PinguGame.class.getDeclaredField("board");
			boardField.setAccessible(true);

			board = (Board) boardField.get(game);
		} catch (NoSuchFieldException e) {
			Assertions.fail("Field 'board' of class 'PinguGame' mustn't be renamed to maintain functionality of tests!");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		Object[][] figures = null;
		try {
			Field figuresField = Board.class.getDeclaredField("figures");
			figuresField.setAccessible(true);

			figures = (Object[][]) figuresField.get(board);
		} catch (NoSuchFieldException e) {
			Assertions.fail("Field 'figures' of class 'Board' mustn't be renamed to maintain functionality of tests!");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		setFigureAttributes(figures[playerID - 1][figureID - 1], isHome, reachedGoal, position);
	}

	private static void setFigureAttributes(Object figure, boolean isHome, boolean reachedGoal, int position) {
		try {
			Field isHomeField = figure.getClass().getDeclaredField("isHome");
			isHomeField.setAccessible(true);

			isHomeField.set(figure, isHome);
		} catch (NoSuchFieldException e) {
			Assertions.fail("Field 'isHome' of class 'Figure' mustn't be renamed to maintain functionality of tests!");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		try {
			Field isHomeField = figure.getClass().getDeclaredField("reachedGoal");
			isHomeField.setAccessible(true);

			isHomeField.set(figure, reachedGoal);
		} catch (NoSuchFieldException e) {
			Assertions.fail("Field 'reachedGoal' of class 'Figure' mustn't be renamed to maintain functionality of tests!");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		try {
			Field isHomeField = figure.getClass().getDeclaredField("position");
			isHomeField.setAccessible(true);

			isHomeField.set(figure, position);
		} catch (NoSuchFieldException e) {
			Assertions.fail("Field 'position' of class 'Figure' mustn't be renamed to maintain functionality of tests!");
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
