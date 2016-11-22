import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private LinkedList<LineSegment> lineSegments = new LinkedList<LineSegment>();
    // finds all line segments containing 4 or more points.
    public FastCollinearPoints(Point[] points) {
        // no null element allowed
        if (points == null || Arrays.asList(points).contains(null)) {
            throw new NullPointerException(
            "Null is not allowed as argument of FastCollinearPoints");
        }
        // sort points using compareTo method.
        Arrays.sort(points); 
        // not allow duplicate points
        for (int i = 0; i < points.length-1; ++i) {
            if (points[i].compareTo(points[i+1]) == 0) 
                throw new IllegalArgumentException(
                    "No duplicate points allowed!");
        }
        for (int i = 0; i < points.length; ++i) {
            // to make sure p here has the same original order as points, 
            // then sort p according to elements' slope to p[i]
            Point[] p = new Point[points.length];
            System.arraycopy(points, 0, p, 0, points.length); 
            Arrays.sort(p, p[i].slopeOrder()); 
            
            for (int j = 1; j < p.length - 2;) {
                // slope to p[0] instead of p[i] because p has been sorted.
                Double slope1 = p[j].slopeTo(p[0]); // double?
                // StdOut.printf("i=%d, j=%d, slope1=%f\n",i,j,slope1);
                int count = 1;
                while (j < p.length-1 && 
                      slope1.equals(p[++j].slopeTo(p[0]))) { // the same as line 27.
                    ++count;
                }
                // check corner case
                if (j == p.length-1 && slope1.equals(p[j].slopeTo(p[0]))) j = j+1;
                if (count >= 3) {
                    Point startP = findMinPoint(p, count, j-1);
                    Point endP = findMaxPoint(p, count, j-1);
                    // StdOut.printf("add segment when: i=%d, j=%d\t",i,j-1);
                    // StdOut.println("StartP= "+startP+", endP= "+endP);
                    // ensure that no duplicate in lineSegments.
                    // reason: if startP smaller than p[0], this segment is already check in previous round
                    if (startP.compareTo(p[0]) >= 0) 
                        lineSegments.add(new LineSegment(startP, endP));
                }
            }
            p = null;
        }      
    }

    // find the min point in given array of Point segment.
    private Point findMinPoint(Point[] points, int count, int end) {
        Point minP = points[0];
        int start = end-count+1;
        for (int i = start; i <= end; ++i) {
            if (points[i].compareTo(minP) < 0) 
                minP = points[i];
        }
        return minP;
    }
     // find the max point in given array of Point segment.
    private Point findMaxPoint(Point[] points, int count, int end) {
        Point maxP = points[0];
        int start = end-count+1;
        for (int i = start; i <= end; ++i) {
            if (points[i].compareTo(maxP) > 0) 
                maxP = points[i];
        }
        return maxP;
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }
    // the line segments
    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[0]); // ?
    }
    // unit test
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenColor(StdDraw.BLUE);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
