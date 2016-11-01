import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
    private static final double X_POS_MIN = 0.0;
    private static final double X_POS_MAX = 1.0;
    private static final double Y_POS_MIN = 0.0;
    private static final double Y_POS_MAX = 1.0;       
    private static class KdNode {
        private Point2D point;        
        private boolean direction;        
        private RectHV rect;
        private KdNode lb, rt;        
        KdNode(Point2D p, boolean drct) {
            if (p == null)
                throw new NullPointerException();
            direction = drct;
            point = p;
            rect = null;
            lb = null;
            rt = null;                     
        }
    }
    private int numOfSize;
    private KdNode kdt;    
    
// construct an empty set of points 
    public KdTree()
    {
        kdt = null;
        numOfSize = 0;
    }
    // is the set empty? 
    public boolean isEmpty()
    {
        return size() == 0;
    }
    // number of points in the set 
    public int size()
    {
        return numOfSize;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {     
        if (p == null)
            throw new NullPointerException();
        kdt = insertTree(kdt, p, true);    
        if (kdt.rect == null) 
            kdt.rect = new RectHV(X_POS_MIN, Y_POS_MIN, X_POS_MAX, Y_POS_MAX);
    }
    private KdNode insertTree(KdNode kd, Point2D p, boolean drct) {
        if (kd == null) {
            numOfSize++;
            kd = new KdNode(p, drct);
            return kd;
        }        
        if (kd.point.equals(p)) return kd;
        double cmp;        
        if (kd.direction) {
            cmp = p.x() - kd.point.x();
            if (cmp < 0.0) {
                kd.lb = insertTree(kd.lb, p, !kd.direction);                
                if (kd.lb.rect == null)
                    kd.lb.rect = new RectHV(kd.rect.xmin(),
                                        kd.rect.ymin(),
                                        kd.point.x(),
                                        kd.rect.ymax());
            }
            else if (cmp > 0.0) {
                kd.rt = insertTree(kd.rt, p, !kd.direction);                
                if (kd.rt.rect == null)
                    kd.rt.rect = new RectHV(kd.point.x(),
                                        kd.rect.ymin(),
                                        kd.rect.xmax(),
                                        kd.rect.ymax());
            }
            else if (cmp == 0.0) {
                if (p.y() - kd.point.y() < 0.0) {
                    kd.lb = insertTree(kd.lb, p, !kd.direction);  
                    if (kd.lb.rect == null)
                        kd.lb.rect = new RectHV(kd.rect.xmin(),
                                            kd.rect.ymin(),
                                            kd.point.x(),
                                            kd.rect.ymax());
                } else {
                    kd.rt = insertTree(kd.rt, p, !kd.direction); 
                    if (kd.rt.rect == null)
                        kd.rt.rect = new RectHV(kd.point.x(),
                                            kd.rect.ymin(),
                                            kd.rect.xmax(),
                                            kd.rect.ymax());
                }
            }
        } else {
            cmp = p.y() - kd.point.y();
            if (cmp < 0.0) {
                kd.lb = insertTree(kd.lb, p, !kd.direction);
                if (kd.lb.rect == null)
                    kd.lb.rect = new RectHV(kd.rect.xmin(),
                                        kd.rect.ymin(),
                                        kd.rect.xmax(),
                                        kd.point.y());
            }
            else if (cmp > 0.0) {
                kd.rt = insertTree(kd.rt, p, !kd.direction);                
                if (kd.rt.rect == null)
                    kd.rt.rect = new RectHV(kd.rect.xmin(),
                                        kd.point.y(),
                                        kd.rect.xmax(),
                                        kd.rect.ymax());
            }
            else if (cmp == 0.0) {
                if (p.x() - kd.point.x() < 0.0) {
                    kd.lb = insertTree(kd.lb, p, !kd.direction);                    
                    if (kd.lb.rect == null)
                        kd.lb.rect = new RectHV(kd.rect.xmin(),
                                            kd.rect.ymin(),
                                            kd.rect.xmax(),
                                            kd.point.y());
                } else {
                    kd.rt = insertTree(kd.rt, p, !kd.direction);                    
                    if (kd.rt.rect == null)
                        kd.rt.rect = new RectHV(kd.rect.xmin(),
                                            kd.point.y(),
                                            kd.rect.xmax(),
                                            kd.rect.ymax());
                }
            }         
        }
        return kd;
    }
    // does the set contain point p? 
    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new NullPointerException();
        return search(kdt, p);        
    }
    private boolean search(KdNode kd, Point2D p)
    {        
        if (kd == null) return false;                
        double cmp;
        boolean result = false;
        if (kd.direction) cmp = p.x() - kd.point.x();
        else cmp = p.y() - kd.point.y();        
        if (kd.point.equals(p)) return true;
        if (cmp < 0.0) result = search(kd.lb, p);
        else if (cmp > 0.0) result = search(kd.rt, p);     
        else if (cmp == 0.0) {
            if (kd.direction) {
            if (p.y() - kd.point.y() < 0.0) result = search(kd.lb, p);
            else result = search(kd.rt, p);
            } else {
                if (p.x() - kd.point.x() < 0.0) result = search(kd.lb, p);
                else result = search(kd.rt, p);
            }
        }
        return result;
    }
     // draw all points to standard draw 
    public void draw()
    {
        if (kdt == null) 
            throw new NullPointerException();
        boolean rtState = true;
        for (KdNode curNode : this.kdnodes()) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            curNode.point.draw();
            StdDraw.setPenRadius();
            if (rtState) {
                curNode.rect.draw();
                StdDraw.setPenColor(StdDraw.RED);
                new Point2D(curNode.point.x(), Y_POS_MAX).
                    drawTo(new Point2D(curNode.point.x(), Y_POS_MIN));
                rtState = false;
            } else {                
                if (curNode.direction) {
                    StdDraw.setPenColor(StdDraw.RED);
                    new Point2D(curNode.point.x(), curNode.rect.ymin()).
                        drawTo(new Point2D(curNode.point.x(), curNode.rect.ymax()));                    
                } else {
                    StdDraw.setPenColor(StdDraw.BLUE);
                    new Point2D(curNode.rect.xmax(), curNode.point.y()).
                        drawTo(new Point2D(curNode.rect.xmin(), curNode.point.y()));                    
                }                
            }            
        }
    }
    // all points in the kd tree
    private Iterable<KdNode> kdnodes()
    {
        Queue<KdNode> kNodes = new Queue<KdNode>();               
        preorder(kdt, kNodes);
        return kNodes;
    }
    private void preorder(KdNode root, Queue<KdNode> q) {
        if (root == null) return;
        q.enqueue(root);
        preorder(root.lb, q);
        preorder(root.rt, q);
    }
    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect)
    {
        Queue<Point2D> rectPoints = new Queue<Point2D>();
        addRectPoint(kdt, rect, rectPoints);
        return rectPoints;
    }
    private void addRectPoint(KdNode p, RectHV rect, Queue<Point2D> q) {
        if (p == null) return;
        if (rect.contains(p.point)) {
            q.enqueue(p.point);
            addRectPoint(p.rt, rect, q);
            addRectPoint(p.lb, rect, q);
            return;
        }
        double cmpxMin = p.point.x() - rect.xmin();
        double cmpxMax = p.point.x() - rect.xmax();
        double cmpyMin = p.point.y() - rect.ymin();
        double cmpyMax = p.point.y() - rect.ymax();        
        if (p.direction) {
            if (cmpxMin < 0.0) addRectPoint(p.rt, rect, q);
            if (cmpxMax > 0.0) addRectPoint(p.lb, rect, q);
            if (cmpxMin >= 0.0 && cmpxMax <= 0.0) {
                addRectPoint(p.rt, rect, q);
                addRectPoint(p.lb, rect, q);
            }
        } else {
            if (cmpyMin < 0.0) addRectPoint(p.rt, rect, q);
            if (cmpyMax > 0.0) addRectPoint(p.lb, rect, q);
            if (cmpyMin >= 0.0 && cmpyMax <= 0.0) { 
                addRectPoint(p.rt, rect, q);
                addRectPoint(p.lb, rect, q);
            }
        }  
    }
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p)
    {
        if (p == null) 
            throw new NullPointerException();
        if (kdt != null)
            return nearPoint(kdt, p, kdt).point;       
        return null;
    }
    private KdNode nearPoint(KdNode kd, Point2D p, KdNode q) {        
        if (kd == null) return q;
        double nrDist = p.distanceSquaredTo(q.point);
        double kdDist = p.distanceSquaredTo(kd.point);
        if (nrDist >= kdDist || 
            nrDist >= kd.rect.distanceSquaredTo(p))
        {
             if (nrDist > kdDist) q = kd;
             if (kd.direction) {
                 double cmpX = p.x() - kd.point.x();
                 if (cmpX < 0.0) {
                     if (kd.lb != null) q = nearPoint(kd.lb, p, q);
                     if (kd.rt != null) q = nearPoint(kd.rt, p, q);
                 } else {            
                     if (kd.rt != null) q = nearPoint(kd.rt, p, q);
                     if (kd.lb != null) q = nearPoint(kd.lb, p, q);
                 }
             } else {
                 double cmpY = p.y() - kd.point.y();
                 if (cmpY < 0.0) {
                     if (kd.lb != null) q = nearPoint(kd.lb, p, q);
                     if (kd.rt != null) q = nearPoint(kd.rt, p, q);
                 } else {
                     if (kd.rt != null) q = nearPoint(kd.rt, p, q);
                     if (kd.lb != null) q = nearPoint(kd.lb, p, q);           
                 }
             }        
        }        
        return q;
    }    
    
    // unit testing of the methods (optional) 
    public static void main(String[] args)
    {        
        KdTree kdt = new KdTree();
        while (!StdIn.isEmpty())        
        {
             double posX = StdIn.readDouble();
             double posY = StdIn.readDouble();
             Point2D point = new Point2D(posX, posY);
             kdt.insert(point);
        } 
        if (kdt.isEmpty()) StdOut.println("An empty point set.");
        StdOut.println("size = " + kdt.size());
        StdOut.println("(0.024472, 0.654508)" +
                       kdt.contains(new Point2D(0.024472, 0.654508)));        
        StdOut.println("(0.024888, 0.555508)" +
                       kdt.contains(new Point2D(0.024888, 0.555508))); 
        // kdt.draw();
        StdOut.println("range: ");
        for (Point2D curP : kdt.range(new RectHV(0.01, 0.04, 0.89, 0.99)))
            StdOut.println(curP);            
        StdOut.println("nearest: " + kdt.nearest(new Point2D(0.1, 0.7)));
        StdOut.println("nearest: " + kdt.nearest(new Point2D(0.206107, 0.5))); 
    }
}