SET SESSION sql_mode = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DROP DATABASE IF EXISTS trainingTracker;
CREATE DATABASE trainingTracker;
USE trainingTracker;

DROP TABLE IF EXISTS Users;
CREATE TABLE Users (
  nick VARCHAR(50) PRIMARY KEY,
  pass VARCHAR(50) NOT NULL ,
  email VARCHAR(50) NOT NULL UNIQUE,
  registerDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
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
  foreign key(muscle_group) references MuscleGroup(name) ON DELETE CASCADE,
  primary key(_id)
);

DROP TABLE IF EXISTS Own;
CREATE TABLE Own (
    nick VARCHAR(50),
    exercise INTEGER,
    foreign key(nick) references Users(nick) ON DELETE CASCADE,
    foreign key(exercise) references Exercises(_id) ON DELETE CASCADE,
    primary key(nick, exercise)
);
DROP TABLE IF EXISTS Records;
CREATE TABLE Records (
    exercise INTEGER NOT NULL,
    user_nick VARCHAR(50),
    weigth DOUBLE NOT NULL,
    series INTEGER NOT NULL,
    repetitions INTEGER NOT NULL,
    record_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key(exercise) references Exercises(_id) ON DELETE CASCADE,
    foreign key(user_nick) references Users(nick) ON DELETE CASCADE,
    primary key(exercise, user_nick, record_date)
);
USE trainingTracker;
INSERT INTO MuscleGroup (name) VALUES ('Pecho');
INSERT INTO MuscleGroup (name) VALUES ('Espalda');
INSERT INTO MuscleGroup (name) VALUES ('Brazo');
INSERT INTO MuscleGroup (name) VALUES ('Pierna');

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Dominadas', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press Banca', 'Pecho', 1);
