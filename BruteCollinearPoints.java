import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Quick3way;

public class BruteCollinearPoints {
    private int numOfSegs;
    private LineSegment[] lineSegs;  
    public BruteCollinearPoints(Point[] points) 
    // finds all line segments containing 4 points
    {
        if (points == null) throw new NullPointerException();
        Point[] pcopy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            pcopy[i] = points[i];
        }        
        for (Point p : pcopy)
        {
            if (p == null) throw new NullPointerException();            
        }
        Quick3way.sort(pcopy);
        for (int i = 0; i < pcopy.length - 1; i++) {
            if (pcopy[i].compareTo(pcopy[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
        numOfSegs = 0;
        lineSegs = new LineSegment[pcopy.length];
        
        for (int i = 0; i < pcopy.length && pcopy.length >= 4; i++) {
            for (int j = i + 1; j < pcopy.length; j++) {
                for (int k = j + 1; k < pcopy.length; k++) {
                    for (int m = k + 1; m < pcopy.length; m++) {
                        if (pcopy[i].slopeTo(pcopy[j]) == pcopy[i].slopeTo(pcopy[k]) 
                                && 
                            pcopy[i].slopeTo(pcopy[j]) == pcopy[i].slopeTo(pcopy[m])) {
                         lineSegs[numOfSegs] = new LineSegment(pcopy[i], 
                                    pcopy[m]);                          
                            numOfSegs++;
                        }
                    }
                }
            }
        }
    }
    public  int numberOfSegments()        // the number of line segments
    {   
     return numOfSegs;
    }
    public LineSegment[] segments()                // the line segments
    {
        if (lineSegs == null) throw new NullPointerException();
        LineSegment[] tSegs = new LineSegment[numOfSegs];
        for (int i = 0; i < numOfSegs; i++) {
            tSegs[i] = lineSegs[i];
        }              
        return tSegs;
    }
    public static void main(String[] args)
    {        
        String filename = args[0];
        In in = new In(filename); 
        int line = in.readInt(); 
        Point[] points = new Point[line];
        
        for (int i = 0; i < line; i++) {  
            int x = in.readInt();  
            int y = in.readInt();  
            points[i] = new Point(x, y);  
        }
        BruteCollinearPoints bcPoints = new BruteCollinearPoints(points);
        StdOut.println(bcPoints.numberOfSegments());
        for (int i = 0; i < bcPoints.numberOfSegments(); i++) {
            StdOut.println(bcPoints.segments()[i].toString());
        }
    }
}