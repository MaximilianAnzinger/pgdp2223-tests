package pgdp;

import pgdp.math.PinguSqrt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

public class UnitTest2 {

	private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private static final PrintStream originalOut = System.out;
	private static final PrintStream originalErr = System.err;

	@BeforeAll
	public static void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@AfterAll
	public static void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	public void assertOutput(String content) {
		String[] lines = outContent.toString().split("\n");
		String last_line = lines[lines.length - 1];
		assertEquals(content, last_line);
	}
	
	@Test
	@DisplayName("It should calculate the square roots of example cases")
	void example_sqrt() {
		PinguSqrt.sqrt(1049.76);
		assertOutput("Ergebnis: 32.4");

		PinguSqrt.sqrt(4.0);
		assertOutput("Ergebnis: 2.0");
	}

	@Test
	@DisplayName("Should calculate natural square roots")
	void natural_sqrt() {

		PinguSqrt.sqrt(0.0);
		assertOutput("Ergebnis: 0.0");

		PinguSqrt.sqrt(1.0);
		assertOutput("Ergebnis: 1.0");

		PinguSqrt.sqrt(4.0);
		assertOutput("Ergebnis: 2.0");

		PinguSqrt.sqrt(9.0);
		assertOutput("Ergebnis: 3.0");

		PinguSqrt.sqrt(16.0);
		assertOutput("Ergebnis: 4.0");

		PinguSqrt.sqrt(25.0);
		assertOutput("Ergebnis: 5.0");

		PinguSqrt.sqrt(36.0);
		assertOutput("Ergebnis: 6.0");

		PinguSqrt.sqrt(49.0);
		assertOutput("Ergebnis: 7.0");

		PinguSqrt.sqrt(64.0);
		assertOutput("Ergebnis: 8.0");

		PinguSqrt.sqrt(81.0);
		assertOutput("Ergebnis: 9.0");

		PinguSqrt.sqrt(100.0);
		assertOutput("Ergebnis: 10.0");

		PinguSqrt.sqrt(2500.0);
		assertOutput("Ergebnis: 50.0");

		PinguSqrt.sqrt(360000.0);
		assertOutput("Ergebnis: 600.0");

		PinguSqrt.sqrt(49000000.0);
		assertOutput("Ergebnis: 7000.0");

		PinguSqrt.sqrt(1000000.0);
		assertOutput("Ergebnis: 1000.0");
	}

	@Test
	@DisplayName("It should calculate the square roots of numbers near zero")
	void comma_sqrt() {

		PinguSqrt.sqrt(0.01);
		assertOutput("Ergebnis: 0.1");

		PinguSqrt.sqrt(0.04);
		assertOutput("Ergebnis: 0.2");

		PinguSqrt.sqrt(0.09);
		assertOutput("Ergebnis: 0.3");

		PinguSqrt.sqrt(0.16);
		assertOutput("Ergebnis: 0.4");

		PinguSqrt.sqrt(0.25);
		assertOutput("Ergebnis: 0.5");

		PinguSqrt.sqrt(0.36);
		assertOutput("Ergebnis: 0.6");

		PinguSqrt.sqrt(0.49);
		assertOutput("Ergebnis: 0.7");

		PinguSqrt.sqrt(0.64);
		assertOutput("Ergebnis: 0.8");

		PinguSqrt.sqrt(0.81);
		assertOutput("Ergebnis: 0.9");

		PinguSqrt.sqrt(0.0001);
		assertOutput("Ergebnis: 0.01");

		PinguSqrt.sqrt(0.0004);
		assertOutput("Ergebnis: 0.02");

		PinguSqrt.sqrt(0.0009);
		assertOutput("Ergebnis: 0.03");

		PinguSqrt.sqrt(0.0016);
		assertOutput("Ergebnis: 0.04");

		PinguSqrt.sqrt(0.0025);
		assertOutput("Ergebnis: 0.05");

		PinguSqrt.sqrt(0.0036);
		assertOutput("Ergebnis: 0.06");

		PinguSqrt.sqrt(0.0049);
		assertOutput("Ergebnis: 0.07");

		PinguSqrt.sqrt(0.0064);
		assertOutput("Ergebnis: 0.08");

		PinguSqrt.sqrt(0.0081);
		assertOutput("Ergebnis: 0.09");
	}

	@Test
	@DisplayName("It should calculate big numbers")
	void big_number() {

		PinguSqrt.sqrt(2147483647);
		assertOutput("Ergebnis: 46340.95");

		PinguSqrt.sqrt(2147483646.9999);
		assertOutput("Ergebnis: 46340.95");

		PinguSqrt.sqrt(2111111111.0001);
		assertOutput("Ergebnis: 45946.82");

	}

}
