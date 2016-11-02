package org.trainingTracker.database.valueObject;

public class RecordVO {
	private int exercise;
	private String user_nick;
    private double weight;
    private int series;
	private int repetitions;
	private String record_date;

    /**
     * Constructor that initialises the class with the given fields
     * @param exercise
     * @param user_nick
     * @param weight
     * @param series
     * @param repetitions
     * @param record_date
     */
	public RecordVO(int exercise, String user_nick, double weight, int series, int repetitions, String record_date){
		this.exercise = exercise;
		this.user_nick = user_nick;
        this.weight = weight;
		this.series = series;
		this.repetitions = repetitions;
		this.record_date = record_date;
	}

    public int getExercise() { return exercise;}
    public String getUserNick() { return user_nick;}
    public double getWeight() { return weight;}
    public int getSeries() { return series;}
    public int getRepetitions() { return repetitions;}
    public String getRecordDate() { return record_date;}

    /**
     * Represents the class fields in a human readable format
     * @return
     */
	@Override
	public String toString() {
		return String.format("Exercise: %d Nick: %s Weight: %.2f Series: %d Repetitions: %d RecordDate: %s",
            exercise, user_nick, weight, series, repetitions, record_date);
		
	}

    /**
     * Returns a JSON representation of the object
     * @return
     */
    public String serialize(){
        return String.format("{\"exercise\": \"%s\",  \"nick\": \"%s\",  \"weight\": \"%s\", " +
                "\"series\": \"%s\", \"repetitions\": \"%s\", \"date\": \"%s\" }",
            exercise, user_nick, weight, series, repetitions, record_date);
    }

}
