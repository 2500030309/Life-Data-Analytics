# LifeData — Large-Scale Personal Data Analytics Platform
### Core Java + Professional Java Swing / AWT | DSA College Hackathon
**"Understand Your Data. Optimize Every Operation."**

---

## 1. Project Overview & Identity

**LifeData** is an interactive, high-performance desktop analytics platform built in pure **Core Java and Java Swing/AWT**. Designed for large-scale personal data processing, LifeData demonstrates how foundational and advanced **Data Structures and Algorithms (DSA)** solve real-world challenges in personal health telemetry, activity scheduling, query retrieval, resource allocation, and large-scale parallel data transformation.

The platform provides a single-window interface with **10 dedicated workspaces**, custom **2D anti-aliased graph and chart visualizers**, an in-memory **Personal Data Explorer**, an empirical **Benchmark Laboratory**, and a **Synthetic Dataset Engine**.

### Core Tenets
- **100% Core Java:** Zero external dependencies, no Maven/Gradle required, no external databases or web servers.
- **Native Eclipse IDE Support:** Seamlessly opens and executes directly inside Eclipse IDE (`src/` compiles to `bin/`).
- **High-Contrast Professional GUI:** Designed with a dark slate canvas (`#0F172A`), card panels (`#1E293B`), cyber blue accents (`#38BDF8`), and high-contrast bold dark labels on interactive controls (`Color.BLACK`) for presentation readability.
- **Glyph-Safe Typography:** Built entirely without external font dependencies or emoji characters to ensure universal cross-platform rendering across Windows, macOS, and Linux without missing box symbols (`▯`).

---

## 2. Problem Statement & Motivation

With the exponential growth of personal wearables, fitness trackers, digital journals, and continuous sensor streams, users accumulate massive volumes of heterogeneous personal records:
1. **Unstructured Text & Notes:** Medical records, workout journals, dietary notes, and clinical instructions.
2. **Sequential Time-Series Events:** Biometric pulse observations, sleep phases, and activity timestamps.
3. **Constrained Multi-Task Allocation:** Limited daily time and energy budgets requiring optimal activity subsets.
4. **Distributed Resource Scheduling:** Mapping computational telemetry jobs to heterogeneous sensor processors.
5. **Real-Time Stream Transformation:** Massive sensor logs demanding parallel reductions and prefix aggregations.

Traditional software architectures often rely on external databases, black-box libraries, or naive brute-force loops. **LifeData** solves these challenges by implementing **25+ algorithms from scratch**, exposing their internal mechanics, visual state transitions, and empirical complexities.

---

## 3. System Architecture & Workspaces

LifeData is structured around a single-window `JFrame` using a responsive `BorderLayout` with a fixed navigation sidebar, dynamic header, real-time status bar, and a `CardLayout` workspace viewport hosting 12 independent modules:

```
+---------------------------------------------------------------------------------------------------+
|  [LOGO]  LifeData — Large-Scale Personal Data Analytics Platform            [Records: 1,000 | Ready] |
+------------------+--------------------------------------------------------------------------------+
|  NAVIGATION      |  ACTIVE WORKSPACE (CardLayout)                                                 |
|  --------------- |                                                                                |
|  1. Dashboard    |  [ 10 Integrated Workspaces ]                                                  |
|  2. Explorer     |                                                                                |
|  3. String Intel |  • Interactive Controls & Configuration Forms                                  |
|  4. Suffix Trees |  • Step-by-Step Visualization & Matrix Tables                                  |
|  5. Dynamic Prog |  • Anti-Aliased 2D Flow & Reduction Graphs                                     |
|  6. Network Flow |  • Empirical Execution Performance & Complexity Cards                          |
|  7. Constraints  |                                                                                |
|  8. Rand/Parallel|                                                                                |
|  9. Benchmark Lab|                                                                                |
| 10. Dataset Mgr  |                                                                                |
+------------------+--------------------------------------------------------------------------------+
|  Status: In-Memory Engine Active | Memory: 32MB / 512MB | Dataset: 1,000 Records Loaded           |
+---------------------------------------------------------------------------------------------------+
```

