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

@TestMethodOrder(MethodOrderer.MethodName.class)
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
    private static final byte[] AUTHENTICATION_FAILURE = new byte[]{0x00, (byte) 0xf1};
    private static final byte[] SERVER_ACK = new byte[]{0x00, 0x05};
    private static final byte[] SERVER_TIMEOUT = new byte[] {(byte) 0x00, (byte) 0xff};

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
        System.out.println("""
                If you are using a slower device and your SocketTests keep failing for no reason, try increasing the SOCKET_TIMEOUT.""");
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

    @Test
    @DisplayName("connect() – successful")
    void testConnectSuccess() throws IOException {
        warnConnect();

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
        int id = bufferToInt(copyOf(buffer, 5, 5 + idSize, "ClientIdentification id"));
        assertEquals(84342, id, "Client sent incorrect id.");

        // Assert ClientAuthentication
        int authentication = 5 + idSize;
        byte[] expectedAuthentication = new byte[] {0x00, 0x03};
        byte[] actualAuthentication = copyOf(buffer, authentication, authentication + 2, "ClientAuthentication");
        assertArrayEquals(actualAuthentication, expectedAuthentication);
        int tokenLength = bufferToInt(copyOf(buffer, authentication + 2, authentication + 4, "ClientAuthentication k"));
        String token = new String(copyOf(buffer, authentication + 4, authentication + 4 + tokenLength, "ClientAuthentication token"), StandardCharsets.UTF_8);
        assertEquals("exampleToken", token);

        // Assert end of output
        assertEquals(authentication + 4 + tokenLength, buffer.length, "Transferred bytes did not end after ClientAuthentication.");

        // Assert DataHandler attributes
        // This doesn't check if they were set correctly, only if they were set at all.
        assertNotNull(getField(dataHandler, "socket"));
        assertNotNull(getField(dataHandler, "in"));
        assertNotNull(getField(dataHandler, "out"));
        assertTrue(dataHandler.connected);
    }

    @DisplayName("connect() – no response")
    @Test
     void testConnectNoResponse() throws IOException {
        warnConnect();

        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        var buffer = getAllBytes(clientThread, client);
        assertNotNull(lastThrowable, "connect() did not throw an exception when it was supposed to.");
        assertEquals(DataHandler.ConnectionException.class, lastThrowable.getClass(), "connect() threw wrong Exception.");
        assertEquals(0, buffer.length, "Sent unexpected bytes to socket.");

        // Assert DataHandler attributes
        assertNull(getField(dataHandler, "socket"));
        assertNull(getField(dataHandler, "in"));
        assertNull(getField(dataHandler, "out"));
        assertFalse(dataHandler.connected);
    }

    @Test
    @DisplayName("connect() – incorrect version")
    void testConnectWrongVersion() throws IOException {
        warnConnect();

        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(new byte[]{0x00, 0x00, 0x2b});
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertNotNull(lastThrowable, "connect() did not throw an exception when it was supposed to.");
        assertEquals(DataHandler.ConnectionException.class, lastThrowable.getClass(), "connect() threw wrong Exception.");
        assertEquals(0, buffer.length, "Sent unexpected bytes to socket.");

        // Assert DataHandler attributes
        assertNull(getField(dataHandler, "socket"));
        assertNull(getField(dataHandler, "in"));
        assertNull(getField(dataHandler, "out"));
        assertFalse(dataHandler.connected);
    }

    @Test
    @DisplayName("connect() – incorrect first byte")
    void testConnectWrongServerHelloFirstByte() throws IOException {
        warnConnect();

        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(new byte[]{0x01, 0x00, 0x2a});
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertNotNull(lastThrowable, "connect() did not throw an exception when it was supposed to.");
        assertEquals(DataHandler.ConnectionException.class, lastThrowable.getClass(), "connect() threw wrong Exception.");
        assertEquals(0, buffer.length, "Sent unexpected bytes to socket.");

        // Assert DataHandler attributes
        assertNull(getField(dataHandler, "socket"));
        assertNull(getField(dataHandler, "in"));
        assertNull(getField(dataHandler, "out"));
        assertFalse(dataHandler.connected);
    }

    @Test
    @DisplayName("connect() – incorrect second byte")
    void testConnectWrongServerHelloSecondByte() throws IOException {
        warnConnect();

        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(new byte[]{0x00, 0x01, 0x2a});
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertNotNull(lastThrowable, "connect() did not throw an exception when it was supposed to.");
        assertEquals(DataHandler.ConnectionException.class, lastThrowable.getClass(), "connect() threw wrong Exception.");
        assertEquals(0, buffer.length, "Sent unexpected bytes to socket.");

        // Assert DataHandler attributes
        assertNull(getField(dataHandler, "socket"));
        assertNull(getField(dataHandler, "in"));
        assertNull(getField(dataHandler, "out"));
        assertFalse(dataHandler.connected);
    }

    @Test
    @DisplayName("connect() – authentication failure")
    void testConnectAuthenticationFailure() throws IOException {
        warnConnect();

        // Execute method and read socket output
        Thread clientThread = new Thread(this::connect);
        clientThread.start();
        Socket client = server.accept();
        client.getOutputStream().write(SERVER_HELLO);
        client.getOutputStream().flush();
        client.getOutputStream().write(AUTHENTICATION_FAILURE);
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertNotNull(lastThrowable, "connect() did not throw an exception when it was supposed to.");
        assertEquals(DataHandler.ConnectionException.class, lastThrowable.getClass(), "connect() threw wrong Exception.");

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
    @DisplayName("switchConnection(int) – successful")
    void testSwitchConnectionSuccess() throws IOException, InterruptedException {
        warnConnect();
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
        awaitPartnerSwitch(client);
        // Write ACK
        client.getOutputStream().write(SERVER_ACK);
        client.getOutputStream().flush();
        var buffer = getAllBytes(clientThread, client);
        assertTrue(handshakeMutex.wasDequeued, "The handshakeMutex was not empty. This is likely because you did not use getResponse().");
        assertNull(lastThrowable, "switchConnection() threw an exception when it wasn't supposed to.");
        byte idSize = copyOf(buffer, 0, 1, "PartnerSwitch k")[0];
        long id = bufferToLong(copyOf(buffer, 1, 1 + idSize, "PartnerSwitch id"));
        assertEquals(2346123, id, "Client sent incorrect id.");

        // Assert end of output
        assertEquals(1 + idSize, buffer.length, "Transferred bytes did not end after PartnerSwitch.");
    }

    @Test
    @DisplayName("sendMessage() – short")
    void testMessages() throws IOException, InterruptedException {
        String message = """
                    I tell you what, this has been really fun.
                    And I got to help make something pretty cool, so I’ve got no complaints.
                    I mean, not me, exactly, but close enough.
                    It’s the kind of thing that makes you glad you stopped and smelled the pine trees along the way, you know?""";
        byte[] messageBuffer = StandardCharsets.UTF_8.encode(message).array();
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
        assertEquals(0x01, buffer[0], "First byte");
        assertEquals(messageBuffer.length, bufferToInt(copyOf(buffer, 1, 3, "Message length")), "Message length");

        var expected = new byte[3 + messageBuffer.length];
        expected[0] = 0x01;
        expected[1] = (byte) 1;
        expected[2] = (byte) 35;
        System.arraycopy(messageBuffer, 0, expected, 3, expected.length - 3);
        assertArrayEquals(expected, buffer);

        // Assert end of output
        assertEquals(expected.length, buffer.length, "Transferred bytes did not end after expected message.");
    }

    @Test
    @DisplayName("sendMessage() – huge")
    void testMessagesHuge() throws IOException, InterruptedException {
        // Ideally, we would use resources here, but that seems difficult with the current tests setup
        String lipsum = Files.readString(Path.of("./test/pgdp/networking/lipsum.txt"));
        String lipsumTruncated = Files.readString(Path.of("./test/pgdp/networking/lipsum_truncated.txt"));
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
        int messageLength = bufferToInt(copyOf(buffer, 1, 3, "Message length"));
        assertEquals(0xffff, messageLength, "Message length was incorrect.");
        assertEquals(lipsumTruncated, new String(buffer).substring(3), "Text message was incorrect.");
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

    private void warnConnect() {
        System.out.println("""
                If this test doesn't finish, it's likely because you are using the address of the server (carol.sse.cit.tum.de) directly.
                Instead, please use the variable DataHandler.serverAddress.
                """);
        System.out.println("""
                If this method does not finish, you likely threw an Exception that was not DataHandler.ConnectionException or your client is trying to read bytes which are never being sent.
                """);
    }

    private static int bufferToInt(byte[] bytes) {
        if (bytes.length > 4) throw new IllegalArgumentException("`bytes.length` may not be larger than 4.");
        int n = 0;
        for (int i = 0; i < bytes.length; i++) {
            int shift = (bytes.length - i - 1) * 8;
            int b = bytes[i];
            if (b < 0) b = 256 + b;
            n |= b << shift;
        }
        return n;
    }

    private static long bufferToLong(byte[] bytes) {
        if (bytes.length > 8) throw new IllegalArgumentException("`bytes.length` may not be larger than 8.");
        long n = 0;
        for (int i = 0; i < bytes.length; i++) {
            int shift = (bytes.length - i - 1) * 8;
            long b = bytes[i];
            if (b < 0) b = 256 + b;
            n |= b << shift;
        }
        return n;
    }

    /**
     * @return All bytes sent to a socket
     */
    private static byte[] getAllBytes(Thread clientThread, Socket client) {
        try {
            clientThread.join();
        } catch (InterruptedException ignored) {}
        try {
            byte[] out = new byte[client.getInputStream().available()];
            client.getInputStream().read(out);
            return out;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
