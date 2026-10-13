import java.util.ArrayList;
import java.util.List;

/**
 * Z-Algorithm for Linear Time Pattern Matching.
 * Constructs concatenated string S = pattern + "$" + text and computes the Z-array.
 *
 * Complexity:
 * - Time Complexity: O(n + m)
 * - Space Complexity: O(n + m) for the Z-array
 */
public class ZAlgorithm {

    public static class ZResult {
        public String concatenated;
        public int[] zArray;
        public List<Integer> positions = new ArrayList<>();
        public long executionTimeNs = 0;
    }

    /**
     * Computes the Z-array for string s.
     * Z[i] is the length of the longest substring starting from s[i] that is also a prefix of s.
     */
    public static int[] computeZArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0, r = 0; // [l, r] defines the current Z-box

        for (int i = 1; i < n; i++) {
            if (i > r) {
                l = r = i;
                while (r < n && s.charAt(r - l) == s.charAt(r)) {
                    r++;
                }
                z[i] = r - l;
                r--;
            } else {
                int k = i - l;
                if (z[k] < r - i + 1) {
                    z[i] = z[k];
                } else {
                    l = i;
                    while (r < n && s.charAt(r - l) == s.charAt(r)) {
                        r++;
                    }
                    z[i] = r - l;
                    r--;
                }
            }
        }
        return z;
    }

    /**
     * Searches pattern in text using the Z-algorithm with delimiter '$'.
     */
    public static ZResult search(String text, String pattern) {
        ZResult result = new ZResult();
        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            result.zArray = new int[0];
            return result;
        }

        long startTime = System.nanoTime();
        String concat = pattern + "$" + text;
        result.concatenated = concat;
        result.zArray = computeZArray(concat);

        int m = pattern.length();
        for (int i = m + 1; i < concat.length(); i++) {
            if (result.zArray[i] == m) {
                result.positions.add(i - (m + 1));
            }
        }

        result.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }
}
