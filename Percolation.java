/* *****************************************************************************
 *  Name: Daniel Ochoa
 *  Date: 02/10/20
 *  Description: Percolation assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid;
    private int size;
    private int noOfOpenSites;
    private WeightedQuickUnionUF uf;
    private int indexOfTopVirtualNode;
    private int indexOfBottomVirtualNode;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("You must pass a number > than 0");
        size = n;
        grid = new int[n][n];
        noOfOpenSites = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = 0;
            }
        }
        // why add two? represents our two virtual sites. first of two, top node, second of two, bottom node.
        int ufList = (n * n) + 2;
        indexOfBottomVirtualNode = ufList - 1;
        indexOfTopVirtualNode = ufList - 2;
        uf = new WeightedQuickUnionUF(ufList);
    }

    // opens the site (row, col) if it's not open already. remember, starts at 1,1 not 0,0.
    // increments noOfOpenSites var.
    public void open(int row, int col) {
        validateBounds(row, col);
        row--;
        col--;
        if (grid[row][col] == 1) return;

        grid[row][col] = 1;
        noOfOpenSites++;
        connectToAdjacentSites(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateBounds(row, col);
        return grid[row - 1][col - 1] == 1;
    }

    // is the site full (row, col) (closed?)
    public boolean isFull(int row, int col) {
        return !isOpen(row, col);
    }

    // returns the number of open sites (1 == open)
    public int numberOfOpenSites() {
        // StdOut.println("requested numberOfOpenSites: " + noOfOpenSites);
        return noOfOpenSites;
    }

    // does the grid percolate? Basically, our virtual sites need to be linked!
    public boolean percolates() {
        return uf.connected(indexOfTopVirtualNode, indexOfBottomVirtualNode);
    }

    // test client (optional)
    public static void main(String[] args) {
        // let's test xyToPosition
        Percolation perc = new Percolation(4);
        System.out.println(
                "for (1, 4) in size 4: " + perc.xyToPosition(0, 3) + " and expects 3"); // expects 3
        System.out.println(
                "for (2, 1) in size 4: " + perc.xyToPosition(1, 0) + " and expects 4"); // expects 4
        System.out.println(
                "for (4, 4) in size 4: " + perc.xyToPosition(3, 3)
                        + " and expects 15"); // expects 15
    }

    /**
     * private methods.
     */

    /**
     * Takes a row col value, and returns the location as if it were a 1d single row. Expects rows
     * and columns to start at 0.
     *
     * @param row row value
     * @param col col value
     * @return int
     */
    private int xyToPosition(int row, int col) {
        return (row * size) + col;
    }

    private void validateBounds(int row, int col) {
        if (row > size || row <= 0 || col > size || col <= 0)
            throw new IllegalArgumentException(
                    "row or col is out of bounds for grid: (" + row + ", " + col + ")");
    }

    /**
     * Create connections using `union(x, y)` to adjacent sites IF they are open, and if they
     * exist.
     */
    private void connectToAdjacentSites(int row, int col) {
        int site = xyToPosition(row, col);
        // process top site and check if site above is open.
        if ((row - 1) >= 0 && (row - 1) < size && grid[row - 1][col] == 1) {
            uf.union(site, xyToPosition(row - 1, col));
        }

        // process right site.
        if ((col + 1) < size && grid[row][col + 1] == 1) {
            uf.union(site, xyToPosition(row, col + 1));
        }

        // process bottom site
        if ((row + 1) < size && (row + 1) >= 0 && grid[row + 1][col] == 1) {
            uf.union(site, xyToPosition(row + 1, col));
        }

        // process left site
        if ((col - 1) >= 0 && (col - 1) < size && grid[row][col - 1] == 1) {
            uf.union(site, xyToPosition(row, col - 1));
        }

        // connect to virtual top, or virtual bottom, depending on row site is. It is not mutually
        // exclusive in the case of a 1 by 1 grid.
        if (row == 0) {
            // connect to indexOfTopVirtualNode
            // StdOut.println("Virtual connection:: " + site + ", " + indexOfTopVirtualNode);
            uf.union(site, indexOfTopVirtualNode);
        }

        if (row == size - 1) {
            uf.union(site, indexOfBottomVirtualNode);
            // StdOut.println("Virtual connection:: " + site + ", " + indexOfBottomVirtualNode);
        }
    }
}
