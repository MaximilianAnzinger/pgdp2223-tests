package pgdp.pingunetwork.structural;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pingunetwork.Group;
import pgdp.pingunetwork.Picture;
import pgdp.pingunetwork.User;

public class GroupStructuralTest extends StructuralTest<Group> {

    public GroupStructuralTest() {
        super(Group.class);
    }

    @Test
    @DisplayName("Check fields")
    void checkFields() {
        expectAttribute("name", String.class, PRIVATE);
        expectAttribute("description", String.class, PRIVATE);
        expectAttribute("owner", User.class, PRIVATE);
        expectAttribute("members", User[].class, PRIVATE);
        expectAttribute("picture", Picture.class, PRIVATE);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(PUBLIC, String.class, String.class, User.class, Picture.class);
    }

    @Test
    @DisplayName("Check getters")
    void checkGetters() {
        expectMethod(PUBLIC, "getName");
        expectMethod(PUBLIC, "getDescription");
        expectMethod(PUBLIC, "getOwner");
        expectMethod(PUBLIC, "getMembers");
        expectMethod(PUBLIC, "getPicture");
    }

    @Test
    @DisplayName("Check setters")
    void checkSetters() {
        expectMethod(PUBLIC, "setName", String.class);
        expectMethod(PUBLIC, "setDescription", String.class);
        expectMethod(PUBLIC, "setOwner", User.class);
        expectMethod(PUBLIC, "setPicture", Picture.class);
        dontExpectMethod(PUBLIC, "setMembers", User[].class);
    }

}
