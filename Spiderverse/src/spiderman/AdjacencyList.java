package spiderman;
import java.util.*;

public class AdjacencyList {
    private Hashtable<Integer, List<Integer>> adjList;

    public AdjacencyList(String file) {
        adjList = readAdjList(file);
    }

    private Hashtable<Integer, List<Integer>> readAdjList(String file) {
        Hashtable<Integer, List<Integer>> adjList = new Hashtable<>();
        StdIn.setFile(file);

        int numDimensions = StdIn.readInt();
        StdIn.readInt();  // Read and ignore an integer
        StdIn.readDouble();  // Read and ignore a double

        int i = 0;
        while (i < numDimensions) {
            int d = StdIn.readInt();
            int numEvents = StdIn.readInt();
            StdIn.readInt();  // Read and ignore an integer
            List<Integer> connectedD = new ArrayList<>();

            int j = 0;
            while (j < numEvents) {
                connectedD.add(d);
                j++;
            }
            adjList.put(d, connectedD);
            i++;
        }
        return adjList;
    }

    public void mergeClusters(List<List<Integer>> clusters, Hashtable<Integer, List<Integer>> dimensions) {
        int index = 0;
        while (index < clusters.size()) {
            List<Integer> cluster = clusters.get(index);
            if (cluster.size() > 1) {
                int firstD = cluster.get(0);
                List<Integer> firstCluster = dimensions.getOrDefault(firstD, new ArrayList<>());

                int i = 1;
                while (i < cluster.size()) {
                    int currentD = cluster.get(i);

                    firstCluster.add(currentD);
                    List<Integer> currentC = dimensions.getOrDefault(currentD, new ArrayList<>());
                    currentC.add(firstD);
                    dimensions.put(currentD, currentC);

                    i++;
                }
                dimensions.put(firstD, firstCluster);
            }
            index++;
        }
    }

    public Hashtable<Integer, List<Integer>> getAdjacencyList() {
        return adjList;
    }
}
