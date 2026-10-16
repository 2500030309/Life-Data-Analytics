import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DatasetService manages the in-memory personal dataset,
 * synthetic data generation (100, 1,000, 10,000 records), and CSV persistence.
 */
public class DatasetService {

    private final List<PersonalRecord> records = new ArrayList<>();
    private static final String[] CATEGORIES = {"Fitness", "Cardio", "Mental Health", "Nutrition", "Sleep & Recovery", "Medical"};
    private static final String[] ACTIVITIES = {
        "Morning Jogging", "Guided Meditation", "Brisk Walking", "Gym Weightlifting",
        "Deep REM Sleep", "Keto Lunch Tracking", "Swimming Laps", "Cycling Session",
        "Yoga & Flexibility", "HIIT Cardio", "Hydration Routine", "Physiotherapy"
    };
    private static final String[] SENSORS = {
        "142 bpm", "68 bpm", "85 bpm", "420 kcal", "7.5 hrs", "1850 kcal",
        "130 bpm", "510 kcal", "72 bpm", "165 bpm", "2.8 L", "Pain: 2/10"
    };
    private static final String[] PREFERENCES = {"Outdoor", "Indoor", "Gym", "Home", "Park", "Clinical"};
    private static final String[] TAGS_POOL = {
        "heart-rate", "endurance", "stress-relief", "hydration", "protein", "recovery",
        "vital-signs", "blood-pressure", "step-count", "sleep-hygiene", "vo2-max", "mindfulness"
    };
    private static final String[] NOTE_TEMPLATES = {
        "Completed %s for 45 minutes along the trail. Sensor recorded %s with stable respiration.",
        "Evening reflection after %s. Heart rate settled at %s, muscle soreness minimal.",
        "Clinical check: Post-%s reading shows %s. Hydration and electrolytes prioritized.",
        "Recovery protocol: High-quality %s observed with average reading of %s. Vitals nominal.",
        "Session log: Patient finished %s indoors. Telemetry verified at %s with good endurance."
    };

    public DatasetService() {
        loadDefaultDataset();
    }

    public List<PersonalRecord> getRecords() {
        return records;
    }

    public int getRecordCount() {
        return records.size();
    }

    public void clear() {
        records.clear();
    }

    public void addRecord(PersonalRecord r) {
        records.add(r);
    }

    public boolean deleteRecord(int index) {
        if (index >= 0 && index < records.size()) {
            records.remove(index);
            return true;
        }
        return false;
    }

    public void loadDefaultDataset() {
        records.clear();
        records.add(new PersonalRecord("REC-101", "2026-10-01", "Morning Jogging", "Fitness",
                "Morning outdoor jogging for 4 km in cool weather. Heart rate stable during exercise.",
                "148 bpm", "Outdoor", "fitness, cardio, jogging", "device=garmin-forerunner;status=valid"));
        records.add(new PersonalRecord("REC-102", "2026-10-01", "Guided Meditation", "Mental Health",
                "Guided breathing session and stress relief recovery. Low anxiety detected.",
                "64 bpm", "Indoor", "mental-health, mindfulness, calm", "device=headspace-band;status=valid"));
        records.add(new PersonalRecord("REC-103", "2026-10-02", "Brisk Walking", "Fitness",
                "Patient completed 5000 steps and checked arterial health today.",
                "82 bpm", "Outdoor", "walking, steps, health", "device=apple-watch;status=valid"));
        records.add(new PersonalRecord("REC-104", "2026-10-02", "Gym Weightlifting", "Fitness",
                "Core strength training, deadlifts and free weights session.",
                "410 kcal", "Gym", "strength, weights, calorie-burn", "device=whoop-strap;status=valid"));
        records.add(new PersonalRecord("REC-105", "2026-10-03", "Deep REM Sleep", "Sleep & Recovery",
                "Uninterrupted sleep cycle lasting 8 hours. Restorative phase peaked at 2 AM.",
                "58 bpm", "Home", "sleep, recovery, rem", "device=oura-ring;status=valid"));
        records.add(new PersonalRecord("REC-106", "2026-10-03", "Cycling Session", "Cardio",
                "Outdoor cycling along the river trail covering 15 kilometers with uphill sprints.",
                "152 bpm", "Outdoor", "cycling, cadence, cardio", "device=wahoo-bolt;status=valid"));
        records.add(new PersonalRecord("REC-107", "2026-10-04", "Keto Lunch Tracking", "Nutrition",
                "Balanced nutritional intake: high protein, low carbohydrate, adequate electrolyte hydration.",
                "1900 kcal", "Home", "nutrition, diet, keto", "device=myfitnesspal;status=verified"));
        records.add(new PersonalRecord("REC-108", "2026-10-04", "Yoga & Flexibility", "Mental Health",
                "Sun salutations, spine mobility, and deep diaphragm stretching exercises.",
                "76 bpm", "Indoor", "yoga, mobility, flexibility", "device=apple-watch;status=valid"));
    }

