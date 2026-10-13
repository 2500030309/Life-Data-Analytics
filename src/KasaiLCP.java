/**
 * Kasai's Algorithm for Longest Common Prefix (LCP) Array Construction.
 * Computes the LCP array from a given text and its Suffix Array in linear time O(n).
 *
 * Invariant:
 * If suffix i has rank r, and suffix (i-1) had common prefix length h,
 * then suffix i will have common prefix length at least h - 1 with its predecessor in the suffix array.
 * Thus the comparison counter h decrements at most once per suffix, bounding comparisons to O(n).
 *
 * Complexity:
 * - Time Complexity: O(n)
 * - Space Complexity: O(n) for rank and LCP arrays
 */
public class KasaiLCP {

    /**
     * Builds the LCP array in O(n) time.
     * lcp[i] = length of Longest Common Prefix between suffixArray[i] and suffixArray[i-1].
     * lcp[0] = 0.
     */
    public static int[] buildLCP(String text, int[] suffixArray) {
        int n = text.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];

        // rank[i] gives the index position of suffix i in the sorted suffixArray
        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int h = 0; // Length of current longest common prefix

        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = suffixArray[rank[i] - 1]; // Preceding suffix in suffix array

                // Compare characters forward manually
                while (i + h < n && j + h < n && text.charAt(i + h) == text.charAt(j + h)) {
                    h++;
                }

                lcp[rank[i]] = h;

                // Decrement h by at most 1 for next suffix (i + 1)
                if (h > 0) {
                    h--;
                }
            } else {
                lcp[0] = 0;
            }
        }

        return lcp;
    }

    /**
     * Finds the longest repeated phrase in the text using the LCP array.
     */
    public static String getLongestRepeatedSubstring(String text, int[] suffixArray, int[] lcp) {
        int maxLen = 0;
        int maxIndex = -1;

        for (int i = 1; i < lcp.length; i++) {
            if (lcp[i] > maxLen) {
                maxLen = lcp[i];
                maxIndex = suffixArray[i];
            }
        }

        if (maxLen > 0 && maxIndex != -1) {
            return text.substring(maxIndex, maxIndex + maxLen);
        }
        return "None";
    }
}
