package pgdp.messenger.behavior;

import org.junit.jupiter.api.Test;
import pgdp.messenger.List;
import pgdp.messenger.Message;
import pgdp.messenger.User;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static pgdp.messenger.behavior.TestUtils.getTestMessage;
import static pgdp.messenger.behavior.TestUtils.getTestUser;

public class ListTest {
    @Test()
    void megaMerge() {
        final var lists = new List[3];
        final var author = new User(1, "Pingu", null);
        Message message1 = new Message(
                1,
                LocalDateTime.now(), author, "content"
        );
        Message message2 = new Message(
                1,
                LocalDateTime.now(), author, "content"
        );
        Message message3 = new Message(
                1,
                LocalDateTime.now(), author, "content"
        );
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new List();
        }
        lists[2].add(message1);
        lists[1].add(message3);
        lists[0].add(message2);
        final var merged = List.megaMerge(lists);
        assertEquals(3, merged.size());
        assertTrue(message1.getTimestamp().isEqual(merged.getByIndex(0).getTimestamp()));
        assertTrue(message2.getTimestamp().isEqual(merged.getByIndex(1).getTimestamp()));
        assertTrue(message3.getTimestamp().isEqual(merged.getByIndex(2).getTimestamp()));
    }

    @Test
    void megaMergeWithDifferentSizedLists() {
        final var lists = new List[2];
        final var messages = new Message[4];
        Arrays.fill(messages, getTestMessage());
        for (int i = 0; i < lists.length; i++) {
            lists[i] = new List();
        }
        lists[1].add(messages[0]);
        lists[1].add(messages[3]);
        lists[1].add(messages[1]);
        lists[0].add(messages[2]);
        final var merged = List.megaMerge(lists);
        assertEquals(messages.length, merged.size());
        for (int i = 0; i < messages.length; i++) {
            assertTrue(messages[i].getTimestamp().isEqual(merged.getByIndex(i).getTimestamp()));
        }
    }

    @Test
    void megaMergeWithEmptyArray (){
        final var merged = List.megaMerge();
        assertEquals(0, merged.size());
    }

    @Test
    void getById() {
        final var message = getTestMessage();
        final var list = new List();
        list.add(message);
        assertEquals(message, list.getByID(message.getId()));
    }

    @Test
    void getByIdShouldReturnNullWhenMessageDoesNotExist() {
        final var message = getTestMessage();
        final var list = new List();
        list.add(message);
        assertNull(list.getByID(-1));
    }

    @Test
    void filterDays() {
        final var list = new List();
        final var messageCreatedAt = LocalDateTime.now();
        final var message = getTestMessage(messageCreatedAt);
        list.add(message);
        final var filtered = list.filterDays(messageCreatedAt, LocalDateTime.now().plusDays(1));
        assertEquals(1, filtered.size());
        assertEquals(message, filtered.getByIndex(0));
    }

    @Test
    void filterDaysShouldRespectEndBeingExclusive() {
        final var list = new List();
        final var messageCreatedAt = LocalDateTime.now().plusDays(1);
        final var message = getTestMessage(messageCreatedAt);
        list.add(message);
        final var filtered = list.filterDays(LocalDateTime.now(), messageCreatedAt);
        assertEquals(0, filtered.size());
    }

    @Test
    void filterDaysShouldReturnEmptyListWhenNoMessagesMatch() {
        final var list = new List();
        final var messageCreatedAt = LocalDateTime.now().plusDays(1);
        final var message = getTestMessage(messageCreatedAt);
        list.add(message);
        final var filtered = list.filterDays(LocalDateTime.now(),messageCreatedAt.minusMinutes(2));
        assertEquals(0, filtered.size());
    }

    @Test
    void filterDaysShouldReturnEmptyListWhenStartOrEndIsNull() {
        final var list = new List();
        final var messageCreatedAt = LocalDateTime.now().plusDays(1);
        final var message = getTestMessage(messageCreatedAt);
        list.add(message);
        final var filtered = list.filterDays(null, LocalDateTime.now().plusDays(1));
        assertEquals(0, filtered.size());
        final var filtered2 = list.filterDays(LocalDateTime.now(), null);
        assertEquals(0, filtered2.size());
    }

    @Test
    void filterDaysShouldReturnEmptyListWhenEndIsBeforeStart() {
        final var list = new List();
        final var messageCreatedAt = LocalDateTime.now();
        final var message = getTestMessage(messageCreatedAt);
        list.add(message);
        final var filtered = list.filterDays(LocalDateTime.now().plusDays(1), LocalDateTime.now());
        assertEquals(0, filtered.size());
    }

    @Test
    void filterDaysShouldNotMutateList() {
        final var list = new List();
        final var messageCreatedAt = LocalDateTime.now();
        final var message = getTestMessage(messageCreatedAt);
        list.add(message);
        list.filterDays(messageCreatedAt, LocalDateTime.now().plusDays(1));
        assertEquals(1, list.size());
        assertEquals(message, list.getByIndex(0));
    }

    @Test
    void filterUser() {
        final var list = new List();
        final var message = getTestMessage();
        list.add(message);
        final var filtered = list.filterUser(message.getAuthor());
        assertEquals(1, filtered.size());
        assertEquals(message, filtered.getByIndex(0));
    }

    @Test
    void filterUserShouldReturnNullWhenQueriedUserIsNull() {
        final var list = new List();
        final var message = getTestMessage();
        list.add(message);
        final var filtered = list.filterUser(null);
        assertEquals(0, filtered.size());
    }

    @Test
    void filterUserShouldNotMutateList() {
        final var list = new List();
        final var message = getTestMessage();
        list.add(message);
        list.filterUser(message.getAuthor());
        assertEquals(1, list.size());
        assertEquals(message, list.getByIndex(0));
    }

    @Test
    void filterUserShouldReturnEmptyListWhenNoMessagesMatch() {
        final var list = new List();
        final var message = getTestMessage();
        list.add(message);
        final var filtered = list.filterUser(getTestUser());
        assertEquals(0, filtered.size());
    }

    @Test
    void filterUserShouldReturnEmptyListWhenListIsEmpty() {
        final var list = new List();
        final var filtered = list.filterUser(getTestUser());
        assertEquals(0, filtered.size());
    }

    @Test
    void toStringShouldReturnEmptyStringWhenListIsEmpty() {
        final var list = new List();
        assertEquals("", list.toString());
    }

    @Test
    void testToString() {
        final var list = new List();
        final var message = getTestMessage();
        list.add(message);
        assertEquals(message.toString() + "\n", list.toString());
    }

    @Test
    void testToStringWithMultipleMessages() {
        final var list = new List();
        final var message = getTestMessage();
        final var message2 = getTestMessage();
        list.add(message);
        list.add(message2);
        assertEquals(message.toString() + "\n" + message2.toString() + "\n", list.toString());
    }
}
