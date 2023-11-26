/**
 * Class for Card objects which contain a face {@link Card#value}.
 */
public class Card {

    private final int value;

    public Card (int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
