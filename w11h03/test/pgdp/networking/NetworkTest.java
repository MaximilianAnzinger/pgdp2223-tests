package pgdp.networking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import pgdp.networking.ViewController.User;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetworkTest {

    private static boolean executeTests = false;
    private static DataHandler dataHandler = new DataHandler();

    private static String kennung;
    private static String username;
    private static String password;
    private static String token;
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

    @Test
    @Order(1)
    public void requestTokenTest() {
        token = dataHandler.requestToken();
        assertNotNull(token);
    }

    @Test
    @Order(2)
    public void loginTest() throws Exception {
        assertTrue(dataHandler.login(username, password));

        id = (int) Lib.getField(dataHandler, "id").get(dataHandler);

        assertNotNull(id);
        assertTrue(id > 0);
    }

    @Test
    @Order(3)
    public void getContactsTest() throws Exception {
        var users = dataHandler.getContacts();

        assertTrue(users.containsKey(id));
        assertEquals(username, users.get(id).name());
    }

    @Test
    @Order(4)
    public void getMessagesWithUserTest() throws Exception {
        assertNotNull(dataHandler.getMessagesWithUser(0, 0, 0));
    }

    @Test
    @Order(5)
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
    @Order(6)
    @DisplayName("This test will always pass, don't get hope")
    public void partnerSwitchTest() throws Exception {
        // If not crash -> Test went good?
        dataHandler.switchConnection(id);
    }

    @Test
    @Order(7)
    @DisplayName("This test will always pass, don't get hope")
    public void sendMessageTest() throws Exception {
        // If not crash -> Test went good?
        dataHandler.sendMessage("Hello, Pingu!");
    }

    @Test
    @Order(8)
    @DisplayName("Checks `sendMessageTest` by actually checking if messages were updated")
    public void updatedMessagesTest() throws Exception {
        var _todo = dataHandler.getMessagesWithUser(0, 0, 0);
        System.out.println(_todo);
    }
}
