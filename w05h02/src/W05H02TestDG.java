package pgdp.pingunetwork;

import org.junit.jupiter.api.*;

public class W05H02TestDG {

    // TESTS USER

    @Test
    @DisplayName("Test User Konstruktor, getter, setter")
    public void Test01(){
        User user = new User("user01", "USER01");
        Assertions.assertEquals("user01", user.getName());
        Assertions.assertEquals("USER01", user.getDescription());
        user.setName("userr01");
        user.setDescription("USERR01");
        Assertions.assertEquals("userr01", user.getName());
        Assertions.assertEquals("USERR01", user.getDescription());
        Assertions.assertEquals(0, user.getFriends().length);
        Assertions.assertEquals(0, user.getGroups().length);
        Assertions.assertEquals(0, user.getPosts().length);
        Assertions.assertEquals(null, user.getProfilePicture());
        Picture profilePicture = new Picture("ort1", new int[][]{{1,1}});
        user.setProfilePicture(profilePicture);
        Assertions.assertEquals(profilePicture, user.getProfilePicture());
    }

    //unnötig: ist schon vorgegeben also Test02 eigentlich immer passed
    @Test
    @DisplayName("Test User addFriend, removeFriend")
    public void Test02(){
        User user = new User("user01", "description01");
        User[] friends = new User[10];
        for(int i = 0; i < friends.length; i++){
            friends[i] = new User("friend01", "frienddes01");
            user.addFriend(friends[i]);
        }
        Assertions.assertEquals(friends.length, user.getFriends().length);
        for(int i = 0; i < friends.length; i++){
            Assertions.assertEquals(friends[i], user.getFriends()[i]);
        };
        // versuche selbe friend nochmal einzufügne
        user.addFriend(friends[0]);
        Assertions.assertEquals(friends.length, user.getFriends().length);
        for(int i = 0; i < friends.length; i++){
            Assertions.assertEquals(friends[i], user.getFriends()[i]);
        };
    }

    @Test
    @DisplayName("Test User interact zwei interactions bei einem post")
    public void Test03(){
        User user = new User("user01", "description01");
        Post post = new Post("post01", "content01");
        user.interact(post, 5);
        Assertions.assertEquals(1, post.getInteractions().length);
        Assertions.assertEquals(5, post.getInteractions()[0].getInteractionType());
        user.interact(post, 6);
        Assertions.assertEquals(2, post.getInteractions().length);
        Assertions.assertEquals(6, post.getInteractions()[1].getInteractionType());
    }

