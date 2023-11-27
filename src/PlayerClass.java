import java.io.*;
public class PlayerClass implements Serializable, Player{
    /** Player index, indicates their position relative to card decks */
    private int index;
    public boolean turnTaken;
    private String[] previousTurn = new String[1];

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

    public void logInitialHand(){
        try {
            FileWriter out = new FileWriter("output/player" + index + "_output.txt", true);
            out.append(String.format("player %1$d initial hand %2$d %3$d %4$d %5$d\n",
                   index, hand[0].getValue(), hand[1].getValue(), hand[2].getValue(), hand[3].getValue()));
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
//        if (newCard.getValue() == index) {
            for (int i = 0; i < hand.length; i++) {
                // Discards the first card in hand which its face value does not match the player's index
                if (hand[i].getValue() != index) {
                    trashCard = hand[i];
                    hand[i] = newCard;
                    return trashCard;
                }
            }
//        }
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
    public void rollback(){hand = oldHand.clone();}

    public void logTurn(int playerCount) {
        try {
            if(previousTurn[0] != null) {
                FileWriter out = new FileWriter("output/player" + index + "_output.txt", true);
                out.append(previousTurn[0]);
                out.close();
            }
            if (newCard != null) {
                previousTurn[0] = String.format("""
                                player %1$d draws a %2$d from deck %1$d
                                player %1$d discards a %3$d to deck %4$d
                                player %1$d current hand is %5$d %6$d %7$d %8$d
                                """,
                        index, newCard.getValue(), trashCard.getValue(), index % playerCount + 1, hand[0].getValue(), hand[1].getValue(), hand[2].getValue(), hand[3].getValue());
            }
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void logWin(int winningPlayer) {
        try {
            FileWriter out = new FileWriter("output/player" + index + "_output.txt", true);
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