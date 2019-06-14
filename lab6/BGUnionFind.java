public interface BGUnionFind {

    /* Returns the size of the set v1 belongs to. */
    int sizeOf(int v1);

    /* Returns the parent of v1. If v1 is the root of a tree, returns the
       negative size of the tree for which v1 is the root. */
    int parent(int v1);


    /* Return the final parent of v1. */
    int parentHelp(int v1);


    /* Returns true if nodes v1 and v2 are connected. */
    boolean connected(int v1, int v2);


    default void BGUnion(int v1, int v2) {
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
}
