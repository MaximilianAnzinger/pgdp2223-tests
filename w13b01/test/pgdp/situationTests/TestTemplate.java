package pgdp.situationTests;

import org.junit.jupiter.api.Assertions;
import pgdp.GameInputStream;
import pgdp.game.PinguGame;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static pgdp.GameReflectionUtils.getBoardFields;
import static pgdp.GameReflectionUtils.setFigureAttributes;

//Just a class with Test Method templates, No tests!!!
class TestTemplate {

	void testTemplateCommented() {
		//Template Test Method body

		//Insert expected output here
		String expected = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		//Initialize ByteArrayInputStream with simulated user inputs, seperated by '\n'
		System.setIn(new GameInputStream("4", "2", "1"));

		//Initialize SpecificPinguGame with results of dice rolls, Game will terminate by next dice roll, if all dices have already been rolled
		PinguGame game = new SpecificPinguGame(6);

		//Set attributes and position of a figure
		setFigureAttributes(game, 1, 1, false, false, 14);
		getBoardFields(game)[14] = 1100;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	void testTemplate() {
		String expected = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new GameInputStream("4", "2", "1"));

		PinguGame game = new SpecificPinguGame(6);

		setFigureAttributes(game, 1, 1, false, false, 14);
		getBoardFields(game)[14] = 1100;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}
}