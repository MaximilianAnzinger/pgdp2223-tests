package pgdp.messenger.behavior;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pgdp.messenger.PinguTalk;
import pgdp.messenger.Topic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static pgdp.messenger.behavior.TestUtils.getTestUser;

public class PinguTalkTest {


    long getCurrentUserId() throws NoSuchFieldException, IllegalAccessException {
        final var field = PinguTalk.class.getDeclaredField("userID");
        field.setAccessible(true);
        return field.getLong(null);

    }


    long getCurrentTopicId() throws NoSuchFieldException, IllegalAccessException {
        final var field = PinguTalk.class.getDeclaredField("topicID");
        field.setAccessible(true);
        return field.getLong(null);

    }

    @Test()
    void initializeArraysWithLengthOfAtLeastOne() {
        final var pinguTalk = new PinguTalk(0, 0);
        assertEquals(1, pinguTalk.getTopics().length);
        assertEquals(1, pinguTalk.getMembers().getUsers().length);
    }

    @Test
    void addMember() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var name = "Test";
        final var supervisor = getTestUser();
        pinguTalk.addMember(name, supervisor);
        assertEquals(name, pinguTalk.getMembers().getUsers()[0].getName());
    }

    @Test
    void addedMemberShouldHaveCurrentUserId() throws NoSuchFieldException, IllegalAccessException {
        final var pinguTalk = new PinguTalk(0, 0);
        final var name = "Test";
        final var supervisor = getTestUser();
        final var currentUserId = getCurrentUserId();
        pinguTalk.addMember(name, supervisor);
        assertEquals(currentUserId, pinguTalk.getMembers().getUsers()[0].getId());
    }

    @Test
    void addMemberShouldIncreaseUserId() throws NoSuchFieldException, IllegalAccessException {
        final var pinguTalk = new PinguTalk(0, 0);
        final var name = "Test";
        final var supervisor = getTestUser();
        final var currentUserId = getCurrentUserId();
        pinguTalk.addMember(name, supervisor);
        assertEquals(currentUserId + 1, getCurrentUserId());

    }

    @Test
    void addMemberShouldReturnAddedUser() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var name = "Test";
        final var supervisor = getTestUser();
        final var user = pinguTalk.addMember(name, supervisor);
        assertEquals(name, user.getName());
        assertEquals(supervisor, user.getSupervisor());
    }

    @Test
    void deleteMember() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var name = "Test";
        final var supervisor = getTestUser();
        final var addedMember = pinguTalk.addMember(name, supervisor);
        pinguTalk.deleteMember(addedMember.getId());
        assertEquals(0, pinguTalk.getMembers().size());
    }

    @Test
    void deleteMemberShouldReturnDeletedUser() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var name = "Test";
        final var supervisor = getTestUser();
        final var addedMember = pinguTalk.addMember(name, supervisor);
        final var deletedMember = pinguTalk.deleteMember(addedMember.getId());
        assertEquals(addedMember, deletedMember);
    }

    @Test
    void deleteMemberShouldReturnNullWhenUserDoesNotExist() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var deletedMember = pinguTalk.deleteMember(0);
        assertNull(deletedMember);
    }

    @Test()
    void createNewTopic() {
        final var pinguTalk = new PinguTalk(0, 1);
        final var topic = pinguTalk.createNewTopic("topic");
        assertEquals(1, pinguTalk.getTopics().length);
        Assertions.assertArrayEquals(new Topic[]{topic}, pinguTalk.getTopics());
    }

    @Test
    void createNewTopicShouldIncreaseTopicId() throws NoSuchFieldException, IllegalAccessException {
        final var pinguTalk = new PinguTalk(0, 1);
        final var currentTopicId = getCurrentTopicId();
        pinguTalk.createNewTopic("topic");
        assertEquals(currentTopicId + 1, getCurrentTopicId());
    }

    @Test
    void shouldReturnAddedTopic() {
        final var pinguTalk = new PinguTalk(0, 1);
        final var topic = pinguTalk.createNewTopic("topic");
        assertEquals(topic, pinguTalk.getTopics()[0]);
    }

    @Test
    void shouldReturnNullIfTopicsIsFull() {
        final var pinguTalk = new PinguTalk(0, 0);
        pinguTalk.createNewTopic("topic");
        final var topic = pinguTalk.createNewTopic("topic");
        assertNull(topic);
    }

    @Test
    void shouldNotAddTopicIfTopicsIsFull() {
        final var pinguTalk = new PinguTalk(0, 0);
        pinguTalk.createNewTopic("topic");
        pinguTalk.createNewTopic("topic");
        assertEquals(1, pinguTalk.getTopics().length);
    }

    @Test
    void shouldNotIncreaseTopicIdIfTopicsIsFull() throws NoSuchFieldException, IllegalAccessException {
        final var pinguTalk = new PinguTalk(0, 0);
        pinguTalk.createNewTopic("topic");
        final var currentTopicId = getCurrentTopicId();
        pinguTalk.createNewTopic("topic");
        assertEquals(currentTopicId, getCurrentTopicId());
    }

    @Test
    void shouldAddTopicAtFirstFreeSpace(){
        final var pinguTalk = new PinguTalk(0, 2);
        final var toBeDeletedTopic = pinguTalk.createNewTopic("topic");
        pinguTalk.createNewTopic("topic");
        pinguTalk.deleteTopic(toBeDeletedTopic.getId());
        final var topic =  pinguTalk.createNewTopic("topic");
        assertEquals(topic, pinguTalk.getTopics()[0]);
        assertEquals(2, pinguTalk.getTopics().length);
    }

    @Test()
    void deleteTopic() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var topic = pinguTalk.createNewTopic("topic");
        pinguTalk.deleteTopic(topic.getId());
        assertNull(pinguTalk.getTopics()[0]);
    }

    @Test()
    void deleteTopicShouldNotDecreaseArraySize() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var topic = pinguTalk.createNewTopic("topic");
        pinguTalk.deleteTopic(topic.getId());
      assertEquals(1, pinguTalk.getTopics().length);
    }

    @Test
    void deleteTopicShouldReturnDeletedTopic() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var topic = pinguTalk.createNewTopic("topic");
        final var deletedTopic = pinguTalk.deleteTopic(topic.getId());
        assertEquals(topic, deletedTopic);
    }

    @Test
    void deleteTopicShouldReturnNullWhenTopicDoesNotExist() {
        final var pinguTalk = new PinguTalk(0, 0);
        final var deletedTopic = pinguTalk.deleteTopic(0);
        assertNull(deletedTopic);
    }


}
