package pgdp.messenger.test;
import org.junit.jupiter.api.*;
import pgdp.messenger.*;

import java.time.LocalDateTime;
import java.time.Month;

public class ListBehaviourTest {
    private Message[] messages;
    private User Peter, David, Anna, Linda;

    @BeforeEach
    @DisplayName("Creating messages")
    void setup() {
        Peter = new User(1, "Peter", null);
        David = new User(2, "David", null);
        Anna = new User(3, "Anna", null);
        Linda = new User(4, "Linda", null);

        Message message1 = new Message(0, LocalDateTime.of(2022, Month.AUGUST, 20, 19, 30, 40), Peter, "Wanna grab a bite?");
        Message message2 = new Message(1, LocalDateTime.of(2022, Month.AUGUST, 20, 19, 32, 30), David, "Yeah, sure! When, bro?");
        Message message3 = new Message(2, LocalDateTime.of(2022, Month.AUGUST, 20, 19, 33, 40), Peter, "How about tomorrow during lunch time?");
        Message message4 = new Message(3, LocalDateTime.of(2022, Month.AUGUST, 20, 19, 40, 00), David, "Yeah, that's sounds great! Where do you want to meet?");
        Message message5 = new Message(4, LocalDateTime.of(2022, Month.AUGUST, 20, 20, 11, 19), Peter, "I think we could go to the new restaurant across the street, so let's meet at the reception.");
        Message message6 = new Message(5, LocalDateTime.of(2022, Month.AUGUST, 20, 20, 11, 30), David, "Aight! See ya tomorrow.");

        Message message7 = new Message(6, LocalDateTime.of(2022, Month.AUGUST, 24, 8, 30, 40), Anna, "Happy 20th birthday, Linda!");
        Message message8 = new Message(7, LocalDateTime.of(2022, Month.AUGUST, 24, 9, 03, 23), Anna, "I wish all the best and enjoy your day!");
        Message message9 = new Message(8, LocalDateTime.of(2022, Month.AUGUST, 24, 10, 24, 13), Linda, "Happy 20th birthday, Anna!");
        Message message10 = new Message(9, LocalDateTime.of(2023, Month.APRIL, 24, 11, 19, 44), Anna, "Happy 21st birthday, Linda!");
        Message message11 = new Message(10, LocalDateTime.of(2023, Month.AUGUST, 24, 12, 44, 53), Linda, "Happy 21st birthday, Anna!");

        this.messages = new Message[]{message1, message2, message3, message4, message5, message6, message7, message8, message9, message10, message11};
    }

    @Nested
    @DisplayName("Tests for the method getByID")
    class getByIDTests {
        @Test
        @DisplayName("Get a specific message with its ID")
        void getMessageById() {
            List list = new List();
            List orderedList = new List();

            // Unordered list
            list.add(messages[3]);
            list.add(messages[4]);
            list.add(messages[8]);
            list.add(messages[1]);
            list.add(messages[9]);
            list.add(messages[10]);

            Message message1 = list.getByID(8);
            Message message2 = list.getByID(3);
            Message message3 = list.getByID(10);

            Assertions.assertSame(messages[3],message2, "Method getByID doesn't work properly. Got wrong message");
            Assertions.assertSame(messages[8],message1, "Method getByID doesn't work properly. Got wrong message");
            Assertions.assertSame(messages[10],message3, "Method getByID doesn't work properly. Got wrong message");

            orderedList.add(messages[0]);
            orderedList.add(messages[1]);
            orderedList.add(messages[2]);

            Message message4 = orderedList.getByID(2);

            Assertions.assertSame(messages[2],message4, "Method getByID doesn't work properly. Got wrong message");
        }

        @Test
        @DisplayName("Get a specific message from empty list")
        void getMessageFromEmptyList() {
            List list = new List();
            Message message = list.getByID(3);

            Assertions.assertSame(null, message, "Method getByID doesn't work properly. Expected to return null");
        }

        @Test
        @DisplayName("Get a message that is not in the list")
        void getMessageNotInList() {
            List list = new List();
            list.add(messages[3]);
            list.add(messages[4]);
            list.add(messages[8]);
            list.add(messages[1]);
            list.add(messages[9]);
            list.add(messages[10]);

            Message message = list.getByID(7);

            Assertions.assertSame(null, message, "Method getByID doesn't work properly. Expected to return null");
        }
    }

