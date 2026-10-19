package com.mycompany.chatapp.prog5121_poe_part1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/** Represents one QuickChat message and its send, discard, and storage actions. */
public class Message {

    private static final List<Message> sentMessages = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String message;
    private final String messageHash;
    private String action;

    public static void setScanner(Scanner inputScanner) {
        scanner = inputScanner;
    }

    public Message(int messageNumber, String recipient, String message) {
        this(generateMessageID(), messageNumber, recipient, message);
    }

    // This constructor also makes it possible to test a known message ID and hash.
    public Message(String messageID, int messageNumber, String recipient, String message) {
        this.messageID = messageID == null ? "" : messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient == null ? "" : recipient;
        this.message = message == null ? "" : message;
        this.messageHash = buildMessageHash(this.messageID, this.messageNumber, this.message);
        this.action = "Pending";
    }

    private static String generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        // The assignment's international-format example is +27 followed by nine digits.
        if (recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /** Checks the 250-character limit and returns the assignment's test messages. */
    public String validateMessageLength() {
        if (message.length() <= 250) {
            return "Message ready to send.";
        }
        int excessCharacters = message.length() - 250;
        return "Message exceeds 250 characters by " + excessCharacters + "; please reduce the size.";
    }

    public String createMessageHash() {
        return buildMessageHash(messageID, messageNumber, message);
    }

    private static String buildMessageHash(String id, int number, String messageText) {
        String firstTwoDigits = id.substring(0, 2);
        String trimmedMessage = messageText.trim();
        if (trimmedMessage.isEmpty()) {
            return (firstTwoDigits + ":" + number + ":").toUpperCase();
        }
        String[] words = trimmedMessage.split("\\s+");
        String firstWord = words[0].replaceAll("[^\\p{L}\\p{N}]", "");
        String lastWord = words[words.length - 1].replaceAll("[^\\p{L}\\p{N}]", "");
        return (firstTwoDigits + ":" + number + ":" + firstWord + lastWord).toUpperCase();
    }

    public String SentMessage() {
        System.out.println("Choose an action: 1) Send Message  0) Disregard Message  2) Store Message");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return "Invalid option. Message remains pending.";
        }
        return SentMessage(choice);
    }

    // This overload lets the same choices be tested without interactive input.
    public String SentMessage(int choice) {
        if (choice == 1) {
            if (message.length() > 250) {
                return validateMessageLength();
            }
            if (!checkRecipientCell().startsWith("Cell phone number successfully")) {
                return checkRecipientCell();
            }
            action = "Sent";
            sentMessages.add(this);
            return "Message successfully sent.";
        } else if (choice == 0) {
            action = "Disregarded";
            return "Press 0 to delete the message.";
        } else if (choice == 2) {
            if (message.length() > 250) {
                return validateMessageLength();
            }
            if (!checkRecipientCell().startsWith("Cell phone number successfully")) {
                return checkRecipientCell();
            }
            try {
                storeMessage();
                action = "Stored";
                return "Message successfully stored.";
            } catch (IOException e) {
                return "Message could not be stored: " + e.getMessage();
            }
        }
        return "Invalid option. Please choose 1, 0, or 2.";
    }

    public String printMessages() {
        StringBuilder output = new StringBuilder();
        for (Message sentMessage : sentMessages) {
            output.append(sentMessage.getDetails()).append(System.lineSeparator());
        }
        return output.toString().trim();
    }

    public int returnTotalMessages() {
        return sentMessages.size();
    }

    public void storeMessage() throws IOException {
        Path file = Path.of("messages.json");
        String jsonObject = "  {\n"
                + "    \"messageID\": \"" + escapeJson(messageID) + "\",\n"
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "    \"messageNumber\": " + messageNumber + ",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(message) + "\",\n"
                + "    \"action\": \"Stored\"\n"
                + "  }";
        if (!Files.exists(file) || Files.readString(file, StandardCharsets.UTF_8).trim().isEmpty()) {
            Files.writeString(file, "[\n" + jsonObject + "\n]\n", StandardCharsets.UTF_8);
        } else {
            String existing = Files.readString(file, StandardCharsets.UTF_8).trim();
            if (existing.endsWith("]")) {
                String body = existing.substring(0, existing.length() - 1).trim();
                String updated = body.equals("[") ? "[\n" + jsonObject + "\n]\n"
                        : body + ",\n" + jsonObject + "\n]\n";
                Files.writeString(file, updated, StandardCharsets.UTF_8);
            } else {
                throw new IOException("messages.json is not a valid JSON array.");
            }
        }
        action = "Stored";
    }

    private String escapeJson(String value) {
        StringBuilder escaped = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char character = value.charAt(i);
            switch (character) {
                case '\\' -> escaped.append("\\\\");
                case '\"' -> escaped.append("\\\"");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                default -> {
                    if (character < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) character));
                    } else {
                        escaped.append(character);
                    }
                }
            }
        }
        return escaped.toString();
    }

    public String getDetails() {
        return "Message ID: " + messageID + System.lineSeparator()
                + "Message Hash: " + messageHash + System.lineSeparator()
                + "Recipient: " + recipient + System.lineSeparator()
                + "Message: " + message;
    }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessage() { return message; }
    public String getMessageHash() { return messageHash; }
    public String getAction() { return action; }

    public static void clearSentMessagesForTests() {
        sentMessages.clear();
    }
}
