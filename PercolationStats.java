/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int gridSize;
    private int iterations;
    // private double[] fracOfOpenSites;
    private double[] openSiteCountList;

    // perform independent trials on a n-by-n grid
    public PercolationStats(int gSize, int trials) {
        if (gSize <= 0 || trials <= 0)
            throw new IllegalArgumentException("Passed args less than zero!");
        gridSize = gSize;
        iterations = trials;
        openSiteCountList = new double[trials];
        Percolation currentPerc;
        for (int i = 0; i < trials; i++) {
            currentPerc = new Percolation(gridSize);
            while (!currentPerc.percolates()) {
                // no need to check if it's open, as this will keep running until it percolates
                currentPerc.open(StdRandom.uniform(gridSize) + 1, StdRandom.uniform(gridSize) + 1);
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
        return StdStats.mean(fracOfOpenSites()) - ((1.96 * StdStats.stddev(fracOfOpenSites()))
                / Math
                .sqrt(iterations));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return StdStats.mean(fracOfOpenSites()) + ((1.96 * StdStats.stddev(fracOfOpenSites()))
                / Math
                .sqrt(iterations));
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
        double[] fracOfOpenSites = new double[iterations];
        for (int i = 0; i < iterations; i++) {
            fracOfOpenSites[i] = openSiteCountList[i] / ((double) gridSize * gridSize);
        }
        return fracOfOpenSites;
    }
}
