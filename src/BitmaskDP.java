import java.util.ArrayList;
import java.util.List;

/**
 * Bitmask Dynamic Programming for Activity and Preference Selection.
 * Finds the combination of activities that maximizes user utility/value
 * without exceeding total available time or cost budget.
 *
 * Each subset of N activities is represented by a binary bitmask from 0 to 2^N - 1.
 * For example, with 3 activities:
 * Mask 5 (binary 101) represents selecting Activity 0 and Activity 2.
 *
 * Complexity:
 * - Time Complexity: O(2^N * N)
 * - Space Complexity: O(2^N) for the DP memoization table
 */
public class BitmaskDP {

    public static class Activity {
        public String name;
        public int timeMinutes;
        public int utilityValue;

        public Activity(String name, int timeMinutes, int utilityValue) {
            this.name = name;
            this.timeMinutes = timeMinutes;
            this.utilityValue = utilityValue;
        }

        @Override
        public String toString() {
            return String.format("%-16s | Time: %3d mins | Value: %3d pts", name, timeMinutes, utilityValue);
        }
    }

    public static class OptimizationResult {
        public int optimalMask;
        public int maxUtility;
        public int totalTimeSpent;
        public List<Activity> selectedActivities = new ArrayList<>();
        public long executionTimeNs = 0;
    }

    /**
     * Solves the constrained activity selection problem using Bitmask DP.
     */
    public static OptimizationResult optimize(List<Activity> activities, int maxTimeBudget) {
        long startTime = System.nanoTime();
        int n = activities.size();
        int totalStates = 1 << n; // 2^N subsets

        // dp[mask] = maximum utility achievable for the subset 'mask'
        int[] dp = new int[totalStates];
        // totalTime[mask] = sum of time needed for subset 'mask'
        int[] totalTime = new int[totalStates];

        // Base state (empty set has 0 time and 0 value)
        dp[0] = 0;
        totalTime[0] = 0;

        int bestMask = 0;
        int maxUtility = 0;

        // Iterate through all 2^N subsets in increasing order of binary representation
        for (int mask = 1; mask < totalStates; mask++) {
            // Find an activity 'i' present in this mask (e.g. least significant set bit)
            int lsb = Integer.numberOfTrailingZeros(mask);
            int prevMask = mask ^ (1 << lsb);

            Activity act = activities.get(lsb);
            totalTime[mask] = totalTime[prevMask] + act.timeMinutes;

            // Check if within budget
            if (totalTime[mask] <= maxTimeBudget) {
                // Calculate utility using DP transition from previous submask
                int currentUtility = 0;
                for (int i = 0; i < n; i++) {
                    if ((mask & (1 << i)) != 0) {
                        currentUtility += activities.get(i).utilityValue;
                    }
                }
                dp[mask] = currentUtility;

                if (currentUtility > maxUtility) {
                    maxUtility = currentUtility;
                    bestMask = mask;
                }
            } else {
                dp[mask] = -1; // Exceeds budget
            }
        }

        OptimizationResult result = new OptimizationResult();
        result.optimalMask = bestMask;
        result.maxUtility = maxUtility;
        result.totalTimeSpent = totalTime[bestMask];

        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) {
                result.selectedActivities.add(activities.get(i));
            }
        }

        result.executionTimeNs = System.nanoTime() - startTime;
        return result;
    }
}
