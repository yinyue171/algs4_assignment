import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int brdDim;
    private int[][] brdBlocks;
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) 
    {
        if (blocks == null) 
            throw new NullPointerException();
        brdDim = blocks[0].length;
        brdBlocks = new int[brdDim][brdDim];
        for (int i = 0; i < brdDim; i++) {
            for (int j = 0; j < brdDim; j++)
                brdBlocks[i][j] = blocks[i][j];
        }
    }
    public int dimension()  // board dimension n
    {
        return brdDim;
    }
    public int hamming()    // number of blocks out of place
    {
        int hamNum = 0;
        for (int i = 0; i < brdDim; i++) {
            for (int j = 0; j < brdDim; j++) {
                if (brdBlocks[i][j] != i * brdDim + j + 1 && brdBlocks[i][j] != 0) 
                    hamNum++;            
            }
        }
        return hamNum;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan()                 
    {
        int mhNum = 0;
        int blockNum = 0;
        int posX = 0;
        int posY = 0;
        for (int i = 0; i < brdDim; i++) {
            for (int j = 0; j < brdDim; j++) {
                blockNum = i * brdDim + j + 1;                
                if (brdBlocks[i][j] != blockNum && brdBlocks[i][j] != 0) {
                    posX = brdBlocks[i][j] / brdDim;
                    posY = brdBlocks[i][j] % brdDim - 1;
                    if (posY < 0) { 
                        posY += brdDim; 
                        posX -= 1; 
                    }
                    mhNum += Math.abs(posY - j) + Math.abs(posX - i);               
                }                
            }
        }
        return mhNum;        
    }
    public boolean isGoal()                // is this board the goal board?
    {        
        for (int i = 0; i < brdDim; i++) {
            for (int j = 0; j < brdDim; j++) {     
                if (brdBlocks[i][j] != i * brdDim + j + 1 
                        && brdBlocks[i][j] != 0) return false;
            }
        }
        return true;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin()                    
    {
        int[][] twinBlocks = new int[brdDim][brdDim];
        for (int i = 0; i < brdDim; i++) {
            for (int j = 0; j < brdDim; j++)                    
                twinBlocks[i][j] = brdBlocks[i][j];
        }
        
        int blockNum = 0;
        boolean exchState = true;
        for (int i = 0; i < brdDim && exchState; i++) {
            for (int j = 1; j < brdDim; j++) { 
                if (twinBlocks[i][j - 1] > twinBlocks[i][j] && exchState 
                        && twinBlocks[i][j] != 0) {
                    blockNum = twinBlocks[i][j];
                    twinBlocks[i][j] = twinBlocks[i][j - 1];
                    twinBlocks[i][j - 1] = blockNum;
                    exchState = false;
                    break;
                }
            }
        }
        if (exchState) {
         for (int i = 0; i < brdDim && exchState; i++) {
                for (int j = 1; j < brdDim; j++) {                               
                    if (twinBlocks[i][j - 1] != 0 && exchState
                      && twinBlocks[i][j] != 0) {
                        blockNum = twinBlocks[i][j];
                        twinBlocks[i][j] = twinBlocks[i][j - 1];
                        twinBlocks[i][j - 1] = blockNum;
                        exchState = false;
                        break;
                    }
                }
            } 
        }        
        return new Board(twinBlocks);
    }
    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == this) return true;
        if (y == null) return false;        
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.brdDim) return false;        
        for (int i = 0; i < this.brdDim; i++) {
            for (int j = 0; j < this.brdDim; j++) {
                if (this.brdBlocks[i][j] != that.brdBlocks[i][j]) 
                    return false;
            }
        }
        return true;
    }
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        Queue<Board> neighbours = new Queue<Board>(); 
        int row = 0;
        int col = 0;                
        boolean findZero = true;
        for (int i = 0; i < brdDim && findZero; i++) {
            for (int j = 0; j < brdDim; j++) {
                if (brdBlocks[i][j] == 0) { 
                    row = i; 
                    col = j; 
                    findZero = false;
                    break;
                }                     
            }
        }
        int[] dx = {-1, 0, 0, 1};
        int[] dy = {0, -1, 1, 0};        
        int[][] tmpBlocks = new int[brdDim][brdDim];
        int posX, posY;
        for (int i = 0; i < 4; i++) {
            posX = row + dx[i];
            posY = col + dy[i];
            if (posX >= 0 && posX < brdDim 
                    && posY >= 0 & posY < brdDim) {                
                for (int j = 0; j < brdDim; j++) {
                    for (int k = 0; k < brdDim; k++) 
                        tmpBlocks[j][k] = brdBlocks[j][k];
                }                    
                int tmpNum = tmpBlocks[row][col];
                tmpBlocks[row][col] = tmpBlocks[posX][posY];
                tmpBlocks[posX][posY] = tmpNum;                    
                Board nbr = new Board(tmpBlocks);
                neighbours.enqueue(nbr);                    
            }       
        }    
        return neighbours;
    }
    // string representation of this board (in the output format specified below)
    public String toString()               
    {
        StringBuilder rStr = new StringBuilder();
        rStr.append(brdDim + "\n");
        for (int i = 0; i < brdDim; i++) {            
            for (int j = 0; j < brdDim; j++) {                
                rStr.append(String.format("%2d ", brdBlocks[i][j]));
            }
            rStr.append("\n");
        }
        return rStr.toString();
    }
    public static void main(String[] args) // unit tests (not graded)
    {
        int[][] blocks = {{0, 1, 3}, { 4, 2, 5}, {7, 8, 6}};        
        Board initBoard = new Board(blocks);
        StdOut.println(initBoard.dimension());
        StdOut.println("hamming = " + initBoard.hamming());
        StdOut.println("manhattan = " + initBoard.manhattan());
        StdOut.println(initBoard.toString());
        StdOut.println(initBoard.twin().toString());
        for (Board board: initBoard.neighbors())
            StdOut.println(board);
    }
}