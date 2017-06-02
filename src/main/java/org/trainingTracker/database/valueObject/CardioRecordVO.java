package org.trainingTracker.database.valueObject;

public class CardioRecordVO {
	private int exercise;
	private String user_nick;
    private double distance;
    private String time;
    private int intensity;
	private String record_date;

    /**
     * Constructor that initialises the class with the given fields
     * @param exercise
     * @param user_nick
     * @param distance
     * @param time
     * @param intensity
     * @param record_date
     */
	public CardioRecordVO(int exercise, String user_nick, double distance, String time, int intensity, String record_date){
		this.exercise = exercise;
		this.user_nick = user_nick;
        this.distance = distance;
        this.time = time;
        this.intensity = intensity;
		this.record_date = record_date;
	}

    public int getExercise() { return exercise;}
    public String getUserNick() { return user_nick;}
    public double getDistance() { return distance;}
    public String getTime() { return time;}
    public int getIntensity() { return intensity;}
    public String getRecordDate() { return record_date;}

    /**
     * Represents the class fields in a human readable format
     * @return
     */
	@Override
	public String toString() {
		return String.format("Exercise: %d Nick: %s Distance: %.1f Time: %s Intensity: %d RecordDate: %s",
            exercise, user_nick, distance, time.toString(), intensity, record_date);
		
	}

    /**
     * Returns a JSON representation of the object
     * @return
     */
    public String serialize(){
        return String.format("{\"exercise\": \"%s\",  \"nick\": \"%s\",  \"distance\": \"%.1f\", " +
                "\"time\": \"%s\",  \"intensity\": \"%s\", \"date\": \"%s\" }",
            exercise, user_nick, distance, time.toString(), intensity, record_date);
    }

}
