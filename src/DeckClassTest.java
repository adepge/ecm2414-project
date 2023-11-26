import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import java.io.IOException;
import static org.junit.Assert.*;

public class DeckClassTest {

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
        // Clear null values (garbage collection)
        System.gc();
    }

    @Test
    public void addCard() {
        MockDeckClass deck;
        try {
            // MockDeckClass has an identical addCard method, but has a public contents field
            deck = new MockDeckClass(1);

            // Invoke the method
            assert deck.addCard(new Card(2)); // Assert that the card can be offered to the deck when there is sufficient capacity
            assertNotNull(deck.contents.peek()); // Assert that the head of the queue has the Card object
            assertEquals(2, deck.contents.peek().getValue());
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            deck = null;
        }
    }
    @Test
    public void addCardOverflow() {
        DeckClass deck;
        deck = new DeckClass(1);
        deck.addCard(new Card(2));
        deck.addCard(new Card(2));
        deck.addCard(new Card(2));
        deck.addCard(new Card(2));

        // Invoke the target method
        assert !deck.addCard(new Card(2)); // Assert that the card cannot be offered because the deck is full
        deck = null;
    }

    @Test
    public void removeCard() {
        int[] cards = {1, 2, 3, 4};
        Card TrashCard;
        MockDeckClass deck;
        try {
            // Using MockDeckClass for getOldContents() and public contents field
            deck = new MockDeckClass(1);
            deck.setDeckContents(cards);
            // Invokes the method tested
            TrashCard = deck.removeCard();

            // Asserts there is one space free in the deck
            assertEquals(1, deck.contents.remainingCapacity());
            assertEquals(1, TrashCard.getValue());

            // Asserts that the oldContents variable is assigned properly
            assertEquals(1, deck.getOldContents()[0].getValue());
            assertEquals(2, deck.getOldContents()[1].getValue());
            assertEquals(3, deck.getOldContents()[2].getValue());
            assertEquals(4, deck.getOldContents()[3].getValue());

            // Asserts next card in deck is 2
            assertNotNull(deck.contents.peek());
            assertEquals(2, deck.contents.peek().getValue());
        } catch (IOException | PackThresholdException e) {
            e.printStackTrace();
            assert false;
        } finally {
            cards = null;
            deck = null;
            TrashCard = null;
        }
    }

    @Test(expected = PackThresholdException.class)
    public void removeCardUnderflow() throws PackThresholdException{
        DeckClass deck;
        deck = new DeckClass(1);
        deck.addCard(new Card(1));
        deck.addCard(new Card(1));
        deck.addCard(new Card(1));

        // Invokes the method tested
        deck.removeCard();
        // Expects PackThresholdException thrown
        deck = null;
    }

    @Test
    public void logDeck() {
        MockDeckClass deck;
        Card[] oldContents;
        try {
            deck = new MockDeckClass(1);
            deck.addCard(new Card(1));
            deck.addCard(new Card(2));
            deck.addCard(new Card(3));
            deck.addCard(new Card(4));

            // Invokes the method tested
            deck.removeCard();
            oldContents = deck.getOldContents();
            deck.logDeck(true);

            // Triggers the conditions for a rollback to occur
            assert deck.getContentsToPrint() == oldContents;
        } catch (IOException | PackThresholdException e) {
            e.printStackTrace();
            assert false;
        } finally {
            deck = null;
            oldContents = null;
        }
    }
}