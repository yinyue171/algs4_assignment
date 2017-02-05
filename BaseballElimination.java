import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private int[] w, l, r;
    private int[][] games;
    private Map<String, Integer> teamMap;
    private int maxWins = -1;
    private String leadingTeam;    
    private static final double MAX_EDGE = Double.POSITIVE_INFINITY; 

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int numOfTeams = in.readInt();
        w = new int[numOfTeams];
        l = new int[numOfTeams];
        r = new int[numOfTeams];
        games = new int[numOfTeams][numOfTeams];
        teamMap = new HashMap<String, Integer>();
        for (int i = 0; i < numOfTeams; i++) {
            String curTeam = in.readString();
            teamMap.put(curTeam, i);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < numOfTeams; j++) 
                games[i][j] = in.readInt();            
            if (w[i] > maxWins) {
                maxWins = w[i];
                leadingTeam = curTeam;                
            }
        }
    }
    public int numberOfTeams() {
        return teamMap.size();
    }
    public Iterable<String> teams() {
        return teamMap.keySet();
    }
    public int wins(String team) {
        checkValidTeam(team);
        return w[teamMap.get(team)];
    }
    public int losses(String team) {
        checkValidTeam(team);
        return l[teamMap.get(team)];
    }
    public int remaining(String team) {
        checkValidTeam(team);
        return r[teamMap.get(team)];
    }
    public int against(String team1, String team2) {
        checkValidTeam(team1);
        checkValidTeam(team2);
        return games[teamMap.get(team1)][teamMap.get(team2)];
    }
    private void checkValidTeam(String team) {  
        if (!teamMap.containsKey(team))
            throw new java.lang.IllegalArgumentException();
    }
    public boolean isEliminated(String team) {
        checkValidTeam(team);
        int id = teamMap.get(team);
        if (trvialEnd(id)) 
            return true;        
        spGraph g = buildGraph(id);        
        for (FlowEdge edge : g.flowNetwork.adj(g.s)) {
            if (edge.flow() < edge.capacity()) 
                return true;            
        }
        return false;
    }
    private boolean trvialEnd(int id) {
        return w[id] + r[id] < maxWins;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkValidTeam(team);
        Set<String> set = new HashSet<String>();
        if (trvialEnd(teamMap.get(team))) {
            set.add(leadingTeam);
            return set;
        }
        spGraph g = buildGraph(teamMap.get(team));
        for (FlowEdge edge : g.flowNetwork.adj(g.s)) {
            if (edge.flow() < edge.capacity()) {
                for (String t : teams()) {
                    int id = teamMap.get(t);
                    if (g.ff.inCut(id)) {
                        set.add(t);
                    }
                }
            }
        }  
        if (set.isEmpty())
            set = null;
        return set;
    }
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private spGraph buildGraph(int id) {
        int n = numberOfTeams();
        int source = n;
        int sink = n + 1;
        int gameVertice = n + 2;
        int curMaxWins = w[id] + r[id];
        Set<FlowEdge> edges = new HashSet<FlowEdge>();
        for (int i = 0; i < n; i++) {
            if (i == id || trvialEnd(i)) 
                continue;            
            for (int j = 0; j < i; j++) {
                if (j == id || trvialEnd(j) || games[i][j] == 0)
                    continue;                
                edges.add(new FlowEdge(source, gameVertice, games[i][j]));
                edges.add(new FlowEdge(gameVertice, i, MAX_EDGE));
                edges.add(new FlowEdge(gameVertice, j, MAX_EDGE));
                gameVertice++;
            }
            edges.add(new FlowEdge(i, sink, curMaxWins - w[i]));
        }
        FlowNetwork flowNetwork = new FlowNetwork(gameVertice);
        for (FlowEdge edge : edges) 
            flowNetwork.addEdge(edge);        
        FordFulkerson ff = new FordFulkerson(flowNetwork, source, sink);
        return new spGraph(ff, flowNetwork, source, sink);
    }

    private class spGraph {
        FordFulkerson ff;
        FlowNetwork flowNetwork;
        int s;

        public spGraph(FordFulkerson ff, FlowNetwork network, int source, int sink) {
            super();
            this.ff = ff;
            this.flowNetwork = network;
            this.s = source;            
        }
    }
}