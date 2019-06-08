package es.datastructur.synthesizer;
import org.hamcrest.internal.ArrayIterator;

import java.util.Iterator;

//TODO: Make sure to that this class and all of its methods are public
//TODO: Make sure to add the override tag for all overridden methods
//TODO: Make sure to make this class implement BoundedQueue<T>

public class ArrayRingBuffer<T> implements BoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;
    /* Index for the next enqueue. */
    private int last;
    /* Variable for the fillCount. */
    private int fillCount;
    /* Array for storing the buffer data. */
    private T[] rb;
    /* size of Buffer. */
    private int size;


    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        rb = (T[]) new Object[capacity];
        size = capacity;
        fillCount = first = last = 0;

    }

    /**
     * return the capacity.
     */
    @Override
    public int capacity() {
        return size;
    }

    /**
     * return the fillCount.
     */
    @Override
    public int fillCount() {
        return fillCount;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow").
     */
    @Override
    public void enqueue(T x) {
        // TODO: Enqueue the item. Don't forget to increase fillCount and update
        //       last.
        if (isFull()) {
            throw new RuntimeException("Ring Buffer overflow");
        } else {
            rb[last] = x;
            fillCount += 1;
            last = (last + 1) % this.size;
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    public T dequeue() {
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and
        //       update first.
        if (isEmpty()) {
            throw new RuntimeException("Ring Buffer underflow");
        } else {
            T returnItem = rb[first];
            rb[first] = null;
            fillCount -= 1;
            first = (first + 1) % this.size;
            return returnItem;
        }
    }

    /**
     * Return oldest item, but don't remove it. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow").
     */
    public T peek() {
        // TODO: Return the first item. None of your instance variables should
        //       change.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        } else {
            return rb[first];
        }
    }

    // TODO: When you get to part 4, implement the needed code to support
    //       iteration and equals.

    /**
     *iterator
     */
    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    private class BufferIterator implements Iterator<T> {
        int index;
        public BufferIterator() {
            index = first;
        }

        public boolean hasNext() {
            return index != last;
        }

        public T next() {
            T returnItem = rb[index];
            index = (index + 1) % size;
            return returnItem;
        }
    }

    /**
     * override equals(Object o)
     */
    @Override
    public boolean equals(Object A) {
        if (A == this) {
            return true;
        }
        //null
        if (A == null) {
            return false;
        }
        //check the type
        if (A.getClass() != this.getClass()) {
            return false;
        }
        //make an ArrayRingBuffer to check after check the type
        ArrayRingBuffer<T> forCheck = (ArrayRingBuffer<T>) A;
        // check the size
        if (forCheck.fillCount != this.fillCount) {
            return false;
        }
        //check every value
        Iterator<T> thisIterator = this.iterator();
        Iterator<T> forIterator = forCheck.iterator();
        while (thisIterator.hasNext() && forIterator.hasNext()) {
            if (thisIterator.next() != forIterator.next()) {
                return false;
            }
        }
        return true;
    }
}
    // TODO: Remove all comments that say TODO when you're done.
