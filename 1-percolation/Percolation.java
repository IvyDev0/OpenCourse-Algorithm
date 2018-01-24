/******************************************************************************
 *  Compilation:  javac-algs4 Percolation.java
 *  Execution:    java-algs4 Percolation
 *  Dependencies: WeightedQuickUnionUF.java
 *
 *  This program takes the grid size n as a command-line argument, and print
 *  the uniformly choosed sites until the system percolates.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private final WeightedQuickUnionUF ufGrid;
    private final WeightedQuickUnionUF ufGridCopy;
    private int numOpenSites;
    private boolean[][] isOpen;
    private final int gridLen;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        ufGrid = new WeightedQuickUnionUF(n * n + 2);
        ufGridCopy = new WeightedQuickUnionUF(n * n + 2);
        numOpenSites = 0;
        isOpen = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isOpen[i][j] = false;
            }
        }
        gridLen = n;

        // virtual top site: 0; virtual bottom site: n^2+1
        for (int j = 1; j <= n; j++) {
            ufGrid.union(j, 0);
            ufGridCopy.union(j, 0);
            ufGridCopy.union(j + n * (n-1), n * n + 1);
        }
    }

    // map from 2D to 1D indices
    private int mapHelper(int row, int col) {
        return (row - 1) * gridLen + col;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int n = gridLen;
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            numOpenSites++;
            isOpen[row-1][col-1] = true;
            int idx = mapHelper(row, col);

            // check the connection with four neighboors
            if (row - 1 >= 1) {
                if (isOpen(row - 1, col) && (!ufGrid.connected(idx, idx - n))) {
                    ufGrid.union(idx, idx - n);
                    ufGridCopy.union(idx, idx - n);
                }
            }
            if (row + 1 <= n) {
                if (isOpen(row + 1, col) && (!ufGrid.connected(idx, idx + n))) {
                    ufGrid.union(idx, idx + n);
                    ufGridCopy.union(idx, idx + n);
                }
            }
            if (col - 1 >= 1) {
                if (isOpen(row, col - 1) && (!ufGrid.connected(idx, idx - 1))) {
                    ufGrid.union(idx, idx - 1);
                    ufGridCopy.union(idx, idx - 1);
                }
            }
            if (col + 1 <= n) {
                if (isOpen(row, col + 1) && (!ufGrid.connected(idx, idx + 1))) {
                    ufGrid.union(idx, idx + 1);
                    ufGridCopy.union(idx, idx + 1);
                }
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > gridLen || col < 1 || col > gridLen) {
            throw new java.lang.IllegalArgumentException();
        }
        return isOpen[row-1][col-1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > gridLen || col < 1 || col > gridLen) {
            throw new java.lang.IllegalArgumentException();
        }
        int idx = mapHelper(row, col);
        return isOpen(row, col) && ufGrid.connected(0, idx);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (numOpenSites > 0)
            return ufGridCopy.connected(0, gridLen * gridLen + 1);
        else
            return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        Percolation perc = new Percolation(n);

        while (!perc.percolates()) {
            // open a random site
            // row, col: [1,n]
            int row = StdRandom.uniform(1, n+1);
            int col = StdRandom.uniform(1, n+1);
            StdOut.println(row + " " + col);

            perc.open(row, col);
        }
    }
}
