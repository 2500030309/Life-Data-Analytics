import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Levenshtein / Wagner-Fischer Algorithm.
 * Measures the minimum number of single-character edits (insertions, deletions, substitutions)
 * needed to transform string 1 into string 2.
 *
 * Complexity:
 * - Time Complexity: O(n * m)
 * - Space Complexity: O(n * m)
 */
public class Levenshtein {

    public static class Result {
        public int distance;
        public int[][] dpTable;
        public List<String> editSteps = new ArrayList<>();
        public long executionTimeNs = 0;
    }

    public static Result compute(String s1, String s2) {
        long startTime = System.nanoTime();
        Result res = new Result();
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        // Base cases
        for (int i = 0; i <= m; i++) dp[i][0] = i; // Deleting all chars from s1
        for (int j = 0; j <= n; j++) dp[0][j] = j; // Inserting all chars into s1

        // Fill DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // Match
                } else {
                    int deleteCost = dp[i - 1][j] + 1;
                    int insertCost = dp[i][j - 1] + 1;
                    int replaceCost = dp[i - 1][j - 1] + 1;
                    dp[i][j] = Math.min(deleteCost, Math.min(insertCost, replaceCost));
                }
            }
        }

        res.distance = dp[m][n];
        res.dpTable = dp;

        // Backtrack to find step-by-step transformation
        int i = m, j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && s1.charAt(i - 1) == s2.charAt(j - 1)) {
                res.editSteps.add("KEEP '" + s1.charAt(i - 1) + "'");
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                res.editSteps.add("REPLACE '" + s1.charAt(i - 1) + "' with '" + s2.charAt(j - 1) + "'");
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                res.editSteps.add("DELETE '" + s1.charAt(i - 1) + "'");
                i--;
            } else {
                res.editSteps.add("INSERT '" + s2.charAt(j - 1) + "'");
                j--;
            }
        }
        Collections.reverse(res.editSteps);

        res.executionTimeNs = System.nanoTime() - startTime;
        return res;
    }

    /**
     * Helper to print formatted DP table to console.
     */
    public static void printTable(String s1, String s2, int[][] dp) {
        System.out.print("      -  ");
        for (int j = 0; j < s2.length(); j++) {
            System.out.printf("%2c ", s2.charAt(j));
        }
        System.out.println();

        for (int i = 0; i <= s1.length(); i++) {
            if (i == 0) System.out.print("  - ");
            else System.out.printf("  %c ", s1.charAt(i - 1));

            for (int j = 0; j <= s2.length(); j++) {
                System.out.printf("%2d ", dp[i][j]);
            }
            System.out.println();
        }
    }
}