    @Nested
    @DisplayName("Tests for the megaMerge method")
    class megaMergeTests {
        @Test
        @DisplayName("Merge multiple sorted list")
        void megaMergeLists() {
            List orderedList1 = new List();
            List orderedList2 = new List();
            List orderedList3 = new List();

            orderedList1.add(messages[0]);
            orderedList1.add(messages[8]);

            orderedList2.add(messages[1]);
            orderedList2.add(messages[3]);
            orderedList2.add(messages[4]);
            orderedList2.add(messages[6]);
            orderedList2.add(messages[9]);

            orderedList3.add(messages[2]);
            orderedList3.add(messages[5]);
            orderedList3.add(messages[7]);
            orderedList3.add(messages[10]);

            List mergedList = new List();
            mergedList.add(messages[0]);
            mergedList.add(messages[1]);
            mergedList.add(messages[2]);
            mergedList.add(messages[3]);
            mergedList.add(messages[4]);
            mergedList.add(messages[5]);
            mergedList.add(messages[6]);
            mergedList.add(messages[7]);
            mergedList.add(messages[8]);
            mergedList.add(messages[9]);
            mergedList.add(messages[10]);

            List megaMergedList = List.megaMerge(orderedList1, orderedList3, orderedList2);

            for(int i = 0; i < megaMergedList.size(); i++) {
                Assertions.assertSame(mergedList.getByIndex(i), megaMergedList.getByIndex(i), "Wrong order of the merged list");
            }
        }

        @Test
        @DisplayName("Merge without any lists")
        void mergeWithoutLists() {
            List megaMergedList = List.megaMerge();

            Assertions.assertTrue(megaMergedList.isEmpty(), "Expected the returned List to be empty.");
        }

        @Test
        @DisplayName("Merge empty lists")
        void mergeEmptyLists() {
            List emptyList1 = new List();
            List emptyList2 = new List();
            List emptyList3 = new List();

            List megaMergedList = List.megaMerge(emptyList1, emptyList2, emptyList3);
            Assertions.assertTrue(megaMergedList.isEmpty(), "Expected the returned List to be empty");
        }

        @Test
        @DisplayName("Merge one list")
        void mergeOneList() {
            List orderedList1 = new List();
            List orderedList2 = new List();

            orderedList1.add(messages[0]);
            orderedList1.add(messages[8]);
            
            orderedList2.add(messages[0]);
            orderedList2.add(messages[8]);

            List megaMeredList = List.megaMerge(orderedList1);

            for(int i = 0; i < megaMeredList.size(); i++) {
                Assertions.assertSame(orderedList2.getByIndex(i), megaMeredList.getByIndex(i), "Wrong order of the merged list");
            }
        }

        @Test
        @DisplayName("Merge ordered lists with empty list")
        void mergeListsWithEmptyList() {
            List orderedList1 = new List();
            List orderedList2 = new List();
            List orderedList3 = new List();
            List emptyList = new List();

            orderedList1.add(messages[0]);
            orderedList1.add(messages[8]);

            orderedList2.add(messages[1]);
            orderedList2.add(messages[3]);
            orderedList2.add(messages[4]);
            orderedList2.add(messages[6]);
            orderedList2.add(messages[9]);

            orderedList3.add(messages[2]);
            orderedList3.add(messages[5]);
            orderedList3.add(messages[7]);
            orderedList3.add(messages[10]);

            List mergedList = new List();
            mergedList.add(messages[0]);
            mergedList.add(messages[1]);
            mergedList.add(messages[2]);
            mergedList.add(messages[3]);
            mergedList.add(messages[4]);
            mergedList.add(messages[5]);
            mergedList.add(messages[6]);
            mergedList.add(messages[7]);
            mergedList.add(messages[8]);
            mergedList.add(messages[9]);
            mergedList.add(messages[10]);

            List megaMergedList = List.megaMerge(orderedList1, orderedList2, emptyList, orderedList3);

            for(int i = 0; i < megaMergedList.size(); i++) {
                Assertions.assertSame(mergedList.getByIndex(i), megaMergedList.getByIndex(i), "Wrong order of the merged list");
            }
        }
    }

    @Nested
    @DisplayName("Tests for the filterDays method")
    class filterDays {
        @Test
        @DisplayName("Test filterDays method for start time null")
        void filterDaysStartTimeNull() {
            List sortedList = new List();
            sortedList.add(messages[0]);
            sortedList.add(messages[1]);
            sortedList.add(messages[2]);
            sortedList.add(messages[3]);

            LocalDateTime end = LocalDateTime.of(2022, Month.JULY, 20, 19, 30, 20);

            List filteredList = sortedList.filterDays(null, end);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the filtered list to be empty with start time null");
            for(int i = 0; i < sortedList.size(); i++) {
                Assertions.assertSame(messages[i], sortedList.getByIndex(i), "The given list shouldn't be changed");
            }
        }

