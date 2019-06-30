import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class MyTrieSet implements TrieSet61B {

    private static final int R = 128; // ASCII
    private Node root = new Node(false, R);

    private class Node {

        private boolean isKey;
        private Node[] next;

        private Node(boolean isKey, int R) {
            this.isKey = isKey;
            this.next = new Node[R];
        }
    }


    /**
     * Clears all items out of Trie
     */
    @Override
    public void clear() {
        this.root = new Node(false, R);
    }

    /**
     * Returns true if the Trie contains KEY, false otherwise
     */
    @Override
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        Node curr = root;
        for (int i = 0; i < key.length(); i ++) {
            char c = key.charAt(i);
            if (curr.next[c] == null) {
                return false;
            }
            curr = curr.next[c];
        }
        return (curr.isKey);
    }

    /**
     * Inserts string KEY into Trie
     */
    @Override
    public void add(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key can not be null");
        }
        add(key, root, 0);
    }

    private void add(String key, Node x, int i) {
        if (x.next[key.charAt(i)] == null) {
            if (i == key.length() - 1) {
                x.next[key.charAt(i)] = new Node(true, R);
            } else {
                x.next[key.charAt(i)] = new Node(false, R);
                add(key, x.next[key.charAt(i)], i + 1);
            }
        }
        if (i == key.length() - 1) {
            x.next[key.charAt(i)].isKey = true;
        } else {
            add(key, x.next[key.charAt(i)], i + 1);
        }
    }

    /**
     * Returns a list of all words that start with PREFIX
     */
    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> kpf = new LinkedList<>();
        Node curr = root;
        for (int i = 0; i < prefix.length(); i ++) {
            if (curr.next[prefix.charAt(i)] != null) {
                curr = curr.next[prefix.charAt(i)];
            }
        }
        colHelp(prefix, kpf, curr);
        return kpf;
    }


    /**
     * Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    private void colHelp(String s, List<String> x, Node n) {
        if (n.isKey) {
            x.add(s);
        }
        for (int i = 0; i < R; i ++) {
            if (n.next[i] != null) {
                colHelp(s + (char)i, x, n.next[i]);
            }
        }
    }
}
