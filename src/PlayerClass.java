import java.io.*;
public class PlayerClass implements Serializable, Player{
    /** Player index, indicates their position relative to card decks */
    private int index;
    public boolean turnTaken;
    private String[] previousTurn = new String[3];

    /** Player's hand of 4 cards */
    private Card[] hand = new Card[4];
    private Card[] oldHand = new Card[4];

    /** Card */
    private Card newCard;
    private Card trashCard;

    /**
     * Constructor creates a {@link Player} object and assigns its index
     * @param index Unique numerical identifier for each player
     */
    public PlayerClass(int index) throws IOException{

        this.index = index;
        FileWriter out = new FileWriter("output/player" + this.index + "_output.txt", false);
        out.close();
    }

    public void addToHand(Card c, int position) {
        hand[position] = c;
    }

    public Card draw(Card c) {
        newCard = c;
        return chooseDiscard();
    }

    private Card chooseDiscard() {
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