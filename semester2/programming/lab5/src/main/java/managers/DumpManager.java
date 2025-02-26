package managers;

import models.MusicBand;
import utility.Console;
import com.opencsv.*;

import java.io.*;
import java.util.Stack;

/**
 * Класс, управляющий сохранением и загрузкой коллекции музыкальных групп.
 */
public class DumpManager {
    private final String fileName;
    private final Console console;

    /**
     * Конструктор для создания объекта DumpManager.
     * @param fileName имя файла для сохранения и загрузки коллекции
     * @param console консоль для вывода сообщений
     */
    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    /**
     * Сохраняет коллекцию музыкальных групп в файл.
     * @param collection коллекция музыкальных групп
     */
    public void WriteCollection(Stack<MusicBand> collection) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            for (MusicBand band : collection) {
                writer.writeNext(MusicBand.toArray(band));
            }
            writer.close();
        } catch (IOException e) {
            console.println("Произошла ошибка при записи коллекции в файл!");
        }
    }

    /**
     * Загружает коллекцию музыкальных групп из файла.
     * @param collection коллекция музыкальных групп
     */
    public void ReadCollection(Stack<MusicBand> collection) {
        InputStreamReader input = null;
        try {
            input = new InputStreamReader(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (CSVReader reader = new CSVReader(input)) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                MusicBand buf = MusicBand.fromArray(line);
                if (buf != null && buf.validate()) {
                    collection.push(buf);
                } else {
                    console.printError("Введены некорректные данные элемента коллекции!");
                }
            }
        } catch (Exception e) {
            console.printError("Произошла ошибка при чтении коллекции из файла!");
        }
    }
}