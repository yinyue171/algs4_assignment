import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class WordNet {
    private  ArrayList<String> wordList;
    private  HashMap<String, Bag<Integer>> idMap;
    private  HashSet<Integer> ids;
    private  Digraph G;
    private  SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        ids = new HashSet<Integer>();
        wordList = new ArrayList<String>();
        idMap = new HashMap<String, Bag<Integer>>();
        readSynsets(synsets);
        // build digraph
        G = new Digraph(wordList.size());
        buildWordDigraph(G, buildHynMap(hypernyms));
        // check is rooted DAG
        isRootedDAG();
        // create sap instance
        sap = new SAP(G);
    }
    private void readSynsets(String filepath) {
        In in = new In(filepath);
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);
            ids.add(id);
            wordList.add(fields[1]);
            for (String n : fields[1].split(" ")) {
                if (!idMap.containsKey(n))
                    idMap.put(n, new Bag<Integer>());
                idMap.get(n).add(id);
            }
        }
    }
    private Map<Integer, Bag<Integer>> buildHynMap(String filepath) {
        In in = new In(filepath);
        Map<Integer, Bag<Integer>> map = new HashMap<Integer, Bag<Integer>>();
        while (in.hasNextLine()) {
            String[] fields = in.readLine().split(",");
            int id = Integer.parseInt(fields[0]);            
            ids.remove(id);
            if (!map.containsKey(id))
                map.put(id, new Bag<Integer>());
            for (int i = 1; i < fields.length; i++) {
                int val = Integer.parseInt(fields[i]);
                map.get(id).add(val);
            }
        }
        return map;
    }
    private void buildWordDigraph(Digraph graph, Map<Integer, Bag<Integer>> map) {
        for (Map.Entry<Integer, Bag<Integer>> entry : map.entrySet()) {
            for (int val : entry.getValue())
                graph.addEdge(entry.getKey(), val);
        }
    }
    private void isRootedDAG() {
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle() || ids.size() > 1)
            throw new java.lang.IllegalArgumentException();
    }
    // the set of nouns (no duplicates), returned as an Iterable
    public Iterable<String> nouns() {
        return idMap.keySet();
    }
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) 
            throw new java.lang.NullPointerException();
        return idMap.containsKey(word);
    }
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        Bag<Integer> idA = idMap.get(nounA);
        Bag<Integer> idB = idMap.get(nounB);
        return sap.length(idA, idB);
    }
    // a synset (second field of synsets.txt) that is the common ancestor
    // of nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNouns(nounA, nounB);
        Bag<Integer> idA = idMap.get(nounA);
        Bag<Integer> idB = idMap.get(nounB);
        int idAn = sap.ancestor(idA, idB);
        return wordList.get(idAn);
    }
    private void checkNouns(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new java.lang.IllegalArgumentException();
    }
    // for unit testing of this class
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String u = StdIn.readString();
            String w = StdIn.readString();
            int length = wn.distance(u, w);
            StdOut.printf("length = %d\n", length);
        }
    }
}
