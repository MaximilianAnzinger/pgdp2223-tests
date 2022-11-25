package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pgdp.messenger.List;
import pgdp.messenger.Message;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class ListMergeTests {
    @Test
    void MergeNoLists() {
        var actual = List.megaMerge();

        // new list size should be 0
        Assertions.assertEquals(0, actual.size());

        // toString should be an empty string
        Assertions.assertEquals("", actual.toString());

        // check if list isEmpty
        Assertions.assertTrue(actual.isEmpty());
    }

    @Test
    void MergeTwoSequentialLists() {
        var l1 = new List();
        var l2 = new List();
        int id = 0;

        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));

        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));


        var actual = List.megaMerge(l1, l2);

        String expected =
                """
                        0; No Author Available; 2022-01-01T10:21: Message 1
                        1; No Author Available; 2022-01-01T10:22: Message 2
                        2; No Author Available; 2022-01-01T10:23: Message 3
                        3; No Author Available; 2022-01-01T10:24: Message 4
                        4; No Author Available; 2022-01-01T10:25: Message 5
                        5; No Author Available; 2022-01-01T10:26: Message 6
                        6; No Author Available; 2022-01-01T10:27: Message 7
                        7; No Author Available; 2022-01-01T10:28: Message 8
                        8; No Author Available; 2022-01-01T10:29: Message 9
                        9; No Author Available; 2022-01-01T10:30: Message 10
                        10; No Author Available; 2022-01-01T10:31: Message 11
                        """;

        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    void MergeTwoListsInArbitraryOrder() {
        var l1 = new List();
        var l2 = new List();
        int id = 0;

        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));


        var actual = List.megaMerge(l1, l2);

        String expected =
                """
                        0; No Author Available; 2022-01-01T10:21: Message 1
                        1; No Author Available; 2022-01-01T10:22: Message 2
                        2; No Author Available; 2022-01-01T10:23: Message 3
                        3; No Author Available; 2022-01-01T10:24: Message 4
                        4; No Author Available; 2022-01-01T10:25: Message 5
                        5; No Author Available; 2022-01-01T10:26: Message 6
                        6; No Author Available; 2022-01-01T10:27: Message 7
                        7; No Author Available; 2022-01-01T10:28: Message 8
                        8; No Author Available; 2022-01-01T10:29: Message 9
                        9; No Author Available; 2022-01-01T10:30: Message 10
                        10; No Author Available; 2022-01-01T10:31: Message 11
                        """;

        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    void MergeManyListsInArbitraryOrder() {
        var l1 = new List();
        var l2 = new List();
        var l3 = new List();
        var l4 = new List();
        int id = 0;

        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l4.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l4.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l2.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l4.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));
        l1.add(new Message(id++, LocalDateTime.of(2022, 1, 1, 10, 20 + id), null, "Message " + id));


        var actual = List.megaMerge(l3, l1, l2, l4);

        String expected =
                """
                        0; No Author Available; 2022-01-01T10:21: Message 1
                        1; No Author Available; 2022-01-01T10:22: Message 2
                        2; No Author Available; 2022-01-01T10:23: Message 3
                        3; No Author Available; 2022-01-01T10:24: Message 4
                        4; No Author Available; 2022-01-01T10:25: Message 5
                        5; No Author Available; 2022-01-01T10:26: Message 6
                        6; No Author Available; 2022-01-01T10:27: Message 7
                        7; No Author Available; 2022-01-01T10:28: Message 8
                        8; No Author Available; 2022-01-01T10:29: Message 9
                        9; No Author Available; 2022-01-01T10:30: Message 10
                        10; No Author Available; 2022-01-01T10:31: Message 11
                        """;

        Assertions.assertEquals(expected, actual.toString());
    }

    @Test
    void MergeManyEmptyLists() {
        var l1 = new List();
        var l2 = new List();
        var l3 = new List();
        var l4 = new List();
        var actual = List.megaMerge(l3, l1, l2, l4);
        Assertions.assertEquals("", actual.toString());
    }

    @Test
    void MergeManyRandom() {
        List[] lists = generateNLists(10);
        Message[] messages = generateMessagesInOrder(100);
        StringBuilder expected = new StringBuilder();
        Random rdm = new Random();
        for (Message m : messages) {
            expected.append(m).append("\n");
            lists[rdm.nextInt(lists.length)].add(m);
        }

        var merged = List.megaMerge(lists);

        for (int i = 0; i < messages.length; i++) {
            Assertions.assertEquals(messages[i], merged.getByIndex(i));
        }

        Assertions.assertEquals(expected.toString(), merged.toString());
    }

    List[] generateNLists(int n) {
        return Arrays.stream(new List[n]).map((__)->new List()).toArray(List[]::new);
    }

    Message[] generateMessagesInOrder(int howMany) {
        var ret = new Message[howMany];
        for (int i = 0; i < howMany; i++) {
           int id = i+1;
           ret[i] = new Message(id,LocalDateTime.of(2022, 1, ((id/60)/24)%28+1, (id/60)%24, id%60), null, "Message " + id);
        }
        return ret;
   }
}
