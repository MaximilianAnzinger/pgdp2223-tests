package pgdp.networking;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static pgdp.networking.ReflectUtils.*;

/**
 * These tests do not actually send data anywhere, so no need to worry.
 * Note that these tests assume you are using org.json.* and does not test for incorrect parsing.
 * If something is wrong with my tests, feel free to chat me up on Zulip :)
 * @author Bjarne Hansen
 */
public class HTTPTest {
    private static final String error = """
                        {
                          "detail": [
                            {
                              "loc": [
                                "mock",
                                3259872
                              ],
                              "msg": "Test-Fehler",
                              "type": "intentional"
                            }
                          ]
                        }}""";
    private final DataHandler dataHandler = new DataHandler();
    private HTTPClientMock client;
    private Method requestToken;
    @BeforeEach
    void setup() {
        this.client = new HTTPClientMock();
        setField(dataHandler, "client", client);
        requestToken = getMethod(dataHandler, "requestToken", String.class, String.class);
    }

    @Test
    @DisplayName("register – successful")
    void testRegisterSuccess() {
        client.respond(200, """
                {
                  "username": "Alfred",
                  "id": 387234566
                }""");
        assertTrue(dataHandler.register("Alfred", "ga81fde"));
    }

    @DisplayName("register – unsuccessful")
    @Test
    void testRegisterFailed() {
        client.respond(422, error);
        assertFalse(dataHandler.register("Alfred", "ga81fde"));
    }

    // I believe handling this case is technically required
    @Test
    @DisplayName("register – garbage data")
    void testRegisterGarbage() {
        client.respond(500, "Internal server fuckup");
        assertFalse(dataHandler.register("Alfred", "ga81fde"));
    }

    @Test
    @DisplayName("requestToken – successful")
    void testRequestTokenSuccess() {
        client.respond(200, """
                {
                  "access_token": "d2hhdCBhcmUgeWEgc25vb3BpbmcgYXJvdW5kIGZvcj8=",
                  "token_type": "irrelevant"
                }""");
        assertEquals("d2hhdCBhcmUgeWEgc25vb3BpbmcgYXJvdW5kIGZvcj8=", requestToken("Alfred", "Wagner"));
    }

    @Test
    @DisplayName("requestToken – unsuccessful")
    void testRequestTokenFailed() {
        client.respond(422, error);
        assertNull(requestToken("Alfred", "Wagner"));
    }

    @Test
    @DisplayName("requestToken – garbage data")
    void testRequestTokenGarbage() {
        client.respond(418, "I'm a teapot.");
        assertNull(requestToken("Alfred", "Wagner"));
    }

    @Test
    @DisplayName("login – success")
    void testLoginSuccess() {
        client
                .respond(200, """
                {
                  "access_token": "YXJlIHlvdSBsb29raW5nIGZvciBzb21ldGhpbmc/",
                  "token_type": "irrelevant"
                }""")
                .assertNextRequest(httpRequest -> {
                    var authorization = httpRequest.headers().allValues("Authorization");
                    assertNotNull(authorization);
                    assertTrue(authorization.size() > 0);
                    assertEquals("Bearer YXJlIHlvdSBsb29raW5nIGZvciBzb21ldGhpbmc/", authorization.get(0));
                })
                .respond(200, """
                {
                  "username": "Alfred",
                  "id": 439287
                }""");
        assertTrue(dataHandler.login("Alfred", "Wagner"));
        assertEquals("Alfred", getField(dataHandler, "username"));
        assertEquals("Wagner", getField(dataHandler, "password"));
        assertEquals(439287, getFieldInt(dataHandler, "id"));
    }

    @Test
    @DisplayName("login – no token")
    void testLoginTokenFailed() {
        client
                .respond(422,  error);
        assertFalse(dataHandler.login("Alfred", "Wagner"));
        assertNull(getField(dataHandler, "username"));
        assertNull(getField(dataHandler, "password"));
        assertEquals(0, getFieldInt(dataHandler, "id"));
    }

