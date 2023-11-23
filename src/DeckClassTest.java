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
    }

    @Test
    public void getIndex() {
        try {
            DeckClass deck = new DeckClass(1);
            assertEquals(deck.getIndex(), 1);
        } catch (IOException ignored){}
    }

    @Test
    public void addCard() {
        try {
            // MockDeckClass has an identical addCard method, but has a public contents field
            MockDeckClass deck = new MockDeckClass(1);
            assert deck.addCard(new Card(2));
            assert deck.contents.peek() != null;
            assertEquals(deck.contents.peek().getValue(),2);
        } catch (IOException ignored){}
    }
    @Test
    public void addCardOverflow() {
        try {
            DeckClass deck = new DeckClass(1);
            deck.addCard(new Card(2));
            deck.addCard(new Card(2));
            deck.addCard(new Card(2));
            deck.addCard(new Card(2));
            assert !deck.addCard(new Card(2));
        } catch (IOException ignored){}
    }

    @Test
    public void removeCard() {
        try {
            // Using MockDeckClass for getOldContents() and public contents field
            int[] cards = {1, 2, 3, 4};
            MockDeckClass deck = new MockDeckClass(1);
            deck.setDeckContents(cards);
            // Invokes the method tested
            Card TrashCard = deck.removeCard();

            // Asserts there is one space free in the deck
            assertEquals(deck.contents.remainingCapacity(),1);
            assertEquals(TrashCard.getValue(),1);

            // Asserts that the oldContents variable is assigned properly
            assertEquals(deck.getOldContents()[0].getValue(),1);
            assertEquals(deck.getOldContents()[1].getValue(),2);
            assertEquals(deck.getOldContents()[2].getValue(),3);
            assertEquals(deck.getOldContents()[3].getValue(),4);

            // Asserts next card in deck is 2
            assert deck.contents.peek() != null;
            assertEquals(deck.contents.peek().getValue(), 2);
        } catch (IOException | PackThresholdException ignored) {
        }
    }

    @Test
    public void removeCardUnderflow() {
        try {
            DeckClass deck = new DeckClass(1);
            deck.addCard(new Card(1));
            deck.addCard(new Card(1));
            deck.addCard(new Card(1));

            // Invokes the method tested
            assertThrows(PackThresholdException.class, (ThrowingRunnable) deck.removeCard());
            // Expects PackThresholdException thrown
        } catch (IOException ignored) {
        } catch (PackThresholdException e){
            System.out.println("PackThresholdException thrown");
            assert false;
        }
    }

    @Test
    public void logDeck() {
        try {
            MockDeckClass deck = new MockDeckClass(1);
            deck.addCard(new Card(1));
            deck.addCard(new Card(2));
            deck.addCard(new Card(3));
            deck.addCard(new Card(4));

            // Invokes the method tested
            deck.removeCard();
            Card[] oldContents = deck.getOldContents();
            deck.logDeck(true);

            // Triggers the conditions for a rollback to occur
            assert deck.getContentsToPrint() == oldContents;
        } catch (IOException | PackThresholdException ignored) {}
    }
}