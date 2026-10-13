import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Main Driver for LifeData Platform.
 * Supports both modern Graphical User Interface (GUI) and Interactive CLI for Final Review.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<PersonalRecord> recordList = new ArrayList<>();

    public static void main(String[] args) {
        boolean forceCli = false;
        for (String arg : args) {
            if ("--cli".equalsIgnoreCase(arg)) {
                forceCli = true;
                break;
            }
        }

        // Launch GUI by default if desktop environment is available
        if (!forceCli && !GraphicsEnvironment.isHeadless()) {
            System.out.println("Launching LifeData Analytics Studio GUI...");
            LifeDataGUI.main(args);
            return;
        }

        initializeSampleRecords();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice (1-20): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addPersonalRecord();
                    break;
                case "2":
                    displayAllRecords();
                    break;
                case "3":
                    handleNaiveSearch();
                    break;
                case "4":
                    handleKMP();
                    break;
                case "5":
                    handleZAlgorithm();
                    break;
                case "6":
                    handleRabinKarp();
                    break;
                case "7":
                    handleAhoCorasick();
                    break;
                case "8":
                    handleSuffixArray();
                    break;
                case "9":
                    handleKasaiLCP();
                    break;
                case "10":
                    handleSuffixArraySearch();
                    break;
                case "11":
                    handleLevenshtein();
                    break;
                case "12":
                    handleDamerauLevenshtein();
                    break;
                case "13":
                    handleWeightedEditDistance();
                    break;
                case "14":
                    handleNeedlemanWunsch();
                    break;
                case "15":
                    handleSmithWaterman();
                    break;
                case "16":
                    handleBitmaskDP();
                    break;
                case "17":
                    handleMatrixChain();
                    break;
                case "18":
                    displayComplexityComparison();
                    break;
                case "19":
                    System.out.println("\nLaunching LifeData Analytics Studio GUI...");
                    LifeDataGUI.main(new String[]{});
                    break;
                case "20":
                    System.out.println("\nThank you for using LifeData Analytics Platform. Good luck with the Final Review!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 20.");
            }

            if (running) {
                System.out.println("\nPress Enter to return to the main menu...");
                scanner.nextLine();
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n=======================================================");
        System.out.println("            LIFEDATA ANALYTICS PLATFORM");
        System.out.println("=======================================================");
        System.out.println(" 1. Add Personal Record");
        System.out.println(" 2. Display All Records");
        System.out.println("\n--- STRING ALGORITHMS (MODULE 1 / CO1) ---");
        System.out.println(" 3. Search Keyword using Naive String Matching");
        System.out.println(" 4. Search Keyword using KMP");
        System.out.println(" 5. Search Pattern using Z-Algorithm");
        System.out.println(" 6. Search Record/Event ID using Rabin-Karp");
        System.out.println(" 7. Search Multiple Keywords using Aho-Corasick");
        System.out.println("\n--- SUFFIX STRUCTURES (MODULE 2 / CO2) ---");
        System.out.println(" 8. Build Suffix Array");
        System.out.println(" 9. Find LCP using Kasai Algorithm");
        System.out.println(" 10. Search Substring using Suffix Array");
        System.out.println("\n--- ADVANCED DYNAMIC PROGRAMMING (MODULE 3 / CO3) ---");
        System.out.println(" 11. Compare Two Records using Levenshtein Distance");
        System.out.println(" 12. Compare Two Records using Damerau-Levenshtein Distance");
        System.out.println(" 13. Weighted Edit Distance");
        System.out.println(" 14. Needleman-Wunsch Sequence Alignment");
        System.out.println(" 15. Smith-Waterman Local Alignment");
        System.out.println(" 16. Bitmask DP for Activity/Preference Selection");
        System.out.println(" 17. Matrix Chain Multiplication");
        System.out.println("\n--- FINAL REVIEW EVALUATION & BENCHMARKS ---");
        System.out.println(" 18. Compare Algorithm Complexity / Information");
        System.out.println(" 19. Launch Graphical User Interface (GUI Studio)");
        System.out.println(" 20. Exit");
        System.out.println("=======================================================");
    }

    private static void initializeSampleRecords() {
        recordList.add(new PersonalRecord("REC-101", "2026-10-01", "Jogging", "Fitness", 
                "Morning outdoor jogging for 4 km", "148 bpm", "Outdoor"));
        recordList.add(new PersonalRecord("REC-102", "2026-10-01", "Meditation", "Mental Health", 
                "Guided breathing session after work", "64 bpm", "Indoor"));
        recordList.add(new PersonalRecord("REC-103", "2026-10-02", "Walking", "Fitness", 
                "I walked for 5000 steps and checked my health today", "82 bpm", "Outdoor"));
        recordList.add(new PersonalRecord("REC-104", "2026-10-02", "Gym Workout", "Fitness", 
                "Core strength training and free weights", "410 kcal", "Gym"));
    }

    // =========================================================================
    // 1 & 2: PERSONAL RECORD MANAGEMENT
    // =========================================================================
    private static void addPersonalRecord() {
        System.out.println("\n--- ADD PERSONAL RECORD ---");
        System.out.print("Enter Record ID (e.g. REC-105): ");
        String id = scanner.nextLine().trim();
        System.out.print("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();
        System.out.print("Enter Activity (e.g. Cycling, Sleep, Reading): ");
        String activity = scanner.nextLine().trim();
        System.out.print("Enter Category (e.g. Fitness, Recovery, Focus): ");
        String category = scanner.nextLine().trim();
        System.out.print("Enter Personal Note: ");
        String note = scanner.nextLine().trim();
        System.out.print("Enter Sensor Value (e.g. 72 bpm, 500 kcal, 94%): ");
        String sensor = scanner.nextLine().trim();
        System.out.print("Enter Preference (e.g. Outdoor, Indoor, Morning): ");
        String preference = scanner.nextLine().trim();

        PersonalRecord record = new PersonalRecord(id, date, activity, category, note, sensor, preference);
        recordList.add(record);
        System.out.println("\n[SUCCESS] Personal Record added successfully!");
        System.out.println(record);
    }

    private static void displayAllRecords() {
        System.out.println("\n--- ALL STORED PERSONAL RECORDS (" + recordList.size() + " total) ---");
        if (recordList.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        for (PersonalRecord r : recordList) {
            System.out.println(r);
        }
    }

    // =========================================================================
    // HELPER: SOURCE TEXT SELECTION (Manual input vs TXT file vs Stored Records)
    // =========================================================================
    private static String obtainTextSource() {
        System.out.println("\nChoose Text Source:");
        System.out.println("  1. Load from external TXT file (e.g., data/personal_notes.txt)");
        System.out.println("  2. Enter custom text directly at runtime");
        System.out.println("  3. Use all currently stored Personal Records text");
        System.out.print("Enter option (1-3, default 1): ");
        String opt = scanner.nextLine().trim();

        if ("2".equals(opt)) {
            System.out.print("Enter text: ");
            return scanner.nextLine();
        } else if ("3".equals(opt)) {
            StringBuilder sb = new StringBuilder();
            for (PersonalRecord r : recordList) {
                sb.append(r.getSearchableContent()).append("\n");
            }
            return sb.toString();
        } else {
            System.out.print("Enter TXT file path (press Enter for 'data/personal_notes.txt'): ");
            String path = scanner.nextLine().trim();
            if (path.isEmpty()) {
                path = "data/personal_notes.txt";
            }
            return readTextFile(path);
        }
    }

    private static String readTextFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[WARNING] File '" + filePath + "' not found. Falling back to default personal note sample.");
            return "I walked for 5000 steps and checked my health today. Consistency in daily walking and healthy food improves vitality.";
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            System.out.printf("[FILE LOADED] '%s' (%d characters)%n", filePath, content.length());
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to read file: " + e.getMessage());
            return "";
        }
        return content.toString();
    }

    // =========================================================================
    // 3: NAIVE STRING MATCHING
    // =========================================================================
    private static void handleNaiveSearch() {
        System.out.println("\n========================================");
        System.out.println("      NAIVE STRING MATCHING SEARCH");
        System.out.println("========================================");

        String text = obtainTextSource();
        if (text.isEmpty()) return;

        System.out.print("Enter keyword/pattern to search: ");
        String pattern = scanner.nextLine().trim();

        NaiveSearch.SearchResult res = NaiveSearch.search(text, pattern);

        System.out.println("\n--- NAIVE MATCH RESULTS ---");
        System.out.println("Algorithm Used: Naive Brute-Force String Matching");
        System.out.println("Text Size:      " + text.length() + " characters");
        System.out.println("Pattern:        \"" + pattern + "\" (" + pattern.length() + " characters)");
        System.out.println("Total Matches:  " + res.positions.size());
        System.out.println("Comparisons:    " + res.comparisons);
        System.out.println("Execution Time: " + res.executionTimeNs + " ns");
        System.out.println("Matching Positions (0-based): " + res.positions);
    }

    // =========================================================================
    // 4: KMP SEARCH
    // =========================================================================
    private static void handleKMP() {
        System.out.println("\n========================================");
        System.out.println("         KMP STRING SEARCH");
        System.out.println("========================================");

        String text = obtainTextSource();
        if (text.isEmpty()) return;

        System.out.print("Enter keyword/pattern to search: ");
        String pattern = scanner.nextLine().trim();

        KMP.KmpResult res = KMP.search(text, pattern);

        System.out.println("\n--- KMP MATCH RESULTS ---");
        System.out.println("Algorithm Used: Knuth-Morris-Pratt (KMP)");
        System.out.println("Text Size:      " + text.length() + " characters");
        System.out.println("Pattern:        \"" + pattern + "\"");
        
        System.out.print("LPS (pi) Array: [ ");
        for (int val : res.lps) System.out.print(val + " ");
        System.out.println("]");

        System.out.println("Total Matches:  " + res.positions.size());
        System.out.println("Comparisons:    " + res.comparisons);
        System.out.println("Execution Time: " + res.executionTimeNs + " ns");
        System.out.println("Matching Positions (0-based): " + res.positions);
    }

    // =========================================================================
    // 5: Z-ALGORITHM
    // =========================================================================
    private static void handleZAlgorithm() {
        System.out.println("\n========================================");
        System.out.println("        Z-ALGORITHM PATTERN SEARCH");
        System.out.println("========================================");

        String text = obtainTextSource();
        if (text.isEmpty()) return;

        System.out.print("Enter pattern to search: ");
        String pattern = scanner.nextLine().trim();

        ZAlgorithm.ZResult res = ZAlgorithm.search(text, pattern);

        System.out.println("\n--- Z-ALGORITHM RESULTS ---");
        System.out.println("Algorithm Used: Z-Algorithm (Prefix Matching)");
        System.out.println("Constructed:    pattern + \"$\" + text");
        System.out.println("Text Size:      " + text.length() + " characters");
        System.out.println("Pattern:        \"" + pattern + "\"");

        // Display snippet of Z-array
        System.out.print("Z-Array Preview (first 25 values): [ ");
        int previewLimit = Math.min(25, res.zArray.length);
        for (int i = 0; i < previewLimit; i++) {
            System.out.print(res.zArray[i] + " ");
        }
        if (res.zArray.length > previewLimit) System.out.print("...");
        System.out.println("]");

        System.out.println("Total Matches:  " + res.positions.size());
        System.out.println("Execution Time: " + res.executionTimeNs + " ns");
        System.out.println("Matching Positions (0-based): " + res.positions);
    }

    // =========================================================================
    // 6: RABIN-KARP SEARCH
    // =========================================================================
    private static void handleRabinKarp() {
        System.out.println("\n========================================");
        System.out.println("       RABIN-KARP ROLLING HASH");
        System.out.println("========================================");

        String text = obtainTextSource();
        if (text.isEmpty()) return;

        System.out.print("Enter Record/Event ID or keyword to search (e.g. REC-104, EVT-206): ");
        String pattern = scanner.nextLine().trim();

        RabinKarp.RkResult res = RabinKarp.search(text, pattern);

        System.out.println("\n--- RABIN-KARP RESULTS ---");
        System.out.println("Algorithm Used:     Rabin-Karp with Polynomial Rolling Hash");
        System.out.println("Pattern Hash:       " + res.patternHash);
        System.out.println("Initial Window Hash:" + res.initialWindowHash);
        System.out.println("Pattern:            \"" + pattern + "\"");
        System.out.println("Total Matches:      " + res.positions.size());
        System.out.println("Total Comparisons:  " + res.comparisons);
        System.out.println("Spurious Hits:      " + res.spuriousHits + " (hash collisions avoided)");
        System.out.println("Execution Time:     " + res.executionTimeNs + " ns");
        System.out.println("Matching Positions: " + res.positions);
    }

    // =========================================================================
    // 7: AHO-CORASICK MULTI-KEYWORD SEARCH
    // =========================================================================
    private static void handleAhoCorasick() {
        System.out.println("\n========================================");
        System.out.println("    AHO-CORASICK MULTI-KEYWORD SEARCH");
        System.out.println("========================================");

        String text = obtainTextSource();
        if (text.isEmpty()) return;

        System.out.println("Enter keywords separated by comma (e.g.: health, sleep, exercise, walking, food):");
        System.out.print("Keywords: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            input = "health, sleep, exercise, walking, food";
        }

        String[] parts = input.split(",");
        List<String> keywords = new ArrayList<>();
        for (String p : parts) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) keywords.add(trimmed);
        }

        AhoCorasick ac = new AhoCorasick(keywords);
        AhoCorasick.MatchResult res = ac.search(text);

        System.out.println("\n--- AHO-CORASICK RESULTS ---");
        System.out.println("Algorithm Used: Aho-Corasick Automaton (Trie + Failure Links + Output Links)");
        System.out.println("Text Size:      " + text.length() + " characters");
        System.out.println("Execution Time: " + res.executionTimeNs + " ns");
        System.out.println("\nKeyword          | Occurrences | Matching Positions");
        System.out.println("----------------------------------------------------------------------");

        for (Map.Entry<String, List<Integer>> entry : res.keywordPositions.entrySet()) {
            String kw = entry.getKey();
            List<Integer> pos = entry.getValue();
            System.out.printf("%-16s | %-11d | %s%n", kw, pos.size(), pos.isEmpty() ? "None" : pos.toString());
        }

        System.out.println("\n[VIVA NOTE] Aho-Corasick processes all keywords simultaneously in O(n + matches)");
        System.out.println("without scanning the text multiple times.");
    }

    // =========================================================================
    // 8: SUFFIX ARRAY
    // =========================================================================
    private static void handleSuffixArray() {
        System.out.println("\n========================================");
        System.out.println("          BUILD SUFFIX ARRAY");
        System.out.println("========================================");

        System.out.println("1. Enter custom short word (e.g. 'banana', 'exercise') for step-by-step trace");
        System.out.println("2. Load text from file or records");
        System.out.print("Choose option (1 or 2, default 1): ");
        String opt = scanner.nextLine().trim();

        String text;
        if ("2".equals(opt)) {
            text = obtainTextSource();
        } else {
            System.out.print("Enter string (default 'banana'): ");
            text = scanner.nextLine().trim();
            if (text.isEmpty()) text = "banana";
        }

        SuffixArray sa = new SuffixArray(text);
        SuffixArray.SuffixItem[] original = sa.getAllOriginalSuffixes();
        int[] sortedSA = sa.getSuffixArray();

        System.out.println("\n--- ORIGINAL SUFFIXES ---");
        System.out.println("Suffix Index | Suffix Substring");
        System.out.println("----------------------------------------");
        int displayLimit = Math.min(15, original.length);
        for (int i = 0; i < displayLimit; i++) {
            System.out.printf("%-12d | %s%n", original[i].index, original[i].suffix);
        }
        if (original.length > displayLimit) System.out.println("... (" + (original.length - displayLimit) + " more)");

        System.out.println("\n--- SORTED SUFFIX ARRAY ---");
        System.out.print("Suffix Array (SA): [ ");
        for (int i = 0; i < displayLimit; i++) {
            System.out.print(sortedSA[i] + " ");
        }
        if (sortedSA.length > displayLimit) System.out.print("... ");
        System.out.println("]");
    }

    // =========================================================================
    // 9: KASAI LCP ARRAY
    // =========================================================================
    private static void handleKasaiLCP() {
        System.out.println("\n========================================");
        System.out.println("        KASAI LCP ARRAY BUILDER");
        System.out.println("========================================");

        System.out.print("Enter string (press Enter for 'banana', or type a custom note): ");
        String text = scanner.nextLine().trim();
        if (text.isEmpty()) text = "banana";

        SuffixArray sa = new SuffixArray(text);
        int[] suffixArray = sa.getSuffixArray();
        int[] lcp = KasaiLCP.buildLCP(text, suffixArray);

        System.out.println("\n--- KASAI LCP RESULTS ---");
        System.out.println("Text: \"" + text + "\"");
        System.out.println("Rank | SA[i] | LCP[i] | Suffix");
        System.out.println("----------------------------------------");
        for (int i = 0; i < text.length(); i++) {
            System.out.printf("%-4d | %-5d | %-6d | %s%n", i, suffixArray[i], lcp[i], text.substring(suffixArray[i]));
        }

        String longestRepeated = KasaiLCP.getLongestRepeatedSubstring(text, suffixArray, lcp);
        System.out.println("\nLongest Repeated Substring / Phrase: \"" + longestRepeated + "\"");
        System.out.println("[VIVA NOTE] Kasai's algorithm runs in O(n) linear time because the match pointer");
        System.out.println("decrements at most once per suffix.");
    }

    // =========================================================================
    // 10: SUFFIX ARRAY SUBSTRING SEARCH
    // =========================================================================
    private static void handleSuffixArraySearch() {
        System.out.println("\n========================================");
        System.out.println("      SUFFIX ARRAY SUBSTRING SEARCH");
        System.out.println("========================================");

        String text = obtainTextSource();
        if (text.isEmpty()) return;

        SuffixArray sa = new SuffixArray(text);

        System.out.print("Enter query substring to search: ");
        String query = scanner.nextLine().trim();

        long startTime = System.nanoTime();
        List<Integer> matches = sa.searchSubstring(query);
        long elapsed = System.nanoTime() - startTime;

        System.out.println("\n--- SUFFIX ARRAY SEARCH RESULTS ---");
        System.out.println("Query:          \"" + query + "\"");
        System.out.println("Status:         " + (!matches.isEmpty() ? "Substring found!" : "Not found."));
        System.out.println("Total Matches:  " + matches.size());
        System.out.println("Positions:      " + matches);
        System.out.println("Execution Time: " + elapsed + " ns (O(m * log n) Binary Search)");
    }

    // =========================================================================
    // 11: LEVENSHTEIN DISTANCE
    // =========================================================================
    private static void handleLevenshtein() {
        System.out.println("\n========================================");
        System.out.println("     LEVENSHTEIN / WAGNER-FISCHER");
        System.out.println("========================================");

        System.out.print("Enter String 1 (default 'health'): ");
        String s1 = scanner.nextLine().trim();
        if (s1.isEmpty()) s1 = "health";

        System.out.print("Enter String 2 (default 'healt'): ");
        String s2 = scanner.nextLine().trim();
        if (s2.isEmpty()) s2 = "healt";

        Levenshtein.Result res = Levenshtein.compute(s1, s2);

        System.out.println("\n--- DP TABLE ---");
        Levenshtein.printTable(s1, s2, res.dpTable);

        System.out.println("\nEdit Distance = " + res.distance);
        System.out.println("\nStep-by-step Transformation Path:");
        for (String step : res.editSteps) {
            System.out.println("  * " + step);
        }

        System.out.println("\n[VIVA NOTE] Allowed operations: Insertion (+1), Deletion (+1), Replacement (+1).");
        System.out.println("Time Complexity: O(n * m) | Space Complexity: O(n * m)");
    }

    // =========================================================================
    // 12: DAMERAU-LEVENSHTEIN DISTANCE
    // =========================================================================
    private static void handleDamerauLevenshtein() {
        System.out.println("\n========================================");
        System.out.println("    DAMERAU-LEVENSHTEIN (TRANSPOSITION)");
        System.out.println("========================================");

        System.out.print("Enter String 1 (default 'form'): ");
        String s1 = scanner.nextLine().trim();
        if (s1.isEmpty()) s1 = "form";

        System.out.print("Enter String 2 (default 'from'): ");
        String s2 = scanner.nextLine().trim();
        if (s2.isEmpty()) s2 = "from";

        DamerauLevenshtein.Result res = DamerauLevenshtein.compute(s1, s2);
        Levenshtein.Result standardLev = Levenshtein.compute(s1, s2);

        System.out.println("\n--- DP TABLE ---");
        DamerauLevenshtein.printTable(s1, s2, res.dpTable);

        System.out.println("\nDamerau-Levenshtein Distance = " + res.distance);
        System.out.println("Standard Levenshtein Distance  = " + standardLev.distance);
        System.out.println("Transposition detected: " + res.usedTransposition);

        System.out.println("\n[VIVA NOTE] Adjacent transposition (swapping adjacent characters like 'or' -> 'ro')");
        System.out.println("is counted as 1 operation in Damerau-Levenshtein, whereas standard Levenshtein counts it as 2.");
    }

    // =========================================================================
    // 13: WEIGHTED EDIT DISTANCE
    // =========================================================================
    private static void handleWeightedEditDistance() {
        System.out.println("\n========================================");
        System.out.println("        WEIGHTED EDIT DISTANCE");
        System.out.println("========================================");

        System.out.print("Enter String 1 (default 'walking'): ");
        String s1 = scanner.nextLine().trim();
        if (s1.isEmpty()) s1 = "walking";

        System.out.print("Enter String 2 (default 'walked'): ");
        String s2 = scanner.nextLine().trim();
        if (s2.isEmpty()) s2 = "walked";

        System.out.print("Enter Insertion Cost (default 1.0): ");
        String insStr = scanner.nextLine().trim();
        double insCost = insStr.isEmpty() ? 1.0 : Double.parseDouble(insStr);

        System.out.print("Enter Deletion Cost (default 1.0): ");
        String delStr = scanner.nextLine().trim();
        double delCost = delStr.isEmpty() ? 1.0 : Double.parseDouble(delStr);

        System.out.print("Enter Replacement Cost (default 2.0): ");
        String repStr = scanner.nextLine().trim();
        double repCost = repStr.isEmpty() ? 2.0 : Double.parseDouble(repStr);

        WeightedEditDistance.Result res = WeightedEditDistance.compute(s1, s2, insCost, delCost, repCost);

        System.out.println("\n--- DP TABLE ---");
        WeightedEditDistance.printTable(s1, s2, res.dpTable);

        System.out.printf("%nMinimum Weighted Edit Distance = %.2f%n", res.totalCost);
        System.out.println("[VIVA NOTE] Weighted edit distance allows custom penalties for non-uniform data errors.");
    }

    // =========================================================================
    // 14: NEEDLEMAN-WUNSCH GLOBAL ALIGNMENT
    // =========================================================================
    private static void handleNeedlemanWunsch() {
        System.out.println("\n========================================");
        System.out.println("  NEEDLEMAN-WUNSCH GLOBAL ALIGNMENT");
        System.out.println("========================================");

        System.out.print("Enter Sequence 1 (default 'EXERCISE'): ");
        String s1 = scanner.nextLine().trim();
        if (s1.isEmpty()) s1 = "EXERCISE";

        System.out.print("Enter Sequence 2 (default 'ENERGIZE'): ");
        String s2 = scanner.nextLine().trim();
        if (s2.isEmpty()) s2 = "ENERGIZE";

        NeedlemanWunsch.AlignmentResult res = NeedlemanWunsch.align(s1, s2, 1, -1, -1);

        System.out.println("\n--- DP SCORING TABLE ---");
        NeedlemanWunsch.printTable(s1, s2, res.dpTable);

        System.out.println("\nFinal Global Alignment Score = " + res.score);
        System.out.println("Aligned Sequence 1: " + res.alignedSeq1);
        System.out.println("Aligned Sequence 2: " + res.alignedSeq2);
        System.out.println("\n[VIVA NOTE] Performs global end-to-end alignment using dynamic programming.");
    }

    // =========================================================================
    // 15: SMITH-WATERMAN LOCAL ALIGNMENT
    // =========================================================================
    private static void handleSmithWaterman() {
        System.out.println("\n========================================");
        System.out.println("   SMITH-WATERMAN LOCAL ALIGNMENT");
        System.out.println("========================================");

        System.out.print("Enter Sequence 1 (default 'MORNING_WALK_ROUTINE'): ");
        String s1 = scanner.nextLine().trim();
        if (s1.isEmpty()) s1 = "MORNING_WALK_ROUTINE";

        System.out.print("Enter Sequence 2 (default 'EVENING_WALK_SESSION'): ");
        String s2 = scanner.nextLine().trim();
        if (s2.isEmpty()) s2 = "EVENING_WALK_SESSION";

        SmithWaterman.AlignmentResult res = SmithWaterman.align(s1, s2, 2, -1, -1);

        System.out.println("\n--- DP SCORING TABLE ---");
        SmithWaterman.printTable(s1, s2, res.dpTable);

        System.out.println("\nMaximum Local Alignment Score = " + res.maxScore);
        System.out.println("Local Aligned Substring 1: " + res.alignedSeq1);
        System.out.println("Local Aligned Substring 2: " + res.alignedSeq2);
        System.out.println("\n[VIVA NOTE] Performs local alignment by clamping negative matrix values to 0.");
    }

    // =========================================================================
    // 16: BITMASK DP
    // =========================================================================
    private static void handleBitmaskDP() {
        System.out.println("\n========================================");
        System.out.println("  BITMASK DP FOR ACTIVITY SELECTION");
        System.out.println("========================================");

        List<BitmaskDP.Activity> activities = new ArrayList<>();
        activities.add(new BitmaskDP.Activity("Exercise", 45, 50));
        activities.add(new BitmaskDP.Activity("Reading", 30, 35));
        activities.add(new BitmaskDP.Activity("Walking", 40, 45));
        activities.add(new BitmaskDP.Activity("Meditation", 20, 30));
        activities.add(new BitmaskDP.Activity("Study", 60, 70));

        System.out.println("Candidate Daily Activities (User Preferences):");
        for (int i = 0; i < activities.size(); i++) {
            System.out.println("  [" + i + "] " + activities.get(i));
        }

        System.out.print("\nEnter Maximum Available Time in Minutes (default 90): ");
        String timeStr = scanner.nextLine().trim();
        int maxTime = timeStr.isEmpty() ? 90 : Integer.parseInt(timeStr);

        BitmaskDP.OptimizationResult res = BitmaskDP.optimize(activities, maxTime);

        System.out.println("\n--- BITMASK DP RESULTS ---");
        System.out.println("Total Subsets Explored: 2^" + activities.size() + " (" + (1 << activities.size()) + " states)");
        System.out.println("Optimal Bitmask:        0b" + Integer.toBinaryString(res.optimalMask) + " (decimal: " + res.optimalMask + ")");
        System.out.println("Total Time Spent:       " + res.totalTimeSpent + " / " + maxTime + " mins");
        System.out.println("Total Utility Score:    " + res.maxUtility + " points");
        System.out.println("\nSelected Activities for the Optimal Daily Schedule:");
        for (BitmaskDP.Activity a : res.selectedActivities) {
            System.out.println("  * " + a.name + " (" + a.timeMinutes + " mins, +" + a.utilityValue + " utility)");
        }

        System.out.println("\n[VIVA NOTE] Bitmask DP models state subsets as binary integers in O(2^N * N).");
    }

    // =========================================================================
    // 17: MATRIX CHAIN MULTIPLICATION
    // =========================================================================
    private static void handleMatrixChain() {
        System.out.println("\n========================================");
        System.out.println("      MATRIX CHAIN MULTIPLICATION");
        System.out.println("========================================");

        System.out.print("Enter number of matrices (default 4): ");
        String nStr = scanner.nextLine().trim();
        int n = nStr.isEmpty() ? 4 : Integer.parseInt(nStr);

        System.out.println("Enter " + (n + 1) + " dimensions separated by spaces (default '10 20 30 40 30'):");
        System.out.print("Dimensions: ");
        String dimStr = scanner.nextLine().trim();

        int[] p;
        if (dimStr.isEmpty()) {
            p = new int[]{10, 20, 30, 40, 30};
        } else {
            String[] tokens = dimStr.split("\\s+");
            p = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                p[i] = Integer.parseInt(tokens[i]);
            }
        }

        MatrixChain.Result res = MatrixChain.compute(p);

        System.out.println("\n--- MATRIX CHAIN RESULTS ---");
        MatrixChain.printCostTable(res.dpCostTable, n);

        System.out.println("\nOptimal Multiplication Cost:   " + res.minCost + " scalar operations");
        System.out.println("Naive Left-to-Right Cost:      " + res.naiveCost + " scalar operations");
        System.out.println("Optimal Parenthesization Plan: " + res.parenthesization);
        if (res.naiveCost > 0) {
            double speedup = ((double) (res.naiveCost - res.minCost) / res.naiveCost) * 100.0;
            System.out.printf("Computational Saving:          %.2f%%%n", speedup);
        }

        System.out.println("\n[VIVA NOTE] MCM uses Interval DP with O(n^3) time complexity to optimize");
        System.out.println("the evaluation order of multi-stage sensor transformation pipelines.");
    }

    // =========================================================================
    // 18: COMPLEXITY COMPARISON TABLE
    // =========================================================================
    private static void displayComplexityComparison() {
        System.out.println("\n=========================================================================================");
        System.out.println("                     LIFEDATA ALGORITHMIC COMPLEXITY MATRIX");
        System.out.println("=========================================================================================");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Algorithm", "Purpose in LifeData", "Time Complexity", "Space Complexity");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Naive Matching", "Baseline search", "O(n * m)", "O(1)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "KMP", "Keyword search in notes", "O(n + m)", "O(m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Z-Algorithm", "Telemetry pattern period", "O(n + m)", "O(n + m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Rabin-Karp", "Record ID rolling hash", "O(n + m) avg", "O(1)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Aho-Corasick", "Multi-keyword stream tag", "O(n + z)", "O(Sigma * m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Suffix Array", "Text indexing & queries", "O(n^2 log n)*", "O(n)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Kasai LCP", "Repeated phrase mining", "O(n)", "O(n)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Levenshtein", "Typo distance in queries", "O(n * m)", "O(n * m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Damerau-Levenshtein", "Transposition typo fix", "O(n * m)", "O(n * m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Weighted Edit Dist", "Keyboard distance cost", "O(n * m)", "O(n * m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Needleman-Wunsch", "Global log alignment", "O(n * m)", "O(n * m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Smith-Waterman", "Local pattern alignment", "O(n * m)", "O(n * m)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Bitmask DP", "Daily activity optimizer", "O(2^N * N)", "O(2^N)");
        System.out.printf("%-18s | %-26s | %-16s | %-16s%n", "Matrix Chain", "Data pipeline optimizer", "O(n^3)", "O(n^2)");
        System.out.println("=========================================================================================");
        System.out.println("* Note: Suffix array uses comparison-based sorting for clean, beginner-friendly viva explanation.");
    }
}

