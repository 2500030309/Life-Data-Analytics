/**
 * Damerau-Levenshtein Distance Algorithm.
 * Extends classic edit distance by adding adjacent character transpositions (swaps).
 * Example: 'form' -> 'from' has distance 1 (1 transposition), whereas standard Levenshtein is 2.
 *
 * Complexity:
 * - Time Complexity: O(n * m)
 * - Space Complexity: O(n * m)
 */
public class DamerauLevenshtein {

    public static class Result {
        public int distance;
        public int[][] dpTable;
        public boolean usedTransposition;
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

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                // Standard operations: Deletion, Insertion, Substitution
                dp[i][j] = Math.min(dp[i - 1][j] + 1,
                           Math.min(dp[i][j - 1] + 1,
                                    dp[i - 1][j - 1] + cost));

                // Adjacent character transposition
                if (i > 1 && j > 1 &&
                    s1.charAt(i - 1) == s2.charAt(j - 2) &&
                    s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    int transCost = dp[i - 2][j - 2] + 1;
                    if (transCost < dp[i][j]) {
                        dp[i][j] = transCost;
                        res.usedTransposition = true;
                    }
                }
            }
        }

        res.distance = dp[m][n];
        res.dpTable = dp;
        res.executionTimeNs = System.nanoTime() - startTime;
        return res;
    }

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
