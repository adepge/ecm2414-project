import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.File;

import static org.junit.Assert.*;

public class PlayerClassTest {

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
        System.gc();
        File outputfile = new File("output/player1_test_output.txt");
        outputfile.delete();
    }

    @Test
    public void addToHand() {
        MockPlayerClass player;
        try {
            player = new MockPlayerClass(1);
            player.addToHand(new Card(1), 0);
            player.addToHand(new Card(2), 1);
            player.addToHand(new Card(3), 2);
            player.addToHand(new Card(4), 3);
            for (int i = 0; i < 4; i++) {
                assertEquals(player.hand[i].getValue(), i + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }

    @Test
    public void draw() {
        MockPlayerClass player;
        try {
            player = new MockPlayerClass(1);
            player.setHand(new int[]{1, 1, 1, 1});
            assertEquals(1, player.draw(new Card(1)).getValue());
            assertEquals(1, player.newCard.getValue());
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }

    @Test
    public void chooseDiscard() {
        MockPlayerClass player;
        try {
            player = new MockPlayerClass(1);
            player.setHand(new int[]{1, 2, 3, 4});
            assertNull(player.newCard);
            assertNull(player.trashCard);

            //Method is invoked by calling the draw() method
            assertEquals(2, player.draw(new Card(1)).getValue());

            assertEquals(1, player.oldHand[0].getValue());
            assertEquals(2, player.oldHand[1].getValue());
            assertEquals(3, player.oldHand[2].getValue());
            assertEquals(4, player.oldHand[3].getValue());

            assertEquals(1, player.newCard.getValue());
            assertEquals(2, player.trashCard.getValue());
            assertEquals(1, player.hand[1].getValue());

            assertEquals(3, player.draw(new Card(8)).getValue());
            assertEquals(1, player.hand[0].getValue());
            assertEquals(1, player.hand[1].getValue());
            assertEquals(8, player.hand[2].getValue());
            assertEquals(4, player.hand[3].getValue());

        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }

    @Test
    public void checkWin() {
        MockPlayerClass player;
        try {
            player = new MockPlayerClass(1);
            player.setHand(new int[]{1, 1, 1, 1});
            assert player.checkWin();
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }

    @Test
    public void checkNonIndexWin() {
        MockPlayerClass player;
        try {
            player = new MockPlayerClass(1);
            player.setHand(new int[]{2, 2, 2, 2});
            assert player.checkWin();
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }

    @Test
    public void rollback() {
        MockPlayerClass player;
        try {
            player = new MockPlayerClass(1);
            player.setHand(new int[]{1, 2, 3, 4});
            assertEquals(2, player.draw(new Card(1)).getValue());

            assertEquals(1, player.hand[0].getValue());
            assertEquals(1, player.hand[1].getValue());
            assertEquals(3, player.hand[2].getValue());
            assertEquals(4, player.hand[3].getValue());
            player.rollback();
            assertEquals(player.oldHand, player.hand);

            assertEquals(1, player.hand[0].getValue());
            assertEquals(2, player.hand[1].getValue());
            assertEquals(3, player.hand[2].getValue());
            assertEquals(4, player.hand[3].getValue());
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }

    }

    @Test
    public void logInitialHand() {
        PlayerClass player;
        try{
            player = new PlayerClass(1);
            player.addToHand(new Card(1), 0);
            player.addToHand(new Card(2), 1);
            player.addToHand(new Card(3), 2);
            player.addToHand(new Card(4), 3);
            player.logInitialHand();
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }

    @Test
    public void logTurn() {
        MockPlayerClass player;
        try {
            player = new MockPlayerClass(1);
            player.addToHand(new Card(1), 0);
            player.addToHand(new Card(2), 1);
            player.addToHand(new Card(3), 2);
            player.addToHand(new Card(4), 3);
            player.draw(new Card(1));
            player.logTurn(2);
            assertEquals("""
            player 1 draws a 1 from deck 1
            player 1 discards a 2 to deck 2
            player 1 current hand is 1 1 3 4
            """
            , player.previousTurn[0]);
            player.draw(new Card(1));
            player.logTurn(2);
            assertEquals("""
            player 1 draws a 1 from deck 1
            player 1 discards a 3 to deck 2
            player 1 current hand is 1 1 1 4
            """, player.previousTurn[0]);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }

    @Test
    public void logWin() {
        PlayerClass player;
        try {
            player = new PlayerClass(1);
            player.addToHand(new Card(1), 0);
            player.addToHand(new Card(1), 1);
            player.addToHand(new Card(1), 2);
            player.addToHand(new Card(1), 3);
            player.logWin(1);
            player.logWin(2);
        } catch (IOException e) {
            e.printStackTrace();
            assert false;
        } finally {
            player = null;
        }
    }
}