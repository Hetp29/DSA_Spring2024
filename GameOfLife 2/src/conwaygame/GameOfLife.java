package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        int numRows = StdIn.readInt();
        int numCols = StdIn.readInt();
        grid = new boolean [numRows][numCols];
        
        for(int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j] = StdIn.readBoolean();
                }
            }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        return grid[row][col]; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j]) {
                    return true;
                }
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        // WRITE YOUR CODE HERE
        int alive = 0;
        int nRows = grid.length;
        int nCols = grid[0].length;
        for(int i = row - 1; i <= row + 1; i++) {
            for(int j = col - 1; j <= col + 1; j++) {
            if(i == row && j == col) { //checks that cell being checked is not cell we're finding neighbors for
                continue;
            }
            int r = (i + nRows) % nRows;
            int c = (j + nCols) % nCols;

            if(grid[r][c]) {
                alive++;}
            }
        }
        //for(int i = 0; i < gRows.length; i++)
        return alive; // update this line, provided so that code compiles
    }


    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE
        boolean [][] newGame = new boolean [grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++) {
                int alive = numOfAliveNeighbors(i, j);

                if(grid[i][j]) {
                    if(alive == 2  || alive == 3){
                        newGame[i][j] = true;
                    }
                    else{
                        newGame[i][j]= false;
                    }
                }
                else{
                    if(alive==3){
                        newGame[i][j] = true;
                    }
                    else {
                        newGame[i][j] = false;
                    }
                }
            }
        }
        return newGame;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        boolean [][]  temp = computeNewGrid();
        grid=temp;
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for(int i =0 ; i<n; i++){
            grid = computeNewGrid();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        int r = grid.length;
        int c = grid[0].length;
        int communities = 0; //counter for number of communities
        WeightedQuickUnionUF unite = new WeightedQuickUnionUF(r, c); //object to track connected cells

        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                if (grid[i][j]) { //if cell is alive
                    if(grid[(i - 1 + r) % r][j]) unite.union(i, j, (i - 1 + r) % r, j); //above
                    if(grid[i][(j - 1 + c) % c]) unite.union(i, j, i, (j - 1 + c) % c); //left
                    if(grid[(i + 1) % r][j]) unite.union(i, j, (i + 1) % r, j);  //below
                    if(grid[i][(j + 1) % c]) unite.union(i, j, i, (j + 1) % c); //right
                    if(grid[(i - 1 + r) % r][(j - 1 + c) % c]) unite.union(i, j, (i - 1 + r) % r, (j - 1 + c) % c); //top left
                    if(grid[(i - 1 + r) % r][(j + 1) % c]) unite.union(i, j, (i - 1 + r) % r, (j + 1) % c); //top right
                    if(grid[(i + 1) % r][(j - 1 + c) % c]) unite.union(i, j, (i + 1) % r, (j - 1 + c) % c); //bottom left 
                    if(grid[(i + 1) % r][(j + 1) % c]) unite.union(i, j, (i + 1) % r, (j + 1) % c); //bottom right
                }
            }
        }
        boolean[] roots = new boolean[r * c]; //array tracks roots
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                if(grid[i][j]) {//if current cell in grid is alive
                    int markRoot = unite.find(i, j); //mark root as seen
                    roots[markRoot] = true;
                }
            }
        }
        for (int i = 0; i < roots.length; i++) {
            if(roots[i]) {
                communities++; //if root is true, community is added
            }
        }
        return communities;
    }
    
}
