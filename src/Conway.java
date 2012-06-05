/**
 * Created with IntelliJ IDEA.
 * User: Robert
 * Date: 6/4/12
 * Time: 7:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Conway {
    private boolean[][] grid;

    public Conway(boolean[][] grid) {
        this.grid = grid;
    }

    public boolean[][] nextStep() {
        int neighbors;
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            //Any live cell with fewer than two live neighbors dies
            //Any live cell with two or three live neighbours lives on to the next generation.
            //Any live cell with more than three live neighbours dies, as if by overcrowding.
            //Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
            for (int j = 0; j < grid[0].length; j++) {
                neighbors = findNeighbors(i, j);
                if (neighbors > 3 || neighbors < 2) {
                    newGrid[i][j] = false;
                }
                else if (neighbors == 3) {
                    newGrid[i][j] = true;
                } else {
                    newGrid[i][j] = grid[i][j];
                }
            }
        }
        grid = newGrid;
        return newGrid;
    }

    public void setGrid(boolean[][] grid){
        this.grid = grid;
    }

    private int findNeighbors(int i, int j) {
        //using wrap-around formula found from:
        //http://okshaw.com/tools/life/life.htm#
        int neighbors = 0;
        //the maximum number of cells in each row and column
        int m = grid[0].length; //assuming square

        //left
        if (grid[i][(j - 1 + m) % m]) neighbors++;
        //right
        if (grid[i][(j + 1 + m) % m]) neighbors++;
        //up
        if (grid[(i + 1 + m) % m][j]) neighbors++;
        //down
        if(grid[(i-1+m)%m][j]) neighbors++;
        //up-left
        if(grid[(i-1+m)%m][(j-1+m)%m]) neighbors++;
        //up-right
        if(grid[(i-1+m)%m][(j+1+m)%m]) neighbors++;
        //down-left
        if(grid[(i+1+m)%m][(j-1+m)%m]) neighbors++;
        //down-right
        if(grid[(i+1+m)%m][(j+1+m)%m]) neighbors++;

        return neighbors;
    }


}
