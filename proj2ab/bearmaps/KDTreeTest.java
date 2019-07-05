package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.DoubleStream;

public class KDTreeTest {
    public List<Point> randomPoints(int pointCount) {
        Random random = new Random();
        List<Point> p = new LinkedList<>();
        for (int i = 0; i < pointCount; i ++) {
            p.add(new Point(random.nextDouble() * 100, random.nextDouble() * 100));
        }
        return p;
    }

    @Test
    public void testKDComparedWithNV() {
        List<Point> p = randomPoints(100000);
        KDTree kd = new KDTree(p);
        NaivePointSet nv = new NaivePointSet(p);
        List<Point> queries = randomPoints(2000);

        for (Point q : queries) {
            Point actualKD = kd.nearest(q.getX(), q.getY());
            Point actualNV = nv.nearest(q.getX(), q.getY());
            assertEquals(actualKD, actualNV);
            //System.out.println("KD: " + actualKD + " NV: " + actualNV);
        }
    }

    private void timeWithPointsAndQueriesKD(int pointCount, int  queryCount) {
        List<Point> p = randomPoints(pointCount);
        KDTree kd = new KDTree(p);
        List<Point> queries = randomPoints(queryCount);

        Stopwatch sw = new Stopwatch();
        for (Point q : queries) {
            Point actual = kd.nearest(q.getX(), q.getY());
        }
        System.out.println("Time elapsed for " + queryCount + " queries on " + pointCount +
                                                " points: " + sw.elapsedTime());
    }

    private void timeWithPointsAndQueriesKDNaivenearest(int pointCount, int  queryCount) {
        List<Point> p = randomPoints(pointCount);
        KDTree kd = new KDTree(p);
        List<Point> queries = randomPoints(queryCount);

        Stopwatch sw = new Stopwatch();
        for (Point q : queries) {
            Point actual = kd.naiveNearest(q.getX(), q.getY());
        }
        System.out.println("Time elapsed for " + queryCount + " queries on " + pointCount +
                " points: " + sw.elapsedTime());
    }

    private void timeWithPointsAndQueriesNV(int pointCount, int  queryCount) {
        List<Point> p = randomPoints(pointCount);
        NaivePointSet nv = new NaivePointSet(p);
        List<Point> queries = randomPoints(queryCount);

        Stopwatch sw = new Stopwatch();
        for (Point q : queries) {
            Point actual = nv.nearest(q.getX(), q.getY());
        }
        System.out.println("Time elapsed for " + queryCount + " queries on " + pointCount +
                " points: " + sw.elapsedTime());
    }

    @Test
    public void testWith10000PointsAnd2000Queries() {
        timeWithPointsAndQueriesKD(10000, 2000);
        timeWithPointsAndQueriesKDNaivenearest(10000, 2000);
        timeWithPointsAndQueriesNV(10000, 2000);
    }

    @Test
    public void testWithVariousNumbersOfPoints() {
        List<Integer> pointCounts = List.of(1000, 10000, 100000, 1000000);
        for (int N : pointCounts) {
            timeWithPointsAndQueriesKD(N, 10000);
        }
    }

    @Test
    public void testWithKDvsNV() {
        timeWithPointsAndQueriesKD(100000, 10000);
        timeWithPointsAndQueriesNV(100000, 10000);
    }


}
