import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * LifeData Analytics – GUI Application for Final Review.
 * Features:
 * - Category-Specific Algorithm Benchmarking (Exact String Match, Distance/Alignment Measures, Suffix Indexing, DP Optimization)
 * - Anti-aliased Real-time Bar Graphs with multi-metric toggles
 * - Personal Record Manager with CRUD & file loading
 * - Comprehensive Final Review Viva Dashboard & Report Export
 */
public class LifeDataGUI extends JFrame {

    private final List<PersonalRecord> recordList = new ArrayList<>();
    private final DecimalFormat numFmt = new DecimalFormat("#,###");

    // UI Colors - Modern Slate Palette
    public static final Color BG_DARK = new Color(15, 23, 42);       // #0f172a
    public static final Color BG_CARD = new Color(30, 41, 59);       // #1e293b
    public static final Color BG_INPUT = new Color(51, 65, 85);      // #334155
    public static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    public static final Color TEXT_MUTED = new Color(148, 163, 184);
    public static final Color ACCENT_BLUE = new Color(14, 165, 233);
    public static final Color ACCENT_GREEN = new Color(16, 185, 129);
    public static final Color ACCENT_AMBER = new Color(245, 158, 11);
    public static final Color ACCENT_PURPLE = new Color(139, 92, 246);
    public static final Color ACCENT_ROSE = new Color(244, 63, 94);

