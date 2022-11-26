package pgdp.messenger.test;
import org.junit.jupiter.api.*;
import pgdp.messenger.*;

import java.util.Arrays;


public class UserArrayBehaviourTest {
    private User Peter, David, Anna, Linda, George, Dan, Andrew, Mike, Emily, Susan;
    private User supervisor = new User(1000, "Big Boss", null);

    @BeforeEach
    @DisplayName("Initializing some user instances")
    void setupUsers() {
        Peter = new User(1, "Peter", supervisor);
        David = new User(2, "David", supervisor);
        Anna = new User(3, "Anna", supervisor);
        Linda = new User(4, "Linda", supervisor);
        George = new User(5, "George", supervisor);
        Dan = new User(6, "Dan", supervisor);
        Andrew = new User(7, "Andrew", supervisor);
        Mike = new User(8, "Mike", supervisor);
        Emily = new User(9, "Emily", supervisor);
        Susan = new User(10, "Susan", supervisor);
    }

    @Nested
    @DisplayName("Initializing UserArray with different lengths params")
    class initUserArray {
        @Test
        @DisplayName("Test initial lenght of UserArray")
        void setUserArrayLength() {
            UserArray userArray = new UserArray(10);
            Assertions.assertEquals(10, userArray.getUsers().length, "Wrong length of the UserArray. Expected 10, but got " + userArray.getUsers().length + " instead.");
            Assertions.assertArrayEquals(new User[10], userArray.getUsers(), "Wrong list of users. Expected empty array of users. Got " + Arrays.toString(userArray.getUsers()) + " instead.");
        }

        @Test
        @DisplayName("Test initial length of UserArray set to negative number")
        void setUserArrayLengthNegative() {
            UserArray userArray = new UserArray(-3);
            Assertions.assertEquals(1, userArray.getUsers().length, "Wrong length of the UserArray. Expected 1, but got " + userArray.getUsers().length + " instead.");
            Assertions.assertArrayEquals(new User[1], userArray.getUsers(), "Wrong list of users. Expected empty array of users. Got " + Arrays.toString(userArray.getUsers()) + " instead.");
        }
    }

    @Nested
    @DisplayName("Test differnt options of adding a user to the UserArray")
    class addUserToUserArray {
        @Test
        @DisplayName("Add user to empty UserArray")
        void addUserToEmptyUserArray() {
            UserArray userArray = new UserArray(4);
            userArray.addUser(Peter);
            Assertions.assertEquals(1, userArray.size(), "Wrong number of the users in the UserArray. Expected 1, but got " + userArray.size() + " instead");
            Assertions.assertArrayEquals(new User[]{Peter, null, null, null}, userArray.getUsers(), "Wrong list of users in the UserArray");
        }

        @Test
        @DisplayName("Add null user")
        void addNullUser() {
            UserArray userArray = new UserArray(4);
            userArray.addUser(Mike);
            userArray.addUser(Emily);
            userArray.addUser(null);

            Assertions.assertEquals(2, userArray.size(), "Wrong number of the users in the UserArray. Expected 2, but got " + userArray.size() + " instead");
            Assertions.assertArrayEquals(new User[]{Mike, Emily, null, null}, userArray.getUsers(), "Wrong list of users in the UserArray");
        }

        @Test
        @DisplayName("Add user to the end of UserArray")
        void addUserToTheEnd() {
            UserArray userArray = new UserArray(4);
            userArray.addUser(Peter);
            userArray.addUser(David);
            userArray.addUser(Anna);
            userArray.addUser(Linda);
            Assertions.assertEquals(4, userArray.size(), "Wrong number of the users in the UserArray. Expected 4, but got " + userArray.size() + " instead");
            Assertions.assertEquals(4, userArray.getUsers().length, "Wrong length of the UserArray. Expected 4, but got " + userArray.getUsers().length + " instead.");
            Assertions.assertArrayEquals(new User[]{Peter, David, Anna, Linda}, userArray.getUsers(), "Wrong list of users in the UserArray");
        }

        @Test
        @DisplayName("Add user to the first empty place in the UserArray - with removeUser")
        void addToEmptyPlaceWithRemoveUser() {
            UserArray userArray = new UserArray(5);
            userArray.addUser(David);
            userArray.addUser(Emily);
            userArray.addUser(Susan);
            userArray.addUser(Andrew);
            userArray.deleteUser(Susan.getId());

            Assertions.assertEquals(3, userArray.size(), "Wrong number of the users in the UserArray. Expected 3, but got " + userArray.size() + " instead");
            userArray.addUser(Mike);

            Assertions.assertEquals(4, userArray.size(), "Wrong number of the users in the UserArray. Expected 4, but got " + userArray.size() + " instead");
            Assertions.assertArrayEquals(new User[]{David, Emily, Mike, Andrew, null}, userArray.getUsers(), "Wrong list of users");
        }

        @Test
        @DisplayName("Add user to a full array")
        void addUserToFullArray() {
            UserArray userArray = new UserArray(4);
            userArray.setUsers(new User[]{Mike, Peter, Dan, David});

            userArray.addUser(Anna);
            userArray.addUser(Linda);

            Assertions.assertEquals(8, userArray.getUsers().length, "Wrong length of the UserArray. Expected 8, but got " + userArray.getUsers().length + " instead.");
            Assertions.assertEquals(6, userArray.size(), "Wrong number of the users in the UserArray. Expected 6, but got " + userArray.size() + " instead");
        }
    }


    @Nested
    @DisplayName("Test users setter with different params")
    class userSetter {

