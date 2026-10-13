# LifeData – Large-Scale Personal Data Analytics Platform
### A Java-based Data Structures and Algorithms (DSA) Platform for Final Review (CO1 – CO3)
**Now with Desktop Graphical User Interface (GUI), Category-Specific Benchmarking & Anti-Aliased Bar Graphs**

---

## 1. Project Title
**LifeData – Large-Scale Personal Data Analytics Platform (GUI & CLI Studio)**

## 2. Problem Statement
With the rapid proliferation of personal wearable devices, mobile health sensors, fitness trackers, and digital journaling apps, individuals generate large volumes of continuous heterogeneous personal data:
- Biometric sensor observations (heart rate, glucose, steps, sleep efficiency)
- Free-form textual notes (medical prescriptions, workout reflections, daily logs)
- Discrete event streams and identifier records (event IDs, medication notifications)
- Combinatorial activity choices subject to constraints (time limits, stamina costs, prerequisites)

Conventional software approaches relying on brute-force searches or high-level library shortcuts fail to address the core computer science requirements of low latency, linear-time stream scanning, typo-tolerant query resolution, and constrained optimization. 

**LifeData** bridges this gap by directly implementing foundational and advanced Data Structures & Algorithms (DSA) to demonstrate efficient data searching, indexing, comparison, and combinatorial optimization.

## 3. Key Enhancements for Final Review
1. **Interactive Desktop GUI Studio (`LifeDataGUI.java`):** Built entirely in pure standard Java (zero external libraries), featuring a modern dark-slate theme, tabbed navigation, and interactive verification controls.
2. **Category-Specific Algorithm Benchmarking:** Solves the "apples-to-oranges" comparison fallacy by strictly grouping and comparing algorithms within their respective theoretical problem domains:
   - **Category 1: Exact String Pattern Matchers** (Naive vs KMP vs Z-Algorithm vs Rabin-Karp vs Aho-Corasick)
   - **Category 2: Sequence Distance & Alignment Measures** (Levenshtein vs Damerau-Levenshtein vs Weighted Edit Distance vs Needleman-Wunsch vs Smith-Waterman)
   - **Category 3: Suffix Structures & Indexing** (Suffix Array & Kasai LCP vs Scan-Based Matchers on repeated query batches)
   - **Category 4: Combinatorial DP & Optimization** (Bitmask Activity Selection vs Matrix Chain Pipeline Optimization)
3. **Anti-Aliased Bar Graph Engine (`BarChartPanel.java`):** Custom 2D graphics rendering with auto-scaling Y-axes, smooth gradients, value tags, and multi-metric toggle dropdowns (Execution Time, Operations/Comparisons, Match Counts).
4. **Personal Records Manager:** In-memory record management with CRUD operations, search filters, and one-click data pipeline forwarding.
5. **Final Review Viva Dashboard & Report Exporter:** Master 15-algorithm complexity matrix, examiner viva Q&A guide, and one-click export to `LifeData_Final_Review_Report.md`.
6. **Dual Execution Modes:** Runs seamlessly as a desktop GUI or as an interactive CLI (`--cli`).

---

## 4. System Workflow & Architecture Diagram

```
+-----------------------------------------------------------------------------------+
|                        LIFEDATA ANALYTICS PLATFORM                                |
|        [Modern Desktop GUI Studio]   <--->   [Interactive Console CLI]            |
+-----------------------------------------------------------------------------------+
                                      │
                        ┌─────────────┴─────────────┐
                        ▼                           ▼
            [Interactive GUI Inputs]       [External TXT Files]
            - Record form fields            - data/personal_notes.txt
            - Query pattern & keywords      - data/activity_records.txt
            - Typo & Alignment strings      - data/event_records.txt
            - Matrix dims & Activity DP     - data/sample_large_text.txt
                        │                           │
                        └─────────────┬─────────────┘
                                      │
       ┌──────────────────────────────┼──────────────────────────────┐
       ▼                              ▼                              ▼
 [CAT 1: STRING MATCHING]       [CAT 2: DISTANCE & ALIGN]      [CAT 3: SUFFIX INDEXING]
 - Naive Search (O(nm))         - Levenshtein (Wagner-Fischer) - Suffix Array (O(n² log n))
 - KMP (O(n+m), LPS array)      - Damerau-Levenshtein          - Kasai LCP (O(n))
 - Z-Algo (O(n+m), Z-boxes)       (Adjacent Transpositions)    - Suffix Binary Search
 - Rabin-Karp (Rolling Hash)    - Weighted Edit Distance         (O(m log n) queries)
 - Aho-Corasick (Trie + BFS)    - Needleman-Wunsch (Global)
                                - Smith-Waterman (Local)
                                      │
                                      ▼
                        [CAT 4: DP & OPTIMIZATION]
                        - Bitmask DP (O(2^N · N), Subsets)
                        - Matrix Chain Mult (O(N³), MCM)
                                      │
                                      ▼
             [REAL-TIME BAR GRAPH & COMPARATIVE EVALUATION]
             - Execution Time Bar Chart (Nanoseconds)
             - Operation & Comparison Counts Bar Chart
             - Match & Score Correctness Verification
             - Final Review Viva Studio & Markdown Exporter
```

