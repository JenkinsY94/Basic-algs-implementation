import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Subset {
    // using Deque does not saisfy the random requirement.
    /* public static void main(String[] args) {
        int k = Integer.parseInt(args[0]); // assume that 0<=k<=n
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString(); 
            deque.addLast(s);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(deque.removeLast());
        }
    } */
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int count = 0;
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while(!StdIn.isEmpty()) {
            String s = StdIn.readString();
            rq.enqueue(s);
        }
        for (String str : rq) {
            if (count++ < k) {
                StdOut.println(str);
            }
        }
    }
}
