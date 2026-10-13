import java.util.ArrayList;
import java.util.List;

/**
 * Rabin-Karp String Matching Algorithm using Polynomial Rolling Hash.
 * Computes hash of pattern and sliding window of text in O(1) time per step.
 *
 * Complexity:
 * - Average Time Complexity: O(n + m)
 * - Worst Case Time Complexity: O(n * m) (when many hash collisions occur)
 * - Space Complexity: O(1) auxiliary
 */
public class RabinKarp {

    public static final long BASE = 256;
    public static final long PRIME_MOD = 1000000007L;

    public static class RkResult {
        public long patternHash = 0;
        public long initialWindowHash = 0;
        public List<Integer> positions = new ArrayList<>();
        public long comparisons = 0;
        public long spuriousHits = 0;
        public long executionTimeNs = 0;
    }

    public static RkResult search(String text, String pattern) {
        RkResult result = new RkResult();
        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return result;
        }

        int n = text.length();
        int m = pattern.length();
        long startTime = System.nanoTime();

        // Precompute BASE^(m-1) % PRIME_MOD
        long h = 1;
        for (int i = 0; i < m - 1; i++) {
            h = (h * BASE) % PRIME_MOD;
        }

        long pHash = 0;
        long tHash = 0;

        // Compute hash of pattern and first window
        for (int i = 0; i < m; i++) {
            pHash = (BASE * pHash + pattern.charAt(i)) % PRIME_MOD;
            tHash = (BASE * tHash + text.charAt(i)) % PRIME_MOD;
        }

        result.patternHash = pHash;
        result.initialWindowHash = tHash;

        for (int i = 0; i <= n - m; i++) {
            if (pHash == tHash) {
                // Hashes match: manually verify characters to handle collisions
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    result.comparisons++;
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        result.spuriousHits++;
                        break;
                    }
                }
                if (match) {
                    result.positions.add(i);
                }
            }

            // Rolling hash update for next window
            if (i < n - m) {
                tHash = (BASE * (tHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME_MOD;
                if (tHash < 0) {
                    tHash += PRIME_MOD;
                }
            }
        }

        result.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }
}
