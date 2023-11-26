import jdk.jfr.consumer.RecordedStackTrace;

import java.io.IOException;

public interface Player {


    /**
     * Method which adds a {@link Card} object to the player's hand
     * @param c Card
     */
    void addToHand(Card c, int position);

    /**
     * Method which draws a card from the deck to their left
     * and returns the card they will discard into the deck to their right
     *
     * @param c Card drawn from deck {index}
     * @return Card to be discarded to deck {index+1}
     */
    Card draw(Card c);

    /**
     * Checks if the player has won or not
     * @return True if the player has won, false if not
     */
    boolean checkWin();

    /**
     * Rolls back the player's hand if they have completed an illegal turn
     */
    void rollback();

    /**
     * Method which logs the players actions into a text file
     * with the name player{index}_output.txt
     *
     * @param playerCount total number of players in game
     */
    void logTurn(int playerCount);

    /**
     * Appends winning turn message to the player's log files
     * @param winningPlayer index of player who has won
     */
    void logWin(int winningPlayer);
}
