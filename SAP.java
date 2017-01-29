import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;
    private BreadthFirstDirectedPaths bfsPrior, bfsNext;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }
    // length of shortest ancestral bfs between v and w; -1 if no such bfs
    public int length(int u, int w) {
        checkInput(u, w);
        int an = ancestor(u, w);
        if (an == -1)
            return -1;
        return bfsPrior.distTo(an) + bfsNext.distTo(an);
    }
    // a common ancestor of v and w that participates in a shortest
    // ancestral bfs; -1 if no such bfs
    public int ancestor(int u, int w) {
        checkInput(u, w);
        int result = -1;
        int shortestLength = Integer.MAX_VALUE;
        bfsPrior = new BreadthFirstDirectedPaths(G, u);
        bfsNext = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfsPrior.hasPathTo(i) && bfsNext.hasPathTo(i)) {
                int dist = bfsPrior.distTo(i) + bfsNext.distTo(i);
                if (dist < shortestLength) {
                    shortestLength = dist;
                    result = i;
                }
            }
        }
        return result;
    }
    // length of shortest ancestral bfs between any vertex in v and any
    // vertex in w; -1 if no such bfs
    public int length(Iterable<Integer> u, Iterable<Integer> w) {
        checkInput(u, w);
        int an = ancestor(u, w);
        if (an == -1)
            return -1;
        return bfsPrior.distTo(an) + bfsNext.distTo(an);
    }
    // a common ancestor that participates in shortest ancestral bfs;
    // -1 if no such bfs
    public int ancestor(Iterable<Integer> u, Iterable<Integer> w) {
        checkInput(u, w);
        int result = -1;
        int shortestLength = Integer.MAX_VALUE;
        bfsPrior = new BreadthFirstDirectedPaths(G, u);
        bfsNext = new BreadthFirstDirectedPaths(G, w);
        for (int i = 0; i < G.V(); i++) {
            if (bfsPrior.hasPathTo(i) && bfsNext.hasPathTo(i)) {
                int dist = bfsPrior.distTo(i) + bfsNext.distTo(i);
                if (dist < shortestLength) {
                    shortestLength = dist;
                    result = i;
                }
            }
        }
        return result;
    }
    // Helper methods for validate input argument
    private void checkInput(int u, int w) {
        if (u < 0 || u > G.V() - 1 || w < 0 || w > G.V() - 1)
            throw new java.lang.IndexOutOfBoundsException();
    }
    private void checkInput(Iterable<Integer> u, Iterable<Integer> w) {
        for (int x : u)
            if (x < 0 || x > G.V() - 1)
            throw new java.lang.IndexOutOfBoundsException();
        for (int y : w)
            if (y < 0 || y > G.V() - 1)
            throw new java.lang.IndexOutOfBoundsException();
    }
    // for unit testing of this class (such as the one below)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int u = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(u, w);
            int ancestor = sap.ancestor(u, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}