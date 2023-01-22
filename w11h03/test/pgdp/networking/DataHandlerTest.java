package pgdp.networking;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.networking.Util.*;

public class DataHandlerTest {

    private static final String USERNAME = "TestUser123";
    private static final String ID = "ge69sex";
    private static final String PASSWORD = "verySecurePassw0rd";
    private static final String DUMMY_TOKEN = "ijd3948rzjsdfgbß9834z";

    void registerRequestTest(Util.Request actual, String username, String id) {
        assertEquals("POST", actual.requestMethod());
        assertEquals(List.of("application/json"), actual.requestHeaders().get("Content-Type"));
        assertEquals(URI.create("http://carol.sse.cit.tum.de/api/user/register"), actual.requestURI());
        assertEquals("{\"username\": \"" + username + "\",\"tum_kennung\": \"" + id + "\"}", actual.getBodyUTF8());
    }

    @Test
    public void registerSuccessTest() throws IOException {
        Pair<Boolean, Request> result = inspectEndpoint("/api/user/register", 200, "{\n" +
                "  \"username\": \"" + USERNAME + "\",\n" +
                "  \"id\": " + ID + "\n" +
                "}", dataHandler -> dataHandler.register(USERNAME, ID));
        registerRequestTest(result.getRight(), USERNAME, ID);
        assertTrue(result.getLeft());
    }

    @Test
    public void registerFailTest() throws IOException {
        Pair<Boolean, Request> result = inspectEndpoint("/api/user/register", 422, "", dataHandler -> dataHandler.register(USERNAME, ID));
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
    public void requestTokenSuccessTest() throws IOException {
        Pair<String, Request> result = inspectEndpoint("/token", 200, "{\n" +
                "  \"access_token\": \"" + DUMMY_TOKEN + "\",\n" +
                "  \"token_type\": \"someTokenType\"\n" +
                "}", dataHandler -> callRequestToken(dataHandler, USERNAME, PASSWORD));

        requestTokenRequestTest(result.getRight(), USERNAME, PASSWORD);

        assertEquals(DUMMY_TOKEN, result.getLeft());
    }

}
