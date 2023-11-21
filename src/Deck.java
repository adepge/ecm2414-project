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
        boolean addCard(Card c);
        boolean checkForTurn();
        Card removeCard() throws PackThresholdException;
        void clearContents(Card c);
        boolean logDeck(boolean rollback) throws IOException;
}
