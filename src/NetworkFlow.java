import java.util.*;

/**
 * NetworkFlow Engine for LifeData.
 * Implements:
 * - Ford-Fulkerson (DFS)
 * - Edmonds-Karp (BFS)
 * - Dinic's Algorithm (Level Graph + Blocking Flow)
 * - Bipartite Matching (Task to Resource allocation)
 * - Minimum Cut (Max-Flow Min-Cut Theorem)
 */
public class NetworkFlow {

    public static class Node {
        public final int id;
        public final String name;
        public int x, y; // visual canvas coordinates

        public Node(int id, String name, int x, int y) {
            this.id = id;
            this.name = name;
            this.x = x;
            this.y = y;
        }

        @Override public String toString() { return name; }
    }

    public static class Edge {
        public final int u, v;
        public final int capacity;
        public int flow;
        public Edge residual;

        public Edge(int u, int v, int capacity) {
            this.u = u;
            this.v = v;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int remainingCapacity() {
            return capacity - flow;
        }

        public void augment(int bottleneck) {
            flow += bottleneck;
            residual.flow -= bottleneck;
        }

        @Override public String toString() {
            return u + "->" + v + " (" + flow + "/" + capacity + ")";
        }
    }

    public static class FlowResult {
        public final String algorithm;
        public final int maxFlow;
        public final long executionTimeNs;
        public final int augmentingPathsCount;
        public final List<String> pathHistory;
        public final List<Edge> minCutEdges;
        public final Set<Integer> sourceReachableSet;

        public FlowResult(String algorithm, int maxFlow, long executionTimeNs, 
                          int augmentingPathsCount, List<String> pathHistory, 
                          List<Edge> minCutEdges, Set<Integer> sourceReachableSet) {
            this.algorithm = algorithm;
            this.maxFlow = maxFlow;
            this.executionTimeNs = executionTimeNs;
            this.augmentingPathsCount = augmentingPathsCount;
            this.pathHistory = pathHistory;
            this.minCutEdges = minCutEdges;
            this.sourceReachableSet = sourceReachableSet;
        }
    }

    public static class FlowGraph {
        public final List<Node> nodes = new ArrayList<>();
        public final List<Edge> edges = new ArrayList<>();
        public final Map<Integer, List<Edge>> adj = new HashMap<>();

        public void addNode(int id, String name, int x, int y) {
            nodes.add(new Node(id, name, x, y));
            adj.putIfAbsent(id, new ArrayList<>());
        }

        public void addEdge(int u, int v, int capacity) {
            Edge e1 = new Edge(u, v, capacity);
            Edge e2 = new Edge(v, u, 0); // residual back-edge
            e1.residual = e2;
            e2.residual = e1;
            edges.add(e1);
            adj.computeIfAbsent(u, k -> new ArrayList<>()).add(e1);
            adj.computeIfAbsent(v, k -> new ArrayList<>()).add(e2);
        }

        public void resetFlow() {
            for (Edge e : edges) {
                e.flow = 0;
                if (e.residual != null) e.residual.flow = 0;
            }
        }
    }

    // =========================================================================
    // 1. FORD-FULKERSON (DFS Augmentation)
    // =========================================================================
    public static FlowResult solveFordFulkerson(FlowGraph g, int s, int t) {
        g.resetFlow();
        long start = System.nanoTime();
        int maxFlow = 0;
        int paths = 0;
        List<String> history = new ArrayList<>();

        while (true) {
            boolean[] visited = new boolean[g.nodes.size() + 20];
            List<Edge> path = new ArrayList<>();
            int bottleneck = dfs(g, s, t, Integer.MAX_VALUE, visited, path);
            if (bottleneck <= 0) break;

            maxFlow += bottleneck;
            paths++;
            StringBuilder sb = new StringBuilder("Path: ");
            for (int i = 0; i < path.size(); i++) {
                sb.append(g.nodes.get(path.get(i).u).name).append(" -> ");
                if (i == path.size() - 1) sb.append(g.nodes.get(path.get(i).v).name);
            }
            sb.append(" [Bottleneck: ").append(bottleneck).append("]");
            history.add(sb.toString());
        }

        long time = System.nanoTime() - start;
        Set<Integer> reachable = findReachableSet(g, s);
        List<Edge> minCut = findMinCutEdges(g, reachable);
        return new FlowResult("Ford-Fulkerson (DFS)", maxFlow, time, paths, history, minCut, reachable);
    }

