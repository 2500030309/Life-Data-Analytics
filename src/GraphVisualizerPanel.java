import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * GraphVisualizerPanel provides custom 2D Swing canvas rendering
 * for Network Flow graphs, Bipartite Matching, 3-SAT to CLIQUE reductions,
 * and Vertex Cover solutions.
 */
public class GraphVisualizerPanel extends JPanel {

    public enum Mode { NETWORK_FLOW, REDUCTION_GRAPH }

    private Mode currentMode = Mode.NETWORK_FLOW;
    private NetworkFlow.FlowGraph flowGraph;
    private NetworkFlow.FlowResult flowResult;

    private ConstraintOptimization.ReductionGraph reductionGraph;
    private List<Integer> highlightedNodeSet = new ArrayList<>();
    private String graphTitle = "Interactive Graph Canvas";

    // Colors
    private static final Color BG_CANVAS = new Color(15, 23, 42); // slate-900
    private static final Color NODE_COLOR = new Color(30, 41, 59);
    private static final Color NODE_BORDER = new Color(56, 189, 248); // sky-400
    private static final Color NODE_TEXT = new Color(248, 250, 252);
    private static final Color EDGE_DEFAULT = new Color(71, 85, 105);
    private static final Color EDGE_FLOW = new Color(14, 165, 233); // cyan
    private static final Color EDGE_SATURATED = new Color(244, 63, 94); // rose
    private static final Color EDGE_CUT = new Color(234, 179, 8); // amber
    private static final Color HIGHLIGHT_NODE = new Color(139, 92, 246); // purple

    public GraphVisualizerPanel() {
        setBackground(BG_CANVAS);
        setPreferredSize(new Dimension(600, 380));
    }

    public void setFlowGraph(NetworkFlow.FlowGraph g, NetworkFlow.FlowResult res, String title) {
        this.currentMode = Mode.NETWORK_FLOW;
        this.flowGraph = g;
        this.flowResult = res;
        this.graphTitle = title;
        repaint();
    }

    public void setReductionGraph(ConstraintOptimization.ReductionGraph g, List<Integer> highlightedNodes, String title) {
        this.currentMode = Mode.REDUCTION_GRAPH;
        this.reductionGraph = g;
        this.highlightedNodeSet = (highlightedNodes != null) ? highlightedNodes : Collections.emptyList();
        this.graphTitle = title;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background subtle grid
        drawSubtleGrid(g2);

        // Header Title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(new Color(226, 232, 240));
        g2.drawString(graphTitle, 16, 26);

        if (currentMode == Mode.NETWORK_FLOW) {
            paintFlowGraph(g2);
        } else {
            paintReductionGraph(g2);
        }
    }

    private void drawSubtleGrid(Graphics2D g2) {
        g2.setColor(new Color(30, 41, 59, 120));
        int w = getWidth(), h = getHeight();
        for (int x = 0; x < w; x += 40) g2.drawLine(x, 0, x, h);
        for (int y = 0; y < h; y += 40) g2.drawLine(0, y, w, y);
    }

    private void paintFlowGraph(Graphics2D g2) {
        if (flowGraph == null || flowGraph.nodes.isEmpty()) {
            drawEmptyMessage(g2, "No flow graph loaded. Run Max-Flow or Bipartite Matching to visualize.");
            return;
        }

        // 1. Draw Edges
        Set<NetworkFlow.Edge> cutEdges = new HashSet<>();
        if (flowResult != null && flowResult.minCutEdges != null) {
            cutEdges.addAll(flowResult.minCutEdges);
        }

        for (NetworkFlow.Edge e : flowGraph.edges) {
            if (e.capacity == 0) continue; // skip residual back-edge
            NetworkFlow.Node u = flowGraph.nodes.get(e.u);
            NetworkFlow.Node v = flowGraph.nodes.get(e.v);

            boolean isCut = cutEdges.contains(e);
            boolean isSaturated = (e.flow == e.capacity && e.capacity > 0);
            boolean hasFlow = (e.flow > 0);

            if (isCut) {
                g2.setColor(EDGE_CUT);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[]{6f, 4f}, 0f));
            } else if (isSaturated) {
                g2.setColor(EDGE_SATURATED);
                g2.setStroke(new BasicStroke(2.2f));
            } else if (hasFlow) {
                g2.setColor(EDGE_FLOW);
                g2.setStroke(new BasicStroke(2.0f));
            } else {
                g2.setColor(EDGE_DEFAULT);
                g2.setStroke(new BasicStroke(1.2f));
            }

            drawArrow(g2, u.x, u.y, v.x, v.y, 18);

