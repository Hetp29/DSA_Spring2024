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
 * 
 * Step 2:
 * ClusterOutputFile name is passed in through the command line as args[1]
 * Output to ClusterOutputFile with the format:
 * 1. n lines, listing all of the dimension numbers connected to 
 *    that dimension in order (space separated)
 *    n is the size of the cluster table.
 * 
 * @author Seth Kelley
 */

public class Clusters {
    

    public static void main(String[] args) {

        if ( args.length < 2 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Clusters <dimension INput file> <clusters OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        String input = args[0];
        String output = args[1];
        
        
        StdIn.setFile(input);
        int a = StdIn.readInt();//dimensions
        int b = StdIn.readInt();//initial table size
        double c = StdIn.readDouble();//capacity threshold

        Hashtable<Integer, List<Integer>> clusters = new Hashtable<>(b);//hashtable for storing clusters
        int dimSoFar = 0;//counters to keep track of dimensions and clusters
        int clustersSoFar = 0;
        for(int i = 0; i < a; i++) //go through each dimension
        {
            int dimensionNum = StdIn.readInt();
            int numCanonEvents = StdIn.readInt();
            int dimWeight = StdIn.readInt();
            int index = dimensionNum % b;
            
            if(!clusters.containsKey(index)) {//create new cluster if one does not exist 
                clusters.put(index, new ArrayList<>());
                clustersSoFar++;
            }
            clusters.get(index).add(0, dimensionNum);//add dimension to cluster 
            dimSoFar++;

            
            if(((double) dimSoFar / clustersSoFar) >= c) {//rehash if load factor is equal to or more than threshold 
                b *=2; //new size of cluster table is doubled 
                rehash(clusters, b);
                clustersSoFar = clusters.size();
            }
        }

        connect(clusters);

        StdOut.setFile(output);
        
        List<Integer> clusterIndices = new ArrayList<>(clusters.keySet());//clusters are stored in this arraylist
        //printing clusters to clusters.out
        for(int i = clusterIndices.size() - 1; i >= 0; i--) {
            int index = clusterIndices.get(i);
            List<Integer> cluster = clusters.get(index);

            for(int j = 0; j < cluster.size(); j++) {
                StdOut.print(cluster.get(j));
                if(j < cluster.size() - 1) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        } 

        

        
        
    

}
private static void rehash(Hashtable<Integer, List<Integer>> clusterTable, int newSize) {//rehash cluster table
    //new hashtable will contain rehashed clusters 
    Hashtable<Integer, List<Integer>> newC = new Hashtable<>(newSize);
    for(List<Integer> l : clusterTable.values()) //go through clusters in original cluster table
    {
        for(int dimension : l) //go through dimension in original cluster table
        {
            int newI = dimension % newSize;//new index for dimension based on new size 
            if(!newC.containsKey(newI)) //create new empty list if hashtable does not contain list 
            {
                newC.put(newI, new ArrayList<>());
            }
            newC.get(newI).add(0, dimension);//add dimension at new index to list, add it to beginning of the list 
        }
    }
    clusterTable.clear();//clear original cluster table
    clusterTable.putAll(newC); //copy all entries from new hashtable to original cluster hashtable 
}

private static void connect(Hashtable<Integer, List<Integer>> clusterTable) {//connect dimensions between different clusters 
    for(int i = 0; i < clusterTable.size(); i++) 
    {
    
        List<Integer> current = clusterTable.get(i);
        List<Integer> prev1 = clusterTable.get((i - 1 + clusterTable.size()) % clusterTable.size());
        List<Integer> prev2 = clusterTable.get((i - 2 + clusterTable.size()) % clusterTable.size());
        if(current != null && prev1 != null && prev2 != null) 
        {
            current.add(prev1.get(0));//retrieves first dimension from prev1 list
            //current.add adds dimension from prev1 to end of list


            current.add(prev2.get(0));//retrieves firs dimension of prev2 list
        }
    }
}



}