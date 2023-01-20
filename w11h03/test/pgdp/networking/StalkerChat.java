package pgdp.networking;

import java.util.ArrayList;
import java.util.List;
import pgdp.networking.ViewController.Message;

public class StalkerChat {
    private static String username = "YOUR_USERNAME_HERE";
    private static String password = "YOUR_PASSWORD_HERE";

    private static class PrintMessage {
        private Message m;
        private boolean printed;

        public PrintMessage(Message message) {
            this.m = message;
            this.printed = false;
        }

        public Message getMessage() {
            return m;
        }

        public void print() {
            if (printed)
                return;
            System.out.printf("[%s:%s] %s\n", m.date().getHour(), m.date().getMinute(), m.content());
            printed = true;
        }
    }

    private static DataHandler dataHandler = new DataHandler();

    private static List<PrintMessage> messages = new ArrayList<>();
    private static int page = 0;
    private static int count = 1000;

    //
    // Print new Chats
    //

    public static void printAll() {
        scanMessages();
        messages.forEach(i -> i.print());
    }

    //
    // Get all recent messages
    //

    public static void scanMessages() {
        var s = 0;

        do {
            var m = dataHandler.getMessagesWithUser(1, count, page++);

            for (Message message : m) {
                if (messages.stream().noneMatch(i -> i.getMessage().id() == message.id())) {
                    messages.add(new PrintMessage(message));
                }
            }

            s = m.size();
        } while (s >= count);

        page--;
    }

    //
    // Entry
    //

    public static void main(String[] args) {
        if (!dataHandler.login(username, password)) {
            System.err.println("Could not login");
            return;
        }

        while (true) {
            scanMessages();
            printAll();
        }
    }
}
