package pgdp.messenger.behavior;

import org.junit.jupiter.api.Test;
import pgdp.messenger.User;
import pgdp.messenger.UserArray;

import static org.junit.jupiter.api.Assertions.*;

public class UserArrayTest {
    @Test()
    void initializeWithLengthOfAtLeastOne() {
        final var userArray = new UserArray(0);
        assertEquals(1, userArray.getUsers().length);
    }

    @Test()
    void initialSizeShouldBeZero() {
        final var userArray = new UserArray(100);
        assertEquals(0, userArray.size());
    }

    @Test()
    void add() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        final var user1 = TestUtils.getTestUser();
        userArray.addUser(user1);
        final var expected = new User[100];
        expected[0] = user;
        expected[1] = user1;
        assertArrayEquals(expected, userArray.getUsers());
    }

    @Test()
    void stateShouldNotChangeWhenAddedUserIsNull() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        userArray.addUser(null);
        final var expected = new User[100];
        expected[0] = user;
        assertArrayEquals(expected, userArray.getUsers());
        assertEquals(1, userArray.size());
    }

    @Test()
    void shouldIncreaseSizeWhenUserIsAdded() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        assertEquals(1, userArray.size());
    }

    @Test()
    void shouldDoubleCapacity() {
        final var initialCapacity = 2;
        final var userArray = new UserArray(initialCapacity);
        final var amountOfUsersToAdd = initialCapacity + 1;
        for (int i = 0; i < amountOfUsersToAdd; i++) {
            userArray.addUser(TestUtils.getTestUser());
        }
        assertEquals(initialCapacity * 2, userArray.getUsers().length);
    }

    @Test()
    void shouldAddUserAtFirstFreeLocation() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        final var user1 = TestUtils.getTestUser();
        userArray.addUser(user1);
        userArray.deleteUser(user.getId());
        final var user2 = TestUtils.getTestUser();
        userArray.addUser(user2);
        final var expected = new User[100];
        expected[0] = user2;
        expected[1] = user1;
        assertArrayEquals(expected, userArray.getUsers());
    }


    @Test()
    void delete() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        final var user1 = TestUtils.getTestUser();
        userArray.addUser(user1);
        userArray.deleteUser(user.getId());
        final var expected = new User[100];
        // User should be at index 1 because of this quote from the exercise: "Die dabei entstandene Lücke darf bleiben."
        expected[1] = user1;
        assertArrayEquals(expected, userArray.getUsers());
    }

    @Test()
    void deleteShouldDecreaseSize() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        userArray.deleteUser(user.getId());
        assertEquals(0, userArray.size());
    }

    @Test
    void deleteShouldNotChangeSizeWhenUserDoesNotExist() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        userArray.deleteUser(2);
        assertEquals(1, userArray.size());
    }

    @Test
    void deleteShouldReturnDeletedUser() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        final var deletedUser = userArray.deleteUser(user.getId());
        assertEquals(user, deletedUser);
    }

    @Test
    void deleteShouldNotReturnDeletedUserWhenTheyDoNotExist() {
        final var userArray = new UserArray(100);
        final var user = TestUtils.getTestUser();
        userArray.addUser(user);
        final var deletedUser = userArray.deleteUser(2);
        assertNull(deletedUser);
    }
}

