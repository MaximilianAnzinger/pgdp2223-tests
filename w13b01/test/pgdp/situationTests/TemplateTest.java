package pgdp.situationTests;

import org.junit.jupiter.api.Assertions;
import pgdp.game.PinguGame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static pgdp.GameReflectionUtils.getBoardFields;
import static pgdp.GameReflectionUtils.setFigureAttributes;

//Just a class with Test Method templates, No tests!!!
class TemplateTest {

	void templateCommentedTest() {
		//Template Test Method body

		//Insert expected output here
		String expected = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		//Initialize ByteArrayInputStream with simulated user inputs, seperated by '\n'
		System.setIn(new ByteArrayInputStream("4\n1\n2".getBytes()));

		//Initialize SpecificPinguGame with results of dice rolls, Game will terminate by next dice roll, if all dices have already been rolled
		PinguGame game = new SpecificPinguGame(6);

		//Set attributes and position of a figure
		setFigureAttributes(game, 1, 1, false, false, 14);
		getBoardFields(game)[14] = 1100;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	void templateTest() {
		String expected = "";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new ByteArrayInputStream("4\n1\n2".getBytes()));

		PinguGame game = new SpecificPinguGame(6);

		setFigureAttributes(game, 1, 1, false, false, 14);
		getBoardFields(game)[14] = 1100;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}
}