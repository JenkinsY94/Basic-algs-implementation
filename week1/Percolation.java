import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private boolean[][] grid;
    private int size;
    private WeightedQuickUnionUF uf_1;
    private WeightedQuickUnionUF uf_2;
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException(
                      "size of grid must be greater than 0.");
        }
        size = n;
        grid = new boolean[n][n];
        // uf_1 contains virtual nodes. top virtual node (0),
        // bottom virtual node (size*size+1).
        uf_1 = new WeightedQuickUnionUF(size*size+2);
        // uf_2 only contains the top virtual node(0).
        uf_2 = new WeightedQuickUnionUF(size*size+1);
        
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
    }
    // open site at (i,j) if it is not open already
    public void open(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size) {
            throw new IndexOutOfBoundsException(
                      "Index Out of Bound in method open");
        }
        grid[i-1][j-1] = true;
        // union the top row opened nodes with (0).
        if (i == 1) {
            uf_1.union((i-1)*size+j, 0);
            uf_2.union((i-1)*size+j, 0);
        }
        // union the bottom row opened nodes with (size*size+1).
        if (i == size) {
            uf_1.union((i-1)*size+j, size*size+1);
        }
        if (i-1 > 0 && grid[i-2][j-1]) {
            uf_1.union((i-1)*size+j, (i-2)*size+j);
            uf_2.union((i-1)*size+j, (i-2)*size+j);
        }
        if (i < size && grid[i][j-1]) {
            uf_1.union((i-1)*size+j, i*size+j);
            uf_2.union((i-1)*size+j, i*size+j);
        }
        if (j-1 > 0 && grid[i-1][j-2]) {
            uf_1.union((i-1)*size+j, (i-1)*size+j-1);
            uf_2.union((i-1)*size+j, (i-1)*size+j-1);
        }
        if (j < size && grid[i-1][j]) {
            uf_1.union((i-1)*size+j, (i-1)*size+j+1);
            uf_2.union((i-1)*size+j, (i-1)*size+j+1);
        }

    }
    // check if site is open?
    public boolean isOpen(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size) {
            throw new IndexOutOfBoundsException(
                      "Index Out of Bound in method isOpen");
        }
        return grid[i-1][j-1];
    }
    // check if site is full?
    public boolean isFull(int i, int j) {
        if (i <= 0 || i > size || j <= 0 || j > size) {
            throw new IndexOutOfBoundsException(
                      "Index Out of Bound in method isFull");
        }
        // use uf_2 to avoid backwash problem.
        return uf_2.connected((i-1)*size+j, 0);
    }
    // does the system percolate?
    public boolean percolates() {
        if (uf_1.connected(0, size*size+1)) return true;
        return false;
    }

    // test client
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int  i, j;
        double thresh;
        int count = 0;
        Percolation p = new Percolation(n);

        while (!p.percolates()) {
            // randomly pick a site and open it
            i = StdRandom.uniform(n) + 1;
            j = StdRandom.uniform(n) + 1;
            if (!p.isOpen(i, j)) {
                p.open(i, j);
                count++;
            }
        }
        thresh = (float) count /(n*n);
        StdOut.printf("the threshhold is: %5.3f when n = %d\ncount = %d\n", 
                       thresh, n, count);
    }
}
