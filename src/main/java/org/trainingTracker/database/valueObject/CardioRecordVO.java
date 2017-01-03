package org.trainingTracker.database.valueObject;

public class CardioRecordVO {
	private int exercise;
	private String user_nick;
    private String time;
    private int intensity;
    private String comment;
	private String record_date;

    /**
     * Constructor that initialises the class with the given fields
     * @param exercise
     * @param user_nick
     * @param time
     * @param intensity
     * @param comment
     * @param record_date
     */
	public CardioRecordVO(int exercise, String user_nick, String time, int intensity, String comment, String record_date){
		this.exercise = exercise;
		this.user_nick = user_nick;
        this.time = time;
        this.intensity = intensity;
        this.comment = comment;
		this.record_date = record_date;
	}

    public int getExercise() { return exercise;}
    public String getUserNick() { return user_nick;}
    public String getTime() { return time;}
    public int getIntensity() { return intensity;}
    public String getComment() { return comment;}
    public String getRecordDate() { return record_date;}

    /**
     * Represents the class fields in a human readable format
     * @return
     */
	@Override
	public String toString() {
		return String.format("Exercise: %d Nick: %s Time: %s Intensity: %d Comment: %s RecordDate: %s",
            exercise, user_nick, time, intensity, comment, record_date);
		
	}

    /**
     * Returns a JSON representation of the object
     * @return
     */
    public String serialize(){
        return String.format("{\"exercise\": \"%s\",  \"nick\": \"%s\",  \"time\": \"%s\", " +
                "\"intensity\": \"%s\", \"commentary\": \"%s\", \"date\": \"%s\" }",
            exercise, user_nick, time, intensity, comment, record_date);
    }

}
