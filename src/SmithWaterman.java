/**
 * Smith-Waterman Algorithm for Local Sequence Alignment.
 * Identifies the most similar local regions/subsequences between two activity streams,
 * sensor observations, or personal notes.
 *
 * Scoring Scheme:
 * - Match = +2
 * - Mismatch = -1
 * - Gap Penalty = -1
 * - Cell values clamped at 0 (negative values are reset to 0)
 *
 * Complexity:
 * - Time Complexity: O(n * m)
 * - Space Complexity: O(n * m)
 */
public class SmithWaterman {

    public static class AlignmentResult {
        public int maxScore;
        public int maxI;
        public int maxJ;
        public int[][] dpTable;
        public String alignedSeq1;
        public String alignedSeq2;
    }

    public static AlignmentResult align(String s1, String s2, int matchScore, int mismatchScore, int gapPenalty) {
        AlignmentResult res = new AlignmentResult();
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        int maxScore = 0;
        int maxI = 0, maxJ = 0;

        // Fill DP table (all borders are 0)
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int scoreDiag = dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : mismatchScore);
                int scoreUp = dp[i - 1][j] + gapPenalty;
                int scoreLeft = dp[i][j - 1] + gapPenalty;

                // Clamped at 0 for local alignment
                dp[i][j] = Math.max(0, Math.max(scoreDiag, Math.max(scoreUp, scoreLeft)));

                if (dp[i][j] > maxScore) {
                    maxScore = dp[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        res.maxScore = maxScore;
        res.maxI = maxI;
        res.maxJ = maxJ;
        res.dpTable = dp;

        // Backtrack from (maxI, maxJ) until a cell with value 0 is hit
        StringBuilder aligned1 = new StringBuilder();
        StringBuilder aligned2 = new StringBuilder();
        int i = maxI, j = maxJ;

        while (i > 0 && j > 0 && dp[i][j] > 0) {
            int scoreDiag = s1.charAt(i - 1) == s2.charAt(j - 1) ? matchScore : mismatchScore;

            if (dp[i][j] == dp[i - 1][j - 1] + scoreDiag) {
                aligned1.append(s1.charAt(i - 1));
                aligned2.append(s2.charAt(j - 1));
                i--;
                j--;
            } else if (dp[i][j] == dp[i - 1][j] + gapPenalty) {
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
