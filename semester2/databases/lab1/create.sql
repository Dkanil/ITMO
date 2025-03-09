CREATE TABLE weather (
     id          SERIAL PRIMARY KEY,
     habitable    BOOLEAN NOT NULL,
     temperature INTEGER NOT NULL
);
CREATE TABLE location (
      id         SERIAL PRIMARY KEY,
      name       VARCHAR(50) NOT NULL,
      id_weather INTEGER REFERENCES weather (id)
);
CREATE TABLE engine (
    id      SERIAL PRIMARY KEY,
    power   INTEGER NOT NULL,
    working BOOLEAN NOT NULL
);
CREATE TABLE spaceship (
       id          SERIAL PRIMARY KEY,
       name        VARCHAR(50) NOT NULL,
       location_id INTEGER REFERENCES location (id)
);
CREATE TABLE role
(
id   SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL
);
CREATE TABLE human
(
id           SERIAL PRIMARY KEY,
name         VARCHAR(50) NOT NULL,
birth_date   DATE        NOT NULL,
role_id      INTEGER REFERENCES role (id),
spaceship_id INTEGER REFERENCES spaceship (id)
);
CREATE TABLE battery
(
id     SERIAL PRIMARY KEY,
charge REAL NOT NULL
);
CREATE TABLE antenna
(
id           SERIAL PRIMARY KEY,
battery_id   INTEGER REFERENCES battery (id),
direction_id INTEGER REFERENCES location (id)
);
CREATE TABLE human_antenna
(
id         SERIAL PRIMARY KEY,
human_id   INTEGER REFERENCES human (id),
antenna_id INTEGER REFERENCES antenna (id)
);