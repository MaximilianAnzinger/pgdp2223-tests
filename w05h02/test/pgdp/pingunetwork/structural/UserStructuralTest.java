package pgdp.pingunetwork.structural;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.pingunetwork.Group;
import pgdp.pingunetwork.Picture;
import pgdp.pingunetwork.Post;
import pgdp.pingunetwork.User;

public class UserStructuralTest extends StructuralTest<User> {

    public UserStructuralTest() {
        super(User.class);
    }

    @Test
    @DisplayName("Check fields")
    void checkFields() {
        expectAttribute("name", 0, String.class, PRIVATE);
        expectAttribute("description", 1, String.class, PRIVATE);
        expectAttribute("profilePicture", Picture.class, PRIVATE);
        expectAttribute("groups", Group[].class, PRIVATE);
        expectAttribute("friends", User[].class, PRIVATE);
        expectAttribute("posts", Post[].class, PRIVATE);
    }

    @Test
    @DisplayName("Check constructor")
    void checkConstructor() {
        onlyExpectConstructor(PUBLIC, String.class, String.class, Picture.class);
    }

    @Test
    @DisplayName("Check getters")
    void checkGetters() {
        expectMethod(PUBLIC, "getName");
        expectMethod(PUBLIC, "getDescription");
        expectMethod(PUBLIC, "getProfilePicture");
        expectMethod(PUBLIC, "getGroups");
        expectMethod(PUBLIC, "getFriends");
    }

    @Test
    @DisplayName("Check setters")
    void checkSetters() {
        expectMethod(PUBLIC, "setName", String.class);
        expectMethod(PUBLIC, "setDescription", String.class);
        expectMethod(PUBLIC, "setProfilePicture", Picture.class);
        dontExpectMethod(PUBLIC, "setGroups", Group[].class);
        dontExpectMethod(PUBLIC, "setFriends", User[].class);
    }

}
