package commands;

import utility.*;

/**
 * Класс команды для завершения программы (без сохранения в файл).
 */
public class exit extends Command {
    Console console;

    /**
     * Конструктор команды exit.
     * @param console Консоль для ввода/вывода.
     */
    public exit(Console console) {
        super("exit", "завершить программу (без сохранения в файл)");
        this.console = console;
    }

    /**
     * Выполняет команду завершения программы.
     * @param arg Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String arg) {
        if (!arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды exit нет аргументов!!\nПример корректного ввода: " + getName());
        }
        System.exit(0);
        return new ExecutionStatus(true, "Программа завершена!");
    }
}