    @Test
    @DisplayName("login – unsuccessful")
    void testLoginFailed() {
        client
                .respond(200, """
                {
                  "access_token": "aG93IGxvbmcgYXJlIHlvdSBnb25uYSBrZWVwIGRvaW5nIHRoaXM=",
                  "token_type": "irrelevant"
                }""")
                .assertNextRequest(httpRequest -> {
                    var authorization = httpRequest.headers().allValues("Authorization");
                    assertNotNull(authorization);
                    assertTrue(authorization.size() > 0);
                    assertEquals("Bearer aG93IGxvbmcgYXJlIHlvdSBnb25uYSBrZWVwIGRvaW5nIHRoaXM=", authorization.get(0));
                })
                .respond(422, error);
              
        // See: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/login.20Methode.20Attribute/near/909604
        assertFalse(dataHandler.login("Alfred", "Wagner"));
        assertNull(getField(dataHandler, "username"));
        assertNull(getField(dataHandler, "password"));
        assertEquals(0, getFieldInt(dataHandler, "id"));
    }

    // I believe handling this case is technically required
    @Test
    @DisplayName("login – garbage data")
    void testLoginGarbage() {
        client
                .respond(200, """
                {
                  "access_token": "SSdtIG5vdCBoaWRpbmcgYW55dGhpbmcgaGVyZSB5J2tub3c=",
                  "token_type": "irrelevant"
                }""")
                .assertNextRequest(httpRequest -> {
                    var authorization = httpRequest.headers().allValues("Authorization");
                    assertNotNull(authorization);
                    assertTrue(authorization.size() > 0);
                    assertEquals("Bearer SSdtIG5vdCBoaWRpbmcgYW55dGhpbmcgaGVyZSB5J2tub3c=", authorization.get(0));
                })
                .respond(500, "Server's on fire");
        // See: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/login.20Methode.20Attribute/near/909604
        assertFalse(dataHandler.login("Alfred", "Wagner"));
        assertNull(getField(dataHandler, "username"));
        assertNull(getField(dataHandler, "password"));
        assertEquals(0, getFieldInt(dataHandler, "id"));
    }

    @Test
    @DisplayName("getContacts – successful")
    void testGetContactsSuccess() {
        setField(dataHandler, "username", "Alfred");
        setField(dataHandler, "password", "Wagner");
        client
                .respond(200, """
                        {
                          "access_token": "SSBkb24ndCBldmVuIGhhdmUgYW55dGhpbmcgdG8gZ2l2ZSB5b3U=",
                          "token_type": "irrelevant"
                        }""")
                .respond(200, """
                        [
                          {
                            "username": "Svenno",
                            "id": 32489
                          },
                          {
                            "username": "Michel",
                            "id": 32449
                          }
                        ]""");
        // For new Users, `messages` may be an empty list
        // https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/.E2.9C.94.20getContacts.28.29.20Nutzerobjekte/near/902350
        Map<Integer, ViewController.User> expected = Map.of(
                32489, new ViewController.User(32489, "Svenno", List.of()),
                32449, new ViewController.User(32449, "Michel", List.of())
        );
        assertEquals(expected, dataHandler.getContacts());
    }

    @Test
    @DisplayName("getContacts – unsuccessful")
    void testGetContactsFailed() {
        setField(dataHandler, "username", "Alfred");
        setField(dataHandler, "password", "Wagner");
        client
                .respond(200, """
                        {
                          "access_token": "SSBkb24ndCBldmVuIGhhdmUgYW55dGhpbmcgdG8gZ2l2ZSB5b3U=",
                          "token_type": "irrelevant"
                        }""")
                .respond(422, error);

        // See: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/.E2.9C.94.20getContacts.28.29.20return.20type/near/902467
        var contacts = dataHandler.getContacts();
        OrBuilder
                .assertThat(() -> assertNull(contacts))
                .or(() -> assertEquals(Map.of(), contacts))
                .run();
    }

    @Test
    @DisplayName("getContacts – garbage data")
    void testGetContactsGarbage() {
        setField(dataHandler, "username", "Alfred");
        setField(dataHandler, "password", "Wagner");
        client
                .respond(200, """
                        {
                          "access_token": "SSBkb24ndCBldmVuIGhhdmUgYW55dGhpbmcgdG8gZ2l2ZSB5b3U=",
                          "token_type": "irrelevant"
                        }""")
                .respond(500,
                        "The stars! They’re all dying! There’ve been too many supernovae for it to be anything else! We’re next, do you understand?! Our sun! By Hearth’s name, we’re next!");
        // See: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/.E2.9C.94.20getContacts.28.29.20return.20type/near/902467
        var contacts = dataHandler.getContacts();
        OrBuilder
                .assertThat(() -> assertNull(contacts))
                .or(() -> assertEquals(Map.of(), contacts))
                .run();
    }

