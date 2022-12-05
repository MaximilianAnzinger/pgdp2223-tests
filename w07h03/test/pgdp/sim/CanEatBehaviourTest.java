package pgdp.sim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CanEatBehaviourTest {
    static Hamster ham = new Hamster();
    static Plant plant = new Plant();
    static Pingu pingu = new Pingu();
    static Wolf wolf = new Wolf();

    @Test
    void expectEat() {
        Assertions.assertTrue(ham.canEat(plant), "Expected Hamster to be able to eat a Plant");
        Assertions.assertTrue(pingu.canEat(plant), "Expected Pingu to be able to eat a Plant");
        Assertions.assertTrue(wolf.canEat(ham), "Expected Wolf to be able to eat a Hamster");
    }

    @Test
    void expectCannotEat() {
        Assertions.assertFalse(ham.canEat(null));
        Assertions.assertFalse(ham.canEat(wolf));
        Assertions.assertFalse(ham.canEat(pingu));
        Assertions.assertFalse(ham.canEat(ham));

        Assertions.assertFalse(pingu.canEat(null));
        Assertions.assertFalse(pingu.canEat(wolf));
        Assertions.assertFalse(pingu.canEat(pingu));
        Assertions.assertFalse(pingu.canEat(ham));

        Assertions.assertFalse(wolf.canEat(null));
        Assertions.assertFalse(wolf.canEat(wolf));
        Assertions.assertFalse(wolf.canEat(pingu));
        Assertions.assertFalse(wolf.canEat(plant));
    }
}