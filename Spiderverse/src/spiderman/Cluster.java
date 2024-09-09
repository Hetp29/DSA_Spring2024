package spiderman;

import java.util.*;

public class Cluster {
    private Hashtable<Integer, List<Integer>> clusterTable;

    public Cluster(String inputFile) {
        clusterTable = new Hashtable<>();
        initializeClusters(inputFile);
    }

    private void initializeClusters(String inputFile) {
        StdIn.setFile(inputFile);
        int a = StdIn.readInt(); // dimensions
        int b = StdIn.readInt(); // initial table size
        double c = StdIn.readDouble(); // capacity threshold

        clusterTable = new Hashtable<>(b);
        int dimSoFar = 0; 
        int clustersSoFar = 0; 

        for (int i = 0; i < a; i++) {
            int dimensionNum = StdIn.readInt();
            int numCanonEvents = StdIn.readInt(); 
            int dimWeight = StdIn.readInt(); 
            int index = dimensionNum % b;

            if (!clusterTable.containsKey(index)) {
                clusterTable.put(index, new ArrayList<>());
                clustersSoFar++;
            }
            clusterTable.get(index).add(0,dimensionNum);
            dimSoFar++;

            if (((double) dimSoFar / clusterTable.size()) >= c) {
                b *= 2; 
                rehash(clusterTable, b);
                clustersSoFar = clusterTable.size();
            }
        }
        connect(clusterTable);
        

        List<Integer> clusterIndices = new ArrayList<>(clusterTable.keySet());//clusters are stored in this arraylist
        //printing clusters to clusters.out
        for(int i = clusterIndices.size() - 1; i >= 0; i--) {
            int index = clusterIndices.get(i);
            List<Integer> cluster = clusterTable.get(index);

            for(int j = 0; j < cluster.size(); j++) {
                StdOut.print(cluster.get(j));
                if(j < cluster.size() - 1) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        } 
    }
        

    private void rehash(Hashtable<Integer, List<Integer>> clusterTable, int newSize) {
        Hashtable<Integer, List<Integer>> newClusterTable = new Hashtable<>(newSize);
        for (List<Integer> cluster : clusterTable.values()) {
            for (int dimension : cluster) {
                int newIndex = dimension % newSize;
                if(!newClusterTable.containsKey(newIndex)) {
                    newClusterTable.put(newIndex, new ArrayList<>());
                }
                newClusterTable.get(newIndex).add(0, dimension);
                
            }
        }
        clusterTable.clear();;
        clusterTable.putAll(newClusterTable);
    }

    private void connect(Hashtable<Integer, List<Integer>> clusterTable) {
        

        for (int i = 0; i < clusterTable.size(); i++) {
            
            List<Integer> current = clusterTable.get(i);
            List<Integer> prev1 = clusterTable.get((i - 1 + clusterTable.size()) % clusterTable.size());
            List<Integer> prev2 = clusterTable.get((i - 2 + clusterTable.size()) % clusterTable.size());

            if (current != null && prev1 != null && prev2 != null) {
                current.add(prev1.get(0));
                current.add(prev2.get(0));
                
            }
        }
    }

    public List<List<Integer>> getClusters() {
        List<List<Integer>> clusters = new ArrayList<>(clusterTable.values());
        Collections.reverse(clusters);
        return clusters;

        
    }
    
}
