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

    public CardGameClass(int playerCount, String deckFileName) throws IOException, InterruptedException{
        playerWon = 0;
        this.playerCount = playerCount;
        players = new PlayerClass[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new PlayerClass(i);
        }
        try {
            distributeCards(loadPack(deckFileName));
        } catch (InvalidPackException e) {
            System.out.println(e.getMessage());
        }
    }

    public Card[] loadPack(String filename) throws IOException, InvalidPackException {
        int[] placeholder = new int[8 * playerCount]; // Placeholder array to hold integers read from text file
        Card[] pack = new Card[8 * playerCount];  // Card array to hold all 8n cards
        File obj = new File(filename);
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
            pack[i] = new Card(placeholder[i], i);
        }
        return pack;
    }

    private void distributeCards(Card[] pack) throws IOException{
        decks = new DeckClass[playerCount];
        for (int i=0;i<playerCount;i++){
            DeckClass deckObject = new DeckClass(i);
            decks[i] = deckObject;
         }

        for (int i = 0; i < 4 * playerCount; i++) {
            decks[i % playerCount].addCard(pack[i]);
        }
        for (int i = 4 * playerCount; i < 8 * playerCount; i++) { // i starts from index after all cards have been distributed to players hand
            players[i % playerCount].addToHand(pack[i], i / playerCount - 4);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        Scanner playerInput = new Scanner(System.in);
        System.out.println("Please enter the number of players:");
        int nPlayers = Integer.parseInt(playerInput.nextLine());
        playerInput.close();

        Scanner deckInput = new Scanner(System.in);
        System.out.println("Please enter location of pack to load:");
        String deckFile = deckInput.nextLine();
        deckInput.close();

        CardGameClass cardGame = new CardGameClass(4,deckFile);
        for (int i = 0; i<cardGame.playerCount; i++){
            int n = i;
            Thread playerThread = new Thread(new Runnable() {
                @Override
                public void run() {

//                    for (int i = 0;i<cardGame.playerCount;i++){
//                        try {
//                            cardGame.decks[i].logDeck();
//                        } catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
                    //Checks that the thread isn't interrupted and that nobody has won the game. if both are true, the while loop begins
                    while (cardGame.playerWon <= 0) {
                        cardGame.players[n].turnTaken = false;
                        // Checks if the player has won
                        if (cardGame.players[n].checkWin()) {
                            //Tells the game that this player is the winner
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
//                                        System.out.println("player " + (n+1) + " is stuck");
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
                            } catch (PackThresholdException ignored) {}
                        }
                    }
                    try {
                        // Checks if deck can be logged without rollback (decks are complete), does so if true. If false, a player has initiated an illegal turn (a turn after another player has declared they have won)
                        if (!cardGame.decks[n].logDeck(false)) {
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
