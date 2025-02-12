package models;

import utility.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Long getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public Long getAlbumsCount() {
        return albumsCount;
    }

    public String getDescription() {
        return description;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public Studio getStudio() {
        return studio;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MusicBand musicBand = (MusicBand) object;
        return Objects.equals(id, musicBand.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, numberOfParticipants, albumsCount, description, genre, studio);
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
        } catch (Exception e) { // TODO добавить обработку ошибок
            return null;
        }
        return new MusicBand(id, name, creationDate, numberOfParticipants, description, coordinates, albumsCount, genre, studio);
    }

    public static String[] toArray(MusicBand band) {
        return new String[] {
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
