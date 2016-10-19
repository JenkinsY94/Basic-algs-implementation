import edu.princeton.cs.algs4.*;

public class Percolation_1 {
	private boolean grid[][];
	private int size;
	QuickFindUF uf;
	//create n-by-n grid, with all sites blocked
	public Percolation_1(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("size of grid must be greater than 0.");
		}
		size = n;
		grid = new boolean[n][n];
		uf = new QuickFindUF(size*size);
		for(int i = 0; i<n; i++) 
			for(int j = 0; j<n; j++) {
				grid[i][j] = false;
			}
	}
	// open site at (i,j) if it is not open already
	public void open(int i, int j) {
		grid[i][j] = true;
		if(i > 0 && grid[i-1][j]) {
			uf.union(i*size+j, (i-1)*size+j);
		}
		if(i < size-1 && grid[i+1][j])	{
			uf.union(i*size+j, (i+1)*size+j);
		}
		if(j > 0 && grid[i][j-1]) {
			uf.union(i*size+j, i*size+j-1);
		}
		if(j < size-1 && grid[i][j+1]) {
			uf.union(i*size+j, i*size+j+1);
		}

	}
	// check if site is open?
	public boolean isOpen(int i, int j) {
		return grid[i][j];
	}
	// check if site is full?
	public boolean isFull(int i, int j) {
		for(int k = 0; k < size; k++) {
			if(grid[0][k] == true && uf.connected(i*size+j, k)) 
				return true;
		}
		return false;
	}
	// does the system percolate?
	public boolean percolates() {
		for(int j=0; j<size;j++) {
			if(isFull(size-1, j)) return true;
		}
		return false;
	}

	//test client
	public static void main(String[] args) {
		Stopwatch time = new Stopwatch();
		int n = Integer.parseInt(args[0]);
		int  i, j;
		float thresh;
		int count = 0;
		Percolation_1 p = new Percolation_1(n);

		while (!p.percolates()) {
			// randomly pick a site and open it
			i = StdRandom.uniform(n);
			j = StdRandom.uniform(n);
			if(!p.isOpen(i,j)) {
				p.open(i,j);
				count ++;
			}
		}
		thresh = (float)count /(n*n);
		StdOut.printf("the threshhold is: %5.3f when n = %d\ncount = %d\n",thresh,n,count);
	}
}
