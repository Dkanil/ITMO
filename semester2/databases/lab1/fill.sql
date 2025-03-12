INSERT INTO weather (habitable, temperature)
VALUES (true, 25),
       (false, -100),
       (false, 8000),
       (false, 20);
INSERT INTO location (name, id_weather)
VALUES ('Mars', 4),
       ('Earth', 1),
       ('Neptune', 2),
       ('Sun', 3);
INSERT INTO spaceship (name, location_id)
VALUES ('Big Destroyer', 4),
       ('Mars Explorer', 1),
       ('Union', 2);
INSERT INTO engine (power, working, spaceship_id)
VALUES (5000, true, 2),
       (3000, false, 2),
       (4500, true, 1);
INSERT INTO role (name)
VALUES ('Captain'),
       ('Engineer'),
       ('Scientist');
INSERT INTO human (name, birth_date, role_id, spaceship_id)
VALUES ('Pul', '1990-05-15', 1, 2),
       ('Alice Smith', '1985-10-20', 2, 2),
       ('Bob Brown', '2000-01-30', 3, 2);
INSERT INTO battery (charge)
VALUES (100.0),
       (0),
       (45.0);
INSERT INTO antenna (battery_id, direction_id, spaceship_id)
VALUES (2, 2, 1),
       (3, 2, 1),
       (1, 3, 2);
INSERT INTO human_antenna (human_id, antenna_id)
VALUES (1, 2),
       (2, 2),
       (3, 3);