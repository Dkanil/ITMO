package commands;

import models.Coordinates;
import models.MusicBand;
import models.MusicGenre;
import models.Studio;
import utility.*;

import java.time.LocalDateTime;

public class Asker {
    public static class Breaker extends Exception { }

    public static MusicBand askBand(Console console, Long id) throws Breaker {
        String name;
        do {
            console.println("Введите значение поля name:");
            name = console.readln();
            if (name.equals("exit")) {
                throw new Breaker();
            }
        } while (name.isEmpty());

        Coordinates coordinates = askCoordinates(console);

        Long numberOfParticipants = -1L;
        do {
            console.println("Введите значение поля numberOfParticipants:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                }
                else {
                    numberOfParticipants = Long.valueOf(input);
                }
            }
            catch (NumberFormatException e) {
                console.println("Некорректное значение поля numberOfParticipants!\nЗначение поля должно быть больше 0");
                numberOfParticipants = -1L;
            }
        } while(numberOfParticipants <= 0);

        Long albumsCount = -1L;
        do {
            console.println("Введите значение поля albumsCount:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                }
                else {
                    albumsCount = Long.valueOf(input);
                }
            }
            catch (NumberFormatException e) {
                console.println("Некорректное значение поля albumsCount!\nЗначение поля должно быть больше 0");
                albumsCount = -1L;
            }
        } while(albumsCount <= 0);

        String description;
        do {
            console.println("Введите значение поля description:");
            description = console.readln();
            if (description.equals("exit")) {
                throw new Breaker();
            }
        } while (description.isEmpty());

        MusicGenre genre = null;
        do {
            console.println("Список возможных значений: " + MusicGenre.list());
            console.println("Введите значение поля genre:");
            String input = console.readln();
            if (input.equals("exit")) {
                throw new Breaker();
            }
            else {
                try {
                    genre = MusicGenre.valueOf(input);
                }
                catch (IllegalArgumentException e) {
                    console.println("Некорректное значение поля genre!\nСписок возможных значений: " + MusicGenre.list());
                }
            }
        } while (genre == null);

        Studio studio = askStudio(console);

        return new MusicBand(id, name, LocalDateTime.now(), numberOfParticipants, description, coordinates, albumsCount, genre, studio);
    }

    public static Coordinates askCoordinates(Console console) throws Breaker {
        console.println("Введите координаты:");

        double x;
        do {
            console.println("Введите значение поля x:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                }
                else {
                    x = Double.parseDouble(input);
                }
            }
            catch (NumberFormatException e) {
                console.println("Некорректное значение поля x!\nЗначение поля должно быть больше -980");
                x = -981;
            }
        } while (x <= -980);

        Integer y;
        do {
            console.println("Введите значение поля y:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                }
                else {
                    y = Integer.valueOf(input);
                }
            }
            catch (NumberFormatException e) {
                console.println("Некорректное значение поля y!\nМаксимальное значение поля: 295");
                y = 296;
            }
        } while (y > 295);

        return new Coordinates(x, y);
    }

    public static Studio askStudio(Console console) throws Breaker {
        String name;
        do {
            console.println("Введите значение поля name:");
            name = console.readln();
            if (name.equals("exit")) {
                throw new Breaker();
            }
        } while (name.isEmpty());

        String address;
        do {
            console.println("Введите значение поля address:");
            address = console.readln();
            if (address.equals("exit")) {
                throw new Breaker();
            }
        } while (address.isEmpty());

        return new Studio(name, address);
    }
}
