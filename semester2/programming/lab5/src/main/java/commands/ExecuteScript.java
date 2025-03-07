package commands;

import utility.*;

/**
 * Класс команды для выполнения скрипта из указанного файла.
 */
public class ExecuteScript extends Command {

    /**
     * Конструктор команды executeScript.
     * @param console Консоль для ввода/вывода.
     */
    public ExecuteScript(Console console) {
        super(CommandNames.EXECUTE_SCRIPT.getName() + " file_name", CommandNames.EXECUTE_SCRIPT.getDescription(), console);
    }

    /**
     * Проверяет корректность аргументов команды.
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        return null;
    }

    /**
     * Выполняет команду выполнения скрипта из указанного файла.
     * @param arg Имя файла, содержащего скрипт.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String arg) {
        return null;
    }
}