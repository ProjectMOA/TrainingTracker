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
    comment TEXT,
    record_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key(exercise) references Exercises(_id) ON DELETE CASCADE,
    foreign key(user_nick) references Users(nick) ON DELETE CASCADE,
    primary key(exercise, user_nick, record_date)
);

DROP TABLE IF EXISTS CardioType;
CREATE TABLE CardioType (
    name VARCHAR(50) NOT NULL,
    primary key(name)
);

DROP TABLE IF EXISTS CardioExercises;
CREATE TABLE CardioExercises (
    _id INTEGER auto_increment,
    name VARCHAR(50),
    type VARCHAR(50) NOT NULL,
    predefined BOOL NOT NULL DEFAULT FALSE,
    foreign key(type) references CardioType(name) ON DELETE CASCADE,
    primary key(_id)
);

DROP TABLE IF EXISTS CardioOwn;
CREATE TABLE CardioOwn (
    nick VARCHAR(50),
    exercise INTEGER,
    foreign key(nick) references Users(nick) ON DELETE CASCADE,
    foreign key(exercise) references CardioExercises(_id) ON DELETE CASCADE,
    primary key(nick, exercise)
);

DROP TABLE IF EXISTS CardioRecords;
CREATE TABLE CardioRecords (
    exercise INTEGER NOT NULL,
    user_nick VARCHAR(50),
    distance DOUBLE NOT NULL,
    time TIME NOT NULL,
    intensity INTEGER NOT NULL,
    record_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key(exercise) references CardioExercises(_id) ON DELETE CASCADE,
    foreign key(user_nick) references Users(nick) ON DELETE CASCADE,
    primary key(exercise, user_nick, record_date)
);

USE trainingTracker;
INSERT INTO MuscleGroup (name) VALUES ('Pecho');
INSERT INTO MuscleGroup (name) VALUES ('Espalda');
INSERT INTO MuscleGroup (name) VALUES ('Hombro');
INSERT INTO MuscleGroup (name) VALUES ('Bíceps');
INSERT INTO MuscleGroup (name) VALUES ('Tríceps');
INSERT INTO MuscleGroup (name) VALUES ('Abdominales');
INSERT INTO MuscleGroup (name) VALUES ('Pierna');

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press banca', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Flexión', 'Pecho', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Dominada con peso libre', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Remo', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Dominada con polea', 'Espalda', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Levantamiento vertical de mancuerna', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Levantamiento vertical de barra', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Levantamiento en arco de mancuerna', 'Hombro', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Curl barra', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Curl mancuerna interior con apoyo', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Curl mancuerna frontal con apoyo', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Curl mancuerna frontal sin apoyo', 'Bíceps', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Levantamiento vertical de mancuerna', 'Tríceps', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Abdomen en suelo individual', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Abdomen en suelo con agarre', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Extensión con rodillo', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Levantamiento de piernas con lastre', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Levantamiento de piernas en espaldera', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Contracción alterna de codo y rodilla', 'Abdominales', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Zancada con carga', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Zancada sin carga', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Patada contra saco', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Sentadilla con pesas rusas', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Sentadilla con barra', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Sentadilla en máquina', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Salto', 'Pierna', 1);

INSERT INTO CardioType (name) VALUES ('Carrera');
INSERT INTO CardioType (name) VALUES ('Natación');
INSERT INTO CardioType (name) VALUES ('MTB');
INSERT INTO CardioType (name) VALUES ('Bici de carretera');
INSERT INTO CardioType (name) VALUES ('Kayak');

INSERT INTO CardioExercises (name, type, predefined) VALUES ('Carrera Contínua', 'Carrera', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('HIIT', 'Carrera', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Series', 'Carrera', 1);

INSERT INTO CardioExercises (name, type, predefined) VALUES ('Series mariposa', 'Natación', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Series braza', 'Natación', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Series espalda', 'Natación', 1);

INSERT INTO CardioExercises (name, type, predefined) VALUES ('Travesía', 'MTB', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Series', 'MTB', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Descenso', 'MTB', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Velocidad', 'MTB', 1);

INSERT INTO CardioExercises (name, type, predefined) VALUES ('Series', 'Bici de carretera', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Fondo', 'Bici de carretera', 1);
INSERT INTO CardioExercises (name, type, predefined) VALUES ('Velocidad', 'Bici de carretera', 1);

INSERT INTO CardioExercises (name, type, predefined) VALUES ('Descenso de barranco', 'Kayak', 1);
