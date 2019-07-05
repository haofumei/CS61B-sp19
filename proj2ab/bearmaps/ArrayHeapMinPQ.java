package bearmaps;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.HashMap;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T> {

    private ArrayList<PriorityNode> pq = new ArrayList<>();
    private int size;
    private HashMap<T, Integer> items = new HashMap<>();

    /* Adds an item with the given priority value. Throws an
     * IllegalArgumentExceptionb if item is already present.
     * You may assume that item is never null. */

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException("can not input the same item");
        }
        PriorityNode addNew = new PriorityNode(item, priority);
        pq.add(addNew);
        items.put(item, size());
        swim(size);
        size += 1;
    }

    // new PNode swims to its suitable position
    private void swim(int k) {
        while (pq.get(k).compareTo(pq.get(parent(k))) < 0) {
            swap(k, parent(k));
            k = parent(k);
        }
    }

    // swap the position of two keys
    private void swap(int k, int parent) {
        PriorityNode temp = pq.get(k);
        pq.set(k, pq.get(parent));
        pq.set(parent, temp);
        items.replace(pq.get(k).getItem(), k);
        items.replace(pq.get(parent).getItem(), parent);
    }

    // return the parent of k key
    private int parent(int k) {
        return (k - 1) / 2;
    }

    /* Returns true if the PQ contains the given item. */
    @Override
    public boolean contains(T item) {
        if (items.containsKey(item)) {
            return true;
        }
        return false;
    }

    /* Returns the item with smallest priority. If no items exist, throw a NoSuchElementException. */
    @Override
    public T getSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("no items exist");
        }
        return pq.get(0).getItem();
    }

    /* Removes and returns the item with smallest priority. If no items exist, throw a NoSuchElementException. */
    @Override
    public T removeSmallest() {
        if (size() == 0) {
            throw new NoSuchElementException("no items exist");
        }

        int lastIndex = this.size() - 1;
        T returnItem = getSmallest();
        swap(0, lastIndex);
        pq.remove(lastIndex);
        items.remove(returnItem);
        size -= 1;
        sink(0);
        return returnItem;
    }

    // sinks Node if it is smaller than its children
    private void sink(int k) {
        if (leftChild(k) >= size()) {
            return;
        }

        int cmpLCandRC = pq.get(leftChild(k)).compareTo(pq.get(rightChild(k)));
        if (cmpLCandRC < 0) { // left < right or right is null
            if (pq.get(leftChild(k)).compareTo(pq.get(k)) < 0) { // left < parent
                swap(k, leftChild(k));
                sink(leftChild(k));
            }
        }
        if (cmpLCandRC > 0) { // left > right
            if (pq.get(rightChild(k)).compareTo(pq.get(k)) < 0) { // right < parent
                swap(k, rightChild(k));
                sink(rightChild(k));
            }
        }
    }

    // returns the left child
    private int leftChild(int k) {
        return k * 2 + 1;
    }

    // returns the right child
    private int rightChild(int k) {
        return k * 2 + 2;
    }

    /* Returns the number of items in the PQ. */
    @Override
    public int size() {
        return this.size;
    }

    /* Changes the priority of the given item. Throws NoSuchElementException if the item
     * doesn't exist. */
    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }
        int indexKey = items.get(item);
        if (pq.get(indexKey).priority > priority) {
            pq.get(indexKey).setPriority(priority);
            swim(indexKey);
        }
        pq.get(indexKey).setPriority(priority);
        sink(indexKey);
    }

    private class PriorityNode implements Comparable<PriorityNode> {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        // returns a negative integer as first object is less than other other object is null.
        // returns 0 as they are equal.
        // returns a positive integer as first object is greater than other.
        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }
    }
}
