package pgdp.networking;

import com.sun.net.httpserver.HttpServer;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class DataHandlerTest {

    DataHandler dataHandler;
    HttpServer server;

    private static final String USERNAME = "TestUser123";
    private static final String ID = "geXYZtup";
    private static final String PASSWORD = "verySecurePassw0rd";
    private static final String DUMMY_TOKEN = "ijd3948rzjsdfgbß9834z";

    @BeforeEach
    public void setup() throws IOException {

        server = HttpServer.create();
        server.bind(new InetSocketAddress("127.0.0.1", 1337), 0);

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .proxy(ProxySelector.of(new InetSocketAddress("127.0.0.1", 1337)))
                .build();

        dataHandler = new DataHandler();
        dataHandler.setClient(client);

        server.start();
    }

    @AfterEach
    public void cleanup() {
        server.stop(0);
    }

    void registerRequestTest(Request actual, String username, String id) {
        assertEquals("POST", actual.requestMethod());
        assertEquals(List.of("application/json"), actual.requestHeaders().get("Content-Type"));
        assertEquals(URI.create("http://carol.sse.cit.tum.de/api/user/register"), actual.requestURI());
        assertEquals("{\"username\": \"" + username + "\",\"tum_kennung\": \"" + id + "\"}", actual.getBodyUTF8());
    }

    @Test
    public void registerSuccessTest() {
        Pair<Boolean, Request> result = testEndpoint("/api/user/register", 200, "", () -> dataHandler.register(USERNAME, ID)); // TODO: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/Definition.20erfolgreiche.20Anfragen
        registerRequestTest(result.getRight(), USERNAME, ID);
        assertTrue(result.getLeft());
    }

    @Test
    public void registerFailTest() {
        Pair<Boolean, Request> result = testEndpoint("/api/users/register", 422, "", () -> dataHandler.register(USERNAME, ID)); // TODO: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/Definition.20erfolgreiche.20Anfragen
        registerRequestTest(result.getRight(), USERNAME, ID);
        assertFalse(result.getLeft());
    }

    private void requestTokenRequestTest(Request actual, String username, String password) {
        assertEquals("POST", actual.requestMethod());
        assertEquals(List.of("application/x-www-form-urlencoded"), actual.requestHeaders().get("Content-Type"));
        assertEquals(URI.create("http://carol.sse.cit.tum.de/token"), actual.requestURI());
        assertEquals("username=" + username + "&password=" + password, actual.getBodyUTF8());
    }

    @Test
    public void requestTokenSuccessTest() {
        Pair<String, Request> result = testEndpoint("/token", 200, "{\n" +
                "  \"access_token\": \"" + DUMMY_TOKEN + "\",\n" +
                "  \"token_type\": \"someTokenType\"\n" +
                "}", () -> callRequestToken(dataHandler, USERNAME, PASSWORD));

        requestTokenRequestTest(result.getRight(), USERNAME, PASSWORD);

        assertEquals(DUMMY_TOKEN, result.getLeft());
    }

    private <T> Pair<T, Request> testEndpoint(String endpoint, int code, String responseBody, Supplier<T> fn) {
        AtomicReference<Request> req = new AtomicReference<>();

        server.createContext(endpoint, exchange -> {
            req.set(new Request(exchange.getRequestMethod(), exchange.getRequestHeaders(), exchange.getRequestURI(), exchange.getRequestBody().readAllBytes()));

            byte[] body = responseBody.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(code, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });

        T result = fn.get();

        server.removeContext(endpoint);

        return Pair.of(result, Objects.requireNonNull(req.get(), "provided function did not send request to specified endpoint"));
    }

    private String callRequestToken(DataHandler dh, String username, String password) {
        try {
            Method m = dh.getClass().getDeclaredMethod("requestToken", String.class, String.class);
            m.setAccessible(true);
            return (String) m.invoke(dh, username, password);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
