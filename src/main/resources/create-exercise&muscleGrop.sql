DROP TABLE IF EXISTS MuscleGroup;
CREATE TABLE MuscleGroup (
  name VARCHAR(50) NOT NULL,
  primary key(name)
);

DROP TABLE IF EXISTS Exercises;
CREATE TABLE Exercise (
  _id INTEGER auto_increment,
  name VARCHAR(50) NOT NULL,
  muscleGroup VARCHAR(50) NOT NULL ,
  weigth INTEGER NOT NULL,
  series INTEGER NOT NULL,
  repetitions INTEGER NOT NULL,
  foreign key(muscleGroup) references MuscleGroup(name),
  primary key(_id)
);
