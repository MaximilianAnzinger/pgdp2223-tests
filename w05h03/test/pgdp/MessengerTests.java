package pgdp;

import pgdp.messenger.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import pgdp.messenger.PinguTalk;
import pgdp.messenger.Topic;
import pgdp.messenger.User;
import pgdp.messenger.UserArray;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;


public class MessengerTests {

    /*---- TESTS FÜR UserArray ----*/
    //Master-Test, der alle Konzepte von UserArray beinhalten sollte. Wenn alle anderen UserArray Tests bestanden werden, sollte es auch dieser
    @Test
    @DisplayName("Allgemeiner Test User Array")
    public void testUserArray() {
        UserArray ua = new UserArray(0);
        User alice = new User(69, "Alice", null);
        User bob = new User(420, "Bob", null);
        User eve = new User(1337, "Eve", null);

        ua.addUser(alice);
        Assertions.assertEquals(1, ua.size(), "[Schritt 1.1] Expected Size: 1; Actual: " + ua.size());
        Assertions.assertArrayEquals(new User[] {alice}, ua.getUsers(), "[Schritt 1.2] Expected Users: 'Alice'; Actual: " + Arrays.deepToString(ua.getUsers()));

        ua.addUser(bob);
        Assertions.assertEquals(2, ua.size(), "[Schritt 2.1] Expected Size: 2; Actual: " + ua.size());
        Assertions.assertArrayEquals(new User[] {alice, bob}, ua.getUsers(), "[Schritt 2.2] Expected Users: 'Alice, Bob'; Actual: " + Arrays.deepToString(ua.getUsers()));

        ua.deleteUser(eve.getId());
        Assertions.assertEquals(2, ua.size(), "[Schritt 3.1] Expected Size: 2; Actual: " + ua.size());
        Assertions.assertArrayEquals(new User[] {alice, bob}, ua.getUsers(), "[Schritt 3.2] Expected Users: 'Alice, Bob'; Actual: " + Arrays.deepToString(ua.getUsers()));

        ua.deleteUser(alice.getId());
        Assertions.assertEquals(1, ua.size(), "[Schritt 4.1] Expected Size: 2; Actual: " + ua.size());
        Assertions.assertArrayEquals(new User[] {null, bob}, ua.getUsers(), "[Schritt 4.2] Expected Users: 'null, Bob'; Actual: " + Arrays.deepToString(ua.getUsers()));
        Assertions.assertEquals(2, ua.getUsers().length, "[Schritt 4.3] Expected ArraySize: 2; Actual: " + ua.getUsers().length);

        ua.addUser(eve);
        Assertions.assertEquals(2, ua.size(), "[Schritt 5.1] Expected Size: 2; Actual: " + ua.size());
        Assertions.assertArrayEquals(new User[] {eve, bob}, ua.getUsers(), "[Schritt 5.2] Expected Users: 'Eve, Bob'; Actual: " + Arrays.deepToString(ua.getUsers()));
    }

    @Test
    @DisplayName("UserArray-Konstruktor mit Länge = 5")
    public void testUserArrayConstructor1() {
        UserArray ua = new UserArray(5);
        Assertions.assertEquals(5, ua.getUsers().length);
    }

    @Test
    @DisplayName("UserArray-Konstruktor mit Länge = -4")
    public void testUserArrayConstructor2() {
        UserArray ua = new UserArray(-4);
        Assertions.assertEquals(1, ua.getUsers().length);
    }

    @Test
    @DisplayName("[OPTIONAL] UserArray.addUser")
    public void testUserArrayAddUser1() {
        UserArray ua = new UserArray(3);
        User alice = new User(5, "Alice", null);
        ua.addUser(alice);
        ua.addUser(alice);
        Assertions.assertArrayEquals(new User[] {alice, alice, null}, ua.getUsers(), "Wrong Users");
    }

    @Test
    @DisplayName("UserArray.deleteUser")
    public void testUserArrayDeleteUser1() {
        UserArray ua = new UserArray(3);
        User alice = new User(5, "Alice", null);
        User bob = new User(9, "Bob", null);
        ua.addUser(bob);
        ua.addUser(alice);
        User deleted = ua.deleteUser(bob.getId());
        Assertions.assertEquals(bob, deleted, "Wrong User deleted");
        Assertions.assertArrayEquals(new User[] {null, alice, null}, ua.getUsers());
    }

