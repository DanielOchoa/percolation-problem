/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int n;
    private int T;
    private double[] fracOfOpenSites;
    private double[] openSiteCountList;

    // perform independent trials on a n-by-n grid
    public PercolationStats(int gridSize, int trials) {
        if (gridSize <= 0 || trials <= 0)
            throw new IllegalArgumentException("Passed args less than zero!");
        n = gridSize;
        T = trials;
        openSiteCountList = new double[T];
        Percolation currentPerc;
        for (int i = 0; i < T; i++) {
            currentPerc = new Percolation(n);
            while (!currentPerc.percolates()) {
                int col = StdRandom.uniform(n) + 1;
                int row = StdRandom.uniform(n) + 1;
                // no need to check if it's open, as this will keep running until it percolates
                currentPerc.open(row, col);
            }
            openSiteCountList[i] = currentPerc.numberOfOpenSites();
        }
    }

    // sample mean of percolation threshold.
    public double mean() {
        return StdStats.mean(fracOfOpenSites());
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fracOfOpenSites());
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(T));
    }

    // test client (see below)
    public static void main(String[] args) {
        String n = args[0];
        String t = args[1];
        int nInt = Integer.parseInt(n);
        int tInt = Integer.parseInt(t);
        PercolationStats pstats = new PercolationStats(nInt, tInt);
        StdOut.println("mean                    = " + pstats.mean());
        StdOut.println("stddev                  = " + pstats.stddev());
        StdOut.println(
                "95% confidence interval = [" + pstats.confidenceLo() + ", " + pstats.confidenceHi()
                        + "]");
    }

    private double[] fracOfOpenSites() {
        // cached
        if (fracOfOpenSites != null) {
            return fracOfOpenSites;
        }
        fracOfOpenSites = new double[T];
        for (int i = 0; i < T; i++) {
            fracOfOpenSites[i] = openSiteCountList[i] / ((double) n * n);
        }
        return fracOfOpenSites;
    }
}