        @Test
        @DisplayName("Test filterDays method for end time null")
        void filterDaysEndTimeNull() {
            List sortedList = new List();
            sortedList.add(messages[0]);
            sortedList.add(messages[1]);
            sortedList.add(messages[2]);
            sortedList.add(messages[3]);

            LocalDateTime start = LocalDateTime.of(2022, Month.JULY, 20, 19, 30, 20);

            List filteredList = sortedList.filterDays(start, null);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the filtered list to be empty with end time null");
            for(int i = 0; i < sortedList.size(); i++) {
                Assertions.assertSame(messages[i], sortedList.getByIndex(i), "The given list shouldn't be changed");
            }
        }

        @Test
        @DisplayName("Test filterDays method for the empty list")
        void filterDaysEmptyList() {
            List emptyList = new List();

            LocalDateTime start = LocalDateTime.of(2022, Month.JULY, 20, 19, 30, 20);
            LocalDateTime end = LocalDateTime.of(2022, Month.JULY, 21, 19, 30, 20);

            List filteredList = emptyList.filterDays(start, end);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the filtered list to be empty");
            Assertions.assertTrue(emptyList.isEmpty(), "The given list shouldn't be changed");
        }

        @Test
        @DisplayName("Test filterDays method for the start and end being the same")
        void filterDaysStartEndSame() {
            List sortedList = new List();
            sortedList.add(messages[0]);
            sortedList.add(messages[1]);
            sortedList.add(messages[2]);
            sortedList.add(messages[3]);

            LocalDateTime start = LocalDateTime.of(2022, Month.JULY, 20, 19, 30, 20);
            LocalDateTime end = LocalDateTime.of(2022, Month.JULY, 20, 19, 30, 20);

            List filteredList = sortedList.filterDays(start, end);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the filtered list to be empty with start time and end time being same");
            for(int i = 0; i < sortedList.size(); i++) {
                Assertions.assertSame(messages[i], sortedList.getByIndex(i), "The given list shouldn't be changed");
            }
        }

        @Test
        @DisplayName("Test filterDays method for the end time being before start time")
        void filterDaysEndBeforeStart() {
            List sortedList = new List();
            sortedList.add(messages[0]);
            sortedList.add(messages[1]);
            sortedList.add(messages[2]);
            sortedList.add(messages[3]);

            LocalDateTime start = LocalDateTime.of(2022, Month.JULY, 20, 19, 30, 20);
            LocalDateTime end = LocalDateTime.of(2022, Month.JULY, 19, 19, 30, 20);

            List filteredList = sortedList.filterDays(start, end);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the filtered list to be empty with end time being before start time");
            for(int i = 0; i < sortedList.size(); i++) {
                Assertions.assertSame(messages[i], sortedList.getByIndex(i), "The given list shouldn't be changed");
            }
        }

        @Test
        @DisplayName("Test filterDays method")
        void filterDays() {
            List sortedList = new List();
            sortedList.add(messages[0]);
            sortedList.add(messages[1]);
            sortedList.add(messages[2]);
            sortedList.add(messages[3]);
            sortedList.add(messages[4]);
            sortedList.add(messages[5]);
            sortedList.add(messages[6]);
            sortedList.add(messages[7]);
            sortedList.add(messages[8]);
            sortedList.add(messages[9]);
            sortedList.add(messages[10]);

            List expectedList = new List();
            expectedList.add(messages[4]);
            expectedList.add(messages[5]);
            expectedList.add(messages[6]);
            expectedList.add(messages[7]);
            expectedList.add(messages[8]);
            expectedList.add(messages[9]);

            LocalDateTime start = LocalDateTime.of(2022, Month.AUGUST, 20, 20, 11, 19);
            LocalDateTime end = LocalDateTime.of(2023, Month.MAY, 19, 19, 30, 20);

            List filteredList = sortedList.filterDays(start, end);

            for(int i = 0; i < filteredList.size(); i++) {
                Assertions.assertSame(expectedList.getByIndex(i), filteredList.getByIndex(i), "Filter method doesn't work as it supposes to.");
            }
            for(int i = 0; i < sortedList.size(); i++) {
                Assertions.assertSame(messages[i], sortedList.getByIndex(i), "The given list shouldn't be changed");
            }
        }