### The 10 Navigation Workspaces
1. **Overview Dashboard:** Executive summary cards (Total Records, Unique Categories, Date Ranges, Memory Footprint), category distribution bar chart, and quick module launchers.
2. **Personal Data Explorer:** Interactive table supporting multi-column sorting, text filtering, record inspection, record creation/deletion, and RFC-compliant CSV import/export.
3. **Module 1: String Intelligence:** Exact pattern matching and multi-keyword text scanning across personal notes.
4. **Module 2: Suffix Structures:** Full-text suffix indexing, LCP array generation, and Longest Repeated Substring extraction.
5. **Module 3: Dynamic Programming:** Sequence edit distances, global/local biological alignment, constrained activity selection, and matrix chain optimization.
6. **Module 4: Network Flow & Matching:** Max-flow computation, bipartite task-to-device allocation, and min-cut residual visualization.
7. **Module 5: Constraint Optimization:** Exact 3-SAT DPLL solving, Karp reduction gadget to CLIQUE, and 2-approximation Vertex Cover.
8. **Module 6: Randomized & Parallel:** Randomized QuickSort with recursion depth tracing, streaming Reservoir Sampling, Miller-Rabin primality testing, Blelloch parallel prefix scan, and ForkJoin parallel reduction.
9. **Benchmark Laboratory:** Multi-iteration empirical testing engine running on non-blocking background threads (`SwingWorker`) with Min, Mean, Median statistics and 2D bar charts.
10. **Dataset Manager:** Synthetic data generator (100, 1,000, 10,000 records) with realistic categories, sensor readings, and RFC-4180 CSV export/import.

---

## 4. Comprehensive DSA Modules (25+ Algorithms from Scratch)

### Module 1: String Intelligence & Pattern Matching
- **Naive Pattern Search:** Baseline comparison verifying all alignment windows ($O(n \cdot m)$).
- **Knuth-Morris-Pratt (KMP):** Linear search using the Longest Proper Prefix which is also Suffix (LPS) array ($O(n + m)$).
- **Z-Algorithm:** Computes the Z-array of longest common prefixes between suffixes and the query string ($O(n + m)$).
- **Rabin-Karp:** Polynomial rolling hash ($H = \sum c_i b^{m-1-i} \pmod q$) with hash collision tracking ($O(n + m)$ average).
- **Aho-Corasick Automaton:** Trie-based multi-pattern string matcher with BFS failure transitions and dictionary links ($O(n + z)$).

### Module 2: Suffix Structures & Indexing
- **Suffix Array Construction:** Lexicographic ordering of all text suffixes for sub-linear query resolution ($O(n^2 \log n)$).
- **Kasai's LCP Algorithm:** Linear-time Longest Common Prefix array construction utilizing the property $\text{LCP}[r] \ge \text{LCP}[r-1] - 1$ ($O(n)$).
- **Substring Existence Search:** Binary search over sorted suffix pointers ($O(m \log n)$).
- **Longest Repeated Substring (LRS):** Extraction of maximum LCP value and corresponding substring ($O(n)$).

### Module 3: Dynamic Programming & Sequence Alignment
- **Levenshtein Distance:** Standard Wagner-Fischer minimum edit distance (insertions, deletions, substitutions) ($O(n \cdot m)$).
- **Damerau-Levenshtein Distance:** Edit distance accounting for adjacent transpositions (touchscreen typos) ($O(n \cdot m)$).
- **Weighted Edit Distance:** Asymmetric penalties reflecting keyboard layout distance ($O(n \cdot m)$).
- **Needleman-Wunsch Alignment:** Global end-to-end alignment with similarity scoring matrices ($O(n \cdot m)$).
- **Smith-Waterman Alignment:** Local sub-segment alignment identifying conserved behavioral episodes ($O(n \cdot m)$).
- **Bitmask Dynamic Programming:** Exact subset optimization for activity selection under budget constraints ($O(2^N \cdot N)$).
- **Matrix-Chain Multiplication (MCM):** Optimal parenthesization of multi-stage sensor data processing pipelines ($O(n^3)$).

