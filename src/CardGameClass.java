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
    private boolean playerWon;

    public CardGameClass(int playerCount, String deckFileName) throws IOException {
        playerWon = false;
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

    private void distributeCards(Card[] pack) {
        this.decks = new DeckClass[playerCount];
        for (int i=0;i<playerCount;i++){
            decks[i] = new DeckClass();
        }

        for (int i = 0; i < 4 * playerCount; i++) {
            decks[i % playerCount].addCard(pack[i]);
        }
        for (int i = 4 * playerCount; i < 8 * playerCount; i++) { // i starts from index after all cards have been distributed to players hand
            players[i % playerCount].addToHand(pack[i], i / playerCount - 4);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException{
//        Scanner playerInput = new Scanner(System.in);
//        System.out.println("Please enter the number of players:");
//        int nPlayers = Integer.parseInt(playerInput.nextLine());
//        playerInput.close();

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

                    while(!Thread.currentThread().isInterrupted() && !cardGame.playerWon){
                        System.out.println(String.valueOf(cardGame.decks[n]));
                        if (cardGame.players[n].checkWin()){
                            cardGame.playerWon = true;
                            Thread.currentThread().interrupt();
                        }
                        Card discardCard = cardGame.players[n].draw(cardGame.decks[n].removeCard());
                        cardGame.decks[(n + 1) % cardGame.playerCount].addCard(discardCard);
                        try{
                            cardGame.players[n].logTurn(cardGame.playerCount);
                        } catch (IOException e){
                            e.printStackTrace();
                            System.out.println("Unable to write to text file");
                        }
                    }

                }
            });
            playerThread.start();
            playerThread.join();
        }
    }
}
