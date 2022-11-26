package pgdp.messenger.behavior;


import pgdp.messenger.Message;
import pgdp.messenger.User;

import java.time.LocalDateTime;

public class TestUtils {

    static long userId = 0;
    static long messageId = 0;
    public static User getTestUser(long id, String name, User supervisor) {
        return new User(id, name, supervisor);
    }

    public static User getTestUser() {
        final var user =  getTestUser(userId, "Test", null);
        userId++;
        return user;
    }

    public static Message getTestMessage() {
       return getTestMessage(LocalDateTime.now());
    }

    public static Message getTestMessage(LocalDateTime dateTime) {
        final var message =  new Message(messageId, dateTime, getTestUser(), "Test");
        messageId++;
        return message;
    }
}