---

## 5. Functional & Non-Functional Requirements

### Functional Requirements
1. **Interactive GUI & Console CLI:** Choose between full desktop graphical studio or interactive console terminal.
2. **Category-Specific Benchmarks:** Side-by-side bar graph comparisons within identical algorithm classes.
3. **Data Source Flexibility:** Run algorithms on external `.txt` files, typed runtime text, or in-memory personal records.
4. **Intermediate Inspection:** Display LPS tables, Z-arrays, suffix sorted rankings, LCP values, alignment strings, and 2D DP cost matrices.
5. **Zero External Dependencies:** 100% pure standard Java (`javax.swing`, `java.awt`, `java.util`, `java.io`).

---

## 6. DSA-to-LifeData Mapping Table (CO1 – CO3)

| Module | Algorithm | LifeData Use Case | Time Complexity | Space Complexity |
| :--- | :--- | :--- | :--- | :--- |
| **Cat 1** | **Naive Search** | Baseline comparative search for health terms in logs | $O(n \cdot m)$ | $O(1)$ |
| **Cat 1** | **KMP Search** | Exact keyword search in personal notes without text pointer backtracking | $O(n + m)$ | $O(m)$ |
| **Cat 1** | **Z-Algorithm** | Identifies periodic telemetry cycles and repeated patterns in sensor logs | $O(n + m)$ | $O(n + m)$ |
| **Cat 1** | **Rabin-Karp** | Fast single & multi-record ID lookups via polynomial rolling hash | $O(n + m)$ avg | $O(1)$ |
| **Cat 1** | **Aho-Corasick** | Real-time simultaneous multi-keyword tagging across activity streams | $O(n + z)$ | $O(\Sigma \cdot m)$ |
| **Cat 2** | **Levenshtein** | Wagner-Fischer edit distance for typo tolerance in user queries | $O(n \cdot m)$ | $O(n \cdot m)$ |
| **Cat 2** | **Damerau-Levenshtein** | Adjacent transposition-aware distance for touchscreen/keyboard typos | $O(n \cdot m)$ | $O(n \cdot m)$ |
| **Cat 2** | **Weighted Edit Distance**| Non-uniform costs for asymmetric user data-entry errors | $O(n \cdot m)$ | $O(n \cdot m)$ |
| **Cat 2** | **Needleman-Wunsch** | End-to-end global sequence alignment of activity streams | $O(n \cdot m)$ | $O(n \cdot m)$ |
| **Cat 2** | **Smith-Waterman** | Local region alignment to discover common behavioral episodes | $O(n \cdot m)$ | $O(n \cdot m)$ |
| **Cat 3** | **Suffix Array** | Lexicographical indexing of large collections of notes for fast queries | $O(n^2 \log n)$ | $O(n)$ |
| **Cat 3** | **Kasai LCP** | Identifies recurrent phrases and clinical recommendations across records | $O(n)$ | $O(n)$ |
| **Cat 3** | **Suffix Search** | Substring existence checks via binary search over sorted suffixes | $O(m \log n)$ | $O(1)$ |
| **Cat 4** | **Bitmask DP** | Explores combinations of user activities subject to time & stamina budgets | $O(2^N \cdot N)$ | $O(2^N)$ |
| **Cat 4** | **Matrix Chain Mult** | Optimizes evaluation order for multi-stage sensor processing pipelines | $O(n^3)$ | $O(n^2)$ |

