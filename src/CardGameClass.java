import java.io.File;
import java.util.Arrays;
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

    public CardGameClass(int playerCount, String deckFileName) throws IOException, InterruptedException{
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
//        System.out.println(Arrays.toString(placeholder));
        for (int i = 0; i < 8 * playerCount; i++) {
            pack[i] = new Card(placeholder[i], i);
        }
//        for (int i = 0; i < 8 * playerCount; i++) {
//            System.out.println(pack[i].getValue());
//        }
        return pack;
    }

    private void distributeCards(Card[] pack) throws InterruptedException{
        decks = new DeckClass[playerCount];
        for (int i=0;i<playerCount;i++){
            DeckClass deckObject = new DeckClass(i);
            decks[i] = deckObject;
         }

        for (int i = 0; i < 4 * playerCount; i++) {
            System.out.println(pack[i].getValue());
            decks[i % playerCount].addCard(pack[i]);
        }


//        for (int i=0;i<playerCount;i++) {
//            System.out.println("Deck "+ i);
//            DeckClass sampleDeck = decks[i];
//            for (int j = 0; j < playerCount; j++) {
//                System.out.println(sampleDeck.removeCard().getValue());
//            }
//        }

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
//        Thread[] playerThreads = new Thread[cardGame.playerCount];
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
                        try {
                            cardGame.decks[n].logDeck();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    while(!Thread.currentThread().isInterrupted() && !cardGame.playerWon){



                        if (cardGame.players[n].checkWin()){
                            cardGame.playerWon = true;
                            Thread.currentThread().interrupt();
                        }
//                        if (cardGame.decks[n].checkForTurn()) {
                            Card discardCard = cardGame.players[n].draw(cardGame.decks[n].removeCard());
                            try {
                                cardGame.decks[(n + 1) % cardGame.playerCount].addCard(discardCard);
                            } catch (InterruptedException e) {
                                System.out.println("GAME END");
                            }
                            try {
                                cardGame.players[n].logTurn(cardGame.playerCount);
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("Unable to write to text file");
                            }
//                        } else{
//                            System.out.println("player " + (n+1) + " cannot play" );
//                        }
                    }

                }
            });

            playerThread.join();
            playerThread.start();

        }

    }
}
