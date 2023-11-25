import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * The CardGameClass is the concrete implementation of the {@link CardGame} interface.
 * This class is responsible for the management of the card game,
 * {@link Card} objects, {@link Deck} objects, and {@link Player} objects
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 11-11-2023
 */
public class MockCardGameClass implements CardGame
{
    int playerCount;
    public MockPlayerClass[] players;
    public MockDeckClass[] decks;
    public int playerWon;

    public MockCardGameClass(int playerCount, String deckFileName) throws IOException, InvalidPackException{
        playerWon = 0;
        this.playerCount = playerCount;
        players = new MockPlayerClass[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new MockPlayerClass(i+1);
        }
        distributeCards(loadPack(deckFileName));
    }

    public Card[] loadPack(String filename) throws IOException, InvalidPackException {
        int[] placeholder = new int[8 * playerCount]; // Placeholder array to hold integers read from text file
        Card[] pack = new Card[8 * playerCount];  // Card array to hold all 8n cards
        File obj = new File("pack/" + filename);
        Scanner myReader = new Scanner(obj);
        for (int i = 0; i < 8 * playerCount; i++) {
            if (myReader.hasNextLine()) {
                int data = Integer.parseInt(myReader.nextLine());
                placeholder[i] = data;
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

    private void distributeCards(Card[] pack) throws IOException{
        decks = new MockDeckClass[playerCount];
        for (int i=0;i<playerCount;i++){
            MockDeckClass deckObject = new MockDeckClass(i+1);
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


    public static void runGame(int nPlayers, String deckFile) throws IOException, InterruptedException, InvalidPackException{
        MockCardGameClass cardGame = new MockCardGameClass(nPlayers,deckFile);
        for (int i = 0; i<cardGame.playerCount; i++){
            int n = i;
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
                                    try {
                                        cardGame.players[n].logTurn(cardGame.playerCount);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        System.out.println("Unable to write to text file");
                                    }
                                }
                            } catch (PackThresholdException e) {System.out.println("Deck is not 4 cards");}
                        }
                    }
                    //Each player logs their game over state, as well as their decks'
                    try {
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
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("failed to log win");
                    }
                }
            });
            playerThread.join();
            playerThread.start();
        }
    }
}