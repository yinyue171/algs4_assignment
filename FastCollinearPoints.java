import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Quick3way;
import java.util.Arrays;

public class FastCollinearPoints {
    private int numOfSegs;
    private Node last;
    
    private class Node {
        private LineSegment value;
        private Node prev;
    }    
    public FastCollinearPoints(Point[] points) // finds segments
    {
        if (points == null) throw new NullPointerException();   
        
        numOfSegs = 0;
        int num = points.length;   
        Point[] pClone = new Point[num]; 
        
        for (int i = 0; i < num; i++)
        {
            if (points[i] == null) throw new NullPointerException();
            pClone[i] = points[i];
        }        
        Quick3way.sort(pClone);        
        for (int i = 0; i < num - 1; i++) {
            if (pClone[i].compareTo(pClone[i + 1]) == 0) 
                throw new IllegalArgumentException();
        }
        
        Point[] pCopy = new Point[num];        
        for (int i = 0; i < num; i++) {                                            
            if (pClone[i] == null) {                                               
                throw new NullPointerException();                                  
            }                                                                      
                                                                                   
            for (int j = i + 1; j < num; j++) {                                    
                if (pClone[i].compareTo(pClone[j]) == 0) {                         
                    throw new IllegalArgumentException();                          
                }                                                                  
            }                                                                      
            pCopy[i] = pClone[i];                                                  
        }                                                                          
        Arrays.sort(pCopy);                                                        
                                                                                   
        if (num < 4) {                                                             
            return;                                                                
        }                                                                          
                                                                                   
        for (int i = 0; i < num - 1; i++) {                                        
            int tempPointsNum = 0;                                                 
            Point[] tempPoints = new Point[num - 1];                               
                                                                                   
            for (int j = 0; j < num; j++) {                                        
                if (i != j) tempPoints[tempPointsNum++] = pCopy[j];                
            }                                                                      
                                                                                   
            Arrays.sort(tempPoints, pCopy[i].slopeOrder());                        
                                                                                   
            int count = 0;                                                         
            Point min = null;                                                      
            Point max = null;                                                      
                                                                                   
            for (int j = 0; j < (tempPointsNum - 1); j++)
            {                        
                if (pCopy[i].slopeTo(tempPoints[j]) == 
                    pCopy[i].slopeTo(tempPoints[j + 1])) {                                  
                    if (min == null) {                                             
                        if (pCopy[i].compareTo(tempPoints[j]) > 0) {               
                            max = pCopy[i];                                        
                            min = tempPoints[j];                                   
                        } else {                                                   
                            max = tempPoints[j];                                   
                            min = pCopy[i];                                        
                        }                                                          
                    }                                                              
                                                                                   
                    if (min.compareTo(tempPoints[j + 1]) > 0) {                    
                        min = tempPoints[j + 1];                                   
                    }                                                              
                                                                                   
                    if (max.compareTo(tempPoints[j + 1]) < 0) {                    
                        max = tempPoints[j + 1];                                   
                    }                                                              
                                                                                   
                    count++;                                                       
                                                                                   
                    if (j == (tempPointsNum - 2)) {                                
                        if (count >= 2 && pCopy[i].compareTo(min) == 0) {          
                            addLine(min, max);                                     
                        }                                                          
                                                                                   
                        count = 0;                                                 
                        min = null;                                                
                        max = null;                                                
                    }                                                              
                } else {                                                           
                    if (count >= 2 && pCopy[i].compareTo(min) == 0) {              
                        addLine(min, max);                                         
                    }                                                              
                                                                                   
                    count = 0;                                                     
                    min = null;                                                    
                    max = null;                                                    
                } 
            }
        }
    }
    private void addLine(Point a, Point b) {          
        if (last != null) {                           
            Node newNode = new Node();                
            newNode.prev = last;                      
            newNode.value = new LineSegment(a, b);    
            last = newNode;                           
        } else {                                      
            last = new Node();                        
            last.value = new LineSegment(a, b);       
        }                                             
        numOfSegs++;                                 
    }                                                 

    

    public  int numberOfSegments()        // the number of line segments
    {   
     
        return numOfSegs;
    }
    public LineSegment[] segments()                // the line segments
    {  
        LineSegment[] lines = new LineSegment[numOfSegs];        
        Node current = last;                                      
                                                                  
        for (int i = 0; i < numOfSegs; i++) {                    
            lines[i] = current.value;                             
            current = current.prev;                               
        }                                                         
                                                                  
        return lines;                                             

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
        FastCollinearPoints bcPoints = new FastCollinearPoints(points);
        StdOut.println(bcPoints.numberOfSegments());
        for (int i = 0; i < bcPoints.numberOfSegments(); i++) {
            StdOut.println(bcPoints.segments()[i].toString());
        }
    }
}