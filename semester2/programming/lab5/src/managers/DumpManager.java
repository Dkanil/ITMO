package managers;

import models.MusicBand;
import utility.Console;
import com.opencsv.*;

import java.io.FileInputStream;
import java.util.Stack;
import java.io.InputStreamReader;
import java.io.FileWriter;

import java.io.IOException;

public class DumpManager {
    private final String fileName;
    private final Console console;

    public DumpManager(String fileName, Console console) {
        this.fileName = fileName;
        this.console = console;
    }

    public void WriteCollection (Stack<MusicBand> collection) {
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName), ';', '"', '"', "\n"); // TODO: костыли с параметрами, по документации не работает
            for (MusicBand band : collection) {
                writer.writeNext(MusicBand.toArray(band));
            }
            writer.close();
        } catch (IOException e) { // TODO добавить обработку ошибок
            console.println("Произошла ошибка при записи коллекции в файл!");
        }
    }

    public void ReadCollection (Stack<MusicBand> collection) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(fileName))); // TODO проверить правильность чтения из файла, поменять разделитель на ;
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
            console.println("Произошла ошибка при чтении коллекции из файла!");
        }
    }
}
