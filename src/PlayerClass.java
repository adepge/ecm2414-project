import java.io.*;
public class PlayerClass implements Serializable, Player{
    /** Player index, indicates their position relative to card decks */
    private int index;

    /** Player's hand of 4 cards */
    private Card[] hand = new Card[4];

    /** Card */
    private Card newCard;
    private Card trashCard;

    /**
     * Constructor creates a {@link Player} object and assigns its index
     * @param index Unique numerical identifier for each player
     */
    public PlayerClass(int index){
        this.index = index + 1 ;
    }

    public String getHand(){
        String handString = new String();
        for (int i = 0; i<4;i++){
            handString.concat(hand[i].getValue() + " ");
        }
        return handString;
    }

    public int getIndex(){
        return index;
    }

    public void addToHand(Card c, int position) {
        hand[position] = c;
    }

    public Card draw(Card c) {
        newCard = c;
        return chooseDiscard();
    }

    public Card chooseDiscard() {
        if (newCard.getValue() == index) {
            for (int i = 0; i < hand.length; i++) {
                // Discards the first card in hand which its face value does not match the player's index
                if (hand[i].getValue() != index) {
                    trashCard = hand[i];
                    hand[i] = newCard;
                    return trashCard;
                }
            }
        }
        trashCard = newCard;
        return trashCard;
    }

    public boolean checkWin() {
        int numToCheck = hand[0].getValue();
        for (int i = 1; i < hand.length; i++) {
            // Checks each card sequentially for equality, if all are equal, the game is won
            if (hand[i].getValue() != numToCheck) {
                return false;
            }
        }
        return true;
    }

    public void logTurn(int playerCount) throws IOException {
        FileWriter out = new FileWriter("player" + index + "_output.txt", true);
        out.append("player " + (index) + " draws a " + (newCard.getValue()) + " from deck " + (index) + "\n");
        out.append("player " + (index) + " discards a " + (trashCard.getValue()) + " to deck " + (index % playerCount + 1) + "\n");
        out.append("player " + (index) + " current hand is " + (hand[0].getValue()) + " " + (hand[1].getValue()) + " " + (hand[2].getValue()) + " " + (hand[3].getValue()) + "\n");
        out.close();
    }
}