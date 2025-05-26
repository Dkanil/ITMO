package com.lab7.common.models;

import com.lab7.common.utility.Element;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий музыкальную группу.
 */
public class MusicBand extends Element implements Serializable {
    @Serial
    private static final long serialVersionUID = 20L;
    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private final LocalDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long numberOfParticipants; // Поле может быть null, Значение поля должно быть больше 0
    private Long albumsCount; // Поле может быть null, Значение поля должно быть больше 0
    private String description; // Поле не может быть null
    private MusicGenre genre; // Поле может быть null
    private Studio studio; //Поле не может быть null
    private final String user;

    protected MusicBand(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, Long numberOfParticipants, Long albumsCount, String description, MusicGenre genre, Studio studio, String user) {
        this.id = id; //Ставится значение по умолчанию, так как id присваивается в БД
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.albumsCount = albumsCount;
        this.description = description;
        this.genre = genre;
        this.studio = studio;
        this.user = user;
    }

    public void updateId(Long id) {
        this.id = id;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void updateNumberOfParticipants(Long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public void updateAlbumsCount(Long albumsCount) {
        this.albumsCount = albumsCount;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public void updateStudio(Studio studio) {
        this.studio = studio;
    }

    /**
     * Возвращает идентификатор элемента.
     *
     * @return идентификатор элемента
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Возвращает название группы.
     *
     * @return название группы
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает координаты группы.
     *
     * @return координаты группы
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Возвращает дату создания группы.
     *
     * @return дата создания группы
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает количество участников группы.
     *
     * @return количество участников группы
     */
    public Long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    /**
     * Возвращает количество альбомов группы.
     *
     * @return количество альбомов группы
     */
    public Long getAlbumsCount() {
        return albumsCount;
    }

    /**
     * Возвращает описание группы.
     *
     * @return описание группы
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает жанр музыки группы.
     *
     * @return жанр музыки группы
     */
    public MusicGenre getGenre() {
        return genre;
    }

    /**
     * Возвращает студию группы.
     *
     * @return студия группы
     */
    public Studio getStudio() {
        return studio;
    }

    /**
     * Возвращает пользователя, которому принадлежит группа.
     *
     * @return пользователь группы
     */
    public String getUser() {
        return user;
    }

    /**
     * Проверяет равенство текущего объекта с другим объектом.
     *
     * @param object объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MusicBand musicBand = (MusicBand) object;
        return Objects.equals(id, musicBand.id);
    }

    /**
     * Возвращает хэш-код объекта.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, numberOfParticipants, albumsCount, description, genre, studio, user);
    }

    /**
     * Возвращает строковое представление объекта.
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", albumsCount=" + albumsCount +
                ", description='" + description + '\'' +
                ", genre=" + genre +
                ", studio=" + studio +
                ", user='" + user + '\'' +
                '}';
    }

    /**
     * Проверяет валидность объекта.
     */
    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (creationDate == null) return false;
        if (numberOfParticipants <= 0) return false;
        if (albumsCount <= 0) return false;
        if (description == null) return false;
        if (user == null) return false;
        return studio != null && studio.validate();
    }

    /**
     * Сравнивает текущий объект с другим объектом.
     *
     * @param o объект для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Element o) {
        return (int) (this.id - o.getId());
    }
}