    @Test
    @DisplayName("getMessagesWithUser – successful")
    void testGetMessagesSuccess() {
        setField(dataHandler, "id", 47);
        client.respond(200, """
                {
                  "access_token": "eW91cmUgbm90IGxldHRpbmcgdXAgYXJlIHlh=",
                  "token_type": "irrelevant"
                }""")
                .respond(200, """
                        [
                          {
                            "from_id": 06,
                            "to_id": 47,
                            "text": "Don't you remember his name? You know this. Deep down, you know. What was his name?",
                            "id": 438,
                            "time": "2022-01-13T10:39:10.808342"
                          },
                          {
                            "from_id": 47,
                            "to_id": 06,
                            "text": "Subject 6. Your name is 6.",
                            "id": 439,
                            "time": "2022-01-13T10:40:10.234983"
                          },
                          {
                            "from_id": 06,
                            "to_id": 47,
                            "text": "And what is our purpose?",
                            "id": 440,
                            "time": "2022-01-13T10:40:20.584023"
                          },
                          {
                            "from_id": 47,
                            "to_id": 06,
                            "text": "To take them all down.",
                            "id": 441,
                            "time": "2022-01-13T10:40:43.349201"
                          }
                        ]
                        """);
        var actual = dataHandler.getMessagesWithUser(06, 47, 0);
        var expected = List.of(
                new ViewController.Message(ofEpoch(1642070350, 808342000), "Don't you remember his name? You know this. Deep down, you know. What was his name?", false, 438),
                new ViewController.Message(ofEpoch(1642070410, 234983000), "Subject 6. Your name is 6.", true, 439),
                new ViewController.Message(ofEpoch(1642070420, 584023000), "And what is our purpose?", false, 440),
                new ViewController.Message(ofEpoch(1642070443, 349201000), "To take them all down.", true, 441)
        );
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getMessagesWithUser – unsuccessful")
    void testGetMessagesFailed() {
        setField(dataHandler, "id", 47);
        client.respond(200, """
                {
                  "access_token": "RmluZSwgeW91IHdpbi4gTWVzc2FnZSBtZSB0aGUgd29yZCAnZ2FiYnJvJyBvbiBadWxpcC4=",
                  "token_type": "irrelevant"
                }""")
                .respond(422, error);
        var result = dataHandler.getMessagesWithUser(06, 47, 0);
        // See: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/Was.20bei.20.60getMessagesWithUser.20.60.20!.3D.20200.20returnen.3F/near/909580
        OrBuilder
                .assertThat(() -> assertNull(result))
                .or(() -> assertEquals(List.of(), result))
                .withMessage("The result of getMessageWithUser did not match an empty list or null.")
                .run();
    }

    @Test
    @DisplayName("getMessagesWithUser – garbage data")
    void testGetMessagesGarbage() {
        setField(dataHandler, "id", 47);
        client.respond(200, """
                {
                  "access_token": "TmV2ZXIgY29udGVudCwgYXJlIHdlPw==",
                  "token_type": "irrelevant"
                }""")
                .respond(500, "This song is new to me, but I am honored to be a part of it." );
        var result = dataHandler.getMessagesWithUser(06, 47, 0);
        // See: https://zulip.in.tum.de/#narrow/stream/1525-PGdP-W11H03/topic/Was.20bei.20.60getMessagesWithUser.20.60.20!.3D.20200.20returnen.3F/near/909580
        OrBuilder
                .assertThat(() -> assertNull(result))
                .or(() -> assertEquals(List.of(), result))
                .withMessage("The result of getMessageWithUser did not match an empty list or null.")
                .run();
    }

    private LocalDateTime ofEpoch(long es, int ns) {
        return LocalDateTime.ofEpochSecond(es, ns, ZoneOffset.UTC);
    }

    private String requestToken(String username, String password) {
        try {
            return (String) requestToken.invoke(dataHandler, username, password);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to invoke requestToken(String, String) on DataHandler.\n" + e);
        }
    }
}
