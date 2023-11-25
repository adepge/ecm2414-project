import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CardGameClassTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception{
        System.gc();
    }

    @Test
    public void packDistributionTest() {
        MockCardGameClass cardGame;
        try{
            // Invoke a new card game of 4 players instance with pack2.txt (all cards are ordered by size)
            cardGame = new MockCardGameClass(4,"pack2.txt");

            assertEquals(4, cardGame.players.length);
            assertEquals(4, cardGame.decks.length);

            // Makes sure that every player hand and deck has the same number of cards (4)
            for(int i = 0; i < 4; i++){
                assertEquals(4,cardGame.players[i].hand.length);
                assertEquals(4,cardGame.decks[i].contents.size());

                Card[] deckArray = cardGame.decks[i].contents.toArray(new Card[4]);
                    // Asserts the contents of each player's hand have been distributed in round-robin fashion
                    assertEquals(1,cardGame.players[i].hand[0].getValue());
                    assertEquals(2,cardGame.players[i].hand[1].getValue());
                    assertEquals(3,cardGame.players[i].hand[2].getValue());
                    assertEquals(4,cardGame.players[i].hand[3].getValue());

                    // Asserts the contents of each deck are assigned in round-robin fashion
                    assertEquals(5,deckArray[0].getValue());
                    assertEquals(6,deckArray[1].getValue());
                    assertEquals(7,deckArray[2].getValue());
                    assertEquals(8,deckArray[3].getValue());

            }
            // Invokes the method only
            Card[] pack = cardGame.loadPack("pack.txt");
            assertEquals(32, pack.length);


        } catch (IOException | InvalidPackException e){
            e.printStackTrace();
            assert false;
        } finally {
            cardGame = null;
        }
    }
}