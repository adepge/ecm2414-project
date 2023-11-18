import java.io.*;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;


public class DeckClass implements Deck{
    private int index;
    private ArrayBlockingQueue<Card> contents = new ArrayBlockingQueue<>(4);

    public DeckClass(int index){
        this.index = index + 1;
    }

    public int getIndex(){return index;};

    public boolean checkForTurn(){
        if (contents.remainingCapacity()>1){
            return false;
        }
        return true;
    }

    public void addCard(Card c) throws InterruptedException{
//        Random random = new Random();
        contents.offer(c);
//        if(contents.remainingCapacity() > 0) {
//            while (!contents.offer(c)) {
//                System.out.println("Waiting to add card...");
//                contents.offer(c, random.nextInt(101), TimeUnit.MILLISECONDS);
//            }
//        }
    }

    public Card removeCard(){return contents.poll();};

    public void clearContents(Card c){contents.clear();};

    public void logDeck() throws IOException {
        Card[] contentsToPrint = contents.toArray(new Card[4]);
        FileWriter out = new FileWriter("deck" + index + "_output.txt", true);
        out.append("deck" + index + " contents: " + contentsToPrint[0].getValue() + " " + contentsToPrint[1].getValue() + " " + contentsToPrint[2].getValue() + " " + contentsToPrint[3].getValue() + "\n");
        out.close();
    }
}