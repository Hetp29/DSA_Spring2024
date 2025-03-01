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
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

 import java.util.*;

 public class TrackSpot {
 
     public static void main(String[] args) {
         if (args.length < 4) {
             StdOut.println(
                 "Execute: java -cp bin spiderman.TrackSpot <dimension input file> <spiderverse input file> <spot input file> <trackspot output file>");
             return;
         }
 
         
         DimensionNetwork dimensionNetwork = new DimensionNetwork(args[0]);
         Hashtable<Integer, List<Integer>> networkMap = dimensionNetwork.getNetworkMap();
 
        
         int[] travelEndpoints = fetchEndpoints(args[2]);
         int startingDimension = travelEndpoints[0];
         int destinationDimension = travelEndpoints[1];
 
        
         ClusterController clusterController = new ClusterController(args[0]);
         List<List<Integer>> clusterGroups = clusterController.retrieveClusters();
         dimensionNetwork.integrateClusters(clusterGroups, networkMap);
 
        
         List<Integer> traveledPath = new ArrayList<>();
         HashSet<Integer> visitedDimensions = new HashSet<>();
         performDepthFirstSearch(networkMap, startingDimension, destinationDimension, visitedDimensions, traveledPath);
 
         
         StdOut.setFile(args[3]);
         for (int dimension : traveledPath) {
             StdOut.print(dimension + " ");
         }
         StdOut.println();
     }
 
     private static int[] fetchEndpoints(String filePath) {
         StdIn.setFile(filePath);
         int[] endpoints = new int[2];
         endpoints[0] = Integer.parseInt(StdIn.readLine());
         endpoints[1] = Integer.parseInt(StdIn.readLine());
         return endpoints;
     }
 
     private static void performDepthFirstSearch(Hashtable<Integer, List<Integer>> networkMap, int currentDimension, int finalDimension, Set<Integer> visited, List<Integer> path) {
         visited.add(currentDimension);
         path.add(currentDimension);
 
         if (currentDimension == finalDimension) {
             return;
         }
 
         List<Integer> neighbors = networkMap.get(currentDimension);
         int index = 0;
         while (neighbors != null && index < neighbors.size()) {
             int neighborDimension = neighbors.get(index);
             if (!visited.contains(neighborDimension)) {
                 performDepthFirstSearch(networkMap, neighborDimension, finalDimension, visited, path);
                 if (path.get(path.size() - 1) == finalDimension) {
                     return; 
                 }
                 
                 path.remove(path.size() - 1);
             }
             index++;
         }
     }
 }
 