import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom High-Performance Swing Bar Chart Component.
 * Features:
 * - Anti-aliased 2D rendering with modern gradients and glow effects.
 * - Dynamic Y-axis auto-scaling with clean gridlines.
 * - Algorithm badges, Big-O complexity sub-labels, and value tags.
 * - Interactive hover tooltips highlighting bar values.
 */
public class BarChartPanel extends JPanel {

    public static class BarItem {
        public String label;
        public String subLabel;
        public double value;
        public String displayValue;
        public Color topColor;
        public Color bottomColor;

        public BarItem(String label, String subLabel, double value, String displayValue, Color baseColor) {
            this.label = label;
            this.subLabel = subLabel;
            this.value = Math.max(0, value);
            this.displayValue = displayValue != null ? displayValue : String.valueOf((long) value);
            this.topColor = lighten(baseColor, 0.25f);
            this.bottomColor = baseColor;
        }

        private static Color lighten(Color c, float factor) {
            int r = Math.min(255, (int) (c.getRed() + (255 - c.getRed()) * factor));
            int g = Math.min(255, (int) (c.getGreen() + (255 - c.getGreen()) * factor));
            int b = Math.min(255, (int) (c.getBlue() + (255 - c.getBlue()) * factor));
            return new Color(r, g, b);
        }
    }

    private final List<BarItem> items = new ArrayList<>();
    private String title = "Algorithm Benchmark";
    private String yAxisLabel = "Execution Time (ns)";
    private int hoveredIndex = -1;

    // Harmonious modern color palette
    public static final Color[] PALETTE = {
            new Color(14, 165, 233),   // Sky Blue
            new Color(16, 185, 129),   // Emerald Green
            new Color(245, 158, 11),   // Warm Amber
            new Color(139, 92, 246),   // Purple / Violet
            new Color(244, 63, 94),    // Rose Pink
            new Color(20, 184, 166),   // Teal
            new Color(99, 102, 241)    // Indigo
    };

