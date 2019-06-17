import java.util.*;
import java.lang.Comparable;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root; // root of tree
    private Set<K> set = new HashSet<>(); // set of keys in order

    private class Node {
        private K key;
        private V value;
        private Node left, right; // left and right subtree
        private int size; // number of nodes in subtree

        public Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    // initialize an empty BSTMap
    public BSTMap() {
    }




    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to containsKey() is null");
        }
        return get(key) != null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(Node x, K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return get(x.left, key);
        } else if (cmp > 0) {
            return get(x.right, key);
        } else {
            return x.value;
        }
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        return x.size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("argument to put() is null");
        }
        if (value == null) {

        }
        root = put(root, key, value);
    }

    private Node put(Node x, K key, V value) {
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(x.left, key, value);
        } else if (cmp > 0) {
            x.right = put(x.right, key, value);
        } else {
            x.value = value;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }
    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        keySet(root);
        return set;
    }

    private void keySet(Node x) {
        K key = x.key;
        if (x.left != null) {
            keySet(x.left);
        }
        set.add(key);
        if (x.right != null) {
            keySet(x.right);
        }
    }

    /* Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (containsKey(key)) {
            V returnItem = get(key);
            root = remove(key, root);
            return returnItem;
        }
        return null;
    }

    private Node remove(K key, Node x) {
        if (x == null) {
            return null;
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = remove(key, x.left);
        } else if (cmp > 0) {
            x.right = remove(key, x.right);
        } else {
            if (x.right == null) {
                return x.left;
            }
            if (x.left == null) {
                return x.right;
            }
            Node t = x;
            x = min(t.right);
            x.right = removeMin(t.right);
            x.left = t.left;
        }
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    private Node min(Node x) {
        if (x.left == null) {
            return x;
        } else {
            return min(x.left);
        }
    }

    private Node removeMin(Node x) {
        if (x.left == null) {
            return x.right;
        }
        x.left = removeMin(x.left);
        x.size = size(x.left) + size(x.right) + 1;
        return x;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    /* prints out your BSTMap in order of increasing Key.*/
    public void printInOrder() {
        printOut(root);
    }

    private void printOut(Node x) {
        if (x == null) {
            System.out.println("empty BST");
        }
        V keep = x.value; // keep the value before check the left
        if (x.left != null) {
            printOut(x.left);
        }
        System.out.println(keep); // print keep after printOut the left
        if (x.right != null) {
            printOut(x.right);
        }
    }


    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {

        private int wizPos;
        private ArrayList<K> keys = new ArrayList<>();

        public BSTMapIterator() {
            wizPos = 0;
            BSTMapIterator(root);
        }
        private void BSTMapIterator(Node x) {
            K key = x.key;
            if (x.left != null) {
                BSTMapIterator(x.left);
            }
            keys.add(key);
            if (x.right != null) {
                BSTMapIterator(x.right);
            }
        }

        public boolean hasNext() {
            return wizPos < size();
        }

        public K next() {
            K returnKey = keys.remove(0);
            wizPos += 1;
            return returnKey;
        }
    }
}
