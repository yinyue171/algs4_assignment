import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private boolean[] openStates;
    private int row, col;
    private WeightedQuickUnionUF wquUF;
    private WeightedQuickUnionUF wquUFTop;
    private boolean isPercolates;
    
    public Percolation(int num) {
        if (num < 1) throw new IllegalArgumentException("Illeagal Argument");
        wquUF = new WeightedQuickUnionUF(num*num + 2);
        wquUFTop = new WeightedQuickUnionUF(num*num + 1);
        isPercolates = false;
        row = num;
        col = num;
        openStates = new boolean[num*num+1];
        for (int i = 0; i < num*num + 1; i++)
            openStates[i] = false;
    }
    
    private void validate(int i, int j) {
        if (i < 1 || i > row) 
            throw new IndexOutOfBoundsException("row index i out of bounds");
        if (j < 1 || j > col) 
            throw new IndexOutOfBoundsException("col index j out of bounds");        
    }
    
    public void open(int i, int j) {
        validate(i, j);
        int curIdx = (i-1)*col + j; 
        if (openStates[curIdx]) return;
        openStates[curIdx] = true;
        if (i == 1) {
            wquUF.union(curIdx, 0);
            wquUFTop.union(curIdx, 0);
        }
        if (i == row) {
            wquUF.union(curIdx, row*col+1);
        }
        
        int[] dx = {1, -1, 0, 0};
        int[] dy = {0, 0, 1, -1};
        for (int k = 0; k < 4; k++) {
            int posX = i + dx[k];
            int posY = j + dy[k];
            if (posX <= row && posX >= 1 
                    && posY <= row && posY >= 1 
                    && isOpen(posX, posY)) {
                wquUF.union(curIdx, (posX-1)*col+posY);
                wquUFTop.union(curIdx, (posX-1)*col+posY);
            }
        }
    }
    
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return openStates[(i-1)*col + j];
    }
    
    public boolean isFull(int i, int j) {
        validate(i, j);
        int curIdx = (i-1)*col+j;
        if (openStates[curIdx] && wquUFTop.find(curIdx) == wquUFTop.find(0)) return true;
        return false;
    }
    
    public boolean percolates() {
        if (!isPercolates && wquUF.find(0) == wquUF.find(row*col+1))
        {
            isPercolates = true; 
        }
        return isPercolates;
    }
    
    public static void main(String[] args) {
        Percolation perco = new Percolation(3);
        perco.open(1, 1);
        perco.open(1, 2);
        perco.open(2, 1);
        perco.open(3, 1);
        System.out.println(perco.percolates());
    }
    
}