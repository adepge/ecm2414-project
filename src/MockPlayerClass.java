import java.io.*;
import java.lang.reflect.Array;

/**
 * Mock object class based on {@link PlayerClass}. This class has identical method implementations
 * for all methods in {@link PlayerClass}, but additional methods to set fields in the object as
 * well as different access modifiers
 */
public class MockPlayerClass implements Serializable, Player{
    /** Player index, indicates their position relative to card decks */
    public int index;
    public boolean turnTaken;
    public String[] previousTurn = new String[3];

    /** Player's hand of 4 cards */
    public Card[] hand = new Card[4];
    public Card[] oldHand = new Card[4];

    /** Card */
    public Card newCard;
    public Card trashCard;

    /**
     * Constructor creates a {@link Player} object and assigns its index
     * @param index Unique numerical identifier for each player
     */
    public MockPlayerClass(int index) throws IOException{
        this.index = index;
        FileWriter out = new FileWriter("output/player" + this.index + "_output.txt", false);
        out.close();
    }

    public void setHand(int[] cards){
        for (int i = 0; i<4;i++){
            addToHand(new Card(cards[i]), i);
        }
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public Card getNewCard(){
        return newCard;
    }

    public Card getTrashCard(){
        return trashCard;
    }

    public void addToHand(Card c, int position) {
        hand[position] = c;
    }

    public Card draw(Card c) {
        newCard = c;
        return chooseDiscard();
    }

    public Card chooseDiscard() {
        // Stores previous version of players hand
        oldHand = hand.clone();
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

    // Restores previous hand
    public void rollback(){
        hand = oldHand.clone();
        // Remember to remove this debug code later
        System.out.println("rollback occurred for player " + index);
    }

    public void logTurn(int playerCount) throws IOException {
        if (!(previousTurn[0] == null)){
            FileWriter out = new FileWriter("output/player" + index + "_output.txt", true);
            out.append(previousTurn[0]);
            out.append(previousTurn[1]);
            out.append(previousTurn[2]);
            out.close();
        }
        previousTurn[0] = ("player " + (index) + " draws a " + (newCard.getValue()) + " from deck " + (index) + "\n");
        previousTurn[1] = ("player " + (index) + " discards a " + (trashCard.getValue()) + " to deck " + (index % playerCount + 1) + "\n");
        previousTurn[2] = ("player " + (index) + " current hand is " + (hand[0].getValue()) + " " + (hand[1].getValue()) + " " + (hand[2].getValue()) + " " + (hand[3].getValue()) + "\n");
    }

    public void logWin(int winningPlayer) throws IOException {
        FileWriter out = new FileWriter("output/player" + index + "_output.txt", true);
        if (winningPlayer == index) {
            out.append("player " + (index) + " wins\n");
            out.append("player " + (index) + " exits\n");
            out.append("player " + (index) + " hand: " + (hand[0].getValue()) + " " + (hand[1].getValue()) + " " + (hand[2].getValue()) + " " + (hand[3].getValue()) + "\n");
        } else {
            out.append("player " + (winningPlayer) + " has informed player " + (index) + " that player " + (winningPlayer) + " has won\n");
            out.append("player " + (index) + " exits\n");
            out.append("player " + (index) + " hand: " + (hand[0].getValue()) + " " + (hand[1].getValue()) + " " + (hand[2].getValue()) + " " + (hand[3].getValue()) + "\n");
        }
        out.close();
    }

}