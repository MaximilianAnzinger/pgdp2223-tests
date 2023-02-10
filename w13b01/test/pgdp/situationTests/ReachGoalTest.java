package pgdp.situationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import pgdp.GameInputStream;
import pgdp.game.PinguGame;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static pgdp.GameReflectionUtils.getBoardFields;
import static pgdp.GameReflectionUtils.setFigureAttributes;

public class ReachGoalTest {

	@Test
	@Timeout(2)
	@DisplayName("First Player reaching goal by rolling 6, but can't move after next roll (artemis example #10")
	void reachGoalCantMoveTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t11\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 1
				> 1
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t11\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 hat eine 1 gewürfelt.
				Keine Figur von Pinguin 1 kann mit einer 1 bewegt werden.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t11\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 2 ist am Zug.
				Pinguin 2 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		System.setIn(new GameInputStream("4", "1"));

		PinguGame game = new SpecificPinguGame(6, 1);

		setFigureAttributes(game, 1, 1, false, false, 29);
		getBoardFields(game)[29] = 1100;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("First player reaching goal by capturing figure (artemis example #9)")
	void reachGoalByCaptureTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t2⌂\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				23\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t11\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 5 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 1
				> 1
				Beim Erreichen des Ziels wurde Figur 3 des Pinguins 2 geschlagen.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t11\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 2 ist am Zug.
				Pinguin 2 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		System.setIn(new GameInputStream("4", "1"));

		PinguGame game = new SpecificPinguGame(5);

		setFigureAttributes(game, 1, 1, false, false, 29);
		getBoardFields(game)[29] = 1100;

		setFigureAttributes(game, 2, 3, false, false, 31);
		getBoardFields(game)[31] = 2300;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("Reaching the goal should have priority over capturing enemy figure")
	void reachGoalPrioTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				1⌂\t \t \t11\t2x\t22\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t13\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 4 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 3
				> 3
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				1⌂\t \t \t11\t2x\t22\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t13\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 2 ist am Zug.
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new GameInputStream("4", "3"));

		PinguGame game = new SpecificPinguGame(4);

		setFigureAttributes(game, 1, 3, false, false, 28);
		getBoardFields(game)[28] = 1300;

		setFigureAttributes(game, 1, 1, false, false, 5);
		getBoardFields(game)[5] = 1100;

		setFigureAttributes(game, 2, 2, false, false, 9);
		getBoardFields(game)[9] = 2200;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("Can every Player reach their Goal?")
	void everyGoalReachTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				11\t12\t \t21\t o\t2⊚\t \t2⌂\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t32\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t13\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t41\t \t \t \t
				4⌂\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t3⌂\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 4 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 3
				> 3
				11\t12\t \t21\t o\t2⊚\t \t2⌂\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t32\t o\t o\t o\t
				 o\t1x\t1x\t13\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t41\t \t \t \t
				4⌂\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t3⌂\t33\t

				Pinguin 2 ist am Zug.
				Pinguin 2 hat eine 1 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 1
				> 1
				11\t12\t \t o\t o\t2⊚\t \t2⌂\t22\t
				1⌂\t \t \t o\t21\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t32\t o\t o\t o\t
				 o\t1x\t1x\t13\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t41\t \t \t \t
				4⌂\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t3⌂\t33\t

				Pinguin 3 ist am Zug.
				Pinguin 3 hat eine 5 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 2
				> 2
				11\t12\t \t o\t o\t2⊚\t \t2⌂\t22\t
				1⌂\t \t \t o\t21\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t13\t \t3x\t32\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t41\t \t \t \t
				4⌂\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t3⌂\t33\t

				Pinguin 4 ist am Zug.
				Pinguin 4 hat eine 3 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 1
				> 1
				11\t12\t \t o\t o\t2⊚\t \t2⌂\t22\t
				1⌂\t \t \t o\t21\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t13\t \t3x\t32\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				4⌂\t \t \t o\t41\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t3⌂\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new GameInputStream("4", "3", "1", "2", "1"));

		PinguGame game = new SpecificPinguGame(4, 1, 5, 3);

		setFigureAttributes(game, 1, 3, false, false, 29);
		getBoardFields(game)[29] = 1300;

		setFigureAttributes(game, 2, 1, false, false, 6);
		getBoardFields(game)[6] = 2100;

		setFigureAttributes(game, 3, 2, false, false, 11);
		getBoardFields(game)[11] = 3200;

		setFigureAttributes(game, 4, 1, false, false, 20);
		getBoardFields(game)[20] = 4100;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

}
