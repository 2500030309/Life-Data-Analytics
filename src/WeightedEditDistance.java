/**
 * Weighted Edit Distance Algorithm.
 * Allows custom, non-uniform penalties for insertion, deletion, and replacement operations.
 * Useful in personal data analytics where user-entry errors on keyboards or speech-to-text
 * have asymmetric probabilities.
 *
 * Complexity:
 * - Time Complexity: O(n * m)
 * - Space Complexity: O(n * m)
 */
public class WeightedEditDistance {

    public static class Result {
        public double totalCost;
        public double[][] dpTable;
    }

    public static Result compute(String s1, String s2, double insertCost, double deleteCost, double replaceCost) {
        Result res = new Result();
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int m = s1.length();
        int n = s2.length();
        double[][] dp = new double[m + 1][n + 1];

        // Base cases
        for (int i = 0; i <= m; i++) dp[i][0] = i * deleteCost;
        for (int j = 0; j <= n; j++) dp[0][j] = j * insertCost;

        // DP state transitions
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1]; // Match (cost 0)
                } else {
                    double del = dp[i - 1][j] + deleteCost;
                    double ins = dp[i][j - 1] + insertCost;
                    double rep = dp[i - 1][j - 1] + replaceCost;
                    dp[i][j] = Math.min(del, Math.min(ins, rep));
                }
            }
        }

        res.totalCost = dp[m][n];
        res.dpTable = dp;
        return res;
    }

    public static void printTable(String s1, String s2, double[][] dp) {
        System.out.print("        -    ");
        for (int j = 0; j < s2.length(); j++) {
            System.out.printf("%5c ", s2.charAt(j));
        }
        System.out.println();

        for (int i = 0; i <= s1.length(); i++) {
            if (i == 0) System.out.print("  -  ");
            else System.out.printf("  %c  ", s1.charAt(i - 1));

            for (int j = 0; j <= s2.length(); j++) {
                System.out.printf("%5.1f ", dp[i][j]);
            }
            System.out.println();
        }
    }
}
