package pgdp.networking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pgdp.networking.ViewController.Message;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetworkTest {

    private static boolean executeTests = false;
    private static DataHandler dataHandler = new DataHandler();

    private static String kennung;
    private static String username;
    private static String password;
    private static int id;
    
    private static String msg = "Hello, Pingu! (" + Lib.generateString(new Random()) + ")";

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
    @DisplayName("should set visibility to public")
    public void setVisibilityToPublic() throws Exception {
        assertTrue(Lib.makePublic(dataHandler, true));
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

        assertEquals(username, (String) Lib.getField(dataHandler, "username").get(dataHandler));
        assertEquals(password, (String) Lib.getField(dataHandler, "password").get(dataHandler));
    }

    //
    // </end-region>
    // <region behaviour tests>
    //

    @Test
    @Order(6)
    @DisplayName("[P] should contain youself in contacts")
    public void getContactsTest() throws Exception {
        var users = dataHandler.getContacts();

        assertNotNull(users);

        // Last time I checked there were 228

        assertTrue(users.size() > 100);

        // Should contain self

        assertTrue(users.containsKey(id));
        assertEquals(username, users.get(id).name());

        // Should contain Faid

        assertTrue(users.containsKey(2032346041));
        assertEquals("faid", users.get(2032346041).name());

        // Should contain Nils

        assertTrue(users.containsKey(1259660950));
        assertEquals("nilsreichardt", users.get(1259660950).name());
    }

    @Test
    @Order(7)
    @DisplayName("[P] should get messages from general channel")
    public void getMessagesGeneralChatTest() throws Exception {
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
    @Order(8)
    @DisplayName("should connect")
    public void connectTest() throws Exception {
        Lib.getMethod(dataHandler, "connect").invoke(dataHandler);

        assertTrue(dataHandler.connected);
        var socket = (Socket) Lib.getField(dataHandler, "socket").get(dataHandler);
        assertNotNull(socket);
        assertNotNull(Lib.getField(dataHandler, "in").get(dataHandler));
        assertNotNull(Lib.getField(dataHandler, "out").get(dataHandler));

        //
        // TODO System.err catch -> if this method throws, there was a problem with connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(9)
    @DisplayName("[A] should switch partner to general channel")
    public void partnerSwitchTest() throws Exception {
        dataHandler.switchConnection(1);

        //
        // TODO System.err catch -> if this method throws, there was a problem with connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(10)
    @DisplayName("[A] should switch partner to faid")
    public void partnerSwitchToFaidTest() throws Exception {
        dataHandler.switchConnection(2032346041);

        //
        // TODO System.err catch -> if this method throws, there was a problem with connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(11)
    @DisplayName("[A] should send message")
    public void sendMessageTest() throws Exception {
        dataHandler.sendMessage(msg);

        //
        // TODO System.err catch -> if this method throws, there was a problem with connect.
        //
        var bytes = (byte[]) Lib.getIntMethod(dataHandler, "getResponse").invoke(dataHandler, 0);
    }

    @Test
    @Order(12)
    @DisplayName("should check `sendMessageTest` by actually checking if messages were updated")
    public void updatedMessagesTest() throws Exception {
        // Ensure we see the messages from last page.
        List<Message> messages;
        do {
            messages = dataHandler.getMessagesWithUser(2032346041, 50, 0);
        } while (messages.size() == 10);

        // Get last message from last page.
        var lastMessage = messages.get(messages.size() - 1);

        // Check if the message equals to the one sent in `sendMessageTest()`
        assertEquals(msg, lastMessage.content().substring(0, msg.length()));
    }

    //
    // </end-region>
    // <region interaction test>
    //

    // TODO

    //
    // </end-region>
    //
}
