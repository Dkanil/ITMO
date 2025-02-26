package commands;

import utility.*;

/**
 * Класс команды для выполнения скрипта из указанного файла.
 */
public class executeScript extends Command {
    public final Console console;

    /**
     * Конструктор команды executeScript.
     * @param console Консоль для ввода/вывода.
     */
    public executeScript(Console console) {
        super("execute_script file_name", "считать и исполнить скрипт из указанного файла.");
        this.console = console;
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