import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PlayerClassTest {

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
        System.gc();
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
            assertEquals(player.draw(new Card(1)).getValue(), 1);
            assertEquals(player.newCard.getValue(), 1);
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
            assertEquals(player.draw(new Card(1)).getValue(), 2);

            assertEquals(player.oldHand[0].getValue(), 1);
            assertEquals(player.oldHand[1].getValue(), 2);
            assertEquals(player.oldHand[2].getValue(), 3);
            assertEquals(player.oldHand[3].getValue(), 4);

            assertEquals(player.newCard.getValue(), 1);
            assertEquals(player.trashCard.getValue(), 2);
            assertEquals(player.hand[1].getValue(), 1);
            assertEquals(player.draw(new Card(8)).getValue(), 8);
            assertEquals(player.hand[0].getValue(), 1);
            assertEquals(player.hand[1].getValue(), 1);
            assertEquals(player.hand[2].getValue(), 3);
            assertEquals(player.hand[3].getValue(), 4);

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
            assertEquals(player.draw(new Card(1)).getValue(), 2);

            assertEquals(player.hand[0].getValue(), 1);
            assertEquals(player.hand[1].getValue(), 1);
            assertEquals(player.hand[2].getValue(), 3);
            assertEquals(player.hand[3].getValue(), 4);
            player.rollback();
            assertEquals(player.oldHand, player.hand);

            assertEquals(player.hand[0].getValue(), 1);
            assertEquals(player.hand[1].getValue(), 2);
            assertEquals(player.hand[2].getValue(), 3);
            assertEquals(player.hand[3].getValue(), 4);
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
            assertEquals(player.previousTurn[0], ("player 1 draws a 1 from deck 1\n"));
            assertEquals(player.previousTurn[1], ("player 1 discards a 2 to deck 2\n"));
            assertEquals(player.previousTurn[2], ("player 1 current hand is 1 1 3 4\n"));
            player.draw(new Card(1));
            player.logTurn(2);
            assertEquals(player.previousTurn[0], ("player 1 draws a 1 from deck 1\n"));
            assertEquals(player.previousTurn[1], ("player 1 discards a 3 to deck 2\n"));
            assertEquals(player.previousTurn[2], ("player 1 current hand is 1 1 1 4\n"));
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