    private static int dfs(FlowGraph g, int u, int t, int minCap, boolean[] visited, List<Edge> path) {
        if (u == t) return minCap;
        visited[u] = true;
        for (Edge e : g.adj.getOrDefault(u, Collections.emptyList())) {
            if (!visited[e.v] && e.remainingCapacity() > 0) {
                path.add(e);
                int pushed = dfs(g, e.v, t, Math.min(minCap, e.remainingCapacity()), visited, path);
                if (pushed > 0) {
                    e.augment(pushed);
                    return pushed;
                }
                path.remove(path.size() - 1);
            }
        }
        return 0;
    }

    // =========================================================================
    // 2. EDMONDS-KARP (BFS Shortest Augmenting Path, O(V * E^2))
    // =========================================================================
    public static FlowResult solveEdmondsKarp(FlowGraph g, int s, int t) {
        g.resetFlow();
        long start = System.nanoTime();
        int maxFlow = 0;
        int paths = 0;
        List<String> history = new ArrayList<>();

        while (true) {
            Edge[] parentEdge = new Edge[g.nodes.size() + 20];
            Queue<Integer> q = new ArrayDeque<>();
            q.add(s);

            while (!q.isEmpty() && parentEdge[t] == null) {
                int curr = q.poll();
                for (Edge e : g.adj.getOrDefault(curr, Collections.emptyList())) {
                    if (e.v != s && parentEdge[e.v] == null && e.remainingCapacity() > 0) {
                        parentEdge[e.v] = e;
                        q.add(e.v);
                    }
                }
            }

            if (parentEdge[t] == null) break; // no augmenting path found

            int bottleneck = Integer.MAX_VALUE;
            for (Edge e = parentEdge[t]; e != null; e = parentEdge[e.u]) {
                bottleneck = Math.min(bottleneck, e.remainingCapacity());
            }

            List<String> pathNodes = new ArrayList<>();
            for (Edge e = parentEdge[t]; e != null; e = parentEdge[e.u]) {
                e.augment(bottleneck);
                pathNodes.add(g.nodes.get(e.v).name);
            }
            pathNodes.add(g.nodes.get(s).name);
            Collections.reverse(pathNodes);

            maxFlow += bottleneck;
            paths++;
            history.add("Path: " + String.join(" -> ", pathNodes) + " [Bottleneck: " + bottleneck + "]");
        }

        long time = System.nanoTime() - start;
        Set<Integer> reachable = findReachableSet(g, s);
        List<Edge> minCut = findMinCutEdges(g, reachable);
        return new FlowResult("Edmonds-Karp (BFS)", maxFlow, time, paths, history, minCut, reachable);
    }

    // =========================================================================
    // 3. DINIC'S ALGORITHM (Level Graph + Blocking Flow, O(V^2 * E))
    // =========================================================================
    public static FlowResult solveDinic(FlowGraph g, int s, int t) {
        g.resetFlow();
        long start = System.nanoTime();
        int maxFlow = 0;
        int phases = 0;
        List<String> history = new ArrayList<>();

        int[] level = new int[g.nodes.size() + 20];
        while (bfsLevelGraph(g, s, t, level)) {
            phases++;
            int[] ptr = new int[g.nodes.size() + 20];
            int phaseFlow = 0;
            while (true) {
                int pushed = dfsDinic(g, s, t, Integer.MAX_VALUE, level, ptr);
                if (pushed <= 0) break;
                phaseFlow += pushed;
            }
            maxFlow += phaseFlow;
            history.add("Phase " + phases + " Level Graph: Pushed " + phaseFlow + " blocking flow units.");
            if (phaseFlow == 0) break;
        }

        long time = System.nanoTime() - start;
        Set<Integer> reachable = findReachableSet(g, s);
        List<Edge> minCut = findMinCutEdges(g, reachable);
        return new FlowResult("Dinic's Algorithm", maxFlow, time, phases, history, minCut, reachable);
    }

