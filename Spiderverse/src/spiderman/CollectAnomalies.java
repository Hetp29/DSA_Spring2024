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
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */
import java.util.*;

public class CollectAnomalies {
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        AdjacencyList adjList = new AdjacencyList(args[0]);
        Hashtable<Integer, List<Integer>> dGraph = adjList.getAdjacencyList();
        Cluster cluster = new Cluster(args[0]);
        List<List<Integer>> clusters = cluster.getClusters();
        adjList.mergeClusters(clusters, dGraph);
        //System.out.println("Adjacency List: " + dGraph);
        Spiderverse reader = new Spiderverse(args[1]);
        Hashtable<Integer, List<Spider>> spiderverse = reader.getSpiderverse();

        int start = readHub(args[2]);
        List<Route> routes = findAnomalies(spiderverse, dGraph, start);
        
        writeOutput(args[3], routes);

        
    }

    private static int readHub(String file) {
        StdIn.setFile(file);
        int num = StdIn.readInt();
        return num;
    }

    private static List<Route> findAnomalies(Hashtable<Integer, List<Spider>> spiderverse, Hashtable<Integer, List<Integer>> dimensions, int start) {
        List<Route> r = new ArrayList<>();
        int maxD = getMax(dimensions);
        boolean[] added = new boolean[maxD + 1];
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parent = new HashMap<>();
        queue.add(start);
        added[start] = true; 
    
        
        while(!queue.isEmpty()) {
            int current = queue.poll();
            List<Spider> spiders = spiderverse.get(current);
            if(spiders != null) {
                for(Spider s : spiders) {
                    
                    if(s.getCurrentDimension() != start && s.getSign() != current) {
                        
                        List<Integer> aRoute = bfs(parent, current);
                        
                        
                        if(spiders.get(0).getSign() == current) {
                            List<Integer> reverse = new ArrayList<>(aRoute);
                            Collections.reverse(reverse);
                            String matchingSpider = null; 
                            for(Spider spider : spiders) {
                                if(spider.getCurrentDimension() == current) {
                                    matchingSpider = spider.getName();
                                    break;

                                }
                            }
                            if(matchingSpider != null){
                                r.add(new Route(s.getName(), matchingSpider, reverse));

                            }
                            
                        }
                        else {
                            List<Integer> palindrome = new ArrayList<>(aRoute);
                            int n = palindrome.size();
                            for(int i = n - 2; i >= 0; i--) {
                                palindrome.add(palindrome.get(i));
                            }
                            r.add(new Route(s.getName(), s.getName(), palindrome));
                        }
                    }
                }
            }
            List<Integer> n = dimensions.get(current);
            if(n != null) {
                for(int neighbor : n) {
                    if(!added[neighbor]) {
                        added[neighbor] = true;
                        parent.put(neighbor, current);
                        queue.add(neighbor);
                        
                    }
                }
            }
        }

        return r;
    }
    
    
    
    
    
    private static int getMax(Hashtable<Integer, List<Integer>> d) {
        int max = 0;
        for(int dim : d.keySet()) {
            max= Math.max(max, dim);
            List<Integer> n = d.get(dim);
            if(n != null) {
                for(int neighbor : n) {
                    max = Math.max(max, neighbor);
                }
            }
        }
        return max;
    }

    private static List<Integer> bfs(Map<Integer, Integer> parent, int end) {
        List<Integer> route = new ArrayList<>();
        while(parent.containsKey(end)) {
            route.add(end);
            end = parent.get(end);
        }
        route.add(end);
        Collections.reverse(route);
        return route;
    }

    private static void writeOutput(String file, List<Route> routes) {
        StdOut.setFile(file);
        Set<String> duplicates = new HashSet<>();
        for(Route route : routes) {
            String duplicate = route.getAnomalyName();
            if(!duplicates.contains(duplicate)) {
                duplicates.add(duplicate);
                StdOut.print(route.toString());
                
            }
            else {
                routes.remove(route);
            }
            
            
        }
        
    }
}