### Module 4: Network Flow & Bipartite Matching
- **Ford-Fulkerson Algorithm:** Augmenting path max-flow framework using DFS ($O(E \cdot |f^*|)$).
- **Edmonds-Karp Algorithm:** Max-flow using BFS shortest augmenting paths ($O(V \cdot E^2)$).
- **Dinic's Algorithm:** High-performance max-flow using layered level graphs and blocking flows via DFS ($O(V^2 E)$).
- **Maximum Bipartite Matching:** Optimal allocation of computational analytics tasks to distributed edge sensors ($O(V \cdot E)$).
- **Minimum $s-t$ Cut:** Residual graph reachability traversal partitioning network bottlenecks.
- **Custom 2D Graph Visualizer (`GraphVisualizerPanel.java`):** Custom AWT rendering of vertices, directed capacity/flow edges, and cut boundaries.

### Module 5: Constraint Optimization & Intractability
- **Exact 3-SAT DPLL Solver:** Davis-Putnam-Logemann-Loveland backtracking solver with unit propagation and pure literal elimination ($O(2^n)$ worst-case).
- **Karp Reduction (3-SAT to CLIQUE):** Polynomial-time reduction constructing a 7-clause gadget graph proving NP-completeness ($O(k^2)$).
- **Graph Complement Transformation:** Duality mapping between CLIQUE and Independent Set.
- **Vertex Cover 2-Approximation:** Maximal matching heuristic guaranteeing cover size at most $2 \times \text{OPT}$ ($O(V + E)$) alongside an exact exponential branch-and-bound baseline.

### Module 6: Randomized & Parallel Processing
- **Randomized QuickSort:** Pivot selection via uniform random distribution preventing $O(n^2)$ worst-case degradation with recursion depth tracing ($O(n \log n)$ expected).
- **Reservoir Sampling (Algorithm R):** Single-pass uniform streaming sampling of $k$ items from an unknown or infinite record stream ($O(N)$ time, $O(k)$ space).
- **Miller-Rabin Primality Test:** Randomized compositeness test utilizing modular exponentiation and quadratic non-residues for cryptographic sensor token validation ($O(k \log^3 n)$).
- **Blelloch Parallel Prefix Scan:** Work-efficient parallel scan with up-sweep (reduce) and down-sweep phases achieving work $O(n)$ and span $O(\log n)$.
- **Parallel Reduce (`ForkJoinPool`):** Divide-and-conquer parallel aggregation across multi-core CPU processors with theoretical bounds validated via Brent's Theorem ($T_p \le (T_1 - T_\infty)/p + T_\infty$).

---

## 5. Comparative Complexity Analysis Matrix

