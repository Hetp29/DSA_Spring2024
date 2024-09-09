public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        int r = grid.length; REPRESENTS THE NUMBER OF ROWS IN THE GRID
        int c = grid[0].length; NUMBER OF COLUMNS IN THE GRID 
        int communities = 0; //counter for number of communities
        WeightedQuickUnionUF unite = new WeightedQuickUnionUF(r, c); //OBJECT TO TRACK CONNECTED CELLS USING WEIGHTED QUICK UNION

        ITERATE THROUGH CELL IN THE GRID 
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                if (grid[i][j]) { //IF THE CELL IS ALIVE

                    if(grid[(i - 1 + r) % r][j]) unite.union(i, j, (i - 1 + r) % r, j); //'(I - 1 + R) % R' HANDLES CASE WHEN CURRENT CELL IN FIRST ROW ('I = 0'), WRAPS AROUND THE LAST ROW
                    
                    if(grid[i][(j - 1 + c) % c]) unite.union(i, j, i, (j - 1 + c) % c); //left
                    '(J - 1 + C) % R' HANDLES CASE WHEN CELL IS IN FIRST COLUMN ('J = 0'). IT WRAPS AROUND TO LAST COLUMN 

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