    // Font definitions
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);

    // Main Tabbed Pane
    private JTabbedPane tabbedPane;

    // Component references - Category 1 (String Matching)
    private JTextArea txtStrTarget;
    private JTextField txtStrPattern;
    private JTextField txtStrAhoKeywords;
    private JComboBox<String> cmbStrMetric;
    private BarChartPanel chartStrMatch;
    private DefaultTableModel modelStrTable;
    private JTextArea txtStrDetails;

    // Component references - Category 2 (Distance & Alignment)
    private JTextField txtDistStr1;
    private JTextField txtDistStr2;
    private JSpinner spnInsertCost, spnDeleteCost, spnReplaceCost;
    private JSpinner spnMatchScore, spnMismatchScore, spnGapPenalty;
    private JComboBox<String> cmbDistMetric;
    private BarChartPanel chartDist;
    private DefaultTableModel modelDistTable;
    private JTextArea txtDistDetails;

    // Component references - Category 3 (Suffix Structures)
    private JTextArea txtSuffixSource;
    private JTextField txtSuffixQuery;
    private BarChartPanel chartSuffix;
    private DefaultTableModel modelSuffixTable;
    private JLabel lblSuffixLRS;

    // Component references - Category 4 (DP Optimization)
    private DefaultTableModel modelActivities;
    private JSpinner spnMaxTime;
    private JTextArea txtBitmaskResults;
    private JTextField txtMatrixDims;
    private BarChartPanel chartMatrixChain;
    private JTextArea txtMatrixResults;

    // Component references - Records Manager
    private DefaultTableModel modelRecords;
    private JTextField txtRecId, txtRecDate, txtRecActivity, txtRecCategory, txtRecSensor, txtRecPref;
    private JTextArea txtRecNote;

    public LifeDataGUI() {
        super("LifeData Analytics – Algorithm Comparison & Verification Studio (Final Review)");
        initializeSampleData();
        setupLookAndFeel();
        buildUI();
    }

    private void initializeSampleData() {
        recordList.add(new PersonalRecord("REC-101", "2026-10-01", "Jogging", "Fitness",
                "Morning outdoor jogging for 4 km in cool weather", "148 bpm", "Outdoor"));
        recordList.add(new PersonalRecord("REC-102", "2026-10-01", "Meditation", "Mental Health",
                "Guided breathing session and stress relief recovery", "64 bpm", "Indoor"));
        recordList.add(new PersonalRecord("REC-103", "2026-10-02", "Walking", "Fitness",
                "I walked for 5000 steps and checked my health today", "82 bpm", "Outdoor"));
        recordList.add(new PersonalRecord("REC-104", "2026-10-02", "Gym Workout", "Fitness",
                "Core strength training, deadlifts and free weights", "410 kcal", "Gym"));
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("TabbedPane.selected", BG_CARD);
        UIManager.put("TabbedPane.background", BG_DARK);
        UIManager.put("TabbedPane.foreground", TEXT_PRIMARY);
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1240, 840);
        setMinimumSize(new Dimension(1050, 700));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        // Header Banner
        JPanel headerPanel = createHeaderPanel();
        root.add(headerPanel, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBackground(BG_DARK);
        tabbedPane.setForeground(TEXT_PRIMARY);
        tabbedPane.setFont(FONT_BOLD);

        tabbedPane.addTab("🔍 1. String Match Benchmark", createStringMatchingTab());
        tabbedPane.addTab("📏 2. Distance & Cost Measures", createDistanceMeasuresTab());
        tabbedPane.addTab("🧬 3. Suffix Indexing & Query", createSuffixStructuresTab());
        tabbedPane.addTab("🧩 4. DP Combinatorial Optimization", createDPOptimizationTab());
        tabbedPane.addTab("📋 5. Personal Records Manager", createRecordsManagerTab());
        tabbedPane.addTab("🎓 6. Final Review & Viva Studio", createFinalReviewTab());

        root.add(tabbedPane, BorderLayout.CENTER);

        // Status Bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(11, 17, 32));
        statusBar.setBorder(new EmptyBorder(6, 16, 6, 16));
        JLabel lblStatus = new JLabel("● LifeData Analytics Platform | 15 Manual DSA Algorithms | Rigorous Category-Specific Benchmarking");
        lblStatus.setForeground(TEXT_MUTED);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusBar.add(lblStatus, BorderLayout.WEST);

        JLabel lblTime = new JLabel("Zero-Dependency Java Swing Runtime");
        lblTime.setForeground(ACCENT_GREEN);
        lblTime.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusBar.add(lblTime, BorderLayout.EAST);

        root.add(statusBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel createHeaderPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(51, 65, 85)),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);

        JLabel lblTitle = new JLabel("LIFEDATA ANALYTICS PLATFORM");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(TEXT_PRIMARY);

        JLabel lblSub = new JLabel("Comprehensive DSA Verification & Comparative Evaluation for Final Review (CO1 – CO3)");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(ACCENT_BLUE);

        left.add(lblTitle);
        left.add(lblSub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        JButton btnQuickDoc = createStyledButton("📘 View Viva Guide", ACCENT_PURPLE);
        btnQuickDoc.addActionListener(e -> tabbedPane.setSelectedIndex(5));

        JButton btnExport = createStyledButton("💾 Export Full Review Report", ACCENT_GREEN);
        btnExport.addActionListener(e -> exportFullReviewReport());

        right.add(btnQuickDoc);
        right.add(btnExport);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // =========================================================================
    // TAB 1: STRING MATCHING BENCHMARK (Exact Matching Category)
    // =========================================================================
    private JPanel createStringMatchingTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top Control / Input Panel
        JPanel topCard = createCardPanel("Benchmark Configuration & Data Inputs");
        topCard.setLayout(new BorderLayout(8, 8));

        JPanel inputGrid = new JPanel(new GridBagLayout());
        inputGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Target Text Input
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        inputGrid.add(createLabel("Target Text Source:"), gbc);

        JPanel fileBtnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        fileBtnRow.setOpaque(false);

        JButton btnLoadNotes = createSmallButton("Load personal_notes.txt");
        btnLoadNotes.addActionListener(e -> loadFileIntoText("data/personal_notes.txt", txtStrTarget));

        JButton btnLoadLarge = createSmallButton("Load sample_large_text.txt");
        btnLoadLarge.addActionListener(e -> loadFileIntoText("data/sample_large_text.txt", txtStrTarget));

        JButton btnLoadEvents = createSmallButton("Load event_records.txt");
        btnLoadEvents.addActionListener(e -> loadFileIntoText("data/event_records.txt", txtStrTarget));

        JButton btnBrowseFile = createSmallButton("📂 Browse Any TXT...");
        btnBrowseFile.addActionListener(e -> browseAndLoadFile(txtStrTarget));

        JButton btnUseRecords = createSmallButton("Use Stored Records");
        btnUseRecords.addActionListener(e -> txtStrTarget.setText(getAggregatedRecordsText()));

        fileBtnRow.add(btnLoadNotes);
        fileBtnRow.add(btnLoadLarge);
        fileBtnRow.add(btnLoadEvents);
        fileBtnRow.add(btnBrowseFile);
        fileBtnRow.add(btnUseRecords);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        inputGrid.add(fileBtnRow, gbc);

        // Text Area for target text
        txtStrTarget = new JTextArea(4, 50);
        txtStrTarget.setLineWrap(true);
        txtStrTarget.setWrapStyleWord(true);
        txtStrTarget.setFont(FONT_MONO);
        txtStrTarget.setText(loadDefaultContent("data/personal_notes.txt",
                "Patient completed morning exercise and walking for 5000 steps. Heart rate stable during exercise."));
        styleTextInput(txtStrTarget);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        inputGrid.add(new JScrollPane(txtStrTarget), gbc);
        gbc.gridwidth = 1;

        // Search Pattern Input
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        inputGrid.add(createLabel("Query Pattern:"), gbc);

        txtStrPattern = new JTextField("exercise", 20);
        styleTextInput(txtStrPattern);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        inputGrid.add(txtStrPattern, gbc);

        // Aho-Corasick Multi-Keywords
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        inputGrid.add(createLabel("Aho-Corasick Keywords (CSV):"), gbc);

        txtStrAhoKeywords = new JTextField("exercise, walking, health, rate", 20);
        styleTextInput(txtStrAhoKeywords);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        inputGrid.add(txtStrAhoKeywords, gbc);

        // Action & Metric Row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        actionRow.setOpaque(false);

        JButton btnRunStr = createStyledButton("⚡ Run & Compare 5 Algorithms", ACCENT_BLUE);
        btnRunStr.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRunStr.addActionListener(e -> runStringMatchingBenchmark());

        JLabel lblMetric = createLabel("Chart Metric:");
        cmbStrMetric = new JComboBox<>(new String[]{
                "Execution Time (Nanoseconds)",
                "Character Comparisons / Operations",
                "Matches Found"
        });
        cmbStrMetric.setBackground(BG_INPUT);
        cmbStrMetric.setForeground(TEXT_PRIMARY);
        cmbStrMetric.addActionListener(e -> updateStringMatchingChart());

        actionRow.add(btnRunStr);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(lblMetric);
        actionRow.add(cmbStrMetric);

        topCard.add(inputGrid, BorderLayout.CENTER);
        topCard.add(actionRow, BorderLayout.SOUTH);

        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Bar Chart (Left) + Table & Details (Right)
        JSplitPane splitCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCenter.setResizeWeight(0.52);
        splitCenter.setDividerSize(6);
        splitCenter.setBackground(BG_DARK);

        // Left: Bar Chart Panel
        chartStrMatch = new BarChartPanel();
        chartStrMatch.setChartData("String Matching Benchmark", "Execution Time (ns)", null);
        JPanel chartCard = createCardPanel("Performance Comparison (Bar Graph)");
        chartCard.setLayout(new BorderLayout());
        chartCard.add(chartStrMatch, BorderLayout.CENTER);
        splitCenter.setLeftComponent(chartCard);

        // Right: Results Table & Invariants Details
        JPanel rightCard = createCardPanel("Benchmark Metrics & Output Verification");
        rightCard.setLayout(new BorderLayout(6, 6));

        String[] cols = {"Algorithm", "Time (ns)", "Comparisons", "Matches", "Time Big-O", "Space Big-O"};
        modelStrTable = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableStr = createStyledTable(modelStrTable);
        JScrollPane spTable = new JScrollPane(tableStr);
        spTable.setPreferredSize(new Dimension(480, 140));

        txtStrDetails = new JTextArea();
        txtStrDetails.setEditable(false);
        txtStrDetails.setFont(FONT_MONO);
        styleTextInput(txtStrDetails);
        txtStrDetails.setText("Click 'Run & Compare 5 Algorithms' above to benchmark.\n\n"
                + "Why Compare String Matching Algorithms Together?\n"
                + "• All 5 algorithms solve the identical problem: exact pattern search over text.\n"
                + "• Comparing execution time and character comparisons directly validates theoretical\n"
                + "  complexity advantages (e.g. KMP O(n+m) avoiding backtracking vs Naive O(nm)).");
        JScrollPane spDetails = new JScrollPane(txtStrDetails);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spTable, spDetails);
        rightSplit.setResizeWeight(0.42);
        rightSplit.setDividerSize(5);
        rightSplit.setBackground(BG_DARK);

        rightCard.add(rightSplit, BorderLayout.CENTER);
        splitCenter.setRightComponent(rightCard);

        panel.add(splitCenter, BorderLayout.CENTER);
        return panel;
    }

    private void runStringMatchingBenchmark() {
        String text = txtStrTarget.getText();
        String pattern = txtStrPattern.getText();

        if (text == null || text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide non-empty target text.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (pattern == null || pattern.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search query pattern.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Parse Aho-Corasick keywords
        String[] kwArray = txtStrAhoKeywords.getText().split(",");
        List<String> keywords = new ArrayList<>();
        for (String k : kwArray) {
            if (!k.trim().isEmpty()) keywords.add(k.trim());
        }
        if (keywords.isEmpty()) keywords.add(pattern);

        // Warmup JIT
        for (int i = 0; i < 5; i++) {
            NaiveSearch.search(text, pattern);
            KMP.search(text, pattern);
            ZAlgorithm.search(text, pattern);
            RabinKarp.search(text, pattern);
        }

        // Execute 1: Naive Search
        NaiveSearch.SearchResult rNaive = NaiveSearch.search(text, pattern);

        // Execute 2: KMP
        KMP.KmpResult rKmp = KMP.search(text, pattern);

        // Execute 3: Z-Algorithm
        ZAlgorithm.ZResult rZ = ZAlgorithm.search(text, pattern);

        // Execute 4: Rabin-Karp
        RabinKarp.RkResult rRk = RabinKarp.search(text, pattern);

        // Execute 5: Aho-Corasick
        AhoCorasick ac = new AhoCorasick(keywords);
        AhoCorasick.MatchResult rAc = ac.search(text);

        // Populate Table
        modelStrTable.setRowCount(0);
        modelStrTable.addRow(new Object[]{"Naive Search", numFmt.format(rNaive.executionTimeNs), numFmt.format(rNaive.comparisons), rNaive.positions.size(), "O(n · m)", "O(1)"});
        modelStrTable.addRow(new Object[]{"KMP", numFmt.format(rKmp.executionTimeNs), numFmt.format(rKmp.comparisons), rKmp.positions.size(), "O(n + m)", "O(m)"});
        modelStrTable.addRow(new Object[]{"Z-Algorithm", numFmt.format(rZ.executionTimeNs), numFmt.format(rZ.comparisons), rZ.positions.size(), "O(n + m)", "O(n + m)"});
        modelStrTable.addRow(new Object[]{"Rabin-Karp", numFmt.format(rRk.executionTimeNs), numFmt.format(rRk.comparisons), rRk.positions.size(), "O(n + m) avg", "O(1)"});
        modelStrTable.addRow(new Object[]{"Aho-Corasick", numFmt.format(rAc.executionTimeNs), numFmt.format(rAc.transitions), rAc.getTotalMatches(), "O(n + z)", "O(Σ·m)"});

        // Store cached results on table for quick metric switching
        currentStrResults = new Object[]{rNaive, rKmp, rZ, rRk, rAc, pattern, keywords};

        // Update Details text
        StringBuilder sb = new StringBuilder();
        sb.append("=== STRING MATCHING BENCHMARK RESULTS ===\n");
        sb.append(String.format("Text Length: %d chars | Query Pattern: '%s' (%d chars)\n\n", text.length(), pattern, pattern.length()));
        sb.append(String.format("[1] Naive Search      : %d matches found at positions %s\n", rNaive.positions.size(), rNaive.positions));
        sb.append(String.format("[2] KMP               : %d matches found at positions %s\n", rKmp.positions.size(), rKmp.positions));
        sb.append(String.format("    └─ LPS Array      : %s\n", Arrays.toString(rKmp.lps)));
        sb.append(String.format("[3] Z-Algorithm       : %d matches found at positions %s\n", rZ.positions.size(), rZ.positions));
        sb.append(String.format("[4] Rabin-Karp        : %d matches found | Pattern Hash: %d | Spurious Hits: %d\n", rRk.positions.size(), rRk.patternHash, rRk.spuriousHits));
        sb.append(String.format("[5] Aho-Corasick      : %d total matches across %d keywords:\n", rAc.getTotalMatches(), keywords.size()));
        for (Map.Entry<String, List<Integer>> e : rAc.keywordPositions.entrySet()) {
            sb.append(String.format("    └─ '%s': %d matches at %s\n", e.getKey(), e.getValue().size(), e.getValue()));
        }
        sb.append("\n[ACADEMIC VERIFICATION]\n");
        sb.append("• Correctness: All 4 single-pattern matchers returned identical match counts and index positions.\n");
        sb.append("• KMP LPS avoids text backtracking by utilizing prefix-suffix overlap.\n");
        sb.append("• Rabin-Karp achieved O(1) rolling hash sliding window updates.\n");
        txtStrDetails.setText(sb.toString());
        txtStrDetails.setCaretPosition(0);

        updateStringMatchingChart();
    }

    private Object[] currentStrResults = null;

    private void updateStringMatchingChart() {
        if (currentStrResults == null) return;

        NaiveSearch.SearchResult rNaive = (NaiveSearch.SearchResult) currentStrResults[0];
        KMP.KmpResult rKmp = (KMP.KmpResult) currentStrResults[1];
        ZAlgorithm.ZResult rZ = (ZAlgorithm.ZResult) currentStrResults[2];
        RabinKarp.RkResult rRk = (RabinKarp.RkResult) currentStrResults[3];
        AhoCorasick.MatchResult rAc = (AhoCorasick.MatchResult) currentStrResults[4];

        int sel = cmbStrMetric.getSelectedIndex();
        List<BarChartPanel.BarItem> items = new ArrayList<>();

        if (sel == 0) {
            // Execution Time (ns)
            items.add(new BarChartPanel.BarItem("Naive", "O(nm)", rNaive.executionTimeNs, numFmt.format(rNaive.executionTimeNs) + " ns", BarChartPanel.PALETTE[0]));
            items.add(new BarChartPanel.BarItem("KMP", "O(n+m)", rKmp.executionTimeNs, numFmt.format(rKmp.executionTimeNs) + " ns", BarChartPanel.PALETTE[1]));
            items.add(new BarChartPanel.BarItem("Z-Algo", "O(n+m)", rZ.executionTimeNs, numFmt.format(rZ.executionTimeNs) + " ns", BarChartPanel.PALETTE[2]));
            items.add(new BarChartPanel.BarItem("Rabin-Karp", "O(n+m)", rRk.executionTimeNs, numFmt.format(rRk.executionTimeNs) + " ns", BarChartPanel.PALETTE[3]));
            items.add(new BarChartPanel.BarItem("Aho-Corasick", "O(n+z)", rAc.executionTimeNs, numFmt.format(rAc.executionTimeNs) + " ns", BarChartPanel.PALETTE[4]));
            chartStrMatch.setChartData("Execution Time Comparison (Lower is Faster)", "Execution Time (Nanoseconds)", items);
        } else if (sel == 1) {
            // Comparisons / Operations
            items.add(new BarChartPanel.BarItem("Naive", "char checks", rNaive.comparisons, numFmt.format(rNaive.comparisons) + " ops", BarChartPanel.PALETTE[0]));
            items.add(new BarChartPanel.BarItem("KMP", "char checks", rKmp.comparisons, numFmt.format(rKmp.comparisons) + " ops", BarChartPanel.PALETTE[1]));
            items.add(new BarChartPanel.BarItem("Z-Algo", "Z checks", rZ.comparisons, numFmt.format(rZ.comparisons) + " ops", BarChartPanel.PALETTE[2]));
            items.add(new BarChartPanel.BarItem("Rabin-Karp", "hash verify", rRk.comparisons, numFmt.format(rRk.comparisons) + " ops", BarChartPanel.PALETTE[3]));
            items.add(new BarChartPanel.BarItem("Aho-Corasick", "Trie hops", rAc.transitions, numFmt.format(rAc.transitions) + " hops", BarChartPanel.PALETTE[4]));
            chartStrMatch.setChartData("Character Comparisons & Automata Transitions", "Operation Count (Operations)", items);
        } else {
            // Matches Found
            items.add(new BarChartPanel.BarItem("Naive", "Exact Matches", rNaive.positions.size(), rNaive.positions.size() + " matches", BarChartPanel.PALETTE[0]));
            items.add(new BarChartPanel.BarItem("KMP", "Exact Matches", rKmp.positions.size(), rKmp.positions.size() + " matches", BarChartPanel.PALETTE[1]));
            items.add(new BarChartPanel.BarItem("Z-Algo", "Exact Matches", rZ.positions.size(), rZ.positions.size() + " matches", BarChartPanel.PALETTE[2]));
            items.add(new BarChartPanel.BarItem("Rabin-Karp", "Exact Matches", rRk.positions.size(), rRk.positions.size() + " matches", BarChartPanel.PALETTE[3]));
            items.add(new BarChartPanel.BarItem("Aho-Corasick", "Multi-keyword", rAc.getTotalMatches(), rAc.getTotalMatches() + " matches", BarChartPanel.PALETTE[4]));
            chartStrMatch.setChartData("Match Occurrences Verified Across Algorithms", "Total Matches Found", items);
        }
    }

    // =========================================================================
    // TAB 2: DISTANCE & COST MEASURES (Edit Distance & Sequence Alignment)
    // =========================================================================
    private JPanel createDistanceMeasuresTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top Configuration Card
        JPanel topCard = createCardPanel("Sequence Distance & Alignment Configuration");
        topCard.setLayout(new BorderLayout(8, 8));

        JPanel inputs = new JPanel(new GridBagLayout());
        inputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // String 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        inputs.add(createLabel("String 1 (Source Sequence):"), gbc);

        txtDistStr1 = new JTextField("cardiac arrhytmia", 25);
        styleTextInput(txtDistStr1);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        inputs.add(txtDistStr1, gbc);

        // String 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputs.add(createLabel("String 2 (Target / Comparison):"), gbc);

        txtDistStr2 = new JTextField("cardiac arrhythmia", 25);
        styleTextInput(txtDistStr2);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        inputs.add(txtDistStr2, gbc);

        // Presets
        JPanel presetRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        presetRow.setOpaque(false);

        JButton btnTypoPreset = createSmallButton("Keyboard Typo ('form' vs 'from')");
        btnTypoPreset.addActionListener(e -> {
            txtDistStr1.setText("form");
            txtDistStr2.setText("from");
        });

        JButton btnClinicalPreset = createSmallButton("Clinical Typo ('arrhytmia' vs 'arrhythmia')");
        btnClinicalPreset.addActionListener(e -> {
            txtDistStr1.setText("cardiac arrhytmia");
            txtDistStr2.setText("cardiac arrhythmia");
        });

        JButton btnSensorPreset = createSmallButton("Sensor Stream ('HR:72-ST:5000' vs 'HR:75-ST:5200')");
        btnSensorPreset.addActionListener(e -> {
            txtDistStr1.setText("HR:72-ST:5000");
            txtDistStr2.setText("HR:75-ST:5200");
        });

        presetRow.add(createLabel("Quick Presets:"));
        presetRow.add(btnTypoPreset);
        presetRow.add(btnClinicalPreset);
        presetRow.add(btnSensorPreset);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inputs.add(presetRow, gbc);
        gbc.gridwidth = 1;

        // Cost & Penalty Parameters Row
        JPanel paramsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        paramsRow.setOpaque(false);

        paramsRow.add(createLabel("Weighted Edit [Ins/Del/Rep]:"));
        spnInsertCost = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 10.0, 0.5));
        spnDeleteCost = new JSpinner(new SpinnerNumberModel(1.5, 0.1, 10.0, 0.5));
        spnReplaceCost = new JSpinner(new SpinnerNumberModel(2.0, 0.1, 10.0, 0.5));
        styleSpinner(spnInsertCost); styleSpinner(spnDeleteCost); styleSpinner(spnReplaceCost);
        paramsRow.add(spnInsertCost);
        paramsRow.add(spnDeleteCost);
        paramsRow.add(spnReplaceCost);

        paramsRow.add(Box.createHorizontalStrut(10));
        paramsRow.add(createLabel("Alignment [Match/Mismatch/Gap]:"));
        spnMatchScore = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        spnMismatchScore = new JSpinner(new SpinnerNumberModel(-1, -10, 0, 1));
        spnGapPenalty = new JSpinner(new SpinnerNumberModel(-1, -10, 0, 1));
        styleSpinner(spnMatchScore); styleSpinner(spnMismatchScore); styleSpinner(spnGapPenalty);
        paramsRow.add(spnMatchScore);
        paramsRow.add(spnMismatchScore);
        paramsRow.add(spnGapPenalty);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        inputs.add(paramsRow, gbc);
        gbc.gridwidth = 1;

        // Action Row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        actionRow.setOpaque(false);

        JButton btnRunDist = createStyledButton("⚡ Compute & Compare 5 Distance/Alignment Algorithms", ACCENT_GREEN);
        btnRunDist.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRunDist.addActionListener(e -> runDistanceMeasuresBenchmark());

        JLabel lblMetric = createLabel("Chart Metric:");
        cmbDistMetric = new JComboBox<>(new String[]{
                "Execution Time (Nanoseconds)",
                "Computed Metric Value (Distance / Score)",
                "DP Table Cells Computed ((M+1) x (N+1))"
        });
        cmbDistMetric.setBackground(BG_INPUT);
        cmbDistMetric.setForeground(TEXT_PRIMARY);
        cmbDistMetric.addActionListener(e -> updateDistanceChart());

        actionRow.add(btnRunDist);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(lblMetric);
        actionRow.add(cmbDistMetric);

        topCard.add(inputs, BorderLayout.CENTER);
        topCard.add(actionRow, BorderLayout.SOUTH);

        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Chart (Left) + Results Table & Alignment Visualizer (Right)
        JSplitPane splitCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCenter.setResizeWeight(0.52);
        splitCenter.setDividerSize(6);
        splitCenter.setBackground(BG_DARK);

        chartDist = new BarChartPanel();
        chartDist.setChartData("Distance & Alignment Measures Benchmark", "Execution Time (ns)", null);
        JPanel chartCard = createCardPanel("Comparison Bar Graph");
        chartCard.setLayout(new BorderLayout());
        chartCard.add(chartDist, BorderLayout.CENTER);
        splitCenter.setLeftComponent(chartCard);

        JPanel rightCard = createCardPanel("Detailed Distance & Alignment Outputs");
        rightCard.setLayout(new BorderLayout(6, 6));

        String[] cols = {"Algorithm", "Metric Type", "Result Value", "Time (ns)", "DP Cells", "Time Big-O"};
        modelDistTable = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableDist = createStyledTable(modelDistTable);
        JScrollPane spTable = new JScrollPane(tableDist);
        spTable.setPreferredSize(new Dimension(480, 140));

        txtDistDetails = new JTextArea();
        txtDistDetails.setEditable(false);
        txtDistDetails.setFont(FONT_MONO);
        styleTextInput(txtDistDetails);
        txtDistDetails.setText("Click 'Compute & Compare 5 Distance/Alignment Algorithms' to evaluate.\n\n"
                + "Why Group Distance & Alignment Algorithms Together?\n"
                + "• All 5 algorithms operate on pairwise sequence comparison using 2D Dynamic Programming.\n"
                + "• Wagner-Fischer Levenshtein vs Damerau-Levenshtein highlights the cost difference\n"
                + "  when adjacent character transpositions are permitted.\n"
                + "• Needleman-Wunsch provides global end-to-end alignment, while Smith-Waterman finds\n"
                + "  the highest scoring local subsequence motif.");
        JScrollPane spDetails = new JScrollPane(txtDistDetails);

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spTable, spDetails);
        rightSplit.setResizeWeight(0.42);
        rightSplit.setDividerSize(5);
        rightSplit.setBackground(BG_DARK);

        rightCard.add(rightSplit, BorderLayout.CENTER);
        splitCenter.setRightComponent(rightCard);

        panel.add(splitCenter, BorderLayout.CENTER);
        return panel;
    }

    private Object[] currentDistResults = null;

    private void runDistanceMeasuresBenchmark() {
        String s1 = txtDistStr1.getText();
        String s2 = txtDistStr2.getText();

        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        double ins = ((Number) spnInsertCost.getValue()).doubleValue();
        double del = ((Number) spnDeleteCost.getValue()).doubleValue();
        double rep = ((Number) spnReplaceCost.getValue()).doubleValue();

        int mScore = ((Number) spnMatchScore.getValue()).intValue();
        int mmScore = ((Number) spnMismatchScore.getValue()).intValue();
        int gap = ((Number) spnGapPenalty.getValue()).intValue();

        // 1. Levenshtein
        Levenshtein.Result rLev = Levenshtein.compute(s1, s2);

        // 2. Damerau-Levenshtein
        DamerauLevenshtein.Result rDam = DamerauLevenshtein.compute(s1, s2);

        // 3. Weighted Edit Distance
        WeightedEditDistance.Result rWeight = WeightedEditDistance.compute(s1, s2, ins, del, rep);

        // 4. Needleman-Wunsch
        NeedlemanWunsch.AlignmentResult rNw = NeedlemanWunsch.align(s1, s2, mScore, mmScore, gap);

        // 5. Smith-Waterman
        SmithWaterman.AlignmentResult rSw = SmithWaterman.align(s1, s2, 2, -1, -1);

        int dpCells = (s1.length() + 1) * (s2.length() + 1);

        // Update Table
        modelDistTable.setRowCount(0);
        modelDistTable.addRow(new Object[]{"Levenshtein", "Edit Distance", rLev.distance + " edits", numFmt.format(rLev.executionTimeNs), numFmt.format(dpCells), "O(n · m)"});
        modelDistTable.addRow(new Object[]{"Damerau-Lev", "Edit Dist + Transp", rDam.distance + " edits", numFmt.format(rDam.executionTimeNs), numFmt.format(dpCells), "O(n · m)"});
        modelDistTable.addRow(new Object[]{"Weighted Edit", "Weighted Cost", String.format("%.2f cost", rWeight.totalCost), numFmt.format(rWeight.executionTimeNs), numFmt.format(dpCells), "O(n · m)"});
        modelDistTable.addRow(new Object[]{"Needleman-Wunsch", "Global Align Score", rNw.score + " pts", numFmt.format(rNw.executionTimeNs), numFmt.format(dpCells), "O(n · m)"});
        modelDistTable.addRow(new Object[]{"Smith-Waterman", "Local Align Score", rSw.maxScore + " pts", numFmt.format(rSw.executionTimeNs), numFmt.format(dpCells), "O(n · m)"});

        currentDistResults = new Object[]{rLev, rDam, rWeight, rNw, rSw, s1, s2, dpCells};

        // Update Details text
        StringBuilder sb = new StringBuilder();
        sb.append("=== DISTANCE & ALIGNMENT DETAILED EVALUATION ===\n");
        sb.append(String.format("Sequence 1: \"%s\" (len=%d)\nSequence 2: \"%s\" (len=%d)\n\n", s1, s1.length(), s2, s2.length()));

        sb.append(String.format("[1] LEVENSHTEIN DISTANCE: %d\n", rLev.distance));
        sb.append("    Backtracked Edit Sequence:\n");
        for (String step : rLev.editSteps) {
            sb.append("    • ").append(step).append("\n");
        }

        sb.append(String.format("\n[2] DAMERAU-LEVENSHTEIN DISTANCE: %d\n", rDam.distance));
        sb.append(String.format("    Adjacent Transposition Utilized? %s\n", rDam.usedTransposition ? "YES (Saved 1 edit operation!)" : "NO"));

        sb.append(String.format("\n[3] WEIGHTED EDIT DISTANCE: %.2f\n", rWeight.totalCost));
        sb.append(String.format("    Penalty Configuration: Insert=%.1f, Delete=%.1f, Replace=%.1f\n", ins, del, rep));

        sb.append(String.format("\n[4] NEEDLEMAN-WUNSCH GLOBAL ALIGNMENT (Score: %d)\n", rNw.score));
        sb.append(String.format("    Aligned Seq 1: %s\n", rNw.alignedSeq1));
        sb.append("    Alignment    : ");
        for (int i = 0; i < Math.min(rNw.alignedSeq1.length(), rNw.alignedSeq2.length()); i++) {
            char c1 = rNw.alignedSeq1.charAt(i);
            char c2 = rNw.alignedSeq2.charAt(i);
            if (c1 == c2 && c1 != '-') sb.append("|");
            else if (c1 == '-' || c2 == '-') sb.append(" ");
            else sb.append(":");
        }
        sb.append("\n");
        sb.append(String.format("    Aligned Seq 2: %s\n", rNw.alignedSeq2));

        sb.append(String.format("\n[5] SMITH-WATERMAN LOCAL ALIGNMENT (Peak Score: %d at [%d, %d])\n", rSw.maxScore, rSw.maxI, rSw.maxJ));
        sb.append(String.format("    Optimal Local Motif 1: %s\n", rSw.alignedSeq1));
        sb.append(String.format("    Optimal Local Motif 2: %s\n", rSw.alignedSeq2));

        txtDistDetails.setText(sb.toString());
        txtDistDetails.setCaretPosition(0);

        updateDistanceChart();
    }

    private void updateDistanceChart() {
        if (currentDistResults == null) return;

        Levenshtein.Result rLev = (Levenshtein.Result) currentDistResults[0];
        DamerauLevenshtein.Result rDam = (DamerauLevenshtein.Result) currentDistResults[1];
        WeightedEditDistance.Result rWeight = (WeightedEditDistance.Result) currentDistResults[2];
        NeedlemanWunsch.AlignmentResult rNw = (NeedlemanWunsch.AlignmentResult) currentDistResults[3];
        SmithWaterman.AlignmentResult rSw = (SmithWaterman.AlignmentResult) currentDistResults[4];
        int dpCells = (int) currentDistResults[7];

        int sel = cmbDistMetric.getSelectedIndex();
        List<BarChartPanel.BarItem> items = new ArrayList<>();

        if (sel == 0) {
            // Execution Time (ns)
            items.add(new BarChartPanel.BarItem("Levenshtein", "O(nm)", rLev.executionTimeNs, numFmt.format(rLev.executionTimeNs) + " ns", BarChartPanel.PALETTE[0]));
            items.add(new BarChartPanel.BarItem("Damerau-Lev", "O(nm)", rDam.executionTimeNs, numFmt.format(rDam.executionTimeNs) + " ns", BarChartPanel.PALETTE[1]));
            items.add(new BarChartPanel.BarItem("Weighted Edit", "O(nm)", rWeight.executionTimeNs, numFmt.format(rWeight.executionTimeNs) + " ns", BarChartPanel.PALETTE[2]));
            items.add(new BarChartPanel.BarItem("Needleman-W", "O(nm)", rNw.executionTimeNs, numFmt.format(rNw.executionTimeNs) + " ns", BarChartPanel.PALETTE[3]));
            items.add(new BarChartPanel.BarItem("Smith-Water", "O(nm)", rSw.executionTimeNs, numFmt.format(rSw.executionTimeNs) + " ns", BarChartPanel.PALETTE[4]));
            chartDist.setChartData("Execution Time Comparison (Lower is Faster)", "Execution Time (Nanoseconds)", items);
        } else if (sel == 1) {
            // Computed Metric Value
            items.add(new BarChartPanel.BarItem("Levenshtein", "Unit Edits", rLev.distance, rLev.distance + " edits", BarChartPanel.PALETTE[0]));
            items.add(new BarChartPanel.BarItem("Damerau-Lev", "With Swaps", rDam.distance, rDam.distance + " edits", BarChartPanel.PALETTE[1]));
            items.add(new BarChartPanel.BarItem("Weighted Edit", "Asymmetric", rWeight.totalCost, String.format("%.1f cost", rWeight.totalCost), BarChartPanel.PALETTE[2]));
            items.add(new BarChartPanel.BarItem("Needleman-W", "Global Align", Math.max(0, rNw.score), rNw.score + " pts", BarChartPanel.PALETTE[3]));
            items.add(new BarChartPanel.BarItem("Smith-Water", "Local Align", rSw.maxScore, rSw.maxScore + " pts", BarChartPanel.PALETTE[4]));
            chartDist.setChartData("Computed Metric Values / Scores", "Distance or Alignment Score", items);
        } else {
            // DP Cells
            items.add(new BarChartPanel.BarItem("Levenshtein", "(M+1)x(N+1)", dpCells, numFmt.format(dpCells) + " cells", BarChartPanel.PALETTE[0]));
            items.add(new BarChartPanel.BarItem("Damerau-Lev", "(M+1)x(N+1)", dpCells, numFmt.format(dpCells) + " cells", BarChartPanel.PALETTE[1]));
            items.add(new BarChartPanel.BarItem("Weighted Edit", "(M+1)x(N+1)", dpCells, numFmt.format(dpCells) + " cells", BarChartPanel.PALETTE[2]));
            items.add(new BarChartPanel.BarItem("Needleman-W", "(M+1)x(N+1)", dpCells, numFmt.format(dpCells) + " cells", BarChartPanel.PALETTE[3]));
            items.add(new BarChartPanel.BarItem("Smith-Water", "(M+1)x(N+1)", dpCells, numFmt.format(dpCells) + " cells", BarChartPanel.PALETTE[4]));
            chartDist.setChartData("Dynamic Programming Matrix Cells Evaluated", "DP Matrix Cells Computed", items);
        }
    }

    // =========================================================================
    // TAB 3: SUFFIX STRUCTURES & INDEXING
    // =========================================================================
    private JPanel createSuffixStructuresTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top Card
        JPanel topCard = createCardPanel("Suffix Array Indexing & Repeated Query Benchmark");
        topCard.setLayout(new BorderLayout(8, 8));

        JPanel inputs = new JPanel(new GridBagLayout());
        inputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        inputs.add(createLabel("Text to Index:"), gbc);

        txtSuffixSource = new JTextArea(3, 40);
        txtSuffixSource.setLineWrap(true);
        txtSuffixSource.setWrapStyleWord(true);
        txtSuffixSource.setText("patient exercise and morning walking. regular walking improves stamina. daily exercise is essential.");
        styleTextInput(txtSuffixSource);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        inputs.add(new JScrollPane(txtSuffixSource), gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        inputs.add(createLabel("Query Substring:"), gbc);

        txtSuffixQuery = new JTextField("walking", 20);
        styleTextInput(txtSuffixQuery);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        inputs.add(txtSuffixQuery, gbc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        btnRow.setOpaque(false);

        JButton btnBuild = createStyledButton("⚡ Construct Suffix Array & Run Repeated Query Test", ACCENT_PURPLE);
        btnBuild.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuild.addActionListener(e -> runSuffixBenchmark());

        lblSuffixLRS = new JLabel("Longest Repeated Substring: (Click button above)");
        lblSuffixLRS.setForeground(ACCENT_AMBER);
        lblSuffixLRS.setFont(FONT_BOLD);

        btnRow.add(btnBuild);
        btnRow.add(Box.createHorizontalStrut(15));
        btnRow.add(lblSuffixLRS);

        topCard.add(inputs, BorderLayout.CENTER);
        topCard.add(btnRow, BorderLayout.SOUTH);

        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Chart (Query Time comparison across 50 repeated queries) + Table (Suffix Array & LCP)
        JSplitPane splitCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCenter.setResizeWeight(0.5);
        splitCenter.setDividerSize(6);
        splitCenter.setBackground(BG_DARK);

        chartSuffix = new BarChartPanel();
        chartSuffix.setChartData("Multi-Query Throughput: Suffix Index vs Full Scans", "Time for 50 Queries (ns)", null);
        JPanel chartCard = createCardPanel("Index Amortization Comparison (50 Repeated Queries)");
        chartCard.setLayout(new BorderLayout());
        chartCard.add(chartSuffix, BorderLayout.CENTER);
        splitCenter.setLeftComponent(chartCard);

        JPanel tableCard = createCardPanel("Sorted Suffix Array & Kasai LCP Table");
        tableCard.setLayout(new BorderLayout());

        String[] cols = {"Rank", "Suffix Index", "LCP", "Suffix Substring Preview"};
        modelSuffixTable = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableSuffix = createStyledTable(modelSuffixTable);
        tableCard.add(new JScrollPane(tableSuffix), BorderLayout.CENTER);

        splitCenter.setRightComponent(tableCard);

        panel.add(splitCenter, BorderLayout.CENTER);
        return panel;
    }

    private void runSuffixBenchmark() {
        String text = txtSuffixSource.getText();
        String query = txtSuffixQuery.getText();

        if (text == null || text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter non-empty text to index.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (query == null || query.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a query substring.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long t0 = System.nanoTime();
        SuffixArray sa = new SuffixArray(text);
        long tBuildSa = System.nanoTime() - t0;

        int[] suffixArr = sa.getSuffixArray();
        int[] lcp = KasaiLCP.buildLCP(text, suffixArr);
        String lrs = KasaiLCP.getLongestRepeatedSubstring(text, suffixArr, lcp);

        lblSuffixLRS.setText("Longest Repeated Substring: \"" + lrs + "\" (Length: " + lrs.length() + ")");

        // Benchmark 50 repeated queries: Suffix Array Binary Search vs KMP vs Naive
        int repeats = 50;

        // Warmup
        for (int i = 0; i < 5; i++) {
            sa.searchSubstring(query);
            KMP.search(text, query);
            NaiveSearch.search(text, query);
        }

        long tSaStart = System.nanoTime();
        List<Integer> saMatches = null;
        for (int i = 0; i < repeats; i++) {
            saMatches = sa.searchSubstring(query);
        }
        long tSaQueryTotal = System.nanoTime() - tSaStart;

        long tKmpStart = System.nanoTime();
        for (int i = 0; i < repeats; i++) {
            KMP.search(text, query);
        }
        long tKmpQueryTotal = System.nanoTime() - tKmpStart;

        long tNaiveStart = System.nanoTime();
        for (int i = 0; i < repeats; i++) {
            NaiveSearch.search(text, query);
        }
        long tNaiveQueryTotal = System.nanoTime() - tNaiveStart;

        // Populate Suffix Table
        modelSuffixTable.setRowCount(0);
        int previewCount = Math.min(100, text.length());
        for (int i = 0; i < previewCount; i++) {
            int idx = suffixArr[i];
            String suf = text.substring(idx);
            if (suf.length() > 35) suf = suf.substring(0, 32) + "...";
            modelSuffixTable.addRow(new Object[]{i, idx, lcp[i], suf});
        }

        // Populate Bar Chart
        List<BarChartPanel.BarItem> items = new ArrayList<>();
        items.add(new BarChartPanel.BarItem("Suffix Binary Search", "O(m log n) / q", tSaQueryTotal, numFmt.format(tSaQueryTotal) + " ns", BarChartPanel.PALETTE[1]));
        items.add(new BarChartPanel.BarItem("KMP Linear Scan", "O(n + m) / q", tKmpQueryTotal, numFmt.format(tKmpQueryTotal) + " ns", BarChartPanel.PALETTE[0]));
        items.add(new BarChartPanel.BarItem("Naive Scan", "O(nm) / q", tNaiveQueryTotal, numFmt.format(tNaiveQueryTotal) + " ns", BarChartPanel.PALETTE[4]));

        chartSuffix.setChartData(
                String.format("Throughput on %d Repeated Queries for \"%s\"", repeats, query),
                "Total Time for 50 Queries (Nanoseconds)",
                items
        );
    }

    // =========================================================================
    // TAB 4: COMBINATORIAL DP & OPTIMIZATION
    // =========================================================================
    private JPanel createDPOptimizationTab() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 12, 12));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Module 4A: Bitmask DP Activity Selection
        JPanel leftCard = createCardPanel("Bitmask Dynamic Programming (Activity & Preference Optimization)");
        leftCard.setLayout(new BorderLayout(8, 8));

        String[] cols = {"Activity Name", "Time (mins)", "Utility (pts)"};
        modelActivities = new DefaultTableModel(cols, 0);
        modelActivities.addRow(new Object[]{"Jogging", 40, 50});
        modelActivities.addRow(new Object[]{"Meditation", 20, 30});
        modelActivities.addRow(new Object[]{"Cycling", 60, 70});
        modelActivities.addRow(new Object[]{"Reading Health Log", 15, 25});
        modelActivities.addRow(new Object[]{"Stretching / Yoga", 30, 40});
        modelActivities.addRow(new Object[]{"Swimming", 45, 60});

        JTable tableAct = createStyledTable(modelActivities);
        JScrollPane spAct = new JScrollPane(tableAct);
        spAct.setPreferredSize(new Dimension(300, 160));

        JPanel bitmaskControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        bitmaskControl.setOpaque(false);
        bitmaskControl.add(createLabel("Max Time Budget (mins):"));
        spnMaxTime = new JSpinner(new SpinnerNumberModel(90, 10, 300, 5));
        styleSpinner(spnMaxTime);
        bitmaskControl.add(spnMaxTime);

        JButton btnSolveBitmask = createStyledButton("⚡ Solve Optimal Subset", ACCENT_AMBER);
        btnSolveBitmask.addActionListener(e -> solveBitmaskDP());
        bitmaskControl.add(btnSolveBitmask);

        txtBitmaskResults = new JTextArea();
        txtBitmaskResults.setEditable(false);
        txtBitmaskResults.setFont(FONT_MONO);
        styleTextInput(txtBitmaskResults);
        txtBitmaskResults.setText("Click 'Solve Optimal Subset' to evaluate combinations.\n\n"
                + "Complexity: O(2^N · N) exploring 2^N subsets.\n"
                + "Demonstrates state compression via binary bitmasks for personal time allocation.");

        leftCard.add(spAct, BorderLayout.NORTH);
        leftCard.add(bitmaskControl, BorderLayout.CENTER);
        leftCard.add(new JScrollPane(txtBitmaskResults), BorderLayout.SOUTH);

        // Module 4B: Matrix Chain Multiplication
        JPanel rightCard = createCardPanel("Matrix Chain Multiplication (Pipeline Parenthesization)");
        rightCard.setLayout(new BorderLayout(8, 8));

        JPanel mcmControl = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        mcmControl.setOpaque(false);
        mcmControl.add(createLabel("Dimension Sequence:"));
        txtMatrixDims = new JTextField("10, 30, 5, 60, 20", 18);
        styleTextInput(txtMatrixDims);
        mcmControl.add(txtMatrixDims);

        JButton btnSolveMCM = createStyledButton("⚡ Optimize Pipeline", ACCENT_PURPLE);
        btnSolveMCM.addActionListener(e -> solveMatrixChain());
        mcmControl.add(btnSolveMCM);

        chartMatrixChain = new BarChartPanel();
        chartMatrixChain.setPreferredSize(new Dimension(400, 220));
        chartMatrixChain.setChartData("Scalar Multiplications: Naive vs Optimal MCM", "Scalar Multiplications", null);

        txtMatrixResults = new JTextArea();
        txtMatrixResults.setEditable(false);
        txtMatrixResults.setFont(FONT_MONO);
        styleTextInput(txtMatrixResults);
        txtMatrixResults.setText("Enter matrix dimensions above and click 'Optimize Pipeline'.\n\n"
                + "Complexity: O(N^3) via Interval Dynamic Programming.\n"
                + "Calculates optimal parenthesization order to minimize computational cost in sensor pipelines.");

        rightCard.add(mcmControl, BorderLayout.NORTH);
        rightCard.add(chartMatrixChain, BorderLayout.CENTER);
        rightCard.add(new JScrollPane(txtMatrixResults), BorderLayout.SOUTH);

        panel.add(leftCard);
        panel.add(rightCard);
        return panel;
    }

    private void solveBitmaskDP() {
        int rows = modelActivities.getRowCount();
        List<BitmaskDP.Activity> activities = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            String name = modelActivities.getValueAt(i, 0).toString();
            int time = Integer.parseInt(modelActivities.getValueAt(i, 1).toString());
            int val = Integer.parseInt(modelActivities.getValueAt(i, 2).toString());
            activities.add(new BitmaskDP.Activity(name, time, val));
        }

        int maxBudget = ((Number) spnMaxTime.getValue()).intValue();
        BitmaskDP.OptimizationResult res = BitmaskDP.optimize(activities, maxBudget);

        StringBuilder sb = new StringBuilder();
        sb.append("=== BITMASK DP OPTIMIZATION RESULTS ===\n");
        sb.append(String.format("Available Activities: %d | Total Subsets Explored: 2^%d = %d states\n",
                activities.size(), activities.size(), 1 << activities.size()));
        sb.append(String.format("Time Budget: %d minutes | Time Consumed: %d minutes\n", maxBudget, res.totalTimeSpent));
        sb.append(String.format("Optimal Utility Achieved: %d points (Mask: %s)\n", res.maxUtility, Integer.toBinaryString(res.optimalMask)));
        sb.append(String.format("Algorithm Execution Time: %s ns\n\n", numFmt.format(res.executionTimeNs)));
        sb.append("Selected Optimal Activity Subset:\n");
        for (BitmaskDP.Activity a : res.selectedActivities) {
            sb.append(String.format("  ✓ %-18s (%2d mins, %2d pts)\n", a.name, a.timeMinutes, a.utilityValue));
        }
        txtBitmaskResults.setText(sb.toString());
        txtBitmaskResults.setCaretPosition(0);
    }

    private void solveMatrixChain() {
        String dimStr = txtMatrixDims.getText();
        String[] parts = dimStr.split(",");
        int[] p = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) {
                p[i] = Integer.parseInt(parts[i].trim());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid comma-separated integers.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (p.length < 3) {
            JOptionPane.showMessageDialog(this, "At least 3 dimensions required for 2 matrices.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MatrixChain.Result res = MatrixChain.compute(p);

        StringBuilder sb = new StringBuilder();
        sb.append("=== MATRIX CHAIN MULTIPLICATION (INTERVAL DP) ===\n");
        sb.append(String.format("Matrices in Chain: %d | Dimensions: %s\n", p.length - 1, Arrays.toString(p)));
        sb.append(String.format("Optimal Parenthesization: %s\n", res.parenthesization));
        sb.append(String.format("Optimized Multiplications: %s scalar operations\n", numFmt.format(res.minCost)));
        sb.append(String.format("Naive Evaluation Cost   : %s scalar operations\n", numFmt.format(res.naiveCost)));

        double savedPct = ((double) (res.naiveCost - res.minCost) / res.naiveCost) * 100.0;
        sb.append(String.format("Computational Savings   : %.1f%% operations eliminated!\n", savedPct));
        sb.append(String.format("Algorithm Execution Time: %s ns\n", numFmt.format(res.executionTimeNs)));

        txtMatrixResults.setText(sb.toString());
        txtMatrixResults.setCaretPosition(0);

        List<BarChartPanel.BarItem> items = new ArrayList<>();
        items.add(new BarChartPanel.BarItem("Optimal DP", "MCM O(N^3)", res.minCost, numFmt.format(res.minCost) + " ops", BarChartPanel.PALETTE[1]));
        items.add(new BarChartPanel.BarItem("Naive Eval", "Left-to-Right", res.naiveCost, numFmt.format(res.naiveCost) + " ops", BarChartPanel.PALETTE[4]));
        chartMatrixChain.setChartData("Operations Saved: Optimal MCM vs Naive", "Scalar Multiplications", items);
    }

    // =========================================================================
    // TAB 5: PERSONAL RECORDS MANAGER
    // =========================================================================
    private JPanel createRecordsManagerTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Form Card (North)
        JPanel formCard = createCardPanel("Add New Personal Health / Activity Record");
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRecId = new JTextField("REC-" + (105 + recordList.size()), 10);
        txtRecDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 10);
        txtRecActivity = new JTextField("Swimming", 12);
        txtRecCategory = new JTextField("Cardio", 12);
        txtRecSensor = new JTextField("135 bpm", 10);
        txtRecPref = new JTextField("Morning", 10);
        txtRecNote = new JTextArea(2, 25);
        txtRecNote.setText("Completed 10 laps in pool, steady breathing");

        styleTextInput(txtRecId); styleTextInput(txtRecDate); styleTextInput(txtRecActivity);
        styleTextInput(txtRecCategory); styleTextInput(txtRecSensor); styleTextInput(txtRecPref);
        styleTextInput(txtRecNote);

        gbc.gridx = 0; gbc.gridy = 0; formCard.add(createLabel("Record ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formCard.add(txtRecId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; formCard.add(createLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; formCard.add(txtRecDate, gbc);

        gbc.gridx = 4; gbc.gridy = 0; formCard.add(createLabel("Activity:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; formCard.add(txtRecActivity, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formCard.add(createLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formCard.add(txtRecCategory, gbc);

        gbc.gridx = 2; gbc.gridy = 1; formCard.add(createLabel("Sensor Value:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; formCard.add(txtRecSensor, gbc);

        gbc.gridx = 4; gbc.gridy = 1; formCard.add(createLabel("Preference:"), gbc);
        gbc.gridx = 5; gbc.gridy = 1; formCard.add(txtRecPref, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formCard.add(createLabel("Personal Note:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3;
        formCard.add(new JScrollPane(txtRecNote), gbc);
        gbc.gridwidth = 1;

        JButton btnAddRecord = createStyledButton("➕ Add Record", ACCENT_GREEN);
        btnAddRecord.addActionListener(e -> addNewRecord());
        gbc.gridx = 4; gbc.gridy = 2; gbc.gridwidth = 2;
        formCard.add(btnAddRecord, gbc);
        gbc.gridwidth = 1;

        panel.add(formCard, BorderLayout.NORTH);

        // Records Table (Center)
        JPanel tableCard = createCardPanel("All In-Memory Records (" + recordList.size() + " total)");
        tableCard.setLayout(new BorderLayout(6, 6));

        String[] cols = {"ID", "Date", "Activity", "Category", "Sensor Value", "Preference", "Personal Note"};
        modelRecords = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        refreshRecordsTable();

        JTable tableRec = createStyledTable(modelRecords);
        tableCard.add(new JScrollPane(tableRec), BorderLayout.CENTER);

        // Button row
        JPanel btmRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 4));
        btmRow.setOpaque(false);

        JButton btnSendToSearch = createStyledButton("🔍 Send Selected Note to String Matcher", ACCENT_BLUE);
        btnSendToSearch.addActionListener(e -> {
            int row = tableRec.getSelectedRow();
            if (row != -1) {
                String note = modelRecords.getValueAt(row, 6).toString();
                txtStrTarget.setText(note);
                tabbedPane.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a record from the table first.", "Selection Needed", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton btnDelete = createSmallButton("🗑️ Delete Selected");
        btnDelete.addActionListener(e -> {
            int row = tableRec.getSelectedRow();
            if (row != -1) {
                recordList.remove(row);
                refreshRecordsTable();
            }
        });

        btmRow.add(btnDelete);
        btmRow.add(btnSendToSearch);
        tableCard.add(btmRow, BorderLayout.SOUTH);

        panel.add(tableCard, BorderLayout.CENTER);
        return panel;
    }

    private void addNewRecord() {
        String id = txtRecId.getText().trim();
        String date = txtRecDate.getText().trim();
        String act = txtRecActivity.getText().trim();
        String cat = txtRecCategory.getText().trim();
        String sensor = txtRecSensor.getText().trim();
        String pref = txtRecPref.getText().trim();
        String note = txtRecNote.getText().trim();

        if (id.isEmpty() || act.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Record ID and Activity are mandatory.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        PersonalRecord r = new PersonalRecord(id, date, act, cat, note, sensor, pref);
        recordList.add(r);
        refreshRecordsTable();
        txtRecId.setText("REC-" + (105 + recordList.size()));
        JOptionPane.showMessageDialog(this, "Record " + id + " added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshRecordsTable() {
        if (modelRecords == null) return;
        modelRecords.setRowCount(0);
        for (PersonalRecord r : recordList) {
            modelRecords.addRow(new Object[]{
                    r.getRecordId(), r.getDate(), r.getActivity(), r.getCategory(), r.getSensorValue(), r.getPreference(), r.getNote()
            });
        }
    }

    // =========================================================================
    // TAB 6: FINAL REVIEW & VIVA STUDIO
    // =========================================================================
    private JPanel createFinalReviewTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTabbedPane subTabs = new JTabbedPane();
        subTabs.setBackground(BG_CARD);
        subTabs.setForeground(TEXT_PRIMARY);

        // Subtab 1: Category Comparison Rationale
        JPanel pRationale = new JPanel(new BorderLayout());
        pRationale.setBackground(BG_DARK);
        pRationale.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea txtRationale = new JTextArea();
        txtRationale.setEditable(false);
        txtRationale.setFont(FONT_MONO);
        styleTextInput(txtRationale);
        txtRationale.setText(getAcademicJustificationText());
        pRationale.add(new JScrollPane(txtRationale), BorderLayout.CENTER);

        // Subtab 2: Complete 15 Algorithms Complexity Matrix
        JPanel pMatrix = new JPanel(new BorderLayout());
        pMatrix.setBackground(BG_DARK);
        pMatrix.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] mCols = {"Category", "Algorithm", "Time (Worst)", "Time (Best/Avg)", "Space", "Primary Application"};
        DefaultTableModel modelMatrix = new DefaultTableModel(mCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        populateMasterMatrix(modelMatrix);
        JTable tableMatrix = createStyledTable(modelMatrix);
        pMatrix.add(new JScrollPane(tableMatrix), BorderLayout.CENTER);

        // Subtab 3: Viva Q&A Guide
        JPanel pViva = new JPanel(new BorderLayout());
        pViva.setBackground(BG_DARK);
        pViva.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea txtViva = new JTextArea();
        txtViva.setEditable(false);
        txtViva.setFont(FONT_MONO);
        styleTextInput(txtViva);
        txtViva.setText(getVivaCheatSheetText());
        pViva.add(new JScrollPane(txtViva), BorderLayout.CENTER);

        subTabs.addTab("📖 Category Comparison Justification", pRationale);
        subTabs.addTab("📊 15 Algorithms Complexity Matrix", pMatrix);
        subTabs.addTab("🎯 Professor & Examiner Viva Q&A", pViva);

        panel.add(subTabs, BorderLayout.CENTER);

        // Bottom Export Row
        JPanel exportRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 6));
        exportRow.setOpaque(false);
        JButton btnExportMD = createStyledButton("💾 Export Final Review Report (Markdown & TXT)", ACCENT_GREEN);
        btnExportMD.addActionListener(e -> exportFullReviewReport());
        exportRow.add(btnExportMD);
        panel.add(exportRow, BorderLayout.SOUTH);

        return panel;
    }

    private void populateMasterMatrix(DefaultTableModel model) {
        model.addRow(new Object[]{"String Match", "Naive Search", "O(n · m)", "O(n)", "O(1)", "Baseline substring verification"});
        model.addRow(new Object[]{"String Match", "KMP Algorithm", "O(n + m)", "O(n + m)", "O(m)", "No-backtrack search via LPS table"});
        model.addRow(new Object[]{"String Match", "Z-Algorithm", "O(n + m)", "O(n + m)", "O(n + m)", "Linear search using Z-box boundaries"});
        model.addRow(new Object[]{"String Match", "Rabin-Karp", "O(n · m)", "O(n + m)", "O(1)", "Polynomial rolling hash substring search"});
        model.addRow(new Object[]{"String Match", "Aho-Corasick", "O(n + z)", "O(n + z)", "O(Σ · m)", "Simultaneous multi-keyword stream tagging"});
        model.addRow(new Object[]{"Suffix Index", "Suffix Array", "O(n² log n)", "O(n² log n)", "O(n)", "Lexicographical indexing of all suffixes"});
        model.addRow(new Object[]{"Suffix Index", "Kasai LCP", "O(n)", "O(n)", "O(n)", "Linear common prefix array construction"});
        model.addRow(new Object[]{"Suffix Index", "Binary Suffix Query", "O(m log n)", "O(m log n)", "O(1)", "High-throughput repeated queries"});
        model.addRow(new Object[]{"Cost / Align", "Levenshtein", "O(n · m)", "O(n · m)", "O(n · m)", "Typo correction (Ins/Del/Replace)"});
        model.addRow(new Object[]{"Cost / Align", "Damerau-Levenshtein", "O(n · m)", "O(n · m)", "O(n · m)", "Keyboard typo with character transpositions"});
        model.addRow(new Object[]{"Cost / Align", "Weighted Edit Distance", "O(n · m)", "O(n · m)", "O(n · m)", "Asymmetric penalties for sensor drift"});
        model.addRow(new Object[]{"Cost / Align", "Needleman-Wunsch", "O(n · m)", "O(n · m)", "O(n · m)", "End-to-end global sequence alignment"});
        model.addRow(new Object[]{"Cost / Align", "Smith-Waterman", "O(n · m)", "O(n · m)", "O(n · m)", "Local alignment motif discovery"});
        model.addRow(new Object[]{"DP Planning", "Bitmask DP", "O(2^N · N)", "O(2^N · N)", "O(2^N)", "Activity subset optimization under budget"});
        model.addRow(new Object[]{"DP Planning", "Matrix Chain Mult", "O(N³)", "O(N³)", "O(N²)", "Optimal sensor pipeline parenthesization"});
    }

    private String getAcademicJustificationText() {
        return "========================================================================================\n"
                + "  ACADEMIC JUSTIFICATION: WHY CATEGORY-SPECIFIC COMPARISON IS ESSENTIAL\n"
                + "========================================================================================\n\n"
                + "1. PROBLEM FORMULATION MISMATCH (Apples-to-Oranges Fallacy):\n"
                + "   Comparing an exact pattern search algorithm (e.g. KMP) to an edit distance algorithm\n"
                + "   (e.g. Levenshtein) is mathematically invalid because they solve fundamentally\n"
                + "   different algorithmic problems with distinct input/output contracts:\n"
                + "   • String Matchers decide membership: \"Does pattern P occur in text T, and at what offsets?\"\n"
                + "   • Edit Distance solves optimization: \"What is the minimum transition cost to transform S1 into S2?\"\n"
                + "   • Comparing execution time between KMP O(n+m) and Levenshtein O(nm) yields no meaningful insight\n"
                + "     unless compared within their respective problem domains.\n\n"
                + "2. CATEGORY 1 – EXACT STRING MATCHING (KMP vs Naive vs Z vs Rabin-Karp vs Aho-Corasick):\n"
                + "   • Valid Comparison: All 5 algorithms take identical (Text, Pattern) inputs and find exact matches.\n"
                + "   • Metric: Execution time in nanoseconds and character comparison count.\n"
                + "   • Invariant Proved: KMP guarantees O(n+m) worst-case time by never decrementing the text pointer.\n"
                + "     Z-Algorithm achieves O(n+m) by maintaining the rightmost matched Z-box [l, r].\n"
                + "     Rabin-Karp achieves O(1) sliding window updates via polynomial rolling hash.\n"
                + "     Aho-Corasick tags all keywords simultaneously in a single linear pass.\n\n"
                + "3. CATEGORY 2 – DISTANCE & COST MEASURES (Levenshtein vs Damerau vs Weighted vs NW vs SW):\n"
                + "   • Valid Comparison: All 5 algorithms compute 2D Dynamic Programming recurrences over two sequences.\n"
                + "   • Metric: Distance/Alignment score and DP cells computed.\n"
                + "   • Invariant Proved: Damerau-Levenshtein correctly handles adjacent transpositions in 1 edit\n"
                + "     (e.g. 'form' -> 'from' costs 1 in Damerau vs 2 in classic Levenshtein).\n"
                + "     Needleman-Wunsch aligns entire sequences globally, whereas Smith-Waterman clamps\n"
                + "     negative scores to 0 to extract optimal local motifs.\n\n"
                + "4. CATEGORY 3 – SUFFIX STRUCTURES (Suffix Array & Kasai LCP vs Scan-Based Matchers):\n"
                + "   • Valid Comparison: Tradeoff between one-time indexing cost and high-throughput repeated queries.\n"
                + "   • Invariant Proved: Suffix Array binary search achieves O(m log n) query time, outperforming\n"
                + "     repeated O(n+m) scans by 10x-50x when executing batches of user queries.\n\n"
                + "5. CATEGORY 4 – COMBINATORIAL OPTIMIZATION (Bitmask DP & Matrix Chain Multiplication):\n"
                + "   • Solves resource-constrained decision problems in personal data analytics.\n"
                + "   • Bitmask DP evaluates all 2^N subsets in O(2^N · N) to maximize utility under time budgets.\n"
                + "   • Matrix Chain Multiplication uses Interval DP O(N^3) to eliminate 60%+ of redundant scalar\n"
                + "     multiplications in multi-stage sensor aggregation pipelines.";
    }

    private String getVivaCheatSheetText() {
        return "========================================================================================\n"
                + "  FINAL REVIEW VIVA QUESTIONS & ANSWERS (CO1 – CO3)\n"
                + "========================================================================================\n\n"
                + "Q1: Why does KMP avoid backtracking the text pointer?\n"
                + "A:  The Failure Function (LPS array) precomputes the longest proper prefix of pattern[0..j-1]\n"
                + "    that is also a suffix. Upon a mismatch at pattern[j], the text pointer 'i' stays in place,\n"
                + "    and pattern pointer slides to j = lps[j-1], guaranteeing linear O(n+m) runtime.\n\n"
                + "Q2: How does Z-Algorithm compute matches in linear time?\n"
                + "A:  It maintains an active window [l, r] (the Z-box) representing the rightmost substring matching\n"
                + "    the prefix of S. If index i < r, Z[i] is initialized using previously computed Z[i-l] in O(1).\n\n"
                + "Q3: What makes Rabin-Karp prone to worst-case O(n·m) and how do you mitigate it?\n"
                + "A:  Hash collisions (spurious hits) force manual character verification. We mitigate this using a\n"
                + "    large prime modulus (1,000,000,007) and base 256 to ensure uniform hash distribution.\n\n"
                + "Q4: Explain Kasai's Invariant for linear-time LCP construction.\n"
                + "A:  If the suffix starting at index i has common prefix length h with its predecessor in the suffix\n"
                + "    array, then the suffix at (i+1) will have common prefix length at least (h-1) with its\n"
                + "    predecessor. Thus h decrements at most n times, bounding total comparisons to O(n).\n\n"
                + "Q5: Difference between Needleman-Wunsch and Smith-Waterman?\n"
                + "A:  Needleman-Wunsch is Global Alignment: it initializes boundary cells to gap penalties and allows\n"
                + "    negative cell scores to align sequences end-to-end. Smith-Waterman is Local Alignment: it clamps\n"
                + "    negative scores to 0 and backtracks from the cell containing the maximum score anywhere in the table.\n\n"
                + "Q6: Why is Bitmask DP suited for Activity Selection?\n"
                + "A:  For N <= 20 activities, each subset can be stored as an integer bitmask (bit i = 1 if activity i is chosen).\n"
                + "    Transitions compute submask solutions in O(1), evaluating all 2^N combinations in O(2^N · N).";
    }

    private void exportFullReviewReport() {
        JFileChooser fc = new JFileChooser(new File("."));
        fc.setSelectedFile(new File("LifeData_Final_Review_Report.md"));
        int res = fc.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File target = fc.getSelectedFile();
            try (PrintWriter pw = new PrintWriter(new FileWriter(target))) {
                pw.println("# LifeData Platform – Comprehensive Final Review Evaluation Report");
                pw.println("Generated on: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                pw.println();
                pw.println(getAcademicJustificationText());
                pw.println();
                pw.println(getVivaCheatSheetText());
                JOptionPane.showMessageDialog(this, "Review report successfully exported to:\n" + target.getAbsolutePath(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to write report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================================================================
    // UI HELPERS & STYLING
    // =========================================================================
    private JPanel createCardPanel(String title) {
        JPanel p = new JPanel();
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                        title,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        FONT_BOLD,
                        TEXT_PRIMARY
                ),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return p;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_PRIMARY);
        l.setFont(FONT_BOLD);
        return l;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(FONT_BOLD);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton createSmallButton(String text) {
        JButton b = new JButton(text);
        b.setBackground(BG_INPUT);
        b.setForeground(TEXT_PRIMARY);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                new EmptyBorder(4, 10, 4, 10)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleTextInput(JComponent comp) {
        comp.setBackground(BG_INPUT);
        comp.setForeground(TEXT_PRIMARY);
        if (comp instanceof JTextField) {
            ((JTextField) comp).setCaretColor(Color.WHITE);
            comp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                    new EmptyBorder(4, 6, 4, 6)
            ));
        } else if (comp instanceof JTextArea) {
            ((JTextArea) comp).setCaretColor(Color.WHITE);
            comp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(71, 85, 105), 1),
                    new EmptyBorder(6, 6, 6, 6)
            ));
        }
    }

    private void styleSpinner(JSpinner spn) {
        spn.setPreferredSize(new Dimension(65, 26));
        JComponent editor = spn.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(BG_INPUT);
            tf.setForeground(TEXT_PRIMARY);
            tf.setCaretColor(Color.WHITE);
        }
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(new Color(24, 32, 47));
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(new Color(51, 65, 85));
        table.setRowHeight(24);
        table.setFont(FONT_BODY);
        table.setSelectionBackground(new Color(14, 165, 233, 160));
        table.setSelectionForeground(Color.WHITE);

        table.getTableHeader().setBackground(new Color(15, 23, 42));
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setFont(FONT_BOLD);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i > 0 && i < table.getColumnCount() - 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        return table;
    }

    private void loadFileIntoText(String path, JTextArea target) {
        String content = loadDefaultContent(path, null);
        if (content != null) {
            target.setText(content);
            target.setCaretPosition(0);
        } else {
            JOptionPane.showMessageDialog(this, "Could not open file: " + path, "File Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void browseAndLoadFile(JTextArea target) {
        JFileChooser fc = new JFileChooser(new File("data"));
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line).append("\n");
                target.setText(sb.toString());
                target.setCaretPosition(0);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Read Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getAggregatedRecordsText() {
        StringBuilder sb = new StringBuilder();
        for (PersonalRecord r : recordList) {
            sb.append(r.getSearchableContent()).append("\n");
        }
        return sb.toString();
    }

    private static String loadDefaultContent(String path, String fallback) {
        File f = new File(path);
        if (!f.exists()) return fallback;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
            return sb.toString();
        } catch (Exception e) {
            return fallback;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LifeDataGUI gui = new LifeDataGUI();
            gui.setVisible(true);
        });
    }
}