| Module | Algorithm | Time Complexity (Best / Avg / Worst) | Space Complexity | Practical LifeData Use Case |
| :--- | :--- | :--- | :--- | :--- |
| **String** | **Naive Search** | $\Omega(n)$ / $O(n \cdot m)$ / $O(n \cdot m)$ | $O(1)$ | Baseline comparative validation in search pipelines |
| **String** | **KMP Algorithm** | $\Omega(n + m)$ / $\Theta(n + m)$ / $O(n + m)$ | $O(m)$ | Zero-backtracking search in personal health logs |
| **String** | **Z-Algorithm** | $\Omega(n + m)$ / $\Theta(n + m)$ / $O(n + m)$ | $O(n + m)$ | Sensor pulse pattern & periodic telemetry cycle detection |
| **String** | **Rabin-Karp** | $\Omega(n + m)$ / $\Theta(n + m)$ / $O(n \cdot m)$ | $O(1)$ | Fast hash-based ID and event record retrieval |
| **String** | **Aho-Corasick** | $\Omega(n + z)$ / $\Theta(n + z)$ / $O(n + z)$ | $O(\Sigma \cdot m)$ | Real-time multi-keyword clinical tag scanning |
| **Suffix** | **Suffix Array** | $O(n^2 \log n)$ / $O(n^2 \log n)$ / $O(n^2 \log n)$ | $O(n)$ | In-memory indexing of notes for sublinear queries |
| **Suffix** | **Kasai LCP** | $\Omega(n)$ / $\Theta(n)$ / $O(n)$ | $O(n)$ | Linear-time discovery of repeated clinical recommendations |
| **Suffix** | **Binary Suffix Query**| $\Omega(1)$ / $O(m \log n)$ / $O(m \log n)$ | $O(1)$ | Repeated sub-millisecond query lookups |
| **DP** | **Levenshtein** | $O(n \cdot m)$ / $O(n \cdot m)$ / $O(n \cdot m)$ | $O(n \cdot m)$ | Typo tolerance and query suggestions |
| **DP** | **Damerau-Levenshtein**| $O(n \cdot m)$ / $O(n \cdot m)$ / $O(n \cdot m)$ | $O(n \cdot m)$ | Touchscreen transposition error correction |
| **DP** | **Needleman-Wunsch** | $O(n \cdot m)$ / $O(n \cdot m)$ / $O(n \cdot m)$ | $O(n \cdot m)$ | Global biometric sequence alignment |
| **DP** | **Smith-Waterman** | $O(n \cdot m)$ / $O(n \cdot m)$ / $O(n \cdot m)$ | $O(n \cdot m)$ | Local similarity detection in activity routines |
| **DP** | **Bitmask DP** | $O(2^N \cdot N)$ / $O(2^N \cdot N)$ / $O(2^N \cdot N)$ | $O(2^N)$ | Optimal activity schedule within energy/time limits |
| **DP** | **Matrix Chain Mult**| $O(n^3)$ / $O(n^3)$ / $O(n^3)$ | $O(n^2)$ | Minimum-cost execution order for sensor filter chains |
| **Flow** | **Edmonds-Karp** | $O(V \cdot E^2)$ / $O(V \cdot E^2)$ / $O(V \cdot E^2)$ | $O(V + E)$ | Safe flow routing across wearable device clusters |
| **Flow** | **Dinic's Algorithm** | $O(V^2 E)$ / $O(V^2 E)$ / $O(V^2 E)$ | $O(V + E)$ | High-throughput data ingestion bandwidth allocation |
| **Flow** | **Bipartite Matching**| $O(V \cdot E)$ / $O(V \cdot E)$ / $O(V \cdot E)$ | $O(V + E)$ | Edge tasks to compute node assignment |
| **NP** | **3-SAT DPLL** | $O(1)$ / $O(1.618^n)$ / $O(2^n)$ | $O(n + m)$ | Complex health condition constraint checking |
| **NP** | **Vertex Cover 2-App**| $\Theta(V + E)$ / $\Theta(V + E)$ / $O(V + E)$ | $O(V)$ | Sensor network minimum surveillance placement |
| **Rand** | **Random QuickSort** | $\Omega(n \log n)$ / $\Theta(n \log n)$ / $O(n^2)$ | $O(\log n)$ | Non-degrading in-memory record sorting |
| **Rand** | **Reservoir Sampling**| $\Theta(N)$ / $\Theta(N)$ / $O(N)$ | $O(k)$ | Unbiased live stream sampling of wearable telemetry |
| **Rand** | **Miller-Rabin** | $O(k \log^3 n)$ / $O(k \log^3 n)$ / $O(k \log^3 n)$ | $O(1)$ | Cryptographic signature validation for health data |
| **Par** | **Blelloch Prefix Scan**| Work: $O(n)$ / Span: $O(\log n)$ | $O(n)$ | Cumulative telemetry step and energy sums |
| **Par** | **ForkJoin Reduce** | Work: $O(n)$ / Span: $O(\log n)$ | $O(\log n)$ | Multi-core parallel aggregation across CPU cores |

