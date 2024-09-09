package spiderman;

import java.util.*;


public class Spiderverse {
    private Hashtable<Integer, List<Spider>> spiderverse = new Hashtable<>();

    public Spiderverse(String file) {
        StdIn.setFile(file);
        int num = StdIn.readInt();
        for(int i = 0; i < num; i++) {
            int currentD = StdIn.readInt();
            String name = StdIn.readString();
            int sign = StdIn.readInt();

            Spider spider = new Spider(currentD, name, sign);
            if(!spiderverse.containsKey(currentD)) {
                spiderverse.put(currentD, new ArrayList<>());
            }
            spiderverse.get(currentD).add(spider);
        }

        
    }
    public Hashtable<Integer, List<Spider>> getSpiderverse() {
        return spiderverse;
    }
    
}
