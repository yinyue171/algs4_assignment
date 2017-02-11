import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private static final int[] XDELTA = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] YDELTA = { -1, 0, 1, -1, 1, -1, 0, 1 };
    private Trie dict;
    private String[][] gameStr;
    private Bag<String> words;  // found words
    private Bag<Trie.Node> nodes;    // nodes of found words
    
    public BoggleSolver(String[] dictionary) {
        dict = new Trie();
        for (int i = 0; i < dictionary.length; i++)
            dict.put(dictionary[i], 1);
    }
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        words = new Bag<String>();
        nodes = new Bag<Trie.Node>();
        int col = board.cols(), row = board.rows();  
        boolean[][] marked = new boolean[row][col];
        gameStr = new String[row][col];  
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) 
            gameStr[i][j] = representQ(i, j, board);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {    
                Trie.Node pNode = dict.getNext(dict.root, gameStr[i][j]);
                if (pNode != null)
                 dfs(board, marked, i, j, pNode, gameStr[i][j]);
            }
        }
        for (Trie.Node n : nodes)
            n.val = 1;
        return words;
    }
    private void dfs(BoggleBoard board, boolean[][] marked,
                     int x, int y, Trie.Node pNode, String preStr) {  
        marked[x][y] = true;  
        String curStr;
        int xNeighbor, yNeighbor;
        Trie.Node queryNode;
        if (pNode.val == 1 && preStr.length() > 2) {
            words.add(preStr);
            nodes.add(pNode);
            pNode.val = -1;
        }
        for (int i = 0; i < XDELTA.length; i++) {
            xNeighbor = x + XDELTA[i];
            yNeighbor = y + YDELTA[i];
            if (validPos(xNeighbor, yNeighbor, board)
                    && !marked[xNeighbor][yNeighbor]) {
                queryNode = dict.getNext(pNode, gameStr[xNeighbor][yNeighbor]);
                if (queryNode != null) {
                    curStr = preStr + gameStr[xNeighbor][yNeighbor];
                    dfs(board, marked, xNeighbor, yNeighbor, queryNode, curStr);
                }
            }
        }
        marked[x][y] = false;
    }
    private String representQ(int x, int y, BoggleBoard board) {
        String str;
        if (board.getLetter(x, y) == 'Q')
            str = "QU";
        else
            str = "" + board.getLetter(x, y);
        return str;
    }
    private boolean validPos(int x, int y, BoggleBoard board) {
        return x >= 0 && x < board.rows() && y >= 0 && y < board.cols();
    }
    public int scoreOf(String word) {
        int score = 0;
        int len = word.length();
        if (dict.contains(word)) {
            if (len > 2 && len < 5) score = 1;
            else if (len == 5) score = 2;
            else if (len == 6) score = 3;   
            else if (len == 7) score = 5;
            else if (len >= 8) score = 11;
        }
        return score;
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        long startTime, endTime;
        startTime = System.currentTimeMillis();
        for (String word : solver.getAllValidWords(board)) {
            // StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        endTime = System.currentTimeMillis();
        double time = endTime - startTime;
        System.out.println("time = " + Double.toString(time));
    }
    private static class Trie {
        private static int R = 26; // Radix, English alphabet
        private static final int OFFSET = 'A'; 
        private static final char Q = 'Q';
        private static final char U = 'U';
        public Node root;
        
        class Node {
            int val;
            Node[] next = new Node[R];
        } 
        public int get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return 0;
            return x.val;
        }
        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[c - OFFSET], key, d + 1);
        }
        public boolean contains(String key) {
            return get(key) != 0;
        }    
        public void put(String key, int val) {
            root = put(root, key, val, 0);
        }
        private Node put(Node x, String key, int val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.val = val;
                return x;
            }
            char c = key.charAt(d);
            x.next[c - OFFSET] = put(x.next[c - OFFSET], key, val, d + 1);
            return x;
        }
        public Node getNext(Node x, String key) {
            if (x == null || key.length() == 0) return null;
            Node p;
            if (key.length() > 1) { 
                p = x.next[Q - OFFSET];
                if (p == null) return null;
                p = p.next[U - OFFSET];
            } else {
                p = x.next[key.charAt(0) - OFFSET];
            }
            return p;    
        }
    }
}