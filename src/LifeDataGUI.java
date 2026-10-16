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
 * LifeData — Large-Scale Personal Data Analytics Platform.
 * Full Desktop Application for DSA College Hackathon.
 * Built 100% in Core Java & Java Swing (zero external dependencies).
 * Features 10 dedicated workspaces via CardLayout:
 *  1. Overview Dashboard
 *  2. Personal Data Explorer
 *  3. Module 1: String Intelligence
 *  4. Module 2: Suffix Structures
 *  5. Module 3: Dynamic Programming
 *  6. Module 4: Network Flow
 *  7. Module 5: Constraint Optimization
 *  8. Module 6: Randomized & Parallel
 *  9. Benchmark Laboratory
 * 10. Dataset Manager
 */
public class LifeDataGUI extends JFrame {

    private final DatasetService datasetService = new DatasetService();
    private final DecimalFormat numFmt = new DecimalFormat("#,###");
    private int benchmarkExecutionsCount = 0;

    // UI Colors - Modern Slate & Electric Accents
    public static final Color BG_DARK = new Color(15, 23, 42);       // #0f172a
    public static final Color BG_SIDEBAR = new Color(11, 17, 32);    // #0b1120
    public static final Color BG_CARD = new Color(30, 41, 59);       // #1e293b
    public static final Color BG_INPUT = new Color(51, 65, 85);      // #334155
    public static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    public static final Color TEXT_MUTED = new Color(148, 163, 184);
    public static final Color ACCENT_BLUE = new Color(14, 165, 233);
    public static final Color ACCENT_GREEN = new Color(16, 185, 129);
    public static final Color ACCENT_AMBER = new Color(245, 158, 11);
    public static final Color ACCENT_PURPLE = new Color(139, 92, 246);
    public static final Color ACCENT_ROSE = new Color(244, 63, 94);
    public static final Color ACCENT_CYAN = new Color(6, 182, 212);

    // Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 12);

    // Navigation & Layout
    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private JLabel lblHeaderTitle;
    private JLabel lblHeaderBreadcrumb;
    private JLabel lblStatusDataset;
    private JLabel lblStatusMem;
    private final JButton[] navButtons = new JButton[10];
    private int currentWorkspaceIdx = 0;

    // Component references - Module 1 (String Matching)
    private JTextArea txtStrTarget;
    private JTextField txtStrPattern;
    private JTextField txtStrAhoKeywords;
    private JComboBox<String> cmbStrMetric;
    private BarChartPanel chartStrMatch;
    private DefaultTableModel modelStrTable;
    private JTextArea txtStrDetails;

    // Component references - Module 2 (Suffix Structures)
    private JTextArea txtSuffixSource;
    private JTextField txtSuffixQuery;
    private BarChartPanel chartSuffix;
    private DefaultTableModel modelSuffixTable;
    private JLabel lblSuffixLRS;

    // Component references - Module 3 (DP)
    private JTextField txtDistStr1, txtDistStr2;
    private JSpinner spnInsertCost, spnDeleteCost, spnReplaceCost;
    private JSpinner spnMatchScore, spnMismatchScore, spnGapPenalty;
    private JComboBox<String> cmbDistMetric;
    private BarChartPanel chartDist;
    private DefaultTableModel modelDistTable;
    private JTextArea txtDistDetails;
    private DefaultTableModel modelActivities;
    private JSpinner spnMaxTime;
    private JTextArea txtBitmaskResults;
    private JTextField txtMatrixDims;
    private BarChartPanel chartMatrixChain;
    private JTextArea txtMatrixResults;

    // Component references - Module 4 (Network Flow)
    private GraphVisualizerPanel canvasFlow;
    private JTextArea txtFlowLog;
    private JLabel lblFlowResult;
    private NetworkFlow.FlowGraph activeFlowGraph;

    // Component references - Module 5 (Constraint Optimization)
    private GraphVisualizerPanel canvasConstraint;
    private JTextArea txtConstraintLog;
    private JLabel lblConstraintResult;
    private ConstraintOptimization.ReductionGraph activeReductionGraph;

    // Component references - Module 6 (Randomized & Parallel)
    private JTextArea txtQuickSortLog;
    private JTextArea txtReservoirLog;
    private JTextArea txtMillerRabinLog;
    private JTextArea txtBlellochLog;
    private DefaultTableModel modelBrent;
    private JLabel lblParallelReduceSummary;

    // Component references - Personal Data Explorer
    private DefaultTableModel modelExplorer;
    private JTable tableExplorer;
    private JTextField txtExplorerSearch;
    private JComboBox<String> cmbExplorerCategory;
    private JTextArea txtExplorerDetail;

    // Component references - Benchmark Lab
    private JComboBox<String> cmbBenchCategory;
    private JSpinner spnBenchSize;
    private JSpinner spnBenchTrials;
    private BarChartPanel chartBenchmark;
    private DefaultTableModel modelBenchmark;
    private JProgressBar progressBenchmark;

    // Component references - Dataset Manager
    private JLabel lblDatasetCount;
    private BarChartPanel chartDatasetDist;
    private JTextArea txtDatasetSummary;

    public LifeDataGUI() {
        super("LifeData — Large-Scale Personal Data Analytics Platform");
        setupLookAndFeel();
        buildUI();
        navigateToWorkspace(0);
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("TabbedPane.foreground", Color.BLACK);
        UIManager.put("TabbedPane.selectedForeground", Color.BLACK);
        UIManager.put("TabbedPane.unselectedForeground", Color.BLACK);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("TableHeader.foreground", Color.BLACK);
        UIManager.put("TableHeader.background", Color.WHITE);
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", Color.BLACK);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("Spinner.foreground", Color.BLACK);
        UIManager.put("Spinner.background", Color.WHITE);
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1320, 880);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        // 1. Left Sidebar
        JPanel sidebar = createSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // 2. Right Center Container: Top Header + CardLayout Workspaces + Bottom Status Bar
        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setBackground(BG_DARK);

        JPanel headerPanel = createHeaderPanel();
        rightContainer.add(headerPanel, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.setBackground(BG_DARK);

        // Register the 10 Workspaces
        cardsPanel.add(createOverviewDashboard(), "WS_0");
        cardsPanel.add(createDataExplorerWorkspace(), "WS_1");
        cardsPanel.add(createStringModuleWorkspace(), "WS_2");
        cardsPanel.add(createSuffixModuleWorkspace(), "WS_3");
        cardsPanel.add(createDynamicProgrammingWorkspace(), "WS_4");
        cardsPanel.add(createNetworkFlowWorkspace(), "WS_5");
        cardsPanel.add(createConstraintWorkspace(), "WS_6");
        cardsPanel.add(createRandomizedParallelWorkspace(), "WS_7");
        cardsPanel.add(createBenchmarkWorkspace(), "WS_8");
        cardsPanel.add(createDatasetManagerWorkspace(), "WS_9");

        rightContainer.add(cardsPanel, BorderLayout.CENTER);

        JPanel statusBar = createStatusBar();
        rightContainer.add(statusBar, BorderLayout.SOUTH);

        root.add(rightContainer, BorderLayout.CENTER);
        setContentPane(root);
    }

