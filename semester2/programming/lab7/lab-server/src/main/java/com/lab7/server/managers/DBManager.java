package com.lab7.server.managers;

import com.lab7.common.models.Coordinates;
import com.lab7.common.models.MusicBand;
import com.lab7.common.models.MusicGenre;
import com.lab7.common.models.Studio;
import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.server.Server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Stack;

/**
 * Класс, управляющий сохранением и загрузкой коллекции музыкальных групп.
 */
public class DBManager {
    private static volatile DBManager instance;
    private static Connection connection;

    /**
     * Конструктор для создания объекта DBManager.
     */
    private DBManager(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            Server.logger.info("Connected to database successfully");
        }
        catch (SQLException e) {
            Server.logger.severe("Failed to connect to database: " + e.getMessage());
        }
    }

    public static DBManager getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager("jdbc:postgresql://pg:5432/studs", "s466217", ""); // пароль из файла .pgpass
                }
            }
        }
        return instance;
    }

    public ExecutionStatus addUser(Pair<String, String> user) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?);";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getFirst());
            p.setString(2, user.getSecond());
            p.executeUpdate();
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при добавлении пользователя в базу данных: " + e.getMessage());
        }
        return new ExecutionStatus(true, "User registered successfully!");
    }

    private ExecutionStatus checkUser(Pair<String, String> user) {
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE username = ?);";
        try (PreparedStatement p = connection.prepareStatement(query)){
            p.setString(1, user.getFirst());
            ResultSet res = p.executeQuery();
            if (res.next() && res.getBoolean(1)) {
                return new ExecutionStatus(true, "Пользователь успешно найден!");
            }
            else {
                return new ExecutionStatus(false, "Пользователь не найден!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при проверке пользователя в базе данных: " + e.getMessage());
        }
    }

    public ExecutionStatus checkPassword(Pair<String, String> user) {
        ExecutionStatus checkUserStatus = checkUser(user);
        if (checkUserStatus.isSuccess()) {
            String query = "SELECT password FROM users WHERE username = ?;";
            try (PreparedStatement p = connection.prepareStatement(query)){
                p.setString(1, user.getFirst());
                ResultSet res = p.executeQuery();
                if (res.next()) {
                    String password = res.getString("password");
                    boolean match = password.equals(user.getSecond());
                    if (match) {
                        return new ExecutionStatus(true, "Login successful!");
                    }
                    else {
                        return new ExecutionStatus(false, "Введён неверный пароль!");
                    }
                } else {
                    return new ExecutionStatus(false, "Пользователь не найден!");
                }
            } catch (SQLException | NullPointerException e) {
                return new ExecutionStatus(false, "Ошибка при проверке пользователя в базе данных: " + e.getMessage());
            }
        } else {
            return new ExecutionStatus(false, checkUserStatus.getMessage());
        }
    }

    public ExecutionStatus clear(Pair<String, String> user) {
        String query = "DELETE FROM music_bands WHERE user_id IN (SELECT id FROM users WHERE username = ?);";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getFirst());
            int affectedRows = p.executeUpdate();
            if (affectedRows > 0) {
                return new ExecutionStatus(true, "Успешно удалено " + affectedRows + " элементов пользователя " + user.getFirst() + "!");
            }
            else {
                return new ExecutionStatus(true, "У пользователя " + user.getFirst() + " нет элементов в коллекции!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при очистке коллекции в базе данных: " + e.getMessage());
        }
    }

    public ExecutionStatus removeById(Long id, Pair<String, String> user) {
        String query = "DELETE FROM music_bands WHERE id = ? AND user_id IN (SELECT id FROM users WHERE username = ?);";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setLong(1, id);
            p.setString(2, user.getFirst());
            int affectedRows = p.executeUpdate();
            if (affectedRows > 0) {
                return new ExecutionStatus(true, "Элемент успешно удалён!");
            }
            else {
                return new ExecutionStatus(false, "Элемент не может быть удалён, так как вы не являетесь его владельцем!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при удалении элемента коллекции из базы данных: " + e.getMessage());
        }
    }

    public ExecutionStatus removeAllByGenre(MusicGenre genre, Pair<String, String> user) {
        String query = "DELETE FROM music_bands WHERE genre_id = ? AND user_id IN (SELECT id FROM users WHERE username = ?);";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setLong(1, genre.ordinal() + 1);
            p.setString(2, user.getFirst());
            int affectedRows = p.executeUpdate();
            if (affectedRows > 0) {
                return new ExecutionStatus(true, "Успешно удалено " + affectedRows + " элементов пользователя " + user.getFirst() + " с жанром " + genre + "!");
            }
            else {
                return new ExecutionStatus(false, "У пользователя " + user.getFirst() + " элементы с указанным genre не найдены!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при удалении элемента коллекции из базы данных: " + e.getMessage());
        }
    }

    public ExecutionStatus addMusicBand(MusicBand band, Pair<String, String> user) {
        try {
            // Запись координат
            int coordinatesId = -1;
            String insertCoordinates = "INSERT INTO coordinates (x, y) VALUES (?, ?) RETURNING id";
            try (PreparedStatement coordinatesStmt = connection.prepareStatement(insertCoordinates)) {
                coordinatesStmt.setDouble(1, band.getCoordinates().getX());
                coordinatesStmt.setInt(2, band.getCoordinates().getY());
                ResultSet rs = coordinatesStmt.executeQuery();
                if (rs.next()) {
                    coordinatesId = rs.getInt("id");
                }
            }

            // Запись студии
            int studioId = -1;
            String insertStudio = "INSERT INTO studio (name, address) VALUES (?, ?) RETURNING id";
            try (PreparedStatement studioStmt = connection.prepareStatement(insertStudio)) {
                studioStmt.setString(1, band.getStudio().getName());
                studioStmt.setString(2, band.getStudio().getAddress());
                ResultSet rs = studioStmt.executeQuery();
                if (rs.next()) {
                    studioId = rs.getInt("id");
                }
            }

            // Запись самой музыкальной группы
            String insertBand = "INSERT INTO music_bands (name, coordinates_id, creation_date, number_of_participants, albums_count, description, genre_id, studio_id, user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, (SELECT id FROM users WHERE username = ?)) RETURNING id";
            try (PreparedStatement bandStmt = connection.prepareStatement(insertBand)) {
                bandStmt.setString(1, band.getName());
                bandStmt.setInt(2, coordinatesId);
                bandStmt.setTimestamp(3, Timestamp.valueOf(band.getCreationDate()));
                bandStmt.setLong(4, band.getNumberOfParticipants());
                bandStmt.setLong(5, band.getAlbumsCount());
                bandStmt.setString(6, band.getDescription());
                bandStmt.setInt(7, band.getGenre().ordinal() + 1);
                bandStmt.setInt(8, studioId);
                bandStmt.setString(9, user.getFirst());
                ResultSet rs = bandStmt.executeQuery();
                if (rs.next()) {
                    return new ExecutionStatus(true, rs.getString("id"));
                }
                else {
                    return new ExecutionStatus(false, "Не удалось сохранить элемент в базу данных!");
                }
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при сохранении элемента коллекции в базу данных: " + e.getMessage());
        }
    }

    public ExecutionStatus updateMusicBand(MusicBand band, Pair<String, String> user) {
        try {
            int coordinatesId;
            int studioId;

            String checkQuery = "SELECT COUNT(*) FROM music_bands WHERE id = ?;";
            // Обновление самой музыкальной группы
            String updateBand = "UPDATE music_bands SET name = ?, number_of_participants = ?, albums_count = ?, " +
                    "description = ?, genre_id = ? WHERE id = ? AND user_id IN (SELECT id FROM users WHERE username = ?) RETURNING coordinates_id, studio_id";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
                 PreparedStatement bandStmt = connection.prepareStatement(updateBand)) {
                checkStmt.setLong(1, band.getId());
                ResultSet checkResult = checkStmt.executeQuery();
                if (checkResult.next() && checkResult.getInt(1) == 0) {
                    return new ExecutionStatus(false, "Элемент с указанным id не найден!");
                }

                bandStmt.setString(1, band.getName());
                bandStmt.setLong(2, band.getNumberOfParticipants());
                bandStmt.setLong(3, band.getAlbumsCount());
                bandStmt.setString(4, band.getDescription());
                bandStmt.setInt(5, band.getGenre().ordinal() + 1);
                bandStmt.setLong(6, band.getId());
                bandStmt.setString(7, user.getFirst());
                ResultSet rs = bandStmt.executeQuery();
                if (rs.next()) {
                    coordinatesId = rs.getInt("coordinates_id");
                    studioId = rs.getInt("studio_id");
                }
                else {
                    return new ExecutionStatus(false, "Пользователь не является владельцем элемента коллекции!");
                }
            }

            // Обновление координат
            String updateCoordinates = "UPDATE coordinates SET x = ?, y = ? WHERE id = ?";
            try (PreparedStatement coordinatesStmt = connection.prepareStatement(updateCoordinates)) {
                coordinatesStmt.setDouble(1, band.getCoordinates().getX());
                coordinatesStmt.setInt(2, band.getCoordinates().getY());
                coordinatesStmt.setInt(3, coordinatesId);
                coordinatesStmt.executeUpdate();
            }

            // Обновление студии
            String insertStudio = "UPDATE studio SET name = ?, address = ? WHERE id = ?";
            try (PreparedStatement studioStmt = connection.prepareStatement(insertStudio)) {
                studioStmt.setString(1, band.getStudio().getName());
                studioStmt.setString(2, band.getStudio().getAddress());
                studioStmt.setInt(3, studioId);
                studioStmt.executeUpdate();
            }

            return new ExecutionStatus(true, "Элемент успешно обновлён!");
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при сохранении элемента коллекции в базу данных: " + e.getMessage());
        }
    }

    /**
     * Загружает коллекцию музыкальных групп из базы данных.
     * @param collection коллекция музыкальных групп
     */
    public ExecutionStatus loadCollection(Stack<MusicBand> collection) {
        String query = "SELECT music_bands.id       AS id, " +
                "music_bands.name                   AS band_name, " +
                "coordinates.x                      AS coordinates_x, " +
                "coordinates.y                      AS coordinates_y, " +
                "music_bands.creation_date          AS creation_date, " +
                "music_bands.number_of_participants AS number_of_participants, " +
                "music_bands.albums_count           AS albums_count, " +
                "music_bands.description            AS description, " +
                "music_genre.genre_name             AS genre_name, " +
                "studio.name                        AS studio_name, " +
                "studio.address                     AS studio_address, " +
                "users.username                     AS username " +
                "FROM music_bands " +
                "JOIN coordinates ON music_bands.coordinates_id = coordinates.id " +
                "JOIN studio ON music_bands.studio_id = studio.id " +
                "JOIN music_genre ON music_bands.genre_id = music_genre.id " +
                "JOIN users ON music_bands.user_id = users.id;";
        try (PreparedStatement p = connection.prepareStatement(query); ResultSet res = p.executeQuery()) {
            while (res.next()) {
                MusicBand band = new MusicBand(
                        res.getLong("id"),
                        res.getString("band_name"),
                        new Coordinates(res.getDouble("coordinates_x"), res.getInt("coordinates_y")),
                        res.getTimestamp("creation_date").toLocalDateTime(),
                        res.getLong("number_of_participants"),
                        res.getLong("albums_count"),
                        res.getString("description"),
                        MusicGenre.valueOf(res.getString("genre_name")),
                        new Studio(res.getString("studio_name"), res.getString("studio_address")),
                        res.getString("username")
                );
                collection.push(band);
            }
        } catch (IllegalArgumentException e) {
            return new ExecutionStatus(false, "Введены некорректные данные элемента коллекции!");
        } catch (SQLException e) {
            return new ExecutionStatus(false, "Произошла ошибка при чтении коллекции из базы данных!");
        }
        return new ExecutionStatus(true, "Коллекция успешно загружена!");
    }
}
