package pgdp.security.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import pgdp.security.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UnitTests {
    @Nested
    @DisplayName("Unit tests for the LightPanel class")
    class LightPanelUnitTests {

        private LightPanel lightPanel;

        @BeforeEach
        void setup() {
            this.lightPanel = new LightPanel(0);
        }

        @Test
        @DisplayName("Initialize LightPanel object")
        void initializeLightPanel() {
            assertEquals(0, lightPanel.getPostNumber(),
                    "Wrong post number. Expected " + 0 + " but got " + lightPanel.getPostNumber());
            assertEquals(0, lightPanel.getLevel(),
                    "Wrong initial level. Expected " + 0 + " but got " + lightPanel.getLevel());
            assertEquals("", lightPanel.getDepiction(),
                    "Wrong initial depiction. Expected " + "" + " but got " + lightPanel.getDepiction());
        }

        @Nested
        @DisplayName("Unit tests for the up method in the LightPanel class")
        class upMethodLightPanel {
            @Test
            @DisplayName("Test invalid input for the up method - level: 0 and depiction:''")
            void invalidStringInputUpMethod() {
                boolean success = lightPanel.up("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(0, lightPanel.getLevel(),
                        "Wrong initial level. Expected " + 0 + " but got " + lightPanel.getLevel());
                assertEquals("", lightPanel.getDepiction(),
                        "Wrong initial depiction. Expected " + "" + " but got " + lightPanel.getDepiction());
            }

            @Test
            @DisplayName("Test invalid input for the up method - level: 2 and depiction:'yellow'")
            void invalidStringInputUpMethodNotInitialState() {
                lightPanel.setLevel(2);
                lightPanel.setDepiction("yellow");

                boolean success = lightPanel.up("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(2, lightPanel.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + lightPanel.getLevel());
                assertEquals("yellow", lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + lightPanel.getDepiction());
            }

            @Test
            @DisplayName("Test valid input for the up method, however, backwards direction - same level")
            void testValidInputBackwardsSameLevel() {
                lightPanel.setLevel(3);
                lightPanel.setDepiction("[SC]");

                boolean success = lightPanel.up("doubleYellow");
                assertFalse(success, "Expected the return value of false");
                assertEquals(3, lightPanel.getLevel(),
                        "Wrong level. Expected " + 3 + " but got " + lightPanel.getLevel());
                assertEquals("[SC]", lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + "[SC]" + " but got " + lightPanel.getDepiction());
            }

            @Test
            @DisplayName("Test valid input for the up method, however, backwards direction")
            void testValidInputBackwards() {
                lightPanel.setLevel(2);
                lightPanel.setDepiction("yellow");

                boolean success = lightPanel.up("green");
                assertFalse(success, "Expected the return value of false");
                assertEquals(2, lightPanel.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + lightPanel.getLevel());
                assertEquals("yellow", lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + lightPanel.getDepiction());
            }

            @Test
            @DisplayName("Check if the state would change from the level 5")
            void checkStateChangeForLastLevel() {
                lightPanel.setLevel(5);
                lightPanel.setDepiction("yellow");

                boolean success = lightPanel.up("green");
                assertFalse(success, "Expected the return value of false");
                assertEquals(5, lightPanel.getLevel(),
                        "Wrong level. Expected " + 5 + " but got " + lightPanel.getLevel());
                assertEquals("yellow", lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + lightPanel.getDepiction());
            }

            @ParameterizedTest(name = "Initial level: {0} | Initial depiction: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4} | Up method input: {5}")
            @MethodSource
            @DisplayName("Test up method with valid inputs in the correct direction.")
            void testUpMethodLightPanel(int level, String depiction, int expectedLevel, String expectedString,
                    boolean returnValue, String input) {

                // initial setup of the light panel before tests
                lightPanel.setDepiction(depiction);
                lightPanel.setLevel(level);

                boolean success = lightPanel.up(input);
                assertTrue(success, "Expected the return value of true");
                assertEquals(expectedLevel, lightPanel.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + lightPanel.getLevel());
                assertEquals(expectedString, lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + expectedString + " but got " + lightPanel.getDepiction());
            }

            static Stream<Arguments> testUpMethodLightPanel() {
                return Stream.of(
                        arguments(0, "", 1, "green", true, "green"),
                        arguments(0, "", 1, "blue", true, "blue"),
                        arguments(0, "", 3, "doubleYellow", true, "doubleYellow"),
                        arguments(3, "doubleYellow", 3, "[SC]", true, "[SC]"),
                        arguments(1, "green", 1, "blue", true, "blue"),
                        arguments(1, "blue", 4, "red", true, "red"),
                        arguments(2, "yellow", 5, "yellow", true, "end"),
                        arguments(2, "yellow", 4, "red", true, "red"));
            }
        }

        @Nested
        @DisplayName("Unit tests for the down method in the LightPanel class")
        class downMethodLightPanel {
            @Test
            @DisplayName("Test invalid input for the down method - level: 0 and depiction:''")
            void testInvalidInputInitialState() {
                boolean success = lightPanel.down("Mars");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(0, lightPanel.getLevel(),
                        "Wrong level. Expected " + 0 + " but got " + lightPanel.getLevel());
                assertEquals("", lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + "" + " but got " + lightPanel.getDepiction());
            }

            @Test
            @DisplayName("Test invalid input for the down method - level: 2 and depiction:'yellow'")
            void invalidStringInputNotInitialState() {
                lightPanel.setLevel(2);
                lightPanel.setDepiction("yellow");

                boolean success = lightPanel.down("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(2, lightPanel.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + lightPanel.getLevel());
                assertEquals("yellow", lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + lightPanel.getDepiction());
            }

            @ParameterizedTest(name = "Initial level: {0} | Initial depiction: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4} | Input type: {5}")
            @MethodSource
            @DisplayName("Test down method for blue input")
            void testDownMethod(int level, String depiction, int expectedLevel, String expectedString,
                    boolean returnValue, String type) {

                // initial setup of the light panel before tests
                lightPanel.setDepiction(depiction);
                lightPanel.setLevel(level);

                boolean success = lightPanel.down(type);
                if (returnValue) {
                    assertTrue(success, "Expected the return value of " + returnValue);
                } else {
                    assertFalse(success, "Expected the return value of " + returnValue);
                }

                assertEquals(expectedLevel, lightPanel.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + lightPanel.getLevel());
                assertEquals(expectedString, lightPanel.getDepiction(),
                        "Wrong depiction. Expected " + expectedString + " but got " + lightPanel.getDepiction());
            }

            static Stream<Arguments> testDownMethod() {
                return Stream.of(
                        // input type: clear
                        arguments(0, "", 0, "", false, "clear"),
                        arguments(5, "yellow", 0, "", true, "clear"),
                        arguments(4, "red", 0, "", true, "clear"),
                        arguments(3, "doubleYellow", 0, "", true, "clear"),
                        arguments(3, "[SC]", 0, "", true, "clear"),
                        arguments(2, "yellow", 0, "", true, "clear"),
                        arguments(1, "blue", 0, "", true, "clear"),
                        arguments(1, "green", 0, "", true, "clear"),

                        // input type: blue
                        arguments(0, "", 0, "", false, "blue"),
                        arguments(5, "yellow", 5, "yellow", false, "blue"),
                        arguments(4, "red", 4, "red", false, "blue"),
                        arguments(3, "doubleYellow", 3, "doubleYellow", false, "blue"),
                        arguments(3, "[SC]", 3, "[SC]", false, "blue"),
                        arguments(2, "yellow", 2, "yellow", false, "blue"),
                        arguments(1, "blue", 0, "", true, "blue"),
                        arguments(1, "green", 1, "green", false, "blue"),

                        // input type: green
                        arguments(0, "", 0, "", false, "green"),
                        arguments(5, "yellow", 5, "yellow", false, "green"),
                        arguments(4, "red", 4, "red", false, "green"),
                        arguments(3, "doubleYellow", 3, "doubleYellow", false, "green"),
                        arguments(3, "[SC]", 3, "[SC]", false, "green"),
                        arguments(2, "yellow", 2, "yellow", false, "green"),
                        arguments(1, "blue", 1, "blue", false, "green"),
                        arguments(1, "green", 0, "", true, "green"),

                        // input type: danger
                        arguments(0, "", 0, "", false, "danger"),
                        arguments(5, "yellow", 5, "yellow", false, "danger"),
                        arguments(4, "red", 1, "green", true, "danger"),
                        arguments(3, "doubleYellow", 1, "green", true, "danger"),
                        arguments(3, "[SC]", 1, "green", true, "danger"),
                        arguments(2, "yellow", 1, "green", true, "danger"),
                        arguments(1, "blue", 1, "blue", false, "danger"),
                        arguments(1, "green", 1, "green", false, "danger"));
            }
        }
    }

    @Nested
    @DisplayName("Unit test for the FlagPost class")
    class FlagPostUnitTests {

        private FlagPost flagPost;

        @BeforeEach
        void setup() {
            this.flagPost = new FlagPost(0);
        }

        @Test
        @DisplayName("Initialize FlagPost object")
        void initializeFlagPost() {
            assertEquals(0, flagPost.getPostNumber(),
                    "Wrong post number. Expected " + 0 + " but got " + flagPost.getPostNumber());
            assertEquals(0, flagPost.getLevel(),
                    "Wrong initial level. Expected " + 0 + " but got " + flagPost.getLevel());
            assertEquals("", flagPost.getDepiction(),
                    "Wrong initial depiction. Expected " + "" + " but got " + flagPost.getDepiction());
        }

        @Nested
        @DisplayName("Unit tests for the up method in the FlagPost class")
        class upMethodFlagPost {
            @Test
            @DisplayName("Test invalid input for the up method - level: 0 and depiction:''")
            void invalidStringInputUpMethod() {
                boolean success = flagPost.up("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(0, flagPost.getLevel(),
                        "Wrong initial level. Expected " + 0 + " but got " + flagPost.getLevel());
                assertEquals("", flagPost.getDepiction(),
                        "Wrong initial depiction. Expected " + "" + " but got " + flagPost.getDepiction());
            }

            @Test
            @DisplayName("Test invalid input for the up method - level: 2 and depiction:'yellow'")
            void invalidStringInputUpMethodNotInitialState() {
                flagPost.setLevel(2);
                flagPost.setDepiction("yellow");

                boolean success = flagPost.up("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(2, flagPost.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + flagPost.getLevel());
                assertEquals("yellow", flagPost.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + flagPost.getDepiction());
            }

            @Test
            @DisplayName("Test valid input for the up method, however, backwards direction - same level")
            void testValidInputBackwardsSameLevel() {
                flagPost.setLevel(3);
                flagPost.setDepiction("doubleYellow/[SC]");

                boolean success = flagPost.up("doubleYellow");
                assertFalse(success, "Expected the return value of false");
                assertEquals(3, flagPost.getLevel(),
                        "Wrong level. Expected " + 3 + " but got " + flagPost.getLevel());
                assertEquals("doubleYellow/[SC]", flagPost.getDepiction(),
                        "Wrong depiction. Expected " + "doubleYellow/[SC]" + " but got " + flagPost.getDepiction());
            }

            @Test
            @DisplayName("Test valid input for the up method, however, backwards direction")
            void testValidInputBackwards() {
                flagPost.setLevel(2);
                flagPost.setDepiction("yellow");

                boolean success = flagPost.up("green");
                assertFalse(success, "Expected the return value of false");
                assertEquals(2, flagPost.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + flagPost.getLevel());
                assertEquals("yellow", flagPost.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + flagPost.getDepiction());
            }

            @ParameterizedTest(name = "First up type: {0} | Second up type: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4}")
            @MethodSource
            @DisplayName("Test up method with valid inputs in the correct direction.")
            void testUpMethodForGreenBlue(String firstType, String secondType, int expectedLevel,
                    String expectedDepiction, boolean returnValue) {
                flagPost.up(firstType);

                boolean success = flagPost.up(secondType);
                if (returnValue) {
                    assertTrue(success, "Expected the return value of " + returnValue);
                } else {
                    assertFalse(success, "Expected the return value of " + returnValue);
                }
                assertEquals(expectedLevel, flagPost.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + flagPost.getLevel());
                assertEquals(expectedDepiction, flagPost.getDepiction(),
                        "Wrong depiction. Expected " + expectedDepiction + " but got " + flagPost.getDepiction());
            }

            static Stream<Arguments> testUpMethodForGreenBlue() {
                return Stream.of(
                        arguments("blue", "blue", 1, "blue", false),
                        arguments("green", "green", 1, "green", false),
                        arguments("blue", "green", 1, "green/blue", true),
                        arguments("green", "blue", 1, "green/blue", true));
            }

            @Test
            @DisplayName("Check if the state would change from the level 5")
            void checkStateChangeForLastLevel() {
                flagPost.setLevel(5);
                flagPost.setDepiction("green/yellow/red/blue");

                boolean success = flagPost.up("green");
                assertFalse(success, "Expected the return value of false");
                assertEquals(5, flagPost.getLevel(),
                        "Wrong level. Expected " + 5 + " but got " + flagPost.getLevel());
                assertEquals("green/yellow/red/blue", flagPost.getDepiction(),
                        "Wrong depiction. Expected " + "green/yellow/red/blue" + " but got " + flagPost.getDepiction());
            }

            @ParameterizedTest(name = "Initial level: {0} | Initial depiction: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4} | Up method input: {5}")
            @MethodSource
            @DisplayName("Test up method with valid inputs in the correct direction.")
            void testUpMethodLightPanel(int level, String depiction, int expectedLevel, String expectedString,
                    boolean returnValue, String input) {

                // initial setup of the light panel before tests
                flagPost.setDepiction(depiction);
                flagPost.setLevel(level);

                boolean success = flagPost.up(input);
                if (returnValue) {
                    assertTrue(success, "Expected the return value of " + returnValue);
                } else {
                    assertFalse(success, "Expected the return value of " + returnValue);
                }
                assertEquals(expectedLevel, flagPost.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + flagPost.getLevel());
                assertEquals(expectedString, flagPost.getDepiction(),
                        "Wrong depiction. Expected " + expectedString + " but got " + flagPost.getDepiction());
            }

            static Stream<Arguments> testUpMethodLightPanel() {
                return Stream.of(
                        arguments(0, "", 1, "green", true, "green"),
                        arguments(0, "", 1, "blue", true, "blue"),
                        arguments(0, "", 3, "doubleYellow", true, "doubleYellow"),
                        arguments(3, "doubleYellow", 3, "doubleYellow/[SC]", true, "[SC]"),
                        arguments(1, "green", 1, "green", false, "green"),
                        arguments(1, "blue", 4, "red", true, "red"),
                        arguments(2, "yellow", 5, "green/yellow/red/blue", true, "end"),
                        arguments(2, "yellow", 4, "red", true, "red"));
            }
        }

        @Nested
        @DisplayName("Unit tests for the down method in the FlagPost class")
        class downMethodFlagPost {
            @Test
            @DisplayName("Test invalid input for the down method - level: 0 and depiction:''")
            void testInvalidInputInitialState() {
                boolean success = flagPost.down("Mars");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(0, flagPost.getLevel(),
                        "Wrong level. Expected " + 0 + " but got " + flagPost.getLevel());
                assertEquals("", flagPost.getDepiction(),
                        "Wrong depiction. Expected " + "" + " but got " + flagPost.getDepiction());
            }

            @Test
            @DisplayName("Test invalid input for the down method - level: 2 and depiction:'yellow'")
            void invalidStringInputNotInitialState() {
                flagPost.setLevel(2);
                flagPost.setDepiction("yellow");

                boolean success = flagPost.down("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(2, flagPost.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + flagPost.getLevel());
                assertEquals("yellow", flagPost.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + flagPost.getDepiction());
            }

            @ParameterizedTest(name = "Initial level: {0} | Initial depiction: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4} | Input type: {5}")
            @MethodSource
            @DisplayName("Test down method for blue input")
            void testDownMethod(int level, String depiction, int expectedLevel, String expectedString,
                    boolean returnValue, String type) {

                // initial setup of the light panel before tests
                flagPost.setDepiction(depiction);
                flagPost.setLevel(level);

                boolean success = flagPost.down(type);
                if (returnValue) {
                    assertTrue(success, "Expected the return value of " + returnValue);
                } else {
                    assertFalse(success, "Expected the return value of " + returnValue);
                }

                assertEquals(expectedLevel, flagPost.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + flagPost.getLevel());
                assertEquals(expectedString, flagPost.getDepiction(),
                        "Wrong depiction. Expected " + expectedString + " but got " + flagPost.getDepiction());
            }

            static Stream<Arguments> testDownMethod() {
                return Stream.of(
                        // input type: clear
                        arguments(0, "", 0, "", false, "clear"),
                        arguments(5, "green/yellow/red/blue", 0, "", true, "clear"),
                        arguments(4, "red", 0, "", true, "clear"),
                        arguments(3, "doubleYellow", 0, "", true, "clear"),
                        arguments(3, "doubleYellow/[SC]", 0, "", true, "clear"),
                        arguments(2, "yellow", 0, "", true, "clear"),
                        arguments(1, "green/blue", 0, "", true, "clear"),
                        arguments(1, "blue", 0, "", true, "clear"),
                        arguments(1, "green", 0, "", true, "clear"),

                        // input type: blue
                        arguments(0, "", 0, "", false, "blue"),
                        arguments(5, "green/yellow/red/blue", 5, "green/yellow/red/blue", false, "blue"),
                        arguments(4, "red", 4, "red", false, "blue"),
                        arguments(3, "doubleYellow", 3, "doubleYellow", false, "blue"),
                        arguments(3, "doubleYellow/[SC]", 3, "doubleYellow/[SC]", false, "blue"),
                        arguments(2, "yellow", 2, "yellow", false, "blue"),
                        arguments(1, "green/blue", 1, "green", true, "blue"),
                        arguments(1, "blue", 0, "", true, "blue"),
                        arguments(1, "green", 1, "green", false, "blue"),

                        // input type: green
                        arguments(0, "", 0, "", false, "green"),
                        arguments(5, "green/yellow/red/blue", 5, "green/yellow/red/blue", false, "green"),
                        arguments(4, "red", 4, "red", false, "green"),
                        arguments(3, "doubleYellow", 3, "doubleYellow", false, "green"),
                        arguments(3, "doubleYellow/[SC]", 3, "doubleYellow/[SC]", false, "green"),
                        arguments(2, "yellow", 2, "yellow", false, "green"),
                        arguments(1, "green/blue", 1, "blue", true, "green"),
                        arguments(1, "blue", 1, "blue", false, "green"),
                        arguments(1, "green", 0, "", true, "green"),

                        // input type: danger
                        arguments(0, "", 0, "", false, "danger"),
                        arguments(5, "green/yellow/red/blue", 5, "green/yellow/red/blue", false, "danger"),
                        arguments(4, "red", 1, "green", true, "danger"),
                        arguments(3, "doubleYellow", 1, "green", true, "danger"),
                        arguments(3, "doubleYellow/[SC]", 1, "green", true, "danger"),
                        arguments(2, "yellow", 1, "green", true, "danger"),
                        arguments(1, "green/blue", 1, "green/blue", false, "danger"),
                        arguments(1, "blue", 1, "blue", false, "danger"),
                        arguments(1, "green", 1, "green", false, "danger"));
            }
        }
    }

    @Nested
    @DisplayName("Unit test for the FinishPost class")
    class FinishPostUnitTests {
        private FinishPost finishPost;

        @BeforeEach
        void setup() {
            this.finishPost = new FinishPost(0);
        }

        @Test
        @DisplayName("Initialize FinishPost object")
        void initializeFlagPost() {
            assertEquals(0, finishPost.getPostNumber(),
                    "Wrong post number. Expected " + 0 + " but got " + finishPost.getPostNumber());
            assertEquals(0, finishPost.getLevel(),
                    "Wrong initial level. Expected " + 0 + " but got " + finishPost.getLevel());
            assertEquals("", finishPost.getDepiction(),
                    "Wrong initial depiction. Expected " + "" + " but got " + finishPost.getDepiction());
        }

        @Nested
        @DisplayName("Unit tests for the up method in the FinishPost class")
        class upMethodFlagPost {
            @Test
            @DisplayName("Test invalid input for the up method - level: 0 and depiction:''")
            void invalidStringInputUpMethod() {
                boolean success = finishPost.up("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(0, finishPost.getLevel(),
                        "Wrong initial level. Expected " + 0 + " but got " + finishPost.getLevel());
                assertEquals("", finishPost.getDepiction(),
                        "Wrong initial depiction. Expected " + "" + " but got " + finishPost.getDepiction());
            }

            @Test
            @DisplayName("Test invalid input for the up method - level: 2 and depiction:'yellow'")
            void invalidStringInputUpMethodNotInitialState() {
                finishPost.setLevel(2);
                finishPost.setDepiction("yellow");

                boolean success = finishPost.up("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(2, finishPost.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + finishPost.getLevel());
                assertEquals("yellow", finishPost.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + finishPost.getDepiction());
            }

            @Test
            @DisplayName("Test valid input for the up method, however, backwards direction - same level")
            void testValidInputBackwardsSameLevel() {
                finishPost.setLevel(3);
                finishPost.setDepiction("doubleYellow/[SC]");

                boolean success = finishPost.up("doubleYellow");
                assertFalse(success, "Expected the return value of false");
                assertEquals(3, finishPost.getLevel(),
                        "Wrong level. Expected " + 3 + " but got " + finishPost.getLevel());
                assertEquals("doubleYellow/[SC]", finishPost.getDepiction(),
                        "Wrong depiction. Expected " + "doubleYellow/[SC]" + " but got " + finishPost.getDepiction());
            }

            @Test
            @DisplayName("Test valid input for the up method, however, backwards direction")
            void testValidInputBackwards() {
                finishPost.setLevel(2);
                finishPost.setDepiction("yellow");

                boolean success = finishPost.up("green");
                assertFalse(success, "Expected the return value of false");
                assertEquals(2, finishPost.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + finishPost.getLevel());
                assertEquals("yellow", finishPost.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + finishPost.getDepiction());
            }

            @ParameterizedTest(name = "First up type: {0} | Second up type: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4}")
            @MethodSource
            @DisplayName("Test up method with valid inputs in the correct direction.")
            void testUpMethodForGreenBlue(String firstType, String secondType, int expectedLevel,
                    String expectedDepiction, boolean returnValue) {
                finishPost.up(firstType);

                boolean success = finishPost.up(secondType);
                if (returnValue) {
                    assertTrue(success, "Expected the return value of " + returnValue);
                } else {
                    assertFalse(success, "Expected the return value of " + returnValue);
                }
                assertEquals(expectedLevel, finishPost.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + finishPost.getLevel());
                assertEquals(expectedDepiction, finishPost.getDepiction(),
                        "Wrong depiction. Expected " + expectedDepiction + " but got " + finishPost.getDepiction());
            }

            static Stream<Arguments> testUpMethodForGreenBlue() {
                return Stream.of(
                        arguments("blue", "blue", 1, "blue", false),
                        arguments("green", "green", 1, "green", false),
                        arguments("blue", "green", 1, "green/blue", true),
                        arguments("green", "blue", 1, "green/blue", true));
            }

            @Test
            @DisplayName("Check if the state would change from the level 5")
            void checkStateChangeForLastLevel() {
                finishPost.setLevel(5);
                finishPost.setDepiction("chequered");

                boolean success = finishPost.up("green");
                assertFalse(success, "Expected the return value of false");
                assertEquals(5, finishPost.getLevel(),
                        "Wrong level. Expected " + 5 + " but got " + finishPost.getLevel());
                assertEquals("chequered", finishPost.getDepiction(),
                        "Wrong depiction. Expected " + "chequered" + " but got " + finishPost.getDepiction());
            }

            @ParameterizedTest(name = "Initial level: {0} | Initial depiction: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4} | Up method input: {5}")
            @MethodSource
            @DisplayName("Test up method with valid inputs in the correct direction.")
            void testUpMethodLightPanel(int level, String depiction, int expectedLevel, String expectedString,
                    boolean returnValue, String input) {

                // initial setup of the light panel before tests
                finishPost.setDepiction(depiction);
                finishPost.setLevel(level);

                boolean success = finishPost.up(input);
                if (returnValue) {
                    assertTrue(success, "Expected the return value of " + returnValue);
                } else {
                    assertFalse(success, "Expected the return value of " + returnValue);
                }
                assertEquals(expectedLevel, finishPost.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + finishPost.getLevel());
                assertEquals(expectedString, finishPost.getDepiction(),
                        "Wrong depiction. Expected " + expectedString + " but got " + finishPost.getDepiction());
            }

            static Stream<Arguments> testUpMethodLightPanel() {
                return Stream.of(
                        arguments(0, "", 1, "green", true, "green"),
                        arguments(0, "", 1, "blue", true, "blue"),
                        arguments(0, "", 3, "doubleYellow", true, "doubleYellow"),
                        arguments(3, "doubleYellow", 3, "doubleYellow/[SC]", true, "[SC]"),
                        arguments(1, "green", 1, "green", false, "green"),
                        arguments(1, "blue", 4, "red", true, "red"),
                        arguments(2, "yellow", 5, "chequered", true, "end"),
                        arguments(2, "yellow", 4, "red", true, "red"));
            }
        }

        @Nested
        @DisplayName("Unit tests for the down method in the FinishPost class")
        class downMethodFinishPost {
            @Test
            @DisplayName("Test invalid input for the down method - level: 0 and depiction:''")
            void testInvalidInputInitialState() {
                boolean success = finishPost.down("Mars");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(0, finishPost.getLevel(),
                        "Wrong level. Expected " + 0 + " but got " + finishPost.getLevel());
                assertEquals("", finishPost.getDepiction(),
                        "Wrong depiction. Expected " + "" + " but got " + finishPost.getDepiction());
            }

            @Test
            @DisplayName("Test invalid input for the down method - level: 2 and depiction:'yellow'")
            void invalidStringInputNotInitialState() {
                finishPost.setLevel(2);
                finishPost.setDepiction("yellow");

                boolean success = finishPost.down("Venus");
                assertFalse(success, "Expected the return value of false for the invalid string input");
                assertEquals(2, finishPost.getLevel(),
                        "Wrong level. Expected " + 2 + " but got " + finishPost.getLevel());
                assertEquals("yellow", finishPost.getDepiction(),
                        "Wrong depiction. Expected " + "yellow" + " but got " + finishPost.getDepiction());
            }

            @ParameterizedTest(name = "Initial level: {0} | Initial depiction: {1} | Expected level: {2} | Expected depiction: {3} | Expected return value: {4} | Input type: {5}")
            @MethodSource
            @DisplayName("Test down method for blue input")
            void testDownMethod(int level, String depiction, int expectedLevel, String expectedString,
                    boolean returnValue, String type) {

                // initial setup of the light panel before tests
                finishPost.setDepiction(depiction);
                finishPost.setLevel(level);

                boolean success = finishPost.down(type);
                if (returnValue) {
                    assertTrue(success, "Expected the return value of " + returnValue);
                } else {
                    assertFalse(success, "Expected the return value of " + returnValue);
                }

                assertEquals(expectedLevel, finishPost.getLevel(),
                        "Wrong level. Expected " + expectedLevel + " but got " + finishPost.getLevel());
                assertEquals(expectedString, finishPost.getDepiction(),
                        "Wrong depiction. Expected " + expectedString + " but got " + finishPost.getDepiction());
            }

            static Stream<Arguments> testDownMethod() {
                return Stream.of(
                        // input type: clear
                        arguments(0, "", 0, "", false, "clear"),
                        arguments(5, "green/yellow/red/blue", 0, "", true, "clear"),
                        arguments(4, "red", 0, "", true, "clear"),
                        arguments(3, "doubleYellow", 0, "", true, "clear"),
                        arguments(3, "doubleYellow/[SC]", 0, "", true, "clear"),
                        arguments(2, "yellow", 0, "", true, "clear"),
                        arguments(1, "green/blue", 0, "", true, "clear"),
                        arguments(1, "blue", 0, "", true, "clear"),
                        arguments(1, "green", 0, "", true, "clear"),

                        // input type: blue
                        arguments(0, "", 0, "", false, "blue"),
                        arguments(5, "green/yellow/red/blue", 5, "green/yellow/red/blue", false, "blue"),
                        arguments(4, "red", 4, "red", false, "blue"),
                        arguments(3, "doubleYellow", 3, "doubleYellow", false, "blue"),
                        arguments(3, "doubleYellow/[SC]", 3, "doubleYellow/[SC]", false, "blue"),
                        arguments(2, "yellow", 2, "yellow", false, "blue"),
                        arguments(1, "green/blue", 1, "green", true, "blue"),
                        arguments(1, "blue", 0, "", true, "blue"),
                        arguments(1, "green", 1, "green", false, "blue"),

                        // input type: green
                        arguments(0, "", 0, "", false, "green"),
                        arguments(5, "green/yellow/red/blue", 5, "green/yellow/red/blue", false, "green"),
                        arguments(4, "red", 4, "red", false, "green"),
                        arguments(3, "doubleYellow", 3, "doubleYellow", false, "green"),
                        arguments(3, "doubleYellow/[SC]", 3, "doubleYellow/[SC]", false, "green"),
                        arguments(2, "yellow", 2, "yellow", false, "green"),
                        arguments(1, "green/blue", 1, "blue", true, "green"),
                        arguments(1, "blue", 1, "blue", false, "green"),
                        arguments(1, "green", 0, "", true, "green"),

                        // input type: danger
                        arguments(0, "", 0, "", false, "danger"),
                        arguments(5, "green/yellow/red/blue", 5, "green/yellow/red/blue", false, "danger"),
                        arguments(4, "red", 1, "green", true, "danger"),
                        arguments(3, "doubleYellow", 1, "green", true, "danger"),
                        arguments(3, "doubleYellow/[SC]", 1, "green", true, "danger"),
                        arguments(2, "yellow", 1, "green", true, "danger"),
                        arguments(1, "green/blue", 1, "green/blue", false, "danger"),
                        arguments(1, "blue", 1, "blue", false, "danger"),
                        arguments(1, "green", 1, "green", false, "danger"));
            }
        }
    }
}
