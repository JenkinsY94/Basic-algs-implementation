import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
public class Board {
    private final int[][] blocks;
    private final int size;
    // construct a board from an n-by-n array
    public Board(int[][] blocks) {
        size = blocks.length;
        this.blocks = new int[size][size];
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                this.blocks[i][j] = blocks[i][j];
    }
    // board dimension n
    public int dimension() {
        return blocks.length;
    }
    // number of blocks out of place
    public int hamming() {
        int number = 0;
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                if (blocks[i][j] != i*size+j+1 && i*size+j+1 != size*size)
                    number++;
        return number;
    }
    // sum of Manhattan distance between blocks and goal
    public int manhattan() {
        int goalRow, goalCol; 
        int distance = 0;
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] > 0) {
                    goalRow = (blocks[i][j] - 1) / size;
                    goalCol = (blocks[i][j] - 1) % size;
                    distance += (Math.abs(i-goalRow) + Math.abs(j-goalCol)); 
                }   
            }
        return distance;
    }
    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j) {
                if (blocks[i][j] != i*size+j+1 && i*size+j+1 != size*size)
                    return false;
            }
        return true;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int i1, j1, i2, j2;
        i1 = j1 = i2 = j2 = 0;
        while ((i1 == i2 && j1 == j2) || blocks[i1][j1] == 0 || blocks[i2][j2] == 0) {
            i1 = StdRandom.uniform(size);
            i2 = StdRandom.uniform(size);
            j1 = StdRandom.uniform(size);
            j2 = StdRandom.uniform(size);
        }
        return exc(i1, j1, i2, j2);
    }
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        // use tostring to compare?
        return this.toString().equals(that.toString());
    }
    /********************************************************
        all neighboring boards. 
        store all neighboring boards in an arraylist and return it.
    **********************************************************/
    public Iterable<Board> neighbors() {
        ArrayList<Board> nb = new ArrayList<Board>();
        int row = 0; 
        int col = 0;
        boolean flag = false;
        for (int i = 0; i < size; ++i) 
            for (int j = 0; j < size; ++j)
                if (blocks[i][j] == 0) {
                    row = i;
                    col = j;
                    flag = true;
                    break;
                }
        if (flag) {
            if (row > 0) nb.add(exc(row, col, row-1, col));
            if (row < size-1) nb.add(exc(row, col, row+1, col));
            if (col > 0) nb.add(exc(row, col, row, col-1));
            if (col < size-1) nb.add(exc(row, col, row, col+1));
        }
        return nb;
    }
    // exchange two blocks in a board and return the new board
    private Board exc(int i1, int j1, int i2, int j2) {
        if (i1 < 0 || i1 >= size || j1 < 0 || j1 >= size 
            || i2 < 0 || i2 >= size || j2 < 0 || j2 >= size)
            throw new IllegalArgumentException(
                "cannt exchange blocks not in the Board.");
        int[][] newBlocks = new int[size][size];
        for (int i = 0; i < size; ++i) 
            for (int j = 0; j < size; ++j) 
                newBlocks[i][j] = blocks[i][j];
        int mid = newBlocks[i1][j1];
        newBlocks[i1][j1] = newBlocks[i2][j2];
        newBlocks[i2][j2] = mid;
        return new Board(newBlocks);
    }
    // string representation of this board 
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(size + "\n");
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                    s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append('\n');
        }
        return s.toString();
    }
    // unit tests 
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial);
        // StdOut.println(initial.twin());
        // StdOut.println(initial.twin());
        for (Board b: initial.neighbors())
            StdOut.println(b);
        // StdOut.println(initial.hamming());
        // StdOut.println(initial.manhattan());
    }
}
