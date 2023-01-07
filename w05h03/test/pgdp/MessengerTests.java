package pgdp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pgdp.messenger.PinguTalk;
import pgdp.messenger.Topic;
import pgdp.messenger.User;
import pgdp.messenger.UserArray;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public class MessengerTests {
    //USERARRAY
    private static User[] getUsers(UserArray ua) {
        return ReflectionHelper.invokeMethod(User[].class, ua,"getUsers", null);
    }

    private static void setUsers(UserArray ua, User[] users) {
        ReflectionHelper.invokeVoidMethod(ua, "setUsers", new Class[]{User[].class}, (Object) users); //weird casting hack because Java varargs don't like arrays
    }

    private static int getSize(UserArray ua) {
        return ReflectionHelper.invokeMethod(int.class, ua, "size", null);
    }

    //PINGUTALK
    private static User addMember(PinguTalk pt, String name, User supervisor) {
        return ReflectionHelper.invokeMethod(User.class, pt, "addMember", new Class[]{String.class, User.class}, name, supervisor);
    }

    private static User deleteMember(PinguTalk pt, long id) {
        return ReflectionHelper.invokeMethod(User.class, pt, "deleteMember", new Class[]{long.class}, id);
    }

    private static Topic createNewTopic(PinguTalk pt, String name) {
        return ReflectionHelper.invokeMethod(Topic.class, pt, "createNewTopic", new Class[]{String.class}, name);
    }

    private static Topic deleteTopic(PinguTalk pt, long id) {
        return ReflectionHelper.invokeMethod(Topic.class, pt, "deleteTopic", new Class[]{long.class}, id);
    }

    private static UserArray getMembers(PinguTalk pt) {
        return ReflectionHelper.invokeMethod(UserArray.class, pt, "getMembers", null);
    }

    private static void setMembers(PinguTalk pt, UserArray ua) {
        ReflectionHelper.invokeVoidMethod(pt, "setMembers", new Class[]{UserArray.class}, ua);
    }

    private static Topic[] getTopics(PinguTalk pt) {
        return ReflectionHelper.invokeMethod(Topic[].class, pt, "getTopics", null);
    }

    private static void setTopics(PinguTalk pt, Topic[] topics) {
        ReflectionHelper.invokeVoidMethod(pt, "setTopics", new Class[]{Topic[].class}, (Object) topics);
    }

    //Constructor
    private static PinguTalk pinguTalk(int userCount, int topicLength) {
        Constructor<PinguTalk> c = ReflectionHelper.getConstructor(PinguTalk.class, int.class, int.class);
        try {
            return c.newInstance(userCount, topicLength);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

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
        Assertions.assertEquals(1, getSize(ua), "[Schritt 1.1] Expected Size: 1; Actual: " + getSize(ua));
        Assertions.assertArrayEquals(new User[] {alice}, getUsers(ua), "[Schritt 1.2] Expected Users: 'Alice'; Actual: " + Arrays.deepToString(getUsers(ua)));

        ua.addUser(bob);
        Assertions.assertEquals(2, getSize(ua), "[Schritt 2.1] Expected Size: 2; Actual: " + getSize(ua));
        Assertions.assertArrayEquals(new User[] {alice, bob}, getUsers(ua), "[Schritt 2.2] Expected Users: 'Alice, Bob'; Actual: " + Arrays.deepToString(getUsers(ua)));

        ua.deleteUser(eve.getId());
        Assertions.assertEquals(2, getSize(ua), "[Schritt 3.1] Expected Size: 2; Actual: " + getSize(ua));
        Assertions.assertArrayEquals(new User[] {alice, bob}, getUsers(ua), "[Schritt 3.2] Expected Users: 'Alice, Bob'; Actual: " + Arrays.deepToString(getUsers(ua)));

        ua.deleteUser(alice.getId());
        Assertions.assertEquals(1, getSize(ua), "[Schritt 4.1] Expected Size: 2; Actual: " + getSize(ua));
        Assertions.assertArrayEquals(new User[] {null, bob}, getUsers(ua), "[Schritt 4.2] Expected Users: 'null, Bob'; Actual: " + Arrays.deepToString(getUsers(ua)));
        Assertions.assertEquals(2, getUsers(ua).length, "[Schritt 4.3] Expected ArraySize: 2; Actual: " + getUsers(ua).length);

        ua.addUser(eve);
        Assertions.assertEquals(2, getSize(ua), "[Schritt 5.1] Expected Size: 2; Actual: " + getSize(ua));
        Assertions.assertArrayEquals(new User[] {eve, bob}, getUsers(ua), "[Schritt 5.2] Expected Users: 'Eve, Bob'; Actual: " + Arrays.deepToString(getUsers(ua)));
    }

    @Test
    @DisplayName("UserArray-Konstruktor mit Länge = 5")
    public void testUserArrayConstructor1() {
        UserArray ua = new UserArray(5);
        Assertions.assertEquals(5, getUsers(ua).length);
    }

    @Test
    @DisplayName("UserArray-Konstruktor mit Länge = -4")
    public void testUserArrayConstructor2() {
        UserArray ua = new UserArray(-4);
        Assertions.assertEquals(1, getUsers(ua).length);
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
        Assertions.assertArrayEquals(new User[] {null, alice, null}, getUsers(ua));
    }

    @Test
    @DisplayName("UserArray.deleteUser with space")
    public void testUserArrayDeleteUser2() {
        UserArray ua = new UserArray(2);
        User alice = new User(1, "Alice", null);
        User u = ua.deleteUser(alice.getId());
        Assertions.assertArrayEquals(new User[] {null, null}, getUsers(ua), "Wrong members");
        Assertions.assertEquals(null, u, "Wrong return");
    }

    @Test
    @DisplayName("UserArray.size & .length")
    public void testUserArraySizeAndLength2() {
        UserArray ua = new UserArray(0);
        Assertions.assertEquals(0, getSize(ua), "Wrong size at start");
        Assertions.assertEquals(1, getUsers(ua).length, "Wrong size at start");
        User alice = new User(5, "Alice1", null);
        User alice2 = new User(1, "Alice2", null);
        User alice3 = new User(17, "Alice3", null);
        ua.addUser(alice);
        ua.addUser(alice2);
        ua.addUser(alice3);
        Assertions.assertEquals(3, getSize(ua), "Wrong size after adding Users");
        Assertions.assertEquals(4, getUsers(ua).length, "Wrong size after adding users");
    }

    @Test
    @DisplayName("UserArray.Getter & Setter")
    public void testUserArrayGetterSetter() {
        UserArray ua = new UserArray(3);
        User alice = new User(5, "Alice", null);
        User[] output = {alice, null, alice, null};
        Assertions.assertEquals(3, getUsers(ua).length, "Wrong size at start");
        setUsers(ua, output);
        Assertions.assertEquals(output, getUsers(ua), "Wrong members after setting with setter");
    }

    /*---- TESTS FÜR PinguTalk ----*/
    @Test
    @DisplayName("Allgemeiner Test PinguTalk")
    public void testPinguTalk() {
        PinguTalk pt = pinguTalk(-5, 4);
        Assertions.assertEquals(1, getUsers(getMembers(pt)).length, "Wrong size at start");
        Assertions.assertEquals(4, getTopics(pt).length, "Wrong size of topics at start");
        User u = addMember(pt, "Alice", null);
        User v = addMember(pt, "Bob", null);
        User w = addMember(pt, "Eve", null);
        Assertions.assertEquals(4, getUsers(getMembers(pt)).length, "[Schritt 1.1] Wrong size");
        Assertions.assertArrayEquals(new User[] {u, v, w, null}, getUsers(getMembers(pt)), "[Schritt 1.2] Wrong members");
        Assertions.assertEquals(3, getMembers(pt).size(), "[Schritt 1.3] Wrong size");
        Assertions.assertEquals(2, w.getId()-u.getId(), "[Schritt 1.34] Wrong IDs");

        User x = deleteMember(pt, v.getId());
        Assertions.assertEquals(x, v, "[Schritt 2.1] Wrong User deleted");
        Assertions.assertArrayEquals(new User[] {u, null, w, null}, getUsers(getMembers(pt)), "[Schritt 2.2] Wrong Users");

        User y = new User(-1000, "Null", null);
        User z = deleteMember(pt, y.getId());
        Assertions.assertEquals(null, z, "[Schritt 3.1] Wrong User deleted");
        Assertions.assertArrayEquals(new User[] {u, null, w, null}, getUsers(getMembers(pt)), "[Schritt 3.2] Wrong Users");

        Topic a = createNewTopic(pt, "Topic 1");
        Topic b = createNewTopic(pt, "Topic 2");
        Topic c = createNewTopic(pt, "Topic 3");
        Assertions.assertEquals(4, getTopics(pt).length, "[Schritt 4.1] Wrong size");
        Assertions.assertArrayEquals(new Topic[] {a, b, c, null}, getTopics(pt), "[Schritt 4.2] Wrong topics");
        Assertions.assertEquals(2, c.getId() - a.getId(), "[Schritt 4.3] Wrong IDs");

        Topic d = deleteTopic(pt, b.getId());
        Assertions.assertEquals(b, d, "[Schritt 5.1] Wrong Topic deleted");
        Assertions.assertArrayEquals(new Topic[] {a, null, c, null}, getTopics(pt), "[Schritt 5.2] Wrong Topics");

        Topic e = new Topic(-69, "Topic 4");
        Topic f = deleteTopic(pt, e.getId());
        Assertions.assertEquals(null, f, "[Schritt 6.1] Wrong Topic deleted");
        Assertions.assertArrayEquals(new Topic[] {a, null, c, null}, getTopics(pt), "[Schritt 6.2] Wrong Topics");

        setTopics(pt, new Topic[] {a, b});
        Assertions.assertArrayEquals(new Topic[] {a, b}, getTopics(pt), "[Schritt 7.1] Wrong Topics set");
        Topic g = createNewTopic(pt, "Topic 5");
        Assertions.assertArrayEquals(new Topic[] {a, b}, getTopics(pt), "[Schritt 7.2] Wrong Topics set");
        Assertions.assertEquals(null, g, "[Schritt 7.3] Wrong topic");
    }

    @Test
    @DisplayName("PinguTalk.PinguTalk")
    public void testPinguTalkConstructor() {
        PinguTalk pt = pinguTalk(3, 4);
        Assertions.assertEquals(3, getUsers(getMembers(pt)).length, "UserArray: Wrong size at start");
        Assertions.assertEquals(4, getTopics(pt).length, "Topic: Wrong size at start");
    }

    @Test
    @DisplayName("PinguTalk.PinguTalk negativer Input ")
    public void testPinguTalkConstructor2() {
        PinguTalk pt = pinguTalk(-3, -3);
        Assertions.assertEquals(1, getUsers(getMembers(pt)).length, "UserArray: Wrong size at start");
        Assertions.assertEquals(1, getTopics(pt).length, "Topic: Wrong size at start");
    }

    @Test
    @DisplayName("PinguTalk.createNewTopic")
    public void testPinguTalkCreateNewTopic() {
        PinguTalk pt = pinguTalk(2, 3);
        Topic t = createNewTopic(pt, "Topic 1");
        Topic u = createNewTopic(pt, "Topic 2");
        Assertions.assertEquals(1, u.getId() - t.getId(), "IDs dont differ by 1");
        Assertions.assertArrayEquals(new Topic[] {t, u, null}, getTopics(pt), "Wrong topics");
    }

    @Test
    @DisplayName("PinguTalk.createNewTopic with empty space")
    public void testPinguTalkCreateNewTopic2() {
        PinguTalk pt = pinguTalk(2, 3);
        Topic t = new Topic(9, "Topic 1");
        setTopics(pt, new Topic[] {null, t});
        Topic u = createNewTopic(pt, "Topic 2");
        Assertions.assertArrayEquals(new Topic[] {u, t}, getTopics(pt), "Wrong topics");
    }

    @Test
    @DisplayName("PinguTalk.createNewTopic with no space")
    public void testPinguTalkCreateNewTopic3() {
        PinguTalk pt = pinguTalk(2, 2);
        Topic t = createNewTopic(pt, "Topic 1");
        Topic u = createNewTopic(pt, "Topic 2");
        Topic v = createNewTopic(pt, "Topic 3");
        Assertions.assertArrayEquals(new Topic[] {t, u}, getTopics(pt), "Wrong topics");
        Assertions.assertEquals(null, v, "Wrong return for CreateNewTopic");
    }

    @Test
    @DisplayName("PinguTalk.deleteTopic")
    public void testPinguTalkDeleteTopic() {
        PinguTalk pt = pinguTalk(2, 2);
        Topic t = new Topic(20, "Topic 1");
        Topic u = new Topic(69, "Topic 2");
        Topic v = new Topic(1, "Topic 3");
        setTopics(pt, new Topic[] {t, u});
        Topic w = deleteTopic(pt, t.getId());
        Topic x = deleteTopic(pt, v.getId());
        Assertions.assertArrayEquals(new Topic[] {null, u}, getTopics(pt), "Wrong topics");
        Assertions.assertEquals(w, t, "Wrong return for DeleteTopic");
        Assertions.assertEquals(null, x, "Wrong return for DeleteTopic");
    }

    @Test
    @DisplayName("PinguTalk.addMember")
    public void testPinguTalkAddMember1() {
        PinguTalk pt = pinguTalk(2, 2);
        User alice = addMember(pt, "Alice", null);
        User bob = addMember(pt, "Bob", null);
        User eve = addMember(pt, "Eve", null);
        Assertions.assertEquals(3, getMembers(pt).size(), "Wrong size of members");
        Assertions.assertEquals(4, getUsers(getMembers(pt)).length, "Wrong size of members");
        Assertions.assertArrayEquals(new User[] {alice, bob, eve, null}, getUsers(getMembers(pt)));
    }

    @Test
    @DisplayName("PinguTalk.addMember UserID")
    public void testPinguTalkAddMember2() {
        PinguTalk pt = pinguTalk(2, 2);
        User alice = addMember(pt, "Alice", null);
        User bob = addMember(pt,"Bob", null);
        User eve = addMember(pt, "Eve", null);
        Assertions.assertEquals(2, eve.getId() - alice.getId(), "Wrong IDs");
    }

    @Test
    @DisplayName("PinguTalk.addMember with space")
    public void testPinguTalkAddMember3() {
        PinguTalk pt = pinguTalk(2, 2);
        User alice = new User(1, "Alice", null);
        User bob = new User(2, "Bob", null);
        UserArray ua = new UserArray(1);
        setUsers(ua, new User[] {alice, null, bob, null});
        setMembers(pt, ua);
        User eve = addMember(pt, "Eve", null);
        Assertions.assertArrayEquals(new User[] {alice, eve, bob, null}, getUsers(getMembers(pt)), "Wrong users");
    }

    @Test
    @DisplayName("PinguTalk.addMember with no space")
    public void testPinguTalkAddMember4() {
        PinguTalk pt = pinguTalk(2, 2);
        User alice = new User(1, "Alice", null);
        User bob = new User(2, "Bob", null);
        UserArray ua = new UserArray(1);
        setUsers(ua, new User[] {alice, bob});
        setMembers(pt, ua);
        User eve = addMember(pt, "Eve", null);
        Assertions.assertArrayEquals(new User[] {alice, bob, eve, null}, getUsers(getMembers(pt)), "Wrong users");
    }

    @Test
    @DisplayName("PinguTalk.deleteMember")
    public void testPinguTalkDeleteMember() {
        PinguTalk pt = pinguTalk(2, 2);
        User alice = new User(1, "Alice", null);
        User bob = new User(2, "Bob", null);
        UserArray ua = new UserArray(1);
        setUsers(ua, new User[] {alice, bob});
        setMembers(pt, ua);
        User u = deleteMember(pt, alice.getId());
        Assertions.assertArrayEquals(new User[] {null, bob}, getUsers(getMembers(pt)), "Wrong members");
        Assertions.assertEquals(alice, u, "Wrong return");
    }

    @Test
    @DisplayName("PinguTalk.deleteMember not existing")
    public void testPinguTalkDeleteMember2() {
        PinguTalk pt = pinguTalk(1, 2);
        User alice = new User(1, "Alice", null);
        User u = deleteMember(pt, alice.getId());
        Assertions.assertArrayEquals(new User[] {null}, getUsers(getMembers(pt)), "Wrong members");
        Assertions.assertEquals(null, u, "Wrong return");
    }
}
