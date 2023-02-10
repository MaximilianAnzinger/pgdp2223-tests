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

public class CaptureTest {
	@Test
	@Timeout(2)
	@DisplayName("First player captures Piece 3 of Player 2")
	void standardCaptureTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t2⌂\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t11\t23\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 1 gewürfelt.
				Eine der folgenden Figuren kann eine gegnerische Figur schlagen (bitte auswählen): 1
				> 1
				Figur 3 von Pinguin 2 wurde geschlagen.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t11\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
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

		PinguGame game = new SpecificPinguGame(1);

		setFigureAttributes(game, 1, 1, false, false, 1);
		getBoardFields(game)[1] = 1100;

		setFigureAttributes(game, 2, 3, false, false, 2);
		getBoardFields(game)[2] = 2300;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("Capture has priority over ordinary move")
	void capturePrioTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t12\t \t o\t o\t11\t \t21\t22\t
				1⌂\t \t \t23\t2x\t o\t \t \t2⌂\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t13\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 4 gewürfelt.
				Eine der folgenden Figuren kann eine gegnerische Figur schlagen (bitte auswählen): 3
				> 3
				Figur 3 von Pinguin 2 wurde geschlagen.
				1⌂\t12\t \t o\t o\t11\t \t21\t22\t
				1⌂\t \t \t13\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 2 ist am Zug.
				Pinguin 2 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		System.setIn(new GameInputStream("4", "3"));

		PinguGame game = new SpecificPinguGame(4);

		setFigureAttributes(game, 1, 3, false, false, 1);
		getBoardFields(game)[1] = 1300;

		setFigureAttributes(game, 1, 1, false, false, 8);
		getBoardFields(game)[8] = 1100;

		setFigureAttributes(game, 2, 3, false, false, 5);
		getBoardFields(game)[5] = 2300;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("Figure is captured during ordinary move (artemis example #7)")
	void captureOrdinaryTest() {
		String expected = "Willkommen zu \"Pingu ärgere dich nicht\"!\n" +
				"Wie viele Pinguine wollen spielen?\n" +
				"Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!\n" +
				"> 4\n" +
				"Starte Spiel mit 4 \"echten\" und 0 KI Pinguinen.\n" +
				"1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t\n" +
				"13\t \t \t o\t2x\t o\t \t \t2⌂\t\n" +
				" \t \t \t o\t2x\t o\t \t \t \t\n" +
				"1⊚\t11\t23\t o\t2x\t o\t o\t o\t o\t\n" +
				" o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t\n" +
				" o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t\n" +
				" \t \t \t o\t4x\t o\t \t \t \t\n" +
				"41\t \t \t o\t4x\t o\t \t \t31\t\n" +
				"42\t43\t \t4⊚\t o\t o\t \t32\t33\t\n" +
				"\n" +
				"Pinguin 1 ist am Zug.\n" +
				"Pinguin 1 hat eine 1 gewürfelt.\n" +
				"Eine der folgenden Figuren kann eine gegnerische Figur schlagen (bitte auswählen): 1\n" +
				"> 1\n" +
				"Figur 3 von Pinguin 2 wurde geschlagen.\n" +
				"1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t\n" +
				"13\t \t \t o\t2x\t o\t \t \t23\t\n" +
				" \t \t \t o\t2x\t o\t \t \t \t\n" +
				"1⊚\t o\t11\t o\t2x\t o\t o\t o\t o\t\n" +
				" o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t\n" +
				" o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t\n" +
				" \t \t \t o\t4x\t o\t \t \t \t\n" +
				"41\t \t \t o\t4x\t o\t \t \t31\t\n" +
				"42\t43\t \t4⊚\t o\t o\t \t32\t33\t\n" +
				"\n" +
				"Pinguin 2 ist am Zug.\n" +
				"Pinguin 2 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.\n";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new GameInputStream("4", "1"));

		PinguGame game = new SpecificPinguGame(1);

		setFigureAttributes(game, 1, 1, false, false, 1);
		getBoardFields(game)[1] = 1100;

		setFigureAttributes(game, 2, 3, false, false, 2);
		getBoardFields(game)[2] = 2300;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}
}
