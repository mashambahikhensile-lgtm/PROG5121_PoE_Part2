
package com.mycompany.chatapp.prog5121_poe_part1;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    @BeforeEach
    void resetMessages() {
        Message.clearSentMessagesForTests();
    }

    @Test
    void testMessageIdHasTenDigits() {
        Message message = new Message(
                1, "+27716893002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertTrue(message.getMessageID().matches("\\d{10}"));
    }

    @Test
    void testRecipientWithInternationalCode() {
        Message message = new Message(
                1, "+27716893002", "Hi Mike"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    void testRecipientWithoutInternationalCode() {
        Message message = new Message(
                2, "08575975889",
                "Hi Keegan, did you receive the payment?"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message.checkRecipientCell()
        );
    }

    @Test
    void testMessageHashMatchesAssignmentExample() {
        Message message = new Message(
                "0000000000", 0, "+27716893002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals("00:0:HITONIGHT", message.getMessageHash());
    }

    @Test
    void testHashesForRemainingMessagesInLoop() {
        String[] messages = {
            "Hello there friend",
            "Please call me tomorrow!",
            "We are meeting today."
        };

        for (int i = 0; i < messages.length; i++) {
            Message message = new Message(
                    i + 1, "+27716893002", messages[i]
            );

            String[] words = messages[i].trim().split("\\s+");

            String first = words[0].replaceAll("[^\\p{L}\\p{N}]", "");
            String last = words[words.length - 1]
                    .replaceAll("[^\\p{L}\\p{N}]", "");

            String expected = message.getMessageID().substring(0, 2)
                    + ":" + (i + 1) + ":"
                    + (first + last).toUpperCase();

            assertEquals(expected, message.getMessageHash());
        }
    }

    @Test
    void testMessageLengthSuccessMessage() {
        Message message = new Message(1, "+27716893002", "Hi Mike");

        assertEquals(
                "Message ready to send.",
                message.validateMessageLength()
        );
    }

    @Test
    void testMessageLengthFailureIncludesExcessCount() {
        Message message = new Message(
                1, "+27716893002", "a".repeat(257)
        );

        assertEquals(
                "Message exceeds 250 characters by 7; please reduce the size.",
                message.validateMessageLength()
        );
    }

    @Test
    void testMessageUnder250CharactersCanBeSent() {
        Message message = new Message(
                1, "+27716893002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );

        assertEquals(1, message.returnTotalMessages());
        assertTrue(message.printMessages().contains("Message Hash: "));
    }

    @Test
    void testMessageOver250CharactersIsRejected() {
        Message message = new Message(
                1, "+27716893002", "a".repeat(251)
        );

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.SentMessage(1)
        );

        assertEquals(0, message.returnTotalMessages());
    }

    @Test
    void testInvalidRecipientCannotBeSent() {
        Message message = new Message(
                1, "08575975889", "Test message"
        );

        assertTrue(
                message.SentMessage(1)
                        .startsWith("Cell phone number is incorrectly formatted")
        );

        assertEquals(0, message.returnTotalMessages());
    }

    @Test
    void testDiscardMessageDoesNotIncreaseSentCount() {
        Message message = new Message(
                2, "+27716893002",
                "Hi Keegan, did you receive the payment?"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(0)
        );

        assertEquals("Disregarded", message.getAction());
        assertEquals(0, message.returnTotalMessages());
    }

    @Test
    void testStoreMessageWritesJsonFile() throws IOException {
        Path file = Path.of("messages.json");

        boolean fileExistedBefore = Files.exists(file);
        String previousContent = fileExistedBefore
                ? Files.readString(file) : null;

        try {
            Files.deleteIfExists(file);

            Message message = new Message(
                    1, "+27716893002", "Test JSON storage"
            );

            assertEquals(
                    "Message successfully stored.",
                    message.SentMessage(2)
            );

            assertTrue(Files.exists(file));

            String json = Files.readString(file);

            assertTrue(json.trim().startsWith("["));

            // Trim whitespace so the test works with Windows line endings.
            assertTrue(json.trim().endsWith("]"));

            assertTrue(
                    json.contains("\"message\": \"Test JSON storage\"")
            );

            assertTrue(
                    json.contains("\"action\": \"Stored\"")
            );

        } finally {
            Files.deleteIfExists(file);

            if (fileExistedBefore) {
                Files.writeString(file, previousContent);
            }
        }
    }

    @Test
    void testInvalidRecipientCannotBeStored() {
        Message message = new Message(
                1, "08575975889", "Test message"
        );

        assertTrue(
                message.SentMessage(2)
                        .startsWith("Cell phone number is incorrectly formatted")
        );
    }

    @Test
    void testInvalidAction() {
        Message message = new Message(
                1, "+27716893002", "Test message"
        );

        assertEquals(
                "Invalid option. Please choose 1, 0, or 2.",
                message.SentMessage(9)
        );
    }
}
