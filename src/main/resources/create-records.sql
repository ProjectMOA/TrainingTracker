USE trainingTracker;
DROP TABLE IF EXISTS Records;
CREATE TABLE Records (
    exercise INTEGER NOT NULL,
    user_nick VARCHAR(50),
    weigth DOUBLE NOT NULL,
    series INTEGER NOT NULL,
    repetitions INTEGER NOT NULL,
    record_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    foreign key(exercise) references Exercises(_id),
    foreign key(user_nick) references Users(nick),
    primary key(exercise, user_nick, record_date)
);
