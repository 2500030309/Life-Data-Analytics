import java.util.ArrayList;
import java.util.List;

/**
 * Naive String Matching Algorithm.
 * Slides the pattern character-by-character over the text.
 *
 * Complexity:
 * - Time Complexity: O(n * m) worst case, where n = text length, m = pattern length.
 * - Space Complexity: O(1) auxiliary.
 */
public class NaiveSearch {

    public static class SearchResult {
        public List<Integer> positions = new ArrayList<>();
        public long comparisons = 0;
        public long executionTimeNs = 0;
    }

    public static SearchResult search(String text, String pattern) {
        SearchResult result = new SearchResult();
        if (text == null || pattern == null || pattern.isEmpty() || pattern.length() > text.length()) {
            return result;
        }

        int n = text.length();
        int m = pattern.length();
        long startTime = System.nanoTime();

        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m) {
                result.comparisons++;
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
                j++;
            }
            if (j == m) {
                result.positions.add(i);
            }
        }

        result.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }
}
