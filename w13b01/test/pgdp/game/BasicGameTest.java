package pgdp.game;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import pgdp.GameInputStream;
import pgdp.situationTests.SpecificPinguGame;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

//THESE TESTS PRODUCE OUTPUT FILES. IN CASE OF WRONG IMPLEMENTATION, THE HUMAN GAME MIGHT PRODUCE A RELATIVELY LARGE OUTPUT FILE (<1 MB)
public class BasicGameTest
{
    private PinguGame pinguGame;


    @Test
    @DisplayName("One whole Game, standard Seed")
    public void oneGame() throws IOException
    {
        File expected = new File("./test/pgdp/game/OneWholeGame.txt");
        File actual = new File("./test/pgdp/game/Out_OneWholeGame.txt");

        PrintStream t = new PrintStream(actual);
        System.setOut(t);
        ByteArrayInputStream in = new ByteArrayInputStream("0\n0\n".getBytes());
        System.setIn(in);
        PinguGame c = new PinguGame(true, 420, 0);
        c.play();

        String expectedString = Files.readString(expected.toPath());
        String actualString = Files.readString(actual.toPath()).replaceAll("\r", "");
        Assertions.assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Five whole Games, standard Seed")
    public void fiveGames() throws IOException
    {
        File expected = new File("./test/pgdp/game/FiveWholeGames.txt");
        File actual = new File("./test/pgdp/game/Out_FiveWholeGames.txt");

        PrintStream t = new PrintStream(actual);
        System.setOut(t);
        ByteArrayInputStream in = new ByteArrayInputStream("0\n1\n0\n1\n0\n1\n0\n1\n0\n0\n".getBytes());
        System.setIn(in);
        PinguGame c = new PinguGame(true, 420, 0);
        c.play();

        String expectedString = Files.readString(expected.toPath());
        String actualString = Files.readString(actual.toPath()).replaceAll("\r", "");
        Assertions.assertEquals(expectedString, actualString);
    }

    @Test
    @DisplayName("Five whole Games, non standard Seed")
    public void fiveGamesNonStandardSeed() throws IOException
    {
        File expected = new File("./test/pgdp/game/FiveWholeGamesNonStandardSeed.txt");
        File actual = new File("./test/pgdp/game/Out_FiveWholeGamesNonStandardSeed.txt");

        PrintStream t = new PrintStream(actual);
        System.setOut(t);
        ByteArrayInputStream in = new ByteArrayInputStream("0\n1\n0\n1\n0\n1\n0\n1\n0\n0\n".getBytes());
        System.setIn(in);
        PinguGame c = new PinguGame(true, 42069, 0);
        c.play();

        String expectedString = Files.readString(expected.toPath());
        String actualString = Files.readString(actual.toPath()).replaceAll("\r", "");
        Assertions.assertEquals(expectedString, actualString);
    }

    //SIMULATES ONE HUMAN PLAYER, MIGHT TIME OUT IF WRONG OR SLOW IMPLEMENTATION AND PRODUCE MODERATELY LARGE OUTPUT FILE
    @Test
    @Timeout(value = 2, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @DisplayName("One Human Player, standard Seed, if it timeouts, its wrong")
    public void HumanPlayerStandardSeed() throws IOException
    {
        File expected = new File("./test/pgdp/game/HumanPlayerStandardSeed.txt");
        File actual = new File("./test/pgdp/game/Out_HumanPlayerStandardSeed.txt");

        PrintStream t = new PrintStream(actual);
        System.setOut(t);
        ByteArrayInputStream in = new ByteArrayInputStream("1\n1\n2\n1\n1\n1\n2\n1\n2\n1\n1\n1\n2\n1\n1\n1\n2\n1\n2\n1\n1\n1\n2\n1\n1\n1\n2\n1\n2\n1\n2\n1\n1\n1\n2\n1\n2\n1\n2\n1\n1\n1\n3\n3\n3\n3\n3\n3\n3\n3\n3\n3\n3\n3\n0\n".getBytes());
        System.setIn(in);
        PinguGame c = new PinguGame(true, 420, 5);
        c.play();
        String expectedString = Files.readString(expected.toPath());
        String actualString = Files.readString(actual.toPath()).replaceAll("\r", "");
        Assertions.assertEquals(expectedString, actualString);
    }

    //SIMULATES FOUR HUMANOID PLAYERS(pressing 0,1,2,3 in that order shared across all players, plus one extra 4 at the beginning to start with 4 humanoid players), ONE GAME, ASCENDING DICE ROLLS, ADDED TIMEOUT
    @Test
    @Timeout(value = 2, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    @DisplayName("Four humanoid Players, set Dice Roll")
    public void humanoidPlayersStandardSeed() throws IOException
    {
        File expected = new File("./test/pgdp/game/HumanoidPlayersStandardSeed.txt");
        File actual = new File("./test/pgdp/game/Out_HumanoidPlayersStandardSeed.txt");

        PrintStream t = new PrintStream(actual);
        System.setOut(t);
        List<String> stringInput = new ArrayList<>();
        stringInput.add(String.valueOf(4));
        for (int i = 0; i < 10000; i++)
        {
            stringInput.add(String.valueOf(i % 4));
        }
        List<Integer> diceRolls = new ArrayList<>();
        for (int i = 0; i < 1000; i++)
        {
            diceRolls.add((i%6) + 1);
        }

        System.setIn(new GameInputStream(stringInput));
        PinguGame c = new SpecificPinguGame(diceRolls);
        c.play();
        String expectedString = Files.readString(expected.toPath());
        String actualString = Files.readString(actual.toPath()).replaceAll("\r", "");
        Assertions.assertEquals(expectedString, actualString);
    }
}
