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
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press disco', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press mancuernas', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Chest press', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Pectoral machine', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Aperturas', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Cruces polea', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Fondos', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Pullover', 'Pecho', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Flexiones', 'Pecho', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Vertical traction', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Jalón polea', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Low row', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Pulley', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Dominadas', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Remo mancuernas', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Remo barra', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Jalón cerrado', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Tirón barra recta', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Jalón palanca', 'Espalda', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Shoulder press', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press mancuernas', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Elevación frontal', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Elevación lateral', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Lateral polea', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press multipower', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Pájaro pie', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Remo vertical', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Upper back', 'Hombro', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press militar', 'Hombro', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Curl barra', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Curl mancuernas', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Barra romana', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Curl Scott', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Martillo', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Cristo', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Bíceps sentado', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Polea baja', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Bíceps tumbado', 'Bíceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Concentrado', 'Bíceps', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Tríceps polea', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Trasnuca polea', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Extensión mancuernas', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press francés', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Fondos', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Patada', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press cerrado', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Jalón 1 mano', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Flexiones muñeca', 'Tríceps', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Disco cuerda', 'Tríceps', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Abdomen en máquina', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Crunch en banco', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Elevaciones de pelvis', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Barra auxiliar', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Crunch banco inclinado', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Oblicuos', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Uves', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Elevaciones de rodillas', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Lumbar inclinado', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Isom. tumbado', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Isom. lateral', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Glúteos patada doblada', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Glúteos patada estiramiento', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Nadador', 'Abdominales', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Paracaídas', 'Abdominales', 1);

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Leg. extensión', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Prensa', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Gemelos moto', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Leg curl', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Zancadas', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Isquio tumbado', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Fitball', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Glúteo máquina', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Media sentadilla', 'Pierna', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Isquio de pie', 'Pierna', 1);

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












