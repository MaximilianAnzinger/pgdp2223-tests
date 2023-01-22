package pgdp.networking;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static pgdp.networking.ReflectUtils.*;

/**
 * These tests do not actually send data anywhere, so no need to worry.
 * IMPORTANT:   When connecting to the socket, please MAKE SURE that you use DataHandler.serverAddress
 *              as a variable, and not the string literal "carol.sse.cit.tum.de".
 * If something is wrong with my tests, feel free to chat me up on Zulip :)
 * @author Bjarne Hansen
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SocketTest {
    // Timeout for sockets in ms
    private static final int SOCKET_TIMEOUT = 1000;
    private final DataHandler dataHandler = new DataHandler();
    private Method connect;
    private static ServerSocket server;
    private HTTPClientMock httpClient;
    private Throwable lastThrowable = null;

    // Messages
    private static final byte[] SERVER_HELLO = new byte[]{0x00, 0x00, 0x2a};
    private static final byte[] SERVER_ACK = new byte[]{0x00, 0x05};

    @BeforeAll
    static void setupAll() {
        try {
            server = new ServerSocket(1337);
        } catch (IOException e) {
            throw new RuntimeException(
                    "Something went wrong while creating the local SocketServer. Got anything running on port 1337?\n"
                            + "Error:\n" + e
            );
        }
    }

    @BeforeEach
    void setup() {
        this.httpClient = new HTTPClientMock();
        httpClient.respond(200, """
                {
                  "access_token": "exampleToken",
                  "token_type": "irrelevant"
                }""");
        setField(dataHandler, "client", httpClient);
        this.connect = getMethod(dataHandler, "connect");
        // Set socket to localhost
        setField(dataHandler, "serverAddress", "127.0.0.1");
        setField(dataHandler, "username", "Alfred");
        setField(dataHandler, "password", "Wagner");
        setField(dataHandler, "id", 84342);
    }

    @BeforeEach
    void readmeMessage() {
        System.out.println("""
                If a test in SocketTest doesn't finish, it's likely because you are using the address of the server (carol.sse.cit.tum.de) directly.
                Instead, please use the variable DataHandler.serverAddress when creating your client socket.
                Make sure to use port 1337.
                """);
        System.out.println("""
                If a test prints an exception but neither succeeds nor fails, some method called System.exit().
                This is likely because you threw an Exception which was not DataHandler.ConnectionException.
                """);
        System.out.println("""
                If you are using a slower device and your SocketTests keep failing for no reason, try increasing the SOCKET_TIMEOUT.
                """);
        System.out.println("""
                Occasionally, the handleInput thread might crash with an EOFException and terminate the tests.
                I believe this is caused by a race condition between the termination of the client socket in handleInput() and the server socket.
                There is no good solution to this as far as I'm aware.
                If this happens, just rerun the program.
                """);
    }

    @Test
    @Order(1)
    @DisplayName("connect() – successful")
    void testConnectSuccess() throws IOException {
        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(SERVER_HELLO);
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);

        // Assert no Exception
        assertNull(lastThrowable, "connect() threw an exception when it wasn't supposed to.");

        // Assert ClientHello
        final byte[] expectedHello = new byte[] {0x00, 0x01};
        byte[] actualHello = copyOf(buffer, 0, 2, "ClientHello");
        assertArrayEquals(expectedHello, actualHello, "Did not receive ClientHello as expected.");

        // Assert ClientIdentification
        byte[] clientIdentification = new byte[] {0x00, 0x02};
        byte[] actualIdentification = copyOf(buffer, 2, 4, "ClientIdentification");
        assertArrayEquals(clientIdentification, actualIdentification, "Did not receive ClientIdentification as expected.");
        byte idSize = copyOf(buffer, 4, 5, "ClientIdentification k")[0];
        byte[] idBuffer = copyOf(buffer, 2, 5 + idSize, "ClientIdentification id");
        OrBuilder
                .assertThat(() -> assertArrayEquals(new byte[]{0x00, 0x02, 0x03, 0x01, 0x49, 0x76}, idBuffer))
                .or(() -> assertArrayEquals(new byte[]{0x00, 0x02, 0x04, 0x00, 0x01, 0x49, 0x76}, idBuffer))
                .or(() -> assertArrayEquals(new byte[]{0x00, 0x02, 0x05, 0x00, 0x00, 0x01, 0x49, 0x76}, idBuffer))
                .or(() -> assertArrayEquals(new byte[]{0x00, 0x02, 0x06, 0x00, 0x00, 0x00, 0x01, 0x49, 0x76}, idBuffer))
                .or(() -> assertArrayEquals(new byte[]{0x00, 0x02, 0x07, 0x00, 0x00, 0x00, 0x00, 0x01, 0x49, 0x76}, idBuffer))
                .or(() -> assertArrayEquals(new byte[]{0x00, 0x02, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x49, 0x76}, idBuffer))
                .withMessage("ClientIdentification did not match any of the correct variants.")
                .run();

        // Assert ClientAuthentication
        int authentication = 5 + idSize;
        byte[] expectedAuthentication = new byte[] {0x00, 0x03};
        byte[] actualAuthentication = copyOf(buffer, authentication, authentication + 2, "ClientAuthentication");
        assertArrayEquals(actualAuthentication, expectedAuthentication);
        byte[] lengthBuffer = copyOf(buffer, authentication + 2, authentication + 4, "ClientAuthentication k");
        assertArrayEquals(new byte[] {0, 12}, lengthBuffer, "Incorrect length");
        String token = new String(copyOf(buffer, authentication + 4, authentication + 16, "ClientAuthentication token"), StandardCharsets.UTF_8);
        assertEquals("exampleToken", token);

        // Assert end of output
        assertEquals(authentication + 16, buffer.length, "Transferred bytes did not end after ClientAuthentication.");

        // Assert DataHandler attributes
        // This doesn't check if they were set correctly, only if they were set at all.
        assertNotNull(getField(dataHandler, "socket"));
        assertNotNull(getField(dataHandler, "in"));
        assertNotNull(getField(dataHandler, "out"));
        assertTrue(dataHandler.connected);
    }

    @Test
    @Order(3)
    @DisplayName("connect() – incorrect version")
    void testConnectWrongVersion() throws IOException {
        
        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(new byte[]{0x00, 0x00, 0x2b});
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertThrowsExactly(DataHandler.ConnectionException.class);
        assertEquals(0, buffer.length, "Sent unexpected bytes to socket.");

        // Assert DataHandler attributes
        assertNull(getField(dataHandler, "socket"));
        assertNull(getField(dataHandler, "in"));
        assertNull(getField(dataHandler, "out"));
        assertFalse(dataHandler.connected);
    }

    @Test
    @Order(4)
    @DisplayName("connect() – incorrect first byte")
    void testConnectWrongServerHelloFirstByte() throws IOException {
        
        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(new byte[]{0x01, 0x00, 0x2a});
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertThrowsExactly(DataHandler.ConnectionException.class);
        assertEquals(DataHandler.ConnectionException.class, lastThrowable.getClass(), "connect() threw wrong Exception.");
        assertEquals(0, buffer.length, "Sent unexpected bytes to socket.");

        // Assert DataHandler attributes
        assertNull(getField(dataHandler, "socket"));
        assertNull(getField(dataHandler, "in"));
        assertNull(getField(dataHandler, "out"));
        assertFalse(dataHandler.connected);
    }

    @Test
    @Order(5)
    @DisplayName("connect() – incorrect second byte")
    void testConnectWrongServerHelloSecondByte() throws IOException {
        
        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(new byte[]{0x00, 0x01, 0x2a});
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertThrowsExactly(DataHandler.ConnectionException.class);
        assertEquals(0, buffer.length, "Sent unexpected bytes to socket.");

        // Assert DataHandler attributes
        assertNull(getField(dataHandler, "socket"));
        assertNull(getField(dataHandler, "in"));
        assertNull(getField(dataHandler, "out"));
        assertFalse(dataHandler.connected);
    }

    // For switchConnection, we can assume that the server always responds with an ACKNOWLEDGE.
    // However, using getResponse() is still required. This is tested for.
    // (https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/Server.20switchConnection.20ack/near/907721)
    @Test
    @Order(7)
    @DisplayName("switchConnection(int) – successful")
    void testSwitchConnectionSuccess() throws IOException, InterruptedException {
                var handshakeMutex = new HandshakeMutex();
        setField(dataHandler, "handshakeMutex", handshakeMutex);
        // Execute method and read socket output
        Thread clientThread = new Thread(() -> {
            try {
                dataHandler.switchConnection(2346123);
            } catch (DataHandler.ConnectionException e) {
                lastThrowable = e;
            }
        });
        // Send Server Hello
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(SERVER_HELLO);
        client.getOutputStream().flush();
        Thread.sleep(SOCKET_TIMEOUT);
        // Removes the two first bytes from the buffer
        awaitPartnerSwitch(client);
        // Write ACK
        client.getOutputStream().write(SERVER_ACK);
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertTrue(handshakeMutex.wasDequeued, "The handshakeMutex was not empty. This is likely because you did not use getResponse().");
        assertDoesNotThrow();
        byte idSize = copyOf(buffer, 0, 1, "PartnerSwitch k")[0];
        OrBuilder
                .assertThat(() -> assertArrayEquals(new byte[]{0x03, 0x23, (byte) 0xcc, (byte) 0x8b}, buffer))
                .or(() -> assertArrayEquals(new byte[]{0x04, 0x00, 0x23, (byte) 0xcc, (byte) 0x8b}, buffer))
                .or(() -> assertArrayEquals(new byte[]{0x05, 0x00, 0x00, 0x23, (byte) 0xcc, (byte) 0x8b}, buffer))
                .or(() -> assertArrayEquals(new byte[]{0x06, 0x00, 0x00, 0x00, 0x23, (byte) 0xcc, (byte) 0x8b}, buffer))
                .or(() -> assertArrayEquals(new byte[]{0x07, 0x00, 0x00, 0x00, 0x00, 0x23, (byte) 0xcc, (byte) 0x8b}, buffer))
                .or(() -> assertArrayEquals(new byte[]{0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x23, (byte) 0xcc, (byte) 0x8b}, buffer))
                .withMessage("SwitchConnection did not match any of the correct variants.")
                .run();

        // Assert end of output
        assertEquals(1 + idSize, buffer.length, "Transferred bytes did not end after PartnerSwitch.");
    }

    @Test
    @Order(8)
    @DisplayName("sendMessage() – short")
    void testMessages() throws IOException, InterruptedException {
        String message = """
                    I tell you what, this has been really fun.
                    And I got to help make something pretty cool, so I’ve got no complaints.
                    I mean, not me, exactly, but close enough.
                    It’s the kind of thing that makes you glad you stopped and smelled the pine trees along the way, you know?""";
        Thread connectThread = new Thread(this::connect);
        connectThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(SERVER_HELLO);
        client.getOutputStream().flush();
        getAllBytes(connectThread, client);

        Thread clientThread = new Thread(() -> {
            dataHandler.sendMessage(message);
        });
        // Send Server Hello
        clientThread.start();
        Thread.sleep(SOCKET_TIMEOUT);
        // Write ACK
        var buffer = getAllBytes(clientThread, client);
        assertEquals(0x01, buffer[0], "Incorrect first byte");
        var actualLength = copyOf(buffer, 1, 3, "Message length");
        String actualMessage = new String(buffer, StandardCharsets.UTF_8).substring(3);
        int actualMessageLength = actualMessage.length();

        // See below
        if (actualLength[0] == 0x00 || actualLength[0] == 1 && actualLength[1] > 0 && actualLength[1] < 0x0D) {
            assertTrue(actualLength[1] >= 0x0D, "Message length too small. Expected (270 <= length).");
        }
        if (actualLength[0] != 0x01 || actualLength[1] != 0x0D) {
            System.err.println("[WARNING] The length bytes of your message might be incorrect.");
            System.err.println("[WARNING] Due to the restrictions placed on me by the Übungsleitung, I cannot dynamically check whether your message size actually matches.");
            System.err.println("[WARNING] Messages may end with a series of null-bytes. How many bytes there are, and consequently, how long your message is, is dependent on your implementation and OS.");
            System.err.println("[WARNING] Bytes received: " + Arrays.toString(actualLength) + ", Actual length: " + actualMessageLength);
            System.err.println("[WARNING] You will need to check if these values match by hand. Sorry :(");
        }

        // THIS REMOVES NULL-BYTES AT THE END OF THE STRING AND DELIBERATELY ALLOWS FOR A FAULTY REPRESENTATION OF THE MESSAGE
        // This is because the transformation into a byte-array is done incorrectly in the template
        // https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/StandardCharsets.2EUTF_8.2Eencode.28message.29.2Earray.28.29/near/909726
        String trimmedMessage = actualMessage.substring(0, message.length());
        assertTrue(actualMessage.substring(message.length()).matches("\\x00*"), "Message did not terminate with 0 or more null-bytes.");
        assertEquals(message, trimmedMessage, "Incorrect message content.");
    }

    @Test
    @Order(9)
    @DisplayName("sendMessage() – huge")
    void testMessagesHuge() throws IOException, InterruptedException {
        // Ideally, we would use resources here, but that seems difficult with the current tests setup
        String lipsum = Files.readString(Path.of("./test/pgdp/networking/lipsum.txt")).replaceAll("\\r\\n?", "\n");
        String lipsumTruncated = Files.readString(Path.of("./test/pgdp/networking/lipsum_truncated.txt")).replaceAll("\\r\\n?", "\n");
        Thread connectThread = new Thread(this::connect);
        connectThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(SERVER_HELLO);
        client.getOutputStream().flush();
        getAllBytes(connectThread, client);

        Thread clientThread = new Thread(() -> {
            dataHandler.sendMessage(lipsum);
        });
        // Send Server Hello
        clientThread.start();
        Thread.sleep(SOCKET_TIMEOUT);
        // Write ACK
        var buffer = getAllBytes(clientThread, client);
        assertEquals(0x01, buffer[0], "First byte");
        byte[] actualLength = copyOf(buffer, 1, 3, "Message length");
        assertArrayEquals(new byte[] {(byte) 0xff, (byte) 0xff}, actualLength, "Message length was incorrect.");
        assertEquals(lipsumTruncated, new String(buffer, StandardCharsets.UTF_8).substring(3), "Text message was incorrect.");
        // Assert end of output
        assertEquals(65538, buffer.length, "Transferred too many or too few bytes.");
    }

    @AfterAll
    static void cleanup() throws IOException {
        server.close();
    }
    
    /////////////////////////////////////////
    //           UTILITY METHODS           //
    /////////////////////////////////////////

    private void awaitPartnerSwitch(Socket client) throws IOException {
        var in = client.getInputStream();
        while (true) {
            if (in.available() < 2) {
                fail("Did not receive PartnerSwitch message (0x00, 0x04).");
            }
            int first = in.read();
            if (first == 0x00) {
                int second;
                while (true) {
                    second = in.read();
                    if (second != 0x00) {
                        break;
                    }
                }
                if (second == 0x04) {
                    break;
                }
            }
        }
    }

    private static byte[] copyOf(byte[] bytes, int from, int to, String purpose) {
        if (to < from) throw new IllegalArgumentException("`from` may not be greater than `to`.");
        byte[] out = new byte[to - from];
        try {
            System.arraycopy(bytes, from, out, 0, to - from);
        } catch (IndexOutOfBoundsException e) {
            fail(purpose + ": Output ended unexpectedly. Tried to read from index "
                    + from + " up to index " + to + " (exclusive).\nWas: " + Arrays.toString(bytes));
        }
        return out;
    }

    private void connect() {
        try {
            connect.invoke(dataHandler);
            lastThrowable = null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            lastThrowable = e.getCause();
        }
    }

    /**
     * @return All bytes sent to a socket
     */
    private static byte[] getAllBytes(Thread clientThread, Socket client) {
        try {
            clientThread.join();
        } catch (InterruptedException ignored) {}
        try {
            byte[] out = new byte[0];
            int available = client.getInputStream().available();
            while (available > 0) {
                int offset = out.length;
                out = Arrays.copyOf(out, out.length + available);
                client.getInputStream().read(out, offset, available);
                Thread.sleep(SOCKET_TIMEOUT);
                available = client.getInputStream().available();
            }
            return out;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertDoesNotThrow() {
        if (lastThrowable != null) {
            System.err.println("connect() threw an exception when it wasn't supposed to.");
            lastThrowable.printStackTrace();
        }
        assertNull(lastThrowable);
    }

    private void assertThrowsExactly(Class<?> exception) {
        if (lastThrowable == null) {
            System.err.println("connect() did not throw an exception when it was supposed to.");
            assertEquals(exception, null);
        } else if (!lastThrowable.getClass().equals(exception)) {
            System.err.println("connect() threw an incorrect exception.");
            System.err.println("Expected: <" + exception.getName() + ">.");
            System.err.println("Was:");
            lastThrowable.printStackTrace();
            assertEquals(exception, lastThrowable.getClass());
        }
    }

    private static class HandshakeMutex extends LinkedList<Byte> {
        boolean wasDequeued = false;

        @Override
        public Byte remove() {
            if (this.size() == 1)
                wasDequeued = true;
            return super.remove();
        }

    }
}
