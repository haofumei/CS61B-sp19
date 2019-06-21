import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hug.
 */
public class ExperimentHelper {

    /** Returns the internal path length for an optimum binary search tree of
     *  size N. Examples:
     *  N = 1, OIPL: 0
     *  N = 2, OIPL: 1
     *  N = 3, OIPL: 2
     *  N = 4, OIPL: 4
     *  N = 5, OIPL: 6
     *  N = 6, OIPL: 8
     *  N = 7, OIPL: 10
     *  N = 8, OIPL: 13
     */
    public static int optimalIPL(int N) {
        validate(N);
        int i = 0;
        int OIPL = 0;
        int size = N;
        while (Math.pow(2, i) <= size) {
            OIPL += i * (int)Math.pow(2, i);
            size -= Math.pow(2, i);
            i ++;
        }
        OIPL += size * i;
        return OIPL;
    }

    /** Returns the average depth for nodes in an optimal BST of
     *  size N.
     *  Examples:
     *  N = 1, OAD: 0
     *  N = 5, OAD: 1.2
     *  N = 8, OAD: 1.625
     * @return
     */
    public static double optimalAverageDepth(int N) {
        return (double)optimalIPL(N) / N;
    }

    public static void validate(int N) {
        if (N < 0) {
            throw new IllegalArgumentException("N should be bigger than 0");
        }
    }

    public static void scsDeleteAndInsert(BST bst, int delete, int insert) {
        bst.deleteTakingSuccessor(delete);
        bst.add(insert);
    }

    public static void rdDeleteAndInsert(BST bst, int delete, int insert) {
        bst.deleteTakingRandom(delete);
        bst.add(insert);
    }

    public static void main(String[] args) {
        int N = 1;
        N = 5000;
        System.out.print(optimalAverageDepth(N) + "");
        System.out.println(optimalIPL(N));

        BST test = new BST();
        test.add(7);
        test.add(5);
        test.add(9);
        test.add(1);
        test.add(3);
        test.add(4);
        test.add(8);
        System.out.println(test.averageDepth() * 7);
    }
}
