package pgdp;

import org.junit.jupiter.api.*;
import pgdp.pingunetwork.*;

import java.util.Arrays;

public class BehaviorTest {

    private User defaultUser;
    private Picture defaultPicture;
    private Picture defaultPicture2;
    private Post defaultPost;
    private Interaction defaultInteraction;
    private Group defaultGroup;

    private final int[][] pictureWithData1 = new int[][] {
            new int[]{1, 2, 3, 5, 8, 0, 8},
            new int[]{2, 5, 9, 3, 0, 8, 1},
            new int[]{4, 6, 9, 6, 2, 4, 5},
            new int[]{7, 7, 1, 8, 3, 4, 6},
            new int[]{1, 3, 4, 9, 2, 0, 7}
    };
    private final int[][] pictureWithData2 = new int[][] {
            new int[]{3, 4, 9, 10},
            new int[]{8, 1, 8, 0},
            new int[]{2, 1, 5, 1},
            new int[]{6, 4, 7, 3},
            new int[]{3, 4, 3, 5}
    };
    private final int[][] picture1 = new int[2][4];
    private final int[][] picture2 = new int[5][8];
    private final int[][] picture3 = new int[1][0];
    private final int[][] picture4 = new int[0][0];

    @BeforeEach
    @DisplayName("Initialization of the default instances")
    public void setup() {
        defaultPicture = new Picture("Maledives", pictureWithData1);
        defaultPicture2 = new Picture("Abu Dhabi", pictureWithData2);
        defaultUser = new User("Bob", "Just some random text", defaultPicture);
        defaultPost = new Post("Random post", "Random content for the post");
        defaultGroup = new Group("Formula 1 Fans", "Can't wait for the preseason testing", defaultUser, defaultPicture2);
        defaultInteraction = new Interaction(defaultUser, 1);
    }

    @Nested
    @DisplayName("Test instance creating of each class")
    class initialization {

        @Test
        @DisplayName("Should create picture instance")
        void shouldCreatePicture() {
            Assertions.assertEquals("Maledives", defaultPicture.getLocation());
            Assertions.assertArrayEquals(pictureWithData1, defaultPicture.getData());
            Assertions.assertEquals(pictureWithData1.length, defaultPicture.getHeight());
            Assertions.assertEquals(pictureWithData1[0].length, defaultPicture.getWidth());
        }

        @Test
        @DisplayName("Should create user instance")
        void shouldCreateUser() {
            Assertions.assertEquals("Bob", defaultUser.getName());
            Assertions.assertEquals("Just some random text", defaultUser.getDescription());
            Assertions.assertSame(defaultPicture, defaultUser.getProfilePicture());
            Assertions.assertEquals(0, defaultUser.getFriends().length);
            Assertions.assertEquals(0, defaultUser.getPosts().length);
            Assertions.assertEquals(0, defaultUser.getGroups().length);
        }


        @Test
        @DisplayName("Should create post instance")
        void shouldCreatePost() {
            Assertions.assertEquals("Random post", defaultPost.getTitle());
            Assertions.assertEquals("Random content for the post", defaultPost.getContent());
            Assertions.assertEquals(0, defaultPost.getComments().length);
            Assertions.assertEquals(0, defaultPost.getInteractions().length);
        }


        @Test
        @DisplayName("Should create interaction instance")
        void shouldCreateInteraction() {
            Assertions.assertSame(defaultUser, defaultInteraction.getUser());
            Assertions.assertEquals(1, defaultInteraction.getInteractionType());
        }

        @Test
        @DisplayName("Should create group instance")
        void shouldCreateGroup() {
            Assertions.assertEquals("Formula 1 Fans", defaultGroup.getName());
            Assertions.assertEquals("Can't wait for the preseason testing", defaultGroup.getDescription());
            Assertions.assertSame(defaultUser, defaultGroup.getOwner());
            Assertions.assertEquals(1, defaultGroup.getMembers().length);
            Assertions.assertSame(defaultUser, defaultGroup.getMembers()[0]);
            Assertions.assertSame(defaultPicture2, defaultGroup.getPicture());
        }
    }


    @Nested
    @DisplayName("Behaviour tests of the User class")
    class userBehaviourTests {
        @Test
        @DisplayName("User create posts")
        void userCreatesPost() {
            // Creates just one post
            defaultUser.post("My First Post", "Just some random text");
            Assertions.assertEquals(1, defaultUser.getPosts().length);
            Assertions.assertEquals("My First Post", defaultUser.getPosts()[0].getTitle());

            defaultUser.post("My Second Post", "Just some random text");
            defaultUser.post("My Third Post", "Just some random text");

            Assertions.assertEquals(3, defaultUser.getPosts().length);
            Assertions.assertEquals("My Second Post", defaultUser.getPosts()[1].getTitle());
            Assertions.assertEquals("My Third Post", defaultUser.getPosts()[2].getTitle());
        }

