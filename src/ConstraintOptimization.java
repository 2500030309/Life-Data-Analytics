import java.util.*;

/**
 * ConstraintOptimization Engine for LifeData.
 * Implements:
 * - 3-SAT Exact Solver (DPLL / Bounded Backtracking)
 * - 3-SAT to CLIQUE Reduction (Karp Gadget Construction)
 * - CLIQUE to INDEPENDENT SET Reduction (Complement Graph)
 * - INDEPENDENT SET to VERTEX COVER Relation (VC = V \ IS)
 * - Vertex-Cover 2-Approximation (Maximal Matching Heuristic)
 */
public class ConstraintOptimization {

    public static class Literal {
        public final int var; // 1-indexed (e.g. 1 for x1)
        public final boolean negated;

        public Literal(int var, boolean negated) {
            this.var = var;
            this.negated = negated;
        }

        @Override public String toString() {
            return (negated ? "¬x" : "x") + var;
        }
    }

    public static class Clause {
        public final List<Literal> literals;

        public Clause(Literal... lits) {
            this.literals = Arrays.asList(lits);
        }

        public Clause(List<Literal> lits) {
            this.literals = lits;
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < literals.size(); i++) {
                sb.append(literals.get(i).toString());
                if (i < literals.size() - 1) sb.append(" ∨ ");
            }
            return sb.append(")").toString();
        }
    }

    public static class SatFormula {
        public final int varCount;
        public final List<Clause> clauses = new ArrayList<>();

        public SatFormula(int varCount) {
            this.varCount = varCount;
        }

        public void addClause(Literal... lits) {
            clauses.add(new Clause(lits));
        }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < clauses.size(); i++) {
                sb.append(clauses.get(i).toString());
                if (i < clauses.size() - 1) sb.append(" ∧ ");
            }
            return sb.toString();
        }
    }

    public static class SatResult {
        public final boolean satisfiable;
        public final Map<Integer, Boolean> assignment;
        public final long executionTimeNs;
        public final int stepsExplored;

        public SatResult(boolean satisfiable, Map<Integer, Boolean> assignment, long time, int steps) {
            this.satisfiable = satisfiable;
            this.assignment = assignment;
            this.executionTimeNs = time;
            this.stepsExplored = steps;
        }
    }

    // =========================================================================
    // 1. EXACT DPLL / BACKTRACKING 3-SAT SOLVER
    // =========================================================================
    public static SatResult solve3Sat(SatFormula formula) {
        long start = System.nanoTime();
        int[] steps = new int[]{0};
        Map<Integer, Boolean> assignment = new HashMap<>();
        boolean sat = dpll(formula, 1, assignment, steps);
        long time = System.nanoTime() - start;
        return new SatResult(sat, sat ? assignment : Collections.emptyMap(), time, steps[0]);
    }

    private static boolean dpll(SatFormula formula, int varIdx, Map<Integer, Boolean> assignment, int[] steps) {
        steps[0]++;
        if (isFormulaSatisfied(formula, assignment)) return true;
        if (isFormulaConflicted(formula, assignment)) return false;
        if (varIdx > formula.varCount) return isFormulaSatisfied(formula, assignment);

        // Try TRUE
        assignment.put(varIdx, true);
        if (dpll(formula, varIdx + 1, assignment, steps)) return true;

        // Try FALSE
        assignment.put(varIdx, false);
        if (dpll(formula, varIdx + 1, assignment, steps)) return true;

        assignment.remove(varIdx);
        return false;
    }

    private static boolean isFormulaSatisfied(SatFormula formula, Map<Integer, Boolean> assignment) {
        for (Clause c : formula.clauses) {
            boolean clauseTrue = false;
            for (Literal l : c.literals) {
                if (assignment.containsKey(l.var)) {
                    boolean val = assignment.get(l.var);
                    if (l.negated ? !val : val) {
                        clauseTrue = true;
                        break;
                    }
                }
            }
            if (!clauseTrue) return false;
        }
        return true;
    }

    private static boolean isFormulaConflicted(SatFormula formula, Map<Integer, Boolean> assignment) {
        for (Clause c : formula.clauses) {
            boolean allAssigned = true;
            boolean clauseTrue = false;
            for (Literal l : c.literals) {
                if (!assignment.containsKey(l.var)) {
                    allAssigned = false;
                } else {
                    boolean val = assignment.get(l.var);
                    if (l.negated ? !val : val) clauseTrue = true;
                }
            }
            if (allAssigned && !clauseTrue) return true;
        }
        return false;
    }

    // =========================================================================
    // 2. REDUCTION GRAPH STRUCTURE & KARP GADGETS
    // =========================================================================
    public static class GraphNode {
        public final int id;
        public final String label;
        public final int clauseIdx;
        public final Literal literal;
        public int x, y;

        public GraphNode(int id, String label, int clauseIdx, Literal literal, int x, int y) {
            this.id = id;
            this.label = label;
            this.clauseIdx = clauseIdx;
            this.literal = literal;
            this.x = x;
            this.y = y;
        }

        @Override public String toString() { return label; }
    }

    public static class ReductionGraph {
        public final List<GraphNode> nodes = new ArrayList<>();
        public final boolean[][] adj;
        public final int n;

        public ReductionGraph(int n) {
            this.n = n;
            this.adj = new boolean[n][n];
        }

        public void addEdge(int u, int v) {
            if (u >= 0 && u < n && v >= 0 && v < n && u != v) {
                adj[u][v] = true;
                adj[v][u] = true;
            }
        }

        public int edgeCount() {
            int count = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (adj[i][j]) count++;
                }
            }
            return count;
        }
    }

    /**
     * Standard Karp Reduction: 3-SAT to CLIQUE
     * Each clause creates a 3-literal triangle gadget.
     * Edges connect literals in DIFFERENT clauses that are NOT contradictory.
     * Target Clique size k = number of clauses m.
     */
    public static ReductionGraph reduce3SatToClique(SatFormula formula) {
        int m = formula.clauses.size();
        int totalNodes = m * 3;
        ReductionGraph g = new ReductionGraph(totalNodes);

        int nodeId = 0;
        int radius = 180;
        int centerX = 260, centerY = 200;

        for (int c = 0; c < m; c++) {
            Clause cl = formula.clauses.get(c);
            double baseAngle = (2 * Math.PI * c) / m;
            for (int j = 0; j < 3; j++) {
                Literal lit = (j < cl.literals.size()) ? cl.literals.get(j) : new Literal(1, false);
                double angle = baseAngle + (j - 1) * 0.35;
                int x = centerX + (int) (radius * Math.cos(angle));
                int y = centerY + (int) (radius * Math.sin(angle));
                g.nodes.add(new GraphNode(nodeId++, "C" + (c + 1) + ":" + lit.toString(), c, lit, x, y));
            }
        }

        for (int i = 0; i < totalNodes; i++) {
            GraphNode u = g.nodes.get(i);
            for (int j = i + 1; j < totalNodes; j++) {
                GraphNode v = g.nodes.get(j);
                if (u.clauseIdx != v.clauseIdx) {
                    boolean contradictory = (u.literal.var == v.literal.var && u.literal.negated != v.literal.negated);
                    if (!contradictory) {
                        g.addEdge(i, j);
                    }
                }
            }
        }
        return g;
    }

    /**
     * CLIQUE to INDEPENDENT SET Reduction via Graph Complement
     */
    public static ReductionGraph computeGraphComplement(ReductionGraph orig) {
        ReductionGraph comp = new ReductionGraph(orig.n);
        comp.nodes.addAll(orig.nodes);
        for (int i = 0; i < orig.n; i++) {
            for (int j = i + 1; j < orig.n; j++) {
                if (!orig.adj[i][j]) {
                    comp.addEdge(i, j);
                }
            }
        }
        return comp;
    }

    /**
     * Vertex Cover 2-Approximation via Maximal Matching
     */
    public static List<Integer> solveVertexCover2Approx(ReductionGraph g) {
        List<Integer> vc = new ArrayList<>();
        boolean[] matched = new boolean[g.n];

        for (int i = 0; i < g.n; i++) {
            if (matched[i]) continue;
            for (int j = i + 1; j < g.n; j++) {
                if (!matched[j] && g.adj[i][j]) {
                    matched[i] = true;
                    matched[j] = true;
                    vc.add(i);
                    vc.add(j);
                    break;
                }
            }
        }
        Collections.sort(vc);
        return vc;
    }

    /**
     * Exact Minimum Vertex Cover via Maximum Independent Set (brute-force on small bounded reduction graph)
     */
    public static List<Integer> solveExactMinVertexCover(ReductionGraph g) {
        int n = Math.min(g.n, 18); // safety bound for fast GUI calculation
        List<Integer> bestVC = new ArrayList<>();
        for (int i = 0; i < n; i++) bestVC.add(i);

        for (int mask = 0; mask < (1 << n); mask++) {
            boolean validCover = true;
            for (int u = 0; u < n; u++) {
                for (int v = u + 1; v < n; v++) {
                    if (g.adj[u][v]) {
                        boolean uInCover = (mask & (1 << u)) != 0;
                        boolean vInCover = (mask & (1 << v)) != 0;
                        if (!uInCover && !vInCover) {
                            validCover = false;
                            break;
                        }
                    }
                }
                if (!validCover) break;
            }

            if (validCover) {
                int count = Integer.bitCount(mask);
                if (count < bestVC.size()) {
                    bestVC.clear();
                    for (int i = 0; i < n; i++) {
                        if ((mask & (1 << i)) != 0) bestVC.add(i);
                    }
                }
            }
        }
        return bestVC;
    }

    public static SatFormula createSampleFormula() {
        // (x1 ∨ ¬x2 ∨ x3) ∧ (¬x1 ∨ x2 ∨ x3) ∧ (¬x1 ∨ ¬x2 ∨ ¬x3)
        SatFormula f = new SatFormula(3);
        f.addClause(new Literal(1, false), new Literal(2, true), new Literal(3, false));
        f.addClause(new Literal(1, true), new Literal(2, false), new Literal(3, false));
        f.addClause(new Literal(1, true), new Literal(2, true), new Literal(3, true));
        return f;
    }
}
