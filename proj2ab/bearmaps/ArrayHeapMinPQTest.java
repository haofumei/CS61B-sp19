package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayHeapMinPQTest {

    @Test
    public void basicTest() {
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();
        for (int i = 10; i > 5; i --) {
            test.add("hi" + i, i);
        }
        assertEquals("hi6", test.getSmallest());
        test.add("hi5", 5);
        assertEquals("hi5", test.getSmallest());
        assertEquals(true, test.contains("hi5"));
        assertEquals(6, test.size());
    }

    @Test
    public void removeSmallestTest() {
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();
        for (int i = 10; i > 0; i --) {
            test.add("hi" + i, i);
        }
        assertEquals("hi1", test.removeSmallest());
        assertEquals("hi2", test.getSmallest());
        assertEquals(false, test.contains("hi1"));
    }

    @Test
    public void changePriorityTest() {
        ArrayHeapMinPQ test = new ArrayHeapMinPQ();
        for (int i = 10; i > 5; i --) {
            test.add("hi" + i, i);
        }
        test.changePriority("hi10", 2.0);
        assertEquals("hi10", test.getSmallest());
    }

    // time test
    public static void main(String[] args) {
        int n = 1000000;
        Stopwatch sw1 = new Stopwatch();
        ExtrinsicMinPQ test1 = new ArrayHeapMinPQ();
        for (int i = 0; i < n; i += 1) {
            test1.add("hi" + i, StdRandom.uniform(n));
        }
        for (int i = 0; i < 1000; i += 1) {
            test1.changePriority("hi" + StdRandom.uniform(n), StdRandom.uniform(n));
        }
        System.out.println("Total time elapsed: " + sw1.elapsedTime() +  " seconds.");

        Stopwatch sw2 = new Stopwatch();
        ExtrinsicMinPQ test2 = new NaiveMinPQ();
        for (int i = 0; i < n; i += 1) {
            test2.add("hi" + i, StdRandom.uniform(n));
        }
        for (int i = 0; i < 1000; i += 1) {
            test2.changePriority("hi" + StdRandom.uniform(n), StdRandom.uniform(n));
        }
        System.out.println("Total time elapsed: " + sw2.elapsedTime() +  " seconds.");
    }
}
