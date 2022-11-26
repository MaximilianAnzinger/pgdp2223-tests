package pgdp.pingunetwork.behavioral;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Import everything from package pgdp.pingunetwork
import pgdp.pingunetwork.*;

public class PinguNetworkTest {

    private Picture pic1 = new Picture("abc", new int[5][4]);
    private Picture pic2 = new Picture("testest", new int[0][4]);
    private Picture pic3 = new Picture("", new int[][]{{0, 0, 0}, {0, 1, 1}, {0, -6, 3}});
    private Picture pic4 = new Picture("3", new int[7][0]);

    /**
     * Vertauscht expected und actual, damit die Fehler richtig angezeigt werden
     * !!!!!!!!!!!!!!!!!!! assertEquals(actual, expected) !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param actual
     * @param expected
     */
    public static void assertEquals(Object actual, Object expected){
        Assertions.assertEquals(expected, actual);
    }

    public static void assertArrayEquals(Object[] actual, Object[] expected){
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("User - Interact with own post")
    public void testUser2() {
        User bob = new User("Bob", "i code stuff", pic1);
        bob.post("Rainy day", "2cm of water already");
        Post bobs_post = bob.getPosts()[0];

        bob.interact(bobs_post, 2);

        assertEquals(bob.getPosts()[0].getInteractions().length, 1);
        assertEquals(bob.getPosts()[0].getInteractions()[0].getUser(), bob);
        assertEquals(bob.getPosts()[0].getInteractions()[0].getInteractionType(), 2);
    }

    @Test
    @DisplayName("User - Multiple interactions from other users")
    public void testUser3() {
        User bob = new User("Bob", "i code stuff", pic1);
        User alice = new User("Alice", "lol", pic2);
        User joe = new User("Joe", "jell-o is tasty", pic2);

        bob.post("Rainy day", "2cm of water already");
        Post bobs_post = bob.getPosts()[0];

        alice.interact(bobs_post, 4);
        joe.interact(bobs_post, 5);

        assertEquals(bob.getPosts()[0].getInteractions().length, 2);
        assertEquals(bob.getPosts()[0].getInteractions()[0].getUser(), alice);
        assertEquals(bob.getPosts()[0].getInteractions()[0].getInteractionType(), 4);
        assertEquals(bob.getPosts()[0].getInteractions()[1].getUser(), joe);
        assertEquals(bob.getPosts()[0].getInteractions()[1].getInteractionType(), 5);
    }

    @Test
    @DisplayName("User - Post")
    public void testUser4() {
        User bob = new User("Bob", "i code stuff", pic4);
        assertEquals(bob.getPosts().length, 0);

        bob.post("sunday vibes", "pgdp again :pain:");

        assertEquals(bob.getPosts().length, 1);
        assertEquals(bob.getPosts()[0].getTitle(), "sunday vibes");
        assertEquals(bob.getPosts()[0].getContent(), "pgdp again :pain:");
        assertArrayEquals(bob.getPosts()[0].getComments(), new Post[0]);
        assertArrayEquals(bob.getPosts()[0].getInteractions(), new Interaction[0]);
    }

    @Test
    @DisplayName("User - Post two times")
    public void testUser5() {
        User bob = new User("Bob", "i code stuff", pic2);
        assertEquals(bob.getPosts().length, 0);

        bob.post("sunday vibes", "pgdp again :pain:");
        bob.post("monday feeling", "era again");

        assertEquals(bob.getPosts().length, 2);
        assertEquals(bob.getPosts()[0].getTitle(), "sunday vibes");
        assertEquals(bob.getPosts()[0].getContent(), "pgdp again :pain:");
        assertArrayEquals(bob.getPosts()[0].getComments(), new Post[0]);
        assertArrayEquals(bob.getPosts()[0].getInteractions(), new Interaction[0]);
        assertEquals(bob.getPosts()[1].getTitle(), "monday feeling");
        assertEquals(bob.getPosts()[1].getContent(), "era again");
        assertArrayEquals(bob.getPosts()[1].getComments(), new Post[0]);
        assertArrayEquals(bob.getPosts()[1].getInteractions(), new Interaction[0]);
    }

    @Test
    @DisplayName("User - Comment on own post")
    public void testUser6() {
        User bob = new User("Bob", "i code stuff", pic3);
        assertEquals(bob.getPosts().length, 0);

        bob.post("sunday vibes", "pgdp again :pain:");
        Post bobs_post = bob.getPosts()[0];

        bob.comment(bobs_post, "jk", "i love pgdp");

        assertEquals(bobs_post.getComments().length, 1);
        assertEquals(bobs_post.getComments()[0].getTitle(), "jk");
        assertEquals(bobs_post.getComments()[0].getContent(), "i love pgdp");
        assertArrayEquals(bobs_post.getComments()[0].getComments(), new Post[0]);
        assertArrayEquals(bobs_post.getComments()[0].getInteractions(), new Interaction[0]);

        assertEquals(bob.getPosts().length, 2);
        assertEquals(bob.getPosts()[1], bob.getPosts()[0].getComments()[0]);
    }

    @Test
    @DisplayName("User - Comment on other user's post")
    public void testUser7() {
        User bob = new User("Bob", "i code stuff", pic4);
        User alice = new User("Alice", "lol", pic3);
        assertEquals(bob.getPosts().length, 0);

        bob.post("sunday vibes", "pgdp again :pain:");
        Post bobs_post = bob.getPosts()[0];

        alice.comment(bobs_post, "fancy pic", "what are weekends?");

        assertEquals(bobs_post.getComments().length, 1);
        assertEquals(alice.getPosts().length, 1);
        assertEquals(alice.getPosts()[0], bob.getPosts()[0].getComments()[0]);
    }


    // TEST PICTURE

    @Test
    @DisplayName("Picture - Create")
    void testPicture1() {
        int[][] picData = {
                {0, 0, 1},
                {1, 2, 7},
                {44444, 83718, 819823738},
                {45612, 0, -3}
        };
        Picture pic = new Picture("Toronto", picData);

        assertEquals("Toronto", pic.getLocation());
        assertArrayEquals(pic.getData(), picData);
        assertEquals(pic.getHeight(), 4);
        assertEquals(pic.getWidth(), 3);
        assertArrayEquals(pic.getThumbnails(), new Picture[0]);
    }

    @Test
    @DisplayName("Picture - Use in User")
    public void testPicture2() {
        User bob = new User("Bob", "i code stuff", new Picture("Bayern", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        assert (bob.getProfilePicture().getLocation().equals("Bayern"));
        assertArrayEquals(bob.getProfilePicture().getData(), new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
    }

    @Test
    @DisplayName("Picture - Use in Group")
    public void testPicture3() {
        User bob = new User("Bob", "i code stuff", pic1);
        Group bob_gang = new Group("zulip-chat", "more recycling symbols than on most bottles", bob, new Picture("Bayern", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));

        assertEquals(bob_gang.getPicture().getLocation(), "Bayern");
        assertArrayEquals(bob_gang.getPicture().getData(), new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
    }

    @Test
    @DisplayName("Picture - Set Thumbnails")
    public void testPicture4() {
        Picture pic = new Picture("The Länd", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}});
        assertArrayEquals(pic.getThumbnails(), new Picture[0]);

        Picture[] thumbnails = new Picture[]{
                new Picture("münchner freiheit", new int[][]{{0, 0, 0}, {0, 4, 5}, {1, 0, 0}}),
                new Picture("entenhausen", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}})
        };
        pic.setThumbnails(thumbnails);

        assertArrayEquals(pic.getThumbnails(), thumbnails);
    }

    @Test
    @DisplayName("Picture - width is 0")
    public void testPicture5() {
        Picture pic = new Picture("mnb", new int[5][0]);
        assertEquals(pic.getWidth(), 0);
        assertEquals(pic.getHeight(), 5);
        assertTrue(Arrays.deepEquals(new int[5][0], pic.getData()));
    }

    @Test
    @DisplayName("Picture - height is 0")
    public void testPicture6() {
        Picture pic = new Picture("mnb", new int[0][7]);
        assertEquals(pic.getWidth(), 0);
        assertEquals(pic.getHeight(), 0);
        assertTrue(Arrays.deepEquals(new int[0][7], pic.getData()));
    }


    //TEST INTERACTION

    @Test
    @DisplayName("InteractionTest")
    void testInteraction1() {
        User bob = new User("Bob", "i code stuff", pic3);
        Interaction interaction = new Interaction(bob, 4);

        assertEquals(interaction.getUser(), bob);
        assertEquals(interaction.getInteractionType(), 4);
    }


    //TEST GROUP

    @Test
    @DisplayName("Group - remove user (not owner)")
    void testGroup1() {
        User bob = new User("Bob", "i code stuff", pic1);
        Group bob_gang = new Group("zulip-chat", "more recycling symbols than on most bottles", bob, new Picture("Bayern", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        User alice = new User("Alice", "lol", pic3);

        bob_gang.addUser(alice);
        assertArrayEquals(bob_gang.getMembers(), new User[]{bob, alice});
        assertEquals(bob_gang.getOwner(), bob);

        bob_gang.removeUser(alice);
        assertArrayEquals(bob_gang.getMembers(), new User[]{bob});
        assertEquals(bob_gang.getOwner(), bob);
    }

    @Test
    @DisplayName("Group - remove owner, members not empty afterwards")
    void testGroup2() {
        User bob = new User("Bob", "i code stuff", pic2);
        Group bob_gang = new Group("zulip-chat", "more recycling symbols than on most bottles", bob, new Picture("Bayern", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        User alice = new User("Alice", "lol", pic4);

        bob_gang.addUser(alice);
        assertArrayEquals(bob_gang.getMembers(), new User[]{bob, alice});
        assertEquals(bob_gang.getOwner(), bob);

        bob_gang.removeUser(bob);
        assertArrayEquals(bob_gang.getMembers(), new User[]{alice});
        assertEquals(bob_gang.getOwner(), alice);
    }

    @Test
    @DisplayName("Group - remove owner, members empty afterwards")
    void testGroup3() {
        User bob = new User("Bob", "i code stuff", pic1);
        Group bob_gang = new Group("zulip-chat", "more recycling symbols than on most bottles", bob, new Picture("Bayern", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));

        bob_gang.removeUser(bob);
        assertArrayEquals(bob_gang.getMembers(), new User[]{});
        assertNull(bob_gang.getOwner());
    }

    @Test
    @DisplayName("Group - remove nonexistent user")
    void testGroup4() {
        User bob = new User("Bob", "i code stuff", pic4);
        Group bob_gang = new Group("zulip-chat", "more recycling symbols than on most bottles", bob, new Picture("Bayern", new int[][]{{0, 0, 0}, {0, 1, 0}, {0, 0, 0}}));
        User alice = new User("Alice", "lol", pic2);
        User joe = new User("Joe", "jell-o is tasty", pic4);

        bob_gang.addUser(alice);

        bob_gang.removeUser(joe);
        assertArrayEquals(bob_gang.getMembers(), new User[]{bob, alice});
        assertEquals(bob_gang.getOwner(), bob);
    }

    @Test
    @DisplayName("Simulated interactions - 1")
    void simulateInteraction() {
        User alice = new User("Alice", "quirky girl", new Picture("Toronto", new int[][]{{1, 1, 1}, {1, 2, 3}, {1, 1, 1}}));
        User bob = new User("Bob", "nerdy stuff is my hobby", new Picture("Land des Bieres", new int[][]{{4, 5, 6}, {0, 0, 4}, {7, 8, 9}}));

        assertEquals(alice.getPosts().length, 0);

        alice.post("been to berlin", "was in berlin last weekend");
        assertEquals(alice.getPosts().length, 1);

        Post aliceFirstPost = alice.getPosts()[0];

        alice.comment(aliceFirstPost, "what's ur fav hotdog spot?", "gimme some inspo. comment down below!");
        assertEquals(alice.getPosts()[0].getComments().length, 1);
        assertEquals(alice.getPosts().length, 2);
        assertEquals(alice.getPosts()[1], alice.getPosts()[0].getComments()[0]);

        bob.comment(aliceFirstPost, "i like hotcorn.", "fancy you were in berlin.");
        assertEquals(alice.getPosts()[0].getComments().length, 2);
        assertEquals(bob.getPosts().length, 1);
        assertEquals(bob.getPosts()[0], alice.getPosts()[0].getComments()[1]);
    }
}