        //https://zulip.in.tum.de/#narrow/stream/1467-PGdP-W05H03/topic/UserArray.20size
        @Test
        @DisplayName("Test users setter for the correct size - number of users")
        void testUsersSetter() {
            UserArray userArray = new UserArray(5);
            userArray.setUsers(new User[]{David, Emily, Andrew, null, null});
            Assertions.assertEquals(3, userArray.size(), "Wrong number of the users in the UserArray. Expected 3, but got " + userArray.size() + " instead");
        }

        //https://zulip.in.tum.de/#narrow/stream/1467-PGdP-W05H03/topic/UserArray.20addUser
        // TODO ask about the case when the given array is shorter than the original
        @Test
        @DisplayName("Test size and length of the users array after using the setter")
        void testUsersSetterSmallerArray() {
            UserArray userArray = new UserArray(5);
            userArray.setUsers(new User[]{David, Emily});
            Assertions.assertEquals(2, userArray.size(), "Wrong number of the users in the UserArray. Expected 2, but got " + userArray.size() + " instead");
            Assertions.assertEquals(2, userArray.getUsers().length, "Wrong length of the UserArray. Expected 2, but got " + userArray.size() + " instead");
        }
    }

    @Nested
    @DisplayName("Test different options how to delete user from UserArray")
    class deleteUserFromUserArray {
        @Test
        @DisplayName("Remove user from the user array")
        void removeUser() {
            UserArray userArray = new UserArray(4);
            userArray.setUsers(new User[]{Mike, Peter, Dan, David});

            User firstRemovedUser = userArray.deleteUser(Mike.getId());
            User secondRemovedUser = userArray.deleteUser(Peter.getId());

            Assertions.assertEquals(2, userArray.size(), "Wrong number of the users in the UserArray. Expected 2, but got " + userArray.size() + " instead");
            Assertions.assertArrayEquals(new User[]{null, null, Dan, David}, userArray.getUsers(), "Wrong list of users");

            Assertions.assertSame(Mike, firstRemovedUser, "Wrong user returned");
            Assertions.assertSame(Peter, secondRemovedUser, "Wrong user returned");
        }

        @Test
        @DisplayName("Remove user who is not in the UserArray")
        void removeWrongUser() {
            UserArray userArray = new UserArray(4);
            userArray.setUsers(new User[]{Mike, Peter, Dan, David});
            User firstRemovedUser = userArray.deleteUser(Dan.getId());
            User secondRemovedUser = userArray.deleteUser(Emily.getId());

            Assertions.assertEquals(3, userArray.size(), "Wrong number of the users in the UserArray. Expected 3, but got " + userArray.size() + " instead");
            Assertions.assertArrayEquals(new User[]{Mike, Peter, null, David}, userArray.getUsers(), "Wrong list of users");

            Assertions.assertSame(Dan, firstRemovedUser, "Wrong user returned");
            Assertions.assertSame(null, secondRemovedUser, "Wrong returned value, expected null");
        }

        @Test
        @DisplayName("Remove user from empty array")
        void removeUserFromEmptyArray() {
            UserArray userArray = new UserArray(4);

            User removedUser = userArray.deleteUser(Mike.getId());
            Assertions.assertEquals(0, userArray.size(), "Wrong number of the users in the UserArray. Expected 0, but got " + userArray.size() + " instead");
            Assertions.assertArrayEquals(new User[4], userArray.getUsers(), "Wrong list of users");

            Assertions.assertSame(null, removedUser, "Wrong returned value, expected null");
        }
    }

    @Nested
    @DisplayName("Test size attribute with different conditions")
    class sizeAttributeTest{
        @Test
        @DisplayName("Test size attribute - number of saved users (with the setter - no empty spaces)")
        void sizeWithSetter() {
            UserArray userArray = new UserArray(5);
            userArray.setUsers(new User[]{Mike, Peter, Dan, David, Mike,});

            Assertions.assertEquals(5, userArray.size(), "Wrong number of the users in the UserArray. Expected 5, but got " + userArray.size() + " instead");
        }

        @Test
        @DisplayName("Test size attribute - number of saved users (with the setter)")
        void sizeWithSetterWithSpaces() {
            UserArray userArray = new UserArray(8);
            userArray.setUsers(new User[]{Mike, Peter, Dan, David, Mike, null, null, null});

            Assertions.assertEquals(5, userArray.size(), "Wrong number of the users in the UserArray. Expected 5, but got " + userArray.size() + " instead");
        }

        @Test
        @DisplayName("Test size attribute - empty array")
        void sizeEmptyArray() {
            UserArray userArray = new UserArray(4);
            Assertions.assertEquals(0, userArray.size(), "Wrong number of the users in the UserArray. Expected 0, but got " + userArray.size() + " instead");

            userArray.addUser(Mike);
            userArray.addUser(Peter);
            userArray.addUser(Susan);


            userArray.deleteUser(Mike.getId());
            userArray.deleteUser(Peter.getId());
            userArray.addUser(Dan);
            userArray.deleteUser(Susan.getId());
            userArray.deleteUser(Dan.getId());

            Assertions.assertEquals(0, userArray.size(), "Wrong number of the users in the UserArray. Expected 0, but got " + userArray.size() + " instead");
            Assertions.assertArrayEquals(new User[4], userArray.getUsers(), "Wrong list of users");
        }

        @Test
        @DisplayName("Test size attribute - array with empty spaces")
        void sizeArrayWithEmptySpaces() {
            UserArray userArray = new UserArray(4);
            Assertions.assertEquals(0, userArray.size(), "Wrong number of the users in the UserArray. Expected 0, but got " + userArray.size() + " instead");

            userArray.addUser(Mike);
            userArray.addUser(Peter);
            userArray.addUser(Susan);


            userArray.deleteUser(Mike.getId());
            userArray.deleteUser(Peter.getId());

            Assertions.assertEquals(1, userArray.size(), "Wrong number of the users in the UserArray. Expected 1, but got " + userArray.size() + " instead");
        }
    }
}
