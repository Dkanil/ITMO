package com.lab7.client.managers;

import com.lab7.client.Client;
import com.lab7.client.utility.FileConsole;
import com.lab7.client.utility.Console;
import com.lab7.common.models.MusicBand;
import com.lab7.common.models.MusicGenre;
import com.lab7.common.models.Coordinates;
import com.lab7.common.models.Studio;
import com.lab7.common.models.MusicBandBuilder;

import java.time.LocalDateTime;

/**
 * Класс, запрашивающий у пользователя данные для создания объектов.
 */
public class Asker {
    /**
     * Исключение, выбрасываемое для прерывания ввода.
     */
    public static class Breaker extends Exception {
    }

    /**
     * Исключение, выбрасываемое при некорректном вводе.
     */
    public static class IllegalInputException extends IllegalArgumentException {
        /**
         * Конструктор исключения с сообщением.
         *
         * @param message Сообщение об ошибке.
         */
        public IllegalInputException(String message) {
            super(message);
        }
    }

    /**
     * Запрашивает у пользователя данные для создания объекта MusicBand.
     *
     * @param console Консоль для ввода/вывода.
     * @return Объект MusicBand с введенными данными.
     * @throws Breaker               Исключение, выбрасываемое при вводе команды "exit".
     * @throws IllegalInputException Исключение, выбрасываемое при некорректном вводе.
     */
    public static MusicBand askBand(Console console) throws Breaker, IllegalInputException {
        MusicBandBuilder builder = new MusicBandBuilder();
        String name;
        do {
            console.println("Введите значение поля name:");
            name = console.readln();
            if (name.equals("exit")) {
                throw new Breaker();
            }
        } while (name.isEmpty());
        builder.setName(name);

        builder.setCoordinates(askCoordinates(console));

        Long numberOfParticipants;
        do {
            console.println("Введите значение поля numberOfParticipants:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    numberOfParticipants = Long.valueOf(input);
                }
            } catch (NumberFormatException e) {
                numberOfParticipants = -1L;
            }
            if (numberOfParticipants <= 0) {
                if (console instanceof FileConsole) {
                    throw new IllegalInputException("Некорректное значение поля numberOfParticipants!\nЗначение поля должно быть больше 0");
                }
                console.printError("Некорректное значение поля numberOfParticipants!\nЗначение поля должно быть больше 0");
            }
        } while (numberOfParticipants <= 0);
        builder.setNumberOfParticipants(numberOfParticipants);

        Long albumsCount;
        do {
            console.println("Введите значение поля albumsCount:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    albumsCount = Long.valueOf(input);
                }
            } catch (NumberFormatException e) {
                albumsCount = -1L;
            }
            if (albumsCount <= 0) {
                if (console instanceof FileConsole) {
                    throw new IllegalInputException("Некорректное значение поля albumsCount!\nЗначение поля должно быть больше 0");
                }
                console.printError("Некорректное значение поля albumsCount!\nЗначение поля должно быть больше 0");
            }
        } while (albumsCount <= 0);
        builder.setAlbumsCount(albumsCount);

        String description;
        do {
            console.println("Введите значение поля description:");
            description = console.readln();
            if (description.equals("exit")) {
                throw new Breaker();
            }
        } while (description.isEmpty());
        builder.setDescription(description);

        MusicGenre genre = null;
        do {
            console.println("Введите значение поля genre:");
            console.println("Список возможных значений: " + MusicGenre.list());
            String input = console.readln();
            if (input.equals("exit")) {
                throw new Breaker();
            } else {
                try {
                    genre = MusicGenre.valueOf(input);
                } catch (IllegalArgumentException e) {
                    if (console instanceof FileConsole) {
                        throw new IllegalArgumentException("Некорректное значение поля genre!");
                    }
                    console.printError("Некорректное значение поля genre!");
                }
            }
        } while (genre == null);
        builder.setGenre(genre);
        builder.setStudio(askStudio(console));

        return builder.setUser(Client.user.getFirst()).setCreationDate(LocalDateTime.now()).build();
    }

    /**
     * Запрашивает у пользователя данные для создания объекта Coordinates.
     *
     * @param console Консоль для ввода/вывода.
     * @return Объект Coordinates с введенными данными.
     * @throws Breaker               Исключение, выбрасываемое при вводе команды "exit".
     * @throws IllegalInputException Исключение, выбрасываемое при некорректном вводе.
     */
    private static Coordinates askCoordinates(Console console) throws Breaker, IllegalInputException {
        console.println("Ввод значений поля Coordinates...");
        double x;
        do {
            console.println("Введите значение поля x:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    x = Double.parseDouble(input);
                }
            } catch (NumberFormatException e) {
                x = -981;
            }
            if (x <= -980) {
                if (console instanceof FileConsole) {
                    throw new IllegalInputException("Некорректное значение поля x!\nЗначение поля должно быть больше -980");
                }
                console.printError("Некорректное значение поля x!\nЗначение поля должно быть больше -980");
            }
        } while (x <= -980);

        Integer y;
        do {
            console.println("Введите значение поля y:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    y = Integer.valueOf(input);
                }
            } catch (NumberFormatException e) {
                y = 296;
            }
            if (y > 295) {
                if (console instanceof FileConsole) {
                    throw new IllegalInputException("Некорректное значение поля y!\nМаксимальное значение поля: 295");
                }
                console.printError("Некорректное значение поля y!\nМаксимальное значение поля: 295");
            }
        } while (y > 295);

        console.println("Значения поля Coordinates записаны...");
        return new Coordinates(x, y);
    }

    /**
     * Запрашивает у пользователя данные для создания объекта Studio.
     *
     * @param console Консоль для ввода/вывода.
     * @return Объект Studio с введенными данными.
     * @throws Breaker Исключение, выбрасываемое при вводе команды "exit".
     */
    private static Studio askStudio(Console console) throws Breaker {
        console.println("Ввод значений поля Studio...");
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

        console.println("Значения поля Studio записаны...");
        return new Studio(name, address);
    }
}
