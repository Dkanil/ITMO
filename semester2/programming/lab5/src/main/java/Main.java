import commands.askingCommands.*;
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
            register(CommandNames.HELP.getName(), new Help(console, this));
            register(CommandNames.INFO.getName(), new Info(console, collectionManager));
            register(CommandNames.SHOW.getName(), new Show(console, collectionManager));
            register(CommandNames.ADD.getName(), new Add(console, collectionManager));
            register(CommandNames.UPDATE.getName(), new Update(console, collectionManager));
            register(CommandNames.REMOVE_BY_ID.getName(), new RemoveById(console, collectionManager));
            register(CommandNames.CLEAR.getName(), new Clear(console, collectionManager));
            register(CommandNames.SAVE.getName(), new Save(console, collectionManager));
            register(CommandNames.EXECUTE_SCRIPT.getName(), new ExecuteScript(console));
            register(CommandNames.EXIT.getName(), new Exit(console));
            register(CommandNames.REMOVE_FIRST.getName(), new RemoveFirst(console, collectionManager));
            register(CommandNames.ADD_IF_MIN.getName(), new AddIfMin(console, collectionManager));
            register(CommandNames.SORT.getName(), new Sort(console, collectionManager));
            register(CommandNames.REMOVE_ALL_BY_GENRE.getName(), new RemoveAllByGenre(console, collectionManager));
            register(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), new PrintFieldAscendingDescription(console, collectionManager));
            register(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), new PrintFieldDescendingDescription(console, collectionManager));
        }};

        // Запуск интерактивного режима
        new Executer(console).interactiveMode();
    }
}