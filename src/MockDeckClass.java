import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class MockDeckClass implements Deck{
    private int index;
    private ArrayBlockingQueue<Card> contents = new ArrayBlockingQueue<>(4);

    public int getIndex(){return index;}

    public void setIndex(int index){
        this.index = index;
    }

//    public void MockDeckClass(int[] ){
//        contents.add(c);
//    }
public boolean checkForTurn(){
    if (contents.toArray().length<3){
        return false;
    }
    return true;
}

    public boolean addCard(Card c){
        return contents.offer(c);
    }

    public  Card removeCard(){return contents.poll();};

    public void clearContents(Card c){contents.clear();};

    public boolean logDeck(boolean rollback) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("deck" + index + "_output.txt"));
        out.writeObject("deck" + index + " contents: " + contents.toString());
        return true;
    }
}
