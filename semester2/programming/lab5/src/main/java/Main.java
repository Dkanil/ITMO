import models.*;
import utility.*;

import java.util.Stack;
import managers.*;
import utility.*;
import commands.*;
import models.*;

// Вариант 4192

public class Main {
    public static void main(String[] args) {
        var console = new StandartConsole();
        String filePath = System.getenv("LAB5_FILE_PATH");
        if (filePath == null) {
            console.printError("Переменная окружения LAB5_FILE_PATH не найдена!");
            System.exit(1);
        } else if (filePath.isEmpty()) {
            console.printError("Переменная окружения LAB5_FILE_PATH не содержит пути к файлу!");
            System.exit(1);
        } else if (!filePath.endsWith(".csv")) {
            console.printError("Файл должен быть формата .csv!");
            System.exit(1);
        } else if (!new java.io.File(filePath).exists()) {
            console.printError("Файл по указанному пути не найден!");
            System.exit(1);
        }

        DumpManager dumpManager = new DumpManager(filePath, console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);

        CommandManager commandManager = new CommandManager() {{
            register("help", new help(console, this));

            register("show", new show(console, collectionManager));
            register("add", new add(console, collectionManager));

            register("execute_script", new executeScript(console));

        }};
        new Executer(console).interactiveMode();
    }
}