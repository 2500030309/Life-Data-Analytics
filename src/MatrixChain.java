/**
 * Matrix Chain Multiplication (MCM) using Interval Dynamic Programming.
 * Determines the most efficient way to parenthesize a sequence of matrix multiplications
 * or analytical data-processing transformation stages.
 *
 * Complexity:
 * - Time Complexity: O(n^3) where n = number of matrices / stages
 * - Space Complexity: O(n^2) for memoization and split tables
 */
public class MatrixChain {

    public static class Result {
        public long minCost;
        public long[][] dpCostTable;
        public int[][] splitTable;
        public String parenthesization;
        public long naiveCost;
    }

    /**
     * Computes the optimal parenthesization and cost using Interval DP.
     * @param p Array of dimensions: Matrix i has dimension p[i-1] x p[i]
     */
    public static Result compute(int[] p) {
        Result res = new Result();
        int n = p.length - 1; // Number of matrices

        long[][] m = new long[n + 1][n + 1];
        int[][] s = new int[n + 1][n + 1];

        // Cost is 0 when multiplying one matrix
        for (int i = 1; i <= n; i++) {
            m[i][i] = 0;
        }

        // L is chain length (from 2 to n)
        for (int L = 2; L <= n; L++) {
            for (int i = 1; i <= n - L + 1; i++) {
                int j = i + L - 1;
                m[i][j] = Long.MAX_VALUE;

                for (int k = i; k <= j - 1; k++) {
                    long cost = m[i][k] + m[k + 1][j] + (long) p[i - 1] * p[k] * p[j];
                    if (cost < m[i][j]) {
                        m[i][j] = cost;
                        s[i][j] = k; // Record optimal split point
                    }
                }
            }
        }

        // Calculate naive left-to-right evaluation cost for comparison
        long naive = 0;
        int currentRows = p[0];
        for (int i = 1; i < n; i++) {
            naive += (long) currentRows * p[i] * p[i + 1];
        }

        res.minCost = m[1][n];
        res.dpCostTable = m;
        res.splitTable = s;
        res.naiveCost = naive;
        res.parenthesization = buildParentheses(s, 1, n);

        return res;
    }

    private static String buildParentheses(int[][] s, int i, int j) {
        if (i == j) {
            return "A" + i;
        } else {
            return "(" + buildParentheses(s, i, s[i][j]) + " x " + buildParentheses(s, s[i][j] + 1, j) + ")";
        }
    }

    public static void printCostTable(long[][] m, int n) {
        System.out.println("DP Cost Table m[i][j]:");
        System.out.print("      ");
        for (int j = 1; j <= n; j++) {
            System.out.printf("   j=%d   ", j);
        }
        System.out.println();

        for (int i = 1; i <= n; i++) {
            System.out.printf("i=%d | ", i);
            for (int j = 1; j <= n; j++) {
                if (j < i) {
                    System.out.print("    -    ");
                } else {
                    System.out.printf("%8d ", m[i][j]);
                }
            }
            System.out.println();
        }
    }
}