        @Test
        @DisplayName("Test filterDays method with an interval without any message")
        void filterDaysNoMessageInBetween() {
            List sortedList = new List();
            sortedList.add(messages[0]);
            sortedList.add(messages[1]);
            sortedList.add(messages[2]);
            sortedList.add(messages[3]);
            sortedList.add(messages[4]);
            sortedList.add(messages[5]);
            sortedList.add(messages[6]);
            sortedList.add(messages[7]);
            sortedList.add(messages[8]);
            sortedList.add(messages[9]);
            sortedList.add(messages[10]);

            LocalDateTime start = LocalDateTime.of(2022, Month.DECEMBER, 20, 20, 11, 19);
            LocalDateTime end = LocalDateTime.of(2023, Month.FEBRUARY, 19, 19, 30, 20);

            List filteredList = sortedList.filterDays(start, end);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the filtered array to be empty with no messages in the given interval");
            for(int i = 0; i < sortedList.size(); i++) {
                Assertions.assertSame(messages[i], sortedList.getByIndex(i), "The given list shouldn't be changed");
            }
        }
    }

    @Nested
    @DisplayName("Tests for the filterUser method")
    class filterUserTests {
        @Test
        @DisplayName("Test filterUser method - user is null")
        void filterUserNull() {
            List list = new List();
            list.add(messages[0]);
            list.add(messages[1]);
            list.add(messages[2]);
            list.add(messages[3]);
            list.add(messages[4]);


            List filteredList = list.filterUser(null);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the list to be empty with user null");
            for(int i = 0; i < list.size(); i++) {
                Assertions.assertSame(messages[i], list.getByIndex(i), "The given list shouldn't be changed");
            }
        }

        @Test
        @DisplayName("Test filterUser method - list is empty")
        void filterUserEmptyList() {
            List list = new List();

            List filteredList = list.filterUser(Peter);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the list to be empty with empty list");
            Assertions.assertTrue(list.isEmpty(), "The given list shouldn't be changed");
        }

        @Test
        @DisplayName("Test filterUser method - user doesn't have any message in the list")
        void filterUserNoSuchUser() {
            List list = new List();
            list.add(messages[0]);
            list.add(messages[1]);
            list.add(messages[2]);
            list.add(messages[3]);
            list.add(messages[4]);


            List filteredList = list.filterUser(Anna);

            Assertions.assertTrue(filteredList.isEmpty(), "Expected the filtered list to be empty with user that doesn't have any messages in the list");
            for(int i = 0; i < list.size(); i++) {
                Assertions.assertSame(messages[i], list.getByIndex(i), "The given list shouldn't be changed");
            }
        }

        @Test
        @DisplayName("Test filterUser method")
        void filterUser() {
            List list = new List();
            list.add(messages[0]);
            list.add(messages[1]);
            list.add(messages[2]);
            list.add(messages[3]);
            list.add(messages[4]);
            list.add(messages[5]);

            List expectedList = new List();
            expectedList.add(messages[0]);
            expectedList.add(messages[2]);
            expectedList.add(messages[4]);

            List filteredList = list.filterUser(Peter);

            for(int i = 0; i < filteredList.size(); i++) {
                Assertions.assertSame(expectedList.getByIndex(i), filteredList.getByIndex(i), "The method filterByUser doesn't work as expected.");
            }
            for(int i = 0; i < list.size(); i++) {
                Assertions.assertSame(messages[i], list.getByIndex(i), "The given list shouldn't be changed");
            }
        }
    }

    @Nested
    @DisplayName("Tests for the toString method")
    class toStringTests {
        @Test
        @DisplayName("Test toString method for the empty list")
        void toStringEmptyList() {
            List list = new List();
            Assertions.assertEquals("", list.toString(), "Expected the string to be empty for the empty list.");
        }

        @Test
        @DisplayName("Test toString method")
        void toStringTest() {
            List list = new List();
            list.add(messages[0]);
            list.add(messages[1]);
            list.add(messages[2]);
            list.add(messages[3]);
            list.add(messages[4]);
            list.add(messages[5]);

            String exptectedOutput = messages[0] + "\n" + messages[1] + "\n" + messages[2] + "\n" + messages[3] + "\n" + messages[4] + "\n" + messages[5] + "\n";
            Assertions.assertEquals(exptectedOutput, list.toString(), "Wrong string as an output, check if after the last message there is a breakline");
        }
    }
}