    private static boolean bfsLevelGraph(FlowGraph g, int s, int t, int[] level) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (Edge e : g.adj.getOrDefault(u, Collections.emptyList())) {
                if (e.remainingCapacity() > 0 && level[e.v] < 0) {
                    level[e.v] = level[u] + 1;
                    q.add(e.v);
                }
            }
        }
        return level[t] >= 0;
    }

    private static int dfsDinic(FlowGraph g, int u, int t, int pushed, int[] level, int[] ptr) {
        if (pushed == 0 || u == t) return pushed;
        List<Edge> edges = g.adj.getOrDefault(u, Collections.emptyList());
        for (int cid = ptr[u]; cid < edges.size(); cid++, ptr[u]++) {
            Edge e = edges.get(cid);
            int v = e.v;
            if (level[u] + 1 != level[v] || e.remainingCapacity() == 0) continue;
            int tr = dfsDinic(g, v, t, Math.min(pushed, e.remainingCapacity()), level, ptr);
            if (tr == 0) continue;
            e.augment(tr);
            return tr;
        }
        return 0;
    }

    // =========================================================================
    // 4. BIPARTITE MATCHING (Tasks to Computing Resources)
    // =========================================================================
    public static FlowGraph createBipartiteMatchingDemo(List<String> tasks, List<String> resources, boolean[][] compatible) {
        FlowGraph g = new FlowGraph();
        int s = 0;
        g.addNode(s, "Source", 50, 200);

        int t = 1 + tasks.size() + resources.size();
        for (int i = 0; i < tasks.size(); i++) {
            int id = 1 + i;
            int y = 60 + i * 55;
            g.addNode(id, tasks.get(i), 220, y);
            g.addEdge(s, id, 1);
        }

        for (int j = 0; j < resources.size(); j++) {
            int id = 1 + tasks.size() + j;
            int y = 60 + j * 55;
            g.addNode(id, resources.get(j), 480, y);
            g.addEdge(id, t, 1);
        }

        g.addNode(t, "Sink", 650, 200);

        for (int i = 0; i < tasks.size(); i++) {
            for (int j = 0; j < resources.size(); j++) {
                if (compatible[i][j]) {
                    int u = 1 + i;
                    int v = 1 + tasks.size() + j;
                    g.addEdge(u, v, 1);
                }
            }
        }
        return g;
    }

    public static FlowGraph createDefaultSensorPipelineNetwork() {
        FlowGraph g = new FlowGraph();
        // S(0) -> Preprocessing A(1), B(2) -> Aggregation C(3), D(4) -> Analytics Sink T(5)
        g.addNode(0, "Raw Stream (S)", 60, 180);
        g.addNode(1, "Filter A", 200, 100);
        g.addNode(2, "Filter B", 200, 260);
        g.addNode(3, "Aggregator C", 400, 100);
        g.addNode(4, "Aggregator D", 400, 260);
        g.addNode(5, "Database (T)", 550, 180);

        g.addEdge(0, 1, 10);
        g.addEdge(0, 2, 8);
        g.addEdge(1, 2, 4);
        g.addEdge(1, 3, 7);
        g.addEdge(2, 4, 11);
        g.addEdge(3, 4, 3);
        g.addEdge(3, 5, 8);
        g.addEdge(4, 5, 12);
        return g;
    }

    private static Set<Integer> findReachableSet(FlowGraph g, int s) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> q = new ArrayDeque<>();
        q.add(s);
        visited.add(s);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (Edge e : g.adj.getOrDefault(u, Collections.emptyList())) {
                if (e.remainingCapacity() > 0 && !visited.contains(e.v)) {
                    visited.add(e.v);
                    q.add(e.v);
                }
            }
        }
        return visited;
    }

    private static List<Edge> findMinCutEdges(FlowGraph g, Set<Integer> sourceReachable) {
        List<Edge> minCut = new ArrayList<>();
        for (Edge e : g.edges) {
            if (e.capacity > 0 && sourceReachable.contains(e.u) && !sourceReachable.contains(e.v)) {
                minCut.add(e);
            }
        }
        return minCut;
    }
}
