import java.io.IOException;

public interface Player {

    /**
     * @return player's hand
     */
    String getHand();

    /**
     * Method which adds a {@link Card} object to the player's hand
     * @param c Card
     */
    void addToHand(Card c, int position);

    int getIndex();
    /**
     * Method which draws a card from the deck to their left
     * and returns the card they will discard into the deck to their right
     *
     * @param c Card drawn from deck {index}
     * @return Card to be discarded to deck {index+1}
     */
    Card draw(Card c);
    /**
     * Method which a player chooses which card to discard out of their hand
     * Assigns a sequentially incrementing account id.
     *
     * @return Card chosen to be discarded from hand
     */
    Card chooseDiscard();
    /**
     * Checks if the player has won or not
     * @return True if the player has won, false if not
     */
    boolean checkWin();
    /**
     * Method which logs the players actions into a text file
     * with the name player{index}_output.txt
     *
     * @param playerCount total number of players in game
     * @throws IOException file error
     */
    void rollback();
    void logTurn(int playerCount) throws IOException;

    void logWin(int winningPlayer) throws IOException;
}
