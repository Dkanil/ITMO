package com.lab6.common.models;

import com.lab6.common.utility.Element;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий музыкальную группу.
 */
public class MusicBand extends Element implements Serializable {
    @Serial
    private static final long serialVersionUID = 10L;

    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private LocalDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long numberOfParticipants; // Поле может быть null, Значение поля должно быть больше 0
    private Long albumsCount; // Поле может быть null, Значение поля должно быть больше 0
    private String description; // Поле не может быть null
    private MusicGenre genre; // Поле может быть null
    private Studio studio; //Поле не может быть null

    /**
     * Конструктор для создания объекта MusicBand.
     *
     * @param id                   идентификатор группы, не может быть null
     * @param name                 название группы, не может быть null
     * @param creationDate         дата создания группы, не может быть null
     * @param numberOfParticipants количество участников, может быть null
     * @param description          описание группы, не может быть null
     * @param coordinates          координаты группы, не может быть null
     * @param albumsCount          количество альбомов, может быть null
     * @param genre                жанр музыки, может быть null
     * @param studio               студия, не может быть null
     */
    public MusicBand(Long id, String name, LocalDateTime creationDate, Long numberOfParticipants, String description, Coordinates coordinates, Long albumsCount, MusicGenre genre, Studio studio) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.description = description;
        this.coordinates = coordinates;
        this.albumsCount = albumsCount;
        this.genre = genre;
        this.studio = studio;
    }

    public void updateId(Long id) {
       this.id = id;
    }

    /**
     * Возвращает идентификатор группы.
     *
     * @return идентификатор группы
     */
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
        return Objects.hash(id, name, coordinates, creationDate, numberOfParticipants, albumsCount, description, genre, studio);
    }

    /**
     * Возвращает строковое представление объекта.
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return '{' +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", albumsCount=" + albumsCount +
                ", description='" + description + '\'' +
                ", genre=" + genre +
                ", studio=" + studio +
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
        if (studio == null || !studio.validate()) return false;
        return true;
    }

    /**
     * Возвращает идентификатор элемента.
     *
     * @return идентификатор элемента
     */
    @Override
    public Long getID() {
        return id;
    }

    /**
     * Сравнивает текущий объект с другим объектом.
     *
     * @param o объект для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Element o) {
        return (int) (this.id - o.getID());
    }

    /**
     * Создает объект MusicBand из массива строк.
     *
     * @param array массив строк
     * @return объект MusicBand
     */
    public static MusicBand fromArray(String[] array) {
        Long id;
        String name;
        Coordinates coordinates;
        LocalDateTime creationDate;
        Long numberOfParticipants;
        Long albumsCount;
        String description;
        MusicGenre genre;
        Studio studio;
        try {
            id = Long.parseLong(array[0]);
            name = array[1];
            coordinates = new Coordinates(Double.parseDouble(array[2]), Integer.parseInt(array[3]));
            creationDate = LocalDateTime.parse(array[4]);
            numberOfParticipants = Long.parseLong(array[5]);
            albumsCount = Long.parseLong(array[6]);
            description = array[7];
            genre = MusicGenre.valueOf(array[8]);
            studio = new Studio(array[9], array[10]);
        } catch (Exception e) {
            return null;
        }
        return new MusicBand(id, name, creationDate, numberOfParticipants, description, coordinates, albumsCount, genre, studio);
    }

    /**
     * Преобразует объект MusicBand в массив строк.
     *
     * @param band объект MusicBand
     * @return массив строк
     */
    public static String[] toArray(MusicBand band) {
        return new String[]{
                band.getId().toString(),
                band.getName(),
                Double.toString(band.getCoordinates().getX()),
                band.getCoordinates().getY().toString(),
                band.getCreationDate().toString(),
                band.getNumberOfParticipants().toString(),
                band.getAlbumsCount().toString(),
                band.getDescription(),
                band.getGenre().toString(),
                band.getStudio().getName(),
                band.getStudio().getAddress()
        };
    }
}
