import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void getValue() {
        Card card = new Card(1);
        assertEquals(1, card.getValue());
    }

    @Test
    public void getMaxIntegerValue() {
        Card card = new Card(2147483647);
        assertEquals(2147483647, card.getValue());
    }
}