import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

public class QuickChatAppTest {

    @BeforeEach
    public void setUp() {
        // Initialize/reset message lists before each test
        QuickChatApp.sentMessages = new ArrayList<>();
        QuickChatApp.storedMessages = new ArrayList<>();
        QuickChatApp.disregardedMessages = new ArrayList<>();

        // Add sample messages for testing
        QuickChatApp.sentMessages.add(new Message("+27834557896", "Did you get the cake?"));
        QuickChatApp.sentMessages.add(new Message("+27838884567", "Where are you? You are late! I have asked you to be on time."));
        QuickChatApp.sentMessages.add(new Message("+27834484567", "Yohoooo, I am at your gate."));
        QuickChatApp.sentMessages.add(new Message("08388884567", "It is dinner time!"));
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertEquals(4, QuickChatApp.sentMessages.size());
        assertEquals("Did you get the cake?", QuickChatApp.sentMessages.get(0).getMessage());
        assertEquals("It is dinner time!", QuickChatApp.sentMessages.get(3).getMessage());
    }

    @Test
    public void testLongestMessage() {
        String longest = "";
        for (Message m : QuickChatApp.sentMessages) {
            if (m.getMessage().length() > longest.length()) {
                longest = m.getMessage();
            }
        }
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    public void testSearchByMessageID_Valid() {
        Message target = QuickChatApp.sentMessages.get(3); // "It is dinner time!"
        String id = target.getMessageID();

        boolean found = false;
        for (Message m : QuickChatApp.sentMessages) {
            if (m.getMessageID().equals(id)) {
                assertEquals("08388884567", m.getRecipientCell());
                assertEquals("It is dinner time!", m.getMessage());
                found = true;
            }
        }
        assertTrue(found, "Message with valid ID should be found.");
    }

    @Test
    public void testSearchByMessageID_Invalid() {
        String invalidID = "0000000000";
        boolean found = false;
        for (Message m : QuickChatApp.sentMessages) {
            if (m.getMessageID().equals(invalidID)) {
                found = true;
            }
        }
        assertFalse(found, "Message with invalid ID should not be found.");
    }

    @Test
    public void testDeleteByHash() {
        String hashToDelete = QuickChatApp.sentMessages.get(0).getMessageHash();
        int initialSize = QuickChatApp.sentMessages.size();

        boolean removed = QuickChatApp.sentMessages.removeIf(m -> m.getMessageHash().equals(hashToDelete));

        assertTrue(removed, "Message should be removed.");
        assertEquals(initialSize - 1, QuickChatApp.sentMessages.size());
    }
}
