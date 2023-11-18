import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Deck interface. This interface describes the methods
 * used in card-object interaction.
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 11-11-2023
 */
public interface Deck{

        int getIndex();
        void addCard(Card c) throws InterruptedException;
        boolean checkForTurn();
        Card removeCard();
        void clearContents(Card c);
        void logDeck() throws IOException;
}
