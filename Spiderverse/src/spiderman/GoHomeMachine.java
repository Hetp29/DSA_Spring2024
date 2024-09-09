package spiderman;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * HubInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 * 
 */

import java.util.*;

public class GoHomeMachine {
    

    public static void main(String[] args) {
        if (args.length < 5) {
            StdOut.println("Usage: java -cp bin spiderman.GoHomeMachine <dimensionInputFile> <spiderverseInputFile> <hubInputFile> <anomaliesInputFile> <reportOutputFile>");
            return;
        }

        // Create instance of GoHomeMachine and process input/output
        Map<Integer, Integer> weights = new HashMap<>();
        int hub;
        AdjacencyList adj = new AdjacencyList(args[0]);
        Hashtable<Integer, List<Integer>> adjList = adj.getAdjacencyList();
        
        Cluster cluster = new Cluster(args[0]);
        List<List<Integer>> clusters = cluster.getClusters();
        adj.mergeClusters(clusters, adjList);
        hub = readHub(args[2]);
        Map<String, Integer> signs = readSpiderverse(args[1]);
        Map<Integer, Dimension> dimensions = readDimensions(args[0], weights);
        writeAnomalies(args[3], args[4], weights, adjList, hub, signs, dimensions);
    }
    private static Map<Integer, Dimension> readDimensions(String file, Map<Integer, Integer> weights) {
        Map<Integer, Dimension> d = new HashMap<>();
        StdIn.setFile(file);
        int numD = StdIn.readInt();
        StdIn.readInt();
        StdIn.readDouble();
        for(int i = 0; i < numD; i++) {
            int dimNum = StdIn.readInt();
            int canon = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            d.put(dimNum, new Dimension(dimNum, canon, dimWeight));
            weights.put(dimNum, dimWeight);
            
        }
        return d;

    }
    private static Map<String, Integer> readSpiderverse(String file) {
        Map<String, Integer> signs = new HashMap<>();
        StdIn.setFile(file);
        int n = StdIn.readInt();
        for(int i = 0; i <  n; ++i) {
            int dimension = StdIn.readInt();
            String name = StdIn.readString();
            int sign = StdIn.readInt();
            signs.put(name, sign);
        }
        return signs;
    }
    private static int readHub(String file) {
        StdIn.setFile(file);
        int n = StdIn.readInt();
        return n;
    }
    private static void writeAnomalies(String aFile, String outputFile, Map<Integer, Integer> weights, Map<Integer, List<Integer>> adjList, int hub, Map<String, Integer> spiderverse, Map<Integer, Dimension> dimensions) {
        StdOut.setFile(outputFile);
        Map<Integer, Integer> pred = dikstras(adjList, weights, hub);
        StdIn.setFile(aFile);
        int n = StdIn.readInt();
        for(int i = 0; i < n; i++) {
            String name = StdIn.readString();
            int time = StdIn.readInt();

            int home = spiderverse.getOrDefault(name, -1);
            if(home  == -1) continue;

            int aSign = spiderverse.get(name);
            int canon = dimensions.get(aSign).getCanonEvents();

            ArrayList<Integer> path = getPath(home, hub, pred);
            reportAnomaly(name, home, canon, time, path, weights, pred, spiderverse, dimensions);

        }
    }

    private static Map<Integer, Integer> dikstras(Map<Integer, List<Integer>> adjList, Map<Integer, Integer> weights, int hub) {
        Map<Integer, Integer> distance = new HashMap<>();
        Map<Integer, Integer> pred = new HashMap<>();
        PriorityQueue<Integer> fringe = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        Map<Integer, Boolean> d = new HashMap<>();
        
        System.out.println("Adjacency List: " + adjList);
        System.out.println("Weights " + weights);

        
        
        
        for(Integer dim : adjList.keySet()) {
            
            distance.put(dim, Integer.MAX_VALUE);
            pred.put(dim, null);
            d.put(dim, false);
            weights.putIfAbsent(dim, 1);
        }
        distance.put(hub, 0);
        fringe.add(hub);
        while(!fringe.isEmpty()) {
            int current = fringe.poll();
            if(d.get(current)) continue;
            d.put(current, true);
            
            for(Integer neighbor : adjList.get(current)) {
                if(d.get(neighbor)) continue;
                int weight = weights.get(current) + weights.get(neighbor);
                int newD =  distance.get(current) + weight;
                
                if(newD < distance.get(neighbor)) {
                    distance.put(neighbor,newD);
                    pred.put(neighbor, current);
                    fringe.add(neighbor);
                    
                    
                }
                
            }
        }

        System.out.println("Final distances: " + distance);
        System.out.println("Predecessor Map: " + pred);
        
        return pred;
    }
    private static ArrayList<Integer> getPath(int home, int hub, Map<Integer, Integer> pred) {
        ArrayList<Integer> route = new ArrayList<>();
        Integer pointer = home;
        while(pointer !=  hub && pointer != null) {
            route.add(0, pointer);
            pointer = pred.get(pointer);
            
        }
        route.add(0, hub);
        return route;
    }
    private static void reportAnomaly(String name, int home, int canon, int time, ArrayList<Integer> path, Map<Integer, Integer> weights, Map<Integer, Integer> pred, Map<String, Integer> spiderverse, Map<Integer, Dimension> dimensions) {        
        
        int cost = 0; 
        for(int i = 0; i < path.size() - 1; i++) {
            int from = path.get(i);
            int to = path.get(i + 1);
            cost += weights.get(from) + weights.get(to);
        }
        
        if(cost > time) {
            weights.put(home, weights.get(home) - 1);
            StdOut.print((canon - 1) + " ");
            StdOut.print(name + " ");
            StdOut.print("FAILED ");
            for(int v : path) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
        else {
            StdOut.print(canon + " ");
            StdOut.print(name + " ");
            StdOut.print("SUCCESS ");
            for(int v : path) {
                StdOut.print(v + " ");
            }
            StdOut.println();
        }
    }
}