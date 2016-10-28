DROP TABLE IF EXISTS MuscleGroup;
CREATE TABLE MuscleGroup (
  name VARCHAR(50) NOT NULL,
  primary key(name)
);

DROP TABLE IF EXISTS Exercises;
CREATE TABLE Exercises (
  name VARCHAR(50),
  muscle_group VARCHAR(50) NOT NULL,
  foreign key(muscle_group) references MuscleGroup(name),
  primary key(name)
);
