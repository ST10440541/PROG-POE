package com.mycompany.quickchatapp;

/*
 * References:
 * Tpointtech.com, 2024. JavaScript Array Methods. [online] Available at: https://www.tpointtech.com/javascript-array-methods [Accessed 25 June 2025].
 * Tpointtech.com, 2024. JavaScript JSON. [online] Available at: https://www.tpointtech.com/javascript-json [Accessed 25 June 2025].
 * Tpointtech.com, 2024. JavaScript Array. [online] Available at: https://www.tpointtech.com/javascript-array [Accessed 25 June 2025].
 * OpenAI, 2025. ChatGPT (June 2025 version). [online] Available at: https://chat.openai.com [Accessed 25 June 2025].
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

class Message {
    private final String messageID;
    private static int totalMessages = 0;
    private final int messageNumber;
    private final String recipientCell;
    private final String message;
    private final String messageHash;

    public Message(String recipientCell, String message) {
        this.messageID = generateMessageID();
        this.messageNumber = ++totalMessages;
        this.recipientCell = recipientCell;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    public boolean checkRecipientCell() {
        return recipientCell.length() <= 10 && (recipientCell.startsWith("+") || recipientCell.startsWith("0"));
    }

    public boolean isValidMessage() {
        return message.length() <= 250;
    }

    private String createMessageHash() {
        String[] words = message.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String getRecipientCell() {
        return recipientCell;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    
    public String toString() {
        return "Message ID: " + messageID +
               "\nRecipient: " + recipientCell +
               "\nMessage: " + message +
               "\nHash: " + messageHash;
    }
}
 
public class QuickChatApp {

    static List<Message> sentMessages = new ArrayList<>();
    static List<Message> disregardedMessages = new ArrayList<>();
    static List<Message> storedMessages = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please log in to continue.");
        System.out.print("Username: ");
        String user = scanner.nextLine();
        System.out.print("Password: ");
        String pass = scanner.nextLine();

        if (!(user.equals("admin") && pass.equals("1234"))) {
            System.out.println("Login failed.");
            return;
        }

        System.out.println("Welcome to QuickChat.");
        System.out.print("How many messages would you like to enter? ");
        int messageLimit = Integer.parseInt(scanner.nextLine());
        int messageCount = 0;

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1) Send/Store/Disregard Message");
            System.out.println("2) Show all sent messages (with sender/recipient)");
            System.out.println("3) Show longest message");
            System.out.println("4) Search message by ID");
            System.out.println("5) Search messages by recipient");
            System.out.println("6) Delete message by hash");
            System.out.println("7) Show full message report");
            System.out.println("8) Quit");

            System.out.print("Enter your choice: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1" -> {
                    if (messageCount >= messageLimit) {
                        System.out.println("You have reached the message limit.");
                        continue;
                    }

                    System.out.print("Enter recipient phone number: ");
                    String cell = scanner.nextLine();

                    System.out.print("Enter message: ");
                    String msgText = scanner.nextLine();

                    if (msgText.length() > 250) {
                        System.out.println("Please enter a message of less than 250 characters.");
                        continue;
                    }

                    Message msg = new Message(cell, msgText);

                    if (!msg.checkRecipientCell()) {
                        System.out.println("Invalid phone number. Must be 10 digits or less and start with '+' or '0'.");
                        continue;
                    }

                    System.out.println("Choose one:");
                    System.out.println("a) Send Message");
                    System.out.println("b) Store Message");
                    System.out.println("c) Disregard");

                    String action = scanner.nextLine();

                    switch (action.toLowerCase()) {
                        case "a" -> {
                            sentMessages.add(msg);
                            System.out.println("Message sent.");
                            System.out.println(msg);
                            messageCount++;
                        }
                        case "b" -> {
                            storedMessages.add(msg);
                            System.out.println("Message stored for later.");
                            System.out.println(msg);
                            messageCount++;
                        }
                        case "c" -> {
                            disregardedMessages.add(msg);
                            System.out.println("Message disregarded.");
                        }
                        default -> System.out.println("Invalid choice.");
                    }
                }

                case "2" -> showSentMessages();

                case "3" -> showLongestMessage();

                case "4" -> {
                    System.out.print("Enter message ID: ");
                    String id = scanner.nextLine();
                    searchByID(id);
                }

                case "5" -> {
                    System.out.print("Enter recipient number: ");
                    String recipient = scanner.nextLine();
                    searchByRecipient(recipient);
                }

                case "6" -> {
                    System.out.print("Enter message hash to delete: ");
                    String hash = scanner.nextLine();
                    deleteByHash(hash);
                }

                case "7" -> showFullReport();

                case "8" -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void showSentMessages() {
        System.out.println("\nSent Messages:");
        for (Message msg : sentMessages) {
            System.out.println("Sender/Recipient: " + msg.getRecipientCell());
            System.out.println("Message: " + msg.getMessage());
            System.out.println();
        }
    }

    static void showLongestMessage() {
        String longest = "";
        for (Message msg : sentMessages) {
            if (msg.getMessage().length() > longest.length()) {
                longest = msg.getMessage();
            }
        }
        if (!longest.isEmpty()) {
            System.out.println("Longest Message: " + longest);
        } else {
            System.out.println("No sent messages.");
        }
    }

    static void searchByID(String id) {
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(id)) {
                System.out.println("Recipient: " + msg.getRecipientCell());
                System.out.println("Message: " + msg.getMessage());
                return;
            }
        }
        System.out.println("Message ID not found.");
    }

    static void searchByRecipient(String recipient) {
        boolean found = false;
        for (Message msg : sentMessages) {
            if (msg.getRecipientCell().equals(recipient)) {
                System.out.println(msg);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No messages found for recipient.");
        }
    }

    static void deleteByHash(String hash) {
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).getMessageHash().equals(hash)) {
                sentMessages.remove(i);
                System.out.println("Message deleted.");
                return;
            }
        }
        System.out.println("Hash not found.");
    }

    static void showFullReport() {
        System.out.println("\n--- Sent Messages ---");
        for (Message msg : sentMessages) {
            System.out.println(msg);
        }
        System.out.println("\n--- Stored Messages ---");
        for (Message msg : storedMessages) {
            System.out.println(msg);
        }
        System.out.println("\n--- Disregarded Messages ---");
        for (Message msg : disregardedMessages) {
            System.out.println(msg);
        }
    }
}