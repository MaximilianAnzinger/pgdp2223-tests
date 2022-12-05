package pgdp.sim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class SimulationTest {
    void runTest(String seed, int width, int height, String[] states) {
        runTest(seed.getBytes(StandardCharsets.UTF_8), width, height, states);
    }

    void runTest(byte[] seed, int width, int height, String[] states) {
        RandomGenerator.reseed(seed);
        var cells = FieldVisualizeHelper.fieldDescriptionToCellArray(states[0], width, height);
        var a = new Simulation(cells, width, height);
        for (int i = 1; i < states.length; i++) {
            a.tick();
            Assertions.assertEquals(states[i] + "\n", FieldVisualizeHelper.cellArrayToFieldDescription(a.getCells(), width, height, false), "State " + i);
        }
    }

    @Test
    @DisplayName("One single plant only")
    void singlePlant() {
        var states = new String[]{"""
                p . . . .
                . . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p . . . .
                w . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p w . . .
                w . . . .
                . . . . .
                . . . . .
                . . . . .""", """
                p w w . .
                w w . . .
                w . . . .
                . . . . .
                . . . . .""", """
                p w w w .
                w w w . .
                w w . . .
                . w . . .
                . . . . ."""};
        runTest("plant", 5, 5, states);
    }
}
