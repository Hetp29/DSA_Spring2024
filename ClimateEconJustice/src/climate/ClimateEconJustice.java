package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine(); // Skips header
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] extract = inputLine.split(",");
        
        String stateName = extract[2].trim(); //extracts only states name

        StateNode current = firstState;
        while(current != null) {
            if(current.getName().equals(stateName)){
                return; //skip adding state if it already exists 
            }
            current = current.getNext();
        }

        StateNode newState = new StateNode();
        newState.setName(stateName);

        if(firstState == null) {
            //if list is empty, set newState ad firstState
            firstState = newState;
        } 
        else {//find last state and add new state after
            current = firstState;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newState);
        } 

    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        //  WRITE YOUR CODE HERE
        String[] split = inputLine.split(",");//split input lin to extract info
        String county = split[1].trim(); //line gets element at index 1 & assigns it to 
        //county and removes whitespace with .trim()
        String state = split[2].trim();

        StateNode current = firstState;//search for first state
        while(current != null && !current.getName().equals(state)) {
            current = current.getNext();
        }
        if(current != null) { //if state is found, check if county exists
            CountyNode currCounty = current.getDown();
            while(currCounty != null) {
                if(currCounty.getName().equals(county)) {
                    return; //do nothing if county already exists 
                }
                currCounty = currCounty.getNext();
            }
            CountyNode newCounty = new CountyNode(); //create new node for county & 
            //add it to end of county list
            newCounty.setName(county);
            //if county list is empty, set newCounty as first county
            if(current.getDown() == null) {
                current.setDown(newCounty);
            } else {
                //find last county & add new county after it
                currCounty = current.getDown();
                while(currCounty.getNext() != null) {
                    currCounty = currCounty.getNext();
                }
                currCounty.setNext(newCounty);
            }
        }





    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] split = inputLine.split(","); //split input line to extract info 
        String comm = split[0].trim(); //extracts name of community from first index and removes white spaces
        String state = split[2].trim();
        String county = split[1].trim();
        //search for state
        StateNode current = firstState;
        while(current != null && !current.getName().equals(state)) {
            current = current.getNext();
        }
        if(current != null) { //state found, next search for county in state's list
        
            CountyNode countyCurr = current.getDown(); //countyNode below StateNode
            while(countyCurr != null && !countyCurr.getName().equals(county)){//search for county in state list
                countyCurr = countyCurr.getNext();
                
            }
            if(countyCurr != null) {//county is found
                CommunityNode community = countyCurr.getDown(); //CommunityNode below CountyNode 
                CommunityNode last = null; //keeps track of last community 
                while(community != null) {
                    if(community.getName().equals(comm)) {
                        
                        return; //don't add if community already exists 
                    }
                    last = community;//update last community before moving to next community 
                    community = community.getNext();
                } 
            
            Data commData = createDataFromInput(split); //Data object to store data from this community 
            CommunityNode newComm = new CommunityNode();//add new community node to end of community list
            newComm.setName(comm);
            newComm.setInfo(commData);
            //if community list is empty, set newCommunity as first community 
            if(countyCurr.getDown() == null) 
            {
                countyCurr.setDown(newComm);
            }
            else 
            //find last community and add new community after
            {

                if(last != null) 
                {
                    last.setNext(newComm);
                }
                else
                {
                    //last is null, means list for community is empty 
                    countyCurr.setDown(newComm);
                }
            }
        }
    }
}
private Data createDataFromInput(String[] split) {
    //returns data object based on input from String array

    Data commData = new Data();
    commData.setPrcntAfricanAmerican(Double.parseDouble(split[3].trim()));
    commData.setPrcntNative(Double.parseDouble(split[4].trim()));
    commData.setPrcntAsian(Double.parseDouble(split[5].trim()));
    commData.setPrcntWhite(Double.parseDouble(split[8].trim()));
    commData.setPrcntHispanic(Double.parseDouble(split[9].trim()));
    commData.setAdvantageStatus(split[19].trim());
    commData.setPMlevel(Double.parseDouble(split[49].trim()));
    commData.setChanceOfFlood(Double.parseDouble(split[37].trim()));
    commData.setPercentPovertyLine(Double.parseDouble(split[121].trim()));

    // Add more fields as needed

    return commData;
}
    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        // WRITE YOUR CODE HERE
        int ctr = 0;
        StateNode state = firstState;
        while(state != null) 
        {
            CountyNode county = state.getDown();
            while(county != null) 
            {
                CommunityNode community = county.getDown();
                while(community != null)
                { //checks conditions for given racial group and disadvantaged 
                    if(race.equals("African American") &&
                    (community.getInfo().getPrcntAfricanAmerican() * 100 >= userPrcntage) &&
                    (community.getInfo().getAdvantageStatus().equals("True"))) {
                        ctr++;
                    }
                    else if(race.equals("Native American") &&
                    (community.getInfo().getPrcntNative() * 100 >= userPrcntage) &&
                    (community.getInfo().getAdvantageStatus().equals("True"))) {
                        ctr++;
                    }
                    else if(race.equals("Asian American") &&
                    (community.getInfo().getPrcntAsian() * 100 >= userPrcntage) &&
                    (community.getInfo().getAdvantageStatus().equals("True"))) {
                        ctr++;
                    }
                    else if(race.equals("White American") &&
                    (community.getInfo().getPrcntWhite() * 100 >= userPrcntage) &&
                    (community.getInfo().getAdvantageStatus().equals("True"))) {
                        ctr++;
                    }
                    else if(race.equals("Hispanic American") &&
                    (community.getInfo().getPrcntHispanic() * 100 >= userPrcntage) &&
                    (community.getInfo().getAdvantageStatus().equals("True"))) {
                        ctr++;
                    }
                    community = community.getNext();
                }
                county = county.getNext();
            }
            state = state.getNext();
        }

        
        return ctr; //update line
    }


    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

	//WRITE YOUR CODE HERE
        
    int ctr = 0;
    StateNode state = firstState;
    while(state != null) 
    {
        CountyNode county = state.getDown();
        while(county != null) 
        {
            CommunityNode community = county.getDown();
            while(community != null)
            { //checks conditions for given racial group and disadvantaged 
                if(race.equals("African American") &&
                (community.getInfo().getPrcntAfricanAmerican() * 100 >= userPrcntage) &&
                (community.getInfo().getAdvantageStatus().equals("False"))) {
                    ctr++;
                }
                else if(race.equals("Native American") &&
                (community.getInfo().getPrcntNative() * 100 >= userPrcntage) &&
                (community.getInfo().getAdvantageStatus().equals("False"))) {
                    ctr++;
                }
                else if(race.equals("Asian American") &&
                (community.getInfo().getPrcntAsian() * 100 >= userPrcntage) &&
                (community.getInfo().getAdvantageStatus().equals("False"))) {
                    ctr++;
                }
                else if(race.equals("White American") &&
                (community.getInfo().getPrcntWhite() * 100 >= userPrcntage) &&
                (community.getInfo().getAdvantageStatus().equals("False"))) {
                    ctr++;
                }
                else if(race.equals("Hispanic American") &&
                (community.getInfo().getPrcntHispanic() * 100 >= userPrcntage) &&
                (community.getInfo().getAdvantageStatus().equals("False"))) {
                    ctr++;
                }
                community = community.getNext();
            }
            county = county.getNext();
        }
        state = state.getNext();
    }

    
    return ctr; // update this line
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        // WRITE YOUR METHOD HERE
        ArrayList<StateNode> states = new ArrayList<StateNode>();

        // Traverse the linked list structure layer by layer
        StateNode state = firstState;
        while(state != null) 
        {
            CountyNode county = state.getDown();
            while(county != null) 
            {
                CommunityNode community = county.getDown();
                while(community != null) 
                {
                    if(community.getInfo().getPMlevel() >= PMlevel) 
                    {
                        if(states.isEmpty() || !states.contains(state))
                        {
                            states.add(state);
                        }
                    }
                    community = community.getNext();
                }
                county = county.getNext();
            }
            state = state.getNext();
        }
                    
        
    
        return states; // Return the list of states with high pollution levels
    }
    

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

	// WRITE YOUR METHOD HERE
    int ctr = 0;
    //iterate through linked structure 
    StateNode state = firstState; 
    while(state != null) 
    {
        CountyNode county = state.getDown();
        while(county != null)
        {
            CommunityNode community = county.getDown();
            while(community != null) 
            {
                if(community.getInfo().getChanceOfFlood() >= userPercntage)
                {
                    ctr++;
                }
                community = community.getNext();
            }
            county = county.getNext();
        }
        state = state.getNext();
    }
        
        return ctr; // update this line
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

	//WRITE YOUR METHOD HERE
    ArrayList<CommunityNode> lowIncome = new ArrayList<CommunityNode>();
    //traverse to find specific state
    StateNode state = firstState; 
    while(state != null && !state.getName().equals(stateName))
    {
        state = state.getNext();
    }
    if(state != null) //check if state is found
    {
        
        //traverse linked list connected to state until we reach community 
        CountyNode county = state.getDown();
        while(county != null) 
        {
            CommunityNode community = county.getDown();
            while(community != null) 
            {
                if(lowIncome.size() < 10) //check if ArrayList has 10 communities 
                {
                    lowIncome.add(community);

                }
                else
                {
                    //it has 10 communities and we need to find lowest percent poverty line
                    double povertyLine = Double.MAX_VALUE;
                    
                    int index = 0;
                    for(int i = 0; i < lowIncome.size(); i++)
                    {
                        double povertyLine2 = lowIncome.get(i).getInfo().getPercentPovertyLine();
                        if(povertyLine2 < povertyLine)
                        {
                            povertyLine = povertyLine2; //replace lowest poverty line with community we're at if it has higher percent that lowest
                            index = i; 
                        }
                    }
                    
                    if(community.getInfo().getPercentPovertyLine() > povertyLine)
                    {//replace community with high poverty line if current has lower poverty line
                        lowIncome.set(index, community);
                    }
                }
                community = community.getNext();
            }
            county = county.getNext();
        }

    }

        return lowIncome; // update this line
    }
}
    
