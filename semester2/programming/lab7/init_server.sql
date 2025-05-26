CREATE TABLE IF NOT EXISTS users
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    permissions VARCHAR(50) NOT NULL CHECK (permissions IN ('USER', 'ADMIN', 'MODERATOR', 'ABOBA')) DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS coordinates
(
    id SERIAL PRIMARY KEY,
    x  DOUBLE PRECISION CHECK (x > -980),
    y  INTEGER CHECK (y <= 295) NOT NULL
);
CREATE TABLE IF NOT EXISTS studio
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    address VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS music_genre
(
    id         SERIAL PRIMARY KEY,
    genre_name VARCHAR(50) NOT NULL UNIQUE
);
INSERT INTO music_genre (genre_name)
VALUES ('JAZZ'),
       ('MATH_ROCK'),
       ('BRIT_POP');
CREATE TABLE IF NOT EXISTS music_bands
(
    id                     SERIAL PRIMARY KEY,
    name                   VARCHAR(255)                        NOT NULL CHECK (name <> ''),
    coordinates_id         INT                                 NOT NULL,
    FOREIGN KEY (coordinates_id) REFERENCES coordinates (id) ON DELETE CASCADE,
    creation_date          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    number_of_participants INT CHECK (number_of_participants > 0),
    albums_count           BIGINT CHECK (albums_count > 0),
    description            TEXT                                NOT NULL,
    genre_id               INT                                 NOT NULL,
    FOREIGN KEY (genre_id) REFERENCES music_genre (id),
    studio_id              INT                                 NOT NULL,
    FOREIGN KEY (studio_id) REFERENCES studio (id) ON DELETE CASCADE,
    user_id                INT                                 NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE OR REPLACE FUNCTION delete_related_records()
    RETURNS TRIGGER AS $$
BEGIN
    DELETE FROM coordinates WHERE id = OLD.coordinates_id;
    DELETE FROM studio WHERE id = OLD.studio_id;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER after_music_bands_delete
    AFTER DELETE ON music_bands
    FOR EACH ROW
EXECUTE FUNCTION delete_related_records();

SELECT music_bands.id                     AS id,
       music_bands.name                   AS band_name,
       coordinates.x                      AS coordinates_x,
       coordinates.y                      AS coordinates_y,
       music_bands.creation_date          AS creation_date,
       music_bands.number_of_participants AS number_of_participants,
       music_bands.albums_count           AS albums_count,
       music_bands.description            AS description,
       music_genre.genre_name             AS genre_name,
       studio.name                        AS studio_name,
       studio.address                     AS studio_address,
       users.username                     AS username
FROM music_bands
         JOIN coordinates ON music_bands.coordinates_id = coordinates.id
         JOIN studio ON music_bands.studio_id = studio.id
         JOIN music_genre ON music_bands.genre_id = music_genre.id
         JOIN users ON music_bands.user_id = users.id