---

## 7. Step-by-Step Instructions to Run the Project

### Running the Desktop GUI (Recommended for Final Review)
1. Navigate to the project root directory:
   ```bash
   cd c:\Users\LUCKY\eclipse-workspace\LifeData
   ```
2. Compile all Java source files:
   ```bash
   javac -d bin src/*.java
   ```
3. Launch the GUI Application:
   ```bash
   java -cp bin LifeDataGUI
   ```
   *(Or run `java -cp bin Main` directly – it automatically detects the desktop environment and launches the GUI!)*

### Running the Interactive Console CLI
If you prefer running in command line terminal mode:
```bash
java -cp bin Main --cli
```

### Running in Eclipse IDE
1. Open Eclipse and choose `c:\Users\LUCKY\eclipse-workspace` as the workspace.
2. File -> Import -> General -> Projects from Folder or Archive -> select `LifeData`.
3. Right click `LifeDataGUI.java` (or `Main.java`) inside `src`.
4. Select **Run As -> Java Application**.
5. The Desktop GUI window will open immediately with all tabs, bar graphs, and controls.

---

## 8. Sample Demonstration Walkthrough (Review-1 Order)

### Step 1: Add a Personal Record (Option 1)
- **Input:**
  - Record ID: `REC-105`
  - Date: `2026-10-02`
  - Activity: `Walking`
  - Category: `Fitness`
  - Note: `I walked for 5000 steps and checked my health today.`
  - Sensor Value: `82 bpm`
  - Preference: `Outdoor`
- **Output:** Stored record confirmed and displayed.

### Step 2: Search Keyword using KMP (Option 4)
- **Input:**
  - Choose Option 1 (Load from external TXT file)
  - File path: `data/personal_notes.txt`
  - Keyword: `exercise`
- **Output:**
  - File size: 1484 characters
  - LPS Array: `[ 0 0 1 0 0 0 0 1 ]`
  - Matches found at: `[66, 454, 1430]`
  - Total Matches: 3

### Step 3: Search Multiple Keywords using Aho-Corasick (Option 7)
- **Input:**
  - File path: `data/personal_notes.txt`
  - Keywords: `health, sleep, exercise, walking, food`
- **Output:**
  - `health`: 2 matches at `[533, 1286]`
  - `sleep`: 5 matches at `[616, 677, 706, 1065, 1467]`
  - `exercise`: 3 matches at `[66, 454, 1430]`
  - `walking`: 1 match at `[1421]`
  - `food`: 2 matches at `[957, 1449]`

### Step 4: Suffix Array and Kasai LCP (Options 8 & 9)
- **Input:** Text: `banana`
- **Output:**
  - Suffix Array: `[ 5 3 1 0 4 2 ]`
  - Kasai LCP Array: `[ 0 1 3 0 0 2 ]`
  - Longest Repeated Substring: `"ana"`

### Step 5: Damerau-Levenshtein Distance (Option 12)
- **Input:** String 1: `form`, String 2: `from`
- **Output:**
  - DP Table displayed
  - Damerau-Levenshtein Distance = 1 (1 transposition)
  - Standard Levenshtein Distance = 2
  - Transposition detected: `true`

### Step 6: Needleman-Wunsch Global Alignment (Option 14)
- **Input:** Sequence 1: `EXERCISE`, Sequence 2: `ENERGIZE`
- **Output:**
  - Final Global Alignment Score = 3
  - Aligned 1: `EXERC-ISE`
  - Aligned 2: `E-NERGIZE`

### Step 7: Bitmask DP Activity Selection (Option 16)
- **Input:** Maximum available time: `90` minutes
- **Output:**
  - 32 subsets explored ($2^5$)
  - Optimal Bitmask: `0b1110` (decimal: 14)
  - Total time: `90 / 90 mins`
  - Total utility: `110 points`
  - Chosen activities: `Reading`, `Walking`, `Meditation`

### Step 8: Matrix Chain Multiplication (Option 17)
- **Input:** Number of matrices: `4`, Dimensions: `10 20 30 40 30`
- **Output:**
  - DP Cost Table displayed
  - Optimal Multiplication Cost: `30000 scalar operations`
  - Optimal Parenthesization: `(((A1 x A2) x A3) x A4)`

