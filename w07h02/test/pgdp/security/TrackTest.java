package pgdp.security;

import org.junit.jupiter.api.Test;
import pgdp.mocks.SignalPostMock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class TrackTest {
    private static Track trackWithMocks(int amountOfPosts) {
        Track track = new Track(amountOfPosts);
        final var mockPosts = new SignalPost[amountOfPosts];
        for (int i = 0; i < mockPosts.length; i++) {
            mockPosts[i] = new SignalPostMock(i);
        }
        track.setPosts(mockPosts);
        return track;
    }

    private static Track trackWithMocks() {
        return trackWithMocks(3);
    }

    @Test
    void postsArrayShouldBeInitializedWithCorrectSize() {
        Track track = new Track(5);
        assertEquals(5, track.getPosts().length);
    }

    @Test
    void postsArrayShouldBeInitializedWithSizeTenWhenLengthOutOfRange() {
        Track track = new Track(0);
        assertEquals(10, track.getPosts().length);
        track = new Track(-1);
        assertEquals(10, track.getPosts().length);
    }

    @Test
    void lastPostShouldBeFinishPost() {
        final var amountOfPosts = IntStream.range(-20, 100).toArray();
        for (int i : amountOfPosts) {
            Track track = new Track(i);
            if (i <= 0) {
                i = 10;
            }
            assertTrue(track.getPosts()[i - 1] instanceof FinishPost);

        }
    }

    @Test
    void postsWithLengthOneShouldOnlyHaveFinishPost() {
        Track track = new Track(1);
        assertTrue(track.getPosts()[0] instanceof FinishPost);
    }

    @Test
    void everyThirdPostShouldBeLightPanelElseFlagPost() {
        final var amountOfPosts = IntStream.range(-20, 100).toArray();
        for (int i : amountOfPosts) {
            Track track = new Track(i);
            if (i <= 0) {
                i = 10;
            }
            for (int j = 0; j < i; j++) {
                if (j == i - 1) {
                    assertEquals(FinishPost.class, track.getPosts()[j].getClass());
                } else if (j % 3 == 0) {
                    assertEquals(LightPanel.class, track.getPosts()[j].getClass());
                } else {
                    assertEquals(FlagPost.class, track.getPosts()[j].getClass());
                }
            }
        }
    }

    @Test
    void postsShouldHaveIncreasingPostNumbers() {
        Track track = new Track(20);
        final var posts = track.getPosts();
        for (int i = 0; i < posts.length; i++) {
            assertEquals(i, posts[i].getPostNumber());
        }
    }

    @Test
    void postsShouldHaveIncreasingPostNumbersWithNegativeCapacity() {
        Track track = new Track(-1);
        final var posts = track.getPosts();
        for (int i = 0; i < posts.length; i++) {
            assertEquals(i, posts[i].getPostNumber());
        }
    }

    @Test
    void postsShouldHaveIncreasingPostNumbersWithZeroCapacity() {
        Track track = new Track(0);
        final var posts = track.getPosts();
        for (int i = 0; i < posts.length; i++) {
            assertEquals(i, posts[i].getPostNumber());
        }
    }

    @Test
    void smallPostArrayTest() {
        Track track = new Track(3);
        assertEquals(3, track.getPosts().length);
        assertTrue(track.getPosts()[0] instanceof LightPanel);
        assertTrue(track.getPosts()[1] instanceof FlagPost);
        assertTrue(track.getPosts()[2] instanceof FinishPost);
    }

    void setAllPosts(boolean up) {
        final var track = trackWithMocks();
        final var type = "down";
        track.setAll("down", up);
        for (SignalPost post : track.getPosts()) {
            final var methodCalls = ((SignalPostMock) post).getMethodCalls(up ? "up" : "down");
            assertEquals(1, methodCalls.size());
            assertEquals(type, methodCalls.get(0).args()[0]);
        }
    }

    @Test
    void setAllShouldSetAllPostsToUp() {
        setAllPosts(true);
    }


    @Test
    void setAllShouldSetAllPostsToDown() {
        setAllPosts(false);
    }

    @Test
    void setAllShouldSetAllPostsToUpWithNullType() {
        final var track = trackWithMocks();
        track.setAll(null, true);
        for (SignalPost post : track.getPosts()) {
            final var methodCalls = ((SignalPostMock) post).getMethodCalls("up");
            assertEquals(1, methodCalls.size());
            assertNull(methodCalls.get(0).args()[0]);
        }
    }


    void assertSetRange(String type, boolean up, int start, int end, int amountOfPosts, List<Integer> expectedCalls) {
        final var track = trackWithMocks(amountOfPosts);
        track.setRange(type, up, start, end);
        for (final var post : track.getPosts()) {
            final var methodCalls = ((SignalPostMock) post).getMethodCalls(up ? "up" : "down");
            if (expectedCalls.contains(post.getPostNumber())) {
                assertEquals(1, methodCalls.size());
                final var args = methodCalls.get(0).args();
                assertEquals(type, args[0]);
            } else {
                assertEquals(0, methodCalls.size());
            }
        }
    }

    @Test
    void setRangeNotCircle() {
        assertSetRange("down", true, 0, 1, 3, List.of(0, 1));
        assertSetRange("down", true, 0, 2, 3, List.of(0, 1, 2));
        assertSetRange("down", true, 5, 10, 20, List.of(5, 6, 7, 8, 9, 10));
    }

    @Test
    void setRangeCircle() {
        assertSetRange("down", true, 2, 1, 3, List.of(2, 0, 1));
        assertSetRange("down", true, 3, 2, 5, List.of(3, 4, 5, 0, 1, 2));
        assertSetRange("down", true, 1, 1, 3, List.of(1));
    }

    void assertCreateHazardAt(int start, int end, int capacity, List<Integer> expectedCalls) {
        final var track = trackWithMocks(capacity);
        track.createHazardAt(start, end);
        for (final var post : track.getPosts()) {
            final var methodCalls = ((SignalPostMock) post).getMethodCalls("up");
            if (expectedCalls.contains(post.getPostNumber())) {
                assertEquals(1, methodCalls.size());
                final var args = methodCalls.get(0).args();
                if (post.getPostNumber() == end) {
                    assertEquals(args[0], "green");
                } else {
                    assertEquals(args[0], "yellow");
                }
            } else {
                assertEquals(0, methodCalls.size());
            }
        }
    }

    @Test
    void createdHazardAtNotCircle() {
        assertCreateHazardAt(1, 3, 4, List.of(1, 2, 3));
        assertCreateHazardAt(1, 2, 3, List.of(1, 2));
        assertCreateHazardAt(2, 10, 20, List.of(2, 3, 4 ,5, 6, 7, 8, 9, 10));    }

    @Test
    void createHazardAtCircle() {
        assertCreateHazardAt(3, 1, 4, List.of(3, 0, 1));
        assertCreateHazardAt(10, 5, 15, List.of(10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5));
        assertCreateHazardAt(3, 3, 5, List.of(3));    }

    void assertRemoveHazardAt(int start, int end, int capacity, List<Integer> expectedCalls) {
        final var track = trackWithMocks(capacity);
        track.removeHazardAt(start, end);
        for (final var post : track.getPosts()) {
            final var methodCalls = ((SignalPostMock) post).getMethodCalls("down");
            if (expectedCalls.contains(post.getPostNumber())) {
                assertEquals(1, methodCalls.size());
                final var args = methodCalls.get(0).args();
                assertEquals(args[0], "danger");
            } else {
                assertEquals(0, methodCalls.size());
            }
        }
    }

    @Test
    void removeHazardAtNotCircle() {
        assertRemoveHazardAt(1, 3, 4, List.of(1, 2, 3));
        assertRemoveHazardAt(1, 2, 3, List.of(1, 2));
        assertRemoveHazardAt(2, 10, 20, List.of(2, 3, 4 ,5, 6, 7, 8, 9, 10));
    }

    @Test
    void removeHazardAtCircle() {
        assertRemoveHazardAt(3, 1, 4, List.of(3, 0, 1));
        assertRemoveHazardAt(10, 5, 15, List.of(10, 11, 12, 13, 14, 0, 1, 2, 3, 4, 5));
        assertRemoveHazardAt(3, 3, 5, List.of(3));
    }

    void assertLappedCarAt(boolean up, int postAt, int capacity, List<Integer> expectedCalls) {
        final HashMap<Integer, Integer> callsPerPost = new HashMap<>();
        for (int i = 0; i < expectedCalls.size(); i++) {
            final var posts = callsPerPost.computeIfAbsent(expectedCalls.get(i), k -> 0);
            callsPerPost.put(expectedCalls.get(i), posts + 1);
        }
        final var track = trackWithMocks(capacity);
        if (up) track.createLappedCarAt(postAt);
        else track.removeLappedCarAt(postAt);
        for (final var post : track.getPosts()) {
            final var methodCalls = ((SignalPostMock) post).getMethodCalls(up ? "up" : "down");
            if (callsPerPost.containsKey(post.getPostNumber())) {
                assertEquals(callsPerPost.get(post.getPostNumber()), methodCalls.size());
                for (final var methodCall : methodCalls) {
                    final var args = methodCall.args();
                    assertEquals(args[0], "blue");
                }
            } else {
                assertEquals(0, methodCalls.size());
            }
        }
    }

    void assertCreateLappedCarAt(int postAt, int capacity, List<Integer> expectedCalls) {
        assertLappedCarAt(true, postAt, capacity, expectedCalls);
    }

    void assertRemoveLappedCarAt(int postAt, int capacity, List<Integer> expectedCalls) {
        assertLappedCarAt(false, postAt, capacity, expectedCalls);
    }

    @Test
    void createLappedCarAtSmallArray() {
        assertCreateLappedCarAt(1, 4, List.of(1, 2, 3, 0));
        assertCreateLappedCarAt(1, 3, List.of(1, 2, 0));
        assertCreateLappedCarAt(2, 3, List.of(2, 0, 1));
    }

    @Test
    void createLappedCarAtArrayBiggerThanThree() {
        assertCreateLappedCarAt(0, 8, List.of(0, 1, 2, 3));
        assertCreateLappedCarAt(7, 8, List.of(7, 0, 1, 2));
        assertCreateLappedCarAt(5, 10, List.of(5, 6, 7, 8));
        assertCreateLappedCarAt(9, 10, List.of(9, 0, 1, 2));
    }

    @Test
    void removeLappedCarAtSmallArray() {
        assertRemoveLappedCarAt(1, 4, List.of(1, 2, 3, 0));
        assertRemoveLappedCarAt(1, 3, List.of(1, 2, 0));
        assertRemoveLappedCarAt(2, 3, List.of(2, 0, 1));
    }

    @Test
    void removeLappedCarAtArrayBiggerThanThree() {
        assertRemoveLappedCarAt(0, 8, List.of(0, 1, 2, 3));
        assertRemoveLappedCarAt(7, 8, List.of(7, 0, 1, 2));
        assertRemoveLappedCarAt(5, 10, List.of(5, 6, 7, 8));
        assertRemoveLappedCarAt(9, 10, List.of(9, 0, 1, 2));
    }

    @Test
    void lengthZeroTests() {
        assertSetRange("down", true, 0, 0, 0, List.of());
        assertRemoveLappedCarAt(0, 0, List.of());
        assertCreateHazardAt(0, 0, 0, List.of());

    }

    @Test
    void printStatus() {
        final var output = new ByteArrayOutputStream();
        final var outSave = System.out;
        final var printStream = new PrintStream(output);
        System.setOut(printStream);
        final var track = new Track(4);
        final var posts = track.getPosts();
        final var expected = posts[0] +
                "\n" +
                posts[1] + "\n" +
                posts[2] + "\n" +
                posts[3] + "\n" +
                "\n";
        track.printStatus();

        assertEquals(expected, output.toString().replace("\r\n",  "\n").replace("\r", "\n"));
        System.setOut(outSave);
    }

}
