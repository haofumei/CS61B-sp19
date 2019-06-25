import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V>{

    private static final int initialSize = 16;    // must be static, or this() can not be compiled
    private double loadFactor = 0.75;             // n / m
    private int n;                                // number of key-value pairs
    private int m;                                // hash table size
    private Node<K, V>[] buckets;                 // Array of buckets

    private class Node<K, V> {

        public K key;
        public V value;
        public Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    public MyHashMap() {
        this(initialSize);
    }

    /**
     * Initially have a number of buckets equal to initialSize.
     */
    public MyHashMap(int initialSize) {
        this.m = initialSize;
        this.n = 0;
        buckets = new Node[m];
        for (int i = 0; i < m; i ++) {
            buckets[i] = null;
        }
    }

    /**
     *  Increase the size of the MyHashMap
     *  when the load factor exceeds the set loadFactor.
     *  If initialSize and loadFactor aren't given,
     *  you should set defaults initialSize = 16 and loadFactor = 0.75 (as Java’s built-in HashMap does).
     */
    public MyHashMap(int initialSize, double loadFactor) {
        this(initialSize);
        this.loadFactor = loadFactor;
    }

    // hash value between 0 to m-1
    private int hash(K key) {
        return (key.hashCode() & 0x7fffffff) % m;
    }

    /**
     * resize the buckets with double size.
     */
    private void resize(int m) {
        MyHashMap temp = new MyHashMap(m);
        for (int i = 0; i < m / 2; i ++) {
            Node tempbucket = buckets[i];
            while (tempbucket != null) {
                temp.put(tempbucket.key, tempbucket.value);
                tempbucket = tempbucket.next;
            }
        }
        this.buckets = temp.buckets;
        this.m = m;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        for (int i = 0; i < m; i ++) {
            buckets[i] = null;
        }
        n = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        validate(key);
        return buckets[hash(key)] != null;

    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (!containsKey(key)) {
            return null;
        }
        return get(buckets[hash(key)], key);
    }

    private V get(Node x, K key) {
        if (x.key.equals(key)) {
            return (V)x.value;
        } else {
            return (V)get(x.next, key);
        }
    }

    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return this.n;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        validate(key);
        if (value == null) {
            remove(key);
        }
        if (buckets[hash(key)] == null) {
            buckets[hash(key)] = new Node(key, value);
            n += 1;
            if (n / m > loadFactor) {
                resize(m * 2);
            }
        } else {
            put(buckets[hash(key)], key, value);
        }
    }

    private void put(Node x, K key, V value) {
        if (x.key.equals(key)) {
            x.value = value;
        } else if (x.next == null) {
            x.next = new Node(key, value);
            n += 1;
            if (n / m > loadFactor) {
                resize(m * 2);
            }
        } else {
            put(x.next, key, value);
        }
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet();
        for (int i = 0; i < m; i ++) {
            Node tempbucket = buckets[i];
            while (tempbucket != null) {
                keys.add((K)tempbucket.key);
                tempbucket = tempbucket.next;
            }
        }
        return keys;
    }

    @Override
    public Iterator<K> iterator() {
        return new myHashMapIterator();
    }

    public class myHashMapIterator<K> implements Iterator<K> {

        int keyNum;
        Set keys;
        Iterator nextSetItem;

        public myHashMapIterator() {
            keyNum = n;
            keys = keySet();
            nextSetItem = keys.iterator();
        }

        public boolean hasNext() {
            return keyNum > 0;
        }

        public K next() {
            K nextItem = (K)nextSetItem.next();
            keyNum -= 1;
            return nextItem;
        }
    }

    /**
     * Removes the mapping for the specified key from this map if present.
     */
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        Node indexBucket = buckets[hash(key)];
        if (indexBucket.key.equals(key)) {
            V removeItem = (V)indexBucket.value;
            buckets[hash(key)] = indexBucket.next;
            return removeItem;
        }
        return (V)remove(buckets[hash(key)], indexBucket.next, key);
    }

    private V remove(Node last, Node now, K key) {
        if (now.key.equals(key)) {
            V removeItem = (V)now.value;
            last.next = now.next;
            return removeItem;
        }
        return (V)remove(now, now.next, key);
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value.
     */
    @Override
    public V remove(K key, V value) {
        if (!get(key).equals(value)) {
            return null;
        }
        return remove(key);
    }


    public void validate(K key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
    }

    public static void main(String[] args) { // test iterator
        MyHashMap<String, Integer> b = new MyHashMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
        }
        int count = 0;
        for (String s : b) {
            count += 1;
            System.out.println(s);
        }
        System.out.println(count);
    }
}