### Step 9: Complexity Analysis Matrix (Option 18)
- Displays complete summary table with theoretical and space bounds.

---

## 9. Review-1 Faculty Viva Preparation

### Q1: Why is KMP better than Naive search?
> **Answer:** In Naive search, when a mismatch occurs after matching $j$ characters, the text pointer $i$ is reset back to $i - j + 1$, resulting in $O(n \cdot m)$ worst-case time. KMP precomputes the LPS (Longest Proper Prefix which is also a Suffix) array in $O(m)$. On mismatch, the text pointer never backtracks; only the pattern pointer falls back using the LPS value. Thus, KMP runs in deterministic $O(n + m)$ time.

### Q2: How does Kasai's algorithm compute LCP in O(n) time?
> **Answer:** Kasai's algorithm uses the key invariant that if suffix $i$ has LCP length $h$ with its predecessor in the suffix array, then suffix $i+1$ has an LCP of at least $h - 1$ with its predecessor. Therefore, the character comparison counter $h$ decrements at most once per suffix and can increment at most $n$ times overall, bounding total character comparisons to $2n$, which is strictly $O(n)$.

### Q3: What is the difference between Levenshtein and Damerau-Levenshtein?
> **Answer:** Classic Levenshtein allows three operations: insertion, deletion, and substitution. Damerau-Levenshtein adds adjacent character transposition (e.g. `teh` $\rightarrow$ `the` or `form` $\rightarrow$ `from`). Classic Levenshtein treats this as 2 operations (delete + insert), whereas Damerau-Levenshtein treats it as a single swap with cost 1. This accurately models over 80% of human typing slips.

### Q4: What is the difference between Needleman-Wunsch and Smith-Waterman?
> **Answer:** Needleman-Wunsch performs **global alignment** across the entire length of both sequences (penalizing leading and trailing gaps). Smith-Waterman performs **local alignment** by resetting negative cell values to 0. It finds the highest-scoring local matching subsequence anywhere within two logs, without forcing end-to-end alignment.

### Q5: Why use Bitmask DP for activity selection?
> **Answer:** When selecting from $N$ candidate activities subject to time or energy budgets, greedy choices fail because individual activity durations and utilities do not exhibit the greedy-choice property. A brute-force search takes $O(2^N)$ without memoization. Bitmask DP models any subset as a binary integer `mask` $\in [0, 2^N - 1]$ and solves the problem in $O(2^N \cdot N)$ using bottom-up state transitions.

---

## 10. 2–3 Minute Speech Script for Faculty Review

> *"Good morning respected faculty. Today I present my Review-1 project: **LifeData – Large-Scale Personal Data Analytics Platform**.*
>
> *Personal wearables and lifestyle applications generate continuous, heterogeneous data: sensor observations, unstructured health notes, event logs, and daily preference choices. Our objective is to manage, search, index, and optimize this personal data using core and advanced DSA concepts without relying on high-level library shortcuts.*
>
> *In **Module 1 (CO1/CO2)**, we implement string matching algorithms. For searching medical notes, we implemented **KMP** with LPS arrays to guarantee $O(n+m)$ search without text backtracking. For anomaly cycle detection in sensor streams, we implemented the **Z-Algorithm**. For event ID lookups, we implemented **Rabin-Karp** with rolling polynomial hashes. And for real-time streaming tags like 'health', 'sleep', 'exercise', we implemented the **Aho-Corasick** automaton using Trie and failure links to search multiple keywords in a single linear pass.*
>
> *In **Module 2 (CO2)**, for large document collections, we implemented **Suffix Arrays** and **Kasai's algorithm**, which constructs the Longest Common Prefix (LCP) array in linear $O(n)$ time. This enables instant substring querying via binary search and mines repeated lifestyle advice across records.*
>
> *In **Module 3 (CO3)**, we implemented advanced Dynamic Programming: **Levenshtein** and **Damerau-Levenshtein** for autocorrecting user typos on touchscreens, **Needleman-Wunsch** and **Smith-Waterman** for sequence alignment of activity logs, **Bitmask DP** to find the optimal daily schedule maximizing user utility within strict time limits, and **Matrix Chain Multiplication** using Interval DP to optimize the execution order of data processing pipelines.*
>
> *All algorithms accept runtime input via Scanner and can process external `.txt` files directly. I am now ready to demonstrate the live application."*
