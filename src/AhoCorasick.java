import java.util.*;

/**
 * Aho-Corasick Multi-Pattern Search Automaton.
 * Combines a Trie data structure with BFS failure links and output links
 * to search multiple keywords simultaneously in a single linear pass over the text.
 *
 * Complexity:
 * - Preprocessing Time: O(sum of pattern lengths * alphabet)
 * - Searching Time: O(n + total matches) where n = text length
 * - Space Complexity: O(sum of pattern lengths * alphabet)
 */
public class AhoCorasick {

    public static class Node {
        public Map<Character, Node> children = new HashMap<>();
        public Node fail = null;
        public List<String> outputs = new ArrayList<>();
    }

    private final Node root;
    private final List<String> keywords;

    public AhoCorasick(List<String> keywords) {
        this.root = new Node();
        this.keywords = new ArrayList<>(keywords);
        buildTrie();
        buildFailureLinks();
    }

    private void buildTrie() {
        for (String word : keywords) {
            if (word == null || word.isEmpty()) continue;
            Node current = root;
            for (int i = 0; i < word.length(); i++) {
                char ch = Character.toLowerCase(word.charAt(i));
                current = current.children.computeIfAbsent(ch, c -> new Node());
            }
            current.outputs.add(word);
        }
    }

    private void buildFailureLinks() {
        Queue<Node> queue = new LinkedList<>();

        // Root's immediate children fail to root
        for (Node child : root.children.values()) {
            child.fail = root;
            queue.add(child);
        }

        // BFS traversal for deeper levels
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char ch = entry.getKey();
                Node child = entry.getValue();

                Node fallback = current.fail;
                while (fallback != null && !fallback.children.containsKey(ch)) {
                    fallback = fallback.fail;
                }

                if (fallback == null) {
                    child.fail = root;
                } else {
                    child.fail = fallback.children.get(ch);
                    // Inherit output words from failure link
                    child.outputs.addAll(child.fail.outputs);
                }

                queue.add(child);
            }
        }
    }

    public static class MatchResult {
        public Map<String, List<Integer>> keywordPositions = new LinkedHashMap<>();
        public long transitions = 0;
        public long executionTimeNs = 0;

        public int getTotalMatches() {
            int total = 0;
            for (List<Integer> list : keywordPositions.values()) {
                total += list.size();
            }
            return total;
        }
    }

    /**
     * Searches all keywords simultaneously in text.
     */
    public MatchResult search(String text) {
        MatchResult result = new MatchResult();
        for (String kw : keywords) {
            result.keywordPositions.put(kw, new ArrayList<>());
        }

        if (text == null || text.isEmpty()) {
            return result;
        }

        long startTime = System.nanoTime();
        Node current = root;

        for (int i = 0; i < text.length(); i++) {
            char ch = Character.toLowerCase(text.charAt(i));

            while (current != null && !current.children.containsKey(ch)) {
                result.transitions++;
                current = current.fail;
            }

            if (current == null) {
                current = root;
                continue;
            }

            result.transitions++;
            current = current.children.get(ch);

            for (String matchedWord : current.outputs) {
                int startPos = i - matchedWord.length() + 1;
                result.keywordPositions.get(matchedWord).add(startPos);
            }
        }

        result.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }
}
