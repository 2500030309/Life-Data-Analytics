import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PersonalRecord represents a heterogeneous personal record in LifeData.
 * Contains activity details, health notes, sensor readings, tags, and user preferences.
 */
public class PersonalRecord {
    private String recordId;
    private String date;
    private String activity;
    private String category;
    private String note;
    private String sensorValue;
    private String preference;
    private String tags;
    private String metadata;

    public PersonalRecord(String recordId, String date, String activity, 
                          String category, String note, String sensorValue, String preference) {
        this(recordId, date, activity, category, note, sensorValue, preference, 
             category.toLowerCase() + ", " + activity.toLowerCase(), 
             "source=wearable;device=vitals-hub;status=verified");
    }

    public PersonalRecord(String recordId, String date, String activity, 
                          String category, String note, String sensorValue, 
                          String preference, String tags, String metadata) {
        this.recordId = recordId;
        this.date = date;
        this.activity = activity;
        this.category = category;
        this.note = note;
        this.sensorValue = sensorValue;
        this.preference = preference;
        this.tags = (tags == null || tags.trim().isEmpty()) ? "personal, sensor" : tags;
        this.metadata = (metadata == null || metadata.trim().isEmpty()) ? "device=sensor-stream" : metadata;
    }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getSensorValue() { return sensorValue; }
    public void setSensorValue(String sensorValue) { this.sensorValue = sensorValue; }

    public String getPreference() { return preference; }
    public void setPreference(String preference) { this.preference = preference; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public String getSearchableContent() {
        return "ID: " + recordId + " | Date: " + date + " | Activity: " + activity +
               " | Category: " + category + " | Note: " + note + 
               " | Sensor: " + sensorValue + " | Preference: " + preference +
               " | Tags: " + tags + " | Metadata: " + metadata;
    }

    public String toCsvLine() {
        return escapeCsv(recordId) + "," +
               escapeCsv(date) + "," +
               escapeCsv(activity) + "," +
               escapeCsv(category) + "," +
               escapeCsv(sensorValue) + "," +
               escapeCsv(preference) + "," +
               escapeCsv(tags) + "," +
               escapeCsv(metadata) + "," +
               escapeCsv(note);
    }

    private static String escapeCsv(String val) {
        if (val == null) return "\"\"";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %-12s | %-14s | Note: %-35s | Sensor: %-10s | Pref: %s | Tags: %s",
                recordId, date, activity, category, note, sensorValue, preference, tags);
    }
}
