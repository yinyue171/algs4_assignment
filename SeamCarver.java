import java.awt.Color;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int[][] colorRGB;
    private int width;
    private int height;         
    private Picture picture;
    private static final double MAX_ENERGY = 1000.0;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        width = picture.width();
        height = picture.height();
        this.picture = picture;
        colorRGB = new int[width][height];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
            colorRGB[x][y] = picture.get(x, y).getRGB();
        }
    }
    // current picture
    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
            pic.set(x, y, new Color(colorRGB[x][y]));
        }
        picture = pic;
        return pic;
    }
    // width of current picture
   public     int width() {
       return width;
   }
   // height of current picture
   public     int height() {
       return height;
   }
   // energy of pixel at column x and row y
   public  double energy(int x, int y) {
       checkPos(x, y);
       if (x == 0 || x == width - 1 || y == 0 || y == height - 1) 
           return MAX_ENERGY;
       double xDiff = gradient(picture.get(x - 1, y), picture.get(x + 1, y));
       double yDiff = gradient(picture.get(x, y - 1), picture.get(x, y + 1));
       return Math.sqrt(xDiff + yDiff);
   }
   private double gradient(Color a, Color b) {
       int red = a.getRed() - b.getRed();
       int green = a.getGreen() - b.getGreen();
       int blue = a.getBlue() - b.getBlue();
       return red * red + green * green + blue * blue;
   }   
   private void checkPos(int x, int y) {
       if (x < 0 || x >= width || y < 0 || y >= height)
           throw new java.lang.IndexOutOfBoundsException();
   }
   // sequence of indices for horizontal seam
   public   int[] findHorizontalSeam() { 
       int V = width * height;
       int[] edgeTo = new int[V];
       double[] distTo = new double[V];
       double[][] horizonEnergy = energyTranspose(height, width, true);
       DijkstraSP(false, horizonEnergy, distTo, edgeTo);
       return findSeam(false, distTo, edgeTo);
   }
   // sequence of indices for vertical seam
   public   int[] findVerticalSeam() {
       int V = width * height;
       int[] edgeTo = new int[V];
       double[] distTo = new double[V];
       double[][] verticalEnergy = energyTranspose(width, height, false);       
       DijkstraSP(true, verticalEnergy, distTo, edgeTo);
       return findSeam(true, distTo, edgeTo); 
   }
   private int[] findSeam(boolean isVertical, double[] distTo, int[] edgeTo) {
       int minIndex = 0;       
       double minDist = Double.POSITIVE_INFINITY;
       int seamWidth = width, seamHeight = height;
       if (!isVertical) {     
           seamWidth = height;
           seamHeight = width;
       }
       int[] seamPath = new int [seamHeight];
       for (int i = 0; i < seamWidth; i++)      
           if (distTo[(seamHeight - 1) * seamWidth + i] < minDist) {
           minIndex = (seamHeight - 1) * seamWidth + i;
           minDist = distTo[(seamHeight -1) * seamWidth + i];
       }    
       for (int i = seamHeight - 1; i > 0 ; i--) {
           seamPath[i] = minIndex % seamWidth;
           minIndex = edgeTo[minIndex];
       }
       if (seamPath.length > 1) seamPath[0] = seamPath[1];
       return seamPath;
   }   
   private double[][] energyTranspose(int W, int H, boolean isTranspose) {
       double[][] result = new double[W][H];
       for (int y = 0; y < H; y++)
           for (int x = 0; x < W; x++) {
           if (isTranspose) result[x][y] = energy(y, x);
           else result[x][y] = energy(x, y);
       }   
       return result;        
   }   
   private void DijkstraSP(boolean isVertical, double[][] energy, double[] distTo, int[] edgeTo) {
       int V = width * height;  
       for (int v = 0; v < V; v++) {
           distTo[v] = Double.POSITIVE_INFINITY;
           edgeTo[v] = 0;
       }
       int initLen = width;
       if (!isVertical) initLen = height;
       IndexMinPQ<Double> pq = new IndexMinPQ<Double>(V);       
       for (int v = 0; v < initLen; v++) {
           distTo[v] = MAX_ENERGY;
           pq.insert(v, distTo[v]);
       }
       boolean notFinStat = true;
       while (!pq.isEmpty() && notFinStat) { 
           relax(pq.delMin(), isVertical, energy, distTo, edgeTo, pq);           
           for (int i = V - 1; i > V - initLen; i--) 
               if (distTo[i] != Double.POSITIVE_INFINITY) {
                   notFinStat = false;
                   break;
               }
       }
   }   
   private void relax(int v, boolean isVertical, double[][] energy, double[] distTo, int[] edgeTo, IndexMinPQ<Double> pq) {
       int x, y, w;
       double weight;
       int seamWidth = width, seamHeight = height;  
       if (!isVertical) {
           seamWidth = height; 
           seamHeight = width;
       }
       x = v % seamWidth; 
       y = v / seamWidth;
       if (x == 0 || x == seamWidth -1 || y == seamHeight - 1) 
           return;    
       for (int delta = -1; delta < 2; delta++) {     
           w = (y+1) * seamWidth + (x + delta);
           weight = energy[x + delta][y + 1];      
           if (distTo[w] > distTo[v] + weight) {
               distTo[w] = distTo[v] + weight;
               edgeTo[w] = v;
               if(y + 1 == seamHeight - 1) return;
               if (pq.contains(w)) pq.changeKey(w, distTo[w]);
               else pq.insert(w, distTo[w]);
           }
       }    
   }
   public void removeHorizontalSeam(int[] seam) {
       if (seam.length != width || height <= 1) 
           throw new java.lang.IllegalArgumentException();
       checkSeam(seam, height);
       int[][] copy = new int[width][height-1];
       for (int x = 0; x < width; x++) {
           System.arraycopy(colorRGB[x], 0, copy[x], 0, seam[x]);
           System.arraycopy(colorRGB[x], seam[x]+1, copy[x], seam[x], height-seam[x]-1);
       }
       height--;
       colorRGB = copy;
       picture();
   }
   // remove vertical seam from current picture
   public void removeVerticalSeam(int[] seam) {
       if (seam.length != height || width <= 1) 
           throw new java.lang.IllegalArgumentException();
       checkSeam(seam, width);
       int[][] copy = new int[width-1][height];
       for (int y = 0; y < height; y++) {
           for (int x = 0; x < width; x++) {
               if (x < seam[y]) copy[x][y] = colorRGB[x][y];
               else if (x > seam[y]) copy[x-1][y] = colorRGB[x][y];
           }
       }
       width--;
       colorRGB = copy;  
       picture();
   } 
   private void checkSeam(int[] seam, int lineLen) {
    for (int i = 0; i < seam.length; i++)
        if (seam[i] < 0 || seam[i] >= lineLen)
        throw new IllegalArgumentException();
    for (int i = 1; i < seam.length; i++) 
        if (Math.abs(seam[i - 1] - seam[i]) > 1)
        throw new IllegalArgumentException();       
   }
}
