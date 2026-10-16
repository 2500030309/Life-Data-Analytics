import java.util.*;
import java.util.concurrent.*;

/**
 * RandomizedParallel Engine for LifeData.
 * Implements:
 * - Randomized QuickSort with step tracing
 * - Reservoir Sampling for streaming personal data
 * - Miller-Rabin Probabilistic Primality Testing
 * - Blelloch Parallel Prefix Scan (Up-sweep & Down-sweep)
 * - Parallel Reduce via standard ForkJoinPool
 * - Brent's Theorem Work-Span Analytical Calculator
 */
public class RandomizedParallel {

    // =========================================================================
    // 1. RANDOMIZED QUICKSORT
    // =========================================================================
    public static class QuickSortResult {
        public final int[] sortedArray;
        public final long executionTimeNs;
        public final int comparisonCount;
        public final int partitionCount;
        public final List<String> traceLog;

        public QuickSortResult(int[] arr, long time, int comps, int parts, List<String> trace) {
            this.sortedArray = arr;
            this.executionTimeNs = time;
            this.comparisonCount = comps;
            this.partitionCount = parts;
            this.traceLog = trace;
        }
    }

    public static QuickSortResult sortRandomized(int[] input) {
        int[] arr = Arrays.copyOf(input, input.length);
        int[] stats = new int[2]; // [0] = comparisons, [1] = partitions
        List<String> trace = new ArrayList<>();
        Random rng = new Random();

        long start = System.nanoTime();
        quickSortHelper(arr, 0, arr.length - 1, rng, stats, trace);
        long time = System.nanoTime() - start;

        return new QuickSortResult(arr, time, stats[0], stats[1], trace);
    }

    private static void quickSortHelper(int[] arr, int low, int high, Random rng, int[] stats, List<String> trace) {
        if (low < high) {
            // Pick random pivot and swap with high
            int pivotIdx = low + rng.nextInt(high - low + 1);
            int pivotVal = arr[pivotIdx];
            swap(arr, pivotIdx, high);

            int p = partition(arr, low, high, stats);
            stats[1]++;
            if (trace.size() < 12) {
                trace.add(String.format("Pivot [%d] at idx %d -> Subarray [%d..%d] partitioned", pivotVal, p, low, high));
            }

            quickSortHelper(arr, low, p - 1, rng, stats, trace);
            quickSortHelper(arr, p + 1, high, rng, stats, trace);
        }
    }

