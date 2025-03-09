INSERT INTO weather (id, habitable, temperature)
VALUES (1, true, 25),
       (2, false, -100),
       (3, false, 8000),
       (4, false, 20);
INSERT INTO location (id, name, id_weather)
VALUES (1, 'Mars', 4),
       (2, 'Earth', 1),
       (3, 'Neptune', 2),
       (4, 'Sun', 3);
INSERT INTO engine (id, power, working)
VALUES (1, 5000, true),
       (2, 3000, false),
       (3, 4500, true);
INSERT INTO spaceship (id, name, location_id)
VALUES (1, 'Big Destroyer', 4),
       (2, 'Mars Explorer', 1),
       (3, 'Union', 2);
INSERT INTO role (id, name)
VALUES (1, 'Captain'),
       (2, 'Engineer'),
       (3, 'Scientist');
INSERT INTO human (id, name, birth_date, role_id, spaceship_id)
VALUES (1, 'Pul', '1990-05-15', 1, 2),
       (2, 'Alice Smith', '1985-10-20', 2, 2),
       (3, 'Bob Brown', '2000-01-30', 3, 2);
INSERT INTO battery (id, charge)
VALUES (1, 100.0),
       (2, 0),
       (3, 45.0);
INSERT INTO antenna (id, battery_id, direction_id)
VALUES (1, 2, 2),
       (2, 3, 2),
       (3, 1, 3);
INSERT INTO human_antenna (human_id, antenna_id)
VALUES (1, 2),
       (2, 2),
       (3, 3);