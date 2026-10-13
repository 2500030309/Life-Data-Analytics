/**
 * PersonalRecord represents a heterogeneous personal record in LifeData.
 * Contains activity details, health notes, sensor readings, and user preferences.
 */
public class PersonalRecord {
    private String recordId;
    private String date;
    private String activity;
    private String category;
    private String note;
    private String sensorValue;
    private String preference;

    public PersonalRecord(String recordId, String date, String activity, 
                          String category, String note, String sensorValue, String preference) {
        this.recordId = recordId;
        this.date = date;
        this.activity = activity;
        this.category = category;
        this.note = note;
        this.sensorValue = sensorValue;
        this.preference = preference;
    }

    public String getRecordId() { return recordId; }
    public String getDate() { return date; }
    public String getActivity() { return activity; }
    public String getCategory() { return category; }
    public String getNote() { return note; }
    public String getSensorValue() { return sensorValue; }
    public String getPreference() { return preference; }

    public String getSearchableContent() {
        return "ID: " + recordId + " | Date: " + date + " | Activity: " + activity +
               " | Category: " + category + " | Note: " + note + 
               " | Sensor: " + sensorValue + " | Preference: " + preference;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %-12s | %-14s | Note: %-35s | Sensor: %-10s | Pref: %s",
                recordId, date, activity, category, note, sensorValue, preference);
    }
}
