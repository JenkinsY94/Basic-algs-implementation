import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ArrayDeque;
public class Solver {
    /*********************************************************************
    a search node inner class. provides 2 compare method based on 
    hamming priority function and manhattan priority function respectively. 
    **********************************************************************/
    private class Node {
        Board board;
        int moves;
        Node prev;
        int manDis;
        public Node(Board board, int moves, Node prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            manDis = board.manhattan();
        }
        public Comparator<Node> HamPriority() {return new ByHamming();}
        public Comparator<Node> ManPriority = new ByManhattan();
        private class ByHamming implements Comparator<Node> {
            public int compare(Node a, Node b) {
                if (a.board.hamming() + a.moves
                    > b.board.hamming() + b.moves)
                    return 1;
                else if (a.board.hamming() + a.moves 
                         < b.board.hamming() + b.moves)
                    return -1;
                else return 0;
            }
        }
        private class ByManhattan implements Comparator<Node> {
            // breaks tie here can speed up the solver.
            public int compare(Node a, Node b) {
                if (a.manDis + a.moves > b.manDis + b.moves)
                    return 1;
                if (a.manDis + a.moves < b.manDis + b.moves)
                    return -1;
                if (a.moves > b.moves)
                    return -1;
                if (a.moves < b.moves)
                    return 1;
                return 0;
            }
        }
    }
    private Node node;  
    private Node nodeTwin;
    private Node first;
    private Node firstTwin;
    /****************************************************************
        this constructor serves to 
        find a solution to the initial board(using the A* algorithm)
        Critical Optimization: don't enque the neighbor that is 
        same as previous search node's board.

        the constructor solves initial board and its twin node simutaneously
        in order to check if initial board is solvable. 
    *****************************************************************/
    public Solver(Board initial) {
        first = new Node(initial, 0, null);
        firstTwin = new Node(initial.twin(), 0, null);
        // how to initialize PQ with comparator?.....................
        MinPQ<Node> pq = new MinPQ<Node>(first.ManPriority);
        MinPQ<Node> pqTwin = new MinPQ<Node>(firstTwin.ManPriority);
        // MinPQ<Node> pq = new MinPQ<Node>(first.HamPriority()); 
        pq.insert(first);
        pqTwin.insert(firstTwin);
        while (true) {
            if (!pq.isEmpty()) node = pq.delMin(); 
            if (!pqTwin.isEmpty()) nodeTwin = pqTwin.delMin(); 
            if (node.board.isGoal()) 
                break;
            if (nodeTwin.board.isGoal()) 
                break;
            else {
                for (Board b : node.board.neighbors()) {
                    if (node.prev == null || !b.equals(node.prev.board))
                        pq.insert(new Node(b, node.moves+1, node));
                }
                for (Board b : nodeTwin.board.neighbors()) {
                    if (nodeTwin.prev == null || !b.equals(nodeTwin.prev.board))
                        pqTwin.insert(new Node(b, nodeTwin.moves+1, nodeTwin));
                }
            }
        }
        
    }
    // is the initial board solvable?
    public boolean isSolvable() {
        return node.board.isGoal();
    }
    // min number of moves to solve initial board; -1 if unsolvable.
    public int moves() {
        if (isSolvable()) return node.moves;
        else return -1;
    }
    // sequence of boards in a shortest solution; null if unsolvable.
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        ArrayDeque<Board> steps = new ArrayDeque<Board>();
        Node newNode = node;
        while (newNode != null) {
            steps.addFirst(newNode.board);
            newNode = newNode.prev; 
        }
        return steps;
    }
    // solve a slider puzzle
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible.");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board: solver.solution())
                StdOut.println(board);
            StdOut.println("Minimum number of moves = " + solver.moves());
        }


    }
}
