package pgdp.pingunetwork.structural;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pingunetwork.Interaction;
import pgdp.pingunetwork.User;

public class InteractionStructuralTest extends StructuralTest<Interaction> {

    public InteractionStructuralTest() {
        super(Interaction.class);
    }

    @Test
    @DisplayName("Check fields")
    void checkFields() {
        expectAttribute("user", 0, User.class, PRIVATE);
        expectAttribute("interactionType", 1, int.class, PRIVATE);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(PUBLIC, User.class, int.class);
    }

    @Test
    @DisplayName("Check getters")
    void checkGetters() {
        expectMethod(PUBLIC, "getUser");
        expectMethod(PUBLIC, "getInteractionType");
    }

    @Test
    @DisplayName("Check setters")
    void checkSetters() {
        dontExpectMethod(PUBLIC, "setUser", User.class);
        dontExpectMethod(PUBLIC, "setInteractionType", int.class);
    }

}
