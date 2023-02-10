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

public class AITest {

	@Test
	@Timeout(2)
	@DisplayName("AI should always choose figure with lowest ID, even when able to leave home with other figure")
	void aiLowestChoiceTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 0
				Starte Spiel mit 0 "echten" und 4 KI Pinguinen.
				1⌂\t12\t \t11\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t11\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann bewegt werden (bitte auswählen): 1, 2, 3
				KI wählt Figur 1.
				1⌂\t12\t \t11\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t11\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new GameInputStream("0"));

		PinguGame game = new SpecificPinguGame(6);

		setFigureAttributes(game, 1, 1, false, false, 14);
		getBoardFields(game)[14] = 1100;

		setFigureAttributes(game, 1, 3, false, false, 6);
		getBoardFields(game)[6] = 1103;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("AI start of game and first move (artemis example #12)")
	void startFirstMoveTest() {
		String expected = """
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
				Pinguin 1 hat eine 1 gewürfelt.
				Pinguin 1 hat eine 6 gewürfelt.
				Welche Figur möchtest du aufs Spielfeld ziehen? Bitte wählen: 1, 2, 3
				KI wählt Figur 1.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				11\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 muss das Startfeld räumen.
				Pinguin 1 hat eine 2 gewürfelt.
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

		System.setIn(new GameInputStream("0"));

		PinguGame game = new SpecificPinguGame(1, 6, 2);

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}
}
