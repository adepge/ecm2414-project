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

        /**
         * Method to add a {@link Card} object to the deck
         * @param c Card to be added to deck
         * @return true if card was added successfully, false if deck was full
         */
        boolean addCard(Card c);

        /**
         * Method which removes a {@link Card} object from the deck
         * @return Card drawn from deck
         * @throws PackThresholdException if Card was drawn when capacity is insufficient
         */
        Card removeCard() throws PackThresholdException;

        /**
         * Method which logs the deck into a text file
         * @param rollback if player performed an invalid turn
         * @return true if deck logged successfully, false if deck logged when empty
         */
        boolean logDeck(boolean rollback);
}
