package pgdp;

import org.junit.jupiter.api.Test;
import pgdp.warmup.PenguWarmup;

class PenguWarmupTest extends PgdpTestBase {

    @Test
    void penguInfoOut() {
        final var methodName = "penguInfoOut";

        whenCallingMethod(() -> PenguWarmup.penguInfoOut(-99))
            .withTheFollowingDescribingData(methodName, -99)
            .theConsole()
            .mustContainExactly("Penguin -99 is not a known penguin!");

        whenCallingMethod(() -> PenguWarmup.penguInfoOut(1))
            .withTheFollowingDescribingData(methodName, 1)
            .theConsole().mustContainExactly(
                "Penguin: 1",
                "This penguin is a female."
            );

        whenCallingMethod(() -> PenguWarmup.penguInfoOut(2))
            .withTheFollowingDescribingData(methodName, 2)
            .theConsole().mustContainExactly(
                "Penguin: 2",
                "This penguin is a male."
            );

        whenCallingMethod(() -> PenguWarmup.penguInfoOut(0))
            .withTheFollowingDescribingData(methodName, 0)
            .theConsole().mustContainExactly(
                "Penguin: 0",
                "This penguin is a male."
            );

        whenCallingMethod(() -> PenguWarmup.penguInfoOut(Integer.MAX_VALUE))
            .withTheFollowingDescribingData(methodName, Integer.MAX_VALUE)
            .theConsole().mustContainExactly(
                "Penguin: " + Integer.MAX_VALUE,
                "This penguin is a female."
            );

        whenCallingMethod(() -> PenguWarmup.penguInfoOut(-1))
            .withTheFollowingDescribingData(methodName, -1)
            .theConsole()
            .mustContainExactly("Penguin -1 is not a known penguin!");
    }

    @Test
    void checkPenguEvolutions() {
        final var methodName = "penguEvolution";

        whenCallingMethod(() -> PenguWarmup.penguEvolution(128, 2))
            .withTheFollowingDescribingData(methodName, 128, 2)
            .itShouldReturn(4);

        whenCallingMethod(() -> PenguWarmup.penguEvolution(9, 9))
            .withTheFollowingDescribingData(methodName, 9, 9)
            .itShouldReturn(7);

        whenCallingMethod(() -> PenguWarmup.penguEvolution(9, 10))
            .withTheFollowingDescribingData(methodName, 9, 10)
            .itShouldReturn(22);
    }

    @Test
    void checkPenguSum() {
        final var methodName = "penguSum";

        whenCallingMethod(() -> PenguWarmup.penguSum(128))
            .withTheFollowingDescribingData(methodName, 128)
            .itShouldReturn(11);

        whenCallingMethod(() -> PenguWarmup.penguSum(1337))
            .withTheFollowingDescribingData(methodName, 1337)
            .itShouldReturn(14);

        whenCallingMethod(() -> PenguWarmup.penguSum(54354))
            .withTheFollowingDescribingData(methodName, 54354)
            .itShouldReturn(21);

        whenCallingMethod(() -> PenguWarmup.penguSum(1000))
            .withTheFollowingDescribingData(methodName, 1)
            .itShouldReturn(1);

        whenCallingMethod(() -> PenguWarmup.penguSum(0))
            .withTheFollowingDescribingData(methodName, 0)
            .itShouldReturn(0);

        whenCallingMethod(() -> PenguWarmup.penguSum(9))
            .withTheFollowingDescribingData(methodName, 9)
            .itShouldReturn(9);

        whenCallingMethod(() -> PenguWarmup.penguSum(4004))
            .withTheFollowingDescribingData(methodName, 4004)
            .itShouldReturn(8);

        whenCallingMethod(() -> PenguWarmup.penguSum(1234567))
            .withTheFollowingDescribingData(methodName, 1234567)
            .itShouldReturn(28);
    }

    @Test
    void checkPenguPermutation() {
        final var methodName = "penguPermutation";

        whenCallingMethod(() -> PenguWarmup.penguPermutation(6, 3))
            .withTheFollowingDescribingData(methodName, 6, 3)
            .itShouldReturn(120L);

        whenCallingMethod(() -> PenguWarmup.penguPermutation(21, 19))
            .withTheFollowingDescribingData(methodName, 21, 19)
            .itShouldReturn(420L);

        whenCallingMethod(() -> PenguWarmup.penguPermutation(10, 7))
            .withTheFollowingDescribingData(methodName, 10, 7)
            .itShouldReturn(720L);

        whenCallingMethod(() -> PenguWarmup.penguPermutation(42, 42))
            .withTheFollowingDescribingData(methodName, 42, 42)
            .itShouldReturn(1L);

        whenCallingMethod(() -> PenguWarmup.penguPermutation(500000000000001L, 500000000000000L))
            .withTheFollowingDescribingData(methodName, 500000000000001L, 500000000000000L)
            .itShouldReturn(500000000000001L);
    }

    @Test
    void checkPenguPowers() {
        final var methodName = "penguPowers";

        whenCallingMethod(() -> PenguWarmup.penguPowers(1337, 2))
            .withTheFollowingDescribingData(methodName, 1337, 2)
            .itShouldReturn(1787569L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(-1337, 2))
            .withTheFollowingDescribingData(methodName, -1337, 2)
            .itShouldReturn(1787569L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(-8, 3))
            .withTheFollowingDescribingData(methodName, -8, 3)
            .itShouldReturn(-512L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(3, 4))
            .withTheFollowingDescribingData(methodName, 3, 4)
            .itShouldReturn(81L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(3, 0))
            .withTheFollowingDescribingData(methodName, 3, 0)
            .itShouldReturn(1L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(9, 1))
            .withTheFollowingDescribingData(methodName, 9, 1)
            .itShouldReturn(9L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(-9, 2))
            .withTheFollowingDescribingData(methodName, -9, 2)
            .itShouldReturn(81L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(-3, 3))
            .withTheFollowingDescribingData(methodName, -3, 3)
            .itShouldReturn(-27L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(1, -1))
            .withTheFollowingDescribingData(methodName, 1, -1)
            .itShouldReturn(1L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(1, -42))
            .withTheFollowingDescribingData(methodName, 1, -42)
            .itShouldReturn(1L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(0, 1))
            .withTheFollowingDescribingData(methodName, 0, 1)
            .itShouldReturn(0L);

        whenCallingMethod(() -> PenguWarmup.penguPowers(0, 0))
            .withTheFollowingDescribingData(methodName, 0, 0)
            .itShouldReturn(1L);
    }

}
