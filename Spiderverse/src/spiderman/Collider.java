/**
 * The Collider class in the Spiderman package reads dimensions and clusters data, merges clusters,
 * inserts people into dimensions, and writes the output to a file.
 */
package spiderman;

import java.util.*;
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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        Cluster cluster = new Cluster(args[0]);
    
        
        
        Hashtable <Integer, List<Integer>> dimensions = readDimensions(args[0]);
        List<List<Integer>> clusters = cluster.getClusters();
        System.out.println("Cluster table: " + clusters);
        mergeClusters(clusters, dimensions);
        readSpiderverse(args[1], dimensions);
        writeOutput(args[2], dimensions);
        

    }

    //reads data from dimension.in 
    //dimensions are stored in hashtable, each dimension is mapped to other connected dimensions
    private static Hashtable<Integer, List<Integer>> readDimensions(String inputFile) {
        Hashtable<Integer, List<Integer>> c = new Hashtable<>();
        StdIn.setFile(inputFile);
    
        int numDimensions = StdIn.readInt();
        StdIn.readInt();
        StdIn.readDouble();


        for (int i = 0; i < numDimensions; i++) {
            int dimension = StdIn.readInt();
            int numEvents = StdIn.readInt();
            StdIn.readInt();
            List<Integer> connectedDimensions = new ArrayList<>();

            
            
            for(int j = 0; j < numEvents; j++) {
                
                
                
                connectedDimensions.add(dimension);

            
                }
                
            c.put(dimension, connectedDimensions);
            
        }
        return c; 
    }
    
    public static void readSpiderverse(String file, Hashtable<Integer,List<Integer>> dim){
        StdIn.setFile(file);
        int num = StdIn.readInt();

        for(int i = 0;i < num; i++) {
            int currD = StdIn.readInt();
            String name = StdIn.readString();
            int sign = StdIn.readInt();
        }
    }

    

    
    //makes cluster of dimensions, takes clusters list and updates dimension hashtable
    //for each cluster, iterate through dimensions and update their connections 
    private static void mergeClusters(List<List<Integer>> clusters, Hashtable<Integer, List<Integer>> dimensions) {
        for (List<Integer> cluster : clusters) {
            if (cluster.size() > 1) {
                int firstD = cluster.get(0);
                List<Integer> firstCluster = dimensions.getOrDefault(firstD, new ArrayList<>());

                
                
                for (int i = 1; i < cluster.size(); i++) 
                {
                    int currentD = cluster.get(i);

                    firstCluster.add(currentD);
                    List<Integer> currentC = dimensions.getOrDefault(currentD, new ArrayList<>());
                    currentC.add(firstD);
                    dimensions.put(currentD, currentC);
    
                    
                    
                }
                dimensions.put(firstD, firstCluster);
            }
        }
    }
    
    //writes to collider.out
    private static void writeOutput(String output, Hashtable<Integer, List<Integer>> dimensions) {
        StdOut.setFile(output);
        for(int dimension : dimensions.keySet()) {
            List<Integer> connections = dimensions.get(dimension);
            String line = dimension + " ";
            

            Set<Integer> printedConnections = new HashSet<>();

            for(int connected : connections) {
                if(connected != dimension && !printedConnections.contains(connected)) {
                    line += connected + " ";
                    printedConnections.add(connected);
                }
                
            }

            StdOut.println(line.toString());
        }
    }
}
    

    