            // Flow / Capacity Label
            int midX = (u.x + v.x) / 2;
            int midY = (u.y + v.y) / 2 - 4;
            String label = e.flow + "/" + e.capacity;
            g2.setFont(new Font("Consolas", Font.BOLD, 11));
            g2.setColor(isCut ? EDGE_CUT : (hasFlow ? Color.WHITE : new Color(148, 163, 184)));
            g2.drawString(label, midX - 10, midY);
        }

        // 2. Draw Nodes
        for (NetworkFlow.Node n : flowGraph.nodes) {
            boolean inSourceReachable = (flowResult != null && flowResult.sourceReachableSet.contains(n.id));

            int r = 18;
            g2.setColor(inSourceReachable ? new Color(14, 165, 233, 180) : NODE_COLOR);
            g2.fillOval(n.x - r, n.y - r, r * 2, r * 2);

            g2.setColor(inSourceReachable ? Color.WHITE : NODE_BORDER);
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawOval(n.x - r, n.y - r, r * 2, r * 2);

            // Label
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            FontMetrics fm = g2.getFontMetrics();
            int tx = n.x - fm.stringWidth(n.name) / 2;
            int ty = n.y + r + 14;

            g2.setColor(NODE_TEXT);
            g2.drawString(n.name, tx, ty);
        }

        // Legend
        drawLegend(g2, new String[]{"Normal Capacity", "Active Flow", "Saturated Edge", "Min-Cut Boundary"}, 
                        new Color[]{EDGE_DEFAULT, EDGE_FLOW, EDGE_SATURATED, EDGE_CUT});
    }

    private void paintReductionGraph(Graphics2D g2) {
        if (reductionGraph == null || reductionGraph.nodes.isEmpty()) {
            drawEmptyMessage(g2, "No reduction graph constructed. Run 3-SAT to CLIQUE reduction.");
            return;
        }

        // 1. Draw Edges
        g2.setColor(new Color(51, 65, 85, 180));
        g2.setStroke(new BasicStroke(1.0f));
        int n = reductionGraph.n;
        for (int i = 0; i < n; i++) {
            ConstraintOptimization.GraphNode u = reductionGraph.nodes.get(i);
            for (int j = i + 1; j < n; j++) {
                if (reductionGraph.adj[i][j]) {
                    ConstraintOptimization.GraphNode v = reductionGraph.nodes.get(j);
                    g2.drawLine(u.x, u.y, v.x, v.y);
                }
            }
        }

        // 2. Draw Nodes
        Set<Integer> highlightSet = new HashSet<>(highlightedNodeSet);
        for (int i = 0; i < n; i++) {
            ConstraintOptimization.GraphNode node = reductionGraph.nodes.get(i);
            boolean isHighlighted = highlightSet.contains(i);

            int r = 16;
            g2.setColor(isHighlighted ? HIGHLIGHT_NODE : NODE_COLOR);
            g2.fillOval(node.x - r, node.y - r, r * 2, r * 2);

            g2.setColor(isHighlighted ? new Color(244, 63, 94) : NODE_BORDER);
            g2.setStroke(new BasicStroke(isHighlighted ? 2.5f : 1.5f));
            g2.drawOval(node.x - r, node.y - r, r * 2, r * 2);

            // Node Text
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            FontMetrics fm = g2.getFontMetrics();
            int tx = node.x - fm.stringWidth(node.label) / 2;
            int ty = node.y + 4;
            g2.setColor(Color.WHITE);
            g2.drawString(node.label, tx, ty);
        }

        drawLegend(g2, new String[]{"Graph Edge", "Cover / Clique Vertex"}, new Color[]{EDGE_DEFAULT, HIGHLIGHT_NODE});
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2, int nodeRadius) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int sx = x1 + (int) (nodeRadius * Math.cos(angle));
        int sy = y1 + (int) (nodeRadius * Math.sin(angle));
        int ex = x2 - (int) (nodeRadius * Math.cos(angle));
        int ey = y2 - (int) (nodeRadius * Math.sin(angle));

        g2.drawLine(sx, sy, ex, ey);

        // Arrow head
        int arrowSize = 6;
        int xA = ex - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int yA = ey - (int) (arrowSize * Math.sin(angle - Math.PI / 6));
        int xB = ex - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int yB = ey - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        Polygon arrow = new Polygon();
        arrow.addPoint(ex, ey);
        arrow.addPoint(xA, yA);
        arrow.addPoint(xB, yB);
        g2.fillPolygon(arrow);
    }

    private void drawLegend(Graphics2D g2, String[] items, Color[] colors) {
        int x = 16;
        int y = getHeight() - 16;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        for (int i = 0; i < items.length; i++) {
            g2.setColor(colors[i]);
            g2.fillRect(x, y - 8, 10, 10);
            g2.setColor(new Color(203, 213, 225));
            g2.drawString(items[i], x + 14, y);
            x += g2.getFontMetrics().stringWidth(items[i]) + 26;
        }
    }

    private void drawEmptyMessage(Graphics2D g2, String msg) {
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        g2.setColor(new Color(148, 163, 184));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }
}
