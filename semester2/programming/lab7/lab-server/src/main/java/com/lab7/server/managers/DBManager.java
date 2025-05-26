package com.lab7.server.managers;

import com.lab7.common.models.*;
import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.server.Server;
import com.lab7.common.utility.PermissionType;
import com.lab7.server.utility.Transactional;
import com.lab7.server.utility.TransactionalProxy;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Stack;

/**
 * Класс, управляющий сохранением и загрузкой коллекции музыкальных групп.
 */
public class DBManager implements DBManagerInterface {
    private static volatile DBManagerInterface instance;
    private static Connection connection;

    /**
     * Конструктор для создания объекта DBManager.
     */
    private DBManager() {
        try (FileInputStream input = new FileInputStream("dbconfig.properties")) {
            Properties properties = new Properties();
            properties.load(input);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user"); // sXXXXXX
            String password = properties.getProperty("db.password"); // пароль из файла .pgpass

            connection = DriverManager.getConnection(url, user, password);
            Server.logger.info("Connected to database successfully");
        } catch (SQLException e) {
            Server.logger.severe("Failed to connect to database: " + e.getMessage());
        } catch (IOException e) {
            Server.logger.severe("Failed to load database properties: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static DBManagerInterface getInstance() {
        if (instance == null) {
            synchronized (DBManager.class) {
                if (instance == null) {
                    instance = new DBManager();
                    instance = TransactionalProxy.createProxy(instance, connection); // оборачиваем в прокси для поддержки транзакций
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
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getFirst());
            ResultSet res = p.executeQuery();
            if (res.next() && res.getBoolean(1)) {
                return new ExecutionStatus(true, "Пользователь успешно найден!");
            } else {
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
            try (PreparedStatement p = connection.prepareStatement(query)) {
                p.setString(1, user.getFirst());
                ResultSet res = p.executeQuery();
                if (res.next()) {
                    String password = res.getString("password");
                    boolean match = password.equals(user.getSecond());
                    if (match) {
                        return new ExecutionStatus(true, "Login successful!");
                    } else {
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

    public ExecutionStatus updateUserPermissions(String username, PermissionType permission) {
        String query = "UPDATE users SET permissions = ? WHERE username = ?;";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, permission.name());
            p.setString(2, username);
            int affectedRows = p.executeUpdate();
            if (affectedRows > 0) {
                return new ExecutionStatus(true, "Права пользователя " + username + " успешно обновлены!");
            } else {
                return new ExecutionStatus(false, "Пользователь " + username + " не найден!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при обновлении прав пользователя в базе данных: " + e.getMessage());
        }
    }

    public ExecutionStatus checkUserPermission(Pair<String, String> user) {
        String query = "SELECT permissions FROM users WHERE username = ?;";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getFirst());
            ResultSet res = p.executeQuery();
            if (res.next()) {
                String permission = res.getString("permissions");
                return new ExecutionStatus(true, permission);
            } else {
                return new ExecutionStatus(false, "Пользователь не найден!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при проверке прав пользователя в базе данных: " + e.getMessage());
        }
    }

    public ExecutionStatus clear(Pair<String, String> user) {
        String query = "DELETE FROM music_bands WHERE user_id IN (SELECT id FROM users WHERE username = ?);";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setString(1, user.getFirst());
            int affectedRows = p.executeUpdate();
            if (affectedRows > 0) {
                return new ExecutionStatus(true, "Успешно удалено " + affectedRows + " элементов пользователя " + user.getFirst() + "!");
            } else {
                return new ExecutionStatus(true, "У пользователя " + user.getFirst() + " нет элементов в коллекции!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при очистке коллекции в базе данных: " + e.getMessage());
        }
    }

    public ExecutionStatus clearAll() {
        String query = "TRUNCATE music_bands CASCADE;";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
            return new ExecutionStatus(true, "Коллекция успешно очищена!");
        } catch (SQLException e) {
            return new ExecutionStatus(false, "Ошибка при очистке коллекции: " + e.getMessage());
        }
    }

    public ExecutionStatus removeById(Long id, Pair<String, String> user) {
        ExecutionStatus accessStatus = checkUserPermission(user);
        if (!accessStatus.isSuccess()) {
            return accessStatus;
        }
        String query;
        if (accessStatus.getMessage().equals("USER")) {
            query = "DELETE FROM music_bands WHERE id = ? AND user_id IN (SELECT id FROM users WHERE username = ?);";
        }
        else {
            query = "DELETE FROM music_bands WHERE id = ?;";
        }
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setLong(1, id);
            if (accessStatus.getMessage().equals("USER")) {
                p.setString(2, user.getFirst());
            }
            int affectedRows = p.executeUpdate();
            if (affectedRows > 0) {
                return new ExecutionStatus(true, "Элемент успешно удалён!");
            } else {
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
                return new ExecutionStatus(true, "Успешно удалено " + affectedRows + " элементов с жанром " + genre + "!");
            } else {
                return new ExecutionStatus(false, "Доступные для удаления элементы с указанным genre не найдены!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при удалении элемента коллекции из базы данных: " + e.getMessage());
        }
    }

    public ExecutionStatus removeAllByGenre(MusicGenre genre) {
        String query = "DELETE FROM music_bands WHERE genre_id = ?;";
        try (PreparedStatement p = connection.prepareStatement(query)) {
            p.setLong(1, genre.ordinal() + 1);
            int affectedRows = p.executeUpdate();
            if (affectedRows > 0) {
                return new ExecutionStatus(true, "Успешно удалено " + affectedRows + " элементов с жанром " + genre + "!");
            } else {
                return new ExecutionStatus(false, "Доступные для удаления элементы с указанным genre не найдены!");
            }
        } catch (SQLException | NullPointerException e) {
            return new ExecutionStatus(false, "Ошибка при удалении элемента коллекции из базы данных: " + e.getMessage());
        }
    }

    @Transactional
    public ExecutionStatus addMusicBand(MusicBand band, Pair<String, String> user) throws SQLException {
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
            } else {
                throw new SQLException();
            }
        }
    }

    @Transactional
    public ExecutionStatus updateMusicBand(MusicBand band, Pair<String, String> user) throws SQLException {
        int coordinatesId;
        int studioId;

        ExecutionStatus accessStatus = checkUserPermission(user);
        if (!accessStatus.isSuccess()) {
            return accessStatus;
        }
        String updateBandQuery;
        if (accessStatus.getMessage().equals("USER")) {
            updateBandQuery = "UPDATE music_bands SET name = ?, number_of_participants = ?, albums_count = ?, description = ?, genre_id = ? WHERE id = ? AND user_id IN (SELECT id FROM users WHERE username = ?) RETURNING coordinates_id, studio_id";
        }
        else {
            updateBandQuery = "UPDATE music_bands SET name = ?, number_of_participants = ?, albums_count = ?, description = ?, genre_id = ? WHERE id = ? RETURNING coordinates_id, studio_id";
        }

        String checkQuery = "SELECT COUNT(*) FROM music_bands WHERE id = ?;";
        // Обновление самой музыкальной группы
        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
             PreparedStatement bandStmt = connection.prepareStatement(updateBandQuery)) {
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
            if (accessStatus.getMessage().equals("USER")) {
            bandStmt.setString(7, user.getFirst());
            }
            ResultSet rs = bandStmt.executeQuery();
            if (rs.next()) {
                coordinatesId = rs.getInt("coordinates_id");
                studioId = rs.getInt("studio_id");
            } else {
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
    }

    /**
     * Загружает коллекцию музыкальных групп из базы данных.
     *
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
                MusicBand band = new MusicBandBuilder()
                        .setId(res.getLong("id"))
                        .setName(res.getString("band_name"))
                        .setCoordinates(new Coordinates(res.getDouble("coordinates_x"), res.getInt("coordinates_y")))
                        .setCreationDate(res.getTimestamp("creation_date").toLocalDateTime())
                        .setNumberOfParticipants(res.getLong("number_of_participants"))
                        .setAlbumsCount(res.getLong("albums_count"))
                        .setDescription(res.getString("description"))
                        .setGenre(MusicGenre.valueOf(res.getString("genre_name")))
                        .setStudio(new Studio(res.getString("studio_name"), res.getString("studio_address")))
                        .setUser(res.getString("username"))
                        .build();
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
