import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int n;
    private class Node {
        private Item item;
        private Node prev;
        private Node next;
    }
    // construct an empty deque
    public Deque() {
        n = 0;
    }
    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }
    // return the number of items on the deque
    public int size() {
        return n;
    }
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException(
                    "you cannot add a null item to the front.");
        }
        if (!isEmpty()) {
            Node oldFirst = first;
            first = new Node(); 
            first.item = item;
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        else {
            first = new Node();
            first.item = item;
            last = first;
        }
        n++;
    }
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException(
                    "you cannot add a null item to the end.");
        }
        if (!isEmpty()) {
            Node oldLast = last;
            last = new Node();
            last.item = item;
            last.prev = oldLast;
            oldLast.next = last;
        } else {
            last = new Node();
            // after removeFirst final element, first=null.
            first = new Node(); 
            last.item = item;
            first = last;
        }
        n++;
    }
    // remove and return the item from the first
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                      "No element to remove.");
        }
        Item item = first.item;
        if (size() == 1) {
            first = last = new Node();
        } else {
            first = first.next;
            first.prev = null;
        }
        n--;
        return item;
    }
    // remove and return the item from the end 
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                      "No element to remove.");
        }
        Item item = last.item;
        if (size() == 1) {
            last = first = new Node(); // avoid last = null
        } else {
            last = last.prev;
            last.next = null;
        }
        n--;
        return item;
    }
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new SequenceIterator();
    }
    private class SequenceIterator implements Iterator<Item> {
        private Node n = first;
        public boolean hasNext() {
            return n != null; 
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException(
                          "no element left, you cannot call next() now.");
            }
            Item current = n.item;
            n = n.next;
            return current;
        }
        public void remove() {
            throw new UnsupportedOperationException(
                      "remove method not supported.");
        }
    } 
    // unit testing
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        /* while (!StdIn.isEmpty()) {
            String s = StdIn.readString(); 
            if (!s.equals("-")) deque.addLast(s);
            else if (!deque.isEmpty()) {
                StdOut.print(deque.removeFirst() + " ");
            }
        }
        StdOut.println("(" + deque.size() + " strings left in the stack.)");
        for (String s : deque) StdOut.println(s); */
       
        // test corner case performance of add()/remove()
        StdOut.println(deque.isEmpty());
        deque.addLast("to");
        StdOut.println(deque.isEmpty());
        StdOut.print(deque.removeLast() + " \n");
        StdOut.println(deque.isEmpty());
        deque.addLast("be (add this after deque is empty(not initialization case.).)");
        StdOut.println(deque.isEmpty());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.isEmpty());
    }
}
