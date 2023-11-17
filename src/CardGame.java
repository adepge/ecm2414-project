import java.io.IOException;

/**
 * CardGame interface. This interface describes the methods
 * used for game operations: initialising, threading & closing conditions
 *
 * @author Adam George
 * @author Ben Ellison
 * @version 11-11-2023
 */
public interface CardGame {


    /**
     * Method for loading in a pack from a text file specified.
     * Validates each pack to make sure it has 8n rows and only consists of positive integers.
     *
     * @param filename
     * @return array of cards
     * @throws IOException
     * @throws InvalidPackException when validation fails
     */
    Card[] loadPack(String filename) throws IOException, InvalidPackException;
    static void startGame(String[] args){}

    private void distributeCards(Card[] pack){}
    public static void main(String[] args) {}
}
