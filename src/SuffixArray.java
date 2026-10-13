import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Suffix Array Construction and Substring Search Engine.
 * Constructs the sorted permutation of all suffixes of a text.
 *
 * Complexity:
 * - Construction (Beginner-friendly Sort): O(n^2 log n) comparison-based, or O(n log^2 n) doubling.
 * - Substring Search (Binary Search): O(m * log n) where m = query length, n = text length.
 * - Space Complexity: O(n) for suffix indices and references.
 */
public class SuffixArray {

    public static class SuffixItem {
        public int index;       // Starting index of the suffix in original text
        public String suffix;   // Suffix substring

        public SuffixItem(int index, String suffix) {
            this.index = index;
            this.suffix = suffix;
        }
    }

    private final String text;
    private final SuffixItem[] allSuffixes;
    private final int[] suffixArray;

    public SuffixArray(String text) {
        this.text = text != null ? text : "";
        int n = this.text.length();

        // 1. Generate all suffixes
        this.allSuffixes = new SuffixItem[n];
        for (int i = 0; i < n; i++) {
            this.allSuffixes[i] = new SuffixItem(i, this.text.substring(i));
        }

        // 2. Sort suffixes lexicographically
        SuffixItem[] sortedSuffixes = Arrays.copyOf(allSuffixes, n);
        Arrays.sort(sortedSuffixes, Comparator.comparing(a -> a.suffix));

        // 3. Populate suffix array indices
        this.suffixArray = new int[n];
        for (int i = 0; i < n; i++) {
            this.suffixArray[i] = sortedSuffixes[i].index;
        }
    }

    public String getText() { return text; }
    public SuffixItem[] getAllOriginalSuffixes() { return allSuffixes; }
    public int[] getSuffixArray() { return suffixArray; }

    /**
     * Searches for occurrences of pattern using binary search over the suffix array.
     * Manually compares characters without using String.contains() or indexOf().
     */
    public List<Integer> searchSubstring(String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (pattern == null || pattern.isEmpty() || text.isEmpty()) {
            return matches;
        }

        int n = text.length();
        int m = pattern.length();

        // Binary search for first occurrence (lower bound)
        int low = 0, high = n - 1;
        int firstIndex = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int suffixStart = suffixArray[mid];

            int cmp = compareManual(text, suffixStart, pattern);
            if (cmp >= 0) {
                if (cmp == 0) firstIndex = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (firstIndex == -1) return matches;

        // Binary search for last occurrence (upper bound)
        low = 0;
        high = n - 1;
        int lastIndex = -1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int suffixStart = suffixArray[mid];

            int cmp = compareManual(text, suffixStart, pattern);
            if (cmp <= 0) {
                if (cmp == 0) lastIndex = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (firstIndex != -1 && lastIndex != -1) {
            for (int i = firstIndex; i <= lastIndex; i++) {
                matches.add(suffixArray[i]);
            }
            matches.sort(Integer::compareTo);
        }

        return matches;
    }

    /**
     * Manually compares prefix of text starting at suffixStart with pattern.
     * Returns 0 if pattern matches prefix of suffix.
     */
    private int compareManual(String text, int suffixStart, String pattern) {
        int n = text.length();
        int m = pattern.length();
        int len = Math.min(n - suffixStart, m);

        for (int i = 0; i < len; i++) {
            char c1 = text.charAt(suffixStart + i);
            char c2 = pattern.charAt(i);
            if (c1 != c2) {
                return c1 - c2;
            }
        }

        if (n - suffixStart < m) {
            return -1; // Suffix is shorter than pattern
        }
        return 0; // Pattern matches prefix of suffix
    }
}
