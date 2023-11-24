import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void getValue() {
        Card card = new Card(1);
        assert card.getValue() == 1;
    }

    @Test
    public void getMaxIntegerValue() {
        Card card = new Card(2147483647);
        assert card.getValue() == 2147483647;
    }
}