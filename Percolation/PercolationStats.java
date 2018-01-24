/******************************************************************************
 *  Compilation:  javac-algs4 PercolationStats.java
 *  Execution:    java-algs4 PercolationStats n T
 *  Dependencies: Percolation.java
 *                StdRandom.java StdStats.java StdOut.java
 *
 *  This program takes the grid size n and the number of experiments T as
 *  two command-line arguments, and prints the sample mean, sample standard
 *  deviation, and the 95% confidence interval for the percolation threshold.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double mean;
    private final double stddev;
    private final double cfL;
    private final double cfH;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        double[] siteNum = new double[trials];
        double trialNum = (double) trials;
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                // open a random site
                // row, col: [1,n]
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                perc.open(row, col);
            }
            siteNum[i] = (double) perc.numberOfOpenSites() / (double) (n*n);
        }

        mean = StdStats.mean(siteNum);
        stddev = StdStats.stddev(siteNum);
        cfL = mean - CONFIDENCE_95 * stddev / Math.sqrt(trialNum);
        cfH = mean + CONFIDENCE_95 * stddev / Math.sqrt(trialNum);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return cfL;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return cfH;
    }

    // test client
    // performs T independent computational experiments on an n-by-n grid
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percStats = new PercolationStats(n, t);
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo() + ", " + percStats.confidenceHi() +"]");
    }
}
