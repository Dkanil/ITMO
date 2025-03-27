package managers;

import models.*;
import utility.*;

import java.time.LocalDateTime;

/**
 * Класс, запрашивающий у пользователя данные для создания объектов.
 */
public class Asker {
    /**
     * Исключение, выбрасываемое для прерывания ввода.
     */
    public static class Breaker extends Exception { }

    /**
     * Исключение, выбрасываемое при некорректном вводе.
     */
    public static class IllegalInputException extends IllegalArgumentException {
        /**
         * Конструктор исключения с сообщением.
         * @param message Сообщение об ошибке.
         */
        public IllegalInputException(String message) {
            super(message);
        }
    }

    /**
     * Запрашивает у пользователя данные для создания объекта MusicBand.
     * @param console Консоль для ввода/вывода.
     * @param id Идентификатор музыкальной группы.
     * @return Объект MusicBand с введенными данными.
     * @throws Breaker Исключение, выбрасываемое при вводе команды "exit".
     * @throws IllegalInputException Исключение, выбрасываемое при некорректном вводе.
     */
    public static MusicBand askBand(Console console, Long id) throws Breaker, IllegalInputException {
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
                } else {
                    numberOfParticipants = Long.valueOf(input);
                }
            } catch (NumberFormatException e) {
                numberOfParticipants = -1L;
            }
            if (numberOfParticipants <= 0) {
                if (console instanceof FileConsole){
                    throw new IllegalInputException("Некорректное значение поля numberOfParticipants!\nЗначение поля должно быть больше 0");
                }
                console.printError("Некорректное значение поля numberOfParticipants!\nЗначение поля должно быть больше 0");
            }
        } while (numberOfParticipants <= 0);

        Long albumsCount = -1L;
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
                if (console instanceof FileConsole){
                    throw new IllegalInputException("Некорректное значение поля albumsCount!\nЗначение поля должно быть больше 0");
                }
                console.printError("Некорректное значение поля albumsCount!\nЗначение поля должно быть больше 0");
            }
        } while (albumsCount <= 0);

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
            console.println("Введите значение поля genre:");
            console.println("Список возможных значений: " + MusicGenre.list());
            String input = console.readln();
            if (input.equals("exit")) {
                throw new Breaker();
            } else {
                try {
                    genre = MusicGenre.valueOf(input);
                } catch (IllegalArgumentException e) {
                    if (console instanceof FileConsole){
                        throw new IllegalArgumentException("Некорректное значение поля genre!");
                    }
                    console.printError("Некорректное значение поля genre!");
                }
            }
        } while (genre == null);

        Studio studio = askStudio(console);

        return new MusicBand(id, name, LocalDateTime.now(), numberOfParticipants, description, coordinates, albumsCount, genre, studio);
    }

    /**
     * Запрашивает у пользователя данные для создания объекта Coordinates.
     * @param console Консоль для ввода/вывода.
     * @return Объект Coordinates с введенными данными.
     * @throws Breaker Исключение, выбрасываемое при вводе команды "exit".
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
                if (console instanceof FileConsole){
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
                if (console instanceof FileConsole){
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