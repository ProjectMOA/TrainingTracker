USE trainingTracker;
INSERT INTO MuscleGroup (name) VALUES ('Pecho');
INSERT INTO MuscleGroup (name) VALUES ('Espalda');
INSERT INTO MuscleGroup (name) VALUES ('Brazo');
INSERT INTO MuscleGroup (name) VALUES ('Pierna');

INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Dominadas', 'Espalda', 1);
INSERT INTO Exercises (name, muscle_group, predefined) VALUES ('Press Banca', 'Pecho', 1);