        @Test
        @DisplayName("User creates a post and comments it")
        void userCreatesPostAndComments() {
            defaultUser.post("My post", "My post");
            Post usersPost = defaultUser.getPosts()[0];
            defaultUser.comment(usersPost, "My Comment", "My comment");

            Assertions.assertEquals(2, defaultUser.getPosts().length);
            Assertions.assertEquals(1, usersPost.getComments().length);
            Assertions.assertSame(defaultUser.getPosts()[1], usersPost.getComments()[0]);
        }

        @Test
        @DisplayName("User comments a post")
        void userCreatesComment() {
            defaultUser.comment(defaultPost, "My first comment", "My first comment");
            Assertions.assertEquals(1, defaultUser.getPosts().length);
            Assertions.assertEquals(1, defaultPost.getComments().length);
            Assertions.assertSame(defaultUser.getPosts()[0], defaultPost.getComments()[0]);
            Assertions.assertEquals("My first comment", defaultPost.getComments()[0].getTitle());
        }

        @Test
        @DisplayName("Multiple users comment multiple posts")
        void usersCreateComments() {
            Post firstPost = new Post("First post", "First post content");
            Post secondPost = new Post("Second post", "Second post content");
            Post thirdPost = new Post("Third post", "Third post content");

            User user1 = new User("David", "Just David", new Picture("Germany", picture1));
            User user2 = new User("Martin", "Just Martin", new Picture("Croatia", picture2));
            User user3 = new User("Peter", "Just Peter", new Picture("Argentina", picture3));

            // user1 and user3 comment first post
            user1.comment(firstPost, "Post", "Post");
            user3.comment(firstPost, "Post3", "Post3");
            Assertions.assertEquals(2, firstPost.getComments().length);
            Assertions.assertSame(user1.getPosts()[0], firstPost.getComments()[0]);
            Assertions.assertSame(user3.getPosts()[0], firstPost.getComments()[1]);


            // user3 comments second post
            user3.comment(secondPost, "Post2", "Post2");
            Assertions.assertEquals(1, secondPost.getComments().length);
            Assertions.assertSame(user3.getPosts()[1], secondPost.getComments()[0]);

            // user1, user2(comments two times) and user3 comment thirdPost
            user1.comment(thirdPost, "Post", "Post");
            user2.comment(thirdPost, "Post", "Post");
            user2.comment(thirdPost, "Post another", "Post another");
            user3.comment(thirdPost, "Post", "Post");
            Assertions.assertEquals(4, thirdPost.getComments().length);

            Assertions.assertEquals(2, user1.getPosts().length);
            Assertions.assertEquals(2, user2.getPosts().length);
            Assertions.assertEquals(3, user3.getPosts().length);
            Assertions.assertSame(user1.getPosts()[1], thirdPost.getComments()[0]);
            Assertions.assertSame(user2.getPosts()[0], thirdPost.getComments()[1]);
            Assertions.assertSame(user2.getPosts()[1], thirdPost.getComments()[2]);
            Assertions.assertSame(user3.getPosts()[2], thirdPost.getComments()[3]);
        }

        @Test
        @DisplayName("User interacts with a post")
        void userInteraction() {
            defaultUser.interact(defaultPost, 1);
            Assertions.assertEquals(1, defaultPost.getInteractions().length);
            Assertions.assertEquals(1, defaultPost.getInteractions()[0].getInteractionType());
            Assertions.assertSame(defaultUser, defaultPost.getInteractions()[0].getUser());
        }

        @Test
        @DisplayName("Users interact with a post")
        void usersInteractions() {
            User user1 = new User("David", "Just David", new Picture("Germany", picture1));
            User user2 = new User("Martin", "Just Martin", new Picture("Croatia", picture2));
            User user3 = new User("Peter", "Just Peter", new Picture("Argentina", picture3));

            user1.interact(defaultPost, 2);
            user2.interact(defaultPost, 1);
            user3.interact(defaultPost, 2);

            Assertions.assertEquals(3, defaultPost.getInteractions().length);
            Assertions.assertEquals(2, defaultPost.getInteractions()[0].getInteractionType());
            Assertions.assertSame(user1, defaultPost.getInteractions()[0].getUser());
            Assertions.assertEquals(1, defaultPost.getInteractions()[1].getInteractionType());
            Assertions.assertSame(user2, defaultPost.getInteractions()[1].getUser());
            Assertions.assertEquals(2, defaultPost.getInteractions()[2].getInteractionType());
            Assertions.assertSame(user3, defaultPost.getInteractions()[2].getUser());
        }
    }

