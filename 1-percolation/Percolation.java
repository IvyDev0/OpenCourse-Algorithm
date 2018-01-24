import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF ufGrid;
    private int numOpenSites;
    private boolean[][] isOpen;
    private int gridLen;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if(n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        ufGrid = new WeightedQuickUnionUF(n * n + 2);
        numOpenSites = 0;
        isOpen = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                isOpen[i][j] = false;
            }
        }
        gridLen = n;

        // virtual top: 0; virtual bottom node: n^2+1
        for (int j = 1; j <= n; j++) {
            ufGrid.union(j, 0);
            ufGrid.union(j + n * (n-1), n * n + 1);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        int n = gridLen;
        if(row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IllegalArgumentException();
        }
        if(!isOpen(row, col)) {
            numOpenSites++;
            isOpen[row-1][col-1] = true;

            int idx = (row - 1) * n + col;
            // check the connection with four neighboors
            if(isOpen(row-1, col) && (!ufGrid.connected(idx, idx - n))) {
                ufGrid.union(idx, idx - n);
            }
            if(isOpen(row+1, col) && (!ufGrid.connected(idx, idx + n))) {
                ufGrid.union(idx, idx + n);
            }
            if(isOpen(row, col-1) && (!ufGrid.connected(idx, idx - 1))) {
                ufGrid.union(idx, idx - 1);
            }
            if(isOpen(row, col+1) && (!ufGrid.connected(idx, idx + 1))) {
                ufGrid.union(idx, idx + 1);
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        int n = gridLen;
        if(row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IllegalArgumentException();
        }
        return isOpen[row-1][col-1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        int n = gridLen;
        if(row < 1 || row > n || col < 1 || col > n) {
            throw new java.lang.IllegalArgumentException();
        }
        int idx = (row - 1) * n + col;
        return isOpen(row, col) && ufGrid.connected(0, idx);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return ufGrid.connected(0, gridLen * gridLen + 1);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
