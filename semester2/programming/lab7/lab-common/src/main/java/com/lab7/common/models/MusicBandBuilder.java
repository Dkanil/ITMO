package com.lab7.common.models;

import java.time.LocalDateTime;

public class MusicBandBuilder {
    private Long id = 1L; // Значение по умолчанию, так как id присваивается в БД
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Long numberOfParticipants;
    private Long albumsCount;
    private String description;
    private MusicGenre genre;
    private Studio studio;
    private String user;

    public MusicBandBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public MusicBandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MusicBandBuilder setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public MusicBandBuilder setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public MusicBandBuilder setNumberOfParticipants(Long numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
        return this;
    }

    public MusicBandBuilder setAlbumsCount(Long albumsCount) {
        this.albumsCount = albumsCount;
        return this;
    }

    public MusicBandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public MusicBandBuilder setGenre(MusicGenre genre) {
        this.genre = genre;
        return this;
    }

    public MusicBandBuilder setStudio(Studio studio) {
        this.studio = studio;
        return this;
    }

    public MusicBandBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public MusicBand build() {
        MusicBand band = new MusicBand(
                id,
                name,
                coordinates,
                creationDate,
                numberOfParticipants,
                albumsCount,
                description,
                genre,
                studio,
                user
        );
        if (band.validate()){
            return band;
        }
        else {
            throw new IllegalArgumentException("Некорректные данные для создания объекта MusicBand");
        }
    }
}
