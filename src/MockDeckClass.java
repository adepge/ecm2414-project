import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public class MockDeckClass implements Deck{
    private int index;
    private ArrayBlockingQueue<Card> contents = new ArrayBlockingQueue<>(4);
    protected boolean rolledBack = false;
    private Card[] oldContents;
    private Card[] contentsToPrint;

    public int getIndex(){return index;}
    public void setIndex(int index){
        this.index = index;
    }

    public MockDeckClass(int[] cards){
        for(int i=0; i < 4; i++){
            contents.add(new Card(cards[i]));
        }
    }

    public void setDeckContents(int[] cards){
        contents.clear();
        for(int i=0; i < 4; i++){
            contents.add(new Card(cards[i]));
        }
    }

    public boolean checkForTurn(){
        if (contents.remainingCapacity()>1){
            return false;
        }
        return true;
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

    public void clearContents(Card c){contents.clear();}

    public boolean logDeck(boolean rollback) throws IOException {
        if (rollback){
            if(contents.remainingCapacity()>0){
                rolledBack = true;
            }
            contentsToPrint = oldContents;
        } else {
            if (contentsToPrint == null) {
                contentsToPrint = contents.toArray(new Card[4]);
            } else {
                for (int i = 2; i > -1; i--) {
                    contentsToPrint[i + 1] = contentsToPrint[i];

                }
                contentsToPrint[0] = oldContents[0];
            }
        }
        try {
            FileWriter out = new FileWriter("/output/deck" + index + "_output.txt", true);
            out.append("deck" + index + " contents: " + contentsToPrint[0].getValue() + " " + contentsToPrint[1].getValue() + " " + contentsToPrint[2].getValue() + " " + contentsToPrint[3].getValue() + "\n");
            out.close();
            return true;
        } catch (NullPointerException e){
            return false;}
    }
}
