import java.util.ArrayList;
import java.util.List;

/**
 * Knuth-Morris-Pratt (KMP) Algorithm.
 * Avoids backtracking the text pointer by precomputing the LPS (Longest Proper Prefix which is also Suffix) array.
 *
 * Complexity:
 * - Preprocessing Time: O(m)
 * - Searching Time: O(n)
 * - Total Time Complexity: O(n + m)
 * - Space Complexity: O(m) for the LPS array
 */
public class KMP {

    public static class KmpResult {
        public int[] lps;
        public List<Integer> positions = new ArrayList<>();
        public long comparisons = 0;
        public long executionTimeNs = 0;
    }

    /**
     * Builds the LPS (Longest Proper Prefix that is also Suffix) array for the pattern.
     */
    public static int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * Searches pattern in text using KMP.
     */
    public static KmpResult search(String text, String pattern) {
        KmpResult result = new KmpResult();
        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            result.lps = new int[0];
            return result;
        }

        int n = text.length();
        int m = pattern.length();

        long startTime = System.nanoTime();
        result.lps = computeLPS(pattern);

        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            result.comparisons++;
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                result.positions.add(i - j);
                j = result.lps[j - 1]; // Reset j using LPS to find next match
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = result.lps[j - 1]; // Do not increment i, slide pattern
                } else {
                    i++;
                }
            }
        }

        result.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }
}
