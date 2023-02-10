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

public class VictoryTest {

	@Test
	@Timeout(2)
	@DisplayName("Victory, New round query, wrong input (artemis example #11)")
	void vicNewRoundQWITest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t12\t13\t \t3x\t3x\t3x\t o\t
				 o\t11\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 1
				> 1
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t11\t12\t13\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Herzlichen Glückwunsch Pinguin 1, du hast gewonnen!!!
				Soll ein neues Spiel gestartet werden? 1 - Ja, 0 - Nein
				> nein
				Keine gültige Zahl!
				> 2
				> 1
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 0
				Starte Spiel mit 0 "echten" und 4 KI Pinguinen.
				11\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		System.setIn(new GameInputStream("4", "1", "nein", "2", "1", "0"));

		PinguGame game = new SpecificPinguGame(6);

		setFigureAttributes(game, 1, 1, false, false, 29);
		getBoardFields(game)[29] = 1100;

		setFigureAttributes(game, 1, 2, false, true, -1);
		setFigureAttributes(game, 1, 3, false, true, -1);

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("Victory while capturing enemy figure")
	void vicWithCaptureTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t2⌂\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				22\t11\t1x\t13\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t12\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann das Ziel erreichen (bitte auswählen): 2
				> 2
				Beim Erreichen des Ziels wurde Figur 2 des Pinguins 2 geschlagen.
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t11\t12\t13\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Herzlichen Glückwunsch Pinguin 1, du hast gewonnen!!!
				Soll ein neues Spiel gestartet werden? 1 - Ja, 0 - Nein
				> 0
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new GameInputStream("4", "2", "0"));

		PinguGame game = new SpecificPinguGame(6);

		setFigureAttributes(game, 1, 2, false, false, 27);
		getBoardFields(game)[27] = 1200;

		setFigureAttributes(game, 1, 1, false, true, -1);
		setFigureAttributes(game, 1, 3, false, true, -1);

		setFigureAttributes(game, 2, 2, false, false, 31);
		getBoardFields(game)[31] = 2200;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}
}