    private static int partition(int[] arr, int low, int high, int[] stats) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            stats[0]++;
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int t = arr[i]; arr[i] = arr[j]; arr[j] = t;
    }

    // =========================================================================
    // 2. RESERVOIR SAMPLING (Algorithm R)
    // =========================================================================
    public static class ReservoirResult {
        public final List<String> sample;
        public final List<String> stepLogs;
        public final int totalStreamSize;
        public final int sampleK;

        public ReservoirResult(List<String> sample, List<String> logs, int total, int k) {
            this.sample = sample;
            this.stepLogs = logs;
            this.totalStreamSize = total;
            this.sampleK = k;
        }
    }

    public static ReservoirResult runReservoirSampling(List<String> stream, int k) {
        List<String> reservoir = new ArrayList<>(k);
        List<String> logs = new ArrayList<>();
        Random rng = new Random();

        for (int i = 0; i < stream.size(); i++) {
            String item = stream.get(i);
            if (i < k) {
                reservoir.add(item);
                logs.add(String.format("Item %03d: Directly filled reservoir slot %d -> '%s'", i + 1, i, item));
            } else {
                int j = rng.nextInt(i + 1);
                if (j < k) {
                    String replaced = reservoir.get(j);
                    reservoir.set(j, item);
                    if (logs.size() < 30) {
                        logs.add(String.format("Item %03d: Replaced '%s' at slot %d with probability %d/%d", 
                                i + 1, replaced, j, k, i + 1));
                    }
                }
            }
        }
        return new ReservoirResult(reservoir, logs, stream.size(), k);
    }

    // =========================================================================
    // 3. MILLER-RABIN PROBABILISTIC PRIMALITY TEST
    // =========================================================================
    public static class MillerRabinResult {
        public final long candidate;
        public final boolean isProbablyPrime;
        public final int trials;
        public final double confidencePercent;
        public final List<String> stepDetails;

        public MillerRabinResult(long candidate, boolean prime, int trials, double conf, List<String> steps) {
            this.candidate = candidate;
            this.isProbablyPrime = prime;
            this.trials = trials;
            this.confidencePercent = conf;
            this.stepDetails = steps;
        }
    }

    public static MillerRabinResult testPrimality(long n, int k) {
        List<String> steps = new ArrayList<>();
        if (n <= 1) return new MillerRabinResult(n, false, k, 100.0, Collections.singletonList("n <= 1 is composite."));
        if (n <= 3) return new MillerRabinResult(n, true, k, 100.0, Collections.singletonList("2 and 3 are prime."));
        if (n % 2 == 0) return new MillerRabinResult(n, false, k, 100.0, Collections.singletonList("Even number > 2 is composite."));

        // Factor out powers of 2 from n - 1: n - 1 = 2^s * d
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }
        steps.add(String.format("Decomposition: %d - 1 = 2^%d * %d", n, s, d));

        Random rng = new Random();
        for (int i = 1; i <= k; i++) {
            long a = 2 + (Math.abs(rng.nextLong()) % (n - 3));
            long x = modPow(a, d, n);

            if (x == 1 || x == n - 1) {
                steps.add(String.format("Trial %d (base a=%d): a^d mod n = %d -> Inconclusive (pass)", i, a, x));
                continue;
            }

            boolean composite = true;
            for (int r = 1; r < s; r++) {
                x = modMultiply(x, x, n);
                if (x == n - 1) {
                    composite = false;
                    steps.add(String.format("Trial %d: a^(d*2^%d) mod n = -1 -> Pass", i, r));
                    break;
                }
            }

            if (composite) {
                steps.add(String.format("Trial %d (base a=%d): Found witness to compositeness! Guaranteed composite.", i, a));
                return new MillerRabinResult(n, false, i, 100.0, steps);
            }
        }

        double confidence = (1.0 - Math.pow(0.25, k)) * 100.0;
        steps.add(String.format("All %d independent trials passed. Number is PRIME with >= %.4f%% confidence.", k, confidence));
        return new MillerRabinResult(n, true, k, confidence, steps);
    }

    private static long modPow(long base, long exp, long mod) {
        long res = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) res = modMultiply(res, base, mod);
            base = modMultiply(base, base, mod);
            exp >>= 1;
        }
        return res;
    }

    private static long modMultiply(long a, long b, long mod) {
        return (long) (((java.math.BigInteger.valueOf(a))
                .multiply(java.math.BigInteger.valueOf(b)))
                .mod(java.math.BigInteger.valueOf(mod)).longValue());
    }

    // =========================================================================
    // 4. BLELLOCH PARALLEL SCAN (Work-Efficient Prefix Sum)
    // =========================================================================
    public static class BlellochResult {
        public final int[] input;
        public final int[] prefixSum;
        public final List<String> upSweepLog;
        public final List<String> downSweepLog;

        public BlellochResult(int[] in, int[] out, List<String> up, List<String> down) {
            this.input = in;
            this.prefixSum = out;
            this.upSweepLog = up;
            this.downSweepLog = down;
        }
    }

    public static BlellochResult computeBlellochScan(int[] input) {
        // Pad to power of 2
        int n = input.length;
        int pow2 = 1;
        while (pow2 < n) pow2 <<= 1;
        int[] a = new int[pow2];
        System.arraycopy(input, 0, a, 0, n);

        List<String> upLogs = new ArrayList<>();
        List<String> downLogs = new ArrayList<>();

        // Phase 1: Up-Sweep (Parallel Reduction Tree)
        for (int d = 0; d < Integer.numberOfTrailingZeros(pow2); d++) {
            int step = 1 << (d + 1);
            int half = 1 << d;
            for (int k = 0; k < pow2; k += step) {
                a[k + step - 1] += a[k + half - 1];
            }
            upLogs.add(String.format("Level %d (step %d): %s", d + 1, step, Arrays.toString(a)));
        }

        // Set root to 0 for exclusive scan
        a[pow2 - 1] = 0;

        // Phase 2: Down-Sweep (Parallel Distribution Tree)
        for (int d = Integer.numberOfTrailingZeros(pow2) - 1; d >= 0; d--) {
            int step = 1 << (d + 1);
            int half = 1 << d;
            for (int k = 0; k < pow2; k += step) {
                int temp = a[k + half - 1];
                a[k + half - 1] = a[k + step - 1];
                a[k + step - 1] += temp;
            }
            downLogs.add(String.format("Level %d: %s", d, Arrays.toString(a)));
        }

        // Inclusive scan result for original length
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = (i + 1 < pow2 ? a[i + 1] : a[pow2 - 1] + input[n - 1]);
        }
        return new BlellochResult(input, result, upLogs, downLogs);
    }

    // =========================================================================
    // 5. PARALLEL REDUCE VIA FORKJOINPOOL
    // =========================================================================
    private static class SumTask extends RecursiveTask<Long> {
        private final long[] arr;
        private final int start, end;
        private static final int THRESHOLD = 5000;

        public SumTask(long[] arr, int s, int e) {
            this.arr = arr; this.start = s; this.end = e;
        }

        @Override protected Long compute() {
            if (end - start <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) sum += arr[i];
                return sum;
            }
            int mid = (start + end) / 2;
            SumTask left = new SumTask(arr, start, mid);
            SumTask right = new SumTask(arr, mid, end);
            left.fork();
            long rAns = right.compute();
            long lAns = left.join();
            return lAns + rAns;
        }
    }

    public static class ParallelReduceResult {
        public final long sum;
        public final long seqTimeNs;
        public final long parTimeNs;
        public final double speedup;
        public final int processorsUsed;

        public ParallelReduceResult(long sum, long seq, long par, double sp, int proc) {
            this.sum = sum; this.seqTimeNs = seq; this.parTimeNs = par;
            this.speedup = sp; this.processorsUsed = proc;
        }
    }

    public static ParallelReduceResult runParallelReduceBenchmark(int size) {
        long[] arr = new long[size];
        for (int i = 0; i < size; i++) arr[i] = i % 100;

        // Sequential
        long sStart = System.nanoTime();
        long sSum = 0;
        for (int i = 0; i < size; i++) sSum += arr[i];
        long sTime = System.nanoTime() - sStart;

        // Parallel
        ForkJoinPool pool = ForkJoinPool.commonPool();
        long pStart = System.nanoTime();
        long pSum = pool.invoke(new SumTask(arr, 0, size));
        long pTime = System.nanoTime() - pStart;

        double speedup = pTime > 0 ? ((double) sTime / pTime) : 1.0;
        return new ParallelReduceResult(pSum, sTime, pTime, speedup, pool.getParallelism());
    }

    // =========================================================================
    // 6. BRENT'S THEOREM WORK-SPAN CALCULATOR
    // =========================================================================
    public static class BrentAnalysisRow {
        public final int processors;
        public final double theoreticalTime;
        public final double speedup;
        public final double efficiency;

        public BrentAnalysisRow(int p, double t, double s, double e) {
            this.processors = p;
            this.theoreticalTime = t;
            this.speedup = s;
            this.efficiency = e;
        }
    }

    /**
     * Brent's Theorem: Tp <= (T1 - T_inf) / p + T_inf
     */
    public static List<BrentAnalysisRow> computeBrentAnalysis(long workT1, long spanTInf) {
        List<BrentAnalysisRow> rows = new ArrayList<>();
        int[] procList = {1, 2, 4, 8, 16, 32, 64};
        for (int p : procList) {
            double tp = ((double) (workT1 - spanTInf) / p) + spanTInf;
            double speedup = (double) workT1 / tp;
            double efficiency = (speedup / p) * 100.0;
            rows.add(new BrentAnalysisRow(p, tp, speedup, efficiency));
        }
        return rows;
    }
}