    @Nested
    @DisplayName("Behaviour tests for the group class")
    class groupBehaviourTests {
        @Test
        @DisplayName("Removes member from the group")
        void removeMemberOfTheGroup() {
            User user1 = new User("David", "Just David", new Picture("Germany", picture1));
            User user2 = new User("Martin", "Just Martin", new Picture("Croatia", picture2));
            User user3 = new User("Peter", "Just Peter", new Picture("Argentina", picture3));

            defaultGroup.addUser(user1);
            defaultGroup.addUser(user2);
            defaultGroup.addUser(user3);

            Assertions.assertEquals(4, defaultGroup.getMembers().length);
            defaultGroup.removeUser(user2);
            Assertions.assertEquals(3, defaultGroup.getMembers().length);
            Assertions.assertFalse(Arrays.stream(defaultGroup.getMembers()).anyMatch(user -> user == user2));
            defaultGroup.removeUser(user3);
            Assertions.assertEquals(2, defaultGroup.getMembers().length);
            Assertions.assertFalse(Arrays.stream(defaultGroup.getMembers()).anyMatch(user -> user == user3));
        }

        @Test
        @DisplayName("Removes the owner from the group, but the group is not empty afterwards")
        void removeOwnerNotEmpty() {
            User user1 = new User("David", "Just David", new Picture("Germany", picture1));
            User user2 = new User("Martin", "Just Martin", new Picture("Croatia", picture2));
            User user3 = new User("Peter", "Just Peter", new Picture("Argentina", picture3));
            User user4 = new User("Ahmed", "Just Ahmed", new Picture("USA", picture4));

            Group group = new Group("Late night birds", "Hard to fall asleep", user3, defaultPicture);
            group.addUser(user1);
            group.addUser(user2);
            group.addUser(user4);

            Assertions.assertEquals(4, group.getMembers().length);
            group.removeUser(user3);
            Assertions.assertEquals(3, group.getMembers().length);
            Assertions.assertFalse(Arrays.stream(group.getMembers()).anyMatch(user -> user == user3));
            Assertions.assertSame(user1, group.getOwner());

            group.addUser(user3);
            group.setOwner(user4);
            Assertions.assertSame(user4, group.getOwner());
            group.removeUser(user4);
            Assertions.assertEquals(3, group.getMembers().length);
            Assertions.assertFalse(Arrays.stream(group.getMembers()).anyMatch(user -> user == user4));
            Assertions.assertSame(user1, group.getOwner());
        }

        @Test
        @DisplayName("Removes the owner and the group is empty afterwards")
        void removesOwnerEmpty() {
            defaultGroup.removeUser(defaultUser);
            Assertions.assertEquals(0, defaultGroup.getMembers().length);
            Assertions.assertSame(null, defaultGroup.getOwner());
        }

        @Test
        @DisplayName("Remove a user that is not a member of the group")
        void notMember() {
            User user1 = new User("David", "Just David", new Picture("Germany", picture1));
            User[] beforeRemove = defaultGroup.getMembers();
            defaultGroup.removeUser(user1);

            Assertions.assertEquals(1, defaultGroup.getMembers().length);
            Assertions.assertArrayEquals(beforeRemove, defaultGroup.getMembers());
        }
    }

    @Nested
    @DisplayName("Picture class tests")
    class pictureBehaviourTests {
        @Test
        @DisplayName("Set thumbnails")
        void setThumbnails() {
            Assertions.assertArrayEquals(new Picture[0], defaultPicture.getThumbnails());
            Picture[] thumbnails = new Picture[]{new Picture("New York", picture1), new Picture("Toronto", picture2)};
            defaultPicture.setThumbnails(thumbnails);
            Assertions.assertArrayEquals(thumbnails, defaultPicture.getThumbnails());
        }

        @Test
        @DisplayName("Create Picture with the data parameter as empty array")
        void createPictureWithEmptyArrayData() {
            Picture pictureEmptyArray1 = new Picture("Munich", new int[0][0]);
            Picture pictureEmptyArray2 = new Picture("Berlin", new int[0][1]);

            Assertions.assertArrayEquals(new int[0][0], pictureEmptyArray1.getData());
            Assertions.assertEquals(0, pictureEmptyArray1.getWidth());
            Assertions.assertEquals(0, pictureEmptyArray1.getHeight());

            Assertions.assertArrayEquals(new int[0][1], pictureEmptyArray2.getData());
            Assertions.assertEquals(0, pictureEmptyArray2.getWidth());
            Assertions.assertEquals(0, pictureEmptyArray2.getHeight());
        }
    }
}
