package forensic;

import java.util.Stack;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {

        // WRITE YOUR CODE HERE
        //read all information from dataset, store it in an array/profile object, return array as profile
        int num = StdIn.readInt();//read STRs for person
        STR[] str = new STR[num];//initialize array of STR objects
        int ctr = 0;//counter for loop
        while(ctr < num) //read information for STRs 
        {
            String name = StdIn.readString();//read STr name
            int occurTime = StdIn.readInt();//reads # of occurences
            STR strO = new STR(name, occurTime);
            str[ctr] = strO; //adds str object to STR array
            ctr++;
            
        }

        
        
        return new Profile(str); // update this line
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */
    public void insertPerson(String name, Profile newProfile) {

        // WRITE YOUR CODE HERE
        //inserts node in BST based on name and profile object
        TreeNode nod = new TreeNode(name, newProfile,null,null);//create tree node with name and profile
        if(treeRoot == null) //if tree is empty
        {
            treeRoot = nod;//set new node as tree's root
            return;
        }
        insertNod(treeRoot, nod);
    }   private void insertNod(TreeNode currentNod, TreeNode newNod)
    {
        int compare = newNod.getName().compareTo(currentNod.getName());//compare names using compareTo
        if(compare < 0 ) //if newNod name is less than currentNod, go to left of tree
        {
            if(currentNod.getLeft() == null) 
            {
                currentNod.setLeft(newNod);//insert new node if left of tree is empty
            }
            else 
            {
                insertNod(currentNod.getLeft(), newNod); //insert into left of tree if left of tree is not empty using recursion
            }
        }
        else if(compare > 0)//newNod name is greater than currentNod, go to right of tree
        {
            if(currentNod.getRight() == null)
            {
                currentNod.setRight(newNod);//insert new node if right of tree is empty 
            }
            else 
            {
                insertNod(currentNod.getRight(), newNod); //insert into right of tree if right of tree is not empty using recursion
                //use recursion to keep going until right or left of tree is null to insert new node
            }

        }
        
    }

    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    public int getMatchingProfileCount(boolean isOfInterest) {
        
        // WRITE YOUR CODE HERE
        //call helper recursive method
        return countProfilesHelper(treeRoot, isOfInterest);

        

         // update this line
    }
    private int countProfilesHelper(TreeNode currentNode, boolean profileOfInterest)
    {
        if(currentNode == null)
        {
            return 0; //return 0 if current node is null(base case)
        }
        int ctrForMatches = 0; //counter for tracking profiles that match
        ctrForMatches += countProfilesHelper(currentNode.getLeft(), profileOfInterest);//call helper for left of tree
        ctrForMatches += countProfilesHelper(currentNode.getRight(), profileOfInterest);//for right of tree
        Profile curNode = currentNode.getProfile(); //get the profile for the current node
        if(curNode.getMarkedStatus() == profileOfInterest) 
        {
            ctrForMatches++;//if there is a match, increase the counter for matches

        }
        return  ctrForMatches;

    }

    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
public void flagProfilesOfInterest() {
    traverseBst(treeRoot, firstUnknownSequence, secondUnknownSequence);
}

private void traverseBst(TreeNode cur, String seq1, String seq2) {
    // Stack for iterative traversal
    Stack<TreeNode> stack = new Stack<>();
    stack.push(cur);

    while (!stack.isEmpty()) {
        TreeNode node = stack.pop();

        if (node != null) {
            Profile curProfile = node.getProfile();
            STR[] profiles = curProfile.getStrs();
            int total = profiles.length;
            int matchTimes = 0;

            // Iterate through STRs
            for (int i = 0; i < total; i++) {
                STR currentStr = profiles[i];
                int profileCounter = currentStr.getOccurrences();
                int totalMatches = numberOfOccurrences(seq1, currentStr.getStrString())
                                    + numberOfOccurrences(seq2, currentStr.getStrString());

                // Check for match
                matchTimes += (profileCounter == totalMatches) ? 1 : 0;
            }

            // Check if at least half of STRs occur the same amount of times
            int atLeastHalf = (total + 1) / 2;
            curProfile.setInterestStatus(matchTimes >= atLeastHalf);

            // Push children to stack
            stack.push(node.getRight());
            stack.push(node.getLeft());
        }
    }
}



    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {

        // WRITE YOUR CODE HERE
        int sizeOfUnmarkedArray = getMatchingProfileCount(false);//get amount of unmarked people so you can set that as size of unmarked array
        //set getMatchingProfileCount to false because we are creating an array for UNMARKED profiles, not MARKED profiles
        String[] unmarkedTotal = new String[sizeOfUnmarkedArray]; //array that we return and size s length of unmarked profiles

        Queue<TreeNode> untilEmpty = new Queue<>();
        untilEmpty.enqueue(treeRoot);//enqueue to start traversing
        int i= 0;
        while(!untilEmpty.isEmpty())//traverse the queue until the queue is empty
        {
            TreeNode currNode = untilEmpty.dequeue();//dequeue current node
            if(!currNode.getProfile().getMarkedStatus()) //check if profile for current node is marked or unmarked
            {
                unmarkedTotal[i++] = currNode.getName();//add person's name to array if profile is unmarked

            }
            //keep enqueuing because we have to do it for the nodes on the right and left subtree as well
            if(currNode.getLeft() != null)
            {
                untilEmpty.enqueue(currNode.getLeft());
            }
            if(currNode.getRight() != null)
            {
                untilEmpty.enqueue(currNode.getRight());
            }

        }

        return unmarkedTotal; // update this line

    }

    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    public void removePerson(String fullName) {
        // WRITE YOUR CODE HERE
        //use recursion here because we're removing from a BST (we have to keep moving down)
        treeRoot = removePersonFromBST(treeRoot, fullName);
        
    }
    //recursive method to remove node with name we want to remove
    private TreeNode removePersonFromBST(TreeNode currentNode, String fullName) 
    {
        if(currentNode == null)
        {
            return null;//return null if subtree is empty
        }

        int compare = fullName.compareTo(currentNode.getName());//compare names to determine position of nodes in tree
        if(compare < 0) //remove from left side of tree
        {
            currentNode.setLeft(removePersonFromBST(currentNode.getLeft(), fullName));

        }
        else if(compare > 0) //remove from right of tree
        {
            currentNode.setRight(removePersonFromBST(currentNode.getRight(), fullName));

        }

        else  //if node that we want to remove is found
        {
            //if there is no child, we delete by setting parent to null
            //if there is one child, we replace parent link
            if(currentNode.getLeft() == null) 
            {
                return  currentNode.getRight(); 
            }
            else if (currentNode.getRight() == null)
            {
                return currentNode.getLeft();
            }

            //if there is two children, find in order successor, delete minimum in right subtree, and put in order successor in node's spot
            TreeNode newnode = findSuccessor(currentNode.getRight());//find the in-order next node which will be our new node (one that replaces old node)            
            //place the smallest value in the right subtree into the spot of the node being removed, then delete the node
            currentNode.setName(newnode.getName());  
            currentNode.setProfile(newnode.getProfile());//update  profile info as well
            currentNode.setRight(removePersonFromBST(currentNode.getRight(), newnode.getName()));
        }
        return  currentNode;
    }
    //helper for finding minimum node or successor in a subtree
    private TreeNode findSuccessor(TreeNode node)
    {
        while(node.getLeft() != null)
        {
            node = node.getLeft(); //put in order successor in node's spot 
        }
        return node;
    }

    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
        //remove all unmarked profiles from the bst
        String[] unmarked = getUnmarkedPeople();//get array for unmarked people by using getUnmarkedPeople() method
        int index = 0;
        while(index < unmarked.length) 
        {
            removePerson(unmarked[index]);//use removePerson on unmarked people to remove unmarked people
            index++;
        }
    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}