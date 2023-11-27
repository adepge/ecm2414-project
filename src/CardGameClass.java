import java.io.File;
import java.util.Scanner;
import java.io.IOException;

/**
 * The CardGameClass is the concrete implementation of the {@link CardGame} interface.
 * This class is responsible for the management of the card game,
 * {@link Card} objects, {@link Deck} objects, and {@link Player} objects
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 11-11-2023
 */
public class CardGameClass implements CardGame
{
    int playerCount;
    private PlayerClass[] players;
    private DeckClass[] decks;
    private int playerWon;

    public CardGameClass(int playerCount, String deckFileName) throws IOException, InvalidPackException{
        playerWon = 0;
        this.playerCount = playerCount;
        players = new PlayerClass[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new PlayerClass(i+1);
        }
        distributeCards(loadPack(deckFileName));
    }

    public Card[] loadPack(String filename) throws IOException, InvalidPackException {
        int[] placeholder = new int[8 * playerCount]; // Placeholder array to hold integers read from text file
        Card[] pack = new Card[8 * playerCount];  // Card array to hold all 8n cards
        File obj = new File("pack/" +filename);
        Scanner myReader = new Scanner(obj);
        for (int i = 0; i < 8 * playerCount; i++) {
            if (myReader.hasNextLine()) {
                try {
                    int data = Integer.parseInt(myReader.nextLine());
                    if(data < 0){
                        throw new InvalidPackException("Pack contains negative integers");
                    }
                    placeholder[i] = data;
                } catch (NumberFormatException e){
                    throw new InvalidPackException("Pack contains non-integer types");
                }
            } else {
                throw new InvalidPackException("Pack length is too short"); // When text file lines < 8n
            }
        }
        if (myReader.hasNextLine()) {
            throw new InvalidPackException("Pack length is too long"); // When text file lines > 8n
        }
        for (int i = 0; i < 8 * playerCount; i++) {
            pack[i] = new Card(placeholder[i]);
        }
        return pack;
    }

    /**
     * Method to distribute a valid pack of cards into player's hands
     * and then into decks in a round-robin fashion
     * @param pack Array of {@link Card} objects
     */
    private void distributeCards(Card[] pack){
        decks = new DeckClass[playerCount];
        for (int i=0;i<playerCount;i++){
            DeckClass deckObject = new DeckClass(i+1);
            decks[i] = deckObject;
        }
        // Distribute cards to players in a round-robin fashion first
        for (int i = 0; i < 4 * playerCount; i++) {
            players[i % playerCount].addToHand(pack[i], i/playerCount);
        }
        // Then distributes cards to decks in a round-robin fashion
        for (int i = 4 * playerCount; i < 8 * playerCount; i++) { // i starts from index after all cards have been distributed to players hand
            decks[i % playerCount].addCard(pack[i]);
        }
    }

    public static void main(String[] args) throws IOException, InvalidPackException{
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the number of players:");
        int nPlayers = Integer.parseInt(input.nextLine());

        input = new Scanner(System.in);
        System.out.println("Please enter pack file (in pack directory) to load:");
        String deckFile = input.nextLine();
        input.close();

        CardGameClass cardGame = new CardGameClass(nPlayers,deckFile);
        for (int i = 0; i<cardGame.playerCount; i++){
            int n = i;
            cardGame.players[n].logInitialHand();
            Thread playerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Checks that the thread isn't interrupted and that nobody has won the game. The while loop begins
                    while (cardGame.playerWon <= 0) {
                        cardGame.players[n].turnTaken = false;
                        // Checks if the player has won
                        if (cardGame.players[n].checkWin()) {
                            // Tells the game that this player is the winner, and prints it to the console
                            cardGame.playerWon = n + 1;
                            cardGame.players[n].turnTaken = true;
                            System.out.println("player " + (cardGame.playerWon) + " wins");
                        }
                        // Checks that no player has won within the cardGame instance
                        if (cardGame.playerWon <= 0) {
                            // Tells the player to draw a card from their deck, and choose which card to discard
                            try {
                                Card discardCard;
                                discardCard = cardGame.players[n].draw(cardGame.decks[n].removeCard());
                                // Checks that no one has won and player has played a full turn (drawn and discarded a card successfully)
                                while (cardGame.playerWon <= 0 && !cardGame.players[n].turnTaken) {
                                    // Checks whether a player successfully discarded their chosen card to the next deck
                                    if (!cardGame.decks[(n + 1) % cardGame.playerCount].addCard(discardCard)) {
                                        cardGame.players[n].turnTaken = false;
                                    } else {
                                        cardGame.players[n].turnTaken = true;
                                    }
                                }
                                //Logs player's turn to their txt file.
                                if (cardGame.players[n].turnTaken && cardGame.playerWon<=0) {
                                        cardGame.players[n].logTurn(cardGame.playerCount);
                                }
                            } catch (PackThresholdException ignore) {}
                        }
                    }
                    // Each player logs their game over state, as well as their decks'
                        // Checks if deck can be logged without rollback (decks are complete), does so if true. If false, a player has initiated an illegal turn (a turn after another player has declared they have won)
                        if (!cardGame.decks[n].logDeck(false)) {
                            // Check if player has completed their illegal turn
                            if (cardGame.players[n].turnTaken){
                                cardGame.decks[n % cardGame.playerCount + 1].logDeck(true);
                            } else {
                                cardGame.players[n].logTurn(cardGame.playerCount);
                            }
                            // Rolls back their hand, undoing their illegal turn internally
                            cardGame.players[n].rollback();
                            cardGame.decks[n].logDeck(true);
                        } else {
                            cardGame.players[n].logTurn(cardGame.playerCount);
                        }
                        cardGame.players[n].logWin(cardGame.playerWon);
                }
            });
            playerThread.start();
        }
    }
}