    // =========================================================================
    // SIDEBAR NAVIGATION
    // =========================================================================
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(230, 800));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(30, 41, 59)));

        // Sidebar Branding
        JPanel brandPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JLabel lblBrand = new JLabel("LIFEDATA");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBrand.setForeground(ACCENT_BLUE);

        JLabel lblTag = new JLabel("Personal Analytics Platform");
        lblTag.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTag.setForeground(TEXT_MUTED);

        brandPanel.add(lblBrand);
        brandPanel.add(lblTag);
        sidebar.add(brandPanel, BorderLayout.NORTH);

        // Nav Buttons in scrollable vertical list
        JPanel navList = new JPanel(new GridLayout(10, 1, 0, 4));
        navList.setOpaque(false);
        navList.setBorder(new EmptyBorder(6, 10, 10, 10));

        String[] navLabels = {
            "1. Overview Dashboard",
            "2. Data Explorer",
            "3. Mod 1: String Intelligence",
            "4. Mod 2: Suffix Structures",
            "5. Mod 3: Dynamic Programming",
            "6. Mod 4: Network Flow",
            "7. Mod 5: Constraint Optimization",
            "8. Mod 6: Randomized & Parallel",
            "9. Benchmark Laboratory",
            "10. Dataset Manager"
        };

        for (int i = 0; i < 10; i++) {
            final int idx = i;
            JButton btn = new JButton(navLabels[i]);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btn.setForeground(Color.BLACK);
            btn.setBackground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                    new EmptyBorder(6, 10, 6, 10)
            ));
            btn.addActionListener(e -> navigateToWorkspace(idx));
            navButtons[i] = btn;
            navList.add(btn);
        }

        JScrollPane scrollNav = new JScrollPane(navList);
        scrollNav.setOpaque(false);
        scrollNav.getViewport().setOpaque(false);
        scrollNav.setBorder(null);
        sidebar.add(scrollNav, BorderLayout.CENTER);

        // Sidebar Footer
        JPanel sideFoot = new JPanel(new FlowLayout(FlowLayout.CENTER));
        sideFoot.setOpaque(false);
        sideFoot.setBorder(new EmptyBorder(8, 8, 8, 8));
        JLabel lblVer = new JLabel("Core Java 17+ | Swing GUI");
        lblVer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVer.setForeground(new Color(100, 116, 139));
        sideFoot.add(lblVer);
        sidebar.add(sideFoot, BorderLayout.SOUTH);

        return sidebar;
    }

    private void navigateToWorkspace(int index) {
        currentWorkspaceIdx = index;
        cardLayout.show(cardsPanel, "WS_" + index);

        // Update button highlights - ensure black text on all buttons for maximum contrast
        for (int i = 0; i < 10; i++) {
            if (i == index) {
                navButtons[i].setBackground(new Color(186, 230, 253));
                navButtons[i].setForeground(Color.BLACK);
                navButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 12));
                navButtons[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(2, 132, 199), 2),
                        new EmptyBorder(5, 9, 5, 9)
                ));
            } else {
                navButtons[i].setBackground(Color.WHITE);
                navButtons[i].setForeground(Color.BLACK);
                navButtons[i].setFont(new Font("Segoe UI", Font.BOLD, 11));
                navButtons[i].setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                        new EmptyBorder(6, 10, 6, 10)
                ));
            }
        }

        // Update Header Titles
        String[] titles = {
            "Overview Dashboard — Personal Data Analytics Summary",
            "Personal Data Explorer — Filter, Search & Manage Records",
            "Module 1: String Intelligence — Exact Pattern Matchers & Multi-Keyword Stream Tagging",
            "Module 2: Suffix Structures — Lexicographical Suffix Array & Kasai LCP Mining",
            "Module 3: Advanced Dynamic Programming — Edit Distance & Matrix-Chain Optimization",
            "Module 4: Network Flow — Maximum Capacity, Dinic & Bipartite Task Allocation",
            "Module 5: Constraint Optimization — 3-SAT Solver, CLIQUE Reductions & Vertex Cover",
            "Module 6: Randomized & Parallel Algorithms — Reservoir Sampling, Miller-Rabin & Parallel Reduce",
            "Benchmark Laboratory — Microsecond & Nanosecond Algorithm Performance Evaluation",
            "Dataset Manager — Synthetic Personal Data Generation & CSV Pipeline"
        };
        lblHeaderTitle.setText(titles[index]);
        updateStatusBar();
    }

    private JPanel createHeaderPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(51, 65, 85)),
                new EmptyBorder(10, 18, 10, 18)
        ));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);

        lblHeaderTitle = new JLabel("Overview Dashboard");
        lblHeaderTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHeaderTitle.setForeground(TEXT_PRIMARY);

        lblHeaderBreadcrumb = new JLabel("LifeData Platform • \"Understand Your Data. Optimize Every Operation.\"");
        lblHeaderBreadcrumb.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblHeaderBreadcrumb.setForeground(ACCENT_BLUE);

        left.add(lblHeaderTitle);
        left.add(lblHeaderBreadcrumb);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton btnQuickBench = createSmallButton("Quick Benchmark");
        btnQuickBench.addActionListener(e -> navigateToWorkspace(8));

        JButton btnQuickExplore = createSmallButton("Data Explorer");
        btnQuickExplore.addActionListener(e -> navigateToWorkspace(1));

        right.add(btnQuickBench);
        right.add(btnQuickExplore);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(11, 17, 32));
        statusBar.setBorder(new EmptyBorder(5, 16, 5, 16));

        lblStatusDataset = new JLabel("Dataset: " + datasetService.getRecordCount() + " records in-memory");
        lblStatusDataset.setForeground(TEXT_MUTED);
        lblStatusDataset.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel rightStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightStatus.setOpaque(false);

        lblStatusMem = new JLabel("JVM Memory: checking...");
        lblStatusMem.setForeground(new Color(148, 163, 184));
        lblStatusMem.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JLabel lblTag = new JLabel("Core Java • Eclipse Desktop Runtime");
        lblTag.setForeground(ACCENT_GREEN);
        lblTag.setFont(new Font("Segoe UI", Font.BOLD, 11));

        rightStatus.add(lblStatusMem);
        rightStatus.add(lblTag);

        statusBar.add(lblStatusDataset, BorderLayout.WEST);
        statusBar.add(rightStatus, BorderLayout.EAST);
        updateStatusBar();
        return statusBar;
    }

    private void updateStatusBar() {
        if (lblStatusDataset != null) {
            lblStatusDataset.setText("Dataset: " + numFmt.format(datasetService.getRecordCount()) + 
                                     " records loaded | Executions: " + benchmarkExecutionsCount);
        }
        if (lblStatusMem != null) {
            long free = Runtime.getRuntime().freeMemory() / (1024 * 1024);
            long total = Runtime.getRuntime().totalMemory() / (1024 * 1024);
            lblStatusMem.setText("Heap: " + (total - free) + " MB / " + total + " MB");
        }
    }

    // =========================================================================
    // WORKSPACE 0: OVERVIEW DASHBOARD
    // =========================================================================
    private JPanel createOverviewDashboard() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top Summary Cards Grid (6 cards)
        JPanel cardsGrid = new JPanel(new GridLayout(1, 6, 8, 0));
        cardsGrid.setOpaque(false);

        cardsGrid.add(createMetricCard("TOTAL RECORDS", numFmt.format(datasetService.getRecordCount()), "In-Memory Records", ACCENT_BLUE));
        cardsGrid.add(createMetricCard("CATEGORIES", "6 Active", "Health, Cardio, Sleep", ACCENT_GREEN));
        cardsGrid.add(createMetricCard("DSA MODULES", "6 Workspaces", "Strings, DP, Flow, etc.", ACCENT_PURPLE));
        cardsGrid.add(createMetricCard("ALGORITHMS", "25+ Algorithms", "From-Scratch Core Java", ACCENT_AMBER));
        cardsGrid.add(createMetricCard("EXECUTIONS", String.valueOf(benchmarkExecutionsCount), "Runtime Operations", ACCENT_CYAN));
        cardsGrid.add(createMetricCard("DEPENDENCIES", "Zero Libs", "100% Standard JDK", ACCENT_ROSE));

        panel.add(cardsGrid, BorderLayout.NORTH);

        // Center Split: Category Chart (Left) + Quick Launchers (Right)
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 0));
        center.setOpaque(false);

        BarChartPanel chartOverview = new BarChartPanel();
        List<BarChartPanel.BarItem> items = new ArrayList<>();
        Map<String, Integer> dist = datasetService.getCategoryDistribution();
        int colorIdx = 0;
        for (Map.Entry<String, Integer> e : dist.entrySet()) {
            items.add(new BarChartPanel.BarItem(e.getKey(), "Count", e.getValue(), String.valueOf(e.getValue()), BarChartPanel.PALETTE[colorIdx % BarChartPanel.PALETTE.length]));
            colorIdx++;
        }
        chartOverview.setChartData("Personal Dataset Category Distribution", "Record Count", items);

        JPanel pLeft = createCardPanel("Dataset Category Breakdown");
        pLeft.setLayout(new BorderLayout());
        pLeft.add(chartOverview, BorderLayout.CENTER);

        // Right: Module Quick Launchers
        JPanel pRight = createCardPanel("Direct Access to 6 DSA Modules & Lab Workspaces");
        pRight.setLayout(new GridLayout(4, 2, 8, 8));

        String[][] quickModules = {
            {"String Intelligence", "KMP, Z-Algorithm, Aho-Corasick", "2"},
            {"Suffix Structures", "Suffix Array & Kasai LCP", "3"},
            {"Dynamic Programming", "Edit Distance & Matrix Chain", "4"},
            {"Network Flow", "Edmonds-Karp & Dinic Max Flow", "5"},
            {"Constraint Optimization", "3-SAT & Vertex Cover Reductions", "6"},
            {"Randomized & Parallel", "Reservoir, Miller-Rabin, Blelloch", "7"},
            {"Benchmark Laboratory", "Real-Time Nanosecond Performance", "8"},
            {"Dataset Manager", "Synthetic Generation & CSV I/O", "9"}
        };

        for (String[] m : quickModules) {
            JPanel qCard = new JPanel(new BorderLayout(4, 4));
            qCard.setBackground(new Color(24, 32, 47));
            qCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                    new EmptyBorder(8, 10, 8, 10)
            ));

            JLabel title = new JLabel(m[0]);
            title.setFont(new Font("Segoe UI", Font.BOLD, 12));
            title.setForeground(ACCENT_BLUE);

            JLabel sub = new JLabel(m[1]);
            sub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            sub.setForeground(TEXT_MUTED);

            final int target = Integer.parseInt(m[2]);
            JButton btnGo = createSmallButton("Open Workspace");
            btnGo.addActionListener(e -> navigateToWorkspace(target));

            qCard.add(title, BorderLayout.NORTH);
            qCard.add(sub, BorderLayout.CENTER);
            qCard.add(btnGo, BorderLayout.EAST);
            pRight.add(qCard);
        }

        center.add(pLeft);
        center.add(pRight);
        panel.add(center, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMetricCard(String label, String value, String sub, Color accent) {
        JPanel p = new JPanel(new GridLayout(3, 1, 0, 2));
        p.setBackground(BG_CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, accent),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JLabel lblL = new JLabel(label);
        lblL.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblL.setForeground(TEXT_MUTED);

        JLabel lblV = new JLabel(value);
        lblV.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblV.setForeground(Color.WHITE);

        JLabel lblS = new JLabel(sub);
        lblS.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblS.setForeground(accent);

        p.add(lblL);
        p.add(lblV);
        p.add(lblS);
        return p;
    }

    // =========================================================================
    // WORKSPACE 1: PERSONAL DATA EXPLORER
    // =========================================================================
    private JPanel createDataExplorerWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        // Top Filter Bar
        JPanel topBar = createCardPanel("Data Explorer Controls & Filtering");
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));

        topBar.add(createLabel("Category Filter:"));
        cmbExplorerCategory = new JComboBox<>(new String[]{"All Categories", "Fitness", "Cardio", "Mental Health", "Nutrition", "Sleep & Recovery", "Medical"});
        styleComboBox(cmbExplorerCategory);
        cmbExplorerCategory.addActionListener(e -> filterExplorerTable());
        topBar.add(cmbExplorerCategory);

        topBar.add(Box.createHorizontalStrut(10));
        topBar.add(createLabel("Search:"));
        txtExplorerSearch = new JTextField(16);
        styleTextInput(txtExplorerSearch);
        txtExplorerSearch.addActionListener(e -> filterExplorerTable());
        topBar.add(txtExplorerSearch);

        JButton btnSearch = createSmallButton("Filter");
        btnSearch.addActionListener(e -> filterExplorerTable());
        topBar.add(btnSearch);

        JButton btnResetFilter = createSmallButton("Reset Filters");
        btnResetFilter.addActionListener(e -> {
            txtExplorerSearch.setText("");
            cmbExplorerCategory.setSelectedIndex(0);
            refreshExplorerTable();
        });
        topBar.add(btnResetFilter);

        topBar.add(Box.createHorizontalStrut(15));
        JButton btnAddRec = createStyledButton("Add Record", ACCENT_GREEN);
        btnAddRec.addActionListener(e -> showAddRecordDialog());
        topBar.add(btnAddRec);

        JButton btnDeleteRec = createSmallButton("Delete Selected");
        btnDeleteRec.addActionListener(e -> deleteSelectedExplorerRecord());
        topBar.add(btnDeleteRec);

        panel.add(topBar, BorderLayout.NORTH);

        // Center Table
        String[] cols = {"Record ID", "Date", "Activity", "Category", "Sensor Value", "Preference", "Tags", "Personal Note"};
        modelExplorer = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        refreshExplorerTable();

        tableExplorer = createStyledTable(modelExplorer);
        tableExplorer.getSelectionModel().addListSelectionListener(e -> {
            int row = tableExplorer.getSelectedRow();
            if (row != -1 && row < datasetService.getRecords().size()) {
                PersonalRecord r = datasetService.getRecords().get(row);
                txtExplorerDetail.setText(r.getSearchableContent() + "\nMetadata: " + r.getMetadata());
            }
        });

        JScrollPane scrollTable = new JScrollPane(tableExplorer);
        scrollTable.getViewport().setBackground(Color.WHITE);

        // Bottom Detail Preview
        JPanel btmCard = createCardPanel("Selected Record Inspector & Forwarding");
        btmCard.setLayout(new BorderLayout(6, 6));

        txtExplorerDetail = new JTextArea(3, 40);
        txtExplorerDetail.setEditable(false);
        txtExplorerDetail.setFont(FONT_MONO);
        styleTextInput(txtExplorerDetail);
        txtExplorerDetail.setText("Select any record above to inspect complete raw telemetry and metadata.");

        JPanel btmActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btmActions.setOpaque(false);

        JButton btnSendToStr = createStyledButton("Forward Note to String Matcher", ACCENT_BLUE);
        btnSendToStr.addActionListener(e -> {
            int row = tableExplorer.getSelectedRow();
            if (row != -1 && row < datasetService.getRecords().size()) {
                PersonalRecord r = datasetService.getRecords().get(row);
                txtStrTarget.setText(r.getNote());
                navigateToWorkspace(2); // String workspace
            } else {
                JOptionPane.showMessageDialog(this, "Select a record first.");
            }
        });

        JButton btnSendToSuffix = createStyledButton("Forward Note to Suffix Indexer", ACCENT_PURPLE);
        btnSendToSuffix.addActionListener(e -> {
            int row = tableExplorer.getSelectedRow();
            if (row != -1 && row < datasetService.getRecords().size()) {
                PersonalRecord r = datasetService.getRecords().get(row);
                txtSuffixSource.setText(r.getNote());
                navigateToWorkspace(3); // Suffix workspace
            } else {
                JOptionPane.showMessageDialog(this, "Select a record first.");
            }
        });

        btmActions.add(btnSendToStr);
        btmActions.add(btnSendToSuffix);

        btmCard.add(new JScrollPane(txtExplorerDetail), BorderLayout.CENTER);
        btmCard.add(btmActions, BorderLayout.EAST);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollTable, btmCard);
        split.setResizeWeight(0.72);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private void refreshExplorerTable() {
        if (modelExplorer == null) return;
        modelExplorer.setRowCount(0);
        for (PersonalRecord r : datasetService.getRecords()) {
            modelExplorer.addRow(new Object[]{
                r.getRecordId(), r.getDate(), r.getActivity(), r.getCategory(),
                r.getSensorValue(), r.getPreference(), r.getTags(), r.getNote()
            });
        }
    }

    private void filterExplorerTable() {
        String query = txtExplorerSearch.getText().trim().toLowerCase();
        String cat = (String) cmbExplorerCategory.getSelectedItem();
        boolean filterCat = cat != null && !cat.equals("All Categories");

        modelExplorer.setRowCount(0);
        for (PersonalRecord r : datasetService.getRecords()) {
            boolean matchCat = !filterCat || r.getCategory().equalsIgnoreCase(cat);
            boolean matchQuery = query.isEmpty() || r.getSearchableContent().toLowerCase().contains(query);
            if (matchCat && matchQuery) {
                modelExplorer.addRow(new Object[]{
                    r.getRecordId(), r.getDate(), r.getActivity(), r.getCategory(),
                    r.getSensorValue(), r.getPreference(), r.getTags(), r.getNote()
                });
            }
        }
    }

    private void showAddRecordDialog() {
        JTextField tfId = new JTextField("REC-" + (100 + datasetService.getRecordCount() + 1), 10);
        JTextField tfDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 10);
        JTextField tfAct = new JTextField("Cycling", 12);
        JTextField tfCat = new JTextField("Cardio", 12);
        JTextField tfSensor = new JTextField("140 bpm", 10);
        JTextField tfPref = new JTextField("Outdoor", 10);
        JTextField tfTags = new JTextField("cycling, endurance", 12);
        JTextArea taNote = new JTextArea("Afternoon cycling session covering 12 km trail.", 2, 20);

        JPanel p = new JPanel(new GridLayout(8, 2, 4, 4));
        p.add(new JLabel("Record ID:")); p.add(tfId);
        p.add(new JLabel("Date (YYYY-MM-DD):")); p.add(tfDate);
        p.add(new JLabel("Activity:")); p.add(tfAct);
        p.add(new JLabel("Category:")); p.add(tfCat);
        p.add(new JLabel("Sensor Value:")); p.add(tfSensor);
        p.add(new JLabel("Preference:")); p.add(tfPref);
        p.add(new JLabel("Tags:")); p.add(tfTags);
        p.add(new JLabel("Personal Note:")); p.add(new JScrollPane(taNote));

        int res = JOptionPane.showConfirmDialog(this, p, "Add New Personal Record", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            PersonalRecord r = new PersonalRecord(tfId.getText().trim(), tfDate.getText().trim(),
                    tfAct.getText().trim(), tfCat.getText().trim(), taNote.getText().trim(),
                    tfSensor.getText().trim(), tfPref.getText().trim(), tfTags.getText().trim(), "source=manual");
            datasetService.addRecord(r);
            refreshExplorerTable();
            updateStatusBar();
            JOptionPane.showMessageDialog(this, "Record successfully added!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedExplorerRecord() {
        int row = tableExplorer.getSelectedRow();
        if (row != -1 && row < datasetService.getRecords().size()) {
            int res = JOptionPane.showConfirmDialog(this, "Delete selected record from dataset?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                datasetService.deleteRecord(row);
                refreshExplorerTable();
                updateStatusBar();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a record to delete.");
        }
    }

    // =========================================================================
    // WORKSPACE 2: MODULE 1 — STRING INTELLIGENCE
    // =========================================================================
    private JPanel createStringModuleWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topCard = createCardPanel("Module 1: Exact Pattern Search & Multi-Keyword Stream Inputs");
        topCard.setLayout(new BorderLayout(6, 6));

        JPanel inputs = new JPanel(new GridBagLayout());
        inputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Target text loader buttons
        gbc.gridx = 0; gbc.gridy = 0; inputs.add(createLabel("Text Data Source:"), gbc);
        JPanel fileRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        fileRow.setOpaque(false);

        JButton btnLoadNotes = createSmallButton("Load personal_notes.txt");
        btnLoadNotes.addActionListener(e -> loadFileIntoText("data/personal_notes.txt", txtStrTarget));
        JButton btnLoadLarge = createSmallButton("Load sample_large_text.txt");
        btnLoadLarge.addActionListener(e -> loadFileIntoText("data/sample_large_text.txt", txtStrTarget));
        JButton btnUseDataset = createSmallButton("Use Active Dataset Records");
        btnUseDataset.addActionListener(e -> txtStrTarget.setText(datasetService.getAllNotesAggregated()));
        JButton btnBrowse = createSmallButton("Browse TXT...");
        btnBrowse.addActionListener(e -> browseAndLoadFile(txtStrTarget));

        fileRow.add(btnLoadNotes);
        fileRow.add(btnLoadLarge);
        fileRow.add(btnUseDataset);
        fileRow.add(btnBrowse);
        gbc.gridx = 1; gbc.gridy = 0; inputs.add(fileRow, gbc);

        // Row 1: Target text area
        txtStrTarget = new JTextArea(4, 50);
        txtStrTarget.setLineWrap(true);
        txtStrTarget.setWrapStyleWord(true);
        txtStrTarget.setFont(FONT_MONO);
        txtStrTarget.setText(loadDefaultContent("data/personal_notes.txt",
                "Patient completed morning exercise and walking for 5000 steps. Heart rate stable during exercise."));
        styleTextInput(txtStrTarget);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        inputs.add(new JScrollPane(txtStrTarget), gbc);
        gbc.gridwidth = 1;

        // Row 2: Search pattern
        gbc.gridx = 0; gbc.gridy = 2; inputs.add(createLabel("Single Pattern (KMP, Z, Naive, RK):"), gbc);
        txtStrPattern = new JTextField("exercise", 20);
        styleTextInput(txtStrPattern);
        gbc.gridx = 1; gbc.gridy = 2; inputs.add(txtStrPattern, gbc);

        // Row 3: Aho-Corasick Keywords
        gbc.gridx = 0; gbc.gridy = 3; inputs.add(createLabel("Aho-Corasick Multi-Keywords (CSV):"), gbc);
        txtStrAhoKeywords = new JTextField("exercise, walking, health, rate, steps", 20);
        styleTextInput(txtStrAhoKeywords);
        gbc.gridx = 1; gbc.gridy = 3; inputs.add(txtStrAhoKeywords, gbc);

        // Row 4: Run row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        actionRow.setOpaque(false);

        JButton btnRun = createStyledButton("Run & Compare 5 Algorithms", ACCENT_BLUE);
        btnRun.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRun.addActionListener(e -> runStringMatchingBenchmark());

        actionRow.add(btnRun);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(createLabel("Chart Metric:"));

        cmbStrMetric = new JComboBox<>(new String[]{
            "Execution Time (Nanoseconds)",
            "Character Comparisons / Operations",
            "Matches Found"
        });
        styleComboBox(cmbStrMetric);
        cmbStrMetric.addActionListener(e -> updateStringMatchingChart());
        actionRow.add(cmbStrMetric);

        topCard.add(inputs, BorderLayout.CENTER);
        topCard.add(actionRow, BorderLayout.SOUTH);
        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Chart on Left, Table and Details on Right
        JSplitPane splitCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCenter.setResizeWeight(0.5);
        splitCenter.setDividerSize(6);
        splitCenter.setBackground(BG_DARK);

        chartStrMatch = new BarChartPanel();
        chartStrMatch.setChartData("String Matching Benchmark", "Execution Time (ns)", null);
        splitCenter.setLeftComponent(chartStrMatch);

        JPanel rightCard = createCardPanel("Benchmark Metrics & Output Verification");
        rightCard.setLayout(new BorderLayout(6, 6));

        String[] cols = {"Algorithm", "Time (ns)", "Comparisons", "Matches", "Time Big-O", "Space Big-O"};
        modelStrTable = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableStr = createStyledTable(modelStrTable);
        JScrollPane spTable = new JScrollPane(tableStr);
        spTable.getViewport().setBackground(Color.WHITE);
        spTable.setPreferredSize(new Dimension(480, 140));

        txtStrDetails = new JTextArea();
        txtStrDetails.setEditable(false);
        txtStrDetails.setFont(FONT_MONO);
        styleTextInput(txtStrDetails);
        txtStrDetails.setText("Click 'Run & Compare 5 Algorithms' above to benchmark exact pattern search.\n\n"
                + "Why Compare String Matching Algorithms?\n"
                + "• All 5 algorithms solve exact pattern search over text.\n"
                + "• Compares KMP O(n+m) (avoiding pointer backtracking) vs Naive O(nm) vs Z-Algorithm vs Aho-Corasick.");

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spTable, new JScrollPane(txtStrDetails));
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
        String csv = txtStrAhoKeywords.getText();

        if (text.isEmpty() || pattern.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Target text and pattern cannot be empty.");
            return;
        }

        List<String> keywords = new ArrayList<>();
        for (String k : csv.split(",")) {
            String trimmed = k.trim();
            if (!trimmed.isEmpty()) keywords.add(trimmed);
        }
        if (keywords.isEmpty()) keywords.add(pattern);

        modelStrTable.setRowCount(0);
        benchmarkExecutionsCount += 5;
        updateStatusBar();

        // 1. Naive
        long t0 = System.nanoTime();
        NaiveSearch.SearchResult rNaive = NaiveSearch.search(text, pattern);
        long timeNaive = System.nanoTime() - t0;
        modelStrTable.addRow(new Object[]{"Naive Search", numFmt.format(timeNaive), numFmt.format(rNaive.comparisons), rNaive.positions.size(), "O(n · m)", "O(1)"});

        // 2. KMP
        t0 = System.nanoTime();
        KMP.KmpResult rKMP = KMP.search(text, pattern);
        long timeKMP = System.nanoTime() - t0;
        modelStrTable.addRow(new Object[]{"KMP", numFmt.format(timeKMP), numFmt.format(rKMP.comparisons), rKMP.positions.size(), "O(n + m)", "O(m)"});

        // 3. Z-Algorithm
        t0 = System.nanoTime();
        ZAlgorithm.ZResult rZ = ZAlgorithm.search(text, pattern);
        long timeZ = System.nanoTime() - t0;
        modelStrTable.addRow(new Object[]{"Z-Algorithm", numFmt.format(timeZ), numFmt.format(rZ.comparisons), rZ.positions.size(), "O(n + m)", "O(n + m)"});

        // 4. Rabin-Karp
        t0 = System.nanoTime();
        RabinKarp.RkResult rRK = RabinKarp.search(text, pattern);
        long timeRK = System.nanoTime() - t0;
        modelStrTable.addRow(new Object[]{"Rabin-Karp", numFmt.format(timeRK), numFmt.format(rRK.comparisons), rRK.positions.size(), "O(n + m) avg", "O(1)"});

        // 5. Aho-Corasick
        t0 = System.nanoTime();
        AhoCorasick ac = new AhoCorasick(keywords);
        AhoCorasick.MatchResult rAC = ac.search(text);
        long timeAC = System.nanoTime() - t0;
        modelStrTable.addRow(new Object[]{"Aho-Corasick", numFmt.format(timeAC), numFmt.format(rAC.transitions), rAC.getTotalMatches(), "O(n + z)", "O(Σ · m)"});

        // Details Log
        StringBuilder sb = new StringBuilder();
        sb.append("=== PATTERN SEARCH VERIFICATION ===\n");
        sb.append("Pattern: '").append(pattern).append("' | Text Length: ").append(text.length()).append(" chars\n");
        sb.append("KMP Match Offsets: ").append(rKMP.positions).append("\n");
        sb.append("KMP LPS Failure Table: ").append(Arrays.toString(rKMP.lps)).append("\n");
        sb.append("\n=== AHO-CORASICK MULTI-KEYWORD HITS ===\n");
        for (Map.Entry<String, List<Integer>> e : rAC.keywordPositions.entrySet()) {
            sb.append(String.format("  • '%s': %d occurrences at %s\n", e.getKey(), e.getValue().size(), e.getValue()));
        }
        txtStrDetails.setText(sb.toString());
        updateStringMatchingChart();
    }

    private void updateStringMatchingChart() {
        if (modelStrTable == null || modelStrTable.getRowCount() < 5) return;
        int metricIdx = cmbStrMetric.getSelectedIndex();
        List<BarChartPanel.BarItem> items = new ArrayList<>();
        String[] algs = {"Naive Search", "KMP", "Z-Algorithm", "Rabin-Karp", "Aho-Corasick"};
        String[] bigO = {"O(nm)", "O(n+m)", "O(n+m)", "O(n+m)", "O(n+z)"};

        for (int i = 0; i < 5; i++) {
            double val = 0;
            String display = "";
            try {
                if (metricIdx == 0) { // Time
                    String s = modelStrTable.getValueAt(i, 1).toString().replace(",", "");
                    val = Double.parseDouble(s);
                    display = numFmt.format(val) + " ns";
                } else if (metricIdx == 1) { // Comparisons
                    String s = modelStrTable.getValueAt(i, 2).toString().replace(",", "");
                    val = Double.parseDouble(s);
                    display = numFmt.format(val) + " ops";
                } else { // Matches
                    String s = modelStrTable.getValueAt(i, 3).toString();
                    val = Double.parseDouble(s);
                    display = String.valueOf((int) val);
                }
            } catch (Exception ignored) {}
            items.add(new BarChartPanel.BarItem(algs[i], bigO[i], val, display, BarChartPanel.PALETTE[i % BarChartPanel.PALETTE.length]));
        }

        String yLabel = (metricIdx == 0) ? "Execution Time (ns)" : ((metricIdx == 1) ? "Character Comparisons / Ops" : "Matches Found");
        chartStrMatch.setChartData("String Matching Benchmark", yLabel, items);
    }

    // =========================================================================
    // WORKSPACE 3: MODULE 2 — SUFFIX STRUCTURES
    // =========================================================================
    private JPanel createSuffixModuleWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topCard = createCardPanel("Module 2: Suffix Array Indexing & Kasai LCP Mining");
        topCard.setLayout(new BorderLayout(6, 6));

        JPanel inputs = new JPanel(new GridBagLayout());
        inputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; inputs.add(createLabel("Source Text to Index:"), gbc);
        txtSuffixSource = new JTextArea(3, 50);
        txtSuffixSource.setLineWrap(true);
        txtSuffixSource.setWrapStyleWord(true);
        txtSuffixSource.setFont(FONT_MONO);
        txtSuffixSource.setText(loadDefaultContent("data/personal_notes.txt",
                "Patient completed morning exercise and walking for 5000 steps. Heart rate stable during exercise."));
        styleTextInput(txtSuffixSource);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; inputs.add(new JScrollPane(txtSuffixSource), gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 2; inputs.add(createLabel("Substring Query:"), gbc);
        txtSuffixQuery = new JTextField("exercise", 20);
        styleTextInput(txtSuffixQuery);
        gbc.gridx = 1; gbc.gridy = 2; inputs.add(txtSuffixQuery, gbc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        btnRow.setOpaque(false);
        JButton btnBuild = createStyledButton("Construct Suffix Array & Run Repeated Query Test", ACCENT_PURPLE);
        btnBuild.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuild.addActionListener(e -> runSuffixBenchmark());
        btnRow.add(btnBuild);

        lblSuffixLRS = new JLabel("Longest Repeated Substring: (Not yet built)");
        lblSuffixLRS.setForeground(ACCENT_AMBER);
        lblSuffixLRS.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRow.add(lblSuffixLRS);

        topCard.add(inputs, BorderLayout.CENTER);
        topCard.add(btnRow, BorderLayout.SOUTH);
        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Chart vs Suffix Table
        JSplitPane splitCenter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitCenter.setResizeWeight(0.5);
        splitCenter.setDividerSize(6);
        splitCenter.setBackground(BG_DARK);

        chartSuffix = new BarChartPanel();
        chartSuffix.setChartData("Suffix Query vs Repeated Scans", "Execution Time (ns)", null);
        splitCenter.setLeftComponent(chartSuffix);

        JPanel rightCard = createCardPanel("Suffix Array & Kasai LCP Table");
        rightCard.setLayout(new BorderLayout(6, 6));

        String[] cols = {"Rank", "SA[i]", "LCP[i]", "Suffix Preview"};
        modelSuffixTable = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableSuffix = createStyledTable(modelSuffixTable);
        JScrollPane spSuffix = new JScrollPane(tableSuffix);
        spSuffix.getViewport().setBackground(Color.WHITE);
        rightCard.add(spSuffix, BorderLayout.CENTER);

        splitCenter.setRightComponent(rightCard);
        panel.add(splitCenter, BorderLayout.CENTER);

        return panel;
    }

    private void runSuffixBenchmark() {
        String text = txtSuffixSource.getText().trim();
        String query = txtSuffixQuery.getText().trim();
        if (text.isEmpty() || query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Text and query cannot be empty.");
            return;
        }

        benchmarkExecutionsCount += 3;
        updateStatusBar();

        // Build Suffix Array & LCP
        long t0 = System.nanoTime();
        SuffixArray saObj = new SuffixArray(text);
        int[] sa = saObj.getSuffixArray();
        int[] lcp = KasaiLCP.buildLCP(text, sa);
        long buildTime = System.nanoTime() - t0;

        String lrs = KasaiLCP.getLongestRepeatedSubstring(text, sa, lcp);
        lblSuffixLRS.setText("Longest Repeated Phrase (LRS): \"" + (lrs.isEmpty() ? "None" : lrs) + "\" (len " + lrs.length() + ")");

        // Populate Table
        modelSuffixTable.setRowCount(0);
        int limit = Math.min(sa.length, 50); // limit preview for speed
        for (int i = 0; i < limit; i++) {
            String preview = text.substring(sa[i]);
            if (preview.length() > 40) preview = preview.substring(0, 40) + "...";
            modelSuffixTable.addRow(new Object[]{i, sa[i], lcp[i], preview});
        }

        // Compare 100 repeated queries: Suffix Binary Search O(m log n) vs Repeated KMP scans O(n+m)
        int queryRuns = 100;
        long tBS = System.nanoTime();
        int bsMatches = 0;
        for (int i = 0; i < queryRuns; i++) {
            List<Integer> res = saObj.searchSubstring(query);
            bsMatches = res.size();
        }
        long timeBS = (System.nanoTime() - tBS) / queryRuns;

        long tScan = System.nanoTime();
        for (int i = 0; i < queryRuns; i++) {
            KMP.search(text, query);
        }
        long timeScan = (System.nanoTime() - tScan) / queryRuns;

        List<BarChartPanel.BarItem> items = new ArrayList<>();
        items.add(new BarChartPanel.BarItem("Suffix Binary Search", "O(m log n)", timeBS, numFmt.format(timeBS) + " ns", ACCENT_BLUE));
        items.add(new BarChartPanel.BarItem("Repeated KMP Scan", "O(n + m)", timeScan, numFmt.format(timeScan) + " ns", ACCENT_ROSE));
        items.add(new BarChartPanel.BarItem("One-Time SA Build", "O(n log n)", buildTime / 10, numFmt.format(buildTime) + " ns", ACCENT_PURPLE));

        chartSuffix.setChartData("Repeated Query Speed: Suffix Array vs Full Scans", "Average Query Time (ns)", items);
    }

    // =========================================================================
    // WORKSPACE 4: MODULE 3 — ADVANCED DYNAMIC PROGRAMMING
    // =========================================================================
    private JPanel createDynamicProgrammingWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTabbedPane dpTabs = new JTabbedPane();
        dpTabs.setForeground(Color.BLACK);
        dpTabs.setFont(FONT_BOLD);

        // Subtab A: Distance & Alignment
        dpTabs.addTab("1. Distance & Sequence Alignment", createDistanceSubTab());
        // Subtab B: Bitmask DP
        dpTabs.addTab("2. Bitmask DP Activity Selection", createBitmaskSubTab());
        // Subtab C: Matrix-Chain Multiplication
        dpTabs.addTab("3. Matrix-Chain Multiplication", createMCMSubTab());

        panel.add(dpTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createDistanceSubTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(BG_DARK);

        JPanel topCard = createCardPanel("String Distance & Alignment Configuration");
        topCard.setLayout(new BorderLayout(6, 6));

        JPanel inputs = new JPanel(new GridBagLayout());
        inputs.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; inputs.add(createLabel("String 1:"), gbc);
        txtDistStr1 = new JTextField("cardiac arrhytmia", 20);
        styleTextInput(txtDistStr1);
        gbc.gridx = 1; gbc.gridy = 0; inputs.add(txtDistStr1, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputs.add(createLabel("String 2:"), gbc);
        txtDistStr2 = new JTextField("cardiac arrhythmia", 20);
        styleTextInput(txtDistStr2);
        gbc.gridx = 1; gbc.gridy = 1; inputs.add(txtDistStr2, gbc);

        // Action row
        JPanel actRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        actRow.setOpaque(false);

        JButton btnRun = createStyledButton("Compute & Compare 5 Distance/Alignment Algorithms", ACCENT_GREEN);
        btnRun.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRun.addActionListener(e -> runDistanceMeasuresBenchmark());
        actRow.add(btnRun);

        topCard.add(inputs, BorderLayout.CENTER);
        topCard.add(actRow, BorderLayout.SOUTH);
        panel.add(topCard, BorderLayout.NORTH);

        // Center Split
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);

        chartDist = new BarChartPanel();
        chartDist.setChartData("Distance & Alignment Benchmark", "Execution Time (ns)", null);
        split.setLeftComponent(chartDist);

        JPanel rightCard = createCardPanel("DP Metrics & Results");
        rightCard.setLayout(new BorderLayout(6, 6));

        String[] cols = {"Algorithm", "Score / Distance", "Time (ns)", "DP Cells Computed"};
        modelDistTable = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableDist = createStyledTable(modelDistTable);
        JScrollPane sp = new JScrollPane(tableDist);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setPreferredSize(new Dimension(460, 140));

        txtDistDetails = new JTextArea();
        txtDistDetails.setEditable(false);
        txtDistDetails.setFont(FONT_MONO);
        styleTextInput(txtDistDetails);
        txtDistDetails.setText("Click 'Compute & Compare 5 Algorithms' to evaluate Levenshtein, Damerau, Weighted, NW, and SW.");

        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, new JScrollPane(txtDistDetails));
        rightSplit.setResizeWeight(0.45);
        rightSplit.setDividerSize(5);
        rightSplit.setBackground(BG_DARK);

        rightCard.add(rightSplit, BorderLayout.CENTER);
        split.setRightComponent(rightCard);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private void runDistanceMeasuresBenchmark() {
        String s1 = txtDistStr1.getText();
        String s2 = txtDistStr2.getText();
        if (s1.isEmpty() || s2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Strings cannot be empty.");
            return;
        }

        benchmarkExecutionsCount += 5;
        updateStatusBar();
        modelDistTable.setRowCount(0);

        // 1. Levenshtein
        long t0 = System.nanoTime();
        Levenshtein.Result rLev = Levenshtein.compute(s1, s2);
        long timeLev = System.nanoTime() - t0;
        modelDistTable.addRow(new Object[]{"Levenshtein", rLev.distance, numFmt.format(timeLev), (s1.length()+1)*(s2.length()+1)});

        // 2. Damerau
        t0 = System.nanoTime();
        DamerauLevenshtein.Result rDam = DamerauLevenshtein.compute(s1, s2);
        long timeDam = System.nanoTime() - t0;
        modelDistTable.addRow(new Object[]{"Damerau-Levenshtein", rDam.distance, numFmt.format(timeDam), (s1.length()+1)*(s2.length()+1)});

        // 3. Weighted
        t0 = System.nanoTime();
        WeightedEditDistance.Result rW = WeightedEditDistance.compute(s1, s2, 1.0, 1.5, 2.0);
        long timeW = System.nanoTime() - t0;
        modelDistTable.addRow(new Object[]{"Weighted Edit Distance", String.format("%.2f", rW.totalCost), numFmt.format(timeW), (s1.length()+1)*(s2.length()+1)});

        // 4. Needleman-Wunsch
        t0 = System.nanoTime();
        NeedlemanWunsch.AlignmentResult rNW = NeedlemanWunsch.align(s1, s2, 1, -1, -1);
        long timeNW = System.nanoTime() - t0;
        modelDistTable.addRow(new Object[]{"Needleman-Wunsch (Global)", rNW.score, numFmt.format(timeNW), (s1.length()+1)*(s2.length()+1)});

        // 5. Smith-Waterman
        t0 = System.nanoTime();
        SmithWaterman.AlignmentResult rSW = SmithWaterman.align(s1, s2, 2, -1, -1);
        long timeSW = System.nanoTime() - t0;
        modelDistTable.addRow(new Object[]{"Smith-Waterman (Local)", rSW.maxScore, numFmt.format(timeSW), (s1.length()+1)*(s2.length()+1)});

        // Details
        StringBuilder sb = new StringBuilder();
        sb.append("=== ALIGNMENT & TYPO DETAILS ===\n");
        sb.append("Damerau Transposition detected: ").append(rDam.usedTransposition).append("\n");
        sb.append("Needleman-Wunsch Global Alignment:\n  Seq1: ").append(rNW.alignedSeq1).append("\n  Seq2: ").append(rNW.alignedSeq2).append("\n");
        sb.append("Smith-Waterman Local Alignment Motif:\n  Sub1: ").append(rSW.alignedSeq1).append("\n  Sub2: ").append(rSW.alignedSeq2).append("\n");
        txtDistDetails.setText(sb.toString());

        List<BarChartPanel.BarItem> items = new ArrayList<>();
        items.add(new BarChartPanel.BarItem("Levenshtein", "O(nm)", timeLev, numFmt.format(timeLev) + " ns", ACCENT_BLUE));
        items.add(new BarChartPanel.BarItem("Damerau-Lev", "O(nm)", timeDam, numFmt.format(timeDam) + " ns", ACCENT_GREEN));
        items.add(new BarChartPanel.BarItem("Weighted Edit", "O(nm)", timeW, numFmt.format(timeW) + " ns", ACCENT_AMBER));
        items.add(new BarChartPanel.BarItem("Needleman-Wunsch", "O(nm)", timeNW, numFmt.format(timeNW) + " ns", ACCENT_PURPLE));
        items.add(new BarChartPanel.BarItem("Smith-Waterman", "O(nm)", timeSW, numFmt.format(timeSW) + " ns", ACCENT_ROSE));
        chartDist.setChartData("Sequence Alignment & Distance Measures", "Execution Time (ns)", items);
    }

    private JPanel createBitmaskSubTab() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(BG_DARK);

        JPanel topCard = createCardPanel("Bitmask DP Activity Optimization (Exact O(2^N * N))");
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 4));

        topCard.add(createLabel("Time Budget (minutes):"));
        spnMaxTime = new JSpinner(new SpinnerNumberModel(90, 10, 300, 5));
        styleSpinner(spnMaxTime);
        topCard.add(spnMaxTime);

        JButton btnSolve = createStyledButton("Solve Optimal Subset", ACCENT_AMBER);
        btnSolve.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSolve.addActionListener(e -> solveBitmaskDP());
        topCard.add(btnSolve);

        p.add(topCard, BorderLayout.NORTH);

        // Center split: Activity Table vs Result
        String[] cols = {"Activity Name", "Duration (mins)", "Utility (+Score)"};
        modelActivities = new DefaultTableModel(cols, 0);
        modelActivities.addRow(new Object[]{"Morning Jogging", 30, 40});
        modelActivities.addRow(new Object[]{"Weightlifting", 45, 55});
        modelActivities.addRow(new Object[]{"Meditation", 20, 35});
        modelActivities.addRow(new Object[]{"Yoga Stretching", 25, 30});
        modelActivities.addRow(new Object[]{"Swimming Laps", 40, 60});
        modelActivities.addRow(new Object[]{"Cycling Sprint", 35, 50});
        modelActivities.addRow(new Object[]{"Reading Health Notes", 15, 20});

        JTable tableAct = createStyledTable(modelActivities);
        JScrollPane spAct = new JScrollPane(tableAct);
        spAct.getViewport().setBackground(Color.WHITE);

        txtBitmaskResults = new JTextArea();
        txtBitmaskResults.setEditable(false);
        txtBitmaskResults.setFont(FONT_MONO);
        styleTextInput(txtBitmaskResults);
        txtBitmaskResults.setText("Click 'Solve Optimal Subset' to evaluate all 2^N state bitmasks.");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spAct, new JScrollPane(txtBitmaskResults));
        split.setResizeWeight(0.5);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);
        p.add(split, BorderLayout.CENTER);

        return p;
    }

    private void solveBitmaskDP() {
        int maxTime = (int) spnMaxTime.getValue();
        List<BitmaskDP.Activity> acts = new ArrayList<>();
        for (int i = 0; i < modelActivities.getRowCount(); i++) {
            String name = modelActivities.getValueAt(i, 0).toString();
            int time = Integer.parseInt(modelActivities.getValueAt(i, 1).toString());
            int util = Integer.parseInt(modelActivities.getValueAt(i, 2).toString());
            acts.add(new BitmaskDP.Activity(name, time, util));
        }

        benchmarkExecutionsCount++;
        updateStatusBar();

        long t0 = System.nanoTime();
        BitmaskDP.OptimizationResult res = BitmaskDP.optimize(acts, maxTime);
        long elapsed = System.nanoTime() - t0;

        StringBuilder sb = new StringBuilder();
        sb.append("=== BITMASK DP OPTIMAL SCHEDULE ===\n");
        sb.append("Execution Time: ").append(numFmt.format(elapsed)).append(" ns\n");
        sb.append("Total Combinations Evaluated: 2^").append(acts.size()).append(" = ").append(1 << acts.size()).append(" states\n");
        sb.append("Maximum Utility Score Achieved: ").append(res.maxUtility).append("\n");
        sb.append("Total Time Consumed: ").append(res.totalTimeSpent).append(" / ").append(maxTime).append(" minutes\n");
        sb.append("Optimal Bitmask Binary: 0b").append(Integer.toBinaryString(res.optimalMask)).append("\n\n");
        sb.append("Selected Activities:\n");
        for (BitmaskDP.Activity a : res.selectedActivities) {
            sb.append(String.format("  • %s (%d mins, +%d utility)\n", a.name, a.timeMinutes, a.utilityValue));
        }
        txtBitmaskResults.setText(sb.toString());
    }

    private JPanel createMCMSubTab() {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBackground(BG_DARK);

        JPanel topCard = createCardPanel("Matrix-Chain Multiplication Configuration (O(N³))");
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 4));

        topCard.add(createLabel("Pipeline Dimensions Array (CSV):"));
        txtMatrixDims = new JTextField("10, 30, 5, 60, 20", 18);
        styleTextInput(txtMatrixDims);
        topCard.add(txtMatrixDims);

        JButton btnSolve = createStyledButton("Optimize Pipeline", ACCENT_PURPLE);
        btnSolve.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSolve.addActionListener(e -> solveMatrixChain());
        topCard.add(btnSolve);

        p.add(topCard, BorderLayout.NORTH);

        chartMatrixChain = new BarChartPanel();
        chartMatrixChain.setChartData("Matrix Operations: Naive vs Optimal Order", "Scalar Multiplications", null);

        txtMatrixResults = new JTextArea();
        txtMatrixResults.setEditable(false);
        txtMatrixResults.setFont(FONT_MONO);
        styleTextInput(txtMatrixResults);
        txtMatrixResults.setText("Click 'Optimize Pipeline' to compute optimal parenthesization.");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chartMatrixChain, new JScrollPane(txtMatrixResults));
        split.setResizeWeight(0.5);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);
        p.add(split, BorderLayout.CENTER);

        return p;
    }

    private void solveMatrixChain() {
        String[] parts = txtMatrixDims.getText().split(",");
        int[] dims = new int[parts.length];
        try {
            for (int i = 0; i < parts.length; i++) dims[i] = Integer.parseInt(parts[i].trim());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter valid positive integer dimensions separated by commas.");
            return;
        }

        benchmarkExecutionsCount++;
        updateStatusBar();

        long t0 = System.nanoTime();
        MatrixChain.Result res = MatrixChain.compute(dims);
        long elapsed = System.nanoTime() - t0;

        StringBuilder sb = new StringBuilder();
        sb.append("=== MATRIX CHAIN MULTIPLICATION RESULT ===\n");
        sb.append("Execution Time: ").append(numFmt.format(elapsed)).append(" ns\n");
        sb.append("Optimal Parenthesization: ").append(res.parenthesization).append("\n");
        sb.append("Minimum Scalar Multiplications: ").append(numFmt.format(res.minCost)).append("\n");
        sb.append("Naive Left-to-Right Cost: ").append(numFmt.format(res.naiveCost)).append("\n");
        double saving = ((double)(res.naiveCost - res.minCost) / res.naiveCost) * 100.0;
        sb.append(String.format("Computational Saving: %.2f%%\n", saving));
        txtMatrixResults.setText(sb.toString());

        List<BarChartPanel.BarItem> items = new ArrayList<>();
        items.add(new BarChartPanel.BarItem("Optimal DP Order", "O(N³)", res.minCost, numFmt.format(res.minCost), ACCENT_GREEN));
        items.add(new BarChartPanel.BarItem("Naive L-to-R Order", "O(1)", res.naiveCost, numFmt.format(res.naiveCost), ACCENT_ROSE));
        chartMatrixChain.setChartData("Scalar Multiplications: Optimal vs Naive", "Operation Count", items);
    }

    // =========================================================================
    // WORKSPACE 5: MODULE 4 — NETWORK FLOW
    // =========================================================================
    private JPanel createNetworkFlowWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topCard = createCardPanel("Module 4: Maximum Flow, Edmonds-Karp & Bipartite Matching");
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));

        JButton btnDefaultNet = createSmallButton("Load Sensor Pipeline Network");
        btnDefaultNet.addActionListener(e -> loadDefaultSensorNetwork());
        topCard.add(btnDefaultNet);

        JButton btnBipartite = createSmallButton("Load Bipartite Task-Resource Mesh");
        btnBipartite.addActionListener(e -> loadBipartiteMesh());
        topCard.add(btnBipartite);

        topCard.add(Box.createHorizontalStrut(10));
        JButton btnRunEK = createStyledButton("Run Edmonds-Karp (BFS)", ACCENT_BLUE);
        btnRunEK.addActionListener(e -> runFlowAlgorithm("Edmonds-Karp"));
        topCard.add(btnRunEK);

        JButton btnRunDinic = createStyledButton("Run Dinic's Algorithm", ACCENT_PURPLE);
        btnRunDinic.addActionListener(e -> runFlowAlgorithm("Dinic"));
        topCard.add(btnRunDinic);

        JButton btnRunFF = createSmallButton("Run Ford-Fulkerson (DFS)");
        btnRunFF.addActionListener(e -> runFlowAlgorithm("Ford-Fulkerson"));
        topCard.add(btnRunFF);

        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Canvas on Left, Trace and Metrics on Right
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.62);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);

        canvasFlow = new GraphVisualizerPanel();
        split.setLeftComponent(canvasFlow);

        JPanel rightCard = createCardPanel("Flow Results & Augmenting Path Log");
        rightCard.setLayout(new BorderLayout(6, 6));

        lblFlowResult = new JLabel("Max Flow: Not yet executed");
        lblFlowResult.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFlowResult.setForeground(ACCENT_GREEN);
        rightCard.add(lblFlowResult, BorderLayout.NORTH);

        txtFlowLog = new JTextArea();
        txtFlowLog.setEditable(false);
        txtFlowLog.setFont(FONT_MONO);
        styleTextInput(txtFlowLog);
        txtFlowLog.setText("Select an algorithm above to push augmenting flow across the network.");
        rightCard.add(new JScrollPane(txtFlowLog), BorderLayout.CENTER);

        split.setRightComponent(rightCard);
        panel.add(split, BorderLayout.CENTER);

        loadDefaultSensorNetwork();
        return panel;
    }

    private void loadDefaultSensorNetwork() {
        activeFlowGraph = NetworkFlow.createDefaultSensorPipelineNetwork();
        canvasFlow.setFlowGraph(activeFlowGraph, null, "Sensor Pipeline Flow Network (Source: Raw Stream -> Sink: DB)");
        lblFlowResult.setText("Loaded default 6-node multi-stage sensor pipeline.");
        txtFlowLog.setText("Nodes: S(0), Filter A(1), Filter B(2), Aggregator C(3), Aggregator D(4), Sink T(5)\nEdges configured with capacities 4 to 12 units.");
    }

    private void loadBipartiteMesh() {
        List<String> tasks = Arrays.asList("ECG Analysis", "Sleep Classifier", "Calorie Model", "GPS Route");
        List<String> resources = Arrays.asList("GPU Core 1", "GPU Core 2", "DSP Core", "Edge NPU");
        boolean[][] compat = {
            {true, true, false, false},
            {false, true, true, false},
            {false, false, true, true},
            {true, false, false, true}
        };
        activeFlowGraph = NetworkFlow.createBipartiteMatchingDemo(tasks, resources, compat);
        canvasFlow.setFlowGraph(activeFlowGraph, null, "Bipartite Task-to-Resource Assignment Mesh");
        lblFlowResult.setText("Loaded 4 Tasks x 4 Resources Bipartite Mesh.");
        txtFlowLog.setText("Left: Tasks (Capacity=1) | Right: Hardware Resources (Capacity=1)\nGoal: Maximum matching allocation via Max-Flow reduction.");
    }

    private void runFlowAlgorithm(String algo) {
        if (activeFlowGraph == null) return;
        benchmarkExecutionsCount++;
        updateStatusBar();

        int s = 0;
        int t = activeFlowGraph.nodes.size() - 1;

        NetworkFlow.FlowResult res;
        if ("Dinic".equals(algo)) {
            res = NetworkFlow.solveDinic(activeFlowGraph, s, t);
        } else if ("Ford-Fulkerson".equals(algo)) {
            res = NetworkFlow.solveFordFulkerson(activeFlowGraph, s, t);
        } else {
            res = NetworkFlow.solveEdmondsKarp(activeFlowGraph, s, t);
        }

        canvasFlow.setFlowGraph(activeFlowGraph, res, algo + " Completed: Max Flow = " + res.maxFlow + " units");
        lblFlowResult.setText(String.format("Max Flow = %d units | Time: %s ns", res.maxFlow, numFmt.format(res.executionTimeNs)));

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(res.algorithm.toUpperCase()).append(" EXECUTION TRACE ===\n");
        sb.append("Execution Time: ").append(numFmt.format(res.executionTimeNs)).append(" ns\n");
        sb.append("Augmenting Iterations: ").append(res.augmentingPathsCount).append("\n\n");
        sb.append("Augmenting Path History:\n");
        for (String p : res.pathHistory) sb.append("  • ").append(p).append("\n");
        sb.append("\n=== MAX-FLOW MIN-CUT THEOREM ===\n");
        sb.append("Source Reachable Partition (S): ").append(res.sourceReachableSet).append("\n");
        sb.append("Bottleneck Min-Cut Edges crossing S -> T:\n");
        int cutCap = 0;
        for (NetworkFlow.Edge e : res.minCutEdges) {
            sb.append(String.format("  ✂ Edge (%s -> %s) Capacity: %d\n", 
                    activeFlowGraph.nodes.get(e.u).name, activeFlowGraph.nodes.get(e.v).name, e.capacity));
            cutCap += e.capacity;
        }
        sb.append("Sum of Min-Cut Capacities = ").append(cutCap).append(" (Equal to Max-Flow ").append(res.maxFlow).append(")\n");
        txtFlowLog.setText(sb.toString());
    }

    // =========================================================================
    // WORKSPACE 6: MODULE 5 — CONSTRAINT OPTIMIZATION
    // =========================================================================
    private JPanel createConstraintWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topCard = createCardPanel("Module 5: 3-SAT Solver, CLIQUE Reductions & Vertex Cover 2-Approximation");
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));

        JButton btnSolveSat = createStyledButton("1. Solve 3-SAT (DPLL)", ACCENT_BLUE);
        btnSolveSat.addActionListener(e -> run3SatDemo());
        topCard.add(btnSolveSat);

        JButton btnReduceClique = createStyledButton("2. Reduce 3-SAT to CLIQUE", ACCENT_PURPLE);
        btnReduceClique.addActionListener(e -> runCliqueReductionDemo());
        topCard.add(btnReduceClique);

        JButton btnSolveVC = createStyledButton("3. Compute Vertex Cover 2-Approx", ACCENT_GREEN);
        btnSolveVC.addActionListener(e -> runVertexCoverDemo());
        topCard.add(btnSolveVC);

        panel.add(topCard, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.62);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);

        canvasConstraint = new GraphVisualizerPanel();
        split.setLeftComponent(canvasConstraint);

        JPanel rightCard = createCardPanel("Reduction Transformation & Complexity Log");
        rightCard.setLayout(new BorderLayout(6, 6));

        lblConstraintResult = new JLabel("Status: Ready to solve constraint problems");
        lblConstraintResult.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblConstraintResult.setForeground(ACCENT_CYAN);
        rightCard.add(lblConstraintResult, BorderLayout.NORTH);

        txtConstraintLog = new JTextArea();
        txtConstraintLog.setEditable(false);
        txtConstraintLog.setFont(FONT_MONO);
        styleTextInput(txtConstraintLog);
        txtConstraintLog.setText("Select an action above to execute 3-SAT solving or Karp reductions.");
        rightCard.add(new JScrollPane(txtConstraintLog), BorderLayout.CENTER);

        split.setRightComponent(rightCard);
        panel.add(split, BorderLayout.CENTER);

        runCliqueReductionDemo();
        return panel;
    }

    private void run3SatDemo() {
        ConstraintOptimization.SatFormula f = ConstraintOptimization.createSampleFormula();
        benchmarkExecutionsCount++;
        updateStatusBar();

        ConstraintOptimization.SatResult res = ConstraintOptimization.solve3Sat(f);
        lblConstraintResult.setText("3-SAT Solved: " + (res.satisfiable ? "SATISFIABLE" : "UNSATISFIABLE") + " in " + numFmt.format(res.executionTimeNs) + " ns");

        StringBuilder sb = new StringBuilder();
        sb.append("=== 3-SAT EXACT DPLL VERIFICATION ===\n");
        sb.append("Boolean Formula: ").append(f.toString()).append("\n");
        sb.append("Variable Count: ").append(f.varCount).append(" | Clauses: ").append(f.clauses.size()).append("\n");
        sb.append("Satisfiable: ").append(res.satisfiable ? "YES (True)" : "NO (False)").append("\n");
        sb.append("Decisions/Branching Steps: ").append(res.stepsExplored).append("\n\n");
        sb.append("Satisfying Assignment Found:\n");
        for (Map.Entry<Integer, Boolean> e : res.assignment.entrySet()) {
            sb.append(String.format("  • Variable x%d = %s\n", e.getKey(), e.getValue()));
        }
        sb.append("\nTheoretical Note: General 3-SAT is NP-complete. Bounded n<=16 evaluated in milliseconds.\n");
        txtConstraintLog.setText(sb.toString());
    }

    private void runCliqueReductionDemo() {
        ConstraintOptimization.SatFormula f = ConstraintOptimization.createSampleFormula();
        activeReductionGraph = ConstraintOptimization.reduce3SatToClique(f);

        canvasConstraint.setReductionGraph(activeReductionGraph, null, "3-SAT to CLIQUE Gadget Graph (Target k=" + f.clauses.size() + ")");
        lblConstraintResult.setText("Reduced 3-SAT (" + f.clauses.size() + " clauses) -> CLIQUE Graph (" + activeReductionGraph.n + " vertices, " + activeReductionGraph.edgeCount() + " edges)");

        StringBuilder sb = new StringBuilder();
        sb.append("=== 3-SAT TO CLIQUE KARP REDUCTION ===\n");
        sb.append("Formula: ").append(f.toString()).append("\n");
        sb.append("Reduction Theorem: Formula has satisfying assignment iff Gadget Graph has a clique of size k = m = ").append(f.clauses.size()).append("\n\n");
        sb.append("Gadget Structure:\n");
        sb.append("  • 3 vertices per clause (one per literal)\n");
        sb.append("  • Edges placed between literals in distinct clauses that are non-contradictory\n");
        sb.append("Total Vertices |V|: ").append(activeReductionGraph.n).append("\n");
        sb.append("Total Edges |E|: ").append(activeReductionGraph.edgeCount()).append("\n");
        txtConstraintLog.setText(sb.toString());
    }

    private void runVertexCoverDemo() {
        if (activeReductionGraph == null) runCliqueReductionDemo();
        benchmarkExecutionsCount += 2;
        updateStatusBar();

        List<Integer> approxVC = ConstraintOptimization.solveVertexCover2Approx(activeReductionGraph);
        List<Integer> exactVC = ConstraintOptimization.solveExactMinVertexCover(activeReductionGraph);

        canvasConstraint.setReductionGraph(activeReductionGraph, approxVC, "Vertex Cover Solution (Cover Vertices Highlighted)");
        lblConstraintResult.setText("Vertex Cover 2-Approx: " + approxVC.size() + " vertices | Exact Minimum: " + exactVC.size() + " vertices");

        StringBuilder sb = new StringBuilder();
        sb.append("=== VERTEX COVER 2-APPROXIMATION VS EXACT ===\n");
        sb.append("Problem: Find minimum vertex subset touching every edge in graph.\n");
        sb.append("Heuristic: Maximal Matching Edge Removal (2-Approximation)\n\n");
        sb.append("2-Approx Cover Size: ").append(approxVC.size()).append(" vertices: ").append(approxVC).append("\n");
        sb.append("Exact Optimal Cover: ").append(exactVC.size()).append(" vertices: ").append(exactVC).append("\n");
        double ratio = (double) approxVC.size() / exactVC.size();
        sb.append(String.format("Approximation Ratio: %.2fx (Guaranteed <= 2.0x)\n\n", ratio));
        sb.append("Reduction Chain: 3-SAT <=p CLIQUE <=p Independent Set <=p Vertex Cover\n");
        txtConstraintLog.setText(sb.toString());
    }

    // =========================================================================
    // WORKSPACE 7: MODULE 6 — RANDOMIZED & PARALLEL ALGORITHMS
    // =========================================================================
    private JPanel createRandomizedParallelWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTabbedPane randTabs = new JTabbedPane();
        randTabs.setForeground(Color.BLACK);
        randTabs.setFont(FONT_BOLD);

        // 1. Randomized QuickSort
        JPanel pQS = new JPanel(new BorderLayout(8, 8));
        pQS.setBackground(BG_DARK);
        JPanel topQS = createCardPanel("Randomized QuickSort — Uniform Random Pivot Selection");
        topQS.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JButton btnRunQS = createStyledButton("Sort 500 Sensor Readings", ACCENT_BLUE);
        btnRunQS.addActionListener(e -> runQuickSortDemo());
        topQS.add(btnRunQS);
        pQS.add(topQS, BorderLayout.NORTH);
        txtQuickSortLog = new JTextArea();
        txtQuickSortLog.setFont(FONT_MONO);
        styleTextInput(txtQuickSortLog);
        pQS.add(new JScrollPane(txtQuickSortLog), BorderLayout.CENTER);
        randTabs.addTab("1. Randomized QuickSort", pQS);

        // 2. Reservoir Sampling
        JPanel pRes = new JPanel(new BorderLayout(8, 8));
        pRes.setBackground(BG_DARK);
        JPanel topRes = createCardPanel("Reservoir Sampling (Algorithm R) — Stream Processing");
        topRes.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JButton btnRunRes = createStyledButton("Sample k=5 from 100 Streaming Records", ACCENT_GREEN);
        btnRunRes.addActionListener(e -> runReservoirDemo());
        topRes.add(btnRunRes);
        pRes.add(topRes, BorderLayout.NORTH);
        txtReservoirLog = new JTextArea();
        txtReservoirLog.setFont(FONT_MONO);
        styleTextInput(txtReservoirLog);
        pRes.add(new JScrollPane(txtReservoirLog), BorderLayout.CENTER);
        randTabs.addTab("2. Reservoir Sampling", pRes);

        // 3. Miller-Rabin
        JPanel pMR = new JPanel(new BorderLayout(8, 8));
        pMR.setBackground(BG_DARK);
        JPanel topMR = createCardPanel("Miller-Rabin Probabilistic Primality Verification");
        topMR.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JButton btnRunMR = createStyledButton("Test 1,000,000,007 (k=10 trials)", ACCENT_PURPLE);
        btnRunMR.addActionListener(e -> runMillerRabinDemo());
        topMR.add(btnRunMR);
        pMR.add(topMR, BorderLayout.NORTH);
        txtMillerRabinLog = new JTextArea();
        txtMillerRabinLog.setFont(FONT_MONO);
        styleTextInput(txtMillerRabinLog);
        pMR.add(new JScrollPane(txtMillerRabinLog), BorderLayout.CENTER);
        randTabs.addTab("3. Miller-Rabin Primality", pMR);

        // 4. Blelloch Parallel Scan
        JPanel pScan = new JPanel(new BorderLayout(8, 8));
        pScan.setBackground(BG_DARK);
        JPanel topScan = createCardPanel("Blelloch Work-Efficient Parallel Prefix Scan (Up & Down Sweep)");
        topScan.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JButton btnRunScan = createStyledButton("Execute 8-Element Tree Scan", ACCENT_AMBER);
        btnRunScan.addActionListener(e -> runBlellochDemo());
        topScan.add(btnRunScan);
        pScan.add(topScan, BorderLayout.NORTH);
        txtBlellochLog = new JTextArea();
        txtBlellochLog.setFont(FONT_MONO);
        styleTextInput(txtBlellochLog);
        pScan.add(new JScrollPane(txtBlellochLog), BorderLayout.CENTER);
        randTabs.addTab("4. Blelloch Parallel Scan", pScan);

        // 5. Parallel Reduce & Brent's Theorem
        JPanel pBrent = new JPanel(new BorderLayout(8, 8));
        pBrent.setBackground(BG_DARK);
        JPanel topBrent = createCardPanel("Parallel Reduce (ForkJoin) & Brent's Theorem Work-Span Speedup Bounds");
        topBrent.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
        JButton btnRunBrent = createStyledButton("Run 100,000 Multi-Core Reduce", ACCENT_CYAN);
        btnRunBrent.addActionListener(e -> runParallelReduceDemo());
        topBrent.add(btnRunBrent);
        lblParallelReduceSummary = new JLabel("Click button to measure ForkJoin multi-core speedup.");
        lblParallelReduceSummary.setForeground(TEXT_PRIMARY);
        topBrent.add(lblParallelReduceSummary);
        pBrent.add(topBrent, BorderLayout.NORTH);

        String[] bCols = {"Processors (p)", "Theoretical Time Tp", "Theoretical Speedup", "Parallel Efficiency"};
        modelBrent = new DefaultTableModel(bCols, 0);
        JTable tableBrent = createStyledTable(modelBrent);
        JScrollPane spBrent = new JScrollPane(tableBrent);
        spBrent.getViewport().setBackground(Color.WHITE);
        pBrent.add(spBrent, BorderLayout.CENTER);
        randTabs.addTab("5. Parallel Reduce & Brent's Theorem", pBrent);

        panel.add(randTabs, BorderLayout.CENTER);
        runQuickSortDemo();
        return panel;
    }

    private void runQuickSortDemo() {
        int[] arr = new int[500];
        Random rng = new Random(101);
        for (int i = 0; i < arr.length; i++) arr[i] = rng.nextInt(1000);

        benchmarkExecutionsCount++;
        updateStatusBar();

        RandomizedParallel.QuickSortResult res = RandomizedParallel.sortRandomized(arr);
        StringBuilder sb = new StringBuilder();
        sb.append("=== RANDOMIZED QUICKSORT EXECUTION ===\n");
        sb.append("Elements Sorted: ").append(arr.length).append(" | Time: ").append(numFmt.format(res.executionTimeNs)).append(" ns\n");
        sb.append("Character/Key Comparisons: ").append(numFmt.format(res.comparisonCount)).append("\n");
        sb.append("Total Partitions: ").append(res.partitionCount).append("\n\n");
        sb.append("Partition Execution Trace (Sample):\n");
        for (String t : res.traceLog) sb.append("  • ").append(t).append("\n");
        txtQuickSortLog.setText(sb.toString());
    }

    private void runReservoirDemo() {
        List<String> stream = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            stream.add(String.format("Telemetry-Packet-#%03d [HeartRate:%d]", i, 60 + (i * 7) % 80));
        }
        benchmarkExecutionsCount++;
        updateStatusBar();

        RandomizedParallel.ReservoirResult res = RandomizedParallel.runReservoirSampling(stream, 5);
        StringBuilder sb = new StringBuilder();
        sb.append("=== RESERVOIR SAMPLING (ALGORITHM R) ===\n");
        sb.append("Stream Size: ").append(res.totalStreamSize).append(" | Reservoir Capacity (k): ").append(res.sampleK).append("\n");
        sb.append("Mathematical Guarantee: Every item has exact equal probability ").append(res.sampleK).append("/").append(res.totalStreamSize).append(" = 5.0%\n\n");
        sb.append("Final Uniformly Sampled Elements in Reservoir:\n");
        for (int i = 0; i < res.sample.size(); i++) {
            sb.append(String.format("  Slot [%d]: %s\n", i, res.sample.get(i)));
        }
        sb.append("\nReplacement Event Log:\n");
        for (String log : res.stepLogs) sb.append("  ").append(log).append("\n");
        txtReservoirLog.setText(sb.toString());
    }

    private void runMillerRabinDemo() {
        long candidate = 1000000007L;
        benchmarkExecutionsCount++;
        updateStatusBar();

        RandomizedParallel.MillerRabinResult res = RandomizedParallel.testPrimality(candidate, 10);
        StringBuilder sb = new StringBuilder();
        sb.append("=== MILLER-RABIN PRIMALITY VERIFICATION ===\n");
        sb.append("Tested Candidate: ").append(res.candidate).append("\n");
        sb.append("Result: ").append(res.isProbablyPrime ? "PRIME" : "COMPOSITE").append("\n");
        sb.append("Confidence: ").append(String.format("%.6f%%", res.confidencePercent)).append("\n\n");
        sb.append("Witness Verification Steps:\n");
        for (String s : res.stepDetails) sb.append("  • ").append(s).append("\n");
        txtMillerRabinLog.setText(sb.toString());
    }

    private void runBlellochDemo() {
        int[] in = {3, 1, 7, 0, 4, 1, 6, 3};
        benchmarkExecutionsCount++;
        updateStatusBar();

        RandomizedParallel.BlellochResult res = RandomizedParallel.computeBlellochScan(in);
        StringBuilder sb = new StringBuilder();
        sb.append("=== BLELLOCH PARALLEL PREFIX SCAN ===\n");
        sb.append("Input Array:        ").append(Arrays.toString(res.input)).append("\n");
        sb.append("Computed Prefix Sum: ").append(Arrays.toString(res.prefixSum)).append("\n\n");
        sb.append("Phase 1: Up-Sweep (Parallel Reduction Tree, O(n) Work, O(log n) Span):\n");
        for (String u : res.upSweepLog) sb.append("  ").append(u).append("\n");
        sb.append("\nPhase 2: Down-Sweep (Parallel Distribution Tree):\n");
        for (String d : res.downSweepLog) sb.append("  ").append(d).append("\n");
        txtBlellochLog.setText(sb.toString());
    }

    private void runParallelReduceDemo() {
        benchmarkExecutionsCount++;
        updateStatusBar();

        RandomizedParallel.ParallelReduceResult res = RandomizedParallel.runParallelReduceBenchmark(100000);
        lblParallelReduceSummary.setText(String.format("Sequential: %s ns | Parallel (%d cores): %s ns | Speedup: %.2fx",
                numFmt.format(res.seqTimeNs), res.processorsUsed, numFmt.format(res.parTimeNs), res.speedup));

        modelBrent.setRowCount(0);
        List<RandomizedParallel.BrentAnalysisRow> rows = RandomizedParallel.computeBrentAnalysis(res.seqTimeNs, 5000);
        for (RandomizedParallel.BrentAnalysisRow r : rows) {
            modelBrent.addRow(new Object[]{
                r.processors,
                numFmt.format((long) r.theoreticalTime) + " ns",
                String.format("%.2fx", r.speedup),
                String.format("%.1f%%", r.efficiency)
            });
        }
    }

    // =========================================================================
    // WORKSPACE 8: BENCHMARK LABORATORY
    // =========================================================================
    private JPanel createBenchmarkWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topCard = createCardPanel("Benchmarking Laboratory (Non-Blocking SwingWorker)");
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));

        topCard.add(createLabel("Algorithm Category:"));
        cmbBenchCategory = new JComboBox<>(new String[]{"Exact String Matchers", "Distance & Sequence Alignment", "Network Flow & Bipartite", "Randomized & Sorting"});
        styleComboBox(cmbBenchCategory);
        topCard.add(cmbBenchCategory);

        topCard.add(createLabel("Input Size:"));
        spnBenchSize = new JSpinner(new SpinnerNumberModel(1000, 100, 50000, 500));
        styleSpinner(spnBenchSize);
        topCard.add(spnBenchSize);

        topCard.add(createLabel("Trials:"));
        spnBenchTrials = new JSpinner(new SpinnerNumberModel(5, 1, 20, 1));
        styleSpinner(spnBenchTrials);
        topCard.add(spnBenchTrials);

        JButton btnStart = createStyledButton("Run Rigorous Benchmark", ACCENT_BLUE);
        btnStart.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnStart.addActionListener(e -> executeBenchmarkSuite());
        topCard.add(btnStart);

        progressBenchmark = new JProgressBar();
        progressBenchmark.setPreferredSize(new Dimension(140, 20));
        progressBenchmark.setStringPainted(true);
        topCard.add(progressBenchmark);

        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Bar Chart vs Metrics Table
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);

        chartBenchmark = new BarChartPanel();
        chartBenchmark.setChartData("Laboratory Benchmark Results", "Execution Time (ns)", null);
        split.setLeftComponent(chartBenchmark);

        JPanel rightCard = createCardPanel("Statistical Results (Min, Average, Median)");
        rightCard.setLayout(new BorderLayout(6, 6));

        String[] cols = {"Algorithm", "Min Time (ns)", "Avg Time (ns)", "Median Time (ns)", "Theoretical Big-O"};
        modelBenchmark = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tableBench = createStyledTable(modelBenchmark);
        JScrollPane sp = new JScrollPane(tableBench);
        sp.getViewport().setBackground(Color.WHITE);
        rightCard.add(sp, BorderLayout.CENTER);

        split.setRightComponent(rightCard);
        panel.add(split, BorderLayout.CENTER);

        return panel;
    }

    private void executeBenchmarkSuite() {
        int catIdx = cmbBenchCategory.getSelectedIndex();
        int size = (int) spnBenchSize.getValue();
        int trials = (int) spnBenchTrials.getValue();

        progressBenchmark.setIndeterminate(true);
        progressBenchmark.setString("Benchmarking...");
        modelBenchmark.setRowCount(0);

        // Run on background SwingWorker to keep UI completely responsive
        SwingWorker<List<BarChartPanel.BarItem>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<BarChartPanel.BarItem> doInBackground() {
                List<BarChartPanel.BarItem> items = new ArrayList<>();
                benchmarkExecutionsCount += (trials * 4);

                if (catIdx == 0) { // String matching
                    String text = datasetService.getAllNotesAggregated();
                    if (text.length() < size) {
                        StringBuilder sb = new StringBuilder(text);
                        while (sb.length() < size) sb.append(" exercise walking health vital ");
                        text = sb.toString();
                    }
                    String pat = "exercise";

                    String[] names = {"Naive Search", "KMP", "Z-Algorithm", "Rabin-Karp"};
                    String[] bigO = {"O(nm)", "O(n+m)", "O(n+m)", "O(n+m)"};

                    for (int a = 0; a < 4; a++) {
                        long[] times = new long[trials];
                        for (int t = 0; t < trials; t++) {
                            long start = System.nanoTime();
                            if (a == 0) NaiveSearch.search(text, pat);
                            else if (a == 1) KMP.search(text, pat);
                            else if (a == 2) ZAlgorithm.search(text, pat);
                            else RabinKarp.search(text, pat);
                            times[t] = System.nanoTime() - start;
                        }
                        Arrays.sort(times);
                        long min = times[0];
                        long median = times[trials / 2];
                        long sum = 0; for (long tm : times) sum += tm;
                        long avg = sum / trials;

                        final int idx = a;
                        final long fMin = min, fAvg = avg, fMed = median;
                        SwingUtilities.invokeLater(() -> {
                            modelBenchmark.addRow(new Object[]{names[idx], numFmt.format(fMin), numFmt.format(fAvg), numFmt.format(fMed), bigO[idx]});
                        });
                        items.add(new BarChartPanel.BarItem(names[a], bigO[a], avg, numFmt.format(avg) + " ns", BarChartPanel.PALETTE[a % BarChartPanel.PALETTE.length]));
                    }
                } else {
                    // General comparison demo
                    String[] names = {"Levenshtein", "Damerau-Lev", "Needleman-Wunsch", "Smith-Waterman"};
                    String[] bigO = {"O(nm)", "O(nm)", "O(nm)", "O(nm)"};
                    String s1 = "cardiac arrhytmia stream test";
                    String s2 = "cardiac arrhythmia sensor stream";

                    for (int a = 0; a < 4; a++) {
                        long[] times = new long[trials];
                        for (int t = 0; t < trials; t++) {
                            long start = System.nanoTime();
                            if (a == 0) Levenshtein.compute(s1, s2);
                            else if (a == 1) DamerauLevenshtein.compute(s1, s2);
                            else if (a == 2) NeedlemanWunsch.align(s1, s2, 1, -1, -1);
                            else SmithWaterman.align(s1, s2, 2, -1, -1);
                            times[t] = System.nanoTime() - start;
                        }
                        Arrays.sort(times);
                        long min = times[0];
                        long median = times[trials / 2];
                        long sum = 0; for (long tm : times) sum += tm;
                        long avg = sum / trials;

                        final int idx = a;
                        final long fMin = min, fAvg = avg, fMed = median;
                        SwingUtilities.invokeLater(() -> {
                            modelBenchmark.addRow(new Object[]{names[idx], numFmt.format(fMin), numFmt.format(fAvg), numFmt.format(fMed), bigO[idx]});
                        });
                        items.add(new BarChartPanel.BarItem(names[a], bigO[a], avg, numFmt.format(avg) + " ns", BarChartPanel.PALETTE[a % BarChartPanel.PALETTE.length]));
                    }
                }
                return items;
            }

            @Override
            protected void done() {
                try {
                    List<BarChartPanel.BarItem> res = get();
                    chartBenchmark.setChartData("Benchmarked Algorithms Runtime (Average)", "Execution Time (ns)", res);
                } catch (Exception ignored) {}
                progressBenchmark.setIndeterminate(false);
                progressBenchmark.setValue(100);
                progressBenchmark.setString("Finished");
                updateStatusBar();
            }
        };
        worker.execute();
    }

    // =========================================================================
    // WORKSPACE 9: DATASET MANAGER
    // =========================================================================
    private JPanel createDatasetManagerWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topCard = createCardPanel("Dataset Generation & Import / Export Controls");
        topCard.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));

        JButton btnGen100 = createStyledButton("Generate 100 Records", ACCENT_BLUE);
        btnGen100.addActionListener(e -> generateDataAndRefresh(100));
        topCard.add(btnGen100);

        JButton btnGen1k = createStyledButton("Generate 1,000 Records", ACCENT_GREEN);
        btnGen1k.addActionListener(e -> generateDataAndRefresh(1000));
        topCard.add(btnGen1k);

        JButton btnGen10k = createStyledButton("Generate 10,000 Records", ACCENT_PURPLE);
        btnGen10k.addActionListener(e -> generateDataAndRefresh(10000));
        topCard.add(btnGen10k);

        JButton btnReset = createSmallButton("Reset Default Data");
        btnReset.addActionListener(e -> {
            datasetService.loadDefaultDataset();
            refreshDatasetViews();
        });
        topCard.add(btnReset);

        topCard.add(Box.createHorizontalStrut(10));
        JButton btnExport = createSmallButton("Export CSV");
        btnExport.addActionListener(e -> exportDatasetToCsv());
        topCard.add(btnExport);

        JButton btnImport = createSmallButton("Import CSV");
        btnImport.addActionListener(e -> importDatasetFromCsv());
        topCard.add(btnImport);

        panel.add(topCard, BorderLayout.NORTH);

        // Center Split: Chart vs Summary
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setDividerSize(6);
        split.setBackground(BG_DARK);

        chartDatasetDist = new BarChartPanel();
        split.setLeftComponent(chartDatasetDist);

        JPanel rightCard = createCardPanel("Active Dataset Metadata & Diagnostics");
        rightCard.setLayout(new BorderLayout(6, 6));

        txtDatasetSummary = new JTextArea();
        txtDatasetSummary.setEditable(false);
        txtDatasetSummary.setFont(FONT_MONO);
        styleTextInput(txtDatasetSummary);
        rightCard.add(new JScrollPane(txtDatasetSummary), BorderLayout.CENTER);

        split.setRightComponent(rightCard);
        panel.add(split, BorderLayout.CENTER);

        refreshDatasetViews();
        return panel;
    }

    private void generateDataAndRefresh(int count) {
        datasetService.generateSyntheticData(count);
        refreshDatasetViews();
        refreshExplorerTable();
        updateStatusBar();
        JOptionPane.showMessageDialog(this, "Generated " + numFmt.format(count) + " synthetic personal records in-memory.", "Dataset Ready", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshDatasetViews() {
        Map<String, Integer> dist = datasetService.getCategoryDistribution();
        List<BarChartPanel.BarItem> items = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> e : dist.entrySet()) {
            items.add(new BarChartPanel.BarItem(e.getKey(), "Count", e.getValue(), numFmt.format(e.getValue()), BarChartPanel.PALETTE[i % BarChartPanel.PALETTE.length]));
            i++;
        }
        if (chartDatasetDist != null) {
            chartDatasetDist.setChartData("Category Record Counts", "Total Records", items);
        }

        if (txtDatasetSummary != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== DATASET OVERVIEW ===\n");
            sb.append("Total In-Memory Records: ").append(numFmt.format(datasetService.getRecordCount())).append("\n");
            sb.append("Storage Mode: Pure Java Collections (Zero external database required)\n\n");
            sb.append("Category Distribution Breakdown:\n");
            for (Map.Entry<String, Integer> e : dist.entrySet()) {
                double pct = (datasetService.getRecordCount() > 0) ? ((double) e.getValue() / datasetService.getRecordCount()) * 100.0 : 0;
                sb.append(String.format("  • %-18s: %6s records (%.1f%%)\n", e.getKey(), numFmt.format(e.getValue()), pct));
            }
            sb.append("\nSample Data Inspection (First 3 Records):\n");
            for (int k = 0; k < Math.min(3, datasetService.getRecords().size()); k++) {
                sb.append("  ").append(datasetService.getRecords().get(k).toString()).append("\n");
            }
            txtDatasetSummary.setText(sb.toString());
        }
    }

    private void exportDatasetToCsv() {
        JFileChooser fc = new JFileChooser(new File("."));
        fc.setSelectedFile(new File("lifedata_export.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                datasetService.exportToCsv(fc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Dataset exported to:\n" + fc.getSelectedFile().getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Export error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importDatasetFromCsv() {
        JFileChooser fc = new JFileChooser(new File("."));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                int count = datasetService.importFromCsv(fc.getSelectedFile());
                refreshDatasetViews();
                refreshExplorerTable();
                updateStatusBar();
                JOptionPane.showMessageDialog(this, "Successfully imported " + count + " records.", "Import Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Import error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // =========================================================================
    // UI HELPERS & HIGH-CONTRAST STYLING
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
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(148, 163, 184), 1),
                new EmptyBorder(6, 14, 6, 14)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JButton createSmallButton(String text) {
        JButton b = new JButton(text);
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 11));
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(148, 163, 184), 1),
                new EmptyBorder(4, 10, 4, 10)
        ));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void styleComboBox(JComboBox<?> cmb) {
        cmb.setBackground(Color.WHITE);
        cmb.setForeground(Color.BLACK);
        cmb.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cmb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                l.setBorder(new EmptyBorder(4, 6, 4, 6));
                if (isSelected) {
                    l.setBackground(new Color(14, 165, 233));
                    l.setForeground(Color.WHITE);
                } else {
                    l.setBackground(Color.WHITE);
                    l.setForeground(Color.BLACK);
                }
                return l;
            }
        });
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
        spn.setBackground(Color.WHITE);
        JComponent editor = spn.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(Color.WHITE);
            tf.setForeground(Color.BLACK);
            tf.setCaretColor(Color.BLACK);
            tf.setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(203, 213, 225));
        table.setRowHeight(24);
        table.setFont(FONT_BODY);
        table.setSelectionBackground(new Color(14, 165, 233));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(FONT_BOLD);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(Color.BLACK);
        centerRenderer.setBackground(Color.WHITE);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setForeground(Color.BLACK);
        leftRenderer.setBackground(Color.WHITE);

        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i > 0 && i < table.getColumnCount() - 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            } else {
                table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
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
