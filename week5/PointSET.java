import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;
import java.util.TreeSet;
import java.util.ArrayList;

public class PointSET {
    private int pNum;
    private TreeSet<Point2D> points;
    // construct an empty set of points
    public PointSET() {
        pNum = 0;
        points = new TreeSet<Point2D>(); // need a comparator?
    }
    // is the set empty?
    public boolean isEmpty() {
        return pNum == 0;
    }
    // number of points in the set
    public int size() {
        return pNum;
    }
    // add the point to the set (if not already in)
    public void insert(Point2D p) {
        if (!points.contains(p)) {
            points.add(p);
            pNum++; 
        }
    }
    // does the set contain p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }
    // draw all points to standard draw
    public void draw() {
        for (Point2D p :  points) {
            p.draw();
        }
    }
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException();
        }
        ArrayList<Point2D> inPoints = new ArrayList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                inPoints.add(p);
            }
        }
        return inPoints;
    }
    // a nearest neighbor in the set to point p; null if set if empty.
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        double minDis = 2; // 2 is sure to be greater than any distance.
        Point2D minP = new Point2D(0, 0);
        for (Point2D pp : points) {
            double tempDis = pp.distanceTo(p);
            if (tempDis < minDis) {
                minDis = tempDis;
                minP = pp;
            } 
        }
        return minP;
    }
    // unit test
    public static void main(String[] args) {
         String fname = args[0];
        In in = new In(fname);

        PointSET pSet = new PointSET();
        
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            pSet.insert(p);
        }
        
        StdDraw.setPenRadius(0.01);
        pSet.draw();
    }
}
