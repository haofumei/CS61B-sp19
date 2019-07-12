import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * BnBSolver for the Bears and Beds problem. Each Bear can only be compared to Bed objects and each Bed
 * can only be compared to Bear objects. There is a one-to-one mapping between Bears and Beds, i.e.
 * each Bear has a unique size and has exactly one corresponding Bed with the same size.
 * Given a list of Bears and a list of Beds, create lists of the same Bears and Beds where the ith Bear is the same
 * size as the ith Bed.
 */
public class BnBSolver {

    Node root;
    List<Pair<Bear, Bed>> pairs = new ArrayList<>();

    public BnBSolver(List<Bear> bears, List<Bed> beds) {
        for (Bear bear : bears) {
            root = buildBSTAndPair(root, beds, bear);
        }
    }

    /**
     * build BST and pair bear and bed.
     */
    private Node buildBSTAndPair(Node x, List beds, Bear pivotBear) {
        if (x == null) {
            List<Bed> small = new LinkedList<>();
            List<Bed> large = new LinkedList<>();
            Pair<Bear, Bed> pair = partition(beds, pivotBear, small, large);
            return new Node(pair, small, large);
        }
        int cmp = pivotBear.compareTo(x.pair.second());
        if (cmp < 0) {
            x.left = buildBSTAndPair(x.left, x.small, pivotBear);
        }
        if (cmp > 0) {
            x.right = buildBSTAndPair(x.right, x.large, pivotBear);
        }
        return x;
    }

    /**
     * partition the beds according to a pivotBear.
     * If it finds one bed is equal to pivotBear, then pair them.
     * And return the pair.
     */
    private Pair<Bear, Bed> partition(List<Bed> beds, Bear pivotBear, List<Bed> small, List<Bed> large) {
        for (Bed bed : beds) {
            int cmp = bed.compareTo(pivotBear);
            if (cmp < 0) {
                small.add(bed);
            }
            if (cmp > 0) {
                large.add(bed);
            }
            if (cmp == 0) {
                pairs.add(new Pair(pivotBear, bed));
            }
        }
        return pairs.get(pairs.size() - 1);
    }
    /**
     * Returns List of Bears such that the ith Bear is the same size as the ith Bed of solvedBeds().
     */
    public List<Bear> solvedBears() {
        LinkedList<Bear> bears = new LinkedList<>();
        for (Pair<Bear, Bed> pair : pairs) {
            bears.addLast(pair.first());
        }
        return bears;
    }

    /**
     * Returns List of Beds such that the ith Bear is the same size as the ith Bear of solvedBears().
     */
    public List<Bed> solvedBeds() {
        LinkedList<Bed> beds = new LinkedList<>();
        for (Pair<Bear, Bed> pair : pairs) {
            beds.addLast(pair.second());
        }
        return beds;
    }

    private class Node {

        Pair<Bear, Bed> pair;
        List<Bed> small;
        List<Bed> large;
        Node left, right;

        public Node(Pair<Bear, Bed> pair, List<Bed> small, List<Bed> large) {
            this.pair = pair;
            this.small = small;
            this.large = large;
        }
    }

}