    @Test
    @DisplayName("UserArray.deleteUser with space")
    public void testUserArrayDeleteUser2() {
        UserArray ua = new UserArray(2);
        User alice = new User(1, "Alice", null);
        User u = ua.deleteUser(alice.getId());
        Assertions.assertArrayEquals(new User[] {null, null}, ua.getUsers(), "Wrong members");
        Assertions.assertEquals(null, u, "Wrong return");
    }

    @Test
    @DisplayName("[OPTIONAL] UserArray.size & .length")
    public void testUserArraySizeAndLength() {
        UserArray ua = new UserArray(0);
        Assertions.assertEquals(0, ua.size(), "Wrong size at start");
        Assertions.assertEquals(1, ua.getUsers().length, "Wrong size at start");
        User alice = new User(5, "Alice", null);
        ua.addUser(alice);
        ua.addUser(alice);
        ua.addUser(alice);
        Assertions.assertEquals(3, ua.size(), "Wrong size after adding Users");
        Assertions.assertEquals(4, ua.getUsers().length, "Wrong size after adding users");
    }

    @Test
    @DisplayName("UserArray.size & .length")
    public void testUserArraySizeAndLength2() {
        UserArray ua = new UserArray(0);
        Assertions.assertEquals(0, ua.size(), "Wrong size at start");
        Assertions.assertEquals(1, ua.getUsers().length, "Wrong size at start");
        User alice = new User(5, "Alice1", null);
        User alice2 = new User(1, "Alice2", null);
        User alice3 = new User(17, "Alice3", null);
        ua.addUser(alice);
        ua.addUser(alice2);
        ua.addUser(alice3);
        Assertions.assertEquals(3, ua.size(), "Wrong size after adding Users");
        Assertions.assertEquals(4, ua.getUsers().length, "Wrong size after adding users");
    }

    @Test
    @DisplayName("UserArray.Getter & Setter")
    public void testUserArrayGetterSetter() {
        UserArray ua = new UserArray(3);
        User alice = new User(5, "Alice", null);
        User[] output = {alice, null, alice, null};
        Assertions.assertEquals(3, ua.getUsers().length, "Wrong size at start");
        ua.setUsers(output);
        Assertions.assertEquals(output, ua.getUsers(), "Wrong members after setting with setter");
    }

