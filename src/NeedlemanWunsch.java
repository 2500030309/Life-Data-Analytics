/**
 * Needleman-Wunsch Algorithm for Global Sequence Alignment.
 * Finds the optimal end-to-end alignment between two activity logs, sensor streams, or textual notes.
 *
 * Scoring Scheme (Standard):
 * - Match = +1
 * - Mismatch = -1
 * - Gap Penalty = -1
 *
 * Complexity:
 * - Time Complexity: O(n * m)
 * - Space Complexity: O(n * m)
 */
public class NeedlemanWunsch {

    public static class AlignmentResult {
        public int score;
        public int[][] dpTable;
        public String alignedSeq1;
        public String alignedSeq2;
        public long executionTimeNs = 0;
    }

    public static AlignmentResult align(String s1, String s2, int matchScore, int mismatchScore, int gapPenalty) {
        long startTime = System.nanoTime();
        AlignmentResult res = new AlignmentResult();
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        // Base cases: align with gaps
        for (int i = 0; i <= m; i++) dp[i][0] = i * gapPenalty;
        for (int j = 0; j <= n; j++) dp[0][j] = j * gapPenalty;

        // Fill DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int scoreDiag = dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : mismatchScore);
                int scoreUp = dp[i - 1][j] + gapPenalty;
                int scoreLeft = dp[i][j - 1] + gapPenalty;
                dp[i][j] = Math.max(scoreDiag, Math.max(scoreUp, scoreLeft));
            }
        }

        res.score = dp[m][n];
        res.dpTable = dp;

        // Backtrack to find global alignment
        StringBuilder aligned1 = new StringBuilder();
        StringBuilder aligned2 = new StringBuilder();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            int scoreDiag = (i > 0 && j > 0) ? (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : mismatchScore) : Integer.MIN_VALUE;

            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + scoreDiag) {
                aligned1.append(s1.charAt(i - 1));
                aligned2.append(s2.charAt(j - 1));
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + gapPenalty) {
                aligned1.append(s1.charAt(i - 1));
                aligned2.append('-');
                i--;
            } else {
                aligned1.append('-');
                aligned2.append(s2.charAt(j - 1));
                j--;
            }
        }

        res.alignedSeq1 = aligned1.reverse().toString();
        res.alignedSeq2 = aligned2.reverse().toString();
        res.executionTimeNs = System.nanoTime() - startTime;
        return res;
    }

    public static void printTable(String s1, String s2, int[][] dp) {
        System.out.print("        -    ");
        for (int j = 0; j < s2.length(); j++) {
            System.out.printf("%4c ", s2.charAt(j));
        }
        System.out.println();

        for (int i = 0; i <= s1.length(); i++) {
            if (i == 0) System.out.print("  -  ");
            else System.out.printf("  %c  ", s1.charAt(i - 1));

            for (int j = 0; j <= s2.length(); j++) {
                System.out.printf("%4d ", dp[i][j]);
            }
            System.out.println();
        }
    }
}
