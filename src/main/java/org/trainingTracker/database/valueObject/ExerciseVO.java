package org.trainingTracker.database.valueObject;

public class ExerciseVO {
	private int id;
	private String name;
    private String muscleGroup;
	private boolean predefined;

    /**
     * Constructor that initialises the class with the given fields
     * @param id
     * @param name
     * @param muscleGroup
     * @param predefined
     */
	public ExerciseVO(int id,String name, String muscleGroup, boolean predefined){
        this.id=id;
        this.name=name;
        this.muscleGroup=muscleGroup;
        this.predefined=predefined;
	}

    public int getId() { return id;}
    public String getName() { return name;}
    public String getMuscleGroup() { return muscleGroup;}
    public boolean isPredefined() { return predefined;}

    /**
     * Represents the class fields in a human readable format
     * @return
     */
	@Override
	public String toString() {
		return String.format("_id: %s Name: %s Muscle Group: %s Predefined: %s;", id, name, muscleGroup, predefined);
		
	}

    /**
     * Returns a JSON representation of the object
     * @return
     */
    public String serialize(){
        return String.format("{\"id\": \"%s\",  \"name\": \"%s\",  \"muscleGroup\": \"%s\", \"predefines\": \"%s\" }",
            id, name, muscleGroup, predefined);
    }
}
