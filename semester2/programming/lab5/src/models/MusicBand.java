package models;

import utility.*;
import java.time.LocalDateTime;

public class MusicBand extends Element {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long numberOfParticipants; //Поле может быть null, Значение поля должно быть больше 0
    private Long albumsCount; //Поле может быть null, Значение поля должно быть больше 0
    private String description; //Поле не может быть null
    private MusicGenre genre; //Поле может быть null
    private Studio studio; //Поле не может быть null

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

    @Override
    public String toString() {
        return "models.MusicBand{" +
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

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public int compareTo(Element o) {
        return (int) (this.id - o.getID());
    }
}