    @Test
    @DisplayName("Test User interact mit null post")
    public void Test04(){
        User user = new User("user01", "description01");
        try{
            user.interact(null, 5);
        }
        catch(Exception e){
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Test User post")
    public void Test05(){
        User user = new User("user01", "description01");
        Assertions.assertEquals(0, user.getPosts().length);
        user.post("post01", "content01");
        Assertions.assertEquals(1, user.getPosts().length);
        user.post("post02", "content02");
        Assertions.assertEquals(2, user.getPosts().length);
        user.post("post03", "content03");
        Assertions.assertEquals(3, user.getPosts().length);
        user.post("post04", "content04");
        Assertions.assertEquals(4, user.getPosts().length);
        Assertions.assertEquals("post01", user.getPosts()[0].getTitle());
        Assertions.assertEquals("post02", user.getPosts()[1].getTitle());
        Assertions.assertEquals("post03", user.getPosts()[2].getTitle());
        Assertions.assertEquals("post04", user.getPosts()[3].getTitle());
    }

    @Test
    @DisplayName("Test User comment zwei comments mit einem post")
    public void Test06(){
        User user = new User("user01", "description01");
        Post post = new Post("post", "content");
        user.comment(post, "post01", "content01");
        Assertions.assertEquals(1, user.getPosts().length);
        Assertions.assertEquals("post01", user.getPosts()[0].getTitle());
        Assertions.assertEquals(1, post.getComments().length);
        Assertions.assertEquals("post01", post.getComments()[0].getTitle());

        user.comment(post, "post02", "content02");
        Assertions.assertEquals(2, user.getPosts().length);
        Assertions.assertEquals("post02", user.getPosts()[1].getTitle());
        Assertions.assertEquals(2, post.getComments().length);
        Assertions.assertEquals("post02", post.getComments()[1].getTitle());
    }

    @Test // zulip frage: wie soll sich comment bei post == null verhalten
    @DisplayName("Test User comment mit null post")
    public void Test07(){
        User user = new User("user01", "description01");
        user.comment(null, "post01", "content01");
        Assertions.assertEquals(1, user.getPosts().length);
        Assertions.assertEquals("post01", user.getPosts()[0].getTitle());

        Post post = new Post("post", "content");
        user.comment(post, "post02", "content02");
        Assertions.assertEquals(2, user.getPosts().length);
        Assertions.assertEquals("post01", user.getPosts()[0].getTitle());
        Assertions.assertEquals("post02", user.getPosts()[1].getTitle());
        Assertions.assertEquals(1, post.getComments().length);
        Assertions.assertEquals("post02", post.getComments()[0].getTitle());

        user.comment(null, "post03", "content03");
        Assertions.assertEquals(3, user.getPosts().length);
        Assertions.assertEquals("post01", user.getPosts()[0].getTitle());
        Assertions.assertEquals("post02", user.getPosts()[1].getTitle());
        Assertions.assertEquals("post03", user.getPosts()[2].getTitle());
        Assertions.assertEquals(1, post.getComments().length);
        Assertions.assertEquals("post02", post.getComments()[0].getTitle());
    }


    // TESTS GROUP


    @Test
    @DisplayName("Test Group removeUser")
    public void Test08(){
        User owner = new User("owner", "descriptionOwner");
        Group group = new Group("group01", "descriptionGroup", owner);
        Assertions.assertEquals(1, group.getMembers().length);
        User[] users = new User[10];
        for(int i = 0; i < users.length; i++){
            users[i] = new User("user0"+i, "description0"+i);
            group.addUser(users[i]);
        }
        Assertions.assertEquals(11, group.getMembers().length);

        group.removeUser(users[0]);
        Assertions.assertEquals(10, group.getMembers().length);
        Assertions.assertEquals(owner, group.getMembers()[0]);
        Assertions.assertEquals(users[1], group.getMembers()[1]);
        Assertions.assertEquals(users[2], group.getMembers()[2]);
        Assertions.assertEquals(users[3], group.getMembers()[3]);
        Assertions.assertEquals(users[4], group.getMembers()[4]);
        Assertions.assertEquals(users[5], group.getMembers()[5]);
        Assertions.assertEquals(users[6], group.getMembers()[6]);
        Assertions.assertEquals(users[7], group.getMembers()[7]);
        Assertions.assertEquals(users[8], group.getMembers()[8]);
        Assertions.assertEquals(users[9], group.getMembers()[9]);
        Assertions.assertEquals(owner, group.getOwner());

        group.removeUser(users[9]);
        Assertions.assertEquals(9, group.getMembers().length);
        Assertions.assertEquals(owner, group.getMembers()[0]);
        Assertions.assertEquals(users[1], group.getMembers()[1]);
        Assertions.assertEquals(users[2], group.getMembers()[2]);
        Assertions.assertEquals(users[3], group.getMembers()[3]);
        Assertions.assertEquals(users[4], group.getMembers()[4]);
        Assertions.assertEquals(users[5], group.getMembers()[5]);
        Assertions.assertEquals(users[6], group.getMembers()[6]);
        Assertions.assertEquals(users[7], group.getMembers()[7]);
        Assertions.assertEquals(users[8], group.getMembers()[8]);
        Assertions.assertEquals(owner, group.getOwner());

        group.removeUser(users[8]);
        Assertions.assertEquals(8, group.getMembers().length);
        Assertions.assertEquals(owner, group.getMembers()[0]);
        Assertions.assertEquals(users[1], group.getMembers()[1]);
        Assertions.assertEquals(users[2], group.getMembers()[2]);
        Assertions.assertEquals(users[3], group.getMembers()[3]);
        Assertions.assertEquals(users[4], group.getMembers()[4]);
        Assertions.assertEquals(users[5], group.getMembers()[5]);
        Assertions.assertEquals(users[6], group.getMembers()[6]);
        Assertions.assertEquals(users[7], group.getMembers()[7]);
        Assertions.assertEquals(owner, group.getOwner());

        group.removeUser(users[1]);
        Assertions.assertEquals(7, group.getMembers().length);
        Assertions.assertEquals(owner, group.getMembers()[0]);
        Assertions.assertEquals(users[2], group.getMembers()[1]);
        Assertions.assertEquals(users[3], group.getMembers()[2]);
        Assertions.assertEquals(users[4], group.getMembers()[3]);
        Assertions.assertEquals(users[5], group.getMembers()[4]);
        Assertions.assertEquals(users[6], group.getMembers()[5]);
        Assertions.assertEquals(users[7], group.getMembers()[6]);
        Assertions.assertEquals(owner, group.getOwner());

        //remove den owner
        group.removeUser(owner);
        Assertions.assertEquals(6, group.getMembers().length);
        Assertions.assertEquals(users[2], group.getMembers()[0]);
        Assertions.assertEquals(users[3], group.getMembers()[1]);
        Assertions.assertEquals(users[4], group.getMembers()[2]);
        Assertions.assertEquals(users[5], group.getMembers()[3]);
        Assertions.assertEquals(users[6], group.getMembers()[4]);
        Assertions.assertEquals(users[7], group.getMembers()[5]);
        Assertions.assertEquals(users[2], group.getOwner());

        group.removeUser(group.getOwner());
        Assertions.assertEquals(5, group.getMembers().length);
        Assertions.assertEquals(users[3], group.getMembers()[0]);
        Assertions.assertEquals(users[4], group.getMembers()[1]);
        Assertions.assertEquals(users[5], group.getMembers()[2]);
        Assertions.assertEquals(users[6], group.getMembers()[3]);
        Assertions.assertEquals(users[7], group.getMembers()[4]);
        Assertions.assertEquals(users[3], group.getOwner());

        //remove user nicht in der group
        group.removeUser(new User("neuer1", "beschr1"));
        Assertions.assertEquals(5, group.getMembers().length);
        Assertions.assertEquals(users[3], group.getMembers()[0]);
        Assertions.assertEquals(users[4], group.getMembers()[1]);
        Assertions.assertEquals(users[5], group.getMembers()[2]);
        Assertions.assertEquals(users[6], group.getMembers()[3]);
        Assertions.assertEquals(users[7], group.getMembers()[4]);
        Assertions.assertEquals(users[3], group.getOwner());

        group.removeUser(new User("neuer2", "beschr2"));
        Assertions.assertEquals(5, group.getMembers().length);
        Assertions.assertEquals(users[3], group.getMembers()[0]);
        Assertions.assertEquals(users[4], group.getMembers()[1]);
        Assertions.assertEquals(users[5], group.getMembers()[2]);
        Assertions.assertEquals(users[6], group.getMembers()[3]);
        Assertions.assertEquals(users[7], group.getMembers()[4]);
        Assertions.assertEquals(users[3], group.getOwner());

        //remove alle user
        group.removeUser(users[3]);
        group.removeUser(users[4]);
        group.removeUser(users[5]);
        group.removeUser(users[6]);
        group.removeUser(users[7]);
        Assertions.assertEquals(0, group.getMembers().length);
        Assertions.assertEquals(null, group.getOwner());

        group.removeUser(users[1]);
        Assertions.assertEquals(0, group.getMembers().length);
        Assertions.assertEquals(null, group.getOwner());
        group.removeUser(new User("neuer2", "beschr2"));
        Assertions.assertEquals(0, group.getMembers().length);
        Assertions.assertEquals(null, group.getOwner());
    }


    // TESTS PICTURE


    @Test
    @DisplayName("Test Picture Konstruktor, getter, setter")
    public void Test09(){
        int[][] data = new int[][]{{0,1,2},{3,4,5}}; // ist ein [2][3]
        Picture picture = new Picture("ort1", data);
        Assertions.assertEquals("ort1", picture.getLocation());
        Assertions.assertEquals(data, picture.getData());
        Assertions.assertEquals(2,picture.getHeight());
        Assertions.assertEquals(3, picture.getWidth());
        Assertions.assertEquals(0, picture.getThumbnails().length);

        Picture[] thumbnails = new Picture[]{new Picture("thumb1", data), new Picture("thumb2", data), new Picture("thumb3", data)};
        picture.setThumbnails(thumbnails);
        Assertions.assertEquals(3, picture.getThumbnails().length);
        Assertions.assertEquals(thumbnails, picture.getThumbnails());
    }


    // TESTS INTERACTION

    @Test
    @DisplayName("Test Interaction Konstruktor, getter, setter")
    public void Test10(){
        User user1 = new User("user01", "description01");
        Interaction interaction = new Interaction(user1, 5);
        Assertions.assertEquals(user1, interaction.getUser());
        Assertions.assertEquals(5, interaction.getInteractionType());
    }
}
