CREATE TABLE weather (
    id          SERIAL PRIMARY KEY,
    habitable    BOOLEAN NOT NULL,
    temperature INTEGER NOT NULL
);
CREATE TABLE location (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    id_weather INTEGER NOT NULL REFERENCES weather ON DELETE CASCADE
);
CREATE TABLE spaceship (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    location_id INTEGER NOT NULL REFERENCES location ON DELETE CASCADE
);
CREATE TABLE engine (
    id      SERIAL PRIMARY KEY,
    power   INTEGER NOT NULL CHECK ( power > 0 ),
    working BOOLEAN NOT NULL,
    spaceship_id INTEGER REFERENCES spaceship ON DELETE CASCADE
);
CREATE TABLE role (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
CREATE TABLE human (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    birth_date   DATE        NOT NULL CHECK ( birth_date < CURRENT_DATE ),
    role_id      INTEGER REFERENCES role ON DELETE CASCADE,
    spaceship_id INTEGER REFERENCES spaceship ON DELETE CASCADE
);
CREATE TABLE battery (
    id     SERIAL PRIMARY KEY,
    charge REAL NOT NULL CHECK ( charge >= 0 AND charge <= 100)
);
CREATE TABLE antenna (
    id           SERIAL PRIMARY KEY,
    battery_id   INTEGER NOT NULL REFERENCES battery ON DELETE CASCADE,
    direction_id INTEGER REFERENCES location ON DELETE CASCADE,
    spaceship_id INTEGER NOT NULL REFERENCES spaceship ON DELETE CASCADE
);
CREATE TABLE human_antenna (
    id         SERIAL PRIMARY KEY,
    human_id   INTEGER NOT NULL REFERENCES human ON DELETE CASCADE,
    antenna_id INTEGER NOT NULL REFERENCES antenna ON DELETE CASCADE
);