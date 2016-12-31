package org.trainingTracker.database.valueObject;

public class CardioExerciseVO {
	private int id;
	private String name;
    private String type;
	private boolean predefined;

    /**
     * Constructor that initialises the class with the given fields
     * @param id
     * @param name
     * @param type
     * @param predefined
     */
	public CardioExerciseVO(int id, String name, String type, boolean predefined){
        this.id=id;
        this.name=name;
        this.type=type;
        this.predefined=predefined;
	}

    public int getId() { return id;}
    public String getName() { return name;}
    public String getType() { return type;}
    public boolean isPredefined() { return predefined;}

    /**
     * Represents the class fields in a human readable format
     * @return
     */
	@Override
	public String toString() {
		return String.format("_id: %s Name: %s Muscle Group: %s Predetermined: %s;", id, name, type, predefined);
		
	}

    /**
     * Returns a JSON representation of the object
     * @return
     */
    public String serialize(){
        return String.format("{\"id\": \"%s\",  \"name\": \"%s\",  \"muscleGroup\": \"%s\", \"predetermined\": %s }",
            id, name, type, predefined);
    }
}
