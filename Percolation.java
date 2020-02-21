/* *****************************************************************************
 *  Name: Daniel Ochoa
 *  Date: 02/10/20
 *  Description: Percolation assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid; // false == isOpen. true = is not open
    private int size;
    private int noOfOpenSites;
    private WeightedQuickUnionUF uf;
    private int indexOfTopVirtualNode;
    private int indexOfBottomVirtualNode;
    private int[] openSitesAtBottom;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("You must pass a number > than 0");
        size = n;
        openSitesAtBottom = new int[size];
        grid = new boolean[n][n];
        noOfOpenSites = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
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
        if (grid[row][col]) return;

        grid[row][col] = true;
        noOfOpenSites++;

        connectAdjacentSites(row, col);
    }

    /**
     * By connecting sites, we make sure they get filled or isFull will return true for them.
     *
     * @param row row
     * @param col col
     */
    private void connectAdjacentSites(int row, int col) {
        int site = xyToPosition(row, col);

        // drip from top
        if (row == 0) {
            uf.union(indexOfTopVirtualNode, site);
        }

        // check adjacent blocks, if is open, and is full
        // top
        if (row - 1 >= 0 && grid[row - 1][col]) {
            uf.union(site, xyToPosition(row - 1, col));
        }

        // left
        if (col - 1 >= 0 && grid[row][col - 1]) {
            uf.union(site, xyToPosition(row, col - 1));
        }

        // bottom
        if (row + 1 < size && grid[row + 1][col]) {
            uf.union(site, xyToPosition(row + 1, col));
        }

        // right
        if (col + 1 < size && grid[row][col + 1]) {
            uf.union(site, xyToPosition(row, col + 1));
        }

        if (row == size - 1) {
            // keep track of sites at bottom row, using col as index.
            openSitesAtBottom[col] = site;
        }
    }

    /**
     * Is the site open?
     *
     * @param row row
     * @param col col
     * @return boolean
     */
    public boolean isOpen(int row, int col) {
        validateBounds(row, col);
        return grid[row - 1][col - 1];
    }

    /**
     * Is the site full? As in, is it full of water (open and full).
     *
     * @param row row
     * @param col col
     * @return boolean
     */
    public boolean isFull(int row, int col) {
        validateBounds(row, col);
        row--;
        col--;
        if (!grid[row][col]) return false;
        int site = xyToPosition(row, col);
        return uf.connected(site, indexOfTopVirtualNode);
    }

    // returns the number of open sites (1 == open)
    public int numberOfOpenSites() {
        return noOfOpenSites;
    }

    /**
     * Does the grid percolate? We can tell if the virtual top site is connected to the bottom
     * virtual site.
     *
     * @return boolean
     */
    public boolean percolates() {
        // makes sure to connect any full (isFull) node in last row to bottom virtual node.
        // NOTE: edge case is handled for when size is 1. Doesn't seem to be a good solution
        // to make it efficient but I can't think of another way right now.
        for (int i = 0; i < openSitesAtBottom.length; i++) {
            if (openSitesAtBottom[i] != 0 || size == 1) {
                if (uf.connected(openSitesAtBottom[i], indexOfTopVirtualNode)) {
                    uf.union(indexOfBottomVirtualNode, openSitesAtBottom[i]);
                }
            }
        }
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
}
