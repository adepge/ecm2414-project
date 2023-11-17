import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class DeckClass {
    private int index;
    private ArrayBlockingQueue<Card> contents = new ArrayBlockingQueue<>(4);

    int getIndex(){return index;};

    void addCard(Card c){
        contents.add(c);
    }

    Card removeCard(){return contents.poll();};

    void clearContents(Card c){contents.clear();};

    void logDeck() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("deck" + index + "_output.txt"));
        out.writeObject("deck" + index + " contents: " + contents.toString());
    }
}