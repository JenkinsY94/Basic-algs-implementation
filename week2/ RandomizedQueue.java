// in order to satisfy the "constant amortized time for each operation"
// implement this queue using resizing `array` with a lazy approach - 
// after dequeue, dont shift elements left, just let the reoved one to be null.
// each time resizing the array, remove those null elements.
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int validN;
    private int invalidN;
    private Item[] itemList;
    // construct an empty randomized queue
    public RandomizedQueue() {
        validN = invalidN = 0;
        itemList = (Item[]) new Object[1];
    }
    // is the queue empty?
    public boolean isEmpty() {
        return validN == 0;
    }
    // return the number of items on the queue
    public int size() {
        return validN;
    }
    // resize the array
    private Item[] resize(int n) {
        Item[] item = (Item[]) new Object[n];
        for (int i = 0, j = 0; i < validN + invalidN; i++) {
            if (itemList[i] != null) {
                item[j++] = itemList[i];
            }  
        }
        // after resizing, invalidN = 0. because no "null"  
        // in between valid elements.
        invalidN = 0;
        return item;
    }
    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException(
                      "You are not allowed to add a null item.");
        }
        if ((validN + invalidN) == itemList.length) {
            itemList = resize(2*itemList.length);
        }
        itemList[validN + invalidN] = item; 
        validN++;
    }
    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                      "No element left in the RandomizedQueue.");
        }
        if (validN + invalidN < itemList.length/4) {
            itemList = resize(itemList.length/2);
        }
        int index = StdRandom.uniform(validN);
        int i = 0;
        Item item = null;
        for (int indexTrans = 0; indexTrans < validN+invalidN; indexTrans++) {
            // indexTrans is the actual index 
            // in the array containing invalid elements.
            if (itemList[indexTrans] != null) {
                if (i++ == index) {
                    item = itemList[indexTrans];
                    itemList[indexTrans] = null;
                    validN--;
                    invalidN++;
                    break;
                }
            }
        }
        return item;
    }
    // return (but do not remove)  a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                      "No element left in the RandomizedQueue.");
        }
        int index = StdRandom.uniform(validN);
        int i = 0;
        Item item = null;
        for (int indexTrans = 0; indexTrans < validN+invalidN; indexTrans++) {
            if (itemList[indexTrans] != null) {
                if (i++ == index) {
                    item = itemList[indexTrans];
                    break;
                }
            }
        }
        return item;
    }
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new SequenceIterator();
    }
    private class SequenceIterator implements Iterator<Item> {
        private int n = validN + invalidN;
        private int count = validN;
        private int index = 0;
        // generate random list of index
        private int[] randomList = new int[n];
        public SequenceIterator() {
            for (int i = 0; i < n; i++) {
                randomList[i] = i;
            }
            StdRandom.shuffle(randomList);
        }
        public boolean hasNext() { return count > 0; }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException(
                          "no element left, you cannot call next() now.");
            } 
            while (index < n) {
                if (itemList[randomList[index]] != null) {
                    count--;
                    return itemList[randomList[index++]];
                } else {
                    index++;
                }
            }
            return null;
        }
        public void remove() {
            throw new UnsupportedOperationException(
                      "remove method not supported.");
        }
    } 
    // unit testing
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        StdOut.println(rq.dequeue() + "\t" + rq.sample());
        StdOut.println("the size: " + rq.size() + ", invalidN: " + rq.invalidN);
        rq.enqueue(999);
        // StdOut.println(rq.dequeue() + "\t" + rq.sample());
        StdOut.println("the size: " + rq.size() + ", invalidN: " + rq.invalidN);
        for(int item : rq) StdOut.print(item + " "); 
        /* int[] randomList = new int[5];
        for (int i = 0; i < 5; i++) {
                randomList[i] = i;
        }
        StdRandom.shuffle(randomList);
        StdOut.println("after shuffle: ");
        for(int i=0; i<5; i++) StdOut.print(randomList[i] + " "); */
    }
}
