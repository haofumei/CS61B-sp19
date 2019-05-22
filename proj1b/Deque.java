public interface Deque<T> {
    public void addFirst(T item);

    /**
     * Adds an item of type T to the back of the deque.
     */
    /* must not involve any looping or recursion.
     * A single such operation must take constant time
     */
    public void addLast(T item);
    /**
     * Returns true if deque is empty, false otherwise.
     */
    default public boolean isEmpty() {
        return size() == 0;
    }
    /**
     * Returns the number of items in the deque.
     */
    public int size();
    /**
     * Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    public void printDeque();
    /**
     * Removes and returns the item at the front of the deque. If no such item exists, returns null.
     */
    /* must not involve any looping or recursion.
     * A single such operation must take constant time
     */
    public T removeFirst();
    /**
     * Removes and returns the item at the back of the deque. If no such item exists, returns null.
     */
    /* must not involve any looping or recursion.
     * A single such operation must take constant time
     */
    public T removeLast();
    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * * If no such item exists, returns null. Must not alter the deque!
     */
    /* must use iteration, not recursion.*/
    public T get(int index);
    /* Same as get, but uses recursion.*/
    public T getRecursive(int index);
    /*
    private  T getRecursive(IntNode help, int index) {
        if (index == 0) {
            return sentinel.next.item;
        } else {
            return this.getRecursive(help.next, index - 1);
        }
    }
    */
}
