package pgdp.situationTests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import pgdp.GameInputStream;
import pgdp.game.PinguGame;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static pgdp.GameReflectionUtils.*;

public class LeaveHomeTest {

	@Test
	@Timeout(2)
	@DisplayName("First Player didn't roll a six => Cannot leave home")
	void noSixTest() {
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
				Pinguin 1 hat eine 4 gewürfelt.
				Pinguin 1 hat eine 5 gewürfelt.
				Pinguin 1 hat eine 1 gewürfelt.
				Schade, keine 6. Mehr Glück nächste Runde!
				11\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
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
		System.setIn(new GameInputStream("0"));

		PinguGame game = new SpecificPinguGame(4, 5, 1);

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("First Player rolls six on 2nd try - 2nd Player figure on 1st Player Start Pos => Gets captured")
	void captureOnLeaveTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				11\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				22\t o\t o\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				Pinguin 1 hat eine 3 gewürfelt.
				Pinguin 1 hat eine 6 gewürfelt.
				Welche Figur möchtest du aufs Spielfeld ziehen? Bitte wählen: 1, 2, 3
				> 1
				Die Figur 2 von Pinguin 2 wurde beim Verlassen des Hauses geschlagen.
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
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		System.setIn(new GameInputStream("4", "1"));

		PinguGame game = new SpecificPinguGame(3, 6);

		setFigureAttributes(game, 2, 2, false, false, 0);
		getBoardFields(game)[0] = 2200;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("First player leaves home and can only go less than rolled (artemis example #5)")
	void goLessAfterLeaveTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t11\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann bewegt werden (bitte auswählen): 1, 2, 3
				> 2
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				12\t o\t11\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 muss das Startfeld räumen.
				Pinguin 1 hat eine 2 gewürfelt.
				Feld bereits belegt, es kann/können nur 1 Feld(er) gegangen werden.
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t12\t11\t o\t2x\t o\t o\t o\t o\t
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
		System.setIn(new GameInputStream("4", "2"));

		PinguGame game = new SpecificPinguGame(6, 2);

		setFigureAttributes(game, 1, 1, false, false, 2);
		getBoardFields(game)[2] = 1100;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("First player leaves home on 1st roll and can go more")
	void goMoreAfterLeaveTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				11\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t12\t13\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann bewegt werden (bitte auswählen): 1, 2, 3
				> 1
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				11\t12\t13\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 muss das Startfeld räumen.
				Pinguin 1 hat eine 1 gewürfelt.
				Feld bereits belegt, es dürfen sogar 3 Felder gegangen werden.
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				1⌂\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t12\t13\t11\t2x\t o\t o\t o\t o\t
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

		PinguGame game = new SpecificPinguGame(6, 1);

		setFigureAttributes(game, 1, 2, false, false, 1);
		getBoardFields(game)[1] = 1200;

		setFigureAttributes(game, 1, 3, false, false, 2);
		getBoardFields(game)[2] = 1300;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("First player can only go less after leaving and captures figure")
	void lessAndCaptureTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
				11\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t2⌂\t
				 \t \t \t12\t2x\t o\t \t \t \t
				1⊚\t o\t o\t23\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 ist am Zug.
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann bewegt werden (bitte auswählen): 1, 2, 3
				> 1
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t2⌂\t
				 \t \t \t12\t2x\t o\t \t \t \t
				11\t o\t o\t23\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 muss das Startfeld räumen.
				Pinguin 1 hat eine 4 gewürfelt.
				Feld bereits belegt, es kann/können nur 3 Feld(er) gegangen werden und dabei wird eine andere Figur geschlagen.
				Figur 3 von Pinguin 2 wurde geschlagen.
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t12\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t o\t o\t
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

		PinguGame game = new SpecificPinguGame(6, 4);

		setFigureAttributes(game, 1, 2, false, false, 4);
		getBoardFields(game)[4] = 1200;

		setFigureAttributes(game, 2, 3, false, false, 3);
		getBoardFields(game)[3] = 2300;

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

	@Test
	@Timeout(2)
	@DisplayName("First player can go more after leaving and captures figure (artemis example #6)")
	void moreAndCaptureTest() {
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
				Pinguin 1 hat eine 6 gewürfelt.
				Eine der folgenden Figuren kann bewegt werden (bitte auswählen): 1, 2, 3
				> 2
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t2⌂\t
				 \t \t \t o\t2x\t o\t \t \t \t
				12\t11\t23\t o\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 1 muss das Startfeld räumen.
				Pinguin 1 hat eine 1 gewürfelt.
				Feld bereits belegt, es dürfen sogar 2 Felder gegangen werden und dabei wird eine andere Figur geschlagen.
				Figur 3 von Pinguin 2 wurde geschlagen.
				1⌂\t1⌂\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t11\t12\t o\t2x\t o\t o\t o\t o\t
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
		System.setIn(new GameInputStream("4", "2"));

		PinguGame game = new SpecificPinguGame(6, 1);

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
	@DisplayName("Can every player leave their home?")
	void everyHomeLeaveTest() {
		String expected = """
				Willkommen zu "Pingu ärgere dich nicht"!
				Wie viele Pinguine wollen spielen?
				Bitte eine Zahl von 0 (nur KI) bis 4 eingeben!
				> 4
				Starte Spiel mit 4 "echten" und 0 KI Pinguinen.
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
				Pinguin 1 hat eine 6 gewürfelt.
				Welche Figur möchtest du aufs Spielfeld ziehen? Bitte wählen: 1, 2, 3
				> 1
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
				Pinguin 1 hat eine 3 gewürfelt.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t22\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 2 ist am Zug.
				Pinguin 2 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				Pinguin 2 hat eine 6 gewürfelt.
				Welche Figur möchtest du aufs Spielfeld ziehen? Bitte wählen: 1, 2, 3
				> 2
				1⌂\t12\t \t o\t o\t22\t \t21\t2⌂\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t o\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 2 muss das Startfeld räumen.
				Pinguin 2 hat eine 5 gewürfelt.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t22\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t33\t

				Pinguin 3 ist am Zug.
				Pinguin 3 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				Pinguin 3 hat eine 6 gewürfelt.
				Welche Figur möchtest du aufs Spielfeld ziehen? Bitte wählen: 1, 2, 3
				> 3
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t22\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t o\t o\t33\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t3⌂\t

				Pinguin 3 muss das Startfeld räumen.
				Pinguin 3 hat eine 2 gewürfelt.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t22\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t33\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				41\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t3⌂\t

				Pinguin 4 ist am Zug.
				Pinguin 4 hat keine Figur auf dem Feld und braucht eine 6. Er darf bis zu 3-mal würfeln.
				Pinguin 4 hat eine 6 gewürfelt.
				Welche Figur möchtest du aufs Spielfeld ziehen? Bitte wählen: 1, 2, 3
				> 1
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t22\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t o\t o\t o\t4x\t o\t33\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				4⌂\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t41\t o\t o\t \t32\t3⌂\t

				Pinguin 4 muss das Startfeld räumen.
				Pinguin 4 hat eine 5 gewürfelt.
				1⌂\t12\t \t o\t o\t2⊚\t \t21\t2⌂\t
				13\t \t \t o\t2x\t o\t \t \t23\t
				 \t \t \t o\t2x\t o\t \t \t \t
				1⊚\t o\t o\t11\t2x\t o\t o\t22\t o\t
				 o\t1x\t1x\t1x\t \t3x\t3x\t3x\t o\t
				 o\t41\t o\t o\t4x\t o\t33\t o\t3⊚\t
				 \t \t \t o\t4x\t o\t \t \t \t
				4⌂\t \t \t o\t4x\t o\t \t \t31\t
				42\t43\t \t4⊚\t o\t o\t \t32\t3⌂\t

				Pinguin 1 ist am Zug.
				""";

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));

		System.setIn(new GameInputStream("4", "1", "2", "3", "1"));

		PinguGame game = new SpecificPinguGame(6, 3, 6, 5, 6, 2, 6, 5);

		game.play();

		System.out.flush();
		Assertions.assertEquals(expected, out.toString().replaceAll("\r", ""));
	}

}
