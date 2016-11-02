DROP TABLE IF EXISTS MuscleGroup;
CREATE TABLE MuscleGroup (
  name VARCHAR(50) NOT NULL,
  primary key(name)
);

DROP TABLE IF EXISTS Exercises;
CREATE TABLE Exercises (
  _id INTEGER auto_increment,
  name VARCHAR(50),
  muscle_group VARCHAR(50) NOT NULL,
  predefined BOOL NOT NULL DEFAULT FALSE,
  foreign key(muscle_group) references MuscleGroup(name),
  primary key(_id)
);

DROP TABLE IF EXISTS Own;
CREATE TABLE Own (
    nick VARCHAR(50),
    exercise INTEGER,
    foreign key(nick) references Users(nick),
    foreign key(exercise) references Exercises(_id),
    primary key(nick, exercise)
);
