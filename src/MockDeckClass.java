import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Mock object class based on {@link DeckClass}. This class has identical method implementations
 * for all methods in {@link DeckClass}, but additional methods to set fields in the object
 */
public class MockDeckClass implements Deck {
    private int index;
    public ArrayBlockingQueue<Card> contents = new ArrayBlockingQueue<>(4);
    protected boolean rolledBack = false;
    private Card[] oldContents;
    private Card[] contentsToPrint;

    public void setIndex(int index) {
        this.index = index;
    }

    public MockDeckClass(int index) throws IOException {
        this.index = index;
        // Clear previous output instances of decks with this index
        FileWriter out = new FileWriter("output/deck" + this.index + "_test_output.txt", false);
        out.close();
    }

    public Card[] getOldContents() {
        return oldContents;
    }

    public Card[] getContentsToPrint() {
        return contentsToPrint;
    }

    public void setDeckContents(int[] cards) {
        contents.clear();
        for (int i = 0; i < 4; i++) {
            contents.add(new Card(cards[i]));
        }
    }

    public boolean checkForTurn() {
        if (contents.remainingCapacity() > 1) {
            return false;
        }
        return true;
    }

    public boolean addCard(Card c) {
        return contents.offer(c);
    }

    public Card removeCard() throws PackThresholdException {
        if (contents.remainingCapacity() > 0) {
            throw new PackThresholdException();
        }
        oldContents = contents.toArray(new Card[4]);
        return contents.poll();
    }

    public void clearContents(Card c) {
        contents.clear();
    }

    public boolean logDeck(boolean rollback){
        if (rollback){
            if (contents.remainingCapacity() > 0){ rolledBack = true; }
            contentsToPrint = oldContents;
        } else {
            if (contentsToPrint == null) {
                contentsToPrint = contents.toArray(new Card[4]);
            } else {
                // Shifts all elements by 1
                for (int i = 2; i > -1; i--) {
                    contentsToPrint[i + 1] = contentsToPrint[i];
                }
                contentsToPrint[0] = oldContents[0];
            }
        }
        if (contentsToPrint[3] == null) {
            return false;
        } else {
            try {
                FileWriter out = new FileWriter("output/deck" + index + "_test_output.txt", false);
                out.append(String.format("deck %1$d contents: %2$d %3$d %4$d %5$d ",
                        index,contentsToPrint[0].getValue(),contentsToPrint[1].getValue(),contentsToPrint[2].getValue(),contentsToPrint[3].getValue()));
                out.close();
            } catch (IOException e){
                // Exception handled if FileWriter fails
                System.out.println(e.getMessage());
            }
            return true;
        }
    }
}