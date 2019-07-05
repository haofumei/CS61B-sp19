package bearmaps;
import java.util.List;

public class KDTree {

    Node root; // root of tree

    private class Node{

        Point p;
        Node left, right, up, down;

        public Node(Point p) {
            this.p = p;
        }

        public double getX() {
            return this.p.getX();
        }
        public double getY() {
            return this.p.getY();
        }

    }

    public KDTree(List<Point> points) {
        if (points == null) {
            return;
        }
        for (Point p : points) {
            root = add(p, root, 1);
        }
    }

    private Node add(Point p, Node n, int xOry) { // compares x if xOry is 1, compares y if xOry is -1.
        if (n == null) {
            return new Node(p);
        }
        if (n.p.equals(p)) {
            return n;
        }
        if (xOry == 1) {
            int cmpX = Double.compare(p.getX(), n.getX());
            if (cmpX < 0) {
                n.left = add(p, n.left, 0 - xOry);
            } else {
                n.right = add(p, n.right, 0 - xOry); // go off to the right (or up) child of each node if they are equal
            }
        }
        if (xOry == -1) {
            int cmpY = Double.compare(p.getY(), n.getY());
            if (cmpY < 0) {
                n.down = add(p, n.down, 0 - xOry);
            } else {
                n.up = add(p, n.up, 0 - xOry);
            }
        }
        return n;
    }

    /**
     * Returns the closest point to the inputted coordinates.
     * This should take O(logN) time on average, where N is the number of points.
     */
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        side good = new side();
        return nearest(root, goal, root, 1, good).p;
    }

    private Node nearest(Node n, Point goal, Node best, int xOry, side good) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, goal) < Point.distance(best.p, goal)) {
            best = n;
        }

        Node goodSide, badSide;
        side bad = good.copy();
        if (xOry == 1) {
            if (goal.getX() < n.getX()) {
                goodSide = n.left;
                badSide = n.right;
                good.xMax = n.getX();
                bad.xMin = n.getX();
            } else {
                goodSide = n.right;
                badSide = n.left;
                good.xMin = n.getX();
                bad.xMax = n.getX();
            }
            best = nearest(goodSide, goal, best, 0 - xOry, good);

            if (Point.distance(bad.bestPoint(n.getX(), goal.getY()), goal) < Point.distance(best.p, goal)) {
                best = nearest(badSide, goal, best, 0 - xOry, bad);
            }
        }
        if (xOry == -1) {
            if (goal.getY() < n.getY()) {
                goodSide = n.down;
                badSide = n.up;
                good.yMax = n.getY();
                bad.yMin = n.getY();
            } else {
                goodSide = n.up;
                badSide = n.down;
                good.yMin = n.getY();
                bad.yMax = n.getY();
            }
            best = nearest(goodSide, goal, best, 0 - xOry, good);

            if (Point.distance(bad.bestPoint(goal.getX(), n.getY()), goal) < Point.distance(best.p, goal)) {
                best = nearest(badSide, goal, best, 0 - xOry, bad);
            }
        }
        return best;
    }

    public Point naiveNearest(double x, double y) {
        Point goal = new Point(x, y);
        return naiveNearest(root, goal, root, 1).p;
    }

    private Node naiveNearest(Node n, Point goal, Node best, int xOry) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, goal) < Point.distance(best.p, goal)) {
            best = n;
        }
        if (xOry == 1) {
            best = naiveNearest(n.left, goal, best, 0 - xOry);
            best = naiveNearest(n.right, goal, best, 0 - xOry);
        }
        if (xOry == -1)  {
            best = naiveNearest(n.down, goal, best, 0 - xOry);
            best = naiveNearest(n.up, goal, best, 0 - xOry);
        }
        return best;
    }

    // define the area of X and Y
    private class side{

        double xMax, xMin, yMax, yMin;
        static final double max = Double.MAX_VALUE;

        public side(double xMax, double xMin, double yMax, double yMin) {
            this.xMax = xMax;
            this.xMin = xMin;
            this.yMax = yMax;
            this.yMin = yMin;
        }

        public side() {
            this(max, -max, max, -max);
        }

        public side copy() {
            return new side(this.xMax, this.xMin, this.yMax, this.yMin);
        }

        // return the bestpoint in this side
        public Point bestPoint(double x, double y) {
            if (x > xMax) {
                x = xMax;
            }
            if (x < xMin) {
                x = xMin;
            }
            if (y > yMax) {
                y = yMax;
            }
            if (y < yMin) {
                y = yMin;
            }
            return new Point(x, y);
        }
    }
}
