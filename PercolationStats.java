/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int n;
    private int T;
    private Percolation[] percs;
    private double[] fracOfOpenSites;

    // perform independent trials on a n-by-n grid
    public PercolationStats(int gridSize, int trials) {
        if (gridSize <= 0 || trials <= 0)
            throw new IllegalArgumentException("Passed args less than zero!");
        n = gridSize;
        T = trials;

        percs = new Percolation[T];
        for (int i = 0; i < T; i++) {
            percs[i] = new Percolation(n);
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
        if (fracOfOpenSites != null) {
            return fracOfOpenSites;
        }
        fracOfOpenSites = new double[percs.length];
        for (int i = 0; i < percs.length; i++) {
            fracOfOpenSites[i] = percs[i].numberOfOpenSites() / (double) n * n;
        }
        return fracOfOpenSites;
    }
}
