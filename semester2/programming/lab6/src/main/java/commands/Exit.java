package commands;

import commands.validators.EmptyValidator;
import utility.*;

/**
 * Класс команды для завершения программы (без сохранения в файл).
 */
public class Exit extends Command<EmptyValidator> {

    /**
     * Конструктор команды exit.
     * @param console Консоль для ввода/вывода.
     */
    public Exit(Console console) {
        super(CommandNames.EXIT.getName(), CommandNames.EXIT.getDescription(), console, new EmptyValidator());
    }

    /**
     * Выполняет команду завершения программы.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        System.exit(0);
        return new ExecutionStatus(true, "Программа завершена!");
    }
}