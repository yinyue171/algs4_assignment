import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double[] x;
    private int expT;
    
    public PercolationStats(int num, int times) {
        if (num <= 0 || times <= 0) 
        {
            throw new IllegalArgumentException("Illeagal Argument");
        }
        x = new double[times+1];
        expT = times;
        for (int i = 1; i <= times; i++) {
            Percolation perco = new Percolation(num);
            boolean[] isEmptySiteLine = new boolean[num+1];
            int lineNum = 0;
            while (true) {    
                int posX, posY;
                do {
                    posX = StdRandom.uniform(num)+1;
                    posY = StdRandom.uniform(num)+1;
                } while (perco.isOpen(posX, posY));
                perco.open(posX, posY);
                x[i] += 1;
                if (!isEmptySiteLine[posX]) {
                    isEmptySiteLine[posX] = true;
                    lineNum++;
                }
                if (lineNum == num && perco.percolates())  break;
            }
            x[i] = x[i] / (double) (num*num);
        }
    }
    
    public double mean() {
        double mu = 0.0;
        for (int i = 1; i <= expT; ++i) {
            mu += x[i];
        }
        return mu / (double) expT;
    }
    
    public double stddev() {
        if (expT == 1)
            return Double.NaN;
        double sigma = 0.0;
        double mu = mean();
        for (int i = 1; i <= expT; ++i) {
            sigma += (x[i]-mu)*(x[i]-mu);
        }
        return Math.sqrt(sigma / (double) (expT-1));
    }
    
    public double confidenceLo() {
        double mu = mean();
        double sigma = stddev();
        return mu - 1.96*sigma / Math.sqrt(expT);
    }
    
    public double confidenceHi() {
        double mu = mean();
        double sigma = stddev();
        return mu + 1.96*sigma / Math.sqrt(expT);
    }
    
    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);
        int times = Integer.parseInt(args[1]);
        PercolationStats percoStats = new PercolationStats(num, times);
        StdOut.printf("mean                    = %f\n", percoStats.mean());
        StdOut.printf("stddev                  = %f\n", percoStats.stddev());
        StdOut.printf("95%% confidence interval = %f, %f\n", 
                      percoStats.confidenceLo(), percoStats.confidenceHi());
        
    }
    
    
}