    /*---- TESTS FÜR PinguTalk ----*/
    @Test
    @DisplayName("Allgemeiner Test PinguTalk")
    public void testPinguTalk() {
        PinguTalk pt = new PinguTalk(-5, 4);
        Assertions.assertEquals(1, pt.getMembers().getUsers().length, "Wrong size at start");
        Assertions.assertEquals(4, pt.getTopics().length, "Wrong size of topics at start");
        User u = pt.addMember("Alice", null);
        User v = pt.addMember("Bob", null);
        User w = pt.addMember("Eve", null);
        Assertions.assertEquals(4, pt.getMembers().getUsers().length, "[Schritt 1.1] Wrong size");
        Assertions.assertArrayEquals(new User[] {u, v, w, null}, pt.getMembers().getUsers(), "[Schritt 1.2] Wrong members");
        Assertions.assertEquals(3, pt.getMembers().size(), "[Schritt 1.3] Wrong size");
        Assertions.assertEquals(2, w.getId()-u.getId(), "[Schritt 1.34] Wrong IDs");

        User x = pt.deleteMember(v.getId());
        Assertions.assertEquals(x, v, "[Schritt 2.1] Wrong User deleted");
        Assertions.assertArrayEquals(new User[] {u, null, w, null}, pt.getMembers().getUsers(), "[Schritt 2.2] Wrong Users");

        User y = new User(-1000, "Null", null);
        User z = pt.deleteMember(y.getId());
        Assertions.assertEquals(null, z, "[Schritt 3.1] Wrong User deleted");
        Assertions.assertArrayEquals(new User[] {u, null, w, null}, pt.getMembers().getUsers(), "[Schritt 3.2] Wrong Users");

        Topic a = pt.createNewTopic("Topic 1");
        Topic b = pt.createNewTopic("Topic 2");
        Topic c = pt.createNewTopic("Topic 3");
        Assertions.assertEquals(4, pt.getTopics().length, "[Schritt 4.1] Wrong size");
        Assertions.assertArrayEquals(new Topic[] {a, b, c, null}, pt.getTopics(), "[Schritt 4.2] Wrong topics");
        Assertions.assertEquals(2, c.getId() - a.getId(), "[Schritt 4.3] Wrong IDs");

        Topic d = pt.deleteTopic(b.getId());
        Assertions.assertEquals(b, d, "[Schritt 5.1] Wrong Topic deleted");
        Assertions.assertArrayEquals(new Topic[] {a, null, c, null}, pt.getTopics(), "[Schritt 5.2] Wrong Topics");

        Topic e = new Topic(-69, "Topic 4");
        Topic f = pt.deleteTopic(e.getId());
        Assertions.assertEquals(null, f, "[Schritt 6.1] Wrong Topic deleted");
        Assertions.assertArrayEquals(new Topic[] {a, null, c, null}, pt.getTopics(), "[Schritt 6.2] Wrong Topics");

        pt.setTopics(new Topic[] {a, b});
        Assertions.assertArrayEquals(new Topic[] {a, b}, pt.getTopics(), "[Schritt 7.1] Wrong Topics set");
        Topic g = pt.createNewTopic("Topic 5");
        Assertions.assertArrayEquals(new Topic[] {a, b}, pt.getTopics(), "[Schritt 7.2] Wrong Topics set");
        Assertions.assertEquals(null, g, "[Schritt 7.3] Wrong topic");
    }

    @Test
    @DisplayName("PinguTalk.PinguTalk")
    public void testPinguTalkConstructor() {
        PinguTalk pt = new PinguTalk(3, 4);
        Assertions.assertEquals(3, pt.getMembers().getUsers().length, "UserArray: Wrong size at start");
        Assertions.assertEquals(4, pt.getTopics().length, "Topic: Wrong size at start");
    }

    @Test
    @DisplayName("PinguTalk.PinguTalk negativer Input ")
    public void testPinguTalkConstructor2() {
        PinguTalk pt = new PinguTalk(-3, -3);
        Assertions.assertEquals(1, pt.getMembers().getUsers().length, "UserArray: Wrong size at start");
        Assertions.assertEquals(1, pt.getTopics().length, "Topic: Wrong size at start");
    }

    @Test
    @DisplayName("PinguTalk.createNewTopic")
    public void testPinguTalkCreateNewTopic() {
        PinguTalk pt = new PinguTalk(2, 3);
        Topic t = pt.createNewTopic("Topic 1");
        Topic u = pt.createNewTopic("Topic 2");
        Assertions.assertEquals(1, u.getId() - t.getId(), "IDs dont differ by 1");
        Assertions.assertArrayEquals(new Topic[] {t, u, null}, pt.getTopics(), "Wrong topics");
    }

    @Test
    @DisplayName("PinguTalk.createNewTopic with empty space")
    public void testPinguTalkCreateNewTopic2() {
        PinguTalk pt = new PinguTalk(2, 3);
        Topic t = new Topic(9, "Topic 1");
        pt.setTopics(new Topic[] {null, t});
        Topic u = pt.createNewTopic("Topic 2");
        Assertions.assertArrayEquals(new Topic[] {u, t}, pt.getTopics(), "Wrong topics");
    }

    @Test
    @DisplayName("PinguTalk.createNewTopic with no space")
    public void testPinguTalkCreateNewTopic3() {
        PinguTalk pt = new PinguTalk(2, 2);
        Topic t = pt.createNewTopic("Topic 1");
        Topic u = pt.createNewTopic("Topic 2");
        Topic v = pt.createNewTopic("Topic 3");
        Assertions.assertArrayEquals(new Topic[] {t, u}, pt.getTopics(), "Wrong topics");
        Assertions.assertEquals(null, v, "Wrong return for CreateNewTopic");
    }

