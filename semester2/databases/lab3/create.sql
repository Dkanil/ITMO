CREATE TABLE weather (
    id          SERIAL PRIMARY KEY,
    habitable    BOOLEAN NOT NULL,
    temperature INTEGER NOT NULL
);
CREATE TABLE location (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    id_weather INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (id_weather) REFERENCES weather ON DELETE CASCADE
);
CREATE TABLE spaceship (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    location_id INTEGER NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location
);
CREATE TABLE engine (
    id      SERIAL PRIMARY KEY,
    power   INTEGER NOT NULL CHECK ( power > 0 ),
    working BOOLEAN NOT NULL,
    spaceship_id INTEGER,
    FOREIGN KEY (spaceship_id) REFERENCES spaceship
);
CREATE TABLE role (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
CREATE TABLE human (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(50) NOT NULL,
    birth_date   DATE        NOT NULL CHECK ( birth_date < CURRENT_DATE ),
    role_id      INTEGER,
    spaceship_id INTEGER,
    FOREIGN KEY (role_id) REFERENCES role,
    FOREIGN KEY (spaceship_id) REFERENCES spaceship
);
CREATE TABLE battery (
    id     SERIAL PRIMARY KEY,
    charge REAL NOT NULL CHECK ( charge >= 0 AND charge <= 100)
);
CREATE TABLE antenna (
    id           SERIAL PRIMARY KEY,
    battery_id   INTEGER NOT NULL,
    direction_id INTEGER,
    spaceship_id INTEGER NOT NULL,
    FOREIGN KEY (battery_id) REFERENCES battery ON DELETE CASCADE,
    FOREIGN KEY (direction_id) REFERENCES location ON DELETE CASCADE,
    FOREIGN KEY (spaceship_id) REFERENCES spaceship ON DELETE CASCADE
);
CREATE TABLE human_antenna (
    id         SERIAL PRIMARY KEY,
    human_id   INTEGER NOT NULL,
    antenna_id INTEGER NOT NULL,
    FOREIGN KEY (human_id) REFERENCES human ON DELETE CASCADE,
    FOREIGN KEY (antenna_id) REFERENCES antenna ON DELETE CASCADE
);