import edu.princeton.cs.algs4.Queue;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Random;

public class TestSortAlgs {

    @Test
    public void testQuickSort() {
        Queue<String> tas = new Queue<String>();
        tas.enqueue("Joe");
        tas.enqueue("Omar");
        tas.enqueue("Itai");
        Queue<String> test1 = QuickSort.quickSort(tas);
        Iterator iterator1 = test1.iterator();
        assertEquals("Itai", iterator1.next());
        assertEquals("Joe", iterator1.next());
        assertEquals("Omar", iterator1.next());

        tas = new Queue<>();
        Queue<String> test2 = QuickSort.quickSort(tas);
        assertEquals(true, test2.isEmpty());

        int n = 100000;
        Random random = new Random();
        Queue test3 = new Queue();
        for (int i = 0; i < n; i ++) {
            test3.enqueue(random.nextDouble());
        }
        Stopwatch sw = new Stopwatch();
        Queue sortedQ = QuickSort.quickSort(test3);
        System.out.print("Sorting " + n + " items will cost " + sw.elapsedTime());

    }

    @Test
    public void testMergeSort() {
        Queue<String> tas = new Queue<String>();
        tas.enqueue("Joe");
        tas.enqueue("Omar");
        tas.enqueue("Itai");
        Queue<String> test1 = MergeSort.mergeSort(tas);
        Iterator iterator1 = test1.iterator();
        assertEquals("Itai", iterator1.next());
        assertEquals("Joe", iterator1.next());
        assertEquals("Omar", iterator1.next());

        tas = new Queue<>();
        Queue<String> test2 = MergeSort.mergeSort(tas);
        assertEquals(true, test2.isEmpty());

        int n = 100000;
        Random random = new Random();
        Queue test3 = new Queue();
        for (int i = 0; i < n; i ++) {
            test3.enqueue(random.nextDouble());
        }
        Stopwatch sw = new Stopwatch();
        MergeSort.mergeSort(test3);
        System.out.print("Sorting " + n + " items will cost " + sw.elapsedTime());
    }

    /**
     * Returns whether a Queue is sorted or not.
     *
     * @param items  A Queue of items
     * @return       true/false - whether "items" is sorted
     */
    private <Item extends Comparable> boolean isSorted(Queue<Item> items) {
        if (items.size() <= 1) {
            return true;
        }
        Item curr = items.dequeue();
        Item prev = curr;
        while (!items.isEmpty()) {
            prev = curr;
            curr = items.dequeue();
            if (curr.compareTo(prev) < 0) {
                return false;
            }
        }
        return true;
    }
}
