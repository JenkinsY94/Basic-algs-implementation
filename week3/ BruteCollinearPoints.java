import java.util.Arrays;
import java.util.LinkedList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
public class BruteCollinearPoints {
    private LinkedList<LineSegment> lineSegments = new LinkedList<LineSegment>();
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null)) {
            throw new NullPointerException(
            "Null is not allowed as argument of FastCollinearPoints");
        }
        
        int n = points.length;
        for (int i = 0; i < n-3; i++) {
            Point minP = points[i];
            for (int j = i+1; j < n-2; j++) {
                Double slope1 = points[i].slopeTo(points[j]);
                for (int k = j+1; k < n-1; k++) {
                    Double slope2 = points[i].slopeTo(points[k]);
                    if (slope1.equals(slope2)) {
                        for (int l = k+1; l < n; l++) {
                            Double slope3 = points[i].slopeTo(points[l]);
                            if (slope1.equals(slope3)) {
                                // find segment start point and end point
                                Point startP = points[i];
                                if (startP.compareTo(points[j]) > 0) startP = points[j];
                                if (startP.compareTo(points[k]) > 0) startP = points[k];
                                if (startP.compareTo(points[l]) > 0) startP = points[l];
                                Point endP = points[i];
                                if (endP.compareTo(points[j]) < 0) endP = points[j];
                                if (endP.compareTo(points[k]) < 0) endP = points[k];
                                if (endP.compareTo(points[l]) < 0) endP = points[l];
                                // save this segment to the array
                                lineSegments.add(new LineSegment(startP, endP));
                            }
                        }
                    }
                }
            }
        }
    }
    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lsArray = new LineSegment[lineSegments.size()];
        for (int i = 0; i < lineSegments.size(); i++) {
            lsArray[i] = lineSegments.get(i);
        }
        return lsArray;
    }

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
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdOut.println(collinear.numberOfSegments());
        StdDraw.show();
    }
}
