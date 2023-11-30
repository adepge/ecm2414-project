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
    public int turnCount;
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
        turnCount = 0;
        FileWriter out = new FileWriter("output/player" + this.index + "_test_output.txt", false);
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
        for (int i = 0; i < hand.length; i++) {
            // Discards the first card in hand which its face value does not match the player's index
            if (hand[i].getValue() != index) {
                trashCard = hand[i];
                hand[i] = newCard;
                return trashCard;
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

    public void logInitialHand(){
        try {
            FileWriter out = new FileWriter("output/player" + index + "_test_output.txt", true);
            out.append(String.format("player %1$d initial hand %2$d %3$d %4$d %5$d\n",
                    index, hand[0].getValue(), hand[1].getValue(), hand[2].getValue(), hand[3].getValue()));
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void logTurn(int playerCount) {
        try {
            if(!(previousTurn[0] == null)) {
                FileWriter out = new FileWriter("output/player" + index + "_test_output.txt", true);
                out.append(previousTurn[0]);
                out.close();
            }
            previousTurn[0] = String.format("""
                            player %1$d draws a %2$d from deck %1$d
                            player %1$d discards a %3$d to deck %4$d
                            player %1$d current hand is %5$d %6$d %7$d %8$d
                            """,
                    index, newCard.getValue(), trashCard.getValue(), index % playerCount + 1, hand[0].getValue(), hand[1].getValue(), hand[2].getValue(), hand[3].getValue());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void logWin(int winningPlayer) {
        try {
            FileWriter out = new FileWriter("output/player" + index + "_test_output.txt", true);
            if (winningPlayer == index) {
                out.append(String.format("""
                                player %1$d wins
                                player %1$d exits
                                player %1$d final hand: %2$d %3$d %4$d %5$d
                                """,
                        index, hand[0].getValue(), hand[1].getValue(), hand[2].getValue(), hand[3].getValue()));
            } else {
                out.append(String.format("""
                                player %2$d has informed player %1$d that player %2$d has won
                                player %1$d exits
                                player %1$d final hand: %3$d %4$d %5$d %6$d
                                """,
                        index, winningPlayer, hand[0].getValue(), hand[1].getValue(), hand[2].getValue(), hand[3].getValue()));
            }
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}