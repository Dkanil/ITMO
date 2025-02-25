package managers;

import models.MusicBand;
import utility.Console;
import com.opencsv.*;

import java.io.*;
import java.util.Locale;
import java.util.Stack;

public class DumpManager {
    private final String fileName;
    private final Console console;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    public void WriteCollection (Stack<MusicBand> collection) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName)); // TODO: костыли с параметрами, по документации не работает
            for (MusicBand band : collection) {
                writer.writeNext(MusicBand.toArray(band));
            }
            writer.close();
        } catch (IOException e) { // TODO добавить обработку ошибок
            console.println("Произошла ошибка при записи коллекции в файл!");
        }
    }

    public void ReadCollection (Stack<MusicBand> collection) {
        InputStreamReader input = null;
        try {
            input = new InputStreamReader(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (CSVReader reader = new CSVReader(input)) {
            // TODO проверить правильность чтения из файла, поменять разделитель на ;
            String[] line;
            while ((line = reader.readNext()) != null) {
                MusicBand buf = MusicBand.fromArray(line);
                if (buf.validate())
                {
                    collection.push(buf);
                }
                else
                {
                    console.println("Введены некорректные данные элемента коллекции!");
                }
            }
        } catch (Exception e) { // TODO добавить обработку ошибок
            console.printError("Произошла ошибка при чтении коллекции из файла!");
        }
    }
}
