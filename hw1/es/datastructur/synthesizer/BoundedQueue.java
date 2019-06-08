package es.datastructur.synthesizer;

import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable {
    int capacity();     // return size of the buffer

    int fillCount();    // return number of items currently in the buffer

    void enqueue(T x);  // add item x to the end

    T dequeue();        // delete and return item from the front

    T peek();           // return (but do not delete) item from the front

    // return true if the BoundedQueue is empty
    default boolean isEmpty() {
        return fillCount() == 0;
    }

    //return true if the BoundedQueue is full
    default boolean isFull() {
        return fillCount() == capacity();
    }

    //return an iterator
    Iterator<T> iterator();
    //equals
    boolean equals(Object A);
}
