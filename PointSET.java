import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.Queue;

public class PointSET {
    private SET<Point2D> pSet;    
    // construct an empty set of points 
    public PointSET()
    {
        pSet = new SET<Point2D>();
    }
    // is the set empty? 
    public boolean isEmpty()
    {
        return pSet.isEmpty();
    }
    // number of points in the set 
    public int size()
    {
        return pSet.size();
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        pSet.add(p);        
    }
    // does the set contain point p? 
    public boolean contains(Point2D p)
    {
        if (pSet == null) 
            throw new NullPointerException();
        return pSet.contains(p);
    }
     // draw all points to standard draw 
    public void draw()
    {
        if (pSet == null) 
            throw new NullPointerException();
        for (Point2D curPoint : pSet)
            curPoint.draw();
    }
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect)
    {
        if (pSet == null) 
            throw new NullPointerException();
        Queue<Point2D> rectPoints = new Queue<Point2D>();       
        for (Point2D curPoint : pSet) {
            if (rect.distanceTo(curPoint) == 0) rectPoints.enqueue(curPoint);
        }
        return rectPoints;
    }
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p)
    {
        Point2D nrPoint = null;
        boolean shortInit = false;
        double shortestDist = 0.0;
        if (pSet == null) 
            throw new NullPointerException();
        for (Point2D curPoint : pSet) {
            if (!shortInit) {
                shortestDist = curPoint.distanceTo(p);
                nrPoint = curPoint;
                shortInit = true;
            }
            if (curPoint.distanceTo(p) < shortestDist) {
                nrPoint = curPoint;
                shortestDist = curPoint.distanceTo(p);
            }
        }
        return nrPoint;
    }
    // unit testing of the methods (optional) 
    public static void main(String[] args)
    {        
        PointSET pointSet = new PointSET();
        while (!StdIn.isEmpty())        
         {
             double posX = StdIn.readDouble();
             double posY = StdIn.readDouble();
             Point2D point = new Point2D(posX, posY);
             pointSet.insert(point);
         }
        if (pointSet.isEmpty()) StdOut.println("An empty point set.");
        StdOut.println("size = " + pointSet.size());        
        StdOut.println("range: ");
        for (Point2D curP : pointSet.range(new RectHV(0.01, 0.04, 0.89, 0.99)))            
            StdOut.println(curP);            
        StdOut.println("nearest: " + pointSet.nearest(new Point2D(0.1, 0.7)));  
        StdOut.println("nearest: " + pointSet.nearest(new Point2D(0.206107, 0.5))); 
    }
}