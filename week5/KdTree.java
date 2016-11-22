/*********************************************************************
* use 2d-tree to represent a set of points enables a more efficient 
* implementation of `range()` and `nearest()`. 
* method 'range()' and 'contain()' is efficient;
* TODO: improve 'insert()' and 'nearest()' running time, reduce number of
* calls to 'RectHV()'
**********************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class KdTree {
    private static final boolean UPDOWN = true;
    private static final boolean LEFTRIGHT = false;

    private Node root;
    private int nodeNum;
    private RectHV rootGrid;

    // construct an empty set of points
    public KdTree() {
        nodeNum = 0;
        rootGrid = new RectHV(0, 0, 1, 1);
    }

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb; // left/bottom subtree
        private Node rt; // right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }
   
    // is the set empty?
    public boolean isEmpty() {
        return size() == 0;
    }
    // number of points in the set
    public int size() {
        return nodeNum;
    }
    // add the point to the set (if not already in)
    public void insert(Point2D p) {
        if (!contains(p) && rootGrid.contains(p)) {
            root = insert(root, p, LEFTRIGHT, rootGrid);
        }
    }
    private Node insert(Node x, Point2D p, boolean orient, RectHV rect) {
        RectHV tempRect = rect;
        if (p == null) {
            throw new IllegalArgumentException("argument to insert() is null");
        }
        if (x == null) {
            nodeNum++;
            return new Node(p, tempRect);
        }

        int cmp = compare(p, x.p, orient);

        if (orient == UPDOWN) {
            if (cmp < 0) {
                tempRect = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), x.p.y());
                x.lb = insert(x.lb, p, !orient, tempRect);
            }
            else if (cmp > 0) {
                tempRect = new RectHV(rect.xmin(), x.p.y(), rect.xmax(), rect.ymax());
                x.rt = insert(x.rt, p, !orient, tempRect);
            }
        }
        else {
            if (cmp < 0) {
                tempRect = new RectHV(rect.xmin(), rect.ymin(), x.p.x(), rect.ymax());
                x.lb = insert(x.lb, p, !orient, tempRect);
            }
            else if (cmp > 0) {
                tempRect = new RectHV(x.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                x.rt = insert(x.rt, p, !orient, tempRect);
            }
        }
        return x;
    }

    // does the set contain p?
    public boolean contains(Point2D p) {
        return contains(root, p, LEFTRIGHT);
    }
    private boolean contains(Node x, Point2D p, boolean orient) {
        if (x == null) return false;
        
        int cmp = compare(p, x.p, orient);

        if (orient == UPDOWN) {
            if (cmp < 0) return contains(x.lb, p, !orient);
            if (cmp > 0) return contains(x.rt, p, !orient);
            return true;
        }
        else {
            if (cmp < 0) return contains(x.lb, p, !orient);
            if (cmp > 0) return contains(x.rt, p, !orient);
            return true;
        }
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }
    private void draw(Node x) {
        if (x != null) {
            x.p.draw();
            draw(x.lb);
            draw(x.rt);
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        ArrayList<Point2D> inPoints = new ArrayList<Point2D>();
        range(root, rect, inPoints);
        return inPoints; 
    }

    private void range(Node x, RectHV rect, ArrayList<Point2D> inPoints) {
        if (x == null) return;
        if (rect.intersects(x.rect)) {
            if (rect.contains(x.p)) inPoints.add(x.p);
            if (x.lb != null && rect.intersects(x.lb.rect)) 
                range(x.lb, rect, inPoints);
            if (x.rt != null && rect.intersects(x.rt.rect)) 
                range(x.rt, rect, inPoints);
        }
    }

     /*************************************************************** 
     * a nearest neighbor in the set to point p; null if set if empty.
     * use 'distanceSquaredTo' instead of 'distanceTo'
     *****************************************************************/
    
    public Point2D nearest(Point2D p) {
        if (root == null) return null;
        double minDis = root.p.distanceSquaredTo(p);
        return nearest(root, p, minDis, root.p);
    }

    private Point2D nearest(Node x, Point2D p, double minDis, Point2D minP) {
        
        if (x == null) return null;

        Point2D closestPoint = minP;
        double closestDis = minDis;
        
        double currentDis = p.distanceSquaredTo(x.p);
        
        if (currentDis < closestDis) {
            closestDis = currentDis;
            closestPoint = x.p;
            // StdOut.printf("current point: %s\tcurrent distance: %.3f\n",x.p,closestDis);
        }

        // if (x.lb != null && x.lb.rect.contains(p)) {
        if (x.lb != null) {
            closestPoint = nearest(x.lb, p, closestDis, closestPoint);
            closestDis = closestPoint.distanceSquaredTo(p);
            if (x.rt != null && closestDis > x.rt.rect.distanceSquaredTo(p))
                closestPoint = nearest(x.rt, p, closestDis, closestPoint);
        }
        else if (x.rt != null) {
            closestPoint = nearest(x.rt, p, closestDis, closestPoint);
            closestDis = closestPoint.distanceSquaredTo(p);
            if (x.lb != null && closestDis > x.lb.rect.distanceSquaredTo(p))
                closestPoint = nearest(x.lb, p, closestDis, closestPoint);
        }
        return closestPoint;
    }

    /**********************************************************
    * compare points according to the orient. if p1 and p2 have 
    * the same y(or x) coordinate, return 1.
    * (then p1 assign to right/top subtree of p2).
    ***********************************************************/
    private int compare(Point2D p1, Point2D p2, boolean orient) {
        if (p1.equals(p2)) return 0;
        if (orient == LEFTRIGHT) {
            if (p1.x() > p2.x()) return 1;
            else if (p1.x() < p2.x()) return -1;
            else return 1;

        } else { // orient == UPDOWN
            if (p1.y() > p2.y()) return 1;
            else if (p1.y() < p2.y()) return -1;
            else return 1; 
        }
    }

    // unit test
    public static void main(String[] args) {
        String fname = args[0];
        In in = new In(fname);

        KdTree kdtree = new KdTree();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        
        StdDraw.setPenRadius(0.005);
        kdtree.draw();
        StdOut.println("kdtree's size is: " + kdtree.size());
        Point2D p = new Point2D(0.2, 0.85);
        StdOut.printf("root is: %s\n", kdtree.root.p);
        StdOut.printf("nearest point to %s is: %s\n", p, kdtree.nearest(p));
    }
}
