import utility.*;
import managers.*;
import commands.*;

/**
 * Главный класс программы.
 * Вариант 4192
 */
public class Main {
    public static void main(String[] args) {
        var console = new StandartConsole();
        String filePath = System.getenv("LAB5_FILE_PATH");

        // Проверка наличия и корректности переменной окружения LAB5_FILE_PATH
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
        ExecutionStatus loadStatus = collectionManager.loadCollection();

        // Проверка успешности загрузки коллекции
        if (!loadStatus.isSuccess()){
            console.printError(loadStatus.getMessage());
            System.exit(1);
        }

        // Регистрация команд
        CommandManager commandManager = new CommandManager() {{
            register("help", new help(console, this));
            register("info", new info(console, collectionManager));
            register("show", new show(console, collectionManager));
            register("add", new add(console, collectionManager));
            register("update", new update(console, collectionManager));
            register("remove_by_id", new removeById(console, collectionManager));
            register("clear", new clear(console, collectionManager));
            register("save", new save(console, collectionManager));
            register("execute_script", new executeScript(console));
            register("exit", new exit(console));
            register("remove_first", new removeFirst(console, collectionManager));
            register("add_if_min", new addIfMin(console, collectionManager));
            register("sort", new sort(console, collectionManager));
            register("remove_all_by_genre", new removeAllByGenre(console, collectionManager));
            register("print_field_ascending_description", new printFieldAscendingDescription(console, collectionManager));
            register("print_field_descending_description", new printFieldDescendingDescription(console, collectionManager));
        }};

        // Запуск интерактивного режима
        new Executer(console).interactiveMode();
    }
}