import java.io.*;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


public class DeckClass implements Deck{
    private int index;
    protected boolean rolledBack = false;
    private Card[] oldContents;
    private ArrayBlockingQueue<Card> contents = new ArrayBlockingQueue<>(4);
    private Card[] contentsToPrint;

    public DeckClass(int index){
        this.index = index;
        // Clear previous output instances of decks with this index
        try {
            FileWriter out = new FileWriter("output/deck" + this.index + "_output.txt", false);
            out.close();
        } catch (IOException e){
            // Exception handled if FileWriter fails
            System.out.println(e.getMessage());
        }
    }


    public boolean addCard(Card c){
        return contents.offer(c);
    }

    public Card removeCard() throws PackThresholdException{
        if (contents.remainingCapacity() > 0){
            throw new PackThresholdException();
        }
        oldContents = contents.toArray(new Card[4]);
        return contents.poll();}

    public boolean logDeck(boolean rollback){
        if (rollback){
            if (contents.remainingCapacity() > 0){ rolledBack = true; }
            contentsToPrint = oldContents;
        } else {
            if (contentsToPrint == null) {
                contentsToPrint = contents.toArray(new Card[4]);
            } else {
                // Shifts all elements by 1
                for (int i = 2; i > -1; i--) {
                    contentsToPrint[i + 1] = contentsToPrint[i];
                }
                contentsToPrint[0] = oldContents[0];
            }
        }
        if (contentsToPrint[3] == null) {
            return false;
        } else {
            try {
                FileWriter out = new FileWriter("output/deck" + index + "_output.txt", false);
                out.append(String.format("deck %1$d contents: %2$d %3$d %4$d %5$d ",
                index,contentsToPrint[0].getValue(),contentsToPrint[1].getValue(),contentsToPrint[2].getValue(),contentsToPrint[3].getValue()));
                out.close();
            } catch (IOException e){
                // Exception handled if FileWriter fails
                System.out.println(e.getMessage());
            }
            return true;
        }
    }
}
