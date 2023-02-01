package pgdp.networking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pgdp.networking.ViewController.Message;
import pgdp.networking.ViewController.User;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetworkTest {

    private static boolean executeTests = false;
    private static DataHandler dataHandler = new DataHandler();
    private static Map<Integer, User> users;

    private static enum State {
        Init,
        LoggedIn, IsPublic
    }

    private static State state = State.Init;
    private static String ensureOrder = "This test failed because it was run separately from others. These tests need a strict execution order, please run all tests together.";

    private static String kennung;
    private static String username;
    private static String password;
    private static int id;

    //
    // I gave you my id.
    // Behave.
    //

    private static int recipient = 361563966;
    private static String msg = "Hello, Pingu! (" + Lib.generateString(new Random()) + ")";

    //
    // Register the first possible not reserved kennung.
    //

    @BeforeAll
    public static void init() throws IOException {
        Map<String, String> config;
        try (Stream<String> lines = Files.lines(Path.of("test/config.env"))) {
            config = lines.filter(i -> i.trim().length() != 0)
                    .filter(i -> !i.startsWith("#"))
                    .collect(Collectors.toMap(i -> i.split("=")[0].trim(), i -> i.split("=")[1].trim()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
    // [T] - <T>emplate can be broken, ensure this.
    //

    //
    // <region structural tests>
    //

    @Test
    @Order(1)
    @DisplayName("[T] should be able to login with credentials")
    public void loginTest() throws Exception {
        assertTrue(dataHandler.login(username, password));

        id = (int) Lib.getField(dataHandler, "id").get(dataHandler);
        System.out.println("ID: " + id);

        assertNotNull(id);
        assertTrue(id > 0);

        assertEquals(username, (String) Lib.getField(dataHandler, "username").get(dataHandler));
        assertEquals(password, (String) Lib.getField(dataHandler, "password").get(dataHandler));

        state = State.LoggedIn;
    }

    @Test
    @Order(2)
    @DisplayName("should request token")
    public void requestTokenTest() {
        assertEquals(State.LoggedIn, state, ensureOrder);

        var token = dataHandler.requestToken();
        assertNotNull(token);
        System.out.println("TOKEN: " + token);
    }

    @Test
    @Order(3)
    @DisplayName("should set visibility to public")
    public void setVisibilityToPublic() throws Exception {
        assertEquals(State.LoggedIn, state, ensureOrder);

        assertTrue(Lib.makePublic(dataHandler, true));

        state = State.IsPublic;
    }

    //
    // </end-region>
    // <region behaviour tests>
    //

    @Test
    @Order(6)
    @DisplayName("[P] should access in contacts")
    public void getContactsTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        users = dataHandler.getContacts();

        // Last time I checked there were 228 users, > 100
        assertNotNull(users);
        assertTrue(users.size() > 100);
    }

    @ParameterizedTest
    @Order(7)
    @MethodSource
    @DisplayName("[P] should contain youself in contacts")
    public void scanContacts(int uid, String name) {
        assertEquals(State.IsPublic, state, ensureOrder);

        assertTrue(users.containsKey(uid));
        assertEquals(name, users.get(uid).name());
    }

    public static Stream<Arguments> scanContacts() {
        return Stream.of(
                arguments(id, users.get(id).name()),
                arguments(2032346041, "faid"),
                arguments(1259660950, "nilsreichardt"),
                arguments(361563966, "The One and Only"));
    }

    @Test
    @Order(8)
    @DisplayName("[P] [T] should get messages from general channel")
    @Disabled
    public void getMessagesGeneralChatTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        // TODO

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
    @Order(9)
    @DisplayName("should connect")
    public void connectTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        Lib.getMethod(dataHandler, "connect").invoke(dataHandler);

        assertTrue(dataHandler.connected);
        var socket = (Socket) Lib.getField(dataHandler, "socket").get(dataHandler);
        assertNotNull(socket);
        assertNotNull(Lib.getField(dataHandler, "in").get(dataHandler));
        assertNotNull(Lib.getField(dataHandler, "out").get(dataHandler));

        //
        // TODO System.err catch -> if this method throws, there was a problem with
        // connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(10)
    @DisplayName("[A] should switch partner to faid")
    public void partnerSwitchToFaidTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        dataHandler.switchConnection(recipient);

        //
        // TODO System.err catch -> if this method throws, there was a problem with
        // connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(11)
    @DisplayName("[A] should send message")
    public void sendMessageTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        dataHandler.sendMessage(msg);

        //
        // TODO System.err catch -> if this method throws, there was a problem with
        // connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(12)
    @DisplayName("should check `sendMessageTest` by actually checking if messages were updated")
    public void updatedMessagesTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        // Ensure we see the messages from last page.
        List<Message> messages = dataHandler.getMessagesWithUser(recipient, 5000, 0);

        // Check if the message equals to the one sent in `sendMessageTest()`
        assertTrue(messages.stream().anyMatch(i -> i.content().contains(msg)));
    }

    //
    // </end-region>
    // <region interaction test>
    //

    @Test
    @Order(13)
    @DisplayName("[A] should switch partner to general channel")
    public void partnerSwitchTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        dataHandler.switchConnection(1);

        //
        // TODO System.err catch -> if this method throws, there was a problem with
        // connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(14)
    @DisplayName("[A] should send message")
    public void spamGeneralTest() throws Exception {
        assertEquals(State.IsPublic, state, ensureOrder);

        dataHandler.sendMessage("Hallo, ich bin ein Network Bot von " + username);

        //
        // TODO System.err catch -> if this method throws, there was a problem with
        // connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    //
    // </end-region>
    //
}
