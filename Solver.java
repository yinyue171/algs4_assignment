import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Board initBoard;
    private int moveCount;
    private MinPQ<SearchNode> pq;
    private SearchNode finalNode;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)           
    {
        initBoard = initial;
        moveCount = 0;
        finalNode = null;
        if (initBoard.isGoal()) {
            finalNode = new SearchNode(initBoard);
            finalNode.moves = moveCount;
            return;
        }
        pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initBoard));
        pq.insert(new SearchNode(initBoard.twin()));
        boolean findGoal = true;                
        while (findGoal) {            
            SearchNode srNode = pq.delMin();            
            for (Board nbrBoard: srNode.a.neighbors()) {
                if (nbrBoard.isGoal()) {
                    finalNode = new SearchNode(nbrBoard);
                    finalNode.prev = srNode;
                    finalNode.moves = srNode.moves + 1;
                    moveCount = finalNode.moves;
                    findGoal = false;                    
                    break;
                } else {
                    if (srNode.prev == null || !nbrBoard.equals(srNode.prev.a)) {
                        SearchNode nextNode = new SearchNode(nbrBoard);
                        nextNode.prev = srNode;
                        nextNode.moves = srNode.moves + 1;                        
                        pq.insert(nextNode);
                    }
                }         
            }            
        }
    }
    public boolean isSolvable()            // is the initial board solvable?
    {
     Board solveBoard = null;
     if (solution() != null) {
         for (Board board : solution()) {
             solveBoard = board;
             break;
         }
     }
     if (solveBoard == null) {
         moveCount = -1;
         return false;
     }
     return true;
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()                     
    {
        if (!isSolvable()) moveCount = -1;
        return moveCount; 
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()      
    {
        Stack<Board> neighbours = new Stack<Board>(); 
        if (finalNode == null) {
             StdOut.println("No solution possible");
             return null;
        }
        SearchNode curNode = finalNode;
        SearchNode preNode = curNode;
        do {
            neighbours.push(curNode.a);
            preNode = curNode;
            curNode = curNode.prev;
        } while (curNode != null);        
        if (!preNode.a.equals(initBoard)) return null;
        return neighbours;
    }
    private static class SearchNode implements Comparable<SearchNode> {        
        private final Board a;       // Board involved in event, possibly null
        private SearchNode prev;        
        private int moves;
        public SearchNode(Board a) {
            this.a = a;
            prev = null;
            moves = 0;            
        }
        public int compareTo(SearchNode that) {
            return Integer.compare(this.a.manhattan() + moves, 
              that.a.manhattan() + that.moves);
        }
    }
   
    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}