    /**
     * Generate synthetic dataset with realistic randomized combinations.
     * @param count Target count (e.g. 100, 1000, 10000)
     */
    public void generateSyntheticData(int count) {
        records.clear();
        Random rng = new Random(42); // deterministic seed for reproducibility
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        for (int i = 1; i <= count; i++) {
            cal.add(Calendar.MINUTE, -(rng.nextInt(120) + 15));
            String date = sdf.format(cal.getTime());
            String id = String.format("REC-%05d", i);

            int catIdx = rng.nextInt(CATEGORIES.length);
            String category = CATEGORIES[catIdx];

            int actIdx = rng.nextInt(ACTIVITIES.length);
            String activity = ACTIVITIES[actIdx];

            String sensor = SENSORS[rng.nextInt(SENSORS.length)];
            String pref = PREFERENCES[rng.nextInt(PREFERENCES.length)];

            String tag1 = TAGS_POOL[rng.nextInt(TAGS_POOL.length)];
            String tag2 = TAGS_POOL[rng.nextInt(TAGS_POOL.length)];
            String tags = tag1.equals(tag2) ? tag1 : tag1 + ", " + tag2;

            String noteTmpl = NOTE_TEMPLATES[rng.nextInt(NOTE_TEMPLATES.length)];
            String note = String.format(noteTmpl, activity.toLowerCase(), sensor);

            String metadata = "device=synth-sensor-" + (rng.nextInt(5) + 1) + ";confidence=" + (85 + rng.nextInt(15)) + "%";

            records.add(new PersonalRecord(id, date, activity, category, note, sensor, pref, tags, metadata));
        }
    }

    public Map<String, Integer> getCategoryDistribution() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (String c : CATEGORIES) map.put(c, 0);
        for (PersonalRecord r : records) {
            map.put(r.getCategory(), map.getOrDefault(r.getCategory(), 0) + 1);
        }
        return map;
    }

    public Map<String, Integer> getActivityDistribution() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (PersonalRecord r : records) {
            map.put(r.getActivity(), map.getOrDefault(r.getActivity(), 0) + 1);
        }
        return map;
    }

    public String getAllNotesAggregated() {
        StringBuilder sb = new StringBuilder();
        for (PersonalRecord r : records) {
            sb.append(r.getNote()).append(" ");
        }
        return sb.toString().trim();
    }

    public void exportToCsv(File file) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("RecordID,Date,Activity,Category,SensorValue,Preference,Tags,Metadata,PersonalNote");
            for (PersonalRecord r : records) {
                pw.println(r.toCsvLine());
            }
        }
    }

    public int importFromCsv(File file) throws IOException {
        List<PersonalRecord> imported = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                List<String> tokens = parseCsvLine(line);
                if (tokens.size() >= 7) {
                    String id = tokens.get(0);
                    String date = tokens.get(1);
                    String act = tokens.get(2);
                    String cat = tokens.get(3);
                    String sensor = tokens.get(4);
                    String pref = tokens.get(5);
                    String tags = tokens.size() > 6 ? tokens.get(6) : "imported";
                    String meta = tokens.size() > 7 ? tokens.get(7) : "source=csv";
                    String note = tokens.size() > 8 ? tokens.get(8) : (tokens.size() > 6 ? tokens.get(6) : "");
                    imported.add(new PersonalRecord(id, date, act, cat, note, sensor, pref, tags, meta));
                }
            }
        }
        if (!imported.isEmpty()) {
            records.clear();
            records.addAll(imported);
        }
        return imported.size();
    }

    private static List<String> parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString().trim());
        return tokens;
    }
}
