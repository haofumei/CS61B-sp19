public class LinkedListDeque<T> implements Deque<T> {

    public class IntNode {
        public IntNode prev;
        public T item;
        public IntNode next;

        public IntNode(IntNode p, T i, IntNode n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    private IntNode sentinel = new IntNode(null, null, null);;
    private int size = 0;



    /** Adds an item of type T to the front of the deque.*/
    /* must not involve any looping or recursion.
     * A single such operation must take constant time
     */
    @Override
    public void addFirst(T item) {
        sentinel.next = new IntNode(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size = size + 1;
    }

    /** Adds an item of type T to the back of the deque.*/
    /* must not involve any looping or recursion.
     * A single such operation must take constant time
     */
    @Override
    public void addLast(T item) {
        sentinel.prev = new IntNode(sentinel.prev, item, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size = size + 1;
    }

    /** Returns true if deque is empty, false otherwise.*/
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque.*/
    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.
     * Once all the items have been printed, print out a new line.
     */
    @Override
    public void printDeque() {
        IntNode p = sentinel;
        for (int i = 0; i < size; i++) {
            p = p.next;
            System.out.print(p.item + " ");
        }
        System.out.println(" ");
    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null.*/
    /* must not involve any looping or recursion.
     * A single such operation must take constant time
     */
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        } else {
            T remove = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
            return remove;
        }
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null.*/
    /* must not involve any looping or recursion.
     * A single such operation must take constant time
     */
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else {
            T remove = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size--;
            return remove;
        }
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     ** If no such item exists, returns null. Must not alter the deque!
     */
    /* must use iteration, not recursion.*/
    @Override
    public T get(int index) {
        if (size <= index) {
            return null;
        } else {
            IntNode g = sentinel.next;
            for (int i = 0; i < index; i++) {
                g = g.next;
            }
            return g.item;
        }
    }

    /* Same as get, but uses recursion.*/
    @Override
    public T getRecursive(int index) {
        if (size <= index) {
            return null;
        } else if (index == 0) {
            return sentinel.next.item;
        } else {
            LinkedListDeque<T> help = new LinkedListDeque<>(this);
            help.removeFirst();
            return help.getRecursive(index - 1);
        }
    }
    /*
    private  T getRecursive(IntNode help, int index) {
        if (index == 0) {
            return sentinel.next.item;
        } else {
            return this.getRecursive(help.next, index - 1);
        }
    }
     */


    /** Creates an empty linked list deque.*/
    public LinkedListDeque() {
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /** Creates a deep copy of other.*/
    public LinkedListDeque(LinkedListDeque other){
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        IntNode temp = other.sentinel;
        for (int i = 0; i < other.size(); i++) {
            addLast(temp.next.item);
            temp = temp.next;
        }
    }
}