    public BarChartPanel() {
        setBackground(new Color(17, 24, 39)); // Dark slate background (#111827)
        setPreferredSize(new Dimension(650, 320));

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int oldHover = hoveredIndex;
                hoveredIndex = getBarIndexAt(e.getX(), e.getY());
                if (oldHover != hoveredIndex) {
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredIndex != -1) {
                    hoveredIndex = -1;
                    repaint();
                }
            }
        });
    }

    public void setChartData(String chartTitle, String yUnit, List<BarItem> newItems) {
        this.title = chartTitle;
        this.yAxisLabel = yUnit;
        this.items.clear();
        if (newItems != null) {
            this.items.addAll(newItems);
        }
        this.hoveredIndex = -1;
        repaint();
    }

    public void clearChart() {
        this.items.clear();
        this.hoveredIndex = -1;
        repaint();
    }

    private int getBarIndexAt(int mouseX, int mouseY) {
        if (items.isEmpty()) return -1;

        int padLeft = 85;
        int padRight = 35;
        int padBottom = 65;
        int padTop = 60;

        int plotWidth = getWidth() - padLeft - padRight;
        int plotHeight = getHeight() - padTop - padBottom;
        if (plotWidth <= 0 || plotHeight <= 0) return -1;

        int n = items.size();
        int slotWidth = plotWidth / n;

        for (int i = 0; i < n; i++) {
            int slotX = padLeft + i * slotWidth;
            if (mouseX >= slotX && mouseX <= slotX + slotWidth && mouseY >= padTop && mouseY <= padTop + plotHeight) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 1. Draw Background Card Panel
        g2.setColor(new Color(17, 24, 39));
        g2.fillRect(0, 0, width, height);

        // 2. Draw Title & Y-Axis Label
        g2.setColor(new Color(243, 244, 246));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 15));
        g2.drawString(title, 20, 28);

        g2.setColor(new Color(156, 163, 175));
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString(yAxisLabel, 20, 48);

        if (items.isEmpty()) {
            drawEmptyState(g2, width, height);
            g2.dispose();
            return;
        }

        // Plot dimensions
        int padLeft = 85;
        int padRight = 35;
        int padTop = 65;
        int padBottom = 65;

        int plotWidth = width - padLeft - padRight;
        int plotHeight = height - padTop - padBottom;
        if (plotWidth <= 20 || plotHeight <= 20) {
            g2.dispose();
            return;
        }

        // Determine max value for Y-scaling
        double maxVal = 0;
        for (BarItem it : items) {
            if (it.value > maxVal) maxVal = it.value;
        }
        if (maxVal <= 0) maxVal = 1;

        // Round up maxVal to a clean ceiling
        double niceMax = getNiceCeiling(maxVal);

        // 3. Draw Horizontal Gridlines & Y-Axis Scale
        int gridSteps = 4;
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        DecimalFormat df = new DecimalFormat("#,###.##");

        for (int i = 0; i <= gridSteps; i++) {
            double tickVal = (niceMax / gridSteps) * i;
            int yPos = padTop + plotHeight - (int) ((tickVal / niceMax) * plotHeight);

            // Gridline
            g2.setColor(new Color(55, 65, 81, 140));
            g2.drawLine(padLeft, yPos, padLeft + plotWidth, yPos);

            // Y-tick label
            g2.setColor(new Color(156, 163, 175));
            String tickText = formatCompact(tickVal);
            FontMetrics fm = g2.getFontMetrics();
            int strW = fm.stringWidth(tickText);
            g2.drawString(tickText, padLeft - strW - 10, yPos + 4);
        }

        // 4. Draw Bars
        int n = items.size();
        int slotWidth = plotWidth / n;
        int barWidth = Math.max(18, Math.min(65, (int) (slotWidth * 0.58)));

        for (int i = 0; i < n; i++) {
            BarItem item = items.get(i);
            int slotCenterX = padLeft + i * slotWidth + slotWidth / 2;
            int barX = slotCenterX - barWidth / 2;

            int barHeight = (int) ((item.value / niceMax) * plotHeight);
            if (barHeight < 3 && item.value > 0) barHeight = 3;
            int barY = padTop + plotHeight - barHeight;

            boolean isHovered = (i == hoveredIndex);

            // Bar Gradient Fill
            Color cTop = isHovered ? BarItem.lighten(item.topColor, 0.2f) : item.topColor;
            Color cBottom = isHovered ? BarItem.lighten(item.bottomColor, 0.2f) : item.bottomColor;
            GradientPaint gp = new GradientPaint(barX, barY, cTop, barX, barY + barHeight, cBottom);
            g2.setPaint(gp);

            RoundRectangle2D barShape = new RoundRectangle2D.Double(barX, barY, barWidth, barHeight, 8, 8);
            g2.fill(barShape);

            // Bar Border Outline
            g2.setColor(isHovered ? Color.WHITE : new Color(255, 255, 255, 70));
            g2.setStroke(new BasicStroke(isHovered ? 2.0f : 1.0f));
            g2.draw(barShape);

            // 5. Draw Value Badge above bar
            String valStr = item.displayValue;
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            FontMetrics fmVal = g2.getFontMetrics();
            int valW = fmVal.stringWidth(valStr);
            int valX = slotCenterX - valW / 2;
            int valY = Math.max(padTop - 8, barY - 7);

            // Subtle value background tag if bars are crowded
            g2.setColor(new Color(30, 41, 59, 210));
            g2.fillRoundRect(valX - 4, valY - fmVal.getAscent(), valW + 8, fmVal.getHeight(), 6, 6);

            g2.setColor(isHovered ? new Color(253, 224, 71) : new Color(243, 244, 246));
            g2.drawString(valStr, valX, valY);

            // 6. Draw X-Axis Labels (Algorithm name + Big-O complexity badge)
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fmLbl = g2.getFontMetrics();
            int lblW = fmLbl.stringWidth(item.label);
            g2.setColor(isHovered ? Color.WHITE : new Color(229, 231, 235));
            g2.drawString(item.label, slotCenterX - lblW / 2, padTop + plotHeight + 20);

            // Sub-label (Complexity or metric note)
            if (item.subLabel != null && !item.subLabel.isEmpty()) {
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                FontMetrics fmSub = g2.getFontMetrics();
                int subW = fmSub.stringWidth(item.subLabel);
                g2.setColor(new Color(156, 163, 175));
                g2.drawString(item.subLabel, slotCenterX - subW / 2, padTop + plotHeight + 35);
            }
        }

        // Draw baseline
        g2.setColor(new Color(107, 114, 128));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(padLeft, padTop + plotHeight, padLeft + plotWidth, padTop + plotHeight);

        g2.dispose();
    }

    private void drawEmptyState(Graphics2D g2, int width, int height) {
        g2.setColor(new Color(156, 163, 175));
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        String msg = "No benchmark data yet. Click 'Run Benchmark & Compare' to generate bar chart.";
        FontMetrics fm = g2.getFontMetrics();
        int msgW = fm.stringWidth(msg);
        g2.drawString(msg, (width - msgW) / 2, height / 2 + 10);
    }

    private static double getNiceCeiling(double max) {
        if (max <= 0) return 10;
        double magnitude = Math.pow(10, Math.floor(Math.log10(max)));
        double normalized = max / magnitude;
        double nice;
        if (normalized <= 1.0) nice = 1.0;
        else if (normalized <= 2.0) nice = 2.0;
        else if (normalized <= 2.5) nice = 2.5;
        else if (normalized <= 5.0) nice = 5.0;
        else nice = 10.0;
        return nice * magnitude * 1.15; // 15% breathing room on top
    }

    private static String formatCompact(double val) {
        if (val >= 1_000_000_000) {
            return String.format("%.1fB", val / 1_000_000_000.0);
        } else if (val >= 1_000_000) {
            return String.format("%.1fM", val / 1_000_000.0);
        } else if (val >= 1_000) {
            return String.format("%.1fk", val / 1_000.0);
        } else if (val == (long) val) {
            return String.valueOf((long) val);
        } else {
            return String.format("%.1f", val);
        }
    }
}