    @Test
    @DisplayName("PinguTalk.deleteTopic")
    public void testPinguTalkDeleteTopic() {
        PinguTalk pt = new PinguTalk(2, 2);
        Topic t = new Topic(20, "Topic 1");
        Topic u = new Topic(69, "Topic 2");
        Topic v = new Topic(1, "Topic 3");
        pt.setTopics(new Topic[] {t, u});
        Topic w = pt.deleteTopic(t.getId());
        Topic x = pt.deleteTopic(v.getId());
        Assertions.assertArrayEquals(new Topic[] {null, u}, pt.getTopics(), "Wrong topics");
        Assertions.assertEquals(w, t, "Wrong return for DeleteTopic");
        Assertions.assertEquals(null, x, "Wrong return for DeleteTopic");
    }

    @Test
    @DisplayName("PinguTalk.addMember")
    public void testPinguTalkAddMember1() {
        PinguTalk pt = new PinguTalk(2, 2);
        User alice = pt.addMember("Alice", null);
        User bob = pt.addMember("Bob", null);
        User eve = pt.addMember("Eve", null);
        Assertions.assertEquals(3, pt.getMembers().size(), "Wrong size of members");
        Assertions.assertEquals(4, pt.getMembers().getUsers().length, "Wrong size of members");
        Assertions.assertArrayEquals(new User[] {alice, bob, eve, null}, pt.getMembers().getUsers());
    }

    @Test
    @DisplayName("PinguTalk.addMember UserID")
    public void testPinguTalkAddMember2() {
        PinguTalk pt = new PinguTalk(2, 2);
        User alice = pt.addMember("Alice", null);
        User bob = pt.addMember("Bob", null);
        User eve = pt.addMember("Eve", null);
        Assertions.assertEquals(2, eve.getId() - alice.getId(), "Wrong IDs");
    }

    @Test
    @DisplayName("PinguTalk.addMember with space")
    public void testPinguTalkAddMember3() {
        PinguTalk pt = new PinguTalk(2, 2);
        User alice = new User(1, "Alice", null);
        User bob = new User(2, "Bob", null);
        UserArray ua = new UserArray(1);
        ua.setUsers(new User[] {alice, null, bob, null});
        pt.setMembers(ua);
        User eve = pt.addMember("Eve", null);
        Assertions.assertArrayEquals(new User[] {alice, eve, bob, null}, pt.getMembers().getUsers(), "Wrong users");
    }

    @Test
    @DisplayName("PinguTalk.addMember with no space")
    public void testPinguTalkAddMember4() {
        PinguTalk pt = new PinguTalk(2, 2);
        User alice = new User(1, "Alice", null);
        User bob = new User(2, "Bob", null);
        UserArray ua = new UserArray(1);
        ua.setUsers(new User[] {alice, bob});
        pt.setMembers(ua);
        User eve = pt.addMember("Eve", null);
        Assertions.assertArrayEquals(new User[] {alice, bob, eve, null}, pt.getMembers().getUsers(), "Wrong users");
    }

    @Test
    @DisplayName("PinguTalk.deleteMember")
    public void testPinguTalkDeleteMember() {
        PinguTalk pt = new PinguTalk(2, 2);
        User alice = new User(1, "Alice", null);
        User bob = new User(2, "Bob", null);
        UserArray ua = new UserArray(1);
        ua.setUsers(new User[] {alice, bob});
        pt.setMembers(ua);
        User u = pt.deleteMember(alice.getId());
        Assertions.assertArrayEquals(new User[] {null, bob}, pt.getMembers().getUsers(), "Wrong members");
        Assertions.assertEquals(alice, u, "Wrong return");
    }

    @Test
    @DisplayName("PinguTalk.deleteMember not existing")
    public void testPinguTalkDeleteMember2() {
        PinguTalk pt = new PinguTalk(1, 2);
        User alice = new User(1, "Alice", null);
        User u = pt.deleteMember(alice.getId());
        Assertions.assertArrayEquals(new User[] {null}, pt.getMembers().getUsers(), "Wrong members");
        Assertions.assertEquals(null, u, "Wrong return");
    }
}
