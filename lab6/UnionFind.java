public class UnionFind implements BGUnionFind {

    // TODO - Add instance variables?
    int[] parents;
    int[] size;

    /* Creates a UnionFind data structure holding n vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int n) {
        // TODO
        parents = new int[n];
        size = new int[n];
        for (int i = 0; i < parents.length; i ++) {
            parents[i] = -1;
            size[i] = 1;
        }

    }

    /* Throws an exception if v1 is not a valid index. */
    private void validate(int vertex) {
        // TODO
        if (vertex > (parents.length - 1) && vertex < 0 ) {
            throw new RuntimeException("invalid index");
        }
    }

    /* Returns the size of the set v1 belongs to. */
    public int sizeOf(int v1) {
        // TODO
        // the final parent holds the size
        validate(v1);
        return -parent(parentHelp(v1));
    }

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    public int parent(int v1) {
        // TODO
        validate(v1);
        if (parents[v1] > 0) {
            return parents[v1];
        } else {
            return -size[v1];
        }
    }

    /* Return the final parent of v1. */
    public int parentHelp(int v1) {
        int parentV1 = v1;
        while (parent(parentV1) > 0) {
            parentV1 = parent(parentV1);
        }
        return parentV1;
    }

    /* Returns true if nodes v1 and v2 are connected. */
    public boolean connected(int v1, int v2) {
        // TODO
        if (find(v1) == find(v2)) {
            return true;
        }
        return false;
    }

    /* Connects two elements v1 and v2 together. v1 and v2 can be any valid 
       elements, and a union-by-size heuristic is used. If the sizes of the sets
       are equal, tie break by connecting v1's root to v2's root. Unioning a 
       vertex with itself or vertices that are already connected should not 
       change the sets but may alter the internal structure of the data. */
    public void union(int v1, int v2) {
        // TODO
        // check if they are connected
        if (connected(v1, v2)) {
            return;
        } else {
            // check their sizes
            if (sizeOf(v1) == sizeOf(v2)) {
                parents[find(v1)] = find(v2);
                size[find(v2)] += sizeOf(v1);
            }
            // size of v1 is bigger than size of v2
            if (sizeOf(v1) > sizeOf(v2)) {
                parents[find(v2)] = find(v1);
                size[find(v1)] += sizeOf(v2);
            }
            // size of v1 is smaller than size of v2
            if (sizeOf(v1) < sizeOf(v2)) {
                parents[find(v1)] = find(v2);
                size[find(v2)] += sizeOf(v1);
            }
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. */
    public int find(int vertex) {
        // TODO

        int nowV = vertex;
        int p = parentHelp(vertex);
        int lastparent;
        while (parent(nowV) > 0) {
            lastparent = parent(nowV);
            parents[nowV] = p;
            nowV = lastparent;
        }
        return nowV;
    }

    /**
     * for the BubbleGrid
     */

    // union the root of v1 to the root of v2
    public void BGUnion(int v1, int v2) {
        parents[v1] = parent(v2);
    }

     // change one item parent to 0.
    public void reParent(int vertex) {
        parents[vertex] = 0;
    }

}