---

## 6. How to Run the Project

### Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher (compatible with Oracle OpenJDK, Eclipse Temurin, Amazon Corretto).
- **Operating System:** Windows, macOS, or Linux.
- **Dependencies:** None (100% Core Java Standard Library).

---

### Method 1: Running in Eclipse IDE (Recommended)
1. **Open Eclipse IDE.**
2. Set your workspace directory (e.g., `c:\Users\LUCKY\eclipse-workspace`).
3. Click **File -> Import... -> General -> Existing Projects into Workspace** (or **Projects from Folder or Archive**).
4. Select the `LifeData` root directory and click **Finish**.
5. In the **Package Explorer**, expand `LifeData` -> `src`.
6. Right-click on **`Main.java`** (or **`LifeDataGUI.java`**).
7. Select **Run As -> Java Application**.
8. The LifeData platform desktop window will immediately launch maximized.

---

### Method 2: Running via Terminal / Command Prompt
1. Open PowerShell or Command Prompt.
2. Navigate to the project folder:
   ```bash
   cd c:\Users\LUCKY\eclipse-workspace\LifeData
   ```
3. Compile all source files into the `bin` directory:
   ```bash
   javac -d bin src/*.java
   ```
4. Launch the application:
   ```bash
   java -cp bin Main
   ```
   *(LifeData automatically detects the desktop display and launches the full GUI).*

### Optional: Running in Terminal CLI Mode
To run the interactive command-line interface directly in your console:
```bash
java -cp bin Main --cli
```

---

## 7. Directory Structure

```
LifeData/
├── .classpath                     # Eclipse classpath configuration
├── .project                       # Eclipse project metadata
├── README.md                      # Comprehensive project documentation
├── bin/                           # Compiled .class binaries (Eclipse output)
├── data/                          # Sample external dataset files
│   ├── activity_records.txt
│   ├── event_records.txt
│   ├── personal_notes.txt
│   └── sample_large_text.txt
└── src/                           # Pure Core Java source code
    ├── BarChartPanel.java         # 2D anti-aliased bar graph renderer
    ├── ConstraintOptimization.java# 3-SAT DPLL, CLIQUE Gadget, Vertex Cover 2-Approx
    ├── DatasetService.java        # Synthetic generation & CSV import/export
    ├── GraphVisualizerPanel.java  # 2D flow and reduction graph visualizer
    ├── LifeDataGUI.java           # Unified 12-workspace Swing desktop GUI
    ├── Main.java                  # Dual-mode desktop/CLI bootstrap launcher
    ├── NetworkFlow.java           # Ford-Fulkerson, Edmonds-Karp, Dinic, Bipartite
    ├── PersonalRecord.java        # Core data model with metadata & CSV mapping
    ├── RandomizedParallel.java    # QuickSort, Reservoir, Miller-Rabin, Blelloch, ForkJoin
    ├── SuffixStructures.java      # Suffix Array, Kasai LCP, Substring Query, LRS
    └── StringIntelligence.java    # KMP, Z-Algorithm, Rabin-Karp, Aho-Corasick, Naive
```

---

## 9. Hackathon Credentials & Compliance

- **Course / Track:** Data Structures and Algorithms (DSA) Practical Implementation
- **Restrictions:** Pure Core Java Standard Edition (JDK 17+), Java Swing, Java AWT
- **Zero Third-Party Libraries:** No external JARs or package managers required
- **License:** Open Source for Educational and Hackathon Evaluation
