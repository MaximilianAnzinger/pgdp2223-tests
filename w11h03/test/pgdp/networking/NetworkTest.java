package pgdp.networking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetworkTest {

    private static boolean executeTests = false;
    private static DataHandler dataHandler = new DataHandler();

    private static String kennung;
    private static String username;
    private static String password;
    private static int id;

    //
    // Register the first possible not reserved kennung.
    //

    @BeforeAll
    public static void init() throws Exception {
        var config = Files
                .lines(Path.of("test/config.env"))
                .filter(i -> i.trim().length() != 0)
                .filter(i -> !i.startsWith("#"))
                .collect(Collectors.toMap(i -> i.split("=")[0].trim(), i -> i.split("=")[1].trim()));

        kennung = config.get("kennung");
        username = config.get("username");
        password = config.get("password");

        if (config.get("kennung").equals("null")) {
            System.err.println("""
                    [STEP 1] To setup the Tests, please provide your
                    TUM kennung in the `config.env` file and run the
                    tests again. This execution all tests will fail.
                    """);
            return;
        }

        if (config.get("username").equals("null")) {
            System.err.println("""
                    [STEP 2] Please provide a username to your
                    config file.
                    """);
            return;
        }

        if (config.get("password").equals("null")) {
            assertTrue(dataHandler.register(username, kennung));

            System.err.println("""
                    [STEP 3] An email was sent to your mailbox
                    associated with the TUM kennung. Please open
                    it and provide the config file with the pass-
                    word you have received.
                    """);
            return;
        }

        Lib.getField(dataHandler, "username").set(dataHandler, username);
        Lib.getField(dataHandler, "password").set(dataHandler, password);

        executeTests = true;
        System.out.println("RUNNING TESTS...");
    }

    @BeforeEach
    public void configError() {
        if (!executeTests)
            fail("Please follow the instructions to set up your test files.");
    }

    //
    // BEHAVIOUR TEST
    //

    //
    // FLAGS
    //
    // [A] - Test that should <A>lawys pass and not be affected by code.
    // [P] - Test in <P>ublic environment.
    //

    //
    // <region structural tests>
    //

    @Test
    @Order(1)
    @DisplayName("should request token")
    public void requestTokenTest() {
        var token = dataHandler.requestToken();
        assertNotNull(token);
        System.out.println("TOKEN: " + token);
    }

    @Test
    @Order(2)
    @DisplayName("should set visibility to private")
    public void setVisibilityToPrivate() throws Exception {
        assertTrue(Lib.makePublic(dataHandler, false));
    }

    @Test
    @Order(3)
    @DisplayName("should be able to login with credentials")
    public void loginTest() throws Exception {
        assertTrue(dataHandler.login(username, password));

        id = (int) Lib.getField(dataHandler, "id").get(dataHandler);
        System.out.println("ID: " + id);

        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    @Order(4)
    @DisplayName("should get contacts")
    public void getContactsTest() throws Exception {
        var users = dataHandler.getContacts();

        assertTrue(users.containsKey(id));
        assertEquals(username, users.get(id).name());
    }

    @Test
    @Order(5)
    @DisplayName("should get messages in general")
    public void getMessagesWithUserTest() throws Exception {
        assertNotNull(dataHandler.getMessagesWithUser(1, 0, 0));
    }

    //
    // </end-region>
    // <region behaviour tests>
    //

    @Test
    @Order(6)
    @DisplayName("[P] [A] should set visibility to public")
    public void setVisibilityToPublic() throws Exception {
        assertTrue(Lib.makePublic(dataHandler, true));
    }

    @Test
    @Order(7)
    @DisplayName("[P] should contain youself in contacts")
    public void getContactsPublicTest() throws Exception {
        var users = dataHandler.getContacts();

        assertNotNull(users);
        assertTrue(users.containsKey(id));
        assertEquals("The One and Only", users.get(id).name());
    }

    @Test
    @Order(8)
    @DisplayName("[P] should get messages from general channel")
    public void getMessagesGeneralChatPublicTest() throws Exception {
        var messages = dataHandler.getMessagesWithUser(1, 10, 0);

        assertNotNull(messages);
        assertEquals(10, messages.size());

        //
        // Konrad's message
        //

        assertEquals(LocalDateTime.parse("2023-01-19T02:08:41.226077"), messages.get(0).date());

        assertEquals("Konrad: Hallo zusammen :)\u0000", messages.get(0).content());

        // https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/self.20Parameter.20der.20Message/near/906780
        // The average user is not Konrad.
        assertFalse(messages.get(0).self());

        assertEquals(2591, messages.get(0).id());

        //
        // Possible Edge Case: Last Message
        //

        assertEquals(LocalDateTime.parse("2023-01-19T02:19:04.687715"), messages.get(9).date());
        assertEquals("bulbasaur: Guna", messages.get(9).content());
        // If you are bulbasaur this test will fail
        assertFalse(messages.get(9).self());
        assertEquals(2603, messages.get(9).id());
    }

    //
    // </end-region>
    // <region socket tests>
    //

    @Test
    @Order(20)
    @DisplayName("should connect")
    public void connectTest() throws Exception {
        //
        // TODO check if `Input Handler started` was printed into console?
        //
        Lib.getMethod(dataHandler, "connect").invoke(dataHandler);

        assertTrue(dataHandler.connected);
        assertNotNull(Lib.getField(dataHandler, "socket").get(dataHandler));
        assertNotNull(Lib.getField(dataHandler, "in").get(dataHandler));
        assertNotNull(Lib.getField(dataHandler, "out").get(dataHandler));
    }

    @Test
    @Order(21)
    @DisplayName("[A] should switch partner")
    public void partnerSwitchTest() throws Exception {
        // TODO If not crash -> Test went good?
        dataHandler.switchConnection(id);
    }

    @Test
    @Order(22)
    @DisplayName("[A] should send message")
    public void sendMessageTest() throws Exception {
        // TODO If not crash -> Test went good?
        dataHandler.sendMessage("Hello, Pingu!");
    }

    @Test
    @Order(23)
    @DisplayName("should check `sendMessageTest` by actually checking if messages were updated")
    public void updatedMessagesTest() throws Exception {
        // TODO
        var _todo = dataHandler.getMessagesWithUser(id, 10, 0);
        System.out.println(_todo);
    }

    @Test
    @Order(24)
    @DisplayName("[P] [A] should switch partner to general chat")
    public void partnerSwitchPublicTest() throws Exception {
        // TODO
        dataHandler.switchConnection(1);
    }

    @Test
    @Order(25)
    @DisplayName("[P] [A] should send message to switched-to user")
    public void sendMessagePublicTest() throws Exception {
        // TODO
        dataHandler.sendMessage("Hello, Pingu!");
    }

    //
    // </end-region>
    //
}
