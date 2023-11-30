import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class PackGenerator {
    static ArrayList<Integer> pack;
    public static void generatePack(int players){
        pack = new ArrayList<>();
        for(int i=1; i <= players*2; i++){
            pack.add(i);
            pack.add(i);
            pack.add(i);
            pack.add(i);
        }
        Collections.shuffle(pack);
        exportPack(pack, "standard");

    }

    public static void generateSimplePack(int players){
        pack = new ArrayList<>();
        for(int i=0; i < players*4; i++){
            pack.add(i % players + 1);
            pack.add(i % players + 1);
        }
        Collections.shuffle(pack);
        exportPack(pack, "simple");
    }

    public static  void generateUnshuffledPack(int players){
        pack = new ArrayList<>();
        for(int i=1; i <= players*2; i++){
            pack.add(i);
            pack.add(i);
            pack.add(i);
            pack.add(i);
        }
        exportPack(pack, "unshuffled");
    }

    public static void exportPack(ArrayList<Integer> pack, String type){
        try {
            FileWriter out = new FileWriter("pack/" + pack.size()/8 + type + "_pack.txt", false);
            for(Integer cards : pack){
                out.append(cards.toString()).append("\n");
            }
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        generatePack(500);
    }
}
