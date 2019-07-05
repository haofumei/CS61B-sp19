package bearmaps;
import java.util.List;
import java.util.Random;

public class NaivePointSet implements PointSet {

    List<Point> ps;
    public NaivePointSet(List<Point> points) {
        ps = List.copyOf(points);
    }

    /**
     * Returns the closest point to the inputted coordinates.
     * This should take θ(N) time where N is the number of points.
     */
    @Override
    public Point nearest(double x, double y) {
        Point v = new Point(x, y);
        double dist = Point.distance(ps.get(0), v);
        Point nearest = ps.get(0);
        for (Point p : ps) {
            if (Point.distance(p, v) < dist) {
                dist = Point.distance(p, v);
                nearest = p;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        Point p1 = new Point(2.0, 3.0); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(4.0, 2.0);
        Point p3 = new Point(1.0, 5.0);
        Point p4 = new Point(4.0, 5.0);
        Point p5 = new Point(3.0, 3.0);
        Point p6 = new Point(4.0, 4.0);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3, p4, p5, p6));
        Point goal = new Point(0.0, 7.0);
        Point ret = nn.nearest(goal.getX(), goal.getY()); // returns p2
        System.out.println(ret.getX()); // evaluates to 3.3
        System.out.println(ret.getY()); // evaluates to 4.4

        KDTree knn = new KDTree(List.of(p1, p2, p3, p4, p5, p6));
        Point testn = knn.naiveNearest(goal.getX(), goal.getY());
        System.out.println(testn.getX());
        System.out.println(testn.getY());

        Point test = knn.nearest(goal.getX(), goal.getY());
        System.out.println(test.getX());
        System.out.println(test.getY());

        Random random = new Random();
        for (int i = 0; i < 10; i ++) {
            System.out.print(random.nextDouble() + " ");
        }


    }
}
